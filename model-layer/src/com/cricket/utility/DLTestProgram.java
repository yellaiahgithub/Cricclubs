package com.cricket.utility;

import java.util.List;

import com.cricket.dao.MatchDLRecordsFactory;
import com.cricket.dlscalculation.MatchDLRecord;

public class DLTestProgram {
	
	public static void main(String[] args) {
		
		try {
			
			DLCalculatorICC dl = new DLCalculatorICC();
			
			List<MatchDLRecord> recordList = MatchDLRecordsFactory.getDLRecords(896, 1, 50);
			
			int parScore = dl.parScoreCC(5.3, 0, 73, 73, 20, recordList);
			
			System.out.println(parScore);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
