package com.tarena.xmpp.receiver;

import com.tarena.xmpp.util.LogUtils;
import com.tarena.xmpp.util.Tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络状态改变广播接收器
 * @author chencaimei
 *
 */
public class NetworkChangedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager manager=(ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo=manager.getActiveNetworkInfo();
		
		if(activeNetworkInfo==null){//网络断开，需要给用户提示
			LogUtils.i("netstate", "网络断开");
			Tools.showInfo(context, "网络中断");
		}else{
			//网络打开，需要重新连接，重新登录
			NetworkInfo wifiInfo=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileNetworkInfo=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if(wifiInfo!=null){
				LogUtils.i("netstate", "wifi打开");
				Tools.showInfo(context, "wifi已连接");
			}
			if(mobileNetworkInfo!=null){
				LogUtils.i("netstate", "移动网络打开");
				Tools.showInfo(context, "移动网络已打开");
			}
			
			//重新连接，重新登录
		}

	}

}
