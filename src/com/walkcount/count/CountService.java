package com.walkcount.count;

import com.walkcount.R;
import com.walkcount.count.CountService.CountReceiver;

import android.R.anim;
import android.app.IntentService;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class CountService extends Service {

	public static final int RUNNING = 0;
	public static final int STOPPING = 1;
	private boolean isForeground = false;
	private NotificationManager notiManager;
	private Notification notification;
	private Builder builder;
	private CountReceiver receiver;
	public static boolean isRunning = false;
	
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onCreate(){
		notiManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		receiver = new CountReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.count");
		registerReceiver(receiver, filter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int flag = intent.getIntExtra("flag", 0);
		int count = intent.getIntExtra("count", 0);
		if(flag == RUNNING){
			isRunning = true;
			if(!isForeground){
				isForeground = true;
				startForground(count);
			}else{
				updateNotification(count);
			}
		}else{
			isRunning = false;
			if(isForeground){
				isForeground = false;
				stopForeground(true);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	

	private void startForground(int count) {
		CharSequence title = getString(R.string.hello_world);
		CharSequence content = getString(R.string.walkcount, count);
		builder = new Notification.Builder(this);
		builder.setSmallIcon(R.drawable.logo).setContentTitle(title).setContentText(content).setWhen(System.currentTimeMillis());
		Intent intent = new Intent(this,MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		builder.setContentIntent(pendingIntent);
		startForeground(1, builder.build());
	}
	
	private void updateNotification(int count){
		CharSequence title = getString(R.string.hello_world);
		CharSequence content = getString(R.string.walkcount, count);
		builder.setContentText(content);
		notiManager.notify(1, builder.build());
		
	}
	
	@Override
	public void onDestroy() {
		stopSelf();
		unregisterReceiver(receiver);
		super.onDestroy();
		
	}
	
	class CountReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent intent) {

			int count = intent.getIntExtra("count", 0);
			updateNotification(count);
		}
	}
	

}
