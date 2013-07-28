package com.woolgrass.beanUtils.build;

public interface ListContext extends CollectionContext {
	public ListElementContext startNewElement();
	public void addNewElement(Object value);
	public void setListValue();
}
