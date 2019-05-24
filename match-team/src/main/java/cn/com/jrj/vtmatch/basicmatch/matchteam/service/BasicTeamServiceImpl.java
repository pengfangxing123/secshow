package cn.com.jrj.vtmatch.basicmatch.matchteam.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.com.jrj.vtmatch.basicmatch.helper.StringRedisHelper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchBasicMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamBasicMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamJoinMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchBasic;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamBasic;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamJoinHistory;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUser;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.WechatTeamJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.enums.RankType;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IMatchJoinService;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IMatchTeamBasicService;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IMatchTeamJoinHistoryService;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IMatchTeamJoinService;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IMatchUserService;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IWechatTeamJoinService;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.CacheConstants;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.ErrorMsg;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.JsonResult;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchMemberRankVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchTeamBasicVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchTeamInfo;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchTeamManageVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.TeamMemberRankVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.UserJoinTeamInfo;
import cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces.BasicTeamService;
import cn.com.jrj.vtmatch.basicmatch.matchteam.config.TeamCache;
import lombok.extern.slf4j.Slf4j;


/**
 * 比赛团队业务逻辑
 *
 * @author NL
 * @date 2018-10-30 11:13:44
 */
@Service
@Slf4j
public class BasicTeamServiceImpl implements BasicTeamService {

    public static final String MATCH_ID = "match_id";
    @Resource
    private IWechatTeamJoinService iWechatTeamJoinService;

    @Resource
    private IMatchUserService iMatchUserService;
    @Resource
    private IMatchJoinService iMatchJoinService;
    @Resource
    private IMatchTeamJoinHistoryService iMatchTeamJoinHistoryService;

    @Resource
    private IMatchTeamJoinService iMatchTeamJoinService;

    @Resource
    private IMatchTeamBasicService iMatchTeamBasicService;

    @Resource
    private MatchTeamBasicMapper matchTeamBasicMapper;
    @Resource
    private MatchTeamJoinMapper matchTeamJoinMapper;
    @Resource
    private MatchBasicMapper matchBasicMapper;
    @Resource
    private StringRedisHelper stringRedisHelper;

    @Override
    public JsonResult joinTeam(long matchId, long teamId, long userId) throws Exception {
        MatchBasic matchBasic = matchBasicMapper.selectById(matchId);
        if(matchBasic.getIsDeleted() == 1){
            return JsonResult.buildFail(ErrorMsg.MATCH_MAY_BE_DELETE_ERROR);
        }

        if(LocalDate.now().isBefore(matchBasic.getApplyStartDate())){
            return JsonResult.buildFail("还未开始报名，请于 " +
                    (matchBasic.getApplyStartDate().toEpochDay() - LocalDate.now().toEpochDay() + 1) +
                    " 天后报名比赛");
        }
        if(matchBasic.getApplyEndDate().isBefore(LocalDate.now())){
            return JsonResult.buildFail("报名已结束，请关注其他比赛");
        }

        //先报名团队赛，再参加战队
        QueryWrapper<MatchJoin> queryWrapperMatchJoin = new QueryWrapper<>();
        queryWrapperMatchJoin.eq("match_id", matchId)
                .eq("user_id", userId)
                .eq("is_deleted", 0);
        MatchJoin matchJoin = iMatchJoinService.getOne(queryWrapperMatchJoin);
        if (matchJoin == null) {
            return JsonResult.buildFail(ErrorMsg.USER_NO_JOINED_MATCH_ERROR);
        }

        //查询是否参加战队，未参加的直接加入战队
        QueryWrapper<MatchTeamJoin> queryWrapperMatchTeamJoin = new QueryWrapper<>();
        queryWrapperMatchTeamJoin
                .eq(MATCH_ID, matchId)
                .eq("user_id", userId)
                .eq("account_id", matchJoin.getAccountId());
        MatchTeamJoin matchTeamJoin = iMatchTeamJoinService.getOne(queryWrapperMatchTeamJoin);
        if (matchTeamJoin == null) {
            //未参加战队的，直接加入战队

            //查询账户
//            QueryWrapper<MatchUserAccount> queryWrapperMatchUserAccount = new QueryWrapper<>();
//            queryWrapperMatchUserAccount
//                    .eq("user_id", userId)
//                    //默认账户
//                    .eq("is_default", 1);
//            MatchUserAccount matchUserAccount = iMatchUserAccountService.getOne(queryWrapperMatchUserAccount, true);
//            if (matchUserAccount == null) {
//                throw new MatchUserAccountNotExistException("" + userId);
//            }

            MatchTeamJoin oldMatchTeamJoin = new MatchTeamJoin();
            oldMatchTeamJoin.setMatchId(matchId);
            oldMatchTeamJoin.setTeamId(teamId);
            oldMatchTeamJoin.setUserId(userId);
            oldMatchTeamJoin.setAccountId(matchJoin.getAccountId());
            oldMatchTeamJoin.setJoinDate(LocalDateTime.now());
            oldMatchTeamJoin.setJoinStatus(1);

            if (iMatchTeamJoinService.save(oldMatchTeamJoin)) {
                addTeamJoinHistory(oldMatchTeamJoin);
                deleteUserJoinRedis(matchId, userId);
                matchTeamBasicMapper.updateTeamMember(teamId);
                return JsonResult.buildSuccess();
            } else {
                return JsonResult.buildFail(ErrorMsg.SYSTEM_EXCEPTION);
            }
            //离开战队后，重新加入
        } else if (matchTeamJoin.getJoinStatus() == 0) {
            matchTeamJoin.setTeamId(teamId);
            matchTeamJoin.setQuitDate(null);
            matchTeamJoin.setJoinDate(LocalDateTime.now());
            matchTeamJoin.setJoinStatus(1);
            if (iMatchTeamJoinService.updateById(matchTeamJoin)) {
                addTeamJoinHistory(matchTeamJoin);
                deleteUserJoinRedis(matchId, userId);
                matchTeamBasicMapper.updateTeamMember(teamId);
                return JsonResult.buildSuccess();
            } else {
                return JsonResult.buildFail(ErrorMsg.SYSTEM_EXCEPTION);
            }
        } else {
            //已参加该比赛战队的，报错
            if (teamId == matchTeamJoin.getTeamId()) {
                return JsonResult.buildFail(ErrorMsg.USER_JOINED_ERROR);
            } else {
                return JsonResult.buildFail(ErrorMsg.USER_JOIN_OTHER_TEAM_ERROR);
            }
        }
    }

    @Override
    public JsonResult quitTeam(long matchId, long teamId, long userId) throws Exception {
        MatchBasic match = matchBasicMapper.selectById(matchId);
        int quitDays = 0;
        if (match != null && match.getMatchStatus() > 0 && !StringUtils.isBlank(match.getRuleConfig())) {
            quitDays = getChangeTeamInterval(match.getRuleConfig());
        }

        //先报名团队赛，再参加战队
        QueryWrapper<MatchTeamJoin> queryWrapperMatchJoin = new QueryWrapper<>();
        queryWrapperMatchJoin.eq("match_id", matchId)
                .eq("team_id", teamId)
                .eq("user_id", userId)
                .eq("join_status", 1);
        MatchTeamJoin teamJoin = iMatchTeamJoinService.getOne(queryWrapperMatchJoin);
        if (teamJoin == null) {
            return JsonResult.buildFail(ErrorMsg.USER_NO_JOINED_MATCH_ERROR);
        }
        //int inTeamDays = Period.between(teamJoin.getJoinDate().toLocalDate(), LocalDate.now()).getDays();
        MatchTeamBasic team = iMatchTeamBasicService.getById(teamId);
        if (team == null) {
            log.info("战队不存在");
            return JsonResult.buildFail(ErrorMsg.SYSTEM_EXCEPTION);
        }
        if (team.getMasterId().equals(userId)) {
            log.info("战队队长，不能离开战队");
            return JsonResult.buildFail(ErrorMsg.MASTER_DOT_QUIT_TEAM_ERROR);
        }
        long inTeamDays = LocalDate.now().toEpochDay() - teamJoin.getJoinDate().toLocalDate().toEpochDay();
        if (inTeamDays < quitDays) {
            long qd = quitDays - inTeamDays;
            return JsonResult.buildFail("您在" + qd + "天后才能更换战队！");
        }
        teamJoin.getJoinDate();

        //查询是否参加战队，未参加的直接加入战队
        QueryWrapper<MatchTeamJoin> queryWrapperMatchTeamJoin = new QueryWrapper<>();
        queryWrapperMatchTeamJoin
                .eq("match_id", matchId)
                .eq("user_id", userId)
                .eq("account_id", teamJoin.getAccountId())
                .eq("team_id", teamId);
        MatchTeamJoin matchTeamJoin = iMatchTeamJoinService.getOne(queryWrapperMatchTeamJoin);
        if (matchTeamJoin != null && matchTeamJoin.getJoinStatus() == 1) {
            matchTeamJoin.setQuitDate(LocalDateTime.now());
            matchTeamJoin.setJoinStatus(0);
            if (iMatchTeamJoinService.updateById(matchTeamJoin)) {
                addTeamJoinHistory(matchTeamJoin);
                deleteUserJoinRedis(matchId, userId);
                matchTeamBasicMapper.updateTeamMember(teamId);
                return JsonResult.buildSuccess();
            } else {
                return JsonResult.buildFail(ErrorMsg.SYSTEM_EXCEPTION);
            }
        } else {
            //已参加该比赛战队的，报错
            return JsonResult.buildFail(ErrorMsg.USER_NO_JOINED_TEAM_ERROR);
        }
    }


    private int getChangeTeamInterval(String ruleConfig) {
        try {
            JSONObject config = JSONObject.parseObject(ruleConfig);
            int days = config.getJSONObject("team").getJSONObject("rule").getIntValue("change_team_interval");
            return days;
        } catch (Exception e) {
            // TODO: handle exception
            log.info("");
        }
        return 0;
    }

    /**
     * 添加加入战队的历史记录
     *
     * @param matchTeamJoin
     */
    private void addTeamJoinHistory(MatchTeamJoin matchTeamJoin) {
        MatchTeamJoinHistory matchTeamJoinHistory = new MatchTeamJoinHistory();
        matchTeamJoinHistory.setAccountId(matchTeamJoin.getAccountId());
        matchTeamJoinHistory.setMatchId(matchTeamJoin.getMatchId());
        matchTeamJoinHistory.setTeamId(matchTeamJoin.getTeamId());
        matchTeamJoinHistory.setUserId(matchTeamJoin.getUserId());
        matchTeamJoinHistory.setJoinStatus(matchTeamJoin.getJoinStatus());
        try {
            iMatchTeamJoinHistoryService.save(matchTeamJoinHistory);
        } catch (Exception e) {
            // TODO: handle exception
            log.info("MatchTeamJoinHistory save fail");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult weixinJoin(long matchId, long teamId, String unionId) throws Exception {

        if (StringUtils.isBlank(unionId)) {
            return JsonResult.buildFail("unionId can not be empty");
        }

        WechatTeamJoin wechatTeamJoin = new WechatTeamJoin();
        wechatTeamJoin.setMatchId(matchId);
        wechatTeamJoin.setUnionId(unionId);
        wechatTeamJoin.setTeamId(teamId);

        //根据openid反查用户
        QueryWrapper<MatchUser> queryWrapperMatchUser = new QueryWrapper<>();
        queryWrapperMatchUser
                .eq("union_id", unionId);
        queryWrapperMatchUser.select("id");
        MatchUser matchUser = iMatchUserService.getOne(queryWrapperMatchUser, true);

        if (matchUser == null) {
            // 不是注册用户
            //是否已经报名
            QueryWrapper<WechatTeamJoin> queryWrapperWechat = new QueryWrapper<>();
            queryWrapperWechat
                    .eq("union_id", unionId)
                    .eq(MATCH_ID, matchId)
                    .eq("team_id", teamId);
            if (iWechatTeamJoinService.count(queryWrapperWechat) > 0) {
                return JsonResult.buildFail(ErrorMsg.ALREADY_JOINING_SYSTEM_ERROR);
            }

            //直接保存微信openid、战队对应关系
            if (iWechatTeamJoinService.save(wechatTeamJoin)) {
                return JsonResult.buildSuccess();
            } else {
                return JsonResult.buildFail(ErrorMsg.SYSTEM_EXCEPTION);
            }
        } else {
            //老用户直接加入战队
            return joinTeam(matchId, teamId, matchUser.getId());

        }
    }

    @Override
    public JsonResult createTeam(MatchTeamBasic mtb) throws Exception {
        log.info("createTeam param : {} ", JSONObject.toJSONString(mtb));
        //验证是否已经创建此比赛
        QueryWrapper<MatchTeamBasic> myTeamWrapperMatch = new QueryWrapper<>();
        myTeamWrapperMatch
                .eq(MATCH_ID, mtb.getMatchId())
                .eq("master_id", mtb.getMasterId())
                .eq("is_deleted", 0);
        MatchTeamBasic myMatchTeamBasic = iMatchTeamBasicService.getOne(myTeamWrapperMatch, true);
        if (myMatchTeamBasic != null) {
            log.info("您已经创建过此比赛的战队，请确认");
            return JsonResult.buildFail("您已经创建过此比赛的战队，请确认");
        }

        //验证重名
        QueryWrapper<MatchTeamBasic> queryWrapperMatchTeamBasic = new QueryWrapper<>();
        queryWrapperMatchTeamBasic
                .eq("team_name", mtb.getTeamName())
                .eq(MATCH_ID, mtb.getMatchId())
                .eq("is_deleted", 0);

        if (mtb.getId() > 0) {
            queryWrapperMatchTeamBasic.ne("id", mtb.getId());
        }
        int linkNameNum = iMatchTeamBasicService.count(queryWrapperMatchTeamBasic);

        if (linkNameNum > 0) {
            log.info("团队名称，已经被占用");
            return JsonResult.buildFail("团队名称，已经被占用");
        }

        if (StringUtils.isBlank(mtb.getAccountId())) {
            QueryWrapper<MatchJoin> queryWrapperMatchJoin = new QueryWrapper<>();
            queryWrapperMatchJoin.eq("match_id", mtb.getMatchId())
                    .eq("user_id", mtb.getMasterId())
                    .eq("is_deleted", 0);
            MatchJoin matchJoin = iMatchJoinService.getOne(queryWrapperMatchJoin);
            if (matchJoin == null) {
                return JsonResult.buildFail("未查询到此用户参赛信息");
            }
            mtb.setAccountId(matchJoin.getAccountId());
        }

        if (mtb.getId() > 0) {
            iMatchTeamBasicService.updateById(mtb);
        } else {
            if (iMatchTeamBasicService.save(mtb)) {
                joinTeam(mtb.getMatchId(), mtb.getId(), mtb.getMasterId());
                stringRedisHelper.delete(TeamCache.getDiscorveryTeamKey(mtb.getMatchId()));
            }
        }
        return JsonResult.buildSuccess(mtb);
    }

    /**
     * 查询比赛信息
     *
     * @return
     */
    @Override
    public MatchTeamBasicVO getTeamBasicInfo(long matchId, long userId) throws Exception {
        QueryWrapper<MatchUser> queryWrapperMatchUser = new QueryWrapper<>();
        queryWrapperMatchUser
                .eq("id", userId);
        queryWrapperMatchUser.select("id,head_pic,nick_name,phone_no");
        MatchUser matchUser = iMatchUserService.getOne(queryWrapperMatchUser, true);
        MatchTeamBasicVO vo = new MatchTeamBasicVO();
        vo.setMasterId(userId);
        vo.setMatchId(matchId);
        if (matchUser != null) {
            vo.setHeadPic(matchUser.getHeadPic());
            vo.setNickName(matchUser.getNickName());
        }

        QueryWrapper<MatchTeamBasic> queryWrapperMatchTeamBasic = new QueryWrapper<>();
        queryWrapperMatchTeamBasic
                .eq(MATCH_ID, matchId)
                .eq("master_id", matchUser.getId())
                .eq("is_deleted", 0);
        MatchTeamBasic matchTeamBasic = iMatchTeamBasicService.getOne(queryWrapperMatchTeamBasic, true);
        if (matchTeamBasic != null) {
            vo.setId(matchTeamBasic.getId());
            vo.setTeamName(matchTeamBasic.getTeamName());
            vo.setDeclaration(matchTeamBasic.getDeclaration());
        }
        return vo;
    }

    @Override
    public JsonResult discoveryTeamInfo(long matchId) {
        String key = TeamCache.getDiscorveryTeamKey(matchId);
        if (!StringUtils.isBlank(key)) {
            String value = stringRedisHelper.get(key);
            if (!StringUtils.isBlank(value)) {
                JSONArray arr = JSONArray.parseArray(value);
                Collections.shuffle(arr);
                return JsonResult.buildSuccess(arr);
            }
        }
        IPage<MatchTeamInfo> queryTeamRank =  matchTeamBasicMapper.selectTeamList(new Page<>(1,1000), matchId);
        stringRedisHelper.set(key, JSONObject.toJSONString(queryTeamRank.getRecords()), 1200);
        return JsonResult.buildSuccess(queryTeamRank.getRecords());
    }


    @Cacheable(
            value = CacheConstants.MATCH_REDIS_MATCH_TEAM_INFO_KEY,
            key = "#teamId",
            unless = "#result==null"
    )
    @Override
    public MatchTeamInfo queryMatchTeamInfo(long teamId) {
        return matchTeamBasicMapper.selectMatchTeamInfo(teamId);
    }

    @Cacheable(
            value = CacheConstants.MATCH_REDIS_USER_TEAM_RANKINFO_KEY,
            key = "T(String).valueOf(#userId).concat('::').concat(#teamId)",
            condition = "#userId!=0 and #teamId!=0",
            unless = "#result==null"
    )
    @Override
    public TeamMemberRankVO queryUserTeamMemberRank(long teamId, long userId) {
    	try {
    		return matchTeamBasicMapper.queryUserTeamMemberRank(teamId, userId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	return null;
    }

    @Override
    public IPage<MatchTeamInfo> queryTeamRank(Page<MatchTeamInfo> page, RankType type, long matchId) {
        IPage<MatchTeamInfo> result;
        switch (type) {
            default:
            case DAY:
                page.setAsc("r.day_rank");
                result = matchTeamBasicMapper.selectTeamRank(page, matchId);
                break;
            case WEEK:
                page.setAsc("r.week_rank");
                result = matchTeamBasicMapper.selectTeamRank(page, matchId);
                break;
            case MONTH:
                page.setAsc("r.month_rank");
                result = matchTeamBasicMapper.selectTeamRank(page, matchId);
                break;
            case TOTAL:
                page.setAsc("r.total_rank");
                result = matchTeamBasicMapper.selectTeamRank(page, matchId);
                break;
            case MEMBERNUM:
                page.setDesc("b.member_num");
                result = matchTeamBasicMapper.selectTeamRankByMemberNum(page, matchId);
                break;
        }
        return result;
    }

    public void deleteUserJoinRedis(long matchId, long userId) {
        String key = TeamCache.getUserJoinTeamInfoKey(matchId, userId);
        stringRedisHelper.delete(key);
    }

    @Override
    public UserJoinTeamInfo queryUserJoinTeamInfo(long matchId, long userId) {
        return matchTeamJoinMapper.queryUserJoinTeamInfo(matchId, userId);
    }

    @Override
    public IPage<TeamMemberRankVO> queryTeamMemberRank(Page<TeamMemberRankVO> page, RankType type, long matchId, long teamId, Long currentUserId) {
        IPage<TeamMemberRankVO> result;
        switch (type) {
            default:
            case DAY:
                page.setAsc("day_rank");
                result = matchTeamBasicMapper.selectTeamMemberRank(page, matchId, teamId, currentUserId);
                break;
            case WEEK:
                page.setAsc("week_rank");
                result = matchTeamBasicMapper.selectTeamMemberRank(page, matchId, teamId, currentUserId);
                break;
            case MONTH:
                page.setAsc("month_rank");
                result = matchTeamBasicMapper.selectTeamMemberRank(page, matchId, teamId, currentUserId);
                break;
            case TOTAL:
                page.setAsc("total_rank");
                result = matchTeamBasicMapper.selectTeamMemberRank(page, matchId, teamId, currentUserId);
                break;
        }
        return result;
    }

    @Override
    public TeamMemberRankVO queryTeamMemberRankByUserId(RankType type, long matchId, long teamId, Long currentUserId) {
        TeamMemberRankVO result;
        switch (type) {
            default:
            case DAY:
                result = matchTeamBasicMapper.selectTeamMemberRankByUserId( matchId, teamId, currentUserId);
                break;
            case WEEK:
                result = matchTeamBasicMapper.selectTeamMemberRankByUserId( matchId, teamId, currentUserId);
                break;
            case MONTH:
                result = matchTeamBasicMapper.selectTeamMemberRankByUserId( matchId, teamId, currentUserId);
                break;
            case TOTAL:
                result = matchTeamBasicMapper.selectTeamMemberRankByUserId( matchId, teamId, currentUserId);
                break;
        }
        return result;
    }

    @Override
    public IPage<MatchMemberRankVO> queryMatchMemberRank(Page<MatchMemberRankVO> page, RankType type, long matchId) {
        IPage<MatchMemberRankVO> result;
        switch (type) {
            default:
            case DAY:
                page.setAsc("day_rank");
                result = matchTeamBasicMapper.selectMatchMemberRank(page, matchId);
                break;
            case WEEK:
                page.setAsc("week_rank");
                result = matchTeamBasicMapper.selectMatchMemberRank(page, matchId);
                break;
            case MONTH:
                page.setAsc("month_rank");
                result = matchTeamBasicMapper.selectMatchMemberRank(page, matchId);
                break;
            case TOTAL:
                page.setAsc("total_rank");
                result = matchTeamBasicMapper.selectMatchMemberRank(page, matchId);
                break;
        }
        return result;
    }

    @Override
    public IPage<MatchTeamManageVO> page(Page<MatchTeamManageVO> pageHelp, MatchTeamBasic matchTeamBasic) {
        return pageHelp.setRecords(this.matchTeamBasicMapper.selectTeamListPage(pageHelp, matchTeamBasic));
    }

    @Override
    public MatchTeamBasic getById(Long id) {
        return this.matchTeamBasicMapper.selectById(id);
    }

    @Override
    public void lockTeam(MatchTeamBasic team) {
        Integer isLocked = team.getIsLocked() == 1 ? 0 : 1;
        team.setIsLocked(isLocked);
        this.matchTeamBasicMapper.updateById(team);
    }

    @Override
    public int teamCount(String teamName) {
        QueryWrapper<MatchTeamBasic> teamWrapper = new QueryWrapper<>();
        teamWrapper.eq("team_name", teamName);
        return this.matchTeamBasicMapper.selectCount(teamWrapper);
    }

    @Override
    public int matchAvaliTeamCount(Long matchId){
        QueryWrapper<MatchTeamBasic> teamWrapper = new QueryWrapper<>();
        teamWrapper.eq("match_id", matchId)
                .eq("isDeleted",0);
        return this.matchTeamBasicMapper.selectCount(teamWrapper);
    }

    @Override
    public void updateById(MatchTeamBasic team) {
        this.matchTeamBasicMapper.updateById(team);
    }

	@Override
	public MatchTeamManageVO getTeamInfoById(long id) {
		return matchTeamBasicMapper.getTeamInfoById(id);
	}

}
