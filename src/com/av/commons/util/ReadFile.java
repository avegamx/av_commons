package com.av.commons.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class ReadFile {

	private static org.apache.log4j.Logger logger = Logger.getLogger(ReadFile.class);
	
	public static String getStringFromFile(String file) {
		
		String text=null;
		BufferedReader reader = null;
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = "\n";

		try {
			
			reader = new BufferedReader(new FileReader (file));
			
			while((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			text = stringBuilder.toString();
			
		} catch(Exception e){
			
			logger.error("Ocurrio un error al leer el archivo: " + e);
			
		} finally {
				
			if(reader!=null){
				try{
					reader.close();				
				} catch(Exception e){}
				
			}
				
		}
		
		return text;
		
	}
	
	public static String getStringFromResource(String resource) {
		
		String text = null;
		InputStream input = null;
		BufferedReader reader = null;
		InputStreamReader isr = null;
		
		try {
			
			input = ReadFile.class.getClassLoader().getResourceAsStream(resource);
			isr = new InputStreamReader(input);
			
			reader = new BufferedReader(isr);
			
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			String ls = "\n";
			
			while((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			text = stringBuilder.toString();	
			
		} catch(Exception e){
			
			logger.error("Ocurrio un error al leer el archivo: " + e);
			
		} finally {
			
			if(reader!=null){
				try{
					reader.close();				
				} catch(Exception x){}
				
			}

		}
		return text;
		
	}
	
	
}
