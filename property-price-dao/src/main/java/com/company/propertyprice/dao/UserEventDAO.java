package com.company.propertyprice.dao;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.company.propertyprice.model.UserEvent;
import com.company.propertyprice.util.HibernateUtil;



public class UserEventDAO {

    private static final Log logger = LogFactory.getLog(UserEventDAO.class);
	
    private static UserEventDAO instance = null;
    
    
    private UserEventDAO() {
    }
    
    public static UserEventDAO getInstance() {
    	
    	if (instance == null) {
    		instance = new UserEventDAO();
    	}
    	
    	return instance;
    }
   
    public void attachDirty(UserEvent instance) {
	
	logger.debug("attaching dirty UserEvent instance");
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
	
}