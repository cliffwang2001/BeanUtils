package com.woolgrass.beanUtils.property;

import java.lang.reflect.Type;
import java.util.List;

import com.woolgrass.beanUtils.property.PropertyType.IncorrectPropertyTypeException;


/**
 * @author Cliff Wang
 * 
 * Get and Set a property of a Java object specified in the format similar to  Apache Commons BeanUtils:
 * Simple property: "firstName"
 * Indexed property: "children[1]" - List, Array
 * Mapped property: "address(home)" - Map
 * Nested property: "address(home).city"
 */
public class PropertyUtils {

	public static Object getPropertyValue(Object instance, String fieldPath, boolean throwExceptionWhenNullInMiddle) {
		if(instance == null)
			throw new IllegalArgumentException("argument instance is null");
		if(fieldPath == null || fieldPath.trim().length() == 0)
			throw new IllegalArgumentException("fieldPath is null or blank");
		fieldPath = fieldPath.trim();
		
		PropertyPathResolver pathResolver = new PropertyPathResolver(fieldPath);
		pathResolver.resolve();
		
		PropertyInfo propInfo = null;	
		List<PropertyContext> propContextList = pathResolver.getResolvedProperties();
		for(int i = 0; i < propContextList.size(); i++) {
			PropertyContext pc = propContextList.get(i);
			PropertyInfo owner = null;
			if(i == 0) {
				owner = new PropertyInfo();
				owner.setPropValue(instance);
				owner.setPropName(instance.getClass().getName());
				owner.setPropDeclareType(instance.getClass());
				owner.setRoot(true);
			}
			else {
				owner = propInfo;
			}
			
			propInfo = extractPropertyValue(pc, owner);			
			if(propInfo.getPropValue() == null) {
				if(i < propContextList.size() - 1 && throwExceptionWhenNullInMiddle)
					throw  new IllegalStateException(propInfo.getPropName() + " returns null");
				else
					break;
			}
		}
		
		return propInfo.getPropValue();
	}


	public static void setPropertyValue(Object instance, String fieldPath, Object value) {
		if(instance == null)
			throw new IllegalArgumentException("argument instance is null");
		if(fieldPath == null || fieldPath.trim().length() == 0)
			throw new IllegalArgumentException("fieldPath is null or blank");
		fieldPath = fieldPath.trim();
		
		PropertyPathResolver pathResolver = new PropertyPathResolver(fieldPath);
		pathResolver.resolve();
		
		PropertyInfo propInfo = null;	
		List<PropertyContext> propContextList = pathResolver.getResolvedProperties();
		for(int i = 0; i < propContextList.size() - 1; i++) {
			PropertyContext pc = propContextList.get(i);
			PropertyInfo owner = null;
			if(i == 0) {
				owner = new PropertyInfo();
				owner.setPropValue(instance);
				owner.setPropName(instance.getClass().getName());
				owner.setPropDeclareType(instance.getClass());
				owner.setRoot(true);
			}
			else {
				owner = propInfo;
			}
			
			propInfo = extractPropertyValue(pc, owner);			
			if(propInfo.getPropValue() == null) {
					throw  new IllegalStateException(propInfo.getPropName() + " returns null");
			}
		}
		
		setPropertyValue(propContextList.get(propContextList.size() -1), propInfo, value);
	}
	
	protected static PropertyInfo extractPropertyValue(PropertyContext pc, PropertyInfo owner) {
		PropertyInfo propInfo = null;
		PropertyType type = pc.getPropertyType();
		if(type == PropertyType.FIRSTSIMPLE) {			
			propInfo = PropertyType.FIRSTSIMPLE.getPropertyValue(pc, owner);
		}
		else if(type == PropertyType.NESTED) {
			propInfo = PropertyType.NESTED.getPropertyValue(pc, owner);				
		}
		else if(type == PropertyType.INDEXED) {
			try {
				propInfo = PropertyType.INDEXED.getPropertyValue(pc, owner);	
			}catch(IncorrectPropertyTypeException typeEx) {
				propInfo = PropertyType.MAPPED.getPropertyValue(pc, owner);
			}
		}
		else if(type == PropertyType.MAPPED) {
			try {
				propInfo = PropertyType.MAPPED.getPropertyValue(pc, owner);
			}catch(IncorrectPropertyTypeException typeEx) {
				propInfo = PropertyType.INDEXED.getPropertyValue(pc, owner);
			}
		}
		return propInfo;
	}
	
	protected static void setPropertyValue(PropertyContext pc, PropertyInfo owner, Object value) {
		PropertyType type = pc.getPropertyType();
		if(type == PropertyType.FIRSTSIMPLE) {			
			PropertyType.FIRSTSIMPLE.setPropertyValue(pc, owner, value);
		}
		else if(type == PropertyType.NESTED) {
			PropertyType.NESTED.setPropertyValue(pc, owner, value);				
		}
		else if(type == PropertyType.INDEXED) {
			try {
				PropertyType.INDEXED.setPropertyValue(pc, owner, value);
			}catch(IncorrectPropertyTypeException typeEx) {
				PropertyType.MAPPED.setPropertyValue(pc, owner, value);
			}
		}
		else if(type == PropertyType.MAPPED) {
			try {
				PropertyType.MAPPED.setPropertyValue(pc, owner, value);
			}catch(IncorrectPropertyTypeException typeEx) {
				PropertyType.INDEXED.setPropertyValue(pc, owner, value);
			}
		}
		
	}
	
	static protected class PropertyInfo {
				
		protected Object propValue;
		protected String propName;
		protected Type propDeclareType;
		protected boolean root;
		
		public Object getPropValue() {
			return propValue;
		}
		public void setPropValue(Object propValue) {
			this.propValue = propValue;
		}
		public String getPropName() {
			return propName;
		}
		public void setPropName(String propName) {
			this.propName = propName;
		}
		public Type getPropDeclareType() {
			return propDeclareType;
		}
		public void setPropDeclareType(Type propDeclareType) {
			this.propDeclareType = propDeclareType;
		}
		public boolean isRoot() {
			return root;
		}
		public void setRoot(boolean root) {
			this.root = root;
		}
			
	}
}
