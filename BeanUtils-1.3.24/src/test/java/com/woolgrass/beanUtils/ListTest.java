package com.woolgrass.beanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ListTest {

	protected List<String> strList;
	/**
	 * @param args
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws Exception {
		Field field = ListTest.class.getDeclaredField("strList");  
		ListTest listTest = new ListTest();
//		Object obj = field.get(new ListTest()); 
//		Class<?> cls = field.getType();
//		Object listObj = cls.newInstance();
		Object listObj = ArrayList.class.newInstance();
		((List)listObj).add("Bill");
		field.set(listTest, listObj);
		return;
	}

}
