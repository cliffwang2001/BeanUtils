package com.woolgrass.beanUtils;

import com.woolgrass.beanUtils.traverse.XMLGenerator;


public class XMLGeneratorTest {
	
	
	public static void main(String[] args) {
		Company company = CompanyCreator.createCompany();
		
		XMLGenerator generator = new XMLGenerator();
		String xml = generator.getXml(company);
		System.out.println(xml);
	}
}
