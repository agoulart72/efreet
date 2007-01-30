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
 * @version $Id: ParameterAdapter.java,v 1.1 2007-01-30 18:14:57 agoulart Exp $
 */
public class ParameterAdapter extends QueryParameter {

	/**
	 * Adapter constructor
	 * @param bean User's object with accessor methods
	 */
	public ParameterAdapter(Object bean) {
		
		if (bean != null) {
			Class cl = bean.getClass();
			Field[] cpos = cl.getDeclaredFields();
			for (int i = 0; i < cpos.length; i++) {
				Field fld = cpos[i];
				if (fld != null) {
					String fldNm = fld.getName();
					if (fldNm != null && fldNm.length() > 0) {
						String mtdName = fldNm.replaceFirst(fldNm.substring(0, 1), "get" + fldNm.substring(0, 1).toUpperCase());
						try {
							Method met = cl.getDeclaredMethod(mtdName, null);
							if (met == null) {
								met = cl.getDeclaredMethod(mtdName, new Class[0]);
							}
							Object val = met.invoke(bean, null);
							if (val == null) {
								val = met.invoke(bean, new Object[0]);
							}
							put(fldNm, val);
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
	
}
