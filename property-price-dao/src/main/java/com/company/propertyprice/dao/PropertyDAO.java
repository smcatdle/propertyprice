package com.company.propertyprice.dao;

// Generated May 3, 2014 8:42:05 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.company.geo.Coordinate;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.Property;
import com.company.propertyprice.model.PropertySale;
import com.company.propertyprice.model.PropertySaleError;
import com.company.propertyprice.util.HibernateUtil;


import static org.hibernate.criterion.Example.create;


/**
 * Home object for domain model class PropertySale.
 * @see com.company.propertyprice.model.PropertySale
 * @author Hibernate Tools
 */
public class PropertyDAO {

    private static final Log logger = LogFactory.getLog(PropertyDAO.class);
	
    private static PropertyDAO instance = null;
    
    
    private PropertyDAO() {
    }
    
    public static PropertyDAO getInstance() {
    	
    	if (instance == null) {
    		instance = new PropertyDAO();
    	}
    	
    	return instance;
    }
    
	public void persist(PropertySale transientInstance) {
		logger.debug("persisting PropertySale instance");
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

	public void attachDirty(PropertySale instance) {
		logger.debug("attaching dirty PropertySale instance");
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

	public void attachDirty(PropertySaleError instance) {
		logger.debug("attaching dirty PropertySaleError instance");
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
	
	public void attachDirty(Property instance) {
		logger.debug("attaching dirty Property instance");
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
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			t = null;
		}
	}
	
	public void attachClean(PropertySale instance) {
		logger.debug("attaching clean PropertySale instance");
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

	public void update(PropertySale instance) {
		logger.debug("updating clean PropertySale instance");
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
	
	public void delete(PropertySale persistentInstance) {
		logger.debug("deleting PropertySale instance");
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

	public PropertySale merge(PropertySale detachedInstance) {
		logger.debug("merging PropertySale instance");
		Session session = null;
		
		try {
		    	session = HibernateUtil.openSession();
		    
			PropertySale result = (PropertySale) session.merge(detachedInstance);	
			
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

	public PropertySale findById(int id) {
		logger.debug("getting PropertySale instance with id: " + id);
		Session session = null;
		
		try {	
			session = HibernateUtil.openSession();

			PropertySale instance = (PropertySale) session.get(
					"com.company.propertyprice.model.PropertySale", id);	
			
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

	public List<PropertySale> findByExample(PropertySale instance) {
		logger.debug("finding PropertySale instance by example");
		Session session = null;
		
		try {	
			session = HibernateUtil.openSession();

			List<PropertySale> results = (List<PropertySale>) session.createCriteria("com.company.propertyprice.model.PropertySale")
			.add(create(instance)).list();	
			
			logger.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			logger.error("find by example failed", re);
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
		}
	}
	
	public List<PropertySale> findByUrl(String url) {
		logger.debug("finding PropertySale instance by example");
		Query query = null;
		Session session = null;
		
		try {	
			session = HibernateUtil.openSession();

			query = session.getNamedQuery("PropertySale.findPropertyByURL")
			.setString("url", url);
			List<PropertySale> sales = query.list();
			
			logger.debug("find by url successful, result size: "
					+ sales.size());
			return sales;
		} catch (RuntimeException re) {
			logger.error("find by url failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}
	
	public List<PropertySale> findExistingAddress(Address address) {
		logger.debug("finding existing PropertySale instance by address");
		Query query = null;
		Session session = null;
		
		try {	
			session = HibernateUtil.openSession();

			query = session.getNamedQuery("PropertySale.findExistingAddress")
			.setString("address_line_1", address.getAddressLine1())
			.setString("address_line_2", address.getAddressLine2())
			.setString("address_line_3", address.getAddressLine3())
			.setString("address_line_4", address.getAddressLine4())
			.setString("address_line_5", address.getAddressLine5());
			List<PropertySale> sales = query.list();
			
			logger.debug("find existing address successful, result size: "
					+ sales.size());
			return sales;
		} catch (RuntimeException re) {
			logger.error("find existing address failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}
	
	public List<PropertySale> getPropertyByUUID(String UUID) {
		logger.debug("finding property by UUID");
		Query query = null;
		Session session = null;
		
		try {	
			long startTime = System.currentTimeMillis();
			
			session = HibernateUtil.openSession();

			query = session.getNamedQuery("PropertySale.getPropertyByUUID").setString("uuid", "%" + UUID + "%");
			
			List<PropertySale> sales = query.list();
			
			logger.info("find, result size: "
					+ sales.size());
			
			long endTime = System.currentTimeMillis();
			logger.info("DAO time is : [" + (endTime - startTime) + "] milliseconds.");
			
			return sales;
		} catch (RuntimeException re) {
			logger.error("find by UUID failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
		
	}
	
	public List<PropertySale> getProperties() {
		logger.debug("finding all PropertySales");
		Query query = null;
		Session session = null;
		
		try {	
			long startTime = System.currentTimeMillis();
			
			session = HibernateUtil.openSession();

			query = session.getNamedQuery("PropertySale.findProperties");
			
			List<PropertySale> sales = query.list();
			
			logger.debug("find all, result size: "
					+ sales.size());
			
			long endTime = System.currentTimeMillis();
			logger.info("DAO time is : [" + (endTime - startTime) + "] milliseconds.");
			
			return sales;
		} catch (RuntimeException re) {
			logger.error("find by coordinates failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
		
	}
	
	public List<PropertySale> getPropertiesWithinViewport(Coordinate topLeft, Coordinate bottomRight) {
		logger.debug("finding PropertySale by their coordinates");
		Query query = null;
		Session session = null;
		
		try {	
			long startTime = System.currentTimeMillis();
			
			session = HibernateUtil.openSession();

			query = session.getNamedQuery("PropertySale.findPropertiesWithinViewport")
				.setFloat("topLeftLat", topLeft.getLat()).setFloat("topLeftLong", topLeft.getLng()).setFloat("bottomRightLat", bottomRight.getLat()).setFloat("bottomRightLong", bottomRight.getLng());
			
			List<PropertySale> sales = query.list();
			
			logger.debug("find by coordinates successful, result size: "
					+ sales.size());
			
			long endTime = System.currentTimeMillis();
			logger.info("DAO time is : [" + (endTime - startTime) + "] milliseconds.");
			
			return sales;
		} catch (RuntimeException re) {
			logger.error("find by coordinates failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
		
	}
	
	public List<PropertySale> getPropertiesWithinViewportWithSession(Coordinate topLeft, Coordinate bottomRight, Session session) {
		logger.debug("finding PropertySale by their coordinates");
		Query query = null;
		
		try {	
			long startTime = System.currentTimeMillis();

			query = session.getNamedQuery("PropertySale.findPropertiesWithinViewport")
				.setFloat("topLeftLat", topLeft.getLat()).setFloat("topLeftLong", topLeft.getLng()).setFloat("bottomRightLat", bottomRight.getLat()).setFloat("bottomRightLong", bottomRight.getLng());
			
			List<PropertySale> sales = query.list();
			
			logger.debug("find by coordinates successful, result size: "
					+ sales.size());
			
			long endTime = System.currentTimeMillis();
			logger.info("DAO time is : [" + (endTime - startTime) + "] milliseconds.");
			
			return sales;
		} catch (RuntimeException re) {
			logger.error("find by coordinates failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			query = null;
		}
		
	}
	
	public List<PropertySale> getPropertiesByDateWithinViewport(Coordinate topLeft, Coordinate bottomRight, Date dateFrom, Date dateTo) {
		logger.debug("finding PropertySale by their coordinates and date range");
		Query query = null;
		Session session = null;
		
		try {	
			long startTime = System.currentTimeMillis();
			
			session = HibernateUtil.openSession();

			query = session.getNamedQuery("PropertySale.findPropertiesByDateWithinViewport")
				.setFloat("topLeftLat", topLeft.getLat()).setFloat("topLeftLong", topLeft.getLng()).setFloat("bottomRightLat", bottomRight.getLat()).setFloat("bottomRightLong", bottomRight.getLng()).setDate("dateFrom", dateFrom).setDate("dateTo", dateTo);
			
			List<PropertySale> sales = query.list();
			
			logger.debug("find by coordinates successful and date range, result size: "
					+ sales.size());
			
			long endTime = System.currentTimeMillis();
			logger.info("DAO time is : [" + (endTime - startTime) + "] milliseconds.");
			
			return sales;
		} catch (RuntimeException re) {
			logger.error("find by coordinates  and date range failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
		
	}
	
	public List<Integer> getGridsInsideViewPort(Coordinate topLeft, Coordinate bottomRight) {
		logger.debug("finding all property gridIds");
		Query query = null;
		Session session = null;
		
		try {	
			long startTime = System.currentTimeMillis();
			session = HibernateUtil.openSession();
			
			query = session.getNamedQuery("PropertySale.getGridsInsideViewPort")
					.setFloat("topLeftLat", topLeft.getLat()).setFloat("topLeftLong", topLeft.getLng()).setFloat("bottomRightLat", bottomRight.getLat()).setFloat("bottomRightLong", bottomRight.getLng());
			List<Integer> gridIds = query.list();
			
			logger.debug("finding all property gridIds: "
					+ gridIds.size());
			
			long endTime = System.currentTimeMillis();
			logger.info("DAO time is : [" + (endTime - startTime) + "] milliseconds.");
			
			return gridIds;
		} catch (RuntimeException re) {
			logger.error("finding all property gridIds failed", re);
			
			throw re;
			
		}  finally {
			session.clear();
			session.close();
			query = null;
		}
		
	}
	
	public List<Integer> getGridsOutsideViewPort(Coordinate topLeft, Coordinate bottomRight) {
		logger.debug("finding all property gridIds outside ViewPort");
		Query query = null;
		Session session = null;
		
		try {	
			long startTime = System.currentTimeMillis();
			
			session = HibernateUtil.openSession();
			
			query = session.getNamedQuery("PropertySale.getGridsOutsideViewPort")
					.setFloat("topLeftLat", topLeft.getLat()).setFloat("topLeftLong", topLeft.getLng()).setFloat("bottomRightLat", bottomRight.getLat());
			List<Integer> gridIds = query.list();
			
			logger.debug("finding all property gridIds: "
					+ gridIds.size());
			
			long endTime = System.currentTimeMillis();
			logger.info("DAO time is : [" + (endTime - startTime) + "] milliseconds.");
			
			return gridIds;
		} catch (RuntimeException re) {
			logger.error("finding all property gridIds failed", re);
			
			throw re;
			
		}  finally {
			session.clear();
			session.close();
			query = null;
		}
		
	}
	
	public List<PropertySale> findPropertiesInGrid(int gridId) {
		logger.debug("finding PropertySale by their gridId");
		Query query = null;
		Session session = null;
		
		try {	
			long startTime = System.currentTimeMillis();

			session = HibernateUtil.openSession();
			
			query = session.getNamedQuery("PropertySale.findPropertiesInsideGrid")
				.setInteger("gridId", gridId);
			
			List<PropertySale> sales = query.list();
			
			logger.debug("find by gridId: "
					+ sales.size());
			
			long endTime = System.currentTimeMillis();
			logger.info("DAO time is : [" + (endTime - startTime) + "] milliseconds.");
			
			return sales;
		} catch (RuntimeException re) {
			logger.error("find by gridId failed", re);
			
			throw re;
			
		}  finally {
			session.clear();
			session.close();
			query = null;
		}
		
	}
	
	public List<PropertySale> findBadAddressProperties(Coordinate topLeft, Coordinate bottomRight) {
		logger.debug("findBadAddressProperties");
		Query query = null;
		Session session = null;
		
		try {	
			long startTime = System.currentTimeMillis();
			
			session = HibernateUtil.openSession();

			query = session.getNamedQuery("PropertySale.findBadAddressProperties")
				.setFloat("topLeftLat", topLeft.getLat()).setFloat("topLeftLong", topLeft.getLng()).setFloat("bottomRightLat", bottomRight.getLat()).setFloat("bottomRightLong", bottomRight.getLng());
			
			List<PropertySale> sales = query.list();
			
			logger.debug("findBadAddressProperties, result size: "
					+ sales.size());
			
			long endTime = System.currentTimeMillis();
			logger.info("DAO time is : [" + (endTime - startTime) + "] milliseconds.");
			
			return sales;
		} catch (RuntimeException re) {
			logger.error("findBadAddressProperties  and date range failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
		
	}
	
	public synchronized Session getSession() {
	    return HibernateUtil.openSession();
	    
	}
}
