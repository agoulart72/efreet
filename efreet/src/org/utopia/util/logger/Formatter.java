/*
 * $Id: Formatter.java,v 1.1.1.1 2005-03-21 00:59:53 agoulart Exp $
 */
package org.utopia.util.logger;

import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.ResourceBundle;
import java.util.Arrays;
import java.text.DateFormat;

public class Formatter 
{
  // Constructors
  public Formatter() { }

  // Methods
  public String format(LogRecord logRecord) {
      
      String result = "";

      // Level
      if (logRecord.getLevel() != null)
	  result += logRecord.getLevel().toString();
      result += ": ";

      // Date / Time
      Calendar clnd = Calendar.getInstance();
      DateFormat df = DateFormat.getDateInstance();
      DateFormat tf = DateFormat.getTimeInstance();
      result += "[" + df.format(clnd.getTime()) + " " + tf.format(clnd.getTime()) + "] ";

      // Class/Method
      if (logRecord.getSourceClassName() != null) 
	  result += logRecord.getSourceClassName();
      if (logRecord.getSourceMethodName() != null) {
	  result += "." + logRecord.getSourceMethodName() + "(";
	  if (logRecord.getParameters() != null) {
	      Object[] prm = logRecord.getParameters();
	      int siz = Arrays.asList(prm).size();
	      for (int i=0; i < siz-1; i++) {
		  if (prm[i] != null)
		      result += prm[i].toString();
		  if (i < siz -2) result += ", ";
	      }
	  }
	  result += ") ";
      }
      
      // Formatted Message (uses the resource bundle to substitute "%key" in the message)
      if (logRecord.getMessage() != null) {
	  String msg = logRecord.getMessage();
	  if (logRecord.getResourceBundle() != null) {
	      ResourceBundle bundle = logRecord.getResourceBundle();
	      StringTokenizer msgTokenizer = new StringTokenizer(msg,"% ");
	      while (msgTokenizer.hasMoreTokens()) {
		  String msgPiece = msgTokenizer.nextToken();
		  if (msg.indexOf("%"+msgPiece) < 0) {
		      result += msgPiece + " ";		      
		  } else {
		      String msgSub = bundle.getString(msgPiece);
		      if (msgSub != null) result += msgSub + " ";
		  }
		      
	      }
	  } else {
	      result += msg;
	  }
      }

      // Throwable are treated directly in the handler class, does not need to be
      // formatted, since it's already formatted

      result += "\n";

      // Return the formatted message
      return result;
  }

}
