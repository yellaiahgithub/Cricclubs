package com.cricket.utility;

import java.util.Comparator;
import java.util.Date;

import com.cricket.dto.MatchDto;

public class MatchDateSort implements Comparator<MatchDto>{

	boolean isDesc = false;
	
	@Override
	public int compare(MatchDto o1, MatchDto o2) {
	
		Date date1 = CommonUtility.getDateFromFixtureString(isDesc ? o2.getMatchDate() : o1.getMatchDate()  , "") ;
		Date date2 = CommonUtility.getDateFromFixtureString(isDesc ? o1.getMatchDate() : o2.getMatchDate()  , "") ;
		
		if(date1 == null && date2 == null) {
			return 0;
		}else if(date1 == null) {
			return -1;
		}else if(date2 == null) {
			return 1;
		}
		return date1.compareTo(date2);
	}

	public MatchDateSort(boolean isDesc) {
		super();
		this.isDesc = isDesc;
	}
	
	public MatchDateSort() {
		super();
	}
	
}