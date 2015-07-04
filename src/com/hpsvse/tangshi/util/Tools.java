package com.hpsvse.tangshi.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class Tools {

	/**
	 * 判断某服务是否正在运行
	 * @return
	 */
	public static boolean isServiceRunning(Context context,String string) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceInfos = manager.getRunningServices(30);
		for (RunningServiceInfo serviceInfo : serviceInfos) {
			System.out.println("getClassName() = " + serviceInfo.service.getClassName());
			if (serviceInfo.service.getClassName().equals(string)) {
				return true;
			}
		}
		return false;
	}
}
