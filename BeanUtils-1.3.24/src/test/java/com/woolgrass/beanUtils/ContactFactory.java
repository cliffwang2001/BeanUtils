package com.woolgrass.beanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.woolgrass.beanUtils.Contact.Address;
import com.woolgrass.beanUtils.Contact.Name;

public class ContactFactory {

	public static Contact getSimpleContact() {
		Contact contact = new Contact();
		contact.setEmail("abc@hotmail.com");
		contact.setAge(50);
		
		Calendar calender = new GregorianCalendar(1980, Calendar.DECEMBER, 25);
		Date birthdate = calender.getTime();
		contact.setBirthdate(birthdate);
		Name name = new Name();
		name.setFirstName("Bruce");
		name.setLastName("Li");
		contact.setName(name);
		contact.setDescription("<b>Thi is an example.</b>");
		return contact;
		
	}
	
	public static Contact getContactWithArrayProperty() {
		Contact contact = new Contact();
		contact.setEmail("abc@hotmail.com");
		contact.setAge(50);
		Name name = new Name();
		name.setFirstName("Bruce");
		name.setLastName("Smith");
		contact.setName(name);
		
		Address addr = new Address("200 Queen Street", "Kitchener", "ON", "12345");
		contact.setAddress(addr);
		
		String[] phones = {"519-999-8834", "226-123-5734 & 5735"};
		contact.setPhones(phones);
		
		Person[] children = new Person[2];
		Person child = new Person();
		child.setFirstName("Mike");
		child.setLastName("Smith");
		child.setAge(3);
		children[0] = child;
		
		child = new Person();
		child.setFirstName("Sarah");
		child.setLastName("Smith");
		child.setAge(1);
		children[1] = child;
		
		contact.setChildren(children);
		
		List notes = new ArrayList();
		notes.add("Mike is Bruce's son");
		notes.add(59);
		contact.setNotes(notes);
		
		List<Friend> friends = new ArrayList<Friend>();
		Friend friend = new Friend("Mike", "male", 4);
		friends.add(friend);
		friend = new Friend("Chathrine", "female", 5);
		friends.add(friend);
		contact.setFriends(friends);
		
		Set<Integer> yearsOfUnemployed =  new HashSet<Integer>();
		yearsOfUnemployed.addAll(Arrays.asList(1998, 1999, 2001, 2007));
		contact.setYearsOfUnemployed(yearsOfUnemployed);
		
		Map<String, Address> addressMap = new HashMap<String, Address>();
		Address address = new Address("100 King Street", "Waterloo", "ON", "10036");
		addressMap.put("home", address);
		address = new Address("299 Wonderland Road", "London", "ON", "10059");
		addressMap.put("work", address);
		contact.setAddressMap(addressMap);
		
		return contact;
		
		
		
	}
}
