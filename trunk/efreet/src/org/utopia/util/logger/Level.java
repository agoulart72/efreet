/*
 * $Id: Level.java,v 1.1.1.1 2005-03-21 00:59:54 agoulart Exp $
 */
package org.utopia.util.logger;

public class Level
{
    // My patterns
    public static Level DEBUG = new Level("DEBUG", 0);
    public static Level TRACE = new Level("TRACE", 1);
    public static Level DETAIL = new Level("DETAIL", 2);
    public static Level CONFIG = new Level("CONFIG", 3);
    public static Level INFO = new Level("INFO", 4);
    public static Level WARNING = new Level("WARNING", 5);
    public static Level SEVERE = new Level("SEVERE", 6);

    public static Level ALL = new Level("ALL", 65535);
    public static Level OFF = new Level("OFF", -1);
    
    // Standard patterns from 1.4
    public static Level FINEST = new Level("FINEST", 0);
    public static Level FINER = new Level("FINER", 1);
    public static Level FINE = new Level("FINE", 2);
    
    private String name = null;
    private int value = 0;

    protected Level(String name, int value) {
	this.name = name;
	this.value = value;
    }

    public int intValue() { return this.value; }
    public String toString() { return this.name; }
    /** Returns true if this level requires that stack trace information must be exhibited */
    public boolean stackTraceInformation() { return this.value <= TRACE.intValue(); }
    /** Returns true if this level requires that exception information must be exhibited */
    public boolean exceptionInformation() { return this.value <= DETAIL.intValue(); }
}
