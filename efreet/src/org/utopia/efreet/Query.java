package org.utopia.efreet;

import java.util.Vector;

/**
 * This class represents a query statement to be called from a DAO
 */

public class Query 
{
    protected String queryName = null;
    protected String queryStatement = null;
    protected Vector parameters = null;
    protected Vector results = null;

    public static final int P_NONE = 0;
    public static final int P_NUMERIC = 1;
    public static final int P_CHAR = 2;
    public static final int P_DATE = 3;

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
        return P_NONE;
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
