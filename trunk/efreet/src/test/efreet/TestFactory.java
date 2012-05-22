package test.efreet;

import java.sql.Types;
import java.util.SortedSet;

import org.utopia.efreet.DAOFactory;
import org.utopia.efreet.DAOModel;
import org.utopia.efreet.ParameterModel;
import org.utopia.efreet.QueryModel;
import org.utopia.efreet.ResultModel;

import junit.framework.TestCase;

public class TestFactory extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public final void testReadXML_1() throws Exception {
		DAOModel myTestModel = DAOFactory.readXML("test/efreet/test");
		innerTest (myTestModel, "test1");
	}

	public void testReadXML_2() throws Exception {
		DAOModel myTestModel = DAOFactory.readXML("test/efreet/test");
		innerTest (myTestModel, "test2");
	}

	public final void testReadXML_3() throws Exception {
		DAOModel myTestModel = DAOFactory.readXML("test/efreet/test");
		innerTest (myTestModel, "test3");
	}

	public final void testReadXML_4() throws Exception {
		DAOModel myTestModel = DAOFactory.readXML("test/efreet/test");
		innerTest (myTestModel, "test4");
	}

	public final void testReadXML_5() throws Exception {
		DAOModel myTestModel = DAOFactory.readXML("test/efreet/test");
		innerTest (myTestModel, "test5");
	}

	public final void testReadXML_6() throws Exception {
		DAOModel myTestModel = DAOFactory.readXML("test/efreet/test");
		innerTest (myTestModel, "test6");
	}	
	
	private void innerTest(DAOModel testModel, String testCase) throws Exception {
		
		QueryModel testQueryModel = testModel.getQuery(testCase);
		assertNotNull(testQueryModel);	
		if (testQueryModel != null) {
			SortedSet<ParameterModel> params = testQueryModel.getParameters();			
			assertNotNull(params);
			for (ParameterModel pm : params) {
				switch (pm.getParamIndex()) {
				case 1 : assertEquals("param1", pm.getParamName());
						 assertEquals(Types.NUMERIC, pm.getParamType()); break;
				case 2 : // Test 5
					 assertEquals("param2", pm.getParamName());
					 assertEquals(Types.CHAR, pm.getParamType()); break;
				default : fail("Index " + pm.getParamIndex());
				}
			}
			SortedSet<ResultModel> results = testQueryModel.getResults();
			assertNotNull(results);
			for (ResultModel rm : results) {
				switch (rm.getResultIndex()) {
				case 1 : assertEquals("result1", rm.getResultName());
						 assertEquals(Types.NUMERIC, rm.getResultType()); break;
				default : fail("Index " + rm.getResultIndex());
				}					
			}
		}			
	}
}
