package com.businessextractor.services.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.Query;

import com.businessextractor.entity.business.Business;
import com.businessextractor.services.search.query.MultiFieldQuery;
import com.businessextractor.services.search.query.QueryField;
import org.springframework.transaction.annotation.Transactional;

/**
 * Controller used to issue a search command to the search engine and return the results.
 * @author Bogdan Vlad
 *
 */
public class SearchController {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(SearchController.class);

	
	// a list of all searcheable classes in the application
	private List<Class> searcheableClasses;
	
	// search engine that will be used to do the actual searching
	private SearchEngine searchEngine;
	
	/**
	 * Standard constructor that populates the searcheable classes field
	 */
	public SearchController(List<String> searcheableClassesAsStrings) {
		this.searcheableClasses = new ArrayList<Class>();
		for (String classAsString : searcheableClassesAsStrings ) {
			try {
				// load each class into the searcheable classes array
				searcheableClasses.add(Class.forName(classAsString));
			} catch (ClassNotFoundException e) {
				logger.info("Class not found " + classAsString, e );
			}
		}
	}
	
	/**
	 * Does a simple search on the engine's preset entities using the given query fields and paging criterion
	 * @param queryFields The quert fields that will be used to resolve this query
	 * @param pagingCriterion The paging criterion that will be used
	 * @return The searchResults object that the search produced
	 */
	public SearchResults searchByFields (List<QueryField> queryFields, PagingCriterion pagingCriterion) throws Exception {
		
		// translate the application level fields into  a lucene query
		MultiFieldQuery multiFieldQuery = new MultiFieldQuery(queryFields);
		Query luceneQuery = multiFieldQuery.toQuery();
		
		SearchCommand searchCommand = new SearchCommand(luceneQuery,pagingCriterion,searcheableClasses.toArray(new Class[0]));
		
		// do the actual search here
		SearchResults searchResults = searchEngine.search(searchCommand);
		
		// return results
		return searchResults;
	}
	
	/**
	 * Executes a search command and returns the results to the client.
	 * @param searchCommand The search command to execute
	 * @throws Exception In case a search exception occured
	 */
	@Transactional
	public SearchResults search( SearchCommand searchCommand )  throws Exception {
		
		// set the classes to be queried
		searchCommand.setClassesQueried(searcheableClasses);
		
		// execute the search command and return the results
		return searchEngine.search(searchCommand);
	}
	

	public SearchEngine getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

	public List<Class> getSearcheableClasses() {
		return searcheableClasses;
	}

	
}
