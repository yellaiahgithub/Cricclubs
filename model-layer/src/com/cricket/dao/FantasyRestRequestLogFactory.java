package com.cricket.dao;

import com.cricket.dto.FantasyRestRequestLogDto;

public class FantasyRestRequestLogFactory {
	
	private static FantasyRestRequestLogDAO fantasyRestRequestLogDAO = null;

	private static FantasyRestRequestLogDAO getDaoInstance() {
		if (fantasyRestRequestLogDAO == null) {
			fantasyRestRequestLogDAO = new FantasyRestRequestLogDAO();
		}
		return fantasyRestRequestLogDAO;
	}

	public static void addReuestLog(FantasyRestRequestLogDto rrLogDto) throws Exception {
		getDaoInstance().addReuestLog(rrLogDto);
	}
	
}
