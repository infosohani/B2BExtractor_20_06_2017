package com.businessextractor.services.parser.dmoz;
public class DMOZBusinessProcessingService{
}
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import au.id.jericho.lib.html.Element;
//import au.id.jericho.lib.html.Source;
//import au.id.jericho.lib.html.Tag;
//
//import com.businessextractor.entity.business.Business;
//import com.businessextractor.entity.directory.Directory;
//import com.businessextractor.services.parser.HierarchicalBusinessProcessingService;
//import java.io.IOException;
//import java.net.URL;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// * Implementation of a hierarchical business processing service used to process the www.dmoz.org directories.
// * @author Bogdan Vlad
// *
// */
//public class DMOZBusinessProcessingService
//	extends HierarchicalBusinessProcessingService {
//	/**
//	 * Logger for this class
//	 */
//	private static final Log logger = LogFactory.getLog(DMOZBusinessProcessingService.class);
//
//	@Override
//	public List<Business> getBusinessesFromDirectory( Directory directory ) {
//		   Source source;
//            try {
//                source = new Source(new URL(directory.getSourceURL()));
//          
//		List<Business> businessList = new ArrayList<Business>();
//                
//	
//			// find all unordererd lists
//			List<Element> elements = source.findAllElements(Tag.UL);
//                   
//			// get the one close to the end ( the one before the last one )                       
//                        for(int i = 1; i < elements.size() - 1; i++){
//			Element businessElements = elements.get(i);                       
//			// find all list items of unordered lists
//			List<Element> businesses = businessElements.findAllElements(Tag.LI);
//			int k = 0;
//			for (Element business : businesses  ) {
//
//				// get all the links in the supposed business directory
//				List<Element> links = business.findAllElements(Tag.A);	
//				if (links.size() >= 1) {
//                                   for(Element businessLink : links ){
//					// get the first link ( the business link )
//					String businessDomain = businessLink.getAttributeValue("href");
//					String businessTitle = businessLink.getContent().extractText();					
//					String text =  business.extractText();
//                                        /**
//                                         * By SK
//                                         */
//					String businessDescription = text.substring(businessTitle.length()+1);
//
//                                        //String businessDescription = businessTitle;
//					// this eliminates the care where categories have to be parsed and makes sure
//					// the business currently being analyzed is valid                                      
//					    if (businessDescription.length() > 20 && businessDescription.startsWith("-")) {
//						Business businessStub = new Business ();
//						businessStub.setBusinessTitle(businessTitle);
//                                                //System.out.println("businessDescription" + businessDescription);
//						businessStub.setBusinessDescription(repalceSpecialChar(businessDescription));
//						businessStub.setBusinessDomain(businessDomain);
//						// set the import date to the current date
//						businessStub.setImportDate(new Date());
//						
//						// add to the businesses list
//                                                if(!businessDomain.contains("World")) {
//						businessList.add(businessStub);
//                                                }
//                                            }
//                                //        }
//					}
//                                   }
//				
//			}
//                   }
//                        
//                        return businessList;
//		} catch (Exception e) {
//			logger.info("Exception occured while extracting DMOZ businesses from " +e);
//		}
//		
//		// return the list of results to the client / or empty array if exception occured
//		return null;
//	}
//
//
//	@Override
//	public List<Directory> getSubdirectories( Directory directory ) {
//		
//		// the list of directories extracted from this website
//		List<Directory> directories = new ArrayList<Directory>();
//		Source  source;
//            try {
//                source = new Source(new URL(directory.getSourceURL()));
//          
//		// find all tables
//		List<Element> tableTags = source.findAllElements(Tag.DIV);
//		
//		for  ( Element table : tableTags ) {
//			List<Element> listItems = table.findAllElements(Tag.LI);
//			
//			for ( Element listItem : listItems ) {
//				String text = listItem.extractText();
//                               // System.out.println(text);
//				
//				// only mathch items like "Nail Polishing (121)"
//				if (text.matches(".*\\(.*\\).*")) {
//                                    if(listItem.findAllElements(Tag.A).size() != 0){
//					Element directoryLink = (Element)listItem.findAllElements(Tag.A).get(0);
//					
//					String sourceUrl = directoryLink.getAttributeValue("href");
//					sourceUrl= getWebsiteRoot().getBaseURL() + sourceUrl;
//					String sourceName = directoryLink.extractText();
//					
//					// create and add a new directory stub to the list
//					Directory directoryStub = new Directory();
//					directoryStub.setSourceURL(sourceUrl);
//					directoryStub.setSourceName(sourceName);
//					if(!sourceUrl.contains("World")){
//					directories.add(directoryStub);
//                                        }
//                                    }
//				}
//
//			}
//		}
//		
//		// return the list of parsed subdirectories
//		return directories;
//                  } catch (IOException ex) {
//                Logger.getLogger(DMOZBusinessProcessingService.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            return null;
//	}
//
//    private String repalceSpecialChar(String businessDescription) {
//

//
//
//
//        
//        return businessDescription;
//
//    }
//
//}
