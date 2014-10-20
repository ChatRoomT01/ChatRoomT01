package com.tarena.xmpp.model;

import java.util.HashMap;

import org.jivesoftware.smack.AccountManager;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.util.Log;

public class RegisterBiz {
	
	/**
	 * 注册
	 * @param username
	 * @param password
	 * @param map
	 */
	public void register(final String username,final String password,
			final HashMap<String,String> map){
		
		new Thread(){
			public void run() {
				try {
					//判断服务器是否连接上
					int count=0;
					while(count<6000 && TApplication.isConnected==false){
						//网络没有连接上线程休息
						Log.i("threadid", "等待网络连通");
						Thread.sleep(10);
						count++;
					}
					
					//如果连接上了就进行注册操作
					if(TApplication.xmppConnection.isConnected()){
						AccountManager manager=
								TApplication.xmppConnection.getAccountManager();
						manager.createAccount(username, password, map);
						//这里不管是注册成功还是失败都不返回任何东西
						//如果注册成功则程序会继续往下执行，如果注册失败就会出现Exception
						//发广播
						Log.i("threadid", "注册成功");
						Intent intent=new Intent(TApplication.ACTION_REGISTER);
						intent.putExtra(TApplication.KEY_IS_SUCCESS, true);
						TApplication.instance.sendBroadcast(intent);
					}else{
						//如果没有连接上就发广播说明服务器没有连接
						Log.i("threadid", "注册失败，网络不通");
						Intent intent=new Intent(TApplication.ACTION_REGISTER);
						intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
						intent.putExtra(TApplication.KEY_EXTRA_MESSAGE, "注册失败，网络错误，请联网");
						TApplication.instance.sendBroadcast(intent);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					//用户名已存在
					Log.i("threadid", "注册失败，该用户已存在");
					Intent intent=new Intent(TApplication.ACTION_REGISTER);
					intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
					intent.putExtra(TApplication.KEY_EXTRA_MESSAGE, "注册失败,该用户名已存在");
					TApplication.instance.sendBroadcast(intent);
				}
			};
		}.start();
		
	}

}
