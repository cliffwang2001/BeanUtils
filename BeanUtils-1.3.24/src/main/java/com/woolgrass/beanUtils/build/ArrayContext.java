package com.woolgrass.beanUtils.build;

public interface ArrayContext extends CollectionContext {
	public ArrayElementContext startNewElement();
	public void addNewElement(Object value);
	public void setArrayValueFromList();
}
