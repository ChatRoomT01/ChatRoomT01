package com.tarena.xmpp.util;

import android.content.Context;
import android.widget.Toast;

public class Tools {
	
	/**
	 * 判断字符串是否为空
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
	 * 通过toast显示提示信息
	 * @param context
	 * @param msg
	 */
	public static void showInfo(Context context,String msg){
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}
