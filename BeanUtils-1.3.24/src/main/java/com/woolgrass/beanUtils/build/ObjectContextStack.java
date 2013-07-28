package com.woolgrass.beanUtils.build;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


public class ObjectContextStack {
	protected Stack<ObjectContext> contextStack = new Stack<ObjectContext>();
	
//	public RootObjectContext createRootContext(Object targetObject) {
//		if(!contextStack.empty())
//			throw new IllegalStateException("RootContext has already been created");
//		ObjectCategory category = ObjectCategory.checkCategory(targetObject);
//		RootObjectContext rootCtx = new RootObjectContext(targetObject, category);
//		contextStack.push(rootCtx);
//		return rootCtx;
//	}
	
	public ObjectContext getTopContext() {
		int len = contextStack.size();
		if(len == 0)
			return null;
		else
			return contextStack.peek();
	}
	
	
	public ObjectContext peek() {
		return contextStack.peek();
	}
	
	public void push(ObjectContext ctx) {
		contextStack.push(ctx);
	}
	
	public ObjectContext pop() {
		return contextStack.pop();
	}
	
	public int size() {
		return contextStack.size();
	}
	
	public boolean empty() {
		return contextStack.empty();
	}
//	public FieldContext createFieldContext(Object fieldValue, ObjectCategory category, Object owner, String fieldName, Field definition) {
//		if(contextStack.empty())
//			throw new IllegalStateException("RootContext has to be created first");
//		
//		ObjectContext topNonDummyCtx = getTopNonDummyContext();
//		int curLevel = topNonDummyCtx.getLevel();
//		FieldContext ctx = new FieldContext(fieldValue, topNonDummyCtx, category, owner, fieldName, definition, curLevel + 1);
//	}
}
