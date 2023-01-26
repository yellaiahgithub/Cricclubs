package com.cricket.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class CommonSftpFileTrasfer {
	private static boolean isFTPInprogress = false;
	private static String pathTobeSent = "";
	private static Logger log = LoggerFactory.getLogger(CommonSftpFileTrasfer.class);
	private static ExecutorService executor = Executors.newFixedThreadPool(5);

	public static void sendFileToAbuDhabi(final String path, final boolean loop) {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				sendFileToSFTP(path, loop, "abudhabi2018.sftp.wpengine.com", 2222, "abudhabi2018-santosh", "bIajxWgv8CkS");
			}
		});
	}

	private static void sendFileToSFTP(String path, boolean loop, String SFTPHOST, 
			int SFTPPORT, String SFTPUSER, String SFTPPASS ) {

		if (!isFTPInprogress) {
			isFTPInprogress = true;
			try {
				long startTime = System.currentTimeMillis();
				
				/*String SFTPHOST = "35.190.210.20";
				int SFTPPORT = 2222;
				String SFTPUSER = "usacricket-cricclubs";
				String SFTPPASS = "UdoXOiMj4Wi3";*/

				Session session = null;
				Channel channel = null;
				ChannelSftp channelSftp = null;

				JSch jsch = new JSch();
				session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
				session.setPassword(SFTPPASS);
				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				session.connect();
				channel = session.openChannel("sftp");
				channel.connect();
				channelSftp = (ChannelSftp) channel;
				channelSftp.cd("cricclubs");
				File f = new File(path);
				channelSftp.put(new FileInputStream(f), f.getName());
				session.disconnect();

			} catch (Exception ex) {
				List<String> emailList = new ArrayList<String>();
				emailList.add("support@cricclubs.com");
				NotificationHelper.sendManualNotification(emailList,
						"Problem in SFTP for  - " + SFTPUSER, path + "---" + ex.getMessage(),
						"support@cricclubs.com");
				ex.printStackTrace();
			}
			pathTobeSent = "";
			isFTPInprogress = false;
			
			
			
		} else if (CommonUtility.isNullOrEmpty(pathTobeSent)) {// when a request
																// is processing
																// try again
																// forever.
			pathTobeSent = path;
			sendFileToSFTP(path, true, SFTPHOST, SFTPPORT, SFTPUSER, SFTPPASS);
		} else if (loop) { // this will be called from the thread that is
							// waiting.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// it may not happen
				log.error(e.getMessage(),e);
			}
			sendFileToSFTP(path, true, SFTPHOST, SFTPPORT, SFTPUSER, SFTPPASS);
		} else {
			// do nothing as there is already a request waiting to be processed.
		}
	}

	public static void main(String args[]) {
		sendFileToAbuDhabi("D:\\Users\\data\\test.xml", false);
	}

	
}







