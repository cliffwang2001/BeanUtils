package com.woolgrass.beanUtils;

import java.util.Stack;

public class StackTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Stack<Integer> intStack = new Stack<Integer>();
		intStack.push(1);
		intStack.push(2);
		intStack.push(3);
		System.out.println(intStack.size());
		System.out.println(intStack.search(3));

	}

}
