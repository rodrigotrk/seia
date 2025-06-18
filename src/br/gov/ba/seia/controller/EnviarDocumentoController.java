package br.gov.ba.seia.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoVeiculo;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.LacLegislacao;
import br.gov.ba.seia.entity.LacTransporte;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.facade.DocumentoObrigatorioRequerimentoFacadeService;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.DocumentoAtoAmbientalService;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EmpreendimentoVeiculoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.LacLegislacaoService;
import br.gov.ba.seia.service.LacTransporteService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.VwDocumentoIdentificacaoObrigatorioService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("enviarDocumentoController")
@ViewScoped
@SuppressWarnings("all")
public class EnviarDocumentoController extends DocumentoController{

	ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	private List<DocumentoObrigatorio> arqvsObrigatorios;
	private List<DocumentoObrigatorioRequerimento> arqvsFormsEnviados;
	private List<DocumentoObrigatorio> arqvsFormsObrigatorios;
	private List<DocumentoObrigatorioRequerimento> arqvsADeletar;
	private List<DocumentoObrigatorioRequerimento> arqvsDocsEnviados;
	private List<DocumentoObrigatorioRequerimento> arqvsObrigatoriosUpoloaded;
	private List<DocumentoObrigatorio> listaDocumentos;
	private List<DocumentoObrigatorio> listaFormularios;
	private DocumentoObrigatorio docObrigatorioFormSelected;
	private DocumentoObrigatorio docObrigatorioDocSelected;
	private DocumentoObrigatorio docDownload;
	private StreamedContent arquivoBaixar;
	private Usuario usuario;
	private Enquadramento enquadramento;
	private RequerimentoUnico requerimentoUnico;
	private Boolean renderedFormulario;
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private DocumentoAtoAmbientalService docObrigatorioService;
	@EJB
	private EnquadramentoService enquadramentoService;
	@EJB
	private DocumentoObrigatorioRequerimentoFacadeService docObrigatorioRequerimentoFacadeService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private ParametroService parametroService;
	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;
	@EJB
	private VwDocumentoIdentificacaoObrigatorioService docRequerimentoService;
	@EJB
	private DocumentoObrigatorioRequerimentoService docObrigatorioRequerimentoService;
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	@EJB
	private LacLegislacaoService lacLegislacaoService;
	@EJB
	private DocumentoIdentificacaoService documentoIdenficacaoService;
	@EJB
	private LacTransporteService lacTransporteService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private EmpreendimentoVeiculoService veiculoService;
	private List<DocumentoObrigatorioRequerimento> arqvsDocumento;
	private List<AtoAmbiental> atosAmbientais;
	private List<DocumentoIdentificacao> arqvsDocumentoIdentificacao;
	private DocumentoObrigatorioRequerimento documentoObrigatorioSelecionado;
	private DocumentoIdentificacao documentoIdentificacaoSelecionado;
	private Usuario usuarioLogado;

	private List<String> listaArquivo;
	private String caminhoArquivo;
	private DocumentoObrigatorioRequerimento docObrigatorioFceIntervencao;
	private DocumentoObrigatorioRequerimento docObrigatorioFceIntervencaoTemporaria;

	private FileUploadEvent fileUploadEvent;

	public EnviarDocumentoController() {
	}

	@PostConstruct
	public void init() {
		arqvsFormsObrigatorios = new ArrayList<DocumentoObrigatorio>();
	}

	public void carregarTela(RequerimentoUnico requerimentoUnico) {
		this.requerimentoUnico = requerimentoUnico;
		usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
		try {
			enquadramento = enquadramentoService.buscarEnquadramentoCompletoRequerimento(requerimentoUnico);
			if (!enquadramento.getAtoAmbientalCollection().isEmpty()) {
				this.arqvsFormsObrigatorios = (List<DocumentoObrigatorio>) this.docObrigatorioRequerimentoService.listarFormulariosAtoWhereIn(requerimentoUnico, enquadramento.getAtoAmbientalCollection());
			} else {
				enquadramento.setAtoAmbientalCollection(new ArrayList<AtoAmbiental>());
			}
			this.arqvsDocsEnviados = (List<DocumentoObrigatorioRequerimento>) this.docObrigatorioRequerimentoFacadeService.listarDocumentosEnviados(enquadramento);
			arqvsDocumento = docRequerimentoService.buscarDocsFormulariosObrigatorio(requerimentoUnico);

			for (DocumentoObrigatorioRequerimento doc : arqvsDocumento) {

				if (!Util.isNull(doc.getDscCaminhoArquivo())) {
					File arquivo = new File(doc.getDscCaminhoArquivo());
					doc.setFileSize(arquivo.length());
					arquivo = null;
				}
			}

			agruparListaAtosAmbientais();
			arqvsDocumentoIdentificacao = new ArrayList<DocumentoIdentificacao>();
			Requerimento requerimento = requerimentoService.buscarEntidadePorId(new Requerimento(enquadramento.getIdeRequerimento().getIdeRequerimento()));
			Pessoa pessoaRequerente = null;
			for (RequerimentoPessoa rp : requerimento.getRequerimentoPessoaCollection()) {
				if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento() == TipoPessoaRequerimentoEnum.REQUERENTE.getId()) {
					pessoaRequerente = rp.getPessoa();
				}
			}
			arqvsDocumentoIdentificacao.addAll(documentoIdentificacaoService.listarDocumentosIdentificacaoPorPessoa(pessoaRequerente));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void agruparListaAtosAmbientais() {
		atosAmbientais = new ArrayList<AtoAmbiental>();

		for (DocumentoObrigatorioRequerimento doc : arqvsDocumento) {
			AtoAmbiental atoAmbiental = doc.getAtoAmbiental();
			//atoAmbiental.setDocumentosObrigatoriosRequerimento(new ArrayList<DocumentoObrigatorioRequerimento>());
			if(atosAmbientais.contains(atoAmbiental)){
				int indexOf = atosAmbientais.indexOf(atoAmbiental);
				if(indexOf >= 0){
					if(!Util.isNullOuVazio(doc.getIndDocumentoValidado()) && !doc.getIndDocumentoValidado()){
						//atosAmbientais.get(indexOf).getDocumentosObrigatoriosRequerimento().add(doc);
					}
				}
			}else{
				if(!Util.isNullOuVazio(doc.getIndDocumentoValidado()) && !doc.getIndDocumentoValidado()){
					//atoAmbiental.getDocumentosObrigatoriosRequerimento().add(doc);
				}
				atosAmbientais.add(atoAmbiental);
			}

		}
	}

	private void carregarFormularios(List<AtoAmbiental> parametros) {
		try {
			listaFormularios = new ArrayList<DocumentoObrigatorio>();
			//listaFormularios.add(new DocumentoObrigatorio(null, BUNDLE.getString("geral_lbl_selecione")));
			listaFormularios.addAll(this.docObrigatorioService.listarFormulariosAtoWhereIn(parametros));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void preencherArquivos(DocumentoObrigatorio docObrigatorio) {
		/*if (docObrigatorio.getDscCaminhoArquivo() != null && !docObrigatorio.getDscCaminhoArquivo().trim().isEmpty()) {
			try {
				//String caminhoArquivo = docObrigatorio.getDocumentoObrigatorioRequerimento(requerimentoUnico.getRequerimento()).getDscCaminhoArquivo().trim();
				InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
				//String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
				//docObrigatorio.setFile(new DefaultStreamedContent(stream, mimeType, caminhoArquivo.substring(caminhoArquivo.lastIndexOf(File.separator) + 1)));
				//docObrigatorio.setFileSize(new File(caminhoArquivo).length());
			} catch (FileNotFoundException e) {
				JsfUtil.addErrorMessage(e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		} else {
			docObrigatorio.setDscCaminhoArquivo("");
		}*/
	}

	private void preencherArquivos(List<DocumentoObrigatorioRequerimento> docsObrigatorioReq) {
		for (DocumentoObrigatorioRequerimento docObrigatorioReq : docsObrigatorioReq) {
			if (docObrigatorioReq.getDscCaminhoArquivo() != null && !docObrigatorioReq.getDscCaminhoArquivo().trim().isEmpty()) {
				try {
					String caminhoArquivo = docObrigatorioReq.getDscCaminhoArquivo().trim();
					InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
					String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
					docObrigatorioReq.setFile(new DefaultStreamedContent(stream, mimeType, caminhoArquivo.substring(caminhoArquivo.lastIndexOf(File.separator) + 1)));
					docObrigatorioReq.setFileSize(new File(caminhoArquivo).length());
				} catch (FileNotFoundException e) {
					JsfUtil.addErrorMessage(e.getMessage());
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			} else {
				docObrigatorioReq.setDscCaminhoArquivo("");
			}
		}
	}

	private void preencherArquivosDownload(DocumentoObrigatorio docObrigatorio) {
		if (docObrigatorio.getDscCaminhoArquivo() != null && !docObrigatorio.getDscCaminhoArquivo().trim().isEmpty()) {
			try {
				String caminhoArquivo = docObrigatorio.getDscCaminhoArquivo().trim();
				InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
				String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
				//docObrigatorio.setFile(new DefaultStreamedContent(stream, mimeType, caminhoArquivo.substring(caminhoArquivo.lastIndexOf(File.separator) + 1)));
				//docObrigatorio.setFileSize(new File(caminhoArquivo).length());
			} catch (FileNotFoundException e) {
				JsfUtil.addErrorMessage(e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		} else {
			docObrigatorio.setDscCaminhoArquivo("");
		}
	}

	public void handleFileUploadFormularioObrigatorio(FileUploadEvent event) {
		try {
			StreamedContent file = new DefaultStreamedContent(new DataInputStream(new BufferedInputStream(event.getFile().getInputstream())), event.getFile().getContentType(), event.getFile().getFileName());
			if (docObrigatorioFormSelected != null) {
				if (!this.isArquivoFormUploaded(docObrigatorioFormSelected)) {
					if (!this.isNomeArquivoSemelhante(file, arqvsDocsEnviados) && !this.isNomeArquivoSemelhante(file, arqvsFormsEnviados)) {
						//docObrigatorioFormSelected.setFile(file);
						//docObrigatorioFormSelected.setFileSize(event.getFile().getSize());
						DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento = new DocumentoObrigatorioRequerimento();
						documentoObrigatorioRequerimento.setFileUpload(event);
						//documentoObrigatorioRequerimento.setIdeDocumentoObrigatorio(listaFormularios.get(listaFormularios.indexOf(docObrigatorioFormSelected)));
						documentoObrigatorioRequerimento.setFile(file);
						documentoObrigatorioRequerimento.setFileSize(event.getFile().getSize());
						documentoObrigatorioRequerimento.setIdePessoaUpload(new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
						arqvsFormsEnviados.add(documentoObrigatorioRequerimento);
					} else {
						JsfUtil.addErrorMessage(BUNDLE.getString("documento_obrigatorio_lbl_outro_documento_mesmo_nome_ja_enviado"));
					}
				} else {
					JsfUtil.addErrorMessage(BUNDLE.getString("documento_obrigatorio_lbl_doc_obrigatorio_ja_uploaded"));
				}
			} else {
				JsfUtil.addErrorMessage(BUNDLE.getString("documento_obrigatorio_lbl_selecione_doc_obrigatorio"));
			}
			this.carregarFormularios((List<AtoAmbiental>) enquadramento.getAtoAmbientalCollection());
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}

	public void handleFileUploadDocumentoObrigatorio(FileUploadEvent event) {
		try {
			StreamedContent file = new DefaultStreamedContent(new DataInputStream(new BufferedInputStream(event.getFile().getInputstream())), event.getFile().getContentType(), event.getFile().getFileName());
			//docObrigatorioDocSelected = documentoObrigatorioService.carregarDocumentoObrigatorio(documentoObrigatorioSelecionado.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio());
			Requerimento requerimento = null;
			DocumentoObrigatorio docObrigatorio = null;
			if (Util.isNull(requerimento)) {
				requerimento = requerimentoService.buscarEntidadePorId(new Requerimento(documentoObrigatorioSelecionado.getIdeRequerimento().getIdeRequerimento()));
			}
			//if (Util.isNull(docObrigatorio) || !docObrigatorio.getIdeDocumentoObrigatorio().equals(documentoObrigatorioSelecionado.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())) {
			//	docObrigatorio = documentoObrigatorioService.obterDocumentoObrigatorioCriteria(new DocumentoObrigatorio(documentoObrigatorioSelecionado.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()));
			//}
			if (!Util.isNullOuVazio(documentoObrigatorioSelecionado.getDscCaminhoArquivo())) {
				gerenciaArquivoService.deletarArquivo(documentoObrigatorioSelecionado.getDscCaminhoArquivo());
			}
			documentoObrigatorioSelecionado.setFileUpload(event);
			documentoObrigatorioSelecionado.setFile(file);
			documentoObrigatorioSelecionado.setFileSize(event.getFile().getSize());
			atualizarDocObrigatorioRequerimento(documentoObrigatorioSelecionado, requerimento, docObrigatorio);
			JsfUtil.addSuccessMessage("Arquivo carregado com sucesso!");

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void handleFileUploadDocumentoIdentificacao(FileUploadEvent event) {
		try {
			documentoIdenficacaoService.atualizarDocumentoIdenticacao(documentoIdentificacaoSelecionado, event.getFile());
			JsfUtil.addSuccessMessage("Arquivo carregado com sucesso!");
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void carregarDocumento(DocumentoObrigatorioRequerimento docObrigatorio){
		this.documentoObrigatorioSelecionado = docObrigatorio;
	}

	public void atualizarDocObrigatorioRequerimento(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento, Requerimento requerimento, DocumentoObrigatorio docObrigatorio) throws Exception {
		DocumentoObrigatorioRequerimento arquivoObrigatorio = new DocumentoObrigatorioRequerimento();
		arquivoObrigatorio.setIdeDocumentoObrigatorioRequerimento(documentoObrigatorioRequerimento.getIdeDocumentoObrigatorioRequerimento());
		arquivoObrigatorio.setFileUpload(documentoObrigatorioRequerimento.getFileUpload());
		arquivoObrigatorio.setFile(documentoObrigatorioRequerimento.getFile());
		arquivoObrigatorio.setIdeRequerimento(requerimento);
		arquivoObrigatorio.setIdeDocumentoObrigatorio(docObrigatorio);
		arquivoObrigatorio.setDtcValidacao(documentoObrigatorioRequerimento.getDtcValidacao());
		arquivoObrigatorio.setIndDocumentoValidado(documentoObrigatorioRequerimento.getIndDocumentoValidado());
		documentoObrigatorioRequerimento.setIsArquivoChanged(Boolean.TRUE);
		documentoObrigatorioRequerimento.setDscCaminhoArquivo(DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO + documentoObrigatorioRequerimento.getIdeDocumentoObrigatorioRequerimento().toString() + File.separator
				+ FileUploadUtil.getFileName(documentoObrigatorioRequerimento.getFile().getName()));
		arquivoObrigatorio.setDscCaminhoArquivo(documentoObrigatorioRequerimento.getDscCaminhoArquivo());
		arquivoObrigatorio.setArquivoChanged(Boolean.TRUE);
		arquivoObrigatorio.setIdeDocumentoAto(documentoObrigatorioRequerimento.getIdeDocumentoAto());
		arquivoObrigatorio.setIdeEnquadramentoAtoAmbiental(documentoObrigatorioRequerimento.getIdeEnquadramentoAtoAmbiental());
		docObrigatorioRequerimentoService.salvarOuAtualizar(arquivoObrigatorio);
		docObrigatorio.setDscCaminhoArquivo(documentoObrigatorioRequerimento.getDscCaminhoArquivo());
		//		documentoObrigatorioService.salvarAtualizarDocumentoObrigatorio(docObrigatorio);
	}

	private boolean isArquivoFormUploaded(DocumentoObrigatorio docObrigatorio) {
		for (DocumentoObrigatorioRequerimento docForm : arqvsFormsEnviados) {
			//if (docObrigatorio.getIdeDocumentoObrigatorio().equals(docForm.getIdeDocumentoObrigatorio())) {
			//	return true;
			//}
		}
		return false;
	}

	private boolean isArquivoDocUploaded(DocumentoObrigatorio docObrigatorio) {
		for (DocumentoObrigatorioRequerimento docForm : arqvsDocsEnviados) {
			//if (docObrigatorio.getIdeDocumentoObrigatorio().equals(docForm.getIdeDocumentoObrigatorio())) {
			//	return true;
			//}
		}
		return false;
	}

	private Boolean isNomeArquivoSemelhante(StreamedContent file, Collection<DocumentoObrigatorioRequerimento> docsObrigatorio) {
		for (DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento : docsObrigatorio) {
			if (!Util.isNull(documentoObrigatorioRequerimento.getFile()) && file.getName().equalsIgnoreCase(documentoObrigatorioRequerimento.getFile().getName())) {
				return true;
			}
		}
		return false;
	}

	public void salvarDocumentoObrigatorios() throws Exception {
		//docObrigatorioRequerimentoFacadeService.deletar(this.arqvsADeletar);
		this.arqvsObrigatoriosUpoloaded = new ArrayList<DocumentoObrigatorioRequerimento>();
		usuario = ContextoUtil.getContexto().getUsuarioLogado();
		this.saveAndUploadFormularios();
		this.saveAndUploadDocumentos();
		Boolean isArchiveChanged = Boolean.FALSE;
		for (DocumentoObrigatorioRequerimento lDocumentoObrigatorioRequerimento : arqvsObrigatoriosUpoloaded) {
			if (Util.isNullOuVazio(lDocumentoObrigatorioRequerimento) && lDocumentoObrigatorioRequerimento.getArquivoChanged()) {
				isArchiveChanged = Boolean.TRUE;
				break;
			}
		}
		docObrigatorioRequerimentoFacadeService.salvarOuAtualizar(arqvsObrigatoriosUpoloaded, isArchiveChanged, enquadramento, arqvsDocumento, arqvsDocumentoIdentificacao);
		JsfUtil.addSuccessMessage(BUNDLE.getString("documento_obrigatorio_msg_envio_documentacao_efetuado_sucesso"));
	}

	private void saveAndUploadFormularios() throws Exception {
		if (!Util.isNullOuVazio(arqvsFormsEnviados)) {
			for (DocumentoObrigatorioRequerimento docObrigatorioReq : arqvsFormsEnviados) {
				docObrigatorioReq.setIdePessoaUpload(usuario.getPessoaFisica().getPessoa());
				docObrigatorioReq.setTipoDocumento(DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.name());
				docObrigatorioReq.setIdeRequerimento(enquadramento.getIdeRequerimento());
				arqvsObrigatoriosUpoloaded.add(docObrigatorioReq);
			}
		}
	}

	public String mostrarTamanhoArquivoDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao) {
		File arquivo = null;
		if (!Util.isNull(documentoIdentificacao.getDscCaminhoArquivo())) {
			arquivo = new File(documentoIdentificacao.getDscCaminhoArquivo());
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
			}
		}
		return "";
	}

	private void saveAndUploadDocumentos() throws Exception {
		if (!Util.isNullOuVazio(arqvsDocsEnviados)) {
			for (DocumentoObrigatorioRequerimento docObrigatorioReq : arqvsDocsEnviados) {
				for (DocumentoObrigatorioRequerimento lVwDocumentoIdentificacaoObrigatorio : arqvsDocumento) {
					if (lVwDocumentoIdentificacaoObrigatorio.getIdeDocumentoObrigatorioRequerimento().equals(docObrigatorioReq.getIdeDocumentoObrigatorioRequerimento())) {
						docObrigatorioReq.setDscCaminhoArquivo(lVwDocumentoIdentificacaoObrigatorio.getDscCaminhoArquivo());
						break;
					}
				}
				docObrigatorioReq.setIdePessoaUpload(usuario.getPessoaFisica().getPessoa());
				docObrigatorioReq.setTipoDocumento(DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.name());
				docObrigatorioReq.setIdeRequerimento(enquadramento.getIdeRequerimento());
				arqvsObrigatoriosUpoloaded.add(docObrigatorioReq);
			}
		}
	}

	private Integer getIdDocumentoObrigatorioRequerimento(DocumentoObrigatorio docObrigatorio) {
		/*for (DocumentoObrigatorioRequerimento docReq : docObrigatorio.getDocumentoObrigatorioRequerimentoCollection()) {
			//if (docObrigatorio.equals(docReq.getIdeDocumentoObrigatorio()) && docReq.getIdeRequerimento().equals(this.requerimentoUnico.getRequerimento())) {
			//	return docReq.getIdeDocumentoObrigatorioRequerimento();
			//}
		}*/
		return null;
	}


	public void salvarUploadExtratoHidrologico(Requerimento requerimento, DocumentoObrigatorio docObrigatorio) {
		try {
			if(!Util.isNullOuVazio(fileUploadEvent)) {
				if (!Util.isNullOuVazio(docObrigatorioFceIntervencao.getDscCaminhoArquivo())) {
					gerenciaArquivoService.deletarArquivo(docObrigatorioFceIntervencao.getDscCaminhoArquivo());
				}
				StreamedContent file = new DefaultStreamedContent(new DataInputStream(new BufferedInputStream(fileUploadEvent.getFile().getInputstream())), fileUploadEvent.getFile().getContentType(), fileUploadEvent.getFile().getFileName());
				docObrigatorioFceIntervencao.setFileUpload(fileUploadEvent);
				docObrigatorioFceIntervencao.setFile(file);
				docObrigatorioFceIntervencao.setFileSize(fileUploadEvent.getFile().getSize());
				docObrigatorioFceIntervencao.setIdeDocumentoObrigatorio(docObrigatorio);
				docObrigatorioFceIntervencao.setIdeRequerimento(requerimento);
				docObrigatorioRequerimentoService.salvarOuAtualizar(docObrigatorioFceIntervencao);
				atualizarDocObrigatorioRequerimento(docObrigatorioFceIntervencao, requerimento, docObrigatorio);
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(BUNDLE.getString("lac_dadosGerais_mensagens_erro002") + " o Upload do Extrato de estudo hidrológico.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}

	private boolean isEdicaoBarragem(){
		return !Util.isNullOuVazio(docObrigatorioFceIntervencao.getIdeDocumentoObrigatorioRequerimento());
	}

	/**
	 * Método criado para burlar a validação que não será feita pela ATEND.
	 * @author eduardo.fernandes
	 * @deprecated
	 */
	private void validacaoFake(){
		docObrigatorioFceIntervencao.setIndDocumentoValidado(true);
		docObrigatorioFceIntervencao.setDtcValidacao(new Date());
		docObrigatorioFceIntervencao.setIdePessoaValidacao(usuarioLogado.getPessoaFisica().getPessoa());
	}

	public void trataUploadExtratoHidrologico(FileUploadEvent event) {
		preparaListaAndDoc();
		fileUploadEvent = event;
		caminhoArquivo = FileUploadUtil.Enviar(fileUploadEvent, DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.toString());
		listaArquivo.add(FileUploadUtil.getFileName(caminhoArquivo));
		docObrigatorioFceIntervencao.setDscCaminhoArquivo(caminhoArquivo);
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
	}

	private void preparaListaAndDoc(){
		iniciarLista();
		iniciarDoc();
	}

	private void iniciarLista(){
		if(Util.isNullOuVazio(listaArquivo)){
			listaArquivo = new ArrayList<String>();
		} else {
			listaArquivo.clear();
		}
	}

	private void iniciarDoc(){
		docObrigatorioFceIntervencao = new DocumentoObrigatorioRequerimento();
	}

	public boolean isTemArquivo() {
		return !Util.isNullOuVazio(listaArquivo);
	}

	public void limparListaArquivo(){
		if(!Util.isNullOuVazio(listaArquivo)){
			fileUploadEvent = null;
			listaArquivo.clear();
		}
	}

	public StreamedContent getExtratoUpado() {
		try {
			arquivoBaixar = gerenciaArquivoService.getContentFile(docObrigatorioFceIntervencao.getDscCaminhoArquivo());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return arquivoBaixar;
	}

	public void carregarExrtatoHidrografico(DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento){
		try {
			docObrigatorioFceIntervencao = docObrigatorioRequerimentoService.buscarDocumentoObrigatorioRequerimentoByIde(ideDocumentoObrigatorioRequerimento);
			iniciarLista();
			listaArquivo.add(docObrigatorioFceIntervencao.getDscCaminhoArquivo());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	/**
	 * @return the arqvsObrigatorios
	 */
	public Collection<DocumentoObrigatorio> getArqvsObrigatorios() {
		return arqvsObrigatorios;
	}

	/**
	 * @param arqvsObrigatorios
	 * the arqvsObrigatorios to set
	 */
	public void setArqvsObrigatorios(List<DocumentoObrigatorio> arqvsObrigatorios) {
		this.arqvsObrigatorios = arqvsObrigatorios;
	}

	public List<DocumentoObrigatorioRequerimento> getArqvsFormsEnviados() {
		return arqvsFormsEnviados;
	}

	public void setArqvsFormsEnviados(List<DocumentoObrigatorioRequerimento> arqvsFormsEnviados) {
		this.arqvsFormsEnviados = arqvsFormsEnviados;
	}

	public List<DocumentoObrigatorioRequerimento> getArqvsDocsEnviados() {
		return arqvsDocsEnviados;
	}

	public void setArqvsDocsEnviados(List<DocumentoObrigatorioRequerimento> arqvsDocsEnviados) {
		this.arqvsDocsEnviados = arqvsDocsEnviados;
	}

	public List<DocumentoObrigatorioRequerimento> getArqvsADeletar() {
		return arqvsADeletar;
	}

	public void setArqvsADeletar(List<DocumentoObrigatorioRequerimento> arqvsADeletar) {
		this.arqvsADeletar = arqvsADeletar;
	}

	public DocumentoObrigatorio getDocObrigatorioFormSelected() {
		return docObrigatorioFormSelected;
	}

	public void setDocObrigatorioFormSelected(DocumentoObrigatorio docObrigatorioFormSelected) {
		this.docObrigatorioFormSelected = docObrigatorioFormSelected;
	}

	public DocumentoObrigatorio getDocObrigatorioDocSelected() {
		return docObrigatorioDocSelected;
	}

	public void setDocObrigatorioDocSelected(DocumentoObrigatorio docObrigatorioDocSelected) {
		this.docObrigatorioDocSelected = docObrigatorioDocSelected;
	}

	/**
	 * @return the listaDocumentos
	 */
	public Collection<DocumentoObrigatorio> getListaDocumentos() {
		return listaDocumentos;
	}

	/**
	 * @param listaDocumentos
	 * the listaDocumentos to set
	 */
	public void setListaDocumentos(List<DocumentoObrigatorio> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}

	/**
	 * @return the arqvsObrigatoriosUpoloaded
	 */
	public List<DocumentoObrigatorioRequerimento> getArqvsObrigatoriosUpoloaded() {
		return arqvsObrigatoriosUpoloaded;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 * the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the requerimentoService
	 */
	public RequerimentoUnicoService getRequerimentoService() {
		return requerimentoUnicoService;
	}

	/**
	 * @param requerimentoService
	 * the requerimentoService to set
	 */
	public void setRequerimentoService(RequerimentoUnicoService requerimentoUnicoService) {
		this.requerimentoUnicoService = requerimentoUnicoService;
	}

	public List<DocumentoObrigatorio> getArqvsFormsObrigatorios() {
		return arqvsFormsObrigatorios;
	}

	public void setArqvsFormsObrigatorios(List<DocumentoObrigatorio> arqvsFormsObrigatorios) {
		this.arqvsFormsObrigatorios = arqvsFormsObrigatorios;
	}

	/**
	 * @return the docObrigatorioService
	 */
	public DocumentoAtoAmbientalService getDocObrigatorioService() {
		return docObrigatorioService;
	}

	/**
	 * @param docObrigatorioService
	 * the docObrigatorioService to set
	 */
	public void setDocObrigatorioService(DocumentoAtoAmbientalService docObrigatorioService) {
		this.docObrigatorioService = docObrigatorioService;
	}

	/**
	 * @param arqvsObrigatoriosUpoloaded
	 * the arqvsObrigatoriosUpoloaded to set
	 */
	public void setArqvsObrigatoriosUpoloaded(List<DocumentoObrigatorioRequerimento> arqvsObrigatoriosUpoloaded) {
		this.arqvsObrigatoriosUpoloaded = arqvsObrigatoriosUpoloaded;
	}

	public Collection<DocumentoObrigatorio> getListaFormularios() {
		return listaFormularios;
	}

	public void setListaFormularios(List<DocumentoObrigatorio> listaFormularios) {
		this.listaFormularios = listaFormularios;
	}

	public Enquadramento getEnquadramento() {
		return enquadramento;
	}

	public void setEnquadramento(Enquadramento enquadramento) {
		this.enquadramento = enquadramento;
	}

	public RequerimentoUnico getRequerimentoUnico() {
		return requerimentoUnico;
	}

	public void setRequerimentoUnico(RequerimentoUnico requerimentoUnico) {
		this.requerimentoUnico = requerimentoUnico;
	}

	public DocumentoObrigatorio getDocDownload() {
		return docDownload;
	}

	public void setDocDownload(DocumentoObrigatorio docDownload) {
		this.docDownload = docDownload;
	}

	public StreamedContent getArquivoBaixar() {
		/*try {
			arquivoBaixar = gerenciaArquivoService.getContentFile(docDownload.getDscCaminhoArquivo());
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}*/
		return arquivoBaixar;
	}

	public void setArquivoBaixar(StreamedContent arquivoBaixar) {
		this.arquivoBaixar = arquivoBaixar;
	}

	public List<DocumentoIdentificacao> getArqvsDocumentoIdentificacao() {
		return arqvsDocumentoIdentificacao;
	}

	public void setArqvsDocumentoIdentificacao(List<DocumentoIdentificacao> arqvsDocumentoIdentificacao) {
		this.arqvsDocumentoIdentificacao = arqvsDocumentoIdentificacao;
	}

	public ParametroService getParametroService() {
		return parametroService;
	}

	public void setParametroService(ParametroService parametroService) {
		this.parametroService = parametroService;
	}

	public List<DocumentoObrigatorioRequerimento> getArqvsDocumento() {
		return arqvsDocumento;
	}

	public void setArqvsDocumento(List<DocumentoObrigatorioRequerimento> arqvsDocumento) {
		this.arqvsDocumento = arqvsDocumento;
	}

	public DocumentoObrigatorioRequerimento getDocumentoObrigatorioSelecionado() {
		return documentoObrigatorioSelecionado;
	}

	public void setDocumentoObrigatorioSelecionado(DocumentoObrigatorioRequerimento documentoObrigatorioSelecionado) {
		this.documentoObrigatorioSelecionado = documentoObrigatorioSelecionado;
	}

	public boolean isLacErb(DocumentoObrigatorio documentoObrigatorio) {
		Boolean isValido = Boolean.FALSE;
		try {
			if (!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())) {
				if (documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_ERB).getDscValor())) {
					isValido = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return isValido;
	}

	public boolean isLacPosto(DocumentoObrigatorio documentoObrigatorio) {
		Boolean isValido = Boolean.FALSE;
		try {
			if (!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())) {
				if (documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_POSTO).getDscValor())) {
					isValido = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return isValido;
	}

	public boolean isLacTransportadora(DocumentoObrigatorio documentoObrigatorio) {
		Boolean isValido = Boolean.FALSE;
		try {
			if (!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())) {
				if (documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_TRANSPORTADORA).getDscValor())) {
					isValido = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return isValido;
	}
	public boolean isFceAsv(DocumentoObrigatorio documentoObrigatorio) {
		Boolean isValido = Boolean.FALSE;
		try {
			if (!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())) {
				if (documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_FCE_ASV).getDscValor())) {
					isValido = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return isValido;
	}

	public boolean isAbastecimentoHumano(DocumentoObrigatorio documentoObrigatorio) {
		Boolean isValido = Boolean.FALSE;
		try {
			if (!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())) {
				if (documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_FCE_ABASTECIMENTO_HUMANO).getDscValor())) {
					isValido = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return isValido;
	}


	public boolean isLac(DocumentoObrigatorio documentoObrigatorio) {
		return isLacErb(documentoObrigatorio) || isLacPosto(documentoObrigatorio)|| isLacTransportadora(documentoObrigatorio) ||  isFceAsv(documentoObrigatorio); /** adicionar  quando FCE AGROSSILVOPASTORIL for para produção  || isFceAgrossilvoPastoril(documentoObrigatorio)*/
	}

	public Boolean getFormularioLacCompleto() {
		for (DocumentoObrigatorio documentoObrigatorio : arqvsFormsObrigatorios) {
			if (isLacPosto(documentoObrigatorio) || isLacErb(documentoObrigatorio)) {
				Collection<LacLegislacao> legislacoesAceitasLac;
				try {
					legislacoesAceitasLac = this.lacLegislacaoService.carregarByIdeRequerimentoWithLac(requerimentoUnico.getRequerimento().getIdeRequerimento());
					return !Util.isNullOuVazio(legislacoesAceitasLac);
				} catch (Exception e) {
					return true;
				}
			}
			if(isLacTransportadora(documentoObrigatorio)){
				LacTransporte lacTransporte;
				try {
					lacTransporte = lacTransporteService.buscarLacTransporteByIdeRequerimento(requerimentoUnico.getRequerimento());
					return isLacTransportadoraSalva(lacTransporte) && isFrotaVeiculoCadastrada();
				}
				catch (Exception e) {
					return true;
				}
			}
		}
		return true;
	}
	
	private boolean isLacTransportadoraSalva(LacTransporte lacTransporte){
		return !Util.isNullOuVazio(lacTransporte) && !Util.isNullOuVazio(lacTransporte.getIdeLacTransporte());
	}

	private boolean isFrotaVeiculoCadastrada(){
		try{
			List<Empreendimento> coll = (List<Empreendimento>) empreendimentoService.buscarEmpreendimentoPorRequerimento(requerimentoUnico.getRequerimento());
			Empreendimento empreendimento = empreendimentoService.carregarById(coll.get(0).getIdeEmpreendimento());
			List<EmpreendimentoVeiculo> listEmpreendimentoVeiculos = veiculoService.listarEmpreendimentoVeiculoByEmpreedimento(empreendimento);
			if(Util.isNullOuVazio(listEmpreendimentoVeiculos)){
				return false;
			}
			else {
				return true;
			}
		}
		catch (Exception e) {
			return false;
		}
	}

	public Boolean getRenderedFormulario() {
		renderedFormulario = true;
		if (Util.isNull(arqvsFormsObrigatorios) || arqvsFormsObrigatorios.isEmpty()) {
			renderedFormulario = false;
		}
		return renderedFormulario;
	}

	public void setRenderedFormulario(Boolean renderedFormulario) {
		this.renderedFormulario = renderedFormulario;
	}

	public DocumentoIdentificacao getDocumentoIdentificacaoSelecionado() {
		return documentoIdentificacaoSelecionado;
	}

	public void setDocumentoIdentificacaoSelecionado(DocumentoIdentificacao documentoIdentificacaoSelecionado) {
		this.documentoIdentificacaoSelecionado = documentoIdentificacaoSelecionado;
	}

	public List<AtoAmbiental> getAtosAmbientais() {
		return atosAmbientais;
	}

	public void setAtosAmbientais(List<AtoAmbiental> atosAmbientais) {
		this.atosAmbientais = atosAmbientais;
	}

	public boolean isExibirArqvsDocumentoIdentificacao(){
		return !Util.isNullOuVazio(this.arqvsDocumentoIdentificacao);
	}
	public boolean isExibirLista(Collection<DocumentoObrigatorioRequerimento> docs){
		return !Util.isNullOuVazio(docs);
	}

	public List<String> getListaArquivo() {
		return listaArquivo;
	}

	public void setListaArquivo(List<String> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public DocumentoObrigatorioRequerimento getDocObrigatorioFceIntervencao() {
		return docObrigatorioFceIntervencao;
	}

	public void setDocObrigatorioFceIntervencao(
			DocumentoObrigatorioRequerimento docObrigatorioFceIntervencao) {
		this.docObrigatorioFceIntervencao = docObrigatorioFceIntervencao;
	}

	public DocumentoObrigatorioRequerimento getDocObrigatorioFceIntervencaoTemporaria() {
		return docObrigatorioFceIntervencaoTemporaria;
	}

	public void setDocObrigatorioFceIntervencaoTemporaria(
			DocumentoObrigatorioRequerimento docObrigatorioFceIntervencaoTemporaria) {
		this.docObrigatorioFceIntervencaoTemporaria = docObrigatorioFceIntervencaoTemporaria;
	}

}
