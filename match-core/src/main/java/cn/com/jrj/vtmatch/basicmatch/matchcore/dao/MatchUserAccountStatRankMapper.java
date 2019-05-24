package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccountStatRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 用户账户收益排名 Mapper 接口
 * </p>
 *
 * @author yuan.cheng
 */
public interface MatchUserAccountStatRankMapper extends BaseMapper<MatchUserAccountStatRank> {

    int batchInsert(@Param("list") List<MatchUserAccountStatRank> listToSave);

    List<MatchUserAccountStatRank> selectByDate(@Param("date") LocalDate date);

    List<MatchUserAccountStatRank> selectLastStat(@Param("date") LocalDate date);

    int deleteByDate(@Param("date") LocalDate date);
}
