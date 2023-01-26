package com.cricket.utility;

import java.util.Comparator;
import java.util.Date;

import com.cricket.dto.FixtureDto;
import com.cricket.utility.CommonUtility;

public class FixtureDateSort implements Comparator<FixtureDto>{

	private boolean isDesc = false;
	
	@Override
	public int compare(FixtureDto o1, FixtureDto o2) {
		
		Date date1 = null;
		Date date2 = null;
		
		if(!isDesc) {
			date1 = CommonUtility.getDateFromFixtureString(o1.getDate() , o1.getTime());
			date2 = CommonUtility.getDateFromFixtureString(o2.getDate() , o2.getTime());
		}else {
			date2 = CommonUtility.getDateFromFixtureString(o1.getDate() , o1.getTime());
			date1 = CommonUtility.getDateFromFixtureString(o2.getDate() , o2.getTime());
		}
		
		if(date1 == null && date2 == null) {
			return 0;
		}else if(date1 == null) {
			return -1;
		}else if(date2 == null) {
			return 1;
		}
		if (date1.compareTo(date2) > 0) {
            return 1;
        } else if (date1.compareTo(date2) < 0) {
            return -1;
        }
		return 0;
	}

	public FixtureDateSort(boolean isDesc) {
		super();
		this.isDesc = isDesc;
	}
	public FixtureDateSort() {
		super();
	}
}
