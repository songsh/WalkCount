package com.youtx.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;


public class TimelyTextView extends android.widget.TextView {

	private int fromNumber;
	private long duration = 2000;
	private int mRunState = STOPPING;
	private static final int RUNNING =0 ;
	private static final int STOPPING = 1;

	public TimelyTextView(Context context) {
		super(context);
	}
	public TimelyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public TimelyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	private void runInt(int number){
		ValueAnimator  animator = ValueAnimator.ofInt(fromNumber,number);
		animator.setDuration(duration).start();
		animator.addUpdateListener(new AnimatorUpdateListener(){

			@Override
			public void onAnimationUpdate(ValueAnimator value) {
				setText(value.getAnimatedValue().toString());
				if(value.getAnimatedFraction()>=1){
					mRunState = STOPPING;
				}
			
			}
			
		});
	}
	
	public void start(int fromNumber,int number){
		if(isRunning()!=RUNNING){
			this.fromNumber = fromNumber;
			mRunState = RUNNING;
			runInt(number);
		}
	}
	private int isRunning() {
		return mRunState;
	}

}
