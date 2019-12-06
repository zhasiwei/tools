package com.zsw.tools.mysqlData.app;

import java.sql.SQLException;
import java.util.Map;

import com.zsw.tools.utils.CommonUtil;
import com.zsw.tools.utils.DataSourceUtil;

/**
 *  删除数据
 * @author Administrator
 *
 */
public class MysqlDataDeleteDataApp {
	
	public static void main(String[] args) throws SQLException {

		Map<String, String> sqlMap = CommonUtil.getSQLPropertyMap();
		
		sqlMap.forEach((key, value) ->{
			try {
				// TODO 启用线程进行处理
				DataSourceUtil.deleteData(key);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	
	}

	
}
