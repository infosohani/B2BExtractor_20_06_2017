package com.businessextractor.entity.directory;

import java.util.List;

import com.businessextractor.dao.DaoInterface;
import com.businessextractor.entity.websiteroot.WebsiteRoot;

/**
 * DAO for manipulating persistent Directory Objects.
 * @author Bogdan Vlad
 *
 */
public interface DirectoryDao extends DaoInterface<Directory> {
	
	/**
	 * Loads all directories market 'active' from  a webiste root
	 * @param websiteRoot The website root
	 * @return The list of active directories
	 */
	public List<Directory> loadAllActiveDirectoriesFromWebsiteRoot (WebsiteRoot websiteRoot); 
	
	/**
	 * Loads all directories from a website root
	 * @param websiteRoot The website root
	 * @return The list of directories
	 */
	public List<Directory> loadAllDirectoriesFromWebsiteRoot (WebsiteRoot websiteRoot);
	
	/**
	 * Checks if there already exists a directory with a specified source name and url
	 * @param sourceName The source name
	 * @param sourceURL The source url
	 * @return true if directory already exsits, false otherwise
	 */
	public boolean existsDirectoryWithSourceNameAndSourceURL ( String sourceName, String sourceURL );
	
}
