package com.cricket.mailService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dao.UserFactory;

public class ReadingEmail {
	private static Logger log = LoggerFactory.getLogger(ReadingEmail.class);
	private static final String FILE_LOCATION = "C:\\output\\output.csv";

	public static void main(String[] args) throws Exception {
		//readEmails(args);
        removeEmailsFromCricClubs();
	}

	private static void removeEmailsFromCricClubs() throws FileNotFoundException, IOException, Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(FILE_LOCATION)));
		List<String> emails = new ArrayList<String>();
		String line = br.readLine();
		while(line != null){
			line = line.trim();
			if(!emails.contains(line)){
				emails.add(line);
			}
			line = br.readLine();
		}
		br.close();
		
		int i = 1;
		for(String email:emails){
			
			UserFactory.cleanUpEmail(email);
		}
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
			Folder inbox = store.getFolder("[Gmail]/Spam");
			inbox.open(Folder.READ_ONLY);
			Message[] messages = inbox.getMessages();
			List<String> invalidEmails = new ArrayList<String>();
			for (Message message : messages) {
				Address[] in = message.getFrom();
				String from = "";
				for (Address address : in) {
					from = address.toString();
				}
				if ("MAILER-DAEMON@amazonses.com".equals(from)) {
					try {
						Multipart mp = (Multipart) message.getContent();
						BodyPart bp = mp.getBodyPart(0);
						String content = bp.getContent().toString();
						String email = content.substring(content.indexOf(":") + 3);
						invalidEmails.add(email);						
						bw.write(email);
						bw.write("\n");
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}
			bw.close();
		} catch (Exception mex) {
			mex.printStackTrace();
		}
		
	}
}