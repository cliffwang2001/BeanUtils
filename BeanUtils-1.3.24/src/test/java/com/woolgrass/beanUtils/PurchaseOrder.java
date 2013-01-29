package com.woolgrass.beanUtils;

public class PurchaseOrder {

	protected Customer customer;
	protected LineItem[] lineItem;
	protected Shipper shipper;
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public LineItem[] getLineItem() {
		return lineItem;
	}
	public void setLineItem(LineItem[] lineItem) {
		this.lineItem = lineItem;
	}
	public Shipper getShipper() {
		return shipper;
	}
	public void setShipper(Shipper shipper) {
		this.shipper = shipper;
	}
	
	
}
