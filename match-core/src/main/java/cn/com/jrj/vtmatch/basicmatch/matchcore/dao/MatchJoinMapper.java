package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.AccountTradeTimes;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.AccountMatchInfo;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.UserMatchJoinVO;

/**
 * <p>
 * 帐户加入比赛表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
public interface MatchJoinMapper extends BaseMapper<MatchJoin> {

    /**
     * 查询有效的参赛用户
     *
     * <p>其中交易次数取的match_user_account 中的trade_times</p>
     * @param matchId 比赛ID
     * @param date 清算时间
     * @return list of {@link MatchJoin}
     */
    List<MatchJoin> selectAllJoinUser(@Param("matchId") Long matchId, @Param("date") LocalDateTime date);

    int updateTradeTimesByMatchIdAndUserId(@Param("matchId") Long matchId, @Param("accountId") String accountId, @Param("times") Integer times);

    int batchUpdateTradeTimes(@Param("matchId")Long matchId, @Param("list") List<AccountTradeTimes> list);
    
    List<AccountMatchInfo> selectAccountActiveMatchList(@Param("accountId") String accountId);
    
    AccountMatchInfo selectUserMatchJoinInfo(@Param("matchId") long matchId,@Param("userId") long userId);
    
    
    IPage<UserMatchJoinVO> selectUserJoinList(Page page,@Param("matchId") Long matchId);

    int endMatchStatus(@Param("matchId") Long match_id);

    int reStartMatchStatus(@Param("matchId") Long match_id);

}
