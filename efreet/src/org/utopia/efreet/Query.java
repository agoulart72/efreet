package org.utopia.efreet;

import java.sql.Types;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.SortedSet;
import java.util.Vector;

/**
 * This class represents a query statement to be called from a DAO
 */

public class Query implements Cloneable
{
    protected String queryName = null;
    protected int queryType = 0;
    protected String queryStatement = null;
    protected Vector<Object> parameters = null;
    protected Vector<Object> results = null;

    protected SortedSet<ParameterModel> paramSet = null;
    protected SortedSet<ResultModel> resultSet = null;
    
    // Param Type Constants
    // as defined by java.sql.Types
    
    // Query Type Constants
    public static final int Q_QUERY = 0;
    public static final int Q_UPDATE = 1;
    public static final int Q_PROCEDURE = 2;
    public static final int Q_CONDITIONAL = 3;

    // Delimiter Identifier
    public static final String VAR_DELIMITER_START = "${";
    public static final String VAR_DELIMITER_END = "}";
    public static final String CONDITIONAL_DELIMITER_START = "?{";
    public static final String CONDITIONAL_DELIMITER_END = "}";
    
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
    public String getStatement(Hashtable<String, String> variables) {
    	
    	String retStatement = this.queryStatement; 
    	
    	if (variables != null) {
    		Enumeration<String> enm = variables.keys();
    		while (enm.hasMoreElements()) {
    			String chave = (String) enm.nextElement();
    			if (chave != null) {
    				retStatement = retStatement.replaceAll("\\$\\{" + chave + "\\}", (String) variables.get(chave));
    			}
    		}
    	}
    	
    	return retStatement;
    }
    
    /**
     * Add a parameter to the query
     * @deprecated use {@link #addParameterAt(ParameterModel, int)} instead
     * @param pType Type of parameter (possible values Query.P_NUMERIC, Query.P_CHAR, Query.P_DATE )
     */
    public void addParameter(int pType) {
        if (parameters == null) parameters = new Vector<Object>();
        parameters.add(new Integer(pType));
    }

    /**
     * Add a parameter model to the query
     * @param pm parameter model
     * @param pos position in the vector
     */
    public void addParameterAt(ParameterModel pm, int pos) {
    	if (parameters == null) parameters = new Vector<Object>();
    	if (pos >= parameters.size()) {
    		parameters.setSize(pos + 1);
    		parameters.ensureCapacity(pos + 1);
    	}
    	parameters.setElementAt(pm, pos);
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
     * @return parameter type (possible values possible values Types.JAVA_OBJECT, Types.NUMERIC, Types.CHAR, Types.DATE, Types.TIME, Types.TIMESTAMP )
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
     * @return the whole Parameters Vector
     */
    public Vector<Object> getParameters() {
    	return this.parameters;
    }
    
    /**
     * Sets the whole Parameters Vector
     * BEWARE !!! DANGEROUS OPERATION
     * @param vParams Vector of parameters
     */
    public void setParameters(Vector<Object> vParams) {
    	this.parameters = vParams;
    }
    
    /**
     * Add a result name to the query
     * @deprecated use {@link #addResultAt(ResultModel, int)} instead
     * @param rName Name of the result
     */
    public void addResult(String rName) {
        if (results == null) results = new Vector<Object>();
        results.add(rName);
    }
    
    /**
     * Add a result name to the query at a defined position
     * @deprecated use {@link #addResultAt(ResultModel, int)} instead
     * @param rName Name of the result
     */
    public void addResultAt(String rName, int pos) {
    	if (results == null) results = new Vector<Object>();
    	if (pos >= results.size()) {
    		results.setSize(pos + 1);
    		results.ensureCapacity(pos + 1);
    	}
		results.setElementAt(rName, pos);
    }

    /**
     * Add a result name to the query at a defined position
     * @param rModel Model for the result
     */
    public void addResultAt(ResultModel rModel, int pos) {
    	if (results == null) results = new Vector<Object>();
    	if (pos >= results.size()) {
    		results.setSize(pos + 1);
    		results.ensureCapacity(pos + 1);
    	}
		results.setElementAt(rModel, pos);
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
    public ResultModel getResult(int pos) {
        if ((results != null) && 
            (pos >= 0 && pos < results.size())) {
        	Object obj = results.get(pos);
        	if (obj != null && obj instanceof ResultModel) {
        		return (ResultModel) obj;
        	}
        }
        return null;
    }

    /**
     * Get the result name for a column retrieved by the query
     * @param pos position of the column retrieved
     * @return name of the column retrieved
     */
    public String getResultName(int pos) {
        if ((results != null) && 
            (pos >= 0 && pos < results.size())) {
        	Object obj = results.get(pos);
        	if (obj != null && obj instanceof ResultModel) {
        		return ((ResultModel) obj).getResultName();
        	}
        	// Legacy conversion
        	if (obj != null && obj instanceof String) {
        		return (String) results.get(pos);
        	}
        }
        return null;
    }

    /**
     * @return the whole Results Vector
     */
    public Vector<Object> getResults() {
    	return this.results;
    }
    
    /**
     * Sets the whole Results Vector
     * BEWARE !!! DANGEROUS OPERATION
     * @param vResults
     */
    public void setResults(Vector<Object> vResults) {
    	this.results = vResults;
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@SuppressWarnings("unchecked")
	protected Object clone() throws CloneNotSupportedException {
		
		Query clone = new Query();
		clone.setName(this.queryName);
		clone.setStatement(this.queryStatement);
		clone.setType(this.queryType);
		
		if (this.parameters != null) {
			clone.parameters = (Vector<Object>) this.parameters.clone();
		}
		
		if (this.results != null) {
			clone.results = (Vector<Object>) this.results.clone();
		}
		
		return clone;
	}
    
}
