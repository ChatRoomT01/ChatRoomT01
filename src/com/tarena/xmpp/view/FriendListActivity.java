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

			//��ʼ��
			setupView();
			
			//��Ӽ�����
			setListener();

		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	/**
	 * ��Ӽ�����
	 */
	private void setListener() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					//��ȡ������ĺ���
					RosterEntry rosterEntry=adapter.getItem(position);
					//��ȡ��������
					String to=rosterEntry.getName();
//					//Ҫ���͵���Ϣ
//					String body="����";
//					//����Biz������Ϣ
//					PrivateChatBiz privateChatBiz=new PrivateChatBiz();
//					privateChatBiz.sendMessage(to, body);
//					LogUtils.i("chat", "��Ϣ�ѷ���");
					
					//�����������
					Intent intent=new Intent(FriendListActivity.this,
							PrivateChatActivity.class);
					//����ǰ����ĺ������Ʒ��͵��������
					intent.putExtra(TApplication.KEY_EXTRA_FRIEND_NAME, to);
					startActivity(intent);
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			}
		});
		
	}

	/**
	 * ��ʼ��
	 */
	private void setupView() {
		//��ȡ�ؼ�������
		this.lv=(ListView) findViewById(R.id.lv_friendList);
		
		// ��ȡ�ϸ����淢�͹�����������Ϣ
		groupName = getIntent().getStringExtra(
				TApplication.KEY_EXTRA_GROUP_NAME);
		LogUtils.i("friendList", "groupName=" + groupName);
		
		//��ȡ����ĺ����б���Ϣ
		//��ȡ����
		RosterGroup group=TApplication.xmppConnection.
				getRoster().getGroup(groupName);
		//��ȡ�������
		friends=new ArrayList<RosterEntry>(
				group.getEntries());
		
		//����������
		adapter=new FriendListAdapter(this, friends);
		
		//���������
		lv.setAdapter(adapter);

	}

}
