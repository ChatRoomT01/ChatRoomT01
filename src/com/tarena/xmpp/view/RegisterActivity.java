package com.tarena.xmpp.view;

import java.util.HashMap;

import com.tarena.xmpp.R;
import com.tarena.xmpp.model.RegisterBiz;
import com.tarena.xmpp.model.TApplication;
import com.tarena.xmpp.util.BaseActivity;
import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.Tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements OnClickListener{
	
	private Button btnLogin,btnRegister;
	private EditText etUsername,EtPassword,EtConfirmPassword,EtNick;
	private RegisterBiz registerBiz;
	private RegisterReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		try {
			registerBiz=new RegisterBiz();
			
			//初始化
			setupView();
			//添加监听器
			addListener();
			
			//创建广播接收器
			receiver=new RegisterReceiver();
			IntentFilter filter=new IntentFilter(TApplication.ACTION_REGISTER);
			registerReceiver(receiver, filter);
			
			/*//测试RegisterBiz是否正确
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("name", "cm");
			registerBiz.register("ccm", "123456", map);*/
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//注销广播接收器的注册
		unregisterReceiver(receiver);
	}

	//初始化界面
	private void setupView() {
		//获取控件的引用
		btnLogin=(Button) findViewById(R.id.btn_register_login);
		btnRegister=(Button) findViewById(R.id.btn_register_register);
		etUsername=(EditText) findViewById(R.id.et_register_username);
		EtPassword=(EditText) findViewById(R.id.et_register_password);
		EtConfirmPassword=(EditText) findViewById(R.id.et_register_confirmPassword);
		EtNick=(EditText) findViewById(R.id.et_register_nick);
		
	}

	/**
	 * 给控件添加监听事件
	 */
	private void addListener() {
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.btn_register_login://登录按钮
				//页面跳转
				break;
			case R.id.btn_register_register://注册按钮
				//获取用户输入的数据
				String username=etUsername.getText().toString();
				String password=EtPassword.getText().toString();
				String confirmPassword=EtConfirmPassword.getText().toString();
				String nick=EtNick.getText().toString();
				
				//对用户输入的数据进行非空验证
				if(Tools.isNull(username)){//用户名为空
					Tools.showInfo(RegisterActivity.this,"用户名不能为空");
					return;
				}
				if(Tools.isNull(password)){//密码为空
					Tools.showInfo(this,"密码不能为空");
					return;
				}
				if(Tools.isNull(confirmPassword)){//确认密码为空
					Tools.showInfo(this,"确认密码不能为空");
					return;
				}
				if(Tools.isNull(nick)){//昵称为空
					Tools.showInfo(this,"昵称不能为空");
					return;
				}
				
				if(!password.equals(confirmPassword)){
					Tools.showInfo(this,"输入的两次密码不相同，请重新输入");
					return;
				}
				//将数据交给RegisterBiz处理
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("name", nick);
				registerBiz.register(username, confirmPassword, map);
				break;
			}
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
	/**
	 * 广播接收器
	 */
	class RegisterReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action=intent.getAction();
			if(TApplication.ACTION_REGISTER.equals(action)){
				//获取广播携带的数据
				boolean isRegistered=intent.getBooleanExtra(
						TApplication.KEY_IS_SUCCESS, false);
				if(isRegistered){//注册成功
					Tools.showInfo(RegisterActivity.this,"注册成功");
					Intent i=new Intent(RegisterActivity.this, LoginActivity.class);
					startActivity(i);
					RegisterActivity.this.finish();
				}else{//注册失败
					String msg=intent.getStringExtra(TApplication.KEY_EXTRA_MESSAGE);
					if("".equals(msg)|| msg==null){
						msg="注册失败";
					}
					Tools.showInfo(RegisterActivity.this,msg);
				}
			}
			
		}
		
	}
}
