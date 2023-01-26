package com.cricket.zoho;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dao.ClubFactory;
import com.cricket.dao.LeagueFactory;
import com.cricket.dao.UserFactory;
import com.cricket.dto.ClubDto;
import com.cricket.dto.LeagueDto;
import com.cricket.dto.UserDto;
import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.utility.CommonUtility;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ZohoIntegration {
	
	private static final Logger log = LoggerFactory.getLogger(ZohoIntegration.class);
	
	private static ExecutorService executor = Executors.newFixedThreadPool(1);
	
	private static final String ZOHO_AUTH_URL = "https://accounts.zoho.in/oauth/v2/token";
	private static final String ZOHO_API_URL = "https://www.zohoapis.in/crm/v2/";
	private static final String ZOHO_REFRESH_TOKEN = "1000.734f6a331808b5d4d5e8cb1e3af6d064.2dac6b134ebd9c36653e3aff4ecd5399";	
	private static final String ZOHO_CLIENT_ID = "1000.26XX9QRUX25JR0U1FOJBY6HUKUC6MB";
	private static final String ZOHO_CLIENT_SECRET = "0b25e1385be449d401abd62614da2f98b96d7c5dde";
	private static final String ZOHO_AUTH_REQ_URL = ZOHO_AUTH_URL+"?refresh_token="+ZOHO_REFRESH_TOKEN+"&client_id="+ZOHO_CLIENT_ID+"&client_secret="+ZOHO_CLIENT_SECRET+"&grant_type=refresh_token";

	public static void main(String[] args) {
		
		try {
			createZohoLeads(ClubFactory.getClub(16), null, 0, 0);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	public static void createZohoLeads(ClubDto club, LeagueDto series, int clubId, int userId) {
		
		executor.submit(new Runnable() {
			@Override
			public void run() {
				if(club == null && series == null) {
					log.error("ERROR - Zoho Add Leads - club dto or series dto required - "); 
					return;
				}
				if(series != null && (clubId == 0 || userId == 0)) {
					log.error("ERROR - Zoho Add Leads - Club and User Id required - "); 
					return;
				}
				String accessToken = null;				
				if(club != null) {
					accessToken = getZohoAccessToken(club.getClubId(), 0);
				}else if(series != null) {
					accessToken = getZohoAccessToken(clubId, series.getLeagueId());
				}
				
				if(!CommonUtility.isNullOrEmpty(accessToken)) {
					try {
						HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
						SSLContext sslContext = SSLContext.getDefault();
						SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,	NoopHostnameVerifier.INSTANCE);
						CloseableHttpClient httpclient = httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory).build();

						URIBuilder uriBuilder = new URIBuilder(ZOHO_API_URL + "Leads");
						HttpUriRequest requestObj = new HttpPost(uriBuilder.build());
						HttpEntityEnclosingRequestBase requestBase = (HttpEntityEnclosingRequestBase) requestObj;

						JSONObject requestBody = new JSONObject();

						JSONArray recordArray = new JSONArray();
						
						if(club != null) {
							recordArray.put(getLeadDetailsObjectForLeague(club));
						}else if(series != null) {
							recordArray.put(getLeadDetailsObjectForSeries(series, clubId, userId));
						}

						requestBody.put("data", recordArray);

						JSONArray trigger = new JSONArray();

						trigger.put("approval");
						trigger.put("workflow");
						trigger.put("blueprint");

						requestBody.put("trigger", trigger);

						requestBase.setEntity(new StringEntity(requestBody.toString(), HTTP.UTF_8));

						requestObj.addHeader("Authorization", "Zoho-oauthtoken " + accessToken);
						
						HttpResponse response = httpclient.execute(requestObj);
						
						
						HttpEntity responseEntity = response.getEntity();

						if (responseEntity != null) {
							Object responseObject = EntityUtils.toString(responseEntity);
							String responseString = responseObject.toString();
							System.out.println("HTTP Status Code : " + response.getStatusLine().getStatusCode());
							System.out.println(responseString);
						}
						
					}catch(Exception e) {
						log.error("ERROR - Zoho Add Leads - club id - "+club.getClubId()+"-"+e.getMessage()); 
					}
				}
			}
		});
	}

	private static String getZohoAccessToken(int clubId, int seriesId){
		
		String accessToken = null;		
		try {				
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("text/plain");
			RequestBody body = RequestBody.create("", mediaType);
			
			Request request = new Request.Builder().url(ZOHO_AUTH_REQ_URL).method("POST", body).build();
			Response response = client.newCall(request).execute();
			
			if(response != null && response.code() == 200) {				
				JSONObject obj1 = new JSONObject(response.body().string());				
				accessToken = (String) obj1.get("access_token");
			}else {
				return accessToken;
			}
			
		}catch (Exception e) {	
			String errorMessage = "ERROR - Zoho Leads - Generating Access Token for Club id- "+clubId;
			if(seriesId>0) {
				errorMessage += " - and for Series id - "+seriesId;
			}
			log.error(errorMessage); 
		}
		return accessToken;
	}

	private static JSONObject getLeadDetailsObjectForLeague(ClubDto club) {
		
		JSONObject recordObject = new JSONObject();
		
		recordObject.put("Company", club.getName());
		recordObject.put("Club_ID_Order_ID", club.getClubId()+"");
		if(CommonUtility.isNullOrEmpty(club.getLastName())) {
			recordObject.put("Last_Name", "NO_LAST_NAME");
		}else {
			recordObject.put("Last_Name", club.getLastName());
		}
		if(CommonUtility.isNullOrEmpty(club.getFirstName())) {
			recordObject.put("First_Name", "NO_FIRST_NAME");
		}else {
			recordObject.put("First_Name", club.getFirstName());
		}
		if(CommonUtility.isNullOrEmpty(club.getEmail())) {
			recordObject.put("Email", "NO_EMAIL@cricclubs.com");
		}else {
			recordObject.put("Email", club.getEmail());
		}
		recordObject.put("Phone", club.getPhone());
		recordObject.put("Street", club.getAddress());
		recordObject.put("City", club.getCity());
		recordObject.put("State", club.getState());
		recordObject.put("Zip_Code", club.getZipcode());
		recordObject.put("Country", club.getCountry());
		recordObject.put("Lead_Source", "Sign Up");
		recordObject.put("Product", club.getIsAcademy() == 1?"Academy Management":"League Management");
		recordObject.put("Lead_Status", "Yet to Contact");
		
		return recordObject;
	}
	
	private static JSONObject getLeadDetailsObjectForSeries(LeagueDto series, int clubId, int userId) {
		
		JSONObject recordObject = new JSONObject();
		
		recordObject.put("Club_ID_Order_ID", clubId+"");
		
		UserDto user = null;
		try {
			user = UserFactory.getUserById(userId, clubId);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if(CommonUtility.isNullOrEmpty(user.getLname())) {
			recordObject.put("Last_Name", "NO_LAST_NAME");
		}else {
			recordObject.put("Last_Name", user.getLname());
		}
		if(CommonUtility.isNullOrEmpty(user.getFname())) {
			recordObject.put("First_Name", "NO_FIRST_NAME");
		}else {
			recordObject.put("First_Name", user.getFname());
		}
		if(CommonUtility.isNullOrEmpty(user.getEmail())) {
			recordObject.put("Email", "NO_EMAIL@cricclubs.com");
		}else {
			recordObject.put("Email", user.getEmail());
		}
		recordObject.put("Phone", user.getPhone());
		recordObject.put("Street", user.getAddress());
		recordObject.put("City", user.getCity());
		recordObject.put("State", user.getState());
		
		if(clubId>0) {
			List<ClubDtoLite> clubLiteList = null;
			try {
				clubLiteList = ClubFactory.getLiteClubsFromDB(clubId, true);
				if(!CommonUtility.isListNullEmpty(clubLiteList)) {
					ClubDtoLite clubLite = clubLiteList.get(0);
					if(clubLite != null) {
						recordObject.put("Company", clubLite.getName()+"-"+series.getName());
						recordObject.put("Zip_Code", clubLite.getZipcode());
						recordObject.put("Country", clubLite.getCountry());
					}else {
						recordObject.put("Company", series.getName());
						recordObject.put("Zip_Code", "NO_ZIP_CODE");
						recordObject.put("Country", "NO_COUNTRY");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		recordObject.put("Lead_Source", "League Management");
		recordObject.put("Product", "CricStores");
		recordObject.put("Lead_Status", "Yet to Contact");
		
		return recordObject;
	}

	
}