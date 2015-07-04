package com.hpsvse.tangshi;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class WelcomeActivity extends Activity {

	private static final long DELAY_TIME = 2000;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				transView();
			}
		}, DELAY_TIME);
		// 如果服务没有运行则启动服务
		if (isStartMainService()) {
			startService(new Intent(WelcomeActivity.this, MainService.class));
		}
	}

	/**
	 * 判断是否需要启动服务
	 * @return	true 需要    	false 不需要
	 */
	private boolean isStartMainService() {
		SharedPreferences sp = this.getSharedPreferences("runningService", Context.MODE_PRIVATE);
		if (sp.getBoolean("mainService", true)) {
			return true;
		}
		return false;
	}

	/**
	 * 切换到下一个页面
	 */
	public void transView() {
		startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
		// 设置界面退出和进入动画
		this.overridePendingTransition(R.anim.view_move_left_show,R.anim.view_move_left_hide);
		finish();
	}

}
