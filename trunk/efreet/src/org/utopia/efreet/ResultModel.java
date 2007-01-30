package org.utopia.efreet;

import java.sql.Types;

/**
 * @author Adriano M Goulart
 * @version $Id: ResultModel.java,v 1.1 2007-01-30 18:14:57 agoulart Exp $
 */
public class ResultModel {

	/** Parameter Type */
	private int resultType = Types.NULL;
	
	/** Parameter Name */
	private String resultName = null;
	
	/** Parameter Size */
	private int resultSize = 0;

	/**
	 * @return the resultName
	 */
	public String getResultName() {
		return resultName;
	}

	/**
	 * @param resultName the resultName to set
	 */
	public void setResultName(String paramName) {
		this.resultName = paramName;
	}

	/**
	 * @return the resultSize
	 */
	public int getResultSize() {
		return resultSize;
	}

	/**
	 * @param resultSize the resultSize to set
	 */
	public void setResultSize(int paramSize) {
		this.resultSize = paramSize;
	}

	/**
	 * @return the resultType
	 */
	public int getResultType() {
		return resultType;
	}

	/**
	 * @param resultType the resultType to set
	 */
	public void setResultType(int paramType) {
		this.resultType = paramType;
	}
	
	
}
