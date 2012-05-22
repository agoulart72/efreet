package org.utopia.efreet.cache;

import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * @author Metal
 * @version $Id: BaseObjectCache.java,v 1.1 2012-05-22 13:37:38 agoulart Exp $
 * 
 */
public abstract class BaseObjectCache {

	/**
	 * Synchronization flag
	 */
	private static Object LOCK = new Object(); 
	
	/**
	 * The hash that keeps the cached objects.
	 */
	protected HashMap<Object, CachedObjectDecorator> hsh = null;
	
	/**
	 * Configuratiuon class.
	 */
	protected BaseCacheConfiguration conf;

	/**
	 * Timer - control cache timer events.
	 */
	protected Timer timer;
	
	/**
	 * TimerTask.
	 */
	protected TimerTask timerTask;
	
	/**
	 * The builder to build new objects.
	 */
	protected CachedObjectBuilder builder;

	/** log4j. */
    private static Logger logger = Logger.getLogger(BaseObjectCache.class.getName());

	
	/**
	 * Start the cache events with a timer.
	 */
	protected void startTimer() {
		
		final long second = 1000; // a second
		
		long delay = 5 * second;
		long interval = second;

		if (conf != null) {
			delay = 5 * conf.getScanTime();
			interval = conf.getScanTime();
		}
		
		this.timer = new Timer();

		timerTask = new TimerTask() {
			public void run() {
				scanCache();
			}
		};

		this.timer.scheduleAtFixedRate(timerTask, delay, interval); 

	}
	
	/**
	 * Method to create the hashtable for the cache.
	 */
	@SuppressWarnings("unchecked")
	private void createCache() {
		synchronized (LOCK) {
			if (this.hsh == null) {
				this.hsh = new HashMap(this.conf.getInitialCapacity(), this.conf.getLoadFactor());
			}			
		}
	}
	
	/**
	 * Base method for retrieving objects in the cache.
	 * @param key key
	 * @return cached object
	 * @throws Exception error
	 */
	protected Object get(Object key) throws Exception {
		
		Object objRet = null;
		
		createCache();
		
		synchronized (LOCK) {
			
			if (key != null) {
				CachedObjectDecorator dec = (CachedObjectDecorator) this.hsh.get(key);
				if (dec != null) {
					objRet = dec.getCachedObject();
					dec.hit();
					dec.setLastAccess(System.currentTimeMillis());
				}
				if (objRet == null) {
					if (this.builder != null) {
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("key", key);
						if (this.conf.getParams() != null) {
							params.putAll(this.conf.getParams());
						}
						objRet = builder.build(params);
					}
					// wrap object in decorator
					dec = new CachedObjectDecorator(objRet);
					dec.setFrequencyHit(1);
					dec.setLastAccess(System.currentTimeMillis());
					dec.setLifeTime(System.currentTimeMillis());

					// add object to hash
					this.hsh.put(key, dec);
					logger.debug("Caching object : Type [" + conf.getObjectType() + ", Key [" + key + "]");
					
				}
			}
			
			return objRet;
		}
	}
	
	/**
	 * Limpa o objeto do cache (proxima busca se encarrega de atualizar).
	 * @param key chave
	 * @throws Exception erro
	 */
	protected void refresh(Object key) throws Exception {

		synchronized (LOCK) {

			if (this.hsh != null && !this.hsh.isEmpty()) {
				if (key != null) {
					hsh.remove(key);
					logger.debug("Refreshing cache : Type [" + conf.getObjectType() + "], Key [" + key + "]");
				}
			}						
		}
	}
		
	/**
	 * Method executed on each passing of the scan through the cache.
	 *
	 */
	protected void scanCache() {
		
		synchronized (LOCK) {
			
			if (this.hsh != null && !this.hsh.isEmpty()) {
	
				logger.debug("Scan Cache : " + this.hsh.size() + " Objects in cache");
				
				int mode = BaseCacheConfiguration.MODE_LEAST_REQUIRED;
				long minHit = 0;
				long maxAccess = 3600000;
				long maxLife = 3600000;
				long currTime = System.currentTimeMillis();
				
				if (conf != null) {
					mode = conf.getCacheMode();
					minHit = conf.getMinHit();
					maxAccess = conf.getMaxAccessTime();
					maxLife = conf.getMaxLifeTime();
				}
	
				for (Object objKey : hsh.keySet()) {
					Object objTmp = this.hsh.get(objKey);
					boolean remove = false;
					if (objTmp != null && objTmp instanceof CachedObjectDecorator) {
						CachedObjectDecorator dec = (CachedObjectDecorator) objTmp;
						switch (mode) {
							case BaseCacheConfiguration.MODE_LEAST_REQUIRED:
								dec.decHit();
								remove = (dec.getFrequencyHit() < minHit);
								break;
							case BaseCacheConfiguration.MODE_LAST_ACCESS:
								remove = (currTime - dec.getLastAccess() > maxAccess);
								break;
							case BaseCacheConfiguration.MODE_FIFO:
								remove = (currTime - dec.getLifeTime() > maxLife);
								break;
						}
					}
					if (remove) {
						try {
							hsh.remove(objKey);
						} catch (Exception ex) {
							logger.error("Error on refreshing cache : Key [" + objKey + "]", ex);
						}
					}
				}
			}
		}
		
	}
		
	/**
	 * gets the configuration
	 * @return Configuration
	 */
	public BaseCacheConfiguration getConfiguration() {
		return this.conf;
	}
	
	/**
	 * sets the configuration.
	 * @param cc Configuration
	 */
	public void setConfiguration(BaseCacheConfiguration cc) {
		this.conf = cc;
	}

	
	/**
	 * @param cBuilder the builder to set
	 */
	public void setBuilder(CachedObjectBuilder cBuilder) {
		this.builder = cBuilder;
	}
	
	/**
	 * @return List of Object keys
	 */
	@SuppressWarnings("unchecked")
	public Set getKeys() {

		Set ret = null;
		synchronized (LOCK) {
			if (this.hsh != null && !this.hsh.isEmpty()) {
				ret = hsh.keySet();
			}
		}
		return ret;
	}

	/**
	 * Clean the cache
	 */
	public void cleanCache() {
		synchronized (LOCK) {
			hsh.clear();
		}
	}
	
}
