package com.tarena.xmpp.receiver;

import com.tarena.xmpp.util.LogUtils;
import com.tarena.xmpp.util.Tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * ����״̬�ı�㲥������
 * @author chencaimei
 *
 */
public class NetworkChangedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager manager=(ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo=manager.getActiveNetworkInfo();
		
		if(activeNetworkInfo==null){//����Ͽ�����Ҫ���û���ʾ
			LogUtils.i("netstate", "����Ͽ�");
			Tools.showInfo(context, "�����ж�");
		}else{
			//����򿪣���Ҫ�������ӣ����µ�¼
			NetworkInfo wifiInfo=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileNetworkInfo=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if(wifiInfo!=null){
				LogUtils.i("netstate", "wifi��");
				Tools.showInfo(context, "wifi������");
			}
			if(mobileNetworkInfo!=null){
				LogUtils.i("netstate", "�ƶ������");
				Tools.showInfo(context, "�ƶ������Ѵ�");
			}
			
			//�������ӣ����µ�¼
		}

	}

}
