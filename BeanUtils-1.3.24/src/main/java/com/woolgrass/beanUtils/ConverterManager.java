package com.woolgrass.beanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.woolgrass.beanUtils.build.ObjectContext;
import com.woolgrass.beanUtils.traverse.FieldContext;
import com.woolgrass.beanUtils.traverse.TargetContext;

public class ConverterManager {

	protected Map<Class<?>, TypeConverter> typeConverterMap = new HashMap<Class<?>, TypeConverter>();
	protected Map<ClassField, TypeConverter> fieldConverterMap = new HashMap<ClassField, TypeConverter>();
	
	public void addTypeConverter(Class<?> type, TypeConverter converter) {
		typeConverterMap.put(type, converter);
	}
	
	public void addFieldConverter(Class<?> clazz, String fieldName, TypeConverter converter) {
		ClassField clsField = new ClassField(clazz, fieldName);
		fieldConverterMap.put(clsField, converter);
	}
	
	public TypeConverter lookupTypeConverter(Class<?> type) {
		TypeConverter converter = typeConverterMap.get(type);
//		if(converter == null) {
//			for(Entry<Class<?>, TypeConverter> entry : typeConverterMap.entrySet()) {
//				Class<?> clsForConverter = entry.getKey();
//				if(clsForConverter.isAssignableFrom(type)) {
//					converter = entry.getValue();
//					break;
//				}
//					
//			}
//		}
		return converter;
		
	}
	
	public TypeConverter lookupFieldConverter(Class<?> clazz, String fieldName) {
		ClassField clsField = new ClassField(clazz, fieldName);
		TypeConverter converter =  fieldConverterMap.get(clsField);
		if(converter == null) {
			for(Entry<ClassField, TypeConverter> entry : fieldConverterMap.entrySet()) {
				ClassField clsFieldForConverter = entry.getKey();
				Class<?> clsForConverter = clsFieldForConverter.getClazz();
				if(clsForConverter.isAssignableFrom(clazz)) {
					if(clsFieldForConverter.getFieldName().equals(fieldName)) {
						converter = entry.getValue();
					}
					break;
				}
					
			}
		}
		return converter;
	}
	
	public TypeConverter getConverter(TargetContext context) {
		TypeConverter converter = null;
		Object value = context.getValue();
		Class<?> valueObjCls = value.getClass();
		if(context instanceof FieldContext) {
			FieldContext fieldCtx = (FieldContext)context;
			Class<?> ownerCls = fieldCtx.getParentObjectClass();
			String fieldName = fieldCtx.getName();
			converter = lookupFieldConverter(ownerCls, fieldName);
		}
		if(converter == null) {
			converter = lookupTypeConverter(valueObjCls);
		}
		return converter;
	}
	
	public TypeConverter getconverter(ObjectContext context) {
		TypeConverter converter = null;
//		Object value = context.getValue();
//		Class<?> valueObjCls = value.getClass();
		Class<?> valueObjCls = context.getType();
		if(context instanceof com.woolgrass.beanUtils.build.FieldContext) {
			com.woolgrass.beanUtils.build.FieldContext fieldCtx = 
				(com.woolgrass.beanUtils.build.FieldContext)context;
			Class<?> ownerCls = fieldCtx.getParentObjectClass();
			String fieldName = fieldCtx.getFieldName();
			converter = lookupFieldConverter(ownerCls, fieldName);
		}
		if(converter == null) {
			converter = lookupTypeConverter(valueObjCls);
		}
		return converter;
	}
	
	static class ClassField {
		Class<?> clazz;
		String fieldName;
		
		public ClassField(Class<?> clazz, String fieldName) {
			this.clazz = clazz;
			this.fieldName = fieldName;
		}

		public Class<?> getClazz() {
			return clazz;
		}

		public void setClazz(Class<?> clazz) {
			this.clazz = clazz;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
			result = prime * result
					+ ((fieldName == null) ? 0 : fieldName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ClassField other = (ClassField) obj;
			if (clazz == null) {
				if (other.clazz != null)
					return false;
			} else if (!clazz.equals(other.clazz))
				return false;
			if (fieldName == null) {
				if (other.fieldName != null)
					return false;
			} else if (!fieldName.equals(other.fieldName))
				return false;
			return true;
		}
		
		
		
	};
}
