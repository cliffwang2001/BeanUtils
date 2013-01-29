package com.woolgrass.beanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionFieldStruct {
	static public final String FIELD_NAME = "$(fieldname)";
	static public final String CLASS_NAME = "$(classname)";
	static public final String ANY_NAME = "$(anyname)";
	static public final String MAPENTRY = "entry";
	static public final String MAPKEY = "key";
	static public final String MAPVALUE = "value";
	
	protected ArrayStruct arrayStruct;
	protected ListStruct listStruct;
	protected SetStruct setStruct;
	protected MapStruct mapStruct;
	
	static public final CollectionFieldStruct NO_GROUPING_TAG;
	static public final CollectionFieldStruct FIELDNAME_AS_GROUPING_TAG;
	
	static {
		FIELDNAME_AS_GROUPING_TAG = new CollectionFieldStruct(ArrayStruct.FIELDNAME_AS_GROUPING_TAG,
				ListStruct.FIELDNAME_AS_GROUPING_TAG,
				SetStruct.FIELDNAME_AS_GROUPING_TAG,
				MapStruct.FIELDNAME_AS_GROUPING_TAG);
		
		NO_GROUPING_TAG = new CollectionFieldStruct(ArrayStruct.NO_GROUPING_TAG,
				ListStruct.NO_GROUPING_TAG,
				SetStruct.NO_GROUPING_TAG,
				MapStruct.NO_MAP_GROUPING);
	}
	
	public CollectionFieldStruct() {
		this.arrayStruct = ArrayStruct.FIELDNAME_AS_GROUPING_TAG;
		this.listStruct = ListStruct.FIELDNAME_AS_GROUPING_TAG;
		this.setStruct = SetStruct.FIELDNAME_AS_GROUPING_TAG;
		this.mapStruct = MapStruct.FIELDNAME_AS_GROUPING_TAG;
	}
	
	
	public CollectionFieldStruct(ArrayStruct arrayStruct,
			ListStruct listStruct, SetStruct setStruct, MapStruct mapStruct) {
		this.arrayStruct = arrayStruct;
		this.listStruct = listStruct;
		this.setStruct = setStruct;
		this.mapStruct = mapStruct;
	}
	
	
	

	public ArrayStruct getArrayStruct() {
		return arrayStruct;
	}


	public ListStruct getListStruct() {
		return listStruct;
	}


	public SetStruct getSetStruct() {
		return setStruct;
	}


	public MapStruct getMapStruct() {
		return mapStruct;
	}
	
	
	public void setArrayStruct(ArrayStruct arrayStruct) {
		this.arrayStruct = arrayStruct;
	}


	public void setListStruct(ListStruct listStruct) {
		this.listStruct = listStruct;
	}


	public void setSetStruct(SetStruct setStruct) {
		this.setStruct = setStruct;
	}


	public void setMapStruct(MapStruct mapStruct) {
		this.mapStruct = mapStruct;
	}




	public static class ArrayStruct {
		protected List<String> arrayStruct = new ArrayList<String>();
		protected List<String> elementStruct = new ArrayList<String>();
		
		public static final ArrayStruct NO_GROUPING_TAG;
		public static final ArrayStruct FIELDNAME_AS_GROUPING_TAG; 
		
		static {
			NO_GROUPING_TAG = new ArrayStruct();
			NO_GROUPING_TAG.setElementStruct(Arrays.asList(FIELD_NAME));
			
			FIELDNAME_AS_GROUPING_TAG = new ArrayStruct();
			FIELDNAME_AS_GROUPING_TAG.setArrayStruct(Arrays.asList(FIELD_NAME));
			FIELDNAME_AS_GROUPING_TAG.setElementStruct(Arrays.asList(CLASS_NAME));
		}
		
		public ArrayStruct() {
//			this.arrayStruct = Arrays.asList(FIELD_NAME);
//			this.elementStruct = Arrays.asList(CLASS_NAME);
//			this.elementStruct = Arrays.asList(ANY_NAME);
		}


		public ArrayStruct(List<String> arrayStruct, List<String> elementStruct) {
			super();
			this.arrayStruct = arrayStruct;
			this.elementStruct = elementStruct;
		}


		public List<String> getArrayStruct() {
			return arrayStruct;
		}


		public void setArrayStruct(List<String> arrayStruct) {
			this.arrayStruct = arrayStruct;
		}


		public List<String> getElementStruct() {
			return elementStruct;
		}


		public void setElementStruct(List<String> elementStruct) {
			this.elementStruct = elementStruct;
		}
		
		public boolean isArrayStructEmpty() {
			return arrayStruct.isEmpty();
		}
		
		
	}
	
	public static class ListStruct {
		protected List<String> listStruct = new ArrayList<String>();
		protected List<String> elementStruct = new ArrayList<String>();
		
		public static final ListStruct NO_GROUPING_TAG;
		public static final ListStruct FIELDNAME_AS_GROUPING_TAG; 
		
		static {
			NO_GROUPING_TAG = new ListStruct();
			NO_GROUPING_TAG.setElementStruct(Arrays.asList(FIELD_NAME)); 
			
			FIELDNAME_AS_GROUPING_TAG = new ListStruct();
			FIELDNAME_AS_GROUPING_TAG.setListStruct(Arrays.asList(FIELD_NAME));
			FIELDNAME_AS_GROUPING_TAG.setElementStruct(Arrays.asList(CLASS_NAME));
		}
		
		public ListStruct() {
//			this.elementStruct = Arrays.asList(ANY_NAME);
		}


		public ListStruct(List<String> listStruct, List<String> elementStruct) {
			super();
			this.listStruct = listStruct;
			this.elementStruct = elementStruct;
		}


		public List<String> getListStruct() {
			return listStruct;
		}


		public void setListStruct(List<String> listStruct) {
			this.listStruct = listStruct;
		}


		public List<String> getElementStruct() {
			return elementStruct;
		}


		public void setElementStruct(List<String> elementStruct) {
			this.elementStruct = elementStruct;
		}
		
		
	}
	
	public static class SetStruct {
		protected List<String> setStruct = new ArrayList<String>();
		protected List<String> elementStruct = new ArrayList<String>();
		
		public static final SetStruct NO_GROUPING_TAG;
		public static final SetStruct FIELDNAME_AS_GROUPING_TAG; 
		
		static {
			NO_GROUPING_TAG = new SetStruct();
			NO_GROUPING_TAG.setElementStruct(Arrays.asList(FIELD_NAME));
			
			FIELDNAME_AS_GROUPING_TAG = new SetStruct();
			FIELDNAME_AS_GROUPING_TAG.setSetStruct(Arrays.asList(FIELD_NAME));
			FIELDNAME_AS_GROUPING_TAG.setElementStruct(Arrays.asList(CLASS_NAME));
		}
		
		public SetStruct() {
//			this.elementStruct = Arrays.asList(ANY_NAME);
		}

		public SetStruct(List<String> setStruct, List<String> elementStruct) {
			super();
			this.setStruct = setStruct;
			this.elementStruct = elementStruct;
		}

		public List<String> getSetStruct() {
			return setStruct;
		}

		public void setSetStruct(List<String> setStruct) {
			this.setStruct = setStruct;
		}

		public List<String> getElementStruct() {
			return elementStruct;
		}

		public void setElementStruct(List<String> elementStruct) {
			this.elementStruct = elementStruct;
		}
		
		
	}
	
	public static class MapStruct {
		
		protected List<String> mapStruct = new ArrayList<String>();
		protected List<String> entryStruct = new ArrayList<String>();
		protected List<String> keyStruct = new ArrayList<String>();
		protected List<String> valueStruct = new ArrayList<String>();
		
		public static final MapStruct FIELDNAME_AS_GROUPING_TAG;
		public static final MapStruct NO_ENTRY_GROUPING;
		public static final MapStruct NO_MAP_GROUPING;
		
		static {
			FIELDNAME_AS_GROUPING_TAG = new MapStruct();
			FIELDNAME_AS_GROUPING_TAG.setMapStruct(Arrays.asList(FIELD_NAME));
			FIELDNAME_AS_GROUPING_TAG.setEntryStruct(Arrays.asList(MAPENTRY));
			FIELDNAME_AS_GROUPING_TAG.setKeyStruct(Arrays.asList(CLASS_NAME));
			FIELDNAME_AS_GROUPING_TAG.setValueStruct(Arrays.asList(CLASS_NAME));
			
			NO_ENTRY_GROUPING = new MapStruct();
			NO_ENTRY_GROUPING.setMapStruct(Arrays.asList(FIELD_NAME));
			NO_ENTRY_GROUPING.setKeyStruct(Arrays.asList(CLASS_NAME));
			NO_ENTRY_GROUPING.setValueStruct(Arrays.asList(CLASS_NAME));
			
			NO_MAP_GROUPING = new MapStruct();
			NO_MAP_GROUPING.setEntryStruct(Arrays.asList(FIELD_NAME));
			NO_MAP_GROUPING.setKeyStruct(Arrays.asList(CLASS_NAME));
			NO_MAP_GROUPING.setValueStruct(Arrays.asList(CLASS_NAME));
		}
		
		public MapStruct() {
//			this.entryStruct = Arrays.asList("entry");
//			this.keyStruct = Arrays.asList("key", ANY_NAME);
//			this.valueStruct = Arrays.asList("value", ANY_NAME);
		}


		public MapStruct(List<String> mapStruct, List<String> entryStruct,
				List<String> keyStruct, List<String> valueStruct) {
			super();
			this.mapStruct = mapStruct;
			this.entryStruct = entryStruct;
			this.keyStruct = keyStruct;
			this.valueStruct = valueStruct;
		}


		public List<String> getMapStruct() {
			return mapStruct;
		}


		public void setMapStruct(List<String> mapStruct) {
			this.mapStruct = mapStruct;
		}


		public List<String> getEntryStruct() {
			return entryStruct;
		}


		public void setEntryStruct(List<String> entryStruct) {
			this.entryStruct = entryStruct;
		}


		public List<String> getKeyStruct() {
			return keyStruct;
		}


		public void setKeyStruct(List<String> keyStruct) {
			this.keyStruct = keyStruct;
		}


		public List<String> getValueStruct() {
			return valueStruct;
		}


		public void setValueStruct(List<String> valueStruct) {
			this.valueStruct = valueStruct;
		}


	}
}
