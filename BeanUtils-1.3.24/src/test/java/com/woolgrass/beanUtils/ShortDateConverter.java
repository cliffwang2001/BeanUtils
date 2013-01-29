package com.woolgrass.beanUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShortDateConverter implements TypeConverter {

	DateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public Object parse(String stringValue) throws ParseException {
		return datetimeFormat.parse(stringValue);
	}

	public String print(Object typeValue) {
		if(!(typeValue instanceof Date))
			throw new IllegalStateException("typeValue is not of Date type");
		return datetimeFormat.format(typeValue);
	}

}
