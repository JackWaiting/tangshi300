package com.hpsvse.tangshi;

import java.io.File;

import com.hpsvse.tangshi.db.PoemDAO;
import com.hpsvse.tangshi.entity.Poem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ContentActivity extends Activity implements OnClickListener{

	private PoemDAO poemDAO;
	private TextView tv_title,tv_author,tv_content,tv_desc;
	private Button btn_back,btnRecord;
	private ImageButton ib_play;
	private ImageView iv_font_small,iv_font_big;
	private float initFontSize = 14,initTitleFontSize = 18,initContentFontSize = 20;
	private MediaRecorder recorder;
	public static File catalogFile;
	public int recordNum = 0;
	private SharedPreferences sp;
	String poemName = "";
	boolean flag = true;
	PopupWindow popupWindow;
	LinearLayout ll_root;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);
		poemDAO = new PoemDAO(this);
		sp = this.getSharedPreferences("recoder", Context.MODE_PRIVATE);
		loadFind();
		loadResource();
		loadLister();
		
		loadPopupWindow();
	}
	
	private void loadPopupWindow() {
		ll_root = (LinearLayout) findViewById(R.id.ll_root);
		View contentView = getLayoutInflater().inflate(R.layout.record_view,null);	// 填充窗口		
		popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);	
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.dialog_anim);	// 设置动画
	}

	/**
	 * 添加按钮的监听事件
	 */
	private void loadLister() {
		btn_back.setOnClickListener(this);
		iv_font_small.setOnClickListener(this);
		iv_font_big.setOnClickListener(this);
		ib_play.setOnClickListener(this);
	}

	Poem poem;
	/**
	 * 加载资源
	 */
	private void loadResource() {
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		String auth = intent.getStringExtra("auth");
		poem = poemDAO.getDetail(id);
		poemName = poem.getTitle();
		tv_title.setText(poemName);
		tv_author.setText("诗人：" + auth);
		tv_content.setText(Html.fromHtml(poem.getContent()));
		tv_desc.setText(Html.fromHtml(poem.getDesc()));
		
		recordNum = sp.getInt("recordNum", 0);
		// 初始化录音
		loadRecorder();
	}

	/**
	 * 加载录音资源
	 */
	public void loadRecorder() {
		catalogFile = new File(Environment.getExternalStorageDirectory() + "/tangshi/recorder");
		if (!catalogFile.exists()) {
			catalogFile.mkdirs();
		}
		loadRecordLister();
	}

	int recordTime = 0;
	File file = null;
	/**
	 * 录音的监听事件
	 */
	public void loadRecordLister() {
		btnRecord.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					recordStart(event);
					break;
					
				case MotionEvent.ACTION_UP:
					recordEnd(event);
					break;
				}
				return true;
			}

			private void recordStart(MotionEvent event) {
				try {
					popupWindow.showAtLocation(ll_root, Gravity.CENTER, 0, 0);	
					btnRecord.setText("放开结束录音");
					recordNum ++;
					file = new File(catalogFile , "record" + recordNum + ".amr");
					recorder = new MediaRecorder();
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);	
					recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					recorder.setOutputFile(file.getAbsolutePath());
					new Thread(){
						public void run() {
							timer();
						};
					}.start();
					recorder.prepare();
					recorder.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private void recordEnd(MotionEvent event) {
				flag = false;
				popupWindow.dismiss();
				btnRecord.setText("按住录音");
				recorder.stop();
				recorder.release();
				if (recordTime >= 3000) {
					// 录音成功
					sp.edit().putString(poemName, "record"+recordNum)
					.putInt("recordNum", recordNum).commit();
				}else{
					if (file.exists()) {
						file.delete();
					}
					recordNum--;
					Toast.makeText(ContentActivity.this, "录音时间过短",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	/**
	 * 计时器
	 */
	public void timer(){
		int num = 0;
		while(flag){
			try {
				Thread.sleep(100);
				num += 100;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		recordTime = num;
		System.out.println("--- 录音时长：" + recordTime + " ms");
	}

	/**
	 * 查找控件
	 */
	private void loadFind() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_author = (TextView) findViewById(R.id.tv_author);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		btn_back = (Button) findViewById(R.id.btn_back);
		iv_font_small = (ImageView) findViewById(R.id.iv_font_small);
		iv_font_big = (ImageView) findViewById(R.id.iv_font_big);
		btnRecord = (Button) findViewById(R.id.btnRecord);
		ib_play = (ImageButton) findViewById(R.id.ib_play);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			back();
			break;
		case R.id.iv_font_small:
			fontSmall();
			break;
		case R.id.iv_font_big:
			fontBig();
			break;
		case R.id.ib_play:
			playRecord(poem);
			break;
		}
	}

	MediaPlayer mediaPlayer;
	
	/**
	 * 播放录音
	 */
	private void playRecord(Poem poem) {
		try {
			mediaPlayer =new MediaPlayer();
			mediaPlayer.setDataSource(getFilePath(poem)); //设置数据源
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
			mediaPlayer = null;
			Toast.makeText(this, "没有录音", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 根据诗查找对应的录音文件
	 * @param poem2
	 * @return
	 */
	private String getFilePath(Poem poem2) {
		String fileName = sp.getString(poem2.getTitle(), "");
		return "/mnt/sdcard/tangshi/recorder/" + fileName + ".amr";
	}

	float fontSize = 0;
	float contentFontSize = 0;
	float titleFontSize = 0;
	float fontScope = 0;
	
	/**
	 * 增大字体
	 */
	private void fontBig() {
		fontScope ++;
		if (fontScope > 8) {
			fontScope = 8;
		}
		// 初始化字体大小
		fontSize = initFontSize;
		titleFontSize = initTitleFontSize;
		contentFontSize = initContentFontSize;
		alterFont();
	}

	/**
	 * 改变字体
	 */
	public void alterFont() {
		fontSize += fontScope;
		contentFontSize += fontScope;
		titleFontSize += fontScope;
		tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
		tv_author.setTextSize(fontSize);
		tv_desc.setTextSize(fontSize);
		tv_content.setTextSize(contentFontSize);
	}

	/**
	 * 减小字体
	 */
	private void fontSmall() {
		fontScope --;
		if (fontScope < -8) {
			fontScope = -8;
		}
		// 初始化字体大小
		fontSize = initFontSize;
		titleFontSize = initTitleFontSize;
		contentFontSize = initContentFontSize;
		alterFont();
	}

	/**
	 * 返回
	 */
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
}
