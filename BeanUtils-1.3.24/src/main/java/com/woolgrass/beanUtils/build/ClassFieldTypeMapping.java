package com.woolgrass.beanUtils.build;

import java.util.HashMap;
import java.util.Map;

import com.woolgrass.beanUtils.build.FieldTypeMapping.CollectionFieldMappingEntry;
import com.woolgrass.beanUtils.build.FieldTypeMapping.MapFieldMappingEntry;


public class ClassFieldTypeMapping {
	protected Map<Class<?>, FieldTypeMapping> classMapping = new HashMap<Class<?>, FieldTypeMapping>();
	
	public void addFieldMapping(FieldTypeMapping fieldMapping) {
		classMapping.put(fieldMapping.getOwnerClass(), fieldMapping);
	}
	
	public void addSimpleMappingEntry(Class<?> ownerClass, String fieldName, Class<?> fieldClass) {
		FieldTypeMapping fieldTypeMapping = getFieldTypeMapping(ownerClass);
		fieldTypeMapping.addSimpleMappingEntry(fieldName, fieldClass);
		
	}
	
	public void addSimpleMappingEntry(String ownerClassName, String fieldName, String fieldClassName) {
		Class<?> ownerClass = getClassObject(ownerClassName);
		Class<?> fieldClass = getClassObject(fieldClassName);
		addSimpleMappingEntry(ownerClass, fieldName, fieldClass);
	}
	
	public void addCollectionMappingEntry(Class<?> ownerClass, String fieldName, Class<?> fieldClass, Class<?> elementClass) {
		FieldTypeMapping fieldTypeMapping = getFieldTypeMapping(ownerClass);
		fieldTypeMapping.addCollectionMappingEntry(fieldName, fieldClass, elementClass);
	}
	
	public void addCollectionMappingEntry(String ownerClassName, String fieldName, String fieldClassName, String elementClassName) {
		Class<?> ownerClass = getClassObject(ownerClassName); 
		Class<?> fieldClass = getClassObject(fieldClassName); 
		Class<?> elementClass = getClassObject(elementClassName);
		addCollectionMappingEntry(ownerClass, fieldName, fieldClass, elementClass);
	}
	
	public void addMapMappingEntry(Class<?> ownerClass, String fieldName, Class<?> fieldClass, Class<?> keyClass, Class<?> valueClass) {
		FieldTypeMapping fieldTypeMapping = getFieldTypeMapping(ownerClass);
		fieldTypeMapping.addMapMappingEntry(fieldName, fieldClass, keyClass, valueClass);
	}
	
	public void addMapMappingEntry(String ownerClassName, String fieldName, String fieldClassName, String keyClassName, String valueClassName) {
		Class<?> ownerClass = getClassObject(ownerClassName); 
		Class<?> fieldClass = getClassObject(fieldClassName); 
		Class<?> keyClass = getClassObject(keyClassName);
		Class<?> valueClass = getClassObject(valueClassName);
		addMapMappingEntry(ownerClass, fieldName, fieldClass, keyClass, valueClass);
	}

	protected Class<?> getClassObject(String className) {
		try {
			 return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
	
	protected FieldTypeMapping getFieldTypeMapping(Class<?> ownerClass) {
		FieldTypeMapping fieldTypeMapping = classMapping.get(ownerClass);
		if(fieldTypeMapping == null) {
			fieldTypeMapping = new FieldTypeMapping(ownerClass);
			classMapping.put(ownerClass, fieldTypeMapping);
			
		}
		return fieldTypeMapping;
	}
}
