package com.businessextractor.services.parser.yahoo;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import net.htmlparser.jericho.Tag;

import com.businessextractor.entity.business.Business;
import com.businessextractor.entity.directory.Directory;
import com.businessextractor.services.parser.HierarchicalBusinessProcessingService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Implementation of a hierarchical business processing service used to process the dir.yahoo.com directories.
 * @author Bogdan Vlad
 *
 */
@SuppressWarnings(value="unchecked")
public class YahooBusinessProcessingService
	extends HierarchicalBusinessProcessingService {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(YahooBusinessProcessingService.class);

	@Override
	public List<Business> getBusinessesFromDirectory(Directory directory) {
            
              Source directoryUrlSource;
            try {
                directoryUrlSource = new Source(new URL(directory.getSourceURL()));
           
		List<Business> businesses = new ArrayList<Business> ();
			// call functions to populate the business array
			
			// get the sponsored listings first
			getSponsorListings (directoryUrlSource, businesses);
			
			// get all other site listings by recursing through pages
			getSiteListings(directoryUrlSource, businesses);	
		return businesses;
                
                 } catch (IOException ex) {
                Logger.getLogger(YahooBusinessProcessingService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
	}
	
	/**
	 * Method used for extracting the businesses
	 * @param source The source page used for extraction
//	 * @param reference to the list to which the businesses will be added
	 */
	private void getSiteListings(Source source, List<Business> businesses) {
		// extract data from site listings on this page
		List<Element> divs = source.getAllElements(HTMLElementName.DIV);
                System.out.println("----------------**************---------------");
                System.out.println(source);
		System.out.println("----------------**************---------------");
		
		for ( Element div : divs ) {
			if (div.getAttributeValue("class")!=null && div.getAttributeValue("class").equals("st")) {
				// a site listing div has been found
				List<Element> listItems = div.getAllElements(HTMLElementName.LI);
				for ( Element businessListItem : listItems ) {
					try {
						// convert the list item to a business and add it to the businesses list.
						businesses.add(convertListItemToBusiness(businessListItem));
					} catch (Exception e) {
						logger.error("Error extracting businesses from source",e);
					}
				}
			}
		}
		
		// parse the next page ( if there is one ) recursivley to add all businesses to the array.
		
		for ( Element div : divs ) {
				if (div.getAttributeValue("id")!=null && div.getAttributeValue("id").equals("yschnext")) {
					// recurse to the next page
					List<Element> linkList = div.getAllElements(HTMLElementName.A);
					Element link = linkList.get(0);
					
					try {
						// recurse to the next page
						String nextPageURL = link.getAttributeValue("href");
						Source nextPageSource = new Source(new URL(nextPageURL));
						getSiteListings(nextPageSource, businesses);
					} catch (Exception e) {
						logger.error("An exception has occured "+e,e);
					}
				}
		}
		
	}
	
	/**
	 * Method used for extracting the sponsored listings
	 * @param source The source page
	 * @param businesses The business list to which the results will be added
	 */
	private void getSponsorListings (Source source, List<Business> businesses) {
		// extract data from sponsor listings on this page
		List<Element> divs = source.getAllElements(HTMLElementName.DIV);
                System.out.println("----------------**************---------------");
                System.out.println(source);
		System.out.println("----------------**************---------------");
		
		for ( Element div : divs ) {
			if (div.getAttributeValue("class")!=null && div.getAttributeValue("class").equals("spnsr")) {
				// a sponsored listing div has been found
				List<Element> listItems = div.getAllElements(HTMLElementName.LI);
				for ( Element businessListItem : listItems ) {
					try {
						// convert the list item to a business and add it to the businesses list.
						businesses.add(convertListItemToBusiness(businessListItem));
					} catch (Exception e) {
						logger.error("Error extracting businesses from source",e);
					}
				}
				// do not iterate further
				break;
			}
		}
	}
	
	/**
	 * Extracts the business from an actual listItem.
	 * @param listItem
	 * @return
	 */
	private Business convertListItemToBusiness (Element listItem) {
		
		Business business = new Business();
		business.setImportDate(new Date());
		
		List<Element> links = listItem.getAllElements(HTMLElementName.A);
		if (links.size()>0) {
			Element link = links.get(0);
			business.setBusinessDomain(link.getAttributeValue("href"));
			business.setBusinessTitle(link.getName());
			
			if (listItem.getAllElements(HTMLElementName.BR).size()==2) {
			    // extract the business description
				String listItemAsText = listItem.toString();
				Scanner scanner = new Scanner(listItemAsText);
				scanner.useDelimiter("<br />");
				scanner.next();
				String businessDescription = scanner.next();
				business.setBusinessDescription(businessDescription);
			} else {
				// no business description available.. use empty description
				business.setBusinessDescription("");
			}
			
		}
		
		return business;
	}



	@Override
	public List<Directory> getSubdirectories(Directory directory) {
            
            Source source;
            try {
                source = new Source(new URL(directory.getSourceURL()));
            
		// create a new list of directories
		List<Directory> directories = new ArrayList<Directory>();
		
		                      
                        
                        List<Element> base = source.getAllElements(HTMLElementName.BASE);
                        
                        String sourceURL= base.get(0).getAttributeValue("href");                        
			List<Element> tables = source.getAllElements(HTMLElementName.TABLE);
			String tempURL="";
			for (Element table : tables ) {
				if (table.getAttributeValue("width")!=null && table.getAttributeValue("width").equals("100%")
                                && table.getAttributeValue("border")!=null && table.getAttributeValue("border").equals("0")
                                 && table.getAttributeValue("cellspacing")!=null && table.getAttributeValue("cellspacing").equals("0")
                                  && table.getAttributeValue("cellpadding")!=null && table.getAttributeValue("cellpadding").equals("0")) {
					
					
					List<Element> tableDivs = table.getAllElements(HTMLElementName.DIV);
						// iterate each div in the categories table
						for (Element tableDiv : tableDivs) {
							// analyze div for directories
							if (tableDiv.getAttributeValue("class")!=null && tableDiv.getAttributeValue("class").equals("cat")) {
								List<Element> listItems = tableDiv.getAllElements(HTMLElementName.LI);
								for (Element listItem : listItems) {
									// extract directory from list item
									     directory = new Directory();	
										tempURL=sourceURL;
										// extract and parse the source name
										String sourceName = listItem.getName();
										if (sourceName.contains("@")) {
											sourceName=sourceName.substring(0,sourceName.indexOf('@'));
                                            tempURL=getWebsiteRoot().getBaseURL();
										}
										Scanner scanner = new Scanner(sourceName);
										scanner.useDelimiter(" (.*)");
										String actualSourceName = scanner.next();
										directory.setSourceName(actualSourceName);
										List<Element> links = listItem.getAllElements(HTMLElementName.A);
										
										// extract and parse the link
										String hrefValue = links.get(0).getAttributeValue("href");
										if (!hrefValue.startsWith("/")) {
											hrefValue = "/"+hrefValue;
										}
										
										directory.setSourceURL(tempURL+hrefValue);
									// add the directory to the directory array
									directories.add(directory);
								}
							}
						}
					// break the operation
					break;
				}
			}
                        return directories;
		} catch (Exception e) {
			logger.info("An exception occured while processing the subdirectories "+e,e);
		}
		
		// return the subdirectories extracted or an empty array if no subdirectories were produced
		return null;
                
	}

}
