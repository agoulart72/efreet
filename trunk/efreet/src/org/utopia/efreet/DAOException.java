/*
 * $Id: DAOException.java,v 1.1.1.1 2005-03-21 00:59:58 agoulart Exp $
 */
package org.utopia.efreet;

import org.utopia.common.ResourceableException;

/**
 * Special exceptions throwed by the DAO with resources.
 */
public class DAOException extends ResourceableException {

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, String resourceMessage) {
        super(message,resourceMessage);
    }

    public DAOException(String message, String resourceMessage, Object param0) 
    {
        super(message,resourceMessage,param0);
    }

    public DAOException(String message, String resourceMessage, Object param0, Object param1) 
    {
        super(message,resourceMessage,param0,param1);
    }

    public DAOException(String message, String resourceMessage, Object param0, Object param1, Object param2) 
    {
        super(message,resourceMessage,param0,param1,param2);
    }

    public DAOException(String message, String resourceMessage, Object param0, Object param1, Object param2, Object param3) 
    {
        super(message,resourceMessage,param0,param1,param2,param3);
    }

}
