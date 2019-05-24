package cn.com.jrj.vtmatch.basicmatch.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.CacheConstants;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VTRedisHelper {
	//判断是否是交易时间
	@Resource 
	private StringRedisHelper stringRedisHelper;
	public static final String REDIS_CACHE_KEY_PROFIX = CacheConstants.REDIS_CACHE_KEY_PREFIX;
	@Resource 
	private VTHelper vtHelper;
	//交易日缓存标识
	private static final String TRADE_DATE_KEY = REDIS_CACHE_KEY_PROFIX + "trade:date:flag";
	//非交易日缓存标识
	private static final String NOT_TRADE_DATE_KEY = REDIS_CACHE_KEY_PROFIX + "not:trade:date:flag";
	
	private static final String VT_POSITION_PROFIX = REDIS_CACHE_KEY_PROFIX + "vt:positions:";
	
	private static final String VT_POSITION_NP_PROFIX = REDIS_CACHE_KEY_PROFIX + "vt:positions:np:";
	
	private static final String VT_CANCEL_PROFIX = REDIS_CACHE_KEY_PROFIX + "vt:cancels:";
	
	private static final String VT_CONCLUDE_PROFIX = REDIS_CACHE_KEY_PROFIX + "vt:concludes:";
	
	private static final String VT_COMMISSION_PROFIX = REDIS_CACHE_KEY_PROFIX + "vt:commissions:";
	
	private static final String VT_YIELD_CURSE = REDIS_CACHE_KEY_PROFIX + "vt:yield:curse:";
	
	private static final String VT_FUNDINFO = REDIS_CACHE_KEY_PROFIX + "vt:fundInfo:";
	
	private static final String VT_FUNDSTATEMENT = REDIS_CACHE_KEY_PROFIX + "vt:fundStatement:";
	
	private static final String VT_COMMISSION_TIMEINFO = REDIS_CACHE_KEY_PROFIX + "commissionTime_";
	
	public static final long LONG_TIME_OUT  =  60*20L;
	
	public static final long SHORT_TIME_OUT  =  15L;
	
	public static final long BIG_LONG_TIME_OUT  =  7200L;
	
	//最近两天的交易日
	private static final String VT_LAST_TWO_TRADE = REDIS_CACHE_KEY_PROFIX + "vt:lastTwoTradeDay";
	//最近上一次统计
	private static final String VT_LAST_STATMENT_TRADE = REDIS_CACHE_KEY_PROFIX + "vt:lastStatmentDate";
	/**
	 * 判断是否是交易时间
	 * @return
	 */
	public boolean isTradeTime(){
		//时间判断
		Calendar cal = Calendar.getInstance();
		int minuteOfDay = cal.get(Calendar.HOUR_OF_DAY)*60 + cal.get(Calendar.MINUTE);
		if(minuteOfDay<510  || minuteOfDay>930){
			return false;
		}
		//交易日
		if(stringRedisHelper.exist(TRADE_DATE_KEY)){
			return true;
		}else if(stringRedisHelper.exist(NOT_TRADE_DATE_KEY)){
				return false;
		}else{
			if(vtHelper.isTradeDateTime()){
				stringRedisHelper.set(TRADE_DATE_KEY, "1", 60*30);
				return true;
			}else{
				stringRedisHelper.set(NOT_TRADE_DATE_KEY, "0", 60*120);
				return false;
			}
		}
	}
	
	
	public static String getPositionKey(String uuid){
		return VT_POSITION_PROFIX + uuid;
	}
	public static String getCommissionTimeInfoKey(String date){
		return VT_POSITION_PROFIX + date;
	}
	public static String getFundInfoKey(String uuid){
		return VT_FUNDINFO + uuid;
	}
	public static String getFundStatementKey(String uuid){
		return VT_FUNDSTATEMENT + uuid;
	}
	public static String getNpPositionKey(String uuid){
		return VT_POSITION_NP_PROFIX + uuid;
	}
	public static String getlastTwoTradeKey(){
		return VT_LAST_TWO_TRADE;
	}
	public static String getlastStatTradeKey(){
		return VT_LAST_STATMENT_TRADE;
	}
	
	public static String getCancelKey(String uuid){
		return VT_CANCEL_PROFIX + uuid;
	}
	public static String getYieldCurseKey(String uuid){
		return VT_YIELD_CURSE + uuid;
	}
	
	public static String getConcludeKey(String uuid,int pageNo,int pageSize){
		if(pageNo > 3 || pageSize != 10){
			return null;
		}
		return VT_CONCLUDE_PROFIX+uuid+":"+pageNo;
	}
	
	public static String getCommissionKey(String uuid,int pageNo,int pageSize){
		if(pageNo > 3 || pageSize != 10){
			return null;
		}
		return VT_COMMISSION_PROFIX+uuid+":"+pageNo;
	}
	
	/**
	 * 删除用户全部缓存
	 * @param uuid
	 */
	@Async
	public long deleteUserVt(String uuid){
		List<String> dkeys = new ArrayList<String>();
		dkeys.add(getPositionKey(uuid));
		dkeys.add(getCancelKey(uuid));
		dkeys.add(getFundInfoKey(uuid));
		dkeys.add(getFundStatementKey(uuid));
		dkeys.add(getNpPositionKey(uuid));
		dkeys.add(getCommissionKey(uuid, 1, 10));
		dkeys.add(getCommissionKey(uuid, 2, 10));
		dkeys.add(getCommissionKey(uuid, 3, 10));
		dkeys.add(getConcludeKey(uuid, 1, 10));
		dkeys.add(getConcludeKey(uuid, 2, 10));
		dkeys.add(getConcludeKey(uuid, 3, 10));
		long count = stringRedisHelper.delete(dkeys);
		log.info("delete redis key count == " + count);
		return count;
	}
}
