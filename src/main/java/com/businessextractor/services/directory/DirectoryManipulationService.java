package com.businessextractor.services.directory;
import com.businessextractor.entity.directory.Directory;
import com.businessextractor.entity.directory.DirectoryDao;
import com.businessextractor.services.directory.exception.DirectoryValidationException;

import javax.transaction.Transactional;

/**
 * Service used for the manipulation of Directory objects.
 * @author user
 *
 */
public class DirectoryManipulationService {

	// DAO for manipulating persistent Directory objects
	private DirectoryDao directoryDao;

	// reference to the singleton directory validator
	private DirectoryValidator directoryValidator;
	

	/**
	 * Validates and saves a directory object
	 * @param directory The directory
	 * @throws DirectoryValidationException 
	 */
	@Transactional
	public void saveDirectory ( Directory directory ) throws DirectoryValidationException  {
		
		try {
			// validate the directory 
			directoryValidator.validate(directory);
			
			// check if directory is not duplicate
			 if (directoryDao.existsDirectoryWithSourceNameAndSourceURL(directory.getSourceName(), directory.getSourceURL())){
					// duplicate name/url pair
				 	throw new DirectoryValidationException("A directory with the same 'Source Name' and 'Source URL' already exists"); 
			}
			
			// save the directory
			directoryDao.save(directory);
			
		} catch (DirectoryValidationException e) {
			// log the exception here if needed and throw
			throw e;
		}
		
	}
	
	/**
	 * Updates a directory in the database if it passes validation
	 * @param directory The directory that will be updated
	 * @throws DirectoryValidationException Validation exception
	 */
	public void updateDirectory ( Directory directory ) throws DirectoryValidationException {
		
		// validate the updated parameters of the directory
		directoryValidator.validate(directory);
		
		// update the directory
		directoryDao.delete(directory);
	}
	
	/**
	 * Deletes a directory from persistent storage
	 */
	public void deleteDirectory ( Directory directory ) {
		
		// use DAO to delete the directoryz
		directoryDao.delete(directory);
	}
	
	/**
	 * Sets the directory validator used to validate directory objects
	 * @param directoryValidator
	 */
	public void setDirectoryValidator(DirectoryValidator directoryValidator) {
		this.directoryValidator = directoryValidator;
	}
	
	/**
	 * Sets the directory dao used to manipulate persistent directory objects
	 * @param directoryDao
	 */
	public void setDirectoryDao(DirectoryDao directoryDao) {
		this.directoryDao = directoryDao;
	}
	
	
}
