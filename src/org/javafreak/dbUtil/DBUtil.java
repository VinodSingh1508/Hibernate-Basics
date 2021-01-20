package org.javafreak.dbUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBUtil {
	/*
	 1. Create SeesionFactory
	 2. Use SeesionFactory to create Session
	 3. Use session to perform DB operations
	 */
	public static SessionFactory sessionFactory= new Configuration().configure().buildSessionFactory();
	public static Session createSessionAndOpenTransaction() {
		Session session=sessionFactory.openSession();
		session.beginTransaction();
		return session;
	}
	public static String commitCloseTransactionCloseSession(Session session) {
		session.getTransaction().commit();
		session.close();
		return "Commit Successful";
	}
}
