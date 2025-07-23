package com.businessextractor.ui.search;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

import com.businessextractor.services.search.query.QueryField;
import com.businessextractor.services.search.query.RangeQueryField;
import com.toedter.calendar.JDateChooser;

/**
 * A search component with 2 date choosers which will resolve itself to a RangeQueryField.
 * It is used for searching in date fields
 * @author user
 *
 */
public class DateRangeSearchComponent
	extends SearchComponent {

	// the label presenting the field 
	private JLabel label;
	
	// the from date
	private JDateChooser fromDateChooser;
	// the to date
	private JDateChooser toDateChooser;
	
	/**
	 * Standard constructor with field bound and label
	 * @param field The field that this 
	 * @param label
	 */
	public DateRangeSearchComponent(String field, String label) {
		super(field);
		this.label = new JLabel(label);
		// init the date choosers
//		fromDateChooser = new JDateChooser("dd-MM-yyyy",true);
//		fromDateChooser.setPreferredSize(new Dimension(35,17));
//		toDateChooser = new JDateChooser("dd-MM-yyyy",true);
//		toDateChooser.setPreferredSize(new Dimension(35,17));
		fromDateChooser = new JDateChooser();
		fromDateChooser.setDateFormatString("dd-MM-yyyy");
		fromDateChooser.setPreferredSize(new Dimension(35, 17));

		toDateChooser = new JDateChooser();
		toDateChooser.setDateFormatString("dd-MM-yyyy");
		toDateChooser.setPreferredSize(new Dimension(35, 17));

	}
	
	@Override
	public QueryField getQueryField() {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				
		if ( fromDateChooser.getDate() != null && toDateChooser.getDate() != null && !simpleDateFormat.format(fromDateChooser.getDate()).equals(simpleDateFormat.format(toDateChooser.getDate())) ) {
			Date fromDate = fromDateChooser.getDate();
			Date toDate = toDateChooser.getDate();
			// create and return a new range query field
			RangeQueryField queryField = new RangeQueryField();
			queryField.setField(getField());
			queryField.setFromValue(simpleDateFormat.format(fromDate));
			queryField.setToValue(simpleDateFormat.format(toDate));
			
			// return the query field
			return queryField;
		} else {
			// no query field can be extracted
			return null;
		}
	}

	public JLabel getLabel() {
		return label;
	}

	public JDateChooser getFromDateChooser() {
		return fromDateChooser;
	}

	public JDateChooser getToDateChooser() {
		return toDateChooser;
	}

}
