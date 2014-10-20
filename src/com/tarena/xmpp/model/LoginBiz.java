package com.tarena.xmpp.model;

import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.LogUtils;

import android.content.Intent;
import android.util.Log;

public class LoginBiz {
	
	public void login(final String username,final String password){
		
		//启动一个新的线程，联网，登录
		new Thread(){
			public void run() {
				LogUtils.i("LoginBiz", "currentUser="+username);
				
				Intent intent=new Intent(TApplication.ACTION_LOGIN);
				try {
					Log.i("threadid", "login biz threadId="+Thread.currentThread().getId());
					//一下三个步骤有asmack完成
					//OutputStream
					//InputStream
					//parser
					//发广播
					
					Log.i("threadid", "准备登录");
					//判断是否已经和服务器连接上
					int count=0;
					//每隔0.1秒检查是否已经和服务器连接上了，如果1秒以内还是连接不上则发广播说连接不上服务器，不再继续进行登录操作
					long startTime=System.currentTimeMillis();
					while(count<220 && TApplication.xmppConnection.isConnected()==false){
						Thread.sleep(100);
						count++;
//						Log.i("threadid", "login biz count="+count);
					}
					long endTime=System.currentTimeMillis();
					Log.i("threadid", "登录占用的时间="+(endTime-startTime));
					
					
//					Log.i("threadid", "服务器是否连接上="+TApplication.xmppConnection.isConnected());
					if(TApplication.xmppConnection.isConnected()){//服务器已经连接上了
						Log.i("threadid", "连接成功");
						TApplication.xmppConnection.login(username, password);
						
						//判断是否登录成功
						TApplication.isLoginSuccess=TApplication.xmppConnection.isAuthenticated();
						intent.putExtra(TApplication.KEY_IS_SUCCESS,TApplication.isLoginSuccess);
//						TApplication.instance.sendBroadcast(intent);
						
						//如果登录成功则将用户名和密码存储到TApplication中
						if(TApplication.isLoginSuccess){
							TApplication.saveUsername(username);
							TApplication.savePassword(password);
							
						}
						
//						Log.i("threadid", "登录成功");
					}else{//1秒后服务器还是连接不上
						//发广播
						LogUtils.i("threadid", "登录超时");
						intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
						intent.putExtra(TApplication.KEY_EXTRA_MESSAGE, "登录失败，请检查网络");
					}
					
				} catch (Exception e) {
					LogUtils.i("threadid", "服务端正在维护");
					intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
					intent.putExtra(TApplication.KEY_EXTRA_MESSAGE, "登录失败，服务端正在维护");
					ExceptionUtils.handle(e);
				}
				//发广播
				TApplication.instance.sendBroadcast(intent);
			};
		}.start();
	}

}
