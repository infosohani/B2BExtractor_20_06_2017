package com.businessextractor.ui.search;

import javax.swing.JPanel;

import com.businessextractor.services.search.query.QueryField;

/**
 * Data bindable abstract component used to specify a prototype for other visual components
 * that will help with search. 
 * @author user
 *
 */
public abstract class SearchComponent {
	
	// the field used for search to which this visual compoenent is bound
	private String field;
	
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	/**
	 * Standard constructor with bound field parameter
	 * @param field the field used for search to wich this visual compoenent is bound
	 */
	public SearchComponent(String field) {
		super();
		this.field = field;
	}

	/**
	 * Returns the queryField object that this component implementation resolves to
	 * @return A concrete implementation of a queryField
	 */
	public abstract QueryField getQueryField ();
	
}
