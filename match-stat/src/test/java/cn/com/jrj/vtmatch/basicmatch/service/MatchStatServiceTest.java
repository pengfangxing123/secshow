package cn.com.jrj.vtmatch.basicmatch.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import cn.com.jrj.vtmatch.basicmatch.dto.OpenCloseResult;
import cn.com.jrj.vtmatch.basicmatch.dto.StatLogResult;
import cn.com.jrj.vtmatch.basicmatch.helper.StringRedisHelper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchJoinMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamDayStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamMemberStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserDayStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamMemberStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccount;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccountStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserStatRank;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * @author yuan.cheng
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MatchStatServiceTest {

    @Autowired
    private MatchStatService matchStatService;
    @Autowired
    private MatchUserAccountMapper matchUserAccountMapper;
    @Autowired
    private MatchJoinMapper matchJoinMapper;
    @MockBean
    OAuth2RestTemplate restTemplate;
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MatchUserAccountStatRankMapper matchUserAccountStatRankMapper;
    @Autowired
    private MatchUserStatRankMapper matchUserStatRankMapper;
    @Autowired
    private MatchUserDayStatRankMapper matchUserDayStatRankMapper;
    @Autowired
    private MatchTeamMemberStatRankMapper matchTeamMemberDayStatRankMapper;
    @Autowired
    private MatchTeamDayStatRankMapper matchTeamDayStatRankMapper;

    @MockBean
    private StringRedisTemplate stringRedisTemplate;

    @Value("${vt.domain}")
    private String vtDomain;
    @Before
    public void init() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json;charset=utf-8");
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        final String openCloseUrl = vtDomain + "/exchangecalendar/1/openclose";
        final String datePattern = "yyyy-MM-dd";
        final String endDateParam = "endDate";
        when(restTemplate.exchange(UriComponentsBuilder.fromUriString(openCloseUrl)
            .queryParam(endDateParam, LocalDate.of(2018, 9, 28).format(DateTimeFormatter.ofPattern(datePattern)))
            .toUriString(), HttpMethod.GET, entity, OpenCloseResult.class))
            .thenReturn(new ResponseEntity<>(objectMapper.readValue(
                resourceLoader.getResource("classpath:open-close-20180928.json").getInputStream(), OpenCloseResult.class), HttpStatus.OK));

        when(restTemplate.exchange(UriComponentsBuilder.fromUriString(openCloseUrl)
            .queryParam(endDateParam, LocalDate.of(2018, 10, 7).format(DateTimeFormatter.ofPattern(datePattern)))
            .toUriString(), HttpMethod.GET, entity, OpenCloseResult.class))
            .thenReturn(new ResponseEntity<>(objectMapper.readValue(
                resourceLoader.getResource("classpath:open-close-20181007.json").getInputStream(), OpenCloseResult.class), HttpStatus.OK));

        when(restTemplate.exchange(UriComponentsBuilder.fromUriString(openCloseUrl)
            .queryParam(endDateParam, LocalDate.of(2018, 10, 8).format(DateTimeFormatter.ofPattern(datePattern)))
            .toUriString(), HttpMethod.GET, entity, OpenCloseResult.class))
            .thenReturn(new ResponseEntity<>(objectMapper.readValue(
                resourceLoader.getResource("classpath:open-close-20181008.json").getInputStream(), OpenCloseResult.class), HttpStatus.OK));
        when(restTemplate.exchange(UriComponentsBuilder.fromUriString(openCloseUrl)
            .queryParam(endDateParam, LocalDate.of(2018, 10, 9).format(DateTimeFormatter.ofPattern(datePattern)))
            .toUriString(), HttpMethod.GET, entity, OpenCloseResult.class))
            .thenReturn(new ResponseEntity<>(objectMapper.readValue(
                resourceLoader.getResource("classpath:open-close-20181009.json").getInputStream(), OpenCloseResult.class), HttpStatus.OK));


        final String statLogUrl = vtDomain + "/dayStatLog/query/{mid}/{eid}";
        when(restTemplate.postForObject(eq(UriComponentsBuilder.fromUriString(statLogUrl)
            .queryParam("date", LocalDate.of(2018, 9, 28).format(DateTimeFormatter.ofPattern(datePattern)))
            .toUriString()), any(), any(), eq(30), eq("SSAS")))
            .thenReturn(objectMapper.readValue(
                resourceLoader.getResource("classpath:vt-statlog-20180928.json").getInputStream(), StatLogResult.class))
            .thenReturn(objectMapper.readValue(
                resourceLoader.getResource("classpath:vt-statlog-20180928-1.json").getInputStream(), StatLogResult.class));

        LocalDate date2 = LocalDate.of(2018, 10, 8);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(statLogUrl)
            .queryParam("date", date2.format(DateTimeFormatter.ofPattern(datePattern)));
        final StatLogResult statLog20181008 = objectMapper.readValue(
            resourceLoader.getResource("classpath:vt-statlog-20181008.json").getInputStream(), StatLogResult.class);
        final StatLogResult statLog20181008_1 = objectMapper.readValue(
            resourceLoader.getResource("classpath:vt-statlog-20181008-1.json").getInputStream(), StatLogResult.class);
        when(restTemplate.postForObject(eq(builder.toUriString()), any(), any(), eq(30), eq("SSAS")))
            .thenReturn(statLog20181008)
            .thenReturn(statLog20181008_1)
            .thenReturn(statLog20181008)
            .thenReturn(statLog20181008_1);

        when(restTemplate.postForObject(eq(UriComponentsBuilder.fromUriString(statLogUrl)
            .queryParam("date", LocalDate.of(2018, 10, 9).format(DateTimeFormatter.ofPattern(datePattern)))
            .toUriString()), any(), any(), eq(30), eq("SSAS")))
            .thenReturn(objectMapper.readValue(
                resourceLoader.getResource("classpath:vt-statlog-20181009.json").getInputStream(), StatLogResult.class))
            .thenReturn(objectMapper.readValue(
                resourceLoader.getResource("classpath:vt-statlog-20181009-1.json").getInputStream(), StatLogResult.class));

        when(stringRedisTemplate.delete(anyCollection())).thenReturn(1L);
    }

    @Test
    public void matchStatTest() {
        matchStatService.statAllMatchByDate(LocalDate.of(2018, 9, 28));
        matchStatService.statAllMatchByDate(LocalDate.of(2018, 10, 7));
        matchStatService.statAllMatchByDate(LocalDate.of(2018, 10, 8));
        matchStatService.statAllMatchByDate(LocalDate.of(2018, 10, 9));

        matchStatService.reStatMatch(LocalDate.of(2018, 10, 7), null, true);
        matchStatService.reStatMatch(LocalDate.of(2018, 10, 8), Collections.singletonList(1), true);
        matchStatService.reStatMatch(LocalDate.of(2018, 10, 9), null, false);

        //check stat trade times
        final String testUserAccount = "win_100179";
        LambdaQueryWrapper<MatchUserAccount> query = new LambdaQueryWrapper<>();
        query.select(MatchUserAccount::getAccountId, MatchUserAccount::getTradeTimes)
            .eq(MatchUserAccount::getAccountId, testUserAccount);
        MatchUserAccount account1 = matchUserAccountMapper.selectOne(query);
        final String tradeTimes = "tradeTimes";
        assertThat(account1).isNotNull().hasFieldOrPropertyWithValue(tradeTimes, 2);

        LambdaQueryWrapper<MatchJoin> query1 = new LambdaQueryWrapper<>();
        query1.select(MatchJoin::getTradeTimes)
            .eq(MatchJoin::getMatchId, 1)
            .eq(MatchJoin::getAccountId, testUserAccount);
        MatchJoin join1 = matchJoinMapper.selectOne(query1);
        assertThat(join1).isNotNull().hasFieldOrPropertyWithValue(tradeTimes, 1);

        LambdaQueryWrapper<MatchJoin> query2 = new LambdaQueryWrapper<>();
        query2.select(MatchJoin::getTradeTimes)
            .eq(MatchJoin::getMatchId, 2)
            .eq(MatchJoin::getAccountId, testUserAccount);
        MatchJoin join2 = matchJoinMapper.selectOne(query2);
        assertThat(join2).isNotNull().hasFieldOrPropertyWithValue(tradeTimes, 1);

        //check account yield. example user is 100179
        LambdaQueryWrapper<MatchUserAccountStatRank> accountStatQuery = new LambdaQueryWrapper<>();
        accountStatQuery.select(MatchUserAccountStatRank::getDayYield,
            MatchUserAccountStatRank::getWeekYield,
            MatchUserAccountStatRank::getMonthYield,
            MatchUserAccountStatRank::getTotalYield,
            MatchUserAccountStatRank::getDayRank,
            MatchUserAccountStatRank::getLastDayRank,
            MatchUserAccountStatRank::getMonthRank,
            MatchUserAccountStatRank::getLastMonthRank,
            MatchUserAccountStatRank::getWeekRank,
            MatchUserAccountStatRank::getLastWeekRank,
            MatchUserAccountStatRank::getTotalRank,
            MatchUserAccountStatRank::getLastTotalRank,
            MatchUserAccountStatRank::getDayTrend,
            MatchUserAccountStatRank::getWeekTrend,
            MatchUserAccountStatRank::getMonthTrend,
            MatchUserAccountStatRank::getTotalTrend)
            .eq(MatchUserAccountStatRank::getAccountId, testUserAccount)
            .eq(MatchUserAccountStatRank::getStatDate, LocalDate.of(2018, 9, 28));

        MatchUserAccountStatRank accountStatRank0928 = matchUserAccountStatRankMapper.selectOne(accountStatQuery);
        log.info("account stat rank 0928 {}", accountStatRank0928);
        assertThat(accountStatRank0928).isNotNull();
        assertThat(accountStatRank0928.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(0.03));
        assertThat(accountStatRank0928.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(0.07));
        assertThat(accountStatRank0928.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.01));
        assertThat(accountStatRank0928.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.002));

        LambdaQueryWrapper<MatchUserAccountStatRank> accountStatQuery1 = new LambdaQueryWrapper<>();
        accountStatQuery1.select(MatchUserAccountStatRank::getDayYield,
            MatchUserAccountStatRank::getWeekYield,
            MatchUserAccountStatRank::getMonthYield,
            MatchUserAccountStatRank::getTotalYield,
            MatchUserAccountStatRank::getDayRank,
            MatchUserAccountStatRank::getLastDayRank,
            MatchUserAccountStatRank::getWeekRank,
            MatchUserAccountStatRank::getLastWeekRank,
            MatchUserAccountStatRank::getMonthRank,
            MatchUserAccountStatRank::getLastMonthRank,
            MatchUserAccountStatRank::getTotalRank,
            MatchUserAccountStatRank::getLastTotalRank,
            MatchUserAccountStatRank::getDayTrend,
            MatchUserAccountStatRank::getWeekTrend,
            MatchUserAccountStatRank::getMonthTrend,
            MatchUserAccountStatRank::getTotalTrend)
            .eq(MatchUserAccountStatRank::getAccountId, testUserAccount)
            .eq(MatchUserAccountStatRank::getStatDate, LocalDate.of(2018, 10, 8));

        MatchUserAccountStatRank accountStatRank1008 = matchUserAccountStatRankMapper.selectOne(accountStatQuery1);
        log.info("account stat rank 1008 {}", accountStatRank1008);
        assertThat(accountStatRank1008).isNotNull();
        assertThat(accountStatRank1008.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.01));
        assertThat(accountStatRank1008.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.01));
        assertThat(accountStatRank1008.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.01));
        assertThat(accountStatRank1008.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.012));
        assertThat(accountStatRank1008.getDayTrend()).isEqualByComparingTo(-1);
        assertThat(accountStatRank1008.getWeekTrend()).isEqualByComparingTo(-1);
        assertThat(accountStatRank1008.getMonthTrend()).isEqualByComparingTo(1);
        assertThat(accountStatRank1008.getTotalTrend()).isEqualByComparingTo(-1);

        LambdaQueryWrapper<MatchUserAccountStatRank> accountStatQuery2 = new LambdaQueryWrapper<>();
        accountStatQuery2.select(MatchUserAccountStatRank::getDayYield,
            MatchUserAccountStatRank::getWeekYield,
            MatchUserAccountStatRank::getMonthYield,
            MatchUserAccountStatRank::getTotalYield,
            MatchUserAccountStatRank::getDayRank,
            MatchUserAccountStatRank::getLastDayRank,
            MatchUserAccountStatRank::getWeekRank,
            MatchUserAccountStatRank::getLastWeekRank,
            MatchUserAccountStatRank::getMonthRank,
            MatchUserAccountStatRank::getLastMonthRank,
            MatchUserAccountStatRank::getTotalRank,
            MatchUserAccountStatRank::getLastTotalRank,
            MatchUserAccountStatRank::getDayTrend,
            MatchUserAccountStatRank::getWeekTrend,
            MatchUserAccountStatRank::getMonthTrend,
            MatchUserAccountStatRank::getTotalTrend)
            .eq(MatchUserAccountStatRank::getAccountId, testUserAccount)
            .eq(MatchUserAccountStatRank::getStatDate, LocalDate.of(2018, 10, 9));

        MatchUserAccountStatRank accountStatRank1009 = matchUserAccountStatRankMapper.selectOne(accountStatQuery2);
        log.info("account stat rank 1009 {}", accountStatRank1009);
        assertThat(accountStatRank1009).isNotNull();
        assertThat(accountStatRank1009.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.02));
        assertThat(accountStatRank1009.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.0298));
        assertThat(accountStatRank1009.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.0298));
        assertThat(accountStatRank1009.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.0318));
        assertThat(accountStatRank1009.getDayTrend()).isEqualByComparingTo(-1);
        assertThat(accountStatRank1009.getWeekTrend()).isEqualByComparingTo(-1);
        assertThat(accountStatRank1009.getMonthTrend()).isEqualByComparingTo(-1);
        assertThat(accountStatRank1009.getTotalTrend()).isEqualByComparingTo(0);

        //check match user stat for match 1. example user is 100179
        LambdaQueryWrapper<MatchUserStatRank> matchUserStatQuery1 = new LambdaQueryWrapper<>();
        matchUserStatQuery1.select(MatchUserStatRank::getDayYield,
            MatchUserStatRank::getWeekYield,
            MatchUserStatRank::getMonthYield,
            MatchUserStatRank::getTotalYield,
            MatchUserStatRank::getDayRank,
            MatchUserStatRank::getLastDayRank,
            MatchUserStatRank::getWeekRank,
            MatchUserStatRank::getLastWeekRank,
            MatchUserStatRank::getMonthRank,
            MatchUserStatRank::getLastMonthRank,
            MatchUserStatRank::getTotalRank,
            MatchUserStatRank::getLastTotalRank,
            MatchUserStatRank::getDayTrend,
            MatchUserStatRank::getWeekTrend,
            MatchUserStatRank::getMonthTrend,
            MatchUserStatRank::getTotalTrend)
            .eq(MatchUserStatRank::getMatchId, 1)
            .eq(MatchUserStatRank::getAccountId, testUserAccount)
            .eq(MatchUserStatRank::getStatDate, LocalDate.of(2018, 9, 28));

        MatchUserStatRank userStatRank0928 = matchUserStatRankMapper.selectOne(matchUserStatQuery1);
        assertThat(userStatRank0928).isNull();

        LambdaQueryWrapper<MatchUserStatRank> matchUserStatQuery11 = new LambdaQueryWrapper<>();
        matchUserStatQuery11.select(MatchUserStatRank::getDayYield,
            MatchUserStatRank::getWeekYield,
            MatchUserStatRank::getMonthYield,
            MatchUserStatRank::getTotalYield,
            MatchUserStatRank::getDayRank,
            MatchUserStatRank::getLastDayRank,
            MatchUserStatRank::getWeekRank,
            MatchUserStatRank::getLastWeekRank,
            MatchUserStatRank::getMonthRank,
            MatchUserStatRank::getLastMonthRank,
            MatchUserStatRank::getTotalRank,
            MatchUserStatRank::getLastTotalRank,
            MatchUserStatRank::getDayTrend,
            MatchUserStatRank::getWeekTrend,
            MatchUserStatRank::getMonthTrend,
            MatchUserStatRank::getTotalTrend)
            .eq(MatchUserStatRank::getMatchId, 1)
            .eq(MatchUserStatRank::getAccountId, testUserAccount)
            .eq(MatchUserStatRank::getStatDate, LocalDate.of(2018, 10, 8));

        MatchUserStatRank userStatRank1008 = matchUserStatRankMapper.selectOne(matchUserStatQuery11);
        assertThat(userStatRank1008).isNull();

        LambdaQueryWrapper<MatchUserStatRank> matchUserStatQuery12 = new LambdaQueryWrapper<>();
        matchUserStatQuery12.select(MatchUserStatRank::getDayYield,
            MatchUserStatRank::getWeekYield,
            MatchUserStatRank::getMonthYield,
            MatchUserStatRank::getTotalYield,
            MatchUserStatRank::getDayRank,
            MatchUserStatRank::getLastDayRank,
            MatchUserStatRank::getWeekRank,
            MatchUserStatRank::getLastWeekRank,
            MatchUserStatRank::getMonthRank,
            MatchUserStatRank::getLastMonthRank,
            MatchUserStatRank::getTotalRank,
            MatchUserStatRank::getLastTotalRank,
            MatchUserStatRank::getDayTrend,
            MatchUserStatRank::getWeekTrend,
            MatchUserStatRank::getMonthTrend,
            MatchUserStatRank::getTotalTrend)
            .eq(MatchUserStatRank::getMatchId, 1)
            .eq(MatchUserStatRank::getAccountId, testUserAccount)
            .eq(MatchUserStatRank::getStatDate, LocalDate.of(2018, 10, 9));

        MatchUserStatRank userStatRank1009 = matchUserStatRankMapper.selectOne(matchUserStatQuery12);
        log.info("match1 user stat rank 1009 {}", userStatRank1009);
        assertThat(userStatRank1009).isNotNull();
        assertThat(userStatRank1009.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.02));
        assertThat(userStatRank1009.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.02));
        assertThat(userStatRank1009.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.02));
        assertThat(userStatRank1009.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.02));

        //check match1 user day stat rank
        LambdaQueryWrapper<MatchUserDayStatRank> matchUserDayStatQuery1 = new LambdaQueryWrapper<>();
        matchUserDayStatQuery1.select(MatchUserDayStatRank::getDayYield,
            MatchUserDayStatRank::getWeekYield,
            MatchUserDayStatRank::getMonthYield,
            MatchUserDayStatRank::getTotalYield,
            MatchUserDayStatRank::getDayRank,
            MatchUserDayStatRank::getLastDayRank,
            MatchUserDayStatRank::getWeekRank,
            MatchUserDayStatRank::getLastWeekRank,
            MatchUserDayStatRank::getMonthRank,
            MatchUserDayStatRank::getLastMonthRank,
            MatchUserDayStatRank::getTotalRank,
            MatchUserDayStatRank::getLastTotalRank,
            MatchUserDayStatRank::getDayTrend,
            MatchUserDayStatRank::getWeekTrend,
            MatchUserDayStatRank::getMonthTrend,
            MatchUserDayStatRank::getTotalTrend)
            .eq(MatchUserDayStatRank::getMatchId, 1)
            .eq(MatchUserDayStatRank::getAccountId, testUserAccount);

        MatchUserDayStatRank userDayStatRank1009 = matchUserDayStatRankMapper.selectOne(matchUserDayStatQuery1);
        log.info("match1 user day stat rank {}", userDayStatRank1009);
        assertThat(userDayStatRank1009).isNotNull();
        assertThat(userDayStatRank1009.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.02));
        assertThat(userDayStatRank1009.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.02));
        assertThat(userDayStatRank1009.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.02));
        assertThat(userDayStatRank1009.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.02));

        //check match user stat for match 2. example user 100179
        LambdaQueryWrapper<MatchUserStatRank> matchUserStatQuery2 = new LambdaQueryWrapper<>();
        matchUserStatQuery2.select(MatchUserStatRank::getDayYield,
            MatchUserStatRank::getWeekYield,
            MatchUserStatRank::getMonthYield,
            MatchUserStatRank::getTotalYield,
            MatchUserStatRank::getDayRank,
            MatchUserStatRank::getLastDayRank,
            MatchUserStatRank::getWeekRank,
            MatchUserStatRank::getLastWeekRank,
            MatchUserStatRank::getMonthRank,
            MatchUserStatRank::getLastMonthRank,
            MatchUserStatRank::getTotalRank,
            MatchUserStatRank::getLastTotalRank,
            MatchUserStatRank::getDayTrend,
            MatchUserStatRank::getWeekTrend,
            MatchUserStatRank::getMonthTrend,
            MatchUserStatRank::getTotalTrend)
            .eq(MatchUserStatRank::getMatchId, 2)
            .eq(MatchUserStatRank::getAccountId, testUserAccount)
            .eq(MatchUserStatRank::getStatDate, LocalDate.of(2018, 9, 28));

        MatchUserStatRank userStatRank20928 = matchUserStatRankMapper.selectOne(matchUserStatQuery2);
        assertThat(userStatRank20928).isNotNull();
        assertThat(userStatRank20928.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(0.03));
        assertThat(userStatRank20928.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(0.03));
        assertThat(userStatRank20928.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(0.03));
        assertThat(userStatRank20928.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(0.03));

        LambdaQueryWrapper<MatchUserStatRank> matchUserStatQuery21 = new LambdaQueryWrapper<>();
        matchUserStatQuery21.select(MatchUserStatRank::getDayYield,
            MatchUserStatRank::getWeekYield,
            MatchUserStatRank::getMonthYield,
            MatchUserStatRank::getTotalYield,
            MatchUserStatRank::getDayRank,
            MatchUserStatRank::getLastDayRank,
            MatchUserStatRank::getWeekRank,
            MatchUserStatRank::getLastWeekRank,
            MatchUserStatRank::getMonthRank,
            MatchUserStatRank::getLastMonthRank,
            MatchUserStatRank::getTotalRank,
            MatchUserStatRank::getLastTotalRank,
            MatchUserStatRank::getDayTrend,
            MatchUserStatRank::getWeekTrend,
            MatchUserStatRank::getMonthTrend,
            MatchUserStatRank::getTotalTrend)
            .eq(MatchUserStatRank::getMatchId, 2)
            .eq(MatchUserStatRank::getAccountId, testUserAccount)
            .eq(MatchUserStatRank::getStatDate, LocalDate.of(2018, 10, 8));

        MatchUserStatRank userStatRank21008 = matchUserStatRankMapper.selectOne(matchUserStatQuery21);
        log.info("match2 user stat rank 1008 {}", userStatRank21008);
        assertThat(userStatRank21008).isNotNull();
        assertThat(userStatRank21008.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.01));
        assertThat(userStatRank21008.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.01));
        assertThat(userStatRank21008.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.01));
        assertThat(userStatRank21008.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(0.0197));
        assertThat(userStatRank21008.getDayTrend()).isEqualByComparingTo(-1);
        assertThat(userStatRank21008.getWeekTrend()).isEqualByComparingTo(-1);
        assertThat(userStatRank21008.getMonthTrend()).isEqualByComparingTo(-1);
        assertThat(userStatRank21008.getTotalTrend()).isEqualByComparingTo(0);

        LambdaQueryWrapper<MatchUserStatRank> matchUserStatQuery22 = new LambdaQueryWrapper<>();
        matchUserStatQuery22.select(MatchUserStatRank::getDayYield,
            MatchUserStatRank::getWeekYield,
            MatchUserStatRank::getMonthYield,
            MatchUserStatRank::getTotalYield,
            MatchUserStatRank::getDayRank,
            MatchUserStatRank::getLastDayRank,
            MatchUserStatRank::getWeekRank,
            MatchUserStatRank::getLastWeekRank,
            MatchUserStatRank::getMonthRank,
            MatchUserStatRank::getLastMonthRank,
            MatchUserStatRank::getTotalRank,
            MatchUserStatRank::getLastTotalRank,
            MatchUserStatRank::getDayTrend,
            MatchUserStatRank::getWeekTrend,
            MatchUserStatRank::getMonthTrend,
            MatchUserStatRank::getTotalTrend)
            .eq(MatchUserStatRank::getMatchId, 2)
            .eq(MatchUserStatRank::getAccountId, testUserAccount)
            .eq(MatchUserStatRank::getStatDate, LocalDate.of(2018, 10, 9));

        MatchUserStatRank userStatRank21009 = matchUserStatRankMapper.selectOne(matchUserStatQuery22);
        log.info("match2 user stat rank 1009 {}", userStatRank21009);
        assertThat(userStatRank21009).isNotNull();
        assertThat(userStatRank21009.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.02));
        assertThat(userStatRank21009.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.0298));
        assertThat(userStatRank21009.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.0298));
        assertThat(userStatRank21009.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.00069));
        assertThat(userStatRank21009.getDayTrend()).isEqualByComparingTo(-1);
        assertThat(userStatRank21009.getWeekTrend()).isEqualByComparingTo(-1);
        assertThat(userStatRank21009.getMonthTrend()).isEqualByComparingTo(-1);
        assertThat(userStatRank21009.getTotalTrend()).isEqualByComparingTo(-1);

        //check match 2 use day stat rank
        LambdaQueryWrapper<MatchUserDayStatRank> matchUserDayStatQuery2 = new LambdaQueryWrapper<>();
        matchUserDayStatQuery2.select(MatchUserDayStatRank::getDayYield,
            MatchUserDayStatRank::getWeekYield,
            MatchUserDayStatRank::getMonthYield,
            MatchUserDayStatRank::getTotalYield,
            MatchUserDayStatRank::getDayRank,
            MatchUserDayStatRank::getLastDayRank,
            MatchUserDayStatRank::getWeekRank,
            MatchUserDayStatRank::getLastWeekRank,
            MatchUserDayStatRank::getMonthRank,
            MatchUserDayStatRank::getLastMonthRank,
            MatchUserDayStatRank::getTotalRank,
            MatchUserDayStatRank::getLastTotalRank,
            MatchUserDayStatRank::getDayTrend,
            MatchUserDayStatRank::getWeekTrend,
            MatchUserDayStatRank::getMonthTrend,
            MatchUserDayStatRank::getTotalTrend)
            .eq(MatchUserDayStatRank::getMatchId, 2)
            .eq(MatchUserDayStatRank::getAccountId, testUserAccount);

        MatchUserDayStatRank userDayStatRank2 = matchUserDayStatRankMapper.selectOne(matchUserDayStatQuery2);
        log.info("match2 user day stat rank {}", userDayStatRank2);
        assertThat(userDayStatRank2).isNotNull();
        assertThat(userDayStatRank2.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.02));
        assertThat(userDayStatRank2.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.0298));
        assertThat(userDayStatRank2.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.0298));
        assertThat(userDayStatRank2.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.00069));
        assertThat(userDayStatRank2.getDayTrend()).isEqualByComparingTo(-1);
        assertThat(userDayStatRank2.getWeekTrend()).isEqualByComparingTo(-1);
        assertThat(userDayStatRank2.getMonthTrend()).isEqualByComparingTo(-1);
        assertThat(userDayStatRank2.getTotalTrend()).isEqualByComparingTo(-1);

        //check team member data for 10001 in team 1 match2
        LambdaQueryWrapper<MatchTeamMemberStatRank> matchTeamMemberStatQuery111 = new LambdaQueryWrapper<>();
        matchTeamMemberStatQuery111.select(
            MatchTeamMemberStatRank::getDayYield,
            MatchTeamMemberStatRank::getWeekYield,
            MatchTeamMemberStatRank::getMonthYield,
            MatchTeamMemberStatRank::getTotalYield,
            MatchTeamMemberStatRank::getDayRank,
            MatchTeamMemberStatRank::getLastDayRank,
            MatchTeamMemberStatRank::getWeekRank,
            MatchTeamMemberStatRank::getLastWeekRank,
            MatchTeamMemberStatRank::getMonthRank,
            MatchTeamMemberStatRank::getLastMonthRank,
            MatchTeamMemberStatRank::getTotalRank,
            MatchTeamMemberStatRank::getLastTotalRank,
            MatchTeamMemberStatRank::getDayTrend,
            MatchTeamMemberStatRank::getWeekTrend,
            MatchTeamMemberStatRank::getMonthTrend,
            MatchTeamMemberStatRank::getTotalTrend)
            .eq(MatchTeamMemberStatRank::getMatchId, 2)
            .eq(MatchTeamMemberStatRank::getTeamId, 1)
            .eq(MatchTeamMemberStatRank::getAccountId, "win_10001")
            .eq(MatchTeamMemberStatRank::getStatDate, LocalDate.of(2018, 9, 28));

        MatchTeamMemberStatRank matchTeamMemberStatRank110928 = matchTeamMemberDayStatRankMapper.selectOne(matchTeamMemberStatQuery111);
        log.info("match1 team1 user member stat rank 0928 {}", matchTeamMemberStatRank110928);
        assertThat(matchTeamMemberStatRank110928).isNotNull();
        assertThat(matchTeamMemberStatRank110928.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(0.00599));
        assertThat(matchTeamMemberStatRank110928.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(0.00599));
        assertThat(matchTeamMemberStatRank110928.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(0.00599));
        assertThat(matchTeamMemberStatRank110928.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(0.00599));

        LambdaQueryWrapper<MatchTeamMemberStatRank> matchTeamMemberStatQuery112 = new LambdaQueryWrapper<>();
        matchTeamMemberStatQuery112.select(
            MatchTeamMemberStatRank::getDayYield,
            MatchTeamMemberStatRank::getWeekYield,
            MatchTeamMemberStatRank::getMonthYield,
            MatchTeamMemberStatRank::getTotalYield,
            MatchTeamMemberStatRank::getDayRank,
            MatchTeamMemberStatRank::getLastDayRank,
            MatchTeamMemberStatRank::getWeekRank,
            MatchTeamMemberStatRank::getLastWeekRank,
            MatchTeamMemberStatRank::getMonthRank,
            MatchTeamMemberStatRank::getLastMonthRank,
            MatchTeamMemberStatRank::getTotalRank,
            MatchTeamMemberStatRank::getLastTotalRank,
            MatchTeamMemberStatRank::getDayTrend,
            MatchTeamMemberStatRank::getWeekTrend,
            MatchTeamMemberStatRank::getMonthTrend,
            MatchTeamMemberStatRank::getTotalTrend)
            .eq(MatchTeamMemberStatRank::getMatchId, 2)
            .eq(MatchTeamMemberStatRank::getTeamId, 1)
            .eq(MatchTeamMemberStatRank::getAccountId, "win_10001")
            .eq(MatchTeamMemberStatRank::getStatDate, LocalDate.of(2018, 10, 8));

        MatchTeamMemberStatRank matchTeamMemberStatRank111008 = matchTeamMemberDayStatRankMapper.selectOne(matchTeamMemberStatQuery112);
        log.info("match1 team1 user member stat rank 1008 {}", matchTeamMemberStatRank111008);
        assertThat(matchTeamMemberStatRank111008).isNotNull();
        assertThat(matchTeamMemberStatRank111008.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.01966));
        assertThat(matchTeamMemberStatRank111008.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.01966));
        assertThat(matchTeamMemberStatRank111008.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.01966));
        assertThat(matchTeamMemberStatRank111008.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.01379));
        assertThat(matchTeamMemberStatRank111008.getDayTrend()).isEqualByComparingTo(-1);
        assertThat(matchTeamMemberStatRank111008.getWeekTrend()).isEqualByComparingTo(-1);
        assertThat(matchTeamMemberStatRank111008.getMonthTrend()).isEqualByComparingTo(-1);
        assertThat(matchTeamMemberStatRank111008.getTotalTrend()).isEqualByComparingTo(-1);

        LambdaQueryWrapper<MatchTeamMemberStatRank> matchTeamMemberStatQuery113 = new LambdaQueryWrapper<>();
        matchTeamMemberStatQuery113.select(
            MatchTeamMemberStatRank::getDayYield,
            MatchTeamMemberStatRank::getWeekYield,
            MatchTeamMemberStatRank::getMonthYield,
            MatchTeamMemberStatRank::getTotalYield,
            MatchTeamMemberStatRank::getDayRank,
            MatchTeamMemberStatRank::getLastDayRank,
            MatchTeamMemberStatRank::getWeekRank,
            MatchTeamMemberStatRank::getLastWeekRank,
            MatchTeamMemberStatRank::getMonthRank,
            MatchTeamMemberStatRank::getLastMonthRank,
            MatchTeamMemberStatRank::getTotalRank,
            MatchTeamMemberStatRank::getLastTotalRank,
            MatchTeamMemberStatRank::getDayTrend,
            MatchTeamMemberStatRank::getWeekTrend,
            MatchTeamMemberStatRank::getMonthTrend,
            MatchTeamMemberStatRank::getTotalTrend)
            .eq(MatchTeamMemberStatRank::getMatchId, 2)
            .eq(MatchTeamMemberStatRank::getTeamId, 1)
            .eq(MatchTeamMemberStatRank::getAccountId, "win_10001")
            .eq(MatchTeamMemberStatRank::getStatDate, LocalDate.of(2018, 10, 9));

        MatchTeamMemberStatRank matchTeamMemberStatRank111009 = matchTeamMemberDayStatRankMapper.selectOne(matchTeamMemberStatQuery113);
        log.info("match1 team1 user member stat rank 1009 {}", matchTeamMemberStatRank111009);
        assertThat(matchTeamMemberStatRank111009).isNotNull();
        assertThat(matchTeamMemberStatRank111009.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(0.01262));
        assertThat(matchTeamMemberStatRank111009.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.00729));
        assertThat(matchTeamMemberStatRank111009.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.00729));
        assertThat(matchTeamMemberStatRank111009.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(-0.00134));
        assertThat(matchTeamMemberStatRank111009.getDayTrend()).isEqualByComparingTo(1);
        assertThat(matchTeamMemberStatRank111009.getWeekTrend()).isEqualByComparingTo(0);
        assertThat(matchTeamMemberStatRank111009.getMonthTrend()).isEqualByComparingTo(0);
        assertThat(matchTeamMemberStatRank111009.getTotalTrend()).isEqualByComparingTo(0);

        //check match team day stat rank for team 1 in match 2
        LambdaQueryWrapper<MatchTeamDayStatRank> matchTeamDayStatQuery11 = new LambdaQueryWrapper<>();
        matchTeamDayStatQuery11.select(
            MatchTeamDayStatRank::getDayYield,
            MatchTeamDayStatRank::getWeekYield,
            MatchTeamDayStatRank::getMonthYield,
            MatchTeamDayStatRank::getTotalYield,
            MatchTeamDayStatRank::getDayRank,
            MatchTeamDayStatRank::getLastDayRank,
            MatchTeamDayStatRank::getWeekRank,
            MatchTeamDayStatRank::getLastWeekRank,
            MatchTeamDayStatRank::getMonthRank,
            MatchTeamDayStatRank::getLastMonthRank,
            MatchTeamDayStatRank::getTotalRank,
            MatchTeamDayStatRank::getLastTotalRank,
            MatchTeamDayStatRank::getDayTrend,
            MatchTeamDayStatRank::getWeekTrend,
            MatchTeamDayStatRank::getMonthTrend,
            MatchTeamDayStatRank::getTotalTrend)
            .eq(MatchTeamDayStatRank::getMatchId, 2)
            .eq(MatchTeamDayStatRank::getTeamId, 1);
        MatchTeamDayStatRank matchTeamDayStatRank = matchTeamDayStatRankMapper.selectOne(matchTeamDayStatQuery11);
        log.info("match team day stat rank {}", matchTeamDayStatRank);
        assertThat(matchTeamDayStatRank).isNotNull();
        assertThat(matchTeamDayStatRank.getDayYield()).isEqualByComparingTo(BigDecimal.valueOf(0.00063));
        assertThat(matchTeamDayStatRank.getWeekYield()).isEqualByComparingTo(BigDecimal.valueOf(0.00063));
        assertThat(matchTeamDayStatRank.getMonthYield()).isEqualByComparingTo(BigDecimal.valueOf(0.00063));
        assertThat(matchTeamDayStatRank.getTotalYield()).isEqualByComparingTo(BigDecimal.valueOf(0.00093));
        assertThat(matchTeamDayStatRank.getDayRank()).isEqualByComparingTo(1);
        assertThat(matchTeamDayStatRank.getWeekRank()).isEqualByComparingTo(1);
        assertThat(matchTeamDayStatRank.getMonthRank()).isEqualByComparingTo(1);
        assertThat(matchTeamDayStatRank.getTotalRank()).isEqualByComparingTo(1);
        assertThat(matchTeamDayStatRank.getDayTrend()).isEqualByComparingTo(0);
        assertThat(matchTeamDayStatRank.getWeekTrend()).isEqualByComparingTo(0);
        assertThat(matchTeamDayStatRank.getMonthTrend()).isEqualByComparingTo(0);
        assertThat(matchTeamDayStatRank.getTotalTrend()).isEqualByComparingTo(1);
    }
}