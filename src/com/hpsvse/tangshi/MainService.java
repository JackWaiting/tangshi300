package com.hpsvse.tangshi;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.hpsvse.tangshi.db.PoemDAO;
import com.hpsvse.tangshi.entity.Poem;
import com.hpsvse.tangshi.util.XMLTools;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class MainService extends Service {

	private SharedPreferences sp;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private boolean flag = true;
	@Override
	public void onCreate() {
		super.onCreate();
		try {
			InputStream is = this.getAssets().open("tangshi300.xml");
			System.out.println("���ڽ���XML�ļ�������������������");
			new Thread(){
				public void run() {
					timer();
				};
			}.start();
			List<Poem> list = XMLTools.readXML(is);
			flag = false;
			System.out.println("����XML�ļ���ɣ�����������");
			
			new Thread(){
				public void run() {
					flag = true;
					timer();
				};
			}.start();
			PoemDAO poemDAO = new PoemDAO(this);
			for(Poem poem : list){
				poemDAO.insert(poem);
			}
			flag = false;
			signService();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("����XML�ļ�ʧ��");
		}
	}
	
	/**
	 * ��ǵ�ǰ���������У�����Ҫ�ٿ���
	 */
	private void signService() {
		sp = this.getSharedPreferences("runningService", Context.MODE_PRIVATE);
		sp.edit().putBoolean("mainService", false).commit();
	}

	/**
	 * ��ʱ��
	 */
	public void timer(){
		int num = 0;
		while(flag){
			try {
				Thread.sleep(5);
				num += 5;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("--- ������ʱ��" + num + " ms");
	}
	
	@Override
	public void onDestroy() {
		System.out.println("�������١�������������");
		deleteCacheFile();
		super.onDestroy();
	}
	
	/**
	 * ɾ�������ļ�
	 */
	public void deleteCacheFile() {
		// ɾ�������ļ�
		File file = getCacheDir();
		if (file != null && file.exists() && file.isDirectory()) {
			for(File item : file.listFiles()){
				item.delete();
			}
			file.delete();
		}
		// ɾ����ʱ���ݿ�
		this.deleteDatabase("cache.db");
		// �ڷ�����Ϊ��Ҫ����
		sp.edit().putBoolean("mainService", true).commit();
	}

}
