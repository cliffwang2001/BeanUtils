package com.woolgrass.beanUtils.build;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.woolgrass.beanUtils.ObjectCategory;

public class ArrayRootContext implements ArrayContext {
	protected List<Object> listForArray = new ArrayList<Object>();
	
	protected Object value;
	protected Class<?> type;
	
	public ArrayRootContext(Class<?> targetClass) {
		if(!targetClass.isArray())
			throw new IllegalArgumentException("targetClass is not of Array type");
		this.type = targetClass;
	}
	
	public Object getValue() {
		return value;
	}
	public ObjectContext getParentContext() {
		return null;
	}
	public ObjectCategory getCategory() {
		return ObjectCategory.ARRAY;
	}
	public Class<?> getType() {
		return type;
	}
	public int getLevel() {
		return 1;
	}
	public void setValue(Object value) {
		this.value = value;
		
	}
	public ArrayElementContext startNewElement() {
		Class<?> elemCls = getElementType();
		int curLevel = this.getLevel();
		ObjectCategory category = ObjectCategory.checkCategory(elemCls);
		ArrayElementContext elemCtx = new ArrayElementContext(null, this, category, elemCls, curLevel + 1, listForArray.size());
		return elemCtx;		
	}
	
	public void addNewElement(Object value) {
		listForArray.add(value);
		
	}
	
	public void setArrayValueFromList() {
		setValue(getArray());		
	}

	public Class<?> getElementType() {
		return type.getComponentType();
	}
	
	protected Object getArray() {
		Class<?> elemCls = getElementType();
		int listSize = listForArray.size();
		Object arrayObj = Array.newInstance(elemCls, listSize);
		for(int i = 0; i < listSize; i++) {
			Array.set(arrayObj, i, listForArray.get(i));
		}
		return arrayObj;
	}
}
