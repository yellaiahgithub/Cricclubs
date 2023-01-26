package com.cricket.dto;

public class FantasyUserKycDocDto {

	private int     id;
	private long	userId;
	private	String 	name;
	private	String 	dob;
	private String 	address;
	private String 	city;
	private String 	state;
	private String 	postalCode;
	private String 	userCountryCode;
	private String	docPath;
	private int 	status;
	private String 	reason;
	private String	createdDate;
	private String 	documentType;
	private String 	documentName;
	private String 	documentTypeInfo;
	private String 	kycCountryCode;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getDocumentTypeInfo() {
		return documentTypeInfo;
	}
	public void setDocumentTypeInfo(String documentTypeInfo) {
		this.documentTypeInfo = documentTypeInfo;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDocPath() {
		return docPath;
	}
	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getUserCountryCode() {
		return userCountryCode;
	}
	public void setUserCountryCode(String userCountryCode) {
		this.userCountryCode = userCountryCode;
	}
	public String getKycCountryCode() {
		return kycCountryCode;
	}
	public void setKycCountryCode(String kycCountryCode) {
		this.kycCountryCode = kycCountryCode;
	}
		
	
}
