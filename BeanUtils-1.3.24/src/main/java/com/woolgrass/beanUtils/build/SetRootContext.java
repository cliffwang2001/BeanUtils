package com.woolgrass.beanUtils.build;

import java.util.HashSet;
import java.util.Set;

import com.woolgrass.beanUtils.ObjectCategory;

public class SetRootContext implements SetContext {
	protected Set<Object> tempSet = new HashSet<Object>();
	
	protected Object value;
	protected Class<?> type;
	protected Class<?> elementType;
	
	public SetRootContext(Class<?> type, Class<?> elementType) {
		super();
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
		return ObjectCategory.SET;
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

	public SetElementContext startNewElement() {
		Class<?> elemCls = getElementType();
		if(elemCls == null)
			throw new IllegalStateException("Can't identifiy the type of the Set element");
		int curLevel = this.getLevel();
		ObjectCategory category = ObjectCategory.checkCategory(elemCls);
		SetElementContext elemCtx = new SetElementContext(null, this, category,elemCls, curLevel + 1, tempSet.size());
		return elemCtx;	
	}

	public void addNewElement(Object value) {
		tempSet.add(value);
	}

	public void setListValue() {
		setValue(tempSet);
	}

}
