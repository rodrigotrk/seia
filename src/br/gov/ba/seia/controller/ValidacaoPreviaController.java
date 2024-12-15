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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.dto.DocumentoObrigatorioEnquadramentoDTO;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.CumprimentoReposicaoFlorestal;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoAppCaracteristca;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.DocumentoIdentificacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.DocumentoImovelRuralRequerimento;
import br.gov.ba.seia.entity.DocumentoObrigatorioReenquadramento;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.DocumentoRepresentacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoRequerimentoEmpreendimento;
import br.gov.ba.seia.entity.DocumentoValidacao;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.ProcuradorPfEmpreendimento;
import br.gov.ba.seia.entity.ProcuradorRepEmpreendimento;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.TramitacaoReenquadramentoProcesso;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.facade.AtoDeclaratorioFacade;
import br.gov.ba.seia.facade.FceServiceFacade;
import br.gov.ba.seia.facade.TramitacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.facade.ValidacaoPreviaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.DocumentoObrigatorioInterface;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorio;
import br.gov.ba.seia.service.ComunicacaoRequerimentoService;
import br.gov.ba.seia.service.CumprimentoReposicaoFlorestalService;
import br.gov.ba.seia.service.DeclaracaoIntervencaoAppCaracteristcaService;
import br.gov.ba.seia.service.DocumentoIdentificacaoRequerimentoService;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.DocumentoImovelRuralRequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.DocumentoRepresentacaoRequerimentoService;
import br.gov.ba.seia.service.DocumentoRequerimentoEmpreendimentoService;
import br.gov.ba.seia.service.EmailService;
import br.gov.ba.seia.service.EmpreendimentoRequerimentoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorPfEmpreendimentoService;
import br.gov.ba.seia.service.ProcuradorRepEmpreendimentoService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.ResponsavelEmpreendimentoService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.service.facade.ComunicacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.service.facade.ComunicacaoServiceFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("validacaoPreviaController")
@ViewScoped
public class ValidacaoPreviaController extends BaseValidacaoController {

	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioRequerimentoService;

	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;

	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;

	@EJB
	private DocumentoIdentificacaoRequerimentoService documentoIdentificacaoRequerimentoService;

	@EJB
	private DocumentoImovelRuralRequerimentoService documentoImovelRuralRequerimentoService;

	@EJB
	private GerenciaArquivoService gerenciaArquivoService;

	@EJB
	private DocumentoRepresentacaoRequerimentoService documentoRepresentacaoRequerimentoService;

	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;

	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;

	@EJB
	private StatusRequerimentoService statusRequerimentoService;

	@EJB
	private RequerimentoService requerimentoService;

	@EJB
	private EnquadramentoService enquadramentoService;

	@EJB
	private ValidacaoPreviaServiceFacade validacaoPreviaServiceFacade;

	@EJB
	private EmailService emailService;

	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;

	@EJB
	private FceServiceFacade fceServiceFacade;

	@EJB
	private AtoDeclaratorioFacade atosDeclaratoriosFacade;

	@EJB
	private DeclaracaoIntervencaoAppCaracteristcaService declaracaoIntervencaoAppCaracteristcaService;

	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;

	@EJB
	private EmpreendimentoRequerimentoService empreendimentoRequerimentoService;

	@EJB
	private ResponsavelEmpreendimentoService responsavelEmpreendimentoService;

	@EJB
	private DocumentoRequerimentoEmpreendimentoService documentoRequerimentoEmpreendimentoService;

	@EJB
	private PessoaFisicaService pessoaFisicaService;

	@EJB
	private CumprimentoReposicaoFlorestalService cumprimentoReposicaoFlorestalService;
	
	@EJB
	private ComunicacaoServiceFacade comunicacaoServiceFacade;
	
	@EJB
	private ComunicacaoReenquadramentoProcessoServiceFacade comunicacaoReenquadramentoProcessoServiceFacade;
	
	@EJB
	private ProcuradorRepEmpreendimentoService procuradorRepEmpreendimentoService;
	
	@EJB
	private ProcuradorPfEmpreendimentoService procuradorPfEmpreendimentoService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;

	/**
	 * OBJETOS
	 */
	private String emailDocumentoValidado;

	private Collection<DocumentoIdentificacaoRequerimento> documentosIdentificacao;

	private List<DocumentoImovelRuralRequerimento> documentosImovelRuralReq;

	private Collection<DocumentoRepresentacaoRequerimento> documentosRepresentacao;

	private Requerimento requerimento;

	private String emailPendenciaValidacao;

	private DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento;

	private List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramento;

	private List<DocumentoObrigatorioInterface> listaDocumentoObrigatorioEnquadramentoAntigo;

	private List<DeclaracaoIntervencaoAppCaracteristca> listaDeclaracaoIntervencao;

	private Boolean reqAntigo;

	private DocumentoObrigatorioRequerimento documentoObrigatorioSelecionado;

	private List<Fce> listaFce;

	private List<AtoDeclaratorio> listaAtoDeclaratorio;

	private TramitacaoRequerimento tramitacaoRequerimento;

	private ProcessoReenquadramentoDTO processoReenquadramentoDTO;

	private TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcesso;

	private List<DocumentoRequerimentoEmpreendimento> listaDocumentoRequerimentoEmpreendimentos;

	@PostConstruct
	public void init() {
	}

	private void limpar() {
		this.documentosIdentificacao = new ArrayList<DocumentoIdentificacaoRequerimento>();
		this.listaFce = null;
	}

	public void load(Requerimento requerimento) {

		try {
			limpar();
			Integer ideRequerimento = requerimento.getIdeRequerimento();
			this.requerimento = requerimentoService.carregarDadosBasicos(ideRequerimento);
			empreendimentoRequerimento = empreendimentoService.buscarEmpreendimentoRequerimento(requerimento);
			atosAmbientais = atoAmbientalService.listarAtosEnquadradosByRequerimento(ideRequerimento);

			reqAntigo = requerimentoService.isRequerimentoAntigo(ideRequerimento);

			carregarDocumentosObrigatorios(ideRequerimento);

			docObrigatorioPossuiEnquadramentoAtoAmbiental(ideRequerimento);

			verificarDocumentosIdentificacao(requerimento);
			verificarDocumentosRepresentacao(requerimento);
			carregarDocumentosImovelRuralReq(requerimento);

			consultarAreas(requerimento);

			carregarListaFce(requerimento);

			carregarListaAtoDeclaratorio(requerimento);

			carregarComunicacaoRequerimento(requerimento);

			carregarDeclaracoes(ideRequerimento);

			if (!Util.isNullOuVazio(requerimento.getAtosAmbientais())) {
				if (requerimento.getAtosAmbientais().iterator().next().isTLA()
						|| requerimento.getAtosAmbientais().iterator().next().getIdeAtoAmbiental()
								.equals(AtoAmbientalEnum.ARLS)
						|| requerimento.getAtosAmbientais().iterator().next().getIdeAtoAmbiental()
								.equals(AtoAmbientalEnum.DTRP)
						|| requerimento.getAtosAmbientais().iterator().next().getIdeAtoAmbiental()
								.equals(AtoAmbientalEnum.INEXIGIBILIDADE)) {
					carregarUltimoEmpreendimento(requerimento);
					carregarDocumentoRequerimento();
				}
			}

			carregarRequerente(requerimento);

			tramitacaoRequerimento = tramitacaoRequerimentoService
					.buscarUltimaTramitacaoPorRequerimento(ideRequerimento);

		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);// log
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarRequerente(Requerimento requerimento) throws Exception {
		if (Util.isNullOuVazio(requerimento.getRequerente())) {
			Pessoa requerente = pessoaFisicaService.carregarDadosRequerente(requerimento.getIdeRequerimento(), null);
			requerimento.setRequerente(requerente);
			if (requerente.isPF()) {
				requerente.setCpfCnpj(requerente.getPessoaFisica().getNumCpfFormatado());
			} else {
				requerente.setCpfCnpj(requerente.getPessoaJuridica().getNumCnpj());
			}
		}
	}

	private void carregarUltimoEmpreendimento(Requerimento requerimento) throws Exception {
		EmpreendimentoRequerimento er = empreendimentoRequerimentoService
				.buscarEmpreendimentoRequerimento(requerimento);
		requerimento.setUltimoEmpreendimento(er.getIdeEmpreendimento());
	}

	public void loadReenquadramento(ProcessoReenquadramentoDTO processoReenquadramentoDTO) throws Exception {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
		load(processoReenquadramentoDTO.getRequerimento());
		carregarReenquadramento(processoReenquadramentoDTO);
		setRequerimento(processoReenquadramentoDTO.getRequerimento());
		setTramitacaoReenquadramentoProcesso(
				tramitacaoReenquadramentoProcessoServiceFacade.buscarUltimaTramitacaoPorReenquadramentoProcesso(
						processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento()));
	}

	private void carregarReenquadramento(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		try {
			listaDocumentoObrigatorioEnquadramento = documentoObrigatorioService
					.listaDocumentoObrigatorioEnquadramentoDTOReenquadramento(
							processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento(),
							true);
			this.reqAntigo = false;
			this.documentosIdentificacao = null;
			this.documentosImovelRuralReq = null;
			this.documentosRepresentacao = null;
			this.listaFce = null;
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isAutorizacaoProcedimentoEspecial() {
		for (AtoAmbiental a : atosAmbientais) {
			if (new AtoAmbiental(AtoAmbientalEnum.APE.getId()).equals(a)) {
				return true;
			}
		}
		return false;
	}

	public boolean isReenquadramento() {
		return !Util.isNull(processoReenquadramentoDTO);
	}

	private void carregarComunicacaoRequerimento(Requerimento requerimento) {
		ComunicacaoRequerenteController comunicacaoRequerenteController = (ComunicacaoRequerenteController) SessaoUtil
				.recuperarManagedBean("#{comunicacaoRequerenteController}", ComunicacaoRequerenteController.class);

		if (isReenquadramento()) {
			comunicacaoRequerenteController.carregarReenquadramento(processoReenquadramentoDTO);
		} else {
			comunicacaoRequerenteController.carregarTela(requerimento);
		}
	}

	/**
	 * Método para listar os FCE's daquele {@link Requerimento}
	 * 
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param requerimento
	 * @since 09/04/2015
	 * @see Melhoria #6805
	 */
	private void carregarListaFce(Requerimento requerimento) {
		try {
			listaFce = fceServiceFacade.listarFceByIdeRequerimento(requerimento);
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para listar os {@link AtoDeclaratorio} daquele {@link Requerimento}.
	 * 
	 * @author eduardo.fernandes
	 * @since 16/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8411">#8411</a>
	 * @param requerimento
	 */
	private void carregarListaAtoDeclaratorio(Requerimento requerimento) {
		try {
			listaAtoDeclaratorio = atosDeclaratoriosFacade.listarAtoDeclaratorioBy(requerimento);
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarDeclaracoes(Integer ideRequerimento) {
		try {

			this.listaDeclaracaoIntervencao = this.declaracaoIntervencaoAppCaracteristcaService
					.listarDeclaracoes(ideRequerimento);

		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * FLAG para indicar se existe {@link Fce} para o {@link Requerimento}.
	 * 
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return <i>true</i> se existir pelo menos um {@link Fce}
	 * @since 09/04/2015
	 */
	public boolean isRenderedFCE() {
		return !Util.isNullOuVazio(listaFce);
	}

	/**
	 * FLAG para indicar se existe {@link AtoDeclaratorio} para o
	 * {@link Requerimento}.
	 * 
	 * @author eduardo.fernandes
	 * @return <i>true</i> se existir pelo menos um {@link AtoDeclaratorio}
	 * @since 16/01/2017
	 */
	public boolean isRenderedFormularioAtoDeclaratorio() {
		return !Util.isNullOuVazio(listaAtoDeclaratorio);
	}

	public StreamedContent imprimirRelatorio(Object object) {
		if (object instanceof Fce) {
			return imprimirRelatorio((Fce) object);
		} else if (object instanceof AtoDeclaratorio) {
			return imprimirRelatorio((AtoDeclaratorio) object);
		} else {
			JsfUtil.addErrorMessage("Não Ã© possí­vel imprimir esse tipo de relatÃ³rio.");
			return null;
		}
	}

	/**
	 * Método para imprimir o relatÃ³rio daquele {@link Fce}
	 * 
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fce
	 * @return <i>Arquivo .pdf</i>
	 * @since 09/04/2015
	 */
	private StreamedContent imprimirRelatorio(Fce fce) {
		try {
			return fceServiceFacade.getImprimirRelatorio(fce);
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para imprimir o relatÃ³rio daquele {@link AtoDeclaratorio}
	 * 
	 * @author eduardo.fernandes
	 * @param atoDeclaratorio
	 * @return <i>Arquivo .pdf</i>
	 * @since 16/01/2017
	 */
	private StreamedContent imprimirRelatorio(AtoDeclaratorio atoDeclaratorio) {
		try {
			return new ImpressoraAtoDeclaratorio().imprimirRelatorio(atoDeclaratorio);

		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarDocumentosObrigatorios(Integer ideRequerimento) throws Exception {

		if (reqAntigo == null) {
			reqAntigo = requerimentoService.isRequerimentoAntigo(ideRequerimento);
		}

		if (!reqAntigo) {
			boolean isTelaValidacao = true; // Melhoria #6643
			listaDocumentoObrigatorioEnquadramento = documentoObrigatorioService
					.listaDocumentoObrigatorioEnquadramentoDTO(ideRequerimento, isTelaValidacao);
		}
		listaDocumentoObrigatorioEnquadramentoAntigo = (List<DocumentoObrigatorioInterface>) documentoObrigatorioRequerimentoService
				.buscarDocsObrigatorioReqPorRequerimentoUnico(new RequerimentoUnico(ideRequerimento));
	}

	/**
	 * Método verifica se o requerimento foi feito na V1 mas enquadrado na V2
	 * 
	 * @param ideRequerimento
	 * @throws Exception
	 */
	private void docObrigatorioPossuiEnquadramentoAtoAmbiental(Integer ideRequerimento) throws Exception {

		if (reqAntigo) {
			for (DocumentoObrigatorioInterface doc : listaDocumentoObrigatorioEnquadramentoAntigo) {
				if (doc.getIdeEnquadramentoAtoAmbiental() != null) {
					reqAntigo = false;
					carregarDocumentosObrigatorios(ideRequerimento);
				}
			}
		} else {

			for (DocumentoObrigatorioInterface doc : listaDocumentoObrigatorioEnquadramentoAntigo) {
				if (doc.getIdeEnquadramentoAtoAmbiental() != null) {
					reqAntigo = false;
					carregarDocumentosObrigatorios(ideRequerimento);
				}
			}
		}
	}

	private void verificarDocumentosIdentificacao(Requerimento requerimento) throws Exception {

		Integer ideRequerimento = requerimento.getIdeRequerimento();

		Pessoa ideRequerente = null;

		if (!Util.isNullOuVazio(this.requerimento) && !Util.isNullOuVazio(this.requerimento.getRequerente())) {

			if (this.requerimento.getRequerente().getPessoaJuridica() != null
					&& !Util.isNullOuVazio(this.requerimento.getRequerente().getPessoaJuridica().getNumCnpj())) {

				ideRequerente = this.requerimento.getRequerente();

			} else if (this.requerimento.getRequerente().getPessoaFisica() != null
					&& !Util.isNullOuVazio(this.requerimento.getRequerente().getPessoaFisica().getNumCpf())) {

				ideRequerente = this.requerimento.getRequerente();
			}

			if (!Util.isNullOuVazio(ideRequerente)) {
				List<DocumentoIdentificacaoRequerimento> docsIdentificacaoRequerimento = this.documentoIdentificacaoRequerimentoService
						.buscarDocsIdentificacao(ideRequerimento, ideRequerente);

				if (Util.isNullOuVazio(docsIdentificacaoRequerimento)) {
					this.documentosIdentificacao = this.documentoIdentificacaoRequerimentoService
							.gerarDocumentosIdentificacaoRequerente(requerimento);
				} else {
					this.documentosIdentificacao = docsIdentificacaoRequerimento;
				}
			}
		}

		// SE O REQUERIMENTO FOR TLA, DEVE-SE ADICIONAR E TRAZER O DOCUMENTO DO NOVO
		// TITULAR
		adicionaDocumentoTLA(requerimento);
	}

	private void adicionaDocumentoTLA(Requerimento requerimento) throws Exception {

		SolicitacaoAdministrativo sa = solicitacaoAdministrativoService.obterSolicitacaoAdministrativa(requerimento,
				TipoSolicitacaoEnum.TRANSFERENCIA_LICENCA_AMBIENTAL);

		if (!Util.isNull(sa) && !Util.isNull(sa.getIdePessoaNovoTitular())) {
			List<DocumentoIdentificacao> documentosNovoTitular = documentoIdentificacaoService
					.listarDocumentosIdentificacaoPorPessoa(sa.getIdePessoaNovoTitular());

			if (!Util.isNullOuVazio(documentosNovoTitular)) {
				for (DocumentoIdentificacao di : documentosNovoTitular) {
					if (verificarNovaInsercaoDocumentoIdentificacao(di)) {
						DocumentoIdentificacaoRequerimento documentoIdentificacaoRequerimento = this.documentoIdentificacaoRequerimentoService
								.gerarDocumentoIdentificacaoRequerimento(requerimento, di);
						this.documentoIdentificacaoRequerimentoService.salvar(documentoIdentificacaoRequerimento);
						this.documentosIdentificacao.add(documentoIdentificacaoRequerimento);
					}
				}
			}
		}
	}

	private boolean verificarNovaInsercaoDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao) {
		for (DocumentoIdentificacaoRequerimento doc : documentosIdentificacao) {
			if (doc.getIdeDocumentoIdentificacao().equals(documentoIdentificacao)) {
				return false;
			}
		}
		return true;
	}

	public void carregarDocumentoRequerimento() throws Exception {

		documentoRequerimentoEmpreendimentoService.carregarDocumentoRequerimento(this);
	}

	public void verificarDocumentoArt() throws Exception {

		if (!Util.isNullOuVazio(empreendimentoRequerimento)) {
			listaDocumentoRequerimentoEmpreendimentos = documentoRequerimentoEmpreendimentoService
					.listarDocumentoRequerimentoEmpreendimento(this.empreendimentoRequerimento);

			List<DocumentoRequerimentoEmpreendimento> listaExcluirDocumentoRequerimentoEmpreendimentos = new ArrayList<DocumentoRequerimentoEmpreendimento>();

			for (DocumentoRequerimentoEmpreendimento documentoRequerimentoEmpreendimento : listaDocumentoRequerimentoEmpreendimentos) {

				if (!documentoRequerimentoEmpreendimento.isIndDocumentoValidado()) {
					documentoRequerimentoEmpreendimentoService
							.removerDocumentoRequerimentoEmpreendimento(documentoRequerimentoEmpreendimento);
					listaExcluirDocumentoRequerimentoEmpreendimentos.add(documentoRequerimentoEmpreendimento);
				}
			}

			listaDocumentoRequerimentoEmpreendimentos.removeAll(listaExcluirDocumentoRequerimentoEmpreendimentos);

			gerarDocumentoRequerimentoEmpreendimento();
		}
	}

	public List<DocumentoRequerimentoEmpreendimento> gerarDocumentoRequerimentoEmpreendimento() throws Exception {

		List<ResponsavelEmpreendimento> responsavelEmpreendimentos = responsavelEmpreendimentoService
				.listaResponsavelEmpreendimentoPorRequerimentoComArt(this.requerimento,
						empreendimentoRequerimento.getIdeEmpreendimento());

		List<DocumentoRequerimentoEmpreendimento> documentoRequerimentoEmpreendimentos = new ArrayList<DocumentoRequerimentoEmpreendimento>();

		if (!Util.isNullOuVazio(listaDocumentoRequerimentoEmpreendimentos)) {

			for (DocumentoRequerimentoEmpreendimento doc : listaDocumentoRequerimentoEmpreendimentos) {
				for (ResponsavelEmpreendimento responsavelEmpreendimento : responsavelEmpreendimentos) {
					if (!existeDocumentoRequerimentoEmpreendimentoRepresentante(doc, responsavelEmpreendimento)) {

						DocumentoRequerimentoEmpreendimento documentoRequerimentoEmpreendimento = new DocumentoRequerimentoEmpreendimento();

						documentoRequerimentoEmpreendimento
								.setIdeEmpreendimentoRequerimento(this.empreendimentoRequerimento);
						documentoRequerimentoEmpreendimento
								.setNomDocumento("ART-N°" + responsavelEmpreendimento.getNumART() + "-"
										+ responsavelEmpreendimento.getIdePessoaFisica().getNomPessoa());
						documentoRequerimentoEmpreendimento.setIdePessoaFisica(
								responsavelEmpreendimento.getIdePessoaFisica().getIdePessoaFisica());
						documentoRequerimentoEmpreendimento
								.setDscCaminhoArquivoAntigo(responsavelEmpreendimento.getDscCaminhoArquivoART());
						documentoRequerimentoEmpreendimento.setDtcValidacao(new Date());
						documentoRequerimentoEmpreendimento.setIdePessoaValidacao(getPessoaLogada());

						documentoRequerimentoEmpreendimentos.add(documentoRequerimentoEmpreendimento);

						documentoRequerimentoEmpreendimentoService
								.copiarArquivoArt(documentoRequerimentoEmpreendimento);

					}
				}
			}
		} else {
			for (ResponsavelEmpreendimento responsavelEmpreendimento : responsavelEmpreendimentos) {

				DocumentoRequerimentoEmpreendimento documentoRequerimentoEmpreendimento = new DocumentoRequerimentoEmpreendimento();

				documentoRequerimentoEmpreendimento.setIdeEmpreendimentoRequerimento(this.empreendimentoRequerimento);
				documentoRequerimentoEmpreendimento.setNomDocumento("ART-N°" + responsavelEmpreendimento.getNumART()
						+ "-" + responsavelEmpreendimento.getIdePessoaFisica().getNomPessoa());
				documentoRequerimentoEmpreendimento
						.setIdePessoaFisica(responsavelEmpreendimento.getIdePessoaFisica().getIdePessoaFisica());
				documentoRequerimentoEmpreendimento
						.setDscCaminhoArquivoAntigo(responsavelEmpreendimento.getDscCaminhoArquivoART());
				documentoRequerimentoEmpreendimento.setDtcValidacao(new Date());
				documentoRequerimentoEmpreendimento.setIdePessoaValidacao(getPessoaLogada());

				documentoRequerimentoEmpreendimentos.add(documentoRequerimentoEmpreendimento);

				documentoRequerimentoEmpreendimentoService.copiarArquivoArt(documentoRequerimentoEmpreendimento);
			}
		}

		listaDocumentoRequerimentoEmpreendimentos.addAll(documentoRequerimentoEmpreendimentos);

		return listaDocumentoRequerimentoEmpreendimentos;
	}

	public boolean existeDocumentoRequerimentoEmpreendimentoRepresentante(
			DocumentoRequerimentoEmpreendimento documentoRequerimentoEmpreendimento,
			ResponsavelEmpreendimento responsavelEmpreendimento) throws Exception {

		if (documentoRequerimentoEmpreendimento.getIdePessoaFisica()
				.equals(responsavelEmpreendimento.getIdePessoaFisica().getIdePessoaFisica())) {
			return true;
		}

		return false;

	}

	private void carregarDocumentosImovelRuralReq(Requerimento requerimento) throws Exception {

		documentosImovelRuralReq = this.documentoImovelRuralRequerimentoService
				.listarDocumentoImovelRuralReqByRequerimento(requerimento.getIdeRequerimento());

		if (Util.isNullOuVazio(documentosImovelRuralReq) && !Util.isNullOuVazio(empreendimentoRequerimento)
				&& !Util.isNullOuVazio(empreendimentoRequerimento.getIdeEmpreendimento())) {
			Integer ideEmpreendimento = empreendimentoRequerimento.getIdeEmpreendimento().getIdeEmpreendimento();
			List<DocumentoImovelRural> docsImovelRural = this.documentoImovelRuralRequerimentoService
					.listarDocumentoImovelRuralByEmpreendimento(ideEmpreendimento);

			for (DocumentoImovelRural documentoImovelRural : docsImovelRural) {
				DocumentoImovelRuralRequerimento docImoRurReq = new DocumentoImovelRuralRequerimento(
						documentoImovelRural);
				docImoRurReq.setIdeRequerimento(getRequerimento());
				docImoRurReq.setIndDocumentoValidado(false);
				documentosImovelRuralReq.add(docImoRurReq);
			}
			if (!Util.isNullOuVazio(docsImovelRural)) {
				documentoImovelRuralRequerimentoService
						.salvarListaDocumentoImovelRuralRequerimento(documentosImovelRuralReq);
			}
		}

	}

private void verificarDocumentosRepresentacao(Requerimento requerimento) throws Exception {
		
		ArrayList<DocumentoRepresentacaoRequerimento> docsRepresentacaoRequerimento = new ArrayList<DocumentoRepresentacaoRequerimento>();
		List<Integer> listaIdeEmpreendimento = null;
		List<RepresentanteLegal> listaRepresentanteLegal = null;
		List<ProcuradorRepEmpreendimento> listaProcuradorRepEmpreendimento = null;
		List<ProcuradorPfEmpreendimento> listaProcuradorPfEmpreendimento = null;
		
		listaIdeEmpreendimento = carregarListaIdeEmpreendimento();
			
		listaRepresentanteLegal = representanteLegalService.getListaRepresentanteLegalPorRequerimento(requerimento.getIdeRequerimento());
		
		if(!Util.isNullOuVazio(listaIdeEmpreendimento)) {
		
			listaProcuradorRepEmpreendimento = procuradorRepEmpreendimentoService.listarProcuradorRepEmpreendimentoPorRequerimento(requerimento.getIdeRequerimento(), listaIdeEmpreendimento);
			
			listaProcuradorPfEmpreendimento = procuradorPfEmpreendimentoService.listarProcuradorPfEmpreendimentoPorRequerimento(requerimento.getIdeRequerimento(), listaIdeEmpreendimento);
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
	
	
		this.documentosRepresentacao = docsRepresentacaoRequerimento;
	}

	public void salvar() {
		try {

			if (!isCRF()) {
				if (Util.isNull(this.requerimento.getIdeArea()) && !isReenquadramento()) {
					JsfUtil.addErrorMessage("Favor selecionar a Área onde o processo será formado.");
					return;
				}
			}

			if (isRequerimentoFoiTramitado()) {
				JsfUtil.addErrorMessage(Util.getString("requerimento_msg_ja_tramitado"));
				return;
			}

			validacaoPreviaServiceFacade.salvarValidacaoPrevia(this);
		} catch (FileNotFoundException e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(
					"Não foi possí­vel encontrar todos os arquivos necessários para finalizar a etapa.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possí­vel salvar a validação prévia.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private boolean isRequerimentoFoiTramitado() throws Exception {
		if (isReenquadramento()) {
			return !tramitacaoReenquadramentoProcessoServiceFacade.isTamitacaoRequerimentoAtual(
					processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento(),
					getTramitacaoReenquadramentoProcesso());
		}
		return !tramitacaoRequerimentoService.isTamitacaoRequerimentoAtual(requerimento.getIdeRequerimento(),
				tramitacaoRequerimento);
	}

	public boolean arquivosValidados() {
		if (!reqAntigo) {
			for (DocumentoObrigatorioEnquadramentoDTO documentoObrigatorioEnquadramentoDTO : listaDocumentoObrigatorioEnquadramento) {
				for (DocumentoObrigatorioInterface docObrigReq : documentoObrigatorioEnquadramentoDTO
						.getListaDocumentoObrigatorio()) {
					if (docObrigReq.getIndDocumentoValidado()) {
						docObrigReq.setIdePessoaValidacao(this.getPessoaLogada());
						docObrigReq.setDtcValidacao(new Date());
					} else {
						return false;
					}
				}
			}

		} else {
			for (DocumentoObrigatorioInterface documentoObrigatorioRequerimento : listaDocumentoObrigatorioEnquadramentoAntigo) {

				if (documentoObrigatorioRequerimento.getIndDocumentoValidado()) {

					documentoObrigatorioRequerimento.setIdePessoaValidacao(this.getPessoaLogada());
					documentoObrigatorioRequerimento.setDtcValidacao(new Date());
				} else {
					return false;
				}
			}
		}
		if (!Util.isNullOuVazio(documentosIdentificacao)) {
			for (DocumentoIdentificacaoRequerimento documentoIdentificacao : documentosIdentificacao) {
				if (documentoIdentificacao.getIndDocumentoValidado()) {
					documentoIdentificacao.setIdePessoaValidacao(this.getPessoaLogada());
					documentoIdentificacao.setDtcValidacao(new Date());
				} else {
					return false;
				}

			}
		}

		if (!Util.isNullOuVazio(documentosRepresentacao)) {
			for (DocumentoRepresentacaoRequerimento documentoRepresentacao : documentosRepresentacao) {
				if (documentoRepresentacao.getIndDocumentoValidado()) {
					documentoRepresentacao.setIdePessoaValidacao(this.getPessoaLogada());
					documentoRepresentacao.setDtcValidacao(new Date());
				} else {
					return false;
				}
			}
		}

		if (!Util.isNullOuVazio(documentosImovelRuralReq)) {
			for (DocumentoImovelRuralRequerimento docImovelRuralReq : documentosImovelRuralReq) {
				if (docImovelRuralReq.getIndDocumentoValidado()) {
					docImovelRuralReq.setIdePessoaValidacao(this.getPessoaLogada());
					docImovelRuralReq.setDtcValidacao(new Date());
				} else {
					return false;
				}
			}
		}

		if (!Util.isNullOuVazio(listaDocumentoRequerimentoEmpreendimentos)) {
			for (DocumentoRequerimentoEmpreendimento docReqEmp : listaDocumentoRequerimentoEmpreendimentos) {
				if (docReqEmp.isIndDocumentoValidado()) {
					docReqEmp.setIdePessoaValidacao(this.getPessoaLogada());
					docReqEmp.setDtcValidacao(new Date());
				} else {
					return false;
				}
			}
		}

		return true;
	}

	

	/**
	 * Obtem a entidade da interface DocumentoObrigatorioInterface e retorna na
	 * interface DocumentoValidacao para addição do Collection
	 */
	private DocumentoValidacao obterEntidadeDocumento(DocumentoObrigatorioInterface docObrigReq) {
		if (isReenquadramento()) {
			return (DocumentoObrigatorioReenquadramento) docObrigReq;
		}
		return (DocumentoObrigatorioRequerimento) docObrigReq;
	}

	private Collection<DocumentoValidacao> getDocumentosPendentes() {

		Collection<DocumentoValidacao> documentosPendentes = new ArrayList<DocumentoValidacao>();
		if (!reqAntigo) {
			for (DocumentoObrigatorioEnquadramentoDTO documentoObrigatorioEnquadramentoDTO : listaDocumentoObrigatorioEnquadramento) {
				for (DocumentoObrigatorioInterface docObrigReq : documentoObrigatorioEnquadramentoDTO
						.getListaDocumentoObrigatorio()) {
					if (!docObrigReq.getIndDocumentoValidado()) {
						DocumentoValidacao documentoValidacao = obterEntidadeDocumento(docObrigReq);
						documentosPendentes.add(documentoValidacao);
					}
				}
			}
		} else {
			for (DocumentoObrigatorioInterface docObrigReq : listaDocumentoObrigatorioEnquadramentoAntigo) {
				if (!docObrigReq.getIndDocumentoValidado()) {
					DocumentoValidacao documentoValidacao = obterEntidadeDocumento(docObrigReq);
					documentosPendentes.add(documentoValidacao);
				}
			}
		}

		if (documentosIdentificacao != null) {
			for (DocumentoIdentificacaoRequerimento docIdent : documentosIdentificacao) {
				if (!docIdent.getIndDocumentoValidado()) {
					documentosPendentes.add(docIdent);
				}
			}
		}

		if (documentosRepresentacao != null) {
			for (DocumentoRepresentacaoRequerimento docRep : documentosRepresentacao) {
				if (!docRep.getIndDocumentoValidado()) {
					documentosPendentes.add(docRep);
				}
			}
		}
		return documentosPendentes;
	}

	
	public void salvarComunicacaoDocumentoValidado() {

		try {

			Map<String, String> mapEmail = new HashMap<String, String>();
			
			
			String assunto = "SEIA - Comunicado - Documento Validado nº " + requerimento.getNumRequerimento();
			mapEmail.put("assunto",assunto);
			mapEmail.put("mensagem", emailDocumentoValidado);
			comunicacaoServiceFacade.gerarComunicacao(this.processoReenquadramentoDTO.getProcessoReenquadramento(), mapEmail);	

			RequestContext.getCurrentInstance().execute("emailDocumentoValidado.hide()");
			JsfUtil.addSuccessMessage("E-mail enviado com sucesso.");

		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);// log throw
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

	}
	
	
	public void salvarComunicacaoDocumentoPendente() {
		try {
			Map<String,String> mapEmail = new HashMap<String,String>();
			String nrRequerimento = "";
			Object obj = null;

			if (!Util.isNullOuVazio(this.processoReenquadramentoDTO)) {
				nrRequerimento = this.getProcessoReenquadramentoDTO().getProcessoReenquadramento().getNumProcesso();
				obj = this.getProcessoReenquadramentoDTO().getProcessoReenquadramento();
				mapEmail.put("mensagem", gerarEmailPadraoPendenciaValidacao());

				String assunto = "SEIA - Comunicado - Pendência de Documentação Requerimento de nº " + nrRequerimento;
				mapEmail.put("assunto", assunto);

				comunicacaoServiceFacade.gerarComunicacao(obj, mapEmail);
			}else {
				StringBuilder msg = new StringBuilder();
				msg.append("Prezado(a) ");
				msg.append(", \nFavor acessar o SEIA para verificar pendências na documentação enviada.\n \n");

				for (DocumentoValidacao documentoValidacao : getDocumentosPendentes()) {
					msg.append("\t \u25CF " + documentoValidacao.getDescricao() + "\n");
				}

				msg.append("\n Atte.,\n");
				msg.append("Central de Atendimento/INEMA.\n");

				this.emailPendenciaValidacao = msg.toString();
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			Util.capturarException(e, Util.SEIA_EXCEPTION); 
		}
	}
	  
	 
	public void gerarEmailDocumentoValidado() {
		emailDocumentoValidado = comunicacaoReenquadramentoProcessoServiceFacade.gerarEmailDocumentoValidado(processoReenquadramentoDTO);
	}

	public String gerarEmailPadraoPendenciaValidacao() {
		try {
			StringBuilder msg = new StringBuilder();
			msg.append("Prezado(a), ");
			msg.append(
					"Foram identificadas pendências durante a validação da documentação de formação do processo de reenquadramento  "
							+ processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso().getNumProcesso()
									.concat(", o que impede sua continuidade. \n\n"));
			msg.append("A lista de documentos com pendência é:\n\n");

			
			for (DocumentoObrigatorioEnquadramentoDTO documentoObrigatorioEnquadramentoDTO : listaDocumentoObrigatorioEnquadramento) {
				for (DocumentoObrigatorioInterface docObrigReq : documentoObrigatorioEnquadramentoDTO.getListaDocumentoObrigatorio()) {
					if (!docObrigReq.getIndDocumentoValidado()) {
					
						msg.append("\t\u25CF " + docObrigReq.getIdeDocumentoAto().getIdeDocumentoObrigatorio().getNomDocumentoObrigatorio()+ "\n\n");
					}	
				}
			}
		
			msg.append("Para dar continuidade, acesse o SEIA, no menu lateral à esquerda acione a aba "
					+ "\"PROCESSO\" em seguida " + "\"REENQUADRAMENTO\" e siga os seguintes passos:\n\n");
			msg.append(" Passo 1: Informe o número do processo mencionado acima no campo " + "\"Nº do processo:"
					+ "\" e acione o botão " + "\"Consultar" + "\";\n\n");
			msg.append(" Passo 2: Na coluna " + "\"Ações" + "\" acione o ícone  " + "\"Enviar documentação obrigatória"
					+ "\";\n\n");
			msg.append(" Passo 3: Na tela " + "\"ENVIAR DOCUMENTO OBRIGATÓRIO"
					+ "\" preencha o Formulário de Caracterização do Empreendimento - FCE, caso tenho sido solicitado;\n\n");
			msg.append(" Passo 4: Na coluna " + "\"Ações" + "\" acione o ícone " + "\"Upload de arquivo"
					+ "\"e carregue o arquivo correspondente ao documento solicitado para cada ato ambiental;\n\n");
			msg.append(" Passo 5: Acione o botão " + "\"Salvar" + "\" e aguarde novas orientações.\n\n");

			msg.append("Atenciosamente,\n");
			msg.append("Central de Atendimento/INEMA.\n");

			this.emailPendenciaValidacao = msg.toString();
			return msg.toString();
		
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);// log
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
				
	}

	public void salvarComunicacaoPendenciaValidacao() {
		try {
			String assunto = "SEIA - Comunicado - Pendência de Documentação Requerimento de nº "
					+ requerimento.getNumRequerimento();

			this.comunicacaoRequerimentoService.salvar(this.requerimento, assunto, emailPendenciaValidacao);
			this.requerimento = requerimentoService.buscarEntidadePorId(this.requerimento);

			emailService.enviarEmailsAoRequerente(requerimento, assunto, emailPendenciaValidacao);

			RequestContext.getCurrentInstance().execute("emailPendenciaValidacao.hide()");
			JsfUtil.addSuccessMessage("E-mail enviado com sucesso.");
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);// log
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private Pessoa getPessoaLogada() {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return new Pessoa(usuario.getIdePessoaFisica());
	}

	public StreamedContent getFileDownload(String caminhoArquivo) {
		StreamedContent file = null;

		try {
			InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
			String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
			file = new DefaultStreamedContent(stream, mimeType,
					caminhoArquivo.substring(caminhoArquivo.lastIndexOf(File.separator) + 1));
		} catch (FileNotFoundException e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);// log
			JsfUtil.addErrorMessage("Erro ao tentar fazer o download do arquivo");
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

		return file;
	}

	public void invalidarDocumento() {
		this.documentoObrigatorioRequerimento.setIndDocumentoValidado(false);
	}

	public void gerenciarUploadDocumentoObrigatorio(FileUploadEvent event) {
		DataInputStream dataInputStream = null;
		try {
			UploadedFile arquivo = event.getFile();

			documentoObrigatorioSelecionado.setFileUpload(event);
			dataInputStream = new DataInputStream(new BufferedInputStream(arquivo.getInputstream()));
			documentoObrigatorioSelecionado.setFile(
					new DefaultStreamedContent(dataInputStream,
							arquivo.getContentType(), arquivo.getFileName()));

			documentoObrigatorioSelecionado.setFileSize(arquivo.getSize());

			DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento = documentoObrigatorioSelecionado.clone();

			this.vincularOperadorDocumentoObrigatorio(documentoObrigatorioRequerimento);

			this.documentoObrigatorioRequerimentoService.atualizar(documentoObrigatorioRequerimento, arquivo);

			Integer ideRequerimento = documentoObrigatorioRequerimento.getIdeRequerimento().getIdeRequerimento();

			this.carregarDocumentosObrigatorios(ideRequerimento);
			dataInputStream.close();
			JsfUtil.addSuccessMessage("Arquivo enviado com sucesso!");
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void vincularOperadorDocumentoObrigatorio(
			DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) {
		Pessoa pessoaUpload = getPessoaLogada();
		documentoObrigatorioRequerimento.setIdePessoaUpload(pessoaUpload);
	}

	public boolean isCRF() {
		try {
			if (!Util.isNullOuVazio(requerimento)) {
				CumprimentoReposicaoFlorestal crf = cumprimentoReposicaoFlorestalService
						.obterCumprimentoReposicaoFlorestalPorRequerimento(requerimento);
				if (!Util.isNullOuVazio(crf)) {
					return true;
				}
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}

		return false;

	}
	private boolean isExisteRepresentante(List<RepresentanteLegal> listaRepresentanteLegal,List<ProcuradorRepEmpreendimento> listaProcuradorRepEmpreendimento,List<ProcuradorPfEmpreendimento> listaProcuradorPfEmpreendimento) {
		return !Util.isNullOuVazio(listaRepresentanteLegal) || !Util.isNullOuVazio(listaProcuradorPfEmpreendimento) || !Util.isNullOuVazio(listaProcuradorRepEmpreendimento);
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

	public Collection<DocumentoIdentificacaoRequerimento> getDocumentosIdentificacao() {
		return documentosIdentificacao;
	}

	public void setDocumentosIdentificacao(Collection<DocumentoIdentificacaoRequerimento> documentosIdentificacao) {
		this.documentosIdentificacao = documentosIdentificacao;
	}

	public Collection<DocumentoRepresentacaoRequerimento> getDocumentosRepresentacao() {
		return documentosRepresentacao;
	}

	public void setDocumentosRepresentacao(Collection<DocumentoRepresentacaoRequerimento> documentosRepresentacao) {
		this.documentosRepresentacao = documentosRepresentacao;
	}

	public String getEmailPendenciaValidacao() {
		return emailPendenciaValidacao;
	}

	public void setEmailPendenciaValidacao(String emailPendenciaValidacao) {
		this.emailPendenciaValidacao = emailPendenciaValidacao;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public DocumentoObrigatorioRequerimento getDocumentoObrigatorioRequerimento() {
		return documentoObrigatorioRequerimento;
	}

	public void setDocumentoObrigatorioRequerimento(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) {
		this.documentoObrigatorioRequerimento = documentoObrigatorioRequerimento;
	}

	public List<DocumentoImovelRuralRequerimento> getDocumentosImovelRuralReq() {
		return documentosImovelRuralReq;
	}

	public void setDocumentosImovelRuralReq(List<DocumentoImovelRuralRequerimento> documentosImovelRuralReq) {
		this.documentosImovelRuralReq = documentosImovelRuralReq;
	}

	public List<DocumentoObrigatorioInterface> getListaDocumentoObrigatorioEnquadramentoAntigo() {
		return listaDocumentoObrigatorioEnquadramentoAntigo;
	}

	public void setListaDocumentoObrigatorioEnquadramentoAntigo(
			List<DocumentoObrigatorioInterface> listaDocumentoObrigatorioEnquadramentoAntigo) {
		this.listaDocumentoObrigatorioEnquadramentoAntigo = listaDocumentoObrigatorioEnquadramentoAntigo;
	}

	public Boolean getReqAntigo() {
		return reqAntigo;
	}

	public void setReqAntigo(Boolean reqAntigo) {
		this.reqAntigo = reqAntigo;
	}

	public List<DocumentoObrigatorioEnquadramentoDTO> getListaDocumentoObrigatorioEnquadramento() {
		return listaDocumentoObrigatorioEnquadramento;
	}

	public void setListaDocumentoObrigatorioEnquadramento(
			List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramento) {
		this.listaDocumentoObrigatorioEnquadramento = listaDocumentoObrigatorioEnquadramento;
	}

	public DocumentoObrigatorioRequerimento getDocumentoObrigatorioSelecionado() {
		return documentoObrigatorioSelecionado;
	}

	public void setDocumentoObrigatorioSelecionado(DocumentoObrigatorioRequerimento documentoObrigatorioSelecionado) {
		this.documentoObrigatorioSelecionado = documentoObrigatorioSelecionado;
	}

	public List<Fce> getListaFce() {
		return listaFce;
	}

	public TramitacaoRequerimento getTramitacaoRequerimento() {
		return tramitacaoRequerimento;
	}

	public void setTramitacaoRequerimento(TramitacaoRequerimento tramitacaoRequerimento) {
		this.tramitacaoRequerimento = tramitacaoRequerimento;
	}

	public List<AtoDeclaratorio> getListaAtoDeclaratorio() {
		return listaAtoDeclaratorio;
	}

	public List<DeclaracaoIntervencaoAppCaracteristca> getListaDeclaracaoIntervencao() {
		return listaDeclaracaoIntervencao;
	}

	public void setListaDeclaracaoIntervencao(List<DeclaracaoIntervencaoAppCaracteristca> listaDeclaracaoIntervencao) {
		this.listaDeclaracaoIntervencao = listaDeclaracaoIntervencao;
	}

	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}

	public void setProcessoReenquadramentoDTO(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}

	public TramitacaoReenquadramentoProcesso getTramitacaoReenquadramentoProcesso() {
		return tramitacaoReenquadramentoProcesso;
	}

	public void setTramitacaoReenquadramentoProcesso(
			TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcesso) {
		this.tramitacaoReenquadramentoProcesso = tramitacaoReenquadramentoProcesso;
	}

	public List<DocumentoRequerimentoEmpreendimento> getListaDocumentoRequerimentoEmpreendimentos() {
		return listaDocumentoRequerimentoEmpreendimentos;
	}

	public void setListaDocumentoRequerimentoEmpreendimentos(
			List<DocumentoRequerimentoEmpreendimento> listaDocumentoRequerimentoEmpreendimentos) {
		this.listaDocumentoRequerimentoEmpreendimentos = listaDocumentoRequerimentoEmpreendimentos;
	}

	public String getEmailDocumentoValidado() {
		return emailDocumentoValidado;
	}

	public void setEmailDocumentoValidado(String emailDocumentoValidado) {
		this.emailDocumentoValidado = emailDocumentoValidado;
	}
}