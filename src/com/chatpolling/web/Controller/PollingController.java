package com.chatpolling.main;

import java.io.IOException;
import java.util.ArrayList;
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


@Controller
@RequestMapping(value="/poll")
public class PollingController {
	

	private static Logger logger =  Logger.getLogger(PollingController.class.getPackage().getName());
	
	@RequestMapping(value="/createPoll",method = RequestMethod.POST)
	public @ResponseBody String createPoll(@RequestBody String requestJSON) throws JsonGenerationException, JsonMappingException, IOException{
		
		String responseJSON = "";
		HashMap<String,Object> requestMap = new HashMap<String,Object>();
		HashMap<String,Object> responseMap = new HashMap<String,Object>();

		ObjectMapper mapper = new ObjectMapper();
		TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
		try{
			 requestMap = mapper.readValue(requestJSON, typeRef);
			 ArrayList<HashMap<String,Object>> pollItemsList = (ArrayList<HashMap<String,Object>>) requestMap.get("pollItemsList");
			 HashMap<String,Object> pollReqMap = (HashMap<String, Object>) requestMap.get("pollReqMap");
			 
			 responseMap = PollingHelper.createPollHelper(pollReqMap,pollItemsList);
			 responseJSON = mapper.writeValueAsString(responseMap);
			
		}catch(Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
			requestMap.put("success", false);
			requestMap.put("message", "Error on creating poll");
			responseJSON = mapper.writeValueAsString(requestMap);
		}
		return responseJSON;
	}
}
