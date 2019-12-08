package com.zsw.tools.utils.data;

import java.util.List;

public class TableBean {

	private String targeTableName;
	
	private String querySql;
	
	private List<SubTableRelaBean> subTableList;

	public String getTargeTableName() {
		return targeTableName;
	}

	public void setTargeTableName(String targeTableName) {
		this.targeTableName = targeTableName;
	}

	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public List<SubTableRelaBean> getSubTableList() {
		return subTableList;
	}

	public void setSubTableList(List<SubTableRelaBean> subTableList) {
		this.subTableList = subTableList;
	}
	
	
	
}
