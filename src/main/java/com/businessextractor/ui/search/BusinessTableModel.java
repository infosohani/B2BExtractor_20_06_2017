package com.businessextractor.ui.search;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.businessextractor.entity.business.Business;

/**
 * Table model for the business search resutls table.
 * @author Bogdan Vlad
 *
 */
public class BusinessTableModel
	extends AbstractTableModel {
	
	private List<Business> businesses;
	
	/**
	 * Standard model constructor with business array parameter
	 * @param businesses The businesses that will be displayed ( the actual model data )
	 */
	public BusinessTableModel(List<Business> businesses) {
		super();
		this.businesses = businesses;
	}	

	
	@Override
	public String getColumnName(int column) {
		
		switch (column) {
			case 0: return "Business Title";
			case 1: return "Business Domain";
                        case 2: return "Business Email";
			case 3: return "Business Description";
		}
		
		// return empty string if no match was found.
		return "";
	}
	
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 4;
	}


	public int getRowCount() {
		// TODO Auto-generated method stub
		return businesses.size();
	}


	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0: return businesses.get(rowIndex).getBusinessTitle();
			case 1: return businesses.get(rowIndex).getBusinessDomain();
                        case 2: return businesses.get(rowIndex).getBusinessEmail();
			case 3: return businesses.get(rowIndex).getBusinessDescription();
			default: return null;
		}
	}

	public List<Business> getBusinesses() {
		return businesses;
	}


	public void setBusinesses(List<Business> businesses) {
		this.businesses = businesses;
	}
}
