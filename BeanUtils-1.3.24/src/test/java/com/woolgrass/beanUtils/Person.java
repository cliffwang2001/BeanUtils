package com.woolgrass.beanUtils;

import java.util.HashMap;
import java.util.Map;

public class Person {
	protected String firstName;
	protected String lastName;
	protected int  age;
	protected Map<String, Address> addressMap;
	protected Person[] children;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Map<String, Address> getAddressMap() {
		return addressMap;
	}
	public void setAddressMap(Map<String, Address> addressMap) {
		this.addressMap = addressMap;
	}
	public Person[] getChildren() {
		return children;
	}
	public void setChildren(Person[] children) {
		this.children = children;
	}

}
