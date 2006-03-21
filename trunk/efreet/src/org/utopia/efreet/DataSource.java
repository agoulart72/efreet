package org.utopia.efreet;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * This class is used to store information for DAOs about DataSource
 */
public class DataSource {
        
	private static DataSource instance = null;
    private String datasourceName = null;

    static Logger logger = Logger.getLogger(DataSource.class.getName());
    
    public static DataSource getInstance() {
    	if (instance == null) instance = new DataSource();
    	return instance;
    }
    
    public void setName(String value) { this.datasourceName = value; }
    public String getName() { return datasourceName; }
    
    public Connection getConnection(String dsName) {
    	Connection conn = null;
    	try {
    		Context initContext = new InitialContext();
    		Context envContext  = (Context)initContext.lookup("java:/comp/env");
    		javax.sql.DataSource ds = (javax.sql.DataSource)envContext.lookup("jdbc/" + dsName);
    		conn = ds.getConnection();
    	} catch (NamingException e) {
    		logger.fatal("Error retrieving DataSource connection", e);
    	} catch (SQLException e) {
    		logger.fatal("Error retrieving DataSource connection", e);
    	}
		return conn;
    }
    
}
