package com.woolgrass.beanUtils.build;

public interface SetContext extends CollectionContext {
	public SetElementContext startNewElement();
	public void addNewElement(Object value);
	public void setListValue();
}
