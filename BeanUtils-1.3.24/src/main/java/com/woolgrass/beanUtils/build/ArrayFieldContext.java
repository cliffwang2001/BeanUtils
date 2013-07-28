package com.woolgrass.beanUtils.build;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.woolgrass.beanUtils.CollectionTypeUtils;
import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.ReflectionUtils;

public class ArrayFieldContext extends FieldContext implements ArrayContext {

	protected List<Object> listForArray = new ArrayList<Object>();
//	protected Object currentValue;
	
	public ArrayFieldContext(Object value, ObjectContext parentContext,
			ObjectCategory category, Object owner, String fieldName,
			Field definition, int level) {
		super(value, parentContext, category, owner, fieldName, definition, level);
		if(value != null && value instanceof Object[]) {
			Object[] objArr = (Object[])value;
			for(Object obj : objArr) {
				listForArray.add(obj);
			}
			
		}
	}

	public Class<?> getElementType() {
		return CollectionTypeUtils.getArrayFieldElementType(definition);
	}
	
	public ArrayElementContext startNewElement() {
		
		Class<?> elemCls = getElementType();
//		currentValue = ReflectionUtils.newInstance(elemCls);
//		listForArray.add(currentValue);
		int curLevel = this.getLevel();
		ObjectCategory category = ObjectCategory.checkCategory(elemCls);
		ArrayElementContext elemCtx = new ArrayElementContext(null, this, category, elemCls, curLevel + 1, listForArray.size());
		return elemCtx;		
	}
	
//	public CollectionElementContext createNewElementObject() {
//		Class<?> elemCls = getElementType();
//		currentValue = ReflectionUtils.newInstance(elemCls);
//		listForArray.add(currentValue);
//		int curLevel = this.getLevel();
//		ObjectCategory category = ObjectCategory.checkCategory(elemCls);
//		CollectionElementContext elemCtx = new CollectionElementContext(currentValue, this, category,elemCls, curLevel + 1, listForArray.size() - 1);
//		return elemCtx;
//	}
//	
	public void addNewElement(Object value) {
		listForArray.add(value);
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
	
	public void setArrayValueFromList() {
		setValue(getArray());
	}
}
