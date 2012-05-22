package org.utopia.efreet.cache;

import java.util.Map;

/**
 * @author Metal
 * @version $Id: CachedObjectBuilder.java,v 1.1 2012-05-22 13:37:38 agoulart Exp $
 * An interface for creating factories for cached objects 
 */
public interface CachedObjectBuilder {
	
	/**
	 * Create an object to be cached.
	 * @param params Map whith parameters
	 * @return object created
	 * @throws Exception error
	 */
	Object build(Map<String, Object> params) throws Exception;

}
