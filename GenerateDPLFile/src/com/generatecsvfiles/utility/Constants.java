package com.generatecsvfiles.utility;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	public static final Integer FIRST_PAGE = 0;
	public static final Integer RECORDSPER_PAGE = 100;
	public static final String CSV_DATE_FORMAT = "MM/dd/yyyy";
	
	public static final String COUNTRY = "COUNTRY";
	public static final String STATE = "STATE";
	public static final String COUNTRY_OF_ORIGIN = "COUNTRY_OF_ORIGIN";
	public static final String TYPE = "TYPE";
	public static final String ENTITY_TYPE = "ENTITY_TYPE";
	public static final String DPL_ENTIRE_COUNTRY = "*ENTIRE COUNTRY*";
	public static final String REPLACE_BLANK_COUNTRY_CODE = "XX";

	public static final String YES = "Y";
	public static final String NO = "N";

	public static final String ACTIVE = "A";
	public static final String DEACTIVE = "D";

	public static final String DATE_REGEX = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{2,4}";

	public static final Map<String, String> ENTITY_TYPE_MAP = new HashMap<String, String>();


	public static final String PREVIOUS_PUBLISH_DATE_STATE = "PREVIOUS_DATE";
	public static final String LAST_PUBLISH_DATE_STATE = "CURRENT_DATE";

	static {
		// Add entries in ENTITY_TYPE_MAP
		ENTITY_TYPE_MAP.put("INDIVIDUAL", "INDIVIDUAL");
		ENTITY_TYPE_MAP.put("FIRM", "FIRM");
		ENTITY_TYPE_MAP.put("VESSEL", "VESSEL");
		ENTITY_TYPE_MAP.put("AIRCRAFT", "AIRCRAFT");
	}

	public static final String INCREMENTAL = "INCREMENTAL";
	public static final String ENTIRE = "ENTIRE";
	public static final String FILE_TYPE_ORACLE_GTM_CSV = "ORACLE_GTM_CSV";
	public static final String CUSTOM_FILENAME = "Custom";

	public static final String ZIP_NAME = "GTM_DPL_DATA.zip";
	
	

}
