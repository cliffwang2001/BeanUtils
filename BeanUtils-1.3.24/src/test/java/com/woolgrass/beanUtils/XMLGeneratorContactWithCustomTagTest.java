package com.woolgrass.beanUtils;

import com.woolgrass.beanUtils.Contact.Name;
import com.woolgrass.beanUtils.traverse.XMLGenerator;


public class XMLGeneratorContactWithCustomTagTest {
	
	
	public static void main(String[] args) {
		Contact contact = ContactFactory.getSimpleContact();
		
//		contact = null;
		XMLGenerator generator = new XMLGenerator();
		generator.addClassTagMapping(contact.getClass(), "contact");
		generator.addFieldTagMapping(Name.class, "firstName", "first_name");
		generator.addFieldTagMapping(Name.class, "lastName", "last_name");
		String xml = generator.getXml(contact);
		System.out.println(xml);
		
//		generator.setShowNullElement(true); 
//		xml = generator.getXml();
//		System.out.println(xml);
	}
}
