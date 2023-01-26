package com.cricket.dto;

import java.util.Date;

public class TransactionDto {

	private long    id;
	private long 	userId;
	private int 	contestId;
	private float 	amount;
	private float 	bonus;
	private String 	txnType;
	private String 	txnFrom;
	private long 	txnRefId;
	private Date 	txnDate;
	private String 	txnDay;
	private String 	txnMon;
	private String 	txnTime;
	private float 	contestBalance;
	private float 	withdrawBalance;
	private float 	bonusBalance;
	private String 	remarks;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getTxnFrom() {
		return txnFrom;
	}
	public void setTxnFrom(String txnFrom) {
		this.txnFrom = txnFrom;
	}
	public long getTxnRefId() {
		return txnRefId;
	}
	public void setTxnRefId(long txnRefId) {
		this.txnRefId = txnRefId;
	}
	public Date getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}
	public String getTxnDay() {
		return txnDay;
	}
	public void setTxnDay(String txnDay) {
		this.txnDay = txnDay;
	}
	public String getTxnMon() {
		return txnMon;
	}
	public void setTxnMon(String txnMon) {
		this.txnMon = txnMon;
	}
	public String getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}
	public float getContestBalance() {
		return contestBalance;
	}
	public void setContestBalance(float contestBalance) {
		this.contestBalance = contestBalance;
	}
	public float getWithdrawBalance() {
		return withdrawBalance;
	}
	public void setWithdrawBalance(float withdrawBalance) {
		this.withdrawBalance = withdrawBalance;
	}
	public float getBonusBalance() {
		return bonusBalance;
	}
	public void setBonusBalance(float bonusBalance) {
		this.bonusBalance = bonusBalance;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public float getBonus() {
		return bonus;
	}
	public void setBonus(float bonus) {
		this.bonus = bonus;
	}
	public int getContestId() {
		return contestId;
	}
	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
}
