package com.walkcount.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.DateFormat;
import android.text.format.Formatter;

public class DateUtils {
	
	public static String getNowDate(){
		
		SimpleDateFormat fformatter = new SimpleDateFormat("yyyy-MM-dd");
		return fformatter.format(new Date());
	}

}
