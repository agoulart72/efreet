/*
 * $Id: Logger.java,v 1.1.1.1 2005-03-21 00:59:54 agoulart Exp $
 */
package org.utopia.util.logger;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Iterator;

/**
 * Implementation of the Logger to replace the Logger of jdk 1.4.1
 * to compile our module in jdk 1.3.1. It is much simpler than the 1.4.1 
 * logger version
 *
 * @author Adriano M. Goulart
 * @version $Id: Logger.java,v 1.1.1.1 2005-03-21 00:59:54 agoulart Exp $
 */

public class Logger 
{
    public static Logger uniqueInstance = null;

    private HashSet handlerSet = null;
    private Level level = null;
    
    /**
     * Protected method to construct a logger for a named subsystem
     */
    protected Logger() {
	this.handlerSet = new HashSet();
    }

    /**
     * Recovers the Instance of the singleton, or create one 
     * if this is the first time
     */
    public static Logger getLogger() {	
        if (uniqueInstance == null)
            uniqueInstance = new Logger();
        return uniqueInstance;
    }
    
    /**
     * Add a log handler to receive logging messages
     */
    public void addHandler(Handler handler) {
	handlerSet.add(handler);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * Flush to exit
     */
    public void flush() {
	if (handlerSet != null) {
	    Iterator it = handlerSet.iterator();
	    while (it.hasNext()) {
		Handler thisHandler = (Handler) it.next();
		if (thisHandler != null) thisHandler.flush();
	    }
	}
    }

    /**
     * Basic log method
     */
    public void doLog(Level level, 
		       String msg, 
		       Throwable throwable, 
		       String sourceClass, 
		       String sourceMethod, 
		       Object[] params) 
    {
	// Create a log record and fill in the parameters
	if (level == null) level = this.level;
	LogRecord logRecord = new LogRecord(level, msg);
	logRecord.setThrowable(throwable);
	logRecord.setSourceClassName(sourceClass);
	logRecord.setSourceMethodName(sourceMethod);
	logRecord.setParameters(params);
	// Publish the record on all of its handlers
	if (handlerSet != null) {
	    Iterator it = handlerSet.iterator();
	    while (it.hasNext()) {
		Handler thisHandler = (Handler) it.next();
		if (thisHandler != null) thisHandler.publish(this.level, logRecord);
	    }
	}
    }    

    /**
     * Basic Static Method
     */
    public static void log(Level level, 
			   String msg, 
			   Throwable throwable, 
			   String sourceClass, 
			   String sourceMethod, 
			   Object[] params) 
    {
	Logger thisLogger = getLogger();
	thisLogger.doLog(level, msg, throwable, sourceClass, sourceMethod, params);
    }

    /**
     * Convenience Methods
     */
    public static void log(Level level, String msg, Throwable throwable) 
    { log(level, msg, throwable, null, null, null); }
    public static void log(Level level, String msg) 
    { log(level,msg,null,null,null,null); }
    public static void log(String msg) 
    { log (null, msg, null,null,null,null); }

    /**
     * Convenience methods for starting and ending functions
     */
    public static void entering(String sourceClass, String sourceMethod, Object[] params) 
    {
	String msg = "- Enter ";
	log(Level.DEBUG, msg, null, sourceClass, sourceMethod, params);
    }
    public static void exiting(String sourceClass, String sourceMethod) 
    {
	String msg = "- Exit ";
	log(Level.DEBUG, msg, null, sourceClass, sourceMethod, null);
    }
    
}

