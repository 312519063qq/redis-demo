package com.gsj.rediswatch.utils.common;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author zhangjiguo
 * @version V1.0
 * @Description: 调用接口工具类
 * @date 2017-9-17 19:43
 */
public class HttpClientUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private static CloseableHttpClient createHttpClient(){
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setSocketTimeout(5000)
				.setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000)
				.build();

		// CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(defaultRequestConfig)
				.build();
		return httpClient;
	}
	
	public static String sendHttpGetRequest(String url) throws IOException {
		return sendHttpGetRequest(url, null);
	}

	public static String sendHttpGetRequest(String url, Map<String, Object> header) throws IOException {
		logger.debug("调用HttpGet :url={}", url);

		HttpGet httpGet = new HttpGet(url);
		if(header != null){
			for (Map.Entry<String, Object> entry : header.entrySet()) {
				httpGet.setHeader(entry.getKey(), entry.getValue().toString());
			}
		}

		return sendRequest(url, null, httpGet);
	}

	public static String sendHttpPostRequest(String url, Map<String, Object> map) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		// 设置参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if(entry.getValue() == null){
				throw new IllegalArgumentException("调用HttpPost :url= " + url + "；发送POST请求参数数据为空 ： " + entry.getKey());
			}
			list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
		}

		logger.debug("调用HttpPost :url={}；参数={}", httpPost.getURI(), map.toString());

		if (list.size() > 0) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			httpPost.setEntity(entity);
		}
		return sendRequest(url, httpPost, null);
	}

	public static String sendHttpPostRequest(String url, String json) throws IOException {
		logger.debug("调用HttpPost :url={}；参数={}", url, json);

		HttpPost httpPost = new HttpPost(url);
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes("UTF-8"));
		InputStreamEntity inputEntry = new InputStreamEntity(is);
		httpPost.setEntity(inputEntry);

		return sendRequest(url, httpPost, null);
	}

	private static String sendRequest(String url, HttpPost httpPost, HttpGet httpGet) throws IOException {
		CloseableHttpClient httpClient = null;
		try {
			httpClient = HttpClientUtil.createHttpClient();

			// 执行get请求.
			CloseableHttpResponse response = null;
			for(int i = 1; i < 4; i++){
				try{
					if(httpPost != null){
						response = httpClient.execute(httpPost);
					}else if(httpGet != null){
						response = httpClient.execute(httpGet);
					}
					break;
					// } catch (ConnectTimeoutException e) {
					// 	e.printStackTrace();
					// } catch (SocketTimeoutException e) {
					// 	e.printStackTrace();
					// } catch (SocketException e){
					// 	e.printStackTrace();
				} catch (Exception e){
					logger.error("连接失败，重试第[ {} ] 次，url = {}；error = {}", i, url, e.getMessage(), e);
				}
			}

			if(response == null){
				return null;
			}

			try {
				if(response.getStatusLine().getStatusCode() != 200){
					throw new HttpResponseException(response.getStatusLine().getStatusCode(), "调用URL：" + url + "失败：Unexpected response status: " + response.getStatusLine().getStatusCode());
				}
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					String resJson = EntityUtils.toString(resEntity, "UTF-8");
					logger.debug("调用Http返回 :url={}；json={}", url, resJson);
					return resJson;
				}
			} finally {
				try {
					if(response != null){
						response.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("response.close() 失败！error={}", e.getMessage(), e);
				}
			}
		} finally {
			// 关闭连接,释放资源
			try {
				if(httpClient != null){
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("httpClient.close() 失败！error={}", e.getMessage(), e);
			}
		}
		return null;
	}
}