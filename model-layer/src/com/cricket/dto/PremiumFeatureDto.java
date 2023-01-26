package com.cricket.dto;

import java.util.Date;

public class PremiumFeatureDto {

	public static int SPONSOR_AD_SPACE = 1;
	public static int PLAYER_REGISTRATION_FEE = 2;
	public static int TEAM_REGISTRATION_FEE = 2;
	
	int featureId;
	String featureName;
	String featureDisplayTitle;	
	String featureDesc;
	String featureCategory;
	boolean standardFeature;
	Date effectiveStartDate;
	String status;
	Date creationDate;
	Date updateDate;
	
	public int getFeatureId() {
		return featureId;
	}
	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public String getFeatureDisplayTitle() {
		return featureDisplayTitle;
	}
	public void setFeatureDisplayTitle(String featureDisplayTitle) {
		this.featureDisplayTitle = featureDisplayTitle;
	}
	public String getFeatureDesc() {
		return featureDesc;
	}
	public void setFeatureDesc(String featureDesc) {
		this.featureDesc = featureDesc;
	}
	public String getFeatureCategory() {
		return featureCategory;
	}
	public void setFeatureCategory(String featureCategory) {
		this.featureCategory = featureCategory;
	}
	public boolean isStandardFeature() {
		return standardFeature;
	}
	public void setStandardFeature(boolean standardFeature) {
		this.standardFeature = standardFeature;
	}
	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}
	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
