package com.youtx.count;

import java.io.Serializable;

import com.youtx.test.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btn = null;
	private Button btn2 = null;
	private Button btn3 = null;
	private ProgressBar firstBar = null;
	private ProgressBar secondBar = null;
	private int i = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_linear);
		
		 btn = (Button)findViewById(R.id.btn);
		 btn2 = (Button) findViewById(R.id.btn2);
		 btn3 = (Button) findViewById(R.id.btn3);
		 firstBar = (ProgressBar)findViewById(R.id.firstbar);
		 secondBar = (ProgressBar)findViewById(R.id.secondbar);
		 btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(i ==0){
					firstBar.setVisibility(View.VISIBLE);
					secondBar.setVisibility(View.VISIBLE);
					Toast.makeText(getApplicationContext(), "visible", Toast.LENGTH_SHORT).show();
					
				}else if(i < 100){
					firstBar.setProgress(i);
					firstBar.setSecondaryProgress(i+10);
					
				}else{
					firstBar.setVisibility(View.GONE);
					
				}
				i = i +10;
			}
		});
		 btn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("test1", "123");
				intent.putExtra("test2", "456");
//				intent.setClass(MainActivity.this, SecondActivity.class);
				MainActivity.this.startActivity(intent);
				
			}
			 
			 
		 });
		btn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("smsto://08000023422");
				Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
				intent.putExtra("sms_body", "the sms body");
				startActivity(intent);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0,1,1,"ÍË³ö");
		menu.add(0, 2, 2, "¹ØÓÚ");
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == 1){
			finish();	
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class student implements Serializable{
		String name;
		int age;
		boolean sex;
		
	}
	

}
