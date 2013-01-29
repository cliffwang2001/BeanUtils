package com.woolgrass.beanUtils;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<Long, String> map = new HashMap<Long, String>();
		map.put((long) 1, "abc");
		map.put( 2L, "2abc");
		boolean hasKey = map.containsKey(1);
		System.out.println(hasKey);
		
		hasKey = map.containsKey(1L);
		System.out.println(hasKey);

	}

}
