package com.cricket.utility;

import java.util.ArrayList;
import java.util.List;
import com.cricket.dao.AuditRecordsFactory;
import com.cricket.dao.FixturesFactory;
import com.cricket.dao.MatchesFactory;
import com.cricket.dao.ScoringFactory;
import com.cricket.dto.AuditRecordsDto;
import com.cricket.dto.DeletedMatchDto;
import com.cricket.dto.FixtureDto;
import com.cricket.dto.InningsDto;
import com.cricket.dto.MatchDto;
import com.google.gson.Gson;

public class RestoreScorecard {
	
	public static void main(String[] args) {
		
		try {
			int clubId = 6070;
            int matchId = 106;
			DeletedMatchDto deletedMatchDto = new DeletedMatchDto();
			
			AuditRecordsDto arDto = AuditRecordsFactory.getAuditRecordsByRecordId(clubId, matchId, 
					ApplicationConstants.RECORDS_TYPE_DELETE_SCORE_CARD, ApplicationConstants.AUDIT_TYPE_DELETE);
			if(arDto == null) {
				
			}
			if(arDto != null) {
				String deletedMatchDtoStr = arDto.getData();
				//we may get jsonParsingError on lineNumber 33 just update isFantacy column value as 0.
				deletedMatchDto = new Gson().fromJson(deletedMatchDtoStr, DeletedMatchDto.class);
				if(deletedMatchDto != null) {
					FixtureDto fixture = deletedMatchDto.getFixture();
					if(fixture != null) {
						 FixturesFactory.addMatch(fixture.getFixtureId(), matchId, clubId);					
					}else {
						int fixtureId=FixturesFactory.createFixture(fixture, clubId);
						FixturesFactory.addMatch(fixtureId, matchId, clubId);	
					}
					MatchDto match = deletedMatchDto.getMatch();
					if(match != null) {
						MatchDto dbMatch = MatchesFactory.getMatch(match.getMatchID(), clubId);
						if(dbMatch == null) {
							MatchesFactory.restoreMatch(match, clubId);
						}	
					}
					List<InningsDto> inningsList = deletedMatchDto.getInnings();
					if(!CommonUtility.isListNullEmpty(inningsList)) {
						List<InningsDto> dbInningsList = 	ScoringFactory.getInningsListForMatch(matchId, clubId);
						List<Integer> dbInningsIds = new ArrayList<Integer>();
						for(InningsDto innings : dbInningsList) {
							dbInningsIds.add(innings.getInningsId());
						}
						for (InningsDto innings : inningsList) {
							if (CommonUtility.isListNullEmpty(dbInningsIds)
									|| (!CommonUtility.isListNullEmpty(dbInningsIds)
											&& dbInningsIds.contains(innings.getInningsId()))) {
								ScoringFactory.restoreInnings(innings, clubId);
								try {
									ScoringFactory.restoreBalls(innings.getInningsId(), clubId);
								}catch(Exception e) {
									
								}
							}
						}
					}
					ScoringFactory.synchronizeDeletedScorecardInThread(matchId, clubId);
				}
			}
			System.out.println("scorecard restored successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
