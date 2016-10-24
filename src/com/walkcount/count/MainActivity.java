package com.walkcount.count;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.walkcount.view.TimelyTextView;
import com.walkcount.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.walkcount.dao.WalkCountDao;
import com.walkcount.utils.DateUtils;
import com.walkcount.utils.WalkUtils;

public class MainActivity extends Activity implements WalkUtils.Callback{

	private static final int SPEED_SHRESHOLD = 3000;
	public static final String EVENT_ONACCELERATE = "onAccelerometerChanged";

	private int footCount = 0;
	private final int i_zaxis = 1600;
	protected int fromNumber =0 ;
	private ToggleButton tb_start;
	private boolean bCheck = true;
	private TimelyTextView tv_status;
	public long lastUpdateTime;
	private WalkUtils walkUtils;
	WalkCountDao countDao = new WalkCountDao();
	public String nowDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
		if(savedInstanceState!=null){
			bCheck  = savedInstanceState.getBoolean("isCheck");
			fromNumber = savedInstanceState.getInt("count", 0);
		}
		walkUtils = WalkUtils.getInstance(MainActivity.this);
		walkUtils.setListener(this);
		initViews();
		initData();
		
		
		
	}


	private void initData() {
		nowDate = DateUtils.getNowDate();
		fromNumber = countDao.getCount(nowDate);
		tv_status.setText(fromNumber + "");
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	   super.onCreateOptionsMenu(menu);
	   menu.add(0, 0, 0,getString(R.string.menu1));
	   menu.add(0, 0, 0,getString(R.string.menu2));
	   return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(MainActivity.this,DataActivity.class);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(MainActivity.this,DataActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void initViews() {
		tv_status = (TimelyTextView) findViewById(R.id.tv_statue);

		tb_start = (ToggleButton) findViewById(R.id.tb_start);
		tb_start.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					walkUtils.start();
					tv_status.setVisibility(View.VISIBLE);
				} else {
					walkUtils.stop();
				}
			}
		});
		if(bCheck){
			tb_start.setChecked(true);
			tv_status.setText(fromNumber+"");
		}
	}

	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isCheck", tb_start.isChecked());
		outState.putInt("count", fromNumber);
		super.onSaveInstanceState(outState);
	}
	

	
	protected void onDestory() {
		walkUtils.stop();
		super.onDestroy();
	}

	@Override
	public void walkCount(final int count) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				tv_status.start(fromNumber, fromNumber + count);
				fromNumber = fromNumber + count;
				countDao.save(nowDate, fromNumber);
				if(!CountService.isRunning){
					Intent intent = new Intent(MainActivity.this,CountService.class);
					intent.putExtra("flag", 0);
					intent.putExtra("count", fromNumber);
					startService(intent);
				}else{
					Intent intent = new Intent("android.intent.count");
					intent.putExtra("count", fromNumber);
					sendBroadcast(intent);
				}
			}
		});	
	}

	@Override
	public void fail(String message) {
		
	}
}
