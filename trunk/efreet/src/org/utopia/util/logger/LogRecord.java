/*
 * $Id: LogRecord.java,v 1.1.1.1 2005-03-21 00:59:54 agoulart Exp $
 */
package org.utopia.util.logger;

import java.util.ResourceBundle;

public class LogRecord
{
    private Level level = null;
    private String message = null;
    private Object[] parameters = { null };
    private ResourceBundle resourceBundle = null;
    private String sourceClassName = null;
    private String sourceMethodName = null;
    private int threadID = 0;
    private Throwable throwable = null;

    public LogRecord(Level level, String msg) {
	this.level = level;
	this.message = msg;
    }

    public void setLevel(Level level) { this.level = level; }
    public void setMessage(String msg) { this.message = msg; }  
    public void setParameters(Object[] parameters) { this.parameters = parameters; } 
    public void setResourceBundle(ResourceBundle resourceBundle) { this.resourceBundle = resourceBundle; }  
    public void setSourceClassName(String sourceClassName) { this.sourceClassName = sourceClassName; }  
    public void setSourceMethodName(String sourceMethodName) { this.sourceMethodName = sourceMethodName; }  
    public void setThreadID(int threadID) { this.threadID = threadID; }  
    public void setThrowable(Throwable throwable) { this.throwable = throwable; }      

    public Level getLevel() { return this.level; }
    public String getMessage() { return this.message; }  
    public Object[] getParameters() { return this.parameters; } 
    public ResourceBundle getResourceBundle() { return this.resourceBundle; }  
    public String getSourceClassName() { return this.sourceClassName; }  
    public String getSourceMethodName() { return this.sourceMethodName; }  
    public int getThreadID() { return this.threadID; }  
    public Throwable getThrowable() { return this.throwable; }  

}

