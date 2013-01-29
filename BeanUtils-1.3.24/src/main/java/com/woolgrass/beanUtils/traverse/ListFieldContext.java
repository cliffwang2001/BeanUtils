package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.ReflectionUtils;

public class ListFieldContext extends IndexedFieldContext {

	public ListFieldContext(String name, Object value, Field definition,
			TargetContext parentContext, ObjectCategory category,
			Traverser traverser, int level) {
		super(name, value, definition, parentContext, category, traverser, level);
		// TODO Auto-generated constructor stub
	}

	public Type getElementDeclaredType() {
//		Type genericType = definition.getGenericType();
//		if (genericType instanceof ParameterizedType) {  
//            ParameterizedType pt = (ParameterizedType) genericType;  
//              
//            Type elementType = pt.getActualTypeArguments()[0];
//            return elementType;
//        } else {
//        	return Object.class;
//        }
		
		
		Type[] arguments = ReflectionUtils.getActualTypeArguments(definition);
		if(arguments == null)
			return Object.class;
		else
			return arguments[0];
	}
}
