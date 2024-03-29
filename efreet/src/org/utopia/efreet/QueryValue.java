/*
 * $Id: QueryValue.java,v 1.6 2012-05-22 13:34:43 agoulart Exp $
 */
package org.utopia.efreet;

/**
 * Object to store a String containing a sql query
 * This object is to be used to create non-standard
 * queries on classes extending DataAccessObject.
 */
public class QueryValue
{
    public static int EQUAL = 0;
    public static int LIKE = 1;

    private String sqlQuery = null;
    public int queryType = EQUAL;

    public QueryValue(String param) { 
	this.sqlQuery = param; 
    }

    public QueryValue(String param, int tipo) { 
	this.sqlQuery = param; 
	this.queryType = tipo;
    }

    public String toString() {
	return this.sqlQuery;
    }
}
