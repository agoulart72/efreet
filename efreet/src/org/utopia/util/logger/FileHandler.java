/*
 * $Id: FileHandler.java,v 1.1.1.1 2005-03-21 00:59:53 agoulart Exp $
 */
package org.utopia.util.logger;

import java.util.Calendar;
import java.util.Date;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandler extends StreamHandler
{
    private String fileName = "logger";
    private int fileDay = 0;
    private int fileMonth = 0;
    private int fileYear = 0;

    public FileHandler(String fileName, Formatter fmt)
    {
	try {
	    Calendar clnd = Calendar.getInstance();
	    this.fileDay = clnd.get(Calendar.DAY_OF_MONTH);
	    this.fileMonth = clnd.get(Calendar.MONTH) + 1;
	    this.fileYear = clnd.get(Calendar.YEAR);

	    this.fileName = fileName;
	    
	    String logFile = fileName + "." + getDateExtension() + ".log";
	    FileOutputStream outputStream = new FileOutputStream(logFile,true);
	    this.output = outputStream;
	    this.formatter = fmt;	
	} catch (Exception ex) {
	    System.err.println(ex.getLocalizedMessage());	    
	}
    }

    private String getDateExtension()
    {
	String sdia = (this.fileDay < 10)?"0"+this.fileDay:""+this.fileDay;
	String smes = (this.fileMonth < 10)?"0"+this.fileMonth:""+this.fileMonth;
	String sano = (this.fileYear < 10)?"000"+this.fileYear:(this.fileYear<100)?"00"+this.fileYear:(this.fileYear<1000)?"0"+this.fileYear:""+this.fileYear;

	return sano + "-" + smes + "-" + sdia;
    }

    public void publish(Level maxlevel, LogRecord record)
    {
	try {
	    Calendar clnd = Calendar.getInstance();
	    int dia = clnd.get(Calendar.DAY_OF_MONTH);
	    int mes = clnd.get(Calendar.MONTH) + 1;
	    int ano = clnd.get(Calendar.YEAR);

	    if ((ano > this.fileYear) || 
		(mes > this.fileMonth) || 
		(dia > this.fileDay) ) {
		
		this.fileDay = dia;
		this.fileMonth = mes;
		this.fileYear = ano;

		flush();
		close();

		String logFile = this.fileName + "." + getDateExtension() + ".log";
		FileOutputStream outputStream = new FileOutputStream(logFile,true);
		this.output = outputStream;	    
	    }
	} catch (Exception ex) {
	    System.err.println(ex.getLocalizedMessage());
	} finally {
	    super.publish(maxlevel, record);
	}
    }
}
