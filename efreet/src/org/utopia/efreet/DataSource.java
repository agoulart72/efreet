package org.utopia.efreet;

import java.util.Hashtable;

/**
 * This class is used to store information about DAOs from one DataSource
 */
public class DataSource {
        
    private String datasourceName = null;
    private Hashtable daoList = null;
    
    public void setName(String value) { this.datasourceName = value; }
    
    public void put(String key, Object val) {
        if (daoList == null) daoList = new Hashtable();
        if (key != null) daoList.put(key, val);
    }
    
    public Object get(String key) {
        if (daoList == null) return null;
        return daoList.get(key);
    }
    
}
