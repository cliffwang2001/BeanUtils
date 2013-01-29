package com.woolgrass.beanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LineItemCreator {
	public static List<LineItem> createLineItemList() {
		List<LineItem> itemList = new ArrayList<LineItem>();
		LineItem item = new LineItem("Burnham's Celestial Handbook, Vol 1", 5, new BigDecimal("21.79"), 2);
		itemList.add(item);
		item = new LineItem("Burnham's Celestial Handbook, Vol 2", 5, new BigDecimal("19.89"), 4);
		itemList.add(item);
		return itemList;
	}
}
