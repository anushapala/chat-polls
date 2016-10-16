package com.chatpolling.JDO;

import java.io.Serializable;
import java.util.ArrayList;

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

	String pollOptionText = "";

	String pollOptionImageURL = "";

	ArrayList<String> optionLikedList = new ArrayList<String>();

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

	public String getPollOptionText() {
		return pollOptionText;
	}

	public void setPollOptionText(String pollOptionText) {
		this.pollOptionText = pollOptionText;
	}

	public String getPollOptionImageURL() {
		return pollOptionImageURL;
	}

	public void setPollOptionImageURL(String pollOptionImageURL) {
		this.pollOptionImageURL = pollOptionImageURL;
	}

	public ArrayList<String> getOptionLikedList() {
		return optionLikedList;
	}

	public void setOptionLikedList(ArrayList<String> optionLikedList) {
		this.optionLikedList = optionLikedList;
	}

}
