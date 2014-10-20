package com.tarena.xmpp.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
	
	/**
	 * 处理错误，将错误信息显示到控制台，并发送邮件
	 * @param e
	 */
	public static void handle(Exception e){
		StringWriter sw=new StringWriter();
		PrintWriter pw=new PrintWriter(sw);
		e.printStackTrace(pw);
		e.printStackTrace();
//		System.out.println(sw);//打印到控制台
		
		//保存错误信息到文件
		//发送邮件
	}
}
