package com.tarena.xmpp.model;

import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.packet.Message;

public class GroupChatEntity {
	
	/**
	 * key �Ƿ�����
	 * Message �����µ���Ϣ����
	 */
	public static ConcurrentHashMap<String,Message> map=
			new ConcurrentHashMap<String, Message>();
}