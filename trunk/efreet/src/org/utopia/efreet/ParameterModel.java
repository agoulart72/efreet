package org.utopia.efreet;

import java.sql.Types;

/**
 * @author Adriano M Goulart
 * @version $Id: ParameterModel.java,v 1.2 2012-05-22 13:34:42 agoulart Exp $
 */
public class ParameterModel implements Comparable<ParameterModel>, Cloneable {

	/** Parameter Type */
	private int paramType = Types.NULL;
	
	/** Parameter Name */
	private String paramName = null;
	
	/** Parameter Size */
	private int paramSize = 0;

	/** Parameter Index */
	private int paramIndex = 0;

	/** Used in conditional queries, where you'd want to fix a value */
	private Object staticValue = null;
	
	/* Delimiters */
    public static final String PARAM_DELIMITER_BEGIN = "#{";
    public static final String PARAM_DELIMITER_END = "}";
    public static final String PARAM_TYPE_DELIMITER_BEGIN = "<";
    public static final String PARAM_TYPE_DELIMITER_END = ">";
    public static final String PARAM_SIZE_DELIMITER_BEGIN = ")";
    public static final String PARAM_SIZE_DELIMITER_END = "(";
	
	/**
	 * @return the paramName
	 */
	public String getParamName() {
		return paramName;
	}

	/**
	 * @param paramName the paramName to set
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * @return the paramSize
	 */
	public int getParamSize() {
		return paramSize;
	}

	/**
	 * @param paramSize the paramSize to set
	 */
	public void setParamSize(int paramSize) {
		this.paramSize = paramSize;
	}

	/**
	 * @param psiz as String
	 */
	public void setParamSize(String psiz) {
		try {
			setParamSize(Integer.parseInt(psiz));
		} catch (Exception e) {
			setParamSize(0);
		}		
	}
	
	/**
	 * @return the paramType
	 */
	public int getParamType() {
		return paramType;
	}

	/**
	 * @param paramType the paramType to set
	 */
	public void setParamType(int paramType) {
		this.paramType = paramType;
	}

	/**
	 * @return the paramIndex
	 */
	public int getParamIndex() {
		return paramIndex;
	}

	/**
	 * @param paramIndex the paramIndex to set
	 */
	public void setParamIndex(int paramIndex) {
		this.paramIndex = paramIndex;
	}

	/**
	 * @return the staticValue
	 */
	public Object getStaticValue() {
		return staticValue;
	}

	/**
	 * @param staticValue the staticValue to set
	 */
	public void setStaticValue(Object staticValue) {
		this.staticValue = staticValue;
	}

	/**
	 * Implements Comparable Interface, compares the models based on the Index
	 */
	public int compareTo(ParameterModel o) {
		return this.paramIndex - o.getParamIndex();
	}

	/*
	 * DEBUG purpose
	 * @return the type of the parameter in human readable format
	 */
	public String getParamTypeDescription() {
		switch (this.paramType) {
		case java.sql.Types.TIME : return "<TIME>";
		case java.sql.Types.TIMESTAMP : return "<TIMESTAMP>";
		case java.sql.Types.DATE : return "<DATE>";
		case java.sql.Types.NUMERIC : return "<NUMBER>";
		case java.sql.Types.CHAR : return "<CHAR>";
		case java.sql.Types.BLOB : return "<BLOB>";
		case java.sql.Types.CLOB : return "<CLOB>";
		case java.sql.Types.JAVA_OBJECT : return "<OBJECT>";
		default : return "<NONE>";
		}
	}
	
	@Override
	public String toString() {
		return "[" + this.paramIndex + "] " + this.paramName +  this.getParamTypeDescription() + "(" + this.paramSize + ")";
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	protected ParameterModel clone() throws CloneNotSupportedException {
		
		ParameterModel clone = new ParameterModel();
		clone.setParamIndex(getParamIndex());
		clone.setParamName(getParamName());
		clone.setParamSize(getParamSize());
		clone.setParamType(getParamType());
		clone.setStaticValue(getStaticValue());
		
		return clone;
	}

}
