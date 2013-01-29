package com.woolgrass.beanUtils;

public class Employee extends Person {
	protected Person boss;
	protected Company company;
	
	public Person getBoss() {
		return boss;
	}

	public void setBoss(Person boss) {
		this.boss = boss;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	
}
