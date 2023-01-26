package com.cricket.mailService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.lang3.ArrayUtils;

public class ReadingEmailInbox {
	
	private static final String FILE_LOCATION = "C:\\output\\output.csv";

	public static void main(String[] args) throws Exception {
		readEmails(args);
       // removeEmailsFromCricClubs();
	}

	private static void readEmails(String[] args) throws IOException {
		File file = new File(FILE_LOCATION);
		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));

		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		try {
			Session session = Session.getInstance(props, null);
			Store store = session.getStore();
			store.connect("imap.gmail.com", "nganeshreddi@gmail.com", args[0]);
			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);
			Message[] messages = inbox.getMessages();
			List<String> invalidEmails = new ArrayList<String>();
			int i= 0;
			ArrayUtils.reverse(messages);
			for (Message message : messages) {
				i++;
				Address[] in = message.getFrom();
				String subject = message.getSubject();
				String from = "";
				for (Address address : in) {
					from = address.toString();
				}
				
				if(subject != null && subject.startsWith("Unsubscribe-ICC Survey Announcement")){
					if(from.contains("<")){
						from = from.substring(from.indexOf("<")+1,from.indexOf(">"));
					}
					invalidEmails.add(from);
					bw.write(from);
					bw.write("\n");
				}
				
				if(i>500){
					break;
				}
			}
			bw.close();
		} catch (Exception mex) {
			mex.printStackTrace();
		}
		
	}
}