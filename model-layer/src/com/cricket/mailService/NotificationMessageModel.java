package com.cricket.mailService;

public class NotificationMessageModel {
	
	private String notificationType;
	
	private Bounce bounce;
	
	private MailModel mail;

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Bounce getBounce() {
		return bounce;
	}

	public void setBounce(Bounce bounce) {
		this.bounce = bounce;
	}

	public MailModel getMail() {
		return mail;
	}

	public void setMail(MailModel mail) {
		this.mail = mail;
	}

	
}
