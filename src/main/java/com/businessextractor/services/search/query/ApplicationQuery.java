package com.businessextractor.services.search.query;

import org.apache.lucene.search.Query;

/**
 * An application query that provides the base for resolving subclasses of this class to
 * to lucene queries
 * @author user
 *
 */
public interface ApplicationQuery {


	/**
	 * Implemented by child subclasses to resolve themselves to lucene queries
	 * @return The analogous lucene query that will be used for search.
	 */
	public Query toQuery () throws Exception;
	
	
}
