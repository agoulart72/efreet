package org.utopia.efreet.adapter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author agoulart
 *
 */
public class BeanModel {

	private Class<?> beanClass;
	
	private String key = null;
	
	private HashMap<String, Class<?>> attributes;
	private HashMap<String, Method> getMethods;
	private HashMap<String, Method> setMethods;
	
	private HashSet<String> primaryKeys;
	
	private HashMap<String, BeanModel> children;
	
	private boolean collection = false;
	private Class<?> colClass;
	
	/* Delimiters */
    public static final String ATTRIBUTE_DELIMITER = ".";
	
	/**
	 * @return the beanClass
	 */
	public Class<?> getBeanClass() {
		return beanClass;
	}
	/**
	 * @param beanClass the beanClass to set
	 */
	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
	/**
	 * @return the Key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the Key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Add a new attribute and its class
	 * @param name attribute name
	 * @param attrClass attribute class
	 */
	public void addAttribute(String name, Class<?> attrClass) {
		if (this.attributes == null) {
			this.attributes = new HashMap<String, Class<?>>();
		}
		this.attributes.put(name, attrClass);
	}

	/**
	 * @param name Attribute name
	 * @return attribute class
	 */
	public Class<?> getAttributeClass(String name) {
		Class<?> ret = null;
		if (this.attributes != null) {
			ret = this.attributes.get(name);
		}
		return ret;
	}
	
	/**
	 * Add a new "get" method 
	 * @param name attribute name
	 * @param getMethod "get" method
	 */
	public void addToGetMethods(String name, Method getMethod) {
		if (this.getMethods == null) {
			this.getMethods = new HashMap<String, Method>();
		}
		this.getMethods.put(name, getMethod);
	}
	
	/**
	 * @param name attribute name
	 * @return "get" method
	 */
	public Method getGetMethod(String name) {
		Method ret = null;
		if (this.getMethods != null) {
			ret = this.getMethods.get(name);
		}
		return ret;
	}

	/**
	 * Add a new "set" method 
	 * @param name attribute name
	 * @param setMethod "set" method
	 */
	public void addToSetMethods(String name, Method setMethod) {
		if (this.setMethods == null) {
			this.setMethods = new HashMap<String, Method>();
		}
		this.setMethods.put(name, setMethod);
	}
	
	/**
	 * @param name attribute name
	 * @return "set" method
	 */
	public Method getSetMethod(String name) {
		Method ret = null;
		if (this.setMethods != null) {
			ret = this.setMethods.get(name);
		}
		return ret;
	}
	
	/**
	 * Add a primary key
	 * @param name attribute name
	 */
	public void addPrimaryKey(String name) {
		if (this.primaryKeys == null) {
			this.primaryKeys = new HashSet<String>();
		}
		this.primaryKeys.add(name);
	}
	
	/**
	 * @return the set of primary keys
	 */
	public Set<String> getPrimaryKeys() {
		return this.primaryKeys;
	}
	
	/**
	 * Adds a children model for this bean
	 * @param name child name
	 * @param child child model
	 */
	public void addChild(String name, BeanModel child) {
		if (this.children == null) {
			this.children = new HashMap<String, BeanModel>();
		}
		this.children.put(getFirstName(name), child);
	}
	
	/**
	 * @param name child's name
	 * @return child model
	 */
	public BeanModel getChild(String name) {
		BeanModel ret = null;
		if (this.children != null) {
			ret = this.children.get(getFirstName(name));
		}
		return ret;
	}
	
	/**
	 * Inner function, simplify getting the first name in a composite name
	 * @param fullName
	 * @return first name
	 */
	private String getFirstName(String fullName) {
		String ret = fullName;
		if (fullName.indexOf(ATTRIBUTE_DELIMITER) > 0) {
			ret = fullName.substring(0, fullName.indexOf(ATTRIBUTE_DELIMITER));
		}
		return ret;
	}
	
	/**
	 * Sets the flag collection
	 * @param bol
	 */
	public void setCollection(boolean bol) {
		this.collection = bol;
	}
	
	/**
	 * Indicates if these model is contained inside a Collection
	 * @return boolean
	 */
	public boolean isCollection() {
		return this.collection;
	}
	/**
	 * @return the colClass
	 */
	public Class<?> getColClass() {
		return colClass;
	}
	/**
	 * @param colClass the colClass to set
	 */
	public void setColClass(Class<?> colClass) {
		this.colClass = colClass;
	}
	
	public String toString() {
		
		return (getBeanClass()!=null?getBeanClass().getName():"null") 
			+ (isCollection()?"[" + (getColClass()!=null?getColClass().getName():"null") + "]":""); 
		
	}
	
}
