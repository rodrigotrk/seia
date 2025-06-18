package br.gov.ba.seia.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	

	
	public static boolean isDouble(String numero) {
		String pattern = "^[-+]?[0-9]*\\.?[0-9]+$";
		
		Matcher m = Pattern.compile(pattern).matcher(numero);
		if(m.find()) {
			return true;
		}
		return false;
	}
	
	
	public static boolean isDateUSA(String data) {
		
		String pattern = "[A-z]{3} [A-z]{3} \\d{2} \\d{2}:\\d{2}:\\d{2} GMT \\d{4}";
		
		Matcher m = Pattern.compile(pattern).matcher(data);
		if(m.find()) {
			return true;
		}
		return false;
	}
	
	public static boolean isDateBR(String data) {
		
		String pattern = "[A-z]{3} [A-z]{3} \\d{2} \\d{2}:\\d{2}:\\d{2} BRT \\d{4}";
		
		Matcher m = Pattern.compile(pattern).matcher(data);
		if(m.find()) {
			return true;
		}
		return false;
	}
	
	public static boolean isDateNormalizada(String data) {
		
		String pattern = "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}";
		
		Matcher m = Pattern.compile(pattern).matcher(data);
		if(m.find()) {
			return true;
		}
		return false;
	}
}
