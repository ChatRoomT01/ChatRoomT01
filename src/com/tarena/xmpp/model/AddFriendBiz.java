package com.tarena.xmpp.model;

import org.jivesoftware.smack.Roster;

import com.tarena.xmpp.util.LogUtils;

import android.content.Intent;
import android.util.Log;

public class AddFriendBiz {
	
	public void addFriend(final String username,final String name,final String[] groups){
		new Thread(){
			public void run() {
				try{
					
					//检查网络是否连接，如果没有连接重新连接
					if(!TApplication.xmppConnection.isConnected()){
						TApplication.connecteToServer();//连网
					}
					
					/*这段代码不执行，因为在登录操作中已经有等待网络连接的操作了，所以这里不再做等待的操作了
					int countConnect=0;
					//while等待网络连接
					while(countConnect<200 && !TApplication.xmppConnection.isConnected()){
						Thread.sleep(100);
						countConnect++;
					}*/
					
					//网络连接成功后检查是否登录,如果没有登录则重新登录
					if(!TApplication.xmppConnection.isAuthenticated()){
						LoginBiz loginBiz=new LoginBiz();
						loginBiz.login(TApplication.getUsername(),TApplication.getPassword());
					}
					
					//while等待登录
					int countLogin=0;
					while(countLogin<600 && !TApplication.xmppConnection.isAuthenticated()){
						Thread.sleep(100);
						countLogin++;
					}
					
					if(TApplication.xmppConnection.isAuthenticated()){
						//登录成功后添加好友
						//Roster是花名册，包括用户的好友分类和好友信息
						Roster roster=TApplication.xmppConnection.getRoster();
						String user=username+"@tarena3gxmpp.com";
						roster.createEntry(user, name, groups);
						LogUtils.i("roster", "添加好友成功");
						
						//发送广播
						Intent intent=new Intent(TApplication.ACTION_ADD_FRIEND);
						intent.putExtra(TApplication.KEY_IS_SUCCESS, true);
						TApplication.instance.sendBroadcast(intent);
					}
					
				} catch (Exception e) {
					//发送广播说明请求信息发送失败
					Intent intent=new Intent(TApplication.ACTION_ADD_FRIEND);
					intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
					TApplication.instance.sendBroadcast(intent);
					
					LogUtils.i("roster","添加好友失败");
				}
			};
		}.start();
	}

}
