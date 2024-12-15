package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DualListModel;

import br.gov.ba.seia.dto.TipoAtoeAtoAmbientalDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Cnae;
import br.gov.ba.seia.entity.FormaManejo;
import br.gov.ba.seia.entity.NivelCompetencia;
import br.gov.ba.seia.entity.NivelTipologia;
import br.gov.ba.seia.entity.ParametroReferencia;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.PorteCompetencia;
import br.gov.ba.seia.entity.PorteCompetenciaPK;
import br.gov.ba.seia.entity.PorteTipologia;
import br.gov.ba.seia.entity.PotencialPoluicao;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.TipologiaGrupoArea;
import br.gov.ba.seia.entity.TipologiaTipoAto;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.entity.UnidadeMedidaTipologiaGrupo;
import br.gov.ba.seia.entity.VwFormaManejo;
import br.gov.ba.seia.entity.VwTipologiaCnae;
import br.gov.ba.seia.entity.VwTipologiaTipoAtoUnidadeMedida;
import br.gov.ba.seia.facade.CnaeServiceFacede;
import br.gov.ba.seia.facade.FormaManejoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.CnaeService;
import br.gov.ba.seia.service.FormaManejoService;
import br.gov.ba.seia.service.NivelTipologiaService;
import br.gov.ba.seia.service.ParametroReferenciaService;
import br.gov.ba.seia.service.PorteCompetenciaService;
import br.gov.ba.seia.service.PorteService;
import br.gov.ba.seia.service.PorteTipologiaService;
import br.gov.ba.seia.service.PotencialPoluicaoService;
import br.gov.ba.seia.service.TipoAtoService;
import br.gov.ba.seia.service.TipologiaGrupoAreaService;
import br.gov.ba.seia.service.TipologiaGrupoService;
import br.gov.ba.seia.service.TipologiaPaiService;
import br.gov.ba.seia.service.TipologiaTipoAtoService;
import br.gov.ba.seia.service.UnidadeMedidaService;
import br.gov.ba.seia.service.UnidadeMedidaTipologiaGrupoService;
import br.gov.ba.seia.service.VwFormaManejoService;
import br.gov.ba.seia.service.VwTipologiaCnaeService;
import br.gov.ba.seia.service.VwTipologiaTipoAtoUnidadeMedidaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("tipologiaPaiController")
@ViewScoped
public class TipologiaPaiController {

	@EJB
	private CnaeService cnaeService;
	@EJB
	private CnaeServiceFacede cnaeServiceFacede;
	@EJB
	private NivelTipologiaService nivelTipologiaService;
	@EJB
	private UnidadeMedidaService unidadeMedidaService;
	@EJB
	private FormaManejoService formaManejoService;
	@EJB
	private PotencialPoluicaoService potencialPoluicaoService;
	@EJB
	private AreaService areaService;// nao fez DAOImpl
	@EJB
	private TipoAtoService tipoAtoService;
	@EJB
	private AtoAmbientalService atoAmbientalService;// nao fez DAOImpl
	@EJB
	private TipologiaGrupoService tipologiaGrupoService;// nao fez DAOImpl
	@EJB
	private UnidadeMedidaTipologiaGrupoService unidadeMedidaTipologiaGrupoService;
	@EJB
	private TipologiaGrupoAreaService tipologiaGrupoAreaService;
	@EJB
	private PorteService porteService;
	@EJB
	private VwTipologiaTipoAtoUnidadeMedidaService vwTipologiaTipoAtoUnidadeMedidaService;
	@EJB
	private PorteTipologiaService porteTipologiaService;
	@EJB
	private PorteCompetenciaService porteCompetenciaService;
	@EJB
	private TipologiaPaiService tipologiaPaiService;

	@EJB
	private TipologiaTipoAtoService tipologiaTipoAtoService;
	@EJB
	private VwFormaManejoService vwFormaManejoService;
	@EJB
	private VwTipologiaCnaeService vwTipologiaCnaeService;
	@EJB
	private ParametroReferenciaService parametroReferenciaService;
	@EJB
	FormaManejoServiceFacade formaManejoServiceFacade;// aqui parou

	private String codTipologia;
	private String desTipologia;
	private String ideNivelTipologia;
	private Cnae cnae;
	private Tipologia tipologia;
	private TipologiaGrupo tipologiaGrupo;
	private Tipologia tipologiaPaiSelecionado;

	private VwFormaManejo vwFormaManejoSelecionado;
	private VwFormaManejo vwFormaManejoExcluir;

	private FormaManejo formaManejoSelecionado;
	private PotencialPoluicao potencialPoluicaoSelecionado;
	private NivelTipologia nivelTipologiaSelecionado;
	private VwTipologiaCnae vwTipologiaCnaeSelecionado;

	private DataModel<Tipologia> cnaeData;
	private DataModel<VwFormaManejo> modelVwFormaManejo;
	private DataModel<VwTipologiaCnae> modelVwTipologiaCnae;

	private Integer indexAba;

	private boolean habilitaCnae;
	private boolean habilitaManejo;
	private boolean habilitaPorte;
	private boolean habilitaPicklistUndMedida;
	private boolean habilitaCheckboxUndMedida;
	private String isAtividade;
	private boolean unidMedidaSimNao;
	private boolean potencialPoluicaoSimNao;
	private boolean isTipoAtoRendered;
	private boolean viewMode;
	private boolean viewCnae;
	private boolean redireatividade;

	private boolean disbleTipologiaPai;
	private boolean disbleEUmAtividade;
	private boolean disbleBtTipologia;

	private boolean disblePossuiFormas;
	private boolean showExpandirCadFormaManejo;
	private boolean disableIncluirCadFormaManejo;

	private boolean disableFormCadFormaManejo;
	private boolean disableBtForm;

	private boolean disableTabFormaManejo;

	private List<Porte> listaPorte;

	// private List<NivelCompetencia> listaNivelCompetencia;

	private List<VwTipologiaTipoAtoUnidadeMedida> listaVwTipologiaTipoAtoUnidadeMedida;

	// ----------------funcionalidade checkbox tipologia é uma
	// atividade----------------------
	private String tipologiaTemFilhos;
	private String tipoAtoChange;

	// -----------COMBOS---------------------------
	private List<SelectItem> tipologiaItens;
	private List<SelectItem> formaManejoItens;
	private List<SelectItem> potencialPoluicaoItens;
	private List<SelectItem> nivelTipologiaItens;

	// -----------CNAE---------------------------
	private Cnae cnaeSecao;
	private List<SelectItem> listaSecao;
	private Cnae cnaeDivisao;
	private List<SelectItem> listaDivisao;
	private Cnae cnaeGrupo;
	private List<SelectItem> listaGrupo;

	private Cnae cnaeClasse;
	private List<SelectItem> listaClasse;
	private Cnae cnaeSubclasse;
	private List<SelectItem> listaSubclasse;

	private Cnae cnaeSelecionado;
	private Cnae cnaeCodPai;

	// ------------picklist---------------------
	private DualListModel<UnidadeMedida> unidMedidas;
	private DualListModel<Area> area;
	private DualListModel<TipoAto> tipoAto;
	private DualListModel<AtoAmbiental> atoAmbiental;
	private DualListModel<TipoAtoeAtoAmbientalDTO> tipoAtoeAtoAmbientalDTO;

	private boolean excluirPicklist;

	private Collection<UnidadeMedida> listaUnMedidaSource;
	private Collection<UnidadeMedida> listaUnMedidaTarget;

	private Collection<Area> listaAreaTarget;
	private Collection<Area> listaAreaSource;

	private Collection<TipoAto> listaTipoAtoTarget;
	private Collection<TipoAto> listaTipoAtoSource;

	private Collection<AtoAmbiental> listaAtoAmbientalTarget;
	private Collection<AtoAmbiental> listaAtoAmbientalSource;

	// ////////////////////////////////////////
	// VARIAVEIS DE PORTE //
	// ////////////////////////////////////////

	private BigDecimal microMinimo;
	private BigDecimal microMaximo;
	private BigDecimal pequenoMinimo;
	private BigDecimal pequenoMaximo;
	private BigDecimal medioMinimo;
	private BigDecimal medioMaximo;
	private BigDecimal grandeMinimo;
	private BigDecimal grandeMaximo;
	private BigDecimal excepcionalMinimo;
	private BigDecimal excepcionalMaximo;
	private Collection<PorteTipologia> listaPorteForma;

	// ////////////////////////////////////////
	// VARIAVEIS DE COMPETENCIA //
	// ////////////////////////////////////////
	private Boolean nivel1MicroMunicipal;
	private Boolean nivel1PequenoMunicipal;
	private Boolean nivel1MedioMunicipal;
	private Boolean nivel1GrandeMunicipal;
	private Boolean nivel1ExcepcionalMunicipal;

	private Boolean nivel2MicroMunicipal;
	private Boolean nivel2PequenoMunicipal;
	private Boolean nivel2MedioMunicipal;
	private Boolean nivel2GrandeMunicipal;
	private Boolean nivel2ExcepcionalMunicipal;

	private Boolean nivel3MicroMunicipal;
	private Boolean nivel3PequenoMunicipal;
	private Boolean nivel3MedioMunicipal;
	private Boolean nivel3GrandeMunicipal;
	private Boolean nivel3ExcepcionalMunicipal;

	private Boolean nivel4estadualMicro;
	private Boolean nivel4estadualPequeno;
	private Boolean nivel4estadualMedio;
	private Boolean nivel4estadualGrande;
	private Boolean nivel4estadualExcepcional;

	private Collection<PorteCompetencia> listaPorteCompetencia;
	private Collection<ParametroReferencia> listarParametroReferenciaForma;
	private UnidadeMedidaTipologiaGrupo listaUnMedidaTipologia;
	private TipologiaGrupoArea listaAreaTipologia;
	private TipologiaTipoAto listaTipologiaTipoAto;
	private TipologiaTipoAto listaTipologiaAtoAmbiental;

	private PorteCompetencia listPorteCompentencia;

	@PostConstruct
	public void init() {

		limpar();

	}

	public void limpar() {
		tipologia = new Tipologia();
		
		carregarItensTipologia();
		carregarItensFormaManejo();
		carregarItensPotencialPoluicao();
		carregarPorte();

		carregarVwTipologiaCnae();
		carregarItensNivelTipologia();
		tratarTipologiaSelecionada();

		nivelTipologiaSelecionado = new NivelTipologia();

		cnaeSecao = new Cnae();
		cnaeDivisao = new Cnae();
		cnaeGrupo = new Cnae();
		cnaeClasse = new Cnae();
		setCnaeSubclasse(new Cnae());
		carregarListaSecao();
		cnaeSubclasse = new Cnae();

		carregarVwFormaManejo();
		unidMedidaSimNao = false;

		viewMode = Boolean.FALSE;
		viewCnae = Boolean.TRUE;

		indexAba = 0;
	}

	// -------------metodos pra popular os combos
	public void carregarItensTipologia() {
		try {
			this.tipologiaItens = new ArrayList<SelectItem>();
			Tipologia tp = new Tipologia(0);
			tp.setDesTipologia("Selecione...");
			tp.setIndExcluido(Boolean.FALSE);
			this.tipologiaItens.add(new SelectItem(tp, tp.getDesTipologia()));

			for (Tipologia tipologiaList : tipologiaPaiService.listaTipologiaPaiPossuiFilho()) {
				this.tipologiaItens.add(new SelectItem(tipologiaList, tipologiaList.getCodTipologia() + " : " + tipologiaList.getDesTipologia()));
			}
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void carregarItensFormaManejo() {
		try {
			this.formaManejoItens = new ArrayList<SelectItem>();
			FormaManejo fm = new FormaManejo(0);
			fm.setNomTipoManejo("Selecione...");

			this.formaManejoItens.add(new SelectItem(0, fm.getNomTipoManejo()));

			for (FormaManejo formaManejo : formaManejoService.listaFormaManejo()) {
				this.formaManejoItens.add(new SelectItem(formaManejo.getIdeTipoManejo(), formaManejo.getNomTipoManejo()));
			}
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void carregarItensPotencialPoluicao() {
		try {
			this.potencialPoluicaoItens = new ArrayList<SelectItem>();
			PotencialPoluicao pp = new PotencialPoluicao(0);
			pp.setNomPotencialPoluicao("Selecione...");

			this.potencialPoluicaoItens.add(new SelectItem(0, pp.getNomPotencialPoluicao()));

			for (PotencialPoluicao potencialPoluicao : potencialPoluicaoService.listaPotencialPoluicao()) {
				this.potencialPoluicaoItens.add(new SelectItem(potencialPoluicao.getIdePotencialPoluicao(), potencialPoluicao.getNomPotencialPoluicao()));
			}
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void carregarItensNivelTipologia() {
		try {
			this.nivelTipologiaItens = new ArrayList<SelectItem>();
			NivelTipologia nt = new NivelTipologia(0);
			nt.setNomNivelTecnologia("Selecione...");
			this.nivelTipologiaItens.add(new SelectItem(nt, nt.getNomNivelTecnologia()));

			for (NivelTipologia nivelTipologiaList : nivelTipologiaService.listaNivelTipologia()) {
				this.nivelTipologiaItens.add(new SelectItem(nivelTipologiaList, nivelTipologiaList.getNumNivelTipologia() + ": "
						+ nivelTipologiaList.getNomNivelTecnologia()));
			}
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void carregarPorte() {
		try {
			this.listaPorte = porteService.listarPorte(6);
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void carregarlistaVwTipologiaTipoAtoUnidadeMedida() {
		try {
			// this.listaVwTipologiaTipoAtoUnidadeMedida =
			// vwTipologiaTipoAtoUnidadeMedidaService.listarVwTipologiaTipoAtoUnidadeMedida(tipologia.getIdeTipologia());
			this.listaVwTipologiaTipoAtoUnidadeMedida = vwTipologiaTipoAtoUnidadeMedidaService.listarVwTipologiaTipoAtoUnidadeMedida(
					tipologia.getIdeTipologia(), tipologiaGrupo.getIdeTipologiaGrupo());

		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void carregarVwFormaManejo() {
		try {
			modelVwFormaManejo = Util.castListToDataModel(vwFormaManejoService.listarVwFormaManejo(tipologia.getIdeTipologia()));

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}

	}

	public void carregarVwTipologiaCnae() {
		try {
			modelVwTipologiaCnae = Util.castListToDataModel(vwTipologiaCnaeService.listarVwTipologiaCnae());
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void pesquisarVwTipologiaCnae() {
		try {
			VwTipologiaCnae vwTipologiaCnae = new VwTipologiaCnae();
			vwTipologiaCnae.setCodTipologia(codTipologia);
			vwTipologiaCnae.setDesTipologia(desTipologia);
			if (nivelTipologiaSelecionado != null) {
				vwTipologiaCnae.setIdeNivelTipologia(nivelTipologiaSelecionado.getIdeNivelTipologia());
			}
			modelVwTipologiaCnae = Util.castListToDataModel(vwTipologiaCnaeService.filtrarListaVwTipologiaCnae(vwTipologiaCnae));
		} catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public String prepararParaInserirTipologia() {
		try {
			
			  disableBtForm = Boolean.TRUE; 
			  ContextoUtil.getContexto().setTipologia(null) ; 
			 
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
		return "/paginas/manter-atividadelicenciamento/cadastro.xhtml";
	}

	public String prepararParaEditarTipologia() {
		try {
			tipologia = tipologiaPaiService.carregarTipologiaPorId(vwTipologiaCnaeSelecionado.getIdeTipologia());
			disableBtForm = Boolean.TRUE;
			if (tipologia != null) {
				ContextoUtil.getContexto().setTipologia(tipologia);
			}

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
		return "/paginas/manter-atividadelicenciamento/cadastro.xhtml?isEdit=true";
	}

	public void tratarTipologiaSelecionada() {

		tipologia = ContextoUtil.getContexto().getTipologia();
		tipologiaGrupo = ContextoUtil.getContexto().getTipologiaGrupo();
		if (Util.isNull(tipologiaGrupo)) {
			tipologiaGrupo = new TipologiaGrupo();
		}

		try {

			formaManejoSelecionado = new FormaManejo();
			potencialPoluicaoSelecionado = new PotencialPoluicao();

			if (Util.isNull(tipologia)) {
				// /Inserindo Tipologia nova

				tipologia = new Tipologia();
				habilitaCnae = Boolean.TRUE;
				habilitaManejo = Boolean.TRUE;
				disbleBtTipologia = Boolean.TRUE;
				disableBtForm = Boolean.FALSE;
				disblePossuiFormas = Boolean.TRUE;
				disableFormCadFormaManejo = Boolean.TRUE;
				showExpandirCadFormaManejo = Boolean.TRUE;

				disableIncluirCadFormaManejo = Boolean.TRUE;

			} else {

				// /Editando Tipologia
				tipologiaPaiSelecionado = tipologia.getIdeTipologiaPai();
				for (TipologiaGrupo listTipologiaGrupo : tipologiaGrupoService.listaTipologiaGrupoTipologia(tipologia)) {

					if (!porteTipologiaService.listarPorteCompetenciaTipologiaGrupo(listTipologiaGrupo).isEmpty()) {
						// tipoAtoChange = "1";
					}
				}

				if (tipologia.getIndPossuiFilhos()) {
					// IND_POSSUI_FILHOS = TRUE
					tipologiaTemFilhos = "2";
					habilitaCnae = Boolean.TRUE;
					habilitaManejo = Boolean.TRUE;

				} else {

					// IND_POSSUI_FILHOS = FALSE
					if (tipologiaGrupoService.listaTipologiaGrupoTipologia(tipologia).size() > 0) {
						if (tipologiaGrupoService.listaTipologiaGrupoTipologia(tipologia).size() == 1) {

							disableTabFormaManejo = Boolean.FALSE;
							disableFormCadFormaManejo = Boolean.FALSE;
							showExpandirCadFormaManejo = Boolean.FALSE;

							TipologiaGrupo tpSelecionado = new TipologiaGrupo();
							Collection<TipologiaGrupo> tg = tipologiaGrupoService.listaTipologiaGrupoTipologia(tipologia);
							for (TipologiaGrupo ListTipGrupo : tg) {
								tpSelecionado = tipologiaGrupoService.carregarTipologiaGrupoId(ListTipGrupo.getIdeTipologiaGrupo());
							}

							if (tpSelecionado != null) {

								// ContextoUtil.getContexto().setTipologiaGrupo(tipologiaGrupo)
								// ;

								tipologiaGrupo = tpSelecionado;

								editarPicklistFormamanejo(tpSelecionado);
								editaCompetenciasFormaMenejo(tpSelecionado);
								editarPorteFormaManejo(tpSelecionado);

								editaValorReferenciaFormaMenejo(tpSelecionado);

								isDesabilitaComponentesPicklistUnMedida();
								disableFormCadFormaManejo = Boolean.FALSE;

								showExpandirCadFormaManejo = Boolean.FALSE;
								disableIncluirCadFormaManejo = Boolean.FALSE;

								isTipoAtoRendered = Boolean.TRUE;

								carregarlistaVwTipologiaTipoAtoUnidadeMedida();
							}
						} else {
							disableTabFormaManejo = Boolean.TRUE;
							disableFormCadFormaManejo = Boolean.TRUE;
							showExpandirCadFormaManejo = Boolean.TRUE;
							disableIncluirCadFormaManejo = Boolean.TRUE;
						}

					}

					tipologiaTemFilhos = "1";
					habilitaCnae = Boolean.FALSE;
					habilitaManejo = Boolean.FALSE;
				}

				isAtividade = "";
				disbleBtTipologia = Boolean.TRUE;
				disbleBtTipologia = Boolean.TRUE;
				disbleEUmAtividade = Boolean.TRUE;
				disblePossuiFormas = Boolean.FALSE;

				disbleTipologiaPai = Boolean.TRUE;
			}

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void onTabChange(TabChangeEvent event) {

		String activeTab = event.getTab().getId();
		int activeTabIndex = 0;
		for (UIComponent comp : event.getTab().getParent().getChildren()) {
			if (comp.getId().equals(activeTab)) {
				break;
			}
			activeTabIndex++;
		}
		setIndexAba(activeTabIndex);

	}

	public String prepararParaEditarFormaManejo() {

		try {
			tipologiaGrupo = tipologiaGrupoService.carregarTipologiaGrupoId(vwFormaManejoSelecionado.getIdeTipologiaGrupo());

			if (tipologiaGrupo != null) {
				ContextoUtil.getContexto().setTipologiaGrupo(tipologiaGrupo);

				disableFormCadFormaManejo = Boolean.FALSE;
				showExpandirCadFormaManejo = Boolean.FALSE;

				editarPicklistFormamanejo(tipologiaGrupo);
				editaCompetenciasFormaMenejo(tipologiaGrupo);
				editarPorteFormaManejo(tipologiaGrupo);
				editaValorReferenciaFormaMenejo(tipologiaGrupo);

				isDesabilitaComponentesPicklistUnMedida();

				isTipoAtoRendered = Boolean.TRUE;

			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}

		return "";
	}

	private void editaValorReferenciaFormaMenejo(TipologiaGrupo tipologiaGrupo) {
		try {
			listarParametroReferenciaForma = parametroReferenciaService.listarParamentroReferenciaTipologiaGrupo(tipologiaGrupo);
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void editarPorteFormaManejo(TipologiaGrupo tipologiaGrupo) {
		try {
			listaPorteForma = porteTipologiaService.listarPorteCompetenciaTipologiaGrupo(tipologiaGrupo);
			for (PorteTipologia porteTipologia : listaPorteForma) {
				if (porteTipologia.getIdePorte().getIdePorte() == 1) {
					if (!Util.isNull(porteTipologia.getValMinimo())) {
						microMinimo = porteTipologia.getValMinimo();
					}
					if (!Util.isNull(porteTipologia.getValMaximo())) {
						microMaximo = porteTipologia.getValMaximo();
					}
				}

				if (porteTipologia.getIdePorte().getIdePorte() == 2) {
					if (!Util.isNull(porteTipologia.getValMinimo())) {
						pequenoMinimo = porteTipologia.getValMinimo();
					}
					if (!Util.isNull(porteTipologia.getValMaximo())) {
						pequenoMaximo = porteTipologia.getValMaximo();
					}
				}

				if (porteTipologia.getIdePorte().getIdePorte() == 3) {
					if (!Util.isNull(porteTipologia.getValMinimo())) {
						medioMinimo = porteTipologia.getValMinimo();
					}
					if (!Util.isNull(porteTipologia.getValMaximo())) {
						medioMaximo = porteTipologia.getValMaximo();
					}
				}

				if (porteTipologia.getIdePorte().getIdePorte() == 4) {
					if (!Util.isNull(porteTipologia.getValMinimo())) {
						grandeMinimo = porteTipologia.getValMinimo();
					}
					if (!Util.isNull(porteTipologia.getValMaximo())) {
						grandeMaximo = porteTipologia.getValMaximo();
					}
				}

				if (porteTipologia.getIdePorte().getIdePorte() == 5) {
					if (!Util.isNull(porteTipologia.getValMinimo())) {
						excepcionalMinimo = porteTipologia.getValMinimo();
					}
					if (!Util.isNull(porteTipologia.getValMaximo())) {
						excepcionalMaximo = porteTipologia.getValMaximo();
					}
				}

			}

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}

	}

	private void editaCompetenciasFormaMenejo(TipologiaGrupo tipologiaGrupo) {
		try {
			listaPorteCompetencia = porteCompetenciaService.listarPorteCompetenciaTipologiaGrupo(tipologiaGrupo);
			for (PorteCompetencia porteCompetencia : listaPorteCompetencia) {
				// //////////////////////////////////////////////////////////////////////////////////////////////////
				// Nivel - 1 ///
				// /////////////////////////////////////////////////////////////////////////////////////////////////
				// Nivel = 1 ;Porte = 1 Micro ; Tipo_competencia = 1 Federal
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 1) && (porteCompetencia.getPorte().getIdePorte() == 1)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel1MicroMunicipal = true;
				}
				// Tipo_competencia = 1 Federal - Nivel = 1 - Porte = 2 Pequeno
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 1) && (porteCompetencia.getPorte().getIdePorte() == 2)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel1PequenoMunicipal = true;
				}
				// Tipo_competencia = 1 Federal - Nivel = 1 - Porte = 3 M�dio
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 1) && (porteCompetencia.getPorte().getIdePorte() == 3)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel1MedioMunicipal = true;
				}
				// Tipo_competencia = 1 Federal - Nivel = 1 - Porte = 4 Grande
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 1) && (porteCompetencia.getPorte().getIdePorte() == 4)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel1GrandeMunicipal = true;
				}
				// Tipo_competencia = 1 Federal - Nivel = 1 - Porte = 4
				// Excepcional
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 1) && (porteCompetencia.getPorte().getIdePorte() == 5)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel1ExcepcionalMunicipal = true;
				}
				// //////////////////////////////////////////////////////////////////////////////////////////////////
				// Nivel - 2 ///
				// /////////////////////////////////////////////////////////////////////////////////////////////////
				// Tipo_competencia = 1 Federal - Nivel = 2 - Porte = 1 Micro
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 2) && (porteCompetencia.getPorte().getIdePorte() == 1)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel2MicroMunicipal = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 2 - Porte = 2 Pequeno
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 2) && (porteCompetencia.getPorte().getIdePorte() == 2)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel2PequenoMunicipal = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 2 - Porte = 3 M�dio
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 2) && (porteCompetencia.getPorte().getIdePorte() == 3)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel2MedioMunicipal = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 2 - Porte = 4 Grande
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 2) && (porteCompetencia.getPorte().getIdePorte() == 4)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel2GrandeMunicipal = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 2 - Porte = 4
				// Excepcional
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 2) && (porteCompetencia.getPorte().getIdePorte() == 5)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {

					nivel2ExcepcionalMunicipal = true;
				}

				// //////////////////////////////////////////////////////////////////////////////////////////////////
				// Nivel - 3 ///
				// /////////////////////////////////////////////////////////////////////////////////////////////////
				// Tipo_competencia = 1 Federal - Nivel = 3 - Porte = 1 Micro
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 3) && (porteCompetencia.getPorte().getIdePorte() == 1)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel3MicroMunicipal = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 2 - Porte = 2 Pequeno
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 3) && (porteCompetencia.getPorte().getIdePorte() == 2)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel3PequenoMunicipal = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 2 - Porte = 3 M�dio
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 3) && (porteCompetencia.getPorte().getIdePorte() == 3)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel3MedioMunicipal = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 2 - Porte = 4 Grande
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 3) && (porteCompetencia.getPorte().getIdePorte() == 4)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {
					nivel3GrandeMunicipal = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 2 - Porte = 4
				// Excepcional
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 3) && (porteCompetencia.getPorte().getIdePorte() == 5)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 3)) {

					nivel3ExcepcionalMunicipal = true;
				}

				// //////////////////////////////////////////////////////////////////////////////////////////////////
				// Nivel - 4 ///
				// /////////////////////////////////////////////////////////////////////////////////////////////////
				// Tipo_competencia = 1 Federal - Nivel = 4 - Porte = 1 Micro
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 4) && (porteCompetencia.getPorte().getIdePorte() == 1)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 2)) {
					nivel4estadualMicro = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 4 - Porte = 2 Pequeno
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 4) && (porteCompetencia.getPorte().getIdePorte() == 2)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 2)) {
					nivel4estadualPequeno = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 4 - Porte = 3 M�dio
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 4) && (porteCompetencia.getPorte().getIdePorte() == 3)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 2)) {
					nivel4estadualMedio = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 4 - Porte = 4 Grande
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 4) && (porteCompetencia.getPorte().getIdePorte() == 4)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 2)) {
					nivel4estadualGrande = true;
				}

				// Tipo_competencia = 1 Federal - Nivel = 4 - Porte = 4
				// Excepcional
				if ((porteCompetencia.getNivelCompetencia().getIdeNivelCompetencia() == 4) && (porteCompetencia.getPorte().getIdePorte() == 5)
						&& (porteCompetencia.getNivelCompetencia().getIdeTipoCompetencia().getIdeTipoCompetencia() == 2)) {

					nivel4estadualExcepcional = true;
				}
			}

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}

	}

	private void editarPicklistFormamanejo(TipologiaGrupo tipologiaGrupo) {
		try {

			if (tipologiaGrupo.getIdePotencialPoluicao() != null) {
				potencialPoluicaoSelecionado = tipologiaGrupo.getIdePotencialPoluicao();
			}

			listaUnMedidaTarget = unidadeMedidaService.ListarUnidadeMedidaTipologia(tipologiaGrupo);
			if (!listaUnMedidaTarget.isEmpty()) {
				listaUnMedidaSource = unidadeMedidaService.filtrarListaUnidadeMedidaSouce(listaUnMedidaTarget);
				unidMedidas = Util.castListToDualListModel(listaUnMedidaSource, listaUnMedidaTarget);
			}

			listaAreaTarget = areaService.listarAreaTipologiaGrupo(tipologiaGrupo);
			if (!listaAreaTarget.isEmpty()) {
				listaAreaSource = areaService.filtrarListaAreaSouce(listaAreaTarget);
				area = Util.castListToDualListModel(listaAreaSource, listaAreaTarget);
			}

			listaTipoAtoTarget = tipoAtoService.listarTipoAtoTipologiaGrupo(tipologiaGrupo);
			if (!listaTipoAtoTarget.isEmpty()) {
				listaTipoAtoSource = tipoAtoService.filtrarListaTipoAtoSouce(listaTipoAtoTarget);
				tipoAto = Util.castListToDualListModel(listaTipoAtoSource, listaTipoAtoTarget);
			}

			listaAtoAmbientalTarget = atoAmbientalService.listarAtoAmbientalTipologiaGrupo(tipologiaGrupo);
			if (!listaAtoAmbientalTarget.isEmpty()) {
				listaAtoAmbientalSource = atoAmbientalService.filtrarListaAtoAmbientalSouce(listaAtoAmbientalTarget);
				atoAmbiental = Util.castListToDualListModel(listaAtoAmbientalSource, listaAtoAmbientalTarget);
			}

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void removerTipologiaDaSessao() {
		ContextoUtil.getContexto().setTipologia(null);
	}

	// metodos para salvar///
	public void salvarCoordenacaoAreaTipologiaGrupo() {
		try {
			Collection<TipologiaGrupoArea> listTipologiaGrupoAreaSalvar = new ArrayList<TipologiaGrupoArea>();
			Collection<TipologiaGrupoArea> listTipologiaGrupoAreaDel = new ArrayList<TipologiaGrupoArea>();
			if (!area.getTarget().isEmpty()) {
				for (Area listArea : area.getTarget()) {
					listaAreaTipologia = tipologiaGrupoAreaService.listarAreaTipologiaGrupo(tipologiaGrupo, listArea.getIdeArea());
					if (listaAreaTipologia == null) {
						listTipologiaGrupoAreaSalvar.add(new TipologiaGrupoArea(tipologiaGrupo, listArea));
						excluirPicklist = false;
					} else {
						listTipologiaGrupoAreaDel.add(new TipologiaGrupoArea(tipologiaGrupo, listArea));
						excluirPicklist = true;
					}
				}

				if (!listTipologiaGrupoAreaSalvar.isEmpty()) {
					tipologiaGrupoAreaService.salvarTipologiaGrupoArea(listTipologiaGrupoAreaSalvar);
				}

				if (!listTipologiaGrupoAreaDel.isEmpty() && excluirPicklist) {
					Collection<TipologiaGrupoArea> lista = new ArrayList<TipologiaGrupoArea>();
					lista = tipologiaGrupoAreaService.filtrarAreaTipologiaGrupo(tipologiaGrupo, listTipologiaGrupoAreaDel);
					tipologiaGrupoAreaService.excluirAreaTipologiaGrupo(lista);
				}
			} else {
				tipologiaGrupoAreaService.excluirAreaTipologiaGrupo(tipologiaGrupo);
			}

			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void salvarTipoAto() {
		try {
			Collection<TipologiaTipoAto> listTipologiaTipoAtoSalvar = new ArrayList<TipologiaTipoAto>();
			Collection<TipologiaTipoAto> listTipologiaTipoAtoDel = new ArrayList<TipologiaTipoAto>();
			if (!tipoAto.getTarget().isEmpty()) {
				for (TipoAto listTipoAto : tipoAto.getTarget()) {
					listaTipologiaTipoAto = tipologiaTipoAtoService.listarTipologiaTipoAto(tipologiaGrupo, listTipoAto.getIdeTipoAto());
					if (listaTipologiaTipoAto == null) {
						listTipologiaTipoAtoSalvar.add(new TipologiaTipoAto(tipologiaGrupo, listTipoAto, null));
						excluirPicklist = false;
					} else {
						listTipologiaTipoAtoDel.add(new TipologiaTipoAto(tipologiaGrupo, listTipoAto, null));
						excluirPicklist = true;
					}
				}

				if (!listTipologiaTipoAtoSalvar.isEmpty()) {
					tipologiaTipoAtoService.salvarTipologiaTipoAtos(listTipologiaTipoAtoSalvar);
				}

				if (!listTipologiaTipoAtoDel.isEmpty() && excluirPicklist) {
					Collection<TipologiaTipoAto> lista = new ArrayList<TipologiaTipoAto>();
					lista = tipologiaTipoAtoService.filtrarTipologiaTipoAto(tipologiaGrupo, listTipologiaTipoAtoDel);
					tipologiaTipoAtoService.excluirTipologiaTipoAto(lista);
				}
			} else {
				tipologiaTipoAtoService.excluirTipologiaTipoAto(tipologiaGrupo);
			}
			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void salvarCnae() {

		try {

			if (validate()) {
				cnaeServiceFacede.salvarTipologiaCnae(cnaeSubclasse, tipologia);
				JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
			}

			isAtividade = "";
		} catch (Exception exception) {

			if (exception.getCause() instanceof ConstraintViolationException) {
				tipologia.getCnaeCollection().remove(cnaeSubclasse);
				JsfUtil.addWarnMessage("Já existe um registro para esse CNAE cadastrado.");
			} else {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

	}

	private Boolean validate() {
		Boolean retorno = Boolean.TRUE;
		if (Util.isNull(cnaeSecao.getIdeCnae()) || cnaeSecao.getIdeCnae() == 0) {
			JsfUtil.addErrorMessage("O campo Seção é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}

		if (Util.isNull(cnaeDivisao.getIdeCnae()) || cnaeDivisao.getIdeCnae() == 0) {
			JsfUtil.addErrorMessage("O campo Divisão é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}

		if (Util.isNull(cnaeGrupo.getIdeCnae()) || cnaeGrupo.getIdeCnae() == 0) {
			JsfUtil.addErrorMessage("O campo Grupo é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}

		if (Util.isNull(cnaeClasse.getIdeCnae()) || cnaeClasse.getIdeCnae() == 0) {
			JsfUtil.addErrorMessage("O campo Classe é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}

		if (Util.isNull(cnaeSubclasse.getIdeCnae()) || cnaeSubclasse.getIdeCnae() == 0) {
			JsfUtil.addErrorMessage("O campo Subclasse é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}

		return retorno;
	}

	public String salvarTipologia() {
		try {

			if (tipologiaTemFilhos != null) {
				tipologia.setDtcCriacao(new Date());
				tipologia.setIndExcluido(false);

				if (tipologia.getCnaeCollection() == null) {
					tipologia.setCnaeCollection(new ArrayList<Cnae>());
				}

				NivelTipologia nivelTipologia = null;

				if (tipologiaPaiSelecionado.getIdeTipologia() == 0) {
					nivelTipologia = nivelTipologiaService.filtrarNivelTipologia(1);
					tipologia.setIdeNivelTipologia(nivelTipologia);
				} else {
					tipologia.setIdeTipologiaPai(tipologiaPaiSelecionado);
				}
				tipologiaPaiService.salvarTipologia(tipologia);

				viewMode = true;

				JsfUtil.addSuccessMessage("Operação realizada com sucesso.");

				if (!tipologia.getIndPossuiFilhos()) {
					this.isDesabilitaComponentesTipologiaAbaIdentificacao();
					indexAba = 1;
				}

				if (redireatividade) {
					return "/paginas/manter-atividadelicenciamento/consulta.xhtml?faces-redirect=true";
				} else {
					return "";
				}

			} else {
				JsfUtil.addErrorMessage("O campo atividade é de preenchimento obrigatório.");

				return "";
			}

		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
			return "";
		}
	}

	public void salvarManejoPotencial() {
		try {
			tipologiaGrupo.setIdeTipologia(tipologia);

			if (potencialPoluicaoSelecionado.getIdePotencialPoluicao() == null) {
				tipologiaGrupo.setIdePotencialPoluicao(null);
			} else {

				if ((potencialPoluicaoSelecionado.getIdePotencialPoluicao() == 0) || (potencialPoluicaoSimNao)) {
					tipologiaGrupo.setIdePotencialPoluicao(null);
				} else {
					tipologiaGrupo.setIdePotencialPoluicao(potencialPoluicaoSelecionado);
				}
			}
			tipologiaGrupo.setDtcCriacao(new Date());
			tipologiaGrupo.setIndExcluido(false);
			tipologiaGrupoService.salvarTipologiaGrupo(tipologiaGrupo);

			this.isDesabilitaComponentesPicklistUnMedida();

			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");

		} catch (Exception exception) {

			if (exception.getCause() instanceof ConstraintViolationException) {
				tipologia.getCnaeCollection().remove(cnaeSubclasse);
				JsfUtil.addWarnMessage("Já existe um registro para esse Forma Manejo cadastrado.");
			} else {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
				JsfUtil.addErrorMessage(exception.getMessage());
			}

		}
	}

	public void salvarPorteTipologia() {

		try {
			Porte porte;
			PorteTipologia pt;
			Collection<PorteTipologia> listPorteTipologia = new ArrayList<PorteTipologia>();


			if (!Util.isNullOuVazio(pequenoMaximo)) {
				pt = new PorteTipologia();
				pt.setIdeTipologiaGrupo(tipologiaGrupo);
				porte = new Porte(2);

				setValorPorteTipologia(pt, porte, listPorteTipologia, pequenoMinimo,pequenoMaximo);
			} else {
				porte = new Porte(2);
				porteTipologiaService.excluirPorteTipologia(tipologiaGrupo, porte);
			}

			if (!Util.isNullOuVazio(medioMinimo) && !Util.isNullOuVazio(medioMaximo)) {
				pt = new PorteTipologia();
				pt.setIdeTipologiaGrupo(tipologiaGrupo);
				porte = new Porte(3);
				setValorPorteTipologia(pt, porte, listPorteTipologia,medioMinimo,medioMaximo);
			} else {
				porte = new Porte(3);
				porteTipologiaService.excluirPorteTipologia(tipologiaGrupo, porte);
			}

			if (!Util.isNullOuVazio(grandeMinimo) && !Util.isNullOuVazio(grandeMaximo)) {
				pt = new PorteTipologia();
				pt.setIdeTipologiaGrupo(tipologiaGrupo);
				porte = new Porte(4);
				
				setValorPorteTipologia(pt, porte, listPorteTipologia,grandeMinimo,grandeMaximo);
			} else {
				porte = new Porte(4);
				porteTipologiaService.excluirPorteTipologia(tipologiaGrupo, porte);
			}

			if(BigDecimal.ZERO.intValue() == grandeMaximo.intValue()){
				grandeMaximo = null;
			}
			
			porteTipologiaService.salvarPorteTipologias(listPorteTipologia);

			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	// ///////////////////////////

	private void setValorPorteTipologia(PorteTipologia pt, Porte porte, Collection<PorteTipologia> listPorteTipologia, BigDecimal valorMinimo,
			BigDecimal valorMaximo) {
		if (!Util.isNull(valorMinimo)) {
			if (valorMinimo.compareTo(new BigDecimal(1)) < 0) {
				valorMinimo = null;
			}
		}

		if (!Util.isNull(valorMaximo)) {
			if (valorMaximo.compareTo(new BigDecimal(1)) < 0) {
				valorMaximo = null;
			}
		}

		pt.setIdePorte(porte);
		pt.setValMinimo(valorMinimo);
		pt.setValMaximo(valorMaximo);
		listPorteTipologia.add(pt);
	}

	public void salvarPorteCompetencia() {

		try {
			Porte porte;
			NivelCompetencia nivelCompetencia;

			PorteCompetencia pc = new PorteCompetencia();
			Collection<PorteCompetencia> listPorteCompetencia = new ArrayList<PorteCompetencia>();
			if (nivel1MicroMunicipal) {
				nivelCompetencia = new NivelCompetencia(1);
				pc = new PorteCompetencia();
				porte = new Porte(1);
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(1);
				porte = new Porte(1);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel1PequenoMunicipal) {
				nivelCompetencia = new NivelCompetencia(1);
				porte = new Porte(2);
				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(1);
				porte = new Porte(2);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel1MedioMunicipal) {
				nivelCompetencia = new NivelCompetencia(1);
				porte = new Porte(3);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(1);
				porte = new Porte(3);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel1GrandeMunicipal) {
				nivelCompetencia = new NivelCompetencia(1);
				porte = new Porte(4);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);

			} else {
				nivelCompetencia = new NivelCompetencia(1);
				porte = new Porte(4);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel1ExcepcionalMunicipal) {
				nivelCompetencia = new NivelCompetencia(1);
				porte = new Porte(5);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(1);
				porte = new Porte(5);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel2MicroMunicipal) {
				nivelCompetencia = new NivelCompetencia(2);
				porte = new Porte(1);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(2);
				porte = new Porte(1);

				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel2PequenoMunicipal) {
				nivelCompetencia = new NivelCompetencia(2);
				porte = new Porte(2);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(2);
				porte = new Porte(2);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel2MedioMunicipal) {
				nivelCompetencia = new NivelCompetencia(2);
				porte = new Porte(3);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(2);
				porte = new Porte(3);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel2GrandeMunicipal) {
				nivelCompetencia = new NivelCompetencia(2);
				porte = new Porte(4);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(2);
				porte = new Porte(4);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel2ExcepcionalMunicipal) {
				nivelCompetencia = new NivelCompetencia(2);
				porte = new Porte(5);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(2);
				porte = new Porte(5);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel3MicroMunicipal) {
				nivelCompetencia = new NivelCompetencia(3);
				porte = new Porte(1);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(3);
				porte = new Porte(1);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel3PequenoMunicipal) {
				nivelCompetencia = new NivelCompetencia(3);
				porte = new Porte(2);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(3);
				porte = new Porte(2);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel3MedioMunicipal) {
				nivelCompetencia = new NivelCompetencia(3);
				porte = new Porte(3);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);

			} else {
				nivelCompetencia = new NivelCompetencia(3);
				porte = new Porte(3);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel3GrandeMunicipal) {
				nivelCompetencia = new NivelCompetencia(3);
				porte = new Porte(4);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);

			} else {
				nivelCompetencia = new NivelCompetencia(3);
				porte = new Porte(4);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel3ExcepcionalMunicipal) {
				nivelCompetencia = new NivelCompetencia(3);
				porte = new Porte(5);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(3);
				porte = new Porte(5);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel4estadualMicro) {
				nivelCompetencia = new NivelCompetencia(4);
				porte = new Porte(1);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(4);
				porte = new Porte(1);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel4estadualPequeno) {
				nivelCompetencia = new NivelCompetencia(4);
				porte = new Porte(2);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(4);
				porte = new Porte(2);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel4estadualMedio) {
				nivelCompetencia = new NivelCompetencia(4);
				porte = new Porte(3);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(4);
				porte = new Porte(3);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel4estadualGrande) {
				nivelCompetencia = new NivelCompetencia(4);
				porte = new Porte(4);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(4);
				porte = new Porte(4);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			if (nivel4estadualExcepcional) {
				nivelCompetencia = new NivelCompetencia(4);
				porte = new Porte(5);

				pc = new PorteCompetencia();
				setPorteCompetencia(porte, nivelCompetencia, pc, listPorteCompetencia);
			} else {
				nivelCompetencia = new NivelCompetencia(4);
				porte = new Porte(5);
				porteCompetenciaService.excluirPorteCompetenciaNivel(tipologiaGrupo, nivelCompetencia, porte);
			}

			porteCompetenciaService.salvarPorteCompetencias(listPorteCompetencia);

			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void setPorteCompetencia(Porte porte, NivelCompetencia nivelCompetencia, PorteCompetencia pc, Collection<PorteCompetencia> listPorteCompetencia) {

		pc.setPorte(porte);
		pc.setTipologiaGrupo(tipologiaGrupo);
		pc.setNivelCompetencia(nivelCompetencia);

		PorteCompetenciaPK porteCompetenciaPK = new PorteCompetenciaPK();
		porteCompetenciaPK.setIdeNivelCompetencia(nivelCompetencia.getIdeNivelCompetencia());
		porteCompetenciaPK.setIdePorte(porte.getIdePorte());
		porteCompetenciaPK.setIdeTipologiaGrupo(tipologiaGrupo.getIdeTipologiaGrupo());

		pc.setPorteCompetenciaPK(porteCompetenciaPK);

		listPorteCompetencia.add(pc);
	}

	public void salvarUnidaMedidaTipologiaGrupo() {
		try {
			Collection<UnidadeMedidaTipologiaGrupo> listUnMedSalvar = new ArrayList<UnidadeMedidaTipologiaGrupo>();
			Collection<UnidadeMedidaTipologiaGrupo> listUnMedDel = new ArrayList<UnidadeMedidaTipologiaGrupo>();
			if (!unidMedidas.getTarget().isEmpty()) {

				for (UnidadeMedida listUnidadeMedida : unidMedidas.getTarget()) {
					listaUnMedidaTipologia = unidadeMedidaTipologiaGrupoService.listarUnidadeMedidaTipologiaGrupo(tipologiaGrupo,
							listUnidadeMedida.getIdeUnidadeMedida());
					if (listaUnMedidaTipologia == null) {
						listUnMedSalvar.add(new UnidadeMedidaTipologiaGrupo(tipologiaGrupo, listUnidadeMedida));
						excluirPicklist = false;
					} else {
						listUnMedDel.add(new UnidadeMedidaTipologiaGrupo(tipologiaGrupo, listUnidadeMedida));
						excluirPicklist = true;
					}
				}

				if (!listUnMedSalvar.isEmpty()) {
					unidadeMedidaTipologiaGrupoService.salvarUnidadeMedidaTipologiaGrupos(listUnMedSalvar);
				}

				if (!listUnMedDel.isEmpty() && excluirPicklist) {
					Collection<UnidadeMedidaTipologiaGrupo> lista = new ArrayList<UnidadeMedidaTipologiaGrupo>();
					lista = unidadeMedidaTipologiaGrupoService.filtrarUnidadeMedidaTipologiaGrupo(tipologiaGrupo, listUnMedDel);
					unidadeMedidaTipologiaGrupoService.excluirUnidadeMedidaTipologiaGrupo(lista);
				}
			} else {
				unidadeMedidaTipologiaGrupoService.excluirUnidadeMedidaTipologiaGrupo(tipologiaGrupo);
			}

			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");

		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void salvarAtoAmbiental() {

		try {
			Collection<TipologiaTipoAto> listTipologiaAtoAmbientalSalvar = new ArrayList<TipologiaTipoAto>();
			Collection<TipologiaTipoAto> listTipologiaAtoAmbientalDel = new ArrayList<TipologiaTipoAto>();
			if (!atoAmbiental.getTarget().isEmpty()) {
				for (AtoAmbiental listAtoAmbiental : atoAmbiental.getTarget()) {
					listaTipologiaAtoAmbiental = tipologiaTipoAtoService.listarTipologiaAtoAmbiental(tipologiaGrupo, listAtoAmbiental.getIdeAtoAmbiental());
					if (listaTipologiaAtoAmbiental == null) {
						listTipologiaAtoAmbientalSalvar.add(new TipologiaTipoAto(tipologiaGrupo, null, listAtoAmbiental));
						excluirPicklist = false;
					} else {
						listTipologiaAtoAmbientalDel.add(new TipologiaTipoAto(tipologiaGrupo, null, listAtoAmbiental));
						excluirPicklist = true;
					}
				}

				if (!listTipologiaAtoAmbientalSalvar.isEmpty()) {
					tipologiaTipoAtoService.salvarTipologiaTipoAtos(listTipologiaAtoAmbientalSalvar);
				}

				if (!listTipologiaAtoAmbientalDel.isEmpty() && excluirPicklist) {
					Collection<TipologiaTipoAto> lista = new ArrayList<TipologiaTipoAto>();
					lista = tipologiaTipoAtoService.filtrarTipologiaAtoAmbiental(tipologiaGrupo, listTipologiaAtoAmbientalDel);

					tipologiaTipoAtoService.excluirTipologiaAtoAmbiental(lista);
				}
			} else {
				tipologiaTipoAtoService.excluirTipologiaAtoAmbiental(tipologiaGrupo);
			}

			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");

		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}

	}

	public String finalizarFormaManejo() {

		if (isAtividade.equals("0")) {
			disableTabFormaManejo = Boolean.TRUE;

			/*
			 * showExpandirCadFormaManejo = Boolean.TRUE;
			 * disableFormCadFormaManejo = Boolean.TRUE; tipoAtoChange = "";
			 * isTipoAtoRendered = Boolean.FALSE;
			 */

		}

		if (isAtividade.equals("1")) {
			limparTelaFormamanejo();

			disableTabFormaManejo = Boolean.TRUE;// TRUE mostra o grid ; FALSE
													// esconde o grid
			carregarVwFormaManejo();
			showExpandirCadFormaManejo = Boolean.TRUE;// true espandi o
														// cadastro; false
														// esconde o gride

			disableFormCadFormaManejo = Boolean.TRUE;// true vai fechar o
														// cadastro

			// tipoAtoChange = "";
			isTipoAtoRendered = Boolean.FALSE;

		} else {
			showExpandirCadFormaManejo = Boolean.TRUE;// true espandi o
														// cadastro; false
														// esconde o gride
			disableFormCadFormaManejo = Boolean.TRUE;// true vai fechar o
														// cadastro
		}

		if (isAtividade.equals("2")) {
			return "/paginas/manter-atividadelicenciamento/consulta.xhtml?faces-redirect=true";
		}

		return "";
	}

	public void salvarTipoAtoReferencia() {

		try {
			vwTipologiaTipoAtoUnidadeMedidaService.salvarAtoReferencia(listaVwTipologiaTipoAtoUnidadeMedida);
			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");

		} catch (Exception exception) {

			if (exception.getCause() instanceof ConstraintViolationException) {
				tipologia.getCnaeCollection().remove(cnaeSubclasse);
				JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
			} else {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

	}

	public void salvarTipoAtoAmbiental_DTO() {

		try {
			List<TipoAto> listaTipoAto = new ArrayList<TipoAto>();
			List<AtoAmbiental> listaAtoambiental = new ArrayList<AtoAmbiental>();
			for (Object item : tipoAtoeAtoAmbientalDTO.getTarget()) {
				if (item instanceof TipoAto) {
					listaTipoAto.add(new TipoAto(((TipoAto) item).getIdeTipoAto()));
				} else {
					listaAtoambiental.add(new AtoAmbiental(((TipoAto) item).getIdeTipoAto()));
				}
			}

			// tipologiaTipoAtoService.salvarTipologiaTipoAtos(listaTipoAto);
			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void editarTipologiaCnae() {

		// recupera Subclasse
		this.cnaeSubclasse = cnaeSelecionado;
		// recupera Classe
		this.cnaeClasse = this.cnaeSubclasse.getIdeCnaePai();
		// recupera Grupo
		this.cnaeGrupo = this.cnaeClasse.getIdeCnaePai();
		// recupera Divisao
		this.cnaeDivisao = this.cnaeGrupo.getIdeCnaePai();
		// recupera Seção
		this.cnaeSecao = this.cnaeDivisao.getIdeCnaePai();

		buscarDivisaoPorSecao();
		buscarGrupoPorDivisao();
		buscarClassePorGrupo();
		buscarSubclassePorClasse();

		RequestContext.getCurrentInstance().addPartialUpdateTarget(":tabAbas:formAbaCnae:paneldiaglogCnae");
		RequestContext.getCurrentInstance().execute("dialogCnae.show()");

	}

	public void excluirFormaManejo() {
		try {
			formaManejoServiceFacade.deletarFormaManejo(vwFormaManejoExcluir);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void excluirTipologia() {
		try {
			tipologia = tipologiaPaiService.carregarTipologia(vwTipologiaCnaeSelecionado.getIdeTipologia());
			tipologia.setIndExcluido(true);
			tipologiaPaiService.salvarTipologia(tipologia);

			carregarVwTipologiaCnae();

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void excluirCnae() {
		try {
			cnaeServiceFacede.excluirTipologiaCnae(cnaeSelecionado, tipologia);
			atualizarTipologia();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void atualizarTipologia() {
		try {
			tipologia = tipologiaPaiService.carregarTipologiaPorId(tipologia.getIdeTipologia());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void carregarListaSecao() {
		try {
			this.listaSecao = new ArrayList<SelectItem>();
			this.listaSecao.add(new SelectItem(0, "Selecione a Seção"));
			List<Cnae> listaCnae = (List<Cnae>) cnaeService.listarCnaeSecao();
			for (Cnae cnae : listaCnae) {
				this.listaSecao.add(new SelectItem(cnae.getIdeCnae(), cnae.getCodCnae() + " - " + cnae.getDesCnae()));
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void buscarDivisaoPorSecao() {
		try {
			listaDivisao = new ArrayList<SelectItem>();
			this.listaDivisao.add(new SelectItem(0, "Selecione Divisão"));

			cnaeCodPai = cnaeService.carregarCnae(cnaeSecao.getIdeCnae());
			for (Cnae cnae : cnaeService.listarCnaePorPai(cnaeSecao)) {
				this.listaDivisao.add(new SelectItem(cnae.getIdeCnae(), cnaeCodPai.getCodCnae() + cnae.getCodCnae() + " - " + cnae.getDesCnae()));
			}
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void buscarGrupoPorDivisao() {
		try {
			listaGrupo = new ArrayList<SelectItem>();
			this.listaGrupo.add(new SelectItem(0, "Selecione Grupo"));

			for (Cnae cnae : cnaeService.listarCnaePorPai(cnaeDivisao)) {
				this.listaGrupo.add(new SelectItem(cnae.getIdeCnae(), cnaeCodPai.getCodCnae() + cnae.getCodCnae() + " - " + cnae.getDesCnae()));
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void buscarClassePorGrupo() {
		try {
			listaClasse = new ArrayList<SelectItem>();
			this.listaClasse.add(new SelectItem(0, "Selecione Classe"));
			for (Cnae cnae : cnaeService.listarCnaePorPai(cnaeGrupo)) {
				this.listaClasse.add(new SelectItem(cnae.getIdeCnae(), cnaeCodPai.getCodCnae() + cnae.getCodCnae() + " - " + cnae.getDesCnae()));
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void buscarSubclassePorClasse() {
		try {
			this.listaSubclasse = new ArrayList<SelectItem>();
			this.listaSubclasse.add(new SelectItem(0, "Selecione SubClasse"));
			for (Cnae cnae : cnaeService.listarCnaePorPai(cnaeClasse)) {
				this.listaSubclasse.add(new SelectItem(cnae.getIdeCnae(), cnaeCodPai.getCodCnae() + cnae.getCodCnae() + " - " + cnae.getDesCnae()));
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void limparTelaFormamanejo() {
		try {

			tipologiaGrupo = new TipologiaGrupo();
			formaManejoSelecionado = new FormaManejo();
			potencialPoluicaoSelecionado = new PotencialPoluicao();

			unidMedidas = Util.castListToDualListModel(getColUnidadeMedida(), new ArrayList<UnidadeMedida>());
			area = Util.castListToDualListModel(getColArea(), new ArrayList<Area>());
			tipoAto = Util.castListToDualListModel(getColTipoAto(), new ArrayList<TipoAto>());
			atoAmbiental = Util.castListToDualListModel(getColAtoAmbiental(), new ArrayList<AtoAmbiental>());

			nivel1MicroMunicipal = Boolean.FALSE;
			nivel1PequenoMunicipal = Boolean.FALSE;
			nivel1MedioMunicipal = Boolean.FALSE;
			nivel1GrandeMunicipal = Boolean.FALSE;
			nivel1ExcepcionalMunicipal = Boolean.FALSE;

			nivel2MicroMunicipal = Boolean.FALSE;
			nivel2PequenoMunicipal = Boolean.FALSE;
			nivel2MedioMunicipal = Boolean.FALSE;
			nivel2GrandeMunicipal = Boolean.FALSE;
			nivel2ExcepcionalMunicipal = Boolean.FALSE;

			nivel3MicroMunicipal = Boolean.FALSE;
			nivel3PequenoMunicipal = Boolean.FALSE;
			nivel3MedioMunicipal = Boolean.FALSE;
			nivel3GrandeMunicipal = Boolean.FALSE;
			nivel3ExcepcionalMunicipal = Boolean.FALSE;

			nivel4estadualMicro = Boolean.FALSE;
			nivel4estadualPequeno = Boolean.FALSE;
			nivel4estadualMedio = Boolean.FALSE;
			nivel4estadualGrande = Boolean.FALSE;
			nivel4estadualExcepcional = Boolean.FALSE;

			microMinimo = null;
			microMaximo = null;
			pequenoMinimo = null;
			pequenoMaximo = null;
			medioMinimo = null;
			medioMaximo = null;
			grandeMinimo = null;
			grandeMaximo = null;
			excepcionalMinimo = null;
			excepcionalMaximo = null;

			carregarlistaVwTipologiaTipoAtoUnidadeMedida();

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}

	}

	public void limparObjetos() {
		try {
			cnaeSecao = new Cnae();
			cnaeDivisao = new Cnae();
			cnaeGrupo = new Cnae();
			cnaeClasse = new Cnae();
			cnaeSubclasse = new Cnae();
			carregarListaSecao();
			listaDivisao = new ArrayList<SelectItem>();
			listaGrupo = new ArrayList<SelectItem>();
			listaClasse = new ArrayList<SelectItem>();
			listaSubclasse = new ArrayList<SelectItem>();

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}

	}

	public void isDesabilitaComponentesTipologiaAbaIdentificacao() {

		if (!Util.isNullOuVazio(getTipologia()) && !Util.isNullOuVazio(getTipologia().getIdeTipologia())) {
			habilitaCnae = false;
		} else {
			habilitaCnae = true;
		}
	}

	/*
	 * public void habilitaAbaManejo() {
	 * 
	 * disableTabFormaManejo = Boolean.TRUE;
	 * 
	 * habilitaManejo = false; this.indexAba = 2;
	 * 
	 * habilitaTabelaFormaManejo(); }
	 */

	public void diferenteFormasManejo(String atividade) {

		try {

			if (atividade.equals("1")) {
				TipologiaGrupo ListaTipologiaGrupo;
				ListaTipologiaGrupo = tipologiaGrupoService.carregarTipologiaGrupoTipologia(tipologia);
				if (ListaTipologiaGrupo != null) {
					disableTabFormaManejo = Boolean.TRUE;
				} else {
					disableTabFormaManejo = Boolean.FALSE;
					// disableDisableFormCadFormaManejo = Boolean.FALSE;
				}
				habilitaManejo = false;
				this.indexAba = 2;

			}

			if (atividade.equals("2")) {
				disableTabFormaManejo = Boolean.FALSE;
				showExpandirCadFormaManejo = Boolean.FALSE;
				disableFormCadFormaManejo = Boolean.FALSE;

				disableIncluirCadFormaManejo = Boolean.FALSE;

				this.indexAba = 2;
				habilitaManejo = false;
			}

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void habilitaAbaManejo(ValueChangeEvent event) {

		isAtividade = (String) event.getNewValue();
		try {

			diferenteFormasManejo(isAtividade);

			// /com base no ticket #474 solicitou que nao fizesse mas essa
			// validação
			/*
			 * if (!tipologia.getCnaeCollection().isEmpty()){
			 * diferenteFormasManejo(isAtividade); }else{
			 * JsfUtil.addErrorMessage
			 * ("É necessário incluir uma atividade CNAE."); }
			 */

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}

	}

	public void validarUnMedSimNao() {
		if (unidMedidaSimNao == true) {
			habilitaPicklistUndMedida = false;
		} else {
			habilitaPicklistUndMedida = true;
		}
	}

	public void validarPorte() {
		habilitaPorte = true;
	}

	public void isDesabilitaComponentesPicklistUnMedida() {
		habilitaPicklistUndMedida = false;
		habilitaCheckboxUndMedida = false;
	}

	public void save(ActionEvent actionEvent) {
		// Persist user
		// FacesMessage msg = new FacesMessage("Successful", "Welcome :" +
		// pessoaFisica.getNomPessoa());
		// FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void temFilhosChanged(ValueChangeEvent event) {
		String value = "";
		if (!Util.isNull(event.getNewValue())) {
			value = event.getNewValue().toString();
		}

		if (value.equals("1")) {
			tipologia.setIndPossuiFilhos(false);
			tipologiaTemFilhos = "1";
			// istipologiaTemFilhos = Boolean.TRUE;
		} else if (value.equals("2")) {
			// istipologiaTemFilhos = Boolean.FALSE;
			tipologiaTemFilhos = "2";
			tipologia.setIndPossuiFilhos(true);
			redireatividade = Boolean.TRUE;
		}

	}

	public void tipoAtoChanged(ValueChangeEvent event) {
		String value = event.getNewValue().toString();
		if (value.equals("1")) {
			isTipoAtoRendered = Boolean.TRUE;
		} else if (value.equals("2")) {
			isTipoAtoRendered = Boolean.FALSE;
		}
		carregarlistaVwTipologiaTipoAtoUnidadeMedida();
	}

	// Gets e Sets
	public PotencialPoluicao getPotencialPoluicaoSelecionado() {
		return potencialPoluicaoSelecionado;
	}

	public void setPotencialPoluicaoSelecionado(PotencialPoluicao potencialPoluicaoSelecionado) {
		this.potencialPoluicaoSelecionado = potencialPoluicaoSelecionado;
	}

	public NivelTipologia getNivelTipologiaSelecionado() {
		return nivelTipologiaSelecionado;
	}

	public void setNivelTipologiaSelecionado(NivelTipologia nivelTipologiaSelecionado) {
		this.nivelTipologiaSelecionado = nivelTipologiaSelecionado;
	}

	public VwTipologiaCnae getVwTipologiaCnaeSelecionado() {
		return vwTipologiaCnaeSelecionado;
	}

	public void setVwTipologiaCnaeSelecionado(VwTipologiaCnae vwTipologiaCnaeSelecionado) {
		this.vwTipologiaCnaeSelecionado = vwTipologiaCnaeSelecionado;
	}

	public Cnae getCnaeCodPai() {
		return cnaeCodPai;
	}

	public void setCnaeCodPai(Cnae cnaeCodPai) {
		this.cnaeCodPai = cnaeCodPai;
	}

	public Boolean getNivel4estadualMicro() {
		return nivel4estadualMicro;
	}

	public void setNivel4estadualMicro(Boolean nivel4estadualMicro) {
		this.nivel4estadualMicro = nivel4estadualMicro;
	}

	public Boolean getNivel4estadualPequeno() {
		return nivel4estadualPequeno;
	}

	public void setNivel4estadualPequeno(Boolean nivel4estadualPequeno) {
		this.nivel4estadualPequeno = nivel4estadualPequeno;
	}

	public Boolean getNivel4estadualMedio() {
		return nivel4estadualMedio;
	}

	public void setNivel4estadualMedio(Boolean nivel4estadualMedio) {
		this.nivel4estadualMedio = nivel4estadualMedio;
	}

	public Boolean getNivel4estadualGrande() {
		return nivel4estadualGrande;
	}

	public void setNivel4estadualGrande(Boolean nivel4estadualGrande) {
		this.nivel4estadualGrande = nivel4estadualGrande;
	}

	public Boolean getNivel4estadualExcepcional() {
		return nivel4estadualExcepcional;
	}

	public void setNivel4estadualExcepcional(Boolean nivel4estadualExcepcional) {
		this.nivel4estadualExcepcional = nivel4estadualExcepcional;
	}

	public Boolean getNivel1MicroMunicipal() {
		return nivel1MicroMunicipal;
	}

	public void setNivel1MicroMunicipal(Boolean nivel1MicroMunicipal) {
		this.nivel1MicroMunicipal = nivel1MicroMunicipal;
	}

	public Boolean getNivel1PequenoMunicipal() {
		return nivel1PequenoMunicipal;
	}

	public void setNivel1PequenoMunicipal(Boolean nivel1PequenoMunicipal) {
		this.nivel1PequenoMunicipal = nivel1PequenoMunicipal;
	}

	public Boolean getNivel1MedioMunicipal() {
		return nivel1MedioMunicipal;
	}

	public void setNivel1MedioMunicipal(Boolean nivel1MedioMunicipal) {
		this.nivel1MedioMunicipal = nivel1MedioMunicipal;
	}

	public Boolean getNivel1GrandeMunicipal() {
		return nivel1GrandeMunicipal;
	}

	public void setNivel1GrandeMunicipal(Boolean nivel1GrandeMunicipal) {
		this.nivel1GrandeMunicipal = nivel1GrandeMunicipal;
	}

	public Boolean getNivel1ExcepcionalMunicipal() {
		return nivel1ExcepcionalMunicipal;
	}

	public void setNivel1ExcepcionalMunicipal(Boolean nivel1ExcepcionalMunicipal) {
		this.nivel1ExcepcionalMunicipal = nivel1ExcepcionalMunicipal;
	}

	public Boolean getNivel2MicroMunicipal() {
		return nivel2MicroMunicipal;
	}

	public void setNivel2MicroMunicipal(Boolean nivel2MicroMunicipal) {
		this.nivel2MicroMunicipal = nivel2MicroMunicipal;
	}

	public Boolean getNivel2PequenoMunicipal() {
		return nivel2PequenoMunicipal;
	}

	public void setNivel2PequenoMunicipal(Boolean nivel2PequenoMunicipal) {
		this.nivel2PequenoMunicipal = nivel2PequenoMunicipal;
	}

	public Boolean getNivel2MedioMunicipal() {
		return nivel2MedioMunicipal;
	}

	public void setNivel2MedioMunicipal(Boolean nivel2MedioMunicipal) {
		this.nivel2MedioMunicipal = nivel2MedioMunicipal;
	}

	public Boolean getNivel2GrandeMunicipal() {
		return nivel2GrandeMunicipal;
	}

	public void setNivel2GrandeMunicipal(Boolean nivel2GrandeMunicipal) {
		this.nivel2GrandeMunicipal = nivel2GrandeMunicipal;
	}

	public Boolean getNivel2ExcepcionalMunicipal() {
		return nivel2ExcepcionalMunicipal;
	}

	public void setNivel2ExcepcionalMunicipal(Boolean nivel2ExcepcionalMunicipal) {
		this.nivel2ExcepcionalMunicipal = nivel2ExcepcionalMunicipal;
	}

	public Boolean getNivel3MicroMunicipal() {
		return nivel3MicroMunicipal;
	}

	public void setNivel3MicroMunicipal(Boolean nivel3MicroMunicipal) {
		this.nivel3MicroMunicipal = nivel3MicroMunicipal;
	}

	public Boolean getNivel3PequenoMunicipal() {
		return nivel3PequenoMunicipal;
	}

	public void setNivel3PequenoMunicipal(Boolean nivel3PequenoMunicipal) {
		this.nivel3PequenoMunicipal = nivel3PequenoMunicipal;
	}

	public Boolean getNivel3MedioMunicipal() {
		return nivel3MedioMunicipal;
	}

	public void setNivel3MedioMunicipal(Boolean nivel3MedioMunicipal) {
		this.nivel3MedioMunicipal = nivel3MedioMunicipal;
	}

	public Boolean getNivel3GrandeMunicipal() {
		return nivel3GrandeMunicipal;
	}

	public void setNivel3GrandeMunicipal(Boolean nivel3GrandeMunicipal) {
		this.nivel3GrandeMunicipal = nivel3GrandeMunicipal;
	}

	public Boolean getNivel3ExcepcionalMunicipal() {
		return nivel3ExcepcionalMunicipal;
	}

	public void setNivel3ExcepcionalMunicipal(Boolean nivel3ExcepcionalMunicipal) {
		this.nivel3ExcepcionalMunicipal = nivel3ExcepcionalMunicipal;
	}


	public BigDecimal getMicroMinimo() {
		return microMinimo;
	}

	public void setMicroMinimo(BigDecimal microMinimo) {
		this.microMinimo = microMinimo;
	}

	public BigDecimal getMicroMaximo() {
		return microMaximo;
	}

	public void setMicroMaximo(BigDecimal microMaximo) {
		this.microMaximo = microMaximo;
	}

	public BigDecimal getPequenoMinimo() {
		return pequenoMinimo;
	}

	public void setPequenoMinimo(BigDecimal pequenoMinimo) {
		this.pequenoMinimo = pequenoMinimo;
	}

	public BigDecimal getPequenoMaximo() {
		return pequenoMaximo;
	}

	public void setPequenoMaximo(BigDecimal pequenoMaximo) {
		this.pequenoMaximo = pequenoMaximo;
	}

	public BigDecimal getMedioMinimo() {
		return medioMinimo;
	}

	public void setMedioMinimo(BigDecimal medioMinimo) {
		this.medioMinimo = medioMinimo;
	}

	public BigDecimal getMedioMaximo() {
		return medioMaximo;
	}

	public void setMedioMaximo(BigDecimal medioMaximo) {
		this.medioMaximo = medioMaximo;
	}

	public BigDecimal getGrandeMinimo() {
		return grandeMinimo;
	}

	public void setGrandeMinimo(BigDecimal grandeMinimo) {
		this.grandeMinimo = grandeMinimo;
	}

	public BigDecimal getGrandeMaximo() {
		return grandeMaximo;
	}

	public void setGrandeMaximo(BigDecimal grandeMaximo) {
		this.grandeMaximo = grandeMaximo;
	}

	public BigDecimal getExcepcionalMinimo() {
		return excepcionalMinimo;
	}

	public void setExcepcionalMinimo(BigDecimal excepcionalMinimo) {
		this.excepcionalMinimo = excepcionalMinimo;
	}

	public BigDecimal getExcepcionalMaximo() {
		return excepcionalMaximo;
	}

	public void setExcepcionalMaximo(BigDecimal excepcionalMaximo) {
		this.excepcionalMaximo = excepcionalMaximo;
	}

	public Collection<PorteTipologia> getListaPorteForma() {
		return listaPorteForma;
	}

	public void setListaPorteForma(Collection<PorteTipologia> listaPorteForma) {
		this.listaPorteForma = listaPorteForma;
	}

	public Collection<PorteCompetencia> getListaPorteCompetencia() {
		return listaPorteCompetencia;
	}

	public void setListaPorteCompetencia(Collection<PorteCompetencia> listaPorteCompetencia) {
		this.listaPorteCompetencia = listaPorteCompetencia;
	}

	public Collection<ParametroReferencia> getListarParametroReferenciaForma() {
		return listarParametroReferenciaForma;
	}

	public void setListarParametroReferenciaForma(Collection<ParametroReferencia> listarParametroReferenciaForma) {
		this.listarParametroReferenciaForma = listarParametroReferenciaForma;
	}

	public UnidadeMedidaTipologiaGrupo getListaUnMedidaTipologia() {
		return listaUnMedidaTipologia;
	}

	public void setListaUnMedidaTipologia(UnidadeMedidaTipologiaGrupo listaUnMedidaTipologia) {
		this.listaUnMedidaTipologia = listaUnMedidaTipologia;
	}

	public TipologiaGrupoArea getListaAreaTipologia() {
		return listaAreaTipologia;
	}

	public void setListaAreaTipologia(TipologiaGrupoArea listaAreaTipologia) {
		this.listaAreaTipologia = listaAreaTipologia;
	}

	public TipologiaTipoAto getListaTipologiaTipoAto() {
		return listaTipologiaTipoAto;
	}

	public void setListaTipologiaTipoAto(TipologiaTipoAto listaTipologiaTipoAto) {
		this.listaTipologiaTipoAto = listaTipologiaTipoAto;
	}

	public TipologiaTipoAto getListaTipologiaAtoAmbiental() {
		return listaTipologiaAtoAmbiental;
	}

	public void setListaTipologiaAtoAmbiental(TipologiaTipoAto listaTipologiaAtoAmbiental) {
		this.listaTipologiaAtoAmbiental = listaTipologiaAtoAmbiental;
	}

	public PorteCompetencia getListPorteCompentencia() {
		return listPorteCompentencia;
	}

	public void setListPorteCompentencia(PorteCompetencia listPorteCompentencia) {
		this.listPorteCompentencia = listPorteCompentencia;
	}

	public DualListModel<Area> getArea() throws Exception {
		if (Util.isNullOuVazio(area))
			area = Util.castListToDualListModel(getColArea(), new ArrayList<Area>());
		return area;

	}

	public void setArea(DualListModel<Area> area) {
		this.area = area;
	}

	public boolean isHabilitaPorte() {
		return habilitaPorte;
	}

	public List<Porte> getListaPorte() {
		return listaPorte;
	}

	public void setListaPorte(List<Porte> listaPorte) {

		this.listaPorte = listaPorte;
	}

	public void setHabilitaPorte(boolean habilitaPorte) {
		this.habilitaPorte = habilitaPorte;
	}

	public boolean isUnidMedidaSimNao() {
		return unidMedidaSimNao;
	}

	public boolean isPotencialPoluicaoSimNao() {
		return potencialPoluicaoSimNao;
	}

	public void setPotencialPoluicaoSimNao(boolean potencialPoluicaoSimNao) {
		this.potencialPoluicaoSimNao = potencialPoluicaoSimNao;
	}

	public void setUnidMedidaSimNao(boolean unidMedidaSimNao) {
		this.unidMedidaSimNao = unidMedidaSimNao;
	}

	public String getTipoAtoChange() {
		return tipoAtoChange;
	}

	public void setTipoAtoChange(String tipoAtoChange) {
		this.tipoAtoChange = tipoAtoChange;
	}

	public String getTipologiaTemFilhos() {
		return tipologiaTemFilhos;
	}

	public void setTipologiaTemFilhos(String tipologiaTemFilhos) {
		this.tipologiaTemFilhos = tipologiaTemFilhos;
	}

	public Boolean getIsTipoAtoRendered() {
		carregarlistaVwTipologiaTipoAtoUnidadeMedida();
		return isTipoAtoRendered;
	}

	public void setIsTipoAtoRendered(Boolean isTipoAtoRendered) {
		this.isTipoAtoRendered = isTipoAtoRendered;
	}

	public Boolean getViewMode() {
		return viewMode;
	}

	public void setViewMode(Boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isViewCnae() {
		return viewCnae;
	}

	public void setViewCnae(boolean viewCnae) {
		this.viewCnae = viewCnae;
	}

	public boolean isDisbleTipologiaPai() {
		return disbleTipologiaPai;
	}

	public void setDisbleTipologiaPai(boolean disbleTipologiaPai) {
		this.disbleTipologiaPai = disbleTipologiaPai;
	}

	public boolean isDisbleEUmAtividade() {
		return disbleEUmAtividade;
	}

	public void setDisbleEUmAtividade(boolean disbleEUmAtividade) {
		this.disbleEUmAtividade = disbleEUmAtividade;
	}

	public boolean isDisbleBtTipologia() {
		return disbleBtTipologia;
	}

	public void setDisbleBtTipologia(boolean disbleBtTipologia) {
		this.disbleBtTipologia = disbleBtTipologia;
	}

	public boolean isDisblePossuiFormas() {
		return disblePossuiFormas;
	}

	public void setDisblePossuiFormas(boolean disblePossuiFormas) {
		this.disblePossuiFormas = disblePossuiFormas;
	}

	public Boolean getDisableFormCadFormaManejo() {
		return disableFormCadFormaManejo;
	}

	public void setDisableFormCadFormaManejo(Boolean disableFormCadFormaManejo) {
		this.disableFormCadFormaManejo = disableFormCadFormaManejo;
	}

	public Boolean getDisableTabFormaManejo() {
		return disableTabFormaManejo;
	}

	public void setDisableTabFormaManejo(Boolean disableTabFormaManejo) {
		this.disableTabFormaManejo = disableTabFormaManejo;
	}

	public Boolean getShowExpandirCadFormaManejo() {
		return showExpandirCadFormaManejo;
	}

	public void setShowExpandirCadFormaManejo(Boolean showExpandirCadFormaManejo) {
		this.showExpandirCadFormaManejo = showExpandirCadFormaManejo;
	}

	public Boolean getDisableIncluirCadFormaManejo() {
		return disableIncluirCadFormaManejo;
	}

	public void setDisableIncluirCadFormaManejo(Boolean disableIncluirCadFormaManejo) {
		this.disableIncluirCadFormaManejo = disableIncluirCadFormaManejo;
	}

	public Boolean getDisableBtForm() {
		return disableBtForm;
	}

	public void setDisableBtForm(Boolean disableBtForm) {
		this.disableBtForm = disableBtForm;
	}

	public void setHabilitaManejo(boolean habilitaManejo) {
		this.habilitaManejo = habilitaManejo;
	}

	public FormaManejo getFormaManejoSelecionado() {
		return formaManejoSelecionado;
	}

	public VwFormaManejo getVwFormaManejoSelecionado() {
		return vwFormaManejoSelecionado;
	}

	public void setVwFormaManejoSelecionado(VwFormaManejo vwFormaManejoSelecionado) {
		this.vwFormaManejoSelecionado = vwFormaManejoSelecionado;
	}

	public String getIsAtividade() {
		return isAtividade;
	}

	public void setIsAtividade(String isAtividade) {
		this.isAtividade = isAtividade;
	}

	public void setFormaManejoSelecionado(FormaManejo formaManejoSelecionado) {
		this.formaManejoSelecionado = formaManejoSelecionado;
	}

	public boolean isHabilitaPicklistUndMedida() {
		return habilitaPicklistUndMedida;
	}

	public void setHabilitaPicklistUndMedida(boolean habilitaPicklistUndMedida) {
		this.habilitaPicklistUndMedida = habilitaPicklistUndMedida;
	}

	public boolean isHabilitaCheckboxUndMedida() {
		return habilitaCheckboxUndMedida;
	}

	public void setHabilitaCheckboxUndMedida(boolean habilitaCheckboxUndMedida) {
		this.habilitaCheckboxUndMedida = habilitaCheckboxUndMedida;
	}

	public boolean isHabilitaManejo() {
		return habilitaManejo;
	}

	/*
	 * public void carregarListaTipologiaCnae() { try {
	 * if(!Util.isNullOuVazio(this.tipologia) &&
	 * !Util.isNullOuVazio(this.tipologia.getIdeTipologia())){ this.cnaeData =
	 * new ListDataModel<PessoaJuridicaCnae>((List<PessoaJuridicaCnae>)
	 * pessoaJuridicaCnaeService
	 * .buscaPessoaJuridicaCnaePorPessoaJuridica(this.pessoaJuridica)); } }
	 * catch (Exception e) { Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION); }
	 * 
	 * }
	 */

	public TipologiaGrupo getTipologiaGrupo() {
		return tipologiaGrupo;
	}

	public void setTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		this.tipologiaGrupo = tipologiaGrupo;
	}

	// ----------------------metodo para popular os picklists
	public Collection<UnidadeMedida> getColUnidadeMedida() throws Exception {
		return unidadeMedidaService.filtrarListaUnidadeMedida(new UnidadeMedida());
	}

	public boolean isHabilitaCnae() {
		return habilitaCnae;
	}

	public DualListModel<UnidadeMedida> getUnidMedidas() throws Exception {

		if (Util.isNullOuVazio(unidMedidas))
			unidMedidas = Util.castListToDualListModel(getColUnidadeMedida(), new ArrayList<UnidadeMedida>());
		return unidMedidas;

	}

	public void setUnidMedidas(DualListModel<UnidadeMedida> unidMedidas) {
		this.unidMedidas = unidMedidas;
	}

	public void setHabilitaCnae(boolean habilitaCnae) {
		this.habilitaCnae = habilitaCnae;
	}

	public Collection<Area> getColArea() throws Exception {
		return areaService.filtrarListaAreas(new Area());
	}

	public void setTipoAto(DualListModel<TipoAto> tipoAto) {
		this.tipoAto = tipoAto;
	}

	public Collection<TipoAto> getColTipoAto() throws Exception {
		return tipoAtoService.filtrarListaTipoAto(new TipoAto());
	}

	public DualListModel<TipoAto> getTipoAto() throws Exception {
		if (Util.isNullOuVazio(tipoAto))
			tipoAto = Util.castListToDualListModel(getColTipoAto(), new ArrayList<TipoAto>());
		return tipoAto;
	}

	public Collection<AtoAmbiental> getColAtoAmbiental() throws Exception {
		return atoAmbientalService.filtrarListaAtoAmbiental(new AtoAmbiental());
	}

	public DualListModel<AtoAmbiental> getAtoAmbiental() throws Exception {
		if (Util.isNullOuVazio(atoAmbiental))
			atoAmbiental = Util.castListToDualListModel(getColAtoAmbiental(), new ArrayList<AtoAmbiental>());
		return atoAmbiental;
	}

	public void setAtoAmbiental(DualListModel<AtoAmbiental> atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	public Collection<UnidadeMedida> getListaUnMedidaSource() {
		return listaUnMedidaSource;
	}

	public void setListaUnMedidaSource(Collection<UnidadeMedida> listaUnMedidaSource) {
		this.listaUnMedidaSource = listaUnMedidaSource;
	}

	public Collection<UnidadeMedida> getListaUnMedidaTarget() {
		return listaUnMedidaTarget;
	}

	public void setListaUnMedidaTarget(Collection<UnidadeMedida> listaUnMedidaTarget) {
		this.listaUnMedidaTarget = listaUnMedidaTarget;
	}

	public Collection<Area> getListaAreaTarget() {
		return listaAreaTarget;
	}

	public void setListaAreaTarget(Collection<Area> listaAreaTarget) {
		this.listaAreaTarget = listaAreaTarget;
	}

	public Collection<Area> getListaAreaSource() {
		return listaAreaSource;
	}

	public void setListaAreaSource(Collection<Area> listaAreaSource) {
		this.listaAreaSource = listaAreaSource;
	}

	public Collection<TipoAto> getListaTipoAtoTarget() {
		return listaTipoAtoTarget;
	}

	public void setListaTipoAtoTarget(Collection<TipoAto> listaTipoAtoTarget) {
		this.listaTipoAtoTarget = listaTipoAtoTarget;
	}

	public Collection<TipoAto> getListaTipoAtoSource() {
		return listaTipoAtoSource;
	}

	public void setListaTipoAtoSource(Collection<TipoAto> listaTipoAtoSource) {
		this.listaTipoAtoSource = listaTipoAtoSource;
	}

	public Collection<AtoAmbiental> getListaAtoAmbientalTarget() {
		return listaAtoAmbientalTarget;
	}

	public void setListaAtoAmbientalTarget(Collection<AtoAmbiental> listaAtoAmbientalTarget) {
		this.listaAtoAmbientalTarget = listaAtoAmbientalTarget;
	}

	public Collection<AtoAmbiental> getListaAtoAmbientalSource() {
		return listaAtoAmbientalSource;
	}

	public void setListaAtoAmbientalSource(Collection<AtoAmbiental> listaAtoAmbientalSource) {
		this.listaAtoAmbientalSource = listaAtoAmbientalSource;
	}

	public Collection<TipoAtoeAtoAmbientalDTO> getColTipoAtoeAtoAmbientalDTO() throws Exception {
		List<TipoAtoeAtoAmbientalDTO> listaDTO = new ArrayList<TipoAtoeAtoAmbientalDTO>();
		List<TipoAto> listaAto = (List<TipoAto>) tipoAtoService.filtrarListaTipoAto(new TipoAto());
		for (TipoAto tipoAto : listaAto) {
			TipoAtoeAtoAmbientalDTO dto = new TipoAtoeAtoAmbientalDTO();
			dto.setObject(tipoAto);
			listaDTO.add(dto);
		}
		List<AtoAmbiental> listaAmbiental = (List<AtoAmbiental>) atoAmbientalService.filtrarListaAtoAmbiental(new AtoAmbiental());
		for (AtoAmbiental atoAmbiental : listaAmbiental) {
			TipoAtoeAtoAmbientalDTO dto = new TipoAtoeAtoAmbientalDTO();
			dto.setObject(atoAmbiental);
			listaDTO.add(dto);
		}
		return listaDTO;
	}

	public DualListModel<TipoAtoeAtoAmbientalDTO> getTipoAtoeAtoAmbientalDTO() throws Exception {
		if (Util.isNullOuVazio(tipoAtoeAtoAmbientalDTO))
			tipoAtoeAtoAmbientalDTO = Util.castListToDualListModel(getColTipoAtoeAtoAmbientalDTO(), new ArrayList<TipoAtoeAtoAmbientalDTO>());
		return tipoAtoeAtoAmbientalDTO;

	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public String getCodTipologia() {
		return codTipologia;
	}

	public void setCodTipologia(String codTipologia) {
		this.codTipologia = codTipologia;
	}

	public String getDesTipologia() {
		return desTipologia;
	}

	public void setDesTipologia(String desTipologia) {
		this.desTipologia = desTipologia;
	}

	public String getIdeNivelTipologia() {
		return ideNivelTipologia;
	}

	public void setIdeNivelTipologia(String ideNivelTipologia) {
		this.ideNivelTipologia = ideNivelTipologia;
	}

	public CnaeService getCnaeService() {
		return cnaeService;
	}

	public void setCnaeService(CnaeService cnaeService) {
		this.cnaeService = cnaeService;
	}

	public NivelTipologiaService getNivelTipologiaService() {
		return nivelTipologiaService;
	}

	public void setNivelTipologiaService(NivelTipologiaService nivelTipologiaService) {
		this.nivelTipologiaService = nivelTipologiaService;
	}

	public List<SelectItem> getNivelTipologiaItens() {
		return nivelTipologiaItens;
	}

	public void setNivelTipologiaItens(List<SelectItem> nivelTipologiaItens) {
		this.nivelTipologiaItens = nivelTipologiaItens;
	}

	public List<SelectItem> getTipologiaItens() {
		return tipologiaItens;
	}

	public void setTipologiaItens(List<SelectItem> tipologiaItens) {
		this.tipologiaItens = tipologiaItens;
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

	public List<SelectItem> getListaClasse() {
		return listaClasse;
	}

	public void setListaClasse(List<SelectItem> listaClasse) {
		this.listaClasse = listaClasse;
	}

	public Cnae getCnaeSubclasse() {
		return cnaeSubclasse;
	}

	public void setCnaeSubclasse(Cnae cnaeSubclasse) {
		this.cnaeSubclasse = cnaeSubclasse;
	}

	public List<SelectItem> getListaSubclasse() {
		return listaSubclasse;
	}

	public void setListaSubclasse(List<SelectItem> listaSubclasse) {
		this.listaSubclasse = listaSubclasse;
	}

	public Cnae getCnaeSelecionado() {
		return cnaeSelecionado;
	}

	public void setCnaeSelecionado(Cnae cnaeSelecionado) {
		this.cnaeSelecionado = cnaeSelecionado;
	}

	public DataModel<Tipologia> getCnaeData() {
		return cnaeData;
	}

	public DataModel<VwFormaManejo> getModelVwFormaManejo() {
		return modelVwFormaManejo;
	}

	public void setModelVwFormaManejo(DataModel<VwFormaManejo> modelVwFormaManejo) {
		this.modelVwFormaManejo = modelVwFormaManejo;
	}

	public DataModel<VwTipologiaCnae> getModelVwTipologiaCnae() {
		return modelVwTipologiaCnae;
	}

	public void setModelVwTipologiaCnae(DataModel<VwTipologiaCnae> modelVwTipologiaCnae) {
		this.modelVwTipologiaCnae = modelVwTipologiaCnae;
	}

	public void setCnaeData(DataModel<Tipologia> cnaeData) {
		this.cnaeData = cnaeData;
	}

	public TipologiaPaiService getTipologiaPaiService() {
		return tipologiaPaiService;
	}

	public void setTipologiaPaiService(TipologiaPaiService tipologiaPaiService) {
		this.tipologiaPaiService = tipologiaPaiService;
	}

	public Tipologia getTipologiaPaiSelecionado() {
		return tipologiaPaiSelecionado;
	}

	public void setTipologiaPaiSelecionado(Tipologia tipologiaPaiSelecionado) {
		this.tipologiaPaiSelecionado = tipologiaPaiSelecionado;
	}

	public Cnae getCnae() {
		return cnae;
	}

	public void setCnae(Cnae cnae) {
		this.cnae = cnae;
	}

	public List<SelectItem> getFormaManejoItens() {
		return formaManejoItens;
	}

	public void setFormaManejoItens(List<SelectItem> formaManejoItens) {
		this.formaManejoItens = formaManejoItens;
	}

	public List<SelectItem> getPotencialPoluicaoItens() {
		return potencialPoluicaoItens;
	}

	public void setPotencialPoluicaoItens(List<SelectItem> potencialPoluicaoItens) {
		this.potencialPoluicaoItens = potencialPoluicaoItens;
	}

	public Integer getIndexAba() {
		return indexAba;
	}

	public void setIndexAba(Integer indexAba) {
		this.indexAba = indexAba;
	}

	public List<VwTipologiaTipoAtoUnidadeMedida> getListaVwTipologiaTipoAtoUnidadeMedida() {
		return listaVwTipologiaTipoAtoUnidadeMedida;
	}

	public void setListaVwTipologiaTipoAtoUnidadeMedida(List<VwTipologiaTipoAtoUnidadeMedida> listaVwTipologiaTipoAtoUnidadeMedida) {
		this.listaVwTipologiaTipoAtoUnidadeMedida = listaVwTipologiaTipoAtoUnidadeMedida;
	}

	public VwFormaManejo getVwFormaManejoExcluir() {
		return vwFormaManejoExcluir;
	}

	public void setVwFormaManejoExcluir(VwFormaManejo vwFormaManejoExcluir) {
		this.vwFormaManejoExcluir = vwFormaManejoExcluir;
	}

	// //----------- METODOS CRIADO POR CADU--------------------//////
	public void novoNivelTipologia(ValueChangeEvent event) {
		Tipologia tipologia = (Tipologia) event.getNewValue();
		Integer ideNivelTipologia = tipologia.getIdeTipologia();

		try {
			tipologia = tipologiaPaiService.carregarTipologia(ideNivelTipologia);
			if (tipologia != null && tipologia.getIdeTipologia() != 0) { // <---
				// por favor criar um enum
				// para essa condiçao
				Integer IntNivelTipologia = tipologia.getIdeNivelTipologia().getIdeNivelTipologia() + 1;
				NivelTipologia nivelTipologia = nivelTipologiaService.carregarNivelTipologia(IntNivelTipologia);
				this.tipologia.setIdeNivelTipologia(nivelTipologia);
			}
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

}