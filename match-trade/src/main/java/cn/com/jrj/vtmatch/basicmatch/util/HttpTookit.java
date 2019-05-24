package cn.com.jrj.vtmatch.basicmatch.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 基于 httpclient 4.3.1版本的 http工具类
 */
@Slf4j
public class HttpTookit {


    private static final CloseableHttpClient HTTPCLIENT;
    private static final String CHARSET_GBK = "GBK";
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    public static final String HTTP_CLIENT_ERROR_STATUS_CODE = "HttpClient,error status code :";


    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(12000).build();
        HTTPCLIENT = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * 默认编码HTTP Get 请求
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, null, CHARSET_GBK);
    }

    /**
     * UTF_8编码HTTP Get 请求
     *
     * @param url
     * @return
     */
    public static String doGetStatis(String url) {
        return doGet(url, null, CHARSET_UTF_8);
    }

    /**
     * HTTP Get 请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, String> params) {
        return doGet(url, params, CHARSET_GBK);
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doGet(String url, Map<String, String> params, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, CHARSET_UTF_8));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = HTTPCLIENT.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException(HTTP_CLIENT_ERROR_STATUS_CODE + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
//			if (entity != null) {
//				result = EntityUtils.toString(entity, charset);
//			}
//			EntityUtils.consume(entity);

            StringBuilder sb = new StringBuilder();
            if (entity != null) {
                getStringByEntity(charset, entity, sb);
            } else {
                sb.append(response.getStatusLine().getStatusCode());
            }

            //httpGet.releaseConnection();
            result = sb.toString();
            return result;
        } catch (Exception e) {
            log.error("HTTP Get Exception:", e);
        }
        return null;
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url
     * @param data
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String doPost(String url, String data, boolean json) throws ClientProtocolException, IOException {
        return doPost(url, data, json, CHARSET_UTF_8);
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址
     * @param data    数据
     * @param charset 编码格式
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String doPost(String url, String data, boolean json, String charset) throws ClientProtocolException, IOException {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        if (json) {
            httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        }
        if (StringUtils.isNotEmpty(data)) {
            StringEntity strEntity = new StringEntity(data, charset);

            httpPost.setEntity(strEntity);
        }
        CloseableHttpResponse response = HTTPCLIENT.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpPost.abort();
            throw new RuntimeException(HTTP_CLIENT_ERROR_STATUS_CODE + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        StringBuilder sb = new StringBuilder();
        if (entity != null) {
            getStringByEntity(charset, entity, sb);
        } else {
            sb.append(response.getStatusLine().getStatusCode());
        }
        result = sb.toString();
        response.close();
        //httpPost.releaseConnection();
        return result;
    }

    /**
     * POST提交json数据
     *
     * @param url
     * @param jsonData
     * @param charset
     * @return 状态码 返回状态码，金牛接口专用
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Integer doPostJson(String url, String jsonData, String charset) throws ClientProtocolException, IOException {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        setEntity(jsonData, charset, httpPost);
        CloseableHttpResponse response = HTTPCLIENT.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
        log.debug("result === " + result);
        int statusCode = response.getStatusLine().getStatusCode();
        response.close();
        return statusCode;
    }

    private static void setEntity(String jsonData, String charset, HttpPost httpPost) {
        if (StringUtils.isNotEmpty(jsonData)) {
            StringEntity strEntity = new StringEntity(jsonData, charset);
            strEntity.setContentType(CONTENT_TYPE_TEXT_JSON);
            strEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            httpPost.setEntity(strEntity);
        }
    }

    /**
     * POST提交json数据
     *
     * @param url
     * @param jsonData
     * @param charset
     * @return 状态码 返回状态码 及返回的内容，金牛接口专用
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Map<String, Object> doPostJsonReturnMap(String url, String jsonData, String charset) throws ClientProtocolException, IOException {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        setEntity(jsonData, charset, httpPost);
        CloseableHttpResponse response = HTTPCLIENT.execute(httpPost);
        Integer statusCode = response.getStatusLine().getStatusCode();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("statusCode", statusCode);
        HttpEntity entity = response.getEntity();
        String context = null;
        StringBuilder sb = new StringBuilder();
        if (entity != null) {
            getStringByEntity(charset, entity, sb);
        }
        context = sb.toString();
        resultMap.put("context", context);
        response.close();
        //httpPost.releaseConnection();
        return resultMap;
    }

    /**
     * POST提交json数据
     *
     * @param url
     * @param jsonData
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Integer doPostJson(String url, String jsonData) throws ClientProtocolException, IOException {
        return doPostJson(url, jsonData, CHARSET_UTF_8);
    }

    /**
     * POST提交json数据
     *
     * @param url
     * @param jsonData
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Map<String, Object> doPostJsonReturnMap(String url, String jsonData) throws ClientProtocolException, IOException {
//    	return doPostJsonReturnMap(url,jsonData,CHARSET_UTF_8);
        return doPostJsonReturnMap(url, jsonData, "GBK");
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url    请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @return
     */
    public static String doPost(String url, Map<String, String> params) {
        return doPost(url, params, CHARSET_UTF_8);
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doPost(String url, Map<String, String> params, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if (pairs != null && !pairs.isEmpty()) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET_UTF_8));
            }
            CloseableHttpResponse response = HTTPCLIENT.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException(HTTP_CLIENT_ERROR_STATUS_CODE + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            StringBuilder sb = new StringBuilder();
            if (entity != null) {
                getStringByEntity(charset, entity, sb);
            } else {
                sb.append(response.getStatusLine().getStatusCode());
            }
            //httpPost.releaseConnection();
            result = sb.toString();
            return result;
        } catch (Exception e) {
            log.error("HTTP Post Exception:", e);
        }
        return null;
    }

    private static void getStringByEntity(String charset, HttpEntity entity, StringBuilder sb) throws IOException {
        try (InputStream instream = entity.getContent();
             BufferedReader br = new BufferedReader(new InputStreamReader(instream, charset))) {
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        }
    }

    /**
     * HTTP PUT
     *
     * @param url
     * @param data
     * @param charset
     * @return
     */
    public static String doPut(String url, String data, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            HttpPut httpPut = new HttpPut(url);
            if (StringUtils.isNotEmpty(data)) {
                StringEntity strEntity = new StringEntity(data, charset);
                httpPut.setEntity(strEntity);
            }
            CloseableHttpResponse response = HTTPCLIENT.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPut.abort();
                throw new RuntimeException(HTTP_CLIENT_ERROR_STATUS_CODE + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result;
            StringBuilder sb = new StringBuilder();
            if (entity != null) {
                getStringByEntity(charset, entity, sb);
            } else {
                sb.append(response.getStatusLine().getStatusCode());
            }
            //httpPut.releaseConnection();
            result = sb.toString();
            return result;
        } catch (Exception e) {
            log.error("HTTP Put Exception:", e);
        }
        return null;
    }

    /**
     * HTTP PUT  金牛专用 返回响应码
     *
     * @param url
     * @param jsonData
     * @param charset
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static Integer doPutJson(String url, String jsonData, String charset) throws ClientProtocolException, IOException {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        HttpPut httpPut = new HttpPut(url);
        httpPut.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        if (StringUtils.isNotEmpty(jsonData)) {
            StringEntity strEntity = new StringEntity(jsonData, charset);
//        	strEntity.setContentType(CONTENT_TYPE_TEXT_JSON);
//        	strEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            httpPut.setEntity(strEntity);
        }
        CloseableHttpResponse response = HTTPCLIENT.execute(httpPut);
        int statusCode = response.getStatusLine().getStatusCode();
        response.close();
        //httpPut.releaseConnection();
        return statusCode;
    }

    /**
     * HTTP PUT  金牛专用 返回响应码
     *
     * @param url
     * @param jsonData
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Integer doPutJson(String url, String jsonData) throws ClientProtocolException, IOException {
        return doPutJson(url, jsonData, CHARSET_UTF_8);
    }

    /**
     * HTTP DELETE 金牛专用 返回响应码
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Integer doDelete(String url) throws ClientProtocolException, IOException {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        HttpDelete httpDelete = new HttpDelete(url);
        CloseableHttpResponse response = HTTPCLIENT.execute(httpDelete);
        int statusCode = response.getStatusLine().getStatusCode();
        response.close();
        httpDelete.releaseConnection();
        return statusCode;
    }

    /**
     * HTTP DELETE 金牛专用 返回响应码及内容
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Map<String, Object> doDeleteReturnMap(String url) throws ClientProtocolException, IOException {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        HttpDelete httpDelete = new HttpDelete(url);
        CloseableHttpResponse response = HTTPCLIENT.execute(httpDelete);
        int statusCode = response.getStatusLine().getStatusCode();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("statusCode", statusCode);
        HttpEntity entity = response.getEntity();
        String context = null;
        StringBuilder sb = new StringBuilder();
        if (entity != null) {
            getStringByEntity(CHARSET_UTF_8, entity, sb);
        }
        context = sb.toString();
        resultMap.put("context", context);
        response.close();
        return resultMap;
    }


    public static void main(String[] args) throws ClientProtocolException, IOException {
//        String getData = doGet("http://www.baidu.com/");
//        System.out.println(getData);
//        System.out.println("----------------------分割线-----------------------");
//        String postData = doPost("http://www.baidu.com/","s",false);
//        System.out.println(postData);

        String uuid = "jrj_141107010049770731_17";

    }

}

