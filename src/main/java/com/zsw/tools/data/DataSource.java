package com.zsw.tools.data;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.zsw.tools.utils.CommonUtil;

public class DataSource implements javax.sql.DataSource{

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

}
