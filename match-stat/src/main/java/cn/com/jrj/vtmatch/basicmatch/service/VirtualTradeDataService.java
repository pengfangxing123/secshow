package cn.com.jrj.vtmatch.basicmatch.service;

import cn.com.jrj.vtmatch.basicmatch.dto.StatLog;
import cn.com.jrj.vtmatch.basicmatch.dto.StatLogResult;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.StatConfigMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccount;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccountStatRank;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 金牛系统数据服务
 *
 * @author yuan.cheng
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "vt")
class VirtualTradeDataService {

    private static final int DEFAULT_MATCH_ID = 30;
    private static final String DEFAULT_EXCHANGE_ID = "SSAS";

    private final RestTemplate restTemplate;

    private final StatConfigMapper statConfigMapper;
    private final MatchUserAccountMapper userAccountMapper;
    private final MatchUserAccountStatRankMapper userAccountStatRankMapper;

    private static final Map<String, MatchUserAccountStatRank> ACCOUNT_STAT_MAP = new HashMap<>();

    @Setter
    private String domain;
    /**
     * load day stat log from vt
     * @param date stat date
     * @return if load data from db return false, else return true
     */
    boolean loadDayStatLog(final LocalDate date) {
        int result = loadDayStatLogFromDb(date);
        if (0 != result) {
            log.info("import day stat log from db for {}, size {}", date, result);
            return false;
        }
        Integer matchId = Optional.ofNullable(statConfigMapper.selectConfigByKey("companyId"))
            .map(Integer::valueOf).orElse(DEFAULT_MATCH_ID);
        String exchangeId = Optional.ofNullable(statConfigMapper.selectConfigByKey("exchangeId"))
            .orElse(DEFAULT_EXCHANGE_ID);

        loadDayStatLogFromSource(date, matchId, exchangeId);
        return true;
    }

    private int loadDayStatLogFromDb(LocalDate date) {
        List<MatchUserAccountStatRank> list = userAccountStatRankMapper.selectByDate(date);
        ACCOUNT_STAT_MAP.clear();
        ACCOUNT_STAT_MAP.putAll(list.stream()
            .collect(Collectors.toMap(MatchUserAccountStatRank::getAccountId, Function.identity())));
        return list.size();
    }

    private void loadDayStatLogFromSource(LocalDate date, Integer matchId, String exchangeId) {
        List<MatchUserAccount> userAccounts = userAccountMapper.selectAll();

        List<List<MatchUserAccount>> accountIdPartitionList = ListUtils.partition(userAccounts, 200);
        List<Future<List<MatchUserAccountStatRank>>> resultList = accountIdPartitionList.stream()
            .map(accountIds -> this.loadDayStatLog(matchId, exchangeId, date, accountIds))
            .collect(Collectors.toList());
        ACCOUNT_STAT_MAP.clear();
        ACCOUNT_STAT_MAP.putAll(resultList.stream().flatMap(re -> {
            try {
                return re.get(10, TimeUnit.SECONDS).stream();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return Stream.empty();
            } catch (ExecutionException e) {
                log.error("loadDayStatLogFromSource execute error", e);
                return Stream.empty();
            } catch (TimeoutException e) {
                log.warn("loadDayStatLogFromSource timeout", e);
                return Stream.empty();
            }
        }).collect(Collectors.toMap(MatchUserAccountStatRank::getAccountId, Function.identity())));
        log.info("import day stat log from source for {}, size {}", date, ACCOUNT_STAT_MAP.size());
    }

    @Async
    Future<List<MatchUserAccountStatRank>> loadDayStatLog(Integer matchId, String exchangeId, LocalDate date, List<MatchUserAccount> userAccounts) {
        List<String> uuids = userAccounts.stream().map(MatchUserAccount::getAccountId).collect(Collectors.toList());
        Map<String, Long> accountIdUserIdMap = userAccounts.stream()
            .collect(Collectors.toMap(MatchUserAccount::getAccountId, MatchUserAccount::getUserId));
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(domain + "/dayStatLog/query/{mid}/{eid}")
            .queryParam("date", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Map<String, List<String>> data = new HashMap<>(2);
        data.put("uuids", uuids);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Accept", "application/json;chartset=utf-8");
        HttpEntity<Object> entity = new HttpEntity<>(data, headers);
        StatLogResult statLogResult = restTemplate.postForObject(builder.toUriString(), entity, StatLogResult.class, matchId, exchangeId);
        List<StatLog> statLogs = Optional.ofNullable(statLogResult).map(StatLogResult::getItems).orElse(Collections.emptyList());
        List<MatchUserAccountStatRank> listToSave = statLogs.stream().map(stat -> {
            MatchUserAccountStatRank accountStat = new MatchUserAccountStatRank();
            accountStat.setAccountId(stat.getAccountId());
            accountStat.setStatDate(stat.getStatDate().atZone(ZoneId.systemDefault()).toLocalDate());
            //BeanUtils.copyProperties(stat, accountStat)
            //金牛拉来的数据收益率是乘了100的，这里要缩小100倍，小数点左移两位
            accountStat.setDayYield(stat.getDayYield().movePointLeft(2));
            accountStat.setWeekYield(stat.getWeekYield().movePointLeft(2));
            accountStat.setMonthYield(stat.getMonthYield().movePointLeft(2));
            accountStat.setTotalYield(stat.getTotalYield().movePointLeft(2));
            accountStat.setUserId(accountIdUserIdMap.get(accountStat.getAccountId()));
            return accountStat;
        }).collect(Collectors.toList());
        return new AsyncResult<>(listToSave);
    }

    List<MatchUserAccountStatRank> queryAllAccountStat() {
        return new ArrayList<>(ACCOUNT_STAT_MAP.values());
    }

    /**
     * 按账户查询金牛清算信息
     *
     * @param accountId 账户ID
     * @return {@link MatchUserAccountStatRank}
     */
    MatchUserAccountStatRank queryAccountStat(String accountId) {
        return ACCOUNT_STAT_MAP.computeIfAbsent(accountId, VirtualTradeDataService::defaultAccountStatRank);
    }

    /**
     * 当查询不到账户对应数据时提供的默认数据
     *
     * @param accountId 账户ID
     * @return {@link MatchUserAccountStatRank}
     */
    static MatchUserAccountStatRank defaultAccountStatRank(String accountId) {
        MatchUserAccountStatRank stat = new MatchUserAccountStatRank();
        stat.setAccountId(accountId);
        stat.setDayYield(BigDecimal.ZERO);
        stat.setWeekYield(BigDecimal.ZERO);
        stat.setMonthYield(BigDecimal.ZERO);
        stat.setTotalYield(BigDecimal.ZERO);
        return stat;
    }

    /**
     * 清除jvm缓存数据
     */
    void clearData() {
        ACCOUNT_STAT_MAP.clear();
    }
}
