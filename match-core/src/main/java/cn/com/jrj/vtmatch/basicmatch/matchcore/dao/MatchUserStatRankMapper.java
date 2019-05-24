package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserStatRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 比赛账户收益和排名 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
public interface MatchUserStatRankMapper extends BaseMapper<MatchUserStatRank> {

    List<MatchUserStatRank> selectByDate(@Param("matchId") Long matchId, @Param("date") LocalDate date);

    List<MatchUserStatRank> selectLastStat(@Param("matchId") Long matchId, @Param("date") LocalDate date);

    int batchInsert(List<MatchUserStatRank> list);

    int deleteByDateAndMatchIds(@Param("date") LocalDate date, @Param("matchIds") List<Integer> matchIds);

    int deleteByDate(@Param("date") LocalDate date);
}
