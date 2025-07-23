package com.businessextractor.entity.business;

import com.businessextractor.dao.DaoInterface;

/**
 * Dao interface for manipulating Business objects.
 * @author Bogdan Vlad
 *
 */
public interface BusinessDao
	extends DaoInterface<Business> {

	/**
	 * Loads all businesses with specified title and domain name.
	 * @param businessTitle
	 * @param businessDomain
	 * @return
	 */
	public Business loadBusinessByTitleAndDomain ( String businessTitle, String businessDomain );
	
	/**
	 * Checks to see if there exists a business with the specified title and domain name.
	 * @param businessTitle
	 * @param businessDomain
	 * @return
	 */
	public boolean existsBusinessWithTitleAndDomain ( String businessTitle, String businessDomain );
	
        /**
	 * Use for get MAX Result from Bisiness table
	 * @return
	 */

        long getGeneratedID();
	

}
