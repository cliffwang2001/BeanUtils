package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Field;

import com.woolgrass.beanUtils.ObjectCategory;

public class ArrayFieldContext extends IndexedFieldContext {

	//protected Type elementType;
	
	public ArrayFieldContext(String name, Object value, Field definition,
			TargetContext parentContext, ObjectCategory category,
			Traverser traverser, int level) {
		super(name, value, definition, parentContext, category, traverser, level);
	}

	public Class<?> getElementDeclaredType() {
		return definition.getType().getComponentType();
	}

	public void incrementLevel() {
		level++;
	}
}
