package br.gov.ba.seia.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FormaterUtil {
	
	private final static String formatoPtBr = "dd/MM/yyyy";
	public final static String diaMesAno = "yyyy/MM/dd";
	public final static String formatoBanco = "yyyy-MM-dd HH:mm:ss";//2017-05-17 10:46:32
	
	public static String getDataFormatoPtBr(Date data){
		return new SimpleDateFormat(formatoPtBr).format(data);
	}
	
	
	public static String formatarData(Date data, String formato){
		return new SimpleDateFormat(formato).format(data);	
	}
	
}