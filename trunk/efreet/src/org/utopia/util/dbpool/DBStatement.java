/*
 * $Id: DBStatement.java,v 1.1.1.1 2005-03-21 00:59:56 agoulart Exp $
 */
package org.utopia.util.dbpool;

import java.io.*;
import java.util.*;
import java.sql.*;

/**
 * Esta classe encapsula as fun��es de um statement de base de dados,
 * sem a necessidade de abrir e fechar a conex�o.<p>
 * Ela utiliza as conex�es do pool de conex�es. (com.hexacta.util.dbpool.ConnectionPool) <p>
 *
 * @author Adriano M. Goulart
 * @version $Id: DBStatement.java,v 1.1.1.1 2005-03-21 00:59:56 agoulart Exp $
 */
public class DBStatement {

    /**
     * Construtor simples, n�o abre a conex�o de base. <p>
     * Serve para os casos, em que se deseja utilizar o mesmo objeto ao
     * longo de v�rias utiliza��es sem que a conex�o do pool de conex�es
     * esteja em estado prolongado de utiliza��o.
     */
    public DBStatement() {
    }

    /**
     * Construtor j� abre pega a conex�o do pool na inicializa��o.
     */
    public DBStatement(String owner) 
        throws Exception, SQLException
    {
        thisConnection = ConnectionPool.instance().getConnection(owner);
        thisStmt = thisConnection.createStatement();
    }

    /**
     * Pega uma conex�o com a base, caso tenha sido usado o construtor mais simples
     */
    public void open(String owner)
        throws Exception, SQLException
    {
        thisConnection = ConnectionPool.instance().getConnection(owner);
        thisStmt = thisConnection.createStatement();
    }

    /**
     * Encerra o statement e retorna a conex�o ao pool
     */
    public void close()
        throws Exception, SQLException
    {
        thisStmt.close();
        ConnectionPool.instance().freeConnection(thisConnection);
    }

    /**
     * Connection Methods
     */
    public void commit()
        throws SQLException
    {
        thisConnection.commit();
    }

    /**
     * Connection Methods
     */
    public void rollback()
        throws SQLException
    {
        thisConnection.rollback();
    }

    /**
     * Statement Query Methods
     */
    public boolean execute(String sql)
        throws SQLException
    {
        return thisStmt.execute(sql);
    }

    /**
     * Statement Query Methods
     */
    public ResultSet executeQuery(String sql)
        throws SQLException
    {
        return thisStmt.executeQuery(sql);
    }

    /**
     * Statement Query Methods
     */
    public int executeUpdate(String sql)
        throws SQLException
    {
        return thisStmt.executeUpdate(sql);
    }

    /**
     * Statement Query Methods
     * Batch Statement Methods
     */
    public void addBatch (String sql)
        throws SQLException
    {
        thisStmt.addBatch(sql);
    }

    /**
     * Statement Query Methods
     * Batch Statement Methods
     */
    public void clearBatch()
        throws SQLException
    {
        thisStmt.clearBatch();
    }

    /**
     * Statement Query Methods
     * Batch Statement Methods
     */
    public int[] executeBatch()
        throws SQLException
    {
        return thisStmt.executeBatch();
    }


    /**
     * Statement Query Methods
     * Set Methods
     */
    public void setMaxRows(int max)
        throws SQLException
    {
        thisStmt.setMaxRows(max);
    }

    /**
     * Caso seja necess�rio alguma fun��o mais espec�fica ainda n�o implementada
     * nesta classe, esta fun��o permite recuperar o java.sql.Statement que est�
     * aberto.<p>
     * <b> Perigo !!! </b>
     * Tenha cuidado ao usar o resultado desta fun��o, pois pode causar
     * problemas ao pool de conex�es.
     */
    public Statement getStatement() 
    {
	return thisStmt;
    }

    /**
     * Caso seja necess�rio alguma fun��o mais espec�fica ainda n�o implementada
     * nesta classe, esta fun��o permite recuperar o java.sql.Connection que est�
     * aberto.<p>
     * <b> Perigo !!! </b>
     * Tenha cuidado ao usar o resultado desta fun��o, pois pode causar
     * problemas ao pool de conex�es.
     */
    public Connection getConnection() 
    {
	return thisConnection;
    }

    private Statement thisStmt = null;
    private Connection thisConnection = null;
}
