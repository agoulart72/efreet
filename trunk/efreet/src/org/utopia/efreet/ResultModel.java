package org.utopia.efreet;

import java.sql.Types;

/**
 * @author Adriano M Goulart
 * @version $Id: ResultModel.java,v 1.2 2012-05-22 13:34:43 agoulart Exp $
 */
public class ResultModel implements Comparable<ResultModel>, Cloneable {

	/** Result Type */
	private int resultType = Types.NULL;
	
	/** Result Name */
	private String resultName = null;
	
	/** Result Size */
	private int resultSize = 0;

	/** Result Index */
	private int resultIndex = 0;
	
	/**
	 * @return the resultName
	 */
	public String getResultName() {
		return resultName;
	}

	/**
	 * @param resultName the resultName to set
	 */
	public void setResultName(String resultName) {
		this.resultName = resultName;
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
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
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
	public void setResultType(int resultType) {
		this.resultType = resultType;
	}

	/**
	 * @return the resultIndex
	 */
	public int getResultIndex() {
		return resultIndex;
	}

	/**
	 * @param resultIndex the resultIndex to set
	 */
	public void setResultIndex(int resultIndex) {
		this.resultIndex = resultIndex;
	}
	
	/**
	 * Implements Comparable Interface, compares the models based on the Index
	 */
	public int compareTo(ResultModel o) {
		return this.resultIndex - o.getResultIndex();
	}

	/**
	 * DEBUG purpose
	 * @return the type of the result in human readable format
	 */
	public String getResultTypeDescription() {
		switch (this.resultType) {
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
		return "[" + this.resultIndex + "] " + this.resultName + getResultTypeDescription() + "(" + this.resultSize + ")";
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	protected ResultModel clone() throws CloneNotSupportedException {
		
		ResultModel clone = new ResultModel();
		clone.setResultIndex(getResultIndex());
		clone.setResultName(getResultName());
		clone.setResultSize(getResultSize());
		clone.setResultType(getResultType());
		
		return clone;
	}
}
