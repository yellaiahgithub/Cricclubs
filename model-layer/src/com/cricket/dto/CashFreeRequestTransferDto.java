package com.cricket.dto;

import java.math.BigDecimal;

public class CashFreeRequestTransferDto {

	private String beneId;
	private BigDecimal amount;
	private String transferId;
	private String transferMode;
	private String remarks;

	public String getBeneId() {
		return beneId;
	}

	public void setBeneId(String beneId) {
		this.beneId = beneId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
