package com.company.propertyprice.dao;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.company.propertyprice.model.UserCrash;
import com.company.propertyprice.util.HibernateUtil;



public class UserCrashDAO {

    private static final Log logger = LogFactory.getLog(UserCrashDAO.class);
	
    private static UserCrashDAO instance = null;
    
    
    private UserCrashDAO() {
    }
    
    public static UserCrashDAO getInstance() {
    	
    	if (instance == null) {
    		instance = new UserCrashDAO();
    	}
    	
    	return instance;
    }
   
    public void attachDirty(UserCrash instance) {
	
	logger.debug("attaching dirty UserCrash instance");
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