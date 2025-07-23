package com.businessextractor.ui.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import com.businessextractor.entity.business.Business;
import com.businessextractor.entity.business.BusinessDao;
import com.businessextractor.services.search.PagingCriterion;
import com.businessextractor.services.search.SearchCommand;
import com.businessextractor.services.search.SearchController;
import com.businessextractor.services.search.SearchResults;
import com.businessextractor.spring.ApplicationContextHolder;
import com.businessextractor.ui.business.BusinessEditFrame;

/**
 * Panel class that is used to display the results of a Business search, navigate the objects,
 * and manipulate them
 * @author user
 *
 */
@SuppressWarnings(value="unchecked")
public class ResultsPanel
	extends JPanel {
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(ResultsPanel.class);

	// search controller used to do a re-search
	private SearchController searchController;
	
	// the currently managed model
	private SearchResults searchResults;
	
	// The JTable holding the displayed search results
	private JTable resultsTable;
	
	// the table model holding the data that will be displayed in resultsTable
	private BusinessTableModel businessTableModel;
	
	// the label containing paging information
	private JLabel pagingLabel = new JLabel ("No results");
	
	// buttons controling resultset navigation
	private JButton nextPageButton;
	private JButton previousPageButton;
	
	/**
	 * Standard constructor which initializes components
	 */
	public ResultsPanel() {
		// locate the business search controller from the spring application context
		this.searchController = (SearchController)ApplicationContextHolder.getContext().getBean("businessSearchController");
		// set layout 
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(4,4,4,4));
		
		// init components 
		initComponents();
	}
	
	/**
	 * Initializes all required components for this view
	 */
	private void initComponents() {
		// initialize the results table and add it to the north section
		this.resultsTable = new JTable();
		JScrollPane tableScrollPane = new JScrollPane(resultsTable);
		tableScrollPane.setPreferredSize(new Dimension(0,150));
		this.add(tableScrollPane,BorderLayout.NORTH);
		
		// initialize the paging components
		nextPageButton = new JButton(">");
		nextPageButton.setEnabled(false);
		nextPageButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// advance to the next page by modifiying the paging criterion
				PagingCriterion pagingCriterion = searchResults.getPagingCriterion();
				pagingCriterion.setInitialResultOffset(pagingCriterion.getInitialResultOffset() + pagingCriterion.getMaxResults());
				
				// execute the new search 
				doSearch();
				
			}
			
		});
		
		previousPageButton = new JButton("<");
		previousPageButton.setEnabled(false);
		previousPageButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// advance to the next page by modifiying the paging criterion
				PagingCriterion pagingCriterion = searchResults.getPagingCriterion();
				pagingCriterion.setInitialResultOffset(pagingCriterion.getInitialResultOffset() - pagingCriterion.getMaxResults());
				
				// execute the new search 
				doSearch();
				
			}
			
		});
		
		// add the paging components to the main panel sequentially
		JPanel searchControlsPanel = new JPanel();
		searchControlsPanel.setBorder(new EmptyBorder(3,2,3,2));
		JPanel panel = new JPanel();
		panel.add(previousPageButton);
		searchControlsPanel.add(panel);
		searchControlsPanel.add(pagingLabel);
		panel = new JPanel();
		panel.add(nextPageButton);
		searchControlsPanel.add(panel);
		this.add(searchControlsPanel,BorderLayout.CENTER);
		
		
		// initialize the CRUD controls and add them to the South section
		JPanel crudPanel = new JPanel();
		JButton button = new JButton("Delete selected");
		button.addActionListener(new DeleteSelectedBusinessActionListener());
		panel = new JPanel();
		panel.add(button);
		crudPanel.add(panel);
		JButton button1 = new JButton ("View/Edit selected");
		button1.addActionListener(new EditSelectedBusinessActionListener());
		panel = new JPanel();
		panel.add(button1);
		crudPanel.add(panel);
		JButton button2 = new JButton ("New business");
		button2.addActionListener(new NewBusinessActionListener());
		panel = new JPanel();
		panel.add(button2);
		crudPanel.add(panel);
		this.add(crudPanel,BorderLayout.SOUTH);
	}
	
	/**
	 * Does the search and refreshes the model with the new data 
	 */
	private void doSearch () {
		// if there are search results do a re-search
		if (searchResults!=null) {
			SearchCommand searchCommand = new SearchCommand(searchResults.getQuery(),searchResults.getPagingCriterion());
			try {
				// execute the search and display the new results
				SearchResults searchResults = searchController.search(searchCommand);
				display(searchResults);
			} catch (Exception e1) {
				logger.info("Error occured while searching "+e1,e1);
				JOptionPane.showMessageDialog(null,"An error occured search.","Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Main method used for displaying search results in this panel. It can be used by external
	 * controlling classes or by this own class to do a repetitive page search
	 * @param searchResults
	 */
	public void display (SearchResults searchResults) {
		this.searchResults = searchResults;
		
		PagingCriterion pagingCriterion = searchResults.getPagingCriterion();
		
		// update the buttons on the display
		if (pagingCriterion.getInitialResultOffset()==0) {
			this.previousPageButton.setEnabled(false);
		} else {
			this.previousPageButton.setEnabled(true);
		}
		if (pagingCriterion.getInitialResultOffset()+pagingCriterion.getMaxResults() < searchResults.getTotalResultCount()) {
			this.nextPageButton.setEnabled(true);
		} else {
			this.nextPageButton.setEnabled(false);
		}
		
		// update and display the new model
		if (businessTableModel==null) {
			// if there was no previous model ( first search ) create one
			businessTableModel = new BusinessTableModel(searchResults.getResults());
			resultsTable.setModel(businessTableModel);
		} else {
			// else populate the previous one
			businessTableModel.setBusinesses(searchResults.getResults());
		}
		
		// signal data has changed
		businessTableModel.fireTableDataChanged();
		
		String resultText = "Results "+searchResults.getPagingCriterion().getInitialResultOffset()+
							 "-"+( searchResults.getPagingCriterion().getInitialResultOffset()+ searchResults.getPagingCriterion().getMaxResults())+ 
							 " of "+searchResults.getTotalResultCount() + " total";
		pagingLabel.setText(resultText);
	}
	
	/**
	 * Controller used for deleting the selected class in the table and uptading the model
	 * @author Bogdan Vlad
	 *
	 */
	private class DeleteSelectedBusinessActionListener implements ActionListener {
		
		// dao for deleting persistent business objects
		private BusinessDao businessDao;

		
		/**
		 * Standard constructor
		 * @param businessDao
		 */
		public DeleteSelectedBusinessActionListener() {
			super();
			// obtain a reference to the businessDao using the service locator
			this.businessDao = (BusinessDao)ApplicationContextHolder.getContext().getBean("businessDao");
		}

		public void actionPerformed(ActionEvent e) {
			
			int selectedRowIndex = resultsTable.getSelectedRow();
			if (selectedRowIndex != -1) {
				try {
					// a row was selected and its corresponding business must be deleted
					Business selectedBusiness = businessTableModel.getBusinesses().get(selectedRowIndex);
					businessDao.delete(selectedBusiness);
					
					// search the businesses again and display new results
					SearchCommand searchCommand = new SearchCommand(searchResults.getQuery(),searchResults.getPagingCriterion());
					
						// execute the search and display the new results
						SearchResults searchResults = searchController.search(searchCommand);
						// update the view
						display(searchResults);
						
				} catch (Exception e1) {
					logger.info("Error occured while deleting business "+e1,e1);
					JOptionPane.showMessageDialog(null,"An error occured while deleting business.","Error",JOptionPane.ERROR_MESSAGE);
				}
			
			}
			
		}
		
	}
	
	/**
	 * Controller used for editing/viewing a business's details
	 * @author user
	 *
	 */
	private class EditSelectedBusinessActionListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {

			int selectedRowIndex = resultsTable.getSelectedRow();
			if (selectedRowIndex != -1) {
					// a row was selected and its corresponding business must be deleted
					Business selectedBusiness = businessTableModel.getBusinesses().get(selectedRowIndex);
					// create a new business edit frame 
					new BusinessEditFrame(selectedBusiness).addWindowListener(new WindowAdapter() {
					      public void windowClosing(WindowEvent e) {
					    	// do the search again in order to include model changes.
					          doSearch();
					       }
					});
			}
		}
	}
	
	/**
	 * Controller used for creating a new business details
	 * @author user
	 *
	 */
	private class NewBusinessActionListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
				// create a new business edit frame
				new BusinessEditFrame().addWindowListener(new WindowAdapter() {
				      public void windowClosing(WindowEvent e) {
				    	  // do the search again in order to include model changes.
				          doSearch();
				       }
				});;
		}
	}
	
}
