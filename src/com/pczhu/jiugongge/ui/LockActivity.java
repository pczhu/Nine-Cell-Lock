package com.pczhu.jiugongge.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pczhu.jiugongge.R;
import com.pczhu.jiugongge.view.NinePointLineView;
import com.pczhu.jiugongge.view.NinePointLineView.OnStateChangedListener;

public class LockActivity extends Activity {
	private LinearLayout nine_con;//九宫格容器
	
	NinePointLineView nv;//九宫格View
	
	TextView showInfo;
	
	boolean isSetFirst = false;

	private String pwd;

	private Intent intent;
	private boolean isSettingPwd = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//设置标题不显示
		
		setContentView(R.layout.activity_lock);
		intent = getIntent();
		if(intent != null){
			isSettingPwd = intent.getBooleanExtra("SET_PSW", true);
		}
		TextView textView = (TextView) findViewById(R.id.setting);
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LockActivity.this,SettingActivity.class);
				startActivity(intent);
				
			}
		});
		initNimePointPwd();
		SharedPreferences shareDate = getSharedPreferences("GUE_PWD", 0);
		if(shareDate != null){
			pwd = shareDate.getString("GUE_PWD", "NO HAVE PWD !");
		}
		if(pwd != null){
			showInfo.setText("请输入手势密码");
		}
		getSetPwd();
	}
	
	private void initNimePointPwd() {
		nv = new NinePointLineView(LockActivity.this);
		
		nine_con = (LinearLayout)findViewById(R.id.nine_con);
		
		nine_con.addView(nv);
		
		showInfo = (TextView)findViewById(R.id.show_set_info);
		
	}


	/**
	 * 作用：获取现在密码的设置步骤
	 * 作者：unfind
	 * 时间：2013年10月29日 14:20:36
	 * */
	public void getSetPwd(){
		
		final SharedPreferences shareDate = getSharedPreferences("GUE_PWD", 0);
		
		isSetFirst = shareDate.getBoolean("IS_SET_FIRST", false);
			nv.setOnStateChanged(new OnStateChangedListener() {
				@Override
				public void OnStateChanged(int statue) {
					switch (statue) {
					case 0:
						showInfo.setText("请设置手势密码");
						shareDate.edit().clear().commit();
						break;
					case 1:
						showInfo.setText("请再次确认手势密码");
						break;
					case 2:
						showInfo.setText("和第一次输入手势密码不一致，重新输入");
						break;
					case 3:
						showInfo.setText("密码设置完成");
						break;
					case 4:
						showInfo.setText("请设置四个单元以上图案");
						break;
					case 5:
						showInfo.setText("密码不正确，请重新输入");
						break;
					case 6:
						showInfo.setText("密码正确");
						break;
					default:
						break;
					}

				}
			});
	}
	public void setting(View v){

	}
}
