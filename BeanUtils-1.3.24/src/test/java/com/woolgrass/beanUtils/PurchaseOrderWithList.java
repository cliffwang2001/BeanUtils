package com.woolgrass.beanUtils;

import java.util.List;

public class PurchaseOrderWithList {

	protected Customer customer;
	protected List<LineItem> lineItem;
	protected Shipper shipper;
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public List<LineItem> getLineItem() {
		return lineItem;
	}
	public void setLineItem(List<LineItem> lineItem) {
		this.lineItem = lineItem;
	}
	public Shipper getShipper() {
		return shipper;
	}
	public void setShipper(Shipper shipper) {
		this.shipper = shipper;
	}
	
	
}
