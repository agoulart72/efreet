/**
 * 
 */
package org.utopia.efreet;

/**
 * @author metal
 * @version $Id: QueryInBatch.java,v 1.1 2012-05-22 13:35:30 agoulart Exp $
 * 
 * POJO bean for use with batch processes
 * 
 */
public class QueryInBatch {

	/**
	 * Name of the Query Stored
	 */
	private String queryName = null;
	
	/**
	 * Query Stored
	 */
	private QueryModel query = null;
	
	/**
	 * Parameters for this query
	 */
	private QueryParameter parameters = null;

	/**
	 * @return the parameters
	 */
	public QueryParameter getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(QueryParameter parameters) {
		this.parameters = parameters.clone();
	}

	/**
	 * @return the queryName
	 */
	public String getQueryName() {
		return queryName;
	}

	/**
	 * @param queryName the queryName to set
	 */
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	/**
	 * @param queryObj model the queryModel to set
	 */
	public void setQuery(QueryModel queryObj) {
		this.query = queryObj;
	}

	/**
	 * @return the query model
	 */
	public QueryModel getQuery() {
		return this.query;
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return visual representation
	 */
	public String toString() {
		
		String repres = "";
		if (this.queryName != null) repres = this.queryName.toString() + " : ";
		if (this.parameters != null) repres += "[" + this.parameters.toString() + "]";
		
		return repres;
	}

}
