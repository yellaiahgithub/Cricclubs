package com.cricket.authentication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.cricket.dao.UserFactory;
import com.cricket.dto.UserDto;
import com.cricket.utility.CommonUtility;

public class FacebookAuthentication
{

	public static UserDto getUserForAccessToken(String accessToken, int clubId) throws Exception {
		
		String userDetails = "";
		String userEmail = "";
		String UserTokenId = "";
		UserDto user = null;
		
		if(!CommonUtility.isNullOrEmpty(accessToken)) {
			userDetails = getUserDetailsForFBAccessToken(accessToken);
		}
		
		if(!CommonUtility.isNullOrEmpty(userDetails) && userDetails.toLowerCase().contains("email")) {	
			userDetails = userDetails.replace("\\u0040", "@");
			userEmail = userDetails.substring(userDetails.toLowerCase().indexOf("email")+8, userDetails.toLowerCase().indexOf("id")-3);
			userEmail = userEmail.trim();
			UserTokenId = userDetails.substring(userDetails.toLowerCase().indexOf("id")+5, userDetails.length()-3);
			UserTokenId = UserTokenId.trim();		
		}
		
		if(!CommonUtility.isNullOrEmpty(UserTokenId)) {	
			user = UserFactory.getUserByFacebookId(UserTokenId, clubId);
		}
		
		if (user == null) {			
			if (!CommonUtility.isNullOrEmpty(UserTokenId))
			{
				user = UserFactory.getUserByEmail(userEmail, clubId);
				if (user != null) {
					UserFactory.addFacebookUser(user.getUserID(), UserTokenId);
				}

			}

		}
		
		return user;
	}

	public static String getEmailForAccessToken(String accessToken) throws Exception {
		String userDetails = "";
		String userEmail = "";		
		
		if(!CommonUtility.isNullOrEmpty(accessToken)) {
			userDetails = getUserDetailsForFBAccessToken(accessToken);
		}
		
		if(!CommonUtility.isNullOrEmpty(userDetails) && userDetails.toLowerCase().contains("email")) {	
			userDetails = userDetails.replace("\\u0040", "@");
			userEmail = userDetails.substring(userDetails.toLowerCase().indexOf("email")+8, userDetails.toLowerCase().indexOf("id")-3);
			userEmail = userEmail.trim();					
		}

		return userEmail;
	}
	
	public static String getUserDetailsForFBAccessToken(String accessToken)
	{
		
		String userDetails = "";		
		try {

			String g = "https://graph.facebook.com/me?fields=email&access_token=" + accessToken;
			URL u = new URL(g);
			URLConnection c = u.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					c.getInputStream()));
			String inputLine;
			StringBuffer b = new StringBuffer();
			while ((inputLine = in.readLine()) != null)
				b.append(inputLine + "\n");
			in.close();
			userDetails = b.toString();						
		} catch (Exception e) {
			throw new RuntimeException("ERROR in getting FB graph data. " + e);
		}
		
		return userDetails;

	}
}
