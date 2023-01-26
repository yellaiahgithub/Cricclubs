package com.cricket.dao;

import java.util.List;

import com.cricket.dto.StreamingPriceDto;
import com.cricket.utility.CommonUtility;

public class StreamingPriceFactory {
	
	private static StreamingPriceDAO streamingPriceDAO = null;

	private static StreamingPriceDAO getDaoInstance() {
		if (streamingPriceDAO == null) {
			streamingPriceDAO = new StreamingPriceDAO();
		}
		return streamingPriceDAO;
	}

	public static List<StreamingPriceDto> getStreamingPriceInfo(String currency) throws Exception {
		return getDaoInstance().getStreamingPriceInfo(currency,0);
	}
	
	public static StreamingPriceDto getNoOfMatchesByAmount(String currency, float amount) throws Exception {
		
		List<StreamingPriceDto> dtoList = getDaoInstance().getNoOfMatchesByAmount(currency,amount);
		if(!CommonUtility.isListNullEmpty(dtoList)) {
			return dtoList.get(0);
		}
		return null;
	}
}
