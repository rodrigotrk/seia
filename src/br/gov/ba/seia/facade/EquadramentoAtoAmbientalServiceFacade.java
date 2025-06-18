package br.gov.ba.seia.facade;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Level;
import org.hibernate.Hibernate;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.DocumentoIdentificacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.DocumentoRepresentacaoRequerimento;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorPfEmpreendimento;
import br.gov.ba.seia.entity.ProcuradorRepEmpreendimento;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.VwDocumentoIdentificacao;
import br.gov.ba.seia.entity.VwDocumentoIdentificacaoObrigatorio;
import br.gov.ba.seia.entity.VwResponsavelDocumentoProcuracao;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.DocumentoIdentificacaoRequerimentoService;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.DocumentoRepresentacaoRequerimentoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.ProcuradorPfEmpreendimentoService;
import br.gov.ba.seia.service.ProcuradorRepEmpreendimentoService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.service.VwDocumentoIdentificacaoObrigatorioService;
import br.gov.ba.seia.service.VwDocumentoIdentificacaoService;
import br.gov.ba.seia.service.VwResponsavelDocumentoProcuracaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author mario.junior
 */
@Stateless
public class EquadramentoAtoAmbientalServiceFacade extends PessoaService {

	@EJB
	private EnquadramentoService enquadramentoService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private DocumentoObrigatorioService docObrigatorioService;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	@EJB
	private VwDocumentoIdentificacaoService vwDocumentoIdentificacaoService;
	@EJB
	private DocumentoIdentificacaoService docIdentificacaoService;
	@EJB
	private DocumentoIdentificacaoRequerimentoService docIdentificacaoRequerimentoService;
	@EJB
	private VwResponsavelDocumentoProcuracaoService vwResponsavelDocumentoProcuracaoService;
	@EJB
	private RepresentanteLegalService representanteLegalService;
	@EJB
	private ProcuradorPfEmpreendimentoService procuradorPfEmpreendimentoService;
	@EJB
	private ProcuradorRepEmpreendimentoService procuradorRepEmpreendimentoService;
	@EJB
	private DocumentoRepresentacaoRequerimentoService docRepresentacaoRequerimentoService;
	@EJB
	private DocumentoObrigatorioRequerimentoService docObrigatorioRequerimentoService;
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	@EJB
	private RequerimentoUnicoService requerimentoUnicoSevice;
	@EJB
	private VwDocumentoIdentificacaoObrigatorioService vwDocumentoIdentificacaoObrigatorio;
	@EJB
	private DocumentoIdentificacaoRequerimentoService documentoIdentificacaoRequerimentoService;
	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;
	@EJB
	private DocumentoRepresentacaoRequerimentoService documentoRepresentacaoRequerimentoService;
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	@EJB
	private RequerimentoService requerimentoService;
	private final Integer IDE_DOC_DATA_PERFURACAO_POCO = 170;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarEnquadramento(Enquadramento enquadramento) throws Exception {
		Exception erro =null;
		try {
			List<AtoAmbiental> newAtosAmbientais = new ArrayList<AtoAmbiental>();
			List<DocumentoObrigatorio> newFomularios = new ArrayList<DocumentoObrigatorio>();
			List<AtoAmbiental> atosAmbientais = (List<AtoAmbiental>) enquadramento.getAtoAmbientalCollection();
			List<DocumentoObrigatorio> fomularios = (List<DocumentoObrigatorio>) (Hibernate.isInitialized(enquadramento.getDocumentoObrigatorioCollection()) ? enquadramento.getDocumentoObrigatorioCollection() : new ArrayList<DocumentoObrigatorio>());
			enquadramento.setAtoAmbientalCollection(null);
			enquadramento.setDocumentoObrigatorioCollection(null);
			enquadramentoService.salvarOuAtualizarEnquadramento(enquadramento);
			if (enquadramento.getIndEnquadramentoAprovado()) {
				if (atosAmbientais != null && !atosAmbientais.isEmpty()) {
					for (AtoAmbiental atoAmbiental : atosAmbientais) {
						atoAmbiental = atoAmbientalService.obterAtoAmbiental(atoAmbiental);
						atoAmbiental.setEnquadramentoCollection(new ArrayList<Enquadramento>());
						atoAmbiental.getEnquadramentoCollection().add(enquadramento);
						newAtosAmbientais.add(atoAmbiental);
					}
					enquadramento.setAtoAmbientalCollection(newAtosAmbientais);
					atoAmbientalService.salvarAtoAmbientalEmLotes(newAtosAmbientais, enquadramento);
				}
				if (fomularios != null && !fomularios.isEmpty()) {
					for (DocumentoObrigatorio documentoObrigatorio : fomularios) {
						documentoObrigatorio = docObrigatorioService.obterDocumentoObrigatorioCriteria(documentoObrigatorio);
						documentoObrigatorio.setEnquadramentoCollection(new ArrayList<Enquadramento>());
						documentoObrigatorio.getEnquadramentoCollection().add(enquadramento);
						newFomularios.add(documentoObrigatorio);
					}
				}
				enquadramento.setDocumentoObrigatorioCollection(Util.sigletonList(newFomularios));
				docObrigatorioService.salvarDocumentoObrigatorioEmLotes(newFomularios, enquadramento);
				this.gerarDocumentosRequerimento(enquadramento);
				//enquadramentoService.salvarOuAtualizarEnquadramento(enquadramento);
				// Inserir nova tramita��o com status ENQUADRADO
				StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.ENQUADRADO.getStatus());
				tramitacaoRequerimentoService.criarTramitacaoRequerimento(enquadramento.getIdeRequerimentoUnico().getRequerimento(), status, enquadramento.getIdePessoa());
			} else {
				// Inserir nova tramita��o com status PEND�NCIA ENQUADRAMENTO
				StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO.getStatus());
				tramitacaoRequerimentoService.criarTramitacaoRequerimento(enquadramento.getIdeRequerimentoUnico().getRequerimento(), status, enquadramento.getIdePessoa());
			}
			this.recuperarDocumentoRequerimentoAnterior(enquadramento);
		} catch (Exception e) {
			erro =e;
			System.out.println("Erro Salvar Enquadramento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw e;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void recuperarDocumentoRequerimentoAnterior(Enquadramento enquadramento) throws Exception {
		Collection<VwDocumentoIdentificacaoObrigatorio> arqvsDocumento = new ArrayList<VwDocumentoIdentificacaoObrigatorio>();
		arqvsDocumento.addAll(vwDocumentoIdentificacaoObrigatorio.buscarDocsObrigatorioReqPorRequerimentoUnico(enquadramento.getIdeRequerimentoUnico()));
		requerimentoUnicoSevice.RequerimentoUnicoInitializeEmpreendimento(enquadramento.getIdeRequerimentoUnico());
		Empreendimento empreendimento = null;
		for (EmpreendimentoRequerimento empreendimentocoll : enquadramento.getIdeRequerimentoUnico().getRequerimento().getEmpreendimentoRequerimentoCollection()) {
			empreendimento = empreendimentocoll.getIdeEmpreendimento();
		}
		RequerimentoUnico requerimentoUnicoAnterior = requerimentoUnicoSevice.recuperarRequerimentoUnicoProcessoFormadoAnterior(empreendimento);
		if (requerimentoUnicoAnterior != null) {
			Collection<VwDocumentoIdentificacaoObrigatorio> arqvsDocumentoRequerimentoAnterior = new ArrayList<VwDocumentoIdentificacaoObrigatorio>();
			arqvsDocumentoRequerimentoAnterior.addAll(vwDocumentoIdentificacaoObrigatorio.buscarDocsObrigatorioReqPorRequerimentoUnico(requerimentoUnicoAnterior));
			for (VwDocumentoIdentificacaoObrigatorio doc : arqvsDocumento) {
				for (VwDocumentoIdentificacaoObrigatorio docAnterior : arqvsDocumentoRequerimentoAnterior) {
					if (doc.getIdeDocumentoDto() == docAnterior.getIdeDocumentoDto()) {
						doc.setCaminhoArquivoAnterior(docAnterior.getDscCaminhoArquivoDto());
						doc.setIsArquivoChanged(Boolean.TRUE);
						atualizarDadosArquivoBanco(doc);
					}
				}
			}
		}
	}

	private void gerarDocumentosRequerimento(Enquadramento enquadramento) {
		this.gerarDocumentosObrigatorios(enquadramento);
		this.gerarDocumentosIdentificacao(enquadramento.getIdeRequerimentoUnico());
		this.gerarDocumentosProcuracao(enquadramento.getIdeRequerimentoUnico());
	}

	private void gerarDocumentosObrigatorios(Enquadramento enquadramento) {
		Exception erro =null;
		try {
			List<DocumentoObrigatorioRequerimento> docsObrigatorioRequerimento = new ArrayList<DocumentoObrigatorioRequerimento>();
			for (DocumentoObrigatorio docObrigatorio : enquadramento.getDocumentoObrigatorioCollection()) {
				DocumentoObrigatorioRequerimento docObrigReq = new DocumentoObrigatorioRequerimento();
				docObrigReq.setIdeDocumentoObrigatorio(docObrigatorio);
				docObrigReq.setIdeRequerimento(enquadramento.getIdeRequerimentoUnico().getRequerimento());
				if (!docObrigatorio.getIndFormulario()) {
					docObrigReq.setDscCaminhoArquivo(docObrigatorio.getDscCaminhoArquivo());
				}
				docObrigatorio.setDocumentoObrigatorioRequerimentoCollection(new ArrayList<DocumentoObrigatorioRequerimento>());
				docObrigatorio.getDocumentoObrigatorioRequerimentoCollection().add(docObrigReq);
				docsObrigatorioRequerimento.add(docObrigReq);
			}
			boolean utilizaAgua = !Util.isNull(enquadramento.getIdeRequerimentoUnico().getIndUtilizaAgua()) && enquadramento.getIdeRequerimentoUnico().getIndUtilizaAgua();
			boolean pocoPerfurado = !Util.isNull(enquadramento.getIdeRequerimentoUnico().getIndPocoCaptacaoPerfurado()) && enquadramento.getIdeRequerimentoUnico().getIndPocoCaptacaoPerfurado();
			if (utilizaAgua && pocoPerfurado) {
				this.adicionarDocumentoPerfuracaoPoco(enquadramento, docsObrigatorioRequerimento);
			}
			enquadramento.getIdeRequerimentoUnico().getRequerimento().setDocumentoObrigatorioRequerimentoCollection(docsObrigatorioRequerimento);
			docObrigatorioRequerimentoService.salvarEmLote(Util.sigletonList(docsObrigatorioRequerimento));
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void adicionarDocumentoPerfuracaoPoco(Enquadramento enquadramento, List<DocumentoObrigatorioRequerimento> docsObrigatorioRequerimento) {
		DocumentoObrigatorio docObrigatorio = new DocumentoObrigatorio(IDE_DOC_DATA_PERFURACAO_POCO);
		DocumentoObrigatorioRequerimento docObrigReq = new DocumentoObrigatorioRequerimento();
		docObrigReq.setIdeDocumentoObrigatorio(docObrigatorio);
		docObrigReq.setIdeRequerimento(enquadramento.getIdeRequerimentoUnico().getRequerimento());
		docObrigatorio.setDocumentoObrigatorioRequerimentoCollection(new ArrayList<DocumentoObrigatorioRequerimento>());
		docObrigatorio.getDocumentoObrigatorioRequerimentoCollection().add(docObrigReq);
		docsObrigatorioRequerimento.add(docObrigReq);
	}

	private void gerarDocumentosIdentificacao(RequerimentoUnico requerimentoUnico) {
		Exception erro = null;
		try {
			List<DocumentoIdentificacaoRequerimento> docsIdentificacaoRequerimento = new ArrayList<DocumentoIdentificacaoRequerimento>();
			Collection<VwDocumentoIdentificacao> vwDocumentoIdentificacaoList = vwDocumentoIdentificacaoService.filtrarDocumentosIdentificacaoRequerimentoUnicoById(requerimentoUnico);
			for (VwDocumentoIdentificacao vwDocumentoIdentificacao : vwDocumentoIdentificacaoList) {
				DocumentoIdentificacaoRequerimento docIdentfReq = new DocumentoIdentificacaoRequerimento();
				docIdentfReq.setIdeRequerimento(requerimentoUnico.getRequerimento());
				if (vwDocumentoIdentificacao.getIdeDocumentoIdentificacao() != null) {
					DocumentoIdentificacao docIdentfic = new DocumentoIdentificacao(vwDocumentoIdentificacao.getIdeDocumentoIdentificacao());
					docIdentfic = docIdentificacaoService.recuperarDocumentoById(docIdentfic);
					docIdentfReq.setIdeDocumentoIdentificacao(docIdentfic);
					docIdentfic.setDocumentoIdentificacaoRequerimentoCollection(docsIdentificacaoRequerimento);
				} else {
					PessoaJuridica pj = new PessoaJuridica(vwDocumentoIdentificacao.getIdePessoa());
					pj = pessoaJuridicaService.obterPessoaJuridicaId(pj);
					Collection<DocumentoIdentificacaoRequerimento> docsIdent = new ArrayList<DocumentoIdentificacaoRequerimento>();
					docsIdent.add(docIdentfReq);
					pj.setDocumentoIdentificacaoRequerimentoCollection(docsIdent);
					docIdentfReq.setIdePessoaJuridica(pj);
				}
				boolean isJaAdicionado = Boolean.FALSE;
				for (DocumentoIdentificacaoRequerimento docIdentReq : docsIdentificacaoRequerimento) {
					if (docIdentReq.getIdeDocumentoIdentificacao().equals(docIdentfReq.getIdeDocumentoIdentificacao())) {
						isJaAdicionado = Boolean.TRUE;
						break;
					}
				}
				if (isJaAdicionado) {
					docsIdentificacaoRequerimento.add(docIdentfReq);
				}
			}
			requerimentoUnico.getRequerimento().setDocumentoIdentificacaoRequerimentoCollection(docsIdentificacaoRequerimento);
			if (!docsIdentificacaoRequerimento.isEmpty()) {
				docIdentificacaoRequerimentoService.salvarEmLote(Util.sigletonList(docsIdentificacaoRequerimento));
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void gerarDocumentosProcuracao(RequerimentoUnico requerimentoUnico) {
		Exception erro =null;
		try {
			List<DocumentoRepresentacaoRequerimento> docsRepresentacaoRequerimento = new ArrayList<DocumentoRepresentacaoRequerimento>();
			Collection<VwResponsavelDocumentoProcuracao> respDocProcuracao = vwResponsavelDocumentoProcuracaoService.listarPorRequerimento(requerimentoUnico);
			for (VwResponsavelDocumentoProcuracao vwResponsavelDocumentoProcuracao : respDocProcuracao) {
				DocumentoRepresentacaoRequerimento docRepresentacaoRequerimento = new DocumentoRepresentacaoRequerimento();
				docRepresentacaoRequerimento.setIdeRequerimento(requerimentoUnico.getRequerimento());
				if (!Util.isNullOuVazio(vwResponsavelDocumentoProcuracao.getTipoDocumento()) && vwResponsavelDocumentoProcuracao.getTipoDocumento().equalsIgnoreCase("REQUERENTE_PESSOA_FISICA")) {
					ProcuradorPfEmpreendimento ideProcuradorPfEmpreendimento = new ProcuradorPfEmpreendimento(vwResponsavelDocumentoProcuracao.getIdeRepresentanteDocRepresentacao());
					docRepresentacaoRequerimento.setIdeProcuradorPfEmpreendimento(procuradorPfEmpreendimentoService.buscarPorIdViaCriteria(ideProcuradorPfEmpreendimento));
				} else if (!Util.isNullOuVazio(vwResponsavelDocumentoProcuracao.getTipoDocumento()) && vwResponsavelDocumentoProcuracao.getTipoDocumento().equalsIgnoreCase("PROCURADOR_PESSOA_JURIDICA")) {
					ProcuradorRepEmpreendimento ideProcuradorRepresentante = new ProcuradorRepEmpreendimento(vwResponsavelDocumentoProcuracao.getIdeRepresentanteDocRepresentacao());
					docRepresentacaoRequerimento.setIdeProcuradorRepEmpreendimento(procuradorRepEmpreendimentoService.buscarPorIdViaCriteria(ideProcuradorRepresentante));
				} else if (!Util.isNullOuVazio(vwResponsavelDocumentoProcuracao.getTipoDocumento()) && vwResponsavelDocumentoProcuracao.getTipoDocumento().equalsIgnoreCase("PROCURADOR_PESSOA_FISICA")) {
					ProcuradorPfEmpreendimento ideProcuradorPfEmpreendimento = new ProcuradorPfEmpreendimento(vwResponsavelDocumentoProcuracao.getIdeRepresentanteDocRepresentacao());
					docRepresentacaoRequerimento.setIdeProcuradorPfEmpreendimento(procuradorPfEmpreendimentoService.buscarPorIdViaCriteria(ideProcuradorPfEmpreendimento));
				} else if (!Util.isNullOuVazio(vwResponsavelDocumentoProcuracao.getTipoDocumento()) && vwResponsavelDocumentoProcuracao.getTipoDocumento().equalsIgnoreCase("PROCURADOR_REPRESENTANTE")) {
					ProcuradorRepresentante ideProcuradorRepresentante = new ProcuradorRepresentante(vwResponsavelDocumentoProcuracao.getIdeRepresentanteDocRepresentacao());
					docRepresentacaoRequerimento.setIdeProcuradorRepEmpreendimento(procuradorRepEmpreendimentoService.buscarPorProcuradorRepresentanteViaCriteria(ideProcuradorRepresentante, requerimentoUnico.getRequerimento().getEmpreendimentoRequerimentoCollection().iterator().next().getIdeEmpreendimento().getIdeEmpreendimento()));
				} else if (!Util.isNullOuVazio(vwResponsavelDocumentoProcuracao.getTipoDocumento()) && vwResponsavelDocumentoProcuracao.getTipoDocumento().equalsIgnoreCase("REPRESENTANTE_LEGAL")) {
					RepresentanteLegal ideRepresentanteLegal = new RepresentanteLegal(vwResponsavelDocumentoProcuracao.getIdeRepresentanteDocRepresentacao());
					docRepresentacaoRequerimento.setIdeRepresentanteLegal(representanteLegalService.buscarPorIdViaCriteria(ideRepresentanteLegal));
				}
				boolean isJaAdicionado = Boolean.FALSE;
				if (!Util.isNull(docsRepresentacaoRequerimento) && docsRepresentacaoRequerimento.isEmpty()) {
					for (DocumentoRepresentacaoRequerimento docRepRequerimento : docsRepresentacaoRequerimento) {
						if (docRepRequerimento.getIdeProcuradorRepEmpreendimento() != null && docRepresentacaoRequerimento.getIdeProcuradorRepEmpreendimento() != null
								&& docRepRequerimento.getIdeProcuradorRepEmpreendimento().getIdeProcuradorRepEmpreendimento().equals(docRepresentacaoRequerimento.getIdeProcuradorRepEmpreendimento().getIdeProcuradorRepEmpreendimento())) {
							isJaAdicionado = Boolean.TRUE;
							break;
						}
					}
				}
				if (!isJaAdicionado) {
					docsRepresentacaoRequerimento.add(docRepresentacaoRequerimento);
				}
			}
			requerimentoUnico.getRequerimento().setDocumentoRepresentacaoRequerimentoCollection(docsRepresentacaoRequerimento);
			if (!docsRepresentacaoRequerimento.isEmpty()) {
				docRepresentacaoRequerimentoService.salvarEmLote(Util.sigletonList(docsRepresentacaoRequerimento));
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void iniciarEnquadramento(Enquadramento enquadramento) throws Exception {
		StatusRequerimento statusRequerimento = statusRequerimentoService.getMaxStatusTramitacaoRequerimantoPorData(enquadramento);
		if (statusRequerimento == null || statusRequerimento.getIdeStatusRequerimento().compareTo(StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus()) < 0) {
			enquadramento.setAtoAmbientalCollection(null);
			enquadramento.setDocumentoObrigatorioCollection(null);
			enquadramentoService.salvarOuAtualizarEnquadramento(enquadramento);
			// Inserir nova tramita��o com status SENDO_ENQUADRADO
			StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus());
			tramitacaoRequerimentoService.criarTramitacaoRequerimento(enquadramento.getIdeRequerimentoUnico().getRequerimento(), status, enquadramento.getIdePessoa());
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarDadosArquivoBanco(VwDocumentoIdentificacaoObrigatorio vwDocumentoIdentificacaoObrigatorio) {
		Exception erro =null;
		try {
			Requerimento requerimento = requerimentoService.buscarEntidadePorId(new Requerimento(vwDocumentoIdentificacaoObrigatorio.getIdeRequerimentoDto()));
			DocumentoObrigatorio docObrigatorio = documentoObrigatorioService.obterDocumentoObrigatorioCriteria(new DocumentoObrigatorio(vwDocumentoIdentificacaoObrigatorio.getIdeDocumentoDto()));
			if (vwDocumentoIdentificacaoObrigatorio.getTipoDocumentoDto().equalsIgnoreCase("documento_obrigatorio_requerimento")) {
				atualizarDocObrigatorioRequerimento(vwDocumentoIdentificacaoObrigatorio, requerimento, docObrigatorio);
			} else if (vwDocumentoIdentificacaoObrigatorio.getTipoDocumentoDto().equalsIgnoreCase("documento_identificacao_requerimento")) {
				atualizarDocIdentificacaoRequerimento(vwDocumentoIdentificacaoObrigatorio, requerimento);
			} else if (vwDocumentoIdentificacaoObrigatorio.getTipoDocumentoDto().equalsIgnoreCase("DOCUMENTO_REPRESENTACAO_REQUERIMENTO")) {
				atualizarDocRepresentacaoRequerimento(vwDocumentoIdentificacaoObrigatorio, requerimento);
			}
			JsfUtil.addSuccessMessage("Arquivo carregado com sucesso!");
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void atualizarDocObrigatorioRequerimento(VwDocumentoIdentificacaoObrigatorio vwDocumentoIdentificacaoObrigatorio, Requerimento requerimento, DocumentoObrigatorio docObrigatorio) throws Exception {
		DocumentoObrigatorioRequerimento arquivoObrigatorio = new DocumentoObrigatorioRequerimento();
		arquivoObrigatorio.setIdeDocumentoObrigatorioRequerimento(vwDocumentoIdentificacaoObrigatorio.getIdeDocumentoRequerimentoDto());
		arquivoObrigatorio.setArquivoChanged(vwDocumentoIdentificacaoObrigatorio.getIsArquivoChanged());
		arquivoObrigatorio.setCaminhoArquivoAnterior(vwDocumentoIdentificacaoObrigatorio.getCaminhoArquivoAnterior());
		arquivoObrigatorio.setIdeRequerimento(requerimento);
		arquivoObrigatorio.setIdeDocumentoObrigatorio(docObrigatorio);
		arquivoObrigatorio.setDtcValidacao(vwDocumentoIdentificacaoObrigatorio.getDtcValidacaoDto());
		arquivoObrigatorio.setIndDocumentoValidado(vwDocumentoIdentificacaoObrigatorio.getIndDocumentoValidadoDto());
		vwDocumentoIdentificacaoObrigatorio.setDscCaminhoArquivoDto(DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO + File.separator + vwDocumentoIdentificacaoObrigatorio.getIdeDocumentoRequerimentoDto().toString() + File.separator
				+ getFileName(vwDocumentoIdentificacaoObrigatorio));
		arquivoObrigatorio.setDscCaminhoArquivo(vwDocumentoIdentificacaoObrigatorio.getDscCaminhoArquivoDto());
		docObrigatorioRequerimentoService.salvarOuAtualizar(arquivoObrigatorio);
	}

	private void atualizarDocIdentificacaoRequerimento(VwDocumentoIdentificacaoObrigatorio vwDocumentoIdentificacaoObrigatorio, Requerimento requerimento) throws Exception {
		DocumentoIdentificacaoRequerimento documentoIdentificacaoRequerimento = new DocumentoIdentificacaoRequerimento();
		documentoIdentificacaoRequerimento.setIdeDocumentoIdentificacaoRequerimento(vwDocumentoIdentificacaoObrigatorio.getIdeDocumentoRequerimentoDto());
		documentoIdentificacaoRequerimento.setArquivoChanged(vwDocumentoIdentificacaoObrigatorio.getIsArquivoChanged());
		documentoIdentificacaoRequerimento.setCaminhoArquivoAnterior(vwDocumentoIdentificacaoObrigatorio.getCaminhoArquivoAnterior());
		documentoIdentificacaoRequerimento.setIdeRequerimento(requerimento);
		documentoIdentificacaoRequerimento.setIdeDocumentoIdentificacao(documentoIdentificacaoService.recuperarDocumentoById(new DocumentoIdentificacao(vwDocumentoIdentificacaoObrigatorio.getIdeDocumentoDto())));
		vwDocumentoIdentificacaoObrigatorio.setDscCaminhoArquivoDto(DiretorioArquivoEnum.DOCUMENTOIDENTIFICACAO + File.separator + vwDocumentoIdentificacaoObrigatorio.getIdeDocumentoRequerimentoDto().toString() + File.separator
				+ getFileName(vwDocumentoIdentificacaoObrigatorio));
		documentoIdentificacaoRequerimentoService.salvarOuAtualizar(documentoIdentificacaoRequerimento, true);
		DocumentoIdentificacao documentoIdentificacao = documentoIdentificacaoService.recuperarDocumentoById(new DocumentoIdentificacao(vwDocumentoIdentificacaoObrigatorio.getIdeDocumentoDto()));
		documentoIdentificacao.setDscCaminhoArquivo(vwDocumentoIdentificacaoObrigatorio.getDscCaminhoArquivoDto());
		documentoIdentificacaoService.salvarDocumentoIdentificacao(documentoIdentificacao);
	}

	private void atualizarDocRepresentacaoRequerimento(VwDocumentoIdentificacaoObrigatorio vwDocumentoIdentificacaoObrigatorio, Requerimento requerimento) throws Exception {
		DocumentoRepresentacaoRequerimento documentoRepresentacaoRequerimento = documentoRepresentacaoRequerimentoService.obterDocumentoRepReqCriteria(new DocumentoRepresentacaoRequerimento(
				vwDocumentoIdentificacaoObrigatorio.getIdeDocumentoRequerimentoDto()));
//		String fileName = getFileName(vwDocumentoIdentificacaoObrigatorio);
		vwDocumentoIdentificacaoObrigatorio.setDscCaminhoArquivoDto(DiretorioArquivoEnum.PESSOA + File.separator + getFileName(vwDocumentoIdentificacaoObrigatorio));
		if (!Util.isNull(documentoRepresentacaoRequerimento.getIdeRepresentanteLegal())) {
			RepresentanteLegal representanteLegal = documentoRepresentacaoRequerimento.getIdeRepresentanteLegal();
			representanteLegal = representanteLegalService.buscarPorIdViaCriteria(representanteLegal);
			representanteLegal.setDscCaminhoRepresentacao(vwDocumentoIdentificacaoObrigatorio.getDscCaminhoArquivoDto());
			representanteLegalService.salvarRepresentanteLegal(representanteLegal);
		}
	}

	public String getFileName(VwDocumentoIdentificacaoObrigatorio documento) {
		if (!Util.isNull(documento) && !Util.isNull(documento.getCaminhoArquivoAnterior())) {
			return documento.getCaminhoArquivoAnterior().substring(documento.getCaminhoArquivoAnterior().lastIndexOf(File.separator) + 1);
		}
		return null;
	}
}