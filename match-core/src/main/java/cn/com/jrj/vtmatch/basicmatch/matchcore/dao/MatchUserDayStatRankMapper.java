package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserDayStatRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 比赛账户收益和排名 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
public interface MatchUserDayStatRankMapper extends BaseMapper<MatchUserDayStatRank> {

    int insertOrUpdateBatch(List<MatchUserDayStatRank> list);
    int batchInsert(List<MatchUserDayStatRank> list);
    int deleteByMatchId(Long matchId);
}
