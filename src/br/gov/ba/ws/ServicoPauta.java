package br.gov.ba.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.ba.seia.entity.ControleCronograma;
import br.gov.ba.seia.service.ControleCronogramaService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.ws.entity.ProcessoWS;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@Path("sse/pauta")
public class ServicoPauta {

	@EJB
	private ProcessoService processoService;
	@EJB
	private ControleCronogramaService controleCronogramaService;
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	/**
	 * Método para buscar os processos da pauta.
	 * 
	 * @param ideUsuarioLogado o ide da pessoa fisica logada no sistema
	 * @param max número máximo de registros a serem buscados
	 * @param first o indice do primeiro registro a ser buscado
	 * @param numProcesso número do processo preenchido na tela de busca.
	 * @param dtcInicial data inicial preenchida na tela de consulta, se a data inicial e final forem preenchidas, carrega somente os processos formados entre essas datas.
	 * @param dtcFinal data final preenchida na tela de consulta, se a data inicial e final forem preenchidas, carrega somente os processos formados entre essas datas.
	 * @param tipoPauta o tipo da pauta 1-Área, 2 - Gestor, 3 - Técnico
	 * @return
	 */
	  @POST
	  @Path("buscar")
	  @Produces("text/html; charset=UTF-8")
	  public String getPauta(@FormParam("ideUsuarioLogado") Integer ideUsuarioLogado, @FormParam("max") Integer max, @FormParam("first") Integer first, @FormParam("tipoPauta") Integer tipoPauta, @FormParam("dtcInicial") String dtcInicial, @FormParam("dtcFinal") String dtcFinal, @FormParam("numProcesso") String numProcesso) {
		Map<String, Object> parametros = new HashMap<String, Object>();  
		try {

			List<ProcessoWS> processoWS = processoService.listarProcessoPautaWs(ideUsuarioLogado, max, first, tipoPauta,Util.formataDataComHora(dtcInicial + " 00:00:00"),Util.formataDataComHora(dtcFinal + " 23:59:59"), numProcesso);
			JSONSerializer serializer = new JSONSerializer(); 
			String processo = serializer.exclude("*.class","dscStatusFluxoExterno","desEmail","nomPorte","nomMunicipio","sistema","requerimento.numRequerimento","requerimento.cpfCnpj","requerimento.ideEmpreendimento","requerimento.requerente","requerimento.ideRequerente","requerimento.dtcRequerimento","requerimento.nomStatusRequerimento","requerimento.nomEmpreendimento").transform(new DateTransformer("dd/MM/yyyy"), "dtcFormacao").serialize(processoWS);
			if (!Util.isNullOuVazio(processoWS)) {
				   	Long quantidade = processoService.countListarProcessoPautaWs(ideUsuarioLogado, tipoPauta,Util.formataDataComHora(dtcInicial + " 00:00:00"),Util.formataDataComHora(dtcFinal + " 23:59:59"), numProcesso);
					parametros.put("PROCESSO",  new JSONArray(processo));
					parametros.put("QTD",  quantidade);
			} else {
					parametros.put("ERRO", "001");
					parametros.put("MSGERRO", BUNDLE.getString("nao_encontrado"));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametros);
		return objJson.toString();
	  }
	  
	  /**
	   * Método para buscar o detalhe da equipe do processo
	   * @param ideProcesso
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("detalheEquipe")
	  @Produces("text/html; charset=UTF-8")
	  public String getEquipeProcesso(@FormParam("ideProcesso") Integer ideProcesso) {

		return null;
	  }
	  
	  /**
	   * Método para buscar os detalhes do cronograma do processo
	   * @param ideProcesso
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("detalheCronograma")
	  @Produces("text/html; charset=UTF-8")
	  public String getCronogramaProcesso(@FormParam("ideProcesso") Integer ideProcesso) {
		Map<String, Object> parametros = new HashMap<String, Object>();  
		try {
			List<ControleCronograma> cronogramas = controleCronogramaService.listarControleCronogramaByIdeProcesso(ideProcesso);
			JSONSerializer serializer = new JSONSerializer(); 
			String cronogramaJson = serializer.exclude("*.class").transform(new DateTransformer("dd/MM/yyyy"), "dtcItemPrevista","dtcItemRealizada").serialize(cronogramas);
			if (!Util.isNullOuVazio(cronogramas)) {
					parametros.put("CRONOGRAMA",  new JSONArray(cronogramaJson));
			} else {
					parametros.put("ERRO", "001");
					parametros.put("MSGERRO", "Não existe Cronograma.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametros);
		return objJson.toString();
	  }
	
}
