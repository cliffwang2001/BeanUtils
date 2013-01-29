package com.woolgrass.beanUtils.traverse;

import com.woolgrass.beanUtils.ObjectCategory;
import com.woolgrass.beanUtils.objectTracker.ObjectTracker;

public class NonSimpleObjectReferenceChecker {
	public static ObjecReferenceInfo getReferenceInfo(ObjectTracker tracker, TargetContext fieldContext ) {
		ObjecReferenceInfo refInfo = new ObjecReferenceInfo();
		Object targetObject = fieldContext.getValue();
		ObjectCategory	category = fieldContext.getCategory();
		
		if(targetObject == null ||category == ObjectCategory.UNKNOWN 
			|| category == ObjectCategory.SIMPLE || category == ObjectCategory.MAPENTRY) {
			refInfo.exists = false;
		}
		else {
			boolean isRootObj = tracker.getRootObj() == targetObject;
			if(isRootObj) {
				refInfo.exists = true;
				refInfo.referenceRoot = true;
			}else {
				String path = tracker.getPathFromObject(targetObject);
				if(path != null) {
					refInfo.exists = true;
	//				refInfo.referredObjectPath = path.replace(".", "\\");
					refInfo.referredObjectPath = path;
				}
			}
			if(refInfo.exists == true) {
				refInfo.referringObjectPath = tracker.getTopObjectPath();
			}
		}
		return refInfo;
	}
	
	
	public static class ObjecReferenceInfo {
		protected TargetContext fieldContext;
		protected ObjectTracker tracker;
		protected boolean exists;
		protected boolean referenceRoot;
		protected String referringObjectPath;
		protected String referredObjectPath;
		
		public TargetContext getFieldContext() {
			return fieldContext;
		}
		public ObjectTracker getTracker() {
			return tracker;
		}
		public boolean isExists() {
			return exists;
		}
		public boolean isReferenceRoot() {
			return referenceRoot;
		}
		public String getReferringObjectPath() {
			return referringObjectPath;
		}
		public String getReferredObjectPath() {
			return referredObjectPath;
		}
		
		
	}
	
}
