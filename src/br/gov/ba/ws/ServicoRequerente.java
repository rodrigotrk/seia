package br.gov.ba.ws;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.VwRequerentePfExternoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSONSerializer;

@Path("sse/requerente")
public class ServicoRequerente {
	
	@EJB
	VwRequerentePfExternoService vwRequerentePfExternoService;
	
	@EJB
	PessoaFisicaService pessoaFisicaService;
	
	@EJB 
	PessoaJuridicaService pessoaJuridicaService;
	
	/**
	 * Método para buscar os requerentes
	 * 
	* @param nome string passada para busca
     * @param idePessoa id do usuario logado, obrigatório se o usuário for externo
     * @param #usuarioExterno indicador se o usuário é externo
	 * @return
	 * @author jorge.ramos
	 */
	  @POST
	  @Path("busca")
	  @Produces("text/html; charset=UTF-8")
	  public String buscaRequerente(@FormParam("nome") String nome, @FormParam("idPessoa") Integer idePessoa, @FormParam("usuarioExterno") Boolean usuarioExterno){
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>(); 
		  try {
			  ArrayList<Pessoa> listaPessoa = new ArrayList<Pessoa>();
			  Pessoa pessoa = new Pessoa(idePessoa);
			  PessoaFisica pessoaFisica = new PessoaFisica(nome,"");
			  pessoaFisica.setPessoa(pessoa);
			  PessoaJuridica pessoaJuridica = new PessoaJuridica();
			  pessoaJuridica.setNomRazaoSocial(nome);
			  pessoaJuridica.setPessoa(pessoa);
			  ArrayList<PessoaFisica> listaRequerentePF;
			  ArrayList<PessoaJuridica> listaRequerentePJ;
			  JSONSerializer serializer = new JSONSerializer(); 
			  	if(usuarioExterno){
			  		listaRequerentePF =  (ArrayList<PessoaFisica>) vwRequerentePfExternoService.listarVwRequerentePfExternoSemAcento(pessoaFisica);
			  		listaRequerentePJ = (ArrayList<PessoaJuridica>) pessoaJuridicaService.filtrarPJRequerenteExternoSemAcento(pessoaJuridica);
			  	}else{
			  		listaRequerentePF =	(ArrayList<PessoaFisica>) pessoaFisicaService.filtrarPessoaRequerenteSemAcento(pessoaFisica);
			  		listaRequerentePJ = (ArrayList<PessoaJuridica>) pessoaJuridicaService.filtrarPessoaJuridicaSemAcento(pessoaJuridica);
			  	}
				String lista = serializer.exclude("*.class").serialize(unirPessoas(listaRequerentePF,listaRequerentePJ));
				parametros.put("REQUERENTES", new JSONArray(lista));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		  JSONObject objJson = new JSONObject(parametros);
		  return objJson.toString();
	  }
	  
	  private ArrayList<Pessoa> unirPessoas(ArrayList<PessoaFisica> listaRequerentePF,  ArrayList<PessoaJuridica> listaRequerentePJ){
		  ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
		  if(!Util.isNullOuVazio(listaRequerentePF)){
			  for(PessoaFisica pf : listaRequerentePF){
				  Pessoa pessoa = new Pessoa(pf.getIdePessoaFisica(), pf);
				  pessoas.add(pessoa);
			  }
		  }if(!Util.isNullOuVazio(listaRequerentePJ)){
			  for(PessoaJuridica pj : listaRequerentePJ){
				  Pessoa pessoa = new Pessoa(pj.getIdePessoaJuridica(), pj);
				  pessoas.add(pessoa);
			  }
		  }
		  return pessoas;
	  }
}
