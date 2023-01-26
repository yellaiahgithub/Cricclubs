package com.cricket.dto;

import java.util.Date;

public class EmailMobileUpdateLogDto {
	
	private int id;
	private long userId;
	private String fieldType;
	private String newEmailMobile;
	private String otp;
	private int status;
	private Date expiryTime;
	
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
	public String getNewEmailMobile() {
		return newEmailMobile;
	}
	public void setNewEmailMobile(String newEmailMobile) {
		this.newEmailMobile = newEmailMobile;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}	
	
}
