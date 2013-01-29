package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import com.woolgrass.beanUtils.ObjectCategory;

public class MapEntryContext implements TargetContext {

	protected static final String NAME = "Entry";
	
	protected Entry value;
	protected Map mapObject;
	protected int index;
	protected ObjectCategory category;
	protected TargetContext parentContext;
	protected Traverser traverser;
	protected int level;
	protected Type keyDeclaredType;
	protected Type valueDeclaredType;
	
	public MapEntryContext(Entry value, Type keyDeclaredType, Type valueDeclaredType, Map mapObject, int index,
			ObjectCategory category, TargetContext parentContext,
			Traverser traverser, int level) {
		this.value = value;
		this.mapObject = mapObject;
		this.index = index;
		this.category = category;
		this.parentContext = parentContext;
		this.traverser = traverser;
		this.level = level;
		this.keyDeclaredType = keyDeclaredType;
		this.valueDeclaredType = valueDeclaredType;
	}
	public String getName() {
		return NAME;
	}
	public Entry getValue() {
		return value;
	}
	public Map getMapObject() {
		return mapObject;
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
	
	public ObjectCategory getKeyCategroy() {
		return ObjectCategory.checkCategory(value.getKey().getClass());
	}
	public ObjectCategory getValueCategroy() {
		return ObjectCategory.checkCategory(value.getValue().getClass());
	}
	public int getLevel() {
		// TODO Auto-generated method stub
		return level;
	}
	public Type getKeyDeclaredType() {
		return keyDeclaredType;
	}
	public Type getValueDeclaredType() {
		return valueDeclaredType;
	}
	public Type getDeclaredType() {
		// TODO Auto-generated method stub
		return Entry.class;
	}

	public Class<?> getParentObjectClass() {
		return parentContext.getValue().getClass();
	}
}
