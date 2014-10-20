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
			
			//初始化
			setupView();
			//设置监听器
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
	 * 初始化
	 */
	private void setupView() {
		//获取控件的引用
		btnAdd=(Button) findViewById(R.id.btn_addFriend_add);
		btnBack=(Button) findViewById(R.id.btn_addFriend_back);
		
		etUsername=(EditText) findViewById(R.id.et_addFriend_username);
		etNick=(EditText) findViewById(R.id.et_addFriend_nick);
		EtGroup=(EditText) findViewById(R.id.et_addFriend_group);
		
		addFriendBiz=new AddFriendBiz();
		
		//创建广播接收器
		addFriendReceiver=new AddFriendReceiver();
		IntentFilter filter=new IntentFilter(TApplication.ACTION_ADD_FRIEND);
		registerReceiver(addFriendReceiver, filter);
	}
	
	/**
	 * 添加监听器
	 */
	private void addListener() {
		//添加按钮的监听事件
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取用户输入的数据
				String username = etUsername.getText().toString();
				String name = etNick.getText().toString();
				String group = EtGroup.getText().toString();

				// 对数据进行非空验证
				if (Tools.isNull(username)) {
					Tools.showInfo(AddFriendActivity.this, "用户名不能为空");
					return;
				}
				if (Tools.isNull(name)) {
					Tools.showInfo(AddFriendActivity.this, "昵称不能为空");
					return;
				}
				if (Tools.isNull(group)) {
					Tools.showInfo(AddFriendActivity.this, "组名不能为空");
					return;
				}
				String[] groups = new String[] { group };

				// 将数据交给AddFriendBiz处理
				addFriendBiz.addFriend(username, name, groups);
			}
		});
		
		//返回按钮的监听事件
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddFriendActivity.this.finish();
				TApplication.allActivities.remove(AddFriendActivity.this);
			}
		});
	}

/**
 * 添加好友广播接收器
 * @author chencaimei
 *
 */
class AddFriendReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		String action=intent.getAction();
		if(TApplication.ACTION_ADD_FRIEND.equals(action)){
			//获取传送过来的数据
			boolean isSuccess=intent.getBooleanExtra(TApplication.KEY_IS_SUCCESS, false);
			//对数据进行处理
			if(isSuccess){
				Tools.showInfo(AddFriendActivity.this, "请求发送成功");
			}else{
				Tools.showInfo(AddFriendActivity.this, "请求发送失败，请重新发送");
				
			}
		}
	}
	
}
}
