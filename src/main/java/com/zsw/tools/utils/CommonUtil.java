package com.zsw.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommonUtil {
	
	private static Map<String, String> FK_RELA_MAP = new HashMap<String, String>();
	
	private static String FILE_PREFIX = null;
	
	public static Map<String, String> getFKMap() throws SQLException{
		synchronized (FK_RELA_MAP) {
			if(CommonUtil.isEmpty(FK_RELA_MAP)) {
				FK_RELA_MAP = new DataSourceUtil().getFkTbale();
			}
		}
		return FK_RELA_MAP;
	}
	
	// 初始化文件路径前缀
	private static void initFilePrefix() {
		System.out.println(CommonUtil.class);
		System.out.println(CommonUtil.class.getResource("/").getPath());
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
