package org.utopia.efreet;

import java.sql.Types;

/**
 * @author Adriano M Goulart
 * @version $Id: ParameterModel.java,v 1.1 2007-01-30 18:14:57 agoulart Exp $
 */
public class ParameterModel {

	/** Parameter Type */
	private int paramType = Types.NULL;
	
	/** Parameter Name */
	private String paramName = null;
	
	/** Parameter Size */
	private int paramSize = 0;

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
	
	
}
