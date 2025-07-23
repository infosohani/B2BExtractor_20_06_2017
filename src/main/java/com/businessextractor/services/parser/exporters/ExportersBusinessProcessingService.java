package com.businessextractor.services.parser.exporters;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.businessextractor.entity.business.Business;
import com.businessextractor.entity.directory.Directory;
import com.businessextractor.services.parser.HierarchicalBusinessProcessingService;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import javax.swing.JOptionPane;

public class ExportersBusinessProcessingService extends HierarchicalBusinessProcessingService {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(ExportersBusinessProcessingService.class);
    private static int counter = 0;

    @Override
    protected List<Business> getBusinessesFromDirectory(Directory directory) {

        Source directorySource;
        try {
            directorySource = new Source(new URL(directory.getSourceURL()));


            List<Business> businesses = new ArrayList<Business>();

            this.getBusinesses(directorySource, businesses);

            return businesses;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ExportersBusinessProcessingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * This function is suitable for recursive parsing of pages due to its collective parameter businesses
     * Takes every page, parses it for businesses and passes everything on to another function call, for each page
     *
     * @param source     - source to parse
     * @param businesses - collective variable (gathers all business from all sources under recursion)
     */
    private void getBusinesses(Source source, List<Business> businesses) {
        //form with members/businesses
        counter = 0;
        Element memberForm = null;

        List<Business> childBusiness = new ArrayList<Business>();

        try {
            List<Element> fonts = source.getAllElements(HTMLElementName.DIV);
            Element font = source.getElementById("INDEX_CONTENT_MAIN");


            List<Element> links = font.getAllElements(HTMLElementName.A);


            for (Element link : links) {
                //     System.out.println("links"+link);
                String businessDomain = link.getAttributeValue("href");


                if (businessDomain != null && (!businessDomain.equals(""))) {
                    /**
                     * @BY SK 16-DEC-2010
                     * @Condition (businessDomain.contains ( " member_profile ")|| businessDomain.contains("buying_lead")) ? true : false ;
                     * @Description This condtion is use for checking the two categeories  member_profile OR buying_lead.
                     *
                     */
                    boolean isTrue = (businessDomain.contains("member_profile.asp?")) ? true : false;

                    if (isTrue == true) {

                        //Element newTr = trs.get(trs.indexOf(tr)+1);
                        String businessTitle = link.getContent().getTextExtractor().toString();
                        String text = link.getTextExtractor().toString();
                        // String businessDescription = newTr.getContent().extractText();
                        int size = fonts.indexOf(font);
                        Element newTr = fonts.get(size + 3);
                        Element newTr1 = fonts.get(size + 4);
                        Element bisCountry = fonts.get(size + 2);
                        Business businessStub = new Business();
                        businessStub.setBusinessTitle(businessTitle);

                        //  businessStub.setBusinessDescription(businessDescription);
                        //Inserting business domain


                        String businessURL = "https://www.exporters.sg" + businessDomain;

                        Source source1 = new Source(new URL(businessURL));
                        //   System.out.println("com.businessextractor.services.parser.exporters.ExportersBusinessProcessingService.getBusinesses()"+source1);
//
//                        FileWriter fw = new FileWriter("new1.txt");
//                        fw.write(source1.toString());
//                        fw.close();

                        businessStub.setSourceName("Bathrooms");
                        List<Element> l = source1.getAllElements(HTMLElementName.TABLE);

                        for (int i = 0; i < l.size(); i++) {
                            String s = l.get(i).toString();
                            Element e = l.get(i);
                            if (s.indexOf("Website:") != -1) {
                                int j = s.indexOf("Website:");
                                //    System.out.println(s);

                                List<Element> tagb = l.get(i).getAllElements(HTMLElementName.B);
                                String address = tagb.get(1).getTextExtractor().toString();
                                businessStub.setAddress(address);

                                List<Element> el = l.get(i).getAllElements(HTMLElementName.A);
                                String Countryname = el.get(0).getTextExtractor().toString().substring(5);
                                businessStub.setCountry(Countryname);

                                int k = s.indexOf("href", j);
                                int k1 = s.indexOf('>', k);
                                String url = s.substring(k + 13, k1 - 1);
                                if (url.equalsIgnoreCase(""))
                                    businessStub.setBusinessDomain("");
                                else
                                    businessStub.setBusinessDomain(url);
                                // JOptionPane.showMessageDialog(null, s.substring(k + 5, k1));

                                ///List<Element> links=e.findAllElements(Tag.A);
//                    System.out.println(links.get(0).getAttributeValue("HREF"));
//                    JOptionPane.showMessageDialog(null,links.get(0).getAttributeValue("HREF"));
                                break;
                            }
                            //
                        }

                        int j = source.toString().indexOf("Website:");


                        System.out.println(j);


                        //End business domain

                        // set the import date to the current date
                        businessStub.setImportDate(new Date());
                        String businessDescription = newTr.getContent().getTextExtractor().toString();
                        String businessDescription1 = newTr1.getContent().getTextExtractor().toString();
                        if (businessDescription.length() > businessDescription1.length()) {
                            businessStub.setBusinessDescription(businessDescription);
                        } else {
                            businessStub.setBusinessDescription(businessDescription1);
                        }
                        //   businessStub.setCountry(bisCountry.getContent().extractText());
                        businesses.add(this.convertToBusiness(businessStub));
//                            String nextPageUrl = this.getWebsiteRoot().getBaseURL() +  businessDomain;
//                            System.out.println(nextPageUrl);
//                            Source nextPageSource = new Source(new URL(nextPageUrl));
//                            System.out.println("----------------#@#@#@#@#@##---------------");
//                            System.out.println(nextPageSource);
//                            System.out.println("----------------#@#@#@#@#@##---------------");
//                            if (nextPageSource != null) {
//                                directory.setSourceURL(nextPageUrl);
//                                directory.setSourceName(source.toString());
//                                // this.getChildBusinesses(nextPageSource, businesses);
////                                if (!getDirectoryDao().existsDirectoryWithSourceNameAndSourceURL("abc", directory.getSourceURL())) {
////                                    // set the website root to the current website root of this service
////                                    directory.setWebsiteRoot(getWebsiteRoot());
////                                    // persist the directory
////                                    getDirectoryDao().save(directory);
////                                }
//                            }
//                            System.out.println("businessDescription" + businessDescription);


                        //}
                        //    }
                        // }
                        //  }

//                               String nextPageUrl = nextPageAnchor.getAttributeValue("href");
//									nextPageUrl = this.getWebsiteRoot().getBaseURL() + "/" +nextPageUrl;
//									System.out.println(nextPageUrl);
//									Source nextPageSource = new Source(new URL(nextPageUrl));
//									this.getBusinesses(nextPageSource, businesses);
                    }


                }
            }

            Element element = source.getElementById("PAGING_NEXT");
            if (element != null) {
                String href = element.getAttributeValue("href");
                String nextpageurl = "https://www.exporters.sg" + href;
                Source sourcenext = new Source(new URL(nextpageurl));
                System.out.println("com.businessextractor.services.parser.exporters.ExportersBusinessProcessingService.getBusinesses()" + href);
                this.getBusinesses(sourcenext, businesses);
            }


//                 if (businessDomain.contains("membersdir.asp?cat_id") && link.getContent().extractText().equalsIgnoreCase("next")) {
//                            businessDomain = this.getWebsiteRoot().getBaseURL() + "/" + businessDomain;
//                            //System.out.println(businessDomain);
//                            Source nextPageSource = new Source(new URL(businessDomain));
//                            this.getBusinesses(nextPageSource, businesses);
//                        }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("this was the coming from the Exporterbusinessprocessingervice");

        }
    }

    /**
     * This function is suitable for recursive parsing of pages due to its collective parameter businesses
     * Takes every page, parses it for businesses and passes everything on to another function call, for each page
     *
     * @param source     - source to parse
     * @param businesses - collective variable (gathers all business from all sources under recursion)
     */
    private void getChildBusinesses(Source source, List<Business> businesses) {
        //form with members/businesses
        Element memberForm = null;

        try {
            //List<Element> forms = source.findAllElements("form");
            List<Element> forms = source.getAllElements(HTMLElementName.A);

            for (Element form : forms) {

                if (form.getAttributes() != null && form.getAttributes().size() >= 0) {
                    String businessDomain = form.getAttributeValue("href");
                    if (businessDomain != null && (!businessDomain.equals("/"))) {
                        boolean isTrue = businessDomain.contains(businessDomain);
                        if (isTrue == true) {

                            String businessTitle = form.getContent().getTextExtractor().toString();

                            String text = form.getTextExtractor().toString();
                            String businessDescription = businessTitle;
                            //if (businessDescription.length() > 5) {
                            Business businessStub = new Business();
                            businessStub.setBusinessTitle(businessTitle);
                            businessStub.setBusinessDescription(businessDescription);
                            businessStub.setBusinessDomain(businessDomain);
                            // set the import date to the current date
                            businessStub.setImportDate(new Date());
                            if (!businesses.contains(this.convertToBusiness(businessStub))) {
                                businesses.add(this.convertToBusiness(businessStub));
                            }
                            String nextPageUrl = this.getWebsiteRoot().getBaseURL() + businessDomain;
                            System.out.println(nextPageUrl);
//                            Source nextPageSource = new Source(new URL(nextPageUrl));
//                            System.out.println("----------------#@#@#@#@#@##---------------");
//                            System.out.println(nextPageSource);
//                            System.out.println("----------------#@#@#@#@#@##---------------");
//                            if (nextPageSource != null) {
//                                this.getBusinesses(nextPageSource, businesses);
//                            }
//                            System.out.println("businessDescription" + businessDescription);

                        }


                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
//                    }
//                    if (form.getAttributeValue("name") != null && form.getAttributeValue("name").equals("memberdir")) {
//                        //mark form as members form
//                        memberForm = form;
//
//                        List<Element> rows = form.findAllElements(Tag.TR);
//                        if (rows != null && rows.size() != 0) {
//
//                            //take every row starting with the second one and without last 4
//                            for (int i = 1; i < rows.size() - 4; i++) {
//                                //convert/extract data to business and add it to collective variable
//                                businesses.add(this.convertToBusiness(rows.get(i)));
//                            }
//
//                        }
//
//                        break;
//                    }
//                }
//            }
//
//            if (memberForm != null) {
//                //after member form, paging should be found
//                Element pagingElement = memberForm.getEndTag().findNextTag().findNextTag().findNextTag().getElement();
//                if (pagingElement.getName().equals("tr")) {
//                    //might be the row with paging details
//                    List<Element> bolds = pagingElement.findAllElements(Tag.B);
//                    if (bolds != null && bolds.size() != 0) {
//                        //take every bold to assure it has child elements, otherwise it's not the searched one
//                        for (Element bold : bolds) {
//                            List<Element> elems = bold.getChildElements();
//                            if (elems != null && elems.size() != 0) {
//                                //take next anchor after bold
//                                Element nextPageAnchor = bold.getEndTag().findNextTag().getElement();
//                                if (nextPageAnchor.getName().equals("a")) {
//                                    try {
//                                        //form the url of the next page
//                                        String nextPageUrl = nextPageAnchor.getAttributeValue("href");
//                                        nextPageUrl = this.getWebsiteRoot().getBaseURL() + "/" + nextPageUrl;
//                                        System.out.println(nextPageUrl);
//                                        Source nextPageSource = new Source(new URL(nextPageUrl));
//                                        this.getBusinesses(nextPageSource, businesses);
//                                    } catch (Exception e) {
//                                        logger.error("An error has occured while forming url for next page " + e, e);
//                                    }
//                                }
//                                break;
//                            }
//                        }
//
//
//                    }
//                }
//            }

    /**
     * Converts a html element into a Business object
     * //     * @param row - html row element which contains business data
     *
     * @return - Business object
     */
    private Business convertToBusiness(Business bis) {
        return bis;
    }

    @Override
    protected List<Directory> getSubdirectories(Directory directory) {
        Source directorySource;
        try {
            directorySource = new Source(new URL(directory.getSourceURL()));


            List<Directory> directories = new ArrayList<Directory>();


            List<Element> forms = directorySource.getAllElements(HTMLElementName.A);

            for (Element form : forms) {

                if (form.getAttributes() != null && form.getAttributes().size() >= 0) {
                    String directoryUrl = form.getAttributeValue("href");
                    if (directoryUrl != null && (!directoryUrl.equals(""))) {
                        boolean isTrue = (directoryUrl.startsWith("/membersdir.asp?")) ? true : false;

                        if (isTrue == true) {
                            if (counter == 0) {
                                counter = 1;  // Condition check for remove the infinte loop.
                                continue;
                            }
                            directory = new Directory();
                            directoryUrl = this.getWebsiteRoot().getBaseURL() + directoryUrl;
                            directory.setActivated(false);
                            directory.setSourceURL(directoryUrl);
                            directory.setSourceName(form.getTextExtractor().toString());
                            if (!directory.getSourceName().equals("") && directory.getSourceName().length() > 7) {
                                directories.add(directory);
                            }
                            //     this.getSubdirectoriesFromBusinessPage(directorySource, directories);

                        }
                    }
                }

//            if (form.getAttributes() != null && form.getAttributes().size() >= 0) {
//                if (form.getAttributeValue("name") != null && form.getAttributeValue("name").equals("premiumsvc")) {
//                    //means this source represents a page of businesses + subdirectories
//
//                }
//            }
            }


            //    this.getSubdirectoriesFromDirectoryPage(directorySource, directories);
            return directories;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ExportersBusinessProcessingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void getSubdirectoriesFromBusinessPage(Source directorySource, List<Directory> directories) {

        List<Element> tables = directorySource.getAllElements(HTMLElementName.TABLE);

        if (tables != null && tables.size() != 0) {
            for (Element table : tables) {
                //if table presents the attributes width, cellspacing, cellpadding, border with certain values, then it's the categories table
                if (table.getAttributeValue("width") != null && table.getAttributeValue("width").equals("100%") && table.getAttributeValue("cellspacing") != null && table.getAttributeValue("cellspacing").equals("0") && table.getAttributeValue("cellpadding") != null && table.getAttributeValue("cellpadding").equals("5") && table.getAttributeValue("border") != null && table.getAttributeValue("border").equals("0") && table.getAttributeValue("style") != null && table.getAttributeValue("style").equals("border: 1px solid #336699")) {
                    //take the inner anchors and extract wnated data
                    List<Element> anchors = table.getAllElements(HTMLElementName.A);
                    if (anchors != null && anchors.size() != 0) {
                        for (Element anchor : anchors) {
                            Directory directory = new Directory();
                            //form the url of the newly found directory
                            String directoryUrl = anchor.getAttributeValue("href");
                            directoryUrl = this.getWebsiteRoot().getBaseURL() + directoryUrl;
                            directory.setActivated(false);
                            directory.setSourceURL(directoryUrl);
                            directory.setSourceName(anchor.getTextExtractor().toString());
                            directories.add(directory);
                        }
                    }
                    //stop looking for anything else
                    break;
                }
            }
        }
    }

    private void getSubdirectoriesFromDirectoryPage(Source directorySource, List<Directory> directories) {

        List<Element> tables = directorySource.getAllElements("style", "border: 1px solid #336699", true);
        if (tables != null && tables.size() != 0) {

            //this is a source presenting only subdirectories
            //take the first one (should be the only one)
            Element table = tables.get(0);

            //take all anchor elements
            List<Element> anchors = table.getAllElements(HTMLElementName.A);
            if (anchors != null && anchors.size() != 0) {
                for (Element anchor : anchors) {
                    List<Element> anchorElements = anchor.getAllElements();
                    if (anchorElements != null && anchorElements.size() != 0) {
                        for (Element anchorElement : anchorElements) {
                            if (anchorElement.getName().equals("b")) {
                                Directory directory = new Directory();
                                directory.setActivated(false);
                                //form the url of the newly found directory
                                String directoryUrl = anchor.getAttributeValue("href");
                                directoryUrl = this.getWebsiteRoot().getBaseURL() + directoryUrl;
                                directory.setSourceURL(directoryUrl);
                                directory.setSourceName(anchor.getTextExtractor().toString());
                                directories.add(directory);
                                break;
                            }
                        }
                    }
                }
            }

        }
    }
} 