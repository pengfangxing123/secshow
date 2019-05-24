package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchBasic;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.YcRedDetailVO;

/**
 * <p>
 * 大赛基本表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
public interface MatchBasicMapper extends BaseMapper<MatchBasic> {

    List<MatchBasic> selectAllAvailable(@Param("date") LocalDate date);

    MatchBasic selectByMatchId(@Param("matchId") Integer matchId);

    List<MatchBasic> selectAvailableByMatchIds(@Param("date") LocalDate date, @Param("matchIds") List<Integer> matchIds);

	void updateTeamRule(@Param("id")Long id, @Param("rule_config")String rule_config);

    int startReport(@Param("applyStartDate")String reportStartDate);

    int startMatch(@Param("startDate")String startDate);

    int reStartMatch();

    int deleteMatchById(@Param("matchId") Long matchId);

    int endMatch(@Param("endDate")String endDate);
    
    List<YcRedDetailVO> selectMatchRedList(@Param("matchId") Long matchId);

    void updateJoinMatchNum(@Param("id")Long id);

    int endMatchAndSetEndDateById(@Param("id")Long id,@Param("endDate")String endDate);
}
