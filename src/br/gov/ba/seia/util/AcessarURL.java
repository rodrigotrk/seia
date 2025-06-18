package br.gov.ba.seia.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AcessarURL {
	
	/**
	 * Realiza uma conexão com a URL informada e lê o HTML retornado da mesma.
	 * @author micael.coutinho
	 * @param pUrl
	 * @return Boolean
	 */
	 public static Boolean callAppGeoSeia(String pUrl) {
		 
	      if (Util.isNullOuVazio(pUrl) || pUrl.isEmpty()) {
	         System.out.println("Não foi especificado nenhuma URL.");
	         return Boolean.FALSE;
	      }
	 
	      try {
	         URL url = new URL(pUrl);
	         HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	         BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	 
	         String line = null;
	         String line2 = "";
	 
	         while( (line = in.readLine()) != null ){
	        	 line2 +=line;
	         }
	         System.out.println(line2);
	 
	         in.close();
	         urlConnection.disconnect();
	         if(line2.toLowerCase().contains("ok") || line2.length() == 6)
	        	 return Boolean.TRUE;
	         else
	        	 return Boolean.FALSE;
	 
	      } catch (MalformedURLException e){
	         System.out.println("Erro ao criar URL. Formato inválido.");
	         return false;
	      } catch (IOException e2) {
	         System.out.println("Erro ao acessar URL.");
	         return false;
	      }
	   }

	/**
	 * Realiza uma conexão com a URL informada e lê o HTML retornado da mesma.
	 * @author lucas.santiago
	 * @param pUrl
	 * @return Array
	 */
	 public static String[] callAppGeoSeia2(String pUrl) throws Exception {
		 if (Util.isNullOuVazio(pUrl) || pUrl.isEmpty()) {
			 //System.out.println("Não foi especificado nenhuma URL.");
			 return new String[]{"ERRO","9996","Erro interno. Contacte o Suporte técnico!"};
		 }
	 
		 try {
			 URL url = new URL(pUrl);
	         HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	         BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	 
	         String line = null;
	         String line2 = "";
	 
	         while( (line = in.readLine()) != null ){
	        	 line2 +=line;
	         }
	         //System.out.println(line2);
	 
	         in.close();
	         urlConnection.disconnect();
	         
	         String[] retorno = line2.split(";");
	         return retorno;
		 } catch (MalformedURLException e){
			 //System.out.println("Erro ao criar URL. Formato inválido.");
			 return new String[]{"ERRO","9997","Erro interno. Contacte o Suporte técnico!"};
		 } catch (IOException e2) {
			 //System.out.println("Erro ao acessar URL.");
			 return new String[]{"ERRO","9998","Erro interno. Contacte o Suporte técnico!"};
		 }
	 }

	 public static String[] callAppGeoSeiaValidacao(String pUrl) {
	      if (Util.isNullOuVazio(pUrl) || pUrl.isEmpty()) {
	         System.out.println("Não foi especificado nenhuma URL.");
	         return new String[]{"ERRO","9996","Erro interno. Contacte o Suporte técnico!"};
	      }
	 
	      try {
	         URL url = new URL(pUrl);
	         HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	         BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	 
	         String line = null;
	         String line2 = "";
	 
	         while( (line = in.readLine()) != null ){
	        	 line2 +=line;
	         }
	         System.out.println(line2);
	 
	         in.close();
	         urlConnection.disconnect();
	         
	         String[] retorno = line2.split(";");
	         return retorno;
	 
	      } catch (MalformedURLException e){
	         System.out.println("Erro ao criar URL. Formato inválido.");
	         return new String[]{"ERRO","9997","Erro interno. Contacte o Suporte técnico!"};
	      } catch (IOException e2) {
	         System.out.println("Erro ao acessar URL.");
	         return new String[]{"ERRO","9998","Erro interno. Contacte o Suporte técnico!"};
	      }
	 }
	 
	 public static String[] callAppShape2Geom(String pUrl) {
		 if (Util.isNullOuVazio(pUrl) || pUrl.isEmpty()) {
		     System.out.println("Não foi especificado nenhuma URL.");
		     return new String[]{"ERRO","9996","Erro interno. Contacte o Suporte técnico!"};
		 }
		 try {
		         URL url = new URL(pUrl);
		         HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		         BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		 
		         String line = null;
		         String line2 = "";
		 
		         while( (line = in.readLine()) != null ){
		        	 line2 +=line;
		         }
		         System.out.println(line2);
		 
		         in.close();
		         urlConnection.disconnect();
		         
		         String[] retorno = line2.split(";");
		     return retorno;
		
		     
		  } catch (MalformedURLException e){
		     System.out.println("Erro ao criar URL. Formato inválido.");
		     return new String[]{"ERRO","9997","Erro interno. Contacte o Suporte técnico!"};
		  } catch (IOException e2) {
		     System.out.println("Erro ao acessar URL.");
		     return new String[]{"ERRO","9998","Erro interno. Contacte o Suporte técnico!"};
		  }
	 }
}
