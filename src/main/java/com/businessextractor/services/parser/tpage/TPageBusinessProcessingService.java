package com.businessextractor.services.parser.tpage;

import net.htmlparser.jericho.HTMLElementName;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.Tag;

import com.businessextractor.entity.business.Business;
import com.businessextractor.entity.directory.Directory;
import com.businessextractor.services.parser.HierarchicalBusinessProcessingService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.logging.Level;

/**
 * A parser implementation for "http://dir.tpage.com/" base site
 *
 * @author Claudia Hosu
 */
@SuppressWarnings(value = "unchecked")
public class TPageBusinessProcessingService extends
        HierarchicalBusinessProcessingService {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(TPageBusinessProcessingService.class);

    /* (non-Javadoc)
     * @see com.businessextractor.services.parser.HierarchicalBusinessProcessingService#getBusinessesFromDirectory(au.id.jericho.lib.html.Source)
     */
    @Override
    protected List<Business> getBusinessesFromDirectory(Directory directory) {

        try {
            Source directoryUrlSource = new Source(new URL(directory.getSourceURL()));


            List<Business> businessList = new ArrayList<Business>();
            Document doc = Jsoup.connect(directory.getSourceURL()).get();
            this.getNewBusinesses(doc, businessList);


            return businessList;
        } catch (IOException ex) {
            //   java.util.logging.Logger.getLogger(TPageBusinessProcessingService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private void getNewBusinesses(Document doc, List<Business> businesses) {

        try {

            Elements companyElements = doc.select("font[color=#1052A3] b");
            for (org.jsoup.nodes.Element company : companyElements) {
                Business business = new Business();
                // Get business name
                String businessName = company.text();
                // Get parent <a> tag for the link
                org.jsoup.nodes.Element linkElement = company.parent().parent();
                String businessLink = linkElement.attr("href");

                // Get product info
                org.jsoup.nodes.Element productElement = linkElement.parent().selectFirst("font[color=#bf0000]");
                String productInfo = productElement != null ? productElement.parent().text() : "No product info available";

                business.setBusinessTitle(businessName);
                business.setSourceUrl(businessLink);
                business.setBusinessDomain(businessLink);
                business.setBusinessDescription(productInfo);

                businesses.add(business);
            }
        } catch (Exception e) {
            //logger.error("Error while retrieving elements from source "+source,e);                 //ypathak
            //    e.printStackTrace();
        }
    }
    /**
     * This function is suitable for recursive parsing of pages due to its collective parameter businesses
     * Takes every page, parses it for businesses and passes everything on to another function call, for each page
     *
     * @param source     - source to parse
     * @param businesses - collective variable (gathers all business from all sources under recursion)
     */
    private void getBusinesses(Source source, List<Business> businesses) {

        try {
            List<Element> businessItems = null;

            // find all unordererd lists
            List<Element> uls = source.getAllElements(HTMLElementName.UL);

            if (uls != null && uls.size() > 1) {
                List<Element> hrs = source.getAllElements(HTMLElementName.HR);

                if (hrs != null && hrs.size() != 0) {
                    Element hr = hrs.get(0);
                    //get the div before the ul
                    Element elem = hr.getStartTag().getNextTag().getElement();

                    //get the ul
                    Element ul = elem.getEndTag().getNextTag().getElement();

                    //check first that it's an unordered list
                    if (ul.getStartTag().getName().equals("ul")) {

                        businessItems = source.getAllElements(HTMLElementName.LI);

                    }
                }
            } else {
                businessItems = uls.get(0).getAllElements(HTMLElementName.LI);
            }
            if (businessItems != null) {
                //take every business item and convert it to an actual Business object
                for (Element businessItem : businessItems) {
                    Business business = this.convertToBusiness(businessItem);
                    businesses.add(business);
                }

                //prepare to take next page in parsing service
                List<Element> centeredItems = source.getAllElements(HTMLElementName.CENTER);
                if (centeredItems != null && centeredItems.size() >= 2) {
                    //take second centerd item because it contains paging
                    Element centeredItem = centeredItems.get(1);
                    //get the table after it
                    Tag tableTag = centeredItem.getStartTag().getNextTag();
                    Element table = tableTag.getElement();
                    //get fonts in table
                    List<Element> fonts = table.getAllElements(HTMLElementName.FONT);
                    if (fonts != null && fonts.size() >= 2) {

                        //get second font
                        Element font = fonts.get(1);
                        //after the font the anchor for the next page should be situated
                        Tag anchor = font.getEndTag().getNextTag();

                        if (anchor.getName().equals("a")) {

                            Element anchorElement = anchor.getElement();
                            String nextPageURL = anchorElement.getAttributeValue("href");
                            Source nextPageSource = new Source(new URL(nextPageURL));
                            this.getBusinesses(nextPageSource, businesses);
                        }
                    }
                }
            }

        } catch (Exception e) {
            //logger.error("Error while retrieving elements from source "+source,e);                 //ypathak
            //    e.printStackTrace();
        }
    }

    /**
     * Converts/extracts from an html element data necessary to configure a business object
     *
     * @param businessItem - html element which parsed, contains data necessary for a business outline
     * @return - Business object extracted from the given element
     */
    private Business convertToBusiness(Element businessItem) {
        Business business = new Business();
        business.setImportDate(new Date());
        //	System.out.println("Business URL- " + businessItem );                      //ypathak
        //find all anchors (should be only one)
        List<Element> anchors = businessItem.getAllElements(HTMLElementName.A);
        if (anchors != null && anchors.size() > 0) {

            //take first anchor which contains the title of business
            Element titleAnchor = anchors.get(0);

            business.setBusinessTitle(titleAnchor.getName().trim());

            //take, if present, the second anchor which contains the domain/web site of business
            if (anchors.size() >= 2) {
                Element domainAnchor = anchors.get(1);
                business.setBusinessDomain(domainAnchor.getAttributeValue("href").trim());
            }

        }

        List<Element> childFonts = businessItem.getAllElements(HTMLElementName.FONT);

        if (childFonts != null && childFonts.size() > 0) {
            //get the last font, which should contain the description of the business
            Element childFont = childFonts.get(childFonts.size() - 1);
            String businessDescription = childFont.getName().trim();
            business.setBusinessDescription(replaceSpecialChar(businessDescription));
        }

        //ensure that either way, business has at least the empty description
        if (business.getBusinessDescription() == null) {
            business.setBusinessDescription("");
        }

        List<Element> image = businessItem.getAllElements(HTMLElementName.IMG);
        if (image != null && image.size() > 0) {
            Element contryImg = image.get(0);
            try {
                business.setCountry(contryImg.getAttributeValue("alt").trim());
            } catch (Exception e) {
            }
        }

        return business;
    }

    /* (non-Javadoc)
     * @see com.businessextractor.services.parser.HierarchicalBusinessProcessingService#getSubdirectories(au.id.jericho.lib.html.Source)
     */
    @Override
    protected List<Directory> getSubdirectories(Directory directory) {

        Source directoryUrlSource;
        try {
            directoryUrlSource = new Source(new URL(directory.getSourceURL()));

            List<Directory> directories = new ArrayList<Directory>();

            List<Element> uls = directoryUrlSource.getAllElements(HTMLElementName.UL);
            if (uls != null && uls.size() >= 1) {
                //List<Element> hrs = directorySource.getAllElements(HTMLElementName.HR);

                //if (hrs != null && hrs.size() != 0) {
                //	Element hr = hrs.get(0);
                for (Element hr : uls) {
                    List<Element> lis = hr.getAllElements(HTMLElementName.LI);
                    for (Element li : lis) {
                        //  Element dirTable = hr.getStartTag().findPreviousTag().getElement();

                        //take all anchors within
                        List<Element> anchors = li.getAllElements(HTMLElementName.A);
                        if (anchors != null && anchors.size() != 0) {
                            //each anchor represents a directory
                            for (Element anchor : anchors) {
                                //set new directory and add it
                                directory = new Directory();
                                directory.setSourceName(anchor.getName().trim());
                                directory.setActivated(false);
                                directory.setSourceURL(anchor.getAttributeValue("href").trim());
                                directories.add(directory);
                            }
                        }
                    }
                }
            }
            return directories;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(TPageBusinessProcessingService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private String replaceSpecialChar(String businessDescription) {
        if (businessDescription == null || businessDescription.isEmpty()) {
            return businessDescription;
        }

        // Replace problematic characters
        businessDescription = businessDescription.replaceAll("[\uFFFD]", ""); // Remove unknown characters
        businessDescription = businessDescription.replaceAll("[´`‘’']", "");  // Remove single quotes, accents
        businessDescription = businessDescription.replaceAll("[“”\"]", "");   // Remove double quotes

        // Normalize accented characters
        businessDescription = java.text.Normalizer.normalize(businessDescription, java.text.Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");

        return businessDescription;
    }

}
