package com.tarena.xmpp.view;

import com.tarena.xmpp.R;
import com.tarena.xmpp.model.TApplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MoreFragment extends Fragment {
	
	Button btnExit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.more, null);
		
		//初始化界面
		setupView(view);
		
		//添加监听器
		addListener();
		return view;
	}
	/**
	 * 初始化界面
	 * @param view
	 */
	private void setupView(View view) {
		//获取控件的引用
		btnExit=(Button) view.findViewById(R.id.btn_more_exit);
		
	}
	
	/**
	 * 添加监听器
	 */
	private void addListener() {
		//退出按钮的监听事件
		this.btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TApplication.exit();
			}
		});
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
