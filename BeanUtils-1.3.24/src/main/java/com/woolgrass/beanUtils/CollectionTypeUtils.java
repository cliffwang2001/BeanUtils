package com.woolgrass.beanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class CollectionTypeUtils {

	public static Class<?> getArrayFieldElementType(Class<?> arrayCls) {
		if(!arrayCls.isArray())
			throw new IllegalArgumentException("arrayCls is not of type array");
		
		Class<?> elemType = arrayCls.getComponentType();
		return elemType;
	}
	
	public static Class<?> getArrayFieldElementType(Field field) {
		Class<?> fieldType = field.getType();
		return getArrayFieldElementType(fieldType);
	}
	

	
	public static Class<?> getListFieldElementType(Field field) {
		Class<?> fieldType = field.getType();
		if(!List.class.isAssignableFrom(fieldType))
			throw new IllegalArgumentException("field is not of type List");
		
		Type[] arguments = ReflectionUtils.getActualTypeArguments(field);
		if(arguments == null)
			return Object.class;
		else
			return (Class<?>)arguments[0];
	}
	
	public static Class<?> getSetFieldElementType(Field field) {
		Class<?> fieldType = field.getType();
		if(!Set.class.isAssignableFrom(fieldType))
			throw new IllegalArgumentException("field is not of type List");
		
		Type[] arguments = ReflectionUtils.getActualTypeArguments(field);
		if(arguments == null)
			return Object.class;
		else
			return (Class<?>)arguments[0];
	}
	
	public static Class<?> getMapFieldKeyType(Field field) {
		Class<?> fieldType = field.getType();
		if(!Map.class.isAssignableFrom(fieldType))
			throw new IllegalArgumentException("field is not of type List");
		
		Type[] arguments = ReflectionUtils.getActualTypeArguments(field);
		if(arguments == null)
			return Object.class;
		else
			return (Class<?>)arguments[0];
	}
	
	public static Class<?> getMapFieldValueType(Field field) {
		Class<?> fieldType = field.getType();
		if(!Map.class.isAssignableFrom(fieldType))
			throw new IllegalArgumentException("field is not of type List");
		
		Type[] arguments = ReflectionUtils.getActualTypeArguments(field);
		if(arguments == null)
			return Object.class;
		else
			return (Class<?>)arguments[1];
	}
}
