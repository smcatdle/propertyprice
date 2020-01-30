/**
 * 
 */
package com.company.propertyprice.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author smcardle
 *
 */
public class HibernateUtil {
	
	private static final SessionFactory sessionFactory; 
	
	static {
		try { 
			Configuration cfg = new Configuration(); 
			sessionFactory = cfg.configure().buildSessionFactory(); 
		} catch (Throwable ex) {
			ex.printStackTrace(System.out);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static Session openSession() throws HibernateException {  
		
		Session session = sessionFactory.openSession(); 
		
		return session;
		
	} 
	
	
	public static void closeSession(Session session) throws HibernateException {  
		
		session.close();
		session = null;
	} 

}
