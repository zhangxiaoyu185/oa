package com.xiaoyu.lingdian.tool.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUrlConnectionUtil {

	protected final static Logger logger = LoggerFactory.getLogger("BASE_LOGGER");
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url 发送请求的URL
	 * @return URL 所代表远程资源的响应结果
	 */
	public static void sendGet(String url) {
		BufferedReader in = null;
		HttpURLConnection connection = null;
		try {
			URL realUrl = new URL(url);
			// URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection
			connection = (HttpURLConnection) realUrl.openConnection();
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
		} catch (Exception e) {
			logger.error("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			connection.disconnect();
		}
		return;
	}
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		HttpURLConnection connection = null;
		try {
			// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			logger.info(urlNameString);
			// URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection
			connection = (HttpURLConnection) realUrl.openConnection();
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("发送GET请求出现异常！" + e);
			e.printStackTrace();
			return "";
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			connection.disconnect();
		}
		logger.info(result);
		return result;
	}

	public static String sendPost(String url, Map<String, String> params) {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		String str = "";
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
			str = sb.substring(0, sb.length() - 1);
		}
		// 尝试发送请求
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			// // POST 只能为大写，严格限制，post会不识别
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(
					con.getOutputStream(), "UTF-8");
			logger.info("data:"+str);
			osw.write(str);
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			// 一定要有返回值，否则无法把请求发送给server端。
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
			logger.info("请求成功："+url);
			logger.info("result:" + buffer.toString());
		} catch (Exception e) {
			logger.error("请求失败：" + e);
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
}