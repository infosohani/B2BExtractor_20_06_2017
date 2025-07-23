package com.businessextractor.ui.search;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.businessextractor.services.search.query.QueryField;
import com.businessextractor.services.search.query.ValueQueryField;

/**
 * A simple search component consisting of a label and a text field wich will resolve to a
 * ValueQuery.
 * @author user
 *
 */
public class ValueSearchComponent
	extends SearchComponent {
	
	// the label presenting the text
	private JLabel label;
	// the textbox used for entering the field value
	private JTextField textField;
	
	/**
	 * Standard constructor with field bound and label
	 * @param field The field that this 
	 * @param label
	 */
	public ValueSearchComponent(String field, String label) {
		super(field);
		this.label = new JLabel(label);
		Font font = new Font("Dialog", Font.PLAIN, 11);
		this.label.setFont(font);
		this.textField = new JTextField(12);
	}
	
	@Override
	public QueryField getQueryField() {
		
		if (textField.getText()!=null && textField.getText().matches(".*\\S.*")) {
			ValueQueryField queryField = new ValueQueryField();
			queryField.setField(getField());
			queryField.setValue(textField.getText());
			// return the created query field
			return queryField;
		} else {
			// return null because no valid query field can be resolved
			return null;
		}
		
	}

	public JLabel getLabel() {
		return label;
	}

	public JTextField getTextField() {
		return textField;
	}



}
