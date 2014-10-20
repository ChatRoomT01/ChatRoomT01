package com.tarena.xmpp.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {
	
	private ArrayList<Fragment> list;

	public MainPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		if(list!=null){
			this.list=list;
		}else{
			this.list=new ArrayList<Fragment>();
		}
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
