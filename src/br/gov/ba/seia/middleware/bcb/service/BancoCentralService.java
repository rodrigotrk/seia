package br.gov.ba.seia.middleware.bcb.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.commons.lang.StringUtils;

import br.gov.ba.seia.middleware.bcb.enumerator.BancoCentralApiEnum;
import br.gov.ba.seia.middleware.bcb.model.Selic;
import br.gov.ba.seia.middleware.bcb.util.BancoCentralApiUtil;
import br.gov.ba.seia.middleware.restful.RESTfulResponse;
import br.gov.ba.seia.middleware.restful.RESTfulUtil;
import br.gov.ba.seia.middleware.restful.RequestContentType;
import br.gov.ba.seia.middleware.restful.RequestParameter;

import com.google.gson.Gson;
/**
 * Classe de serviço do BANCO CENTRAL
 * @author 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BancoCentralService {
	/**
	 * Metodo para obter selic por periodo -mês
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public Selic[] obterSelicMesPorPeriodo(Date dataInicial, Date dataFinal) throws URISyntaxException, IOException  {
		Calendar calInicial = Calendar.getInstance();
		calInicial.setTime(dataInicial);
		
		Calendar calFinal = Calendar.getInstance();
		calFinal.setTime(dataFinal);

		String dtInicial = "01/" + StringUtils.leftPad(String.valueOf((calInicial.get(Calendar.MONTH) + 1)), 2, "0") + "/" +  calInicial.get(Calendar.YEAR);
		String dtFinal = "01/" + StringUtils.leftPad(String.valueOf((calFinal.get(Calendar.MONTH) + 1)), 2, "0") + "/" +  calFinal.get(Calendar.YEAR);
		
		String url = BancoCentralApiUtil.getUrl(BancoCentralApiEnum.SELIC_MES, 
				new RequestParameter("formato", "json"), 
				new RequestParameter("dataInicial", dtInicial), 
				new RequestParameter("dataFinal", dtFinal)
		);
		
		RESTfulResponse response = RESTfulUtil.httpGET(url, RequestContentType.JSON);
		
		if(RESTfulUtil.returnedSuccess(response)) {
			Selic[] retorno = new Gson().fromJson(response.getResponseContent(), Selic[].class);
			return retorno;
		}
		else {
			throw new RuntimeException("Erro no servidor!");
		}
	}
	/**
	 * Metodo para obter valor total da selic por periodo - mês
	 * @param dataInicial
	 * @param dataFinal
	 * @return valor da selic
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public Double obterTotalValorSelicMesPorPeriodo(Date dataInicial, Date dataFinal) throws URISyntaxException, IOException {
		Selic[] listaSelic = obterSelicMesPorPeriodo(dataInicial, dataFinal);
		Double valorSelic = 0.0;
		
		for (Selic selic : listaSelic) {
			valorSelic += selic.getValor();
		}
		
		return valorSelic;
	}
}
