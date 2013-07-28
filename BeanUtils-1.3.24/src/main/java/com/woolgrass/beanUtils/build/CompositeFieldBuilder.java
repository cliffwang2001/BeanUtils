package com.woolgrass.beanUtils.build;

import java.util.Stack;

import com.woolgrass.beanUtils.ObjectCategory;


public class CompositeFieldBuilder implements FieldBuilder {
	protected BeanBuilder beanBuilder; 
	protected Stack<FieldBuilder> builderStack;
	protected String currentElement;
	protected BuilderManager buildManager;
	
	public CompositeFieldBuilder(BuilderManager buildManager, BeanBuilder beanBuilder,  Stack<FieldBuilder> builderStack) {
		this.beanBuilder = beanBuilder;
		this.builderStack = builderStack;
		this.builderStack.push(this);
		this.buildManager = buildManager;
	}
	
	public FieldBuilder startNextElement(String nextElement) {
		if(currentElement == null) {
			if(!beanBuilder.hasPropertyOnCurrentCompositeObject(nextElement)) {
				ObjectContext curCtx = beanBuilder.getCurrentContext();
				Class<?> curCls = curCtx.getType();
				throw new IllegalStateException(nextElement + " doesn't exist in class " + curCls.getName());
			}
			currentElement = nextElement;
			ObjectContext nextCtx = beanBuilder.startPropertyOnCurrentCompositeObject(nextElement);
			return this;
		}else {
			return processNestedElement(nextElement);
		}

	}

	private FieldBuilder processNestedElement(String nextElement) {
		ObjectContext curCtx = beanBuilder.getCurrentContext();
		ObjectCategory curCategory = curCtx.getCategory();
//			if(curCategory == ObjectCategory.COMPOSITE) {
//				beanBuilder.createObjectOnCurrentProperty();
//				CompositeFieldBuilder compositeBuilder = new CompositeFieldBuilder(beanBuilder, builderStack);
//				FieldBuilder nextBuilder = compositeBuilder.startNextElement(nextElement);
//				return nextBuilder;
//			}else 
//				throw new IllegalStateException(currentElement + " is not a composite object");

		FieldBuilder nextBuilder = null;
		switch(curCategory) {
		case SIMPLE:
			throw new IllegalStateException(currentElement + " is a simple object");
		case COMPOSITE:
			beanBuilder.createObjectOnCurrentProperty();
			CompositeFieldBuilder compositeBuilder = new CompositeFieldBuilder(buildManager, beanBuilder, builderStack);
			nextBuilder = compositeBuilder.startNextElement(nextElement);
			break;
		case ARRAY:
			ArrayFieldBuilder arrayBuilder = new ArrayFieldBuilder(buildManager, beanBuilder, builderStack, (ArrayFieldContext)curCtx);
			nextBuilder = arrayBuilder.startNextElement(nextElement);
			break;
		case LIST:
			ListFieldBuilder listBuilder = new ListFieldBuilder(buildManager, beanBuilder, builderStack, (ListFieldContext)curCtx);
			nextBuilder = listBuilder.startNextElement(nextElement);
			break;
		case SET:
			SetFieldBuilder setBuilder = new SetFieldBuilder(buildManager, beanBuilder, builderStack, (SetFieldContext)curCtx);
			nextBuilder = setBuilder.startNextElement(nextElement);
			break;
		case MAP:
			MapFieldBuilder mapBuilder = new MapFieldBuilder(buildManager, beanBuilder, builderStack, (MapFieldContext)curCtx);
			nextBuilder = mapBuilder.startNextElement(nextElement);
			break;
		case MAPENTRY:
			throw new IllegalStateException("Category of the current field can't be of type of Map.Entry");
		}
		return nextBuilder;
	}

	public FieldBuilder endCurrentElement(String element, String text) {
		if(this.currentElement == null) {
			builderStack.pop();
			if(builderStack.empty())
				return null;
			else {
				FieldBuilder prevBuilder = builderStack.peek();
				return prevBuilder.endCurrentElement(element, text);
			}
		}else if(this.currentElement == element) {
			ObjectContext curCtx = beanBuilder.getCurrentContext();
			ObjectCategory curCategory =  curCtx.getCategory();
			if(curCategory == ObjectCategory.SIMPLE ) {
//				beanBuilder.setValueOnCurrentProperty(text);
				beanBuilder.setValueOnCurrentSimpleObjectFromString(text, null);
			}
			else {
				ObjectCategory elemCategory = ObjectCategory.UNKNOWN;
				if(curCtx instanceof CollectionContext) {
					CollectionContext collCtx = (CollectionContext)curCtx;
					Class<?> elemCls = collCtx.getElementType();
					elemCategory = ObjectCategory.checkCategory(elemCls);
				}
				
				if(elemCategory == ObjectCategory.SIMPLE && ObjectCategory.isCollectionObject(curCategory) && buildManager.isNoGroupingTag(curCategory)) {
					if(curCategory == ObjectCategory.ARRAY) {
						ArrayElementContext elemCtx = beanBuilder.startNewElementOnCurrentArrayObject();
						beanBuilder.setValueOnCurrentSimpleObjectFromString(text, null);
						beanBuilder.endCurrentContext();
					}else if(curCategory == ObjectCategory.LIST) {
						ListElementContext elemCtx = beanBuilder.startNewElementOnCurrentListObject();
						beanBuilder.setValueOnCurrentSimpleObjectFromString(text, null);
						beanBuilder.endCurrentContext();
					}else if(curCategory == ObjectCategory.SET) {
						SetElementContext elemCtx = beanBuilder.startNewElementOnCurrentSetObject();
						beanBuilder.setValueOnCurrentSimpleObjectFromString(text, null);
						beanBuilder.endCurrentContext();
					}
				}else if(text != null && text.trim().length() > 0) {
					throw new IllegalStateException(element + " can't have character data.");
				}
			}
			
			beanBuilder.endCurrentContext();
			this.currentElement = null; 
			return this;
		}else 
			throw new IllegalStateException(element + " doesn't match " + this.currentElement);

	}

}
