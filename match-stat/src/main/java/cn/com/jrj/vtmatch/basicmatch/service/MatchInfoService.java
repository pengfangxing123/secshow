package cn.com.jrj.vtmatch.basicmatch.service;

import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchBasicMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchJoinMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamJoinMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.AccountTradeTimes;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchBasic;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamJoin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 比赛信息，参赛者信息
 *
 * @author yuan.cheng
 */
@Slf4j
@Service
@RequiredArgsConstructor
class MatchInfoService {

    private final MatchBasicMapper matchBasicMapper;
    private final MatchJoinMapper matchJoinMapper;
    private final MatchTeamJoinMapper matchTeamJoinMapper;
    private final MatchUserAccountMapper matchUserAccountMapper;

    /**
     * 查询有效的比赛
     *
     * @param date     清算日期
     * @param matchIds 当为空时返回所有有效的比赛
     * @return list of {@link MatchBasic}
     */
    List<MatchBasic> queryAvailableMatchByIds(LocalDate date, List<Integer> matchIds) {
        if (CollectionUtils.isNotEmpty(matchIds)) {
            return matchBasicMapper.selectAvailableByMatchIds(date, matchIds);
        } else {
            return matchBasicMapper.selectAllAvailable(date);
        }
    }

    /**
     * 查询所有有效参赛人员
     *
     * @param matchInfo {@link MatchBasic}
     * @param date 清算日期
     * @return list of {@link MatchJoin}
     */
    List<MatchJoin> queryAvailableMatchJoin(MatchBasic matchInfo, LocalDate date) {
        List<MatchJoin> list = matchJoinMapper.selectAllJoinUser(matchInfo.getId(), date.atTime(LocalTime.MAX));
        return list.stream()
            .peek(join -> {
                //针对先报名后开赛情况，校正参赛时间
                if (join.getJoinDate().isBefore(matchInfo.getStartDate().atStartOfDay())) {
                    join.setJoinDate(matchInfo.getStartDate().atStartOfDay());
                }
            })
            //参赛后交易次数大于0的用户
            //2019-01-04 matchJoinMapper.selectAllJoinUser方法中tradeTimes取的是match_user_account表中的trade_times字段。
            //上条规则忽略，改为取账户交易次数大于0的用户
            //2019-03-12 将matchJoinMapper.selectAllJoinUser修改为原逻辑，不取match_user_account中的trade_times
            //不再过滤交易次数
            //.filter(join -> join.getTradeTimes() > 0)
            .collect(Collectors.toList());
    }

    /**
     * 查询加入团队人员
     *
     * @param date 清算时间
     * @return list of {@link MatchTeamJoin}
     */
    List<MatchTeamJoin> queryAvailableTeamJoin(Long matchId, LocalDate date) {
        return matchTeamJoinMapper.selectAllJoinUser(matchId, date.atTime(LocalTime.MAX), 1);
    }

    int updateAccountTradeTimes(List<AccountTradeTimes> accountTradeTimesList) {
        if (CollectionUtils.isEmpty(accountTradeTimesList)) {
            return 0;
        }
        return matchUserAccountMapper.batchUpdateTradeTimes(accountTradeTimesList);
    }

    int updateMatchTradeTimes(Long matchId, List<AccountTradeTimes> tradeTimeList) {
        if (CollectionUtils.isEmpty(tradeTimeList)) {
            return 0;
        }
        return matchJoinMapper.batchUpdateTradeTimes(matchId, tradeTimeList);
    }
}
