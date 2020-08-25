package com.av.commons.test;

import com.av.commons.util.SendMail;

public class MailTest {

    public static void main(String args[]){
    	
	    	
    	SendMail sm = new SendMail();
    	
    	String [] recipients = {"armando.vega@speedymovil.com"};
    	String subject = "Prueba";
    	String message = "Mensaje de prueba";
    	String from = "soporte@speedymovil.com";
    	//String [] attachments = null;
    	//String [] fileNames = null;
    	String [] attachments = {"C:\\1210320001.JPG"};
    	String [] fileNames = {"imagen.jpg"};
    	
    	try{
    		sm.postMail(recipients, subject, message, from, attachments, fileNames);

    	
    	}catch(Exception e){
    		System.out.println("Error e: " + e);
    		e.printStackTrace();
    	}
    }
	
}
