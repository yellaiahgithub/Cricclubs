package com.cricket.dto;

import java.util.Date;

public class PlayerCategoryDto {
	
	private int 	id;		
	private int 	sportId;
	private String 	category;
	private int 	maxLimit;
	private int 	minLimit;
	private long 	contestId;
	private Date	createdDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getMaxLimit() {
		return maxLimit;
	}
	public void setMaxLimit(int maxLimit) {
		this.maxLimit = maxLimit;
	}
	public long getContestId() {
		return contestId;
	}
	public void setContestId(long contestId) {
		this.contestId = contestId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getMinLimit() {
		return minLimit;
	}
	public void setMinLimit(int minLimit) {
		this.minLimit = minLimit;
	}
	public int getSportId() {
		return sportId;
	}
	public void setSportId(int sportId) {
		this.sportId = sportId;
	}
	
	
	
}
