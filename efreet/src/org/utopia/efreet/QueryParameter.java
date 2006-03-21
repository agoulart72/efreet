package org.utopia.efreet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * This class is used to pass parameters for the query
 */
public class QueryParameter {

	private Vector values = null;
	private Hashtable variables = null;
	
	/**
	 * Main method to add parameters
	 * @param value object containing the value
	 */
	public void add(Object value) {
		// Instancia Vetor de Valores deste Objeto
		if (this.values == null) values = new Vector();

		if (value == null) value = new NullObject(); 
		
		values.add(value);
	}
	
    /**
     * Set Methods
     */
    public void add(boolean param) { add(new Boolean(param)); }
    public void add(byte param)    { add(new Byte(param)); }
    public void add(char param)    { add(new Character(param)); }
    public void add(short param)   { add(new Short(param)); }
    public void add(int param)     { add(new Integer(param)); }
    public void add(long param)    { add(new Long(param)); }
    public void add(float param)   { add(new Float(param)); }
    public void add(double param)  { add(new Double(param)); }

    /**
     * Allows you to add a string parameter as a boolean
     * where true = "TRUE","true","Y","y","T","t","S","s"
     * and false is anything else, including null
     * @param param string value
     */
    public void addBoolean(String param) {
    	if (param != null) {
    		if ("true".equalsIgnoreCase(param) ||
    			"y".equalsIgnoreCase(param) ||
    			"t".equalsIgnoreCase(param) ||
    			"s".equalsIgnoreCase(param)) {
    			add (new Boolean(true));
    		} else {
        		add(new Boolean(false));
    		}
    	} else {
    		add(new Boolean(false));
    	}
    }

    /**
     * Allows you to add a string as a byte parameter
     * @param param string
     */
    public void addByte(String param) throws ParseException {
    	if (param == null) add(param); else add (new Byte(param));
    }
    /**
     * Allows you to add a string as a Char parameter
     * gets only the first char of the string 
     * @param param string
     */
    public void addChar(String param) { 
    	if (param == null) add(param); else	
    	add(new Character(param.charAt(0))); 
    }
    /**
     * Allows you to add a string as a short parameter
     * @param param string
     */
    public void addShort(String param) throws ParseException { 
    	if (param == null) add(param); else	add(new Short(param)); 
    }
    /**
     * Allows you to add a string as an int parameter
     * @param param string
     */
    public void addInt(String param) throws ParseException { 
    	if (param == null) add(param); else	add(new Integer(param)); 
    }
    /**
     * Allows you to add a string as a long parameter
     * @param param string
     */
    public void addLong(String param) throws ParseException { 
    	if (param == null) add(param); else	add(new Long(param)); 
    }
    /**
     * Allows you to add a string as a float parameter
     * @param param string
     */
    public void addFloat(String param) throws ParseException { 
    	if (param == null) add(param); else	add(new Float(param)); 
    }
    /**
     * Allows you to add a string as a double parameter
     * @param param string
     */
    public void addDouble(String param) throws ParseException { 
    	if (param == null) add(param); else	add(new Double(param)); 
    }
    
    /**
     * Allows you to add a date parameter as a string
     * The format used is the same as for the SimpleDateFormat :
     * <br />
     * <table border=0 cellspacing=3 cellpadding=0>
<tr bgcolor="#ccccff"><th>Letter</th><th>Date or Time Component</th><th>Presentation</th><th>Examples</th></tr>  
<tr><td>G</td><td>Era designator</td><td>Text</td><td>AD</td></tr>  
<tr bgcolor="#eeeeff"><td>y</td><td>Year</td><td>Year</td><td>1996; 96</td></tr>  
<tr><td>M</td><td>Month in year</td><td>Month</td><td>July; Jul; 07</td></tr>  
<tr bgcolor="#eeeeff"><td>w</td><td>Week in year</td><td>Number</td><td>27</td></tr>  
<tr><td>W</td><td>Week in month</td><td>Number</td><td>2</td></tr>  
<tr bgcolor="#eeeeff"><td>D</td><td>Day in year</td><td>Number</td><td>189</td></tr>  
<tr><td>d</td><td>Day in month</td><td></td><td>Number</td><td>10</td></tr>  
<tr bgcolor="#eeeeff"><td>F</td><td>Day of week in month</td><td>Number</td><td>2</td></tr>  
<tr><td>E</td><td>Day in week</td><td>Text</td><td>Tuesday; Tue</td></tr>  
<tr bgcolor="#eeeeff"><td>a</td><td>Am/pm marker</td><td>Text</td><td>PM</td></tr>  
<tr><td>H</td><td>Hour in day (0-23)</td><td>Number</td><td>0</td></tr>  
<tr bgcolor="#eeeeff"><td>k</td><td>Hour in day (1-24)</td><td>Number</td><td>24</td></tr>  
<tr><td>K</td><td>Hour in am/pm (0-11)</td><td>Number</td><td>0</td></tr>  
<tr bgcolor="#eeeeff"><td>h</td><td>Hour in am/pm (1-12)</td><td></td><td>Number</td><td>12</td></tr>  
<tr><td>m</td><td>Minute in hour</td><td>Number</td><td>30</td></tr>  
<tr bgcolor="#eeeeff"><td>s</td><td>Second in minute</td><td>Number</td><td>55</td></tr>  
<tr><td>S</td><td>Millisecond</td><td>Number</td><td>978</td></tr>  
<tr bgcolor="#eeeeff"><td>z</td><td>Time zone</td><td>General time zone</td><td>Pacific Standard Time; PST; GMT-08:00</td></tr>  
<tr><td>Z</td><td>Time zone</td><td>RFC 822 time zone</td><td>-0800</td></tr>  
</table>
	 * <br />
     * @param param string
     * @param format format
     */
    public void addDate(String param, String format) throws ParseException {
    	if (param == null) add(param); else {
    		SimpleDateFormat sdft = new SimpleDateFormat(format);
    		Date value = sdft.parse(param);
    		add(value);
    	}
    }
    
    /**
     * Set a variable for the query
     * @param key
     * @param value
     */
    public void setVariable(String key, String value) {
    	if (key != null) {
    		if (variables == null) {
    			variables = new Hashtable();
    		}
    		if (value != null) {
    			variables.put(key, value);
    		} else {
    			variables.put(key, "");
    		}
    	}
    }

    /**
     * Return the Variables in a Hashtable
     * @return Hashtable of variables
     */
    public Hashtable getVariables() {
    	return this.variables;
    }
    
	/**
	 * Return the Values of the parameters in a object list style
	 * @return object array
	 */
	public Object[] getParameters() {
		if (values == null) return null;
		Object[] ret = new Object[values.size()];
		for (int c=0; c < values.size(); c++) {
			Object tmp = values.get(c);
			if (tmp instanceof NullObject) {
				ret[c] = null;
			} else {
				ret[c] = tmp;
			}
		}
		return ret;
	}

	/**
	 * Defined to avoid the error of putting null into a vector
	 */
	private class NullObject { }

}
