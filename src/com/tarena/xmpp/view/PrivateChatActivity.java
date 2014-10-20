package com.tarena.xmpp.view;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tarena.xmpp.R;
import com.tarena.xmpp.model.PrivateChatBiz;
import com.tarena.xmpp.model.PrivateChatEntity;
import com.tarena.xmpp.model.TApplication;
import com.tarena.xmpp.util.BaseActivity;
import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.LogUtils;
import com.tarena.xmpp.util.Tools;

public class PrivateChatActivity extends BaseActivity {

	TextView tvFriendName;
	EditText etBody;
	Button btnSendMessage;
	LinearLayout linearLayout;
	ScrollView scrollView;
	
	
	String friendName;

	PrivateChatBiz privateChatBiz;
	PrivateChatReceiver receiver;
	
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {

			setContentView(R.layout.private_chat);

			// ��ʼ������
			setupView();

			// ��Ӽ�����
			setListener();

		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	/**
	 * ��ʼ��
	 */
	private void setupView() {
		// ��ȡ�ؼ�������
		tvFriendName = (TextView) findViewById(R.id.tv_privateChat_friendName);
		etBody = (EditText) findViewById(R.id.et_privateChat_body);
		btnSendMessage = (Button) findViewById(R.id.btn_privateChat_send);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout_privateChat);
		scrollView=(ScrollView) findViewById(R.id.scrollView_privateChat);

		// ��ȡ�ϸ����洫�͹�����friendName
		friendName = getIntent().getStringExtra(
				TApplication.KEY_EXTRA_FRIEND_NAME);
		// ���ռ��������
		tvFriendName.setText(friendName);

		privateChatBiz = new PrivateChatBiz();

		// ע��㲥������
		receiver = new PrivateChatReceiver();
		IntentFilter filter = new IntentFilter(
				TApplication.ACTION_PRIVATE_CHAT_MESSAGE);
		registerReceiver(receiver, filter);
		
		handler=new Handler(){
			@Override
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					//������
					if(linearLayout.getHeight()>scrollView.getHeight()){
						int y=linearLayout.getHeight()-scrollView.getHeight();
						scrollView.scrollTo(0, y);
					}
					break;
				}
			}
		};
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	/**
	 * ��Ӽ�����
	 */
	private void setListener() {
		// �����Ͱ�ť��Ӽ�����
		btnSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// LogUtils.i("privatechat", "���Ͱ�ť�����ɹ�");
					// ��ȡ�û����������
					String body = etBody.getText().toString();
					// ����������ݽ��зǿ���֤
					if (!Tools.isNull(body)) {
						// ����PrivateChatBiz������Ϣ
						privateChatBiz.sendMessage(friendName, body);
						// ���༭���������
						etBody.getText().clear();
					}
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			}
		});
	}

	// ���������¼�Ĺ㲥���������û�����������棬��ʾ������Ϣ
	class PrivateChatReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			//����Ļ����ʾ��˵�����ݺͺ���˵������
			String action = intent.getAction();
			if (TApplication.ACTION_PRIVATE_CHAT_MESSAGE.equals(action)) {
//				// ��ȡ������Ϣ������
//				String from = intent
//						.getStringExtra(TApplication.KEY_EXTRA_MESSAGE_FROM);
				// ��ȡ�ú��ѵ�����ʵ����
				Vector<Message> vector = PrivateChatEntity.map.get(friendName);

				if (vector != null) {
					// ������ʵ�壬�����������
					linearLayout.removeAllViews();
					for (Message msg : vector) {
						LogUtils.i("PrivateChatReceiver", msg.getFrom()
								+ " ˵  " + msg.getBody());
						
						//��ȡ���ͷ�
						String from=msg.getFrom();
						if(from.contains("@")){
							from=from.substring(0, from.indexOf("@"));
						}
						//��ȡ��������
						String body=msg.getBody();
						//�ڽ�������ʾ������Ϣ
						if(from.equals(TApplication.getUsername())){//��ǰ�û���������Ϣ
							//����һ��VIew
							View view=View.inflate(PrivateChatActivity.this, 
									R.layout.dialog_right, null);
							//��View��ӵ�LinearLayout����ʾ
							linearLayout.addView(view);
							//������������ӵ����������ʾ����
							TextView tv=(TextView) view.findViewById(R.id.tv_privateChat_right);
							tv.setText(body);
						}else{//���ѷ��͹�������Ϣ
							//����һ��VIew
							View view=View.inflate(PrivateChatActivity.this, 
									R.layout.dialog_left, null);
							//��View��ӵ�LinearLayout����ʾ
							linearLayout.addView(view);
							//������������ӵ����������ʾ����
							TextView tv=(TextView) view.findViewById(R.id.tv_privateChat_left);
							tv.setText(body);
						}
						
						//������ʾ���µ�����
						//����LinearLayout addView֮�󣬼���LinnearLayout�����Ǽ������һ��VIew�ĸ߶ȣ�
						//����һ���ӳ٣�����һ��Ҫ��һ�������Ҫ�����ĸ߶�
						new Thread(){
							public void run() {
								handler.sendEmptyMessage(-1);
							};
						}.start();
						
					}
				}
			}

		}

	}
}
