package com.av.commons.db;

import java.sql.Connection;
import java.util.Properties;
import java.io.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;

import org.apache.log4j.Logger;

public class ConnectionManager {

	private static org.apache.log4j.Logger logger = Logger.getLogger(ConnectionManager.class);

	private static Context ctx = null;  
	private static String url;
	private static String user;
	private static String pass;
	private static String driver;

	private static Properties config;
	private static InputStream input;

	private static String jndi;
	private static String dbalias;

	private static ConnectionManager cm = new ConnectionManager();


	public ConnectionManager(){

		try{

			input = getClass().getClassLoader().getResourceAsStream("Db.properties");
			config = new Properties();
			config.load(input);
			jndi = config.getProperty("jndi","false");
			driver = config.getProperty("driver","oracle.jdbc.driver.OracleDriver");
			url = config.getProperty("url","jdbc:oracle:thin:@172.19.3.232:1521:SRSDB");
			user = config.getProperty("user","sia_dbuser");
			pass = config.getProperty("password","sia_dbuser");
			dbalias = config.getProperty("dbalias","sia_core");
			logger.info("Db.properties successfully loaded");

		} catch(Exception e){
			logger.error("Could not load Db.properties: "+e);          
		}                 
	}


	/**
	 * Used by servlets and JSPs to get a connection from the pool.
	 */

	public static Connection getConnection() throws SQLException {

		if (jndi.equalsIgnoreCase("false")){

			Connection con = null;

			try{

				Class.forName(cm.driver);
				con = DriverManager.getConnection(cm.url, cm.user, cm.pass);
				logger.debug("Conexion exitosa a BD ... - Connection: " + con);

			}catch(Exception e){
				
				logger.error("Error al conectar a BD: "+e.getMessage(),e);                      
				con = null;
			}
			
			return con;  

		}else{

			return getConnection(dbalias); 

		}
	}

	/**
	 * Used to get a new connection to the specified alias param
	 */

	public static Connection getConnection(String alias) throws SQLException {

		Connection con = null;

		if (jndi.equalsIgnoreCase("false")){

			try{

				String driver= config.getProperty(alias+".driver","oracle.jdbc.driver.OracleDriver");
				String url = config.getProperty(alias+".url","jdbc:oracle:thin:@172.16.66.217:1521:SRSDB");
				String user = config.getProperty(alias+".user","username");
				String pass = config.getProperty(alias+".password","password");  
				
				logger.debug(url);
				Class.forName(driver);
				con=DriverManager.getConnection(url, user, pass);
				
				logger.debug("Conexion exitosa a BD");
				return con;

			}catch(Exception e){
				logger.error("Error al Conectarse a la BD: "+e.getMessage());
			}

		}else{

			try{

				if(ctx==null){
					ctx=new InitialContext();
				}
				
				ctx.lookup(alias);
				DataSource dataSource = (DataSource) ctx.lookup(alias);                
				con = dataSource.getConnection();
				logger.debug("Conexion establecida " + alias + " - Connection: " + con);
				return con;

			}catch(Exception e){

				logger.fatal("Error al obtener conexion: "+e.getMessage(),e);
				throw new SQLException (e.getMessage());

			}
		}
		return con;
	}


	/**
	 * Returns the connection back to the pool.
	 */

	public static Connection closeConnection(Connection conn) {

		String code = null;

		try{

			if(conn != null){
				code = conn.toString();
				conn.close();
				logger.debug("DB connection closed - Connection: " + code);
			}

		} catch(Exception e){
			
			//logger.error("Error when closing DB connection: " + code + " - ",e);  
			
		} finally{
			conn = null;
		}
		
		return null;

	}
	
	
	public static Statement closeStatement(Statement stmt){
		
		try{ 
			if (stmt!=null) 
				stmt.close(); 
			
		} catch(Exception e){ 
			
		} finally { 			
			stmt=null; 			
		}
		
		return null;
		
	}

	public static PreparedStatement closePreparedStatement(PreparedStatement pstmt){
		
		try{ 
			if (pstmt!=null) 
				pstmt.close(); 
			
		} catch(Exception e){ 
			
		} finally { 			
			pstmt=null; 			
		}
		
		return null;
		
	}
	
	
	public static ResultSet closeResulSet(ResultSet rs){
		
		try{ 
			if (rs!=null) 
				rs.close(); 
			
		} catch(Exception e){ 
			
		} finally { 			
			rs=null; 			
		}
		
		return null;
		
	}
}