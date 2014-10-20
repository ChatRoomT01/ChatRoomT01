package com.tarena.xmpp.util;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	
	public void checkedNetworkState(final Context context){
		ConnectivityManager manager=(ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=manager.getActiveNetworkInfo();
		//如果info为空则说明显示是无网络状态
		if(null==info){
			//显示警告对话框
			AlertDialog.Builder builder=new Builder(context);
			builder.setTitle("网络不可用");
			builder.setPositiveButton("设置网络", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						//发送广播设置网络
						Intent intent=new Intent(
								android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						context.startActivity(intent);
					} catch (Exception e) {
						ExceptionUtils.handle(e);
					}
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						dialog.dismiss();
					} catch (Exception e) {
						ExceptionUtils.handle(e);
					}
				}
			});
			AlertDialog dialog=builder.create();
			dialog.show();
			
		}
	}
}
