package cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUser;
import cn.com.jrj.vtmatch.basicmatch.matchcore.exception.CreateUserException;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.JsonResult;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.AccountMatchInfo;

/**
 * 基础比赛用户service.
 */
public interface BasicUserService {

    /**
     * 注册
     * 流程：调用云端开户、参赛，生成对应账户关系，比赛用户信息。同时需关联微信参加战队关联表，确定是否参加战队
     *
     * @param openId    微信openid
     * @param unionId   微信unionId
     * @param headPic   头像
     * @param nickName  昵称
     * @param mobileTag 手机标记
     * @param type      类型 1普通用户 2投顾
     * @param userNo    券商用户标识id
     * @return JsonResult
     */
    JsonResult register(String openId, String unionId, String headPic, String nickName, String mobileTag, int type, String userNo) throws CreateUserException;

    /**
     * 手机号是否已经注册
     *
     * @param mobileTag 手机号标记
     * @return 已经注册true 否则false
     */
    boolean isRegisted(String mobileTag);

    /**
     * 根据手机号查询用户信息
     *
     * @param mobileTag 手机号标记
     * @return MatchUser
     */
    MatchUser queryByMobileTag(String mobileTag);


    /**
     * 获取账户比赛中的比赛信息
     *
     * @param accountId
     * @return
     */
    JsonResult queryAccountActiveMatchList(String accountId);


    /**
     * 查询用户加入比赛的情况,自身的排名,比赛信息
     *
     * @param matchId
     * @param userId
     * @return
     */
    AccountMatchInfo queryUserMatchJoinInfo(long matchId, long userId);
    
    /**
	 * 根据用户名查询用户信息
	 * @param nickName
	 * @return
	 */
	MatchUser queryUserByNickName(String nickName);

	/**
	 * 根据用户id修改用户信息
	 * @param record
	 * @return
	 */
	JsonResult updateMatchUserById(MatchUser record);

    /**
     * 更新用户登陆时间
     * @param userId
     * @return
     */
    int updateLoginTime(long userId);
}
