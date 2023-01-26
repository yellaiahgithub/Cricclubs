package com.cricket.utility;

import com.cricket.dao.FacebookFactory;
import com.cricket.mailService.Notifier;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;

public class FacebookConnector {

	// private static final String CC_APP_SECRET =
	// "2a34525b91d2712dad7db78de6e9c78e";
	// private static final String CC_APP_ID = "745734118815722";

	public static String createPagePost(String message, String link,
			String accessToken, String pageId) {
		String postId = "";
		try {
			FacebookClient fbClient = new DefaultFacebookClient(accessToken,
					Version.LATEST);
			FacebookType fbtype = fbClient.publish(pageId + "/feed",
					FacebookType.class, Parameter.with("message", message + "\n" + link));
			postId = fbtype.getId();
		} catch (Exception e) {
			handleException("createPagePost", "message:" + message + ",link:"
					+ link + ",pageId:" + pageId, e);
		}
		return postId;
	}

	public static void editPagePost(String message, String link, String postId,
			String accessToken, String pageId) {
		try {
			FacebookClient fbClient = new DefaultFacebookClient(accessToken,
					Version.LATEST);

			fbClient.publish(postId, Post.class,
					Parameter.with("message", message + "\n" + link));
		} catch (Exception e) {
			handleException("createPagePost", "message:" + message + ",link:"
					+ link + ",pageId:" + pageId, e);
		}
	}

	public static String createGroupPost(String message, String link,
			String groupId) {
		String postId = "";
		try {
			String accessToken = FacebookFactory.getUserAccessToken();

			FacebookClient fbClient = new DefaultFacebookClient(accessToken,
					Version.LATEST);
			FacebookType fbtype = fbClient.publish(groupId + "/feed",
					FacebookType.class, Parameter.with("message", message),
					Parameter.with("link", link));
			postId = fbtype.getId();
		} catch (Exception e) {
			handleException("createGroupPost", "message:" + message + ",link:"
					+ link + ",groupId:" + groupId, e);
		}
		return postId;
	}

	private static void handleException(String method, String params,
			Exception e) {
		try {
			Notifier.sendEmail(Notifier.NOTIFY_FROM_EMAIL, "support@cricclubs.com", "Error Posting the message in Facebook", method+params+e.getMessage(),"");
		} catch (Exception e1) {
			// do nothing
		}

	}

	public static void editGroupPost(String message, String link,
			String postId, String groupId) {
		try {
			String accessToken = FacebookFactory.getUserAccessToken();
			FacebookClient fbClient = new DefaultFacebookClient(accessToken,
					Version.LATEST);

			fbClient.publish(postId, Post.class,
					Parameter.with("message", message),
					Parameter.with("link", link));
		} catch (Exception e) {
			handleException("editGroupPost", "message:" + message + ",link:"
					+ link + ",groupId:" + groupId + ",postId:" + postId, e);
		}

	}

	public static void main(String[] args) throws InterruptedException {
		// String postId = "115604995180935_1441171115957643";
		String postId = FacebookConnector
				.createPagePost(
						"Follow IPL 2017 Here. https://cricclubs.com/IPL2017",
						"",
						"EAAKmPcEZCtZBoBAFwPIb7UOuzVVT8qcrEHhA6FMxZCX0ux15v4fnvXrLaxBT9WhYgHUcJoKglv9j3WQlf8dZCLoxsfOFV15JOxqeeE5ZCHihplJdmidYPZAqxwKc6dp79ktTIPDxYXT6DMh8DxGmJ37bxZADCjLnGjcb18SY9XpWAZDZD",
						"115604995180935");
		Thread.sleep(50000);
		FacebookConnector
				.editPagePost(
						"IPL 2017 KKR vs MI. https://cricclubs.com/IPL2017/viewScorecard.do?matchId=8&clubId=2447",
						"https://cricclubs.com/IPL2017/viewScorecard.do?matchId=2&clubId=2447&change=123",
						postId,
						"EAAKmPcEZCtZBoBAFwPIb7UOuzVVT8qcrEHhA6FMxZCX0ux15v4fnvXrLaxBT9WhYgHUcJoKglv9j3WQlf8dZCLoxsfOFV15JOxqeeE5ZCHihplJdmidYPZAqxwKc6dp79ktTIPDxYXT6DMh8DxGmJ37bxZADCjLnGjcb18SY9XpWAZDZD",
						"115604995180935");
	}

}
