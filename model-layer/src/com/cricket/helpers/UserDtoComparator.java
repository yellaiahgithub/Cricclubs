package com.cricket.helpers;

import java.util.Comparator;

import com.cricket.dto.PlayerDto;
import com.cricket.dto.UserDto;
import com.restfb.types.User;

public class UserDtoComparator implements Comparator<UserDto> {
	
	boolean shortByFName = false;
	boolean shortByLName = false;
	boolean isDesc = false;
	
	@Override
	public int compare(UserDto o1, UserDto o2) {
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
				if(o2.getFname() != null) {
					return o2.getFname().compareTo(o1.getFname());
				}else {
					return 1;
				}
			}else {
				if(o1.getFname() != null) {
					return o1.getFname().compareTo(o2.getFname());
				}else {
					return -1;
				}
			}
		}
		if(shortByLName && o1 != null && o2 != null) {
			if(isDesc) {
				if(o2.getLname() != null) {
					return o2.getLname().compareTo(o1.getLname());
				}else {
					return 1;
				}
			}else {
				if(o1.getLname() != null) {
					return o1.getLname().compareTo(o2.getLname());
				}else {
					return -1;
				}
			}
		}
		return 0;
	}


	public UserDtoComparator(boolean shortByFName, boolean shortByLName, boolean isDesc) {
		super();
		this.shortByFName = shortByFName;
		this.shortByLName = shortByLName;
		this.isDesc = isDesc;
	}

}
