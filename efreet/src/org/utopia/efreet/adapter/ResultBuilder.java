package org.utopia.efreet.adapter;

import java.util.Collection;
import java.util.List;

import org.utopia.efreet.DAOException;
import org.utopia.efreet.QueryResult;

/**
 * A builder-like pattern
 * Implement this builder to adapt a QueryResult class to your own bean classes
 * @author metal
 */
public interface ResultBuilder {

	/**
	 * Main method - implement this to translate a query result into your own bean
	 * @param beanClass class for the bean
	 * @param result result
	 * @return a populated bean
	 * @throws DAOException error
	 */
	public <T> T build(Class<T> beanClass, Collection<QueryResult> result) throws DAOException;
		
	/**
	 * Alternative method - implement this to return a list of beans
	 * @param beanClass class for the bean
	 * @param result the result set from efreet
	 * @return a Collection of populated Beans
	 * @throws DAOException error
	 */
	public <T> List<T> buildList(Class<T> beanClass, Collection<QueryResult> result) throws DAOException;
	
	/**
	 * Implement this to translate a query result into a bean already created
	 * @param bean object already created
	 * @param result result
	 * @throws DAOException error
	 */
	public <T> T update(T bean, Collection<QueryResult> result) throws DAOException;
	
	/**
	 * Similar implementation as previous, except that this case populates a list, and
	 * create new beans if not already in the list
	 * @param beanList list of beans
	 * @param result result
	 * @throws DAOException error
	 */
	public <T> List<T> updateList(List<T> beanList, Collection<QueryResult> result) throws DAOException;
	
}
