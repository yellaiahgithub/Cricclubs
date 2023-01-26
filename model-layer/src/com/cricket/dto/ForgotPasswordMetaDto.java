package com.cricket.dto;

import java.util.Date;

public class ForgotPasswordMetaDto {
	
	private int id;
	private long userId;
	private String emailToken;
	private Date createdAt;
	private String mobileOtp;
	private Date expiryAt;
	private int status;
	
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
	public String getEmailToken() {
		return emailToken;
	}
	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getMobileOtp() {
		return mobileOtp;
	}
	public void setMobileOtp(String mobileOtp) {
		this.mobileOtp = mobileOtp;
	}
	public Date getExpiryAt() {
		return expiryAt;
	}
	public void setExpiryAt(Date expiryAt) {
		this.expiryAt = expiryAt;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
