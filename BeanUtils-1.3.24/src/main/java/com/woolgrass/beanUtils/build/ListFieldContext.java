package com.woolgrass.beanUtils.build;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.woolgrass.beanUtils.CollectionTypeUtils;
import com.woolgrass.beanUtils.ObjectCategory;

public class ListFieldContext extends FieldContext implements ListContext{
	protected List<Object> tempList = new ArrayList<Object>();
	
	public ListFieldContext(Object value, ObjectContext parentContext,
			ObjectCategory category, Object owner, String fieldName,
			Field definition, int level) {
		super(value, parentContext, category, owner, fieldName, definition, level);
		if(value != null && value instanceof List<?>) {
			List<?> objList = (List<?>)value;
			for(Object obj : objList) {
				tempList.add(obj);
			}
			
		}
	}

	public Class<?> getElementType() {
		return CollectionTypeUtils.getListFieldElementType(definition);
	}

	public ListElementContext startNewElement() {
		Class<?> elemCls = getElementType();
		if(elemCls == null)
			throw new IllegalStateException("Can't identifiy the type of the list element");
		int curLevel = this.getLevel();
		ObjectCategory category = ObjectCategory.checkCategory(elemCls);
		ListElementContext elemCtx = new ListElementContext(null, this, category,elemCls, curLevel + 1, tempList.size());
		return elemCtx;		
	}
	
	public void addNewElement(Object value) {
		tempList.add(value);
	}
	
	public void setListValue() {
		setValue(tempList);
	}
}
