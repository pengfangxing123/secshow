package cn.com.jrj.vtmatch.basicmatch.service;

import cn.com.jrj.vtmatch.basicmatch.dto.MatchTeamRule;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.TradingMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.AccountTradeTimes;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchBasic;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamMemberStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccountStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.Stat;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.StatRank;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 清算任务主服务类
 *
 * @author yuan.cheng
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MatchStatService {

    private static final int YIELD_RATE_SCALE = 5;
    private static final int DEFAULT_TEAM_MEMBER_THRESHOLD = 40;
    private static final int DEFAULT_TEAM_MEMBER_CALCULATE_RATE = 50;

    private final MatchInfoService matchInfoService;
    private final VirtualTradeDataService virtualTradeDataService;
    private final MatchStatDataService matchStatDataService;
    private final ExchangeCalendarService exchangeCalendarService;
    private final TradingMapper tradingMapper;
    private final ObjectMapper objectMapper;

    /**
     * 交易次数统计
     *
     * @param date 清算日期
     */
    private void tradeTimesStat(LocalDate date) {
        List<AccountTradeTimes> accountTradeTimesList = tradingMapper.selectAccountTradeTimesByDate(date.plusDays(1).atStartOfDay());
        int recordSize = matchInfoService.updateAccountTradeTimes(accountTradeTimesList);
        log.info("update account trade times success. record size {}", recordSize);

        List<MatchBasic> matchList = matchInfoService.queryAvailableMatchByIds(date, null);
        int updateCount = matchList.stream().mapToInt(match -> {
            List<AccountTradeTimes> tradeTimeList =
                tradingMapper.selectAccountTradeTimesByRange(match.getStartDate().atStartOfDay(),
                    date.plusDays(1).atStartOfDay());
            return matchInfoService.updateMatchTradeTimes(match.getId(), tradeTimeList);
        }).sum();
        log.info("match account trade times update success, records size {}", updateCount);
    }

    /**
     * 清算所有比赛.
     *
     * @param date 清算日期
     */
    public void statAllMatchByDate(final LocalDate date) {
        Assert.notNull(date, "stat date must not be null!");
        if (!exchangeCalendarService.isTradeDay(date)) {
            log.warn("{} is not trade date, skip stat!", date);
            return;
        }
        //统计交易次数
        tradeTimesStat(date);
        //查询所有有效比赛
        List<MatchBasic> matchInfoList = matchInfoService.queryAvailableMatchByIds(date, null);

        matchStat(date, matchInfoList);
    }

    public void reStatMatch(final LocalDate date, List<Integer> matchIds, boolean reloadSource) {
        Assert.notNull(date, "stat date must not be null!");
        if (!exchangeCalendarService.isTradeDay(date)) {
            log.warn("{} is not trade date, skip stat!", date);
            return;
        }
        if (reloadSource) {
            matchStatDataService.clearMatchUserAccountStatRankByDate(date);
        }

        matchStatDataService.clearMatchUserStatRankByMatchIdAndDate(date, matchIds);
        matchStatDataService.clearMatchTeamStatRankByMatchIdAndDate(date, matchIds);
        matchStatDataService.clearMatchTeamMemberStatRankByMatchIdAndDate(date, matchIds);
        //统计交易次数
        tradeTimesStat(date);
        List<MatchBasic> matchInfoList = matchInfoService.queryAvailableMatchByIds(date, matchIds);
        matchStat(date, matchInfoList);
    }

    private void matchStat(final LocalDate date, List<MatchBasic> matchInfoList) {
        final long start = System.currentTimeMillis();
        //从数据源拉取数据的情况则计算账户排名入库.
        if (virtualTradeDataService.loadDayStatLog(date)) {
            matchStatDataService.loadUserAccountPrevData(date);
            List<MatchUserAccountStatRank> userAccountStatList = virtualTradeDataService.queryAllAccountStat().stream()
                .peek(stat -> dealWithLastRank(stat, matchStatDataService.queryUserAccountStatRankByAccountId(stat.getAccountId())))
                .collect(Collectors.toList());
            calculateRank(userAccountStatList);
            matchStatDataService.clearData();
            int result = matchStatDataService.saveMatchUserAccountStatData(userAccountStatList);
            log.info("user account stat for {} finished. record {}", date, result);
        }

        matchInfoList.forEach(matchInfo -> {
            Future<Integer> userStatResult = this.matchUserStat(matchInfo, date);
            if (matchInfo.getType() == 2) {
                List<Future<Integer>> resultList = new ArrayList<>(this.matchTeamStat(matchInfo, date));
                resultList.add(0, userStatResult);
                printStatResult(matchInfo, date, resultList);
            } else {
                printStatResult(matchInfo, date, Collections.singletonList(userStatResult));
            }
            log.info("match {} stat for {} finished cost {}ms", matchInfo.getId(), date, System.currentTimeMillis() - start);
        });

        virtualTradeDataService.clearData();
        log.info("match stat for {} finished, cost {}ms", date, System.currentTimeMillis() - start);
    }

    private void printStatResult(MatchBasic matchInfo, LocalDate date, List<Future<Integer>> results) {
        List<Integer> resultList = new ArrayList<>(3);
        final int timeout = 30;
        try {
            for (Future<Integer> integerFuture : results) {
                resultList.add(integerFuture.get(timeout, TimeUnit.MINUTES));
            }
            String summary = generateSummary(resultList);
            log.info("match {} stat for {} finished. {}", matchInfo.getId(), date, summary);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error(String.format("match %d stat for %s, execute error", matchInfo.getId(), date), e);
        } catch (TimeoutException e) {
            log.warn(String.format("match %d stat for %s, execute timeout after %d minutes", matchInfo.getId(), date, timeout), e);
        }
    }

    private String generateSummary(List<Integer> results) {
        if (results.size() == 1) {
            return String.format("match user stat record %d", results.get(0));
        } else if (results.size() == 3) {
            return String.format("match user stat record %d, match member stat record %d, match team stat record %d",
                results.get(0), results.get(1), results.get(2));
        } else {
            return "";
        }
    }

    /**
     * 比赛用户清算.
     *
     * @param matchInfo 比赛信息
     * @param date      清算日期
     */
    private Future<Integer> matchUserStat(MatchBasic matchInfo, LocalDate date) {
        matchStatDataService.loadMatchUserPrevData(matchInfo.getId(), date);
        List<MatchJoin> userList = matchInfoService.queryAvailableMatchJoin(matchInfo, date);
        List<MatchUserStatRank> statList = userList.parallelStream().map(user -> singleUserStat(user, date))
            .collect(Collectors.toList());

        calculateRank(statList);

        Future<Integer> result = matchStatDataService.saveMatchUserStatData(matchInfo.getId(), statList);
        matchStatDataService.clearData();
        return result;
    }

    /**
     * 比赛团队清算
     *
     * @param matchInfo 比赛信息
     * @param date      清算日期
     * @return 0：teamMemberStatResult， 1：matchTeamStatResult
     */
    private List<Future<Integer>> matchTeamStat(MatchBasic matchInfo, LocalDate date) {
        List<MatchTeamJoin> teamJoins = matchInfoService.queryAvailableTeamJoin(matchInfo.getId(), date);
        MatchTeamRule rule = parseMatchTeamRule(matchInfo);
        final int teamMemberThreshold = rule.getTeamMemberThreshold();
        final double teamMemberCalculateRate = rule.getTeamMemberCalculateRate() / 100D;

        matchStatDataService.loadMatchTeamMemberPrevData(matchInfo.getId(), date);
        List<MatchTeamMemberStatRank> teamMemberStatRanks = new ArrayList<>(teamJoins.size());
        Map<Long, MatchTeamStatRank> teamTodayStatMap = teamJoins.stream()
            .collect(Collectors.groupingBy(MatchTeamJoin::getTeamId,
                Collectors.collectingAndThen(Collectors.toList(), userList -> {
                    List<MatchTeamMemberStatRank> statList = userList.parallelStream()
                        .map(user -> singleTeamMemberStat(user, date)).collect(Collectors.toList());

                    calculateRank(statList);

                    teamMemberStatRanks.addAll(statList);

                    MatchTeamStatRank todayMatchTeamStatRank = new MatchTeamStatRank();
                    todayMatchTeamStatRank.setMatchId(matchInfo.getId());
                    todayMatchTeamStatRank.setTeamId(userList.get(0).getTeamId());
                    todayMatchTeamStatRank.setMemberNum((long) statList.size());
                    todayMatchTeamStatRank.setStatDate(date);
                    todayMatchTeamStatRank.setDayYield(BigDecimal.valueOf(statList.stream()
                        .sorted(Comparator.comparing(MatchTeamMemberStatRank::getDayYield).reversed())
                        .limit(Math.round(teamMemberCalculateRate * statList.size()))
                        .mapToDouble(stat -> stat.getDayYield().doubleValue())
                        .average().orElse(0D))
                        .setScale(YIELD_RATE_SCALE, BigDecimal.ROUND_HALF_UP));

                    return todayMatchTeamStatRank;

                })));
        matchStatDataService.clearData();

        log.info("match {} team member stat for {} finished, total record {}. saving data", matchInfo.getId(), date, teamMemberStatRanks.size());
        Future<Integer> teamMemberStatResult = matchStatDataService.saveMatchTeamMemberStatData(matchInfo.getId(), teamMemberStatRanks);

        matchStatDataService.loadMatchTeamPrevData(matchInfo.getId(), date);
        List<MatchTeamStatRank> teamStatRanks = teamTodayStatMap.values().parallelStream()
            .peek(this::singleTeamStat)
            .collect(Collectors.toList());
        matchStatDataService.clearData();
        calculateRank(teamStatRanks.stream().filter(statRank -> statRank.getMemberNum() >= teamMemberThreshold).collect(Collectors.toList()));

        log.info("match {} team stat for {} finished, total record {}. saving data", matchInfo.getId(), date, teamStatRanks.size());
        Future<Integer> matchTeamStatResult = matchStatDataService.saveMatchTeamStatData(matchInfo.getId(), teamStatRanks);
        return Arrays.asList(teamMemberStatResult, matchTeamStatResult);
    }

    private MatchTeamRule parseMatchTeamRule(MatchBasic matchInfo) {
        try {
            JsonNode node = objectMapper.readTree(matchInfo.getRuleConfig());
            return Optional.ofNullable(node).map(value -> value.get("team")).map(value -> value.get("rule"))
                .map(value -> {
                    try {
                        return objectMapper.treeToValue(value, MatchTeamRule.class);
                    } catch (JsonProcessingException e) {
                        log.warn("can't parse math rule for match {}, use default value", matchInfo.getId());
                        return null;
                    }
                })
                .orElse(new MatchTeamRule(DEFAULT_TEAM_MEMBER_THRESHOLD, DEFAULT_TEAM_MEMBER_CALCULATE_RATE));
        } catch (IOException e) {
            log.warn("can't read match rule for match {}, use default value", matchInfo.getId());
        }
        return new MatchTeamRule(DEFAULT_TEAM_MEMBER_THRESHOLD, DEFAULT_TEAM_MEMBER_CALCULATE_RATE);
    }

    private void singleTeamStat(MatchTeamStatRank todayMatchTeamStatRank) {
        MatchTeamStatRank prevStat = matchStatDataService.queryMatchTeamStatRank(todayMatchTeamStatRank.getTeamId());
        BigDecimal todayYield = todayMatchTeamStatRank.getDayYield();
        BigDecimal totalYield = calculateYield(prevStat.getTotalYield(), todayYield);
        todayMatchTeamStatRank.setTotalYield(totalYield);
        //如果清算日期和前一交易日不是同一周，则说明清算日是当周第一个交易日
        if (null == prevStat.getStatDate()
            || todayMatchTeamStatRank.getStatDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) !=
            prevStat.getStatDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)) {
            todayMatchTeamStatRank.setWeekYield(todayYield);
        } else {
            todayMatchTeamStatRank.setWeekYield(calculateYield(prevStat.getWeekYield(), todayYield));
        }
        //如果清算日与前一交易日不是同一月，则说明清算日是当月第一个交易日
        if (null == prevStat.getStatDate()
            || !todayMatchTeamStatRank.getStatDate().getMonth().equals(prevStat.getStatDate().getMonth())) {
            todayMatchTeamStatRank.setMonthYield(todayYield);
        } else {
            todayMatchTeamStatRank.setMonthYield(calculateYield(prevStat.getMonthYield(), todayYield));
        }

        dealWithLastRank(todayMatchTeamStatRank, prevStat);
    }

    private MatchUserStatRank singleUserStat(MatchJoin user, LocalDate date) {
        MatchUserStatRank todayStatRank = new MatchUserStatRank();
        todayStatRank.setMatchId(user.getMatchId());
        todayStatRank.setAccountId(user.getAccountId());
        todayStatRank.setUserId(user.getUserId());
        todayStatRank.setStatDate(date);
        MatchUserStatRank prevStat = matchStatDataService.queryUserStatRandByAccountId(user.getAccountId());
        MatchUserAccountStatRank todayStat = virtualTradeDataService.queryAccountStat(user.getAccountId());
        calculateStat(user.getJoinDate().toLocalDate(), date, todayStatRank, prevStat, todayStat);
        return todayStatRank;
    }

    private MatchTeamMemberStatRank singleTeamMemberStat(MatchTeamJoin user, LocalDate date) {
        MatchTeamMemberStatRank todayStatRank = new MatchTeamMemberStatRank();
        todayStatRank.setMatchId(user.getMatchId());
        todayStatRank.setTeamId(user.getTeamId());
        todayStatRank.setAccountId(user.getAccountId());
        todayStatRank.setUserId(user.getUserId());
        todayStatRank.setStatDate(date);
        MatchTeamMemberStatRank prevStat = matchStatDataService.queryMatchMemberStatRank(user.getTeamId(), user.getAccountId());
        MatchUserAccountStatRank todayStat = virtualTradeDataService.queryAccountStat(user.getAccountId());
        calculateStat(user.getJoinDate().toLocalDate(), date, todayStatRank, prevStat, todayStat);
        return todayStatRank;
    }

    private void calculateRank(List<? extends StatRank> statList) {
        //day rank
        statList.sort(Comparator.comparing(StatRank::getDayYield).reversed());
        IntStream.rangeClosed(1, statList.size()).forEachOrdered(index -> {
            StatRank stat = statList.get(index - 1);
            stat.setDayRank(index);
            if (null == stat.getLastDayRank()) {
                stat.setDayTrend(0);
            } else {
                stat.setDayTrend(Integer.compare(stat.getLastDayRank(), index));
            }
        });
        //week rank
        statList.sort(Comparator.comparing(StatRank::getWeekYield).reversed());
        IntStream.rangeClosed(1, statList.size()).forEachOrdered(index -> {
            StatRank stat = statList.get(index - 1);
            stat.setWeekRank(index);
            if (null == stat.getLastWeekRank()) {
                stat.setWeekTrend(0);
            } else {
                stat.setWeekTrend(Integer.compare(stat.getLastWeekRank(), index));
            }
        });
        //month rank
        statList.sort(Comparator.comparing(StatRank::getMonthYield).reversed());
        IntStream.rangeClosed(1, statList.size()).forEachOrdered(index -> {
            StatRank stat = statList.get(index - 1);
            stat.setMonthRank(index);
            if (null == stat.getLastMonthRank()) {
                stat.setMonthTrend(0);
            } else {
                stat.setMonthTrend(Integer.compare(stat.getLastMonthRank(), index));
            }
        });
        //total rank
        statList.sort(Comparator.comparing(StatRank::getTotalYield).reversed());
        IntStream.rangeClosed(1, statList.size()).forEachOrdered(index -> {
            StatRank stat = statList.get(index - 1);
            stat.setTotalRank(index);
            if (null == stat.getLastTotalRank()) {
                stat.setTotalTrend(0);
            } else {
                stat.setTotalTrend(Integer.compare(stat.getLastTotalRank(), index));
            }
        });
    }

    private void calculateStat(LocalDate joinDate, LocalDate date, StatRank todayStatRank, StatRank prevStat,
                               Stat todayStat) {
        todayStatRank.setDayYield(todayStat.getDayYield());
        BigDecimal totalYield = calculateYield(prevStat.getTotalYield(), todayStat.getDayYield());
        todayStatRank.setTotalYield(totalYield);
        //如果是同一周
        if (joinDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
            && joinDate.getYear() == date.getYear()) {
            todayStatRank.setWeekYield(totalYield);
        } else {
            todayStatRank.setWeekYield(todayStat.getWeekYield());
        }

        //如果是同一月
        if (joinDate.getMonthValue() == date.getMonthValue()
            && joinDate.getYear() == date.getYear()) {
            todayStatRank.setMonthYield(totalYield);
        } else {
            todayStatRank.setMonthYield(todayStat.getMonthYield());
        }
        dealWithLastRank(todayStatRank, prevStat);
    }

    private void dealWithLastRank(StatRank todayStatRank, StatRank prevStat) {
        todayStatRank.setLastDayRank(prevStat.getDayRank());
        todayStatRank.setLastWeekRank(prevStat.getWeekRank());
        todayStatRank.setLastMonthRank(prevStat.getMonthRank());
        todayStatRank.setLastTotalRank(prevStat.getTotalRank());
    }

    private BigDecimal calculateYield(BigDecimal prev, BigDecimal curr) {
        return prev.add(BigDecimal.ONE).multiply(curr.add(BigDecimal.ONE)).subtract(BigDecimal.ONE)
            .setScale(YIELD_RATE_SCALE, BigDecimal.ROUND_HALF_UP);
    }
}
