package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamDayStatRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 战队收日益排行表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
public interface MatchTeamDayStatRankMapper extends BaseMapper<MatchTeamDayStatRank> {
    int insertOrUpdateBatch(List<MatchTeamDayStatRank> list);
    int batchInsert(List<MatchTeamDayStatRank> list);
    int deleteByMatchId(Long matchId);
}
