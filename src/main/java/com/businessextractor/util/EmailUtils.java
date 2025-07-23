package com.businessextractor.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Email parsing/cropping utilitary class.
 * @author Bogdan Vlad
 *
 */
public class EmailUtils {

	/**
	 * Crops a domain name for use in various places in the application
	 * @param domainName The original domain name
	 * @return The cropped domain name
	 */
	public static String cropDomainName (String domainName ) {
		try {
			URI uri = new URI(domainName);
			String hostName = uri.getHost();
			if (hostName==null) {
				return domainName;
			} else if (hostName.startsWith("www.")) {
				// crop the "www." part
				return hostName.substring(hostName.indexOf('.')+1);
			} else {
				return hostName;
			}
		} catch (URISyntaxException e) {
			return domainName;
		} catch (Exception e) {
			return domainName;
		}
	}
        
        /**
	 * Crops a Business URL (Web Address)
	 * @param domainName The original domain name
	 * @return The cropped Bisiness URL(Web Address)
	 */
        public static String cropBusinessURL (String domainName ) {
		try {
			URI uri = new URI(domainName);
			String hostName = uri.getHost();
			if (hostName==null) {
				return domainName;
			}else {
				return hostName;
			}
		} catch (URISyntaxException e) {
			return domainName;
		} catch (Exception e) {
			return domainName;
		}
	}
	
}
