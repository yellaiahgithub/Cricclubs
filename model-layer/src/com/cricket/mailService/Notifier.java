package com.cricket.mailService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cricket.dto.UserDto;
import com.cricket.utility.CommonUtility;

public class Notifier {
	public static String PLAYER_WELCOME_TEMPLATE = "playerWelcome";
	public static String UMPIRE_WELCOME_TEMPLATE = "umpireWelcome";
	public static String STORES_NOTIFICATION_TEMPLATE = "storesNotificationForNewSeries";
	public static String ACADEMY_USER_WELCOME_TEMPLATE = "academyUserWelcome";
	public static String FORGOT_PASSWORD_TEMPLATE = "forgotPassword";
	public static String SEND_OTP_TO_EMAIL 		= "sendOtpToEmail";
	public static String VERIFY_FANTASY_EMAIL 	= "verifyFantasyEmail";
	public static String VERIFY_EMAIL 			= "verifyEmail";
	public static String FORGOT_PASSWORD_TEMPLATE_ACADEMY = "academyForgotPassword";
	public static String NEW_MESSAGE_TEMPLATE = "newMessage";
	public static String NEW_MESSAGE_ACADEMY_TEMPLATE = "academyNewMessage";
	public static String NEWS_NOTIFICATION_TEMPLATE = "newsNotification";
	public static String Article_NOTIFICATION_TEMPLATE = "articleNotification";
	public static String DOCUMENT_NOTIFICATION_TEMPLATE = "documentNotification";
	public static String ALBUM_NOTIFICATION_TEMPLATE = "albumNotification";
	public static String COMMENT_NOTIFICATION_TEMPLATE = "commentNotification";
	public static String SCORECARD_NOTIFICATION_TEMPLATE = "scorecardNotification";
	public static String CLAIM_APPROVAL_ADMIN_TEMPLATE = "claimApprovalToAdmin";
	public static String PLAYER_APPROVAL_ADMIN_TEMPLATE = "playerApprovalToAdmin";
	public static String TICKET_SUBMITION_TO_ADMIN_TAMPLATE = "ticketSubmisionToAdmin";
	public static String TICKET_STATUS_CAHNGE_TAMPLATE = "ticketStatusChangeToUser";
	public static String PLAYER_APPROVED_PLAYER_TEMPLATE = "playerApprovedToPlayer";
	public static String PLAYER_APPROVED_BY_CLUB_ADMIN_TEMPLATE = "playerApprovalByClubAdmin";
	
	public static String TEAM_APPROVAL_ADMIN_TEMPLATE = "teamApprovalToAdmin";
	public static String TEAM_APPROVED_CAPTAIN_TEMPLATE = "teamApprovedToCaptain";
	public static String PRE_TEAM_APPROVED_TEMPLATE = "preTeamApproveMail";
	
	public static String UMPIRE_APPROVED_UMPIRE_TEMPLATE = "umpireApprovedToUmpire";
	public static String UMPIRE_APPROVAL_ADMIN_TEMPLATE = "umpireApprovalToAdmin";
	public static String CLAIM_APPROVED_PLAYER_TEMPLATE = "claimApprovedPlayer";
	public static String CLAIM_REJECTED_PLAYER_TEMPLATE = "claimRejectedPlayer";
	public static String WEEKLY_SUMMARY_NOTIFICATION_TEMPLATE = "weeklySummaryNotification";
	public static String WEEKLY_OFFICIALS_ALLOCATION_REPORT = "WeeklyOfficialAllocationReport";
	public static String WEEKLY_UMPIRING_NOTIFICATION_TEMPLATE = "weeklyUmpiringNotification";
	public static String WEEKLY_SCORING_NOTIFICATION_TEMPLATE = "weeklyScoringNotification";
	public static String WEEKLY_PLAYING_NOTIFICATION_TEMPLATE = "weeklyPlayingNotification";
	public static String CLUB_ACTIVATION_LINK_NOTIFICATION_TEMPLATE = "clubActivationLinkNotification";
	public static String ADMIN_WELCOME_TEMPLATE = "adminWelcome";
	public static String ACADEMY_ADMIN_WELCOME_TEMPLATE = "academyAdminWelcome";
	public static final String VERIFICATION_CODE_TEMPLATE = "verificationCode";
	public static final String SEND_OTP_TO_EMAIL_FOR_USER_REGISTRATION = "sendOtpToEmailForUserRegistration";
	public static final String HOTSTAR_REFERRAL_TEMPLATE = "hotstarReferralEmail";
	public static String LED_SCORE_BOARD_DISPLAY_TEMPLATE = "ledScoreBoardDisplayTemplate";
	
	public static final String TEAM_REGISTRATION_NCF = "TeamRegistrationNCF";
	
	public static String HOTSTAR_CRICKET_CHAMPION_LEAGUE = "hotstarccLeague";	
	public static String HOTSTAR_CRICKET_CHAMPION_TEAM = "hotstarccTeam";
	
	public static String JERSEY_ORDER_EMAIL 		= "jerseyOrderEmail";

	public static final String NOTIFY_FROM_EMAIL = "notify@cricclubs.com";
	public static final String NOTIFY_FROM_EMAIL_SCA = "media@singaporecricket.org";
	
	public static String REPORT_POST_EMAIL 		= "reportPostEmail";
	
	public static String STREAMING_PAYMENT_EMAIL    = "StreamingPayment";
	
	public static final String SCORECARD_CAPTAIN_NOTIFICATION_TEMPLATE = "scorecardCaptainNotification";
	public static final String SEND_EMAIL_TO_ADMIN = "sendEmailToAdmin";
	public static final String ACADEMY_EVENT_USER_DELETE_NOTIFICATION = "academyEventUserDeleteNotification";
	public static final String ACADEMY_EVENT_USER_UPDATE_NOTIFICATION = "academyEventUserUpdateNotification";
	public static final String FIXTURE_ASSIGNMENT_TO_UMPIRE_TEMPLATE="fixtureAssignmentToUmpire";
	public static final String BULK_FIXTURE_ASSIGNMENT_TO_UMPIRE_TEMPLATE="bulkFixtureAssignmentToUmpire";
	public static final String SEND_UPDATE_FIXTURES_EMAILS="sendUpdateFixturesEmails";
	
	public static final String PLAYER_REGISTRATION_REQUEST_REJECT="playerRegistrationRequestReject";
	
	
	public static String getNotifyEmail(int clubId) {
		if(clubId == 7683) {
			return NOTIFY_FROM_EMAIL_SCA;
		}else {
			return NOTIFY_FROM_EMAIL;
		}
	}
	public static String getMailBody(String templatename, Map<String, String> parameters) throws Exception {
		return CommonUtility.applyParametersToTemplateFile("notifications",templatename+ ".html",parameters);
	}
	
	public static String getDynamicHtml(String templatename, Map<String, String> parameters) throws Exception {
		return CommonUtility.applyParametersToTemplateFile("notifications",templatename+ ".html",parameters);
	}

	public static void sendEmail(String from, String to, String title, String message,String replyEmail,String fromEmailDescription) throws Exception {
		message = message.replaceAll("<REPLACE_EMAIL_ADDRESS>",to);
		AWSMail.sendMessage(from, to, title, message,replyEmail,fromEmailDescription);
	}

	public static void sendEmail(String from, String to, String title, String message, String replyEmail)throws Exception {

		if (to.contains(",")) {
			String[] mutipletos = to.split(",");
			for(String toEmail : mutipletos) {
				sendEmail(from, toEmail, title, message, replyEmail, null);
			}
		} else {
			sendEmail(from, to, title, message, replyEmail, null);
		}
	}	
	
	
	public static Collection<UserDto> getDistictUsers(List<UserDto> users) {
		HashMap<String, UserDto> usermap = new HashMap<String, UserDto>();
		if (users != null && !users.isEmpty()) {
			for (Iterator<UserDto> itr = users.iterator(); itr.hasNext();) {
				UserDto user = (UserDto) itr.next();
				if (usermap.get(user.getEmail()) == null) {
					usermap.put(user.getEmail(), user);
				}
			}
		}
		return usermap.values();
	}
}
