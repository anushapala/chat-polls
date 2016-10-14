package com.chatpolling.DAO;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.chatpolling.JDO.PollItemJDO;
import com.chatpolling.util.PersistenceManagerUtil;

public class PollItemDAO{
	
	private static Logger logger = Logger.getLogger(PollItemDAO.class.getPackage().getName());
	
	public static boolean savePollItemJDO(PollItemJDO objPollItemJDO){
		try{
			PersistenceManager pm = PersistenceManagerUtil.getPersistenceManager();
			pm.makePersistent(objPollItemJDO);
			pm.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			logger.severe("Exceptio occurred :: " + e.getMessage());
			return false;
		}
	}
	
}