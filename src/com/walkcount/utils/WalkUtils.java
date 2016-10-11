package com.walkcount.utils;


import java.util.Timer;
import java.util.TimerTask;

import com.walkcount.count.CountService;
import com.walkcount.count.MainActivity;
import com.walkcount.utils.WalkUtils.Callback;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class WalkUtils {
	
	private static WalkUtils walkUtils;
	private Context context;
	private int fromNumber;
	private SensorManager mSensorManager;
	private SensorEventListener mSensorListener;
	private Sensor mSensor;
	protected long lastUpdateTime;
	protected int i_zaxis = 300;
	protected int zaxisIndex;
	protected float[] zaxis;
	private SensorType walkType;
	private int mRate = 200 * 1000;
	private Callback callback;
	private Timer t;
	private MyTimerTask t1;
	private WalkUtils(Context context){
		this.context = context;
		initSensor();
	}
	
	public static WalkUtils getInstance(Context context){
		if(walkUtils == null){
			walkUtils = new WalkUtils(context);
		}
		return walkUtils;
	}
	
	
	private void initSensor(){
		if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER)){
			walkType = SensorType.Linear;
			initLinearSensor();
		}
		else if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)){
			walkType = SensorType.step;
			initStepSensor();
		}else{
			walkType = SensorType.jiao;
			initGyroSensor();
		}
	}
	private void initStepSensor(){
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		mSensorListener = new SensorEventListener() {
			
			@Override
			public void onSensorChanged(SensorEvent event) {
				if (event.sensor.getType() != Sensor.TYPE_STEP_COUNTER)
					return;
				
				Log.i("tag", event.values[0]+"");
				zaxisIndex = (int)event.values[0];
			}
			
			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1) {

			}
		};
	}
	private void initGyroSensor(){
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mSensorListener = new SensorEventListener() {
			
			@Override
			public void onSensorChanged(SensorEvent event) {
				if (event.sensor.getType() != Sensor.TYPE_GYROSCOPE)
					return;
				
				long curTime = System.currentTimeMillis();
				if (zaxisIndex > i_zaxis ) {
					zaxisIndex = 0;
				}
				Log.i("axis", zaxisIndex+" "+event.values[SensorManager.DATA_Y]+ " "+(curTime - lastUpdateTime));
				zaxis[zaxisIndex++] = event.values[SensorManager.DATA_Y];
				lastUpdateTime = curTime;
			}
			
			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1) {
				
			}
		};
	}
	private void initLinearSensor() {
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
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
						if (zaxisIndex > i_zaxis ) {
							zaxisIndex = 0;
						}
						Log.i("axis", zaxisIndex+" "+event.values[SensorManager.DATA_Z]+ " "+(curTime - lastUpdateTime));
						zaxis[zaxisIndex++] = event.values[SensorManager.DATA_Z];
						lastUpdateTime = curTime;
				} catch (Exception e) {
					Log.e("e", e.getMessage());
				}
			}

		};

	}
	
	public boolean isReady() {
		return (mSensor == null) ? false : true;
	}

	public void start() {
		if (isReady()) {
			// mShakeListener.stop();
			zaxis = new float[i_zaxis];
			mSensorManager.registerListener(mSensorListener, mSensor, mRate);
			startTimer();
		} else {
			Toast.makeText(context, "Orientation sensor is not found.",10).show();
		}
	}

	private void startTimer() {
		if(t==null){
			t = new Timer();
		}
		if(t1!=null){
			t1.cancel();
		}
		t1 = new MyTimerTask();
		t.scheduleAtFixedRate(t1, 1, 1000 * 30);
		
	}

	public void stop() {
		if (isReady()) {
			mSensorManager.unregisterListener(mSensorListener);
		}
		if(t !=null){
			t.cancel();
		}
		Intent intent = new Intent(context,CountService.class);
		intent.putExtra("flag", 1);
		context.startService(intent);
	}
	
	public interface Callback{
		public void walkCount(int count);
		public void fail(String message);
	}
	
	public void setListener(Callback callback){
		this.callback = callback;
	}
	
	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			int footCount = 0;
			if(walkType == SensorType.jiao){
				for (int i = 0; i < zaxis.length - 2; i++) {
					if (Math.abs(zaxis[i]) < 0.25)
						continue;
					if ((zaxis[i] >= 0 && zaxis[i + 1] < 0)
							|| (zaxis[i] > 0 && zaxis[i + 1] <= 0)) {
						footCount++;
					}
				}
			}else if(walkType == SensorType.Linear){
				for (int i = 0; i < zaxis.length - 2; i++) {
					if (Math.abs(zaxis[i]) < 0.25)
						continue;
					if ((zaxis[i] >= 0 && zaxis[i + 1] < 0)
							|| (zaxis[i] > 0 && zaxis[i + 1] <= 0)) {
						footCount++;
					}
				}
			}else{
				footCount = zaxisIndex;
			}
			callback.walkCount(footCount);
			resetData();
		}
	};
	
	enum SensorType{
		Linear(0),jiao(1),step(2);
		int type;
		private  SensorType(int type){
			this.type = type;
		}
	}
	
	private void resetData() {
		zaxis = new float[i_zaxis];
		zaxisIndex = 0;
		Log.i("reset", zaxisIndex+"");
	}

}


