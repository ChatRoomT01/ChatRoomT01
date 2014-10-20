package com.tarena.xmpp.view;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterGroup;

import com.tarena.xmpp.R;
import com.tarena.xmpp.adapter.GroupListAdapter;
import com.tarena.xmpp.model.TApplication;
import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.LogUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;

public class GroupListFragment extends Fragment {
	private Button btnAdd;
	private ListView listView;
	
	private GroupListAdapter groupListAdapter;
	ArrayList<RosterGroup> list;
	
	GroupListReceiver receiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			//ע��㲥������
			receiver=new GroupListReceiver();
			IntentFilter filter=new IntentFilter(TApplication.ACTION_GROUP_LIST_CHANGE);
			this.getActivity().registerReceiver(receiver, filter);
			
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.group_list, null);
		
		//��ʼ��
		setupView(view);
		//��Ӽ�����
		addListener();
		return view;
	}
	/**
	 * ��ʼ��
	 */
	private void setupView(View view) {
		btnAdd=(Button) view.findViewById(R.id.btn_groupList_addFriend);
		listView=(ListView) view.findViewById(R.id.lv_groupList_group);
		
		//��¼�ɹ�-->LoginActivity-->GroupListFragment-->
		//��ȡ������Ϣ
		list=new ArrayList<RosterGroup>(TApplication.xmppConnection.
				getRoster().getGroups());
		//����������
		groupListAdapter=new GroupListAdapter(this.getActivity(), list);
		//���������
		listView.setAdapter(groupListAdapter);
	}
	
	/**
	 * ��Ӽ�����
	 */
	private void addListener() {
		//��Ӻ��Ѱ�ť�ļ����¼�
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),AddFriendActivity.class);
				startActivity(intent);
			}
		});
		
		//�������б���Ӽ�����
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					//��ת�������б����
					Intent intent=new Intent(getActivity(),FriendListActivity.class);
					//���������͹�ȥ
					intent.putExtra(TApplication.KEY_EXTRA_GROUP_NAME, 
							groupListAdapter.getItem(position).getName());
					startActivity(intent);
					LogUtils.i("groupList", "������ת");
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			}
		});
		
//		LogUtils.i("groupList", "ListView��������ӳɹ�");
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			//ע���㲥������
			this.getActivity().unregisterReceiver(receiver);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
/**
 * ������б����ݷ����ı�
 * @author chencaimei
 *
 */
class GroupListReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		String action=intent.getAction();
		if(TApplication.ACTION_GROUP_LIST_CHANGE.equals(action)){
			//LogUtils.i("updategroup", "�յ����·���㲥");
			//��ȡ����
			list=new ArrayList<RosterGroup>(
					TApplication.xmppConnection.getRoster().getGroups());
			//�����ݽ���������
			groupListAdapter.dateChange(list);
		}
	}
	
}
}
