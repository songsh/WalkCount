package com.youtx.count;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.youtx.test.R;
import com.youtx.view.TimelyTextView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
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

public class MainMusic extends Activity {

	private static final int SPEED_SHRESHOLD = 3000;
	public static final String EVENT_ONACCELERATE = "onAccelerometerChanged";

	private SensorManager mSensorManager;
	private SensorEventListener mSensorListener;
	private Sensor mSensor;

	private int mRate = SensorManager.SENSOR_DELAY_NORMAL;
	float lastX = 0, lastY = 0, lastZ = 0;

	private float[] zaxis;
	private int zaxisIndex = 0;
	private int footCount = 0;
	private Timer t = new Timer();
	private final int i_zaxis = 200;
	protected int fromNumber =0 ;
	private MyTimerTask t1;
	private ToggleButton tb_start;
	private boolean bCheck = false;
	private TimelyTextView tv_status;
	public long lastUpdateTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
		if(savedInstanceState!=null){
			bCheck  = savedInstanceState.getBoolean("isCheck");
			fromNumber = savedInstanceState.getInt("count", 0);
		}
		initViews();
		initSensor();
		initDisplay();
	}

	@Override
	public void finish() {

		super.finish();
		stop();
	}

	private void initViews() {
		tv_status = (TimelyTextView) findViewById(R.id.tv_statue);

		 tb_start = (ToggleButton) findViewById(R.id.tb_start);
		tb_start.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					start();
					tv_status.setVisibility(View.VISIBLE);
					if(t!=null){
						if(t1!=null){
							t1.cancel();
						}
						t1 = new MyTimerTask();
					}
					t.scheduleAtFixedRate(t1, 1, 1000 * 30);
				} else {
					stop();
				}
			}
		});
		if(bCheck){
			tb_start.setChecked(true);
			tv_status.setText(fromNumber+"");
		}
	}

	public boolean isReady() {
		return (mSensor == null) ? false : true;
	}

	public void start() {
		if (isReady()) {
			// mShakeListener.stop();
			zaxis = new float[i_zaxis];
			mSensorManager.registerListener(mSensorListener, mSensor, mRate);

		} else {
			Toast.makeText(MainMusic.this, "Orientation sensor is not found.",
					10).show();

		}
	}

	public void stop() {
		if (isReady()) {
			mSensorManager.unregisterListener(mSensorListener);
		}
		Intent intent = new Intent(MainMusic.this,CountService.class);
		intent.putExtra("flag", 1);
		startService(intent);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public void initDisplay() {

	}

	Handler h1 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			tv_status.start(fromNumber, fromNumber + msg.what * 2);
			fromNumber = fromNumber + msg.what * 2;
			Intent intent = new Intent(MainMusic.this,CountService.class);
			intent.putExtra("flag", 0);
			intent.putExtra("count", fromNumber);
			startService(intent);
			super.handleMessage(msg);
		}
	};
	

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			for (int i = 0; i < zaxis.length - 2; i++) {
				if (Math.abs(zaxis[i]) < 0.25)
					continue;
				if ((zaxis[i] >= 0 && zaxis[i + 1] < 0)
						|| (zaxis[i] > 0 && zaxis[i + 1] <= 0)) {
					footCount++;
				}
			}
			Message m = h1.obtainMessage();
			m.what = footCount;
			h1.sendMessage(m);
			resetData();
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isCheck", tb_start.isChecked());
		outState.putInt("count", fromNumber);
		super.onSaveInstanceState(outState);
	}
	
	private void initSensor() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mSensorListener = new SensorEventListener() {
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}

			@Override
			public void onSensorChanged(SensorEvent event) {
				try {

					if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION)
						return;
					long curTime = System.currentTimeMillis();
					if (curTime - lastUpdateTime > 10) {
						if (zaxisIndex > i_zaxis) {
							zaxisIndex = 0;
						}
						Log.i("axis", zaxisIndex+" "+event.values[SensorManager.DATA_Z]+ " "+(curTime - lastUpdateTime));
						zaxis[zaxisIndex++] = event.values[SensorManager.DATA_Z];
						lastUpdateTime = curTime;
					}
				} catch (Exception e) {
					Log.e("e", e.getMessage());
				}
			}

		};

	}

	protected void resetData() {
		zaxis = new float[i_zaxis];
		zaxisIndex = 0;
		Log.i("reset", zaxisIndex+"");
		footCount = 0;
	}

	
	protected void onDestory() {
		stop();
		h1.removeCallbacksAndMessages(null);
		super.onDestroy();
	}
}
