package com.walkcount.count;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.walkcount.R;
import com.walkcount.view.SplineChart04View;

public class DataActivity extends Activity{

	private SplineChart04View lineView;
	private Context context;
	private int fromNumber;
	private SensorManager mSensorManager;
	private SensorEventListener mSensorListener;
	private Sensor mSensor;
	private int mRate = 200 * 1000;
	private LinkedList<Float> oLinkedList = new LinkedList<Float>();
	private LinkedList<Float> sqartLinkedList = new LinkedList<Float>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		
		initView();
		initData();
	}

	private void initData() {
		context = this;
		initLinearSensor();
		start();
	}

	private void initView() {
		lineView = (SplineChart04View) findViewById(R.id.line_data);
		
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
					Log.i("sensor", event.values[SensorManager.DATA_Z]+"");
					long curTime = System.currentTimeMillis();
					oLinkedList.add((Float)event.values[SensorManager.DATA_Z]);
					sqartLinkedList.add((float) Math.sqrt(Math.pow(event.values[0], 2)
			                + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2)));
					
					lineView.updateData(oLinkedList, sqartLinkedList);
					
				} catch (Exception e) {
					Log.e("e", e.getMessage());
				}
			}
		};
	}
	
	public void stop() {
		if (isReady()) {
			mSensorManager.unregisterListener(mSensorListener);
		}
		
	}
	
	public boolean isReady() {
		return (mSensor == null) ? false : true;
	}

	public void start() {
		if (isReady()) {
			// mShakeListener.stop();
			mSensorManager.registerListener(mSensorListener, mSensor, mRate);
		} else {
			Toast.makeText(context, "Orientation sensor is not found.",10).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stop();
	}
	
}
