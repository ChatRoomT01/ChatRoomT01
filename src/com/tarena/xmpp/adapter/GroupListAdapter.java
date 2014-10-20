package com.tarena.xmpp.adapter;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterGroup;

import com.tarena.xmpp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class GroupListAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<RosterGroup> list;
	LayoutInflater inflater;
	
	

	public GroupListAdapter(Context context, ArrayList<RosterGroup> list) {
		super();
		inflater=LayoutInflater.from(context);
		setList(list);
	}
	
	/**
	 * 设置ListView的显示数据
	 * @param list
	 * @return 
	 */
	public void setList(ArrayList<RosterGroup> list){
		if(list==null){
			this.list=new ArrayList<RosterGroup>();
		}else{
			this.list=list;
		}
	}
	
	/**
	 * 数据发生改变
	 * @param list
	 */
	public void dateChange(ArrayList<RosterGroup> list){
		setList(list);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public RosterGroup getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView==null){
			convertView=inflater.inflate(R.layout.group_list_item, null);
			holder=new ViewHolder();
			holder.tvName=(TextView) convertView.findViewById(R.id.tv_groupListItem_groupName);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(list.get(position).getName());
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tvName;
	}

}
