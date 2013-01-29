package com.woolgrass.beanUtils;

import java.util.ArrayList;
import java.util.List;

public class Department {
	protected String DepartmentName;
	protected Person director;
	protected List<Person> employees = new ArrayList<Person>();
	
	public String getDepartmentName() {
		return DepartmentName;
	}
	public void setDepartmentName(String departmentName) {
		DepartmentName = departmentName;
	}
	public Person getDirector() {
		return director;
	}
	public void setDirector(Person director) {
		this.director = director;
	}
	public List<Person> getEmployees() {
		return employees;
	}
	public void setEmployees(List<Person> employees) {
		this.employees = employees;
	}

}
