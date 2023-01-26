package com.cricket.utility;

import java.util.Comparator;

import com.cricket.dto.BallDto;

public class BallDtoComparator implements Comparator<BallDto> {

	@Override
	public int compare(BallDto o1, BallDto o2) {
		return o1.getBallId() - o2.getBallId();
	}

}
