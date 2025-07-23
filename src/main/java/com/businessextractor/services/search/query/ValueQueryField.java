package com.businessextractor.services.search.query;

public class ValueQueryField
	extends QueryField {

	// the value (ex:"human resources")
	private String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
}
