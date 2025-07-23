package com.businessextractor.services.directory;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

import com.businessextractor.entity.directory.Directory;
import com.businessextractor.entity.directory.DirectoryDao;
import com.businessextractor.services.directory.exception.DirectoryValidationException;
import com.businessextractor.ui.websiteRoot.WebsiteRootManagementFrame;
import com.businessextractor.util.Validator;

/**
 * Concrete validator used for validating the attributes of a Directory object.
 * @author user
 *
 */
public class DirectoryValidator  implements Validator<Directory,DirectoryValidationException>{

	public void validate(Directory directory)
		throws DirectoryValidationException {
		// validate fields
		validateFields ( directory );
	}
	
	/**
	 * Validates field values in the directory. Common for both save and update validations.
	 * @param directory
	 * @throws DirectoryValidationException
	 */
	private void validateFields (Directory directory ) throws DirectoryValidationException {
		
		if (directory.getSourceName() == null || directory.getSourceName().equals("")) {
			throw new DirectoryValidationException("The field 'Source Name' cannot be empty");
		}
		if (directory.getSourceURL() == null || directory.getSourceURL().equals("")) {
			throw new DirectoryValidationException("The field 'Source URL' cannot be empty");
		} else {
			try {
				// create a new dummy URL to detect a malformed url exception
				new URL (directory.getSourceURL());                               
				
				// if the url is not malformed check agains website root validation
//				if (!directory.getSourceURL().startsWith(directory.getWebsiteRoot().getBaseURL())) {
//					throw new DirectoryValidationException("The URL in 'Source URL' must start with " + directory.getWebsiteRoot().getBaseURL());
//				}
				
			} catch (MalformedURLException e) {
				throw new DirectoryValidationException("The URL in 'Source URL' is malformed");
			}
		}
		
	}

	
}
