package com.cricket.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarUtil {

	/*
	 * public static void main(String args[]) { List<Integer> days = new
	 * ArrayList<Integer>();
	 * 
	 * days.add(1); days.add(7); //days.add(6); //getValidDateList(new Date(), days,
	 * 5); Date date = getNextDateByDayOfWeek(new Date(), 3); Date date1 =
	 * getNextDateByDayOfWeek(new Date(), 5); 
	 * 
	 * 
	 * List<Date> dates = getValidDateList(new Date(),days, 5 );
	 *  }
	 */

	
	public static void removeUnavailableDates(List<Date> dates, List<Date> unavailableDates) {
		dates.removeAll(unavailableDates);
	}

	public static void setTimeOfMatch(List<Date> dates, List<String> timeOfMatch) {
		// TODO Auto-generated method stub
		
	}
	
	public static String getDateToString(Date date, String formate){
		
		return new SimpleDateFormat(formate).format(date);
	}
	
	public static Date getDateFromString(String date, String formate) {
	     try {
	         return new SimpleDateFormat(formate).parse(date);
	     } catch (Exception e) {
	         return null;
	     }
	  }
	
	public static List<Date> getDateFromString(List<String> dates, String formate) {
		List<Date> convertedDates = new ArrayList<Date>(dates.size());
		
	     try {
	    	 for(String date : dates) {
	    		 Date d = getDateFromString(date, formate);
	    		 if(null != d) {
	    			 convertedDates.add(d);
	    		 }
	    	 }
	     } catch (Exception e) {
	         return null;
	     }
	     return convertedDates;
	  }
	
	public static List<Date> getValidDateList(Date startDate, List<Integer> days) {
		List<Date> dates = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		
		for(int i=0;i<750;i++){
			if(i>0) {
				cal.add(Calendar.DATE,1);
			}
			if(days.contains(cal.get(Calendar.DAY_OF_WEEK))){
				dates.add(setTime( cal.getTime(), 0, 0, 0, 0 ));
			}
		}
		
		return dates;
	}

	
	public static int dayOfWeek(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);		
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	public static Date getNextDateByDayOfWeek(Date startDate, int dayOfWeek) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate); // Now use today date.
		int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
		if (currentDay > dayOfWeek) {
			calendar.add(Calendar.DATE, (dayOfWeek - currentDay + 7));
		}
		else {
			calendar.add(Calendar.DATE, dayOfWeek - currentDay);
		}
		
		return setTime( calendar.getTime(), 0, 0, 0, 0 );
	}
	public static Date setTime( final Date date, final int hourOfDay, final int minute, final int second, final int ms )
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( date );
		calendar.set( Calendar.HOUR_OF_DAY, hourOfDay );
		calendar.set( Calendar.MINUTE, minute );
		calendar.set( Calendar.SECOND, second );
		calendar.set( Calendar.MILLISECOND, ms );
	    return calendar.getTime();
	}
	
	public static List<Date> setTime( final List<Date> dates, final int hourOfDay, final int minute, final int second, final int ms )
	{
		List<Date> updatedDates = new ArrayList<Date>(dates.size());
		for(Date date : dates) {
			updatedDates.add(setTime(date, hourOfDay, minute, second, ms));
		}
		return updatedDates;
	}
	public static List<Date> getAvilableDatsWithTime(Date date, List<String> matchTime) {
		List<Date> availableDatesWithTime = new ArrayList<Date>();
			for(String time : matchTime) {
				String[] hhmm = time.split(":");
				Date dateWithTime = CalendarUtil.setTime(date, Integer.valueOf(hhmm[0]), Integer.valueOf(hhmm[1]), 0, 0);
				availableDatesWithTime.add(dateWithTime);
			}
		return availableDatesWithTime;
	}
	public static List<Date> getAvilableDatsWithTime(List<Date> availableDates, List<String> matchTime) {
		List<Date> availableDatesWithTime = new ArrayList<Date>();
		for(Date date : availableDates) {
			for(String time : matchTime) {
				String[] hhmm = time.split(":");
				Date dateWithTime = CalendarUtil.setTime(date, Integer.valueOf(hhmm[0]), Integer.valueOf(hhmm[1]), 0, 0);
				availableDatesWithTime.add(dateWithTime);
			}
		}
		return availableDatesWithTime;
	}
	
	public static List<Date> getDaysBetweenDates(Date startdate, Date enddate)
	{
	    List<Date> dates = new ArrayList<Date>();
	    Calendar calendar = new GregorianCalendar();
	    
	    calendar.setTime(enddate);
	    calendar.add(Calendar.DATE, 1);
	    enddate = calendar.getTime();
	    calendar.setTime(startdate);

	    while (calendar.getTime().before(enddate))
	    {
	        Date result = calendar.getTime();
	        dates.add(result);
	        calendar.add(Calendar.DATE, 1);
	    }
	    return dates;
	}

	public static Date getStartDateOfCurWeek(Date date) {
		Calendar calendar = new GregorianCalendar();
	    calendar.setTime(date);
	    calendar.add(Calendar.DATE, -3);
	   return calendar.getTime();
	}

	public static Date getEndDateOfCurWeek(Date date) {
		Calendar calendar = new GregorianCalendar();
	    calendar.setTime(date);
	    calendar.add(Calendar.DATE, 2);
	   return calendar.getTime();
	}
}
