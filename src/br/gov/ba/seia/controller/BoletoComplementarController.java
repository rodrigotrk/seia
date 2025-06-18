package br.gov.ba.seia.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.hamcrest.core.IsNull;
import org.jrimum.bopepo.view.BoletoViewer;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.com.caelum.stella.boleto.transformer.GeradorDeBoleto;
import br.gov.ba.seia.dto.BoletoComplementarDTO;
import br.gov.ba.seia.dto.BoletoComplementarFilter;
import br.gov.ba.seia.dto.EnviarComprovantePagamentoDTO;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.BoletoDaeHistorico;
import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.BoletoPagamentoHistorico;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.DadoBancario;
import br.gov.ba.seia.entity.MotivoCancelamentoBoleto;
import br.gov.ba.seia.entity.MotivoIsencaoBoleto;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.StatusBoletoPagamento;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TipoBoletoPagamento;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.BancoEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.StatusBoletoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoBoletoPagamentoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.BoletoComplementarServiceFacade;
import br.gov.ba.seia.facade.TramitacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BoletoComplementarService;
import br.gov.ba.seia.service.BoletoDaeHistoricoService;
import br.gov.ba.seia.service.BoletoDaeRequerimentoService;
import br.gov.ba.seia.service.BoletoPagamentoHistoricoService;
import br.gov.ba.seia.service.BoletoPagamentoRequerimentoService;
import br.gov.ba.seia.service.BoletoService;
import br.gov.ba.seia.service.ComprovantePagamentoDaeService;
import br.gov.ba.seia.service.ComprovantePagamentoService;
import br.gov.ba.seia.service.DadoBancarioService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.TipoBoletoPagamentoService;
import br.gov.ba.seia.service.TramitacaoProcessoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.service.VwRequerentePfExternoService;
import br.gov.ba.seia.util.BoletoUtil;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityController;

/**
 * Controller da tela "Boleto Complementar".
 * 
 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
 * @since 06/11/2013
 */
@Named("boletoComplementarController")
@ViewScoped
public class BoletoComplementarController {
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");

	// SERVICES
	@EJB
	private PessoaFisicaService pessoaFisicaService;

	@EJB
	private PessoaJuridicaService pessoaJuridicaService;

	@EJB
	private BoletoComplementarService boletoComplementarService;

	@EJB
	private RequerimentoService requerimentoService;

	@EJB
	private ProcessoService processoService;

	@EJB
	private VwRequerentePfExternoService vwRequerentePfExternoService;

	@EJB
	private BoletoService boletoService;

	@EJB
	private TipoBoletoPagamentoService tipoBoletoPagamentoService;

	@EJB
	private BoletoPagamentoHistoricoService boletoPagamentoHistoricoService;

	@EJB
	private DadoBancarioService dadoBancarioService;

	@EJB
	private PessoaService pessoaService;

	@EJB
	private BoletoPagamentoRequerimentoService boletoPagamentoRequerimentoService;

	@EJB
	private ComprovantePagamentoService comprovantePagamentoService;
	
	@EJB
	private BoletoDaeRequerimentoService boletoDaeRequerimentoService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoservice;

	@EJB
	private ComprovantePagamentoDaeService comprovantePagamentoDaeService;
	
	@EJB
	private BoletoDaeHistoricoService boletoDaeHistoricoService;
	
	@EJB
	private BoletoComplementarServiceFacade boletoComplementarServiceFacade;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;
	
	@EJB
	private TramitacaoProcessoService tramitacaoProcessoService;
	
	// OBJETOS DA TELA PRINCIPAL
	private BoletoComplementarFilter filter;

	private boolean datas;

	private DataTable dataTableBoleto;

	private LazyDataModel<BoletoComplementarDTO> boletoModel;

	private boolean usuarioExterno;

	// OBJETOS DO POPUP DE SELECIONAR REQUERENTE
	private DataTable dataTablePessoaFisica;

	private DataTable dataTablePessoaJuridica;

	private LazyDataModel<PessoaFisica> pessoaFisicaModel;

	private LazyDataModel<PessoaJuridica> pessoaJuridicaModel;

	private boolean renderizaGrid;

	private boolean pessoaFisica;

	private String nomeRequerenteFilter;

	private String documentoRequerenteFilter;

	// OBJETOS DO POPUP DE NOVO BOLETO
	private List<TipoBoletoPagamento> listTipoBoleto;

	private TipoBoletoPagamento novoBoletoTipo;

	private String novoBoletoNumero;

	private Requerimento novoBoletoRequerimento;

	private Processo novoBoletoProcesso;

	private String novoBoletonomeRequerente;

	private String novoBoletoCpfCnpjRequerente;

	private boolean novoBoletoRenderizaPanelDados;

	private Double novoBoletoValor;

	private Date novoBoletoDataVencimento;
	
	private String novoBoletoMensagemErro;
	
	private boolean novoBoletoBoleto;
	
	private boolean novoBoletoDae;
	
	private BigDecimal novoBoletoVlrTotalVistoria;
	
	private BigDecimal novoBoletoVlrTotalCertificado;
	
	private EnviarComprovantePagamentoDTO enviarComprovantePagamentoDTO;
	
	private ProcessoReenquadramentoDTO processoReenquadramentoDTO; 
	
	private BoletoComplementarDTO boletoReequerimento;
	
	private BoletoComplementarDTO daeReequerimento;
	
	private boolean novoBoletoIsencaoBoleto;
	
	private boolean novoBoletoIsencaoDae;
	
	private MotivoIsencaoBoleto novoBoletoMotivoIsencaoBoleto;
	
	private MotivoIsencaoBoleto novoBoletoMotivoIsencaoDae;
	
	private List<MotivoIsencaoBoleto> listaMotivoIsencaoBoletoParaReenquadramento;
	
	// OBJETOS DO POPUP ENVIAR COMPROVANTE
	private BoletoComplementarDTO enviarComprovanteBoleto;
	private ComprovantePagamentoDae comprovanteVistoria;
	private ComprovantePagamentoDae comprovanteCertificado;
	private ComprovantePagamento comprovanteBoleto;
	private String textoValorVistoria;
	private String textoValorCertificado;
	
	// OBJETOS POPUP CANCELAR BOLETO
	private BoletoComplementarDTO boletoCancelar;

	private BoletoPagamentoRequerimento boletoPagReqCancelar;
	private BoletoDaeRequerimento boletoDaeReqCancelar;

	private BoletoPagamentoHistorico boletoPagamentHistorico;
	private BoletoPagamentoHistorico boletoPagamentHistUltimo;
	private BoletoDaeHistorico boletoDaeHistUltimo;
	private BoletoDaeHistorico boletoDaeHistorico;
	
	private Boolean statusCancelar;
	private Boolean cancelamentoSolicitado;
	private Boolean emitido;
	
	private List<MotivoCancelamentoBoleto> listaMotivoCancelamentoBoleto;

	// OBJETOS DO POPUP VALIDAR COMPROVANTE
	private Pessoa validarComprovantePessoa;

	private BoletoComplementarDTO validarComprovanteBoletoComplementar;

	private String validarComprovanteNumRequerimentoProcesso;

	private String validarComprovanteTamanhoArquivo;

	private boolean validarComprovanteDocumentoValidado;

	private ComprovantePagamento validarComprovanteComprovantePagamento;
	
	private ComprovantePagamentoDae validarComprovanteComprovantePagamentoDae;

	private BoletoPagamentoRequerimento validarComprovanteBoletoPagamentoRequerimento;

	private BoletoDaeRequerimento validarComprovanteBoletoDaeRequerimento;
	
	private String labelNumRequerimentoOuProcesso = StringUtils.EMPTY;
	
	private String validarComprovanteDscInvalidacaoComprovante;
	
	private List<ComprovantePagamentoDae> listaComprovanteDae;
	
	// OBJETOS DO POPUP HISTORICO DO BOLETO
	private BoletoComplementarDTO historicoBoletoBoletoComplementar;
	
	private BoletoPagamentoRequerimento historicoBoletoBoletoPagamentoRequerimento;
	
	private Pessoa historicoBoletoPessoa;
	
	private List<BoletoPagamentoHistorico> historicoBoletoDataTable;
	
	private String historicoBoletoNumProcesso;
	
	private Requerimento historicoBoletoRequerimento;
	
	private boolean pesquisando;
	
	private boolean incluindo;
	
	private boolean editando;
	
	private boolean excluindo;
	
	private BoletoDaeRequerimento historicoBoletoBoletoDaeRequerimento;

	private List<BoletoDaeHistorico> historicoBoletoDataTableDae;
	
	/*
	 * 
	 * 
	 * MÉTODOS
	 * 
	 * 
	 */

	@PostConstruct
	public void load() {

		SecurityController security = (SecurityController) SessaoUtil.recuperarManagedBean("#{security}", SecurityController.class);

		// Através desta ação podemos saber se o usuário deverá ser interpretado como um usuário externo.
		if (security.temAcesso("3.56.4")) {
			setUsuarioExterno(Boolean.FALSE);
		} else if (security.temAcesso("3.57.4")) {
			setUsuarioExterno(Boolean.TRUE);
		} else {
			setUsuarioExterno(Boolean.FALSE);
		}

		setPesquisando(Boolean.TRUE);
		setPessoaFisica(Boolean.TRUE);
		
		limpar();
		
		if (Util.isNullOuVazio(getEnviarComprovantePagamentoDTO())){
			setEnviarComprovantePagamentoDTO(new EnviarComprovantePagamentoDTO());
		}
	}

	public void limpar() {

		if (isPesquisando()) {
			limparTelaPesquisa();
			limparPopupRequerente();
		} else if (isIncluindo()) {
			limparPopupNovoBoleto();
		}
	}

	/**
	 * Limpa a tela de pesquisa.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	public void limparTelaPesquisa() {

		setFilter(new BoletoComplementarFilter());
		setBoletoModel(null);
		setDataTableBoleto(null);
	}

	/**
	 * Limpa o popup que seleciona o requerente.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	public void limparPopupRequerente() {

		setPessoaFisicaModel(null);
		setPessoaJuridicaModel(null);
		setNomeRequerenteFilter(null);
		setDataTablePessoaFisica(null);
		setRenderizaGrid(Boolean.FALSE);
		setDataTablePessoaJuridica(null);
		setDocumentoRequerenteFilter(null);
	}

	/**
	 * Limpa o popup que emite um novo boleto.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	public void limparPopupNovoBoleto() {

		setListTipoBoleto(null);
		setNovoBoleto_tipo(null);
		setNovoBoleto_valor(null);
		setNovoBoleto_numero(null);
		setNovoBoleto_processo(null);
		setNovoBoleto_requerimento(null);
		setNovoBoleto_dataVencimento(null);
		setNovoBoleto_nomeRequerente(null);
		setNovoBoleto_cpfCnpjRequerente(null);
		setNovoBoleto_renderizaPanelDados(Boolean.FALSE);
		setNovoBoleto_boleto(Boolean.FALSE);
		setNovoBoleto_dae(Boolean.FALSE);
		setNovoBoleto_isencaoBoleto(Boolean.FALSE);
		setNovoBoleto_isencaoDae(Boolean.FALSE);
		setNovoBoleto_motivoIsencaoBoleto(null);
		setNovoBoleto_motivoIsencaoDae(null);
	}

	/**
	 * Limpa o popup que valida o comprovante de pagamento.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	public void limparPopUpValidarComprovante() {

		setValidarComprovante_pessoa(null);
		setValidarComprovante_tamanhoArquivo(null);
		setValidarComprovante_comprovantePagamento(null);
		setValidarComprovante_numRequerimentoProcesso(null);
		setValidarComprovante_documentoValidado(Boolean.TRUE);
		setValidarComprovante_boletoPagamentoRequerimento(null);
		setValidarComprovante_dscInvalidacaoComprovante("Favor acessar o SEIA para verificar a(s) pendência(s) no comprovante enviado. \n\nAtte. \n\nASCAI/CTFA/INEMA");
	}
	
	/**
	 * Limpa o popup de historico do boleto.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	public void limparPopUpHistoricoBoleto() {
		setHistoricoBoleto_pessoa(null);
		setHistoricoBoleto_dataTable(null);
	}
	
	public void limparUpload() {
		setTextoValorCertificado(null);
		setTextoValorVistoria(null);
		setEnviarComprovantePagamentoDTO(new EnviarComprovantePagamentoDTO());
		setProcessoReenquadramentoDTO(null);
	}
	
	private void limparCancelamento(){
		setBoletoPagReqCancelar(null);
		setBoletoDaeReqCancelar(null);
		setBoletoPagamentHistorico(null);
		setBoletoPagamentHistUltimo(null);
		setBoletoDaeHistUltimo(null);
		setBoletoDaeHistorico(null);
		setStatusCancelar(null);
		setCancelamentoSolicitado(null);
		setEmitido(null);
	}

	/**
	 * Método que limpa o filtro das datas dos status da tela de consulta do boleto complementar.
	 * INDEX: 0-Em Processamento, 1-Emitido, 2-Cancelamento Solicitado, 3-Comprovante Enviado, 4-Pago, 5-Cancelado, 6-Vencido.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param e - Evento de clicar no checkbox
	 * @param index - Utilizado para identificar qual a data deve ser limpa. 
	 */
	public void limparDatas(ValueChangeEvent e, int index) {
		
		if(index == 0) {
			filter.setDtEmProcessamentoInicial(null);
			filter.setDtEmProcessamentoFinal(null);
		} else if(index == 1) {
			filter.setDtEmitidoInicial(null);
			filter.setDtEmitidoFinal(null);
		} else if(index == 2) {
			filter.setDtCancelamentoInicial(null);
			filter.setDtCancelamentoFinal(null);
		} else if(index == 3) {
			filter.setDtComprovanteInicial(null);
			filter.setDtComprovanteFinal(null);
		} else if(index == 4) {
			filter.setDtPagoInicial(null);
			filter.setDtPagoFinal(null);
		} else if(index == 5) {
			filter.setDtCanceladoInicial(null);
			filter.setDtCanceladoFinal(null);
		} else if(index == 6) {
			filter.setDtVencidoInicial(null);
			filter.setDtVencidoFinal(null);
		}
	}
	
	public void carregarBoleto(BoletoComplementarDTO enviarComprovante_boleto) throws Exception{
		limparUpload();
		
		if (!Util.isNullOuVazio(enviarComprovante_boleto.getIdeProcessoReenquadramento())){
			enviarComprovantePagamentoDTO.setBoleto(boletoComplementarService.carragerBoleto(this, enviarComprovante_boleto.getIdeProcessoReenquadramento()));
			enviarComprovantePagamentoDTO.setCertificado(boletoComplementarService.carregarCertificadoDae(this, enviarComprovante_boleto.getIdeProcessoReenquadramento()));
			enviarComprovantePagamentoDTO.setVistoria(boletoComplementarService.carregarVistoriaDae(this, enviarComprovante_boleto.getIdeProcessoReenquadramento()));
		}
	}

	public void carregarEnviarComprovanteBoleto(ProcessoReenquadramentoDTO processoReenquadramentoDTO) throws Exception{
		limparUpload();
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
		boletoComplementarService.carregarEnviarComprovanteBoleto(this);
		Html.atualizar("formDialogComprovante", "certificadoForm", "vistoriaForm", "comprovanteDaeBt");
		Html.exibir("dialogEnviarComprovante");
	}
	
	public void consultar() {

		if(verificaAlgumFiltroPreenchido()) {
			
			getDataTableBoleto().setFirst(0);
			getDataTableBoleto().setPage(1);
			
			try {
				
				if (!validar()) {
					MensagemUtil.erro(BUNDLE.getString("geral_msg_periodo_invalido"));
					return;
				}
				
				setBoletoModel(new LazyDataModel<BoletoComplementarDTO>() {
					
					private static final long serialVersionUID = -2208228405486565761L;
					
					@Override
					public List<BoletoComplementarDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
					
						List<BoletoComplementarDTO> lista = null;
						
						try {
							
							if (isUsuarioExterno()) {
								
								Integer idePessoa = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
								
								PessoaFisica pf = new PessoaFisica();
								pf.setIdePessoaFisica(idePessoa);
								pf.setPessoa(new Pessoa(idePessoa));
								
								getFilter().setListPF((List<PessoaFisica>) vwRequerentePfExternoService.listarVwRequerentePfExterno(pf));
								getFilter().setListPJ(pessoaJuridicaService.listarPessoaJuridicaRepresentada(pf));
								
								lista = boletoComplementarService.consultarBoletoComplementar(getFilter(), first, pageSize, isUsuarioExterno());
							} else {
								lista = boletoComplementarService.consultarBoletoComplementar(getFilter(), first, pageSize, isUsuarioExterno());
							}
						} catch (Exception e) {
							getBoletoModel().setRowCount(0);
							Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
							throw Util.capturarException(e, Util.SEIA_EXCEPTION);
						}
						
						return lista;
					}
				});
				
				if (isUsuarioExterno()) {
					
					Integer idePessoa = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
					
					PessoaFisica pf = new PessoaFisica();
					pf.setIdePessoaFisica(idePessoa);
					pf.setPessoa(new Pessoa(idePessoa));
					
					getFilter().setListPF((List<PessoaFisica>) vwRequerentePfExternoService.listarVwRequerentePfExterno(pf));
					
					PessoaJuridica pj = new PessoaJuridica();
					pj.setIdePessoaJuridica(idePessoa);
					pj.setPessoa(new Pessoa(idePessoa));
					
					getFilter().setListPJ(pessoaJuridicaService.filtrarPJRequerenteExterno(pj));
					
					getBoletoModel().setRowCount(boletoComplementarService.consultarBoletoComplementarCount(getFilter(), isUsuarioExterno()));
				} else {
					getBoletoModel().setRowCount(boletoComplementarService.consultarBoletoComplementarCount(getFilter(), isUsuarioExterno()));
				}
				
			} catch (Exception e) {
				getBoletoModel().setRowCount(0);
				Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		} else {
			MensagemUtil.erro("Favor preencher pelo menos um dos parâmetros de consulta.");
		}
	}
	
	/**
	 * Verifica se algum dos filtros foi preenchido.
	 * 
	 * @return TRUE para algum filtro preenchido, FALSE para nenhum filtro preenchido.
	 */
	public boolean verificaAlgumFiltroPreenchido() {
		if(filter.isStatusSelecionado() || !Util.isNullOuVazio(filter.getRequerente()) || !Util.isNullOuVazio(filter.getNumRequerimento()) 
				|| !Util.isNullOuVazio(filter.getNumProcesso()) || !Util.isNullOuVazio(filter.getNumBoleto())) {
			
			return true;
		} else {
			return false;
		}
	}
	
	public void prepararParaSalvar() {
		boletoComplementarServiceFacade.salvarBoletoComplementar(this);
	}

	public void salvar() {
		BoletoPagamentoRequerimento boletoPagReq = null;
		BoletoPagamentoHistorico bph = null;
		BoletoDaeRequerimento boletoDAEReq = null;
		
		try {
			if (novoBoletoBoleto || novoBoletoTipo.getIdeTipoBoletoPagamento() != 7){
				boletoPagReq = preencherCamposBoleto();	
			
				if (boletoPagReq == null) return;
				
				if(!novoBoletoIsencaoBoleto) bph = (BoletoPagamentoHistorico) boletoPagReq.getBoletoPagamentoHistoricoCollection().iterator().next();
				
				if (isReenquadramento()) boletoPagReq.setIdeProcessoReenquadramento(processoReenquadramentoDTO.getProcessoReenquadramento());
				
				boletoService.salvarBoleto(boletoPagReq);
				if(!novoBoletoIsencaoBoleto) boletoPagamentoHistoricoService.salvar(bph);
			} 

			if (novoBoletoDae){
				boletoDAEReq = preencherCamposDAE();
				
				if (boletoDAEReq == null) return;
				
				if (isReenquadramento()) boletoDAEReq.setIdeProcessoReenquadramento(processoReenquadramentoDTO.getProcessoReenquadramento());
				
				boletoDaeRequerimentoService.salvarOuAtualizar(boletoDAEReq);
			}
			
			if (boletoPagReq == null && boletoDAEReq == null){
				MensagemUtil.alerta(BUNDLE.getString("gerar_novo_boleto_msg_erro_slecionar_boleto_dae"));
				return;
			}
			
			if (isReenquadramento()) tramitarStatusReenquadramentoAoSalvar();
			
			if(!novoBoletoIsencaoBoleto && !novoBoletoIsencaoDae) {
				if(!Util.isNullOuVazio(novoBoletoProcesso)) {
					enviarEmails(null, novoBoletoProcesso, false);
				} else if(!Util.isNullOuVazio(novoBoletoRequerimento)) {
					enviarEmails(novoBoletoRequerimento, null, false);
				}
			}
			
			Html.esconder("dialogGerarNovoBoleto");
			MensagemUtil.sucesso(BUNDLE.getString("gerar_novo_boleto_msg_sucesso_salvar"));
			
			((PautaReenquadramentoProcessoController) 
					SessaoUtil.recuperarManagedBean("#{pautaReenquadramentoProcessoController}", PautaReenquadramentoProcessoController.class))
						.consultar();
			
			Html.atualizar("frmPautaReenquadramentoProcesso");
		} catch (Exception e) {
			MensagemUtil.erro(BUNDLE.getString("gerar_novo_boleto_msg_erro_salvar"));
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void tramitarStatusReenquadramentoAoSalvar() throws Exception {
		/**
		 * Caso o usuário não solicite nenhuma isenção
		 */
		if(!novoBoletoIsencaoBoleto && !novoBoletoIsencaoDae) {
			if (novoBoletoBoleto){
				tramitarReenquadramentoProcesso(StatusReenquadramentoEnum.BOLETO_EM_PROCESSAMENTO);
				
			} else if(novoBoletoDae){
				tramitarReenquadramentoProcesso(StatusReenquadramentoEnum.BOLETO_PAGAMENTO_LIBERADO);
			}
		} else 
			/**
			 * Caso 1: O usuário seleciona apenas a opção "Boleto" e informa que será isento de cobrança
			 * Caso 2: O usuário seleciona apenas a opção "DAE" e informa que será isento de cobrança 
			 * Caso 3: O usuário seleciona a opção "Boleto" e a opção "DAE" e isenta as duas opções 
			 */
			if((novoBoletoIsencaoBoleto && !novoBoletoDae)
				|| (novoBoletoIsencaoDae && !novoBoletoBoleto)
				|| (novoBoletoIsencaoBoleto && novoBoletoIsencaoDae)) {
				
				tramitarReenquadramentoProcesso(StatusReenquadramentoEnum.REENQUADRADO);
				tramitarProcesso();
		} else
			/**
			 * Caso o usuário seleciona a opção "Boleto" e a opção "DAE" e isenta apenas o boleto
			 */
			if(novoBoletoIsencaoBoleto && novoBoletoDae && !novoBoletoIsencaoDae) {
				tramitarReenquadramentoProcesso(StatusReenquadramentoEnum.BOLETO_PAGAMENTO_LIBERADO);
				
		} else
			/**
			 * Caso o usuário seleciona a opção "Boleto" e a opção "DAE" e isenta apenas o DAE 
			 */
			if(novoBoletoBoleto && novoBoletoIsencaoDae) {
				tramitarReenquadramentoProcesso(StatusReenquadramentoEnum.BOLETO_EM_PROCESSAMENTO);
		}
	}
	
	/***
	 * Tramita o processo de forma parametrizada
	 * @param ideProcesso
	 * @param ideStatus
	 * @throws Exception
	 */
	public void tramitarProcesso(Integer ideProcesso, Integer ideStatus) throws Exception {

		
		tramitacaoProcessoService.tramitarProcessoAtualizandoAnterior(
				ideProcesso,
				ideStatus, AreaEnum.DIRRE.getId());

	}

	/***
	 * Tramita o processo 
	 * @param ideProcesso
	 * @param ideStatus
	 * @throws Exception
	 */
	private void tramitarProcesso() throws Exception {

		tramitacaoProcessoService.tramitarProcessoAtualizandoAnterior(
				processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso().getId(),
				StatusFluxoEnum.REENQUADRADO_AGUARDANDO_DISTRIBUICAO.getStatus(), AreaEnum.DIRRE.getId());

	}
	private BoletoDaeRequerimento preencherCamposDAE() {
		if(!novoBoletoIsencaoDae) {
			if (Util.isNullOuVazio(novoBoletoVlrTotalVistoria) && Util.isNullOuVazio(novoBoletoVlrTotalCertificado)){
				MensagemUtil.alerta(BUNDLE.getString("gerar_novo_boleto_msg_erro_valor_certificado_vistoria_obrigadorio"));
				return null;
			}
		} else {
			novoBoletoVlrTotalVistoria = new BigDecimal(0);
			novoBoletoVlrTotalCertificado = new BigDecimal(0);
		}
		
		BoletoDaeRequerimento boletoDaeRequerimento = new BoletoDaeRequerimento();
		
		boletoDaeRequerimento.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
		boletoDaeRequerimento.setIdeProcesso(novoBoletoProcesso);
		boletoDaeRequerimento.setIdeTipoBoletoPagamento(novoBoletoTipo);
		boletoDaeRequerimento.setVlrTotalVistoria(novoBoletoVlrTotalVistoria);
		boletoDaeRequerimento.setVlrTotalCertificado(novoBoletoVlrTotalCertificado);
		boletoDaeRequerimento.setIndIsento(novoBoletoIsencaoDae);
		
		if(novoBoletoIsencaoDae) {
			if(!Util.isNullOuVazio(novoBoletoMotivoIsencaoDae)) {
				boletoDaeRequerimento.setIdeMotivoIsencaoBoleto(novoBoletoMotivoIsencaoDae);
			} else { 
				MensagemUtil.alerta("Selecione um motivo para a isenção do DAE.");
				return null;
			}
		}
		
		if(!novoBoletoIsencaoDae) {
			BoletoDaeHistorico bph = new BoletoDaeHistorico();
			bph.setIdeBoletoDaeRequerimento(boletoDaeRequerimento);
			bph.setDtcTramitacao(new Date());
			bph.setIdePessoa(new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			bph.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.EMITIDO.getId()));
			
			boletoDaeRequerimento.setBoletoDaeHistorico(new ArrayList<BoletoDaeHistorico>());
			boletoDaeRequerimento.getBoletoDaeHistorico().add(bph);
		}
		
		return boletoDaeRequerimento;
	}
	
	private BoletoPagamentoRequerimento preencherCamposBoleto() {
		BoletoPagamentoRequerimento bpr = new BoletoPagamentoRequerimento();

		if (novoBoletoTipo.getIdeTipoBoletoPagamento().equals(TipoBoletoPagamentoEnum.REQUERIMENTO_BOLETO_COMPLEMENTAR.getId())) {
			bpr.setIdeRequerimento(novoBoletoRequerimento);
		} else {
			bpr.setIdeProcesso(novoBoletoProcesso);
		}

		if(!novoBoletoIsencaoBoleto) {
			if (novoBoletoValor >= 0.01) {
				if (novoBoletoValor <= 99999999.99) {
					bpr.setValBoleto(new BigDecimal(novoBoletoValor));
				} else {
					MensagemUtil.alerta(BUNDLE.getString("gerar_novo_boleto_msg_erro_valor_grande"));
					return null;
				}
			} else {
				MensagemUtil.alerta(BUNDLE.getString("gerar_novo_boleto_msg_erro_valor"));
				return null;
			}
		} else {
			novoBoletoValor = 0.0;
			bpr.setValBoleto(new BigDecimal(novoBoletoValor));
		}
		
		if (!Util.isNullOuVazio(novoBoletoDataVencimento)) {
			bpr.setDtcVencimento(novoBoletoDataVencimento);
			
		} else if(novoBoletoIsencaoBoleto) {
			bpr.setDtcVencimento(new Date());
			
		} else {
			MensagemUtil.alerta(BUNDLE.getString("gerar_novo_boleto_msg_erro_data"));
			return null;
		}

		bpr.setDtcEmissao(new Date());
		bpr.setIdePessoa(new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		bpr.setIdeTipoBoletoPagamento(novoBoletoTipo);
		if(!novoBoletoIsencaoBoleto) bpr.setIndBoletoRegistrado(true);
		if(!novoBoletoIsencaoBoleto) bpr.setIndBoletoGeradoManualmente(false);
		
		bpr.setIndIsento(novoBoletoIsencaoBoleto);
		
		if(novoBoletoIsencaoBoleto) {
			if(!Util.isNullOuVazio(novoBoletoMotivoIsencaoBoleto)) {
				bpr.setIdeMotivoIsencaoBoleto(novoBoletoMotivoIsencaoBoleto);
			} else { 
				MensagemUtil.alerta("Selecione um motivo para a isenção do boleto.");
				return null;
			}
		}
		
		if(!novoBoletoIsencaoBoleto) {
			BoletoPagamentoHistorico bph = new BoletoPagamentoHistorico();
			bph.setIdeBoletoPagamento(bpr);
			bph.setDtcTramitacao(new Date());
			bph.setIdePessoa(new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			bph.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.EM_PROCESSAMENTO.getId()));
			
			bpr.setBoletoPagamentoHistoricoCollection(new ArrayList<BoletoPagamentoHistorico>());
			bpr.getBoletoPagamentoHistoricoCollection().add(bph);
		}
		
		return bpr;
	}
	

	public boolean validar() {

		if (!isUsuarioExterno()) {
			if (!validaDatas()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Método utilizado pelo popup de selecionar requerente, principalmente para definir se a busca será feita por pessoa física ou jurídica.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	public void consultarRequerente() {

		setRenderizaGrid(Boolean.TRUE);
		getDataTablePessoaFisica().setFirst(0);
		getDataTablePessoaFisica().setPage(1);

		getDataTablePessoaJuridica().setFirst(0);
		getDataTablePessoaJuridica().setPage(1);

		if (pessoaFisica) consultarPessoaFisica();
		else if (!pessoaFisica) consultarPessoaJuridica();
	}

	/**
	 * Método utilizado pelo popup de selecionar requerente para consultar uma pessoa física por nome ou cpf.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	private void consultarPessoaFisica() {

		final PessoaFisica lPessoa = new PessoaFisica();
		lPessoa.setNomPessoa(getNomeRequerenteFilter().trim());
		lPessoa.setNumCpf(getDocumentoRequerenteFilter().trim());

		try {

			lPessoa.setPessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());

			setPessoaFisicaModel(new LazyDataModel<PessoaFisica>() {

				private static final long serialVersionUID = -2208228405486565761L;

				@Override
				public List<PessoaFisica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {

					List<PessoaFisica> lista = null;

					try {
						if (isUsuarioExterno()) {
							lista = (List<PessoaFisica>) vwRequerentePfExternoService.listarVwRequerentePfExterno(lPessoa);
						} else {
							lista = (List<PessoaFisica>) pessoaFisicaService.filtrarPessoaRequerente(first, pageSize, lPessoa);
						}
					} catch (Exception e) {
						getPessoaFisicaModel().setRowCount(0);
						Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
						throw Util.capturarException(e, Util.SEIA_EXCEPTION);
					}

					return lista;
				}
			});

			if (isUsuarioExterno()) {
				getPessoaFisicaModel().setRowCount(vwRequerentePfExternoService.listarVwRequerentePfExterno(lPessoa).size());
			} else {
				getPessoaFisicaModel().setRowCount(pessoaFisicaService.countFiltroPessoaFisicaSolicitante(lPessoa));
			}

		} catch (Exception e) {
			getPessoaFisicaModel().setRowCount(0);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método utilizado pelo popup de selecionar requerente para consultar uma pessoa jurídica por razão social ou cnpj.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	private void consultarPessoaJuridica() {

		final PessoaJuridica lPessoa = new PessoaJuridica();
		lPessoa.setNomRazaoSocial(getNomeRequerenteFilter().trim());
		lPessoa.setNumCnpj(getDocumentoRequerenteFilter().trim());

		try {

			setPessoaJuridicaModel(new LazyDataModel<PessoaJuridica>() {

				private static final long serialVersionUID = 2212198666323116770L;

				@Override
				public List<PessoaJuridica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {

					List<PessoaJuridica> lista = null;
					
					try {

						if (isUsuarioExterno()) {
							lista = pessoaJuridicaService.filtrarPJRequerenteExterno(lPessoa);
						} else {
							lista = pessoaJuridicaService.filtrarPessoaJuridica(first, pageSize, lPessoa);
						}

					} catch (Exception e) {
						getPessoaJuridicaModel().setRowCount(0);
						Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
						throw Util.capturarException(e, Util.SEIA_EXCEPTION);
					}

					return lista;
				}
			});

			if (isUsuarioExterno()) {
				lPessoa.setPessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
				getPessoaJuridicaModel().setRowCount(pessoaJuridicaService.countFiltroPessoaJuridicaSolicitanteExterno(lPessoa));
			} else {
				getPessoaJuridicaModel().setRowCount(pessoaJuridicaService.countFiltroPessoaJuridicaSolicitante(lPessoa));
			}

		} catch (Exception e) {
			getPessoaJuridicaModel().setRowCount(0);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método utilizado pelo popup de selecionar requerente quando uma pessoa física é selecionada. 
	 * Define o requerente utilizado no filtro da tela de consulta do boleto complementar.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 * @param requerente
	 */
	public void selecionarRequerentePessoaFisica(PessoaFisica requerente) {

		getFilter().setRequerente(new Pessoa());
		getFilter().getRequerente().setIdePessoa(requerente.getIdePessoaFisica());
		getFilter().getRequerente().setPessoaFisica(requerente);
	}

	/**
	 * Método utilizado pelo popup de selecionar requerente quando uma pessoa jurídica é selecionada. 
	 * Define o requerente utilizado no filtro da tela de consulta do boleto complementar.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 * @param requerente
	 */
	public void selecionarRequerentePessoaJuridica(PessoaJuridica requerente) {

		getFilter().setRequerente(new Pessoa());
		getFilter().getRequerente().setIdePessoa(requerente.getIdePessoaJuridica());
		getFilter().getRequerente().setPessoaJuridica(requerente);
	}

	/**
	 * Método que verifica se os períodos dos status estão corretos. 
	 * Caso QUALQUER período esteja incorreto, retorna FALSE.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 * @return {@link Boolean} TRUE para todas as datas válidas, FALSE para qualquer inválida.
	 */
	public boolean validaDatas() {

		if (getFilter().isEmitido()) {

			if (!Util.validaPeriodo(getFilter().getDtEmitidoInicial(), getFilter().getDtEmitidoFinal())) return false;
		}

		if (getFilter().isCancelamento()) {

			if (!Util.validaPeriodo(getFilter().getDtCancelamentoInicial(), getFilter().getDtCancelamentoInicial())) return false;
		}

		if (getFilter().isComprovante()) {

			if (!Util.validaPeriodo(getFilter().getDtComprovanteInicial(), getFilter().getDtComprovanteFinal())) return false;
		}

		if (getFilter().isPago()) {

			if (!Util.validaPeriodo(getFilter().getDtPagoInicial(), getFilter().getDtPagoFinal())) return false;
		}

		if (getFilter().isCancelado()) {

			if (!Util.validaPeriodo(getFilter().getDtCanceladoInicial(), getFilter().getDtCanceladoFinal())) return false;
		}

		if (getFilter().isVencido()) {

			if (!Util.validaPeriodo(getFilter().getDtVencidoInicial(), getFilter().getDtVencidoFinal())) return false;
		}

		return true;
	}
	
	/**
	 * Método que prepara o popup de novo boleto.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	public void novoBoletoPopup() {

		limparPopupNovoBoleto();

		listTipoBoleto = tipoBoletoPagamentoService.consultarTipoBoleto();
		novoBoletoTipo = new TipoBoletoPagamento();
		incluindo = Boolean.TRUE;
	}
	
	/**
	 * Método que limpa o popup de novo boleto quando o botão 'voltar' é acionado.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	public void novoBoletoVoltar() {

		incluindo = Boolean.FALSE;
	}
	
	/**
	 * Método que verifica se algum valor foi digitado no número do requerimento/processo no popup de novo boleto. 
	 * Caso exista, exibe na tela os dados do requerente, caso não exista, exibe uma mensagem ao usuário.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 * @author micael.coutinho
	 * @throws Exception
	 */
	public void novoBoletoCarregaValores() {
		
		try {
			novoBoletoDae = false;
			novoBoletoBoleto = false;
			if (novoBoletoTipo.getIdeTipoBoletoPagamento() == -1 || Util.isNullOuVazio(novoBoletoNumero)) {
				novoBoletoRenderizaPanelDados = Boolean.FALSE;
			} else {
				novoBoletoNumero = novoBoletoNumero.replaceAll("\\s+","");
				carregarRequerimentoOuProcesso();
			}
		} catch (Exception e) {
			MensagemUtil.erro(BUNDLE.getString("gerar_novo_boleto_msg_erro_salvar"));
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Carrega o requerimento ou processo.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @author micael.coutinho
	 * @throws Exception
	 */
	public void carregarRequerimentoOuProcesso() {

		Processo proc = null;
		Requerimento req = null;

		// REQUERIMENTO
		if (novoBoletoTipo.getIdeTipoBoletoPagamento() == 2) {
			
			req = requerimentoService.buscarRequerimentoPeloNumero(new Requerimento(novoBoletoNumero));
			
			if (!Util.isNullOuVazio(req) && !Util.isNullOuVazio(req.getTramitacaoRequerimentoCollection())) {
				
				if(verificaStatusRequerimento(req)) {
					
					List<BoletoPagamentoRequerimento> list = boletoPagamentoRequerimentoService.carregarListaBoletoByRequerimento(req.getIdeRequerimento());
					
					if(!Util.isNullOuVazio(list)) {
						defineValoresReqProcesso(proc, req);
						
					} else {
						novoBoletoRenderizaPanelDados = Boolean.FALSE;
						MensagemUtil.erro(BUNDLE.getString("gerar_novo_boleto_msg_erro_boleto_requerimento"));
					}				
				} else {
					novoBoletoRenderizaPanelDados = Boolean.FALSE;
					MensagemUtil.erro(BUNDLE.getString("gerar_novo_boleto_msg_erro_status_requerimento") + "\"" + novoBoletoMensagemErro + "\".");
				}
			} else {
				novoBoletoRenderizaPanelDados = Boolean.FALSE;
				MensagemUtil.erro(BUNDLE.getString("geral_msg_requerimento_nao_encontrado"));
			}
				
		// PROCESSO
		} else {
			proc = processoService.buscarProcessoPeloNumero(new Processo(novoBoletoNumero));
			
			if (!Util.isNullOuVazio(proc)) {
				if (!Util.isNullOuVazio(proc.getIdeRequerimento())) {
					req = proc.getIdeRequerimento();
					defineValoresReqProcesso(proc, req);
				} else {
					novoBoletoRenderizaPanelDados = Boolean.FALSE;
					MensagemUtil.erro(BUNDLE.getString("geral_msg_requerimento_nao_encontrado"));
				}
			} else {
				novoBoletoRenderizaPanelDados = Boolean.FALSE;
				MensagemUtil.erro(BUNDLE.getString("geral_msg_processo_nao_encontrado"));
			}
		}
	}
	
	/**
	 * Método que verifica se o status do requerimento é igual a: REQUERIMENTO CONCLUIDO, PROCESSO FORMADO, CANCELADO ou AGUARDANDO ENQUADRAMENTO.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param req - {@link Requerimento} com a collection de {@link TramitacaoRequerimento} já carregada
	 * @return {@link Boolean} FALSE caso o status seja igual a um dos citados acima.
	 */
	public boolean verificaStatusRequerimento(Requerimento req) {
		
		int x=0;
		StatusRequerimento status = new StatusRequerimento();
		
		for (TramitacaoRequerimento tram : req.getTramitacaoRequerimentoCollection()) {
			if(x < tram.getIdeTramitacaoRequerimento()) {
				x = tram.getIdeTramitacaoRequerimento();
				status = tram.getIdeStatusRequerimento();
			}
		}
		
		if(status.getIdeStatusRequerimento() == StatusRequerimentoEnum.REQUERIMENTO_CONCLUIDO.getStatus()) {
			novoBoletoMensagemErro = status.getNomStatusRequerimento();
			return false;
		} else if(status.getIdeStatusRequerimento() == StatusRequerimentoEnum.FORMADO.getStatus()) {
			novoBoletoMensagemErro = status.getNomStatusRequerimento();
			return false;
		} else if(status.getIdeStatusRequerimento() == StatusRequerimentoEnum.CANCELADO.getStatus()) {
			novoBoletoMensagemErro = status.getNomStatusRequerimento();
			return false;
		} else if(status.getIdeStatusRequerimento() == StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus()) {
			novoBoletoMensagemErro = status.getNomStatusRequerimento();
			return false;
		}

		return true;
	}

	/**
	 * Define o requerimento e o processo do novo boleto.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @author micael.coutinho
	 * @param proc
	 * @param req
	 */
	public void defineValoresReqProcesso(Processo proc, Requerimento req) {

		if (!Util.isNullOuVazio(req) && !Util.isNullOuVazio(req.getIdeRequerimento()) && !Util.isNullOuVazio(req.getRequerimentoPessoaCollection())) {

			setarRequerente(req);
			
			novoBoletoRequerimento = req;
			novoBoletoProcesso = proc;
			novoBoletoRenderizaPanelDados = Boolean.TRUE;
		} else {

			novoBoletoRenderizaPanelDados = Boolean.FALSE;
			MensagemUtil.erro(BUNDLE.getString("geral_msg_requerimento_nao_encontrado"));
		}
	}

	/**
	 * Define o requerente do novo boleto.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @author micael.coutinho
	 * @param req
	 */
	public void setarRequerente(Requerimento req) {

		for (RequerimentoPessoa rp : req.getRequerimentoPessoaCollection()) {

			if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {

				if (!Util.isNullOuVazio(rp.getPessoa().getNomeRazao())) novoBoletonomeRequerente = rp.getPessoa().getNomeRazao();
				if (!Util.isNullOuVazio(rp.getPessoa().getCpfCnpjFormatado())) novoBoletoCpfCnpjRequerente = rp.getPessoa().getCpfCnpjFormatado();
			}
		}
	}
	
	/**
	 * Método que envia e-mails para os envolvidos com o requerimento/processo na criação de um novo boleto complementar
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param req - requerimento da onde serão extraídos dados para compor o e-mail
	 * @param proc - processo da onde serão extraídos dados para compor o e-mail
	 * @throws Exception
	 */
	public void enviarEmails(Requerimento req, Processo proc, boolean forValidacao){
		
		if(req != null) {
			
			List<String> listaEmailsRequerimento = new ArrayList<String>();
			
			Collections.sort((List<RequerimentoPessoa>) req.getRequerimentoPessoaCollection());
			
			for (RequerimentoPessoa rp : req.getRequerimentoPessoaCollection()) {
				
				if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {
					if (!Util.isNullOuVazio(rp.getPessoa().getDesEmail()) && verificaDuplicacaoEmail(listaEmailsRequerimento, rp.getPessoa().getDesEmail())) {
						listaEmailsRequerimento.add(rp.getPessoa().getDesEmail());
						req.setRequerente(rp.getPessoa());
					}
				}
				
				if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.PROCURADOR.getId())) {
					if (!Util.isNullOuVazio(rp.getPessoa().getDesEmail()) && verificaDuplicacaoEmail(listaEmailsRequerimento, rp.getPessoa().getDesEmail())) {
						listaEmailsRequerimento.add(rp.getPessoa().getDesEmail());
					}
				}
				
				if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId())) {
					if (!Util.isNullOuVazio(rp.getPessoa().getDesEmail()) && verificaDuplicacaoEmail(listaEmailsRequerimento, rp.getPessoa().getDesEmail())) {
						listaEmailsRequerimento.add(rp.getPessoa().getDesEmail());
					}
				}
			}
			
			if (!Util.isNullOuVazio(req.getDesEmail()) && verificaDuplicacaoEmail(listaEmailsRequerimento, req.getDesEmail())) {
				listaEmailsRequerimento.add(req.getDesEmail());
			}
			
			StringBuilder assunto = new StringBuilder();
			StringBuilder msg = new StringBuilder();
			
			if(forValidacao) {

				assunto.append("SEIA - Pendência de comprovante do requerimento de nº "); 
				if(!Util.isNullOuVazio(req.getNumRequerimento())) {
					assunto.append(req.getNumRequerimento());
				}
				msg.append(validarComprovanteDscInvalidacaoComprovante);
				boletoPagamentoRequerimentoService.enviarEmailBoletoRequerimento(assunto.toString(), msg.toString(), req, listaEmailsRequerimento);
			} 
			
			
		} else if(proc != null) {
			
			List<String> listaEmailsProcesso = new ArrayList<String>();
			
			Collections.sort((List<RequerimentoPessoa>) proc.getIdeRequerimento().getRequerimentoPessoaCollection());
			
			for (RequerimentoPessoa rp : proc.getIdeRequerimento().getRequerimentoPessoaCollection()) {
				
				if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {
					if (!Util.isNullOuVazio(rp.getPessoa().getDesEmail()) && verificaDuplicacaoEmail(listaEmailsProcesso, rp.getPessoa().getDesEmail())) {
						listaEmailsProcesso.add(rp.getPessoa().getDesEmail());
						proc.getIdeRequerimento().setRequerente(rp.getPessoa());
					}
				}
				
				if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.PROCURADOR.getId())) {
					if (!Util.isNullOuVazio(rp.getPessoa().getDesEmail()) && verificaDuplicacaoEmail(listaEmailsProcesso, rp.getPessoa().getDesEmail())) {
						listaEmailsProcesso.add(rp.getPessoa().getDesEmail());
					}
				}
				
				if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId())) {
					if (!Util.isNullOuVazio(rp.getPessoa().getDesEmail()) && verificaDuplicacaoEmail(listaEmailsProcesso, rp.getPessoa().getDesEmail())) {
						listaEmailsProcesso.add(rp.getPessoa().getDesEmail());
					}
				}
			}
			
			if (!Util.isNullOuVazio(proc.getIdeRequerimento().getDesEmail()) && verificaDuplicacaoEmail(listaEmailsProcesso, proc.getIdeRequerimento().getDesEmail())) {
				listaEmailsProcesso.add(proc.getIdeRequerimento().getDesEmail());
			}
			
			StringBuilder assunto = new StringBuilder();
			StringBuilder msg = new StringBuilder();
			
			if(forValidacao) {

				assunto.append("SEIA - Pendência de comprovante do processo de nº ");
				if(!Util.isNullOuVazio(proc.getNumProcesso())) {
					assunto.append(proc.getNumProcesso());
				}
				
				msg.append(validarComprovanteDscInvalidacaoComprovante);
				boletoPagamentoRequerimentoService.enviarEmailBoletoProcesso(assunto.toString(), msg.toString(), proc, listaEmailsProcesso);
			}
			
		}
	}
	
	/**
	 * Verifica se o e-mail que está tentando ser incluído já existe numa determinada lista.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param listaEmails
	 * @param email
	 * @return
	 */
	public boolean verificaDuplicacaoEmail(List<String> listaEmails, String email) {
		
		if (!Util.isNullOuVazio(listaEmails)) {
			for (String emailDaLista : listaEmails) {
				if (emailDaLista.equals(email)) {
					return false;
				}
			}
		}
		
		return true;
	}

	/**
	 * Ação para solicitar cancelamento de boletos.
	 * 
	 * @author micael.coutinho
	 */
	public void solicitarCancelamentoBoleto() {

		boletoPagamentHistorico.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getId()));

		if (cancelamentoBoletoValidado(boletoPagamentHistorico)) {
			atualizarBoletoPagHistorico(boletoPagamentHistorico);
		}
	}
	
	/**
	 * Ação para solicitar cancelamento de dae.
	 */
	public void solicitarCancelamentoDae() {

		boletoDaeHistorico.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getId()));

		if (cancelamentoDaeValidado(boletoDaeHistorico)) {
			atualizarBoletoDaeHistorico(boletoDaeHistorico);
		}
	}

	/**
	 * Valida o cancelamento do boleto.
	 * 
	 * @author micael.coutinho
	 * @param bolPagHist
	 * @return TRUE para tudo correto, ou FALSE para qualquer erro.
	 */
	private Boolean cancelamentoBoletoValidado(BoletoPagamentoHistorico bolPagHist) {

		if (Util.isNullOuVazio(bolPagHist)) {
			MensagemUtil.erro("Por favor, selecione um boleto.");
			return false;
		} else if (Util.isNullOuVazio(bolPagHist.getIdeMotivoCancelamentoBoleto().getIdeMotivoCancelamentoBoleto()) || bolPagHist.getIdeMotivoCancelamentoBoleto().getIdeMotivoCancelamentoBoleto() == -1) {
			MensagemUtil.erro("Por favor, selecione o motivo do Cancelamento.");
			return false;
		} else if (Util.isNullOuVazio(bolPagHist.getDscObservacao())) {
			MensagemUtil.erro("Por favor, informe o motivo para Cancelamento.");
			return false;
		}

		return true;
	}
	
	/**
	 * Valida o cancelamento do dae.
	 * 
	 * @param bolPagHist
	 * @return TRUE para tudo correto, ou FALSE para qualquer erro.
	 */
	private Boolean cancelamentoDaeValidado(BoletoDaeHistorico bolPagHist) {

		if (Util.isNullOuVazio(bolPagHist)) {
			MensagemUtil.erro("Por favor, selecione um boleto.");
			return false;
		} else if (Util.isNullOuVazio(bolPagHist.getIdeMotivoCancelamentoBoleto().getIdeMotivoCancelamentoBoleto()) || bolPagHist.getIdeMotivoCancelamentoBoleto().getIdeMotivoCancelamentoBoleto() == -1) {
			MensagemUtil.erro("Por favor, selecione o motivo do Cancelamento.");
			return false;
		} else if (Util.isNullOuVazio(bolPagHist.getDscObservacao())) {
			MensagemUtil.erro("Por favor, informe o motivo para Cancelamento.");
			return false;
		}

		return true;
	}

	/**
	 * Ação para solicitar cancelamento de boletos.
	 * 
	 * @author micael.coutinho
	 */
	public void cancelarBoleto(){
		boletoPagamentHistorico.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.CANCELADO.getId()));
		if(cancelamentoBoletoValidado(boletoPagamentHistorico)){
			atualizarBoletoPagHistorico(boletoPagamentHistorico);
		}
	}
	
	/**
	 * Ação para solicitar cancelamento de boletos de dae.
	 */
	public void cancelarBoletoDae(){
		boletoDaeHistorico.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.CANCELADO.getId()));
		if(cancelamentoDaeValidado(boletoDaeHistorico)){
			atualizarBoletoDaeHistorico(boletoDaeHistorico);
		}
	}
	
	/**
	 * Ação para rejeitar solicitação de cancelamento.
	 * 
	 * @author micael.coutinho
	 */
	public void rejeitarSolicitacaoCancelamento(){
		BoletoPagamentoHistorico historicoRejeitado = new BoletoPagamentoHistorico(boletoPagReqCancelar, boletoPagamentHistorico.getIdeMotivoCancelamentoBoleto());
		historicoRejeitado.setDscObservacao(boletoPagamentHistorico.getDscObservacao());
		historicoRejeitado.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.CANCELAMENTO_REJEITADO.getId()));
		if(cancelamentoBoletoValidado(historicoRejeitado)){
			atualizarBoletoPagHistorico(historicoRejeitado);
		}
		boletoPagamentHistorico.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.EMITIDO.getId()));
		if(cancelamentoBoletoValidado(boletoPagamentHistorico)){
			atualizarBoletoPagHistorico(boletoPagamentHistorico);
		}
	}
	
	/**
	 * Ação para rejeitar solicitação de cancelamento de dae.
	 */
	public void rejeitarSolicitacaoCancelamentoDae(){
		BoletoDaeHistorico historicoRejeitado = new BoletoDaeHistorico(boletoDaeReqCancelar, boletoDaeHistorico.getIdeMotivoCancelamentoBoleto());
		historicoRejeitado.setDscObservacao(boletoDaeHistorico.getDscObservacao());
		historicoRejeitado.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.CANCELAMENTO_REJEITADO.getId()));
		if(cancelamentoDaeValidado(historicoRejeitado)){
			atualizarBoletoDaeHistorico(historicoRejeitado);
		}
		boletoDaeHistorico.setIdeStatusBoletoPagamento(new StatusBoletoPagamento(StatusBoletoEnum.EMITIDO.getId()));
		if(cancelamentoDaeValidado(boletoDaeHistorico)){
			atualizarBoletoDaeHistorico(boletoDaeHistorico);
		}
	}

	/**
	 * 
	 * 
	 * @author micael.coutinho
	 */
	public void atualizarBoletoPagHistorico(BoletoPagamentoHistorico boletoPagHisto) {
		boletoPagHisto.setIdeBoletoPagamento(boletoPagReqCancelar);
		boletoPagHisto.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
		boletoPagHisto.setDtcTramitacao(new Date());
		
		try {
			boletoService.salvarBoletoPagHistorico(boletoPagHisto);
			mensagensPorStatusBoleto(boletoPagHisto);
			boletoPagHisto = iniciarBoletoPagHistorico(boletoPagHisto);
		} catch (Exception e) {
			MensagemUtil.erro("Erro ao Alterar Status do Boleto.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void atualizarBoletoDaeHistorico(BoletoDaeHistorico boletoPagHisto) {
		boletoPagHisto.setIdeBoletoDaeRequerimento(boletoDaeReqCancelar);
		boletoPagHisto.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
		boletoPagHisto.setDtcTramitacao(new Date());
		
		try {
			boletoDaeHistoricoService.salvarOuAtualizar(boletoPagHisto);
			mensagensPorStatusBoleto(boletoPagHisto);
			boletoPagHisto = iniciarBoletoDaeHistorico(boletoPagHisto);
		} catch (Exception e) {
			MensagemUtil.erro("Erro ao Alterar Status do Boleto.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * 
	 * 
	 * @author micael.coutinho
	 */
	public void mensagensPorStatusBoleto(BoletoPagamentoHistorico boletoPagHisto) {
		if (boletoPagHisto.getIdeStatusBoletoPagamento().getIdeStatusBoletoPagamento().intValue() == StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getId()) 
			MensagemUtil.sucesso("Cancelamento Solicitado com sucesso!");
		
		else if(boletoPagHisto.getIdeStatusBoletoPagamento().getIdeStatusBoletoPagamento().intValue() == StatusBoletoEnum.CANCELADO.getId())
			MensagemUtil.sucesso("Boleto cancelado com sucesso!");
		
		else if(boletoPagHisto.getIdeStatusBoletoPagamento().getIdeStatusBoletoPagamento().intValue() == StatusBoletoEnum.CANCELAMENTO_REJEITADO.getId())
			MensagemUtil.sucesso("Cancelamento do boleto rejeitado com sucesso! O boleto voltou ao Status de Emitido.");
		
		Html.esconder("dialogCancelarBoleto");
	}
	
	public void mensagensPorStatusBoleto(BoletoDaeHistorico boletoPagHisto) {
		if (boletoPagHisto.getIdeStatusBoletoPagamento().getIdeStatusBoletoPagamento().intValue() == StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getId()) 
			MensagemUtil.sucesso("Cancelamento Solicitado com sucesso!");
		
		else if(boletoPagHisto.getIdeStatusBoletoPagamento().getIdeStatusBoletoPagamento().intValue() == StatusBoletoEnum.CANCELADO.getId())
			MensagemUtil.sucesso("Boleto cancelado com sucesso!");
		else if(boletoPagHisto.getIdeStatusBoletoPagamento().getIdeStatusBoletoPagamento().intValue() == StatusBoletoEnum.CANCELAMENTO_REJEITADO.getId())
			MensagemUtil.sucesso("Cancelamento do boleto rejeitado com sucesso! O boleto voltou ao Status de Emitido.");
		
		Html.esconder("dialogCancelarDae");
	}

	/**
	 * 
	 * 
	 * @author micael.coutinho
	 */
	public BoletoPagamentoHistorico iniciarBoletoPagHistorico(BoletoPagamentoHistorico boletoPagHisto) {
		boletoPagHisto = null;
		boletoPagHisto = new BoletoPagamentoHistorico();
		boletoPagHisto.setIdeMotivoCancelamentoBoleto(new MotivoCancelamentoBoleto());
		return boletoPagHisto;
	}
	
	public BoletoDaeHistorico iniciarBoletoDaeHistorico(BoletoDaeHistorico boletoPagHisto) {
		boletoPagHisto = new BoletoDaeHistorico();
		boletoPagHisto.setIdeMotivoCancelamentoBoleto(new MotivoCancelamentoBoleto());
		return boletoPagHisto;
	}

	/**
	 * Carrega valores para exibição da tela de cancelamento.
	 * 
	 * @author micael.coutinho
	 * @throws Exception
	 */
	public void carregaValoresParaCancelarBoleto(){
		
		try {
			Requerimento req = null;
			boletoPagamentHistUltimo = null;
			
			boletoPagReqCancelar = new BoletoPagamentoRequerimento(boletoCancelar.getId(), boletoCancelar.getDtVencimento(), boletoCancelar.getValor(), boletoCancelar.getDtGeracao());
			boletoPagReqCancelar.setNumBoleto(boletoCancelar.getNumBoleto());
			boletoPagReqCancelar = boletoPagamentoRequerimentoService.carregarById(boletoCancelar.getId());
	
			boletoPagReqCancelar.setStatusBoletoPagamento(new StatusBoletoPagamento(boletoCancelar.getStatus()));
			labelNumRequerimentoOuProcesso = defineLabelRequerimentoProcesso(boletoPagReqCancelar);
	
			if(ultimoStatusBoletoPagReq(boletoPagReqCancelar).getIdeStatusBoletoPagamento().getIdeStatusBoletoPagamento().intValue() == StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getId()){
				boletoPagamentHistUltimo = ultimoStatusBoletoPagReq(boletoPagReqCancelar);
				boletoPagamentHistorico = new BoletoPagamentoHistorico();
				boletoPagamentHistorico.setDscObservacao(StringUtils.EMPTY);
				boletoPagamentHistorico.setIdeBoletoPagamento(boletoPagReqCancelar);
				boletoPagamentHistorico.setIdeMotivoCancelamentoBoleto(boletoPagamentHistUltimo.getIdeMotivoCancelamentoBoleto());
			}else{
				boletoPagamentHistorico = ultimoStatusBoletoPagReq(boletoPagReqCancelar);
			}
			
			if( (Util.isNullOuVazio(boletoPagamentHistorico.getIdeStatusBoletoPagamento()) && Util.isNullOuVazio(boletoPagamentHistorico.getIdeMotivoCancelamentoBoleto())) 
					|| (Util.isNullOuVazio(boletoPagamentHistorico.getIdeBoletoPagamentoHistorico()) && Util.isNullOuVazio(boletoPagamentHistUltimo))
					|| (!Util.isNullOuVazio(boletoPagamentHistorico.getIdeStatusBoletoPagamento()) && boletoPagamentHistorico.getIdeStatusBoletoPagamento().getIdeStatusBoletoPagamento().intValue() == StatusBoletoEnum.EMITIDO.getId()) )
				boletoPagamentHistorico = iniciarBoletoPagHistorico(boletoPagamentHistorico);
			
			req = carregarRequerimentoDoBoleto(boletoPagReqCancelar);
			
			for (RequerimentoPessoa rp : req.getRequerimentoPessoaCollection()) {
				if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {
					req.setRequerente(rp.getPessoa());
				}
			}
			boletoPagReqCancelar.setIdeRequerimento(req);
			
			listaMotivoCancelamentoBoleto = boletoComplementarService.listarMotivosCancelamentoBoleto();
		} catch (Exception e) {
			MensagemUtil.erro("Não foi possível carregar o boleto para Cancelamento.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregaValoresParaCancelarDae(){
		limparCancelamento();
		
		try {
			Requerimento req = null;
			boletoPagamentHistUltimo = null;
			statusCancelar = StatusBoletoEnum.CANCELADO.getValue().equalsIgnoreCase(boletoCancelar.getStatus());
			cancelamentoSolicitado = StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getValue().equalsIgnoreCase(boletoCancelar.getStatus());
			emitido = StatusBoletoEnum.EMITIDO.getValue().equalsIgnoreCase(boletoCancelar.getStatus());	
					
			boletoDaeReqCancelar = boletoDaeRequerimentoService.carregarById(boletoCancelar.getId());
			labelNumRequerimentoOuProcesso = defineLabelDaeProcesso(boletoDaeReqCancelar);
	
			if(ultimoStatusDaePagReq(boletoDaeReqCancelar).getIdeStatusBoletoPagamento().getIdeStatusBoletoPagamento().intValue() == StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getId()){
				boletoDaeHistUltimo = ultimoStatusDaePagReq(boletoDaeReqCancelar);
				boletoDaeHistorico = new BoletoDaeHistorico();
				boletoDaeHistorico.setDscObservacao(StringUtils.EMPTY);
				boletoDaeHistorico.setIdeBoletoDaeRequerimento(boletoDaeReqCancelar);
				boletoDaeHistorico.setIdeMotivoCancelamentoBoleto(boletoDaeHistUltimo.getIdeMotivoCancelamentoBoleto());
			}else{
				boletoDaeHistorico = ultimoStatusDaePagReq(boletoDaeReqCancelar);
				
			}
			
			if( (Util.isNullOuVazio(boletoDaeHistorico.getIdeStatusBoletoPagamento()) && Util.isNullOuVazio(boletoDaeHistorico.getIdeMotivoCancelamentoBoleto())) 
					|| (Util.isNullOuVazio(boletoDaeHistorico.getIdeBoletoDaeHistorico()) && Util.isNullOuVazio(boletoDaeHistUltimo))
					|| (!Util.isNullOuVazio(boletoDaeHistorico.getIdeStatusBoletoPagamento()) && StatusBoletoEnum.EMITIDO.getValue().equalsIgnoreCase(boletoDaeHistorico.getIdeStatusBoletoPagamento().getNomStatusBoletoPagamento()))){
				boletoDaeHistorico = iniciarBoletoDaeHistorico(boletoDaeHistorico);
			}
			
			req = carregarRequerimentoDoDae(boletoDaeReqCancelar);
			
			for (RequerimentoPessoa rp : req.getRequerimentoPessoaCollection()) {
				if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {
					req.setRequerente(rp.getPessoa());
				}
			}
			boletoDaeReqCancelar.setIdeRequerimento(req);
			
			listaMotivoCancelamentoBoleto = boletoComplementarService.listarMotivosCancelamentoBoleto();
		} catch (Exception e) {
			MensagemUtil.erro("Não foi possível carregar o boleto para Cancelamento.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @return
	 * @throws Exception
	 * @author micael.coutinho
	 */
	public Requerimento carregarRequerimentoDoBoleto(BoletoPagamentoRequerimento boleto) {
		Requerimento req;
		if(!Util.isNullOuVazio(boleto) && !Util.isNullOuVazio(boleto.getIdeRequerimento()) && !Util.isNullOuVazio(boleto.getIdeRequerimento().getNumRequerimento()))
			req = requerimentoService.buscarRequerimentoPeloNumero(boleto.getIdeRequerimento());
		else
			req = processoService.buscarProcessoPorCriteria(boleto.getIdeProcesso().getNumProcesso()).getIdeRequerimento();
		return req;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public Requerimento carregarRequerimentoDoDae(BoletoDaeRequerimento boleto) {
		Requerimento req;
		if(!Util.isNullOuVazio(boleto) && !Util.isNullOuVazio(boleto.getIdeRequerimento()) && !Util.isNullOuVazio(boleto.getIdeRequerimento().getNumRequerimento()))
			req = requerimentoService.buscarRequerimentoPeloNumero(boleto.getIdeRequerimento());
		else
			req = processoService.buscarProcessoPorCriteria(boleto.getIdeProcesso().getNumProcesso()).getIdeRequerimento();
		return req;
	}

	/**
	 * Método que retorna BoletoPagamentoHistorico que detém o status atual do Boleto.
	 * @param boletoPagReq
	 * @return BoletoPagamentoHistorico
	 * @author micael.coutinho
	 */
	public BoletoPagamentoHistorico ultimoStatusBoletoPagReq(BoletoPagamentoRequerimento boletoPagReq) {
		BoletoPagamentoHistorico boletoPagHistMaiorID = new BoletoPagamentoHistorico(0);
		for (BoletoPagamentoHistorico boletoPagHist : boletoPagReq.getBoletoPagamentoHistoricoCollection()) {
			if(boletoPagHistMaiorID.getIdeBoletoPagamentoHistorico().intValue() < boletoPagHist.getIdeBoletoPagamentoHistorico().intValue())
				boletoPagHistMaiorID = boletoPagHist;
		}
		return boletoPagHistMaiorID;
	}
	
	/**
	 * Método que retorna BoletoDaeRequerimento que detém o status atual do Boleto.
	 * @param boletoPagReq
	 */
	public BoletoDaeHistorico ultimoStatusDaePagReq(BoletoDaeRequerimento boletoPagReq) {
		BoletoDaeHistorico boletoPagHistMaiorID = new BoletoDaeHistorico(0);
		for (BoletoDaeHistorico boletoPagHist : boletoPagReq.getBoletoDaeHistorico()) {
			if(boletoPagHistMaiorID.getIdeBoletoDaeHistorico().intValue() < boletoPagHist.getIdeBoletoDaeHistorico().intValue())
				boletoPagHistMaiorID = boletoPagHist;
		}
		return boletoPagHistMaiorID;
	}

	/**
	 * Método que realiza o download do boleto para pagamento.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 * @return {@link StreamedContent} utilizado na view pelo componente fileDownload do primefaces.
	 */
	public StreamedContent getDownloadBoleto() {

		try {
			Integer id = getEnviarComprovante_boleto().getId();
			
			if (isReenquadramento()){
				id = boletoReequerimento.getId();
			}
			
			BoletoPagamentoRequerimento bpr = boletoPagamentoRequerimentoService.consultarPorId(id);

			Integer ideRequerimento;
			Processo proc = null;
			if (bpr.getIdeRequerimento() != null && bpr.getIdeRequerimento().getIdeRequerimento() != null) {
				ideRequerimento = bpr.getIdeRequerimento().getIdeRequerimento();
			} else {
				proc = processoService.carregarProcesso(bpr.getIdeProcesso().getIdeProcesso());
				ideRequerimento = proc.getIdeRequerimento().getIdeRequerimento();
			}

			Pessoa requerente = this.pessoaService.carregarDadosRequerente(ideRequerimento, null);
			DadoBancario dadoBancario = this.dadoBancarioService.carregarBancoBoleto(BancoEnum.BANCO_DO_BRASIL);
			
			dadoBancario.setDscInstrucao3("Valor referente ao boleto complementar vinculado ao ");
			dadoBancario.setDscInstrucao4(( (proc == null) ? ("requerimento "+bpr.getIdeRequerimento().getNumRequerimento()) : ("processo "+proc.getNumProcesso() ) )+"." );
			
//			BoletoViewer boletoViewer = BoletoUtil.gerarBoleto(bpr, requerente, dadoBancario);
//			DefaultStreamedContent dsc = new DefaultStreamedContent(new ByteArrayInputStream(boletoViewer.getPdfAsByteArray()), "", "boleto" + new Date().getTime() + ".pdf");
			
			GeradorDeBoleto geradorBoleto = BoletoUtil.geradorDeBoleto(bpr, requerente, dadoBancario);
			return new DefaultStreamedContent(new ByteArrayInputStream(geradorBoleto.geraPDF()), "", "boleto" + new Date().getTime()+".pdf");
			

		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que salva o comprovante de pagamento do boleto complementar.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 * @param event - Objeto utilizado pelo componente p:fileUpload
	 */
	public void uploadComprovante(FileUploadEvent event) {

		try {
			
			Integer id = enviarComprovanteBoleto.getId();
			
			if (isReenquadramento()){
				id = boletoReequerimento.getId();
			}
			
			String caminho = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.BOLETO_REQUERIMENTO.toString() + id + File.separator);

			BoletoPagamentoRequerimento boleto = new BoletoPagamentoRequerimento(id);
			boleto.setPathComprovante(caminho);
			enviarComprovantePagamentoDTO.setBoleto(boleto);
			
			MensagemUtil.sucesso(BUNDLE.getString("geral_msg_arquivo_enviado_sucesso"));
		} catch (Exception e) {
			MensagemUtil.erro(BUNDLE.getString("geral_msg_erro_envio_arquivo"));
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void salvarComprovanteBoleto() {
		
		try {
			boletoComplementarService.salvarComprovanteBoleto(this);
			
			Html.esconder("dialogEnviarComprovante");
			MensagemUtil.sucesso("Comprovante de pagamento enviado.");
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			MensagemUtil.erro("Ococrreu um Erro.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	private void tramitarReenquadramentoProcesso(StatusReenquadramentoEnum status) throws Exception{
		tramitacaoReenquadramentoProcessoServiceFacade.criarTramitacaoReenquadramentoProcessoSemFlush(
			processoReenquadramentoDTO.getProcessoReenquadramento(), 
			new StatusReenquadramento(status), 
			ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa()
		);
	}

	/**
	 * Método que prepara o popup de validar comprovante de pagamento.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	public void validarComprovantePopUp() {

		limparPopUpValidarComprovante();

		try {

			validarComprovanteBoletoPagamentoRequerimento = boletoPagamentoRequerimentoService.consultarPorId(validarComprovanteBoletoComplementar.getId());
			
			enviarComprovanteBoleto = validarComprovanteBoletoComplementar;

			if (!Util.isNullOuVazio(validarComprovanteBoletoPagamentoRequerimento)){
				validarComprovanteNumRequerimentoProcesso = defineLabelRequerimentoProcesso(validarComprovanteBoletoPagamentoRequerimento);
				Requerimento req = null;
				if(Util.isNullOuVazio(validarComprovanteBoletoPagamentoRequerimento.getIdeRequerimento()))
					req = requerimentoService.buscarEntidadePorId(validarComprovanteBoletoPagamentoRequerimento.getIdeProcesso().getIdeRequerimento());
				else
					req = requerimentoService.buscarEntidadePorId(validarComprovanteBoletoPagamentoRequerimento.getIdeRequerimento());
				
				for (RequerimentoPessoa rp : req.getRequerimentoPessoaCollection()) {
					
					if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {
	
						if (!Util.isNullOuVazio(rp.getPessoa())) validarComprovantePessoa = rp.getPessoa();
					}
				}
	
				validarComprovanteComprovantePagamento = comprovantePagamentoService.consultarPorIdBoletoPagamentoRequerimento(validarComprovanteBoletoPagamentoRequerimento.getIdeBoletoPagamentoRequerimento());
	
				if (validarComprovanteComprovantePagamento != null) {
					validarComprovanteTamanhoArquivo = getTamanhoDoc(validarComprovanteComprovantePagamento.getDscCaminhoArquivo());
				}
			}
		} catch (Exception e) {
			MensagemUtil.sucesso(BUNDLE.getString("validar_comprovante_msg_erro_validar"));
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * obtem o nome do arquivo do dae
	 */
	public String obterNomeArquivoDae(String caminho){
		
		if (caminho.contains("vistoria")){
			return "Comprovante do DAE - Vistoria";
		}
		return "Comprovante do DAE - Certificado";
	}
	
	public void carregarValidacaoComprovante(ProcessoReenquadramentoDTO processoReenquadramentoDTO) throws Exception{
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;

		carregarEnviarComprovanteBoleto(processoReenquadramentoDTO);
		
		if (!Util.isNullOuVazio(daeReequerimento)){
			validarComprovanteBoletoComplementar = daeReequerimento;
			validarComprovantePopUpDae();
		}
		
		if (!Util.isNullOuVazio(boletoReequerimento)){
			validarComprovanteBoletoComplementar = boletoReequerimento;
			validarComprovantePopUp();
		}
	}
	
	/**
	 * Método que prepara o popup de validar comprovante de pagamento.
	 */
	public void validarComprovantePopUpDae() {

		limparPopUpValidarComprovante();
		
		try {
			validarComprovanteBoletoDaeRequerimento = boletoDaeRequerimentoService.carregarById(validarComprovanteBoletoComplementar.getId());
			
			enviarComprovanteBoleto = validarComprovanteBoletoComplementar;
			
			if (!Util.isNullOuVazio(validarComprovanteBoletoDaeRequerimento)){
				
			
				validarComprovanteNumRequerimentoProcesso = defineLabelDaeProcesso(validarComprovanteBoletoDaeRequerimento);
				Requerimento req = null;
				if(Util.isNullOuVazio(validarComprovanteBoletoDaeRequerimento.getIdeRequerimento()))
					req = requerimentoService.buscarEntidadePorId(validarComprovanteBoletoDaeRequerimento.getIdeProcesso().getIdeRequerimento());
				else
					req = requerimentoService.buscarEntidadePorId(validarComprovanteBoletoDaeRequerimento.getIdeRequerimento());
				
				for (RequerimentoPessoa rp : req.getRequerimentoPessoaCollection()) {
					
					if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {
	
						if (!Util.isNullOuVazio(rp.getPessoa())) validarComprovantePessoa = rp.getPessoa();
					}
				}
	
				listaComprovanteDae = comprovantePagamentoDaeService.consultarPorIdBoletoDaeRequerimento(validarComprovanteBoletoDaeRequerimento.getIdeBoletoDaeRequerimento());
			}
		} catch (Exception e) {
			MensagemUtil.sucesso(BUNDLE.getString("validar_comprovante_msg_erro_validar"));
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Retorna uma {@link String} com o Num. Requerimento ou Num. Processo.
	 * 
	 * @author micael.coutinho
	 * @param bolPagReq
	 * @return
	 */
	public String defineLabelRequerimentoProcesso(BoletoPagamentoRequerimento bolPagReq) {

		if (bolPagReq.getIdeRequerimento() != null && !Util.isNullOuVazio(bolPagReq.getIdeRequerimento().getNumRequerimento())) {
			return BUNDLE.getString("consulta_boleto_num_requerimento") + ": " + bolPagReq.getIdeRequerimento().getNumRequerimento();
		} else if (bolPagReq.getIdeProcesso() != null && !Util.isNullOuVazio(bolPagReq.getIdeProcesso().getNumProcesso())) {
			return BUNDLE.getString("consulta_boleto_num_processo") + ": " + bolPagReq.getIdeProcesso().getNumProcesso();
		}
		
		return StringUtils.EMPTY;
	}
	
	/**
	 * Retorna uma {@link String} com o Num. Requerimento ou Num. Processo. DAE
	 * 
	 * @param bolPagReq
	 * @return
	 */
	public String defineLabelDaeProcesso(BoletoDaeRequerimento bolPagReq) {

		if (bolPagReq.getIdeRequerimento() != null && !Util.isNullOuVazio(bolPagReq.getIdeRequerimento().getNumRequerimento())) {
			return BUNDLE.getString("consulta_boleto_num_requerimento") + ": " + bolPagReq.getIdeRequerimento().getNumRequerimento();
		} else if (bolPagReq.getIdeProcesso() != null && !Util.isNullOuVazio(bolPagReq.getIdeProcesso().getNumProcesso())) {
			return BUNDLE.getString("consulta_boleto_num_processo") + ": " + bolPagReq.getIdeProcesso().getNumProcesso();
		}
		
		return StringUtils.EMPTY;
	}

	/**
	 * Método que retorna o tamanho de um arquivo em KB.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 * @param caminhoArquivo - Local onde se encontra o arquivo.
	 * @return {@link String} com o tamanho do arquivo e a medida KB no final.
	 */
	public String getTamanhoDoc(String caminhoArquivo) {

		File arquivo = null;

		if (!Util.isNullOuVazio(caminhoArquivo)) {
			arquivo = new File(caminhoArquivo);
			
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
			}
		}
		
		return StringUtils.EMPTY;
	}

	/**
	 * Método que realiza o download do comprovante de pagamento.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 * @return {@link StreamedContent} utilizado na view pelo componente fileDownload do primefaces.
	 */
	public StreamedContent getFileDownload() {

		String caminhoArquivo = validarComprovanteComprovantePagamento.getDscCaminhoArquivo();
		StreamedContent file = null;

		try {
			InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
			String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
			file = new DefaultStreamedContent(stream, mimeType, caminhoArquivo.substring(caminhoArquivo.lastIndexOf(File.separator) + 1));
		} catch (FileNotFoundException e) {
			MensagemUtil.erro(BUNDLE.getString("validar_comprovante_msg_erro_download"));
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

		return file;
	}

	/**
	 * Método que valida um comprovante de pagamento e altera o status do boleto complementar para VALIDADO.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	public void validaComprovantePagamento() {

		try {
			if (validarComprovanteDocumentoValidado) {
				boletoComplementarService.validaComprovantePagamento(this);
				
				Html.esconder("dialogValidarComprovante");
				MensagemUtil.sucesso(BUNDLE.getString("validar_comprovante_msg_sucesso_salvar"));
			}
		} catch (Exception e) {
			MensagemUtil.erro(BUNDLE.getString("validar_comprovante_msg_erro_validar"));
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método que valida um comprovante de pagamento dae e altera o status do boleto complementar para VALIDADO.
	 */
	public void validaComprovantePagamentoDae() {

		try {
			
			if (validarComprovanteDocumentoValidado) {	
				boletoComplementarService.validaComprovantePagamentoDae(this);
				
				Html.esconder("dialogValidarComprovanteDae");
				MensagemUtil.sucesso(BUNDLE.getString("validar_comprovante_msg_sucesso_salvar"));
			}
		} catch (Exception e) {
			MensagemUtil.erro(BUNDLE.getString("validar_comprovante_msg_erro_validar"));
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public Boolean mudarStatusProcessoReenquadramento(StatusReenquadramentoEnum status, boolean salvarComprovanteDae, boolean validacaoComprovante) throws Exception{
		Boolean primeiraValidacao = Boolean.FALSE;
		
		List<BoletoComplementarDTO> listaBoletoComplementarReequerimento = boletoComplementarService.consultarBoletoComplementar(getFilter(), 0, 2, isUsuarioExterno());
		getFilter().setIdeProcessoReenquadramento(null);
		
		if (!Util.isNullOuVazio(listaBoletoComplementarReequerimento) && (listaBoletoComplementarReequerimento.size() == 1 || salvarComprovanteDae)){
			setProcessoReenquadramentoDTO(new ProcessoReenquadramentoDTO());
			getProcessoReenquadramentoDTO().setProcessoReenquadramento(new ProcessoReenquadramento(listaBoletoComplementarReequerimento.get(0).getIdeProcessoReenquadramento()));
			tramitarReenquadramentoProcesso(status);
		} else {
			if (validacaoComprovante){
				if (StatusReenquadramentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getId().equals(status.getId())){
					setProcessoReenquadramentoDTO(new ProcessoReenquadramentoDTO());
					getProcessoReenquadramentoDTO().setProcessoReenquadramento(new ProcessoReenquadramento(listaBoletoComplementarReequerimento.get(0).getIdeProcessoReenquadramento()));
					tramitarReenquadramentoProcesso(status);
					
					primeiraValidacao = Boolean.TRUE;
				} else {
					getFilter().setPago(false);
					listaBoletoComplementarReequerimento = boletoComplementarService.consultarBoletoComplementar(getFilter(), 0, 2, isUsuarioExterno());
					
					if (listaBoletoComplementarReequerimento.size() == 1){
						setProcessoReenquadramentoDTO(new ProcessoReenquadramentoDTO());
						getProcessoReenquadramentoDTO().setProcessoReenquadramento(new ProcessoReenquadramento(listaBoletoComplementarReequerimento.get(0).getIdeProcessoReenquadramento()));
						tramitarReenquadramentoProcesso(status);
						
						primeiraValidacao = Boolean.FALSE;
					}
				}
			}
		}
		
	
		return primeiraValidacao;
	}

	
	/**
	 * Método que invalida um comprovante de pagamento e altera o status do boleto complementar para PENDÊNCIA DE VALIDAÇÃO DE COMPROVANTE.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	public void invalidaComprovantePagamento() {

		try {

			if (!validarComprovanteDocumentoValidado) {
				boletoComplementarService.invalidaComprovantePagamento(this);
				
				Html.esconder("dialogValidarComprovante");
				MensagemUtil.sucesso("Pendência de validação de comprovante registrada com sucesso!");
			}
		} catch (Exception e) {
			MensagemUtil.erro(BUNDLE.getString("validar_comprovante_msg_erro_validar"));
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método que invalida um comprovante de pagamento e altera o status do boleto complementar para PENDÊNCIA DE VALIDAÇÃO DE COMPROVANTE.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	public void invalidaComprovantePagamentoDae() {

		try {

			if (!validarComprovanteDocumentoValidado) {
				boletoComplementarService.invalidaComprovantePagamentoDae(this);

				Html.esconder("dialogValidarComprovanteDae");
				MensagemUtil.sucesso("Pendência de validação de comprovante registrada com sucesso!");
			}
		} catch (Exception e) {
			MensagemUtil.erro(BUNDLE.getString("validar_comprovante_msg_erro_validar"));
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método utilizado para exibir o botão de 'Download' na tela principal.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param status do boleto
	 * @return TRUE para ser exibido e FALSE para ser escondido
	 */
	public boolean exibeAcaoDownload(String status) {
		return !(status.equals(StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getValue()) || status.equals(StatusBoletoEnum.CANCELADO.getValue())
					|| status.equals(StatusBoletoEnum.EM_PROCESSAMENTO.getValue()));
	}

	/**
	 * Método utilizado para exibir o botão de 'Upload' na tela principal.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param status do boleto
	 * @return TRUE para ser exibido e FALSE para ser escondido
	 */
	public boolean exibeAcaoUpload(String status) {

		if (status.equals(StatusBoletoEnum.EMITIDO.getValue()) || status.equals(StatusBoletoEnum.PENDENCIA_VALIDACAO.getValue())) return true;
		else return false;
	}

	/**
	 * Método utilizado para exibir o botão de 'Cancelamento' na tela principal.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param status do boleto
	 * @return TRUE para ser exibido e FALSE para ser escondido
	 */
	public boolean exibeAcaoCancelar(String status) {

		if (status.equals(StatusBoletoEnum.EMITIDO.getValue()) || status.equals(StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getValue())) return true;
		else return false;
	}

	/**
	 * Método utilizado para exibir o botão de 'Validação' na tela principal.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param status do boleto
	 * @return TRUE para ser exibido e FALSE para ser escondido
	 */
	public boolean exibeAcaoValidar(String status) {

		if (status.equals(StatusBoletoEnum.COMPROVANTE.getValue())) return true;
		else return false;
	}
	

	/**
	 * Método utilizado para exibir o botão de 'Histórico' na tela principal.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param status do boleto
	 * @return TRUE para ser exibido e FALSE para ser escondido
	 */
	public boolean exibeAcaoHistorico(String status) {

		if (!isUsuarioExterno()) return true;
		else return false;
	}
	
	public boolean exibeNumBoletoCanceladoOuCancelamentoSolicitado(String status){
		if(status.equals(StatusBoletoEnum.CANCELADO.getValue()) || status.equals(StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getValue()))
			return true;
		else 
			return false;
	}
	
	public boolean isBoletoRegistrado(BoletoComplementarDTO boleto) {

		try {
			BoletoPagamentoRequerimento bpr = boletoPagamentoRequerimentoService.consultarPorId(boleto.getId());
			
			if(!Util.isNullOuVazio(bpr) && bpr.getIndBoletoRegistrado() != null) {
				return bpr.getIndBoletoRegistrado() || "Pago".equals(boleto.getStatus())
						;
			}
			
			return false;
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public String getDataAtual() {
		return Util.getDataHoje();
	}
	
	/**
	 * Retorna TRUE caso algum status tenha sido selecionado na tela de filtro e o usuario seja interno.
	 * 
	 * @return the datas
	 */
	public boolean isDatas() {
		
		if (isUsuarioExterno()) setDatas(false);
		else if (getFilter().isStatusSelecionado()) setDatas(true);
		else setDatas(false);
		
		return datas;
	}
	
	/**
	 * Método que prepara o popup de histórico do boleto.
	 * 
	 * @author Vitor Aléxis de Almeida Leitão (vitor.leitao@zcr.com.br)
	 */
	public void abreHistoricoPopUp() {

		limparPopUpHistoricoBoleto();
		
		try {
			historicoBoletoBoletoPagamentoRequerimento = boletoPagamentoRequerimentoService.consultarPorId(historicoBoletoBoletoComplementar.getId());
			historicoBoletoNumProcesso = defineLabelRequerimentoProcesso(historicoBoletoBoletoPagamentoRequerimento);
			
			historicoBoletoRequerimento = carregarRequerimentoDoBoleto(historicoBoletoBoletoPagamentoRequerimento);
			
			for (RequerimentoPessoa rp : historicoBoletoRequerimento.getRequerimentoPessoaCollection()) {
				if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {
					historicoBoletoPessoa = rp.getPessoa();
				}
			}
			
			historicoBoletoDataTable = boletoPagamentoHistoricoService.findByBoletoPagamentoRequerimento(historicoBoletoBoletoPagamentoRequerimento.getIdeBoletoPagamentoRequerimento());
		} catch (Exception e) {
			MensagemUtil.erro("Erro ao carregar histórico do boleto.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método que prepara o popup de histórico do boleto dae.
	 * 
	 */
	public void abreHistoricoPopUpDae() {

		limparPopUpHistoricoBoleto();
		
		try {
			historicoBoletoBoletoDaeRequerimento = boletoDaeRequerimentoService.carregarById(historicoBoletoBoletoComplementar.getId());
			historicoBoletoNumProcesso = defineLabelDaeProcesso(historicoBoletoBoletoDaeRequerimento);
			
			historicoBoletoRequerimento = carregarRequerimentoDoDae(historicoBoletoBoletoDaeRequerimento);
			
			for (RequerimentoPessoa rp : historicoBoletoRequerimento.getRequerimentoPessoaCollection()) {
				if (rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {
					historicoBoletoPessoa = rp.getPessoa();
				}
			}
			
			historicoBoletoDataTableDae = boletoDaeHistoricoService.findByBoletoDaeRequerimento(historicoBoletoBoletoDaeRequerimento.getIdeBoletoDaeRequerimento());
		} catch (Exception e) {
			MensagemUtil.erro("Erro ao carregar histórico do boleto.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Remover comprovante de boleto
	 */
	public void excluirComprovanteBoleto()  {
		boletoComplementarService.excluirComprovanteBoleto(this);
		
		MensagemUtil.sucesso(BUNDLE.getString("consulta_boleto_exlusao_comprovante_boleto"));
	}
	
	/**
	 * Remover comprovante de DAE
	 */
	public void excluirComprovanteDAE(String tipo) throws Exception {
		boletoComplementarService.excluirComprovanteDAE(this, tipo);
		
		MensagemUtil.sucesso(BUNDLE.getString("consulta_boleto_exlusao_comprovante_dae"));
		Html.atualizar("formDialogComprovante", "certificadoForm", "vistoriaForm");
	}
	
	/**
	 * Método que realiza o download de DAE
	 */
	public StreamedContent baixarArquivoDae(String caminhoArquivo) {
		try {
			return gerenciaArquivoservice.getContentFile(caminhoArquivo);
		} 
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	/**
	 * Faz upload do comprovante de pagamento para o DAE
	 */
	public void uploadDaeCertificadoVistoria(FileUploadEvent event) {
		try {
			Integer id = enviarComprovanteBoleto.getId();
			
			if (isReenquadramento() && !Util.isNullOuVazio(daeReequerimento)){
				id = daeReequerimento.getId();
			}
			
			String tipo = (String) event.getComponent().getAttributes().get("uploadDaeTipo");
			BoletoDaeRequerimento certificadoVistoria = new BoletoDaeRequerimento(id);  
			
			if ("certificado".equals(tipo)){
				String path = this.gerenciaArquivoservice.uploadArquivoComprovanteBoletoDae(event.getFile(), certificadoVistoria, "certificado_dae");
				certificadoVistoria.setPathComprovante(path);
				enviarComprovantePagamentoDTO.setCertificado(certificadoVistoria);
			} else {
				String path = this.gerenciaArquivoservice.uploadArquivoComprovanteBoletoDae(event.getFile(), certificadoVistoria, "vistoria_dae");
				certificadoVistoria.setPathComprovante(path);
				enviarComprovantePagamentoDTO.setVistoria(certificadoVistoria);
			}
			
			MensagemUtil.sucesso(BUNDLE.getString("geral_msg_arquivo_enviado_sucesso"));
		} 
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void atualizarEnviarComprovanteBoleto(){
		if (isReenquadramento()){
			if (new StatusReenquadramento(StatusReenquadramentoEnum.BOLETO_PAGAMENTO_LIBERADO).equals(processoReenquadramentoDTO.getStatusAtual())){
				getFilter().setEmitido(true);
			}
			
			if (new StatusReenquadramento(StatusReenquadramentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE).equals(processoReenquadramentoDTO.getStatusAtual())){
				getFilter().setPendenciaValidacaoComprovante(true);
			}
		}
	}
	/**
	 * Salvar os comprovante de DAE no banco
	 */
	public void salvarComprovanteDae() throws Exception{
		
		try {
			
			boletoComplementarService.salvarComprovanteDae(this);
			
			Html.esconder("dialogEnviarComprovante");
			MensagemUtil.sucesso(BUNDLE.getString("geral_msg_arquivo_enviado_sucesso"));
			
		} catch (SeiaValidacaoRuntimeException e) {
			MensagemUtil.alerta(e.getMessage());
			Html.atualizar("formDialogComprovante", "certificadoForm", "vistoriaForm");
			
		} catch (Exception e) {
			MensagemUtil.erro("Ococrreu um Erro.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public boolean isReenquadramento() {
		return !Util.isNull(processoReenquadramentoDTO);
	}
	
	public boolean isRenderedSalvar() {
		if(!Util.isNull(enviarComprovanteBoleto) 
			&& (!enviarComprovanteBoleto.getIndBoletoRegistrado() 
				|| BigDecimal.ZERO.compareTo(enviarComprovanteBoleto.getVlrTotalCertificado()) < 0
				|| BigDecimal.ZERO.compareTo(enviarComprovanteBoleto.getVlrTotalVistoria()) < 0)) {
			return true;
		}		
		return false;
	}

	public String getTextoValorVistoria() {
		if (textoValorVistoria == null) 
			setTextoValorVistoria(Util.getString("enviar_comprovante_lbl_descricao_dae_vistoria", enviarComprovanteBoleto.getVlrTotalVistoriaFormatado()));
		
		return textoValorVistoria;
	}
	
	public String getTextoValorCertificado() {
		if (textoValorCertificado == null) 
			setTextoValorCertificado(Util.getString("enviar_comprovante_lbl_descricao_dae_certificado", enviarComprovanteBoleto.getVlrTotalCertificadoFormatado()));
		
		return textoValorCertificado;
	}
	
	public void carregarMotivosIsencao(ValueChangeEvent e) {
		try {
			if(!novoBoletoIsencaoBoleto) novoBoletoMotivoIsencaoBoleto = null;
			if(!novoBoletoIsencaoDae) novoBoletoMotivoIsencaoDae = null;
			
			listaMotivoIsencaoBoletoParaReenquadramento = boletoComplementarServiceFacade.listarTodosMotivoIsencaoBoletoParaReenquadramento();
		} catch (Exception e2) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e2);
			throw Util.capturarException(e2, Util.SEIA_EXCEPTION);
		}
	}
	
	public void limparIsencao(ValueChangeEvent e) {
		if(!novoBoletoBoleto) novoBoletoIsencaoBoleto = false;
		if(!novoBoletoDae) novoBoletoIsencaoDae = false;
	}
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/

	public void setDatas(boolean datas) {
		this.datas = datas;
	}

	public BoletoComplementarFilter getFilter() {
		return filter;
	}

	public void setFilter(BoletoComplementarFilter filter) {
		this.filter = filter;
	}

	public boolean isPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(boolean pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public boolean isRenderizaGrid() {
		return renderizaGrid;
	}

	public void setRenderizaGrid(boolean renderizaGrid) {
		this.renderizaGrid = renderizaGrid;
	}

	public DataTable getDataTablePessoaFisica() {
		return dataTablePessoaFisica;
	}

	public void setDataTablePessoaFisica(DataTable dataTablePessoaFisica) {
		this.dataTablePessoaFisica = dataTablePessoaFisica;
	}

	public DataTable getDataTablePessoaJuridica() {
		return dataTablePessoaJuridica;
	}

	public void setDataTablePessoaJuridica(DataTable dataTablePessoaJuridica) {
		this.dataTablePessoaJuridica = dataTablePessoaJuridica;
	}

	public DataTable getDataTableBoleto() {
		return dataTableBoleto;
	}

	public void setDataTableBoleto(DataTable dataTableBoleto) {
		this.dataTableBoleto = dataTableBoleto;
	}

	public LazyDataModel<PessoaFisica> getPessoaFisicaModel() {
		return pessoaFisicaModel;
	}

	public void setPessoaFisicaModel(LazyDataModel<PessoaFisica> pessoaFisicaModel) {
		this.pessoaFisicaModel = pessoaFisicaModel;
	}

	public LazyDataModel<PessoaJuridica> getPessoaJuridicaModel() {
		return pessoaJuridicaModel;
	}

	public void setPessoaJuridicaModel(LazyDataModel<PessoaJuridica> pessoaJuridicaModel) {
		this.pessoaJuridicaModel = pessoaJuridicaModel;
	}

	public LazyDataModel<BoletoComplementarDTO> getBoletoModel() {
		return boletoModel;
	}

	public void setBoletoModel(LazyDataModel<BoletoComplementarDTO> boletoModel) {
		this.boletoModel = boletoModel;
	}

	public String getNomeRequerenteFilter() {
		return nomeRequerenteFilter;
	}

	public void setNomeRequerenteFilter(String nomeRequerenteFilter) {
		this.nomeRequerenteFilter = nomeRequerenteFilter;
	}

	public String getDocumentoRequerenteFilter() {
		return documentoRequerenteFilter;
	}

	public void setDocumentoRequerenteFilter(String documentoRequerenteFilter) {
		this.documentoRequerenteFilter = documentoRequerenteFilter;
	}

	public boolean isUsuarioExterno() {
		return usuarioExterno;
	}

	public void setUsuarioExterno(boolean usuarioExterno) {
		this.usuarioExterno = usuarioExterno;
	}

	public List<TipoBoletoPagamento> getListTipoBoleto() {
		return listTipoBoleto;
	}

	public void setListTipoBoleto(List<TipoBoletoPagamento> listTipoBoleto) {
		this.listTipoBoleto = listTipoBoleto;
	}

	public TipoBoletoPagamento getNovoBoleto_tipo() {
		return novoBoletoTipo;
	}

	public void setNovoBoleto_tipo(TipoBoletoPagamento novoBoleto_tipo) {
		this.novoBoletoTipo = novoBoleto_tipo;
	}

	public String getNovoBoleto_numero() {
		return novoBoletoNumero;
	}

	public void setNovoBoleto_numero(String novoBoleto_numero) {
		this.novoBoletoNumero = novoBoleto_numero;
	}

	public boolean isNovoBoleto_renderizaPanelDados() {
		return novoBoletoRenderizaPanelDados;
	}

	public void setNovoBoleto_renderizaPanelDados(boolean novoBoleto_renderizaPanelDados) {
		this.novoBoletoRenderizaPanelDados = novoBoleto_renderizaPanelDados;
	}

	public Requerimento getNovoBoleto_requerimento() {
		return novoBoletoRequerimento;
	}

	public void setNovoBoleto_requerimento(Requerimento novoBoleto_requerimento) {
		this.novoBoletoRequerimento = novoBoleto_requerimento;
	}

	public String getNovoBoleto_nomeRequerente() {
		return novoBoletonomeRequerente;
	}

	public void setNovoBoleto_nomeRequerente(String novoBoleto_nomeRequerente) {
		this.novoBoletonomeRequerente = novoBoleto_nomeRequerente;
	}

	public String getNovoBoleto_cpfCnpjRequerente() {
		return novoBoletoCpfCnpjRequerente;
	}

	public void setNovoBoleto_cpfCnpjRequerente(String novoBoleto_cpfCnpjRequerente) {
		this.novoBoletoCpfCnpjRequerente = novoBoleto_cpfCnpjRequerente;
	}

	public Double getNovoBoleto_valor() {
		return novoBoletoValor;
	}

	public void setNovoBoleto_valor(Double novoBoletoValor) {
		this.novoBoletoValor = novoBoletoValor;
	}

	public Date getNovoBoleto_dataVencimento() {
		return novoBoletoDataVencimento;
	}

	public void setNovoBoleto_dataVencimento(Date novoBoletodataVencimento) {
		this.novoBoletoDataVencimento = novoBoletodataVencimento;
	}

	public Processo getNovoBoleto_processo() {
		return novoBoletoProcesso;
	}

	public void setNovoBoleto_processo(Processo novoBoletoProcesso) {
		this.novoBoletoProcesso = novoBoletoProcesso;
	}

	public BoletoComplementarDTO getEnviarComprovante_boleto() {
		return enviarComprovanteBoleto;
	}

	public void setEnviarComprovante_boleto(BoletoComplementarDTO enviarComprovanteBoleto) {
		this.enviarComprovanteBoleto = enviarComprovanteBoleto;
	}

	public Pessoa getValidarComprovante_pessoa() {
		return validarComprovantePessoa;
	}
	
	public void setValidarComprovante_pessoa(Pessoa validarComprovantePessoa) {
		this.validarComprovantePessoa = validarComprovantePessoa;
	}

	public String getValidarComprovante_numRequerimentoProcesso() {
		return validarComprovanteNumRequerimentoProcesso;
	}

	public void setValidarComprovante_numRequerimentoProcesso(String validarComprovanteNumRequerimentoProcesso) {
		this.validarComprovanteNumRequerimentoProcesso = validarComprovanteNumRequerimentoProcesso;
	}

	public boolean isValidarComprovante_documentoValidado() {
		return validarComprovanteDocumentoValidado;
	}

	public void setValidarComprovante_documentoValidado(boolean validarComprovanteDocumentoValidado) {
		this.validarComprovanteDocumentoValidado = validarComprovanteDocumentoValidado;
	}

	public String getValidarComprovante_tamanhoArquivo() {
		return validarComprovanteTamanhoArquivo;
	}

	public void setValidarComprovante_tamanhoArquivo(String validarComprovante_tamanhoArquivo) {
		this.validarComprovanteTamanhoArquivo = validarComprovante_tamanhoArquivo;
	}

	public ComprovantePagamento getValidarComprovante_comprovantePagamento() {
		return validarComprovanteComprovantePagamento;
	}

	public void setValidarComprovante_comprovantePagamento(ComprovantePagamento validarComprovanteComprovantePagamento) {
		this.validarComprovanteComprovantePagamento = validarComprovanteComprovantePagamento;
	}

	public BoletoComplementarDTO getValidarComprovante_boletoComplementar() {
		return validarComprovanteBoletoComplementar;
	}

	public void setValidarComprovante_boletoComplementar(BoletoComplementarDTO validarComprovanteBoletoComplementar) {
		this.validarComprovanteBoletoComplementar = validarComprovanteBoletoComplementar;
	}

	public BoletoPagamentoRequerimento getValidarComprovante_boletoPagamentoRequerimento() {
		return validarComprovanteBoletoPagamentoRequerimento;
	}

	public void setValidarComprovante_boletoPagamentoRequerimento(BoletoPagamentoRequerimento validarComprovante_boletoPagamentoRequerimento) {
		this.validarComprovanteBoletoPagamentoRequerimento = validarComprovante_boletoPagamentoRequerimento;
	}

	public BoletoComplementarDTO getBoletoCancelar() {
		return boletoCancelar;
	}

	public void setBoletoCancelar(BoletoComplementarDTO boletoCancelar) {
		this.boletoCancelar = boletoCancelar;
	}

	public BoletoPagamentoRequerimento getBoletoPagReqCancelar() {
		return boletoPagReqCancelar;
	}

	public void setBoletoPagReqCancelar(BoletoPagamentoRequerimento boletoPagReqCancelar) {
		this.boletoPagReqCancelar = boletoPagReqCancelar;
	}

	public BoletoPagamentoHistorico getBoletoPagamentHistorico() {
		return boletoPagamentHistorico;
	}

	public void setBoletoPagamentHistorico(BoletoPagamentoHistorico boletoPagamentHistorico) {
		this.boletoPagamentHistorico = boletoPagamentHistorico;
	}

	public List<MotivoCancelamentoBoleto> getListaMotivoCancelamentoBoleto() {
		return listaMotivoCancelamentoBoleto;
	}

	public void setListaMotivoCancelamentoBoleto(List<MotivoCancelamentoBoleto> listaMotivoCancelamentoBoleto) {
		this.listaMotivoCancelamentoBoleto = listaMotivoCancelamentoBoleto;
	}

	public String getLabelNumRequerimentoOuProcesso() {
		return labelNumRequerimentoOuProcesso;
	}

	public void setLabelNumRequerimentoOuProcesso(String labelNumRequerimentoOuProcesso) {
		this.labelNumRequerimentoOuProcesso = labelNumRequerimentoOuProcesso;
	}

	public BoletoPagamentoHistorico getBoletoPagamentHistUltimo() {
		return boletoPagamentHistUltimo;
	}

	public void setBoletoPagamentHistUltimo(BoletoPagamentoHistorico boletoPagamentHistUltimo) {
		this.boletoPagamentHistUltimo = boletoPagamentHistUltimo;
	}

	public String getNovoBoleto_mensagemErro() {
		return novoBoletoMensagemErro;
	}

	public void setNovoBoleto_mensagemErro(String novoBoleto_mensagemErro) {
		this.novoBoletoMensagemErro = novoBoleto_mensagemErro;
	}

	public BoletoComplementarDTO getHistoricoBoleto_boletoComplementar() {
		return historicoBoletoBoletoComplementar;
	}
	
	public void setHistoricoBoleto_boletoComplementar(BoletoComplementarDTO historicoBoleto_boletoComplementar) {
		this.historicoBoletoBoletoComplementar = historicoBoleto_boletoComplementar;
	}

	public Pessoa getHistoricoBoleto_pessoa() {
		return historicoBoletoPessoa;
	}
	
	public void setHistoricoBoleto_pessoa(Pessoa historicoBoleto_pessoa) {
		this.historicoBoletoPessoa = historicoBoleto_pessoa;
	}
	
	public String getHistoricoBoleto_numProcesso() {
		return historicoBoletoNumProcesso;
	}
	
	public void setHistoricoBoleto_numProcesso(String historicoBoleto_numProcesso) {
		this.historicoBoletoNumProcesso = historicoBoleto_numProcesso;
	}
	
	public BoletoPagamentoRequerimento getHistoricoBoleto_boletoPagamentoRequerimento() {
		return historicoBoletoBoletoPagamentoRequerimento;
	}

	public void setHistoricoBoleto_boletoPagamentoRequerimento(BoletoPagamentoRequerimento historicoBoleto_boletoPagamentoRequerimento) {
		this.historicoBoletoBoletoPagamentoRequerimento = historicoBoleto_boletoPagamentoRequerimento;
	}

	public Requerimento getHistoricoBoleto_requerimento() {
		return historicoBoletoRequerimento;
	}

	public void setHistoricoBoleto_requerimento(Requerimento historicoBoleto_requerimento) {
		this.historicoBoletoRequerimento = historicoBoleto_requerimento;
	}

	public List<BoletoPagamentoHistorico> getHistoricoBoleto_dataTable() {
		return historicoBoletoDataTable;
	}
	
	public void setHistoricoBoleto_dataTable(List<BoletoPagamentoHistorico> historicoBoleto_dataTable) {
		this.historicoBoletoDataTable = historicoBoleto_dataTable;
	}
	
	public String getValidarComprovante_dscInvalidacaoComprovante() {
		return validarComprovanteDscInvalidacaoComprovante;
	}
	
	public void setValidarComprovante_dscInvalidacaoComprovante(String validarComprovante_dscInvalidacaoComprovante) {
		this.validarComprovanteDscInvalidacaoComprovante = validarComprovante_dscInvalidacaoComprovante;
	}
	
	public boolean isPesquisando() {
		return pesquisando;
	}

	public void setPesquisando(boolean pesquisando) {
		this.pesquisando = pesquisando;
	}
	
	public boolean isIncluindo() {
		return incluindo;
	}
	
	public void setIncluindo(boolean incluindo) {
		this.incluindo = incluindo;
	}
	
	public boolean isEditando() {
		return editando;
	}
	
	public void setEditando(boolean editando) {
		this.editando = editando;
	}
	
	public boolean isExcluindo() {
		return excluindo;
	}
	
	public void setExcluindo(boolean excluindo) {
		this.excluindo = excluindo;
	}

	public boolean isNovoBoleto_boleto() {
		return novoBoletoBoleto;
	}

	public void setNovoBoleto_boleto(boolean novoBoleto_boleto) {
		this.novoBoletoBoleto = novoBoleto_boleto;
	}

	public boolean isNovoBoleto_dae() {
		return novoBoletoDae;
	}

	public void setNovoBoleto_dae(boolean novoBoleto_dae) {
		this.novoBoletoDae = novoBoleto_dae;
	}

	public BigDecimal getNovoBoleto_vlrTotalVistoria() {
		return novoBoletoVlrTotalVistoria;
	}

	public void setNovoBoleto_vlrTotalVistoria(BigDecimal novoBoleto_vlrTotalVistoria) {
		this.novoBoletoVlrTotalVistoria = novoBoleto_vlrTotalVistoria;
	}

	public BigDecimal getNovoBoleto_vlrTotalCertificado() {
		return novoBoletoVlrTotalCertificado;
	}

	public void setNovoBoleto_vlrTotalCertificado(BigDecimal novoBoleto_vlrTotalCertificado) {
		this.novoBoletoVlrTotalCertificado = novoBoleto_vlrTotalCertificado;
	}

	public ComprovantePagamentoDae getComprovanteVistoria() {
		return comprovanteVistoria;
	}

	public void setComprovanteVistoria(ComprovantePagamentoDae comprovanteVistoria) {
		this.comprovanteVistoria = comprovanteVistoria;
	}

	public ComprovantePagamentoDae getComprovanteCertificado() {
		return comprovanteCertificado;
	}

	public void setComprovanteCertificado(ComprovantePagamentoDae comprovanteCertificado) {
		this.comprovanteCertificado = comprovanteCertificado;
	}

	public ComprovantePagamentoDae getValidarComprovante_comprovantePagamentoDae() {
		return validarComprovanteComprovantePagamentoDae;
	}

	public void setValidarComprovante_comprovantePagamentoDae(ComprovantePagamentoDae validarComprovante_comprovantePagamentoDae) {
		this.validarComprovanteComprovantePagamentoDae = validarComprovante_comprovantePagamentoDae;
	}

	public EnviarComprovantePagamentoDTO getEnviarComprovantePagamentoDTO() {
		return enviarComprovantePagamentoDTO;
	}

	public void setEnviarComprovantePagamentoDTO(EnviarComprovantePagamentoDTO enviarComprovantePagamentoDTO) {
		this.enviarComprovantePagamentoDTO = enviarComprovantePagamentoDTO;
	}

	public ComprovantePagamento getComprovanteBoleto() {
		return comprovanteBoleto;
	}

	public void setComprovanteBoleto(ComprovantePagamento comprovanteBoleto) {
		this.comprovanteBoleto = comprovanteBoleto;
	}

	public BoletoDaeRequerimento getBoletoDaeReqCancelar() {
		return boletoDaeReqCancelar;
	}

	public void setBoletoDaeReqCancelar(BoletoDaeRequerimento boletoDaeReqCancelar) {
		this.boletoDaeReqCancelar = boletoDaeReqCancelar;
	}

	public BoletoDaeHistorico getBoletoDaeHistUltimo() {
		return boletoDaeHistUltimo;
	}

	public void setBoletoDaeHistUltimo(BoletoDaeHistorico boletoDaeHistUltimo) {
		this.boletoDaeHistUltimo = boletoDaeHistUltimo;
	}

	public BoletoDaeHistorico getBoletoDaeHistorico() {
		return boletoDaeHistorico;
	}

	public void setBoletoDaeHistorico(BoletoDaeHistorico boletoDaeHistorico) {
		this.boletoDaeHistorico = boletoDaeHistorico;
	}

	public Boolean getStatusCancelar() {
		return statusCancelar;
	}

	public void setStatusCancelar(Boolean statusCancelar) {
		this.statusCancelar = statusCancelar;
	}

	public Boolean getCancelamentoSolicitado() {
		return cancelamentoSolicitado;
	}

	public void setCancelamentoSolicitado(Boolean cancelamentoSolicitado) {
		this.cancelamentoSolicitado = cancelamentoSolicitado;
	}

	public Boolean getEmitido() {
		return emitido;
	}

	public void setEmitido(Boolean emitido) {
		this.emitido = emitido;
	}

	public BoletoDaeRequerimento getHistoricoBoleto_boletoDaeRequerimento() {
		return historicoBoletoBoletoDaeRequerimento;
	}

	public void setHistoricoBoleto_boletoDaeRequerimento(BoletoDaeRequerimento historicoBoleto_boletoDaeRequerimento) {
		this.historicoBoletoBoletoDaeRequerimento = historicoBoleto_boletoDaeRequerimento;
	}

	public List<BoletoDaeHistorico> getHistoricoBoleto_dataTableDae() {
		return historicoBoletoDataTableDae;
	}

	public void setHistoricoBoleto_dataTableDae(List<BoletoDaeHistorico> historicoBoleto_dataTableDae) {
		this.historicoBoletoDataTableDae = historicoBoleto_dataTableDae;
	}

	public List<ComprovantePagamentoDae> getListaComprovanteDae() {
		return listaComprovanteDae;
	}

	public void setListaComprovanteDae(List<ComprovantePagamentoDae> listaComprovanteDae) {
		this.listaComprovanteDae = listaComprovanteDae;
	}
	
	public void setTextoValorVistoria(String textoValorVistoria) {
		this.textoValorVistoria = textoValorVistoria;
	}

	public void setTextoValorCertificado(String textoValorCertificado) {
		this.textoValorCertificado = textoValorCertificado;
	}

	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}

	public void setProcessoReenquadramentoDTO(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}

	public BoletoComplementarDTO getBoletoReequerimento() {
		return boletoReequerimento;
	}

	public void setBoletoReequerimento(BoletoComplementarDTO boletoReequerimento) {
		this.boletoReequerimento = boletoReequerimento;
	}

	public BoletoComplementarDTO getDaeReequerimento() {
		return daeReequerimento;
	}

	public void setDaeReequerimento(BoletoComplementarDTO daeReequerimento) {
		this.daeReequerimento = daeReequerimento;
	}

	public BoletoDaeRequerimento getValidarComprovante_boletoDaeRequerimento() {
		return validarComprovanteBoletoDaeRequerimento;
	}

	public void setValidarComprovante_boletoDaeRequerimento(BoletoDaeRequerimento validarComprovante_boletoDaeRequerimento) {
		this.validarComprovanteBoletoDaeRequerimento = validarComprovante_boletoDaeRequerimento;
	}

	public boolean isNovoBoleto_isencaoBoleto() {
		return novoBoletoIsencaoBoleto;
	}

	public void setNovoBoleto_isencaoBoleto(boolean novoBoleto_isencaoBoleto) {
		this.novoBoletoIsencaoBoleto = novoBoleto_isencaoBoleto;
	}

	public boolean isNovoBoleto_isencaoDae() {
		return novoBoletoIsencaoDae;
	}

	public void setNovoBoleto_isencaoDae(boolean novoBoleto_isencaoDae) {
		this.novoBoletoIsencaoDae = novoBoleto_isencaoDae;
	}

	public MotivoIsencaoBoleto getNovoBoleto_motivoIsencaoBoleto() {
		return novoBoletoMotivoIsencaoBoleto;
	}

	public void setNovoBoleto_motivoIsencaoBoleto(MotivoIsencaoBoleto novoBoleto_motivoIsencaoBoleto) {
		this.novoBoletoMotivoIsencaoBoleto = novoBoleto_motivoIsencaoBoleto;
	}

	public MotivoIsencaoBoleto getNovoBoleto_motivoIsencaoDae() {
		return novoBoletoMotivoIsencaoDae;
	}

	public void setNovoBoleto_motivoIsencaoDae(MotivoIsencaoBoleto novoBoleto_motivoIsencaoDae) {
		this.novoBoletoMotivoIsencaoDae = novoBoleto_motivoIsencaoDae;
	}

	public List<MotivoIsencaoBoleto> getListaMotivoIsencaoBoletoParaReenquadramento() {
		return listaMotivoIsencaoBoletoParaReenquadramento;
	}

	public void setListaMotivoIsencaoBoletoParaReenquadramento(List<MotivoIsencaoBoleto> listaMotivoIsencaoBoletoParaReenquadramento) {
		this.listaMotivoIsencaoBoletoParaReenquadramento = listaMotivoIsencaoBoletoParaReenquadramento;
	}

	
}