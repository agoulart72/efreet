package test.efreet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.utopia.efreet.adapter.PrimaryKey;

/**
 * @author Adriano M Goulart
 * @version $Id: BeanTest.java,v 1.2 2012-05-22 13:32:23 agoulart Exp $
 * 
 */
public class BeanTest {

	@PrimaryKey private BigDecimal propBD1;
	private BigDecimal propBD2;
	
	private int propInt1;
	private int propInt2;
	
	private String propStr1;
	private String propStr2;
	
	private java.util.Date propDt1;
	private java.util.Date propDt2;
	
	private String[][] propArr1;
	
	public String noAccessorProp;
	
	private BeanTest innerBean;
	
	private List<BeanChildTest> children;
	
	/**
	 * @return the propBD1
	 */
	public BigDecimal getPropBD1() {
		return propBD1;
	}
	/**
	 * @param propBD1 the propBD1 to set
	 */
	public void setPropBD1(BigDecimal propBD1) {
		this.propBD1 = propBD1;
	}
	/**
	 * @return the propBD2
	 */
	public BigDecimal getPropBD2() {
		return propBD2;
	}
	/**
	 * @param propBD2 the propBD2 to set
	 */
	public void setPropBD2(BigDecimal propBD2) {
		this.propBD2 = propBD2;
	}
	/**
	 * @return the propDt1
	 */
	public java.util.Date getPropDt1() {
		return propDt1;
	}
	/**
	 * @param propDt1 the propDt1 to set
	 */
	public void setPropDt1(java.util.Date propDt1) {
		this.propDt1 = propDt1;
	}
	/**
	 * @return the propDt2
	 */
	public java.util.Date getPropDt2() {
		return propDt2;
	}
	/**
	 * @param propDt2 the propDt2 to set
	 */
	public void setPropDt2(java.util.Date propDt2) {
		this.propDt2 = propDt2;
	}
	/**
	 * @return the propInt1
	 */
	public int getPropInt1() {
		return propInt1;
	}
	/**
	 * @param propInt1 the propInt1 to set
	 */
	public void setPropInt1(int propInt1) {
		this.propInt1 = propInt1;
	}
	/**
	 * @return the propInt2
	 */
	public int getPropInt2() {
		return propInt2;
	}
	/**
	 * @param propInt2 the propInt2 to set
	 */
	public void setPropInt2(int propInt2) {
		this.propInt2 = propInt2;
	}
	/**
	 * @return the propStr1
	 */
	public String getPropStr1() {
		return propStr1;
	}
	/**
	 * @param propStr1 the propStr1 to set
	 */
	public void setPropStr1(String propStr1) {
		this.propStr1 = propStr1;
	}
	/**
	 * @return the propStr2
	 */
	public String getPropStr2() {
		return propStr2;
	}
	/**
	 * @param propStr2 the propStr2 to set
	 */
	public void setPropStr2(String propStr2) {
		this.propStr2 = propStr2;
	}
	/**
	 * @return the propArr1
	 */
	public String[][] getPropArr1() {
		return propArr1;
	}
	/**
	 * @param propArr1 the propArr1 to set
	 */
	public void setPropArr1(String[][] propArr1) {
		this.propArr1 = propArr1;
	}
	/**
	 * @return the innerBean
	 */
	public BeanTest getInnerBean() {
		return innerBean;
	}
	/**
	 * @param innerBean the innerBean to set
	 */
	public void setInnerBean(BeanTest innerBean) {
		this.innerBean = innerBean;
	}
	/**
	 * @return the children
	 */
	public List<BeanChildTest> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<BeanChildTest> children) {
		this.children = children;
	}

	public void addToChildren(BeanChildTest child) {
		if (children == null) {
			children = new ArrayList<BeanChildTest>();
		}
		children.add(child);
	}
	
	public String toString() {
				
		return "BD1@PK=" + propBD1 + " BD2=" + propBD2
			+ " Int1=" + propInt1 + " Int2=" + propInt2
			+ " Str1=" + propStr1 + " Str2=" + propStr2
			+ " Dt1=" + propDt1 + " Dt2=" + propDt2
			+ " propArr1=[" + propArr1 + "]"
			+ " noAccessorProp=" + noAccessorProp
			+ " innerBean=[" + String.valueOf(innerBean) + "]"
			+ " children={" + String.valueOf(children) + "}";		
	}
	
}
