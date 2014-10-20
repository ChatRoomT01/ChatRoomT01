package com.tarena.xmpp.model;

import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.LogUtils;

import android.content.Intent;
import android.util.Log;

public class LoginBiz {
	
	public void login(final String username,final String password){
		
		//����һ���µ��̣߳���������¼
		new Thread(){
			public void run() {
				LogUtils.i("LoginBiz", "currentUser="+username);
				
				Intent intent=new Intent(TApplication.ACTION_LOGIN);
				try {
					Log.i("threadid", "login biz threadId="+Thread.currentThread().getId());
					//һ������������asmack���
					//OutputStream
					//InputStream
					//parser
					//���㲥
					
					Log.i("threadid", "׼����¼");
					//�ж��Ƿ��Ѿ��ͷ�����������
					int count=0;
					//ÿ��0.1�����Ƿ��Ѿ��ͷ������������ˣ����1�����ڻ������Ӳ����򷢹㲥˵���Ӳ��Ϸ����������ټ������е�¼����
					long startTime=System.currentTimeMillis();
					while(count<220 && TApplication.xmppConnection.isConnected()==false){
						Thread.sleep(100);
						count++;
//						Log.i("threadid", "login biz count="+count);
					}
					long endTime=System.currentTimeMillis();
					Log.i("threadid", "��¼ռ�õ�ʱ��="+(endTime-startTime));
					
					
//					Log.i("threadid", "�������Ƿ�������="+TApplication.xmppConnection.isConnected());
					if(TApplication.xmppConnection.isConnected()){//�������Ѿ���������
						Log.i("threadid", "���ӳɹ�");
						TApplication.xmppConnection.login(username, password);
						
						//�ж��Ƿ��¼�ɹ�
						TApplication.isLoginSuccess=TApplication.xmppConnection.isAuthenticated();
						intent.putExtra(TApplication.KEY_IS_SUCCESS,TApplication.isLoginSuccess);
//						TApplication.instance.sendBroadcast(intent);
						
						//�����¼�ɹ����û���������洢��TApplication��
						if(TApplication.isLoginSuccess){
							TApplication.saveUsername(username);
							TApplication.savePassword(password);
							
						}
						
//						Log.i("threadid", "��¼�ɹ�");
					}else{//1���������������Ӳ���
						//���㲥
						LogUtils.i("threadid", "��¼��ʱ");
						intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
						intent.putExtra(TApplication.KEY_EXTRA_MESSAGE, "��¼ʧ�ܣ���������");
					}
					
				} catch (Exception e) {
					LogUtils.i("threadid", "���������ά��");
					intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
					intent.putExtra(TApplication.KEY_EXTRA_MESSAGE, "��¼ʧ�ܣ����������ά��");
					ExceptionUtils.handle(e);
				}
				//���㲥
				TApplication.instance.sendBroadcast(intent);
			};
		}.start();
	}

}
