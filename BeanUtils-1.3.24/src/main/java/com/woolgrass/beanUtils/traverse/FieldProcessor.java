package com.woolgrass.beanUtils.traverse;

public interface FieldProcessor {
	void processRootObject(RootObjectContext context);
	//void postProcessRootObject(RootObjectContext context);
	
	void processSimpleField(FieldContext context, String contextPath);
	//void postProcessSimpleField(TargetContext context);
	
	void processCompositeField(FieldContext context, String contextPath);
	//void postProcessCompositeField(FieldContext context);
	
//	void processCollectionField(TargetContext context);
//	void postProcessCollectionField(TargetContext context);
	
	void processArrayField(ArrayFieldContext context, String contextPath);
	void processListField(ListFieldContext context, String contextPath);
	void processSetField(SetFieldContext context, String contextPath);
	void processMapField(MapFieldContext context, String contextPath);
//	void postProcessMapField(TargetContext context);
	
	void processSimpleArrayElement(ArrayElementContext context, String contextPath);
	void processComplexArrayElement(ArrayElementContext context, String contextPath);
	
	void processSimpleListElement(ListElementContext context, String contextPath);
	void processComplexListElement(ListElementContext context, String contextPath);
	
	void processSimpleSetElement(SetElementContext context, String contextPath);
	void processComplexSetElement(SetElementContext context, String contextPath);
	
	void processMapEntryElement(MapEntryContext context, String contextPath);
	
	void processSimpleMapKeyElement(MapKeyContext context, String contextPath);
	void processComplexMapKeyElement(MapKeyContext context, String contextPath);
	
	void processSimpleMapValueElement(MapValueContext context, String contextPath);
	void processComplexMapValueElement(MapValueContext context, String contextPath);
	
	void processRootObjectCrossReferenceField(TargetContext context, String contextPath);
	void processNonRootObjectCrossReferenceField(TargetContext context, String contextPath, String referredObjectPath);
}
