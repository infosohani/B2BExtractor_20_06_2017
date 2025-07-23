package com.businessextractor.services.parser;

import com.businessextractor.entity.business.BusinessDao;
import com.businessextractor.spring.ApplicationContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.businessextractor.entity.business.Business;
import com.businessextractor.entity.directory.Directory;
import com.businessextractor.ui.websiteRoot.WebsiteRootListPanel;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * Abstract business processing service used for searching in hierarchical website structures. It defines the
 * template methods ( algorithm skeletons ) needed to do top level operations and defers implementation to subclasses
 * for concrete extracting.
 *
 * @author Bogdan Vlad
 */
public abstract class HierarchicalBusinessProcessingService
        extends BusinessProcessingService {
    private BusinessDao businessDao;
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(HierarchicalBusinessProcessingService.class);

    // the maximum recursion depth along the directories of a base site
    private int maxFetchDepth;

    /**
     * Returns the maximum fetch depth
     *
     * @return The maximum fetch depth
     */
    public int getMaxFetchDepth() {
        return maxFetchDepth;
    }

    /**
     * Sets the maximum fetch depth
     *
     * @param maxFetchDepth The maximum fetch depth
     */
    public void setMaxFetchDepth(int maxFetchDepth) {
        this.maxFetchDepth = maxFetchDepth;
    }

    @Override
    protected void processActiveDirectory(Directory directory) {
        // start the recursive process of going through the directory tree
        try {
            processDirectory(directory, 0);
        } catch (Exception e) {
            //     logger.error("An Exception occured while processing active directory " + directory,e);
            if (e.getMessage().toString().contains("Server returned HTTP response code: 503")) {
                JOptionPane.showMessageDialog(new WebsiteRootListPanel(), " Directory is not activated for extraction, Please first activate directory.");
            }

        }
    }

    /**
     * Auxiliary recursive function used to parse a directory.
     *
     * @param directory    The current directory that is to be parsed
     * @param currentDepth The current depth of the parser
     * @throws Exception Thrown if an exception has occured while processing a business directory
     */
    private void processDirectory(Directory directory, int currentDepth) throws Exception {
        this.businessDao = (BusinessDao) ApplicationContextHolder.getContext().getBean("businessDao");

        try {
            // sleep for 25 miliseconds to allow for garbage collection
            Thread.sleep(25);
        } catch (InterruptedException e) {
            logger.error("Interrupted exception occured while thread sleeping ", e);
        }

        if (currentDepth != 0) {
            // it is a (possibly) new Directory wich could require persisting
            if (!getDirectoryDao().existsDirectoryWithSourceNameAndSourceURL(directory.getSourceName(), directory.getSourceURL())) {
                // set the website root to the current website root of this service
                directory.setWebsiteRoot(getWebsiteRoot());
                // persist the directory
                getDirectoryDao().save(directory);
            }
        }
        if (currentDepth < maxFetchDepth) {
//        System.getProperties().put("proxySet", "true");
//        System.getProperties().put("proxyHost", "192.168.0.250");
//        System.getProperties().put("proxyPort", "8080");
            // get the source of this directory URL using the Html Parser. This is obtained here so it will not be obtained twice in each parsing method

            //       Source directoryUrlSource = new Source(new URL(directory.getSourceURL()));                  //this fiels comment was 09-03-2017 and change abstract method and all other field

//                BusinessEmail email = new BusinessEmail();
//                email.setBusinessEmail("test@test.com");
//                email.setCountry("UK");
//                email.setBusinessId(1L);
//                getBusinessEmailDao().save(email);
            // get and iterate all businesses


            List<Business> businessListings = getBusinessesFromDirectory(directory);


            for (Business business : businessListings) {
                // if the business has not been yet stored in the database store it
                // commented due to the allow multiple business with same domanin and title. 29-09-09
                // if (!getBusinessDao().existsBusinessWithTitleAndDomain(business.getBusinessTitle(), business.getBusinessDomain())) {
                try {
                    try {
                        // set directory
                        business.setDirectory(directory);
                        // set redundant source name information
                        business.setSourceName(directory.getSourceName());
                        // set redundant source name information
                        business.setSourceUrl(directory.getSourceURL());

                        if (business.getBusinessDomain() != null) {
                            URL url = new URL(business.getBusinessDomain());
                            String domain = url.getHost();
                            domain = domain.startsWith("www.") ? domain.substring(4) : domain;
                            business.setBusinessDomain(domain);
                        }
                        try {
                            // obtain and store emails from secondary database for this business
                            //  getEmailObtainingService().scrapeEmails(business);                              //       2/3/2017 change
                            //  getEmailObtainingService().getBusinessDetails(business);                        //      2/3/2017  change
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // save the business to the database
                        businessDao.save(business);

                    } catch (Exception e) {
                        //             logger.error("An exception occured while processing business " + business.getBusinessTitle()+ " ERROR " + e);                        //ypatahk
                    }
                    //Save the business detail for the domain and title

                    //         List<Business> businesseAdd = getEmailObtainingService().getBusinessDetails(business);                 //     3-3-2017 change

//                    int  count = businesseAdd.size();
//                    for (int index = 0; index < count; index++) {
//                        Business busineAdd = businesseAdd.get(index);
//                        business.setAddress(busineAdd.getAddress());
//                        business.setCity(busineAdd.getCity());
//                        business.setState(busineAdd.getState());
//                        business.setPhone(busineAdd.getPhone());
//                        business.setZipCode(busineAdd.getZipCode());
//                        business.setSICCode(busineAdd.getSICCode());
//                        business.setSICDescription(busineAdd.getSICDescription());
//                        business.setFax(busineAdd.getFax() == null ? "" : busineAdd.getFax());
//                        getBusinessDao().update(business);
//                    }
                } catch (Exception e) {
                    logger.error("An exception occured while updading besiness " + business.getBusinessTitle() + " ERROR " + e);
                }
            }

            // recurse through the subdirectories if maxFetchDepth has not been reached

            showCompletionDialog(businessListings, directory.getSourceName());
            List<Directory> subdirectories = getSubdirectories(directory);

            for (Directory subdirectory : subdirectories) {
                try {
                    processDirectory(subdirectory, currentDepth + 1);
                } catch (Exception e) {
                    //        logger.error("An error occured while processing directory " + subdirectory,e);                   //ypathak
                }
            }


        }
    }

    private void showCompletionDialog(List<Business> businessListings, String sourceName) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new FlowLayout());

            int option = JOptionPane.showOptionDialog(
                    frame,
                    "Data extraction completed. What would you like to do?",
                    "Process Completed",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Download Excel", "Cancel"},
                    "Download Excel"
            );

            if (option == JOptionPane.YES_OPTION) {
                downloadExcel(businessListings, sourceName);
            }
        });
    }

    private void downloadExcel(List<Business> businessListings, String sourceName) {
        // Call the service or utility to generate and download the Excel file
        logger.info("Downloading Excel file...");
        try {
            exportToExcel(businessListings, sourceName);
            JOptionPane.showMessageDialog(null, "Excel file downloaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            logger.error("Failed to download Excel file", e);
            JOptionPane.showMessageDialog(null, "Failed to download Excel file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToExcel(List<Business> businessListings, String sourceName){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Business Listings");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Business Title", "Description", "Domain", "Email", "Import Date",
                "Contact First Name", "Contact Last Name", "Contact Email", "Phone", "Fax",
                "Cell Phone", "Address", "City", "State", "Province", "Country",
                "Revised Date", "Source Name", "Source URL", "ZIP Code",
                "SIC Code", "SIC Description", "Location", "Hours", "Upcoming Special Hours"
        };
        // Create header style with background color
        XSSFCellStyle headerCellStyle = (XSSFCellStyle) workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(new XSSFColor(new Color(89, 197, 234), null)); // Light Blue
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Populate rows with business data
        int rowNum = 1;
        for (Business business : businessListings) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(business.getBusinessTitle());
            row.createCell(1).setCellValue(business.getBusinessDescription());
            row.createCell(2).setCellValue(business.getBusinessDomain());
            row.createCell(3).setCellValue(business.getBusinessEmail());
            row.createCell(4).setCellValue(business.getImportDate() != null ? business.getImportDate().toString() : "");
            row.createCell(5).setCellValue(business.getContactFirstName());
            row.createCell(6).setCellValue(business.getContactLastName());
            row.createCell(7).setCellValue(business.getContactEmail());
            row.createCell(8).setCellValue(business.getPhone());
            row.createCell(9).setCellValue(business.getFax());
            row.createCell(10).setCellValue(business.getCellPhone());
            row.createCell(11).setCellValue(business.getAddress());
            row.createCell(12).setCellValue(business.getCity());
            row.createCell(13).setCellValue(business.getState());
            row.createCell(14).setCellValue(business.getProvince());
            row.createCell(15).setCellValue(business.getCountry());
            row.createCell(16).setCellValue(business.getRevisedDate() != null ? business.getRevisedDate().toString() : "");
            row.createCell(17).setCellValue(business.getSourceName());
            row.createCell(18).setCellValue(business.getSourceUrl());
            row.createCell(19).setCellValue(business.getZipCode());
            row.createCell(20).setCellValue(business.getSICCode());
            row.createCell(21).setCellValue(business.getSICDescription());

            row.createCell(22).setCellValue(business.getLocation());
            row.createCell(23).setCellValue(business.getHours());
            row.createCell(24).setCellValue(business.getUpcomingSpecialHours());
        }

        // Adjust column width for better readability
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Save the file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Excel File");
        fileChooser.setSelectedFile(new File(sourceName.toString()+"_Business_Listings.xlsx"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileOutputStream fileOut = new FileOutputStream(fileToSave)) {
                workbook.write(fileOut);
                workbook.close();
//                JOptionPane.showMessageDialog(null, "Excel file saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Failed to save Excel file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Loads all the business stubs from the directorySource and returns them as a list.
     * <p>
     * <p>
     * //     * @param directorySource The directory source used to obtain businesses
     *
     * @return The list of business stubs.
     */

    protected abstract List<Business> getBusinessesFromDirectory(Directory directory);

    /**
     * Load all subdirectory stubs from the directorySource representing the current directory.
     * <p>
     * <p>
     * //     * @param directorySource The directorySource representing the current directory
     *
     * @return A list of all subdirectories in the current directory.
     */
    protected abstract List<Directory> getSubdirectories(Directory directory);

}
