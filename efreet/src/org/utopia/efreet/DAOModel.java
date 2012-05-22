package org.utopia.efreet;

import java.util.HashMap;

/**
 * This class is a model for a DAO, containing the columns and
 * queries required by the DAO.<br>
 * The model is created automatically by the factory by reading the
 * XML file corresponding to the DAO.
 */
public class DAOModel
{
    /**
     * List of all queries that can be executed by this DAO
     */
    protected HashMap<String, QueryModel> queries = null;

    protected String dataSource = null;
    
    protected String name = null;
    
    /**
     * Adds a new query to the collection of queries for this model
     */
    public void addQuery(QueryModel newQuery)  {
        if (this.queries == null) this.queries = new HashMap<String, QueryModel>();
        this.queries.put(newQuery.getName(),newQuery);
    }

    /**
     * Retrieve query from the model
     */
    public QueryModel getQuery(String name) {
        if (this.queries == null) return null;
        return (QueryModel) this.queries.get(name);
    }

    /**
     * Retrieve the JNDI datasource name
     * @return datasource name
     */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * Sets the JNDI for the datasource name
	 * @param dataSource name
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
    
}
