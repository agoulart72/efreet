/**
 * 
 */
package test.efreet;

import java.math.BigDecimal;
import java.util.Date;

import org.utopia.efreet.ParameterAdapter;
import org.utopia.efreet.QueryResult;
import org.utopia.efreet.ResultAdapter;

import junit.framework.TestCase;

/**
 * @author Adriano M Goulart
 * @version $Id: TestAdapter.java,v 1.1 2007-01-30 18:14:57 agoulart Exp $
 * 
 */
public class TestAdapter extends TestCase {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test method for {@link org.utopia.efreet.ParameterAdapter#ParameterAdapter(java.lang.Object)}.
	 */
	public final void testParameterAdapter() throws Exception {
		
		BigDecimal propBD1 = new BigDecimal(1);
		BigDecimal propBD2 = new BigDecimal(2);
		Date propDt1 = new Date();
		Date propDt2 = new Date();
		int propInt1 = 1;
		int propInt2 = 2;
		String propStr1 = "str1";
		String propStr2 = "str2";
		
		BeanTest bean = new BeanTest();
		bean.setPropBD1(propBD1);
		bean.setPropBD2(propBD2);
		bean.setPropDt1(propDt1);
		bean.setPropDt2(propDt2);
		bean.setPropInt1(propInt1);
		bean.setPropInt2(propInt2);
		bean.setPropStr1(propStr1);
		bean.setPropStr2(propStr2);
		
		ParameterAdapter pa = new ParameterAdapter(bean);
		
		assertEquals(propBD1, pa.get("propBD1"));
		assertEquals(propBD2, pa.get("propBD2"));
		assertEquals(propDt1, pa.get("propDt1"));
		assertEquals(propDt2, pa.get("propDt2"));
		assertEquals(new Integer(propInt1), pa.get("propInt1"));
		assertEquals(new Integer(propInt2), pa.get("propInt2"));
		assertEquals(propStr1, pa.get("propStr1"));
		assertEquals(propStr2, pa.get("propStr2"));
		
	}

	public final void testResultAdapter() throws Exception {

		QueryResult qr = new QueryResult();
		
		BigDecimal propBD1 = new BigDecimal(1);
		BigDecimal propBD2 = new BigDecimal(2);
		Date propDt1 = new Date();
		Date propDt2 = new Date();
		int propInt1 = 1;
		int propInt2 = 2;
		String propStr1 = "str1";
		String propStr2 = "str2";
		
		qr.set("propBD1", propBD1);
		qr.set("propBD2", propBD2);
		qr.set("propDt1", propDt1);
		qr.set("propDt2", propDt2);
		qr.set("propInt1", propInt1);
		qr.set("propInt2", propInt2);
		qr.set("propStr1", propStr1);
		qr.set("propStr2", propStr2);

		ResultAdapter ra = new ResultAdapter(qr);

		BeanTest bean = (BeanTest) ra.fill(new BeanTest());
		
		assertEquals(propBD1, bean.getPropBD1());
		assertEquals(propBD2, bean.getPropBD2());
		assertEquals(propDt1, bean.getPropDt1());
		assertEquals(propDt2, bean.getPropDt2());
		assertEquals(propInt1, bean.getPropInt1());
		assertEquals(propInt2, bean.getPropInt2());
		assertEquals(propStr1, bean.getPropStr1());
		assertEquals(propStr2, bean.getPropStr2());

	}

}
