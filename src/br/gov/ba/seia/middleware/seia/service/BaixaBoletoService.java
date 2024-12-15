package br.gov.ba.seia.middleware.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.springframework.security.crypto.codec.Base64;

import br.gov.ba.seia.middleware.restful.RESTfulResponse;
import br.gov.ba.seia.middleware.restful.RESTfulUtil;
import br.gov.ba.seia.middleware.restful.RequestContentType;
import br.gov.ba.seia.middleware.restful.RequestProperty;
import br.gov.ba.seia.middleware.seia.enumerator.SeiaApiEnum;
import br.gov.ba.seia.middleware.seia.model.BaixaArquivoRetornoDTO;
import br.gov.ba.seia.middleware.seia.model.LeituraArquivoRetornoDTO;
import br.gov.ba.seia.middleware.seia.util.SeiaApiUtil;
import br.gov.ba.seia.util.Log4jUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Classe service de baixa de boleto
 * @author 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BaixaBoletoService {

	public BaixaArquivoRetornoDTO registrarBaixa(LeituraArquivoRetornoDTO leituraArquivoRetornoDTO) {
		try {
			String url = SeiaApiUtil.getUrl(SeiaApiEnum.BAIXA_BOLETO);
			
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			
			String input = gson.toJson(leituraArquivoRetornoDTO);
			String basicAuth = "Basic " +  new String(Base64.encode("seia:123456".getBytes()));
			
			RESTfulResponse response = RESTfulUtil.httpPOST(url, input, RequestContentType.JSON, new RequestProperty("Authorization", basicAuth));
			
			if(RESTfulUtil.returnedSuccess(response)) {
				return gson.fromJson(response.getResponseContent(), BaixaArquivoRetornoDTO.class);
			}
			else {
				throw new Exception("Ocorreu um erro na baixa do boleto.");
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getSimpleName(), Level.ERROR, e);
			return null;
		}
	}
}
