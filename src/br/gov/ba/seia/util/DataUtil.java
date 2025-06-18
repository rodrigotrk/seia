package br.gov.ba.seia.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtil {

	public static Calendar finaldoDia(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return finalDoDia(calendar);
	}

	public static Calendar finalDoDia(Calendar calendar){
		calendar.add(Calendar.DATE,1);
		calendar.add(Calendar.SECOND, -1);
		
		return calendar;
	}
	
	public static boolean validaDataSeMaiorQueAtual(Date date) {
		return !(date.compareTo(new Date(System.currentTimeMillis())) == 1);
	}

	public static String getDataHoje() {
		return new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));
	}
	
	public static String getData(Date data) {
		return new SimpleDateFormat("dd/MM/yyyy").format(data);
	}
	
	public static Date somarMes(Date data, Integer meses) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.MONTH, meses);
		
		return cal.getTime(); //daqui a 1 mês
	}
	
	public static Integer obterAno() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}
	
	public static Date getDataAtual() {
		Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	 
	
		return calendar.getTime();
	}
	
	public static Date getDataOntem() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, -24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static boolean isDataValida(String data) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);
			sdf.parse(data);
			return true;
		} catch (ParseException ex) {
			return false;
		}
	}
}
