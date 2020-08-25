package com.av.commons.test;

import com.av.commons.util.Base64Coder;

public class Base64Test {

	public static void main(String args[]){


		String codigo = "R2RQMDFkMXQ0cw==";
		
		System.out.println(Base64Coder.decodeString(codigo));

		System.out.println(Base64Coder.encodeString("a"));
	}
	
}
