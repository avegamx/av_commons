package com.av.commons.util;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*; 
import org.apache.log4j.Logger;
import java.util.Properties;


public class SendMail {

	private static org.apache.log4j.Logger logger = Logger.getLogger(SendMail.class);	
	private Properties properties;
	
	
	public SendMail(){}
	
	
	public SendMail(Properties props){
		this.properties = props;	
	}
	
	
	public void postMail(String recipients[], String subject, String message, String from) throws MessagingException{
		
		postMail(recipients, subject, message, from, null, null);
				
	}
	
	
	public void postMail(String recipients[], String subject, String message , String from, String[] attachments, String[] fileNames) 
			throws MessagingException {

		
	    Session session = getSession();
	    
	    // create a message
	    Message msg = new MimeMessage(session);

	    logger.debug("Message created");
	    
	    // set the from and to address
	    msg.setFrom(new InternetAddress(from));

	    //Envio a Multiples Direcciones
	    InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
	    for (int i = 0; i < recipients.length; i++){
	    	
	        addressTo[i] = new InternetAddress(recipients[i]);
	    }
	    
	    msg.setRecipients(Message.RecipientType.TO, addressTo);
	   
	    // Optional : You can also set your custom headers in the Email if you Want
	    //msg.addHeader("MyHeaderName", "myHeaderValue");

	    // Setting the Subject and Content Type
	    msg.setSubject(subject);
	    
	    
	    if(attachments != null){
	    	
	        // Create a message part to represent the body text 
	        BodyPart messageBodyPart = new MimeBodyPart(); 
	        messageBodyPart.setText(message); 
		    
		    Multipart multipart = new MimeMultipart();
		    multipart.addBodyPart(messageBodyPart);
		    
	        // add any file attachments to the message 
	        addAtachments(attachments, fileNames, multipart); 
	    	
	        msg.setContent(multipart); 
	        
	    } else {
	    	
		    //Envio Mensaje Sencillo
		    msg.setContent(message, "text/plain; charset=UTF-8");
	    	
	    }

        Transport.send(msg); 
        
        logger.debug("Message sent");
        
        
	}

	
    protected void addAtachments(String[] attachments, String[] fileNames, Multipart multipart) 
    throws MessagingException, AddressException {
    	
		for(int i = 0; i<attachments.length; i++) { 
			
			String filename = attachments[i]; 
			MimeBodyPart attachmentBodyPart = new MimeBodyPart(); 
			
			//use a JAF FileDataSource as it does MIME type detection 
			DataSource source = new FileDataSource(filename); 
			attachmentBodyPart.setDataHandler(new DataHandler(source)); 
			
			//assume that the filename you want to send is the same as the 
			//actual file name - could alter this to remove the file path
			
			if(fileNames != null && fileNames.length>=i+1){
				
				attachmentBodyPart.setFileName(fileNames[i]); 	
				
			} else{
				
				int j = i +1;
				attachmentBodyPart.setFileName("atachment"+ j); 	
				
			}
					
			//add the attachment 
			multipart.addBodyPart(attachmentBodyPart); 
			
			logger.debug("File " +  filename +  " added");
			
		} 
	}

    
	private Session getSession() { 
		
		Authenticator authenticator = new Authenticator(); 
		Properties props = new Properties(); 
		props.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName()); 
		props.setProperty("mail.smtp.auth", properties.getProperty("auth","false"));
		props.setProperty("mail.smtp.host", properties.getProperty("host","smtp.speedymovil.com")); 
		props.setProperty("mail.smtp.port", properties.getProperty("port","35")); 
		
		return Session.getInstance(props, authenticator); 
		
	} 
    
    
    
	private class Authenticator extends javax.mail.Authenticator { 
		
		private PasswordAuthentication authentication; 
		
		public Authenticator() { 
			
			String username = properties.getProperty("username","mail@mail.com"); 
			String password = properties.getProperty("password","password");
			authentication = new PasswordAuthentication(username, password); 
			
		} 
		
		protected PasswordAuthentication getPasswordAuthentication() { 
			
			return authentication; 
			
		} 
		
	} 
    
    
} 
	
