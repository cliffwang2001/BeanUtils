package com.woolgrass.beanUtils.build;


import java.util.Stack;

import com.woolgrass.beanUtils.CollectionFieldStruct;
import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.CollectionFieldStruct.MapStruct;

public class MapFieldBuilder implements FieldBuilder{
		
	protected BeanBuilder beanBuilder; 
	protected Stack<ProcessState> stateStack = new Stack<ProcessState>();
//	protected Stack<String> elementNameStack = new Stack<String>();
	protected Stack<FieldBuilder> builderStack;
	protected BuilderManager buildManager;
	protected MapStruct fieldStruct;
	protected MapFieldContext mapCtx;
	
	public MapFieldBuilder(BuilderManager buildManager, BeanBuilder beanBuilder,  
			Stack<FieldBuilder> builderStack, MapFieldContext mapCtx) {
		this.beanBuilder = beanBuilder;
		this.builderStack = builderStack;
		
		stateStack.push(new MapState());
//		elementStruct.mapElement = mapElementText;
		this.builderStack.push(this);
		this.buildManager = buildManager;
		this.fieldStruct = buildManager.getCollectionStruct().getMapStruct();
		
		this.mapCtx = mapCtx;
		if(this.mapCtx != null) {
			String fieldName = this.mapCtx.getFieldName();
			ProcessState curState = stateStack.peek();
			ProcessState nextState = curState.processElement(fieldName);
		}
	}
	
	
	public FieldBuilder startNextElement(String nextElement) {
		ProcessState curState = stateStack.peek();
		ProcessState nextState = curState.processElement(nextElement);
		if(nextState instanceof NestedCompositeObjectState) {
			ObjectContext curObjCtx = beanBuilder.getCurrentObjectContext();
			ObjectCategory curCategory = curObjCtx.getCategory();
			if(curCategory == ObjectCategory.COMPOSITE) {
				CompositeFieldBuilder compositeBuilder = new CompositeFieldBuilder(buildManager, beanBuilder, builderStack);
				FieldBuilder nextBuilder = compositeBuilder.startNextElement(nextElement);
				return nextBuilder;
			}else 
				throw new IllegalStateException("Map element doesn't support this data type");
			
		}
		return this;
	}
	
	
	public FieldBuilder endCurrentElement(String element, String text) {
		ProcessState curState = stateStack.peek();
		curState.endElement(element, text);
//		if(stateStack.empty())
//			builderStack.pop();
		if(!builderStack.empty())
			return builderStack.peek();
		else
			return null;
	}
	
	
	class MapState extends ProcessState{
		
		public MapState() {
		}
		
		public ProcessState processElement(String nextElement) {
			ProcessState nextState;
			if(elementsStack.size() < fieldStruct.getMapStruct().size()) {
				String expectedElement = fieldStruct.getMapStruct().get(elementsStack.size());
				if(nextElement.equals(expectedElement) || expectedElement.equals(CollectionFieldStruct.FIELD_NAME)) {
					elementsStack.push(nextElement);
					nextState = this;
				}else
					throw new IllegalStateException("expecting " + expectedElement + ", but receiving " + nextElement);
			}else {
				nextState = new MapEntryState();
				stateStack.push(nextState);
				
				MapEntryContext entryCtx = beanBuilder.startNewEntryOnCurrentMapObject();	
				nextState = nextState.processElement(nextElement);
			}
			return nextState;
		}

		public void endElement(String element, String text) {
//			if(!elementsStack.empty() && elementsStack.size() == fieldStruct.mapStruct.size()) {
//				beanBuilder.endCurrentContext();
//			}
			String fieldName = mapCtx.getFieldName();
			if(elementsStack.size() == 1 && elementsStack.peek().equals(fieldName)) {
				if(!element.equals(fieldName))
					throw new IllegalStateException("ending element is " + element + " but should be " + fieldName);
				elementsStack.pop();
			}
			
			if(elementsStack.empty()) {
				stateStack.pop();
				builderStack.pop();
				if(!builderStack.empty()) {
					FieldBuilder prevBuilder = builderStack.peek();
					prevBuilder.endCurrentElement(element, text);
				}
				else
					beanBuilder.endCurrentContext();
			}else {
				elementsStack.pop();
				if(elementsStack.empty()) {
					stateStack.pop();
					builderStack.pop();
				}
			}
		}
	}
	
	class MapEntryState extends ProcessState{
		protected boolean keyElementProcessed;
		
		public MapEntryState() {
		}

		public ProcessState processElement(String nextElement) {
			ProcessState nextState;
			if(elementsStack.size() < fieldStruct.getEntryStruct().size()) {
				String expectedElement = fieldStruct.getEntryStruct().get(elementsStack.size());
				if(nextElement.equals(expectedElement) ) {
					elementsStack.push(nextElement);
					nextState = this;
				}else if(expectedElement.equals(CollectionFieldStruct.FIELD_NAME)) {
					elementsStack.push(nextElement);
					nextState = this;
				}
				else
					throw new IllegalStateException("expecting " + expectedElement + ", but receiving " + nextElement);
			}else {
				if(keyElementProcessed) {
					nextState = new MapValueState();
					stateStack.push(nextState);
					
					MapValueContext valueCtx = beanBuilder.startValueOnCurrentMapEntryObject();
					nextState = nextState.processElement(nextElement);
				}else {
					nextState = new MapKeyState();
					stateStack.push(nextState);
					
					MapKeyContext keyCtx = beanBuilder.startKeyOnCurrentMapEntryObject();
					nextState = nextState.processElement(nextElement);
					keyElementProcessed = true;
				}
				
//				nextState = nextState.processElement(nextElement);
			}
			return nextState;
		}

		public void endElement(String element, String text) {
			if(elementsStack.size() == fieldStruct.getEntryStruct().size()) {
				beanBuilder.endCurrentContext();
			}
			
			String fieldName = mapCtx.getFieldName();
			if(elementsStack.size() == 1 && elementsStack.peek().equals(fieldName)) {
				if(!element.equals(fieldName))
					throw new IllegalStateException("ending element is " + element + " but should be " + fieldName);
				elementsStack.pop();
			}
			
			if(elementsStack.empty()) {
				stateStack.pop();
				ProcessState prevState = stateStack.peek();
				if(prevState.getElementsStack().empty())
					prevState.endElement(element, text);
			}else {
				elementsStack.pop();
				if(elementsStack.empty())
					stateStack.pop();
			}		
			
		}
		
	}
	
	enum ContentsElementType {NO_CONTENTS_ELEMENT, CLASS_NAME, FIXED_TEXT};
	
	class MapKeyState extends ProcessState{
		protected String keyClassName;
		
		public MapKeyState() {
		}

		
		public ProcessState processElement(String nextElement) {
			ProcessState nextState;
			if(elementsStack.size() < fieldStruct.getKeyStruct().size()) {
				String expectedElement = fieldStruct.getKeyStruct().get(elementsStack.size());
				if(expectedElement.equals(CollectionFieldStruct.CLASS_NAME)) {
					keyClassName = nextElement;
					elementsStack.push(nextElement);
					nextState = this;
				}else if(expectedElement.equals(CollectionFieldStruct.ANY_NAME) || nextElement.equals(expectedElement)) {
					elementsStack.push(nextElement);
					nextState = this;
				}else
					throw new IllegalStateException("expecting " + expectedElement + ", but receiving " + nextElement);
				
				if(elementsStack.size() == fieldStruct.getKeyStruct().size()) {
					ObjectContext keyCtx = beanBuilder.getCurrentObjectContext();
					Class<?> keyCls = keyCtx.getType();
					if(keyClassName != null && !TypeUtils.checkTypeCompatibility(keyCls, keyClassName)) {
						throw new IllegalStateException("Element name: " + keyClassName + " is different from class name " 
								+ keyCls.getName() + " of map key");
					}
				}
			}else {
				ObjectContext keyCtx = beanBuilder.getCurrentObjectContext();
				ObjectCategory keyCategory = keyCtx.getCategory();
				if(keyCategory == ObjectCategory.COMPOSITE) {
					beanBuilder.createCurrentMapKeyObject();
					nextState = new NestedCompositeObjectState();
				}else if(keyCategory == ObjectCategory.SIMPLE)
					throw new IllegalStateException("Simple object can't have nested element");
				else
					throw new IllegalStateException("Map element don't support this data type");
				
				
			}
			return nextState;
		}
		
		public void endElement(String element, String text) {
			if(elementsStack.size() == fieldStruct.getKeyStruct().size()) {
				ObjectContext curCtx = beanBuilder.getCurrentContext();
				ObjectCategory keyCategory =  curCtx.getCategory();
				if(keyCategory == ObjectCategory.SIMPLE ) {
					beanBuilder.setValueOnCurrentSimpleObjectFromString(text, keyClassName);
				}
				else if(text != null && text.trim().length() > 0)
						throw new IllegalStateException(elementsStack.peek() + " can't have character data.");
				beanBuilder.endCurrentContext();
			}
			if(elementsStack.empty()) {
				stateStack.pop();
				ProcessState prevState = stateStack.peek();
				prevState.endElement(element, text);
			}else {
				elementsStack.pop();
				if(elementsStack.empty())
					stateStack.pop();
			}		
		}

		
	}
	
	class MapValueState extends ProcessState{
		protected String valueClassName;
		
		public MapValueState() {
		}

		public ProcessState processElement(String nextElement) {
			ProcessState nextState;
			if(elementsStack.size() < fieldStruct.getValueStruct().size()) {
				String expectedElement = fieldStruct.getValueStruct().get(elementsStack.size());
				if(expectedElement.equals(CollectionFieldStruct.CLASS_NAME)) {
					valueClassName = nextElement;
					elementsStack.push(nextElement);
					nextState = this;
				}else if(expectedElement.equals(CollectionFieldStruct.ANY_NAME) || nextElement.equals(expectedElement)) {
					elementsStack.push(nextElement);
					nextState = this;
				}else
					throw new IllegalStateException("expecting " + expectedElement + ", but receiving " + nextElement);
				
				if(elementsStack.size() == fieldStruct.getValueStruct().size()) {
					ObjectContext valueCtx = beanBuilder.getCurrentObjectContext();
					Class<?> valueCls = valueCtx.getType();
					if(valueClassName != null && !TypeUtils.checkTypeCompatibility(valueCls, valueClassName)) {
						throw new IllegalStateException("Element name: " + valueClassName + " is different from class name " 
								+ valueCls.getName() + " of map value");
					}
				}
			}else {
				ObjectContext valueCtx = beanBuilder.getCurrentObjectContext();
				ObjectCategory valueCategory = valueCtx.getCategory();
				if(valueCategory == ObjectCategory.COMPOSITE) {
					beanBuilder.createCurrentMapValueObject();
					nextState = new NestedCompositeObjectState();
				}else if(valueCategory == ObjectCategory.SIMPLE)
					throw new IllegalStateException("Simple object can't have nested element");
				else
					throw new IllegalStateException("Map element don't support this data type");
				
				
			}
			return nextState;
		}

		public void endElement(String element, String text) {
			if(elementsStack.size() == fieldStruct.getValueStruct().size()) {
				ObjectContext curCtx = beanBuilder.getCurrentContext();
				ObjectCategory valueCategory =  curCtx.getCategory();
				if(valueCategory == ObjectCategory.SIMPLE ) {
					beanBuilder.setValueOnCurrentSimpleObjectFromString(text, valueClassName);
				}
				else if(text != null && text.trim().length() > 0)
					throw new IllegalStateException(elementsStack.peek() + " can't have character data.");
				beanBuilder.endCurrentContext();
			}
			if(elementsStack.empty()) {
				stateStack.pop();
				ProcessState prevState = stateStack.peek();
				prevState.endElement(element, text);
			}else {
				elementsStack.pop();
				if(elementsStack.empty()) {
					stateStack.pop();
					ProcessState prevState = stateStack.peek();
					if(prevState.getElementsStack().empty())
						prevState.endElement(element, text);
				}
			}		
		}
			
	}
	
	
}
