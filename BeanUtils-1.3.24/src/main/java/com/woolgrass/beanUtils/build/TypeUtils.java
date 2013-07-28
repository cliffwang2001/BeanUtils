package com.woolgrass.beanUtils.build;

public class TypeUtils {
	
	public static boolean checkTypeCompatibility(Class<?> type, String typeName) {

		if(type.equals(Integer.class) || type.equals(int.class)) {
			if(typeName.equalsIgnoreCase("Integer") || typeName.equalsIgnoreCase("int"))
				return true;
			else
				return false;
		}else if(type.equals(Long.class) || type.equals(long.class)) {
			if(typeName.equalsIgnoreCase("Long") || typeName.equalsIgnoreCase("long"))
				return true;
			else
				return false;
		}else if(type.equals(Short.class) || type.equals(short.class)) {
			if(typeName.equalsIgnoreCase("Short") || typeName.equalsIgnoreCase("short"))
				return true;
			else
				return false;
		}
		else if(type.equals(Byte.class) || type.equals(byte.class)) {
			if(typeName.equalsIgnoreCase("Byte") || typeName.equalsIgnoreCase("byte"))
				return true;
			else
				return false;
		}
		else if(type.equals(Float.class) || type.equals(float.class)) {
			if(typeName.equalsIgnoreCase("Float") || typeName.equalsIgnoreCase("float"))
				return true;
			else
				return false;
		}
		else if(type.equals(Double.class) || type.equals(double.class)) {
			if(typeName.equalsIgnoreCase("Double") || typeName.equalsIgnoreCase("double"))
				return true;
			else
				return false;
		}
		else if(type.equals(Boolean.class) || type.equals(boolean.class)) {
			if(typeName.equalsIgnoreCase("Boolean") || typeName.equalsIgnoreCase("boolean"))
				return true;
			else
				return false;
		}
		else if(type.equals(Character.class) || type.equals(char.class)) {
			if(typeName.equalsIgnoreCase("Character") || typeName.equalsIgnoreCase("char"))
				return true;
			else
				return false;
		}
		else if(type.equals(Object.class)) {
			return true;
		}
		else {
			String fullName = type.getName();
			String shortName = type.getSimpleName();
			if(fullName.equalsIgnoreCase(typeName) || shortName.equalsIgnoreCase(typeName)) {
				return true;
			}
			else
				return false;
		}
			
	}

}
