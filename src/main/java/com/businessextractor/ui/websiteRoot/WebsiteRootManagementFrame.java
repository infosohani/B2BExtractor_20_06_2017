package com.businessextractor.ui.websiteRoot;

import com.businessextractor.dao.hibernate.AbstractHibernateDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import com.businessextractor.entity.directory.Directory;
import com.businessextractor.entity.directory.DirectoryDao;
import com.businessextractor.entity.websiteroot.WebsiteRoot;
import com.businessextractor.services.directory.DirectoryManipulationService;
import com.businessextractor.services.directory.exception.DirectoryValidationException;
import com.businessextractor.spring.ApplicationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Frame containing all neccesary elements to manage a website root's directories.
 * @author Bogdan Vlad
 *
 */
//@Service
public class WebsiteRootManagementFrame
	extends JFrame {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(WebsiteRootManagementFrame.class);

	// the website root managed by this frame
	private WebsiteRoot websiteRoot;
	
	// DAO used to retrieve objects
	private DirectoryDao directoryDao;

	// table containing directory listings
	private JTable directoriesTable;
	
	// table model containing directories
	private DirectoryTableModel directoryTableModel;
	
	// components required to create a new category
	private JTextField directorySourceNameField;
	private JTextField directorySourceURLField;
	private JCheckBox directoryActivatedCheckbox;
        private JButton manageBtn ;
	
	public WebsiteRootManagementFrame(WebsiteRoot websiteRoot, DirectoryDao directoryDao,JButton btn) throws HeadlessException {
		super();
		this.websiteRoot = websiteRoot;
		this.directoryDao = directoryDao;
                this.manageBtn = btn;
		this.initComponents();
	}
	
	/**
	 * Initializes all GUI components for this frame
	 */
	private void initComponents () {
		this.setMinimumSize(new Dimension(800,0));
		this.setVisible(true);
		this.setTitle("Directory management for "+websiteRoot.getBaseURL());

		List<Directory> directories = directoryDao.loadAllDirectoriesFromWebsiteRoot(websiteRoot);

		directoryTableModel = new DirectoryTableModel(directoryDao);
		directoryTableModel.setDirectories(directories);
		
		directoriesTable = new JTable(directoryTableModel);
		// automatically create a row sorter
	//	directoriesTable.setAutoCreateRowSorter(true);
		// set the width of the third column to 140px
	    int columnIndex  = 2;
	    TableColumn column = directoriesTable.getColumnModel().getColumn(columnIndex);
	    column.setPreferredWidth(140);
	    column.setMaxWidth(140);
	    // set the width of the first column to 240px
	    columnIndex = 0;
	    column = directoriesTable.getColumnModel().getColumn(columnIndex);
	    column.setPreferredWidth(240);
	    column.setMaxWidth(240);
	    // set the selection type to single selection
		directoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane directoriesScrollPane = new JScrollPane(directoriesTable);
		
		// add the directories scroll pane
		this.add(directoriesScrollPane,BorderLayout.NORTH);
		
		// add the delete button
		JButton deleteButton = new JButton("Delete selected directory");
		deleteButton.addActionListener(new DeleteDirectoryActionListener());
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(deleteButton);
		
		// sets up the "New Directory" feature components
		initNewDirectoryComponents();
		
		this.add(buttonPanel,BorderLayout.CENTER);
		this.pack();
                addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent ee){
                 manageBtn.setEnabled(true);
                }
                });
	}
	
	/**
	 * Sets up all necessary UI and Listener components to add a new directory.
	 */
	private void initNewDirectoryComponents() {
		directorySourceNameField = new JTextField(10);
		directorySourceURLField = new JTextField(10);
		directoryActivatedCheckbox = new JCheckBox("Activated for extraction");
		
		JPanel newDirectoryPanel = new JPanel();
		newDirectoryPanel.setBorder(new TitledBorder("Add a new directory"));
		newDirectoryPanel.add(new JLabel("Source name:"));
		newDirectoryPanel.add(directorySourceNameField);
		newDirectoryPanel.add(new JLabel("Source URL:"));
		newDirectoryPanel.add(directorySourceURLField);
		newDirectoryPanel.add(directoryActivatedCheckbox);
		
		JButton saveDirectoryButton = new JButton("Save");
		saveDirectoryButton.addActionListener(new SaveDirectoryActionListener());
		newDirectoryPanel.add(saveDirectoryButton);
		
		this.add(newDirectoryPanel,BorderLayout.SOUTH);
	}	
	
	/**
	 * Action listener used for deleting a directory
	 * @author user
	 *
	 */
	private class DeleteDirectoryActionListener implements ActionListener {
		
		// reference to the directory manipulation service used for operating on diretories
		private DirectoryManipulationService directoryManipulationService;
		
		/**
		 * Default constructor
		 */
		public DeleteDirectoryActionListener() {
			directoryManipulationService = (DirectoryManipulationService) ApplicationContextHolder.getContext().getBean("directoryManipulationService");
		}
		
		
		public void actionPerformed(ActionEvent e) {
			// get  selected row and delete corresponding directory
			int selectedRow = directoriesTable.getSelectedRow();
			// if a row was deleted remove the directory
                        AbstractHibernateDao.isTrue = true;
			if (selectedRow >= 0 ) {	
				Directory directory = directoryTableModel.getDirectories().get(selectedRow);
				
				// delete the driectory from the database
				directoryManipulationService.deleteDirectory(directory);
				
				// remove it from the table model
                                if(AbstractHibernateDao.isTrue == true){
				directoryTableModel.getDirectories().remove(selectedRow);
				
				// signal data changes ( update the view )
				directoryTableModel.fireTableDataChanged();
                                }
			}
		}
		
	}
	
	/**
	 * Action listener / controller used for saving a directory
	 */
	private class SaveDirectoryActionListener implements ActionListener {
		
		// reference to the directory manipulation service used for operating on diretories
		private DirectoryManipulationService directoryManipulationService;
		
		/**
		 * Default constructor
		 */
		public SaveDirectoryActionListener() {
			directoryManipulationService = (DirectoryManipulationService) ApplicationContextHolder.getContext().getBean("directoryManipulationService");
		}
		
		public void actionPerformed(ActionEvent e) {
			
			String sourceName = directorySourceNameField.getText();
			String sourceURL = directorySourceURLField.getText();
			boolean activated = directoryActivatedCheckbox.isSelected();
			
			try {
				// success, save the directory and update the model of the tablse
				Directory directory = new Directory();
				directory.setSourceName(sourceName);
				directory.setSourceURL(sourceURL);
				directory.setActivated(activated);
				directory.setWebsiteRoot(websiteRoot);

				// attempt to save the directory
				directoryManipulationService.saveDirectory(directory);
				
				// add the directory to the model
				directoryTableModel.getDirectories().add(directory);
				directoryTableModel.fireTableDataChanged();
				
				//clear fields by returning them to their original state ( update the view )
				directorySourceNameField.setText("");
				directorySourceURLField.setText("");
				directoryActivatedCheckbox.setSelected(false);
				
			} catch (DirectoryValidationException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(),"Validation exception",JOptionPane.ERROR_MESSAGE);
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
	}
	
}
