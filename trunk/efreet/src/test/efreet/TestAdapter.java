package test.efreet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.utopia.efreet.QueryResult;
import org.utopia.efreet.adapter.DefaultResultBuilder;
import org.utopia.efreet.adapter.ParameterAdapter;
import org.utopia.efreet.adapter.ReflectiveResultBuilder;
import org.utopia.efreet.adapter.ResultAdapter;
import org.utopia.efreet.adapter.ResultBuilder;

/**
 * @author Adriano M Goulart
 * @version $Id: TestAdapter.java,v 1.2 2012-05-22 13:32:23 agoulart Exp $
 * 
 */
public class TestAdapter extends TestCase {

	BigDecimal propBD1;
	BigDecimal propBD2;
	Date propDt1;
	Date propDt2;
	int propInt1;
	int propInt2;
	String propStr1;
	String propStr2;
	String propStr3;
	
	/**
	 * QR1 :
	 *       BD1 - 1 (@PrimaryKey)
	 *       BD2 - 2
	 *       DT1 - Sysdate
	 *       DT2 - Sysdate
	 *       Int1 - 1
	 *       Int2 - 2
	 *       Str1 - str1
	 *       Str2 - str2
	 *       noAccessorProp - str3
	 *       innerBean :
	 *       	Str1 - str1
	 *       	Str2 - str2
	 *       	int1 - 1
	 *       	int2 - 2
	 *       	noAccessorProp - str1
	 *       children[] :
	 *       	0 :
	 *       		BD1 - 1
	 *       		Str1 - null
	 */
	QueryResult qr1;
	/**
	 * QR2 :
	 * 		BD1  - 1 (@PrimaryKey)
	 * 		children[] :
	 * 			0 :
	 * 				BD1 - 1
	 * 				Str1 - str1
	 */
	QueryResult qr2;
	/**
	 * QR3
	 * 		BD1  - 1 (@PrimaryKey)
	 * 		children[] :
	 * 			0 :
	 * 				BD1 - 2
	 * 				Str1 - str2
	 */
	QueryResult qr3;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {

		propBD1 = new BigDecimal(1);
		propBD2 = new BigDecimal(2);
		propDt1 = new Date();
		propDt2 = new Date();
		propInt1 = 1;
		propInt2 = 2;
		propStr1 = "str1";
		propStr2 = "str2";
		propStr3 = "str3";

		qr1 = new QueryResult();
		
		qr1.set("propBD1", propBD1);
		qr1.set("propBD2", propBD2);
		qr1.set("propDt1", propDt1);
		qr1.set("propDt2", propDt2);
		qr1.set("propInt1", propInt1);
		qr1.set("propInt2", propInt2);
		qr1.set("propStr1", propStr1);
		qr1.set("propStr2", propStr2);
		qr1.set("noAccessorProp", propStr3);

		qr1.set("innerBean.propStr1", propStr1);
		qr1.set("innerBean.propStr2", propStr2);
		qr1.set("innerBean.propInt1", propInt1);
		qr1.set("innerBean.propInt2", propInt2);
		qr1.set("innerBean.noAccessorProp", propStr1);
		
		qr1.set("children.propBD1", propBD1);
		qr1.set("children.propStr1", null);		
		
		qr2 = new QueryResult();
		
		qr2.set("propBD1", propBD1);
		qr2.set("children.propBD1", propBD1);		
		qr2.set("children.propStr1", propStr1);		

		qr3 = new QueryResult();
		
		qr3.set("propBD1", propBD1);
		qr3.set("children.propBD1", propBD2);		
		qr3.set("children.propStr1", propStr2);		

		super.setUp();
	}

	private void assertResult1(BeanTest bean) {
	
		assertEquals(propBD1, bean.getPropBD1());
		assertEquals(propBD2, bean.getPropBD2());
		assertEquals(propDt1, bean.getPropDt1());
		assertEquals(propDt2, bean.getPropDt2());
		assertEquals(propInt1, bean.getPropInt1());
		assertEquals(propInt2, bean.getPropInt2());
		assertEquals(propStr1, bean.getPropStr1());
		assertEquals(propStr2, bean.getPropStr2());
		assertEquals(propStr3, bean.noAccessorProp);

		assertEquals(propStr1, bean.getInnerBean().getPropStr1());
		assertEquals(propStr2, bean.getInnerBean().getPropStr2());
		assertEquals(propInt1, bean.getInnerBean().getPropInt1());
		assertEquals(propInt2, bean.getInnerBean().getPropInt2());
		assertEquals(propStr1, bean.getInnerBean().noAccessorProp);

	}
	
	private void assertResult2(BeanTest bean) {
		
		assertNotNull(bean.getChildren());
		assertEquals(propBD1, bean.getChildren().get(0).getPropBD1());
		assertEquals(propStr1, bean.getChildren().get(0).getPropStr1());
	}
	
	private void assertResult3(BeanTest bean) {
		
		assertNotNull(bean.getChildren());
		assertEquals(propBD2, bean.getChildren().get(1).getPropBD1());
		assertEquals(propStr2, bean.getChildren().get(1).getPropStr1());
	}

	
	/**
	 * Test method for {@link org.utopia.efreet.adapter.ParameterAdapter#ParameterAdapter(java.lang.Object)}.
	 */
	public final void testParameterAdapter() throws Exception {
		
		BeanTest bean = new BeanTest();
		bean.setPropBD1(propBD1);
		bean.setPropBD2(propBD2);
		bean.setPropDt1(propDt1);
		bean.setPropDt2(propDt2);
		bean.setPropInt1(propInt1);
		bean.setPropInt2(propInt2);
		bean.setPropStr1(propStr1);
		bean.setPropStr2(propStr2);
		
		String[][] propArr = new String[3][3];
		propArr[0][0] = "0_0";
		propArr[0][1] = "0_1";
		propArr[1][0] = "1_0";
		propArr[1][1] = "1_1";
		propArr[1][2] = "1_2";	
		bean.setPropArr1(propArr);
		
		System.out.println(System.currentTimeMillis());
		
		ParameterAdapter pa = new ParameterAdapter(bean);

		System.out.println(System.currentTimeMillis());

		assertEquals(propBD1, pa.get("propBD1"));
		assertEquals(propBD2, pa.get("propBD2"));
		assertEquals(propDt1, pa.get("propDt1"));
		assertEquals(propDt2, pa.get("propDt2"));
		assertEquals(new Integer(propInt1), pa.get("propInt1"));
		assertEquals(new Integer(propInt2), pa.get("propInt2"));
		assertEquals(propStr1, pa.get("propStr1"));
		assertEquals(propStr2, pa.get("propStr2"));
		assertEquals(propArr[0][0], pa.get("propArr1[0][0]"));
		assertEquals(propArr[1][0], pa.get("propArr1[1][0]"));

	}

	public final void testBuild() throws Exception {

		Collection<QueryResult> col = new ArrayList<QueryResult>();
		
		col.add(qr1);
		col.add(qr2);
		col.add(qr3);
		
		ResultAdapter ra = new ResultAdapter(col);
		
		BeanTest bean = (BeanTest) ra.build(BeanTest.class);
	
		assertResult1(bean);
		assertResult2(bean);
		assertResult3(bean);
	}

	public final void testUpdate1() throws Exception {
		testUpdate(new DefaultResultBuilder());
	}

	public final void testUpdate2() throws Exception {
		testUpdate(new ReflectiveResultBuilder());
	}

	public final void testUpdate(ResultBuilder builder) throws Exception {
		
		BeanTest bean = new BeanTest();
		bean.setPropBD1(propBD1);
		
		Collection<QueryResult> col = new ArrayList<QueryResult>();
		
		col.add(qr1);

		ResultAdapter ra = new ResultAdapter(col);
		
		bean = ra.update(bean, builder);

		assertResult1(bean);
	}
	
	public final void testBuildList1() throws Exception {
		testBuildList(new DefaultResultBuilder());
	}

	public final void testBuildList2() throws Exception {
		testBuildList(new ReflectiveResultBuilder());
	}
	
	public final void testBuildList(ResultBuilder builder) throws Exception {
		
		Collection<QueryResult> col = new ArrayList<QueryResult>();
		
		col.add(qr1);
		
		ResultAdapter ra = new ResultAdapter(col);
		
		List<BeanTest> beanList = ra.buildList(BeanTest.class, builder);
		
		assertNotNull(beanList);
		assertEquals(1, beanList.size());
		assertResult1(beanList.get(0));
	}
	
}
