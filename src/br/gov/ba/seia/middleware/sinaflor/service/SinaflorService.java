package br.gov.ba.seia.middleware.sinaflor.service;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import br.gov.ba.seia.dao.ControleProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.EmpreendimentoDAOImpl;
import br.gov.ba.seia.dao.EspecieSupressaoAutorizacaoDAOImpl;
import br.gov.ba.seia.dao.PessoaDAOImpl;
import br.gov.ba.seia.dao.PortariaDAOimpl;
import br.gov.ba.seia.dao.ProcessoAtoConcedidoDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoDAOImpl;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EspecieSupressaoAutorizacao;
import br.gov.ba.seia.entity.ImovelRuralSicar;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Portaria;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.middleware.restful.RESTfulResponse;
import br.gov.ba.seia.middleware.restful.RESTfulUtil;
import br.gov.ba.seia.middleware.restful.RequestContentType;
import br.gov.ba.seia.middleware.restful.RequestParameter;
import br.gov.ba.seia.middleware.restful.RequestProperty;
import br.gov.ba.seia.middleware.sinaflor.enumerator.SinaflorApiEnum;
import br.gov.ba.seia.middleware.sinaflor.enumerator.StatusAutorizacaoEnum;
import br.gov.ba.seia.middleware.sinaflor.enumerator.UnidadeIbamaEnum;
import br.gov.ba.seia.middleware.sinaflor.exception.SinaflorRuntimeException;
import br.gov.ba.seia.middleware.sinaflor.model.AnexoEntrada;
import br.gov.ba.seia.middleware.sinaflor.model.EmpreendimentoEntrada;
import br.gov.ba.seia.middleware.sinaflor.model.EmpreendimentoSaida;
import br.gov.ba.seia.middleware.sinaflor.model.MunicipioSaida;
import br.gov.ba.seia.middleware.sinaflor.model.NomePopularSaida;
import br.gov.ba.seia.middleware.sinaflor.model.PessoaSaida;
import br.gov.ba.seia.middleware.sinaflor.model.ProdutoFlorestalSaida;
import br.gov.ba.seia.middleware.sinaflor.model.ProjetoEntrada;
import br.gov.ba.seia.middleware.sinaflor.model.ProjetoSaida;
import br.gov.ba.seia.middleware.sinaflor.model.ResponsavelTecnicoEntrada;
import br.gov.ba.seia.middleware.sinaflor.model.RetornoPaginado;
import br.gov.ba.seia.middleware.sinaflor.model.RetornoSucessoCriacao;
import br.gov.ba.seia.middleware.sinaflor.model.TaxonomiaSaida;
import br.gov.ba.seia.middleware.sinaflor.model.TokenSaida;
import br.gov.ba.seia.middleware.sinaflor.model.VolumeAutorizadoEntrada;
import br.gov.ba.seia.middleware.sinaflor.util.Base64Util;
import br.gov.ba.seia.middleware.sinaflor.util.SinaflorApiUtil;
import br.gov.ba.seia.middleware.sinaflor.util.SinaflorShapeFileUtil;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.ImovelRuralSicarService;
import br.gov.ba.seia.service.ResponsavelEmpreendimentoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.util.CSVUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
/**
 * Classe serviço sinaflor 
 * @author 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SinaflorService {
	
	@Inject
	private PessoaDAOImpl pessoaDAOImpl;
	@Inject
	private EmpreendimentoDAOImpl empreendimentoDAOImpl;
	@EJB
	private ProcessoAtoDAOImpl processoAtoDAOImpl;
	@EJB
	private ProcessoAtoConcedidoDAOImpl processoAtoConcedidoDAOImpl;
	@EJB
	private EspecieSupressaoAutorizacaoDAOImpl especieSupressaoAutorizacaoDAOImpl;
	@EJB
	private ControleProcessoAtoDAOImpl controleProcessoAtoDAOImpl;
	@EJB
	private PortariaDAOimpl portariaDAOimpl;
	
	@EJB
	private TipologiaService tipologiaService;
	@EJB
	private ResponsavelEmpreendimentoService responsavelEmpreendimentoService;
	@EJB
	private ImovelRuralSicarService imovelRuralSicarService;
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	public EmpreendimentoSaida cadastrarEmpreendimento(TokenSaida token, PessoaSaida pessoaCadastrada, Pessoa pessoaSEIA, VwConsultaProcesso processoSEIA, List<String> log) throws Exception {
		
		EmpreendimentoEntrada novoEmpreendimento = prepararNovoEmpreendimento(processoSEIA, pessoaSEIA, pessoaCadastrada, log);
		EmpreendimentoSaida empreendimentoCadastrado = enviarNovoEmpreendimentoSINAFLOR(token, novoEmpreendimento, log);
		
		return empreendimentoCadastrado;
	}
	
	public void cadastrarProjeto(TokenSaida token, EmpreendimentoSaida empreendimentoCadastrado, VwConsultaProcesso processoSEIA,  List<String> log) throws Exception  {
		
		List<ProcessoAto> listaProcessoAto = processoAtoDAOImpl.listarProcessoAtoParaSinaflor(processoSEIA.getIdeProcesso());
		
		for(ProcessoAto processoAto : listaProcessoAto) {
			ProjetoEntrada novoProjeto = prepararNovoProjeto(processoSEIA, processoAto, empreendimentoCadastrado, log);
			ProjetoSaida projetoCadastrado = enviarNovoProjetoSINAFLOR(token, novoProjeto, log);
		}
	}

	private EmpreendimentoEntrada prepararNovoEmpreendimento(VwConsultaProcesso processoSEIA, Pessoa pessoaSEIA, PessoaSaida pessoaCadastrada, List<String> log) throws Exception {
		
		log.add("Preparar novo empreendimento");
		log.add("=> início");
		
		Empreendimento empreendimento = carregarEmpreendimentoSEIA(processoSEIA);
		MunicipioSaida municipio = carregarMunicipioSINAFLOR(empreendimento, log);
		
		EmpreendimentoEntrada empreendimentoEntrada = new EmpreendimentoEntrada();
		empreendimentoEntrada.setMunicipio(municipio.getId());
		empreendimentoEntrada.setNome(empreendimento.getNomEmpreendimento());
		empreendimentoEntrada.setProprietario(pessoaCadastrada.getIdentificacao());
		empreendimentoEntrada.setUnidadeIbama(UnidadeIbamaEnum.INEMA.getId());
		
		if(pessoaSEIA.isPJ()) {
			empreendimentoEntrada.setInscricaoEstadual(pessoaSEIA.getPessoaJuridica().getNumInscricaoEstadual());
		}
		
		montarListaAtividade(empreendimento, empreendimentoEntrada);
		montarLocalizacaoGeografica(empreendimento, empreendimentoEntrada);
		
		log.add("=> fim");
		
		return empreendimentoEntrada;
	}

	private void montarLocalizacaoGeografica(Empreendimento empreendimento, EmpreendimentoEntrada empreendimentoEntrada) throws Exception {
		
		LocalizacaoGeografica localizacaoGeografica = empreendimento.getIdeLocalizacaoGeografica();
		
		validarLocalizacaoGeografica(localizacaoGeografica);
		
		BigDecimal centroideLatitude = new BigDecimal(localizacaoGeografica.getPontoLatitude()).setScale(13,RoundingMode.HALF_UP);
		empreendimentoEntrada.setCentroideLatitude(centroideLatitude);
		
		BigDecimal centroideLongitude = new BigDecimal(localizacaoGeografica.getPontoLongitude()).setScale(13,RoundingMode.HALF_UP);
		empreendimentoEntrada.setCentroideLongitude(centroideLongitude);
						
		String shapefile = SinaflorShapeFileUtil.makeShapeFile(localizacaoGeografica);
		
		File prj = new File(shapefile+".prj");
		empreendimentoEntrada.setShapePRJ(new AnexoEntrada(prj.getName(),Base64Util.encoder(prj)));
		
		File shp = new File(shapefile+".shp");
		empreendimentoEntrada.setShapeSHP(new AnexoEntrada(shp.getName(),Base64Util.encoder(shp)));
	}

	private void montarListaAtividade(Empreendimento empreendimento, EmpreendimentoEntrada empreendimentoEntrada) {
		empreendimentoEntrada.setAtividades(new ArrayList<String>());
		for(Tipologia t : empreendimento.getTipologias()) {
			empreendimentoEntrada.getAtividades().add(t.getDesTipologia());
		}
	}
	
	private ProjetoEntrada prepararNovoProjeto(VwConsultaProcesso processoSEIA, ProcessoAto processoAto, EmpreendimentoSaida empreendimentoCadastrado, List<String> log) throws Exception {
		
		
		log.add("Preparar novo projeto");
		log.add("=> início");
		
		ProjetoEntrada projeto = new ProjetoEntrada();
		
		projeto.setEmpreendimento(empreendimentoCadastrado.getId());
		projeto.setStatusAutorizacao(StatusAutorizacaoEnum.AUTORIZACAO_EMITIDA.getId());
		projeto.setUnidadeIbama(UnidadeIbamaEnum.INEMA.getId());
		
		projeto.setTipoAtividade(processoAto.getAtoAmbiental().getIdeAtoSinaflor().getIdeAtoSinaflor());
		
		montarListaProperiedadesRurais(projeto, processoSEIA);
		montarListaResponsaveis(projeto, processoSEIA);
		
		calcularValidadeProjeto(projeto, processoAto, processoSEIA);
		
		List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao = especieSupressaoAutorizacaoDAOImpl.listarPorProcessoAto(processoAto);
		montarPanilhaInventario(listaEspecieSupressaoAutorizacao, projeto);
		montarListaVolumeAutorizado(listaEspecieSupressaoAutorizacao, projeto, log);
		
		List<ProcessoAtoConcedido> listaProcessoAtoConcedido = processoAtoConcedidoDAOImpl.listarProcessoAtoConcedidoPor(processoSEIA.getIdeProcesso());
		calcularAreaAutorizada(listaProcessoAtoConcedido, projeto);
		montarLocalizacaoGeografica(listaProcessoAtoConcedido, projeto, processoSEIA);
		montarArquivoIntegrado(processoSEIA.getIdeProcesso(), projeto);
		
		log.add("=> fim");
		
		return projeto;
	}

	private void calcularAreaAutorizada(List<ProcessoAtoConcedido> listaProcessoAtoConcedido, ProjetoEntrada projeto) {
		BigDecimal areaAutorizada = BigDecimal.ZERO;
		for(ProcessoAtoConcedido pac : listaProcessoAtoConcedido) {
			areaAutorizada=areaAutorizada.add(pac.getValAtividade());
		}
		areaAutorizada.setScale(4,RoundingMode.HALF_UP);
		projeto.setAreaAutorizada(areaAutorizada);
	}
	
	private void calcularValidadeProjeto(ProjetoEntrada projeto, ProcessoAto processoAto, VwConsultaProcesso processoSEIA) {
		try {
			Portaria p = portariaDAOimpl.buscarPortariaByProcesso(new Processo(processoSEIA.getIdeProcesso()));
			ControleProcessoAto cpt = controleProcessoAtoDAOImpl.buscarUltimoDeferidoOuIndeferidoPorProcessoAto(processoAto.getIdeProcessoAto());
			
			if(Util.isNullOuVazio(p)) {
				MensagemUtil.erro("Portaria não encontrada");
				throw new Exception();
				
			} else if(Util.isNullOuVazio(cpt)) {
				MensagemUtil.erro("Controle Processo Ato não encontrado");
				throw new Exception();
			}
			
			Date inicio = p.getDtcPublicacaoPortaria();
			
			GregorianCalendar fim = new GregorianCalendar();
			fim.setTime(inicio);
			fim.add(Calendar.YEAR, cpt.getNumPrazoValidade());
			
			projeto.setValidadeInicio(new SimpleDateFormat("dd/MM/yyyy").format(inicio));
			projeto.setValidadeFim(new SimpleDateFormat("dd/MM/yyyy").format(fim.getTime()));
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void montarPanilhaInventario(List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao, ProjetoEntrada projeto) throws Exception {
			
		String CSV_PATH = "/tmp/PLANILHA-" + projeto.getEmpreendimento() + ".csv";
		File csvFile = new File(CSV_PATH);
		List<List<String>> itensPlanilha = new ArrayList<List<String>>();
		itensPlanilha.add(Arrays.asList("Produto", "Nome científico", "Nome popular", "Volume"));
		
		for(EspecieSupressaoAutorizacao esa : listaEspecieSupressaoAutorizacao) {
			String produto = esa.getIdeProduto().getDscProduto();
			String nomeCientifico = "";
			String nomePopular = "";
			if(!Util.isNullOuVazio(esa.getIdeEspecieSupressao())) {
				nomeCientifico = esa.getIdeEspecieSupressao().getNomEspecieSupressao();
			}
			if(!Util.isNullOuVazio(esa.getIdeNomePopularEspecie())) {
				nomePopular = esa.getIdeNomePopularEspecie().getNomPopularEspecie();
			}
			BigDecimal volume = calcularVolumeAPP(esa);
			itensPlanilha.add(Arrays.asList(produto, nomeCientifico, nomePopular, volume.toString()));
		}
		
		CSVUtil.writeFile(csvFile, itensPlanilha);
		projeto.setInventarioPlanilha(new AnexoEntrada(csvFile.getName(), Base64Util.encoder(csvFile)));
	}
	
	private void montarListaVolumeAutorizado(List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao, ProjetoEntrada projeto, List<String> log) throws Exception {
			
		projeto.setVolumesAutorizados(new ArrayList<VolumeAutorizadoEntrada>());
			
		for(EspecieSupressaoAutorizacao esa : listaEspecieSupressaoAutorizacao) {
			
			ProdutoFlorestalSaida produto = carregarProdutoSINAFLOR(esa.getIdeProduto().getDscProduto(), log);
			
			TaxonomiaSaida especie = null;
			NomePopularSaida nomePopular = null;
			
			if(!Util.isNullOuVazio(esa.getIdeEspecieSupressao())) {
				especie = carregarEspecieSINAFLOR(esa.getIdeEspecieSupressao().getNomEspecieSupressao(), log);
			}
			
			if(!Util.isNullOuVazio(esa.getIdeNomePopularEspecie())) {
				nomePopular = carregarNomePopularSINAFLOR(especie.getId(), log);
			}
			
			BigDecimal volume = calcularVolumeAPP(esa);
			
			VolumeAutorizadoEntrada volumeAutorizadoEntrada = new VolumeAutorizadoEntrada();
			if(!Util.isNullOuVazio(especie)) volumeAutorizadoEntrada.setEspecie(especie.getId());
			if(!Util.isNullOuVazio(nomePopular)) volumeAutorizadoEntrada.setNomePopular(nomePopular.getId());
			volumeAutorizadoEntrada.setProduto(produto.getId());
			volumeAutorizadoEntrada.setVolume(volume);
			
			projeto.getVolumesAutorizados().add(volumeAutorizadoEntrada);
		}
		
	}

	private BigDecimal calcularVolumeAPP(EspecieSupressaoAutorizacao esa) {
		
		BigDecimal volume = new BigDecimal(0);
		
		if(!Util.isNullOuVazio(esa.getVolumeTotalEmApp())) volume = volume.add(new BigDecimal(esa.getVolumeTotalEmApp()));
		if(!Util.isNullOuVazio(esa.getVolumeTotalForaApp())) volume = volume.add(new BigDecimal(esa.getVolumeTotalForaApp()));
		
		return volume.setScale(4,RoundingMode.HALF_UP);
	}
	
	private NomePopularSaida carregarNomePopularSINAFLOR(Integer idEspecie, List<String> log) {
		try {
			String url = SinaflorApiUtil.getUrl(SinaflorApiEnum.TAXONOMIAS, idEspecie, SinaflorApiEnum.NOMES_POPULARES);
			RESTfulResponse response = SinaflorApiUtil.httpGET(url, RequestContentType.JSON);
			
			log.add("Carregar nome popular");
			log.add(url);
			
			if(RESTfulUtil.returnedSuccess(response)) {
				log.add(retornarLog(response));
				Type type = new TypeToken<RetornoPaginado<NomePopularSaida>>(){}.getType();
				RetornoPaginado<NomePopularSaida> retorno = new Gson().fromJson(response.getResponseContent(), type);
				return retorno.getRetorno().get(0);
			}
			else {
				throw new Exception(retornarLog(response));
			}
		}
		catch (Exception e) {
			throw new SinaflorRuntimeException(e.getMessage());
		}
	}

	private TaxonomiaSaida carregarEspecieSINAFLOR(String nomEspecieSupressao, List<String> log) {
		try {
			String url = SinaflorApiUtil.getUrl(SinaflorApiEnum.TAXONOMIAS, new RequestParameter("filtro.descricao", nomEspecieSupressao));
			RESTfulResponse response = SinaflorApiUtil.httpGET(url, RequestContentType.JSON);
			
			log.add("Carregar espécie");
			log.add(url);
			
			if(RESTfulUtil.returnedSuccess(response)) {
				log.add(retornarLog(response));
				Type type = new TypeToken<RetornoPaginado<TaxonomiaSaida>>(){}.getType();
				RetornoPaginado<TaxonomiaSaida> retorno = new Gson().fromJson(response.getResponseContent(), type);
				return retorno.getRetorno().get(0);
			}
			else {
				throw new Exception(retornarLog(response));
			}
		}
		catch (Exception e) {
			throw new SinaflorRuntimeException(e.getMessage());
		}		
	}

	private ProdutoFlorestalSaida carregarProdutoSINAFLOR(String dscProduto, List<String> log) {
		try {
			if(dscProduto.equals("Lenha")) {
				dscProduto = "Lenha (m³)";
			}
			
			String url = SinaflorApiUtil.getUrl(SinaflorApiEnum.PRODUTOS_FLORESTAIS, new RequestParameter("filtro.descricao", dscProduto));
			RESTfulResponse response = SinaflorApiUtil.httpGET(url, RequestContentType.JSON);
			
			log.add("Carregar produto florestal");
			log.add(url);
			
			if(RESTfulUtil.returnedSuccess(response)) {
				log.add(retornarLog(response));
				Type type = new TypeToken<RetornoPaginado<ProdutoFlorestalSaida>>(){}.getType();
				RetornoPaginado<ProdutoFlorestalSaida> retorno = new Gson().fromJson(response.getResponseContent(), type);
				return retorno.getRetorno().get(0);
			}
			else {
				throw new Exception(retornarLog(response));
			}
		}
		catch (Exception e) {
			throw new SinaflorRuntimeException(e.getMessage());
		}
	}
	
	private void montarLocalizacaoGeografica(List<ProcessoAtoConcedido> listaProcessoAtoConcedido, ProjetoEntrada projeto, VwConsultaProcesso processoSEIA) throws Exception {
		
		LocalizacaoGeografica localizacaoGeografica = null;
		if(listaProcessoAtoConcedido.size()==1) {
			localizacaoGeografica = listaProcessoAtoConcedido.get(0).getIdeLocalizacaoGeografica();
		}
		else{
			Empreendimento empreendimento = carregarEmpreendimentoSEIA(processoSEIA);
			localizacaoGeografica = empreendimento.getIdeLocalizacaoGeografica();
		}
		
		validarLocalizacaoGeografica(localizacaoGeografica);
		
		BigDecimal latitude = new BigDecimal(localizacaoGeografica.getPontoLatitude()).setScale(13,RoundingMode.HALF_UP);
		projeto.setLatitudeProjeto(latitude);
		BigDecimal longitude = new BigDecimal(localizacaoGeografica.getPontoLongitude()).setScale(13,RoundingMode.HALF_UP);
		projeto.setLongitudeProjeto(longitude);
		
		LocalizacaoGeografica[] localizacoes = retornarLocalizacoes(listaProcessoAtoConcedido);
		
		String shapefile = SinaflorShapeFileUtil.makeShapeFile(localizacoes);
		
		File prj = new File(shapefile+".prj");
		projeto.setShapePRJ(new AnexoEntrada(prj.getName(),Base64Util.encoder(prj)));
		
		File shp = new File(shapefile+".shp");
		projeto.setShapeSHP(new AnexoEntrada(shp.getName(),Base64Util.encoder(shp)));
	}
	
	private void validarLocalizacaoGeografica(LocalizacaoGeografica lg) {
		List<String> msgs = new ArrayList<String>();
		if(Util.isNull(lg)) {
			msgs.add("- O método montarLocalizacaoGeografica não pode receber uma localização geografica nula");
		}
		else {
			if(Util.isNull(lg.getPontoLatitude())) {
				msgs.add("- O método montarLocalizacaoGeografica não pode receber uma localização geográfica com latitude nula");
			}
			if(Util.isNull(lg.getPontoLongitude())) {
				msgs.add("- O método montarLocalizacaoGeografica não pode receber uma localização geográfica com longitude nula");
			}
		}
		if(!msgs.isEmpty()) {
			StringBuilder retorno = new StringBuilder();
			retorno.append("localização geográfica inválida:\n");
			for (Iterator<String> iterator = msgs.iterator(); iterator.hasNext();) {
				String msg = iterator.next();
				retorno.append(msg);
				retorno.append(iterator.hasNext() ? ";\n" : ".");
			}
			throw new SinaflorRuntimeException(retorno.toString());
		}		
	}

	private LocalizacaoGeografica[] retornarLocalizacoes(List<ProcessoAtoConcedido> listaProcessoAtoConcedido) {
		LocalizacaoGeografica[] localizacoes = new LocalizacaoGeografica[listaProcessoAtoConcedido.size()]; 
		for(int i=0; i < listaProcessoAtoConcedido.size(); i++) {
			localizacoes[i] = listaProcessoAtoConcedido.get(i).getIdeLocalizacaoGeografica();
		}
		return localizacoes;
	}
	
	public PessoaSaida validarPessoaSINAFLOR(String numCpfCnpj, List<String> log) {
		try{
			String url = SinaflorApiUtil.getUrl(SinaflorApiEnum.PESSOAS, new RequestParameter("filtro.cpfCnpj", numCpfCnpj));
			RESTfulResponse response = SinaflorApiUtil.httpGET(url, RequestContentType.JSON);
			
			log.add("Validar cadastro no sinaflor");
			log.add("=> início");
			log.add(url);
			
			System.out.println(response.getResponseContent());
			
			if(RESTfulUtil.returnedSuccess(response)) {
				log.add(retornarLog(response));
				Type type = new TypeToken<RetornoPaginado<PessoaSaida>>(){}.getType();
				RetornoPaginado<PessoaSaida> rp = new Gson().fromJson(response.getResponseContent(), type);
				log.add("=> fim");
				return rp.getRetorno().get(0);
			}
			else {
				throw new Exception(retornarLog(response));
			}
		}
		catch(Exception e) {
			throw new SinaflorRuntimeException(e.getMessage());						
		}
	}
	
	public TokenSaida gerarTokenSINAFLOR(List<String> log) {
		try{
			String url = SinaflorApiUtil.getUrl(SinaflorApiEnum.TOKEN);
			RESTfulResponse response = SinaflorApiUtil.httpGET(url, RequestContentType.JSON);
			
			log.add("Gerar token");
			log.add("=> início");
			log.add(url);
			
			if(RESTfulUtil.returnedSuccess(response)) {
				log.add(retornarLog(response));
				TokenSaida token = new Gson().fromJson(response.getResponseContent(), TokenSaida.class);
				log.add("=> fim");
				return token;
			}
			else {
				throw new Exception(retornarLog(response));
			}
		}
		catch(Exception e) {
			throw new SinaflorRuntimeException(e.getMessage());						
		}
	}
	
	private MunicipioSaida carregarMunicipioSINAFLOR(Empreendimento empreendimento, List<String> log) {
		try{
			String nomMunicipio = empreendimento.getEndereco().getIdeLogradouro().getIdeMunicipio().getNomMunicipioSemAcento();
			
			String url = SinaflorApiUtil.getUrl(SinaflorApiEnum.MUNICIPIOS, new RequestParameter("filtro.nome", nomMunicipio));
			RESTfulResponse response = SinaflorApiUtil.httpGET(url, RequestContentType.JSON);
			
			log.add("Carregar município");
			log.add(url);
			
			if(RESTfulUtil.returnedSuccess(response)) {
				log.add(retornarLog(response));
				Type type = new TypeToken<RetornoPaginado<MunicipioSaida>>(){}.getType();
				RetornoPaginado<MunicipioSaida> retorno = new Gson().fromJson(response.getResponseContent(), type);
				return retorno.getRetorno().get(0);
			}
			else {
				throw new Exception(retornarLog(response));
			}
		}
		catch(Exception e) {
			throw new SinaflorRuntimeException(e.getMessage());
		}
	}
	
	private static EmpreendimentoSaida enviarNovoEmpreendimentoSINAFLOR(TokenSaida token, EmpreendimentoEntrada empreendimento, List<String> log) {
		try {
			Gson gson = new Gson();
			
			String input = gson.toJson(empreendimento);
			
			String url = SinaflorApiUtil.getUrl(SinaflorApiEnum.EMPREENDIMENTOS);
			RESTfulResponse response = SinaflorApiUtil.httpPOST(url, input, RequestContentType.JSON, new RequestProperty("token-transacional", token.getHash()));
			
			log.add("Cadastrar empreendimento");
			log.add("=> início");
			log.add(url);
			
			if(RESTfulUtil.returnedSuccess(response)) {
				log.add(retornarLog(response));
				RetornoSucessoCriacao retorno  =  gson.fromJson(response.getResponseContent(), RetornoSucessoCriacao.class);
				EmpreendimentoSaida empreendimentoSaida = new EmpreendimentoSaida();
				empreendimentoSaida.setId(retorno.getId());
				log.add("=> fim");
				return empreendimentoSaida;
			}
			else {
				throw new Exception(retornarLog(response));
			}
		}
		catch (Exception e) {
			throw new SinaflorRuntimeException(e.getMessage());
		}
	}

	private static ProjetoSaida enviarNovoProjetoSINAFLOR(TokenSaida token, ProjetoEntrada projeto, List<String> log) {
		try {
			Gson gson = new Gson();
			
			String input = gson.toJson(projeto);
			
			String url = SinaflorApiUtil.getUrl(SinaflorApiEnum.PROJETOS);
			RESTfulResponse response = SinaflorApiUtil.httpPOST(url, input, RequestContentType.JSON, new RequestProperty("token-transacional", token.getHash()));
			
			log.add("Cadastrar projeto");
			log.add("=> início");
			log.add(url);
			
			if(RESTfulUtil.returnedSuccess(response)) {
				log.add(retornarLog(response));
				RetornoSucessoCriacao retorno  =  gson.fromJson(response.getResponseContent(), RetornoSucessoCriacao.class);
				ProjetoSaida projetoSaida = new ProjetoSaida();
				projetoSaida.setId(retorno.getId());
				log.add("=> fim");
				return projetoSaida;
			}
			else {
				throw new Exception(retornarLog(response));
			}
		}
		catch (Exception e) {
			throw new SinaflorRuntimeException(e.getMessage());
		}
	}
	
	private static String retornarLog(RESTfulResponse response) {
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(response.getResponseContent()).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(json);
	}
	
	private Empreendimento carregarEmpreendimentoSEIA(VwConsultaProcesso processoSEIA) throws Exception {
		Empreendimento empreendimento = empreendimentoDAOImpl.buscarEmpreendimentoPorId(processoSEIA.getIdeEmpreendimento());
		empreendimento.setTipologias(tipologiaService.buscarTipologiasByIdeEmpreendimento(empreendimento));
		return empreendimento;
	}
	
	private void montarListaResponsaveis(ProjetoEntrada projeto, VwConsultaProcesso processoSEIA) throws Exception {
		List<ResponsavelEmpreendimento> listaResponsavel = responsavelEmpreendimentoService.listarResponsavelEmpreendimentoPor(processoSEIA.getIdeEmpreendimento());
		
		if(!Util.isNullOuVazio(listaResponsavel)) {
			projeto.setResponsaveisTecnicos(new ArrayList<ResponsavelTecnicoEntrada>());
			
			for(ResponsavelEmpreendimento re : listaResponsavel) {
				ResponsavelTecnicoEntrada responsavelTecnicoEntrada = new ResponsavelTecnicoEntrada();
				responsavelTecnicoEntrada.setArt(re.getNumART());
				responsavelTecnicoEntrada.setPessoa(re.getIdePessoaFisica().getNomPessoa());
				
				projeto.getResponsaveisTecnicos().add(responsavelTecnicoEntrada);
			}
		}
		
	}
	
	private void montarListaProperiedadesRurais(ProjetoEntrada projeto, VwConsultaProcesso processoSEIA) throws Exception {
		List<ImovelRuralSicar> listaImovelRuralScar = imovelRuralSicarService.listarImovelRuralSicarPor(processoSEIA.getIdeEmpreendimento());
		
		if(!Util.isNullOuVazio(listaImovelRuralScar)) {
			projeto.setPropriedadesRurais(new ArrayList<String>());
			
			for(ImovelRuralSicar irs : listaImovelRuralScar) {
				
				if(!Util.isNullOuVazio(irs.getNumSicar())) {
					projeto.getPropriedadesRurais().add(
							irs.getNumSicar().replaceAll("\\.", "")); //regex para tirar os pontos do número sicar
				}
			}
		}
	}
	
	private void montarArquivoIntegrado(Integer ideProcesso, ProjetoEntrada projetoEntrada) throws Exception {
		
		ArquivoProcesso arquivoProcesso = arquivoProcessoService.carregarUltimaPortariaCertificado(ideProcesso, Boolean.FALSE);
		
		StreamedContent conteudo = gerenciaArquivoService.getContentFile(arquivoProcesso.getDscCaminhoArquivo());
		
		projetoEntrada.setArquivoIntegrado(new AnexoEntrada(conteudo.getName(), Util.encodeBase64(IOUtils.toByteArray(conteudo.getStream()))));
	}
	
	public Pessoa carregarPessoaSEIA(VwConsultaProcesso processoSEIA) throws Exception {
		return pessoaDAOImpl.buscarPessoaPorId(processoSEIA.getIdePessoaRequerente());
	}
	
	public void autenticar() throws Exception {
		SinaflorApiUtil.autenticar();
	}
	public void finalizar() {
		SinaflorApiUtil.finalizar();
	}

}