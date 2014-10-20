package com.tarena.xmpp.model;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import android.content.Intent;

import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.LogUtils;

public class PrivateChatBiz {
	
	/**
	 * 发送消息
	 * @param to
	 * @param body
	 */
	public void sendMessage(final String to,final String body){
		new Thread(){
			public void run() {
				
				try {
					//设置发送的消息内容
					Message msg=new Message();
					msg.setFrom(TApplication.getUsername()+"@tarena3gxmpp.com");
					msg.setTo(to+"@tarena3gxmpp.com");
					msg.setBody(body);
					msg.setType(Type.chat);
					
					//发送消息
					TApplication.xmppConnection.sendPacket(msg);
					
					//将发送的信息保存起来
					PrivateChatEntity.saveChatRecord(to, msg);
					
					LogUtils.i("PrivateChatReceiver", msg.getTo()
							+ " 说  " + msg.getBody());
					
					Intent intent=new Intent(TApplication.ACTION_PRIVATE_CHAT_MESSAGE);
					TApplication.instance.sendBroadcast(intent);
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			};
		}.start();
	}
}
