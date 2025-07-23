package com.businessextractor.services.search.query;

import java.util.ArrayList;
import java.util.List;

import com.businessextractor.services.search.query.QueryField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.BytesRef;

/**
 * A query implementation that resolves to a Lucene Boolean Query containing multiple terms.
 * @author user
 */
public class MultiFieldQuery implements ApplicationQuery {

	// the query fields list used in creating the boolean query
	private List<QueryField> queryFields;

	public Query toQuery() throws Exception {

		// creates a new query
		BooleanQuery booleanQuery = new BooleanQuery();

		// add the query fields to the query
		for (QueryField queryField : queryFields) {

			if (queryField instanceof ValueQueryField) {
				ValueQueryField valueQueryField = (ValueQueryField) queryField;
				TermQuery termQuery = new TermQuery(new Term(valueQueryField.getField(), valueQueryField.getValue()));
				BooleanClause booleanClause = new BooleanClause(termQuery, queryField.getOccurance());
				booleanQuery.add(booleanClause);
			} else if (queryField instanceof RangeQueryField) {
				RangeQueryField rangeQueryField = (RangeQueryField) queryField;

				// Use TermRangeQuery instead of RangeQuery
				TermRangeQuery rangeQuery = new TermRangeQuery(
						rangeQueryField.getField(),
						new BytesRef(rangeQueryField.getFromValue()),
						new BytesRef(rangeQueryField.getToValue()),
						true, // include lower bound
						true  // include upper bound
				);

				BooleanClause booleanClause = new BooleanClause(rangeQuery, queryField.getOccurance());
				booleanQuery.add(booleanClause);
			}
		}

		// return the query used in the search engine
		return booleanQuery;
	}

	/**
	 * Standard constructor providing the list of query field objects
	 * @param queryFields
	 */
	public MultiFieldQuery(List<QueryField> queryFields) {
		super();
		this.queryFields = queryFields;
	}
}
