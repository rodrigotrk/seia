package br.gov.ba.seia.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Cnae;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.NaturezaJuridica;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisicaHistorico;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaHistorico;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.CnaeService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.NaturezaJuridicaService;
import br.gov.ba.seia.service.PessoaJuridicaHistoricoService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityController;
import br.gov.ba.seia.util.security.SecurityService;

/**
*
* @author rubem.filho
*/
@SuppressWarnings("serial")
@Named("pessoaJuridicaController")
@ViewScoped
public class PessoaJuridicaController extends PessoaEndereco {	
	
	private LazyDataModel<PessoaJuridica> modelPessoasJuridicas;// = new ListDataModel<PessoaJuridica>();
	private DataModel<Cnae> modelCnae = new ListDataModel<Cnae>();
	private List<SelectItem> naturezaJuridicaItens;
	private PessoaJuridica pessoaJuridicaSelecionada = new PessoaJuridica();
	private PessoaJuridica pessoaJuridicaAntiga = new PessoaJuridica();	
	private String razao; 
	private String cnpj;
	private ContextoController contexto;
	private Boolean disableAbasDependencias;
	private Boolean disableForm;
	private Boolean disableEdicaoRazaoSocial;
	private int tabAtual;
	private Boolean editMode;
	private String urlOrigem;
	private Boolean temArquivo;
	private String caminhoArquivo;
	private List<String> listaArquivo;
	private StreamedContent arquivoBaixar;
	private Date dataAtual = new Date();
	
	private static Integer abaMAX = 6;
	
	private Boolean visualizaProximo;
	private Boolean visualizaAnterior;
	private Boolean visualizaFinalizar;
	
	private Boolean telaIdentificacaoPapel;
	private Boolean visualizarDados;
	private Boolean pessoaCadastrada;
	private Boolean disableFormVisualizar;
	private Boolean uploadFileField;
	
	private List<PessoaJuridicaHistorico> listaPessoaJuridicaHistorico;
	
	private Empreendimento empreendimento;
	
	private int refresh;
	
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	
	@EJB
	private NaturezaJuridicaService naturezaJuridicaService;
	
	@EJB
	private CnaeService cnaeService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private AreaService areaService;
	
	@EJB 
	private PessoaJuridicaHistoricoService pessoaJuridicaHistoricoService;
	
	public void excluirPessoaJuridica() {
		try {
			super.pessoa = new Pessoa();
			super.pessoa.setIdePessoa(pessoaJuridicaSelecionada.getIdePessoaJuridica());
			pessoaJuridicaSelecionada.setPessoa(super.pessoa);
			pessoaJuridicaService.excluirPessoaJuridica(pessoaJuridicaSelecionada);
			limparObjetos();
			carregarLazyConsultaPessoaJuridica();
			JsfUtil.addSuccessMessage("Exclusão efetuada com sucesso!");
		} catch (AppExceptionError exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			limparObjetos();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
			limparObjetos();
		}		
	}

	public String consultarPessoaJuridica() {
		try {			
			pessoaJuridicaSelecionada.setNomRazaoSocial(razao);
			pessoaJuridicaSelecionada.setNumCnpj(cnpj);	
			carregarLazyConsultaPessoaJuridica();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}		
		return "";
	}
			
	public void consultarPessoaJuridicaPorCnpj() {
		try {
			String numCnpj = this.pessoaJuridicaSelecionada.getNumCnpj();
			if(Util.validaCNPJ(numCnpj)){
				PessoaJuridica pessoaJuridicaFiltro = new PessoaJuridica();
				pessoaJuridicaFiltro.setNumCnpj(numCnpj);
				this.pessoaJuridicaSelecionada = pessoaJuridicaService.filtrarPessoaFisicaByCnpj(pessoaJuridicaFiltro);
				
				if(!Util.isNull(this.pessoaJuridicaSelecionada)){
					super.pessoa   = this.pessoaJuridicaSelecionada.getPessoa();
					ContextoUtil.getContexto().setRequerenteRequerimento(super.pessoa );
					disableAbasDependencias = false;
					disableEdicaoRazaoSocial = true;
					if(!Util.isNullOuVazio(pessoaJuridicaSelecionada.getDscCaminhoArquivo())){
						if(!Util.isNull(listaArquivo) && listaArquivo.isEmpty()){
							listaArquivo.add(FileUploadUtil.getFileName(pessoaJuridicaSelecionada.getDscCaminhoArquivo()));
							this.temArquivo = true;
						}
					}
				} 
				else {
					pessoaJuridicaSelecionada = new PessoaJuridica();
					super.pessoa   = new Pessoa();
					pessoaJuridicaSelecionada.setNumCnpj(numCnpj);
					disableAbasDependencias = true;
					listaArquivo = new ArrayList<String>();
					this.temArquivo = false;
					disableEdicaoRazaoSocial = false;
					pessoaCadastrada = false;
				}
				disableForm = false;
			}
			else{
				disableForm = true;
			}
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}
	
	public void preparaPessoaTerceiro(){
		super.pessoa = pessoaJuridicaSelecionada.getPessoa();
	}
	
	public String consultarPessoaJuridicaPorCnpjIdentificarPapel() {
		consultarPessoaJuridicaPorCnpj();
		return null;
	}
	
	private boolean validarIdentificacaoPessoaJuridica(){
		IdentificarPapelController identificarPapelController = (IdentificarPapelController) SessaoUtil.recuperarManagedBean("#{identificarPapelController}", IdentificarPapelController.class);
		if(!Util.isNull(identificarPapelController) && !identificarPapelController.isNecessitaValidacao()){
			return true;
		}
		return pessoaJuridicaService.validarIdentificacaoPessoaJuridica(pessoaJuridicaSelecionada);
	}
	
	public void salvar() {
		if(validarIdentificacaoPessoaJuridica()){
			try {
				if (Util.isNullOuVazio(super.pessoa)) {
					super.pessoa.setDtcCriacao(new Date());
				}
				super.pessoa.setDtcExclusao(null);
				super.pessoa.setIndExcluido(false);

				pessoaJuridicaSelecionada.setPessoa(super.pessoa);
				super.pessoa.setPessoaJuridica(pessoaJuridicaSelecionada);
				
				if(!Util.isNullOuVazio(caminhoArquivo)) {
					pessoaJuridicaSelecionada.setDscCaminhoArquivo(caminhoArquivo);
				}
				
				if(Util.isNull(pessoaJuridicaHistoricoService.buscarHistoricoAnterior(pessoaJuridicaSelecionada)) && !Util.isNullOuVazio(this.pessoaJuridicaAntiga.getNomRazaoSocial()) && !pessoaJuridicaAntiga.getNomRazaoSocial().equals(pessoaJuridicaSelecionada.getNomRazaoSocial())) {
					pessoaJuridicaService.salvarOuAtualizarPessoaJuridica(pessoaJuridicaSelecionada);
					pessoaJuridicaHistoricoService.salvarPessoaJuridicaHistorico(preencherHistoricoPessoaJuridica());
				} else {
					if(!Util.isNullOuVazio(pessoaJuridicaAntiga.getNomRazaoSocial()) && !pessoaJuridicaAntiga.getNomRazaoSocial().equals(pessoaJuridicaSelecionada.getNomRazaoSocial())){
						pessoaJuridicaService.salvarOuAtualizarPessoaJuridica(pessoaJuridicaSelecionada);
						pessoaJuridicaHistoricoService.salvarPessoaJuridicaHistorico(preencherHistoricoNovaPessoaJuridica());
						
					} else {
						pessoaJuridicaService.salvarOuAtualizarPessoaJuridica(pessoaJuridicaSelecionada);
					}
					
				}

				contexto = ContextoUtil.getContexto();
				contexto.setPessoa(super.pessoa);
				disableAbasDependencias = false;
				disableForm = false;

				if (editMode) {
					JsfUtil.addSuccessMessage("Alteração efetuada com sucesso!");
				} 
				else {
					JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
				}
			} catch (Exception exception) {
				disableForm = true;
				if (exception.getCause() instanceof ConstraintViolationException) {
					JsfUtil.addErrorMessage("Este CNPJ já foi cadastrado. Favor entrar em contato com o atendimento.");
				} 
				else {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
					JsfUtil.addErrorMessage(exception.getMessage());
				}
			}
		}
	}
	
	public PessoaJuridicaHistorico preencherHistoricoPessoaJuridica() {
		PessoaJuridicaHistorico pessoaJuridicaHistorico = new PessoaJuridicaHistorico();
		PessoaJuridicaHistorico oldPessoaJuridicaHistorico = new PessoaJuridicaHistorico();
		
		oldPessoaJuridicaHistorico = pessoaJuridicaHistoricoService.buscarHistoricoAnterior(pessoaJuridicaSelecionada);
		
		pessoaJuridicaHistorico.setIdePessoaJuridica(pessoaJuridicaSelecionada);
		pessoaJuridicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaJuridicaHistorico.setDtcHistorico(new Date());
		pessoaJuridicaHistorico.setNomRazaoSocial(pessoaJuridicaSelecionada.getNomRazaoSocial());
		
		if(!Util.isNullOuVazio(pessoaJuridicaSelecionada.getNumInscricaoMunicipal())) {
			pessoaJuridicaHistorico.setNumInscricaoMunicipal(pessoaJuridicaSelecionada.getNumInscricaoMunicipal());
		}
		
		if(!Util.isNullOuVazio(oldPessoaJuridicaHistorico)) {
			pessoaJuridicaHistorico.setDscCaminhoArquivo(oldPessoaJuridicaHistorico.getDscCaminhoArquivo());
			pessoaJuridicaHistorico.setIdeProcesso(oldPessoaJuridicaHistorico.getIdeProcesso());
			pessoaJuridicaHistorico.setNomeFantasia(oldPessoaJuridicaHistorico.getNomeFantasia());
			pessoaJuridicaHistorico.setDtcAbertura(oldPessoaJuridicaHistorico.getDtcAbertura());
			pessoaJuridicaHistorico.setNomNaturezaJuridica(oldPessoaJuridicaHistorico.getNomNaturezaJuridica());
		} 
		
		
		return pessoaJuridicaHistorico;
	}
	
	public PessoaJuridicaHistorico preencherHistoricoNovaPessoaJuridica() {
		PessoaJuridicaHistorico pessoaJuridicaHistorico = new PessoaJuridicaHistorico();
		
		pessoaJuridicaHistorico.setIdePessoaJuridica(pessoaJuridicaSelecionada);
		pessoaJuridicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaJuridicaHistorico.setDtcHistorico(new Date());
		pessoaJuridicaHistorico.setNomRazaoSocial(pessoaJuridicaSelecionada.getNomRazaoSocial());
		pessoaJuridicaHistorico.setNumInscricaoMunicipal(pessoaJuridicaSelecionada.getNumInscricaoMunicipal());
		pessoaJuridicaHistorico.setDscCaminhoArquivo(pessoaJuridicaSelecionada.getDscCaminhoArquivo());
		pessoaJuridicaHistorico.setNomeFantasia(pessoaJuridicaSelecionada.getNomeFantasia());
		pessoaJuridicaHistorico.setDtcAbertura(pessoaJuridicaSelecionada.getDtcAbertura());
		
		
		return pessoaJuridicaHistorico;
	}
	
	public void carregarNaturezaJuridica(){
		naturezaJuridicaItens = new ArrayList<SelectItem>();	
		try {
			for (NaturezaJuridica naturezaJuridica : naturezaJuridicaService.listarNaturezaJuridica()) {
				naturezaJuridicaItens.add(new SelectItem(naturezaJuridica, naturezaJuridica.getNomNaturezaJuridica()));
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}		
	}
	
	public void carregarModelCnae(PessoaJuridica pessoaJuridica){		
		try {
			this.modelCnae = new ListDataModel<Cnae>(cnaeService.listarCnaePorPessoaJuridica(pessoaJuridica));
			System.out.println(modelCnae.getRowCount());
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());			
		}
	}
	
	@PostConstruct
	public void init() {
		try {
			
			this.refresh = 0;
			disableAbasDependencias = true;
			disableForm = true;		
			uploadFileField = false;	
			temArquivo = false;
			listaArquivo = new ArrayList<String>();
			telaIdentificacaoPapel = false;
			visualizarDados = false;
			pessoaCadastrada = false;
			telaIdentificacaoPapel = false;
			disableFormVisualizar = ContextoUtil.getContexto().getVisualizarPessoaJuridica();
			super.pessoa   = new Pessoa();
			carregarNaturezaJuridica();
			this.pessoaJuridicaSelecionada = new PessoaJuridica();
			this.pessoaJuridicaSelecionada.setIdeNaturezaJuridica(new NaturezaJuridica());
			
			tratarPessoaSelecionadaSessao();
			carregarLazyModelPessoaJuridica();
			tratarDisableEdicaoRazaoSocialSessao();
			
			this.pessoaJuridicaAntiga = new PessoaJuridica();
			if(!Util.isNullOuVazio(this.pessoaJuridicaSelecionada)) {
				this.pessoaJuridicaAntiga.setNomRazaoSocial(this.pessoaJuridicaSelecionada.getNomRazaoSocial());
			}
			
			String lUrl = (String) SessaoUtil.recuperarObjetoSessao("URL_PROCURADOR_ORIGEM");
	
			if (!Util.isNullOuVazio(lUrl)) {
				setTabAtual(6);//Aba de Procurador
				this.urlOrigem = lUrl;
			}
			enviarId();
			desabilitarParaVisualizacao();
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isDisabledCnpj() {
		return !Util.isNullOuVazio(pessoaJuridicaSelecionada);
	}
	
	public boolean isRenderedBtnLimpar() {
		return Util.isNullOuVazio(pessoaJuridicaSelecionada);
	}
	
	public boolean isUserExterno() {
		return ContextoUtil.getContexto().isUsuarioExterno();
	}
	
	public void validacaoUsuario() {
		if(isUserExterno() && Util.isNullOuVazio(pessoaJuridicaSelecionada)  && this.refresh == 0) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
				this.refresh++;
			} catch (Exception exception) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}
		
		if(isUserExterno() && Util.isNullOuVazio(pessoaJuridicaSelecionada)) {
			this.disableAbasDependencias = true;
			this.disableFormVisualizar = true;
		}
	}
	
	public void limparObjetos(){
		super.pessoa   = new Pessoa();
		this.pessoaJuridicaSelecionada = new PessoaJuridica();
		/*this.pessoaJuridicaSelecionada.setIdeNaturezaJuridica(new NaturezaJuridica());*/
		this.disableAbasDependencias = true;
		this.disableEdicaoRazaoSocial = false;
		setTabAtual(0);
		this.temArquivo = false;
		listaArquivo = new ArrayList<String>();
		ContextoUtil.getContexto().setPessoaJuridica(pessoaJuridicaSelecionada);
		
		IdentificarPapelController identificarPapelController = (IdentificarPapelController) SessaoUtil.recuperarManagedBean("#{identificarPapelController}", IdentificarPapelController.class); 
		identificarPapelController.setPessoaJuridica(pessoaJuridicaSelecionada); 
	}
	
	public void limparInternoPJ() {
		
		super.pessoa   = new Pessoa();		
		String CNPJ = new String(this.pessoaJuridicaSelecionada.getNumCnpj());
		String razaoSocial = "";
		
		if(this.pessoaJuridicaSelecionada.getNomRazaoSocial()!=null) {
			new String(this.pessoaJuridicaSelecionada.getNomRazaoSocial());
		}
		
		forcarLimparForm();
		
		this.pessoaJuridicaSelecionada = new PessoaJuridica();
		this.pessoaJuridicaSelecionada.setNumCnpj(CNPJ);
		this.pessoaJuridicaSelecionada.setNomRazaoSocial(razaoSocial);
		this.pessoaJuridicaSelecionada.setIdeNaturezaJuridica(new NaturezaJuridica());

		this.disableAbasDependencias = true;
		this.disableEdicaoRazaoSocial = true;
		
		setTabAtual(0);
		this.temArquivo = false;
		listaArquivo = new ArrayList<String>();
		ContextoUtil.getContexto().setPessoaJuridica(pessoaJuridicaSelecionada);
	}
	
	private void forcarLimparForm() {
		UIComponent component = FacesContext.getCurrentInstance().getViewRoot().findComponent("tabviewpj:formpessoajuridica");
		if(component != null) {
			limparComponentesFormulario(component);
		}
	}

	/**
	* Limpa os dados dos componentes de edição e de seus filhos,
	* recursivamente. Checa se o componente é instância de EditableValueHolder
	* e 'reseta' suas propriedades.
	*/
	private void limparComponentesFormulario(UIComponent pComponente) {

		if (pComponente instanceof EditableValueHolder) {
			EditableValueHolder lComponenteEditavel = (EditableValueHolder) pComponente;
			lComponenteEditavel.setSubmittedValue(null);
			lComponenteEditavel.setValue(null);
			lComponenteEditavel.setLocalValueSet(false);
			lComponenteEditavel.setValid(true);
		}

		if (!Util.isNull(pComponente) &&  pComponente.getChildCount() > 0) {

			for (UIComponent lComponenteFilho : pComponente.getChildren()) {
				limparComponentesFormulario(lComponenteFilho);
			}
		}
	}
	
	public LazyDataModel<PessoaJuridica> getModelPessoasJuridicas() {
		return modelPessoasJuridicas;
	}
	
	public String prepararParaEditar(){
		ContextoUtil.getContexto().setPessoaJuridica(pessoaJuridicaSelecionada);
		ContextoUtil.getContexto().setVisualizarPessoaJuridica(false);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		
		session.setAttribute("DisableEdicaoRazaoSocial", disableEdicaoRazaoSocial);
		
		return "/paginas/manter-pessoajuridica/cadastro.xhtml";
	}
	
	public String prepararParaVisualizar(){
		ContextoUtil.getContexto().setPessoaJuridica(pessoaJuridicaSelecionada);
		ContextoUtil.getContexto().setVisualizarPessoaJuridica(true);
		return "/paginas/manter-pessoajuridica/cadastro.xhtml";
	}
	
	private void tratarDisableEdicaoRazaoSocialSessao() throws Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		disableEdicaoRazaoSocial = (Boolean) session.getAttribute("DisableEdicaoRazaoSocial");
		
		if ("isabel.cristina".equalsIgnoreCase(SecurityService.getUser().getDscLogin())) {
		    disableEdicaoRazaoSocial=false;
		}
		
		removerDisableEdicaoRazaoSocialSessao();
	}
	
	private void removerDisableEdicaoRazaoSocialSessao() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		session.removeAttribute("DisableEdicaoRazaoSocial");
	}
	
	private void tratarPessoaSelecionadaSessao() {
		pessoaJuridicaSelecionada = ContextoUtil.getContexto().getPessoaJuridica();
		editMode = false;
		if (Util.isNull(pessoaJuridicaSelecionada)) {
			pessoaJuridicaSelecionada = new PessoaJuridica();
			editMode = Boolean.FALSE;
		} else {
			super.pessoa   = pessoaJuridicaSelecionada.getPessoa();
			if(!Util.isNull(pessoaJuridicaSelecionada.getIdePessoaJuridica())){
				this.disableForm = false;
				this.disableAbasDependencias = false;
				editMode = true;			
				listaArquivo.add(FileUploadUtil.getFileName(pessoaJuridicaSelecionada.getDscCaminhoArquivo()));
				caminhoArquivo = pessoaJuridicaSelecionada.getDscCaminhoArquivo();
				if(!Util.isNullOuVazio(pessoaJuridicaSelecionada.getDscCaminhoArquivo())) {
					temArquivo = true;
				}
			}
			
		}
	}
	
	public void removerPessoaJuridicaDaSessao() {
		ContextoUtil.getContexto().setPessoaJuridica(null);
		ContextoUtil.getContexto().setVisualizarPessoaJuridica(false);
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
        setTabAtual(activeTabIndex);  
        getVisualizaAnterior();   
    } 
    
	@Override
	public void enviarId() {
		super.enviarId("tabviewpj");
	}

	@Override
	public void desabilitarParaVisualizacao() {
		if(!Util.isNull(ContextoUtil.getContexto().getVisualizarPessoaJuridica()) && ContextoUtil.getContexto().getVisualizarPessoaJuridica()){
			enderecoGenericoController.setVisualizacao(true);
		} 
		else {
			enderecoGenericoController.setVisualizacao(false);
		}
	}
    
    public String finalizar() {
    	limparObjetos();
    	if (Util.isNullOuVazio(urlOrigem)) {
    		urlOrigem = "/paginas/manter-pessoajuridica/consulta.xhtml";
    	} 
    	return urlOrigem;
    }
    
    
    public void incrementaIndexAba() {
		setTabAtual(this.tabAtual + 1);
	}

	public void decrementaIndexAba() {
		setTabAtual(this.tabAtual -1);
	}
	
	
	public boolean isExisteArquivo(String caminhoArquivo){
	
		File  file  = new File(caminhoArquivo);
		
		if(file.exists()){
			return true;
		}else {
			return false;
		}
		
	}
	
	
	public void trataArquivo(FileUploadEvent event) {
		
		try{
			
			caminhoArquivo = FileUploadUtil.Enviar(event,DiretorioArquivoEnum.EMPREENDIMENTO.toString());
			temArquivo = true;
			listaArquivo.clear();
			listaArquivo.add(FileUploadUtil.getFileName(caminhoArquivo));
			pessoaJuridicaSelecionada.setDscCaminhoArquivo(caminhoArquivo);
			
			
			if(isExisteArquivo(pessoaJuridicaSelecionada.getDscCaminhoArquivo())){
				
				
				JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
				RequestContext.getCurrentInstance().addPartialUpdateTarget("tabviewpj:formpessoajuridica:arquivoHidden");

			}else {
				
				JsfUtil.addErrorMessage("Erro: Não foi possível enviar o arquivo.");

			}
			
		}
		
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
	    	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}
	
	public boolean isVerificaEdicaoRazaoSocial() {
		
		if(disableFormVisualizar || disableForm) return true;
		
		if(disableEdicaoRazaoSocial != null && disableEdicaoRazaoSocial && !isCoordenadorAtendSelicComPermissao()) return true;
		
		return false;
	}
	
	public boolean isCoordenadorAtendSelicComPermissao() {
	//TICKET #7208
		
		try {
			SecurityController security = (SecurityController) SessaoUtil.recuperarManagedBean("#{security}", SecurityController.class);
			Usuario usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
			
			//VERIFICA PERMISSAO PARA EDITAR RAZAO SOCIAL
			if (security != null && security.temAcesso("3.13.60")) {
				
				//VERIFICA SE O USUARIO EH COORDENADOR
				if(usuarioLogado != null && usuarioLogado.getIdePerfil() != null && usuarioLogado.getIdePerfil().getIdePerfil() != null && usuarioLogado.getIdePessoaFisica() != null
						&& usuarioLogado.getIdePerfil().getIdePerfil().equals(PerfilEnum.COORDENADOR.getId())) {
					
					Area areaPessoaLogada = areaService.buscarAreaPorPessoaFisica(usuarioLogado.getIdePessoaFisica());
					
					//VERIFICA SE O USUARIO EH DA ATEND OU SELIC
					if(!Util.isNullOuVazio(areaPessoaLogada) 
							&& (areaPessoaLogada.getIdeArea().equals(AreaEnum.ATEND.getId()) || areaPessoaLogada.getIdeArea().equals(AreaEnum.SELIC.getId()))) {
						
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return false;
	}

	public PessoaJuridicaService getPessoaJuridicaService() {
		return pessoaJuridicaService;
	}

	public void setPessoaJuridicaService(PessoaJuridicaService pessoaJuridicaService) {
		this.pessoaJuridicaService = pessoaJuridicaService;
	}
		
	public PessoaJuridica getPessoaJuridicaSelecionada() {
		return pessoaJuridicaSelecionada;
	}

	public void setPessoaJuridicaSelecionada(
			PessoaJuridica pessoaJuridicaSelecionada) {
		this.pessoaJuridicaSelecionada = pessoaJuridicaSelecionada;
	}

	public String getRazao() {
		return razao;
	}

	public void setRazao(String razao) {
		this.razao = razao;
	}


	public String getCnpj() {
		return cnpj;
	}


	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public List<SelectItem> getNaturezaJuridicaItens() {
		return naturezaJuridicaItens;
	}

	public void setNaturezaJuridicaItens(List<SelectItem> naturezaJuridicaItens) {
		this.naturezaJuridicaItens = naturezaJuridicaItens;
	}
	
	public DataModel<Cnae> getModelCnae() {
		return modelCnae;
	}

	public void setModelCnae(DataModel<Cnae> modelCnae) {
		this.modelCnae = modelCnae;
	}

	public Boolean getDisableAbasDependencias() {
		return disableAbasDependencias;
	}

	public void setDisableAbasDependencias(Boolean disableAbasDependencias) {
		this.disableAbasDependencias = disableAbasDependencias;
	}

	public Boolean getDisableForm() {
		return disableForm;
	}

	public void setDisableForm(Boolean disableForm) {
		this.disableForm = disableForm;
	}

	public int getTabAtual() {
		return tabAtual;
	}

	public void setTabAtual(int tabAtual) {
		// TAB Representante Legal
    	if(tabAtual == 3){
    		
    		RepresentanteLegalController repLegalController = (RepresentanteLegalController) SessaoUtil.recuperarManagedBean("#{representanteLegalController}", RepresentanteLegalController.class);
    		repLegalController.limparObjetos();
    		repLegalController.setDisableTabRepLegal(true);
    		repLegalController.setDisableTabDependenciasRepLegal(true);
    		repLegalController.setShowExpandirFormCadastro(true);
    		
    	} // TAB Endereço 
    	else if(tabAtual == 5){
    		
    		super.pessoa = pessoaJuridicaSelecionada.getPessoa();
    		super.prepararEnderecoGenericoController();
    		
    	}
    	
		this.tabAtual = tabAtual;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}
	
	public Boolean getVisualizaProximo() {
		if(tabAtual < abaMAX && !disableAbasDependencias) {
			visualizaProximo = true;
		}else {
			visualizaProximo = false;
		}
		return visualizaProximo;
	}

	public Boolean getVisualizaAnterior() {
		if(tabAtual>0) {
			visualizaAnterior = true;
		}else {
			visualizaAnterior = false;
		}
		return visualizaAnterior;
	}
	
	public void setVisualizaProximo(Boolean visualizaProximo) {
		this.visualizaProximo = visualizaProximo;
	}
	
	public void setVisualizaAnterior(Boolean visualizaAnterior) {
		this.visualizaAnterior = visualizaAnterior;
	}
	
	public StreamedContent getArquivoBaixar() {		
		if(!listaArquivo.isEmpty()) {		
			Exception erro =null;
			try {
				arquivoBaixar = gerenciaArquivoService.getContentFile(pessoaJuridicaSelecionada.getDscCaminhoArquivo());
			} catch (Exception e) {
				erro =e;
				JsfUtil.addErrorMessage("Arquivo não encontrado.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		return arquivoBaixar;
	}
	
	public void setArquivoBaixar(StreamedContent arquivoBaixar) {
		this.arquivoBaixar = arquivoBaixar;
	}

	public List<String> getListaArquivo() {
		return listaArquivo;
	}

	public void setListaArquivo(List<String> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}

	public Boolean getVisualizaFinalizar() {
		if(tabAtual == abaMAX){
			visualizaFinalizar = true;
		}
		else {
			visualizaFinalizar = false;
		}
		return visualizaFinalizar;
	}

	public void setVisualizaFinalizar(Boolean visualizaFinalizar) {
		this.visualizaFinalizar = visualizaFinalizar;
	}

	public Boolean getTemArquivo() {
		return temArquivo;
	}

	public void setTemArquivo(Boolean temArquivo) {
		this.temArquivo = temArquivo;
	}

	public Boolean getTelaIdentificacaoPapel() {
		return telaIdentificacaoPapel;
	}

	public void setTelaIdentificacaoPapel(Boolean telaIdentificacaoPapel) {
		this.telaIdentificacaoPapel = telaIdentificacaoPapel;
	}

	public Boolean getVisualizarDados() {
		return visualizarDados;
	}

	public void setVisualizarDados(Boolean visualizarDados) {
		this.visualizarDados = visualizarDados;
	}

	public Boolean getPessoaCadastrada() {
		return pessoaCadastrada;
	}

	public void setPessoaCadastrada(Boolean pessoaCadastrada) {
		this.pessoaCadastrada = pessoaCadastrada;
	}

	public Boolean getDisableFormVisualizar() {
		return disableFormVisualizar;
	}

	public void setDisableFormVisualizar(Boolean disableFormVisualizar) {
		this.disableFormVisualizar = disableFormVisualizar;
	}
	
	public void carregarLazyModelPessoaJuridica(){
		modelPessoasJuridicas = new LazyDataModel<PessoaJuridica>() {			
			@Override
			public List<PessoaJuridica> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, String> fields) {
				 List<PessoaJuridica> pessoasJuridicas = null;
				Exception erro = null;
				 try {
					setPageSize(pageSize); 
					pessoasJuridicas = populateList(first, pageSize);
				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage(e.getMessage());
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}				
				return pessoasJuridicas;
			}			
		};			
		
		//modelPessoasJuridicas.setRowCount(getRowCount());		
	}
	
	public void carregarLazyConsultaPessoaJuridica(){
		modelPessoasJuridicas = new LazyDataModel<PessoaJuridica>() {			
			@Override
			public List<PessoaJuridica> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, String> fields) {
				 List<PessoaJuridica> pessoasJuridicas = null;
				Exception erro = null;
				 try {
					setPageSize(pageSize); 
					pessoasJuridicas = populateList(first, pageSize);
				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage(e.getMessage());
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}				
				return pessoasJuridicas;
			}			
		};			
		
		modelPessoasJuridicas.setRowCount(getRowCount());		
	}
	
	protected List<PessoaJuridica> populateList(int first, int pageSize) {
		List<PessoaJuridica> pessoasJuridicas = new ArrayList<PessoaJuridica>();
		Exception erro = null;
		
		try {
			pessoasJuridicas = pessoaJuridicaService.listarPorCriteriaDemanda(pessoaJuridicaSelecionada, first, pageSize);						
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		return pessoasJuridicas;
    }
	
	protected int getRowCount(){
		int totalRowCount = 0;		
		Exception erro = null;
		try {
			totalRowCount = pessoaJuridicaService.count(pessoaJuridicaSelecionada);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			erro = e;
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		return totalRowCount;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}
	
	public String getUrlOrigem() {
		return urlOrigem;
	}

	public void setUrlOrigem(String urlOrigem) {
		SessaoUtil.adicionarObjetoSessao("URL_PROCURADOR_ORIGEM", urlOrigem);
		this.urlOrigem = urlOrigem;
	}

	public Boolean getdisableEdicaoRazaoSocial() {
		return disableEdicaoRazaoSocial;
	}

	public void setdisableEdicaoRazaoSocial(Boolean disableEdicaoRazaoSocial) {
		this.disableEdicaoRazaoSocial = disableEdicaoRazaoSocial;
	}

	public List<PessoaJuridicaHistorico> getListaPessoaJuridicaHistorico() {
		return listaPessoaJuridicaHistorico;
	}

	public void setListaPessoaJuridicaHistorico(List<PessoaJuridicaHistorico> listaPessoaJuridicaHistorico) {
		this.listaPessoaJuridicaHistorico = listaPessoaJuridicaHistorico;
	}

	public Boolean getUploadFile() {
		return uploadFileField;
	}

	public void setUploadFile(Boolean uploadFile) {
		this.uploadFileField = uploadFile;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}
	
	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}

}