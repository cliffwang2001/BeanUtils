package com.woolgrass.beanUtils.build;

import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.ReflectionUtils;

public class RootObjectContext implements ObjectContext {

	protected Object value;
	protected Class<?> targetClass;

	public RootObjectContext(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public Object getValue() {
		if(value ==  null)
			value = ReflectionUtils.newInstance(targetClass);;
		return value;
	}

	public ObjectContext getParentContext() {
		return null;
	}

	public ObjectCategory getCategory() {
		return ObjectCategory.COMPOSITE;
	}

	public int getLevel() {
		return 1;
	}

	public Class<?> getType() {
		
		if(value != null)
			return value.getClass();
		else
			return targetClass;
	}

	public void setValue(Object value) {
		throw new IllegalStateException("Can't set value on root context");
	}

}
