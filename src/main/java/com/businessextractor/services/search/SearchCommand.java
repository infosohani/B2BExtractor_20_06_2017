package com.businessextractor.services.search;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.lucene.search.Query;

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.search.Sort;


/**
 * Command object encapsulating all the necessary information do do a search.
 * @author user
 *
 */
@SuppressWarnings(value="unchecked")
public class SearchCommand {

	// the result classes
	private List<Class> classesQueried = new ArrayList<Class>();
	
	// the query used for searching
	private Query query;
	
	// optional sorting criterion
	private Sort sort;
	
	// the paging criterion used for searching
	private PagingCriterion pagingCriterion;

	public List<Class> getClassesQueried() {
		return classesQueried;
	}

	public void setClassesQueried(List<Class> classesQueried) {
		this.classesQueried = classesQueried;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public PagingCriterion getPagingCriterion() {
		return pagingCriterion;
	}

	public void setPagingCriterion(PagingCriterion pagingCriterion) {
		this.pagingCriterion = pagingCriterion;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}
	/**
	 * Standard constructor for the search command
	 * @param query - The query that will be executed
	 * @param pagingCriterion - The pagingCriterion that will be used
	 * @param classes - The classes on which the queries are to be executed
	 */
	public SearchCommand(Query query, PagingCriterion pagingCriterion,Class[] classes) {
		super();
		this.query = query;
		this.pagingCriterion = pagingCriterion;
		// defines all classes queried
		for (Class clazz : classes ) {
			this.classesQueried.add(clazz);
		}
	}
	
	/**
	 * This constructor is used when the queried classes are not known at the moment of instantiation
	 * @param query The query that will be executed
	 * @param pagingCriterion The paging criterion that will be used
	 */
	public SearchCommand(Query query, PagingCriterion pagingCriterion) {
		super();
		this.query = query;
		this.pagingCriterion = pagingCriterion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("query", this.query)
			.append("pagingCriterion", this.pagingCriterion)
			.append("sort", this.sort)
			.append("classesQueried", this.classesQueried)
			.toString();
	}


	
}
