package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.ReflectionUtils;

public class SetFieldContext extends IndexedFieldContext {

	public SetFieldContext(String name, Object value, Field definition,
			TargetContext parentContext, ObjectCategory category,
			Traverser traverser, int level) {
		super(name, value, definition, parentContext, category, traverser, level);
		// TODO Auto-generated constructor stub
	}

	public Type getElementDeclaredType() {
		Type[] arguments = ReflectionUtils.getActualTypeArguments(definition);
		if(arguments == null)
			return Object.class;
		else
			return arguments[0];
	}
}
