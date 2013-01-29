package com.woolgrass.beanUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public enum ObjectCategory {
	UNKNOWN,
	SIMPLE , 
	COMPOSITE , 
	ARRAY, 
	LIST,
	SET,
	MAP,
	MAPENTRY;
	
	public static ObjectCategory checkCategory(Class<?> objectType) {
		ObjectCategory category;			
		if(objectType.isArray()) {								
			category = ObjectCategory.ARRAY;
		}else if(List.class.isAssignableFrom(objectType) ) {
			category = ObjectCategory.LIST;
		}else if(Set.class.isAssignableFrom(objectType) ) {
			category = ObjectCategory.SET;
		}else if(Map.class.isAssignableFrom(objectType) ) {
			category = ObjectCategory.MAP;
		}else if(Map.Entry.class.isAssignableFrom(objectType) ) {
			category = ObjectCategory.MAPENTRY;
		}else if(isJDKClass(objectType)) {
			category = ObjectCategory.SIMPLE;
		}else {
			category = ObjectCategory.COMPOSITE;
		}
		return category;
	}		
	
	public static ObjectCategory checkCategory(Object object) {
		if(object == null)
			return ObjectCategory.UNKNOWN;
		else
			return checkCategory(object.getClass());
	}
	
	private static final String JAVA_PACKAGE = "java.";
	private static final String JAVAX_PACKAGE = "javax.";
	
	protected static boolean isJDKClass(Class<?> cls) {
		return cls.isPrimitive() || cls.getName().startsWith(JAVA_PACKAGE) || cls.getName().startsWith(JAVAX_PACKAGE);
	}
	
	public static String getClassName(Class<?> objectType) {
		if(checkCategory(objectType) == ObjectCategory.SIMPLE) {
			return objectType.getSimpleName();
		}else
			return objectType.getName();
	}
	
	public static boolean isCollectionObject(ObjectCategory objCat) {
		if(objCat == ARRAY || objCat == LIST || objCat == SET )
			return true;
		else
			return false;
	}
}
