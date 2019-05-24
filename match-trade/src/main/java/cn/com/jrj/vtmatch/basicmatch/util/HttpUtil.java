package cn.com.jrj.vtmatch.basicmatch.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtil {

    /**
     * 默认超时时间 3秒
     */
    private static final int DEFAULT_TIME_OUT = 3000;

    private static final int HTTPS_PROTOCOL_LENGTH = 5;

    private static final String GBK = "GBK";

    public static final String CHARSET_UTF_8 = "UTF-8";
    private static final String UTF_8 = CHARSET_UTF_8;
    public static final String HTTP_REQUEST_RETURN_ERROR_URL_STATUS_CODE = "http request return error... url:{}, statusCode:{}";
    public static final String ERROR_OCCURRED_URL_MESSAGE = "error occurred... url:{}, message:{}";

    /**
     * HTTP POST request api
     *
     * @param url      request URL
     * @param reqParam request parameter
     * @return response body(JSON)
     */
    public static JSONObject post(String url, Map<String, Object> reqParam) {
        return post(url, null, reqParam, DEFAULT_TIME_OUT);
    }

    /**
     * HTTP POST request api
     *
     * @param url      request URL
     * @param reqParam request parameter
     * @param timeout  millisecond
     * @return response body(JSON)
     */
    public static JSONObject post(String url, Map<String, Object> reqParam, int timeout) {
        return post(url, null, reqParam, timeout);
    }

    /**
     * HTTP POST request api
     *
     * @param url       request URL
     * @param reqHeader request header
     * @param reqParam  request parameter
     * @return response body(JSON)
     */
    public static JSONObject post(String url, Map<String, Object> reqHeader, Map<String, Object> reqParam,
                                  int timeout) {

        if (StringUtils.isEmpty(url)) {
            return null;
        }

        HttpPost httpPost = new HttpPost(url);

        // 设置请求的头部
        if (!MapUtils.isEmpty(reqHeader)) {
            reqHeader.keySet().forEach(headerKey -> httpPost.addHeader(headerKey, String.valueOf(reqHeader.get(headerKey))));
        }

        // 设置请求的参数
        if (!MapUtils.isEmpty(reqParam)) {
            List<NameValuePair> valuePairs = new LinkedList<>();
            Set<Map.Entry<String, Object>> entries = reqParam.entrySet();
            for (Map.Entry<String, Object> e : entries) {
                if (e == null) {
                    continue;
                }
                valuePairs.add(new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())));
            }
            UrlEncodedFormEntity entityPost = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);

            httpPost.setEntity(entityPost);
        }

        // 执行请求
        return executeHttpRequest(url, httpPost, timeout);
    }

    /**
     * POST提交JSON数据
     *
     * @param url    地址
     * @param params 参数
     * @return JSON
     * @author Faman.Wu
     * @date 2018-7-30
     */
    public static <T> JSONObject json(String url, T params) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            // 设置请求的参数
            httpPost.setEntity(new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON));
            // 执行请求
            return executeHttpRequest(url, httpPost, DEFAULT_TIME_OUT);
        } catch (Exception e) {
            log.error(url, e);
            return null;
        }

    }

    /**
     * HTTP GET request api
     *
     * @param url     request URL
     * @param timeout millisecond
     * @return response body(JSON)
     */
    public static JSONObject get(String url, int timeout) {
        return get(url, null, null, timeout, UTF_8);
    }

    /**
     * @param url       request URL
     * @param reqHeader request header
     * @param params    request params
     * @return response body(JSON)
     */
    public static JSONObject get(String url, Map<String, Object> reqHeader, Map<String, String> params) {
        return get(url, reqHeader, params, DEFAULT_TIME_OUT, UTF_8);
    }

    /**
     * @param url       request URL
     * @param reqHeader request header
     * @return response body(JSON)
     */
    public static JSONObject get(String url, Map<String, Object> reqHeader) {
        return get(url, reqHeader, null, DEFAULT_TIME_OUT, UTF_8);
    }

    /**
     * @param url       request URL
     * @param reqHeader request header
     * @return response body(JSON)
     */
    public static JSONObject get(String url, Map<String, Object> reqHeader, int timeout) {
        return get(url, reqHeader, null, timeout, UTF_8);
    }

    /**
     * HTTP GET request api
     *
     * @param url request URL
     * @return response body(JSON)
     */
    public static JSONObject get(String url) {
        return get(url, null, null, DEFAULT_TIME_OUT, UTF_8);
    }

    /**
     * HTTP GET request api
     *
     * @param url request URL
     * @return response body(JSON)
     */
    public static JSONObject get(String url, String charset) {
        return get(url, null, null, DEFAULT_TIME_OUT, charset);
    }

    /**
     * HTTP GET request api by async
     *
     * @param url request URL
     * @return response body(JSON)
     */
    @Async
    public static JSONObject asyncGet(String url) {
        return get(url, null, null, DEFAULT_TIME_OUT, "UTF_8");
    }

    /**
     * HTTP GET request api
     *
     * @param url       request URL
     * @param reqHeader request header
     * @param params    request params
     * @return response body(JSON)
     */
    public static JSONObject get(String url, Map<String, Object> reqHeader, Map<String, String> params, int timeout, String responseCharset) {

        if (StringUtils.isEmpty(url)) {
            return null;
        }

        // 设置请求参数
        if (MapUtils.isNotEmpty(params)) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
            try {
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, CHARSET_UTF_8));
            } catch (ParseException | IOException e) {
                log.error(url, e);
            }
        }

        HttpGet httpGet = new HttpGet(url);
//        httpGet.addHeader("Content-Type", "application/json;charset=GBK");
        // 设置请求的头部
        if (!MapUtils.isEmpty(reqHeader)) {
            reqHeader.keySet().forEach(headerKey -> httpGet.addHeader(headerKey, String.valueOf(reqHeader.get(headerKey))));
        }


        // 执行请求
        return executeHttpRequest(url, httpGet, timeout, responseCharset);
    }

    /**
     * HTTP GET request api
     *
     * @param url request URL
     * @return response body(String)
     */
    public static String httpGet(String url) {

        if (StringUtils.isEmpty(url)) {
            return null;
        }

        HttpGet httpGet = new HttpGet(url);

        // 执行请求
        HttpResponse response;
        try {
            HttpClient httpClient = buildHttpClient(url);
            httpGet.setConfig(buildRequestConfig(DEFAULT_TIME_OUT));

            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            } else {
                log.error(HTTP_REQUEST_RETURN_ERROR_URL_STATUS_CODE, url,
                        response.getStatusLine().getStatusCode());
                return null;
            }

        } catch (Exception e) {
            log.error(ERROR_OCCURRED_URL_MESSAGE, url, e.getMessage());
        }

        return null;
    }

    /**
     * HTTP GET request api
     *
     * @param url request URL
     * @return response body(String)
     */
    public static byte[] getProtobuf(String url) {
        if (StringUtils.isEmpty(url)) {
            return new byte[0];
        }

        // 执行请求
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response;
        try {
            HttpClient httpClient = buildHttpClient(url);

            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toByteArray(response.getEntity());
            } else {
                log.error(HTTP_REQUEST_RETURN_ERROR_URL_STATUS_CODE, url,
                        response.getStatusLine().getStatusCode());
                return new byte[0];
            }
        } catch (Exception e) {
            log.error(ERROR_OCCURRED_URL_MESSAGE, url, e.getMessage());
        }

        return new byte[0];
    }

    private static HttpClient buildHttpClient(String url)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        if (StringUtils.length(url) < HTTPS_PROTOCOL_LENGTH) {
            return HttpClientBuilder.create().build();
        }

        String protocol = url.substring(0, HTTPS_PROTOCOL_LENGTH);

        if ("https".equalsIgnoreCase(protocol)) {
            return HttpClients.custom()
                    .setSSLSocketFactory(new SSLConnectionSocketFactory(
                            SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build()))
                    .build();
        }

        return HttpClientBuilder.create().build();
    }

    private static RequestConfig buildRequestConfig(int timeout) {
        return RequestConfig.custom().setConnectionRequestTimeout(timeout).setConnectTimeout(timeout)
                .setSocketTimeout(timeout).build();
    }

    private static JSONObject executeHttpRequest(String url, HttpRequestBase request, int timeout) {
        return executeHttpRequest(url, request, timeout, "utf-8");
    }

    private static JSONObject executeHttpRequest(String url, HttpRequestBase request, int timeout, String charset) {
        try {
            HttpClient httpClient = buildHttpClient(url);
            request.setConfig(buildRequestConfig(timeout));

            return convertResponseBody2Json(url, httpClient.execute(request), charset);
        } catch (Exception e) {
            log.error(ERROR_OCCURRED_URL_MESSAGE, url, e.getMessage());
        }

        return null;
    }

    private static JSONObject convertResponseBody2Json(String url, HttpResponse response, String charset) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream instream = response.getEntity().getContent();
            getStringByEntityContent(charset, sb, instream);
            if(sb.toString().startsWith("[")){
            	JSONArray arr = JSONArray.parseArray(sb.toString());
            	return JSONUtil.retSuccess(arr);
            }
            return JSON.parseObject(sb.toString());
//            return JSON.parseObject(EntityUtils.toString(response.getEntity(), "gbk"));
        } else {
            log.error(HTTP_REQUEST_RETURN_ERROR_URL_STATUS_CODE, url,
                    response.getStatusLine().getStatusCode());
            return null;
        }
    }

    private static void getStringByEntityContent(String charset, StringBuilder sb, InputStream instream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(instream, charset))) {
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        }
    }

    public static JSONObject postJson(String url, JSON json, Map<String, Object> reqHeader) {
        return postJson(url, json.toJSONString(), reqHeader);
    }

    public static JSONObject postJson(String url, String requestBody, Map<String, Object> reqHeader) {
        CloseableHttpClient client = HttpClientBuilder.create().useSystemProperties().build();
        HttpPost post = new HttpPost(url);

        // 设置请求的头部
        if (!MapUtils.isEmpty(reqHeader)) {
            reqHeader.keySet().forEach(headerKey -> post.addHeader(headerKey, String.valueOf(reqHeader.get(headerKey))));
        }

        JSONObject response;
        try {

            StringEntity s = new StringEntity(requestBody, CHARSET_UTF_8);
            s.setContentEncoding(CHARSET_UTF_8);
            //发送json数据需要设置contentType
            s.setContentType("application/json");
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 返回json格式：
                String result = EntityUtils.toString(res.getEntity());
                log.debug("http res === {}", result);
                response = JSONObject.parseObject(result);
            } else if (res.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                String result = EntityUtils.toString(res.getEntity(), CHARSET_UTF_8);
                log.debug("result === " + result);
                response = JSONUtil.retSuccess("success");
            } else {
                response = JSONUtil.fail("error");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static JSONObject deleteJson(String url, Map<String, Object> reqHeader) {
        CloseableHttpClient client = HttpClientBuilder.create().useSystemProperties().build();
        HttpDelete delete = new HttpDelete(url);

        // 设置请求的头部
        if (!MapUtils.isEmpty(reqHeader)) {
            reqHeader.keySet().forEach(headerKey -> delete.addHeader(headerKey, String.valueOf(reqHeader.get(headerKey))));
        }

        JSONObject response = null;
        try {
            HttpResponse res = client.execute(delete);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 返回json格式：
                String result = EntityUtils.toString(res.getEntity());
                response = JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static JSONObject putJson(String url, String requestBody, Map<String, Object> reqHeader) {
        CloseableHttpClient client = HttpClientBuilder.create().useSystemProperties().build();
        HttpPut put = new HttpPut(url);

        // 设置请求的头部
        if (!MapUtils.isEmpty(reqHeader)) {
            reqHeader.keySet().forEach(headerKey -> put.addHeader(headerKey, String.valueOf(reqHeader.get(headerKey))));
        }

        JSONObject response;
        try {
            StringEntity s = new StringEntity(requestBody, CHARSET_UTF_8);
            s.setContentEncoding(CHARSET_UTF_8);
            //发送json数据需要设置contentType
            s.setContentType("application/json");
            put.setEntity(s);
            HttpResponse res = client.execute(put);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 返回json格式：
                String result = EntityUtils.toString(res.getEntity());
                response = JSONObject.parseObject(result);
            } else if (res.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                response = JSONUtil.retSuccess("success");
            } else {
                response = JSONUtil.fail("error");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }


    public static String readUrl(String url, String responseCharset) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        HttpGet httpGet = new HttpGet(url);

        try {

            HttpClient httpClient = buildHttpClient(url);
            httpGet.setConfig(buildRequestConfig(5000));


            StringBuilder sb = new StringBuilder();
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream instream = response.getEntity().getContent();
                getStringByEntityContent(responseCharset, sb, instream);
                return sb.toString();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }
    
    public static void main(String[] args) {
		postJson("http://match-midplat.test/trade/concludeBack", "{aaa:json字符串}", null);
	}
}
