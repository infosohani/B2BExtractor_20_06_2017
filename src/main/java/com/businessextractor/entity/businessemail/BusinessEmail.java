/*
 * Email.java
 *
 * Created on July 31, 2009, 10:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.businessextractor.entity.businessemail;

/**
 *
 * @author Avibha
 */



import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import com.businessextractor.entity.AbstractEntity;
import javax.persistence.Table;

/**
 * Main entity containing all information relevant to a business.
 * @author user
 *
 */
@Entity
//@Indexed(index="businessemail")
@Table(name = "businessemail")
public class BusinessEmail extends AbstractEntity {
    
    // the business email field
    private String businessEmail;
    
    private long businessId;
    
  
    @Basic(optional=true)
    public long getBusinessId() {
        return businessId;
    }
    
    public void setBusinessId(long businessId) {
        this.businessId = businessId;
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
     * @param busiessEmail The business email field
     */
    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("businessEmail", this.businessEmail)
        .append("businessId", this.businessId)
        .append("Id", this.getId())
        .toString();
    }
    
    
}

