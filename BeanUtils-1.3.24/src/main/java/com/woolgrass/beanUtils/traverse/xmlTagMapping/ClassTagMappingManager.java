package com.woolgrass.beanUtils.traverse.xmlTagMapping;

import java.util.HashMap;
import java.util.Map;

public class ClassTagMappingManager {
	protected Map<Class<?> , ClassTagMapping> classToTagMap = new HashMap<Class<?> , ClassTagMapping>();
	protected Map<String , ClassTagMapping> tagToClassMap = new HashMap<String , ClassTagMapping>();
	
	public void addClassTagMapping(ClassTagMapping clsTagMaping) {
		classToTagMap.put(clsTagMaping.getTargetClass(), clsTagMaping);
		tagToClassMap.put(clsTagMaping.getClassTagName(), clsTagMaping);
	}
	
	public void addClassTagMapping(Class<?> targetClass, String classTagName) {
		ClassTagMapping clsTagMaping = new ClassTagMapping(targetClass, classTagName);
		classToTagMap.put(clsTagMaping.getTargetClass(), clsTagMaping);
		tagToClassMap.put(clsTagMaping.getClassTagName(), clsTagMaping);
	}
	
	public void addFieldTagMapping(Class<?> targetClass, String fieldName, String tagName) {
		ClassTagMapping clsMapping = classToTagMap.get(targetClass);
		if(clsMapping == null) {
			clsMapping = new ClassTagMapping(targetClass);
			classToTagMap.put(clsMapping.getTargetClass(), clsMapping);
		}
		clsMapping.addMapping(fieldName, tagName);
	}
	
	public String getClassTagName(Class<?> targetClass) {
		ClassTagMapping clsTagMaping = classToTagMap.get(targetClass);
		if(clsTagMaping == null)
			return null;
		return clsTagMaping.getClassTagName();
	}
	
	public String getFieldTagName(Class<?> targetClass, String fieldName) {
		ClassTagMapping clsTagMaping = classToTagMap.get(targetClass);
		if(clsTagMaping == null)
			return null;
		return clsTagMaping.getFieldTagName(fieldName);
	}
	
	public String getFieldName(Class<?> targetClass, String tagName) {
		ClassTagMapping clsTagMaping = classToTagMap.get(targetClass);
		if(clsTagMaping == null)
			return null;
		return clsTagMaping.getFieldName(tagName);
	}
	
	public Class<?> getClass(String tagName) {
		ClassTagMapping clsTagMaping = tagToClassMap.get(tagName);
		if(clsTagMaping == null)
			return null;
		return clsTagMaping.getTargetClass();
	}
	
	
}
