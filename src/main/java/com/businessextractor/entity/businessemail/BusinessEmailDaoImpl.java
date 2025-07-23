/*
 * BusinessEmailDaoImpl.java
 *
 * Created on July 31, 2009, 12:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.businessextractor.entity.businessemail;

/**
 *
 * @author Avibha
 */


import com.businessextractor.dao.hibernate.AbstractHibernateDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DAO implementation for manipulating business objects.
 * @author Bogdan Vlad
 *
 */
public class BusinessEmailDaoImpl
        extends AbstractHibernateDao<BusinessEmail>
        implements BusinessEmailDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}

