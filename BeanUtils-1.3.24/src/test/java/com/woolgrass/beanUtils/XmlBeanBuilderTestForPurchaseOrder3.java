package com.woolgrass.beanUtils;

import com.woolgrass.beanUtils.CollectionFieldStruct.ArrayStruct;
import com.woolgrass.beanUtils.build.XmlBeanBuilder;
import com.woolgrass.beanUtils.traverse.XMLGenerator;

public class XmlBeanBuilderTestForPurchaseOrder3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CollectionFieldStruct collectionStruct = new CollectionFieldStruct();
			collectionStruct.setArrayStruct(ArrayStruct.NO_GROUPING_TAG);
			
			XmlBeanBuilder beanBuilder = XmlBeanBuilder.getBeanBuilder(PurchaseOrder.class, collectionStruct);
			PurchaseOrder purOrder = (PurchaseOrder)beanBuilder.getBean("src/test/resources/purchaseOrder3.xml");
			
			XMLGenerator generator = new XMLGenerator();
			String xml = generator.getXml(purOrder);
			System.out.println(xml);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
