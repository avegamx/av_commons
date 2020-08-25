package com.av.commons.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;


public class DatabaseManager {

	private static Logger logger = Logger.getLogger(DatabaseManager.class);
	
	private static DatabaseManager instance;
	private static final Object lock = new Object();
	
	private Hashtable<String, PoolingDataSource> dss = null;
	private Hashtable<String, GenericObjectPool> pools = null;
		
	//MANDATORY PARAMETER
	public static final String DATASOURCES_PROPERTY_NAME = "datasources";
	
	//MANDATORY PARAMETERS FOR EVERY DATASOURCE
	public static final String USER = "user";
	public static final String PASSWORD = "password";
	public static final String URL = "url";
	
	//OPTIONAL PARAMETERS FOR EVERY DATASOURCE
	public static final String DRIVER = "driver";
	public static final String MAX_ACTIVE = "maxActive";
	public static final String MAX_IDLE = "maxIdle";
	public static final String MIN_IDLE = "minIdle";
	public static final String MAX_WAIT = "maxWait";
	public static final String TEST_ON_BORROW = "testOnBorrow";
	public static final String TEST_ON_RETURN = "testOnReturn";
	public static final String TEST_WHILE_IDLE = "testWhileIdle";
	public static final String VALIDATION_QUERY = "validationQuery";
	public static final String READ_ONLY = "defaultReadOnly";
	public static final String AUTO_COMMIT = "defaultAutoCommit";	
	
	
	public static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final int DEFAULT_MAX_ACTIVE = 20;
	public static final int DEFAULT_MAX_IDLE = 10;
	public static final int DEFAULT_MIN_IDLE = 5;
	public static final int DEFAULT_MAX_WAIT = 3000;
	
	public static final Boolean DEFAULT_TEST_ON_BORROW = false;
	public static final Boolean DEFAULT_TEST_ON_RETURN = false;
	public static final Boolean DEFAULT_TEST_WHILE_IDLE = false;
	public static final String DEFAULT_VALIDATION_QUERY = "select 1 from dual";
	
	public static final Boolean DEFAULT_READ_ONLY = false;
	public static final Boolean DEFAULT_AUTO_COMMIT = true;	
	
	
	private DatabaseManager(){
		init();
	}
	
	public static DatabaseManager getInstance(){
		
		if(instance==null){
			synchronized(lock){
				if(instance == null){
					instance = new DatabaseManager();
				}
				
			}
		}
		
		return instance;
	}
	
	private void init(){
		
		
		dss = new Hashtable<String, PoolingDataSource>();
		pools = new Hashtable<String, GenericObjectPool>();
		
		Properties config = new Properties();
		try {
			
			
			//InputStream is = DatabaseManager.class.getResourceAsStream("/dbConfig.properties");			
			InputStream is = getClass().getClassLoader().getResourceAsStream("Datasources.properties");
			
			config.load(is);
			is.close();
			
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return;
		}

		
		
		String[] datasources = null;
		String dsource = config.getProperty(DATASOURCES_PROPERTY_NAME);
		
		if(dsource!=null && !dsource.equals("")){
			datasources = dsource.split(",");
		}
		

		String url = null;
		String user = null;
		String password = null;
		String driver = null;

		int maxActive = DEFAULT_MAX_ACTIVE;
		int maxIdle = DEFAULT_MAX_IDLE;
		int minIdle = DEFAULT_MIN_IDLE;
		int maxWait = DEFAULT_MAX_WAIT;
		
		Boolean testOnBorrow = DEFAULT_TEST_ON_BORROW;
		Boolean testOnReturn = DEFAULT_TEST_ON_RETURN;
		Boolean testWhileIdle = DEFAULT_TEST_WHILE_IDLE;
		
		String validationQuery = DEFAULT_VALIDATION_QUERY;
		
		Boolean defaultReadOnly = DEFAULT_READ_ONLY;
		Boolean defaultAutoCommit = DEFAULT_AUTO_COMMIT;
		
		
		if(datasources != null){
			
			for(int i=0; i<datasources.length; i++){
				
				String ds = datasources[i].trim();
				
				driver = config.getProperty(ds + "." + DRIVER, DEFAULT_DRIVER);
				
				try {
		            Class.forName(driver);
		            
		        } catch (ClassNotFoundException e) {
		        	
		        	logger.debug("Error loading driver: " + e);
		            e.printStackTrace();
		            
		        } catch (Exception e){
		        	
		        	logger.debug("Error loading driver: " + e);
		        	e.printStackTrace();
		        	
		        }
				
				
				url = config.getProperty(ds + "."+ URL);
				user = config.getProperty(ds + "." + USER);
				password = config.getProperty(ds + "." + PASSWORD);
				
				
				try{
					maxActive = Integer.parseInt(config.getProperty(ds + "." + MAX_ACTIVE));
				}catch(Exception e){
					maxActive = DEFAULT_MAX_ACTIVE;
				}
				try{
					maxIdle = Integer.parseInt(config.getProperty(ds + "." + MAX_IDLE));
				}catch(Exception e){
					maxIdle = DEFAULT_MAX_IDLE;
				}
				try{
					minIdle = Integer.parseInt(config.getProperty(ds + "." + MIN_IDLE));
				}catch(Exception e){
					minIdle = DEFAULT_MIN_IDLE;
				}
				try{
					maxWait = Integer.parseInt(config.getProperty(ds + "." + MAX_WAIT));
				}catch(Exception e){
					maxWait = DEFAULT_MAX_WAIT;
				}			
				try{
					testOnBorrow = Boolean.parseBoolean(config.getProperty(ds + "." + TEST_ON_BORROW));
				}catch(Exception e){
					testOnBorrow = DEFAULT_TEST_ON_BORROW;
				}
				try{
					testOnReturn = Boolean.parseBoolean(config.getProperty(ds + "." + TEST_ON_RETURN));
				}catch(Exception e){
					testOnReturn = DEFAULT_TEST_ON_RETURN;
				}
				try{
					testWhileIdle = Boolean.parseBoolean(config.getProperty(ds + "." + TEST_WHILE_IDLE));
				}catch(Exception e){
					testWhileIdle = DEFAULT_TEST_WHILE_IDLE;
				}
				
				
				validationQuery = config.getProperty(ds + "." + VALIDATION_QUERY, DEFAULT_VALIDATION_QUERY);

				
				try{
					defaultReadOnly = Boolean.parseBoolean(config.getProperty(ds + "." + READ_ONLY));
				}catch(Exception e){
					defaultReadOnly = DEFAULT_READ_ONLY;
				}
				try{
					defaultAutoCommit = Boolean.parseBoolean(config.getProperty(ds + "." + AUTO_COMMIT));
				}catch(Exception e){
					defaultAutoCommit = DEFAULT_AUTO_COMMIT;
				}
			

				registerDataSource(ds, user, password, url, maxActive, maxIdle, minIdle, maxWait, testOnBorrow, testOnReturn,
						testWhileIdle, validationQuery, defaultReadOnly, defaultAutoCommit
						);
								
			}
			
		} else{
			
			logger.info("No datasource available");
			
		}

		
	}
	
	
	public void registerDataSource(String _name, String _user, String _password, String _url){
		
		registerDataSource(_name, _user, _password, _url, DEFAULT_MAX_ACTIVE, DEFAULT_MAX_IDLE, DEFAULT_MIN_IDLE, DEFAULT_MAX_WAIT, 
				DEFAULT_TEST_ON_BORROW, DEFAULT_TEST_ON_RETURN, DEFAULT_TEST_WHILE_IDLE, DEFAULT_VALIDATION_QUERY, DEFAULT_READ_ONLY,
				DEFAULT_AUTO_COMMIT);
		
	}
	
	
	
	public void registerDataSource(String _name, String _user, String _password, String _url, int _maxActive, int _maxIdle, int _minIdle, 
			int _maxWait){
		
		registerDataSource(_name, _user, _password, _url, _maxActive, _maxIdle, _minIdle, _maxWait, 
				DEFAULT_TEST_ON_BORROW, DEFAULT_TEST_ON_RETURN, DEFAULT_TEST_WHILE_IDLE, DEFAULT_VALIDATION_QUERY, DEFAULT_READ_ONLY,
				DEFAULT_AUTO_COMMIT);
		
	}
	
	
	
	public void registerDataSource(String name, String user, String password, String url, int maxActive, int maxIdle, int minIdle, 
			int maxWait, boolean testOnBorrow, boolean testOnReturn, boolean testWhileIdle, String validationQuery, boolean defaultReadOnly,
			boolean defaultAutoCommit){
		
		boolean create = true;

		//Revisa que no exista un DS con ese Nombre, si es así lo borra
		if(dss.containsKey(name))
			create = shutdownDatasource(name);

		if(create){
			
			GenericObjectPool connectionPool = new GenericObjectPool(null);
	        connectionPool.setMaxActive(maxActive);
	        connectionPool.setMaxIdle(maxIdle);
	        connectionPool.setMaxWait(maxWait);
	        connectionPool.setMinIdle(minIdle);
	        connectionPool.setTestOnBorrow(testOnBorrow);
	        connectionPool.setTestOnReturn(testOnReturn);
	        connectionPool.setTestWhileIdle(testWhileIdle);
	        
	        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, user, password);
	        
	        /*
	        Properties props = new Properties();
	        props.setProperty("user",_user);
	        props.setProperty("password",_password);

	        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(_url,props);
	        */        
	        
	        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, 
	        		validationQuery, defaultReadOnly, defaultAutoCommit);
	        connectionPool.setFactory(poolableConnectionFactory);
	        
	        PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
	        
			dss.put(name, dataSource);
			pools.put(name, connectionPool);
			logger.info("Configured datasource '" + name + "' - url: " + url + ", maxActive:" + maxActive + ", maxIdle:"+ maxIdle + ", minIdle:"
			+ minIdle + ", maxWait:" + maxWait + ", testOnBorrow:" + testOnBorrow + ", testOnReturn:" + testOnReturn + ", testWhileIdle:" + testWhileIdle +
			", validationQuery: " + validationQuery + ", defaultReadOnly:" + defaultReadOnly + ", defaultAutoCommit:" + defaultAutoCommit);
			
		}
	}
	
	
	public Connection getConnection(String dsName) throws SQLException, Exception{
		
		try{
		
			PoolingDataSource bds = dss.get(dsName);
	
			if(bds != null){
				Connection conn = (Connection) bds.getConnection();
				return conn;
			}else{
				bds = dss.get("default");
				if(bds != null){
					Connection conn = (Connection) bds.getConnection();
					return conn;
				}
			}
		
		} catch(SQLException s){
			
			logger.error("Unable to get a valid connection: " + dsName);
			throw s;			
			
		} catch(Exception e){
			
			logger.error("Unable to get a valid connection: " + dsName);
			throw e;
		}
		
		throw new Exception("Unknow datasource: "+dsName);
		
	}
	
	public boolean shutdownDatasource(String name){
		
		boolean ok = false;
		
		GenericObjectPool ds = pools.get(name);
		
		if(ds != null){
			try {
				
				ds.close();
				dss.remove(name);
				ok = true;

			} catch (Exception e){
				logger.warn(e.getMessage(), e);
			}
		}
		return ok;
	}
	
	
	public void shutdown(){
		
		Enumeration<GenericObjectPool> datasources = pools.elements();
		while(datasources.hasMoreElements()){
			GenericObjectPool bds = datasources.nextElement();
			try {
				bds.close();
			} catch (SQLException e) {
				logger.warn(e.getMessage(), e);
			} catch (Exception e){
				logger.warn(e.getMessage(), e);
			}
		}
		
		dss.clear();
	}
	
	
	public void printDataSourceStats(String name) throws SQLException, Exception {
		GenericObjectPool ds = pools.get(name);
		if(ds != null){
			logger.debug("DataSource '" + name + "' - NumActive: " + ds.getNumActive() + ", NumIdle: " + ds.getNumIdle());
		}
	}
	
	
}
