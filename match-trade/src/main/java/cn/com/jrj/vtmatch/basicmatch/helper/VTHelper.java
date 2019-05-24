/**
 * 金牛接口
 */
package cn.com.jrj.vtmatch.basicmatch.helper;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.JsonResult;
import cn.com.jrj.vtmatch.basicmatch.model.StockFee;
import cn.com.jrj.vtmatch.basicmatch.util.DateToolkit;
import cn.com.jrj.vtmatch.basicmatch.util.HttpUtil;
import cn.com.jrj.vtmatch.basicmatch.util.JSONUtil;
import cn.com.jrj.vtmatch.basicmatch.util.MvtException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class VTHelper {
	

	@Resource
	private StringRedisHelper stringRedisHelper;
	
    private RestTemplate restTemplate;
    
    
    private String vtDomain;
    //金牛的分组Id
    private String mid;
    //金牛uuid前缀
    private String FROM_SITE;

    @PostConstruct
    public void init(){
        this.REG_USER_URL = this.vtDomain + "/userinfo";
        this.JOIN_MATCH_URL = this.vtDomain + "/matchAccount/" + mid;
        this.VT_MATCHACCOUNT_GET_MATCHCOUNT_URL = this.vtDomain + "/matchAccount/" + mid + "/matchCount";
        this.VT_ACCOUNT_RESET = this.vtDomain + "/tool/{uuid}/{mid}/{eid}/reset";
        this.VT_matchAccount_DELETE_URL = this.vtDomain + "/matchAccount/$mid/$matchAccountID";
        this.FUND_URL = this.vtDomain + "/funds/$uuid/$mid/$eid";
        this.VT_COMMISSION_POST_URL = this.vtDomain + "/commissions/{uuid}/{mid}/{eid}?returnType=2&batch=false";
        this.VT_COMMISSIONS_PUT_URL = this.vtDomain + "/commissions/{uuid}/{mid}/{eid}/{commissionId}";
        this.VT_restraint_buy_GET_URL = this.vtDomain + "/restraint/{uuid}/{mid}/{eid}/buy?scode={scode}";
        this.VT_restraint_sell_GET_URL = this.vtDomain + "/restraint/$uuid/$mid/$eid/sell?scode=$1";
        this.VT_canCancel_GET_URL = this.vtDomain + "/commissions/{uuid}/{mid}/{eid}/canCancel?order={order}";
        this.VT_commissions_GET_URL = this.vtDomain + "/commissions/{uuid}/{mid}/{eid}?&pn={pn}&ps={ps}&begintime={begintime}&endtime={endtime}";
        this.VT_concludes_GET_URL = this.vtDomain + "/concludes/{uuid}/{mid}/{eid}/his?pn={pn}&ps={ps}&begintime={begintime}&endtime={endtime}";
        this.COMMISSION_TIME_URL = this.vtDomain + "/restraint/$mid/$eid/commissionTime?date=$1";
        this.DAY_STAT_LOG = this.vtDomain + "/dayStatLog/{uuid}/{mid}/{eid}/statement";
        this.VT_openclose_GET_URL = this.vtDomain + "/exchangecalendar/1/openclose/";
        this.VT_LASTTRADETIME_GET_URL = this.vtDomain + "/exchangecalendar/1/lasttradetime/";
        this.VT_PRE_TRADE_GET_URL = this.vtDomain + "/exchangecalendar/1/precioustradetime?days=";
        this.VT_userinfo_GET_URL = this.vtDomain + "/userinfo/$uuid";
        this.VT_positions_GET_URL = this.vtDomain + "/positions/$uuid/$mid/$eid?pn=1&ps=10000";
        this.VT_commissions_concludes_page_GET_URL = this.vtDomain + "/commissions/{uuid}/{mid}/{eid}?commission_status=7,8&pn={pn}&ps={ps}&begintime={begintime}&endtime={endtime}";
        this.VT_daystatlog_GET_URL = this.vtDomain + "/dayStatLog/{uuid}/{mid}/{eid}/yieldRate?begintime={begintime}&endtime={endtime}";
        this.VT_statement_GET_URL = this.vtDomain + "/dayStatLog/{uuid}/{mid}/{eid}/statement";
        this.VT_statement_from_lasttradedate_GET_URL = this.vtDomain + "/dayStatLog/{uuid}/{mid}/{eid}/statement?date={endtime}";
        this.VT_list_match_rule_GET_URL=this.vtDomain+"/matchrule/listMatchRule/{mid}/{eid}";
        this.VT_creat_match_rule_POST_URL=this.vtDomain+"/matchrule/createMatchRule/{mid}/{eid}";
        this.VT_ACCOUNTID = eid+"_"+mid;
    }

    public VTHelper(@Qualifier("oAuth2RestTemplate") RestTemplate restTemplate, @Value("${basic.match.vt.url}") String vtDomain,
                    @Value("${basic.match.vt.mid}") String mid,@Value("${basic.match.vt.fromSize}") String fromSite){
    	log.info("oAuth2RestTemplate");
        this.restTemplate = restTemplate;
    	this.vtDomain = vtDomain;
    	this.mid = mid;
    	this.FROM_SITE = fromSite;
    }

//    public VTHelper(RestTemplate restTemplate, @Value("${basic.match.vt.url}") String vtDomain){
//        this.restTemplate = restTemplate;
//        this.vtDomain = vtDomain;
//    }
   

    //交易品种  目前指A股
    private String eid = "SSAS";

    public String VT_ACCOUNTID = eid+"_"+mid;
    // 默认每页条数
    public static final String PAGER_DEFAULT_PS = "30";
    // 默认页码
    public static final String PAGER_DEFAULT_PN = "1";
    // 虚盘炒股大赛接口服务器
//    public static final String vtDomain = "http://vt.jrjc.gateway";

    public static final boolean SINGLE_THREE_ON = false;  //单三模式

    private static final String VT_BeginTime = "19970101";
    private static final String VT_EndTime = "21000101";


//    private static String VT_matchAccount_GET_matchAccountID_URL = vtDomain + "/matchAccount/$mid/$matchAccountID";

    // 金牛用户注册接口
    private String REG_USER_URL = vtDomain + "/userinfo";
    //金牛用户分组接口
    private  String JOIN_MATCH_URL = vtDomain+ "/matchAccount/" + mid;
    //金牛账户的人数
    private String VT_MATCHACCOUNT_GET_MATCHCOUNT_URL = vtDomain + "/matchAccount/" + mid + "/matchCount";
    //重置账户
    private String VT_ACCOUNT_RESET = vtDomain + "/tool/{uuid}/{mid}/{eid}/reset";
    //删除账户
    private String VT_matchAccount_DELETE_URL = vtDomain  + "/matchAccount/$mid/$matchAccountID";

    //获取资金信息接口
    private String FUND_URL = vtDomain + "/funds/$uuid/$mid/$eid";
    //提交委托接口
    private String VT_COMMISSION_POST_URL = vtDomain+ "/commissions/{uuid}/{mid}/{eid}?returnType=2&batch=false";
    //撤单接口
    private String VT_COMMISSIONS_PUT_URL = vtDomain + "/commissions/{uuid}/{mid}/{eid}/{commissionId}";
    //根据用户输入的证券代码获取买入限制信息
    private String VT_restraint_buy_GET_URL = vtDomain + "/restraint/{uuid}/{mid}/{eid}/buy?scode={scode}";
    //根据用户选择的证券代码获取卖出限制信息
    private String VT_restraint_sell_GET_URL = vtDomain + "/restraint/$uuid/$mid/$eid/sell?scode=$1";
    //获取可以撤单的委托
    private String VT_canCancel_GET_URL = vtDomain + "/commissions/{uuid}/{mid}/{eid}/canCancel?order={order}";
    //查询委托记录列表
    private String VT_commissions_GET_URL = vtDomain  + "/commissions/{uuid}/{mid}/{eid}?&pn={pn}&ps={ps}&begintime={begintime}&endtime={endtime}";
    //成交列表查询
    private String VT_concludes_GET_URL = vtDomain + "/concludes/{uuid}/{mid}/{eid}/his?pn={pn}&ps={ps}&begintime={begintime}&endtime={endtime}";
    // 获取交易时间限制
    private String COMMISSION_TIME_URL = vtDomain	+ "/restraint/$mid/$eid/commissionTime?date=$1";


    public String DAY_STAT_LOG = vtDomain + "/dayStatLog/{uuid}/{mid}/{eid}/statement";
    //
    private String VT_openclose_GET_URL = vtDomain + "/exchangecalendar/1/openclose/";
    private String VT_LASTTRADETIME_GET_URL = vtDomain + "/exchangecalendar/1/lasttradetime/";
    private String VT_PRE_TRADE_GET_URL = vtDomain + "/exchangecalendar/1/precioustradetime?days=";
    
    //获取比赛规则（账户设置）
    private String VT_list_match_rule_GET_URL=this.vtDomain+"/matchrule/listMatchRule/{mid}/{eid}";
    //编辑比赛规则（账户设置）
    private String VT_creat_match_rule_POST_URL=this.vtDomain+"/matchrule/createMatchRule/{mid}/{eid}";

    public String getVT_ACCOUNTID(){
        return this.VT_ACCOUNTID;
    }

    public String getFROM_SITE(){
        return this.FROM_SITE;
    }

    /**
     * 注册金牛账户
     * @param accountId 大赛的账户
     * @return  uuid  金牛的账户
     */
    public JsonResult register(String accountId) {
        if(JSONUtil.isFail(create(accountId))){
            log.info("register fail! create account fail");
            return JsonResult.buildFail("创建金牛账户失败！");
        }
        String uuid = FROM_SITE +"_"+accountId;
        if(JSONUtil.isFail(joinMatch(uuid))){
            log.info("register fail! join match fail");
            return JsonResult.buildFail("金牛账户分组失败！");
        }
        log.info("register success! account uuid = " + uuid);
        return JsonResult.buildSuccess(uuid);
    }


    public JSONObject create( String uid) {
    	JSONObject param = new JSONObject();
        param.put("fromsite", FROM_SITE);
        param.put("userid", uid);
        JSONObject res = postJson(REG_USER_URL, param.toJSONString());
        log.info("create：：：{}-================-{}", param.toJSONString(),res.toJSONString());
        res.put("accountId",FROM_SITE+"_"+uid);
        return res;
    }

    public JSONObject joinMatch(String uuid) {
        JSONObject param = new JSONObject();
        param.put("uuid", uuid);
        param.put("matchID", mid);
        JSONObject res = postJson(JOIN_MATCH_URL, param.toJSONString());
        return res;
    }

    public JSONObject getUserCount(){
        JSONObject res = getJson(VT_MATCHACCOUNT_GET_MATCHCOUNT_URL);
        log.info(res.toJSONString());
        return res;
    }

    public JSONObject reset(String uuid) {
        String url = VT_ACCOUNT_RESET;
        url = url.replace("{uuid}", uuid);
        url = url.replace("{mid}", mid);
        url = url.replace("{eid}", eid);
        JSONObject res = postJson(url, "");
        return res;
    }

    /**
     * 用户资金情况
     * @param uuid
     * @return
     */
    public  JSONObject getVtFundInfo(String uuid) {
    	
        String url = FUND_URL.replaceAll("\\$uuid", uuid);
        url = url.replaceAll("\\$mid", mid);
        url = url.replaceAll("\\$eid", eid);
        log.info("fund url === " + url);
        JSONObject context = getJson(url);
        log.info("fund  === " + context.toJSONString());

//        System.out.println("fund  === " + context.toJSONString());
        return context;
    }

    /**
     * 下单
     * @return
     */
    public  JSONObject commissions(String uuid, String jsonData){
        String url = VT_COMMISSION_POST_URL;
        url = url.replace("{uuid}", uuid);
        url = url.replace("{mid}", mid);
        url = url.replace("{eid}", eid);
        System.out.println("commissions url:" + url);
        //System.out.println("commissions data:" + jsonData);
        JSONObject res = postJson(url, jsonData);
//        try {
//
//			int code = HttpTookit.doPostJson(url, jsonData, "utf-8");
//			System.out.println("code === " + code);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        log.info("commissions return code:" + res.toJSONString());
        return res;
    }

    /**
     * 撤单
     */
    public JSONObject cancelCommission(String uuid, Long commissionId,String jsonData){
        String url = VT_COMMISSIONS_PUT_URL;
        url = url.replace("{uuid}", uuid);
        url = url.replace("{mid}", mid);
        url = url.replace("{eid}", eid);
        url = url.replace("{commissionId}", commissionId+"");        

        Integer code = null;
        log.info("put CanCancelCommission url:" + url);
        log.info("put CanCancelCommissionjson:" + jsonData);

        JSONObject res = putJson(url, jsonData);

        log.info("putCanCancelCommission return code:" + code);
        return res;
    }



    /**
     * 得到用户信息（判断用户是否注册）
     GET  http://127.0.0.1:8080/userinfo/{uuid}
     uuid的组成规则为： “所属网站”+“_”+“用户id”
     在虚炒港股中接入“金融界”网站的用户，“所属网站”固定为“jrj”。
     “用户id”就是用户在“金融界”网站的ID。
     * 示例：
     GET  http://127.0.0.1:8080/userinfo/jrj_555
     用户已注册返回：
     {"fromsite":"jrj","remark":"","status":0,"userid":"555","username":"王五额","uuid":"jrj_555"}
     用户未注册返回：
     null
     if(result.equals(“null”)){
     // 2. 用户注册
     }else{
     // 3. 判断用户有没有参加比赛
     }
     */
    private String VT_userinfo_GET_URL = vtDomain + "/userinfo/$uuid";






    /**
     *  获取用户的持仓情况
     *  GET http://127.0.0.1:8080/positions/{uuid}/{mid}/{eid}?pn=1&ps=10&st=13
     *  参数：pn是页码，默认为1；ps是每页显示的条数，默认为20；st: 证券类型(参考数据字典)；
     *  增加了一个证券类型参数
     *  返回：
     *  {"count":1,"items":[{"accountId":"HKS_1","cost_price":106.989,"current_amount":6000.00,"frozen_amount":1000.00,"money_type":"2","positionId":140,"stock_code":"00001","stock_type":"0 ","uuid":"123"}]}
     *  cost_price是成本价、stock_code是证券代码、current_amount是证券数量、frozen_amount是冻结数量、cost_price平均成本、stock_type证券类型（参考字典）、money_type（资金类型）、charge_type申购方式（1前端/2后端）、div_cls_type（分红方式）、div_balance（现金分红）、current_price（当前价/净值）、cost_balance（投入资金/成本金）、stock_name（证券名称）
     */
    private String VT_positions_GET_URL = vtDomain + "/positions/$uuid/$mid/$eid?pn=1&ps=10000";



    /**
     * 查询委托记录中的成交记录列表(分页)
     */
    private String VT_commissions_concludes_page_GET_URL = vtDomain   + "/commissions/{uuid}/{mid}/{eid}?commission_status=7,8&pn={pn}&ps={ps}&begintime={begintime}&endtime={endtime}";


    /** 收益率
     *  示例：
     GET http://127.0.0.1:8080/dayStatLog/{uuid}/{mid}/{eid}/yieldRate?begintime=20100510&endtime=20100517
     begintime默认为30天以前，endtime默认为当天。注意格式为yyyyMMdd
     返回：
     [{"uuid":"123","accountid":"HKS_1","period":"PM","date":1273507200000,"yieldrate":0.11},
     {"uuid":"123","accountid":"HKS_1","period":"PM","date":1273593600000,"yieldrate":0.12},
     {"uuid":"123","accountid":"HKS_1","period":"PM","date":1273680000000,"yieldrate":0.1}]
     结果集参数说明：uuid:全局用户ID;accountid:交易账户ID;period:时段;date:日期;yieldrate:收益率
     日期使用的是java.util.Date.getTime()
     */
    private String VT_daystatlog_GET_URL = vtDomain + "/dayStatLog/{uuid}/{mid}/{eid}/yieldRate?begintime={begintime}&endtime={endtime}";
//    private static String VT_daystatlog_GET_URL2 = vtDomain + "/dayStatLog/{uuid}/20/SSAS/yieldRate";

//    /**
//     * 地址：http://127.0.0.1:8080/exchangecalendar/{marketType}/lasttradetime
//        方法：GET
//        返回数据类型：application/json;charset=utf-8
//            获得指定日期之前的最后一个交易日
//             返回JSON数据：
//             {"last_trade_time":"2012-10-19","endDate":"2012-10-22"}
//     */
//    private static String VT_lasttradedate_GET_URL = vtDomain + "/exchangecalendar/1/lasttradetime";

    /**
     * 地址：http://127.0.0.1:8080/dayStatLog/{uuid}/{mid}/{eid}/statement
     方法：GET
     返回数据类型：application/json;charset=utf-8
     总资产估值水平 等
     */
    private String VT_statement_GET_URL = vtDomain + "/dayStatLog/{uuid}/{mid}/{eid}/statement";
    private String VT_statement_from_lasttradedate_GET_URL = vtDomain + "/dayStatLog/{uuid}/{mid}/{eid}/statement?date={endtime}";

    /**
     * 获取用户信息
     * @param uuid 用户id；格式为：fromsite_userid_pid
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public JSONObject checkUserInfo(String uuid) throws ClientProtocolException, IOException{
        String url = VT_userinfo_GET_URL.replace("$uuid", uuid);
        log.info("checkUserInfo, get url:" + url );
        JSONObject res = getJson(url);
        //System.out.println(res.toJSONString());
        return res;
    }

    /**
     * 删除金牛投组比赛账户
     * @param uuid
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public boolean deleteMatchAccount(String uuid) {
        String url = VT_matchAccount_DELETE_URL.replace("$mid", mid +"").replace("$matchAccountID", mid + "_" + uuid);
        HttpUtil.deleteJson(url, null);
        //System.out.println(res.toJSONString());
        return true;
    }


    /**
     * 得到用户购买限制
     * @param uuid
     * @param scode
     * @return
     */
    public JSONObject getRestraintBuyInfo(String uuid, String scode) {
        String url = VT_restraint_buy_GET_URL;
        url = url.replace("{uuid}", uuid);
        url = url.replace("{mid}", mid);
        url = url.replace("{eid}", eid);
        url = url.replace("{scode}", scode);
        log.info("getRestraintBuyInfo url:" + url);
        JSONObject res = getJson(url);

        log.info("getRestraintBuyInfo res:" + res.toJSONString());
        return res;
    }

    /**
     * 得到用户卖出限制信息
     * @param mid
     * @param eid
     * @param uuid
     * @param scode
     * @return
     */
    public JSONObject getRestraintSellInfo(String uuid, String scode) {
        String url = VT_restraint_sell_GET_URL.replace("$uuid", uuid);
        url = url.replace("$mid", mid);
        url = url.replace("$eid", eid);
        url = url.replace("$1", scode);
        log.info("getRestraintSellInfo url:" + url);
        JSONObject res = getJson(url);
//        System.out.println(res.toJSONString());
        return res;
    }

    /**
     * 得到用户可撤单列表
     * @param uuid
     * @param order
     * @return
     */
    public JSONObject cancelList(String uuid, String order){
        String url = VT_canCancel_GET_URL;
        url = url.replace("{uuid}", uuid);
        url = url.replace("{mid}", mid);
        url = url.replace("{eid}", eid);
        url = url.replace("{order}", order);
        log.info("getCanCancelList url:" + url);
        JSONObject res = getJson(url);
//        System.out.println(JSONObject.toJSONString(res, true));
        return res;
    }

    /**
     * 得到委托记录列表
     * @param uuid
     * @param order
     * @return
     */
    public JSONObject getCommissionsList(String uuid, String begintime, String endtime, int pn, int ps) {
        String url = VT_commissions_GET_URL;
        url = url.replace("{pn}", pn+"").replace("{ps}", ps+"");
        if(StringUtils.isEmpty(begintime) && StringUtils.isEmpty(endtime)){
            url = url.replace("{uuid}", uuid)
                    .replace("{eid}", eid)
                    .replace("{mid}", mid)
                    .replace("{begintime}", VT_BeginTime)
                    .replace("{endtime}", VT_EndTime);
        }else{
            url = url.replace("{uuid}", uuid)
                    .replace("{eid}", eid)
                    .replace("{mid}", mid)
                    .replace("{begintime}", begintime)
                    .replace("{endtime}", endtime);
        }
        log.info("getCommissionsList url:" + url);
        JSONObject res = getJson(url);
//        System.out.println(JSONObject.toJSONString(res, true));
        return res;
    }

    /**
     * 获得持仓Json字符串
     *
     * @param uuid
     * @return
     */
    public JSONObject positionList(String uuid) {
        String url = VT_positions_GET_URL.replace("$uuid", uuid);
        url = url.replace("$mid", mid);
        url = url.replace("$eid", eid);
        log.info("getUserPositions url:" + url);
        JSONObject res = getJson(url);
        
        
        return res;
    }

    /**
     * 获取指定区间的收益率情况
     * @param uuid
     * @param begintime
     * @param endtime
     * @return
     */
    public  JSONArray getDayStatLog(String uuid,String begintime,String endtime){
    	log.info("getDayStatLog ");
        if(begintime != null && endtime != null && begintime.hashCode() == endtime.hashCode()) {
            return  new JSONArray();
        }
        try {
        	String url = VT_daystatlog_GET_URL.replace("{uuid}", uuid)
        			.replace("{eid}", eid)
        			.replace("{mid}", mid)
        			.replace("{begintime}", begintime)
        			.replace("{endtime}", endtime);
        	log.info(url);
        	JSONObject res = getJson(url);
        	log.info("getDayStatLog data: {}" , res.toJSONString());
        	return res.getJSONArray("data");
		} catch (Exception e) {
			log.info("getDayStatLog Exception !",e);
		}
        return new JSONArray();
    }

    /**
     * 查询账户交易记录
     * @param uuid
     * @param begintime
     * @param endtime
     * @return
     */
    public JSONObject getConcludes(String uuid,String begintime,String endtime, int pn, int ps){
        String url = VT_concludes_GET_URL;
        url = url.replace("{pn}", pn+"").replace("{ps}", ps+"");
        if(StringUtils.isEmpty(begintime) && StringUtils.isEmpty(endtime)){
            url = url.replace("{uuid}", uuid)
                    .replace("{eid}", eid)
                    .replace("{mid}", mid)
                    .replace("{begintime}", VT_BeginTime)
                    .replace("{endtime}", VT_EndTime);
        }else{
            url = url.replace("{uuid}", uuid)
                    .replace("{eid}", eid)
                    .replace("{mid}", mid)
                    .replace("{begintime}", begintime)
                    .replace("{endtime}", endtime);
        }
//        log.info("getConcludes url:" + url);
        JSONObject res = getJson(url);
//        System.out.println(JSONObject.toJSONString(res, true));
        return res;
//        log.info("getConcludes return:" + context);
    }

    /**
     * 从委托记录里查询成交记录
     * @param uuid
     * @param begintime
     * @param endtime
     * @param pn
     * @param ps
     * @return
     */
    public String getConcludesFromCommissions(String uuid,String begintime,String endtime, int pn, int ps){
        String url = VT_commissions_concludes_page_GET_URL;
        url = url.replace("{pn}", pn+"").replace("{ps}", ps+"");
        if(StringUtils.isEmpty(begintime) && StringUtils.isEmpty(endtime)){
            url = url.replace("{uuid}", uuid)
                    .replace("{eid}", eid)
                    .replace("{mid}", mid)
                    .replace("&begintime={begintime}&endtime={endtime}", "");
        }else{
            url = url.replace("{uuid}", uuid)
                    .replace("{eid}", eid)
                    .replace("{mid}", mid)
                    .replace("{begintime}", begintime)
                    .replace("{endtime}", endtime);
        }
//        log.info("getConcludesFromCommissions url:" + url);
        JSONObject res = getJson(url);
//        System.out.println(res.toJSONString());
        return res.toJSONString();
    }

    /**
     * 查询账户交易频率指标
     * @param uuid
     * @param begintime
     * @param endtime
     * @return
     */
    public String getStatement(String uuid){
        String url = VT_statement_GET_URL;
        //System.out.println(url);
        url = url.replace("{uuid}", uuid)
                .replace("{eid}", eid)
                .replace("{mid}", mid);
        log.info("getStatement url:" + url);
        JSONObject res = getJson(url);
//        System.out.println(res.toJSONString());
        return res.toJSONString();
    }

    /**
     * 判断当前日期是否是交易日
     * @return
     */
    public boolean isTradeDateTime(){
        JSONObject res = getJson(VT_openclose_GET_URL);
        if(res == null){
            return false;
        }else if(res.getIntValue("open_close") == 1){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 判断当前日期是否是交易日
     * @return
     */
    public boolean isTradeDate(){
    	JSONObject res = getJson(VT_openclose_GET_URL);
    	if(res == null){
    		return false;
    	}else if(res.getIntValue("open_close") == 1){
    		return true;
    	}else{
    		return false;
    	}
    }

    /**
     * 获取前一个交易日
     * @return
     */
    public String getLastTradeDate(){
        JSONObject res = getJson(VT_LASTTRADETIME_GET_URL);
        log.info("last trade date = " + res);
        if(res == null || StringUtils.isBlank(res.getString("last_trade_time"))){
            log.info("date error! get default date");
            return DateToolkit.getNowFormatyyyyMMdd();
        }else{
            return res.getString("last_trade_time").replaceAll("-", "");
        }
    }
    
    /**
     * 获取前一个交易日
     * @return
     */
    public String getPreTradeDate(int days){
    	JSONObject res = getJson(VT_PRE_TRADE_GET_URL+days);
    	log.info("last trade date = " + res);
    	if(res == null || StringUtils.isBlank(res.getString("precious_trade_time"))){
    		log.info("date error! get default date");
    		return getLastTradeDate();
    	}else{
    		return res.getString("precious_trade_time").replaceAll("-", "");
    	}
    }

    /**
     * 获取交易时间
     * @return
     */
    public boolean getIsCommission() {
        boolean isComis = true;
        try {
            JSONObject map =  getCommissionTime(DateToolkit.getNowFormatyyyyMMdd());
            
            String openClose = map.getString("open_close");
            String commission_period = map.getString("commission_period");
            
            if (("1".equals(openClose) && isTimeInCommissionPeriod(new Date(), commission_period)) || openClose.equals("2")) {
                isComis = true;
            } else {
                isComis = false;
            }
        } catch (Exception e) {
            log.error("getIsCommission", e);
        }
        return isComis;
    }

    public static boolean isTimeInCommissionPeriod(Date nowDateTime, String commissionPeriod) {
        if (commissionPeriod==null||commissionPeriod.indexOf("-")<0) {
            return false;
        }
        String[] commissionPeriods = commissionPeriod.split("-");
        // 下午收盘时间
        Calendar c4 = Calendar.getInstance();
        c4.setTime(nowDateTime);
        c4.set(Calendar.HOUR_OF_DAY, Integer.parseInt(commissionPeriods[1].split(":")[0]));
        c4.set(Calendar.MINUTE, Integer.parseInt(commissionPeriods[1].split(":")[1]));
        c4.set(Calendar.SECOND, 0);

        //收盘后2个半小时之内不允许委托
        Calendar c5 =Calendar.getInstance();
        c5.setTime(c4.getTime());
        c5.add(Calendar.HOUR_OF_DAY, 2);
        c5.add(Calendar.MINUTE, 30);

        if ((nowDateTime.getTime()>=c4.getTime().getTime()
                &&nowDateTime.getTime()<=c5.getTime().getTime())) {
            return false;
        } else {
            return true;
        }

    }

    public JSONObject getCommissionTime(String date) {
        String url = COMMISSION_TIME_URL.replaceAll("\\$mid", mid);
        url = url.replaceAll("\\$eid", eid);
        url = url.replaceAll("\\$1", date);
        return getJson(url);
    }

    public byte timeInTradePeriod(Date nowDateTime, String tradePeriod) {
        if (tradePeriod==null||tradePeriod.indexOf("-")<0||tradePeriod.indexOf(",")<0) {
            return 0;
        }
        tradePeriod = tradePeriod.replaceAll(",", "-");
        String[] tradePeriods = tradePeriod.split("-");
        // 早开盘时间
        Calendar c1 = Calendar.getInstance();
        c1.setTime(nowDateTime);
        c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tradePeriods[0].split(":")[0]));
        c1.set(Calendar.MINUTE, Integer.parseInt(tradePeriods[0].split(":")[1]));
        c1.set(Calendar.SECOND, 0);

        // 早收盘时间
        Calendar c2 = Calendar.getInstance();
        c2.setTime(nowDateTime);
        c2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tradePeriods[1].split(":")[0]));
        c2.set(Calendar.MINUTE, Integer.parseInt(tradePeriods[1].split(":")[1]));
        c2.set(Calendar.SECOND, 0);

        // 午开盘时间
        Calendar c3 = Calendar.getInstance();
        c3.setTime(nowDateTime);
        c3.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tradePeriods[2].split(":")[0]));
        c3.set(Calendar.MINUTE, Integer.parseInt(tradePeriods[2].split(":")[1]));
        c3.set(Calendar.SECOND, 0);

        // 午收盘时间
        Calendar c4 = Calendar.getInstance();
        c4.setTime(nowDateTime);
        c4.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tradePeriods[3].split(":")[0]));
        c4.set(Calendar.MINUTE, Integer.parseInt(tradePeriods[3].split(":")[1]));
        c4.set(Calendar.SECOND, 0);

        if(nowDateTime.getTime()<c1.getTime().getTime()||nowDateTime.getTime()>c4.getTime().getTime()){
            return 2;//盘后时间
        }else	if ((nowDateTime.getTime()>=c1.getTime().getTime()
                &&nowDateTime.getTime()<=c2.getTime().getTime())
                ||(nowDateTime.getTime()>=c3.getTime().getTime()
                &&nowDateTime.getTime()<=c4.getTime().getTime())) {
            return 1;//盘中时间
        } else {
            return 3;//中午休盘时间
        }

    }
    
    /**
     * 获取昨日总资产
     * @param uuid
     * @return
     */
    public JSONObject getDateStatement(String uuid,String endtime){
    	String url = VT_statement_from_lasttradedate_GET_URL;
    	url = url.replace("{uuid}", uuid)
    			.replace("{eid}", eid)
    			.replace("{mid}", mid)
    			.replace("{endtime}", endtime);
    	log.info("getLastTradeDateStatement url:" + url);
    	try {
    		return getJson(url);
		} catch (Exception e) {
			log.error("getDateStatement request fail",e);
		}
    	return null;
    }

    public JSONObject getDayStatLog(String uuid){
        String url = DAY_STAT_LOG.replace("{uuid}", uuid)
                .replace("{eid}", eid)
                .replace("{mid}", mid);
        JSONObject obj = null;
        try {
        	obj =  getJson(url);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return obj;
    }
    
    /**
     * 后台查询比赛规则（账户设置）
     * @return
     */
    public JSONObject getMatchRule(){
    	String url=VT_list_match_rule_GET_URL;
        url = url.replaceAll("\\{mid}", mid);
        url = url.replaceAll("\\{eid}", eid);
        log.info("fund url === " + url);
        JSONObject context = getJson(url);
        log.info("fund  === " + context.toJSONString());
//      System.out.println("fund  === " + context.toJSONString());
      return context;
    }
    
    /**
     * 后台编辑比赛规则（账户设置）
     * @param cFee 佣金
     * @param sTax 印花税
     * @param beginBalance 初始资金
     */
	public JSONObject createMatchRule(String cFee, String sTax, String beginBalance) {
    	String url=VT_creat_match_rule_POST_URL;
        url = url.replaceAll("\\{mid}", mid);
        url = url.replaceAll("\\{eid}", eid);
        log.info("fund url === " + url);
        List<Map<String,Object>> arrayList = new ArrayList<Map<String,Object>>();
        Map<String, Object> cFeeMap = new HashMap<String,Object>();
        Map<String, Object> tFeeMap = new HashMap<String,Object>();
        Map<String, Object> beginBalanceMap = new HashMap<String,Object>();
        cFeeMap.put("ruleKey", "C_FEE");
        cFeeMap.put("ruleValue", String.valueOf(new BigDecimal(cFee).divide(new BigDecimal(1000),6,BigDecimal.ROUND_HALF_UP)));
        tFeeMap.put("ruleKey", "S_TAX");
        tFeeMap.put("ruleValue", String.valueOf(new BigDecimal(sTax).divide(new BigDecimal(1000),6,BigDecimal.ROUND_HALF_UP)));
        beginBalanceMap.put("ruleKey", "BEGIN_BALANCE ");
        beginBalanceMap.put("ruleValue", beginBalance);
        arrayList.add(cFeeMap);
        arrayList.add(tFeeMap);
        arrayList.add(beginBalanceMap);
        return postJson(url, JSON.toJSONString(arrayList));
	}

    public  JSONObject postJson(String url, String requestBody) {
        JSONObject response = null;
        try {
        	HttpHeaders headers = new HttpHeaders();
        	headers.setContentType(MediaType.APPLICATION_JSON);
        	headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        	
        	HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);

            ResponseEntity<String> res = restTemplate.postForEntity(url,entity,String.class );
            
            if (res.getStatusCodeValue() == HttpStatus.SC_OK) {
                // 返回json格式：
                String result = (String)res.getBody();
                log.debug("http post json res === {}", result);
                response = JSONObject.parseObject(result);
            } else if (res.getStatusCodeValue() == HttpStatus.SC_CREATED) {
            	log.debug("http post json res === {}", res.getBody());
                response = JSONUtil.retSuccess("success");
            } else if (res.getStatusCodeValue() == HttpStatus.SC_BAD_REQUEST) {
            	log.debug("http post json res === {}", res.getBody());
            	response = JSONUtil.fail("error");
            	response.put("retCode", MvtException.NO_FUND_ERROR.getErrCode());
            } else {
                response = JSONUtil.fail("error");
            }
        } catch (Exception e) {
        	log.error("postJson error:",e);
        	if(e.getMessage().contains("40000")){
                response = JSONUtil.fail("error");
                response.put("retCode", MvtException.NO_FUND_ERROR.getErrCode());
            }
        }
        return response;
    }
    public  JSONObject putJson(String url, String requestBody) {
    	JSONObject response = null;
    	try {
    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.APPLICATION_JSON);
    		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    		
    		HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
    		
    		ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
//    		System.out.println(res.getStatusCodeValue());
//    		System.out.println(res.getBody());
    		
            response = JSONUtil.retSuccess("success");
    	} catch (Exception e) {
            log.error("putJson error:",e);
    		response = JSONUtil.fail("error");
    	}
    	return response;
    }
    
    
    public  JSONObject getJson(String url) {
        JSONObject response = null;
        try {
//            String res = oAuth2RestTemplate.getForObject(url, String.class);
//            System.out.println(res);
        	
        	ResponseEntity<String> res = restTemplate.getForEntity(url, String.class);
            String result = res.getBody();
            log.debug("vt get result == " + result);
            if (res.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotBlank(result)) {
            	if(result.startsWith("[")){
                	JSONArray arr = JSONArray.parseArray(result);
                	return JSONUtil.retSuccess(arr);
                }
            	response = JSON.parseObject(result);
            	response.put("retCode", 0);
            } else {
                response = JSONUtil.fail("error");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("getJson eror",e);
            response = JSONUtil.fail("system Exception");
        }
        return response;
    }
    
    //获取最近上次的统计日期
    public String getLastStatDate(){
    	String key = VTRedisHelper.getlastStatTradeKey();
    	String date = stringRedisHelper.get(key);
    	if(StringUtils.isBlank(date)){
    		if(isTradeDateTime()){
    			date = getPreTradeDate(1);
    		}else{
    			date = getPreTradeDate(2);
    		}
        	stringRedisHelper.set(key, date, 7200);
    	}
    	return date;
    }
    
    //获取最近上次的统计日期
    public StockFee getStockFee(){
    	String key = VTRedisHelper.REDIS_CACHE_KEY_PROFIX+"stock:trade:fee";
    	String value = stringRedisHelper.get(key);
    	
    	if(StringUtils.isBlank(value)){
    		StockFee stockFee = getStockFeeFromVT();
    		stringRedisHelper.set(key, JSONObject.toJSONString(stockFee), 600);
    		return stockFee;
    	}else{
    		return JSONObject.parseObject(value, StockFee.class);
    	}
    }
    
    public StockFee getStockFeeFromVT(){
		JSONObject jsonObject = getMatchRule();
    	Map<String,Object> jsonMap = (Map<String,Object>)jsonObject;  
    	Map<String,Object> items = (Map<String, Object>) jsonMap.get("items"); 	
    	StockFee stockFee = new StockFee();
    	if(items!=null){
    		if(items.containsKey("C_FEE")&&items.get("C_FEE")!=null){
    			stockFee.setCfee(Double.parseDouble(items.get("C_FEE").toString()));
    		}
    		if(items.containsKey("C_FEE_MIN")&&items.get("C_FEE_MIN")!=null){
    			stockFee.setCfeeMin(Double.parseDouble(items.get("C_FEE_MIN").toString()));
    		}
    		if(items.containsKey("T_FEE")&&items.get("T_FEE")!=null){
    			stockFee.setTfee(Double.parseDouble(items.get("T_FEE").toString()));
    		}
    		if(items.containsKey("T_FEE_MIN")&&items.get("T_FEE_MIN")!=null){
    			stockFee.setTfeeMin(Double.parseDouble(items.get("T_FEE_MIN").toString()));
    		}
    		if(items.containsKey("S_TAX")&&items.get("S_TAX")!=null){
    			stockFee.setStax(Double.parseDouble(items.get("C_FEE").toString()));
    		}
    		
    	}
    	return stockFee;
	}
    
    
    
    //交易日取上一个交易日统计信息
    
    //非交易日取上两个交易日的统计信息
    
    
    
    public  JSONObject deletJson(String url) {
    	JSONObject response = null;
    	try {
//            String res = oAuth2RestTemplate.getForObject(url, String.class);
//            System.out.println(res);
    		
    		ResponseEntity<String> res = restTemplate.getForEntity(url, String.class);
    		String result = res.getBody();
    		log.debug("vt get result == " + result);
    		if (res.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotBlank(result)) {
    			response = JSON.parseObject(result);
    		} else {
    			response = JSONUtil.fail("error");
    		}
    	} catch (Exception e) {
    		log.error("deletJson system Exception",e);
    		response = JSONUtil.fail("system Exception");
    	}
    	return response;
    }
    
    public static void main(String[] args) {
        //yc_200000  yc_200001
//		String uuid = "yc_200000";
//		String uuid = "yczq_testnl9";
//        String uuid = "win_599467";
//		String uuid = "win_662336";
        //创建用户
//		register("200002");
        //重置账户
//		reset("yc_200002");
        //统计用户数
//		getUserCount();

//		isTradeDate();
//		System.out.println(getLastTradeDate());
        //获取资金信息
//		getVtFundInfo(uuid);
        //下单
//		JSONObject req = new JSONObject();
//		req.put("accountId", VT_ACCOUNTID);
//		req.put("commission_amount", 100);
//		req.put("commission_price", 2.00);
//		req.put("commission_type","0"); //
//		req.put("stock_code", "000001");
//		req.put("stock_type", "0"); // 股票
//		req.put("uuid", uuid);
//		commissions(uuid, req.toJSONString());


        //撤单
//		JSONObject req = new JSONObject();
//		long commissionId=5337175L;
//		req.put("accountId", VT_ACCOUNTID);
//		req.put("commission_type",4); //
//		req.put("commissionId", commissionId);
//		req.put("uuid", uuid);
//		cancelCommission(uuid,commissionId,req.toJSONString());
//
        //购买限制
//		getRestraintBuyInfo(uuid,"000001");

        //卖出限制
//		getRestraintSellInfo(uuid, "000001");

        //撤单列表
//		cancelList(uuid, "commission_time-desc");

//		getLasterDayStat(uuid);

//		getCommissionTime("20181012");

        //委托列表
//		getCommissionsList(uuid, "20000101", "20190101", 1, 10);
//		getCommissionsList(uuid, null, null, 1, 10);

        //持仓列表
//		positionList(uuid);

//		List<String> scodes = ArrayUtils.getStockCodes(positionList(uuid).getJSONArray("items"), "stock_code");

//		System.out.println(JSONObject.toJSONString(HQUtil.queryHQ(scodes),true));

        //成交列表
//		getConcludes(uuid, "", null, 1, 10);

//        System.out.println(mid);
//        System.out.println(mid);
        
//        getDayStatLog("yczq_testnl9");

//		getDayStatLog(uuid,"20000101", "20190101");
    	
    }



}
