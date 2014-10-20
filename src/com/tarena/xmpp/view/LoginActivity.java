package com.tarena.xmpp.view;


import com.tarena.xmpp.R;
import com.tarena.xmpp.model.LoginBiz;
import com.tarena.xmpp.model.TApplication;
import com.tarena.xmpp.util.BaseActivity;
import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.NetworkUtils;
import com.tarena.xmpp.util.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener{
	
	private EditText EtName,EtPassword;
	private Button btnSubmit;
	private Button btnRegister;
	
	private LoginBiz loginBiz;
	
	private LoginReceiver loginReceiver;//广播接收器
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			setContentView(R.layout.login);
			
			//检查网络状态
			NetworkUtils networkUtils=new NetworkUtils();
			networkUtils.checkedNetworkState(this);
			
			//注册广播接收器
			loginReceiver=new LoginReceiver();
			IntentFilter filter=new IntentFilter();
			filter.addAction(TApplication.ACTION_LOGIN);
			registerReceiver(loginReceiver, filter);
			
			//初始化
			setupView();
			//设置监听事件
			setListener();
			
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
	/**初始化界面*/
	private void setupView() {
		//获取界面上的控件的使用
		EtName=(EditText) findViewById(R.id.et_login_username);
		EtPassword=(EditText) findViewById(R.id.et_login_password);
		btnSubmit=(Button) findViewById(R.id.btn_login_submit);
		btnRegister=(Button) findViewById(R.id.btn_login_register);
		
		//创建LoginBiz对象
		loginBiz=new LoginBiz();
	}

	/**设置监听器*/
	private void setListener() {
		btnSubmit.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_submit://提交按钮
			
			//获取用户输入的数据
			String username=EtName.getText().toString();
			String password=EtPassword.getText().toString();
			//对输入的数据进行非空验证
			if(Tools.isNull(username)){//用户名的非空验证
				Tools.showInfo(this, "用户名不能为空");
				return;
			}
			if(Tools.isNull(password)){//用户名的非空验证
				Tools.showInfo(this, "密码不能为空");
				return;
			}
			//将数据交给LoginBiz进行处理
			loginBiz.login(username, password);
			
			break;
			
		case R.id.btn_login_register://注册按钮
			Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent);

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			//取消广播接收器的注册
			unregisterReceiver(loginReceiver);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
	/**广播接收器*/
	class LoginReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(TApplication.ACTION_LOGIN.equals(intent.getAction())){//登录时发送过来的数据
				//获取传输过来的数据
				boolean isLoginSuccess=intent.getBooleanExtra(
						TApplication.KEY_IS_SUCCESS, false);
				//更新界面
				if(isLoginSuccess){//登录成功
					Tools.showInfo(context, "登录成功");
					
					//进入主界面
					Intent intent1=new Intent(LoginActivity.this,MainPagerActivity.class);
					startActivity(intent1);
				}else{//登录失败
//					Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
					//获取传输过来的错误信息提示
					String msg=intent.getStringExtra(TApplication.KEY_EXTRA_MESSAGE);
					if("".equals(msg) ||msg==null){
						Tools.showInfo(context, "登录失败，密码错误");
					}else{
						Tools.showInfo(context,msg);
					}
				}
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			//警告对话框
			AlertDialog.Builder builder=new Builder(LoginActivity.this);
			builder.setTitle("退出");
			builder.setMessage("你确定退出微信？");
			//退出
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					TApplication.exit();
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			AlertDialog dialog=builder.create();
			dialog.show();
		}
		
		return true;
	}
}
