package com.cricket.dao;

import java.util.List;

public class FantasyRulesFactory {
	
	private static FantasyRulesDAO fantasyRulesDAO = null;

	private static FantasyRulesDAO getDaoInstance() {
		if (fantasyRulesDAO == null) {
			fantasyRulesDAO = new FantasyRulesDAO();
		}
		return fantasyRulesDAO;
	}

	public static List<String> getFantasyRulesByType(String type) throws Exception {
		return getDaoInstance().getFantasyRules(type);
	}
	
}
