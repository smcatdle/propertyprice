package com.company.propertyprice.dao;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import com.company.propertyprice.model.ServerConfig;
import com.company.propertyprice.model.ServerNode;
import com.company.propertyprice.util.HibernateUtil;



public class ServerConfigDAO {

    private static final Log logger = LogFactory.getLog(ServerConfigDAO.class);
	
    private static ServerConfigDAO instance = null;
    
    
    private ServerConfigDAO() {
    }
    
    public static ServerConfigDAO getInstance() {
    	
    	if (instance == null) {
    		instance = new ServerConfigDAO();
    	}
    	
    	return instance;
    }

	public ServerConfig findServerConfigById(int id) {
		logger.debug("getting ServerConfigDAO.java instance with id: " + id);
		Session session = null;
		
		try {	
			session = HibernateUtil.openSession();

			ServerConfig instance = (ServerConfig) session.get(
					"com.company.propertyprice.model.ServerConfig", id);	
			
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

	public List<ServerNode> getServerNodes(String type) {
	    
		logger.debug("finding server nodes");
		Query query = null;
		Session session = null;
		
		try {	
			long startTime = System.currentTimeMillis();
			
			session = HibernateUtil.openSession();

			query = session.getNamedQuery("ServerNode.getServerNodes")
				.setString("type", type);
			
			List<ServerNode> serverNodes = query.list();
			
			logger.debug("find by server nodes successful, result size: "
					+ serverNodes.size());
			
			long endTime = System.currentTimeMillis();
			logger.info("DAO time is : [" + (endTime - startTime) + "] milliseconds.");
			
			return serverNodes;
		} catch (RuntimeException re) {
			logger.error("find server nodes failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
		
	}
}
