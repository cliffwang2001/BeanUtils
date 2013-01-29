package com.woolgrass.beanUtils.traverse.xmlTagMapping;

public class ClassTagMapping {

	protected Class<?> targetClass;
	protected String classTagName;
	protected FieldTagMapping fieldTagMapping = new FieldTagMapping();
	
	public ClassTagMapping(Class<?> targetClass, String classTagName) {
		this.targetClass = targetClass;
		this.classTagName = classTagName;
	}
	
	public ClassTagMapping(Class<?> targetClass) {
		this.targetClass = targetClass;
	}



	public FieldTagMapping getFieldTagMapping() {
		return fieldTagMapping;
	}

	public void setFieldTagMapping(FieldTagMapping fieldTagMapping) {
		this.fieldTagMapping = fieldTagMapping;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public String getClassTagName() {
		return classTagName;
	}
	
	public void addMapping(String fieldName, String tagName) {
		fieldTagMapping.addMapping(fieldName, tagName);
	}
	
	public String getFieldTagName(String fieldName) {
		return fieldTagMapping.getTagName(fieldName);
	}
	
	public String getFieldName(String tagName) {
		return fieldTagMapping.getFieldName(tagName);
	}
	
}
