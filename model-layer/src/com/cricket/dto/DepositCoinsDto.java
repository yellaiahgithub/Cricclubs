package com.cricket.dto;

import java.util.Date;

public class DepositCoinsDto {

	private long    id;
	private long 	userId;
	private float 	purchaseAmount;
	private String 	currencyCode;
	private String 	paymentVia;
	private String 	txnToken;
	private String 	paymentGateway;
	private int 	status;
	private int 	coins;
	private Date 	purchaseTime;	
	
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
	public float getPurchaseAmount() {
		return purchaseAmount;
	}
	public void setPurchaseAmount(float purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getPaymentVia() {
		return paymentVia;
	}
	public void setPaymentVia(String paymentVia) {
		this.paymentVia = paymentVia;
	}
	public String getTxnToken() {
		return txnToken;
	}
	public void setTxnToken(String txnToken) {
		this.txnToken = txnToken;
	}
	public String getPaymentGateway() {
		return paymentGateway;
	}
	public void setPaymentGateway(String paymentGateway) {
		this.paymentGateway = paymentGateway;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getPurchaseTime() {
		return purchaseTime;
	}
	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
}
