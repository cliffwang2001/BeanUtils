package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.ReflectionUtils;

public class MapFieldContext extends IndexedFieldContext{

	public MapFieldContext(String name, Object value, Field definition,
			TargetContext parentContext, ObjectCategory category,
			Traverser traverser, int level) {
		super(name, value, definition, parentContext, category, traverser, level);
		// TODO Auto-generated constructor stub
	}

	public Type getEntryKeyDeclaredType() {
		Type[] arguments = ReflectionUtils.getActualTypeArguments(definition);
		if(arguments == null)
			return Object.class;
		else
			return arguments[0];
	}
	
	public Type getEntryValueDeclaredType() {
		Type[] arguments = ReflectionUtils.getActualTypeArguments(definition);
		if(arguments == null)
			return Object.class;
		else
			return arguments[1];
	}

	@Override
	public Type getElementDeclaredType() {
		throw new IllegalStateException("MapFieldContext doesn't handle getElementDeclaredType() method");
	}
}
