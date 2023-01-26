package com.cricket.mailService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtilities  { 
	private static Logger log = LoggerFactory.getLogger(MailUtilities.class);
	public static void sendMessage(String emailFrom, String emailTo, String emailSubject, String emailText) {
		 
		try {
			
		   //	   set the host
			Properties props = new Properties();
			props.put("mail.smtp.host", "localhost");
			props.put("mail.smtp.port", "25");
			
		    //	   create some properties and get the default Session
			Session session = Session.getInstance(props, null);
			session.setDebug(false);
		
			try
			{
		        // create a message
				MimeMessage msg = new MimeMessage(session);
			   
			    // set the from
				InternetAddress from = new InternetAddress(emailFrom,"CricClubs Notification");
				
				msg.setFrom(from);
			
				StringTokenizer emailtoST = new StringTokenizer(emailTo, ";");
				
				List<InternetAddress> emailtoList = new ArrayList<InternetAddress>();
				while (emailtoST.hasMoreTokens()) {
					emailtoList.add(new InternetAddress(emailtoST.nextToken(),"CricClubs"));
				}
			
				InternetAddress[] addresses = new InternetAddress[emailtoList.size()];
				int idx=0;
				
				for (Iterator<InternetAddress> iter = emailtoList.iterator(); iter.hasNext(); )
					addresses[idx++] = (InternetAddress)iter.next();
				
				msg.setRecipients(javax.mail.Message.RecipientType.TO, addresses);
				msg.setSubject(emailSubject);
				InternetAddress replyTo[] = {new InternetAddress("support@cricclubs.com")};
				msg.setReplyTo(replyTo);
				
			   // send a plain text message
				msg.setContent(emailText, "text/html");
			
				Transport.send(msg);
			}
			catch(MessagingException mex) {
				mex.printStackTrace();
			}
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) {

		try {
			MailUtilities.sendMessage("nganeshreddi@gmail.com","nganeshreddi@gmail.com", "Hello", "Some body");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
} 

  
