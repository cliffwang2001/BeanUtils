package com.woolgrass.beanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericTypeInfo {  
    java.util.List<? extends Foo> fooList = new ArrayList<Bar>();  
    Map<Integer, String> fooMap = new HashMap<Integer, String>();
    String fooStr = "fooStr";
    int[] intArray = {1,2,3};
   
    public static void main(String[] args) throws Exception {  
        Field field = GenericTypeInfo.class.getDeclaredField("fooList");  
   
        Type type = field.getGenericType();  
        System.out.println("type: " + type);  
        if (type instanceof ParameterizedType) {  
            ParameterizedType pt = (ParameterizedType) type;  
            System.out.println("raw type: " + pt.getRawType());  
            System.out.println("owner type: " + pt.getOwnerType());  
            System.out.println("actual type args:");  
            for (Type t : pt.getActualTypeArguments()) {  
                System.out.println("    " + t);  
            }  
        }  
   
        System.out.println();  
   
        Object obj = field.get(new GenericTypeInfo());  
        System.out.println("obj: " + obj);  
        System.out.println("obj class: " + obj.getClass());  
        
        
        System.out.println();  
        
        //Map
        field = GenericTypeInfo.class.getDeclaredField("fooMap");  
        Class<?> declaringClass = field.getDeclaringClass();
        System.out.println("declaringClass: " +declaringClass);
        Class<?> fieldType = field.getType();
        System.out.println("fieldType: " +fieldType);
        
        type = field.getGenericType();  
        System.out.println("type: " + type);  
        if (type instanceof ParameterizedType) {  
            ParameterizedType pt = (ParameterizedType) type;  
            System.out.println("raw type: " + pt.getRawType());  
            System.out.println("owner type: " + pt.getOwnerType());  
            System.out.println("actual type args:");  
            for (Type t : pt.getActualTypeArguments()) {  
                System.out.println("    " + t);  
            }  
        }  
        
        System.out.println();  
        
        obj = field.get(new GenericTypeInfo());  
        System.out.println("obj: " + obj);  
        System.out.println("obj class: " + obj.getClass());  
        
        System.out.println();  
        
        field = GenericTypeInfo.class.getDeclaredField("fooStr");  
        declaringClass = field.getDeclaringClass();
        System.out.println("declaringClass: " +declaringClass);
        fieldType = field.getType();
        System.out.println("fieldType: " +fieldType);
        
        type = field.getGenericType();  
        System.out.println("type: " + type);  
        System.out.println(type == String.class);  
        
        List<String> strList = new ArrayList<String>();
        System.out.println(strList.getClass());
        
        
        TypeVariable<?>[] typeParameters = strList.getClass().getTypeParameters();
        System.out.println(typeParameters);
        
        
        field = GenericTypeInfo.class.getDeclaredField("intArray");  
        fieldType = field.getType();
        System.out.println("fieldType: " +fieldType);
        Class<?> arrayElementType = fieldType.getComponentType();
        System.out.println("arrayElementType: " +arrayElementType);
        
        
    }  
   
    static class Foo {}  
   
    static class Bar extends Foo {}  
}  
