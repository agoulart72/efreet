/*
 * $Id: Loggable.java,v 1.1.1.1 2005-03-21 00:59:54 agoulart Exp $
 */
package org.utopia.util.logger;

import java.io.*;
import java.util.*;

/**
 * Esta interface deve ser implementada por todos as classes que desejam
 * utilizar um log padrão dentro de uma aplicacao WEB.
 */

public interface Loggable {

    public static Level DEBUG = Level.DEBUG;
    public static Level TRACE = Level.TRACE;
    public static Level DETAIL = Level.DETAIL;
    public static Level CONFIG = Level.CONFIG;
    public static Level INFO = Level.INFO;
    public static Level WARNING = Level.WARNING;
    public static Level SEVERE = Level.SEVERE;

    public void log(String msg);
    public void log(Level level, String msg);
    public void log(Level level, String msg, Throwable thrown);
    
    // These ones should be implemented as :
    // public void log(String msg) { Logger.log(msg); }
    // public void log(Level level, String msg) { Logger.log(level, msg); }
    // public void log(Level level, String msg, Throwable thrown) { Logger.log(level, msg, thrown); }

    public void entering(String sourceClass, String sourceMethod);
    public void entering(String sourceClass, String sourceMethod, Object[] params);
    public void exiting(String sourceClass, String sourceMethod);

    // These ones should be implemented as :
    // public void entering(String sourceClass, String sourceMethod) { Logger.log(sourceClass, sourceMethod, null);}
    // public void entering(String sourceClass, String sourceMethod, Object[] params) { Logger.log(sourceClass, sourceMethod, params); }
    // public void exiting(String sourceClass, String sourceMethod) { Logger.log(sourceClass, sourceMethod, null); }

}

