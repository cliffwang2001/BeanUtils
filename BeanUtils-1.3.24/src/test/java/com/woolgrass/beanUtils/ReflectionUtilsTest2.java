package com.woolgrass.beanUtils;

import com.woolgrass.beanUtils.ReflectionUtils;
import com.woolgrass.beanUtils.property.PropertyUtils;

public class ReflectionUtilsTest2 {
	public static void main(String[] args) {
		Company company = CompanyCreator.createCompany();
		
		Object value = PropertyUtils.getPropertyValue(company, "boss.age", true);
		System.out.println(value);
		System.out.println(value.getClass());
		ReflectionUtils.setValue(company, "boss", true);
	}
}
