package com.woolgrass.beanUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CompanyCreator {

	public static Company createCompany() {
		Company company = new Company();
		company.setCompanyName("RIM");
		Person boss = new Person();
		boss.setFirstName("Matti");
		boss.setLastName("Karttunen");
		boss.setAge(40);
		Map<String, Address> addressMap = new HashMap<String, Address>();
		Address address = new Address();
		address.setCity("Toronto");
		address.setProvince("ON");
		address.setStreet("100 King Street");
		addressMap.put("mailingAddress", address);
		addressMap.put("shippingAddress", null);
		
		addressMap.put("2", address);
		boss.setAddressMap(addressMap);
		
		Person[] children = new Person[2];
		Person child = new Person();
		child.setFirstName("Jack");
		child.setLastName("Karttunen");
		child.setAge(3);
		children[0] = child;
		
		child = new Person();
		child.setFirstName("Mary");
		child.setLastName("Karttunen");
		child.setAge(1);
		children[1] = child;
		
		boss.setChildren(children);
		
		company.setBoss(boss);
		
		Set<Employee> employees = new HashSet<Employee>();
		Employee employee = new Employee();
		employee.setFirstName("Isabel");
		employee.setLastName("Karttunen");
		employee.setAge(40);
		employee.setBoss(boss);
		employee.setCompany(company);
		
		employees.add(employee);
		
		employee = new Employee();
		employee.setFirstName("Liz");
		employee.setLastName("Nixon");
		employee.setAge(50);
		employee.setBoss(boss);
		employees.add(employee);
		
		company.setEmployees(employees);
		
		return company;
	}
	
	public static Company createNonCrossReferenceCompany() {
		Company company = new Company();
		company.setCompanyName("The Big Cheese");
		Person boss = new Person();
		boss.setFirstName("John");
		boss.setLastName("Steel");
		boss.setAge(40);
		Map<String, Address> addressMap = new HashMap<String, Address>();
		Address address = new Address();
		address.setCity("Toronto");
		address.setProvince("ON");
		address.setStreet("200 Wellington Street");
		addressMap.put("mailingAddress", address);
		addressMap.put("shippingAddress", null);
		
//		addressMap.put("2", address);
//		boss.setAddressMap(addressMap);
		
		Person[] children = new Person[2];
		Person child = new Person();
		child.setFirstName("Evan");
		child.setLastName("Steel");
		child.setAge(3);
		children[0] = child;
		
		child = new Person();
		child.setFirstName("Sarah");
		child.setLastName("Steel");
		child.setAge(1);
		children[1] = child;
		
		boss.setChildren(children);
		
		company.setBoss(boss);
		
		Set<Employee> employees = new HashSet<Employee>();
		Employee employee = new Employee();
		employee.setFirstName("Cathy");
		employee.setLastName("Steel");
		employee.setAge(40);
		//employee.setBoss(boss);
//		employee.setCompany(company);
		
		employees.add(employee);
		
		employee = new Employee();
		employee.setFirstName("Bill");
		employee.setLastName("Jobs");
		employee.setAge(50);
		//employee.setBoss(boss);
		//employees.add(employee);
		
		company.setEmployees(employees);
		
		return company;
	}
}
