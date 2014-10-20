package com.tarena.xmpp.view;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tarena.xmpp.R;
import com.tarena.xmpp.adapter.FriendListAdapter;
import com.tarena.xmpp.model.PrivateChatBiz;
import com.tarena.xmpp.model.TApplication;
import com.tarena.xmpp.util.BaseActivity;
import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.LogUtils;

public class FriendListActivity extends BaseActivity {
	ListView lv;
	
	String groupName;
	ArrayList<RosterEntry> friends;
	FriendListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.friend_list);

			//初始化
			setupView();
			
			//添加监听器
			setListener();

		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	/**
	 * 添加监听器
	 */
	private void setListener() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					//获取被点击的好友
					RosterEntry rosterEntry=adapter.getItem(position);
					//获取好友名称
					String to=rosterEntry.getName();
//					//要发送的消息
//					String body="测试";
//					//调用Biz发送消息
//					PrivateChatBiz privateChatBiz=new PrivateChatBiz();
//					privateChatBiz.sendMessage(to, body);
//					LogUtils.i("chat", "消息已发送");
					
					//进入聊天界面
					Intent intent=new Intent(FriendListActivity.this,
							PrivateChatActivity.class);
					//将当前点击的好友名称发送到聊天界面
					intent.putExtra(TApplication.KEY_EXTRA_FRIEND_NAME, to);
					startActivity(intent);
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			}
		});
		
	}

	/**
	 * 初始化
	 */
	private void setupView() {
		//获取控件的引用
		this.lv=(ListView) findViewById(R.id.lv_friendList);
		
		// 获取上个界面发送过来的组名信息
		groupName = getIntent().getStringExtra(
				TApplication.KEY_EXTRA_GROUP_NAME);
		LogUtils.i("friendList", "groupName=" + groupName);
		
		//获取该组的好友列表信息
		//获取该组
		RosterGroup group=TApplication.xmppConnection.
				getRoster().getGroup(groupName);
		//获取该组好友
		friends=new ArrayList<RosterEntry>(
				group.getEntries());
		
		//创建适配器
		adapter=new FriendListAdapter(this, friends);
		
		//添加适配器
		lv.setAdapter(adapter);

	}

}
