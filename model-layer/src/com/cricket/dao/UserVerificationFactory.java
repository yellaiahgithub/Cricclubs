package com.cricket.dao;

import com.cricket.dto.UsersVerificationDto;

public class UserVerificationFactory {
	
	private static UserVerificationDAO userVerificationDAO = null;

	private static UserVerificationDAO getDaoInstance() {
		if (userVerificationDAO == null) {
			userVerificationDAO = new UserVerificationDAO();
		}
		return userVerificationDAO;
	}

	public static UsersVerificationDto getUserVerificationInfo(long userId, String fieldType) throws Exception {
		return getDaoInstance().getUserVerificationInfo(userId, fieldType);
	}	

	public static long insertUserVerificationInfo(UsersVerificationDto verificationDto) throws Exception {
		return getDaoInstance().insertUserVerificationInfo(verificationDto);
	}
	
	public static long insertEmailVerification(UsersVerificationDto verificationDto) throws Exception {
		return getDaoInstance().insertUserVerificationInfo(verificationDto);
	}
	
	public static long insertPhoneVerification(UsersVerificationDto verificationDto) throws Exception {
		return getDaoInstance().insertUserVerificationInfo(verificationDto);
	}

	public static void updateUserVerificationStatus(UsersVerificationDto verifyDto) throws Exception {
		getDaoInstance().updateUserVerification(verifyDto);
		
	}	
	
	
}
