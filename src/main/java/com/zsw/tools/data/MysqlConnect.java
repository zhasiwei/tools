package com.zsw.tools.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import com.zsw.tools.utils.CommonUtil;

public class MysqlConnect {

	public static Connection getConnection() {
		Connection connection = null;
		try {
			Map<String, String> preportyMap = CommonUtil.getPropertyMap();
			if(CommonUtil.isEmpty(preportyMap)) {
				System.out.println("=======ERROR:配置信息为空");
			}
			
			String host = preportyMap.get("mysql.host");
			String port = preportyMap.get("mysql.port");
			String database = preportyMap.get("mysql.database");
			String user = preportyMap.get("mysql.username");
			String password = preportyMap.get("mysql.password");
			
			String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("获取数据库连接失败");
			e.printStackTrace();
		}
		return connection;
	}
}
