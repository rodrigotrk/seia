package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.CategoriaTurismo;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoTipologia;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceCategoriaTurismo;
import br.gov.ba.seia.entity.FceTurismo;
import br.gov.ba.seia.entity.FceTurismoLocalizacaoGeografica;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelUrbano;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.TipoApp;
import br.gov.ba.seia.entity.TipoAreaConcedida;
import br.gov.ba.seia.entity.TipoOrigemEnergia;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.TipoAreaConcedidaEnum;
import br.gov.ba.seia.enumerator.TipoImovelEnum;
import br.gov.ba.seia.enumerator.TipoOrigemEnergiaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.FceTurismoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

/**
 * Controller que é chamado pelas telas de FCE - Turismo.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 04/12/2014
 */
@Named("fceTurismoController")
@ViewScoped
public class FceTurismoController extends FceComDocumentoAdicionalController {
	
	@Inject
	private LocalizacaoGeograficaGenericController locGeoController;
	
	@EJB
	private FceTurismoServiceFacade fceTurismoServiceFacade;
	
	private static final DocumentoObrigatorio DOC_TURISMO = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_TURISMO.getId());
	
	private FceTurismo fceTurismo;
	private boolean zonaRural;
	private boolean zonaUrbana;
	private boolean cessionario;
	
	private boolean conjuntoHabitacional;
	private boolean habilitacaoInteresseSocial;
	
	private Empreendimento empreendimento;
	private LocalizacaoGeografica localizacaoGeografica;
	
	private List<CategoriaTurismo> listaTipologiaG11;
	private List<CategoriaTurismo>listaTipologiaG11selected;
	private List<CategoriaTurismo>listaTipologiaG21;
	private List<CategoriaTurismo>listaTipologiaG21selected;
	private List<CategoriaTurismo>listaTipologiaG22;
	private List<CategoriaTurismo>listaTipologiaG22selected;
	private List<TipoOrigemEnergia> listaTipoOrigemEnergia, listaTipoOrigemEnergiaEscolhidos;
	
	private BigDecimal areaEmpreendimentoG11;
	private BigDecimal areaEmpreendimentoG21;
	private BigDecimal areaEmpreendimentoG22;
	private BigDecimal areaEmpreendimentoG23;
	private BigDecimal areaEmpreendimentoG24;
	
	private List<TipoApp> listaTipoApps;
	private List<TipoApp> listaTipoAppsEscolhidos;
	private TipoApp tipoApp;
	
	private List<FceTurismoLocalizacaoGeografica> listaFceTurismoLocalizacaoGeografica;
	private FceTurismoLocalizacaoGeografica fceTurismoLocalizacaoGeografica;
	
	private boolean li;
	
	private boolean indConcedidoEmpreendimento;
	private List<ArquivoProcesso> listaPoligonalDaNotificacao;
	private List<ProcessoAtoConcedido> listProcessoAtoConcedido;
	private ArquivoProcesso apConcedido;
	
	@Override
	public void init() {
		verificarEdicao();
		if(!isFceSalvo()) {
			iniciarFce(DOC_TURISMO);
			fceTurismo = new FceTurismo(super.fce);
		}
		carregarAba();
	}

	@Override
	public void verificarEdicao() {
		carregarFceDoRequerente(DOC_TURISMO);
	}

	@Override
	public void carregarAba() {
		if(isFceSalvo()) {
			carregarFceTurismoByFce();
			carregarFceCategoriaTurismo();
			carregarFceTurismoLocalizacaoGeografica();
			carregarFceTurismoOrigemEnergia();
		}
		carregarInformacoesEmpreendimento();
		carregarListaOrigemEnergia();
	}

	/**
	 * Método que carrega a lista de {@link TipoOrigemEnergia} para o {@link FceTurismo}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 04/12/2014
	 */
	private void carregarListaOrigemEnergia() {
		try {
			/*
			 * #6523
			 * listaTipoOrigemEnergia = origemEnergiaService.buscarListaTipoOrigemEnergiaForListTipoOrigemEnergiaEnum(montarListaTipoOrigemEnergiaEnum());
			 */
			listaTipoOrigemEnergia = fceTurismoServiceFacade.listarTipoOrigemEnergia();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as origens de energia.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método temporário para listar os {@link TipoOrigemEnergia} que podem ser exibidos no {@link FceTurismo}.
	 * <br>
	 * Depreciado devido ao <i>ticket</i> #6523
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 04/12/2014
	 * @return listTipoOrigemEnergiaEnum
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private List<TipoOrigemEnergiaEnum> montarListaTipoOrigemEnergiaEnum() {
		List<TipoOrigemEnergiaEnum> listTipoOrigemEnergiaEnum = new ArrayList<TipoOrigemEnergiaEnum>();
		listTipoOrigemEnergiaEnum.add(TipoOrigemEnergiaEnum.GERADOR);
		listTipoOrigemEnergiaEnum.add(TipoOrigemEnergiaEnum.OUTROS);
		return listTipoOrigemEnergiaEnum;
	}

	/**
	 * Método que carrega o {@link Empreendimento} e suas informações pertinentes para o {@link FceTurismo}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 04/12/2014
	 */
	private void carregarInformacoesEmpreendimento() {
		try {
			empreendimento = fceTurismoServiceFacade.buscarEmpreendimento(this);
			if(!isEmpreendimentoComShape()) {
				armazenarLocGeoDoEmpreendimento();
			}
			carregarEmpreendimentoTipologia();
			carregarEmpreendimentoRequerimento();
			marcarZonaImovelAndListarApps();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void armazenarLocGeoDoEmpreendimento() throws Exception {
		localizacaoGeografica = empreendimento.getIdeLocalizacaoGeografica().clone();
	}

	/**
	 * Método que carrega a lista de {@link EmpreendimentoTipologia} e verifica suas {@link Tipologia}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 04/12/2014
	 */
	private void carregarEmpreendimentoTipologia() throws Exception{
		List<Tipologia> lista = fceTurismoServiceFacade.listarTipologiaByEmpreendimento(empreendimento);
		for (Tipologia tipologia : lista) {
			if(tipologia.getIdeTipologia().intValue() == TipologiaEnum.G11.getId()) {
				habilitarGrupoG11();
			} else if(tipologia.getIdeTipologia().intValue() == TipologiaEnum.G21.getId()) {
				habilitarGrupoG21();
			} else if(tipologia.getIdeTipologia().intValue() == TipologiaEnum.G22.getId()) {
				habilitarGrupoG22();
			} else if(tipologia.getIdeTipologia().intValue() == TipologiaEnum.G23.getId()) {
				habilitarGrupoG23();
			} else if(tipologia.getIdeTipologia().intValue() == TipologiaEnum.G24.getId()) {
				habilitarGrupoG24();
			}
		}
	}

	/**
	 * Método que carrega a lista de {@link CategoriaTurismo} para as {@link Tipologia} e a Área do empreendimento para o grupo G2.4.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 16/12/2014
	 * @throws Exception
	 */
	private void habilitarGrupoG24() throws Exception {
		habilitacaoInteresseSocial = true;
		areaEmpreendimentoG24 = carregarAreaEmpreendimentoByTipologiaEnum(TipologiaEnum.G24);
	}
	/**
	 * Método que carrega a lista de {@link CategoriaTurismo} para as {@link Tipologia} e a Área do empreendimento para o grupo G2.3.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 16/12/2014
	 * @throws Exception
	 */
	private void habilitarGrupoG23() throws Exception {
		conjuntoHabitacional = true;
		areaEmpreendimentoG23 = carregarAreaEmpreendimentoByTipologiaEnum(TipologiaEnum.G23);
	}
	/**
	 * Método que carrega a lista de {@link CategoriaTurismo} para as {@link Tipologia} e a Área do empreendimento para o grupo G2.2.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 16/12/2014
	 * @throws Exception
	 */
	private void habilitarGrupoG22() throws Exception {
		listaTipologiaG22 = carregarListaTipologiaByTipologiaEnum(TipologiaEnum.G22);
		areaEmpreendimentoG22 = carregarAreaEmpreendimentoByTipologiaEnum(TipologiaEnum.G22);
	}
	/**
	 * Método que carrega a lista de {@link CategoriaTurismo} para as {@link Tipologia} e a Área do empreendimento para o grupo G2.1.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 05/12/2014
	 * @throws Exception
	 */
	private void habilitarGrupoG21() throws Exception {
		listaTipologiaG21 = carregarListaTipologiaByTipologiaEnum(TipologiaEnum.G21);
		areaEmpreendimentoG21 = carregarAreaEmpreendimentoByTipologiaEnum(TipologiaEnum.G21);
	}

	/**
	 * Método que carrega a lista de {@link CategoriaTurismo} para as {@link Tipologia} e a Área do empreendimento para o grupo G1.1.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 05/12/2014
	 * @throws Exception
	 */
	private void habilitarGrupoG11() throws Exception {
		listaTipologiaG11 = carregarListaTipologiaByTipologiaEnum(TipologiaEnum.G11);
		areaEmpreendimentoG11 = carregarAreaEmpreendimentoByTipologiaEnum(TipologiaEnum.G11);
	}

	private void carregarEmpreendimentoRequerimento() throws Exception {
		li = fceTurismoServiceFacade.isFaseEmpreendimentoImplantacao(empreendimento, super.requerimento);
	}

	/**
	 * @author eduardo.fernandes
	 * <pre> <b> #6456 </b></pre>
	 */
	private void marcarZonaImovelAndListarApps() throws Exception {
		for(Imovel imovel : empreendimento.getImovelCollection()){
			if(imovel.getIdeTipoImovel().getIdeTipoImovel() == TipoImovelEnum.URBANO.getId()){
				zonaUrbana = true;
				carregarAppsToZonaUrbana();
			}
			else if(imovel.getIdeTipoImovel().getIdeTipoImovel() == TipoImovelEnum.RURAL.getId()){
				zonaRural = true;
				carregarAppsToZonaRural();
				break;
			}
			/* Caso no cadastrado um Empreendimento o usuário tenha respondido que o vínculo do titular com o imóvel é "Cessionário",
			 * o Imóvel é salvo como "Cessionário" e acaba ignorando a pergunta "Tipo de Imóvel?" onde se reponde "Imóvel Rural" ou "Imóvel Urbano".
			 * Para saber o que foi marcado nessa pergunta, verificamos a existência de ImovelUrbano para saber qual Zona marcar no FCE - Turismo.
			 * #6710 #6711
			 */
			else if(imovel.getIdeTipoImovel().getIdeTipoImovel() == TipoImovelEnum.CESSIONARIO.getId()) {
				cessionario = true;
				ImovelUrbano urbano = imovel.getImovelUrbano();
				if (!Util.isNull(urbano) && !Util.isNullOuVazio(urbano.getNumInscricaoIptu())) {
					zonaUrbana = true;
				}
				else {
					zonaRural = true;
				}
			}
		}
	}

	private void carregarListaDeAppsDosImoveisRurais() throws Exception {
		listaFceTurismoLocalizacaoGeografica = fceTurismoServiceFacade.montarListaFceTurismoLocalizacaoGeograficaByImovelRural(empreendimento, fceTurismo);
	}

	private void carregarListaDeAppFull() throws Exception {
		listaTipoApps = fceTurismoServiceFacade.listarTipoApp();
	}

	private void carregarAppsToZonaUrbana() throws Exception {
		carregarListaDeAppFull();
		if(isFceTurismoSalvo()) {
			removerApp();
		}
	}

	private void removerApp() {
		for(FceTurismoLocalizacaoGeografica turismoLocalizacaoGeografica : listaFceTurismoLocalizacaoGeografica) {
			listaTipoApps.remove(turismoLocalizacaoGeografica.getIdeTipoApp());
		}
	}

	private void carregarAppsToZonaRural() throws Exception {
		carregarListaDeAppsDosImoveisRurais();
	}

	/**
	 * Método que retorna o <i>val_atividade</i> do {@link RequerimentoTipologia} do {@link Requerimento} de acordo com a {@link TipologiaEnum}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 05/12/2014
	 * @param tipologiaEnum
	 * @return {@link BigDecimal}
	 * @throws Exception
	 */
	private BigDecimal carregarAreaEmpreendimentoByTipologiaEnum(TipologiaEnum tipologiaEnum) throws Exception {
		return fceTurismoServiceFacade.retonarValorAtividade(super.requerimento, tipologiaEnum);
	}

	/**
	 * Método que retorna a lista de {@link CategoriaTurismo} de acordo com a {@link TipologiaEnum}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 05/12/2014
	 * @param tipologiaEnum
	 * @return lista {@link CategoriaTurismo}
	 * @throws Exception
	 */
	private List<CategoriaTurismo> carregarListaTipologiaByTipologiaEnum(TipologiaEnum tipologiaEnum) throws Exception {
		return fceTurismoServiceFacade.listarTipologiaByTipologiaEnum(tipologiaEnum);
	}

	/**
	 * Método que carrega o {@link FceTurismo} de acordo com o {@link Fce}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 04/12/2014
	 */
	private void carregarFceTurismoByFce() {
		try {
			fceTurismo = fceTurismoServiceFacade.buscarFceTurismo(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Turismo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega o {@link FceCategoriaTurismo} e coloca as {@link CategoriaTurismo} em suas respectivas listas.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 15/12/2014
	 */
	private void carregarFceCategoriaTurismo() {
		try {
			List<FceCategoriaTurismo> listaFceCategoriaTurismo = fceTurismoServiceFacade.listarFceCategoriaTurismo(this);
			if(!Util.isNullOuVazio(listaFceCategoriaTurismo)){
				for(FceCategoriaTurismo fceCategoriaTurismo : listaFceCategoriaTurismo) {
					if(fceCategoriaTurismo.getIdeCategoriaTurismo().getIdeTipologia().getIdeTipologia().equals(TipologiaEnum.G11.getId())) {
						if(Util.isNullOuVazio(listaTipologiaG11selected)) {
							listaTipologiaG11selected = new ArrayList<CategoriaTurismo>();
						}
						listaTipologiaG11selected.add(fceCategoriaTurismo.getIdeCategoriaTurismo());
					} else if(fceCategoriaTurismo.getIdeCategoriaTurismo().getIdeTipologia().getIdeTipologia().equals(TipologiaEnum.G21.getId())) {
						if(Util.isNull(listaTipologiaG21selected)) {
							listaTipologiaG21selected = new ArrayList<CategoriaTurismo>();
						}
						listaTipologiaG21selected.add(fceCategoriaTurismo.getIdeCategoriaTurismo());
					} else if(fceCategoriaTurismo.getIdeCategoriaTurismo().getIdeTipologia().getIdeTipologia().equals(TipologiaEnum.G22.getId())) {
						if(Util.isNull(listaTipologiaG22selected)) {
							listaTipologiaG22selected = new ArrayList<CategoriaTurismo>();
						}
						listaTipologiaG22selected.add(fceCategoriaTurismo.getIdeCategoriaTurismo());
					}
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações de Categoria do Empreendimento do FCE - Turismo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarFceTurismoLocalizacaoGeografica() {
		try {
			listaFceTurismoLocalizacaoGeografica = fceTurismoServiceFacade.listarFceTurismoLocalizacaoGeografica(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as Poligonais das APPs.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarFceTurismoOrigemEnergia() {
		try {
			listaTipoOrigemEnergiaEscolhidos = fceTurismoServiceFacade.listarTipoOrigemEnergiaByFceTurismoCategoriaTurismo(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações de Suprimento de Energia do FCE - Turismo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para salvar o {@link FceTurismo} e suas associativas.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 15/12/2014
	 */
	@Override
	public void finalizar() {
		if(validarAba()) {
			try {
				fceTurismoServiceFacade.finalizar(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Turismo");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		if(isFceTecnico()) preparaParaSalvarProcessoAtoConcedido();
		fceTurismoServiceFacade.finalizarFceTurismo(this);
		fecharDialogAndExibirMensagem();
	}

	/**
	 * Método para gerar {@link Fce}, {@link FceTurismo} e {@link FceTurismoLocalizacaoGeografica} para poder contemplar a Melhoria #6566.
	 * Existe a necesidade de consulta em banco e portanto os Ide's são necessários.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 04/03/2015
	 * @param fceTurismoLocalizacaoGeografica
	 * @see #6566
	 */
	public void prepararFceParaVisualizacaoGeoBahia(FceTurismoLocalizacaoGeografica fceTurismoLocalizacaoGeografica) {
		try {
			fceTurismoServiceFacade.salvarFceTurismoParaVisualizacaoGeoBahia(fceTurismoLocalizacaoGeografica, this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a poligonal da APP.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que fecha o Dialog e verifica se deve exibir a mensagem de "Outros" ou o dialog para imprimir relatório.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 19/12/2014
	 */
	private void fecharDialogAndExibirMensagem() {
		if(!isSuprimentoEnergiaComOutros()) {
			if(!isFceTecnico()) RequestContext.getCurrentInstance().execute("rel_turismo.show()");
			Html.atualizar("frmAnaliseTecnica:gridFce");
//			RequestContext.getCurrentInstance().execute("updateDocumento();");
		} else {
			JsfUtil.addWarnMessage(Util.getString("fce_tur_outros"));
		}
		RequestContext.getCurrentInstance().execute("dlgTurismo.hide()");
		limpar();
	}


	/**
	 * Método para limpar a tela do {@link FceTurismo}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 15/12/2014
	 */
	@Override
	public void limpar() {
		fceTurismo = null;
		limparInfoEmpreendimento();
		limparCategoriaTurismo();
		limparAppShape();
		super.limparFce();
		super.limparDocumentoUpado();
	}

	/**
	 * Método que limpa as informações referentes ao {@link Empreendimento}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 17/12/2014
	 */
	private void limparInfoEmpreendimento() {
		empreendimento = null;
		zonaRural = false;
		zonaUrbana = false;
		cessionario = false;
		li = false;
		areaEmpreendimentoG11 = null;
		areaEmpreendimentoG21 = null;
		areaEmpreendimentoG22 = null;
		areaEmpreendimentoG23 = null;
		areaEmpreendimentoG24 = null;
	}

	/**
	 * Método que limpa as informações referentes a {@link FceTurismoLocalizacaoGeografica}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 17/12/2014
	 */
	private void limparAppShape() {
		listaTipoApps = null;
		listaTipoAppsEscolhidos = null;
		listaFceTurismoLocalizacaoGeografica = null;
		fceTurismoLocalizacaoGeografica = null;
	}

	/**
	 * Método que limpa as informações referentes a {@link CategoriaTurismo}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 17/12/2014
	 */
	private void limparCategoriaTurismo() {
		listaTipologiaG11 = null;
		listaTipologiaG21 = null;
		listaTipologiaG22 = null;
		habilitacaoInteresseSocial = false;
		conjuntoHabitacional = false;
		limparCategoriaTurismoSelected();
	}
	
	private void limparCategoriaTurismoSelected() {
		listaTipologiaG11selected = null;
		listaTipologiaG21selected = null;
		listaTipologiaG22selected = null;
	}

	@Override
	public boolean validarAba() {
		boolean valido = true;
		if(!isEmpreendimentoComShape()) {
			valido = false;
			JsfUtil.addErrorMessage("A poligonal do empreendimento " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		} else {
			if(!isTheGeomPersistido()) {
				valido = false;
				JsfUtil.addErrorMessage("A poligonal do empreendimento " + Util.getString("msg_generica_preenchimento_obrigatorio"));
				empreendimento.setIdeLocalizacaoGeografica(localizacaoGeografica);
			}
		}
		if(isTemZonaUrbana()) {
			if(Util.isNull(fceTurismo.getIndPlanoDiretor())) {
				valido = false;
				JsfUtil.addErrorMessage("A declaração do Plano do Diretor " + Util.getString("msg_generica_preenchimento_obrigatorio"));
				// Correção #6458
			} else if(fceTurismo.getIndPlanoDiretor() && Util.isNullOuVazio(fceTurismo.getDscLeiMunicipal())) {
				valido = false;
				JsfUtil.addErrorMessage("A Lei Municipal ou instrumento legal " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			}
		}
		String categoriaEmpreendimento = Util.getString("msg_generica_selecione_um") + " uma " + Util.getString("fce_tur_categoria_empreendimento") + " do ";
		if(isTipologiaG11() && Util.isNullOuVazio(listaTipologiaG11selected)) {
			valido = false;
			JsfUtil.addErrorMessage(categoriaEmpreendimento + Util.getString("fce_tur_g11") + ".");
		}
		if(isTipologiaG21() && Util.isNullOuVazio(listaTipologiaG21selected)) {
			valido = false;
			JsfUtil.addErrorMessage(categoriaEmpreendimento + Util.getString("fce_tur_g21") + ".");
		}
		if(isTipologiaG22() && Util.isNullOuVazio(listaTipologiaG22selected)) {
			valido = false;
			JsfUtil.addErrorMessage(categoriaEmpreendimento + Util.getString("fce_tur_g22") + ".");
		}
		if(Util.isNullOuVazio(fceTurismo.getNumAreaConstruida())) {
			valido = false;
			JsfUtil.addErrorMessage("A " + Util.getString("fce_tur_area_construida") + " " + Util.getString("msg_generica_null_ou_vazio") );
		}
		/*
		 * #6529
		 * if(isZonaUrbana() && Util.isNullOuVazio(listaFceTurismoLocalizacaoGeografica)) {
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um Tipo de App.");
		}
		else */
		if(isZonaUrbana() && isExisteAppAdicionada()) {
			if(Util.isNullOuVazio(fceTurismo.getNumAreaApp())) {
				valido = false;
				JsfUtil.addErrorMessage("A " + Util.getString("fce_tur_area_app") + " " + Util.getString("msg_generica_null_ou_vazio") );
			}
			for(FceTurismoLocalizacaoGeografica fceTurismoLocalizacaoGeografica : listaFceTurismoLocalizacaoGeografica) {
				if(Util.isNullOuVazio(fceTurismoLocalizacaoGeografica.getIdeLocalizacaoGeografica())) {
					valido = false;
					JsfUtil.addErrorMessage(Util.getString("fce_tur_obrigatorio_poligonal"));
					break;
				}
			}
		}
		if(!isLi() && Util.isNullOuVazio(listaTipoOrigemEnergiaEscolhidos)) {
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um " + Util.getString("fce_tur_sup_energia") + ".");
		}
		if(!isArquivoUpado()) {
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("fce_outorga_obrigatorio_upload"));
		}
		
		if(isFceTecnico() && !validaIndConcedido()) {
			valido = false;
			JsfUtil.addErrorMessage("Pelo menos uma poligonal deve ser concedida.");
		}
		
		return valido;
	}

	/*
	 * [INI] Changes
	 */
	public void changeZonaExpansaoUrbana(ValueChangeEvent event) {
		if(!Util.isNull(event) && !(Boolean) event.getNewValue()) {
			fceTurismo.setIndPlanoDiretor(null);
			changePlanoDiretor(event);
		}
	}

	public void changePlanoDiretor(ValueChangeEvent event) {
		if(!Util.isNull(event) && !(Boolean) event.getNewValue()) {
			fceTurismo.setDscLeiMunicipal(null);
		}
	}

	@SuppressWarnings("unchecked")
	public void changeTipoOrigemEnergia(ValueChangeEvent event) {
		if(!Util.isNull(event.getNewValue())){
			List<TipoOrigemEnergia> origemEnergiaOld = (List<TipoOrigemEnergia>) event.getOldValue();
			List<TipoOrigemEnergia> origemEnergiaNew = (List<TipoOrigemEnergia>) event.getNewValue();
			if(!Util.isNullOuVazio(origemEnergiaOld)) {
				for(TipoOrigemEnergia origemEnergia : origemEnergiaNew) {
					if(!origemEnergiaOld.contains(origemEnergia) && origemEnergia.isOutros()) {
						super.exibirInformacao001();
						break;
					}
				}
			} else if(origemEnergiaNew.get(0).isOutros()) {
				super.exibirInformacao001();
			}
		}
	}
	/*
	 * [FIM] Changes
	 */

	/*
	 * [INI] Shapes
	 */
	public void adicionarApp() {
		if(Util.isNullOuVazio(listaFceTurismoLocalizacaoGeografica)) {
			listaFceTurismoLocalizacaoGeografica = new ArrayList<FceTurismoLocalizacaoGeografica>();
		}
		List<Imovel> lista = (List<Imovel>) empreendimento.getImovelCollection();
		listaFceTurismoLocalizacaoGeografica.add(new FceTurismoLocalizacaoGeografica(tipoApp, fceTurismo, lista.get(0)));
		listaTipoApps.remove(tipoApp);
	}

	public void excluirPoligonalDaApp() {
		if(!Util.isNullOuVazio(fceTurismoLocalizacaoGeografica.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())) {
			locGeoController.excluirShape(fceTurismoLocalizacaoGeografica.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			fceTurismoLocalizacaoGeografica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		if(isZonaUrbana()) {
			listaTipoApps.add(fceTurismoLocalizacaoGeografica.getIdeTipoApp());
			listaFceTurismoLocalizacaoGeografica.remove(fceTurismoLocalizacaoGeografica);
			fceTurismoLocalizacaoGeografica = new FceTurismoLocalizacaoGeografica();
			atualizarAreaShapeApp();
			Collections.sort(listaTipoApps);
		}
	}

	public void atualizarAreaShapeApp(){
		if(isExisteAppAdicionada()){
			try {
				fceTurismo.setNumAreaApp(new BigDecimal(somatorioDasAreasDosShapes()));
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a área da geometria.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		else {
			fceTurismo.setNumAreaApp(null);
		}
	}

	private Double somatorioDasAreasDosShapes() throws Exception{
		Double somatorio = 0.0;
		for(FceTurismoLocalizacaoGeografica turismoLocalizacaoGeografica : listaFceTurismoLocalizacaoGeografica){
			if(getTipoAppComShape(turismoLocalizacaoGeografica)){
				somatorio += fceTurismoServiceFacade.retonarAreaShapeByGeometria(turismoLocalizacaoGeografica.getIdeLocalizacaoGeografica());
			}
		}
		return somatorio;
	}
	/*
	 * [FIM] Shapes
	 */


	/*
	 * [INI] Download && Upload de Arquivos
	 */
	/**
	 * Método que realiza o <i>download</i> do documento adicional do {@link FceTurismo}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 05/12/2014
	 * @return {@link StreamedContent}
	 */
	public StreamedContent getDadosAdicionais() {
		try {
			return getDadosAdicionais("Informacoes_Adicionais_FCE_Turismo.doc", "Informações Adicionais - FCE Turismo.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para definir qual URL deve ser enviada para nova aba do Navegador.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 03/03/2015
	 * @return {@link String}
	 * @see #6566
	 */
	public String exibirLocalizacaoGeografica(Object obj){
		if(Util.isNull(obj) ){
			// Caso o FCE ainda não tenha sido salvo, exibir os limites do Empreendimento pela sua LocalizacaoGeografica;
			if(!Util.isNullOuVazio(super.fce)){
				return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(super.fce);
			}
			// Caso o FCE tenha sido salvo, exibir os limites do Empreendimento e as APP's pelo IdeFce;
			else if(!Util.isNull(empreendimento.getIdeLocalizacaoGeografica())){
				return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(empreendimento);
			}
		} else if(obj instanceof FceTurismoLocalizacaoGeografica){
			FceTurismoLocalizacaoGeografica fceTurismoLocalizacaoGeografica = (FceTurismoLocalizacaoGeografica) obj;
			return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(fceTurismoLocalizacaoGeografica);
		} else if(obj instanceof LocalizacaoGeografica){
			LocalizacaoGeografica locGeo = (LocalizacaoGeografica) obj;
			return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(locGeo);
		}
		
		return new String();
	}

	/**
	 * Método que impriem o relatório do {@link FceTurismo}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 27/11/2014
	 * @return {@link StreamedContent}
	 * @throws Exception
	 * @see #6563
	 */
	public StreamedContent getImprimirRelatorio(){
		try {
			return getImprimirRelatorio(super.fce, DOC_TURISMO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Turismo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	/*
	 * [FIM] Download && Upload de Arquivos
	 */

	/*
	 * [INI] Flags
	 */
	private boolean isEmpreendimentoCarregado() {
		return !Util.isNull(empreendimento) && !Util.isNullOuVazio(empreendimento.getIdeLocalizacaoGeografica());
	}

	public boolean isEmpreendimentoComShape() {
		
		return isEmpreendimentoCarregado() && isTheGeomPersistido()
				&& empreendimento.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(
						ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId());
	}

	public boolean isTheGeomPersistido() {
		try {
			return !Util.isNullOuVazio(fceTurismoServiceFacade.retornarGeometriaShapeByLocalizacaoGeografica(empreendimento.getIdeLocalizacaoGeografica()));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a geometria da Poligonal.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public boolean isTemZonaUrbana() {
		return zonaUrbana || isFceTurismoZonaExtensaoUrbana();
	}

	private boolean isFceTurismoNotNull() {
		return !Util.isNull(fceTurismo);
	}

	public boolean isFceTurismoZonaExtensaoUrbana() {
		return isFceTurismoNotNull() && !Util.isNull(fceTurismo.getIndZonaExtensao()) && fceTurismo.getIndZonaExtensao();
	}

	public boolean isFceTurismoComPlanoDiretor() {
		return isFceTurismoNotNull() && !Util.isNull(fceTurismo.getIndPlanoDiretor()) && fceTurismo.getIndPlanoDiretor();
	}

	public boolean isExisteAppParaAdicionar() {
		return !Util.isNullOuVazio(listaTipoApps);
	}

	public boolean isExisteAppAdicionada() {
		return !Util.isNullOuVazio(listaFceTurismoLocalizacaoGeografica);
	}

	public int getSomenteShape() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue();
	}

	public boolean getTipoAppComShape(FceTurismoLocalizacaoGeografica fceTurismoLocalizacaoGeografica) {
		return !Util.isNull(fceTurismoLocalizacaoGeografica) && !Util.isNullOuVazio(fceTurismoLocalizacaoGeografica.getIdeLocalizacaoGeografica());
	}

	public boolean isTipologiaG11() {
		return !Util.isNullOuVazio(areaEmpreendimentoG11);
	}

	public boolean isTipologiaG2() {
		return isTipologiaG21() || isTipologiaG22() || isTipologiaG23() || isTipologiaG24();
	}

	public boolean isTipologiaG21() {
		return !Util.isNullOuVazio(areaEmpreendimentoG21);
	}

	public boolean isTipologiaG22() {
		return !Util.isNullOuVazio(areaEmpreendimentoG22);
	}

	public boolean isTipologiaG23() {
		return !Util.isNullOuVazio(areaEmpreendimentoG23);
	}

	public boolean isTipologiaG24() {
		return !Util.isNullOuVazio(areaEmpreendimentoG24);
	}

	private boolean isSuprimentoEnergiaComOutros() {
		if(!isLi() && !Util.isNullOuVazio(listaTipoOrigemEnergiaEscolhidos)) {
			for(TipoOrigemEnergia origemEnergia : listaTipoOrigemEnergiaEscolhidos) {
				if(origemEnergia.isOutros()) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isFceTurismoSalvo() {
		return !Util.isNullOuVazio(fceTurismo);
	}
	/*
	 * [FIM] Flags
	 */
	
	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFceTurismo");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioTurismo");
		RequestContext.getCurrentInstance().execute("dlgTurismo.show();");
	}

	@Override
	protected void prepararDuplicacao() {
		fceTurismo.setIdeFceTurismo(null);
		super.fce.setIndConcluido(false);
		fceTurismo.setIdeFce(super.fce);
		for(FceTurismoLocalizacaoGeografica fceTurismoLocalizacaoGeografica : listaFceTurismoLocalizacaoGeografica){
			fceTurismoLocalizacaoGeografica.setIdeFceTurismoLocalizacaoGeografica(null);
		}
		
	}

	@Override
	protected void duplicarFce() {
		try {
			for(FceTurismoLocalizacaoGeografica fceTurismoLocalizacaoGeografica : listaFceTurismoLocalizacaoGeografica){
				LocalizacaoGeografica localizacaoGeografica = fceTurismoLocalizacaoGeografica.getIdeLocalizacaoGeografica();
				fceTurismoLocalizacaoGeografica.setIdeLocalizacaoGeografica(fceTurismoServiceFacade.duplicarLocalizacaoGeografica(localizacaoGeografica));
			}
			fceTurismoServiceFacade.finalizarFceTurismoDuplicacao(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Indústria.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico() {
		carregarFceTurismoByFce();
		limparCategoriaTurismoSelected();
		carregarFceCategoriaTurismo();
		carregarFceTurismoLocalizacaoGeografica();
		carregarFceTurismoOrigemEnergia();
		carregarInformacoesEmpreendimento();
		carregarListaPoligonalDaNotificacao();
		carregarListaOrigemEnergia();
		verificarConcedido();
	}
	
	
	private void verificarConcedido(){
	
		List<ProcessoAtoConcedido> processo = new ArrayList<ProcessoAtoConcedido>();
		
		try {
		
			if(!Util.isNullOuVazio(this.empreendimento)){
				if(!Util.isNullOuVazio(this.empreendimento.getIdeLocalizacaoGeografica())){
					processo = this.fceTurismoServiceFacade.obterProcessoAtoConcedidoByLocalizacao(this.empreendimento.getIdeLocalizacaoGeografica());
					if(!Util.isNullOuVazio(processo)){
						this.indConcedidoEmpreendimento = true;
					}
				}
			}
			
			if(!indConcedidoEmpreendimento){
				if(!Util.isNullOuVazio(this.listaPoligonalDaNotificacao)){
					for(ArquivoProcesso arquivoProcesso: this.listaPoligonalDaNotificacao){
						if(!Util.isNullOuVazio(arquivoProcesso.getIdeLocalizacaoGeografica())){
							processo = this.fceTurismoServiceFacade.obterProcessoAtoConcedidoByLocalizacao(arquivoProcesso.getIdeLocalizacaoGeografica());
							if(!Util.isNullOuVazio(processo)){
								arquivoProcesso.setIndConcedido(true);
							}
						}
					}
				}
			}
		}catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/********************************
	 * 								*
	 * XXX: Processo Ato Concedido	*
	 * 								*
	 ********************************/
	
	private void carregarListaPoligonalDaNotificacao() {
		listaPoligonalDaNotificacao = new ArrayList<ArquivoProcesso>();
		
		List<ArquivoProcesso> list = (List<ArquivoProcesso>) fceTurismoServiceFacade.listarArquivoProcessoPorProcesso(
				fceTurismo.getIdeFce().getIdeAnaliseTecnica().getIdeProcesso());
		
		for (ArquivoProcesso ap : list) {
			if(!Util.isNullOuVazio(ap.getMotivoNotificacao()) 
					&& ap.getMotivoNotificacao().getIdeMotivoNotificacao().equals(MotivoNotificacaoEnum.SHAPE_TURISMO.getId())) {
				
				listaPoligonalDaNotificacao.add(ap);
			}
		}
	}

	public boolean isRenderedListaPoligonalDaNotificacao() {
		return !Util.isNullOuVazio(listaPoligonalDaNotificacao);
	}
	
	public void changeConcedidoEmpreendimento(ValueChangeEvent eve) {
		
		if((Boolean) eve.getNewValue()) {
			indConcedidoEmpreendimento = true;
			for (ArquivoProcesso ap : listaPoligonalDaNotificacao) ap.setIndConcedido(false);
		} else {
			indConcedidoEmpreendimento = false;
		}
	}
	
	public void changeConcedidoNotificacoes(AjaxBehaviorEvent e) {
		
		apConcedido = (ArquivoProcesso) e.getComponent().getAttributes().get("attributeAP");
		
		if(apConcedido != null && apConcedido.isIndConcedido()) {
			indConcedidoEmpreendimento = false;
			
			for (ArquivoProcesso ap : listaPoligonalDaNotificacao) {
				if(!ap.getIdeArquivoProcesso().equals(apConcedido.getIdeArquivoProcesso())) {
					ap.setIndConcedido(false);
				}
			}
		}
	}
	
	private boolean validaIndConcedido() {
		
		if(indConcedidoEmpreendimento) return true;
		
		for (ArquivoProcesso ap : listaPoligonalDaNotificacao) {
			if(ap.isIndConcedido()) return true;
		}
		
		return false;
	}
	
	private void preparaParaSalvarProcessoAtoConcedido() {
		LocalizacaoGeografica locGeo = null;
		listProcessoAtoConcedido = new ArrayList<ProcessoAtoConcedido>();
		
		if(indConcedidoEmpreendimento) {
			locGeo = empreendimento.getIdeLocalizacaoGeografica();
		} else {
			for (ArquivoProcesso ap : listaPoligonalDaNotificacao) {
				if(ap.isIndConcedido()) locGeo = ap.getIdeLocalizacaoGeografica();
			}
		}
		
		for (ProcessoAto pa : fceTurismoServiceFacade.listarProcessoAtoPorFce(super.fce)) {
			listProcessoAtoConcedido.add(
					new ProcessoAtoConcedido(null, locGeo, pa, null, null, new TipoAreaConcedida(TipoAreaConcedidaEnum.EMPREENDIMENTO)));
		}
	}

	/************************
	 * 						*
	 * XXX: Gets and Sets	*
	 * 						*
	 ************************/
	
	public FceTurismo getFceTurismo() {
		return fceTurismo;
	}

	public void setFceTurismo(FceTurismo fceTurismo) {
		this.fceTurismo = fceTurismo;
	}

	public List<TipoOrigemEnergia> getListaTipoOrigemEnergia() {
		return listaTipoOrigemEnergia;
	}

	public boolean isLi() {
		return li;
	}

	public boolean isZonaRural() {
		return zonaRural;
	}

	public boolean isZonaUrbana() {
		return zonaUrbana;
	}

	public List<TipoOrigemEnergia> getListaTipoOrigemEnergiaEscolhidos() {
		return listaTipoOrigemEnergiaEscolhidos;
	}

	public void setListaTipoOrigemEnergiaEscolhidos(List<TipoOrigemEnergia> listaTipoOrigemEnergiaEscolhidos) {
		this.listaTipoOrigemEnergiaEscolhidos = listaTipoOrigemEnergiaEscolhidos;
	}

	public boolean isConjuntoHabitacional() {
		return conjuntoHabitacional;
	}

	public void setConjuntoHabitacional(boolean conjuntoHabitacional) {
		this.conjuntoHabitacional = conjuntoHabitacional;
	}

	public List<TipoApp> getListaTipoAppsEscolhidos() {
		return listaTipoAppsEscolhidos;
	}

	public void setListaTipoAppsEscolhidos(List<TipoApp> listaTipoAppsEscolhidos) {
		this.listaTipoAppsEscolhidos = listaTipoAppsEscolhidos;
	}

	public List<TipoApp> getListaTipoApps() {
		return listaTipoApps;
	}

	public TipoApp getTipoApp() {
		return tipoApp;
	}

	public void setTipoApp(TipoApp tipoApp) {
		this.tipoApp = tipoApp;
	}

	public List<FceTurismoLocalizacaoGeografica> getListaFceTurismoLocalizacaoGeografica() {
		return listaFceTurismoLocalizacaoGeografica;
	}

	public void setListaFceTurismoLocalizacaoGeografica(List<FceTurismoLocalizacaoGeografica> listaFceTurismoLocalizacaoGeografica) {
		this.listaFceTurismoLocalizacaoGeografica = listaFceTurismoLocalizacaoGeografica;
	}

	public FceTurismoLocalizacaoGeografica getFceTurismoLocalizacaoGeografica() {
		return fceTurismoLocalizacaoGeografica;
	}

	public void setFceTurismoLocalizacaoGeografica(FceTurismoLocalizacaoGeografica fceTurismoLocalizacaoGeografica) {
		this.fceTurismoLocalizacaoGeografica = fceTurismoLocalizacaoGeografica;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public BigDecimal getAreaEmpreendimentoG11() {
		return areaEmpreendimentoG11;
	}

	public BigDecimal getAreaEmpreendimentoG21() {
		return areaEmpreendimentoG21;
	}

	public BigDecimal getAreaEmpreendimentoG22() {
		return areaEmpreendimentoG22;
	}

	public BigDecimal getAreaEmpreendimentoG23() {
		return areaEmpreendimentoG23;
	}

	public BigDecimal getAreaEmpreendimentoG24() {
		return areaEmpreendimentoG24;
	}

	public boolean isHabilitacaoInteresseSocial() {
		return habilitacaoInteresseSocial;
	}

	public void setHabilitacaoInteresseSocial(boolean habilitacaoInteresseSocial) {
		this.habilitacaoInteresseSocial = habilitacaoInteresseSocial;
	}

	public List<CategoriaTurismo> getListaTipologiaG11selected() {
		return listaTipologiaG11selected;
	}

	public void setListaTipologiaG11selected(List<CategoriaTurismo> listaTipologiaG11selected) {
		this.listaTipologiaG11selected = listaTipologiaG11selected;
	}

	public List<CategoriaTurismo> getListaTipologiaG21selected() {
		return listaTipologiaG21selected;
	}

	public void setListaTipologiaG21selected(List<CategoriaTurismo> listaTipologiaG21selected) {
		this.listaTipologiaG21selected = listaTipologiaG21selected;
	}

	public List<CategoriaTurismo> getListaTipologiaG22selected() {
		return listaTipologiaG22selected;
	}

	public void setListaTipologiaG22selected(List<CategoriaTurismo> listaTipologiaG22selected) {
		this.listaTipologiaG22selected = listaTipologiaG22selected;
	}

	public List<CategoriaTurismo> getListaTipologiaG11() {
		return listaTipologiaG11;
	}

	public List<CategoriaTurismo> getListaTipologiaG21() {
		return listaTipologiaG21;
	}

	public List<CategoriaTurismo> getListaTipologiaG22() {
		return listaTipologiaG22;
	}

	public boolean isCessionario() {
		return cessionario;
	}

	public List<ArquivoProcesso> getListaPoligonalDaNotificacao() {
		return listaPoligonalDaNotificacao;
	}

	public void setListaPoligonalDaNotificacao(List<ArquivoProcesso> listaPoligonalDaNotificacao) {
		this.listaPoligonalDaNotificacao = listaPoligonalDaNotificacao;
	}

	public boolean isIndConcedidoEmpreendimento() {
		return indConcedidoEmpreendimento;
	}

	public void setIndConcedidoEmpreendimento(boolean indConcedidoEmpreendimento) {
		this.indConcedidoEmpreendimento = indConcedidoEmpreendimento;
	}

	public ArquivoProcesso getApConcedido() {
		return apConcedido;
	}

	public void setApConcedido(ArquivoProcesso apConcedido) {
		this.apConcedido = apConcedido;
	}

	public List<ProcessoAtoConcedido> getListProcessoAtoConcedido() {
		return listProcessoAtoConcedido;
	}

	public void setListProcessoAtoConcedido(List<ProcessoAtoConcedido> listProcessoAtoConcedido) {
		this.listProcessoAtoConcedido = listProcessoAtoConcedido;
	}
}