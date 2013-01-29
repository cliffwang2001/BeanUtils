package com.woolgrass.beanUtils;

import java.text.ParseException;

public interface TypeConverter {
	public Object parse(String stringValue) throws ParseException;
	public String print(Object typeValue);
}
