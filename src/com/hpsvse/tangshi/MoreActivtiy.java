package com.hpsvse.tangshi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreActivtiy extends Activity {

	GridView gv_more;
	String[] names = new String[]{"唐诗三百首","宋词三百首","元曲三百首","古诗词分类赏析","小学古诗全文赏析","三字经"};
	int[] states = new int[]{R.drawable.ic_install,R.drawable.ic_noinstall};
	int[] imgs = new int[]{R.drawable.ic0,R.drawable.ic1,R.drawable.ic2,R.drawable.ic3,R.drawable.ic4,R.drawable.ic5};
	Button btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		gv_more = (GridView) findViewById(R.id.gv_more);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				back();
			}
		});
		gv_more.setAdapter(new GradAdapter());
		
	}

	private void back() {
		finish();
		this.overridePendingTransition(R.anim.view_move_right_show, R.anim.view_move_right_hide);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			back();
		}
		return true;
	}
	
	class GradAdapter extends BaseAdapter{
		
		Paint paint;
		Canvas canvas;
		
		public GradAdapter() {
			paint = new Paint();
			paint.setColor(Color.BLACK);
			//图片的合成模式
			paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_OVER));
			canvas = new Canvas();
		}

		@Override
		public int getCount() {
			return imgs.length;
		}

		@Override
		public Object getItem(int position) {
			return imgs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(MoreActivtiy.this).inflate(R.layout.item_grid, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.desc = (TextView) convertView.findViewById(R.id.desc);
				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
			}
			
			holder.img.setImageBitmap(getBitmap(position));
			holder.desc.setText(names[position]);
			return convertView;
		}

		class Holder{
			public ImageView img;
			public TextView desc;
		}
		
		/**
		 * 图片的合成
		 * @param position
		 * @return
		 */
		private Bitmap getBitmap(int position) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs[position]);
			Bitmap alertBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
			canvas.setBitmap(alertBitmap);
			canvas.drawBitmap(bitmap, new Matrix(), paint);
			
			Bitmap stateBitmap = null;
			if (position == 0) {
				stateBitmap = BitmapFactory.decodeResource(getResources(), states[0]);
			}else{
				stateBitmap = BitmapFactory.decodeResource(getResources(), states[1]);
			}
			Matrix matrix = new Matrix();
			matrix.setTranslate(68, 68);
			canvas.drawBitmap(stateBitmap, matrix, paint);
			
			return alertBitmap;
		}
	}
	
}
