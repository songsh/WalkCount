package com.walkcount.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DateUtilsTest {

	private DateUtils dateUtils = new DateUtils();
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testGetNowDate() {
		System.out.println(DateUtils.getNowDate());
	}

}
