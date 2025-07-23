package com.businessextractor.dao.hibernate;

import java.lang.reflect.ParameterizedType;

import com.businessextractor.dao.DaoInterface;
import com.businessextractor.entity.AbstractEntity;
import com.businessextractor.ui.websiteRoot.WebsiteRootListPanel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.JOptionPane;

/**
 * Implements methods in the DaoInterface common to all entities.
 *
 * @author Bogdan Vlad
 * @param <Entity>
 */
@Repository
@Transactional
@SuppressWarnings("unchecked")
public abstract class AbstractHibernateDao<Entity extends AbstractEntity> implements DaoInterface<Entity> {

	public static boolean isTrue = true;

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void delete(Entity entity) {
		try {
			getSession().delete(entity);
		} catch (Exception ee) {
			JOptionPane.showMessageDialog(new WebsiteRootListPanel(), "Directory has business, so it cannot be deleted.");
			isTrue = false;
		}
	}

	@Override
	public Entity loadById(Long id) {
		return (Entity) getSession().get(getCurrentlyManagedEntityClass(), id);
	}

	@Override
	public void save(Entity entity) {
		getSession().save(entity);
		getSession().flush();

	}

	@Override
	public void update(Entity entity) {
		getSession().update(entity);
	}

	/**
	 * Gets the {@link Class} of the currently managed entity.
	 *
	 * @return the {@link Class} of the currently managed entity.
	 */
	private Class<?> getCurrentlyManagedEntityClass() {
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		return (Class<?>) parameterizedType.getActualTypeArguments()[0];
	}
}
