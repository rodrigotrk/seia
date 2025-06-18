package br.gov.ba.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
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

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.ComprovantePagamentoDaeService;
import br.gov.ba.seia.service.ComprovantePagamentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.PermissaoPerfilService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.ProcessoAtoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.StatusFluxoService;
import br.gov.ba.seia.service.TipoAtoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.ws.entity.ProcessoWS;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@Path("sse/processo")
public class ServicoProcesso {

	@EJB
	private ProcessoService processoService;
	@EJB
	private ProcessoAtoService processoAtoService;
	@EJB
	private PermissaoPerfilService permissaoPerfilService;
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioRequerimentoService;
	@EJB
	private ComprovantePagamentoService comprovantePagamentoService;
	@EJB
	private ComprovantePagamentoDaeService comprovantePagamentoDaeService;
	@EJB
	private StatusFluxoService statusFluxoService;
	@EJB
	private TipoAtoService tipoAtoService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private PessoaFisicaService pessoaFisicaService ;
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	
	
	  
	  /**
	   * Método para buscar os processos de acordo com os parâmetros passados
	   * 
	   * @param requerente requerente escolhido na tela de consulta, se preenchido carrega somente os processos do requerente.
	   * @param status status escolhido na tela de consulta, se preenchido carrega somente os processos que estiverem nesse status.
	   * @param ato ato escolhido na tela de consulta, se preenchido carrega somente os processos que foram formados com esse ato.
	   * @param categoria categoria do ato escolhido na tela de consulta, se preenchido carrega somente os processos que foram formados com os ato pertecentes a essa categoria .
	   * @param cpfcnpj Cpf ou cnpj do requerente.
	   * @param dtcInicial data inicial preenchida na tela de consulta, se a data inicial e final forem preenchidas, carrega somente os processos formados entre essas datas.
	   * @param dtcFinal data final preenchida na tela de consulta, se a data inicial e final forem preenchidas, carrega somente os processos formados entre essas datas.
	   * @param #first indice do primeiro valor a ser carregado na consulta
	   * @param #max quantidade de registros carregados na consulta
	   * @param #ideUsuario id da pessoa f�sica do usu�rio logado.
	   * @param ideMunicipio id do municipio, se preenchido carrega somente os processos do municipio.
	   * @param numProcesso pesquisa pelo número do processo
	   * @param #usuarioExterno booleano indicador se o usu�rio externo, se for externo carrega somente os processos do usu�rio logado e dos requerentes que ele for procurador ou representante legal
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("busca")
	  @Produces("text/html; charset=UTF-8")
	  public String getProcessos(@FormParam("requerente") Integer requerente, @FormParam("status") Integer status,@FormParam("ato") Integer ato, @FormParam("categoria") Integer categoria, @FormParam("dtcInicial") String dtcInicial, @FormParam("dtcFinal") String dtcFinal, @FormParam("first") Integer first, @FormParam("max") Integer max, @FormParam("ideUsuario") Integer ideUsuarioLogado, @FormParam("ideMunicipio") Integer ideMunicipio,  @FormParam("numProcesso") String numProcesso, @FormParam("usuarioExterno") Boolean usuarioExterno, @FormParam("cpfcnpj") String cpfcnpj) {
		  Map<String, Object> parametros = new HashMap<String, Object>();  
		  try {
			
			List<Integer>  idesPessoas = null; 
			List<String>  documentoPessoas = null; 
			Date dataIni = Util.formataDataComHora(dtcInicial + " 00:00:00");
			Date dataFim = Util.formataDataComHora(dtcFinal  + " 23:59:59");
		
   		    if(usuarioExterno && Util.isNullOuVazio(requerente)){
			   PessoaFisica usuarioLogado = new PessoaFisica(ideUsuarioLogado);
			   idesPessoas =  permissaoPerfilService.listarIdesPessoasAptas(usuarioLogado);
			
			   if(isConsultaProcessoExterno(cpfcnpj, dataIni, dataFim, numProcesso)){
				   documentoPessoas =  permissaoPerfilService.listarCpfCnpJPessoasAptas(usuarioLogado);
				   documentoPessoas.add(pessoaFisicaService.carregarPessoaFisicaGet(ideUsuarioLogado).getNumCpf());
			   }
		   }
			List<ProcessoWS> processos = null;
			processos = processoService.consultarProcessoWs(requerente, numProcesso, status, dataIni, dataFim, idesPessoas, ato, categoria, ideMunicipio,cpfcnpj,documentoPessoas,isConsultaProcessoExterno(cpfcnpj, Util.formataData(dtcInicial), Util.formataData(dtcFinal), numProcesso));
			JSONSerializer serializer = new JSONSerializer(); 
			if (!Util.isNullOuVazio(processos)) {
				    String processo = serializer.exclude("nomMunicipio","municipio","diasFormadoProcesso", "desEmail", "*.class", "requerimento.numRequerimento","requerimento.cpfCnpj","requerimento.ideEmpreendimento","requerimento.requerente","requerimento.dtcRequerimento","requerimento.nomStatusRequerimento","requerimento.nomEmpreendimento","requerimento.ideRequerente").transform(new DateTransformer("dd/MM/yyyy"), "dtcFormacao").include("requerimento.ideRequerimento").serialize(paginarProcessoWs(processos,first,max));
					parametros.put("PROCESSO",  new JSONArray(processo));
					parametros.put("QTD",   processos.size());
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
	   * Método para buscar os dados gerais da aba processo.
	   * 
	   * @param ideProcesso
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("detalheDadosGerais")
	  @Produces("text/html; charset=UTF-8")
	  public String getProcesso(@FormParam("ideProcesso") Integer ideProcesso) {
		Map<String, Object> parametros = new HashMap<String, Object>();  
		try {

			ProcessoWS processoWS = processoService.detalheProcessoWs(ideProcesso,null);
			JSONSerializer serializer = new JSONSerializer(); 
			String processo = serializer.exclude("*.class").transform(new DateTransformer("dd/MM/yyyy"), "dtcFormacao","requerimento.dtcRequerimento").serialize(processoWS);
			if (!Util.isNullOuVazio(processoWS)) {
					parametros.put("PROCESSO",  new JSONObject(processo));
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
	   * Método para buscar os atos e tipologias do processo
	   * 
	   * @param ideProcesso
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("detalheProcessoAto")
	  @Produces("text/html; charset=UTF-8")
	  public String getProcessoAto(@FormParam("ideProcesso") Integer ideProcesso) {
		Map<String, Object> parametros = new HashMap<String, Object>();  
		try {

			ArrayList<ProcessoAto> processoAto = (ArrayList<ProcessoAto>) processoAtoService.listarAtosPorProcesso(ideProcesso);
			JSONSerializer serializer = new JSONSerializer(); 
			String atoTipologia = serializer.exclude("*.class").serialize(processoAto);
			if (!Util.isNullOuVazio(processoAto)) {
					parametros.put("PROCESSO_ATO",  new JSONArray(atoTipologia));
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
	   * Método para buscar o nome dos documentos de formação do processo
	   * 
	   * @param ideRequerimento
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("detalheDocumentos")
	  @Produces("text/html; charset=UTF-8")
	  public String getDocumentosProcesso(@FormParam("ideRequerimento") Integer ideRequerimento, @FormParam("indConsultaPublica") boolean indConsultaPublica) {
		Map<String, Object> parametros = new HashMap<String, Object>();  
		ArrayList<DocumentoObrigatorioRequerimento> documentos; 
		try {
			if(indConsultaPublica){
				documentos = (ArrayList<DocumentoObrigatorioRequerimento>) documentoObrigatorioRequerimentoService.buscarDocumentosDeFormacaoPublicos(ideRequerimento);
			}else{
				documentos = (ArrayList<DocumentoObrigatorioRequerimento>) documentoObrigatorioRequerimentoService.buscarDocumentosObrigatoriosRequerimentoEnquadramentoByIdeRequerimento(ideRequerimento);
			}
			JSONSerializer serializer = new JSONSerializer(); 
			String documentoFormacao = serializer.exclude("*.class").serialize(documentos);
			if (!Util.isNullOuVazio(documentos)) {
					parametros.put("DOCUMENTOS",  new JSONArray(documentoFormacao));
			} else {
					parametros.put("ERRO", "001");
					parametros.put("MSGERRO", "Não existem Documentos de formação para este processo.");
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
	   * Métodos para buscar os comprovantes de boleto e DAE
	   * 
	   * @param ideRequerimento
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("detalheComprovante")
	  @Produces("text/html; charset=UTF-8")
	  public String getComprovante(@FormParam("ideRequerimento") Integer ideRequerimento) {
		Map<String, Object> parametros = new HashMap<String, Object>();  
		try {
			Collection<ComprovantePagamento> comprovante =  comprovantePagamentoService.listarByIdeRequerimentoAndTipoBoletoPagamento(ideRequerimento);
			List<ComprovantePagamentoDae> dae = tratarComprovanteDae(comprovantePagamentoDaeService.obterPorIdRequerimento(ideRequerimento));
			JSONSerializer serializer = new JSONSerializer(); 
			String boleto = serializer.exclude("*.class").transform(new DateTransformer("dd/MM/yyyy"), "dtcValidacao").serialize(comprovante);
			String boletoDae = serializer.exclude("*.class").transform(new DateTransformer("dd/MM/yyyy"), "dtcValidacao").serialize(dae);
			if (!Util.isNullOuVazio(comprovante)) {
				parametros.put("BOLETO",  new JSONArray(boleto));
			} 
			if (!Util.isNullOuVazio(dae)) {
				parametros.put("DAE",  new JSONArray(boletoDae));
			} 
			if (Util.isNullOuVazio(comprovante) && Util.isNullOuVazio(dae)){
				parametros.put("ERRO", "001");
				parametros.put("MSGERRO", "Nenhum boleto Bancário ou DAE para este processo.");
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametros);
		return objJson.toString();
	  }
	  
	  
	  private List<ComprovantePagamentoDae> tratarComprovanteDae(List<ComprovantePagamentoDae> lista){
		  if(!Util.isNullOuVazio(lista)&& lista.size() == 1){
			  return lista;
		  }
		  if(!Util.isNullOuVazio(lista) && lista.size() > 1){
			  List<ComprovantePagamentoDae> listaDaeRetorno = new ArrayList<ComprovantePagamentoDae>();
			  List<BoletoDaeRequerimento> listaBoletoDaeRequerimento = new ArrayList<BoletoDaeRequerimento>();
			  for(ComprovantePagamentoDae dae : lista){
				 if(!listaBoletoDaeRequerimento.contains(dae.getIdeBoletoDaeRequerimento())){
					 listaDaeRetorno.add(dae);
					 listaBoletoDaeRequerimento.add(dae.getIdeBoletoDaeRequerimento());
				 }
				 return listaDaeRetorno;
			  }
		  }
		  	return null;
	}

	/**
	   * Método para listar todos os status do processo.
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("status")
	  @Produces("text/html; charset=UTF-8")
	  public String getStatusProcesso() {
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>(); 
		  try {
			  Collection<StatusFluxo> status = statusFluxoService.listarTodosStatusProcessoOrderByAsc();
				if(!Util.isNullOuVazio(status) ){
					JSONSerializer serializer = new JSONSerializer(); 
					String lista = serializer.exclude("*.class").serialize(status);
					parametros.put("STATUS", new JSONArray(lista));
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
	   * Método para listar todas as categorias (Tipo Ato).
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("categoria")
	  @Produces("text/html; charset=UTF-8")
	  public String getTipoAto() {
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>(); 
		  try {
			  Collection<TipoAto> categorias = tipoAtoService.listarTiposAtoOrderByAsc();
				if(!Util.isNullOuVazio(categorias) ){
					JSONSerializer serializer = new JSONSerializer(); 
					String lista = serializer.exclude("*.class").serialize(categorias);
					parametros.put("CATEGORIAS", new JSONArray(lista));
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
	   * Método para listar todos os atos da categorias (Tipo Ato).
	   * @return
	   * @param categoria ide da categoria do ato  
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("ato")
	  @Produces("text/html; charset=UTF-8")
	  public String getAto(@FormParam("categoria") Integer tipoAto) {
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>(); 
		  try {
			  Collection<AtoAmbiental> ato = atoAmbientalService.obterAtoAmbientalPorTipoAto(tipoAto);
				if(!Util.isNullOuVazio(ato) ){
					JSONSerializer serializer = new JSONSerializer(); 
					String lista = serializer.exclude("*.class").serialize(ato);
					parametros.put("ATOS", new JSONArray(lista));
				}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		  JSONObject objJson = new JSONObject(parametros);
		  return objJson.toString();
	  }
	  
	  /**Método para realizar a busca da consulta pública
	   * 
	   * @param first
	   * @param max
	   * @param municipio id do municipio
	   * @param cpfcnpj
	   * @param nomRequerente
	   * @param nomEmpreendimento
	   * @return
	   */
	  
	  @POST
	  @Path("consultaPublica")
	  @Produces("text/html; charset=UTF-8")
	  public String consultaPublica(@FormParam("first") Integer first, @FormParam("max") Integer max, @FormParam("municipio") Integer municipio, @FormParam("cpfcnpj") String cpfcnpj,@FormParam("nomRequerente") String nomRequerente,@FormParam("nomEmpreendimento") String nomEmpreendimento,@FormParam("numProcesso") String numProcesso) {
		Map<String, Object> parametros = new HashMap<String, Object>();  
		try {
			List<ProcessoWS> processos = null;
			processos = processoService.consultarPublicaWS(first, max, municipio, cpfcnpj, nomRequerente, nomEmpreendimento, numProcesso);
			JSONSerializer serializer = new JSONSerializer(); 
			String processo = serializer.exclude("dscStatusFluxo","municipio", "desEmail", "*.class","requerimento.numRequerimento","requerimento.cpfCnpj","requerimento.ideEmpreendimento","requerimento.requerente","requerimento.ideRequerente","requerimento.dtcRequerimento","requerimento.nomStatusRequerimento","requerimento.nomEmpreendimento","sistema","nomMunicipio").transform(new DateTransformer("dd/MM/yyyy"), "dtcFormacao").serialize(processos);
			if (!Util.isNullOuVazio(processos)) {
					parametros.put("PROCESSO",  new JSONArray(processo));
					parametros.put("QTD",  processoService.countConsultarPublicaWS(municipio, cpfcnpj, nomRequerente, nomEmpreendimento, numProcesso));
					
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
	  
	  private boolean isConsultaProcessoExterno(String cpfcnpj, Date dtcInicial,Date dtcFinal, String numProcesso) {
			 return !Util.isNullOuVazio(cpfcnpj) || (!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal)) || !Util.isNullOuVazio(numProcesso);
		}
	  
	  /**
	   * Método para buscar os dados gerais da aba processo.
	   * 
	   * @param ideProcesso
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("detalheDadosGeraisPorNumero")
	  @Produces("text/html; charset=UTF-8")
	  public String getProcesso(@FormParam("numProcesso") String numProcesso) {
		Map<String, Object> parametros = new HashMap<String, Object>();  
		try {

			ProcessoWS processoWS = processoService.detalheProcessoWs(null,numProcesso);
			JSONSerializer serializer = new JSONSerializer(); 
			String processo = serializer.exclude("*.class","dscStatusFluxoExterno","nomRazaoSocial","sistema","requerimento.cpfCnpj","requerimento.ideEmpreendimento","requerimento.requerente","requerimento.requerente","requerimento.ideRequerente","requerimento.nomStatusRequerimento").transform(new DateTransformer("dd/MM/yyyy"), "dtcFormacao","requerimento.dtcRequerimento").serialize(processoWS);
			if (!Util.isNullOuVazio(processoWS)) {
					parametros.put("PROCESSO",  new JSONObject(processo));
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
	  
	  public List<ProcessoWS> paginarProcessoWs(List<ProcessoWS> listProcesso,Integer first, Integer max){
	    if(listProcesso.size()< first+max){
			return listProcesso.subList(first, listProcesso.size());
		}
		return listProcesso.subList(first, first+max);
	  }
}
