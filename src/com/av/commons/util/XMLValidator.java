package com.av.commons.util;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.XMLConstants;

import org.apache.log4j.Logger;

public class XMLValidator {

	private static org.apache.log4j.Logger logger = Logger.getLogger(XMLValidator.class);	
	
	
	public static Boolean validate(String xsdFile, String xmlFile) {
				
		Source xsd = new StreamSource(new File(xsdFile));
		Source xml = new StreamSource(new File(xmlFile));
		
		return validate(xsd, xml);
		
	}
	
	
	public static Boolean validate(String[] xsdFiles, Reader reader) {
		
		Source [] xsd = new StreamSource[xsdFiles.length];
		
		for(int i=0; i<xsdFiles.length; i++){
			
			xsd[i] = new StreamSource(new File(xsdFiles[i]));
			
		}
		
		Source xml = new StreamSource(reader);
		
		return validate(xsd, xml);
		
	}
	
	
	
	public static Boolean validate(String xsdFile, InputStream isr) {
		
		Source xsd = new StreamSource(new File(xsdFile));
		Source xml = new StreamSource(isr);
		
		return validate(xsd,xml);

	}
	
	public static Boolean validate(String xsdFile, Reader reader) {
		
		Source xsd = new StreamSource(new File(xsdFile));
		Source xml = new StreamSource(reader);
		
		return validate(xsd,xml);

	}
	
		
	public static Boolean validate(Source xsd, Source xml){
		
		Boolean ret = false;
		
		try{
			
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
									
			Schema schema = factory.newSchema(xsd);
	        Validator validator = schema.newValidator();
	        validator.validate(xml);

			ret = true;
			
			
		} catch(Exception e){
			
			logger.error("Error e: " + e);
			//e.printStackTrace();
		}
		
		return ret;
	}
	
	
	public static Boolean validate(Source[] xsd, Source xml){
		
		Boolean ret = false;
		
		try{
			
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
									
			Schema schema = factory.newSchema(xsd);
	        Validator validator = schema.newValidator();
	        validator.validate(xml);

			ret = true;
			
			
		} catch(Exception e){
			
			logger.error("Error e: " + e);
			//e.printStackTrace();

		}
		
		return ret;
	}
	
}

