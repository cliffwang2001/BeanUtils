package com.woolgrass.beanUtils.traverse.xmlTagMapping;

import java.util.HashMap;
import java.util.Map;

public class FieldTagMapping {
	protected Map<String, String> fieldToTagMap = new HashMap<String, String>();
	protected Map<String, String> tagToFieldMap = new HashMap<String, String>();
	
	public void addMapping(String fieldName, String tagName) {
		fieldToTagMap.put(fieldName, tagName);
		tagToFieldMap.put(tagName, fieldName);
	}
	
	public String getFieldName(String tagName) {
		return tagToFieldMap.get(tagName);
	}
	
	public String getTagName(String fieldName) {
		return fieldToTagMap.get(fieldName);
	}
}
