package com.businessextractor.entity.websiteroot;

import java.util.List;

import com.businessextractor.dao.DaoInterface;

/**
 * Interface extension containing methods specific for handling this entity ( Website Root ).
 * @author user
 *
 */
public interface WebsiteRootDao extends  DaoInterface<WebsiteRoot> {

	/**
	 * Loads all the current website roots.
	 * @return The list of loaded website roots
	 */
	public List<WebsiteRoot> loadAll();
	
}
