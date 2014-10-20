package com.tarena.xmpp.model;

import java.util.ArrayList;
import java.util.Vector;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.tarena.xmpp.R;
import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.LogUtils;
import com.tarena.xmpp.util.Tools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.util.Log;

public class TApplication extends Application {
	public static final String ACTION_LOGIN = "com.tarena.xmpp.view.LoginActivity.login";
	public static final String ACTION_REGISTER = "com.tarena.xmpp.view.RegisterActivity.ACTION_REGISTER";
	public static final String ACTION_ADD_FRIEND = "com.tarena.xmpp.view.AddFriendActivity.ACTION_ADD_FRIEND";
	public static final String ACTION_GROUP_LIST_CHANGE = "com.tarena.xmpp.view.GroupListFragment.ACTION_GROUP_LIST_CHANGE";
	public static final String ACTION_PRIVATE_CHAT_MESSAGE = "com.tarena.xmpp.view.PrivateChatActivity.ACTION_PRIVATE_CHAT_MESSAGE";
	public static final String ACTION_ENTER_GROUP_CHAT = "com.tarena.xmpp.view.GroupChat.ENTER_GROUP_CHAT";
	public static final String ACTION_GROUP_CHAT_MESSAGE_SHOW = "com.tarena.xmpp.view.GroupChatActivity.GROUP_CHAT_MESSAGE";

	public static final String KEY_IS_SUCCESS = "KEY_IS_SUCCESS";
	public static final String KEY_EXTRA_MESSAGE = "KEY_EXTRA_MASSAGE";
	public static final String KEY_EXTRA_GROUP_NAME = "groupName";
	public static final String KEY_EXTRA_FRIEND_NAME = "friendName";
	public static final String KEY_EXTRA_MESSAGE_FROM = "from";
	public static boolean isReleased = false;

	public static ArrayList<Activity> allActivities = new ArrayList<Activity>();

	private static String currentUsername, password;
	public static MultiUserChat currentRoom;

	/**
	 * �洢�û���
	 * 
	 * @param username
	 */
	public static void saveUsername(String username) {
		TApplication.currentUsername = username;
	}

	/**
	 * �洢����
	 * 
	 * @param password
	 */
	public static void savePassword(String password) {
		TApplication.password = password;
	}

	/**
	 * ��ȡ�û���
	 * 
	 * @return
	 */
	public static String getUsername() {
		return TApplication.currentUsername;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return
	 */
	public static String getPassword() {
		return TApplication.password;
	}

	/**
	 * �˳�Ӧ��
	 */
	public static void exit() {
		try {
			for (Activity a : allActivities) {
				a.finish();
				Log.i("exit app", "exit : " + a);
			}
			// �Ͽ��������������
			xmppConnection.disconnect();

			System.exit(0);// �˳���������ʱ��ִ��TApplication��onCreate����
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	String host = "";
	int port;
	public String serviceName;
	ConnectionConfiguration config = null;
	public static XMPPConnection xmppConnection;
	public static boolean isConnected;
	public static boolean isLoginSuccess;

	public static Context instance;

	@Override
	public void onCreate() {
		super.onCreate();

		LogUtils.i("TApplication", "oncreate");

		instance = this;

		Log.i("threadid", "TApplication app oncreate threadid="
				+ Thread.currentThread().getId());
		try {
			Log.i("oncreate", "run");
			getConfig();

			config = new ConnectionConfiguration(host, port, serviceName);
			xmppConnection = new XMPPConnection(config);

			// ���ӵ�������
			connecteToServer();

			// asmack frameָ������ʵ����
			AllPacketListener allPacketListener = new AllPacketListener();
			xmppConnection.addPacketListener(allPacketListener, null);

			AllPacketInterceptor allPacketInterceptor = new AllPacketInterceptor();
			xmppConnection.addPacketInterceptor(allPacketInterceptor, null);

			// ���ѷ���
			RosterUpdateListener rosterUpdateListener = new RosterUpdateListener();
			// ����
			PacketTypeFilter packetTypeFilter = new PacketTypeFilter(
					RosterPacket.class);
			xmppConnection.addPacketListener(rosterUpdateListener,
					packetTypeFilter);

			// ˽��
			PrivateChatListener privateChatListener = new PrivateChatListener();
			PrivateChatFilter privateChatfilter = new PrivateChatFilter();
			xmppConnection.addPacketListener(privateChatListener,
					privateChatfilter);

			// Ⱥ��
			GroupChatListener groupChatListener = new GroupChatListener();
			GroupChatFilter groupChatFilter = new GroupChatFilter();
			xmppConnection
					.addPacketListener(groupChatListener, groupChatFilter);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	/**
	 * ��������
	 */
	public static void connecteToServer() {
		// �����µ��̣߳�����
		new Thread() {
			public void run() {
				try {
					// ����
					long startTime, endTime;
					Log.i("threadid", "��ʼ����");
					startTime = System.currentTimeMillis();
					xmppConnection.connect();
					isConnected = xmppConnection.isConnected();
					Log.i("threadid", "isConnected=" + isConnected);
					endTime = System.currentTimeMillis();
					Log.i("threadid", "����ռ�õ�ʱ��=" + (endTime - startTime));
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			};
		}.start();
	}

	/** ����res/xml/config.xml�ļ� */
	private void getConfig() {
		try {
			XmlResourceParser parser = this.getResources().getXml(R.xml.config);
			int eventType = parser.getEventType();
			while (eventType != parser.END_DOCUMENT) {
				switch (eventType) {
				case XmlResourceParser.START_TAG:
					String tagName = parser.getName();
					if ("host".equals(tagName)) {
						host = parser.nextText();
					}
					if ("port".equals(tagName)) {
						port = Integer.parseInt(parser.nextText());
					}
					if ("serviceName".equals(tagName)) {
						serviceName = parser.nextText();
					}
					break;

				default:
					break;
				}

				eventType = parser.next();
			}

		} catch (Exception e) {
		}
	}

	// asmack����������������������Ϣ����PacketListener
	class AllPacketListener implements PacketListener {

		@Override
		public void processPacket(Packet packet) {
			LogUtils.i("allPacketListener", packet.toXML());

			// ����������󷵻���Ӧ
			if (packet instanceof Presence) {
				Presence presence = (Presence) packet;
				Type type = presence.getType();
				if (type == Type.subscribed) {// ͬ��
					LogUtils.i("addFriend", "�ѽ�" + presence.getFrom() + "���Ϊ����");
				} else if (type == Type.unsubscribe) {// ��ͬ��
					LogUtils.i("addFriend", presence.getFrom() + "�ܾ����Ϊ����");
				}
			}
		}
	}

	// asmack����
	class AllPacketInterceptor implements PacketInterceptor {
		@Override
		public void interceptPacket(Packet packet) {
			LogUtils.i("allPacketInterceptor", packet.toXML());
		}
	}

	// ���ѷ���
	class RosterUpdateListener implements PacketListener {

		@Override
		public void processPacket(Packet packet) {
			try {

				// Packet�ǳ����࣬���������packet��������packet,����Packet��ʵ����
				// IQҲ�ǳ����࣬��������Ҫ��ȡ������ϢӦ���õ�IQ��ʵ����RosterPacket

				// <iq>��ӦRosterPacket
				// <presence>��ӦPresence
				// ��ȡ������Ϣ
				// ArrayList<RosterGroup> list = new ArrayList<RosterGroup>
				// (xmppConnection.getRoster().getGroups());
				//
				// for (RosterGroup r : list) {
				// LogUtils.i ("RosterUpdateListener", "group=" + r.getName());
				// }

				// ���͹㲥���º��ѷ�����Ϣ
				Intent intent = new Intent(
						TApplication.ACTION_GROUP_LIST_CHANGE);
				sendBroadcast(intent);
				// LogUtils.i("updategroup", "���͹㲥���·�����Ϣ");
			} catch (Exception e) {
				ExceptionUtils.handle(e);
			}
		}
	}

	/**
	 * ˽����Ϣ������
	 * 
	 */
	class PrivateChatFilter implements PacketFilter {
		@Override
		public boolean accept(Packet packet) {
			if (packet instanceof Message) {// �Ƿ�����Ϣxml
				Message msg = (Message) packet;
				org.jivesoftware.smack.packet.Message.Type type = msg.getType();
				if (type == org.jivesoftware.smack.packet.Message.Type.chat) {// �ж��Ƿ���˽����Ϣ
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * ˽����Ϣ������
	 */
	class PrivateChatListener implements PacketListener {
		@Override
		public void processPacket(Packet packet) {

			Message msg = (Message) packet;
			String from = msg.getFrom();
			String body = msg.getBody();

			// ��������Ϣ���浽ʵ�������
			if (from.contains("@")) {
				from = from.substring(0, from.indexOf("@"));
			}
			PrivateChatEntity.saveChatRecord(from, msg);

			// ���͹㲥���½���
			Intent intent = new Intent(TApplication.ACTION_PRIVATE_CHAT_MESSAGE);
			intent.putExtra(TApplication.KEY_EXTRA_MESSAGE_FROM, from);
			instance.sendBroadcast(intent);

			LogUtils.i("privatechat", from + " ˵  " + body);
		}
	}

	/**
	 * Ⱥ����Ϣ������
	 * 
	 * @author chencaimei
	 * 
	 */
	class GroupChatFilter implements PacketFilter {
		@Override
		public boolean accept(Packet packet) {
			if (packet instanceof Message) {
				Message msg = (Message) packet;
				org.jivesoftware.smack.packet.Message.Type type = msg.getType();
				if (type == org.jivesoftware.smack.packet.Message.Type.groupchat)
					return true;
			}
			return false;
		}
	}

	/**
	 * Ⱥ��Ϣ������
	 * 
	 * @author chencaimei
	 * 
	 */
	class GroupChatListener implements PacketListener {
		@Override
		public void processPacket(Packet packet) {
			if (packet instanceof Message) {
				Message msg = (Message) packet;
				// ��ȡ������Ϣ
				//1309@conference.tarena3gxmpp.com/11
				String from = msg.getFrom();
				String body = msg.getBody();

				if (body != null) {
					//LogUtils.i("groupchat", from + " ˵  " + body);
					
					if(from.contains("/")){
						String name=from.substring(from.indexOf("/")+1);
						//���ǡ��ҡ����͵���Ϣ��洢����
						if(!TApplication.getUsername().equals(name)){
							// ��������Ϣ���浽GroupChatEntity��
							from=from.substring(0, from.indexOf("/"));
							GroupChatEntity.map.put(from, msg);
							LogUtils.i("groupchatListener", "from="+from);
							
							//���㲥
							Intent intent=new Intent(TApplication.ACTION_GROUP_CHAT_MESSAGE_SHOW);
							sendBroadcast(intent);
						}
					}
				}
			}
		}
	}
}
