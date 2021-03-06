package com.mobile.library.utils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

/**
 * Activity相关工具类
 * 
 * @author lihy
 *
 */
public class ActivityUtils {

	private ActivityUtils() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	/**
	 * 判断是否存在Activity
	 *
	 * @param context
	 *            上下文
	 * @param packageName
	 *            包名
	 * @param className
	 *            activity全路径类名
	 * @return {@code true}: 是<br>
	 *         {@code false}: 否
	 */
	public static boolean isActivityExists(Context context, String packageName, String className) {
		Intent intent = new Intent();
		intent.setClassName(packageName, className);
		return !(context.getPackageManager().resolveActivity(intent, 0) == null
				|| intent.resolveActivity(context.getPackageManager()) == null
				|| context.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
	}

	/**
	 * 打开Activity
	 *
	 * @param context
	 *            上下文
	 * @param packageName
	 *            包名
	 * @param className
	 *            全类名
	 */
	public static void launchActivity(Context context, String packageName, String className) {
		launchActivity(context, packageName, className, null);
	}

	/**
	 * 打开Activity
	 *
	 * @param context
	 *            上下文
	 * @param packageName
	 *            包名
	 * @param className
	 *            全类名
	 * @param bundle
	 *            bundle
	 */
	public static void launchActivity(Context context, String packageName, String className, Bundle bundle) {
		context.startActivity(IntentUtils.getComponentIntent(packageName, className, bundle));
	}

	/**
	 * 打开Activity
	 * 
	 * @param self
	 *            上下文
	 * @param clazz
	 *            类
	 * @param bundle
	 *            bundle
	 * @param requestCode
	 *            请求标识
	 */
	public static void launchActivity(Activity self, Class<? extends Activity> clazz, Bundle bundle, int requestCode) {
		Intent intent = new Intent(self, clazz);
		intent.putExtras(bundle);
		self.startActivityForResult(intent, requestCode);
	}

	/**
	 * 打开Activity
	 * 
	 * @param self
	 *            上下文
	 * @param clazz
	 *            类
	 * @param bundle
	 *            bundle
	 * @param requestCode
	 *            请求标识
	 */
	public static void launchActivity(Context context, Class<? extends Activity> clazz, Bundle bundle) {
		Intent intent = new Intent(context, clazz);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	/**
	 * 打开Activity
	 * 
	 * @param self
	 *            上下文
	 * @param clazz
	 *            类
	 * @param requestCode
	 *            请求标识
	 */
	public static void launchActivity(Context context, Class<? extends Activity> clazz) {
		Intent intent = new Intent(context, clazz);
		context.startActivity(intent);
	}

	/**
	 * 获取launcher activity
	 *
	 * @param context
	 *            上下文
	 * @param packageName
	 *            包名
	 * @return launcher activity
	 */
	public static String getLauncherActivity(Context context, String packageName) {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo info : infos) {
			if (info.activityInfo.packageName.equals(packageName)) {
				return info.activityInfo.name;
			}
		}
		return "no " + packageName;
	}
}
