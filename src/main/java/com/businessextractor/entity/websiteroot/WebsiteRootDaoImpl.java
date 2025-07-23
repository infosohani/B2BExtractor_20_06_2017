package com.businessextractor.entity.websiteroot;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.businessextractor.dao.hibernate.AbstractHibernateDao;

@Repository("websiteRootDao")
@Transactional
public class WebsiteRootDaoImpl extends AbstractHibernateDao<WebsiteRoot> implements WebsiteRootDao {

    private final HibernateTemplate hibernateTemplate;
    @Autowired
    public WebsiteRootDaoImpl(SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
        setSessionFactory(sessionFactory);
    }
    // HQL Queries
    private static final String LOAD_ALL_HQL = "from WebsiteRoot";
    /**
     * Load all WebsiteRoot entities from the DB.
     */
    @SuppressWarnings("unchecked")
    public List<WebsiteRoot> loadAll() {
        return (List<WebsiteRoot>) hibernateTemplate.find(LOAD_ALL_HQL);
    }
}
