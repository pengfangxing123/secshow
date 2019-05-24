/*
 * Copyright (c) 2018 JRJ, Inc.
 * 中国金融在线
 * http://www.jrj.com.cn/
 * All rights reserved.
 */

package cn.com.jrj.vtmatch.basicmatch.matchcore.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回对象
 *
 * @author NL
 * @date 2018-09-26 17:18:08
 */
@Data
public class JsonResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 0,成功;其他为失败
     */
    private static final int RETCODESUCCESS = 0;
    /**
     * -1,默认失败
     */
    private static final int RETCODEFAIL = -1;
    /**
     * -999,需要重新登录
     */
    public static final int NEED_LOGIN_RETCODE = -999;
    /**
     * 默认为成功
     */
    private int retCode = RETCODESUCCESS;
    private String retMsg = "";
    private String token = "";
    private Object data;//NOSONAR

    /**
     * 封装一个正常的返回对象.
     *
     * @param resultData result object
     * @return {@link JsonResult}
     */
    public static JsonResult buildSuccess(Object resultData) {
        JsonResult result = new JsonResult();
        result.setRetCode(RETCODESUCCESS);
        result.setData(resultData);
        return result;
    }

    /**
     * 封装一个正常的返回对象.
     *
     * @param resultData result object
     * @return {@link JsonResult}
     */
    public static JsonResult buildSuccess(Object resultData,String token) {
        JsonResult result = new JsonResult();
        result.setRetCode(RETCODESUCCESS);
        result.setToken(token);
        result.setData(resultData);
        return result;
    }

    /**
     * 封装返回一个指定的对象.
     *
     * @param retCode int value of return code
     * @param msg     String value of message
     * @return {@link JsonResult}
     */
    private static JsonResult build(int retCode, String msg) {
        JsonResult result = new JsonResult();
        result.setRetCode(retCode);
        result.setRetMsg(msg);
        return result;
    }

    /**
     * 封装返回一个指定需要登录的对象.
     *
     * @param msg     String value of message
     * @return {@link JsonResult}
     */
    public static JsonResult buildNeedLoginFail(String msg) {
        JsonResult result = new JsonResult();
        result.setRetCode(NEED_LOGIN_RETCODE);
        result.setRetMsg(msg);
        return result;
    }

    /**
     * 封装返回一个指定的对象.
     *
     * @param msg     String value of message
     * @return {@link JsonResult}
     */
    public static JsonResult buildFail(String msg) {
        JsonResult result = new JsonResult();
        result.setRetCode(RETCODEFAIL);
        result.setRetMsg(msg);
        return result;
    }

    /**
     * 封装一个正常的返回对象.
     *
     * @param resultData result object
     * @return {@link JsonResult}
     */
    public static JsonResult build(Object resultData) {
        JsonResult result = new JsonResult();
        result.setData(resultData);
        return result;
    }

    /**
     * 封装返回一个成功的对象.
     */
    public static JsonResult buildSuccess() {
        return JsonResult.build(RETCODESUCCESS, "successful");
    }

    /**
     * 封装返回一个成功的对象.
     */
    public static boolean isSuccess(JsonResult result) {
        return result.retCode == RETCODESUCCESS;
    }



}

