package com.businessextractor.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.businessextractor.services.parser.BusinessProcessingService;
import com.businessextractor.ui.websiteRoot.ParsingCallbackInterface;

/**
 * Extracts data from a specific web directory using a parser and notifies using callback when done.
 * This is basically a simple implementation of the swing worker thread.
 * @author user
 *
 */
public class BusinessExtractionThread
	extends Thread {
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(BusinessExtractionThread.class);

	
	// Business processing service implementation that will do the gathering 
	private BusinessProcessingService businessProcessingService;
	
	// a callback object on wich the parsingFinished method is called when the parsing is done
	private ParsingCallbackInterface parsingCallbackInterface;
	

	
	/**
	 * Standard constructor
	 * @param businessProcessingService
	 * @param parsingCallbackInterface
	 */
	public BusinessExtractionThread(BusinessProcessingService businessProcessingService,
		ParsingCallbackInterface parsingCallbackInterface) {
		super();
		this.businessProcessingService = businessProcessingService;
		this.parsingCallbackInterface = parsingCallbackInterface;
	}



	/**
	 * Delegates to businessProcessingService to do the directory processing and
	 * calls the parsingCallbackInterface when done.
	 */
	public void run () {
		
		try {
			businessProcessingService.processDirectories();
		} catch (Exception e) {
			logger.error("An exception occured while gathering business and directory data");
		}
		// inform the "listeneing" object that the parsing is done
		parsingCallbackInterface.parsingFinished();
	}
	
}
