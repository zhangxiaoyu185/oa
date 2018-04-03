package com.xiaoyu.lingdian.tool;

import javax.mail.MessagingException;
import com.xiaoyu.lingdian.tool.MailUtil;

public class ExceptionTools {
	
	/**
	 * 获取异常发生邮件进行通知
	 * @param e
	 */
	public static void getExceptionDetail(Exception e, String details) {
		StringBuffer sb = new StringBuffer(details + "<br />" + e.getMessage()
				+ "<br />");
		StackTraceElement[] elements = e.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			sb.append("at " + elements[i].getClassName() + " "
					+ elements[i].getMethodName() + "(<font color='blue'>"
					+ elements[i].getFileName() + ":"
					+ elements[i].getLineNumber() + "</font>)" + "<br />");
		}
		// 发送邮件
		try {
			MailUtil.sendMail(sb.toString());
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
	}

}