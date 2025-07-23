package com.businessextractor.entity.business;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.businessextractor.entity.AbstractEntity;
import com.businessextractor.entity.directory.Directory;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
/**
 * Main entity containing all information relevant to a business.
 * @author user
 *
 */
@Entity(name = "Business")
@Indexed
@Table(name = "business")
public class Business extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    // the business title field
    @Column(name = "business_title", nullable = false)
    @Field
    private String businessTitle;
    
    // the business description
    @Column(name = "businessDescription")
    @Field(store = Store.YES)
    private String businessDescription;
    
    // the business domain
    @Column
    private String businessDomain;
    
    // the business email extracted from the secondary database
    @Column
    private String businessEmail;
    
    // the import date
    @Column
    private Date importDate;
    
    // the contact's first name
    @Column
    private String contactFirstName;
    
    // the contact's last name
    @Column
    private String contactLastName;
    
    // the contact email
    @Column
    private String contactEmail;
    
    // the contact phone
    @Column
    private String phone;
    
    // the fax
    @Column
    private String fax;
    
    // the cell phone
    @Column
    private String cellPhone;

    // the location
    @Column
    private String location;

    // the hours
    @Column
    private String hours;

    // the Upcoming Special Hours
    @Column
    private String upcomingSpecialHours;

    // the addresss
    @Column
    private String address;
    
    // the city
    @Column
    private String city;
    
    // the state
    @Column
    private String state;
    
    // the province
    @Column
    private String province;
    
    // the country
    private String country;
    
    // the revised date ( date at which a modification to this business is commited 0
    private Date revisedDate;
    
    // redundant data used to persist the sourceName to the business table
    private String sourceName;
    
    // redundant data used to persist the sourceURL to the business table
    private String sourceUrl;
    
    // the parent directory of this business ( not mandatory )
    @ManyToOne
    @JoinColumn(name = "directory_id", nullable = false)
    private Directory directory;


    private String zipCode;
    
    private String SICCode;
    
    private String SICDescription;
    private String OpenCloseHours;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String images;


    /**
     * Returns the business's title
     * @return The business title
     */
    public String getBusinessTitle() {
        return businessTitle;
    }
    
    /**
     * Sets the title for this business
     * @param businessTitle The business title
     */
    public void setBusinessTitle(String businessTitle) {
        this.businessTitle = businessTitle;
    }
    
    /**
     * Returns the business's description
     * @return The business description
     */
    @Lob
    public String getBusinessDescription() {
        return businessDescription;
    }
    
    /**
     * Sets the business description
     * @param businessDescription The business description
     */
    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }
    
    /**
     * Returns the business domain name ( ex: "http://www.equalitysolutions.ro" )
     * @return The busines domain
     */
    @Basic(optional=true)
    public String getBusinessDomain() {
        return businessDomain;
    }
    
    /**
     * Sets the business domain
     * @param businessDomain The business domain
     */
    public void setBusinessDomain(String businessDomain) {
        this.businessDomain = businessDomain;
    }
    
    /**
     * Returns the date at wich this business was scraped/imported from the business directory
     * @return The business import date
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getImportDate() {
        return importDate;
    }
    
    /**
     * Sets the date at which this business was imported
     * @param importDate The import date
     */
    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }
    
    /**
     * Returns the name of this business's contact's first name
     * @return The business contact's firts name
     */
    @Basic(optional=true)
    public String getContactFirstName() {
        return contactFirstName;
    }
    
    /**
     * Sets this business's contact first name
     * @param contactFirstName The contact's first name
     */
    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }
    
    /**
     * Returns the name of this business's contact's last name
     * @return The business contact's last name
     */
    @Basic(optional=true)
    public String getContactLastName() {
        return contactLastName;
    }
    
    /**
     * Sets this business's contact first name
     *
     */
    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }
    
    /**
     * Return's this business's contact email
     * @return The business's contact email
     */
    @Basic(optional=true)
    public String getContactEmail() {
        return contactEmail;
    }
    
    /**
     * Sets this business's contact email
     * @return The business's contact email
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    
    /**
     * Return's this business's phone number
     * @return The business phone number
     */
    @Basic(optional=true)
    public String getPhone() {
        return phone;
    }
    
    /**
     * Sets the business's phone number
     * @param phone The business phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    /**
     * Return's this business's fax number
     * @return The business fax number
     */
    @Basic(optional=true)
    public String getFax() {
        return fax;
    }
    
    /**
     * Sets the business's fax number
//     * @param phone The business fax number
     */
    public void setFax(String fax) {
        this.fax = fax;
    }
    
    /**
     * Returns the business's cell phone number
     * @return The cell phone number
     */
    @Basic(optional=true)
    public String getCellPhone() {
        return cellPhone;
    }
    
    /**
     * Sets the business cell phone number
     * @param cellPhone The cell phone number
     */
    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }
    
    /**
     * Returns the business's address
     * @return The business address
     */
    @Basic(optional=true)
    public String getAddress() {
        return address;
    }
    
    /**
     * Sets the business address
     * @param address The business address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * Returns the business's city
     * @return The city
     */
    @Basic(optional=true)
    public String getCity() {
        return city;
    }
    
    /**
     * Sets the business's city
     * @param city The city
     */
    public void setCity(String city) {
        this.city = city;
    }
    
   
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    @Basic(optional=true)
    public String getProvince() {
        return province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    @Basic(optional=true)
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    /**
     * Gets the last date when this business was revised ( updated )
     * @return The last update date
     */
    @Basic(optional=true)
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getRevisedDate() {
        return revisedDate;
    }
    
    /**
     * Gets the last date when this business was revised ( updated )
     * @return The last update date
     */
    public void setRevisedDate(Date revisedDate) {
        this.revisedDate = revisedDate;
    }
    
    /**
     * Returns the directory to which this business belongs.
     * @return The owning directory
     */

    public Directory getDirectory() {
        return directory;
    }
    
    /**
     * Sets the owning directory for this business
     * @param directory The owning directory
     */
    public void setDirectory(Directory directory) {
        this.directory = directory;
    }
    
    /**
     * The business email field
     * @return The business email field
     */
    @Basic(optional=true)
    @Column(name="businessEmail")
    public String getBusinessEmail() {
        return businessEmail;
    }
    
    /**
     * Sets the business email field
//     * @param busiessEmail The business email field
     */
    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }
    
    /**
     * Gets the redundant directory sourceName field value
     * @return the sourceName field value
     */
    public String getSourceName() {
        return sourceName;
    }
    
    /**
     * Sets the redundant sourceName field value
     * @param sourceName The sourceName field value
     */
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    
    /**
     * Gets the redundant sourceUrl field value
     * @return The sourceUrl field value
     */
    public String getSourceUrl() {
        return sourceUrl;
    }
    
    /**
     * Sets the redundant sourceUrl field value
     * @param sourceUrl The sourceUrl field value
     */
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
    
    public String getSICDescription() {
        return SICDescription;
    }
    
    public void setSICDescription(String SICDescription) {
        this.SICDescription = SICDescription;
    }
    
    public String getSICCode() {
        return SICCode;
    }
    
    public void setSICCode(String SICCode) {
        this.SICCode = SICCode;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getOpenCloseHours() {
        return OpenCloseHours;
    }

    public void setOpenCloseHours(String openCloseHours) {
        OpenCloseHours = openCloseHours;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("state", this.state)
        .append("businessDomain", this.businessDomain)
        .append("businessDescription", this.businessDescription)
        .append("contactFirstName", this.contactFirstName)
        .append("contactEmail", this.contactEmail)
        .append("cellPhone", this.cellPhone)
        .append("fax", this.fax)
        .append("revisedDate", this.revisedDate)
        .append("country", this.country)
        .append("importDate", this.importDate)
        .append("phone", this.phone)
        .append("directory", this.directory)
        .append("contactLastName", this.contactLastName)
        .append("province", this.province)
        .append("businessTitle", this.businessTitle)
        .append("city", this.city)
        .append("id", this.getId())
        .append("location", this.location)
        .append("hours", this.hours)
        .append("upcomingSpecialHours", this.upcomingSpecialHours)
        .append("address", this.address)
        .append("zipCode", this.zipCode)
        .append("SICCode", this.SICCode)
        .append("SICDescription", this.SICDescription)
        .append("OpenCloseHours", this.OpenCloseHours)
        .append("images", this.images)
        .toString();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getUpcomingSpecialHours() {
        return upcomingSpecialHours;
    }

    public void setUpcomingSpecialHours(String upcomingSpecialHours) {
        this.upcomingSpecialHours = upcomingSpecialHours;
    }
}
