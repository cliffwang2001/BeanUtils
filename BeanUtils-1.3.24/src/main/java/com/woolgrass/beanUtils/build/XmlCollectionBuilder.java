package com.woolgrass.beanUtils.build;

import java.io.IOException;
import java.util.List;
import java.util.Stack;


import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.woolgrass.beanUtils.CollectionFieldStruct;
import com.woolgrass.beanUtils.ConverterManager;
import com.woolgrass.beanUtils.TypeConverter;
import com.woolgrass.beanUtils.traverse.xmlTagMapping.ClassTagMapping;
import com.woolgrass.beanUtils.traverse.xmlTagMapping.ClassTagMappingManager;


public class XmlCollectionBuilder extends DefaultHandler {

	protected Class<?> beanClass;
	protected int level;
	protected BeanCollectionBuilder collectionBuilder;
	protected String beanRootElement;
	protected Stack<StringBuilder> characterDataStack = new Stack<StringBuilder>();
	protected CollectionFieldStruct collectionStruct;
	protected ClassTagMappingManager tagMappingManager = new ClassTagMappingManager();
	protected ConverterManager converterManager =  new ConverterManager();
	
	
	public XmlCollectionBuilder(Class<?> beanClass, String beanRootElement) {
		this.beanClass = beanClass;
		this.beanRootElement = beanRootElement;
		this.collectionStruct = new CollectionFieldStruct();
	}
	
	public XmlCollectionBuilder(Class<?> beanClass, String beanRootElement, CollectionFieldStruct collectionStruct) {
		this.beanClass = beanClass;
		this.beanRootElement = beanRootElement;
		this.collectionStruct = collectionStruct;
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
	
	public void addTypeConverter(Class<?> type, TypeConverter converter) {
		converterManager.addTypeConverter(type, converter);
	}
	
	public void addFieldConverter(Class<?> clazz, String fieldName, TypeConverter converter) {
		converterManager.addFieldConverter(clazz, fieldName, converter);
	}


	public void startDocument () {
		collectionBuilder = new BeanCollectionBuilder(beanClass, beanRootElement, 
				collectionStruct, tagMappingManager, converterManager);
	}
	
	public void startElement (String uri, String localName,
		      String qName, Attributes atts) throws SAXException {
		characterDataStack.push(new StringBuilder());
		level++;
		

		collectionBuilder.startNextElement(localName);
	}
	
	public void endElement (String uri, String localName, String qName)	throws SAXException {
		StringBuilder curSb = characterDataStack.peek();
		String strValue = curSb.toString();
		collectionBuilder.endCurrentElement(localName, strValue);
		level--;
		characterDataStack.pop();
    }
	
	public void characters (char ch[], int start, int length) throws SAXException {
		String strValue = new String(ch, start, length);
		StringBuilder curSb = characterDataStack.peek();
		curSb.append(strValue);
		
		
    }
	
	public void endDocument () throws SAXException {
    }
	
//	public List getList() {
//		return collectionBuilder.getList();
//	}
	
	public  List getList(String xmlUri) throws IOException, SAXException {
		// Create instances needed for parsing
		XMLReader reader = XMLReaderFactory.createXMLReader();
	
		// Turn on validation
//		String featureURI = "http://xml.org/sax/features/validation";
//		reader.setFeature(featureURI, true);
		
		// Turn on schema validation, as well
//		featureURI = "http://apache.org/xml/features/validation/schema";
//		reader.setFeature(featureURI, true);
		
		// Register content handler
		reader.setContentHandler(this);
		
		
		// Parse
		InputSource inputSource = new InputSource(xmlUri);
		reader.parse(inputSource);
		
		return collectionBuilder.getList();
	}
	
	
//	public static Object getList(String xmlUri, Class<?> beanClass, String beanRootElement) throws IOException,	SAXException {
//		XmlCollectionBuilder xmlCollectionBuilder = new XmlCollectionBuilder(beanClass, beanRootElement);
//		return getList(xmlUri, xmlCollectionBuilder);
//	}
//	
//	public static Object getList(String xmlUri, Class<?> beanClass, String beanRootElement,
//			CollectionFieldStruct collectionStruct) throws IOException,	SAXException {
//		XmlCollectionBuilder xmlCollectionBuilder = new XmlCollectionBuilder(beanClass, beanRootElement, collectionStruct);
//		return getList(xmlUri, xmlCollectionBuilder);
//	}
}
