package org.utopia.efreet.adapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.utopia.common.util.ClassUtil;
import org.utopia.common.util.Unfinished;
import org.utopia.efreet.DAOException;
import org.utopia.efreet.QueryResult;

/**
 * Uses Reflection and Recursion
 * Old Default implementation for a ResultBuilder
 * @author metal
 *
 *
 */
@Unfinished
public class ReflectiveResultBuilder implements ResultBuilder {

	static Logger logger = Logger.getLogger(ReflectiveResultBuilder.class.getName());
	
	/**
	 * Implements
	 */
	public <T> T build(Class<T> beanClass, Collection<QueryResult> qResult) throws DAOException {		
		try {
			return update(create(beanClass), qResult);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}		
	}
		
	/* (non-Javadoc)
	 * @see org.utopia.efreet.adapter.ResultBuilder#buildList(java.lang.Class, java.util.Collection)
	 */
	public <T> List<T> buildList(Class<T> beanClass, Collection<QueryResult> qResult) throws DAOException {

		return updateList(beanClass, new ArrayList<T>(), qResult);
	}
	
	/* (non-Javadoc)
	 * @see org.utopia.efreet.adapter.ResultBuilder#update(T, java.util.Collection)
	 */
	public <T> T update(T bean, Collection<QueryResult> result) throws DAOException {
		
		try {
			for (QueryResult resp : result) {
				// Set all the values in the new object
				Set<String> keySet = resp.getKeys();
				for (String key : keySet) {
					set(bean, null, key, resp);
				}
			}
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}		
		return bean;
	}

	@SuppressWarnings("unchecked")
	/* (non-Javadoc)
	 * @see org.utopia.efreet.adapter.ResultBuilder#updateList(java.util.List<T>, java.util.Collection)
	 */
	public <T> List<T> updateList(List<T> beanList, Collection<QueryResult> result) throws DAOException {

		Class<?> beanClass = beanList.toArray().getClass().getComponentType();
		
		return updateList((Class<T>) beanClass, beanList, result);
		
	}
		
	@SuppressWarnings("unchecked")
	public <T> List<T> updateList(Class<T> beanClass, List<T> beanList, Collection<QueryResult> result) throws DAOException {
		
		try {
			for (QueryResult resp : result) {
				// recuperar do retList objeto com a mesma chave se tiver chave
				Object bean = get(beanList, null, null, resp);
				// se nao tem objeto, cria novo objeto
				if (bean == null) {
					bean = create((Class<T>) beanClass);
					beanList.add((T) bean);
				}
				// Set all the values in the new object
				Set<String> keySet = resp.getKeys();
				for (String key : keySet) {
					set(bean, null, key, resp);
				}

			}
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}		
		return beanList;
	}
	
	/**
	 * Retrieve the atributes that are anotated as primary keys  
	 * @param beanClass
	 * @param key of the bean
	 * @param resp QueryResult
	 * @return HashMap<String, Object> containing the primary keys
	 * @throws Exception
	 */
	private HashMap<String, Object> getPrimaryKey(Class<?> beanClass, String key, QueryResult resp) throws Exception {
		
		Field[] attrs = ClassUtil.getFields(beanClass);
		HashMap<String, Object> ret = new HashMap<String, Object>();
		Set<String> respKeySet = resp.getKeys();
		
		for (Field f : attrs) {
			String regex = (key!=null?key + "\\.":"") + f.getName() + "(\\.[\\p{Alnum}]*)*";
			if (f.isAnnotationPresent(PrimaryKey.class)) {
			// if (f.getAnnotation(PrimaryKey.class) != null) {
				for (String respKey : respKeySet) {
					if (respKey.matches(regex)) {
						ret.put(respKey, ClassUtil.cast(f.getType(), resp.get(respKey)) ) ;
					}						
				}
			}
		}
		
		return ret;
	}		
				
	/**
	 * Returns an attribute from the bean
	 * @param bean bean
	 * @param preKey first part of the key (bean key)
	 * @param key the key for the attribute
	 * @param resp full result set
	 * @return attribute object from the bean
	 * @throws Exception error
	 */
	@SuppressWarnings("unchecked")
	private <T> T get(Object bean, String preKey, String key, QueryResult resp) throws Exception {

		Object child = null;
		
		key = (key!=null)?key:"";
		
		// Collections
		if ((bean != null) && (bean instanceof Collection)){
			
			for (T itBean : (Collection<T>) bean) {
				
				boolean found = true;

				HashMap<String, Object> pKeys = getPrimaryKey(itBean.getClass(), preKey, resp);

				for (String pk : pKeys.keySet()) {
										
					String remainingKey = pk.substring( (preKey!=null && preKey.length()>0)?preKey.length()+1:0 );
					
					if (! pKeys.get(pk).equals(get(itBean, null, remainingKey, resp))) {
						found = false;
					}
					
				}

				if (found) {
					child = itBean;
					break;
				}
			}
			
		}
		
		// Sub-component - recursive
		if (key.indexOf('.') > 0) {
			Object first = get(bean, key.substring(0, key.indexOf('.')));
			child = get(first, 
						(preKey!=null?preKey + ".":"") + key.substring(0, key.indexOf('.')), 
						key.substring(key.indexOf('.') + 1), 
						resp);
		}
		
		if (child == null) {			
			child = get(bean, key);
		}
		
		return (T) child;
	}
	
	/**
	 * retrieve a child object
	 * @param bean
	 * @param key
	 * @return child object
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private <T> T get(Object bean, String key) throws Exception {
		
		T child = null;
		
		if (bean == null || key == null || key.length() <= 0) return null;
		
		Class<?> cl = bean.getClass();
		
		String mtdName = key;

		// Function
		if (key.indexOf('(') > 0) {
			mtdName = key.substring(0, key.indexOf('('));
		} else {
			// Accessor
			mtdName = key.replaceFirst(key.substring(0, 1), "get" + key.substring(0, 1).toUpperCase());
		}

		try {
			Method met = cl.getMethod(mtdName);
			child = (T) met.invoke(bean);
		} catch (NoSuchMethodException e) {
			logger.warn("No method " + mtdName + " found on " + cl.getName());
		}

		if (child == null) {
			try {
				Field fld = ClassUtil.getField(cl, key);				
				child = (T) fld.get(bean);			
			} catch (Exception e) {
				logger.warn("No field " + key + " found on " + cl.getName());
			}
		}
				
		return child;
	}
		
	/**
	 * Create a new instance of a class
	 * @param beanClass class type
	 * @return a bean of the corresponding bean class
	 * @throws Exception error
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> T create(Class<T> beanClass) throws Exception {
		
		T bean = null;
		
		if (! beanClass.isInterface()) {
			bean = beanClass.newInstance();
		} else

// Collections			
		if (beanClass.equals(Set.class)) {
			//Class<?> elementClass = (Class<?>) beanClass.getTypeParameters()[0].getGenericDeclaration();
			bean = (T) HashSet.class.newInstance();
		} else
		if (beanClass.equals(List.class)) {
			bean = (T) new ArrayList();
		}
// TODO			
//		if (beanClass.equals(Queue.class)) {
//		} else
		
		return bean;
	}
	
	/**
	 * 
	 * @param bean
	 * @param preKey
	 * @param key
	 * @param resp
	 * @throws Exception
	 */
	private void set(Object bean, String preKey, String key, QueryResult resp) throws Exception {
		
		Object value = resp.get((preKey!=null?preKey + ".":"") + key);
		if (bean == null || value == null) return;
				
		// Check for a Composite Bean - recursively create the classes for the fields
		if (key.indexOf('.') >= 0) {
			
			String firstKey = key.substring(0, key.indexOf('.'));
			String remainderKey = key.substring(key.indexOf('.') + 1);
			
			// Sets the first child - the first name of the key before the '.'
			Object firstChild = get(bean, preKey, firstKey, resp);
			Class<?> firstChildCl = null;
			try {
				firstChildCl = ClassUtil.getField(bean.getClass(), firstKey).getType(); 
			} catch (Exception e) {
				logger.error(e);
			}

			if (firstChild == null) {
				try {
					firstChild = create(firstChildCl);
					set(bean, firstKey, firstChild);
				} catch (Exception e) {
					logger.error("Impossible to create " + preKey + " on " + bean.getClass().getName());
				}				
			}

			// Determines if the first child is a Collection
			boolean isCollection = false;
			for (Class<?> fldIntfc : firstChildCl.getInterfaces()) {
				if (fldIntfc.equals(Collection.class)) {
					isCollection = true;
					break;
				}
			}			
			
			// In the case of a collection, we must set the child accordingly to the Primary Key of the child
			if (isCollection) {
				Object elementChild = get(firstChild, (preKey!=null?preKey+".":"") + firstKey, remainderKey, resp);
				
				if (elementChild == null) {
					try {
						Class<?> elementChildCl = (Class<?>)((ParameterizedType) ClassUtil.getField(bean.getClass(), firstKey).getGenericType()).getActualTypeArguments()[0];
						elementChild = create(elementChildCl);
						// Sets the primary keys
						HashMap<String, Object> pKeys = getPrimaryKey(elementChildCl, (preKey!=null?preKey+".":"") + firstKey, resp);
						for (String pk : pKeys.keySet()) {

							String fKey = pk.substring( ((preKey!=null?preKey + ".":"") + firstKey).length()+1 );
							set (elementChild, (preKey!=null?preKey + ".":"") + firstKey, fKey, resp);
						}
						
						add(firstChild, elementChild);
					} catch (Exception e) {
						logger.error("Impossible to create " + preKey + " on " + bean.getClass().getName());
					}										
				}
								
				set(elementChild, (preKey!=null?preKey + ".":"") + firstKey, remainderKey, resp);
			} else {
				// In the case of a composite bean, simply set the value for the first child
				set(firstChild, (preKey!=null?preKey + ".":"") + firstKey, remainderKey, resp);
			}
			
		} else {
			// Simple attribute, just set
			set (bean, key, value);
		}
	}
	
	
	/**
	 * Sets one of the attributes of the bean using reflection
	 * @param bean 
	 * @param value value
	 * @throws Exception
	 */
	private void set(Object bean, String key, Object value) throws Exception {
		
		// Proceed to set the value
		Class<?> cl = bean.getClass();
				
		Field fld;		
		try {
			fld = ClassUtil.getField(cl, key);				
		} catch (Exception ex2) {
			logger.warn("Field " + key + " doesn't exist on bean " + bean.getClass().getCanonicalName(), ex2);
			return;
		}

		Class<?> fldClass = fld.getType();		

		String mtdName = key.replaceFirst(key.substring(0, 1), "set" + key.substring(0, 1).toUpperCase());
		Class<?>[] metParams = new Class[1];
		metParams[0] = fldClass;

//		// Collection
//		for (Class<?> fldIntfc : fldClass.getInterfaces()) {
//			if (fldIntfc.equals(Collection.class)) {
////				mtdName = key.replaceFirst(key.substring(0, 1), "addTo" + key.substring(0, 1).toUpperCase());
////				metParams[0] = (Class<?>)((ParameterizedType)fld.getGenericType()).getActualTypeArguments()[0];
//				// Do nothing
//				return;
//			}
//		}
		
		value = ClassUtil.cast(metParams[0], value);
		
		Object[] params = new Object[1];
		params[0] = value;

		try {
			Method met = cl.getMethod(mtdName, metParams);
			met.invoke(bean, params);
		} catch (NoSuchMethodException ex1) {
			fld.set(bean, value);
		}
	}			

	
	@SuppressWarnings("unchecked")
	private void add(Object list, Object value) {	
		((Collection<Object>)list).add(value);
	}

}
