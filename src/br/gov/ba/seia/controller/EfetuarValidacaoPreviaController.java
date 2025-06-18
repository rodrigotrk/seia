package br.gov.ba.seia.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.RequerimentoUnicoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.DocumentoRepresentacaoRequerimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.VwDocumentoIdentificacaoObrigatorio;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.facade.ProcessoRequerimentoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.DocumentoRepresentacaoRequerimentoService;
import br.gov.ba.seia.service.EmailService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.service.VwDocumentoIdentificacaoObrigatorioService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@ViewScoped
@Named("efetuarValidacaoPreviaController")
public class EfetuarValidacaoPreviaController extends DocumentoController {

	private List<DocumentoObrigatorioRequerimento> arqvsDocumento;
	private DocumentoObrigatorioRequerimento fileUploaded;
	private Usuario usuarioLogado;
	private DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento;
	@EJB
	private DocumentoObrigatorioRequerimentoService docObrigatorioRequerimentoService;
	@EJB
	private VwDocumentoIdentificacaoObrigatorioService docRequerimentoService;
	@EJB
	private AreaService areaService;
	@EJB
	private PessoaService pessoaService;
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;
	@EJB
	private RequerimentoService requerimentoService;
	private Area areaSelecionada;
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private DocumentoRepresentacaoRequerimentoService documentoRepresentacaoRequerimentoService;
	@EJB
	private RepresentanteLegalService representanteLegalService;
	@EJB
	private ProcessoRequerimentoServiceFacade processoRequerimentoServiceFacade;
	private List<Area> areasRequerimento;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	private StreamedContent fileDownload;
	private VwDocumentoIdentificacaoObrigatorio docDownload;
	@EJB
	private RequerimentoUnicoService requerimentoUnicoSevice;
	@EJB
	private DocumentoIdentificacaoService documentoIdenficacaoService;
	
	@EJB
	private EmailService emailService;
	
	private DocumentoIdentificacao documentoIdentificacaoSelecionado;
	
	private DocumentoRepresentacaoRequerimento documentoRepresentacaoSelecionado;
	
	private Requerimento requerimento;

	private List<AtoAmbiental> atosAmbientais;

	private List<RequerimentoUnico> requerimentosUnicosDispensaveis;
	
	private List<DocumentoRepresentacaoRequerimento> arqvsRepresentacao;
	
	private Pessoa pessoaRequerente;
	
	private String email;
	private String tituloEmail;

	private List<DocumentoIdentificacao> arqvsDocumentoIdentificacao;

	public void carregarTela(RequerimentoUnicoDTO dto) {
		usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
		requerimento = new Requerimento();
		
		try {
			RequerimentoUnico requerimentoUnico = dto.getRequerimentoUnico();
			requerimentoUnico.getRequerimento().setUltimoEmpreendimento(dto.getEmpreendimento());
			requerimento = requerimentoUnico.getRequerimento(); 
			this.carregarDocumentoIdentificacao(requerimentoUnico);
			this.carregarDocumentosObrigatorios(requerimentoUnico);
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarDocumentoIdentificacao(RequerimentoUnico requerimentoUnico) throws Exception {
		arqvsDocumentoIdentificacao = new ArrayList<DocumentoIdentificacao>();
		Requerimento requerimento = requerimentoService.buscarEntidadePorId(new Requerimento(requerimentoUnico.getIdeRequerimentoUnico()));
		pessoaRequerente = null;
		for (RequerimentoPessoa rp : requerimento.getRequerimentoPessoaCollection()) {
			if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {
				pessoaRequerente = rp.getPessoa();
			}
		}
		arqvsDocumentoIdentificacao.addAll(documentoIdentificacaoService.listarDocumentosIdentificacaoPorPessoa(pessoaRequerente));
	}

	private void carregarDocumentosObrigatorios(RequerimentoUnico requerimentoUnico) throws Exception {
		this.arqvsDocumento = docRequerimentoService.buscarDocsFormulariosObrigatorio(requerimentoUnico);
		for (DocumentoObrigatorioRequerimento doc : arqvsDocumento) {

			if (!Util.isNull(doc.getDscCaminhoArquivo())) {
				File arquivo = new File(doc.getDscCaminhoArquivo());
				doc.setFileSize(arquivo.length());
				arquivo = null;
			}
		}
		
		this.requerimentosUnicosDispensaveis = this.requerimentoUnicoSevice.buscarRequerimentoUnicoDispensavelDePagamento(requerimentoUnico.getIdeRequerimentoUnico());
		this.consultarAreas(requerimentoUnico);
		
		atosAmbientais = new ArrayList<AtoAmbiental>();
	}



	public void salvar() {
		try {
			
			if(Util.isNull(requerimento.getIndEstadoEmergencia())){
				JsfUtil.addErrorMessage("O Campo 'O empreendimento se enquadra nas características presentes no decreto n° xxxx?' é de preenchimento obrigatório.");
				return;
			}
			
			
			Pessoa usuarioValidacao = pessoaService.carregarGet(usuarioLogado.getIdePessoaFisica());
			for (DocumentoObrigatorioRequerimento arquivoRequerimento : arqvsDocumento) {
				configuraDocumentoObrigatorioRequerimento(usuarioValidacao, arquivoRequerimento);
				docObrigatorioRequerimentoService.salvarOuAtualizar(arquivoRequerimento);
			}
			
			for (DocumentoRepresentacaoRequerimento documentoRepresentacaoRequerimento : arqvsRepresentacao) {
				configuraDocumentoRepresentacaoRequerimento(usuarioValidacao, documentoRepresentacaoRequerimento);
				documentoRepresentacaoRequerimentoService.salvarOuAtualizar(documentoRepresentacaoRequerimento);
			}
			
			requerimentoService.atualizarIndEmergencia(requerimento);
			
			JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("enquadramento_msg_sucesso_validacao"));
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao salvar a validação prévia.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void configuraDocumentoObrigatorioRequerimento(Pessoa usuarioValidacao, DocumentoObrigatorioRequerimento arquivoRequerimento) throws Exception {
		if (arquivoRequerimento.getIndDocumentoValidado() & Util.isNull(arquivoRequerimento.getDtcValidacao())) {
			arquivoRequerimento.setDtcValidacao(new Date());
			arquivoRequerimento.setIdePessoaValidacao(usuarioValidacao);
		}
		
		if (arquivoRequerimento.getIndDocumentoValidado()) {
			arquivoRequerimento.setIdePessoaValidacao(usuarioValidacao);
		}
		
		if (Util.isNull(requerimento)) {
			requerimento = requerimentoService.buscarEntidadePorId(arquivoRequerimento.getIdeRequerimento());
		}
	}
	
	private void configuraDocumentoRepresentacaoRequerimento(Pessoa usuarioValidacao, DocumentoRepresentacaoRequerimento arquivoRequerimento) throws Exception {
		if (arquivoRequerimento.getIndDocumentoValidado() & Util.isNull(arquivoRequerimento.getDtcValidacao())) {
			arquivoRequerimento.setDtcValidacao(new Date());
			arquivoRequerimento.setIdePessoaValidacao(usuarioValidacao);
		}
		
		if (arquivoRequerimento.getIndDocumentoValidado()) {
			arquivoRequerimento.setIdePessoaValidacao(usuarioValidacao);
		}
		
		if (Util.isNull(requerimento)) {
			requerimento = requerimentoService.buscarEntidadePorId(arquivoRequerimento.getIdeRequerimento());
		}
	}

	public StreamedContent getArquivoBaixar() {
		return fileDownload;
	}



	private Boolean verificarConclusaoProcesso(List<RequerimentoUnico> requerimentosUnicosDispensaveis) {
		for (RequerimentoUnico requerimentoUnico : requerimentosUnicosDispensaveis) {
			
			boolean isDispensaOutorga = AtoAmbientalEnum.DOUT.getId().equals(requerimentoUnico.getAtoAmbiental().getIdeAtoAmbiental()) && !Util.isNull(requerimentoUnico.getNumVazaoCaptacao()) && (requerimentoUnico.getNumVazaoCaptacao().doubleValue() > 0 && 
					requerimentoUnico.getNumVazaoCaptacao().doubleValue() < 43.2);
			boolean isPerfuracaoPoco = AtoAmbientalEnum.PERFURACAO_POCO.getId().equals(requerimentoUnico.getAtoAmbiental().getIdeAtoAmbiental());			
			
			if (isPerfuracaoPoco || isDispensaOutorga) {

			}else{
				return false;
			}
			
		}

		return true;
	}

	public boolean isDispensarPagamento() {
		return !Util.isNullOuVazio(requerimentosUnicosDispensaveis) && verificarConclusaoProcesso(requerimentosUnicosDispensaveis);
	}

	public void enviarEmailPendenciaValidacao() {
		try {
			requerimentoUnicoSevice.salvarComunicacao(requerimento, email);
			emailService.enviarEmailsAoRequerente(requerimento, tituloEmail, email);
			JsfUtil.addSuccessMessage("Email enviado com sucesso.");
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void handleFileUploadFormularioObrigatorio(FileUploadEvent event) {
		try {
			StreamedContent file = new DefaultStreamedContent(new DataInputStream(new BufferedInputStream(event.getFile().getInputstream())), event.getFile()
					.getContentType(), event.getFile().getFileName());
			fileUploaded.setFileUpload(event);
			fileUploaded.setFile(file);
			fileUploaded.setFileSize(event.getFile().getSize());
			fileUploaded.setIsArquivoChanged(Boolean.TRUE);
			JsfUtil.addSuccessMessage("Arquivo carregado com sucesso!");
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void consultarAreas(RequerimentoUnico pRequerimento) {
		try {
			areasRequerimento = areaService.listarPorRequerimento(pRequerimento,null);
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}



	public void handleFileUploadDocumentoIdentificacao(FileUploadEvent event) {
		try {
			documentoIdenficacaoService.atualizarDocumentoIdenticacao(documentoIdentificacaoSelecionado, event.getFile());
			JsfUtil.addSuccessMessage("Arquivo carregado com sucesso!");
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao carregar o arquivo.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}
	
	public void handleFileUploadDocumentoRepresentacao(FileUploadEvent event) {
		try {
			StreamedContent file = new DefaultStreamedContent(new DataInputStream(new BufferedInputStream(event.getFile().getInputstream())), event.getFile()
					.getContentType(), event.getFile().getFileName());
			documentoRepresentacaoSelecionado.setFileUpload(event);
			documentoRepresentacaoSelecionado.setFile(file);
			documentoRepresentacaoSelecionado.setFileSize(event.getFile().getSize());
			documentoRepresentacaoSelecionado.setArquivoChanged(Boolean.TRUE);
			atualizarDocRepresentacaoRequerimento(documentoRepresentacaoSelecionado, requerimento);
			JsfUtil.addSuccessMessage("Arquivo carregado com sucesso!");
		} 
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao carregar o arquivo.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void atualizarDocRepresentacaoRequerimento(DocumentoRepresentacaoRequerimento docObrigatorioRequerimento, Requerimento requerimento) throws Exception {
		DocumentoRepresentacaoRequerimento documentoRepresentacaoRequerimento = documentoRepresentacaoRequerimentoService
				.obterDocumentoRepReqCriteria(docObrigatorioRequerimento);
		documentoRepresentacaoRequerimento.setFileUpload(docObrigatorioRequerimento.getFileUpload());
		documentoRepresentacaoRequerimento.setFile(docObrigatorioRequerimento.getFile());
		documentoRepresentacaoRequerimento.setFileSize(docObrigatorioRequerimento.getFileSize());
		documentoRepresentacaoRequerimento.setArquivoChanged(docObrigatorioRequerimento.getArquivoChanged());
		String fileName = getFileNameRepresentacao(documentoRepresentacaoRequerimento);
		if (Util.isNull(fileName)) {
			documentoRepresentacaoRequerimento.setDscCaminhoArquivo(DiretorioArquivoEnum.PESSOA + File.separator + getFileName(fileUploaded));
		}
		documentoRepresentacaoRequerimento.setDscCaminhoArquivo(DiretorioArquivoEnum.PESSOA+ documentoRepresentacaoRequerimento.getIdeDocumentoRepresentacaoRequerimento().toString() + File.separator + getFileNameRepresentacao(documentoRepresentacaoRequerimento));
		if (!Util.isNull(documentoRepresentacaoRequerimento.getIdeRepresentanteLegal())) {
			RepresentanteLegal representanteLegal = documentoRepresentacaoRequerimento.getIdeRepresentanteLegal();
			representanteLegal = representanteLegalService.buscarPorIdViaCriteria(representanteLegal);
			representanteLegal.setDscCaminhoRepresentacao(documentoRepresentacaoRequerimento.getDscCaminhoArquivo());
			documentoRepresentacaoRequerimentoService.uploadFiles(documentoRepresentacaoRequerimento);			
			representanteLegalService.salvarRepresentanteLegal(representanteLegal);
		}
		
		documentoRepresentacaoSelecionado = documentoRepresentacaoRequerimento;
		int indexOf = this.arqvsRepresentacao.indexOf(documentoRepresentacaoRequerimento);
		this.arqvsRepresentacao.set(indexOf, documentoRepresentacaoSelecionado);
	}
	
	public void excluirValidacao(){
		this.documentoObrigatorioRequerimento.setIndDocumentoValidado(false);
	}
	
	public void excluirValidacaoRepresentacao(){
		this.documentoRepresentacaoSelecionado.setIndDocumentoValidado(false);
	}

	public List<DocumentoObrigatorioRequerimento> getArqvsDocumento() {
		return arqvsDocumento;
	}

	public void setArqvsDocumento(List<DocumentoObrigatorioRequerimento> arqvsDocumento) {
		this.arqvsDocumento = arqvsDocumento;
	}

	public List<DocumentoIdentificacao> getArqvsDocumentoIdentificacao() {
		return arqvsDocumentoIdentificacao;
	}

	public void setArqvsDocumentoIdentificacao(List<DocumentoIdentificacao> arqvsDocumentoIdentificacao) {
		this.arqvsDocumentoIdentificacao = arqvsDocumentoIdentificacao;
	}

	public DocumentoObrigatorioRequerimento getFileUploaded() {
		return fileUploaded;
	}

	public void setFileUploaded(DocumentoObrigatorioRequerimento fileUploaded) {
		this.fileUploaded = fileUploaded;
	}

	public VwDocumentoIdentificacaoObrigatorio getDocDownload() {
		return docDownload;
	}

	public void setDocDownload(VwDocumentoIdentificacaoObrigatorio docDownload) {
		this.docDownload = docDownload;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	/**
	 * @param usuarioLogado
	 *            the usuarioLogado to set
	 */
	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Area getAreaSelecionada() {
		return areaSelecionada;
	}

	public void setAreaSelecionada(Area areaSelecionada) {
		this.areaSelecionada = areaSelecionada;
	}

	public StreamedContent getFileDownload() {
		return fileDownload;
	}

	public void setFileDownload(StreamedContent fileDownload) {
		this.fileDownload = fileDownload;
	}

	public DocumentoRepresentacaoRequerimentoService getDocumentoRepresentacaoRequerimentoService() {
		return documentoRepresentacaoRequerimentoService;
	}

	public void setDocumentoRepresentacaoRequerimentoService(DocumentoRepresentacaoRequerimentoService documentoRepresentacaoRequerimentoService) {
		this.documentoRepresentacaoRequerimentoService = documentoRepresentacaoRequerimentoService;
	}

	public List<Area> getAreasRequerimento() {
		return areasRequerimento;
	}

	public void setAreasRequerimento(List<Area> areasRequerimento) {
		this.areasRequerimento = areasRequerimento;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public List<AtoAmbiental> getAtosAmbientais() {
		return atosAmbientais;
	}

	public void setAtosAmbientais(List<AtoAmbiental> atosAmbientais) {
		this.atosAmbientais = atosAmbientais;
	}

	public List<DocumentoRepresentacaoRequerimento> getArqvsRepresentacao() {
		return arqvsRepresentacao;
	}

	public void setArqvsRepresentacao(List<DocumentoRepresentacaoRequerimento> arqvsRepresentacao) {
		this.arqvsRepresentacao = arqvsRepresentacao;
	}

	public DocumentoRepresentacaoRequerimento getDocumentoRepresentacaoSelecionado() {
		return documentoRepresentacaoSelecionado;
	}

	public void setDocumentoRepresentacaoSelecionado(DocumentoRepresentacaoRequerimento documentoRepresentacaoSelecionado) {
		this.documentoRepresentacaoSelecionado = documentoRepresentacaoSelecionado;
	}

	public Pessoa getPessoaRequerente() {
		return pessoaRequerente;
	}

	public void setPessoaRequerente(Pessoa pessoaRequerente) {
		this.pessoaRequerente = pessoaRequerente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTituloEmail() {
		return tituloEmail;
	}

	public void setTituloEmail(String tituloEmail) {
		this.tituloEmail = tituloEmail;
	}

	public DocumentoObrigatorioRequerimento getDocumentoObrigatorioRequerimento() {
		return documentoObrigatorioRequerimento;
	}

	public void setDocumentoObrigatorioRequerimento(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) {
		this.documentoObrigatorioRequerimento = documentoObrigatorioRequerimento;
	}

}
