/*
 * $Id: Column.java,v 1.1.1.1 2005-03-21 00:59:58 agoulart Exp $
 */
package org.utopia.efreet;

import java.util.Calendar;
import java.sql.Types;

/**
 * Object to store the Data type of a column
 */
public class Column
{
    private String nome = null;
    private boolean required = false;
    private Object defaultObj = null;
    private String defaultString = null;
    private int typeCode = Types.NULL;
    private int precision = 0;
    private int scale = 0;

    /**
     * Construtor somente nome da coluna
     */
    public Column(String nomeColuna) 
    {
	this.nome = nomeColuna;
    }

    /**
     * Construtor sem parametro default
     */
    public Column(String nomeColuna, boolean required)
    {
	this(nomeColuna, required, null);
    }
    
    /**
     * Construtores com default
     */
    public Column(String nomeColuna, boolean required, Object vDef)
    {
	this.nome = nomeColuna;
	this.required = required;
	this.defaultObj = vDef;

	if (vDef != null) {
	    if (this.defaultObj instanceof java.lang.String) {
		this.defaultString = (String) vDef;
	    }
	}
    }

    public Column(String nomeColuna, boolean required, boolean vDef) 
    { this(nomeColuna, required, new Boolean(vDef)); }    
    public Column(String nomeColuna, boolean required, byte vDef) 
    { this(nomeColuna, required, new Byte(vDef)); }    
    public Column(String nomeColuna, boolean required, char vDef) 
    { this(nomeColuna, required, new Character(vDef)); }
    public Column(String nomeColuna, boolean required, short vDef) 
    { this(nomeColuna, required, new Short(vDef)); }    
    public Column(String nomeColuna, boolean required, int vDef) 
    { this(nomeColuna, required, new Integer(vDef)); }    
    public Column(String nomeColuna, boolean required, long vDef) 
    { this(nomeColuna, required, new Long(vDef)); }    
    public Column(String nomeColuna, boolean required, float vDef) 
    { this(nomeColuna, required, new Float(vDef)); }    
    public Column(String nomeColuna, boolean required, double vDef) 
    { this(nomeColuna, required, new Double(vDef)); }    

    /**
     * Basic Get Method
     */
    public Object get() 
    {
	return this.defaultObj;	    
    }

    /**
     * Format the Output of the Default Object in a format
     * that can quicly be translated to the SQL format
     */
    public String getDefault()
    {
	Object vDefault = this.defaultObj;

	if (vDefault == null) { return null; }
	if (this.defaultString != null) { return this.defaultString; }

	if ((vDefault instanceof Character) || 
	    (vDefault instanceof Boolean) || 
	    (vDefault instanceof String)) 
	    {
		return "'" + vDefault.toString() + "'";
	    } 
	  
	if ((vDefault instanceof java.util.Date) || 
	    (vDefault instanceof java.sql.Date)) 
	    {
		Calendar clnd = Calendar.getInstance();
		clnd.setTime((java.util.Date) vDefault);
		
		return "to_date('" + clnd.get(Calendar.DAY_OF_MONTH) + 
		    "/" + (clnd.get(Calendar.MONTH) + 1) + 
		    "/" + clnd.get(Calendar.YEAR) + 
		    "','DD/MM/YYYY')";
	    }
		
	return vDefault.toString();
    }

    public String getName() { return this.nome; }

    public boolean isRequired() { return this.required; }
    
    public boolean hasDefault() { return (get() != null); }

    public void setRequired(boolean value) { this.required = value; }
    public void setDefault(Object vDef) { 	
        this.defaultObj = vDef;
        if (vDef != null) {
            if (this.defaultObj instanceof java.lang.String) {
                this.defaultString = (String) vDef;
            }
        }
    }
    public void setTypeCode(int value) { this.typeCode = value; }
    public void setPrecision(int value) { this.precision = value; }
    public void setScale(int value) { this.scale = value; }

    public String toString() {
	return this.nome + "  " + (this.required? "NOT NULL":"NULL") 
             + "  " + (hasDefault()? getDefault(): "no default value");
    }
    
}
