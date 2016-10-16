package com.chatpolling.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.chatpolling.JDO.PollJDO;
import com.chatpolling.util.CommonUtil;
import com.chatpolling.util.PersistenceManagerUtil;

public class PollDAO {
	private static Logger logger = Logger.getLogger(PollDAO.class.getPackage().getName());

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

	@SuppressWarnings("unchecked")
	public static List<PollJDO> fetchPollsForThisStreamID(String streamID) {
		try {

			PersistenceManager pm = PersistenceManagerUtil.getPersistenceManager();
			List<PollJDO> lstPollJDO = new ArrayList<PollJDO>();
			String strQuery = "SELECT FROM " + PollJDO.class.getName();
			if (!CommonUtil.isEmptyString(streamID)) {
				strQuery += " WHERE streamID == '" + streamID + "'";
			}
			strQuery += " ORDER BY createdTime ASC";

			Query query = pm.newQuery(strQuery);

			lstPollJDO = (List<PollJDO>) query.execute();

			if (lstPollJDO != null && lstPollJDO.size() > 0) {
				lstPollJDO = (List<PollJDO>) pm.detachCopyAll(lstPollJDO);
				logger.info("Size of the lstPollJDO is :: " + lstPollJDO.size());
			} else {
				lstPollJDO = null;
				logger.info("Size of the lstPollJDO is 0");
			}

			return lstPollJDO;
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Exception :: " + e.getMessage());
			return null;
		}
	}
}
