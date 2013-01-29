package com.woolgrass.beanUtils;

import java.util.Set;

public class PurchaseOrderWithSet {

	protected Customer customer;
	protected Set<LineItem> lineItem;
	protected Shipper shipper;
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Set<LineItem> getLineItem() {
		return lineItem;
	}
	public void setLineItem(Set<LineItem> lineItem) {
		this.lineItem = lineItem;
	}
	public Shipper getShipper() {
		return shipper;
	}
	public void setShipper(Shipper shipper) {
		this.shipper = shipper;
	}
	
	
}
