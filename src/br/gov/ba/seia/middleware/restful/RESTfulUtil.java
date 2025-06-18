package br.gov.ba.seia.middleware.restful;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

import org.apache.tika.io.IOUtils;
/**
 * Classe de utilitario do restful
 * @author 
 *
 */
public class RESTfulUtil {
	
	private static final Integer[] SUCCESS_CODE = new Integer[] {200, 201, 302};
	
	public static RESTfulResponse httpGET(String url, RequestContentType requestContentType, RequestProperty ... params) throws MalformedURLException, IOException {
		HttpURLConnection http = getConnection(url, RequestMethod.GET, false, params);
		http.addRequestProperty("Accept", requestContentType.toString());
		return makeRequest(http);
	}
	
	public static RESTfulResponse httpPOST(String url, String input, RequestContentType requestContentType, RequestProperty ... params) throws MalformedURLException, IOException {
		HttpURLConnection http = getConnection(url, RequestMethod.POST, true, params);
		http.addRequestProperty("Content-Type", requestContentType.toString());
 		
		OutputStream os = http.getOutputStream();
		os.write(input.getBytes());
		os.flush();
		
		return makeRequest(http);
	}
	
	public static RESTfulResponse HttpPUT(String url, String input, RequestContentType requestContentType, RequestProperty ... params) throws MalformedURLException, IOException {
		
		HttpURLConnection http = getConnection(url, RequestMethod.PUT, false, params);
		http.addRequestProperty("Content-Type", requestContentType.toString());
		
		OutputStream os = http.getOutputStream();
		os.write(input.getBytes());
		os.flush();
		
		return makeRequest(http);
	}
	
	public static RESTfulResponse HttpDELETE(String url, RequestContentType requestContentType, RequestProperty ... params) throws MalformedURLException, IOException {
		HttpURLConnection http = getConnection(url, RequestMethod.DELETE, false, params);
		http.addRequestProperty("Accept", requestContentType.toString());
		return makeRequest(http);
	}

	private static HttpURLConnection getConnection(String url, RequestMethod requestMethodEnum, boolean doOutput, RequestProperty ... params) throws IOException, MalformedURLException, ProtocolException {
		
		System.out.println(url);
		
		HttpURLConnection http = (HttpURLConnection) (new URL(url)).openConnection();
		http.setDoOutput(doOutput);
		http.setRequestMethod(requestMethodEnum.toString());
		addRequestProperty(http, params);
		return http;
		
	}
	
	private static void addRequestProperty(HttpURLConnection http, RequestProperty... params) {
		if(params.length > 0) {
			for(int i=0; i < params.length; i++) {
				http.addRequestProperty(params[i].getProperty(),params[i].getValue());
			}
		}
	}
	
	private static RESTfulResponse makeRequest(HttpURLConnection http) throws IOException {
		
		String retorno = null;
		
		if (returnSuccess(http)) {
			retorno = IOUtils.toString(http.getInputStream());  
		}
		else {
			retorno = IOUtils.toString(http.getErrorStream());  
		}
		
		http.disconnect();
		
		return new RESTfulResponse(http.getResponseCode(), retorno.toString());
	}
	
	private static boolean returnSuccess(HttpURLConnection http) throws IOException {
		Integer responseCode = http.getResponseCode();
		return containsSuccessCode(responseCode);
	}
	
	public static boolean returnedSuccess(RESTfulResponse response) throws IOException {
		Integer responseCode = response.getResponseCode();
		return containsSuccessCode(responseCode);
	}
	
	public static boolean containsSuccessCode(Integer code) throws IOException {
		return Arrays.asList(SUCCESS_CODE).contains(code);
	}
	
}
