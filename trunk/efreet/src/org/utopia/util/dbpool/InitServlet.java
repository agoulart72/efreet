/*
 * $Id: InitServlet.java,v 1.1.1.1 2005-03-21 00:59:56 agoulart Exp $
 */
package org.utopia.util.dbpool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

/**
 * Este servlet inicializa o pool de conex&otilde;es com a base de dados. <p>
 * Ele deve ser chamado na inicializa&ccedil;&atilde;o da aplica&ccedil;&atilde;o. <p>
 * Ex : web.xml (Tomcat) <p>
 * <PRE>
 *    &lt;servlet&gt;
 *      &lt;servlet-name&gt;InitDBPool&lt;/servlet-name&gt;
 *      &lt;description&gt;Start the connection pool&lt;/description&gt;
 *      &lt;servlet-class&gt;br.com.open.util.dbpool.InitServlet&lt;/servlet-class&gt;
 *      &lt;init-param&gt;
 *        &lt;param-name&gt;connectionURL&lt;/param-name&gt;
 *        &lt;param-value&gt;jdbc:oracle:thin:@localhost:1521:SID1&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *      &lt;init-param&gt;
 *        &lt;param-name&gt;connectionUserName&lt;/param-name&gt;
 *        &lt;param-value&gt;mmm&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *      &lt;init-param&gt;
 *        &lt;param-name&gt;connectionPassword&lt;/param-name&gt;
 *        &lt;param-value&gt;xxx&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *      &lt;init-param&gt;
 *        &lt;param-name&gt;connectionsInitial&lt;/param-name&gt;
 *        &lt;param-value&gt;5&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *      &lt;init-param&gt;
 *        &lt;param-name&gt;connectionsIncrement&lt;/param-name&gt;
 *        &lt;param-value&gt;2&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *      &lt;init-param&gt;
 *        &lt;param-name&gt;databaseDriver&lt;/param-name&gt;
 *        &lt;param-value&gt;oracle.jdbc.driver.OracleDriver&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *      &lt;load-on-startup&gt;1&lt;/load-on-startup&gt;
 *    &lt;/servlet&gt;
 * </PRE>
 * <p>
 * Se estes parametros n&atilde;o estiverem dispon&iacute;veis na inicializa&ccedil;&atilde;o,
 * o servlet ira procurar um arquivo db.properties no diret&oacute;rio WEB-INF da 
 * aplica&ccedil;&atilde;o contendo os mesmos parametros.
 * 
 * @author Adriano M. Goulart
 * @version $Id: InitServlet.java,v 1.1.1.1 2005-03-21 00:59:56 agoulart Exp $
 */

public class InitServlet extends HttpServlet
{
    public InitServlet()
    {
    }

    /**
     * Fun&ccedil;&atilde;o chamada na inicializa&ccedil;&atilde;o <p>
     * Instancia um novo pool de conex&otilde;es.
     */
    public void init()
        throws ServletException
    {
	ServletConfig config = getServletConfig();
	ServletContext context = getServletContext();

        try {
            context.log("DBPool - Initialization Started");
            String path_resources = context.getRealPath("WEB-INF/");

	    // Recupera parametros de inicialização
	    String connectionURL = config.getInitParameter("connectionURL");
	    String connectionUserName = config.getInitParameter("connectionUserName");
	    String connectionPassword = config.getInitParameter("connectionPassword");
	    String databaseDriver = config.getInitParameter("databaseDriver");
	    String connectionInitial = config.getInitParameter("connectionsInitial");
	    int connInitial = 1;
	    String connectionIncrement = config.getInitParameter("connectionsIncrement");
	    int connIncrement = 1;

	    if (connectionInitial != null) 
		connInitial = Integer.parseInt(connectionInitial);
	    if (connectionIncrement == null)
		connIncrement = Integer.parseInt(connectionIncrement);

	    if ((connectionURL == null) || (connectionUserName == null) ||
		(connectionPassword == null) ||	(databaseDriver == null) ) {

		context.log ("Missing Parameter in DBPool Initialization");
		context.log ("Searching for a properties file");
	    }

	    // Inicializa conexoes com a base
	    ConnectionPool.instance().init(connectionURL, connectionUserName, connectionPassword, databaseDriver, connInitial, connIncrement, path_resources+"/db.properties");

            // Teste - Conexao com a base
	    DBStatement stmt = new DBStatement();
	    stmt.open("teste");
	    ResultSet set = stmt.executeQuery("SELECT SYSDATE FROM DUAL");
	    while (set.next()) context.log(set.getString(1));
	    set.close();
	    stmt.close();

            context.log("DBPool - Initialization Ends");
	    
        } catch (Exception exception) {
            context.log("DBPool - Initialization Error");
            context.log(exception.getMessage());
            throw new ServletException(exception.getMessage());
        }
    }


    /*
     * Fun&ccedil;&atilde;o chamada quando a aplica&ccedil;&atilde;o termina. <p>
     * Fecha todas as conex&otilde;es abertas com a base.
     */
    public void destroy()
    {
        getServletContext().log("DBPool - Closing connections");
        try {
	    ConnectionPool.instance().closePool();
	} catch(Throwable ex) {
	    getServletContext().log("Error closing connection pool");
	    getServletContext().log(ex.getMessage());
	}
    }

}
