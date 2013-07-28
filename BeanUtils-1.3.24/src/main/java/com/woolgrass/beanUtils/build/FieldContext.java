package com.woolgrass.beanUtils.build;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.ReflectionUtils;

public class FieldContext implements ObjectContext {
	protected Object value;
	protected ObjectContext parentContext;
	protected ObjectCategory category;
	protected Object owner;
	protected String fieldName;
	protected Field definition;
	protected Class<?> type;
	protected int level;
	
	public FieldContext(Object value, ObjectContext parentContext,
			ObjectCategory category, Object owner, String fieldName,
			Field definition, int level) {
		super();
		this.value = value;
		this.parentContext = parentContext;
		this.category = category;
		this.owner = owner;
		this.fieldName = fieldName;
		this.definition = definition;
		this.level = level;
	}

	public ObjectCategory getCategory() {
		return category;
	}

	public Object getValue() {
		return value;
	}

	public ObjectContext getParentContext() {
		return parentContext;
	}


	public Object getOwner() {
		return owner;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Field getDefinition() {
		return definition;
	}

	public void setValue(Object value) {
		ReflectionUtils.setValue(owner, fieldName, value);
		this.value = value;
	}

	public int getLevel() {
		return level;
	}

	public Class<?> getType() {
		
		return definition.getType();
	}

	public Class<?> getParentObjectClass() {
		return parentContext.getValue().getClass();
	}

}
