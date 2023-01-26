package com.cricket.exception;

public class CCException extends Exception {

	/**
	* 
	*/
	private String errorCode;
	private String errorDesc;
	
	private static final long serialVersionUID = 1L;

	public CCException() {
		super();
	}

	public CCException(String s, String errorCode) {
		super(s);
		this.errorCode= errorCode;
		if(this.errorCode == null) {
			this.errorCode = CCErrorConstant.UNKNOWN_ERROR_C;			
		}
		setErrorDesc(errorCode);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorCode) {
		
		this.errorDesc = CCErrorConstant.errorDescMap.get(errorCode);
		
	}

}
