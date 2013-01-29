package com.woolgrass.beanUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Contact {
	protected Name name;
	protected int age;
	protected Date birthdate;
	protected Date weddingdate;
	protected Date appointmentdate;
	protected Address address;
	protected String email;
	protected String[] phones;
	protected Person[] children; 
	protected List<Friend> friends;
	protected Set<Integer> yearsOfUnemployed;
	protected Map<String, Address> addressMap;
	protected List notes;
	protected String description;
	
	public Contact() {
	}

	public Contact(Name name, Address address, String email, String[] phones,
			List<Friend> friends, Set<Integer> yearsOfUnemployed,
			Map<String, Address> addressMap) {
		super();
		this.name = name;
		this.address = address;
		this.email = email;
		this.phones = phones;
		this.friends = friends;
		this.yearsOfUnemployed = yearsOfUnemployed;
		this.addressMap = addressMap;
	}



	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String[] getPhones() {
		return phones;
	}

	public void setPhones(String[] phones) {
		this.phones = phones;
	}



	public Set<Integer> getYearsOfUnemployed() {
		return yearsOfUnemployed;
	}



	public void setYearsOfUnemployed(Set<Integer> yearsOfUnemployed) {
		this.yearsOfUnemployed = yearsOfUnemployed;
	}



	public Map<String, Address> getAddressMap() {
		return addressMap;
	}



	public void setAddressMap(Map<String, Address> addressMap) {
		this.addressMap = addressMap;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Person[] getChildren() {
		return children;
	}

	public void setChildren(Person[] children) {
		this.children = children;
	}

	public List getNotes() {
		return notes;
	}

	public void setNotes(List notes) {
		this.notes = notes;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}




	public Date getWeddingdate() {
		return weddingdate;
	}

	public void setWeddingdate(Date weddingdate) {
		this.weddingdate = weddingdate;
	}




	public Date getAppointmentdate() {
		return appointmentdate;
	}

	public void setAppointmentdate(Date appointmentdate) {
		this.appointmentdate = appointmentdate;
	}




	public static class Name {
		protected String firstName;
		protected String lastName;
		
		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLast_name() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public Name(String firstName, String lastName) {
			super();
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public Name() {
		}
		
		
	}
	
	public static class Address {
		protected String street;
		protected String city;
		protected String state;
		protected String zip;
		
		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getZip() {
			return zip;
		}

		public void setZip(String zip) {
			this.zip = zip;
		}

		public Address(String street, String city, String state, String zip) {
			super();
			this.street = street;
			this.city = city;
			this.state = state;
			this.zip = zip;
		}

		public Address() {
		}
		
		
	}


	
	
}


