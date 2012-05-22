package org.utopia.efreet.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.utopia.efreet.DAOException;
import org.utopia.efreet.QueryResult;

/**
 * The purpose of this adapter is to extract information from a 
 * QueryResult object an create a bean object of the type identified
 * by the user
 *  
 * @author Adriano M Goulart
 * @version $Id: ResultAdapter.java,v 1.1 2012-05-22 13:38:25 agoulart Exp $
 */
public class ResultAdapter {

	private ArrayList<QueryResult> qResult; // Data input
	
	/**
	 * Adapter constructor
	 * @param result QueryResult from a efreet query
	 */
	public ResultAdapter(QueryResult result) {
		if (this.qResult == null) {
			this.qResult = new ArrayList<QueryResult>();
		}
		this.qResult.add(result);
	}

	/**
	 * Adapter constructor
	 * @param result QueryResult from a efreet query
	 */
	public ResultAdapter(Collection<QueryResult> result) {
		if (this.qResult == null) {
			this.qResult = new ArrayList<QueryResult>();
		}
		for (QueryResult r : result) {
			this.qResult.add(r);
		}
	}

	/**
	 * @return the qResult
	 */
	public Collection<QueryResult> getQueryResult() {
		return qResult;
	}

	/**
	 * @param index
	 * @return a single queryresult
	 */
	public QueryResult getQueryResult(int index) {
		return qResult.get(index);
	}
	
	/**
	 * @param result the qResult to set
	 */
	public void setQueryResult(Collection<QueryResult> result) {
		this.qResult = new ArrayList<QueryResult>();
		for (QueryResult r : result) {
			this.qResult.add(r);
		}
	}

	/**
	 * @param result a QueryResult to be added to the collection
	 */
	public void addQueryResult(QueryResult result) {
		if (this.qResult == null) {
			this.qResult = new ArrayList<QueryResult>();
		}
		this.qResult.add(result);		
	}
	
	/**
	 * @param result a Collection of QueryResult to be added to the collection
	 */
	public void addQueryResult(Collection<QueryResult> result) {
		if (this.qResult == null) {
			this.qResult = new ArrayList<QueryResult>();
		}
		for (QueryResult r : result) {
			this.qResult.add(r);
		}
	}
	
	/**
	 * Create a new bean and populate with values
	 * @param beanClass User's object class with accessor methods
	 * @return a bean filled with the results
	 */
	public Object build(Class<?> beanClass) throws DAOException {

		return build(beanClass, new DefaultResultBuilder());
	}
	
	/**
	 * Implement your own resultbuilder
	 * @param beanClass bean class
	 * @param builder your own implementation of the builder
	 * @return a bean filled with the results
	 * @throws DAOException
	 */
	public Object build(Class<?> beanClass, ResultBuilder builder) throws DAOException {

		return builder.build(beanClass, this.qResult);	
	}
	
	/**
	 * Returns a list of Objects
	 * @param beanClass
	 * @return List of beans
	 * @throws DAOException
	 */
	public <T> List<T> buildList(Class<T> beanClass) throws DAOException {

		return buildList(beanClass, new DefaultResultBuilder());
	}

	/**
	 * Returns a list of Objects
	 * @param beanClass
	 * @param builder your own implementation of the builder
	 * @return List of beans
	 * @throws DAOException
	 */
	public <T> List<T> buildList(Class<T> beanClass, ResultBuilder builder) throws DAOException {

		return builder.buildList(beanClass, this.qResult);
	}
	
	/**
	 * Populates a bean object with the values from the result set
	 * @param bean object to be populated
	 * @throws DAOException error
	 */
	public <T> T update(T bean) throws DAOException {
		return update(bean, new DefaultResultBuilder());
	}
	
	/**
	 * Populates a bean object with the values from the result set
	 * @param bean object to be populated
	 * @param builder customized result builder
	 * @throws DAOException error
	 */
	public <T> T update(T bean, ResultBuilder builder) throws DAOException {
		return builder.update(bean, this.qResult);
	}
	
	/**
	 * Populates a list of bean objects with the values from the result set
	 * @param beanList object to be populated
	 * @throws DAOException error
	 */
	public <T> List<T> updateList(List<T> beanList) throws DAOException {
		return update(beanList, new DefaultResultBuilder());
	}
	
	/**
	 * Populates a list of bean objects with the values from the result set
	 * @param beanList object to be populated
	 * @param builder customized result builder
	 * @throws DAOException error
	 */
	public <T> List<T> updateList(List<T> beanList, ResultBuilder builder) throws DAOException {
		return builder.update(beanList, this.qResult);
	}

}
