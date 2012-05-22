package org.utopia.efreet.cache;

import java.util.HashMap;

/**
 * @author Metal
 * @version $Id: BaseCacheConfiguration.java,v 1.1 2012-05-22 13:37:38 agoulart Exp $
 * 
 */
public class BaseCacheConfiguration {

	/** cache Initial Capacity */
	private int initialCapacity = 10;
	
	/** Max Life Time - to be used on FIFO mode */
	private long maxLifeTime = 3600000; // hour
	
	/** Max Access Time - to be used on "Last Access" mode */
	private long maxAccessTime = 3600000; // hour
	
	/** Min access - to be used on "Least Required" mode */
	private long minHit = 0;
	
	/** Scan Time - time between scannings */
	private long scanTime = 60000; // minute
	
	/** Cache Mode */
	private int cacheMode = 0;

	/** Cache Modes - Least Required */
	public static final int MODE_LEAST_REQUIRED = 0;
	/** Cache Modes - Last Access */
	public static final int MODE_LAST_ACCESS = 1;
	/** Cache Modes - FIFO */
	public static final int MODE_FIFO = 2;
	
	/** Cache Object Type */
	public String objectType = null;
	
	/** factory parameters */
	private HashMap<String, Object> params;
	
	/**
	 * accessor Initial Capacity
	 * @return cache Initial Capacity
	 */
	public int getInitialCapacity() {
		return this.initialCapacity;
	}
	
	/**
	 * @param initialCapacity the initialCapacity to set
	 */
	public void setInitialCapacity(int initialCapacity) {
		this.initialCapacity = initialCapacity;
	}

	/**
	 * hashtable load factor
	 * @return default load factor
	 */
	public float getLoadFactor() {
		return Float.parseFloat("0.75"); 
	}

	/**
	 * @return the params
	 */
	public HashMap<String, ?> getParams() {
		return params;
	}

	/**
	 * @return the maxAccessTime
	 */
	public long getMaxAccessTime() {
		return maxAccessTime;
	}

	/**
	 * @param maxAccessTime the maxAccessTime to set
	 */
	public void setMaxAccessTime(long maxAccessTime) {
		this.maxAccessTime = maxAccessTime;
	}

	/**
	 * @return the maxLifeTime
	 */
	public long getMaxLifeTime() {
		return maxLifeTime;
	}

	/**
	 * @param maxLifeTime the maxLifeTime to set
	 */
	public void setMaxLifeTime(long maxLifeTime) {
		this.maxLifeTime = maxLifeTime;
	}

	/**
	 * @return the minHit
	 */
	public long getMinHit() {
		return minHit;
	}

	/**
	 * @param minHit the minHit to set
	 */
	public void setMinHit(long minHit) {
		this.minHit = minHit;
	}

	/**
	 * @return the scanTime
	 */
	public long getScanTime() {
		return scanTime;
	}

	/**
	 * @param scanTime the scanTime to set
	 */
	public void setScanTime(long scanTime) {
		this.scanTime = scanTime;
	}

	/**
	 * @return the cacheMode
	 */
	public int getCacheMode() {
		return cacheMode;
	}

	/**
	 * @param cacheMode the cacheMode to set
	 */
	public void setCacheMode(int cacheMode) {
		this.cacheMode = cacheMode;
	}

	/**
	 * @return the objectType
	 */
	public String getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	
	
	
}
