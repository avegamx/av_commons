package com.av.commons.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.StreamGobbler;

public class SSHClientOld {

	private static org.apache.log4j.Logger logger = Logger.getLogger(SSHClientOld.class);
	
	public List<String> executeCommand(String serverIp, String command, String username, String password) throws Exception{
		return executeCommand(serverIp,command,username,password,null);
	}
	
	public List<String> executeCommand(String serverIp, String command, String username, String password, String publicKeyFile) throws Exception{

		List<String> lines = new ArrayList<String>();
		
        try
        {
            Connection conn = new Connection(serverIp);
            conn.connect();
            
            boolean isAuthenticated;
            
            if(publicKeyFile != null) {
            	File kfile = new File(publicKeyFile);
            	isAuthenticated = conn.authenticateWithPublicKey(username, kfile, null);
            	
            } else {
            	
            	isAuthenticated = conn.authenticateWithPassword(username, password);            	
            }

            
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");  
            
            ch.ethz.ssh2.Session sess = conn.openSession();
            sess.execCommand(command);  
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            
            
            while (true)
            {
                String line = br.readLine();
                if (line == null)
                    break;
                lines.add(line);
                logger.trace(line);
            }
            logger.debug("ExitCode: " + sess.getExitStatus());
            sess.close();
            conn.close();
            br.close();
            
        }
        catch (Exception e)
        {
        	logger.error("Ocurrio un error en la conexión: " + e);
        	e.printStackTrace();
        	throw e;

        }
        
        return lines;
        
    }
	
	public List<List<String>> executeCommands(String serverIp, List<String> commands, String username, String password) throws Exception{
		return executeCommands(serverIp, commands, username, password, null);
	}
	public List<List<String>> executeCommands(String serverIp, List<String> commands, String username, String password, String publicKeyFile) throws Exception{

		List<List<String>> lines = new ArrayList<List<String>>();
		
        try
        {
            Connection conn = new Connection(serverIp);
            conn.connect();
            boolean isAuthenticated;
            
            if(publicKeyFile != null) {
            	File kfile = new File(publicKeyFile);
            	isAuthenticated = conn.authenticateWithPublicKey(username, kfile, null);
            	
            } else {
            	
            	isAuthenticated = conn.authenticateWithPassword(username, password);            	
            }
            
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");  
            

            ch.ethz.ssh2.Session sess = null;
            
            List<String> commandLine = null;
            
            for(String command : commands){
            	
            	commandLine = new ArrayList<String>();
                sess = conn.openSession();
                
            	logger.debug("Command: " + command);
            	
                sess.execCommand(command);  
                InputStream stdout = new StreamGobbler(sess.getStdout());
                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                
                while (true)
                {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    commandLine.add(line);
                    logger.trace(line);
                }
                logger.debug(serverIp + " ExitCode: " + sess.getExitStatus());
                sess.close();
                br.close();
            	
            }
            
            lines.add(commandLine);
            
            conn.close();
	
        }
        
        catch (Exception e)
        {
        	logger.error("Ocurrio un error en la conexión: " + e);
        	e.printStackTrace();
        	throw e;

        }
        
        return lines;
        
    }
	
	
}
