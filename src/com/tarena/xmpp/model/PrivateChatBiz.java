package com.tarena.xmpp.model;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import android.content.Intent;

import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.LogUtils;

public class PrivateChatBiz {
	
	/**
	 * ������Ϣ
	 * @param to
	 * @param body
	 */
	public void sendMessage(final String to,final String body){
		new Thread(){
			public void run() {
				
				try {
					//���÷��͵���Ϣ����
					Message msg=new Message();
					msg.setFrom(TApplication.getUsername()+"@tarena3gxmpp.com");
					msg.setTo(to+"@tarena3gxmpp.com");
					msg.setBody(body);
					msg.setType(Type.chat);
					
					//������Ϣ
					TApplication.xmppConnection.sendPacket(msg);
					
					//�����͵���Ϣ��������
					PrivateChatEntity.saveChatRecord(to, msg);
					
					LogUtils.i("PrivateChatReceiver", msg.getTo()
							+ " ˵  " + msg.getBody());
					
					Intent intent=new Intent(TApplication.ACTION_PRIVATE_CHAT_MESSAGE);
					TApplication.instance.sendBroadcast(intent);
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			};
		}.start();
	}
}
