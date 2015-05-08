package com.pczhu.jiugongge;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

public class BaseApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences mySharedPreferences = getSharedPreferences("SET", Activity.MODE_PRIVATE); 

	}
}
