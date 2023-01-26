package com.cricket.dto;

public class KycDocumentsDto {

	private int    id;
	private String 	documentType;
	private String 	documentName;
	private String 	countryCode;
	private String 	documentTypeInfo;
	
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
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getDocumentTypeInfo() {
		return documentTypeInfo;
	}
	public void setDocumentTypeInfo(String documentTypeInfo) {
		this.documentTypeInfo = documentTypeInfo;
	}
		
	
}
