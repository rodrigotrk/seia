package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.log4j.Level;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.CaepogDocumentoApensadoDTO;
import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.CaepogCampo;
import br.gov.ba.seia.entity.CaepogCertificado;
import br.gov.ba.seia.entity.CaepogCertificadoPK;
import br.gov.ba.seia.entity.CaepogClasseResiduo;
import br.gov.ba.seia.entity.CaepogComunicacao;
import br.gov.ba.seia.entity.CaepogDefinicaoCampo;
import br.gov.ba.seia.entity.CaepogDocumento;
import br.gov.ba.seia.entity.CaepogDocumentoApensado;
import br.gov.ba.seia.entity.CaepogDocumentoApensadoPK;
import br.gov.ba.seia.entity.CaepogFasePerfuracao;
import br.gov.ba.seia.entity.CaepogFaseVariavel;
import br.gov.ba.seia.entity.CaepogFaseVariavelPK;
import br.gov.ba.seia.entity.CaepogFormacaoGeologica;
import br.gov.ba.seia.entity.CaepogFormacaoGeologicaPoco;
import br.gov.ba.seia.entity.CaepogFormacaoGeologicaPocoPK;
import br.gov.ba.seia.entity.CaepogLocacao;
import br.gov.ba.seia.entity.CaepogObjetivoAtividade;
import br.gov.ba.seia.entity.CaepogObjetivoAtividadePoco;
import br.gov.ba.seia.entity.CaepogObjetivoAtividadePocoPK;
import br.gov.ba.seia.entity.CaepogPoco;
import br.gov.ba.seia.entity.CaepogStatus;
import br.gov.ba.seia.entity.CaepogTipoPoco;
import br.gov.ba.seia.entity.CaepogTipoResiduo;
import br.gov.ba.seia.entity.CaepogTipoStatus;
import br.gov.ba.seia.entity.CaepogVariavel;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoExterno;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.TipoCertificado;
import br.gov.ba.seia.entity.VwConsultaEmpreendimento;
import br.gov.ba.seia.enumerator.CaepogCategoriaDocumentoEnum;
import br.gov.ba.seia.enumerator.CaepogTipoResiduoEnum;
import br.gov.ba.seia.enumerator.CaepogTipoStatusEnum;
import br.gov.ba.seia.enumerator.CaepogVariavelEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.OrgaoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.enumerator.ValidacaoShapeEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.PessoaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.CaepogCampoService;
import br.gov.ba.seia.service.CaepogComunicacaoService;
import br.gov.ba.seia.service.CaepogDefinicaoCampoService;
import br.gov.ba.seia.service.CaepogDocumentoApensadoService;
import br.gov.ba.seia.service.CaepogDocumentoService;
import br.gov.ba.seia.service.CaepogLocacaoService;
import br.gov.ba.seia.service.CaepogPocoService;
import br.gov.ba.seia.service.CaepogService;
import br.gov.ba.seia.service.CaepogStatusService;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.OrgaoService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.ProcessoExternoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.ResponsavelEmpreendimentoService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("caepogController")
@ViewScoped
public class CaepogController extends SeiaControllerAb implements Serializable {
	
	@Inject
	protected LocalizacaoGeograficaGenericController locGeoController;
	
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private CaepogService caepogService;
	
	@EJB
	private CaepogCampoService caepogCampoService;
	
	@EJB
	private CaepogPocoService caepogPocoService;
	
	@EJB
	private CaepogDefinicaoCampoService caepogDefinicaoCampoService;
	
	@EJB
	private CaepogLocacaoService caepogLocacaoService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private EnderecoPessoaService enderecoPessoaService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private ResponsavelEmpreendimentoService responsavelEmpreendimentoService;
	
	@EJB
	private CaepogDocumentoApensadoService caepogDocumentoApensadoService;
	
	@EJB
	private CaepogDocumentoService caepogDocumentoService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private ImovelService imovelService;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private ProcessoExternoService processoExternoService;

	@EJB
	private CertificadoService certificadoService;
	
	@EJB
	private OrgaoService orgaoService;
	
	@EJB
	private PessoaService pessoaService;

	@EJB 
	private CaepogComunicacaoService caepogComunicacaoService;
	
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;

	@EJB
	private CaepogStatusService caepogStatusService;
	
	@EJB
	private PessoaFacade pessoaFacade;
	
	private static final long serialVersionUID = 589914014473456176L;
	
	//CONTROLE DAS ABAS
	private int tabAtual;
	private Boolean visualizaProximo;
	private Boolean visualizaAnterior;
	private Boolean visualizaFinalizar;
	private int tabAtualPoco;
	private Boolean visualizaProximoPoco;
	private Boolean visualizaAnteriorPoco;
	private boolean habilitaAbaCampo;
	private boolean habilitaAbaLocacao;
	private boolean habilitaAbaPoco;
	private boolean habilitaAbaDocumento;
	
	//TELA DE CONSULTA
	private Pessoa requerenteFiltro;
	private Empreendimento empreendimentoFiltro;
	private List<CaepogCampo> listaCaepogCampoFiltro;
	private CaepogCampo caepogCampoSelecionadoFiltro;
	private Collection<Municipio> listaMunicipioFiltro;
	private Municipio municipioSelecionadoFiltro;
	private String numCaepogFiltro;
	private List<CaepogTipoStatus> listaCaepogTipoStatusFiltro;
	private CaepogTipoStatus caepogTipoStatusSelecionadoFiltro;
	private Date periodoInicioFiltro;
	private Date periodoFimFiltro;
	private DataTable dataTableCadastroOleoGas;
	private LazyDataModel<Caepog> cadastroOleoGasDataModel;
	private boolean isEditando;
	private boolean isTelaConsulta;
	
	//DIALOG HISTORICO DE COMUNICAÇÃO
	private List<CaepogComunicacao> listaCaepogComunicacoes;
	private List<CaepogStatus> listaCaepogStatus;
	private CaepogComunicacao comunicacaoSelecionada;
	
	//DIALOG BUSCA EMPREENDIMENTO
	private Empreendimento empreendimentoDialogFiltro;
	private LazyDataModel<VwConsultaEmpreendimento> empreendimentoDataModel;
	
	//ABA DADOS BASICOS
	private Caepog caepog;
	private Pessoa requerente;
	private Empreendimento empreendimento;
	private RepresentanteLegal representanteLegal;
	private ResponsavelEmpreendimento responsavelEmpreendimento;
	
	//ABA DADOS DO CAMPO
	private CaepogDefinicaoCampo caepogDefinicaoCampo;
	
	//ABA LOCACAO
	private CaepogLocacao caepogLocacao;
	private List<Imovel> listImovel;
	
	//ABA POCO
	private CaepogPoco caepogPoco;
	private CaepogFasePerfuracao caepogFasePerfuracao;
	private List<CaepogTipoPoco> listCaepogTipoPoco;
	private List<CaepogObjetivoAtividade> listCaepogObjetivoAtividade;
	private List<CaepogObjetivoAtividade> listCaepogObjetivoAtividadeSelecionados;
	private List<CaepogFormacaoGeologica> listCaepogFormacaoGeologica;
	private List<CaepogFormacaoGeologica> listCaepogFormacaoGeologicaSelecionados;
	private List<CaepogTipoResiduo> listCaepogTipoResiduo;
	private List<CaepogClasseResiduo> listCaepogClasseResiduo;
	
	//ABA DOCUMENTOS E ESTUDOS
	private List<CaepogDocumentoApensadoDTO> documentosCampo;
	private List<CaepogDocumentoApensadoDTO> documentosLocacao;
	private List<CaepogDocumentoApensadoDTO> documentosPoco;
	private List<CaepogDocumentoApensadoDTO> documentosAdicionais;
	private CaepogDocumentoApensadoDTO  caepogDocumentoApensadoDtoSelecionado;
	private CaepogComunicacao caepogComunicacao;

	/*************
	/*			 *
	//XXX: GERAL *
	/* 			 *
	/*************/
	
	@PostConstruct
	public void init() {
		try{
			carregarVariaveis();
			carregarObjetosTelaConsulta();
			ContextoUtil.getContexto().setTelaParaRedirecionar(null);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarVariaveis() {
		isEditando = false;
		caepogDefinicaoCampo = new CaepogDefinicaoCampo();
		caepogPoco = new CaepogPoco();
		caepogFasePerfuracao = new CaepogFasePerfuracao();
		
		caepog = ContextoUtil.getContexto().getCaepog();
		ContextoUtil.getContexto().setCaepog(null);
		
		if(Util.isNullOuVazio(caepog)) {
			caepog = new Caepog();
			caepog.setCaepogDefinicaoCampoCollection(new ArrayList<CaepogDefinicaoCampo>());
			
		} else {
			empreendimento = caepog.getIdeEmpreendimento();
			requerente = caepog.getIdePessoaRequerente();
			carregarDadosBasicos(empreendimento);
		}
	}
	
	private void carregarObjetosTelaConsulta()  {
		empreendimentoFiltro = new Empreendimento();
		empreendimentoDialogFiltro = new Empreendimento();
		
		listaMunicipioFiltro = municipioService.filtrarListaMunicipiosPorEstado(new Estado(5));
		listaCaepogCampoFiltro = caepogCampoService.listarTodos();
		Collections.sort(listaCaepogCampoFiltro);
		listaCaepogTipoStatusFiltro = caepogStatusService.listarTodosCaepogTipoStatus();
	}
	
	public void atualizaMensagemByPoll() {
		
		RequestContext.getCurrentInstance().execute("caepogPoll.stop();");
		
		if(!Util.isNullOuVazio(caepog.getMsgErroPendenciaLocalizacao())) {
			JsfUtil.addWarnMessage(caepog.getMsgErroPendenciaLocalizacao());
		}
		
		if(caepog.isCadastroIncompleto()) {
			JsfUtil.addWarnMessage("Cadastro incompleto.");
			
		} else if(caepog.isAguardandoValidacao()) {
			JsfUtil.addSuccessMessage("Cadastro finalizado com sucesso!");
			
		} else if(caepog.isPendenciaValidacao()) {
			JsfUtil.addSuccessMessage("Pendência de validação registrada com sucesso!");
			
		} else if(caepog.isValidado()) {
			JsfUtil.addSuccessMessage("Cadastro validado com sucesso!");
		}

		caepog = new Caepog();
		caepog.setCaepogDefinicaoCampoCollection(new ArrayList<CaepogDefinicaoCampo>());
	}
	
	public void limparTelaConsulta() {
		requerenteFiltro = null;
		caepogCampoSelecionadoFiltro = null;
		empreendimentoFiltro = new Empreendimento();
		empreendimentoDialogFiltro = new Empreendimento();
		municipioSelecionadoFiltro = null;
		numCaepogFiltro = null;
		caepogTipoStatusSelecionadoFiltro = null;
		periodoInicioFiltro = null;
		periodoFimFiltro = null;
		cadastroOleoGasDataModel = null;
	}
	
	public void consultarCadastroOleoGas() {
		try {
			
			dataTableCadastroOleoGas.setFirst(0);
			dataTableCadastroOleoGas.setPage(1);
			
			cadastroOleoGasDataModel = new LazyDataModel<Caepog>() {

				private static final long serialVersionUID = 7717411591034117580L;

				@Override
				public List<Caepog> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
					List<Caepog> cadastros = null;
					
					try {
						
						cadastros = caepogService.listarPorCriteriaDemanda(requerenteFiltro, empreendimentoFiltro, caepogCampoSelecionadoFiltro, 
								municipioSelecionadoFiltro, numCaepogFiltro, caepogTipoStatusSelecionadoFiltro, periodoInicioFiltro, periodoFimFiltro, first, pageSize);
					} catch (Exception e) {
						throw Util.capturarException(e, Util.SEIA_EXCEPTION);
					}
					
					return cadastros;
				}
			};
			
			cadastroOleoGasDataModel.setRowCount(
					caepogService.count(requerenteFiltro, empreendimentoFiltro, caepogCampoSelecionadoFiltro, 
							municipioSelecionadoFiltro, numCaepogFiltro, caepogTipoStatusSelecionadoFiltro, periodoInicioFiltro, periodoFimFiltro));
			
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void preparaNovaLocGeo(Empreendimento emp, LocalizacaoGeografica locGeo, Object objLoc, String idUpdate, boolean shapeTipoPonto) {
		locGeoController.limparLocalizacaoGeografSelecionada();
		locGeoController.setEditandoVertice(false);
		locGeoController.setTipoSecaoGeometrica(ValidacaoShapeEnum.POLIGONO.getId());
		locGeoController.setEmpreendimento(emp);
		locGeoController.setLocalizacaoGeograficaSelecionada(locGeo);
		locGeoController.setObjetoLocalizacao(objLoc);
		locGeoController.setIdDoComponenteParaSerAtualizado(idUpdate);
		locGeoController.setShapeTipoPonto(shapeTipoPonto);
		locGeoController.carregarTela();
	}
	
	public boolean salvarCaepog() {
		try {
			
			caepog.setIdeEmpreendimento(empreendimento);
			caepog.setIdePessoaRequerente(requerente);
			caepog.setIdePessoaFisicaCadastro(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			caepog.setIndExcluidoCaepog(false);
			
			if(Util.isNullOuVazio(caepog.getDtcCriacao())) {
				caepog.setDtcCriacao(new Date());
			}
			
			if(isCaepogValidado()) {
				
				caepogService.salvarCaepog(caepog);
				
				if(Util.isNullOuVazio(caepogStatusService.listarTodosStatusPorCaepog(caepog))) {
					salvarStatus(CaepogTipoStatusEnum.CADASTRO_INCOMPLETO);
					JsfUtil.addSuccessMessage("Atividade cadastrada com sucesso!");
				}
				
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Houve um erro!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean isCaepogValidado() {
		if(Util.isNullOuVazio(caepog.getIdePessoaRequerente())) {
			JsfUtil.addErrorMessage("Favor selecionar o requerente");
			return false;
		} else if(Util.isNullOuVazio(caepog.getIdeEmpreendimento())) {
			JsfUtil.addErrorMessage("Favor selecionar o empreendimento");
			return false;
		} else {
			return true;
		}
	}
	
	public void editarCaepog() {
		try {
			carregaCollections();
			
			ContextoUtil.getContexto().setCaepog(caepog);
			FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-oleo-gas/cadastroCaepog.xhtml");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirCaepog() {
		try {
			caepogService.excluir(caepog);
			JsfUtil.addSuccessMessage("Atividade excluída com sucesso!");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void finalizaAtividade() {
		try {
	
			CaepogStatus ultimoStatusByCaepog = caepogStatusService.getUltimoStatusByCaepog(caepog);
			
			if(!Util.isNullOuVazio(ultimoStatusByCaepog)) {
				
				if(!Util.isNullOuVazio(ultimoStatusByCaepog.getIdeCaepogTipoStatus())) {
				
					CaepogTipoStatus caepogTipoStatus = ultimoStatusByCaepog.getIdeCaepogTipoStatus();
					
					/**
					 * CADASTRO INCOMPLETO
					 */
					if(caepogTipoStatus.getIdeCaepogTipoStatus() == CaepogTipoStatusEnum.CADASTRO_INCOMPLETO.getId()){
									
						if(possuiPendenciaLocalizacao() || isPossuiPendenciaEnvio()) {
							salvarStatus(CaepogTipoStatusEnum.CADASTRO_INCOMPLETO);
							
							caepog.setCadastroIncompleto(true);
							ContextoUtil.getContexto().setCaepog(caepog);
							FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-oleo-gas/consulta.xhtml");
							
						} else {
							salvarStatus(CaepogTipoStatusEnum.CADASTRO_COMPLETO);
							
							if(Util.isNullOuVazio(caepog.getNumCaepog())) {
								
								caepog.setNumCaepog(gerarNumeroCaepog());
								caepogService.salvarCaepog(caepog);
							}
							
							salvarStatus(CaepogTipoStatusEnum.AGUARDANDO_VALIDACAO);
							
							caepog.setAguardandoValidacao(true);
							ContextoUtil.getContexto().setCaepog(caepog);
							
							FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-oleo-gas/consulta.xhtml");
						}
						
					/**
					 * AGUARDANDO VALIDAÇÃO
					 */
					} else if(caepogTipoStatus.getIdeCaepogTipoStatus() == CaepogTipoStatusEnum.AGUARDANDO_VALIDACAO.getId()) {
						
						if(isUsuarioExterno()) {
							salvarStatus(CaepogTipoStatusEnum.AGUARDANDO_VALIDACAO);
							
							caepog.setAguardandoValidacao(true);
							ContextoUtil.getContexto().setCaepog(caepog);
							FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-oleo-gas/consulta.xhtml");
							
						} else if(!caepog.isValidando()) {
							salvarStatus(CaepogTipoStatusEnum.AGUARDANDO_VALIDACAO);
							
							caepog.setAguardandoValidacao(true);
							ContextoUtil.getContexto().setCaepog(caepog);
							FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-oleo-gas/consulta.xhtml");
							
						} else if(possuiPendenciaLocalizacao() || (isPossuiPendenciaValidacao() && caepog.isValidando())) {
							salvarStatus(CaepogTipoStatusEnum.PENDENCIA_VALIDACAO);
							RequestContext.getCurrentInstance().execute("confirPendenciaValidacao.show()");
							
							caepog.setPendenciaValidacao(true);
							ContextoUtil.getContexto().setCaepog(caepog);
							
						} else if(caepog.isValidando()) {				
							salvarStatus(CaepogTipoStatusEnum.VALIDADO);
							enviarEmailStatusValidado();
							
							caepog.setValidado(true);
							ContextoUtil.getContexto().setCaepog(caepog);
							FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-oleo-gas/consulta.xhtml");
						}
						
					/**
					 * PENDÊNCIA DE VALIDAÇÃO
					 */
					} else if(caepogTipoStatus.getIdeCaepogTipoStatus() == CaepogTipoStatusEnum.PENDENCIA_VALIDACAO.getId()) {
						
						if(!possuiPendenciaLocalizacao() && isUsuarioExterno()) {
							salvarStatus(CaepogTipoStatusEnum.AGUARDANDO_VALIDACAO);
							
							caepog.setAguardandoValidacao(true);
							ContextoUtil.getContexto().setCaepog(caepog);
							FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-oleo-gas/consulta.xhtml");
							
						} else {
							if(possuiPendenciaLocalizacao() || isPossuiPendenciaValidacao()) {
								salvarStatus(CaepogTipoStatusEnum.PENDENCIA_VALIDACAO);
								RequestContext.getCurrentInstance().execute("confirPendenciaValidacao.show()");
								
								caepog.setPendenciaValidacao(true);
								ContextoUtil.getContexto().setCaepog(caepog);
							} else {
								salvarStatus(CaepogTipoStatusEnum.VALIDADO);
								enviarEmailStatusValidado();
								
								caepog.setValidado(true);
								ContextoUtil.getContexto().setCaepog(caepog);
								FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-oleo-gas/consulta.xhtml");
							}
						}
					} 
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar finalizar o cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isUsuarioExterno() {
		if(ContextoUtil.getContexto() != null && ContextoUtil.getContexto().getUsuarioLogado() != null) {
			return ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno();
		}
		
		return false;
	}
	
	private boolean isPossuiPendenciaEnvio(){
		return isPendenciaEnvio(documentosCampo)||isPendenciaEnvio(documentosLocacao)||isPendenciaEnvio(documentosPoco)||isPendenciaEnvio(documentosAdicionais);
	}
	
	private boolean isPendenciaEnvio(List<CaepogDocumentoApensadoDTO> documentos){
		
		for(CaepogDocumentoApensadoDTO caepogDocumento  : documentos){
			if(Util.isNullOuVazio(caepogDocumento.getCaepogDocumentoApensado())) {
				return true;
			} else if(!caepogDocumento.getCaepogDocumentoApensado().isExisteArquivo()) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isPossuiPendenciaValidacao(){
		return isPendenciaValidacacao(documentosCampo) 
				|| isPendenciaValidacacao(documentosLocacao) 
				|| isPendenciaValidacacao(documentosPoco)
				|| isPendenciaValidacacao(documentosAdicionais);
	}
	
	private boolean isPendenciaValidacacao(List<CaepogDocumentoApensadoDTO> documentos){	
		
		for(CaepogDocumentoApensadoDTO caepogDocumento  : documentos){
			
			if(Util.isNullOuVazio(caepogDocumento.getCaepogDocumentoApensado())) {
				return true;
				
			} else if(Util.isNullOuVazio(caepogDocumento.getCaepogDocumentoApensado().getDtcValidadoCaepogDocumento()) ||
					Util.isNullOuVazio(caepogDocumento.getCaepogDocumentoApensado().getIdePessoaFisicaValidacao())) {
				
				return true;
			}
		}
			
		return false;
	}
	
	private void salvarStatus(CaepogTipoStatusEnum cts) {
		CaepogStatus status = new CaepogStatus();
		status.setDtcCaepogStatus(new Date());
		status.setIdeCaepog(caepog);
		status.setIdeCaepogTipoStatus(new CaepogTipoStatus(cts));
		status.setIdePessoaFisica(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		caepogStatusService.salvarStatus(status);
	}
	
	public String gerarNumeroCaepog() throws Exception {
		StringBuilder numeroCaepog = new StringBuilder();
		
		String ultimoNumero = caepogService.getUltimoNumeroCaepog();
		
		if(Util.isNullOuVazio(ultimoNumero)) {
			ultimoNumero = String.valueOf(1);
		} else {
			ultimoNumero = String.valueOf(Integer.parseInt(ultimoNumero) + 1);
		}
		
		numeroCaepog  
			.append(new SimpleDateFormat("yyyy").format(new Date()))
			.append('.');
		
		numeroCaepog
			.append("001")
		    .append('.');
	
		numeroCaepog 
			.append(Util.lpad(ultimoNumero, '0', 6))
			.append('/');
			
		numeroCaepog  
			.append("INEMA")
			.append("/CPP");
		
		return numeroCaepog.toString();
	}

	//CARREGA OS DADOS DOS CAMPOS, LOCAÇÕES E POÇOS
	private void carregaCollections() {
		caepog.setCaepogDefinicaoCampoCollection(caepogDefinicaoCampoService.listarPorCaepog(caepog));
					
		if(!Util.isNullOuVazio(caepog.getCaepogDefinicaoCampoCollection())){
			
			for (CaepogDefinicaoCampo cdc : caepog.getCaepogDefinicaoCampoCollection()) {
				
				cdc.setCaepogLocacaoCollection(caepogLocacaoService.listarPorCaepogDefinicaoCampo(cdc));
				
				for (CaepogLocacao cl : cdc.getCaepogLocacaoCollection()) {
					
					cl.setCaepogPocoCollection(caepogPocoService.listarPorCaepogLocacao(cl));
					
					for (CaepogPoco cp : cl.getCaepogPocoCollection()) {
						
						cp.setCaepogFasePerfuracaoCollection(caepogPocoService.listarPorCaepogPoco(cp));
					}
				}
			}
		}
	}
	
	public boolean isRenderizaCertificado(Caepog c) {
		if(!Util.isNullOuVazio(c) 
				&& !Util.isNullOuVazio(c.getIdeUltimoStatus()) 
				&& !Util.isNullOuVazio(c.getIdeUltimoStatus().getIdeCaepogTipoStatus())
				&& !Util.isNullOuVazio(c.getIdeUltimoStatus().getIdeCaepogTipoStatus().getIdeCaepogTipoStatus())) {
			
			if(c.getIdeUltimoStatus().getIdeCaepogTipoStatus().getIdeCaepogTipoStatus() == CaepogTipoStatusEnum.VALIDADO.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isRenderizaRelatorio(Caepog c) {
		if(!Util.isNullOuVazio(c) 
				&& !Util.isNullOuVazio(c.getIdeUltimoStatus()) 
				&& !Util.isNullOuVazio(c.getIdeUltimoStatus().getIdeCaepogTipoStatus())
				&& !Util.isNullOuVazio(c.getIdeUltimoStatus().getIdeCaepogTipoStatus().getIdeCaepogTipoStatus())) {
			
			if(c.getIdeUltimoStatus().getIdeCaepogTipoStatus().getIdeCaepogTipoStatus() != CaepogTipoStatusEnum.CADASTRO_INCOMPLETO.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isRenderedValidacao(Caepog c) {
		if(!Util.isNullOuVazio(c) && !Util.isNullOuVazio(c.getIdeUltimoStatus())) {
			
			CaepogTipoStatus caepogTipoStatus = c.getIdeUltimoStatus().getIdeCaepogTipoStatus();
			
			return isCaepogTipoStatusPendenciaOuAguardandoValidacao(caepogTipoStatus);
		}
		
		return false;
	}

	private boolean isCaepogTipoStatusPendenciaOuAguardandoValidacao(CaepogTipoStatus caepogTipoStatus) {
		return caepogTipoStatus.getIdeCaepogTipoStatus().equals(CaepogTipoStatusEnum.AGUARDANDO_VALIDACAO.getId()) ||
				caepogTipoStatus.getIdeCaepogTipoStatus().equals(CaepogTipoStatusEnum.PENDENCIA_VALIDACAO.getId());
	}
	
	public boolean isRenderedBotaoEditarTelaConsulta(Caepog c) {
		
		if(!Util.isNullOuVazio(c) 
			&& !Util.isNullOuVazio(c.getIdeUltimoStatus()) 
			&& !Util.isNullOuVazio(c.getIdeUltimoStatus().getIdeCaepogTipoStatus())
			&& !Util.isNullOuVazio(c.getIdeUltimoStatus().getIdeCaepogTipoStatus().getIdeCaepogTipoStatus())) {
		
			int idUltimoStatus = c.getIdeUltimoStatus().getIdeCaepogTipoStatus().getIdeCaepogTipoStatus();
		
			if(idUltimoStatus != CaepogTipoStatusEnum.VALIDADO.getId()) {
					return true;
				}
		}
		
		return false;
	}
	
	public String getDataHoje() {
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy");
		return formatarDate.format(data);
	}
	
	public void carregaHistoricoComunicacao() {
		try {
			listaCaepogComunicacoes = caepogComunicacaoService.listarComunicacaoPorCaepog(caepog);
			listaCaepogStatus = caepogStatusService.listarTodosStatusPorCaepog(caepog);
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/*************************
	/*						 *
	//XXX: ABA DADOS BASICOS *
	/* 			 			 *
	/*************************/
	
	public void selecionarRequerente(Pessoa p) {
		
		try {
			if(pessoaFacade.isPessoaCadastroCompleto(p)){
				
				if(isTelaConsulta) { //TELA DE CONSULTA
					requerenteFiltro = p;
					isTelaConsulta = false;
					Html.atualizar("formOleoGas:requerenteFiltro");
					
				} else if(tabAtual == 0) { //ABA DADOS BÁSICOS
					requerente = p;
					empreendimento = null;
					representanteLegal = null;
					responsavelEmpreendimento = null;
					
					empreendimentoDialogFiltro = new Empreendimento();
					empreendimentoDataModel = null;
					
					Html.atualizar("tabViewOleoGas", "formBotoesNavegacao", "formDialogEmpreendimento");
					
				} else if(tabAtual == 3) { //ABA POÇO
					caepogFasePerfuracao.setIdePessoaJuridicaDestino(p.getPessoaJuridica());
				}
				
				Html.esconder("dialogRequerente");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		
	}
	
	public void buscarEmpreendimentoPorRequerente() {
		try {
			empreendimentoDataModel = new LazyDataModel<VwConsultaEmpreendimento>() {

				private static final long serialVersionUID = 7717411591034117580L;

				@Override
				public List<VwConsultaEmpreendimento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
					List<VwConsultaEmpreendimento> empreendimentos = null;
					
					try {
						setPageSize(pageSize);
						empreendimentos = empreendimentoService.listarPorCriteriaDemanda(null, requerente, empreendimentoDialogFiltro.getNomEmpreendimento(), first, pageSize);
					} catch (Exception e) {
						throw Util.capturarException(e, Util.SEIA_EXCEPTION);
					}
					
					return empreendimentos;
				}
			};
			
			empreendimentoDataModel.setRowCount(empreendimentoService.count(null, requerente, empreendimentoDialogFiltro.getNomEmpreendimento()));
			
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarDadosBasicos(Empreendimento emp) {
		try {
			//REQUERENTE
			EnderecoPessoa ep = enderecoPessoaService.buscarEnderecoPorPessoa(requerente);
			
			if(!Util.isNullOuVazio(ep)) {
				requerente.setEndereco(ep.getIdeEndereco());
			}
			
			representanteLegal = null;
			
			if(!Util.isNullOuVazio(requerente.getPessoaJuridica())) {
				List<RepresentanteLegal> rl = representanteLegalService.listarRepresentanteLegalPorPessoaJuridica(requerente.getPessoaJuridica());
				
				if(!Util.isNullOuVazio(rl)) {
					representanteLegal = rl.get(0);
				}
			}
			
			//EMPREENDIMENTO
			empreendimento = empreendimentoService.carregarPorIdComMunicipio(emp.getIdeEmpreendimento());
			
			if (empreendimentoService.isEmpreendimentoValido(empreendimento)) {
				
			

 				if(Util.isNullOuVazio(empreendimento.getEndereco())){
					JsfUtil.addWarnMessage("Complete o cadastro do empreendimento.");
				}else{
					responsavelEmpreendimento = null;
					List<ResponsavelEmpreendimento> rp = (List<ResponsavelEmpreendimento>) responsavelEmpreendimentoService.listarResponsaveisPorEmpreendimento(empreendimento);
					
					if(!Util.isNullOuVazio(rp)) {
						responsavelEmpreendimento = rp.get(0);
						
						if(RequestContext.getCurrentInstance() != null) {
							Html.esconder("dialogEmpreendimento");
							Html.atualizar("tabViewOleoGas:formAbaDadosBasicos","formBotoesNavegacao");
						}
					} else {
						JsfUtil.addWarnMessage("Não existe Responsável Técnico cadastrado para o Empreendimento selecionado. Favor realizar o cadastro!");
					}				
				}
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void onCloseDialogRequerente(CloseEvent event) {
		EmpreendimentoController ec = (EmpreendimentoController) SessaoUtil.recuperarManagedBean("#{empreendimentoController}", EmpreendimentoController.class);
		ec.setFlagRequerenteDestino(false);
	}
	
	/**************************
	/*						  *
	//XXX: ABA DADOS DO CAMPO *
	/* 			 			  *
	/**************************/
	
	public void preparaNovoCampo() {
		isEditando = false;
		caepogDefinicaoCampo = new CaepogDefinicaoCampo();
		caepogDefinicaoCampo.setIdeCaepog(caepog);
		
		RequestContext.getCurrentInstance().execute("dialogCampo.show();");
	}
	
	public void novaLocGeoCampo() {
		if(Util.isNullOuVazio(caepogDefinicaoCampo.getIdeLocalizacaoGeografica())) {
			caepogDefinicaoCampo.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		
		preparaNovaLocGeo(empreendimento, caepogDefinicaoCampo.getIdeLocalizacaoGeografica(), caepogDefinicaoCampo, "formDialogCampo:panelGridDataTable", false);
	}
	
	public void salvarCampo() {
		try {
			String geometria = "";
			if(!Util.isNullOuVazio(caepogDefinicaoCampo.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())) {
				geometria = validacaoGeoSeiaService.buscarGeometriaShape(caepogDefinicaoCampo.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
			
			if(!Util.isNullOuVazio(geometria) && !geometria.isEmpty()) {
				if(isEditando) {
					caepogDefinicaoCampoService.atualizarValidando(caepogDefinicaoCampo);
					JsfUtil.addSuccessMessage("Campo atualizado com sucesso!");
					isEditando = false;
				} else {
					caepogDefinicaoCampoService.salvarValidando(caepogDefinicaoCampo);
					caepog.getCaepogDefinicaoCampoCollection().add(caepogDefinicaoCampo);
					JsfUtil.addSuccessMessage("Campo salvo com sucesso!");
				}
				
				RequestContext.getCurrentInstance().execute("dialogCampo.hide();");
			}else {
				MensagemUtil.alerta("É necessário realizar upload dos arquivos (.dbf, .shp, .shx) do Shape referente a localização do Campo.");
			}
			
		} catch (SeiaValidacaoRuntimeException e) {
			if(e.getMessage().equals("Número do processo inválido.")) {
				JsfUtil.addErrorMessage(e.getMessage());
			} else {
				JsfUtil.addWarnMessage(e.getMessage());
			}
			
		} catch (EJBException e) {
			if(e.getCause() instanceof ConstraintViolationException ) JsfUtil.addWarnMessage("O campo selecionado já foi cadastrado.");
			caepogDefinicaoCampo.setIdeCaepogDefinicaoCampo(null);
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar salvar o campo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void excluirCampo() {
		try {
			List<CaepogLocacao> list = caepogLocacaoService.listarPorCaepogDefinicaoCampo(caepogDefinicaoCampo);
			
			if(Util.isNullOuVazio(list)) {
				caepogDefinicaoCampoService.excluir(caepogDefinicaoCampo);
				
				caepog.getCaepogDefinicaoCampoCollection().remove(caepogDefinicaoCampo);
				JsfUtil.addSuccessMessage("Campo excluído com sucesso!");
			} else {
				JsfUtil.addErrorMessage("O Campo não pode ser excluída pois existem locações cadastradas para ele.");
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar excluir o campo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void onCloseDialogCampo(CloseEvent event) {
		
		try {
			carregaCollections();
			
			if(RequestContext.getCurrentInstance() != null) {
				RequestContext.getCurrentInstance().addPartialUpdateTarget("tabViewOleoGas");
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Houve um erro!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	//Melhoria #7652 - Permiti apenas um campo por cadastro
	public boolean isExisteCampoCadastrado() {
		
		if(!Util.isNullOuVazio(caepog) && !Util.isNullOuVazio(caepog.getCaepogDefinicaoCampoCollection())) {
			return true;
		} else {
			return false;
		}
	}
	
	/********************
	/*					*
	//XXX: ABA LOCACAO  *
	/* 			 		*
	/********************/
	
	public void preparaNovaLocacao() {
		
		try {
			
			if(!isEditando) {
				caepogLocacao = new CaepogLocacao();
			}
			
			caepogLocacao.setIdeCaepogDefinicaoCampo(caepogDefinicaoCampo);
			listImovel = imovelService.listarImovelRuralPorEmpreendimento(empreendimento);
			
			RequestContext.getCurrentInstance().addPartialUpdateTarget("_dialogLocacao");
			RequestContext.getCurrentInstance().execute("dialogLocacao.show();");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Houve um erro!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void novaLocGeoLocacao() {
		if(Util.isNullOuVazio(caepogLocacao.getIdeLocalizacaoGeografica())) {
			caepogLocacao.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		
		preparaNovaLocGeo(empreendimento, caepogLocacao.getIdeLocalizacaoGeografica(), caepogLocacao, "formDialogLocacao:panelGridDataTable", false);
	}
	
	public void salvarLocacao() {
		
		try {
			if(isDialogLocacaoValidado()) {
				
				
				String geometria = "";
				if(!Util.isNullOuVazio(caepogLocacao.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())) {
					geometria = validacaoGeoSeiaService.buscarGeometriaShape(caepogLocacao.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}
				
				if(!Util.isNullOuVazio(geometria) && !geometria.isEmpty()) {
					caepogLocacaoService.salvar(caepogLocacao);
					
					if(isEditando) {
						isEditando = false;
						JsfUtil.addSuccessMessage("Locação atualizada com sucesso!");
					} else {
						if(Util.isNullOuVazio(caepogDefinicaoCampo.getCaepogLocacaoCollection())) {
							caepogDefinicaoCampo.setCaepogLocacaoCollection(new ArrayList<CaepogLocacao>());
						}
						
						caepogDefinicaoCampo.getCaepogLocacaoCollection().add(caepogLocacao);
						JsfUtil.addSuccessMessage("Locação salva com sucesso!");
					}
					
					RequestContext.getCurrentInstance().execute("dialogLocacao.hide();");
				}else {
					MensagemUtil.alerta("É necessário realizar upload dos arquivos (.dbf, .shp, .shx) do Shape referente a localização da Locação.");

				}
				
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar salvar a locação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean isDialogLocacaoValidado() throws Exception {
		
		if(Util.isNullOuVazio(caepogLocacao.getPrefixPocoCaepogLocacao())) {
			JsfUtil.addErrorMessage("Favor preencher o prefixo do poço");
			return false;
		} else if(Util.isNullOuVazio(caepogLocacao.getNumProcessoCaepogLocacao())) {
			JsfUtil.addErrorMessage("Favor preencher o número do processo");
			return false;
		} else if(Util.isNullOuVazio(caepogLocacao.getIdeLocalizacaoGeografica())) {
			JsfUtil.addErrorMessage("Favor inserir os shapes da localização geográfica");
			return false;
		} else if(Util.isNullOuVazio(caepogLocacao.getAreaLocacaoCaepogLocacao())) {
			JsfUtil.addErrorMessage("Favor preencher a Área da locação");
			return false;
		} else if(!verificarProcessoExistente(caepogLocacao.getNumProcessoCaepogLocacao())) {
			JsfUtil.addErrorMessage("O número do processo é invalido ou não está concluído.");
			return false;
		} else {
			return true;
		}
	}
	
	public boolean verificarProcessoExistente(String numProcesso) throws Exception{
		try {
			
			numProcesso = numProcesso.replace(String.valueOf((char) 160), " ").trim();
			
			Processo processo = processoService.buscarProcessoPorCriteria(numProcesso);
			
			if (Util.isNullOuVazio(processo)) {
				ProcessoExterno processoExterno = processoExternoService.buscarProcessoExternoByNumeroProcesso(numProcesso);
				
				if (Util.isNullOuVazio(processoExterno)) {
					return false;
				} else {
					return true;
				}
			} else {
				//#7660 - Melhoria - Validar se processo está concluído
				if(processoService.isProcessoNoStatus(processo.getIdeProcesso(), StatusFluxoEnum.CONCLUIDO.getStatus())) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("O número do processo é invalido ou não está concluído.");
			throw e;
		}
	}

	public void excluirLocacao() {
		try {
			
			List<CaepogPoco> list = caepogPocoService.listarPorCaepogLocacao(caepogLocacao);
			
			if(Util.isNullOuVazio(list)) {
				
				caepogLocacaoService.excluir(caepogLocacao);
				
				caepogDefinicaoCampo.getCaepogLocacaoCollection().remove(caepogLocacao);
				JsfUtil.addSuccessMessage("Locação excluída com sucesso!");
				
				if(RequestContext.getCurrentInstance() != null) {
					RequestContext.getCurrentInstance().addPartialUpdateTarget("tabViewOleoGas:formAbaLocacao");
				}
			} else {
				JsfUtil.addErrorMessage("A Locação não pode ser excluída pois existem poços cadastrados para ela.");
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar excluir a locação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void onCloseDialogLocacao(CloseEvent event) {
		
		try {
			caepogDefinicaoCampo.setCaepogLocacaoCollection(caepogLocacaoService.listarPorCaepogDefinicaoCampo(caepogDefinicaoCampo));
			
			for (CaepogLocacao cl : caepogDefinicaoCampo.getCaepogLocacaoCollection()) {
				
				cl.setCaepogPocoCollection(caepogPocoService.listarPorCaepogLocacao(cl));
				
				for (CaepogPoco cp : cl.getCaepogPocoCollection()) {
					
					cp.setCaepogFasePerfuracaoCollection(caepogPocoService.listarPorCaepogPoco(cp));
				}
			}
			
			if(RequestContext.getCurrentInstance() != null) {
				RequestContext.getCurrentInstance().addPartialUpdateTarget("tabViewOleoGas");
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Houve um erro!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/****************
	/*				*
	//XXX: ABA POCO *
	/* 			 	*
	/****************/
	
	public boolean isAbaResiduoDesabilitada(){
		return isPocoNaoSelecionado() || !isPocoNovo(); 
	}

	public boolean isPocoNaoSelecionado() {
		return Util.isNullOuVazio(isPocoNovo());
	}
	
	private Boolean isPocoNovo() {
		return caepogPoco.getIndNovoCaepogPoco();
	}
	
	public void preparaNovoPoco(){
		try {
			tabAtualPoco = 0;

			if(!isEditando) {
				caepogPoco = new CaepogPoco();
				listCaepogObjetivoAtividadeSelecionados = new ArrayList<CaepogObjetivoAtividade>();
				listCaepogFormacaoGeologicaSelecionados = new ArrayList<CaepogFormacaoGeologica>();
			} else {
				listCaepogObjetivoAtividadeSelecionados = caepogPocoService.listarObjetivosPorPoco(caepogPoco);
				listCaepogFormacaoGeologicaSelecionados = caepogPocoService.listarFormacaoGeologicaPorPoco(caepogPoco);
				
				for (CaepogFasePerfuracao cfp : caepogPoco.getCaepogFasePerfuracaoCollection()) {
					
					cfp.setProfundidadePerfurada(caepogPocoService.carregarFaseVariavelPorPerfuracao(cfp, new CaepogVariavel(CaepogVariavelEnum.PROFUNDIDADE_PERFURADA)));
					cfp.setExtensao(caepogPocoService.carregarFaseVariavelPorPerfuracao(cfp, new CaepogVariavel(CaepogVariavelEnum.EXTENSAO)));
					cfp.setDiametro(caepogPocoService.carregarFaseVariavelPorPerfuracao(cfp, new CaepogVariavel(CaepogVariavelEnum.DIAMETRO)));
					cfp.setTph(caepogPocoService.carregarFaseVariavelPorPerfuracao(cfp, new CaepogVariavel(CaepogVariavelEnum.TPH)));
					cfp.setVolumeEstimado(caepogPocoService.carregarFaseVariavelPorPerfuracao(cfp, new CaepogVariavel(CaepogVariavelEnum.VOLUME_ESTIMADO)));
					cfp.setSalinidade(caepogPocoService.carregarFaseVariavelPorPerfuracao(cfp, new CaepogVariavel(CaepogVariavelEnum.SALINIDADE)));
					cfp.setConfirmadoAbaPoco(true);
					cfp.setConfirmadoAbaResiduo(true);
				}
			}
			
			caepogPoco.setIdeCaepogLocacao(caepogLocacao);
			
			listCaepogObjetivoAtividade = caepogPocoService.listarTodosObjetivoAtividade();
			listCaepogFormacaoGeologica = caepogPocoService.listarTodosFormacaoGeologica();
			
			listCaepogTipoPoco = caepogPocoService.listarTodosTipoPoco();
			listCaepogTipoResiduo = caepogPocoService.listarTodosTipoResiduo();
			listCaepogClasseResiduo = caepogPocoService.listarTodosClasseResiduo();
			
			if(Util.isNullOuVazio(caepogPoco.getCaepogFasePerfuracaoCollection())){
				caepogPoco.setCaepogFasePerfuracaoCollection(new ArrayList<CaepogFasePerfuracao>());
			}
			
			
			isEditando = false;
			RequestContext.getCurrentInstance().addPartialUpdateTarget("_dialogPoco");
			RequestContext.getCurrentInstance().execute("dialogPoco.show();");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void novaLocGeoPoco() {
		if (Util.isNullOuVazio(caepogPoco.getIdeLocalizacaoGeografica())) {
			caepogPoco.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		
		preparaNovaLocGeo(empreendimento, caepogPoco.getIdeLocalizacaoGeografica(), caepogPoco, "formDialogPoco:tabViewDialogPoco:panelGroupLocalizacao", true);
	}
	
	public void salvarPoco() {
		
		try {
			if(isDialogPocoValidado()) {
				
				String geometria = "";
				if(!Util.isNullOuVazio(caepogPoco.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())) {
					geometria = validacaoGeoSeiaService.buscarGeometriaShape(caepogPoco.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}
				
				if(!Util.isNullOuVazio(geometria) && !geometria.isEmpty()) {
					caepogPoco.setIdeCaepogLocacao(caepogLocacao);
					caepogPocoService.salvar(caepogPoco);
					
					salvaObjetivoAtividadePoco();
					salvaFormacaoGeologicaPoco();
					salvarFasePerfuracao();
					
					if(isEditando) {
						JsfUtil.addSuccessMessage("Poço atualizado com sucesso!");
					} else {
						JsfUtil.addSuccessMessage("Poço salvo com sucesso!");
					}
					
					caepogLocacao.setCaepogPocoCollection(caepogPocoService.listarPorCaepogLocacao(caepogLocacao));
					
					for (CaepogPoco cp : caepogLocacao.getCaepogPocoCollection()) {
						cp.setCaepogFasePerfuracaoCollection(caepogPocoService.listarPorCaepogPoco(cp));
					}
					
					isEditando = false;
					RequestContext.getCurrentInstance().execute("dialogPoco.hide();");
					tabAtualPoco = 0;
				}else {
					MensagemUtil.alerta("É necessário realizar upload dos arquivos (.dbf, .shp, .shx) do Shape referente a localização do Poço.");
				}
				
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar poço!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void salvaObjetivoAtividadePoco()  {
		
		//Exclui as CaepogObjetivoAtividade antigas antes de salvar as novas
		if(!Util.isNullOuVazio(caepogPoco)) {
			caepogPocoService.excluirObjetivoAtividadePorPoco(caepogPoco);
		}
		
		//Salva as novas CaepogObjetivoAtividade
		for (CaepogObjetivoAtividade obj : listCaepogObjetivoAtividadeSelecionados) {
			
			CaepogObjetivoAtividadePoco oap = new CaepogObjetivoAtividadePoco();
			oap.setIdeCaepogObjetivoAtividadePocoPK(new CaepogObjetivoAtividadePocoPK(caepogPoco.getIdeCaepogPoco(), obj.getIdeCaepogObjetivoAtividade()));
			oap.setIdeCaepogPoco(caepogPoco);
			oap.setIdeCaepogObjetivoAtividade(obj);
			
			caepogPocoService.salvarObjetivoAtividadePoco(oap);
		}
	}
	
	private void salvaFormacaoGeologicaPoco() {
		
		//Exclui as CaepogFormacaoGeologicaPoco antigas antes de salvar as novas
		if(!Util.isNullOuVazio(caepogPoco)) {
			caepogPocoService.excluirCaepogFormacaoGeologicaPoco(caepogPoco);
		}
		
		//Salva as novas CaepogFormacaoGeologicaPoco
		for (CaepogFormacaoGeologica obj : listCaepogFormacaoGeologicaSelecionados) {
			
			CaepogFormacaoGeologicaPoco cfgp = new CaepogFormacaoGeologicaPoco();
			cfgp.setIdeCaepogFormacaoGeologicaPocoPK(new CaepogFormacaoGeologicaPocoPK(caepogPoco.getIdeCaepogPoco(), obj.getIdeCaepogFormacaoGeologica()));
			cfgp.setIdeCaepogPoco(caepogPoco);
			cfgp.setIdeCaepogFormacaoGeologica(obj);
			
			caepogPocoService.salvarFormacaoGeologicaPoco(cfgp);
		}
	}
	
	private boolean isDialogPocoValidado() throws Exception {
		boolean valido = true;
		if(isPocoNaoSelecionado()) {
			JsfUtil.addErrorMessage("Favor selecionar se o poço é existente ou novo");
			valido = false;
			tabAtualPoco = 0;
		} else {
			//POÇOS NOVOS
			if(isPocoNovo()) {
				if(!isAbaDadosDoPocoValida()){
					valido = false;
					tabAtualPoco = 0;
				} else {
					if(!isAbaResiduosGeradosValida()){
						valido = false;
					}
				}
			//POÇOS ANTIGOS
			} else {
				if(Util.isNullOuVazio(caepogPoco.getProfundidadeCaepogPoco())) {
					JsfUtil.addErrorMessage("Favor preencher a profundidade do poço");
					valido = false;
				}
				
				if(Util.isNullOuVazio(caepogPoco.getIdeLocalizacaoGeografica())) {
					JsfUtil.addErrorMessage("Favor inserir os shapes da localização geográfica");
					valido = false;
				}

				if(Util.isNullOuVazio(caepogPoco.getNomPocoCaepogPoco())) {
					JsfUtil.addErrorMessage("Favor preencher o campo Nome do poço");
					valido = false;
				}
			}
		}
		
		return valido;
	}
	
	private boolean isAbaDadosDoPocoValida() throws Exception{
		boolean valido = true;
		
		if(Util.isNullOuVazio(caepogPoco.getIdeCaepogTipoPoco())) {
			JsfUtil.addErrorMessage("Favor preencher a geometria do poço");
			valido = false;
		}
		
		if(Util.isNullOuVazio(caepogPoco.getIndAltPorteCaepogPoco())) {
			JsfUtil.addErrorMessage("Favor selecionar se o poço irá alterar o porte do campo");
			valido = false;
		}
		
		if(!Util.isNullOuVazio(caepogPoco.getIndAltPorteCaepogPoco()) && caepogPoco.getIndAltPorteCaepogPoco() && Util.isNullOuVazio(caepogPoco.getNumProcessoCaepogPoco())) {
			JsfUtil.addErrorMessage("Favor preencher o número da licença de alteração");
			valido = false;
		}
		
		if(!Util.isNullOuVazio(caepogPoco.getNumProcessoCaepogPoco()) && !verificarProcessoExistente(caepogPoco.getNumProcessoCaepogPoco())) {
			JsfUtil.addErrorMessage("O número do processo da licença de alteração é invalido ou não está concluído.");
			valido = false;
		}
		
		if(Util.isNullOuVazio(listCaepogObjetivoAtividadeSelecionados)) {
			JsfUtil.addErrorMessage("Favor selecionar ao menos um objetivo");
			valido = false;
		}
		
		if(Util.isNullOuVazio(listCaepogFormacaoGeologicaSelecionados)) {
			JsfUtil.addErrorMessage("Favor selecionar ao menos uma formação");
			valido = false;
		}
		
		if(Util.isNullOuVazio(caepogPoco.getIndSupVegetaCaepogPoco())) {
			JsfUtil.addErrorMessage("Favor selecionar se será necessário algum ato autorizativo");
			valido = false;
		}
		
		if(!Util.isNullOuVazio(caepogPoco.getIndSupVegetaCaepogPoco()) && caepogPoco.getIndSupVegetaCaepogPoco() 
				&& Util.isNullOuVazio(caepogPoco.getNumProcessoAsvCaepogPoco())) {
			
			JsfUtil.addErrorMessage("Favor preencher o número do processo da ASV");
			valido = false;
		}
		
		if(!Util.isNullOuVazio(caepogPoco.getNumProcessoAsvCaepogPoco()) && !verificarProcessoExistente(caepogPoco.getNumProcessoAsvCaepogPoco())){
			JsfUtil.addErrorMessage("O processo informado deve estar concluído.");
			valido = false;
		}
		
		if(Util.isNullOuVazio(caepogPoco.getIdeLocalizacaoGeografica())) {
			JsfUtil.addErrorMessage("Favor inserir os shapes da localização geográfica");
			valido = false;
		}
		
		if(Util.isNullOuVazio(caepogPoco.getCaepogFasePerfuracaoCollection())){
			JsfUtil.addErrorMessage("Favor adicionar a(s) fase(s) de perfuração(ões)");
			valido = false;
		} else {
			for(CaepogFasePerfuracao fasePerfuracao : caepogPoco.getCaepogFasePerfuracaoCollection()){
				if(!fasePerfuracao.isConfirmadoAbaPoco()){
					JsfUtil.addErrorMessage("Favor confirmar a fase de perfuração");
					valido = false;
					break;
				}
			}
		}
		
		if(Util.isNullOuVazio(caepogPoco.getNomPocoCaepogPoco())) {
			JsfUtil.addErrorMessage("Favor preencher o campo Nome do poço");
			valido = false;
		}
		
		return valido;
	}

	private boolean isAbaResiduosGeradosValida() {
		boolean valido = true;
		if(Util.isNullOuVazio(caepogPoco.getCascalhoResiduoCaepogPoco())) {
			JsfUtil.addErrorMessage("Favor preencher o cascalho gerado");
			valido = false;
		} 
		if(Util.isNullOuVazio(caepogPoco.getEmpolamentoCaepogPoco())) {
			JsfUtil.addErrorMessage("Favor preencher o coeficiente de empolamento");
			valido = false;
		}
		for(CaepogFasePerfuracao fasePerfuracao : caepogPoco.getCaepogFasePerfuracaoCollection()){
			if(!fasePerfuracao.isConfirmadoAbaResiduo()){
				JsfUtil.addErrorMessage("Favor confirmar a fase de perfuração");
				valido = false;
				break;
			}
		}
		return valido;
	}
	
	public void excluirPoco() {
		try {
			
			if (!Util.isNullOuVazio(caepogPoco)) {
				caepogPocoService.excluirObjetivoAtividadePorPoco(caepogPoco);
				
				caepogPocoService.excluirCaepogFormacaoGeologicaPoco(caepogPoco);
				
				if(!Util.isNullOuVazio(caepogPoco.getIdeLocalizacaoGeografica())) {
					LocalizacaoGeografica lg = caepogPoco.getIdeLocalizacaoGeografica();
					
					caepogPoco.setIdeLocalizacaoGeografica(null);
					caepogPocoService.salvar(caepogPoco);
					localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(lg);
				}
				
				if(!Util.isNullOuVazio(caepogPoco.getCaepogFasePerfuracaoCollection())) {
					for (CaepogFasePerfuracao cfp : caepogPoco.getCaepogFasePerfuracaoCollection()) {
						caepogPocoService.excluirFaseVariavelPorFasePerfuracao(cfp);
					}
				}
				
				caepogPocoService.excluirFasePerfuracaoPorPoco(caepogPoco);
				
				caepogPoco.setCaepogFasePerfuracaoCollection(null);
				
				caepogPocoService.excluir(caepogPoco);
				
				caepogLocacao.getCaepogPocoCollection().remove(caepogPoco);
				JsfUtil.addSuccessMessage("Poço excluído com sucesso!");
				
				if(RequestContext.getCurrentInstance() != null) {
					RequestContext.getCurrentInstance().addPartialUpdateTarget("tabViewOleoGas:formAbaPoco");
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar excluir o poço.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void incluirFasePerfuracao() {
		
		try {
			if(Util.isNullOuVazio(caepogPoco.getCaepogFasePerfuracaoCollection())) { 
				caepogFasePerfuracao = new CaepogFasePerfuracao( 1);
				caepogFasePerfuracao.setIdeCaepogPoco(caepogPoco);
				
				caepogFasePerfuracao.setProfundidadePerfurada(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.PROFUNDIDADE_PERFURADA)));
				caepogFasePerfuracao.setExtensao(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.EXTENSAO)));
				caepogFasePerfuracao.setDiametro(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.DIAMETRO)));
				caepogFasePerfuracao.setTph(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.TPH)));
				caepogFasePerfuracao.setVolumeEstimado(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.VOLUME_ESTIMADO)));
				caepogFasePerfuracao.setSalinidade(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.SALINIDADE)));
				
				caepogPoco.setCaepogFasePerfuracaoCollection(new ArrayList<CaepogFasePerfuracao>());
				caepogPoco.getCaepogFasePerfuracaoCollection().add(caepogFasePerfuracao);
			} else {
				caepogFasePerfuracao = new CaepogFasePerfuracao(new Integer(caepogPoco.getCaepogFasePerfuracaoCollection().size()+1));
				caepogFasePerfuracao.setIdeCaepogPoco(caepogPoco);
				
				caepogFasePerfuracao.setProfundidadePerfurada(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.PROFUNDIDADE_PERFURADA)));
				caepogFasePerfuracao.setExtensao(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.EXTENSAO)));
				caepogFasePerfuracao.setDiametro(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.DIAMETRO)));
				caepogFasePerfuracao.setTph(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.TPH)));
				caepogFasePerfuracao.setVolumeEstimado(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.VOLUME_ESTIMADO)));
				caepogFasePerfuracao.setSalinidade(new CaepogFaseVariavel(caepogFasePerfuracao, new CaepogVariavel(CaepogVariavelEnum.SALINIDADE)));
				
				caepogPoco.getCaepogFasePerfuracaoCollection().add(caepogFasePerfuracao);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void salvarFasePerfuracao() {
		try {
			//Exclui todas as CaepogFasePerfuracao antigas antes de salvar as novas
			List<CaepogFasePerfuracao> listCFP = caepogPocoService.listarPorCaepogPoco(caepogPoco);
			
			if(!Util.isNullOuVazio(listCFP)) {
				for (CaepogFasePerfuracao cfp : listCFP) {
					caepogPocoService.excluirFaseVariavelPorFasePerfuracao(cfp);
				}
			}
			
			//Salva as novas CaepogFasePerfuracao
			if(!Util.isNullOuVazio(caepogPoco.getCaepogFasePerfuracaoCollection())) {
				for (CaepogFasePerfuracao cfp : caepogPoco.getCaepogFasePerfuracaoCollection()) {
					
					caepogPocoService.salvarFasePerfuracao(cfp);
					
					cfp.getProfundidadePerfurada().setIdeCaepogFaseVariavelPK(new CaepogFaseVariavelPK(cfp.getIdeCaepogFasePerfuracao(), cfp.getProfundidadePerfurada().getIdeCaepogVariavel().getIdeCaepogVariavel()));
					cfp.getExtensao().setIdeCaepogFaseVariavelPK(new CaepogFaseVariavelPK(cfp.getIdeCaepogFasePerfuracao(), cfp.getExtensao().getIdeCaepogVariavel().getIdeCaepogVariavel()));
					cfp.getDiametro().setIdeCaepogFaseVariavelPK(new CaepogFaseVariavelPK(cfp.getIdeCaepogFasePerfuracao(), cfp.getDiametro().getIdeCaepogVariavel().getIdeCaepogVariavel()));
					cfp.getTph().setIdeCaepogFaseVariavelPK(new CaepogFaseVariavelPK(cfp.getIdeCaepogFasePerfuracao(), cfp.getTph().getIdeCaepogVariavel().getIdeCaepogVariavel()));
					cfp.getVolumeEstimado().setIdeCaepogFaseVariavelPK(new CaepogFaseVariavelPK(cfp.getIdeCaepogFasePerfuracao(), cfp.getVolumeEstimado().getIdeCaepogVariavel().getIdeCaepogVariavel()));
					cfp.getSalinidade().setIdeCaepogFaseVariavelPK(new CaepogFaseVariavelPK(cfp.getIdeCaepogFasePerfuracao(), cfp.getSalinidade().getIdeCaepogVariavel().getIdeCaepogVariavel()));
					
					caepogPocoService.salvarFaseVariavel(cfp.getProfundidadePerfurada());
					caepogPocoService.salvarFaseVariavel(cfp.getExtensao());
					caepogPocoService.salvarFaseVariavel(cfp.getDiametro());
					caepogPocoService.salvarFaseVariavel(cfp.getTph());
					caepogPocoService.salvarFaseVariavel(cfp.getVolumeEstimado());
					caepogPocoService.salvarFaseVariavel(cfp.getSalinidade());
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Houve um erro!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void excluirFasePerfuracao() {
		try {
			if(!Util.isNullOuVazio(caepogFasePerfuracao)) { 
				caepogPocoService.excluirFaseVariavelPorFasePerfuracao(caepogFasePerfuracao);
				caepogPocoService.excluirFasePerfuracao(caepogFasePerfuracao);
			}
			
			caepogPoco.getCaepogFasePerfuracaoCollection().remove(caepogFasePerfuracao);
			JsfUtil.addSuccessMessage("Perfuração de poço excluída com sucesso!");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Houve um erro!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void confirmarFasePerfuracaoDadoPoco(CaepogFasePerfuracao cfp){
		
		caepogFasePerfuracao = cfp;
		
		if(isFasePerfuracaoDadoPocoValida()){
			caepogFasePerfuracao.setConfirmadoAbaPoco(true);
			JsfUtil.addSuccessMessage("Fase da perfuração salva com sucesso!");
		}
	}
	
	private boolean isFasePerfuracaoDadoPocoValida() {
		boolean valido = true;
		
		if(Util.isNullOuVazio(caepogFasePerfuracao.getProfundidadePerfurada().getNumValorFaseVariavel())){
			JsfUtil.addErrorMessage("Favor preencher a Profundidade Perfurada");
			valido = false;
		}
		
		if(Util.isNullOuVazio(caepogFasePerfuracao.getExtensao().getNumValorFaseVariavel())){
			JsfUtil.addErrorMessage("Favor preencher a Extensão");
			valido = false;
		}
		
		if(Util.isNullOuVazio(caepogFasePerfuracao.getDiametro().getNumValorFaseVariavel())){
			JsfUtil.addErrorMessage("Favor preencher o Diâmetro");
			valido = false;
		}
		
		if(Util.isNullOuVazio(caepogFasePerfuracao.getIdeCaepogTipoResiduo())){
			JsfUtil.addErrorMessage("Favor selecionar o Tipo de Fluído");
			valido = false;
			
		} else if(caepogFasePerfuracao.getIdeCaepogTipoResiduo().getIdeCaepogTipoResiduo().equals(CaepogTipoResiduoEnum.OUTROS.getId())) {
			//Melhoria #7657 - Não permitir o usuário selecionar Outros e avançar
			JsfUtil.addWarnMessage(Util.getString("msg_generica_cadastro_outros"));
			valido = false;
		}
		
		return valido;
	}

	public void editarFasePerfuracaoDadoPoco(CaepogFasePerfuracao cfp){
		caepogFasePerfuracao = cfp;		
		caepogFasePerfuracao.setConfirmadoAbaPoco(false);
	}
	
	public void confirmarFasePerfuracaoResiduoGerado(CaepogFasePerfuracao cfp){
		
		caepogFasePerfuracao = cfp;
		
		if(isFasePerfuracaoResiduoGeradoValida()){
			caepogFasePerfuracao.setConfirmadoAbaResiduo(true);
			JsfUtil.addSuccessMessage("Fase da perfuração salva com sucesso!");
		}
	}

	private boolean isFasePerfuracaoResiduoGeradoValida() {
		boolean valido = true;
		if(Util.isNullOuVazio(caepogFasePerfuracao.getTph().getNumValorFaseVariavel())){
			JsfUtil.addErrorMessage("Favor preencher o TPH(%)");
			valido = false;
		} 
		if(Util.isNullOuVazio(caepogFasePerfuracao.getVolumeEstimado().getNumValorFaseVariavel())){
			JsfUtil.addErrorMessage("Favor preencher o Volume Estimado(m³)");
			valido = false;
		} 
		if(Util.isNullOuVazio(caepogFasePerfuracao.getSalinidade().getNumValorFaseVariavel())){
			JsfUtil.addErrorMessage("Favor preencher a Salinidade(mg/l)");
			valido = false;
		} 
		if(Util.isNullOuVazio(caepogFasePerfuracao.getIdeCaepogClasseResiduo())){
			JsfUtil.addErrorMessage("Favor selecionar a Classificação Resíduos");
			valido = false;
		} 
		if(Util.isNullOuVazio(caepogFasePerfuracao.getIdePessoaJuridicaDestino())){
			JsfUtil.addErrorMessage("Favor selecionar o Destino");
			valido = false;
		}
		return valido;
	}
	
	public void editarFasePerfuracaoResiduoGerado(CaepogFasePerfuracao cfp){
		caepogFasePerfuracao = cfp;
		caepogFasePerfuracao.setConfirmadoAbaResiduo(false);
	}
	
	public void onCloseDialogPoco(CloseEvent event) {
		
		try {
			caepogLocacao.setCaepogPocoCollection(caepogPocoService.listarPorCaepogLocacao(caepogLocacao));
			
			for (CaepogPoco cp : caepogLocacao.getCaepogPocoCollection()) {
				cp.setCaepogFasePerfuracaoCollection(caepogPocoService.listarPorCaepogPoco(cp));
			}
			
			isEditando = false;
			tabAtualPoco = 0;
			
			if(RequestContext.getCurrentInstance() != null) {
				RequestContext.getCurrentInstance().addPartialUpdateTarget("tabViewOleoGas");
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Houve um erro!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void mudarTipoPoco(ValueChangeEvent eve) {
		if (Util.isNullOuVazio(isPocoNovo())){
			caepogPoco.setIndNovoCaepogPoco((Boolean) eve.getNewValue());
		}
		if(isPocoNovo() && !(Boolean) eve.getNewValue()) {
			
			caepogPoco.setIdeCaepogTipoPoco(null);
			caepogPoco.setIndAltPorteCaepogPoco(null);
			caepogPoco.setNumProcessoCaepogPoco(null);
			
			listCaepogObjetivoAtividadeSelecionados = new ArrayList<CaepogObjetivoAtividade>();
			listCaepogFormacaoGeologicaSelecionados = new ArrayList<CaepogFormacaoGeologica>();
			
			caepogPoco.setIndSupVegetaCaepogPoco(null);
			caepogPoco.setNumProcessoAsvCaepogPoco(null);
			
			caepogPoco.setCaepogFasePerfuracaoCollection(null);
			
			caepogPoco.setCascalhoResiduoCaepogPoco(null);
			caepogPoco.setEmpolamentoCaepogPoco(null);
		} else {
			caepogPoco.setProfundidadeCaepogPoco(null);
		}
	}
	
	public void valueChangeIndAlteracaoPorte(ValueChangeEvent e) {
		caepogPoco.setNumProcessoCaepogPoco(null);
	}
	
	public void valueChangeIndProcessoASV(ValueChangeEvent e) {
		caepogPoco.setNumProcessoAsvCaepogPoco(null);
	}
	
	public void abrirDialogRequerentePessoaJuridica(CaepogFasePerfuracao cfp) {
		EmpreendimentoController ec = (EmpreendimentoController) SessaoUtil.recuperarManagedBean("#{empreendimentoController}", EmpreendimentoController.class);
		ec.setFlagRequerenteDestino(true);
		ec.exibeSomentePessoaJuridicaFiltroRequerente();
	}
    
	/********************************
	/*						 		*
	//XXX: ABA DOCUMENTOS E ESTUDOS *
	/* 			 			 		*
	/********************************/
	
	private void carregarAbasDocumentosEstudos() {
	
		try {
			documentosCampo = carregarCaepogDocumentoApensados(caepogDocumentoService.listarDocumentoByCateoria(CaepogCategoriaDocumentoEnum.CAMPO));
			documentosLocacao = carregarCaepogDocumentoApensados(caepogDocumentoService.listarDocumentoByCateoria(CaepogCategoriaDocumentoEnum.LOCACAO));
			documentosPoco = carregarCaepogDocumentoApensados(caepogDocumentoService.listarDocumentoByCateoria(CaepogCategoriaDocumentoEnum.POCO));
			documentosAdicionais = carregarCaepogDocumentoApensados(caepogDocumentoService.listarDocumentoByCateoria(CaepogCategoriaDocumentoEnum.DOCUMENTOADCIONAIS));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private List<CaepogDocumentoApensadoDTO> carregarCaepogDocumentoApensados(List<CaepogDocumento> caepogDocumento){
		
		try {
			List<CaepogDocumentoApensadoDTO> listaCaepogDocumentoApensadoDTO = new ArrayList<CaepogDocumentoApensadoDTO>();
			CaepogDocumentoApensadoDTO caepogDocumentoApensadoDTO= null;
			
			for (CaepogDocumento caepogDoc : caepogDocumento) {
				caepogDocumentoApensadoDTO = new CaepogDocumentoApensadoDTO();
			
				caepogDocumentoApensadoDTO.setCaepogDocumento(caepogDoc.clone());
				caepogDocumentoApensadoDTO.setCaepogDocumentoApensado(caepogDocumentoApensadoService.buscarCaepogDocumentoApensadoByTipoByCaepog(caepog, caepogDoc));
				listaCaepogDocumentoApensadoDTO.add(caepogDocumentoApensadoDTO);
			
				caepogDocumentoApensadoDTO = null;	
			}
			
			return listaCaepogDocumentoApensadoDTO;
			
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent getArquivoBaixar(String caminho) {
		try {
			return gerenciaArquivoService.getContentFile(caminho);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void uploadArquivoCaepog(FileUploadEvent event) {
		
		try {
			CaepogDocumentoApensado cda = new CaepogDocumentoApensado();
			CaepogDocumentoApensadoPK cdaKey = new CaepogDocumentoApensadoPK();
			
			cdaKey.setIdeCaepog(caepog.getIdeCaepog());
			cdaKey.setIdeCaepogDocumento(caepogDocumentoApensadoDtoSelecionado.getCaepogDocumento().getIdeCaepogDocumento());
			
	    	cda.setIdeCaepogDocumentoApensadoPK(cdaKey);
	    	cda.setIdePessoaFisicaEnvio(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			cda.setDtcEnvioCaepogDocumento(new Date());
			cda.setUrlCaepogDocumento(FileUploadUtil.Enviar(event, DiretorioArquivoEnum.CAEPOG.toString() + caepog.getIdeCaepog().toString() + FileUploadUtil.getFileSeparator()));
					
			caepogDocumentoApensadoService.salvarOuAtualizar(cda);			
			JsfUtil.addSuccessMessage("Arquivo carregado com sucesso!");
			carregarAbasDocumentosEstudos();
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao carregar o arquivo.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void validarDocumentoCaepog(ValueChangeEvent e){
		
		Boolean resposta = (Boolean) e.getNewValue();
		try {
			caepogDocumentoApensadoDtoSelecionado = (CaepogDocumentoApensadoDTO) e.getComponent().getAttributes().get("ideCaepogDocumentoDTO");
	
			if (resposta) {
				caepogDocumentoApensadoDtoSelecionado.getCaepogDocumentoApensado().setDtcValidadoCaepogDocumento(new Date());
				caepogDocumentoApensadoDtoSelecionado.getCaepogDocumentoApensado().setIdePessoaFisicaValidacao(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			} else {
				caepogDocumentoApensadoDtoSelecionado.getCaepogDocumentoApensado().setDtcValidadoCaepogDocumento(null);
				caepogDocumentoApensadoDtoSelecionado.getCaepogDocumentoApensado().setIdePessoaFisicaValidacao(null);		
			}
			
			caepogDocumentoApensadoService.salvarOuAtualizar(caepogDocumentoApensadoDtoSelecionado.getCaepogDocumentoApensado());
			
			if(resposta){
				JsfUtil.addSuccessMessage("Arquivo validado.");
			}else{
				JsfUtil.addWarnMessage( "Arquivo não validado.");
			}
			
		} catch (Exception e1) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
		}		
	}
	
	public void excluirDocumentoApensadoPorCaepog() {
		try {
			caepogDocumentoApensadoService.excluirPorCaepog(caepog);
			JsfUtil.addSuccessMessage("Todos documentos apensados desta atividade foram excluídos.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Houve um erro!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	// Relatorio da CAEPOG 
	public StreamedContent getImprimirRelatorioCaepog() {
		try {
			Map<String, Object> lParametros = new HashMap<String, Object>();
			lParametros.put("ide_caepog", caepog.getIdeCaepog());
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("ide_caepog", caepog.getIdeCaepog());
			parametros.put("TIPO_ATIVIDADE",TipoAtividadeNaoSujeitaLicenciamentoEnum.PERFURACAO_DE_POCOS);
			parametros.put("NOME_RELATORIO","relatorio_caepog.jasper");
			
			return new RelatorioUtil(parametros).gerar();
		
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possivel gerar o relatório.");
			return null;
		}
	}
	
	// Certificado da CAEPOG 
	public StreamedContent getImprimirCertificadoCaepog() {
				
		try {
		
			CaepogCertificado cc = certificadoService.getCertificadoByideCaepog(caepog);
			
			if (Util.isNullOuVazio(cc) || Util.isNullOuVazio(cc.getIdeCertificado()) ) {
				gerarCertificadoCaepog();
			}
		
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("ide_caepog", caepog.getIdeCaepog());
			parametros.put("TIPO_ATIVIDADE",TipoAtividadeNaoSujeitaLicenciamentoEnum.PERFURACAO_DE_POCOS);
			parametros.put("NOME_RELATORIO","certificado_caepog.jasper");
			
			return new RelatorioUtil(parametros).gerar();
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possivel gerar o certificado.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
	}
	
	public void gerarCertificadoCaepog(){
		
		try{
			
			Certificado certificado = new Certificado();
			
			certificado.setDtcEmissaoCertificado(new Date());
			certificado.setIdeOrgao(orgaoService.carregar(OrgaoEnum.INEMA.getId()));
			certificado.setTipoCertificado(new TipoCertificado(TipoCertificadoEnum.CAEPOG.getId()));
			certificado.setNumCertificado(caepog.getNumCaepog());
			certificadoService.salvar(certificado);
	
		    CaepogCertificadoPK caepogCertificadoPK = new CaepogCertificadoPK(caepog.getIdeCaepog(), certificado.getIdeCertificado());	
			CaepogCertificado caepogCertificado = new CaepogCertificado(caepogCertificadoPK, caepog, certificado);
			
			certificadoService.salvarCaepogCertificado(caepogCertificado);
	
		}catch(Exception e){
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarEmail(){
		
		try {
			caepogComunicacao = new CaepogComunicacao();
			caepogComunicacao.setCaepog(caepog);
			caepogComunicacao.setDtcComunicacao(new Date());
			caepogComunicacao.setAssunto("Email de pendência de validação dos documentos e estudos");
			caepogComunicacao.setDesMesagem("");
			caepogComunicacao.setIndEnviado(false);
			caepogComunicacaoService.salvar(caepogComunicacao);
			
			RequestContext.getCurrentInstance().execute("emailPendenciaValidacao.show();");
		} catch (Exception e) {
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}	
	}
	
	public void enviarEmailStatusValidado(){
		List<String> destinatario = new ArrayList<String>();
		
		try {
			caepogComunicacao = new CaepogComunicacao();
			caepogComunicacao.setCaepog(caepog);
			caepogComunicacao.setDtcComunicacao(new Date());
			caepogComunicacao.setIndEnviado(true);
			caepogComunicacao.setAssunto("Cadastro de Atividades de Oleo e Gás Validado");
			caepogComunicacao.setDesMesagem
				("Prezado "  + caepog.getIdePessoaRequerente().getNomeRazao() + ", \n\n" +
				 "Seu cadastro de número " + caepog.getNumCaepog() + ", foi validado com sucesso. \n" + 
				 "Acesse o SEIA para gerar o certificado. \n\n" +
			     "Atte., \n" + 
				 "Central de Atendimento/INEMA");
			
				
			for (Pessoa pessoa : pessoaService.listarResponsaveisByCaepog(caepog)) {
				destinatario.add(pessoa.getDesEmail());
			}

			if(!Util.isNullOuVazio(destinatario)){
				new EmailUtil().enviarEmail(null, null, destinatario , caepogComunicacao.getAssunto(), caepogComunicacao.getDesMesagem());				
			}
			
			caepogComunicacaoService.salvar(caepogComunicacao);
		} catch (Exception e) {		
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void enviarEmailPendenciaValidacao(){
		List<String> destinatario = new ArrayList<String>();
		
		try {
			
			for (Pessoa pessoa : pessoaService.listarResponsaveisByCaepog(caepog)) {
				destinatario.add(pessoa.getDesEmail());
			}

			if(!Util.isNullOuVazio(destinatario)){
				new EmailUtil().enviarEmail(null, null, destinatario , caepogComunicacao.getAssunto(), caepogComunicacao.getDesMesagem());				
			}
			
			caepogComunicacao.setIndEnviado(true);
			caepogComunicacaoService.salvar(caepogComunicacao);
		
			RequestContext.getCurrentInstance().execute("emailPendenciaValidacao.hide()");
			FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-oleo-gas/consulta.xhtml");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível enviar o E-mail");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isRenderizaDownloadFormulario(CaepogDocumentoApensadoDTO cda) {
		
		if(cda.getCaepogDocumento().getIdeCaepogDocumento() == 1) { //FORMULARIO DE CAMPO
			return true;
		} else if(cda.getCaepogDocumento().getIdeCaepogDocumento() == 2) { //FORMULARIO DE POÇO
			return true;
		} else {
			return false;
		}
	}
	
	public StreamedContent getFormulario(CaepogDocumentoApensadoDTO cda) {
		try {
			
			if(cda.getCaepogDocumento().getIdeCaepogDocumento() == 1) { //FORMULARIO DE CAMPO
				return getDadosAdicionais("Formulario_de_Dados_Adicionais-Campo.doc", "Formulário de Dados Adicionais - Campo.doc");
				
			} else if(cda.getCaepogDocumento().getIdeCaepogDocumento() == 2) { //FORMULARIO DE POÇO
				return getDadosAdicionais("Formulario_de_Dados_Adicionais-Poco.doc", "Formulário de Dados Adicionais - Poço.doc");
				
			} else {
				return null;
			}
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage("Houve um erro!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent getDadosAdicionais(String nomeDoArquivo, String nomeDeSaida) throws FileNotFoundException {
		InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/caepog/"+nomeDoArquivo);
		
		if(stream == null) {
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			return null;
		} else {
			DefaultStreamedContent file = new DefaultStreamedContent(stream, "application/doc", nomeDeSaida);
			return file;
		}
	}
	
	public boolean possuiPendenciaLocalizacao() {
		
		boolean pendenciaCampo = false;
		boolean pendenciaLocacao = false;
		boolean pendenciaPoco = false;
		
		if(!Util.isNullOuVazio(caepog.getCaepogDefinicaoCampoCollection())) {
			
			for (CaepogDefinicaoCampo cdc : caepog.getCaepogDefinicaoCampoCollection()) {
				if(Util.isNullOuVazio(cdc.getIdeLocalizacaoGeografica())) {
					pendenciaCampo = true;
				}
				
				if(!Util.isNullOuVazio(cdc.getCaepogLocacaoCollection())) {
					
					for (CaepogLocacao cl : cdc.getCaepogLocacaoCollection()) {
						if(Util.isNullOuVazio(cl.getIdeLocalizacaoGeografica())) {
							pendenciaLocacao = true;
						}
						
						if(!Util.isNullOuVazio(cl.getCaepogPocoCollection())) {
						
							for (CaepogPoco cp : cl.getCaepogPocoCollection()) {
								if(Util.isNullOuVazio(cp.getIdeLocalizacaoGeografica())) {
									pendenciaPoco = true;
								}
							}
						} else pendenciaPoco = true;
					}
				} else pendenciaLocacao = true;
			}
		} else pendenciaCampo = true;
		
		if(pendenciaCampo || pendenciaLocacao || pendenciaPoco) {
			String erro = "Existem pendências de informações geográficas na(s) aba(s):";
			
			if(pendenciaCampo && !pendenciaLocacao && !pendenciaPoco) erro = erro + " Campo.";
			if(!pendenciaCampo && pendenciaLocacao && !pendenciaPoco) erro = erro + " Locação.";
			if(!pendenciaCampo && !pendenciaLocacao && pendenciaPoco) erro = erro + " Poço.";
			
			if(pendenciaCampo && pendenciaLocacao && !pendenciaPoco) erro = erro + " Campo e Locação.";
			if(pendenciaCampo && !pendenciaLocacao && pendenciaPoco) erro = erro + " Campo e Poço.";
			if(!pendenciaCampo && pendenciaLocacao && pendenciaPoco) erro = erro + " Locação e Poço.";
			
			if(pendenciaCampo && pendenciaLocacao && pendenciaPoco) erro = erro + " Campo, Locação e Poço.";
			
			caepog.setPendenciaValidacao(true);
			caepog.setMsgErroPendenciaLocalizacao(erro);
			
			return true;			
		} else {
			return false;
		}
	}
	
	public boolean isRenderedColumnValidado() {
		
		if(!Util.isNullOuVazio(caepog) 
			&& !Util.isNullOuVazio(caepog.getIdeUltimoStatus()) 
			&& !Util.isNullOuVazio(caepog.getIdeUltimoStatus().getIdeCaepogTipoStatus())
			&& !Util.isNullOuVazio(caepog.getIdeUltimoStatus().getIdeCaepogTipoStatus().getIdeCaepogTipoStatus())) {
		
			CaepogTipoStatus caepogTipoStatus = caepog.getIdeUltimoStatus().getIdeCaepogTipoStatus();
			
			return isCaepogTipoStatusPendenciaOuAguardandoValidacao(caepogTipoStatus) && caepog.isValidando();
		}
		
		return false;
	}
	
	/*************************
	/*						 *
	//XXX: CONTROLE DE ABAS  *
	/* 			 			 *
	/*************************/
	
	public void onTabChange(TabChangeEvent event) {
		String activeTab = event.getTab().getId();
		
		if(tabAtual == 0){ //ABA DADOS BASICOS
			if(!salvarCaepog()) {
				return; //Se não salvar, não avança a aba
			}
		}
		
		if(activeTab.equals("tabDocumentos")){
			carregarAbasDocumentosEstudos();
		}
		
		int activeTabIndex = 0;
		
		for (UIComponent comp : event.getTab().getParent().getChildren()) {
			if (comp.getId().equals(activeTab)) {
				break;
			}
			
			activeTabIndex++;
		}
		
		tabAtual = activeTabIndex;
	}

	public void incrementaIndexAba() {
		
		if(tabAtual == 0) { //ABA DADOS BASICOS
			if(!salvarCaepog()) {
				return; //Se não salvar, não avança a aba
			}
		} 
		
		if(tabAtual == 3) {
			carregarAbasDocumentosEstudos();
		}
		
		tabAtual++;
	}

	public void decrementaIndexAba() {
		tabAtual--;
	}
	
	public Boolean getVisualizaProximo() {
		
		if((tabAtual == 0) && !Util.isNullOuVazio(responsavelEmpreendimento)) {
			visualizaProximo = true;
		} else if(tabAtual == 1 && habilitaAbaLocacao) {
			visualizaProximo = true;
		} else if(tabAtual == 2 && habilitaAbaPoco) {
			visualizaProximo = true;
		} else if(tabAtual == 3 && habilitaAbaDocumento) {
			visualizaProximo = true;
		} else {
			visualizaProximo = false;
		}
		
		return visualizaProximo;
	}

	public Boolean getVisualizaAnterior() {
		if (tabAtual > 0) {
			visualizaAnterior = true;
		} else {
			visualizaAnterior = false;
		}
		
		return visualizaAnterior;
	}
	
	public Boolean getVisualizaFinalizar() {
		if(tabAtual == 4) {
			visualizaFinalizar = true;
		} else {
			visualizaFinalizar = false;
		}
		
		return visualizaFinalizar;
	}

	public void onTabChangePoco(TabChangeEvent event) {
		String activeTab = event.getTab().getId();
		
		int activeTabIndex = 0;
		
		for (UIComponent comp : event.getTab().getParent().getChildren()) {
			if (comp.getId().equals(activeTab)) {
				break;
			}
			
			activeTabIndex++;
		}
		
		tabAtualPoco = activeTabIndex;
	}
	
	public void incrementaIndexAbaPoco() {
		tabAtualPoco++;
	}

	public void decrementaIndexAbaPoco() {
		tabAtualPoco--;
	}
	
	public Boolean getVisualizaProximoPoco() {
		if (tabAtualPoco == 0 && !isAbaResiduoDesabilitada()) {
			visualizaProximoPoco = true;
		} else {
			visualizaProximoPoco = false;
		}
		
		return visualizaProximoPoco;
	}

	public Boolean getVisualizaAnteriorPoco() {
		if (tabAtualPoco > 0) {
			visualizaAnteriorPoco = true;
		} else {
			visualizaAnteriorPoco = false;
		}
		
		return visualizaAnteriorPoco;
	}
	
	public boolean isHabilitaAbaCampo() {
		habilitaAbaCampo = false;
		
		if(!Util.isNullOuVazio(caepog) && !Util.isNullOuVazio(responsavelEmpreendimento)){
			habilitaAbaCampo = true;
		}
		
		return habilitaAbaCampo;
	}

	public boolean isHabilitaAbaLocacao() {
		habilitaAbaLocacao = false;
		
		if(!Util.isNullOuVazio(caepog) && !Util.isNullOuVazio(responsavelEmpreendimento)){
			if(!Util.isNullOuVazio(caepog.getCaepogDefinicaoCampoCollection())){
				habilitaAbaLocacao = true;
			}
		}
		
		return habilitaAbaLocacao;
	}

	public boolean isHabilitaAbaPoco() {
		habilitaAbaPoco = false;
		boolean naoExisteAoMenosUmaLocacaoPorCampo = false;
		
		if(!Util.isNullOuVazio(caepog) && !Util.isNullOuVazio(responsavelEmpreendimento)){
			if(!Util.isNullOuVazio(caepog.getCaepogDefinicaoCampoCollection())){
				for (CaepogDefinicaoCampo cdc : caepog.getCaepogDefinicaoCampoCollection()) {
					if(!Util.isNullOuVazio(cdc.getCaepogLocacaoCollection())) {
						habilitaAbaPoco = true;
					} else {
						naoExisteAoMenosUmaLocacaoPorCampo = true;
					}
				}
			}
		}
		
		if(habilitaAbaPoco && naoExisteAoMenosUmaLocacaoPorCampo) {
			habilitaAbaPoco = false;
		}
		
		return habilitaAbaPoco;
	}

	public boolean isHabilitaAbaDocumento() {
		habilitaAbaDocumento = false;
		
		if(isUsuarioExterno()) {
			boolean existePendenciaDePocoAntigo = false;
			boolean naoExisteAoMenosUmPocoPorLocacao = false;
			
			if(!Util.isNullOuVazio(caepog) && !Util.isNullOuVazio(responsavelEmpreendimento)){
				if(!Util.isNullOuVazio(caepog.getCaepogDefinicaoCampoCollection())){
					for (CaepogDefinicaoCampo cdc : caepog.getCaepogDefinicaoCampoCollection()) {
						if(!Util.isNullOuVazio(cdc.getCaepogLocacaoCollection())) {
							for (CaepogLocacao cl : cdc.getCaepogLocacaoCollection()) {
								if(!Util.isNullOuVazio(cl.getCaepogPocoCollection())) {
									
									boolean existePocoAntigo = false;
									
									for (CaepogPoco cp : cl.getCaepogPocoCollection()) {
										if(!cp.getIndNovoCaepogPoco()) existePocoAntigo = true;
									}
									
									if(!existePocoAntigo) existePendenciaDePocoAntigo = true;
									
									habilitaAbaDocumento = true;
								} else {
									naoExisteAoMenosUmPocoPorLocacao = true;
								}
							}
						}
					}
				}
			}
	
			if((habilitaAbaDocumento && naoExisteAoMenosUmPocoPorLocacao) || existePendenciaDePocoAntigo) {
				habilitaAbaDocumento = false;
			}
		} else {
			habilitaAbaDocumento = true;
		}
		
		return habilitaAbaDocumento;
	}
	
	/**
	 * GETS
	 * 
	 * AND
	 * 
	 * SETS
	 */
	
	public int getTabAtual() {
		return tabAtual;
	}
	
	public void setTabAtual(int tabAtual) {
		this.tabAtual = tabAtual;
	}
	
	public void setVisualizaProximo(Boolean visualizaProximo) {
		this.visualizaProximo = visualizaProximo;
	}
	
	public void setVisualizaAnterior(Boolean visualizaAnterior) {
		this.visualizaAnterior = visualizaAnterior;
	}
	
	public void setVisualizaFinalizar(Boolean visualizaFinalizar) {
		this.visualizaFinalizar = visualizaFinalizar;
	}

	public int getTabAtualPoco() {
		return tabAtualPoco;
	}
	
	public void setTabAtualPoco(int tabAtualPoco) {
		this.tabAtualPoco = tabAtualPoco;
	}

	public void setVisualizaProximoPoco(Boolean visualizaProximoPoco) {
		this.visualizaProximoPoco = visualizaProximoPoco;
	}

	public void setVisualizaAnteriorPoco(Boolean visualizaAnteriorPoco) {
		this.visualizaAnteriorPoco = visualizaAnteriorPoco;
	}

	public Pessoa getRequerenteFiltro() {
		return requerenteFiltro;
	}

	public void setRequerenteFiltro(Pessoa requerenteFiltro) {
		this.requerenteFiltro = requerenteFiltro;
	}

	public Empreendimento getEmpreendimentoFiltro() {
		return empreendimentoFiltro;
	}

	public void setEmpreendimentoFiltro(Empreendimento empreendimentoFiltro) {
		this.empreendimentoFiltro = empreendimentoFiltro;
	}

	public List<CaepogCampo> getListaCaepogCampoFiltro() {
		return listaCaepogCampoFiltro;
	}

	public void setListaCaepogCampoFiltro(List<CaepogCampo> listaCaepogCampoFiltro) {
		this.listaCaepogCampoFiltro = listaCaepogCampoFiltro;
	}

	public CaepogCampo getCaepogCampoSelecionadoFiltro() {
		return caepogCampoSelecionadoFiltro;
	}

	public void setCaepogCampoSelecionadoFiltro(CaepogCampo caepogCampoSelecionadoFiltro) {
		this.caepogCampoSelecionadoFiltro = caepogCampoSelecionadoFiltro;
	}

	public Collection<Municipio> getListaMunicipioFiltro() {
		return listaMunicipioFiltro;
	}

	public void setListaMunicipioFiltro(Collection<Municipio> listaMunicipioFiltro) {
		this.listaMunicipioFiltro = listaMunicipioFiltro;
	}

	public Municipio getMunicipioSelecionadoFiltro() {
		return municipioSelecionadoFiltro;
	}

	public void setMunicipioSelecionadoFiltro(Municipio municipioSelecionadoFiltro) {
		this.municipioSelecionadoFiltro = municipioSelecionadoFiltro;
	}

	public RepresentanteLegal getRepresentanteLegal() {
		return representanteLegal;
	}

	public void setRepresentanteLegal(RepresentanteLegal representanteLegal) {
		this.representanteLegal = representanteLegal;
	}

	public ResponsavelEmpreendimento getResponsavelEmpreendimento() {
		return responsavelEmpreendimento;
	}

	public void setResponsavelEmpreendimento(ResponsavelEmpreendimento responsavelEmpreendimento) {
		this.responsavelEmpreendimento = responsavelEmpreendimento;
	}

	public Empreendimento getEmpreendimentoDialogFiltro() {
		return empreendimentoDialogFiltro;
	}

	public void setEmpreendimentoDialogFiltro(Empreendimento empreendimentoDialogFiltro) {
		this.empreendimentoDialogFiltro = empreendimentoDialogFiltro;
	}

	public LazyDataModel<VwConsultaEmpreendimento> getEmpreendimentoDataModel() {
		return empreendimentoDataModel;
	}

	public void setEmpreendimentoDataModel(LazyDataModel<VwConsultaEmpreendimento> empreendimentoDataModel) {
		this.empreendimentoDataModel = empreendimentoDataModel;
	}

	public boolean isEditando() {
		return isEditando;
	}

	public void setEditando(boolean isEditando) {
		this.isEditando = isEditando;
	}

	public LazyDataModel<Caepog> getCadastroOleoGasDataModel() {
		return cadastroOleoGasDataModel;
	}

	public void setCadastroOleoGasDataModel(LazyDataModel<Caepog> cadastroOleoGasDataModel) {
		this.cadastroOleoGasDataModel = cadastroOleoGasDataModel;
	}

	public CaepogDefinicaoCampo getCaepogDefinicaoCampo() {
		return caepogDefinicaoCampo;
	}

	public void setCaepogDefinicaoCampo(CaepogDefinicaoCampo caepogDefinicaoCampo) {
		this.caepogDefinicaoCampo = caepogDefinicaoCampo;
	}

	public Caepog getCaepog() {
		return caepog;
	}

	public void setCaepog(Caepog caepog) {
		this.caepog = caepog;
	}

	public CaepogLocacao getCaepogLocacao() {
		return caepogLocacao;
	}

	public void setCaepogLocacao(CaepogLocacao caepogLocacao) {
		this.caepogLocacao = caepogLocacao;
	}

	public CaepogPoco getCaepogPoco() {
		return caepogPoco;
	}

	public void setCaepogPoco(CaepogPoco caepogPoco) {
		this.caepogPoco = caepogPoco;
	}

	public List<CaepogTipoPoco> getListCaepogTipoPoco() {
		return listCaepogTipoPoco;
	}

	public void setListCaepogTipoPoco(List<CaepogTipoPoco> listCaepogTipoPoco) {
		this.listCaepogTipoPoco = listCaepogTipoPoco;
	}

	public List<CaepogObjetivoAtividade> getListCaepogObjetivoAtividade() {
		return listCaepogObjetivoAtividade;
	}

	public void setListCaepogObjetivoAtividade(List<CaepogObjetivoAtividade> listCaepogObjetivoAtividade) {
		this.listCaepogObjetivoAtividade = listCaepogObjetivoAtividade;
	}

	public List<CaepogObjetivoAtividade> getListCaepogObjetivoAtividadeSelecionados() {
		return listCaepogObjetivoAtividadeSelecionados;
	}

	public void setListCaepogObjetivoAtividadeSelecionados(List<CaepogObjetivoAtividade> listCaepogObjetivoAtividadeSelecionados) {
		this.listCaepogObjetivoAtividadeSelecionados = listCaepogObjetivoAtividadeSelecionados;
	}

	public List<CaepogFormacaoGeologica> getListCaepogFormacaoGeologica() {
		return listCaepogFormacaoGeologica;
	}

	public void setListCaepogFormacaoGeologica(List<CaepogFormacaoGeologica> listCaepogFormacaoGeologica) {
		this.listCaepogFormacaoGeologica = listCaepogFormacaoGeologica;
	}

	public List<CaepogFormacaoGeologica> getListCaepogFormacaoGeologicaSelecionados() {
		return listCaepogFormacaoGeologicaSelecionados;
	}

	public void setListCaepogFormacaoGeologicaSelecionados(List<CaepogFormacaoGeologica> listCaepogFormacaoGeologicaSelecionados) {
		this.listCaepogFormacaoGeologicaSelecionados = listCaepogFormacaoGeologicaSelecionados;
	}

	public CaepogFasePerfuracao getCaepogFasePerfuracao() {
		return caepogFasePerfuracao;
	}

	public void setCaepogFasePerfuracao(CaepogFasePerfuracao caepogFasePerfuracao) {
		this.caepogFasePerfuracao = caepogFasePerfuracao;
	}

	public List<CaepogTipoResiduo> getListCaepogTipoResiduo() {
		return listCaepogTipoResiduo;
	}

	public void setListCaepogTipoResiduo(List<CaepogTipoResiduo> listCaepogTipoResiduo) {
		this.listCaepogTipoResiduo = listCaepogTipoResiduo;
	}

	public List<CaepogDocumentoApensadoDTO> getDocumentosCampo() {
		return documentosCampo;
	}

	public void setDocumentosCampo(List<CaepogDocumentoApensadoDTO> documentosCampo) {
		this.documentosCampo = documentosCampo;
	}

	public List<CaepogDocumentoApensadoDTO> getDocumentosLocacao() {
		return documentosLocacao;
	}

	public void setDocumentosLocacao(List<CaepogDocumentoApensadoDTO> documentosLocacao) {
		this.documentosLocacao = documentosLocacao;
	}

	public List<CaepogDocumentoApensadoDTO> getDocumentosPoco() {
		return documentosPoco;
	}

	public void setDocumentosPoco(List<CaepogDocumentoApensadoDTO> documentosPoco) {
		this.documentosPoco = documentosPoco;
	}

	public List<CaepogDocumentoApensadoDTO> getDocumentosAdicionais() {
		return documentosAdicionais;
	}

	public void setDocumentosAdicionais(List<CaepogDocumentoApensadoDTO> documentosAdicionais) {
		this.documentosAdicionais = documentosAdicionais;
	}

	public CaepogDocumentoApensadoDTO getCaepogDocumentoApensadoDtoSelecionado() {
		return caepogDocumentoApensadoDtoSelecionado;
	}

	public void setCaepogDocumentoApensadoDtoSelecionado(CaepogDocumentoApensadoDTO caepogDocumentoApensadoDtoSelecionado) {
		this.caepogDocumentoApensadoDtoSelecionado = caepogDocumentoApensadoDtoSelecionado;
	}

	public List<CaepogClasseResiduo> getListCaepogClasseResiduo() {
		return listCaepogClasseResiduo;
	}

	public void setListCaepogClasseResiduo(List<CaepogClasseResiduo> listCaepogClasseResiduo) {
		this.listCaepogClasseResiduo = listCaepogClasseResiduo;
	}

	public List<Imovel> getListImovel() {
		return listImovel;
	}

	public void setListImovel(List<Imovel> listImovel) {
		this.listImovel = listImovel;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public void setCaepogComunicacao(CaepogComunicacao caepogComunicacao) {
		this.caepogComunicacao = caepogComunicacao;
	}

	public CaepogComunicacao getCaepogComunicacao() {
		return caepogComunicacao;
	}

	public boolean isTelaConsulta() {
		return isTelaConsulta;
	}

	public void setTelaConsulta(boolean isTelaConsulta) {
		this.isTelaConsulta = isTelaConsulta;
	}

	public DataTable getDataTableCadastroOleoGas() {
		return dataTableCadastroOleoGas;
	}

	public void setDataTableCadastroOleoGas(DataTable dataTableCadastroOleoGas) {
		this.dataTableCadastroOleoGas = dataTableCadastroOleoGas;
	}

	public String getNumCaepogFiltro() {
		return numCaepogFiltro;
	}

	public void setNumCaepogFiltro(String numCaepogFiltro) {
		this.numCaepogFiltro = numCaepogFiltro;
	}

	public List<CaepogTipoStatus> getListaCaepogTipoStatusFiltro() {
		return listaCaepogTipoStatusFiltro;
	}

	public void setListaCaepogTipoStatusFiltro(List<CaepogTipoStatus> listaCaepogTipoStatusFiltro) {
		this.listaCaepogTipoStatusFiltro = listaCaepogTipoStatusFiltro;
	}

	public CaepogTipoStatus getCaepogTipoStatusSelecionadoFiltro() {
		return caepogTipoStatusSelecionadoFiltro;
	}

	public void setCaepogTipoStatusSelecionadoFiltro(
			CaepogTipoStatus caepogTipoStatusSelecionadoFiltro) {
		this.caepogTipoStatusSelecionadoFiltro = caepogTipoStatusSelecionadoFiltro;
	}

	public Date getPeriodoInicioFiltro() {
		return periodoInicioFiltro;
	}

	public void setPeriodoInicioFiltro(Date periodoInicioFiltro) {
		this.periodoInicioFiltro = periodoInicioFiltro;
	}

	public Date getPeriodoFimFiltro() {
		return periodoFimFiltro;
	}

	public void setPeriodoFimFiltro(Date periodoFimFiltro) {
		this.periodoFimFiltro = periodoFimFiltro;
	}

	public List<CaepogComunicacao> getListaCaepogComunicacoes() {
		return listaCaepogComunicacoes;
	}

	public void setListaCaepogComunicacoes(
			List<CaepogComunicacao> listaCaepogComunicacoes) {
		this.listaCaepogComunicacoes = listaCaepogComunicacoes;
	}

	public List<CaepogStatus> getListaCaepogStatus() {
		return listaCaepogStatus;
	}

	public void setListaCaepogStatus(List<CaepogStatus> listaCaepogStatus) {
		this.listaCaepogStatus = listaCaepogStatus;
	}

	public CaepogComunicacao getComunicacaoSelecionada() {
		return comunicacaoSelecionada;
	}

	public void setComunicacaoSelecionada(CaepogComunicacao comunicacaoSelecionada) {
		this.comunicacaoSelecionada = comunicacaoSelecionada;
	}
}