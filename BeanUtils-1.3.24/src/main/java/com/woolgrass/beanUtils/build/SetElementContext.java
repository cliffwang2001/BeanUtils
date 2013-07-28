package com.woolgrass.beanUtils.build;

import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.ReflectionUtils;

public class SetElementContext implements ObjectContext{

	protected Object value;
	protected SetContext parentContext;
	protected ObjectCategory category;
	protected Class<?> type;
	protected int level;
	protected int index;
	
	public SetElementContext(Object value, SetContext parentContext,
			ObjectCategory category, Class<?> type, int level, int index) {
		this.value = value;
		this.parentContext = parentContext;
		this.category = category;
		this.type = type;
		this.level = level;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public Object getValue() {
		return value;
	}

	public ObjectContext getParentContext() {
		return parentContext;
	}

	public ObjectCategory getCategory() {
		return category;
	}

	public Class<?> getType() {
		return type;
	}

	public int getLevel() {
		return level;
	}
	
	public void setValue(Object value) {
		this.value = value;
		parentContext.addNewElement(value);
	}
	
	public void createObject() {
		Object obj = ReflectionUtils.newInstance(type);
		setValue(obj);
	}

}
