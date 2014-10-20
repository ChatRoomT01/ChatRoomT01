package com.tarena.xmpp.model;


import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.Intent;
import android.util.Log;

import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.LogUtils;
import com.tarena.xmpp.util.Tools;

public class GroupChatBiz {
	
	/**
	 * 进入聊天室
	 * @param roomName 聊天室的名称
	 */
	public void joinRoom(final String roomName){
		new Thread(){
			public void run() {
				
				Intent intent=new Intent(TApplication.ACTION_ENTER_GROUP_CHAT);
				try {
					
					MultiUserChat groupChat=new MultiUserChat(
							TApplication.xmppConnection, roomName+"@conference.tarena3gxmpp.com");
					String name=TApplication.getUsername();
					if(name.contains("@")){
						name=name.substring(0, name.indexOf("@"));
					}
					groupChat.join(name);
					
					//发广播
					intent.putExtra(TApplication.KEY_IS_SUCCESS, true);
					
					LogUtils.i("groupchat", "进入房间OK");
					
					//保存房间信息
					TApplication.currentRoom=groupChat;
					
				} catch (Exception e) {
					intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
					LogUtils.i("groupchat", "进入房间失败");
					ExceptionUtils.handle(e);
				}
				TApplication.instance.sendBroadcast(intent);
			};
		}.start();
	}
	
	/**
	 * 发送群消息
	 * @param body
	 */
	public void sendMessage(final String body){
		new Thread(){
			public void run() {
				try {
					//创建消息对象
					Message msg=new Message();
					msg.setFrom(TApplication.getUsername()+"@tarena3gxmpp.com");
					msg.setBody(body);
					msg.setType(Type.groupchat);
					msg.setTo(TApplication.currentRoom.getRoom());
					
					//发送群消息
					TApplication.currentRoom.sendMessage(msg);
					
//					LogUtils.i("groupchatbiz", msg.getFrom()+" 说 "+body);
					
					//将信息存储到实体类中
					GroupChatEntity.map.put(msg.getTo(), msg);
					
					//from=ccm@tarena3gxmpp.com
					//to=1309@conference.tarena3gxmpp.com
//					LogUtils.i("groupchatbiz", "from="+msg.getFrom());
//					LogUtils.i("groupchatbiz", "to="+msg.getTo());
					
					//发送消息到Activity中更新界面
					Intent intent=new Intent(TApplication.ACTION_GROUP_CHAT_MESSAGE_SHOW);
					TApplication.instance.sendBroadcast(intent);
					
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			};
		}.start();
	}
}
