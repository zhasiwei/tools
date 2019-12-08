package com.zsw.tools.mysqlData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import com.zsw.tools.utils.CommonUtil;
import com.zsw.tools.utils.PropertyUtil;

public class MysqlConnect {

	public static Connection getConnection() {
		Connection connection = null;
		try {
			Map<String, String> preportyMap = PropertyUtil.PROPERTYMAP;
			if(CommonUtil.isEmpty(preportyMap)) {
				System.out.println("=======ERROR:配置信息为空");
			}
			
			String host = PropertyUtil.getMysqlUtil().host;
			String port = PropertyUtil.getMysqlUtil().port;
			String database = PropertyUtil.getMysqlUtil().database;
			String user = PropertyUtil.getMysqlUtil().user;
			String password = PropertyUtil.getMysqlUtil().password;
			
			String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=utf8";
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("获取数据库连接失败");
			e.printStackTrace();
		}
		return connection;
	}
}
