package com.tarena.xmpp.util;


public class TimeUtils {
	private long start,end;
	
	public TimeUtils(){
		start=System.currentTimeMillis();
	}
	
	/**
	 * 显示使用的时间
	 * @param tag
	 */
	public void getUseTime(String tag){
		end=System.currentTimeMillis();
		LogUtils.i(tag, (end=start));
	}

}
