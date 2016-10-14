package com.chatpolling.web.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.chatpolling.DAO.PollDAO;
import com.chatpolling.JDO.PollJDO;

public class PollingHelper {
	private static Logger logger =  Logger.getLogger(PollingHelper.class.getPackage().getName());

	public static HashMap<String,Object> createPollHelper(HashMap<String,Object>pollReqMap,ArrayList<HashMap<String,Object>>pollItemsList){
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		try{
			HashMap<String,Object> response = new HashMap<String,Object>();
			response = PollDAO.createAPoll(pollReqMap);
			PollJDO objPollJDO = (PollJDO) response.get("objPollJdo");

			responseMap.put("objPollJdo", objPollJDO);
			if((boolean) response.get("success")){
				response = PollDAO.createAPollItem(pollItemsList,objPollJDO.getPollID());
				responseMap.put("pollItemJDOList", response.get("pollItemJDOList"));
			}
			
		}catch(Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return responseMap;
	}
	

}
