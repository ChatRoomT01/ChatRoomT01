package com.tarena.xmpp.adapter;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterEntry;

import com.tarena.xmpp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter {
	
	LayoutInflater inflater;
	ArrayList<RosterEntry> friends;
	
	public FriendListAdapter(Context context,ArrayList<RosterEntry> friends) {
		super();
		this.inflater = LayoutInflater.from(context);
		setFriends(friends);
	}

	/**
	 * 设置列表的数据
	 * @param friends
	 */
	public void setFriends(ArrayList<RosterEntry> friends){
		if(friends!=null){
			this.friends=friends;
		}else{
			this.friends=new ArrayList<RosterEntry>();
		}
		//通知适配器数据已经改变
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return friends.size();
	}

	@Override
	public RosterEntry getItem(int position) {
		return friends.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.friend_list_item, null);
			holder=new ViewHolder();
			holder.tvName=(TextView) convertView.findViewById(
					R.id.tv_friend_list_item_name);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.tvName.setText(getItem(position).getName());
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tvName;
	}

}
