package org.utopia.efreet.adapter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.utopia.efreet.QueryParameter;

/**
 * The purpose of this adapter is to extract information from a 
 * user's object that has accessor methods to be used as 
 * parameters for the query.
 * 
 * @author Adriano M Goulart
 * @version $Id: ParameterAdapter.java,v 1.1 2012-05-22 13:38:25 agoulart Exp $
 */
public class ParameterAdapter extends QueryParameter {

    static Logger logger = Logger.getLogger(ParameterAdapter.class.getName());

    /**
     * Constructor with no reference
     */
    public ParameterAdapter() {
    	
    }
    
	/**
	 * Adapter constructor
	 * @param bean User's object with accessor methods
	 */
	public ParameterAdapter(Object bean) {
		
		if (bean != null) {
			super.put("#object", bean);
		}
		
	}
	
	/**
	 * Return a single parameter from the object
	 * @return parameter
	 */
	@Override
	public Object get(String key) {
		
		Object val = null;
		
		try {			
			// Step 1 - replace any variable within the name
			int initVar = key.indexOf("${");
			while (initVar >= 0) {
				String varName = key.substring(initVar, key.indexOf("}", initVar));
				if (super.getVariables() != null && super.getVariables().get(varName) != null) {
					key = key.replaceFirst("${" + varName + "}", (String) super.getVariables().get(varName) ) ;					
				}
				initVar = key.indexOf("${", initVar + 1);
			}

			// Step 2 - check if the name searched is nested deep inside the object
			int endSub = key.indexOf(".");
			while (endSub == 0) {
				key = key.substring(endSub + 1);
				endSub = key.indexOf(".");
			}

			// Retrieve 1st parent
			Object parent = null;
			if (endSub > 0) {
				parent = super.get(key.substring(0, endSub));
			}

			// If I cant find 1st parent, it means that we have to look in the default '#object'
			if (parent == null) {
				parent = super.get("#object");
			} else {
				// If I find the parent, I can proceed forward in the key
				key = key.substring(endSub + 1);
				endSub = key.indexOf(".");				
			}

			// Loop through the entrie key
			while (endSub > 0) {
				String parentObjName = key.substring(0, endSub);
				parent = retrieveObject(parent, parentObjName);
				key = key.substring(endSub + 1);
				endSub = key.indexOf(".");
			}

			// Step 3 - retrieve the object
			val = retrieveObject(parent, key);

		} catch (Exception e) {
			logger.warn("Efreet : Error Adapting Parameter ", e);
			val = null;
		}
		
		if (val == null) {
			val = super.get(key);
		}
		
		return val;
	}
	
	/**
	 * Internal method to retrieve nested objects
	 * @param parent parent object
	 * @param key name
	 * @return object
	 */
	private Object retrieveObject(Object parent, String key) throws Exception {
	
		Object val = null;

		// Checar para verificar se nao estou procurando em parent errado
		if (parent == null) {
			return null;
		}		
		
		Class<?> cl = parent.getClass();

		// Step 1 - check to see if the object is an array element (recursive)
		if (key.indexOf("[") >= 0) {
			val = retrieveArrayObject(parent, key);
		}
				
		// Step 2 - Check to see if i'm calling a function
		if (val == null) {
			int initFunc = key.indexOf("(");
			if (initFunc > 0) {
				String funcName = key.substring(0, initFunc);
				ArrayList<String> listParams = new ArrayList<String>();
				int initParam = initFunc;
				while (key.indexOf(",", initParam) >= 0) {
					listParams.add(key.substring(initFunc, key.indexOf(",", initParam)));
					initParam = key.indexOf(",", initParam);
				}

				try {
					Method met = null;
					if (listParams.size() > 0) {
						met = cl.getDeclaredMethod(funcName, new Class[listParams.size()]);
					} else {
						met = cl.getDeclaredMethod(funcName, (Class[]) null);
					}
					if (met == null) {
						met = cl.getDeclaredMethod(funcName, new Class[0]);
					}
					if (listParams.size() > 0) {
						val = met.invoke(parent, listParams.toArray());
					} else {
						val = met.invoke(parent, (Object[]) null);
					}
					if (val == null) {
						val = met.invoke(parent, new Object[0]);
					}
				} catch (NoSuchMethodException e) {
					// Ignores
				} catch (IllegalAccessException e) {
					// Ignores
				} catch (InvocationTargetException e) {
					// Ignores
				}

			}
		}
		
		// Step 3 - search name as attribute with accessor
		if (val == null) {
			String mtdName = key.replaceFirst(key.substring(0, 1), "get" + key.substring(0, 1).toUpperCase());
			try {
				Method met = cl.getMethod(mtdName, (Class[]) null);
				if (met == null) {
					met = cl.getMethod(mtdName, new Class[0]);
				}
				val = met.invoke(parent, (Object[]) null);
				if (val == null) {
					val = met.invoke(parent, new Object[0]);
				}
			} catch (NoSuchMethodException e) {
				// Ignores
			} catch (IllegalAccessException e) {
				// Ignores
			} catch (InvocationTargetException e) {
				// Ignores
			}
		}

		// Step 4 - search name as attribute with no accessor method
		if (val == null) {
			try {
				Field fld = cl.getDeclaredField(key);
				val = fld.get(parent);
			} catch (NoSuchFieldException e) {
				// Ignores
			} catch (IllegalAccessException e) {
				// Ignores
			}
		}
				
		return val;
	}
	
	/**
	 * Retrive an element from an array parent - recursive
	 * @param parent Object parent
	 * @param key key
	 * @return Element Object resulting object
	 */
	private Object retrieveArrayObject(Object parent, String key) throws Exception {
		
		Object ret = parent;
		
		if (! parent.getClass().isArray()) {
			parent = retrieveObject(parent, key.substring(0, key.indexOf("[")));
		}
		
		if (key.indexOf("[") >= 0 && parent.getClass().isArray()) {
			
			ret = Array.get(parent, getIndex(key));

			key = key.substring(key.indexOf("]") + 1);
			
			if (key.trim().length() > 0) {
				ret = retrieveObject(ret, key);
			}
			
		}
		
		return ret;
		
	}
	
	/**
	 * Check an array index, if its invalid returns 0
	 */
	private int getIndex(String key) throws Exception {
		
		int idx = 0;
		
		int initArr = key.indexOf("[");
		int endArr = key.indexOf("]");
		if (initArr >= 0 && endArr > 0) {
			String strIdx = key.substring(initArr + 1, endArr);
			try {
				idx = Integer.parseInt(strIdx);
			} catch (NumberFormatException e) {
				idx = 0;
			}
		}
		
		return idx;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ParameterAdapter clone() {
		ParameterAdapter ret = new ParameterAdapter();
		if (values != null) {
			for (Object val : values) {
				ret.add(val);
			}
		}
		if (parameters != null) {
			for (String pKey : parameters.keySet()) {
				ret.put(pKey, get(pKey));
			}
		}
		if (variables != null) {
			for (String vKey : variables.keySet()) {
				ret.setVariable(vKey, variables.get(vKey));
			}
		}		
		return ret;
	}
}
