package org.utopia.efreet;

import java.sql.Types;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a query statement to be called from a DAO
 */

public class QueryModel implements Cloneable
{
    protected String queryName = null;
    protected int queryType = 0;
    protected String queryStatement = null;

    protected SortedSet<ParameterModel> parameters = null;
    protected SortedSet<ResultModel> results = null;
    
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
    	
    	String retStatement = getStatement(); 
    	
    	if (variables != null) {
    		Enumeration<String> enm = variables.keys();
    		while (enm.hasMoreElements()) {
    			String chave = (String) enm.nextElement();
    			if (chave != null) {
    				retStatement = retStatement.replaceAll("\\$\\{" + chave + "\\}", (String) variables.get(chave));
    			}
    		}
    	}
    	
    	// Replace all ocurrences of the new type of parameter
    	retStatement = retStatement.replaceAll("\\#\\{[^\\}]*\\}", "?");
    	
    	return retStatement;
    }

    /**
     * Add a Parameter Model to the QueryModel
     * @param pm Parameter Model to be added
     */
    public void addParameter(ParameterModel pm) {
    	if (parameters ==null) {
    		parameters = Collections.synchronizedSortedSet(new TreeSet<ParameterModel>());
    	}
    	parameters.add(pm);
    }
        
    /**
     * Retrieve the number of parameters
     * @return Number of parameters
     */
    public int parameterSize() {
        if (parameters == null) return 0;
        return parameters.size();
    }

    public ParameterModel getParameter(int pos) {
    	ParameterModel pm = null;
    	if (parameters != null) {
    		for (ParameterModel pmi : parameters) {
    			if (pmi.getParamIndex() == pos) {
    				pm = pmi;
    				break;
    			}
    		}
    	}
    	return pm;
    }
    
    /**
     * Retrieve the type of parameter
     * @param pos Parameter position in the query
     * @return parameter type (possible values possible values Types.JAVA_OBJECT, Types.NUMERIC, Types.CHAR, Types.DATE, Types.TIME, Types.TIMESTAMP )
     */
    public int getParameterType(int pos) {
    	Integer p = Types.JAVA_OBJECT; 
    	if (parameters != null)  {
    		ParameterModel pm = getParameter(pos);
    		if (pm != null) p = pm.getParamType();
        }
        return p;
    }

    /**
     * @return the whole Parameters Vector
     */
    public SortedSet<ParameterModel> getParameters() {
    	return this.parameters;
    }
    
    /**
     * Add a result to the query
     * @param rm Result model
     */
    public void addResult(ResultModel rm) {
    	if (results ==null) {
    		results = Collections.synchronizedSortedSet(new TreeSet<ResultModel>());
    	}
        results.add(rm);
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

    	ResultModel rm = null; 
    	if (results != null) {
    		for (ResultModel rmi : results) {
    			if (rmi.getResultIndex() == pos) {
    				rm = rmi;
    				break;
    			}
    		}
        }
        return rm;    	
    }

    /**
     * Get the result name for a column retrieved by the query
     * @param pos position of the column retrieved
     * @return name of the column retrieved
     */
    public String getResultName(int pos) {
    	return getResult(pos).getResultName();
    }
    
    
    /**
     * @return the whole Results Vector
     */
    public SortedSet<ResultModel> getResults() {
    	return this.results;
    }
        
	/**
	 * @see java.lang.Object#clone()
	 */
	protected QueryModel clone() throws CloneNotSupportedException {
		
		QueryModel clone = new QueryModel();
		clone.setName(this.queryName);
		clone.setStatement(this.queryStatement);
		clone.setType(this.queryType);
		
		if (this.parameters != null) {
			Iterator<ParameterModel> itParam = this.parameters.iterator();
			while (itParam.hasNext()) {
				ParameterModel pm = itParam.next(); 
				if (pm != null) clone.addParameter(pm.clone());
			}
		}
		
		if (this.results != null) {
			Iterator<ResultModel> itResult = this.results.iterator();
			while (itResult.hasNext()) {
				ResultModel rm = itResult.next(); 
				if (rm != null) clone.addResult(rm.clone());
			}
		}
		
		return clone;
	}
    
}
