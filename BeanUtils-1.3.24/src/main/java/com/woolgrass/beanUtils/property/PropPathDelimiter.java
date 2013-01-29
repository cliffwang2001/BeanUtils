package com.woolgrass.beanUtils.property;

import java.util.List;
import java.util.StringTokenizer;


public enum PropPathDelimiter {
	NESTED('.') {
		@Override
		public void parse(PropertyPathResolver resolver) {
			List<PropertyContext> resolvedList = resolver.getResolvedProperties();
//			if(resolvedList.size() == 0)
//				throw new IllegalStateException("Path can't start with \".\"");
			StringTokenizer remaining = resolver.getRemainingPath();
			if(!remaining.hasMoreTokens())
				throw new IllegalStateException("Path can't end with \".\"");
			String nextToken = remaining.nextToken();
			if(nextToken.length() == 1 && findDelimiter(nextToken.charAt(0)) != null)
				throw new IllegalStateException("Found " + nextToken + " immediately after \".\"");
			resolvedList.add(new PropertyContext(nextToken, PropertyType.NESTED));	
			
		}
	},
	MAPPED_OPEN('(') {
		@Override
		public void parse(PropertyPathResolver resolver) {
			StringTokenizer remaining = resolver.getRemainingPath();
			if(!remaining.hasMoreTokens())
				throw new IllegalStateException("Path can't end with \"(\"");
			String nextToken = remaining.nextToken();
			if(nextToken.length() == 1 && findDelimiter(nextToken.charAt(0)) != null)
				throw new IllegalStateException("Found " + nextToken + " immediately after \"(\"");
			if(!remaining.hasMoreTokens())
				throw new IllegalStateException("Path can't end without \")\"");
			String thridToken = remaining.nextToken();
			if(thridToken.length() != 1 || !MAPPED_CLOSE.match(thridToken.charAt(0)))
				throw new IllegalStateException("Expecting \")\"");
			List<PropertyContext> resolvedList = resolver.getResolvedProperties();
			resolvedList.add(new PropertyContext(nextToken, PropertyType.MAPPED));	
		}
	},
	MAPPED_CLOSE(')') {

		@Override
		public void parse(PropertyPathResolver resolver) {
			throw new UnsupportedOperationException("\")\" is parsed when parsing \"(\"");
			
		}
	},
	INDEXED_OPEN('[') {
		@Override
		public void parse(PropertyPathResolver resolver) {
			StringTokenizer remaining = resolver.getRemainingPath();
			if(!remaining.hasMoreTokens())
				throw new IllegalStateException("Path can't end with \"[\"");
			String nextToken = remaining.nextToken();
			if(nextToken.length() == 1 && findDelimiter(nextToken.charAt(0)) != null)
				throw new IllegalStateException("Found " + nextToken + " immediately after \"[\"");
			if(!remaining.hasMoreTokens())
				throw new IllegalStateException("Path can't end without \")\"");
			String thridToken = remaining.nextToken();
			if(thridToken.length() != 1 || !INDEXED_CLOSE.match(thridToken.charAt(0)))
				throw new IllegalStateException("Expecting \"]\"");
			List<PropertyContext> resolvedList = resolver.getResolvedProperties();
			resolvedList.add(new PropertyContext(nextToken, PropertyType.INDEXED));	
			
		}
	},
	INDEXED_CLOSE(']') {

		@Override
		public void parse(PropertyPathResolver resolver) {
			throw new UnsupportedOperationException("\"]\" is parsed when parsing \"[\"");
			
		}
	};
	
	protected char delimiter;
	
	public char getDelimiter() {
		return delimiter;
	}


	PropPathDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}
	
	public abstract void parse(PropertyPathResolver resolver);
	
	public boolean match(String st) {
		if(st == null)
			throw new IllegalArgumentException("Argument st is null");
		if(st.length() != 1)
			return false;
		char ch =st.charAt(0);
		return match(ch);
	}
	
	public boolean match(char ch) {
		if(this.delimiter == ch)
			return true;
		else
			return false;
	}
	
	public static String getAllDelimitersStr() {
		StringBuilder sb = new StringBuilder();
		for(PropPathDelimiter delimiter : values()) {
			sb.append(delimiter.getDelimiter());
		}
		return sb.toString();
	}
	
	public static PropPathDelimiter findDelimiter(char ch) {
		for(PropPathDelimiter elem : values()) {
			if(elem.delimiter == ch)
				return elem;
		}
		return null;
	}
}
