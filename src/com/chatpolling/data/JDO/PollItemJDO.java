package com.chatpolling.data.JDO;

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
	String pollOptID = "";
	
	String pollID = "";
	
	ArrayList<String> pollOptLikedList = new ArrayList<String>();
	
	boolean pollOptIsDelete = false;
	
	HashMap<Object,Object> pollOptContent = new HashMap<Object,Object>();

	public String getPollOptID() {
		return pollOptID;
	}

	public void setPollOptID(String pollOptID) {
		this.pollOptID = pollOptID;
	}

	public String getPollID() {
		return pollID;
	}

	public void setPollID(String pollID) {
		this.pollID = pollID;
	}

	public ArrayList<String> getPollOptLikedList() {
		return pollOptLikedList;
	}

	public void setPollOptLikedList(ArrayList<String> pollOptLikedList) {
		this.pollOptLikedList = pollOptLikedList;
	}

	public boolean isPollOptIsDelete() {
		return pollOptIsDelete;
	}

	public void setPollOptIsDelete(boolean pollOptIsDelete) {
		this.pollOptIsDelete = pollOptIsDelete;
	}

	public HashMap<Object, Object> getPollOptContent() {
		return pollOptContent;
	}

	public void setPollOptContent(HashMap<Object, Object> pollOptContent) {
		this.pollOptContent = pollOptContent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

	
}	
