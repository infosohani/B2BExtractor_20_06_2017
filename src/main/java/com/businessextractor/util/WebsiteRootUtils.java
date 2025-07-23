package com.businessextractor.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * Utility class used for retrieving objects as files from the file system.
 * @author user
 *
 */
public class WebsiteRootUtils {
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(WebsiteRootUtils.class);

	/**
	 * Gets a byte content from a file on  the system
	 * @param logo
	 * @return
	 */
	public static byte[] getLogoAsByteArray (String logo) {
		
		try {
			File f = new File(logo);
			
			FileInputStream fis = new FileInputStream(f);
			
			byte[] logoBytes = new byte[50000];
			
			fis.read(logoBytes);
			
			return logoBytes;
		} catch (Exception e) {
			logger.info("Error occured while fetching content "+e,e);
			return null;
		}
			
	}
	
}
