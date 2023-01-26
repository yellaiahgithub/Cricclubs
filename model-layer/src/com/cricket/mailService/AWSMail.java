package com.cricket.mailService;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class AWSMail {

	// Supply your SMTP credentials below. Note that your SMTP credentials are
	// different from your AWS credentials.
	static final String SMTP_USERNAME = "AKIAJBPR27LO2ACNDQIA"; // Replace with
																// your SMTP
																// username.
	static final String SMTP_PASSWORD = "All2PEvPiaStNZ/XMekLRKQK4ngV5sBDBgbdBL1Z40VL"; // Replace
																						// with
																						// your
																						// SMTP
																						// password.

	// Amazon SES SMTP host name. This example uses the us-east-1 region.
	static final String HOST = "email-smtp.us-east-1.amazonaws.com";

	// Port we will connect to on the Amazon SES SMTP endpoint. We are choosing
	// port 25 because we will use
	// STARTTLS to encrypt the connection.
	static final String PORT = "587";

	private static Logger log = LoggerFactory.getLogger(AWSMail.class);
	public static void sendMessage(String emailFrom, String emailTo, String emailSubject, String emailText,String replyEmail) {
	sendMessage(emailFrom, emailTo, emailSubject, emailText, replyEmail, null);
	}
	
	public static void sendMessage(String emailFrom, String emailTo, String emailSubject, String emailText,String replyEmail,String fromEmailDescription) {
		try {
			List<String> to = new ArrayList<String>(Arrays.asList(emailTo.split(",")));
			
			String description = "CricClubs Notification";
			if(!CommonUtility.isNullOrEmpty(fromEmailDescription)){
				description = fromEmailDescription;
			}
			sendMessageNew(emailSubject, emailText, emailFrom, to, replyEmail, description);
			
			insertIntoCricclubsTable(emailSubject,emailTo,emailText);
			
		} catch (Exception e) {
			log.error("Unable to Send email" + e.getMessage());
			log.error("The email was not sent2." + emailTo);
		}
	}
	
	public static void insertIntoCricclubsTable(String subject,String emailto,String emailText) throws Exception
	{
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
			try
			{
				String query = "insert into mcc.cricclubs_email_audit (email_subject,email_to,email_date ) " + " values (?,?,NOW())";

				int index = 1;
				conn=DButility.getDefaultConnection();
				st = conn.prepareStatement(query);
				st.setString(index++, subject);
				st.setString(index++, emailto);
				st.executeUpdate();
			}catch(Exception e)
			{
				throw new Exception(e.getMessage());
			}
			finally
			{
				DButility.dbCloseAll(conn, st, rs);
			}
	}
	
	public static void sendMessageNew(String subject, String message, String from, List<String> to, String replyTo, String description) {
		 try {
			 Session s = Session.getDefaultInstance(new Properties());
		        MimeMessage mimeMessage = new MimeMessage(s);

		        // Sender and recipient
		        mimeMessage.setFrom(new InternetAddress(from, description));
		        for (String toMail : to) {
		            mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toMail));
		        }
		        
		        if(replyTo == null || "".equals(replyTo.trim())){
					replyTo = "support@cricclubs.com";
				}
				Address[] replyEmail = { new InternetAddress(replyTo) };
				mimeMessage.setReplyTo(replyEmail);
		        
		        // Subject
		        mimeMessage.setSubject(subject);

		        // Add a MIME part to the message
		        MimeMultipart mimeBodyPart = new MimeMultipart();
		        BodyPart part = new MimeBodyPart();
		        part.setContent(message, "text/html; charset=UTF-8");
		        mimeBodyPart.addBodyPart(part);

		        // Add a attachement to the message
			/*
			 * for(FileUploadDto dto : files) { part = new MimeBodyPart(); DataSource source
			 * = new ByteArrayDataSource(dto.getAttachment(), dto.getContentType());
			 * part.setDataHandler(new DataHandler(source));
			 * part.setFileName(dto.getFileName()); mimeBodyPart.addBodyPart(part); }
			 */

		        mimeMessage.setContent(mimeBodyPart);

		        // Create Raw message
		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		        mimeMessage.writeTo(outputStream);
		        RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

		        // Credentials
		        String keyID = "AKIA6ND2UKILNHDD3KET";// <your key id>
		        String secretKey = "hIW6RpSjg/1cCt78nk4n2b6iKpYslSzkTDuBl23p";// <your secret key>
		        AWSCredentials credentials = new BasicAWSCredentials(keyID, secretKey);
				AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);

		        // Send Mail
		        SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
		        rawEmailRequest.setDestinations(to);
		        rawEmailRequest.setSource(from);
		        client.sendRawEmail(rawEmailRequest);
		    } catch (Exception e) {
		    	log.error("-------------------EmailException1---------------------------- : "+String.join(",", to));
		    	e.printStackTrace();
		    }
	}
	
	
	public static void main(String[] args) {
		try {
			//AWSMail.sendMessage("notify@cricclubs.com", "nganeshreddi@gmail.com", "Hello", "Some body","vikas@cricclubs.com");
			AWSMail.sendMessage("notify@cricclubs.com", "raghuramr1984@gmail.com", "Hello", "Some body","r.raghuram4u@gmail.com");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}