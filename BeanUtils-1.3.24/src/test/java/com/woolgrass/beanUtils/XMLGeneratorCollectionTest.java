package com.woolgrass.beanUtils;

import java.util.List;

import com.woolgrass.beanUtils.traverse.XMLGenerator;

public class XMLGeneratorCollectionTest {
	
	public static void main(String[] args) {
		List<LineItem> itemList = LineItemCreator.createLineItemList();
		
		XMLGenerator generator = new XMLGenerator();
		String xml = generator.getXml("purchaseOrder", itemList, "lineItem");
		System.out.println(xml);
	}
	
}
