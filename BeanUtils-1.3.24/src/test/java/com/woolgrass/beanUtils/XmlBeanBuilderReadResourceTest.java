package com.woolgrass.beanUtils;

import java.io.InputStream;
import java.util.Arrays;

import com.woolgrass.beanUtils.CollectionFieldStruct.MapStruct;
import com.woolgrass.beanUtils.Contact.Name;
import com.woolgrass.beanUtils.build.XmlBeanBuilder;
import com.woolgrass.beanUtils.traverse.XMLGenerator;

public class XmlBeanBuilderReadResourceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			InputStream xmlStream = XmlBeanBuilderReadResourceTest.class.getResourceAsStream("/contact1.xml");
			
			XmlBeanBuilder beanBuilder = XmlBeanBuilder.getBeanBuilder(Contact.class);
			beanBuilder.addFieldTagMapping(Name.class, "firstName", "first_name");
			beanBuilder.addFieldTagMapping(Name.class, "lastName", "last_name");
			beanBuilder.addFieldConverter(Contact.class, "weddingdate", new ShortDateConverter());
			
			CollectionFieldStruct collectionStruct = new CollectionFieldStruct();
			MapStruct mapStruct = new MapStruct();
			mapStruct.setMapStruct(Arrays.asList(CollectionFieldStruct.FIELD_NAME));
			mapStruct.setEntryStruct(Arrays.asList(CollectionFieldStruct.MAPENTRY));
			mapStruct.setKeyStruct(Arrays.asList(CollectionFieldStruct.MAPKEY, CollectionFieldStruct.CLASS_NAME));
			mapStruct.setValueStruct(Arrays.asList(CollectionFieldStruct.MAPVALUE, CollectionFieldStruct.CLASS_NAME));
			collectionStruct.setMapStruct(mapStruct);
			
			beanBuilder.setCollectionStruct(collectionStruct);
			Contact contact = (Contact)beanBuilder.getBean(xmlStream);
			
			XMLGenerator generator = new XMLGenerator();
			generator.addClassTagMapping(Friend.class, "myfriend");
			String xml = generator.getXml(contact);
			System.out.println(xml);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
