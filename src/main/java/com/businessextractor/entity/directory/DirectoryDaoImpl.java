package com.businessextractor.entity.directory;

import java.math.BigInteger;
import java.util.*;

import com.businessextractor.entity.business.Business;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.businessextractor.dao.hibernate.AbstractHibernateDao;
import com.businessextractor.entity.websiteroot.WebsiteRoot;

/**
 * DAO implementation that operates on persistent Directory objects.
 *
 * @author Bogdan Vlad
 */
@Repository
public class DirectoryDaoImpl extends AbstractHibernateDao<Directory> implements DirectoryDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Use fully qualified class name in HQL
    private static final String LOAD_ALL_ACTIVE_DIRECTORIES_FROM_WEBSITE_ROOT_HQL =
            "from Directory d join fetch d.websiteRoot wr where d.activated = true AND wr.id = :websiteRootId";

    private static final String LOAD_ALL_DIRECTORIES_FROM_WEBSITE_ROOT_HQL =
            "from Directory d where d.websiteRoot.id = :websiteRootId";

    private static final String EXISTS_DIRECTORY_WITH_SOURCE_NAME_AND_SOURCE_URL_HQL =
            "select count(*) from Directory d where d.sourceName = :sourceName and d.sourceURL = :sourceURL";

    @Override
    public boolean existsDirectoryWithSourceNameAndSourceURL(String sourceName, String sourceURL) {
        String sql = "SELECT COUNT(*) FROM Directory WHERE sourceName = :sourceName AND sourceURL = :sourceURL";
        Query<Long> query = getSession().createQuery(sql);
        query.setParameter("sourceName", sourceName);
        query.setParameter("sourceURL", sourceURL);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public List<Directory> loadAllActiveDirectoriesFromWebsiteRoot(WebsiteRoot websiteRoot) {

        String sql = "SELECT d.id, d.activated, d.sourceName, d.sourceURL, " +
                "d.websiteroot_id, d.nextbits_category_id, d.parantid FROM directory d JOIN website_root wr ON d.websiteroot_id = wr.id WHERE d.activated = true AND wr.id = :websiteRootId";
        Query<Object[]> query = getSession().createNativeQuery(sql);
        query.setParameter("websiteRootId", websiteRoot.getId());
        List<Object[]> rows = query.getResultList();
        List<Directory> result = new ArrayList<>();
        for (Object[] row : rows) {
            Directory directory = new Directory();
            directory.setId(((BigInteger) row[0]).longValue());
            directory.setActivated((boolean) row[1]);
            directory.setSourceName(row[2].toString());
            directory.setSourceURL(row[3].toString());
            directory.setWebsiteRoot(websiteRoot);

            // Now, fetch the related Business entity using the directory id
            String businessQuery = "SELECT id,address,businessEmail,businessDescription,businessDomain,businessTitle,cellPhone,city,contactEmail,contactFirstName,contactLastName,country,fax,importDate,phone,province,revisedDate,sourceName,sourceUrl,state,directory_id,zipCode,SICCode,SICDescription FROM Business b WHERE b.directory_id = :directoryId";
            Query<Business> businessQueryObj = getSession().createNativeQuery(businessQuery);
            businessQueryObj.setParameter("directoryId", directory.getId());
            Set<Business> businesses = new HashSet<>(businessQueryObj.getResultList());
            directory.setBusinesses(businesses);
            result.add(directory);
        }
        return (result != null && !result.isEmpty()) ? result : Collections.emptyList();
    }

    @Override
    public List<Directory> loadAllDirectoriesFromWebsiteRoot(WebsiteRoot websiteRoot) {
        String sql = "SELECT id, activated, sourceName, sourceURL, websiteroot_id, nextbits_category_id, parantid FROM directory WHERE websiteroot_id = :websiteRootId";
        Query<Object[]> query = getSession().createNativeQuery(sql);
        query.setParameter("websiteRootId", websiteRoot.getId());
        List<Object[]> rows = query.getResultList();
        List<Directory> result = new ArrayList<>();
        for (Object[] row : rows) {
            Directory directory = new Directory();
            directory.setId(((BigInteger) row[0]).longValue());
            directory.setActivated((boolean) row[1]);
            directory.setSourceName(row[2].toString());
            directory.setSourceURL(row[3].toString());
            directory.setWebsiteRoot(websiteRoot);

            // Now, fetch the related Business entity using the directory id
            String businessQuery = "SELECT id,address,businessEmail,businessDescription,businessDomain,businessTitle,cellPhone,city,contactEmail,contactFirstName,contactLastName,country,fax,importDate,phone,province,revisedDate,sourceName,sourceUrl,state,directory_id,zipCode,SICCode,SICDescription FROM Business b WHERE b.directory_id = :directoryId";
            Query<Business> businessQueryObj = getSession().createNativeQuery(businessQuery);
            businessQueryObj.setParameter("directoryId", directory.getId());
            Set<Business> businesses = new HashSet<>(businessQueryObj.getResultList());
            directory.setBusinesses(businesses);
            result.add(directory);
        }
        return (result != null && !result.isEmpty()) ? result : Collections.emptyList();
    }
}