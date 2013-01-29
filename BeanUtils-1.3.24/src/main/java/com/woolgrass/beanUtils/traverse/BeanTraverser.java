package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.ReflectionUtils;
import com.woolgrass.beanUtils.ReflectionUtils.FieldCallback;
import com.woolgrass.beanUtils.objectTracker.ObjectTracker;
import com.woolgrass.beanUtils.traverse.NonSimpleObjectReferenceChecker.ObjecReferenceInfo;



public class BeanTraverser implements Traverser{
	protected Object targetBean;
	protected FieldProcessor processor;
	protected Stack<TargetContext> contextStack = new Stack<TargetContext>();
	protected ObjectTracker objectTracker;
	
	public BeanTraverser(Object targetBean, FieldProcessor inspector) {
		this.targetBean = targetBean;
		this.processor = inspector;
		
	}	
	
	public void startTraversal() {
		objectTracker = new ObjectTracker(targetBean);
		ObjectCategory category = ObjectCategory.checkCategory(this.targetBean);
		RootObjectContext rootCtx = new RootObjectContext(this.targetBean, category, this, null);		
		contextStack.push(rootCtx);
		this.processor.processRootObject(rootCtx);
		//traverse(this.targetBean);
		contextStack.pop();
	}
	
	public void traverseCurrentComponent() {
		TargetContext curContext = contextStack.peek();
		Object curtObject = curContext.getValue();
		if(curtObject == null) {
			//throw new IllegalArgumentException("targetObject is null");
			return;
		}
			
		//traverse(curtObject);
		//Type declaredType = curContext.getDeclaredType();
		ObjectCategory category = curContext.getCategory();
		switch(category) {
		case SIMPLE:
			traverseSimpleObject(curContext);
			break;
		case COMPOSITE:
			traverseCompositeObject(curContext);
			break;
		case ARRAY: 
			traverseArrayObject((ArrayFieldContext)curContext);
			break;
		case LIST:
			traverseListObject((ListFieldContext)curContext);
			break;
		case SET:
			traverseSetObject((SetFieldContext)curContext);
			break;
		case MAP:
			traverseMapObject((MapFieldContext)curContext);
			break;
		case MAPENTRY:
			//throw new IllegalArgumentException("targetObject can't be of type of Map.Entry");
			traverseMapEntryObject((MapEntryContext)curContext);
			break;
		}
	}
/*	
	protected void traverse(Object targetObject) {
		if(targetObject == null)
			throw new IllegalArgumentException("targetObject is null");
		
		ObjectCategory category = ObjectCategory.checkCategory(targetObject.getClass());
		switch(category) {
		case SIMPLE:
			traverseSimpleObject(targetObject);
			break;
		case COMPOSITE:
			traverseCompositeObject(targetObject);
			break;
		case ARRAY: 
			traverseArrayObject(targetObject);
			break;
		case LIST:
			traverseListObject(targetObject);
			break;
		case SET:
			traverseSetObject(targetObject);
			break;
		case MAP:
			traverseMapObject(targetObject);
			break;
		case MAPENTRY:
			throw new IllegalArgumentException("targetObject can't be of type of Map.Entry");
		}
	}
*/
	private void traverseMapObject(MapFieldContext context) {
		Object mapObject = context.getValue();
		Map map = (Map)mapObject;
		Set<Entry> entrySet = map.entrySet();
		Type keyType = context.getEntryKeyDeclaredType();
		Type valueType = context.getEntryValueDeclaredType();
		 
		int i = 0;
		for(Entry entry : entrySet) {
			String path = objectTracker.getTopObjectPath() + "[" + i + "]";
			TargetContext parentCtx = contextStack.peek();
			int level = parentCtx.getLevel() + 1;
			MapEntryContext entryCtx = new MapEntryContext(entry, keyType, valueType, map,
					i, ObjectCategory.MAPENTRY, parentCtx, this, level);
			contextStack.push(entryCtx);
			this.processor.processMapEntryElement(entryCtx, path);
			contextStack.pop();
			i++;
		}
		
	}
	
	private void traverseMapEntryObject(final MapEntryContext context) {
		final Entry entryObject = (Entry)context.getValue();
		
		final Object key = entryObject.getKey(); 
		final Type keyType = context.getKeyDeclaredType();
		final ObjectCategory keyCategory = ObjectCategory.checkCategory(key);
//		MapKeyContext keyCxt = new MapKeyContext(key, keyType, entryObject, context.getIndex(), keyCategory,
//				contextStack.peek(), this, contextStack.size() + 1);
		
		ObjectProcessTemplate mapkeyTemplate = new ObjectProcessTemplate() {

			@Override
			protected void addToObjectTracker() {
//				MapKeyContext setElemCtx = (MapKeyContext)fieldContext;
				int index = context.getIndex();
				Object fieldValue = key;
				objectTracker.pushMapKeyObject(fieldValue, index);						
			}
			@Override
			protected TargetContext createTargetContext() {
				TargetContext parentCtx = contextStack.peek();
				int level = parentCtx.getLevel() + 1;
				MapKeyContext keyCxt = new MapKeyContext(key, keyType, entryObject, context.getIndex(), keyCategory,
						parentCtx, BeanTraverser.this, level);
				return keyCxt;
			}
			@Override
			protected void processNonCrossReferenceField(TargetContext fieldContext, String contextPath) {
				ObjectCategory category = fieldContext.getCategory();
				if(category == ObjectCategory.SIMPLE)
					processor.processSimpleMapKeyElement((MapKeyContext)fieldContext, contextPath);
				else
					processor.processComplexMapKeyElement((MapKeyContext)fieldContext, contextPath);						
			}
		};				
		mapkeyTemplate.process();	
		
		
//		contextStack.push(keyCxt);
//		this.processor.processMapKeyElement(keyCxt);
//		contextStack.pop();
		
		final Object value = entryObject.getValue(); 
		final Type valueType = context.getValueDeclaredType();
		final ObjectCategory valueCategory = ObjectCategory.checkCategory(value);
//		MapValueContext valueCxt = new MapValueContext(value, valueType, entryObject, context.getIndex(), valueCategory,
//				contextStack.peek(), this, contextStack.size() + 1);
		
		ObjectProcessTemplate mapValueTemplate = new ObjectProcessTemplate() {
			@Override
			protected void addToObjectTracker() {
//				MapValueContext setElemCtx = (MapValueContext)fieldContext;
				int index = context.getIndex();
				Object fieldValue = value;
				objectTracker.pushMapValueObject(fieldValue, index);						
			}
			@Override
			protected TargetContext createTargetContext() {
				TargetContext parentCtx = contextStack.peek();
				int level = parentCtx.getLevel() + 1;
				MapValueContext valueCxt = new MapValueContext(value, valueType, entryObject, context.getIndex(), valueCategory,
						parentCtx, BeanTraverser.this, level);
				return valueCxt;
			}
			@Override
			protected void processNonCrossReferenceField(TargetContext fieldContext, String contextPath) {
				ObjectCategory category = fieldContext.getCategory();
				if(category == ObjectCategory.SIMPLE)
					processor.processSimpleMapValueElement((MapValueContext)fieldContext, contextPath);
				else
					processor.processComplexMapValueElement((MapValueContext)fieldContext, contextPath);
			}

		};				
		mapValueTemplate.process();	
		
		
//		contextStack.push(valueCxt);
//		this.processor.processMapValueElement(valueCxt);
//		contextStack.pop();
	}

	private void traverseSetObject(SetFieldContext context) {		
		Object setObject = context.getValue();
		final Set<?> set = (Set<?>)setObject;
		final Type elementType = context.getElementDeclaredType();
		
		int i = 0;
		for(Object element : set) {
			final int index = i;
			final Object elmValue = element;
			final ObjectCategory category = ObjectCategory.checkCategory(element);
//			SetElementContext elemCtx = new SetElementContext(elmValue, elementType, set,
//					index, category, contextStack.peek(), this, contextStack.size() + 1);
			
			ObjectProcessTemplate template = new ObjectProcessTemplate() {
				@Override
				protected void addToObjectTracker() {
//					SetElementContext setElemCtx = (SetElementContext)fieldContext;
//					int index = setElemCtx.getIndex();
//					Object fieldValue = element;
					objectTracker.pushSetElemObject(elmValue, index);						
				}
				@Override
				protected TargetContext createTargetContext() {
					TargetContext parentCtx = contextStack.peek();
					int level = parentCtx.getLevel() + 1;
					SetElementContext elemCtx = new SetElementContext(elmValue, elementType, set,
							index, category, parentCtx, BeanTraverser.this, level);
					return elemCtx;
				}
				@Override
				protected void processNonCrossReferenceField(TargetContext fieldContext, String contextPath) {
					ObjectCategory category = fieldContext.getCategory();
					if(category == ObjectCategory.SIMPLE)
						processor.processSimpleSetElement((SetElementContext)fieldContext, contextPath);
					else
						processor.processComplexSetElement((SetElementContext)fieldContext, contextPath);
				}
			};				
			template.process();	
			
//			contextStack.push(elemCtx);
//			this.processor.processSetElement(elemCtx);
//			contextStack.pop();
			i++;
		}
		
	}

	private void traverseListObject(ListFieldContext context) {
		Object listObject = context.getValue();
		final Type elementType = context.getElementDeclaredType();
		
		final List<?> list = (List<?>)listObject;
		int size = list.size();
		for(int i = 0; i < size; i++) {
			final int index = i;
			final Object element = list.get(i);
			final ObjectCategory category = ObjectCategory.checkCategory(element);
//			ListElementContext elemCtx = new ListElementContext(element, elementType, list,
//					i, category, contextStack.peek(), this, contextStack.size() + 1);
			
			ObjectProcessTemplate template = new ObjectProcessTemplate() {
				@Override
				protected void addToObjectTracker() {
//					ListElementContext listElemCtx = (ListElementContext)fieldContext;
//					int index = listElemCtx.getIndex();
//					Object fieldValue = fieldContext.getValue();
					objectTracker.pushListElemObject(element, index);						
				}	
				@Override
				protected TargetContext createTargetContext() {
					TargetContext parentCtx = contextStack.peek();
					int level = parentCtx.getLevel() + 1;
					ListElementContext elemCtx = new ListElementContext(element, elementType, list,
							index, category, parentCtx, BeanTraverser.this, level);
					return elemCtx;
				}
				@Override
				protected void processNonCrossReferenceField(TargetContext fieldContext, String contextPath) {
					ObjectCategory category = fieldContext.getCategory();
					if(category == ObjectCategory.SIMPLE)
						processor.processSimpleListElement((ListElementContext)fieldContext, contextPath);
					else
						processor.processComplexListElement((ListElementContext)fieldContext, contextPath);
				}

			};				
			template.process();	
			
			
//			contextStack.push(elemCtx);
//			this.processor.processListElement(elemCtx);
//			contextStack.pop();
		}
		
	}

	private void traverseArrayObject(ArrayFieldContext context) {
		final Class<?> elementType = context.getElementDeclaredType();
//		if(context instanceof ArrayFieldContext) {
//			ArrayFieldContext arrCxt = (ArrayFieldContext)context;
//			elementType = context.getElementDeclaredType();
//		} else {
//			Type arrayType = context.getDeclaredType();
//			if(arrayType instanceof Class) {
//				elementType = ((Class)arrayType).getComponentType();
//			} 
//		}
		
		final Object arrayObject = context.getValue();
		final int arrLen = Array.getLength(arrayObject);
		for(int i = 0; i < arrLen; i++) {
			final int index = i;
			final Object element = Array.get(arrayObject, i);
			final ObjectCategory category = ObjectCategory.checkCategory(element);
//			ArrayElementContext elemCtx = new ArrayElementContext(element, elementType, arrayObject,
//					i, arrLen, category, contextStack.peek(), this, contextStack.size() + 1);
			
			ObjectProcessTemplate template = new ObjectProcessTemplate() {
				@Override
				protected void addToObjectTracker() {
//					ArrayElementContext arrayElemCtx = (ArrayElementContext)fieldContext;
//					int index = arrayElemCtx.getIndex();
//					Object fieldValue = fieldContext.getValue();
					objectTracker.pushArrayElemObject(element, index);						
				}					

				@Override
				protected TargetContext createTargetContext() {
					TargetContext parentCtx = contextStack.peek();
					int level = parentCtx.getLevel() + 1;
					ArrayElementContext elemCtx = new ArrayElementContext(element, elementType, arrayObject,
							index, arrLen, category, parentCtx, BeanTraverser.this, level);
					return elemCtx;
				}
				@Override
				protected void processNonCrossReferenceField(TargetContext fieldContext, String contextPath) {
					ObjectCategory category = fieldContext.getCategory();
					if(category == ObjectCategory.SIMPLE)
						processor.processSimpleArrayElement((ArrayElementContext)fieldContext, contextPath);
					else
						processor.processComplexArrayElement((ArrayElementContext)fieldContext, contextPath);
				}
			};				
			template.process();	
			
//			contextStack.push(elemCtx);
//			this.processor.processArrayElement(elemCtx);
//			contextStack.pop();
		}
		
	}
	
	private Map<String, Field> getBeanFeilds(Class targetClass) {
		final Map<String, Field> fieldsMap = new LinkedHashMap<String, Field>();
		FieldCallback fc = new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException
			{
				fieldsMap.put(field.getName(), field);
			}
		};
		
		ReflectionUtils.doWithNonStaticFields(targetClass, fc);
		return fieldsMap;
	}

	private void traverseCompositeObject(TargetContext context) {
		Object targetObject = context.getValue();
		//Map<String, Field> fieldMap = ReflectionUtils.getNonStaticFieldsMap(targetObject.getClass());
		Map<String, Field> fieldMap = getBeanFeilds(targetObject.getClass());
		Set<String> fieldNameSet = fieldMap.keySet();
		for(final String fieldName : fieldNameSet) {
			final Field field = fieldMap.get(fieldName);
			final Object fieldValue = ReflectionUtils.getValue(targetObject, fieldName);
			Class<?> fieldDeclaredType = field.getType();
			//Class<?> fieldRTType = fieldValue.getClass();
			final ObjectCategory	category = ObjectCategory.checkCategory(fieldDeclaredType);
			
//			if(category != ObjectCategory.SIMPLE && category != ObjectCategory.MAPENTRY) {
//				
//			}
			
			switch(category) {
			case SIMPLE:
			{
				objectTracker.pushSimpleObject(fieldValue, fieldName);
				TargetContext parentCtx = contextStack.peek();
				int level = parentCtx.getLevel() + 1;
				FieldContext fieldContext = new FieldContext(fieldName, fieldValue, field, 
						parentCtx, category, this, level);
				contextStack.push(fieldContext);
				
				this.processor.processSimpleField(fieldContext, objectTracker.getTopObjectPath());
				contextStack.pop();	
				objectTracker.pop();
				
				break;
			}
			case COMPOSITE:
			{
//				FieldContext fieldContext = new FieldContext(fieldName, fieldValue, field, 
//						contextStack.peek(), category, this, contextStack.size() + 1);
				ObjectProcessTemplate template = new ObjectProcessTemplate() {
					@Override
					protected void addToObjectTracker() {
//						String fieldName = fieldContext.getName();
//						Object fieldValue = fieldContext.getValue();
						objectTracker.pushCompositeObject(fieldValue, fieldName);
						
					}

					@Override
					protected TargetContext createTargetContext() {
						TargetContext parentCtx = contextStack.peek();
						int level = parentCtx.getLevel() + 1;
						FieldContext fieldContext = new FieldContext(fieldName, fieldValue, field, 
								parentCtx, category, BeanTraverser.this, level);
						return fieldContext;
					}					
					@Override
					protected void processNonCrossReferenceField(TargetContext fieldContext, String contextPath) {
						processor.processCompositeField((FieldContext)fieldContext, contextPath);						
					}
				};				
				template.process();				
				break;
			}
			case ARRAY: 
			{
//				ArrayFieldContext arrayContext = new ArrayFieldContext(fieldName, fieldValue, field, 
//						contextStack.peek(), category, this, contextStack.size() + 1);
				ObjectProcessTemplate template = new ObjectProcessTemplate() {
					@Override
					protected void addToObjectTracker() {
//						String fieldName = fieldContext.getName();
//						Object fieldValue = fieldContext.getValue();
						objectTracker.pushArrayObject(fieldValue, fieldName);						
					}

					@Override
					protected TargetContext createTargetContext() {
						TargetContext parentCtx = contextStack.peek();
						int level = parentCtx.getLevel() + 1;
						ArrayFieldContext arrayContext = new ArrayFieldContext(fieldName, fieldValue, field, 
								parentCtx, category, BeanTraverser.this, level);
						return arrayContext;
					}
					@Override
					protected void processNonCrossReferenceField(TargetContext fieldContext, String contextPath) {
						processor.processArrayField((ArrayFieldContext)fieldContext, contextPath);						
					}
				};				
				template.process();	
					
				break;
			}
			case LIST:
			{
//				ListFieldContext listContext = new ListFieldContext(fieldName, fieldValue, field, 
//						contextStack.peek(), category, this, contextStack.size() + 1);
				ObjectProcessTemplate template = new ObjectProcessTemplate() {
					@Override
					protected void addToObjectTracker() {
//						String fieldName = fieldContext.getName();
//						Object fieldValue = fieldContext.getValue();
						objectTracker.pushListObject(fieldValue, fieldName);							
					}

					@Override
					protected TargetContext createTargetContext() {
						TargetContext parentCtx = contextStack.peek();
						int level = parentCtx.getLevel() + 1;
						ListFieldContext listContext = new ListFieldContext(fieldName, fieldValue, field, 
								parentCtx, category, BeanTraverser.this, level);
						return listContext;
					}					
					@Override
					protected void processNonCrossReferenceField(TargetContext fieldContext, String contextPath) {
						processor.processListField((ListFieldContext)fieldContext, contextPath);						
					}
				};				
				template.process();	
				
				break;
			}				
			case SET:
			{
//				SetFieldContext setContext = new SetFieldContext(fieldName, fieldValue, field, 
//						contextStack.peek(), category, this, contextStack.size() + 1);
				ObjectProcessTemplate template = new ObjectProcessTemplate() {
					@Override
					protected void addToObjectTracker() {
//						String fieldName = fieldContext.getName();
//						Object fieldValue = fieldContext.getValue();
						objectTracker.pushSetObject(fieldValue, fieldName);						
					}

					@Override
					protected TargetContext createTargetContext() {
						TargetContext parentCtx = contextStack.peek();
						int level = parentCtx.getLevel() + 1;
						SetFieldContext setContext = new SetFieldContext(fieldName, fieldValue, field, 
								parentCtx, category, BeanTraverser.this, level);
						return setContext;
					}					
					@Override
					protected void processNonCrossReferenceField(TargetContext fieldContext, String contextPath) {
						processor.processSetField((SetFieldContext)fieldContext, contextPath);						
					}
				};				
				template.process();	
				
				break;
			}
			case MAP:
			{
//				MapFieldContext mapContext = new MapFieldContext(fieldName, fieldValue, field, 
//						contextStack.peek(), category, this, contextStack.size() + 1);
				ObjectProcessTemplate template = new ObjectProcessTemplate() {
					@Override
					protected void addToObjectTracker() {
//						String fieldName = fieldContext.getName();
//						Object fieldValue = fieldContext.getValue();
						objectTracker.pushMapObject(fieldValue, fieldName);							
					}

					@Override
					protected TargetContext createTargetContext() {
						TargetContext parentCtx = contextStack.peek();
						int level = parentCtx.getLevel() + 1;
						MapFieldContext mapContext = new MapFieldContext(fieldName, fieldValue, field, 
								parentCtx, category, BeanTraverser.this, level);
						return mapContext;
					}					
					@Override
					protected void processNonCrossReferenceField(TargetContext fieldContext, String contextPath) {
						processor.processMapField((MapFieldContext)fieldContext, contextPath);						
					}
				};				
				template.process();	
				break;
			}
			case MAPENTRY:
				throw new IllegalStateException(fieldName + "can't be of type of Map.Entry");
			}
					
		}		
	}

	private void traverseSimpleObject(TargetContext context) {
		throw new IllegalArgumentException("targetObject is a simple object");
		
	}

	protected void preProcessCrossReferenceField(ObjecReferenceInfo refInfo) {
		
	}
	
//	protected abstract class ObjectProcessTemplate {
//		public abstract void process();
//		protected abstract void processObjectTracker(); 
//		protected abstract void processNonCrossReferenceField();
//	}

	protected abstract class  ObjectProcessTemplate{
	
//		protected TargetContext fieldContext;
//
//		public ObjectProcessTemplate(TargetContext fieldContext) {
//			this.fieldContext = fieldContext;
//		}
		
		public void process() {
			TargetContext fieldContext = createTargetContext();
			contextStack.push(fieldContext);
			ObjecReferenceInfo refInfo = NonSimpleObjectReferenceChecker.getReferenceInfo(objectTracker, fieldContext);
			addToObjectTracker();
			
			String contextPath = objectTracker.getTopObjectPath();
			
			if(!refInfo.isExists()) {
				processNonCrossReferenceField(fieldContext, contextPath);
			}
			else {
				preProcessCrossReferenceField(refInfo);
				
				if(refInfo.isReferenceRoot()) {
					processor.processRootObjectCrossReferenceField(fieldContext, contextPath);
				}else {
					processor.processNonRootObjectCrossReferenceField(fieldContext, contextPath, refInfo.getReferredObjectPath());
				}
			}
			objectTracker.pop();
			contextStack.pop();
		}
		
		protected abstract TargetContext createTargetContext(); 
		protected abstract void addToObjectTracker(); 		
		protected abstract void processNonCrossReferenceField(TargetContext fieldContext, String contextPath);
	}
	
	
}
