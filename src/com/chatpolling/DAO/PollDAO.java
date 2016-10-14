package com.chatpolling.DAO;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.chatpolling.JDO.PollJDO;
import com.chatpolling.util.PersistenceManagerUtil;


public class PollDAO {
	private static Logger logger =  Logger.getLogger(PollDAO.class.getPackage().getName());

	
	public static boolean savePollJDO(PollJDO objPollJDO) {
		try {
			PersistenceManager pm = PersistenceManagerUtil.getPersistenceManager();
			pm.makePersistent(objPollJDO);
			pm.close();
			return true;
		} catch (Exception e) {
			logger.info("Exception :: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}	
}
