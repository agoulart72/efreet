/*
 * $Id: InitServlet.java,v 1.1.1.1 2005-03-21 00:59:54 agoulart Exp $
 */
package org.utopia.util.logger;

import java.util.*;
import java.io.FileOutputStream;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Este servlet inicializa um logger especifico para a aplica&ccedil;&atilde;o. <p>
 * Ele deve ser chamado na inicializa&ccedil;&atilde;o da aplica&ccedil;&atilde;o. <p>
 * Ex : web.xml <p>
 * <PRE>
 *    &lt;servlet&gt;
 *      &lt;servlet-name&gt;InitLogger&lt;/servlet-name&gt;
 *      &lt;description&gt;Start the Logger&lt;/description&gt;
 *      &lt;servlet-class&gt;br.com.open.util.logger.InitServlet&lt;/servlet-class&gt;
 *      &lt;init-param&gt;
 *        &lt;param-name&gt;logFile&lt;/param-name&gt;
 *        &lt;param-value&gt;myApplication.log&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *      &lt;init-param&gt;
 *        &lt;param-name&gt;logLevel&lt;/param-name&gt;
 *        &lt;param-value&gt;INFO&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *      &lt;init-param&gt;
 *        &lt;param-name&gt;resources&lt;/param-name&gt;
 *        &lt;param-value&gt;br.com.application.resource&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 * </PRE>
 * Parametros : <p>
 * logFile  : Nome do arquivo de Log, completo com path absoluto
 * <ul> logLevel : Nivel de exibição do Log, um dos seguintes
 *   <li><i>DEBUG</i></li>
 *   <li><i>TRACE</i></li>
 *   <li><i>DETAIL</i></li>
 *   <li><i>CONFIG</i></li>
 *   <li><i>INFO</i></li>
 *   <li><i>WARNING</i></li>
 *   <li><i>SEVERE</i></li>
 * </ul>
 * <p>
 * resources : properties para exibição de log
 *
 * @author Adriano M. Goulart
 * @version $Id: InitServlet.java,v 1.1.1.1 2005-03-21 00:59:54 agoulart Exp $
 */

public class InitServlet extends HttpServlet
{
    public InitServlet()
    {
    }

    /** 
     * Fun&ccedil;&atilde;o chamada na inicializa&ccedil;&atilde;o <p>
     * Cria um novo Logger 
     */
    public void init()
        throws ServletException
    {
	ServletConfig config = getServletConfig();
	ServletContext context = getServletContext();

        try {
	    // Recupera parametros de inicialização
	    String logFile = config.getInitParameter("logFile");
	    String logLevel = config.getInitParameter("logLevel");
	    String resourceBundleName = config.getInitParameter("resources");

	    Logger myLogger = Logger.getLogger();

	    if (logLevel != null) {
		Level nivel = null;
		if (logLevel.equals("DEBUG"   )) nivel = Level.FINEST ; else
		if (logLevel.equals("TRACE"   )) nivel = Level.FINER ; else
		if (logLevel.equals("DETAIL"  )) nivel = Level.FINE ; else
		if (logLevel.equals("CONFIG"  )) nivel = Level.CONFIG ; else
		if (logLevel.equals("INFO"    )) nivel = Level.INFO ; else
		if (logLevel.equals("WARNING" )) nivel = Level.WARNING ; else
		if (logLevel.equals("SEVERE"  )) nivel = Level.SEVERE ; else
		   nivel = Level.INFO;
		
		myLogger.setLevel(nivel);
	    }

	    if (logFile != null) {
		FileHandler fileHandler = new FileHandler(logFile, new Formatter());
		if (resourceBundleName != null) {
		    try {
			fileHandler.setResourceBundle(ResourceBundle.getBundle(resourceBundleName));
		    } catch(Exception ex) {
			context.log("Logger - cannot find Resource Bundle: " + resourceBundleName);
		    }
		}
		myLogger.addHandler(fileHandler);
	    }

            context.log("Logger - Initialized");

        } catch (Exception exception) {
            context.log("Logger - Initialization Error");
            context.log(exception.getMessage());
            throw new ServletException(exception.getMessage());
        }
    }

    /*
     * Fun&ccedil;&atilde;o chamada quando a aplica&ccedil;&atilde;o termina. <p>
     */
    public void destroy()
    {
	try {
	    Logger myLogger = Logger.getLogger();
	    myLogger.flush();
	} catch(Throwable ex) {
	    getServletContext().log("Error finishing Logger");
	    getServletContext().log(ex.getMessage());
	}
    }	
}
