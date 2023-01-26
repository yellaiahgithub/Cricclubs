package com.cricket.dao;

import java.util.List;

import com.cricket.dto.CashFreePGTxnDto;
import com.cricket.utility.CommonUtility;

public class CashFreePGTxnFactory {
	
	private static CashFreePGTxnDAO cashFreePGTxnDAO = null;

	private static CashFreePGTxnDAO getDaoInstance() {
		if (cashFreePGTxnDAO == null) {
			cashFreePGTxnDAO = new CashFreePGTxnDAO();
		}
		return cashFreePGTxnDAO;
	}

	public static CashFreePGTxnDto getPGTxnInfo(CashFreePGTxnDto txnDto) throws Exception {
		
		List<CashFreePGTxnDto> txnList = getDaoInstance().getPGTxnList(txnDto);
		
		if(!CommonUtility.isListNullEmpty(txnList)) {
			return txnList.get(0);
		}
		return null;
	}
	
	public static long insertPGTransaction(CashFreePGTxnDto txnDto) throws Exception {
		return getDaoInstance().insertPGTransaction(txnDto);
	}
	
	public static void updatePGTransaction(CashFreePGTxnDto txnDto) throws Exception {
		getDaoInstance().updatePGTransaction(txnDto);
	}
	
}
