package com.zsw.tools.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MysqlDataApp {
	
	public static void main(String[] args) throws SQLException {
		Connection conn = MysqlConnect.getConnection();
		String sql = "select * from t_vcc_pv limit 1";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		ResultSetMetaData metaData = pstmt.executeQuery().getMetaData();
		// 获取所有字段
		for(int i=0;i<metaData.getColumnCount(); i++) {
			
		}
		while(rs.next()) {
			System.out.println(rs.getString("id"));
		}
	}
	
}
