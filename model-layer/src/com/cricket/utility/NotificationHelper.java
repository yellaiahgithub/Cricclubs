package com.cricket.utility;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.configuration.EnviromentProperties;
import com.cricket.dao.PlayerFactory;
import com.cricket.dao.UserFactory;
import com.cricket.dto.AlbumDto;
import com.cricket.dto.ArticleDto;
import com.cricket.dto.ClaimProfileDto;
import com.cricket.dto.ClubDto;
import com.cricket.dto.CommentDto;
import com.cricket.dto.DocumentDto;
import com.cricket.dto.FantasyUserDto;
import com.cricket.dto.FixtureDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.MessageDto;
import com.cricket.dto.NewsDto;
import com.cricket.dto.Pair;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.PostGraphDto;
import com.cricket.dto.TeamDetailsDto;
import com.cricket.dto.TeamDto;
import com.cricket.dto.Ticket;
import com.cricket.dto.UmpireDto;
import com.cricket.dto.UserDto;
import com.cricket.dto.adconfig.AdConfigGlobalDTO;
import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.dto.lite.LeagueLite;
import com.cricket.mailService.Notifier;

public class NotificationHelper {

	private static ExecutorService executor = Executors.newFixedThreadPool(5);
	private static Logger log = LoggerFactory.getLogger(NotificationHelper.class);
	
	public static void sendForgotPasswordEmail(UserDto user, ClubDtoLite club) {
		String clubUrl="www.cricclubs.com";
		Map<String, String> paramsMap = new HashMap<String, String>();
		
		if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
		{
			clubUrl=club.getCustomDomain();
		}
		
		paramsMap.put("\\$clubName", club.getName());
		paramsMap.put("\\$playerName", user.getFname());
		paramsMap.put("\\$userName", user.getUserName());
		paramsMap.put("\\$clubUrl", clubUrl);
		
		String password = "";
		
		try {
			password = CommonUtility.decrypt(user.getPassword());
		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		paramsMap.put("\\$password", password);
		paramsMap.put("\\$clubId", "" + club.getClubId());
		paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
		
		String message = "";
		try {
			if(club.getIsAcademy()==1) {
				String academyUrl = EnviromentProperties.getEnvProperty("cricclubs.academy.root.url");
				academyUrl = academyUrl+club.getShortURL()+"/home";	
				paramsMap.put("\\$academyURL", academyUrl);
				message = Notifier.getMailBody(Notifier.FORGOT_PASSWORD_TEMPLATE_ACADEMY, paramsMap);
			}else {
				message = Notifier.getMailBody(Notifier.FORGOT_PASSWORD_TEMPLATE, paramsMap);
			}			
			String subject = "Your Login password for " + club.getName();
			Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), user.getEmail(),
					subject, message,club.getEmail());
		} catch (Exception e) {
			handleException(e);
		}

	}
	
	public static void sendForgotPasswordEmail(UserDto user, ClubDtoLite club, String requestFrom) {
		
		String clubUrl="www.cricclubs.com";
		Map<String, String> paramsMap = new HashMap<String, String>();
		
		if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
		{
			clubUrl=club.getCustomDomain();
		}
		
		if(CommonUtility.isNullOrEmpty(requestFrom)) {
			requestFrom = "LEAGUE";
		}
		
		paramsMap.put("\\$clubName", club.getName());
		paramsMap.put("\\$playerName", user.getFname());
		paramsMap.put("\\$userName", user.getUserName());
		paramsMap.put("\\$clubUrl", clubUrl);
		
		String password = "";
		
		try {
			password = CommonUtility.decrypt(user.getPassword());
		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		paramsMap.put("\\$password", password);
		paramsMap.put("\\$clubId", "" + club.getClubId());
		paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
		
		String message = "";
		try {
			if(club.getIsAcademy()==1 && "ACADEMY".equals(requestFrom)) {
				String academyUrl = EnviromentProperties.getEnvProperty("cricclubs.academy.root.url");
				academyUrl = academyUrl+club.getShortURL()+"/home";	
				paramsMap.put("\\$academyURL", academyUrl);
				message = Notifier.getMailBody(Notifier.FORGOT_PASSWORD_TEMPLATE_ACADEMY, paramsMap);
			}else {
				message = Notifier.getMailBody(Notifier.FORGOT_PASSWORD_TEMPLATE, paramsMap);
			}			
			String subject = "Your Login password for " + club.getName();
			Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), user.getEmail(),
					subject, message,club.getEmail());
		} catch (Exception e) {
			handleException(e);
		}

	}
	
	public static void sendEmailVerificationLink(FantasyUserDto user) throws Exception {
		
		Map<String, String> paramsMap = new HashMap<String, String>();	
		
		paramsMap.put("\\$userName", user.getFname());	
		
		String cricClubsUrl = EnviromentProperties.getEnvProperty("cricclubs.www.root.url");
		
		String verificationLink = cricClubsUrl + "/verifyFantasyEmail.do?email="+ URLEncoder.encode(CommonUtility.encrypt(user.getEmail()),"UTF-8");
		String unsubscribeLink = cricClubsUrl+ "/unsubscribeMarketingEmail.do?email="+ URLEncoder.encode(CommonUtility.encrypt(user.getEmail()),"UTF-8");
		
		paramsMap.put("\\$verificationLink", verificationLink);	
		paramsMap.put("\\$unsubscribeLink", unsubscribeLink);			
		
		String message =  Notifier.getMailBody(Notifier.VERIFY_FANTASY_EMAIL, paramsMap);
		String subject = "Cricclubs Fantasy Account - Email Verification";
		
		Notifier.sendEmail("notify@cricclubs.com",user.getEmail(),subject,message,"");
	}
	
	public static void sendEmailVerificationLinkV1(UserDto user) throws Exception {
		
		Map<String, String> paramsMap = new HashMap<String, String>();	
		
		paramsMap.put("\\$userName", user.getFname()+" "+user.getLname());	
		
		String cricClubsUrl = EnviromentProperties.getEnvProperty("cricclubs.www.root.url");
		
		String verificationLink = cricClubsUrl + "/verifyEmail.do?email="+ URLEncoder.encode(CommonUtility.encrypt(user.getEmail()),"UTF-8");
		String unsubscribeLink = cricClubsUrl+ "/unsubscribeMarketingEmail.do?email="+ URLEncoder.encode(CommonUtility.encrypt(user.getEmail()),"UTF-8");
		
		paramsMap.put("\\$verificationLink", verificationLink);	
		paramsMap.put("\\$unsubscribeLink", unsubscribeLink);			
		
		String message =  Notifier.getMailBody(Notifier.VERIFY_EMAIL, paramsMap);
		String subject = "Cricclubs Account - Email Verification";
		
		Notifier.sendEmail("notify@cricclubs.com",user.getEmail(),subject,message,"");
	}
	
	public static void sendFantasyWithdrawRequestEmail(FantasyUserDto user, float amount) throws Exception {
		
		String message =  user.getFname()+ " has requested INR"+ amount;
		String subject = "Withdraw Request Received from CC Fantasy User with Mobile-"+user.getPhone();
		
		Notifier.sendEmail("notify@cricclubs.com","notify@cricclubs.com",subject,message,"");
	}
	
	public static void sendEmailForNewInternationalNews(NewsDto news, String emailTo, String envToUse) throws Exception {
		
		String host = "";
		if("test".equalsIgnoreCase(envToUse)) {
			host = "www-test.";
		}
		String newsLink = "https://"+host+"cricclubs.com/InternationalScores/viewNewsNew.do?newsId="+news.getNewsId()+"&clubId=11707";
		String message = "News Title - <b>"+news.getTitle()+"</b>";
		message += "<br><br>Click <a href='"+newsLink+"' target='_blank'>here</a> to View News Details";
		message += "<br><br>Thank You,";
		message += "<br>CricClubs Team";		
		String subject = "Notification - New International News Posted" ;		
		Notifier.sendEmail("notify@cricclubs.com",emailTo,subject,message,"");
	}
	
	public static void sendEmailForReportingPost(PostGraphDto post, String host) throws Exception {
		
		String deletePostURL = host+"/CCAPI/post/delete?postId="+post.getPostId();
		
		if(!host.contains("localhost")) {
			deletePostURL = "https://"+deletePostURL;
		}
		String postContent = post.getContent();	
		List<Object> hashTags = post.getHashTags();
		String postHashTags = "";
		
		if(hashTags != null && hashTags.size()>0) {
			for(Object tag:hashTags) {
				postHashTags += tag.toString();
			}
			
		}
		String subject = "Report Post - "+post.getTitle();	
		String postImageUrl = post.getImageUrl();
		
		Map<String, String> paramsMap = new HashMap<String, String>();	
		paramsMap.put("\\$postContent", postContent);	
		paramsMap.put("\\$postImageURL", postImageUrl);	
		paramsMap.put("\\$deletePostURL", deletePostURL);
		paramsMap.put("\\$postHashTags", postHashTags);
		
		String message =  Notifier.getMailBody(Notifier.REPORT_POST_EMAIL, paramsMap);
		
		Notifier.sendEmail("notify@cricclubs.com","ln@cricclubs.com",subject,message,"");
	}
	
	public static void sendEmailForJerseyOrder(String emailTo, String orderDetails, String customerDetails, String subject) throws Exception {
		
		Map<String, String> paramsMap = new HashMap<String, String>();	
		paramsMap.put("\\$customerDetails", customerDetails);	
		paramsMap.put("\\$orderDetails", orderDetails);		
		String message =  Notifier.getMailBody(Notifier.JERSEY_ORDER_EMAIL, paramsMap);
		Notifier.sendEmail("notify@cricclubs.com",emailTo,subject,message,"");
	}
	
	public static void sendEmailCashFreeRegistrationFailure(FantasyUserDto user) throws Exception {
		
		String message =  "Cash Free Beneficiary Registration Has Been Failed for "+user.getFname();
		String subject = "Cash Free Beneficiary Registration Failure - "+user.getUserId();
		
		Notifier.sendEmail("notify@cricclubs.com","notify@cricclubs.com",subject,message,"");
	}
	
	public static void sendOTPToEmail(String email, String otp) throws Exception {
		
		Map<String, String> paramsMap = new HashMap<String, String>();		
		paramsMap.put("\\$forgotpasswordOTP", otp);		
		String message =  Notifier.getMailBody(Notifier.SEND_OTP_TO_EMAIL, paramsMap);
		String subject = "CricClubs Account - "+otp+" is your verification code";
		Notifier.sendEmail("notify@cricclubs.com", email, subject, message, "");
	}
	
	public static void sendOTPToEmailForUserRegistration(String email, String otp) throws Exception {
		
		Map<String, String> paramsMap = new HashMap<String, String>();		
		paramsMap.put("\\$regOTP", otp);		
		String message =  Notifier.getMailBody(Notifier.SEND_OTP_TO_EMAIL_FOR_USER_REGISTRATION, paramsMap);
		String subject = "CricClubs Account - "+otp+" is your verification OTP";
		Notifier.sendEmail("notify@cricclubs.com", email, subject, message, "");
	}
	
	public static void sendCustomBulkEmails(List<String> emails, String subject, String message) throws Exception {
		for(String email : emails) {
			Notifier.sendEmail("notify@cricclubs.com", email, subject, message, "");
		}
	}
	
	private static String getEmailAdContent(ClubDtoLite club) {
		String adContent = "";
		if(!club.isRemoveCCAds() && !club.getDisableBannerAd()){
			Map<String,AdConfigGlobalDTO> adConfigMap = CommonLogic.getAdConfigGlobal(club);
			AdConfigGlobalDTO adConfig = (adConfigMap == null)?null:adConfigMap.get("EMAIL");
			if(adConfig != null){
				return adConfig.getImageUrl();
			}
		}
		return adContent;
	}
	
	public static void sendWelcomePlayerEmail(final UserDto user, final ClubDtoLite club, final boolean existingUser) {
		sendWelcomePlayerEmail(user, club, existingUser, null);
	}

	public static void sendWelcomePlayerEmail(final UserDto user, final ClubDtoLite club, final boolean existingUser, final ClubDto internalClub) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", user.getFname());
					paramsMap.put("\\$userName", user.getUserName());
					paramsMap.put("\\$userName", user.getUserName());
					paramsMap.put("\\$userName", user.getUserName()); 
					paramsMap.put("\\$clubUrl", clubUrl);
					if(!existingUser) {
						String password = CommonUtility.decrypt(user.getPassword());
						paramsMap.put("\\$password", password);
					}else {
						paramsMap.put("\\$password", "Use Existing Account Password");
					}
					
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					String toEmails = "";
					
					if (!CommonUtility.isNullOrEmpty(user.getEmail())) {
						toEmails = user.getEmail();
					}
					if (!CommonUtility.isNullOrEmpty(club.getEmail())) {
						toEmails += "," + club.getEmail();
					}
					if (internalClub != null && !CommonUtility.isNullOrEmpty(internalClub.getEmail())) {

						toEmails += "," + internalClub.getEmail();
					}
					
						String message = Notifier.getMailBody(
								Notifier.PLAYER_WELCOME_TEMPLATE, paramsMap);
						String subject = "Welcome to " + club.getName();
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), toEmails,
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}

	public static void sendWelcomeUmpireEmail(final UserDto user, final ClubDtoLite club,final String userType, final boolean existingUser) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", user.getFname());
					paramsMap.put("\\$userName", user.getUserName());				 
					paramsMap.put("\\$clubUrl", clubUrl);
					
					if(!existingUser) {
						paramsMap.put("\\$password", user.getPassword());
					}else {
						paramsMap.put("\\$password", "Use Existing Account Password");
					}
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					paramsMap.put("\\$userType", "" +userType);
						String message = Notifier.getMailBody(
								Notifier.UMPIRE_WELCOME_TEMPLATE, paramsMap);
						String subject = "Welcome to " + club.getName();
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), user.getEmail(),
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}
	
	
	public static void sendEmailToStoresSupport(final UserDto user, final ClubDtoLite club,final String seriesName ,final int leagueId) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					String serverName = "http://www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain())){
						serverName = "http://www."+club.getCustomDomain();						 
					}
					
					String seriesURL = serverName+"/viewLeague.do?league="+leagueId+"&clubId="+club.getClubId();
					String leagueURL = serverName+"/home.do?clubId="+club.getClubId();
					
					paramsMap.put("\\$seriesName", seriesName);
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$userName", user.getUserName());				 
					paramsMap.put("\\$clubId", "" + club.getClubId());	
					paramsMap.put("\\$seriesURL", "" + seriesURL);
					paramsMap.put("\\$leagueURL", "" + leagueURL);
					
					if(!CommonUtility.isNullOrEmpty(user.getEmail())) {
						paramsMap.put("\\$email", user.getEmail());		
					}else {
						paramsMap.put("\\$email", club.getEmail());		
					}
					if(!CommonUtility.isNullOrEmpty(user.getPhone())) {
						paramsMap.put("\\$phone", user.getPhone());		
					}else {
						paramsMap.put("\\$phone", club.getEmail());		
					}
					
						String message = Notifier.getMailBody(
								Notifier.STORES_NOTIFICATION_TEMPLATE, paramsMap);
						String subject = "Series created " + club.getName();
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), "sales@cricclubs.com",
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}
	
	public static void sendWelcomeAcademyUserEmail(final UserDto user, final ClubDtoLite club,final String userType, final boolean existingUser) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", user.getFname());
					paramsMap.put("\\$userName", user.getUserName());
					paramsMap.put("\\$clubUrl", clubUrl);
					
					if(!existingUser) {
						paramsMap.put("\\$password", user.getPassword());
					}else {
						paramsMap.put("\\$password", "Use Existing Account Password");
					}
					String academyUrl = EnviromentProperties.getEnvProperty("cricclubs.academy.root.url");
					academyUrl = academyUrl+club.getShortURL()+"/home";					
					paramsMap.put("\\$academyURL", academyUrl);					
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					paramsMap.put("\\$userType", "" +userType);
						String message = Notifier.getMailBody(
								Notifier.ACADEMY_USER_WELCOME_TEMPLATE, paramsMap);
						String subject = "Welcome to " + club.getName();
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), user.getEmail(),
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}

	public static void sendClaimApprovalToAdmin(final ClaimProfileDto claim, final ClubDtoLite club) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", claim.getPlayerName());
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$email",claim.getEmail());
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					paramsMap.put("\\$clubUrl", clubUrl);
					
						String message = Notifier.getMailBody(
								Notifier.CLAIM_APPROVAL_ADMIN_TEMPLATE, paramsMap);
						String subject = "Profile Claim Waiting for your Approval ";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), club.getEmail(),
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}

	public static void sendPlayerApprovalToAdmin(final PlayerDto player, final ClubDtoLite club) {
		
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					
					Map<String, String> paramsMap = new HashMap<String, String>();
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", player.getFullName());
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$email", "" + player.getEmail());
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					paramsMap.put("\\$clubUrl", clubUrl);
					
					if(!CommonUtility.isNullOrEmpty(club.getPlayerTerms())){
						paramsMap.put("\\$extraText", "During registration process Player - "+player.getFullName()+" has accepted below Terms and Conditions:<br>" + club.getPlayerTerms());
					}else{
						paramsMap.put("\\$extraText", "");
					}
						String message = Notifier.getMailBody(
								Notifier.PLAYER_APPROVAL_ADMIN_TEMPLATE, paramsMap);
						String subject = "Player Registration waiting for Admin Approval ";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), club.getEmail()+"," + player.getEmail(),
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}
	
public static void sendPlayerApprovalToClubAndLeagueAdmins(final PlayerDto player, final ClubDtoLite club
		,final TeamDto team,final TeamDetailsDto teamDetails, ClubDto internalClub) {
		
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					
					Map<String, String> paramsMap = new HashMap<String, String>();
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", player.getFullName());
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$email", "" + player.getEmail());
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					paramsMap.put("\\$clubUrl", clubUrl);
					
					String toEmails = "";
					if (!CommonUtility.isNullOrEmpty(player.getEmail())) {
						toEmails = player.getEmail() + "," + club.getEmail();
					}
					if (team != null && teamDetails != null && !CommonUtility.isNullOrEmpty(team.getCaptainEmail())
							&& !CommonUtility.isNullOrEmpty(teamDetails.getAdminEmail())) {

						toEmails +=  "," + teamDetails.getAdminEmail();
					}
					if (internalClub != null && !CommonUtility.isNullOrEmpty(internalClub.getEmail())) {

						toEmails += "," + internalClub.getEmail();
					}
					
					if(!CommonUtility.isNullOrEmpty(club.getPlayerTerms())){
						paramsMap.put("\\$extraText", "During registration process Player - "+player.getFullName()+" has accepted below Terms and Conditions:<br>" + club.getPlayerTerms());
					}else{
						paramsMap.put("\\$extraText", "");
					}
						String message = Notifier.getMailBody(
								Notifier.PLAYER_APPROVAL_ADMIN_TEMPLATE, paramsMap);
						String subject = "Player Registration waiting for Admin Approval ";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()),toEmails,
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}
		
		
	
public static void sendTicketSubmitionToAdmin(final Ticket ticket, final ClubDtoLite club) {
		
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$userName", ticket.getReq_by());
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$shortUrl", "" + club.getShortURL());
					paramsMap.put("\\$email", "" + ticket.getReq_email());
					paramsMap.put("\\$clubUrl", clubUrl);
					paramsMap.put("\\$extraText", ticket.getTitle());
					
					String message = Notifier.getMailBody(Notifier.TICKET_SUBMITION_TO_ADMIN_TAMPLATE, paramsMap);
					String subject = "Ticket Submited to Review for " + ticket.getType();
					Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), club.getEmail(), subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}

	public static void sendTeamApprovalToAdmin(final TeamDto team, final ClubDtoLite club) {
		
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					
					int seriesId = CommonUtility.stringToInt(team.getLeague());
					LeagueLite series = club.getLeague(seriesId);
					String seriesName = series.getParentLeagueName();
					if(seriesName == null)
					{
						seriesName=series.getName();
					}
					String divisonName = "";
					
					if(series.isHasDivisions()) {
						divisonName = team.getLeagueName();
						seriesName = series.getParentLeagueName();						
						seriesName = seriesName+ " ("+divisonName+")";
					}
					
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$LeagueAdmin",club.getEmail());
					paramsMap.put("\\$teamName", ""+ team.getTeamName());
					paramsMap.put("\\$seriesName", "" + seriesName);
					if(club.getClubId() == 14202) {
						paramsMap.put("\\$OmanTeamConditions", ""+"Please remember that approval is conditional on the submission of the player consent form. After getting approval, the Player will appear in your Team Info Page.");
					}else {
						paramsMap.put("\\$OmanTeamConditions", ""+" ");
					}
					
						String message = Notifier.getMailBody(Notifier.TEAM_APPROVAL_ADMIN_TEMPLATE, paramsMap);
						String subject = "Team Registration waiting for Admin Approval ";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), club.getEmail()+","+team.getCaptainEmail(), subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}
	
public static void sendTeamCaptainApprovedEmail(final TeamDto team, final ClubDtoLite club) {
		
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					
					
					
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					
					int seriesId = CommonUtility.stringToInt(team.getLeague());
					LeagueLite series = club.getLeague(seriesId);
					String seriesName = series.getParentLeagueName();
					if(seriesName == null)
					{
						seriesName=series.getName ();
					}
					String divisonName = "";
					
					if(series.isHasDivisions()) {
						divisonName = team.getLeagueName();
						seriesName = series.getParentLeagueName();						
						seriesName = seriesName+ " ("+divisonName+")";
					}
					
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$teamAdmin",""+ team.getCaptainName());
					paramsMap.put("\\$teamName", ""+team.getTeamName());
					paramsMap.put("\\$seriesName", "" + seriesName);
					if (club.getClubId() == 14202) {

						int playerExists = PlayerFactory.getPlayerAddtionalyEmailForOman(club.getEmail());

						if (playerExists <= 0 && team.getCaptain()<=0) {
							Pair userPair = UserFactory.getUserEmailPasswordByTeamAdmin(team.getTeamID());
							if (userPair != null) {
								paramsMap.put("\\$userName", userPair.getValues1());
								paramsMap.put("\\$password", CommonUtility.decrypt(userPair.getValues2()));
							}
						} else {
							paramsMap.put("\\$userName", "Use Existing Account UserName");
							paramsMap.put("\\$password", "Use Existing Account Password");
						}
						
					}
					paramsMap.put("\\$leagueAdminEmail", ""+club.getEmail());
//					paramsMap.put("\\$clubId", "" + club.getClubId());
//					paramsMap.put("\\$captainName", "" + team.getCaptainName());
//					paramsMap.put("\\$captainEmail", "" + team.getCaptainEmail());
//					paramsMap.put("\\$clubUrl", clubUrl);
//					
						String message = Notifier.getMailBody(Notifier.TEAM_APPROVED_CAPTAIN_TEMPLATE, paramsMap);
						String subject = "CricClubs - Your Team Approved ";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), team.getCaptainEmail(), subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}

	public static void playerRegistrationRequestReject(final PlayerDto player, final TeamDto team, 
			final ClubDtoLite club, String rejectReason,TeamDetailsDto teamDetails) {
		
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
	
					Map<String, String> paramsMap = new HashMap<String, String>();	
					paramsMap.put("\\$playerName", player.getFullName());
					paramsMap.put("\\$teamName", "" + team.getTeamName());
					paramsMap.put("\\$Reasons", "" + rejectReason);
	
					String message = Notifier.getMailBody(Notifier.PLAYER_REGISTRATION_REQUEST_REJECT, paramsMap);
					String subject = "CricClubs - Player Registration Request Rejected ";
					String toEmails = "";
					if(!CommonUtility.isNullOrEmpty(player.getEmail())) {
						toEmails = player.getEmail();
					}
					if (team != null && teamDetails!=null && !CommonUtility.isNullOrEmpty(team.getCaptainEmail()) 
							&& !CommonUtility.isNullOrEmpty(teamDetails.getAdminEmail())) {
						
						toEmails +=  ","+team.getCaptainEmail() +"," +teamDetails.getAdminEmail();
					}
					Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), toEmails, subject, message, club.getEmail());
	
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}



	public static void sendPreTeamApprovedEmail(final String teamName, String email, String name, final ClubDtoLite club) {
	
	executor.submit(new Runnable() {

		@Override
		public void run() {
			try {
				 
				String clubUrl="www.cricclubs.com";
				String rootUrl = EnviromentProperties.getEnvProperty("cricclubs.www.root.url");
				Map<String, String> paramsMap = new HashMap<String, String>();
				if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
				{
					clubUrl=club.getCustomDomain();
					 
				}
				paramsMap.put("\\$clubName", club.getName());
				paramsMap.put("\\$teamName", teamName);
				paramsMap.put("\\$clubId", "" + club.getClubId());
				paramsMap.put("\\$captainName", "" + name);
				paramsMap.put("\\$captainEmail", "" + email);
				paramsMap.put("\\$rootUrl", "" + rootUrl);
				paramsMap.put("\\$clubUrl", clubUrl);
				
					String message = Notifier.getMailBody(Notifier.PRE_TEAM_APPROVED_TEMPLATE, paramsMap);
					String subject = "CricClubs - Your Request to Register Team Approved ";
					Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), email, subject, message,club.getEmail());
				} catch (Exception e) {
					handleException(e);
				}
		}
	});
}
	
	public static void sendUmpireApprovalToAdmin(final UmpireDto player, final ClubDtoLite club, final String userType) {
		
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", player.getFullName());
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					paramsMap.put("\\$clubUrl", clubUrl);
					
						String message = Notifier.getMailBody(
								Notifier.UMPIRE_APPROVAL_ADMIN_TEMPLATE, paramsMap);
						String subject = userType + " Registration waiting for your Approval ";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), club.getEmail(),
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}

	public static void sendClaimApprovalToPlayer(final UserDto user, final ClubDtoLite club) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", user.getFname());
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$userName", user.getUserName());
					paramsMap.put("\\$password", CommonUtility.decrypt(user.getPassword()));
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					paramsMap.put("\\$clubUrl", clubUrl);
					
						String message = Notifier.getMailBody(
								Notifier.CLAIM_APPROVED_PLAYER_TEMPLATE, paramsMap);
						String subject = "CricClubs - Your Profile claim Approved";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), user.getEmail(),
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}
	
	public static void sendPlayerApprovalToPlayer(final PlayerDto user, final ClubDtoLite club, final TeamDto team, 
			final TeamDetailsDto teamDetails, ClubDto internalClub) {
		
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {

					String clubUrl = "www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();

					if (!CommonUtility.isNullOrEmpty(club.getCustomDomain())) {
						clubUrl = club.getCustomDomain();
					}

					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", user.getFirstName());
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					paramsMap.put("\\$clubUrl", clubUrl);

					String toEmails = "";

					if (!CommonUtility.isNullOrEmpty(user.getEmail())) {
						toEmails = user.getEmail() + "," + club.getEmail();
					}
					if (team != null && teamDetails != null && !CommonUtility.isNullOrEmpty(team.getCaptainEmail())
							&& !CommonUtility.isNullOrEmpty(teamDetails.getAdminEmail())) {
						toEmails += "," + team.getCaptainEmail() + "," + teamDetails.getAdminEmail();
					}
					if (internalClub != null && !CommonUtility.isNullOrEmpty(internalClub.getEmail())) {

						toEmails += "," + internalClub.getEmail();
					}
					String message = Notifier.getMailBody(Notifier.PLAYER_APPROVED_PLAYER_TEMPLATE, paramsMap);
					String subject = "CricClubs - Your Player Registration Approved";

					Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), toEmails, subject, message, club.getEmail());
					
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}
	
	public static void sendPlayerApprovalByClubAdmin(final PlayerDto user, final ClubDtoLite club,
			final TeamDto team,final TeamDetailsDto teamDetails, ClubDto internalClub) {
		
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {

					Map<String, String> paramsMap = new HashMap<String, String>();

					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", user.getFirstName());
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));

					String toEmails = "";
					if (!CommonUtility.isNullOrEmpty(user.getEmail())) {
						toEmails = user.getEmail() + "," + club.getEmail();
					}
					if (team != null && teamDetails != null && !CommonUtility.isNullOrEmpty(team.getCaptainEmail())
							&& !CommonUtility.isNullOrEmpty(teamDetails.getAdminEmail())) {

						toEmails += "," + team.getCaptainEmail() + "," + teamDetails.getAdminEmail();
					}
					if (internalClub != null && !CommonUtility.isNullOrEmpty(internalClub.getEmail())) {

						toEmails += "," + internalClub.getEmail();
					}
					String message = Notifier.getMailBody(Notifier.PLAYER_APPROVED_BY_CLUB_ADMIN_TEMPLATE, paramsMap);
					String subject = "CricClubs - Your Player Registration Approved By Club Admin";
					
					Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), toEmails, subject, message, club.getEmail());

				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}
	
	public static void sendUserTicketStatusChange(final Ticket ticket, final ClubDtoLite club) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$adminName", ticket.getAct_by());
					paramsMap.put("\\$userName", ticket.getReq_by());
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$ticketStatus", "" + ticket.getStatusString());
					paramsMap.put("\\$clubUrl", clubUrl);
					//paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
			
						String message = Notifier.getMailBody(
								Notifier.TICKET_STATUS_CAHNGE_TAMPLATE, paramsMap);
						String subject = "CricClubs - Your Ticket for " + ticket.getType() + " is " + ticket.getStatusString();
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), ticket.getReq_email(),
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}
	
	public static void sendUmpireApprovalToUmpire(final UmpireDto user, final ClubDtoLite club,final String userType) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", user.getFirstName());
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					paramsMap.put("\\$userType", "" + userType);
					paramsMap.put("\\$clubUrl", clubUrl);
					
						String message = Notifier.getMailBody(
								Notifier.UMPIRE_APPROVED_UMPIRE_TEMPLATE, paramsMap);
						String subject = "CricClubs - Your "+userType+" Registration Approved";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), user.getEmail(),
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}

	public static void sendClaimRejectionToPlayer(final String playerName,final String email, final ClubDtoLite club) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", playerName);
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					paramsMap.put("\\$clubUrl", clubUrl);
					
						String message = Notifier.getMailBody(
								Notifier.CLAIM_REJECTED_PLAYER_TEMPLATE, paramsMap);
						String subject = "CricClubs - Your Profile claim Rejected";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), email,
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}
	public static void sendNewMessageEmail(MessageDto messageDto, ClubDtoLite club) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		 
		String clubUrl="www.cricclubs.com";
		String trimmedMessage = messageDto.getMessage();
		
		if (!CommonUtility.isNullOrEmpty(trimmedMessage)
				& trimmedMessage.length() > 20) {
			trimmedMessage = trimmedMessage.substring(0, 20) + "...";
		}
		
		if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
		{
			clubUrl=club.getCustomDomain();
			 
		}

		paramsMap.put("\\$clubName", club.getName());
		paramsMap.put("\\$playerName", club.getName());
		paramsMap.put("\\$userName", messageDto.getName());
		paramsMap.put("\\$clubId", "" + club.getClubId());
		paramsMap.put("\\$subject", messageDto.getSubject());
		paramsMap.put("\\$message", trimmedMessage);
		paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
		paramsMap.put("\\$clubUrl", clubUrl);
		
		try {
			String message = Notifier.getMailBody(
					Notifier.NEW_MESSAGE_TEMPLATE, paramsMap);
			String subject = "You got a new Message at " + club.getName();
			Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), club.getEmail(),
					subject, message,club.getEmail());
		} catch (Exception e) {
			handleException(e);
		}

	}
	
	public static void sendNewMessageAcademyEmail(MessageDto messageDto, ClubDtoLite club) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		 
		String clubUrl="www.cricclubs.com";
		String trimmedMessage = messageDto.getMessage();
		if (!CommonUtility.isNullOrEmpty(trimmedMessage)
				& trimmedMessage.length() > 20) {
			trimmedMessage = trimmedMessage.substring(0, 20) + "...";
		}
		
		if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
		{
			clubUrl=club.getCustomDomain();
			 
		}

		paramsMap.put("\\$clubName", club.getName());
		paramsMap.put("\\$playerName", club.getName());
		paramsMap.put("\\$userName", messageDto.getName());
		paramsMap.put("\\$clubId", "" + club.getClubId());
		paramsMap.put("\\$subject", messageDto.getSubject());
		paramsMap.put("\\$message", trimmedMessage);
		paramsMap.put("\\$clubUrl", clubUrl);
		
		String academyUrl = EnviromentProperties.getEnvProperty("cricclubs.academy.root.url");
		academyUrl = academyUrl+club.getShortURL();					
		paramsMap.put("\\$academyURL", academyUrl+"/home");	
		
		paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
		
		try {
			String message = Notifier.getMailBody(
					Notifier.NEW_MESSAGE_ACADEMY_TEMPLATE, paramsMap);
			String subject = "You got a new Message at " + club.getName();
			Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), club.getEmail(),
					subject, message,club.getEmail());
		} catch (Exception e) {
			handleException(e);
		}

	}

	public static void sendNewMessageEmail(MessageDto messageDto) {
		Map<String, String> paramsMap = new HashMap<String, String>();

//		String trimmedMessage = messageDto.getMessage();

		paramsMap.put("\\$clubName", "Index");
		paramsMap.put("\\$playerName", "Admin");
		paramsMap.put("\\$userName", "User Name:" +messageDto.getName()+", email: " + messageDto.getEmail());
		paramsMap.put("\\$clubId", "" + 1);
		paramsMap.put("\\$subject", messageDto.getSubject());
		paramsMap.put("\\$message", messageDto.getMessage());

		try {
			String message = Notifier.getMailBody(
					Notifier.NEW_MESSAGE_TEMPLATE, paramsMap);
			String subject = "You got a new Message at " + "Index";
			Notifier.sendEmail(Notifier.NOTIFY_FROM_EMAIL,
					"cricclubs@gmail.com", subject, message,"");
			Notifier.sendEmail(Notifier.NOTIFY_FROM_EMAIL,
					"support@cricclubs.com", subject, message,"");
		} catch (Exception e) {
			handleException(e);
		}
	}

	public static void sendNewsUpdateEmail(final ClubDtoLite club) {

		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {					
					List<UserDto> users;
					UserDto user = new UserDto();
					users = UserFactory.getUsersByNotification("News",club.getClubId());
					 
					String clubUrl="www.cricclubs.com";
					
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain())){
						clubUrl=club.getCustomDomain();
						if(!clubUrl.contains("www.")) {
							clubUrl = "www."+clubUrl;
						}
					}
					Collection<?> distinctUsers = Notifier.getDistictUsers(users);
					for (Iterator<?> itr = distinctUsers.iterator(); itr.hasNext();) {
						user = (UserDto) itr.next();
						Map<String, String> paramsMap = new HashMap<String, String>();
						paramsMap.put("\\$clubName", club.getName());
						paramsMap.put("\\$playerName", user.getFname());
						paramsMap.put("\\$clubId", "" + club.getClubId());
						paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
						paramsMap.put("\\$clubUrl", clubUrl);
						
						String message = Notifier.getMailBody(
								Notifier.NEWS_NOTIFICATION_TEMPLATE, paramsMap);
						String subject = "News posted at " + club.getName();

						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()),
								user.getEmail(), subject, message,"");
					}

				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	public static void handleException(Exception e) {
		try {
			Notifier.sendEmail("admin@cricclubs.com", "support@cricclubs.com",
					"Exception - Inform to Dev team.",
					"Mail Error:" + e.getMessage() + "\n" + e.getStackTrace()[0],"");
		} catch (Exception e1) {
				e1.printStackTrace();
		}
	}
	public static void sendNewArticleEmail(final ClubDtoLite club,
			final ArticleDto dto) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					List<UserDto> users;
					UserDto user = new UserDto();
					users = UserFactory.getUsersByNotification("Article",club.getClubId());
					
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					Collection<?> distinctUsers = Notifier.getDistictUsers(users);

					for (Iterator<?> itr = distinctUsers.iterator(); itr.hasNext();) {
						user = (UserDto) itr.next();
						Map<String, String> paramsMap = new HashMap<String, String>();
						paramsMap.put("\\$clubName", club.getName());
						paramsMap.put("\\$playerName", user.getFname());
						paramsMap.put("\\$clubId", "" + club.getClubId());
						paramsMap.put("\\$title", "" + dto.getTitle());
						paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
						paramsMap.put("\\$clubUrl", clubUrl);
						
						String message = Notifier.getMailBody(
								Notifier.Article_NOTIFICATION_TEMPLATE,
								paramsMap);
						String subject = "New Article posted at "
								+ club.getName();
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()),
								user.getEmail(), subject, message,club.getEmail());
					}
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	public static void sendNewDocumentEmail(final ClubDtoLite club,
			final DocumentDto dto) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					List<UserDto> users;
					UserDto user = new UserDto();
					users = UserFactory.getUsersByNotification("Documents",club.getClubId());
					 
					String clubUrl="www.cricclubs.com";
					
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}

					Collection<?> distinctUsers = Notifier.getDistictUsers(users);

					for (Iterator<?> itr = distinctUsers.iterator(); itr.hasNext();) {
						user = (UserDto) itr.next();
						Map<String, String> paramsMap = new HashMap<String, String>();
						paramsMap.put("\\$clubName", club.getName());
						paramsMap.put("\\$playerName", user.getFname());
						paramsMap.put("\\$clubId", "" + club.getClubId());
						paramsMap.put("\\$name", "" + dto.getName());
						paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
						paramsMap.put("\\$clubUrl", clubUrl);
						
						String message = Notifier.getMailBody(
								Notifier.DOCUMENT_NOTIFICATION_TEMPLATE,
								paramsMap);
						String subject = "New Document uploaded at "
								+ club.getName();
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()),
								user.getEmail(), subject, message,club.getEmail());
					}
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}
	public static void sendAlbumCreationEmail(final ClubDtoLite club,
			final AlbumDto dto) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					List<UserDto> users;
					UserDto user = new UserDto();
					users = UserFactory.getUsersByNotification("Album", club.getClubId());
					 
					String clubUrl="www.cricclubs.com";

					Collection<?> distinctUsers = Notifier.getDistictUsers(users);
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					

					for (Iterator<?> itr = distinctUsers.iterator(); itr.hasNext();) {
						user = (UserDto) itr.next();
						Map<String, String> paramsMap = new HashMap<String, String>();
						paramsMap.put("\\$clubName", club.getName());
						paramsMap.put("\\$playerName", user.getFname());
						paramsMap.put("\\$clubId", "" + club.getClubId());
						paramsMap.put("\\$name", "" + dto.getName());
						paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
						paramsMap.put("\\$clubUrl", clubUrl);
						

						String message = Notifier.getMailBody(Notifier.ALBUM_NOTIFICATION_TEMPLATE, paramsMap);
						String subject = "New Photo Album created at " + club.getName();
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), user.getEmail(), subject, message,
								club.getEmail());
					}
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}
	public static void sendCommentPostedEmail(final ClubDtoLite club,
			final CommentDto dto) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					List<UserDto> users;
					UserDto user = new UserDto();
					users = UserFactory.getUsersByNotification("Comments",club.getClubId());
					 
					String clubUrl="www.cricclubs.com";
					
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}

					Collection<?> distinctUsers = Notifier.getDistictUsers(users);

					for (Iterator<?> itr = distinctUsers.iterator(); itr.hasNext();) {
						user = (UserDto) itr.next();
						Map<String, String> paramsMap = new HashMap<String, String>();
						paramsMap.put("\\$clubName", club.getName());
						paramsMap.put("\\$playerName", user.getFname());
						paramsMap.put("\\$clubId", "" + club.getClubId());
						paramsMap.put("\\$name", "" + dto.getUserName());
						paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
						paramsMap.put("\\$clubUrl", clubUrl);
						
						String message = Notifier.getMailBody(
								Notifier.COMMENT_NOTIFICATION_TEMPLATE,
								paramsMap);
						String subject = "A Comment posted at "
								+ club.getName();
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()),
								user.getEmail(), subject, message,club.getEmail());
					}
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	public static void sendScorecardUpdatedNotification(final MatchDto match,
			final ClubDtoLite club) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					List<UserDto> users;
					UserDto user = new UserDto();
					
					users = UserFactory.getUsersByNotification("ScoreCard",club.getClubId());
					List<UserDto> users2 = UserFactory.getUsersWhoPlayedMatch(
							match.getMatchID(), club.getClubId());
					users.addAll(users2);
					Collection<?> distinctUsers = Notifier.getDistictUsers(users);

					for (Iterator<?> itr = distinctUsers.iterator(); itr.hasNext();) {
						String templateName = Notifier.SCORECARD_NOTIFICATION_TEMPLATE;
						 
						String clubUrl="www.cricclubs.com";
						String subject = "Scorecard Updated at  "+ club.getName();
						Map<String, String> paramsMap = new HashMap<String, String>();
						user = (UserDto) itr.next();
						if(user.getPlayerID() == match.getTeamOneCaptain() && (club.getClubId() == 1809 || club.getClubId() == 6265)) {
							templateName = Notifier.SCORECARD_CAPTAIN_NOTIFICATION_TEMPLATE;
							subject = "Update Captain Match Report";
							paramsMap.put("\\$uid", user.getUserID()+"");							
						}
						if(user.getPlayerID() == match.getTeamTwoCaptain() && (club.getClubId() == 1809 || club.getClubId() == 6265)) {
							templateName = Notifier.SCORECARD_CAPTAIN_NOTIFICATION_TEMPLATE;
							subject = "Update Captain Match Report for "+ club.getName();
							paramsMap.put("\\$uid", user.getUserID()+"");
						}					
						if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
						{
							clubUrl=club.getCustomDomain();
							 
						}
						paramsMap.put("\\$clubName", club.getName());
						paramsMap.put("\\$playerName", user.getFname());
						paramsMap.put("\\$clubId", "" + club.getClubId());
						paramsMap.put("\\$team1", "" + match.getTeamOneName());
						paramsMap.put("\\$team2", "" + match.getTeamTwoName());
						paramsMap.put("\\$date", "" + match.getMatchDate());
						paramsMap.put("\\$matchId", "" + match.getMatchID());
						paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
						paramsMap.put("\\$clubUrl", clubUrl);
						
						
						String message = Notifier.getMailBody(templateName, paramsMap);
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()),
								user.getEmail(), subject, message,club.getEmail());
					}
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	public static void sendClubActivationEmail(ClubDtoLite club, String serverName) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		 
		String clubUrl="www.cricclubs.com";
		if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
		{
			clubUrl=club.getCustomDomain();
			 
		}
		serverName = serverName.replaceAll("api.", "");
		paramsMap.put("\\$clubName", club.getName());
		paramsMap.put("\\$activationURL", CommonUtility.getClubBasedURLForEmail("activateLeague.do", club,serverName));
		paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
		paramsMap.put("\\$clubUrl", clubUrl);
	
		try {
			String message = Notifier.getMailBody(Notifier.CLUB_ACTIVATION_LINK_NOTIFICATION_TEMPLATE, paramsMap);
			String subject = "CricClubs - " + club.getName() +" Activation Link.";
			Notifier.sendEmail(Notifier.NOTIFY_FROM_EMAIL, club.getEmail()+",support@cricclubs.com", subject, message,club.getEmail());
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	public static void sendManualNotification(final List<String> emailList,
			final String subject, final String message, final String replyEmail) {
		sendManualNotification(emailList, subject, message, replyEmail,null,null);
	}
	public static void sendManualNotification(final List<String> emailList,
			final String subject, final String message, final String replyEmail,final String fromEmail, final String fromDescription) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					String from = Notifier.NOTIFY_FROM_EMAIL;
					if(!CommonUtility.isNullOrEmpty(fromEmail)){
						from = fromEmail;
					}
					for (String email:emailList) {
						Notifier.sendEmail(from,
								email, subject, message,replyEmail,fromDescription);
					}
				} catch (Exception e) {
					handleException(e);
				}

			}
		});
	}

	public static void sendWelcomeAdminEmail(UserDto user, ClubDtoLite club, String serverName) {
		try {
			serverName = serverName.replaceAll("api.", "");
			 
			String clubUrl="www.cricclubs.com";
			Map<String, String> paramsMap = new HashMap<String, String>();
			
			if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
			{
				clubUrl=club.getCustomDomain();
				 
			}
			paramsMap.put("\\$clubName", club.getName());
			paramsMap.put("\\$userName", user.getUserName());
			paramsMap.put("\\$clubUrl", clubUrl);
			
			String password = CommonUtility.decrypt(user.getPassword());
			
			paramsMap.put("\\$password", password);

			paramsMap.put("\\$clubURL", "<a href='http://"+serverName+"/"+club.getShortURL()+"'>"+serverName+"/"+club.getShortURL()+"</a>");
			paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
			
			
			String message = Notifier.getMailBody(
					Notifier.ADMIN_WELCOME_TEMPLATE, paramsMap);
			String subject = "Welcome to CricClubs - " + club.getName();
			
			Notifier.sendEmail(Notifier.NOTIFY_FROM_EMAIL, club.getEmail()+",support@cricclubs.com",
					subject, message,club.getEmail());
			
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	public static void sendWelcomeAcademyAdminEmail(UserDto user, ClubDtoLite club, String serverName) {
		try {
			serverName = serverName.replaceAll("api.", "");
			
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("\\$clubName", club.getName());
			paramsMap.put("\\$userName", user.getUserName());
			
			String password = CommonUtility.decrypt(user.getPassword());
			
			String academyUrl = EnviromentProperties.getEnvProperty("cricclubs.academy.root.url");						 
			academyUrl = academyUrl + club.getShortURL() + "/home";
			
			paramsMap.put("\\$password", password);
			paramsMap.put("\\$academyURL", academyUrl);
			paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
			
			String message = Notifier.getMailBody(
					Notifier.ACADEMY_ADMIN_WELCOME_TEMPLATE, paramsMap);
			String subject = "Welcome to CricClubs - " + club.getName();
			Notifier.sendEmail(Notifier.NOTIFY_FROM_EMAIL, club.getEmail()+",support@cricclubs.com",
					subject, message,club.getEmail());
		} catch (Exception e) {
			handleException(e);
		}
	}

	public static void sendHotstarCricketChampionMail(String mailTo, ClubDto club, String emailId, String promoCode, String serverName) {
		try {
			
			Map<String, String> paramsMap = new HashMap<String, String>();
			String subject = "";
			
			if(club!=null) {
				serverName = serverName.replaceAll("api.", "");
				paramsMap.put("\\$reference", "http://"+serverName+"/hsplsubs.do");
				subject = "Hotstar Cricket Champion - " + club.getName();
			}else {
				paramsMap.put("\\$reference", "https://cricclubs.com/hsplsubs.do"); 
				subject = "Hotstar Cricket Champion ";
			}
			
			paramsMap.put("\\$promoCode", promoCode);
			
			if(mailTo.equalsIgnoreCase("LEAGUE")) {
				String message = Notifier.getMailBody(
						Notifier.HOTSTAR_CRICKET_CHAMPION_LEAGUE, paramsMap);
							
				Notifier.sendEmail(Notifier.NOTIFY_FROM_EMAIL, emailId,
						subject, message,club.getEmail());
			}else if(mailTo.equalsIgnoreCase("TEAM")) {
				String message = Notifier.getMailBody(
						Notifier.HOTSTAR_CRICKET_CHAMPION_TEAM, paramsMap);
				Notifier.sendEmail(Notifier.NOTIFY_FROM_EMAIL, emailId,
						subject, message,club.getEmail());
			}

		} catch (Exception e) {
			log.error("--- Hotstar email Exception : "+e.getMessage());
		}
	}

	public static void sendVerificationCode(String verificationCode, String email) {
		try {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("\\$verificationCode", verificationCode);

			String message = Notifier.getMailBody(
					Notifier.VERIFICATION_CODE_TEMPLATE, paramsMap);
			String subject = "CricClubs - Verification Code";
			Notifier.sendEmail(Notifier.NOTIFY_FROM_EMAIL, email,
					subject, message,"");
		} catch (Exception e) {
			handleException(e);
		}		
	}
	
	public static void sendVerificationCode(String verificationCode, String email, int clubId) {
		try {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("\\$verificationCode", verificationCode);

			String message = Notifier.getMailBody(
					Notifier.VERIFICATION_CODE_TEMPLATE, paramsMap);
			String subject = "CricClubs - Verification Code";
			Notifier.sendEmail(Notifier.getNotifyEmail(clubId), email,
					subject, message,"");
		} catch (Exception e) {
			handleException(e);
		}		
	}

	public static void sendHotstarReferralEmail(String email2,String name,String email,String encryptMail) {
		try {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("\\$email2",email2);
			paramsMap.put("\\$name", name);
			paramsMap.put("\\$email", email);
			paramsMap.put("\\$encryptMail", encryptMail);
			
			String message = Notifier.getMailBody(
					Notifier.HOTSTAR_REFERRAL_TEMPLATE, paramsMap);
			String subject = "🔥 Hotstar 40% OFF + FREE Custom Team Uniform offer from CricClubs";
			Notifier.sendEmail(Notifier.NOTIFY_FROM_EMAIL, email2,
					subject, message,"");
		} catch (Exception e) {
			handleException(e);
		}		
	}
	

	public static void sendMailToAdmin(String playerFullName,String programName,String playerEmailId, ClubDto club) {
		// TODO Auto-generated method stub
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}					
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$playerName", playerFullName);
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$email", "" + playerEmailId);
					paramsMap.put("\\$programName", "" + programName);			 
					paramsMap.put("\\$clubUrl", clubUrl);
					String message = Notifier.getMailBody(Notifier.SEND_EMAIL_TO_ADMIN, paramsMap);
					String subject = "A new player just registered to your academy!";
					Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()),club.getEmail(),subject,
							message, club.getEmail());
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}
	
	public static void sendTeamRegistrationEmailNCF(String fromEmail, String toEmails, String seriesName,
			String leagueName, String internalClubName, String paymentAmount, String teamName, 
			String paymentDate ,String replyMail) {		
				try {					 
					Map<String, String> paramsMap = new HashMap<String, String>();
					
					paramsMap.put("\\$SeriesName", seriesName);
					paramsMap.put("\\$LeagueName", leagueName);
					paramsMap.put("\\$PaymentAmount", paymentAmount);
					paramsMap.put("\\$InternalClubName", internalClubName);
					paramsMap.put("\\$TeamName", teamName);			 
					paramsMap.put("\\$PaymentDate", paymentDate);
					paramsMap.put("\\$ReplyEmail", replyMail);
					
					String message = Notifier.getMailBody(Notifier.TEAM_REGISTRATION_NCF, paramsMap);					
					String subject = "Team Registration";
					
					Notifier.sendEmail(fromEmail, toEmails, subject, message, fromEmail);
					
				} catch (Exception e) {
					handleException(e);
				}
	}
	
	public static void sendStreamingPaymentEmail(UserDto user, float amount, String paymentDate, 
			String paymentStatus, String currencyCode) throws Exception {
		
		Map<String, String> paramsMap = new HashMap<String, String>();	
		
		String toEmails = user.getEmail()+",support@cricclubs.com,ganesh@cricclubs.com,pavan@cricclubs.com,sales@cricclubs.com";
		
		paramsMap.put("\\$userName", user.getFullName());	
		
		paramsMap.put("\\$paymentDate", paymentDate);	
		paramsMap.put("\\$paymentAmount", amount+"("+currencyCode+")");	
		paramsMap.put("\\$streamingBalance", user.getNoOfMatchesStreaming()+"");	
		paramsMap.put("\\$paymentStatus", paymentStatus);			
		String message =  Notifier.getMailBody(Notifier.STREAMING_PAYMENT_EMAIL, paramsMap);
		String subject = "Cricclubs - Streaming Order Details";
		
		Notifier.sendEmail("notify@cricclubs.com",toEmails,subject,message,"");
	}

	public static void sendFixtureAssignmentToUmpire(final UserDto user,String team1,String team2,String matchDate,String groundName, 
			String leagueName,final ClubDtoLite club, final String userType, String time) throws Exception{
		
		//sendFixtureAssignmentToUmpire(umpireDto, teamDto1.getTeamName(),teamDto2.getTeamName(),matchDate,groundDto.getName(),leagueDto.getName(),CommonUtility.getUmpireCoachScorerString(umpireDto.getType()));
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();

					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$leagueName", club.getName());
					paramsMap.put("\\$umpireName", user.getFname());
					paramsMap.put("\\$team1", team1);
					paramsMap.put("\\$team2", team2);
					paramsMap.put("\\$matchDate", matchDate);
					paramsMap.put("\\$groundName", groundName);
					paramsMap.put("\\$clubId", "" + club.getClubId());
					paramsMap.put("\\$adContent", "" + getEmailAdContent(club));
					paramsMap.put("\\$userType", "" + userType);
					paramsMap.put("\\$time", time);		 
					paramsMap.put("\\$clubUrl", clubUrl);
			
						String message = Notifier.getMailBody(
								Notifier.FIXTURE_ASSIGNMENT_TO_UMPIRE_TEMPLATE, paramsMap);
						String subject = "Fixture Assignment";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), user.getEmail(),
								subject, message,club.getEmail());
					} catch (Exception e) {
						handleException(e);
					}
			}
		});
	}

	public static void sendBulkFixtureAssignmentToUmpire(List<FixtureDto> emailFixtures, List<FixtureDto> emailScorerFixtures, String umpireName, ClubDtoLite club,String email,String leagueName) {
		
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$umpireName", umpireName);
					paramsMap.put("\\$clubUrl", clubUrl);
					StringBuffer fixtures = new StringBuffer();
					
					
						for (FixtureDto fixture : emailFixtures) {
							fixtures.append("<tr>");
							fixtures.append("<td>" + leagueName + "</td>");
							fixtures.append("<td>" + fixture.getTeamOneName() + "</td>");
							fixtures.append("<td>" + fixture.getTeamTwoName() + "</td>");
							fixtures.append("<td>" + fixture.getDate() + "</td>");
							fixtures.append("<td>" + fixture.getTime() + "</td>");
							fixtures.append("<td>" + fixture.getLocation() + "</td>");
							fixtures.append("<td>Umpire</td>");
							fixtures.append("</tr>");
						}
					
					
						for (FixtureDto fixture : emailScorerFixtures) {
							fixtures.append("<tr>");
							fixtures.append("<td>" + leagueName + "</td>");
							fixtures.append("<td>" + fixture.getTeamOneName() + "</td>");
							fixtures.append("<td>" + fixture.getTeamTwoName() + "</td>");
							fixtures.append("<td>" + fixture.getDate() + "</td>");
							fixtures.append("<td>" + fixture.getTime() + "</td>");
							fixtures.append("<td>" + fixture.getLocation() + "</td>");
							fixtures.append("<td>Scorer</td>");
							fixtures.append("</tr>");
						}
					
					paramsMap.put("\\$tableRows", fixtures.toString());
						
						String message = Notifier.getMailBody(
								Notifier.BULK_FIXTURE_ASSIGNMENT_TO_UMPIRE_TEMPLATE, paramsMap);
						String subject = "Fixture Assignments";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), email,
								subject, message,club.getEmail());
						
					} catch (Exception e) {
						log.error("Error Sending Email:", e);
						e.printStackTrace();
						handleException(e);
					}
			}
		});
	}
	
	
	
	public static void sendFixturesEmails(List<FixtureDto> updatedFixtures,Map<String,String> emailsMap, ClubDtoLite club,String leagueName) {
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					
					 
					String clubUrl="www.cricclubs.com";
					Map<String, String> paramsMap = new HashMap<String, String>();
					if(!CommonUtility.isNullOrEmpty(club.getCustomDomain()))
					{
						clubUrl=club.getCustomDomain();
						 
					}
					paramsMap.put("\\$clubName", club.getName());
					paramsMap.put("\\$leagueName", leagueName);
					paramsMap.put("\\$leagueAdminEmail", club.getEmail());
					paramsMap.put("\\$clubUrl", clubUrl);
					
					StringBuffer fixtures = new StringBuffer();
					
					for(FixtureDto fixture:updatedFixtures) {

						fixtures.append("<tr>");
						fixtures.append("<td>" + leagueName + "</td>");
						if(fixture.isTeam1Updated()) {
						fixtures.append("<td bgcolor= #ffff00>" + fixture.getTeamOneName() + "</td>");
						}else {
							fixtures.append("<td>" + fixture.getTeamOneName() + "</td>");
						}
						if(fixture.isTeam2Updated()) {
							fixtures.append("<td bgcolor= #ffff00>" + fixture.getTeamTwoName() + "</td>");
							}else {
								fixtures.append("<td>" + fixture.getTeamTwoName() + "</td>");
							}
						if(fixture.isDateUpdated()) {
							fixtures.append("<td bgcolor= #ffff00>" + (!CommonUtility.isNullOrEmptyOrNULL(fixture.getDate())?fixture.getDate():"TBD")+ "</td>");
							}else {
								fixtures.append("<td>" + (!CommonUtility.isNullOrEmptyOrNULL(fixture.getDate())?fixture.getDate():"TBD") + "</td>");
							}
						
						if(fixture.isTimeUpdated()) {
							fixtures.append("<td bgcolor= #ffff00>" + fixture.getTime() + "</td>");
							}else {
								fixtures.append("<td>" + fixture.getTime() + "</td>");
							}
						
						if(fixture.isGroundUpdated()) {
							fixtures.append("<td bgcolor= #ffff00>" + fixture.getLocation() + "</td>");
							}else {
								fixtures.append("<td>" + fixture.getLocation() + "</td>");
							}
						
						if(fixture.isUmpire1Updated()) {
							fixtures.append("<td bgcolor= #ffff00>" + fixture.getUmpire1Name() + "</td>");
							}else {
								fixtures.append("<td>" + fixture.getUmpire1Name() + "</td>");
							}
						
						if(fixture.isUmpire2Updated()) {
							fixtures.append("<td bgcolor= #ffff00>" + fixture.getUmpire2Name() + "</td>");
							}else {
								fixtures.append("<td>" + fixture.getUmpire2Name() + "</td>");
							}
						if(fixture.isScorerUpdated()) {
							fixtures.append("<td bgcolor= #ffff00>" + fixture.getScorerName() + "</td>");
							}else {
								fixtures.append("<td>" + fixture.getScorerName() + "</td>");
							}
						
												
						fixtures.append("</tr>");
					}
					
					paramsMap.put("\\$tableRows", fixtures.toString());
			
						
						for(String email:emailsMap.keySet()) {
							paramsMap.put("\\$name", emailsMap.get(email));
							String message = Notifier.getMailBody(
									Notifier.SEND_UPDATE_FIXTURES_EMAILS, paramsMap);
							String subject = leagueName+" Fixture Announcement!";
						Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), email,
								subject, message,club.getEmail());
						}
					} catch (Exception e) {
						log.error("Error Sending Email:", e);
						e.printStackTrace();
						handleException(e);
					}
			}
		});
	}
	
	public static boolean sendPaymentSuccessEmail(UserDto user, ClubDto club, String program,String batchid, String cycle,String paymentID,String paymentDate) {
		try {
			
		String message = "Subject: "+club.getName()+" - Payment Confirmation.\r\n" + 
				"\r\n" + 
				"Hi "+user.getFullName()+",\r\n" + 
				"\r\n" + 
				"This is to inform you that your payment towards "+program+" for the period - "+cycle+" is successful. Please find the transaction details below.\r\n" + 
				"\r\n" + 
				"Name: "+user.getFullName()+" Id: "+user.getPlayerID()+" Program: "+program+" Batch: "+batchid +""
						+ "PaymentID: "+paymentID+"  PaymentDate: "+paymentDate+"";	
			
			Notifier.sendEmail(Notifier.getNotifyEmail(club.getClubId()), user.getEmail(), "Payment Details", message,
					club.getEmail());
		} catch (Exception e) {
			log.error("Error Sending Payment Email:", e);
			return false;
		}
		return true;
	}
}
