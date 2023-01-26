package com.cricket.dto;

public class GalleryRecordDto {
	
	private int id;
	private int clubId;
 	private int galleryId;
 	private String associationType; 	
 	private int associationId; 	
 	private int attr1; 	
 	private int attr2;
 	private int attr3;
 	
 	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getAttr1() {
		return attr1;
	}
	public void setAttr1(int attr1) {
		this.attr1 = attr1;
	}
	public int getAttr2() {
		return attr2;
	}
	public void setAttr2(int attr2) {
		this.attr2 = attr2;
	}
	public int getAttr3() {
		return attr3;
	}
	public void setAttr3(int attr3) {
		this.attr3 = attr3;
	}	
}
