package com.chatpolling.data.JDO;

import java.io.Serializable;
import java.util.ArrayList;
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
	
	String createdBy = "";
	
	String pollQuestion = "";
		
	String pollDescription = "";
	
	Long pollCreated = new Date().getTime();
	
	Long pollEnded = new Date().getTime();
	
	boolean pollIsDelete = false;
	
	
	
	boolean selectMultipleOptions = false;
	

	public String getPollID() {
		return pollID;
	}

	public void setPollID(String pollID) {
		this.pollID = pollID;
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
	
	public Long getPollCreated() {
		return pollCreated;
	}

	public void setPollCreated(Long pollCreated) {
		this.pollCreated = pollCreated;
	}

	public Long getPollEnded() {
		return pollEnded;
	}

	public void setPollEnded(Long pollEnded) {
		this.pollEnded = pollEnded;
	}

	public boolean isPollIsDelete() {
		return pollIsDelete;
	}

	public void setPollIsDelete(boolean pollIsDelete) {
		this.pollIsDelete = pollIsDelete;
	}

	public String getStreamID() {
		return streamID;
	}

	public void setStreamID(String streamID) {
		this.streamID = streamID;
	}
	
	public boolean isSelectMultipleOptions() {
		return selectMultipleOptions;
	}

	public void setSelectMultipleOptions(boolean selectMultipleOptions) {
		this.selectMultipleOptions = selectMultipleOptions;
	}

}
