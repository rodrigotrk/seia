package br.gov.ba.ws;

import java.util.ArrayList;
import java.util.HashMap;
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

import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.entity.JustificativaRejeicao;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.service.JustificativaRejeicaoService;
import br.gov.ba.seia.service.MotivoNotificacaoService;
import br.gov.ba.seia.service.NotificacaoServiceFacade;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.ws.entity.RequerimentoWS;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@Path("sse/notificacao")
public class ServicoNotificacao {

	@EJB
	private NotificacaoServiceFacade notificacaoService;
	
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private MotivoNotificacaoService motivoNotificacaoService;
	
	@EJB
	private JustificativaRejeicaoService justificativaRejeicaoService;
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
/**
 * Método para buscar as notificações de acordo com os parâmetros passados.
 * 
 * @param dtcInicial
 * @param dtcFinal
 * @param #first
 * @param #max
 * @param ideUsuarioLogado Obrigatorio se o usuário for externo.
 * @param numProcesso
 * @param numNotificacao
 * @param #usuarioExterno
 * @return
 * @author jorge.ramos
 */
	  @POST
	  @Path("busca")
	  @Produces("text/html; charset=UTF-8")
	  public String getNotificacao(@FormParam("dtcInicial") String dtcInicial, @FormParam("dtcFinal") String dtcFinal, @FormParam("first") Integer first, @FormParam("max") Integer max, @FormParam("ideUsuarioLogado") Integer ideUsuarioLogado,  @FormParam("numProcesso") String numProcesso, @FormParam("numNotificacao") String numNotificacao, @FormParam("usuarioExterno") Boolean usuarioExterno) {
		  Map<String, Object> parametrosJson = new HashMap<String, Object>();  
		  try {
				PessoaFisica usuarioLogado = new PessoaFisica(ideUsuarioLogado);
				Map<String, Object> parametrosConsulta = new HashMap<String, Object>();
				if(!Util.isNullOuVazio(numNotificacao)){
					parametrosConsulta.put("numNotificacao", numNotificacao);
				}
				if(!Util.isNullOuVazio(numProcesso)){
					parametrosConsulta.put("numProcesso", numProcesso);
				}
				if(!Util.isNullOuVazio(dtcInicial)){
					parametrosConsulta.put("periodoInicio", Util.formataDataComHora(dtcInicial+" 00:00:00"));
				}
				if(!Util.isNullOuVazio(dtcFinal)){
					parametrosConsulta.put("periodoFim", Util.formataDataComHora(dtcFinal+" 23:59:59"));
				}
				if(!Util.isNullOuVazio(usuarioLogado)){
					parametrosConsulta.put("usuarioLogado", usuarioLogado);
				}if(!Util.isNullOuVazio(usuarioExterno)){
					parametrosConsulta.put("usuarioExterno", usuarioExterno);
				}
				
			
				
				ArrayList<Notificacao> notificacoes = (ArrayList<Notificacao>) notificacaoDAOImpl.filtrarlistaNotificacaoWS(first, max, parametrosConsulta);
				JSONSerializer serializer = new JSONSerializer(); 
				String notificacao = serializer.exclude("*.class").transform(new DateTransformer("dd/MM/yyyy"), "dtcEnvio","ideProcesso.dtcFormacao").serialize(notificacoes);
				if (!Util.isNullOuVazio(notificacoes)) {
						parametrosJson.put("NOTIFICACAO",  new JSONArray(notificacao));
						Integer quantidade = notificacaoDAOImpl.countNotificacaoWS(parametrosConsulta);
						parametrosJson.put("QTD",  quantidade);
				} else {
						parametrosJson.put("ERRO", "001");
						parametrosJson.put("MSGERRO", BUNDLE.getString("nao_encontrado"));
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				parametrosJson.put("ERRO", "999");
				parametrosJson.put("MSGERRO", e.toString());
			}
		JSONObject objJson = new JSONObject(parametrosJson);
		return objJson.toString();
	  }
	  
	  /**
	   * Método para buscar os detalhes da notificação
	   * 
	   * @param ideNotificacao
	   * @param ideProcesso
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("detalhe")
	  @Produces("text/html; charset=UTF-8")
	  public String getDetalheNotificacao(@FormParam("ideNotificacao") Integer ideNotificacao, @FormParam("ideProcesso") Integer ideProcesso) {
		Map<String, Object> parametrosJson = new HashMap<String, Object>();  
		try {
			RequerimentoWS requerimento = requerimentoService.buscaDadosRequerimentoByIdeProcesso(ideProcesso);
			ArrayList<MotivoNotificacao> motivos = (ArrayList<MotivoNotificacao>) motivoNotificacaoService.listarByIdeNotificacao(ideNotificacao);
			ArrayList<JustificativaRejeicao> justificativas = (ArrayList<JustificativaRejeicao>) justificativaRejeicaoService.listarByIdeNotificacao(ideNotificacao);
			JSONSerializer serializer = new JSONSerializer(); 
			String requerimentoJson = serializer.exclude("*.class").transform(new DateTransformer("dd/MM/yyyy"), "dtcRequerimento").serialize(requerimento);
			if (!Util.isNullOuVazio(requerimento)) {
					parametrosJson.put("REQUERIMENTO",  new JSONObject(requerimentoJson));
			} else {
					parametrosJson.put("ERRO", "001");
					parametrosJson.put("MSGERRO", BUNDLE.getString("nao_encontrado"));
			}
			String motivoJson = serializer.exclude("*.class").serialize(motivos);
			if (!Util.isNullOuVazio(motivos)) {
				parametrosJson.put("MOTIVOS",  new JSONArray(motivoJson));
			} else {
					parametrosJson.put("ERRO", "002");
					parametrosJson.put("MSGERRO", BUNDLE.getString("nao_encontrado"));
			}
			String justificativaJson = serializer.exclude("*.class").serialize(justificativas);
			if (!Util.isNullOuVazio(justificativaJson)) {
				parametrosJson.put("JUSTIFICATIVAS",  new JSONArray(justificativaJson));
			} else {
					parametrosJson.put("ERRO", "003");
					parametrosJson.put("MSGERRO", BUNDLE.getString("nao_encontrado"));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametrosJson.put("ERRO", "999");
			parametrosJson.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametrosJson);
		return objJson.toString();
	  }
	  
	  
	  /**
	   * Método para buscar os detalhes da notificação pelo número da notificação.
	   * 
	   * @param numNotificacao
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("detalhePorNumero")
	  @Produces("text/html; charset=UTF-8")
	  public String getDetalheNotificacaoPorNumero(@FormParam("numNotificacao") String numNotificacao) {
		Map<String, Object> parametrosJson = new HashMap<String, Object>();  
		JSONSerializer serializer = new JSONSerializer(); 
		try {
			Notificacao notificacao = notificacaoDAOImpl.buscarNotificaoPorNumero(numNotificacao);
			String notificacaoJson = serializer.exclude("*.class","dtcEnvio").transform(new DateTransformer("dd/MM/yyyy"), "ideProcesso.dtcFormacao").serialize(notificacao);
			if (!Util.isNullOuVazio(notificacao)) {
				parametrosJson.put("NOTIFICACAO",  new JSONObject(notificacaoJson));
			} 
			RequerimentoWS requerimento = requerimentoService.buscaDadosRequerimentoByIdeProcesso(notificacao.getIdeProcesso().getIdeProcesso());
			String requerimentoJson = serializer.exclude("*.class","cpfCnpj","ideEmpreendimento","ideRequerimento","ideRequerente","nomStatusRequerimento").transform(new DateTransformer("dd/MM/yyyy"), "dtcRequerimento").include("ideProcesso.").serialize(requerimento);
			if (!Util.isNullOuVazio(requerimento)) {
					parametrosJson.put("REQUERIMENTO",  new JSONObject(requerimentoJson));
			} else {
					parametrosJson.put("ERRO", "001");
					parametrosJson.put("MSGERRO", BUNDLE.getString("nao_encontrado"));
			}
			ArrayList<MotivoNotificacao> motivos = (ArrayList<MotivoNotificacao>) motivoNotificacaoService.listarByIdeNotificacao(notificacao.getIdeNotificacao());
			
			String motivoJson = serializer.exclude("*.class").serialize(motivos);
			if (!Util.isNullOuVazio(motivos)) {
				parametrosJson.put("MOTIVOS",  new JSONArray(motivoJson));
			} else {
					parametrosJson.put("ERRO", "002");
					parametrosJson.put("MSGERRO", BUNDLE.getString("nao_encontrado"));
			}
			ArrayList<JustificativaRejeicao> justificativas = (ArrayList<JustificativaRejeicao>) justificativaRejeicaoService.listarByIdeNotificacao(notificacao.getIdeNotificacao());
			String justificativaJson = serializer.exclude("*.class").serialize(justificativas);
			if (!Util.isNullOuVazio(justificativaJson)) {
				parametrosJson.put("JUSTIFICATIVAS",  new JSONArray(justificativaJson));
			} else {
					parametrosJson.put("ERRO", "003");
					parametrosJson.put("MSGERRO", BUNDLE.getString("nao_encontrado"));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametrosJson.put("ERRO", "999");
			parametrosJson.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametrosJson);
		return objJson.toString();
	  }
	  
	  
	  /**
	   * Método para buscar a descrição da notificação.
	   * 
	   * @param ideNotificacao
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("descricao")
	  @Produces("text/html; charset=UTF-8")
	  public String getDescricaoNotificacao(@FormParam("ideNotificacao") Integer ideNotificacao) {
		Map<String, Object> parametrosJson = new HashMap<String, Object>();  
		JSONSerializer serializer = new JSONSerializer(); 
		try {
			Notificacao notificacao = notificacaoDAOImpl.carregarGet(ideNotificacao);
			String notificacaoJson = serializer.exclude("*.class","dtcEnvio","validadeEmDias","ideNotificacao","numNotificacao","ideProcesso","tipo").include("dscNotificacao").transform(new DateTransformer("dd/MM/yyyy"), "ideProcesso.dtcFormacao").serialize(notificacao);
			if (!Util.isNullOuVazio(notificacao)) {
				parametrosJson.put("NOTIFICACAO",  new JSONObject(notificacaoJson));
			} else{
				parametrosJson.put("ERRO", "001");
				parametrosJson.put("MSGERRO", "Não existe Notificação Final.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametrosJson.put("ERRO", "999");
			parametrosJson.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametrosJson);
		return objJson.toString();
	  }
}