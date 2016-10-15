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
				String pollQuestion = (String) pollQuestionDetails.get("pollQuestion");
				String pollDescription = (String) pollQuestionDetails.get("pollDescription");
				pollDescription = CommonUtil.isEmptyString(pollDescription) ? "" : pollDescription;
				if( CommonUtil.isEmptyString(streamID) || CommonUtil.isEmptyString(createdBy) || CommonUtil.isEmptyString(pollQuestion) ){
					responseMap.put("success", false);
					responseMap.put("message", "Required details are empty!");
				}else {
					PollJDO objPollJDO = new PollJDO();
					objPollJDO.setPollID(CommonUtil.getUniqueId());
					objPollJDO.setStreamID(streamID);
					objPollJDO.setCreatedBy(createdBy);
					objPollJDO.setPollQuestion(pollQuestion);
					objPollJDO.setPollDescription(pollDescription);
					objPollJDO.setCreatedTime(new Date().getTime());
					objPollJDO.setEndedTime(new Date().getTime());
					
					boolean isSaved = PollDAO.savePollJDO(objPollJDO);
					
					if(isSaved){
						ArrayList<String> optionLinkedList = new ArrayList<String>();

						for(HashMap<String, Object> singleOptionDetails : pollOptionsDetails){
							String pollOptionText = (String) singleOptionDetails.get("pollOptionContent");
							String pollOptionImageURL = (String) singleOptionDetails.get("pollOptionImageURL");
							pollOptionImageURL = "https://pbs.twimg.com/profile_images/515564873158098944/y9MfP_Xo.png";
							
							if(!CommonUtil.isEmptyString(pollOptionText)){
								PollItemJDO objPollItemJDO = new PollItemJDO();
								objPollItemJDO.setPollOptionID(CommonUtil.getUniqueId());
								objPollItemJDO.setPollID(objPollJDO.getPollID());
								objPollItemJDO.setPollOptionText(pollOptionText);
								objPollItemJDO.setPollOptionImageURL(pollOptionImageURL);
								objPollItemJDO.setOptionLikedList(optionLinkedList);
								
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
	

}
