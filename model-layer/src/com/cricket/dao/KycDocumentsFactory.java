package com.cricket.dao;

import java.util.List;

import com.cricket.dto.KycDocumentsDto;
import com.cricket.utility.CommonUtility;

public class KycDocumentsFactory {
	
	private static KycDocumentsDAO kycDocumentsDAO = null;
	
	private static KycDocumentsDAO getDaoInstance(){
		if(kycDocumentsDAO == null){
			kycDocumentsDAO = new KycDocumentsDAO();
		}
		return kycDocumentsDAO;
	}
	
	public static List<KycDocumentsDto> getKycDocumentsByCountry(String countryCode) throws Exception {
		return getDaoInstance().getKycDocuments(countryCode,0);
	}
	
	public static int getNoOfKYCDocsRequiredForCountry(String countryCode) throws Exception {
		return getDaoInstance().getNoOfKYCDocsRequiredForCountry(countryCode);
	}
	
	public static List<String> getKycDocTypes() throws Exception {
		return getDaoInstance().getKycDocTypes();
	}
	
	public static KycDocumentsDto getKycDocumentsById(int kycId) throws Exception {
		List<KycDocumentsDto> kycDocList = getDaoInstance().getKycDocuments("",kycId);
		if(!CommonUtility.isListNullEmpty(kycDocList)) {
			return kycDocList.get(0);
		}
		return null;		
	}
	
}
