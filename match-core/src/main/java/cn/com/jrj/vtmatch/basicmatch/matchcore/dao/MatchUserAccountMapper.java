package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.AccountTradeTimes;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户账户关系表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
public interface MatchUserAccountMapper extends BaseMapper<MatchUserAccount> {

    List<MatchUserAccount> selectAll();

    int updateTradeTimesByAccountId(@Param("accountId") String accountId, @Param("times") Integer times);

    int batchUpdateTradeTimes(List<AccountTradeTimes> list);

    MatchUserAccount selectByAccountId(@Param("accountId") String accountId);
}
