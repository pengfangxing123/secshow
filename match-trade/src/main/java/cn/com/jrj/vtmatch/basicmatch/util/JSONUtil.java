package cn.com.jrj.vtmatch.basicmatch.util;


import com.alibaba.fastjson.JSONObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <p>
 * JSON工具类
 * </p>
 *
 * @author xinsheng.dong
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JSONUtil {

    /**
     * 结果状态码 0:成功
     */
    private static final int RESULT_SUCCESS = 0;

    /**
     * 结果状态码 -1:失败
     */
    private static final int RESULT_FAILURE = -1;
    private static final String RET_CODE = "retCode";
    private static final String SUCCESS = "success";

    public static boolean isFail(JSONObject json) {
        return !(json != null && json.containsKey(RET_CODE) && RESULT_SUCCESS == json.getIntValue(RET_CODE)) ;
    }

    public static JSONObject fail(String message) {
        JSONObject ret = new JSONObject();
        ret.put(RET_CODE, RESULT_FAILURE);
        ret.put("msg", message);
        return ret;
    }
    
    public static JSONObject success() {
    	JSONObject ret = new JSONObject();
    	ret.put(RET_CODE, RESULT_SUCCESS);
    	ret.put("msg", "");
    	return ret;
    }

    public static JSONObject retSuccess(Object data) {
        JSONObject ret = new JSONObject();
        ret.put(RET_CODE, RESULT_SUCCESS);
        ret.put("msg", SUCCESS);
        if (data != null) {
            ret.put("data", data);
        }
        return ret;
    }

    public static JSONObject successList(Object data, int nextStart, int pageSize) {
        JSONObject ret = new JSONObject();
        ret.put(RET_CODE, RESULT_SUCCESS);
        ret.put("msg", SUCCESS);
        ret.put("data", data);
        ret.put("nextStart", nextStart);
        ret.put("pageSize", pageSize);
        return ret;
    }

}
