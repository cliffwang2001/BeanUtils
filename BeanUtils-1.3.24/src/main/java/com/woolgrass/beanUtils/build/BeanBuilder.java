package com.woolgrass.beanUtils.build;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.EmptyStackException;
import java.util.Map;
import java.util.Stack;

import com.woolgrass.beanUtils.ConverterManager;
import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.ReflectionUtils;
import com.woolgrass.beanUtils.SimpleTypeUtils;
import com.woolgrass.beanUtils.TypeConverter;
import com.woolgrass.beanUtils.build.ObjectContext;
import com.woolgrass.beanUtils.traverse.xmlTagMapping.ClassTagMapping;
import com.woolgrass.beanUtils.traverse.xmlTagMapping.ClassTagMappingManager;

public class BeanBuilder {
	protected Class<?> targetClass;
	protected ObjectContextStack objectInfoStack = new ObjectContextStack();
	protected ObjectContext rootContext;
	protected ClassTagMappingManager tagMappingManager;
	protected ConverterManager converterManager;
	
	public BeanBuilder(Class<?> compositeOrArrayClass, 
			ClassTagMappingManager tagMappingManager, ConverterManager converterManager) {
		this.targetClass = compositeOrArrayClass;
		this.tagMappingManager = tagMappingManager;
		this.converterManager = converterManager;
		ObjectCategory category = ObjectCategory.checkCategory(targetClass);
		if(category == ObjectCategory.COMPOSITE)
			rootContext = new RootObjectContext(targetClass);
		else if(category == ObjectCategory.ARRAY)
			rootContext = new ArrayRootContext(targetClass);
		else
			throw new IllegalArgumentException("compositeClass is not of composite or array type");
		objectInfoStack.push(rootContext);
	}
	
	public BeanBuilder(Class<?> collectionClass, Class<?> elementType, 
			ClassTagMappingManager tagMappingManager, ConverterManager converterManager) {
		this.targetClass = collectionClass;
		this.tagMappingManager = tagMappingManager;
		this.converterManager = converterManager;
		ObjectCategory category = ObjectCategory.checkCategory(targetClass);
		if(category == ObjectCategory.LIST) 
			rootContext = new ListRootContext(targetClass, elementType);
		else if(category == ObjectCategory.SET) 
			rootContext = new SetRootContext(targetClass, elementType);
		else
			throw new IllegalArgumentException("collectionClass is not of list or set type");
		objectInfoStack.push(rootContext);
	}
	
	public BeanBuilder(Class<?> mapClass, Class<?> mapKeyType, Class<?> mapValueType, 
			ClassTagMappingManager tagMappingManager, ConverterManager converterManager) {
		this.targetClass = mapClass;
		this.tagMappingManager = tagMappingManager;
		this.converterManager = converterManager;
		ObjectCategory category = ObjectCategory.checkCategory(targetClass);
		if(category == ObjectCategory.MAP) 
			rootContext = new MapRootContext(targetClass, mapKeyType, mapValueType);
		else
			throw new IllegalArgumentException("mapClass is not of map type");
		objectInfoStack.push(rootContext);
	}
/*	
	public ObjectContext createRootObject() {
		if(!objectInfoStack.empty())
			throw new IllegalStateException("RootContext has already been created");
		
		ObjectCategory category = ObjectCategory.checkCategory(targetClass);
//		if(category != ObjectCategory.COMPOSITE)
//			throw new IllegalStateException("target object has to be of composite type");
		
		switch(category) {
		case SIMPLE:
			throw new IllegalStateException("target object can't be of simple type");
		case COMPOSITE:
			rootContext = new RootObjectContext(targetClass);
			break;
		case ARRAY:
			rootContext = new ArrayRootContext(targetClass);
			break;
		case LIST:
			rootContext = new ListRootContext(targetClass);
			break;
		case SET:
			rootContext = new SetRootContext(targetClass);
			break;
		case MAP:
			rootContext = new MapRootContext(targetClass);
			break;
		case MAPENTRY:
			throw new IllegalStateException("Category of the current field can't be of type of Map.Entry");
		}
		objectInfoStack.push(rootContext);
		return rootContext;
	}
*/	
	public boolean hasPropertyOnCurrentCompositeObject(String propertyName) {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(curCtx == null)
			throw new IllegalStateException("No non dummy context exists");
		
		ObjectCategory curCategory = curCtx.getCategory();
		if(curCategory != ObjectCategory.COMPOSITE)
			throw new IllegalStateException("Current object is not of a composite type");
		Object curObject = curCtx.getValue();
		
		String realPropertyName = getPropertyNameByTagName(propertyName, curCtx.getType());
		
		return ReflectionUtils.hasField(curObject, realPropertyName);
	}

	private String getPropertyNameByTagName(String propertyName, Class<?> cls) {
		if(tagMappingManager == null)
			return propertyName;
		
		String realPropertyName = tagMappingManager.getFieldName(cls, propertyName);
		if(realPropertyName == null)
			realPropertyName = propertyName;
		return realPropertyName;
	}
	
	public ObjectContext startPropertyOnCurrentCompositeObject(String propertyName) {
		if(objectInfoStack.empty())
			throw new IllegalStateException("Root object has not been created yet");
		
		if(!hasPropertyOnCurrentCompositeObject(propertyName))
			throw new IllegalArgumentException(propertyName + " doesn't exist on current object");
		
		ObjectContext curCtx = objectInfoStack.getTopContext();
		ObjectCategory curCategory = curCtx.getCategory();
		if(curCategory != ObjectCategory.COMPOSITE)
			throw new IllegalStateException("Current object is not of a composite type");
		
		Object owner = curCtx.getValue();
		if(owner == null)
			throw new IllegalStateException("Current object has not been created yet");
		
		String realPropertyName = getPropertyNameByTagName(propertyName, curCtx.getType());
		Object fieldValue = ReflectionUtils.getValue(owner, realPropertyName);
		Field fieldDef = ReflectionUtils.getNonStaticField(owner.getClass(), realPropertyName);
		Class<?> fieldDeclaredType = fieldDef.getType();
		ObjectCategory category = ObjectCategory.checkCategory(fieldDeclaredType);
		
		
		
		int curLevel = curCtx.getLevel();
		ObjectContext nextCtx = null;
		switch(category) {
		case SIMPLE:
		case COMPOSITE:
			nextCtx = new FieldContext(fieldValue, curCtx, category, owner, realPropertyName, fieldDef, curLevel + 1);
			break;
		case ARRAY:
			nextCtx = new ArrayFieldContext(fieldValue, curCtx, category, owner, realPropertyName, fieldDef, curLevel + 1);
			break;
		case LIST:
			nextCtx = new ListFieldContext(fieldValue, curCtx, category, owner, realPropertyName, fieldDef, curLevel + 1);
			break;
		case SET:
			nextCtx = new SetFieldContext(fieldValue, curCtx, category, owner, realPropertyName, fieldDef, curLevel + 1);
			break;
		case MAP:
			nextCtx = new MapFieldContext(fieldValue, curCtx, category, owner, realPropertyName, fieldDef, curLevel + 1);
			break;
		case MAPENTRY:
			throw new IllegalStateException("Category of the current field can't be of type of Map.Entry");
		}
		
		objectInfoStack.push(nextCtx);
		return nextCtx;
		
	}
	
	public ObjectContext endCurrentContext() {
		ObjectContext curCtx = null;
		try {
			curCtx = objectInfoStack.pop();
			if(curCtx instanceof ArrayContext) {
				ArrayContext arrayCtx = (ArrayContext)curCtx;
				arrayCtx.setArrayValueFromList();
			}else if(curCtx instanceof  ListContext) {
				ListContext listCtx = (ListContext)curCtx;
				listCtx.setListValue();
			}else if(curCtx instanceof  SetContext) {
				SetContext setCtx = (SetContext)curCtx;
				setCtx.setListValue();
			}else if(curCtx instanceof  MapContext) {
				MapContext mapCtx = (MapContext)curCtx;
				mapCtx.setMapValue();
			}else if(curCtx instanceof MapEntryContext) {
				MapEntryContext entryCtx = (MapEntryContext)curCtx;
				entryCtx.setMapEntry();
			}
		}catch(EmptyStackException emptyEx) {
		}
		return curCtx;
	}
	
	public ObjectContext getCurrentContext() {
		ObjectContext ctx = null;
		try {
			ctx = objectInfoStack.peek();
		}catch(EmptyStackException emptyEx) {
		}
		return ctx;
	}
	
	public ObjectContext getCurrentObjectContext() {
		ObjectContext objCtx = objectInfoStack.getTopContext();
		return objCtx;
	}
	
	public void setValueOnCurrentProperty(Object value) {
		ObjectContext objCtx = objectInfoStack.getTopContext();
		if(!(objCtx instanceof FieldContext))
			throw new IllegalStateException("Current object context is not of type field context");
		
		
		FieldContext fieldCtx = (FieldContext)objCtx;
//		String fieldName = fieldCtx.getFieldName();
//		Field fieldDef = ctx.getDefinition();
		Object owner = fieldCtx.getOwner();
		if(owner == null)
			throw new IllegalStateException("Parent object has not been created yet");
		
//		ReflectionUtils.setValue(owner, fieldName, value);
		fieldCtx.setValue(value);
	}
	
	public void createObjectOnCurrentProperty() {
		FieldContext ctx = (FieldContext)objectInfoStack.getTopContext();
//		Field fieldDef = ctx.getDefinition();
//		Class<?> fieldType = fieldDef.getType();
		Class<?> fieldType = ctx.getType();
		ObjectCategory category = ctx.getCategory();
		Object value;
		switch(category) {
		case SIMPLE:
			throw new IllegalStateException("For simple type, the value should be assigned explicitly");
		case COMPOSITE:
			value = ReflectionUtils.newInstance(fieldType);
			ctx.setValue(value);
			break;
		case ARRAY: 
			throw new IllegalStateException("Array type is not supported");
//			break;
		case LIST:
			throw new IllegalStateException("List type is not supported");
//			break;
		case SET:
			throw new IllegalStateException("Set type is not supported");
//			break;
		case MAP:
			throw new IllegalStateException("Map type is not supported");
//			break;
		case MAPENTRY:
			throw new IllegalStateException("Category of the current field can't be of type of Map.Entry");
		}
	}
	
	public ArrayElementContext startNewElementOnCurrentArrayObject() {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof ArrayContext)) {
			throw new IllegalStateException("Current Object is not of array type"); 
		}
		
		ArrayContext arrayCtx = (ArrayContext)curCtx;
		ArrayElementContext elemCtx = arrayCtx.startNewElement();
		objectInfoStack.push(elemCtx);
		return elemCtx;
	}
	
	public void setValueOnCurrentArrayElement(Object value) {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof ArrayElementContext)) {
			throw new IllegalStateException("Current Object is not a array element"); 
		}
		
		ArrayElementContext elemCtx = (ArrayElementContext)curCtx;
		elemCtx.setValue(value);
	}
	
	public void createCurrentArrayElementObject() {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof ArrayElementContext)) {
			throw new IllegalStateException("Current Object is not a array element"); 
		}		
		ObjectCategory category = curCtx.getCategory();
		if(category == ObjectCategory.SIMPLE)
			throw new IllegalStateException("For simple type, the value should be assigned explicitly");
		
		ArrayElementContext elemCtx = (ArrayElementContext)curCtx;
		elemCtx.createObject();
	}
	
	public void setValueOnCurrentSimpleObjectFromString(String strValue, String specifiedClass) {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		ObjectCategory category = curCtx.getCategory();
		if(category == ObjectCategory.SIMPLE ) {
			Class<?>  type = curCtx.getType();
			Object value = null;
			
			TypeConverter converter = null;
			if(converterManager != null)
				converter = converterManager.getconverter(curCtx);
			if(converter != null) {
				try {
					value = converter.parse(strValue);
				} catch (ParseException e) {
					throw new IllegalStateException("Data format error.", e);
				}
			}else {
				if(type.equals(Object.class) && specifiedClass != null) {
					value = SimpleTypeUtils.getValueFromString(specifiedClass, strValue);
				}else {
					value = SimpleTypeUtils.getValueFromString(type, strValue);
				}
			}
			setValueOnCurrentObject(value);
		}
		else
			throw new IllegalStateException("Current object is not of simple type.");
	}
	
	public void setValueOnCurrentObject(Object value) {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(curCtx instanceof FieldContext) {
			setValueOnCurrentProperty(value);
		}else if(curCtx instanceof ArrayElementContext) {
			setValueOnCurrentArrayElement(value);
		}else if(curCtx instanceof ListElementContext) {
			setValueOnCurrentListElement(value);
		}else if(curCtx instanceof SetElementContext) {
			setValueOnCurrentSetElement(value);
		}else if(curCtx instanceof MapKeyContext) {
			setValueOnCurrentMapKey(value);
		}else if(curCtx instanceof MapValueContext) {
			setValueOnCurrentMapValue(value);
		}
	}

	public void setValueOnCurrentListElement(Object value) {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof ListElementContext)) {
			throw new IllegalStateException("Current Object is not a List element"); 
		}
		
		ListElementContext elemCtx = (ListElementContext)curCtx;
		elemCtx.setValue(value);
		
	}


	public ListElementContext startNewElementOnCurrentListObject() {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof ListContext)) {
			throw new IllegalStateException("Current Object is not of list type"); 
		}
		
		ListContext listCtx = (ListContext)curCtx;
		ListElementContext elemCtx = listCtx.startNewElement();
		objectInfoStack.push(elemCtx);
		return elemCtx;
	}

	public void createCurrentListElementObject() {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof ListElementContext)) {
			throw new IllegalStateException("Current Object is not a list element"); 
		}		
		ObjectCategory category = curCtx.getCategory();
		if(category == ObjectCategory.SIMPLE)
			throw new IllegalStateException("For simple type, the value should be assigned explicitly");
		
		ListElementContext elemCtx = (ListElementContext)curCtx;
		elemCtx.createObject();
		
	}

	public SetElementContext startNewElementOnCurrentSetObject() {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof SetContext)) {
			throw new IllegalStateException("Current Object is not of list type"); 
		}
		
		SetContext setCtx = (SetContext)curCtx;
		SetElementContext elemCtx = setCtx.startNewElement();
		objectInfoStack.push(elemCtx);
		return elemCtx;
	}
	
	public void createCurrentSetElementObject() {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof SetElementContext)) {
			throw new IllegalStateException("Current Object is not a Set element"); 
		}		
		ObjectCategory category = curCtx.getCategory();
		if(category == ObjectCategory.SIMPLE)
			throw new IllegalStateException("For simple type, the value should be assigned explicitly");
		
		SetElementContext elemCtx = (SetElementContext)curCtx;
		elemCtx.createObject();
		
	}
	
	public void setValueOnCurrentSetElement(Object value) {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof SetElementContext)) {
			throw new IllegalStateException("Current Object is not a Set element"); 
		}
		
		SetElementContext elemCtx = (SetElementContext)curCtx;
		elemCtx.setValue(value);
		
	}
	
	
	public MapEntryContext startNewEntryOnCurrentMapObject() {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof MapContext)) {
			throw new IllegalStateException("Current Object is not of map type"); 
		}
		
		MapContext mapCtx = (MapContext)curCtx;
		MapEntryContext elemCtx = mapCtx.startNewEntry();
		objectInfoStack.push(elemCtx);
		return elemCtx;
	}
	

	
	public void setValueOnCurrentMapEntry(Map.Entry entry) {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof MapEntryContext)) {
			throw new IllegalStateException("Current Object is not a map entry"); 
		}
		
		MapEntryContext entryCtx = (MapEntryContext)curCtx;
		entryCtx.setValue(entry);
		
	}
	
	public MapKeyContext startKeyOnCurrentMapEntryObject() {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof MapEntryContext)) {
			throw new IllegalStateException("Current Object is not of map entry type"); 
		}
		
		MapEntryContext entryCtx = (MapEntryContext)curCtx;
		MapKeyContext keyCtx = entryCtx.startMapKey();
		objectInfoStack.push(keyCtx);
		return keyCtx;
	}
	
	public MapValueContext startValueOnCurrentMapEntryObject() {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof MapEntryContext)) {
			throw new IllegalStateException("Current Object is not of map entry type"); 
		}
		
		MapEntryContext entryCtx = (MapEntryContext)curCtx;
		MapValueContext valueCtx = entryCtx.startMapValue();
		objectInfoStack.push(valueCtx);
		return valueCtx;
	}
	
	public void setValueOnCurrentMapKey(Object value) {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof MapKeyContext)) {
			throw new IllegalStateException("Current Object is not a map entry"); 
		}
		
		MapKeyContext keyCtx = (MapKeyContext)curCtx;
		keyCtx.setValue(value);
		
	}
	
	public void setValueOnCurrentMapValue(Object value) {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof MapValueContext)) {
			throw new IllegalStateException("Current Object is not a map entry"); 
		}
		
		MapValueContext valueCtx = (MapValueContext)curCtx;
		valueCtx.setValue(value);
		
	}
	
	
	public void createCurrentMapKeyObject() {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof MapKeyContext)) {
			throw new IllegalStateException("Current Object is not a map key"); 
		}		
		ObjectCategory category = curCtx.getCategory();
		if(category == ObjectCategory.SIMPLE)
			throw new IllegalStateException("For simple type, the value should be assigned explicitly");
		
		MapKeyContext keyCtx = (MapKeyContext)curCtx;
		keyCtx.createObject();
		
	}
	
	public void createCurrentMapValueObject() {
		ObjectContext curCtx = objectInfoStack.getTopContext();
		if(!(curCtx instanceof MapValueContext)) {
			throw new IllegalStateException("Current Object is not a map value"); 
		}		
		ObjectCategory category = curCtx.getCategory();
		if(category == ObjectCategory.SIMPLE)
			throw new IllegalStateException("For simple type, the value should be assigned explicitly");
		
		MapValueContext valueCtx = (MapValueContext)curCtx;
		valueCtx.createObject();
		
	}
	
	public Object getTargetObject() {
		return rootContext.getValue();
	}	
	

}
