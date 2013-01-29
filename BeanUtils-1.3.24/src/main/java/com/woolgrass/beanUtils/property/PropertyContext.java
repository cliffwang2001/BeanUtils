package com.woolgrass.beanUtils.property;

public class PropertyContext {
		
	protected String propertyName;
	protected PropertyType propertyType;
	
	public PropertyContext(String propertyName, PropertyType propertyType) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;
	}
	
	public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	
}
