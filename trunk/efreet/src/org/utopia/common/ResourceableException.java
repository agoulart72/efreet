/*
 * $Id: ResourceableException.java,v 1.1.1.1 2005-03-21 01:00:08 agoulart Exp $
 */
package org.utopia.common;

/**
 * This Exception can handle parameters and an extra message, it's useful
 * for logging using a .properties file. <br>
 */
public class ResourceableException extends Exception {

    protected String resourceMessage = null;
    protected Object placeHolder[] = null;
    protected int placeHolderSize = 0;

    public ResourceableException(String message) {
        super(message);
    }

    public ResourceableException(String message, String resourceMessage) {
        super(message);
        this.resourceMessage = resourceMessage;
    }

    public ResourceableException(String message, String resourceMessage, Object param0) 
    {
        super(message);
        placeHolderSize = 1;
        placeHolder = new Object[1];
        placeHolder[0] = param0;
        this.resourceMessage = resourceMessage;
    }

    public ResourceableException(String message, String resourceMessage, Object param0, Object param1) 
    {
        super(message);
        placeHolderSize = 2;
        placeHolder = new Object[2];
        placeHolder[0] = param0;
        placeHolder[1] = param1;	
        this.resourceMessage = resourceMessage;
    }

    public ResourceableException(String message, String resourceMessage, Object param0, Object param1, Object param2) 
    {
        super(message);
        placeHolderSize = 3;
        placeHolder = new Object[3];
        placeHolder[0] = param0;
        placeHolder[1] = param1;	
        placeHolder[2] = param2;	
        this.resourceMessage = resourceMessage;
    }
    
    public ResourceableException(String message, String resourceMessage, Object param0, Object param1, Object param2, Object param3) 
    {
        super(message);
        placeHolderSize = 4;
        placeHolder = new Object[4];
        placeHolder[0] = param0;
        placeHolder[1] = param1;	
        placeHolder[2] = param2;	
        placeHolder[3] = param3;	
        this.resourceMessage = resourceMessage;
    }

    public String getResourceMessage() {
        return resourceMessage==null?"":resourceMessage;
    }

    public Object getParam(int index) {
        if (index < placeHolderSize) {
            return placeHolder[index];
        } else
            return null;
    }
}
