package com.rds.platform.msg;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.log4j.Logger;

import com.rds.platform.utils.CacheUtils;

/***
 * 创瑞短信发送平台
 * 
 * @author Administrator
 *
 */
public class CRMessageUtils {
//	private static Logger logger = Logger.getLogger("com.rds.platform.msg.CRMessageUtils");

	public static boolean sendMessage(String content, String mobile) throws IOException {

		CacheUtils cacheutil = CacheUtils.getInstace();
		Map<String, String> cache = cacheutil.getCache();
		String sign = cache.get("sign");
		String api = cache.get("api");
		String name = cache.get("name");
		String pwd = cache.get("pwd");
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer(api);

		// 向StringBuffer追加用户名
		sb.append("name=" + name);

		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd=" + pwd);

		// 向StringBuffer追加手机号码
		sb.append("&mobile=" + mobile);

		// 向StringBuffer追加消息内容转URL标准码
		sb.append("&content=" + URLEncoder.encode(content, "UTF-8"));

		// 追加发送时间，可为空，为空为及时发送
		sb.append("&stime=");

		// 加签名
		sb.append("&sign=" + URLEncoder.encode(sign, "UTF-8"));

		// type为固定值pt extno为扩展码，必须为数字 可为空
		sb.append("&type=pt&extno=");
		// 创建url对象
		// String temp = new String(sb.toString().getBytes("GBK"),"UTF-8");
//		logger.info("sb:" + sb.toString());
		URL url = new URL(sb.toString());

		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置url请求方式 ‘get’ 或者 ‘post’
		connection.setRequestMethod("POST");

		// 发送
		InputStream is = url.openStream();

		// 转换返回值
		String returnStr = convertStreamToString(is);

		// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功 具体见说明文档
		System.out.println(returnStr);
		return returnStr.contains("提交成功");
		// 返回发送结果
	}

	public static String getFeedBack() throws IOException {

		CacheUtils cacheutil = CacheUtils.getInstace();
		Map<String, String> cache = cacheutil.getCache();
		String feedbackapi = cache.get("feedbackapi");
		String name = cache.get("name");
		String pwd = cache.get("pwd");
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer(feedbackapi);

		// 向StringBuffer追加用户名
		sb.append("name=" + name);

		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd=" + pwd);
		sb.append("&type=mo");
		// 创建url对象
		// String temp = new String(sb.toString().getBytes("GBK"),"UTF-8");
//		logger.info("sb:" + sb.toString());
		URL url = new URL(sb.toString());

		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置url请求方式 ‘get’ 或者 ‘post’
		connection.setRequestMethod("POST");

		// 发送
		InputStream is = url.openStream();

		// 转换返回值
		String returnStr = convertStreamToString(is);

		// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功 具体见说明文档
		return returnStr;

	}

	/**
	 * 转换返回值类型为UTF-8格式.
	 * 
	 * @param is
	 * @return
	 */
	private static String convertStreamToString(InputStream is) {
		StringBuilder sb1 = new StringBuilder();
		byte[] bytes = new byte[4096];
		int size = 0;

		try {
			while ((size = is.read(bytes)) > 0) {
				String str = new String(bytes, 0, size, "UTF-8");
				sb1.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb1.toString();
	}
}
