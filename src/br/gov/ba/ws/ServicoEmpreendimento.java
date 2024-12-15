package br.gov.ba.ws;

import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSONSerializer;


@Path("sse/empreendimento")
public class ServicoEmpreendimento {

	@EJB
	private EmpreendimentoService empreendimentoService;
	
	/**
	 * Método para fazer a busca do empreendimentos do requerente.
	 * @param ideRequerente
	 * @return
	 * @author jorge.ramos
	 */
	  @POST
	  @Path("empreendimentosRequerente")
	  @Produces("text/html; charset=UTF-8")
	  public String getEmpreendimentoRequerente(@FormParam("ideRequerente") Integer ideRequerente) {
		  
		Hashtable<String, Object> parametros = new Hashtable<String, Object>();  
		  Pessoa pessoa = new Pessoa(ideRequerente);
		try {
			Collection<Empreendimento> empreendimentos =  empreendimentoService.listarEmpreendimento(pessoa);
			JSONSerializer serializer = new JSONSerializer(); 
			String empreendimentoJson = serializer.exclude("*.class").serialize(empreendimentos);
			if (!Util.isNullOuVazio(empreendimentos)) {
					parametros.put("EMPREENDIMENTO", new JSONArray(empreendimentoJson));
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
