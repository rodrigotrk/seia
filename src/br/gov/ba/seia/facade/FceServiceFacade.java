package br.gov.ba.seia.facade;

import static br.gov.ba.seia.util.fce.FceUtil.isFceAbastecimentoHumano;
import static br.gov.ba.seia.util.fce.FceUtil.isFceAgrossilvoPastoril;
import static br.gov.ba.seia.util.fce.FceUtil.isFceAsv;
import static br.gov.ba.seia.util.fce.FceUtil.isFceAutorizacaoMineracao;
import static br.gov.ba.seia.util.fce.FceUtil.isFceBarragem;
import static br.gov.ba.seia.util.fce.FceUtil.isFceCanais;
import static br.gov.ba.seia.util.fce.FceUtil.isFceIndustria;
import static br.gov.ba.seia.util.fce.FceUtil.isFceInfraestrutura;
import static br.gov.ba.seia.util.fce.FceUtil.isFceIntervencaoMineracao;
import static br.gov.ba.seia.util.fce.FceUtil.isFceLicenciamentoAquicultura;
import static br.gov.ba.seia.util.fce.FceUtil.isFceLicenciamentoMineracao;
import static br.gov.ba.seia.util.fce.FceUtil.isFceLinhaEnergiaTrasmissaoEnergia;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaAbastecimentoIndustrial;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaAquicultura;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaCaptacaoSubterranea;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaCaptacaoSuperficial;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaDessedentacaoAnimal;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaIrrigacao;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaLancamentoEfluentes;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaPulverizacao;
import static br.gov.ba.seia.util.fce.FceUtil.isFcePerfuracaoPoco;
import static br.gov.ba.seia.util.fce.FceUtil.isFceSES;
import static br.gov.ba.seia.util.fce.FceUtil.isFceSistemaAbastecimentoAgua;
import static br.gov.ba.seia.util.fce.FceUtil.isFceTurismo;
import static br.gov.ba.seia.util.fce.FceUtil.isFceFlorestal;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.FceFlorestalAbstractController;
import br.gov.ba.seia.dao.ProcessoReenquadramentoDAOImpl;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.factory.FceFlorestalControllerFactory;
import br.gov.ba.seia.factory.FceFlorestalServiceFactory;
import br.gov.ba.seia.service.AgrossilvopastorilService;
import br.gov.ba.seia.service.AsvSupressaoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.FceAbastecimentoIndustrialService;
import br.gov.ba.seia.service.FceAquiculturaService;
import br.gov.ba.seia.service.FceBarragemService;
import br.gov.ba.seia.service.FceCaptacaoSubterraneaService;
import br.gov.ba.seia.service.FceCaptacaoSuperficialService;
import br.gov.ba.seia.service.FceDessedentacaoAnimalService;
import br.gov.ba.seia.service.FceIndustriaService;
import br.gov.ba.seia.service.FceIntervencaoAbastecimentoHumanoService;
import br.gov.ba.seia.service.FceIntervencaoBarragemService;
import br.gov.ba.seia.service.FceIrrigacaoService;
import br.gov.ba.seia.service.FceLancamentoEfluentesService;
import br.gov.ba.seia.service.FceLinhaEnergiaService;
import br.gov.ba.seia.service.FceLocalizacaoGeograficaService;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoGeograficaFinalidadeService;
import br.gov.ba.seia.service.FcePulverizacaoService;
import br.gov.ba.seia.service.FceService;
import br.gov.ba.seia.service.FceTurismoService;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;
import br.gov.ba.seia.util.fce.FceUtil;



/**
 * @author eduardo.fernandes
 * @since 09/04/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceServiceFacade {

	@EJB
	private FceService fceService;
	@EJB
	private AgrossilvopastorilService fceAgrossilvoPastorilService;
	@EJB
	private AsvSupressaoService fceAsvService;
	@EJB
	private FceIntervencaoBarragemService fceIntervencaoBarragemService;
	@EJB
	private FceAbastecimentoIndustrialService fceAbastecimentoIndustrialService;
	@EJB
	private FceCaptacaoSubterraneaService fceCaptacaoSubterraneaService;
	@EJB
	private FceCaptacaoSuperficialService fceCaptacaoSuperficialService;
	@EJB
	private FceDessedentacaoAnimalService fceDessedentacaoAnimalService;
	@EJB
	private FceIrrigacaoService fceIrrigacaoService;
	@EJB
	private FceLancamentoEfluentesService fceLancamentoEfluentesService;
	@EJB
	private FcePulverizacaoService fcePulverizacaoService;
	@EJB
	private FceIndustriaService fceIndustriaService;
	@EJB
	private FceLocalizacaoGeograficaService fcePerfuracaoPocoService;
	@EJB
	private FceIntervencaoAbastecimentoHumanoService fceAbastecimentoHumanoService;
	@EJB
	private FceAquiculturaService fceAquiculturaService;
	@EJB
	private FceTurismoService fceTurismoService;
	@EJB
	private FceLicenciamentoAquiculturaServiceFacade fceAquiculturaLicencaServiceFacade;
	@EJB
	private FceOutorgaLocalizacaoGeograficaFinalidadeService fceOutorgaLocalizacaoGeograficaFinalidadeService; 
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioRequerimentoService;
	@EJB
	private FuncionarioService funcionarioService;
	@EJB
	private AnaliseTecnicaServiceFacade analiseTecnicaServiceFacade;
	@EJB
	private FceLicenciamentoMineracaoFacade fceLicenciamentoMineracaoFacade;
	@EJB
	private FceAutorizacaoMineracaoFacade fceAutorizacaoMineracaoFacade;
	@Inject
	private ProcessoReenquadramentoDAOImpl processoReenquadramentoDAOImpl;
	@EJB
	private FceInfraestruturaFacade fceInfraestruturaFacade;
	@EJB
	private FceBarragemService fceBarragemService;
	@EJB
	private FceCanalServiceFacade fceCanalServiceFacade; 
	@EJB
	private FceSaaFacade fceSaaFacade;
	@EJB
	private FceSesFacade fceSesFacade;
	@EJB
	private FceLinhaEnergiaService fceLinhaEnergiaService;
	@EJB
	private FceGeracaoEnergiaFacade fceGeracaoEnergiaFacade;
	@EJB
	private FceIntervencaoMineracaoFacade fceIntervencaoMineracaoFacade;
	@EJB
	private FceFlorestalServiceFactory fceFlorestalServiceFactory;
	@EJB
	private FceFlorestalControllerFactory fceFlorestalControllerFactory;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Fce buscarFceByIdeRequerimentoDocumentoObrigatorioAndOrigemFce(Requerimento requerimento, DocumentoObrigatorio documentoObrigatorio, DadoOrigemEnum DadoOrigemEnum) throws Exception {
		return fceService.buscarFceByIdReqAndDoc(requerimento, documentoObrigatorio, DadoOrigemEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Fce> listarFceByIdeRequerimento(Requerimento requerimento) throws Exception {
		return fceService.listarFceByIdeRequerimento(requerimento);
	}
	
	/*@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)FceFlorestalServiceFactory
	public List<Fce> listarFceByIdeRequerimento(Integer ideProcesso) throws Exception {
		return fceService.listarFceByIdeRequerimento(ideProcesso);
	}*/

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent getImprimirRelatorio(Fce fce) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_fce", fce.getIdeFce());
		lParametros.put("ide_origem_fce", fce.getIdeDadoOrigem().getIdeDadoOrigem());
		lParametros.put("NOM_DOC", retornarNomDocumento(fce));
		lParametros.put("TIPO_DOC", fce.isFceTecnico() ? "DADOS CONCEDIDOS" : "FORMULÁRIO DE CARACTERIZAÇÃO DO EMPREENDIMENTO – FCE");
		// #6563, implementação da exibição dos limites do Empreendimento + APP's adicionadas.
		if(FceUtil.isFceTurismo(fce.getIdeDocumentoObrigatorio())){
			lParametros.put("SHAPE", FceGeoBahiaUtil.criarURLToVisualizarShapeEmpreendimentoAndAppInRelatorioByIdeFce(fce.getIdeFce()));
		}
		
		if(isFceFlorestal(fce.getIdeDocumentoObrigatorio())){
			FceFlorestalAbstractController fceFlorestalAbstractController = fceFlorestalControllerFactory.getInstance(fce);
			return new RelatorioUtil(FceUtil.retornarJasperFce(fce.getIdeDocumentoObrigatorio(),fceFlorestalAbstractController), lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		}
		
		return new RelatorioUtil(FceUtil.retornarJasperFce(fce.getIdeDocumentoObrigatorio()), lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);


	}

	public String retornarNomDocumento(Fce fce) {
		
		if(fce != null && fce.getIdeDocumentoObrigatorio() != null && !Util.isNullOuVazio(fce.getIdeDocumentoObrigatorio().getNomDocumentoObrigatorio())) {
			String nomDocumentoObrigatorio = fce.getIdeDocumentoObrigatorio().getNomDocumentoObrigatorio();
			
			if(!Util.isNullOuVazio(nomDocumentoObrigatorio)) {
				return nomDocumentoObrigatorio.substring(nomDocumentoObrigatorio.lastIndexOf("-") + 1, nomDocumentoObrigatorio.length()).trim().toUpperCase();
			}
		}
		
		return "";
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFce(Fce fce) throws Exception {
		fceService.salvarFce(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceComFlushEClear(Fce fce) throws Exception {
		fceService.salvarFceComFlushEClear(fce);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFce(Fce fce) throws Exception {
		fceService.excluirFce(fce);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Object retornarFce(Fce fce) throws Exception {
		DocumentoObrigatorio documentoObrigatorio = fce.getIdeDocumentoObrigatorio();
		if(isFceAgrossilvoPastoril(documentoObrigatorio)){
			return fceAgrossilvoPastorilService.buscarFceAgrossilvopastorilByIdFce(fce);
		}
		else if(isFceAsv(documentoObrigatorio)){
			return fceAsvService.buscarFceAsvByIdeFce(fce);
		}
		else if(isFceBarragem(documentoObrigatorio)){
			//return fceIntervencaoBarragemService.listarFceIntervencaoBarragemByIdeFce(fce);
			return fceBarragemService.buscarFcebarragem(fce);
		}
		else if(isFceOutorgaAbastecimentoIndustrial(documentoObrigatorio)){
			return fceAbastecimentoIndustrialService.buscarFceAbsIndustrialByIdeFce(fce);
		}
		else if(isFceOutorgaCaptacaoSubterranea(documentoObrigatorio)){
			return fceCaptacaoSubterraneaService.buscarFceCaptacaoSubterranea(fce);
		}
		else if(isFceOutorgaCaptacaoSuperficial(documentoObrigatorio)){
			return fceCaptacaoSuperficialService.listarFceCaptacaoSuperficial(fce);
		}
		else if(isFceOutorgaDessedentacaoAnimal(documentoObrigatorio)){
			return fceDessedentacaoAnimalService.buscarFceDesAnimalByIdeFce(fce);
		}
		else if(isFceOutorgaIrrigacao(documentoObrigatorio)){
			return fceIrrigacaoService.buscarFceIrrigacaoByIdeFce(fce);
		}
		else if(isFceOutorgaLancamentoEfluentes(documentoObrigatorio)){
			return fceLancamentoEfluentesService.listarFceLancamentoEfluentesByFce(fce);
		}
		else if(isFceOutorgaPulverizacao(documentoObrigatorio)){
			return fcePulverizacaoService.buscarFcePulverizacaoByIdeFce(fce);
		}
		else if(isFceIndustria(documentoObrigatorio)){
			return fceIndustriaService.buscarFceIndustriaByIdeFce(fce);
		}
		else if(isFcePerfuracaoPoco(documentoObrigatorio)){
			return !Util.isNullOuVazio(fcePerfuracaoPocoService.listarFcePerfuracaoPocoByIdeFce(fce)) || !Util.isNull(documentoObrigatorioRequerimentoService.buscarDocObrigatorioRequerimentoByRequerimentoAndIdeDocumentoObrigatorioEnum(fce.getIdeRequerimento(), DocumentoObrigatorioEnum.FCE_LOCALIZACAO_GEOGRAFICA_DADOS_ADICIONAIS.getId()));
		}
		else if(isFceAbastecimentoHumano(documentoObrigatorio)){
			return fceAbastecimentoHumanoService.buscarFceAbsHumanoByIdeFce(fce);
		}
		else if(isFceOutorgaAquicultura(documentoObrigatorio)){
			return fceAquiculturaService.listarFceAquiculturaByIdeFce(fce);
		}
		else if(isFceTurismo(documentoObrigatorio)){
			return fceTurismoService.buscarFceTurismoByIdeFce(fce);
		}
		else if(isFceLicenciamentoAquicultura(documentoObrigatorio)){
			return verificarFceLicencaAquicultura(fce);
		}
		else if(isFceLicenciamentoMineracao(documentoObrigatorio)){
			return fceLicenciamentoMineracaoFacade.buscarFceLicenciamentoMineralBy(fce);
		}
		else if(isFceAutorizacaoMineracao(documentoObrigatorio)){
			return fceAutorizacaoMineracaoFacade.getFcePesquisaMineralBy(fce);
		}
		else if(isFceInfraestrutura(documentoObrigatorio)){
			return fceInfraestruturaFacade.buscarFceOutorgaInfraestruturaPorFce(fce);
		}
		else if(isFceCanais(documentoObrigatorio)){
			return fceCanalServiceFacade.getFceCanal(fce);
		}
		else if(isFceSistemaAbastecimentoAgua(documentoObrigatorio)){
			return fceSaaFacade.buscarFceSaaByIdeFce(fce);
		}
		else if(isFceSES(documentoObrigatorio)){
			return fceSesFacade.buscarFceSesByIdeFce(fce);
		}
		else if(isFceLinhaEnergiaTrasmissaoEnergia(documentoObrigatorio)){
			return fceLinhaEnergiaService.buscarFceLinhaEnergiaPorFce(fce);
		}
		else if(FceUtil.isFceGeracaoEnergia(documentoObrigatorio)){
			return fceGeracaoEnergiaFacade.getFceGeracaoEnergiaBy(fce);
		}
		else if(isFceIntervencaoMineracao(documentoObrigatorio)){
			return fceIntervencaoMineracaoFacade.getFceIntervencaoMineracao(fce);
		}
		else if (isFceFlorestal(documentoObrigatorio)) {
			FceFlorestalAbstractService fceFlorestalAbstractService = fceFlorestalServiceFactory.getInstance(fce);
			return fceFlorestalAbstractService.buscarFceFlorestal(fce);
		}
		return null;
	}

	/**
	 * Método que verifica se o <b>FCE - Licenciamento para Aquicultura</b> tem suas listas de {@link FceAquiculturaLicencaTipoAtividade} devidamente preenchidas.
	 * Verifica-se as listas pois é possível editar o <b>FCE - Outorga para Aquicultura</b> e consequentemente modificar a lista exibida no <b>FCE - Licenciamento para Aquicultura</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fce
	 * @return {@code fceAquiculturaLicenca} or {@code null}
	 * @throws Exception
	 * @since 03/07/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private FceAquiculturaLicenca verificarFceLicencaAquicultura(Fce fce) throws Exception {
		FceAquiculturaLicenca fceAquiculturaLicenca = fceAquiculturaLicencaServiceFacade.buscarFceAquiculturaLicencaByFce(fce);
		if(!Util.isNullOuVazio(fceAquiculturaLicenca)){
			if(fceAquiculturaLicenca.getIndAquiculturaViveiroEscavado() && Util.isNullOuVazio(fceAquiculturaLicencaServiceFacade.listarFceAquiculturaLicencaTipoAtividadeToViveiroEscavadoBy(fce.getIdeRequerimento()))){
				return null;
			}
			if(fceAquiculturaLicenca.getIndAquiculturaTanqueRede() && Util.isNullOuVazio(fceAquiculturaLicencaServiceFacade.listarFceAquiculturaLicencaTipoAtividadeToTanqueRedeBy(fce.getIdeRequerimento()))){
				return null;
			}
		}
		return fceAquiculturaLicenca;
	}
	
	/**
	 * Método que vai retornar o {@link Funcionario} (com {@link Perfil} de Técnico) através do {@link Usuario} logado para registrar o {@link Fce} da {@link AnaliseTecnica}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 05/01/2016
	 * @see adicionar o #ticket
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Funcionario obterTecnico(PessoaFisica idePessoaFisica){
		return funcionarioService.buscarFuncionarioPorPessoaFisica(idePessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void apagarFceTecnicoByFce(String sql, Fce fce) throws Exception {
		fceService.apagarFceByFunction(sql, fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Fce> listarFce(Integer ideProcesso, DadoOrigemEnum...listDadoOrigem) throws Exception {
		return fceService.listarFcePor(ideProcesso, listDadoOrigem);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Fce> listarFceDoProcessoReenquadramento(Integer ideProcesso) throws Exception {
		return fceService.listarFcePorProcessoReenquadramento(ideProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<Fce> listarFce(AnaliseTecnica analiseTecnica) throws Exception {
		return fceService.listarFcePor(analiseTecnica, null);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Fce> listarFceParaASV(AnaliseTecnica analiseTecnica, Boolean indAprovado) throws Exception {
		return fceService.listarFceParaASV(analiseTecnica, null);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Fce> listarFceComExcluido(AnaliseTecnica analiseTecnica) throws Exception {
		return fceService.listarFceComExcluido(analiseTecnica, null);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Fce> listarFceAprovadoNaAnaliseTecnicaBy(Integer ideProcesso) throws Exception {
		AnaliseTecnica analiseTecnica = analiseTecnicaServiceFacade.buscarAnaliseTecnica(ideProcesso);
		if(!Util.isNull(analiseTecnica)){
			return fceService.listarFcePor(analiseTecnica, true);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DadoOrigemEnum ajustarFceParaReenquadramento(Requerimento requerimento, Fce fce, AnaliseTecnica analiseTecnica) throws Exception{
		DadoOrigemEnum dadoOrigemEnum = null;
		
		ProcessoReenquadramento processoReenquadramento = processoReenquadramentoDAOImpl.processoReenquadramentoPorRequerimento(requerimento);
		
		if (Util.isNull(analiseTecnica) || fce.isFceTecnico()){
			if (!Util.isNullOuVazio(processoReenquadramento)){
				dadoOrigemEnum = DadoOrigemEnum.REENQUADRAMENTO;
			} else {
				dadoOrigemEnum = DadoOrigemEnum.REQUERIMENTO;
			}
			
			if (!Util.isNull(fce) && !Util.isNull(fce.getIdeDadoOrigem())){
				fce.getIdeDadoOrigem().setIdeDadoOrigem(dadoOrigemEnum.getId());
				fce.setIdeProcessoReenquadramento(processoReenquadramento);
			}
		}
		
		return dadoOrigemEnum;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void adicionarProcessoReenquadramento(Requerimento requerimento, Fce fce) throws Exception {
		ProcessoReenquadramento processoReenquadramento = processoReenquadramentoDAOImpl.processoReenquadramentoPorRequerimento(requerimento);
		if (!Util.isNull(fce) && !Util.isNull(fce.getIdeDadoOrigem())){
			fce.setIdeProcessoReenquadramento(processoReenquadramento);
		}
	}
	public Fce buscarFcePorIdeFce(Integer ideFce) throws Exception {
		return fceService.buscarFcePorIdeFce(ideFce);
	}	
}