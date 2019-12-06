package com.zsw.tools.utils;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import com.zsw.tools.mysqlData.MysqlConnect;

public class DataSourceUtil implements javax.sql.DataSource{

	private static final String TEMP_TABLE_NAME_PREFIX = "data_temp_";
	
	private  Connection conn;
	//1.创建一个容器存放连接对象Connection
	private static LinkedList<Connection> pool = new LinkedList<Connection>();   
	 
	@Override
	public Connection getConnection() {
		if(pool.isEmpty()) {
			
			String configCount = CommonUtil.getPropertyMap().get("mysql.connection_max_count");
			int count = Integer.parseInt(CommonUtil.isEmpty(configCount) ? "5" : configCount);
			for(int i = 0; i < count; i++) {
				   Connection conn = MysqlConnect.getConnection();
				   pool.add(conn);
		      }
		}
		conn = pool.removeFirst();
		return conn;
	}
	
	/**
	 * 将连接放回连接池中
	 * @param conn
	 */
	public void backConnection(Connection conn) {
		pool.add(conn);
	}
	
	public static List<String> getColumnNameList(String tableName) throws SQLException{
		
		List<String> list = new ArrayList<String>();
		Connection conn = MysqlConnect.getConnection();
		String sql = "select * from " + tableName + " limit 1";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSetMetaData metaData = pstmt.executeQuery().getMetaData();
		// 获取所有字段
		for(int i=0;i<metaData.getColumnCount(); i++) {
			list.add(metaData.getColumnName(i+1));
		}
		return list;
		
	}
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 处理表：1.整理insert sql模板，
	 * 		2.每次查出固定条数的数据，
	 * 		3.整理出对应的insert语句
	 * 		4.将原数据的id和insert语句录入临时表中
	 * @throws SQLException 
	 */
	public static void prepareAndBackTableData(String tableName, String querySql) throws SQLException {
		
		String tempName = new DataSourceUtil().createTable(tableName);
		
		List<String> insertSqlList = new ArrayList<String>();
		List<String> deleteSqlList = new ArrayList<String>();
		List<String> idList = new ArrayList<String>();
		
		Map<String, String> fkMap = CommonUtil.getFKMap();
		List<String> columnNameList = getColumnNameList(tableName);
		// 准备插入insert语句
		String insertSql = "insert into " + tableName;
		String deleteSql = "delete from " + tableName + " where id = ";
		for(int i=0 ;i <columnNameList.size() ;i++) {
			if(i ==0) {
				insertSql += "(";
			}
			insertSql +=  columnNameList.get(i);
			if(i == columnNameList.size() -1) {
				insertSql += ") values(";
			}else {
				insertSql += ",";
			}
		}
		
		Connection conn = new DataSourceUtil().getConnection();
		if(conn != null) {
			// 查询
			if(CommonUtil.isNotEmpty(querySql)) {
				boolean flag = true;
				String index = "0";
				String size = CommonUtil.getPropertyMap().get("mysql.onceQueryCount");
				size = CommonUtil.isEmpty(size) ? "5000" : size;
				PreparedStatement ps = null;
				ResultSet rs  = null;
				while(flag) {
					String realSql = querySql;
					if(querySql.indexOf("limit") == -1) {
						realSql += " limit " + index + "," + size;
						index = Integer.parseInt(index) + Integer.parseInt(size) + "";
					}
					System.out.println("===准备删除数据以及备份删除数据，当前sql为：" + realSql);
					ps = conn.prepareStatement(realSql);
					rs = ps.executeQuery();
					rs.last();//移到最后一行
					if(rs.getRow() ==0) {
						flag = false;
						break;
					}
					rs.beforeFirst();//移到初始位置
					while(rs.next()) {
						int count = 0;
						String id = rs.getString("id");
						String realInsert = insertSql;
						String realdelete = deleteSql;
						String value = null;
						realdelete = deleteSql + "'" + id + "'";
						for(String name : columnNameList) {
							value = rs.getString(name);
							if(CommonUtil.isNotEmpty(value)) {
								realInsert += "'" + value + "'";
							}else {
								realInsert += "NULL";
							}
							if(count == columnNameList.size() -1) {
								realInsert +=");";
							}else {
								realInsert +=",";
							}
							count ++;
						}
						insertSqlList.add(realInsert);
						deleteSqlList.add(realdelete);
						idList.add(id);
					}
					
					// 插入临时备份表
					String dataSql = "insert into " + tempName + "(id, insertSql, deleteSql,state, fk_table_name) values(?,?,?,?,?)"; 
					PreparedStatement pss = conn.prepareStatement(dataSql);
					conn.setAutoCommit(false);

					for(int i=0;i<idList.size();i++) {
						pss.setString(1, idList.get(i));
						pss.setString(2, insertSqlList.get(i));
						pss.setString(3, deleteSqlList.get(i));
						pss.setString(4, "I");
						pss.setString(5, fkMap.get(tableName));
						pss.addBatch();
					}
					pss.executeBatch();
					conn.commit();
					idList.clear();
					insertSqlList.clear();
					conn.setAutoCommit(true);
				}
			}
			
		}
		
	}
	
	public static void deleteData(String tableName) throws SQLException {
		String tempTableName = TEMP_TABLE_NAME_PREFIX + tableName;
		Connection conn = new DataSourceUtil().getConnection();
		// 检查是否有外键
		List<String> fkList = hasFk(conn, tempTableName);
		if(CommonUtil.isNotEmpty(fkList)) {
			for(String fk : fkList) {
				//递归，先删除无外键的数据
				deleteData(fk);
			}
		}
		
		// 每次查询固定条数，进行删除
		boolean flag = true;
		String index = "0";
		String size = CommonUtil.getPropertyMap().get("mysql.onceQueryCount");
		size = CommonUtil.isEmpty(size) ? "5000" : size;
		String querySql = "select * from " + tempTableName + " where state='I'";
		PreparedStatement ps = null;
		ResultSet rs  = null;
		List<String> deleteIdList;
		String deleteSql = null;
		String updateSql = null;
		while(flag) {
			deleteIdList = new ArrayList<String>();
			String realSql = querySql;
			if(querySql.indexOf("limit") == -1) {
				realSql += " limit " + index + "," + size;
				index = Integer.parseInt(index) + Integer.parseInt(size) + "";
			}
			ps = conn.prepareStatement(realSql);
			rs = ps.executeQuery();
			rs.last();//移到最后一行
			if(rs.getRow() ==0) {
				flag = false;
				break;
			}
			System.out.println("===开始删除表数据，查询临时表的Id sql为：" + realSql);
			rs.beforeFirst();//移到初始位置
			while(rs.next()) {
				deleteIdList.add(rs.getString("id"));
			}
			
			//开始删除和更新状态
			deleteSql = "delete from " + tableName + " where id=?";
			updateSql = "update " + tempTableName + " set state='C'" + " where id=?";
			if(CommonUtil.isNotEmpty(deleteIdList)) {
				PreparedStatement ups = conn.prepareStatement(updateSql);
				PreparedStatement dps = conn.prepareStatement(deleteSql);
				conn.setAutoCommit(false);
				for(String id: deleteIdList) {
					dps.setString(1, id);
					dps.addBatch();
					ups.setString(1, id);
					ups.addBatch();
				}
				dps.executeBatch();
				ups.executeBatch();
				conn.commit();
				deleteIdList.clear();
				conn.setAutoCommit(true);
				
			}
		}
	}
	
	private static List<String> hasFk(Connection conn, String tableName) throws SQLException {
		String sql = "select * from " + tableName + " limit 1";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List<String> fkList = new ArrayList<String>();
		while(rs.next()) {
			if(CommonUtil.isNotEmpty(rs.getString("fk_table_name"))) {
				String[] arry = rs.getString("fk_table_name").split(",");
				for(String name : arry) {
					fkList.add(name);
				}
			}
		}
		return fkList;
	}
	
	
	public String createTable(String tableName) throws SQLException {
		
		String tempName = TEMP_TABLE_NAME_PREFIX + tableName;
		String sql = "CREATE TABLE IF NOT EXISTS `" + tempName +"`  (\r\n" + 
				"  `id` varchar(32) NOT NULL,\r\n" + 
				"  `insertSql` text NULL,\r\n" + 
				"  `deleteSql` text NULL,\r\n" + 
				"  `state` varchar(1) NULL,\r\n" + 
				"  `fk_table_name` varchar(512) NULL,\r\n" + 
				"  PRIMARY KEY (`id`)\r\n" + 
				");";
		
		Connection conn = new DataSourceUtil().getConnection();
		Statement  ps = conn.createStatement();
		ps.executeUpdate(sql);
		
		return tempName;
	}
	

	public Map<String, String> getFkTbale() throws SQLException {
		DatabaseMetaData metaData = new DataSourceUtil().getConnection().getMetaData();
		ResultSet tables = metaData.getTables("vcc", null, "%", null);
		Map<String, String> pkMap = new HashMap<String, String>();
		while(tables.next()) {
			String tableName = tables.getString(3);
			System.out.println("====扫描表外键关系,当前表为：" + tableName);
			ResultSet fks = metaData.getImportedKeys(null, null, tableName);
			while(fks.next()) {
				if(CommonUtil.isNotEmpty(fks.getString("PKTABLE_NAME")) && !tableName.equals(fks.getString("PKTABLE_NAME"))) {
					String value = fks.getString("PKTABLE_NAME");
					if(pkMap.containsKey(value)) {
						tableName = pkMap.get(value) + "," + tableName; 
					}
					pkMap.put(value, tableName);
				}
			}
			
		}
		return pkMap;
	}
	
}

