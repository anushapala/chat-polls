package com.chatpolling.JDO;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class PollJDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2965690203852718173L;
	
	@PrimaryKey
	String pollID = "";
	
	String streamID = "";
	
	String pollQuestion = "";
	
	String pollDescription = "";
	
	String createdBy = "";
	
	String createdUserName = "";
	
	String createdUserImg = "";
	
	Long createdTime = new Date().getTime();
	
	Long endedTime = new Date().getTime();

	public String getPollID() {
		return pollID;
	}

	public void setPollID(String pollID) {
		this.pollID = pollID;
	}

	public String getStreamID() {
		return streamID;
	}

	public void setStreamID(String streamID) {
		this.streamID = streamID;
	}

	public String getPollQuestion() {
		return pollQuestion;
	}

	public void setPollQuestion(String pollQuestion) {
		this.pollQuestion = pollQuestion;
	}

	public String getPollDescription() {
		return pollDescription;
	}

	public void setPollDescription(String pollDescription) {
		this.pollDescription = pollDescription;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public String getCreatedUserImg() {
		return createdUserImg;
	}

	public void setCreatedUserImg(String createdUserImg) {
		this.createdUserImg = createdUserImg;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	public Long getEndedTime() {
		return endedTime;
	}

	public void setEndedTime(Long endedTime) {
		this.endedTime = endedTime;
	}

}
