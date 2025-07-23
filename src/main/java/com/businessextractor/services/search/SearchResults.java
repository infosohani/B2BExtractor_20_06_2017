package com.businessextractor.services.search;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

/**
 * Value object used to hold all results of a search that will be returned do the 
 * above layer by the GenericSearchEngine.
 * @author user
 *
 */
@SuppressWarnings(value="unchecked")
public class SearchResults {

	// the list of search results
	private List results;
	
	// the total result count that was found
	private int totalResultCount;
	
	// the initial paging criterion used to do the search ( this helps determine the page posittion )
	private PagingCriterion pagingCriterion;

	// the query that originated the search ( kept here in case it needs to be reused in a automatic search such as nextPage/previousPage etc.)
	private Query query; 
	
	// the sorting option for the query ( kept here in case it needs to be reused in a automatic search such as nextPage/previousPage etc.)
	private Sort sort;

	/**
	 * Returns the sort option that was used to generate the results.
	 * @return The sort option
	 */
	public Sort getSort() {
		return sort;
	}

	/**
	 * Sets the sort option that was used to generate the results.
	 * @param sort The sort option
	 */
	public void setSort(Sort sort) {
		this.sort = sort;
	}

	/**
	 * Returns the actual result list.
	 * @return
	 */
	public List getResults() {
		return results;
	}

	/**
	 * Sets the actual result list.
	 * @param results
	 */
	public void setResults(List results) {
		this.results = results;
	}

	/**
	 * Returns the total result count.
	 * @return
	 */
	public int getTotalResultCount() {
		return totalResultCount;
	}

	/**
	 * Sets the total result count.
	 * @param totalResultCount
	 */
	public void setTotalResultCount(int totalResultCount) {
		this.totalResultCount = totalResultCount;
	}

	/**
	 * Returns the paging criterion used to do the search.
	 * @return
	 */
	public PagingCriterion getPagingCriterion() {
		return pagingCriterion;
	}
	
	/**
	 * Sets the paginig criterion used to do the search
	 * @param pagingCriterion
	 */
	public void setPagingCriterion(PagingCriterion pagingCriterion) {
		this.pagingCriterion = pagingCriterion;
	}

	/**
	 * Returns the query used to do the search.
	 * @return
	 */
	public Query getQuery() {
		return query;
	}

	/**
	 * Sets the query used to do the search.
	 * @param query
	 */
	public void setQuery(Query query) {
		this.query = query;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("query", this.query)
			.append("pagingCriterion", this.pagingCriterion)
			.append("sort", this.sort)
			.append("results", this.results)
			.append("totalResultCount", this.totalResultCount)
			.toString();
	}
	
	
}
