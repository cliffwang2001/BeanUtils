package com.woolgrass.beanUtils.build;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.woolgrass.beanUtils.CollectionTypeUtils;
import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.build.MapEntryContext.MapEntry;

public class MapFieldContext extends FieldContext implements MapContext{
	protected Map<Object, Object> tempMap = new HashMap<Object, Object>();

	public MapFieldContext(Object value, ObjectContext parentContext,
			ObjectCategory category, Object owner, String fieldName,
			Field definition, int level) {
		super(value, parentContext, category, owner, fieldName, definition, level);
		if(value != null && value instanceof Map<?,?>) {
			Map<?,?> mapObj = (Map<?,?>)value;
			tempMap.putAll(mapObj);
			
		}
	}

	public Class<?> getMapKeyType() {
		return CollectionTypeUtils.getMapFieldKeyType(definition);
	}
	
	public Class<?> getMapValueType() {
		return CollectionTypeUtils.getMapFieldValueType(definition);
	}
	
	public MapEntryContext startNewEntry() {
		Class<?> keyType  = getMapKeyType();
		if(keyType == null)
			throw new IllegalStateException("Can't identifiy the type of the map key");
		
		Class<?> valueType  = getMapValueType();
		if(valueType == null)
			throw new IllegalStateException("Can't identifiy the type of the map value");
		int curLevel = this.getLevel();
//		ObjectCategory category = ObjectCategory.checkCategory(elemCls);
//		MapEntryContext entryCtx = new MapEntryContext(null, this, category,elemCls, curLevel + 1, tempList.size());
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
