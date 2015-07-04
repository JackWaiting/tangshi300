package com.hpsvse.tangshi.util;

import java.util.List;

import com.hpsvse.tangshi.R;
import com.hpsvse.tangshi.entity.Type;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PopupAdapter extends BaseAdapter {

	Context context;
	List<Type> list;
	
	public PopupAdapter(Context context,List<Type> list) {
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.list_dialog_item, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_type);
		tv.setText(list.get(position).getType());
		return view;
	}
}
