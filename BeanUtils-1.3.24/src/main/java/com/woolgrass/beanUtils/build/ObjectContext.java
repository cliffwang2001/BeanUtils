package com.woolgrass.beanUtils.build;

import com.woolgrass.beanUtils.ObjectCategory;

public interface ObjectContext {
	public Object getValue();
	public ObjectContext getParentContext();
	public ObjectCategory getCategory();
	public int getLevel();
	public Class<?> getType();
	public void setValue(Object value);
}
