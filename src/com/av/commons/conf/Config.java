
package com.av.commons.conf;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;

import com.av.commons.db.ConnectionManager;

public class Config {

	private static org.apache.log4j.Logger logger = Logger.getLogger(Config.class);

	private static Config config;
	private Properties querys;
	
	private static Object lock=new Object();
	private Properties props;
	
	//private static Properties config;
	private static InputStream input;

	/** Creates a new instance of Config */
	private Config() {

		Connection conn=null;
		Statement stmt = null;
		ResultSet rs = null;

		props=new Properties();
		
		try {

			input = getClass().getClassLoader().getResourceAsStream("Config.properties");
			querys = new Properties();
			querys.load(input);
			
			String sql = querys.getProperty("configs", "select param, value from config");
			conn = ConnectionManager.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			logger.debug("Loading params .. ");

			while (rs.next()) {

				String key=rs.getString("param");
				String val=rs.getString("value");
				if(val != null)
					props.setProperty(key, val);
			}

		}
		catch (Exception x) {
			logger.debug("Config error: " + x);         
		}

		finally {

			ConnectionManager.closeResulSet(rs);
			ConnectionManager.closeStatement(stmt);            
			ConnectionManager.closeConnection(conn);
			
			try	{
				
				input.close();
				input = null;
			
			} catch(Exception e){}
			

		}
	}

	public static Config getConfig() {

		if (config == null) {
			synchronized (lock) {
				if (config == null) {
					config = new Config();
				}
			}
		}
		return config;
	}

	public static void reloadProperties() {
		config=null;
	}

	public String getProperty(String key) {
		return getProperty(key, "");
	}

	public Properties getProperties() {
		return props;
	}

	public String getProperty(String key, String val) {
		return props.getProperty(key, val);
	}
	
	public int getPropertyAsInt(String key, int val) {

		try {
			return Integer.parseInt(props.getProperty(key,""+val));

		} catch(Exception e){
			return val;
		}
	}
}
