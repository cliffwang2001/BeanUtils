package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Type;

import com.woolgrass.beanUtils.ObjectCategory;

public interface TargetContext {
	public TargetContext getParentContext();
	public Object getValue();
	public Type getDeclaredType();
	public String getName();
	public ObjectCategory getCategory();
	public Traverser getTraverser();
	public int getLevel();
	public Class<?> getParentObjectClass();
}
