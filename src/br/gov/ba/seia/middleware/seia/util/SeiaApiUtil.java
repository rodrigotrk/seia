package br.gov.ba.seia.middleware.seia.util;

import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.middleware.restful.RequestParameter;
import br.gov.ba.seia.middleware.seia.enumerator.SeiaApiEnum;
/**
 * Classe utilitária do seia api
 * @author 
 *
 */
public class SeiaApiUtil {
	
	private static final String URL = ConfigEnum.SEIA_API_SERVER.toString();
	
	public static String getUrl(SeiaApiEnum seiaApiEnum, RequestParameter ... params) throws MalformedURLException, URISyntaxException {
		
		String strURL = URL + seiaApiEnum.toString();
		
		if(params.length > 0) {
			for(int i=0; i < params.length; i++) {
				strURL += strURL.contains("?") ? "&" : "?";
				strURL += params[i];
			}
		}
		
		URL url= new URL(strURL);
		String encodedURL = new URI(url.getProtocol(), url.getUserInfo(), IDN.toASCII(url.getHost()), url.getPort(), url.getPath(), url.getQuery(), url.getRef()).toASCIIString();
		
		return encodedURL;
	}
		
}
