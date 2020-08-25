package com.av.commons.test;

import com.av.commons.util.SendMail2;

public class MailTest2 {

    public static void main(String args[]){
    	
	    	
    	SendMail2 sm = new SendMail2();
    	
    	String [] recipients = {"armando.vega@prosa.com.mx","veginha@hotmail.com"};
    	String subject = "Prueba";
    	String message = "Mensaje de prueba";
    	String from = "oem@prosa.com.mx";
    	String [] attachments = null;
    	String [] fileNames = null;
    	//String [] attachments = {"C:\\1210320001.JPG"};
    	//String [] fileNames = {"imagen.jpg"};
    	
    	try{
    		sm.postMail(recipients, subject, message, from, attachments, fileNames);

    	
    	}catch(Exception e){
    		System.out.println("Error e: " + e);
    		e.printStackTrace();
    	}
    }
	
}
