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
			
			//��ʼ��
			setupView();
			//��Ӽ�����
			addListener();
			
			//�����㲥������
			receiver=new RegisterReceiver();
			IntentFilter filter=new IntentFilter(TApplication.ACTION_REGISTER);
			registerReceiver(receiver, filter);
			
			/*//����RegisterBiz�Ƿ���ȷ
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
		//ע���㲥��������ע��
		unregisterReceiver(receiver);
	}

	//��ʼ������
	private void setupView() {
		//��ȡ�ؼ�������
		btnLogin=(Button) findViewById(R.id.btn_register_login);
		btnRegister=(Button) findViewById(R.id.btn_register_register);
		etUsername=(EditText) findViewById(R.id.et_register_username);
		EtPassword=(EditText) findViewById(R.id.et_register_password);
		EtConfirmPassword=(EditText) findViewById(R.id.et_register_confirmPassword);
		EtNick=(EditText) findViewById(R.id.et_register_nick);
		
	}

	/**
	 * ���ؼ���Ӽ����¼�
	 */
	private void addListener() {
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.btn_register_login://��¼��ť
				//ҳ����ת
				break;
			case R.id.btn_register_register://ע�ᰴť
				//��ȡ�û����������
				String username=etUsername.getText().toString();
				String password=EtPassword.getText().toString();
				String confirmPassword=EtConfirmPassword.getText().toString();
				String nick=EtNick.getText().toString();
				
				//���û���������ݽ��зǿ���֤
				if(Tools.isNull(username)){//�û���Ϊ��
					Tools.showInfo(RegisterActivity.this,"�û�������Ϊ��");
					return;
				}
				if(Tools.isNull(password)){//����Ϊ��
					Tools.showInfo(this,"���벻��Ϊ��");
					return;
				}
				if(Tools.isNull(confirmPassword)){//ȷ������Ϊ��
					Tools.showInfo(this,"ȷ�����벻��Ϊ��");
					return;
				}
				if(Tools.isNull(nick)){//�ǳ�Ϊ��
					Tools.showInfo(this,"�ǳƲ���Ϊ��");
					return;
				}
				
				if(!password.equals(confirmPassword)){
					Tools.showInfo(this,"������������벻��ͬ������������");
					return;
				}
				//�����ݽ���RegisterBiz����
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
	 * �㲥������
	 */
	class RegisterReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action=intent.getAction();
			if(TApplication.ACTION_REGISTER.equals(action)){
				//��ȡ�㲥Я��������
				boolean isRegistered=intent.getBooleanExtra(
						TApplication.KEY_IS_SUCCESS, false);
				if(isRegistered){//ע��ɹ�
					Tools.showInfo(RegisterActivity.this,"ע��ɹ�");
					Intent i=new Intent(RegisterActivity.this, LoginActivity.class);
					startActivity(i);
					RegisterActivity.this.finish();
				}else{//ע��ʧ��
					String msg=intent.getStringExtra(TApplication.KEY_EXTRA_MESSAGE);
					if("".equals(msg)|| msg==null){
						msg="ע��ʧ��";
					}
					Tools.showInfo(RegisterActivity.this,msg);
				}
			}
			
		}
		
	}
}
