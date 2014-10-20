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
	 * ����������
	 * @param roomName �����ҵ�����
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
					
					//���㲥
					intent.putExtra(TApplication.KEY_IS_SUCCESS, true);
					
					LogUtils.i("groupchat", "���뷿��OK");
					
					//���淿����Ϣ
					TApplication.currentRoom=groupChat;
					
				} catch (Exception e) {
					intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
					LogUtils.i("groupchat", "���뷿��ʧ��");
					ExceptionUtils.handle(e);
				}
				TApplication.instance.sendBroadcast(intent);
			};
		}.start();
	}
	
	/**
	 * ����Ⱥ��Ϣ
	 * @param body
	 */
	public void sendMessage(final String body){
		new Thread(){
			public void run() {
				try {
					//������Ϣ����
					Message msg=new Message();
					msg.setFrom(TApplication.getUsername()+"@tarena3gxmpp.com");
					msg.setBody(body);
					msg.setType(Type.groupchat);
					msg.setTo(TApplication.currentRoom.getRoom());
					
					//����Ⱥ��Ϣ
					TApplication.currentRoom.sendMessage(msg);
					
//					LogUtils.i("groupchatbiz", msg.getFrom()+" ˵ "+body);
					
					//����Ϣ�洢��ʵ������
					GroupChatEntity.map.put(msg.getTo(), msg);
					
					//from=ccm@tarena3gxmpp.com
					//to=1309@conference.tarena3gxmpp.com
//					LogUtils.i("groupchatbiz", "from="+msg.getFrom());
//					LogUtils.i("groupchatbiz", "to="+msg.getTo());
					
					//������Ϣ��Activity�и��½���
					Intent intent=new Intent(TApplication.ACTION_GROUP_CHAT_MESSAGE_SHOW);
					TApplication.instance.sendBroadcast(intent);
					
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			};
		}.start();
	}
}
