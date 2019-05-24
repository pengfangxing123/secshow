package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccountDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.UserStatRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户账户日收益排名 Mapper 接口
 * </p>
 *
 * @author yuan.cheng
 */
public interface MatchUserAccountDayStatRankMapper extends BaseMapper<MatchUserAccountDayStatRank> {

    UserStatRank selectByUserId(@Param("userId") Long userId);

    IPage<UserStatRank> selectDayRankOrderPage(Page page);
    IPage<UserStatRank> selectWeekRankOrderPage(Page page);
    IPage<UserStatRank> selectMonthRankOrderPage(Page page);
    IPage<UserStatRank> selectTotalRankOrderPage(Page page);

    int insertOrUpdateBatch(List<MatchUserAccountDayStatRank> list);
    int batchInsert(List<MatchUserAccountDayStatRank> list);
    int deleteAll();
}
