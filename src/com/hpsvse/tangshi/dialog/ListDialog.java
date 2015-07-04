package com.hpsvse.tangshi.dialog;

import java.util.List;

import com.hpsvse.tangshi.R;
import com.hpsvse.tangshi.entity.Type;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListDialog extends MyDialog {

	Context context;
	List<Type> list;
	ListView lv_show;
	
	public ListDialog(Context context, int theme, List<Type> list) {
		super(context, theme);
		this.context = context;
		this.list = list;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.list_dialog);
		lv_show = (ListView) findViewById(R.id.lv_show);
		lv_show.setAdapter(new DialogAdapter());
		lv_show.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Type type = (Type) parent.getItemAtPosition(position);
				String idStr = type.getId() + "";
				Toast.makeText(context, "idStr = " + idStr, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	class DialogAdapter extends BaseAdapter{

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
}
