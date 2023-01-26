package com.cricket.dto;

import java.util.Date;

public class UsersVerificationDto {	

	private int    id;
	private long    userId;
	private String fieldType = "";
	private String tokenOtp = "";
	private int status;	
	private Date   createdAt;
	private Date   updatedAt;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getTokenOtp() {
		return tokenOtp;
	}
	public void setTokenOtp(String tokenOtp) {
		this.tokenOtp = tokenOtp;
	}	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
