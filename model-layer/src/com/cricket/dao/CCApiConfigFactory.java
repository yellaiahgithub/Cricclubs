package com.cricket.dao;

import java.util.List;

import com.cricket.dto.CCApiConfigDto;
import com.cricket.utility.CommonUtility;

public class CCApiConfigFactory {
	
	private static CCApiConfigDAO ccApiConfigDAO = null;

	private static CCApiConfigDAO getDaoInstance() {
		if (ccApiConfigDAO == null) {
			ccApiConfigDAO = new CCApiConfigDAO();
		}
		return ccApiConfigDAO;
	}

	public static CCApiConfigDto getCCAppConfigDetailsByName(String name) throws Exception {
		
		List<CCApiConfigDto> dtoList = getDaoInstance().getCCAppConfigDetails(name);
		if(!CommonUtility.isListNullEmpty(dtoList)) {
			return dtoList.get(0);
		}		
		return null;
	}	
	
}
