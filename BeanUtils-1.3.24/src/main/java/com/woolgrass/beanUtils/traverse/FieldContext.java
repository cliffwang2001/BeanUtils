package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import com.woolgrass.beanUtils.ObjectCategory;

public class FieldContext implements TargetContext {
	protected String name;
	protected TargetContext parentContext;
	protected Object value;
	protected ObjectCategory category;
	protected Traverser traverser;
	protected Field definition;
	protected int level;

	public FieldContext(String name, Object value, Field definition, TargetContext parentContext, 
			ObjectCategory category, Traverser traverser, int level) {
		this.name = name;
		this.parentContext = parentContext;
		this.value = value;
		this.category = category;
		this.traverser = traverser;
		this.definition = definition;
		this.level = level;
	}
	
	public String getName() {
		return name;
	}
	public TargetContext getParentContext() {
		return parentContext;
	}
	public Object getValue() {
		return value;
	}
	public ObjectCategory getCategory() {
		return category;
	}
	public Traverser getTraverser() {
		return traverser;
	}
	public Field getDefinition() {
		return definition;
	}

	public int getLevel() {
		// TODO Auto-generated method stub
		return level;
	}

	public Type getDeclaredType() {
		// TODO Auto-generated method stub
		return definition.getGenericType();
	}

	public Class<?> getParentObjectClass() {
		return parentContext.getValue().getClass();
	}
	

}
