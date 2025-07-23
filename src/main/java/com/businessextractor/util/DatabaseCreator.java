package com.businessextractor.util;

import com.businessextractor.entity.directory.DirectoryDao;
import com.businessextractor.entity.websiteroot.WebsiteRoot;
import com.businessextractor.entity.websiteroot.WebsiteRootDao;
import com.businessextractor.spring.ApplicationContextHolder;

/**
 * 1 time used class that is used to populate the inital database. Was used in the beta version.
 * @author Bogdan Vlad
 *
 */
public class DatabaseCreator {

	private static WebsiteRootDao websiteRootDao;
	private static DirectoryDao directoryDao;
	
	public static WebsiteRoot getWebsiteRootAndCreateDirectories() {
		
		websiteRootDao = (WebsiteRootDao)ApplicationContextHolder.getContext().getBean("websiteRootDao");
		directoryDao = (DirectoryDao)ApplicationContextHolder.getContext().getBean("directoryDao");
		
		WebsiteRoot websiteRoot = new WebsiteRoot();
		websiteRoot.setBaseURL("http://www.dmoz.org");
		websiteRoot.setName("DMOZ");
		websiteRoot.setLogo(WebsiteRootUtils.getLogoAsByteArray("I:/eclipse/workspace/BusinessExtractorNewResources/dmoz.gif"));
		
		websiteRootDao.save(websiteRoot);

		websiteRoot = new WebsiteRoot();
		websiteRoot.setBaseURL("http://dir.yahoo.com");
		websiteRoot.setName("Yahoo");
		websiteRoot.setLogo(WebsiteRootUtils.getLogoAsByteArray("I:/eclipse/workspace/BusinessExtractorNewResources/yahoo.gif"));
		
		websiteRootDao.save(websiteRoot);
		
		websiteRoot = new WebsiteRoot();
		websiteRoot.setBaseURL("http://dir.tpage.com");
		websiteRoot.setName("TPage");
		websiteRoot.setLogo(WebsiteRootUtils.getLogoAsByteArray("I:/eclipse/workspace/BusinessExtractorNewResources/tpage.gif"));
		
		websiteRootDao.save(websiteRoot);
		
		websiteRoot = new WebsiteRoot();
		websiteRoot.setBaseURL("http://www.exporters.sg");
		websiteRoot.setName("Exporters");
		websiteRoot.setLogo(WebsiteRootUtils.getLogoAsByteArray("I:/eclipse/workspace/BusinessExtractorNewResources/exporters.gif"));
		
		websiteRootDao.save(websiteRoot);

		return websiteRoot;
		
	}

	
}
