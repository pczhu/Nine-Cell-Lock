package com.pczhu.jiugongge.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.pczhu.jiugongge.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//设置标题不显示
		
		setContentView(R.layout.activity_main);
		Intent intent = new Intent(this,LockActivity.class);
		startActivity(intent);
		
	}
	
}
