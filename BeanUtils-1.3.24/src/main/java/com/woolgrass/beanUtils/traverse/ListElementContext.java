package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Type;
import java.util.List;

import com.woolgrass.beanUtils.ObjectCategory;

public class ListElementContext implements IndexedElementContext {
	protected static final String NAME = "List";
	protected TargetContext parentContext;
	protected Object value;
	protected ObjectCategory category;
	protected Traverser traverser;
	protected List<?> listObject;
	protected int index;
	protected int level;
	protected Type declaredType;
	
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
	public int getIndex() {
		return index;
	}
	public List<?> getListObject() {
		return listObject;
	}
	public ListElementContext(Object value, Type declaredType, List<?> listObject, int index, ObjectCategory category, 
			TargetContext parentContext, Traverser traverser, int level) {
		this.parentContext = parentContext;
		this.value = value;
		this.category = category;
		this.traverser = traverser;
		this.listObject = listObject;
		this.index = index;
		this.level = level;
		this.declaredType = declaredType;
	}
	public int getLevel() {
		// TODO Auto-generated method stub
		return level;
	}
	public Type getDeclaredType() {
		// TODO Auto-generated method stub
		return this.declaredType;
	}

	public Class<?> getParentObjectClass() {
		return parentContext.getValue().getClass();
	}
}
