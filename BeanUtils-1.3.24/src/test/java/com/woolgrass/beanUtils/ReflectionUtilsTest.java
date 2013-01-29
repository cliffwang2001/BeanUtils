package com.woolgrass.beanUtils;

import com.woolgrass.beanUtils.ReflectionUtils;

public class ReflectionUtilsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(java.lang.Boolean.class.isPrimitive());
		System.out.println(java.lang.Boolean.TYPE.isPrimitive());
		
		System.out.println(String.class.getName());
		
		String testStr = "Cliff test";
		System.out.println(testStr);
		Object value = ReflectionUtils.getValue(testStr, "Cliff");
		System.out.println(value);
		
		try
		{
			Object instance = ReflectionUtilsTest.class.newInstance();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		ReflectionUtils.newInstance(ReflectionUtilsTest.class);

	}

	public ReflectionUtilsTest(String placeHolder) 
	{
		
	}
	
	public void a(String s, int i){
		
	}
	
	
	public void a(int i, String s){
		
	}
		
	
}
