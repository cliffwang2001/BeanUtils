package com.woolgrass.beanUtils.build;

import com.woolgrass.beanUtils.build.MapEntryContext.MapEntry;

public interface MapContext extends ObjectContext {
	public Class<?> getMapKeyType();
	public Class<?> getMapValueType();
	public MapEntryContext startNewEntry();
	public void addNewEntry(MapEntry entry);
	public void setMapValue();
}
