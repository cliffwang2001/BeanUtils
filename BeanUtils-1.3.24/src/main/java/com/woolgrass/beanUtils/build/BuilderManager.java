package com.woolgrass.beanUtils.build;

import java.util.Stack;

import com.woolgrass.beanUtils.CollectionFieldStruct;
import com.woolgrass.beanUtils.ConverterManager;
import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.CollectionFieldStruct.ArrayStruct;
import com.woolgrass.beanUtils.CollectionFieldStruct.ListStruct;
import com.woolgrass.beanUtils.CollectionFieldStruct.MapStruct;
import com.woolgrass.beanUtils.CollectionFieldStruct.SetStruct;
import com.woolgrass.beanUtils.traverse.xmlTagMapping.ClassTagMappingManager;

public class BuilderManager {
	protected BeanBuilder beanBuilder; 
	protected Stack<FieldBuilder> builderStack = new Stack<FieldBuilder>();
	protected boolean rootObjectCreated;
	protected String rootElement;
	protected FieldBuilder currentBuilder;
	protected boolean done;
	protected CollectionFieldStruct collectionStruct;
	protected ClassTagMappingManager tagMappingManager;
	protected ConverterManager converterManager;
	protected Stack<Tag2Property> tagStack = new Stack<Tag2Property>();
	
	public BuilderManager(Class<?> compositeOrArrayClass, String rootElement, 
			ClassTagMappingManager tagMappingManager, ConverterManager converterManager) {
		this.tagMappingManager = tagMappingManager;
		this.converterManager = converterManager;
		beanBuilder = new BeanBuilder(compositeOrArrayClass, tagMappingManager, converterManager);
		
		this.rootElement = rootElement;
//		collectionStruct = new CollectionFieldStruct();
		collectionStruct = CollectionFieldStruct.FIELDNAME_AS_GROUPING_TAG;
	}
	
	public BuilderManager(Class<?> collectionClass, Class<?> elementType, String rootElement, 
			ClassTagMappingManager tagMappingManager, ConverterManager converterManager) {
		this.tagMappingManager = tagMappingManager;
		this.converterManager = converterManager;
		beanBuilder = new BeanBuilder(collectionClass, elementType, tagMappingManager, converterManager);
		this.rootElement = rootElement;
//		collectionStruct = new CollectionFieldStruct();
		collectionStruct = CollectionFieldStruct.FIELDNAME_AS_GROUPING_TAG;
	}
	
	public BuilderManager(Class<?> mapClass, Class<?> mapKeyType, Class<?> mapValueType, String rootElement,  
			ClassTagMappingManager tagMappingManager, ConverterManager converterManager) {
		this.tagMappingManager = tagMappingManager;
		this.converterManager = converterManager;
		beanBuilder = new BeanBuilder(mapClass, mapKeyType, mapValueType, tagMappingManager, converterManager);
		this.rootElement = rootElement;
//		collectionStruct = new CollectionFieldStruct();
		collectionStruct = CollectionFieldStruct.FIELDNAME_AS_GROUPING_TAG;
	}
	
	
	public BuilderManager(Class<?> compositeOrArrayClass, String rootElement, CollectionFieldStruct collectionStruct,  
			ClassTagMappingManager tagMappingManager, ConverterManager converterManager) {
		this.tagMappingManager = tagMappingManager;
		this.converterManager = converterManager;
		beanBuilder = new BeanBuilder(compositeOrArrayClass, tagMappingManager, converterManager);
		this.rootElement = rootElement;
		this.collectionStruct = collectionStruct;
	}
	
	public BuilderManager(Class<?> collectionClass, Class<?> elementType, String rootElement, 
			CollectionFieldStruct collectionStruct,  ClassTagMappingManager tagMappingManager, ConverterManager converterManager) {
		this.tagMappingManager = tagMappingManager;
		this.converterManager = converterManager;
		beanBuilder = new BeanBuilder(collectionClass, elementType, tagMappingManager, converterManager);
		this.rootElement = rootElement;
		this.collectionStruct = collectionStruct;
	}
	
	public BuilderManager(Class<?> mapClass, Class<?> mapKeyType, Class<?> mapValueType, String rootElement, 
			CollectionFieldStruct collectionStruct,  ClassTagMappingManager tagMappingManager, ConverterManager converterManager) {
		this.tagMappingManager = tagMappingManager;
		this.converterManager = converterManager;
		beanBuilder = new BeanBuilder(mapClass, mapKeyType, mapValueType, tagMappingManager, converterManager);
		this.rootElement = rootElement;
		this.collectionStruct = collectionStruct;
	}
	
	public boolean isDone() {
		return done;
	}
	
	private String getPropertyNameByTagName(String propertyName, ObjectContext curCtx) {
		if(!(curCtx instanceof FieldContext || curCtx instanceof RootObjectContext))
			return propertyName;
		
		if(tagMappingManager == null)
			return propertyName;
		
		Class<?> cls = curCtx.getType();
		String realPropertyName = tagMappingManager.getFieldName(cls, propertyName);
		if(realPropertyName == null)
			realPropertyName = propertyName;
		return realPropertyName;
	}
	
	public void startNextElement(String nextElement) {
		ObjectContext curCtx = beanBuilder.getCurrentContext();
		String realPropertyName = getPropertyNameByTagName(nextElement, curCtx);
		Tag2Property tagProperty = new Tag2Property(nextElement, realPropertyName);
		tagStack.add(tagProperty);
		
		if(rootElement.equals(realPropertyName) && !rootObjectCreated) {
//			beanBuilder.createRootObject();
			rootObjectCreated = true;
			ObjectCategory curCategory = curCtx.getCategory();
			switch(curCategory) {
			case COMPOSITE:
				currentBuilder = new CompositeFieldBuilder(this, beanBuilder, builderStack);
				break;
			case ARRAY:
				currentBuilder = new ArrayFieldBuilder(this, beanBuilder, builderStack, null);
				break;
			case LIST:
				currentBuilder = new ListFieldBuilder(this, beanBuilder, builderStack, null);
				break;
			case SET:
				currentBuilder = new SetFieldBuilder(this, beanBuilder, builderStack, null);
				break;
			case MAP:
				currentBuilder = new MapFieldBuilder(this, beanBuilder, builderStack, null);
				break;
			}
//			currentBuilder = new CompositeFieldBuilder(this, beanBuilder, builderStack);
		}else if(currentBuilder != null)
			currentBuilder = currentBuilder.startNextElement(realPropertyName);
	}
	public void endCurrentElement(String element, String text) {
		if(done)
			return;
		if(currentBuilder == null)
			return;
		Tag2Property tagProperty = tagStack.pop();
		String propertyName = tagProperty.getProperty(element);
		currentBuilder = currentBuilder.endCurrentElement(propertyName, text);
//		if(rootElement.equals(element)) {
		if(builderStack.empty()) {
			done = true;
		}
	}
	
	public Object getTargetObject() {
		return beanBuilder.getTargetObject();
	}

	public CollectionFieldStruct getCollectionStruct() {
		return collectionStruct;
	}	
	
	public boolean isNoGroupingTag(ObjectCategory collCategory) {
		switch(collCategory) {
		case ARRAY:
			if(collectionStruct.getArrayStruct() == ArrayStruct.NO_GROUPING_TAG)
				return true;
			else
				return false;
		case LIST:
			if(collectionStruct.getListStruct() == ListStruct.NO_GROUPING_TAG)
				return true;
			else
				return false;
		case SET:
			if(collectionStruct.getSetStruct() == SetStruct.NO_GROUPING_TAG)
				return true;
			else
				return false;
		case MAP:
			if(collectionStruct.getMapStruct() == MapStruct.NO_MAP_GROUPING)
				return true;
			else
				return false;
		default: 
			throw new IllegalStateException("collCategory is of a collection category");
		}
	}
	
	protected class Tag2Property {
		protected String tag;
		protected String property;
		
		public Tag2Property(String tag, String property) {
			this.tag = tag;
			this.property = property;
		}
		
		public String getProperty(String tag) {
			if(tag.equals(this.tag))
				return property;
			else
				throw new IllegalStateException("expecting " + this.tag + ", but received " + tag);
		}
	}
}
