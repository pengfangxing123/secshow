package cn.com.jrj.vtmatch.basicmatch.helper;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.jrj.vtmatch.basicmatch.util.DateToolkit;
import cn.com.jrj.vtmatch.basicmatch.util.HQUtil;
import cn.com.jrj.vtmatch.basicmatch.util.StockSummary;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class IncomeRatePicHelper {
	@Resource 
	private VTHelper vtHelper;

	@Value("${basic.match.hs300.url}")
	private String hs300Url;
	
	private static HashMap<String, Long> hs300hq = new HashMap<String, Long>();
	
	
	public JSONObject getYieldcurve(String uuid){
		String endtime = DateToolkit.getNowFormatyyyyMMddNoSymbol();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -3);
		
		String begintime =  DateToolkit.getFormatyyyyMMddNoSymbol(cal.getTime())+"";
		
		return getYieldcurve(uuid,begintime,endtime);
	}
	
	public  JSONObject getYieldcurve(String uuid, String begintime,
			String endtime) {
		
		JSONObject res = new JSONObject();
		try {
			JSONArray rateOJson=vtHelper.getDayStatLog(uuid, begintime, endtime);
			JSONArray hs300yieldcurve=new JSONArray(); 
			log.debug("hs300yieldcurve === {}",rateOJson);
			log.info("fill data");
			JSONArray yieldcurve= fillORateData(rateOJson,hs300yieldcurve);
			
			
			// 加入恒生指数收益率数据
//			if (categoryNameSet.size() > 0) {
//				List<HSVO> hs300VoList = hSDataInterfaceService.getHS300List(categoryNameSet,lastDate.getLastDate());//根据收益率天数来取
//				hs300yieldcurve = fillHSData(hs300VoList);
//			} else{
//				hs300yieldcurve = new JSONArray();
//			}
			
			res.put("retcode", 0);
			JSONObject data = new JSONObject();
			data.put("incomeList", yieldcurve);
			data.put("incomeListHS300", hs300yieldcurve);
			res.put("data", data);
			
			
//			System.out.println(res.toJSONString());
			return res;
		} catch (Exception e) {
			log.error("getYieldcurve exception:::::",e);
			res.put("retcode", 1);
			res.put("msg", "获取数据异常");
			return res;
		}
	}
	
	public void getHS300(String lastDate){
		String beginDate = DateToolkit.getFormatyyyyMMddNoSymbol(DateToolkit.addDays(new Date(), -90))+"";
//		String url = "https://taurus.jrj.com.cn/hq1/data/kline?securityId=2399300&type=day&range.begin={beginDate}&format=json&range.num=70".replace("{beginDate}", beginDate);
		String url = hs300Url.replace("{beginDate}", beginDate);
		log.info(hs300Url);
		JSONObject res = vtHelper.getJson(url);
		//全部获取
		if(res != null && res.containsKey("retcode") && (0==res.getIntValue("retcode"))){
			JSONObject data = res.getJSONObject("data");
			JSONArray arr= data.getJSONArray("kline");
			if(arr !=null && arr.size() > 50){
				hs300hq.clear();
				arr.forEach(_obj ->{
					JSONObject obj = (JSONObject)_obj;
					hs300hq.put(obj.getString("nTime"), obj.getLong("nLastPx"));
				});
			}else{
				return;
			}
		}
		
		if(!hs300hq.containsKey(lastDate)){
			StockSummary hq = HQUtil.queryHQIndex("399300");
			if(hq != null){
				long value = hq.getNp().multiply(new BigDecimal(10000)).longValue();
				hs300hq.put(lastDate, value);
			}
		}
	}
	/**
	 * 填充收益图 收益率 
	 * @param rateJson
	 * @param categoryNameSet
	 * @param yRangeMap
	 * @param lastDate 
	 */
	private JSONArray fillORateData(JSONArray rateArr,JSONArray hs300yieldcurve){
			JSONArray arr = new JSONArray();
			if(rateArr==null || rateArr.size() < 1)
				return arr;
			Date lastDate = rateArr.getJSONObject(rateArr.size()-1).getDate("date");
			String _lastDate = DateToolkit.getFormatyyyyMMddNoSymbol(lastDate)+"";
			if(!hs300hq.containsKey(_lastDate)){
				getHS300(_lastDate);
			}
		
			double firstYieldRate=0;
			double hs300firstvalue=0;
			double hs300nextValue=0;
			for(int i=0;i<rateArr.size();i++){
				JSONObject obj = rateArr.getJSONObject(i);
				long dateL = obj.getLong("date");
				double yieldRate = obj.getDoubleValue("yieldrate");
				String dateS = DateToolkit.getFormatyyyyMMddNoSymbol(obj.getDate("date"))+"";
				
				if(!hs300hq.containsKey(dateS)){
					continue;
				}
				hs300nextValue = hs300hq.get(dateS);
				if (i==0){
					firstYieldRate = yieldRate;
					hs300firstvalue = hs300nextValue;
				}
				
				yieldRate = (yieldRate - firstYieldRate);
				
				JSONObject jo = new JSONObject();
				jo.put("initDate", dateL);
				jo.put("incomeRate", new BigDecimal(String.valueOf(yieldRate)).setScale(3, BigDecimal.ROUND_HALF_UP));
				
				JSONObject hsjo = new JSONObject();
				hsjo.put("initDate", dateL);
//				System.out.println(hs300firstvalue);
//				System.out.println(hs300nextValue);
//				System.out.println(hs300nextValue-hs300firstvalue);
//				System.out.println(new BigDecimal((hs300nextValue-hs300firstvalue)/hs300firstvalue*100).setScale(5, BigDecimal.ROUND_HALF_UP));
				hsjo.put("incomeRate", hs300firstvalue == 0 ? 0 : new BigDecimal((hs300nextValue-hs300firstvalue)/hs300firstvalue*100.0).setScale(3, BigDecimal.ROUND_HALF_UP));
				
//				categoryNameSet.add(dateS);
				arr.add(jo);
				hs300yieldcurve.add(hsjo);
			}
			System.out.println(JSONObject.toJSONString(hs300yieldcurve, true));
			return arr;
	}
	
	
	public static void main(String[] args) {
//		getYieldcurve("yczq_testnl9","20181001","20181030");
	}
}
