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
	 * 存储用户名
	 * 
	 * @param username
	 */
	public static void saveUsername(String username) {
		TApplication.currentUsername = username;
	}

	/**
	 * 存储密码
	 * 
	 * @param password
	 */
	public static void savePassword(String password) {
		TApplication.password = password;
	}

	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public static String getUsername() {
		return TApplication.currentUsername;
	}

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public static String getPassword() {
		return TApplication.password;
	}

	/**
	 * 退出应用
	 */
	public static void exit() {
		try {
			for (Activity a : allActivities) {
				a.finish();
				Log.i("exit app", "exit : " + a);
			}
			// 断开与服务器的连接
			xmppConnection.disconnect();

			System.exit(0);// 退出再启动的时候执行TApplication的onCreate方法
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

			// 连接到服务器
			connecteToServer();

			// asmack frame指向具体的实现类
			AllPacketListener allPacketListener = new AllPacketListener();
			xmppConnection.addPacketListener(allPacketListener, null);

			AllPacketInterceptor allPacketInterceptor = new AllPacketInterceptor();
			xmppConnection.addPacketInterceptor(allPacketInterceptor, null);

			// 好友分组
			RosterUpdateListener rosterUpdateListener = new RosterUpdateListener();
			// 过滤
			PacketTypeFilter packetTypeFilter = new PacketTypeFilter(
					RosterPacket.class);
			xmppConnection.addPacketListener(rosterUpdateListener,
					packetTypeFilter);

			// 私聊
			PrivateChatListener privateChatListener = new PrivateChatListener();
			PrivateChatFilter privateChatfilter = new PrivateChatFilter();
			xmppConnection.addPacketListener(privateChatListener,
					privateChatfilter);

			// 群聊
			GroupChatListener groupChatListener = new GroupChatListener();
			GroupChatFilter groupChatFilter = new GroupChatFilter();
			xmppConnection
					.addPacketListener(groupChatListener, groupChatFilter);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	/**
	 * 连网操作
	 */
	public static void connecteToServer() {
		// 启动新的线程，联网
		new Thread() {
			public void run() {
				try {
					// 联网
					long startTime, endTime;
					Log.i("threadid", "开始连接");
					startTime = System.currentTimeMillis();
					xmppConnection.connect();
					isConnected = xmppConnection.isConnected();
					Log.i("threadid", "isConnected=" + isConnected);
					endTime = System.currentTimeMillis();
					Log.i("threadid", "连接占用的时间=" + (endTime - startTime));
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			};
		}.start();
	}

	/** 解析res/xml/config.xml文件 */
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

	// asmack监听器，服务器过来的信息都是PacketListener
	class AllPacketListener implements PacketListener {

		@Override
		public void processPacket(Packet packet) {
			LogUtils.i("allPacketListener", packet.toXML());

			// 好友添加请求返回响应
			if (packet instanceof Presence) {
				Presence presence = (Presence) packet;
				Type type = presence.getType();
				if (type == Type.subscribed) {// 同意
					LogUtils.i("addFriend", "已将" + presence.getFrom() + "添加为好友");
				} else if (type == Type.unsubscribe) {// 不同意
					LogUtils.i("addFriend", presence.getFrom() + "拒绝添加为好友");
				}
			}
		}
	}

	// asmack拦截
	class AllPacketInterceptor implements PacketInterceptor {
		@Override
		public void interceptPacket(Packet packet) {
			LogUtils.i("allPacketInterceptor", packet.toXML());
		}
	}

	// 好友分类
	class RosterUpdateListener implements PacketListener {

		@Override
		public void processPacket(Packet packet) {
			try {

				// Packet是抽象类，所以这里的packet不可能是packet,而是Packet的实现类
				// IQ也是抽象类，所有这里要获取分组信息应该用到IQ的实现类RosterPacket

				// <iq>对应RosterPacket
				// <presence>对应Presence
				// 获取分组信息
				// ArrayList<RosterGroup> list = new ArrayList<RosterGroup>
				// (xmppConnection.getRoster().getGroups());
				//
				// for (RosterGroup r : list) {
				// LogUtils.i ("RosterUpdateListener", "group=" + r.getName());
				// }

				// 发送广播更新好友分类信息
				Intent intent = new Intent(
						TApplication.ACTION_GROUP_LIST_CHANGE);
				sendBroadcast(intent);
				// LogUtils.i("updategroup", "发送广播更新分组信息");
			} catch (Exception e) {
				ExceptionUtils.handle(e);
			}
		}
	}

	/**
	 * 私聊信息过滤器
	 * 
	 */
	class PrivateChatFilter implements PacketFilter {
		@Override
		public boolean accept(Packet packet) {
			if (packet instanceof Message) {// 是否是消息xml
				Message msg = (Message) packet;
				org.jivesoftware.smack.packet.Message.Type type = msg.getType();
				if (type == org.jivesoftware.smack.packet.Message.Type.chat) {// 判断是否是私聊信息
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 私聊信息监听器
	 */
	class PrivateChatListener implements PacketListener {
		@Override
		public void processPacket(Packet packet) {

			Message msg = (Message) packet;
			String from = msg.getFrom();
			String body = msg.getBody();

			// 将聊天信息保存到实体对象中
			if (from.contains("@")) {
				from = from.substring(0, from.indexOf("@"));
			}
			PrivateChatEntity.saveChatRecord(from, msg);

			// 发送广播更新界面
			Intent intent = new Intent(TApplication.ACTION_PRIVATE_CHAT_MESSAGE);
			intent.putExtra(TApplication.KEY_EXTRA_MESSAGE_FROM, from);
			instance.sendBroadcast(intent);

			LogUtils.i("privatechat", from + " 说  " + body);
		}
	}

	/**
	 * 群聊信息过滤器
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
	 * 群消息监听器
	 * 
	 * @author chencaimei
	 * 
	 */
	class GroupChatListener implements PacketListener {
		@Override
		public void processPacket(Packet packet) {
			if (packet instanceof Message) {
				Message msg = (Message) packet;
				// 获取聊天信息
				//1309@conference.tarena3gxmpp.com/11
				String from = msg.getFrom();
				String body = msg.getBody();

				if (body != null) {
					//LogUtils.i("groupchat", from + " 说  " + body);
					
					if(from.contains("/")){
						String name=from.substring(from.indexOf("/")+1);
						//不是“我”发送的消息则存储起来
						if(!TApplication.getUsername().equals(name)){
							// 将聊天信息保存到GroupChatEntity中
							from=from.substring(0, from.indexOf("/"));
							GroupChatEntity.map.put(from, msg);
							LogUtils.i("groupchatListener", "from="+from);
							
							//发广播
							Intent intent=new Intent(TApplication.ACTION_GROUP_CHAT_MESSAGE_SHOW);
							sendBroadcast(intent);
						}
					}
				}
			}
		}
	}
}
