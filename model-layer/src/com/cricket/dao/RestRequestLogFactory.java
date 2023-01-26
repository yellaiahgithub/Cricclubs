package com.cricket.dao;

import java.util.List;

import com.cricket.dto.RestRequestLogDto;

public class RestRequestLogFactory {
	
	private static RestRequestLogDAO requestLogDao = null;
	
	private static RestRequestLogDAO getDaoInstance(){
		if(requestLogDao == null){
			requestLogDao = new RestRequestLogDAO();
		}
		return requestLogDao;
	}
	
	public static void insertRequestLog(RestRequestLogDto log) throws Exception{
		getDaoInstance().insertRequestLog(log);
	}

	public static List<RestRequestLogDto> getRequestData(String startDate, String endDate, String clubId, String ip) throws Exception {
		return getDaoInstance().getRequestData(startDate, endDate, clubId, ip);
	}
}
