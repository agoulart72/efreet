/*
 * $Id: ManagerServlet.java,v 1.1.1.1 2005-03-21 00:59:56 agoulart Exp $
 */
package org.utopia.util.dbpool;

import java.util.*;
import java.text.*;
import java.lang.*;
import java.io.*;
import java.sql.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Este servlet implementa um serviço de gerenciamento das conexões no pool <p>
 * Desde que as conexões no pool tenha sido abertas com um label coenrete, este servlet
 * exibe todas as conexões que estão sendo utilizadas, e quantas estão disponíveis 
 * no pool.<p>
 * Este gerenciamento é util para verificar se todos os códigos que utilizam o pool
 * estão liberando as conexões corretamente, e detectar falhas na lógica do programa.<p>
 * Somente usuários pertencentes ao role "manager" podem ter acesso a este servlet.<br>
 * Para ativar o manager acrescente as seguintes linhas ao seu arquivo 
 * web.xml : <br>
 * <CODE>
<br>
   &lt;!-- Na secao de servlet --&gt;<br>
<br>
  &lt;servlet&gt;<br>
    &lt;servlet-name&gt;DBPoolManager&lt;/servlet-name&gt;<br>
    &lt;servlet-class&gt;br.com.open.util.dbpool.ManagerServlet&lt;/servlet-class&gt;<br>
  &lt;/servlet&gt;<br>
<br>
  &lt;!-- Na secao de Mappings --&gt;<br>
<br>
  &lt;servlet-mapping&gt;<br>
    &lt;servlet-name&gt;DBPoolManager&lt;/servlet-name&gt;<br>
    &lt;url-pattern&gt;/dbmanager&lt;/url-pattern&gt;<br>
  &lt;/servlet-mapping&gt;<br>
<br>
  &lt;!-- Na parte de seguranca --&gt;<br>
<br>
  &lt;security-constraint&gt;<br>
    &lt;web-resource-collection&gt;<br>
      &lt;web-resource-name&gt;Utopia&lt;/web-resource-name&gt;<br>
      &lt;url-pattern&gt;/dbmanager&lt;/url-pattern&gt;<br>
    &lt;auth-constraint&gt;<br>
      &lt;role-name&gt;manager&lt;/role-name&gt;<br>
    &lt;/auth-constraint&gt;<br>
  &lt;/security-constraint&gt;<br>
<br>
  &lt;login-config&gt;<br>
    &lt;auth-method&gt;BASIC&lt;/auth-method&gt;<br>
    &lt;realm-name&gt;UTOPIA&lt;/realm-name&gt;<br>
  &lt;/login-config&gt;<br>
<br>
  &lt;security-role&gt;<br>
    &lt;role-name&gt;manager&lt;/role-name&gt;<br>
  &lt;/security-role&gt;<br>
<br>
 *
 * </CODE>
 *
 * @author Adriano M. Goulart
 * @version $Id: ManagerServlet.java,v 1.1.1.1 2005-03-21 00:59:56 agoulart Exp $
 */

public class ManagerServlet extends HttpServlet 
{

    public ManagerServlet() { }

    protected void doPost (HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, java.io.IOException
    {
	if (!req.isUserInRole("manager")) {
	    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
	    return;
	}

	java.io.PrintWriter out = resp.getWriter();

	try {
	    ConnectionPool pool = ConnectionPool.instance();

	    out.write("<html>\n");
	    out.write("<body>\n");
	    out.write("<br>\n");
	    out.write("N&uacute;mero de Conex&otilde;es Livres no Pool : ");
	    out.write(pool.numberOfAvailableConnections()+"\n");
	    out.write("<br>\n");
	    out.write("N&uacute;mero de Conex&otilde;es em Uso no Pool : ");
	    out.write(pool.numberOfUsedConnections()+"\n");
	    out.write("<br>\n <hr>\n <br>\n\n");
	    
	    out.write("<table border=\"1\">\n");
	    out.write("<caption><b>Conex&otilde;es em Uso</b></caption>");
	    out.write("<tr>\n");
	    out.write("<td bgcolor=\"#ECECEC\" align=\"center\">Conex&atildeo</td>\n");
	    out.write("<td bgcolor=\"#ECECEC\">Status</td>\n");
	    out.write("<td bgcolor=\"#ECECEC\">R&oacute;tulo</td>\n");
	    out.write("</tr>\n\n");
	    
	    Iterator listaConex = pool.getConnectionsInUse().iterator();
	    
	    while (listaConex.hasNext()) {
		Connection con = (Connection) listaConex.next();
		String label = pool.getConnectionLabel(con);
		String state = "Aberta";
		if (con.isClosed()) state = "Fechada";
		
		out.write("<tr>\n");
		out.write("<td align=\"center\">"+con.hashCode()+"</td>\n");
		out.write("<td align=\"center\">"+state+"</td>\n");
		out.write("<td align=\"center\">"+label+"</td>\n");
		out.write("</tr>\n");

	    }

	    out.write("</table>\n");
	    out.write("</body>\n");
	    out.write("</html>\n");
	} catch (Exception ex) {
	    throw new ServletException(ex.getMessage());
	}
    }

    protected void doGet (HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, java.io.IOException
    {
	doPost (req, resp);
    }

}
