package com.tarena.xmpp.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
	
	/**
	 * ������󣬽�������Ϣ��ʾ������̨���������ʼ�
	 * @param e
	 */
	public static void handle(Exception e){
		StringWriter sw=new StringWriter();
		PrintWriter pw=new PrintWriter(sw);
		e.printStackTrace(pw);
		e.printStackTrace();
//		System.out.println(sw);//��ӡ������̨
		
		//���������Ϣ���ļ�
		//�����ʼ�
	}
}
