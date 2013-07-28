package com.woolgrass.beanUtils.build;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.woolgrass.beanUtils.CollectionFieldStruct;
import com.woolgrass.beanUtils.ConverterManager;
import com.woolgrass.beanUtils.DefaultDatetimeConverter;
import com.woolgrass.beanUtils.TypeConverter;
import com.woolgrass.beanUtils.traverse.xmlTagMapping.ClassTagMapping;
import com.woolgrass.beanUtils.traverse.xmlTagMapping.ClassTagMappingManager;


public class XmlBeanBuilder extends DefaultHandler {

	protected Object targetObject;
	protected Class<?> beanClass;
	protected int level;
	protected BuilderManagerCreator bmCreator;
	protected BuilderManager builderManager;
	protected String rootElement;
	protected Stack<StringBuilder> characterDataStack = new Stack<StringBuilder>();
	protected CollectionFieldStruct collectionStruct = CollectionFieldStruct.FIELDNAME_AS_GROUPING_TAG;
//	protected String xmlUri;
//	protected InputStream xmlByteStream;
	protected ClassTagMappingManager tagMappingManager = new ClassTagMappingManager(); 
	protected ConverterManager converterManager = new ConverterManager();
	
	
	public XmlBeanBuilder(Class<?> compositeOrArrayClass) {
		this.beanClass = compositeOrArrayClass;
		bmCreator =  new BuilderManagerCreator() {
			public BuilderManager createBuilderManager() {
				return new BuilderManager(beanClass, rootElement, collectionStruct, tagMappingManager, converterManager);
			}
		};
		converterManager.addTypeConverter(Date.class, new DefaultDatetimeConverter());
	}
	
	public XmlBeanBuilder(Class<?> compositeOrArrayClass, CollectionFieldStruct collectionStruct) {
		this(compositeOrArrayClass);
		this.collectionStruct = collectionStruct;
		
	}
	
	public XmlBeanBuilder(Class<?> compositeOrArrayClass, String rootElement) {
		this(compositeOrArrayClass);
		this.rootElement = rootElement;
		
	}
	
	public XmlBeanBuilder(Class<?> compositeOrArrayClass, String rootElement, CollectionFieldStruct collectionStruct) {
		this(compositeOrArrayClass);
		this.rootElement = rootElement;
		this.collectionStruct = collectionStruct;
	}
	
	
	public XmlBeanBuilder(Class<?> collectionClass, final Class<?> elementType) {
		this.beanClass = collectionClass;
		bmCreator =  new BuilderManagerCreator() {
			public BuilderManager createBuilderManager() {
				return new BuilderManager(beanClass, elementType, rootElement, collectionStruct, tagMappingManager, converterManager);
			}
		};
		converterManager.addTypeConverter(Date.class, new DefaultDatetimeConverter());
	}
	
	public XmlBeanBuilder(Class<?> collectionClass, final Class<?> elementType, String rootElement) {
		this(collectionClass, elementType);
		this.rootElement = rootElement;
	}
	
	public XmlBeanBuilder(Class<?> collectionClass, final Class<?> elementType, CollectionFieldStruct collectionStruct) {
		this(collectionClass, elementType);
		this.collectionStruct = collectionStruct;
	}
	
	public XmlBeanBuilder(Class<?> collectionClass, final Class<?> elementType, String rootElement, CollectionFieldStruct collectionStruct) {
		this(collectionClass, elementType, collectionStruct);
		this.rootElement = rootElement;
	}
	
	
	public XmlBeanBuilder(Class<?> mapClass, final Class<?> mapKeyType, final Class<?> mapValueType) {
		this.beanClass = mapClass;
		bmCreator =  new BuilderManagerCreator() {
			public BuilderManager createBuilderManager() {
				return new BuilderManager(beanClass, mapKeyType, mapValueType, rootElement, collectionStruct, tagMappingManager, converterManager);
			}
		};
		converterManager.addTypeConverter(Date.class, new DefaultDatetimeConverter());
	}
	
	public XmlBeanBuilder(Class<?> mapClass, final Class<?> mapKeyType, final Class<?> mapValueType, String rootElement) {
		this(mapClass, mapKeyType, mapValueType);
		this.rootElement = rootElement;
	}
	
	public XmlBeanBuilder(Class<?> mapClass, final Class<?> mapKeyType, final Class<?> mapValueType, CollectionFieldStruct collectionStruct) {
		this(mapClass, mapKeyType, mapValueType);
		this.collectionStruct = collectionStruct;
	}
	
	public XmlBeanBuilder(Class<?> mapClass, final Class<?> mapKeyType, final Class<?> mapValueType, String rootElement, CollectionFieldStruct collectionStruct) {
		this(mapClass, mapKeyType, mapValueType, collectionStruct);
		this.rootElement = rootElement;
	}
	
	public void addTypeConverter(Class<?> type, TypeConverter converter) {
		converterManager.addTypeConverter(type, converter);
	}
	
	public void addFieldConverter(Class<?> clazz, String fieldName, TypeConverter converter) {
		converterManager.addFieldConverter(clazz, fieldName, converter);
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public void startDocument () {
//		builderManager = new BuilderManager(beanClass);
		
	}
	
	public void startElement (String uri, String localName,
		      String qName, Attributes atts) throws SAXException {
		characterDataStack.push(new StringBuilder());
		level++;
		if(rootElement == null) {
			rootElement = localName;
		}
		if(builderManager == null) {
//			if(rootElement != localName)
//				throw new IllegalArgumentException("Expecting " + rootElement + ", but get " + localName );
//			if(collectionStruct == null)
//				builderManager = new BuilderManager(beanClass, rootElement);
//			else
//				builderManager = new BuilderManager(beanClass, rootElement, collectionStruct);
			builderManager = bmCreator.createBuilderManager();
		}
		builderManager.startNextElement(localName);
	}
	
	public void endElement (String uri, String localName, String qName)	throws SAXException {
		StringBuilder curSb = characterDataStack.peek();
		String strValue = curSb.toString();
		builderManager.endCurrentElement(localName, strValue);
		level--;
		characterDataStack.pop();
    }
	
	public void characters (char ch[], int start, int length) throws SAXException {
		String strValue = new String(ch, start, length);
		StringBuilder curSb = characterDataStack.peek();
		curSb.append(strValue);
		
		
    }
	
	public void endDocument () throws SAXException {
		targetObject = builderManager.getTargetObject();
    }
	
	protected void resetInternalState() {
		targetObject = null;
		level = 0;
		characterDataStack = new Stack<StringBuilder>();
	}
	
	public Object getBean(String xmlUri) throws IOException, SAXException, ParserConfigurationException {
		if(xmlUri == null) 
			throw new IllegalArgumentException("xmlUri is null");
		
		InputSource inputSource = new InputSource(xmlUri);
		return getBean(inputSource);
	}
	
	public Object getBean(InputStream xmlByteStream) throws IOException, SAXException, ParserConfigurationException {
		if(xmlByteStream == null) 
			throw new IllegalArgumentException("xmlByteStream is null");
		
		InputSource inputSource = new InputSource(xmlByteStream);
		return getBean(inputSource);
	}
	
	public Object getBean(InputSource inputSource) throws IOException, SAXException, ParserConfigurationException {
		resetInternalState();
		// Create instances needed for parsing
		XMLReader reader = getXmlReader();
	
		// Register content handler
		reader.setContentHandler(this);
		
		
		// Parse
		reader.parse(inputSource);
		
		return getTargetObject();
	}

	protected XMLReader getXmlReader() throws SAXException, ParserConfigurationException {
//		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		// Create a JAXP SAXParserFactory and configure it
		SAXParserFactory spf = SAXParserFactory.newInstance();
		
		spf.setNamespaceAware(true);
		
		// Create a JAXP SAXParser
        SAXParser saxParser = spf.newSAXParser();
        
        // Get the encapsulated SAX XMLReader
        XMLReader xmlReader = saxParser.getXMLReader();
		
		return xmlReader;
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
	
	protected interface BuilderManagerCreator {
		BuilderManager createBuilderManager();
	}
	
	public CollectionFieldStruct getCollectionStruct() {
		return collectionStruct;
	}

	public void setCollectionStruct(CollectionFieldStruct collectionStruct) {
		this.collectionStruct = collectionStruct;
	}

//	public static XmlBeanBuilder getBeanBuilder(Class<?> compositeOrArrayClass) {
//		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(compositeOrArrayClass);
//		return xmlBeanBuilder;
//	}
	
	public static XmlBeanBuilder getBeanBuilder(Class<?> compositeOrArrayClass, 
			CollectionFieldStruct collectionStruct) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(compositeOrArrayClass, collectionStruct);
		return xmlBeanBuilder;

	}
	
	public static XmlBeanBuilder getBeanBuilder(Class<?> compositeOrArrayClass, String rootElement) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(compositeOrArrayClass, rootElement);
		return xmlBeanBuilder;
	}
	
	public static XmlBeanBuilder getBeanBuilder(Class<?> compositeOrArrayClass, String rootElement,
			CollectionFieldStruct collectionStruct) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(compositeOrArrayClass, rootElement, collectionStruct);
		return xmlBeanBuilder;
	}
	
	
	public static XmlBeanBuilder getBeanBuilder(String xmlUri, Class<?> collectionClass, Class<?> elementType) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(collectionClass, elementType);
		return xmlBeanBuilder;

	}
	
	public static XmlBeanBuilder getBeanBuilder(Class<?> collectionClass, Class<?> elementType, 
			CollectionFieldStruct collectionStruct) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(collectionClass, elementType, collectionStruct);
		return xmlBeanBuilder;

	}
	
	public static XmlBeanBuilder getBeanBuilder(Class<?> collectionClass, Class<?> elementType, String rootElement) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(collectionClass, elementType, rootElement);
		return xmlBeanBuilder;
	}
	
	public static XmlBeanBuilder getBeanBuilder(Class<?> collectionClass, Class<?> elementType, String rootElement,
			CollectionFieldStruct collectionStruct) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(collectionClass, elementType, rootElement, collectionStruct);
		return xmlBeanBuilder;
	}
	
	
	public static XmlBeanBuilder getBeanBuilder(Class<?> mapClass, Class<?> mapKeyType, Class<?> mapValueType) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(mapClass, mapKeyType, mapValueType);
		return xmlBeanBuilder;

	}
	
	public static XmlBeanBuilder getBeanBuilder(Class<?> mapClass, Class<?> mapKeyType, Class<?> mapValueType, 
			CollectionFieldStruct collectionStruct) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(mapClass, mapKeyType, mapValueType, collectionStruct);
		return xmlBeanBuilder;

	}
	
	public static XmlBeanBuilder getBeanBuilder(Class<?> mapClass, Class<?> mapKeyType, Class<?> mapValueType, String rootElement) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(mapClass, mapKeyType, mapValueType, rootElement);
		return xmlBeanBuilder;
	}
	
	public static XmlBeanBuilder getBeanBuilder(Class<?> mapClass, Class<?> mapKeyType, Class<?> mapValueType, String rootElement,
			CollectionFieldStruct collectionStruct) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(mapClass, mapKeyType, mapValueType, rootElement, collectionStruct);
		return xmlBeanBuilder;
	}
	
	public static XmlBeanBuilder getBeanBuilder(Class<?> compositeOrArrayClass) throws IOException,	SAXException {
		XmlBeanBuilder xmlBeanBuilder = new XmlBeanBuilder(compositeOrArrayClass);
		return xmlBeanBuilder;

	}

	
}
