package br.gov.ba.seia.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Functions {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	private Functions() {
    }
	/**
	 * 
	 * @param key
	 * @param param
	 * @return mensagem formatada
	 */
	 public static String messageFormat(String key, String param) {
	        try {
	            return MessageFormat.format(BUNDLE.getString(key), param);
	        } catch (MissingResourceException e) {
	            return '!' + key + '!';
	        }
	    }
}
