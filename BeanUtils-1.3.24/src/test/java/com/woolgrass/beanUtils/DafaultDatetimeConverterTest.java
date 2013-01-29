package com.woolgrass.beanUtils;

import java.util.Date;

public class DafaultDatetimeConverterTest {

	public static void main(String[] args) throws Exception {
		DefaultDatetimeConverter datetimeConverter = new DefaultDatetimeConverter();
		Date date = (Date)datetimeConverter.parse("1990-10-11T21:45:30");
		String strDate = datetimeConverter.print(date);
		System.out.println(strDate);
	}
}
