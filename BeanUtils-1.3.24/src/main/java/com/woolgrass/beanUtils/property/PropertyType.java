package com.woolgrass.beanUtils.property;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.woolgrass.beanUtils.ReflectionUtils;
import com.woolgrass.beanUtils.property.PropertyUtils.PropertyInfo;


public enum PropertyType {
	FIRSTSIMPLE {
		@Override
		public PropertyInfo getPropertyValue(PropertyContext pc, PropertyInfo owner) {
			String propName = pc.getPropertyName();
			Object instance = owner.getPropValue();
			Object propValue = ReflectionUtils.getValue(instance, propName);
			Type propDeclareType = ReflectionUtils.getType(instance, propName);
			PropertyInfo propInfo = new PropertyInfo();
			propInfo.setPropName(propName);
			propInfo.setPropValue(propValue);
			propInfo.setPropDeclareType(propDeclareType);
			propInfo.setRoot(false);
			return propInfo;
		}

		@Override
		public void setPropertyValue(PropertyContext pc, PropertyInfo owner, Object value) {
			String propName = pc.getPropertyName();
			Object ownerInstance = owner.getPropValue();
			ReflectionUtils.setValue(ownerInstance, propName, value);
		}
	}, 
	NESTED {
		@Override
		public PropertyInfo getPropertyValue(PropertyContext pc, PropertyInfo owner) {
			String propName = pc.getPropertyName();
			Object instance = owner.getPropValue();
			Object propValue = ReflectionUtils.getValue(instance, propName);
			Type propDeclareType = ReflectionUtils.getType(instance, propName);
			PropertyInfo propInfo = new PropertyInfo();
			propInfo.setPropName(propName);
			propInfo.setPropValue(propValue);
			propInfo.setPropDeclareType(propDeclareType);
			propInfo.setRoot(false);
			return propInfo;
		}

		@Override
		public void setPropertyValue(PropertyContext pc, PropertyInfo owner, Object value) {
			String propName = pc.getPropertyName();
			Object ownerInstance = owner.getPropValue();
			ReflectionUtils.setValue(ownerInstance, propName, value);			
		}
	}, 
	MAPPED {
		@Override
		public PropertyInfo getPropertyValue(PropertyContext pc, PropertyInfo owner) {			
			String ownerName = owner.getPropName();		
			Object instance = owner.getPropValue();
			if(!(instance instanceof Map)) {
				throw new IncorrectPropertyTypeException(ownerName + " is not an map."); 
			}
			PropertyInfo propInfo;
			Type mapType = owner.getPropDeclareType();
			if(mapType instanceof ParameterizedType) {
						
				ParameterizedType mapPt = (ParameterizedType) mapType; 
				Type mapRawType = mapPt.getRawType();
				if(mapRawType != Map.class)
					throw new IncorrectPropertyTypeException(ownerName + " is not an map.");
				
				Type[] keyValueType = mapPt.getActualTypeArguments();
				Type keyType = keyValueType[0];	
				Type valueType = keyValueType[1];
				if(keyType == String.class) {					
					propInfo = getValueFromStringKey(pc, owner, valueType);
						
				}else if(keyType == Integer.class) {
					propInfo = getValueFromIntKey(pc, owner, valueType);	
				}else if(keyType == Long.class) {
					propInfo = getValueFromLongKey(pc, owner, valueType);	
					
				}else if(keyType == Object.class) {
					propInfo = getValueFromObjectkey(pc, owner, valueType);
				}else {
					throw new IllegalArgumentException(owner.getPropName() + "'s key type - " + keyType + " is not supported");
				}
			}
			else {
				propInfo = getValueFromObjectkey(pc, owner, Object.class);					
				
			}
			return propInfo;
		}

		protected PropertyInfo getValueFromLongKey(PropertyContext pc, PropertyInfo owner, Type valueType) {
			String keyName = pc.getPropertyName();
			Object instance = owner.getPropValue();
			String ownerName = owner.getPropName();
			Map<?,?> mapInst = (Map<?,?>)instance;
			PropertyInfo propInfo = null;
			try {
				Long longKey = Long.valueOf(keyName);
				boolean hasKey = mapInst.containsKey(longKey);
				if(hasKey) {
					Object value = mapInst.get(longKey);
					propInfo = new PropertyInfo();
					propInfo.setPropValue(value);
					propInfo.setPropName(ownerName + "(" + keyName + ")");
					if(value == null)
						propInfo.setPropDeclareType(valueType);
					else
						propInfo.setPropDeclareType(value.getClass());
					propInfo.setRoot(false);
				}else {
					throw new IllegalArgumentException(ownerName + " doesn't contain key - " + keyName);
				}
			}
			catch (NumberFormatException e) {
				throw new IllegalArgumentException(keyName + " is not an integer");
			}
			return propInfo;
		}

		protected PropertyInfo getValueFromIntKey(PropertyContext pc, PropertyInfo owner, Type valueType) {
			String keyName = pc.getPropertyName();
			Object instance = owner.getPropValue();
			String ownerName = owner.getPropName();
			Map<?,?> mapInst = (Map<?,?>)instance;
			PropertyInfo propInfo = null;
			try {
				Integer intKey = Integer.valueOf(keyName);
				boolean hasKey = mapInst.containsKey(intKey);
				if(hasKey) {
					Object value = mapInst.get(intKey);
					propInfo = new PropertyInfo();
					propInfo.setPropValue(value);
					propInfo.setPropName(ownerName + "(" + keyName + ")");
					if(value == null)
						propInfo.setPropDeclareType(valueType);
					else
						propInfo.setPropDeclareType(value.getClass());
					propInfo.setRoot(false);
				}else {
					throw new IllegalArgumentException(ownerName + " doesn't contain key - " + keyName);
				}
			}
			catch (NumberFormatException e) {
				throw new IllegalArgumentException(keyName + " is not an integer");
			}
			return propInfo;
		}

		protected PropertyInfo getValueFromStringKey(PropertyContext pc, PropertyInfo owner, Type valueType) {
			String keyName = pc.getPropertyName();
			Object instance = owner.getPropValue();
			String ownerName = owner.getPropName();
			Map<?,?> mapInst = (Map<?,?>)instance;
			boolean hasKey = mapInst.containsKey(keyName);
			if(!hasKey) {
				throw new IllegalArgumentException(ownerName + " doesn't contain key - " + keyName);
			}
			
			Object value = mapInst.get(keyName);
			PropertyInfo propInfo = new PropertyInfo();
			propInfo.setPropValue(value);
			propInfo.setPropName(ownerName + "(" + keyName + ")");
			if(value == null)
				propInfo.setPropDeclareType(valueType);
			else
				propInfo.setPropDeclareType(value.getClass());
			propInfo.setRoot(false);
			return propInfo;
		}

		protected PropertyInfo getValueFromObjectkey(PropertyContext pc, PropertyInfo owner, Type valueType) {
			String keyName = pc.getPropertyName();
			Object instance = owner.getPropValue();
			String ownerName = owner.getPropName();
			Type mapType = owner.getPropDeclareType();
			PropertyInfo propInfo = new PropertyInfo();
			if(instance instanceof Map) {
				Map<?,?> mapInst = (Map<?,?>)instance;
				Object value = null;
				boolean hasKey = mapInst.containsKey(keyName);
				if(hasKey) {
					value = mapInst.get(keyName);
				}else {
					Integer intKey = null;
					try {
						intKey = Integer.valueOf(keyName);
						hasKey = mapInst.containsKey(intKey);
					}
					catch (NumberFormatException e) {							
					}				
					if(hasKey) {
						value = mapInst.get(intKey);
					}else {
						Long longKey = null;
						try {
							longKey = Long.valueOf(keyName);
							hasKey = mapInst.containsKey(longKey);
						}
						catch (NumberFormatException e) {							
						}
						if(hasKey) {
							value = mapInst.get(longKey);
						}
					}
				}
				
				if(hasKey) {
					propInfo.setPropValue(value);
					propInfo.setPropName(ownerName + "(" + keyName + ")");
					if(value == null)
						propInfo.setPropDeclareType(valueType);
					else
						propInfo.setPropDeclareType(value.getClass());
					propInfo.setRoot(false);
				}
				else
					throw new IllegalArgumentException(ownerName + " does't contain key - " + keyName);
			}
			else {					
				throw new IncorrectPropertyTypeException(ownerName + " is not an map.");
			}
			return propInfo;
		}

		@Override
		public void setPropertyValue(PropertyContext pc, PropertyInfo owner, Object value) {
			String ownerName = owner.getPropName();		
			Object instance = owner.getPropValue();
			if(!(instance instanceof Map)) {
				throw new IncorrectPropertyTypeException(ownerName + " is not an map."); 
			}
			
			Type mapType = owner.getPropDeclareType();
			if(mapType instanceof ParameterizedType) {
				ParameterizedType mapPt = (ParameterizedType) mapType; 
				Type mapRawType = mapPt.getRawType();
				if(mapRawType != Map.class)
					throw new IncorrectPropertyTypeException(ownerName + " is not an map.");
				
				Type[] keyValueType = mapPt.getActualTypeArguments();
				Type keyType = keyValueType[0];	
				//Type valueType = keyValueType[1];

				if(keyType == String.class) {					
					setValueForStringKey(pc, owner, value);						
				}else if(keyType == Integer.class) {
					setValueForIntKey(pc, owner, value);	
				}else if(keyType == Long.class) {
					setValueForLongKey(pc, owner, value);	
					
				}else if(keyType == Object.class) {
					setValueForObjectkey(pc, owner, value);
				}else {
					throw new IllegalArgumentException(owner.getPropName() + "'s key type - " + keyType + " is not supported");
				}
			}
			else {
				setValueForObjectkey(pc, owner, value);					
				
			}
			
		}
		
		protected void setValueForStringKey(PropertyContext pc, PropertyInfo owner, Object value) {
			String keyName = pc.getPropertyName();
			Object instance = owner.getPropValue();
			Map mapInst = (Map)instance;			
			mapInst.put(keyName, value);
		}
		
		protected void setValueForLongKey(PropertyContext pc, PropertyInfo owner, Object value) {
			String keyName = pc.getPropertyName();
			Object instance = owner.getPropValue();
			Map mapInst = (Map)instance;
			try {
				Long longKey = Long.valueOf(keyName);
				mapInst.put(longKey, value);
			}
			catch (NumberFormatException e) {
				throw new IllegalArgumentException(keyName + " is not an long integer");
			}
		}

		protected void setValueForIntKey(PropertyContext pc, PropertyInfo owner, Object value) {
			String keyName = pc.getPropertyName();
			Object instance = owner.getPropValue();
			Map mapInst = (Map)instance;
			try {
				Integer intKey = Integer.valueOf(keyName);
				mapInst.put(intKey, value);
			}
			catch (NumberFormatException e) {
				throw new IllegalArgumentException(keyName + " is not an integer");
			}
		}
		
		protected void setValueForObjectkey(PropertyContext pc, PropertyInfo owner, Object value) {
			String keyName = pc.getPropertyName();
			Object instance = owner.getPropValue();

			Map mapInst = (Map)instance;
			boolean hasKey = mapInst.containsKey(keyName);
			Object keyValue = null;;
			if(hasKey) {
				keyValue = keyName;
			}else {
				Integer intKey = null;
				try {
					intKey = Integer.valueOf(keyName);
					hasKey = mapInst.containsKey(intKey);
				}
				catch (NumberFormatException e) {							
				}				
				if(hasKey) {
					keyValue = intKey;
				}else {
					Long longKey = null;
					try {
						longKey = Long.valueOf(keyName);
						hasKey = mapInst.containsKey(longKey);
					}
					catch (NumberFormatException e) {							
					}
					if(hasKey) {
						keyValue = longKey;;
					}
				}
			}
				
			if(hasKey) {
				mapInst.put(keyValue, value);
			}
			else {
				mapInst.put(keyName, value);
			}
		}
	}, 
	INDEXED {
		
		protected boolean isArrayOrList(Object instance) {
			if(instance.getClass().isArray()) {
				return true;
			}else if(instance instanceof List) {
				return true;
			}else
				return false;
		}
		
		@Override
		public PropertyInfo getPropertyValue(PropertyContext pc, PropertyInfo owner) {
//			String keyName = pc.getPropertyName();
			Object instance = owner.getPropValue();
			String ownerName = owner.getPropName();
			if(!isArrayOrList(instance))
				throw new IncorrectPropertyTypeException(ownerName + " is not an array or list.");
			
			
			String index = pc.getPropertyName();
			PropertyInfo propInfo = new PropertyInfo();
			Object value = null;
			int intIndex;
			try {
				intIndex = Integer.parseInt(index);
			}
			catch (NumberFormatException e) {
				throw new IllegalStateException(index + " is not a integer index.");
			}
			if(instance.getClass().isArray()) {
				value = Array.get(instance, intIndex);
			}
			else if(instance instanceof List) {
				List<?> list = (List<?>)instance;
				value = list.get(intIndex);
			}
			else {					
				throw new IllegalStateException(ownerName + " is not an array or list.");
			}
			propInfo.setPropValue(value);
//			propInfo.setPropName(ownerName + "[" + keyName + "]");
			propInfo.setPropName(ownerName + "[" + index + "]");
			propInfo.setPropDeclareType(value.getClass());
			propInfo.setRoot(false);
			return propInfo;
		}

		@Override
		public void setPropertyValue(PropertyContext pc, PropertyInfo owner, Object value) {
			Object instance = owner.getPropValue();
			String ownerName = owner.getPropName();
			String index = pc.getPropertyName();
			
			if(!isArrayOrList(instance))
				throw new IncorrectPropertyTypeException(ownerName + " is not an array or list.");

			int intIndex;
			try {
				intIndex = Integer.parseInt(index);
			}
			catch (NumberFormatException e) {
				throw new IllegalStateException(index + " is not a integer index.");
			}
			if(instance.getClass().isArray()) {
				Array.set(instance, intIndex, value);
			}
			else if(instance instanceof List) {
				List list = (List)instance;
				list.set(intIndex, value);
			}
			else {					
				throw new IllegalStateException(ownerName + " is not an array or list.");
			}
			
		}
	};
	
	public abstract PropertyInfo getPropertyValue(PropertyContext pc, PropertyInfo owner);
	
	public abstract void setPropertyValue(PropertyContext pc, PropertyInfo owner, Object value);
	
	
	public static class IncorrectPropertyTypeException extends RuntimeException{

		private static final long serialVersionUID = 4817529752348919155L;

		public IncorrectPropertyTypeException(String message) {
			super(message);
		}
		
	}
}
