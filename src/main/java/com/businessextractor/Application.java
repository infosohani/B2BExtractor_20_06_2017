package com.businessextractor;

import com.businessextractor.spring.ApplicationContextHolder;
import com.businessextractor.ui.search.ResultsPanel;
import com.businessextractor.ui.search.SearchPanel;
import com.businessextractor.ui.websiteRoot.WebsiteRootListPanel;

import javax.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml") // Load existing XML config
@ComponentScan(basePackages = "com.businessextractor")
public class Application {

	private static final Log logger = LogFactory.getLog(Application.class);

	public static void main(String[] args) {
		try {
			// Start Spring Boot and load the existing context
			ApplicationContext APPLICATION_CONTEXT = new SpringApplicationBuilder(Application.class)
					.headless(false) // Important for Swing
					.run(args);
			ApplicationContext context = SpringApplication.run(Application.class, args);

			// Fetch the panel from Spring context (not with "new")
			WebsiteRootListPanel websiteRootListPanel = context.getBean(WebsiteRootListPanel.class);


			// Set the look and feel to that of the system
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			logger.error("An error occurred while setting the look and feel", e);
		}

		// Launch the Swing GUI
		SwingUtilities.invokeLater(Application::createAndShowGUI);
	}

	private static void createAndShowGUI() {
		JFrame topLevelFrame = new JFrame("Business Extractor");
		JPanel topLevelPanel = new JPanel();
		topLevelPanel.setLayout(new BoxLayout(topLevelPanel, BoxLayout.Y_AXIS));

		// Initialize UI components
		WebsiteRootListPanel websiteRootListPanel = new WebsiteRootListPanel();
		ResultsPanel resultsPanel = new ResultsPanel();
		SearchPanel searchPanel = new SearchPanel(resultsPanel);

		// Add components to the panel
		topLevelPanel.add(websiteRootListPanel);
		topLevelPanel.add(searchPanel);
		topLevelPanel.add(resultsPanel);

		// Set up the frame
		topLevelFrame.add(topLevelPanel);
		topLevelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		topLevelFrame.setVisible(true);
		topLevelFrame.pack();
	}
}
