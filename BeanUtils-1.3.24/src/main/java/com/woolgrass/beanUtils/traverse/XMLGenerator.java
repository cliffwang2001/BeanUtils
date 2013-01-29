package com.woolgrass.beanUtils.traverse;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang.StringEscapeUtils;

//import org.apache.commons.lang3.StringEscapeUtils;

import com.woolgrass.beanUtils.CollectionFieldStruct;
import com.woolgrass.beanUtils.ConverterManager;
import com.woolgrass.beanUtils.DefaultDatetimeConverter;
import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.TypeConverter;
import com.woolgrass.beanUtils.CollectionFieldStruct.ArrayStruct;
import com.woolgrass.beanUtils.CollectionFieldStruct.ListStruct;
import com.woolgrass.beanUtils.CollectionFieldStruct.MapStruct;
import com.woolgrass.beanUtils.CollectionFieldStruct.SetStruct;
import com.woolgrass.beanUtils.traverse.xmlTagMapping.ClassTagMapping;
import com.woolgrass.beanUtils.traverse.xmlTagMapping.ClassTagMappingManager;

public class XMLGenerator implements FieldProcessor {

	public static final int PRINT_FULL_CLASSNAME = 0;
	public static final int PRINT_SHORT_CLASSNAME = 1;
	
//	protected Object targetBean;
	protected Traverser beanTraverser;
	protected boolean showNullElement;
	protected int classNamePrintType = PRINT_FULL_CLASSNAME;
	protected boolean crossReferenceAllowed = true;

	
	protected ClassTagMappingManager tagMappingManager = new ClassTagMappingManager();
	protected CollectionFieldStruct collectionStruct = CollectionFieldStruct.FIELDNAME_AS_GROUPING_TAG;
	protected ConverterManager converterManager = new ConverterManager();
	
	protected XmlTagPrinter tagPrinter;
	
	public XMLGenerator() {
		this(true);
	}
	public XMLGenerator(boolean crossReferenceAllowed) {
		this.crossReferenceAllowed = crossReferenceAllowed;
		
		
//		this.targetBean = targetBean;
		
		converterManager.addTypeConverter(Date.class, new DefaultDatetimeConverter());
	}
	
//	public StringBuilder getBuilder() {
//		return builder;
//	}
	
	public void addTypeConverter(Class<?> type, TypeConverter converter) {
		converterManager.addTypeConverter(type, converter);
	}
	
	public void addFieldConverter(Class<?> clazz, String fieldName, TypeConverter converter) {
		converterManager.addFieldConverter(clazz, fieldName, converter);
	}
	
	public void addClassTagMapping(ClassTagMapping clsTagMaping) {
		tagMappingManager.addClassTagMapping(clsTagMaping);
	}
	
	public void addClassTagMapping(Class<?> targetClass, String classTagName) {
		tagMappingManager.addClassTagMapping(targetClass, classTagName);
	}
	
	public void addFieldTagMapping(Class<?> targetClass, String fieldName, String tagName) {
		tagMappingManager.addFieldTagMapping(targetClass, fieldName, tagName);
	}

	
	public void processRootObject(RootObjectContext context) {
		ObjectCategory category = context.getCategory();
		if(category == ObjectCategory.UNKNOWN) 
			return;
		Object value = context.getValue();
		Class<?> valueObjCls = value.getClass();
		String valueClsTagName = getClassTagName(valueObjCls);
//		String valueType = value.getClass().getName();
		if(category == ObjectCategory.SIMPLE) {
//			builder.append(createElement(valueClsTagName, value)).append("\n");
//			tagPrinter.printSimpleValueTag(valueClsTagName, value);
			RootObjectSimpleValueTagBuilder tagBuilder = new RootObjectSimpleValueTagBuilder(context);
			tagBuilder.appendTag();
		}else {
//			builder.append(createOpenTag(valueClsTagName)).append("\n");
//			Traverser traverser = context.getTraverser();
//			traverser.traverseCurrentComponent();
//			builder.append(createCloseTag(valueClsTagName)).append("\n");
			tagPrinter.printOpenTag(valueClsTagName);
			Traverser traverser = context.getTraverser();
			traverser.traverseCurrentComponent();
			tagPrinter.printCloseTag(valueClsTagName);
		}

	}
	
	

	public void processSimpleField(FieldContext context, String contextPath) {
		SimpleFieldTagBuilder tagBuilder = new SimpleFieldTagBuilder(context);
		tagBuilder.appendTag();
	}




	public void processCompositeField(FieldContext context, String contextPath) {
		
		ComplexFieldTagBuilder tagBuilder = new ComplexFieldTagBuilder(context);
		tagBuilder.appendTag();
		
	}

	public void processArrayField(ArrayFieldContext context, String contextPath) {
//		ComplexFieldTagBuilder tagBuilder = new ComplexFieldTagBuilder(context);
//		tagBuilder.appendTag();
		
		ArrayFieldTagBuilder tagBuilder = new ArrayFieldTagBuilder(context);
		tagBuilder.appendTag();

	}

	public void processListField(ListFieldContext context, String contextPath) {
//		ComplexFieldTagBuilder tagBuilder = new ComplexFieldTagBuilder(context);
//		tagBuilder.appendTag();
		ListFieldTagBuilder tagBuilder = new ListFieldTagBuilder(context);
		tagBuilder.appendTag();

	}

	public void processSetField(SetFieldContext context, String contextPath) {
//		ComplexFieldTagBuilder tagBuilder = new ComplexFieldTagBuilder(context);
//		tagBuilder.appendTag();
		SetFieldTagBuilder tagBuilder = new SetFieldTagBuilder(context);
		tagBuilder.appendTag();

	}

	public void processMapField(MapFieldContext context, String contextPath) {
//		ComplexFieldTagBuilder tagBuilder = new ComplexFieldTagBuilder(context);
//		tagBuilder.appendTag();
		MapFieldTagBuilder tagBuilder = new MapFieldTagBuilder(context);
		tagBuilder.appendTag();

	}


	public void processMapEntryElement(MapEntryContext context, String contextPath) {
//		int level = context.getLevel();
//		String name = context.getName();
//		appendBlankSpace(level);
//		builder.append(createOpenTag(name)).append("\n");	
//		Traverser traverser = context.getTraverser();
//		traverser.traverseCurrentComponent();
//		appendBlankSpace(level);
//		builder.append(createCloseTag(name)).append("\n");
		
//		ContextTagBuilder tagBuilder = new ContextTagBuilder(context);
		MapEntryTagBuilder tagBuilder = new MapEntryTagBuilder(context);
		tagBuilder.appendTag();

	}
	
	private String getClassTagName(Class<?> objCls) {
		String clsTagName = tagMappingManager.getClassTagName(objCls);
		if(clsTagName == null) {
			if(classNamePrintType == PRINT_FULL_CLASSNAME)
//				clsTagName = objCls.getName();
				clsTagName = ObjectCategory.getClassName(objCls);
			else if(classNamePrintType == PRINT_SHORT_CLASSNAME)
				clsTagName = objCls.getSimpleName();
		}
		return clsTagName;
	}
	
	protected String getFieldTag(Class<?> parentCls, String fieldName) {
		String fieldTag = tagMappingManager.getFieldTagName(parentCls, fieldName);
		if(fieldTag == null)
			fieldTag = fieldName;
		return fieldTag;
	}
	
	public String getXml(Object targetBean) {
		if(targetBean == null) {
			throw new IllegalArgumentException("targetBean is null");
		}
		tagPrinter = new XmlTagPrinter();
		traverseBean(targetBean);
		return tagPrinter.toString();
	}
	protected void traverseBean(Object targetBean) {
		if(crossReferenceAllowed) {
			beanTraverser = new BeanTraverser(targetBean, this);
		}else {
			beanTraverser = new NonCrossReferenceFieldBeanTraverser(targetBean, this);
		}
		 
		beanTraverser.startTraversal();
	}

	public void processRootObjectCrossReferenceField(TargetContext context, String contextPath) {
		int level = context.getLevel();
		
		String name = context.getName();
//		appendBlankSpace(level);
//		builder.append("<").append(name).append(" reference=\"\\\"").append("\\>\n");
		
		tagPrinter.printOpenTagStart(name).printAttribute("reference", "/").printCombinedTagEnd().println();
		
	}

	public void processNonRootObjectCrossReferenceField(TargetContext context, String contextPath,
			String fieldPath) {
		int level = context.getLevel();
		
		String name = context.getName();
//		appendBlankSpace(level);
		fieldPath = fieldPath.replace('.', '/');
//		builder.append("<").append(name).append(" reference=\"").append(fieldPath).append("\"\\>\n");
		tagPrinter.printOpenTagStart(name).printAttribute("reference", fieldPath).printCombinedTagEnd().println();
		
	}

	public void processSimpleArrayElement(ArrayElementContext context,
			String contextPath) {
//		IndexedElementSimpleXmlTagBuilder tagBuilder = new IndexedElementSimpleXmlTagBuilder(context);
//		tagBuilder.appendTag();
		ArrayElementSimpleTagBuilder tagBuilder = new ArrayElementSimpleTagBuilder(context);
		tagBuilder.appendTag();
	}

	public void processComplexArrayElement(ArrayElementContext context,
			String contextPath) {
//		IndexedElementComplexXmlTagBuilder tagBuilder = new IndexedElementComplexXmlTagBuilder(context);
//		tagBuilder.appendTag();
		ArrayFieldElementComplexTagBuilder tagBuilder = new ArrayFieldElementComplexTagBuilder(context);
		tagBuilder.appendTag();
	}

	protected String getTypeName(TargetContext context, Object elemValue) {
		String elemTypeName = "Object";

		Type elemType = context.getDeclaredType();
		if(elemValue != null) {
//			elemTypeName = elemValue.getClass().getName();
//			elemTypeName = ObjectCategory.getClassName(elemValue.getClass());
			elemTypeName = getClassTagName(elemValue.getClass());
		}else if(elemType instanceof Class) {
//			elemTypeName = ((Class<?>)elemType).getName();
//			elemTypeName = ObjectCategory.getClassName((Class<?>)elemType);
			elemTypeName = getClassTagName((Class<?>)elemType);
		}
		return elemTypeName;
	}

	public void processSimpleListElement(ListElementContext context,
			String contextPath) {
		ListElementSimpleTagBuilder tagBuilder = new ListElementSimpleTagBuilder(context);
		tagBuilder.appendTag();
		
	}

	public void processComplexListElement(ListElementContext context,
			String contextPath) {
		ListFieldElementComplexTagBuilder tagBuilder = new ListFieldElementComplexTagBuilder(context);
		tagBuilder.appendTag();
	}

	public void processSimpleSetElement(SetElementContext context,
			String contextPath) {
		SetElementSimpleTagBuilder tagBuilder = new SetElementSimpleTagBuilder(context);
		tagBuilder.appendTag();
	}

	public void processComplexSetElement(SetElementContext context,
			String contextPath) {
		SetFieldElementComplexTagBuilder tagBuilder = new SetFieldElementComplexTagBuilder(context);
		tagBuilder.appendTag();
	}

	public void processSimpleMapKeyElement(MapKeyContext context,
			String contextPath) {
//		SimpleElementTagBuilder tagBuilder = new SimpleElementTagBuilder(context);
		MapKeySimpleTagBuilder tagBuilder = new MapKeySimpleTagBuilder(context);
		tagBuilder.appendTag();
	}

	public void processComplexMapKeyElement(MapKeyContext context,
			String contextPath) {
//		ComplexElementTagBuilder tagBuilder = new ComplexElementTagBuilder(context);
		MapKeyComplexTagBuilder tagBuilder = new MapKeyComplexTagBuilder(context);
		tagBuilder.appendTag();
	}

	public void processSimpleMapValueElement(MapValueContext context,
			String contextPath) {
//		SimpleElementTagBuilder tagBuilder = new SimpleElementTagBuilder(context);
		MapValueSimpleTagBuilder tagBuilder = new MapValueSimpleTagBuilder(context);
		tagBuilder.appendTag();
		
	}

	public void processComplexMapValueElement(MapValueContext context,
			String contextPath) {
//		ComplexElementTagBuilder tagBuilder = new ComplexElementTagBuilder(context);
		MapValueComplexTagBuilder tagBuilder = new MapValueComplexTagBuilder(context);
		tagBuilder.appendTag();
	}
	
//	private void appendSimpleValue(int level, String name, Object value) {
//		if(value == null && !showNullElement)
//			return;
//		appendBlankSpace(level);
//		builder.append(createElement(name, value)).append("\n");
//	}
	
	public boolean isShowNullElement() {
		return showNullElement;
	}
	public void setShowNullElement(boolean showNullElement) {
		this.showNullElement = showNullElement;
	}
	
	
	public  String  getXml(String rootTag, Collection collection,String elementTag) {
		
		tagPrinter = new XmlTagPrinter();
		tagPrinter.printOpenTag(rootTag);
		for(Object bean : collection) {
			addClassTagMapping(bean.getClass(), elementTag);
			traverseBean(bean);
		}
		tagPrinter.printCloseTag(rootTag);
		return tagPrinter.toString();
	}

	protected static class XmlTagPrinter {
		protected static final String BLANKSPACE = "    ";
		protected static final String OPENTAG_FORMAT = "<{0}>";
		protected static final String CLOSETAG_FORMAT = "</{0}>";
		protected static final String ATTRIBUTE_FORMAT = " {0}=\"{1}\"";
		protected static final String OPENTAG_START = "<";
		protected static final String CLOSETAG_START = "</";
		protected static final String OPENCLOSETAG_END = ">";
		protected static final String COMBINED_TAG_END = "/>";
		
		protected int tagLevel;
		protected StringBuilder builder = new StringBuilder(); ;
		
		
		protected String createElement(String tagName, Object value) {
//			if(value instanceof String) {
//				value = StringEscapeUtils.escapeXml((String)value);
//			}
			
			return MessageFormat.format(OPENTAG_FORMAT, tagName) + (value == null ? "": value) + MessageFormat.format(CLOSETAG_FORMAT, tagName);
		}
		
		public String toString() {
			return builder.toString();
		}

		protected String createOpenTag(String tagName) {
			return MessageFormat.format(OPENTAG_FORMAT, tagName);
		}
		
		protected String createCloseTag(String tagName) {
			return MessageFormat.format(CLOSETAG_FORMAT, tagName);
		}
		
		protected String createAttribute(String attrName, Object value) {
			return MessageFormat.format(ATTRIBUTE_FORMAT, attrName, value);
			
		}
		
		protected void appendBlankSpace(int level) {
			for(int i = 0; i < level - 1; i++) {
				builder.append(BLANKSPACE);			
			}
		}
		
		public void printBlankSpace() {
			appendBlankSpace(tagLevel);
		}
		
		public void incrementLevel() {
			tagLevel++;
		}
		
		public void decrementLevel() {
			tagLevel--;
		}
		
		public void printOpenTag(String tagName) {
			incrementLevel();
			printBlankSpace();
			builder.append(createOpenTag(tagName)).append("\n");
		}
		
		
		public void printCloseTag(String tagName) {
			printBlankSpace();
			builder.append(createCloseTag(tagName)).append("\n");
			decrementLevel();
		}
		
		public void printEmptyTag(String name) {
			incrementLevel();
			printBlankSpace();
			builder.append(createOpenTag(name)).append(createCloseTag(name)).append("\n");
			decrementLevel();
		}
		
		public void printSimpleValueTag(String name, Object value) {
			incrementLevel();
			printBlankSpace();
			builder.append(createElement(name, value)).append("\n");
			decrementLevel();
		}
		
		public XmlTagPrinter printAttribute(String attrName, Object value) {
			builder.append(createAttribute(attrName, value));
			return this;
		}
		public XmlTagPrinter printOpenTagStart(String name) {
			incrementLevel();
			printBlankSpace();
			builder.append(OPENTAG_START).append(name);
			return this;
		}
		public XmlTagPrinter printOpenTagEnd() {
			builder.append(OPENCLOSETAG_END);
			return this;
		}
		public XmlTagPrinter printCloseTagStart() {
			builder.append(CLOSETAG_START);
			return this;
		}
		public XmlTagPrinter printCloseTagEnd() {
			builder.append(OPENCLOSETAG_END);
			decrementLevel();
			return this;
		}
		public XmlTagPrinter printCombinedTagEnd() {
			builder.append(COMBINED_TAG_END);
			decrementLevel();
			return this;
		}
		
		public XmlTagPrinter println() {
			builder.append("\n");
			return this;
		}
		
		
	}
	
	protected abstract class AbstractXmlTagBuilder {
		protected TargetContext context;
		
		public AbstractXmlTagBuilder(TargetContext context) {
			this.context = context;
		}
		
		protected void appendSimpleValue(String name, Object value) {
			if(value == null && !showNullElement)
				return;
//			appendBlankSpace(level);
//			builder.append(createElement(name, value)).append("\n");
			if(value == null) {
				tagPrinter.printSimpleValueTag(name, value);
			}else {
//				TypeConverter converter = null;
//				Class<?> valueObjCls = value.getClass();
//				if(context instanceof FieldContext) {
//					FieldContext fieldCtx = (FieldContext)context;
//					Class<?> ownerCls = fieldCtx.getParentObjectClass();
//					String fieldName = fieldCtx.getName();
//					converter = converterManager.lookupFieldConverter(ownerCls, fieldName);
//				}
//				if(converter == null) {
//					converter = converterManager.lookupTypeConverter(valueObjCls);
//				}
				TypeConverter converter = converterManager.getConverter(context);
				if(converter != null ) {
					String strValue = converter.print(value);
					tagPrinter.printSimpleValueTag(name, strValue);
				}else {
					if(value instanceof String) {
						value = StringEscapeUtils.escapeXml((String)value);
					}
					tagPrinter.printSimpleValueTag(name, value);
				}
			}
			
		}
		
		protected void appendEmptyTag(String tagName) {
			if( !showNullElement)
				return;
			tagPrinter.printEmptyTag(tagName);
		}
		
		protected void appendNonSimpleValue(String tagName, Object value) {
			if(value == null) {
				appendEmptyTag(tagName);
			}else {
				appendNonSimpleTag(tagName);
			}
		}

		protected void appendNonSimpleTag(String tagName) {
//			appendBlankSpace(level);
//			builder.append(createOpenTag(tagName)).append("\n");
//			buildNextLevel();
//			appendBlankSpace(level);
//			builder.append(createCloseTag(tagName)).append("\n");
			tagPrinter.printOpenTag(tagName);
			buildNextLevel();
			tagPrinter.printCloseTag(tagName);
		}
		
		protected String getFieldXmlTag() {
			String fieldName = context.getName();
			Class<?> parentCls = context.getParentObjectClass();
			String fieldTag = getFieldTag(parentCls, fieldName);
			return fieldTag;
		}
		
		protected  void buildNextLevel(){
		}
		
		public abstract void appendTag();
		
	}
	
//	protected  class SimpleElementTagBuilder extends AbstractXmlTagBuilder{
//		protected TargetContext context;
//		
//		public SimpleElementTagBuilder(TargetContext context) {
//			this.context = context;
//		}
//
//		public void appendTag() {
////			int level = context.getLevel();		
//			String name = context.getName();		
//			Object value = context.getValue();
//			appendSimpleValue(name, value);
//			
//		}
//	}
	
	protected abstract class RecursiveXmlTagBuilder extends AbstractXmlTagBuilder{
		
		public RecursiveXmlTagBuilder(TargetContext context) {
			super(context);
		}
		protected void buildNextLevel() {
			Traverser traverser = context.getTraverser();
			traverser.traverseCurrentComponent();
		}
	}
	
//	protected  class ComplexElementTagBuilder extends RecursiveXmlTagBuilder{
//		
//		public ComplexElementTagBuilder(TargetContext context) {
//			super(context);
//		}
//
//		public void appendTag() {
////			int level = context.getLevel();		
//			String name = context.getName();		
//			Object value = context.getValue();
//			appendNonSimpleValue(name, value);
//			
//		}
//	}
	
	protected  class RootObjectSimpleValueTagBuilder extends AbstractXmlTagBuilder{
		
		public RootObjectSimpleValueTagBuilder(RootObjectContext context) {
			super(context);
		}
		
		public void appendTag() {
			Object value = context.getValue();
			Class<?> valueObjCls = value.getClass();
			String valueClsTagName = getClassTagName(valueObjCls);
			
			appendSimpleValue(valueClsTagName, value);
		}
	}
	
	protected  class SimpleFieldTagBuilder extends AbstractXmlTagBuilder{
		
		public SimpleFieldTagBuilder(FieldContext context) {
			super(context);
		}
		
		public void appendTag() {
			Object fieldValue = context.getValue();
			String fieldTag = getFieldXmlTag();
			
//			int level = context.getLevel();
			appendSimpleValue(fieldTag, fieldValue);
		}
	}
	
/*
	protected  class IndexedElementComplexXmlTagBuilder extends RecursiveXmlTagBuilder{
		
		public IndexedElementComplexXmlTagBuilder(IndexedElementContext context) {
			super(context);
		}
		
		public void appendTag() {
			Object elemValue = context.getValue();
			String elemTypeName = getTypeName(context, elemValue); 
			
			appendNonSimpleValue(elemTypeName, elemValue);
		}
		
	}
*/
	
	protected class ContextTagBuilder extends RecursiveXmlTagBuilder{
		public ContextTagBuilder(TargetContext context) {
			super(context);
		}
		public void appendTag() {
//			int level = context.getLevel();
			String tagName = context.getName();
			
			appendNonSimpleTag(tagName);
		}
		
	}
	
	protected class ComplexFieldTagBuilder extends RecursiveXmlTagBuilder{
		
		public ComplexFieldTagBuilder(FieldContext context) {
			super(context);
		}
		
		public void appendTag() {
			FieldContext fieldCtx = (FieldContext)context;
			Object fieldValue = fieldCtx.getValue();
			String fieldTag = getFieldXmlTag();
			
			appendNonSimpleValue(fieldTag, fieldValue);
			
		}

	}

	protected abstract class IndexedFieldTagBuilder extends RecursiveXmlTagBuilder{
		protected Queue<String> xmlTagQue;
		
		public IndexedFieldTagBuilder(IndexedFieldContext context) {
			super(context);
			List<String> xmlStruct = getXmlTagStruct();
			xmlTagQue = new LinkedList<String>(xmlStruct);
		}
		
		protected abstract List<String> getXmlTagStruct();
		
		public void appendTag() {
//			int level = context.getLevel();		
//			String name = context.getName();		
			Object value = context.getValue();
			if(value == null) {
				String fieldTag = getFieldXmlTag();
				appendSimpleValue(fieldTag, value);
			}else {
				buildNextLevel();
			}
		}
		protected void buildNextLevel() {
			if(!xmlTagQue.isEmpty()) {
//				int level = context.getLevel();
//				ArrayFieldContext arrayCtx = (ArrayFieldContext)context;
				
				String tag = xmlTagQue.remove();
//				if(!arrayTagQue.isEmpty())
//					arrayCtx.incrementLevel();
				
				if(tag == CollectionFieldStruct.CLASS_NAME ){
					Object elemValue = context.getValue();
					tag = getTypeName(context, elemValue); 
				}else if(tag == CollectionFieldStruct.FIELD_NAME ) {
//					tag = context.getName();
					tag = getFieldXmlTag();
				}
				
				appendNonSimpleTag(tag);
			}
			else {
				Traverser traverser = context.getTraverser();
				traverser.traverseCurrentComponent();
			}
		}
		
	}
	
	protected class ArrayFieldTagBuilder extends IndexedFieldTagBuilder {
		
		public ArrayFieldTagBuilder(ArrayFieldContext context) {
			super(context);
		}

		protected List<String> getXmlTagStruct() {
			ArrayStruct arrayStruct = collectionStruct.getArrayStruct();
			List<String> xmlStruct = arrayStruct.getArrayStruct();
			return xmlStruct;
		}
	}
	
	protected class ListFieldTagBuilder extends IndexedFieldTagBuilder {
		
		public ListFieldTagBuilder(ListFieldContext context) {
			super(context);
		}

		protected List<String> getXmlTagStruct() {
			ListStruct listStruct = collectionStruct.getListStruct();
			List<String> xmlStruct = listStruct.getListStruct();
			return xmlStruct;
		}
	}
	
	protected class SetFieldTagBuilder extends IndexedFieldTagBuilder {
		
		public SetFieldTagBuilder(SetFieldContext context) {
			super(context);
		}

		protected List<String> getXmlTagStruct() {
			SetStruct listStruct = collectionStruct.getSetStruct();
			List<String> xmlStruct = listStruct.getSetStruct();
			return xmlStruct;
		}
	}
	
	protected class MapFieldTagBuilder extends IndexedFieldTagBuilder {
		
		public MapFieldTagBuilder(MapFieldContext context) {
			super(context);
		}

		protected List<String> getXmlTagStruct() {
			MapStruct mapStruct = collectionStruct.getMapStruct();
			List<String> xmlStruct = mapStruct.getMapStruct();
			return xmlStruct;
		}
	}
	
	
	protected class MapEntryTagBuilder extends RecursiveXmlTagBuilder{
		protected Queue<String> xmlTagQue;
		
		public MapEntryTagBuilder(MapEntryContext context) {
			super(context);
			MapStruct mapStruct = collectionStruct.getMapStruct();
			List<String> xmlStruct = mapStruct.getEntryStruct();
			xmlTagQue = new LinkedList<String>(xmlStruct);
		}
		
		public void appendTag() {
			buildNextLevel();
		}
		
		protected void buildNextLevel() {
			if(!xmlTagQue.isEmpty()) {
				String tag = xmlTagQue.remove();
				tag = mapTagName(tag);
				
				appendNonSimpleTag(tag);
			}
			else {
				Traverser traverser = context.getTraverser();
				traverser.traverseCurrentComponent();
			}
		}

		protected String mapTagName(String tag) {
			if(tag == CollectionFieldStruct.CLASS_NAME ){
				Object elemValue = context.getValue();
				tag = getTypeName(context, elemValue); 
			}else if(tag == CollectionFieldStruct.FIELD_NAME ) {
				TargetContext parentCtx = context.getParentContext();
				tag = parentCtx.getName();
			}
			return tag;
		}
		
	}
	
	protected abstract class IndexedElementSimpleXmlTagBuilder extends AbstractXmlTagBuilder{
//		protected IndexedElementContext context;
		protected Queue<String> elementTagQue ;
		
		public IndexedElementSimpleXmlTagBuilder(IndexedElementContext context) {
			super(context);
			List<String> elementList = getXmlStruct();
			elementTagQue = new LinkedList<String>(elementList);
		}

		protected abstract List<String> getXmlStruct(); 
		
		public void appendTag() {
			if(elementTagQue.isEmpty()) {
				Object elemValue = context.getValue();
				String elemTypeName = getTypeName(context, elemValue); 
				appendSimpleValue(elemTypeName, elemValue);
			}
			else {
				buildTagTree();
			}
		}
		
		protected void buildTagTree() {
			if(!elementTagQue.isEmpty()) {
				
				String tag = elementTagQue.remove();
				if(tag == CollectionFieldStruct.CLASS_NAME ){
					Object elemValue = context.getValue();
					tag = getTypeName(context, elemValue); 
				}else if(tag == CollectionFieldStruct.FIELD_NAME ) {
//					TargetContext parentCtx = context.getParentContext();
//					tag = parentCtx.getName();
					tag = getFieldXmlTag();
				}
				
				if(!elementTagQue.isEmpty()) {

					tagPrinter.printOpenTag(tag);
					buildTagTree();
					tagPrinter.printCloseTag(tag);
				}else {
					Object elemValue = context.getValue();
//					tagPrinter.printSimpleValueTag(tag, elemValue);
					appendSimpleValue(tag, elemValue);
				}
			}
		}
		
		protected String getFieldXmlTag() {
			TargetContext indexedFieldCtx = context.getParentContext();
			String fieldName = indexedFieldCtx.getName();
			Class<?> indexedFieldParentCls = indexedFieldCtx.getParentObjectClass();
			String fieldTag = getFieldTag(indexedFieldParentCls, fieldName);
			return fieldTag;
		}
		
	}
	
	protected  class ArrayElementSimpleTagBuilder extends IndexedElementSimpleXmlTagBuilder{
		
		public ArrayElementSimpleTagBuilder(ArrayElementContext context) {
			super(context);
		}

		protected List<String> getXmlStruct() {
			ArrayStruct arrayXmlStruct = collectionStruct.getArrayStruct();
			List<String> elementXmlList = arrayXmlStruct.getElementStruct();
			return elementXmlList;
		}
	}
	
	protected  class ListElementSimpleTagBuilder extends IndexedElementSimpleXmlTagBuilder{
		
		public ListElementSimpleTagBuilder(ListElementContext context) {
			super(context);
		}

		protected List<String> getXmlStruct() {
			ListStruct listXmlStruct = collectionStruct.getListStruct();
			List<String> elementXmlList = listXmlStruct.getElementStruct();
			return elementXmlList;
		}
	}
	
	protected  class SetElementSimpleTagBuilder extends IndexedElementSimpleXmlTagBuilder{
		
		public SetElementSimpleTagBuilder(SetElementContext context) {
			super(context);
		}

		protected List<String> getXmlStruct() {
			SetStruct setXmlStruct = collectionStruct.getSetStruct();
			List<String> elementXmlList = setXmlStruct.getElementStruct();
			return elementXmlList;
		}
	}
	
	protected  class MapKeySimpleTagBuilder extends IndexedElementSimpleXmlTagBuilder{
		
		public MapKeySimpleTagBuilder(MapKeyContext context) {
			super(context);
		}

		protected List<String> getXmlStruct() {
			MapStruct mapXmlStruct = collectionStruct.getMapStruct();
			List<String> keyXmlList = mapXmlStruct.getKeyStruct();
			return keyXmlList;
		}
	}
	
	protected  class MapValueSimpleTagBuilder extends IndexedElementSimpleXmlTagBuilder{
		
		public MapValueSimpleTagBuilder(MapValueContext context) {
			super(context);
		}

		protected List<String> getXmlStruct() {
			MapStruct mapXmlStruct = collectionStruct.getMapStruct();
			List<String> valueXmlList = mapXmlStruct.getValueStruct();
			return valueXmlList;
		}
	}
	
	protected abstract class IndexedFieldElementComplexTagBuilder extends RecursiveXmlTagBuilder{
		protected Queue<String> elementTagQue;
		
		public IndexedFieldElementComplexTagBuilder(IndexedElementContext context) {
			super(context);
			List<String> elementList = getXmlTagStruct();
			elementTagQue = new LinkedList<String>(elementList);
			
		}

		protected abstract List<String> getXmlTagStruct();
		
		public void appendTag() {
			if(elementTagQue.isEmpty()) {
				Object elemValue = context.getValue();
				String elemTypeName = getTypeName(context, elemValue); 
				appendNonSimpleValue(elemTypeName, elemValue);
			}
			else {
				buildTagTree();
			}
		}
		
		protected void buildTagTree() {
			if(!elementTagQue.isEmpty()) {
				
				String tag = elementTagQue.remove();
				tag = mapTagName(tag);
				
				tagPrinter.printOpenTag(tag);
				buildTagTree();
				tagPrinter.printCloseTag(tag);
			}else {
				buildNextLevel();
			}
		}

		protected String mapTagName(String tag) {
			if(tag == CollectionFieldStruct.CLASS_NAME ){
				Object elemValue = context.getValue();
				tag = getTypeName(context, elemValue); 
			}else if(tag == CollectionFieldStruct.FIELD_NAME ) {
//				TargetContext parentCtx = context.getParentContext();
//				tag = parentCtx.getName();
				tag = getFieldXmlTag();
			}
			return tag;
		}
		
		protected String getFieldXmlTag() {
			TargetContext indexedFieldCtx = context.getParentContext();
			String fieldName = indexedFieldCtx.getName();
			Class<?> indexedFieldParentCls = indexedFieldCtx.getParentObjectClass();
			String fieldTag = getFieldTag(indexedFieldParentCls, fieldName);
			return fieldTag;
		}
	}
	
	protected  class ArrayFieldElementComplexTagBuilder extends IndexedFieldElementComplexTagBuilder {
		
		public ArrayFieldElementComplexTagBuilder(ArrayElementContext context) {
			super(context);
		}

		protected List<String> getXmlTagStruct() {
			ArrayStruct arrayStruct = collectionStruct.getArrayStruct();
			List<String> elementXmlList = arrayStruct.getElementStruct();
			return elementXmlList;
		}
	}
	
	protected  class ListFieldElementComplexTagBuilder extends IndexedFieldElementComplexTagBuilder {
		
		public ListFieldElementComplexTagBuilder(ListElementContext context) {
			super(context);
		}

		protected List<String> getXmlTagStruct() {
			ListStruct listStruct = collectionStruct.getListStruct();
			List<String> elementXmlList = listStruct.getElementStruct();
			return elementXmlList;
		}
	}
	
	protected  class SetFieldElementComplexTagBuilder extends IndexedFieldElementComplexTagBuilder {
		
		public SetFieldElementComplexTagBuilder(SetElementContext context) {
			super(context);
		}

		protected List<String> getXmlTagStruct() {
			SetStruct setStruct = collectionStruct.getSetStruct();
			List<String> elementXmlList = setStruct.getElementStruct();
			return elementXmlList;
		}
	}
	
	protected  class MapKeyComplexTagBuilder extends IndexedFieldElementComplexTagBuilder {
		
		public MapKeyComplexTagBuilder(MapKeyContext context) {
			super(context);
		}

		protected List<String> getXmlTagStruct() {
			MapStruct mapStruct = collectionStruct.getMapStruct();
			List<String> keyXmlList = mapStruct.getKeyStruct();
			return keyXmlList;
		}
		
		protected String mapTagName(String tag) {
			if(tag == CollectionFieldStruct.CLASS_NAME ){
				Object elemValue = context.getValue();
				tag = getTypeName(context, elemValue); 
			}
			return tag;
		}
	}
	
	protected  class MapValueComplexTagBuilder extends IndexedFieldElementComplexTagBuilder {
		
		public MapValueComplexTagBuilder(MapValueContext context) {
			super(context);
		}

		protected List<String> getXmlTagStruct() {
			MapStruct mapStruct = collectionStruct.getMapStruct();
			List<String> valueXmlList = mapStruct.getValueStruct();
			return valueXmlList;
		}
		
		protected String mapTagName(String tag) {
			if(tag == CollectionFieldStruct.CLASS_NAME ){
				Object elemValue = context.getValue();
				tag = getTypeName(context, elemValue); 
			}
			return tag;
		}
	}
		
	public void setClassNamePrintType(int classNamePrintType) {
		this.classNamePrintType = classNamePrintType;
	}
	
	public void setCollectionStruct(CollectionFieldStruct collectionStruct) {
		this.collectionStruct = collectionStruct;
	}	
	

}
