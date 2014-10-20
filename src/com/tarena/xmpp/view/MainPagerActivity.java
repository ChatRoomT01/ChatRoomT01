package com.tarena.xmpp.view;

import java.util.ArrayList;

import com.tarena.xmpp.R;
import com.tarena.xmpp.adapter.MainPagerAdapter;
import com.tarena.xmpp.model.TApplication;
import com.tarena.xmpp.util.ExceptionUtils;
import com.tarena.xmpp.util.LogUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainPagerActivity extends FragmentActivity
implements OnPageChangeListener, OnClickListener{
	
	ViewPager viewPager;
	Button[] btnArray=new Button[4];
	ArrayList<Fragment> list;
	MainPagerAdapter adapter;
	
	int currentPageIndex=0;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		try {
			setContentView(R.layout.main);
			TApplication.allActivities.add(this);
			LogUtils.i("exit app", "add to TApplicaiton:"+this);
			
			//��ʼ��
			setupView();
			setButtonColor();
			//���ü����¼�
			setListener();
			
		} catch (Exception e) {
			ExceptionUtils.handle(e);
		}
	}

	/**
	 * ��ʼ��
	 */
	private void setupView() {
		//��ȡ�ؼ�������
		viewPager=(ViewPager) findViewById(R.id.vp_main_viewPager);
		btnArray[0]=(Button) findViewById(R.id.btn_main_friend);
		btnArray[1]=(Button) findViewById(R.id.btn_main_groupTalk);
		btnArray[2]=(Button) findViewById(R.id.btn_main_topic);
		btnArray[3]=(Button) findViewById(R.id.btn_main_more);
		
		//׼��Ҫ��ʾ�Ľ���
		list=new ArrayList<Fragment>();
		list.add(new GroupListFragment());
		list.add(new InputRoomFragment());
		list.add(new TopicFragment());
		list.add(new MoreFragment());
		
		//����������
		adapter=new MainPagerAdapter(getSupportFragmentManager(), list);
		
		//���������
		viewPager.setAdapter(adapter);
		
	}

	/**
	 * ���ü����¼�
	 */
	private void setListener() {
		//viewPagerҳ��ı�ļ����¼�
		viewPager.setOnPageChangeListener(this);
		
		//��ť�ļ����¼�
		for(Button btn:btnArray){
			btn.setOnClickListener(this);
		}
		
	}

	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		this.currentPageIndex=position;
		//�ı䰴ť����ɫ
		setButtonColor();
		
	}
	
	/**
	 * �ı䰴ť�����ֵ���ɫ
	 */
	public void setButtonColor(){
		for(int i=0;i<btnArray.length;i++){
			if(i==this.currentPageIndex){
				btnArray[i].setTextColor(0xffffffff);
			}else{
				btnArray[i].setTextColor(0xff000000);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_main_friend:
			this.currentPageIndex=0;
			break;
		case R.id.btn_main_groupTalk:
			this.currentPageIndex=1;
			break;
		case R.id.btn_main_topic:
			this.currentPageIndex=2;
			break;
		case R.id.btn_main_more:
			this.currentPageIndex=3;
			break;
		}
		//���õ�ǰ��ʾ��ҳ��
		this.viewPager.setCurrentItem(currentPageIndex);
		//�ı䰴ť����ɫ
		setButtonColor();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){//���·��ؼ�
			//����Ի���
			AlertDialog.Builder builder=new Builder(MainPagerActivity.this);
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
