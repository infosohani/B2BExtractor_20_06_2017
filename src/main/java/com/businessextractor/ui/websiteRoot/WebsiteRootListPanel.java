package com.businessextractor.ui.websiteRoot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.businessextractor.entity.directory.DirectoryDao;
import com.businessextractor.entity.websiteroot.WebsiteRoot;
import com.businessextractor.entity.websiteroot.WebsiteRootDao;
import com.businessextractor.services.parser.BusinessProcessingService;
import com.businessextractor.services.parser.ProcessorClassMap;
import com.businessextractor.spring.ApplicationContextHolder;
import com.businessextractor.util.BusinessExtractionThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Panel used to enumerate the list of available website roots.
 * @author Bogdan Vlad
 *
 */
@Component
public class WebsiteRootListPanel
	extends JPanel {
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(WebsiteRootListPanel.class);
	
	// DAO for accessing website root objects
	private WebsiteRootDao websiteRootDao;
	public WebsiteRootListPanel () {
		// locate the website root dao service from the Spring application context. 
		this.websiteRootDao = (WebsiteRootDao)ApplicationContextHolder.getContext().getBean("websiteRootDao");
		
		// set panel specific parameters
		this.setLayout(new FlowLayout());
		
		// load and display all website root editing options
		for (WebsiteRoot websiteRoot : websiteRootDao.loadAll()) {

			// create a new panel to manage actions for this website root
			WebsiteRootManagementPanel websiteRootManagementPanel = new WebsiteRootManagementPanel(websiteRoot);
			// create a small padding
			JPanel paddingPanel = new JPanel();
			paddingPanel.setBorder(new EmptyBorder(1,1,1,1));
			paddingPanel.add(websiteRootManagementPanel);
			// add it to the main screen
			this.add(paddingPanel);
		}
	}

	/**
	 * A JPanel extension class encapsulating a 
	 * @author user
	 *
	 */
	private class WebsiteRootManagementPanel extends JPanel implements ParsingCallbackInterface {
		
		// the website root currently managed by this panel
		private WebsiteRoot websiteRoot;
		
		// button used to start the business scrape
		private JButton gatherBusinessDataButton;
		
		/**
		 * Standard constructor with websiteRoot
		 * @param websiteRoot The websiteRoot model that is managed by this panel
		 */
		public WebsiteRootManagementPanel(WebsiteRoot websiteRoot) {
			super();
			this.websiteRoot = websiteRoot;
			initComponents();
		}
		
		/**
		 * Creates all the visual components displayed in this panel
		 */
		private void initComponents() {
			TitledBorder titledBorder;
			titledBorder = BorderFactory.createTitledBorder(websiteRoot.getName());
			this.setBorder(titledBorder);
			this.setLayout(new BorderLayout());
			
			// upperPanel contains website URL and LOGO
			JPanel upperPanel = new JPanel();
			upperPanel.setLayout(new BoxLayout(upperPanel,BoxLayout.Y_AXIS));
			upperPanel.setBorder(new EmptyBorder(2,0,2,0));
			upperPanel.add(new JLabel(websiteRoot.getBaseURL()));
			this.add(upperPanel,BorderLayout.NORTH);
			
			// if the website root has a logo add it too
			if (websiteRoot.getLogo()!=null) {
				// add logo to the upperPanel
				upperPanel.add(new JLabel(new ImageIcon(websiteRoot.getLogo())));
				
			}
			
			// add the edit button
			JButton websiteRootEditButton = new JButton("Manage "+websiteRoot.getName());
			websiteRootEditButton.addActionListener(new WebsiteRootEditActionListener());
			this.add(websiteRootEditButton,BorderLayout.CENTER);
			
			// add the business scrape button
			gatherBusinessDataButton = new JButton("Gather business data");
			gatherBusinessDataButton.addActionListener(new GatherBusinessDataActionListener()); 
			this.add(gatherBusinessDataButton,BorderLayout.SOUTH);
		}
		
		/**
		 *  Activated on button press and used to start a new websiteRootManagementFrame
		 */
		private class WebsiteRootEditActionListener implements ActionListener {
		
	
			public void actionPerformed(ActionEvent e) {
				// create a new website root management frame
                                ((JButton)e.getSource()).setEnabled(false);
				WebsiteRootManagementFrame managementFrame = new WebsiteRootManagementFrame(websiteRoot,
															 (DirectoryDao)ApplicationContextHolder.getContext().getBean("directoryDao"),(JButton)e.getSource());
			  

                        }
		
		}
		
		/**
		 * Activated on button press and used to start/stop the business data gathering process
		 */
		private class GatherBusinessDataActionListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				
				// get the name of the processing service
				String processingServiceName = ApplicationContextHolder.getContext().getBeanNamesForType(ProcessorClassMap.websiteRootParserClassMap.get(websiteRoot.getName()))[0];
				
				// should always be 1 business processing service
				BusinessProcessingService businessProcessingService =(BusinessProcessingService)ApplicationContextHolder.getContext().getBean(processingServiceName);

					businessProcessingService.setWebsiteRoot(websiteRoot);
					// start a gathering thread for each service found and use the current website root
					BusinessExtractionThread thread = new BusinessExtractionThread(businessProcessingService,WebsiteRootManagementPanel.this);
					thread.start();
				
				gatherBusinessDataButton.setEnabled(false);
				gatherBusinessDataButton.setText("Gathering data...please wait");
			}
			
		}

		/**
		 *  When all parsing operations finish reset the interface
		 */
		public void parsingFinished() {
			gatherBusinessDataButton.setEnabled(true);
			gatherBusinessDataButton.setText("Gather business data");
		}
		
		
	}
	
	
}



