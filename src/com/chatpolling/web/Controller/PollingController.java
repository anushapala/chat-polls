package com.chatpolling.web.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chatpolling.util.CommonUtil;
import com.chatpolling.web.Helper.PollingHelper;


@Controller
@RequestMapping(value="/poll")
public class PollingController {
	

	private static Logger logger =  Logger.getLogger(PollingController.class.getPackage().getName());
	
	@RequestMapping(value="/createPoll",method = RequestMethod.POST)
	public @ResponseBody String createPoll(@RequestBody String requestJSON) throws JsonGenerationException, JsonMappingException, IOException{

		logger.info("IN PollingController -> createPoll() \n requestJSON :: " + requestJSON);
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		String responseJSON = "";
		ObjectMapper objectMapper = new ObjectMapper();
		
		try{
			HashMap<String,Object> pollDetailsMap = new HashMap<String,Object>();
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>(){};
			pollDetailsMap = objectMapper.readValue(requestJSON, typeRef);
		
			responseMap = PollingHelper.createPollHelper(pollDetailsMap);
			
		}catch(Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
			responseMap.put("success", false);
			responseMap.put("message", "Error on creating poll!");
		}
		responseJSON = objectMapper.writeValueAsString(responseMap);
		logger.info("\n OUT PollingController -> createPoll() \nresponseJSON to be returned is :: " + responseJSON);
		return responseJSON;
	}
	
	@RequestMapping(value="/fetchPoll",method = RequestMethod.POST)
	public @ResponseBody String fetchPoll(@RequestBody String requestJSON) throws JsonGenerationException, JsonMappingException, IOException{

		logger.info("IN PollingController -> fetchPoll() \n requestJSON :: " + requestJSON);
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		String responseJSON = "";
		ObjectMapper objectMapper = new ObjectMapper();
		
		try{
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>(){};
			HashMap<String,Object> requestMap = objectMapper.readValue(requestJSON, typeRef);
			String streamID = (String) requestMap.get("streamID");
		
			if(!CommonUtil.isEmptyString(streamID)){
				responseMap = PollingHelper.fetchPollHelper(streamID);
			}else{
				responseMap.put("success", false);
				responseMap.put("message", "Required details are empty!");
			}
			
		}catch(Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
			responseMap.put("success", false);
			responseMap.put("message", "Error in fetching polls!");
		}
		responseJSON = objectMapper.writeValueAsString(responseMap);
		logger.info("\n OUT PollingController -> fetchPoll() \nresponseJSON to be returned is :: " + responseJSON);
		return responseJSON;
	}
	
	@RequestMapping(value="/updatePollOption",method = RequestMethod.POST)
	public @ResponseBody String updatePollOption(@RequestBody String requestJSON) throws JsonGenerationException, JsonMappingException, IOException{

		logger.info("IN PollingController -> updatePollOption() \n requestJSON :: " + requestJSON);
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		String responseJSON = "";
		ObjectMapper objectMapper = new ObjectMapper();
		
		try{
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>(){};
			HashMap<String,Object> requestMap = objectMapper.readValue(requestJSON, typeRef);
			String pollID = (String) requestMap.get("pollID");
			String pollOptionID = (String) requestMap.get("pollOptionID");
			String contactID = (String) requestMap.get("contactID");
		
			if( CommonUtil.isEmptyString(pollID) || CommonUtil.isEmptyString(pollOptionID) || CommonUtil.isEmptyString(contactID) ){
				responseMap.put("success", false);
				responseMap.put("message", "Required details are empty!");
			}else{
				responseMap = PollingHelper.updatePollOptionHelper(pollID, pollOptionID, contactID);
			}
			
		}catch(Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
			responseMap.put("success", false);
			responseMap.put("message", "Error in fetching polls!");
		}
		responseJSON = objectMapper.writeValueAsString(responseMap);
		logger.info("\n OUT PollingController -> updatePollOption() \nresponseJSON to be returned is :: " + responseJSON);
		return responseJSON;
	}
}
