package org.utopia.efreet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The purpose of this adapter is to extract information from a 
 * user's object that has accessor methods to be used as 
 * parameters for the query.
 * 
 * @author Adriano M Goulart
 * @version $Id: ResultAdapter.java,v 1.1 2007-01-30 18:14:57 agoulart Exp $
 */
public class ResultAdapter extends QueryResult {

	/**
	 * Adapter constructor
	 * @param result QueryResult from a efreet query
	 */
	public ResultAdapter(QueryResult result) {
		this.values = result.getHash();
	}

	/**
	 * Populate this bean with values
	 * @param bean User's object with accessor methods
	 * @return
	 */
	public Object fill(Object bean) {
		
		if (bean != null) {
			Class cl = bean.getClass();
			Field[] cpos = cl.getDeclaredFields();
			for (int i = 0; i < cpos.length; i++) {
				Field fld = cpos[i];
				if (fld != null) {
					String fldNm = fld.getName();
					if (fldNm != null && fldNm.length() > 0) {
						if (this.values.containsKey(fldNm)) {
							String mtdName = fldNm.replaceFirst(fldNm.substring(0, 1), "set" + fldNm.substring(0, 1).toUpperCase());
							Object val = this.values.get(fldNm);
							Class valClass = val.getClass();
							
							// booolean, byte, char, short, int, long, float, double
							if (val instanceof Boolean) {
								valClass = Boolean.TYPE;
							} else if (val instanceof Byte) {
								valClass = Byte.TYPE;
							} else if (val instanceof Character) {
								valClass = Character.TYPE;
							} else if (val instanceof Short) {
								valClass = Short.TYPE;
							} else if (val instanceof Integer) {
								valClass = Integer.TYPE;
							} else if (val instanceof Long) {
								valClass = Long.TYPE;
							} else if (val instanceof Float) {
								valClass = Float.TYPE;
							} else if (val instanceof Double) {
								valClass = Double.TYPE;
							}
							
							try {
								Class[] metParams = new Class[1];
								metParams[0] = valClass;
								Object[] params = new Object[1];
								params[0] = val;
								
								Method met = cl.getDeclaredMethod(mtdName, metParams);
								if (met != null) {
									met.invoke(bean, params);
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
				}
			}
		}
		
		return bean;
	}
	
}
