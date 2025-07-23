package com.businessextractor.services.search;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Search engine implementation that can be used to search and retrieve any
 * PERSISTENT and INDEXED entity in the application.
 */
@Transactional
public class SearchEngine {

    private final SessionFactory sessionFactory;

    @Autowired
    public SearchEngine(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public SearchResults search(final SearchCommand searchCommand) {
        // create the search results object
        final SearchResults searchResults = new SearchResults();
        searchResults.setPagingCriterion(searchCommand.getPagingCriterion());
        searchResults.setQuery(searchCommand.getQuery());

        Session session = sessionFactory.getCurrentSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(
                searchCommand.getQuery(),
                searchCommand.getClassesQueried().toArray(new Class[0])
        );

        fullTextQuery.setFirstResult(searchCommand.getPagingCriterion().getInitialResultOffset());
        fullTextQuery.setMaxResults(searchCommand.getPagingCriterion().getMaxResults());

        if (searchCommand.getSort() != null) {
            fullTextQuery.setSort(searchCommand.getSort());
            searchResults.setSort(searchCommand.getSort());
        }

        // get the result size
        int resultCount = fullTextQuery.getResultSize();
        searchResults.setTotalResultCount(resultCount);

        // get the result items
        List results = fullTextQuery.list();
        searchResults.setResults(results);

        return searchResults;
    }
}
