package com.woolgrass.beanUtils.objectTracker;

import java.util.Stack;

public class ObjectPathTracker {
	protected Stack<ObjectInfo> objectPathStack = new Stack<ObjectInfo>();
	
	public void pushSimpleObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.SIMPLE, propertyName));
	}
	
	public void pushCompositeObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.COMPOSITE, propertyName));
	}

	public void pushArrayObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.ARRAY, propertyName));
	}
	
	public void pushListObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.LIST, propertyName));
	}
	
	public void pushSetObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.SET, propertyName));
	}
	
	public void pushMapObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.MAP, propertyName));
	}
	
	public void pushArrayElemObject(Object object, int index) {
		objectPathStack.push(new ObjectInfo(ObjectType.ARRAYELEM, index));
	}
	
	public void pushListElemObject(Object object, int index) {
		objectPathStack.push(new ObjectInfo(ObjectType.LISTELEM, index));
	}
	
	public void pushSetElemObject(Object object, int index) {
		objectPathStack.push(new ObjectInfo(ObjectType.SETELEM, index));
	}
	
	public void pushMapKeyObject(Object object, int index) {
		objectPathStack.push(new ObjectInfo(ObjectType.MAPKEY, index));
	}
	
	public void pushMapValueObject(Object object, int index) {
		objectPathStack.push(new ObjectInfo(ObjectType.MAPVALUE, index));
	}

	public void pop() {
		objectPathStack.pop();
	}
	
	public String getTopObjectPath() {
		return ObjectInfo.getPath(objectPathStack);
	}
}
