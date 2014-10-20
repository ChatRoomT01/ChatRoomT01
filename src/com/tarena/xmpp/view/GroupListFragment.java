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
			//注册广播接收器
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
		
		//初始化
		setupView(view);
		//添加监听器
		addListener();
		return view;
	}
	/**
	 * 初始化
	 */
	private void setupView(View view) {
		btnAdd=(Button) view.findViewById(R.id.btn_groupList_addFriend);
		listView=(ListView) view.findViewById(R.id.lv_groupList_group);
		
		//登录成功-->LoginActivity-->GroupListFragment-->
		//获取分组信息
		list=new ArrayList<RosterGroup>(TApplication.xmppConnection.
				getRoster().getGroups());
		//创建适配器
		groupListAdapter=new GroupListAdapter(this.getActivity(), list);
		//添加适配器
		listView.setAdapter(groupListAdapter);
	}
	
	/**
	 * 添加监听器
	 */
	private void addListener() {
		//添加好友按钮的监听事件
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),AddFriendActivity.class);
				startActivity(intent);
			}
		});
		
		//给分组列表添加监听器
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					//跳转到好友列表界面
					Intent intent=new Intent(getActivity(),FriendListActivity.class);
					//把组名发送过去
					intent.putExtra(TApplication.KEY_EXTRA_GROUP_NAME, 
							groupListAdapter.getItem(position).getName());
					startActivity(intent);
					LogUtils.i("groupList", "进行跳转");
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			}
		});
		
//		LogUtils.i("groupList", "ListView监听器添加成功");
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			//注销广播接收器
			this.getActivity().unregisterReceiver(receiver);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
/**
 * 组分类列表数据发生改变
 * @author chencaimei
 *
 */
class GroupListReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		String action=intent.getAction();
		if(TApplication.ACTION_GROUP_LIST_CHANGE.equals(action)){
			//LogUtils.i("updategroup", "收到更新分组广播");
			//获取数据
			list=new ArrayList<RosterGroup>(
					TApplication.xmppConnection.getRoster().getGroups());
			//将数据交给适配器
			groupListAdapter.dateChange(list);
		}
	}
	
}
}
