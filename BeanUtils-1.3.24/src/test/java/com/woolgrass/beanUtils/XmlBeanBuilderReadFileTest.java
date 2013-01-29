package com.woolgrass.beanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.woolgrass.beanUtils.CollectionFieldStruct.MapStruct;
import com.woolgrass.beanUtils.Contact.Name;
import com.woolgrass.beanUtils.build.XmlBeanBuilder;
import com.woolgrass.beanUtils.traverse.XMLGenerator;

public class XmlBeanBuilderReadFileTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			SaxXmlBeanBuilder beanBuilder = SaxXmlBeanBuilder.getBeanBuilder("src/test/resources/contact1.xml", Contact.class);
//			Contact contact = (Contact)beanBuilder.getBean();
//			
//			XMLGenerator generator = new XMLGenerator(contact);
//			String xml = generator.getXml();
//			System.out.println(xml);
			
			CollectionFieldStruct collectionStruct = new CollectionFieldStruct();
			MapStruct mapStruct = new MapStruct();
			
			mapStruct.setEntryStruct(new ArrayList<String>());
			List keys = Arrays.asList("key", CollectionFieldStruct.CLASS_NAME);
			List values = Arrays.asList("value", CollectionFieldStruct.CLASS_NAME);
			mapStruct.setMapStruct(Arrays.asList(CollectionFieldStruct.FIELD_NAME));
			mapStruct.setEntryStruct(Arrays.asList("entry"));
			mapStruct.setKeyStruct(keys);
			mapStruct.setValueStruct(values);
			collectionStruct.setMapStruct(mapStruct);
			XmlBeanBuilder beanBuilder2 = XmlBeanBuilder.getBeanBuilder(Contact.class,
					"contact", collectionStruct);
			beanBuilder2.addFieldTagMapping(Name.class, "firstName", "first_name");
			beanBuilder2.addFieldTagMapping(Name.class, "lastName", "last_name");
			beanBuilder2.addFieldConverter(Contact.class, "weddingdate", new ShortDateConverter());
			beanBuilder2.addFieldConverter(Contact.class, "appointmentdate", new ShortDateConverter());
			
			Contact contact2 = (Contact)beanBuilder2.getBean("src/test/resources/contact1.xml");
			XMLGenerator generator2 = new XMLGenerator();
			generator2.addFieldConverter(Contact.class, "appointmentdate", new ShortDateConverter());
			String xml2 = generator2.getXml(contact2);
			System.out.println(xml2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
