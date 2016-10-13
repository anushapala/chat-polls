package com.chatpolling.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/poll")
public class PollingController {
	

	private static Logger logger =  Logger.getLogger(PollingController.class.getPackage().getName());
	
	
	public @ResponseBody String createPoll(@RequestBody String requestJSON) throws JsonGenerationException, JsonMappingException, IOException{
		
		String responseJSON = "";
		HashMap<String,Object> requestMap = new HashMap<String,Object>();
		ObjectMapper mapper = new ObjectMapper();
		try{
				
			
			
		}catch(Exception e){
			e.printStackTrace();
			requestMap.put("success", false);
			requestMap.put("message", "Error on creating poll");
			responseJSON = mapper.writeValueAsString(requestMap);
		}
		return responseJSON;
	}
}
