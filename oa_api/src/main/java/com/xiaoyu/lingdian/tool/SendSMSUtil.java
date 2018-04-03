package com.xiaoyu.lingdian.tool;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.xiaoyu.lingdian.tool.http.HttpUrlConnectionUtil;
import com.xiaoyu.lingdian.tool.init.ConfigIni;

public class SendSMSUtil {

	/**
	 * 注册发送短信
	 * 
	 * @param mobileTel
	 * @param code
	 * @return
	 */
	public static boolean sendRegister(String mobileTel, String code) {
		String content = "";
		try {
			content = URLEncoder.encode("您在微信平台的注册验证码："+code+",十分钟内有效.号外号外:即日起通过官方微信平台下单寄快递,立可享受首单寄件9折优惠!", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("注册发送短信验证信息编码失败！");
			e.printStackTrace();
		}
		String param = "loginname="+ConfigIni.getIniStrValue("SEND_SMS", "loginname", "")+
				"&password="+ConfigIni.getIniStrValue("SEND_SMS", "password", "")
				+"&mobile="+mobileTel+"&content="+content+"&extNo=";	
		String result = HttpUrlConnectionUtil.sendGet(
				ConfigIni.getIniStrValue("SEND_SMS", "path", ""), param);
		
		if (("").equals(result) || result.indexOf("smsid") == -1) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 忘记密码发送短信
	 * 
	 * @param mobileTel
	 * @param code
	 * @return
	 */
	public static boolean sendForget(String mobileTel, String code) {
		String content = "";
		try {
			content = URLEncoder.encode("您在微信平台的注册验证码："+code+",十分钟内有效.号外号外:即日起通过官方微信平台下单寄快递,立可享受首单寄件9折优惠!", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("忘记密码发送短信验证信息编码失败！");
			e.printStackTrace();
		}
		String param = "loginname="+ConfigIni.getIniStrValue("SEND_SMS", "loginname", "")+
				"&password="+ConfigIni.getIniStrValue("SEND_SMS", "password", "")
				+"&mobile="+mobileTel+"&content="+content+"&extNo=";
		String result = HttpUrlConnectionUtil.sendGet(
				ConfigIni.getIniStrValue("SEND_SMS", "path", ""), param);
		
		if (("").equals(result) || result.indexOf("smsid") == -1) {
			return false;
		}
		
		return true;
	}
	
}