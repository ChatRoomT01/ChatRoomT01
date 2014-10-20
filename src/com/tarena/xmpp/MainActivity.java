package com.tarena.xmpp;

import com.tarena.xmpp.model.LoginBiz;
import com.tarena.xmpp.model.TApplication;
import com.tarena.xmpp.util.BaseActivity;
import com.tarena.xmpp.view.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


public class MainActivity extends BaseActivity {
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1://��ת����¼����
				Intent intent=new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.i("threadid", "MainActivty app onCreate threadid="+Thread.currentThread().getId());
		
		//��¼
//		LoginBiz loginBiz=new LoginBiz();
//		loginBiz.login("ccm", "123456");
		
		//����һ�������߳�5��֮���Զ���ת����¼����
		new Thread(){
			public void run() {
				Log.i("threadid", "MainActivity app thread threadid="+Thread.currentThread().getId());
				int count=0;
				while(count<1){//�߳���Ϣ5��֮������Ϣ�����߳̽���ҳ����ת
					try {
						Log.i("workthread", "count="+count);
						Thread.sleep(1000); 
						count++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(-1);
			};
		}.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
		TApplication.allActivities.remove(this);
	}
	
	
}
