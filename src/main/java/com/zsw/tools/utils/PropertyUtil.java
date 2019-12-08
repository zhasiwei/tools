package com.zsw.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zsw.tools.utils.data.MysqlUtil;
import com.zsw.tools.utils.data.SubTableRelaBean;
import com.zsw.tools.utils.data.TableBean;

public class PropertyUtil {

	
	static {
		System.out.println("PropertyUtil.enclosing_method()");
	}
	private static String FILE_NAME = "application.properties";
	
	public static Map<String, String> PROPERTYMAP;
	
	private static MysqlUtil mysqlUtil;
	
	private static List<TableBean> tableBeanList = new ArrayList<>();
	
	static{
		PROPERTYMAP = new HashMap<String, String>();
//		initFilePrefix();
//		String filePath = CommonUtil.class.getResource(FILE_NAME).getPath();
		System.out.println("==========================初始化配置文件==========================");
		System.out.println("==========================文件路径：" + FILE_NAME + "==========================");
	
		File file = new File(FILE_NAME);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str = null;
			String[] strArry;
			String tableName = null;
			String sql = null;
			String subReal = null;
			List<SubTableRelaBean> subRelaList;
			SubTableRelaBean subRela;
			int count = 0;
			TableBean table = null;
			while((str = br.readLine()) != null) {
				if(!str.startsWith("#")) {
					//解析文件
					if(CommonUtil.isEmpty(str)) {
						continue;
					}
					if(!str.startsWith("$")) {
						strArry = str.split("=");
						PROPERTYMAP.put(strArry[0].trim(), strArry[1].trim());
					}
					// 解析sql
					else {
						str = str.substring(1).trim();
						if(count%3 == 0) {
							table = new TableBean();
							table.setTargeTableName(str);
							count++;
						}else if(count % 3 == 1) {
							table.setQuerySql(str);
							count++;
						}else if(count %3 == 2) {
							subRelaList = new ArrayList<SubTableRelaBean>();
							if(!"no".equals(str)) {
								String[] arry = str.split(",");
								for(String sub : arry) {
									subRela = new SubTableRelaBean();
									String[] subArry = sub.split("\\|");
									subRela.setSubTableName(subArry[0]);
									subRela.setMainTableColumn(subArry[1]);
									subRela.setSubsTableColumn(subArry[2]);
									subRelaList.add(subRela);
								}
							}
							table.setSubTableList(subRelaList);
							tableBeanList.add(table);
							count = 0;
						}
					}
				}
			}
			mysqlUtil = new MysqlUtil(PROPERTYMAP);
			System.out.println("==========================初始化配置文件完成==========================");
		} catch (Exception e) {
			System.out.println("读取配置文件失败");
			e.printStackTrace();
		}
	}

	public static MysqlUtil getMysqlUtil() {
		return mysqlUtil;
	}

	public static void setMysqlUtil(MysqlUtil mysqlUtil) {
		PropertyUtil.mysqlUtil = mysqlUtil;
	}

	public static List<TableBean> getTableBeanList() {
		return tableBeanList;
	}

	public static void setTableBeanList(List<TableBean> tableBeanList) {
		PropertyUtil.tableBeanList = tableBeanList;
	}
	
	
}
