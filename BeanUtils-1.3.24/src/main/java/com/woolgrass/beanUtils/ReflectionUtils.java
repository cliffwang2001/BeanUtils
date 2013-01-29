package com.woolgrass.beanUtils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class ReflectionUtils {
	
	protected final static List<Class> primitiveWrapperClassList = Arrays.<Class>asList(java.lang.Boolean.class, java.lang.Character.class,
	     java.lang.Byte.class, java.lang.Short.class, java.lang.Integer.class, java.lang.Long.class,
	     java.lang.Float.class, java.lang.Double.class, java.lang.Void.class); 
	
	protected static Map<Class,Map<String, Field>> classToNonstaticFields = Collections.synchronizedMap(new HashMap<Class,Map<String, Field>>());
	protected static Map<Class,Map<String, Field>> classToStaticFields = Collections.synchronizedMap(new HashMap<Class,Map<String, Field>>());

	protected static Map<Class,Map<MethodInfo, Method>> classToNonstaticMethods = Collections.synchronizedMap(new HashMap<Class,Map<MethodInfo, Method>>());
	protected static Map<Class,Map<MethodInfo, Method>> classToStaticMethods = Collections.synchronizedMap(new HashMap<Class,Map<MethodInfo, Method>>());

	public static void handleReflectionException(Exception ex) {
		if(ex instanceof InstantiationException) {
			throw new IllegalStateException("Can't create instance: " + ex.getMessage());
		}
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method or field: " + ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException) {
			throw (RuntimeException)ex;
		}
		throw new IllegalStateException(
				"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
	}

	public static void handleInvocationTargetException(InvocationTargetException ex) {
		if (ex.getTargetException() instanceof RuntimeException) {
			throw (RuntimeException) ex.getTargetException();
		}
		if (ex.getTargetException() instanceof Error) {
			throw (Error) ex.getTargetException();
		}
		throw new IllegalStateException(
				"Unexpected exception thrown by method - " + ex.getTargetException().getClass().getName() +
				": " + ex.getTargetException().getMessage());
	}
	
	public static void doWithFields(Class targetClass, FieldCallback fc) throws IllegalArgumentException {
		doWithFields(targetClass, fc, null);
	}

	public static void doWithFields(Class targetClass, FieldCallback fc, FieldFilter ff)
			throws IllegalArgumentException {

		if(targetClass == null )
			throw new IllegalArgumentException("targetClass is null");
		if(fc == null )
			throw new IllegalArgumentException("fc is null");
		
		// Keep backing up the inheritance hierarchy.
		while (targetClass != null && targetClass != Object.class) {
			// Copy each field declared on this class unless it's static or file.
			//Field[] fields = targetClass.getDeclaredFields();
			Field[] fields = targetClass.isInterface() ? targetClass.getFields() : targetClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				// Skip static and final fields.
				if (ff != null && !ff.matches(fields[i])) {
					continue;
				}
				try {
					//fields[i].setAccessible(true);
					makeAccessible(fields[i]);
					fc.doWith(fields[i]);
				}
				catch (IllegalAccessException ex) {
					throw new IllegalStateException(
							"Shouldn't be illegal to access field '" + fields[i].getName() + "': " + ex);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		
	}

	public static interface FieldCallback {
		void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
	}

	public static interface FieldFilter {
		boolean matches(Field field);
	}

	public static FieldFilter NONSTATIC_FIELDS = new FieldFilter() {
		public boolean matches(Field field) {
			return !(Modifier.isStatic(field.getModifiers()));
		}
	};
	
	public static FieldFilter STATIC_FIELDS = new FieldFilter() {
		public boolean matches(Field field) {
			return Modifier.isStatic(field.getModifiers());
		}
	};
	
	public static void doWithNonStaticFields(Class targetClass, FieldCallback fc) throws IllegalArgumentException {
		doWithFields(targetClass, fc, NONSTATIC_FIELDS);
	}	
	
	public static void doWithStaticFields(Class targetClass, FieldCallback fc) throws IllegalArgumentException {
		doWithFields(targetClass, fc, STATIC_FIELDS);
	}		
	
	public static interface InstanceFieldProcessor {
		void processField(String fieldName, Object fieldValue);
	}
	
	public static void doWithNonStaticFields(Class targetClass, final Object instance, final InstanceFieldProcessor processor) throws IllegalArgumentException {
		FieldCallback fc = new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException
			{
				//field.setAccessible(true);
				Object fieldValue = field.get(instance);
				String fieldName = field.getName();
				processor.processField(fieldName, fieldValue);
			}
		};
		
		doWithNonStaticFields(targetClass, fc);
	}
	
	public static void doWithNonStaticFields(final Object instance, final InstanceFieldProcessor processor) throws IllegalArgumentException {
		doWithNonStaticFields(instance.getClass(), instance, processor);
	}
	
	public static Map<String, Field> getNonStaticFieldsMap(Class targetClass) throws IllegalArgumentException {
		Map<String, Field> existingMap = classToNonstaticFields.get(targetClass);
		if(existingMap != null)
			return existingMap;
		
		synchronized(classToNonstaticFields) {
			existingMap = classToNonstaticFields.get(targetClass);
			if(existingMap != null)
				return existingMap;
			
			final Map<String, Field> fieldsMap = new HashMap<String, Field>();
			FieldCallback fc = new FieldCallback() {
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException
				{
					fieldsMap.put(field.getName(), field);
				}
			};
			
			doWithNonStaticFields(targetClass, fc);
			classToNonstaticFields.put(targetClass, fieldsMap);
			return fieldsMap;
		}
	}	
	
	public static Field getNonStaticField(Class<?> targetClass, String fieldName) {
		Map<String, Field> fieldMap = getNonStaticFieldsMap(targetClass);
		return fieldMap.get(fieldName);
	}
	
	public static Map<String, Field> getStaticFieldsMap(Class targetClass) throws IllegalArgumentException {
		Map<String, Field> existingMap = classToStaticFields.get(targetClass);
		if(existingMap != null)
			return existingMap;
		
		synchronized(classToStaticFields) {
			existingMap = classToStaticFields.get(targetClass);
			if(existingMap != null)
				return existingMap;
			
			final Map<String, Field> fieldsMap = new HashMap<String, Field>();
			FieldCallback fc = new FieldCallback() {
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException
				{
					fieldsMap.put(field.getName(), field);
				}
			};
			
			doWithStaticFields(targetClass, fc);
			classToStaticFields.put(targetClass, fieldsMap);
			return fieldsMap;
		}
	}	
	
	@Deprecated
	public static Map<String, Field> getNonStaticFieldsMapWithLowerCase(Class targetClass) throws IllegalArgumentException {
//		Map<String, Field> existingMap = classToNonstaticFields.get(targetClass);
//		if(existingMap != null)
//			return existingMap;
		
		final Map<String, Field> fieldsMap = new HashMap<String, Field>();
		FieldCallback fc = new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException
			{
				fieldsMap.put(field.getName().toLowerCase(), field);
			}
		};
		
		doWithNonStaticFields(targetClass, fc);
//		classToNonstaticFields.put(targetClass, fieldsMap);
		return fieldsMap;
	}	
	
	public static Object newInstance(Class clazz)
	{
		Object instance = null;
		try
		{
			instance = clazz.newInstance();
		}
		catch(Exception ex)
		{
			handleReflectionException(ex);
		}
		return instance;
	}	
	
	public static void setField(Field field, Object instance, Object value)
	{
		Class<?> fieldType = field.getType();
		Class<?> valueType = value.getClass();
//		if(!fieldType.isAssignableFrom(valueType)) {
//			throw new IllegalArgumentException(valueType.getName() + " can't be assigned to " + fieldType.getName());
//		}
		
		try
		{
			field.set(instance, value);
		}
		catch(Exception ex)
		{
			handleReflectionException(ex);
		}		
	}

	public static interface fieldValueExtractor {
		Object getValueFor(String field);
	}
	
	public static void populate(Class targetClass, Object instance, fieldValueExtractor valueExtractor)
	{
		Map<String, Field> fieldMap = getNonStaticFieldsMap(targetClass);
		Set<String> keySet = fieldMap.keySet();
		for(String fieldname : keySet)
		{
			Field field = fieldMap.get(fieldname);
			Object value = valueExtractor.getValueFor(fieldname);
			setField(field, instance, value);
		}
	}
	
	public static Map<String, Object> getValueMap(Class targetClass, Object instance)
	{
		if(instance.getClass() != targetClass)
			throw new IllegalArgumentException("instance parameter is not of type targeCalss parameter");
		
		Map<String, Object> valueMap = new HashMap<String, Object>();
		
		Map<String, Field> fieldMap = getNonStaticFieldsMap(targetClass);
		Set<String> keySet = fieldMap.keySet();
		for(String fieldname : keySet)
		{
			Field field = fieldMap.get(fieldname);
			Object value;
			try {
				value = field.get(instance);
				valueMap.put(fieldname, value);			
			} catch (IllegalAccessException ex) {
				throw new IllegalStateException(
						"Shouldn't be illegal to access field '" + field.getName() + "': " + ex);
			}			
		}	
		return valueMap;
	}

	public static Map<String, Object> getValueMap(Object instance)
	{
		Class targetClass = instance.getClass();
		return getValueMap(targetClass, instance);
	}
	
	public static void setValue(Object instance, String fieldname, Object value)
	{
		Class cls = instance.getClass();
		Map<String, Field> fieldMap = getNonStaticFieldsMap(cls);
		Field field = fieldMap.get(fieldname);
		if(field == null)
		{
			//return false;
			throw new IllegalArgumentException("field - " + fieldname + " doesn't exist.");
		}
			
		
		setField(field, instance, value);
		//return true;
	}
	
	public static Object getValue(Object instance, String fieldname)
	{
		
		Class cls = instance.getClass();
		
		Map<String, Field> fieldMap = getNonStaticFieldsMap(cls);
		Field field = fieldMap.get(fieldname);
		if(field == null)
		{
			//return false;
			throw new IllegalArgumentException("field - " + fieldname + " doesn't exist.");
		}
						
		Object value;
		try {
			value = field.get(instance);
			return value;
		} catch (IllegalAccessException ex) {
			throw new IllegalStateException(
					"Shouldn't be illegal to access field '" + field.getName() + "': " + ex);
		}			
	}	
	
	public static boolean setStaticFieldValue(Class cls, String fieldname, Object value)
	{
		Map<String, Field> fieldMap = getStaticFieldsMap(cls);
		Field field = fieldMap.get(fieldname);
		if(field == null)
			return false;
		
		setField(field, null, value);
		return true;
	}
	
	public static Object getStaticFieldValue(Class cls, String fieldname)
	{
				
		Map<String, Field> fieldMap = getStaticFieldsMap(cls);
		Field field = fieldMap.get(fieldname);
		if(field == null)
			return null;
						
		Object value;
		try {
			value = field.get(null);
			return value;
		} catch (IllegalAccessException ex) {
			throw new IllegalStateException(
					"Shouldn't be illegal to access field '" + field.getName() + "': " + ex);
		}			
	}		
	
	public static boolean isSimpleType(Object instance)
	{ 
		if(instance == null)
			throw new IllegalArgumentException("instance parameter is null");
		
		Class cls = instance.getClass();
		if(cls.isPrimitive())
			return true;
		else if(primitiveWrapperClassList.contains(cls))
			return true;
		else if(cls.equals(String.class))
			return true;
		else if(cls.equals(BigDecimal.class))
			return true;
		else if(cls.equals(BigInteger.class))
			return true;		
		else if(cls.equals(java.util.Date.class))
			return true;
		else if(cls.equals(java.sql.Date.class))
			return true;
		else
			return false;
	}

	/*
	public static Object getNestedFieldValue(Object instance, String fieldname) 
	{
		String[] fieldsArr = fieldname.split("\\.");
		for(String field : fieldsArr)
		{
			instance = getValue(instance,field);
			if(instance == null)
				return null;
		}
		return instance;
	}
*/
	

	

	public static interface MethodCallback {
		void doWith(Method Method) throws IllegalArgumentException, IllegalAccessException;
	}

	public static interface MethodFilter {
		boolean matches(Method Method);
	}
	
	public static MethodFilter NONSTATIC_METHODS = new MethodFilter() {
		public boolean matches(Method Method) {
			return !(Modifier.isStatic(Method.getModifiers()));
		}
	};
	
	public static MethodFilter STATIC_METHODS = new MethodFilter() {
		public boolean matches(Method Method) {
			return Modifier.isStatic(Method.getModifiers());
		}
	};
	
	public static void doWithMethods(Class targetClass, MethodCallback mc, MethodFilter mf)	throws IllegalArgumentException {	
		if(targetClass == null)
			throw new IllegalArgumentException("targetClass is null.");
		if(mc == null )
			throw new IllegalArgumentException("mc is null");
			
		while (targetClass != null && targetClass != Object.class) {
			Method[] methods = (targetClass.isInterface() ? targetClass.getMethods() : targetClass.getDeclaredMethods());
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (mf != null && !mf.matches(method)) {
					continue;
				}
				try {
					makeAccessible(method);
					mc.doWith(method);
				}
				catch (IllegalAccessException ex) {
					throw new IllegalStateException(
							"Shouldn't be illegal to access method '" + method.getName() + "': " + ex);
				}				
			}
			targetClass = targetClass.getSuperclass();
		}
	}

	public static void doWithNonStaticMethods(Class targetClass, MethodCallback mc) throws IllegalArgumentException {
		doWithMethods(targetClass, mc, NONSTATIC_METHODS);
	}	
	
	public static void doWithStaticMethods(Class targetClass, MethodCallback mc) throws IllegalArgumentException {
		doWithMethods(targetClass, mc, STATIC_METHODS);
	}	
	
	
	public static Map<MethodInfo, Method> getNonStaticMethodsMap(Class targetClass) throws IllegalArgumentException {
		Map<MethodInfo, Method> existingMap = classToNonstaticMethods.get(targetClass);
		if(existingMap != null)
			return existingMap;
		
		synchronized(classToNonstaticMethods) {
			existingMap = classToNonstaticMethods.get(targetClass);
			if(existingMap != null)
				return existingMap;
			
			final Map<MethodInfo, Method> methodsMap = new HashMap<MethodInfo, Method>();
			MethodCallback mc = new MethodCallback() {
				public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException
				{
					MethodInfo mi = new MethodInfo(method.getName(), method.getParameterTypes());
					methodsMap.put(mi, method);
				}
			};
			
			doWithNonStaticMethods(targetClass, mc);
			classToNonstaticMethods.put(targetClass, methodsMap);
			return methodsMap;
		}
	}	
	
	public static Map<MethodInfo, Method> getStaticMethodsMap(Class targetClass) throws IllegalArgumentException {
		Map<MethodInfo, Method> existingMap = classToStaticMethods.get(targetClass);
		if(existingMap != null)
			return existingMap;
		
		synchronized(classToStaticMethods) {
			existingMap = classToStaticMethods.get(targetClass);
			if(existingMap != null)
				return existingMap;
			
			final Map<MethodInfo, Method> methodsMap = new HashMap<MethodInfo, Method>();
			MethodCallback mc = new MethodCallback() {
				public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException
				{
					MethodInfo mi = new MethodInfo(method.getName(), method.getParameterTypes());
					methodsMap.put(mi, method);
				}
			};
			
			doWithStaticMethods(targetClass, mc);
			classToStaticMethods.put(targetClass, methodsMap);
			return methodsMap;
		}
	}		
	
	public static Object invokeMethod(Method method, Object instance) {
		return invokeMethod(method, instance, null);
	}

	public static Object invokeMethod(Method method, Object instance, Object[] args) {
		Object rtn = null;
		try {
			rtn = method.invoke(instance, args);
		}
		catch (Exception ex) {
			handleReflectionException(ex);
		}
		return rtn;
		
	}

	public static Object invokeNonStaticMethod(Class targetClass, Object instance, String methodname, Class[] parameterTypes, Object... args)
	{
		Map<MethodInfo, Method> methodsMap = getNonStaticMethodsMap(targetClass);
		MethodInfo mi = new MethodInfo(methodname, parameterTypes);
		Method method = methodsMap.get(mi);
		if(method == null)
			throw new IllegalArgumentException("No such method - " + methodname + " in class " + targetClass.getName());
		
		return invokeMethod(method, instance, args);
		
	}
	
	public static Object invokeNonStaticMethod(Object instance, String methodname, Class[] parameterTypes, Object... args)
	{
		Class targetClass = instance.getClass();
		return invokeNonStaticMethod(targetClass, instance, methodname, parameterTypes, args);
	}
	
	
	public static Object invokeStaticMethod(Class targetClass, String methodname, Class[] parameterTypes, Object... args)
	{
		Map<MethodInfo, Method> methodsMap = getStaticMethodsMap(targetClass);
		MethodInfo mi = new MethodInfo(methodname, parameterTypes);
		Method method = methodsMap.get(mi);
		if(method == null)
			throw new IllegalArgumentException("No such method - " + methodname + " in class " + targetClass.getName());
		
		return invokeMethod(method, null, args);
		
	}
	
	
	/*
	public static Object invokeNonStaticMethod(Object instance, String methodname, Class[] parameterTypes, Object... args)
	{
		Object result = null;
		try
		{
			Class cls = instance.getClass();
			Method method = cls.getDeclaredMethod(methodname, parameterTypes);
			if (Modifier.isStatic(method.getModifiers()))
			{
				throw new IllegalArgumentException(methodname + " is not a instance method of class " + cls.getName());
			}
			//method.setAccessible(true);
			makeAccessible(method);
			result = method.invoke(instance, args);
			
		}
		catch(Exception ex)
		{
			handleReflectionException(ex);
		}		
		
		return result;		
	}
	
	public static Object invokeNonStaticMethodFromSuperCalss(Object instance, String methodname, Class[] parameterTypes, Object... args)
	{
		Object result = null;
		try
		{
			Class superClass = instance.getClass().getSuperclass();
			Method method = superClass.getDeclaredMethod(methodname, parameterTypes);
			if (Modifier.isStatic(method.getModifiers()))
			{
				throw new IllegalArgumentException(methodname + " is not a instance method of class " + superClass.getName());
			}
			//method.setAccessible(true);
			makeAccessible(method);
			result = method.invoke(instance, args);
			
		}
		catch(Exception ex)
		{
			handleReflectionException(ex);
		}		
		
		return result;		
	}
	
	public static Object invokeStaticMethod(Class cls, String methodname, Class[] parameterTypes, Object... args)
	{
		Object result = null;
		try
		{
			Method method = cls.getDeclaredMethod(methodname, parameterTypes);
			if (!Modifier.isStatic(method.getModifiers()))
			{
				throw new IllegalArgumentException(methodname + " is not a instance method of class " + cls.getName());
			}
			//method.setAccessible(true);
			makeAccessible(method);
			result = method.invoke(null, args);
			
		}
		catch(Exception ex)
		{
			handleReflectionException(ex);
		}		
		
		return result;		
	}	
	*/	
	
	public static boolean setFinalStaticField(Class cls, String fieldname, Object value)
	{
		Map<String, Field> fieldMap = getStaticFieldsMap(cls);
		Field field = fieldMap.get(fieldname);
		if(field == null)
			return false;

		try
		{
			Field modifiersField = Field.class.getDeclaredField("modifiers");       
			modifiersField.setAccessible(true);       
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);         
			setField(field, null, value);
			
		}
		catch(Exception ex)
		{
			handleReflectionException(ex);
		}	
		return true;
	}

	public static Type getType(Object instance, String fieldname)
	{
		
		Class cls = instance.getClass();
		
		Map<String, Field> fieldMap = getNonStaticFieldsMap(cls);
		Field field = fieldMap.get(fieldname);
		if(field == null)
		{
			throw new IllegalArgumentException("field - " + fieldname + " doesn't exist.");
		}
						
		Type type = field.getGenericType();
		return type;		
	}	
	
	public static boolean hasField(Object instance, String fieldname)
	{
		
		Class<?> cls = instance.getClass();
		
		Map<String, Field> fieldMap = getNonStaticFieldsMap(cls);
		Field field = fieldMap.get(fieldname);
		if(field == null) {
			return false;
		}
		else {
			return true;
		}
						
		
	}	
	
	public static Type[] getActualTypeArguments(Field field) {
		Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType) {  
            ParameterizedType pt = (ParameterizedType) genericType;  
              
            return pt.getActualTypeArguments();
		}
		else
			return null;
	}
	
	private static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers()) ||
				!Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}


	private static void makeAccessible(Method method) {
		if (!Modifier.isPublic(method.getModifiers()) ||
				!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
			method.setAccessible(true);
		}
	}
	
	
	public  static class MethodInfo {
		protected String methodname;
		protected Class[] parameterTypes;
		
		public String getMethodname() {
			return methodname;
		}

		public Class[] getParameterTypes() {
			return parameterTypes;
		}
				
		public MethodInfo(String methodname, Class[] parameterTypes) {
			super();
			this.methodname = methodname;
			this.parameterTypes = parameterTypes;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((methodname == null) ? 0 : methodname.hashCode());
			result = prime * result + Arrays.hashCode(parameterTypes);
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
			MethodInfo other = (MethodInfo) obj;
			if (methodname == null) {
				if (other.methodname != null)
					return false;
			} else if (!methodname.equals(other.methodname))
				return false;
			if (!Arrays.equals(parameterTypes, other.parameterTypes))
				return false;
			return true;
		}
		
		
		
	}
}

