package com.zsw.tools.utils.data;

import java.util.Map;

import com.zsw.tools.utils.CommonUtil;

public class MysqlUtil {

	public static String host;
	public static String port;
	public static String database;
	public static String user;
	public static String password;
	public static int onceQueryCount;//每次处理数据数量
	public static int connectionMaxCount;// 最多连接数 
	public static boolean fkCheck = false;
	
	public MysqlUtil(Map<String, String> configMap) {
		this.host = configMap.get("mysql.host");
		this.port = configMap.get("mysql.port");
		this.database = configMap.get("mysql.database");
		this.user = configMap.get("mysql.username");
		this.password = configMap.get("mysql.password");
		this.onceQueryCount = CommonUtil.isNotEmpty(configMap.get("mysql.onceQueryCount")) ? Integer.parseInt(configMap.get("mysql.onceQueryCount")) : 5000;
		this.connectionMaxCount = CommonUtil.isNotEmpty(configMap.get("mysql.connection_max_count")) ? Integer.parseInt(configMap.get("mysql.connection_max_count")) : 5;
		this.fkCheck = "true".equals(configMap.get("mysql.fk_check"));
	}
}
