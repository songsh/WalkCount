package com.walkcount.db;

import com.orm.SugarRecord;

public class WalkCount extends SugarRecord{
	String date;
	int walkcount;
	
	public WalkCount(){	
	}
	
	public WalkCount(String date,int walkcount){
		this.date = date;
		this.walkcount = walkcount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getWalkcount() {
		return walkcount;
	}

	public void setWalkcount(int walkcount) {
		this.walkcount = walkcount;
	}
	
	

}
