package com.av.commons.test;

import com.av.commons.util.XMLValidator;

public class XMLValidatorTest {

	public static void main(String [] args){
		
		System.out.println(XMLValidator.validate("C:\\xsd\\deviceService.xsd", "C:\\xsd\\response\\detectDevice.txt"));
		
	}
	
}
