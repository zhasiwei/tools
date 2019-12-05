package com.zsw.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommonUtil {
	
	private static Map<String, String> PROPERTYMAP = null;
	
	private static String FILE_NAME = "application.properties";
	
	private static String FILE_PREFIX = null;
	
	public static Map<String, String> getPropertyMap(){
		if(CommonUtil.isEmpty(PROPERTYMAP)) {
			initPropertyMap();
		}
		return PROPERTYMAP;
	}

	private static void initPropertyMap() {
		PROPERTYMAP = new HashMap<String, String>();
		initFilePrefix();
		String filePath = FILE_PREFIX + FILE_NAME;
		System.out.println("==========================初始化配置文件==========================");
		System.out.println("==========================文件路径：" + filePath + "==========================");
	
		File file = new File(filePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str = null;
			String[] strArry;
			while((str = br.readLine()) != null) {
				if(!str.startsWith("#")) {
					strArry = str.split("=");
					PROPERTYMAP.put(strArry[0], strArry[1]);
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
