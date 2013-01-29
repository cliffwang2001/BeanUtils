package com.woolgrass.beanUtils;

import java.util.Arrays;

import com.woolgrass.beanUtils.CollectionFieldStruct.ArrayStruct;
import com.woolgrass.beanUtils.CollectionFieldStruct.ListStruct;
import com.woolgrass.beanUtils.CollectionFieldStruct.MapStruct;
import com.woolgrass.beanUtils.CollectionFieldStruct.SetStruct;
import com.woolgrass.beanUtils.Contact.Name;
import com.woolgrass.beanUtils.traverse.XMLGenerator;


public class XMLGeneratorContactWithArrayPropertyTest {
	
	
	public static void main(String[] args) {
		Contact contact = ContactFactory.getContactWithArrayProperty();
		
		XMLGenerator generator = new XMLGenerator();
		String xml = generator.getXml(contact);
		System.out.println(xml);
		
		generator.setShowNullElement(true);
		generator.setClassNamePrintType(XMLGenerator.PRINT_SHORT_CLASSNAME);
		generator.addClassTagMapping(contact.getClass(), "contact");
		generator.addClassTagMapping(Person.class, "Person");
		generator.addFieldTagMapping(Contact.class, "friends", "myFriends");
		xml = generator.getXml(contact);
		System.out.println(xml);
		
		generator.setCollectionStruct(CollectionFieldStruct.NO_GROUPING_TAG);
		xml = generator.getXml(contact);
		System.out.println(xml);
		
		
		ArrayStruct  arrayStruct = new ArrayStruct();
		arrayStruct.setArrayStruct(Arrays.asList(CollectionFieldStruct.FIELD_NAME, "Array"));
		arrayStruct.setElementStruct(Arrays.asList("Element", CollectionFieldStruct.CLASS_NAME));
		
		ListStruct  listStruct = new ListStruct();
		listStruct.setListStruct(Arrays.asList(CollectionFieldStruct.FIELD_NAME, "List"));
		listStruct.setElementStruct(Arrays.asList("Element", CollectionFieldStruct.CLASS_NAME));
		
		SetStruct  setStruct = new SetStruct();
		setStruct.setSetStruct(Arrays.asList(CollectionFieldStruct.FIELD_NAME, "Set"));
		setStruct.setElementStruct(Arrays.asList("Element", CollectionFieldStruct.CLASS_NAME));
		
		MapStruct mapStruct = new MapStruct();
		mapStruct.setMapStruct(Arrays.asList(CollectionFieldStruct.FIELD_NAME, "Map"));
		mapStruct.setEntryStruct(Arrays.asList("Entry"));
		mapStruct.setKeyStruct(Arrays.asList("Key", CollectionFieldStruct.CLASS_NAME));
		mapStruct.setValueStruct(Arrays.asList("Value",CollectionFieldStruct.CLASS_NAME));
		
		CollectionFieldStruct collectionStruct = new CollectionFieldStruct(arrayStruct, 
				listStruct, setStruct, mapStruct);
		generator.setCollectionStruct(collectionStruct);
		xml = generator.getXml(contact);
		System.out.println(xml);
		
		mapStruct.setKeyStruct(Arrays.asList("Key"));
		mapStruct.setValueStruct(Arrays.asList("Value"));
		xml = generator.getXml(contact);
		System.out.println(xml);
	}
}
