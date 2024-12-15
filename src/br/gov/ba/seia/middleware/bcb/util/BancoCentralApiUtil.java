package br.gov.ba.seia.middleware.bcb.util;

import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.middleware.bcb.enumerator.BancoCentralApiEnum;
import br.gov.ba.seia.middleware.restful.RequestParameter;

public class BancoCentralApiUtil {
	
	///dados/serie/bcdata.sgs.4390/dados?formato=json&dataInicial=01/01/2010&dataFinal=31/12/2016
	private static final String URL = ConfigEnum.BANCO_CENTRAL_SERVER.toString();
	
	public static String getUrl(BancoCentralApiEnum bancoCentralApiEnum, RequestParameter ... params) throws MalformedURLException, URISyntaxException {
		
		String strURL = URL + bancoCentralApiEnum.toString() + bancoCentralApiEnum.DADOS.toString();
		
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
