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
		// �������û����������������
		if (isStartMainService()) {
			startService(new Intent(WelcomeActivity.this, MainService.class));
		}
	}

	/**
	 * �ж��Ƿ���Ҫ��������
	 * @return	true ��Ҫ    	false ����Ҫ
	 */
	private boolean isStartMainService() {
		SharedPreferences sp = this.getSharedPreferences("runningService", Context.MODE_PRIVATE);
		if (sp.getBoolean("mainService", true)) {
			return true;
		}
		return false;
	}

	/**
	 * �л�����һ��ҳ��
	 */
	public void transView() {
		startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
		// ���ý����˳��ͽ��붯��
		this.overridePendingTransition(R.anim.view_move_left_show,R.anim.view_move_left_hide);
		finish();
	}

}
