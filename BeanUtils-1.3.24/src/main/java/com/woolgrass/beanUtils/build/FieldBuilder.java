package com.woolgrass.beanUtils.build;

public interface FieldBuilder {

	public FieldBuilder startNextElement(String nextElement);
	public FieldBuilder endCurrentElement(String element, String text);
}
