package com.cricket.dto;

public class GalleryDto {	
	
 	private int id;
 	
 	private String creationTime;

	private String galleryType;
 	
 	private String path;
 	
 	private int uploadedBy;
 	
 	private String title;
 	
 	private String description;
 	
 	private int clubId;
 	
 	private int galleryId;
 	
 	private String associationType;
 	
 	private int associationId; 
 	
 	private int ballNum; 
 	
 	private int overNum;
 	
 	private int teamId;
 
 	public int getId() {
 		return id;
 	}
 
 	public void setId(int id) {
 		this.id = id;
 	}
 
 	public String getCreationTime() {
 		return creationTime;
 	}
 
 	public void setCreationTime(String creationTime) {
 		this.creationTime = creationTime;
 	}
 	
 	public String getGalleryType() {
		return galleryType;
	}

	public void setGalleryType(String galleryType) {
		this.galleryType = galleryType;
	}
 	
 	public String getPath() {
 		return path;
 	}
 
 	public void setPath(String path) {
 		this.path = path;
 	}
 
 	public int getUploadedBy() {
 		return uploadedBy;
 	}
 
 	public void setUploadedBy(int uploadedBy) {
 		this.uploadedBy = uploadedBy;
	 	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public int getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(int galleryId) {
		this.galleryId = galleryId;
	}

	public String getAssociationType() {
		return associationType;
	}

	public void setAssociationType(String associationType) {
		this.associationType = associationType;
	}

	public int getAssociationId() {
		return associationId;
	}

	public void setAssociationId(int associationId) {
		this.associationId = associationId;
	}

	public int getBallNum() {
		return ballNum;
	}

	public void setBallNum(int ballNum) {
		this.ballNum = ballNum;
	}

	public int getOverNum() {
		return overNum;
	}

	public void setOverNum(int overNum) {
		this.overNum = overNum;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

}
