package com.businessextractor.services.business;

import java.util.Date;

import com.businessextractor.spring.ApplicationContextHolder;
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
public class BusinessManipulationService {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(BusinessManipulationService.class);

	// DAO for manipulating business objects
	private BusinessDao businessDao;
	
	// reference to a singleton validator used for validating businesses
	private BusinessValidator businessValidator;
	
	/**
	 * Saves a business to persistent storage if it passes validation
	 * @param business The business to save
	 * @throws BusinessValidationException A businessValidationException
	 */
	public void saveBusiness ( Business business ) throws BusinessValidationException {
		
		try {
			// validate the business ( throws exception if validation vails )
			businessValidator.validate(business);
			this.businessDao = (BusinessDao) ApplicationContextHolder.getContext().getBean("businessDao");
			// set the import date on the business to now
			business.setImportDate(new Date());
			
			// save the business to persistent storage
			businessDao.save(business);
		} catch (BusinessValidationException e) {
			// throw the exception to the view for display
			throw e;
		}
		
	}
	
	/**
	 * Updates a business if it passes validation
	 * @param business The business to update
	 * @throws BusinessValidationException
	 */
	public void updateBusiness ( Business business ) throws BusinessValidationException {
		
		try {
			// validate the business ( throws exception if validation fails 
			businessValidator.validate(business);
			
			// set the revised date on the business to now
			business.setRevisedDate(new Date());
			
			// save the business to persistent storage
			businessDao.update(business);
		} catch (BusinessValidationException e) {
			// throw the exception to the view for display
			throw e;
		}
		
	}
	
	/**
	 * Removes a business from persistent storage.
	 * @param business The business that is to be removed from persistent storage
	 */
	public void deleteBusiness (Business business )  {
		
		// call DAO to remove business
		businessDao.delete(business);
		
	}

	public BusinessDao getBusinessDao() {
		return businessDao;
	}

	public void setBusinessDao(BusinessDao businessDao) {
		this.businessDao = businessDao;
	}

	public BusinessValidator getBusinessValidator() {
		return businessValidator;
	}

	public void setBusinessValidator(BusinessValidator businessValidator) {
		this.businessValidator = businessValidator;
	}
	
}
