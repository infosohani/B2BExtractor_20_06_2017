package com.businessextractor.services.search.query;

/**
 * Range query field that will lead to a test verifying if a specific field is between 
 * 2 given values (fromValue and toValue).
 * @author user
 *
 */
public class RangeQueryField
	extends QueryField {

	// the starting value
	private String fromValue;
	
	// the ending value
	private String toValue;

	public String getFromValue() {
		return fromValue;
	}

	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}

	public String getToValue() {
		return toValue;
	}

	public void setToValue(String toValue) {
		this.toValue = toValue;
	}
	
}
