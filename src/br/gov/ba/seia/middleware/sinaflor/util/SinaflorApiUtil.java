package br.gov.ba.seia.middleware.sinaflor.util;

import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.google.gson.Gson;

import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.middleware.restful.RESTfulResponse;
import br.gov.ba.seia.middleware.restful.RESTfulUtil;
import br.gov.ba.seia.middleware.restful.RequestContentType;
import br.gov.ba.seia.middleware.restful.RequestParameter;
import br.gov.ba.seia.middleware.restful.RequestProperty;
import br.gov.ba.seia.middleware.sinaflor.enumerator.SinaflorApiEnum;
import br.gov.ba.seia.middleware.sinaflor.model.AutenticacaoEntrada;
import br.gov.ba.seia.middleware.sinaflor.model.AutenticacaoSaida;

public class SinaflorApiUtil {
	
	private static final String URL = ConfigEnum.SINAFLOR_SERVER.toString();
	private static AutenticacaoSaida autenticacao;
	
	/**
	 * 
	 * @throws Exception
	 */
	public static void autenticar() throws Exception {
		
		if(autenticacao == null) {
			
			Gson gson = new Gson(); 
			
			String input = gson.toJson(new AutenticacaoEntrada(ConfigEnum.SINAFLOR_CHAVE_ACESSO.toString(), Integer.parseInt(ConfigEnum.SINAFLOR_CODIGO_ACESSO.toString())));
			
			String url = SinaflorApiUtil.getUrl(SinaflorApiEnum.AUTENTICACAO); 
			
			RESTfulResponse response = RESTfulUtil.httpPOST(url, input, RequestContentType.JSON); 
			if(RESTfulUtil.returnedSuccess(response)) { 
				autenticacao = new Gson().fromJson(response.getResponseContent(), AutenticacaoSaida.class); 
			} 
			else { 
				throw new Exception(response.getResponseContent());
			}
		}
	}
	/**
	 * 
	 */
	public static void finalizar() {
		autenticacao = null;
	}
	/**
	 * 
	 * @param url
	 * @param requestContentType
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public static RESTfulResponse httpGET(String url, RequestContentType requestContentType, RequestProperty ... properties) throws Exception {
		return RESTfulUtil.httpGET(url, requestContentType, params(properties));
	}
	
	public static RESTfulResponse httpPOST(String url, String input, RequestContentType requestContentType, RequestProperty ... properties) throws Exception {
		return RESTfulUtil.httpPOST(url, input, requestContentType, params(properties));
	}
	/**
	 * 
	 * @param properties
	 * @return
	 */
	private static RequestProperty[] params(RequestProperty... properties)  {
		RequestProperty params[] = new RequestProperty[properties.length+1];
		params[0] = new RequestProperty("Authorization", autenticacao.getToken());
		if(params.length>1) {
			System.arraycopy(properties, 0, params, 1, properties.length);
		}
		return params;
	}
	/**
	 * 
	 * @param sinaflorEnum
	 * @param params
	 * @return
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	public static String getUrl(SinaflorApiEnum sinaflorEnum, RequestParameter ... params) throws MalformedURLException, URISyntaxException {
		
		String strURL = URL + sinaflorEnum.toString();
		
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
	/**
	 * 
	 * @param sinaflorEnum1
	 * @param id
	 * @param sinaflorEnum2
	 * @param params
	 * @return
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	public static String getUrl(SinaflorApiEnum sinaflorEnum1, Integer id, SinaflorApiEnum sinaflorEnum2, RequestParameter ... params) throws MalformedURLException, URISyntaxException {
		
		String strURL = URL + sinaflorEnum1.toString() + "/" + id + sinaflorEnum2.toString();
		
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
