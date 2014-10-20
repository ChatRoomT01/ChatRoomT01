package com.tarena.xmpp.model;

import org.jivesoftware.smack.Roster;

import com.tarena.xmpp.util.LogUtils;

import android.content.Intent;
import android.util.Log;

public class AddFriendBiz {
	
	public void addFriend(final String username,final String name,final String[] groups){
		new Thread(){
			public void run() {
				try{
					
					//��������Ƿ����ӣ����û��������������
					if(!TApplication.xmppConnection.isConnected()){
						TApplication.connecteToServer();//����
					}
					
					/*��δ��벻ִ�У���Ϊ�ڵ�¼�������Ѿ��еȴ��������ӵĲ����ˣ��������ﲻ�����ȴ��Ĳ�����
					int countConnect=0;
					//while�ȴ���������
					while(countConnect<200 && !TApplication.xmppConnection.isConnected()){
						Thread.sleep(100);
						countConnect++;
					}*/
					
					//�������ӳɹ������Ƿ��¼,���û�е�¼�����µ�¼
					if(!TApplication.xmppConnection.isAuthenticated()){
						LoginBiz loginBiz=new LoginBiz();
						loginBiz.login(TApplication.getUsername(),TApplication.getPassword());
					}
					
					//while�ȴ���¼
					int countLogin=0;
					while(countLogin<600 && !TApplication.xmppConnection.isAuthenticated()){
						Thread.sleep(100);
						countLogin++;
					}
					
					if(TApplication.xmppConnection.isAuthenticated()){
						//��¼�ɹ�����Ӻ���
						//Roster�ǻ����ᣬ�����û��ĺ��ѷ���ͺ�����Ϣ
						Roster roster=TApplication.xmppConnection.getRoster();
						String user=username+"@tarena3gxmpp.com";
						roster.createEntry(user, name, groups);
						LogUtils.i("roster", "��Ӻ��ѳɹ�");
						
						//���͹㲥
						Intent intent=new Intent(TApplication.ACTION_ADD_FRIEND);
						intent.putExtra(TApplication.KEY_IS_SUCCESS, true);
						TApplication.instance.sendBroadcast(intent);
					}
					
				} catch (Exception e) {
					//���͹㲥˵��������Ϣ����ʧ��
					Intent intent=new Intent(TApplication.ACTION_ADD_FRIEND);
					intent.putExtra(TApplication.KEY_IS_SUCCESS, false);
					TApplication.instance.sendBroadcast(intent);
					
					LogUtils.i("roster","��Ӻ���ʧ��");
				}
			};
		}.start();
	}

}
