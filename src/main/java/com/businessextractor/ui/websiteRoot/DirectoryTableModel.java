package com.businessextractor.ui.websiteRoot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.businessextractor.entity.directory.Directory;
import com.businessextractor.entity.directory.DirectoryDao;

/**
 * Table model for the directories table
 * @author Bogdan Vlad
 *
 */
public class DirectoryTableModel
	extends AbstractTableModel {
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(DirectoryTableModel.class);
	
	// the actual directories model data
	private List<Directory> directories;
	
	// DAO used to quickly update directories
	private DirectoryDao directoryDao;
	
	public DirectoryTableModel(DirectoryDao directoryDao) {
		super();
		this.directoryDao = directoryDao;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		// update the directory in memory and in the DB
		Directory changedDirectory = directories.get(rowIndex);
		changedDirectory.setActivated((Boolean)value);
		directoryDao.update(changedDirectory);
	}


	@Override
	public String getColumnName(int column) {
		
		switch (column) {
			case 0: return "Directory Name";
			case 1: return "Directory URL";
			case 2: return "Activated for extraction";
		}
		
		// return empty string if no match was found.
		return "";
	}


	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex < 2) {
            return false;
        } else {
            return true;
        }
	}


	public List<Directory> getDirectories() {
		return directories;
	}


	public void setDirectories(List<Directory> directories) {
		this.directories = directories;
	}


	public int getColumnCount() {
		return 3;
	}


	public int getRowCount() {
		return directories.size();
	}

	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		try {
			switch (columnIndex) {
				case 2: return Class.forName("java.lang.Boolean");
				default: return Class.forName("java.lang.String");
			}
		} catch (ClassNotFoundException e) {
			logger.error("A Class Not Found Exception occured "+e,e);
			return null;
		}
	}


	public Object getValueAt(int rowIndex, int columnIndex) {
		Directory directory = directories.get(rowIndex);
		
		switch (columnIndex) {
			case 0:  return directory.getSourceName();
			case 1:  return directory.getSourceURL();
			case 2:  return directory.isActivated();
		}
		
		return null;
	}

}
