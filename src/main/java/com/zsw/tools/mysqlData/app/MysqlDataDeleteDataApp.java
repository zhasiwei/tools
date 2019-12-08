package com.zsw.tools.mysqlData.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.zsw.tools.utils.CommonUtil;
import com.zsw.tools.utils.DataSourceUtil;
import com.zsw.tools.utils.PropertyUtil;
import com.zsw.tools.utils.data.TableBean;

/**
 *  删除数据
 * @author Administrator
 *
 */
public class MysqlDataDeleteDataApp {
	
	public static void main(String[] args) throws SQLException {

//		Map<String, String> sqlMap = CommonUtil.getSQLPropertyMap();
		List<TableBean> tableBeanList = PropertyUtil.getTableBeanList();
		
		for(TableBean table : tableBeanList) {
			// TODO 启用线程进行处理
			DataSourceUtil.deleteData(table.getTargeTableName());
		}
//		sqlMap.forEach((key, value) ->{
//			try {
//				// TODO 启用线程进行处理
//				DataSourceUtil.deleteData(key);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		});
	
	}

	
}
