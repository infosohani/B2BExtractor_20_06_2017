package com.businessextractor.services.search;

/**
 * Paging criterion value object used to command the search engine
 *  to fetch a maximum of maxResults starting from initialResultOffset.
 * 
 * @author user
 *
 */
public class PagingCriterion {

	// the maximum number of results returned. By default 20
	private int maxResults = 20;
	
	// the offset of the first result in the result list. By default the firs object in the list
	private int initialResultOffset = 0;

	/**
	 * Returns the maximum number of results that can be returned by searches using this paging
	 * criterion
	 * @return The maximum number of searches using this paging criterion
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * Sets the maximum number of results that can be returned by searches using this paging
	 * criterion
	 * @param maxResults The maximum number of searches using this paging criterion
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * Returns the offset of the first search result obtained with this search engine
	 * @return The initial offset
	 */
	public int getInitialResultOffset() {
		return initialResultOffset;
	}

	/**
	 * Returns the offset of the first search result obtained with this search engine
	 * @return The initial offset
	 */
	public void setInitialResultOffset(int initialResultOffset) {
		this.initialResultOffset = initialResultOffset;
	}
	
}
