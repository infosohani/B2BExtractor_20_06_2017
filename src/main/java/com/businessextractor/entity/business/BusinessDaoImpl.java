package com.businessextractor.entity.business;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.businessextractor.dao.hibernate.AbstractHibernateDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DAO implementation for manipulating business objects.
 * @author Bogdan Vlad
 */
@Repository("businessDao")
@Transactional
public class BusinessDaoImpl extends AbstractHibernateDao<Business> implements BusinessDao {

	@Autowired
	private SessionFactory sessionFactory;
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private static final String EXISTS_BUSINESS_WITH_TITLE_AND_DOMAIN_HQL =
			"select count(*) from Business b where b.businessTitle = :businessTitle AND b.businessDomain = :businessDomain";

	private static final String MAX_HQL = "select max(id) from Business";

	public List<Business> loadAll() {
		return sessionFactory.getCurrentSession()
				.createQuery("from Business", Business.class)
				.getResultList();
	}
	@Override
	public boolean existsBusinessWithTitleAndDomain(String businessTitle, String businessDomain) {
		Session session = getSession();
		Query<Long> query = session.createQuery(EXISTS_BUSINESS_WITH_TITLE_AND_DOMAIN_HQL, Long.class);
		query.setParameter("businessTitle", businessTitle);
		query.setParameter("businessDomain", businessDomain);
		Long count = query.uniqueResult();
		return count != null && count > 0;
	}

	@Override
	public Business loadBusinessByTitleAndDomain(String businessTitle, String businessDomain) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Business.class)
				.add(Restrictions.eq("businessTitle", businessTitle))
				.add(Restrictions.eq("businessDomain", businessDomain));
		return (Business) criteria.uniqueResult();
	}

	@Override
	public long getGeneratedID() {
		Session session = getSession();
		Query<Long> query = session.createQuery(MAX_HQL, Long.class);
		Long maxId = query.uniqueResult();
		return maxId != null ? maxId : 0L;
	}
}

