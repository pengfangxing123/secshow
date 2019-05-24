package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.AccountTradeTimes;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.Trading;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 调仓记录表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
public interface TradingMapper extends BaseMapper<Trading> {

    List<AccountTradeTimes> selectAccountTradeTimesByDate(@Param("date") LocalDateTime date);
    List<AccountTradeTimes> selectAccountTradeTimesByRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
