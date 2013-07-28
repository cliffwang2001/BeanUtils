package com.woolgrass.beanUtils.build;

import java.util.HashMap;
import java.util.Map;

import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.build.MapEntryContext.MapEntry;

public class MapRootContext implements MapContext {
	protected Map<Object, Object> tempMap = new HashMap<Object, Object>();
	
	protected Object value;
	protected Class<?> type;
	protected Class<?> mapKeyType;
	protected Class<?> mapValueType;
	
	
	public MapRootContext(Class<?> type, Class<?> mapKeyType,
			Class<?> mapValueType) {
		this.type = type;
		this.mapKeyType = mapKeyType;
		this.mapValueType = mapValueType;
	}

	public Object getValue() {
		return value;
	}

	public ObjectContext getParentContext() {
		return null;
	}

	public ObjectCategory getCategory() {
		return ObjectCategory.MAP;
	}

	public int getLevel() {
		return 1;
	}

	public Class<?> getType() {
		return type;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Class<?> getMapKeyType() {
		return mapKeyType;
	}

	public Class<?> getMapValueType() {
		return mapValueType;
	}

	public MapEntryContext startNewEntry() {
		Class<?> keyType  = getMapKeyType();
		if(keyType == null)
			throw new IllegalStateException("Can't identifiy the type of the map key");
		
		Class<?> valueType  = getMapValueType();
		if(valueType == null)
			throw new IllegalStateException("Can't identifiy the type of the map value");
		int curLevel = this.getLevel();
		MapEntryContext entryCtx = new MapEntryContext(this, curLevel + 1, tempMap.size(), keyType, valueType);
		return entryCtx;	
	}

	public void addNewEntry(MapEntry entry) {
		tempMap.put(entry.getKey(), entry.getValue());
	}

	public void setMapValue() {
		setValue(tempMap);
	}

}
