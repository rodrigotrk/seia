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

import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSONSerializer;

@Path("sse/municipio")
public class ServicoMunicipio {
	
	@EJB
	private MunicipioService municipioService;

	/**
	   * Método para listar todos os municipios da Bahia.
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("todosBahia")
	  @Produces("text/html; charset=UTF-8")
	  public String getMunicipio() {
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>(); 
		  try {
			  Collection<Municipio> municipio = municipioService.filtrarListaMunicipiosDaBahia();
				if(!Util.isNullOuVazio(municipio) ){
					JSONSerializer serializer = new JSONSerializer(); 
					String lista = serializer.exclude("*.class").serialize(municipio);
					parametros.put("MUNICIPIOS", new JSONArray(lista));
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
	  * Listar os municipios da bahia pelo nome.
	  * @param municipio nome do municipio
	  * @return
	  * @author jorge.ramos
	  */
	  @POST
	  @Path("nomeBahia")
	  @Produces("text/html; charset=UTF-8")
	  public String getMunicipioPorNome(@FormParam("municipio") String municipio) {
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>(); 
		  try {
			  Collection<Municipio> municipios = municipioService.obterMunicipiosBahiaByNomeSemAcento(municipio);
				if(!Util.isNullOuVazio(municipios) ){
					JSONSerializer serializer = new JSONSerializer(); 
					String lista = serializer.exclude("*.class").serialize(municipios);
					parametros.put("MUNICIPIOS", new JSONArray(lista));
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
