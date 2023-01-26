package com.cricket.dto;

import java.util.Date;

public class UserDocumentsListDto {

	private long    userId;
	private long 	accountNo;
	private String 	bankName;
	private String 	branchName;
	private String 	ifscSwiftCode;
	private String 	bankIdentifierLabel;
	private int 	status;	
	private Date    createdAt;
	private Date    updatedAt;	
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	public String getIfscSwiftCode() {
		return ifscSwiftCode;
	}
	public void setIfscSwiftCode(String ifscSwiftCode) {
		this.ifscSwiftCode = ifscSwiftCode;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBankIdentifierLabel() {
		return bankIdentifierLabel;
	}
	public void setBankIdentifierLabel(String bankIdentifierLabel) {
		this.bankIdentifierLabel = bankIdentifierLabel;
	}
	
	
}
