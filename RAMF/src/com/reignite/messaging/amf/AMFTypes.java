package com.reignite.messaging.amf;

/**
 * Holds all the constants for AMF
 * 
 * @author Surrey
 * 
 */
public interface AMFTypes {

	String NULL_STRING = "null";
	String EMPTY_STRING = "";
	int UNKNOWN_CONTENT_LENGTH = -1;

	int AMF0_NUMBER_TYPE = 0;
	int AMF0_BOOLEAN_TYPE = 1;
	int AMF0_STRING_TYPE = 2;
	int AMF0_OBJECT_TYPE = 3;
	int AMF0_MOVIECLIP_TYPE = 4;
	int AMF0_NULL_TYPE = 5;
	int AMF0_UNDEFINED_TYPE = 6;
	int AMF0_REFERENCE_TYPE = 7;
	int AMF0_ECMAARRAY_TYPE = 8;
	int AMF0_OBJECTEND_TYPE = 9;
	int AMF0_STRICTARRAY_TYPE = 10;
	int AMF0_DATE_TYPE = 11;
	int AMF0_LONGSTRING_TYPE = 12;
	int AMF0_UNSUPPORTED_TYPE = 13;
	int AMF0_RECORDSET_TYPE = 14;
	int AMF0_XMLOBJECT_TYPE = 15;
	int AMF0_TYPEDOBJECT_TYPE = 16;
	int AMF0_AVMPLUSOBJECT_TYPE = 17;

	int AMF3_UNDEFINED_TYPE = 0;
	int AMF3_NULL_TYPE = 1;
	int AMF3_FALSE_TYPE = 2;
	int AMF3_TRUE_TYPE = 3;
	int AMF3_INTEGER_TYPE = 4;
	int AMF3_DOUBLE_TYPE = 5;
	int AMF3_STRING_TYPE = 6;
	int AMF3_XML_TYPE = 7;
	int AMF3_DATE_TYPE = 8;
	int AMF3_ARRAY_TYPE = 9;
	int AMF3_OBJECT_TYPE = 10;
	int AMF3_AVMPLUSXML_TYPE = 11;
	int AMF3_BYTEARRAY_TYPE = 12;

	int AMFR_UNDEFINED_TYPE = 0;
 	int AMFR_NULL_TYPE = 1;
 	int AMFR_FALSE_TYPE = 2;
 	int AMFR_TRUE_TYPE = 3;
 	int AMFR_INTEGER_TYPE = 4;
 	int AMFR_DOUBLE_TYPE = 5;
 	int AMFR_STRING_TYPE = 6;
 	int AMFR_DATE_TYPE = 7;
 	int AMFR_ARRAY_TYPE = 8;
 	int AMFR_OBJECT_TYPE = 9;
	int AMFR_BYTEARRAY_TYPE = 10;
	int AMFR_LIST_TYPE = 11;
	int AMFR_SET_TYPE = 12;
	int AMFR_MAP_TYPE = 13;
	
	/**
	 * Internal use only
	 * 
	 * @exclude
	 */
	int UINT29_MASK = 0x1FFFFFFF; // 2^29 - 1

	/**
	 * The maximum value for an <code>int</code> that will avoid promotion to an
	 * ActionScript Number when sent via AMF 3 is 2<sup>28</sup> - 1, or
	 * <code>0x0FFFFFFF</code>.
	 */
	int INT28_MAX_VALUE = 0x0FFFFFFF; // 2^28 - 1

	/**
	 * The minimum value for an <code>int</code> that will avoid promotion to an
	 * ActionScript Number when sent via AMF 3 is -2<sup>28</sup> or
	 * <code>0xF0000000</code>.
	 */
	int INT28_MIN_VALUE = 0xF0000000; // -2^28 in 2^29 scheme

}
