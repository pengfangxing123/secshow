package cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces;

import com.alibaba.fastjson.JSONObject;

public interface MatchRedJobService {

	/**
	 * 获取当天结束的比赛
	 * 单一比赛处理
	 * 普通比赛分配红包
	 *    检查是否已经分配过红包
	 *    获取比赛分配参数
	 *    获取总红包额
	 *    验证红包是否超过总额
	 *    入库
	 *    
	 * 团队赛分配红包
	 *    检查是否已经分配过红包
	 *    获取比赛分配参数
	 *    获取总红包额
	 *    验证红包是否超过总额
	 *    入库
	 * 
	 * 
	 * 
	 * 
	 */
	/**
	 * 处理红包
	 */
	JSONObject dealMatchRed(long matchId);

	void sendTestRed(long matchId);

}