/*
 * 作者		www.TheWk.cn.vc
 * 开发环境	WindowsXp MyEclipse6.5 JDK1.6.0_17
 * 开发日期	Aug 29, 2010
 */
package com.cmcc.zysoft.sellmanager.util;

import java.lang.reflect.ParameterizedType;

/**
 * <br/> 反射工具类 <hr/>
 *
 * @author www.TheWk.cn.vc
 * @version 1.0 Aug 29, 2010
 * @class framework.utils.ReflectUtils
 */
public class ReflectUtils {

	/**
	 * 得到第一个参数的类型
	 * @param <T>
	 * @param clz
	 * @return
	 * @author www.TheWk.cn.vc
	 */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class<?> clz) {
		return (Class<T>) ((ParameterizedType) clz.getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
