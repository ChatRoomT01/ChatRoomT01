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
		//���infoΪ����˵����ʾ��������״̬
		if(null==info){
			//��ʾ����Ի���
			AlertDialog.Builder builder=new Builder(context);
			builder.setTitle("���粻����");
			builder.setPositiveButton("��������", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						//���͹㲥��������
						Intent intent=new Intent(
								android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						context.startActivity(intent);
					} catch (Exception e) {
						ExceptionUtils.handle(e);
					}
				}
			});
			builder.setNegativeButton("ȡ��", new OnClickListener() {
				
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
