package com.woolgrass.beanUtils.build;

import java.util.Stack;

import com.woolgrass.beanUtils.CollectionFieldStruct;
import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.CollectionFieldStruct.SetStruct;



public class SetFieldBuilder implements FieldBuilder {
	protected BeanBuilder beanBuilder; 
	protected Stack<ProcessState> stateStack = new Stack<ProcessState>();
	protected Stack<FieldBuilder> builderStack;
	protected SetStruct fieldStruct;
	protected BuilderManager buildManager;
	protected SetFieldContext setCtx;
	
	public SetFieldBuilder(BuilderManager buildManager, BeanBuilder beanBuilder,  
			Stack<FieldBuilder> builderStack, SetFieldContext setCtx) {
		this.beanBuilder = beanBuilder;
		this.builderStack = builderStack;
		
		stateStack.push(new SetState());
		this.builderStack.push(this);
		this.buildManager = buildManager;
		fieldStruct = buildManager.getCollectionStruct().getSetStruct();
		
		this.setCtx = setCtx;
		if(this.setCtx != null) {
			String fieldName = this.setCtx.getFieldName();
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
				throw new IllegalStateException("Set element doesn't support this data type");
			
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
	
	class SetState extends ProcessState {
		protected Stack<String> elementsStack = new Stack<String>();
		
		public ProcessState processElement(String nextElement) {
			ProcessState nextState;
			if(elementsStack.size() < fieldStruct.getSetStruct().size()) {
				String expectedElement = fieldStruct.getSetStruct().get(elementsStack.size());
				if(nextElement.equals(expectedElement) || expectedElement.equals(CollectionFieldStruct.FIELD_NAME)) {
					elementsStack.push(nextElement);
					nextState = this;
				}else
					throw new IllegalStateException("expecting " + expectedElement + ", but receiving " + nextElement);
			}else {
				nextState = new SetElementState();
				stateStack.push(nextState);
				
				SetElementContext elemCtx = beanBuilder.startNewElementOnCurrentSetObject();	
				nextState = nextState.processElement(nextElement);
			}
			return nextState;
		}

		public void endElement(String element, String text) {
//			if(!elementsStack.empty() && elementsStack.size() == fieldStruct.setStruct.size()) {
//				beanBuilder.endCurrentContext();
//			}
			String fieldName = setCtx.getFieldName();
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

	class SetElementState extends ProcessState {
		protected Stack<String> elementsStack = new Stack<String>();
		protected String elemClassName;
		
		public ProcessState processElement(String nextElement) {
			ProcessState nextState;
			if(elementsStack.size() < fieldStruct.getElementStruct().size()) {
				String expectedElement = fieldStruct.getElementStruct().get(elementsStack.size());
				if(expectedElement.equals(CollectionFieldStruct.CLASS_NAME)) {
					elemClassName = nextElement;
					elementsStack.push(nextElement);
					nextState = this;
				}else if(expectedElement.equals(CollectionFieldStruct.ANY_NAME) || nextElement.equals(expectedElement)) {
					elementsStack.push(nextElement);
					nextState = this;
				}else if(expectedElement.equals(CollectionFieldStruct.FIELD_NAME)) {
					elementsStack.push(nextElement);
					nextState = this;
				}else
					throw new IllegalStateException("expecting " + expectedElement + ", but receiving " + nextElement);
				
				if(elementsStack.size() == fieldStruct.getElementStruct().size()) {
					ObjectContext curCtx = beanBuilder.getCurrentObjectContext();
					Class<?> curCls = curCtx.getType();
					if(elemClassName != null && !TypeUtils.checkTypeCompatibility(curCls, elemClassName)) {
						throw new IllegalStateException("Element name: " + elemClassName + " is different from class name " 
								+ curCls.getName());
					}
				}
			}else {
				ObjectContext curCtx = beanBuilder.getCurrentObjectContext();
				ObjectCategory category = curCtx.getCategory();
				if(category == ObjectCategory.COMPOSITE) {
					beanBuilder.createCurrentSetElementObject();
					nextState = new NestedCompositeObjectState();
				}else if(category == ObjectCategory.SIMPLE)
					throw new IllegalStateException("Simple object can't have nested element");
				else
					throw new IllegalStateException("Don't support this data type");
				
				
			}
			return nextState;
		}

		public void endElement(String element, String text) {
			if(elementsStack.size() == fieldStruct.getElementStruct().size()) {
				ObjectContext curCtx = beanBuilder.getCurrentContext();
				ObjectCategory valueCategory =  curCtx.getCategory();
				if(valueCategory == ObjectCategory.SIMPLE ) {
					beanBuilder.setValueOnCurrentSimpleObjectFromString(text, elemClassName);
				}
				else if(text != null && text.trim().length() > 0)
					throw new IllegalStateException(elementsStack.peek() + " can't have character data.");
				beanBuilder.endCurrentContext();
			}
			
			String fieldName = setCtx.getFieldName();
			if(elementsStack.size() == 1 && elementsStack.peek().equals(fieldName)) {
				if(!element.equals(fieldName))
					throw new IllegalStateException("ending element is " + element + " but should be " + fieldName);
				elementsStack.pop();
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
	

}
