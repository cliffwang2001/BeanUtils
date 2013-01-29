package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Type;
import java.util.Map.Entry;

import com.woolgrass.beanUtils.ObjectCategory;

public class MapValueContext implements IndexedElementContext {
	protected static final String NAME = "Value";
	
	protected Object value;
	protected Type declaredType;
	protected Entry entryObject;
	protected ObjectCategory category;
	protected TargetContext parentContext;
	protected Traverser traverser;
	protected int level;
	protected int index;

	public MapValueContext(Object value, Type declaredType, Entry entryObject, int index,
			ObjectCategory category, TargetContext parentContext,
			Traverser traverser, int level) {
		this.value = value;
		this.declaredType = declaredType;
		this.entryObject = entryObject;
		this.category = category;
		this.parentContext = parentContext;
		this.traverser = traverser;
		this.level = level;
		this.index = index;
	}
	public String getName() {
		return NAME;
	}

	public Object getValue() {
		return value;
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
		// TODO Auto-generated method stub
		return declaredType;
	}
	public int getIndex() {
		return index;
	}
	public Class<?> getParentObjectClass() {
		return parentContext.getValue().getClass();
	}
}
