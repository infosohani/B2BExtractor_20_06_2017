package com.businessextractor.services.search.query;

import org.apache.lucene.search.BooleanClause;

/**
 * Abstract class used in assembling a clause in the lucene query.
 * @author user
 *
 */
public abstract class QueryField {

	// the field name that will be searched (ex:"description")
	private String field;
	
	// the occurance of this field in the application ( by default MUST )
	private BooleanClause.Occur occurance = BooleanClause.Occur.MUST;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public BooleanClause.Occur getOccurance() {
		return occurance;
	}

	public void setOccurance(BooleanClause.Occur occurance) {
		this.occurance = occurance;
	}
	
}
