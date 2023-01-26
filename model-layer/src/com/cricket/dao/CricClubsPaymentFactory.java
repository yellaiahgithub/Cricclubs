package com.cricket.dao;

import java.util.List;

import com.cricket.dto.CricClubsPaymentDto;

public class CricClubsPaymentFactory {
	
	private static CricClubsPaymentsDAO cricclubsPaymentsDAO = null;
	
	private static CricClubsPaymentsDAO getDaoInstance(){
		
		if(cricclubsPaymentsDAO == null){
			cricclubsPaymentsDAO = new CricClubsPaymentsDAO();
		}
		return cricclubsPaymentsDAO;
	}

	public int createCricClubsPayment(CricClubsPaymentDto cricclubsPaymentDto) throws Exception {
		
		return getDaoInstance().createCricClubsPayment(cricclubsPaymentDto);
	}
	
	public CricClubsPaymentDto getCricClubsPayment(int leaguePaymentId)  throws Exception {
		
		return getDaoInstance().getCricClubsPayment(leaguePaymentId);
	}
	
	public List<CricClubsPaymentDto> getCricClubsPayments(int paymentAccountId) throws Exception {
		
		return getDaoInstance().getCricClubsPayments(paymentAccountId);
	}

	public static void main(String[] args) {
		
		try {
			
			CricClubsPaymentDto cricclubsPaymentDto = new CricClubsPaymentDto();
			
			cricclubsPaymentDto.setPaymentDesc("Sample Payment Description");
			cricclubsPaymentDto.setAmount(2);
			cricclubsPaymentDto.setOrigTrxAmount(10);
			
			cricclubsPaymentDto.setPaymentSource("LEAGUE");
			cricclubsPaymentDto.setSourceId("MCC");
			cricclubsPaymentDto.setProvider("WEPAY");
			
			int paymentId = new CricClubsPaymentFactory().createCricClubsPayment(cricclubsPaymentDto);
			
			
			cricclubsPaymentDto = new CricClubsPaymentFactory().getCricClubsPayment(paymentId);
			
		} catch (Exception e) {
		}
	}
}
