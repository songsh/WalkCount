package com.walkcount.dao;

import java.util.List;

import com.walkcount.db.WalkCount;


public class WalkCountDao {
	
	public void save(String date,int walkcount){
		List<WalkCount> list = WalkCount.find(WalkCount.class,"date = ?", date);
		if(list.size() ==0){
			WalkCount walkCount = new WalkCount(date,walkcount);
			walkCount.save();
		}else{
			WalkCount walkCount = list.get(0);
			walkCount.setWalkcount(walkcount);
			walkCount.save();
			
		}
	}
	
	public int getCount(String date){
		List<WalkCount> list = WalkCount.find(WalkCount.class,"date = ?", date);
		if(list.size() ==0){
			return 0;
		}else{
			return list.get(0).getWalkcount();
			
		}
	}

}
