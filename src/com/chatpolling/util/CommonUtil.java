package com.chatpolling.util;

import java.util.UUID;

public class CommonUtil {
	
	public static final String Success = "success";
	public static final String error = "error";
	public static final String message = "message";

	/**
	 * To get uniqueId 
	 * @return uniqueId
	 */
	public static String getUniqueId() {

		return UUID.randomUUID().toString();

	}
	
	public static boolean isEmptyString(String string){
		if(string != null && string != ""){
			return false;
		}else{
			return true;
		}
	}
}
