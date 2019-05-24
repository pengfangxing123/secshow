package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamStatRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 战队收益排行表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
public interface MatchTeamStatRankMapper extends BaseMapper<MatchTeamStatRank> {

    List<MatchTeamStatRank> selectLastStat(@Param("matchId") Long matchId, @Param("date") LocalDate date);

    int batchInsert(List<MatchTeamStatRank> list);

    int deleteByDateAndMatchIds(@Param("date") LocalDate date, @Param("matchIds") List<Integer> matchIds);

    int deleteByDate(@Param("date") LocalDate date);
}
