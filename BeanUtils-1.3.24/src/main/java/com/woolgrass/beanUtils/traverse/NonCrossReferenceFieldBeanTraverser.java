package com.woolgrass.beanUtils.traverse;

import com.woolgrass.beanUtils.traverse.NonSimpleObjectReferenceChecker.ObjecReferenceInfo;

public class NonCrossReferenceFieldBeanTraverser extends BeanTraverser{

	public NonCrossReferenceFieldBeanTraverser(Object targetBean,
			FieldProcessor inspector) {
		super(targetBean, inspector);
	}

	@Override
	protected void preProcessCrossReferenceField(ObjecReferenceInfo refInfo) {
		if(refInfo.isExists()) {
			String referringPath = refInfo.getReferringObjectPath();
			if(refInfo.isReferenceRoot()) {
				throw new IllegalStateException("There is cross reference property. Referring path: " + referringPath
						+ "; Referred path: \\");
			}else {
				String referredPath = refInfo.getReferredObjectPath();
				throw new IllegalStateException("There is cross reference property. Referring path: " + referringPath
						+ "; Referred path: " + referredPath);
			}
		}
	}

}
