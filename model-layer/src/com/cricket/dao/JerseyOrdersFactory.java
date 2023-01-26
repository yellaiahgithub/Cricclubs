package com.cricket.dao;

import com.cricket.dto.JerseyOrdersDto;

public class JerseyOrdersFactory {
	
	private static JerseyOrdersDAO jerseyOrdersDAO = null;

	private static JerseyOrdersDAO getDaoInstance() {
		if (jerseyOrdersDAO == null) {
			jerseyOrdersDAO = new JerseyOrdersDAO();
		}
		return jerseyOrdersDAO;
	}

	public static JerseyOrdersDto getJerseyOrder(String txnId) throws Exception {
		return getDaoInstance().getJerseyOrder(txnId);
	}
	
	public static int insertJerseyOrder(JerseyOrdersDto dto) throws Exception {
		return getDaoInstance().insertJerseyOrder(dto);
	}
	
	public static void updateJerseyOrder(int orderId, int status) throws Exception {
		getDaoInstance().updateJerseyOrder(orderId, status);
	}
	
}
