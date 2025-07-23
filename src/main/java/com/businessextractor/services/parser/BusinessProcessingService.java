package com.businessextractor.services.parser;

import com.businessextractor.entity.businessemail.BusinessEmailDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.util.List;

import com.businessextractor.entity.business.BusinessDao;
import com.businessextractor.entity.directory.Directory;
import com.businessextractor.entity.directory.DirectoryDao;
import com.businessextractor.entity.websiteroot.WebsiteRoot;
import com.businessextractor.services.email.EmailObtainingService;
import com.businessextractor.ui.websiteRoot.WebsiteRootListPanel;

import javax.swing.*;

/**
 * Top level business processing service. This class is the base class used for extension to implement
 * parsing business directories. Its implementations will dictate how and what gets parsed.
 * @author user
 *
 */
public abstract class BusinessProcessingService {
    
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BusinessProcessingService.class);
    
    /**
     *  The website root which governs the parsing of directories.
     */
    private WebsiteRoot websiteRoot;
    
    /**
     * DAO for accessing Directories from persistent storage
     */
    private DirectoryDao directoryDao;

    /**
     * DAO for accessing Businesses from persistent storage
     */
    private BusinessDao businessDao;
    
    /**
     * DAO for accessing Business Emails from persistent storage
     */
    private BusinessEmailDao businessEmailDao;
    
    /**
     * Service used to process and store emails for extracted businesses from secondary
     * database.
     */
    private EmailObtainingService emailObtainingService;
    
    /**
     *
     * @return
     */
    public EmailObtainingService getEmailObtainingService() {
        return emailObtainingService;
    }
    
    /**
     *
     * @param emailObtainingService
     */
    public void setEmailObtainingService(EmailObtainingService emailObtainingService) {
        this.emailObtainingService = emailObtainingService;
    }
    
     /**
     * get DAO for accessing Business Emails
     * 
     */
    
    public BusinessEmailDao getBusinessEmailDao() {
        return businessEmailDao;
    }
    
    /**
     *
     * @param businessEmailDao
     */
    public void setBusinessEmailDao(BusinessEmailDao businessEmailDao) {
        this.businessEmailDao = businessEmailDao;
    }
    
    public DirectoryDao getDirectoryDao() {
        return directoryDao;
    }

    /**
     * Sets the DirectoryDao
     * @param directoryDao
     */
    public void setDirectoryDao(DirectoryDao directoryDao) {
        this.directoryDao = directoryDao;
    }
    
    /**
     * Getter for the website root object
     * @return
     */
    public WebsiteRoot getWebsiteRoot() {
        return websiteRoot;
    }
    
    /**
     * Setter for the website root object
     * @param websiteRoot
     */
    public void setWebsiteRoot(WebsiteRoot websiteRoot) {
        this.websiteRoot = websiteRoot;
    }
    
    /**
     * Getter for the businessDao object.
     * @return
     */
    public BusinessDao getBusinessDao() {
        return businessDao;
    }
    
    /**
     * Setter for the businessDao object.
     * @param businessDao
     */
    public void setBusinessDao(BusinessDao businessDao) {
        this.businessDao = businessDao;
    }
    
    
    /**
     * This method does all processing needed to fetch business directories and businesses
     * from the websiteRoot associated with this BusinessProcessingService
     */
    public void processDirectories() {
        
        List<Directory> activeDirectories = directoryDao.loadAllActiveDirectoriesFromWebsiteRoot(websiteRoot);
        if(activeDirectories.size() > 0){
        for (Directory activeDirectory : activeDirectories ) {
            processActiveDirectory( activeDirectory );
        }
        }else{
            JOptionPane.showMessageDialog(new WebsiteRootListPanel(), " Directory is not activated for extraction, Please first activate directory.");
        }
    }
    
    /**
     * Processes an active directory according to subclass implementation-specific templates and parameters.
     * @param directory The directory that is going to be processed
     */
    protected abstract void processActiveDirectory( Directory directory );


    
}
