package com.woolgrass.beanUtils.objectTracker;

import java.util.Stack;

public class ObjectInfo {
	protected ObjectType type;
	protected String propertyName;
	protected int elemIndex;
	
	public ObjectInfo(ObjectType type, int elemIndex) {
		this.type = type;
		this.elemIndex = elemIndex;
	}
	

	public ObjectInfo(ObjectType type, String propertyName) {
		this.type = type;
		this.propertyName = propertyName;
	}
	
	public ObjectType getType() {
		return type;
	}

	public String getPropertyName() {
		return propertyName;
	}
	
	public int getElemIndex() {
		return elemIndex;
	}
	
	static public String getPath(Stack<ObjectInfo> objectPathStack) {
		StringBuilder sb = new StringBuilder();
		int level = 0;
		for(ObjectInfo objInfo : objectPathStack) {
			ObjectType type = objInfo.getType();
			switch(type) {
			case SIMPLE:
			case COMPOSITE:
			case ARRAY:
			case LIST:
			case SET:
			case MAP:
//				level++;
//				if(level == 1)
				sb.append(".");
				sb.append(objInfo.getPropertyName());
				break;
			case ARRAYELEM:
			case LISTELEM:
			case SETELEM:
				sb.append("[");
				sb.append(objInfo.getElemIndex());
				sb.append("]");
				break;
			case MAPKEY:
				sb.append("[");
				sb.append(objInfo.getElemIndex());
				sb.append("]");
				sb.append(".key");
				break;
			case MAPVALUE:
				sb.append("[");
				sb.append(objInfo.getElemIndex());
				sb.append("]");
				sb.append(".value");
				break;				
			}
		}
		return sb.toString();
	}
}
