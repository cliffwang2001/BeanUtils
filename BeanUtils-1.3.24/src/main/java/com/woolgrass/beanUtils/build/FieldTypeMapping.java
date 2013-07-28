package com.woolgrass.beanUtils.build;

import java.util.HashMap;
import java.util.Map;

public class FieldTypeMapping {
	protected Class<?> ownerClass;
	protected Map<String, MappingEntry> entries = new HashMap<String, MappingEntry>();
	
	
	public FieldTypeMapping(Class<?> ownerClass) {
		this.ownerClass = ownerClass;
	}
	
	public FieldTypeMapping(String className) {
		this.ownerClass = getClassObject(className);
	}

	
	public Class<?> getOwnerClass() {
		return ownerClass;
	}

	protected Class<?> getClassObject(String className) {
		try {
			 return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	public void addSimpleMappingEntry(String fieldName, Class<?> fieldClass) {
		SimpleMappingEntry entry = new SimpleMappingEntry(fieldName, fieldClass);
		entries.put(fieldName, entry);
	}
	
	public void addSimpleMappingEntry(String fieldName, String fieldClassName) {
		Class<?> fieldClass =  getClassObject(fieldClassName);
		addSimpleMappingEntry(fieldName, fieldClass);
	}
	
	public void addCollectionMappingEntry(String fieldName, Class<?> fieldClass, Class<?> elementClass) {
		CollectionFieldMappingEntry entry = new CollectionFieldMappingEntry(fieldName, fieldClass, elementClass);
		entries.put(fieldName, entry);
	}
	
	public void addCollectionMappingEntry(String fieldName, String fieldClassName, String elementClassName) {
		Class<?> fieldClass = getClassObject(fieldClassName); 
		Class<?> elementClass = getClassObject(elementClassName);
		addCollectionMappingEntry(fieldName, fieldClass, elementClass);
	}
	
	public void addMapMappingEntry(String fieldName, Class<?> fieldClass, Class<?> keyClass, Class<?> valueClass) {
		MapFieldMappingEntry entry = new MapFieldMappingEntry(fieldName, fieldClass, keyClass, valueClass);
		entries.put(fieldName, entry);
	}
	
	public void addMapMappingEntry(String fieldName, String fieldClassName, String keyClassName, String valueClassName) {
		Class<?> fieldClass = getClassObject(fieldClassName); 
		Class<?> keyClass = getClassObject(keyClassName);
		Class<?> valueClass = getClassObject(valueClassName);
		addMapMappingEntry(fieldName, fieldClass, keyClass, valueClass);
	}
	
	static public interface MappingEntry {
		String getFieldName();
		Class<?> getFieldClass();
	}
	
	static public class SimpleMappingEntry implements MappingEntry {
		protected String fieldName;
		protected Class<?> fieldClass;
		
		public SimpleMappingEntry(String fieldName, Class<?> fieldClass) {
			this.fieldName = fieldName;
			this.fieldClass = fieldClass;
		}

		public String getFieldName() {
			return fieldName;
		}

		public Class<?> getFieldClass() {
			return fieldClass;
		}
	}
	
	static public class CollectionFieldMappingEntry extends SimpleMappingEntry {
		protected Class<?> elementClass;

		public CollectionFieldMappingEntry(String fieldName,
				Class<?> fieldClass, Class<?> elementClass) {
			super(fieldName, fieldClass);
			this.elementClass = elementClass;
		}

		public Class<?> getElementClass() {
			return elementClass;
		}
	}
	
	static public class MapFieldMappingEntry extends SimpleMappingEntry {
		protected Class<?> keyClass;
		protected Class<?> valueClass;
		
		public MapFieldMappingEntry(String fieldName, Class<?> fieldClass,
				Class<?> keyClass, Class<?> valueClass) {
			super(fieldName, fieldClass);
			this.keyClass = keyClass;
			this.valueClass = valueClass;
		}

		public Class<?> getKeyClass() {
			return keyClass;
		}

		public Class<?> getValueClass() {
			return valueClass;
		}
	}
}
