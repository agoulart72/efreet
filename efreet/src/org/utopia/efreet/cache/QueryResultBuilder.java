package org.utopia.efreet.cache;

import java.util.Map;

/**
 * @author metal
 *
 */
public class QueryResultBuilder implements CachedObjectBuilder {

	/* (non-Javadoc)
	 * @see org.utopia.efreet.cache.CachedObjectFactory#create(java.util.Map)
	 */
	public Object build(Map<String, Object> params) throws Exception {
		
		Object key = params.get("key");
		
		// TODO - retrieve name of the query and query parameters
		
		return null;
	}

}
