package com.zsw.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommonUtil {
	
	private static Map<String, String> PROPERTYMAP = null;
	
	private static Map<String, String> SQL_PROPERTYMAP = null;
	
	private static Map<String, String> FK_RELA_MAP = new HashMap<String, String>();
	
	private static String FILE_NAME = "application.properties";
	
	private static String FILE_PREFIX = null;
	
	public static Map<String, String> getPropertyMap(){
		if(CommonUtil.isEmpty(PROPERTYMAP)) {
			initPropertyMap();
		}
		return PROPERTYMAP;
	}

	public static Map<String, String> getFKMap() throws SQLException{
		synchronized (FK_RELA_MAP) {
			if(CommonUtil.isEmpty(FK_RELA_MAP)) {
				FK_RELA_MAP = new DataSourceUtil().getFkTbale();
			}
		}
		return FK_RELA_MAP;
	}
	
	public static Map<String, String> getSQLPropertyMap() throws SQLException{
		if(CommonUtil.isEmpty(SQL_PROPERTYMAP)) {
			initPropertyMap();
		}
		return SQL_PROPERTYMAP;
	}
	private static void initPropertyMap() {
		PROPERTYMAP = new HashMap<String, String>();
		SQL_PROPERTYMAP = new HashMap<String, String>();
		initFilePrefix();
		String filePath = FILE_PREFIX + FILE_NAME;
		System.out.println("==========================初始化配置文件==========================");
		System.out.println("==========================文件路径：" + filePath + "==========================");
	
		File file = new File(filePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str = null;
			String[] strArry;
			String tableName = null;
			String sql = null;
			int count = 0;
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
						if(count%2 == 0) {
							tableName = str;
							count++;
						}else if(count % 2 == 1) {
							sql = str;
							SQL_PROPERTYMAP.put(tableName.substring(1).trim(), sql.substring(1).trim());
							count = 0;
						}
					}
				}
			}
			System.out.println("==========================初始化配置文件完成==========================");
		} catch (Exception e) {
			System.out.println("读取配置文件失败");
			e.printStackTrace();
		}
	}
	
	// 初始化文件路径前缀
	private static void initFilePrefix() {
		File f = new File(CommonUtil.class.getResource("/").getPath()); 
		String currentAbsPath = f.getAbsolutePath();
		FILE_PREFIX = currentAbsPath.substring(0, currentAbsPath.indexOf("zswTools"));
		System.out.println("==========================获取当前文件目录==========================" );
		System.out.println("==========================" + FILE_PREFIX + "==========================");
	}
	
	public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
	
	/**
     * Description:字符串不为NULL也不为空 <br>
     * 
     * @author 王伟 <br>
     * @param str <br>
     * @return <br>
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Description: 判断数组是否为NULL或为空<br>
     * 
     * @author yang.zhipeng <br>
     * @taskId <br>
     * @param t <br>
     * @param <T> <br>
     * @return <br>
     */
    public static <T> boolean isEmpty(T[] t) {
        return t == null || t.length == 0;
    }

    /**
     * 判断数组不为NULL也不为空
     * 
     * @param t <br>
     * @param <T> <br>
     * @return <br>
     */
    public static <T> boolean isNotEmpty(T[] t) {
        return !isEmpty(t);
    }

    /**
     * Description: 集合是否为NULL或为空<br>
     * 
     * @author 王伟 <br>
     * @param col <br>
     * @return <br>
     */
    public static boolean isEmpty(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    /**
     * Description:集合不为NULL也不为空 <br>
     * 
     * @author 王伟 <br>
     * @param col <br>
     * @return <br>
     */
    public static boolean isNotEmpty(Collection<?> col) {
        return !isEmpty(col);
    }

    /**
     * Description: map是否为NULL或为空<br>
     * 
     * @author 王伟 <br>
     * @param map <br>
     * @return <br>
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Description:map不为NULL也不为空 <br>
     * 
     * @author 王伟 <br>
     * @param map <br>
     * @return <br>
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
