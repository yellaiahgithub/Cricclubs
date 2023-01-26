package com.cricket.dao;

import com.cricket.dto.EmailMobileLogDto;

public class EmailMobileLogFactory {
	
	private static EmailMobileLogDAO changeEmailMobileLogDAO = null;
	
	private static EmailMobileLogDAO getDaoInstance(){
		if(changeEmailMobileLogDAO == null){
			changeEmailMobileLogDAO = new EmailMobileLogDAO();
		}
		return changeEmailMobileLogDAO;
	}

	public static void addEmailMobileLog(EmailMobileLogDto logDto) throws Exception {
		getDaoInstance().addEmailMobileLog(logDto);
	}
	
	public static void updateEmailMobileLogOtpStatus(long logId) throws Exception {
		getDaoInstance().updateEmailMobileLogOtpStatus(logId);
	}
	
	public static void updateChangeMobileLogOtp(EmailMobileLogDto logDto) throws Exception {
		getDaoInstance().updateChangeMobileLogOtp(logDto);
	}
	
	
	
	public static int checkEmailMobileLogExistsForUser(EmailMobileLogDto logDto) throws Exception {
		return getDaoInstance().checkEmailMobileLogExistsForUser(logDto);
	}
	
	public static String getExitingOtpFromMobileEmailLog(long userId, String fieldType, String emailOrMobile) throws Exception {
		return getDaoInstance().getExitingOtpFromMobileEmailLog(userId, fieldType, emailOrMobile);
	}
	
	
}
