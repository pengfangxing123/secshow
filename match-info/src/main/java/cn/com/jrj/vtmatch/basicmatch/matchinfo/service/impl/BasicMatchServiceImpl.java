package cn.com.jrj.vtmatch.basicmatch.matchinfo.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchBasicMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchJoinMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamJoinMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchBasic;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccount;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.JsonResult;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchIndexRedInfoVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchTeamInfo;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.UserMatchJoinVO;
import cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces.BasicMatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * basic match service impl
 *
 * @author lei.ning
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BasicMatchServiceImpl implements BasicMatchService {

    @Autowired
    private MatchBasicMapper matchBasicMapper;

    @Autowired
    private MatchUserAccountMapper matchUserAccountMapper;

    @Autowired
    private MatchJoinMapper matchJoinMapper;

    @Autowired
    private MatchTeamJoinMapper matchTeamJoinMapper;

    @Override
    public boolean createMatch() {
        return false;
    }

    @Override
    public boolean updateMatch() {
        return false;
    }

	@Override
	public boolean deleteMatch(Long matchId) {
		Assert.notNull(matchId, "joinMatch matchId is not be null");
		return matchBasicMapper.deleteMatchById(matchId) > 0;
	}

	@Override
    public IPage<MatchBasic> matchPage(Page<MatchBasic> page) {
        return matchBasicMapper.selectPage(page, new QueryWrapper<MatchBasic>().select("id","match_name", "match_pic", "start_date", "end_date", "match_status",
                "apply_start_date", "apply_end_date", "curr_num", "summary", "award_desc", "rule_desc","type","match_pic_url","match_banner_url","weight")
				.eq("is_deleted",0)
				.orderByDesc("weight","start_date"));
    }

    @Override
    public boolean joinMatchByAccount(String accountId, Long matchId) {

        Assert.notNull(accountId, "joinMatch accountId is not be null");
        Assert.notNull(matchId, "joinMatch matchId is not be null");

        MatchUserAccount matchUserAccount = matchUserAccountMapper.selectByAccountId(accountId);
        if (matchUserAccount == null) {
            log.error("accountId is not in db");
            return false;
        }

        MatchBasic matchBasic = matchBasicMapper.selectById(matchId);
		if(matchBasic.getIsDeleted() == 1){
			log.error("match is deleted");
			return false;
		}

        if(matchBasic.getMatchStatus() ==4||matchBasic.getMatchStatus()==0){
        	log.error("match is finished");
        	return false;
        }

        if(matchBasic.getApplyEndDate().isBefore(LocalDate.now())){
			log.error("match is apply end");
			return false;
		}

      MatchJoin matchJoin = new MatchJoin();
        matchJoin.setUserId(matchUserAccount.getUserId());
        matchJoin.setMatchId(matchId);
        matchJoin.setAccountId(accountId);
        matchJoin.setJoinDate(LocalDateTime.now());

        int num = matchJoinMapper.insert(matchJoin);

        if (num > 0) {
            //更新参赛人数
            matchBasicMapper.updateJoinMatchNum(matchBasic.getId());
        }

        return num > 0;
    }

    @Override
    public boolean joinMatchByUser(Long userId, Long matchId) {
        Assert.notNull(userId, "joinMatch userId is not be null");
        Assert.notNull(matchId, "joinMatch matchId is not be null");

        try {
        	MatchUserAccount matchUserAccount = matchUserAccountMapper.selectOne(new QueryWrapper<MatchUserAccount>()
        			.eq("is_default", 1).eq("user_id", userId));
        	return joinMatchByAccount(matchUserAccount.getAccountId(), matchId);
		} catch (Exception e) {
			// TODO: handle exception
		}
        return false;
    }

	@Override
    public JsonResult getRedIndexInfo(long matchId){
    	MatchBasic match = matchBasicMapper.selectById(matchId);
    	if(match == null ||match.getMatchStatus() == 0){
    		return JsonResult.buildFail("此比赛已经删除，或者无此比赛");
    	}

    	MatchIndexRedInfoVO result = calRedInfo(match);
    	return JsonResult.buildSuccess(result);
    }

	public MatchIndexRedInfoVO calRedInfo(MatchBasic match){
		MatchIndexRedInfoVO result = new MatchIndexRedInfoVO();

		long matchId = match.getId();
    	//计算参赛人数
    	QueryWrapper<MatchJoin> queryWrapper = new QueryWrapper<MatchJoin>();
    	queryWrapper.eq("match_id", matchId).eq("is_deleted", 0);
    	int joinCount = matchJoinMapper.selectCount(queryWrapper);
    	result.setJoinCount(joinCount);

    	//计算红包总数
    	BigDecimal totalAmount =new BigDecimal(0);
    	BigDecimal preAmount =new BigDecimal(0);
		int showRedMaxPeopleNum =0;
    	JSONObject ruleJson = JSONObject.parseObject(match.getRuleConfig());
    	log.debug("match rule:matchid= {},rule={}",match.getId(),match.getRuleConfig());
    	if(match.getType() == 1){
    		preAmount = getGeneralPreRed(ruleJson);
			showRedMaxPeopleNum = getShowRedMinPeopleNum(ruleJson,joinCount);
    		totalAmount = calGeneralMatchTotalAmount(ruleJson, joinCount, preAmount);
    	}else if(match.getType() == 2){
    		preAmount = getTeamPreRed(ruleJson);
			showRedMaxPeopleNum = getTeamShowRedMinPeopleNum(ruleJson,matchId);
    		totalAmount = calTeamMatchTotalAmount(ruleJson, matchId, preAmount);
    	}

    	result.setPreRed(preAmount.setScale(2).floatValue());
    	result.setTotalAmount(totalAmount.intValue());
		result.setShowRedMinPeopleNum(showRedMaxPeopleNum);

    	//获取红包人
    	Page<UserMatchJoinVO> page = new Page<UserMatchJoinVO>();
//    	IPage<MatchTeamInfo> result = new Page<MatchTeamInfo>();
    	page.setCurrent(1);
    	page.setSize(20);

    	IPage<UserMatchJoinVO> pageResult = matchJoinMapper.selectUserJoinList(page, matchId);
    	if(result == null || pageResult.getRecords() == null){
    		result.setJoinList(Collections.emptyList());
    	}else{
    		result.setJoinList(pageResult.getRecords());
    	}
    	return result;
	}

	private int getTeamShowRedMinPeopleNum(JSONObject ruleJson,long matchId) {
    	int num = 0;
		JSONObject rule = ruleJson.getJSONObject("team").getJSONObject("rule");
		int minTeamMemberNum=0;
		if(rule != null){
			minTeamMemberNum = rule.getIntValue("constitute_num");
		}
		//查询成团的战队人数和计算成团战队的红包
		List<MatchTeamInfo> queryStatTeam = matchTeamJoinMapper.queryStatTeamMemberNum(matchId, minTeamMemberNum);
		for(MatchTeamInfo teamInfo:queryStatTeam){
			if(teamInfo.getMemberNum() > minTeamMemberNum){
				num += teamInfo.getMemberNum() - minTeamMemberNum;
			}
		}
		return num;
	}

	private int getShowRedMinPeopleNum(JSONObject ruleJson,int joinCount) {
		try {
			if(ruleJson == null || ruleJson.getJSONObject("reward") == null){
				return 0;
			}
			JSONObject reward = ruleJson.getJSONObject("reward");
			JSONObject rule = reward.getJSONObject("rule");
			int minPlay = rule.getIntValue("min_player_num");
			if(minPlay >= joinCount){
				return 0;
			}else{
				return joinCount - minPlay;
			}
		} catch (Exception e) {
			log.error("",e);
		}
		return 0;
	}

	/**
     * 普通比赛每个成员的红包
     * @param ruleJson
     * @param joinCount
     * @param preAmount
     * @return
     */
    public BigDecimal getGeneralPreRed(JSONObject ruleJson){
    	try {
    		if(ruleJson == null || ruleJson.getJSONObject("reward") == null){
    			return new BigDecimal(0);
    		}
    		JSONObject reward = ruleJson.getJSONObject("reward");
    		JSONObject rule = reward.getJSONObject("rule");
    		BigDecimal preAmount = rule.getBigDecimal("step_amount").divide(rule.getBigDecimal("step_num"));
    		return preAmount;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	return new BigDecimal(0);
    }

    /**
     * 普通比赛计算
     * @param ruleJson
     * @param joinCount
     * @param preAmount
     * @return
     */
    public BigDecimal calGeneralMatchTotalAmount(JSONObject ruleJson,long joinCount,BigDecimal preAmount){
    	if(ruleJson == null || ruleJson.getJSONObject("reward") == null){
    		return new BigDecimal(0);
    	}
    	JSONObject reward = ruleJson.getJSONObject("reward");
    	BigDecimal maxAmount = reward.getBigDecimal("max_amount");
    	BigDecimal minAmount = reward.getBigDecimal("min_amount");
    	if(maxAmount.compareTo(new BigDecimal(0)) <= 0 ){
    		return new BigDecimal(0);
    	}

    	JSONObject rule = reward.getJSONObject("rule");
    	if(rule == null){
    		return minAmount;
    	}
    	int minPlayerNum = rule.getIntValue("min_player_num");

    	BigDecimal totalAmount = new BigDecimal(0);
    	if(joinCount <= minPlayerNum){
    		return minAmount;
    	}else {
    		totalAmount = minAmount.add(preAmount.multiply(new BigDecimal(joinCount-minPlayerNum)));
    		if(totalAmount.compareTo(maxAmount) > 0){
    			totalAmount = maxAmount;
    		}
    	}
    	return totalAmount;
    }


    /**
     * 团队赛每个每个成员的红包
     * @param ruleJson
     * @param joinCount
     * @param preAmount
     * @return
     */
    public BigDecimal getTeamPreRed(JSONObject ruleJson){
    	BigDecimal preAmount = new BigDecimal(0);
    	try {
    		JSONObject constituteRule = ruleJson.getJSONObject("team").getJSONObject("constitute_reward").getJSONObject("rule");
    		preAmount = constituteRule.getBigDecimal("step_amount").divide(constituteRule.getBigDecimal("step_num"));
		} catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
			log.info("无法计算出每个成员的红包");
		}
    	return preAmount;
    }

    /**
     * 团队赛比赛计算
     * @param ruleJson
     * @param joinCount
     * @param preAmount
     * @return
     */
    public BigDecimal calTeamMatchTotalAmount(JSONObject ruleJson,long matchId,BigDecimal preAmount){
    	if(ruleJson == null || ruleJson.getJSONObject("team") == null || ruleJson.getJSONObject("personal") == null){
    		return new BigDecimal(0);
    	}
    	JSONObject reward = ruleJson.getJSONObject("personal").getJSONObject("reward");
    	//个人部分奖金
    	BigDecimal totalAmount = new BigDecimal(0);
    	if(reward != null){
    		totalAmount = reward.getBigDecimal("total");
    	}

    	//优胜部分奖金
    	JSONObject winTeamReward = ruleJson.getJSONObject("team").getJSONObject("win_team_reward");
    	if(winTeamReward != null){
    		totalAmount = totalAmount.add(winTeamReward.getBigDecimal("total"));
    	}

    	//计算单个团队红包,基础参数
    	JSONObject constituteReward = ruleJson.getJSONObject("team").getJSONObject("constitute_reward");
    	BigDecimal maxTeamAmount = new BigDecimal(0);
    	BigDecimal minTeamAmount = new BigDecimal(0);
    	if(constituteReward != null){
    		maxTeamAmount = constituteReward.getBigDecimal("max_amount");
    		minTeamAmount = constituteReward.getBigDecimal("min_amount");
    	}
    	if(maxTeamAmount.compareTo(new BigDecimal(0)) <= 0 ){
    		return totalAmount;
    	}
    	JSONObject rule = ruleJson.getJSONObject("team").getJSONObject("rule");
    	int minTeamMemberNum=0;
    	if(rule != null){
    		minTeamMemberNum = rule.getIntValue("constitute_num");
    	}

    	//查询成团的战队人数和计算成团战队的红包
    	List<MatchTeamInfo> queryStatTeam = matchTeamJoinMapper.queryStatTeamMemberNum(matchId, minTeamMemberNum);
    	for(MatchTeamInfo teamInfo:queryStatTeam){
    		BigDecimal teamTotalAmount = minTeamAmount;
    		if(teamInfo.getMemberNum() > minTeamMemberNum){
    			teamTotalAmount = minTeamAmount.add(preAmount.multiply(new BigDecimal(teamInfo.getMemberNum() - minTeamMemberNum)));
    		}
    		if(teamTotalAmount.compareTo(maxTeamAmount) > 0){
    			teamTotalAmount = maxTeamAmount;
    		}
    		totalAmount = totalAmount.add(teamTotalAmount);
    	}
    	return totalAmount;
    }

	@Override
	public int createMatch(MatchBasic match) {
		Assert.notNull(match, "match must not be null!");
		Assert.hasLength(match.getMatchName(),"matchName must not be null!");
		Assert.notNull(match.getWeight(),"weight must not be null!");
		Assert.notNull(match.getStartDate(),"startDate must not be null!");
		Assert.notNull(match.getEndDate(),"endDate must not be null!");
		Assert.notNull(match.getApplyStartDate(),"applyStartDate must not be null!");
		Assert.notNull(match.getApplyEndDate(),"applyEndDate must not be null!");
		Assert.notNull(match.getType(),"type must not be null!");
		Assert.hasLength(match.getSummary(),"summary must not be null!");
		Assert.hasLength(match.getAwardDesc(),"awardDesc must not be null!");
		Assert.notNull(match.getRuleConfig(),"ruleConfig must not be null!");
		return matchBasicMapper.insert(match);
	}

	@Override
	public JsonResult editMatch(MatchBasic match, String fileFolder) throws Exception {
		Assert.notNull(match, "match must not be null!");
		Assert.notNull(match.getId(),"id must not be null!");
		MatchBasic matchBasic = matchBasicMapper.selectOne(new QueryWrapper<MatchBasic>().eq("id", match.getId()));
		if (Objects.isNull(matchBasic)) {
			log.info("未查询到对应比赛信息！id={}",match.getId());
		    return JsonResult.buildFail("未查询到对应比赛信息！");
		}
		String oldMatchPic = matchBasic.getMatchPic();
		String oldMatchBanner = matchBasic.getMatchBanner();
		matchBasicMapper.update(match, new QueryWrapper<MatchBasic>().eq("id", match.getId()));

		if(!StringUtils.equals(match.getMatchPic(), oldMatchPic)){
	         try {
				String filePath =new StringBuffer().append(fileFolder).append(File.separator).append(oldMatchPic).toString();
				 File newFile = new File(filePath);
				 if(newFile.exists())newFile.delete();
			} catch (Exception e) {
				log.info("原图删除失败,oldMatchBanner={}",oldMatchBanner);
			}
		}
		if(!StringUtils.equals(match.getMatchBanner(), oldMatchBanner)){
	         try {
				String filePath =new StringBuffer().append(fileFolder).append(File.separator).append(oldMatchBanner).toString();
				 File newFile = new File(filePath);
				 if(newFile.exists())newFile.delete();
			} catch (Exception e) {
				log.info("原图删除失败,oldMatchPic={}",oldMatchPic);
			}
		}

		return JsonResult.buildSuccess();
	}

	@Override
	public MatchBasic findMatchById(Long id) {
		Assert.notNull(id,"id must not be null!");
		return matchBasicMapper.selectOne(new QueryWrapper<MatchBasic>().eq("id", id));
	}

	@Override
	public List<MatchBasic> queryMatchList() {
		QueryWrapper<MatchBasic> wrapper = new QueryWrapper<MatchBasic>();
		//M by ninglei ,只要不结束的比赛就可以查询筛选
//		wrapper.eq("type", 2).ge("end_date", LocalDate.now()).le("start_date", LocalDate.now()).orderByDesc("id");
		wrapper.eq("type", 2).ge("end_date", LocalDate.now()).orderByDesc("id");
		return matchBasicMapper.selectList(wrapper);
	}

	@Override
	public Map<String, Object> getMatchRule(Long id) {
		MatchBasic match = matchBasicMapper.selectById(id);
		if(StringUtils.isBlank(match.getRuleConfig()))return new HashMap<>();
		JSONObject  jsonObject = JSONObject.parseObject(match.getRuleConfig());
		Map<String,Object> map = (Map<String,Object>)jsonObject;
		Map<String,Object> rule=null;
		try {
			rule=(Map<String, Object>) ((Map<String, Object>) map.get("team")).get("rule");
		} catch (Exception e) {
		}
		return rule==null?new HashMap<String,Object>():rule;
	}

	@Override
	public void updateTeamRule(Long id, Map<String, Object> rule) {
		MatchBasic match = matchBasicMapper.selectById(id);
		Map<String, Object> rule_desc;
		if(StringUtils.isBlank(match.getRuleConfig())){
			rule_desc = new HashMap<String,Object>();
			Map<String, Object> team=new HashMap<String,Object>();
			team.put("rule", rule);
			rule_desc.put("team", team);
		}else{
			rule_desc= (Map<String,Object>)JSONObject.parseObject(match.getRuleConfig());
			Map<String, Object> team;
			if(rule_desc.containsKey("team") && rule_desc.get("team")!=null){
				team=(Map<String, Object>) rule_desc.get("team");
			}else{
				team=new HashMap<String,Object>();
			}
			team.put("rule", rule);
			rule_desc.put("team", team);
		}
		matchBasicMapper.updateTeamRule(id,JSON.toJSONString(rule_desc));
	}

	@Override
	public boolean isJoinMatch(Long matchId, Long userId) {
		return matchJoinMapper.selectCount(new QueryWrapper<MatchJoin>()
				.eq("user_id", userId).eq("match_id", matchId)) > 0;
	}

	@Override
	public JsonResult canJoinMatch(Long matchId, Long userId) {
		MatchBasic basic = matchBasicMapper.selectById(matchId);
		if(LocalDate.now().isBefore(basic.getApplyStartDate())){
			return JsonResult.buildFail("还未开始报名，请于 " +
                    (basic.getApplyStartDate().toEpochDay() - LocalDate.now().toEpochDay() + 1) +
                    " 天后报名比赛");
		}
		if(basic.getApplyEndDate().isBefore(LocalDate.now())){
			return JsonResult.buildFail("报名已结束，请关注其他比赛");
		}
		int joinNum = matchJoinMapper.selectCount(new QueryWrapper<MatchJoin>()
				.eq("user_id", userId).eq("match_id", matchId));
		if(joinNum > 0){
			return JsonResult.buildFail("您已参加该比赛");
		}
		return JsonResult.buildSuccess();
	}


}
