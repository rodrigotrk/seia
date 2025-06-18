package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.DadoOrigem;
import br.gov.ba.seia.entity.DestinoResiduo;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLinhaEnergia;
import br.gov.ba.seia.entity.FceLinhaEnergiaDestinoResiduo;
import br.gov.ba.seia.entity.FceLinhaEnergiaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceLinhaEnergiaResiduoGerado;
import br.gov.ba.seia.entity.FceLinhaEnergiaTipoEnergia;
import br.gov.ba.seia.entity.FceLinhaEnergiaTipoSubestacao;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.TipoAreaConcedida;
import br.gov.ba.seia.entity.TipoEnergia;
import br.gov.ba.seia.entity.TipoResiduoGerado;
import br.gov.ba.seia.entity.TipoSubestacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.DestinoResiduoEnum;
import br.gov.ba.seia.enumerator.TipoAreaConcedidaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.enumerator.ValidacaoShapeEnum;
import br.gov.ba.seia.facade.FceLinhaTransmissaoDistribuicaoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

@Named("fceLinhaTransmissaoDistribruicaoController")
@ViewScoped
public class FceLinhaTransmissaoDistribuicaoController extends FceController {
	
	@EJB
	private FceLinhaTransmissaoDistribuicaoFacade fceFacade;
	
	private Boolean isSubestacao;
	
	private Boolean editavel;
	
	private Boolean desabilitado;
	
	private Boolean isVisivel;
	
	private Boolean isOutros = Boolean.FALSE;
	
	private Boolean isDeleteAlternativa = Boolean.FALSE;
	
	private ProcessoAtoConcedido processoAtoConcedido;
	
	private ProcessoAto ideProcessoAto;
	
	private FceLinhaEnergia fceLinhaEnergia;
	
	private StreamedContent imprimirRelatorioFce;
	
	private List<TipoEnergia> listaTipoEnergia;
	
	private TipoSubestacao tipoSubestacao;
	
	private TipoEnergia tipoEnergia;
	
	private TipoResiduoGerado tipoResiduoGerado;
	
	private DestinoResiduo destinoResiduo;
	
	private FceLinhaEnergiaTipoSubestacao fceLinhaEnergiaTipoSubestacao;
	
	private FceLinhaEnergiaLocalizacaoGeografica fceLinhaEnergiaLocalizacaoGeograficaAlternativa;
	
	private FceLinhaEnergiaResiduoGerado fceLinhaEnergiaResiduoGerado;
	
	private FceLinhaEnergiaDestinoResiduo fceLinhaEnergiaDestinoResiduo;
	
	private FceLinhaEnergiaLocalizacaoGeografica fceLinhaEnergiaLocalizacaoGeograficaPoligonalNotificacao;
	
	private DocumentoObrigatorio documentoObrigatorio;
	
	private Empreendimento empreendimento;
	
	private List<TipoResiduoGerado> listaTipoResiduoGerado;
	
	private List<DestinoResiduo> listaDestinoResiduo;
	
	private List<TipoResiduoGerado> listaSelecionadaTipoResiduoGerado;
	
	private List<DestinoResiduo> listaSelecionadaDestinoResiduo;
	
	private List<TipoSubestacao> listaTipoSubestacao;
	
	private List<FceLinhaEnergiaTipoSubestacao> listaFceLinhaEnergiaTipoSubestacao;
	
	private List<FceLinhaEnergiaLocalizacaoGeografica> listaFceLinhaEnergiaLocalizacaoGeografica;
	
	private List<FceLinhaEnergiaLocalizacaoGeografica> listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao;
	
	private List<FceLinhaEnergiaTipoEnergia> listTipoEnergiaSelecionados;
	
	private List<TipoEnergia> listTipoEnergia;
	
	private MetodoUtil metodoNovoFceLinhaEnergiaLocGeoPrincipal;
	
	private MetodoUtil metodoNovoFceLinhaEnergiaLocGeoAlternativa;
	
	private Collection<Tipologia> tipologias;
	
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;
	
	public void init() {
		try {
			editavel = false;
			
			tipoEnergia = new TipoEnergia();
			listTipoEnergia = new ArrayList<TipoEnergia>();
			tipoResiduoGerado = new TipoResiduoGerado();
			destinoResiduo = new DestinoResiduo();
			
			fceLinhaEnergia = new FceLinhaEnergia();
			fceLinhaEnergiaTipoSubestacao = new FceLinhaEnergiaTipoSubestacao();
			fceLinhaEnergiaResiduoGerado = new FceLinhaEnergiaResiduoGerado(fceLinhaEnergia, null);
			fceLinhaEnergiaDestinoResiduo = new FceLinhaEnergiaDestinoResiduo(fceLinhaEnergia, null);
			fceLinhaEnergiaLocalizacaoGeograficaAlternativa = new FceLinhaEnergiaLocalizacaoGeografica(fceLinhaEnergia);
			
			listaFceLinhaEnergiaTipoSubestacao = new ArrayList<FceLinhaEnergiaTipoSubestacao>();
			
			metodoNovoFceLinhaEnergiaLocGeoPrincipal = new MetodoUtil(this, this.getClass().getDeclaredMethod("calcularAreaPoligonalPreferencial"));
			metodoNovoFceLinhaEnergiaLocGeoAlternativa = new MetodoUtil(this, this.getClass().getDeclaredMethod("calcularAreaPoligonalAlternativa"));
			
			if (!Util.isNullOuVazio(documentoObrigatorio)) {
				carregarFceDoRequerente(documentoObrigatorio);
			}
			
			listaTipoEnergia = fceFacade.buscarTiposEnergias();
			listaTipoResiduoGerado = fceFacade.buscarListaTipoResiduoGerado();
			listaDestinoResiduo = fceFacade.buscarListaDestinoResiduo();
			
			if (!Util.isNullOuVazio(this.listaDestinoResiduo)) {
				listaDestinoResiduo.remove(new DestinoResiduo(DestinoResiduoEnum.PILHA_ESTERIL.getId(), "Pilha de Estéril"));
			}
			
			empreendimento = fceFacade.buscarEmpreendimentoPorRequerimento(requerimento.getIdeRequerimento());
			
			tipologias = requerimentoTipologiaService.buscarTipologias(requerimento);
			
			for(Tipologia tipologia: tipologias) {
				fceLinhaEnergia.setValExtensaoTotalLinha(
						BigDecimal.valueOf(
								fceFacade.buscarValorAtividadeByRequerimentoAndTipologia(
										requerimento.getIdeRequerimento(), tipologia.getIdeTipologia())));				
			}
			
			if (!Util.isNullOuVazio(fce)) {
				carregarFceLinhaEnergia();
				
				if (!Util.isNullOuVazio(fce.getIdeAnaliseTecnica())) {
					isVisivel = true;
				} else {
					desabilitado = true;
					isVisivel = false;
				}
				
				editavel = true;
				
			} else {
				iniciarFce(documentoObrigatorio);
				fceLinhaEnergia.setIdeFce(fce);
				desabilitado = true;
			}
			
			fceLinhaEnergiaLocalizacaoGeograficaAlternativa = new FceLinhaEnergiaLocalizacaoGeografica(fceLinhaEnergia);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void calcularAreaPoligonalPreferencial() {
		try {
			LocalizacaoGeografica localizacaoGeografica = 
					this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().getIdeLocalizacaoGeografica();
			
			if (isPoligonalObjetoLicencaPreenchida()) {
				this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setListaMunicipio(
						fceFacade.listarMunicipios(localizacaoGeografica));
				
				this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setIdeLocalizacaoGeografica(
						fceFacade.obterValAreaByTheGemo(localizacaoGeografica));
			} else {
				this.fceLinhaEnergia.setFceLinhaEnergiaLocalizacaoGeograficaPreferencial(new FceLinhaEnergiaLocalizacaoGeografica());
				this.fceLinhaEnergia.setIndAlternativaLocacional(null);
				this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setIndObjetoConcedido(false);
				this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setIdeFceLinhaEnergia(fceLinhaEnergia);
				
				if (!Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection())) {
					fceLinhaEnergia.setIndAlternativaLocacional(true);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void calcularAreaPoligonalAlternativa() {
		try {
			if (isPoligonalPreenchida(this.fceLinhaEnergiaLocalizacaoGeograficaAlternativa.getIdeLocalizacaoGeografica())) {
				
				this.fceLinhaEnergiaLocalizacaoGeograficaAlternativa.setIdeLocalizacaoGeografica(
						fceFacade.obterValAreaByTheGemo(this.fceLinhaEnergiaLocalizacaoGeograficaAlternativa.getIdeLocalizacaoGeografica()));
				
				this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection().add(fceLinhaEnergiaLocalizacaoGeograficaAlternativa);
			}
			
			this.fceLinhaEnergiaLocalizacaoGeograficaAlternativa = new FceLinhaEnergiaLocalizacaoGeografica(fceLinhaEnergia);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isPoligonalObjetoLicencaPreenchida() {
		try {
			return isPoligonalPreenchida(getFceLinhaEnergia().getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().getIdeLocalizacaoGeografica());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean isPoligonalPreenchida(LocalizacaoGeografica localizacaoGeografica) {
		try {
			return !Util.isNullOuVazio(fceFacade.obterTheGeom(localizacaoGeografica));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	public void finalizar() {
		try {
			fceFacade.finalizar(this);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	public void abrirDialog() {
		if (!super.fce.isFceTecnico()) {
			carregarFceDoRequerente(fce.getIdeDocumentoObrigatorio());
		}
		
		init();
		
		Processo ideProcesso = null;
		
		try {
			ideProcesso = fceFacade.buscarPorRequerimento(this.requerimento.getIdeRequerimento());
			
			List<ArquivoProcesso> listaArquivoProcesso = 
					(List<ArquivoProcesso>) fceFacade.listaArquivoProcessoPorIdeProcesso(requerimento.getIdeRequerimento());
			
			listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao = new ArrayList<FceLinhaEnergiaLocalizacaoGeografica>();
			
			for (ArquivoProcesso arqP : listaArquivoProcesso) {
				
				if (arqP.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeograficaNotNull()) {
					fceLinhaEnergiaLocalizacaoGeograficaPoligonalNotificacao = new FceLinhaEnergiaLocalizacaoGeografica(fceLinhaEnergia);
					
					fceLinhaEnergiaLocalizacaoGeograficaPoligonalNotificacao.setIdeLocalizacaoGeografica(
							fceFacade.obterValAreaByTheGemo(fceFacade.carregar(arqP.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())));
					
					fceLinhaEnergiaLocalizacaoGeograficaPoligonalNotificacao.setNumeroNotificacao(
							fceFacade.carregarById(arqP.getIdeNotificacao().getIdeNotificacao()).getNumNotificacao());
					
					listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao.add(this.fceLinhaEnergiaLocalizacaoGeograficaPoligonalNotificacao);
				}
			}
			
			if (!Util.isNullOuVazio(ideProcesso)) {
				
				this.setIdeProcessoAto(fceFacade.listarAtosPorProcesso(ideProcesso).get(0));
				
				if (!fceFacade.existeProcessoAtoConcedido(ideProcessoAto.getIdeProcessoAto())) {
					this.processoAtoConcedido = new ProcessoAtoConcedido();
					this.processoAtoConcedido.setIdeProcessoAto(ideProcessoAto);
					this.processoAtoConcedido.setIdeTipoAreaConcedida(new TipoAreaConcedida(TipoAreaConcedidaEnum.OBJETO_DE_LICENCA_LINHA_DE_TRANSMISSAO_E_DISTRIBUICAO));
				
				} else {
					fceFacade.listarProcessoAtoConcedido(ideProcessoAto.getIdeProcessoAto());
					this.processoAtoConcedido = fceFacade.listarProcessoAtoConcedido(ideProcessoAto.getIdeProcessoAto()).get(0);
					
					if (!fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().getIndObjetoConcedido()
							&& !Util.isNullOuVazio(this.listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao)) {
						
						for (FceLinhaEnergiaLocalizacaoGeografica loc : this.listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao) {
							
							if (this.processoAtoConcedido.getIdeLocalizacaoGeografica().equals(loc.getIdeLocalizacaoGeografica())) {
								loc.setIndObjetoConcedido(true);
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		editavel = true;
		desabilitado = false;
		Html.atualizar("fce_linha_trasmissao_distribuicao");
		Html.exibir("fce_linha_trasmissao_distribuicao");
	}
	
	@Override
	protected void prepararDuplicacao() {
		try {
			Fce fceTecnico = this.fce.clone();
			carregarFceDoRequerente(fce.getIdeDocumentoObrigatorio());
			prepararFceTecnico(fceTecnico);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	protected void duplicarFce() {
		try {
			fceFacade.salvarOuAtualizar(fceLinhaEnergia);
			carregarFceDoTecnico(fce.getIdeDocumentoObrigatorio());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void prepararFceTecnico(Fce fceTecnico) throws Exception {
		
		fceTecnico.setIdeDadoOrigem(new DadoOrigem(DadoOrigemEnum.TECNICO.getId()));
		
		FceLinhaEnergia fle = fceFacade.buscarFceLinhaEnergiaPorFce(fce);
		
		fceLinhaEnergia = (FceLinhaEnergia) fle.clone();
		fceLinhaEnergia.setIdeFce(fceTecnico);
		fceLinhaEnergia.setIdeFceLinhaEnergia(null);
		
		List<FceLinhaEnergiaTipoEnergia> listaFLETE = new ArrayList<FceLinhaEnergiaTipoEnergia>();
		
		for (FceLinhaEnergiaTipoEnergia tipoEnergia : fceLinhaEnergia.getFceLinhaEnergiaTipoEnergiaCollection()) {
			FceLinhaEnergiaTipoEnergia flete = (FceLinhaEnergiaTipoEnergia) tipoEnergia.clone();
			flete.setIdeFceLinhaEnergia(fceLinhaEnergia);
			flete.setIdeFceLinhaEnergiaTipoEnergia(null);
			listaFLETE.add(flete);
		}
		
		fceLinhaEnergia.setFceLinhaEnergiaTipoEnergiaCollection(listaFLETE);
		
		List<FceLinhaEnergiaResiduoGerado> listaFLERG = new ArrayList<FceLinhaEnergiaResiduoGerado>();
		
		for (FceLinhaEnergiaResiduoGerado residuo : fceLinhaEnergia.getFceLinhaEnergiaResiduoGeradoCollection()) {
			FceLinhaEnergiaResiduoGerado flerg = (FceLinhaEnergiaResiduoGerado) residuo.clone();
			flerg.setIdeFceLinhaEnergia(fceLinhaEnergia);
			flerg.setIdeFceLinhaEnergiaResiduoGerado(null);
			listaFLERG.add(flerg);
		}
		
		fceLinhaEnergia.setFceLinhaEnergiaResiduoGeradoCollection(listaFLERG);
		
		List<FceLinhaEnergiaDestinoResiduo> listaFLEDR = new ArrayList<FceLinhaEnergiaDestinoResiduo>();
		
		for (FceLinhaEnergiaDestinoResiduo destino : fceLinhaEnergia.getFceLinhaEnergiaDestinoResiduoCollection()) {
			FceLinhaEnergiaDestinoResiduo fledr = (FceLinhaEnergiaDestinoResiduo) destino.clone();
			fledr.setIdeFceLinhaEnergia(fceLinhaEnergia);
			fledr.setIdeFceLinhaEnergiaDestinoResiduo(null);
			listaFLEDR.add(fledr);
		}
		
		fceLinhaEnergia.setFceLinhaEnergiaDestinoResiduoCollection(listaFLEDR);
		
		List<FceLinhaEnergiaTipoSubestacao> listaFLETS = new ArrayList<FceLinhaEnergiaTipoSubestacao>();
		
		for (FceLinhaEnergiaTipoSubestacao tipoSubestacao : fceLinhaEnergia.getFceLinhaEnergiaTipoSubestacaoCollection()) {
			FceLinhaEnergiaTipoSubestacao flets = (FceLinhaEnergiaTipoSubestacao) tipoSubestacao.clone();
			flets.setIdeFceLinhaEnergia(fceLinhaEnergia);
			flets.setIdeSubestacao(null);
			listaFLETS.add(flets);
		}
		
		fceLinhaEnergia.setFceLinhaEnergiaTipoSubestacaoCollection(listaFLETS);
		
		List<FceLinhaEnergiaLocalizacaoGeografica> listaFLELG = new ArrayList<FceLinhaEnergiaLocalizacaoGeografica>();
		
		for (FceLinhaEnergiaLocalizacaoGeografica locGeo : fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection()) {
			FceLinhaEnergiaLocalizacaoGeografica flelg = (FceLinhaEnergiaLocalizacaoGeografica) locGeo.clone();
			flelg.setIdeFceLinhaEnergia(fceLinhaEnergia);
			flelg.setIdeFceLinhaEnergiaLocalizacaoGeografica(null);
			flelg.setIdeLocalizacaoGeografica(fceFacade.duplicarLocalizacaoGeografica(flelg.getIdeLocalizacaoGeografica()));
			listaFLELG.add(flelg);
		}
		
		fceLinhaEnergia.setFceLinhaEnergiaLocalizacaoGeograficaCollection(listaFLELG);
	}
	
	public void carregarManterSubestacao() {
		try {
			this.tipoSubestacao = new TipoSubestacao();
			this.fceLinhaEnergiaTipoSubestacao = new FceLinhaEnergiaTipoSubestacao(fceLinhaEnergia, null);
			this.listaTipoSubestacao = fceFacade.buscarTipoSubestacao();
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o Tipo subestação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public Boolean validarManterSubestacao() {
		boolean retorno = true;
		
		if (Util.isNullOuVazio(tipoSubestacao.getIdeTipoSubestacao())) {
			JsfUtil.addWarnMessage("Tipo subestação é de preenchimento obrigatório.");
			retorno = false;
		}
		
		if (Util.isNullOuVazio(fceLinhaEnergiaTipoSubestacao.getDscSubestacao())) {
			JsfUtil.addWarnMessage("Nome subestação é de preenchimento obrigatório.");
			retorno = false;
		}
		
		return retorno;
	}
	
	public void salvarFecharModalSubestacao() {
		
		if (validarManterSubestacao()) {
			
			try {
				fceLinhaEnergiaTipoSubestacao.setIdeTipoSubestacao(tipoSubestacao);
				fceLinhaEnergiaTipoSubestacao.setIdeSubestacao(null);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
			
			if (StringUtils.isNotEmpty(fceLinhaEnergiaTipoSubestacao.getDscSubestacao())
					&& StringUtils.isNotEmpty(fceLinhaEnergiaTipoSubestacao.getIdeTipoSubestacao().getDscTipoSubestacao())) {
				
				this.listaFceLinhaEnergiaTipoSubestacao.add(fceLinhaEnergiaTipoSubestacao);
				Html.esconder("fce_manter_subestacao");
				JsfUtil.addSuccessMessage("Inclusão realizada com sucesso!");
			}
		}
	}
	
	public void salvarContinuarModalSubestacao() {
		
		if (validarManterSubestacao()) {
			
			try {
				fceLinhaEnergiaTipoSubestacao.setIdeTipoSubestacao(tipoSubestacao);
				fceLinhaEnergiaTipoSubestacao.setIdeSubestacao(null);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
			
			this.listaFceLinhaEnergiaTipoSubestacao.add(fceLinhaEnergiaTipoSubestacao);
			tipoSubestacao = null;
			fceLinhaEnergiaTipoSubestacao = new FceLinhaEnergiaTipoSubestacao(fceLinhaEnergia, null);
			
			JsfUtil.addSuccessMessage("Inclusão realizada com sucesso!");
		}
	}
	
	public void removerSubestacao() {
		int size = this.listaFceLinhaEnergiaTipoSubestacao.size() - 1;
		
		for (int i = size; i >= 0; i--) {
			
			if (this.listaFceLinhaEnergiaTipoSubestacao.get(i).equals(fceLinhaEnergiaTipoSubestacao)) {
				this.listaFceLinhaEnergiaTipoSubestacao.remove(fceLinhaEnergiaTipoSubestacao);
			}
		}
		
		Html.esconder("confirmaExclusaoSubestação");
		JsfUtil.addSuccessMessage("Exclusão realizada com sucesso.");
	}
	
	public Boolean validarRemoverCollections(FceLinhaEnergia fceLinhaEnergia) {
		
		Boolean retorno = true;
		
		if (!Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaDestinoResiduoCollection())) {
			retorno = false;
		}
		
		if (!Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaResiduoGeradoCollection())) {
			retorno = false;
		}
		
		if (!Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaTipoSubestacaoCollection())) {
			retorno = false;
		}
		
		return retorno;
	}
	
	public void carregarFceLinhaEnergia() {
		try {
			this.fceLinhaEnergia = fceFacade.buscarFceLinhaEnergiaPorFce(fce);
			
			carregarFceLinhaTipoEnergia();
			carregarFcelinhaEnergiaLocalizacaoGeograficaAlternativa();
			carregarFcelinhaEnergiaLocalizacaoGeograficaPrincipal();
			carregarFcelinhaEnergiaTipoSubestacao();
			carregarFcelinhaEnergiaResiduoGerados();
			carregarFcelinhaEnergiaDestinoResiduo();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarFceLinhaTipoEnergia() {
		try {
			this.listTipoEnergiaSelecionados = (List<FceLinhaEnergiaTipoEnergia>) this.fceLinhaEnergia.getFceLinhaEnergiaTipoEnergiaCollection();
			
			for (FceLinhaEnergiaTipoEnergia item : listTipoEnergiaSelecionados) {
				listTipoEnergia.add(item.getTipoEnergia());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a Origem da energia.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarFcelinhaEnergiaLocalizacaoGeograficaAlternativa() {
		try {
			this.listaFceLinhaEnergiaLocalizacaoGeografica = 
					new ArrayList<FceLinhaEnergiaLocalizacaoGeografica>(this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection());
			
			if (Util.isNullOuVazio(this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection())) {
				fceLinhaEnergia.setIndAlternativaLocacional(null);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a Poligonal do traçado alternativo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarFcelinhaEnergiaLocalizacaoGeograficaPrincipal() {
		try {
			for (Iterator<FceLinhaEnergiaLocalizacaoGeografica> itFcelinhaEnergiaLocGeo = 
					fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection().iterator(); itFcelinhaEnergiaLocGeo.hasNext();) {
				
				FceLinhaEnergiaLocalizacaoGeografica fcelinhaEnergiaLocGeo = itFcelinhaEnergiaLocGeo.next();
				
				if (fcelinhaEnergiaLocGeo.getIndPreferencial() == true) {
					
					fceLinhaEnergia.setFceLinhaEnergiaLocalizacaoGeograficaPreferencial(fcelinhaEnergiaLocGeo);
					
					fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setListaMunicipio(
							fceFacade.listarMunicipios(fcelinhaEnergiaLocGeo.getIdeLocalizacaoGeografica()));
					
					fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setIdeLocalizacaoGeografica(
							fceFacade.obterValAreaByTheGemo(fcelinhaEnergiaLocGeo.getIdeLocalizacaoGeografica()));
					
					itFcelinhaEnergiaLocGeo.remove();
				} else {
					fcelinhaEnergiaLocGeo.setIdeLocalizacaoGeografica(fceFacade.obterValAreaByTheGemo(fcelinhaEnergiaLocGeo.getIdeLocalizacaoGeografica()));
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a Poligonal do traçado Principal.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarFcelinhaEnergiaTipoSubestacao() {
		try {
			if (Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaTipoSubestacaoCollection())) {
				isSubestacao = false;
			} else {
				isSubestacao = true;
			}
			
			this.listaFceLinhaEnergiaTipoSubestacao = 
					new ArrayList<FceLinhaEnergiaTipoSubestacao>(this.fceLinhaEnergia.getFceLinhaEnergiaTipoSubestacaoCollection());
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a Subestação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarFcelinhaEnergiaResiduoGerados() {
		try {
			fceLinhaEnergiaResiduoGerado.setTipoResiduoGeradoCollection(new ArrayList<TipoResiduoGerado>());
			
			for (Iterator<FceLinhaEnergiaResiduoGerado> itFcelinhaEnergiaResiduoGerado = 
					fceLinhaEnergia.getFceLinhaEnergiaResiduoGeradoCollection().iterator(); itFcelinhaEnergiaResiduoGerado.hasNext();) {
				
				FceLinhaEnergiaResiduoGerado fcelinhaEnergiaResiduoGerado = (FceLinhaEnergiaResiduoGerado) itFcelinhaEnergiaResiduoGerado.next();
				
				for (TipoResiduoGerado tipoResiduoGeradoObj : listaTipoResiduoGerado) {
					
					if (tipoResiduoGeradoObj.equals(fcelinhaEnergiaResiduoGerado.getIdeTipoResiduoGerado())) {
						tipoResiduoGeradoObj.setChecked(true);
						fceLinhaEnergiaResiduoGerado.getTipoResiduoGeradoCollection().add(tipoResiduoGeradoObj);
					}
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " os Resíduos Gerados.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarFcelinhaEnergiaDestinoResiduo() {
		
		try {
			this.fceLinhaEnergiaDestinoResiduo.setDestinoResiduoCollection(new ArrayList<DestinoResiduo>());
			
			for (Iterator<FceLinhaEnergiaDestinoResiduo> itFcelinhaEnergiaDestinoResiduo = 
					fceLinhaEnergia.getFceLinhaEnergiaDestinoResiduoCollection().iterator(); itFcelinhaEnergiaDestinoResiduo.hasNext();) {
				
				FceLinhaEnergiaDestinoResiduo fcelinhaEnergiaDestinoResiduo = itFcelinhaEnergiaDestinoResiduo.next();
				
				for (DestinoResiduo tipoDestinoResiduo : listaDestinoResiduo) {
					if (tipoDestinoResiduo.equals(fcelinhaEnergiaDestinoResiduo.getIdeDestinoResiduo())) {
						tipoDestinoResiduo.setChecked(true);
						
						if ("Outros".equals(tipoDestinoResiduo.getDscDestinoResiduo())) {
							isOutros = true;
						}
						
						this.fceLinhaEnergiaDestinoResiduo.getDestinoResiduoCollection().add(tipoDestinoResiduo);
					}
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " os Resíduos Gerados.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void removerLocalizacao() {
		
		removerLocalizacao(getFceLinhaEnergiaLocalizacaoGeografica(), true);
		
		getFceLinhaEnergiaLocalizacaoGeografica().setListaMunicipio(null);
		
		this.fceLinhaEnergia.setFceLinhaEnergiaLocalizacaoGeograficaPreferencial(new FceLinhaEnergiaLocalizacaoGeografica());
		this.fceLinhaEnergia.setIndAlternativaLocacional(null);
		this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setIndObjetoConcedido(false);
		this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setIdeFceLinhaEnergia(fceLinhaEnergia);
		
		if (!Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection())) {
			fceLinhaEnergia.setIndAlternativaLocacional(true);
		}
	}
	
	public void removerLocalizacaoAlternativa() {
		removerLocalizacao(fceLinhaEnergiaLocalizacaoGeograficaAlternativa, false);
		this.fceLinhaEnergiaLocalizacaoGeograficaAlternativa = new FceLinhaEnergiaLocalizacaoGeografica(fceLinhaEnergia);
	}
	
	private void removerLocalizacao(FceLinhaEnergiaLocalizacaoGeografica fceLinhaEnergiaLocalizacaoGeografica, Boolean indPreferencial) {
		
		try {
			int size = fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection().size();
			
			if (size > 0) {
				for (Iterator<FceLinhaEnergiaLocalizacaoGeografica> itFcelinhaEnergiaLocGeo = 
						fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection().iterator(); itFcelinhaEnergiaLocGeo.hasNext();) {
					
					FceLinhaEnergiaLocalizacaoGeografica fcelinhaEnergiaLocGeo = itFcelinhaEnergiaLocGeo.next();
					
					if (fcelinhaEnergiaLocGeo.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().equals(
							fceLinhaEnergiaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())) {
						
						itFcelinhaEnergiaLocGeo.remove();
					}
				}
				
				if (fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection().isEmpty()) {
					this.fceLinhaEnergia.setIndAlternativaLocacional(null);
				}
			} else {
				this.fceLinhaEnergia.setFceLinhaEnergiaLocalizacaoGeograficaPreferencial(new FceLinhaEnergiaLocalizacaoGeografica());
				this.fceLinhaEnergia.setIndAlternativaLocacional(null);
				this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setIndObjetoConcedido(false);
				this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setIdeFceLinhaEnergia(fceLinhaEnergia);
			}
			
			fceFacade.excluirLocalizacaoGeografica(fceLinhaEnergiaLocalizacaoGeografica);
			
			JsfUtil.addSuccessMessage("Exclusão realizada com sucesso.");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public Boolean validarFormulario() {
		
		boolean retorno = true;
		
		if (Util.isNullOuVazio(listTipoEnergia)) {
			JsfUtil.addWarnMessage("Origem da Energia é de preenchimento obrigatório.");
			retorno = false;
		}
		
		if (Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().getIdeLocalizacaoGeografica())) {
			JsfUtil.addWarnMessage("Poligonal é de preenchimento obrigatório.");
			retorno = false;
		}
		
		if (Util.isNullOuVazio(fceLinhaEnergia.getValTensaoOperacao())) {
			JsfUtil.addWarnMessage("Tensão da operação é de preenchimento obrigatório.");
			retorno = false;
		}
		
		if (Util.isNullOuVazio(fceLinhaEnergiaResiduoGerado.getTipoResiduoGeradoCollection())) {
			JsfUtil.addWarnMessage("Resíduos Gerados é de preenchimento obrigatório.");
			retorno = false;
		}
		
		if (Util.isNullOuVazio(fceLinhaEnergiaDestinoResiduo.getDestinoResiduoCollection())) {
			JsfUtil.addWarnMessage("Destino dos resíduos é de preenchimento obrigatório.");
			retorno = false;
		} else {
			for (DestinoResiduo t : fceLinhaEnergiaDestinoResiduo.getDestinoResiduoCollection()) {
				if (t.getIdeDestinoResiduo().equals(DestinoResiduoEnum.OUTROS.getId())) {
					MensagemUtil.info0035();
					retorno = false;
					break;
				}
			}
		}
		
		if (Util.isNullOuVazio(fceLinhaEnergia.getValLarguraFaixaServidao())) {
			JsfUtil.addWarnMessage("Largura da faixa da servidão é de preenchimento obrigatório.");
			retorno = false;
		}
		
		if (Util.isNullOuVazio(fceLinhaEnergia.getValAreaTotalFaixaServidao())) {
			JsfUtil.addWarnMessage("Área total da faixa de servidão é de preenchimento obrigatório.");
			retorno = false;
		}
		
		if (Util.isNullOuVazio(listaFceLinhaEnergiaTipoSubestacao) && !Util.isNullOuVazio(isSubestacao)) {
			if (isSubestacao) {
				JsfUtil.addWarnMessage("É necessário incluir subestação");
				retorno = false;
			}
		}
		
		if (Util.isNullOuVazio(isSubestacao)) {
			JsfUtil.addWarnMessage("Subestação é de preenchimento obrigatório.");
			retorno = false;
		}
		
		if (!Util.isNullOuVazio(fceLinhaEnergia.getIndAlternativaLocacional())) {
			if (Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection()) && fceLinhaEnergia.getIndAlternativaLocacional()) {
				JsfUtil.addWarnMessage("É necessário incluir alternativa locacional");
				retorno = false;
			}
			
			isDeleteAlternativa = 
					!Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection()) && !fceLinhaEnergia.getIndAlternativaLocacional();
		}
		
		if (!Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().getIdeLocalizacaoGeografica())) {
			
			if (Util.isNullOuVazio(fceLinhaEnergia.getIndAlternativaLocacional())) {
				JsfUtil.addWarnMessage("Existe alternativa locacional é de preenchimento obrigatório.");
				retorno = false;
			}
		}

		
		if(!Util.isNullOuVazio(fce)) {
			if (!Util.isNullOuVazio(fce.getIdeAnaliseTecnica())) {
				
				if (!this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().getIndObjetoConcedido()) {
					
					if (!Util.isNullOuVazio(this.listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao)) {
						
						boolean isCondedido = false;
						
						for (FceLinhaEnergiaLocalizacaoGeografica loc : this.listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao) {
							
							if (loc.getIndObjetoConcedido()) {
								isCondedido = true;
								break;
							}
						}
						
						if (!isCondedido) {
							retorno = false;
							JsfUtil.addWarnMessage("É necessário selecionar a área concedida.");
						}
					} else {
						retorno = false;
						JsfUtil.addWarnMessage("É necessário selecionar a área concedida.");
					}
				}
			}
		}
		
		return retorno;
	}
	
	public void voltar() {
		Html.esconder("fce_linha_trasmissao_distribuicao");
	}
	
	public String visualizarLocalizacao(LocalizacaoGeografica locGeo) {
		return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(locGeo);
	}
	
	@SuppressWarnings("unchecked")
	public void destinoResiduoCheck(ValueChangeEvent destino) {

		List<DestinoResiduo> destinoResiduoList = (List<DestinoResiduo>) destino.getNewValue();
		List<DestinoResiduo> destinoResiduoOldList = (List<DestinoResiduo>) destino.getOldValue();
		
		if (!Util.isNullOuVazio(destinoResiduoList)) {
			
			for (DestinoResiduo destinoResiduo : destinoResiduoList) {
				
				if ("Outros".equals(destinoResiduo.getDscDestinoResiduo())) {
					
					isOutros = true;
				}
			}
		}
		
		if (!Util.isNullOuVazio(destinoResiduoOldList)) {
			
			for (DestinoResiduo destinoResiduoOld : destinoResiduoOldList) {
				
				if ("Outros".equals(destinoResiduoOld.getDscDestinoResiduo()) && isOutros) {
					isOutros = false;
				}
			}
		}
		
		if (isOutros) {
			JsfUtil.addWarnMessage("Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.");
		}
	}
	
	public void concedidoCheck(FceLinhaEnergiaLocalizacaoGeografica fceLoc) {
		
		for (FceLinhaEnergiaLocalizacaoGeografica l2 : this.listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao) {
			
			if (fceLoc.getIdeLocalizacaoGeografica().equals(l2.getIdeLocalizacaoGeografica())) {
				l2.setIndObjetoConcedido(true);
				this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setIndObjetoConcedido(false);
			} else {
				l2.setIndObjetoConcedido(false);
			}
		}
	}
	
	public void imprimirRelatorio() {
		try {
			this.imprimirRelatorioFce = getImprimirRelatorio(fce, fce.getIdeDocumentoObrigatorio());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void naoImprimir() {
		Html.esconder("fce_linha_trasmissao_distribuicao");
		msgFinalizar();
	}
	
	public void msgFinalizar() {
		if (isOutros) {
			JsfUtil.addWarnMessage("Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.");
		}
		
		if (editavel) {
			JsfUtil.addSuccessMessage("Alteração realizada com sucesso!");
		} else {
			JsfUtil.addSuccessMessage("Inclusão realizada com sucesso!");
		}
	}

	@Override
	public void prepararParaFinalizar() throws Exception {
		if (validarFormulario()) {
			
			if (Util.isNullOuVazio(fce)) {
				iniciarFce(documentoObrigatorio);
			}
			
			fceLinhaEnergia.setIdeFce(fce);
			fceLinhaEnergia.setIndExcluido(false);
			
			FceLinhaEnergia fceLinhaRemover = new FceLinhaEnergia(fceLinhaEnergia);
			
			if (!Util.isNullOuVazio(listTipoEnergiaSelecionados)) {
				listTipoEnergiaSelecionados.clear();
			} else {
				listTipoEnergiaSelecionados = new ArrayList<FceLinhaEnergiaTipoEnergia>();
			}
			
			fceLinhaEnergia.getFceLinhaEnergiaTipoEnergiaCollection().clear();
			
			for (TipoEnergia tipoEnergia : listTipoEnergia) {
				FceLinhaEnergiaTipoEnergia aux = new FceLinhaEnergiaTipoEnergia();
				aux.setIdeFceLinhaEnergia(fceLinhaEnergia);
				aux.setTipoEnergia(tipoEnergia);
				
				listTipoEnergiaSelecionados.add(aux);
				fceLinhaEnergia.getFceLinhaEnergiaTipoEnergiaCollection().add(aux);
			}
			
			fceLinhaEnergia.getFceLinhaEnergiaResiduoGeradoCollection().clear();
			
			for (TipoResiduoGerado tipo : fceLinhaEnergiaResiduoGerado.getTipoResiduoGeradoCollection()) {
				FceLinhaEnergiaResiduoGerado fceEnergiaResiduoGerado = new FceLinhaEnergiaResiduoGerado(fceLinhaEnergia, tipo);
				fceLinhaEnergia.getFceLinhaEnergiaResiduoGeradoCollection().add(fceEnergiaResiduoGerado);
			}
			
			fceLinhaEnergia.getFceLinhaEnergiaDestinoResiduoCollection().clear();
			
			for (DestinoResiduo destino : fceLinhaEnergiaDestinoResiduo.getDestinoResiduoCollection()) {
				FceLinhaEnergiaDestinoResiduo fceEnergiaDestinoResiduo = new FceLinhaEnergiaDestinoResiduo(fceLinhaEnergia, destino);
				fceLinhaEnergia.getFceLinhaEnergiaDestinoResiduoCollection().add(fceEnergiaDestinoResiduo);
				
				if ("Outros".equals(destino.getDscDestinoResiduo())) {
					isOutros = true;
				}
			}
			
			fceLinhaEnergia.getFceLinhaEnergiaTipoSubestacaoCollection().clear();
			
			if (isSubestacao == true && !Util.isNullOuVazio(listaFceLinhaEnergiaTipoSubestacao)) {
				
				for (FceLinhaEnergiaTipoSubestacao tipoSub : listaFceLinhaEnergiaTipoSubestacao) {
					tipoSub.setIdeSubestacao(null);
				}
				
				fceLinhaEnergia.setFceLinhaEnergiaTipoSubestacaoCollection(listaFceLinhaEnergiaTipoSubestacao);
			}
			
			fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().setIndPreferencial(true);
			
			if (isDeleteAlternativa) {
				fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection().clear();
			}
			
			fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection().add(fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial());
			
			try {
				if (!Util.isNullOuVazio(this.fce.getIdeAnaliseTecnica())) {
					
					if (this.fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().getIndObjetoConcedido()) {
						
						processoAtoConcedido.setIdeLocalizacaoGeografica(
								fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial().getIdeLocalizacaoGeografica());
					} else {
						
						for (FceLinhaEnergiaLocalizacaoGeografica loc : this.listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao) {
							
							if (loc.getIndObjetoConcedido() == true) {
								processoAtoConcedido.setIdeLocalizacaoGeografica(loc.getIdeLocalizacaoGeografica());
							}
						}
					}
					
					if (!isOutros) {
						super.concluirFce();
					} else {
						fce.setIndConcluido(false);
					}
					
					fceFacade.salvarFcelinhaConcedido(processoAtoConcedido, fceLinhaEnergia, fceLinhaRemover);
					
					Html.atualizar("frmAnaliseTecnica:gridFce");
				} else {
					
					if (!isOutros) {
						super.concluirFce();
					} else {
						fce.setIndConcluido(false);
					}
					
					fceFacade.salvarFceLinha(fceLinhaEnergia, fceLinhaRemover);
				}
				
				Html.exibir("confirmaImprimirRelatorioLinhaEnergia");
				
			} catch (Exception e) {
				JsfUtil.addErrorMessage(e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public int getSecaoPoligonoOuLinha() {
		return ValidacaoShapeEnum.POLIGONO_OU_LINHA.getId().intValue();
	}
	
	@Override
	protected void carregarFceTecnico() {
		//método não necessário para esse contexto
	}
	
	@Override
	public void verificarEdicao() {
		//método não necessário para esse contexto
	}
	
	@Override
	public void carregarAba() {
		//método não necessário para esse contexto
	}
	
	@Override
	public void limpar() {
		//método não necessário para esse contexto
	}
	
	@Override
	public boolean validarAba() {
		return false;
	}
	
	/*********************
	/*					 *
	//XXX: GETS AND SETS *
	/* 					 *
	/*********************/
	
	public Boolean getIsSubestacao() {
		return isSubestacao;
	}
	
	public void setIsSubestacao(Boolean isSubestacao) {
		this.isSubestacao = isSubestacao;
	}
	
	public FceLinhaEnergia getFceLinhaEnergia() {
		return fceLinhaEnergia;
	}
	
	public void setFceLinhaEnergia(FceLinhaEnergia fceLinhaEnergia) {
		this.fceLinhaEnergia = fceLinhaEnergia;
	}
	
	public List<TipoEnergia> getListaTipoEnergia() {
		return listaTipoEnergia;
	}
	
	public void setListaTipoEnergia(List<TipoEnergia> listaTipoEnergia) {
		this.listaTipoEnergia = listaTipoEnergia;
	}
	
	public TipoSubestacao getTipoSubestacao() {
		return tipoSubestacao;
	}
	
	public void setTipoSubestacao(TipoSubestacao tipoSubestacao) {
		this.tipoSubestacao = tipoSubestacao;
	}
	
	public List<TipoResiduoGerado> getListaTipoResiduoGerado() {
		return listaTipoResiduoGerado;
	}
	
	public void setListaTipoResiduoGerado(List<TipoResiduoGerado> listaTipoResiduoGerado) {
		this.listaTipoResiduoGerado = listaTipoResiduoGerado;
	}
	
	public TipoEnergia getTipoEnergia() {
		return tipoEnergia;
	}
	
	public void setTipoEnergia(TipoEnergia tipoEnergia) {
		this.tipoEnergia = tipoEnergia;
	}
	
	public TipoResiduoGerado getTipoResiduoGerado() {
		return tipoResiduoGerado;
	}
	
	public void setTipoResiduoGerado(TipoResiduoGerado tipoResiduoGerado) {
		this.tipoResiduoGerado = tipoResiduoGerado;
	}
	
	public List<DestinoResiduo> getListaDestinoResiduo() {
		return listaDestinoResiduo;
	}
	
	public void setListaDestinoResiduo(List<DestinoResiduo> listaDestinoResiduo) {
		this.listaDestinoResiduo = listaDestinoResiduo;
	}
	
	public DestinoResiduo getDestinoResiduo() {
		return destinoResiduo;
	}
	
	public void setDestinoResiduo(DestinoResiduo destinoResiduo) {
		this.destinoResiduo = destinoResiduo;
	}
	
	public List<TipoResiduoGerado> getListaSelecionadaTipoResiduoGerado() {
		return listaSelecionadaTipoResiduoGerado;
	}
	
	public void setListaSelecionadaTipoResiduoGerado(List<TipoResiduoGerado> listaSelecionadaTipoResiduoGerado) {
		this.listaSelecionadaTipoResiduoGerado = listaSelecionadaTipoResiduoGerado;
	}
	
	public List<DestinoResiduo> getListaSelecionadaDestinoResiduo() {
		return listaSelecionadaDestinoResiduo;
	}
	
	public void setListaSelecionadaDestinoResiduo(List<DestinoResiduo> listaSelecionadaDestinoResiduo) {
		this.listaSelecionadaDestinoResiduo = listaSelecionadaDestinoResiduo;
	}
	
	public List<TipoSubestacao> getListaTipoSubestacao() {
		return listaTipoSubestacao;
	}
	
	public void setListaTipoSubestacao(List<TipoSubestacao> listaTipoSubestacao) {
		this.listaTipoSubestacao = listaTipoSubestacao;
	}
	
	public FceLinhaEnergiaTipoSubestacao getFceLinhaEnergiaTipoSubestacao() {
		return fceLinhaEnergiaTipoSubestacao;
	}
	
	public void setFceLinhaEnergiaTipoSubestacao(FceLinhaEnergiaTipoSubestacao fceLinhaEnergiaTipoSubestacao) {
		this.fceLinhaEnergiaTipoSubestacao = fceLinhaEnergiaTipoSubestacao;
	}
	
	public List<FceLinhaEnergiaTipoSubestacao> getListaFceLinhaEnergiaTipoSubestacao() {
		return listaFceLinhaEnergiaTipoSubestacao;
	}
	
	public void setListaFceLinhaEnergiaTipoSubestacao(List<FceLinhaEnergiaTipoSubestacao> listaFceLinhaEnergiaTipoSubestacao) {
		this.listaFceLinhaEnergiaTipoSubestacao = listaFceLinhaEnergiaTipoSubestacao;
	}
	
	public FceLinhaEnergiaLocalizacaoGeografica getFceLinhaEnergiaLocalizacaoGeografica() {
		return fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaPreferencial();
	}
	
	public DocumentoObrigatorio getDocumentoObrigatorio() {
		return documentoObrigatorio;
	}
	
	public void setDocumentoObrigatorio(DocumentoObrigatorio documentoObrigatorio) {
		this.documentoObrigatorio = documentoObrigatorio;
	}
	
	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}
	
	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}
	
	public FceLinhaEnergiaLocalizacaoGeografica getFceLinhaEnergiaLocalizacaoGeograficaAlternativa() {
		return fceLinhaEnergiaLocalizacaoGeograficaAlternativa;
	}
	
	public void setFceLinhaEnergiaLocalizacaoGeograficaAlternativa(FceLinhaEnergiaLocalizacaoGeografica fceLinhaEnergiaLocalizacaoGeograficaAlternativa) {
		this.fceLinhaEnergiaLocalizacaoGeograficaAlternativa = fceLinhaEnergiaLocalizacaoGeograficaAlternativa;
	}
	
	public MetodoUtil getMetodoNovoFceLinhaEnergiaLocGeoPrincipal() {
		return metodoNovoFceLinhaEnergiaLocGeoPrincipal;
	}
	
	public void setMetodoNovoFceLinhaEnergiaLocGeoPrincipal(MetodoUtil metodoNovoFceLinhaEnergiaLocGeoPrincipal) {
		this.metodoNovoFceLinhaEnergiaLocGeoPrincipal = metodoNovoFceLinhaEnergiaLocGeoPrincipal;
	}
	
	public MetodoUtil getMetodoNovoFceLinhaEnergiaLocGeoAlternativa() {
		return metodoNovoFceLinhaEnergiaLocGeoAlternativa;
	}
	
	public void setMetodoNovoFceLinhaEnergiaLocGeoAlternativa(MetodoUtil metodoNovoFceLinhaEnergiaLocGeoAlternativa) {
		this.metodoNovoFceLinhaEnergiaLocGeoAlternativa = metodoNovoFceLinhaEnergiaLocGeoAlternativa;
	}
	
	public Boolean getEditavel() {
		return editavel;
	}
	
	public void setEditavel(Boolean editavel) {
		this.editavel = editavel;
	}
	
	public Boolean getDesabilitado() {
		return desabilitado;
	}
	
	public void setDesabilitado(Boolean desabilitado) {
		this.desabilitado = desabilitado;
	}
	
	public FceLinhaEnergiaResiduoGerado getFceLinhaEnergiaResiduoGerado() {
		return fceLinhaEnergiaResiduoGerado;
	}
	
	public void setFceLinhaEnergiaResiduoGerado(FceLinhaEnergiaResiduoGerado fceLinhaEnergiaResiduoGerado) {
		this.fceLinhaEnergiaResiduoGerado = fceLinhaEnergiaResiduoGerado;
	}
	
	public FceLinhaEnergiaDestinoResiduo getFceLinhaEnergiaDestinoResiduo() {
		return fceLinhaEnergiaDestinoResiduo;
	}
	
	public void setFceLinhaEnergiaDestinoResiduo(FceLinhaEnergiaDestinoResiduo fceLinhaEnergiaDestinoResiduo) {
		this.fceLinhaEnergiaDestinoResiduo = fceLinhaEnergiaDestinoResiduo;
	}
	
	public List<FceLinhaEnergiaLocalizacaoGeografica> getListaFceLinhaEnergiaLocalizacaoGeografica() {
		return listaFceLinhaEnergiaLocalizacaoGeografica;
	}
	
	public void setListaFceLinhaEnergiaLocalizacaoGeografica(List<FceLinhaEnergiaLocalizacaoGeografica> listaFceLinhaEnergiaLocalizacaoGeografica) {
		this.listaFceLinhaEnergiaLocalizacaoGeografica = listaFceLinhaEnergiaLocalizacaoGeografica;
	}
	
	public Boolean getIsVisivel() {
		return isVisivel;
	}
	
	public void setIsVisivel(Boolean isVisivel) {
		this.isVisivel = isVisivel;
	}
	
	public FceLinhaEnergiaLocalizacaoGeografica getFceLinhaEnergiaLocalizacaoGeograficaPoligonalNotificacao() {
		return fceLinhaEnergiaLocalizacaoGeograficaPoligonalNotificacao;
	}
	
	public void setFceLinhaEnergiaLocalizacaoGeograficaPoligonalNotificacao(FceLinhaEnergiaLocalizacaoGeografica fceLinhaEnergiaLocalizacaoGeograficaPoligonalNotificacao) {
		this.fceLinhaEnergiaLocalizacaoGeograficaPoligonalNotificacao = fceLinhaEnergiaLocalizacaoGeograficaPoligonalNotificacao;
	}
	
	public List<FceLinhaEnergiaLocalizacaoGeografica> getListaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao() {
		return listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao;
	}
	
	public void setListaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao(List<FceLinhaEnergiaLocalizacaoGeografica> listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao) {
		this.listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao = listaFceEnergiaLocalizacaoGeograficaPoligonalNotificacao;
	}
	
	public ProcessoAto getIdeProcessoAto() {
		return ideProcessoAto;
	}
	
	public void setIdeProcessoAto(ProcessoAto ideProcessoAto) {
		this.ideProcessoAto = ideProcessoAto;
	}
	
	public ProcessoAtoConcedido getProcessoAtoConcedido() {
		return processoAtoConcedido;
	}
	
	public void setProcessoAtoConcedido(ProcessoAtoConcedido processoAtoConcedido) {
		this.processoAtoConcedido = processoAtoConcedido;
	}
	
	public Boolean getIsOutros() {
		return isOutros;
	}
	
	public void setIsOutros(Boolean isOutros) {
		this.isOutros = isOutros;
	}
	
	public Boolean getIsDeleteAlternativa() {
		return isDeleteAlternativa;
	}
	
	public void setIsDeleteAlternativa(Boolean isDeleteAlternativa) {
		this.isDeleteAlternativa = isDeleteAlternativa;
	}
	
	public StreamedContent getImprimirRelatorioFce() {
		return imprimirRelatorioFce;
	}
	
	public void setImprimirRelatorioFce(StreamedContent imprimirRelatorioFce) {
		this.imprimirRelatorioFce = imprimirRelatorioFce;
	}
	
	public List<FceLinhaEnergiaTipoEnergia> getListTipoEnergiaSelecionados() {
		return listTipoEnergiaSelecionados;
	}
	
	public void setListTipoEnergiaSelecionados(List<FceLinhaEnergiaTipoEnergia> listTipoEnergiaSelecionados) {
		this.listTipoEnergiaSelecionados = listTipoEnergiaSelecionados;
	}
	
	public List<TipoEnergia> getListTipoEnergia() {
		return listTipoEnergia;
	}
	
	public void setListTipoEnergia(List<TipoEnergia> listTipoEnergia) {
		this.listTipoEnergia = listTipoEnergia;
	}
}