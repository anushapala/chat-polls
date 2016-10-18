package com.chatpolling.web.Helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.chatpolling.DAO.PollDAO;
import com.chatpolling.DAO.PollItemDAO;
import com.chatpolling.JDO.PollItemJDO;
import com.chatpolling.JDO.PollJDO;
import com.chatpolling.util.CommonUtil;

public class PollingHelper {
	private static Logger logger =  Logger.getLogger(PollingHelper.class.getPackage().getName());

	@SuppressWarnings("unchecked")
	public static HashMap<String,Object> createPollHelper(HashMap<String,Object> pollDetailsMap){
		logger.info("IN createPollHelper()");
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		ArrayList<PollItemJDO> pollOptionsList = new ArrayList<PollItemJDO>(); 
		
		try{
			
			HashMap<String,Object> pollQuestionDetails = (HashMap<String, Object>) pollDetailsMap.get("pollQuestionDetails");
			ArrayList<HashMap<String,Object>> pollOptionsDetails = (ArrayList<HashMap<String, Object>>) pollDetailsMap.get("pollOptionsDetails");
			
			if(pollQuestionDetails != null && pollOptionsDetails != null && pollOptionsDetails.size() > 0){
				
				String streamID = (String) pollQuestionDetails.get("streamID");
				String createdBy = (String)pollQuestionDetails.get("createdBy");
				String createdUserName = (String)pollQuestionDetails.get("createdUserName");
				String createdUserImg = (String)pollQuestionDetails.get("createdUserImg");
				String pollQuestion = (String) pollQuestionDetails.get("pollQuestion");
				String pollDescription = (String) pollQuestionDetails.get("pollDescription");
				pollDescription = CommonUtil.isEmptyString(pollDescription) ? "" : pollDescription;
				if( CommonUtil.isEmptyString(streamID) || CommonUtil.isEmptyString(createdBy) || CommonUtil.isEmptyString(createdUserName) || CommonUtil.isEmptyString(createdUserImg) || CommonUtil.isEmptyString(pollQuestion) ){
					responseMap.put("success", false);
					responseMap.put("message", "Required details are empty!");
				}else {
					PollJDO objPollJDO = new PollJDO();
					objPollJDO.setPollID(CommonUtil.getUniqueId());
					objPollJDO.setStreamID(streamID);
					objPollJDO.setCreatedBy(createdBy);
					objPollJDO.setCreatedUserName(createdUserName);
					objPollJDO.setCreatedUserImg(createdUserImg);
					objPollJDO.setPollQuestion(pollQuestion);
					objPollJDO.setPollDescription(pollDescription);
					objPollJDO.setCreatedTime(new Date().getTime());
					objPollJDO.setEndedTime(new Date().getTime());
					
					boolean isSaved = PollDAO.savePollJDO(objPollJDO);
					
					if(isSaved){
						for(HashMap<String, Object> singleOptionDetails : pollOptionsDetails){
							String pollOptionText = (String) singleOptionDetails.get("pollOptionContent");
							String pollOptionImageURL = (String) singleOptionDetails.get("pollOptionImageURL");
							pollOptionImageURL = "https://storage.googleapis.com/pollapplication-146416.appspot.com/Poll_logo.png";
							
							if(!CommonUtil.isEmptyString(pollOptionText)){
								PollItemJDO objPollItemJDO = new PollItemJDO();
								objPollItemJDO.setPollOptionID(CommonUtil.getUniqueId());
								objPollItemJDO.setPollID(objPollJDO.getPollID());
								objPollItemJDO.setPollOptionText(pollOptionText);
								objPollItemJDO.setPollOptionImageURL(pollOptionImageURL);
								objPollItemJDO.setOptionLikedList(new ArrayList<String>());
								
								boolean blnIsSaved = PollItemDAO.savePollItemJDO(objPollItemJDO);
								if(blnIsSaved){
									pollOptionsList.add(objPollItemJDO);
								}
							}
						}
						
						responseMap.put("success", true);
						responseMap.put("pollQuestionDetails", objPollJDO);
						if(pollOptionsList != null && pollOptionsList.size() > 0){
							responseMap.put("pollOptionsList", pollOptionsList);
						}else{
							responseMap.put("pollOptionsList", null);
						}
						responseMap.put("message", "Poll created!");
					}else{
						responseMap.put("success", false);
						responseMap.put("message", "Problem in creating the Poll!");
					}
				}
			}else{
				responseMap.put("success", false);
				responseMap.put("message", "Required details are empty!");
			}
			
		}catch(Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
			
			responseMap.put("success", false);
			responseMap.put("message", "Problem in creating the Poll!");
		}
		return responseMap;
	}
	
	
	public static HashMap<String,Object> fetchPollHelper(String streamID){
		logger.info("IN fetchPollHelper()");
		HashMap<String, Object> responseMap = new HashMap<String, Object>(); 
		try{
			ArrayList<HashMap<String,Object>> lstPollsDetails = new ArrayList<HashMap<String,Object>>();
			
			List<PollJDO> lstPollJDO = PollDAO.fetchPollsForThisStreamID(streamID);
			if(lstPollJDO != null && lstPollJDO.size() > 0){
				for( PollJDO singlePollJDO : lstPollJDO ){
					HashMap<String, Object> singlePollDetailsMap = new HashMap<String, Object>();
					singlePollDetailsMap.put("pollQuestionDetails", singlePollJDO);
	
					ArrayList<PollItemJDO> pollOptionsList = new ArrayList<PollItemJDO>();
					if(!CommonUtil.isEmptyString(singlePollJDO.getPollID())){
						pollOptionsList = (ArrayList<PollItemJDO>) PollItemDAO.fetchPollOptionsForThisPollID(singlePollJDO.getPollID());
					}else{
						pollOptionsList = null;
					}
					singlePollDetailsMap.put("pollOptionsList", pollOptionsList);
					
					lstPollsDetails.add(singlePollDetailsMap);
				}
				responseMap.put("success", true);
				responseMap.put("PollsDetailsList", lstPollsDetails);
			}else{
				responseMap.put("success", true);
				responseMap.put("message", "No polls for the given streamID!");
			}
			
		}catch(Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
			
			responseMap.put("success", false);
			responseMap.put("message", "Problem in fetching polls!");
		}
		return responseMap;
	}
	
	
	public static HashMap<String,Object> updatePollOptionHelper(String pollID, String pollOptionID, String contactID){
		logger.info("IN updatePollOptionHelper()");
		HashMap<String, Object> responseMap = new HashMap<String, Object>(); 
		try{
			
			PollJDO objPollJDO = PollDAO.fetchPollsForThisPollID(pollID);
			if(objPollJDO != null){
				ArrayList<PollItemJDO> pollOptionsList = (ArrayList<PollItemJDO>) PollItemDAO.fetchPollOptionsForThisPollID(pollID);
				if( pollOptionsList != null && pollOptionsList.size() > 0 ){
					boolean isDisLikeOperation = false;
					for(PollItemJDO singlePollItemJDO : pollOptionsList){
						if( pollOptionID.equalsIgnoreCase(singlePollItemJDO.getPollOptionID()) ){
							ArrayList<String> likedContactIDList = singlePollItemJDO.getOptionLikedList();
							if( likedContactIDList != null && likedContactIDList.size() > 0 && likedContactIDList.contains(contactID)){
								likedContactIDList.remove(contactID);
								singlePollItemJDO.setOptionLikedList(likedContactIDList);
								PollItemDAO.savePollItemJDO(singlePollItemJDO);
								isDisLikeOperation = true;
								break;
							}
						}
					}
					
					if(!isDisLikeOperation){
						for(PollItemJDO singlePollItemJDO : pollOptionsList){
							if( pollOptionID.equalsIgnoreCase(singlePollItemJDO.getPollOptionID()) ){
								ArrayList<String> likedContactIDList = singlePollItemJDO.getOptionLikedList();
								likedContactIDList.add(contactID);
								singlePollItemJDO.setOptionLikedList(likedContactIDList);
								PollItemDAO.savePollItemJDO(singlePollItemJDO);
							}else{
								ArrayList<String> likedContactIDList = singlePollItemJDO.getOptionLikedList();
								if( likedContactIDList != null && likedContactIDList.size() > 0 && likedContactIDList.contains(contactID)){
									likedContactIDList.remove(contactID);
									singlePollItemJDO.setOptionLikedList(likedContactIDList);
									PollItemDAO.savePollItemJDO(singlePollItemJDO);
								}
							}
						}
					}
					
					responseMap.put("success", true);
					responseMap.put("pollQuestionDetails", objPollJDO);
					responseMap.put("pollOptionsList", pollOptionsList);
					responseMap.put("message", "Poll options are updated!");
					
				}else{
					responseMap.put("success", false);
					responseMap.put("message", "Problem in updating the poll option!");
				}
			}else{
				responseMap.put("success", false);
				responseMap.put("message", "Problem in updating the poll option!");
			}
		}catch(Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
			
			responseMap.put("success", false);
			responseMap.put("message", "Problem in updating the poll option!");
		}
		return responseMap;
	}
}
