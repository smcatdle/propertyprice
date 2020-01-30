package com.company.propertyprice.dao;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.company.propertyprice.model.Property;
import com.company.propertyprice.model.PropertySale;
import com.company.propertyprice.util.HibernateUtil;



public class DaftPropertyDAO {

    private static final Log logger = LogFactory.getLog(DaftPropertyDAO.class);
	
    private static DaftPropertyDAO instance = null;
    
    
    private DaftPropertyDAO() {
    }
    
    public static DaftPropertyDAO getInstance() {
    	
    	if (instance == null) {
    		instance = new DaftPropertyDAO();
    	}
    	
    	return instance;
    }
   
    public void attachDirty(Property instance) {
	
	logger.debug("attaching dirty DaftProperty instance");
	Transaction t =  null;
	Session session = null;
		
	try {
	    session = HibernateUtil.openSession();
			 
	    t = session.beginTransaction();
	    session.save(instance);	
	    t.commit();
			
	    logger.debug("attach successful");
			
	} catch (RuntimeException re) {
	    logger.error("attach failed", re);
			
	    if (t.isParticipating()) {
		t.rollback();
	    }
	}  finally {
			
	    session.clear();
	    session.close();
	    session = null;
	    t = null;
	}
    }
	
	public void update(Property instance) {
		logger.debug("updating clean Property instance");
		Transaction t =  null;
		Session session = null;
		
		try {
		    	session = HibernateUtil.openSession();
		    
			t = session.beginTransaction();
			session.saveOrUpdate(instance);
			t.commit();
			
			logger.debug("update successful");
		} catch (RuntimeException re) {
			logger.error("update failed", re);

			if (t.isParticipating()) {
				t.rollback();
			}
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			t = null;
		}
	}
}