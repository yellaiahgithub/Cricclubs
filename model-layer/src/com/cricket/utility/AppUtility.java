package com.cricket.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppUtility {
	private static final Logger log = LoggerFactory.getLogger(AppUtility.class);
	public static final String DEFAULT_DATE_FORMATE = "MM/dd/yyyy";
	
	public static String clubFormateDate(String date, String formate) {
		
		try {
			SimpleDateFormat defaultDateFormate = new SimpleDateFormat(DEFAULT_DATE_FORMATE);
			if(CommonUtility.isNullOrEmpty(formate )) formate = DEFAULT_DATE_FORMATE;
			SimpleDateFormat clubDateFormate = new SimpleDateFormat(formate);
			Date dDate = defaultDateFormate.parse(date);
			String mdate = clubDateFormate.format(dDate);
			return mdate;
		} catch (Exception e) {
			//log.error(e.getMessage(),e);
			return "TBD";
		}
	}
	
	public static Date stringToDate(String date) {
		
		try {
			//SimpleDateFormat defaultDateFormate = new SimpleDateFormat(DEFAULT_DATE_FORMATE);
			SimpleDateFormat clubDateFormate = new SimpleDateFormat(DEFAULT_DATE_FORMATE);
			//Date dDate = defaultDateFormate.parse(date);
			Date mdate = clubDateFormate.parse(date);
			return mdate;
		} catch (Exception e) {
			return null;
		}
	}

	public static String clubFormateDate(Date date, String formate) {
		
		try {
			//SimpleDateFormat defaultDateFormate = new SimpleDateFormat(DEFAULT_DATE_FORMATE);
			if(CommonUtility.isNullOrEmpty(formate )) formate = DEFAULT_DATE_FORMATE;
			SimpleDateFormat clubDateFormate = new SimpleDateFormat(formate);
			//Date dDate = defaultDateFormate.parse(date);
			String mdate = clubDateFormate.format(date);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}	
	
	public static String dateByTimeZone(Date date, String dateFormateReq, String endTimeZone) {
		
		if (date == null) {
			return "";	
		}		
		String jvm_timezone = "CST";	
		if(CommonUtility.isNullOrEmpty(dateFormateReq)) {
			dateFormateReq = DEFAULT_DATE_FORMATE;
		}
		if(CommonUtility.isNullOrEmpty(endTimeZone)) {
			endTimeZone = jvm_timezone;
		}
		try {
			final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			String str = sdf1.format(date);
			sdf1.setTimeZone(TimeZone.getTimeZone(jvm_timezone));
			Date date1 = sdf1.parse(str);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormateReq);
		
			sdf.setTimeZone(TimeZone.getTimeZone(endTimeZone));
			
			String dateOutput = sdf.format(calendar.getTime());
			return dateOutput;
		} catch (Exception e) {
			
		}
		return "";
	}
	
}
