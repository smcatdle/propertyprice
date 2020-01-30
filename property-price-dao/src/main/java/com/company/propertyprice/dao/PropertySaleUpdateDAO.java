package com.company.propertyprice.dao;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.company.propertyprice.model.PropertySaleUpdate;
import com.company.propertyprice.util.HibernateUtil;



public class PropertySaleUpdateDAO {

    private static final Log logger = LogFactory.getLog(PropertySaleUpdateDAO.class);
	
    private static PropertySaleUpdateDAO instance = null;
    
    
    private PropertySaleUpdateDAO() {
    }
    
    public static PropertySaleUpdateDAO getInstance() {
    	
    	if (instance == null) {
    		instance = new PropertySaleUpdateDAO();
    	}
    	
    	return instance;
    }
    
	public void persist(PropertySaleUpdate transientInstance) {
		logger.debug("persisting PropertySaleUpdate instance");
		Transaction t =  null;
		Session session = null;
		
		try {
		    	session = HibernateUtil.openSession();
		    
			t = session.beginTransaction();
			session.persist(transientInstance);		
			t.commit();
			
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);

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

	public void attachDirty(PropertySaleUpdate instance) {
		logger.debug("attaching dirty PropertySaleUpdate instance");
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
	
	public void attachClean(PropertySaleUpdate instance) {
		logger.debug("attaching clean PropertySaleUpdate instance");
		Transaction t =  null;
		Session session = null;
		
		try {
		    	session = HibernateUtil.openSession();
		    
			t = session.beginTransaction();
			session.lock(instance, LockMode.NONE);
			t.commit();
			
			logger.debug("attach successful");
		} catch (RuntimeException re) {
			logger.error("attach failed", re);

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

	public void update(PropertySaleUpdate instance) {
		logger.debug("updating clean PropertySaleUpdate instance");
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
	
	public void delete(PropertySaleUpdate persistentInstance) {
		logger.debug("deleting PropertySaleUpdate instance");
		Session session = null;
		Transaction t =  null;
		
		try {
		    	session = HibernateUtil.openSession();
		    
			t = session.beginTransaction();
			session.delete(persistentInstance);	
			t.commit();
			
			logger.debug("delete successful");
		} catch (RuntimeException re) {
			logger.error("delete failed", re);

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

	public PropertySaleUpdate merge(PropertySaleUpdate detachedInstance) {
		logger.debug("merging PropertySaleUpdate instance");
		Session session = null;
		
		try {
		    	session = HibernateUtil.openSession();
		    
			PropertySaleUpdate result = (PropertySaleUpdate) session.merge(detachedInstance);	
			
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
		}
	}

	public PropertySaleUpdate findById(int id) {
		logger.debug("getting PropertySaleUpdate instance with id: " + id);
		Session session = null;
		
		try {	
			session = HibernateUtil.openSession();

			PropertySaleUpdate instance = (PropertySaleUpdate) session.get(
					"com.company.propertyprice.model.PropertySaleUpdate", id);	
			
			if (instance == null) {
				logger.debug("get successful, no instance found");
			} else {
				logger.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
		}
	}

}
