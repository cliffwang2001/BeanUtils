package com.woolgrass.beanUtils.build;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.woolgrass.beanUtils.CollectionTypeUtils;
import com.woolgrass.beanUtils.ObjectCategory;

public class SetFieldContext extends FieldContext implements SetContext {

	protected Set<Object> tempSet = new HashSet<Object>();
	
	public SetFieldContext(Object value, ObjectContext parentContext,
			ObjectCategory category, Object owner, String fieldName,
			Field definition, int level) {
		super(value, parentContext, category, owner, fieldName, definition, level);
		if(value != null && value instanceof Set<?>) {
			Set<?> objSet = (Set<?>)value;
			for(Object obj : objSet) {
				tempSet.add(obj);
			}
			
		}
	}

	public Class<?> getElementType() {
		return CollectionTypeUtils.getSetFieldElementType(definition);
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
