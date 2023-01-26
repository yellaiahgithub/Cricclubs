package com.cricket.utility;

import java.util.ArrayList;
import java.util.List;

import com.cricket.ccapi.res.UserBasicResponse;
import com.cricket.dao.UserGraphDao;

/**
 * @author ccdeveloper SR
 *
 */
public class UpdateIncorrectFollowersCount {

	public static void main(String[] args) throws Exception {
		try {
			List<Integer> connectUserIdss = new ArrayList<Integer>();
			List<Integer> userIds = UserGraphDao.getGraphDBUserIds(0, 0);
			if (!CommonUtility.isListNullEmpty(userIds)) {
				for (Integer userId : userIds) {
					userId=1878206;
					List<UserBasicResponse> userConnectionObj = UserGraphDao.getConnectionsList(userId, 0, 0);
					if (!CommonUtility.isListNullEmpty(userConnectionObj)) {
						for (UserBasicResponse responseObj : userConnectionObj) {
							//UserGraphDao.followUnFollowUser(userId,	Long.valueOf(responseObj.getUserId()), "FOLLOWED");
							connectUserIdss.add(userId);
							System.out.println(responseObj.getUserId());
						}
					}
				}
				System.out.println("connections went userIds " + connectUserIdss);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
