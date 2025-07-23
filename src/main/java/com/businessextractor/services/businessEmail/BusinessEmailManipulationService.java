/*
 * BusinessEmailManipulationService.java
 *
 * Created on July 31, 2009, 12:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.businessextractor.services.businessEmail;

/**
 *
 * @author Avibha
 */

import com.businessextractor.entity.businessemail.BusinessEmail;
import com.businessextractor.entity.businessemail.BusinessEmailDao;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.businessextractor.entity.business.Business;
import com.businessextractor.entity.business.BusinessDao;
import com.businessextractor.services.business.exception.BusinessValidationException;

/**
 * This service was introduced because manipulation of business objects ( sava,update and delete )
 * may imply multiple transactional resources and validations. This service provides a bridge between the view's
 * action controllers and the Data Access Objects controlling access to the persistent businesses.
 * @author user
 *
 */
public class BusinessEmailManipulationService {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(BusinessEmailManipulationService.class);

	// DAO for manipulating business objects
	private BusinessEmailDao businessEmailDao;
	
	
	/**
	 * Saves a business to persistent storage if it passes validation
	 * @param business The business to save
	 * @throws BusinessValidationException A businessValidationException
	 */
	public void saveBusinessEmail(BusinessEmail businessEmail) throws Exception {
		
		try {
                        // save the business to persistent storage
			businessEmailDao.save(businessEmail);
                        
		} catch (Exception e) {
			// throw the exception to the view for display
			throw e;
		}
		
	}
	
	/**
	 * Updates a business if it passes validation
	 * @param business The business to update
	 * @throws BusinessValidationException
	 */
	public void updateBusiness(BusinessEmail businessEmail) throws Exception {
		
		try {			
			// save the business to persistent storage
			businessEmailDao.update(businessEmail);
                        
		} catch (Exception e) {
			// throw the exception to the view for display
			throw e;
		}
		
	}
	
	/**
	 * Removes a business from persistent storage.
	 * @param business The business that is to be removed from persistent storage
	 */
	public void deleteBusiness(BusinessEmail businessEmail)  {
		
		// call DAO to remove business
		businessEmailDao.delete(businessEmail);
		
	}

	public BusinessEmailDao getBusinessEmailDao() {
		return businessEmailDao;
	}

	public void setBusinessEmailDao(BusinessEmailDao businessEmailDao) {
		this.businessEmailDao = businessEmailDao;
	}	
	
}
