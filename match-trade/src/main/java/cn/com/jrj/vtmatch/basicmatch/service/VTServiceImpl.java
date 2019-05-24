package cn.com.jrj.vtmatch.basicmatch.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.jrj.vtmatch.basicmatch.helper.IncomeRatePicHelper;
import cn.com.jrj.vtmatch.basicmatch.helper.StringRedisHelper;
import cn.com.jrj.vtmatch.basicmatch.helper.VTHelper;
import cn.com.jrj.vtmatch.basicmatch.helper.VTRedisHelper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccount;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.Trading;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.impl.TradingServiceImpl;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.JsonResult;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.AccountFundInfo;
import cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces.VTService;
import cn.com.jrj.vtmatch.basicmatch.model.FundInfo;
import cn.com.jrj.vtmatch.basicmatch.model.FundStatement;
import cn.com.jrj.vtmatch.basicmatch.model.StockFee;
import cn.com.jrj.vtmatch.basicmatch.util.ArrayUtils;
import cn.com.jrj.vtmatch.basicmatch.util.DateToolkit;
import cn.com.jrj.vtmatch.basicmatch.util.HQUtil;
import cn.com.jrj.vtmatch.basicmatch.util.JSONUtil;
import cn.com.jrj.vtmatch.basicmatch.util.MvtException;
import cn.com.jrj.vtmatch.basicmatch.util.StockSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * 
 * 金牛接口相关服务
 * 
 * +----------------------------------------------------+
 * |	@author weibo.qin					            |
 * |	@version 创建时间：2016年5月31日 下午6:05:29	    |
 * +----------------------------------------------------+
 *
 */
@Slf4j
@Service
public class VTServiceImpl implements VTService {
	private static final int COMMISSION_BUY = 0;
	private static final int COMMISSION_SELL = 1;
	public static final String ACCOUNT_ID = "accountId";
	public static final String STOCK_CODE = "stock_code";
	public static final String CANBUY = "canbuy";
	public static final String RET_CODE = "retCode";
	public static final String ITEMS = "items";
	public static final String EMBED = "embed";
	public static final String EXPIRY_TIME = "expiry_time";
	public static final String CHARGE_TYPE = "charge_type";
	public static final String DIV_CLS_TYPE = "div_cls_type";
	public static final String MONEY_TYPE = "money_type";
	public static final String MATCH_ID = "matchID";
	public static final String COST_PRICE = "cost_price";
	public static final String CURRENT_PRICE = "current_price";
	public static final String CURRENT_AMOUNT = "current_amount";
	public static final String FROZEN_AMOUNT = "frozen_amount";

	@Resource
	private VTHelper vtHelper;
	@Resource
	private StringRedisHelper stringRedisHelper;
	@Resource 
	private VTRedisHelper vtRedisHelper;
	@Resource 
	private IncomeRatePicHelper incomeRatePicHelper;
	@Resource
	private MatchUserAccountMapper matchUserAccountMapper;
	@Resource 
	private TradingServiceImpl tradingServiceImpl;
	
	private String vtHelperMid ;
   	private String vtHelperFromSite ;
   	private BigDecimal positionRate;
   	
    @Value("${basic.match.vt.mid:30}")
	private void setvtHelperMid(String mid){
		vtHelperMid = mid;
	}
	
	@Value("${basic.match.vt.fromSize:yc}") 
	private void setvtHelperFromSite(String fromSize){
		vtHelperFromSite = fromSize;
	}
	
	@Value("${basic.match.vt.positionRate:30}") 
	private void setPositionRate(int positionRate){
		if(positionRate>=1 && positionRate <=100)
			this.positionRate = new BigDecimal(positionRate).divide(new BigDecimal(100));
		else
			this.positionRate = new BigDecimal(0.3);
	}
	

	public JSONObject createVTAccount(String uid){
		return vtHelper.create(uid);
	}

	public JSONObject joinMatch(String accountId){
		return vtHelper.joinMatch(accountId);
	}

	public JsonResult register(String uid){
		return vtHelper.register(uid);
	}
	
	public JSONObject reset(String uid){
		return vtHelper.reset(uid);
	}
	
	
	/* (non-Javadoc)
	 * @see cn.com.jrj.vtmatch.basicmatch.service.VTServiceInteface#commission(java.lang.String, java.lang.Integer, java.math.BigDecimal, java.math.BigDecimal, java.lang.Integer, java.lang.String)
	 */
	@Override
	public JSONObject commission(String uuid, BigDecimal entrustPrice, BigDecimal entrustAmount,Integer BSType, String stockCode,int stockType) {

		// 缺参数验证
		JSONObject vobj = verify(uuid,entrustAmount,entrustPrice,stockCode,BSType);
		if(JSONUtil.isFail(vobj)){
			return vobj;
		}

		// 执行委托操作
		JSONObject req = new JSONObject();
		req.put(ACCOUNT_ID, vtHelper.getVT_ACCOUNTID());
		req.put("commission_amount", entrustAmount);
		if(stockType == 19){
			req.put("commission_price", entrustPrice.setScale(3, RoundingMode.HALF_UP));
		}else{
			req.put("commission_price", entrustPrice.setScale(2, RoundingMode.HALF_UP));
		}
		req.put("commission_type", BSType+ ""); // 
		req.put(STOCK_CODE, stockCode);
		req.put("stock_type", stockType+""); // 股票
		req.put("uuid", uuid);

		log.info("push commission param = {}" ,req.toJSONString());
		JSONObject commissionResult= vtHelper.commissions(uuid, req.toJSONString());

		//
		if(JSONUtil.isFail(commissionResult)){
			if(commissionResult.getIntValue("retCode") == MvtException.NO_FUND_ERROR.getErrCode()){
				return JSONUtil.fail(MvtException.NO_FUND_ERROR.getErrMsg());
			}
			return JSONUtil.fail(MvtException.UNIDENTIFIED_ERROR.getErrMsg());
		}
		vtRedisHelper.deleteUserVt(uuid);
		return commissionResult;
	}

	private JSONObject verify(String uuid,BigDecimal camount, BigDecimal cprice,String scode,int tradeType){
		// 判定当前时间段是否可以委托
		if (!isCommissionTime()) {
			return JSONUtil.fail("系统正在清算数据，请在17:30后进行委托！");
		}		
		BigDecimal lotSize =new BigDecimal(100);
		JSONObject entrustLimit = queryEntrustLimit(uuid, scode, tradeType, cprice);
		BigDecimal canNum =new BigDecimal(0);
		if(JSONUtil.isFail(entrustLimit)){
			return JSONUtil.fail("委托数量不符合规则，请重新下单"); 
		}
		canNum= entrustLimit.getBigDecimal("data");
		
		if(tradeType==COMMISSION_BUY){
			queryEntrustLimit(uuid, scode, COMMISSION_BUY, cprice);
			if (camount.remainder(lotSize).doubleValue()!=0) {
				return JSONUtil.fail(MvtException.RESTRAINT_ENTRUST_AMOUNT_ERROR.getErrMsg());
			}
			if(canNum.compareTo(camount)<0){
				return JSONUtil.fail(MvtException.RESTRAINT_ENTRUST_BUY_MAX_AMOUNT_ERROR.getErrMsg());
			}
		}
		//委托卖出时,如果有零股,需一次性卖出所有持仓,否则卖出100的整数倍
		if(tradeType==COMMISSION_SELL){
			if (camount.remainder(lotSize).doubleValue()!=0) {
				if(camount.compareTo(canNum)!=0){
					log.info("uuid={}, 卖出时有零股,并且没有一次卖出.委托数量={},最大可买={}",uuid,String.valueOf(camount),String.valueOf(canNum));
					return JSONUtil.fail(MvtException.RESTRAINT_ENTRUST_SELL_AMOUNT_ERROR.getErrMsg());
				}
			}
		}

		return JSONUtil.success();
	}
	

	@Override
	public JSONObject orderCancel(String uuid, long commissionId) {
//		JSONObject req = new JSONObject();
//		req.put("accountId", VTHelper.VT_ACCOUNTID);
//		req.put("commission_type",4+""); // 
//		req.put("commissionId", commissionId); 
//		req.put("uuid", uuid);
		String param = "{\"accountId\":\""+vtHelper.getVT_ACCOUNTID()+"\",\"commissionId\":"+commissionId+",\"commission_type\":\"4\",\"uuid\":\""+uuid+"\"}";
		JSONObject res = vtHelper.cancelCommission(uuid, commissionId, param);
		if(JSONUtil.isFail(res)){
			return JSONUtil.fail(MvtException.ORDER_CANCEL_ERROR.getErrMsg());
		}
		vtRedisHelper.deleteUserVt(uuid);
		return res;
	}


	/* (non-Javadoc)
	 * @see cn.com.jrj.vtmatch.basicmatch.service.VTServiceInteface#queryEntrustLimit(java.lang.String, java.lang.String, int, java.math.BigDecimal)
	 */
	@Override
	public JSONObject queryEntrustLimit(String uuid,String stockCode, int tradeType, BigDecimal entrustPrice) {
		/**
		 * 	金牛返回数据格式：
		 *  {"stock_code":"600000","stock_name":"浦发银行","stock_type":0,"status_type":1,"lotsize":100,"delayprice":7.58,"price":7.58,"last_close":7.61,
		 * 	"available_balance":61225.01,"canbuy":8200,"ladder":"0.01"}
		 * 
		 *  {"stock_code":"300147","stock_name":"香雪制药","stock_type":0,"status_type":1,"lotsize":100,"delayprice":9.59,"price":9.59,"last_close":9.28,
		 *  "cansell":2300,"isbroken":false,"ladder":"0.01"}
		 * 
		 * 
		 */
//		JSONObject json=new JSONObject();
		String tag= CANBUY;
		if (tradeType==COMMISSION_SELL) {
			tag="cansell";
		}
		
		BigDecimal maxAmount ;
		
		if(entrustPrice.compareTo(new BigDecimal(0.00))>0 && CANBUY.equals(tag)){
				//获取手续费费用倍率(预设费率)
				BigDecimal times=new BigDecimal("0.001").add(new BigDecimal(1));
				
				//总资金
				FundStatement statement = getFundStatement(uuid);
				if(statement == null || statement.getLastBalance() == null){
					return JSONUtil.retSuccess(0);
				}
				BigDecimal availableBalance=statement.getAvailableBalance();
				
				
				
				//持仓数量 + 撤单数量
				int hasNum = getStockPositionNum(uuid,stockCode) +getStockCancelBuyNum(uuid, stockCode);
				//可买资金
				// 1.判断当前持仓中的该股票数量是否已超单三最大额
				// 2.如果已超过单三最大额 返回0
				// 3.如果未超过单三最大额,min(可用资金,总资产*0.3-该股票当前价*当前数量-委托未成交数量*委托价)/(委托价*倍率)
				log.info(" queryEntrustLimit fundInfo = {} ,positionRate = {},hasNum={}",
						JSONObject.toJSON(statement),positionRate,hasNum);
				
				BigDecimal buyMaxBalance=statement.getLastBalance().multiply(positionRate).subtract(entrustPrice.multiply(new BigDecimal(hasNum)));
				
				
				if(buyMaxBalance.floatValue() < 0){
					return JSONUtil.retSuccess(0);
				}
				
				if(buyMaxBalance.floatValue() > availableBalance.floatValue()){
					buyMaxBalance = availableBalance;
				}
				buyMaxBalance = buyMaxBalance.subtract(calFee(buyMaxBalance.doubleValue(),stockCode));
				maxAmount = normalComputeEntrustBuyLimit(entrustPrice, times, buyMaxBalance);
				return JSONUtil.retSuccess(maxAmount.intValue());
		}else{
			return JSONUtil.retSuccess(getStockCanSellNum(uuid,stockCode));
		}
	}
	
	
	public BigDecimal calFee(double balance,String scode){
		StockFee stockFee = vtHelper.getStockFee();
		double cfee = balance*stockFee.getCfee();
		cfee = cfee < stockFee.getCfeeMin() ? stockFee.getCfeeMin():cfee;
		double tfee =0;
		if(scode.startsWith("6")){
			tfee = balance*stockFee.getTfee();
			tfee = tfee < stockFee.getTfeeMin() ? stockFee.getTfeeMin():tfee;
		}
		return new BigDecimal(cfee+tfee); 
	}
	
	/**
	 * 获取某只股票持仓数量
	 * @param uuid
	 * @param stockCode
	 * @return
	 */
	public int getStockPositionNum(String uuid,String stockCode){
		JSONObject position = getVTPosition(uuid);
		if(position == null){
			return 0;
		}
		JSONArray list = position.getJSONArray(ITEMS);
		if(list == null || list.size()< 1){
			return 0;
		}
		for(int i=0;i<list.size();i++){
			if(stockCode.equals(list.getJSONObject(i).getString(STOCK_CODE))){
				return list.getJSONObject(i).getIntValue(CURRENT_AMOUNT);
			}
		}
		return 0;
	}
	
	/**
	 * 从持仓中获取可卖数量
	 * @param uuid
	 * @param stockCode
	 * @return
	 */
	public int getStockCanSellNum(String uuid,String stockCode){
		JSONObject position = getVTPosition(uuid);
		if(position == null){
			return 0;
		}
		JSONArray list = position.getJSONArray(ITEMS);
		if(list == null || list.size()< 1){
			return 0;
		}
		for(int i=0;i<list.size();i++){
			JSONObject stockObj = list.getJSONObject(i);
			if(stockCode.equals(stockObj.getString(STOCK_CODE))){
				return stockObj.getIntValue(CURRENT_AMOUNT)-stockObj.getIntValue(FROZEN_AMOUNT);
			}
		}
		return 0;
	}
	
	/**
	 * 获取某只股票撤单列表买入数量
	 * @param uuid
	 * @param stockCode
	 * @return
	 */
	public int getStockCancelBuyNum(String uuid,String stockCode){
		JSONObject cancelList = queryCancel(uuid);
		if(cancelList == null){
			return 0;
		}
		JSONArray list = cancelList.getJSONArray(ITEMS);
		if(list == null || list.size()< 1){
			return 0;
		}
		int num = 0;
		for(int i=0;i<list.size();i++){
			JSONObject obj = list.getJSONObject(i);
			if(stockCode.equals(obj.getString(STOCK_CODE)) && (obj.getIntValue("commission_type")==1)){
				num = num + obj.getIntValue("conclude_amount");
			}
		}
		return num;
	}

	private BigDecimal normalComputeEntrustBuyLimit(BigDecimal entrustPrice, BigDecimal times, BigDecimal availableBalance) {
		
		
		// 取整[可用资金/(委托价格*100*手续费倍率)]*100
		int maxAmount=availableBalance.divide(entrustPrice.multiply(times),2, BigDecimal.ROUND_HALF_DOWN).divide(new BigDecimal(100)).intValue()*100;
		return new BigDecimal(maxAmount);
	}
	
	
	/* (non-Javadoc)
	 * @see cn.com.jrj.vtmatch.basicmatch.service.VTServiceInteface#queryConcludeList(java.lang.String, int, int)
	 */
	@Override
	public JSONObject queryConcludeList(String uuid, int pn ,int ps){
		String key = VTRedisHelper.getConcludeKey(uuid, pn, ps);
		if(!StringUtils.isBlank(key)){
			String value = stringRedisHelper.get(key);
			if(!StringUtils.isBlank(value)){
				return JSONObject.parseObject(value);
			}
		}
		JSONObject res = vtHelper.getConcludes(uuid, null, null, pn, ps);
		
		if(JSONUtil.isFail(res) ){
			return JSONUtil.fail(MvtException.QUERY_CONCLUDELIST_ERROR.getErrMsg());
		}
		// 调用核心可撤单的委托接口
		res.put(RET_CODE, 0);
		JSONArray list=res.getJSONArray(ITEMS);
		list.forEach(obj -> {
			JSONObject item = (JSONObject)obj;
			item.remove("note");
			item.remove(EMBED);
			item.remove(EXPIRY_TIME);
			item.remove(CHARGE_TYPE);
			item.remove(DIV_CLS_TYPE);
			item.remove("offsetProfit");
			item.remove(MONEY_TYPE);
			item.remove(MATCH_ID);
			item.remove(ACCOUNT_ID);
		});
		if(!StringUtils.isBlank(key)){
			stringRedisHelper.set(key, res.toJSONString(), VTRedisHelper.LONG_TIME_OUT);
		}
		return res;
		
	}
	/* (non-Javadoc)
	 * @see cn.com.jrj.vtmatch.basicmatch.service.VTServiceInteface#queryCommissionList(java.lang.String, int, int)
	 */
	@Override
	public JSONObject queryCommissionList(String uuid, int pn ,int ps){
		String key = VTRedisHelper.getCommissionKey(uuid, pn, ps);
		if(!StringUtils.isBlank(key)){
			String value = stringRedisHelper.get(key);
			if(!StringUtils.isBlank(value)){
				return JSONObject.parseObject(value);
			}
		}
		JSONObject res = vtHelper.getCommissionsList(uuid, null, null, pn, ps);
		
		if(JSONUtil.isFail(res) ){
			return JSONUtil.fail(MvtException.QUERY_CONCLUDELIST_ERROR.getErrMsg());
		}
		// 调用核心可撤单的委托接口
		res.put(RET_CODE, 0);
		JSONArray list=res.getJSONArray(ITEMS);
		list.forEach(obj -> {
			JSONObject item = (JSONObject)obj;
			item.remove("note");
			item.remove(EMBED);
			item.remove(EXPIRY_TIME);
			item.remove(CHARGE_TYPE);
			item.remove(DIV_CLS_TYPE);
			item.remove("offsetProfit");
			item.remove(MONEY_TYPE);
			item.remove(MATCH_ID);
			item.remove(ACCOUNT_ID);
		});
		if(!StringUtils.isBlank(key)){
			stringRedisHelper.set(key, res.toJSONString(), VTRedisHelper.LONG_TIME_OUT);
		}
		return res;
		
	}
	
	/* (non-Javadoc)
	 * @see cn.com.jrj.vtmatch.basicmatch.service.VTServiceInteface#queryCancel(java.lang.String)
	 */
	@Override
	public JSONObject queryCancel(String uuid) {
		String key = VTRedisHelper.getCancelKey(uuid);
		String value = stringRedisHelper.get(key);
		if(!StringUtils.isBlank(value)){
			return JSONObject.parseObject(value);
		}
		JSONObject res = vtHelper.cancelList(uuid, "commission_time-desc");
		
		if(JSONUtil.isFail(res) ){
			return JSONUtil.fail(MvtException.CANCEL_ERROR.getErrMsg());
		}
		// 调用核心可撤单的委托接口
		res.put(RET_CODE, 0);
		JSONArray list=res.getJSONArray(ITEMS);
		list.forEach(obj -> {
			JSONObject item = (JSONObject)obj;
			item.remove("note");
			item.remove(EMBED);
			item.remove(EXPIRY_TIME);
			item.remove(MATCH_ID);
			item.remove(MONEY_TYPE);
			item.remove(CHARGE_TYPE);
			item.remove("commission_price_low");
			item.remove("commission_price_high");
			item.remove("to_stock_code");
			item.remove("to_div_cls_type");
			item.remove("to_stock_name");
			item.remove(DIV_CLS_TYPE);
			item.remove(ACCOUNT_ID);
		});
		stringRedisHelper.set(key, res.toJSONString(),VTRedisHelper.LONG_TIME_OUT);
		return res;
	}
	
	/* (non-Javadoc)
	 * @see cn.com.jrj.vtmatch.basicmatch.service.VTServiceInteface#queryPositionList(java.lang.String)
	 */
//	@Override
//	public JSONObject queryPositionList(String uuid) {
//		String key = VTRedisHelper.getNpPositionKey(uuid);
//		String value = stringRedisHelper.get(key);
//		if(!StringUtils.isBlank(value)){
//			return JSONObject.parseObject(value);
//		}
//		
//		JSONObject res = getVTPosition(uuid);
//		
//		if(JSONUtil.isFail(res)){
//			JSONObject obj =  JSONUtil.fail(MvtException.CANCEL_ERROR.getErrMsg());
//			obj.put(ITEMS, CollectionUtils.EMPTY_COLLECTION);
//			return obj;
//		}
//		
//		JSONArray list=res.getJSONArray(ITEMS);
//		
//		List<String> scodes = ArrayUtils.getStockCodes(list, STOCK_CODE);
//		
//		Map<String, StockSummary> hqs = HQUtil.queryHQ(scodes);
//		
//		
//		list.forEach(obj -> {
//			JSONObject item = (JSONObject)obj;
//			String scode = item.getString(STOCK_CODE);
//			StockSummary st = hqs.get(scode);
//			if(st != null){
//				BigDecimal costPrice = item.getBigDecimal(COST_PRICE);
//				float yield = st.getNp().subtract(costPrice).divide(costPrice, 4, BigDecimal.ROUND_HALF_DOWN).floatValue();
//				item.put(CURRENT_PRICE, st.getNp());
//				item.put("total_income", st.getNp().subtract(costPrice).multiply(item.getBigDecimal(CURRENT_AMOUNT)));
//				item.put("total_yield", yield*100);
//			}else{
//				log.info("stock hq st is null: {}",JSONObject.toJSONString(hqs));
//				item.put(CURRENT_PRICE,item.getBigDecimal(COST_PRICE));
//				item.put("total_income", 0);
//				item.put("total_yield", 0);
//			}
//		});
//		// 调用核心可撤单的委托接口
//		res.put(RET_CODE, 0);
//		
//		long timeOut = vtRedisHelper.isTradeTime()? VTRedisHelper.SHORT_TIME_OUT:VTRedisHelper.LONG_TIME_OUT;
//		stringRedisHelper.set(key, res.toJSONString(), timeOut);
//		return res;
//	}
	
	public JSONObject getVTPosition(String uuid) {
		String key = VTRedisHelper.getPositionKey(uuid);
		String value = stringRedisHelper.get(key);
		if(!StringUtils.isBlank(value)){
			return JSONObject.parseObject(value);
		}
		
		JSONObject res = vtHelper.positionList(uuid);
		if(JSONUtil.isFail(res)){
			JSONObject obj =  JSONUtil.fail(MvtException.CANCEL_ERROR.getErrMsg());
			obj.put(ITEMS, CollectionUtils.EMPTY_COLLECTION);
			return obj;
		}
		
		JSONArray list=res.getJSONArray(ITEMS);
		list.forEach(obj -> {
			JSONObject item = (JSONObject)obj;
			item.remove(MONEY_TYPE);
			item.remove(CHARGE_TYPE);
			item.remove(DIV_CLS_TYPE);
			item.remove(ACCOUNT_ID);
			item.remove("direction");
			item.remove(MATCH_ID);
		});
		// 调用核心可撤单的委托接口
		res.put(RET_CODE, 0);
		stringRedisHelper.set(key, res.toJSONString(), VTRedisHelper.LONG_TIME_OUT);
		return res;
	}
	
//	@Override
//	public JSONObject queryUserFunds(String uuid) {
//		FundInfo fundInfo = getFundInfo(uuid);
//		if (fundInfo == null) {
//			log.error("查询用户信息接口失败!");
//			return JSONUtil.fail(MvtException.QUERY_USERFUNDS_ERROR.getErrMsg());
//		}
//		return JSONUtil.retSuccess(fundInfo);
//	}
	
	
//	public FundInfo getFundInfo(String uuid) {
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start("queryPositionList");
//		JSONObject positionObj = queryPositionList(uuid);
//		stopWatch.stop();
//		stopWatch.start("getFundInfoKey");
//		JSONArray positionList = positionObj.getJSONArray(ITEMS);
//		String key = VTRedisHelper.getFundInfoKey(uuid);
//		stopWatch.stop();
//		stopWatch.start("stringRedisHelper.get(key)");
//		String value = stringRedisHelper.get(key);
//		stopWatch.stop();
//		if(!StringUtils.isBlank(value)){
//			log.info("getFundInfo time :{}",stopWatch.prettyPrint());
//			return JSONObject.parseObject(value,FundInfo.class);
//		}
//		stopWatch.start("getFundStatement");
//		FundStatement statement = getFundStatement(uuid);
//		stopWatch.stop();
//		if(statement == null){
//			log.info("query fund statement is null ");
//			log.info("getFundInfo time :{}",stopWatch.prettyPrint());
//			return null;
//		}
//		stopWatch.start("calculate");
//		// 总市值，调用接口计算累加E(持仓股数*当前价)
//		BigDecimal marketValue = new BigDecimal(0);
//		
//		
//		BigDecimal oneMarketValue = new BigDecimal(0);
//		BigDecimal totalBalance = statement.getLastBalance();
//		BigDecimal todayIncome = new BigDecimal(0);
//		BigDecimal totalIncome = new BigDecimal(0);
//		BigDecimal totalYieldrate = statement.getLastTodayYieldrate();
//		BigDecimal todayYieldrate = new BigDecimal(0);
//		
//		
//		
////		boolean hasCurrentPrice = true;
//		if(positionList !=null && positionList.size() > 0){
//			for(int i=0;i < positionList.size();i++){
//				JSONObject p = positionList.getJSONObject(i);
//				if (p.getBigDecimal(CURRENT_PRICE) != null) {
//					oneMarketValue = p.getBigDecimal(CURRENT_PRICE).multiply(
//							p.getBigDecimal(CURRENT_AMOUNT));
//					marketValue = marketValue.add(oneMarketValue);
//				} else {
//					oneMarketValue = p.getBigDecimal(COST_PRICE).multiply(
//							p.getBigDecimal(CURRENT_AMOUNT));
//					marketValue = marketValue.add(oneMarketValue);
//					log.info("获取股票当前价格失败:{}",p.toJSONString());
//				}
//			}
//			// 今日总资产(总资产)=市值+可用资金+冻结资金
//			totalBalance = statement.getCurrentBalance().add(marketValue);
//			
//			// 今日盈亏额 =今日总资产-昨日总资产
//			todayIncome = totalBalance.subtract(statement.getLastBalance());
//			// 总盈亏额 =总资产-初始资金
//			totalIncome = totalBalance.subtract(statement.getBeginBalance());
//			
//			// 总盈亏比例=总盈亏额/初始资金
//			totalYieldrate = totalIncome.multiply(new BigDecimal(100)).divide(statement.getBeginBalance(), 2,
//					BigDecimal.ROUND_HALF_UP);
//			// 今日盈比例=今日盈亏额/昨日总资产
//			Calendar cal = Calendar.getInstance();
//			if(cal.get(Calendar.HOUR_OF_DAY) > 15  && todayIncome.intValue()==0){
//				todayYieldrate=statement.getLastTodayYieldrate();
//				todayIncome = statement.getLastTodayIncome();
//			}else{
//				todayYieldrate = todayIncome.multiply(new BigDecimal(100)).divide(statement.getLastBalance(), 2,
//						BigDecimal.ROUND_HALF_UP);
//			}
//				
//		}
//		
//		FundInfo info = new FundInfo();
//		info.setAvailableBalance(statement.getAvailableBalance());
//		info.setTotalBalance(totalBalance);
//		info.setMarketValue(marketValue);
//		info.setTotalYieldrate(totalYieldrate);
//		info.setTodayYieldrate(todayYieldrate);
//		info.setTodayIncome(todayIncome);
//		info.setTotalIncome(totalIncome);
//		info.setWeekYield(statement.getWeekYield());
//		info.setMonthYield(statement.getMonthYield());
//		
//		long timeOut = vtRedisHelper.isTradeTime()? VTRedisHelper.SHORT_TIME_OUT:VTRedisHelper.LONG_TIME_OUT;
//		stringRedisHelper.set(key, JSONObject.toJSONString(info), timeOut);
//		stopWatch.stop();
//		log.info("getFundInfo time :{}",stopWatch.prettyPrint());
//		return info;
//	}
	
	/**
	 * 获取账户资金的基本嘻嘻
	 * @param accountId
	 * @return
	 */
	public AccountFundInfo queryAccountInfo(String accountId){
		FundStatement fundStatement = getFundStatement(accountId);
		AccountFundInfo accountFundInfo = new AccountFundInfo();
		accountFundInfo.setAccountId(accountId);
		accountFundInfo.setAvailableBalance(fundStatement.getAvailableBalance());
		accountFundInfo.setBeginBalance(fundStatement.getBeginBalance());
		accountFundInfo.setCurrentBalance(fundStatement.getCurrentBalance());
		accountFundInfo.setFrozenBalance(fundStatement.getFrozenBalance());
		accountFundInfo.setLastBalance(fundStatement.getLastBalance());
		return accountFundInfo;
	}
	
	/**
	 * 资金统计状态 （把资金信息可用资金，冻结资金 和 统计信息缓存起来）
	 * @param uuid
	 * @return
	 */
	public FundStatement getFundStatement(String uuid) {
		String key = VTRedisHelper.getFundStatementKey(uuid);
		String value = stringRedisHelper.get(key);
		if(!StringUtils.isBlank(value)){
			return JSONObject.parseObject(value,FundStatement.class);
		}
		JSONObject fundInfoJson = vtHelper.getVtFundInfo( uuid);
		if (fundInfoJson==null) {
			log.info("query fund info is null, uuid = {} ", uuid );
			return null;
		}
		JSONObject zuo = vtHelper.getDateStatement(uuid, vtHelper.getLastStatDate());
//		if(zuo == null || zuo.size() < 1){
//			log.info(" cal  LastStatDate== {} stat is null ",vtHelper.getLastStatDate());
//			zuo =vtHelper.getDayStatLog(uuid);
//			
//		}
		
		// 可用资金
		BigDecimal availableBalance = fundInfoJson.getBigDecimal(
				"current_balance").subtract(fundInfoJson.getBigDecimal("frozen_balance"));
		
		FundStatement state=new FundStatement();
		state.setBeginBalance(fundInfoJson.getBigDecimal("begin_balance"));
		state.setFrozenBalance(fundInfoJson.getBigDecimal("frozen_balance"));
		state.setCurrentBalance(fundInfoJson.getBigDecimal("current_balance"));
		state.setAvailableBalance(availableBalance);
		if(zuo == null || !zuo.containsKey("totalAssets")){
			log.info("query fund zuo stat is null, uuid = {} ", uuid );
			state.setLastBalance(fundInfoJson.getBigDecimal("begin_balance")==null?new BigDecimal(100000):fundInfoJson.getBigDecimal("begin_balance"));
			state.setLastTotalYieldrate(new BigDecimal(0));
			state.setLastTodayYieldrate(new BigDecimal(0));
			state.setWeekYield(new BigDecimal(0));
			state.setMonthYield(new BigDecimal(0));
			state.setLastTodayIncome(new BigDecimal(0));
		}else{
			state.setLastBalance(zuo.getBigDecimal("totalAssets"));
			state.setLastTotalYieldrate(zuo.getBigDecimal("totalYieldRate"));
			state.setLastTodayYieldrate(zuo.getBigDecimal("dayYieldRate"));
			state.setWeekYield(zuo.getBigDecimal("weekYieldRate"));
			state.setMonthYield(zuo.getBigDecimal("monthYieldRate"));
			state.setLastTodayIncome(zuo.getBigDecimal("today_profit"));
		}
		
		stringRedisHelper.set(key, JSONObject.toJSONString(state), VTRedisHelper.LONG_TIME_OUT);
		return state;
	}
	
	public JSONObject queryUserFunds2(String uuid) {
		JSONObject fundInfoJson = vtHelper.getVtFundInfo( uuid);
		return fundInfoJson;
	}

	@Override
	public JSONObject concludeBack(JSONObject param) {
		String uuid = param.getString("uuid");
		//uuid is right
		if(!uuid.startsWith(vtHelper.getFROM_SITE())){
			log.info("uuid isn't this company!");
			return JSONUtil.success();
		}
		//query uuid of userinfo
		QueryWrapper<MatchUserAccount> queryWrapperMatchUser = new QueryWrapper<>();
		queryWrapperMatchUser
				.eq("account_id", uuid);
		MatchUserAccount userAccount =  matchUserAccountMapper.selectOne(queryWrapperMatchUser);
		if(userAccount == null){
			log.info("query userAccount is not Exist ! uuid = {}",uuid );
			return JSONUtil.success();
		}
		
		
		Trading entity = new Trading();
		entity.setAccountId(uuid);
		entity.setUserId(userAccount.getUserId());
		entity.setCommissionId(param.getLong("commissionId"));
		entity.setCommissionType(param.getIntValue("commission_type"));
		entity.setCommissionAmount(param.getIntValue("conclude_amount"));
		entity.setCommissionPrice(param.getBigDecimal("conclude_price"));
		entity.setCommissionStatus(5);
		entity.setConcludePrice(param.getBigDecimal("conclude_price"));
		entity.setStockCode(param.getString("stock_code"));
//		entity.setStockName(getStockName(param.getString("stock_code")));
//		entity.setStockName(param.getString("stock_name"));
		entity.setStockName(param.getString("stock_code"));
		entity.setFromPosition(param.getBigDecimal("position_percent"));
		entity.setToPosition(param.getBigDecimal("position_percent"));
		entity.setConcludeTime(LocalDateTime.now());
		Date date = new Date();
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		entity.setCommissionTime(localDateTime);
		entity.setConcludeTime(getDateTimeOfTimestamp(param.getLong("conclude_time")));
		tradingServiceImpl.save(entity);
		
		vtRedisHelper.deleteUserVt(uuid);
		return JSONUtil.success();
	}

	private LocalDateTime getDateTimeOfTimestamp(long timestamp) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		ZoneId zone = ZoneId.systemDefault();
		return LocalDateTime.ofInstant(instant, zone);
	}

//	@Cacheable(value = "hq:stockName:code", key = "#stockCode", unless = "#stockCode==null")
	public String getStockName(String stockCode){
		StockSummary stockSummary = HQUtil.queryHQ(stockCode);
		return stockSummary == null ? "" : stockSummary.getName();
	}
	
	
	

	@Override
	public JSONObject yieldCurse(String uuid) {
		String key = VTRedisHelper.getYieldCurseKey(uuid);
		String value = stringRedisHelper.get(key);
		if(!StringUtils.isBlank(value)){
			return JSONObject.parseObject(value);
		}
		JSONObject res=incomeRatePicHelper.getYieldcurve(uuid);
		if(!JSONUtil.isFail(res)){
			stringRedisHelper.set(key, res.toJSONString(), VTRedisHelper.BIG_LONG_TIME_OUT);
		}
	
		return res;
	}
	
	/**
     * 是否委托时间
     * @return
     */
    public boolean isCommissionTime() {
    	String key = VTRedisHelper.getCommissionTimeInfoKey(DateToolkit.getNowFormatyyyyMMdd());
		String value = stringRedisHelper.get(key);
		JSONObject map = null;
		if(!StringUtils.isBlank(value)){
			map = JSONObject.parseObject(value);
		}else{
			map = vtHelper.getCommissionTime(DateToolkit.getNowFormatyyyyMMdd());
			if(map != null && map.containsKey("open_close")){
				stringRedisHelper.set(key, map.toJSONString(),60);
			}else{
				log.error("委托时间异常");
			}
		}
		
        boolean isComis = true;
        try {
            String openClose = map.getString("open_close");
            String commission_period = map.getString("commission_period");
            if (("1".equals(openClose) && VTHelper.isTimeInCommissionPeriod(new Date(), commission_period)) || openClose.equals("2")) {
                isComis = true;
            } else {
                isComis = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getIsCommission", e);
        }
        return isComis;
    }
    
    
	
	
//	public JSONObject saveConclude(String uuid,) {
//		//uuid is xianmu
//		
//		//
//		//
//		return null;
//	}

}



