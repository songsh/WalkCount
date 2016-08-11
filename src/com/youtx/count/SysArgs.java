package com.youtx.count;

import java.util.ArrayList;


import android.os.Environment;

public class SysArgs {

	public static String SD = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	private static String appHome = SysArgs.SD+"/mp3/";
	
	public static ArrayList<Music> mp3s = OtherUtils.getSdFile(appHome,new String[]{".mp3"});
	public static ArrayList<Music> mp3s_stop = OtherUtils.getSdFile(appHome + "/1/",new String[]{".mp3"});
	public static ArrayList<Music> mp3s_walk = OtherUtils.getSdFile(appHome + "/2/",new String[]{".mp3"});
	public static ArrayList<Music> mp3s_run = OtherUtils.getSdFile(appHome + "/3/",new String[]{".mp3"});
	public static ArrayList<Music> mp3s_static = OtherUtils.getSdFile(appHome + "/4/",new String[]{".mp3"});
}
