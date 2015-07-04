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
			System.out.println("我在解析XML文件。。。。。。。。。");
			new Thread(){
				public void run() {
					timer();
				};
			}.start();
			List<Poem> list = XMLTools.readXML(is);
			flag = false;
			System.out.println("解析XML文件完成！！！！！！");
			
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
			System.out.println("解析XML文件失败");
		}
	}
	
	/**
	 * 标记当前服务已运行，不需要再开启
	 */
	private void signService() {
		sp = this.getSharedPreferences("runningService", Context.MODE_PRIVATE);
		sp.edit().putBoolean("mainService", false).commit();
	}

	/**
	 * 计时器
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
		System.out.println("--- 解析用时：" + num + " ms");
	}
	
	@Override
	public void onDestroy() {
		System.out.println("服务被销毁。。。。。。。");
		deleteCacheFile();
		super.onDestroy();
	}
	
	/**
	 * 删除缓存文件
	 */
	public void deleteCacheFile() {
		// 删除缓存文件
		File file = getCacheDir();
		if (file != null && file.exists() && file.isDirectory()) {
			for(File item : file.listFiles()){
				item.delete();
			}
			file.delete();
		}
		// 删除临时数据库
		this.deleteDatabase("cache.db");
		// 在服务置为需要启动
		sp.edit().putBoolean("mainService", true).commit();
	}

}
