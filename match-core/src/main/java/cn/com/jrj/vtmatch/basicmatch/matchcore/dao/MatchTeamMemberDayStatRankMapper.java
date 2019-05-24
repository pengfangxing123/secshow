package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamMemberDayStatRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 团队成员日收益排行表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
public interface MatchTeamMemberDayStatRankMapper extends BaseMapper<MatchTeamMemberDayStatRank> {

    int insertOrUpdateBatch(List<MatchTeamMemberDayStatRank> list);
    int batchInsert(List<MatchTeamMemberDayStatRank> list);
    int deleteByMatchId(Long matchId);
}
