package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Type;
import java.util.Set;

import com.woolgrass.beanUtils.ObjectCategory;

public class SetElementContext implements IndexedElementContext {

	protected static final String NAME = "Set";
	
	protected Object value;
	protected Set<?> setObject;
	protected int index;
	protected ObjectCategory category;
	protected TargetContext parentContext;
	protected Traverser traverser;
	protected int level;
	protected Type declaredType;
	
	public SetElementContext(Object value, Type declaredType, Set<?> setObject, int index,
			ObjectCategory category, TargetContext parentContext,
			Traverser traverser, int level) {
		this.value = value;
		this.setObject = setObject;
		this.index = index;
		this.category = category;
		this.parentContext = parentContext;
		this.traverser = traverser;
		this.level = level;
		this.declaredType = declaredType;
	}
	public String getName() {
		return NAME;
	}
	public Object getValue() {
		return value;
	}
	public Set<?> getSetObject() {
		return setObject;
	}
	public int getIndex() {
		return index;
	}
	public ObjectCategory getCategory() {
		return category;
	}
	public TargetContext getParentContext() {
		return parentContext;
	}
	public Traverser getTraverser() {
		return traverser;
	}
	public int getLevel() {
		// TODO Auto-generated method stub
		return level;
	}

	public Type getDeclaredType() {
		return declaredType;
	}
	public Class<?> getParentObjectClass() {
		return parentContext.getValue().getClass();
	}
}
