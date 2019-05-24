package cn.com.jrj.vtmatch.basicmatch.matchinfo.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.*;
import cn.com.jrj.vtmatch.basicmatch.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchBasic;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamMemberDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.SendRedDetail;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.ISendRedDetailService;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.BCrypt;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchIndexRedInfoVO;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.YcRedDetailVO;
import cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces.MatchRedJobService;
import cn.com.jrj.vtmatch.basicmatch.util.JSONUtil;
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
public class MatchRedJobServiceImpl implements MatchRedJobService {

    @Autowired
    private MatchBasicMapper matchBasicMapper;

	@Autowired
	private MatchUserMapper matchUserMapper;
    
    @Autowired
    private MatchUserDayStatRankMapper matchUserDayStatRankMapper;
    
    @Autowired
    private SendRedDetailMapper sendRedDetailMapper;
    
    @Autowired
    private ISendRedDetailService iSendRedDetailService;
    
    @Autowired
    private MatchTeamDayStatRankMapper matchTeamDayStatRankMapper;
    
    @Autowired
    private MatchTeamMemberDayStatRankMapper matchTeamMemberDayStatRankMapper;
    
    @Autowired
    private BasicMatchServiceImpl basicMatchServiceImpl;
    
    
    private String ycRedUrl;
    
   	private String vtMatchFlag;
   	
    @Value("${basic.match.red.ycRedUrl}")
   	public void setYcRedUrl(String ycRedUrl) {
		this.ycRedUrl = ycRedUrl;
	}

    @Value("${basic.match.red.vtMatchFlag:vtmatch}") 
	public void setVtMatchFlag(String vtMatchFlag) {
		this.vtMatchFlag = vtMatchFlag;
	}
    
    
    /* (non-Javadoc)
	 * @see cn.com.jrj.vtmatch.basicmatch.matchinfo.service.impl.MatchRedJobService#dealMatchRed(long)
	 */
    @Override
	public JSONObject dealMatchRed(long matchId){
    	//查询结束的比赛
    	List<MatchBasic> list = queryEndMatchs(matchId);
    	if(list == null || list.size() < 1){
    		log.info("not match is end");
    		return JSONUtil.fail("无可分红的比赛");
    	}
    	
    	for(MatchBasic match: list){
    		if(match.getMatchStatus() != 4){
    			return JSONUtil.fail("有未结束的比赛");
    		}
    		if(match.getType() == 1){
    			allotGeneralMathRed(match);
    		}else if(match.getType() == 2){
    			allotTeamMathRed(match);
    		}
    		sendRedToYc(match.getId());
    	}
    	return JSONUtil.retSuccess("success");
    }


	public List<MatchBasic> queryEndMatchs(long matchId){
    	QueryWrapper<MatchBasic> queryWrapper = new QueryWrapper<MatchBasic>();
    	if(matchId > 0){
    		queryWrapper.eq("id", matchId);
    	}else{
    		queryWrapper.eq("end_date", LocalDate.now().minusDays(1)).eq("match_status", 4);
    	}
    	return matchBasicMapper.selectList(queryWrapper);
    }
    
    public int deleteMatchRed(long matchId){
    	QueryWrapper<SendRedDetail> queryWrapper = new QueryWrapper<SendRedDetail>();
    	queryWrapper.eq("match_id", matchId);
    	return sendRedDetailMapper.delete(queryWrapper);
    }
    
    
    
    /**
     * 联赛红包分配
     * @param match
     */
    public void allotGeneralMathRed(MatchBasic match){
    	MatchIndexRedInfoVO redInfo = basicMatchServiceImpl.calRedInfo(match);
    	if(redInfo == null){
    		log.info("don't get redInfo!");
    	}
    	log.info("match rule == {}",match.getRuleConfig());
    	long totalAmount = redInfo.getTotalAmount() * 100;
    	
    	JSONObject ruleJson = JSONObject.parseObject(match.getRuleConfig());
    	
    	if(ruleJson == null || ruleJson.getJSONObject("reward") == null){
    		log.info("rule is null");
    		return ;
    	}
    	JSONObject reward = ruleJson.getJSONObject("reward");
    	RedRadio redRadio = calRedRadio(reward.getString("ratio"));
    	
    	if(redRadio == null || redRadio.getTotal() == 0){
    		log.info("rule ratio == {} is error!", reward.getString("ratio"));
    		return ;
    	}
    	
    	/**
    	 * 查询前三名
    	 * 
    	 */
    	List<MatchUserDayStatRank> topList = getMatchTop(match.getId(),redRadio.getSubRadio().size());
    	deleteMatchRed(match.getId());
    	
    	
    	long sendTotalCash =0;
    	
    	for(int i=0;i < topList.size(); i++){
    		long cash = totalAmount * redRadio.getSubRadio().get(i)/redRadio.getTotal();
    		sendTotalCash = sendTotalCash + cash;
    		MatchUserDayStatRank statRank = topList.get(i);
    		SendRedDetail sr = new SendRedDetail();
    		sr.setUserId(statRank.getUserId());
    		sr.setMatchId(statRank.getMatchId());
    		sr.setCash(cash);
    		sr.setRedStatus(1);
    		sr.setRedType("1");
    		sr.setRank(statRank.getTotalRank());
    		iSendRedDetailService.save(sr);
    	}
    	log.info("totalAmount = {}, sendTotalCash ={}",totalAmount,sendTotalCash);
    }
    
    public List<MatchUserDayStatRank> getMatchTop(long matchId,int num){
    	QueryWrapper<MatchUserDayStatRank> queryWrapper = new QueryWrapper<MatchUserDayStatRank>();
    	queryWrapper.eq("match_id", matchId).gt("total_rank", 0).orderByAsc("total_rank").last("limit " + num);
    	queryWrapper.select("match_id","user_id","account_id","total_yield","total_rank");
    	
    	return matchUserDayStatRankMapper.selectList(queryWrapper);
    }
    
    public List<MatchTeamDayStatRank> getTeamRank(long matchId){
    	QueryWrapper<MatchTeamDayStatRank> queryWrapper = new QueryWrapper<MatchTeamDayStatRank>();
    	queryWrapper.eq("match_id", matchId).gt("total_rank", 0).orderByAsc("total_rank");
    	queryWrapper.select("match_id","team_id","member_num","total_yield","total_rank");
//    	queryWrapper.last("limit 3");
    	return matchTeamDayStatRankMapper.selectList(queryWrapper);
    }
    
    
    
    public RedRadio calRedRadio(String radio){
    	if(StringUtils.isBlank(radio)){
    		return null;
    	}
    	RedRadio rr = new RedRadio();
    	rr.setSubRadio(new ArrayList<Integer>());
    	try {
        	for(String a1: StringUtils.split(radio, ":")){
        		Integer r1 = Integer.parseInt(a1);
        		rr.setTotal(rr.getTotal()+r1);
        		rr.getSubRadio().add(r1);
        	}
        	return rr;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    }
    
    
    
    
    
    public void allotTeamMathRed(MatchBasic match){
//    	MatchIndexRedInfoVO redInfo = basicMatchServiceImpl.calRedInfo(match);
//    	if(redInfo == null){
//    		log.info("don't get redInfo!");
//    	}
//    	log.info("match rule == {}",match.getRuleConfig());
//    	long totalAmount = redInfo.getTotalAmount() * 100;
    	
    	JSONObject ruleJson = JSONObject.parseObject(match.getRuleConfig());
    	
    	allotTeamPersonal(match, ruleJson);
    	allotTeamMember(match, ruleJson);
    }

	private void allotTeamMember(MatchBasic match, JSONObject ruleJson) {
		if(ruleJson == null || ruleJson.getJSONObject("team") == null){
    		return ;
    	}
    	JSONObject teamRule = ruleJson.getJSONObject("team") ;
    	
    	//优胜部分奖金
    	JSONObject winTeamReward = teamRule.getJSONObject("win_team_reward");
    	//优胜战队比率
    	RedRadio you_radio = calRedRadio(winTeamReward.getString("ratio"));
    	//优胜战队固定奖金
    	long you_totalAmount = winTeamReward.getLongValue("total")*100;
    	
    	//计算单个团队红包,基础参数
    	JSONObject constituteReward = teamRule.getJSONObject("constitute_reward");
    	//团队上限
    	long maxTeamAmount = 0;
    	//团队下限
    	long minTeamAmount = 0;
    	long preAmount = 0;
    	if(constituteReward != null){
    		maxTeamAmount = constituteReward.getLongValue("max_amount")*100;
    		minTeamAmount = constituteReward.getLongValue("min_amount")*100;
    		JSONObject subRule = constituteReward.getJSONObject("rule");
    		preAmount = subRule.getLongValue("step_amount")*100/subRule.getLongValue("step_num");
    	}
    	
    	JSONObject rule = teamRule.getJSONObject("rule");
    	//成团数
    	int minTeamMemberNum=rule.getIntValue("constitute_num");
    	
    	//战队成员奖金分配比率
    	RedRadio teamMemberRadio = calRedRadio(teamRule.getJSONObject("win_team_member_reward").getString("ratio"));
    	
		
		List<MatchTeamDayStatRank> teamRankList = getTeamRank(match.getId());
		for(int i=0; i<teamRankList.size(); i++){
			MatchTeamDayStatRank teamStat = teamRankList.get(i);
			if(teamStat.getMemberNum() < minTeamMemberNum){
				continue;
			}
			
			long totalAmount = minTeamAmount + (teamStat.getMemberNum()-minTeamMemberNum) * preAmount;
			if(totalAmount > maxTeamAmount) 
				totalAmount=maxTeamAmount;
			if(i < you_radio.getSubRadio().size()){
				totalAmount = totalAmount + you_totalAmount*you_radio.getSubRadio().get(i)/you_radio.getTotal();
			}
			QueryWrapper<MatchTeamMemberDayStatRank> queryWrapper = new QueryWrapper<MatchTeamMemberDayStatRank>();
			queryWrapper.eq("match_id", teamStat.getMatchId()).eq("team_id", teamStat.getTeamId()).orderByAsc("total_rank").last("limit 20");
			queryWrapper.select("match_id","team_id","user_id","account_id","total_yield","total_rank");
			
			List<MatchTeamMemberDayStatRank> teamMemberRankList = matchTeamMemberDayStatRankMapper.selectList(queryWrapper);
			int otherSize = teamMemberRankList.size() - teamMemberRadio.getSubRadio().size() + 1;
			for(int k=0;k < teamMemberRankList.size(); k++){
				long cash =0;
				if(k < (teamMemberRadio.getSubRadio().size()-1)){
					cash = totalAmount * teamMemberRadio.getSubRadio().get(k)/teamMemberRadio.getTotal();
				}else{
					cash = totalAmount * teamMemberRadio.getSubRadio().get(teamMemberRadio.subRadio.size()-1)/(teamMemberRadio.getTotal()*otherSize);
				}
				MatchTeamMemberDayStatRank statRank = teamMemberRankList.get(k);
	    		SendRedDetail sr = new SendRedDetail();
	    		sr.setUserId(statRank.getUserId());
	    		sr.setMatchId(statRank.getMatchId());
	    		sr.setCash(cash);
	    		sr.setRedStatus(1);
	    		sr.setRedType("1");
	    		sr.setRank(statRank.getTotalRank());
	    		sr.setTeamId(statRank.getTeamId());
	    		iSendRedDetailService.save(sr);
	    	}
			
		}
	}
	
	
	
	private void allotTeamPersonal(MatchBasic match, JSONObject ruleJson) {
		if(ruleJson == null || ruleJson.getJSONObject("personal") == null || ruleJson.getJSONObject("personal").getJSONObject("reward") == null){
    		log.info("rule is null " );
    		return ;
    	}
    	JSONObject reward = ruleJson.getJSONObject("personal").getJSONObject("reward");
    	long totalAmount = reward.getLongValue("total")*100;
    	RedRadio redRadio = calRedRadio(reward.getString("ratio")+"");
    	
    	if(redRadio == null || redRadio.getTotal() == 0){
    		log.info("rule ratio == {} is error!", reward.getString("ratio"));
    		return ;
    	}
    	
    	List<MatchUserDayStatRank> topList = getMatchTop(match.getId(),redRadio.getSubRadio().size());
    	deleteMatchRed(match.getId());
    	
    	
    	long sendTotalCash =0;
    	
    	for(int i=0;i < topList.size(); i++){
    		long cash = totalAmount * redRadio.getSubRadio().get(i)/redRadio.getTotal();
    		sendTotalCash = sendTotalCash + cash;
    		MatchUserDayStatRank statRank = topList.get(i);
    		SendRedDetail sr = new SendRedDetail();
    		sr.setUserId(statRank.getUserId());
    		sr.setTeamId(0L);
    		sr.setMatchId(statRank.getMatchId());
    		sr.setRank(topList.get(i).getTotalRank());
    		sr.setCash(cash);
    		sr.setRedStatus(1);
    		sr.setRedType("1");
			sr.setOpenId(matchUserMapper.selectById(statRank.getUserId()).getUnionId());
    		
    		iSendRedDetailService.save(sr);
    	}
    	log.info("totalAmount = {}, sendTotalCash ={}",totalAmount,sendTotalCash);
	}
	
	
	public void sendRedToYc(long matchId){
		log.info("======sendRedToYc =======");
		MatchBasic matchBasic = matchBasicMapper.selectById(matchId);
		List<YcRedDetailVO> list = matchBasicMapper.selectMatchRedList(matchId);
		if(list == null || list.size() < 1){
			return;
		}
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String orderDate = LocalDate.now().format(df);
		for(YcRedDetailVO vo :list){
//			log.inf
			JSONObject res = sendToYc(vo,orderDate,matchBasic);
			if(!JSONUtil.isFail(res)){
				log.info("-----------");
				sendRedDetailMapper.updateRedStatus(vo.getId(), vo.getOpenId(), vo.getPhoneNo());
			}
		}
		log.info("======sendRedToYc end=======");
	}
	
	public JSONObject sendToYc(YcRedDetailVO vo,String orderDate,MatchBasic matchBasic){
		try {
			log.info("send http red to yc = "+JSONObject.toJSONString(vo));
			Map<String,String> params = new HashMap<>();

			String merchantId = "fcsc-rp-cgbs";
			long tokentime = System.currentTimeMillis();
			//token生成时间
			params.put("tokentime", Long.toString(tokentime));
			//验证token
			params.put("token", BCrypt.hashpw(tokentime+merchantId, BCrypt.gensalt()));
			//商户代码
			params.put("merchant_id", merchantId);
			//业务类型
			params.put("business_type", "01");
			//支付金额
			params.put("amount", "" + Integer.parseInt(vo.getCash()));
			//交易类型
			params.put("tran_type", "01");
			//商户订单号(id+"_"+match_id+"_"+user_id+"_"+team_id)
			params.put("mer_order_id",vo.getId()+"_"+vo.getMatchId()+"_"+vo.getUserId()+"_"+vo.getTeamId());
			//商户订单日期
			params.put("mer_order_date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
			//已申请退款金额
			params.put("refund_amount", "0");


			Map<String,Object> infoMap = new HashMap<>(7);
			//比赛名称
			infoMap.put("game_name",vo.getMatchName());
			//用户ID
			infoMap.put("userid",StringUtils.isBlank(vo.getOpenId())? vo.getUnionId() : vo.getOpenId());
			//用户ID类型    1:openid 2:unionid
			infoMap.put("usertype",StringUtils.isBlank(vo.getOpenId()) ? 2 : 1);
			//微信公众号
			infoMap.put("weixinpk","gh_d726bbfffcf2");
			//比赛开始时间
			infoMap.put("start_time",matchBasic.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			//比赛结束时间
			infoMap.put("end_time",matchBasic.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			//比赛排名
			infoMap.put("rank",vo.getRank());

			//通知信息
			params.put("notice_info", JSONObject.toJSONString(infoMap));

			log.info("send http red to yc  params = "+ JSONObject.toJSONString(params));
			JSONObject res = HttpUtil.get(ycRedUrl, null, params);
			log.info("send http red to yc  return = "+ res);
			return res;
		} catch (Exception e) {
			log.error("sendRedToYc Exception:",e);
			return null;
		}
	}


	@Override
	public void sendTestRed(long matchId) {
		sendRedToYc(matchId);
	}
	
}
