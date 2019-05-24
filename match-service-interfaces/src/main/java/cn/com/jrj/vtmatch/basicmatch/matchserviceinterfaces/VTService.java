package cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces;

import java.math.BigDecimal;

import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.JsonResult;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.AccountFundInfo;

import com.alibaba.fastjson.JSONObject;

public interface VTService {
	/**
	 * 创建金牛账户
	 * @param uid
	 * @return
	 */
	public JSONObject createVTAccount(String uid);

	/**
	 * 创建金牛账户
	 * @param accountId 账户ID
	 * @return
	 */
	public JSONObject joinMatch(String accountId);


	public JsonResult register(String accountId);

	/**
	 * 委托下单
	 * @param uuid
	 * @param entrustPrice
	 * @param entrustAmount
	 * @param BSType
	 * @param stockCode
	 * @return
	 */
	public JSONObject commission(String uuid,BigDecimal entrustPrice, BigDecimal entrustAmount,
			Integer BSType, String stockCode,int stockType);

	/**
	 * 委托撤单
	 */
	public JSONObject orderCancel(String uuid, long commissionId);

	/**
	 * 查询最大买入卖出量
	 * @param uuid
	 * @param stockCode
	 * @param tradeType
	 * @param entrustPrice
	 * @return
	 */
	public JSONObject queryEntrustLimit(String uuid, String stockCode, int tradeType, BigDecimal entrustPrice);

	/**
	 * 查询成交记录(T)
	 * 
	 * @param uuid
	 * @return QueryUserFunds
	 */
	public JSONObject queryConcludeList(String uuid, int pn, int ps);

	/**
	 * 查询委托记录(T)
	 * 
	 * @param uuid
	 * @return queryCommissionList
	 */
	public JSONObject queryCommissionList(String uuid, int pn, int ps);

	/**
	 * 可撤单的委托
	 * 
	 * @param uuid
	 * @return List<Cancel>
	 */
	public JSONObject queryCancel(String uuid);

	/**
	 * 持仓列表
	 * 
	 * @param uuid
	 */
//	public JSONObject queryPositionList(String uuid);

	/**
	 * 查询用户资金（包含今日盈亏、总盈亏、盈亏额）
	 * 
	 * @param uuid
	 * @return QueryUserFunds
	 */
//	public JSONObject queryUserFunds(String uuid);
	/**
	 * 查询用户资金（包含今日盈亏、总盈亏、盈亏额）
	 * 
	 * @param uuid
	 * @return QueryUserFunds
	 */
	public JSONObject queryUserFunds2(String uuid);
	/**
	 * 回单处理  
	 * @param param
	 * @return
	 */
	public JSONObject concludeBack(JSONObject param);
	/**
	 * 收益图比较  
	 * @param param
	 * @return
	 */
	public JSONObject yieldCurse(String uuid);

	/**
	 * 重置账户
	 * @param uid
	 * @return
	 */
	public JSONObject reset(String uid);

	/**
	 * 获取账户资金的基本嘻嘻
	 * @param accountId
	 * @return
	 */
	public AccountFundInfo queryAccountInfo(String accountId);

	/**
	 * 金牛持仓接口
	 * @param uuid
	 * @return
	 */
	public JSONObject getVTPosition(String uuid);

}