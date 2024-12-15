package br.gov.ba.seia.controller;

import static br.gov.ba.seia.util.AtoDeclaratorioUtil.isDocumentoDIAP;
import static br.gov.ba.seia.util.AtoDeclaratorioUtil.isDocumentoDQC;
import static br.gov.ba.seia.util.AtoDeclaratorioUtil.isDocumentoRFP;
import static br.gov.ba.seia.util.AtoDeclaratorioUtil.isFormularioAtoDeclaratorio;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.dto.DocumentoObrigatorioEnquadramentoDTO;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.DocumentoIdentificacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioReenquadramento;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.DocumentoRepresentacaoRequerimento;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoVeiculo;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Lac;
import br.gov.ba.seia.entity.LacLegislacao;
import br.gov.ba.seia.entity.LacTransporte;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.ProcuradorPfEmpreendimento;
import br.gov.ba.seia.entity.ProcuradorRepEmpreendimento;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.TramitacaoReenquadramentoProcesso;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.exception.SeiaRuntimeException;
import br.gov.ba.seia.exception.SeiaTramitacaoRequerimentoRuntimeException;
import br.gov.ba.seia.facade.AtoDeclaratorioFacade;
import br.gov.ba.seia.facade.EnvioDocumentoServiceFacade;
import br.gov.ba.seia.facade.FceServiceFacade;
import br.gov.ba.seia.facade.TramitacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.DocumentoObrigatorioInterface;
import br.gov.ba.seia.service.DocumentoIdentificacaoRequerimentoService;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.DocumentoObrigatorioReequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.DocumentoRepresentacaoRequerimentoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EmpreendimentoVeiculoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.LacLegislacaoService;
import br.gov.ba.seia.service.LacTransporteService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.ProcuradorPfEmpreendimentoService;
import br.gov.ba.seia.service.ProcuradorRepEmpreendimentoService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceUtil;

@Named("envioDocumentoController")
@ViewScoped
public class EnvioDocumentoController {

	private Collection<DocumentoObrigatorio> formularios;

	private Collection<DocumentoIdentificacaoRequerimento> documentosIdentificacao;

	private Collection<DocumentoRepresentacaoRequerimento> documentosRepresentacao;

	private DocumentoObrigatorioInterface documentoObrigatorioSelecionado;

	private Requerimento requerimento;

	private DocumentoIdentificacaoRequerimento documentoIdentificacaoRequerimentoSelecionado;

	private DocumentoRepresentacaoRequerimento documentoRepresentacaoRequerimentoSelecionado;

	@EJB
	private EnvioDocumentoServiceFacade envioDocumentoServiceFacade;
	
	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;

	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private ProcuradorRepEmpreendimentoService procuradorRepEmpreendimentoService;
	
	@EJB
	private ProcuradorPfEmpreendimentoService procuradorPfEmpreendimentoService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private DocumentoRepresentacaoRequerimentoService documentoRepresentacaoRequerimentoService;

	@EJB
	private DocumentoIdentificacaoRequerimentoService documentoIdentificacaoRequerimentoService;

	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;

	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioRequerimentoService;

	@EJB
	private RequerimentoService requerimentoService;

	@EJB
	private ParametroService parametroService;

	@EJB
	private LacLegislacaoService lacLegislacaoService;
	
	@EJB
	private LacTransporteService lacTransporteService;

	@EJB
	private EmpreendimentoService empreendimentoService;

	@EJB
	private EmpreendimentoVeiculoService veiculoService;

	// FCE's
	@EJB
	private FceServiceFacade fceServiceFacade;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private AtoDeclaratorioFacade atosDeclaratoriosFacade;
	
	@EJB
	private DocumentoObrigatorioReequerimentoService documentoObrigatorioReequerimentoService;
	
	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;

	private List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramento;
	private List<DocumentoObrigatorioInterface> listaDocumentoObrigatorioEnquadramentoAntigo;
	private boolean isRequerimentoAntigoEnquadradoNaV1;
	private TramitacaoRequerimento tramitacaoRequerimento;
	private TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcesso;
	
	private Boolean lacErb;
	private Boolean lacTransporte;
	private Boolean lacPosto;
	private Boolean documentoPresente;

	private ProcessoReenquadramentoDTO processoReenquadramentoDTO; 
	
	public void load(Requerimento requerimento) {

		try {

			Integer ideRequerimento = requerimento.getIdeRequerimento();
			this.requerimento = requerimentoService.carregarDadosBasicos(ideRequerimento);
			
			formularios = documentoObrigatorioService.carregarFormulariosByRequerimento(ideRequerimento);
			carregarDocumentosObrigatorios(ideRequerimento);

			isRequerimentoAntigoEnquadradoNaV1 = verificarRequerimentoAntigoEnquadradoNaV1();

			carregarDocumentosIdentificacao(ideRequerimento);
			carregarDocumentosRepresentacao(ideRequerimento);
			removerFcesDuplicados();

			setTramitacaoRequerimento(tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(ideRequerimento));
			
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void loadDocumento(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
		load(processoReenquadramentoDTO.getRequerimento());
		carregarReenquadramento(processoReenquadramentoDTO);
		setTramitacaoReenquadramentoProcesso(tramitacaoReenquadramentoProcessoServiceFacade.buscarUltimaTramitacaoPorReenquadramentoProcesso(processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento()));
	}

	private void carregarReenquadramento(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		try {
			listaDocumentoObrigatorioEnquadramento = documentoObrigatorioService.listaDocumentoObrigatorioEnquadramentoDTOReenquadramento(processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento(), false);
			formularios = documentoObrigatorioService.carregarFormulariosByProcessoReenquadramento(processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento());
			listaDocumentoObrigatorioEnquadramentoAntigo = null;
			isRequerimentoAntigoEnquadradoNaV1 = false;
			documentosIdentificacao = null;
			documentosRepresentacao = null;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public String gerarNomeOperacaoTela(){
		if (isReenquadramento()){
			return  "processo reenquadramento";
		}
		return "requerimento";
	}

	private boolean verificarRequerimentoAntigoEnquadradoNaV1() {
		if(!Util.isNull(listaDocumentoObrigatorioEnquadramentoAntigo)) {
			for (DocumentoObrigatorioInterface doc : listaDocumentoObrigatorioEnquadramentoAntigo) {
				if(doc.getIdeEnquadramentoAtoAmbiental() == null) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Método que verifica se o {@link DocumentoObrigatorio} é para ser preenchido através do Sistema.
	 * 
	 * <br>
	 * {@link Lac} / {@link Fce} / {@link AtoDeclaratorio}   
	 * 
	 * @author eduardo.fernandes 
	 * @param documentoObrigatorio
	 * @return
	 * @throws Exception
	 */
	public boolean isFormularioDigital(DocumentoObrigatorio documentoObrigatorio){
		return isLac(documentoObrigatorio) || isFce(documentoObrigatorio) || isFormularioAtoDeclaratorio(documentoObrigatorio);
	}

	/**
	 * Método que verifica se o {@link DocumentoObrigatorio} <b>não</b> é para ser preenchido digitalmente. 
	 * 
	 * @author eduardo.fernandes 
	 * @param documentoObrigatorio
	 * @return
	 * @throws Exception
	 */
	public boolean isNotFormularioDigital(DocumentoObrigatorio documentoObrigatorio){
		return !isFormularioDigital(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getDscCaminhoArquivo());
	}
	
	public boolean isAtoRFP(DocumentoObrigatorio documentoObrigatorio){
		return isDocumentoRFP(documentoObrigatorio);
	}
	
	public boolean isAtoDQC(DocumentoObrigatorio documentoObrigatorio){
		return isDocumentoDQC(documentoObrigatorio);
	}
	
	public boolean isAtoDIAP(DocumentoObrigatorio documentoObrigatorio){
		return isDocumentoDIAP(documentoObrigatorio);
	}

	public boolean isAtoDeclaratorio(DocumentoObrigatorio documentoObrigatorio){
		return isAtoRFP(documentoObrigatorio) ||  isAtoDQC(documentoObrigatorio) || isAtoDIAP(documentoObrigatorio);
	}
	
	public boolean isReenquadramento() {
		return !Util.isNull(processoReenquadramentoDTO);
	}

	/**
	 * Método que verifica se existe {@link DocumentoObrigatorio} duplicados na
	 * lista de formularios, caso exista deve-se remover um e manter o outro.
	 * 
	 * @author eduardo.fernandes
	 */
	private void removerFcesDuplicados(){
		List<DocumentoObrigatorio> listaDocsDuplicados = new ArrayList<DocumentoObrigatorio>();
		listaDocsDuplicados.addAll(formularios);
		for(DocumentoObrigatorio documentoObrigatorio : formularios){
			Integer ide = documentoObrigatorio.getIdeDocumentoObrigatorio();
			int qtd = 0;
			for(DocumentoObrigatorio documentoObrigatorioDuplicado : listaDocsDuplicados){
				if(ide.equals(documentoObrigatorioDuplicado.getIdeDocumentoObrigatorio())){
					qtd++;
				}
			}
			if(qtd > 1){
				listaDocsDuplicados.remove(documentoObrigatorio);
			}
		}
		formularios = new ArrayList<DocumentoObrigatorio>();
		formularios.addAll(listaDocsDuplicados);
	}

	private void carregarDocumentosObrigatorios(Integer ideRequerimento) throws Exception {
		listaDocumentoObrigatorioEnquadramento = documentoObrigatorioService.listaDocumentoObrigatorioEnquadramentoDTO(ideRequerimento, false);
		listaDocumentoObrigatorioEnquadramentoAntigo = (List<DocumentoObrigatorioInterface>) documentoObrigatorioRequerimentoService.buscarDocsObrigatorioReqPorRequerimentoUnico(new RequerimentoUnico(ideRequerimento));
	}

	private void carregarDocumentosIdentificacao(Integer ideRequerimento) throws Exception {

		Collection<DocumentoIdentificacaoRequerimento> docsIdentificacaoRequerimento = this.documentoIdentificacaoRequerimentoService.buscarDocsIdentificacao(ideRequerimento, this.requerimento.getRequerente());

		if (Util.isNullOuVazio(docsIdentificacaoRequerimento)) {
			Collection<DocumentoIdentificacao> listaDocumentoIdentificacao =  this.documentoIdentificacaoService.listarDocumentosIdentificacaoRequerente(ideRequerimento);
			if(!Util.isNullOuVazio(listaDocumentoIdentificacao)) {
				docsIdentificacaoRequerimento = new ArrayList<DocumentoIdentificacaoRequerimento>();
				for(DocumentoIdentificacao di : listaDocumentoIdentificacao) {
					DocumentoIdentificacaoRequerimento dir = new DocumentoIdentificacaoRequerimento();
					dir.setIdeDocumentoIdentificacao(di);
					docsIdentificacaoRequerimento.add(dir);
				}

			}
		}
		
		documentosIdentificacao = docsIdentificacaoRequerimento;
	}

	private void carregarDocumentosRepresentacao(Integer ideRequerimento) throws Exception {
		
		Collection<DocumentoRepresentacaoRequerimento> docsRepresentacaoRequerimento = documentoRepresentacaoRequerimentoService.buscarDocsRepresentacaoByRequerimento(ideRequerimento);

			List<Integer> listaIdeEmpreendimento = null;
			List<RepresentanteLegal> listaRepresentanteLegal = null;
			List<ProcuradorRepEmpreendimento> listaProcuradorRepEmpreendimento = null;
			List<ProcuradorPfEmpreendimento> listaProcuradorPfEmpreendimento = null;
			
			listaIdeEmpreendimento = carregarListaIdeEmpreendimento();
 			
			listaRepresentanteLegal = representanteLegalService.getListaRepresentanteLegalPorRequerimento(ideRequerimento);
			
			if(!Util.isNullOuVazio(listaIdeEmpreendimento)) {
			
				listaProcuradorRepEmpreendimento = procuradorRepEmpreendimentoService.listarProcuradorRepEmpreendimentoPorRequerimento(ideRequerimento, listaIdeEmpreendimento);
				
				listaProcuradorPfEmpreendimento = procuradorPfEmpreendimentoService.listarProcuradorPfEmpreendimentoPorRequerimento(ideRequerimento, listaIdeEmpreendimento);
			}
 			
			if(isExisteRepresentante(listaRepresentanteLegal, listaProcuradorRepEmpreendimento, listaProcuradorPfEmpreendimento)) {
				docsRepresentacaoRequerimento = new ArrayList<DocumentoRepresentacaoRequerimento>();
				
				if(!Util.isNullOuVazio(listaRepresentanteLegal)) {
					for(RepresentanteLegal rl : listaRepresentanteLegal) {
						DocumentoRepresentacaoRequerimento doc = new DocumentoRepresentacaoRequerimento();
						doc.setIdeRepresentanteLegal(rl);
						docsRepresentacaoRequerimento.add(doc);
					}
				}
				
				if(!Util.isNullOuVazio(listaProcuradorRepEmpreendimento)) {
					for(ProcuradorRepEmpreendimento pre : listaProcuradorRepEmpreendimento) {
						DocumentoRepresentacaoRequerimento doc = new DocumentoRepresentacaoRequerimento();
						doc.setIdeProcuradorRepEmpreendimento(pre);
						doc.setDscCaminhoArquivo(pre.getDscCaminhoProcuracao());
						docsRepresentacaoRequerimento.add(doc);
					}
				}
				
				if(!Util.isNullOuVazio(listaProcuradorPfEmpreendimento)) {
					for(ProcuradorPfEmpreendimento ppe : listaProcuradorPfEmpreendimento) {
						DocumentoRepresentacaoRequerimento doc = new DocumentoRepresentacaoRequerimento();
						doc.setIdeProcuradorPfEmpreendimento(ppe);
						docsRepresentacaoRequerimento.add(doc);
					}
				}
			}						
		
		
		documentosRepresentacao = docsRepresentacaoRequerimento;
	}

	private List<Integer> carregarListaIdeEmpreendimento()  {
		Collection<Empreendimento> listaEmpreendimento =  empreendimentoService.buscarEmpreendimentoPorRequerimento(requerimento);
		if(!Util.isNullOuVazio(listaEmpreendimento)) {
			List<Integer> listaIde = new ArrayList<Integer>();
			for(Empreendimento e : listaEmpreendimento) {
				listaIde.add(e.getIdeEmpreendimento());
			}
			return listaIde;
		}
		return null;
	}

	private boolean isExisteRepresentante(List<RepresentanteLegal> listaRepresentanteLegal,List<ProcuradorRepEmpreendimento> listaProcuradorRepEmpreendimento,List<ProcuradorPfEmpreendimento> listaProcuradorPfEmpreendimento) {
		return !Util.isNullOuVazio(listaRepresentanteLegal) || !Util.isNullOuVazio(listaProcuradorPfEmpreendimento) || !Util.isNullOuVazio(listaProcuradorRepEmpreendimento);
	}

	public void gerenciarUploadDocumentoObrigatorio(FileUploadEvent event) {
		try {
			UploadedFile arquivo = event.getFile();

			documentoObrigatorioSelecionado.setFileUpload(event);

			documentoObrigatorioSelecionado.setFile(new DefaultStreamedContent(new DataInputStream(new BufferedInputStream(arquivo.getInputstream())), arquivo.getContentType(), arquivo.getFileName()));

			documentoObrigatorioSelecionado.setFileSize(arquivo.getSize());

			DocumentoObrigatorioInterface documentoObrigatorioRequerimento = (DocumentoObrigatorioInterface)documentoObrigatorioSelecionado.clone();

			documentoObrigatorioRequerimento.setIdePessoaUpload(getPessoaLogada());

			if (isReenquadramento()){
				this.documentoObrigatorioReequerimentoService.atualizar((DocumentoObrigatorioReenquadramento)documentoObrigatorioRequerimento, arquivo);
				this.carregarReenquadramento(this.processoReenquadramentoDTO);
			} else {
				this.documentoObrigatorioRequerimentoService.atualizar((DocumentoObrigatorioRequerimento)documentoObrigatorioRequerimento, arquivo);
				Integer ideRequerimento = ((DocumentoObrigatorioRequerimento)documentoObrigatorioRequerimento).getIdeRequerimento().getIdeRequerimento();
				this.carregarDocumentosObrigatorios(ideRequerimento);
			}
			
			JsfUtil.addSuccessMessage("Arquivo enviado com sucesso!");
		}
		catch(Exception e){
			JsfUtil.addErrorMessage("Houve um erro ao tentar salvar o arquivo, tente novamente mais tarde.");
		}
	}

	public void gerenciarUploadDocumentoIdentificacao(FileUploadEvent event) throws Exception {
		try {
			documentoIdentificacaoRequerimentoService.gerenciarUploadDocumentoIdentificacao(documentoIdentificacaoRequerimentoSelecionado, event);
			JsfUtil.addSuccessMessage("Arquivo carregado com sucesso!");
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao enviar documento de identifição");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void gerenciarUploadDocumentoRepresentacao(FileUploadEvent event) throws Exception {		
		try {
			
			String caminhoArquivo = "";
			
			if(!Util.isNull(documentoRepresentacaoRequerimentoSelecionado.getIdeRepresentanteLegal())) {
				
				RepresentanteLegal rl = documentoRepresentacaoRequerimentoSelecionado.getIdeRepresentanteLegal();
				caminhoArquivo = gerenciaArquivoService.uploadArquivoDocumentoRepresentanteLegal(rl, event.getFile());
				rl.setDscCaminhoRepresentacao(caminhoArquivo);
				representanteLegalService.salvarRepresentanteLegal(rl);
				
			}
			else if(!Util.isNull(documentoRepresentacaoRequerimentoSelecionado.getIdeProcuradorRepEmpreendimento())) {
				ProcuradorRepEmpreendimento pre = documentoRepresentacaoRequerimentoSelecionado.getIdeProcuradorRepEmpreendimento();
				caminhoArquivo = gerenciaArquivoService.uploadArquivoDocumentoProcuradorRepresentante(pre.getIdeProcuradorRepresentante(), event.getFile());
				pre.setDscCaminhoProcuracao(caminhoArquivo);
				procuradorRepEmpreendimentoService.salvarOuAtualizar(pre);
			}
			else if(!Util.isNull(documentoRepresentacaoRequerimentoSelecionado.getIdeProcuradorPfEmpreendimento())) {
				ProcuradorPfEmpreendimento ppfe = documentoRepresentacaoRequerimentoSelecionado.getIdeProcuradorPfEmpreendimento();
				caminhoArquivo = gerenciaArquivoService.uploadArquivoDocumentoProcuradorPessoaFisica(ppfe.getIdeProcuradorPessoaFisica(), event.getFile());
				ppfe.setDscCaminhoProcuracao(caminhoArquivo);
				procuradorPfEmpreendimentoService.salvarOuAtualizar(ppfe);
			}
			else {
				throw new SeiaRuntimeException("Não foi possível qualificar o tipo de documento de representação");
			}
			
			if(!Util.isNullOuVazio(documentoRepresentacaoRequerimentoSelecionado)) {
				documentoRepresentacaoRequerimentoSelecionado.setDscCaminhoArquivo(caminhoArquivo);
				documentoRepresentacaoRequerimentoService.salvarOuAtualizar(documentoRepresentacaoRequerimentoSelecionado);
			}
			
			JsfUtil.addSuccessMessage("Arquivo carregado com sucesso!");
		}
		catch(SeiaRuntimeException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao enviar o arquivo.");
			Util.capturarException(e,Util.SEIA_EXCEPTION);	
		}
	}

	public void salvar() {
		try {
			if(podeSalvar()){
				envioDocumentoServiceFacade.salvar(this);
			}
			else {
				JsfUtil.addErrorMessage(showMessagem());
			}
		}
		catch(SeiaTramitacaoRequerimentoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível concluir o envio da documentação.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public String showMessagem(){

		String messagem = "";
		
		if(!Util.isNullOuVazio(lacErb) && lacErb){
			messagem = "É preciso preencher a o formulário Lac Estação Rádio-Base.";
		}
		if(!Util.isNullOuVazio(lacPosto) && lacPosto){
			messagem = "É preciso preencher a o formulário Lac Estação Posto.";
		}
		if(!Util.isNullOuVazio(lacTransporte) &&  lacTransporte){
			messagem = "É preciso preencher a o formulário Lac Transportadora.";
		}
		if(!Util.isNullOuVazio(documentoPresente) && !documentoPresente){
			messagem = "Não foi possível encontrar todos os arquivos.";
		}
		
		return messagem;
	}
	
	
	private Boolean podeSalvar() throws Exception{
		
		boolean permiteSalvar = true ;
		List<DocumentoObrigatorio> documentoObrigatorios = new ArrayList<DocumentoObrigatorio>();
		
		documentoObrigatorios = inicializaDocumentoObrigatorios(documentoObrigatorios);
		
		for (DocumentoObrigatorio documento : documentoObrigatorios) {
			if(isLac(documento)){
				permiteSalvar = false;
				
				//SERVE PARA LAC POSTO E LAC ERB
				if(isLACandPossuiLacLegislacao(requerimento.getIdeRequerimento()) ){
					permiteSalvar = true;
				}
				//SERVE PARA LAC TRANSPORTE
				if(!isLACandPossuiLacTransporte(requerimento.getIdeRequerimento())){
					permiteSalvar = true;
				}
			}	
		}				
		return permiteSalvar;	
	}
				
	private List<DocumentoObrigatorio> inicializaDocumentoObrigatorios(List<DocumentoObrigatorio> documentoObrigatorios) throws Exception {
	
		if(Util.isNullOuVazio(requerimento) || Util.isNullOuVazio(requerimento.getDocumentoObrigatorioRequerimentoCollection())){
			
			documentoObrigatorios = documentoObrigatorioService.carregarFormulariosByRequerimento(requerimento.getIdeRequerimento());
		}
		else {
			for (DocumentoObrigatorioRequerimento documentoObrigatorio : requerimento.getDocumentoObrigatorioRequerimentoCollection()) {
				documentoObrigatorios.add(documentoObrigatorio.getIdeDocumentoObrigatorio());
			}
		}
		return documentoObrigatorios;
	}
	
	public Boolean isLac(DocumentoObrigatorio documentoObrigatorio){
		
		boolean eLac  = false;

		try{
		
			lacErb 		  = false;
			lacPosto 	  = false;
			lacTransporte = false;
			
			if(isLacErb(documentoObrigatorio)){
				lacErb = true;
				eLac = true;
			}
			if(isLacPosto(documentoObrigatorio)){
				lacPosto = true;
				eLac = true;
			}
			if( isLacTransportadora(documentoObrigatorio)){
				lacTransporte = true;
				eLac = true;
			}
			
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível efetuar essa ação.");
		}
		
		return eLac;
	}
	
	
	public Boolean isLACandPossuiLacLegislacao(int ideRequerimento) throws Exception{
		
		
		boolean possuiLacLegistacao = true;
		
		List<LacLegislacao> lacLegislacaos = new ArrayList<LacLegislacao>();
		lacLegislacaos = (List<LacLegislacao>) lacLegislacaoService.carregarByIdeRequerimentoWithLac(ideRequerimento);
		
		if(Util.isNullOuVazio(lacLegislacaos) || lacLegislacaos.isEmpty()){
			possuiLacLegistacao = false;
		}
		
		return possuiLacLegistacao;
	}
	
	public Boolean isLACandPossuiLacTransporte(int ideRequerimento) {
		
		boolean possuiLacLegistacao = false;
		LacTransporte lacTransporte = lacTransporteService.buscarLacTransporteByIdeRequerimento(requerimento);
		
		if(Util.isNullOuVazio(lacTransporte)){
			possuiLacLegistacao = true;
		}
		
		return possuiLacLegistacao;
	}

	
	private Pessoa getPessoaLogada() {
		return new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
	}

	public StreamedContent getFileDownload(String caminhoArquivo) {
		StreamedContent file = null;

		Exception erro = null;

		try {
			InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
			String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
			file = new DefaultStreamedContent(stream, mimeType, caminhoArquivo.substring(caminhoArquivo.lastIndexOf(File.separator) + 1));
		}
		catch(FileNotFoundException e){
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao tentar fazer o download do arquivo");
		}
		finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
		return file;
	}

	public boolean isLacErb(DocumentoObrigatorio documentoObrigatorio) {
		Boolean isValido = Boolean.FALSE;

		Exception erro = null;

		try {
			if(!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())){
				if(documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_ERB).getDscValor())){
					isValido = Boolean.TRUE;
				}
			}
		}
		catch(Exception e){
			erro = e;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
		finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
		return isValido;
	}

	public boolean isLacPosto(DocumentoObrigatorio documentoObrigatorio) {
		Boolean isValido = Boolean.FALSE;

		Exception erro = null;

		try {
			if(!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())){
				if(documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_POSTO).getDscValor())){
					isValido = Boolean.TRUE;
				}
			}
		}
		catch(Exception e){
			erro = e;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
		finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
		return isValido;
	}
	
	public boolean isLacTransportadora(DocumentoObrigatorio documentoObrigatorio) {
		Boolean isValido = Boolean.FALSE;

		Exception erro = null;

		try {
			if(!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())){
				if(documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_TRANSPORTADORA).getDscValor())){
					isValido = Boolean.TRUE;
				}
			}
		}
		catch(Exception e){
			erro =e;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
		finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
		return isValido;
	}

	public Boolean getFormulariosCompletos() {
		if(!Util.isNullOuVazio(formularios)) {
			int qtdFormularios = 0, qtdOutrosForms = 0;
			boolean isNenhumaLac = true, isNenhumFce = true;
			for (DocumentoObrigatorio documentoObrigatorio : formularios) {
				if (isLacPosto(documentoObrigatorio) || isLacErb(documentoObrigatorio)) {
					if(existeLacPostoOrLacErb(documentoObrigatorio)){
						qtdFormularios++;
					}
					isNenhumaLac = false;
				}
				else if(isLacTransportadora(documentoObrigatorio)){
					if(existeLacTransportadora(documentoObrigatorio)){
						qtdFormularios++;
					}
					isNenhumaLac = false;
				}
				else if(isFce(documentoObrigatorio)){
					if(existeFcePreenchido(documentoObrigatorio)){
						qtdFormularios++;
					}
					isNenhumFce = false;
				}
				else {
					qtdOutrosForms++;
				}
			}
			if(qtdFormularios != 0){
				int somatorio = qtdOutrosForms + qtdFormularios;
				return somatorio == formularios.size();
			}
			else{
				return isNenhumaLac && isNenhumFce;
			}
		}
		return true;
	}

	private boolean existeLacPostoOrLacErb(DocumentoObrigatorio documentoObrigatorio){
		Collection<LacLegislacao> legislacoesAceitasLac;
		Exception erro = null;
		try {
			legislacoesAceitasLac = this.lacLegislacaoService.carregarByIdeRequerimentoWithLac(requerimento.getIdeRequerimento());
			return !Util.isNullOuVazio(legislacoesAceitasLac);
		}
		catch(Exception e){
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
		return true;
	}

	private boolean existeLacTransportadora(DocumentoObrigatorio documentoObrigatorio){
		try {
			LacTransporte lacTransporte = lacTransporteService.buscarLacTransporteByIdeRequerimento(requerimento);
			return isLacTransportadoraSalva(lacTransporte) && isFrotaVeiculoCadastrada();
		}
		catch (Exception e) {
			return true;
		}
	}

	public boolean existeFcePreenchido(DocumentoObrigatorio documentoObrigatorio){
		try {
			Fce fce = null;
					
			if (isReenquadramento()){
				fce = fceServiceFacade.buscarFceByIdeRequerimentoDocumentoObrigatorioAndOrigemFce(requerimento, documentoObrigatorio, DadoOrigemEnum.REENQUADRAMENTO);
			} else {
				fce = fceServiceFacade.buscarFceByIdeRequerimentoDocumentoObrigatorioAndOrigemFce(requerimento, documentoObrigatorio, DadoOrigemEnum.REQUERIMENTO);
			}
			
			if(!Util.isNull(fce)){
				return (!Util.isNull(fceServiceFacade.retornarFce(fce)) && (!Util.isNull(fce.getIndConcluido()) && fce.getIndConcluido()));
			}
			return false;
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public boolean existeAtoDeclaratorioPreenchido(DocumentoObrigatorio documentoObrigatorio){
		try {
			AtoDeclaratorio atoDeclaratorio = atosDeclaratoriosFacade.buscarAtoDeclaratorioBy(requerimento, documentoObrigatorio);
			return !Util.isNull(atoDeclaratorio) && atoDeclaratorio.getIndConcluido();
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Ato Declaratório.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean existeFceBarragemPreenchido(DocumentoObrigatorio documentoObrigatorio){
		try {
			Fce fce = fceServiceFacade.buscarFceByIdeRequerimentoDocumentoObrigatorioAndOrigemFce(requerimento, documentoObrigatorio, DadoOrigemEnum.REQUERIMENTO);
			if(!Util.isNull(fce)){
				return fce.getIndConcluido();
			}
			return false;
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
		
	public boolean isFce(DocumentoObrigatorio documentoObrigatorio){
		return FceUtil.isFce(documentoObrigatorio);
	}

	private boolean isLacTransportadoraSalva(LacTransporte lacTransporte){
		return !Util.isNullOuVazio(lacTransporte) && !Util.isNullOuVazio(lacTransporte.getIdeLacTransporte());
	}

	private boolean isFrotaVeiculoCadastrada(){
		try{
			List<Empreendimento> coll = (List<Empreendimento>) empreendimentoService.buscarEmpreendimentoPorRequerimento(requerimento);
			Empreendimento empreendimento = empreendimentoService.carregarById(coll.get(0).getIdeEmpreendimento());
			List<EmpreendimentoVeiculo> listEmpreendimentoVeiculos = veiculoService.listarEmpreendimentoVeiculoByEmpreedimento(empreendimento);

			if(Util.isNull(listEmpreendimentoVeiculos)) {
				return false;
			}
			return !listEmpreendimentoVeiculos.isEmpty();
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public boolean isFceAgrossilvoPastoril(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceAgrossilvoPastoril(documentoObrigatorio);
	}

	public boolean isFceAsv(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceAsv(documentoObrigatorio);
	}

	public boolean isFceIntervencaoBarragem(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceBarragem(documentoObrigatorio);
	}

	public boolean isFceBarragemComPonto(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceBarragemComPonto(documentoObrigatorio);
	}

	public boolean isFceBarragemSemPonto(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceBarragemSemPonto(documentoObrigatorio);
	}

	public boolean isFceOutorgaCaptacaoSubterranea(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceOutorgaCaptacaoSubterranea(documentoObrigatorio);
	}

	public boolean isFceOutorgaCaptacaoSuperficial(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceOutorgaCaptacaoSuperficial(documentoObrigatorio);
	}
	
	public boolean isFceInfraestrutura(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceInfraestrutura(documentoObrigatorio);
	}

	public boolean isFceOutorgaIrrigacao(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceOutorgaIrrigacao(documentoObrigatorio);
	}

	public boolean isFceOutorgaPulverizacao(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceOutorgaPulverizacao(documentoObrigatorio);
	}

	public boolean isFceOutorgaDessedentacaoAnimal(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceOutorgaDessedentacaoAnimal(documentoObrigatorio);
	}

	public boolean isFceOutorgaAbastecimentoIndustrial(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceOutorgaAbastecimentoIndustrial(documentoObrigatorio);
	}

	public boolean isFceOutorgaLancamentoEfluentes(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceOutorgaLancamentoEfluentes(documentoObrigatorio);
	}

	public boolean isFceIndustria(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceIndustria(documentoObrigatorio);
	}

	public boolean isFcePerfuracaoPoco(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFcePerfuracaoPoco(documentoObrigatorio);
	}

	public boolean isFceAbastecimentoHumano(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceAbastecimentoHumano(documentoObrigatorio);
	}

	public boolean isFceOutorgaAquicultura(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceOutorgaAquicultura(documentoObrigatorio);
	}

	public boolean isFceTurismo(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceTurismo(documentoObrigatorio);
	}
	
	public boolean isFceLicenciamentoAquicultura(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceLicenciamentoAquicultura(documentoObrigatorio);
	}

	public boolean isFceAutorizacaoMineracao(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceAutorizacaoMineracao(documentoObrigatorio);
	}

	public boolean isFceLicenciamentoMineracao(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceLicenciamentoMineracao(documentoObrigatorio);
	}
	
	public boolean isFceSistemaAbastecimentoAgua(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceSistemaAbastecimentoAgua(documentoObrigatorio);
	}
	
	public boolean isFceSES(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceSES(documentoObrigatorio);
	}

	public boolean isFceCanais(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceCanais(documentoObrigatorio);
	}
	
	public boolean isFceLinhaEnergiaTrasmissaoEnergia(DocumentoObrigatorio documentoObrigatorio) {
			return FceUtil.isFceLinhaEnergiaTrasmissaoEnergia(documentoObrigatorio);
	}

	public boolean isFceGeracaoEnergia(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceGeracaoEnergia(documentoObrigatorio); 
	}
	
	public boolean isFceIntervencaoMineracao(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceIntervencaoMineracao(documentoObrigatorio);
	}
	
	public boolean isFceFlorestal(DocumentoObrigatorio documentoObrigatorio) {
		return FceUtil.isFceFlorestal(documentoObrigatorio);
	}
	
	public Collection<DocumentoObrigatorio> getFormularios() {
		return formularios;
	}

	public void setFormularios(Collection<DocumentoObrigatorio> formularios) {
		this.formularios = formularios;
	}

	public Collection<DocumentoIdentificacaoRequerimento> getDocumentosIdentificacao() {
		return documentosIdentificacao;
	}

	public void setDocumentosIdentificacao(Collection<DocumentoIdentificacaoRequerimento> documentosIdentificacao) {
		this.documentosIdentificacao = documentosIdentificacao;
	}

	public DocumentoObrigatorioInterface getDocumentoObrigatorioSelecionado() {
		return documentoObrigatorioSelecionado;
	}

	public void setDocumentoObrigatorioSelecionado(DocumentoObrigatorioInterface documentoObrigatorioSelecionado) {
		this.documentoObrigatorioSelecionado = documentoObrigatorioSelecionado;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public List<DocumentoObrigatorioEnquadramentoDTO> getListaDocumentoObrigatorioEnquadramento() {
		return listaDocumentoObrigatorioEnquadramento;
	}

	public void setListaDocumentoObrigatorioEnquadramento(List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramento) {
		this.listaDocumentoObrigatorioEnquadramento = listaDocumentoObrigatorioEnquadramento;
	}

	/**
	 * @return the listaDocumentoObrigatorioEnquadramentoAntigo
	 */
	public List<DocumentoObrigatorioInterface> getListaDocumentoObrigatorioEnquadramentoAntigo() {

		return listaDocumentoObrigatorioEnquadramentoAntigo;
	}

	/**
	 * @param listaDocumentoObrigatorioEnquadramentoAntigo
	 *            the listaDocumentoObrigatorioEnquadramentoAntigo to set
	 */
	public void setListaDocumentoObrigatorioEnquadramentoAntigo(List<DocumentoObrigatorioInterface> listaDocumentoObrigatorioEnquadramentoAntigo) {

		this.listaDocumentoObrigatorioEnquadramentoAntigo = listaDocumentoObrigatorioEnquadramentoAntigo;
	}

	public boolean isRequerimentoAntigoEnquadradoNaV1() {
		return isRequerimentoAntigoEnquadradoNaV1;
	}

	public Collection<DocumentoRepresentacaoRequerimento> getDocumentosRepresentacao() {
		return documentosRepresentacao;
	}

	public void setDocumentosRepresentacao(Collection<DocumentoRepresentacaoRequerimento> documentosRepresentacao) {
		this.documentosRepresentacao = documentosRepresentacao;
	}

	public DocumentoRepresentacaoRequerimento getDocumentoRepresentacaoRequerimentoSelecionado() {
		return documentoRepresentacaoRequerimentoSelecionado;
	}

	public void setDocumentoRepresentacaoRequerimentoSelecionado(DocumentoRepresentacaoRequerimento documentoRepresentacaoRequerimentoSelecionado) {
		this.documentoRepresentacaoRequerimentoSelecionado = documentoRepresentacaoRequerimentoSelecionado;
	}

	public DocumentoIdentificacaoRequerimento getDocumentoIdentificacaoRequerimentoSelecionado() {
		return documentoIdentificacaoRequerimentoSelecionado;
	}

	public void setDocumentoIdentificacaoRequerimentoSelecionado(DocumentoIdentificacaoRequerimento documentoIdentificacaoRequerimentoSelecionado) {
		this.documentoIdentificacaoRequerimentoSelecionado = documentoIdentificacaoRequerimentoSelecionado;
	}

	public TramitacaoRequerimento getTramitacaoRequerimento() {
		return tramitacaoRequerimento;
	}

	public void setTramitacaoRequerimento(TramitacaoRequerimento tramitacaoRequerimento) {
		this.tramitacaoRequerimento = tramitacaoRequerimento;
	}

	public TramitacaoReenquadramentoProcesso getTramitacaoReenquadramentoProcesso() {
		return tramitacaoReenquadramentoProcesso;
	}

	public void setTramitacaoReenquadramentoProcesso(
			TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcesso) {
		this.tramitacaoReenquadramentoProcesso = tramitacaoReenquadramentoProcesso;
	}

	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}

	public void setProcessoReenquadramentoDTO(
			ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}
}