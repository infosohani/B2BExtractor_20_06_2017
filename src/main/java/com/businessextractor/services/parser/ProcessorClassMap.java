package com.businessextractor.services.parser;

import java.util.HashMap;
import java.util.Map;

//import com.businessextractor.services.parser.dmoz.DMOZBusinessProcessingService;                                  //ypathak 16-05-2017
import com.businessextractor.services.parser.exporters.ExportersBusinessProcessingService;
import com.businessextractor.services.parser.newyellowpages.newyellowpages;
import com.businessextractor.services.parser.tpage.TPageBusinessProcessingService;
import com.businessextractor.services.parser.yahoo.YahooBusinessProcessingService;
import com.businessextractor.services.parser.yellowpages.YellowpagesBusinessProcessingService;
import com.businessextractor.services.parser.yelp.YelpBusinessprocessingservice;

/**
 * This class associates a name of a website root to the ProcessingService implementation class that will
 * be used to process that website root.
 * @author Bogdan Vlad
 *
 */
public class ProcessorClassMap {
	
	// map containing website_root_name-Class associations
	public static final Map<String,Class> websiteRootParserClassMap = new HashMap<String,Class>();
	
	/**
	 * This gets loaded and executed on startup of the application.
	 */
	static {
		websiteRootParserClassMap.put("Yahoo",YahooBusinessProcessingService.class);
	        //websiteRootParserClassMap.put("DMOZ", DMOZBusinessProcessingService.class);
		websiteRootParserClassMap.put("Exporters", ExportersBusinessProcessingService.class);
		websiteRootParserClassMap.put("TPage", TPageBusinessProcessingService.class);
                websiteRootParserClassMap.put("koreadaily",YellowpagesBusinessProcessingService.class);
                websiteRootParserClassMap.put("Yelp", YelpBusinessprocessingservice.class);
                websiteRootParserClassMap.put("Yellowpages",newyellowpages.class);
	}
}
