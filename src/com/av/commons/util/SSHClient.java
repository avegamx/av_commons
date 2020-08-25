package com.av.commons.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;



public class SSHClient {

	private static org.apache.log4j.Logger logger = Logger.getLogger(SSHClient.class);
	
	
	public List<String> executeCommand(String serverIp, String command, String username, String password) throws Exception{
		
		List<String> commands = new ArrayList<String>();
		commands.add(command);
		return executeCommand(serverIp,commands,username,password,null);
		
	}
	
	public List<String> executeCommand(String serverIp, List<String> commands, String username, String password) throws Exception{
		
		return executeCommand(serverIp,commands,username,password,null);
		
	}
	
	
	public List<String> executeCommand(String serverIp, String command, String username, String password, String publicKeyFile) throws Exception{
		
		List<String> commands = new ArrayList<String>();
		commands.add(command);
		return executeCommand(serverIp,commands,username,password,publicKeyFile);
		
	}
	
	public List<String> executeCommand(String serverIp, List<String> commands, String username, String password, String publicKeyFile) throws Exception{

		List<String> lines = new ArrayList<String>();
		
    	java.util.Properties config = new java.util.Properties(); 
    	config.put("StrictHostKeyChecking", "no");
    	//config.put("PreferredAuthentications", "publickey,password,keyboard-interactive");
    	config.put("PreferredAuthentications", "publickey,password");

    	Session session = null;
    	
        try
        {

	    	JSch jsch = new JSch();
	    	
	    	if(publicKeyFile != null) {
		    	jsch.addIdentity(publicKeyFile);
	    	}

	    	session = jsch.getSession(username, serverIp, 22);
	    	
	    	session.setPassword(password);
	    	session.setConfig(config);
	    	session.connect();
	    	logger.trace("Connected");

	    	
	    	for (String command : commands) {
	    		
            	//logger.debug("Command: " + command);
            	
	            ChannelExec channel = (ChannelExec) session.openChannel("exec");
	            channel.setErrStream(System.err);
	            channel.setCommand(command);
	            channel.connect();
	            
	            InputStream stdout = channel.getInputStream();
	            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
	            
	            while (true)
	            {
	                String line = br.readLine();
	                if (line == null)
	                    break;
	                lines.add(line);
	                logger.trace(line);
	            }

	            logger.debug(serverIp + " exitCode: " + channel.getExitStatus());
	            channel.disconnect();
	        }
	    	
    
        }  catch (Exception e) {
        	logger.error("Ocurrio un error en la conexión: " + e);
        	e.printStackTrace();
        	throw e;

        } finally {
        	
        	session.disconnect();
        	
        }
        
        return lines;
        
    }	
	
	
}
