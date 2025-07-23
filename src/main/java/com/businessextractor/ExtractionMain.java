/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.businessextractor;

import com.businessextractor.entity.websiteroot.WebsiteRoot;
import com.businessextractor.services.parser.BusinessProcessingService;
import com.businessextractor.spring.ApplicationContextHolder;
/**
 *
 * @author Avibha
 */
public class ExtractionMain {
    private WebsiteRoot websiteRoot;
    public static void main(String[] args) {

        String processingServiceName = "";

        // should always be 1 business processing service
        BusinessProcessingService businessProcessingService = (BusinessProcessingService) ApplicationContextHolder.getContext().getBean(processingServiceName);
       
     
    }
}
