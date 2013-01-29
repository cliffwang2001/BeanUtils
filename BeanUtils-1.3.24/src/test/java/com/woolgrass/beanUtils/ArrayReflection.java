package com.woolgrass.beanUtils;

import java.lang.reflect.Array;

public class ArrayReflection {
	  public static void main(String args[]) {
	    String data[] = new String[3];
	    data[0] = "Java";
	    printType(data);
	  }

	  private static void printType(Object object) {
	    Class type = object.getClass();
	    if (type.isArray()) {
	      Class dataType = type.getComponentType();
	      System.out.println("Array of: " + dataType);
	      System.out.println(" Length: " + Array.getLength(object));
	    }
	  }
}
