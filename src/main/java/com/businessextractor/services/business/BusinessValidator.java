package com.businessextractor.services.business;

import com.businessextractor.entity.business.Business;
import com.businessextractor.services.business.exception.BusinessValidationException;
import com.businessextractor.util.Validator;

/**
 * Concrete implementation of Validator that validates a Business and throws an exception if a field is not valid.
 * @author user
 *
 */
public class BusinessValidator implements Validator<Business, BusinessValidationException>{
	
	
	public void validate ( Business business ) throws BusinessValidationException {
		if (business.getBusinessTitle()==null || business.getBusinessTitle().equals("")) {
			throw new BusinessValidationException( "Field businessTitle cannot be empty " );
		}
		if (business.getBusinessDescription()==null || business.getBusinessDescription().equals("")) {
			throw new BusinessValidationException( "Field businessDescription cannot be empty " );
		}
		if (business.getBusinessDomain()==null || business.getBusinessDomain().equals("")) {
			throw new BusinessValidationException( "Field businessDomain cannot be empty " );
		}
	}
	
}
