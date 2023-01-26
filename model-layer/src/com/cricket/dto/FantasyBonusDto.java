package com.cricket.dto;

import java.util.Date;

public class FantasyBonusDto {

	private long    id;
	private String 	title;
	private String 	code;
	private String 	description;
	private int 	limitPerUser;
	private float 	bonusPercent;
	private float 	maxBonus;
	private int 	expiryDays;
	private int 	status;
	private int		showInApp;
	private Date 	startDate;
	private Date 	expiryDate;	
	private String	bonusFor;
	private	String	bonusForType;
	private	float	bonusForCondition;
	private Date    createdAt;
	private int		createdBy;
	private Date    updatedAt;
	private int		updatedBy;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getBonusPercent() {
		return bonusPercent;
	}
	public void setBonusPercent(float bonusPercent) {
		this.bonusPercent = bonusPercent;
	}
	public float getMaxBonus() {
		return maxBonus;
	}
	public void setMaxBonus(float maxBonus) {
		this.maxBonus = maxBonus;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
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
	public int getLimitPerUser() {
		return limitPerUser;
	}
	public void setLimitPerUser(int limitPerUser) {
		this.limitPerUser = limitPerUser;
	}
	public int getExpiryDays() {
		return expiryDays;
	}
	public void setExpiryDays(int expiryDays) {
		this.expiryDays = expiryDays;
	}
	public int getShowInApp() {
		return showInApp;
	}
	public void setShowInApp(int showInApp) {
		this.showInApp = showInApp;
	}
	public String getBonusFor() {
		return bonusFor;
	}
	public void setBonusFor(String bonusFor) {
		this.bonusFor = bonusFor;
	}
	public String getBonusForType() {
		return bonusForType;
	}
	public void setBonusForType(String bonusForType) {
		this.bonusForType = bonusForType;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public float getBonusForCondition() {
		return bonusForCondition;
	}
	public void setBonusForCondition(float bonusForCondition) {
		this.bonusForCondition = bonusForCondition;
	}
	
	
}
