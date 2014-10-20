package com.tarena.xmpp.view;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tarena.xmpp.R;
import com.tarena.xmpp.model.GroupChatBiz;
import com.tarena.xmpp.model.GroupChatEntity;
import com.tarena.xmpp.model.TApplication;
import com.tarena.xmpp.util.BaseActivity;
import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.LogUtils;
import com.tarena.xmpp.util.Tools;

public class GroupChatActivity extends BaseActivity {
	TextView tvRoomName;
	EditText etbody;
	Button btnSend;
	LinearLayout linearLayout;
	ScrollView scrollView;

	GroupChatBiz groupChatBiz;
	GroupChatReceiver receiver;

	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.group_chat);
			// ��ʼ��
			setupView();
			// ���ü�����
			setListener();

			handler = new Handler();

			receiver = new GroupChatReceiver();
			IntentFilter filter = new IntentFilter(
					TApplication.ACTION_GROUP_CHAT_MESSAGE_SHOW);
			registerReceiver(receiver, filter);

		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	/**
	 * ��ʼ��
	 */
	private void setupView() {
		// ��ȡ�ؼ�������
		tvRoomName = (TextView) findViewById(R.id.tv_groupChat_roomName);
		etbody = (EditText) findViewById(R.id.et_groupChat_body);
		btnSend = (Button) findViewById(R.id.btn_groupChat_send);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout_groupChat);
		scrollView = (ScrollView) findViewById(R.id.scrollView_groupChat);

		String roomName = TApplication.currentRoom.getRoom();
		if (roomName.contains("@")) {
			roomName = roomName.substring(0, roomName.indexOf("@"));
		}
		tvRoomName.setText(roomName);

		groupChatBiz = new GroupChatBiz();

	}

	/**
	 * ���ü�����
	 */
	private void setListener() {
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// ��ȡ���������
					String body = etbody.getText().toString();
					// ���зǿ��ж�
					if (!Tools.isNull(body)) {
						// ����������ݽ���GroupChatBiz
						groupChatBiz.sendMessage(body);
						etbody.getText().clear();
					}
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			}
		});
	}

	class GroupChatReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (TApplication.ACTION_GROUP_CHAT_MESSAGE_SHOW.equals(action)) {
				// ��ȡ��ǰ������������
				MultiUserChat groupChat = TApplication.currentRoom;
				String room = groupChat.getRoom();
				// ��ȡ��Ϣ����
				Message msg = GroupChatEntity.map.get(room);

				if (msg != null) {
					// from:1)�ҷ��͵ģ�from=ccm@tarena3gxmpp.com
					// 2)openfire���͵ģ�from=1309@conference.tarena3gxmpp.com/11
					String from = msg.getFrom();
					String body = msg.getBody();

					String currentUser = TApplication.getUsername()
							+ "@tarena3gxmpp.com";

					if (currentUser.equals(from)) {// ��˵��
						View view = View.inflate(GroupChatActivity.this,
								R.layout.dialog_right, null);
						linearLayout.addView(view);
						((TextView) (view
								.findViewById(R.id.tv_privateChat_right)))
								.setText("�ң�" + body);
					} else {// ����˵��
						if (from.contains("/")) {
							from = from.substring(from.indexOf("/") + 1);
						}
						View view = View.inflate(GroupChatActivity.this,
								R.layout.dialog_left, null);
						linearLayout.addView(view);
						((TextView) (view
								.findViewById(R.id.tv_privateChat_left)))
								.setText(from + "��" + body);
					}

					// ������ʾ���µ���Ϣ
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (linearLayout.getHeight() > scrollView
									.getHeight()) {
								int y = linearLayout.getHeight()
										- scrollView.getHeight();
								scrollView.scrollTo(0, y);
							}
						}
					});

				}

			}
		}

	}
}
