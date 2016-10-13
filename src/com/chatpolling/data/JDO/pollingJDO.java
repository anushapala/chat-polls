package com.chatpolling.data.JDO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class pollingJDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2965690203852718173L;
	
	@PrimaryKey
	String pollingID = "";
	
	String pollItemName = "";
	
	ArrayList<String> pollLikedList = new ArrayList<String>();
	
	Date pollCreated = new Date();
	
	Date pollEnded = new Date();
	
	boolean pollIsDelete = false;
	
	String streamID = "";

	public String getPollingID() {
		return pollingID;
	}

	public void setPollingID(String pollingID) {
		this.pollingID = pollingID;
	}

	public String getPollItemName() {
		return pollItemName;
	}

	public void setPollItemName(String pollItemName) {
		this.pollItemName = pollItemName;
	}

	public ArrayList<String> getPollLikedList() {
		return pollLikedList;
	}

	public void setPollLikedList(ArrayList<String> pollLikedList) {
		this.pollLikedList = pollLikedList;
	}

	public Date getPollCreated() {
		return pollCreated;
	}

	public void setPollCreated(Date pollCreated) {
		this.pollCreated = pollCreated;
	}

	public Date getPollEnded() {
		return pollEnded;
	}

	public void setPollEnded(Date pollEnded) {
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
	
}
