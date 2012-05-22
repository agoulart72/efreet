package org.utopia.efreet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * This class is used to store information for DAOs about DataSource
 */
public class DataSource {
        
	private static Object LOCK = new Object();
	private static DataSource instance = null;
	private static Map<String, javax.sql.DataSource> datasources = null;
	
	private String datasourceName = null;

    static Logger logger = Logger.getLogger(DataSource.class.getName());
    
    @Deprecated
    public static DataSource getInstance() {
    	if (instance == null) instance = new DataSource();
    	return instance;
    }

    /**
     * This method will be used to register a datasource programatically
     * @param dsName datasource name
     * @param dataSource datasource object
     * @throws Exception error
     */
    public static void registerDataSource(String dsName, javax.sql.DataSource dataSource) throws Exception {
    	synchronized (LOCK) {
	    	if (datasources == null) {
	    		datasources = new HashMap<String, javax.sql.DataSource>();
	    	}
	    	if (dsName != null && dataSource != null) {
	    		datasources.put(dsName, dataSource);
	    	}
    	}    	
    }

    // Retrieve a DataSource from JNDI
    private static javax.sql.DataSource getDataSourceFromJNDI(String dsName) throws Exception {

		javax.sql.DataSource thisDs = null;
    	Context initContext = new InitialContext();
    	Context envContext  = (Context)initContext.lookup("java:/comp/env");
    	thisDs = (javax.sql.DataSource)envContext.lookup("jdbc/" + dsName);

    	// Doesn't find the ENC JNDI
		if (thisDs == null) {
			Context context = new InitialContext();
			thisDs = (javax.sql.DataSource) context.lookup(dsName);
		}
		return thisDs;
    }
    
    // Search for the datasource from our static resource
    private static javax.sql.DataSource getDataSource(String dsName) throws Exception {
    	synchronized (LOCK) {
	    	if (datasources == null) {
	    		datasources = new HashMap<String, javax.sql.DataSource>();
	    	}
	    	if (!datasources.containsKey(dsName)) {
    			datasources.put(dsName, getDataSourceFromJNDI(dsName));
	    	}
	    	return datasources.get(dsName);
    	}
    }
    
    @Deprecated
    public void setName(String value) { this.datasourceName = value; }
    @Deprecated
    public String getName() { return datasourceName; }
    

    /**
     * Just for DEBUG purposes
     * @param dsName data source name
     * @param daoModelName dao model name
     * @param queryName query name
     * @return Connection
     * @throws Exception error
     */
    public static Connection getConnection(String dsName, String daoModelName, String queryName) throws Exception {
    	logger.debug("DB Connect (" + dsName + ") : " + daoModelName + "." + queryName );
    	return getConnection(dsName);
    }
    
    /**
     * Retrieves a connection from the specified DataSource
     * @param dsName data source name
     * @return Connection
     * @throws Exception error
     */
    public static Connection getConnection(String dsName) throws Exception {
    	Connection conn = null;
    	try {
    		javax.sql.DataSource ds = getDataSource(dsName);
    		conn = ds.getConnection();
    	} catch (NamingException e) {
    		logger.fatal("Error retrieving DataSource connection", e);
    	} catch (SQLException e) {
    		logger.fatal("Error retrieving DataSource connection", e);
    	}
    	// If not found on the context, try looking for an properties file
    	if (conn == null) {
    		
            URL url = ClassLoader.getSystemResource(dsName + ".properties");

        	try {
                if (url == null) {
                	try {
                		Context initContext = new InitialContext();
                		Context envContext  = (Context)initContext.lookup("java:/comp/env");
                		String xmlFileDir = (String) envContext.lookup("xml/efreet");
                		url = new URL("file:" + xmlFileDir + "/" + dsName + ".properties");
                	} catch (NameNotFoundException nnfe) {
                		logger.warn("Name not found on context ");
                	} catch (NamingException e) {
                		logger.error("Error retrieving Context : ", e);
                	}
                }
                try {
                	if (url != null) {
                		url.openConnection();
                	}
                } catch (FileNotFoundException fnfe) {
                	url = null;
    			}
                if (url == null) {
//                long dt_modified = url.openConnection().getLastModified();
                	url = Thread.currentThread().getContextClassLoader().getResource(dsName + ".properties");
                }

            } catch (IOException ioe) {
            	logger.error("Error reading XML file", ioe);
                throw new EfreetException(ioe.getMessage());
            }

            Properties propConn = new Properties();
            
            propConn.load(url.openStream());
            
            String driver = propConn.getProperty("driverClassName");
    		String urlConn = propConn.getProperty("url");
            String userName = propConn.getProperty("username");
            String password = propConn.getProperty("password");
            
    		try
    		{
    			Class.forName( driver , true, Thread.currentThread().getContextClassLoader());
    		}
    		catch ( ClassNotFoundException ce )
    		{
    			logger.error("Driver " + driver + " not found");
    			throw ce; 
    		}

    		conn = DriverManager.getConnection(urlConn, userName, password);

    	}
//    	if (conn.getAutoCommit()) {
//    		conn.setAutoCommit(false);
//    	}
		return conn;
    }
    
}
