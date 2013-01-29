package com.woolgrass.beanUtils;


import com.woolgrass.beanUtils.CollectionFieldStruct.SetStruct;
import com.woolgrass.beanUtils.build.XmlBeanBuilder;
import com.woolgrass.beanUtils.traverse.XMLGenerator;

public class XmlBeanBuilderTestForPurchaseOrder3WithSet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CollectionFieldStruct collectionStruct = new CollectionFieldStruct();
			SetStruct setStruct = SetStruct.NO_GROUPING_TAG;
			collectionStruct.setSetStruct(setStruct);
			
			XmlBeanBuilder beanBuilder = XmlBeanBuilder.getBeanBuilder(PurchaseOrderWithSet.class, collectionStruct);
			PurchaseOrderWithSet purOrder = (PurchaseOrderWithSet) beanBuilder.getBean("src/test/resources/purchaseOrder3.xml");
			
			XMLGenerator generator = new XMLGenerator();
			String xml = generator.getXml(purOrder);
			System.out.println(xml);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
