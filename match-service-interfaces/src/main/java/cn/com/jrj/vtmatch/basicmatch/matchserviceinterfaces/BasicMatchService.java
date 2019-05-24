package cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchBasic;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.JsonResult;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 基础比赛 service.
 */
public interface BasicMatchService {

    /**
     * 新建比赛
     *
     * @return boolean
     */
    boolean createMatch();

    /**
     * 更新比赛信息
     *
     * @return boolean
     */
    boolean updateMatch();

	/**
	 * 删除比赛信息
	 *
	 * @return boolean
	 */
	boolean deleteMatch(Long matchId);

    /**
     * 列表
     *
     * @param page 分页信息
     * @return IPage
     */
    IPage<MatchBasic> matchPage(Page<MatchBasic> page);

    /**
     * 加入比赛
     *
     * @param accountId 账户id
     * @param matchId 比赛id
     * @return
     */
    boolean joinMatchByAccount(String accountId, Long matchId);

    /**
     * 加入比赛
     *
     * @param userId 用户id
     * @param matchId 比赛id
     * @return
     */
    boolean joinMatchByUser(Long userId, Long matchId);

    /**
     * 获取比赛首页红包信息
     * @param matchId
     * @return
     */
	JsonResult getRedIndexInfo(long matchId);
	
	/**
    * 新建比赛
    * @param match 比赛实体
    * @return
    */
	int createMatch(MatchBasic match);
	
	/**
	 * 更新比赛信息
	 * @param match 比赛实体
	 * @param fileFolder 文件保存路径 
	 * @return
	 * @throws Exception
	 */
	JsonResult editMatch(MatchBasic match,String fileFolder)throws Exception;

	/**
	 * 根据比赛id查询比赛信息
	 * @param id 比赛id
	 * @return MatchBasic
	 */
	MatchBasic findMatchById(Long id);

	/**
	 * 查询所有比赛
	 * @return 团队赛List
	 */
	List<MatchBasic> queryMatchList();
	
	/**
	 * 查询战队规则
	 * @param id 比赛id
	 * @return 大赛规则
	 */
	Map<String, Object> getMatchRule(Long id);

	/**
	 * 编辑战队规则
	 * @param id 比赛id  
	 * @param rule
	 */
	void updateTeamRule(Long id, Map<String, Object> rule);

	/**
	 * 是否参加了比赛
	 * @param matchId 比赛id
	 * @param userId 用户id
	 */
	boolean isJoinMatch(Long matchId, Long userId);

	/**
	 * 能否参加比赛
	 * @param matchId 比赛id
	 * @param userId 用户id
	 */
	JsonResult canJoinMatch(Long matchId, Long userId);
	
}
