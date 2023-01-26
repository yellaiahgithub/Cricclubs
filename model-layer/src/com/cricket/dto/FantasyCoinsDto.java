package com.cricket.dto;

public class FantasyCoinsDto {

	private long    id;
	private String 	currency;
	private float 	amount;
	private float	discount;
	private int 	noOfCoins;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public int getNoOfCoins() {
		return noOfCoins;
	}
	public void setNoOfCoins(int noOfCoins) {
		this.noOfCoins = noOfCoins;
	}
	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
	}
	

}
