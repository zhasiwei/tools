package com.zsw.tools.mysqlData.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mysql.cj.x.protobuf.MysqlxPrepare.PrepareOrBuilder;
import com.zsw.tools.utils.CommonUtil;
import com.zsw.tools.utils.DataSourceUtil;
import com.zsw.tools.utils.PropertyUtil;
import com.zsw.tools.utils.data.TableBean;

/**
 *  准备数据
 * @author Administrator
 *
 */
public class MysqlDataPrepareDataApp {
	
	public static void main(String[] args) throws SQLException, InterruptedException {

		// 准备数据
//		Map<String, String> sqlMap = CommonUtil.getSQLPropertyMap();
		List<TableBean> tableBeanList = PropertyUtil.getTableBeanList();
		if(PropertyUtil.getMysqlUtil().fkCheck) {
			CommonUtil.getFKMap();
		}
		
		String threadCount = PropertyUtil.PROPERTYMAP.get("thread.max");
		int count = Integer.parseInt(CommonUtil.isNotEmpty(threadCount) ? threadCount:"5");
		ExecutorService executor = Executors.newFixedThreadPool(count);
		for(TableBean table :tableBeanList) {
			 executor.submit(() -> {
	                System.out.println("thread id is: " + Thread.currentThread().getId());
             	try {
						DataSourceUtil.prepareAndBackTableData(table);
					} catch (SQLException e) {
						e.printStackTrace();
					}
	            });
		}
//		sqlMap.forEach((key, value) ->{
//			 executor.submit(() -> {
//	                System.out.println("thread id is: " + Thread.currentThread().getId());
//             	try {
//						DataSourceUtil.prepareAndBackTableData(key, value);
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//	            });
//		});
		
		System.out.println("MysqlDataPrepareDataApp.main()");
	}

}
