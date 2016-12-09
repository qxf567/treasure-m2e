package com.github.treasure.m2e.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 驼峰命名法与数据库命名法的相互转换
 * */
public class DBKeyCovert {

    public static List<String> result = null;

    /*
     * data base column-key to java field
     */
    public static List<String> convertToJava(Collection<String> dbKeys) {
	if (result == null) {
	    result = new ArrayList<String>();
	}
	if (dbKeys != null && dbKeys.size() > 0) {
	    for (String key : dbKeys) {
		String[] words = key.split("_");
		String r = toUppercase4FirstLetter(words);
		result.add(r);
	    }
	}
	return result;
    }

    public static String convertToJava(String dbKey) {
	String[] words = dbKey.split("_");
	String r = toUppercase4FirstLetter(words);
	return r;
    }

    /*
     * Java field to data base column-key
     */

    public void getDBKey(String... javaFieldNames) {
	if (javaFieldNames != null && javaFieldNames.length > 0) {
	    for (String name : javaFieldNames) {
		StringBuffer buffer = new StringBuffer();
		char[] array = name.toCharArray();
		List<Integer> insertIndexes = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
		    Character c = array[i];
		    if (i != 0 && Character.isUpperCase(c)) {
			insertIndexes.add(i);
		    }
		}
		if (insertIndexes.size() > 0) {
		    int flag = 0;
		    for (int j = 0; j < insertIndexes.size(); j++) {
			String word = toLowercase4FirstLetter(name.substring(flag, insertIndexes.get(j)));
			buffer.append(word).append("_");
			flag = insertIndexes.get(j);
		    }
		    String last = toLowercase4FirstLetter(name.substring(flag));
		    buffer.append(last);
		    System.out.println(buffer.toString());
		} else {
		    System.out.println(name);
		}
	    }
	}
    }

    private static String toUppercase4FirstLetter(String... words) {
	StringBuffer buffer = new StringBuffer();
	if (words != null && words.length > 0) {
	    for (int i = 0; i < words.length; i++) {
		String word = words[i];
		String firstLetter = word.substring(0, 1);
		String others = word.substring(1);
		String upperLetter = null;
		if (i != 0) {
		    upperLetter = firstLetter.toUpperCase();
		} else {
		    upperLetter = firstLetter;
		}
		buffer.append(upperLetter).append(others);
	    }
	    return buffer.toString();
	}
	return "";
    }

    private String toLowercase4FirstLetter(String word) {
	if (word != null && word.length() > 0) {
	    String firstLetter = word.substring(0, 1);
	    String others = word.substring(1);
	    return firstLetter.toLowerCase() + others;
	} else {
	    return "";
	}
    }
}
