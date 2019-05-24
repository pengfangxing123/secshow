package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamMemberStatRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 团队成员收益排行表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
public interface MatchTeamMemberStatRankMapper extends BaseMapper<MatchTeamMemberStatRank> {

    List<MatchTeamMemberStatRank> selectLastStat(@Param("matchId") Long matchId, @Param("date") LocalDate date);

    int batchInsert(List<MatchTeamMemberStatRank> list);

    int deleteByDateAndMatchIds(@Param("date") LocalDate date, @Param("matchIds") List<Integer> matchIds);

    int deleteByDate(@Param("date") LocalDate date);
}
