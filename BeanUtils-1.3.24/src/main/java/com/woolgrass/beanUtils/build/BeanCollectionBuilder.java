package com.woolgrass.beanUtils.build;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.woolgrass.beanUtils.CollectionFieldStruct;
import com.woolgrass.beanUtils.ConverterManager;
import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.SimpleTypeUtils;
import com.woolgrass.beanUtils.traverse.xmlTagMapping.ClassTagMappingManager;


public class BeanCollectionBuilder {
	protected BuilderManager builderManager;
	protected Class<?> beanClass;
	protected String beanRootElement;
	protected CollectionFieldStruct collectionStruct;
	protected List targetList = new ArrayList();
	protected ObjectCategory objectCategory;
	protected ClassTagMappingManager tagMappingManager;
	protected ConverterManager converterManager;
	
	
	public BeanCollectionBuilder(Class<?> beanClass, String beanRootElement) {
		this.beanClass = beanClass;
		this.beanRootElement = beanRootElement;
		this.collectionStruct = new CollectionFieldStruct();
		this.tagMappingManager = new ClassTagMappingManager();
		this.converterManager = new ConverterManager();
		objectCategory = ObjectCategory.checkCategory(this.beanClass);
		if(objectCategory != ObjectCategory.SIMPLE && objectCategory != ObjectCategory.COMPOSITE)
			throw new IllegalArgumentException("Only support simple or composite class");
	}
	
	public BeanCollectionBuilder(Class<?> beanClass, String beanRootElement,
			CollectionFieldStruct collectionStruct, ClassTagMappingManager tagMappingManager, ConverterManager converterManager) {
		this.beanClass = beanClass;
		this.beanRootElement = beanRootElement;
		this.collectionStruct = collectionStruct;
		this.tagMappingManager = tagMappingManager;
		this.converterManager = converterManager;
		objectCategory = ObjectCategory.checkCategory(this.beanClass);
		if(objectCategory != ObjectCategory.SIMPLE && objectCategory != ObjectCategory.COMPOSITE)
			throw new IllegalArgumentException("Only support simple or composite class");
	}

	public void startNextElement(String nextElement) {
		if(objectCategory == ObjectCategory.SIMPLE) {
			
		}else if(objectCategory == ObjectCategory.COMPOSITE) {
			startNextElementOnCompositeObject(nextElement);
		}
	}

	protected void startNextElementOnCompositeObject(String nextElement) {
		if(builderManager == null) {
			if(beanRootElement.equals(nextElement)) {
				builderManager = new BuilderManager(beanClass, beanRootElement, collectionStruct, tagMappingManager, converterManager);
				builderManager.startNextElement(nextElement);
			}
		}else {
			builderManager.startNextElement(nextElement);
		}
	}
	
	public void endCurrentElement(String element, String text) {
		if(objectCategory == ObjectCategory.SIMPLE) {
			if(beanRootElement.equals(element)) {
				Object value = SimpleTypeUtils.getValueFromString(beanClass, text);
				targetList.add(value);
			}
		}else if(objectCategory == ObjectCategory.COMPOSITE) {
			endCurElementOnCompositeObject(element, text);
		}
	}

	protected void endCurElementOnCompositeObject(String element, String text) {
		if(builderManager == null)
			return;
		builderManager.endCurrentElement(element, text);
		if(builderManager.isDone()) {
			targetList.add(builderManager.getTargetObject());
			builderManager = null;
		}
	}
	
	public Set getSet() {
		return new HashSet(targetList);
	}
	
	public List getList() {
		return targetList;
	}
	
	public Object[] getArray() {
		return targetList.toArray();
	}
}
