package br.gov.ba.seia.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.OrgaoExpedidor;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaFisicaHistorico;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoIdentificacao;
import br.gov.ba.seia.entity.TipoTelefone;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.auditoria.FiltroAuditoria;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.TipoDocumentoEnum;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.facade.CadastroPessoaFisicaServiceFacade;
import br.gov.ba.seia.facade.PessoaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.OrgaoExpedidorService;
import br.gov.ba.seia.service.PessoaFisicaHistoricoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorPessoaFisicaService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoTelefoneService;
import br.gov.ba.seia.service.UsuarioService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.DataUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityController;

@Named("pessoaFisicaController")
@ViewScoped
@SuppressWarnings({ "unused", "serial" })
public class PessoaFisicaController extends PessoaEndereco {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");

	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private PessoaFacade pessoaFacade;
	
	@EJB
	private TipoTelefoneService tipoTelefoneService;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private DocumentoIdentificacaoService documentoIdenficacaoService;
	
	@EJB
	private OrgaoExpedidorService orgaoExpedidorService;
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private CadastroPessoaFisicaServiceFacade cadastroPessoaFisicaServiceFacade;
	
	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;
	
	@EJB
	private AreaService areaService;
	
	@EJB
	private PessoaFisicaHistoricoService pessoaFisicaHistoricoService;
	
	@EJB
	private LogradouroService logradouroService;
	private LazyDataModel<PessoaFisica> modelPessoas;
	private PessoaFisica pessoaSelecionada;
	private PessoaFisica pessoaPesquisa;
	private Boolean editMode;
	private Boolean enableFormPessoaFisica;
	private Boolean disableAbasDependencias;
	
	// --------- TELEFONE  ------ 
	private DataModel<Telefone> modelTelefone;
	private boolean editModeTelefone;
	private boolean newModeTelefone;
	private Telefone telefone;
	private Telefone telefoneNovo;
	private TipoTelefone tipoTelefone;
	private TipoTelefone tipoTelefoneNovo;
	private List<Telefone> listaTelefone;
	private List<TipoTelefone> listaTiposTelefone;
	private boolean flagValidate;
	private Boolean mostrarTableTelefone;
	// ----------------
	
	// --------- ENDERECO ------- 
	private String urlOrigem;
	// ----------------
	
	// --------- DOCUMENTOS  ------ 
	private DataModel<DocumentoIdentificacao> modelDocumentosIdentificacao;
	private DocumentoIdentificacao documentoIdentificacaoSelecionado;
	private List<SelectItem> orgaoExpedidorItens;
	private Boolean campoSerie;
	private Boolean editModeDocumento = false;
	private Boolean temArquivo;
	private List<DocumentoIdentificacao> listaArquivo;
	private List<DocumentoIdentificacao> listaArquivoParaAtualizar;
	private String caminhoArquivo;
	// ----------------
	
	private int tabAtual;
	private static Integer abaMAX = 4;	
	private Boolean visualizaProximo;
	private Boolean visualizaAnterior;
	private Boolean visualizaFinalizar;
	private StreamedContent arquivoBaixar;
	private boolean desabCombOrg = true;
	private Boolean showUfCidade = Boolean.FALSE;
	private Boolean telaIdentificacaoPapel;
	private Boolean visualizarDados;
	private Date dataAtual = new Date();
	private Date dataAmanha;
	private Boolean disableFormPessoaFisica;
	private Boolean cadastroIncompleto;
	private Boolean showFdbairro;
	private int refresh;
	
	//------------ PROCURADORES ---------------
	
	
	
	
	//------------ FORM PESSOA FISICA ---------------
	
	
	private Boolean disabledCpf = Boolean.FALSE;
	private Boolean disabledNome = Boolean.FALSE;
	private Boolean disabledCadastro = Boolean.FALSE;
	private Boolean disabledProprioUsuario = Boolean.FALSE;
	private Boolean disabledPerfil = Boolean.FALSE;
	private Boolean disabledEmail = Boolean.FALSE;
	
	//------------ HISTÓRICO DE PESSOA FÍSICA -------------
	
	PessoaFisicaHistoricoController pessoaFisicaHistoricoController;
	
	

	@Override 
	@PostConstruct
	public void init() {
		this.refresh = 0;
		cadastroIncompleto = false;
		verificarCadastroIncompleto();
		modelTelefone = new ListDataModel<Telefone>();
		pessoaPesquisa = new PessoaFisica();
		setPessoa(new Pessoa());
		editMode = Boolean.TRUE;
		telefone = new Telefone();
		tipoTelefone = new TipoTelefone();
		telefoneNovo = new Telefone();
		tipoTelefoneNovo = new TipoTelefone();
		enableFormPessoaFisica = Boolean.FALSE;
		disableFormPessoaFisica = ContextoUtil.getContexto().getVisualizarPessoaFisica();
		carregarItensOrgaoExpedidor();
		documentoIdentificacaoSelecionado = new DocumentoIdentificacao();
		setCampoSerie(Boolean.FALSE);
		setEditModeDocumento(Boolean.FALSE);
		disableAbasDependencias = Boolean.TRUE;
		tratarPessoaSelecionada();
		
		String lUrl = (String) SessaoUtil.recuperarObjetoSessao("URL_PROCURADOR_ORIGEM");
		
		if (Util.isNullOuVazio(lUrl)) {
			this.urlOrigem = "/paginas/manter-pessoafisica/consulta.xhtml";
		} else {
			this.tabAtual = 4;//Aba de Procurador
			this.urlOrigem = lUrl;
		}
		temArquivo = false;
		listaArquivo = new ArrayList<DocumentoIdentificacao>();
		telaIdentificacaoPapel = Boolean.FALSE;
		listaTelefone = new ArrayList<Telefone>();
		carregarListaTelefone();
		carregarLazyModelPessoaFisica();
		
	}
	
	public boolean isDisabledCpf() {
		return !Util.isNullOuVazio(pessoaSelecionada);
	}
	
	public boolean isRenderedBtnLimpar() {
		return Util.isNullOuVazio(pessoaSelecionada);
	}
	
	
	public boolean isUserExterno() {
		return ContextoUtil.getContexto().isUsuarioExterno();
	}
	
	
	public void validacaoUsuario() {
		if (isUserExterno() && Util.isNullOuVazio(pessoaSelecionada) && this.refresh == 0) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
				this.refresh++;
			} catch (Exception exception) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, exception);
				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		if (isUserExterno() && Util.isNullOuVazio(pessoaSelecionada)) {
			this.disableAbasDependencias = true;
			this.disableFormPessoaFisica = true;
		}

		if (!this.enableFormPessoaFisica || this.disableFormPessoaFisica) {
			this.disabledNome = Boolean.FALSE;
			this.disabledProprioUsuario = Boolean.FALSE;
			this.disabledPerfil = Boolean.TRUE;
			this.disabledEmail = Boolean.TRUE;
			this.disabledCadastro = Boolean.FALSE;
		

		} else {
			
			if (isExisteRequerimentoPorPF() || isCoordenadorAtendSelicComPermissao()) {
				this.disabledNome = Boolean.TRUE;
			}

			if (pessoaSelecionada.equals(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica())) {
				this.disabledCadastro = Boolean.TRUE;
				this.disabledProprioUsuario = Boolean.TRUE;
			}

			else if (isProcuradorPessoaFisica()) {
				this.disabledCadastro = Boolean.TRUE;
			}
			
			if (isPerfilBloqueado()) {
				this.disabledPerfil = Boolean.TRUE;
			}
			if (isEmailBloqueado()) {
				this.disabledEmail = Boolean.TRUE;
			}
		}
 	}
	
	
	public void excluirPessoaFisica() {
		try {
			pessoaFisicaService.excluirPessoaFisica(pessoaSelecionada);
			limparObjetos();
			carregarLazyModelPessoaFisica();
			JsfUtil.addSuccessMessage("Exclusão efetuada com sucesso!");
		} catch (AppExceptionError exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			limparObjetos();
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
			limparObjetos();
		}
	}

	/**
	 * Método chamado no filtro da lista. Ele filtra e atualiza a lista que está na sessão.
	 * 
	 * @return
	 */
	public String consultarPessoaFisica() {
		try {
			carregarLazyConsultaPessoaFisica();
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
		return "";
	}

	public String prepararParaEditar() {
		ContextoUtil.getContexto().setPessoaFisica(pessoaSelecionada);
		ContextoUtil.getContexto().setVisualizarPessoaFisica(Boolean.FALSE);
		disableAbasDependencias = Boolean.TRUE;

		return "/paginas/manter-pessoafisica/cadastro.xhtml?isEdit=true";
	}

	public String prepararParaVisualizar() {
		ContextoUtil.getContexto().setPessoaFisica(pessoaSelecionada);
		ContextoUtil.getContexto().setVisualizarPessoaFisica(Boolean.TRUE);
		disableAbasDependencias = Boolean.FALSE;
		enableFormPessoaFisica = Boolean.FALSE;
		disableFormPessoaFisica = Boolean.TRUE;
		showUfCidade = Boolean.FALSE;
		disabledPerfil = Boolean.TRUE;
		disabledEmail = Boolean.TRUE;
		disabledCadastro = Boolean.TRUE;
		return "/paginas/manter-pessoafisica/cadastro.xhtml?isEdit=true";
	}

	public Boolean getDisabledCpf() {
		return disabledCpf;
	}

	public void setDisabledCpf(Boolean disabledCpf) {
		this.disabledCpf = disabledCpf;
	}

	public Boolean getDisabledNome() {
		return disabledNome;
	}

	public void setDisabledNome(Boolean disabledNome) {
		this.disabledNome = disabledNome;
	}

	public Boolean getDisabledCadastro() {
		return disabledCadastro;
	}

	public void setDisabledCadastro(Boolean disabledCadastro) {
		this.disabledCadastro = disabledCadastro;
	}

	public Boolean getDisabledProprioUsuario() {
		return disabledProprioUsuario;
	}

	public void setDisabledProprioUsuario(Boolean disabledProprioUsuario) {
		this.disabledProprioUsuario = disabledProprioUsuario;
	}

	public void consultarPessoaFisicaPorCpf() {
		try {
			this.pessoaSelecionada = pessoaFisicaService.filtrarPessoaFisicaByCpf(pessoaPesquisa);
			
			if (!Util.isNull(pessoaSelecionada)) {
				super.setPessoa(this.pessoaSelecionada.getPessoa());
				carregarListaTelefone();
				carregarModelDocumentoIdentificacao();
				disableAbasDependencias = Boolean.FALSE;
			} 
			else {
				super.setPessoa(new Pessoa());
				pessoaSelecionada = new PessoaFisica();
				disableAbasDependencias = Boolean.TRUE;
			}
			
			enableFormPessoaFisica = Boolean.TRUE;
			disableFormPessoaFisica = Boolean.FALSE;
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	} 

	public void prepararParaEdicao(ActionEvent event) {
		Pessoa pessoa = this.pessoaSelecionada.getPessoa();
		ContextoUtil.getContexto().setPessoa(pessoa);
	}

	private void verificarCadastroIncompleto() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String cadastroIncompletoString = req.getParameter("cadastroIncompleto");
		cadastroIncompleto = new Boolean(cadastroIncompletoString);
		ContextoUtil.getContexto().setCadastroInCompleto(cadastroIncompleto);
		
		if (!Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado()) && cadastroIncompleto) {
			
			ContextoUtil.getContexto().setPessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
			
			if(ContextoUtil.getContexto().isUsuarioExterno()) {
				JsfUtil.addWarnMessage("Caso queira acessar a consulta pública, clique no link ao lado 'Consulta pública'. Senão, favor complementar o cadastro.");
			}
		} else {
			ContextoUtil.getContexto().setCadastroInCompleto(false);
		}
	}
	
	private void tratarPessoaSelecionada() {
		pessoaSelecionada = ContextoUtil.getContexto().getPessoaFisica();
		if (Util.isNullOuVazio(pessoaSelecionada)) {
			pessoaSelecionada = new PessoaFisica();
			setPessoa(new Pessoa());
		} else {
			pessoaPesquisa = new PessoaFisica();
			pessoaPesquisa.setNumCpf(pessoaSelecionada.getNumCpf());
			try {
				pessoaSelecionada.setUsuario(usuarioService.carregar(new Usuario(pessoaSelecionada.getIdePessoaFisica())));
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro a carregar usuário");
			} 
			setPessoa(pessoaSelecionada.getPessoa());
			this.enableFormPessoaFisica = Boolean.TRUE;
			carregarListaTelefone();
			carregarModelDocumentoIdentificacao();
			this.disableAbasDependencias = Boolean.FALSE;
		}
	}

	/**
	 * M�todo que retorna a lista que ser� exibida no datatable da p�gina. Verifica a sess�o primeiro, caso n�o exista nenhuma lista na sess�o, busca todos os registros no banco e atualiza a lista.
	 * 
	 * @return
	 */
	public LazyDataModel<PessoaFisica> getModelPessoas() {
		return modelPessoas;
	}

	public void salvarPessoa() {
		
		if(validarSalvarPessoa()){
			try {
				String msgSucesso = BUNDLE.getString("geral_msg_alteracao_sucesso");
				if (Util.isNull(getPessoa().getIdePessoa())) {
					getPessoa().setDtcCriacao(new Date());
					getPessoa().setDtcExclusao(null);
					getPessoa().setIndExcluido(Boolean.FALSE);
					msgSucesso = BUNDLE.getString("geral_msg_inclusao_sucesso");
				}
				pessoaSelecionada.setNumCpf(pessoaPesquisa.getNumCpf());
				pessoaSelecionada.setPessoa(getPessoa());
				pessoaSelecionada.setTipSexo(null);
				getPessoa().setPessoaFisica(pessoaSelecionada);
				
				pessoaPesquisa = pessoaFisicaService.buscarPessoaFisicaByCPF(pessoaPesquisa);
				
					
				if(!Util.isNullOuVazio(pessoaPesquisa) && !pessoaPesquisa.getNomPessoa().equals(pessoaSelecionada.getNomPessoa()) || 
						!Util.isNullOuVazio(pessoaPesquisa) && !pessoaPesquisa.getPessoa().getDesEmail().equals(pessoaSelecionada.getPessoa().getDesEmail())) {
					
					pessoaFisicaService.salvarOuAtualizarPessoaFisica(pessoaSelecionada);
					pessoaFisicaHistoricoService.salvarHistoricoPessoaFisica(preencherHistoricoPessoaFisica());
					
				} else if(!Util.isNullOuVazio(pessoaSelecionada.getIdePessoaFisica()) && !Util.isNullOuVazio(pessoaFisicaHistoricoService.buscarHistoricoAnterior(pessoaSelecionada))){
					pessoaFisicaService.salvarOuAtualizarPessoaFisica(pessoaSelecionada);
				} else {
					pessoaFisicaService.salvarOuAtualizarPessoaFisica(pessoaSelecionada);
					pessoaFisicaHistoricoService.salvarHistoricoPessoaFisica(preencherHistoricoNovaPessoaFisica());
				}
				
				

				disableAbasDependencias = Boolean.FALSE;
				JsfUtil.addSuccessMessage(msgSucesso);
			} catch (Exception e) {
				if(e.getCause() instanceof ConstraintViolationException) {
					MensagemUtil.erro("Este CPF já foi cadastrado. Favor entrar em contato com o atendimento.");
				}else{
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}
		}
	}
	
	public PessoaFisicaHistorico preencherHistoricoPessoaFisica() {
		PessoaFisicaHistorico pessoaFisicaHistorico = new PessoaFisicaHistorico();
		
		pessoaFisicaHistorico.setIdePessoaFisica(pessoaSelecionada);
		pessoaFisicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaFisicaHistorico.setDtcHistorico(new Date());
		
		if(!pessoaPesquisa.getNomPessoa().equals(pessoaSelecionada.getNomPessoa())){
			pessoaFisicaHistorico.setNomPessoa(pessoaSelecionada.getNomPessoa());
		}
		
		if(!pessoaPesquisa.getPessoa().getDesEmail().equals(pessoaSelecionada.getPessoa().getDesEmail())){
			pessoaFisicaHistorico.setDesEmail(pessoaSelecionada.getPessoa().getDesEmail());
		}
		
		return pessoaFisicaHistorico;
	}
	
	public PessoaFisicaHistorico preencherHistoricoNovaPessoaFisica() {
		PessoaFisicaHistorico pessoaFisicaHistorico = new PessoaFisicaHistorico();
		
		pessoaFisicaHistorico.setIdePessoaFisica(pessoaSelecionada);
		pessoaFisicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaFisicaHistorico.setDtcHistorico(new Date());
		pessoaFisicaHistorico.setNomPessoa(pessoaSelecionada.getNomPessoa());
		pessoaFisicaHistorico.setDesEmail(pessoaSelecionada.getPessoa().getDesEmail());
		
		return pessoaFisicaHistorico;
	}

	public Boolean validarSalvarPessoa(){
		if(pessoaSelecionada.getNomPessoa().trim().equals("")){
			MensagemUtil.erro("O campo  * Nome não deve conter apenas espaços vazios");
			return false;
		}
		
		return true;
	}
	
	public void limparObjetos() {
		this.pessoaSelecionada = new PessoaFisica();
		this.setPessoa(new Pessoa());
		
		IdentificarPapelController identificarPapelController = (IdentificarPapelController) SessaoUtil.recuperarManagedBean("#{identificarPapelController}", IdentificarPapelController.class); 
		identificarPapelController.setPessoaFisica(pessoaSelecionada); 
	}

	public String limparObjetosPessoaFisicaAction() {
		limparObjetos();
		pessoaPesquisa = new PessoaFisica();
		setEnableFormPessoaFisica(Boolean.FALSE);
		modelTelefone = null;
		modelDocumentosIdentificacao = null;
		return null;
	}

	public void carregarTelefones() {
		this.modelTelefone = new ListDataModel<Telefone>((List<Telefone>) this.getPessoa().getTelefoneCollection());
	}

	public PessoaFisica getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(PessoaFisica pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}

	//------- TELEFONE  ------ // 
	public void carregarListaTiposTelefone() {
		Exception erro = null;
		try {
			listaTiposTelefone = (List<TipoTelefone>) tipoTelefoneService.listarTipoTelefone();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public Collection<SelectItem> getValuesComboBoxTipoTelefone() {
		carregarListaTiposTelefone();
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		Iterator<TipoTelefone> i = listaTiposTelefone.iterator();
		while (i.hasNext()) {
			TipoTelefone tipoTelefone = i.next();
			toReturn.add(new SelectItem(tipoTelefone, tipoTelefone.getNomTipoTelefone()));
		}
		return toReturn;
	}

	private void carregarListaTelefone() {
		List<Pessoa> listaPessoa = new ArrayList<Pessoa>();
		listaPessoa.add(getPessoa());
		Telefone telefoneFiltro = new Telefone();
		telefoneFiltro.setPessoaCollection(listaPessoa);
		Exception erro = null;
		try {
			modelTelefone = null;
			if (!Util.isNull(pessoa.getIdePessoa())) {
				listaTelefone = telefoneService.buscarTelefonesPorPessoa(pessoa);
				modelTelefone = Util.castListToDataModel(listaTelefone);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void adicionaTelefone() {
		final Integer ideTelefoneEditado = telefone.getIdeTelefone();
		try {
			Telefone telefonePersist = null;
			String msgSucesso = null;
			if (newModeTelefone) {
				telefoneNovo.setIdeTipoTelefone(tipoTelefoneNovo);
				telefonePersist = telefoneNovo;
				msgSucesso = BUNDLE.getString("geral_msg_inclusao_sucesso");
			} else {
				telefone.setIdeTipoTelefone(tipoTelefone);
				telefonePersist = telefone;
				msgSucesso = BUNDLE.getString("geral_msg_alteracao_sucesso");
			}
			adicionaTelefonePessoa();
			if (validate()) {
				telefonePersist.setIdeTelefone(ideTelefoneEditado);
				telefoneService.salvarTelefone(telefonePersist);
				if (!Util.isNull(telefonePersist.getIdeTelefone()) && !editModeTelefone) {
					listaTelefone.add(telefonePersist);
				}
				flagValidate = true;
				JsfUtil.addSuccessMessage(msgSucesso);
				limparTelefone();
			} else {
				flagValidate = false;
			}
			Html.adicionarCallBack("flagValidate", flagValidate);
			
			carregarListaTelefone();
			this.limparTelefone();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void limparTelefone() {
		telefone = new Telefone();
		tipoTelefone = new TipoTelefone();
		telefoneNovo = new Telefone();
		tipoTelefoneNovo = new TipoTelefone();
	}

	public String limparTelefoneAction() {
		limparTelefone();
		return null;
	}

	private Boolean validate() {
		List<Telefone> listaAtualizadaBanco = new ArrayList<Telefone>();
		Exception erro = null;
		try {
			listaAtualizadaBanco = telefoneService.filtrarTelefonesNaoExcluidosPorPessoa(pessoa);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
		Boolean retorno = Boolean.TRUE;
		Telefone telefoneValidate = null;
		if (newModeTelefone) {
			telefoneValidate = telefoneNovo;
		} else {
			telefoneValidate = telefone;
		}
		if (Util.isNull(telefoneValidate.getNumTelefone()) || Util.isEmptyString(telefoneValidate.getNumTelefone())) {
			retorno = Boolean.FALSE;
			JsfUtil.addErrorMessage("O campo Número é de preenchimento obrigatório.");
		}
		if (Util.isNull(telefoneValidate.getIdeTipoTelefone()) || Util.isEmptyString(telefoneValidate.getIdeTipoTelefone().getNomTipoTelefone())) {
			retorno = Boolean.FALSE;
			JsfUtil.addErrorMessage("O campo Tipo é de preenchimento obrigatório.");
		}
		telefoneValidate.setIdeTelefone(null);
		if (Util.isObjectInList(telefoneValidate, listaAtualizadaBanco)) {
			retorno = Boolean.FALSE;
			JsfUtil.addErrorMessage("O Tipo de telefone e o Número já foram inseridos.");
		}
		return retorno;
	}

	private void adicionaTelefonePessoa() {
		List<Pessoa> lista = new ArrayList<Pessoa>();
		lista.add(getPessoa());
		if (newModeTelefone) {
			telefoneNovo.setPessoaCollection(lista);
		} else {
			telefone.setPessoaCollection(lista);
		}
	}

	public void removerTelefone() {
		Exception erro = null;
		try {
			telefoneService.excluirTelefone(telefone);
			listaTelefone.remove(telefone);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public boolean isDisableBotoesSalvarLimpar() {
		
		if(Util.isNull(disableFormPessoaFisica)) {
			return !enableFormPessoaFisica;
		}
		return !enableFormPessoaFisica || disableFormPessoaFisica;
	}

	public void limpar() {
		telefone = new Telefone();
		tipoTelefone = new TipoTelefone();
	}

	public String limparObjetosDocumento() {
		
		documentoIdentificacaoSelecionado = new DocumentoIdentificacao();
		
		setCampoSerie(Boolean.FALSE);
		temArquivo = false;
		listaArquivo.clear();
		caminhoArquivo = "";
		this.desabCombOrg = true;
		return null;
	}

	@Override
	public void enviarId() {
		super.enviarId("tabviewpf");
	}

	@Override
	public void desabilitarParaVisualizacao() {
		if((!Util.isNull(enableFormPessoaFisica) && !enableFormPessoaFisica) || (!Util.isNull(disableFormPessoaFisica) && disableFormPessoaFisica)){
			super.enderecoGenericoController.setVisualizacao(true);
		} 
		else {
			super.enderecoGenericoController.setVisualizacao(false);
		}
	}
	
	public void salvarDocumento() {
		try {
			cadastroPessoaFisicaServiceFacade.salvarDocumento(this);
			carregarModelDocumentoIdentificacao();	
			if(getTemArquivo()){
				
				limparObjetosDocumento();
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível salvar o documento.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void listenerValor(ValueChangeEvent event) {
		TipoIdentificacao tipoIdentificacao = (TipoIdentificacao) event.getNewValue();
		if (tipoIdentificacao.getIdeTipoIdentificacao().compareTo(TipoDocumentoEnum.CTPS.getValue()) == 0) {
			this.campoSerie = Boolean.TRUE;
		} else {
			this.campoSerie = Boolean.FALSE;
		}
	}

	public void verificarNumSerie() {
		campoSerie= 
			((!Util.isNull(documentoIdentificacaoSelecionado.getIdeTipoIdentificacao())) && 
			!Util.isNull(documentoIdentificacaoSelecionado.getIdeTipoIdentificacao().getIdeTipoIdentificacao()) && 
			documentoIdentificacaoSelecionado.getIdeTipoIdentificacao().getIdeTipoIdentificacao().equals(TipoDocumentoEnum.CTPS.getValue()));
	}

	public void carregarModelDocumentoIdentificacao() {
		try {
			if(getPessoa()!=null){
				this.modelDocumentosIdentificacao = new ListDataModel<DocumentoIdentificacao>(documentoIdenficacaoService.listarDocumentosIdentificacaoPorPessoa(getPessoa()));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarItensOrgaoExpedidor() {
		Exception erro = null;
		try {
			this.orgaoExpedidorItens = new ArrayList<SelectItem>();
			
			for (OrgaoExpedidor orgaoExpedidor : orgaoExpedidorService.listarOrgaoExpedidor(
					(documentoIdentificacaoSelecionado != null && documentoIdentificacaoSelecionado.getIdeTipoIdentificacao() != null) 
							? documentoIdentificacaoSelecionado.getIdeTipoIdentificacao() : null)) {
				
				this.orgaoExpedidorItens.add(new SelectItem(orgaoExpedidor, orgaoExpedidor.getDscOrgaoExpedidor()));
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void removerDocumento() {
		try {
			documentoIdentificacaoSelecionado.setDtcExclusao(new Date());
			documentoIdentificacaoSelecionado.setIndExcluido(Boolean.TRUE);
			documentoIdenficacaoService.excluirDocumentoIdentificacao(this.documentoIdentificacaoSelecionado);
			
			documentoIdentificacaoSelecionado = new DocumentoIdentificacao();
			
			carregarModelDocumentoIdentificacao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void trataArquivo(FileUploadEvent event) {
		
		try {
			temArquivo = true;
			listaArquivo = new ArrayList<DocumentoIdentificacao>();
			listaArquivoParaAtualizar = new ArrayList<DocumentoIdentificacao>();
			
			DocumentoIdentificacao diNovo = null;
			if(Util.isNullOuVazio(documentoIdentificacaoSelecionado)) {
				diNovo = new DocumentoIdentificacao();
			}
			else {
				listaArquivoParaAtualizar.add(documentoIdentificacaoSelecionado);
				diNovo = documentoIdentificacaoSelecionado.clone();
			}
			diNovo.setIdeDocumentoIdentificacao(null);
			diNovo.setDtcCriacao(new Date());
			diNovo.setIndExcluido(false);
			diNovo.setDscCaminhoArquivo("");
			diNovo.setArquivoEnviado(event.getFile());
			diNovo.setDocumentoIdentificacaoRequerimentoCollection(null);
			
			listaArquivo.add(diNovo);			
			documentoIdentificacaoSelecionado = diNovo; 
			
			Html.atualizar("tabviewpf:formdocumentos:arquivoHidden");
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		}		
	}

	// ----------------
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
	}

	public void incrementaIndexAba() {
		setTabAtual(this.tabAtual + 1);
	}

	public void decrementaIndexAba() {
		setTabAtual(this.tabAtual - 1);
	}

	public String finalizar() {
		if (cadastroIncompleto) {
			Exception erro = null;
			try {
				if (pessoaFacade.verificarCadastroCompletoPessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa())) {
					cadastroIncompleto = false;
					FacesContext.getCurrentInstance().getExternalContext().redirect("/home.xhtml");
					return "/";
				} else {
					FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-pessoafisica/cadastro.xhtml?cadastroIncompleto=true");
					return "/paginas/manter-pessoafisica/cadastro.xhtml?cadastroIncompleto=true";
				}
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		limparSessaoPessoa();
		return urlOrigem;
	}
	

	public void limparSessaoPessoa() {
		ContextoUtil.getContexto().setPessoaFisica(new PessoaFisica());
	}

	public void removerPessoaFisicaDaSessao() {
		ContextoUtil.getContexto().setPessoaFisica(null);
		ContextoUtil.getContexto().setVisualizarPessoaFisica(Boolean.FALSE);
	}
	
//	#6655 - Botao limpar nao renderiza mais na edicao/visualizacao
	public boolean isDocumentoNovo() {
		return Util.isNullOuVazio(documentoIdentificacaoSelecionado);
	}
	
	public boolean isVerificaEdicaoNome() {
		
		if(enableFormPessoaFisica != null && disableFormPessoaFisica != null 
				&& (!enableFormPessoaFisica || disableFormPessoaFisica)) {
			return true;
		}
		
		if(isExisteRequerimentoPorPF() && !isCoordenadorAtendSelicComPermissao()) {
			return true;
		}
	
		return false;
	}
	
	public boolean isCoordenadorAtendSelicComPermissao() {
		//TICKET #7208
		
		try {
			SecurityController security = (SecurityController) SessaoUtil.recuperarManagedBean("#{security}", SecurityController.class);
			Usuario usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
			
			//VERIFICA PERMISSAO PARA EDITAR RAZAO SOCIAL
			if (security != null && security.temAcesso("3.12.61")) {
				
				//VERIFICA SE O USUARIO EH COORDENADOR
				if(usuarioLogado.getIdePessoaFisica() != null && usuarioLogado != null && usuarioLogado.isPerfilCoordenador()) {
					
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
	
	public boolean isExisteRequerimentoPorPF() {
		try {
			return pessoaFisicaService.existeRequerimentoPorPF(pessoaSelecionada);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public DataModel<Telefone> getModelTelefone() {
		return modelTelefone;
	}

	public void setModelTelefone(DataModel<Telefone> modelTelefone) {
		this.modelTelefone = modelTelefone;
	}

	public DataModel<DocumentoIdentificacao> getModelDocumentosIdentificacao() {
		return modelDocumentosIdentificacao;
	}

	public void setModelDocumentosIdentificacao(DataModel<DocumentoIdentificacao> modelDocumentosIdentificacao) {
		this.modelDocumentosIdentificacao = modelDocumentosIdentificacao;
	}

	public PessoaFisica getPessoaPesquisa() {
		return pessoaPesquisa;
	}

	public void setPessoaPesquisa(PessoaFisica pessoaPesquisa) {
		this.pessoaPesquisa = pessoaPesquisa;
	}

	public boolean isEditModeTelefone() {
		return editModeTelefone;
	}

	public void setEditModeTelefone(boolean editModeTelefone) {
		this.editModeTelefone = editModeTelefone;
	}

	public boolean isNewModeTelefone() {
		return newModeTelefone;
	}

	public void setNewModeTelefone(boolean newModeTelefone) {
		this.newModeTelefone = newModeTelefone;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Telefone getTelefoneNovo() {
		return telefoneNovo;
	}

	public void setTelefoneNovo(Telefone telefoneNovo) {
		this.telefoneNovo = telefoneNovo;
	}

	public TipoTelefone getTipoTelefone() {
		return tipoTelefone;
	}

	public void setTipoTelefone(TipoTelefone tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	public TipoTelefone getTipoTelefoneNovo() {
		return tipoTelefoneNovo;
	}

	public void setTipoTelefoneNovo(TipoTelefone tipoTelefoneNovo) {
		this.tipoTelefoneNovo = tipoTelefoneNovo;
	}

	public List<Telefone> getListaTelefone() {
		return listaTelefone;
	}

	public void setListaTelefone(List<Telefone> listaTelefone) {
		this.listaTelefone = listaTelefone;
	}

	public List<TipoTelefone> getListaTiposTelefone() {
		return listaTiposTelefone;
	}

	public void setListaTiposTelefone(List<TipoTelefone> listaTiposTelefone) {
		this.listaTiposTelefone = listaTiposTelefone;
	}

	public boolean isFlagValidate() {
		return flagValidate;
	}

	public void setFlagValidate(boolean flagValidate) {
		this.flagValidate = flagValidate;
	}

	public Boolean getEnableFormPessoaFisica() {
		return enableFormPessoaFisica;
	}

	public void setEnableFormPessoaFisica(Boolean enableFormPessoaFisica) {
		this.enableFormPessoaFisica = enableFormPessoaFisica;
	}

	public DocumentoIdentificacao getDocumentoIdentificacaoSelecionado() {
		return documentoIdentificacaoSelecionado;
	}

	public void setDocumentoIdentificacaoSelecionado(DocumentoIdentificacao documentoIdentificacaoSelecionado) {
		this.documentoIdentificacaoSelecionado = documentoIdentificacaoSelecionado;
	}

	public List<SelectItem> getOrgaoExpedidorItens() {
		return orgaoExpedidorItens;
	}

	public void setOrgaoExpedidorItens(List<SelectItem> orgaoExpedidorItens) {
		this.orgaoExpedidorItens = orgaoExpedidorItens;
	}

	public Boolean getCampoSerie() {
		return campoSerie;
	}

	public void setCampoSerie(Boolean campoSerie) {
		this.campoSerie = campoSerie;
	}

	public Boolean getEditModeDocumento() {
		return editModeDocumento;
	}

	public void setEditModeDocumento(Boolean editModeDocumento) {
		verificarNumSerie();
		if (!Util.isNullOuVazio(documentoIdentificacaoSelecionado.getIdeDocumentoIdentificacao())) {
			listaArquivo.clear();
			listaArquivo.add(documentoIdentificacaoSelecionado);
			caminhoArquivo = documentoIdentificacaoSelecionado.getDscCaminhoArquivo();
			if (!Util.isNullOuVazio(documentoIdentificacaoSelecionado.getDscCaminhoArquivo())) {
				temArquivo = true;
			}
		}
		this.editModeDocumento = editModeDocumento;
	}

	public Boolean getDisableAbasDependencias() {
		return disableAbasDependencias;
	}

	public void setDisableAbasDependencias(Boolean disableAbasDependencias) {
		this.disableAbasDependencias = disableAbasDependencias;
	}

	public int getTabAtual() {
		return tabAtual;
	}

	public void setTabAtual(int tabAtual) {
		this.tabAtual = tabAtual;
		if(tabAtual == 3) {
			super.prepararEnderecoGenericoController();
		} 
		else if(tabAtual == 4){
			ProcuradorPessoaFisicaController procPFcontroller = (ProcuradorPessoaFisicaController) SessaoUtil.recuperarManagedBean("#{procuradorPessoaFisicaController}", ProcuradorPessoaFisicaController.class);
			procPFcontroller.limparObjetos();
			
			Html.atualizar("tabviewpf:paneltabviewprocpf");
		}
	}

	public Boolean getVisualizaProximo() {
		if (tabAtual < abaMAX) {
			visualizaProximo = true;
		} else {
			visualizaProximo = false;
		}
		return visualizaProximo;
	}

	public void setVisualizaProximo(Boolean visualizaProximo) {
		this.visualizaProximo = visualizaProximo;
	}

	public Boolean getVisualizaAnterior() {
		if (tabAtual > 0) {
			visualizaAnterior = true;
		} else {
			visualizaAnterior = false;
		}
		return visualizaAnterior;
	}

	public void setVisualizaAnterior(Boolean visualizaAnterior) {
		this.visualizaAnterior = visualizaAnterior;
	}

	public StreamedContent getArquivoBaixar() {
		if (!listaArquivo.isEmpty()) {
			try {
				if(documentoIdentificacaoSelecionado.getDscCaminhoArquivo().isEmpty()){
					InputStream stream = documentoIdentificacaoSelecionado.getArquivoEnviado().getInputstream();
					String contentType = documentoIdentificacaoSelecionado.getArquivoEnviado().getContentType();
					String fileName = documentoIdentificacaoSelecionado.getArquivoEnviado().getFileName();
					arquivoBaixar = new DefaultStreamedContent(stream, contentType, fileName);
				}
				else {
					arquivoBaixar = gerenciaArquivoService.getContentFile(documentoIdentificacaoSelecionado.getDscCaminhoArquivo());
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				JsfUtil.addErrorMessage("Arquivo não encontrado.");
			}
		}
		return arquivoBaixar;
	}

	public void setArquivoBaixar(StreamedContent arquivoBaixar) {
		this.arquivoBaixar = arquivoBaixar;
	}

	public EnderecoPessoa getEnderecoPessoa() {
		return enderecoPessoa;
	}

	public void setEnderecoPessoa(EnderecoPessoa enderecoPessoa) {
		this.enderecoPessoa = enderecoPessoa;
	}

	public Boolean getVisualizaFinalizar() {
		if (tabAtual == abaMAX) {
			visualizaFinalizar = true;
		} else {
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

	public Boolean getMostrarTableTelefone() {
		if (!Util.isNullOuVazio(modelTelefone) && modelTelefone.getRowCount() > 0) {
			mostrarTableTelefone = Boolean.TRUE;
		} else {
			mostrarTableTelefone = Boolean.FALSE;
		}
		return mostrarTableTelefone;
	}

	public void setMostrarTableTelefone(Boolean mostrarTableTelefone) {
		this.mostrarTableTelefone = mostrarTableTelefone;
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

	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}

	public Boolean getDisableFormPessoaFisica() {
		return disableFormPessoaFisica;
	}

	public void setDisableFormPessoaFisica(Boolean disableFormPessoaFisica) {
		this.disableFormPessoaFisica = disableFormPessoaFisica;
	}

	public void carregarLazyModelPessoaFisica() {
		modelPessoas = new LazyDataModel<PessoaFisica>() {

			@Override
			public List<PessoaFisica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<PessoaFisica> pessoasFisicas = null;
				try {
					setPageSize(pageSize);
					pessoasFisicas = populateList(first, pageSize);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					JsfUtil.addErrorMessage(e.getMessage());
				}
				return pessoasFisicas;
			}
		};
	
	}
	
	public void carregarLazyConsultaPessoaFisica() {
		modelPessoas = new LazyDataModel<PessoaFisica>() {

			@Override
			public List<PessoaFisica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<PessoaFisica> pessoasFisicas = null;
				try {
					setPageSize(pageSize);
					pessoasFisicas = populateList(first, pageSize);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					JsfUtil.addErrorMessage(e.getMessage());
				}
				return pessoasFisicas;
			}
		};
		modelPessoas.setRowCount(getRowCount());
	}

	protected List<PessoaFisica> populateList(int first, int pageSize) {
		List<PessoaFisica> pessoasFisicas = new ArrayList<PessoaFisica>();
		try {
			pessoasFisicas = pessoaFisicaService.listarPorCriteriaDemanda(pessoaSelecionada, first, pageSize);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return pessoasFisicas;
	}

	protected int getRowCount() {
		int totalRowCount = 0;
		try {
			totalRowCount = pessoaFisicaService.count(pessoaSelecionada);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return totalRowCount;
	}

	public Date getDataAmanha() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		return dataAmanha = cal.getTime();
	}

	public void carregarComboOrgaoExp(Boolean desabilitaCombo) {
		carregarItensOrgaoExpedidor();
		desabCombOrg = desabilitaCombo;
	}
	
	public boolean isRenderedEditar(PessoaFisica pessoaFisica) {

		if (!ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno()) {

			return true;
			
		}

		if (ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica()
				.equals(pessoaFisica)) {
			return true;
		}

		return false;

	}

	public void setDataAmanha(Date dataAmanha) {
		this.dataAmanha = dataAmanha;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public boolean isDesabCombOrg() {
		return desabCombOrg;
	}

	public void setDesabCombOrg(boolean desabCombOrg) {
		this.desabCombOrg = desabCombOrg;
	}

	public Boolean getCadastroIncompleto() {
		return cadastroIncompleto;
	}

	public void setCadastroIncompleto(Boolean cadastroIncompleto) {
		this.cadastroIncompleto = cadastroIncompleto;
	}

	public String getUrlOrigem() {
		return urlOrigem;
	}

	public void setUrlOrigem(String urlOrigem) {
		SessaoUtil.adicionarObjetoSessao("URL_PROCURADOR_ORIGEM", urlOrigem);
		this.urlOrigem = urlOrigem;
	}

	public Boolean getShowUfCidade() {
		return showUfCidade;
	}

	public void setShowUfCidade(Boolean showUfCidade) {
		this.showUfCidade = showUfCidade;
	}

	public Boolean getShowFdbairro() {
		return showFdbairro;
	}

	public void setShowFdbairro(Boolean showFdbairro) {
		this.showFdbairro = showFdbairro;
	}
	
	public List<DocumentoIdentificacao> getListaArquivo() {
		return listaArquivo;
	}

	public void setListaArquivo(List<DocumentoIdentificacao> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}

	public List<DocumentoIdentificacao> getListaArquivoParaAtualizar() {
		return listaArquivoParaAtualizar;
	}

	public void setListaArquivoParaAtualizar(List<DocumentoIdentificacao> listaArquivoParaAtualizar) {
		this.listaArquivoParaAtualizar = listaArquivoParaAtualizar;
	}
	
	public Boolean isProcuradorPessoaFisica() {
		try {
			PessoaFisica procuradorPessoaFisica = new PessoaFisica();
			PessoaFisica requerente = new PessoaFisica();
			procuradorPessoaFisica.setIdePessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica());
			requerente.setIdePessoaFisica(pessoaSelecionada.getIdePessoaFisica());
			return procuradorPessoaFisicaService.isProcurarPessoaFisica(requerente, procuradorPessoaFisica);
		}
		catch (Exception e) {
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		
		
		
	}
	
	
	public Boolean isRequerente() {
		try {
			Usuario usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
			PessoaFisica requerente = new PessoaFisica();
			requerente.setIdePessoaFisica(pessoaSelecionada.getIdePessoaFisica());
				if(usuarioLogado.getIdePessoaFisica().equals(requerente.getIdePessoaFisica())) {
					return true;
				}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	private boolean isEmailBloqueado() {
		 try {
		        if (isProcuradorPessoaFisica() || isRequerente()) {
		            return true;
		        } else {
		        	int perfilUsuario = ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil();
			        int[] perfis = {1,5,34,35};
		 
			        for (int i = 0; i < perfis.length; i++) {
			            if (perfis[i] == perfilUsuario) {
			                return false;
			            }
			        }
		        }
		    } catch (Exception e) {
		        throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		    }
		 return true;
	}
	
	public boolean isPerfilBloqueado() {
		try {
			int perfilUsuario = ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil();
	        int[] perfis = {1,5,34,35};
 
	        for (int i = 0; i < perfis.length; i++) {
	            if (perfis[i] == perfilUsuario) {
	                return false;
	            }
	        }
	        
	        
	        if (isProcuradorPessoaFisica()) {
	        	return false;
	        }
	        
	        if (isRequerente()) {
	        	return false;
	        }
	        return true;
		}
		catch (Exception e) {
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}	
	
	public Boolean getDisabledPerfil() {
		return disabledPerfil;
	}
	
	public Boolean getDisabledEmail() {
		return disabledEmail;
	}

}
