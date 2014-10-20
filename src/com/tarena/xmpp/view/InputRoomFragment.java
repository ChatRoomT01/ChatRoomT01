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
			//ע��㲥������
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
		
		//��ʼ��
		setupView(view);
		//��Ӽ�����
		setListener();
		return view;
	}
	
	/**
	 * ��ʼ��
	 */
	private void setupView(View view) {
		//��ȡ�ؼ�������
		btnEnterRoom=(Button) view.findViewById(R.id.btn_inputRoom_in);
		etRoomName=(EditText) view.findViewById(R.id.et_inputRoom_roomName);
		
		groupChatBiz=new GroupChatBiz();
	}
	
	/**
	 * ��Ӽ�����
	 */
	private void setListener() {
		btnEnterRoom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					//��ȡ�û����������
					String roomName=etRoomName.getText().toString();
					//���зǿ��ж�
					if(Tools.isNull(roomName)){
						Tools.showInfo(getActivity(), "����������������");
						return;
					}
					//����GroupChatBiz����������
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
			//ע���㲥������
			getActivity().unregisterReceiver(receiver);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
	/**
	 * ���������ҹ㲥������
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
					Tools.showInfo(getActivity(), "�ɹ�����������");
					Intent intent2=new Intent(getActivity(),GroupChatActivity.class);
					getActivity().startActivity(intent2);
				}else{
					Tools.showInfo(getActivity(), "û�и�������");
				}
			}
		}
		
	}
}
