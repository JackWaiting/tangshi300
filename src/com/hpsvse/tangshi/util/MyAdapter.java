package com.hpsvse.tangshi.util;

import java.util.List;

import com.hpsvse.tangshi.R;
import com.hpsvse.tangshi.entity.Poem;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

	Context context;
	List<Poem> list;
	SharedPreferences sp;
	
	public MyAdapter(Context context, List<Poem> list) {
		this.context = context;
		this.list = list;
		sp = context.getSharedPreferences("recoder", Context.MODE_PRIVATE);
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
		Holder holder;
		Poem poem = list.get(position);
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_type  = (TextView) convertView.findViewById(R.id.tv_type);
			holder.iv_record  = (ImageView) convertView.findViewById(R.id.iv_record);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.tv_title.setText("《" + poem.getTitle() + "》 - " + poem.getAuth());
		holder.tv_type.setText(poem.getType());
		if (existRecord(poem)) {	// 判断该诗是否存在录音
			holder.iv_record.setVisibility(View.VISIBLE);	//设置可见
		}else{
			holder.iv_record.setVisibility(View.INVISIBLE);	//设置不可见
		}
		return convertView;
	}
	
	class Holder{
		public TextView tv_title;
		public TextView tv_type;
		public ImageView iv_record;
	}
	
	

	public boolean existRecord(Poem poem) {
		return !(sp.getString(poem.getTitle(), "").equals(""));
	}
	
}
