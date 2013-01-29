package com.woolgrass.beanUtils;

import java.math.BigDecimal;

public class LineItem {
	protected String description;
	protected int perUnitOunces;
	protected BigDecimal price;
	protected int quantity;
	
	
	
	public LineItem() {
	}

	public LineItem(String description, int perUnitOunces, BigDecimal price,
			int quantity) {
		this.description = description;
		this.perUnitOunces = perUnitOunces;
		this.price = price;
		this.quantity = quantity;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPerUnitOunces() {
		return perUnitOunces;
	}
	public void setPerUnitOunces(int perUnitOunces) {
		this.perUnitOunces = perUnitOunces;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
