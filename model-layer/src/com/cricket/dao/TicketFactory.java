package com.cricket.dao;

import java.util.List;

import com.cricket.dto.Ticket;

public class TicketFactory {
	
	private static TicketDAO ticket = null;
	
	private static TicketDAO getDaoInstance(){
		if(ticket == null){
			ticket = new TicketDAO();
		}
		return ticket;
	}
	
	public static Ticket save(Ticket ticket, int clubId) throws Exception{
		return getDaoInstance().save(ticket, clubId);
	}

	public static int getAllCount(int clubId) {
		return getDaoInstance().getAllCount(clubId);
	}
	
	public static List<Ticket> getAll(int clubId) throws Exception{
		return getDaoInstance().getAll(clubId);
	}

	public static int getApproveRejectTicket(int status, int tktId, int clubId, String actionTakenBy) throws Exception {
		return getDaoInstance().getApproveRejectTicket(status, tktId, clubId, actionTakenBy);
	}

	public static Ticket getTicket(int tktId, int clubId) throws Exception{
		return getDaoInstance().getTicket(tktId, clubId);
	}
	
}
