package org.utopia.efreet.cache;

/**
 * @author Metal
 * @version $Id: CachedObjectDecorator.java,v 1.1 2012-05-22 13:37:38 agoulart Exp $
 * 
 */
public class CachedObjectDecorator {

	/**
	 * Object to be decorated.
	 */
	private Object cachedObject;

	/**
	 * Life Time to be used in FIFO mode.
	 */
	private long lifeTime = 0;
	
	/**
	 * Last access to be used on "Last Access" mode.
	 */
	private long lastAccess = 0;
	
	/**
	 * Frequency Hit to be used on "Least Required" mode.
	 */
	private long frequencyHit = 0;
	
	/**
	 * Decorator constructor.
	 * @param cObject cached object
	 */
	public CachedObjectDecorator(Object cObject) {
		this.cachedObject = cObject;
	}
	
	/**
	 * Accessor method.
	 * @return cached object
	 */
	public Object getCachedObject() {
		return cachedObject;
	}

	/**
	 * @return the frequencyHit
	 */
	public long getFrequencyHit() {
		return frequencyHit;
	}

	/**
	 * @param frequencyHit the frequencyHit to set
	 */
	public void setFrequencyHit(long frequencyHit) {
		this.frequencyHit = frequencyHit;
	}

	/**
	 * Increase frequency hit.
	 */
	public void hit() {
		this.frequencyHit++;
	}

	/**
	 * Decrease frequency hit.
	 */
	public void decHit() {
		this.frequencyHit--;
	}
	
	/**
	 * @return the lastAccess.
	 */
	public long getLastAccess() {
		return lastAccess;
	}

	/**
	 * @param lastAccess the lastAccess to set.
	 */
	public void setLastAccess(long lastAccess) {
		this.lastAccess = lastAccess;
	}

	/**
	 * @return the lifeTime
	 */
	public long getLifeTime() {
		return lifeTime;
	}

	/**
	 * @param lifeTime the lifeTime to set
	 */
	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}
	
}
