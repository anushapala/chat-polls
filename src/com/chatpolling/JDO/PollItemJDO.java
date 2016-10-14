package com.chatpolling.JDO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class PollItemJDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8706921670889027472L;
	
	@PrimaryKey
	String pollOptionID = "";
	
	String pollID = "";
	
	ArrayList<String> optionLikedList = new ArrayList<String>();
	
	HashMap<Object,Object> pollOptionContent = new HashMap<Object,Object>();

	public String getPollOptionID() {
		return pollOptionID;
	}

	public void setPollOptionID(String pollOptionID) {
		this.pollOptionID = pollOptionID;
	}

	public String getPollID() {
		return pollID;
	}

	public void setPollID(String pollID) {
		this.pollID = pollID;
	}

	public ArrayList<String> getOptionLikedList() {
		return optionLikedList;
	}

	public void setOptionLikedList(ArrayList<String> optionLikedList) {
		this.optionLikedList = optionLikedList;
	}

	public HashMap<Object, Object> getPollOptionContent() {
		return pollOptionContent;
	}

	public void setPollOptionContent(HashMap<Object, Object> pollOptionContent) {
		this.pollOptionContent = pollOptionContent;
	}
	
}	
