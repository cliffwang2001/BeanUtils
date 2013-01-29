package com.woolgrass.beanUtils;


import com.woolgrass.beanUtils.CollectionFieldStruct.ListStruct;
import com.woolgrass.beanUtils.build.XmlBeanBuilder;
import com.woolgrass.beanUtils.traverse.XMLGenerator;

public class XmlBeanBuilderTestForPurchaseOrder3WithList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CollectionFieldStruct collectionStruct = new CollectionFieldStruct();
			ListStruct listStruct = ListStruct.NO_GROUPING_TAG;
			collectionStruct.setListStruct(listStruct);
			
			XmlBeanBuilder beanBuilder = XmlBeanBuilder.getBeanBuilder(PurchaseOrderWithList.class, collectionStruct);
			PurchaseOrderWithList purOrder = (PurchaseOrderWithList)beanBuilder.getBean("src/test/resources/purchaseOrder3.xml");
			
			XMLGenerator generator = new XMLGenerator();
			String xml = generator.getXml(purOrder);
			System.out.println(xml);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
