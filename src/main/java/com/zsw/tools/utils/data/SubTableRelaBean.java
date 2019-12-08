package com.zsw.tools.utils.data;

public class SubTableRelaBean {

	private String subTableName; // 子表表名
	
	private String mainTableColumn; // 子表与主表关联字段：主表列名
	
	private String subsTableColumn; // 子表与主表关联字段：子表列名

	public String getSubTableName() {
		return subTableName;
	}

	public void setSubTableName(String subTableName) {
		this.subTableName = subTableName;
	}

	public String getMainTableColumn() {
		return mainTableColumn;
	}

	public void setMainTableColumn(String mainTableColumn) {
		this.mainTableColumn = mainTableColumn;
	}

	public String getSubsTableColumn() {
		return subsTableColumn;
	}

	public void setSubsTableColumn(String subsTableColumn) {
		this.subsTableColumn = subsTableColumn;
	}
	
	
}
