package br.gov.ba.seia.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;

public class Log4jUtil {
	
	private static Log4jUtil log4j;
	
	static {
		getCurrentInstance();
	}
	
	public static Log4jUtil getCurrentInstance() {
		if(log4j == null){
			log4j = new Log4jUtil();
		}
		return log4j;
	}
	
	public Log4jUtil() {
		
		try{
			Properties properties = new Properties();
			InputStream in = getClass().getClassLoader().getResourceAsStream("log4j.properties");
			properties.load(in);
			in.close();
			PropertyConfigurator.configure(properties);
		}
		catch(IOException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.ERROR, e.getLocalizedMessage(), e);
		}
	}	

	public static void log(String nomeClass, Priority lvl, Exception e) {
		Logger.getLogger(nomeClass).log(lvl, e.getLocalizedMessage(), e);
	}

	public static void log(String nomeClass, Priority lvl, Throwable e) {
		Logger.getLogger(nomeClass).log(lvl, e.getLocalizedMessage(), e);
	}

	public static void log(String nomeClass, Priority lvl, String msg) {
		Logger.getLogger(nomeClass).log(lvl, msg);
	}

}
