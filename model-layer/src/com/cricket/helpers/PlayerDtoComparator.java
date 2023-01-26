package com.cricket.helpers;

import java.util.Comparator;

import com.cricket.dto.PlayerDto;

public class PlayerDtoComparator implements Comparator<PlayerDto> {
	
	boolean shortByFName = false;
	boolean shortByLName = false;
	boolean isDesc = false;
	
	@Override
	public int compare(PlayerDto o1, PlayerDto o2) {
		if(shortByFName && shortByLName) {
			if(o1 != null && o2 != null) {
				if(isDesc) {
					return o1.getFullName().compareTo(o2.getFullName());
				}else {
					return o2.getFullName().compareTo(o1.getFullName());
				}
			}
		}
		
		if(shortByFName && o1 != null && o2 != null) {
			if(isDesc) {
				if(o2.getFirstName() != null) {
					return o2.getFirstName().compareTo(o1.getFirstName());
				}else {
					return 1;
				}
			}else {
				if(o1.getFirstName() != null) {
					return o1.getFirstName().compareTo(o2.getFirstName());
				}else {
					return -1;
				}
			}
		}
		if(shortByLName && o1 != null && o2 != null) {
			if(isDesc) {
				if(o2.getLastName() != null) {
					return o2.getLastName().compareTo(o1.getLastName());
				}else {
					return 1;
				}
			}else {
				if(o1.getLastName() != null) {
					return o1.getLastName().compareTo(o2.getLastName());
				}else {
					return -1;
				}
			}
		}
		return 0;
	}


	public PlayerDtoComparator(boolean shortByFName, boolean shortByLName, boolean isDesc) {
		super();
		this.shortByFName = shortByFName;
		this.shortByLName = shortByLName;
		this.isDesc = isDesc;
	}

}
