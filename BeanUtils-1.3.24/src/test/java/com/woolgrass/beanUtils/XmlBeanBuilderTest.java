package com.woolgrass.beanUtils;

import java.util.Arrays;


import com.woolgrass.beanUtils.CollectionFieldStruct.ListStruct;
import com.woolgrass.beanUtils.CollectionFieldStruct.MapStruct;
import com.woolgrass.beanUtils.build.XmlBeanBuilder;
import com.woolgrass.beanUtils.traverse.XMLGenerator;

public class XmlBeanBuilderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			InputStream xmlStream = SaxXmlBeanBuilderReadResourceTest.class.getResourceAsStream("/contact1.xml");
//			Contact contact = (Contact)SaxXmlBeanBuilder.getBean(xmlStream, Contact.class);
			XmlBeanBuilder beanBuilder = XmlBeanBuilder.getBeanBuilder(Contact.class);
			Contact contact = (Contact)beanBuilder.getBean("src/test/resources/contact3.xml");
			XMLGenerator generator = new XMLGenerator();
			String xml = generator.getXml(contact);
			System.out.println(xml);
			
			
			beanBuilder = XmlBeanBuilder.getBeanBuilder(Contact.class, CollectionFieldStruct.NO_GROUPING_TAG);
			Contact contact2 = (Contact)beanBuilder.getBean("src/test/resources/contact4.xml");
			XMLGenerator generator2 = new XMLGenerator();
			generator2.setCollectionStruct(CollectionFieldStruct.NO_GROUPING_TAG);
			String xml2 = generator2.getXml(contact2);
			System.out.println(xml2);
			
			ListStruct  listStruct = new ListStruct();
			listStruct.setListStruct(Arrays.asList(CollectionFieldStruct.FIELD_NAME, "List"));
			listStruct.setElementStruct(Arrays.asList("Element", CollectionFieldStruct.CLASS_NAME));
			CollectionFieldStruct collectionStruct = new CollectionFieldStruct();
			collectionStruct.setListStruct(listStruct);
			
			MapStruct mapStruct = new MapStruct();
			mapStruct.setMapStruct(Arrays.asList(CollectionFieldStruct.FIELD_NAME));
			mapStruct.setEntryStruct(Arrays.asList(CollectionFieldStruct.MAPENTRY));
			mapStruct.setKeyStruct(Arrays.asList(CollectionFieldStruct.MAPKEY, CollectionFieldStruct.CLASS_NAME));
			mapStruct.setValueStruct(Arrays.asList(CollectionFieldStruct.MAPVALUE, CollectionFieldStruct.CLASS_NAME));
			collectionStruct.setMapStruct(mapStruct);
			
			XmlBeanBuilder beanBuilder3 = XmlBeanBuilder.getBeanBuilder(Contact.class, collectionStruct);
			beanBuilder3.addFieldTagMapping(Contact.class, "friends", "myFriends");
			Contact contact3 = (Contact)beanBuilder3.getBean("src/test/resources/contact5.xml");
			XMLGenerator generator3 = new XMLGenerator();
			generator3.setCollectionStruct(collectionStruct);
			String xmls = generator3.getXml(contact3);
			System.out.println(xmls);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
