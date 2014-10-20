package com.tarena.xmpp.model;

import java.util.HashMap;

import org.jivesoftware.smack.AccountManager;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.util.Log;

public class RegisterBiz {
	
	/**
	 * ע��
	 * @param username
	 * @param password
	 * @param map
	 */
	public void register(final String username,final String password,
			final HashMap<String,String> map){
		
		new Thread(){
			public void run() {
				try {
					//�жϷ������Ƿ�������
					int count=0;
					while(count<6000 && TApplication.isConnected==false){
						//����û���������߳���Ϣ
						Log.i("threadid", "�ȴ�������ͨ");
						Thread.sleep(10);
						count++;
					}
					
					//����������˾ͽ���ע�����
					if(TApplication.xmppConnection.isConnected()){
						AccountManager manager=
								TApplication.xmppConnection.getAccountManager();
						manager.createAccount(username, password, map);
						//���ﲻ����ע��ɹ�����ʧ�ܶ��������κζ���
						//���ע��ɹ��������������ִ�У����ע��ʧ�ܾͻ����Exception
						//���㲥
						Log.i("threadid", "ע��ɹ�");
						Intent intent=new Intent(TApplication.ACTION_REGISTER);
						intent.putExtra(TApplication.KEY_IS_SUCCESS, true);
						TApplication.instance.sendBroadcast(intent);
					}else{
						//���û�������Ͼͷ��㲥˵��������û������
						Log.i("threadid", "ע��ʧ�ܣ����粻ͨ");
						Intent intent=new Intent(TApplication.ACTION_REGISTER);
						intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
						intent.putExtra(TApplication.KEY_EXTRA_MESSAGE, "ע��ʧ�ܣ��������������");
						TApplication.instance.sendBroadcast(intent);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					//�û����Ѵ���
					Log.i("threadid", "ע��ʧ�ܣ����û��Ѵ���");
					Intent intent=new Intent(TApplication.ACTION_REGISTER);
					intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
					intent.putExtra(TApplication.KEY_EXTRA_MESSAGE, "ע��ʧ��,���û����Ѵ���");
					TApplication.instance.sendBroadcast(intent);
				}
			};
		}.start();
		
	}

}
