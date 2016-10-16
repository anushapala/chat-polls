package com.chatpolling.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.chatpolling.JDO.PollItemJDO;
import com.chatpolling.JDO.PollJDO;
import com.chatpolling.util.CommonUtil;
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
	
	
	
	@SuppressWarnings({ "unchecked", "null" })
	public static List<PollItemJDO> fetchPollOptionsForThisPollID(String pollID){
		try{
			
			PersistenceManager pm = PersistenceManagerUtil.getPersistenceManager();
			List<PollItemJDO> lstPollItemJDO = new ArrayList<PollItemJDO>();
			String strQuery = "SELECT FROM " + PollItemJDO.class.getName();
			if( !CommonUtil.isEmptyString(pollID) ){
				strQuery += " WHERE pollID == '" + pollID + "'";
			}
			
			Query query = pm.newQuery(strQuery);
			
			lstPollItemJDO = (List<PollItemJDO>) query.execute();
			
			if(lstPollItemJDO != null && lstPollItemJDO.size() > 0){
				lstPollItemJDO = (List<PollItemJDO>) pm.detachCopyAll(lstPollItemJDO);
				logger.info("Size of the lstPollItemJDO is :: " + lstPollItemJDO.size());
			}else{
				lstPollItemJDO = null;
				logger.info("Size of the lstPollItemJDO is 0");
			}
			
			return lstPollItemJDO;
		}catch(Exception e){
			e.printStackTrace();
			logger.severe("Exception :: " +e.getMessage());
			return null;
		}
	}
}