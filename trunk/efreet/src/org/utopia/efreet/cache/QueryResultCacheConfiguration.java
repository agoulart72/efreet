package org.utopia.efreet.cache;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

/**
 * @author metal
 *
 */
public class QueryResultCacheConfiguration extends BaseCacheConfiguration {

	/** log4j */
    private static Logger logger = Logger.getLogger(QueryResultCacheConfiguration.class.getName());
    
    /**
	 * Constructor.
	 */
	public QueryResultCacheConfiguration() {

		setCacheMode(BaseCacheConfiguration.MODE_FIFO);
		/** cache Initial Capacity */
		setInitialCapacity(1000);
		/** Max Life Time - to be used on FIFO mode */
		setMaxLifeTime(3600000);
		/** Max Access Time - to be used on "Last Access" mode */
		// setMaxAccessTime(3600000);
		/** Min access - to be used on "Least Required" mode */
		// setMinHit(0);
		/** Scan Time - time between scannings */
		setScanTime(60000);
		/** Set Object type */
		setObjectType("QueryResult");
		
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			String tmpInitialCapacity = (String) envCtx.lookup("efreet/cache/initialCapacity"); 
			String tmpMaxLifeTime = (String) envCtx.lookup("efreet/cache/maxLifeTime");
			String tmpMaxAccessTime = (String) envCtx.lookup("efreet/cache/maxAccessTime");
			String tmpMinHit = (String) envCtx.lookup("efreet/cache/minHit");
			String tmpScanTime = (String) envCtx.lookup("efreet/cache/scanTime");
			
			if (tmpInitialCapacity != null) { setInitialCapacity(Integer.parseInt(tmpInitialCapacity)); }
			if (tmpMaxLifeTime != null) { setMaxLifeTime(Long.parseLong(tmpMaxLifeTime)); }
			if (tmpMaxAccessTime != null) { setMaxAccessTime(Long.parseLong(tmpMaxAccessTime)); }
			if (tmpMinHit != null) { setMinHit(Long.parseLong(tmpMinHit)); }
			if (tmpScanTime != null) { setScanTime(Long.parseLong(tmpScanTime)); }
			
		} catch (Exception nex) {
			logger.error("Error retrieving context (mail)", nex);
		}

	}

}
