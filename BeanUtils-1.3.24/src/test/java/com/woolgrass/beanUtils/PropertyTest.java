package com.woolgrass.beanUtils;

import java.util.HashMap;
import java.util.Map;

import com.woolgrass.beanUtils.property.PropertyUtils;


public class PropertyTest {

	public static void main(String[] args) {
		
		Company company = new Company();
		company.setCompanyName("RIM");
		Person boss = new Person();
		boss.setFirstName("John");
		boss.setLastName("Steel");
		boss.setAge(40);
		Map<String, Address> addressMap = new HashMap<String, Address>();
		Address address = new Address();
		address.setCity("Toronto");
		address.setProvince("ON");
		address.setStreet("250 Wellington Street");
		addressMap.put("mailingAddress", address);
		addressMap.put("shippingAddress", null);
		
		addressMap.put("2", address);
		boss.setAddressMap(addressMap);
		
		Person[] children = new Person[2];
		Person child = new Person();
		child.setFirstName("Jack");
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
		
		Object value = PropertyUtils.getPropertyValue(company, "boss", true);
		System.out.println(((Person)value).getFirstName());
		
		value = PropertyUtils.getPropertyValue(company, "boss.age", true);
		System.out.println(value);
		
		PropertyUtils.setPropertyValue(company, "boss.age", 30);
		
		value = PropertyUtils.getPropertyValue(company, "boss.children[1].firstName", true);
		System.out.println(value);
		
		value = PropertyUtils.getPropertyValue(company, "boss.children(1).firstName", true);
		System.out.println(value);
		
		value = PropertyUtils.getPropertyValue(company, "boss.addressMap(mailingAddress).street", true);
		System.out.println(value);
		
		value = PropertyUtils.getPropertyValue(company, "boss.addressMap[mailingAddress].street", true);
		System.out.println(value);
		
		value = PropertyUtils.getPropertyValue(company, "boss.addressMap[mailingAddress].street", true);
		System.out.println(value);
		
//		value = ObjectUtils.getPropertyValue(company, "boss.addressMap(2).street", true);
//		System.out.println(value);
//		
//		value = ObjectUtils.getPropertyValue(company, "boss.addressMap(shippingAddress).street", false);
//		System.out.println(value);
		
		PropertyUtils.setPropertyValue(company, "boss.firstName", "Helen");
		System.out.println(company.getBoss().getFirstName());
		
		PropertyUtils.setPropertyValue(company, "boss.children[1].age", 15);
		
		PropertyUtils.setPropertyValue(company, "boss.addressMap(33)", address);
		PropertyUtils.setPropertyValue(company, "boss.addressMap[33]", address);

	}
}
