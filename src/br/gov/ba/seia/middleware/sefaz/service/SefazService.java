package br.gov.ba.seia.middleware.sefaz.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.springframework.security.crypto.codec.Base64;

import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO.DocumentoArrecadacao;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.middleware.restful.RESTfulResponse;
import br.gov.ba.seia.middleware.restful.RESTfulUtil;
import br.gov.ba.seia.middleware.restful.RequestContentType;
import br.gov.ba.seia.middleware.restful.RequestProperty;
import br.gov.ba.seia.middleware.seia.enumerator.SeiaApiEnum;
import br.gov.ba.seia.middleware.seia.util.SeiaApiUtil;

import com.google.gson.Gson;
/**
 * Classe de serviço para Sefaz
 * @author 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SefazService {
	/**
	 * Metodo para enviar documento de arrecadação
	 * @param documentoArrecadacao
	 * @return
	 * @throws Exception
	 */
	public static String enviar(DocumentoArrecadacao documentoArrecadacao) throws Exception {
		
		String url = SeiaApiUtil.getUrl(SeiaApiEnum.TRANSMITIR_DAE);
		String basicAuth = "Basic " +  new String(Base64.encode("seia:123456".getBytes()));
		String input = new Gson().toJson(documentoArrecadacao);
		
		RESTfulResponse response = RESTfulUtil.httpPOST(url, input, RequestContentType.JSON, new RequestProperty("Authorization", basicAuth));
		
		if (RESTfulUtil.returnedSuccess(response)) {
			return response.getResponseContent();
		} else {
			throw new AppExceptionError("Erro ao tentar gerar o DAE. Webservice da SEFAZ indisponível. Tente novamente mais tarde.");
		}
	}
}