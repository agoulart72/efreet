/*
 * $Id: StreamHandler.java,v 1.1.1.1 2005-03-21 00:59:55 agoulart Exp $
 */
package org.utopia.util.logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;

public class StreamHandler extends Handler
{
    protected OutputStream output = null;

    protected StreamHandler() {}

    public StreamHandler(OutputStream out,Formatter fmt) 
    {
	this.output = out;
	this.formatter = fmt;
    }

    public void close() { 
	try { 
	    this.output.close(); 
	} catch(IOException ex) {
	    System.err.println(ex.getLocalizedMessage());
	} 
    }

    public void flush() { 
	try { 
	    this.output.flush(); 
	} catch(IOException ex) {
	    System.err.println(ex.getLocalizedMessage());
	} 
    }

    /**
     * This is the main implemented function of this method.
     */
    public void publish(Level maxlevel, LogRecord record) {
	String msg = null;
	if (this.formatter != null)
	    msg = this.formatter.format(record);
	else
	    msg = record.getMessage();

	if (maxlevel == null) {
	    if (record.getLevel() != null) {
		maxlevel = record.getLevel();
	    }
	}

	if (this.output != null) 
	{
	    try {
		this.output.write(msg.getBytes());
	    } catch(IOException ex) {
		System.err.println(ex.getLocalizedMessage());
	    } 
	
	    // Treat Throwable depending on the severity of the record
	    if (record.getThrowable() != null) {
		Throwable thr = record.getThrowable();
		if (maxlevel.exceptionInformation()) { 
		    String lmsg = thr.getLocalizedMessage();
		    if (lmsg != null) {
			try {
			    this.output.write(lmsg.getBytes()); 
			} catch(IOException ex) {
			    System.err.println(ex.getLocalizedMessage());
			} 
		    }
		}
		if (maxlevel.stackTraceInformation()) { 
		    thr.printStackTrace(new PrintStream(this.output)); 
		}
	    } // throwable != null
	} // output != null
    } // publish

}
