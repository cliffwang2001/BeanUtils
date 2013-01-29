package com.woolgrass.beanUtils;

import java.math.BigDecimal;

public class Shipper {

	protected String name;
	protected BigDecimal perOunceRate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPerOunceRate() {
		return perOunceRate;
	}
	public void setPerOunceRate(BigDecimal perOunceRate) {
		this.perOunceRate = perOunceRate;
	}
	
	
}
