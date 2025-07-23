package com.businessextractor.ui.business;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.businessextractor.entity.business.Business;
import com.businessextractor.entity.business.BusinessDao;
import com.businessextractor.services.business.BusinessManipulationService;
import com.businessextractor.spring.ApplicationContextHolder;
import com.businessextractor.ui.websiteRoot.WebsiteRootListPanel;



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * BusinessEditFrame contaaining all the edit fields and panels for a business.
 * @author Bogdan Vlad
 *
 */
public class BusinessEditFrame extends javax.swing.JFrame {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(BusinessEditFrame.class);
	
	private JLabel businessTitleLabel;
	private JTextField cellPhoneField;
	private JTextField businessEmailField;
	private JLabel businessEmailLabel;
	private JLabel directoryUrlValueLabel;
	private JLabel directoryUrlLabel;
	private JLabel directoryNameValueLabel;
	private JLabel directoryNameLabel;
	private JScrollPane jScrollPane1;
	private JTextField contactEmailField;
	private JLabel contactEmailLabel;
	private JButton saveButton;
	private JLabel revisedDateValueLabel;
	private JLabel importDateValueLabel;
	private JTextField countryField;
	private JTextField provinceField;
	private JTextField stateField;
	private JTextField cityField;
	private JTextField addressField;
	private JTextField faxField;
	private JTextField phoneField;
	private JTextField contactLastNameField;
	private JTextField contactFirstNameField;
	private JTextArea businessDescriptionField;
	private JTextField businessDomainField;
	private JTextField businessTitleField;
	private JLabel revisedDateLabel;
	private JLabel importDateLabel;
	private JLabel businessDomainLabel;
	private JLabel phoneLabel;
	private JLabel stateLabel;
	private JLabel countryLabel;
	private JLabel provinceLabel;
	private JLabel cityLabel;
	private JLabel addressLabel;
	private JLabel cellPhoneLabel;
	private JLabel faxLabel;
	private JLabel contactLastNameLabel;
	private JLabel contactFirstNameLabel;
	private JLabel businessDescriptionLabel;

	/// reference to the Business object model for this edit/view frame
	private Business businessTargetModel;
	
	// true if the frame has been created in edit mode, false if the frame is in new object mode
	private boolean editMode = false;
	
	public BusinessEditFrame() {
		super();
		// init the frame in new mode
		this.editMode = false;
		
		// initialize all gui related components
		initGUI();
		
		// set the title of the frame accordingly
		this.setTitle("Create new Business entry");
	}
	
	public BusinessEditFrame(Business business) {
		super();
		// set the parameters of this frame to editmode
		this.editMode = true;
		
		// set the business target model 
		setBusinessTargetModel(business);
		
		// initialize all gui components
		initGUI();
		
		// and display its values on the view components 
		displayBusiness(business);
		
		// set the title of this frame accordingly
		this.setTitle("Editing Business " + businessTargetModel.getBusinessTitle());
	}
	
	/**
	 * Populates all GUI components with values bound from the business parameters
	 * @param business The business model to set
	 */
	public void setBusinessTargetModel(Business business) {
		this.businessTargetModel = business;
	}
	
	/**
	 * Displays the current business on the view components.
	 */
	private void displayBusiness ( Business business ) {
		// populate fields with business data
		businessTitleField.setText(getTextIfNotNull (business.getBusinessTitle()));
		businessDescriptionField.setText(getTextIfNotNull (business.getBusinessDescription()));
		businessDomainField.setText(getTextIfNotNull(business.getBusinessDomain()));
		businessEmailField.setText(getTextIfNotNull(business.getBusinessEmail()));
		contactFirstNameField.setText(getTextIfNotNull(business.getContactFirstName()));
		contactLastNameField.setText(getTextIfNotNull(business.getContactLastName()));
		contactEmailField.setText(getTextIfNotNull(business.getContactEmail()));
		phoneField.setText(getTextIfNotNull(business.getPhone()));
		faxField.setText(getTextIfNotNull(business.getFax()));
		cellPhoneField.setText(getTextIfNotNull(business.getCellPhone()));
		addressField.setText(getTextIfNotNull(business.getAddress()));
		cityField.setText(getTextIfNotNull(business.getCity()));
		stateField.setText(getTextIfNotNull(business.getState()));
		provinceField.setText(getTextIfNotNull(business.getProvince()));
		countryField.setText(getTextIfNotNull(business.getCountry()));
		revisedDateValueLabel.setText(getTextIfNotNull(business.getRevisedDate()));
		importDateValueLabel.setText(getTextIfNotNull(business.getImportDate()));
		
		// assign the directory values if a directory is present
		if (business.getDirectory()!=null) {
			directoryNameValueLabel.setText(business.getDirectory().getSourceName());
			directoryUrlValueLabel.setText(business.getDirectory().getSourceURL());
		}
	}
	
	
	/**
	 * Returns an object in string mode or a blank string if object is not null
	 * @param data The data that is converted
	 * @return The object as string or "" if the object is null
	 */
	private String getTextIfNotNull ( Object data ) {
		return data == null ? "" : data.toString();
	}

	private void initGUI() {
		try {
			// make the frame visible
			this.setVisible(true);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			{
				businessTitleLabel = new JLabel();
				getContentPane().add(businessTitleLabel);
				businessTitleLabel.setText("Business Title*:");
				businessTitleLabel.setBounds(19, 18, 89, 14);
			}
			{
				businessDomainLabel = new JLabel();
				getContentPane().add(businessDomainLabel);
				businessDomainLabel.setText("Business Domain*:");
				businessDomainLabel.setBounds(19, 44, 89, 14);
			}
			{
				businessDescriptionLabel = new JLabel();
				getContentPane().add(businessDescriptionLabel);
				businessDescriptionLabel.setText("Business Description*:");
				businessDescriptionLabel.setBounds(19, 70, 107, 14);
			}
			{
				contactFirstNameLabel = new JLabel();
				getContentPane().add(contactFirstNameLabel);
				contactFirstNameLabel.setText("Contact First Name:");
				contactFirstNameLabel.setBounds(23, 171, 96, 14);
			}
			{
				contactLastNameLabel = new JLabel();
				getContentPane().add(contactLastNameLabel);
				contactLastNameLabel.setText("Contact Last Name:");
				contactLastNameLabel.setBounds(24, 197, 95, 14);
			}
			{
				phoneLabel = new JLabel();
				getContentPane().add(phoneLabel);
				phoneLabel.setText("Phone:");
				phoneLabel.setBounds(24, 250, 34, 14);
			}
			{
				faxLabel = new JLabel();
				getContentPane().add(faxLabel);
				faxLabel.setText("Fax:");
				faxLabel.setBounds(24, 276, 22, 14);
			}
			{
				cellPhoneLabel = new JLabel();
				getContentPane().add(cellPhoneLabel);
				cellPhoneLabel.setText("Cell Phone:");
				cellPhoneLabel.setBounds(24, 302, 54, 14);
			}
			{
				addressLabel = new JLabel();
				getContentPane().add(addressLabel);
				addressLabel.setText("Address:");
				addressLabel.setBounds(24, 328, 43, 14);
			}
			{
				cityLabel = new JLabel();
				getContentPane().add(cityLabel);
				cityLabel.setText("City:");
				cityLabel.setBounds(24, 354, 23, 14);
			}
			{
				stateLabel = new JLabel();
				getContentPane().add(stateLabel);
				stateLabel.setText("State:");
				stateLabel.setBounds(24, 380, 30, 14);
			}
			{
				provinceLabel = new JLabel();
				getContentPane().add(provinceLabel);
				provinceLabel.setText("Province:");
				provinceLabel.setBounds(24, 406, 45, 14);
			}
			{
				countryLabel = new JLabel();
				getContentPane().add(countryLabel);
				countryLabel.setText("Country:");
				countryLabel.setBounds(24, 432, 43, 14);
			}
			{
				importDateLabel = new JLabel();
				getContentPane().add(importDateLabel);
				importDateLabel.setText("Import Date:");
				importDateLabel.setBounds(24, 454, 62, 14);
			}
			{
				revisedDateLabel = new JLabel();
				getContentPane().add(revisedDateLabel);
				revisedDateLabel.setText("Revised Date:");
				revisedDateLabel.setBounds(24, 478, 68, 14);
			}
			{
				businessTitleField = new JTextField();
				getContentPane().add(businessTitleField);
				businessTitleField.setBounds(136, 15, 415, 21);
			}
			{
				businessDomainField = new JTextField();
				getContentPane().add(businessDomainField);
				businessDomainField.setBounds(136, 41, 415, 21);
			}
			{
				contactFirstNameField = new JTextField();
				getContentPane().add(contactFirstNameField);
				contactFirstNameField.setBounds(137, 168, 185, 21);
			}
			{
				contactLastNameField = new JTextField();
				getContentPane().add(contactLastNameField);
				contactLastNameField.setBounds(137, 194, 185, 21);
			}
			{
				phoneField = new JTextField();
				getContentPane().add(phoneField);
				phoneField.setBounds(137, 247, 185, 21);
			}
			{
				faxField = new JTextField();
				getContentPane().add(faxField);
				faxField.setBounds(137, 273, 185, 21);
			}
			{
				cellPhoneField = new JTextField();
				getContentPane().add(cellPhoneField);
				cellPhoneField.setBounds(137, 299, 185, 21);
			}
			{
				addressField = new JTextField();
				getContentPane().add(addressField);
				addressField.setBounds(137, 325, 185, 21);
			}
			{
				cityField = new JTextField();
				getContentPane().add(cityField);
				cityField.setBounds(137, 351, 185, 21);
			}
			{
				stateField = new JTextField();
				getContentPane().add(stateField);
				stateField.setBounds(137, 377, 185, 21);
			}
			{
				provinceField = new JTextField();
				getContentPane().add(provinceField);
				provinceField.setBounds(137, 403, 185, 21);
			}
			{
				countryField = new JTextField();
				getContentPane().add(countryField);
				countryField.setBounds(137, 429, 185, 21);
			}
			{
				importDateValueLabel = new JLabel();
				getContentPane().add(importDateValueLabel);
				importDateValueLabel.setBounds(137, 458, 185, 14);
			}
			{
				revisedDateValueLabel = new JLabel();
				getContentPane().add(revisedDateValueLabel);
				revisedDateValueLabel.setBounds(137, 482, 185, 14);
			}
			{
				saveButton = new JButton();
				getContentPane().add(saveButton);
				saveButton.setBounds(181, 572, 215, 21);
				if (this.editMode) {
					// the form is in edit mode
					saveButton.setText("Update Business");
				} else {
					// the form is in save mode
					saveButton.setText("Save New Business");
				}
				// add the action listener to the button
				saveButton.addActionListener(new SaveBusinessStateActionListener(this));
			}
			{
				contactEmailLabel = new JLabel();
				getContentPane().add(contactEmailLabel);
				contactEmailLabel.setText("Contact Email:");
				contactEmailLabel.setBounds(24, 223, 95, 14);
			}
			{
				contactEmailField = new JTextField();
				getContentPane().add(contactEmailField);
				contactEmailField.setBounds(137, 220, 185, 21);
			}
			{
				jScrollPane1 = new JScrollPane();
				getContentPane().add(jScrollPane1);
				jScrollPane1.setBounds(136, 68, 415, 67);
				{
					businessDescriptionField = new JTextArea();
					jScrollPane1.setViewportView(businessDescriptionField);
					businessDescriptionField.setBounds(139, 98, 378, 64);
					businessDescriptionField.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
					businessDescriptionField.setLineWrap(true);
				}
			}
			{
				directoryNameLabel = new JLabel();
				getContentPane().add(directoryNameLabel);
				directoryNameLabel.setText("Directory Name:");
				directoryNameLabel.setBounds(24, 504, 78, 14);
			}
			{
				directoryNameValueLabel = new JLabel();
				getContentPane().add(directoryNameValueLabel);
				directoryNameValueLabel.setBounds(137, 508, 259, 14);
			}
			{
				directoryUrlLabel = new JLabel();
				getContentPane().add(directoryUrlLabel);
				directoryUrlLabel.setText("Directory URL:");
				directoryUrlLabel.setBounds(24, 530, 70, 14);
			}
			{
				directoryUrlValueLabel = new JLabel();
				getContentPane().add(directoryUrlValueLabel);
				directoryUrlValueLabel.setBounds(137, 534, 259, 14);
			}
			{
				businessEmailLabel = new JLabel();
				getContentPane().add(businessEmailLabel);
				businessEmailLabel.setText("Business Email:");
				businessEmailLabel.setBounds(23, 144, 72, 14);
			}
			{
				businessEmailField = new JTextField();
				getContentPane().add(businessEmailField);
				businessEmailField.setBounds(137, 141, 414, 21);
			}
			this.setSize(571, 638);
			this.validate();
			this.repaint();
			this.getContentPane().validate();
			this.getContentPane().repaint();
		} catch (Exception e) {
			logger.error("An error occured while laying out components " +e,e);
		}
	}
	
	/**
	 * Controller used for saving a new business to the database
	 * @author Bogdan Vlad
	 *
	 */
	public class SaveBusinessStateActionListener implements ActionListener  {

		/**
		 * Logger for this class
		 */
		private final Log logger = LogFactory.getLog(SaveBusinessStateActionListener.class);
		
		// reference to the Data Access Object used for business manipulation
		private BusinessManipulationService businessManipulationService;
		
		// reference to the parent frame
		private JFrame parentFrame;
		
		public SaveBusinessStateActionListener (JFrame parentFrame) {
			this.parentFrame = parentFrame;
			businessManipulationService = (BusinessManipulationService) ApplicationContextHolder.getContext().getBean("businessManipulationService");
		}
		
		/**
		 * Main controller method which does the business state updates
		 */
		public void actionPerformed(ActionEvent e) {
			
			try {
				// create a new business if in new mode, else use the one in the target model
				Business targetBusiness = editMode == true ? businessTargetModel : new Business();
				
				populateBusinessData (targetBusiness);
				
				if (editMode) {
					// mark the revision date of the current business
					businessManipulationService.updateBusiness(targetBusiness);
				} else {
					// mark the import date of the current business
					businessManipulationService.saveBusiness(targetBusiness);
					// save email fields here if there are any 
				}
				
				// inform the user that the business was succesfully updated
				JOptionPane.showMessageDialog(null, "Business '" +targetBusiness.getBusinessTitle() + "' was saved succesfully ","Error",JOptionPane.INFORMATION_MESSAGE);
				
				// dispose of the frame
				parentFrame.dispose();
				
			} catch (Exception ex) {
				logger.error("An error occured while saving business state " +ex,ex);
				JOptionPane.showMessageDialog(null, "An error occured while saving business state" +ex,"Error",JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	/**
	 *  Populates business data from the view components
	 */
	private void populateBusinessData (Business business) {
		
		business.setBusinessTitle(getTextIfNotEmpty (businessTitleField.getText()));
		business.setBusinessDomain(getTextIfNotEmpty (businessDomainField.getText()));
		business.setBusinessDescription(getTextIfNotEmpty (businessDescriptionField.getText()));
		business.setBusinessEmail(getTextIfNotEmpty(businessEmailField.getText()));
		business.setContactFirstName(getTextIfNotEmpty (contactFirstNameField.getText()));
		business.setContactLastName(getTextIfNotEmpty (contactLastNameField.getText()));
		business.setContactEmail(getTextIfNotEmpty (contactEmailField.getText()));
		business.setPhone(getTextIfNotEmpty (phoneField.getText()));
		business.setFax(getTextIfNotEmpty (faxField.getText()));
		business.setCellPhone(getTextIfNotEmpty (cellPhoneField.getText()));
		business.setAddress(getTextIfNotEmpty(addressField.getText()));
		business.setCity(getTextIfNotEmpty(cityField.getText()));
		business.setState(getTextIfNotEmpty(stateField.getText()));
		business.setProvince(getTextIfNotEmpty(provinceField.getText()));
		business.setCountry(getTextIfNotEmpty(countryField.getText()));
		
	}
	
	/**
	 * Returns null if a string is empty, the string otherwise
	 * @param text The string that will be processed
	 * @return null if a string is empty, the string otherwise
	 */
	private String getTextIfNotEmpty (String text) {
		return text.equals("") ? null:text;
	}
	
	
}
