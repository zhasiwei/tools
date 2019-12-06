package com.zsw.tools.utils;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

public class PrepareThread extends Thread{
	
	private CountDownLatch latch;
	
	private String key;
	
	private String value;
	
	public PrepareThread(CountDownLatch latch) {
		this.latch = latch;
	}
	
	
	public PrepareThread(CountDownLatch latch, String key, String value) {
		super();
		this.latch = latch;
		this.key = key;
		this.value = value;
	}


	@Override
	public void run() {
		try {
			DataSourceUtil.prepareAndBackTableData(key, value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		latch.countDown();
		System.out.println("MysqlDataPrepareDataApp.main(...).new Thread() {...}.run()");
	}
	
	
}