package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.OrgaoExpedidor;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.ResponsavelImovelRural;
import br.gov.ba.seia.entity.TipoIdentificacao;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.TipoDocumentoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.DocumentoImovelRuralService;
import br.gov.ba.seia.service.EstadoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.OrgaoExpedidorService;
import br.gov.ba.seia.service.ResponsavelImovelRuralService;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Named("documentoIdentificacaoController")
@ViewScoped
public class DocumentoIdentificacaoController {
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	private Pessoa pessoa;
	
	private DataModel<DocumentoIdentificacao> modelDocumentosIdentificacao;	
	private DocumentoIdentificacao documentoIdentificacaoSelecionado;
	private Collection<OrgaoExpedidor> listaOrgaoExpedidor;
	private Collection<Estado> listaEstados;
	
	private Boolean mostraLista = false;
	private Boolean campoSerie;
	
	private Boolean editModeDocumento;
	private Boolean editModeCampoTipoDeDocumento;
	private DocumentoIdentificacao documentoIdentificacaoSelecionadoResp;
	
	private Pessoa pessoaJuridica;
	private Pessoa pessoaRepresentanteLegal;
	private Pessoa pessoaProcurador;
	
	private DataModel<DocumentoIdentificacao> modelDocumentosIdentificacaoPessoaJuridica;
	private DataModel<DocumentoIdentificacao> modelDocumentosIdentificacaoPessoaRepresentanteLegal;
	private DataModel<DocumentoIdentificacao> modelDocumentosIdentificacaoPessoaProcurador;
	
	private Boolean temArquivo;
	private Boolean temArquivoRepresentante;
	private String caminhoArquivo;
	private List<String> listaArquivo;
	private List<String> listaArquivoRepresentante;
	private StreamedContent arquivoBaixar;
	private boolean mostraListaPessoaJuridica;
	private boolean mostraListaProcuradorPessoaJuridica;
	private boolean mostraListaRepresentanteLegal;
	private boolean vinculoTitularIgualLAC;
	
	private Boolean desabCombOrg = true;
	
	private Date dataAtual = new Date();
	
	@EJB
	private DocumentoIdentificacaoService documentoIdenficacaoService;	
	@EJB
	private OrgaoExpedidorService orgaoExpedidorService;	
	@EJB
    private EstadoService estadoService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private DocumentoImovelRuralService documentoImovelRuralService;
	@EJB
	private ResponsavelImovelRuralService responsavelImovelRuralService;
	
	private boolean temDocumento;
	
	private List<DocumentoImovelRural> listDocumentoRequerente;
	
	private DocumentoImovelRural documentoImovelRural;
	
	private ResponsavelImovelRural responsavelImovelRural;
	
	@PostConstruct
	public void init() { 
	    carregarItensOrgaoExpedidor();
		carregarModelDocumentoIdentificacao();	
		carregarModelDocumentoIdentificacaoPessoaJuridica();
		carregarModelDocumentoIdentificacaoProcurador();
		carregarModelDocumentoIdentificacaoRepresentanteLegal();
		documentoIdentificacaoSelecionado = new DocumentoIdentificacao();
		documentoIdentificacaoSelecionadoResp = new DocumentoIdentificacao();
		documentoIdentificacaoSelecionado.setIdeOrgaoExpedidor(new OrgaoExpedidor());
		setCampoSerie(Boolean.FALSE);
		setEditModeDocumento(Boolean.FALSE);
		carregarEstados();
		temArquivo = false;
		temArquivoRepresentante = false;
		listaArquivo = new ArrayList<String>();
		listaArquivoRepresentante = new ArrayList<String>();
		editModeDocumento = false;
		dataAtual = new Date();
		modelDocumentosIdentificacao = null;
		listDocumentoRequerente = new ArrayList<DocumentoImovelRural>();
	}
	
	
	 
	 public void excluir() {
		 try {

			 responsavelImovelRural.setIdeDocumentoResponsavel(null);
			 responsavelImovelRuralService.salvarOuAtualizarResponsavelImovelRural(responsavelImovelRural);
			this.documentoImovelRuralService.excluirDocumentoObrigatorio(documentoImovelRural);
			listDocumentoRequerente.remove(documentoImovelRural);
			RequestContext.getCurrentInstance().addPartialUpdateTarget("paginaImovel:paginaCefir:tabAbasCefir:subViewRespTec:formArt:dataTableArt");
			RequestContext.getCurrentInstance().addPartialUpdateTarget("paginaImovel:paginaCefir:tabAbasCefir:idFormResposanvelTecnicoTabela");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	 }
	 
	 
	public void carregarEstados() {
		try {
			listaEstados = estadoService.listar();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarComboOrgaoExp(Boolean desabilitaCombo){
		carregarItensOrgaoExpedidor(documentoIdentificacaoSelecionado.getIdeTipoIdentificacao());
		desabCombOrg = desabilitaCombo;
	}
	
	public void carregarItensOrgaoExpedidor(){
		
		try {
			this.listaOrgaoExpedidor = orgaoExpedidorService.listarOrgaoExpedidor((documentoIdentificacaoSelecionado!= null)? documentoIdentificacaoSelecionado.getIdeTipoIdentificacao() : null);
			
			if(!Util.isNullOuVazio(documentoIdentificacaoSelecionado) && !Util.isNullOuVazio(documentoIdentificacaoSelecionado.getIdeTipoIdentificacao()) && documentoIdentificacaoSelecionado.getIdeTipoIdentificacao().getIdeTipoIdentificacao().intValue() == 3)
				this.campoSerie = true;
			else
				this.campoSerie = false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}			
	}
	
	public void carregarItensOrgaoExpedidor(TipoIdentificacao tpIdentif){
		try {
			this.listaOrgaoExpedidor = orgaoExpedidorService.listarOrgaoExpedidor(tpIdentif);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}			
		
	}
	
	public void limparObjetos(){
		this.pessoa = new Pessoa();
	}
	
	public String limparObjetosDocumento() {
    	documentoIdentificacaoSelecionado = new DocumentoIdentificacao();
		setCampoSerie(Boolean.FALSE);
		temArquivo = false;
		listaArquivo.clear();
		caminhoArquivo= "";
		this.desabCombOrg = true;
		return null;
    }  
	
	public void salvarDocumento() {
	
		if (!temArquivo) {
			JsfUtil.addErrorMessage("O campo upload de documento é de preenchimento obrigatório.");
		} else if (vinculoTitularIgualLAC == false && Util.isNullOuVazio(documentoIdentificacaoSelecionado.getDtcEmissao())) {
			JsfUtil.addErrorMessage("O campo data de emissão é de preenchimento obrigatório.");
		} else {
			salvarDocumento(pessoa);
			carregarModelDocumentoIdentificacao();
			limparObjetosDocumento();
			setTemDocumento(true);
			setEditModeCampoTipoDeDocumento(true);
			
			RequestContext.getCurrentInstance().execute("dialogDocumentoObrigatorio.hide()");
			JsfUtil.addSuccessMessage(BUNDLE.getString("geral_msg_inclusao_sucesso"));
			RequestContext.getCurrentInstance().addPartialUpdateTarget("tabAbas:formDocumentoObrigatorio:gridDocumentoObrigatorio");
		}
	}
	
	public void verificarTemArquivo() {
		if(!temArquivo) {
			JsfUtil.addErrorMessage("O campo upload de documento é de preenchimento obrigatório.");
		}
	}
	
	public void salvarDocumentoPessoaJuridica(){
		if(!temArquivo) {
			JsfUtil.addErrorMessage("O campo upload de documento é de preenchimento obrigatório.");
			return;
		}
		if (validate()) {
			salvarDocumento(pessoaJuridica);
			carregarModelDocumentoIdentificacaoPessoaJuridica();
		}
	}
	
	public void salvarDocumentoRepresentanteLegal(){
		if(!temArquivo && !temArquivoRepresentante) {
			JsfUtil.addErrorMessage("O campo upload de documento é de preenchimento obrigatório.");
			return;
		}
		String msg = "Inclusão efetuada com sucesso!";
		if (!Util.isNull(documentoIdentificacaoSelecionado) && !Util.isNull(documentoIdentificacaoSelecionado.getIdeDocumentoIdentificacao())) {
			msg = "Alteração efetuada com sucesso!";
		}
		if(!salvarDocumento(pessoaRepresentanteLegal,true)){
			return;
		}		
		carregarModelDocumentoIdentificacaoRepresentanteLegal();
		limparObjetosDocumento();
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("flagValidate", Boolean.TRUE);
		JsfUtil.addSuccessMessage(msg);
	}
	
	public void salvarDocumentoProcurador(){
		if(!temArquivo) {
			JsfUtil.addErrorMessage("O campo upload de documento é de preenchimento obrigatório.");
			return;
		}
		String msg = "Inclusão efetuada com sucesso!";
		if (!Util.isNull(documentoIdentificacaoSelecionado) && !Util.isNull(documentoIdentificacaoSelecionado.getIdeDocumentoIdentificacao())) {
			msg = "Alteração efetuada com sucesso!";
		}
		if(!salvarDocumento(pessoaProcurador,false)){
			return;
		}
		carregarModelDocumentoIdentificacaoProcurador();
		limparObjetosDocumento();
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("flagValidate", Boolean.TRUE);
		JsfUtil.addSuccessMessage(msg);
	}
	
	private boolean salvarDocumento(Pessoa pessoa,boolean isRepresentante){
		try {			
			
			if(isRepresentante && this.verificarDuplicidadeDocumentoRepresentante()){
				return false;
			}
			
			if(!isRepresentante && this.verificarDuplicidadeDocumentoProcurador()){
				return false;
			}
			
			OrgaoExpedidor orgaoExpedidor = orgaoExpedidorService.carregarOrgaoExpedidor(this.documentoIdentificacaoSelecionado.getIdeOrgaoExpedidor().getIdeOrgaoExpedidor());
			documentoIdentificacaoSelecionado.setDtcCriacao(new Date());
			documentoIdentificacaoSelecionado.setDtcExclusao(null);
			documentoIdentificacaoSelecionado.setIdePessoa(pessoa);
			documentoIdentificacaoSelecionado.setIndExcluido(Boolean.FALSE);
			documentoIdentificacaoSelecionado.setIdeOrgaoExpedidor(orgaoExpedidor);
			documentoIdentificacaoSelecionado.setDscCaminhoArquivo(caminhoArquivo);
			documentoIdenficacaoService.salvarDocumentoIdentificacao(documentoIdentificacaoSelecionado);
			
			return true;
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
			return false;
		}
	}

	private boolean salvarDocumento(Pessoa pessoa){
		try {			
			OrgaoExpedidor orgaoExpedidor = orgaoExpedidorService.carregarOrgaoExpedidor(this.documentoIdentificacaoSelecionado.getIdeOrgaoExpedidor().getIdeOrgaoExpedidor());
			documentoIdentificacaoSelecionado.setDtcCriacao(new Date());
			documentoIdentificacaoSelecionado.setDtcExclusao(null);
			documentoIdentificacaoSelecionado.setIdePessoa(pessoa);
			documentoIdentificacaoSelecionado.setIndExcluido(Boolean.FALSE);
			documentoIdentificacaoSelecionado.setIdeOrgaoExpedidor(orgaoExpedidor);
			documentoIdentificacaoSelecionado.setDscCaminhoArquivo(caminhoArquivo);
			documentoIdenficacaoService.salvarDocumentoIdentificacao(documentoIdentificacaoSelecionado);
			
			return true;
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
			return false;
		}
	}

	private boolean verificarDuplicidadeDocumentoRepresentante() {
		if(!Util.isNullOuVazio(this.modelDocumentosIdentificacaoPessoaRepresentanteLegal)){
			for (DocumentoIdentificacao documento : this.modelDocumentosIdentificacaoPessoaRepresentanteLegal) {
				if (Util.isNull(documentoIdentificacaoSelecionado.getIdeDocumentoIdentificacao())) {
					if(this.documentoIdentificacaoSelecionado.getIdeTipoIdentificacao().equals(documento.getIdeTipoIdentificacao()) 
							|| (this.documentoIdentificacaoSelecionado.getIdeTipoIdentificacao().equals(documento.getIdeTipoIdentificacao())
							&& this.documentoIdentificacaoSelecionado.getNumDocumento().equals(documento.getNumDocumento()))){
						JsfUtil.addErrorMessage(" Este documento já está cadastrado! Favor cadastrar outro documento");
						return true;
					}
				}
			}
		}
		return false;
	}



	private boolean verificarDuplicidadeDocumentoProcurador() {
		if(!Util.isNullOuVazio(this.modelDocumentosIdentificacaoPessoaProcurador)){
			for (DocumentoIdentificacao documento : this.modelDocumentosIdentificacaoPessoaProcurador) {
				if (Util.isNull(documentoIdentificacaoSelecionado.getIdeDocumentoIdentificacao())) {
					if(this.documentoIdentificacaoSelecionado.getIdeTipoIdentificacao().equals(documento.getIdeTipoIdentificacao())
							|| (this.documentoIdentificacaoSelecionado.getIdeTipoIdentificacao().equals(documento.getIdeTipoIdentificacao())
							&& this.documentoIdentificacaoSelecionado.getNumDocumento().equals(documento.getNumDocumento()))){
						JsfUtil.addErrorMessage(" Este documento já está cadastrado! Favor cadastrar outro documento");
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private Boolean validate() {
		Boolean isValid = Boolean.TRUE;
		if (!Util.isNull(documentoIdentificacaoSelecionado.getIdeTipoIdentificacao()) && documentoIdentificacaoSelecionado.getIdeTipoIdentificacao().getIdeTipoIdentificacao().intValue() == 0) {
			isValid = Boolean.FALSE;
			JsfUtil.addErrorMessage("O campo tipo de documento é de preenchimento obrigatório.");
		}
		return isValid;
	}
	
	public void carregarModelDocumentoIdentificacao(){
		this.modelDocumentosIdentificacao = carregarModelDocumentoIdentificacao(pessoa);
	}
	
	public void carregarModelDocumentoIdentificacaoPessoaJuridica(){
		this.modelDocumentosIdentificacaoPessoaJuridica = carregarModelDocumentoIdentificacao(pessoaJuridica);
	}
	
	public void carregarModelDocumentoIdentificacaoRepresentanteLegal(){
		this.modelDocumentosIdentificacaoPessoaRepresentanteLegal = carregarModelDocumentoIdentificacao(pessoaRepresentanteLegal);
	}
	
	public void carregarModelDocumentoIdentificacaoProcurador(){
		this.modelDocumentosIdentificacaoPessoaProcurador = carregarModelDocumentoIdentificacao(pessoaProcurador);
	}
	
	private DataModel<DocumentoIdentificacao> carregarModelDocumentoIdentificacao(Pessoa pessoa){
		try {
			if (!Util.isNull(pessoa)) {
				return new ListDataModel<DocumentoIdentificacao>(documentoIdenficacaoService.listarDocumentosIdentificacaoPorPessoa(pessoa));
			}
		} catch (Exception exception) {			
			JsfUtil.addErrorMessage(exception.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
		return null;
	}
	
	
	public void listenerValor(ValueChangeEvent event){
		TipoIdentificacao tipoIdentificacao = (TipoIdentificacao) event.getNewValue();
		if(tipoIdentificacao.getIdeTipoIdentificacao().compareTo(TipoDocumentoEnum.CTPS.getValue()) == 0){
			this.campoSerie = Boolean.TRUE;
		}
		else {
			this.campoSerie = Boolean.FALSE;
		}
	}
	
	public void removerDocumentoProcurador() {
		removerDocumento();
		carregarModelDocumentoIdentificacaoProcurador();
	}
	
	
	public void removerDocumentoRepresentanteLegal(){
		removerDocumento();
		carregarModelDocumentoIdentificacaoRepresentanteLegal();
	}
	
	public void removerDocumentoPessoaJuridica(){
		removerDocumento();
		carregarModelDocumentoIdentificacaoPessoaJuridica();
	}
	
	public void removerDocumento(){
		try {
			documentoIdentificacaoSelecionado.setDtcExclusao(new Date());
			documentoIdentificacaoSelecionado.setIndExcluido(Boolean.TRUE);
			documentoIdenficacaoService.excluirDocumentoIdentificacao(this.documentoIdentificacaoSelecionado);
			
			carregarModelDocumentoIdentificacao();
			limparObjetosDocumento();
			JsfUtil.addSuccessMessage("Documento removido com sucesso.");
		} catch (Exception exception) {		
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	
	public void trataArquivo(FileUploadEvent event) {
		caminhoArquivo = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.EMPREENDIMENTO.toString());
		temArquivo = true;
		listaArquivo.clear();
		listaArquivo.add(FileUploadUtil.getFileName(caminhoArquivo));
		documentoIdentificacaoSelecionado.setDscCaminhoArquivo(caminhoArquivo);
		RequestContext.getCurrentInstance().addPartialUpdateTarget("tabviewpj:tabviewproc:formdocumentosproc:arquivoHidden");
		JsfUtil.addSuccessMessage("Arquivo enviado com sucesso.");		
	}
	
	public void trataArquivoRepresentante(FileUploadEvent event) {
		caminhoArquivo = FileUploadUtil.Enviar(event,
					DiretorioArquivoEnum.EMPREENDIMENTO.toString());
		temArquivoRepresentante = true;
		listaArquivoRepresentante.clear();
		listaArquivoRepresentante.add(FileUploadUtil.getFileName(caminhoArquivo));
		documentoIdentificacaoSelecionado.setDscCaminhoArquivo(caminhoArquivo);
		JsfUtil.addSuccessMessage("Arquivo enviado com sucesso.");		
	}
	
	
		
	public DataModel<DocumentoIdentificacao> getModelDocumentosIdentificacao() {
		return modelDocumentosIdentificacao;
	}

	public void setModelDocumentosIdentificacao(
			DataModel<DocumentoIdentificacao> modelDocumentosIdentificacao) {
		this.modelDocumentosIdentificacao = modelDocumentosIdentificacao;
	}

	public DocumentoIdentificacao getDocumentoIdentificacaoSelecionado() {
		return documentoIdentificacaoSelecionado;
	}

	public void setDocumentoIdentificacaoSelecionado(DocumentoIdentificacao documentoIdentificacaoSelecionado) {
		
		this.documentoIdentificacaoSelecionado = documentoIdentificacaoSelecionado;
		
		if(!Util.isNullOuVazio(documentoIdentificacaoSelecionado.getIdePessoa())){
			this.pessoa = documentoIdentificacaoSelecionado.getIdePessoa();
		}
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
		if(!Util.isNullOuVazio(documentoIdentificacaoSelecionado.getIdeDocumentoIdentificacao())) {
			listaArquivo.clear();
			listaArquivo.add(FileUploadUtil.getFileName(documentoIdentificacaoSelecionado.getDscCaminhoArquivo()));
			
			listaArquivo.clear();
			listaArquivo.add(FileUploadUtil.getFileName(documentoIdentificacaoSelecionado.getDscCaminhoArquivo()));
			caminhoArquivo = documentoIdentificacaoSelecionado.getDscCaminhoArquivo();
			
			if(!Util.isNullOuVazio(documentoIdentificacaoSelecionado.getDscCaminhoArquivo()) ){
				temArquivo = true;
			}
		}
		
		this.editModeDocumento = editModeDocumento;
	}



	public Collection<OrgaoExpedidor> getListaOrgaoExpedidor() {
		return listaOrgaoExpedidor;
	}

	public void setListaOrgaoExpedidor(Collection<OrgaoExpedidor> listaOrgaoExpedidor) {
		this.listaOrgaoExpedidor = listaOrgaoExpedidor;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
		this.carregarModelDocumentoIdentificacao();
	}



	public Collection<Estado> getListaEstados() {
		return listaEstados;
	}



	public void setListaEstados(Collection<Estado> listaEstados) {
		this.listaEstados = listaEstados;
	}



	public Pessoa getPessoaJuridica() {
		return pessoaJuridica;
	}



	public void setPessoaJuridica(Pessoa pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
		carregarModelDocumentoIdentificacaoPessoaJuridica();
	}



	public Pessoa getPessoaRepresentanteLegal() {
		return pessoaRepresentanteLegal;
	}



	public void setPessoaRepresentanteLegal(Pessoa pessoaRepresentanteLegal) {
		this.pessoaRepresentanteLegal = pessoaRepresentanteLegal;
		carregarModelDocumentoIdentificacaoRepresentanteLegal();
	}



	public Pessoa getPessoaProcurador() {
		return pessoaProcurador;
	}



	public void setPessoaProcurador(Pessoa pessoaProcurador) {
		this.pessoaProcurador = pessoaProcurador;
		carregarModelDocumentoIdentificacaoProcurador();
	}



	public DataModel<DocumentoIdentificacao> getModelDocumentosIdentificacaoPessoaJuridica() {
		return modelDocumentosIdentificacaoPessoaJuridica;
	}



	public void setModelDocumentosIdentificacaoPessoaJuridica(
			DataModel<DocumentoIdentificacao> modelDocumentosIdentificacaoPessoaJuridica) {
		this.modelDocumentosIdentificacaoPessoaJuridica = modelDocumentosIdentificacaoPessoaJuridica;
	}



	public DataModel<DocumentoIdentificacao> getModelDocumentosIdentificacaoPessoaRepresentanteLegal() {
		return modelDocumentosIdentificacaoPessoaRepresentanteLegal;
	}



	public void setModelDocumentosIdentificacaoPessoaRepresentanteLegal(
			DataModel<DocumentoIdentificacao> modelDocumentosIdentificacaoPessoaRepresentanteLegal) {
		this.modelDocumentosIdentificacaoPessoaRepresentanteLegal = modelDocumentosIdentificacaoPessoaRepresentanteLegal;
	}



	public DataModel<DocumentoIdentificacao> getModelDocumentosIdentificacaoPessoaProcurador() {
		return modelDocumentosIdentificacaoPessoaProcurador;
	}



	public void setModelDocumentosIdentificacaoPessoaProcurador(
			DataModel<DocumentoIdentificacao> modelDocumentosIdentificacaoPessoaProcurador) {
		this.modelDocumentosIdentificacaoPessoaProcurador = modelDocumentosIdentificacaoPessoaProcurador;
	}



	public Boolean getMostraLista() {
		if(!Util.isNullOuVazio(modelDocumentosIdentificacao) && modelDocumentosIdentificacao.getRowCount()>0) {
			mostraLista = true;
			temDocumento = true;
		}else {
			mostraLista = false;
		}
		return mostraLista;
	}



	public void setMostraLista(Boolean mostraLista) {
		this.mostraLista = mostraLista;
	}



	public List<String> getListaArquivo() {
		return listaArquivo;
	}



	public void setListaArquivo(List<String> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}



	public StreamedContent getArquivoBaixar() {		
		if(!listaArquivo.isEmpty()) {			
			try {
				arquivoBaixar = gerenciaArquivoService.getContentFile(documentoIdentificacaoSelecionado.getDscCaminhoArquivo());
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Arquivo não encontrado.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return arquivoBaixar;
	}
	
	
	public void setArquivoBaixar(StreamedContent arquivoBaixar) {
		this.arquivoBaixar = arquivoBaixar;
	}



	public List<String> getListaArquivoRepresentante() {
		return listaArquivoRepresentante;
	}



	public void setListaArquivoRepresentante(List<String> listaArquivoRepresentante) {
		this.listaArquivoRepresentante = listaArquivoRepresentante;
	}



	public void setArquivoBaixarRepresentante(StreamedContent arquivoBaixarRepresentante) {
	}
	
	public StreamedContent getArquivoBaixarRepresentante() {		
		if(!listaArquivoRepresentante.isEmpty()) {			
			try {
				arquivoBaixar = gerenciaArquivoService.getContentFile(documentoIdentificacaoSelecionado.getDscCaminhoArquivo());
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Arquivo não encontrado.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return arquivoBaixar;
	}

	public Boolean getTemArquivo() {
		return temArquivo;
	}

	public void setTemArquivo(Boolean temArquivo) {
		this.temArquivo = temArquivo;
	}

	public Boolean getTemArquivoRepresentante() {
		return temArquivoRepresentante;
	}



	public void setTemArquivoRepresentante(Boolean temArquivoRepresentante) {
		this.temArquivoRepresentante = temArquivoRepresentante;
	}




	public boolean isMostraListaPessoaJuridica() {
		if(!Util.isNullOuVazio(modelDocumentosIdentificacaoPessoaJuridica) && modelDocumentosIdentificacaoPessoaJuridica.getRowCount()>0) {
			mostraListaPessoaJuridica = true;
		}
		else {
			mostraListaPessoaJuridica = false;
		}
		return mostraListaPessoaJuridica;
	}



	public void setMostraListaPessoaJuridica(boolean mostraListaPessoaJuridica) {
		this.mostraListaPessoaJuridica = mostraListaPessoaJuridica;
	}



	public boolean isMostraListaProcuradorPessoaJuridica() {
		if(!Util.isNullOuVazio(modelDocumentosIdentificacaoPessoaProcurador) && modelDocumentosIdentificacaoPessoaProcurador.getRowCount()>0) {
			mostraListaProcuradorPessoaJuridica = true;
		}
		else {
			mostraListaProcuradorPessoaJuridica = false; 
		}
		return mostraListaProcuradorPessoaJuridica;
	}



	public void setMostraListaProcuradorPessoaJuridica(
			boolean mostraListaProcuradorPessoaJuridica) {
		this.mostraListaProcuradorPessoaJuridica = mostraListaProcuradorPessoaJuridica;
	}



	public boolean isMostraListaRepresentanteLegal() {
		if(!Util.isNullOuVazio(modelDocumentosIdentificacaoPessoaRepresentanteLegal) && modelDocumentosIdentificacaoPessoaRepresentanteLegal.getRowCount()>0) {
			mostraListaRepresentanteLegal = true;
		}
		else {
			mostraListaRepresentanteLegal = false;
		}
		return mostraListaRepresentanteLegal;
	}



	public void setMostraListaRepresentanteLegal(
			boolean mostraListaRepresentanteLegal) {
		this.mostraListaRepresentanteLegal = mostraListaRepresentanteLegal;
	}

	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public Boolean getDesabCombOrg() {
		return desabCombOrg;
	}

	public void setDesabCombOrg(Boolean desabCombOrg) {
		this.desabCombOrg = desabCombOrg;
	}

	public Boolean getEditModeCampoTipoDeDocumento() {
		return editModeCampoTipoDeDocumento;
	}

	public void setEditModeCampoTipoDeDocumento(Boolean editModeCampoTipoDeDocumento) {
		this.editModeCampoTipoDeDocumento = editModeCampoTipoDeDocumento;
	}

	public DocumentoIdentificacao getDocumentoIdentificacaoSelecionadoResp() {
		documentoIdentificacaoSelecionadoResp.setIdeTipoIdentificacao(new TipoIdentificacao(TipoDocumentoEnum.CARTEIRA_DE_IDENT_DE_CONS_DE_CLASSE.getValue()));
		return documentoIdentificacaoSelecionadoResp;
	}

	public void setDocumentoIdentificacaoSelecionadoResp(DocumentoIdentificacao documentoIdentificacaoSelecionadoResp) {
		this.documentoIdentificacaoSelecionadoResp = documentoIdentificacaoSelecionadoResp;
	}
	
	public void fecharDialog(CloseEvent event) {
		setEditModeCampoTipoDeDocumento(true);
	}

	public boolean isVinculoTitularIgualLAC() {
		return vinculoTitularIgualLAC;
	}

	public void setVinculoTitularIgualLAC(boolean vinculoTitularIgualLAC) {
		this.vinculoTitularIgualLAC = vinculoTitularIgualLAC;
	}

	public boolean isTemDocumento() {
		return temDocumento;
	}

	public void setTemDocumento(boolean temDocumento) {
		this.temDocumento = temDocumento;
	}

	public List<DocumentoImovelRural> getListDocumentoRequerente() {
		return listDocumentoRequerente;
	}
	
	public void setListDocumentoRequerente(List<DocumentoImovelRural> listDocumentoRequerente) {
		this.listDocumentoRequerente = listDocumentoRequerente;
	}

	public DocumentoImovelRural getDocumentoImovelRural() {
		return documentoImovelRural;
	}

	public void setDocumentoImovelRural(DocumentoImovelRural documentoImovelRural) {
		this.documentoImovelRural = documentoImovelRural;
	}

	public ResponsavelImovelRural getResponsavelImovelRural() {
		return responsavelImovelRural;
	}

	public void setResponsavelImovelRural(ResponsavelImovelRural responsavelImovelRural) {
		this.responsavelImovelRural = responsavelImovelRural;
	}

}
