package com.example.demo;

import javax.mail.Folder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.mail.AbstractMailReceiver;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceiver;

@SpringBootApplication
public class EmailOperationApplication implements ApplicationRunner {
	
	  private static Logger log = LoggerFactory.getLogger(EmailOperationApplication.class);

	    @Autowired
	    private EmailService emailService;
	    
	    Folder inbox;
	    
	public static void main(String[] args) {
		SpringApplication.run(EmailOperationApplication.class, args);
	}
	
	 public void run(ApplicationArguments applicationArguments) throws Exception {
	        log.info("Spring Mail - Sending Email with Attachment Example");
	        log.info("checking");
	        Mail mail = new Mail();
	        mail.setFrom("Paysa-Airtel-Support@davinta.com");
	        mail.setTo("mabraham@davinta.com");
	        mail.setSubject("Sending Email with Attachment Gmail");
	        mail.setContent("This tutorial demonstrates how to send an email with attachment using Spring Framework.");
	        emailService.sendSimpleMessage(mail);
		     
	         ReceiveEmail receiveEmail = (ReceiveEmail) new ReceiveEmail();
	         MailReceiver ss= receiveEmail.imapInboxReceiver();
	       log.warn("Spring Mail - Sent");
	    }
}
