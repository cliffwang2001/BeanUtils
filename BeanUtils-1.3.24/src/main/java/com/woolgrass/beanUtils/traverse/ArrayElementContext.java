package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Type;

import com.woolgrass.beanUtils.ObjectCategory;

public class ArrayElementContext extends CollectionFieldElementContext  implements IndexedElementContext {

	protected static final String NAME = "Array";
	protected TargetContext parentContext;
	protected Object value;
	protected ObjectCategory category;
	protected Traverser traverser;
	protected Object arrayObject;
	protected int arraySize;
	protected int index;
	protected Type declaredType;
	
	
	public ArrayElementContext(Object value, Type declaredType, Object arrayObject, int index, int arraySize,
			ObjectCategory category, TargetContext parentContext, Traverser traverser, int level) {
		this.parentContext = parentContext;
		this.value = value;
		this.category = category;
		this.traverser = traverser;
		this.arrayObject = arrayObject;
		this.index = index;
		this.arraySize = arraySize;
		this.level = level;
		this.declaredType = declaredType;
	}
	
	public String getName() {
		return NAME;
	}
	public TargetContext getParentContext() {
		return parentContext;
	}
	public Object getValue() {
		return value;
	}
	public ObjectCategory getCategory() {
		return category;
	}
	public Traverser getTraverser() {
		return traverser;
	}
	public Object getArrayObject() {
		return arrayObject;
	}
	public int getIndex() {
		return index;
	}

	public int getArraySize() {
		return arraySize;
	}

	public Type getDeclaredType() {
		return declaredType;
	}

	public Class<?> getParentObjectClass() {
		return parentContext.getValue().getClass();
	}
}
