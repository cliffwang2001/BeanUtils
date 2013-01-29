package com.woolgrass.beanUtils.property;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class PropertyPathResolver {

	protected String propPath;
	protected List<PropertyContext> resolvedProperties = new ArrayList<PropertyContext>();
	protected StringTokenizer remainingPath ;
	
	public PropertyPathResolver(String propPath) {
		this.propPath = propPath;
		this.remainingPath = new StringTokenizer(propPath, PropPathDelimiter.getAllDelimitersStr(), true);
	}
	
	public void resolve() {
		while(remainingPath.hasMoreTokens()) {
			boolean isFirstToken = resolvedProperties.size() == 0;
			String token = remainingPath.nextToken();
			if(PropPathDelimiter.NESTED.match(token)) {
				if(isFirstToken)
					throw new IllegalStateException("Path can't start with \".\"");
				else
					PropPathDelimiter.NESTED.parse(this);
			}
			else if(PropPathDelimiter.MAPPED_OPEN.match(token)) {
				PropPathDelimiter.MAPPED_OPEN.parse(this);
			}
			else if(PropPathDelimiter.MAPPED_CLOSE.match(token)) {
				if(isFirstToken)
					throw new IllegalStateException("Path can't start with \"(\"");
				else
					if(isFirstToken)
						throw new IllegalStateException("\")\" needs to be proceded with \"(\"");
			}
			else if(PropPathDelimiter.INDEXED_OPEN.match(token)) {
				PropPathDelimiter.INDEXED_OPEN.parse(this);
			}
			else if(PropPathDelimiter.INDEXED_CLOSE.match(token)) {
				if(isFirstToken)
					throw new IllegalStateException("Path can't start with \"]\"");
				else
					if(isFirstToken)
						throw new IllegalStateException("\"]\" needs to be proceded with \"[\"");
			}
			else {
				if(isFirstToken)
					resolvedProperties.add(new PropertyContext(token, PropertyType.FIRSTSIMPLE));
				else
					throw new IllegalStateException("Expecting a delimiter.");
			}
		}
	}

	public String getPropPath() {
		return propPath;
	}
	
	public List<PropertyContext> getResolvedProperties() {
		return resolvedProperties;
	}

	public StringTokenizer getRemainingPath() {
		return remainingPath;
	}
}
