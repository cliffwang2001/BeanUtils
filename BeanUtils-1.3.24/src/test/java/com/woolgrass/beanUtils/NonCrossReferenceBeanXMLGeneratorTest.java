package com.woolgrass.beanUtils;

import com.woolgrass.beanUtils.traverse.XMLGenerator;

public class NonCrossReferenceBeanXMLGeneratorTest {
	public static void main(String[] args) {
//		Company company = CompanyCreator.createNonCrossReferenceCompany();
		Company company = CompanyCreator.createCompany();
		
//		String xml = XMLGenerator.getXml(company, false);
		
		
		XMLGenerator generator = new XMLGenerator(true);
		String xml = generator.getXml(company);
		System.out.println(xml);
		
		XMLGenerator generator2 = new XMLGenerator(false);
		String xml2 = generator2.getXml(company);
		System.out.println(xml2);
	}
}
