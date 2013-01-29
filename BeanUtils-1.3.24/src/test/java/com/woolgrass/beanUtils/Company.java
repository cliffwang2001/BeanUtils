package com.woolgrass.beanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Company {
	protected List<Department> departments = new ArrayList<Department>();
	protected String companyName;
	protected Person  boss;
	protected Set<Employee> employees;
	
	public List<Department> getDepartments() {
		return departments;
	}
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Person getBoss() {
		return boss;
	}
	public Set<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}
	public void setBoss(Person boss) {
		this.boss = boss;
	}
	
	
}
