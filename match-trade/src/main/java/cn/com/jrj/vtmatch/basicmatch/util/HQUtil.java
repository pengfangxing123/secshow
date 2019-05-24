package cn.com.jrj.vtmatch.basicmatch.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 行情接口
 *
 * @author yuanlong.wang 2013-1-17
 */
@Slf4j
public class HQUtil {
    // private static final String HQ_URL="http://q.jrjc.local:82/?otc=utf-8";
//	private static final String HQ_SERVER = "http://q.jrjc.local:82";
    private static final String HQ_SERVER = "https://q.jrjimg.cn/?otc=utf-8";
    private static final String HQ_SERVER_S = "https://q.jrjimg.cn/?otc=utf-8/?otc=utf-8&q=cn%7cs&n=stock&c=id,code,name,pl,np,hlp,lcp&i=";
    private static final String HQ_SERVER_I = "https://q.jrjimg.cn/?otc=utf-8/?otc=utf-8&q=cn%7ci&n=index&c=id,code,name,pl,np,hlp,lcp&i=";
    private static final String HQ_SERVER_F = "https://q.jrjimg.cn/?otc=utf-8/?otc=utf-8&q=cn%7cf&n=stock&c=id,code,name,pl,np,hlp,lcp&i=";
    private static final String HQ_URL = HQ_SERVER + "/?otc=utf-8";
    /**
     * | 的utf-8
     */
    private static final String SPLIT = "%7c";
    private static final String SCRIPTVAR = "stock";
    public static final String COLUMN = "Column";
    public static final String HQ_DATA = "HqData";

    /**
     * 查询行情 详见：http://wiki.jrj.com.cn/pages/viewpage.action?pageId=24806647
     *
     * @param i (id) : 代码集合,如果有此字段，则q字段中只有市场和证券类型有效,过滤条件被忽略,多个证券代码用,分割
     * @param q (query) : 查询id的范围,根据此参数找出对应的过滤条件进行查询,是由 市场|证券类型|过滤条件组合而成
     * @param p (page) : 分页信息,后3位表示一页显示的记录数(即一页允许显示的最多记录数),其他的表示页码
     * @param o (order) : 排序信息(a顺序，d降序),排序的字段名称和现实的列名称相同，
     * @param n (name) : 返回js对象的名称,默认为obj0
     * @param c (column) : 返回列,系统定义了,短,中,长3中格式,支持用户传入查询的字段,具体的字段参见各个证券类型中的定义
     * @param y (数据类型) : 默认为 hq
     * @return
     */
//	public static String queryHQ(String i, String q, String p, String o, String n,
//			String c, String y) {
//		StringBuffer sb = new StringBuffer();
//		sb.append(HQ_URL);
//
//		if (q != null) {
//			sb.append("&q=").append(q);
//		}
//		if (p != null) {
//			sb.append("&p=").append(p);
//		}
//		if (o != null) {
//			sb.append("&o=").append(o);
//		}
//		if (n != null) {
//			sb.append("&n=").append(n);
//		}
//		if (c != null) {
//			sb.append("&c=").append(c);
//		}
//		if (y != null) {
//			sb.append("&y=").append(y);
//		}
//		if (i != null) {
//			sb.append("&i=").append(i);
//		}
////		Log.info(sb.toString());
//		System.out.println(sb.toString());
//		return HttpUtil.readUrl(sb.toString(), "GBK");
//	}
    public static String queryHQ(String i, String q, String p, String o, String n,
                                 String c, String y) {
        log.info(HQ_SERVER_S + i);
        return HttpUtil.readUrl(HQ_SERVER_S + i, "GBK");
    }

    private final static String NORMAL_C = "id,code,name,pl,np,hlp,lcp";

    /**
     * 查询股票的行情
     *
     * @param stocks
     * @return
     */
    private static JSONObject queryHQ(String[] stocks, StockType type) {
        String hq = null;
        switch (type) {
            case STOCK:
                hq = queryHQ(ArrayUtils.join(stocks), "cn" + SPLIT + "s", null,
                        null, SCRIPTVAR, NORMAL_C, null);
                // http://q.jrjimg.cn/?otc=utf-8&q=cn|s&n=winstock_hqs1358428333473&c=id,code,name,pl,np,hlp,lcp&i=002144,002424,600229,600857,002528
                break;
            case FUND:
//                hq = queryHQ(ArrayUtils.join(stocks), "cn" + SPLIT + "f", null,
//                        null, SCRIPTVAR, NORMAL_C, null);
                hq = HttpUtil.readUrl(HQ_SERVER_F + ArrayUtils.join(stocks), "GBK");

                // http://q.jrjimg.cn/?otc=utf-8&q=cn|f&n=winstock_hqf1358428333473&c=id,code,name,pl,np,hlp,lcp&i=002144,002424,600229,600857,002528
                break;
            case INDEX:
                hq = HttpUtil.readUrl(HQ_SERVER_I + ArrayUtils.join(stocks), "GBK");
                break;
            default:
                break;
        }
        System.out.println(hq);
        return dealHQStr(hq);
    }

    private static JSONObject dealHQStr(String hq) {
        if (hq == null) {
            return null;
        }
        hq = hq.replaceAll("[\r\n]", "").replaceAll(";", "")
                .replaceAll("var " + SCRIPTVAR + "=", "")
                .replaceAll("var index=", "");

        return JSONObject.parseObject(hq);
    }

    /**
     * 查询股票行情
     *
     * @param stockList
     * @return
     */
    public static Map<String, StockSummary> queryHQ(List<String> stockList) {
    	List<String> slist = new ArrayList<String>();
    	
    	List<String> flist = new ArrayList<String>();
    	
    	for(int i = 0; i < stockList.size(); i++){
    		String code = stockList.get(i);
    		if(code.startsWith("0") || code.startsWith("6") || code.startsWith("3")){
        		slist.add(code);
        	}else{
        		flist.add(code);
        	}
    	}
    	
    	Map<String, StockSummary> map = new HashMap<String, StockSummary>();
    	if(slist.size() > 0){
    		map.putAll(queryHQ(1,slist.toArray(new String[slist.size()])));
    	}
    	if(flist.size() > 0){
    		map.putAll(queryHQ(2,flist.toArray(new String[flist.size()])));
    	}
    	
        return map;
    }

    /**
     * 查询股票行情
     * 
     * @param type  1 股票  2.基金
     * @param stocks 代码数组
     * @return
     */
    public static Map<String, StockSummary> queryHQ(int type,String... stocks) {
    	
        Map<String, StockSummary> map = new HashMap<String, StockSummary>();
        // 最多查询50条
        if (stocks.length > 100 || stocks.length < 1) {
            return null;
        }
        StockType stype = null;
        if(type == 1){
        	stype = StockType.STOCK;
        }else{
        	stype = StockType.FUND;
        }
        try {
//			String[] stocks = stockList.toArray(new String[stockList.size()]);
            // 股票行情
            JSONObject stockObj = queryHQ(stocks, stype);
            if (stockObj != null) {
                JSONObject column = stockObj.getJSONObject(COLUMN);
                JSONArray stockHqData = stockObj.getJSONArray(HQ_DATA);
                buildSummary(map, column, stockHqData, StockType.STOCK);
            }
        } catch (Exception e) {
            log.error("查询股票行情异常:", e);
        }
        return map;
    }


    /**
     * 查询股票行情
     *
     * @param stocks
     * @return
     */
    public static Map<String, StockSummary> queryIndexHQ(String... stocks) {
        Map<String, StockSummary> map = new HashMap<String, StockSummary>();
        // 最多查询50条
        if (stocks.length > 50 || stocks.length < 1) {
            return null;
        }
        try {
//			String[] stocks = stockList.toArray(new String[stockList.size()]);
            // 股票行情
            JSONObject stockObj = queryHQ(stocks, StockType.INDEX);
            if (stockObj != null) {
                JSONObject column = stockObj.getJSONObject(COLUMN);
                JSONArray stockHqData = stockObj.getJSONArray(HQ_DATA);
                buildSummary(map, column, stockHqData, StockType.STOCK);
            }
        } catch (Exception e) {
            log.error("查询股票行情异常:", e);
        }
        return map;
    }


    /**
     * 单条股票基金行情查询
     *
     * @param stockCode
     * @return
     */
    public static StockSummary queryHQ(String stockCode) {
        try {
        	StockType type = null;
        	if(stockCode.startsWith("0") || stockCode.startsWith("6") || stockCode.startsWith("3")){
        		type = StockType.STOCK;
        	}else{
        		type = StockType.FUND;
        	}
            String[] stocks = new String[]{stockCode};
            // 股票行情
            JSONObject stockObj = queryHQ(stocks, type);
            if (stockObj != null) {
                JSONObject column = stockObj.getJSONObject(COLUMN);
                JSONArray stockHqData = stockObj.getJSONArray(HQ_DATA);
                return stockHqData == null ? null : toStock(stockHqData.getJSONArray(0), column);
            }
        } catch (Exception e) {
            log.error("单条股票基金行情查询异常:", e);
        }
        return null;
    }

    /**
     * 单条股票基金行情查询
     *
     * @param stockCode
     * @return
     */
    public static StockSummary queryHQIndex(String stockCode) {
        try {
            if (StringUtils.isBlank(stockCode)) {
                return null;
            }
            String[] stocks = new String[]{stockCode};
            // 股票行情
            JSONObject stockObj = queryHQ(stocks, StockType.INDEX);
            if (stockObj != null) {
                JSONObject column = stockObj.getJSONObject(COLUMN);
                JSONArray stockHqData = stockObj.getJSONArray(HQ_DATA);
                return toStock(stockHqData.getJSONArray(0), column);
            }
        } catch (Exception e) {
            log.error("单条股票基金行情查询异常:", e);
        }
        return null;
    }

    /**
     * 构建行情
     *
     * @param map
     * @param column
     * @param stockHqData
     * @param type
     */
    private static void buildSummary(Map<String, StockSummary> map, JSONObject column,
                                     JSONArray stockHqData, StockType type) {
        for (int i = 0; i < stockHqData.size(); i++) {
            JSONArray arr = stockHqData.getJSONArray(i);
            StockSummary obj = toStock(arr, column);
            if (obj != null) {
                map.put(obj.getCode(), obj);
            }
        }
    }

    private static StockSummary toStock(JSONArray arr, JSONObject column) {
        try {
            String id = arr.getString(column.getIntValue("id"));
            String code = arr.getString(column.getIntValue("code"));
            String name = arr.getString(column.getIntValue("name"));
            BigDecimal lcp = arr.getBigDecimal(column.getIntValue("lcp"));
            BigDecimal np = arr.getBigDecimal(column.getIntValue("np"));
            np = BigDecimal.ZERO.compareTo(np) == 0 ? lcp : np;// 如果当前价为0则取昨收价
            BigDecimal hlp = arr.getBigDecimal(column.getIntValue("hlp"));
            BigDecimal pl = arr.getBigDecimal(column.getIntValue("pl"));
            return new StockSummary(id, code, name, lcp, np, hlp, pl);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("", e);
            return null;
        }
    }

    /**
     * 股票类型
     *
     * @author yuanlong.wang 2013-1-17
     */
    public enum StockType {
        STOCK("s", "0"), FUND("f", "19"), INDEX("i", "1");

        public final String type;
        public final String code;

        StockType(String type, String code) {
            this.type = type;
            this.code = code;
        }

    }

    public static void main(String[] args) {
//		log.info(HttpUtil.readUrl(HQ_SERVER_I+ArrayUtils.join("000001,399001".split(",")), "GBK"));
//		log.info(new StringBuffer("ssssss.").substring(0, 6));
//		log.info(DateUtils.formatAll(new Date()));
//        log.info(JSONObject.toJSONString(queryHQ("002694", "000001"), true));
        List<String> list = new ArrayList<String>();
        list.add("000001");
        list.add("512510");
        list.add("159949");
        list.add("002694");
        log.info(JSONObject.toJSONString(queryHQ(list), true));
    }

}
