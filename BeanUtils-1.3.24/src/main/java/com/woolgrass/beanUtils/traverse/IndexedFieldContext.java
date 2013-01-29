package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import com.woolgrass.beanUtils.ObjectCategory;

public abstract class IndexedFieldContext extends FieldContext {

	public IndexedFieldContext(String name, Object value, Field definition,
			TargetContext parentContext, ObjectCategory category,
			Traverser traverser, int level) {
		super(name, value, definition, parentContext, category, traverser, level);
	}

	public abstract Type getElementDeclaredType();
}
