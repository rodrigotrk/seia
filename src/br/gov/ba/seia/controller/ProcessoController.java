package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.OperacaoProcessoEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.facade.DeclaracaoIntervencaoAppFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorioDIAP;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorioIF;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RelatoriosRequerimentoService;
import br.gov.ba.seia.service.StatusFluxoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.service.VwConsultaProcessoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityService;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named("processoController")
@ViewScoped
public class ProcessoController extends FiltroConsultaProcesso {

	@EJB
	private VwConsultaProcessoService vwProcessoService;
	@EJB
	private StatusFluxoService statusFluxoService;
	@EJB
	private AreaService areaService;
	@EJB
	private FuncionarioService funcionarioService;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private MunicipioService municipioService;
	@EJB
	private RelatoriosRequerimentoService relatoriosRequerimentoServiceFacade;
	@EJB
	private TipologiaService tipologiaService;
	@EJB
	private DeclaracaoIntervencaoAppFacade diapFacade;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private EmpreendimentoService empreendimentoService;

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	private final SecurityService securityService = SecurityService.getInstance();
	
	private Boolean showEquipeCronogramaAcoes;
	private Boolean isCoordenador;
	private Boolean isUsuarioInterno;
	private Boolean isSelic;
	private Boolean showTable;
	private Processo processo;
	private Collection<Integer> statusValido;
	private Collection<StatusFluxo> listaStatusFluxo;
	private StatusFluxo statusFluxo;
	private Collection<VwConsultaProcesso> listaProcessos;
	private LazyDataModel<VwConsultaProcesso> dataModelProcessos;
	private Collection<AtoAmbiental> listaAtosPorProcesso;
	private UIForm formularioASerLimpo;
	private Boolean visualizaFiltro = Boolean.FALSE;
	private Usuario usuario;
	private Area area;
	private Funcionario funcionario;
	private String lblRegistros = "";
	private String descnome;
	private Pessoa requerente;
	private String cpfcnpj;
	private VwConsultaProcesso vwConsultaProcessoSelecionado;
	
	private Empreendimento empreendimento;
	private List<SelectItem> collEmpreendimento;
	
	private List<Municipio> listaMunicipios;
	private Municipio  municipioSelecionado;
	
	private Tipologia tipologiaDivisao;
	private List<Tipologia> listaTipologiaDivisao;
	private Tipologia tipologiaAtividade;
	private List<Tipologia> listaTipologiaAtividade;
 
	private boolean impressaoPdf;
	private boolean consultaRealizada = false;

	@PostConstruct
	public void init() {
		carregarDadosBasicos();
		carregarMunicipios();
		consultarStatusFluxo();
		super.listarCategoria();
	}

	private void carregarDadosBasicos() {
		usuario = ContextoUtil.getContexto().getUsuarioLogado();
		area = new Area();
		funcionario = new Funcionario();
		PessoaFisica pessoaFisica = new PessoaFisica();
		funcionario.setPessoaFisica(pessoaFisica);
		if (usuario.getIndTipoUsuario()) {
			visualizaFiltro = true;
		} else {
			visualizaFiltro = false;
		}
		showEquipeCronogramaAcoes = Boolean.TRUE;
		showTable = Boolean.FALSE;		
		isCoordenador = (usuario.getIdePerfil().getIdePerfil().intValue() == PerfilEnum.COORDENADOR.getId());
		isSelic = funcionarioService.validarCoordenadorSelic(usuario.getPessoaFisica());
		carregarDivisao();

		if (Util.isNullOuVazio(empreendimento)) {
			this.empreendimento = new Empreendimento();
		}
		if (collEmpreendimento == null) {
			this.carregarEmpreendimento();
		}
	}

	private void carregarMunicipios() {
		try {
			this.listaMunicipios = (List<Municipio>) municipioService.filtrarListaMunicipiosPorEstado(new Estado(5));
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public void consultarProcesso() {
		try {
			if (validate()) {
				consultaRealizada=true;
				dataModelProcessos = new LazyDataModel<VwConsultaProcesso>() {
					
					private static final long serialVersionUID = -549249300009769036L;

					@Override
					public List<VwConsultaProcesso> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
						if (validate()) {
							try {
								Map<String, Object> params = getParametros();
								listaProcessos = vwProcessoService.listarPorCriteriaDemanda(params, OperacaoProcessoEnum.CONSULTAR, null, first, pageSize);
							} 
							catch (Exception e) {
								Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
								throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
							}
						}
						return (List<VwConsultaProcesso>) listaProcessos;
					}
				};
				dataModelProcessos.setRowCount(getRowCountProcesso());
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível realizar a consulta de processo.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void limparTela() {
		statusFluxo = null;
		consultaRealizada=false;
		listaProcessos = null;
		municipioSelecionado = new Municipio();
		area = new Area();
		funcionario = new Funcionario();
		dataModelProcessos = null;
		requerente = null;
		tipologiaDivisao = null;
		listaTipologiaDivisao = null;
		tipologiaAtividade = null;
		listaTipologiaAtividade = null;
		empreendimento = new Empreendimento();
		super.limpar();
	}

	private boolean validate() {
		if (!Util.validaPeriodo(getPeriodoInicio(), getPeriodoFim())) {
			JsfUtil.addErrorMessage(BUNDLE.getString("geral_msg_periodo_invalido"));
			return false;
		}
		return true;
	}

	
	public Collection<StatusFluxo> getStatus() {
		if (listaStatusFluxo == null) {
			consultarStatusFluxo();
		}
		return listaStatusFluxo;
	}

	private void consultarStatusFluxo() {
		try {
			if(securityService.canAccessUsingCache("5.47.17")){
				listaStatusFluxo = statusFluxoService.listarTodosStatusFluxoProcesso();
			}
			else{
				ArrayList<Integer> status = new ArrayList<Integer>();
				
				if(securityService.canAccessUsingCache("5.47.16")){
					status.add(StatusFluxoEnum.ARQUIVADO.getStatus());
				}
				if(securityService.canAccessUsingCache("5.47.18")){
					status.add(StatusFluxoEnum.CONCLUIDO.getStatus());
				}
				if(securityService.canAccessUsingCache("5.47.19")){
					status.add(StatusFluxoEnum.NOTIFICADO.getStatus());
				}
				if(securityService.canAccessUsingCache("5.47.20")){
					status.add(StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO.getStatus());
				}
				if(securityService.canAccessUsingCache("5.47.21")){
					status.add(StatusFluxoEnum.ENCAMINHADO_PARA_AREA.getStatus());
				}
				if(securityService.canAccessUsingCache("5.47.22")){
					status.add(StatusFluxoEnum.ANALISE_TECNICA.getStatus());
				}
				if(securityService.canAccessUsingCache("5.47.23")){
					status.add(StatusFluxoEnum.NOTIFICACAO_RESPONDIDA.getStatus());
				}
				if(securityService.canAccessUsingCache("5.47.24")){
					status.add(StatusFluxoEnum.NOTIFICACAO_NAO_RESPONDIDA.getStatus());
				}
				if(securityService.canAccessUsingCache("5.47.25")){
					status.add(StatusFluxoEnum.NOTIFICACAO_EXPEDIDA.getStatus());
				}
				if(securityService.canAccessUsingCache("5.47.26")){
					status.add(StatusFluxoEnum.ENVIADO_PUBLICACAO.getStatus());
				}
				if(securityService.canAccessUsingCache("5.47.27")){
					status.add(StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus());
				}
				if(securityService.canAccessUsingCache("5.47.28")){
					status.add(StatusFluxoEnum.FORMADO.getStatus());
				}
				if(!Util.isNullOuVazio(status))
					listaStatusFluxo = statusFluxoService.listarStatusRequerimento(status);
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	public List<Area> autoCompleteArea(String query) {
		List<Area> listaResultArea = new ArrayList<Area>();
		try {
			Area area = new Area();
			area.setNomArea(query);
			listaResultArea = (List<Area>) areaService.filtrarListaAreaLike(area);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		return listaResultArea;
	}

	public List<Funcionario> autoCompleteFuncionario(String query) {
		List<Funcionario> listaResultFunc = new ArrayList<Funcionario>();
		try {
			Funcionario funcionario = new Funcionario();
			PessoaFisica pessoaFisica = new PessoaFisica();
			pessoaFisica.setNomPessoa(query);
			funcionario.setPessoaFisica(pessoaFisica);
			funcionario.setIdeArea(area);
			listaResultFunc = (List<Funcionario>) funcionarioService.listarTecnicosAutoComplete(funcionario);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		return listaResultFunc;
	}

	
	protected int getRowCountProcesso() {
		
		int totalRowCount = 0;
		try {
				totalRowCount = vwProcessoService.consultarProcessosCount(getParametros(), OperacaoProcessoEnum.CONSULTAR, null);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		return totalRowCount;
	}

	@Override
	protected Map<String, Object> getParametros() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		Boolean consultaLivre = true;
		if (!Util.isNullOuVazio(super.getTipoAto()) && super.getTipoAto().getIdeTipoAto().intValue() == TipoAtoEnum.DECLARATORIO.getId() ||
			!Util.isNullOuVazio(super.getAtoAmbiental()) && super.getAtoAmbiental().getIdeAtoAmbiental().intValue() == AtoAmbientalEnum.LAC.getId().intValue() ){
			this.statusFluxo =  new StatusFluxo(2);
		}
		
		if(super.isNumProcessoPreenchido()) {
			params.put("numProcesso", super.getNumProcesso());
			consultaLivre =false;
		}
		if(super.isPeriodoInicialPreenchido()) {
			params.put("periodoInicio", super.getPeriodoInicio());
			consultaLivre =false;
		}
		if(super.isPeriodoFinalPreenchido()) {
			params.put("periodoFim", super.getPeriodoFim());
			consultaLivre =false;
		}
		if(super.isCategoriaSelected()) {
			params.put("tipoAto", super.getTipoAto().getIdeTipoAto());
			consultaLivre =false;
		}
		if (!Util.isNullOuVazio(statusFluxo)) {
			statusValido = new ArrayList<Integer>();
			statusValido.add(statusFluxo.getIdeStatusFluxo());
			params.put("statusFluxo", statusValido);
		}
		else{
			statusValido = new ArrayList<Integer>();
			Collection<StatusFluxo> statusFluxos=getStatus();
			if(!Util.isNullOuVazio(statusFluxos)){
				for (StatusFluxo status : statusFluxos) {
					if(!consultaLivre || ((StatusFluxoEnum.CONCLUIDO.getStatus() != status.getIdeStatusFluxo().intValue()) || ContextoUtil.getContexto().isUsuarioExterno() || ContextoUtil.getContexto().getUsuarioLogado().isMP())){
						statusValido.add(status.getIdeStatusFluxo());
					}
				}
				params.put("statusFluxo", statusValido);
			}
		}
		if(super.isAtoSelected()) {
			params.put("atoAmbiental", super.getAtoAmbiental().getIdeAtoAmbiental());
		}
		if(super.isTipologiaSelected()) {
			params.put("tipologia", super.getTipologia().getIdeTipologia());
		}
		if(super.isFinalidadeSelected()) {
			params.put("finalidade", super.getFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua());
		}
		if (!Util.isNullOuVazio(area)) {
			params.put("area", area.getIdeArea());
		}
		if (!Util.isNullOuVazio(funcionario)) {
			params.put("funcionario", funcionario.getIdePessoaFisica());
		}
		if (!Util.isNullOuVazio(cpfcnpj)) {
			params.put("cpfcnpj", cpfcnpj.trim());
		}
		
		if(!Util.isNullOuVazio(municipioSelecionado)){ 
			params.put("idemunicipio", municipioSelecionado.getIdeMunicipio());
		}
		
		if(!Util.isNull(tipologiaDivisao)) {
			if(!Util.isNull(tipologiaAtividade)) {
				params.put("tipologiaAtividade", tipologiaAtividade);
				
			} else{
				List<Integer> listaIdeTipologiaAtividade = new ArrayList<Integer>();
				
				for(Tipologia t : listaTipologiaAtividade) {
					listaIdeTipologiaAtividade.add(t.getIdeTipologia());
				}
				
				params.put("listaIdeTipologiaAtividade", listaIdeTipologiaAtividade);
			}
		}
		if (!Util.isNullOuVazio(empreendimento) && !empreendimento.getIdeEmpreendimento().equals(0)) {
			params.put("empreendimento", empreendimento);
		}
				
		return params;
	}

	public void listarAtos() {
		try {
			vwConsultaProcessoSelecionado = vwProcessoService.getProcessoComListaAtos(vwConsultaProcessoSelecionado);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public StreamedContent imprimirRelatorioLacErb(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirRelatorioLacErb(ideRequerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirRelatorioLacPosto(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirRelatorioLacPosto(ideRequerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirRelatorioLacTransportadora(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirRelatorioLacTransportadora(ideRequerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirCertificadoLacErb(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirCertificadoLacErb(ideRequerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirCertificadoLacPosto(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirCertificadoLacPosto(ideRequerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirCertificadoLacTransportadora(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirCertificadoLacTransporte(ideRequerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirCertificadoRLAC(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirCertificadoRLAC(ideRequerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirCertificadoTcraLac(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirCertificadoTcraLac(ideRequerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirRelatorioCorteFloresta(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirRelatorioCorteFloresta(ideRequerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirRegistroCorteFlorestaProducaoPlantada(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirRegistroCorteFlorestaProducaoPlantada(ideRequerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirDQC(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirDQC(ideRequerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirCertificadoAPE(Integer ideRequerimento) {
		try {
			return relatoriosRequerimentoServiceFacade.imprimirCertificadoAPE(ideRequerimento);
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a solicitação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void carregarDivisao() {
		try {
			if(Util.isNullOuVazio(listaTipologiaDivisao)) {
				listaTipologiaDivisao = tipologiaService.listarTipologiaDivisao();
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarAtividade() {
		try {
			if(!Util.isNull(tipologiaDivisao)) {
				listaTipologiaAtividade = tipologiaService.listarTipologiaAtividade(tipologiaDivisao);
			}
			else{
				listaTipologiaAtividade = null;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarEmpreendimento() {
		try {

			Collection<Empreendimento> collectionsEmpreendimento = inicializaCollections();

			if (!Util.isNull(requerente)) {
				collectionsEmpreendimento = empreendimentoService.listarEmpreendimento(requerente);
			}

			if (!Util.isNullOuVazio(collectionsEmpreendimento)) {
				for (Empreendimento empreendimento : collectionsEmpreendimento) {
					SelectItem item = new SelectItem(empreendimento.getIdeEmpreendimento(),empreendimento.getNomEmpreendimento());
					this.collEmpreendimento.add(item);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível carregar a lista de empreendimentos - " + e.getMessage());
		}
	}
	
	private Collection<Empreendimento> inicializaCollections() {

		collEmpreendimento = new ArrayList<SelectItem>();
		Collection<Empreendimento> collectionsEmpreendimento = null;
		return collectionsEmpreendimento;
	}
	
	public void selecionarRequerente(Pessoa requerente) {
		this.requerente = requerente;
		if (!Util.isNullOuVazio(requerente) && !Util.isNullOuVazio(requerente.getPessoaFisica())) {
			descnome = requerente.getPessoaFisica().getNomPessoa();
			cpfcnpj = requerente.getPessoaFisica().getNumCpf();
		} else if (!Util.isNullOuVazio(requerente) && !Util.isNullOuVazio(requerente.getPessoaJuridica())) {
			descnome = requerente.getPessoaJuridica().getNomRazaoSocial();
			cpfcnpj = requerente.getPessoaJuridica().getNumCnpj();
		}
		
		carregarEmpreendimento();
		Html.atualizar("formConsultarProcesso");
	}
	
	public boolean isDisabledComboAtividade() {
		return Util.isNull(tipologiaDivisao);		
	}
	
	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}

	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}

	public Collection<VwConsultaProcesso> getListaProcessos() {
		return listaProcessos;
	}

	public void setListaProcessos(Collection<VwConsultaProcesso> processos) {
		this.listaProcessos = processos;
	}

	public Collection<StatusFluxo> getListaStatusFluxo() {
		return listaStatusFluxo;
	}

	public void setListaStatusFluxo(Collection<StatusFluxo> listaStatusFluxo) {
		this.listaStatusFluxo = listaStatusFluxo;
	}

	public StatusFluxo getStatusFluxo() {
		return statusFluxo;
	}

	public void setStatusFluxo(StatusFluxo statusFluxo) {
		this.statusFluxo = statusFluxo;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public Collection<AtoAmbiental> getListaAtosPorProcesso() {
		return listaAtosPorProcesso;
	}

	public void setListaAtosPorProcesso(Collection<AtoAmbiental> listaAtosPorProcesso) {
		this.listaAtosPorProcesso = listaAtosPorProcesso;
	}

	public Boolean getVisualizaFiltro() {
		return visualizaFiltro;
	}

	public void setVisualizaFiltro(Boolean visualizaFiltro) {
		this.visualizaFiltro = visualizaFiltro;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public VwConsultaProcesso getVwConsultaProcessoSelecionado() {
		return vwConsultaProcessoSelecionado;
	}

	public void setVwConsultaProcessoSelecionado(VwConsultaProcesso vwConsultaProcessoSelecionado) {
		this.vwConsultaProcessoSelecionado = vwConsultaProcessoSelecionado;
	}

	public Boolean getShowEquipeCronogramaAcoes() {
		// se for usuario interno ou o usuário for coordenador deve aparecer.
		if (getIsUsuarioInterno() || getIsCoordenador()) {
			showEquipeCronogramaAcoes = Boolean.TRUE;
		} else {
			showEquipeCronogramaAcoes = Boolean.FALSE;
		}
		return showEquipeCronogramaAcoes;
	}

	public void setShowEquipeCronogramaAcoes(Boolean showEquipeCronogramaAcoes) {
		this.showEquipeCronogramaAcoes = showEquipeCronogramaAcoes;
	}

	public Boolean getIsCoordenador() {
		if (ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil().intValue() == PerfilEnum.COORDENADOR.getId()) {
			isCoordenador = Boolean.TRUE;
		} else {
			isCoordenador = Boolean.FALSE;
		}
		return isCoordenador;
	}

	public void setIsCoordenador(Boolean isCoordenador) {
		this.isCoordenador = isCoordenador;
	}

	public Boolean getIsUsuarioInterno() {
		this.isUsuarioInterno = ContextoUtil.getContexto().getUsuarioLogado().getIndTipoUsuario();
		return isUsuarioInterno;
	}

	public void setIsUsuarioInterno(Boolean isUsuarioInterno) {
		this.isUsuarioInterno = isUsuarioInterno;
	}

	public LazyDataModel<VwConsultaProcesso> getDataModelProcessos() {
		return dataModelProcessos;
	}

	public void setDataModelProcessos(LazyDataModel<VwConsultaProcesso> dataModelProcessos) {
		this.dataModelProcessos = dataModelProcessos;
	}

	public Boolean getShowTable() {
		if (!Util.isNullOuVazio(dataModelProcessos)) {
			showTable = true;
		} else {
			showTable = false;
		}
		return showTable;
	}

	public void setShowTable(Boolean showTable) {
		this.showTable = showTable;
	}

	public String getLblRegistros() {
		return lblRegistros;
	}

	public void setLblRegistros(String lblRegistros) {
		this.lblRegistros = lblRegistros;
	}
	
	
	public Pessoa getRequerente() {
		if (Util.isNullOuVazio(requerente))
			requerente = new Pessoa();
		return requerente;
	}

	public void setRequerente(Pessoa pRequerente) {
		this.requerente = pRequerente;
	}

	public String getDescnome() {
		if(!Util.isNullOuVazio(requerente)){
			if (!Util.isNullOuVazio(requerente.getPessoaFisica())) {
					this.descnome = requerente.getPessoaFisica().getNomPessoa();
					return this.descnome;
			}
			else if(!Util.isNullOuVazio(this.requerente.getPessoaJuridica())) {
				this.descnome = requerente.getPessoaJuridica().getNomRazaoSocial();
				return this.descnome;
			}
		}
		else {
			cpfcnpj ="";
			return "";
		}
		
		return "";
	}

	public void setDescnome(String descnome) {
		this.descnome = descnome;
	}

	public Boolean getIsSelic() {
		return isSelic;
	}

	public void setIsSelic(Boolean isSelic) {
		this.isSelic = isSelic;
	}
	
	public boolean habilitarCronogramaFormacaoEquipe(VwConsultaProcesso vwProcesso){		
		PessoaFisica pf = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
		Funcionario funcionario = funcionarioService.buscarFuncionarioPorPessoaFisica(pf);		
		try{
			if(!vwProcesso.getArea().equals(funcionario.getIdeArea())
			   || isSelic 
			   || !isCoordenador 
			   || (vwProcesso.getStatusFluxo().getIdeStatusFluxo() != StatusFluxoEnum.FORMADO.getStatus() && vwProcesso.getStatusFluxo().getIdeStatusFluxo() != StatusFluxoEnum.ENCAMINHADO_PARA_AREA.getStatus())){
				return true;			
			}		
			return false;			
		}
		catch(Exception e){
			return true;
		}		
	}
	
	public boolean isExisteDiap(Integer ideRequerimento) throws Exception{
		
		Collection<AtoAmbiental> atosAmbientais = atoAmbientalService.listarAtoAmbientalPorEnquadramentoRequerimento(ideRequerimento);
		for (AtoAmbiental atoAmbiental : atosAmbientais) {
			if (atoAmbiental.getIdeAtoAmbiental().equals(AtoAmbientalEnum.DIAP.getId())){
				return true;
			}
		}
		return false;
	}
	
	private StreamedContent getImprimirCertificadoAtoDeclaratorio(ImpressoraAtoDeclaratorioIF imprimirAtoDeclaratorioIF, Integer ideRequerimento, DocumentoObrigatorioEnum docObrigatorioEnum) throws Exception{
		return imprimirAtoDeclaratorioIF.imprimirCertificado(ideRequerimento, docObrigatorioEnum);
	}

	public StreamedContent getImprimirCertificadoDIAP(Integer ideRequerimento){

		Requerimento requerimento = new Requerimento(ideRequerimento);
		
		try {
			relatoriosRequerimentoServiceFacade.gerarCertificados(requerimento.getIdeRequerimento());
			return getImprimirCertificadoAtoDeclaratorio(new ImpressoraAtoDeclaratorioDIAP(diapFacade.montarTextoRepresentadoPor(requerimento)), requerimento.getIdeRequerimento(), DocumentoObrigatorioEnum.FORMULARIO_DIAP);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a solicitação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	

	public boolean isImpressaoPdf() {
		return impressaoPdf;
	}

	public void setImpressaoPdf(boolean impressaoPdf) {
		this.impressaoPdf = impressaoPdf;
	}
	
	public StreamedContent getImprimirRelatorio()  {
		if (validate()) {
			try {
				Map<String, Object> params = getParametros();
				listaProcessos = vwProcessoService.listarPorCriteriaDemanda(params, OperacaoProcessoEnum.CONSULTAR, null);

				final Map<String, Object> lParametros = new HashMap<String, Object>();
				
				JRBeanCollectionDataSource lista = new JRBeanCollectionDataSource(listaProcessos);
				lParametros.put("consulta", lista);
				lParametros.put("totalProcessos", listaProcessos.toArray().length);
				if(this.impressaoPdf==true) {
					return new RelatorioUtil("relatorioProcessoPDF.jasper", lParametros, "logoInemaRelatorio.png",
							"sema_vertical.png").gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
				}else {
					return new RelatorioUtil("relatorioProcessoXLS.jasper", lParametros, "logoInemaRelatorio.png",
							"sema_vertical.png").gerarRelatorio(RelatorioUtil.RELATORIO_XLS, true);	
				}
				
				
			} 
			catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
			}
		}
		return null;

	}

	public List<Municipio> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(List<Municipio> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public Municipio getMunicipioSelecionado() {
		return municipioSelecionado;
	}

	public void setMunicipioSelecionado(Municipio municipioSelecionado) {
		this.municipioSelecionado = municipioSelecionado;
	}

	public Tipologia getTipologiaDivisao() {
		return tipologiaDivisao;
	}

	public void setTipologiaDivisao(Tipologia tipologiaDivisao) {
		this.tipologiaDivisao = tipologiaDivisao;
	}

	public List<Tipologia> getListaTipologiaDivisao() {
		return listaTipologiaDivisao;
	}

	public void setListaTipologiaDivisao(List<Tipologia> listaTipologiaDivisao) {
		this.listaTipologiaDivisao = listaTipologiaDivisao;
	}

	public Tipologia getTipologiaAtividade() {
		return tipologiaAtividade;
	}

	public void setTipologiaAtividade(Tipologia tipologiaAtividade) {
		this.tipologiaAtividade = tipologiaAtividade;
	}

	public List<Tipologia> getListaTipologiaAtividade() {
		return listaTipologiaAtividade;
	}

	public void setListaTipologiaAtividade(List<Tipologia> listaTipologiaAtividade) {
		this.listaTipologiaAtividade = listaTipologiaAtividade;
	}
	
	public List<SelectItem> getCollEmpreendimento() {
		return collEmpreendimento;
	}

	public void setCollEmpreendimento(List<SelectItem> collEmpreendimento) {
		this.collEmpreendimento = collEmpreendimento;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public boolean isConsultaRealizada() {
		return consultaRealizada;
	}

	public void setConsultaRealizada(boolean consultaRealizada) {
		this.consultaRealizada = consultaRealizada;
	}
}