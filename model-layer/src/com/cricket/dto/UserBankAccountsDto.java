package com.cricket.dto;

import java.util.Date;

public class UserBankAccountsDto {

	private long    userId;
	private String 	accountNo;
	private String 	accountHolderName;
	private String 	bankName;
	private String 	branchName;
	private String 	ifscSwiftCode;
	private String 	bankIdentifierLabel;
	private int 	bankIdentifierLength;
	private String 	cashFreeBeneficiaryId; 
	private int 	status;	
	private Date    createdAt;
	private Date    updatedAt;	
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
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
	public String getAccountHolderName() {
		return accountHolderName;
	}
	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}
	public int getBankIdentifierLength() {
		return bankIdentifierLength;
	}
	public void setBankIdentifierLength(int bankIdentifierLength) {
		this.bankIdentifierLength = bankIdentifierLength;
	}
	public String getCashFreeBeneficiaryId() {
		return cashFreeBeneficiaryId;
	}
	public void setCashFreeBeneficiaryId(String cashFreeBeneficiaryId) {
		this.cashFreeBeneficiaryId = cashFreeBeneficiaryId;
	}
	
}
