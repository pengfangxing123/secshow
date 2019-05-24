package cn.com.jrj.vtmatch.basicmatch.matchuser.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.com.jrj.vtmatch.basicmatch.config.StringRedisConfig;
import cn.com.jrj.vtmatch.basicmatch.helper.StringRedisHelper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchJoinMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.WechatTeamJoinMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUser;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccount;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.WechatTeamJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.exception.CreateUserException;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.JsonResult;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.AccountMatchInfo;
import cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces.BasicMatchService;
import cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces.BasicTeamService;
import cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces.BasicUserService;
import cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces.VTService;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class BasicUserServiceImpl implements BasicUserService {

    @Autowired
    private MatchUserMapper matchUserMapper;

    @Autowired
    private MatchJoinMapper matchJoinMapper;

    @Autowired
    private MatchUserAccountMapper matchUserAccountMapper;

    @Autowired
    private WechatTeamJoinMapper wechatTeamJoinMapper;

    @Autowired
    private BasicTeamService basicTeamService;

    @Autowired
    private BasicMatchService basicMatchService;

    @Autowired
    private VTService vtService;

    @Autowired
    private StringRedisHelper stringRedisHelper;


    @Override
    @Transactional()
    public JsonResult register(String openId, String unionId, String headPic, String nickName, String mobileTag, int type, String userNo) throws CreateUserException {
        MatchUser matchUser = new MatchUser();
        matchUser.setHeadPic(headPic);
        matchUser.setNickName(nickName);
        matchUser.setOpenId(openId);
        matchUser.setPassportId(userNo);
        matchUser.setPhoneNo(mobileTag);
        matchUser.setType(type);
        matchUser.setUnionId(unionId);
        log.info("=================== 开始注册 ==========================");
        //入库matchUser
        MatchUser dbMatchUser = queryByMobileTag(mobileTag);
        if (dbMatchUser == null) {
            if (matchUserMapper.insert(matchUser) <= 0) {
                return JsonResult.buildFail("保存用户失败：" + openId);
            }
            log.info("注册1-----保存用户：{}", matchUser.toString());
        } else {
            matchUser = dbMatchUser;
            log.info("注册1-----已存在手机号：{}", dbMatchUser.getId());
        }

        String accountId = null;
        //创建账户
        MatchUserAccount dbMmatchUserAccount = matchUserAccountMapper.selectOne(new QueryWrapper<MatchUserAccount>().eq("is_default", 1).eq("user_id", matchUser.getId()));
        if (dbMmatchUserAccount == null) {
            JsonResult vtAccountJson = vtService.register(Long.toString(matchUser.getId()));
            log.info("注册2-----创建账户：{}", vtAccountJson == null ? "" : vtAccountJson.toString());
            if (vtAccountJson == null || !JsonResult.isSuccess(vtAccountJson)) {
                throw new CreateUserException(Long.toString(matchUser.getId()), "创建云账户失败");
            }
            accountId = String.valueOf(vtAccountJson.getData());

            //记录账户对应关系
            MatchUserAccount matchUserAccount = new MatchUserAccount();
            matchUserAccount.setAccountId(accountId);
            matchUserAccount.setIsDefault(true);
            matchUserAccount.setUserId(matchUser.getId());
            //入库
            if (matchUserAccountMapper.insert(matchUserAccount) <= 0) {
                throw new CreateUserException(Long.toString(matchUser.getId()), "保存账户失败");
            }
            log.info("注册3-----记录账户对应关系：{}", matchUserAccount.toString());
        } else {
            accountId = dbMmatchUserAccount.getAccountId();
            log.info("注册3-----已存在对应关系：{}", dbMmatchUserAccount.toString());
        }

        //加入比赛
//        JSONObject joinMatchJson = vtService.joinMatch(accountId);
//        log.info("注册4-----加入比赛：{}", joinMatchJson == null ? "" : joinMatchJson.toJSONString());
//        if (joinMatchJson == null || joinMatchJson.getIntValue("retCode") == 0) {
//            if (dbMmatchUserAccount == null) {
//                throw new CreateUserException(Long.toString(matchUser.getId()), "加入云比赛失败");
//            } else {
//                log.warn("重复加入比赛：accountId---{}", accountId);
//            }
//        }

        //微信加团队逻辑处理
        IPage<WechatTeamJoin> page = wechatTeamJoinMapper.selectPage(
                new Page<>(1, 1),
                new QueryWrapper<WechatTeamJoin>().eq("open_id", openId).eq("status", 0)
                        .orderByDesc("id"));
        if (page == null || page.getRecords().isEmpty()) {
            return JsonResult.buildSuccess(matchUser.getId());
        }

        WechatTeamJoin wechatTeamJoin = page.getRecords().get(0);

        try {
            //加入比赛
            basicMatchService.joinMatchByUser(matchUser.getId(), wechatTeamJoin.getMatchId());

            //加入团队
            JsonResult jsonResult = basicTeamService.joinTeam(wechatTeamJoin.getMatchId(), wechatTeamJoin.getTeamId(), matchUser.getId());
            log.info("注册5-----加入团队：{}", jsonResult.toString());
            if (JsonResult.isSuccess(jsonResult)) {
                wechatTeamJoin.setStatus(1);
                wechatTeamJoinMapper.updateById(wechatTeamJoin);
                jsonResult.setData(matchUser.getId());
            } else {
                //加入战队失败也认为成功注册。
                jsonResult = JsonResult.buildSuccess();
            }
            return jsonResult;
        } catch (Exception e) {
            throw new CreateUserException(Long.toString(matchUser.getId()), "加入团队失败");
        }
    }

    @Override
    public boolean isRegisted(String mobileTag) {
        QueryWrapper<MatchUser> queryWrapperMatchUser = new QueryWrapper<>();
        queryWrapperMatchUser
                .eq("phone_no", mobileTag);
        return matchUserMapper.selectCount(queryWrapperMatchUser) > 0;
    }

    @Override
    public MatchUser queryByMobileTag(String mobileTag) {
        QueryWrapper<MatchUser> queryWrapperMatchUser = new QueryWrapper<>();
        queryWrapperMatchUser
                .eq("phone_no", mobileTag)
                .orderByDesc("id");
        return matchUserMapper.selectOne(queryWrapperMatchUser);
    }

    @Override
    public JsonResult queryAccountActiveMatchList(String accountId) {
        // TODO Auto-generated method stub
        if (accountId == null) {
            return JsonResult.buildSuccess(Collections.EMPTY_LIST);
        }

        List<AccountMatchInfo> selectAccountActiveMatchList = matchJoinMapper.selectAccountActiveMatchList(accountId);
        if (selectAccountActiveMatchList != null) {
            return JsonResult.buildSuccess(selectAccountActiveMatchList);
        } else {
            return JsonResult.buildSuccess(Collections.EMPTY_LIST);
        }
    }

    public AccountMatchInfo queryUserMatchJoinInfo(long matchId, long userId) {
        // TODO Auto-generated method stub
        String key = StringRedisConfig.REDIS_CACHE_KEY_PREFIX + "match_user_join_info_" + matchId + "_" + userId;
        String value = stringRedisHelper.get(key);
        
        if (value != null && !value.trim().equals("")) {
			return JSONObject.parseObject(value, AccountMatchInfo.class);
        }
        AccountMatchInfo userMatchJoinInfo = matchJoinMapper.selectUserMatchJoinInfo(matchId, userId);
        if (userMatchJoinInfo != null) {
            stringRedisHelper.set(key, JSONObject.toJSONString(userMatchJoinInfo), 60);
        }
        return userMatchJoinInfo;
    }

	@Override
	public MatchUser queryUserByNickName(String nickName) {
		Assert.notNull(nickName, "nickName must not be null!");
		QueryWrapper<MatchUser> queryWrapperMatchUser = new QueryWrapper<>();
        queryWrapperMatchUser.eq("nick_name", nickName);
        return matchUserMapper.selectOne(queryWrapperMatchUser);
	}

	@Override
	public JsonResult updateMatchUserById(MatchUser record) {
		Assert.notNull(record, "matchUser must not be null!");
		Assert.notNull(record.getId(),"id must not be null!");

		QueryWrapper<MatchUser> userWrapperMatch = new QueryWrapper<>();
		userWrapperMatch.eq("id", record.getId());
		MatchUser matchUser = matchUserMapper.selectOne(userWrapperMatch);
		if (Objects.isNull(matchUser)) {
			log.info("未查询到对应用户信息！id={}",record.getId());
		    return JsonResult.buildFail("未查询到对应用户信息！");
		}
		matchUserMapper.update(record, userWrapperMatch);
		return JsonResult.buildSuccess();
	}

    @Override
    public int updateLoginTime(long userId) {
        return matchUserMapper.updateLoginTime(userId);
    }
}
