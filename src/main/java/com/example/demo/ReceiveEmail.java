package com.example.demo;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.mail.AbstractMailReceiver;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceiver;

public class ReceiveEmail extends AbstractMailReceiver{
	@Autowired
	private ImapMailReceiver imapMailReceiver;
	
		public MailReceiver imapInboxReceiver() {
			 imapMailReceiver = new ImapMailReceiver("imap://mabraham%40davinta.com:Jinu%40143@outlook.office365.com/INBOX");
			imapMailReceiver.setShouldMarkMessagesAsRead(false);
			// result.setShouldDeleteMessages(true);
			imapMailReceiver.setJavaMailProperties(javaMailProperties());
			imapMailReceiver.setJavaMailAuthenticator(javaMailAuthenticator());
			return imapMailReceiver;
		}

		public Properties javaMailProperties() {
			Properties result = new Properties();
			result.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			result.setProperty("mail.imap.socketFactory.fallback", "false");
			result.setProperty("mail.store.protocol", "imaps");
			result.setProperty("mail.mime.address.strict", "false");
			result.setProperty("mail.debug", "false");
			return result;
		}


		public Authenticator javaMailAuthenticator() {
			Authenticator result = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("mabraham@davinta.com", "Jinu@143");
				}
			};
			return result;
		}

		@Override
		protected Message[] searchForNewMessages() throws MessagingException {
			// TODO Auto-generated method stub
			return null;
		}
	   
}
