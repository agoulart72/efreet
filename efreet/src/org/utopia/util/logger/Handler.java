/*
 * $Id: Handler.java,v 1.1.1.1 2005-03-21 00:59:53 agoulart Exp $
 */
package org.utopia.util.logger;

import java.util.ResourceBundle;

public abstract class Handler
{
    private static String defaultResourceBundleName = "logging.properties";
    protected Formatter formatter = null;
    protected ResourceBundle resourceBundle = null;
    protected Level level = null;

    protected Handler()  {}

    public abstract void close();
    public abstract void flush();
    public abstract void publish(Level maxlevel, LogRecord record);

    public Formatter getFormatter() { return this.formatter; }
    public Level getLevel() { return this.level; }

    public void setFormatter(Formatter formatter) { this.formatter = formatter; }
    public void setLevel(Level level) {	this.level = level; }
    public void setResourceBundle(ResourceBundle resourceBundle) { this.resourceBundle = resourceBundle; }
    
    public boolean isLoggable(LogRecord record) {
	return record.getLevel().intValue() >= this.level.intValue();
    }
}
