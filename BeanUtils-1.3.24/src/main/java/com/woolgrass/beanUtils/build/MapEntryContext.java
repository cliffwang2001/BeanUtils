package com.woolgrass.beanUtils.build;

import java.util.Map;

import com.woolgrass.beanUtils.ObjectCategory;

public class MapEntryContext implements ObjectContext {

	protected MapEntry value = new MapEntry();
	protected MapContext parentContext;
//	protected ObjectCategory category;
//	protected Class<?> type;
	protected int level;
	protected int index;	
	protected Class<?> keyType;
	protected Class<?> valueType;
	protected MapKeyContext keyContext;
	protected MapValueContext valueContext;
	

	public MapEntryContext(MapContext parentContext, int level, int index,
			Class<?> keyType, Class<?> valueType) {
		this.parentContext = parentContext;
		this.level = level;
		this.index = index;
		this.keyType = keyType;
		this.valueType = valueType;
	}

	public Object getValue() {
		return value;
	}

	public ObjectContext getParentContext() {
		return parentContext;
	}

	public ObjectCategory getCategory() {
		return ObjectCategory.MAPENTRY;
	}

	public int getLevel() {
		return level;
	}


	public Class<?> getType() {
		throw new IllegalStateException("MapEntry doesn't have a type");
	}

	public void setValue(Object value) {
		if(!(value instanceof Map.Entry))
			throw new IllegalArgumentException("value is not of type Map.Entry");	
		Map.Entry entryValue = (Map.Entry)value;
		this.value.setKey(entryValue.getKey());
		this.value.setValue(entryValue.getValue());
		parentContext.addNewEntry(this.value);
	}
	
	public void setMapEntry() {
		parentContext.addNewEntry(this.value);
	}
	
	public void setMapKey(Object value) {
		this.value.setKey(value);
	}

	public void setMapValue(Object value) {
		this.value.setValue(value);
	}
	
//	public MapKeyMarkerContext startKeyMarkerContext() {
//		int curLevel = this.getLevel();
//		ObjectCategory keyCategory = ObjectCategory.checkCategory(keyType);
//		MapKeyMarkerContext marker = new MapKeyMarkerContext( this, keyCategory, keyType, curLevel + 1, index);
//		return marker;
//	}
	
	public MapKeyContext startMapKey() {
		int curLevel = this.getLevel();
		ObjectCategory keyCategory = ObjectCategory.checkCategory(keyType);
		keyContext = new MapKeyContext(null, this, keyCategory, keyType, curLevel + 1, index);
		return keyContext;
	}
	
	public boolean isMapKeyStarted() {
		return keyContext != null;
	}
	
	public MapValueContext startMapValue() {
		int curLevel = this.getLevel();
		ObjectCategory valueCategory = ObjectCategory.checkCategory(valueType);
		MapValueContext valueContext = new MapValueContext(null, this, valueCategory, valueType, curLevel + 1, index);
		return valueContext;
	}
	
	public Class<?> getKeyType() {
		return keyType;
	}

	public Class<?> getValueType() {
		return valueType;
	}



	public static class MapEntry {
		protected Object key;
		protected Object value;
		
		
		public MapEntry() {
		}


		public MapEntry(Object key, Object value) {
			this.key = key;
			this.value = value;
		}


		public Object getKey() {
			return key;
		}


		public void setKey(Object key) {
			this.key = key;
		}


		public Object getValue() {
			return value;
		}


		public void setValue(Object value) {
			this.value = value;
		}				
	}
	
}
