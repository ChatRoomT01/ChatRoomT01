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
	
	private LoginReceiver loginReceiver;//�㲥������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			setContentView(R.layout.login);
			
			//�������״̬
			NetworkUtils networkUtils=new NetworkUtils();
			networkUtils.checkedNetworkState(this);
			
			//ע��㲥������
			loginReceiver=new LoginReceiver();
			IntentFilter filter=new IntentFilter();
			filter.addAction(TApplication.ACTION_LOGIN);
			registerReceiver(loginReceiver, filter);
			
			//��ʼ��
			setupView();
			//���ü����¼�
			setListener();
			
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
	/**��ʼ������*/
	private void setupView() {
		//��ȡ�����ϵĿؼ���ʹ��
		EtName=(EditText) findViewById(R.id.et_login_username);
		EtPassword=(EditText) findViewById(R.id.et_login_password);
		btnSubmit=(Button) findViewById(R.id.btn_login_submit);
		btnRegister=(Button) findViewById(R.id.btn_login_register);
		
		//����LoginBiz����
		loginBiz=new LoginBiz();
	}

	/**���ü�����*/
	private void setListener() {
		btnSubmit.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_submit://�ύ��ť
			
			//��ȡ�û����������
			String username=EtName.getText().toString();
			String password=EtPassword.getText().toString();
			//����������ݽ��зǿ���֤
			if(Tools.isNull(username)){//�û����ķǿ���֤
				Tools.showInfo(this, "�û�������Ϊ��");
				return;
			}
			if(Tools.isNull(password)){//�û����ķǿ���֤
				Tools.showInfo(this, "���벻��Ϊ��");
				return;
			}
			//�����ݽ���LoginBiz���д���
			loginBiz.login(username, password);
			
			break;
			
		case R.id.btn_login_register://ע�ᰴť
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
			//ȡ���㲥��������ע��
			unregisterReceiver(loginReceiver);
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}
	
	/**�㲥������*/
	class LoginReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(TApplication.ACTION_LOGIN.equals(intent.getAction())){//��¼ʱ���͹���������
				//��ȡ�������������
				boolean isLoginSuccess=intent.getBooleanExtra(
						TApplication.KEY_IS_SUCCESS, false);
				//���½���
				if(isLoginSuccess){//��¼�ɹ�
					Tools.showInfo(context, "��¼�ɹ�");
					
					//����������
					Intent intent1=new Intent(LoginActivity.this,MainPagerActivity.class);
					startActivity(intent1);
				}else{//��¼ʧ��
//					Toast.makeText(context, "��¼ʧ��", Toast.LENGTH_SHORT).show();
					//��ȡ��������Ĵ�����Ϣ��ʾ
					String msg=intent.getStringExtra(TApplication.KEY_EXTRA_MESSAGE);
					if("".equals(msg) ||msg==null){
						Tools.showInfo(context, "��¼ʧ�ܣ��������");
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
			//����Ի���
			AlertDialog.Builder builder=new Builder(LoginActivity.this);
			builder.setTitle("�˳�");
			builder.setMessage("��ȷ���˳�΢�ţ�");
			//�˳�
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					TApplication.exit();
				}
			});
			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
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
