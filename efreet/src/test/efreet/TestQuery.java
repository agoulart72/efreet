package test.efreet;

import java.util.Hashtable;

import org.utopia.efreet.Query;

import junit.framework.TestCase;

public class TestQuery extends TestCase {

	/*
	 * Test method for 'org.utopia.efreet.Query.setName(String)'
	 */
	public final void testSetName() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.getName()'
	 */
	public final void testGetName() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.setType(int)'
	 */
	public final void testSetType() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.getType()'
	 */
	public final void testGetType() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.setStatement(String)'
	 */
	public final void testSetStatement() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.getStatement()'
	 */
	public final void testGetStatement() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.getStatement(Hashtable)'
	 */
	public final void testGetStatementHashtable() {
		// TODO Auto-generated method stub
		Query query = new Query();
		String param = "select * from TABELA where COLUNA = 1 ORDER BY ${TESTE} ";
		query.setStatement(param);
		Hashtable<String, String> variables = new Hashtable<String, String>();
		variables.put("TESTE", "valor");
		String result = query.getStatement(variables);
		assertEquals("select * from TABELA where COLUNA = 1 ORDER BY valor ", result);
	}

	/*
	 * Test method for 'org.utopia.efreet.Query.addParameter(int)'
	 */
	public final void testAddParameter() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.parameterSize()'
	 */
	public final void testParameterSize() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.getParameter(int)'
	 */
	public final void testGetParameter() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.addResult(String)'
	 */
	public final void testAddResult() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.addResultAt(String, int)'
	 */
	public final void testAddResultAt() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.resultSize()'
	 */
	public final void testResultSize() {
		// TODO Auto-generated method stub

	}

	/*
	 * Test method for 'org.utopia.efreet.Query.getResult(int)'
	 */
	public final void testGetResult() {
		// TODO Auto-generated method stub

	}

}
