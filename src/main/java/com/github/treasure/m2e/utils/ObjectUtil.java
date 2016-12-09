package com.github.treasure.m2e.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 对象相互转换
 * 
 * @author qianxiaofei
 */
public class ObjectUtil {

    /** 将Map转成具体对象 */
    // -------另一种方案------map to json ,json to 实体对象
    public static void map2Object(Map<String, String> map, Object object) throws Exception {
	Field[] fields = object.getClass().getDeclaredFields();
	for (Field field : fields) {
	    int mod = field.getModifiers();
	    if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
		continue;
	    }
	    String name = field.getName();
	    String value = map.get(name);
	    if (StringUtils.isBlank(value)) {
		continue;
	    }
	    field.setAccessible(true);
	    String filedC = field.getType().getName();
	    if ("java.lang.Integer".equals(filedC)) {
		field.set(object, Integer.valueOf(value));
	    } else if ("java.lang.Short".equals(filedC)) {
		field.set(object, Short.valueOf(value));
	    } else if ("java.lang.Float".equals(filedC)) {
		field.set(object, Float.valueOf(value));
	    } else if ("java.lang.Long".equals(filedC)) {
		field.set(object, Long.valueOf(value));
	    } else if ("java.lang.Boolean".equals(filedC)) {
		field.set(object, Boolean.valueOf(value));
	    } else if ("java.lang.Double".equals(filedC)) {
		field.set(object, Double.valueOf(value));
	    } else if ("java.math.BigDecimal".equals(filedC)) {
		field.set(object, new BigDecimal(value));
	    } else if ("java.util.Date".equals(filedC)) {
		Date d = DateUtil.parse(value, DateUtil.ALL + ".SSS");
		if (d == null) {
		    d = DateUtil.parse(value, DateUtil.ALL);
		    if (d == null) {
			SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
			d = sf.parse(value);
		    }
		}
		field.set(object, d);
	    } else if ("java.lang.String".equals(filedC)) {
		field.set(object, value);
	    }
	}
    }

}
