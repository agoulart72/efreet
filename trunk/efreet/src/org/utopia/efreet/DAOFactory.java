package org.utopia.efreet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Types;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * This class is the main factory for the DAO creation, it reads and parses
 * the XML files containing the models for different DAOs.
 */
public class DAOFactory
{
    private static Hashtable<String, DAOModel> models = null;    

    static Object LOCK = new Object();
    
    static Logger logger = Logger.getLogger(DAOFactory.class.getName());

    /**
     * Main function to create DAOs <br>
     * If the factory does not find the model for the the name, it will try
     * to read an XML file from the resources path.
     * @param name Name of the DAO
     * @return A new DAO based on the model of the same name
     */
    public static DataAccessObject createDAO(String name) 
        throws EfreetException
    {
        if (name == null) {
        	logger.error("DAO name is not defined");
            throw new EfreetException("DAO name is not defined");
        }

        DAOModel dm = null;
        
        synchronized (LOCK) {
            if (models == null) models = new Hashtable<String, DAOModel>();
            dm = models.get(name);

            if (dm == null) {
                dm = readXML(name);
//                setContextParameters(dm);
                models.put(name, dm);
            }			
		}

        if (dm == null) {
        	logger.error("DAO model is not defined");
            throw new EfreetException("DAO model is not defined");
        }

        DataAccessObject dao = new DataAccessObject();
        dao.setModel(dm);
        
        return dao;
    }

    /**
     * To use a datasource that's not referenced by a JNDI, this way the datasource will be available 
     * for all the daos.
     * @param dsName Datasource Name to be referenced
     * @param dataSource the datasource object
     * @throws Exception error
     */
    public static void registerDataSource(String dsName, javax.sql.DataSource dataSource) throws Exception {
    	DataSource.registerDataSource(dsName, dataSource);
    }
    
    /**
     * Recover the URL based on the name of the DAO
     * it searches the current classloader first (for non-web applications)
     * then it searches the context for any specific user variable
     * then it searches in the contextloader 
     * @param name
     * @return URL
     * @throws EfreetException
     */
    private static URL getURL(String name) throws EfreetException {

        URL url = ClassLoader.getSystemResource(name + ".xml");

    	try {
            if (url == null) {
            	try {
            		Context initContext = new InitialContext();
            		Context envContext  = (Context)initContext.lookup("java:/comp/env");
            		String xmlFileDir = (String) envContext.lookup("xml/efreet");
            		url = new URL("file:" + xmlFileDir + "/" + name + ".xml");
            	} catch (NameNotFoundException nnfe) {
            		logger.warn("Name not found on context ");
            	} catch (NamingException e) {
            		logger.error("Error retrieving Context : ", e);
            	}
            }
            try {
            	if (url != null) {
            		url.openConnection();
            	}
            } catch (FileNotFoundException fnfe) {
            	url = null;
			}
            if (url == null) {
//            long dt_modified = url.openConnection().getLastModified();
            	url = Thread.currentThread().getContextClassLoader().getResource(name + ".xml");
            }

        } catch (IOException ioe) {
        	logger.error("Error reading XML file", ioe);
            throw new EfreetException(ioe.getMessage());
        }

        if (url == null) {
        	throw new EfreetException("Resource not found : " + name + ".xml");
        }

        return url;
    }
    
    /**
     * Read an XML file for the model for one DAO. <br>
     * It'll search the file as a resource. <br>
     * This method may be used to force reading of any changes 
     * to the XML files.
     * @param name Name of the DAO
     * @return a new DAOModel based on the XML
     */
    @SuppressWarnings("unchecked")
	public static DAOModel readXML(String name) throws EfreetException {
    	
    	URL url = getURL(name);
            
    	try {
            SAXBuilder saxb = new SAXBuilder();
            Document jdomtree = saxb.build(url);
            Element dao = jdomtree.getRootElement();
            DAOModel model = new DAOModel();
            model.setName(name);
            // Parsing
            model.setDataSource(dao.getAttributeValue("datasource"));
            
            // Read Queries
            Iterator<Element> queryList = dao.getChildren("query").iterator();
            while (queryList.hasNext()) {
            	Element thisQuery = queryList.next();
            	QueryModel qm = new QueryModel();
            	qm.setName(thisQuery.getAttributeValue("name"));
            	String qType = thisQuery.getAttributeValue("type");
            	if (qType != null) {
            		if (qType.equalsIgnoreCase("query")) {
            			qm.setType(Query.Q_QUERY);
            		} else if (qType.equalsIgnoreCase("update")) {
            			qm.setType(Query.Q_UPDATE);
            		} else if (qType.equalsIgnoreCase("procedure")) {
            			qm.setType(Query.Q_PROCEDURE);
            		} else if (qType.equalsIgnoreCase("conditional")) {
            			qm.setType(Query.Q_CONDITIONAL);
            		}
            	}

            	qm.setStatement(thisQuery.getTextNormalize());

            	Iterator<Element> paramList = thisQuery.getChildren("parameter").iterator();
            	parseParameters(qm, paramList);

            	Iterator<Element> resultList = thisQuery.getChildren("result").iterator();
            	parseResults(qm, resultList);

            	model.addQuery(qm);
            }

            return model;
            
        } catch (JDOMException jde) {
        	logger.error("Error reading XML", jde);
            throw new EfreetException(jde.getMessage());
        } catch (IOException ioe) {
        	logger.error("Error reading XML file", ioe);
            throw new EfreetException(ioe.getMessage());
        }
    }

    /**
     * Gets global context parameters for the DAOs
     * @param dm
     */
    @SuppressWarnings("unused")
	private static void setContextParameters(DAOModel dm) {
    	
    	try {
    		Context initContext = new InitialContext();
    		Context envContext  = (Context)initContext.lookup("java:/comp/env");
    		// TODO - any Context Parameter I want tO
    		// Memory Verification
//    		String memoryFlag = (String) envContext.lookup("efreet/memory");
//    		if (memoryFlag != null && (memoryFlag.equalsIgnoreCase("true") || memoryFlag.equalsIgnoreCase("t")) ) {
//    			dm.setCheckMemory(true);
//    		}
    	} catch (NameNotFoundException nnfe) {
    		logger.warn("Name not found on context ");
    	} catch (NamingException e) {
    		logger.error("Error retrieving Context : ", e);
    	}

    	
    }
    
    /**
     * Parse the parameters based on the XML tag <parameter> or the EL on the statement
     * @param qm
     * @param paramList
     */
	private static void parseParameters(QueryModel qm, Iterator<Element> paramList) {

		HashMap<Integer, ParameterModel> tmpParam = new HashMap<Integer, ParameterModel>();
		
		// First , look at the XML parameter, that's the old style
		while (paramList.hasNext()) {
			Element thisParam = paramList.next();
			ParameterModel pModel = new ParameterModel();
			pModel.setParamName(thisParam.getTextNormalize());
			pModel.setParamSize(thisParam.getAttributeValue("size"));
			String pType = thisParam.getAttributeValue("type");
			pModel.setParamType(parseType(pType));
			String posParam = thisParam.getAttributeValue("index");
			if (posParam != null) {
				try {
					int idx = Integer.parseInt(posParam);
					pModel.setParamIndex(idx);
					tmpParam.put(idx, pModel);
				} catch (Exception e) {
					logger.warn("Error on XML file parameter ", e);
				}
			}
		}
		
		// Second, check inside the statement for the declarative style (including ? old style)
		String stmt = qm.getStatement();
		int idxStr = -1;
		int idxParam = 0;
		
		// Find the First #{ or ? - add +1 to the idxStr so we dont end in a loop
		while ((idxStr = findFirst(stmt, idxStr+1, ParameterModel.PARAM_DELIMITER_BEGIN, "?") ) > 0) {
			ParameterModel pModel = null;			
			idxParam++;
			if (stmt.charAt(idxStr) == '?') {
				if (tmpParam.containsKey(idxParam)) {
					pModel = tmpParam.get(idxParam);
				}				
			} else {
				pModel = new ParameterModel();
				pModel.setParamIndex(idxParam);
				String paramDecl = stmt.substring(idxStr + ParameterModel.PARAM_DELIMITER_BEGIN.length(), 
						stmt.indexOf(ParameterModel.PARAM_DELIMITER_END, idxStr));
				pModel.setParamName(paramDecl);
				if (paramDecl.indexOf(ParameterModel.PARAM_TYPE_DELIMITER_BEGIN) > 0) {
					pModel.setParamName(paramDecl.substring(0, paramDecl.indexOf(ParameterModel.PARAM_TYPE_DELIMITER_BEGIN)));
					pModel.setParamType(parseType(paramDecl.substring(paramDecl.indexOf(ParameterModel.PARAM_TYPE_DELIMITER_BEGIN) + ParameterModel.PARAM_TYPE_DELIMITER_BEGIN.length(), 
							paramDecl.indexOf(ParameterModel.PARAM_TYPE_DELIMITER_END)) ));
				}
				if (paramDecl.indexOf(ParameterModel.PARAM_SIZE_DELIMITER_BEGIN) > 0) {
					if (paramDecl.indexOf(ParameterModel.PARAM_TYPE_DELIMITER_BEGIN) < 0) {
						pModel.setParamName(paramDecl.substring(0, paramDecl.indexOf(ParameterModel.PARAM_SIZE_DELIMITER_BEGIN)));
					}
					pModel.setParamSize(paramDecl.substring(paramDecl.indexOf(ParameterModel.PARAM_SIZE_DELIMITER_BEGIN) + ParameterModel.PARAM_SIZE_DELIMITER_BEGIN.length(), 
							paramDecl.indexOf(ParameterModel.PARAM_SIZE_DELIMITER_END)) );
				}
			}
			if (pModel != null) {
				qm.addParameter(pModel);
			}
		}
		
		// TODO - verify if we dont have an excessive number of declared parameters not used on the query statement
		
	}
    
	/**
	 * Utility function to find out the first occurrence of one of the criterium strings
	 * @param phrase text to be searched upon
	 * @param fromIndex beginning point of the search
	 * @param criteria multi-value parameter, list of strings
	 * @return position of the first occurrence within the phrase
	 */
	private static int findFirst(String phrase, int fromIndex, String ... criteria) {
		
		int minFound = 0;
		for (String thisCriterium : criteria) {
			int tmpIdx = phrase.indexOf(thisCriterium, fromIndex);
			minFound = (tmpIdx > 0 && ((minFound == 0) || (tmpIdx < minFound)))?tmpIdx:minFound;
		}
		return minFound;
	}
	
	/**
     * Parse the parameters based on the XML tag <result>
     * @param qm
	 * @param resultList
	 */
	private static void parseResults(QueryModel qm, Iterator<Element> resultList) {
		while (resultList.hasNext()) {
			Element thisResult = resultList.next();
			ResultModel rModel = new ResultModel();
			rModel.setResultName(thisResult.getTextNormalize());
			try {
				String psiz = thisResult.getAttributeValue("size");
				rModel.setResultSize(Integer.parseInt(psiz));
			} catch (Exception e) {
				rModel.setResultSize(0);
			}
			String pType = thisResult.getAttributeValue("type");
			rModel.setResultType(parseType(pType));
			String posResult = thisResult.getAttributeValue("index");
			if (posResult != null) {
				try {
					int posR = Integer.parseInt(posResult);
					rModel.setResultIndex(posR);
					qm.addResult(rModel);
				} catch (NumberFormatException e) {
					logger.warn("Error on XML file result ", e);
				}
			}
		}
	}

	/**
	 * Identify the type based on a string
	 * @param pType string defined in the XML
	 * @return the constant definition of the type
	 */
	private static int parseType(String pType) {		
		int iType = Types.JAVA_OBJECT;
		if (pType != null) {
			if (pType.equalsIgnoreCase("number") || pType.equalsIgnoreCase("numeric")) {
				iType = Types.NUMERIC;
			} else if (pType.equalsIgnoreCase("char")) {
				iType = Types.CHAR;
			} else if (pType.equalsIgnoreCase("date")) {
				iType = Types.DATE;
			} else if (pType.equalsIgnoreCase("time")) {
				iType = Types.TIME;
			} else if (pType.equalsIgnoreCase("timestamp")) {
				iType = Types.TIMESTAMP;
			} else if (pType.equalsIgnoreCase("blob")) {
				iType = Types.BLOB;            				
			} else if (pType.equalsIgnoreCase("clob")) {
				iType = Types.CLOB;            				
			} else {
				iType = Types.JAVA_OBJECT;
			}
		}
		return iType;
	}

}
