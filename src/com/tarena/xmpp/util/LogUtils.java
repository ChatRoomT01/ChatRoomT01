package com.tarena.xmpp.util;

import com.tarena.xmpp.model.TApplication;

import android.util.Log;

public class LogUtils {
	
	/**
	 * 输出日志信息
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag,String msg){
		if(!TApplication.isReleased){
			Log.i(tag, msg);
		}
	}
	public static void i(String tag,int msg){
		if(!TApplication.isReleased){
		Log.i(tag, String.valueOf(msg));
		}
	}
	public static void i(String tag,long msg){
		if(!TApplication.isReleased){
			Log.i(tag, String.valueOf(msg));
		}
	}
	public static void i(String tag,boolean msg){
		if(!TApplication.isReleased){
			Log.i(tag, String.valueOf(msg));
		}
	}
	public static void i(String tag,double msg){
		if(!TApplication.isReleased){
			Log.i(tag, String.valueOf(msg));
		}
	}
	public static void i(String tag,short msg){
		if(!TApplication.isReleased){
			Log.i(tag, String.valueOf(msg));
		}
	}
	
	
}
