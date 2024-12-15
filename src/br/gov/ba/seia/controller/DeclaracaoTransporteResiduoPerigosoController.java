package br.gov.ba.seia.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.entity.Acondicionamento;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.DeclaracaoTransporte;
import br.gov.ba.seia.entity.DeclaracaoTransporteDestinatarioResiduo;
import br.gov.ba.seia.entity.DeclaracaoTransporteDisposicaoFinalResiduo;
import br.gov.ba.seia.entity.DeclaracaoTransporteEntidadeTransportadora;
import br.gov.ba.seia.entity.DeclaracaoTransporteGeradorResiduo;
import br.gov.ba.seia.entity.DeclaracaoTransporteResiduo;
import br.gov.ba.seia.entity.DeclaracaoTransporteResiduoEndereco;
import br.gov.ba.seia.entity.DisposicaoFinalResiduo;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.EstadoFisico;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.Residuo;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoEndereco;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.TipoVeiculo;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.EstadoEnum;
import br.gov.ba.seia.enumerator.LacTransporteOutroEnum;
import br.gov.ba.seia.enumerator.MunicipioEnum;
import br.gov.ba.seia.enumerator.OrgaoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.facade.AcondicionamentoFacade;
import br.gov.ba.seia.facade.AtoDeclaratorioFacade;
import br.gov.ba.seia.facade.DisposicaoFinalResiduoFacade;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.facade.EstadoFisicoFacade;
import br.gov.ba.seia.facade.FinalizarRequerimentoServiceFacade;
import br.gov.ba.seia.facade.TipoVeiculoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.DeclaracaoTransporteService;
import br.gov.ba.seia.service.DisposicaoFinalResiduoService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.OrgaoService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.ProcuradorPessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorRepresentanteService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.RequerimentoPessoaService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.ResiduoService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.PessoaUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Uri;
import br.gov.ba.seia.util.Util;

@Named("declaracaoTransporteResiduoPerigosoController")
@ViewScoped
public class DeclaracaoTransporteResiduoPerigosoController {

	private static final int ABA_ETAPA_3 = 2;

	private static final int ABA_ETAPA_2 = 1;

	private static final int ABA_ETAPA_1 = 0;

	private Pessoa geradorResiduo;
	
	private Pessoa destinatario;
	
	private Pessoa requerente;
	
	private EnderecoPessoa enderecoGeradorResiduo;
	
	private DeclaracaoTransporte declaracaoTransporte;
	
	private DeclaracaoTransporteGeradorResiduo declaracaoTransporteGeradorResiduo;
	
	private DeclaracaoTransporteResiduoEndereco declaracaoTransporteResiduoEndereco;
	
	private DeclaracaoTransporteResiduoEndereco declaracaoTransporteDestinacaoResiduoEndereco;
	
	private DeclaracaoTransporteDestinatarioResiduo declaracaoTransporteDestinatarioResiduo;
	
	private DeclaracaoTransporteEntidadeTransportadora entidadeTransportadoraSelecionada;
	
	private DeclaracaoTransporteResiduoEndereco enderecoSelecionado;
	
	private DeclaracaoTransporteDisposicaoFinalResiduo declaracaoTransporteDisposicaoFinalResiduo;
	
	private RequerimentoPessoa reqPessoaRequerente;
	private RequerimentoPessoa reqPessoaSolicitante;
	
	private Logradouro logradouro;
	private Logradouro logradouroPesquisa;
	private Bairro bairro;
	private Endereco endereco;
	private Municipio municipio;
	private Estado estado;
	private TipoLogradouro tipoLogradouro;
	
	private Requerimento requerimento;
	private Requerimento requerimentoConsulta;
	
	private List<DeclaracaoTransporteResiduoEndereco> listaEnderecoGeracaoResiduo;
	
	private List<Endereco> listaEnderecoResiduo;
	
	private List<DisposicaoFinalResiduo> listaDisposicaoFinalResiduo;
	
	private List<DisposicaoFinalResiduo> listaDisposicaoFinalResiduoSelected;
	
	private List<DeclaracaoTransporteResiduo> listaResiduosCadastrados;
	
	private List<Acondicionamento> listaAcondicionamentos;
	
	private List<EstadoFisico> listaEstadoFisico;
	
	private List<Residuo> listaResiduos;
	
	private List<TipoVeiculo> listaTipoVeiculo;
	
	private List<Residuo> listaResiduoSelected;
	
	private List<DeclaracaoTransporteResiduo> listaResiduoSelecionado;
	
	private List<Logradouro> listaLogradouro;
	private List<Bairro> listaBairro;
	private List<Municipio> listaMunicipio;
	private List<Estado> listaEstado;
	
	private List<DeclaracaoTransporteEntidadeTransportadora> listaEntidadeTransportadora;
	
	private LazyDataModel<DeclaracaoTransporteEntidadeTransportadora> listaEntidadeTransportadoraModel;
	
	private LazyDataModel<DeclaracaoTransporteResiduoEndereco> listaEnderecoGeradorResiduoModel;
	
	private String nomeResiduo;
	
	private DeclaracaoTransporteResiduo residuoSelecionado; 
	
	private Residuo residuoSelected;
	
	private Boolean desabilitaTab01;
	
	private Boolean desabilitaTab02;
	
	private Boolean desabilitaTab03;
	private Boolean isMunicipioSalvador;
	private Boolean isEstadoBahia;
	
	private boolean showInputs;
	private boolean showInputLogradouro;
	private boolean enableFormEndereco;
	private boolean escolheuBairro = false;
	private Boolean showUfCidade = Boolean.FALSE;
	private Boolean showFdbairro = Boolean.FALSE;
	
	private boolean permiteEditar = true;
	
	private boolean permiteEditarResiduo = false;
	
	boolean disposicaoFinalResiduoOutros = false;
	
	private int activeIndex;
	
	private RequerimentoDTO requerimentoDTO;
	
	@EJB
	private PessoaService pessoaService;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	@EJB
	private DisposicaoFinalResiduoFacade disposicaoFinalResiduoFacade;
	
	@EJB
	private ResiduoService residuoService;
	
	@EJB
	private AcondicionamentoFacade acondicionamentoFacade;
	
	@EJB
	private EstadoFisicoFacade estadoFisicoFacade;
	
	@EJB
	private TipoVeiculoFacade tipoVeiculoFacade;
	
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private DeclaracaoTransporteService declaracaoTransporteService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private RequerimentoPessoaService requerimentoPessoaService; 
	
	@EJB
	private AtoDeclaratorioFacade atoDeclaratorioFacade;
	
	@EJB
	private LogradouroService logradouroService;
	
	@EJB
	private DisposicaoFinalResiduoService disposicaoFinalResiduoService;
	
	@EJB
	private BairroService bairroService;
	
	@EJB
	private FinalizarRequerimentoServiceFacade finalizarRequerimentoService;
	
	@EJB
	private OrgaoService orgaoService;
	
	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@Inject
	private DeclaracaoEnderecoGeradorResiduoController declaracaoEnderecoGeradorResiduoController;
	
	@Inject
	private EntidadeTransportadoraController entidadeTransportadoraController;
	
	@Inject
	private DeclaracaoEnderecoDestinacaoResiduoController declaracaoEnderecoDestinacaoResiduoController;
	
	@Inject
	private CertificadoUtil certificadoUtil;
	
	@EJB
	private ProcuradorRepresentanteService procuradorRepresentanteService;
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	@PostConstruct
	public void init() {
		final Object temp = ContextoUtil.getContexto().getObject();
		// Caso o usuário clique em VISUALIZAR ou EDITAR na tela de CONSULTA.
		if (temp != null && temp instanceof RequerimentoDTO) {
			requerimentoDTO = ((RequerimentoDTO) temp);
			
			this.requerimento = requerimentoDTO.getRequerimento();
			
			if (requerimentoDTO.isVisualizar()) {
				permiteEditar = false;
			} else {
				permiteEditar = true;
			}
		}
		
		if(activeIndex == ABA_ETAPA_1) {
			carregarDadosAba1();
		}
		
		habilitarAbas();
		
		if(activeIndex == ABA_ETAPA_2) {
			carregarDadosAba2();
		}
		
		requerimentoConsulta = new Requerimento();
		
		ContextoUtil.getContexto().setObject(null);
	}

	private void carregarDadosAba1() {
		this.destinatario = new Pessoa();
		this.destinatario.setPessoaJuridica(new PessoaJuridica());
		
		this.declaracaoTransporteDestinacaoResiduoEndereco = new DeclaracaoTransporteResiduoEndereco();
		
		inicializarDTRP();
	}

	private void habilitarAbas() {
		
		if(!Util.isNull(this.getDeclaracaoTransporte()) && !Util.isNull(this.getDeclaracaoTransporte().getIdeDeclaracaoTransporte())) {
			boolean temGeradorResiduo = (!Util.isNull(getDeclaracaoTransporte().getDeclaracaoTransporteGeradorResiduo()) && !Util.isNull(getDeclaracaoTransporte().getDeclaracaoTransporteGeradorResiduo().getIdeDeclaracaoTransporteGeradorResiduo()));
			
			boolean temEnderecoGeracaoResiduo = (!Util.isNullOuVazio(this.getListaEnderecoGeradorResiduoModel()) && this.getListaEnderecoGeradorResiduoModel().getRowCount() > 0);
			
			boolean temEntidadeDestinataria = (!Util.isNull(getDeclaracaoTransporteDestinatarioResiduo()) && !Util.isNull(getDeclaracaoTransporteDestinatarioResiduo().getIdeDeclaracaoTransporteDestinatarioResiduo()));
			
			boolean temEnderecoDestinacaoResiduo = (!Util.isNull(getDeclaracaoTransporteDestinacaoResiduoEndereco()) && !Util.isNull(getDeclaracaoTransporteDestinacaoResiduoEndereco().getIdeDeclaracaoTransporteResiduo()));
			
			boolean temEntidadeTransportadora = (!Util.isNullOuVazio(this.getListaEntidadeTransportadoraModel()) && this.getListaEntidadeTransportadoraModel().getRowCount() > 0);
			
			if(temGeradorResiduo && temEnderecoGeracaoResiduo && temEntidadeDestinataria && temEnderecoDestinacaoResiduo && temEntidadeTransportadora) {
				this.desabilitaTab01 = Boolean.FALSE;
				this.desabilitaTab02 = Boolean.FALSE;
				this.desabilitaTab03 = Boolean.FALSE;
			}else{
				this.desabilitaTab01 = Boolean.FALSE;
				this.desabilitaTab02 = Boolean.TRUE;
				this.desabilitaTab03 = Boolean.TRUE;
			}
		}
		
		
	}

	private void carregarDadosAba2() {
		//Entidade Resíduos Transportados
		this.setListaResiduosCadastrados(new ArrayList<DeclaracaoTransporteResiduo>());

		//Entidade Disposição Final dos Resíduos
		this.listaDisposicaoFinalResiduo = new ArrayList<DisposicaoFinalResiduo>();
		
		this.residuoSelected = new Residuo();
		
		this.initListas();
	}

	private void inicializarEnderecoDestinacaoResiduo() {
		if (Util.isNull(endereco)) {
			endereco = new Endereco();
			logradouro = new Logradouro();
			logradouroPesquisa = new Logradouro();
			listaLogradouro = new ArrayList<Logradouro>();
			tipoLogradouro = new TipoLogradouro();
			bairro = new Bairro();
			listaBairro = new ArrayList<Bairro>();
			municipio = new Municipio();
			estado = new Estado();
			enableFormEndereco = false;
		}
		showFdbairro = Boolean.FALSE;
	}
	
	private void inicializarDTRP() {
		Pessoa solicitante = ContextoUtil.getContexto().getSolicitanteRequerimento();
		
		reqPessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente();
		reqPessoaSolicitante = ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante();		
		
		try {
			
			RequerimentoPessoa requerimentoPessoa = null;
			
			if(!Util.isNullOuVazio(requerimento)) {
				this.declaracaoTransporte = declaracaoTransporteService.obterDeclaracaoPorRequerimento(this.requerimento);
				
				carregarDadosTela();
				
			}else{
				if(!Util.isNull(solicitante)) {
					requerimentoPessoa = requerimentoService.buscarRequerimentoDTRPPorSolicitante(reqPessoaRequerente.getPessoa(), new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId()));
					
					if(Util.isNull(requerimentoPessoa)) {
						salvarNovoRequerimento(reqPessoaRequerente.getPessoa());
						iniciarNovaDeclaracaoTransporte(this.requerimento);
						
					}else{
						this.requerimento = requerimentoPessoa.getRequerimento();
						
						this.declaracaoTransporte = declaracaoTransporteService.obterDeclaracaoPorRequerimento(this.requerimento);
						
						if(Util.isNullOuVazio(this.declaracaoTransporte)){
							iniciarNovaDeclaracaoTransporte(this.requerimento);
						}
					}
					
					carregarDadosTela();
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void carregarDadosTela() throws Exception {
		//CARREGAR DADOS GERADOR RESIDUO
		carregarDadosGeradorResiduo();
		
		// Endereço Geração dos Resíduos
		this.declaracaoTransporteResiduoEndereco = new DeclaracaoTransporteResiduoEndereco();
		this.declaracaoTransporteResiduoEndereco.setEndereco(new Endereco());
		this.declaracaoTransporteResiduoEndereco.setDeclaracaoTransporte(getDeclaracaoTransporte());
		this.declaracaoTransporteResiduoEndereco.setIndPossuiLicencaAutorizacao(Boolean.FALSE);

		// CARREGAR ENDEREÇO DESTINAÇÃO RESÍDUO
		carregarEnderecoDestinacaoResiduo();
		
		//CARREGAR Entidade Destinatária dos Resíduos
		carregarEntidadeDestinatarioResiduo();
		
		//CARREGAR LISTA DA ENTIDADE TRANSPORTADORA
		carregarListaEntidadeTransportadora();

		//CARREGAR LISTA ENDERECO GERAÇÃO RESIDUO
		carregarListaEnderecoGeracaoResiduo();
	}

	private void carregarDadosGeradorResiduo() throws Exception {
 		if(!Util.isNull(this.declaracaoTransporte.getDeclaracaoTransporteGeradorResiduo()) && !Util.isNull(this.declaracaoTransporte.getDeclaracaoTransporteGeradorResiduo().getPessoa())) {
			this.declaracaoTransporteGeradorResiduo = this.declaracaoTransporte.getDeclaracaoTransporteGeradorResiduo();
			
			this.geradorResiduo = this.declaracaoTransporteGeradorResiduo.getPessoa();
		}
	}

	private void carregarListaEnderecoGeracaoResiduo() throws Exception {
		this.listaEnderecoGeradorResiduoModel = new LazyDataModel<DeclaracaoTransporteResiduoEndereco>() {
			
			private static final long serialVersionUID = 4183110052595601640L;

			@Override
			public List<DeclaracaoTransporteResiduoEndereco> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<DeclaracaoTransporteResiduoEndereco> lista = null;
				Exception erro = null;
				try {
						lista = (List<DeclaracaoTransporteResiduoEndereco>) declaracaoTransporteService.consultarEnderecoGeracaoResiduo(first, pageSize, getDeclaracaoTransporte());
				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					listaEnderecoGeradorResiduoModel.setRowCount(0);
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
				return lista;
			}
		};

		this.listaEnderecoGeradorResiduoModel.setRowCount(declaracaoTransporteService.consultarQuantidadeEnderecoResiduo(getDeclaracaoTransporte()));
	}

	private void carregarListaEntidadeTransportadora() throws Exception {
		if(Util.isNull(this.declaracaoTransporte.getListaEntidadeTransportadora())) {
			this.declaracaoTransporte.setListaEntidadeTransportadora(new ArrayList<DeclaracaoTransporteEntidadeTransportadora>());
		}else{
			
			this.listaEntidadeTransportadoraModel = new LazyDataModel<DeclaracaoTransporteEntidadeTransportadora>() {
				
				private static final long serialVersionUID = 7166638327422415235L;

				@Override
				public List<DeclaracaoTransporteEntidadeTransportadora> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
					List<DeclaracaoTransporteEntidadeTransportadora> lista = null;
					Exception erro = null;
					try {
							lista = (List<DeclaracaoTransporteEntidadeTransportadora>) declaracaoTransporteService.consultarEntidadeTransportadora(first, pageSize, getDeclaracaoTransporte());
					} catch (Exception e) {
						erro =e;
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
						listaEntidadeTransportadoraModel.setRowCount(0);
					}finally{
						if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
					}
					return lista;
				}
			};
			
			this.listaEntidadeTransportadoraModel.setRowCount(declaracaoTransporteService.consultarEntidadeTransportadora(getDeclaracaoTransporte()));
		}
	}

	private void carregarEntidadeDestinatarioResiduo() {
		if(!Util.isNullOuVazio(this.declaracaoTransporte.getDeclaracaoTransporteDestinatarioResiduo()) && !Util.isNullOuVazio(this.declaracaoTransporte.getDeclaracaoTransporteDestinatarioResiduo().getPessoaJuridica())) {
			this.declaracaoTransporteDestinatarioResiduo = this.declaracaoTransporte.getDeclaracaoTransporteDestinatarioResiduo();
			
			this.destinatario = new Pessoa();
			this.destinatario.setPessoaJuridica(this.declaracaoTransporteDestinatarioResiduo.getPessoaJuridica());
		}else{
			this.declaracaoTransporteDestinatarioResiduo = new DeclaracaoTransporteDestinatarioResiduo();
			this.declaracaoTransporteDestinatarioResiduo.setPessoaJuridica(new PessoaJuridica());
			
			this.destinatario = new Pessoa();
		}
	}

	private void carregarEnderecoDestinacaoResiduo() throws Exception {
		this.declaracaoTransporteDestinacaoResiduoEndereco = declaracaoTransporteService.buscarEnderecoDestinacaoResiduo(getDeclaracaoTransporte());
		
		// Endereço Geração dos Resíduos
		if(!Util.isNull(this.declaracaoTransporteDestinacaoResiduoEndereco) && !Util.isNull(this.declaracaoTransporteDestinacaoResiduoEndereco.getEndereco())) {
			this.endereco = this.declaracaoTransporteDestinacaoResiduoEndereco.getEndereco();
			
			this.endereco = enderecoFacade.carregar(this.endereco.getIdeEndereco());
			
			this.logradouroPesquisa = this.endereco.getIdeLogradouro();
			this.bairro = this.endereco.getIdeLogradouro().getIdeBairro();
			this.logradouro = this.endereco.getIdeLogradouro();
			this.tipoLogradouro = this.endereco.getIdeLogradouro().getIdeTipoLogradouro();
			
			this.estado = this.endereco.getIdeLogradouro().getIdeMunicipio().getIdeEstado();
			
			this.municipio = this.endereco.getIdeLogradouro().getIdeMunicipio();
			
			carregarEndereco();
			
			declaracaoEnderecoDestinacaoResiduoController.setEnderecoSelecionado(declaracaoTransporteDestinacaoResiduoEndereco);
			
			declaracaoEnderecoDestinacaoResiduoController.setEndereco(getEndereco());
			
			declaracaoEnderecoDestinacaoResiduoController.init();
			
		} else {
			inicializarEnderecoDestinacaoResiduo();
			
			this.declaracaoTransporteDestinacaoResiduoEndereco = new DeclaracaoTransporteResiduoEndereco();
			this.declaracaoTransporteDestinacaoResiduoEndereco.setEndereco(new Endereco());
			this.declaracaoTransporteDestinacaoResiduoEndereco.setDeclaracaoTransporte(getDeclaracaoTransporte());
			this.declaracaoTransporteDestinacaoResiduoEndereco.setIndPossuiLicencaAutorizacao(Boolean.FALSE);
			
		}
	}

	private void salvarNovoRequerimento(Pessoa pessoaRequerente)
			throws Exception {
		
		Pessoa requerente = pessoaRequerente;
		
		this.requerimento = new Requerimento();
		this.requerimento.setDtcCriacao(new Date());
		this.requerimento.setIdeTipoRequerimento(new TipoRequerimento(TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO));
		//INEMA
		this.requerimento.setIdeOrgao(new Orgao(OrgaoEnum.INEMA.getId()));
		this.requerimento.setIndExcluido(false);
		
		//dados do contato para o requerimento
		List<Telefone> listaTel = null;

		try {
			listaTel = telefoneService.buscarTelefonesPorPessoa(requerente);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		if (!Util.isNullOuVazio(requerente.getPessoaFisica()) && !Util.isNullOuVazio(requerente.getPessoaFisica().getNomPessoa()) && Util.isNullOuVazio(requerimento.getNomContato())) {
			this.requerimento.setNomContato(requerente.getPessoaFisica().getNomPessoa());

			if (!Util.isNullOuVazio(requerente.getPessoaFisica().getPessoa().getDesEmail()) && Util.isNullOuVazio(this.requerimento.getDesEmail())) {
				this.requerimento.setDesEmail(requerente.getPessoaFisica().getPessoa().getDesEmail());
			}
		} else if (!Util.isNullOuVazio(requerente.getPessoaJuridica()) && Util.isNullOuVazio(this.requerimento.getNomContato())) {

			this.requerimento.setNomContato(requerente.getPessoaJuridica().getNomRazaoSocial());

			Integer tipoSolicit = ContextoUtil.getContexto().getTipoSolicitante();
			ContextoUtil.getContexto().setTipoSolicitante(null);

			if (Util.isNullOuVazio(tipoSolicit) || tipoSolicit.equals(1)) {
				PessoaJuridica pJuridica = ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaJuridica();

				if(!Util.isNullOuVazio(pJuridica) && !Util.isNullOuVazio(pJuridica.getPessoa()) && !Util.isNullOuVazio(pJuridica.getPessoa().getDesEmail())) {
					this.requerimento.setDesEmail(pJuridica.getPessoa().getDesEmail());
				}
			} else if (tipoSolicit.equals(4) || tipoSolicit.equals(2)) {

				if (!Util.isNullOuVazio(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica())) {
					this.requerimento.setDesEmail(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica().getPessoa().getDesEmail());
				} else {
					this.requerimento.setDesEmail(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa().getDesEmail());
				}
			}
		}
		
		if (!listaTel.isEmpty() && Util.isNullOuVazio(this.requerimento.getNumTelefone())) {
			try {
				if (listaTel.size() > 1) {
					this.requerimento.setNumTelefone(PessoaUtil.getTelefoneParaRequerimento(listaTel).getNumTelefone());
				} else {
					this.requerimento.setNumTelefone(listaTel.get(0).getNumTelefone());
				}
			} catch (Exception e) {
				this.requerimento.setNumTelefone(listaTel.get(0).getNumTelefone());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		
		pessoasRequerimento();
		
		adicionarTramitacao(StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO,requerente);
		
		declaracaoTransporteService.gerarRequerimento(this.requerimento);
	}
	
	private void adicionarTramitacao(StatusRequerimentoEnum status, Pessoa requerente) {
		TramitacaoRequerimento tramitacaoRequerimento = new TramitacaoRequerimento();
		tramitacaoRequerimento.setIdePessoa(requerente);
		StatusRequerimento statusRequeri = new StatusRequerimento();
		statusRequeri.setIdeStatusRequerimento(status.getStatus());
		tramitacaoRequerimento.setIdeStatusRequerimento(statusRequeri);
		requerimento.setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		requerimento.getTramitacaoRequerimentoCollection().add(tramitacaoRequerimento);

	}
	
	private void salvarGeradorResiduo() {
		try {
			//gerador igual ao requerente
			reqPessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente();
			
			this.geradorResiduo = reqPessoaRequerente.getPessoa();
			
			if(!Util.isNullOuVazio(this.geradorResiduo)) {
				
				this.geradorResiduo = pessoaService.carregarGet(this.geradorResiduo.getIdePessoa());
				
				if(Util.isNull(this.declaracaoTransporteGeradorResiduo)) {
					this.declaracaoTransporteGeradorResiduo = new DeclaracaoTransporteGeradorResiduo();
					
					this.declaracaoTransporteGeradorResiduo.setDeclaracaoTransporte(getDeclaracaoTransporte());
					
					this.declaracaoTransporteGeradorResiduo.setPessoa(getGeradorResiduo());
					//salvaar o gerador de residuo na base, associando com a declaração de transporte.
					this.declaracaoTransporteService.salvarGeradorResiduo(getDeclaracaoTransporteGeradorResiduo());
				}
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			this.declaracaoTransporteGeradorResiduo = new DeclaracaoTransporteGeradorResiduo();
			JsfUtil.addErrorMessage("Não foi possível salvar os dados do gerador de resíduo.");
		}
	}
	
	public void selecionarGeradorResiduoPF(PessoaFisica geradorResiduoPF) {
		if(Util.isNull(this.geradorResiduo)) {
			this.geradorResiduo = new Pessoa();
		}
		
		this.geradorResiduo.setIdePessoa(geradorResiduoPF.getIdePessoaFisica());

		if(!Util.isNullOuVazio(geradorResiduoPF.getIdePessoaFisica())) {
			try {
				this.geradorResiduo = pessoaService.carregarGet(geradorResiduoPF.getIdePessoaFisica());
				
				enderecoGeradorResiduo = enderecoFacade.filtrarEnderecoByPessoa(this.geradorResiduo);
				
				if(Util.isNull(this.declaracaoTransporteGeradorResiduo)) {
					this.declaracaoTransporteGeradorResiduo = new DeclaracaoTransporteGeradorResiduo();
				}
				
				if(Util.isNull(this.declaracaoTransporteGeradorResiduo.getDeclaracaoTransporte())) {
					this.declaracaoTransporteGeradorResiduo.setDeclaracaoTransporte(getDeclaracaoTransporte());
				}
				
				this.declaracaoTransporteGeradorResiduo.setPessoa(getGeradorResiduo());
				
				//salvaar o gerador de residuo na base, associando com a declaração de transporte.
				this.declaracaoTransporteService.salvarGeradorResiduo(getDeclaracaoTransporteGeradorResiduo());
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				this.declaracaoTransporteGeradorResiduo = new DeclaracaoTransporteGeradorResiduo();
				JsfUtil.addErrorMessage("Não foi possível salvar os dados do gerador de resíduo.");
			}
		}else{
			PessoaFisica pessoaFisica = new PessoaFisica();
			pessoaFisica.setNomPessoa(geradorResiduoPF.getNomPessoa());
			this.geradorResiduo.setPessoaFisica(pessoaFisica);
		}
		
	}
	
	private void iniciarNovaDeclaracaoTransporte(Requerimento requerimento) throws Exception {
		declaracaoTransporte = new DeclaracaoTransporte();
		declaracaoTransporte.setIndAceiteResponsabilidade(Boolean.FALSE);
		
		AtoDeclaratorio atoDeclaratorio = new AtoDeclaratorio();
		atoDeclaratorio.setDtcCriacao(new Date());
		atoDeclaratorio.setIdeRequerimento(requerimento);
		atoDeclaratorio.setIndConcluido(false);
		
		atoDeclaratorioFacade.salvarAtoDeclaratorio(atoDeclaratorio);
		
		declaracaoTransporte.setAtoDeclaratorio(atoDeclaratorio);
		
		declaracaoTransporteService.salvarDeclaracaoTransporte(getDeclaracaoTransporte());
		
		salvarGeradorResiduo();
	}

	public void selecionarGeradorResiduoPJ(PessoaJuridica geradorResiduoPJ) {
		if(Util.isNull(this.geradorResiduo)) {
			this.geradorResiduo = new Pessoa();
		}
		
		this.geradorResiduo.setIdePessoa(geradorResiduoPJ.getIdePessoaJuridica());

		if(!Util.isNullOuVazio(geradorResiduoPJ.getIdePessoaJuridica())) {
			try {
				this.geradorResiduo = pessoaService.carregarGet(geradorResiduoPJ.getIdePessoaJuridica());
				
				enderecoGeradorResiduo = enderecoFacade.filtrarEnderecoByPessoa(this.geradorResiduo);
				
				if(Util.isNull(this.declaracaoTransporteGeradorResiduo)) {
					this.declaracaoTransporteGeradorResiduo = new DeclaracaoTransporteGeradorResiduo();
				}
				
				if(Util.isNull(this.declaracaoTransporteGeradorResiduo.getDeclaracaoTransporte())) {
					this.declaracaoTransporteGeradorResiduo.setDeclaracaoTransporte(getDeclaracaoTransporte());
				}
				
				this.declaracaoTransporteGeradorResiduo.setPessoa(getGeradorResiduo());
				
				//salvaar o gerador de residuo na base, associando com a declaração de transporte.
				this.declaracaoTransporteService.salvarGeradorResiduo(getDeclaracaoTransporteGeradorResiduo());
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				this.declaracaoTransporteGeradorResiduo = new DeclaracaoTransporteGeradorResiduo();
				JsfUtil.addErrorMessage("Não foi possível salvar os dados do gerador de resíduo.");
			}
		}else{
			PessoaJuridica pj = new PessoaJuridica();
			pj.setNomRazaoSocial(geradorResiduoPJ.getNomRazaoSocial());
			this.geradorResiduo.setPessoaJuridica(pj);
		}
	}
	
	public void selecionarEntidadeDestinataria(PessoaJuridica pessoaJuridica) {
		if(Util.isNullOuVazio(this.destinatario)) {
				this.destinatario = new Pessoa();
        }
		
		if(!Util.isNull(pessoaJuridica.getIdePessoaJuridica())) {
			try {
				this.destinatario = pessoaService.carregarGet(pessoaJuridica.getIdePessoaJuridica());
				
				if(Util.isNull(this.declaracaoTransporteDestinatarioResiduo)) {
					this.declaracaoTransporteDestinatarioResiduo = new DeclaracaoTransporteDestinatarioResiduo();
				}
				
				if(Util.isNull(this.declaracaoTransporteDestinatarioResiduo.getDeclaracaoTransporte())) {
					this.declaracaoTransporteDestinatarioResiduo.setDeclaracaoTransporte(getDeclaracaoTransporte());
				}
				
				this.declaracaoTransporteDestinatarioResiduo.setPessoaJuridica(this.destinatario.getPessoaJuridica());
				
				//salvaar o gerador de residuo na base, associando com a declaração de transporte.
				this.declaracaoTransporteService.salvarDestinatarioResiduo(getDeclaracaoTransporteDestinatarioResiduo());
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				this.declaracaoTransporteGeradorResiduo = new DeclaracaoTransporteGeradorResiduo();
				JsfUtil.addErrorMessage("Não foi possível salvar os dados do destinatário de resíduo.");
			}
		}else{
			this.destinatario.setIdePessoa(pessoaJuridica.getIdePessoaJuridica());
			this.destinatario.setPessoaJuridica(pessoaJuridica);
		}
	}
	
	private void initListas(){
		
		this.listaEnderecoResiduo = new ArrayList<Endereco>();
		
		this.listaResiduos = new ArrayList<Residuo>();
		
		this.listaResiduoSelected = new ArrayList<Residuo>();
		
		this.listaResiduoSelecionado = new ArrayList<DeclaracaoTransporteResiduo>();
		
		this.listaAcondicionamentos = new ArrayList<Acondicionamento>();
				
		this.listaEstadoFisico = new ArrayList<EstadoFisico>();
		
		this.listaTipoVeiculo = new ArrayList<TipoVeiculo>();
		
		this.listaEnderecoGeracaoResiduo = new ArrayList<DeclaracaoTransporteResiduoEndereco>();
		
		this.popularListas();
		
	}
	
	private void popularListas(){
	
		try {
			
			this.listaDisposicaoFinalResiduo = this.disposicaoFinalResiduoFacade.listarTodos();
			
			this.listaResiduos = residuoService.pesquisarResiduos(!Util.isNullOuVazio(nomeResiduo) ? new Residuo(nomeResiduo) : new Residuo());

			this.popularDeclaracaoTransporteResiduo();
			
			this.listaAcondicionamentos = this.acondicionamentoFacade.listarTodos();
			
			this.listaEstadoFisico = this.estadoFisicoFacade.listarTodos();
			
			this.listaTipoVeiculo = this.tipoVeiculoFacade.listarVeiculoPorResiduo();
			
			this.montarListaItensSalvoEtapa2();
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}
	
	private void montarListaItensSalvoEtapa2() {
		try {
			declaracaoTransporte = declaracaoTransporteService.carregar(getDeclaracaoTransporte().getIdeDeclaracaoTransporte());
			
			List<DeclaracaoTransporteDisposicaoFinalResiduo> lista = this.declaracaoTransporteService.consultarDeclaracaracaoDisposicaoFinalResiduo(getDeclaracaoTransporte());
			
			if(!Util.isNullOuVazio(lista)) {
				
				for(DeclaracaoTransporteDisposicaoFinalResiduo dtdfr : lista) {
					DisposicaoFinalResiduo dfr = dtdfr.getDisposicaoFinalResiduo();
					
					if(this.listaDisposicaoFinalResiduo.contains(dfr)) {
						int idx = this.listaDisposicaoFinalResiduo.indexOf(dfr);
						this.listaDisposicaoFinalResiduo.remove(idx);
						
						dfr.setChecked(true);
						this.listaDisposicaoFinalResiduo.add(idx, dfr);
					}
				}
			}
			
			List<DeclaracaoTransporteResiduo> listaDeclaracaoTransporteResiduo = this.declaracaoTransporteService.consultarDeclaracaoTransporteResiduo(getDeclaracaoTransporte());
			
			this.listaResiduoSelecionado = new ArrayList<DeclaracaoTransporteResiduo>();
			
			for (DeclaracaoTransporteResiduo dtr : listaDeclaracaoTransporteResiduo) {
				this.listaResiduoSelecionado.add(dtr);
				
				for(DeclaracaoTransporteResiduo res : this.listaResiduosCadastrados) {
					if(res.getResiduo().equals(dtr.getResiduo())) {
						this.listaResiduosCadastrados.remove(res);
						break;
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void popularDeclaracaoTransporteResiduo(){
		
		this.setListaResiduosCadastrados(new ArrayList<DeclaracaoTransporteResiduo>());
		
		if(!Util.isNullOuVazio(this.listaResiduos)){
			
			for(Residuo r: listaResiduos){
				DeclaracaoTransporteResiduo dtr = new DeclaracaoTransporteResiduo();
				dtr.setAcondicionamento(new Acondicionamento());
				dtr.setEstadoFisico(new EstadoFisico());
				dtr.setTipoVeiculo(new TipoVeiculo());
				dtr.setResiduo(r);
				dtr.setQtdTransportada(BigDecimal.ZERO);
				dtr.setDeclaracaoTransporte(getDeclaracaoTransporte());
				this.getListaResiduosCadastrados().add(dtr);
			}
		}
	}
	
	public void pesquisarResiduos() {
		try {
			this.listaResiduos = residuoService.pesquisarResiduos(!Util.isNullOuVazio(nomeResiduo) ? new Residuo(nomeResiduo) : new Residuo());
			if(Util.isNullOuVazio(this.listaResiduos)){
				this.listaResiduos = new ArrayList<Residuo>();
				this.listaResiduos.add(residuoService.obterResiduoByIde(LacTransporteOutroEnum.RESIDUO_OUTROS.getId()));
				JsfUtil.addWarnMessage(BUNDLE.getString("lac_dadosGerais_info008_residuo"));
			}

			if(!Util.isNullOuVazio(this.residuoSelected)){
				this.listaResiduoSelected.add(this.residuoSelected);
			}
			
			if(!Util.isNullOuVazio(this.listaResiduoSelected)){
				this.listaResiduos.removeAll(this.listaResiduoSelected);
			}
			
			this.popularDeclaracaoTransporteResiduo();
			
		}
		catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}
	
	public void removerOsSelecionadosDaListaPrincipal(List<DeclaracaoTransporteResiduo> list){
		if(!Util.isNullOuVazio(list)){
			this.getListaResiduosCadastrados().removeAll(this.listaResiduoSelecionado);
		}
	}
	
	public void adicionarResiduoLista(){
		
		if(Util.isNullOuVazio(this.listaResiduoSelecionado)){
			this.listaResiduoSelecionado = new ArrayList<DeclaracaoTransporteResiduo>();
		}
		
		if(!this.listaResiduoSelecionado.contains(this.residuoSelecionado)){
			this.listaResiduoSelecionado.add(this.residuoSelecionado);
			if(isOutro(this.residuoSelecionado)){
				this.residuoSelecionado.setOutro(true);
				this.residuoSelecionado.setQtdTransportada((new BigDecimal(1)));
				JsfUtil.addWarnMessage(BUNDLE.getString("lac_dadosGerais_info005"));
				JsfUtil.addWarnMessage(BUNDLE.getString("lac_dadosGerais_info0056"));
			}
			this.residuoSelected = this.residuoSelecionado.getResiduo();
			this.getListaResiduosCadastrados().remove(this.residuoSelecionado);
			this.residuoSelecionado = new DeclaracaoTransporteResiduo();
			this.nomeResiduo = null;
			this.pesquisarResiduos();
			
		}else {
		
			JsfUtil.addErrorMessage("O resíduo " + BUNDLE.getString("lac_dadosGerais_mensagens_alerta_escolhido"));
			
		}
	}
	
	public void removerResiduoLista(){
		try {
			this.listaResiduoSelecionado.remove(residuoSelecionado);
			
			this.declaracaoTransporteService.removerResiduo(residuoSelecionado);
			
			this.residuoSelecionado = null;
			
			JsfUtil.addSuccessMessage("Resíduo removido com sucesso.");
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível remover o resíduo.");
		}
	}
	
	public void validaCheckOutros(DisposicaoFinalResiduo dfr){
		if(dfr.isOutros() && dfr.isChecked()){
			MensagemUtil.exibirInformacao001();
		}
		
		try {
			if(!dfr.isChecked()) {
				
				dfr = disposicaoFinalResiduoService.carregar(dfr.getIdeDisposicaoFinalResiduo());
				
				int idx = -1;
				
				declaracaoTransporte = declaracaoTransporteService.carregar(getDeclaracaoTransporte().getIdeDeclaracaoTransporte());
				
				DeclaracaoTransporteDisposicaoFinalResiduo disposicaoRemover = null;
				
				for(DeclaracaoTransporteDisposicaoFinalResiduo dtdfr : getDeclaracaoTransporte().getListaDeclaracaoTransporteDisposicaoFinalResiduo()) {
					if(dtdfr.getDisposicaoFinalResiduo().equals(dfr)) {
						idx = getDeclaracaoTransporte().getListaDeclaracaoTransporteDisposicaoFinalResiduo().indexOf(dtdfr);
						
						disposicaoRemover = dtdfr;
						
						break;
					}
				}
				
				if(idx >= 0 && disposicaoRemover != null) {
					declaracaoTransporteService.removerDisposicaoFinalResiduo(disposicaoRemover);
					
					declaracaoTransporte = declaracaoTransporteService.carregar(getDeclaracaoTransporte().getIdeDeclaracaoTransporte());
					//getDeclaracaoTransporte().getListaDeclaracaoTransporteDisposicaoFinalResiduo().remove(idx);
					
					//declaracaoTransporteService.salvarDeclaracaoTransporte(getDeclaracaoTransporte());
					
					
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível desassociar Disposição Final Resíduo.");
		}finally{
			Html.atualizar("tabViewDTRP:form_Etapa2");
		}
	}
	
	public boolean isOutro(Object obj){
		if (obj instanceof Residuo){
			if(((Residuo) obj).getIdeResiduo().equals(LacTransporteOutroEnum.RESIDUO_OUTROS.getId())){
				return true;
			}
		}
		return false;
	}
	
	public void avancar(){
		if(permiteEditar) {
			this.salvar();
		}
		
		if(activeIndex == ABA_ETAPA_1) {
			if(validarAbaEtapa1()) {
				carregarDadosAba2();
				this.habilitarAbas();
				activeIndex = ABA_ETAPA_2;
				Html.atualizar("tabViewDTRP");
			}else{
				JsfUtil.addWarnMessage("Só é permitido alternar de abas assim que todos os dados da Etapa 1 forem preenchidos.");
				Html.atualizar("tabViewDTRP");
			}
		}else if(activeIndex == ABA_ETAPA_2) {
			if(validarAbaEtapa2()) {
				this.habilitarAbas();
				this.desabilitaTab01 = Boolean.FALSE;
				this.desabilitaTab02 = Boolean.FALSE;
				this.desabilitaTab03 = Boolean.FALSE;
				this.activeIndex = ABA_ETAPA_3;
				atualizarTabs();
			}/*else{
				JsfUtil.addErrorMessage("Etapa 2 não concluída. Favor verificar.");
				Html.atualizar("tabViewDTRP");
			}*/
		}else{
			carregarDadosAba1();
			carregarDadosAba2();
			activeIndex = ABA_ETAPA_3;
			Html.atualizar("tabViewDTRP");
			Html.atualizar("tabViewDTRP:tab03");
			Html.atualizar("tabViewDTRP:tab03:formEtapa3");
		}
	}
	
	public void finalizar() {
		try {
			salvar();
			
			if(validarAbas()) {
				this.requerimento = requerimentoService.carregarDadosBasicos(getRequerimento().getIdeRequerimento());
				
				if(!Util.isNull(reqPessoaSolicitante.getIdeTipoPessoaRequerimento()) && reqPessoaSolicitante.isIndSolicitante()) {
					finalizarRequerimentoService.finalizarRequerimentoDTRP(getDeclaracaoTransporte(), getRequerimento(), reqPessoaSolicitante);
				}else{
					finalizarRequerimentoService.finalizarRequerimentoDTRP(getDeclaracaoTransporte(), getRequerimento(), reqPessoaRequerente);
				}
			}else{
				if(!disposicaoFinalResiduoOutros) {
					JsfUtil.addWarnMessage("Preencha todos os campos necessários antes de finalizar.");
				}
				Html.atualizar("tabViewDTRP:formDeclaracao");
				Html.atualizar("tabViewDTRP");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao Finalizar a Declaração.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void pessoasRequerimento() throws Exception {
		try {

			List<RequerimentoPessoa> pessoasRequerimentos = new ArrayList<RequerimentoPessoa>();
			RequerimentoPessoa reqPessoaAtendente = ContextoUtil.getContexto().getReqPapeisDTO().getAtendente();
			RequerimentoPessoa reqPessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente();
			RequerimentoPessoa reqPessoaSolicitante = ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante();

			pessoasRequerimentos.add(reqPessoaRequerente);

			if (!reqPessoaRequerente.isIndSolicitante()) {
				pessoasRequerimentos.add(reqPessoaSolicitante);
			}

			if (!reqPessoaRequerente.isIndUsuarioLogado() && !reqPessoaSolicitante.isIndUsuarioLogado()) {
				pessoasRequerimentos.add(reqPessoaAtendente);
			}

			
			if(!Util.isNull(reqPessoaRequerente.getPessoa().getPessoaFisica()) && !Util.isNull(reqPessoaRequerente.getPessoa().getPessoaFisica().getIdePessoaFisica())) {
				
				ProcuradorPessoaFisica donoProcurador = new ProcuradorPessoaFisica();
				donoProcurador.setIdePessoaFisica(reqPessoaRequerente.getPessoa().getPessoaFisica());
			
				List<ProcuradorPessoaFisica> listaProcuradorPessoaFisica = (List<ProcuradorPessoaFisica>) procuradorPessoaFisicaService.listarProcuradorPessoaFisica(donoProcurador);
				for (ProcuradorPessoaFisica procuradorPessoaFisica : listaProcuradorPessoaFisica) {
					RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
					requerimentoPessoa.setPessoa(procuradorPessoaFisica.getIdeProcurador().getPessoa());
					TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
					tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
					requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);
					pessoasRequerimentos.add(requerimentoPessoa);
				}
			}
			
			ProcuradorRepresentante procuradoRepresentante = new ProcuradorRepresentante();
			procuradoRepresentante.setIdePessoaJuridica(new PessoaJuridica());
			procuradoRepresentante.getIdePessoaJuridica().setIdePessoaJuridica(reqPessoaRequerente.getPessoa().getIdePessoa());
			
			List<ProcuradorRepresentante> listaProcuradorRepresentante = (List<ProcuradorRepresentante>) procuradorRepresentanteService.getListaProcuradorRepresentante(procuradoRepresentante);
			for (ProcuradorRepresentante procuradorRepresentante : listaProcuradorRepresentante) {
				RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
				requerimentoPessoa.setPessoa(procuradorRepresentante.getIdeProcurador().getPessoa());
				TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
				tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
				requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);

				boolean temPessoaRequerimento = false;

				for (RequerimentoPessoa reqPes : pessoasRequerimentos) {
					if (reqPes.getIdeTipoPessoaRequerimento().equals(requerimentoPessoa.getIdeTipoPessoaRequerimento())
							&& reqPes.getPessoa().equals(requerimentoPessoa.getPessoa())) {
						temPessoaRequerimento = true;
						break;
					}
				}

				if (!temPessoaRequerimento) {
					pessoasRequerimentos.add(requerimentoPessoa);
				}
			}

			PessoaJuridica pessoaJuridica = new PessoaJuridica(reqPessoaRequerente.getPessoa().getIdePessoa());
			
			List<RepresentanteLegal> collRepresentanteLegal = representanteLegalService.listarRepresentanteLegalPorPessoaJuridica(pessoaJuridica);
			
			for (RepresentanteLegal representantelegal : collRepresentanteLegal) {

				RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
				requerimentoPessoa.setPessoa(representantelegal.getIdePessoaFisica().getPessoa());
				TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
				tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId());
				requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);

				boolean temPessoaRequerimento = false;

				for (RequerimentoPessoa reqPes : pessoasRequerimentos) {
					if (reqPes.getIdeTipoPessoaRequerimento().equals(requerimentoPessoa.getIdeTipoPessoaRequerimento())
							&& reqPes.getPessoa().equals(requerimentoPessoa.getPessoa())) {

						temPessoaRequerimento = true;
						break;
					}
				}

				if (!temPessoaRequerimento) {
					pessoasRequerimentos.add(requerimentoPessoa);
				}
			}

			for (RequerimentoPessoa reqPess : pessoasRequerimentos) {
				reqPess.setRequerimento(requerimento);
			}

			requerimento.setRequerimentoPessoaCollection(pessoasRequerimentos);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao associar a pessoa ao requerimento.");
			throw e;
		}
	}
	
	public boolean isRequerentePF(){
		return (!Util.isNullOuVazio(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa().getPessoaFisica()));
	}

	public boolean isRequerentePJ(){
		return (!Util.isNull(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa().getPessoaJuridica()));
	}
	
	private boolean validarAbaEtapa1() {
		boolean abaEtapa1 = false;
		
		boolean validacaoSucesso = false;
		
		boolean temGeradorResiduo = (!Util.isNull(this.getDeclaracaoTransporteGeradorResiduo()) && !Util.isNull(this.getDeclaracaoTransporteGeradorResiduo().getIdeDeclaracaoTransporteGeradorResiduo()));
		
		boolean temEnderecoGeracaoResiduo = (!Util.isNullOuVazio(this.getListaEnderecoGeradorResiduoModel()) && this.getListaEnderecoGeradorResiduoModel().getRowCount() > 0);
		
		boolean temEntidadeDestinataria = (!Util.isNull(this.getDeclaracaoTransporteDestinatarioResiduo()) && !Util.isNull(this.getDeclaracaoTransporteDestinatarioResiduo().getIdeDeclaracaoTransporteDestinatarioResiduo()));
		
		boolean temEnderecoDestinacaoResiduo = (!Util.isNull(this.getDeclaracaoTransporteDestinacaoResiduoEndereco()) && !Util.isNull(this.getDeclaracaoTransporteDestinacaoResiduoEndereco().getIdeDeclaracaoTransporteResiduo()));
		
		boolean temEntidadeTransportadora = (!Util.isNullOuVazio(this.getListaEntidadeTransportadoraModel()) && this.getListaEntidadeTransportadoraModel().getRowCount() > 0);
		
		if(temGeradorResiduo && temEnderecoDestinacaoResiduo && temEnderecoGeracaoResiduo && temEntidadeTransportadora && temEntidadeDestinataria) {
			abaEtapa1 = true;
		}
		
		if(abaEtapa1){
			validacaoSucesso = true;
		}
		
		return validacaoSucesso;
	}
	
	private boolean validarAbaEtapa2() {
		boolean abaEtapa2 = false;
		boolean validacaoSucesso = false;
		
		boolean temResiduoSelecionado = verificarResiduoSelecionado();
		boolean temResiduoPreenchido = isTemResiduoSelecionado();
		boolean valorPreenchidoResiduo = isResiduoPreenchido();
		
		if(temResiduoSelecionado && temResiduoPreenchido && valorPreenchidoResiduo) {
			abaEtapa2 = true;
		}
		
		if(abaEtapa2){
			validacaoSucesso = true;
		}
		
		return validacaoSucesso;
	}
	
	private boolean validarAbas() {
		boolean abaEtapa1 = false;
		boolean abaEtapa2 = false;
		boolean abaEtapa3 = false;
		
		boolean validacaoSucesso = false;
		
		boolean indAceite = this.declaracaoTransporte.getIndAceiteResponsabilidade(); 
		
		this.declaracaoTransporte = this.declaracaoTransporteService.carregar(this.declaracaoTransporte.getIdeDeclaracaoTransporte());
		
		this.declaracaoTransporte.setIndAceiteResponsabilidade(indAceite);
		
		boolean temGeradorResiduo = (!Util.isNull(getDeclaracaoTransporte().getDeclaracaoTransporteGeradorResiduo()) && !Util.isNull(getDeclaracaoTransporte().getDeclaracaoTransporteGeradorResiduo().getIdeDeclaracaoTransporteGeradorResiduo()));
		
		boolean temEnderecoGeracaoResiduo = (!Util.isNullOuVazio(this.getListaEnderecoGeradorResiduoModel()) && this.getListaEnderecoGeradorResiduoModel().getRowCount() > 0);
		
		boolean temEntidadeDestinataria = (!Util.isNull(this.getDeclaracaoTransporte().getDeclaracaoTransporteDestinatarioResiduo()) && !Util.isNull(this.getDeclaracaoTransporte().getDeclaracaoTransporteDestinatarioResiduo().getIdeDeclaracaoTransporteDestinatarioResiduo()));
		
		boolean temEnderecoDestinacaoResiduo = (!Util.isNull(getDeclaracaoTransporteDestinacaoResiduoEndereco()) && !Util.isNull(getDeclaracaoTransporteDestinacaoResiduoEndereco().getIdeDeclaracaoTransporteResiduo()));
		
		boolean temEntidadeTransportadora = (!Util.isNullOuVazio(this.getListaEntidadeTransportadoraModel()) && this.getListaEntidadeTransportadoraModel().getRowCount() > 0);
		
		if(temGeradorResiduo && temEnderecoDestinacaoResiduo && temEnderecoGeracaoResiduo && temEntidadeTransportadora && temEntidadeDestinataria) {
			abaEtapa1 = true;
		}
		
		boolean temResiduoSelecionado = verificarResiduoSelecionado();
		boolean temResiduoPreenchido = isTemResiduoSelecionado();
		boolean valorPreenchidoResiduo = isResiduoPreenchido();
		
		
		if(!Util.isNullOuVazio(this.listaDisposicaoFinalResiduo)) {
			for(DisposicaoFinalResiduo disp : this.listaDisposicaoFinalResiduo) {
				if(disp.isChecked() && disp.isOutros()) {
					disposicaoFinalResiduoOutros = true;
					break;
				}
			}
		}
		
		if(temResiduoSelecionado && temResiduoPreenchido && valorPreenchidoResiduo) {
			abaEtapa2 = true;
		}else{
			JsfUtil.addErrorMessage("Etapa 2 não concluída. Favor verificar.");
			abaEtapa2 = false;
		}
		
		if(getDeclaracaoTransporte().getIndAceiteResponsabilidade()) {
			abaEtapa3 = true;
		}
		
		if(abaEtapa1 && abaEtapa2 && abaEtapa3) {
			validacaoSucesso = true;
		}
		
		if(disposicaoFinalResiduoOutros) {
			MensagemUtil.exibirInformacao002();			
			validacaoSucesso = false;
		}
		
		return validacaoSucesso;
	}

	private boolean isResiduoPreenchido() {
		boolean valido = true;
		
		for (DeclaracaoTransporteResiduo declaracaoTransporteResiduo : listaResiduoSelecionado) {
			if(Util.isNullOuVazio(declaracaoTransporteResiduo.getAcondicionamento().getIdeAcondicionamento())) {
				valido = false;
			}
			
			if(Util.isNullOuVazio(declaracaoTransporteResiduo.getEstadoFisico().getIdeEstadoFisico())) {
				valido = false;
			}
			
			if(Util.isNullOuVazio(declaracaoTransporteResiduo.getQtdTransportada())) {
				valido = false;
			}
			
			if(Util.isNullOuVazio(declaracaoTransporteResiduo.getTipoVeiculo().getIdeTipoVeiculo())) {
				valido = false;
			}
		}
		
		return valido;
	}
	
	private boolean verificarResiduoSelecionado() {
		boolean retorno = false;
		
		if(!Util.isNullOuVazio(listaDisposicaoFinalResiduo)) {
			for(DisposicaoFinalResiduo dfr : listaDisposicaoFinalResiduo) {
				if(dfr.isChecked()) {
					retorno = true;
					break;
				}
			}
		}
		
		return retorno;
	}
	
	private void atualizarTabs() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("tabViewDTRP");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("tabViewDTRP:tab01");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("tabViewDTRP:tab02");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("tabViewDTRP:tab03");
	}
	
	public void voltar(){
		
		switch (this.activeIndex) {
		case 1:
			this.desabilitaTab01 = Boolean.FALSE;
			this.desabilitaTab02 = Boolean.FALSE;
			this.desabilitaTab03 = Boolean.FALSE;
			this.activeIndex = 0;
			atualizarTabs();
			break;

		case 2:
			this.desabilitaTab01 = Boolean.FALSE;
			this.desabilitaTab02 = Boolean.FALSE;
			this.desabilitaTab03 = Boolean.FALSE;
			this.activeIndex = 1;
			atualizarTabs();
			break;
		
		}
		
	}
	public boolean isTemResiduoSelecionado(){
		return ((!Util.isNullOuVazio(this.listaResiduoSelecionado) && !this.listaResiduoSelecionado.isEmpty()));
	}
	
	public void adicionarEnderecoDestinacaoResiduo() {
		Endereco endereco = this.getEndereco();
		
		if(!Util.isNull(endereco)) {
			this.declaracaoTransporteResiduoEndereco.setEndereco(endereco);
		}
	}
	
	public void salvar() {
		try {
			if(activeIndex == ABA_ETAPA_1) {
				boolean valido = salvarEnderecoDestinacaoResiduo();
				
				if(valido) {
					this.declaracaoTransporteService.salvarDeclaracaoTransporte(getDeclaracaoTransporte());
					
					JsfUtil.addSuccessMessage(Util.getString("dtrp_mensagem_salvo_sucesso"));
					
					habilitarAbas();
				}
			}
			if(activeIndex == ABA_ETAPA_2) {
				
				if(validarResiduoSelecionado()) {
					if(!Util.isNullOuVazio(this.getListaResiduoSelecionado())) {
						this.getDeclaracaoTransporte().setListaResiduosCadastrados(this.getListaResiduoSelecionado());
					}
					
					if(!Util.isNullOuVazio(this.getListaDisposicaoFinalResiduo())) {
						desassociarDisposicaoFinalResiduo();
						
						associarNovaDisposicaoFinalResiduo();
					}
					
					this.declaracaoTransporteService.salvarDeclaracaoTransporte(getDeclaracaoTransporte());
					
					JsfUtil.addSuccessMessage(Util.getString("dtrp_mensagem_salvo_sucesso"));
					
					habilitarAbas();
					
					if(desabilitaTab02) {
						carregarDadosAba2();
					}
				}else{
					JsfUtil.addErrorMessage("Um ou mais resíduos estão com os dados incompletos.");
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addSuccessMessage(Util.getString("dtrp_mensagem_salvar_erro"));
		} finally {
			Html.atualizar("tabViewDTRP:formDeclaracao");
			Html.atualizar("tabViewDTRP");
		}
	}

	private void associarNovaDisposicaoFinalResiduo() throws Exception {
		for(DisposicaoFinalResiduo dfr : this.getListaDisposicaoFinalResiduo()) {
			if(dfr.isChecked()) {
				this.declaracaoTransporteDisposicaoFinalResiduo = new DeclaracaoTransporteDisposicaoFinalResiduo();
				this.declaracaoTransporteDisposicaoFinalResiduo.setDeclaracaoTransporte(getDeclaracaoTransporte());
				
				dfr = disposicaoFinalResiduoService.carregar(dfr.getIdeDisposicaoFinalResiduo());
				
				this.declaracaoTransporteDisposicaoFinalResiduo.setDisposicaoFinalResiduo(dfr);
				
				this.declaracaoTransporteDisposicaoFinalResiduo = this.declaracaoTransporteService.consultarDeclaracaoTransporteDisposicaoFinalResiduo(this.declaracaoTransporteDisposicaoFinalResiduo);
				
				if(Util.isNull(this.declaracaoTransporteDisposicaoFinalResiduo)) {
					this.declaracaoTransporteDisposicaoFinalResiduo = new DeclaracaoTransporteDisposicaoFinalResiduo();
					this.declaracaoTransporteDisposicaoFinalResiduo.setDeclaracaoTransporte(getDeclaracaoTransporte());
					
					dfr = disposicaoFinalResiduoService.carregar(dfr.getIdeDisposicaoFinalResiduo());
					
					this.declaracaoTransporteDisposicaoFinalResiduo.setDisposicaoFinalResiduo(dfr);
				}
				
				this.declaracaoTransporteService.salvarDeclaracaoTransporteDisposicaoFinalResiduo(this.declaracaoTransporteDisposicaoFinalResiduo);
			}
		}
	}

	private void desassociarDisposicaoFinalResiduo() throws Exception {
		List<DeclaracaoTransporteDisposicaoFinalResiduo> lista = this.declaracaoTransporteService.consultarDeclaracaracaoDisposicaoFinalResiduo(getDeclaracaoTransporte());
		
		if(!Util.isNullOuVazio(lista)) {
			for(DeclaracaoTransporteDisposicaoFinalResiduo dt : lista) {
				for(DisposicaoFinalResiduo df : this.listaDisposicaoFinalResiduo) {
					if(df.equals(dt.getDisposicaoFinalResiduo()) && !df.isChecked()) {
						declaracaoTransporteService.removerDisposicaoFinalResiduo(dt);
					}
				}
			}
		}
		
		this.declaracaoTransporte = declaracaoTransporteService.carregar(getDeclaracaoTransporte().getIdeDeclaracaoTransporte());
	}
	
	public void salvarEnderecoGeracaoResiduo() {
		try {
			DeclaracaoTransporteResiduoEndereco enderecoExistente = this.declaracaoEnderecoGeradorResiduoController.getEnderecoSelecionado();
			
			Endereco enderecoResiduo = this.declaracaoEnderecoGeradorResiduoController.getEndereco();
			Bairro bairroResiduo = this.declaracaoEnderecoGeradorResiduoController.getBairro();
			Logradouro logradouroResiduo = this.declaracaoEnderecoGeradorResiduoController.getLogradouro();
			TipoLogradouro tipoLogradouro = this.declaracaoEnderecoGeradorResiduoController.getTipoLogradouro();
			Municipio municipioResiduo = this.declaracaoEnderecoGeradorResiduoController.getMunicipio();
			Integer numCep = this.declaracaoEnderecoGeradorResiduoController.getLogradouroPesquisa().getNumCep();
			
			if(validarEndereco(enderecoResiduo, numCep, bairroResiduo, logradouroResiduo, tipoLogradouro, municipioResiduo, enderecoExistente)) {
				if(!Util.isNullOuVazio(bairroResiduo.getIdeBairro()) && bairroResiduo.getIdeBairro() != -1) {
					bairroResiduo = bairroService.filtrarBairroById(bairroResiduo);
				}else{
					persistirBairro(bairroResiduo, municipioResiduo);
				}
				
				if(!Util.isNull(logradouroResiduo.getIdeLogradouro()) && logradouroResiduo.getIdeLogradouro() != -1) {
					logradouroResiduo = logradouroService.getLogradouroById(logradouroResiduo.getIdeLogradouro());
				}else{
					persistirLogradouro(logradouroResiduo, municipioResiduo, tipoLogradouro, bairroResiduo, numCep);
				}
				
				enderecoFacade.salvarEnderecoCompletoDTRP(enderecoResiduo, bairroResiduo, logradouroResiduo);
				
				enderecoResiduo = this.enderecoFacade.carregar(enderecoResiduo.getIdeEndereco());
				
				if(Util.isNull(enderecoExistente.getIdeDeclaracaoTransporteResiduo())) {
					DeclaracaoTransporteResiduoEndereco novoEnderecoResiduo = new DeclaracaoTransporteResiduoEndereco();
					novoEnderecoResiduo.setEndereco(enderecoResiduo);
					novoEnderecoResiduo.setTipoEndereco(new TipoEndereco(TipoEnderecoEnum.GERACAO_RESIDUO.getId()));
					novoEnderecoResiduo.setDeclaracaoTransporte(getDeclaracaoTransporte());
					novoEnderecoResiduo.setIndPossuiLicencaAutorizacao(enderecoExistente.getIndPossuiLicencaAutorizacao());
					novoEnderecoResiduo.setNumProcessoLicencaAutorizacao(enderecoExistente.getNumProcessoLicencaAutorizacao());
					
					this.declaracaoTransporteService.salvarEnderecoDestinatarioResiduo(novoEnderecoResiduo);
					
					novoEnderecoResiduo = new DeclaracaoTransporteResiduoEndereco();
				}else{
					this.declaracaoTransporteService.salvarEnderecoDestinatarioResiduo(enderecoExistente);
				}
				
				JsfUtil.addSuccessMessage("Salvo com sucesso.");
				
				carregarListaEnderecoGeracaoResiduo();
				
				Html.atualizar("tabViewDTRP:formDeclaracao:dtEnderecoResiduo");
				Html.esconder("dialogselecionarEnderecoResiduo");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível salvar o endereço!!");
		}finally{
			Html.atualizar("tabViewDTRP:formDeclaracao");
			Html.atualizar("tabViewDTRP");
		}
	}
	
	private boolean validarEndereco(Endereco endereco, Integer numCep,Bairro bairroResiduo, Logradouro logradouroResiduo,
			TipoLogradouro tipoLogradouro2, Municipio municipioResiduo, DeclaracaoTransporteResiduoEndereco enderecoExistente) {
		
		boolean sucesso = true;
		
		if(Util.isNullOuVazio(numCep)) {
			JsfUtil.addErrorMessage("O campo CEP é de preenchimento obrigatório.");
			sucesso = false;
		}
		
		if(Util.isNull(bairroResiduo)) {
			JsfUtil.addErrorMessage("O campo Bairro é de preenchimento obrigatório.");
			sucesso = false;
		}else{
			if(Util.isNull(bairroResiduo.getIdeBairro())) {
				JsfUtil.addErrorMessage("O campo Bairro é de preenchimento obrigatório.");
				sucesso = false;
			}
			
			if(!Util.isNull(bairroResiduo.getIdeBairro()) && Integer.valueOf(-1).equals(bairroResiduo.getIdeBairro())) {
				if(Util.isNullOuVazio(bairroResiduo.getNomBairro())) {
					JsfUtil.addErrorMessage("O campo Bairro é de preenchimento obrigatório.");
					sucesso = false;
				}
			}
		}
		
		if(Util.isNull(logradouroResiduo) || Util.isNull(tipoLogradouro2)) {
			JsfUtil.addErrorMessage("O campo Logradouro é de preenchimento obrigatório.");
			sucesso = false;
		}else{
			if((Util.isNullOuVazio(tipoLogradouro2.getIdeTipoLogradouro())) || 
					!Util.isNull(logradouroResiduo.getIdeLogradouro()) && 
					!Integer.valueOf(-1).equals(logradouroResiduo.getIdeLogradouro()) && 
					Util.isNullOuVazio(logradouroResiduo.getIdeLogradouro())) {
				JsfUtil.addErrorMessage("O campo Logradouro é de preenchimento obrigatório.");
				sucesso = false;
			}else{
				if(Integer.valueOf(-1).equals(tipoLogradouro2.getIdeTipoLogradouro())) {
					if(Util.isNullOuVazio(logradouroResiduo.getNomLogradouro())) {
						JsfUtil.addErrorMessage("O campo Logradouro é de preenchimento obrigatório.");
						sucesso = false;
					}
				}
			}
		}
		
		if(Util.isNullOuVazio(endereco.getNumEndereco())) {
			JsfUtil.addErrorMessage("O campo Número é de preenchimento obrigatório.");
			sucesso = false;
		}
		
		if(!Util.isNull(municipioResiduo.getIdeEstado()) && Util.isNullOuVazio(municipioResiduo.getIdeEstado().getIdeEstado())) {
			JsfUtil.addErrorMessage("O campo UF é de preenchimento obrigatório.");
			sucesso = false;
		}
		
		if(!Util.isNull(municipioResiduo) && Util.isNullOuVazio(municipioResiduo.getIdeMunicipio())) {
			JsfUtil.addErrorMessage("O campo Localidade é de preenchimento obrigatório.");
			sucesso = false;
		}
		
		if(enderecoExistente.getIndPossuiLicencaAutorizacao() && Util.isNullOuVazio(enderecoExistente.getNumProcessoLicencaAutorizacao())) {
			JsfUtil.addErrorMessage("O campo Nº do processo da licença/autorização é de preenchimento obrigatório.");
			sucesso = false;
		}
		
		
		return sucesso;
	}
	
	public boolean salvarEnderecoDestinacaoResiduo() {
		boolean validacao = true;
		
		try {
			
			DeclaracaoTransporteResiduoEndereco enderecoExistente = this.declaracaoEnderecoDestinacaoResiduoController.getEnderecoSelecionado();
			
			boolean temNumeroLicenca = Util.isNullOuVazio(enderecoExistente.getNumProcessoLicencaAutorizacao()); 
			
			if(Util.isNullOuVazio(this.declaracaoTransporteDestinacaoResiduoEndereco)) {
				this.declaracaoTransporteDestinacaoResiduoEndereco = new DeclaracaoTransporteResiduoEndereco();
			}
			
			Endereco enderecoResiduo = this.declaracaoEnderecoDestinacaoResiduoController.getEndereco();
			Bairro bairroResiduo = this.declaracaoEnderecoDestinacaoResiduoController.getBairro();
			Logradouro logradouroResiduo = this.declaracaoEnderecoDestinacaoResiduoController.getLogradouro();
			TipoLogradouro tipoLogradouro = this.declaracaoEnderecoDestinacaoResiduoController.getTipoLogradouro();
			Municipio municipioResiduo = this.declaracaoEnderecoDestinacaoResiduoController.getMunicipio();
			Integer numCep = this.declaracaoEnderecoDestinacaoResiduoController.getLogradouroPesquisa().getNumCep();
			
			boolean enderecoValido = validarEndereco(enderecoResiduo, numCep, bairroResiduo, logradouroResiduo, tipoLogradouro, municipioResiduo, enderecoExistente);
			
			if(enderecoValido && !temNumeroLicenca) {
				Endereco endereco = new Endereco();
				
				if(!Util.isNullOuVazio(bairroResiduo.getIdeBairro()) && (bairroResiduo.getIdeBairro() != 0 && bairroResiduo.getIdeBairro() != -1)) {
					bairroResiduo = bairroService.filtrarBairroById(bairroResiduo);
				}else{
					persistirBairro(bairroResiduo, municipioResiduo);
				}
				
				if(!Util.isNull(logradouroResiduo.getIdeLogradouro()) && (logradouroResiduo.getIdeLogradouro() != 0 && logradouroResiduo.getIdeLogradouro() != -1)) {
					logradouroResiduo = logradouroService.getLogradouroById(logradouroResiduo.getIdeLogradouro());
				}else{
					persistirLogradouro(logradouroResiduo, municipioResiduo, tipoLogradouro, bairroResiduo, numCep);
				}
				
				endereco.setDesComplemento(enderecoResiduo.getDesComplemento());
				endereco.setDtcCriacao(new Date());
				endereco.setIdeLogradouro(logradouroResiduo);
				endereco.setNumEndereco(enderecoResiduo.getNumEndereco());
				
				enderecoFacade.salvarEnderecoCompletoDTRP(endereco, bairroResiduo, logradouroResiduo);
				
				enderecoResiduo = this.enderecoFacade.carregar(endereco.getIdeEndereco());
				
				if(Util.isNull(enderecoExistente.getIdeDeclaracaoTransporteResiduo())) {
					enderecoExistente.setTipoEndereco(new TipoEndereco(TipoEnderecoEnum.DESTINACAO_RESIDUO.getId()));
					enderecoExistente.setDeclaracaoTransporte(getDeclaracaoTransporte());
					enderecoExistente.setEndereco(endereco);
					
					this.declaracaoTransporteService.salvarEnderecoDestinatarioResiduo(enderecoExistente);
					
				}else{
					enderecoExistente.setEndereco(endereco);
					
					this.declaracaoTransporteService.salvarEnderecoDestinatarioResiduo(enderecoExistente);
				}
				if(!Util.isNotNullAndTrue(enderecoExistente.getEndereco().getIdeLogradouro().getIndOrigemCorreio()) && !Util.isNotNullAndTrue(enderecoExistente.getEndereco().getIdeLogradouro().getIndOrigemApi())){
					enderecoExistente.getEndereco().getIdeLogradouro().setIdeLogradouro(-1);
				}
				declaracaoTransporteDestinacaoResiduoEndereco = enderecoExistente;
				
				Html.atualizar("tabViewDTRP:formDeclaracao:dtEnderecoResiduo");

			}else{
				validacao = false;
				JsfUtil.addErrorMessage("Nº do processo da licença/autorização é de preenchimento obrigatório.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível salvar o endereço!!");
		}finally{
			Html.atualizar("tabViewDTRP:formDeclaracao");
			Html.atualizar("tabViewDTRP");
		}
		
		return validacao;
	}
	
	private void persistirLogradouro(Logradouro logradouro, Municipio municipio, TipoLogradouro tipoLogradouro, Bairro bairro, Integer numCep) {
		try {
			logradouro.setIdeLogradouro(null);
			logradouro.setIdeMunicipio(municipio);
			logradouro.setIndOrigemCorreio(false);
			logradouro.setIndOrigemApi(false);
			logradouro.setIdeBairro(bairro);
			logradouro.setIdeTipoLogradouro(tipoLogradouro);
			logradouro.setNumCep(numCep);


		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void persistirBairro(Bairro bairro, Municipio municipio) {
		try {
			bairro.setIdeBairro(null);
			bairro.setIdeMunicipio(municipio);
			bairro.setIndOrigemCorreio(false);
			bairro.setIndOrigemApi(false);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
	        	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void filtrarPorCep() {
		try {
			if(!Util.isNull(logradouroPesquisa.getNumCep())) {
				logradouro = new Logradouro(0);
				montaListaBairros();
				enableFormEndereco = true;
				showInputs = false;
				escolheuBairro = false;
				showInputLogradouro = false;
				bairro = new Bairro(0);
				endereco.setNumEndereco("");
				endereco.setDesComplemento("");
				endereco.setDesPontoReferencia("");
				isMunicipioSalvador = null;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void montaListaBairros() {
		try {
			listaBairro = new ArrayList<Bairro>();
			listaBairro = bairroService.listarBairroByLogradouro(logradouroPesquisa);
			
			if(!Util.isNullOuVazio(logradouroPesquisa.getNumCep())){
				Municipio tentaCarregar = municipioService.obterMunicipioPorCep(logradouroPesquisa.getNumCep());
				
				if(!Util.isNullOuVazio(tentaCarregar)){
					municipio = municipioService.obterMunicipioPorCep(logradouroPesquisa.getNumCep());
					
					if(!Util.isNullOuVazio(municipio.getIdeEstado()) && !Util.isNullOuVazio(municipio.getIdeEstado().getDesSigla()) && !"BA".equals(municipio.getIdeEstado().getDesSigla())) {
						JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
					}
				}
				
			}else if (Util.isNullOuVazio(logradouroPesquisa)){
				municipio = null;
			}
			
			
			if (!listaBairro.isEmpty() && !Util.isNullOuVazio(listaBairro.get(0))) {
				if(!Util.isNullOuVazio(municipio) && !Util.isNullOuVazio(municipio.getIdeMunicipio())){
					isMunicipioSalvador = (municipio.getIdeMunicipio().equals(MunicipioEnum.SALVADOR.getId()));					
				}else{					
					isMunicipioSalvador = null;
				}
				showUfCidade = Boolean.FALSE;
			} else {
				showUfCidade = Boolean.TRUE;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void changeLogradouroMunicipio(ValueChangeEvent event) {
		try {
			
			Bairro bairroSelected = (Bairro) event.getNewValue();
			if (bairroSelected.getIdeBairro() == -1) {
				bairro = new Bairro();
				escolheuBairro = false;
				showFdbairro = true;
				showInputs = Boolean.TRUE;
				showUfCidade = Boolean.TRUE;
				estado = new Estado(0);
			
			}
			if (!Util.isNull(bairroSelected) && bairroSelected.getIdeBairro() != 0 && bairroSelected.getIdeBairro() != -1) {
				bairro = enderecoFacade.filtrarBairroById(bairroSelected);
				carregarLogradouros();
			}
			tipoLogradouro = null;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarLogradouros() throws Exception {
		listaLogradouro = enderecoFacade.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
		
		if(!Util.isNullOuVazio(listaLogradouro) && listaLogradouro.size()!=0){
			showInputs = false;
			showInputLogradouro = false;
			logradouro = new Logradouro();
			logradouro.setIdeLogradouro(-1);
		}
		
		municipio = bairro.getIdeMunicipio();
		isMunicipioSalvador = municipio.getIdeMunicipio().equals(MunicipioEnum.SALVADOR.getId());
		estado = municipio.getIdeEstado();
		isEstadoBahia = estado.getIdeEstado().equals(EstadoEnum.BAHIA.getId());
		
		if(estado != null && !isEstadoBahia) {
			JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
			estado = new Estado(0);
			municipio = new Municipio(0);
			bairro = new Bairro();
			escolheuBairro = false;
			showUfCidade = Boolean.FALSE;
			showInputs = Boolean.FALSE;
		}else{
			escolheuBairro = true;
			showUfCidade = Boolean.FALSE;
			showFdbairro = false;
		}
	}
	
	public Collection<SelectItem> getValuesComboBairro() {
		
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		toReturn.add(new SelectItem(null, "Selecione..."));
		
		if(!Util.isNullOuVazio(listaBairro)) {
			Iterator<Bairro> i = listaBairro.iterator();
			while (i.hasNext()) {
				Bairro bairro = i.next();
				toReturn.add(new SelectItem(bairro, bairro.getNomBairro()));
			}
			toReturn.add(new SelectItem(new Bairro(-1), "Outro"));
		}
		return toReturn;
	}
	
	public boolean getDesabilitarLogradouro() {
		return !(enableFormEndereco && escolheuBairro);
	}
	
	public void changeLogradouro(ValueChangeEvent event) {
		try {
			Logradouro logradouroSelected = (Logradouro) event.getNewValue();
			if (logradouroSelected.getIdeLogradouro() == -1) {//para aparecer a caixa de texto de Logradouro
				showInputs = false;
				showInputLogradouro = true;
				logradouro.setIdeLogradouro(-1);
			}
			if (!Util.isNull(logradouroSelected) && logradouroSelected.getIdeLogradouro() != 0 && logradouroSelected.getIdeLogradouro() != -1) {
				logradouro = enderecoFacade.filtrarLogradouroById(logradouroSelected);
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				showInputLogradouro = false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<SelectItem> getValuesComboLogradouro() {
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		toReturn.add(new SelectItem(new Logradouro(0), "Selecione..."));
		
		if(!Util.isNullOuVazio(listaLogradouro)) {
			Iterator<Logradouro> i = listaLogradouro.iterator();
			while (i.hasNext()) {
				Logradouro logradouro = i.next();
				toReturn.add(new SelectItem(logradouro, logradouro.getNomLogradouro()));
			}
			toReturn.add(new SelectItem(new Logradouro(-1), "Outro"));
		}
		return toReturn;
	}
	
	public void changeEstado() {
		if (estado != null && !(estado.getIdeEstado() == null)) {
			try {
				listaMunicipio = (List<Municipio>) municipioService.filtrarListaMunicipiosPorEstado(estado);				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		} else {
			listaMunicipio = new ArrayList<Municipio>();
		}
	}
	
	public Collection<SelectItem> getValuesComboBoxMunicipio() {
		changeEstado();
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		toReturn.add(new SelectItem(new Municipio(0), "Selecione..."));
		if(!Util.isNullOuVazio(listaMunicipio)) {
			Iterator<Municipio> i = listaMunicipio.iterator();
			while (i.hasNext()) {
				Municipio municipio = (Municipio) i.next();
				toReturn.add(new SelectItem(municipio, municipio.getNomMunicipio()));
			}
		}
		return toReturn;
	}
	
	public void salvarEntidadeTransportadora() {
		Pessoa pessoaTransportadora = this.entidadeTransportadoraController.getEntidadeTransportadora();
		
		if(Util.isNull(pessoaTransportadora)) {
			JsfUtil.addErrorMessage("É obrigatório selecionar uma Entidade Transportadora!");
			return;
		}
		
		boolean existeTransportadora = false;
		
		this.entidadeTransportadoraSelecionada = this.entidadeTransportadoraController.getEntidadeTransportadoraSelecionada();
		
		if(Util.isNullOuVazio(this.entidadeTransportadoraController.getNumeroProcessoLicenca())) {
			JsfUtil.addErrorMessage("Nº do processo de licenciamento é de preenchimento obrigatório.");
			return;
		}
		
		if(!Util.isNull(this.entidadeTransportadoraSelecionada) &&
				Util.isNull(this.entidadeTransportadoraSelecionada.getIdeDeclaracaoTransporteEntidadeTransportadora())) {
			try {
				existeTransportadora = this.declaracaoTransporteService.existeTransportadora(pessoaTransportadora, getDeclaracaoTransporte());
			} catch (Exception e1) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
				JsfUtil.addErrorMessage("Não foi possível salvar a entidade transportadora.");
				existeTransportadora = true;
			}
		}
		
		if(!existeTransportadora) {
			
			if(!Util.isNull(pessoaTransportadora)) {
				try {
					String numProcessoLicenca = this.entidadeTransportadoraController.getNumeroProcessoLicenca();
					
					if(Util.isNull(this.entidadeTransportadoraSelecionada)) {
						this.entidadeTransportadoraSelecionada = new DeclaracaoTransporteEntidadeTransportadora();
					}
					
					if(Util.isNull(this.entidadeTransportadoraSelecionada.getDeclaracaoTransporte())) {
						this.entidadeTransportadoraSelecionada.setDeclaracaoTransporte(getDeclaracaoTransporte());
					}
					
					this.entidadeTransportadoraSelecionada.setNumProcessoLicenciamento(numProcessoLicenca);
					
					if(!Util.isNull(pessoaTransportadora.getPessoaJuridica()) && !Util.isNull(pessoaTransportadora.getPessoaJuridica().getIdePessoaJuridica())) {
						pessoaTransportadora = this.pessoaService.carregarGet(pessoaTransportadora.getPessoaJuridica().getIdePessoaJuridica());
					}
					
					if(!Util.isNull(pessoaTransportadora.getPessoaFisica()) && !Util.isNull(pessoaTransportadora.getPessoaFisica().getIdePessoaFisica())) {
						pessoaTransportadora = this.pessoaService.carregarGet(pessoaTransportadora.getPessoaFisica().getIdePessoaFisica());
					}

					if(!Util.isNull(pessoaTransportadora)){
						
						EnderecoPessoa enderecoPessoa = this.enderecoFacade.filtrarEnderecoByPessoa(pessoaTransportadora);
						
						if(!Util.isNull(enderecoPessoa)) {
							pessoaTransportadora.setEndereco(enderecoPessoa.getIdeEndereco());
						}
						
						this.entidadeTransportadoraSelecionada.setPessoa(pessoaTransportadora);
						
						this.declaracaoTransporteService.salvarEntidadeTransportadora(this.entidadeTransportadoraSelecionada);
					}
					
					JsfUtil.addSuccessMessage("Salvo com sucesso.");
					
					carregarListaEntidadeTransportadora();
					
					Html.esconder("dialogselecionarTransportadora");
					
				} catch(Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage("Não foi possível salvar a entidade transportadora.");
				}
			}
		}else{
			JsfUtil.addErrorMessage("Transportadora já selecionada.");
		}
	}
	
	private void carregarEndereco() {
		try {
			
			if (!Util.isNull(this.endereco)) {
				logradouro = enderecoFacade.filtrarLogradouroById(this.logradouro);
			
				if (!Util.isNotNullAndTrue(logradouro.getIndOrigemCorreio()) && !Util.isNotNullAndTrue(logradouro.getIndOrigemApi()) ) { 
					showInputLogradouro = true;
					showInputs = true;
				}
				
				bairro = enderecoFacade.filtrarBairroById(logradouro.getIdeBairro());
				municipio = bairro.getIdeMunicipio();
				
				
				if(municipio.getIdeMunicipio()!=837) {
					isMunicipioSalvador = false;
				}
				
				estado = municipio.getIdeEstado();
				logradouroPesquisa.setNumCep(logradouro.getNumCep());

				montaListaBairros(bairro.getIndOrigemCorreio());
				
				listaLogradouro = enderecoFacade.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				enableFormEndereco = Boolean.TRUE;
				
				if (!bairro.getIndOrigemCorreio() && !bairro.isIndOrigemApi()) {
					showFdbairro = Boolean.TRUE;
					bairro.setIdeBairro(-1);
				} else {
					showFdbairro = Boolean.FALSE;
				}
			} else {
				endereco = new Endereco();
				showFdbairro = Boolean.FALSE;
			}
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void montaListaBairros(boolean indBairroOrigemCorreio) {
		try {
			
			listaBairro = new ArrayList<Bairro>();
			listaBairro = bairroService.listarBairroByLogradouro(logradouroPesquisa);
			
			if (!listaBairro.isEmpty() && !Util.isNullOuVazio(listaBairro.get(0))) {
				Bairro bairroEncontrado = listaBairro.get(0);
				
				if (!bairroEncontrado.getIndOrigemCorreio() && !bairroEncontrado.isIndOrigemApi()) {
					bairro.setIdeBairro(-1);
				} 
				
				this.municipio = listaBairro.get(0).getIdeMunicipio();
				this.estado = municipio.getIdeEstado();
				this.showUfCidade = Boolean.FALSE;
			} else {
				this.bairro = new Bairro(0);
				this.municipio = new Municipio(0);
				this.estado = new Estado(0);
				this.showUfCidade = Boolean.TRUE;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirEntidadeTransportadora() {
		if(!Util.isNull(this.entidadeTransportadoraSelecionada) && !Util.isNull(this.entidadeTransportadoraSelecionada.getIdeDeclaracaoTransporteEntidadeTransportadora())) {
			try {
				this.declaracaoTransporteService.excluirEntidadeTransportadora(entidadeTransportadoraSelecionada);
				
				this.entidadeTransportadoraSelecionada = new DeclaracaoTransporteEntidadeTransportadora();
				
				carregarListaEntidadeTransportadora();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				
				JsfUtil.addErrorMessage("Não foi possível excluir a Entidade Transportadora.");
			}
			
			Html.atualizar("tabViewDTRP:formDeclaracao:dtEntidadesTransportadoras");
		}
	}
	
	public void excluirEnderecoResiduo() {
		if(!Util.isNull(this.enderecoSelecionado) && !Util.isNull(this.enderecoSelecionado.getIdeDeclaracaoTransporteResiduo())) {
			try {
				this.declaracaoTransporteService.excluirEnderecoResiduo(this.enderecoSelecionado);
				
				this.enderecoSelecionado = new DeclaracaoTransporteResiduoEndereco();
				
				carregarListaEnderecoGeracaoResiduo();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				
				JsfUtil.addErrorMessage("Não foi possível excluir a Endereço Resíduo.");
			}
			
			Html.atualizar("tabViewDTRP:formDeclaracao:dtEntidadesTransportadoras");
		}
	}
	
	public void onTabChange(TabChangeEvent event) {
		if("tab01".equals(event.getTab().getId())) {
			carregarDadosAba1();
			activeIndex = ABA_ETAPA_1;
			Html.atualizar("tabViewDTRP");
		}else if("tab02".equals(event.getTab().getId())) {
			carregarDadosAba2();
			activeIndex = ABA_ETAPA_2;
			Html.atualizar("tabViewDTRP:tab02");
			Html.atualizar("tabViewDTRP:tab02:form_Etapa2");
		}else {
			Html.atualizar("tabViewDTRP");
			carregarDadosAba1();
			carregarDadosAba2();
			activeIndex = ABA_ETAPA_3;
			Html.atualizar("tabViewDTRP:tab03");
			Html.atualizar("tabViewDTRP:tab03:formEtapa3");
		}
	}
	
	public StreamedContent getImprimirRelatorio() {
		Exception erro = null;
		try {
			// obter ato declaratorio by requerimentoDTO
			
			/*
			declaracaoTransporte.getAtoDeclaratorio().setIdeDocumentoObrigatorio(new DocumentoObrigatorio(DocumentoObrigatorioEnum.DTRP_DOCUMENTO_1.getId()));
			return new ImpressoraAtoDeclaratorio().imprimirRelatorio(declaracaoTransporte.getAtoDeclaratorio());
			
			 * 
			 * 
			 */
			
			Map<String, Object> lParametros = new HashMap<String, Object>();
			lParametros.put("SUBREPORT_DIR", retornaCaminhoRelatorioDTRP());
			lParametros.put("NOME_RELATORIO", "relatorio_dtrp.jasper");
			lParametros.put("ide_requerimento", getRequerimentoConsulta().getIdeRequerimento());
			return new RelatorioUtil(lParametros).gerar();
		} catch (Exception e) {
			erro = e;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public StreamedContent getImprimirCertificadoDTRP(Integer ideRequerimento) {
		Exception  erro = null;
		try {
			if (!this.declaracaoTransporteService.hasCertificado(ideRequerimento)) {
				Certificado certificado = this.gerarCertificadoDTRP(ideRequerimento);
				this.declaracaoTransporteService.salvarCertificadoDTRP(certificado);
			}else if(!this.declaracaoTransporteService.hasToken(ideRequerimento, AtoAmbientalEnum.DTRP)){
				Certificado certificado = this.declaracaoTransporteService.carregarCertificado(ideRequerimento, AtoAmbientalEnum.DTRP);
				this.declaracaoTransporteService.atualizarTokenCertificado(certificado);
			}

			return this.imprimirCertificadoDTRP(ideRequerimento);
		} catch (Exception e) {
			erro =null;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	private StreamedContent imprimirCertificadoDTRP(Integer ideRequerimento) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("SUBREPORT_DIR", retornaCaminhoRelatorioDTRP());
		lParametros.put("NOME_RELATORIO", "certificado.jasper");
		lParametros.put("ide_requerimento", ideRequerimento);
		return new RelatorioUtil(lParametros).gerar();
	}
	
	private Certificado gerarCertificadoDTRP(Integer ideRequerimento) throws Exception {
		AtoAmbiental atoAmbiental = getAtoAmbientalDTRP();
		Requerimento requerimento = requerimentoService.buscarEntidadePorId(new Requerimento(ideRequerimento));
		
		return gerarCertificado(atoAmbiental, requerimento);
	}
	
	private Certificado gerarCertificado(AtoAmbiental atoAmbiental, Requerimento requerimento) {
		try {
			return certificadoUtil.gerarCertificado(atoAmbiental, requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
	}
	
	protected AtoAmbiental getAtoAmbientalDTRP() {
		AtoAmbientalEnum atoAmbientalDTRP = AtoAmbientalEnum.DTRP;
		AtoAmbiental atoAmbiental = new AtoAmbiental(atoAmbientalDTRP.getId(), atoAmbientalDTRP.getSigla());
		return atoAmbiental;
	}
	
	public static String retornaCaminhoRelatorioDTRP() {	
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//		File file = new File();
		return sessao.getServletContext().getRealPath(File.separator + Uri.URL_RELATORIOS_ATO_DTRP) + File.separator;
	}

	public void mudarNumProcesso() {
		if(!enderecoSelecionado.getIndPossuiLicencaAutorizacao()) {
			enderecoSelecionado.setNumProcessoLicencaAutorizacao(null);
		}
	}
	
	private boolean validarResiduoSelecionado() {
		boolean valido = true;
		
		if(!Util.isNullOuVazio(this.listaResiduoSelecionado)) {
			for(DeclaracaoTransporteResiduo dtr : this.listaResiduoSelecionado) {
				if(Util.isNullOuVazio(dtr.getQtdTransportada())) {
					valido = false;
				}
				
				if(Util.isNullOuVazio(dtr.getEstadoFisico().getIdeEstadoFisico())) {
					valido = false;
				}
				
				if(Util.isNullOuVazio(dtr.getAcondicionamento().getIdeAcondicionamento())) {
					valido = false;
				}
				
				if(Util.isNullOuVazio(dtr.getTipoVeiculo().getIdeTipoVeiculo())) {
					valido = false;
				}
			}
		}
		
		return valido;
	}
	
	public void salvarResiduo() {
		try {
			if(validarResiduo()) {
				if(this.residuoSelecionado.getDeclaracaoTransporte() == null) {
					this.residuoSelecionado.setDeclaracaoTransporte(getDeclaracaoTransporte());
				}
				
				declaracaoTransporteService.salvarDeclaracaoTransporteResiduo(residuoSelecionado);
				
				this.residuoSelecionado = null;
				
//				carregarDadosAba2();
				
				JsfUtil.addSuccessMessage("Salvo com sucesso.");
				Html.esconder("dialogCaracterizarResiduo");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível salvar o resíduo.");
		}
		
		
		Html.atualizar("tabViewDTRP:form_Etapa2");
		Html.atualizar("tabViewDTRP");
	}
	
	private boolean validarResiduo() {
		boolean valido = true;
		
		if(Util.isNull(this.residuoSelecionado.getQtdTransportada()) || this.residuoSelecionado.getQtdTransportada().compareTo(BigDecimal.ZERO) <= 0) {
			JsfUtil.addWarnMessage("A quantidade média anual do resíduo transportado deve ser maior que zero.");
			valido = false;
		}
		
		if(Util.isNull(this.residuoSelecionado.getEstadoFisico()) 
				|| (!Util.isNull(this.residuoSelecionado.getEstadoFisico()) && Util.isNull(this.residuoSelecionado.getEstadoFisico().getIdeEstadoFisico()))) {
			JsfUtil.addWarnMessage("Estado Físico é obrigatório ser preenchido.");
			valido = false;
		}
		
		if(Util.isNull(this.residuoSelecionado.getAcondicionamento()) 
				|| (!Util.isNull(this.residuoSelecionado.getAcondicionamento()) && Util.isNull(this.residuoSelecionado.getAcondicionamento().getIdeAcondicionamento()))) {
			JsfUtil.addWarnMessage("Acondicionamento é obrigatório ser preenchido.");
			valido = false;
		}
		
		if(Util.isNull(this.residuoSelecionado.getTipoVeiculo()) 
				|| (!Util.isNull(this.residuoSelecionado.getTipoVeiculo()) && Util.isNull(this.residuoSelecionado.getTipoVeiculo().getIdeTipoVeiculo()))) {
			
			JsfUtil.addWarnMessage("Tipo Veículo é obrigatório ser preenchido.");
			valido = false;
		}
		
		return valido;
	}
	
	public void editarResiduo(DeclaracaoTransporteResiduo residuo,boolean visualizar) {
		permiteEditarResiduo = visualizar;
		
		if(!Util.isNullOuVazio(residuo.getIdeDeclaracaoTransporteResiduo())) {
			try {
				this.residuoSelecionado = declaracaoTransporteService.obterDeclaracaoTransporteResiduoPor(residuo.getIdeDeclaracaoTransporteResiduo());
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}
	
	public Pessoa getGeradorResiduo() {
		return geradorResiduo;
	}

	public void setGeradorResiduo(Pessoa geradorResiduo) {
		this.geradorResiduo = geradorResiduo;
	}

	public List<Endereco> getListaEnderecoResiduo() {
		return listaEnderecoResiduo;
	}

	public void setListaEnderecoResiduo(List<Endereco> listaEnderecoResiduo) {
		this.listaEnderecoResiduo = listaEnderecoResiduo;
	}

	public Pessoa getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Pessoa destinatario) {
		this.destinatario = destinatario;
	}

	public EnderecoPessoa getEnderecoGeradorResiduo() {
		return enderecoGeradorResiduo;
	}

	public void setEnderecoGeradorResiduo(EnderecoPessoa enderecoGeradorResiduo) {
		this.enderecoGeradorResiduo = enderecoGeradorResiduo;
	}

	public List<DisposicaoFinalResiduo> getListaDisposicaoFinalResiduo() {
		return listaDisposicaoFinalResiduo;
	}

	public void setListaDisposicaoFinalResiduo(
			List<DisposicaoFinalResiduo> listaDisposicaoFinalResiduo) {
		this.listaDisposicaoFinalResiduo = listaDisposicaoFinalResiduo;
	}

	public List<DisposicaoFinalResiduo> getListaDisposicaoFinalResiduoSelected() {
		return listaDisposicaoFinalResiduoSelected;
	}

	public void setListaDisposicaoFinalResiduoSelected(
			List<DisposicaoFinalResiduo> listaDisposicaoFinalResiduoSelected) {
		this.listaDisposicaoFinalResiduoSelected = listaDisposicaoFinalResiduoSelected;
	}

	public String getNomeResiduo() {
		return nomeResiduo;
	}

	public void setNomeResiduo(String nomeResiduo) {
		this.nomeResiduo = nomeResiduo;
	}

	public List<Residuo> getListaResiduos() {
		return listaResiduos;
	}

	public void setListaResiduos(List<Residuo> listaResiduos) {
		this.listaResiduos = listaResiduos;
	}

	public List<Residuo> getListaResiduoSelected() {
		return listaResiduoSelected;
	}

	public void setListaResiduoSelected(List<Residuo> listaResiduoSelected) {
		this.listaResiduoSelected = listaResiduoSelected;
	}
	
	public List<Acondicionamento> getListaAcondicionamentos() {
		return listaAcondicionamentos;
	}

	public void setListaAcondicionamentos(
			List<Acondicionamento> listaAcondicionamentos) {
		this.listaAcondicionamentos = listaAcondicionamentos;
	}

	public List<EstadoFisico> getListaEstadoFisico() {
		return listaEstadoFisico;
	}

	public void setListaEstadoFisico(List<EstadoFisico> listaEstadoFisico) {
		this.listaEstadoFisico = listaEstadoFisico;
	}

	public List<TipoVeiculo> getListaTipoVeiculo() {
		return listaTipoVeiculo;
	}

	public void setListaTipoVeiculo(List<TipoVeiculo> listaTipoVeiculo) {
		this.listaTipoVeiculo = listaTipoVeiculo;
	}

	public DeclaracaoTransporte getDeclaracaoTransporte() {
		return declaracaoTransporte;
	}

	public void setDeclaracaoTransporte(DeclaracaoTransporte declaracaoTransporte) {
		this.declaracaoTransporte = declaracaoTransporte;
	}

	public List<DeclaracaoTransporteResiduoEndereco> getListaEnderecoGeracaoResiduo() {
		return listaEnderecoGeracaoResiduo;
	}

	public void setListaEnderecoGeracaoResiduo(
			List<DeclaracaoTransporteResiduoEndereco> listaEnderecoGeracaoResiduo) {
		this.listaEnderecoGeracaoResiduo = listaEnderecoGeracaoResiduo;
	}

	public DeclaracaoTransporteResiduoEndereco getDeclaracaoTransporteResiduoEndereco() {
		return declaracaoTransporteResiduoEndereco;
	}

	public void setDeclaracaoTransporteResiduoEndereco(
			DeclaracaoTransporteResiduoEndereco declaracaoTransporteResiduoEndereco) {
		this.declaracaoTransporteResiduoEndereco = declaracaoTransporteResiduoEndereco;
	}

	public DeclaracaoTransporteDestinatarioResiduo getDeclaracaoTransporteDestinatarioResiduo() {
		return declaracaoTransporteDestinatarioResiduo;
	}

	public void setDeclaracaoTransporteDestinatarioResiduo(
			DeclaracaoTransporteDestinatarioResiduo declaracaoTransporteDestinatarioResiduo) {
		this.declaracaoTransporteDestinatarioResiduo = declaracaoTransporteDestinatarioResiduo;
	}

	public DeclaracaoTransporteGeradorResiduo getDeclaracaoTransporteGeradorResiduo() {
		return declaracaoTransporteGeradorResiduo;
	}

	public void setDeclaracaoTransporteGeradorResiduo(
			DeclaracaoTransporteGeradorResiduo declaracaoTransporteGeradorResiduo) {
		this.declaracaoTransporteGeradorResiduo = declaracaoTransporteGeradorResiduo;
	}

	public Boolean getdesabilitaTab01() {
		return desabilitaTab01;
	}

	public void setdesabilitaTab01(Boolean desabilitaTab01) {
		this.desabilitaTab01 = desabilitaTab01;
	}

	public Boolean getdesabilitaTab02() {
		return desabilitaTab02;
	}

	public void setdesabilitaTab02(Boolean desabilitaTab02) {
		this.desabilitaTab02 = desabilitaTab02;
	}

	public Boolean getdesabilitaTab03() {
		return desabilitaTab03;
	}

	public void setdesabilitaTab03(Boolean desabilitaTab03) {
		this.desabilitaTab03 = desabilitaTab03;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public List<DeclaracaoTransporteResiduo> getListaResiduoSelecionado() {
		return listaResiduoSelecionado;
	}

	public void setListaResiduoSelecionado(
			List<DeclaracaoTransporteResiduo> listaResiduoSelecionado) {
		this.listaResiduoSelecionado = listaResiduoSelecionado;
	}

	public Boolean getDesabilitaTab01() {
		return desabilitaTab01;
	}

	public void setDesabilitaTab01(Boolean desabilitaTab01) {
		this.desabilitaTab01 = desabilitaTab01;
	}

	public Boolean getDesabilitaTab02() {
		return desabilitaTab02;
	}

	public void setDesabilitaTab02(Boolean desabilitaTab02) {
		this.desabilitaTab02 = desabilitaTab02;
	}

	public Boolean getDesabilitaTab03() {
		return desabilitaTab03;
	}

	public void setDesabilitaTab03(Boolean desabilitaTab03) {
		this.desabilitaTab03 = desabilitaTab03;
	}

	public DeclaracaoTransporteResiduo getResiduoSelecionado() {
		return residuoSelecionado;
	}

	public void setResiduoSelecionado(DeclaracaoTransporteResiduo residuoSelecionado) {
		this.residuoSelecionado = residuoSelecionado;
	}

	public Residuo getResiduoSelected() {
		return residuoSelected;
	}

	public void setResiduoSelected(Residuo residuoSelected) {
		this.residuoSelected = residuoSelected;
	}

	public Logradouro getLogradouro() {
		return logradouro;
	}

	public Logradouro getLogradouroPesquisa() {
		return logradouroPesquisa;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public Estado getEstado() {
		return estado;
	}

	public TipoLogradouro getTipoLogradouro() {
		return tipoLogradouro;
	}

	public List<Logradouro> getListaLogradouro() {
		return listaLogradouro;
	}

	public List<Bairro> getListaBairro() {
		return listaBairro;
	}

	public List<Municipio> getListaMunicipio() {
		return listaMunicipio;
	}

	public List<Estado> getListaEstado() {
		return listaEstado;
	}

	public Boolean getIsMunicipioSalvador() {
		return isMunicipioSalvador;
	}

	public Boolean getIsEstadoBahia() {
		return isEstadoBahia;
	}

	public boolean isShowInputs() {
		return showInputs;
	}

	public boolean isShowInputLogradouro() {
		return showInputLogradouro;
	}

	public boolean isEnableFormEndereco() {
		return enableFormEndereco;
	}

	public boolean isEscolheuBairro() {
		return escolheuBairro;
	}

	public Boolean getShowUfCidade() {
		return showUfCidade;
	}

	public Boolean getShowFdbairro() {
		return showFdbairro;
	}

	public void setLogradouro(Logradouro logradouro) {
		this.logradouro = logradouro;
	}

	public void setLogradouroPesquisa(Logradouro logradouroPesquisa) {
		this.logradouroPesquisa = logradouroPesquisa;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public void setListaLogradouro(List<Logradouro> listaLogradouro) {
		this.listaLogradouro = listaLogradouro;
	}

	public void setListaBairro(List<Bairro> listaBairro) {
		this.listaBairro = listaBairro;
	}

	public void setListaMunicipio(List<Municipio> listaMunicipio) {
		this.listaMunicipio = listaMunicipio;
	}

	public void setListaEstado(List<Estado> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public void setIsMunicipioSalvador(Boolean isMunicipioSalvador) {
		this.isMunicipioSalvador = isMunicipioSalvador;
	}

	public void setIsEstadoBahia(Boolean isEstadoBahia) {
		this.isEstadoBahia = isEstadoBahia;
	}

	public void setShowInputs(boolean showInputs) {
		this.showInputs = showInputs;
	}

	public void setShowInputLogradouro(boolean showInputLogradouro) {
		this.showInputLogradouro = showInputLogradouro;
	}

	public void setEnableFormEndereco(boolean enableFormEndereco) {
		this.enableFormEndereco = enableFormEndereco;
	}

	public void setEscolheuBairro(boolean escolheuBairro) {
		this.escolheuBairro = escolheuBairro;
	}

	public void setShowUfCidade(Boolean showUfCidade) {
		this.showUfCidade = showUfCidade;
	}

	public void setShowFdbairro(Boolean showFdbairro) {
		this.showFdbairro = showFdbairro;
	}

	public DeclaracaoTransporteResiduoEndereco getDeclaracaoTransporteDestinacaoResiduoEndereco() {
		return declaracaoTransporteDestinacaoResiduoEndereco;
	}

	public void setDeclaracaoTransporteDestinacaoResiduoEndereco(
			DeclaracaoTransporteResiduoEndereco declaracaoTransporteDestinacaoResiduoEndereco) {
		this.declaracaoTransporteDestinacaoResiduoEndereco = declaracaoTransporteDestinacaoResiduoEndereco;
	}

	public List<DeclaracaoTransporteEntidadeTransportadora> getListaEntidadeTransportadora() {
		return listaEntidadeTransportadora;
	}

	public void setListaEntidadeTransportadora(
			List<DeclaracaoTransporteEntidadeTransportadora> listaEntidadeTransportadora) {
		this.listaEntidadeTransportadora = listaEntidadeTransportadora;
	}

	public DeclaracaoTransporteEntidadeTransportadora getEntidadeTransportadoraSelecionada() {
		return entidadeTransportadoraSelecionada;
	}

	public void setEntidadeTransportadoraSelecionada(
			DeclaracaoTransporteEntidadeTransportadora entidadeTransportadoraSelecionada) {
		this.entidadeTransportadoraSelecionada = entidadeTransportadoraSelecionada;
	}

	public LazyDataModel<DeclaracaoTransporteEntidadeTransportadora> getListaEntidadeTransportadoraModel() {
		return listaEntidadeTransportadoraModel;
	}

	public void setListaEntidadeTransportadoraModel(
			LazyDataModel<DeclaracaoTransporteEntidadeTransportadora> listaEntidadeTransportadoraModel) {
		this.listaEntidadeTransportadoraModel = listaEntidadeTransportadoraModel;
	}

	public LazyDataModel<DeclaracaoTransporteResiduoEndereco> getListaEnderecoGeradorResiduoModel() {
		return listaEnderecoGeradorResiduoModel;
	}

	public void setListaEnderecoGeradorResiduoModel(
			LazyDataModel<DeclaracaoTransporteResiduoEndereco> listaEnderecoGeradorResiduoModel) {
		this.listaEnderecoGeradorResiduoModel = listaEnderecoGeradorResiduoModel;
	}

	public DeclaracaoTransporteResiduoEndereco getEnderecoSelecionado() {
		return enderecoSelecionado;
	}

	public void setEnderecoSelecionado(
			DeclaracaoTransporteResiduoEndereco enderecoSelecionado) {
		this.enderecoSelecionado = enderecoSelecionado;
	}

	public List<DeclaracaoTransporteResiduo> getListaResiduosCadastrados() {
		return listaResiduosCadastrados;
	}

	public void setListaResiduosCadastrados(
			List<DeclaracaoTransporteResiduo> listaResiduosCadastrados) {
		this.listaResiduosCadastrados = listaResiduosCadastrados;
	}

	public DeclaracaoTransporteDisposicaoFinalResiduo getDeclaracaoTransporteDisposicaoFinalResiduo() {
		return declaracaoTransporteDisposicaoFinalResiduo;
	}

	public void setDeclaracaoTransporteDisposicaoFinalResiduo(
			DeclaracaoTransporteDisposicaoFinalResiduo declaracaoTransporteDisposicaoFinalResiduo) {
		this.declaracaoTransporteDisposicaoFinalResiduo = declaracaoTransporteDisposicaoFinalResiduo;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public boolean isPermiteSalvar() {
		
		if(!Util.isNull(getDeclaracaoTransporte()) && !Util.isNullOuVazio(getDeclaracaoTransporte().getIdeDeclaracaoTransporte())) {
			return true;
		}
		
		return false;
	}

	public boolean isPermiteEditar() {
		return permiteEditar;
	}

	public void setPermiteEditar(boolean permiteEditar) {
		this.permiteEditar = permiteEditar;
	}

	public RequerimentoDTO getRequerimentoDTO() {
		return requerimentoDTO;
	}

	public void setRequerimentoDTO(RequerimentoDTO requerimentoDTO) {
		this.requerimentoDTO = requerimentoDTO;
	}

	public String getHintEnderecoGeracaoResiduo() {
		return BUNDLE.getString("dtrp_hint_endereco_geracao_residuo");
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public RequerimentoPessoa getReqPessoaRequerente() {
		return reqPessoaRequerente;
	}

	public void setReqPessoaRequerente(RequerimentoPessoa reqPessoaRequerente) {
		this.reqPessoaRequerente = reqPessoaRequerente;
	}

	public RequerimentoPessoa getReqPessoaSolicitante() {
		return reqPessoaSolicitante;
	}

	public void setReqPessoaSolicitante(RequerimentoPessoa reqPessoaSolicitante) {
		this.reqPessoaSolicitante = reqPessoaSolicitante;
	}

	public Requerimento getRequerimentoConsulta() {
		return requerimentoConsulta;
	}

	public void setRequerimentoConsulta(Requerimento requerimentoConsulta) {
		this.requerimentoConsulta = requerimentoConsulta;
	}

	public boolean isPermiteEditarResiduo() {
		return permiteEditarResiduo;
	}

	public void setPermiteEditarResiduo(boolean permiteEditarResiduo) {
		this.permiteEditarResiduo = permiteEditarResiduo;
	}
}
