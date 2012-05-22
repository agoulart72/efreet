package org.utopia.efreet.jasper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

import org.utopia.efreet.DAOException;
import org.utopia.efreet.QueryResult;

/**
 * @author metal
 * @version $Id: JREfreetDataSource.java,v 1.1 2012-05-22 13:36:53 agoulart Exp $
 */
public class JREfreetDataSource implements JRRewindableDataSource {

	/**
	 *
	 */
	private Collection<QueryResult> data = null;
	private Iterator<QueryResult> iterator = null;
	private QueryResult currentQr = null;
	
	/**
	 *
	 */
	protected PropertyNameProvider propertyNameProvider = null;

	protected static final PropertyNameProvider FIELD_NAME_PROPERTY_NAME_PROVIDER =
		new PropertyNameProvider()
		{
			public String getPropertyName(JRField field) 
			{
				return field.getName();
			}
		};

	protected static final PropertyNameProvider FIELD_DESCRIPTION_PROPERTY_NAME_PROVIDER =
		new PropertyNameProvider()
		{
			public String getPropertyName(JRField field) 
			{
				if (field.getDescription() == null)
				{
					return field.getName();
				}
				return field.getDescription();
			}
		};

	/**
	 *
	 */
	interface PropertyNameProvider
	{
		public String getPropertyName(JRField field);
	}

	protected String getPropertyName(JRField field)
	{
		return propertyNameProvider.getPropertyName(field);
	}

	/**
	 * @param data
	 */
	public JREfreetDataSource(Collection<QueryResult> data) {
		this (data, true);
	}
	
	/**
	 * @param data
	 */
	public JREfreetDataSource(Collection<QueryResult> data, boolean isUseFieldDescription) {
		super();
		propertyNameProvider = isUseFieldDescription ? 
				FIELD_DESCRIPTION_PROPERTY_NAME_PROVIDER : 
				FIELD_NAME_PROPERTY_NAME_PROVIDER;
		this.data = data;
	}
	
	public JREfreetDataSource(QueryResult data) {
		this (data, true);
	}
	
	public JREfreetDataSource(QueryResult data, boolean isUseFieldDescription) {
		super();
		propertyNameProvider = isUseFieldDescription ? 
				FIELD_DESCRIPTION_PROPERTY_NAME_PROVIDER : 
				FIELD_NAME_PROPERTY_NAME_PROVIDER;
		this.data = new ArrayList<QueryResult>();
		this.data.add(data);
	}
	
	/**
	 *
	 */
	public boolean next()
	{
		boolean hasNext = false;
		
		if (this.iterator == null) {
			moveFirst();
		}
		
		if (this.iterator != null)
		{
			hasNext = this.iterator.hasNext();
			
			if (hasNext)
			{
				this.currentQr = this.iterator.next();
			}
		}
		
		return hasNext;
	}
	
	
	/**
	 *
	 */
	public void moveFirst()
	{
		if (this.data != null)
		{
			this.iterator = this.data.iterator();
		}
	}

	/**
	 *
	 */
	public Object getFieldValue(JRField field) throws JRException
	{
		return getFieldValue(currentQr, field);
	}

	protected Object getFieldValue(QueryResult qr, JRField field) throws JRException
	{
		return getBeanProperty(qr, getPropertyName(field));
	}

	protected static Object getBeanProperty(QueryResult qr, String propertyName) throws JRException
	{
		Object value = null;
	
		if (qr != null && propertyName != null) {
			try {
				value = qr.getString(propertyName); // WARN : Always get as String to avoid problems with Jasper
			} catch (DAOException e) {
				throw new JRException("Error retrieving field value from bean : " + propertyName, e);
			}
		}
		
		return value;
	}
}
