package com.businessextractor.ui.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.businessextractor.entity.business.Business;
import com.businessextractor.services.search.PagingCriterion;
import com.businessextractor.services.search.SearchController;
import com.businessextractor.services.search.SearchResults;
import com.businessextractor.services.search.business.BusinessField;
import com.businessextractor.services.search.query.QueryField;
import com.businessextractor.spring.ApplicationContextHolder;

/**
 * Main panel containing search objects
 * @author user
 *
 */
public class SearchPanel
	extends JPanel {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(SearchPanel.class);
	
	// reference to the search engine controller used for searching
	private SearchController searchController;

	// a list of all search components deployed in this panel
	private final List<SearchComponent> searchComponents = new ArrayList<SearchComponent>();
	
	// reference to the results panel used for displaying results
	private ResultsPanel resultsPanel;
	
	public SearchPanel(ResultsPanel resultsPanel) {
		super();
		this.resultsPanel = resultsPanel;
		// obtain the search controller using the service locator
		this.searchController = (SearchController)ApplicationContextHolder.getContext().getBean("businessSearchController");
		this.setBorder(new EmptyBorder(1,1,1,1));
		initComponents();
	}
	
	/** 
	 * Creates all components required to do a search
	 */
	private void initComponents() {
		
		JPanel componentsPanel = new JPanel();
		componentsPanel.setLayout(new BorderLayout());
		componentsPanel.setBorder(new TitledBorder("Business search"));
		
		// panel containing all the search fields
		GridLayout layout = new GridLayout(8,4,0,0);
		JPanel fieldPanel = new JPanel(layout);
		
		// iterate and place on business search fields in the layout
		for (BusinessField businessField : BusinessField.values()) {
			if (businessField.getClazz().equals(String.class)) {
				// it is a String field
				ValueSearchComponent component = new ValueSearchComponent(businessField.getField(),businessField.getLabel());
				fieldPanel.add(component.getLabel());
				JPanel panel = new JPanel ();
				panel.add(component.getTextField());
				fieldPanel.add(panel);
				// add component to the list
				searchComponents.add(component);
				
			} 
		}
		
		// create a panel to hold date search components
		JPanel dateFieldPanel = new JPanel();
		dateFieldPanel.setLayout(new BoxLayout(dateFieldPanel,BoxLayout.X_AXIS));
		for (BusinessField businessField : BusinessField.values()) {
			if (businessField.getClazz().equals(Date.class)) {
				// it is a Date field
				DateRangeSearchComponent component = new DateRangeSearchComponent(businessField.getField(),businessField.getLabel());
				dateFieldPanel.add(component.getLabel());
				JPanel innerPanel = new JPanel (new GridLayout(2,1));
				innerPanel.setBorder(new EmptyBorder(2,2,2,2));
				innerPanel.add(component.getFromDateChooser());
				innerPanel.add(component.getToDateChooser());
				dateFieldPanel.add(innerPanel);
				// add component to the list
				searchComponents.add(component);
			}
		}
		
		// create a panel to hold all search components
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.Y_AXIS));
		searchPanel.add(fieldPanel);
		searchPanel.add(dateFieldPanel);
		
		componentsPanel.add(searchPanel,BorderLayout.NORTH);
		
		JButton searchButton = new JButton("Search Businesses");
		searchButton.addActionListener(new SearchActionListener());
		JPanel panel = new JPanel ();
		panel.setBorder(new EmptyBorder(5,5,5,5));
		panel.add(searchButton);
		componentsPanel.add(panel,BorderLayout.CENTER);
		
		this.add(componentsPanel);
		
	}
	
	
	/**
	 * 
	 * @author user
	 *
	 */ 
	private class SearchActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			
			// load all query fields from components 
			List<QueryField> validQueryFields = new ArrayList<QueryField>();
			
			for (SearchComponent searchComponent : searchComponents ) {
				QueryField queryField = searchComponent.getQueryField();
				// if the query field is valid
				if (queryField!=null) {
					validQueryFields.add(queryField);
				}
			}
			
			// instantiate a default paging criterion
			PagingCriterion pagingCriterion = new PagingCriterion();
			try {
				// do the search and display the results
				SearchResults results = searchController.searchByFields(validQueryFields, pagingCriterion);
				
				// add results to the results panel
				resultsPanel.display(results);
			} catch (Exception ex) {
				logger.error("An exception occured while searching "+ex,ex);
				JOptionPane.showMessageDialog(null,"An error occured search.","Error",JOptionPane.ERROR_MESSAGE);
			}
			
		}
    	
    }
	
}
