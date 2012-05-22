/**
 * 
 */
package org.utopia.common.util;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author metal
 *
 */
public class ConnectionUtil {

	public static final int GENERIC_DRIVER = 0;
	public static final int ORACLE_DRIVER = 1;
	
	/**
	 * Try to retrive a delegated connection from Apache Pool
	 * To use this effectively you must set your pool connection with the following directive :
	 * accessToUnderlyingConnectionAllowed=true
	 * @param con - Current connection
	 * @return "real" connection if using a connection pool
	 */
	public static Connection getInnermostConnection(Connection con) {
		
		Connection rCon = con;
		
		// Tomcat Pool
		if (con instanceof org.apache.tomcat.dbcp.dbcp.DelegatingConnection) {
			rCon = ((org.apache.tomcat.dbcp.dbcp.DelegatingConnection) con).getInnermostDelegate();
		} else
		// Apache Commons Pool
		if (con instanceof org.apache.commons.dbcp.DelegatingConnection) {
			rCon = ((org.apache.commons.dbcp.DelegatingConnection) con).getInnermostDelegate();
		}
		
		return rCon;
	}
	
	/**
	 * Try to determine the type of the underlying connection by its driver
	 * @param con current connection
	 * @return int 
	 */
	public static int connectionType(Connection con) {
		
		int retType = GENERIC_DRIVER;
		try {
			DatabaseMetaData metaData = con.getMetaData();			
			String dName = metaData.getDriverName();
			if (dName.contains("OracleDriver")) {
				retType = ORACLE_DRIVER;
			}			
		} catch (SQLException e) {
			// do nothing
			retType = GENERIC_DRIVER;
		}
		return retType;
	}
	
	/**
	 * Create a blob Object
	 * @param con Connection
	 * @return blob object
	 */
	public static Blob createBlob(Connection con) {
		
		Blob retBlob = null;
		
		try {
			// Oracle specific
			if (con instanceof oracle.jdbc.OracleConnection) { // connectionType(con) == ORACLE_DRIVER) {
        		oracle.jdbc.OracleConnection oracleConnection = (oracle.jdbc.OracleConnection) con;    	        
        		retBlob = oracle.sql.BLOB.createTemporary(oracleConnection, true, oracle.sql.BLOB.DURATION_SESSION);					
			} else
			// JDBC JDK 6.0
			if (Connection.class.getMethod("createBlob") != null ) {		
				retBlob = con.createBlob();
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retBlob;
	}
	
	/**
	 * Create a clob Object
	 * @param con Connection
	 * @return clob object
	 */
	public static Clob createClob(Connection con) {
		
		Clob retClob = null;
		
		try {
			// Oracle specific
			if (con instanceof oracle.jdbc.OracleConnection) { // connectionType(con) == ORACLE_DRIVER) {
        		oracle.jdbc.OracleConnection oracleConnection = (oracle.jdbc.OracleConnection) con;    	        
        		retClob = oracle.sql.CLOB.createTemporary(oracleConnection, true, oracle.sql.CLOB.DURATION_SESSION);					
			} else
			// JDBC JDK 6.0
			if (Connection.class.getMethod("createClob") != null ) {		
				retClob = con.createClob();
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retClob;
	}
	
}
