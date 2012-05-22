package org.utopia.efreet.cache;

import java.util.Collection;

import org.utopia.efreet.QueryParameter;
import org.utopia.efreet.QueryResult;

/**
 * @author metal
 *
 */
public class QueryResultCache extends BaseObjectCache {

	private static Object LOCK = new Object();
	
	/** Singleton instance. */
	private static QueryResultCache instance;
	
	/**
	 *  Singleton retrieval method.
	 *  @return instance
	 */
	public static QueryResultCache getInstance() {
		synchronized (LOCK) {
			if (instance == null) {
				instance = new QueryResultCache();
				instance.setBuilder(new QueryResultBuilder());
				instance.setConfiguration(new QueryResultCacheConfiguration());
				instance.startTimer();
			}
			return instance;		
		}
	}

	/**
	 * Retrieves a stored quryresult from the cache
	 * @param queryName QueryName
	 * @param params parameters
	 * @return Collection of QueryResult
	 * @throws Exception error
	 */
	public Collection<QueryResult> getQueryResult(String queryName, QueryParameter params) throws Exception {
		
		Object obj = super.get(queryName);
		if (obj != null) {			
			return (Collection<QueryResult>) obj;
		} else {
			return null;
		}
	}
	
	/**
	 * Releases an object from cache.
	 * @param queryName key
	 * @param params parameter
	 * @throws Exception error
	 */
	public void refresh(String queryName, QueryParameter params) throws Exception {
		super.refresh(queryName);
	}

}
