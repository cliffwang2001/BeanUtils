package com.woolgrass.beanUtils;

import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

import com.woolgrass.beanUtils.build.XmlCollectionBuilder;
import com.woolgrass.beanUtils.traverse.XMLGenerator;

public class XmlCollectionBuilderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			XmlCollectionBuilder collectionBuilder = new XmlCollectionBuilder(LineItem.class, "lineItem");
			List<LineItem> items = (List<LineItem>)collectionBuilder.getList("src/test/resources/purchaseOrder2.xml");
			
			for(LineItem item : items) {
//				String xml = XMLGenerator.getXml(item);
				XMLGenerator generator = new XMLGenerator();
				String xml = generator.getXml(item);
				System.out.println(xml);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
