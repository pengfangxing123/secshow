package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamBasic;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchMemberRankVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchTeamInfo;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchTeamManageVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.TeamMemberRankVO;

/**
 * <p>
 * 战队基础信息表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
public interface MatchTeamBasicMapper extends BaseMapper<MatchTeamBasic> {

    List<MatchTeamBasic> selectAvailableTeam(@Param("matchId") Integer matchId);
    
    IPage<MatchTeamInfo> selectTeamList(Page page,@Param("matchId") long matchId);
    
    MatchTeamInfo  selectMatchTeamInfo(@Param("teamId") long teamId);
    
    
    IPage<MatchTeamInfo> selectTeamRank(Page page,@Param("matchId") long matchId);
    
    IPage<MatchTeamInfo> selectTeamRankByMemberNum(Page page,@Param("matchId") long matchId);

    IPage<TeamMemberRankVO> selectTeamMemberRank(Page page,@Param("matchId") long matchId,@Param("teamId") long teamId,@Param("currentUserId") Long currentUserId);

    IPage<MatchMemberRankVO> selectMatchMemberRank(Page page,@Param("matchId") long matchId);
    
    TeamMemberRankVO queryUserTeamMemberRank(@Param("teamId") long teamId,@Param("userId") long userId);

	List<MatchTeamManageVO> selectTeamListPage(Page<MatchTeamManageVO> pageHelp, @Param("matchTeamBasic")MatchTeamBasic matchTeamBasic);

	MatchTeamManageVO getTeamInfoById(@Param("id")Long id);
	
	void updateTeamMember(@Param("teamId")Long teamId);

    TeamMemberRankVO selectTeamMemberRankByUserId(@Param("matchId") long matchId,@Param("teamId") long teamId,@Param("currentUserId") long currentUserId);
}
