package com.chatpolling.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.chatpolling.JDO.PollItemJDO;
import com.chatpolling.JDO.PollJDO;
import com.chatpolling.util.CommonUtil;
import com.chatpolling.util.PersistenceManagerUtil;


public class PollDAO {
	private static Logger logger =  Logger.getLogger(PollDAO.class.getPackage().getName());

	public static HashMap<String,Object> createAPoll(HashMap<String,Object> pollRequestMap){
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		try{
			
				PollJDO objPollJdo = new PollJDO();
				
				objPollJdo.setPollID(CommonUtil.getUniqueId());
				objPollJdo.setStreamID((String)pollRequestMap.get("streamID"));
				objPollJdo.setPollQuestion((String)pollRequestMap.get("pollQuestion"));
				objPollJdo.setPollDescription((String)pollRequestMap.get("pollDiscription"));
				objPollJdo.setCreatedTime(new Date().getTime());
				objPollJdo.setEndedTime(0l);
				
				boolean success = saveJDOS(objPollJdo);
				responseMap.put("success", success);
				responseMap.put("objPollJdo", objPollJdo);
			
		}catch(Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return responseMap;
	}
	
	public static HashMap<String,Object> createAPollItem(ArrayList<HashMap<String,Object>> pollItemsList,String pollID){
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		try{
				ArrayList<PollItemJDO> pollItemJDOList = new ArrayList<PollItemJDO>();
				for(int index = 0 ; index <= pollItemsList.size() ; index++ ){
						
					PollItemJDO objPollItemJDO = new PollItemJDO();
					ArrayList<String> pollOptLikedList = new ArrayList<String>();
					HashMap<Object,Object> pollOptContent = new HashMap<Object,Object>();
					HashMap<String,Object> pollOptinfo = pollItemsList.get(index);

					objPollItemJDO.setPollOptionID(CommonUtil.getUniqueId());
					objPollItemJDO.setPollID(pollID);
					objPollItemJDO.setOptionLikedList(pollOptLikedList);
					
					pollOptContent.put("img", pollOptinfo.get("img"));
					pollOptContent.put("optionName", pollOptinfo.get("optionName"));
					
					objPollItemJDO.setPollOptionContent(pollOptContent);
					
					saveJDOS(objPollItemJDO);
					pollItemJDOList.add(objPollItemJDO);	
				}
				
				responseMap.put("success", true);
				responseMap.put("pollItemJDOList",pollItemJDOList);
			
		}catch(Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return responseMap;
	}
	
	public static boolean saveJDOS(Object requestObjJDO) {
		try {
			PersistenceManager pm = PersistenceManagerUtil.getPersistenceManager();
			pm.makePersistent(requestObjJDO);
			pm.close();
			return true;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
}
