package cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces;

import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.JsonResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamBasic;
import cn.com.jrj.vtmatch.basicmatch.matchcore.enums.RankType;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchMemberRankVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchTeamBasicVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchTeamInfo;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchTeamManageVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.TeamMemberRankVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.UserJoinTeamInfo;

/**
 * 基础比赛团队 service.
 */
public interface BasicTeamService {

    /**
     * 加入团队
     *
     * @param matchId 比赛id
     * @param teamId  团队id
     * @param userId  用户id
     * @return JsonResult
     */
    JsonResult joinTeam(long matchId, long teamId, long userId) throws Exception;

    /**
     * 微信分享参加战队接口
     *
     * @param matchId 比赛ID
     * @param teamId  团队ID
     * @param unionId  微信unionId
     * @return boolean
     */
    JsonResult weixinJoin(long matchId, long teamId, String unionId) throws Exception;

    /**
     * 创建战队比赛中团队
     *
     * @param mtb
     * @return
     * @throws Exception
     */
    JsonResult createTeam(MatchTeamBasic mtb) throws Exception;

    /**
     * 查询比赛信息和用户信息
     *
     * @param matchId 比赛Id
     * @param userId  用户Id
     * @return
     * @throws Exception
     */
    MatchTeamBasicVO getTeamBasicInfo(long matchId, long userId) throws Exception;

    /**
     * 离开战队
     * @param matchId 比赛id
     * @param teamId  团队id
     * @param userId  用户id
     * @return
     * @throws Exception
     */
	JsonResult quitTeam(long matchId, long teamId, long userId) throws Exception;

	/**
	 * 发现战队
	 * @param matchId
	 * @return
	 */
	JsonResult discoveryTeamInfo(long matchId);

	/**
	 * 查询战队信息
	 * @return
	 */
	MatchTeamInfo queryMatchTeamInfo(long teamId);

	/**
	 * 战队分页查询
	 * @param page
	 * @param type
	 * @return
	 */
	IPage<MatchTeamInfo> queryTeamRank(Page<MatchTeamInfo> page, RankType type,long matchId);

	/**
	 * 获取用户的加入某个比赛的参赛信息
	 * @param matchId
	 * @param userId
	 * @return
	 */
	UserJoinTeamInfo queryUserJoinTeamInfo(long matchId, long userId);

	/**
	 * 团队成员排行版
	 * @param page
	 * @param type
	 * @param matchId
	 * @param teamId
	 * @param currentUserId
	 * @return
	 */
	IPage<TeamMemberRankVO> queryTeamMemberRank(Page<TeamMemberRankVO> page, RankType type, long matchId, long teamId, Long currentUserId);


	/**
	 * 用户在团队成员排行信息
	 * @param type
	 * @param matchId
	 * @param teamId
	 * @param currentUserId
	 * @return
	 */
	TeamMemberRankVO queryTeamMemberRankByUserId(RankType type, long matchId, long teamId, Long currentUserId);


	/**
	 * 比赛成员排行版
	 * @param page
	 * @param type
	 * @param matchId
	 * @return
	 */
	IPage<MatchMemberRankVO> queryMatchMemberRank(Page<MatchMemberRankVO> page, RankType type, long matchId);

	/**
	 * 查询用户在某个战队中排名信息
	 * @param teamId
	 * @param userId
	 * @return
	 */
	TeamMemberRankVO queryUserTeamMemberRank(long teamId, long userId);

	/**
	 * 后台分页条件查询战队
	 * @param pageHelp
	 * @param matchTeamBasic
	 * @return
	 */
	IPage<MatchTeamManageVO> page(Page<MatchTeamManageVO> pageHelp, MatchTeamBasic matchTeamBasic);

	/**
	 * 后台根据id查询战队
	 * @param id
	 * @return
	 */
	MatchTeamBasic getById(Long id);

	/**
	 * 后台锁定战队
	 * @param team
	 */
	void lockTeam(MatchTeamBasic team);

	/**
	 * 后台根据战队名称查询
	 * @param teamName
	 * @return
	 */
	int teamCount(String teamName);

	/**
	 * 查询正常比赛战队数量
	 * @param matchId
	 * @return
	 */
	int matchAvaliTeamCount(Long matchId);

	/**
	 * 后台更新战队
	 * @param team
	 */
	void updateById(MatchTeamBasic team);

	/**
	 * 后台查询战队信息
	 * @param id
	 * @return
	 */
	MatchTeamManageVO getTeamInfoById(long id);

	

}
