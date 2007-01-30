package org.utopia.efreet;

import java.sql.Timestamp;
import java.util.HashMap;

import java.math.BigDecimal;
import java.math.BigInteger;

//import java.util.Collection;
//import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;

/**
 * This class represents a single result row from a resultset
 * to be exchanged between DAOs and BLObs
 */
public class QueryResult
{
	/**
	 * WARNING !!! Do not change the values in this hashmap
	 */
    protected HashMap values = null;

    /**
     * Altera Valores de Colunas
     */
    public void set(String nomeColuna, Object valor) 
	throws DAOException
    {
	// Instancia Vetor de Valores deste Objeto
	if (this.values == null) values = new HashMap();

	values.put(nomeColuna, valor);
    }

    /**
     * Set Methods
     */
    public void set(String nomeColuna, boolean param) throws DAOException { set(nomeColuna, new Boolean(param)); }
    public void set(String nomeColuna, byte param) throws DAOException  { set(nomeColuna, new Byte(param)); }
    public void set(String nomeColuna, char param) throws DAOException  { set(nomeColuna, new Character(param)); }
    public void set(String nomeColuna, short param) throws DAOException  { set(nomeColuna, new Short(param)); }
    public void set(String nomeColuna, int param) throws DAOException  { set(nomeColuna, new Integer(param)); }
    public void set(String nomeColuna, long param) throws DAOException  { set(nomeColuna, new Long(param)); }
    public void set(String nomeColuna, float param) throws DAOException  { set(nomeColuna, new Float(param)); }
    public void set(String nomeColuna, double param) throws DAOException  { set(nomeColuna, new Double(param)); }

    /**
     * Recupera Valores de Coluna
     */
    public Object get(String nomeColuna)
	throws DAOException
    {
	Object retorno = null;

	// Se o valor nao foi setado, retorna null
       	if ((this.values == null) || 
	    (! this.values.containsKey(nomeColuna)) )
	    {
		retorno = null;
	    } else {
		retorno = this.values.get(nomeColuna);
	    }

	return retorno;
    }

    /**
     * Specific Get Methods
     */
    public String getString(String nomeColuna) throws DAOException 
    { 
	Object retorno = get(nomeColuna);
	if (retorno == null) return "";
	return retorno.toString().trim(); 
    }
    public boolean getBoolean(String nomeColuna) throws DAOException 
    { 
	Object retorno = get(nomeColuna);
	if (retorno == null) return false;
	if (!(retorno instanceof Boolean)) throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	return ((Boolean)retorno).booleanValue(); 	    
    }
    public byte getByte(String nomeColuna) throws DAOException 
    { 
	Object retorno = get(nomeColuna);
	if (retorno == null) return 0;
	if (!(retorno instanceof Byte)) throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	return ((Byte)retorno).byteValue(); 
    }
    public char getChar(String nomeColuna) throws DAOException 
    { 
	Object retorno = get(nomeColuna);
	if (retorno == null) return ' ';
	if (!(retorno instanceof Character)) throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	return ((Character)retorno).charValue(); 
    }
    public short getShort(String nomeColuna) throws DAOException 
    { 
	Object retorno = get(nomeColuna);
	if (retorno == null) return 0;
	if (!(retorno instanceof Short)) throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	return ((Short)retorno).shortValue(); 
    }
    public int getInt(String nomeColuna) throws DAOException 
    { 	
	Object retorno = get(nomeColuna);
	if (retorno == null) return 0;
	if (retorno instanceof BigDecimal) return ((BigDecimal)retorno).intValue(); 
	if (retorno instanceof BigInteger) return ((BigInteger)retorno).intValue(); 
	if ((retorno instanceof Character) || (retorno instanceof String)) {
	    try {
		return Integer.parseInt(retorno.toString().trim());
	    } catch(NumberFormatException nfex) {
		throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	    }
	}
	if (!(retorno instanceof Integer)) throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	return ((Integer)retorno).intValue(); 
    }
    public long getLong(String nomeColuna) throws DAOException 
    { 
	Object retorno = get(nomeColuna);
	if (retorno == null) return 0;
	if (retorno instanceof BigDecimal) return ((BigDecimal)retorno).longValue(); 
	if (retorno instanceof BigInteger) return ((BigInteger)retorno).longValue(); 
	if ((retorno instanceof Character) || (retorno instanceof String)) {
	    try { 
		return Long.parseLong(retorno.toString().trim());
	    } catch(NumberFormatException nfex) {
		throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	    }
	}
	if (!(retorno instanceof Long)) throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	return ((Long)retorno).longValue(); 
    }
    public float getFloat(String nomeColuna) throws DAOException 
    { 
	Object retorno = get(nomeColuna);
	if (retorno == null) return 0;
	if (retorno instanceof BigDecimal) return ((BigDecimal)retorno).floatValue(); 
	if (retorno instanceof BigInteger) return ((BigInteger)retorno).floatValue(); 
	if ((retorno instanceof Character) || (retorno instanceof String)) {
	    try { 
		return Float.parseFloat(retorno.toString().trim());
	    } catch(NumberFormatException nfex) {
		throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	    }
	}
	if (!(retorno instanceof Float)) throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	return ((Float)retorno).floatValue(); 
    }
    public double getDouble(String nomeColuna) throws DAOException 
    { 
	Object retorno = get(nomeColuna);
	if (retorno == null) return 0;
	if (retorno instanceof BigDecimal) return ((BigDecimal)retorno).doubleValue(); 
	if (retorno instanceof BigInteger) return ((BigInteger)retorno).doubleValue(); 
	if ((retorno instanceof Character) || (retorno instanceof String)) {
	    try { 
		return Double.parseDouble(retorno.toString().trim());
	    } catch(NumberFormatException nfex) {
		throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	    }
	}
	if (!(retorno instanceof Double)) throw new DAOException("Incorrect Type ("+nomeColuna+")","error.DAO.invalidType",nomeColuna);
	return ((Double)retorno).doubleValue(); 
    }
    
    public java.util.Date getDate(String nomeColuna) throws DAOException
    {
    	Object retorno = get(nomeColuna);
    	if (retorno == null) return null;
    	if (retorno instanceof java.sql.Timestamp) {
    	    return new java.util.Date(((java.sql.Timestamp) retorno).getTime());
    	}
    	if (retorno instanceof java.sql.Date) {
    	    return new java.util.Date(((java.sql.Date) retorno).getTime());
    	}
    	if (retorno instanceof java.util.Date) {
    	    return (java.util.Date) retorno;
    	}
    	return new java.util.Date();
    }
    
    /**
     * Special one to convert from date to string
     */
    public String getDateAsString(String nomeColuna) throws DAOException
    {
	Object retorno = get(nomeColuna);
	if (retorno == null) return null;

	Calendar clnd = Calendar.getInstance();

	if (retorno instanceof java.sql.Timestamp) {
	    clnd.setTime((java.sql.Timestamp) retorno);
	}
	if (retorno instanceof java.util.Date) {
	    clnd.setTime((java.util.Date) retorno);
	}

	String retornoAsString = "";
	int dia = clnd.get(Calendar.DAY_OF_MONTH);
	int mes = clnd.get(Calendar.MONTH) + 1;
	int ano = clnd.get(Calendar.YEAR);
	if (dia < 10) retornoAsString += "0";
	retornoAsString += dia +"/";
	if (mes < 10) retornoAsString += "0";
	retornoAsString += mes +"/";
	if (ano < 1000) retornoAsString += "0";
	if (ano < 100) retornoAsString += "0";
	if (ano < 10) retornoAsString += "0";
	retornoAsString += ano;
       	
	return retornoAsString;
    }
    
    public Timestamp getTimestamp(String nomeColuna) throws DAOException
    {
    	Object retorno = get(nomeColuna);
    	if (retorno == null) return null;
    	if (retorno instanceof java.sql.Timestamp) {
    	    return (java.sql.Timestamp) retorno;
    	}
    	if (retorno instanceof java.sql.Date) {
    	    return new Timestamp(((java.sql.Date) retorno).getTime());
    	}
    	if (retorno instanceof java.util.Date) {
    	    return new Timestamp(((java.util.Date) retorno).getTime());
    	}
    	return new Timestamp((new java.util.Date()).getTime());
    }
    
    public String getTimestampAsString(String nomeColuna) throws DAOException
    {
    	Object retorno = get(nomeColuna);
    	if (retorno == null) return null;

    	Calendar clnd = Calendar.getInstance();

    	if (retorno instanceof java.sql.Timestamp) {
    	    clnd.setTime((java.sql.Timestamp) retorno);
    	}
    	if (retorno instanceof java.util.Date) {
    	    clnd.setTime((java.util.Date) retorno);
    	}

    	String retornoAsString = "";
    	int dia = clnd.get(Calendar.DAY_OF_MONTH);
    	int mes = clnd.get(Calendar.MONTH) + 1;
    	int ano = clnd.get(Calendar.YEAR);
    	if (dia < 10) retornoAsString += "0";
    	retornoAsString += dia +"/";
    	if (mes < 10) retornoAsString += "0";
    	retornoAsString += mes +"/";
    	if (ano < 1000) retornoAsString += "0";
    	if (ano < 100) retornoAsString += "0";
    	if (ano < 10) retornoAsString += "0";
    	retornoAsString += ano + " ";
    	
    	int hora = clnd.get(Calendar.HOUR_OF_DAY);
    	int mins = clnd.get(Calendar.MINUTE);
    	int secs = clnd.get(Calendar.SECOND);
        if (hora < 10) retornoAsString += "0";
    	retornoAsString += hora +":";
        if (mins < 10) retornoAsString += "0";
    	retornoAsString += mins +":";
        if (secs < 10) retornoAsString += "0";
    	retornoAsString += secs;
    	
    	return retornoAsString;
    }

    /**
     * Returns a hashmap containing all the results
     * WARNING !!!! Restricted use 
     * @return HashMap
     */
    protected HashMap getHash() {
    	return this.values;
    }
    
}

    
