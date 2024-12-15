package br.gov.ba.seia.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.FceOutorgaAquiculturaBarragemController;
import br.gov.ba.seia.controller.FceOutorgaAquiculturaCaptacaoController;
import br.gov.ba.seia.controller.FceOutorgaAquiculturaLancamentoController;
import br.gov.ba.seia.controller.FceOutorgaAquiculturaRioController;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.CaracteristicaEfluente;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EspecieAquiculturaTipoAtividade;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.FceAquiculturaEspecie;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceLancamentoEfluente;
import br.gov.ba.seia.entity.FceLancamentoEfluenteCaracteristica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoAquicultura;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ReservaAgua;
import br.gov.ba.seia.entity.TipoAquicultura;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.service.AquiculturaTipoAtividadeService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EspecieAquiculturaTipoAtividadeService;
import br.gov.ba.seia.service.FceAquiculturaEspecieService;
import br.gov.ba.seia.service.FceAquiculturaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.FceAquiculturaService;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoAquiculturaService;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceOutorgaAquiculturaServiceFacade {

	@EJB
	private AquiculturaTipoAtividadeService atividadeService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private EspecieAquiculturaTipoAtividadeService especieAquiculturaTipoAtividadeService;
	@EJB
	private FceServiceFacade fceServiceFacade;
	@EJB
	private FceAquiculturaEspecieService fceAquiculturaEspecieService;
	@EJB
	private FceAquiculturaLocalizacaoGeograficaService fceAquiculturaLocalizacaoGeograficaService;
	@EJB
	private FceAquiculturaService fceAquiculturaService;
	@EJB
	private FceOutorgaLocalizacaoAquiculturaService fceOutorgaLocalizacaoAquiculturaService;
	@EJB
	private FceOutorgaLocalizacaoGeograficaService fceOutorgaLocGeoService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService; 	// #6590
	@EJB
	private FceLancamentoEfluentesServiceFacade lancamentoEfluenteServiceFacade;
	@EJB
	private FceLicenciamentoAquiculturaServiceFacade fceLicenciamentoAquiculturaServiceFacade;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal buscarVazaoRequeridaInCaptacao(Requerimento requerimento) throws Exception {
		return fceAquiculturaService.buscarVazaoByRequerimentoAndTipoAquicultura(requerimento, TipoAquiculturaEnum.CAPTACAO).getNumVazaoRequerida();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal buscarVazaoRequeridaInLancamento(Requerimento requerimento) throws Exception {
		return fceAquiculturaService.buscarVazaoByRequerimentoAndTipoAquicultura(requerimento, TipoAquiculturaEnum.LANCAMENTO).getIdeFceLancamentoEfluente().getNumVazaoEfluente();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isFceLicenciamentoAquiculturaPreenchido(Requerimento requerimento) throws Exception{
		return !Util.isNullOuVazio(fceServiceFacade.buscarFceByIdeRequerimentoDocumentoObrigatorioAndOrigemFce(requerimento, new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_LICENCIAMENTO_AQUICULTURA.getId()), DadoOrigemEnum.REQUERIMENTO));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean isIntervencaoBarragem(Integer ideRequerimento, OutorgaLocalizacaoGeografica olg) throws Exception {
		PerguntaRequerimento perguntaIntervencaoBarragem = perguntaRequerimentoService.consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimentoAndIdeOutorgaLocGeo("NR_A4_DINTERV_P42", ideRequerimento, olg.getIdeOutorgaLocalizacaoGeografica());
		if(!Util.isNull(perguntaIntervencaoBarragem)){
			return perguntaIntervencaoBarragem.getIndResposta();
		}
		else {
			return null;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscarEmpreendimentoBy(Requerimento requerimento) throws Exception {
		return empreendimentoService.carregarByIdeRequerimento(requerimento.getIdeRequerimento());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EspecieAquiculturaTipoAtividade buscarEspecieAquiculturaTipoAtividadeBy(EspecieAquiculturaTipoAtividade especieAquiculturaTipoAtividade) throws Exception {
		return especieAquiculturaTipoAtividadeService.buscarEspecieAquiculturaTipoAtividadeByIdeEspecieAquiculturaTipoAtividade(especieAquiculturaTipoAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceAquicultura buscarFceAquiculturaBy(Fce fce, TipoAquiculturaEnum aquiculturaEnum) throws Exception {
		FceAquicultura fceAquicultura = fceAquiculturaService.buscarFceAquiculturaByIdeFceAndTipoAquicultura(fce, aquiculturaEnum);
		if(!Util.isNull(fceAquicultura) && !Util.isNull(fceAquicultura.getIdeDocumentoObrigatorioRequerimento())){
			fceAquicultura.setUploadCaminhoArquivo(new ArrayList<String>());
			fceAquicultura.getUploadCaminhoArquivo().add(fceAquicultura.getIdeDocumentoObrigatorioRequerimento().getDscCaminhoArquivo());
		}
		return fceAquicultura;

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceAquicultura buscarFceAquiculturaRequerenteBy(Requerimento req, TipoAquiculturaEnum aquiculturaEnum) throws Exception {
		FceAquicultura fceAquicultura = fceAquiculturaService.buscarFceAquiculturaByIdeFceAndTipoAquicultura(req, aquiculturaEnum);
		if(!Util.isNull(fceAquicultura) && !Util.isNull(fceAquicultura.getIdeDocumentoObrigatorioRequerimento())){
			fceAquicultura.setUploadCaminhoArquivo(new ArrayList<String>());
			fceAquicultura.getUploadCaminhoArquivo().add(fceAquicultura.getIdeDocumentoObrigatorioRequerimento().getDscCaminhoArquivo());
		}
		return fceAquicultura;
		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AquiculturaTipoAtividade> listarAquiculturaTipoAtividade() throws Exception {
		return atividadeService.listarTipoAtividadeByIndAtivo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AquiculturaTipoAtividade> listarAquiculturatipoAtividadeBy(List<AquiculturaTipoAtividadeEnum> aquiculturaTipoAtividadeEnum) throws Exception {
		return atividadeService.listarTipoAtividadeByIde(aquiculturaTipoAtividadeEnum);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieAquiculturaTipoAtividade> listarEspecieAquiculturaTivoAtividadeBy(AquiculturaTipoAtividade tipoAtividade) throws Exception {
		return especieAquiculturaTipoAtividadeService.listarEspecieAquiculturaTipoAtividadeByIdeAquiculturaTipoAtividade(tipoAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquicultura> listarFceAquiculturaByIdeFce(Fce fce) throws Exception {
		return fceAquiculturaService.listarFceAquiculturaByIdeFce(fce);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaEspecie> listarFceAquiculturaEspecieBy(FceAquicultura fceAquicultura) throws Exception {
		return fceAquiculturaEspecieService.listarFceAquiculturaByIdeFceAquicultura(fceAquicultura);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLocalizacaoGeografica> listarFceAquiculturaLocalizacaoGeograficaBy(FceAquicultura fceAquicultura) throws Exception {
		return fceAquiculturaLocalizacaoGeograficaService.listarFceAquiculturaLocalizacaoGeograficaByIdeFceAquicultura(fceAquicultura);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoAquicultura> listarFceOutorgaLocalizacaoAquiculturaBy(FceAquicultura fceAquicultura) throws Exception {
		return fceOutorgaLocalizacaoAquiculturaService.listarFceOutorgaLocalizacaoAquiculturaByIdeFceAquicultura(fceAquicultura);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tipologia> listarTipologiasDoRequerimento(Requerimento requerimento) throws Exception {
		return (List<Tipologia>) requerimentoTipologiaService.buscarTipologias(requerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoPeriodoDerivacao> listarTipoPeriodoDerivacao() throws Exception {
		return lancamentoEfluenteServiceFacade.listarTipoPeriodoDerivacao();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaEfluente> listarCaracteristicaEfluentes() throws Exception {
		return lancamentoEfluenteServiceFacade.listarCaracteristicaEfluentes();
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarFceLancamentoEfluenteToFceAquicultura(Map<String, Object> parametros) throws Exception {
		Fce fce = (Fce) parametros.get("fce");
		fce.setIndConcluido(true);
		salvarFce(fce);
		List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLocalizacaoGeografica = (List<FceOutorgaLocalizacaoGeografica>) parametros.get("listaFceOutorgaLocalizacaoGeografica");
		List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLocalizacaoGeograficaFce;
		lancamentoEfluenteServiceFacade.salvarFceLancamentoEfluentes((FceLancamentoEfluente) parametros.get("fceLancamentoEfluente"));
		for(FceOutorgaLocalizacaoGeografica item : listaFceOutorgaLocalizacaoGeografica) {
			listaFceOutorgaLocalizacaoGeograficaFce = new ArrayList<FceOutorgaLocalizacaoGeografica>();
			for(FceOutorgaLocalizacaoGeografica folg : item.getIdeFce().getFceOutorgaLocalizacaoGeografica()) {
				if(!listaFceOutorgaLocalizacaoGeograficaFce.contains(folg)) {
					listaFceOutorgaLocalizacaoGeograficaFce.add(folg);
				}
				if(Util.isNullOuVazio(folg.getListaReservaAgua())) {
					folg.setListaReservaAgua(new ArrayList<ReservaAgua>());
				}
			}
			item.getIdeFce().setFceOutorgaLocalizacaoGeografica(listaFceOutorgaLocalizacaoGeograficaFce);
		}
		salvarListaFceOutorgaLocalizacaoGeografica(listaFceOutorgaLocalizacaoGeografica);
		salvarFceAquicultura((FceAquicultura) parametros.get("fceAquicultura"));
		salvarListaFceOutorgaLocalizacaoAquicultura((List<FceOutorgaLocalizacaoAquicultura>) parametros.get("listaFceOutorgaLocalizacaoAquicultura"));
		lancamentoEfluenteServiceFacade.salvarListaFceLancamentoEfluentesCaracteristica((List<FceLancamentoEfluenteCaracteristica>) parametros.get("listaFceLancamentoEfluenteCaracteristica"));
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarFceAquicultura(Map<String, Object> parametros) throws Exception {

		Fce fce = (Fce) parametros.get("fce");
		FceAquicultura fceAquicultura = (FceAquicultura) parametros.get("fceAquicultura");
		List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLocalizacaoGeografica = (List<FceOutorgaLocalizacaoGeografica>) parametros.get("listaFceOutorgaLocalizacaoGeografica");
		List<FceOutorgaLocalizacaoAquicultura> listaFceOutorgaLocalizacaoAquicultura =(List<FceOutorgaLocalizacaoAquicultura>) parametros.get("listaFceOutorgaLocalizacaoAquicultura");
		List<FceAquiculturaLocalizacaoGeografica> listaFceAquiculturaLocalizacaoGeografica = (List<FceAquiculturaLocalizacaoGeografica>) parametros.get("listaFceAquiculturaLocalizacaoGeografica");
		List<FceAquiculturaEspecie> listaFceAquiculturaEspecie = (List<FceAquiculturaEspecie>) parametros.get("listaFceAquiculturaEspecie");
		salvarFce(fce);
		fceAquicultura.setIdeFce(fce);
//		fce.setIndConcluido(true);

		salvarFceAquicultura(fceAquicultura);

		if(!Util.isNullOuVazio(listaFceOutorgaLocalizacaoGeografica)) {
			salvarListaFceOutorgaLocalizacaoGeografica(listaFceOutorgaLocalizacaoGeografica);
		}

		if(!Util.isNullOuVazio(listaFceOutorgaLocalizacaoAquicultura)) {
			salvarListaFceOutorgaLocalizacaoAquicultura(listaFceOutorgaLocalizacaoAquicultura);
		}

		if(!Util.isNullOuVazio(listaFceAquiculturaLocalizacaoGeografica)) {
			salvarListaFceAquiculturaLocalizacaoGeografica(listaFceAquiculturaLocalizacaoGeografica);
		}

		if(!Util.isNullOuVazio(listaFceAquiculturaEspecie)) {
			salvarListaFceAquiculturaEspecie(listaFceAquiculturaEspecie);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void verificarAndRemoverEspeciesDeFceLicencaAquicultura(TipoAquicultura tipoAquicultura, List<FceAquiculturaEspecie> listaFceAquiculturaEspecie, Requerimento requerimento, List<AquiculturaTipoAtividade> listaAquiculturaTipoAtividade) throws Exception{
		if(tipoAquicultura.getIdeTipoAquiculturaPai().getIdeTipoAquicultura().equals(TipoAquiculturaEnum.TANQUE_REDE.getId())){
			listaFceAquiculturaEspecie = fceLicenciamentoAquiculturaServiceFacade.listarDistinctFceAquiculturaEspecieToTanqueRedeBy(requerimento);
		}
		List<FceAquiculturaLicencaTipoAtividade> listaFceAquiculturaLicencaTipoAtividade = listarFceAquiculturaLicencaTipoAtividadeBy(tipoAquicultura, requerimento);
		if(!Util.isNullOuVazio(listaFceAquiculturaLicencaTipoAtividade)){
			for(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade : listaFceAquiculturaLicencaTipoAtividade){
				removerEspeciesEmLicenca(listaFceAquiculturaEspecie, requerimento, fceAquiculturaLicencaTipoAtividade);
				for(AquiculturaTipoAtividade atividade : listaAquiculturaTipoAtividade){
					if(!atividade.isSelecionado() && fceAquiculturaLicencaTipoAtividade.getIdeEspecieAquiculturaTipoAtividade().getIdeAquiculturaTipoAtividade().equals(atividade)){
						excluirAtividadeInLicenca(fceAquiculturaLicencaTipoAtividade);	
					}
				}
			}
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param listaEspecieSendoSalva
	 * @param fceAquiculturaLicencaTipoAtividade
	 * @param especieUsada
	 * @throws Exception
	 * @since 06/07/2015
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void removerEspeciesEmLicenca(List<FceAquiculturaEspecie> listaFceAquiculturaEspecie, Requerimento requerimento, FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception {
		boolean especieUsada = false;
		for(FceAquiculturaEspecie especie : listaFceAquiculturaEspecie){
			if(fceAquiculturaLicencaTipoAtividade.getIdeEspecieAquiculturaTipoAtividade().getIdeEspecieAquiculturaTipoAtividade().equals(especie.getIdeEspecieAquiculturaTipoAtividade().getIdeEspecieAquiculturaTipoAtividade())){
				especieUsada = true;
				break;
			}
		}
		if(!especieUsada){
			excluirFceAquiculturaLicencaTipoAtividade(fceAquiculturaLicencaTipoAtividade);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<FceAquiculturaLicencaTipoAtividade> listarFceAquiculturaLicencaTipoAtividadeBy(TipoAquicultura tipoAquicultura, Requerimento requerimento) throws Exception{
		if(TipoAquiculturaEnum.VIVEIRO_ESCAVADO.getId().equals(tipoAquicultura.getIdeTipoAquiculturaPai().getIdeTipoAquicultura())){
			return fceLicenciamentoAquiculturaServiceFacade.listarFceAquiculturaLicencaTipoAtividadeToViveiroEscavadoBy(requerimento);
		} else if(TipoAquiculturaEnum.TANQUE_REDE.getId().equals(tipoAquicultura.getIdeTipoAquiculturaPai().getIdeTipoAquicultura())){
			return fceLicenciamentoAquiculturaServiceFacade.listarFceAquiculturaLicencaTipoAtividadeToTanqueRedeBy(requerimento);
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<EspecieAquiculturaTipoAtividade> listarEspecieAquiculturaTipoAtividadeInOutorga(Requerimento requerimento) throws Exception {
		return especieAquiculturaTipoAtividadeService.listarFceAquiculturaEspecieByRequerimentoAntTipoAquicultura(requerimento, TipoAquiculturaEnum.TANQUE_REDE);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<EspecieAquiculturaTipoAtividade> listarEspecieAquiculturaTipoAtividadeInLicenca(Requerimento requerimento) throws Exception{
		return especieAquiculturaTipoAtividadeService.listarFceAquiculturaEspecieByRequerimentoAntTipoAquicultura(requerimento);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquiculturaLicencaTipoAtividade
	 * @throws Exception
	 * @since 03/07/2015
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirFceAquiculturaLicencaTipoAtividade(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception {
		fceLicenciamentoAquiculturaServiceFacade.excluirCaracterizacaoCultivo(fceAquiculturaLicencaTipoAtividade);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirAtividadeInLicenca(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception{
		FceAquiculturaLicenca fceAquiculturaLicenca = fceAquiculturaLicencaTipoAtividade.getIdeFceAquiculturaLicenca();
		TipoAquicultura tipoAquicultura = fceAquiculturaLicencaTipoAtividade.getIdeTipoAquicultura();
		AquiculturaTipoAtividade aquiculturaTipoAtividade = fceAquiculturaLicencaTipoAtividade.getIdeEspecieAquiculturaTipoAtividade().getIdeAquiculturaTipoAtividade();		
		fceLicenciamentoAquiculturaServiceFacade.excluirListaPoligonalCultivoByAtividade(fceAquiculturaLicenca, tipoAquicultura, aquiculturaTipoAtividade);
		fceLicenciamentoAquiculturaServiceFacade.excluirListaFceAquiculturaLicencaTipoLocalizacaoByAtividade(fceAquiculturaLicenca, aquiculturaTipoAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFce(Fce fce) throws Exception {
		fceServiceFacade.salvarFceComFlushEClear(fce);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceAquicultura(FceAquicultura fceAquicultura) throws Exception {
		fceAquiculturaService.salvarFceAquicultura(fceAquicultura);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceOutorgaLocalizacaoGeografica(List<FceOutorgaLocalizacaoGeografica> listaFceOuorgaLocalizacaoGeografica) throws Exception{
		fceOutorgaLocGeoService.salvarListaFceOutorgaLocGeo(listaFceOuorgaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAquiculturaEspecie(List<FceAquiculturaEspecie> listaFceAquiculturaEspecies) throws Exception{
		fceAquiculturaEspecieService.salvarListaFceAquicultura(listaFceAquiculturaEspecies);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAquiculturaLocalizacaoGeografica(List<FceAquiculturaLocalizacaoGeografica> listaFceAquiculturaLocalizacaoGeografica) throws Exception {
		if(!Util.isNull(listaFceAquiculturaLocalizacaoGeografica)){
			fceAquiculturaLocalizacaoGeograficaService.salvarListaFceAquiculturaLocalizacaoGeografica(listaFceAquiculturaLocalizacaoGeografica);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceOutorgaLocalizacaoAquicultura(List<FceOutorgaLocalizacaoAquicultura> listaFceOutorgaLocalizacaoAquicultura) throws Exception{
		fceOutorgaLocalizacaoAquiculturaService.salvarListaFceOutorgaLocalizacaoAquicultura(listaFceOutorgaLocalizacaoAquicultura);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListFceAquiculturaEspecieBy(FceAquicultura fceAquicultura) throws Exception {
		fceAquiculturaEspecieService.excluirFceAquiculturaEspecieByIdeFceAquicultura(fceAquicultura);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarLancamento(FceOutorgaAquiculturaLancamentoController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarBarragem(FceOutorgaAquiculturaBarragemController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarRio(FceOutorgaAquiculturaRioController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarCaptacao(FceOutorgaAquiculturaCaptacaoController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
}