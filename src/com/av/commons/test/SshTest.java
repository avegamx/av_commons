package com.av.commons.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.av.commons.util.Json;
import com.av.commons.util.SSHClient;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.StreamGobbler;

public class SshTest {

	public static void main(String[] args) {

	    String host="10.255.207.10";
	    String user="oracle";
	    String password="passwd";
		List<String> commands = new ArrayList<String>();
		commands.add("date");
		commands.add("hostname");
   
	    List<String> output=null;
	    
	    SSHClient client = new SSHClient();
	    try {
	    	
	    	output = client.executeCommand(host, commands, user, password);
	    	System.out.println(Json.getJson(output));
	    
	    } catch(Exception e) {
	    	System.out.println("erro> " + e);
	    }
	    

	}

}
