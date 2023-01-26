package com.cricket.dto;

public class PollOptionDto {
	
	private int id;
	private int pollId;
	private String pollOption;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPollId() {
		return pollId;
	}
	public void setPollId(int pollId) {
		this.pollId = pollId;
	}
	public String getPollOption() {
		return pollOption;
	}
	public void setPollOption(String pollOption) {
		this.pollOption = pollOption;
	}
	
}
