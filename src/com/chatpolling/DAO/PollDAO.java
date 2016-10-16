package com.chatpolling.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.chatpolling.JDO.PollJDO;
import com.chatpolling.util.CommonUtil;
import com.chatpolling.util.PersistenceManagerUtil;
import javax.jdo.Query;

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
	
	public static List<PollJDO> fetchPollsForThisStreamID(String streamID){
		try{
			
			PersistenceManager pm = PersistenceManagerUtil.getPersistenceManager();
			List<PollJDO> lstPollJDO = new ArrayList<PollJDO>();
			String strQuery = "SELECT FROM " + PollJDO.class.getName();
			if( !CommonUtil.isEmptyString(streamID) ){
				strQuery += " WHERE streamID == '" + streamID + "'";
			}
			strQuery += " ORDER BY createdTime DESC";
			
			Query query = pm.newQuery(strQuery);
			
			lstPollJDO = (List<PollJDO>) query.execute();
			
			if(lstPollJDO != null && lstPollJDO.size() > 0){
				lstPollJDO = (List<PollJDO>) pm.detachCopyAll(lstPollJDO);
				logger.info("Size of the lstPollJDO is :: " + lstPollJDO.size());
			}else{
				lstPollJDO = null;
				logger.info("Size of the lstPollJDO is 0");
			}
			
			return lstPollJDO;
		}catch(Exception e){
			e.printStackTrace();
			logger.severe("Exception :: " +e.getMessage());
			return null;
		}
	}
	
	public static PollJDO fetchPollsForThisPollID(String pollID){
		try{
			
			PersistenceManager pm = PersistenceManagerUtil.getPersistenceManager();
			List<PollJDO> lstPollJDO = new ArrayList<PollJDO>();
			PollJDO objPollJDO = new PollJDO(); 
			String strQuery = "SELECT FROM " + PollJDO.class.getName();
			if( !CommonUtil.isEmptyString(pollID) ){
				strQuery += " WHERE pollID == '" + pollID + "'";
			}
			
			Query query = pm.newQuery(strQuery);
			
			lstPollJDO = (List<PollJDO>) query.execute();
			
			if(lstPollJDO != null && lstPollJDO.size() > 0){
				lstPollJDO = (List<PollJDO>) pm.detachCopyAll(lstPollJDO);
				logger.info("Size of the lstPollJDO is :: " + lstPollJDO.size());
				objPollJDO = lstPollJDO.get(0);
			}else{
				objPollJDO = null;
				logger.info("Size of the lstPollJDO is 0");
			}
			
			return objPollJDO;
		}catch(Exception e){
			e.printStackTrace();
			logger.severe("Exception :: " +e.getMessage());
			return null;
		}
	}
}
