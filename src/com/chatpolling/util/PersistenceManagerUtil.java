package com.chatpolling.util;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class PersistenceManagerUtil {

	private static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public static PersistenceManager getPersistenceManager() {
		PersistenceManager persistenceManager = pmf.getPersistenceManager();
		return persistenceManager;
	}

}