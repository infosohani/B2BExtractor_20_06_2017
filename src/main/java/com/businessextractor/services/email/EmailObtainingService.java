package com.businessextractor.services.email;

import com.businessextractor.entity.businessemail.BusinessEmail;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import com.businessextractor.entity.business.Business;
import com.businessextractor.util.EmailUtils;
import com.businessextractor.util.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Service used for obtaining and storing email addresses for a business once
 * it has been extracted from a directory.
 * @author user
 *
 */
public class EmailObtainingService extends JdbcDaoSupport {

    private static final String LOAD_EMAILS_BY_DOMAIN = "select Email from businessdetail where businessdetail.WebAddress like ?";
    private static final String LOAD_EMAILS_BY_TITLE = "select Email from businessdetail where businessdetail.CompanyName like ?";
    private static final String LOAD_BUSINESS_DETAILS_BY_DOMAIN = "select * from businessdetail where businessdetail.WebAddress like ?";
    private static final String LOAD_BUSINESS_DETAILS_BY_TITLE = "select * from businessdetail where businessdetail.CompanyName like ?";

    /**
     * For a given persistent Business object it tries to obtain and store
     * all email addresses from the secondary database.
     * @param business
     */
    public void scrapeEmails(Business business) {
        // scrape emails only if business has a domain name
        if (business.getBusinessDomain() != null) {
            // scrape and store e-mails here
            List<String> emails = getEmails(business);
            if (emails.size() > 0) {
                // output just the first email
                business.setBusinessEmail(StringUtils.join(emails));
            }
        }
    }

    /**
     * It returns the e-mails for a given business domain from a database
     * @param business The business from which the domain name would be obtain
     * @return result The list of e-mails corresponding to a business domain
     */
    public List<String> getEmails(Business business) {

        // obtain a cropped domain name
        String croppedDomainName = EmailUtils.cropBusinessURL(business.getBusinessDomain());
        String businesstitle = business.getBusinessTitle();
        final List<String> result = new ArrayList<String>();
        if (!croppedDomainName.equals("")) {
            if (croppedDomainName.startsWith("www")) {
                croppedDomainName = croppedDomainName.substring(4);
            }
            croppedDomainName = "%" + croppedDomainName + "%";
            this.getJdbcTemplate().query(LOAD_EMAILS_BY_DOMAIN, new Object[]{croppedDomainName}, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException,
                        DataAccessException {
                    while (rs.next()) {
                        result.add(rs.getString("Email"));
                    }
                    return null;
                }
            });
        }
        System.out.println("#@#@# " + result.size());
        if (croppedDomainName.equals(null) || croppedDomainName.equals("") || result.size() == 0) {

            this.getJdbcTemplate().query(LOAD_EMAILS_BY_TITLE, new Object[]{businesstitle}, new ResultSetExtractor<Object>() {

                public Object extractData(ResultSet rs) throws SQLException,
                        DataAccessException {
                    while (rs.next()) {
                        result.add(rs.getString("Email"));
                    }
                    return null;
                }
            });
        }

        return result;
    }

    /**
     * It returns the e-mails for a given business domain from a database
     * @param business The business from which the domain name would be obtain
     * @return result The list of e-mails corresponding to a business domain
     */
    public List<BusinessEmail> getBusinessEmails(final Business business) {

        // obtain a cropped domain name
        String croppedDomainName = EmailUtils.cropBusinessURL(business.getBusinessDomain());
        String businesstitle = business.getBusinessTitle();
        final List<BusinessEmail> result = new ArrayList<BusinessEmail>();

        if (croppedDomainName.equals(null) || croppedDomainName.equals("")) {
            // businesstitle = "%"+businesstitle+"%";
            this.getJdbcTemplate().query(LOAD_EMAILS_BY_TITLE, new Object[]{businesstitle}, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException,
                        DataAccessException {
                    while (rs.next()) {
                        BusinessEmail businessEmail = new BusinessEmail();
                        businessEmail.setBusinessEmail(rs.getString("Email"));
                        result.add(businessEmail);
                    }
                    return result;
                }
            });
        } else {
            if (croppedDomainName.startsWith("www")) {
                croppedDomainName = croppedDomainName.substring(4);
            }
            croppedDomainName = "%" + croppedDomainName + "%";
            this.getJdbcTemplate().query(LOAD_EMAILS_BY_DOMAIN, new Object[]{croppedDomainName}, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException,
                        DataAccessException {
                    while (rs.next()) {
                        BusinessEmail businessEmail = new BusinessEmail();
                        businessEmail.setBusinessEmail(rs.getString("Email"));
                        result.add(businessEmail);
                    }
                    return result;
                }
            });
        }
        return result;
    }

    /**
     * It returns the business detalis of a given business domain from a database
     * @param business The business from which the domain name would be obtain
     * @return result The list of details to a business domain
     */
    public List<Business> getBusinessDetails(final Business business) {

        // obtain business URL(Web Address)
        String domainName = EmailUtils.cropBusinessURL(business.getBusinessDomain());
        domainName = domainName == null ? "" : domainName;
        String businesstitle = business.getBusinessTitle();
        final List<Business> result = new ArrayList<Business>();
        if (!domainName.equals("")) {
            if (domainName.startsWith("www")) {
                domainName = domainName.substring(4);
            }
            domainName = "%" + domainName + "%";
            this.getJdbcTemplate().query(LOAD_BUSINESS_DETAILS_BY_DOMAIN, new Object[]{domainName}, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException,
                        DataAccessException {
                    while (rs.next()) {
                        Business business = new Business();
                        business.setAddress(rs.getString("Address"));
                        business.setCity(rs.getString("City"));
                        business.setState(rs.getString("State"));
                        business.setPhone(rs.getString("PhoneNumber"));
                        business.setFax(rs.getString("FaxNumber"));
                        business.setSICCode(rs.getString("SICCode"));
                        business.setSICDescription(rs.getString("SICDescription"));
                        business.setZipCode(rs.getString("ZipCode"));
                        result.add(business);
                    }
                    return result;
                }
            });
        }

        if (domainName.equals(null) || domainName.equals("") || result.size() == 0) {
            this.getJdbcTemplate().query(LOAD_BUSINESS_DETAILS_BY_TITLE, new Object[]{businesstitle}, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException,
                        DataAccessException {
                    while (rs.next()) {
                        //  Business business = new Business();
                        business.setAddress(rs.getString("Address"));
                        business.setCity(rs.getString("City"));
                        business.setState(rs.getString("State"));
                        business.setPhone(rs.getString("PhoneNumber"));
                        business.setFax(rs.getString("FaxNumber"));
                        business.setSICCode(rs.getString("SICCode"));
                        business.setSICDescription(rs.getString("SICDescription"));
                        business.setZipCode(rs.getString("ZipCode"));

                        result.add(business);
                    }
                    return result;
                }
            });
        }

        return result;
    }
}
