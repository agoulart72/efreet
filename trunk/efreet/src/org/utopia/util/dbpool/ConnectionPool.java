/*
 * $Id: ConnectionPool.java,v 1.1.1.1 2005-03-21 00:59:55 agoulart Exp $
 */
package org.utopia.util.dbpool;

import java.io.*;
import java.util.*;
import java.sql.*;

/**
 * Esta classe representa uma instancia de um pool de conex�es. <p>
 * Ela � geralmente inicializada junto com a aplica��o para prover
 * conex�es com a base de dados.
 *
 * @author Adriano M. Goulart
 * @version $Id: ConnectionPool.java,v 1.1.1.1 2005-03-21 00:59:55 agoulart Exp $
 */

public class ConnectionPool {

    /**
     * Construtor da instancia.
     */
    public ConnectionPool() {
        available = new ArrayList();
        inUse = new HashMap();
        inUseLabel = new HashMap();
        idCon = 0;
    }

    /**
     * Prepara o pool de conex�es, estabelecendo conex�es com a base de dados. <p>
     * Esta varia��o recebe o nome de um arquivo de properties, de onde vai extrair
     * as informa��es relevantes para estabelecer as conex�es.
     *
     * @param propFile nome do arquivo de properties
     */
    public void init(String propFile)
        throws Exception
    {
        loadConfig(propFile);
        DriverManager.registerDriver((Driver)Class.forName(databaseDriver).newInstance());
        grow (connInitial);
    }

    /**
     * Prepara o pool de conex�es, estabelecendo conex�es com a base de dados. <p>
     * Esta varia��o recebe todos os parametros necess�rios para estabelecer uma
     * conex�o. 
     *
     * @param conURL URL da conex�o (jdbc)
     * @param conName Nome do usuario de acesso a base
     * @param conPasswd Senha do usuario de acesso a base
     * @param dbDriver nome da classe driver provida pelo fornecedor da base
     * @param conInit numero de conex�es inicial
     * @param conInc numero de novas conex�es a serem abertas caso estejam todas em uso
     * @param propFile nome do arquivo de properties (caso os outros parametros n�o estejam bem definidos.
     */
    public void init(String conURL, String conName, String conPasswd, String dbDriver, int conInit, int conInc, String propFile)
	throws Exception
    {
	// Se os parametros n�o foram declarados, inicia o pool usando 
	// arquivo de properties chamado db.properties
	if ((conURL == null) || (conName == null) || (conPasswd == null) || (dbDriver == null)) {
	    init(propFile);
	} else {
	    connectionURL = conURL;
	    connectionUserName = conName;
	    connectionPassword = conPasswd;
	    connInitial = conInit;
	    connIncrement = conInc;
	    DriverManager.registerDriver((Driver)Class.forName(dbDriver).newInstance()); 
	    grow (connInitial);
	}
    }

    /**
     * Recebe o arquivo de properties, e extrai informa��es
     */
    public void loadConfig(String prop_file)
        throws Exception
    {
        FileInputStream in = new FileInputStream(prop_file);
        Properties configfile = new Properties();
        configfile.load(in);
        in.close();
        if(!configfile.containsKey("connectionURL"))
            throw new Exception("connectionURL Missing in db.properties");
        connectionURL = configfile.getProperty("connectionURL");
        if(!configfile.containsKey("connectionUserName"))
            throw new Exception("connectionUserName Missing in db.properties");
        connectionUserName = configfile.getProperty("connectionUserName");
        if(!configfile.containsKey("connectionPassword"))
            throw new Exception("connectionPassword Missing in db.properties");
        connectionPassword = configfile.getProperty("connectionPassword");
        connInitial = 1;
        if(configfile.containsKey("connectionsInitial"))
            connInitial = Integer.parseInt(configfile.getProperty("connectionsInitial"));
        connIncrement = 1;
        if(configfile.containsKey("connectionsIncrement"))
            connIncrement = Integer.parseInt(configfile.getProperty("connectionsIncrement"));
        databaseDriver = "oracle.jdbc.driver.OracleDriver";
        if(configfile.containsKey("driverName"))
            databaseDriver = configfile.getProperty("driverName");
    }

    /**
     * Devolve a instancia est�tica do pool de conex�es
     */
    public static ConnectionPool instance()
    {
        if (uniqueInstance == null)
            uniqueInstance = new ConnectionPool();
        return uniqueInstance;
    }

    /**
     * Devolve uma conex�o com a base de dados
     *
     * @return Conex�o 
     */
    public synchronized Connection getConnection()
        throws SQLException
    {
        return getConnection("Unknown");
    }

    /**
     * Devolve uma conex�o com a base de dados. <p>
     * Para efeitos de debug, armazena uma string junto com a conex�o,
     * para que o gerenciamento tenha alguma forma de reconhecer a conex�o. <p>
     * Pode ser passado o nome da classe, ou nome da p�gina jsp que
     * que estabeleceu a conex�o.
     *
     * @param label String que ser� armazenada junto com a conex�o
     * @return Conex�o
     */
    public synchronized Connection getConnection(String label)
        throws SQLException
    {
        if (available.isEmpty()) grow (connIncrement);
	Connection conx = null;
	while ((conx == null) || (conx.isClosed())) {
	    conx = (Connection) available.remove(0);
	    inUse.put(conx,conx);
	    inUseLabel.put(conx,label);
	}
        return conx;
    }

    /**
     * Encerra a utiliza��o de uma conex�o. <p>
     * A conex�o � colocada de volta ao pool de conex�es dispon�veis.
     *
     * @param conx Conex�o
     */
    public synchronized void freeConnection(Connection conx)
        throws Exception
    {
        if (!inUse.containsKey(conx)) {
            throw new Exception ("Unknown DBConnection");
        } else {
            inUse.remove(conx);
            inUseLabel.remove(conx);
	    if ((conx != null) && (! conx.isClosed())) 
		available.add(conx);
        }
    }

    /**
     * Incrementa o pool de um certo n�mero de conex�es. <p>
     *
     * @param siz Quantidade de novas conex�es a serem abertas no pool
     */
    private synchronized void grow (int siz)
        throws SQLException
    {
        for (; siz !=0 ; siz--) {
            Connection conx = DriverManager.getConnection(connectionURL, connectionUserName, connectionPassword);
            conx.setAutoCommit(false);
            idCon ++;
            available.add(conx);
        }
    }

    /**
     * @return Quantidade de conex�es que est�o em uso
     */
    public int numberOfUsedConnections() {
        return inUse.size();
    }

    /**
     * @return Quantidade de conex�es livres
     */
    public int numberOfAvailableConnections() {
        return available.size();
    }
    
    /**
     * Para ser utilizado pelo manager, devolve uma lista 
     * com todas as conex�es abertas.
     * <b>Perigo !!!</b>
     * O uso indiscriminado deste m�todo pode causar problemas no funcionamento do pool.
     *
     * @return lista de conex�es
     */
    public java.util.Set getConnectionsInUse() {
	return inUse.keySet();
    }

    /**
     * Para ser utilizado pelo manager, devolve uma lista do r�tulo
     * de uma conex�o abertas.
     * @return label
     */
    public String getConnectionLabel(Connection con) {
	return (String) inUseLabel.get(con);
    }


    /**
     * Encerra o pool, fechando todas as conex�es.
     */
    protected void finalize()
	throws Throwable
    {

	closePool();
	super.finalize();

    }

    /**
     * Encerra o pool, fechando todas as conex�es.
     */
    public void closePool()
	throws Throwable
    {
	if (!inUse.isEmpty()) {
	    Object[] allKeys = inUse.keySet().toArray();
	    for (int i=0; i<inUse.size(); i++) {
		inUse.remove(allKeys[i]);
	    }
	}

	while (!available.isEmpty()) {
	    Connection conx = (Connection) available.remove(0);
	    conx.close();
	}
    }

    public static ConnectionPool uniqueInstance = null;
    private ArrayList available;
    private HashMap inUse;
    private HashMap inUseLabel;
    private int idCon;

    private int connInitial;
    private int connIncrement;

    private String databaseDriver;
    private String connectionURL;
    private String connectionUserName;
    private String connectionPassword;

}
