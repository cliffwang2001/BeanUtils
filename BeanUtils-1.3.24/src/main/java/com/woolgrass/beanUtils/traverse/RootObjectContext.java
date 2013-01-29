package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Type;

import com.woolgrass.beanUtils.ObjectCategory;

public class RootObjectContext implements TargetContext {

	static protected String NAME = "Root";
	
	//protected TargetContext ParentContext;
	protected Object value;
	
	protected ObjectCategory category;
	protected Traverser traverser;
//	protected int level;
	protected Type declaredType;
	
	
	public RootObjectContext(Object value, ObjectCategory category,
			Traverser traverser, Type declaredType) {
		this.value = value;
		this.category = category;
		this.traverser = traverser;
		this.declaredType = declaredType;
	}

	public TargetContext getParentContext() {
		return null;
	}

	public Object getValue() {
		return value;
	}

	public String getName() {
		return NAME;
	}

	public ObjectCategory getCategory() {
		return category;
	}

	public Traverser getTraverser() {
		return traverser;
	}

	public int getLevel() {
		// TODO Auto-generated method stub
		return 1;
	}

	public Type getDeclaredType() {
		// TODO Auto-generated method stub
		return declaredType;
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return "";
	}
	public Class<?> getParentObjectClass() {
		throw new IllegalStateException("Root context doesn't have a parent");
		
	}
}
