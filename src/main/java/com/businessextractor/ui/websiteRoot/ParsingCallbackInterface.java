package com.businessextractor.ui.websiteRoot;

/**
 * Implemented by view objects that wish to be notified when the parsing operation failed or completed successfully
 * @author user
 *
 */
public interface ParsingCallbackInterface {

	/**
	 * Called by the service doing the parsing once all parsing has been finished
	 */
	public void parsingFinished();
	
}
