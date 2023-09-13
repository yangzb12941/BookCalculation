package org.enumUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author yangzb
 * @description 枚举工具类
 * @date 2021-08-08
 */
public class EnumUtils {
    /**
     * 判断数值是否属于枚举类的值
     * @param clzz 枚举类 Enum
     * @param key
     * @author wayleung
     * @return
     */
    public static String getLatex(Class<?> clzz,String key)
            throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        String include = "";
        if(clzz.isEnum()){
            Object[] enumConstants = clzz.getEnumConstants();
            Method getType = clzz.getMethod("getKey");
            for (Object enumConstant:enumConstants){
                if (getType.invoke(enumConstant).equals(key)) {
                    Method getTypeName = clzz.getMethod("getLatex");
                    include = getTypeName.invoke(enumConstant).toString();
                    break;
                }
            }
        }
        return include;
    }

	/**
	 * 判断数值是否属于枚举类的值
	 * @param clzz 枚举类 Enum
	 * @param key
	 * @author wayleung
	 * @return
	 */
	public static String getCalculate(Class<?> clzz,String key)
		throws InvocationTargetException,
		IllegalAccessException, NoSuchMethodException {
		String include = "";
		if(clzz.isEnum()){
			Object[] enumConstants = clzz.getEnumConstants();
			Method getType = clzz.getMethod("getKey");
			for (Object enumConstant:enumConstants){
				if (getType.invoke(enumConstant).equals(key)) {
					Method getTypeName = clzz.getMethod("getCalculate");
					include = getTypeName.invoke(enumConstant).toString();
					break;
				}
			}
		}
		return include;
	}

	/**
	 * 判断数值是否属于枚举类的值
	 * @param clzz 枚举类 Enum
	 * @param key
	 * @author wayleung
	 * @return
	 */
	public static String getLatexCal(Class<?> clzz,String key)
		throws InvocationTargetException,
		IllegalAccessException, NoSuchMethodException {
		String include = "";
		if(clzz.isEnum()){
			Object[] enumConstants = clzz.getEnumConstants();
			Method getType = clzz.getMethod("getKey");
			for (Object enumConstant:enumConstants){
				if (getType.invoke(enumConstant).equals(key)) {
					Method getTypeName = clzz.getMethod("getLatexCal");
					include = getTypeName.invoke(enumConstant).toString();
					break;
				}
			}
		}
		return include;
	}
}
