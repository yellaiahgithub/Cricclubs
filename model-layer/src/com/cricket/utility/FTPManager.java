package com.cricket.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class FTPManager {

	private static String SFTPHOST = "69.164.192.62";
	private static int SFTPPORT = 21;
	private static String SFTPUSER = "cricclubs@coloradocricket.org";
	private static String SFTPPASS = "b]GP#KVR7}{S";

	public static void sendFiletoColorado(String path, int clubId, int matchId)
			throws IOException {

		FTPClient ftpClient = new FTPClient();
		Logger log =  LoggerFactory.getLogger(FTPManager.class);

		try {
			ftpClient.connect(SFTPHOST, SFTPPORT);
			ftpClient.enterLocalPassiveMode();
			boolean success = ftpClient.login(SFTPUSER, SFTPPASS);
			
			if (!success) {
				log.error("Connection Failed ");
				return;
			}
			InputStream inputStream = new FileInputStream(path);
			String firstRemoteFile = "/crml-" + clubId + "-" + matchId + ".xml";
			ftpClient.changeWorkingDirectory(firstRemoteFile);
			
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
			inputStream.close();
			if (done) {
				// firstRemoteFile);
			} else {
				// inputStream);
			}
		}

		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				log.error("Error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws Exception {

	}
}
