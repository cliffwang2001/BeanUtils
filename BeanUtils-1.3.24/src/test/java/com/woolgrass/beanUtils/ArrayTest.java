package com.woolgrass.beanUtils;

import java.lang.reflect.Array;

public class ArrayTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
            Class cls = Class.forName(
              "java.lang.String");
            Object arr = Array.newInstance(cls, 10);
            Array.set(arr, 5, "this is a test");
            String s = (String)Array.get(arr, 5);
            System.out.println(s);
         }
         catch (Throwable e) {
            System.err.println(e);
         }

         
         
         int dims[] = new int[]{5, 10, 15};
         Object arr 
           = Array.newInstance(Integer.TYPE, dims);
        
         Object arrobj = Array.get(arr, 3);
         Class cls = 
           arrobj.getClass().getComponentType();
         System.out.println(cls);
         arrobj = Array.get(arrobj, 5);
         Array.setInt(arrobj, 10, 37);
        
         int arrcast[][][] = (int[][][])arr;
         System.out.println(arrcast[3][5][10]);
	}

}
