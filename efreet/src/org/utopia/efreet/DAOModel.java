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
     * Name of the RDBMS table corresponding to this DAO
     */
    protected String tableName = null;
    
    /**
     * List of column objects corresponding to the columns of the
     * table this DAO represents
     */
    protected HashMap colunas = null;

    /**
     * List of all queries that can be executed by this DAO
     */
    protected HashMap queries = null;

    /**
     * Retrieve the table name
     * @return Table Name
     */
    public String getTableName() { return this.tableName; }
    /**
     * Set the table name
     * @param param Table Name
     */
    public void setTableName(String param) { this.tableName = param; }

    /**
     * Retrieve the structure containing the columns
     * @return HashMap of org.utopia.efreet.Column indexed by the name of the column 
     */
    public HashMap getColumns() { return this.colunas; }

    /**
     * Main function to add columns
     * @param newColumn Column to be added
     */
    public void addColumn(Column newColumn) {
        if (this.colunas == null) this.colunas = new HashMap();
        this.colunas.put(newColumn.getName(),newColumn);
    }

    /**
     * Adds a new Column with the specified values
     * @param columnName Name of the column
     * @param required true if column is required (NOT NULL)
     * @param vDef default value for this column
     */
    public void addColumn(String columnName, boolean required, Object vDef) {
        addColumn(new Column(columnName, required, vDef));
    }

    /**
     * Adds a new Column with the specified values
     * @param columnName Name of the column
     * @param required true if column is required (NOT NULL)
     */
    public void addColumn(String columnName, boolean required) { 
        addColumn(columnName, required, null); 
    }

    /**
     * Adds a new Column with the specified values
     * @param columnName Name of the column
     * @param required true if column is required (NOT NULL)
     * @param vDef default value for this column
     */
    public void addColumn(String columnName, boolean required, boolean vDef) { addColumn(columnName, required, new Boolean(vDef)); }
    /**
     * Adds a new Column with the specified values
     * @param columnName Name of the column
     * @param required true if column is required (NOT NULL)
     * @param vDef default value for this column
     */
    public void addColumn(String columnName, boolean required, byte vDef) { addColumn(columnName, required, new Byte(vDef)); }
    /**
     * Adds a new Column with the specified values
     * @param columnName Name of the column
     * @param required true if column is required (NOT NULL)
     * @param vDef default value for this column
     */
    public void addColumn(String columnName, boolean required, char vDef) { addColumn(columnName, required, new Character(vDef)); }
    /**
     * Adds a new Column with the specified values
     * @param columnName Name of the column
     * @param required true if column is required (NOT NULL)
     * @param vDef default value for this column
     */
    public void addColumn(String columnName, boolean required, short vDef) { addColumn(columnName, required, new Short(vDef)); }
    /**
     * Adds a new Column with the specified values
     * @param columnName Name of the column
     * @param required true if column is required (NOT NULL)
     * @param vDef default value for this column
     */
    public void addColumn(String columnName, boolean required, int vDef) { addColumn(columnName, required, new Integer(vDef)); }
    /**
     * Adds a new Column with the specified values
     * @param columnName Name of the column
     * @param required true if column is required (NOT NULL)
     * @param vDef default value for this column
     */
    public void addColumn(String columnName, boolean required, long vDef) { addColumn(columnName, required, new Long(vDef)); }
    /**
     * Adds a new Column with the specified values
     * @param columnName Name of the column
     * @param required true if column is required (NOT NULL)
     * @param vDef default value for this column
     */
    public void addColumn(String columnName, boolean required, float vDef) { addColumn(columnName, required, new Float(vDef)); }
    /**
     * Adds a new Column with the specified values
     * @param columnName Name of the column
     * @param required true if column is required (NOT NULL)
     * @param vDef default value for this column
     */
    public void addColumn(String columnName, boolean required, double vDef) { addColumn(columnName, required, new Double(vDef)); }


    /**
     * Adds a new query to the collection of queries for this model
     */
    public void addQuery(Query newQuery)  {
        if (this.queries == null) this.queries = new HashMap();
        this.queries.put(newQuery.getName(),newQuery);
    }

    /**
     * Retrieve query from the model
     */
    public Query getQuery(String name) {
        if (this.queries == null) return null;
        return (Query) this.queries.get(name);
    }

}
