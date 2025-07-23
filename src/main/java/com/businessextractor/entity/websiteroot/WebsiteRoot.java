package com.businessextractor.entity.websiteroot;

import com.businessextractor.entity.AbstractEntity;

import javax.persistence.*;

/**
 * Website root object. This object encompases all the information relative to a
 * directory-owninig website such as www.dmoz.org or dir.yahoo.com
 * @author Bogdan Vlad
 *
 */
@Entity(name = "WebsiteRoot")
@Table(name = "website_root")
public class WebsiteRoot
	extends AbstractEntity {

	//  The name of this website root ( ex: Yahoo )
	private String name;
	
	 //  The base URL associated with the website root ( ex: http://dir.yahoo.com ).
	private String baseURL;
	
	// an optional logo for the business directory
	private byte[] logo;


	/**
	 * Returns the logo of the website root
	 * @return The logo as a byte array
	 */
	@Lob
	@Basic(fetch=FetchType.EAGER,optional=true)
	public byte[] getLogo() {
		return logo;
	}

	/**
	 * Sets the logo of this website root
	 * @param logo the logo as a byte array[]
	 */
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	/**
	 * Returns the name of this website root
	 * @return The name of the website root
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this website root
	 * @param name The name of the website root
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the base URL for the website
	 * @return the base url for the website
	 */
	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * Sets the base url for the website
	 * @param baseURL The base url for the website
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	
	
	
}
