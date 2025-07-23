package com.businessextractor.services.search.query;

import org.apache.lucene.search.Query;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A query implementation that resolves to a lucene query parsed with a query parser
 * This is the simplest form of query but also the least precise one.
 * @author user
 *
 */
public class StringQuery implements ApplicationQuery {

	
	// a query value string that will be parsed into a Lucene query 
	private String queryValue;
	
	public Query toQuery() throws Exception {
		
		return null;
	}

	/**
	 * Returns the query value
	 * @return The query value
	 */
	public String getQueryValue() {
		return queryValue;
	}

	/**
	 * Sets the query value
	 * @param queryValue The query value
	 */
	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("queryValue", this.queryValue)
			.toString();
	}

}
