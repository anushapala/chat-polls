package com.chatpolling.util;

import java.util.UUID;

public class CommonUtil {
	
	/**
	 * To get uniqueId 
	 * @return uniqueId
	 */
	public static String getUniqueId() {

		return UUID.randomUUID().toString();

	}
}
