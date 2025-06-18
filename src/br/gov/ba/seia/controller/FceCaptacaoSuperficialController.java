package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceCaptacaoController;
import br.gov.ba.seia.entity.CaracteristicaCaptacao;
import br.gov.ba.seia.entity.CaracteristicaSistemaCaptacao;
import br.gov.ba.seia.entity.Cnae;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.DominioBarramento;
import br.gov.ba.seia.entity.FceCaptacaoSuperficial;
import br.gov.ba.seia.entity.FceCaptacaoSuperficialCnae;
import br.gov.ba.seia.entity.FceIntervencaoBarragem;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.LocalCaptacao;
import br.gov.ba.seia.entity.OutorgaConcedida;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.TipoValor;
import br.gov.ba.seia.enumerator.CaracteristicaCaptacaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoBarragemEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.FceCaptacaoSuperficialServiceFacade;
import br.gov.ba.seia.facade.FceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.EnquadramentoDocumentoAtoService;
import br.gov.ba.seia.service.FceCaptacaoCnaeService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaFinalidadeService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

/**
 * 04/12/13
 * @author eduardo.fernandes
 */
@Named("fceCaptacaoSuperficialController")
@ViewScoped
public class FceCaptacaoSuperficialController extends BaseFceCaptacaoController implements FceNavegacaoInterface {

	@Inject
	private FceIntervencaoBarragemController barragemController;
 
	@EJB
	private FceCaptacaoSuperficialServiceFacade facade;
	
	@EJB
	private FceCaptacaoCnaeService fceCnaeService;
	
	@EJB
	private FceCaptacaoCnaeService fceCaptacaoCnaeService;
	
	@EJB
	EnquadramentoDocumentoAtoService enquadramentoDocumentoAtoService;
	
	@EJB
	OutorgaLocalizacaoGeograficaFinalidadeService outorgaLocalizacaoGeograficaFinalidade;
	
	@EJB
	private FceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade fceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade;
	
	private Cnae cnaeSecao;
	private List<SelectItem> listaSecao;
	private Cnae cnaeDivisao = new Cnae();
	private List<SelectItem> listaDivisao;
	private Cnae cnaeGrupo = new Cnae();
	private List<SelectItem> listaGrupo;
	private Cnae cnaeClasse = new Cnae();
	private Collection<Cnae> listaClasse;
	private Cnae cnaeSubclasse = new Cnae();
	private Collection<Cnae> listaSubclasse;
	
	private DataModel<FceCaptacaoSuperficialCnae> fceCaptacaoSuperficialCnaeData;

	// TODAS AS ABAS
	private static DocumentoObrigatorio DOC_CAPTACAO_SUPERIFICAL = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_CAPTACAO_SUPERFICIAL.getId());
	private int activeTab;

	private FceCaptacaoSuperficial fceCaptacaoSuperficial;
	private FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaSelecionada;
	private List<FceCaptacaoSuperficial> listaFceCaptacaoSuperficial;

	// ABA CAPTACAO
	private List<CaracteristicaCaptacao> listaCaracteristicaCaptacao;
	private List<CaracteristicaCaptacao> listaCaracteristicaCaptacaoTemp;
	private List<CaracteristicaSistemaCaptacao> listaCaracteristicaSistemaCaptacao;

	// ABA BARRAMENTO
	private static final Integer PARTICULAR = 2;
	private List<DominioBarramento> listaDominioBarramento;

	private TipoValor tipoValorVolume;
	private TipoValor tipoValorVazao;
	private List<TipoValor> listaTipoValor;

	private LocalCaptacao localCaptacaoSelecionada;
	private List<LocalCaptacao> listaLocalCaptacao;
	
	private Boolean renderCamposCnaeEVazao;

	// ABA ADICIONAIS
	private static DocumentoObrigatorio DOC_OBRIGATORIO_UPLOAD = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_CAPTACAO_SUPERFICIAL_ADICIONAIS.getId());

	// BEGIN
	@Override
	public void init(){
		carregarAba();
		verificarEdicao();
		carregarDadosDoRequerimento();
		if(!isFceSalvo()){
			iniciarFce(DOC_CAPTACAO_SUPERIFICAL);
			criarFceOutorgaLocGeo();
		}
		else {
			listarFceOutorgaLocGeo();
			listarFceCaptacaoSuperficial();
		}
	}

	/**
	 * Centralizador de metódos para chamar as listas que vão ser manipuladas nas xhtml.
	 * @author eduardo.fernandes
	 */
	@Override
	public void carregarAba(){
		carregarListaCaracteriscaCaptacao();
		carregarListaCaracteristicaSistemaCaptacao();
		carregarListaDominioBarramento();
		carregarListaTipoValor();
		carregarListaLocalCaptacao();
		
	}

	/**
	 * Método que preenche a lista de "Característica da Captação" da Aba Captação
	 * @author eduardo.fernandes
	 */
	private void carregarListaCaracteriscaCaptacao(){
		try {
			listaCaracteristicaCaptacao = facade.listarCaracteristicaCaptacao();
			armazenarListaCaracteristicaCaptacao();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Características da Captação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Lista que será exibida ao usuário e que será manipulada a depender das respostas dada no Dialog Captação Superficial do NR.
	 * @author eduardo.fernandes
	 */
	private void armazenarListaCaracteristicaCaptacao(){
		listaCaracteristicaCaptacaoTemp = new ArrayList<CaracteristicaCaptacao>();
		listaCaracteristicaCaptacaoTemp.addAll(listaCaracteristicaCaptacao);
	}

	/**
	 * Método que preenche a lista de "Característica do Sistema de Captação" da Aba Captação
	 * @author eduardo.fernandes
	 */
	private void carregarListaCaracteristicaSistemaCaptacao(){
		try {
			listaCaracteristicaSistemaCaptacao = facade.listarCaracteristicaSistemaCaptacao();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Características do Sistema de Captação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que preenche a lista de "Domínio do Barramento" da Aba Barramento
	 * @author eduardo.fernandes
	 */
	private void carregarListaDominioBarramento(){
		try {
			listaDominioBarramento = facade.listarDominioBarramento();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Características da Captação do Barramento Existente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que preenche a lista de "Tipo Valor" (Calculado/Estimado) da Aba Barramento
	 * @author eduardo.fernandes
	 */
	private void carregarListaTipoValor(){
		try {
			listaTipoValor = facade.listarTipoValor();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Tipo Valor.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que preenche a lista de "Local da Captação" da Aba Barramento
	 * @author eduardo.fernandes
	 */
	private void carregarListaLocalCaptacao(){
		try {
			listaLocalCaptacao = facade.listarLocalCaptacao();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Local da Captação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que verifica a existência de um FCE para aquele Requerimento
	 * @author eduardo.fernandes
	 */
	@Override
	public void verificarEdicao(){
		try {
			carregarFceDoRequerente(DOC_CAPTACAO_SUPERIFICAL);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para trazer as informações preenchidas no NR.
	 * @author eduardo.fernandes
	 */
	public void carregarDadosDoRequerimento(){
		super.listarOutorgaLocGeoBy(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL);
	}

	/**
	 * Método que cria uma lista de FceOutorgaLocalizacaoGeografica com cada OutorgaLocalizacaoGeografica daquela Captacão Superficial
	 * @author eduardo.fernandes
	 */
	private void criarFceOutorgaLocGeo(){
		if(Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeograficaCapSup)){
			super.listaFceOutorgaLocalizacaoGeograficaCapSup = new ArrayList<FceOutorgaLocalizacaoGeografica>();
		}
		tratarListaFceOutorgaLocalizacaoGeografica(super.listaOutorgaLocalizacaoGeografica);
	}

	private void tratarListaFceOutorgaLocalizacaoGeografica(List<OutorgaLocalizacaoGeografica> list){
		if(!Util.isNullOuVazio(list)){
			int indexLista = retornarIndex();
			for(int i = 0; i < list.size(); i++){
				super.listaFceOutorgaLocalizacaoGeograficaCapSup.add(new FceOutorgaLocalizacaoGeografica(list.get(i)));
				if(!isFceSalvo()){
					formatarNumVazao(super.listaFceOutorgaLocalizacaoGeograficaCapSup.get(i));
				}
				else {
					indexLista++;
					formatarNumVazao(super.listaFceOutorgaLocalizacaoGeograficaCapSup.get(indexLista));
				}
			}
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 13/04/2015
	 */
	private int retornarIndex() {
		if(!super.listaFceOutorgaLocalizacaoGeograficaCapSup.isEmpty()){
			return super.listaFceOutorgaLocalizacaoGeograficaCapSup.size() - 1;
		} else {
			return 0;
		}
	}

	/**
	 * Detalhamento no corpo do método
	 * @author eduardo.fernandes
	 */
	public void prepararFceCaptacaoSuperficial(){
		try {
			if(Util.isNullOuVazio(listaFceCaptacaoSuperficial)){
				listaFceCaptacaoSuperficial = new ArrayList<FceCaptacaoSuperficial>();
			}
			
			tratarFceCaptacaoSuperficial();
			

			if(Util.isNullOuVazio(fceCaptacaoSuperficial)){
				// Se o FceCaptacaoSuperficial não tiver sido salvo, cria-se um novo usando aquele FceOurogaLocalizacaoGeografica
				fceCaptacaoSuperficial = new FceCaptacaoSuperficial(fceOutorgaLocalizacaoGeograficaSelecionada);
			}
			else {
				// Se já existir o FceCaptacaoSuperficial, carrega-se o DocumentoObrigatorioRequerimento que ele utiliza
				carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(fceCaptacaoSuperficial.getIdeDocumentoObrigatorioRequerimento());
			}
			
			
			renderCamposCnaeEVazao = Boolean.FALSE;
			
		//	List<EnquadramentoDocumentoAto> enquadramentoDocumentoAtoList =  enquadramentoDocumentoAtoService.listarEnquadramentoDocAtoByRequerimentoToFceDispensaOutorga(
		//			fceOutorgaLocalizacaoGeograficaSelecionada.getIdeFce().getIdeRequerimento(), DocumentoObrigatorioEnum.FCE_OUTORGA_CAPTACAO_SUPERFICIAL);
			
		//	if (!Util.isNullOuVazio(enquadramentoDocumentoAtoList)){
			if(fceOutorgaLocalizacaoGeograficaSelecionada.getIdeOutorgaLocalizacaoGeografica() != null){
				List<OutorgaLocalizacaoGeograficaFinalidade> outorgaFinalidades = outorgaLocalizacaoGeograficaFinalidade.obterListaOutorgaLocalizacaoGeograficaFinalidades(
						fceOutorgaLocalizacaoGeograficaSelecionada.getIdeOutorgaLocalizacaoGeografica());
				
				for (OutorgaLocalizacaoGeograficaFinalidade outorga : outorgaFinalidades) {
					if(outorga.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.LAZER_TURISMO.getId() || outorga.getIdeTipoFinalidadeUsoAgua().
							getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.COMERCIO_SERVICOS.getId() ){
						renderCamposCnaeEVazao = Boolean.TRUE;
						break;
					}
		//		}
				}
		
			}else{
				List<FceOutorgaLocalizacaoGeograficaFinalidade> listaFceOutorgaLocalizacaoGeograficaFinalidade = fceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade.listarTipoFinalidadeUsoAguaByFce(fceOutorgaLocalizacaoGeograficaSelecionada);
				fceCaptacaoSuperficial.getIdeFceOutorgaLocalizacaoGeografica().setListaFinalidade(listaFceOutorgaLocalizacaoGeograficaFinalidade);
				for (FceOutorgaLocalizacaoGeograficaFinalidade outorga : listaFceOutorgaLocalizacaoGeograficaFinalidade) {
					if(outorga.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.LAZER_TURISMO.getId() || outorga.getIdeTipoFinalidadeUsoAgua().
							getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.COMERCIO_SERVICOS.getId() ){
						renderCamposCnaeEVazao = Boolean.TRUE;
						break;
					}
				}
			}
			
			if(renderCamposCnaeEVazao){
				
				/*
				 * Verificar se o campo de vazãoCaptacao já está preenchido na tabela
				 * caso não esteja preeenchido, pegar a vazao da outorga selecionada 
				 */
				if(Util.isNullOuVazio(fceCaptacaoSuperficial.getNumVazaoCaptacao())){
					BigDecimal vazaoCaptacao = new BigDecimal(fceOutorgaLocalizacaoGeograficaSelecionada.getMaskNumVazao().replace(".", "") .replace(",", "."));
					fceCaptacaoSuperficial.setNumVazaoCaptacao(vazaoCaptacao);				
				}
				
				//carregarDadosCnae();
				List<FceCaptacaoSuperficialCnae> listaFce = new ArrayList<FceCaptacaoSuperficialCnae>();
				listaFce = (List<FceCaptacaoSuperficialCnae>) fceCaptacaoCnaeService.listarCnaeByFce(fceCaptacaoSuperficial);
				this.fceCaptacaoSuperficialCnaeData = new ListDataModel<FceCaptacaoSuperficialCnae>(listaFce);
				
				CnaeController cnae = (CnaeController) SessaoUtil.recuperarManagedBean("#{cnaeController}", CnaeController.class);
				cnae.setFceCaptacaoSuperficialCnaeData(fceCaptacaoSuperficialCnaeData);
				cnae.setFceCaptacaoSuperficial(fceOutorgaLocalizacaoGeograficaSelecionada.getIdeFceCaptacaoSuperficial());
				if(fceCaptacaoSuperficialCnaeData.getRowCount() > 0){
					cnae.setFlagTableCnae(true);
				}else{
					cnae.setFlagTableCnae(false);
				}
				
			}
			
			restauraListaCaracteristicaCaptacao();
			
			// Caso a pergunta tenha sido respondida, iremos remover um dos itens da listaCaracteristicaCaptacaoTemp a depender da resposta dada.
			if(isPergunta121Respondida()){
				if(isBarragemContruida()){
					// Se o usuário tiver respondido no Dialog Captação Superficial que "A Barragem está construída", remove-se a opção "Em barragem a construír" da Característica da Captção
					listaCaracteristicaCaptacaoTemp.remove(new CaracteristicaCaptacao(CaracteristicaCaptacaoEnum.BARRAGEM_A_CONSTRUIR.getId()));
				}
				else {
					// Se o usuário tiver negado no Dialog Captação Superficial que "A Barragem está construída", remove-se a opção "Em barragem existente" da Característica da Captção
					listaCaracteristicaCaptacaoTemp.remove(new CaracteristicaCaptacao(CaracteristicaCaptacaoEnum.BARRAGEM_EXISTENTE.getId()));
				}
			}
			activeTab = 0;
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " os dados do FCE de Captação Superficial.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
		Html.atualizar("fceCapSuper");
	}

	/**
	 * listaCaracteristicaCaptacaoTemp recebe todos os dados de listaCaracteristicaCaptacao.
	 * @author eduardo.fernandes
	 * @see #listaCaracteristicaCaptacao
	 */
	private void restauraListaCaracteristicaCaptacao(){
		listaCaracteristicaCaptacaoTemp.clear();
		listaCaracteristicaCaptacaoTemp.addAll(listaCaracteristicaCaptacao);
	}

	/**
	 * Método que verifica se aquele FceCaptacaoSuperficial já foi salvo com um FceOutorgaLocalizacaoGeografica, caso positivo utiliza-se essa FceCaptacaoSuperficial.
	 * @author eduardo.fernandes
	 * @see #fceCaptacaoSuperficial
	 */
	private void tratarFceCaptacaoSuperficial(){
		if(!Util.isNullOuVazio(listaFceCaptacaoSuperficial)){
			for(FceCaptacaoSuperficial fceCaptacaoSuperficialTEMP : listaFceCaptacaoSuperficial){
				if(!Util.isNullOuVazio(fceCaptacaoSuperficialTEMP.getIdeFceOutorgaLocalizacaoGeografica())){
					if(fceCaptacaoSuperficialTEMP.getIdeFceOutorgaLocalizacaoGeografica().getIdeFceOutorgaLocalizacaoGeografica().intValue() == fceOutorgaLocalizacaoGeograficaSelecionada.getIdeFceOutorgaLocalizacaoGeografica().intValue()){
						fceCaptacaoSuperficial = fceCaptacaoSuperficialTEMP;
						fceCaptacaoSuperficial.setEdicao(true);
						break;
					}
				}
			}
		}
	}

	// MODO EDICAO
	private void listarFceOutorgaLocalizacaoGeografica() throws Exception{
		super.listaFceOutorgaLocalizacaoGeograficaCapSup = super.fceOutorgaServiceFacade.listarFceOutorgaLocalizacaoGeograficaByIdeFce(super.fce);
	}
	
	private void listarFceOutorgaLocalizacaoGeograficaDAnaliseTecnica() throws Exception{
		super.listaFceOutorgaLocalizacaoGeograficaCapSup = super.listarFceOutorgaLocalizacaoGeograficaDaAnaliseTecnica();
	}

	/**
	 * Método que retorna os FceOutorgaLocalizacaoGeografica já salvos.
	 * @author eduardo.fernandes
	 */
	public void listarFceOutorgaLocGeo(){
		try {
			listarFceOutorgaLocalizacaoGeografica();
			for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : super.listaFceOutorgaLocalizacaoGeograficaCapSup){
				calcularVazaoHora(fceOutorgaLocalizacaoGeografica);
				formatarNumVazao(fceOutorgaLocalizacaoGeografica);
			}
			if(isListaOutorgaLocGeoMaiorQueFceLocGeo()){
				tratarListaFceOutorgaLocalizacaoGeografica();
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " os dados do requerimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que formata a String [maskNumVazao] exibida na Aba Dados do Requerimento
	 * @author eduardo.fernandes
	 * @param fceOutorgaLocalizacaoGeografica
	 * @see #fceOutorgaLocalizacaoGeografica.getMaskNumVazao()
	 */
	private void formatarNumVazao(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica){
		DecimalFormat format = new DecimalFormat("##,###,###.##");
		format.setMinimumFractionDigits(2);
		if(super.isFceTecnico()){
			fceOutorgaLocalizacaoGeografica.setMaskNumVazao(format.format(fceOutorgaLocalizacaoGeografica.getNumVazao()));
		} else {
			fceOutorgaLocalizacaoGeografica.setMaskNumVazao(format.format(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getNumVazao()));
		}
	}

	/**
	 * Método que cria FceOutorgaLocalizacaoGeografica para as OutorgaLocalizacaoGeografica que não foram utilizadas ainda.
	 * @author eduardo.fernandes
	 * @see #criarFceOutorgaLocGeo()
	 */
	private void tratarListaFceOutorgaLocalizacaoGeografica(){
		List<OutorgaLocalizacaoGeografica> listaTEMP = new ArrayList<OutorgaLocalizacaoGeografica>();
		listaTEMP.addAll(super.listaOutorgaLocalizacaoGeografica);
		for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : super.listaFceOutorgaLocalizacaoGeograficaCapSup){
			if(super.listaOutorgaLocalizacaoGeografica.contains(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica())){
				listaTEMP.remove(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica());
			}
		}
		tratarListaFceOutorgaLocalizacaoGeografica(listaTEMP);
	}

	/**
	 * Método que busca os FceCaptacaoSuperifical de acordo com os FceOutorgaLocalizacaoGeografica salvos no banco.
	 * @author eduardo.fernandes
	 * @see #fceCaptacaoSuperficial.getIdeFceOutorgaLocalizacaoGeografica()
	 */
	public void listarFceCaptacaoSuperficial(){
		listaFceCaptacaoSuperficial = new ArrayList<FceCaptacaoSuperficial>();
		
		if(!Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeograficaCapSup)){
			for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : super.listaFceOutorgaLocalizacaoGeograficaCapSup){
				
				try {
					FceCaptacaoSuperficial fceCaptacaoSuperficial = facade.buscarFceCaptacaoSuperficialBy(fceOutorgaLocalizacaoGeografica);

					if(!Util.isNullOuVazio(fceCaptacaoSuperficial)){
						listaFceCaptacaoSuperficial.add(fceCaptacaoSuperficial);
					} else { //#10749
						listaFceCaptacaoSuperficial.add(
							new FceCaptacaoSuperficial(
									fceOutorgaLocalizacaoGeografica, new DocumentoObrigatorioRequerimento(0), 
									new CaracteristicaCaptacao(0), new CaracteristicaSistemaCaptacao(0)));
					}
					
					if(!Util.isNullOuVazio(super.listaOutorgaLocalizacaoGeografica) 
							&& !Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica())){
						
						for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : super.listaOutorgaLocalizacaoGeografica){
							if(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().equals(outorgaLocalizacaoGeografica)){
								fceOutorgaLocalizacaoGeografica.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
							}
						}
					}
				} catch (Exception e) {
					MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Captação Superficial.");
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		}
	}
	// FIM MODO EDICAO

	// METODOS ATIVADOS PELO USUARIO
	/**
	 *
	 * @author eduardo.fernandes
	 */
	public void confirmar(){
		if(validarFceLocalizacaoGeografica(fceOutorgaLocalizacaoGeograficaSelecionada)){
			try {
				calcularVazaoHora(fceOutorgaLocalizacaoGeograficaSelecionada);
				salvarFceOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeograficaSelecionada);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Captação Superficial.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
			if(isFceOutorgaLocGeoSalva()){
				fceOutorgaLocalizacaoGeograficaSelecionada.setConfirmado(true);
			}
		}
	}

	/**
	 * Método que cálcula a vazão por hora de acordo com os dados fornecidos no NR e na Aba Dados do Requerimento
	 * @see #fceOutorgaLocalizacaoGeografica.getNumTempoCaptacao()
	 * @see #br.gov.ba.novoRequerimento.entity.OutorgaLocalizacaoGeografica.getNumVazao()
	 */
	private void calcularVazaoHora(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica){
		if(super.isFceTecnico()){
			if(!Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getNumVazao()) && !Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getNumTempoCaptacao())) {
				fceOutorgaLocalizacaoGeografica.setVazaoHora(fceOutorgaLocalizacaoGeografica.getNumVazao().doubleValue()/fceOutorgaLocalizacaoGeografica.getNumTempoCaptacao());
			}
		} else {
			if(!Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getNumVazao()) && !Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getNumTempoCaptacao())) {
				fceOutorgaLocalizacaoGeografica.setVazaoHora(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getNumVazao().doubleValue()/fceOutorgaLocalizacaoGeografica.getNumTempoCaptacao());
			}
		}
	}

	/**
	 * Método que permite edição dos campos na Aba Dados do Requerimento
	 * @author eduardo.fernandes
	 */
	public void editar(){
		fceOutorgaLocalizacaoGeograficaSelecionada.setConfirmado(false);
		fceOutorgaLocalizacaoGeograficaSelecionada.setVazaoHora(null);
	}

	/**
	 * Método para salvar o Dialog do FCE - Captação Superficial
	 * @author eduardo.fernandes
	 */
	public void salvarDialogFceCaptacaoSuperficial(){
		if(isFceCaptacaoSuperficialValida()){
			try {
				salvarFceOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeograficaSelecionada);
				salvarAbaAdicionais();
				salvarFceCaptacaoSuperficial();
				salvarFceCaptacaoSuperficialCnae();
				// Caso o usuário diga que a Característica da Captação é "Em barragem a ser construída" e não exista um FceIntervencaoBarragem salvo no banco, exibe-se a mensagem de alerta.
				if(isBarragemToConstruir() && !isFceIntervencaoBarragemSalva()){
					JsfUtil.addWarnMessage(Util.getString("fce_outorga_ajuda_007"));
				}
				if(!fceCaptacaoSuperficial.isEdicao()){
					super.exibirMensagem001();
				}
				else {
					super.exibirMensagem002();
				}
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + "o FCE - Captação Superficial.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
			RequestContext.getCurrentInstance().execute("fceCapSuper.hide()");
			if(!listaFceCaptacaoSuperficial.contains(fceCaptacaoSuperficial)){
				listaFceCaptacaoSuperficial.add(fceCaptacaoSuperficial);
			}
		}
	}

	private void salvarFceCaptacaoSuperficialCnae() {
		if(!Util.isNullOuVazio(fceCaptacaoSuperficialCnaeData)){
			for (FceCaptacaoSuperficialCnae fceDataModel : fceCaptacaoSuperficialCnaeData) {
				try {
					fceCnaeService.salvarOuAtualizarCaptacaoSuperficialCnae(fceDataModel);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}
		}
	}

	
	private void salvarFceCaptacaoSuperficialCnaeTecnico() {
		if(!Util.isNullOuVazio(fceCaptacaoSuperficialCnaeData)){
			for (FceCaptacaoSuperficialCnae fceDataModel : fceCaptacaoSuperficialCnaeData) {
				try {
					fceDataModel.setIdeFceCaptacaoSupercial(fceCaptacaoSuperficial);
					fceDataModel.setIdeFceCaptacaoSuperficialCnae(null);
					fceCnaeService.salvarOuAtualizarCaptacaoSuperficialCnae(fceDataModel);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}
		}
	}
	
	/**
	 * Método que salva o FceCaptacaoSuperficial
	 * @author eduardo.fernandes
	 */
	private void salvarFceCaptacaoSuperficial(){
		try {
			//#9905 - remove a Vazão da captação
			fceCaptacaoSuperficial.setNumVazaoCaptacao(null);
			
			if(!Util.isNull(getDocumentoUpado())){
				fceCaptacaoSuperficial.setIdeDocumentoObrigatorioRequerimento(getDocumentoUpado());
			}
			
			facade.salvarFceCaptacaoSuperficial(fceCaptacaoSuperficial);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " os dados da Captação Superficial.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para verificar se as abas foram preenchidas devidamente.
	 * @author eduardo.fernandes
	 * @return boolean
	 */
	private boolean isFceCaptacaoSuperficialValida(){
		boolean valido = true;
		if(!validarAbaCaptacao()){
			activeTab = 0;
			valido = false;
		}
		if(isBarragemExistente() && !validarAbaBarramento()){
			activeTab = 1;
			valido = false;
		}
		if(!validarAbaAdicionais()){
			valido = false;
		}
		return valido;
	}

	/**
	 * Método que salva o documento adicional enviado pelo usuário
	 * @author eduardo.fernandes
	 */
	private void salvarAbaAdicionais(){
		try {
			salvarDocumentoAdicional(requerimento, DOC_OBRIGATORIO_UPLOAD);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que finaliza o FCE - Captação Superficial
	 * @author eduardo.fernandes
	 */
	@Override
	public void finalizar(){
		if(isPossivelFinalizar()) {
			try {
				facade.finalizar(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_finalizar") + " o FCE - Captação Superficial.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		super.concluirFce();
		
		if(super.isFceTecnico()){
			JsfUtil.addWarnMessage(Util.getString("analise_tecnica_msg_verificar_finalidade"));	
		}
		
		fecharDialogExibirMensagem();
		limpar();
	}

	/**
	 * Método que verifica se é possível finalizar o FCE - Captação Superficial
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean isPossivelFinalizar(){
		boolean valido = true;
		if(!validarAbaDadosRequerimento()){
			valido = false;
		}
		else {
			if(!Util.isNullOuVazio(listaFceCaptacaoSuperficial)){
				if(super.listaFceOutorgaLocalizacaoGeograficaCapSup.size() > listaFceCaptacaoSuperficial.size()){
					JsfUtil.addErrorMessage(Util.getString("fce_cap_super_erro"));
					valido = false;
				}
			}
			else {
				JsfUtil.addErrorMessage(Util.getString("fce_cap_super_erro"));
				valido = false;
			}
		}
		return valido;
	}

	/**
	 * Método que fecha a Aba Dados do Requerimento e exibe os alertas para preenchimento do FCE - Intervenção Barragem (caso ele não tenha sido preenchido)
	 * e imprimir relatório.
	 * @author eduardo.fernandes
	 */
	private void fecharDialogExibirMensagem(){
		JsfUtil.addSuccessMessage(Util.getString("msg_generica_sucesso_finalizar"));
		RequestContext.getCurrentInstance().execute("cap_sup.hide()");
		RequestContext.getCurrentInstance().execute("rel_capSuper.show()");
		verificarAlertaFceBarragem();
	}

	/**
	 * Verifica se existe um FceCaptacaoSuperficial que necessita de preenchimento do FceIntervencaoBarragem, ignorando o alerta se o FceIntervencaoBarragem já tiver salvo.
	 * @author eduardo.fernandes
	 */
	private void verificarAlertaFceBarragem(){
		for(FceCaptacaoSuperficial fceCaptacaoSuperficialObj : listaFceCaptacaoSuperficial){
			this.fceCaptacaoSuperficial = fceCaptacaoSuperficialObj;
			if(isBarragemToConstruir() && !isFceIntervencaoBarragemSalva()){
				RequestContext.getCurrentInstance().execute("alertaFceBarragem.show()");
				break;
			}
		}
	}

	/**
	 * Método que limpa os dados manipulados em FceController e FceOutorgaController
	 * @author eduardo.fernandes
	 */
	@Override
	public void limpar(){
		super.limparDadosOutorga();
		super.limparFce();
		if(!Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeograficaCapSup)){
			super.listaFceOutorgaLocalizacaoGeograficaCapSup.clear();
		}
		if(!Util.isNullOuVazio(listaFceCaptacaoSuperficial)){
			listaFceCaptacaoSuperficial.clear();
		}
	}

	/**
	 * Método que limpa os objetos trabalhados na tela.
	 * @author eduardo.fernandes
	 */
	public void limparDialog(){
		fceOutorgaLocalizacaoGeograficaSelecionada = null;
		fceCaptacaoSuperficial = null;
		limparDocumentoUpado();
	}

	/**
	 * Método que permite a abertura do FCE - Intervencao Barragem
	 * @author eduardo.fernandes
	 */
	public void prepararFceIntervencaoBarragem(){
		barragemController.setRequerimento(requerimento);
		barragemController.init();
		atualizarDialogTipoBarragem();
	}

	private void atualizarDialogTipoBarragem(){
		Collection<String> updates = new ArrayList<String>();
		updates.add("tipoBarragem");
		updates.add("formIntervencaoBarragem:tableOutorgaLocGeo");
		RequestContext.getCurrentInstance().addPartialUpdateTargets(updates);
	}

	/**
	 * Método que desmarca a opção "Calculado ou Estimado" quando o usuário não informa o "Volume Máximo Acumulado" na Aba Barramento
	 * @author eduardo.fernandes
	 */
	public void anularTipoValorVolume(){
		if(isValorMaximoAcumuladoNull()){
			fceCaptacaoSuperficial.setTipoValorVolume(null);
		}
	}

	/**
	 * Método que desmarca a opção "Calculado ou Estimado" quando o usuário não informa o "Vazão Regularizada" na Aba Barramento
	 * @author eduardo.fernandes
	 */
	public void anularTipoValorVazao(){
		if(isVazaoRegularizadaNull()){
			fceCaptacaoSuperficial.setTipoValorVazao(null);
		}
	}

	// RENDEREDS/BOOLEANS CONTROLADORES
	public boolean isBarragemExistente(){
		if(!Util.isNull(fceCaptacaoSuperficial)){
			return !Util.isNullOuVazio(fceCaptacaoSuperficial.getCaracteristicaCaptacao()) && fceCaptacaoSuperficial.getCaracteristicaCaptacao().getIdeCaracteristicaCaptacao().intValue() == CaracteristicaCaptacaoEnum.BARRAGEM_EXISTENTE.getId().intValue();
		}
		else {
			return false;
		}
	}

	/**
	 * Método que verifica se a opção selecionada na Aba Captação foi "Em barragem a ser construída"
	 * @author eduardo.fernandes
	 * @return boolean
	 */
	public boolean isBarragemToConstruir(){
		return !Util.isNullOuVazio(fceCaptacaoSuperficial.getCaracteristicaCaptacao()) && fceCaptacaoSuperficial.getCaracteristicaCaptacao().getIdeCaracteristicaCaptacao().intValue() == CaracteristicaCaptacaoEnum.BARRAGEM_A_CONSTRUIR.getId().intValue();
	}

	/**
	 * Método que verifica se a opção selecionada na Aba Barramento foi "Particular"
	 * @author eduardo.fernandes
	 * @return boolean
	 */
	public boolean isCaptacaoParticular(){
		return !isNenhumDominioBarramentoEscolhido() && fceCaptacaoSuperficial.getIdeDominioBarramento().getIdeDominioBarramento().intValue() == PARTICULAR;
	}

	public boolean isNenhumDominioBarramentoEscolhido(){
		return !Util.isNull(fceCaptacaoSuperficial) && Util.isNullOuVazio(fceCaptacaoSuperficial.getIdeDominioBarramento());
	}

	public boolean isPontoComRio(FceOutorgaLocalizacaoGeografica fceCaptacaoSuperficialLocalizacaoGeografica){
		return !Util.isNullOuVazio(fceCaptacaoSuperficialLocalizacaoGeografica) && !Util.isNullOuVazio(fceCaptacaoSuperficialLocalizacaoGeografica.getNomRio());
	}

	public boolean isFceLocGeoConfirmada(FceOutorgaLocalizacaoGeografica fceLocalizacaoGeografica){
		return !Util.isNullOuVazio(fceLocalizacaoGeografica) && fceLocalizacaoGeografica.isConfirmado();
	}

	public boolean isUnicaCaptacao(){
		return !Util.isNullOuVazio(super.listaOutorgaLocalizacaoGeografica) && super.listaOutorgaLocalizacaoGeografica.size() == 1;
	}

	/**
	 * Método que verifica se o FceOutorgaLocalizacaoGeografica tem Id.
	 * @return !Util.isNullOuVazio(fceOutorgaLocalizacaoGeograficaSelecionada.getIdeFceOutorgaLocalizacaoGeografica())
	 * @author eduardo.fernandes
	 */
	private boolean isFceOutorgaLocGeoSalva(){
		return !Util.isNullOuVazio(fceOutorgaLocalizacaoGeograficaSelecionada.getIdeFceOutorgaLocalizacaoGeografica());
	}

	public boolean isValorMaximoAcumuladoNull(){
		return Util.isNullOuVazio(fceCaptacaoSuperficial.getNumValorMaximoAcumulado()) && !isCaptacaoParticular();
	}

	public boolean isVazaoRegularizadaNull(){
		return Util.isNullOuVazio(fceCaptacaoSuperficial.getNumVazaoRegularizada()) && !isCaptacaoParticular();
	}

	/**
	 * Método que verifica se existe um FceIntervencaoBarragem com aquele IdeOutorgaLocalizacaoGeografica igual ao encontrado no FceCaptacaoSuperficial
	 * @return !Util.isNullOuVazio(fceIntervencaoBarragem)
	 * @see #buscarFceIntervencaoBarragemByIdeOutorgaLocGeo()
	 */
	public boolean isFceIntervencaoBarragemSalva(){
		return !Util.isNullOuVazio(fceCaptacaoSuperficial) && !Util.isNullOuVazio(buscarFceIntervencaoBarragemByIdeOutorgaLocGeo());
	}

	/**
	 * Método que verifica se a pergunta 1.2.1. foi respondida no Dialog Captação Superficial NR
	 * @return !Util.isNullOuVazio(perguntaRequerimento)
	 */
	private boolean isPergunta121Respondida(){
		return !Util.isNullOuVazio(buscarRespostaPergunta121(fceOutorgaLocalizacaoGeograficaSelecionada.getIdeOutorgaLocalizacaoGeografica()));
	}

	/**
	 * Método que verifica a resposta da pergunta 1.2.1. do Dialog Captação Superficial NR
	 * @return pergunta.getIndResposta()
	 */
	public boolean isBarragemContruida(){
		PerguntaRequerimento pergunta = buscarRespostaPergunta121(fceOutorgaLocalizacaoGeograficaSelecionada.getIdeOutorgaLocalizacaoGeografica());
		return pergunta.getIndResposta();
	}

	public boolean isListaCaracteristicaCaptacaoComBarragem(){
		return !Util.isNullOuVazio(listaCaracteristicaCaptacaoTemp) && listaCaracteristicaCaptacaoTemp.contains(new CaracteristicaCaptacao(2));
	}

	public boolean isTipoBarragemRegularizada(){
		return !Util.isNullOuVazio(fceOutorgaLocalizacaoGeograficaSelecionada.getIdeOutorgaLocalizacaoGeografica()) 
				&& !Util.isNullOuVazio(fceOutorgaLocalizacaoGeograficaSelecionada.getIdeOutorgaLocalizacaoGeografica().getIdeTipoBarragem())
				&& fceOutorgaLocalizacaoGeograficaSelecionada.getIdeOutorgaLocalizacaoGeografica().getIdeTipoBarragem().getIdeTipoBarragem().intValue() == TipoBarragemEnum.REGULARIZACAO.getId().intValue();
	}

	/**
	 * Método que verifica se a Lista de OutorgaLocalizacaoGeografica vinda do NR é maior que a Lista de FceOutorgaLocalizacaoGeografica salva.
	 * @author eduardo.fernandes
	 * @return
	 */
	private boolean isListaOutorgaLocGeoMaiorQueFceLocGeo(){
		return super.listaOutorgaLocalizacaoGeografica.size() > super.listaFceOutorgaLocalizacaoGeograficaCapSup.size();
	}
	// FIM DOS RENDEREDS/BOOLEANS CONTROLADORES

	/**
	 * Método que busca o FCE - Intervenção Barragem para aquela OutorgaLocalizacaoGeografica
	 * @return fceIntervencaoBarragem
	 */
	private FceIntervencaoBarragem buscarFceIntervencaoBarragemByIdeOutorgaLocGeo(){
		try {
			if(!Util.isNullOuVazio(fceOutorgaLocalizacaoGeograficaSelecionada)){
				FceIntervencaoBarragem fceIntervencaoBarragem = facade.buscarFceIntervencaoBarragem(fceOutorgaLocalizacaoGeograficaSelecionada);
				return fceIntervencaoBarragem;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return null;
	}

	/**
	 * Método que retorna PerguntaRequerimento do Dialog Captação Superficial daquela OutorgaLocaizacaoGeografica
	 * @param outorgaLocalizacaoGeografica
	 * @return perguntaRequerimento
	 */
	private PerguntaRequerimento buscarRespostaPergunta121(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica){
		try {
			if(!Util.isNullOuVazio(outorgaLocalizacaoGeografica)){
				return facade.buscarPerguntaRequerimento(super.requerimento, outorgaLocalizacaoGeografica);
			}
			return null;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	// VALIDAR, CONTOLAR, AVANCAR E VOLTAR ABAS
	/**
	 * @param event
	 * @author eduardo.fernandes
	 */
	@Override
	public void controlarAbas(TabChangeEvent event) {
		if("capSuperCaptacao".equals(event.getTab().getId())){
			activeTab = 0;
		}
		else if("capSuperBarramento".equals(event.getTab().getId())){
			activeTab = 1;
		}
		else if("capSuperAdicionais".equals(event.getTab().getId())){
			if(!isListaCaracteristicaCaptacaoComBarragem()){
				activeTab = 1;
			}
			else {
				activeTab = 2;
			}
		}
	}

	/**
	 * Método que vai validar a ABA ATIVA e permitir, ou não, que ele navegue entre as abas.
	 * @param 0 - VOLTAR
	 * @param 1 - AVANÇAR
	 * @author eduardo.fernandes
	 */
	@Override
	public boolean validarAba(){
		switch (activeTab) {
		case 0:
			return validarAbaCaptacao();
		case 1:
			return validarAbaBarramento();
		case 2:
			return validarAbaAdicionais();
		default:
			return false;
		}
	}



	/**
	 * @see avancarOuVoltar(int botaoEscolhido)
	 * @author eduardo.fernandes
	 */
	@Override
	public void voltarAba(){
		if(activeTab > 0){
			activeTab --;
			if(isListaCaracteristicaCaptacaoComBarragem() && !isBarragemExistente() && activeTab == 1){
				activeTab --;
			}
			if(!super.isFceSalvo() && activeTab == 1){
				activeTab --;
			}
		}
	}

	/**
	 * @see avancarOuVoltar(int botaoEscolhido)
	 * @author eduardo.fernandes
	 */
	@Override
	public void avancarAba(){
		if(validarAba() && activeTab < 3){
			activeTab ++;
			if(isListaCaracteristicaCaptacaoComBarragem() && !isBarragemExistente() && activeTab == 1){
				activeTab ++;
			}
		}
	}

	// METODOS DE VALIDACAO DAS ABAS
	/**
	 * Método que verifica se a Aba Dados do Requerimento está válida.
	 * @return boolean
	 */
	private boolean validarAbaDadosRequerimento(){
		boolean valido = true;
		if(!Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeograficaCapSup)){
			for(FceOutorgaLocalizacaoGeografica fceLocalizacaoGeografica : super.listaFceOutorgaLocalizacaoGeograficaCapSup){
				if(!validarFceLocalizacaoGeografica(fceLocalizacaoGeografica)){
					valido = false;
					break;
				}
				else {
					if(!fceLocalizacaoGeografica.isConfirmado()){
						valido = false;
						JsfUtil.addErrorMessage(Util.getString("fce_cap_super_obrigatorio_confirmar"));
						break;
					}
				}
			}
		} else {
			valido = false;
		}
		return valido;
	}

	/**
	 * Verifica se o objeto foi preenchido corretamente.
	 * @param fceLocalizacaoGeografica
	 * @return boolean
	 */
	private boolean validarFceLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica fceLocalizacaoGeografica){
		boolean valido = true;
		if(Util.isNullOuVazio(fceLocalizacaoGeografica.getNomRio())){
			JsfUtil.addErrorMessage("O nome do rio " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(super.isFceTecnico() && Util.isNullOuVazio(fceLocalizacaoGeografica.getNumVazao())){
			JsfUtil.addErrorMessage("Vazão máxima " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceLocalizacaoGeografica.getNumTempoCaptacao())){
			JsfUtil.addErrorMessage("O tempo de captação " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		else if (fceLocalizacaoGeografica.getNumTempoCaptacao() > 24){
			JsfUtil.addErrorMessage(Util.getString("fce_captacao_subteranea_tempo_captacao_info_0034"));
			valido = false;
		}
		return valido;
	}

	private boolean validarAbaCaptacao(){
		boolean valido = true;
		if(Util.isNullOuVazio(fceCaptacaoSuperficial.getCaracteristicaCaptacao())){
			JsfUtil.addErrorMessage("A característica da captação " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceCaptacaoSuperficial.getCaracteristicaSistemaCaptacao())){
			JsfUtil.addErrorMessage("A característica do sistema de captação " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(!Util.isNullOuVazio(fceCaptacaoSuperficialCnaeData) && fceCaptacaoSuperficialCnaeData.getRowCount()<1){
			JsfUtil.addErrorMessage("O campo CNAE " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		return valido;
	}

	private boolean validarAbaBarramento(){
		boolean valido = true;
		if(Util.isNullOuVazio(fceCaptacaoSuperficial.getIdeDominioBarramento())){
			JsfUtil.addErrorMessage("O domínio do barramento " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		else if(isCaptacaoParticular()){
			if(Util.isNullOuVazio(fceCaptacaoSuperficial.getNumValorMaximoAcumulado())){
				JsfUtil.addErrorMessage("O volume máximo acumulado " + Util.getString("msg_generica_null_ou_vazio"));
				valido = false;
			}
			if(Util.isNullOuVazio(fceCaptacaoSuperficial.getTipoValorVolume())){
				JsfUtil.addErrorMessage(Util.getString("fce_outorga_inf0026") + " o volume máximo acumulado.");
				valido = false;
			}
			if(isTipoBarragemRegularizada()){
				if(Util.isNullOuVazio(fceCaptacaoSuperficial.getNumVazaoRegularizada())){
					JsfUtil.addErrorMessage("A vazão regularizada " + Util.getString("msg_generica_null_ou_vazio"));
					valido = false;
				}
				if(Util.isNullOuVazio(fceCaptacaoSuperficial.getTipoValorVazao())){
					JsfUtil.addErrorMessage(Util.getString("fce_outorga_inf0026") + " a vazão regularizada.");
					valido = false;
				}
			}
		}
		else {
			if(!isValorMaximoAcumuladoNull() && Util.isNullOuVazio(fceCaptacaoSuperficial.getTipoValorVolume())){
				JsfUtil.addErrorMessage(Util.getString("fce_outorga_inf0026") + " o volume máximo acumulado.");
				valido = false;
			}
			else if(isValorMaximoAcumuladoNull()){
				fceCaptacaoSuperficial.setNumValorMaximoAcumulado(null);
			}
			if(isTipoBarragemRegularizada() && !isVazaoRegularizadaNull() && Util.isNullOuVazio(fceCaptacaoSuperficial.getTipoValorVazao())){
				JsfUtil.addErrorMessage(Util.getString("fce_outorga_inf0026") + " a vazão regularizada.");
				valido = false;
			}
			else if(isTipoBarragemRegularizada() && isVazaoRegularizadaNull()){
				fceCaptacaoSuperficial.setNumVazaoRegularizada(null);
			}
		}
		if(Util.isNullOuVazio(fceOutorgaLocalizacaoGeograficaSelecionada.getLocalCaptacao())){
			JsfUtil.addErrorMessage("O local da captação " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		return valido;
	}

	private boolean validarAbaAdicionais(){
		if(!isArquivoUpado()){
			JsfUtil.addErrorMessage(Util.getString("fce_outorga_obrigatorio_upload"));
			return false;
		}
		return true;
	}
	// FIM DOS METODOS DE VALIDACAO

	// METODOS PARA LIMPAR AS ABAS
	public void changeCaracteristicaCaptacao(ValueChangeEvent event){
		if(!Util.isNullOuVazio(event.getOldValue())) {
			if(event.getOldValue().equals(new CaracteristicaCaptacao(CaracteristicaCaptacaoEnum.BARRAGEM_EXISTENTE.getId()))){
				RequestContext.getCurrentInstance().execute("alertaLimparAbaBarragem.show()");
			}
		}
		event = null;
	}

	public void changeDominioBarramento(ValueChangeEvent event){
		if(!Util.isNullOuVazio(event.getOldValue())) {
			if(event.getOldValue().equals(new DominioBarramento(PARTICULAR))){
				if(Util.isNullOuVazio(fceCaptacaoSuperficial.getNumValorMaximoAcumulado())){
					fceCaptacaoSuperficial.setTipoValorVolume(null);
				}
			}
		}
	}

	public void limparAbaBarramento(){
		fceCaptacaoSuperficial.setIdeDominioBarramento(null);
		fceCaptacaoSuperficial.setNumValorMaximoAcumulado(null);
		fceCaptacaoSuperficial.setNumVazaoRegularizada(null);
		fceCaptacaoSuperficial.setTipoValorVazao(null);
		fceCaptacaoSuperficial.setTipoValorVolume(null);
		fceOutorgaLocalizacaoGeograficaSelecionada.setLocalCaptacao(null);
	}
	// FIM DOS METODOS PARA LIMPAR AS ABAS

	public void manterEmBarragemExistente(){
		fceCaptacaoSuperficial.setCaracteristicaCaptacao(new CaracteristicaCaptacao(CaracteristicaCaptacaoEnum.BARRAGEM_EXISTENTE.getId()));
	}

	// STREAMs
	public StreamedContent getDadosAdicionais() {
		try {
			return getDadosAdicionais("Informacoes_Adicionais_FCE_Captacao_Superficial.doc", "Informações Adicionais - FCE Captação Superficial.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOC_CAPTACAO_SUPERIFICAL);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Captação Superficial.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	// GETTERS && SETTERS
	public TipoValor getTipoValorVolume() {
		return tipoValorVolume;
	}

	public void setTipoValorVolume(TipoValor tipoValorVolume) {
		this.tipoValorVolume = tipoValorVolume;
	}

	public TipoValor getTipoValorVazao() {
		return tipoValorVazao;
	}

	public void setTipoValorVazao(TipoValor tipoValorVazao) {
		this.tipoValorVazao = tipoValorVazao;
	}

	public LocalCaptacao getLocalCaptacaoSelecionada() {
		return localCaptacaoSelecionada;
	}

	public void setLocalCaptacaoSelecionada(LocalCaptacao localCaptacaoSelecionada) {
		this.localCaptacaoSelecionada = localCaptacaoSelecionada;
	}

	public List<CaracteristicaSistemaCaptacao> getListaCaracteristicaSistemaCaptacao() {
		return listaCaracteristicaSistemaCaptacao;
	}

	public List<DominioBarramento> getListaDominioBarramento() {
		return listaDominioBarramento;
	}

	public List<TipoValor> getListaTipoValor() {
		return listaTipoValor;
	}

	public List<LocalCaptacao> getListaLocalCaptacao() {
		return listaLocalCaptacao;
	}

	public FceCaptacaoSuperficial getFceCaptacaoSuperficial() {
		return fceCaptacaoSuperficial;
	}

	public void setFceCaptacaoSuperficial(FceCaptacaoSuperficial fceCaptacaoSuperficial) {
		this.fceCaptacaoSuperficial = fceCaptacaoSuperficial;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public FceOutorgaLocalizacaoGeografica getFceOutorgaLocalizacaoGeograficaSelecionada() {
		return fceOutorgaLocalizacaoGeograficaSelecionada;
	}

	public void setFceOutorgaLocalizacaoGeograficaSelecionada(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaSelecionada) {
		this.fceOutorgaLocalizacaoGeograficaSelecionada = fceOutorgaLocalizacaoGeograficaSelecionada;
	}

	public List<CaracteristicaCaptacao> getListaCaracteristicaCaptacao() {
		return listaCaracteristicaCaptacao;
	}

	public void setListaCaracteristicaCaptacao(List<CaracteristicaCaptacao> listaCaracteristicaCaptacao) {
		this.listaCaracteristicaCaptacao = listaCaracteristicaCaptacao;
	}

	public List<FceCaptacaoSuperficial> getListaFceCaptacaoSuperficial() {
		return listaFceCaptacaoSuperficial;
	}

	public List<CaracteristicaCaptacao> getListaCaracteristicaCaptacaoTemp() {
		return listaCaracteristicaCaptacaoTemp;
	}

	public void setListaCaracteristicaCaptacaoTemp(List<CaracteristicaCaptacao> listaCaracteristicaCaptacaoTemp) {
		this.listaCaracteristicaCaptacaoTemp = listaCaracteristicaCaptacaoTemp;
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFceCapSup");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioCaptacaoSuperficial");
		RequestContext.getCurrentInstance().execute("cap_sup.show();");
	}

	
	@Override
	protected void prepararDuplicacao() {
		try {
			
			super.listaFceOutorgaLocalizacaoGeograficaCapSup.clear();
			super.listaOutorgaConcedida = new ArrayList<OutorgaConcedida>();
			
			if (Util.isNullOuVazio(listaFceCaptacaoSuperficial)){
				listarFceCaptacaoSuperficial();
			}
			
			super.listaFceOutorgaLocalizacaoGeograficaCapSup.clear();
			
			for(FceCaptacaoSuperficial fceCaptacaoSuperficialObj : listaFceCaptacaoSuperficial){
				this.fceCaptacaoSuperficial = fceCaptacaoSuperficialObj;
				
				carregarCnae();
				
				OutorgaLocalizacaoGeografica outorgaLocGeoOriginal = fceCaptacaoSuperficialObj.getIdeFceOutorgaLocalizacaoGeografica().getIdeOutorgaLocalizacaoGeografica();
				
				FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaDuplicada = 
						super.prepararDuplicacaoFceOutorgaLocalizacaoGeografica(
								fceCaptacaoSuperficialObj.getIdeFceOutorgaLocalizacaoGeografica(), outorgaLocGeoOriginal, TipologiaEnum.CAPTACAO_SUPERFICIAL);
				
				fceCaptacaoSuperficialObj.setIdeFceCaptacaoSuperficial(null);
				fceCaptacaoSuperficialObj.setIdeFceOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeograficaDuplicada);
				
				super.adicionarFceOutorgaLocalizacaoGeografica(TipologiaEnum.CAPTACAO_SUPERFICIAL, fceOutorgaLocalizacaoGeograficaDuplicada);
				
				listaOutorgaConcedida.add(new OutorgaConcedida(fceOutorgaLocalizacaoGeograficaDuplicada, outorgaLocGeoOriginal));
			}
			
			super.tratarPontosListaFceOutorgaLocalizacaoGeografica(TipologiaEnum.CAPTACAO_SUPERFICIAL);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Captação Superficial.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void duplicarFce() {
		for(FceCaptacaoSuperficial fceCaptacaoSuperficialObj : listaFceCaptacaoSuperficial){
			this.fceCaptacaoSuperficial = fceCaptacaoSuperficialObj;
			
			super.duplicacao(this.fceCaptacaoSuperficial.getIdeFceOutorgaLocalizacaoGeografica());
			salvarFceCaptacaoSuperficial();
			salvarFceCaptacaoSuperficialCnaeTecnico();
		}
		
		super.salvarListaOutorgaConcedida();
		
		for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : super.listaFceOutorgaLocalizacaoGeograficaCapSup){
			calcularVazaoHora(fceOutorgaLocalizacaoGeografica);
			formatarNumVazao(fceOutorgaLocalizacaoGeografica);
		}
	}
	
	
	public void carregarCnae(){

		
		try {
			renderCamposCnaeEVazao = Boolean.FALSE;
		
					
			if(fceCaptacaoSuperficial.getIdeFceOutorgaLocalizacaoGeografica() != null){
				List<OutorgaLocalizacaoGeograficaFinalidade> outorgaFinalidades;
					outorgaFinalidades = outorgaLocalizacaoGeograficaFinalidade.obterListaOutorgaLocalizacaoGeograficaFinalidades(
							fceCaptacaoSuperficial.getIdeFceOutorgaLocalizacaoGeografica().getIdeOutorgaLocalizacaoGeografica());
				
				for (OutorgaLocalizacaoGeograficaFinalidade outorga : outorgaFinalidades) {
					if(outorga.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.LAZER_TURISMO.getId() || outorga.getIdeTipoFinalidadeUsoAgua().
							getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.COMERCIO_SERVICOS.getId()) ){
						renderCamposCnaeEVazao = Boolean.TRUE;
						break;
					}

				}
		
			}else{
				List<FceOutorgaLocalizacaoGeograficaFinalidade> listaFceOutorgaLocalizacaoGeograficaFinalidade = fceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade.listarTipoFinalidadeUsoAguaByFce(fceCaptacaoSuperficial.getIdeFceOutorgaLocalizacaoGeografica());
				fceCaptacaoSuperficial.getIdeFceOutorgaLocalizacaoGeografica().setListaFinalidade(listaFceOutorgaLocalizacaoGeograficaFinalidade);
				for (FceOutorgaLocalizacaoGeograficaFinalidade outorga : listaFceOutorgaLocalizacaoGeograficaFinalidade) {
					if(outorga.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals( TipoFinalidadeUsoAguaEnum.LAZER_TURISMO.getId()) || outorga.getIdeTipoFinalidadeUsoAgua().
							getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.COMERCIO_SERVICOS.getId()) ){
						renderCamposCnaeEVazao = Boolean.TRUE;
						break;
					}
				}
			}
			
			if(renderCamposCnaeEVazao){
				
				/*
				 * Verificar se o campo de vazãoCaptacao já está preenchido na tabela
				 * caso não esteja preeenchido, pegar a vazao da outorga selecionada 
				 */
				if(Util.isNullOuVazio(fceCaptacaoSuperficial.getNumVazaoCaptacao())){
					BigDecimal vazaoCaptacao = new BigDecimal(fceOutorgaLocalizacaoGeograficaSelecionada.getMaskNumVazao());
					fceCaptacaoSuperficial.setNumVazaoCaptacao(vazaoCaptacao);				
				}
				

				List<FceCaptacaoSuperficialCnae> listaFce = new ArrayList<FceCaptacaoSuperficialCnae>();
				listaFce = (List<FceCaptacaoSuperficialCnae>) fceCaptacaoCnaeService.listarCnaeByFce(fceCaptacaoSuperficial);
				this.fceCaptacaoSuperficialCnaeData = new ListDataModel<FceCaptacaoSuperficialCnae>(listaFce);
				
				CnaeController cnae = (CnaeController) SessaoUtil.recuperarManagedBean("#{cnaeController}", CnaeController.class);
				cnae.setFceCaptacaoSuperficialCnaeData(fceCaptacaoSuperficialCnaeData);
				cnae.setFceCaptacaoSuperficial(fceCaptacaoSuperficial);
				if(fceCaptacaoSuperficialCnaeData.getRowCount() > 0){
					cnae.setFlagTableCnae(true);
				}else{
					cnae.setFlagTableCnae(false);
				}
				
			}
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
			

	}

	@Override
	protected void carregarFceTecnico() {
		try {
			carregarAba();
			listarFceOutorgaLocalizacaoGeograficaDAnaliseTecnica();
			for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : super.listaFceOutorgaLocalizacaoGeograficaCapSup){
				calcularVazaoHora(fceOutorgaLocalizacaoGeografica);
				formatarNumVazao(fceOutorgaLocalizacaoGeografica);
			}
			super.tratarPontosListaFceOutorgaLocalizacaoGeografica(TipologiaEnum.CAPTACAO_SUPERFICIAL);
			listarFceCaptacaoSuperficial();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Captação Superficial.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public Cnae getCnaeSecao() {
		return cnaeSecao;
	}

	public void setCnaeSecao(Cnae cnaeSecao) {
		this.cnaeSecao = cnaeSecao;
	}

	public List<SelectItem> getListaSecao() {
		return listaSecao;
	}

	public void setListaSecao(List<SelectItem> listaSecao) {
		this.listaSecao = listaSecao;
	}

	public Cnae getCnaeDivisao() {
		return cnaeDivisao;
	}

	public void setCnaeDivisao(Cnae cnaeDivisao) {
		this.cnaeDivisao = cnaeDivisao;
	}

	public List<SelectItem> getListaDivisao() {
		return listaDivisao;
	}

	public void setListaDivisao(List<SelectItem> listaDivisao) {
		this.listaDivisao = listaDivisao;
	}

	public Cnae getCnaeGrupo() {
		return cnaeGrupo;
	}

	public void setCnaeGrupo(Cnae cnaeGrupo) {
		this.cnaeGrupo = cnaeGrupo;
	}

	public List<SelectItem> getListaGrupo() {
		return listaGrupo;
	}

	public void setListaGrupo(List<SelectItem> listaGrupo) {
		this.listaGrupo = listaGrupo;
	}

	public Cnae getCnaeClasse() {
		return cnaeClasse;
	}

	public void setCnaeClasse(Cnae cnaeClasse) {
		this.cnaeClasse = cnaeClasse;
	}

	public Collection<Cnae> getListaClasse() {
		return listaClasse;
	}

	public void setListaClasse(Collection<Cnae> listaClasse) {
		this.listaClasse = listaClasse;
	}

	public Cnae getCnaeSubclasse() {
		return cnaeSubclasse;
	}

	public void setCnaeSubclasse(Cnae cnaeSubclasse) {
		this.cnaeSubclasse = cnaeSubclasse;
	}

	public Collection<Cnae> getListaSubclasse() {
		return listaSubclasse;
	}

	public void setListaSubclasse(Collection<Cnae> listaSubclasse) {
		this.listaSubclasse = listaSubclasse;
	}

	public DataModel<FceCaptacaoSuperficialCnae> getFceCaptacaoSuperficialCnaeData() {
		return fceCaptacaoSuperficialCnaeData;
	}

	public void setFceCaptacaoSuperficialCnaeData(
			DataModel<FceCaptacaoSuperficialCnae> fceCaptacaoSuperficialCnaeData) {
		this.fceCaptacaoSuperficialCnaeData = fceCaptacaoSuperficialCnaeData;
	}

	public Boolean getRenderCamposCnaeEVazao() {
		return renderCamposCnaeEVazao;
	}

	public void setRenderCamposCnaeEVazao(Boolean renderCamposCnaeEVazao) {
		this.renderCamposCnaeEVazao = renderCamposCnaeEVazao;
	}
	
}