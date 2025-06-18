package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceCaptacaoController;
import br.gov.ba.seia.entity.Aquifero;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FceCaptacaoSubterranea;
import br.gov.ba.seia.entity.FceCaptacaoSubterraneaCnae;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.OutorgaConcedida;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.TipoAquifero;
import br.gov.ba.seia.entity.TipoPoco;
import br.gov.ba.seia.entity.UnidadeGeologicaAflorante;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoPocoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.FceCaptacaoSubterraneaServiceFacade;
import br.gov.ba.seia.facade.FceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.EnquadramentoDocumentoAtoService;
import br.gov.ba.seia.service.FceCaptacaoSubterraneaCnaeService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaFinalidadeService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("fceCaptacaoSubterraneaController")
@ViewScoped
public class FceCaptacaoSubterraneaController extends BaseFceCaptacaoController implements FceNavegacaoInterface {

	@EJB
	private FceCaptacaoSubterraneaServiceFacade facade;
	
	@EJB
	private FceCaptacaoSubterraneaCnaeService fceCaptacaoSubterraneaCnaeService;
	
	@EJB
	private EnquadramentoDocumentoAtoService enquadramentoDocumentoAtoService;
	
	@EJB
	private OutorgaLocalizacaoGeograficaFinalidadeService outorgaLocalizacaoGeograficaFinalidade;
	
	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	
	@EJB
	private FceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade fceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade;
	
	private int activeTab;

	private FceCaptacaoSubterranea fceCaptacaoSubterranea;

	private List<FceCaptacaoSubterranea> listFceCaptacaoSubterranea;

	private List<UnidadeGeologicaAflorante> listaUnidadesGeologicasAflorantes;
	private List<UnidadeGeologicaAflorante> listaUnidadesGeologicasAflorantesCompleta;
	private List<UnidadeGeologicaAflorante> listaUnidadesGeologicasAflorantesSelecionada;

	private List<Aquifero> listaAquifero;
	private List<Aquifero> listaAquiferoCompleta;
	private List<Aquifero> listaAquiferoSelecionado;

	private List<TipoPoco> listaTipoPoco;
	private List<TipoAquifero> listaTipoAquifero;
	
	private Boolean botaoFceCaracteristicaPoco;
	private Integer tipoPoco;
	private Integer tipoAquifero;
	private String unidadeGeologica;
	private String nomeAquifero;
	private String operacao;
	private Boolean renderCamposCnaeEVazao;
	
	private DataModel<FceCaptacaoSubterraneaCnae> fceCaptacaoSubterraneaCnaeData;

	private static DocumentoObrigatorio DOC_CAPTACAO_SUBTERRANEA = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_CAPTACAO_SUBTERRANEA.getId());
	private static DocumentoObrigatorio DOC_CAPTACAO_SUBTERRANEA_UPLOAD = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_CAPTACAO_SUBTERRANEA_DADOS_ADICIONAIS.getId());

	@Override
	public void init(){
		verificarEdicao();
		carregarAba();
		if(!super.isFceSalvo()){
			iniciarFce(DOC_CAPTACAO_SUBTERRANEA);
			listFceCaptacaoSubterranea = new ArrayList<FceCaptacaoSubterranea>();
		}
		else {
			carregarListaFceCaptacaoSubterranea();
			carregarUploadAbaDadosAdicionais();
		}
		atualizaListaFceCaptacaoSubterranea();
	}

	@Override
	public void carregarAba() {
		carregarListas();
		listarOutorgaLocalizacaoGeografica();
	}

	private void carregarListaAquifero() {
		try {
			listaAquifero = facade.listarAquiferos();
			listaAquiferoCompleta = new ArrayList<Aquifero>();
			listaAquiferoCompleta.addAll(listaAquifero);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Aquíferos.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void pesquisarAquifero(){
		atualizarListaAquifero();
		List<Aquifero> listaTemp = new ArrayList<Aquifero>();
		listaTemp.addAll(listaAquifero);
		listaAquifero.clear();
		for(Aquifero temp : listaTemp){
			if(temp.getNomAquifero().toLowerCase().indexOf(nomeAquifero.toLowerCase()) != -1){
				listaAquifero.add(temp);
			}
		}
	}

	public void excluirAquifero(){
		fceCaptacaoSubterranea.setIdeAquifero(null);
		listaAquifero.clear();
		listaAquifero.addAll(listaAquiferoCompleta);
		listaAquiferoSelecionado.clear();
	}

	private void atualizarListaAquifero() {
		listaAquifero.clear();
		listaAquifero.addAll(listaAquiferoCompleta);
		listaAquifero.removeAll(listaAquiferoSelecionado);
	}

	public void selecionarAquifero(){
		if(Util.isNullOuVazio(listaAquiferoSelecionado)){
			nomeAquifero="";
			listaAquiferoSelecionado.add(fceCaptacaoSubterranea.getIdeAquifero());
			atualizarListaAquifero();
		}
		else{
			JsfUtil.addWarnMessage(Util.getString("msg_generica_selecao_apenas") + "um Aquífero.");
		}

	}

	public boolean isAquiferoSelecionado(){
		return !Util.isNullOuVazio(listaAquiferoSelecionado);
	}

	private void carregarUnidadesGeologicas() {
		try {
			listaUnidadesGeologicasAflorantes = facade.listarUnidadeAflorante();
			listaUnidadesGeologicasAflorantesCompleta = new ArrayList<UnidadeGeologicaAflorante>();
			listaUnidadesGeologicasAflorantesCompleta.addAll(listaUnidadesGeologicasAflorantes);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	public void pesquisarUnidadesGeologicas(){
		atualizarListaUnidadesGeologicas();
		List<UnidadeGeologicaAflorante> listaTemp = new ArrayList<UnidadeGeologicaAflorante>();
		listaTemp.addAll(listaUnidadesGeologicasAflorantes);
		listaUnidadesGeologicasAflorantes.clear();
		for(UnidadeGeologicaAflorante temp : listaTemp){
			if(temp.getDscUnidadeGeologicaAflorante().toLowerCase().indexOf(unidadeGeologica.toLowerCase()) != -1){
				listaUnidadesGeologicasAflorantes.add(temp);
			}
		}
	}

	public void excluirUnidadeGeologica(){
		fceCaptacaoSubterranea.setIdeUnidadeGeologicaAflorante(null);
		listaUnidadesGeologicasAflorantes.clear();
		listaUnidadesGeologicasAflorantes.addAll(listaUnidadesGeologicasAflorantesCompleta);
		listaUnidadesGeologicasAflorantesSelecionada.clear();
	}

	private void atualizarListaUnidadesGeologicas() {
		listaUnidadesGeologicasAflorantes.clear();
		listaUnidadesGeologicasAflorantes.addAll(listaUnidadesGeologicasAflorantesCompleta);
		listaUnidadesGeologicasAflorantes.removeAll(listaUnidadesGeologicasAflorantesSelecionada);
	}

	public void selecionarUnidadeAflorante(){
		if(Util.isNullOuVazio(listaUnidadesGeologicasAflorantesSelecionada)){
			unidadeGeologica="";
			listaUnidadesGeologicasAflorantesSelecionada.add(fceCaptacaoSubterranea.getIdeUnidadeGeologicaAflorante());
			atualizarListaUnidadesGeologicas();
		}
		else{
			JsfUtil.addWarnMessage(Util.getString("msg_generica_selecao_apenas") + "uma Unidade Geológica Aflorante.");
		}
	}

	public boolean isUnidadeAfloranteSelecionada(){
		return !Util.isNullOuVazio(listaUnidadesGeologicasAflorantesSelecionada);
	}

	/**
	 * Edicao
	 * buscar no banco fceCaptacaoSubterranea por fce
	 */
	public void carregarListaFceCaptacaoSubterranea(){
		try{
			listFceCaptacaoSubterranea = facade.listarFceCaptacaoSubterranea(super.fce);
			for(FceCaptacaoSubterranea fceCaptacaoSubterranea : listFceCaptacaoSubterranea){
				if(!Util.isNullOuVazio(fceCaptacaoSubterranea.getNumTempoCaptacao())){
					calcularVazaoHora(fceCaptacaoSubterranea);
					fceCaptacaoSubterranea.setConfirmado(true);
				}
				if(!Util.isNullOuVazio(super.listaOutorgaLocalizacaoGeografica) && !Util.isNullOuVazio(fceCaptacaoSubterranea.getIdeOutorgaLocalizacaoGeografica())){
					for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : super.listaOutorgaLocalizacaoGeografica){
						if(fceCaptacaoSubterranea.getIdeOutorgaLocalizacaoGeografica().equals(outorgaLocalizacaoGeografica)){
							fceCaptacaoSubterranea.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
						}
					}
				}
			}
			
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	public void carregarUploadAbaDadosAdicionais(){
		try{
			carregarDocumentoAdicionalByDocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_CAPTACAO_SUBTERRANEA_DADOS_ADICIONAIS);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o Documento Adicional do FCE - Captação Subterrânea.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void carregarDadosCaracteristicaPoco(){
		carregarUnidadeGeologicaAflorante();
		carregarAquifero();
				
		carregarDadosCnae();
	}

	private void carregarDadosCnae() {

		try {
			if(fceCaptacaoSubterranea != null){
				List<FceCaptacaoSubterraneaCnae> listaFce = (List<FceCaptacaoSubterraneaCnae>) fceCaptacaoSubterraneaCnaeService.listarCnaeByFce(fceCaptacaoSubterranea);
				this.fceCaptacaoSubterraneaCnaeData = new ListDataModel<FceCaptacaoSubterraneaCnae>(listaFce);
				
				
				renderCamposCnaeEVazao = isCnaeEVazao();
				
				CnaeController cnae = (CnaeController) SessaoUtil.recuperarManagedBean("#{cnaeController}", CnaeController.class);
				cnae.init();
				cnae.setFceCaptacaoSubterraneaCnaeData(fceCaptacaoSubterraneaCnaeData);
				cnae.setFceCaptacaoSubterranea(fceCaptacaoSubterranea);
				if(fceCaptacaoSubterraneaCnaeData.getRowCount() > 0){
					
					cnae.setFlagTableCnae(true);
				}else{
					cnae.setFlagTableCnae(false);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private Boolean isCnaeEVazao() {
		Boolean renderCamposCnaeEVazao = Boolean.FALSE;
		try {
			if(fceCaptacaoSubterranea.getIdeOutorgaLocalizacaoGeografica() != null){
				List<OutorgaLocalizacaoGeograficaFinalidade> outorgaFinalidades;
					outorgaFinalidades = outorgaLocalizacaoGeograficaFinalidade
							.obterListaOutorgaLocalizacaoGeograficaFinalidades(
									new OutorgaLocalizacaoGeografica(fceCaptacaoSubterranea.getIdeOutorgaLocalizacaoGeografica().getIdeOutorgaLocalizacaoGeografica()));
				
				for (OutorgaLocalizacaoGeograficaFinalidade outorga : outorgaFinalidades) {
					if(outorga.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.LAZER_TURISMO.getId() || outorga.getIdeTipoFinalidadeUsoAgua().
							getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.COMERCIO_SERVICOS.getId() ){
						renderCamposCnaeEVazao = Boolean.TRUE;
						break;
					}
				}				
			}else{
				List<FceOutorgaLocalizacaoGeograficaFinalidade> listaFceOutorgaLocalizacaoGeograficaFinalidade = fceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade.listarTipoFinalidadeUsoAguaByFce(fceCaptacaoSubterranea.getIdeFceOutorgaLocalizacaoGeografica());
				fceCaptacaoSubterranea.getIdeFceOutorgaLocalizacaoGeografica().setListaFinalidade(listaFceOutorgaLocalizacaoGeograficaFinalidade);
				for (FceOutorgaLocalizacaoGeograficaFinalidade outorga : listaFceOutorgaLocalizacaoGeograficaFinalidade) {
					if(outorga.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.LAZER_TURISMO.getId() || outorga.getIdeTipoFinalidadeUsoAgua().
							getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.COMERCIO_SERVICOS.getId() ){
						renderCamposCnaeEVazao = Boolean.TRUE;
						break;
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return renderCamposCnaeEVazao;
	}

	private void carregarUnidadeGeologicaAflorante() {
		unidadeGeologica="";
		if(isUnidadeAfloranteSelecionada()){
			listaUnidadesGeologicasAflorantesSelecionada.clear();
		}
		else {
			listaUnidadesGeologicasAflorantesSelecionada = new ArrayList<UnidadeGeologicaAflorante>();
		}
		if(!Util.isNullOuVazio(fceCaptacaoSubterranea.getIdeUnidadeGeologicaAflorante())){
			listaUnidadesGeologicasAflorantesSelecionada.add(fceCaptacaoSubterranea.getIdeUnidadeGeologicaAflorante());
			listaUnidadesGeologicasAflorantes.removeAll(listaUnidadesGeologicasAflorantesSelecionada);
		}
	}

	private void carregarAquifero() {
		nomeAquifero="";
		if(isAquiferoSelecionado()){
			listaAquiferoSelecionado.clear();
		}
		else {
			listaAquiferoSelecionado = new ArrayList<Aquifero>();
		}
		if(!Util.isNullOuVazio(fceCaptacaoSubterranea.getIdeAquifero())){
			listaAquiferoSelecionado.add(fceCaptacaoSubterranea.getIdeAquifero());
			listaAquifero.removeAll(listaAquiferoSelecionado);
		}
	}

	@Override
	public void controlarAbas(TabChangeEvent event) {
		if ("abaDadosRequerimento".equals(event.getTab().getId())) {
			activeTab = 0;
		} else if ("abaAdicionais".equals(event.getTab().getId())) {
			activeTab = 1;
		}
	}

	@Override
	public void voltarAba() {
		activeTab--;
	}

	@Override
	public void avancarAba() {
		if(validarAbaDadosDoRequerimento()){
			activeTab++;
		}
	}

	public void salvarFceCaptacaoSubterranea() {
		if (validarAba()) {
			try {
				//#9905 - remove a Vazão da captação
				BigDecimal numVazaoAtual = fceCaptacaoSubterranea.getNumVazao();
				fceCaptacaoSubterranea.setNumVazao(null);
				
				inserirAlterarCaracteristicaPoco();
				verificarDadosHidrogeologicosZero();
				salvarFceCaptacaoSubterraneaCnae();
				facade.salvarFceCaptacaoSubterranea(fceCaptacaoSubterranea);
				fceCaptacaoSubterranea.setNumVazao(numVazaoAtual);
				this.atualizarVazao();
				if(getOperacao().equals("NOVO")){
					super.exibirMensagem001();
				}
				else{
					super.exibirMensagem002();
				}
				RequestContext.getCurrentInstance().execute("fce_caracteristica_poco.hide()");
			}catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar"));
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * Salvar lista de captação subterranea cnae
	 */
	private void salvarFceCaptacaoSubterraneaCnae() {
		if (!Util.isNullOuVazio(fceCaptacaoSubterraneaCnaeData)) {
			for (FceCaptacaoSubterraneaCnae fceDataModel : fceCaptacaoSubterraneaCnaeData) {
				fceCaptacaoSubterraneaCnaeService.salvarOuAtualizarCaptacaoSubterraneaCnae(fceDataModel);
			}
		}
	}
	
	private void salvarFceCaptacaoSubterraneaCnaeTecnico() {
		try {
			if (!Util.isNullOuVazio(fceCaptacaoSubterraneaCnaeData)) {
				for (FceCaptacaoSubterraneaCnae fceDataModel : fceCaptacaoSubterraneaCnaeData) {
					FceCaptacaoSubterraneaCnae fceDataModelClone = fceDataModel.clone();
					fceDataModelClone.setIdeFceCaptacaoSubterraneaCnae(null);
					fceDataModelClone.setIdeFceCaptacaoSubterranea(fceCaptacaoSubterranea);
					fceCaptacaoSubterraneaCnaeService.salvarFceCaptacaoSubterraneaCnae(fceDataModelClone);
				}
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	
	public void inserirAlterarCaracteristicaPoco(){
		if(Util.isNullOuVazio(fceCaptacaoSubterranea.getIdeTipoPoco())){
			operacao = "NOVO";
		}
		else{
			operacao = "EDITAR";
		}
	}

	public void verificarDadosHidrogeologicosZero(){
		if(!Util.isNullOuVazio(fceCaptacaoSubterranea.getNumProfunidadePoco())){
			if(fceCaptacaoSubterranea.getNumProfunidadePoco() == 0.0){
				fceCaptacaoSubterranea.setNumProfunidadePoco(null);
			}
		}
		if(!Util.isNullOuVazio(fceCaptacaoSubterranea.getNumNivelDinamico())){
			if(fceCaptacaoSubterranea.getNumNivelDinamico() == 0.0){
				fceCaptacaoSubterranea.setNumNivelDinamico(null);
			}
		}
		if(!Util.isNullOuVazio(fceCaptacaoSubterranea.getNumNivelEstatico())){
			if(fceCaptacaoSubterranea.getNumNivelEstatico() == 0.0){
				fceCaptacaoSubterranea.setNumNivelEstatico(null);
			}
		}
		if(Util.isNullOuVazio(fceCaptacaoSubterranea.getNumVazaoEspecifica())){
			fceCaptacaoSubterranea.setNumVazaoEspecifica(null);
		}
		if(!Util.isNullOuVazio(fceCaptacaoSubterranea.getNumVazaoTeste())){
			if(fceCaptacaoSubterranea.getNumVazaoTeste() == 0.0){
				fceCaptacaoSubterranea.setNumVazaoTeste(null);
			}
		}
	}

	public void calcularVazaoEspecificia(){
		if(isCalcularVazaoEspecifica()){
			fceCaptacaoSubterranea.setNumVazaoEspecifica(fceCaptacaoSubterranea.getNumVazaoTeste()/ (fceCaptacaoSubterranea.getNumNivelDinamico() - fceCaptacaoSubterranea.getNumNivelEstatico()));
		}
		else{
			fceCaptacaoSubterranea.setNumVazaoEspecifica(new Double(0));
		}
	}

	private boolean isCalcularVazaoEspecifica() {
		return !Util.isNullOuVazio(fceCaptacaoSubterranea) && !Util.isNullOuVazio(fceCaptacaoSubterranea.getNumVazaoTeste()) && !Util.isNullOuVazio(fceCaptacaoSubterranea.getNumNivelDinamico()) && !Util.isNullOuVazio(fceCaptacaoSubterranea.getNumNivelEstatico()) && ((fceCaptacaoSubterranea.getNumVazaoTeste()!=-1) && (fceCaptacaoSubterranea.getNumNivelDinamico()!=-1 && fceCaptacaoSubterranea.getNumNivelEstatico()!=-1)) ;
	}

	/**
	 *Verificar se FCE já existe
	 */
	@Override
	public void verificarEdicao() {
		carregarFceDoRequerente(DOC_CAPTACAO_SUBTERRANEA);
	}

	@Override
	public boolean validarAba() {
		Boolean valido = true;
		if (Util.isNullOuVazio(fceCaptacaoSubterranea.getIdeTipoPoco())) {
			JsfUtil.addErrorMessage("Tipo do poço " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if (Util.isNullOuVazio(fceCaptacaoSubterranea.getIdeUnidadeGeologicaAflorante())) {
			JsfUtil.addErrorMessage("Unidade geológica aflorante " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}

		if (Util.isNullOuVazio(fceCaptacaoSubterranea.getIdeTipoAquifero())) {
			JsfUtil.addErrorMessage("Tipo do aquífero " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if (!Util.isNullOuVazio(fceCaptacaoSubterranea.getIdeTipoPoco()) && fceCaptacaoSubterranea.getIdeTipoPoco().getIdeTipoPoco().equals(TipoPocoEnum.OUTROS.getId())) {
			if (fceCaptacaoSubterranea.getNumProfunidadePoco().doubleValue() == -1){
				JsfUtil.addErrorMessage("Profundidade do poço " + Util.getString("fce_captacao_subterranea_tempo_captacao_msg_0011"));
				fceCaptacaoSubterranea.setNumProfunidadePoco(0.00);
				valido = false;
			}
			if (fceCaptacaoSubterranea.getNumNivelEstatico().doubleValue() == -1){
				JsfUtil.addErrorMessage("Nível estático " + Util.getString("fce_captacao_subterranea_tempo_captacao_msg_0011"));
				fceCaptacaoSubterranea.setNumNivelEstatico(0.00);
				valido = false;
			}
			if (fceCaptacaoSubterranea.getNumNivelDinamico().doubleValue() == -1){
				JsfUtil.addErrorMessage("Nível dinâmico " + Util.getString("fce_captacao_subterranea_tempo_captacao_msg_0011"));
				fceCaptacaoSubterranea.setNumNivelDinamico(0.00);
				valido = false;
			}
			if (fceCaptacaoSubterranea.getNumVazaoTeste().doubleValue() == -1){
				JsfUtil.addErrorMessage("Vazão de teste " + Util.getString("fce_captacao_subterranea_tempo_captacao_msg_0011"));
				fceCaptacaoSubterranea.setNumVazaoTeste(0.00);
				valido = false;
			}
			if (!Util.isNullOuVazio(fceCaptacaoSubterranea.getNumVazaoEspecifica()) && fceCaptacaoSubterranea.getNumVazaoEspecifica().doubleValue() == -1){
				JsfUtil.addErrorMessage("Vazão específica " + Util.getString("fce_captacao_subterranea_tempo_captacao_msg_0011"));
				fceCaptacaoSubterranea.setNumVazaoEspecifica(0.00);
				valido = false;
			}
		}

		if (!Util.isNullOuVazio(fceCaptacaoSubterranea.getIdeTipoPoco()) && fceCaptacaoSubterranea.getIdeTipoPoco().getIdeTipoPoco().equals(TipoPocoEnum.TUBULAR.getId())) {
			if (Util.isNullOuVazio(fceCaptacaoSubterranea.getNumProfunidadePoco())) {
				JsfUtil.addErrorMessage("Profundidade do poço " + Util.getString("msg_generica_preenchimento_obrigatorio"));
				valido = false;
			}else if (fceCaptacaoSubterranea.getNumProfunidadePoco().doubleValue() == -1){
				JsfUtil.addErrorMessage("Profundidade do poço " + Util.getString("fce_captacao_subterranea_tempo_captacao_msg_0011"));
				fceCaptacaoSubterranea.setNumProfunidadePoco(0.00);
				valido = false;
			}
			if (Util.isNullOuVazio(fceCaptacaoSubterranea.getNumNivelEstatico())) {
				JsfUtil.addErrorMessage("Nível estático " + Util.getString("msg_generica_preenchimento_obrigatorio"));
				valido = false;
			}else if (fceCaptacaoSubterranea.getNumNivelEstatico().doubleValue() == -1){
				JsfUtil.addErrorMessage("Nível estático " + Util.getString("fce_captacao_subterranea_tempo_captacao_msg_0011"));
				fceCaptacaoSubterranea.setNumNivelEstatico(0.00);
				valido = false;
			}
			if (Util.isNullOuVazio(fceCaptacaoSubterranea.getNumNivelDinamico())) {
				JsfUtil.addErrorMessage("Nível dinâmico " + Util.getString("msg_generica_preenchimento_obrigatorio"));
				valido = false;
			}
			else if (fceCaptacaoSubterranea.getNumNivelDinamico().doubleValue() == -1){
				JsfUtil.addErrorMessage("Nível dinâmico " + Util.getString("fce_captacao_subterranea_tempo_captacao_msg_0011"));
				fceCaptacaoSubterranea.setNumNivelDinamico(0.00);
				valido = false;
			}
			if (Util.isNullOuVazio(fceCaptacaoSubterranea.getNumVazaoTeste())) {
				JsfUtil.addErrorMessage("Vazão de teste " + Util.getString("msg_generica_preenchimento_obrigatorio"));
				valido = false;
			}else if (fceCaptacaoSubterranea.getNumVazaoTeste().doubleValue() == -1){
				JsfUtil.addErrorMessage("Vazão de teste " + Util.getString("fce_captacao_subterranea_tempo_captacao_msg_0011"));
				fceCaptacaoSubterranea.setNumVazaoTeste(0.00);
				valido = false;
			}
			if (Util.isNullOuVazio(fceCaptacaoSubterranea.getNumVazaoEspecifica())) {
				JsfUtil.addErrorMessage("Vazão específica " + Util.getString("msg_generica_preenchimento_obrigatorio"));
				valido = false;
			}else if (fceCaptacaoSubterranea.getNumVazaoEspecifica().doubleValue() == -1){
				JsfUtil.addErrorMessage("Vazão específica " + Util.getString("fce_captacao_subterranea_tempo_captacao_msg_0011"));
				fceCaptacaoSubterranea.setNumVazaoEspecifica(0.00);
				valido = false;
			}
		}
		if(isNivelEstaticoMaiorIgualNivelDinamico()){
			JsfUtil.addErrorMessage(Util.getString("fce_captacao_subteranea_nivel_estatico_menor_nivel_dinamico"));
			valido = false;
		}
		if(isProfundidadePocoMenorNivelDinamico()){
			JsfUtil.addErrorMessage(Util.getString("fce_captacao_subteranea_profundidado_poco_maior_nivel_dinamico"));
			valido = false;
		}

		if(isProfundidadePocoMenorNivelEstatico()){
			JsfUtil.addErrorMessage(Util.getString("fce_captacao_subteranea_profundidado_poco_maior_nivel_estatico"));
			valido = false;
		}
		if(fceCaptacaoSubterraneaCnaeData.getRowCount()<1 && isCnaeEVazao()){
			JsfUtil.addErrorMessage("O campo CNAE " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		return valido;
	}

	private boolean isProfundidadePocoMenorNivelEstatico() {
		return !Util.isNullOuVazio(fceCaptacaoSubterranea.getNumProfunidadePoco()) && !Util.isNullOuVazio(fceCaptacaoSubterranea.getNumNivelEstatico()) && (fceCaptacaoSubterranea.getNumProfunidadePoco()<fceCaptacaoSubterranea.getNumNivelEstatico());
	}

	private boolean isProfundidadePocoMenorNivelDinamico() {
		return !Util.isNullOuVazio(fceCaptacaoSubterranea.getNumProfunidadePoco()) && !Util.isNullOuVazio(fceCaptacaoSubterranea.getNumNivelDinamico()) && (fceCaptacaoSubterranea.getNumProfunidadePoco()<fceCaptacaoSubterranea.getNumNivelDinamico());
	}

	private boolean isNivelEstaticoMaiorIgualNivelDinamico() {
		return !Util.isNullOuVazio(fceCaptacaoSubterranea.getNumNivelEstatico()) && !Util.isNullOuVazio(fceCaptacaoSubterranea.getNumNivelDinamico()) && fceCaptacaoSubterranea.getNumNivelEstatico()>=fceCaptacaoSubterranea.getNumNivelDinamico();
	}

	public Boolean validarTempoCaptacaoAndVazao(FceCaptacaoSubterranea fceCaptacaoSubterranea) {
		if(Util.isNullOuVazio(fceCaptacaoSubterranea.getNumVazao())){
			JsfUtil.addErrorMessage("Vazão máxima " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			return false;
		}
		if(Util.isNullOuVazio(fceCaptacaoSubterranea.getNumTempoCaptacao())){
			JsfUtil.addErrorMessage("Tempo de captação " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			return false;
		}
		if (fceCaptacaoSubterranea.getNumTempoCaptacao() > 24) {
			JsfUtil.addErrorMessage(Util.getString("fce_captacao_subteranea_tempo_captacao_info_0034"));
			return false;
		}
		return true;
	}

	public void calcularVazaoHora(FceCaptacaoSubterranea fceCaptacaoSubterranea){
		if(!Util.isNullOuVazio(fceCaptacaoSubterranea.getNumVazao()) && !Util.isNullOuVazio(fceCaptacaoSubterranea.getNumTempoCaptacao())) {
			fceCaptacaoSubterranea.setVazaoHora(fceCaptacaoSubterranea.getNumVazao().doubleValue()/fceCaptacaoSubterranea.getNumTempoCaptacao());
		}
	}


	public void salvarTempoCaptacao(){
		if(validarTempoCaptacaoAndVazao(fceCaptacaoSubterranea)){
			calcularVazaoHora(fceCaptacaoSubterranea);
			try{
				super.salvarFce();
				inserirOuAlterar();
				facade.salvarFceCaptacaoSubterranea(fceCaptacaoSubterranea);
				atualizarVazao();
				if(getOperacao().equals("NOVO")){
					super.exibirMensagem001();
				}
				else{
					super.exibirMensagem002();
				}
			}catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar"));
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	public void inserirOuAlterar(){
		if(Util.isNullOuVazio(fceCaptacaoSubterranea.getIdeFceCaptacaoSubterranea())){
			operacao = "NOVO";
		}
		else{
			operacao="EDITAR";
		}
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 16/02/2016
	 */
	private void atualizarVazao() throws Exception {
		if(super.isFceTecnico()){
			super.fceOutorgaServiceFacade.salvarFceOutorgaLocGeo(fceCaptacaoSubterranea.getIdeFceOutorgaLocalizacaoGeografica());
		} 
		else {
			super.fceOutorgaServiceFacade.salvarOrAtualizarOutorgaLocalizacaoGeografica(fceCaptacaoSubterranea.getIdeOutorgaLocalizacaoGeografica());
		}
	}

	public FceCaptacaoSubterranea buscarFceCaptacaoSubterraneaNaLista(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica){
		for(FceCaptacaoSubterranea fceCaptacaoSubterraneaObj : listFceCaptacaoSubterranea){
			if(fceCaptacaoSubterraneaObj.getIdeOutorgaLocalizacaoGeografica().equals(outorgaLocalizacaoGeografica)){
				return fceCaptacaoSubterraneaObj;
			}
		}
		return null;
	}

	public void listarOutorgaLocalizacaoGeografica(){
		try{
			super.listarOutorgaLocGeoBy(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA);
		}catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);		}

	}

	/**
	 * atualiza a listFceCaptacaoSubterranea
	 */
	private void atualizaListaFceCaptacaoSubterranea(){
		List<OutorgaLocalizacaoGeografica> listaTEMP = super.listaOutorgaLocalizacaoGeografica;
		if(super.listaOutorgaLocalizacaoGeografica.size() >= listFceCaptacaoSubterranea.size()){
			for(FceCaptacaoSubterranea fceCaptacaoSubterraneaObj : listFceCaptacaoSubterranea){
				if(super.listaOutorgaLocalizacaoGeografica.contains(fceCaptacaoSubterraneaObj.getIdeOutorgaLocalizacaoGeografica())){
					listaTEMP.remove(fceCaptacaoSubterraneaObj.getIdeOutorgaLocalizacaoGeografica());
				}
				for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : super.listaOutorgaLocalizacaoGeografica){
					if(fceCaptacaoSubterraneaObj.getIdeOutorgaLocalizacaoGeografica().equals(outorgaLocalizacaoGeografica)){
						fceCaptacaoSubterraneaObj.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
					}
				}
			}
		}
		if(!Util.isNullOuVazio(listaTEMP)){
			for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : listaTEMP){
				FceCaptacaoSubterranea fceCaptacaoSubterraneaObj = new FceCaptacaoSubterranea(outorgaLocalizacaoGeografica, super.fce);
				listFceCaptacaoSubterranea.add(fceCaptacaoSubterraneaObj);

			}
		}
		limparListalistaOutorgaLocGeo();
	}

	public void limparListalistaOutorgaLocGeo(){
		super.listaOutorgaLocalizacaoGeografica.clear();
		for(FceCaptacaoSubterranea fceCaptacaoSubterraneaObj : listFceCaptacaoSubterranea){
			super.listaOutorgaLocalizacaoGeografica.add(fceCaptacaoSubterraneaObj.getIdeOutorgaLocalizacaoGeografica());
		}
	}

	public StreamedContent getDadosAdicionais() {
		try {
			return getDadosAdicionais("Informacoes_Adicionais_FCE_Captacao_Subterranea.doc", "Informações Adicionais - FCE Captação Subterrânea.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOC_CAPTACAO_SUBTERRANEA);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Captação Subterrânea.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private boolean validarAbaDadosDoRequerimento(){
		for(FceCaptacaoSubterranea fceCaptacaoSubterranea : listFceCaptacaoSubterranea) {
			if(!fceCaptacaoSubterranea.isConfirmado()){
				JsfUtil.addErrorMessage(Util.getString("msg_generica_necessario_confirmar_inf_0040") + " o FCE - Captação Subterrânea.");
				return false;
			} 
			else {
				if(Util.isNullOuVazio(fceCaptacaoSubterranea.getIdeTipoPoco())){
					JsfUtil.addErrorMessage(Util.getString("fce_captacao_subterranea_caracteristicas_poco_inf_0027"));
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean validarAbaAdicionais() {
		if(!super.isArquivoUpado()) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_upload_inf_0023") + " Upload dos Dados Adicionais.");
			return false;
		}
		return true;
	}

	@Override
	public void finalizar() {
		if(validarAbaAdicionais()) {
			if(validarAbaDadosDoRequerimento()){
				try {
					facade.finalizar(this);
				} 
				catch (Exception e) {
					JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_finalizar") + " o FCE - Captação Subterrânea.");
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
			else{
				voltarAba();
			}
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		salvarAbaAdicionais();
		super.concluirFce();

		JsfUtil.addSuccessMessage(Util.getString("msg_generica_sucesso_finalizar"));
		RequestContext.getCurrentInstance().execute("rel_cap_sub.show()");
		RequestContext.getCurrentInstance().execute("fce_cap_subterranea.hide()");
		if(super.isFceTecnico()){
			JsfUtil.addWarnMessage(Util.getString("analise_tecnica_msg_verificar_finalidade"));	
		}
		limpar();
	}

	public boolean verificarPerguntaRequerimentoCampoNumVazao(FceCaptacaoSubterranea fceCaptacaoSubterranea){
		if(!Util.isNull(fceCaptacaoSubterranea.getIdeOutorgaLocalizacaoGeografica())){
			try {
				PerguntaRequerimento perguntaRequerimento = facade.buscarPerguntaRequerimento(super.requerimento, fceCaptacaoSubterranea);
				return perguntaRequerimento.getIndResposta().equals(true);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return false;
	}

	public boolean isDisableNumVazao(FceCaptacaoSubterranea fceCaptacaoSubterranea){
		if(verificarPerguntaRequerimentoCampoNumVazao(fceCaptacaoSubterranea)){
			return true;
		}
		else if(!fceCaptacaoSubterranea.isConfirmado()){
			return false;
		}
		return true;
	}

	private void salvarAbaAdicionais(){
		try {
			super.salvarDocumentoAdicional(requerimento, DOC_CAPTACAO_SUBTERRANEA_UPLOAD);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void limpar(){
		activeTab = 0;
		fceCaptacaoSubterranea = new FceCaptacaoSubterranea();
		super.limparDadosOutorga();
		super.limparDocumentoUpado();
		if(!Util.isNullOuVazio(listFceCaptacaoSubterranea)){
			listFceCaptacaoSubterranea.clear();
		}
		unidadeGeologica = null;
		nomeAquifero = null;
		listaUnidadesGeologicasAflorantes = null;
		listaUnidadesGeologicasAflorantesCompleta = null;
		listaUnidadesGeologicasAflorantesSelecionada = null;
		listaAquifero = null;
		listaAquiferoCompleta = null;
		listaAquiferoSelecionado = null;
		listaTipoPoco = null;
		listaTipoAquifero = null;
	}

	public void carregarListas() {
		carregarListaTipoPoco();
		carregarListaTipoAquifero();
		carregarUnidadesGeologicas();
		carregarListaAquifero();
	}


	public void carregarListaTipoAquifero() {
		try {
			listaTipoAquifero = facade.listarTipoAquifero();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}


	public void carregarListaTipoPoco() {
		try {
			listaTipoPoco = facade.listarTipoPoco();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public String getOperacao(){
		return operacao;
	}

	public void setOperacao(String operacao){
		this.operacao = operacao;
	}

	public FceCaptacaoSubterranea getFceCaptacaoSubterranea() {
		return fceCaptacaoSubterranea;
	}

	public void setFceCaptacaoSubterranea(FceCaptacaoSubterranea fceCaptacaoSubterranea) {
		this.fceCaptacaoSubterranea = fceCaptacaoSubterranea;
	}

	public List<TipoPoco> getListaTipoPoco() {
		return listaTipoPoco;
	}

	public void setListaTipoPoco(List<TipoPoco> listaTipoPoco) {
		this.listaTipoPoco = listaTipoPoco;
	}

	public List<TipoAquifero> getListaTipoAquifero() {
		return listaTipoAquifero;
	}

	public void setListaTipoAquifero(List<TipoAquifero> listaTipoAquifero) {
		this.listaTipoAquifero = listaTipoAquifero;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public Integer getTipoPoco() {
		return tipoPoco;
	}

	public void setTipoPoco(Integer tipoPoco) {
		this.tipoPoco = tipoPoco;
	}

	public String getUnidadeGeologica() {
		return unidadeGeologica;
	}

	public void setUnidadeGeologica(String unidadeGeologica) {
		this.unidadeGeologica = unidadeGeologica;
	}

	public Integer getTipoAquifero() {
		return tipoAquifero;
	}

	public void setTipoAquifero(Integer tipoAquifero) {
		this.tipoAquifero = tipoAquifero;
	}

	public String getNomeAquifero() {
		return nomeAquifero;
	}

	public void setNomeAquifero(String nomeAquifero) {
		this.nomeAquifero = nomeAquifero;
	}

	public List<FceCaptacaoSubterranea> getListFceCaptacaoSubterranea() {
		return listFceCaptacaoSubterranea;
	}

	public void setListFceCaptacaoSubterranea(List<FceCaptacaoSubterranea> listFceCaptacaoSubterranea) {
		this.listFceCaptacaoSubterranea = listFceCaptacaoSubterranea;
	}

	public Boolean getBotaoFceCaracteristicaPoco() {
		return botaoFceCaracteristicaPoco;
	}

	public void setBotaoFceCaracteristicaPoco(Boolean botaoFceCaracteristicaPoco) {
		this.botaoFceCaracteristicaPoco = botaoFceCaracteristicaPoco;
	}

	public List<UnidadeGeologicaAflorante> getListaUnidadesGeologicasAflorantes() {
		return listaUnidadesGeologicasAflorantes;
	}

	public void setListaUnidadesGeologicasAflorantes(List<UnidadeGeologicaAflorante> listaUnidadesGeologicasAflorantes) {
		this.listaUnidadesGeologicasAflorantes = listaUnidadesGeologicasAflorantes;
	}

	public List<UnidadeGeologicaAflorante> getListaUnidadesGeologicasAflorantesCompleta() {
		return listaUnidadesGeologicasAflorantesCompleta;
	}

	public void setListaUnidadesGeologicasAflorantesCompleta(List<UnidadeGeologicaAflorante> listaUnidadesGeologicasAflorantesCompleta) {
		this.listaUnidadesGeologicasAflorantesCompleta = listaUnidadesGeologicasAflorantesCompleta;
	}

	public List<UnidadeGeologicaAflorante> getListaUnidadesGeologicasAflorantesSelecionada() {
		return listaUnidadesGeologicasAflorantesSelecionada;
	}

	public void setListaUnidadesGeologicasAflorantesSelecionada(List<UnidadeGeologicaAflorante> listaUnidadesGeologicasAflorantesSelecionada) {
		this.listaUnidadesGeologicasAflorantesSelecionada = listaUnidadesGeologicasAflorantesSelecionada;
	}

	public List<Aquifero> getListaAquifero() {
		return listaAquifero;
	}

	public void setListaAquifero(List<Aquifero> listaAquifero) {
		this.listaAquifero = listaAquifero;
	}

	public List<Aquifero> getListaAquiferoCompleta() {
		return listaAquiferoCompleta;
	}

	public void setListaAquiferoCompleta(List<Aquifero> listaAquiferoCompleta) {
		this.listaAquiferoCompleta = listaAquiferoCompleta;
	}

	public List<Aquifero> getListaAquiferoSelecionado() {
		return listaAquiferoSelecionado;
	}

	public void setListaAquiferoSelecionado(List<Aquifero> listaAquiferoSelecionado) {
		this.listaAquiferoSelecionado = listaAquiferoSelecionado;
	}

	public DataModel<FceCaptacaoSubterraneaCnae> getFceCaptacaoSubterraneaCnaeData() {
		return fceCaptacaoSubterraneaCnaeData;
	}

	public void setFceCaptacaoSubterraneaCnaeData(
			DataModel<FceCaptacaoSubterraneaCnae> fceCaptacaoSubterraneaCnaeData) {
		this.fceCaptacaoSubterraneaCnaeData = fceCaptacaoSubterraneaCnaeData;
	}
	
	public Boolean getRenderCamposCnaeEVazao() {
		return renderCamposCnaeEVazao;
	}

	public void setRenderCamposCnaeEVazao(Boolean renderCamposCnaeEVazao) {
		this.renderCamposCnaeEVazao = renderCamposCnaeEVazao;
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFceCapSub");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioCaptacaoSubterranea");
		RequestContext.getCurrentInstance().execute("fce_cap_subterranea.show();");
	}

	@Override
	protected void prepararDuplicacao() {
		try {
			super.listaFceOutorgaLocalizacaoGeograficaCapSub = new ArrayList<FceOutorgaLocalizacaoGeografica>();
			super.listaOutorgaConcedida = new ArrayList<OutorgaConcedida>();
			for(FceCaptacaoSubterranea fceCaptacaoSubterranea : listFceCaptacaoSubterranea){
				FceCaptacaoSubterranea fceCaptacaoSubterraneaClone = fceCaptacaoSubterranea.clone();
				OutorgaLocalizacaoGeografica outorgaLocGeoOriginal = fceCaptacaoSubterraneaClone.getIdeOutorgaLocalizacaoGeografica();
				FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaDuplicada = super.prepararDuplicacaoFceOutorgaLocalizacaoGeografica(null, outorgaLocGeoOriginal, TipologiaEnum.CAPTACAO_SUBTERRANEA);
				fceCaptacaoSubterraneaClone.setIdeOutorgaLocalizacaoGeografica(null);
				fceCaptacaoSubterraneaClone.setIdeFceOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeograficaDuplicada);
				super.adicionarFceOutorgaLocalizacaoGeografica(TipologiaEnum.CAPTACAO_SUBTERRANEA, fceOutorgaLocalizacaoGeograficaDuplicada);
				listaOutorgaConcedida.add(new OutorgaConcedida(fceOutorgaLocalizacaoGeograficaDuplicada, outorgaLocGeoOriginal));
				Integer index = listFceCaptacaoSubterranea.indexOf(fceCaptacaoSubterranea);
				listFceCaptacaoSubterranea.set(index, fceCaptacaoSubterraneaClone);
			}
			super.tratarPontosListaFceOutorgaLocalizacaoGeografica(TipologiaEnum.CAPTACAO_SUBTERRANEA);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Captação Subterrãnea.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			super.salvarFce();
			for(FceCaptacaoSubterranea fceCaptacaoSubterraneaObj : listFceCaptacaoSubterranea){
				this.fceCaptacaoSubterranea = fceCaptacaoSubterraneaObj;
				verificarDadosHidrogeologicosZero();
				super.duplicacao(this.fceCaptacaoSubterranea.getIdeFceOutorgaLocalizacaoGeografica());
				fceCaptacaoSubterraneaObj.setIdeFce(super.fce);
				carregarDadosCnae();
				this.fceCaptacaoSubterranea.setIdeFceCaptacaoSubterranea(null);//apaga o ID para duplicar a captção superficial
				facade.salvarFceCaptacaoSubterranea(this.fceCaptacaoSubterranea);
				salvarFceCaptacaoSubterraneaCnaeTecnico();
			}
			super.salvarListaOutorgaConcedida();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Captação Subterrânea.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico() {
		try {
			carregarListas();
			carregarListaFceCaptacaoSubterranea();
			super.listaFceOutorgaLocalizacaoGeograficaCapSub = new ArrayList<FceOutorgaLocalizacaoGeografica>();
			for(FceCaptacaoSubterranea fceCaptacaoSubterraneaObj : listFceCaptacaoSubterranea){
				super.listaFceOutorgaLocalizacaoGeograficaCapSub.add(fceCaptacaoSubterraneaObj.getIdeFceOutorgaLocalizacaoGeografica());
			}
			super.fceOutorgaServiceFacade.tratarPontosListaFceOutorgaLocalizacaoGeografica(super.listaFceOutorgaLocalizacaoGeograficaCapSub);
			carregarUploadAbaDadosAdicionais();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Captação Subterrânea.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
}