package com.woolgrass.beanUtils;

import java.text.MessageFormat;
import java.text.ParseException;

public class CDataFormater implements TypeConverter {

	protected static final String CDATA_FORMAT = "<![CDATA[{0}]]>";
	public Object parse(String stringValue) throws ParseException {
		return stringValue;
	}

	public String print(Object typeValue) {
		return MessageFormat.format(CDATA_FORMAT, typeValue);
	}

}
