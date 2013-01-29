package com.woolgrass.beanUtils.objectTracker;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.woolgrass.beanUtils.traverse.TargetContext;

public class ObjectTracker {
	protected Map<String, Object> pathToObjectMap = new HashMap<String, Object>();
	protected Map<Object, String> objectTopathMap = new HashMap<Object, String>();
	protected Object rootObj;
	
	protected Stack<ObjectInfo> objectPathStack = new Stack<ObjectInfo>();
	
	public ObjectTracker(Object rootObj) {
		this.rootObj = rootObj;
	}
	
	public boolean exists(Object object) {
		boolean isRootObj = getRootObj() == object;
		if(isRootObj) 
			return true;

		String path = getPathFromObject(object);
		if(path != null) 
			return true;
		else
			return false;
	}
	
	public Object getRootObj() {
		return rootObj;
	}
	
	public Object getObjectFromPath(String objectPath) {
		return pathToObjectMap.get(objectPath);
	}
	
	public String getPathFromObject(Object object) {
		if(object == rootObj)
			return "";
		return objectTopathMap.get(object);
	}
	
	public void pushSimpleObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.SIMPLE, propertyName));
	}
	
	public void pushCompositeObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.COMPOSITE, propertyName));
		addToMaps(object);
	}

	public void pushArrayObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.ARRAY, propertyName));
		addToMaps(object);
	}
	
	public void pushListObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.LIST, propertyName));
		addToMaps(object);
	}
	
	public void pushSetObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.SET, propertyName));
		addToMaps(object);
	}
	
	public void pushMapObject(Object object, String propertyName) {
		objectPathStack.push(new ObjectInfo(ObjectType.MAP, propertyName));
		addToMaps(object);
	}
	
	public void pushArrayElemObject(Object object, int index) {
		objectPathStack.push(new ObjectInfo(ObjectType.ARRAYELEM, index));
		addToMaps(object);
	}
	
	public void pushListElemObject(Object object, int index) {
		objectPathStack.push(new ObjectInfo(ObjectType.LISTELEM, index));
		addToMaps(object);
	}
	
	public void pushSetElemObject(Object object, int index) {
		objectPathStack.push(new ObjectInfo(ObjectType.SETELEM, index));
		addToMaps(object);
	}
	
	public void pushMapKeyObject(Object object, int index) {
		objectPathStack.push(new ObjectInfo(ObjectType.MAPKEY, index));
		addToMaps(object);
	}
	
	public void pushMapValueObject(Object object, int index) {
		objectPathStack.push(new ObjectInfo(ObjectType.MAPVALUE, index));
		addToMaps(object);
	}

	protected void addToMaps(Object object) {
		String topObjPath = getTopObjectPath();
		pathToObjectMap.put(topObjPath, object);
		objectTopathMap.put(object, topObjPath);
	}
	
	public void pop() {
		objectPathStack.pop();
	}
	
	public String getTopObjectPath() {
		return ObjectInfo.getPath(objectPathStack);
	}
	
//	protected static enum ObjectType {COMPOSITE, ARRAY, LIST, SET, MAP, ARRAYELEM, LISTELEM, SETELEM, MAPKEY, MAPVALUE };
	
//	protected static class ObjectInfo {
//		protected ObjectType type;
//		protected String propertyName;
//		protected int elemIndex;
//		
//		public ObjectInfo(ObjectType type, int elemIndex) {
//			this.type = type;
//			this.elemIndex = elemIndex;
//		}
//		
//
//		public ObjectInfo(ObjectType type, String propertyName) {
//			this.type = type;
//			this.propertyName = propertyName;
//		}
//		
//		public ObjectType getType() {
//			return type;
//		}
//
//		public String getPropertyName() {
//			return propertyName;
//		}
//		
//		public int getElemIndex() {
//			return elemIndex;
//		}
//		
//	}
}
