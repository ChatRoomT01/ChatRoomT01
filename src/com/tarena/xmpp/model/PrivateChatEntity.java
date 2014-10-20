package com.tarena.xmpp.model;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.packet.Message;

public class PrivateChatEntity {
	
	//线程安全：多个线程同时操作一个数据
	public static ConcurrentHashMap<String,Vector<Message>> map=
			new ConcurrentHashMap<String, Vector<Message>>();
	
	/**
	 * 将聊天记录保存到到实体中
	 * @param friendName
	 * @param msg
	 */
	public static void saveChatRecord(String friendName,Message msg){
		//将发送的内容存储到实体类中
		Vector<Message> vector=PrivateChatEntity.map.get(friendName);
		if(vector==null){//判断是否是初次聊天
			vector=new Vector<Message>();
			PrivateChatEntity.map.put(friendName, vector);
		}
		vector.add(msg);
		PrivateChatEntity.map.put(friendName, vector);
	}
}
