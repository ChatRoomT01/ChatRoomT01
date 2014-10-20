package com.tarena.xmpp.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.tarena.xmpp.R;
import com.tarena.xmpp.model.AddFriendBiz;
import com.tarena.xmpp.model.TApplication;
import com.tarena.xmpp.util.BaseActivity;
import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.Tools;

public class AddFriendActivity extends BaseActivity {
	Button btnAdd,btnBack;
	
	EditText etUsername,etNick,EtGroup;
	
	AddFriendBiz addFriendBiz;
	
	AddFriendReceiver addFriendReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			setContentView(R.layout.add_friend);
			
			//��ʼ��
			setupView();
			//���ü�����
			addListener();
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(addFriendReceiver);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	/**
	 * ��ʼ��
	 */
	private void setupView() {
		//��ȡ�ؼ�������
		btnAdd=(Button) findViewById(R.id.btn_addFriend_add);
		btnBack=(Button) findViewById(R.id.btn_addFriend_back);
		
		etUsername=(EditText) findViewById(R.id.et_addFriend_username);
		etNick=(EditText) findViewById(R.id.et_addFriend_nick);
		EtGroup=(EditText) findViewById(R.id.et_addFriend_group);
		
		addFriendBiz=new AddFriendBiz();
		
		//�����㲥������
		addFriendReceiver=new AddFriendReceiver();
		IntentFilter filter=new IntentFilter(TApplication.ACTION_ADD_FRIEND);
		registerReceiver(addFriendReceiver, filter);
	}
	
	/**
	 * ��Ӽ�����
	 */
	private void addListener() {
		//��Ӱ�ť�ļ����¼�
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ��ȡ�û����������
				String username = etUsername.getText().toString();
				String name = etNick.getText().toString();
				String group = EtGroup.getText().toString();

				// �����ݽ��зǿ���֤
				if (Tools.isNull(username)) {
					Tools.showInfo(AddFriendActivity.this, "�û�������Ϊ��");
					return;
				}
				if (Tools.isNull(name)) {
					Tools.showInfo(AddFriendActivity.this, "�ǳƲ���Ϊ��");
					return;
				}
				if (Tools.isNull(group)) {
					Tools.showInfo(AddFriendActivity.this, "��������Ϊ��");
					return;
				}
				String[] groups = new String[] { group };

				// �����ݽ���AddFriendBiz����
				addFriendBiz.addFriend(username, name, groups);
			}
		});
		
		//���ذ�ť�ļ����¼�
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddFriendActivity.this.finish();
				TApplication.allActivities.remove(AddFriendActivity.this);
			}
		});
	}

/**
 * ��Ӻ��ѹ㲥������
 * @author chencaimei
 *
 */
class AddFriendReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		String action=intent.getAction();
		if(TApplication.ACTION_ADD_FRIEND.equals(action)){
			//��ȡ���͹���������
			boolean isSuccess=intent.getBooleanExtra(TApplication.KEY_IS_SUCCESS, false);
			//�����ݽ��д���
			if(isSuccess){
				Tools.showInfo(AddFriendActivity.this, "�����ͳɹ�");
			}else{
				Tools.showInfo(AddFriendActivity.this, "������ʧ�ܣ������·���");
				
			}
		}
	}
	
}
}
