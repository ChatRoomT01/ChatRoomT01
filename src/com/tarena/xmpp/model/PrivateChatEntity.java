package com.tarena.xmpp.model;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.packet.Message;

public class PrivateChatEntity {
	
	//�̰߳�ȫ������߳�ͬʱ����һ������
	public static ConcurrentHashMap<String,Vector<Message>> map=
			new ConcurrentHashMap<String, Vector<Message>>();
	
	/**
	 * �������¼���浽��ʵ����
	 * @param friendName
	 * @param msg
	 */
	public static void saveChatRecord(String friendName,Message msg){
		//�����͵����ݴ洢��ʵ������
		Vector<Message> vector=PrivateChatEntity.map.get(friendName);
		if(vector==null){//�ж��Ƿ��ǳ�������
			vector=new Vector<Message>();
			PrivateChatEntity.map.put(friendName, vector);
		}
		vector.add(msg);
		PrivateChatEntity.map.put(friendName, vector);
	}
}
