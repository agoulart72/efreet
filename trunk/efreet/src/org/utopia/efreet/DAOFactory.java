package org.utopia.efreet;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.util.Hashtable;
import java.util.Iterator;

import org.jdom.input.*;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * This class is the main factory for the DAO creation, it reads and parses
 * the XML files containing the models for different DAOs.
 */
public class DAOFactory
{
    private static Hashtable models = null;    

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
            throw new EfreetException("DAO name is not defined");
        }

        if (models == null) models = new Hashtable();

        DAOModel dm = (DAOModel) models.get(name);

        if (dm == null) {
            dm = readXML(name);
            models.put(name, dm);
        }

        if (dm == null) {
            throw new EfreetException("DAO model is not defined");
        }

        DataAccessObject dao = new DataAccessObject();
        dao.setModel(dm);

        return dao;
    }

    /**
     * Read an XML file for the model for one DAO. <br>
     * It'll search the file as a resource. <br>
     * This method may be used to force reading of any changes 
     * to the XML files.
     * @param name Name of the DAO
     * @return a new DAOModel based on the XML
     */
    public static DAOModel readXML(String name) throws EfreetException {
        try {
            URL url = ClassLoader.getSystemResource(name + ".xml");        
            SAXBuilder saxb = new SAXBuilder();
            Document jdomtree = saxb.build(url);
            Element dao = jdomtree.getRootElement();
            DAOModel model = new DAOModel();
            // Parsing
            // Read Columns
            model.setTableName(dao.getAttributeValue("table"));
            Iterator columnList = dao.getChildren("column").iterator();
            while (columnList.hasNext()) {
                Object proximo = columnList.next();
                if (proximo instanceof Element) {
                    Element thisCol = (Element) proximo;
                    Column novaColuna = new Column(thisCol.getAttributeValue("name"));
                    Attribute req = thisCol.getAttribute("required");
                    novaColuna.setRequired(req != null && req.getBooleanValue());
                    Attribute size = thisCol.getAttribute("size");
                    // TODO - set column size (precision, scale)
                    Attribute typeC = thisCol.getAttribute("type");
                    // TODO - set type code
                    novaColuna.setDefault(thisCol.getTextNormalize());
                    model.addColumn(novaColuna);
                }
            }

            // Read Queries
            Iterator queryList = dao.getChildren("query").iterator();
            while (queryList.hasNext()) {
                Object proximo = queryList.next();
                if (proximo instanceof Element) {
                    Element thisQuery = (Element) proximo;
                    Query q = new Query();
                    q.setName(thisQuery.getAttributeValue("name"));
                    q.setStatement(thisQuery.getTextNormalize());
                    Iterator paramList = thisQuery.getChildren("parameter").iterator();
                    while (paramList.hasNext()) {
                        Element thisParam = (Element) paramList.next();
                        // TODO - convert
                        q.addParameter(0);//thisParam.getAttributeValue("type"));
                    }

                    Iterator resultList = thisQuery.getChildren("result").iterator();
                    while (resultList.hasNext()) {
                        Element thisResult = (Element) resultList.next();
                        q.addResult(thisResult.getTextNormalize());
                    }
                    model.addQuery(q);
                }
            }

            return model;
            
        } catch (JDOMException jde) {
            throw new EfreetException(jde.getMessage());
        } catch (IOException ioe) {
            throw new EfreetException(ioe.getMessage());
        }
    }

}
