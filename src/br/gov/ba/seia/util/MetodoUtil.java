package br.gov.ba.seia.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Level;

public class MetodoUtil {
	
	private Object object;
	private Method method;
	
	public MetodoUtil(Object object, Method method) {
		this.object = object;
		this.method = method;
	}
	
	public MetodoUtil(Object object, String methodName, Class<?>... parameterTypes) {
		try {
			this.object = object;
			this.method = object.getClass().getMethod(methodName, parameterTypes);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public Object executeMethod(Object ... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return method.invoke(object, args);
	}
	
}
