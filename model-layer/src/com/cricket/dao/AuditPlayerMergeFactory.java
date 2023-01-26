package com.cricket.dao;

import java.util.List;

import com.cricket.dto.AuditPlayerMergeDto;

public class AuditPlayerMergeFactory {
	
	private static AuditPlayerMergeDAO auditPlayerMergeDao = null;
	
	private static AuditPlayerMergeDAO getDaoInstance(){
		if(auditPlayerMergeDao == null){
			auditPlayerMergeDao = new AuditPlayerMergeDAO();
		}
		return auditPlayerMergeDao;
	}
	
	public static List<AuditPlayerMergeDto> getPlayerMergeAuditData(int primaryPlayerId, int secondaryPlayerId) throws Exception {
		return getDaoInstance().getPlayerMergeAuditData(primaryPlayerId, secondaryPlayerId);
	}
}
