package test.efreet;

import java.math.BigDecimal;

import org.utopia.efreet.adapter.PrimaryKey;

/**
 * @author Adriano M Goulart
 * @version $Id: BeanChildTest.java,v 1.1 2012-05-22 13:32:23 agoulart Exp $
 * 
 */
public class BeanChildTest {

	@PrimaryKey private BigDecimal propBD1;

	private String propStr1;
	
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
	
	public String toString() {
				
		return "BD1@PK=" + propBD1 + " Str1=" + propStr1;		
	}
	
}
