package com.woolgrass.beanUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleTypeUtils {

	public static Object getValueFromString(String simpleType, String strValue) {
		Object value = null;
		if(simpleType.equalsIgnoreCase("String")) {
			value = strValue;
		}
		else if(simpleType.equals("Integer") || simpleType.equals("int")) {
			value = Integer.valueOf(strValue);
		}
		else if(simpleType.equals("Long") || simpleType.equals("long")) {
			value = Long.valueOf(strValue);
		}
		else if(simpleType.equals("Short") || simpleType.equals("short")) {
			value = Short.valueOf(strValue);
		}
		else if(simpleType.equals("Byte") || simpleType.equals("byte")) {
			value = Byte.valueOf(strValue);
		}
		else if(simpleType.equals("Float") || simpleType.equals("float")) {
			value = Float.valueOf(strValue);
		}
		else if(simpleType.equals("Double") || simpleType.equals("double")) {
			value = Double.valueOf(strValue);
		}
		else if(simpleType.equals("Boolean") || simpleType.equals("boolean")) {
			value = Boolean.valueOf(strValue);
		}
		else if(simpleType.equals("Character") || simpleType.equals("char")) {
			if(strValue.length() != 1)
				throw new IllegalArgumentException(strValue + " contains more than 1 character");
			value = Character.valueOf(strValue.charAt(0));
		}
		else if(simpleType.equals("BigDecimal")) {
			value = new BigDecimal(strValue);
		}
		else if(simpleType.equals("BigInteger")) {
			value = new BigInteger(strValue);
		}
		else if(simpleType.equalsIgnoreCase("Date")) {
			try {
				value = new SimpleDateFormat().parse(strValue);
			} catch (ParseException e) {
				throw new IllegalArgumentException(strValue + " is not of recognized date format", e);
			}
		}
		else if(simpleType.equalsIgnoreCase("Timestamp")) {
			try {
				java.util.Date date = new SimpleDateFormat().parse(strValue);
				value = new Timestamp(date.getTime());
			} catch (ParseException e) {
				throw new IllegalArgumentException(strValue + " is not of recognized date format", e);
			}
		}
		else if(simpleType.equalsIgnoreCase("object")) {
			value = strValue;
		}
		
		return value;
	}
	
	public static Object getValueFromString(Class<?> simpleType, String strValue) {
		return getValueFromString(simpleType.getSimpleName(), strValue);
	}
	
	/*
	public static Object getValueFromString(Class<?> simpleType, String strValue) {
		Object value = null;
		if(simpleType.equals(String.class)) {
			value = strValue;
		}
		else if(simpleType.equals(Integer.class) || simpleType.equals(int.class)) {
			value = Integer.valueOf(strValue);
		}
		else if(simpleType.equals(Long.class) || simpleType.equals(long.class)) {
			value = Long.valueOf(strValue);
		}
		else if(simpleType.equals(Short.class) || simpleType.equals(short.class)) {
			value = Short.valueOf(strValue);
		}
		else if(simpleType.equals(Byte.class) || simpleType.equals(byte.class)) {
			value = Byte.valueOf(strValue);
		}
		else if(simpleType.equals(Float.class) || simpleType.equals(float.class)) {
			value = Float.valueOf(strValue);
		}
		else if(simpleType.equals(Double.class) || simpleType.equals(double.class)) {
			value = Double.valueOf(strValue);
		}
		else if(simpleType.equals(Boolean.class) || simpleType.equals(boolean.class)) {
			value = Boolean.valueOf(strValue);
		}
		else if(simpleType.equals(Character.class) || simpleType.equals(char.class)) {
			if(strValue.length() != 1)
				throw new IllegalArgumentException(strValue + " contains more than 1 character");
			value = Character.valueOf(strValue.charAt(0));
		}
		else if(simpleType.equals(BigDecimal.class)) {
			value = new BigDecimal(strValue);
		}
		else if(simpleType.equals(BigInteger.class)) {
			value = new BigInteger(strValue);
		}
		else if(simpleType.equals(java.util.Date.class)) {
			try {
				value = new SimpleDateFormat().parse(strValue);
			} catch (ParseException e) {
				throw new IllegalArgumentException(strValue + " is not of recognized date format", e);
			}
		}
		else if(simpleType.equals(Timestamp.class)) {
			try {
				java.util.Date date = new SimpleDateFormat().parse(strValue);
				value = new Timestamp(date.getTime());
			} catch (ParseException e) {
				throw new IllegalArgumentException(strValue + " is not of recognized date format", e);
			}
		}
		else if(simpleType.equals(java.sql.Date.class)) {
			try {
				java.util.Date date = new SimpleDateFormat().parse(strValue);
				value = new java.sql.Date(date.getTime());
			} catch (ParseException e) {
				throw new IllegalArgumentException(strValue + " is not of recognized date format", e);
			}
		}else if(simpleType.equals(Object.class)) {
			value = strValue;
		}
		
		return value;
	}
	*/
	

}
