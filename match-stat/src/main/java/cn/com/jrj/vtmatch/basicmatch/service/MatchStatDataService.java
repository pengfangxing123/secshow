package cn.com.jrj.vtmatch.basicmatch.service;

import com.google.common.collect.Lists;

import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamDayStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamMemberDayStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamMemberStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountDayStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserDayStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamMemberDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamMemberStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccountDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccountStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.enums.RankType;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.CacheConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 提供清算过程所需要的数据
 * <p>数据主要来自比赛系统，全量加载缓存至jvm，提供接口按单位数据主键频繁查询</p>
 *
 * @author yuan.cheng
 */
@Slf4j
@Service
@RequiredArgsConstructor
class MatchStatDataService {
    private static final Map<String, MatchUserStatRank> MATCH_USER_STAT_RANK_MAP = new HashMap<>();
    private static final Map<Long, MatchTeamStatRank> MATCH_TEAM_STAT_RANK_MAP = new HashMap<>();
    private static final Map<String, MatchTeamMemberStatRank> MATCH_TEAM_MEMBER_STAT_RANK_MAP = new HashMap<>();
    private static final Map<String, MatchUserAccountStatRank> MATCH_USER_ACCOUNT_STAT_RANK_MAP = new HashMap<>();

    private final MatchUserStatRankMapper matchUserStatRankMapper;
    private final MatchTeamStatRankMapper matchTeamStatRankMapper;
    private final MatchTeamMemberStatRankMapper matchTeamMemberStatRankMapper;
    private final MatchUserAccountStatRankMapper matchUserAccountStatRankMapper;
    private final MatchUserDayStatRankMapper matchUserDayStatRankMapper;
    private final MatchUserAccountDayStatRankMapper matchUserAccountDayStatRankMapper;
    private final MatchTeamMemberDayStatRankMapper matchTeamMemberDayStatRankMapper;
    private final MatchTeamDayStatRankMapper matchTeamDayStatRankMapper;
    private final StringRedisTemplate stringRedisTemplate;


    /**
     * 加载账户前一日清算数据
     *
     * @param date 清算日期
     */
    void loadUserAccountPrevData(LocalDate date) {
        MATCH_USER_ACCOUNT_STAT_RANK_MAP.clear();
        MATCH_USER_ACCOUNT_STAT_RANK_MAP.putAll(
            matchUserAccountStatRankMapper.selectLastStat(date).stream()
                .collect(Collectors.toMap(MatchUserAccountStatRank::getAccountId, Function.identity()))
        );
    }

    /**
     * 加载参赛用户前一日清算数据
     *
     * @param matchId 比赛Id
     * @param date    清算日期
     */
    void loadMatchUserPrevData(Long matchId, LocalDate date) {
        List<MatchUserStatRank> statRanks = matchUserStatRankMapper.selectLastStat(matchId, date);
        MATCH_USER_STAT_RANK_MAP.clear();
        MATCH_USER_STAT_RANK_MAP.putAll(
            statRanks.stream().collect(Collectors.toMap(MatchUserStatRank::getAccountId, Function.identity()))
        );
    }

    /**
     * 加载参赛团队前一交易日数据
     *
     * @param matchId 比赛ID
     * @param date    清算日期
     */
    void loadMatchTeamPrevData(Long matchId, LocalDate date) {
        List<MatchTeamStatRank> statRanks = matchTeamStatRankMapper.selectLastStat(matchId, date);
        MATCH_TEAM_STAT_RANK_MAP.clear();
        MATCH_TEAM_STAT_RANK_MAP.putAll(
            statRanks.stream().collect(Collectors.toMap(MatchTeamStatRank::getTeamId, Function.identity()))
        );
    }

    /**
     * 加载比赛团队成员前一交易日数据
     *
     * @param matchId 比赛ID
     * @param date    清算日期
     */
    void loadMatchTeamMemberPrevData(Long matchId, LocalDate date) {
        List<MatchTeamMemberStatRank> statRanks = matchTeamMemberStatRankMapper.selectLastStat(matchId, date);
        MATCH_TEAM_MEMBER_STAT_RANK_MAP.clear();
        MATCH_TEAM_MEMBER_STAT_RANK_MAP.putAll(
            statRanks.stream().collect(Collectors.toMap(stat -> stat.getTeamId() + stat.getAccountId(), Function.identity()))
        );
    }

    MatchUserAccountStatRank queryUserAccountStatRankByAccountId(String accountId) {
        return MATCH_USER_ACCOUNT_STAT_RANK_MAP.computeIfAbsent(accountId, VirtualTradeDataService::defaultAccountStatRank);
    }

    MatchUserStatRank queryUserStatRandByAccountId(String accountId) {
        return MATCH_USER_STAT_RANK_MAP.computeIfAbsent(accountId, this::defaultUserStatRank);
    }

    MatchTeamStatRank queryMatchTeamStatRank(Long teamId) {
        return MATCH_TEAM_STAT_RANK_MAP.computeIfAbsent(teamId, this::defaultTeamStatRank);
    }

    MatchTeamMemberStatRank queryMatchMemberStatRank(Long teamId, String accountId) {
        return MATCH_TEAM_MEMBER_STAT_RANK_MAP.computeIfAbsent(teamId + accountId, key -> this.defaultMatchMemberStatRank(teamId, accountId));
    }

    private MatchTeamMemberStatRank defaultMatchMemberStatRank(Long teamId, String accountId) {
        MatchTeamMemberStatRank stat = new MatchTeamMemberStatRank();
        stat.setTeamId(teamId);
        stat.setAccountId(accountId);
        stat.setDayYield(BigDecimal.ZERO);
        stat.setWeekYield(BigDecimal.ZERO);
        stat.setMonthYield(BigDecimal.ZERO);
        stat.setTotalYield(BigDecimal.ZERO);
        return stat;
    }

    private MatchUserStatRank defaultUserStatRank(String accountId) {
        MatchUserStatRank stat = new MatchUserStatRank();
        stat.setAccountId(accountId);
        stat.setDayYield(BigDecimal.ZERO);
        stat.setWeekYield(BigDecimal.ZERO);
        stat.setMonthYield(BigDecimal.ZERO);
        stat.setTotalYield(BigDecimal.ZERO);
        return stat;
    }

    private MatchTeamStatRank defaultTeamStatRank(Long teamId) {
        MatchTeamStatRank stat = new MatchTeamStatRank();
        stat.setTeamId(teamId);
        stat.setDayYield(BigDecimal.ZERO);
        stat.setWeekYield(BigDecimal.ZERO);
        stat.setMonthYield(BigDecimal.ZERO);
        stat.setTotalYield(BigDecimal.ZERO);
        return stat;
    }

    void clearData() {
        MATCH_USER_STAT_RANK_MAP.clear();
        MATCH_TEAM_STAT_RANK_MAP.clear();
        MATCH_TEAM_MEMBER_STAT_RANK_MAP.clear();
        MATCH_USER_ACCOUNT_STAT_RANK_MAP.clear();
    }

    public int saveMatchUserAccountStatData(List<MatchUserAccountStatRank> statRankList) {
        if (CollectionUtils.isEmpty(statRankList)) {
            return 0;
        }
        matchUserAccountDayStatRankMapper.deleteAll();
        matchUserAccountDayStatRankMapper.batchInsert(statRankList.stream().map(value -> {
            MatchUserAccountDayStatRank stat = new MatchUserAccountDayStatRank();
            BeanUtils.copyProperties(value, stat);
            return stat;
        }).collect(Collectors.toList()));

        int ret = matchUserAccountStatRankMapper.batchInsert(statRankList);

        clearMatchCache(statRankList);

        return ret;
    }

    private void clearMatchCache(List<MatchUserAccountStatRank> statRankList) {
        //clear MATCH_REDIS_USER_RANK_KEY cache
        List<String> keys = statRankList.stream().flatMap(value -> Arrays.stream(RankType.values()).map(rankType ->
            String.format("%s::%d::%s", CacheConstants.MATCH_REDIS_USER_RANK_KEY, value.getUserId(), rankType)
        )).collect(Collectors.toList());
        if (!keys.isEmpty()) {
            List<List<String>> partitions = Lists.partition(keys, 200);
            for (List<String> partition : partitions) {
                stringRedisTemplate.delete(partition);
            }
        }

        //clear MATCH_REDIS_USER_RANK_LIST_KEY cache
        clearCacheByPage(CacheConstants.MATCH_REDIS_USER_RANK_LIST_KEY, statRankList.size());
    }

    /**
     * value =  CacheConstants.MATCH_REDIS_USER_RANK_LIST_KEY,
     * key="T(String).valueOf(#page.current).concat('-').concat(#page.size).concat('-').concat(#type) ",
     */
    private void clearCacheByPage(final String name, final int size) {
        List<String> keys = Arrays.stream(RankType.values()).flatMap(type ->
            IntStream.rangeClosed(1, (int) Math.ceil(size / 20D)).mapToObj(page ->
                String.format("%s::%d-%d-%s", name, page, 20, type))
        ).collect(Collectors.toList());
        if (!keys.isEmpty()) {
            List<List<String>> partitions = Lists.partition(keys, 200);
            for (List<String> partition : partitions) {
                stringRedisTemplate.delete(partition);
            }
        }
    }

    @Async
    Future<Integer> saveMatchUserStatData(Long matchId, List<MatchUserStatRank> statList) {
        if (CollectionUtils.isEmpty(statList)) {
            return new AsyncResult<>(0);
        }
        matchUserDayStatRankMapper.deleteByMatchId(matchId);
        matchUserDayStatRankMapper.batchInsert(statList.stream().map(value -> {
            MatchUserDayStatRank stat = new MatchUserDayStatRank();
            BeanUtils.copyProperties(value, stat);
            return stat;
        }).collect(Collectors.toList()));
        final int result = matchUserStatRankMapper.batchInsert(statList);

        clearCacheByPage(CacheConstants.MATCH_REDIS_MATCH_MEMBER_RANK_KEY, statList.size());

        return new AsyncResult<>(result);
    }

    @Async
    Future<Integer> saveMatchTeamMemberStatData(Long matchId, List<MatchTeamMemberStatRank> teamMemberStatRanks) {
        if (CollectionUtils.isEmpty(teamMemberStatRanks)) {
            return new AsyncResult<>(0);
        }
        matchTeamMemberDayStatRankMapper.deleteByMatchId(matchId);
        matchTeamMemberDayStatRankMapper.batchInsert(teamMemberStatRanks.stream().map(value -> {
            MatchTeamMemberDayStatRank stat = new MatchTeamMemberDayStatRank();
            BeanUtils.copyProperties(value, stat);
            return stat;
        }).collect(Collectors.toList()));
        final int result = matchTeamMemberStatRankMapper.batchInsert(teamMemberStatRanks);

        clearCacheByPage(CacheConstants.MATCH_REDIS_TEAM_MEMBER_RANK_KEY, teamMemberStatRanks.size());

        return new AsyncResult<>(result);
    }

    @Async
    Future<Integer> saveMatchTeamStatData(Long matchId, List<MatchTeamStatRank> teamStatRanks) {
        if (CollectionUtils.isEmpty(teamStatRanks)) {
            return new AsyncResult<>(0);
        }
        matchTeamDayStatRankMapper.deleteByMatchId(matchId);
        matchTeamDayStatRankMapper.batchInsert(teamStatRanks.stream().map(value -> {
            MatchTeamDayStatRank stat = new MatchTeamDayStatRank();
            BeanUtils.copyProperties(value, stat);
            return stat;
        }).collect(Collectors.toList()));
        final int result = matchTeamStatRankMapper.batchInsert(teamStatRanks);

        clearCacheByPage(CacheConstants.MATCH_REDIS_TEAM_RANK_KEY, teamStatRanks.size());

        return new AsyncResult<>(result);
    }

    void clearMatchUserAccountStatRankByDate(LocalDate date) {
        matchUserAccountStatRankMapper.deleteByDate(date);
    }

    void clearMatchUserStatRankByMatchIdAndDate(LocalDate date, List<Integer> matchIds) {
        if (CollectionUtils.isNotEmpty(matchIds)) {
            matchUserStatRankMapper.deleteByDateAndMatchIds(date, matchIds);
        } else {
            matchUserStatRankMapper.deleteByDate(date);
        }
    }

    void clearMatchTeamStatRankByMatchIdAndDate(LocalDate date, List<Integer> matchIds) {
        if (CollectionUtils.isNotEmpty(matchIds)) {
            matchTeamStatRankMapper.deleteByDateAndMatchIds(date, matchIds);
        } else {
            matchTeamStatRankMapper.deleteByDate(date);
        }
    }

    void clearMatchTeamMemberStatRankByMatchIdAndDate(LocalDate date, List<Integer> matchIds) {
        if (CollectionUtils.isNotEmpty(matchIds)) {
            matchTeamMemberStatRankMapper.deleteByDateAndMatchIds(date, matchIds);
        } else {
            matchTeamMemberStatRankMapper.deleteByDate(date);
        }
    }
}
