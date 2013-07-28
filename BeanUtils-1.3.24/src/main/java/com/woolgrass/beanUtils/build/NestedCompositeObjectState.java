package com.woolgrass.beanUtils.build;

public class NestedCompositeObjectState extends ProcessState {

	public ProcessState processElement(String nextElement) {
		throw new IllegalStateException("Can't  process element");
	}

	public void endElement(String element, String text) {
		throw new IllegalStateException("endElement method can'be called on NestedCompositeObjectState class");
		
	}

}
