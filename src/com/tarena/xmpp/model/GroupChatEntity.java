package com.tarena.xmpp.model;

import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.packet.Message;

public class GroupChatEntity {
	
	/**
	 * key 是房间名
	 * Message 是最新的消息内容
	 */
	public static ConcurrentHashMap<String,Message> map=
			new ConcurrentHashMap<String, Message>();
}
