package com.woolgrass.beanUtils;

import java.util.Date;

import com.woolgrass.beanUtils.Contact.Name;
import com.woolgrass.beanUtils.traverse.XMLGenerator;


public class XMLGeneratorSimpleContactTest {
	
	
	public static void main(String[] args) {
		Contact contact = ContactFactory.getSimpleContact();
		
//		contact = null;
		XMLGenerator generator = new XMLGenerator();
		String xml = generator.getXml(contact);
		System.out.println(xml);
		
		generator.addTypeConverter(Date.class, new ShortDateConverter());
		generator.addFieldConverter(Contact.class, "description", new CDataFormater());
		generator.addFieldConverter(Name.class, "firstName", new CDataFormater());
		generator.setShowNullElement(true);
		generator.setClassNamePrintType(XMLGenerator.PRINT_SHORT_CLASSNAME);
		xml = generator.getXml(contact);
		System.out.println(xml);
	}
}
