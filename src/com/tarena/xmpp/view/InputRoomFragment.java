package com.tarena.xmpp.view;

import com.tarena.xmpp.R;
import com.tarena.xmpp.model.GroupChatBiz;
import com.tarena.xmpp.model.TApplication;
import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.Tools;

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
import android.widget.Button;
import android.widget.EditText;

public class InputRoomFragment extends Fragment {
	
	Button btnEnterRoom;
	EditText etRoomName;
	
	GroupChatBiz groupChatBiz;
	
	InputRoomReceiver receiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			//注册广播接收器
			receiver=new InputRoomReceiver();
			IntentFilter filter=new IntentFilter(TApplication.ACTION_ENTER_GROUP_CHAT);
			getActivity().registerReceiver(receiver, filter);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.input_room, null);
		
		//初始化
		setupView(view);
		//添加监听器
		setListener();
		return view;
	}
	
	/**
	 * 初始化
	 */
	private void setupView(View view) {
		//获取控件的引用
		btnEnterRoom=(Button) view.findViewById(R.id.btn_inputRoom_in);
		etRoomName=(EditText) view.findViewById(R.id.et_inputRoom_roomName);
		
		groupChatBiz=new GroupChatBiz();
	}
	
	/**
	 * 添加监听器
	 */
	private void setListener() {
		btnEnterRoom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					//获取用户输入的数据
					String roomName=etRoomName.getText().toString();
					//进行非空判断
					if(Tools.isNull(roomName)){
						Tools.showInfo(getActivity(), "请输入聊天室名称");
						return;
					}
					//调用GroupChatBiz进入聊天室
					groupChatBiz.joinRoom(roomName);
				} catch (Exception e) {
					ExceptionUtils.handle(e);
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		try {
			//注销广播接收器
			getActivity().unregisterReceiver(receiver);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
	/**
	 * 进入聊天室广播接收器
	 * @author chencaimei
	 *
	 */
	class InputRoomReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action=intent.getAction();
			if(TApplication.ACTION_ENTER_GROUP_CHAT.equals(action)){
				boolean isSuccess=intent.getBooleanExtra(TApplication.KEY_IS_SUCCESS, false);
				
				if(isSuccess){
					Tools.showInfo(getActivity(), "成功进入聊天室");
					Intent intent2=new Intent(getActivity(),GroupChatActivity.class);
					getActivity().startActivity(intent2);
				}else{
					Tools.showInfo(getActivity(), "没有该聊天室");
				}
			}
		}
		
	}
}
