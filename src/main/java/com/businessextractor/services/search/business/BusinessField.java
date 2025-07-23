package com.businessextractor.services.search.business;

import java.util.Date;

/**
 * Enumeration stating information about all the searcheable fields of a Business object.
 * @author user
 *
 */
public enum BusinessField {
	BUSINESS_TITLE("businessTitle","Business Title:",String.class),
	BUSINESS_DESCRIPTION("businessDescription","Business Description:",String.class),
	BUSINESS_DOMAIN("businessDomain","Business Domain:",String.class),
	CONTACT_FIRST_NAME("contactFirstName","Contact First Name:",String.class),
	CONTACT_LAST_NAME("contactLastName","Contact Last Name:",String.class),
	CONTACT_EMAIL("contactEmail","Contact Email:",String.class),
	FAX("fax","Fax:",String.class),
	PHONE("phone","Phone:",String.class),
	CELLPHONE("cellPhone","Cell Phone:",String.class),
	ADDRESS("address","Address:",String.class),
	CITY("city","City:",String.class),
	STATE("state","State:",String.class),
	PROVINCE("province","Province:",String.class),
	DIRECTORT_SOURCE_NAME("directory.sourceName","Directory Source Name:",String.class),
	IMPORT_DATE("importDate","Import Date:",Date.class),
	REVISED_DATE("revisedDate","Revised Date:",Date.class);
	
	// the field as stored in the Lucene Index
	private String field;
	// the label for this field shown in the view
	private String label;
	// the class representing the field
	private Class clazz;
	
	/**
	 * Getter for the field name
	 * @return The field name
	 */
	public String getField() {
		return field;
	}

	/**
	 * Setter for the field name
	 * @param field The field name
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * Returns the label for the component
	 * @return The label used in the component rendering
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label for the component rendering
	 * @param label The label for the component rendering
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Returns the class for used for the component rendering
	 * @return The class used for the component rendering
	 */
	public Class getClazz() {
		return clazz;
	}

	/**
	 * Sets the class used in the component rendering
	 * @param clazz The class used in the component rendering
	 */
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	/**
	 * Standard constructor with field name, label and class
	 * @param field The field name
	 * @param label The label for the field
	 * @param clazz The class
	 */
	private BusinessField(String field, String label, Class clazz) {
		this.field = field;
		this.label = label;
		this.clazz = clazz;
	}
}
