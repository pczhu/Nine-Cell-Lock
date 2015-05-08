package com.pczhu.jiugongge.ui;

import java.util.ArrayList;
import java.util.List;

import com.pczhu.jiugongge.R;
import com.pczhu.jiugongge.view.sldingbutton.AbSlidingButton;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SettingActivity extends Activity{


	private List<SettingItem> item;
	private ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		item = new ArrayList<SettingItem>();
		item.add(new SettingItem("显示轨迹","是否显示手势的线条轨迹"));
		item.add(new SettingItem("手势密码","开启或者关闭手势密码"));
		lv = (ListView) findViewById(R.id.lv_setting);
		lv.setAdapter(new MyAdapter());
		findViewById(R.id.titlefinish).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SettingActivity.this.finish();
				
			}
		});
	}
	public class SettingItem{
		private String title;
		private String desc;
		
		public SettingItem(String title, String desc) {
			super();
			this.title = title;
			this.desc = desc;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
	}
	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return item.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView == null){
				view = View.inflate(SettingActivity.this, R.layout.item_list_button, null);
			}else{
				view = convertView;
			}
			TextView desc = (TextView)view.findViewById(R.id.itemsText);
			TextView title = (TextView)view.findViewById(R.id.itemsTitle);
			AbSlidingButton mSliderBtn = (AbSlidingButton) view.findViewById(R.id.mSliderBtn);
			
			desc.setText(""+item.get(position).desc);
			title.setText(""+item.get(position).title);
			mSliderBtn.setImageResource(R.drawable.btn_bottom,R.drawable.btn_frame,R.drawable.btn_mask, R.drawable.btn_unpressed,R.drawable.btn_pressed);
			SharedPreferences mySharedPreferences = getSharedPreferences("SET", Activity.MODE_PRIVATE); 
			mSliderBtn.setChecked(mySharedPreferences.getBoolean(""+position, true));
			
			mSliderBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SharedPreferences mySharedPreferences = getSharedPreferences("SET", Activity.MODE_PRIVATE); 
					mySharedPreferences.edit().putBoolean(""+position, isChecked).commit();
				}
			});
			return view;
		}

	}

}
