package com.woolgrass.beanUtils.build;

import java.util.ArrayList;
import java.util.List;

import com.woolgrass.beanUtils.ObjectCategory;

public class ListRootContext implements ListContext {
	protected List<Object> tempList = new ArrayList<Object>();
	
	protected Object value;
	protected Class<?> type;
	protected Class<?> elementType;
	
	
	
	public ListRootContext(Class<?> type, Class<?> elementType) {
		this.type = type;
		this.elementType = elementType;
	}

	public Object getValue() {
		return value;
	}

	public ObjectContext getParentContext() {
		return null;
	}

	public ObjectCategory getCategory() {
		return ObjectCategory.LIST;
	}

	public int getLevel() {
		return 1;
	}

	public Class<?> getType() {
		return type;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Class<?> getElementType() {
		return elementType;
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
