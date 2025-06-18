package br.gov.ba.seia.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.hibernate.LazyInitializationException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.Escolaridade;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Ocupacao;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ResponsavelImovelRural;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoIdentificacao;
import br.gov.ba.seia.entity.TipoTelefone;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.DocumentoImovelRuralService;
import br.gov.ba.seia.service.EscolaridadeService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.PaisService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.ResponsavelImovelRuralService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoTelefoneService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("responsavelCefirController")
@ViewScoped
public class ResponsavelCefirController extends SeiaControllerAb {

	private PessoaFisica responsavel;
	private Ocupacao ocupacao;
	private Collection<Pais> listaPais;
	private Pais pais;
	private ImovelRural imovelRural;
	private TipoTelefone tipoTelefone;
	private Collection<TipoTelefone> listaTelefone;
	private Telefone telefone;
	private Collection<ResponsavelImovelRural> listaResponsaveis;
	private ResponsavelImovelRural responsavelImovelRural;
	private List<TipoIdentificacao> identificacaoResponsavel;
	private List<Escolaridade> listaEscolaridade;
	private String lblBtnSalvarResponsavelTecnico;

	private Boolean visualizaResponsavel;
	private Boolean editaResponsavel;
	private String numTelefone;
	private Boolean mostraResponsaveis;
	private Boolean temPessoaSelecionada;
	
	private Date dataAtual = new Date();
	// -----------------TELEFONE------------------
	private DataModel<Telefone> modelTelefone;
	
	private Boolean showIncluirResponsavel;
	
	private List<DocumentoImovelRural> listDocumentoRequerente;
	
	private boolean temArt;
	
	private DocumentoImovelRural documentoImovelRural;

	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private PaisService paisService;
	@EJB
	private ResponsavelImovelRuralService responsavelImovelRuralService;
	@EJB
	private TipoTelefoneService tipoTelefoneService;
	@EJB
	private TelefoneService telefoneService;
	@EJB
	private EscolaridadeService escolaridadeService;
	@EJB
	private ImovelRuralService imovelRuralService;
	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;
	@EJB
	private DocumentoImovelRuralService documentoImovelRuralService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private AuditoriaFacade auditoria;
	
	@PostConstruct
	public void init() {
		lblBtnSalvarResponsavelTecnico=ResourceBundle.getBundle("/Bundle").getString("geral_lbl_salvar");
		imovelRural = ContextoUtil.getContexto().getImovelRural();
		//imovelRural = (ImovelRural) getAtributoSession("IMOVEL_RURAL");
		responsavel = new PessoaFisica();
		visualizaResponsavel = false;
		editaResponsavel = false;
		modelTelefone = null;// new ListDataModel<Telefone>();
		tipoTelefone = new TipoTelefone();
		telefone = new Telefone();
		responsavelImovelRural = new ResponsavelImovelRural();
		mostraResponsaveis = false;
		listaResponsaveis = new ArrayList<ResponsavelImovelRural>();
		listaEscolaridade = new ArrayList<Escolaridade>();
		carregarResponsaveis();
		carregarEscolaridade();
		setDocumentoImovelRural(null);
		preparaIdentificacao();
		showIncluirResponsavel = Boolean.TRUE;
		Exception erro = null;
		try {
			listaPais = paisService.listar();
			listaTelefone = tipoTelefoneService.listarTipoTelefone();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		listDocumentoRequerente = new ArrayList<DocumentoImovelRural>();
	}
	
	 public void excluir() {
		 Exception erro =null;
		 try {
			 responsavelImovelRural.setIdeDocumentoResponsavel(null);
			 responsavelImovelRuralService.salvarOuAtualizarResponsavelImovelRural(responsavelImovelRural);
			this.documentoImovelRuralService.excluirDocumentoObrigatorio(documentoImovelRural);
			listDocumentoRequerente.remove(documentoImovelRural);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	 }
	 

	public void preparaIdentificacao() {
		identificacaoResponsavel = new ArrayList<TipoIdentificacao>();
		identificacaoResponsavel.add(new TipoIdentificacao(5, "Carteira de identidade de conselho regional ou federação trabalhista"));
	}

	public void editarResponsavel() {
		Exception erro = null;
		try {
			this.responsavelImovelRural = responsavelImovelRuralService.filtrarResponsavelImovelRuralById(this.responsavelImovelRural.getIdeResponsavelImovelRural());
			this.responsavel = this.responsavelImovelRural.getIdePessoaFisica();
			this.visualizaResponsavel = false;
			this.editaResponsavel = true;
			editarArt();
			showIncluirResponsavel = Boolean.FALSE;
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			System.err.println("Erro ao buscar o responsavel do empreendimento.");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	 public void handleFileUpload(FileUploadEvent event) {
			final String tipoDocumento = "ART";
			String nmArquivo = imovelRural.toString()+"_"+responsavel.getNumCpf()+"_"+tipoDocumento;
			String caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.IMOVELRURAL.toString()+
					FileUploadUtil.getFileSeparator()+imovelRural.getIdeImovelRural().toString()+FileUploadUtil.getFileSeparator()+"ARL", nmArquivo);
			
			setDocumentoImovelRural(new DocumentoImovelRural(null, nmArquivo, caminhoArquivo));
			File file = new File(caminhoArquivo.trim());
			getDocumentoImovelRural().setFileSize(file.length());
			setTemArt(true);
			getListDocumentoRequerente().clear();
			getListDocumentoRequerente().add(getDocumentoImovelRural());
			this.responsavelImovelRural.setIdeDocumentoResponsavel(getDocumentoImovelRural());
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
			
	 }

	 public StreamedContent getBaixarArt() {
			if (!listDocumentoRequerente.isEmpty()) {
				Exception erro = null;
				try {
					if(!Util.isNullOuVazio(this.responsavelImovelRural.getIdeDocumentoResponsavel())) {
						return gerenciaArquivoService.getContentFile(this.responsavelImovelRural.getIdeDocumentoResponsavel().getDscCaminhoArquivo());
					}
				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					JsfUtil.addErrorMessage("Arquivo não encontrado.");
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
			}
			return null;
		}
	 
	public void consultarResponsavelByCPF() {
		String cpf;
		Exception erro = null;
		try {
			cpf = responsavel.getNumCpf(); // preenchido na consulta
			responsavel = pessoaFisicaService.filtrarPessoaFisicaByCpf(responsavel);
			if (!Util.isNullOuVazio(responsavel) && !Util.isNullOuVazio(responsavel.getNumCpf())) {
				pais = responsavel.getIdePais();
				visualizaResponsavel = true;
				carregarTelefones();
			} 
			else {
				visualizaResponsavel = false;
				responsavel = new PessoaFisica();
				responsavel.setNumCpf(cpf);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarTelefones() {
		Exception erro = null;
		try {
			this.modelTelefone = Util.castListToDataModel(telefoneService.filtrarTelefonesNaoExcluidosPorPessoa(this.responsavel.getPessoa()));
		} catch (Exception e) {
			erro =e;
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		// this.telefone =
		// ((List<Telefone>)this.responsavel.getPessoa().getTelefoneCollection()).get(0);
	}

	public void carregarResponsaveis() {
		if (!Util.isNullOuVazio(this.imovelRural)) {
			Exception erro = null;
			try {
				this.listaResponsaveis = responsavelImovelRuralService.filtrarResponsaveisPorImovelRural(this.imovelRural);
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}

	}

	private void carregarEscolaridade() {
		Exception erro = null;
		try {
			listaEscolaridade = escolaridadeService.listarEscolaridadeResponsavelTecnico();
		} catch (Exception e) {
			erro =e;
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void salvarResponsavel() {
		if (existeResponsavelNaLista() && visualizaResponsavel ) {
			JsfUtil.addErrorMessage("Já existe Pessoa cadastrada.");
			showIncluirResponsavel = Boolean.FALSE;
			return;
		}

		if(!Util.isNull(this.responsavel) && !Util.isNull(this.responsavel.getIdePessoaFisica()) ){ 
			salvarOuAtualizarPessoaResponsavel();
		}
		Exception erro = null;
		try {
			if(!Util.isNullOuVazio(getDocumentoImovelRural())) {
				this.documentoImovelRuralService.salvarDocumentoObrigatorio(getDocumentoImovelRural());
			}else {
				if(!imovelRural.isMenorQueQuatroModulosFiscais()) {
					JsfUtil.addErrorMessage("Você deve incluir um Documento ART antes de salvar!");
					return;
				}
			}
			responsavelImovelRural.setIdeDocumentoResponsavel(getDocumentoImovelRural());
			if (editaResponsavel) {
				responsavelImovelRuralService.salvarOuAtualizarResponsavelImovelRural(responsavelImovelRural);
				JsfUtil.addSuccessMessage("Atualização Efetuada com Sucesso!");
			} else {
				this.salvaResponsavelImovel();
				JsfUtil.addSuccessMessage("Inclusão Efetuada com Sucesso!");
				editaResponsavel=true;
				visualizaResponsavel = false;
			}

			this.carregarResponsaveis();
			temPessoaSelecionada=true;
			lblBtnSalvarResponsavelTecnico=ResourceBundle.getBundle("/Bundle").getString("geral_lbl_salvar");
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Erro ao Incluir Responsável.");
			temPessoaSelecionada = Boolean.FALSE;
			showIncluirResponsavel = Boolean.FALSE;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void salvaResponsavelImovel() throws Exception {
		ResponsavelImovelRural resp = new ResponsavelImovelRural();
		resp.setDtcCriacao(new Date());
		resp.setDtcExclusao(null);
		resp.setIdeImovelRural(this.imovelRural);
		resp.setIdePessoaFisica(this.responsavel);
		resp.setIdeDocumentoResponsavel(getDocumentoImovelRural());
		
		try {
			if (imovelRural.getResponsavelImovelRuralCollection() == null) {
				imovelRural.setResponsavelImovelRuralCollection(new ArrayList<ResponsavelImovelRural>());
			}
			imovelRural.getResponsavelImovelRuralCollection().add(resp);
			
		} catch(LazyInitializationException e ) {
			//todo erro de lazy inicialization
			imovelRural = imovelRuralService.carregarById(imovelRural.getIdeImovelRural());
			imovelRural.getResponsavelImovelRuralCollection().add(resp);
		}
		
		this.responsavelImovelRural = responsavelImovelRuralService.salvarOuAtualizarResponsavelImovelRural(resp);
	}

	public void salvarOuAtualizarPessoaResponsavel() {
		Exception erro = null;
		try {
			if(Util.isNullOuVazio(this.responsavel.getPessoa().getDtcCriacao())){
				this.responsavel.getPessoa().setDtcCriacao(new Date());				
			}
			auditarResponsavelCefir();
			this.responsavel = pessoaFisicaService.salvarOuAtualizarPessoaFisica(this.responsavel);
		} catch (Exception e) {
			erro =e;
			temPessoaSelecionada = Boolean.FALSE;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void auditarResponsavelCefir() throws Exception {
		if(Util.isNullOuVazio(this.responsavel.getIdePessoaFisica())) {
			auditoria.salvar(this.responsavel);
		}else {
			PessoaFisica responsavelAntigo = pessoaFisicaService.carregarTudo(this.responsavel);
			auditoria.atualizar(responsavelAntigo, this.responsavel);
		}
	}

	public Boolean existeResponsavelNaLista() {
		for (ResponsavelImovelRural resp : listaResponsaveis) {
			if (!Util.isNullOuVazio(resp) && resp.getIdePessoaFisica().getNumCpf().equals(this.responsavel.getNumCpf())) {
				return true;
			}
		}
		return false;
	}

	public void adicionaTelefone() {
		List<Pessoa> lista = new ArrayList<Pessoa>();
		lista.add(responsavel.getPessoa());
		telefone.setPessoaCollection(lista);

		Exception erro = null;
		try {
			telefoneService.salvarTelefone(telefone);
			carregarTelefones();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void limparTelefone() {
		System.out.println("shall clear");
	}

	public void limpar() {
		init();
		showIncluirResponsavel= Boolean.FALSE;
	}
	
	public void mostrarFormulario() {
		limpar();
		showIncluirResponsavel = Boolean.FALSE;
		RequestContext currentInstance = RequestContext.getCurrentInstance();
		if(!Util.isNullOuVazio(currentInstance)) {
			currentInstance.addCallbackParam("atualizarTab", showIncluirResponsavel);
		}
	}
	
	public void esconderFormulario() {
		limpar();
		showIncluirResponsavel = Boolean.TRUE;
		RequestContext currentInstance = RequestContext.getCurrentInstance();
		if(!Util.isNullOuVazio(currentInstance)) {
			currentInstance.addCallbackParam("atualizarTab", showIncluirResponsavel);
		}
	}

	public void removerTelefone() {
		Exception erro = null;
		try {
			PessoaFisica idePessoaFisica = this.responsavelImovelRural.getIdePessoaFisica();
			if(!Util.isNullOuVazio(idePessoaFisica) && !Util.isNullOuVazio(idePessoaFisica.getPessoa())) {
				List<Telefone> filtrarTelefonePorPessoa = telefoneService.filtrarTelefonesNaoExcluidosPorPessoa(idePessoaFisica.getPessoa());
				if(!Util.isNullOuVazio(filtrarTelefonePorPessoa)) {
					for (Telefone telefone : filtrarTelefonePorPessoa) {
						telefone.setDtcExclusao(new Date());
						telefone.setIndExcluido(true);
						telefoneService.excluirTelefone(telefone);
					}
				}
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void novoTelefone() {
		this.telefone = new Telefone();
	}
	
	public void removerDocumento() {
		Exception erro =null;
		try {
			PessoaFisica idePessoaFisica = this.responsavelImovelRural.getIdePessoaFisica();
			if(!Util.isNullOuVazio(idePessoaFisica) && !Util.isNullOuVazio(idePessoaFisica.getPessoa())) {
				List<DocumentoIdentificacao> listarDocumentosIdentificacaoPorPessoa = this.documentoIdentificacaoService.listarDocumentosIdentificacaoPorPessoa(idePessoaFisica.getPessoa());
				if(!Util.isNullOuVazio(listarDocumentosIdentificacaoPorPessoa)) {
					for (DocumentoIdentificacao documentoIdentificacao : listarDocumentosIdentificacaoPorPessoa) {
						documentoIdentificacao.setDtcExclusao(new Date());
						documentoIdentificacao.setIndExcluido(true);
						documentoIdentificacaoService.excluirDocumentoIdentificacao(documentoIdentificacao);
					}
				}
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void removerResponsavel() {
		Exception erro =null;
		try {
			responsavelImovelRuralService.removerResponsavel(this.responsavelImovelRural);
			removerTelefone();
			removerDocumento();
			carregarResponsaveis();
			JsfUtil.addSuccessMessage("Exclusão efetuada com sucesso!");
			this.responsavel = null;
			this.responsavel = new PessoaFisica();
			listaTelefone = new ArrayList<TipoTelefone>();
			showIncluirResponsavel = Boolean.TRUE; //quem fez a variavel pensou ao contrário. quando true os elementos se escondem.
//			mostrarFormulario();
			esconderFormulario();
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Erro ao Remover Responsável: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	/** GETS/SETS **/
	public PessoaFisica getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(PessoaFisica responsavel) {
		this.responsavel = responsavel;
	}

	public Ocupacao getOcupacao() {
		return ocupacao;
	}

	public void setOcupacao(Ocupacao ocupacao) {
		this.ocupacao = ocupacao;
	}

	public Collection<Pais> getListaPais() {
		return listaPais;
	}

	public void setListaPais(Collection<Pais> listaPais) {
		this.listaPais = listaPais;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Boolean getVisualizaResponsavel() {
		return visualizaResponsavel;
	}

	public void setVisualizaResponsavel(Boolean visualizaResponsavel) {
		this.visualizaResponsavel = visualizaResponsavel;
	}

	public Boolean getEditaResponsavel() {
		return editaResponsavel;
	}

	public void setEditaResponsavel(Boolean editaResponsavel) {
		this.editaResponsavel = editaResponsavel;
	}

	public DataModel<Telefone> getModelTelefone() {
		return modelTelefone;
	}

	public void setModelTelefone(DataModel<Telefone> modelTelefone) {
		this.modelTelefone = modelTelefone;
	}

	public TipoTelefone getTipoTelefone() {
		return tipoTelefone;
	}

	public void setTipoTelefone(TipoTelefone tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	public Collection<TipoTelefone> getListaTelefone() {
		return listaTelefone;
	}

	public void setListaTelefone(Collection<TipoTelefone> listaTelefone) {
		this.listaTelefone = listaTelefone;
	}

	public String getNumTelefone() {
		return numTelefone;
	}

	public void setNumTelefone(String numTelefone) {
		this.numTelefone = numTelefone;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Collection<ResponsavelImovelRural> getListaResponsaveis() {
		return listaResponsaveis;
	}

	public void setListaResponsaveis(Collection<ResponsavelImovelRural> listaResponsaveis) {
		this.listaResponsaveis = listaResponsaveis;
	}

	public Boolean getMostraResponsaveis() {
		if (listaResponsaveis.isEmpty()) {
			mostraResponsaveis = false;
		} else {
			mostraResponsaveis = true;
		}
		return mostraResponsaveis;
	}

	public ResponsavelImovelRural getResponsavelImovelRural() {
		return responsavelImovelRural;
	}

	public void setResponsavelImovelRural(ResponsavelImovelRural responsavelImovelRural) {
		this.responsavelImovelRural = responsavelImovelRural;
	}

	public List<TipoIdentificacao> getIdentificacaoResponsavel() {
		return identificacaoResponsavel;
	}

	public void setIdentificacaoResponsavel(List<TipoIdentificacao> identificacaoResponsavel) {
		this.identificacaoResponsavel = identificacaoResponsavel;
	}

	public Boolean getTemPessoaSelecionada() {
		if (!Util.isNullOuVazio(responsavel) && !Util.isNullOuVazio(responsavel.getIdePessoaFisica())) {
			temPessoaSelecionada = true;
		} else {
			temPessoaSelecionada = false;
		}
		return temPessoaSelecionada;
	}

	public void setTemPessoaSelecionada(Boolean temPessoaSelecionada) {
		this.temPessoaSelecionada = temPessoaSelecionada;
	}

	public List<Escolaridade> getListaEscolaridade() {
		return listaEscolaridade;
	}

	public void setListaEscolaridade(List<Escolaridade> listaEscolaridade) {
		this.listaEscolaridade = listaEscolaridade;
	}

	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}

	public Boolean getShowIncluirResponsavel() {
		return showIncluirResponsavel;
	}

	public void setShowIncluirResponsavel(Boolean showIncluirResponsavel) {
		this.showIncluirResponsavel = showIncluirResponsavel;
	}

	public String getLblBtnSalvarResponsavelTecnico() {
		return lblBtnSalvarResponsavelTecnico;
	}

	public void setLblBtnSalvarResponsavelTecnico(
			String lblBtnSalvarResponsavelTecnico) {
		this.lblBtnSalvarResponsavelTecnico = lblBtnSalvarResponsavelTecnico;
	}

	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}

	public DocumentoIdentificacaoService getDocumentoIdentificacaoService() {
		return documentoIdentificacaoService;
	}

	public void setDocumentoIdentificacaoService(DocumentoIdentificacaoService documentoIdentificacaoService) {
		this.documentoIdentificacaoService = documentoIdentificacaoService;
	}

	public boolean isTemArt() {
		return temArt;
	}

	public void setTemArt(boolean temArt) {
		this.temArt = temArt;
	}

	public Collection<DocumentoImovelRural> getListDocumentoRequerente() {
		return listDocumentoRequerente;
	}

	public void setListDocumentoRequerente(List<DocumentoImovelRural> listDocumentoRequerente) {
		this.listDocumentoRequerente = listDocumentoRequerente;
	}
	
	public void editarArt() {
		listDocumentoRequerente = new ArrayList<DocumentoImovelRural>();
		if(!Util.isNullOuVazio(this.responsavelImovelRural)) {
			setDocumentoImovelRural(this.responsavelImovelRural.getIdeDocumentoResponsavel());
		}
		if(!Util.isNullOuVazio(getDocumentoImovelRural())) {
			listDocumentoRequerente.add(getDocumentoImovelRural());
		}
	}

	public StreamedContent getBaixarArtListagem() {
		Exception erro = null;
		 try {
			return gerenciaArquivoService.getContentFile(documentoImovelRural.getDscCaminhoArquivo());
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		return null;
	 }
	public DocumentoImovelRural getDocumentoImovelRural() {
		return documentoImovelRural;
	}

	public void setDocumentoImovelRural(DocumentoImovelRural documentoImovelRural) {
		this.documentoImovelRural = documentoImovelRural;
	}
}
