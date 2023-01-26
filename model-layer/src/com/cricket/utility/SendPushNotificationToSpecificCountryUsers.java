package com.cricket.utility;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.cricket.dao.MobileNotificationDetailsFactory;
import com.cricket.dao.PushNotificationCredential;
import com.cricket.dto.pushnotification.MobileNotificationDetail;
import com.cricket.dto.pushnotification.PushNotificationMsg;
import com.cricket.mailService.Notifier;
import com.cricket.service.pushnotification.MobilePushNotificationService;

public class SendPushNotificationToSpecificCountryUsers {
	
	public static void main(String[] args) {
		
		try {
 			PushNotificationMsg pnm = new PushNotificationMsg();

			pnm.setTitle("");
			pnm.setBody("Final call to grab your customised jerseys & other merchandise for a flat 25% off, Hop on to cricstores and don't forget to use your coupon code:CRIC25. Hurry now!!!");
			pnm.setDeviceType("Mobile");
			pnm.setPriority("high");
			
			List<String> tokenIds = new ArrayList<String>();
			
			/*
				tokenIds.add("1e80e325-a98f-4f5c-aec6-a47dff0f0b42");
				tokenIds.add("5fc62350-92d1-11ec-a895-8e2bba5a1f97");
				tokenIds.add("9078610a-d33c-42fd-b8e3-ee2a8f0cb281");
				tokenIds.add("b25d71fe-2943-4a4b-a9b4-b1b539d4ef5b");
			*/
			//Enable  Line number 36 and 45 for testing
			// String userIdsStr = "1965218,2781118,1612861,999230,2001082";
			
			PushNotificationCredential pnc = MobileNotificationDetailsFactory.getMobileNotificationCredentialsByProvider("OneSignal");

			MobileNotificationDetail mobileNotificationDetail = new MobileNotificationDetail();
			mobileNotificationDetail.setDeviceType(pnm.getDeviceType());

			List<MobileNotificationDetail> mndList = null;
			
			// tokenIds = MobileNotificationDetailsFactory.getUserMobileTokensByUserIds(userIdsStr);
			
			if(tokenIds.size()==0) {
				mndList = MobileNotificationDetailsFactory.getMobileNotificationCredentialsByCountries("USA");
			}
			
			if(!CommonUtility.isListNullEmpty(mndList)) {
				for (MobileNotificationDetail mnd : mndList) {
					if (!CommonUtility.isNullOrEmpty(mnd.getToken())) {
						tokenIds.add(mnd.getToken());
					}
				}
			}

			JSONObject info = new JSONObject();
			JSONObject notification = new JSONObject();
			notification.put("title", pnm.getTitle());
			notification.put("body", pnm.getBody());
			info.put("notification", notification);

			JSONObject data = new JSONObject();

			data.put("notificationFor", "");
			data.put("notificationType", "");
			data.put("userId", "");
			info.put("data", data);
			info.put("icon", "ic_launcher");

			if (pnc != null) {
				try {
					Notifier.sendEmail("pavan@cricclubs.com", "pavan@cricclubs.com,ln@cricclubs.com", "Push Notifications to Specific Country", "Program Started", null, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				MobilePushNotificationService.sendPushNotificationToSpecificCountry(tokenIds, pnm, pnc, info);
				
				System.out.println("Program Completed");
				try {
					Notifier.sendEmail("support@cricclubs.com", "pavan@cricclubs.com,ln@cricclubs.com", "Push Notifications to Specific Country", "Program Completed", null, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
