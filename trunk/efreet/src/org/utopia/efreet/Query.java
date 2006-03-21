package org.utopia.efreet;

import java.sql.Types;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * This class represents a query statement to be called from a DAO
 */

public class Query 
{
    protected String queryName = null;
    protected int queryType = 0;
    protected String queryStatement = null;
    protected Vector parameters = null;
    protected Vector results = null;

    // Param Type Constants
    // as defined by java.sql.Types
    
    // Query Type Constants
    public static final int Q_QUERY = 0;
    public static final int Q_UPDATE = 1;
    public static final int Q_PROCEDURE = 2;

    /**
     * Sets the query name
     * @param param Query Identification Name
     */
    public void setName(String param) { this.queryName = param; }
    /**
     * Retrieves the query name
     * @return Query Identification name
     */
    public String getName() { return this.queryName; }

    /**
     * Sets the Query Type
     * @param param Query Identification Type
     */
    public void setType(int param) { this.queryType = param; }
    /**
     * Retrieves the query Type
     * @return Query Identification Type
     */
    public int getType() { return this.queryType; }
    
    /**
     * Sets the query statement
     * @param param Query Statement
     */
    public void setStatement(String param) { this.queryStatement = param; }
    /**
     * Retrieves the query statement
     * @return Query Statement
     */
    public String getStatement() { return this.queryStatement; }
    /**
     * Return the query exchanging variable values  
     * @param variables
     * @return Query statement exchanging the variables
     */
    public String getStatement(Hashtable variables) {
    	
    	String retStatement = this.queryStatement; 
    	
    	if (variables != null) {
    		Enumeration enm = variables.keys();
    		while (enm.hasMoreElements()) {
    			String chave = (String) enm.nextElement();
    			if (chave != null) {
    				retStatement.replaceAll("${" + chave + "}", (String) variables.get(chave));
    			}
    		}
    	}
    	
    	return retStatement;
    }
    
    /**
     * Add a parameter to the query
     * @param pType Type of parameter (possible values Query.P_NUMERIC, Query.P_CHAR, Query.P_DATE )
     */
    public void addParameter(int pType) {
        if (parameters == null) parameters = new Vector();
        parameters.add(new Integer(pType));
    }

    /**
     * Retrieve the number of parameters
     * @return Number of parameters
     */
    public int parameterSize() {
        if (parameters == null) return 0;
        return parameters.size();
    }

    /**
     * Retrieve the type of parameter
     * @param pos Parameter position in the query
     * @return parameter type (possible values possible values Query.P_NONE, Query.P_NUMERIC, Query.P_CHAR, Query.P_DATE )
     */
    public int getParameter(int pos) {
        if ((parameters != null) && 
            (pos >= 0 && pos < parameters.size())) {
            Integer p = (Integer) parameters.get(pos);
            if (p != null) return p.intValue();
        }
        return Types.JAVA_OBJECT;
    }

    /**
     * Add a result name to the query
     * @param rName Name of the result
     */
    public void addResult(String rName) {
        if (results == null) results = new Vector();
        results.add(rName);
    }
    
    /**
     * Add a result name to the query at a defined position
     * @param rName Name of the result
     */
    public void addResultAt(String rName, int pos) {
    	if (results == null) results = new Vector();
    	if (pos >= results.size()) {
    		results.setSize(pos + 1);
    		results.ensureCapacity(pos + 1);
    	}
		results.setElementAt(rName, pos);
    }
    
    /**
     * Retrieve the number of resulting columns that will be retrieved by
     * the query
     * @return Number of results
     */
    public int resultSize() {
        if (results == null) return 0;
        return results.size();
    }

    /**
     * Get the result name for a column retrieved by the query
     * @param pos position of the column retrieved
     * @return name of the column retrieved
     */
    public String getResult(int pos) {
        if ((results != null) && 
            (pos >= 0 && pos < results.size())) {
            return (String) results.get(pos);
        }
        return null;
    }
    
}
