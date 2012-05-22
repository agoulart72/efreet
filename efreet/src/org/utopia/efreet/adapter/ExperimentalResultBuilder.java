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
 * Default implementation for a ResultBuilder
 * @author metal
 *
 */
@Unfinished
public class ExperimentalResultBuilder implements ResultBuilder {

	static Logger logger = Logger.getLogger(ExperimentalResultBuilder.class.getName());
	
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
		
		if (bean == null) return null;
		
		return update(bean.getClass(), bean, result);
	}

	public <T> T update(Class<?> beanClass, T bean, Collection<QueryResult> result) throws DAOException {

		try {
			BeanModel beanModel = null;

			for (QueryResult resp : result) {
				// Cria um model para o bean
				if (beanModel == null) {
					beanModel = buildModel(beanClass, resp);
				}
				// Set all the values in the new object
				Set<String> keySet = resp.getKeys();
				for (String key : keySet) {
					set(bean, beanModel, key, resp);
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
			BeanModel beanModel = null;
						
			for (QueryResult resp : result) {
				// Cria um model para o bean
				if (beanModel == null) {
					beanModel = buildModel(beanClass, resp);
				}
				// recuperar do retList objeto com a mesma chave se tiver chave
				Object bean = get(beanList, beanModel, null, resp);
				// se nao tem objeto, cria novo objeto
				if (bean == null) {
					bean = create(beanModel);
					beanList.add((T) bean);
				}
				// Set all the values in the new object
				Set<String> keySet = resp.getKeys();
				for (String key : keySet) {
					set(bean, beanModel, key, resp);
				}

			}
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}		
		return beanList;
	}
		
	/**
	 * Create a bean model based on the model 
	 * @param key key of the bean in teh result name
	 * @param beanClass class of the bean
	 * @param result one result from the set
	 * @return a bean model
	 */
	private <T> BeanModel buildModel(Class<T> beanClass, QueryResult result) throws DAOException {
		
		BeanModel bm = new BeanModel();
		
		bm.setBeanClass(beanClass);
		bm.setKey(null);

		Set<String> keySet = result.getKeys();

		for (String respKey : keySet) {
			
			String[] rKeySeq = respKey.split("\\.");
			
			BeanModel tmpModel = bm;
			Class<?> tmpClass = beanClass;
			String fullKey = "";
			
			// Non-recursive way of building the model
			for (int i = 0; (i < rKeySeq.length) && (tmpModel != null) && (tmpClass != null); i++) {
				fullKey = ((fullKey.length() > 0)?fullKey + ".":"") + rKeySeq[i];
				try {
					Field f = null;
					try {
						f = ClassUtil.getField(tmpClass, rKeySeq[i]);
					} catch (Exception e) {
						logger.warn("No field " + rKeySeq[i] + " found on " + tmpClass.getName());
						break;
					}

					// Attribute
					tmpModel.addAttribute(fullKey, f.getType());
					
					// PKs
					if (f.isAnnotationPresent(PrimaryKey.class)) {
						tmpModel.addPrimaryKey( fullKey );
					}
					// Accessors
					String getMtdName = null;
					if (rKeySeq[i].indexOf('(') > 0) {
						getMtdName = rKeySeq[i].substring(0, rKeySeq[i].indexOf('('));
					} else {
						getMtdName = rKeySeq[i].replaceFirst(rKeySeq[i].substring(0, 1), "get" + rKeySeq[i].substring(0, 1).toUpperCase());
					}
					try {
						Method met = tmpClass.getMethod(getMtdName);
						tmpModel.addToGetMethods( fullKey, met);
					} catch (NoSuchMethodException e) {
						logger.warn("No method " + getMtdName + " found on " + tmpClass.getName());
					}

					String setMtdName = rKeySeq[i].replaceFirst(rKeySeq[i].substring(0, 1), "set" + rKeySeq[i].substring(0, 1).toUpperCase());
					Class<?>[] metParams = new Class[1];
					metParams[0] = f.getType();
					
					try {
						Method met = tmpClass.getMethod(setMtdName, metParams);
						tmpModel.addToSetMethods( fullKey, met);
					} catch (NoSuchMethodException ex1) {
						logger.warn("No method " + setMtdName + " found on " + tmpClass.getName());
					}

					// Model
					if (i + 1 < rKeySeq.length) {
						if (tmpModel.getChild(rKeySeq[i]) == null) {
							
							BeanModel tmpModel2 = new BeanModel();
							tmpModel2.setKey( fullKey );
							tmpModel2.setBeanClass(f.getType());
							
							// Determines if the child is a Collection - uses the element type as class instead of the collection class
							for (Class<?> fldIntfc : f.getType().getInterfaces()) {
								if (fldIntfc.equals(Collection.class)) {
									tmpModel2.setCollection(true);
									tmpModel2.setBeanClass((Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]);
									break;
								}
							}			
							
							tmpModel.addChild( fullKey, tmpModel2);
						}
					}

					// non-recursive traversal
					tmpModel = tmpModel.getChild(fullKey);
					tmpClass = (tmpModel!=null)?tmpModel.getBeanClass():null;
					
				} catch (Exception e) {
					throw new DAOException("Error : Field [" + rKeySeq[i] + "] does not exist on " + tmpClass.getName() + "\n " + e.getMessage());
				}

			} // non-recursive loop
			
		}
				
		return bm;
	}
	
	/**
	 * Retrieve the atributes that are anotated as primary keys  
	 * @param model bean model
	 * @param key of the bean
	 * @param resp QueryResult
	 * @return HashMap<String, Object> containing the primary keys
	 * @throws Exception
	 */
	private HashMap<String, Object> getPrimaryKey(BeanModel model, QueryResult resp) throws Exception {
		
		HashMap<String, Object> ret = new HashMap<String, Object>();
		Set<String> respKeySet = resp.getKeys();

		for (String respKey : respKeySet) {
			if (model.getPrimaryKeys().contains(respKey)) {
				ret.put(respKey, ClassUtil.cast(model.getAttributeClass(respKey), resp.get(respKey)) ) ;
			}
		}		
		
		return ret;
	}
	
			
	@SuppressWarnings("unchecked")
	/**
	 * Returns an attribute from the bean
	 * @param bean bean
	 * @param model bean model
	 * @param key the key for the attribute
	 * @param resp full result set
	 * @return attribute object from the bean
	 * @throws Exception error
	 */
	private <T> T get(Object bean, BeanModel model, String key, QueryResult resp) throws Exception {

		Object child = null;
		
		key = (key!=null)?key:"";
		
		// Collections
		if ((bean != null) && (bean instanceof Collection)){

			HashMap<String, Object> pKeys = getPrimaryKey(model, resp);

			for (T itBean : (Collection<T>) bean) {
				
				boolean found = true;
				
				for (String pk : pKeys.keySet()) {
										
					if (! pKeys.get(pk).equals(get(itBean, model, key, resp))) {
						found = false;
					}
					
				}

				if (found) {
					child = itBean;
					break;
				}
			}			
		}
		
		// Sub-component
		String remKey = key.substring(model.getKey().length() + 1);		
		if (remKey.indexOf('.') > 0) {
			String firstKey = key.substring(0, model.getKey().length() + 1 + remKey.indexOf('.'));
			Object first = get(bean, model, firstKey);
			BeanModel childModel = model.getChild(firstKey);
			child = get(first, childModel, key,	resp);
		}
		
		if (child == null) {			
			child = get(bean, model, key);
		}
		
		return (T) child;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * retrieve a child object
	 * @param model bean
	 * @param bean
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private <T> T get(Object bean, BeanModel model, String key) throws Exception {
		
		T child = null;
		
		if (bean == null || model == null || key == null || key.length() <= 0) return null;
		
		Class<?> cl = model.getBeanClass();
		
		Method met = model.getGetMethod(key);
		child = (T) met.invoke(bean);

		if (child == null) {
			try {
				Field fld = ClassUtil.getField(cl, key.substring(key.lastIndexOf('.')>0?key.lastIndexOf('.'):0) );				
				child = (T) fld.get(bean);			
			} catch (Exception e) {
				logger.warn("No field " + key + " found on " + cl.getName());
			}
		}
				
		return child;
	}
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * Create a new instance of a class
	 * @param beanClass class type
	 * @return a bean of the corresponding bean class
	 * @throws Exception error
	 */
	private <T> T create(BeanModel beanModel) throws Exception {

		T bean = null;
		
		if (! beanModel.getBeanClass().isInterface()) {
			bean = (T) beanModel.getBeanClass().newInstance();
		} else

// Collections			
		if (beanModel.getBeanClass().equals(Set.class)) {
			//Class<?> elementClass = (Class<?>) beanClass.getTypeParameters()[0].getGenericDeclaration();
			bean = (T) HashSet.class.newInstance();
		} else
		if (beanModel.getBeanClass().equals(List.class)) {
			bean = (T) new ArrayList();
		}
// TODO			
//		if (beanClass.equals(Queue.class)) {
//		} else
		
		return bean;

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * Create a new instance of a class
	 * @param beanClass class type
	 * @return a bean of the corresponding bean class
	 * @throws Exception error
	 */
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
	 * Sets on of the attributes of the bean
	 * @param bean
	 * @param preKey
	 * @param key
	 * @param postKey
	 * @param resp
	 * @throws Exception
	 */
	private void set(Object bean, BeanModel model, String key, QueryResult resp) throws Exception {
		
		Object value = resp.get(key);
		if (bean == null || value == null) return;
				
		String[] rKeySeq = key.split("\\.");
		BeanModel tmpModel = model;
		Object tmpBean = bean;
		String fullKey = "";

		// Non-recursive way of setting the bean
		for (int i = 0; (i < rKeySeq.length) && (tmpModel != null) && (tmpBean != null); i++) {

			fullKey = ((fullKey.length() > 0)?fullKey + ".":"") + rKeySeq[i];

			// Non-recursive setting of children
			if (i + 1 < rKeySeq.length) {
				tmpBean = get(tmpBean, tmpModel, fullKey, resp);
				tmpModel = tmpModel.getChild(fullKey);
				
				if (tmpBean == null) {
					try {
						tmpBean = create(tmpModel.getBeanClass());
						if (tmpModel.isCollection()) {
							Object tmpCol = create(List.class);
							// Set all the PKs first (this one is recursive)
							HashMap<String, Object> pKeys = getPrimaryKey(tmpModel, resp);
							for (String pk : pKeys.keySet()) {
								set(tmpBean, tmpModel, pk, resp);
							}
							add(tmpCol, tmpBean);
						}
					} catch (Exception e) {
						logger.error("Impossible to create " + fullKey + " on " + tmpModel.getKey());
					}				
					
				}				
			} else {
				set (tmpBean, tmpModel, key, value);
			}					
		}	// non-recursive loop through the key sequence	
	}
	
	/**
	 * Sets one of the attributes of the bean
	 * @param bean bean
	 * @param model bean model
	 * @param key key
	 * @param value value
	 * @throws Exception error
	 */
	private void set(Object bean, BeanModel model, String key, Object value) throws Exception {
			
		value = ClassUtil.cast(model.getAttributeClass(key), value);

		Object[] params = new Object[1];
		params[0] = value;

		try {
			Method met = model.getSetMethod(key);
			met.invoke(bean, params);
		} catch (Exception e) {
			try {
				Field fld = ClassUtil.getField(model.getAttributeClass(key), key.substring(key.lastIndexOf('.')>0?key.lastIndexOf('.'):0) );				
				fld.set(bean, value);			
			} catch (Exception e2) {
				logger.warn("No field " + key + " found on " + model.getKey());
			}
			
		}
		
	}	
	
	@SuppressWarnings("unchecked")
	private void add(Object list, Object value) {	
		((Collection<Object>)list).add(value);
	}

}
