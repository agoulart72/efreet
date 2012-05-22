/*
 * $Id: DAOException.java,v 1.6 2012-05-22 13:34:41 agoulart Exp $
 */
package org.utopia.efreet;

import org.utopia.common.ResourceableException;

/**
 * Special exceptions throwed by the DAO with resources.
 */
public class DAOException extends ResourceableException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2598247086148642895L;

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
