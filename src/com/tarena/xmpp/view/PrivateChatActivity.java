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

			// 初始化界面
			setupView();

			// 添加监听器
			setListener();

		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	/**
	 * 初始化
	 */
	private void setupView() {
		// 获取控件的引用
		tvFriendName = (TextView) findViewById(R.id.tv_privateChat_friendName);
		etBody = (EditText) findViewById(R.id.et_privateChat_body);
		btnSendMessage = (Button) findViewById(R.id.btn_privateChat_send);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout_privateChat);
		scrollView=(ScrollView) findViewById(R.id.scrollView_privateChat);

		// 获取上个界面传送过来的friendName
		friendName = getIntent().getStringExtra(
				TApplication.KEY_EXTRA_FRIEND_NAME);
		// 给空间添加内容
		tvFriendName.setText(friendName);

		privateChatBiz = new PrivateChatBiz();

		// 注册广播接收器
		receiver = new PrivateChatReceiver();
		IntentFilter filter = new IntentFilter(
				TApplication.ACTION_PRIVATE_CHAT_MESSAGE);
		registerReceiver(receiver, filter);
		
		handler=new Handler(){
			@Override
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					//往上拉
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
	 * 添加监听器
	 */
	private void setListener() {
		// 给发送按钮添加监听器
		btnSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// LogUtils.i("privatechat", "发送按钮监听成功");
					// 获取用户输入的内容
					String body = etBody.getText().toString();
					// 对输入的数据进行非空验证
					if (!Tools.isNull(body)) {
						// 调用PrivateChatBiz发送消息
						privateChatBiz.sendMessage(friendName, body);
						// 将编辑框内容清空
						etBody.getText().clear();
					}
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			}
		});
	}

	// 监听聊天记录的广播接收器，用户更新聊天界面，显示聊天信息
	class PrivateChatReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			//在屏幕上显示我说的内容和好友说的内容
			String action = intent.getAction();
			if (TApplication.ACTION_PRIVATE_CHAT_MESSAGE.equals(action)) {
//				// 获取发送消息的名字
//				String from = intent
//						.getStringExtra(TApplication.KEY_EXTRA_MESSAGE_FROM);
				// 获取该好友的聊天实体类
				Vector<Message> vector = PrivateChatEntity.map.get(friendName);

				if (vector != null) {
					// 遍历该实体，输出聊天内容
					linearLayout.removeAllViews();
					for (Message msg : vector) {
						LogUtils.i("PrivateChatReceiver", msg.getFrom()
								+ " 说  " + msg.getBody());
						
						//获取发送方
						String from=msg.getFrom();
						if(from.contains("@")){
							from=from.substring(0, from.indexOf("@"));
						}
						//获取聊天内容
						String body=msg.getBody();
						//在界面上显示聊天信息
						if(from.equals(TApplication.getUsername())){//当前用户发出的信息
							//创建一个VIew
							View view=View.inflate(PrivateChatActivity.this, 
									R.layout.dialog_right, null);
							//将View添加到LinearLayout中显示
							linearLayout.addView(view);
							//将聊天内容添加掉到组件上显示出来
							TextView tv=(TextView) view.findViewById(R.id.tv_privateChat_right);
							tv.setText(body);
						}else{//好友发送过来的信息
							//创建一个VIew
							View view=View.inflate(PrivateChatActivity.this, 
									R.layout.dialog_left, null);
							//将View添加到LinearLayout中显示
							linearLayout.addView(view);
							//将聊天内容添加掉到组件上显示出来
							TextView tv=(TextView) view.findViewById(R.id.tv_privateChat_left);
							tv.setText(body);
						}
						
						//滚动显示最新的内容
						//尽管LinearLayout addView之后，计算LinnearLayout并不是加上最后一个VIew的高度，
						//会有一点延迟，所以一点要迟一点计算需要滚动的高度
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
