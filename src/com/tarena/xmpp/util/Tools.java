package com.tarena.xmpp.util;

import android.content.Context;
import android.widget.Toast;

public class Tools {
	
	/**
	 * �ж��ַ����Ƿ�Ϊ��
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		if("".equals(str) || null==str ||" ".equals(str) || "null".equals(str)){
			return true;
		}
		return false;
	}
	
	/**
	 * ͨ��toast��ʾ��ʾ��Ϣ
	 * @param context
	 * @param msg
	 */
	public static void showInfo(Context context,String msg){
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}
