package com.av.commons.util;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json {

	private static org.apache.log4j.Logger logger = Logger.getLogger(Json.class);
	
	private static Gson gson = new Gson();
	private static Gson gsonFormated = new GsonBuilder().disableHtmlEscaping().create();
	
	
	public static String getJson(Object obj){
		
		String text=null;
		
		try{
			text = gson.toJson(obj);
			
		} catch(Exception e){
			
			logger.debug("Error: " + e);
		}
				
		return text;
	}
	
	
	public static String getJsonFormated(Object obj){
		
		String text=null;
		
		try{
			
			text = gsonFormated.toJson(obj);
			
		} catch(Exception e){
			
			logger.debug("Error: " + e);
		}
				
		return text;
	}
	
}
