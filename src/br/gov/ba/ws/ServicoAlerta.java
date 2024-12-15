package br.gov.ba.ws;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.ba.seia.entity.Alerta;
import br.gov.ba.seia.enumerator.TipoAlertaEnum;
import br.gov.ba.seia.service.AlertaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@Path("sse/alerta")
public class ServicoAlerta {

	@EJB
	private AlertaService alertaService;
	/**
	 * Método para buscar a quantidades de alertas que não foram lidos pelo usuário logado.
	 * @param #ideUsuario
	 * @return
	 * @author jorge.ramos
	 */
	  @POST
	  @Path("quantidade")
	  @Produces("text/html; charset=UTF-8")
	  public String getQuantidade(@FormParam("ideUsuario") Integer ideUsuario) {
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>();
		  try {
				parametros.put("REQUERIMENTO", alertaService.count(ideUsuario, TipoAlertaEnum.REQUERIMENTO.getId(),true));
				parametros.put("NOTIFICACAO", alertaService.count(ideUsuario, TipoAlertaEnum.NOTIFICACAO.getId(),true));
				parametros.put("PROCESSO", alertaService.count(ideUsuario, TipoAlertaEnum.PROCESSO.getId(),true));
				parametros.put("PAUTA", alertaService.count(ideUsuario, TipoAlertaEnum.PAUTA.getId(),true));
				parametros.put("REQUERIMENTO_TOTAL", alertaService.count(ideUsuario, TipoAlertaEnum.REQUERIMENTO.getId(),false));
				parametros.put("NOTIFICACAO_TOTAL", alertaService.count(ideUsuario, TipoAlertaEnum.NOTIFICACAO.getId(),false));
				parametros.put("PROCESSO_TOTAL", alertaService.count(ideUsuario, TipoAlertaEnum.PROCESSO.getId(),false));
				parametros.put("PAUTA_TOTAL", alertaService.count(ideUsuario, TipoAlertaEnum.PAUTA.getId(),false));

		  } catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		  JSONObject objJson = new JSONObject(parametros);
		  return objJson.toString();
	  }

	  /**
	   * método para buscar todos os alertas de um determinado tipo.
	   * @param #ideUsuario
	   * @param #ideTipoAlerta
	   *  @param #first
	   *  @param #max
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("buscaPorUsuarioTipo")
	  @Produces("text/html; charset=UTF-8")
	  public String getAlertas(@FormParam("ideUsuario") Integer ideUsuario, @FormParam("ideTipoAlerta") Integer ideTipoAlerta,  @FormParam("first") Integer first,  @FormParam("max") Integer max) {
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>();
		  try {
			  List<Alerta> alertas = alertaService.buscarAlertasUsuarioPorTipo(ideUsuario, ideTipoAlerta, first, max);
				if(!Util.isNullOuVazio(alertas) ){
					JSONSerializer serializer = new JSONSerializer();
					String lista = serializer.exclude("*.class").transform(new DateTransformer("dd/MM/yyyy"), "dtcEnvio").serialize(alertas);
					parametros.put("ALERTAS", new JSONArray(lista));
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
	   * Método para buscar os detalhes do alerta.
	   * @param #ideAlerta
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("buscaPorId")
	  @Produces("text/html; charset=UTF-8")
	  public String getAlerta(@FormParam("ideAlerta") Integer ideAlerta) {
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>();
		  try {
			  Alerta alerta = alertaService.carregarPorIde(ideAlerta);
				if(!Util.isNullOuVazio(alerta) ){
					JSONSerializer serializer = new JSONSerializer();
					String lista = serializer.exclude("*.class").transform(new DateTransformer("dd/MM/yyyy"), "dtcEnvio").include("numDocumento","desMensagem").serialize(alerta);
					parametros.put("ALERTAS", new JSONObject(lista));
				}else{
					parametros.put("ERRO", "001");
					parametros.put("MSGERRO", "Não é possível visualizar este alerta porque já foi excluído.");
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
	   * Método para marca o alerta como lido.
	   * @param ideAlerta
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("lido")
	  @Produces("text/html; charset=UTF-8")
	  public String marcarAlertaLido(@FormParam("ideAlerta") Integer ideAlerta) {
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>();
		  try {
			alertaService.marcarAlertaLido(ideAlerta);
			 parametros.put("SUCESSO", "001");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		  JSONObject objJson = new JSONObject(parametros);
		  return objJson.toString();
	  }


	/**
	 *
	 * @param ids
	 * @return
	 * @author jorge.ramos
	 */
	  @POST
	  @Path("excluir")
	  @Produces("text/html; charset=UTF-8")
	  public String excluirAlertas(@FormParam("ids") String ids) {
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>();
		  try {
			 alertaService.excluirAlertas(ids);
			 parametros.put("SUCESSO", "001");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		  JSONObject objJson = new JSONObject(parametros);
		  return objJson.toString();
	  }
}
