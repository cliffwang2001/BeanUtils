package com.woolgrass.beanUtils.build;

import java.util.Stack;


public abstract class ProcessState {
	protected Stack<String> elementsStack = new Stack<String>();
	
	public Stack<String> getElementsStack() {
		return elementsStack;
	}
	public abstract ProcessState processElement(String nextElement);
	public abstract void endElement(String element, String text) ;
	
};
