/*
 * Created on Apr 7, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.mailService;

import java.security.Provider;
import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;




/*
* Created on Feb 21, 2005
*
*/


public class GoogleMail {

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "465";
	private static final String emailFromAddress = "mcricinfo@gmail.com";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    private static class mailProvider extends Provider {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        mailProvider(String name) {
            super(name, "1.0", "CC    ");
        }
    }

    public static void sendEmail(String to,String subject,String message) throws MessagingException{
   
        Security.addProvider(new mailProvider(""));
        String[] toArray = {to};
        new GoogleMail().sendSSLMessage(
        toArray,
            subject,
            message,
            emailFromAddress);

    }

	public void sendSSLMessage(
		String recipients[],
		String subject,
		String message,
		String from)
		throws MessagingException {
		boolean debug = true;

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session =
			Session.getDefaultInstance(props, new javax.mail.Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("mcricinfo@gmail.com", "oakshores");
			}
		});

		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/html");
		Transport.send(msg);
	}
}
