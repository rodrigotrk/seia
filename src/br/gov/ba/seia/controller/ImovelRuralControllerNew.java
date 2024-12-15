package br.gov.ba.seia.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.wizard.Wizard;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.quartz.xml.ValidationException;

import com.vividsolutions.jts.geom.Point;

import br.gov.ba.seia.dto.EnderecoDTO;
import br.gov.ba.seia.dto.ImovelRuralDTO;
import br.gov.ba.seia.dto.TipologiaAtividadeAgriculturaDTO;
import br.gov.ba.seia.dto.TipologiaAtividadeAnimalDTO;
import br.gov.ba.seia.dto.TipologiaAtividadePisciculturaDTO;
import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.AreaProdutiva;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividade;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadeAgricultura;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadeAnimal;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadePiscicultura;
import br.gov.ba.seia.entity.AreaRuralConsolidada;
import br.gov.ba.seia.entity.AssentadoIncra;
import br.gov.ba.seia.entity.AssentadoIncraImovelRural;
import br.gov.ba.seia.entity.AssociacaoAssentadoImovelRuralIncra;
import br.gov.ba.seia.entity.AssociacaoIncra;
import br.gov.ba.seia.entity.AssociacaoIncraImovelRural;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.ContratoConvenioCefir;
import br.gov.ba.seia.entity.ContratoConvenioCefirMunicipio;
import br.gov.ba.seia.entity.CoordenadaGeografica;
import br.gov.ba.seia.entity.CronogramaEtapa;
import br.gov.ba.seia.entity.CronogramaRecuperacao;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.DocumentoImovelRuralPosse;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Escolaridade;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.FaseAssentamentoImovelRural;
import br.gov.ba.seia.entity.GeoJsonSicar;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralDesbloqueio;
import br.gov.ba.seia.entity.ImovelRuralMudancaStatusJustificativa;
import br.gov.ba.seia.entity.ImovelRuralPrad;
import br.gov.ba.seia.entity.ImovelRuralRppn;
import br.gov.ba.seia.entity.ImovelRuralSicar;
import br.gov.ba.seia.entity.ImovelRuralTac;
import br.gov.ba.seia.entity.ImovelRuralUsoAgua;
import br.gov.ba.seia.entity.ImovelRuralUsoAguaIntervencao;
import br.gov.ba.seia.entity.ImovelRuralUsoAguaTipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.MetodoIrrigacao;
import br.gov.ba.seia.entity.ModuloFiscal;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.NaturezaJuridica;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.OutrosPassivosAmbientais;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.entity.PctFamilia;
import br.gov.ba.seia.entity.PctImovelRural;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaPct;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoExterno;
import br.gov.ba.seia.entity.ProcessoTramiteImovelRural;
import br.gov.ba.seia.entity.ProcessoUsoAgua;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoImovel;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ResponsavelImovelRural;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.entity.SubTipoApp;
import br.gov.ba.seia.entity.SupressaoVegetacao;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoApp;
import br.gov.ba.seia.entity.TipoCadastroImovelRural;
import br.gov.ba.seia.entity.TipoDocumentoImovelRural;
import br.gov.ba.seia.entity.TipoEstadoConservacao;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoFinalidadeVegetacaoNativa;
import br.gov.ba.seia.entity.TipoImovel;
import br.gov.ba.seia.entity.TipoImovelRequerimento;
import br.gov.ba.seia.entity.TipoIntervencao;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.entity.TipoPessoaJuridicaPct;
import br.gov.ba.seia.entity.TipoRecuperacao;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.TipoSeguimentoPct;
import br.gov.ba.seia.entity.TipoTerritorioPct;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.VegetacaoNativa;
import br.gov.ba.seia.entity.VegetacaoNativaFinalidade;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.DivisaoAtividadeCefirEnum;
import br.gov.ba.seia.enumerator.FaseAssentamentoImovelRuralEnum;
import br.gov.ba.seia.enumerator.INCRAEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.MunicipioEnum;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.SistemaCoordenadaEnum;
import br.gov.ba.seia.enumerator.StatusCadastroImovelRuralEnum;
import br.gov.ba.seia.enumerator.TemaGeoseiaEnum;
import br.gov.ba.seia.enumerator.TipoBarragemEnum;
import br.gov.ba.seia.enumerator.TipoCadastroImovelRuralEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.enumerator.TipoDocumentoImovelRuralEnum;
import br.gov.ba.seia.enumerator.TipoEstadoConservacaoEnum;
import br.gov.ba.seia.enumerator.TipoImovelEnum;
import br.gov.ba.seia.enumerator.TipoIntervencaoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaJuridicaPctEnum;
import br.gov.ba.seia.enumerator.TipoTerritorioPctEnum;
import br.gov.ba.seia.enumerator.TipoTravessiaDuto;
import br.gov.ba.seia.enumerator.TipoUsoAgua;
import br.gov.ba.seia.enumerator.TipologiaCefirEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.enumerator.UsoBarragem;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.exception.AreaDeclaradaInvalidaException;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.exception.LocalizacaoGeograficaException;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.facade.PctImovelRuralFacade;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.CronogramaRecuperacaoService;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.PctFamiliaService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.ServicoDeCepService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoLogradouroService;
import br.gov.ba.seia.service.UsuarioAutorizacaoGeobahiaService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.GeoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.PostgisUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.json.JsonUtil;

@Named("imovelRuralControllerNew")
@ViewScoped
public class ImovelRuralControllerNew extends SeiaControllerAb implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");

	private static final String PROCESSO_NOVO = "0";

	private static final int PROCURADOR = 5;

	private static final Integer USUARIO_SEM_RESTRICAO = 5077;

	private static final int OUTRO = -1;

	private List<Logradouro> listaLogradouro;

	@EJB
	protected ImovelRuralFacade imovelRuralServiceFacade;
	
	@EJB
	private ServicoDeCepService cepService;

	@EJB
	private EnderecoFacade enderecoFacade;

	@EJB
	private PctImovelRuralFacade pctImovelRuralFacade;

	@EJB
	protected AuditoriaFacade auditoria;

	@Inject
	private ImovelRuralAbaController abaController;

	@EJB
	private CronogramaRecuperacaoService cronogramaRecuperacaoService;

	@EJB
	private PctFamiliaService pctFamiliaService;

	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;

	@EJB
	private UsuarioAutorizacaoGeobahiaService usuarioAutorizacaoGeobahiaService;

	@EJB
	private ImovelRuralFacade imovelRuralFacade;

	@EJB
	private RepresentanteLegalService representanteLegalService;

	@EJB
	private TelefoneService telefoneService;

	@EJB
	private EnderecoPessoaService enderecoPessoaService;

	@EJB
	private LogradouroService logradouroService;

	@EJB
	private BairroService bairroService;

	@EJB
	private MunicipioService municipioService;

	@EJB
	private TipoLogradouroService tipoLogradouroService;
	
	@EJB
	private EnderecoFacade enderecoService;

	/**
	 * <p>
	 * (...) o sistema deve permitir que o perfil de Maria Daniela edite quaisquer
	 * informações, inclusive para imóveis que possuem reserva legal aprovada;
	 * </p>
	 * <p>
	 * O sistema não deverá realizar nenhum tipo de validação na edição do imóvel
	 * rural para o perfil de Maria Daniela;
	 * </p>
	 * 
	 * @see <a href="http://10.105.17.77/redmine/issues/8035">8035</a>
	 */
	
	protected Municipio municipio;
	protected Estado estado;
	private Logradouro logradouro;
	private Bairro bairro;
	private Bairro bairroOutro;
	
	//2707 - Victor Vinicius Santana Arouca
	private static final Integer USUARIO_SEM_RESTRICAO1 = 2707;
	//20 - Arthur Bulhões de Santa Inês Junior
	private static final Integer USUARIO_SEM_RESTRICAO2 = 20;
	//70070 - VANESSA BRITO MATOS DOS SANTOS - Adicionado através do ticket 34285
	private static final Integer USUARIO_SEM_RESTRICAO3 = 70070;

	private boolean enableFormEndereco;
	public static Integer PRESERVADA;
	private int tipoVinculoImovelOld;
	private boolean disableSalvarResponsavelTecnico;
	private boolean editandoProprietario;
	private boolean editandoProprietarioCda;
	private boolean exibirLatitudeLongitude;
	private boolean finalizarRendered;
	private boolean habilitaCadastroProprietarioImovel;
	private boolean habilitaComboMunicipio;
	private boolean houveMudanca;
	private boolean imovelEstaSendoFinalizado;
	private boolean indManejoCria;
	private boolean indManejoEngorda;
	private boolean indManejoRecria;
	private boolean indManejoReproducao;
	private boolean indPodeModificarMunicipio;
	private boolean indProjetoBndes;
	private boolean isEdicaoAp;
	private boolean isEdicaoApp;
	private boolean isEdicaoDocumentacao;
	private boolean isEdicaoCronogramaEtapa;
	private boolean isEdicaoResponsavelTecnico;
	private boolean isIgualShapeRlSeiaECerberus;
	private boolean isInclusaoAtividade;
	private boolean isProprietarioImovel;
	private boolean opcaoDesenhoAp;
	private boolean pnlAgricultura;
	private boolean pnlAnimal;
	private boolean pnlPiscicultura;
	private boolean renderPollProprietarios;
	private boolean skip;
	private boolean telaCnpj;
	private boolean telaCpf;
	private boolean temLocGeoDesenhoAreaProdutiva;
	private boolean temLocGeoShapeAreaProdutiva;
	private boolean temMesAbr;
	private boolean temMesAgo;
	private boolean temMesDez;
	private boolean temMesFev;
	private boolean temMesJan;
	private boolean temMesJul;
	private boolean temMesJun;
	private boolean temMesMai;
	private boolean temMesMar;
	private boolean temMesNov;
	private boolean temMesOut;
	private boolean temMesSet;
	private boolean temUplShapefileAreaProdutiva;
	private boolean temUsoDessedentacao;
	private boolean temUsoLimpeza;
	private boolean visualizacaoAp;
	private boolean visualizarProprietario;
	private boolean visualizarUsoAgua;

	private Boolean aceiteCondicoesRecuperacaoApp;
	private Boolean aceiteCondicoesRecuperacaoOp;
	private Boolean existeSobreposicaoRlApp;
	private Boolean existeSobreposicaoRlAreaProdutiva;
	private Boolean indLancamentoResiduosLiquidos;
	private Boolean isPessoaFisicaJaCadastrada;
	private Boolean processoRlConcluidoCerberus;
	private Boolean rlMenorQueVintePorCento;
	private Boolean temLicAreaProdutiva;
	private Boolean visualizacaoApp;

	private String arquivoShapeExcluir;
	private String caminhoArquivo;
	private String caminhoGeoBahia;
	private String caminhoNovoGeoBahia;
	private String caminhoDesenharGeoBahia;
	private String numCep;
	private String dscMesesIrrigacao;
	private String numMesesIrrigacao;
	private String dscTipoUsoAgua;
	private String valAreaProdutiva;
	private String valVolume;
	private String numProcessoAp;
	private String descLocGeoShapeAreaProdutiva;
	private String excluirShapeAreaProdutiva;
	private String coordenadaPontoxy;
	private String minutosLatitudeLoc;
	private String segundosLatitudeLoc;
	private String grausLatitudeLoc;
	private String minutosLongitudeLoc;
	private String segundosLongitudeLoc;
	private String grausLongitudeLoc;
	private String fracaoGrauLatitudeLoc;
	private String fracaoGrauLongitudeLoc;
	private String minutosLatitudeLocPonto2;
	private String segundosLatitudeLocPonto2;
	private String grausLatitudeLocPonto2;
	private String minutosLongitudeLocPonto2;
	private String segundosLongitudeLocPonto2;
	private String grausLongitudeLocPonto2;
	private String fracaoGrauLatitudeLocPonto2;
	private String fracaoGrauLongitudeLocPonto2;
	private String tipoProcessoTramiteAntigo;
	private String tituloProprietariosJustoPossuidores;
	private String lblListaProprietariosJustoPossuidores;
	private String lblBotaoIncluirProprietariosJustoPossuidores;
	private String lblDocumentoPossePropriedade;
	private String msgDataInvalida;
	protected String geometriaIm;
	protected String geometriaRl;
	private String tipoProcessoTramite;

	private List<Bairro> listaBairro;
	private List<Bairro> listBairros;
	private List<TipoLogradouro> listTipoLogradouro;
	private List<Logradouro> listLogradouro;
	private List<Estado> listEstados;
	private List<Municipio> listMunicipios;
	private List<TipologiaAtividade> listaTableTipologiaAtividade;
	private List<TipologiaAtividadeAgriculturaDTO> listaTableTipologiaAtividadeAgricultura;
	private List<TipologiaAtividadeAnimalDTO> listaTableTipologiaAtividadeAnimal;
	private List<TipologiaAtividadePisciculturaDTO> listaTableTipologiaAtividadePiscicultura;
	private List<Tipologia> listaSubTipologia;
	private List<TipologiaAtividade> listaTipologiaAtividade;
	private List<MetodoIrrigacao> listaMetodoIrrigacao;
	private List<Escolaridade> listaEscolaridade;
	private List<ClassificacaoSecaoGeometrica> listaSecaoGeometrica;
	private List<TipoApp> listaTipoApp;
	private List<SubTipoApp> listaSubTipoApp;
	private List<TipoFinalidadeUsoAgua> listFinalidadesSelecionadas;
	private List<UsoBarragem> listUsoBarragemEnum;
	private List<ProcessoUsoAgua> processoUsoAguaCollection;
	private List<ImovelRuralUsoAgua> listCapSuperficial;
	private List<ImovelRuralUsoAgua> listCapSubterranea;
	private List<ImovelRuralUsoAgua> listLancamentoResiduos;
	private List<ImovelRuralUsoAgua> listPontoIntervencao;
	private List<AssociacaoIncra> listAssociacoesIncra;
	private List<Tipologia> listaDivisaoAtividade;
	private List<Tipologia> listaGrupoAtividade;
	private List<Tipologia> listaSubGrupoAtividade;
	private List<DocumentoImovelRural> listaDocumentoPlanoManejo;
	private List<DocumentoImovelRural> listaPradImportadosApp;
	private List<AssociacaoAssentadoImovelRuralIncra> listAssociacaoAssentadoImovelRuralIncraSelecionado;

	protected List<TipoEstadoConservacao> listaTipoEstadoConservacao;
	protected List<DocumentoImovelRural> listaPradImportadosRl;

	private Collection<Pais> listaPais;
	private Collection<SistemaCoordenada> listaDatums;
	private Collection<TipoRecuperacao> listaTipoRecuperacao;
	private Collection<AreaProdutiva> listaAreaProdutiva;
	private Collection<App> listaApp;
	protected Collection<PessoaImovel> listProprietariosImovel;
	private Collection<TipoDocumentoImovelRural> listTipoDocumentoImovelRural;
	private boolean enableTipoLogradouro = true;
	private Wizard wizard;
	private CommandButton btnFinalizar;
	protected ImovelRural imovelRural;
	private TipoVinculoImovel tipoVinculoImovel;
	private ResponsavelImovelRural responsavelTecnicoSelecionado;
	private Logradouro logradouroSelecionado;
	private Bairro bairroSelecionado;
	private Bairro bairroSelecionadoCombo;
	private Estado estadoSelecionado;
	private Municipio municipioSelecionado;
	private TipoLogradouro tipoLogradouroSelecionado;
	protected CronogramaEtapa cronogramaEtapaSelecionado;
	private App appSelecionada;
	private AreaProdutiva areaProdutivaSelecionada;
	private Tipologia tipologia;
	private Tipologia subTipologia;
	private MetodoIrrigacao metodoIrrigacao;
	private Integer tipoCronogramaEtapa; // 1 - Reserva Legal 2 - APP
	private Integer qtdCabeca;
	private Integer tipoUsoAgua;
	private Integer areaProdutivaIdeLocGeoSelecionada;
	private TipologiaAtividade tipologiaAtividadeExcluir;
	private LocalizacaoGeografica locGeoShapeAreaProdutiva;
	private StreamedContent baixarShapeAreaProdutiva;
	private SistemaCoordenada sistemaCoordenadaShapeAreaProdutiva;
	private ClassificacaoSecaoGeometrica secaoGeometricaShapeAreaProdutiva;
	private Pessoa pessoaPersist;
	private PessoaFisica pessoaFisicaPersist;
	private PessoaJuridica pessoaJuridicaPersist;
	private PessoaImovel proprietarioExclusao;
	private ImovelRuralUsoAgua imovelRuralUsoAgua;
	private ImovelRuralUsoAguaIntervencao imovelRuralUsoAguaIntervencao;
	private ProcessoUsoAgua processoUsoAguaSelecionado;
	private ProcessoExterno processoExterno;
	private AssociacaoIncra associacaoIncraSelecionada;
	private AssentadoIncraImovelRural assentadoIncraImovelRuralSelecionado;
	private AssociacaoAssentadoImovelRuralIncra associacaoAssentadoImovelRuralIncraSelecionado;
	private Tipologia divisaoAtividade;
	private TipologiaAtividade tipologiaAtividade;
	private Municipio municipioOriginal;
	private Endereco ideEnderecoSelecionado;

	private PctImovelRural pctImovelRural;
	private PessoaFisica pctPessoaFisicaSelecionado;
	private PctFamilia pctFamiliaExclusaoSelecionada;
	private int pctTipoInclusaoProprietario;
	private LocalizacaoGeografica ideLocalizacaoGeograficaPctOld;
	private boolean pctHabilitarCampos;
	private Date dataAtual;
	private PessoaJuridicaPct pessoaJuridicaPct;
	private boolean pctHabilitarCamposCnpj;
	private boolean pctHabilitarCampoRepresentanteLegal;
	private PessoaJuridicaPct pessoaJuridicaPctExclusaoSelecionada;
	private DualListModel<TipoSeguimentoPct> tipoSeguimentoPcts;
	private TipoSeguimentoPct tipoSeguimentoPctSelecionadoExclusao;
	private boolean indRepresentanteFamilia;
	private Pessoa pessoaRepresentanteFamilia;
	private PctFamilia pessoaRepresentante;
	private Integer tipoUpload;
	private String clientId;
	private String token;
	private String email;
	private StreamedContent filePctTxt;
	private boolean liConcordoImportacaoPlanilha;
	private UploadedFile planilhaPctUploaded;
	private TipoEstadoConservacao tipoEstadoConservacaoInicial;
	
	private ImovelRuralDesbloqueio ImovelRuralDesbloqueio;
	private List<DocumentoImovelRural> listDocumentoSolicitacaoDesbloqueio;
	private String nmModal = "dlgConfirmacaoDesbloqueioLimite";

	@PostConstruct
	public void init() {
		inicializarVariaveis();
		inicializarResponsavelTecnico();
		inicializarAppSelecionada();
		inicializarCronogramaEtapaSelecionada();
		inicializarOuCarregarImovelRural();
		inicializarDocumentoImovelRuralPosse();

		carregarSecaoGeometrica();
		carregarDatums();
		carregarTipoRecuperacao();
		carregarTipoVinculoImovel();

		inicializarOutrasVariaveis();
		inicializarImovelRuralUsoAgua();
		inicializarEndereco();
		inicializarRequerenteIncra();

		carregarDivisaoAtividade();

		inicializarPct();
		carregarPctImovelRural();

		if ((!Util.isNullOuVazio(this.imovelRural.getIndLancamentoManancial())
				&& this.imovelRural.getIndLancamentoManancial())
				|| (!Util.isNullOuVazio(this.imovelRural.getIndLancamentoConcessionaria())
						&& this.imovelRural.getIndLancamentoConcessionaria())) {
			this.setIndLancamentoResiduosLiquidos(true);
		}
	}

	private void inicializarVariaveis() {
		PRESERVADA = 1;
		indPodeModificarMunicipio = true;
		isEdicaoAp = false;
		isEdicaoDocumentacao = false;
		isInclusaoAtividade = false;
		opcaoDesenhoAp = false;
		pnlAgricultura = false;
		pnlAnimal = false;
		pnlPiscicultura = false;
		renderPollProprietarios = false;
		temLocGeoDesenhoAreaProdutiva = false;
		temLocGeoShapeAreaProdutiva = false;
		temMesAbr = false;
		temMesAgo = false;
		temMesDez = false;
		temMesFev = false;
		temMesJan = false;
		temMesJul = false;
		temMesJun = false;
		temMesMai = false;
		temMesMar = false;
		temMesNov = false;
		temMesOut = false;
		temMesSet = false;
		temUplShapefileAreaProdutiva = false;
		temUsoDessedentacao = false;
		temUsoLimpeza = false;
		visualizacaoAp = false;
		aceiteCondicoesRecuperacaoApp = false;
		aceiteCondicoesRecuperacaoOp = false;
		existeSobreposicaoRlApp = null;
		existeSobreposicaoRlAreaProdutiva = null;
		caminhoGeoBahia = URLEnum.CAMINHO_GEOBAHIA_CEFIR.toString();
		caminhoNovoGeoBahia = URLEnum.CAMINHO_NOVO_GEOBAHIA.toString();
		caminhoDesenharGeoBahia = URLEnum.CAMINHO_GEOBAHIA.toString();
		numProcessoAp = null;
		msgDataInvalida = "";
		geometriaIm = null;
		geometriaRl = null;
		dataAtual = new Date();
		token = usuarioAutorizacaoGeobahiaService.carregarUsuarioAutorizacaoGeobahiaCefir();
		
		this.ImovelRuralDesbloqueio = new ImovelRuralDesbloqueio();
	}

	private void inicializarPct() {
		pctImovelRural = new PctImovelRural();
		pctPessoaFisicaSelecionado = new PessoaFisica();
		pessoaJuridicaPct = new PessoaJuridicaPct();
		pessoaJuridicaPct.setIdePessoaJuridica(new PessoaJuridica());
		tipoSeguimentoPcts = new DualListModel<TipoSeguimentoPct>();
	}

	private void inicializarResponsavelTecnico() {
		if (Util.isNullOuVazio(responsavelTecnicoSelecionado)) {
			responsavelTecnicoSelecionado = new ResponsavelImovelRural();
			responsavelTecnicoSelecionado.setIdePessoaFisica(new PessoaFisica());
			responsavelTecnicoSelecionado.getIdePessoaFisica().setIdePais(new Pais());
			responsavelTecnicoSelecionado.getIdePessoaFisica().setIdeEscolaridade(new Escolaridade());
		}
	}

	private void inicializarAppSelecionada() {
		if (Util.isNullOuVazio(appSelecionada)) {
			appSelecionada = new App();
			appSelecionada.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			appSelecionada.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
			appSelecionada.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
			appSelecionada.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
			appSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(null);
			appSelecionada.setIdeTipoApp(new TipoApp());
			appSelecionada.setIdeTipoEstadoConservacao(new TipoEstadoConservacao());
		}
	}

	private void inicializarCronogramaEtapaSelecionada() {
		if (Util.isNullOuVazio(cronogramaEtapaSelecionado)) {
			cronogramaEtapaSelecionado = new CronogramaEtapa();
			cronogramaEtapaSelecionado.setIdeTipoRecuperacao(new TipoRecuperacao());
		}
	}

	private void inicializarOuCarregarImovelRural() {
		if (Util.isNullOuVazio(ContextoUtil.getContexto().getImovelRural())) {
			this.imovelRural = new ImovelRural();

			if (getShowPCT()) {
				imovelRural.setIdeTipoCadastroImovelRural(
						new TipoCadastroImovelRural(TipoCadastroImovelRuralEnum.PCT.getTipo()));
			}

			this.imovelRural.setImovel(new Imovel());
			this.imovelRural.getImovel().setIdeEndereco(new Endereco());
			this.imovelRural.getImovel().getIdeEndereco().setIdeLogradouro(new Logradouro());
			this.imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().setIdeMunicipio(new Municipio());
			this.imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
					.setIdeEstado(new Estado());
			this.imovelRural.getImovel().setPessoaImovelCollection(new ArrayList<PessoaImovel>());
			this.imovelRural.getImovel().setProprietario(new Pessoa());

			this.listBairros = new ArrayList<Bairro>();
			this.listTipoLogradouro = new ArrayList<TipoLogradouro>();
			this.listLogradouro = new ArrayList<Logradouro>();
			this.listMunicipios = new ArrayList<Municipio>();
			this.listEstados = new ArrayList<Estado>();

			this.listaPradImportadosRl = new ArrayList<DocumentoImovelRural>();
			this.listaPradImportadosApp = new ArrayList<DocumentoImovelRural>();

			bairroSelecionado = new Bairro();
			bairroSelecionadoCombo = new Bairro();
			logradouroSelecionado = new Logradouro();
			municipioSelecionado = new Municipio();
			estadoSelecionado = new Estado();
			tipoLogradouroSelecionado = new TipoLogradouro();

			ContextoUtil.getContexto().setImovelRural(this.imovelRural);

		} else {
			this.imovelRural = ContextoUtil.getContexto().getImovelRural();
			if (imovelRural.isImovelCDA() && !imovelRural.getIndImovelRuralCdaEditado()) {
				this.imovelRural.getImovel().getIdeEndereco().setDesComplemento(null);
				this.imovelRural.getImovel().getIdeEndereco().setDesPontoReferencia(null);
				this.imovelRural.getImovel().getIdeEndereco().setIdeLogradouro(new Logradouro());
				this.imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().setIdeMunicipio(new Municipio());
				this.imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
						.setIdeEstado(new Estado());
				imovelRural.setIndImovelRuralCdaEditado(true);
			}

			if (!Util.isNull(imovelRural.getImovel())) {
				if (!Util.isNullOuVazio(getBairro()) && !getBairro().getIndOrigemCorreio()  && !getBairro().isIndOrigemApi()) {
					if (!Util.isNullOuVazio(this.ideEnderecoSelecionado)) {
						getEndereco().getIdeLogradouro()
								.setIdeBairro(this.ideEnderecoSelecionado.getIdeLogradouro().getIdeBairro());
					} else {
						getBairro().setIdeBairro(OUTRO);
					}
				}

				bairroSelecionado = getBairro();
				bairroSelecionadoCombo = getBairro();
				logradouroSelecionado = getEndereco().getIdeLogradouro();
				tipoLogradouroSelecionado = logradouroSelecionado.getIdeTipoLogradouro();
				municipioSelecionado = getLogradouro().getIdeMunicipio();
				estadoSelecionado = getLogradouro().getIdeMunicipio().getIdeEstado();

				if (!Util.isNullOuVazio(getEndereco().getIdeLogradouro().getNumCep())) {
					setNumCep(getEndereco().getIdeLogradouro().getNumCepString());
				}

				municipioOriginal = getLogradouro().getIdeMunicipio();
			}

			if (!Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais())
					&& !Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao())) {
				aceiteCondicoesRecuperacaoOp = (!Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais()
						.getCronogramaRecuperacao().getCronogramaEtapaCollection()) ? true : false);
			} else {
				aceiteCondicoesRecuperacaoOp = false;
			}

			if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica())
					&& !Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())
					&& !Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao())
					&& imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()
							.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId())) {

				try {
					List<DadoGeografico> listaDadosGeo = imovelRuralServiceFacade.listarDadoGeografico(
							imovelRural.getIdeLocalizacaoGeografica(), imovelRural.getIdeLocalizacaoGeografica()
									.getIdeClassificacaoSecao().getIdeClassificacaoSecao());
					this.coordenadaPontoxy = reorganizarCoordenadaPonto(listaDadosGeo.get(0).getCoordGeoNumerica());
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
				}
			}

			carregarListaPradImportado(1);
		}
	}

	private void inicializarDocumentoImovelRuralPosse() {
		if (Util.isNull(imovelRural.getDocumentoImovelRuralPosse())) {
			imovelRural.setDocumentoImovelRuralPosse(new DocumentoImovelRuralPosse());
		}
	}

	private void inicializarOutrasVariaveis() {
		telaCpf = true;
		pessoaFisicaPersist = new PessoaFisica();
		pessoaPersist = new Pessoa();
		pessoaJuridicaPersist = new PessoaJuridica();
	}

	private void inicializarImovelRuralUsoAgua() {
		if (Util.isNullOuVazio(imovelRuralUsoAgua)) {
			imovelRuralUsoAgua = new ImovelRuralUsoAgua();
			imovelRuralUsoAgua.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			imovelRuralUsoAguaIntervencao = new ImovelRuralUsoAguaIntervencao();
			imovelRuralUsoAguaIntervencao.setIdeTipoIntervencao(new TipoIntervencao());
		}

	}

	private void inicializarEndereco() {
		houveMudanca = false;
	}

	private void inicializarRequerenteIncra() {
		if (isRequerenteIncra()) {
			this.imovelRural.setIdeTipoCadastroImovelRural(
					new TipoCadastroImovelRural(TipoCadastroImovelRuralEnum.ASSENTAMENTO_INCRA.getTipo()));
			this.imovelRural.setIdeFaseAssentamentoImovelRural(
					new FaseAssentamentoImovelRural(FaseAssentamentoImovelRuralEnum.IMPLANTADO.getId()));
		}
	}

	public void carregarDatums() {
		listaDatums = imovelRuralServiceFacade.listarDatum();
	}

	public void carregarSecaoGeometrica() {
		try {
			listaSecaoGeometrica = imovelRuralServiceFacade.listarClassificacaoSecaoGeometrica();
			listaSecaoGeometrica.remove(0);

			if (!getShowPCT()) {
				if (!Util.isNullOuVazio(imovelRural) && !imovelRural.isMenorQueQuatroModulosFiscais()) {
					// removendo a opção de desenho
					listaSecaoGeometrica.remove(1);
				}
			} else if (getShowPCT()) {
				listaSecaoGeometrica.remove(1);
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public int getSomentePontos() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}

	public void carregarTipoRecuperacao() {
		this.listaTipoRecuperacao = imovelRuralServiceFacade.listarTipoRecuperacao();
	}

	public void mudaTipoPropriedade(ValueChangeEvent event) {
		if (!Util.isNull(event.getNewValue()) && !Util.isNull(imovelRural.getIdeImovelRural())) {
			if (this.tipoVinculoImovelOld != (Integer.parseInt(event.getNewValue().toString()))) {
				if (!Util.isNull(event.getOldValue())
						&& this.tipoVinculoImovelOld != (Integer.parseInt(event.getOldValue().toString()))) {
					atualizarTipoVinculoImovelOld(event);
				}
				RequestContext.getCurrentInstance().execute("confirmacaoAlterarVinculo.show()");
			} else {
				atualizarTipoVinculoImovelOld(event);
			}
		} else {
			atualizarTipoVinculoImovelOld(event);
		}
	}

	private void atualizarTipoVinculoImovelOld(ValueChangeEvent event) {
		try {
			this.tipoVinculoImovelOld = Integer.parseInt(event.getOldValue().toString());
		} catch (Exception e) {
			this.tipoVinculoImovelOld = 0;
		}
	}

	public void mudaTipoTerritorio(ValueChangeEvent event) {

		this.pctImovelRural.setIdeTipoTerritorioPctOld((TipoTerritorioPct) event.getOldValue());

		if (!Util.isNull(event.getNewValue()) && !Util.isNull(imovelRural.getIdeImovelRural())) {
			if (!Util.isNull(event.getOldValue())) {
				if (!event.getNewValue().equals(event.getOldValue())) {

					RequestContext.getCurrentInstance().execute("confirmacaoAlterarTerritorio.show()");
				}
			}

		}
	}

	public void retornaTipoPropriedade() {
		this.tipoVinculoImovel = new TipoVinculoImovel(this.tipoVinculoImovelOld);
	}

	public void confirmaAlteracaoTipoPropriedade() {
		setIsProprietarioImovel(false);

		if (this.tipoVinculoImovel.isProprietario()) {
			setIsProprietarioImovel(true);
		}

		imovelRural.setNumMatricula(null);
		imovelRural.setNumRegistro(null);

		if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse())) {
			imovelRural.getDocumentoImovelRuralPosse().setIdeTipoDocumentoImovelRural(null);
		}

		imovelRural.setNumCcir(null);
		imovelRural.setNumItr(null);
		imovelRural.setCodSipra(null);
		imovelRural.setIdeMunicipioCartorio(null);
		imovelRural.setDesCartorio(null);
		imovelRural.setDesLivro(null);
		imovelRural.setNumFolha(null);

		imovelRural.getDocumentoImovelRuralPosse();

		houveMudanca = true;
	}

	public void confirmaAlteracaoTipoTerritorio() {
		imovelRural.setNumMatricula(null);
		imovelRural.setNumRegistro(null);
		if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse())) {
			imovelRural.getDocumentoImovelRuralPosse().setIdeTipoDocumentoImovelRural(null);
		}

		imovelRural.setNumCcir(null);
		imovelRural.setNumItr(null);
		imovelRural.setCodSipra(null);
		imovelRural.setIdeMunicipioCartorio(null);
		imovelRural.setDesCartorio(null);
		imovelRural.setDesLivro(null);
		imovelRural.setNumFolha(null);
		imovelRural.setNumNirf(null);
		imovelRural.setDscTermoAutodeclaracao(null);

		imovelRural.getDocumentoImovelRuralPosse();
	}

	public void retornarTipoTerritorio() {
		this.pctImovelRural.setIdeTipoTerritorioPct(pctImovelRural.getIdeTipoTerritorioPctOld());
	}

	public List<TipoVinculoImovel> getListTipoVinculos() {
		try {
			return imovelRuralServiceFacade.listarTipoVinculoImoveisCefir();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return new ArrayList<TipoVinculoImovel>();
	}

	public boolean getIsProprietarioImovel() {
		return isProprietarioImovel;
	}

	public void setIsProprietarioImovel(boolean isProprietarioImovel) {
		this.isProprietarioImovel = isProprietarioImovel;
	}

	public String editarImovelRural() {
		try {

			carregarImovelRural();
			if (Util.isNullOuVazio(this.imovelRural)) {
				MensagemUtil.erro("Imóvel não encontrado.");
			} else {
				ContextoUtil.getContexto().setPCT(imovelRuralServiceFacade.isPCT(this.imovelRural));
			}

			ContextoUtil.getContexto().setImovelRural(this.imovelRural);

			if (Util.isNullOuVazio(this.imovelRural.getIdeRequerenteCadastro())) {
				try {
					if (!Util.isNullOuVazio(listProprietariosImovel)) {

						ContextoUtil.getContexto()
								.setRequerenteRequerimento(listProprietariosImovel.iterator().next().getIdePessoa());
						ContextoUtil.getContexto().getReqPapeisDTO().getRequerente()
								.setPessoa(listProprietariosImovel.iterator().next().getIdePessoa());
					}
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
				}
			} else {
				ContextoUtil.getContexto().setRequerenteRequerimento(this.imovelRural.getIdeRequerenteCadastro());
				ContextoUtil.getContexto().getReqPapeisDTO().getRequerente()
						.setPessoa(this.imovelRural.getIdeRequerenteCadastro());
			}
			limparPastaTemporariaArquivoShape();
		} catch (Exception e) {
			MensagemUtil.erro(Util.getString("cefir_msg_erro_edicao_imovel"));
		}

		return PaginaEnum.CADASTRO_IMOVEL_RURAL.getUrl();
	}

	private void carregarImovelRural() {

		try {
			if (!Util.isNullOuVazio(imovelRural)) {
				boolean cedeAreaParaCompensacaoRl = imovelRural.isCedeAreaParaCompensacaoRl();
				imovelRural = imovelRuralServiceFacade.carregarTudo(imovelRural.getIdeImovelRural());
				imovelRural.setCedeAreaParaCompensacaoRl(cedeAreaParaCompensacaoRl);

				if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica()) && !Util
						.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())) {
					imovelRural.setIdeLocalizacaoGeografica(imovelRuralServiceFacade.carregarLocalizacaoGeografica(
							imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
				}

				if (imovelRural.isImovelINCRA() && !Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote())
						&& !Util.isNullOuVazio(
								imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica())) {
					imovelRural.setIdeLocalizacaoGeograficaLote(imovelRuralServiceFacade.carregarLocalizacaoGeografica(
							imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica()));
				}

				if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaPct()) && !Util
						.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaPct().getIdeLocalizacaoGeografica())) {
					imovelRuralServiceFacade.tratarPonto(imovelRural.getIdeLocalizacaoGeograficaPct());
				}

				if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio())
						&& !Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
								.getIdeLocalizacaoGeografica())) {
					imovelRuralServiceFacade.tratarPonto(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio());
				}

				carregarDocumentoImovelRuralPosse();

				if (!Util.isNullOuVazio(imovelRural.getReservaLegal())) {
					imovelRural.setReservaLegal(imovelRuralServiceFacade.carregarTudo(imovelRural.getReservaLegal()));
				}
				if (!Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais())) {
					imovelRural.setOutrosPassivosAmbientais(imovelRuralServiceFacade
							.carregarTudoOutrosPassivosAmbientais(imovelRural.getOutrosPassivosAmbientais()));
				}
				imovelRural.setImovelRuralMudancaStatusJustificativaCollection(imovelRuralServiceFacade
						.listarImovelRuralMudancaStatusJustificativaPorImovelRural(imovelRural));
				imovelRural.setAppCollection(imovelRuralServiceFacade.listarAppByImovelRural(imovelRural));
				imovelRural.setAreaProdutivaCollection(
						imovelRuralServiceFacade.listarAreaProdutivaByImovelRural(imovelRural));
				imovelRural
						.setVegetacaoNativa(imovelRuralServiceFacade.listarVegetacaoNativaByImovelRural(imovelRural));
				imovelRural.setIdeAreaRuralConsolidada(
						imovelRuralServiceFacade.listarAreaRuralConsolidadaNativaByImovelRural(imovelRural));
				imovelRural.setImovelRuralUsoAguaCollection(
						imovelRuralServiceFacade.obterListaUsoAguaImovelRural(imovelRural));
				imovelRural.setAssentadoIncraImovelRuralCollection(
						imovelRuralServiceFacade.listarAssentadoIncraImovelRuralPorImovelRural(imovelRural));
				imovelRural.setImovelRuralRevalidacaoCollection(
						imovelRuralServiceFacade.listarImovelRuralRevalidacaoByImovelRural(imovelRural));
				carregarImovelRuralUsoAgua();
				carregarApps();
				carregarAreasProdutivas();
				carregarResponsaveisTecnicos();
				carregarAsentadosIncraImovelRural();
				if (!Util.isNull(imovelRural.getIndRppn()) && imovelRural.getIndRppn()) {
					imovelRural.setIdeImovelRuralRppn(carregarImovelRuralRppn(this.imovelRural));
				}

				if (!Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais())
						&& !Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao())) {
					aceiteCondicoesRecuperacaoOp = (!Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais()
							.getCronogramaRecuperacao().getCronogramaEtapaCollection()) ? true : false);
				} else {
					aceiteCondicoesRecuperacaoOp = false;
				}
				if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica())
						&& !Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())
						&& imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
								.getIdeClassificacaoSecao().equals(1)) {
					List<DadoGeografico> listaDadosGeo = imovelRuralServiceFacade.listarDadoGeografico(
							imovelRural.getIdeLocalizacaoGeografica(), imovelRural.getIdeLocalizacaoGeografica()
									.getIdeClassificacaoSecao().getIdeClassificacaoSecao());
					this.coordenadaPontoxy = reorganizarCoordenadaPonto(listaDadosGeo.get(0).getCoordGeoNumerica());
				}

				this.imovelRural.setProcessoTramiteImovelRuralCollection(
						imovelRuralServiceFacade.listarProcesTramitImovelRuralPorImovelRural(this.imovelRural));
				if (Util.isNullOuVazio(this.imovelRural.getProcessoTramiteImovelRuralCollection())) {
					this.imovelRural
							.setProcessoTramiteImovelRuralCollection(new ArrayList<ProcessoTramiteImovelRural>());
				}

				carregarListaPradImportado(1);

				if (!Util.isNull(this.imovelRural.getIdeContratoConvenioCefir())
						&& !Util.isNull(this.imovelRural.getIdeContratoConvenioCefir().getIdeContratoConvenioCefir())) {
					carregarContratoConvenioCefir();
				}

				if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio())
						&& !Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
								.getIdeLocalizacaoGeografica())) {
					imovelRuralServiceFacade.tratarPonto(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio());
				}

			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void carregarContratoConvenioCefir() throws Exception {
		this.imovelRural.setIdeContratoConvenioCefir(
				imovelRuralServiceFacade.carregarContratoConvenio(this.imovelRural.getIdeContratoConvenioCefir()));
	}

	private void carregarDocumentoImovelRuralPosse() {
		try {
			if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse())) {

				imovelRural.setDocumentoImovelRuralPosse(imovelRuralServiceFacade
						.carregarTudoDocumentoImovelRuralPosse(imovelRural.getDocumentoImovelRuralPosse()));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarAsentadosIncraImovelRural() {
		try {
			if (!Util.isNullOuVazio(imovelRural.getAssentadoIncraImovelRuralCollection())) {
				for (AssentadoIncraImovelRural assentado : imovelRural.getAssentadoIncraImovelRuralCollection()) {
					assentado.setAssociacaoAssentadoImovelRuralIncraCollection(imovelRuralServiceFacade
							.listarAssociacaoAssentadoImovelRuralIncraPorAssentadoIncraImovelRural(assentado));
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void carregarPCTProprietarioPossuidor() {
		try {
			if (!Util.isNullOuVazio(pctImovelRural)) {
				pctImovelRural.setPctFamiliaCollection(pctFamiliaService.listarPctRepresentanteFamilia(pctImovelRural));

				Collection<PctFamilia> listaMembros = pctFamiliaService.listarPctFamilia(pctImovelRural);

				for (PctFamilia representanteFamilia : pctImovelRural.getPctFamiliaCollection()) {

					if (Util.isNullOuVazio(representanteFamilia.getIdePessoaAssociada())) {
						representanteFamilia.setMembrosFamiliaCollection(new ArrayList<PctFamilia>());

						for (PctFamilia membroFamilia : listaMembros) {

							if (!Util.isNullOuVazio(membroFamilia.getIdePessoaAssociada()) && membroFamilia
									.getIdePessoaAssociada().equals(representanteFamilia.getIdePessoa())) {

								representanteFamilia.getMembrosFamiliaCollection().add(membroFamilia);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void carregarResponsaveisTecnicos() throws Exception {
		List<ResponsavelImovelRural> resps = new ArrayList<ResponsavelImovelRural>();
		imovelRural.setResponsavelImovelRuralCollection(
				imovelRuralServiceFacade.filtrarResponsaveisPorImovelRural(imovelRural));
		for (ResponsavelImovelRural responsavel : imovelRural.getResponsavelImovelRuralCollection()) {
			ResponsavelImovelRural resp = new ResponsavelImovelRural();
			resp = imovelRuralServiceFacade
					.filtrarResponsavelImovelRuralById(responsavel.getIdeResponsavelImovelRural());
			resps.add(resp);
		}
		imovelRural.setResponsavelImovelRuralCollection(resps);
	}

	private void carregarApps() {
		try {
			List<App> apps = new ArrayList<App>();
			if (!Util.isNullOuVazio(imovelRural.getAppCollection())) {
				for (App app : imovelRural.getAppCollection()) {
					app = imovelRuralServiceFacade.carregarTudoApp(app);

					if (Util.isNullOuVazio(app.getCodigoPersistirShape())) {
						String codigo = Util.getStringAlfanumAleatoria(5);

						if (!Util.isNullOuVazio(app.getIdeLocalizacaoGeografica())) {
							codigo += "_" + app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString();
						}

						app.setCodigoPersistirShape(codigo);
					}

					apps.add(app);
				}
			}
			imovelRural.setAppCollection(new ArrayList<App>(apps));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void carregarAreasProdutivas() {
		try {
			List<AreaProdutiva> aps = new ArrayList<AreaProdutiva>();
			if (!Util.isNullOuVazio(imovelRural.getAreaProdutivaCollection())) {
				for (AreaProdutiva ap : imovelRural.getAreaProdutivaCollection()) {
					ap = imovelRuralServiceFacade.carregarTudoAreaProdutiva(ap);

					if (ap.possuiTipologiaSubGrupoCadastrada()) {
						switch (ap.getIdeTipologiaSubgrupo().getIdeTipologia()) {
						case 6:
							if (!Util.isNullOuVazio(ap.getIdeAreaProdutiva())) {
								for (AreaProdutivaTipologiaAtividade ta : ap
										.getAreaProdutivaTipologiaAtividadeCollection()) {
									ta.setIdeAreaProdutivaTipologiaAtividadeAgricultura(
											imovelRuralServiceFacade.carregarTipologiaAtividadeAgriculturaByIde(ta));
								}
							}
							break;
						case 8:
						case 10:
							if (!Util.isNullOuVazio(ap.getIdeAreaProdutiva())) {
								for (AreaProdutivaTipologiaAtividade ta : ap
										.getAreaProdutivaTipologiaAtividadeCollection()) {
									ta.setIdeAreaProdutivaTipologiaAtividadeAnimal(
											imovelRuralServiceFacade.carregarTipologiaAtividadeAnimalByIde(ta));
								}
							}
							break;
						case 16:
							if (!Util.isNullOuVazio(ap.getIdeAreaProdutiva())) {
								for (AreaProdutivaTipologiaAtividade ta : ap
										.getAreaProdutivaTipologiaAtividadeCollection()) {
									ta.setIdeAreaProdutivaTipologiaAtividadePiscicultura(
											imovelRuralServiceFacade.carregarTipologiaAtividadePisciculturaByIde(
													ta.getIdeAreaProdutivaTipologiaAtividadePiscicultura()
															.getIdeAreaProdutivaTipologiaAtividadePiscicultura()));
								}
							}
							break;
						}
					}

					if (Util.isNullOuVazio(ap.getCodigoPersistirShape())) {
						String codigo = Util.getStringAlfanumAleatoria(5);

						if (!Util.isNullOuVazio(ap.getIdeLocalizacaoGeografica())) {
							codigo += "_" + ap.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString();
						}

						ap.setCodigoPersistirShape(codigo);
					}

					aps.add(ap);
				}
			}
			imovelRural.setAreaProdutivaCollection(new ArrayList<AreaProdutiva>(aps));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void carregarImovelRuralUsoAgua() {
		try {
			List<ImovelRuralUsoAgua> listUsoAguaImovelRural = new ArrayList<ImovelRuralUsoAgua>();

			for (ImovelRuralUsoAgua imovelRuralUsoAgua : imovelRural.getImovelRuralUsoAguaCollection()) {
				imovelRuralUsoAgua = imovelRuralServiceFacade.obterPorId(imovelRuralUsoAgua);
				imovelRuralUsoAgua.setIdeLocalizacaoGeografica(imovelRuralServiceFacade.carregarLocalizacaoGeografica(
						imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
				imovelRuralUsoAgua.setTipoFinalidadeCollection(this.imovelRuralServiceFacade
						.listarImovelRuralUsoAguaTipoFinalidadeUsoAgua(imovelRuralUsoAgua));
				if (!Util.isNullOuVazio(imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal()))
					imovelRuralUsoAgua.setIdeLocalizacaoGeograficaFinal(
							imovelRuralServiceFacade.carregarLocalizacaoGeografica(imovelRuralUsoAgua
									.getIdeLocalizacaoGeograficaFinal().getIdeLocalizacaoGeografica()));
				listUsoAguaImovelRural.add(imovelRuralUsoAgua);
			}
			imovelRural.setImovelRuralUsoAguaCollection(new ArrayList<ImovelRuralUsoAgua>(listUsoAguaImovelRural));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void carregarPctImovelRural() {
		try {
			if (!Util.isNullOuVazio(imovelRural)) {

				pctImovelRural = imovelRuralServiceFacade.buscarPctImovelRural(imovelRural);

				/*
				 * carregarPCTProprietarioPossuidor(); carregarPCTRepresentanteFamilia();
				 */

				if (!Util.isNullOuVazio(pctImovelRural)) {
					imovelRural.setIdePctImovelRural(pctImovelRural);
					ContextoUtil.getContexto().setPCT(true);
				}
			}

			if (getShowPCT()) {
				getListarTipoSeguimentoPct();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isHabilitaCadastroAmbiental() {
		if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica())) {
			if ((!Util.isNullOuVazio(this.imovelRural.getIndDesejaCompletarInformacoes())
					&& this.imovelRural.getIndDesejaCompletarInformacoes())
					|| (!imovelRural.isMenorQueQuatroModulosFiscais()) || getShowPCT()) {
				return true;
			}
		}
		return false;
	}

	private void carregarTipoVinculoImovel() {
		try {

			if (!getShowPCT()) {

				if (!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(imovelRural.getIdeImovelRural())) {
					Collection<PessoaImovel> proprietarios = imovelRuralServiceFacade
							.filtrarPessoasPorImovel(imovelRural.getImovel());
					for (PessoaImovel pessoaImovel : proprietarios) {
						if (pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel()
								.equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)) {
							setTipoVinculoImovel(new TipoVinculoImovel(2, "Justo Possuidor"));
							return;
						}
					}
					setTipoVinculoImovel(new TipoVinculoImovel(1, "Proprietário"));
				} else {
					setTipoVinculoImovel(new TipoVinculoImovel());
				}
			} else {
				setTipoVinculoImovel(new TipoVinculoImovel(6, "Representante de comunidade"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void carregarTipoTerritorioPCTVinculo() {

		if (TipoTerritorioPctEnum.PROPRIEDADE.getId()
				.equals(pctImovelRural.getIdeTipoTerritorioPct().getIdeTipoTerritorioPct())) {

		} else if (TipoTerritorioPctEnum.POSSE.getId()
				.equals(pctImovelRural.getIdeTipoTerritorioPct().getIdeTipoTerritorioPct())) {

		} else if (TipoTerritorioPctEnum.CONCESSAO.getId()
				.equals(pctImovelRural.getIdeTipoTerritorioPct().getIdeTipoTerritorioPct())) {

		}

		try {
			if (!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(imovelRural.getIdeImovelRural())
					&& getShowPCT()) {
				Collection<PessoaImovel> proprietarios = imovelRuralServiceFacade
						.filtrarPessoasPorImovel(imovelRural.getImovel());
				for (PessoaImovel pessoaImovel : proprietarios) {
					if (pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel()
							.equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)) {
						setTipoVinculoImovel(new TipoVinculoImovel(2, "Justo Possuidor"));
						return;
					}
				}
				setTipoVinculoImovel(new TipoVinculoImovel(1, "Proprietário"));
			} else {
				setTipoVinculoImovel(new TipoVinculoImovel());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void save(ActionEvent actionEvent) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful", "Welcome :"));
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String onFlowProcess(FlowEvent event) {
		return abaController.onFlow(this, event);
	}

	private void atualizarLabelsPossePropriedade() {
		if (this.tipoVinculoImovel.getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)) {
			if (!imovelRuralServiceFacade.verificarExistenciaProprietarios(getListProprietariosImovel())) {
				this.tituloProprietariosJustoPossuidores = Util.getString("cefir_lbl_justos_possidores_atuais_imovel");
				this.lblListaProprietariosJustoPossuidores = Util.getString("cefir_lbl_justo_possuidores");
			} else {
				this.tituloProprietariosJustoPossuidores = Util.getString("cefir_lbl_proprietarios_atuais_imovel");
				this.lblListaProprietariosJustoPossuidores = Util.getString("cefir_lbl_proprietarios_imovel");
			}

			this.lblBotaoIncluirProprietariosJustoPossuidores = Util.getString("cefir_lbl_incluir_justo_possuidor");
			this.lblDocumentoPossePropriedade = Util.getString("cefir_lbl_documento_posse");
		} else {
			this.tituloProprietariosJustoPossuidores = Util.getString("cefir_lbl_proprietarios_atuais_imovel");
			this.lblListaProprietariosJustoPossuidores = Util.getString("cefir_lbl_proprietarios_imovel");
			this.lblBotaoIncluirProprietariosJustoPossuidores = Util.getString("cefir_lbl_incluir_proprietario");
			this.lblDocumentoPossePropriedade = Util.getString("cefir_lbl_documento_propriedade");
		}
	}

	public void atualizarPoll() {
		Html.executarJS("imovelRuralPoll.stop();");
		String mensagem = (String) SessaoUtil.recuperarObjetoSessao("MENSAGEM_ERRO_VALIDACAO_IMOVEL_RURAL");
		SessaoUtil.removerObjetoSessao("MENSAGEM_ERRO_VALIDACAO_IMOVEL_RURAL");

		if (mensagem != null && !mensagem.isEmpty()) {
			MensagemUtil.alerta(mensagem);
		}
	}

	public void salvarDadosBasicos() {

		if (getShowPCT() && Util.isNullOuVazio(pctImovelRural.getTipoSeguimentoPctCollection())) {
			MensagemUtil.erro("O campo Tipos de seguimentos do PCT é de preenchimento obrigatório");
		}

		montarEnderecoDadosBasicos();

		if (imovelRural.getDocumentoImovelRuralPosse() != null
				&& imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRuralPosse() == null) {
			imovelRural.setDocumentoImovelRuralPosse(null);
		}

		if (!Util.isNull(imovelRural.getReservaLegal())
				&& Util.isNull(imovelRural.getReservaLegal().getIdeReservaLegal())) {
			imovelRural.setReservaLegal(null);
		}

		try {

			LocalizacaoGeografica localizacaoGeograficaLoteTemp = null;

			if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica())
					&& Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())) {
				imovelRural.setIdeLocalizacaoGeografica(null);
			}

			if (imovelRural.isImovelINCRA() && imovelRural.getIdeLocalizacaoGeograficaLote() != null && Util
					.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica())) {
				localizacaoGeograficaLoteTemp = imovelRural.getIdeLocalizacaoGeograficaLote();
				imovelRural.setIdeLocalizacaoGeograficaLote(null);
			}

			if (Util.isNullOuVazio(imovelRural.getIdeImovelRural())) {

				imovelRural.getImovel().setIdeTipoImovel(new TipoImovel(TipoImovelEnum.RURAL));
				imovelRural.getImovel().setIndExcluido(false);

				ImovelRuralDTO imovelRuralDto = new ImovelRuralDTO();
				imovelRuralDto = definePessoasDoImovel();
				imovelRuralDto.setImovelRural(imovelRural);
				imovelRuralDto.setReservaLegal(null);
				imovelRuralDto.setImovelRuralAbastecimento(null);
				imovelRuralDto.setSupressaoVegetacao(null);
				imovelRuralDto.setImovelRuralTac(null);
				imovelRuralDto.setImovelRuralPrad(null);

				if (getShowPCT()) {
					montarPctImovelRural();
					imovelRuralDto.setPctImovelRural(pctImovelRural);
					if (!Util.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio())
							&& Util.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
									.getIdeLocalizacaoGeografica())) {
						this.imovelRural.setIdeLocalizacaoGeograficaPctLimiteTerritorio(null);
					}
				}

				imovelRural.setIdeRequerenteCadastro(imovelRuralDto.getPessoaImovel().getIdePessoa());

				salvarBairroLogradouroOpcaoOutros(imovelRural.getImovel().getIdeEndereco());

				imovelRural.setStatusCadastro(StatusCadastroImovelRuralEnum.REGISTRO_INCOMPLETO.getId());

				imovelRuralServiceFacade.salvarDadosBasicos(imovelRural, imovelRuralDto);

				if (getShowPCT()) {
					imovelRural.setIdePctImovelRural(pctImovelRural);
				}

				if (!Util.isNull(imovelRural.getIdePctImovelRural())) {
					auditoria.salvar(imovelRural.getIdePctImovelRural());
				}

				auditoria.salvar(imovelRural.getImovel().getIdeEndereco());
				imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().setEnderecoCollection(null);
				auditoria.salvar(imovelRural.getImovel().getIdeEndereco().getIdeLogradouro());
				imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro()
						.setLogradouroCollection(null);

				auditoria.salvar(imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro());
				auditoria.salvar(imovelRural);
				auditoria.salvar(imovelRural.getImovel());

				ContextoUtil.getContexto().setImovelRural(imovelRural);

				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S001"));
			} else {
				ImovelRural objAntigo = imovelRuralServiceFacade.carregarTudo(imovelRural.getIdeImovelRural());

				if (Util.isNullOuVazio(listProprietariosImovel)) {
					PessoaImovel pessoaImovelPersist = new PessoaImovel();
					pessoaImovelPersist.setIdePessoa(imovelRural.getIdeRequerenteCadastro());
					pessoaImovelPersist.setIdeTipoVinculoImovel(tipoVinculoImovel);
					pessoaImovelPersist.setIdeImovel(imovelRural.getImovel());

					imovelRuralServiceFacade.salvarImovelPessoa(pessoaImovelPersist);
					getListProprietariosImovel();
				} else {
					for (PessoaImovel pessoaImovel : listProprietariosImovel) {
						if (pessoaImovel.getIndExcluido()) {
							if (!Util.isNull(pessoaImovel.getIdePessoa().getIdePessoa())) {
								imovelRuralServiceFacade.removerPessoaImovel(pessoaImovel);
							}

						} else {
							if (pessoaImovel.getIdePessoa().equals(imovelRural.getIdeRequerenteCadastro())) {
								pessoaImovel.setIdeTipoVinculoImovel(tipoVinculoImovel);
							}
							if (!Util.isNull(pessoaImovel.getIdePessoa())
									&& Util.isNull(pessoaImovel.getIdePessoa().getIdePessoa())) {
								if (!Util.isNull(pessoaImovel.getIdePessoa().getPessoaFisica())) {
									imovelRuralServiceFacade.salvarOuAtualizarPessoaFisica(
											pessoaImovel.getIdePessoa().getPessoaFisica());
								} else {
									imovelRuralServiceFacade.salvarOuAtualizarPessoaJuridica(
											pessoaImovel.getIdePessoa().getPessoaJuridica());
								}
							}
							imovelRuralServiceFacade.salvarImovelPessoa(pessoaImovel);
						}
					}
				}

				salvarBairroLogradouroOpcaoOutros(imovelRural.getImovel().getIdeEndereco());

				enderecoFacade.salvarEndereco(imovelRural.getImovel().getIdeEndereco());
				enderecoFacade.salvarLogradouro(imovelRural.getImovel().getIdeEndereco().getIdeLogradouro());

				if (!Util.isNullOuVazio(imovelRural.getSupressaoVegetacao())) {
					imovelRural.getSupressaoVegetacao().setIdeImovelRural(imovelRural);
				}

				if (!Util.isNullOuVazio(imovelRural.getImovelRuralTac())) {
					imovelRural.getImovelRuralTac().setIdeImovelRural(imovelRural);
				}

				if (!Util.isNullOuVazio(imovelRural.getImovelRuralPrad())) {
					imovelRural.getImovelRuralPrad().setIdeImovelRural(imovelRural);
				}
				atualizarModuloFiscal();
				imovelRuralServiceFacade.validarQtdModulosFiscaisImovelBndes(imovelRural);
				persistirImovelRural();

				if (!imovelRural.getIsFinalizado()) {
					carregarImovelRural();
				}

				if (localizacaoGeograficaLoteTemp != null) {
					imovelRural.setIdeLocalizacaoGeograficaLote(localizacaoGeograficaLoteTemp);
					localizacaoGeograficaLoteTemp = null;
				}

				if (getShowPCT()) {
					imovelRuralServiceFacade.salvarPctImovelRural(pctImovelRural);
					auditoria.atualizar(objAntigo.getIdePctImovelRural(), pctImovelRural);
				}
				auditoria.atualizar(objAntigo.getImovel().getIdeEndereco(), imovelRural.getImovel().getIdeEndereco());
				auditoria.atualizar(objAntigo.getImovel().getIdeEndereco().getIdeLogradouro(),
						imovelRural.getImovel().getIdeEndereco().getIdeLogradouro());
				auditoria.atualizar(objAntigo.getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro(),
						imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro());
				auditoria.atualizar(objAntigo, imovelRural);
				ContextoUtil.getContexto().setImovelRural(imovelRural);
			}
		} catch (ValidationException e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
			return;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002"));
			return;
		}

	}

	public void montarEnderecoDadosBasicos() {

		try {
			if (imovelRural.getImovel().getIdeEndereco().getIdeLogradouro() == null || logradouroSelecionado
					.isHouveMudanca(imovelRural.getImovel().getIdeEndereco().getIdeLogradouro())) {

				Logradouro logradouro = (Logradouro) logradouroSelecionado.clone();
				logradouro.setIdeTipoLogradouro((TipoLogradouro) tipoLogradouroSelecionado.clone());
				imovelRural.getImovel().getIdeEndereco().setIdeLogradouro(logradouro);
			}

			if (imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro() == null || bairroSelecionado
					.isHouveMudanca(imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro())) {

				Bairro bairro = (Bairro) bairroSelecionado.clone();
				bairro.setIdeMunicipio(new Municipio(municipioSelecionado.getIdeMunicipio()));
				imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().setIdeBairro(bairro);
			}

			imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().setIdeMunicipio(municipioSelecionado);
			imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getMunicipio().setIdeEstado(estadoSelecionado);
			if (!Util.isNullOuVazio(imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro())) {
				imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeBairro()
						.setIdeMunicipio(municipioSelecionado);
			}
			imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().setNumCep(Integer.valueOf(numCep));

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void salvarBairroLogradouroOpcaoOutros(Endereco endereco) throws Exception {

		if (endereco != null && endereco.getIdeLogradouro() != null
				&& endereco.getIdeLogradouro().getIdeBairro() != null
				&& endereco.getIdeLogradouro().getIdeBairro().getIdeBairro() != null
				&& endereco.getIdeLogradouro().getIdeBairro().getIdeBairro().equals(OUTRO)) {

			Bairro bairro = endereco.getIdeLogradouro().getIdeBairro();
			bairro.setIdeBairro(null);
			bairro.setIndOrigemCorreio(false);
			bairro.setIndOrigemApi(false);
			if (Util.isNullOuVazio(bairro.getIdeMunicipio())) {
				bairro.setIdeMunicipio(endereco.getIdeLogradouro().getIdeMunicipio());
			}
			enderecoFacade.salvarBairro(bairro);
			//salvarLogradouroOpcaoOutros(endereco);

		} else if (endereco != null && endereco.getIdeLogradouro() != null
				&& endereco.getIdeLogradouro().getIdeLogradouro() != null
				&& OUTRO == (endereco.getIdeLogradouro().getIdeLogradouro())) {
			if((endereco.getIdeLogradouro().getIdeBairro().getIdeBairro()==null || endereco.getIdeLogradouro().getIdeBairro().getIdeBairro()==-1 ) && !endereco.getIdeLogradouro().getIdeBairro().getNomBairro().isEmpty()) {
				endereco.getIdeLogradouro().getIdeBairro();
				endereco.getIdeLogradouro().getIdeBairro().setIdeBairro(null);
				endereco.getIdeLogradouro().getIdeBairro().setIndOrigemCorreio(false);
				endereco.getIdeLogradouro().getIdeBairro().setIndOrigemApi(false);
				if (Util.isNullOuVazio(endereco.getIdeLogradouro().getIdeBairro().getIdeMunicipio())) {
					endereco.getIdeLogradouro().getIdeBairro().setIdeMunicipio(endereco.getIdeLogradouro().getIdeMunicipio());
				}
				enderecoFacade.salvarBairro(endereco.getIdeLogradouro().getIdeBairro());
			}
				
		}
		
		salvarLogradouroOpcaoOutros(endereco);
	}

	private void salvarLogradouroOpcaoOutros(Endereco endereco) throws Exception {
		Logradouro logradouro = endereco.getIdeLogradouro();
		
		if (Util.isNullOuVazio(logradouro.getIdeLogradouro()) || (logradouro.getIdeLogradouro()) == 0 || (logradouro.getIdeLogradouro() == -1)){
		logradouro.setIdeLogradouro(null);
		logradouro.setIdeMunicipio(this.getMunicipioSelecionado());
		logradouro.setIndOrigemCorreio(false); 
		logradouro.setIndOrigemApi(false);
		logradouro.setIdeBairro(endereco.getIdeLogradouro().getIdeBairro());
		logradouro.setNumCep(Integer.valueOf(this.getNumCep()));
		}
		
		if(Util.isNullOuVazio(endereco.getDtcCriacao())){
			endereco.setDtcCriacao(dataAtual);
		}
		
		enderecoFacade.salvarLogradouro(logradouro);
	}
	

	private ImovelRuralDTO definePessoasDoImovel() {
		ImovelRuralDTO imovelRuralDto = new ImovelRuralDTO();
		PessoaImovel pessoaImRequerente = new PessoaImovel();

		PessoaImovel pessoaImSolicitante = null;
		PessoaImovel pessoaImCadastrante = null;
		Pessoa usuarioLogadoACadastrar = null;

		Pessoa requerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa();
		Pessoa solicitante = ContextoUtil.getContexto().getSolicitanteRequerimento();

		if (Util.isNullOuVazio(solicitante)) {
			solicitante = (Pessoa) getAtributoSession("SOLICITANTE_REQ_IMOVEL_RURAL");
		}

		if (getShowPCT()) {
			this.tipoVinculoImovel.setIdeTipoVinculoImovel(6);
			this.tipoVinculoImovel.setNomTipoVinculoImovel("Representante de comunidade");
			pessoaImRequerente.setIdeTipoVinculoPCT(ContextoUtil.getContexto().getTipoVinculoPCT());
		}

		pessoaImRequerente.setIdePessoa(requerente);
		pessoaImRequerente.setIdeTipoVinculoImovel(this.tipoVinculoImovel);
		pessoaImRequerente.setIdeImovel(this.imovelRural.getImovel());
		imovelRuralDto.setPessoaImovel(pessoaImRequerente);

		if (solicitante.getIdePessoa() != requerente.getIdePessoa()) {
			pessoaImSolicitante = new PessoaImovel();
			pessoaImSolicitante.setIdePessoa(solicitante);
			if (ContextoUtil.getContexto().getIsProcPfPjOuRepLegal()) {
				pessoaImSolicitante.setIdeTipoVinculoImovel(new TipoVinculoImovel(5, "Procurador"));
			} else {
				pessoaImSolicitante.setIdeTipoVinculoImovel(new TipoVinculoImovel(3, "Conveniado"));
			}
			pessoaImSolicitante.setIdeImovel(this.imovelRural.getImovel());
			imovelRuralDto.setPessoaImovelSolicitante(pessoaImSolicitante);
		}

		if (!Util.isNullOuVazio(ContextoUtil.getContexto().getSolicitanteRequerimento())) {
			requerente = ContextoUtil.getContexto().getSolicitanteRequerimento();
		}
		if (Util.isNullOuVazio(requerente) || Util.isNullOuVazio(requerente.getIdePessoa())) {
			requerente = ContextoUtil.getContexto().getRequerenteRequerimento();
		}

		Usuario usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
		usuarioLogadoACadastrar = usuarioLogado.getPessoaFisica().getPessoa();

		if (!Util.isNullOuVazio(solicitante)
				&& !usuarioLogadoACadastrar.getIdePessoa().equals(solicitante.getIdePessoa())) {
			pessoaImCadastrante = new PessoaImovel();
			pessoaImCadastrante.setIdePessoa(usuarioLogadoACadastrar);
			if (usuarioLogado.isAtende()) {
				pessoaImCadastrante.setIdeTipoVinculoImovel(new TipoVinculoImovel(4, "Atendente"));
			} else if (usuarioLogado.getIdePerfil().getIdePerfil().equals(PerfilEnum.TECNICO_CONVENIADO.getId())) {
				pessoaImCadastrante.setIdeTipoVinculoImovel(new TipoVinculoImovel(3, "Conveniado"));
			} else if ((usuarioLogado.getIdePerfil().getIdePerfil().equals(PerfilEnum.USUARIO_EXTERNO.getId())
					&& getShowPCT())) {
				pessoaImCadastrante.setIdeTipoVinculoImovel(new TipoVinculoImovel(3, "Conveniado"));
				pessoaImCadastrante.setIdeTipoVinculoPCT(ContextoUtil.getContexto().getTipoVinculoPCT());
				pessoaImCadastrante.setDscTipoVinculoPCTOutros(
						ContextoUtil.getContexto().getTipoVinculoPCT().getDscTipoVinculoPCTOutros());
			}
			pessoaImCadastrante.setIdeImovel(this.imovelRural.getImovel());
			imovelRuralDto.setPessoaQueSalvouOuEditouImovel(pessoaImCadastrante);
		} else if (usuarioLogado.isAtende()
				|| usuarioLogado.getIdePerfil().getIdePerfil().equals(PerfilEnum.TECNICO_CONVENIADO.getId())) {
			pessoaImCadastrante = new PessoaImovel();
			pessoaImCadastrante.setIdePessoa(usuarioLogadoACadastrar);
			if (usuarioLogado.isAtende()) {
				pessoaImCadastrante.setIdeTipoVinculoImovel(new TipoVinculoImovel(4, "Atendente"));
			} else if (usuarioLogado.getIdePerfil().getIdePerfil().equals(PerfilEnum.TECNICO_CONVENIADO.getId())) {
				pessoaImCadastrante.setIdeTipoVinculoImovel(new TipoVinculoImovel(3, "Conveniado"));
			}
			pessoaImCadastrante.setIdeImovel(this.imovelRural.getImovel());
			imovelRuralDto.setPessoaQueSalvouOuEditouImovel(pessoaImCadastrante);
		}

		if ((usuarioLogado.getIdePerfil().getIdePerfil().equals(PerfilEnum.USUARIO_EXTERNO.getId()) && getShowPCT())
				&& !Util.isNullOuVazio(ContextoUtil.getContexto().getTipoVinculoPCT())) {

			if (Util.isNullOuVazio(pessoaImCadastrante)) {

				pessoaImCadastrante = new PessoaImovel();
				pessoaImCadastrante.setIdePessoa(usuarioLogadoACadastrar);
				pessoaImCadastrante.setIdeTipoVinculoImovel(new TipoVinculoImovel(3, "Conveniado"));

				pessoaImCadastrante.setIdeTipoVinculoPCT(ContextoUtil.getContexto().getTipoVinculoPCT());
				pessoaImCadastrante.setDscTipoVinculoPCTOutros(
						ContextoUtil.getContexto().getTipoVinculoPCT().getDscTipoVinculoPCTOutros());

				pessoaImCadastrante.setIdeImovel(this.imovelRural.getImovel());
				imovelRuralDto.setPessoaQueSalvouOuEditouImovel(pessoaImCadastrante);
			}
		}

		if (ContextoUtil.getContexto().getIsProcPfPjOuRepLegal() && usuarioLogado.isAtende()) {
			imovelRuralDto.setVinculaRequerenteDeProcurador(true);
			imovelRuralDto.setTipoVinculoRequerenteDeProcurador(this.tipoVinculoImovel);
		} else {
			imovelRuralDto.setVinculaRequerenteDeProcurador(false);
			imovelRuralDto.setTipoVinculoRequerenteDeProcurador(this.tipoVinculoImovel);
		}

		return imovelRuralDto;
	}

	public Pessoa getRequerenteCadastro() {
		try {
			if (Util.isNullOuVazio(this.imovelRural.getIdeRequerenteCadastro())) {
				if (!Util.isNullOuVazio(this.imovelRural.getIdeImovelRural())) {
					Pessoa pessoaRequerente = imovelRuralServiceFacade
							.listarProprietariosImovel(this.imovelRural.getIdeImovelRural()).iterator().next();
					ContextoUtil.getContexto().setRequerenteRequerimento(pessoaRequerente);
					ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setPessoa(pessoaRequerente);
				} else {
					return ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa();
				}
			} else {
				ContextoUtil.getContexto().setRequerenteRequerimento(this.imovelRural.getIdeRequerenteCadastro());
				ContextoUtil.getContexto().getReqPapeisDTO().getRequerente()
						.setPessoa(this.imovelRural.getIdeRequerenteCadastro());

				if (!Util.isNullOuVazio(this.imovelRural.getIdeRequerenteCadastro().getPessoaJuridica())
						&& !Util.isNullOuVazio(this.imovelRural.getIdeRequerenteCadastro().getPessoaJuridica()
								.getRepresentanteLegalCollection())
						&& !Util.isNullOuVazio(this.imovelRural.getIdeRequerenteCadastro())) {
					for (RepresentanteLegal rep : this.imovelRural.getIdeRequerenteCadastro().getPessoaJuridica()
							.getRepresentanteLegalCollection()) {
						this.setEmail(rep.getIdePessoaFisica().getPessoa().getDesEmail());
						break;
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return ContextoUtil.getContexto().getRequerenteRequerimento();
	}

	public Pessoa getProcuradorCadastro() {
		try {
			if (!Util.isNullOuVazio(this.imovelRural.getIdeImovelRural())) {
				Collection<PessoaImovel> pessoaRequerente = imovelRuralServiceFacade
						.filtrarPessoasPorImovel(new Imovel(this.imovelRural.getIdeImovelRural()));
				for (PessoaImovel pessoaImovel : pessoaRequerente) {
					if (pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel().equals(PROCURADOR)) {
						return pessoaImovel.getIdePessoa();
					}
				}
			}
			if (ContextoUtil.getContexto().getIsProcPfPjOuRepLegal()) {
				return ContextoUtil.getContexto().getSolicitanteRequerimento();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return null;
	}

	public String getTelefoneRequerente() {
		List<Telefone> listTelefones;
		try {
			listTelefones = imovelRuralServiceFacade.filtraTelefonePorPessoa(getRequerenteCadastro());
			if (!Util.isNullOuVazio(listTelefones)) {
				return listTelefones.get(0).getNumTelefone().toString();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return "";
	}

	public String getTelefoneProcurador() {
		List<Telefone> listTelefones;
		try {
			listTelefones = imovelRuralServiceFacade.filtraTelefonePorPessoa(getProcuradorCadastro());
			if (!Util.isNullOuVazio(listTelefones)) {
				return listTelefones.get(0).getNumTelefone().toString();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return "";
	}

	public void verificarExistenciaProprietarios() {
		if (this.tipoVinculoImovel.getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)) {
			if (imovelRuralServiceFacade.verificarExistenciaProprietarios(this.getListProprietariosImovel()))
				RequestContext.getCurrentInstance().execute("dlgExistenciaProprietarios.show()");
		}
		this.renderPollProprietarios = false;
	}

	public void montarEnderecoDocumentacao() {
		try {

			if (logradouroDeclarante != null && (imovelRural == null
					|| imovelRural.getDocumentoImovelRuralPosse() == null
					|| imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante() == null
					|| imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro() == null
					|| logradouroDeclarante.isHouveMudanca(
							imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro()))
					|| isEdicaoDocumentacao) {

				Logradouro logradouro = (Logradouro) logradouroDeclarante.clone();
				logradouro.setIdeTipoLogradouro(tipoLogradouroDeclarante);
				imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().setIdeLogradouro(logradouro);
			}

			if (bairroDeclarante != null && (imovelRural == null || imovelRural.getDocumentoImovelRuralPosse() == null
					|| imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante() == null
					|| imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro() == null
					|| imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro()
							.getIdeBairro() == null
					|| bairroDeclarante.isHouveMudanca(imovelRural.getDocumentoImovelRuralPosse()
							.getIdeEnderecoDeclarante().getIdeLogradouro().getIdeBairro()))) {

				Bairro bairro = (Bairro) bairroDeclarante.clone();
				bairro.setIdeMunicipio(new Municipio(municipioDeclarante.getIdeMunicipio()));
				imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro()
						.setIdeBairro(bairro);
			}

			if (imovelRural != null && imovelRural.getDocumentoImovelRuralPosse() != null
					&& imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante() != null) {

				imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante()
						.setNumEndereco(getEnderecoDeclarante().getNumEndereco());
				imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante()
						.setDesComplemento(getEnderecoDeclarante().getDesComplemento());
				imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro()
						.setIdeMunicipio(municipioDeclarante);
				imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().getMunicipio()
						.setIdeEstado(estadoDeclarante);
				imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro()
						.setNumCep(logradouroPesquisa.getNumCep());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void salvarDocumentacao() {
		try {
			String mensagemSucesso = "";

			if (Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse())) {
				mensagemSucesso = Util.getString("cefir_msg_S001");
			} else {
				mensagemSucesso = Util.getString("cefir_msg_S002");
			}

			List<String> mensagens = validaCamposTelaDocumentacao();

			if (!Util.isNullOuVazio(mensagens)) {
				JsfUtil.addErrorMessages(mensagens);
				return;
			}

			if (!Util.isNull(this.imovelRural.getReservaLegal())
					&& Util.isNull(this.imovelRural.getReservaLegal().getIdeReservaLegal())) {
				this.imovelRural.setReservaLegal(null);
			}
			LocalizacaoGeografica localizacaoGeograficaLoteTemp = null;
			if (imovelRural.isImovelINCRA() && this.imovelRural.getIdeLocalizacaoGeograficaLote() != null && Util
					.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica())) {
				localizacaoGeograficaLoteTemp = this.imovelRural.getIdeLocalizacaoGeograficaLote();
				this.imovelRural.setIdeLocalizacaoGeograficaLote(null);
			}

			if (!Util.isNullOuVazio(this.imovelRural.getReservaLegal())
					&& Util.isNullOuVazio(this.imovelRural.getReservaLegal().getIdeReservaLegal())) {
				this.imovelRural.setReservaLegal(null);
			}

			imovelRural.getDocumentoImovelRuralPosse()
					.setIdeTipoDocumentoImovelRural(imovelRuralServiceFacade
							.findByIdeTipoDocumentoImovelRural(imovelRural.getDocumentoImovelRuralPosse()
									.getIdeTipoDocumentoImovelRural().getIdeTipoDocumentoImovelRural()));

			if (!imovelRural.getIsFinalizado() || isEdicaoDocumentacao) {
				if (imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural()
						.getNumGrupoDocumento() == 4) {
					// carregarEnderecoDeclarante();
					montarEnderecoDocumentacao();
				} else {
					imovelRural.getDocumentoImovelRuralPosse().setIdeEnderecoDeclarante(null);
				}
				persistirImovelRural();
			}

			if (localizacaoGeograficaLoteTemp != null) {
				this.imovelRural.setIdeLocalizacaoGeograficaLote(localizacaoGeograficaLoteTemp);
				localizacaoGeograficaLoteTemp = null;
			}

			JsfUtil.addSuccessMessage(mensagemSucesso);

			return;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao salvar.");
		}
	}

	private void persistirImovelRural() throws Exception {
		ImovelRural objAntigoImovel = imovelRuralServiceFacade.carregarTudo(this.imovelRural.getIdeImovelRural());

		if (imovelRural.getDocumentoImovelRuralPosse() != null
				&& imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural() != null) {

			imovelRural.getDocumentoImovelRuralPosse().setIdeImovelRural(imovelRural);

			if (Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse())) {
				imovelRural.getDocumentoImovelRuralPosse().setDtcCriacao(new Date());
				imovelRural.getDocumentoImovelRuralPosse().setIndExcluido(false);

				if (imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural()
						.getNumGrupoDocumento() == 4) {
					
					salvarBairroLogradouroOpcaoOutros(
							imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante());
					imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().setDtcCriacao(new Date());
				}

				imovelRuralServiceFacade.salvarDocumentoImovelRuralPosse(imovelRural.getDocumentoImovelRuralPosse());
				auditoria.salvar(imovelRural.getDocumentoImovelRuralPosse());

			} else {
				DocumentoImovelRuralPosse objetoAntigoDocumento = imovelRuralServiceFacade
						.carregarTudoDocumentoImovelRuralPosse(imovelRural.getDocumentoImovelRuralPosse());

				if (imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural()
						.getNumGrupoDocumento() == 4) {
					salvarBairroLogradouroOpcaoOutros(
							imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante());
					enderecoFacade
							.salvarEndereco(imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante());

				} else {
					if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante())) {
						imovelRural.getDocumentoImovelRuralPosse().setIdeEnderecoDeclarante(null);
					}
				}

				imovelRuralServiceFacade.atualizarDocumentoImovelRuralPosse(imovelRural.getDocumentoImovelRuralPosse());
				auditoria.atualizar(objetoAntigoDocumento, imovelRural.getDocumentoImovelRuralPosse());
			}
		}

		imovelRuralServiceFacade.atualizarImovelRural(this.imovelRural);
		auditoria.atualizar(objAntigoImovel, this.imovelRural);
	}

	private void excluirDadosCefir() throws EJBTransactionRolledbackException, Exception {

		if (!Util.isNullOuVazio(imovelRural.getIndDesejaCompletarInformacoes())
				&& !imovelRural.getIndDesejaCompletarInformacoes()) {
			if (!Util.isNullOuVazio(imovelRural.getResponsavelImovelRuralCollection())) {
				for (ResponsavelImovelRural resp : imovelRural.getResponsavelImovelRuralCollection()) {
					if (!resp.getIndExcluido()) {
						resp.setIndExcluido(true);
						resp.setDtcExclusao(new Date());
					}
				}
			}

			ReservaLegal reservaLegal = imovelRuralServiceFacade.buscaReservaLegalByImovelRural(imovelRural);
			if (!Util.isNullOuVazio(reservaLegal) && !Util.isNullOuVazio(reservaLegal.getIdeReservaLegal())) {
				LocalizacaoGeografica locReserva = reservaLegal.getIdeLocalizacaoGeografica();
				if (!Util.isNullOuVazio(reservaLegal.getIdeReservaLegalAverbada())) {
					imovelRuralServiceFacade.removerReservaLegalAverbada(reservaLegal.getIdeReservaLegalAverbada());
				}
				imovelRuralServiceFacade.removerReservaLegal(reservaLegal);
				auditoria.excluir(reservaLegal);
				if (!Util.isNullOuVazio(locReserva)) {
					imovelRuralServiceFacade.excluirLocalizacaoGeografica(locReserva);
					auditoria.excluir(locReserva);
				}
			}
		}

		if (Util.isNullOuVazio(imovelRural.getIndApp()) || !imovelRural.getIndApp()) {
			if (!Util.isLazyInitExcepOuNull(imovelRural.getAppCollection())
					&& !Util.isNullOuVazio(imovelRural.getAppCollection())) {
				List<App> listaApp = new ArrayList<App>(imovelRural.getAppCollection());
				for (App app : listaApp) {
					if (!Util.isNullOuVazio(app.getIdeApp()))
						persistirExclusaoApp(app);
				}
				imovelRural.getAppCollection().clear();
			}
		}

		if (Util.isNullOuVazio(imovelRural.getIndAreaProdutiva()) || !imovelRural.getIndAreaProdutiva()) {
			if (!Util.isLazyInitExcepOuNull(imovelRural.getAreaProdutivaCollection())
					&& !Util.isNullOuVazio(imovelRural.getAreaProdutivaCollection())) {
				List<AreaProdutiva> listaAreaProdutiva = new ArrayList<AreaProdutiva>(
						imovelRural.getAreaProdutivaCollection());
				for (AreaProdutiva ap : listaAreaProdutiva) {
					if (!Util.isNullOuVazio(ap.getIdeAreaProdutiva()))
						persistirExclusaoAreaProduitiva(ap);
				}
				imovelRural.getAreaProdutivaCollection().clear();
			}
		}

		if (Util.isNullOuVazio(imovelRural.getIndVegetacaoNativa()) || !imovelRural.getIndVegetacaoNativa()) {
			if (!Util.isNullOuVazio(imovelRural.getVegetacaoNativa())
					&& !Util.isNullOuVazio(imovelRural.getVegetacaoNativa().getIdeVegetacaoNativa())) {
				LocalizacaoGeografica locVn = imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica();
				imovelRuralServiceFacade.excluirVegetacaoNativa(imovelRural.getVegetacaoNativa());
				imovelRuralServiceFacade.excluirLocalizacaoGeografica(locVn);
			}
			imovelRural.setVegetacaoNativa(null);
		}

		excluirImovelRuralRppn();

		if ((Util.isNullOuVazio(imovelRural.getIndSupressaoVegetacao()) || !imovelRural.getIndSupressaoVegetacao())) {
			imovelRuralServiceFacade.excluirSupressaoVegetacaoPorImovelRural(imovelRural.getIdeImovelRural());
			imovelRural.setSupressaoVegetacao(null);
		}

		if ((Util.isNullOuVazio(imovelRural.getIndTac()) || !imovelRural.getIndTac())) {
			imovelRuralServiceFacade.excluirImovelRuralTacPorImovelRural(imovelRural.getIdeImovelRural());
			imovelRural.setImovelRuralTac(null);
		}

		if ((Util.isNullOuVazio(imovelRural.getIndPrad()) || !imovelRural.getIndPrad())) {
			imovelRuralServiceFacade.excluirImovelRuralPradPorImovelRural(imovelRural.getIdeImovelRural());
			imovelRural.setImovelRuralPrad(null);
		}

		if ((Util.isNullOuVazio(this.imovelRural.getIndSubterraneo()) || !this.imovelRural.getIndSubterraneo())
				&& !Util.isNull(listCapSubterranea)) {
			for (ImovelRuralUsoAgua i : listCapSubterranea) {
				if (!Util.isNullOuVazio(i.getIdeImovelRuralUsoAgua())) {
					this.imovelRuralUsoAgua = i;
					prepararImovelRuralUsoAguaParaExclusao();
					persistirExclusaoUsoAgua(this.imovelRuralUsoAgua);
				}
			}
			listCapSubterranea = null;
		}
		if ((Util.isNullOuVazio(this.imovelRural.getIndSuperficial()) || !this.imovelRural.getIndSuperficial())
				&& !Util.isNull(listCapSuperficial)) {
			for (ImovelRuralUsoAgua i : listCapSuperficial) {
				if (!Util.isNullOuVazio(i.getIdeImovelRuralUsoAgua())) {
					this.imovelRuralUsoAgua = i;
					prepararImovelRuralUsoAguaParaExclusao();
					persistirExclusaoUsoAgua(this.imovelRuralUsoAgua);
				}
			}
			listCapSuperficial = null;
		}
		if ((Util.isNullOuVazio(this.imovelRural.getIndLancamentoManancial())
				|| !this.imovelRural.getIndLancamentoManancial()) && !Util.isNull(listLancamentoResiduos)) {
			for (ImovelRuralUsoAgua i : listLancamentoResiduos) {
				if (!Util.isNullOuVazio(i.getIdeImovelRuralUsoAgua())) {
					this.imovelRuralUsoAgua = i;
					prepararImovelRuralUsoAguaParaExclusao();
					persistirExclusaoUsoAgua(this.imovelRuralUsoAgua);
				}
			}
			listLancamentoResiduos = null;
		}
		if ((Util.isNullOuVazio(this.imovelRural.getIndIntervencao()) || !this.imovelRural.getIndIntervencao())
				&& !Util.isNull(listPontoIntervencao)) {
			for (ImovelRuralUsoAgua i : listPontoIntervencao) {
				if (!Util.isNullOuVazio(i.getIdeImovelRuralUsoAgua())) {
					this.imovelRuralUsoAgua = i;
					prepararImovelRuralUsoAguaParaExclusao();
					persistirExclusaoUsoAgua(this.imovelRuralUsoAgua);
				}
			}
			listPontoIntervencao = null;
		}
		if ((Util.isNullOuVazio(this.imovelRural.getIndOutrosPassivos()) || !this.imovelRural.getIndOutrosPassivos())
				&& !Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais())) {
			if (!Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getIdeOutrosPassivosAmbientais())) {
				imovelRuralServiceFacade.removerOutrosPassivosAmbientais(imovelRural.getOutrosPassivosAmbientais());
			}
			imovelRural.setOutrosPassivosAmbientais(null);
		}
	}

	public void salvarQuestionario() {
		try {
			if (!Util.isNull(this.imovelRural.getReservaLegal())
					&& Util.isNull(this.imovelRural.getReservaLegal().getIdeReservaLegal())) {
				this.imovelRural.setReservaLegal(null);
			}
			if (this.validaQuestionario()) {

				LocalizacaoGeografica localizacaoGeograficaLoteTemp = null;

				if (imovelRural.isImovelINCRA() && this.imovelRural.getIdeLocalizacaoGeograficaLote() != null
						&& Util.isNullOuVazio(
								this.imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica())) {
					localizacaoGeograficaLoteTemp = this.imovelRural.getIdeLocalizacaoGeograficaLote();
					this.imovelRural.setIdeLocalizacaoGeograficaLote(null);
				}

				if (!Util.isNullOuVazio(this.imovelRural.getSupressaoVegetacao())) {
					this.imovelRural.getSupressaoVegetacao().setIdeImovelRural(this.imovelRural);
				}

				if (!Util.isNullOuVazio(this.imovelRural.getImovelRuralTac())) {
					this.imovelRural.getImovelRuralTac().setIdeImovelRural(this.imovelRural);
				}

				if (!Util.isNullOuVazio(this.imovelRural.getImovelRuralPrad())) {
					this.imovelRural.getImovelRuralPrad().setIdeImovelRural(this.imovelRural);
				}

				excluirDadosCefir();

				persistirImovelRural();
				carregarImovelRural();

				if (localizacaoGeograficaLoteTemp != null) {
					this.imovelRural.setIdeLocalizacaoGeograficaLote(localizacaoGeograficaLoteTemp);
					localizacaoGeograficaLoteTemp = null;
				}

				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S001"));
			} else {
				RequestContext.getCurrentInstance().execute("$(\"[id*='_next']\").hide()");
				if (!Util.isNullOuVazio(msgDataInvalida)) {
					JsfUtil.addErrorMessage(msgDataInvalida);
				} else {
					JsfUtil.addErrorMessage("Favor preencher os campos obrigatórios");
				}
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	// Início Cadastro de Endereço

	public List<Bairro> getListBairros() {
		if (!Util.isNullOuVazio(getNumCep())) {
			try {
				// listBairros =
				// enderecoFacade.filtrarBairroByCep(logradouroPesquisa);
				if (indPodeModificarMunicipio) {
					listBairros = imovelRuralServiceFacade
							.filtrarBairroPorCepSemDuplicidade(Integer.valueOf(getNumCep()));
					Municipio municipio = obterMunicipioAtual();
					if (!Util.isNullOuVazio(municipio)) {
						setMunicipioSelecionado(municipio);
						setEstadoSelecionado(municipio.getIdeEstado());
					} else {
						setMunicipioSelecionado(new Municipio(0));
						setEstadoSelecionado(new Estado(5));
						habilitaComboMunicipio = true;
					}
					if (!Util.isNullOuVazio(this.bairroSelecionado)
							&& !Util.isNullOuVazio(this.bairroSelecionado.getIdeBairro())
							&& this.bairroSelecionado.getIdeBairro() > 0) {
						if(!listBairros.contains(bairroSelecionado)) {
							setBairroSelecionadoCombo(new Bairro(OUTRO, "Outro", false));
						}
						/*bairroSelecionado = enderecoFacade.filtrarBairroById(bairroSelecionado);
						for (Bairro bairro : listBairros) {
							if (bairro.getNomBairro().trim().toLowerCase()
									.equals(bairroSelecionado.getNomBairro().trim().toLowerCase())) {
								setBairroSelecionado(bairro);
								break;
							}
						}*/
					}
					listBairros.add(new Bairro(OUTRO, "Outro", false));
				} else {
					listBairros = new ArrayList<Bairro>();
				}
				return listBairros;
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			}
		}
		return listBairros;
	}

	public void filtrarMunicipioPorCep() {
		try {
			EnderecoDTO	endereco = cepService.carregar(numCep.toString());
			if(Util.isNullOuVazio(endereco) || Util.isNullOuVazio(numCep)) {
				throw new Exception("CEP inválido.");
			}
			Logradouro logradouroPesquisa = cepService.montarEstrutura();
			if(listLogradouro==null) {
				listLogradouro = new ArrayList<Logradouro>();
			}
			if(listLogradouro.contains(logradouroPesquisa)==false && !logradouroPesquisa.getNomLogradouro().isEmpty()) {
				listLogradouro.add(logradouroPesquisa);
			}
			//boolean newBairro = false;

			//enderecoApi = cepService.getEnderecoApi();
			/*if (cepService.getBairroEnt()==null) {
				enderecoApi.setBairro("Centro");
				newBairro = true;
			}*/
			
			indPodeModificarMunicipio = true;
			podeModificarMunicipio();
			if (!indPodeModificarMunicipio) {
				RequestContext.getCurrentInstance().execute("desabilitarBotaoProximoMesmoFinalizado();");
				JsfUtil.addWarnMessage(Util.getString("cefir_msg_mudanca_municipio_car"));
			} else {
				RequestContext.getCurrentInstance().execute("habilitarBotaoProximo();");
			}

			//getLogradouroSelecionado().setIdeLogradouro(0);
			//getLogradouroSelecionado().setIdeBairro(new Bairro());
			getLogradouroSelecionado().setIdeTipoLogradouro(cepService.getTipoLogradouroEnt());
			getLogradouroSelecionado().setIdeMunicipio(cepService.getMunicipioEnt());
			getLogradouroSelecionado().getIdeMunicipio().setIdeEstado(cepService.getEstadoEnt());
			getLogradouroSelecionado().setIdeLogradouro(cepService.getLogradouroEnt().getIdeLogradouro());
			getLogradouroSelecionado().setNomLogradouro(cepService.getLogradouroEnt().getNomLogradouro());
			if(cepService.getBairroEnt()!=null) {
				bairroSelecionado = cepService.getBairroEnt();
				bairroSelecionadoCombo = cepService.getBairroEnt();
				if(!listBairros.contains(bairroSelecionado)) {
					listBairros.add(bairroSelecionado);
				}
				getLogradouroSelecionado().setIdeBairro(bairroSelecionado);				
			}else {
				bairroSelecionado = null;
				bairroSelecionadoCombo = null;
			}
			
			setTipoLogradouroSelecionado(cepService.getTipoLogradouroEnt());
			setLogradouroSelecionado(cepService.getLogradouroEnt());
			 

		} catch (Exception ex) {
			if (ex.getMessage().contains("-1") || ex.getMessage().equals("CEP inválido.")) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, ex);
				JsfUtil.addErrorMessage("CEP inválido ou não encontrado na base dos correios.");
				enableFormEndereco = false;
			} else {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, ex);
				JsfUtil.addErrorMessage("Erro ao carregar dados do Endereço, contate o administrador do sistema.");
				enableFormEndereco = false;
			}
		}
	}
	

	/**
	 * Verifica se o usuário tentou modificar o município de um imóvel.
	 */
	private boolean podeModificarMunicipio() {
	    if (Util.isNull(this.imovelRural.getIdeImovelRural())){
	        return indPodeModificarMunicipio;
	    }
		if (!this.imovelRural.getStatusCadastro().equals(StatusCadastroImovelRuralEnum.CADASTRADO.getId()) 
				&& !this.imovelRural.getStatusCadastro().equals(StatusCadastroImovelRuralEnum.REGISTRADO.getId()))  {
			Municipio municipioAtual = obterMunicipioAtual();
			if (!Util.isNull(municipioAtual.getIdeMunicipio()) && !Util.isNull(municipioOriginal.getIdeMunicipio())) {
				if (!municipioAtual.getIdeMunicipio().equals(municipioOriginal.getIdeMunicipio())) {
					indPodeModificarMunicipio = false;
				}
			}
		}
		return indPodeModificarMunicipio;
	}

	public boolean getIndPodeModificarMunicipio() {
		return indPodeModificarMunicipio;
	}

	/**
	 * Carrega o municipio através do cep informado na tela de dados básicos (caso o
	 * cep seja válido).
	 * 
	 * @return
	 */
	private Municipio obterMunicipioAtual() {
		try {
			return imovelRuralServiceFacade.isCepValido(Integer.valueOf(getNumCep()));
		} catch (NumberFormatException e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return new Municipio();
	}

	public Endereco getEndereco() {
		return this.imovelRural.getImovel().getIdeEndereco();
	}

	private Logradouro getLogradouro() {
		return getEndereco().getIdeLogradouro();
	}

	public void changeBairro(ValueChangeEvent event) {
		Bairro bairroSelected = (Bairro) event.getNewValue();
		bairroSelected.setIdeMunicipio(null);
		bairroSelected.setLogradouroCollection(null);

		if (bairroSelected.getIdeBairro() == OUTRO) {
			bairroSelecionado = new Bairro();
			bairroSelecionadoCombo = new Bairro();
			logradouroSelecionado = new Logradouro(getLogradouroSelecionado().getIdeMunicipio());
			bairroSelecionado.setIdeBairro(OUTRO);
			bairroSelecionado.setIndOrigemCorreio(false);
			bairroSelecionado.setIndOrigemApi(false);
		} else {
			bairroSelecionado = bairroSelected;
			bairroSelecionadoCombo = bairroSelected;
		}

		if (Util.isNullOuVazio(ideEnderecoSelecionado)) {
			ideEnderecoSelecionado = new Endereco();
			ideEnderecoSelecionado.setIdeLogradouro(new Logradouro());
		}

		ideEnderecoSelecionado.getIdeLogradouro().setIdeBairro(bairroSelecionado);
		houveMudanca = true;
	}

	public boolean getPossuiBairro() {
		return (bairroSelecionado != null && bairroSelecionado.getId() != null);
	}

	public void changeLogradouro(ValueChangeEvent event) {
		try {
			Logradouro logradouroSelected = (Logradouro) event.getNewValue();

			if (logradouroSelected.getIdeLogradouro() == OUTRO) {
				logradouroSelecionado = new Logradouro();
				logradouroSelecionado.setIdeMunicipio(new Municipio(0));
				logradouroSelecionado.getIdeMunicipio().setIdeEstado(new Estado(0));
			} else {
				Logradouro logradouro = enderecoFacade.filtrarLogradouroById(logradouroSelected);

				if (!Util.isNullOuVazio(logradouro)) {
					logradouroSelecionado = logradouro;
					tipoLogradouroSelecionado = logradouro.getIdeTipoLogradouro();
				}
			}

			ideEnderecoSelecionado.getIdeLogradouro()
					.setIdeMunicipio(getEndereco().getIdeLogradouro().getIdeMunicipio());
			houveMudanca = true;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public boolean getCadastraLogradouro() {

		if (getCadastraBairro()) {
			return true;
		}

		if (Util.isNullOuVazio(getLogradouroSelecionado())
				|| Util.isNullOuVazio(getLogradouroSelecionado().getIdeLogradouro())) {
			return false;
		}

		if (!Util.isNullOuVazio(getLogradouroSelecionado().getIndOrigemCorreio())) {
			if (!getLogradouroSelecionado().getIndOrigemCorreio()) {
				return true;
			}
		}
		if (!Util.isNullOuVazio(getLogradouroSelecionado().getIndOrigemApi())) {
			if (!getLogradouroSelecionado().getIndOrigemApi()) {
				return true;
			}
		}
		if (getLogradouroSelecionado().getIdeLogradouro() == OUTRO) {
			return true;
		}

		return false;
	}

	public boolean getCadastraBairro() {

		if (bairroSelecionadoCombo == null) {
			return false;
		}

		if (bairroSelecionadoCombo != null && !Util.isNullOuVazio(bairroSelecionadoCombo)
				&& !Util.isNullOuVazio(bairroSelecionadoCombo.getIdeBairro())) {

			if (!bairroSelecionadoCombo.getIndOrigemCorreio() && !bairroSelecionadoCombo.isIndOrigemApi()) {
				return true;
			}

			if (bairroSelecionadoCombo.getIdeBairro().intValue() == OUTRO) {
				return true;
			}
		}

		return false;
	}

	public List<Logradouro> getListLogradouro() {
		try {
			if (bairroSelecionado != null) {

				if (!Util.isNullOuVazio(bairroSelecionado.getIdeBairro())) {

					if (bairroSelecionado.getIdeBairro() != OUTRO) {

						Bairro bairroTemp = enderecoFacade.filtrarBairroById(bairroSelecionado);

						if (!Util.isNull(bairroTemp)) {
							listLogradouro = imovelRuralServiceFacade.filtrarLogradouroByNome(bairroTemp,
									Integer.valueOf(numCep));
							
							/*Consulta logradouro após adição da API, caso não ache logradouro com indOrigemCorreio*/
							if(Util.isNullOuVazio(listLogradouro)) {
								listLogradouro = imovelRuralServiceFacade.filtrarLogradouroByNomeAndApi(bairroTemp,
										Integer.valueOf(numCep));
							}
								
							listLogradouro.add(new Logradouro(OUTRO, "Outro", null, false, null));

							if (!Util.isNullOuVazio(logradouroSelecionado)
									&& logradouroSelecionado.getIdeLogradouro() != 0) {

								if (!listLogradouro.contains(logradouroSelecionado)) {

									for (Logradouro logradouro : listLogradouro) {
										if (logradouro.getNomLogradouro().trim().toLowerCase().equals(
												logradouroSelecionado.getNomLogradouro().trim().toLowerCase())) {

											logradouroSelecionado = logradouro;
											break;
										}
									}
								}
							} else {
								this.logradouroSelecionado = new Logradouro(0);
							}
						}
					}
				} else {
					listLogradouro = new ArrayList<Logradouro>();
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}

		return listLogradouro;
	}

	public List<TipoLogradouro> getListTipoLogradouro() {
		if (!Util.isNullOuVazio(logradouroSelecionado)
				&& !Util.isNullOuVazio(logradouroSelecionado.getIdeTipoLogradouro())) {
			try {
				listTipoLogradouro = enderecoFacade.listarTipoLogradouro();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			}
		}
		return listTipoLogradouro;
	}

	public List<Estado> getListEstados() {
		if (Util.isNullOuVazio(this.listEstados)) {
			try {
				this.listEstados = enderecoFacade.listarEstados();

			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			}
		}
		return this.listEstados;
	}

	public List<Municipio> getListMunicipios() {
		if (Util.isNullOuVazio(this.listMunicipios)) {
			try {
				listMunicipios = imovelRuralServiceFacade.filtrarListaMunicipiosDaBahia();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			}
		}
		return this.listMunicipios;
	}

	// Fim Cadastro de Endereço

	// Inicio proprietario imovel
	public Collection<PessoaImovel> getListProprietariosImovel() {
		List<PessoaImovel> listaProprietariosNaoExcluidos = new ArrayList<PessoaImovel>();
		try {
			if (Util.isNullOuVazio(listProprietariosImovel)) {
				listProprietariosImovel = imovelRuralServiceFacade
						.filtrarPROPRIETARIOImovel(this.imovelRural.getImovel());
			}
			for (PessoaImovel pessoa : listProprietariosImovel) {
				if (!pessoa.getIndExcluido()) {
					listaProprietariosNaoExcluidos.add(pessoa);
				}
			}
			return listaProprietariosNaoExcluidos;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return listaProprietariosNaoExcluidos;
	}

	public boolean getPossuiProcurador() {
		try {
			return imovelRuralServiceFacade.possuiProcurador(imovelRural);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return ContextoUtil.getContexto().getIsProcPfPjOuRepLegal();
		}
	}

	// Fim proprietario Imovel

	// Início Cadastro Responsável Técnico

	public void cadastrarResponsavelTecnico() {
		isEdicaoResponsavelTecnico = false;
		isPessoaFisicaJaCadastrada = true;
		limparFormResponsavelTecnico();
	}

	public void limparFormResponsavelTecnico() {
		if (!isEdicaoResponsavelTecnico) {
			responsavelTecnicoSelecionado = new ResponsavelImovelRural();
			responsavelTecnicoSelecionado.setIdePessoaFisica(new PessoaFisica());
			responsavelTecnicoSelecionado.getIdePessoaFisica().setIdePais(new Pais());
			responsavelTecnicoSelecionado.getIdePessoaFisica().setIdeEscolaridade(new Escolaridade());
			responsavelTecnicoSelecionado.setIdeDocumentoResponsavel(null);
			listaPais = new ArrayList<Pais>();
			listaEscolaridade = new ArrayList<Escolaridade>();
			carregarListaPais();
			carregarListaEscolaridade();
		}
		disableSalvarResponsavelTecnico = false;
	}

	public void removerResponsavel() {
		if (!Util.isNullOuVazio(responsavelTecnicoSelecionado.getIdeResponsavelImovelRural())) {
			responsavelTecnicoSelecionado.setIndExcluido(true);
			responsavelTecnicoSelecionado.setDtcExclusao(new Date());
			// setEscolaridade();
			if (!imovelRural.getIsFinalizado()) {
				try {
					imovelRuralServiceFacade.salvarOuAtualizarResponsavelImovelRural(responsavelTecnicoSelecionado);
				} catch (Exception e) {
					JsfUtil.addErrorMessage("Erro ao Excluir Responsável.");
					Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
				}
			}

		} else {
			imovelRural.getResponsavelImovelRuralCollection().remove(responsavelTecnicoSelecionado);
		}
		isEdicaoResponsavelTecnico = false;
		limparFormResponsavelTecnico();
		JsfUtil.addErrorMessage("Responsável excluído com sucesso.");
	}

	public void editarResponsavel() {
		listaPais = new ArrayList<Pais>();
		listaEscolaridade = new ArrayList<Escolaridade>();
		carregarListaPais();
		carregarListaEscolaridade();
		isEdicaoResponsavelTecnico = true;
		isPessoaFisicaJaCadastrada = true;
		disableSalvarResponsavelTecnico = false;
	}

	public void salvarResponsavel() {
		if (!Util.isNull(responsavelTecnicoSelecionado)) {
			if (!isEdicaoResponsavelTecnico && existeResponsavelNaLista()) {
				JsfUtil.addErrorMessage("Já existe Pessoa cadastrada.");
				return;
			}
			try {

				if (isEdicaoResponsavelTecnico) {
					setEscolaridade(responsavelTecnicoSelecionado);
					if (!imovelRural.getIsFinalizado())
						persistirResponsavelTecnico(responsavelTecnicoSelecionado);
					RequestContext.getCurrentInstance().execute("dlgCadRespTecnico.hide()");
					JsfUtil.addSuccessMessage("Atualização Efetuada com Sucesso!");
				} else {
					incluirNovoResponsavelImovel();
					if (!imovelRural.getIsFinalizado())
						persistirResponsavelTecnico(responsavelTecnicoSelecionado);

					if (imovelRural.getResponsavelImovelRuralCollection() == null) {
						imovelRural.setResponsavelImovelRuralCollection(new ArrayList<ResponsavelImovelRural>());
					}
					imovelRural.getResponsavelImovelRuralCollection().add(responsavelTecnicoSelecionado);
					RequestContext.getCurrentInstance().execute("dlgCadRespTecnico.hide()");
					JsfUtil.addSuccessMessage("Inclusão Efetuada com Sucesso!");
				}
			} catch (EJBTransactionRolledbackException e) {
				JsfUtil.addErrorMessage("Responsável técnico inativo. Favor entrar em contato com o atendimento.");
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro ao Incluir Responsável.");
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			}
			isEdicaoResponsavelTecnico = false;
			limparFormResponsavelTecnico();
		}
	}

	private void persistirResponsavelTecnico(ResponsavelImovelRural responsavel) throws Exception {
		salvarOuAtualizarPessoaResponsavel(responsavel);
		if (!Util.isLazyInitExcepOuNull(responsavel.getIdeDocumentoResponsavel()))
			imovelRuralServiceFacade.salvarDocumentoObrigatorio(responsavel.getIdeDocumentoResponsavel());

		auditarResponsavelCefir(responsavel);
		responsavel = imovelRuralServiceFacade.salvarOuAtualizarResponsavelImovelRural(responsavel);
		if (!Util.isNull(responsavel.getIdePessoaFisica().getIdeEscolaridade())) {
			setEscolaridade(responsavel);
		}
	}

	private void incluirNovoResponsavelImovel() throws Exception {
		responsavelTecnicoSelecionado.setDtcCriacao(new Date());
		responsavelTecnicoSelecionado.setDtcExclusao(null);
		responsavelTecnicoSelecionado.setIdeImovelRural(this.imovelRural);
		disableSalvarResponsavelTecnico = false;
		setEscolaridade(responsavelTecnicoSelecionado);
	}

	public void salvarOuAtualizarPessoaResponsavel(ResponsavelImovelRural resp) throws Exception {
		if (Util.isNullOuVazio(resp.getIdePessoaFisica().getPessoa().getDtcCriacao())) {
			resp.getIdePessoaFisica().getPessoa().setDtcCriacao(new Date());
		}
		imovelRuralServiceFacade.salvarOuAtualizarPessoaFisica(resp.getIdePessoaFisica());
	}

	public Boolean existeResponsavelNaLista() {
		if (!Util.isNullOuVazio(imovelRural.getResponsavelImovelRuralCollection())) {
			for (ResponsavelImovelRural resp : imovelRural.getResponsavelImovelRuralCollection()) {
				if (!Util.isNullOuVazio(resp) && Util.isNullOuVazio(resp.getDtcExclusao()) && resp.getIdePessoaFisica()
						.getNumCpf().equals(this.responsavelTecnicoSelecionado.getIdePessoaFisica().getNumCpf())) {
					return true;
				}
			}
		}
		return false;
	}

	public void consultarResponsavelByCPF() {
		if (!isEdicaoResponsavelTecnico) {
			String cpf;
			PessoaFisica pessoaFisica;
			try {
				cpf = responsavelTecnicoSelecionado.getIdePessoaFisica().getNumCpf();
				pessoaFisica = imovelRuralServiceFacade
						.filtrarPessoaFisicaByCpf(responsavelTecnicoSelecionado.getIdePessoaFisica());
				if (!Util.isNullOuVazio(pessoaFisica) && !Util.isNullOuVazio(pessoaFisica.getNumCpf())) {
					if (Util.isNullOuVazio(pessoaFisica.getIdePais())
							|| Util.isNullOuVazio(pessoaFisica.getPessoa().getDesEmail())) {
						disableSalvarResponsavelTecnico = true;
						JsfUtil.addErrorMessage(
								"Prezado(a) usuário(a), verificamos que o cadastro do responsável técnico informado encontra-se incompleto. Favor atualizar as informações no cadastro de pessoa física.");
					} else {
						disableSalvarResponsavelTecnico = false;
					}
					responsavelTecnicoSelecionado.setIdePessoaFisica(pessoaFisica);
					isPessoaFisicaJaCadastrada = true;
				} else {
					disableSalvarResponsavelTecnico = false;
					isPessoaFisicaJaCadastrada = false;
					responsavelTecnicoSelecionado.setIdePessoaFisica(new PessoaFisica());
					responsavelTecnicoSelecionado.getIdePessoaFisica().setNumCpf(cpf);
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			}
		}
	}

	public void carregarListaPais() {
		try {
			listaPais = imovelRuralServiceFacade.listarPaises();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void carregarListaEscolaridade() {
		try {
			listaEscolaridade = imovelRuralServiceFacade.listarEscolaridadeResponsavelTecnico();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void uploadArt(FileUploadEvent event) {
		final String tipoDocumento = "ART";
		String nmArquivo = imovelRural.toString() + "_" + responsavelTecnicoSelecionado.getIdePessoaFisica().getNumCpf()
				+ "_" + tipoDocumento;
		String caminhoArquivo = FileUploadUtil.EnviarShape(event,
				DiretorioArquivoEnum.IMOVELRURAL.toString() + FileUploadUtil.getFileSeparator()
						+ imovelRural.getIdeImovelRural().toString() + FileUploadUtil.getFileSeparator() + "ARL",
				nmArquivo);

		responsavelTecnicoSelecionado
				.setIdeDocumentoResponsavel(new DocumentoImovelRural(null, nmArquivo, caminhoArquivo));
		File file = new File(caminhoArquivo.trim());
		responsavelTecnicoSelecionado.getIdeDocumentoResponsavel().setFileSize(file.length());
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
	}

	private void auditarResponsavelCefir(ResponsavelImovelRural resp) throws Exception {
		if (Util.isNullOuVazio(resp.getIdeResponsavelImovelRural())) {
			auditoria.salvar(resp);
		} else {
			ResponsavelImovelRural responsavelAntigo = imovelRuralServiceFacade
					.filtrarResponsavelImovelRuralById(resp.getIdeResponsavelImovelRural());
			auditoria.atualizar(responsavelAntigo, resp);
		}
	}

	public void carregarTipoEstadoConservacao() {
		try {
			if (Util.isNullOuVazio(listaTipoEstadoConservacao)) {
				listaTipoEstadoConservacao = imovelRuralServiceFacade.listarTipoEstadoConservacao();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	/**
	 *
	 * Método que verifica se o {@link TipoEstadoConservacao} RECUPERADA pode ser
	 * exibida para a {@link ReservaLegal} ou {@link App}.
	 *
	 * @return <code>false - Para os imóveis rurais que tiverem o PRA de RL ou de APP e que tiverem com a(s) atividade(s) do cronograma de recuperação concluída(s) (100%)</code>
	 *
	 * @author eduardo.fernandes
	 * @since 26/09/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/7823">#7823</a>
	 */
	protected boolean naoExibeEstadoConservacaoRecuperada(CronogramaRecuperacao cronogramaRecuperacao) {
		return Util.isNull(imovelRural) || Util.isNull(imovelRural.getIndDesejaAderirPra())
				|| !imovelRural.getIndDesejaAderirPra() || !imovelRural.getIsFinalizado()
				|| !cronogramaRecuperacaoCompletos(cronogramaRecuperacao);
	}

	/**
	 * Método que verifica se os {@link CronogramaEtapa} estão com 100%.
	 *
	 * @param cronogramaRecuperacao
	 * @return <code>true - se todos os {@link CronogramaEtapa} associados ao {@link CronogramaRecuperacao} estão com 100%</code>
	 *
	 * @author eduardo.fernandes
	 * @since 26/09/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/7823">#7823</a>
	 */
	protected boolean cronogramaRecuperacaoCompletos(CronogramaRecuperacao cronogramaRecuperacao) {
		if (!Util.isNull(cronogramaRecuperacao)
				&& !Util.isNullOuVazio(cronogramaRecuperacao.getCronogramaEtapaCollection())) {
			for (CronogramaEtapa etapa : cronogramaRecuperacao.getCronogramaEtapaCollection()) {
				if (!etapa.getPercentual().equals(100)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public void limparFormCronogramaEtapa(Integer pAbaRepresentacao) {
		cronogramaEtapaSelecionado = new CronogramaEtapa();
		cronogramaEtapaSelecionado.setIdeTipoRecuperacao(new TipoRecuperacao());
		tipoCronogramaEtapa = pAbaRepresentacao;
	}

	public String adicionarCronogramaEtapa(Integer pAbaRepresentacao) {
		try {
			if (!validarDatas())
				return null;

			if (!verificaPercentual(cronogramaEtapaSelecionado.getPercentual())) {
				return null;
			}

			setTipoRecuperacao(cronogramaEtapaSelecionado);

			if (pAbaRepresentacao == 1) {
				if (!Util.isNull(imovelRural.getReservaLegal().getCronogramaRecuperacao()) && Util.isNullOuVazio(
						imovelRural.getReservaLegal().getCronogramaRecuperacao().getCronogramaEtapaCollection())) {
					imovelRural.getReservaLegal().getCronogramaRecuperacao()
							.setCronogramaEtapaCollection(new ArrayList<CronogramaEtapa>());
				}

				cronogramaEtapaSelecionado
						.setIdeCronogramaRecuperacao(imovelRural.getReservaLegal().getCronogramaRecuperacao());

				if (!isEdicaoCronogramaEtapa) {
					imovelRural.getReservaLegal().getCronogramaRecuperacao().getCronogramaEtapaCollection()
							.add(cronogramaEtapaSelecionado);
				}
			} else if (pAbaRepresentacao == 2) {
				if (!Util.isNull(appSelecionada.getCronogramaRecuperacao()) && Util
						.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection())) {
					appSelecionada.getCronogramaRecuperacao()
							.setCronogramaEtapaCollection(new ArrayList<CronogramaEtapa>());
				}

				cronogramaEtapaSelecionado.setIdeCronogramaRecuperacao(appSelecionada.getCronogramaRecuperacao());

				if (!isEdicaoCronogramaEtapa) {
					appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection()
							.add(cronogramaEtapaSelecionado);
				}
			} else if (pAbaRepresentacao == 3) {
				if (!Util.isNull(imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao())
						&& Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao()
								.getCronogramaEtapaCollection())) {
					imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao()
							.setCronogramaEtapaCollection(new ArrayList<CronogramaEtapa>());
				}

				cronogramaEtapaSelecionado.setIdeCronogramaRecuperacao(
						imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao());

				if (!isEdicaoCronogramaEtapa) {
					imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao().getCronogramaEtapaCollection()
							.add(cronogramaEtapaSelecionado);
				}
			}

			cronogramaEtapaSelecionado = new CronogramaEtapa();
			RequestContext.getCurrentInstance().execute("dlgCronogramaEtapa.hide()");

			if (isEdicaoCronogramaEtapa) {
				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S002"));
			}

			return "";
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			return null;
		}
	}

	private boolean validarDatas() {
		return Util.verificaDatas(cronogramaEtapaSelecionado.getMesInicio(), cronogramaEtapaSelecionado.getAnoInicio(),
				cronogramaEtapaSelecionado.getMesFim(), cronogramaEtapaSelecionado.getAnoFim());
	}

	public boolean verificaPercentual(Integer pValor) {
		if (pValor > 100) {
			JsfUtil.addErrorMessage("O valor do Percentual Realizado não pode ultrapassar 100%.");
			return false;
		}
		return true;
	}

	public void carregarListaPradImportado(Integer pAbaRepresentacao) {
		switch (pAbaRepresentacao) {
		case 1:
			if (!Util.isNullOuVazio(imovelRural.getReservaLegal())
					&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao())
					&& !Util.isNullOuVazio(
							imovelRural.getReservaLegal().getCronogramaRecuperacao().getIndPradImportada())
					&& imovelRural.getReservaLegal().getCronogramaRecuperacao().getIndPradImportada()) {
				try {
					listaPradImportadosRl = new ArrayList<DocumentoImovelRural>();
					addPradReservaLegal(pAbaRepresentacao);
					addPradApp(pAbaRepresentacao);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
				}
			} else {
				listaPradImportadosRl = new ArrayList<DocumentoImovelRural>();
			}

			break;
		case 2:
			if (!Util.isNull(appSelecionada.getCronogramaRecuperacao())
					&& !Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection())
					&& (!Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getIndPradImportada())
							&& appSelecionada.getCronogramaRecuperacao().getIndPradImportada())) {
				try {
					listaPradImportadosApp = new ArrayList<DocumentoImovelRural>();
					addPradReservaLegal(pAbaRepresentacao);
					addPradApp(pAbaRepresentacao);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
				}
			} else {
				listaPradImportadosApp = new ArrayList<DocumentoImovelRural>();
			}
			break;
		}
	}

	private void addPradReservaLegal(Integer tipoLista) throws Exception {
		if (!Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao())) {
			List<CronogramaRecuperacao> lListCronogramaRL = imovelRuralServiceFacade
					.listarCronogramaRecuperacaoById(imovelRural.getReservaLegal().getCronogramaRecuperacao());
			for (CronogramaRecuperacao lCronograma : lListCronogramaRL) {
				if (!Util.isNullOuVazio(lCronograma.getIdeDocumentoObrigatorio())) {
					if (tipoLista == 1)
						listaPradImportadosRl.add(lCronograma.getIdeDocumentoObrigatorio());
					else
						listaPradImportadosApp.add(lCronograma.getIdeDocumentoObrigatorio());
				}
			}
		}
	}

	private void addPradApp(Integer tipoLista) throws Exception {
		if (!Util.isNullOuVazio(imovelRural.getAppCollection())) {
			Iterator<App> iterator = imovelRural.getAppCollection().iterator();
			while (iterator.hasNext()) {
				CronogramaRecuperacao lCronograma = imovelRuralServiceFacade
						.listarCronogramaRecuperacaoByApp(iterator.next());
				if (!Util.isNullOuVazio(lCronograma)) {
					if (!Util.isNullOuVazio(lCronograma.getIdeDocumentoObrigatorio())) {
						if (tipoLista == 1)
							listaPradImportadosRl.add(lCronograma.getIdeDocumentoObrigatorio());
						else
							listaPradImportadosApp.add(lCronograma.getIdeDocumentoObrigatorio());
					}
				}
			}
		}
	}

	public void excluirDocumentoPrad(Integer pAbaRepresentacao) {
		try {
			switch (pAbaRepresentacao) {
			case 1:
				ReservaLegal reservaLegalAntiga = imovelRuralServiceFacade.carregarTudo(imovelRural.getReservaLegal());

				if (!Util.isNullOuVazio(reservaLegalAntiga)
						&& !Util.isNullOuVazio(
								reservaLegalAntiga.getCronogramaRecuperacao().getIdeDocumentoObrigatorio())
						&& !Util.isNullOuVazio(reservaLegalAntiga.getCronogramaRecuperacao()
								.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())) {
					List<CronogramaRecuperacao> lCronogramasCadastrados = obterCronogramaPorDocumento(
							reservaLegalAntiga.getCronogramaRecuperacao());

					if (!cronogramaPodeSerExcluido(lCronogramasCadastrados)) {
						tratarDocumentoPRAAssociado(lCronogramasCadastrados,
								reservaLegalAntiga.getCronogramaRecuperacao());
					}

					imovelRural.getReservaLegal().getCronogramaRecuperacao().setIdeDocumentoObrigatorio(null);
				} else {
					imovelRural.getReservaLegal().getCronogramaRecuperacao().setIdeDocumentoObrigatorio(null);
				}
				break;
			case 2:
				App appAntiga = imovelRuralServiceFacade.carregarTudoApp(appSelecionada);

				if (!Util.isNullOuVazio(appAntiga)
						&& !Util.isNullOuVazio(appAntiga.getCronogramaRecuperacao().getIdeDocumentoObrigatorio())
						&& !Util.isNullOuVazio(appAntiga.getCronogramaRecuperacao().getIdeDocumentoObrigatorio()
								.getIdeDocumentoObrigatorio())) {
					List<CronogramaRecuperacao> lCronogramasCadastrados = obterCronogramaPorDocumento(
							appAntiga.getCronogramaRecuperacao());

					if (cronogramaPodeSerExcluido(lCronogramasCadastrados)) {
						tratarDocumentoPRAAssociado(lCronogramasCadastrados, appSelecionada.getCronogramaRecuperacao());
					}

					appSelecionada.getCronogramaRecuperacao().setIdeDocumentoObrigatorio(null);
				} else {
					appSelecionada.getCronogramaRecuperacao().setIdeDocumentoObrigatorio(null);
				}
				break;
			case 3:
				this.imovelRural.getOutrosPassivosAmbientais().setIdeDocumentoPrad(null);
				break;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível excluir o arquivo!");
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	protected boolean cronogramaPodeSerExcluido(List<CronogramaRecuperacao> lCronogramasCadastrados) {
		return !Util.isNullOuVazio(lCronogramasCadastrados) && lCronogramasCadastrados.size() == 1;
	}

	protected void tratarDocumentoPRAAssociado(List<CronogramaRecuperacao> lCronogramasCadastrados,
			CronogramaRecuperacao cronogramaRecuperacaoSelecionado) throws Exception {
		for (CronogramaRecuperacao cronogramaRecuperacao : lCronogramasCadastrados) {
			if (!Util.isNull(cronogramaRecuperacao.getIdeCronogramaRecuperacao())
					&& !cronogramaRecuperacao.getIdeCronogramaRecuperacao()
							.equals(cronogramaRecuperacaoSelecionado.getIdeCronogramaRecuperacao())) {
				cronogramaRecuperacao.setIndPradImportada(false);
				imovelRuralServiceFacade.atualizarCronogramaRecuperacao(cronogramaRecuperacao);
			}
		}
		// return !Util.isNullOuVazio(lCronogramasCadastrados) &&
		// lCronogramasCadastrados.size() == 1;
	}

	protected List<CronogramaRecuperacao> obterCronogramaPorDocumento(CronogramaRecuperacao cronogramaRecuperacao)
			throws Exception {
		return this.imovelRuralServiceFacade.filtrarCronogramaRecuperacaoByDocumento(cronogramaRecuperacao);
	}

	public void excluirShape(LocalizacaoGeografica pLocalizacaoGeografica, Integer pAbaRepresentacao) {
		try {
			String diretorioExcluir = "";

			switch (pAbaRepresentacao) {
			case 1:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
						+ "_" + TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId();

				if (imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isDesenho()) {
					imovelRural.getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
				} else {
					imovelRural.getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
				}

				break;
			case 2:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
						+ "_" + TemaGeoseiaEnum.RESERVA_LEGAL.getId();

				if (imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
						.isDesenho()) {
					imovelRural.getReservaLegal().getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
				} else {
					imovelRural.getReservaLegal().getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
				}

				break;
			case 3:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
						+ "_" + TemaGeoseiaEnum.APP.getId() + "_" + appSelecionada.getCodigoPersistirShape();

				if (appSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isDesenho()) {
					appSelecionada.getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
				} else {
					appSelecionada.getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
				}

				break;
			case 4:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
						+ "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId() + "_"
						+ areaProdutivaSelecionada.getCodigoPersistirShape();

				if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isDesenho()) {
					areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
				} else {
					areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
				}

				break;
			case 5:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
						+ "_" + TemaGeoseiaEnum.VEGETACAO_NATIVA.getId();

				if (imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
						.isDesenho()) {
					imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
				} else {
					imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
				}

				break;
			case 12:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
						+ "_" + TemaGeoseiaEnum.LOTE_ASSENTAMENTO.getId();
				if (this.imovelRural.isImovelINCRA()) {
					excluirPontosDosAssentados();
				}
				break;
			case 13:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
						+ "_" + TemaGeoseiaEnum.RPPN.getId();

				if (imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
						.isDesenho()) {
					imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
				} else {
					imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
				}

				break;
			case 14:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
						+ "_" + TemaGeoseiaEnum.AREA_RURAL_CONSOLIDADA.getId();

				if (imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
						.isDesenho()) {
					imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
				} else {
					imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
				}

				break;
			case 19:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
						+ "_" + TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId();

				if (imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeClassificacaoSecao()
						.isDesenho()) {
					imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
				} else {
					imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
				}

				break;
			case 22:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
						+ "_" + TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId();

				if (imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
						.getIdeClassificacaoSecao().isDesenho()) {
					imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
				} else {
					imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
				}

				break;
			case 23:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
						+ "_" + TemaGeoseiaEnum.PRAD_APP.getId() + "_" + appSelecionada.getCodigoPersistirShape();

				if (appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao()
						.isDesenho()) {
					appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
				} else {
					appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
				}

				break;
			default:
				break;
			}

			pLocalizacaoGeografica.setListArquivosShape(null);
			pLocalizacaoGeografica.setNovosArquivosShapeImportados(false);

			if (!Util.isNullOuVazio(diretorioExcluir)) {
				FileUploadUtil.removerArquivos(new File(diretorioExcluir));
			}

			if (imovelRural.getIsFinalizado()) {
				pLocalizacaoGeografica.setDadoGeograficoCollection(null);
				pLocalizacaoGeografica.setLocalizacaoExcluida(true);
			} else {
				imovelRuralServiceFacade
						.excluirDadosPersistidosLocalizacao(pLocalizacaoGeografica.getIdeLocalizacaoGeografica());
				pLocalizacaoGeografica.setDadoGeograficoCollection(null);
				pLocalizacaoGeografica.setLocalizacaoExcluida(false);
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void excluirArquivo(Integer pAbaRepresentacao) {
		List<String> lstTemp = new ArrayList<String>();
		String diretorioExcluir = "";

		switch (pAbaRepresentacao) {
		case 1:
			lstTemp = imovelRural.getIdeLocalizacaoGeografica().getListArquivosShape();
			diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_"
					+ TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId() + FileUploadUtil.getFileSeparator()
					+ arquivoShapeExcluir;
			if ((arquivoShapeExcluir.substring(arquivoShapeExcluir.lastIndexOf('.')).toLowerCase().equals(".prj"))) {
				imovelRural.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
			}
			break;
		case 2:
			lstTemp = imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape();
			diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_"
					+ TemaGeoseiaEnum.RESERVA_LEGAL.getId() + FileUploadUtil.getFileSeparator() + arquivoShapeExcluir;
			if ((arquivoShapeExcluir.substring(arquivoShapeExcluir.lastIndexOf('.')).toLowerCase().equals(".prj"))) {
				imovelRural.getReservaLegal().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(new SistemaCoordenada());
			}
			break;
		case 3:
			lstTemp = appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape();
			diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_"
					+ TemaGeoseiaEnum.APP.getId() + "_" + appSelecionada.getCodigoPersistirShape()
					+ FileUploadUtil.getFileSeparator() + arquivoShapeExcluir;
			if ((arquivoShapeExcluir.substring(arquivoShapeExcluir.lastIndexOf('.')).toLowerCase().equals(".prj"))) {
				appSelecionada.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
			}
			break;
		case 4:
			lstTemp = areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape();
			diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_"
					+ TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId() + "_"
					+ areaProdutivaSelecionada.getCodigoPersistirShape() + FileUploadUtil.getFileSeparator()
					+ arquivoShapeExcluir;
			if ((arquivoShapeExcluir.substring(arquivoShapeExcluir.lastIndexOf('.')).toLowerCase().equals(".prj"))) {
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
			}
			break;
		case 5:
			lstTemp = imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape();
			diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_"
					+ TemaGeoseiaEnum.VEGETACAO_NATIVA.getId() + FileUploadUtil.getFileSeparator()
					+ arquivoShapeExcluir;
			if ((arquivoShapeExcluir.substring(arquivoShapeExcluir.lastIndexOf('.')).toLowerCase().equals(".prj"))) {
				imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(new SistemaCoordenada());
			}
			break;
		case 12:
			lstTemp = imovelRural.getIdeLocalizacaoGeograficaLote().getListArquivosShape();
			diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_"
					+ TemaGeoseiaEnum.LOTE_ASSENTAMENTO.getId() + FileUploadUtil.getFileSeparator()
					+ arquivoShapeExcluir;
			break;
		case 13:
			lstTemp = imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getListArquivosShape();
			diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_"
					+ TemaGeoseiaEnum.RPPN.getId() + FileUploadUtil.getFileSeparator() + arquivoShapeExcluir;
			if ((arquivoShapeExcluir.substring(arquivoShapeExcluir.lastIndexOf('.')).toLowerCase().equals(".prj"))) {
				imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(new SistemaCoordenada());
			}
			break;
		case 14:
			lstTemp = imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getListArquivosShape();
			diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_"
					+ TemaGeoseiaEnum.AREA_RURAL_CONSOLIDADA.getId() + FileUploadUtil.getFileSeparator()
					+ arquivoShapeExcluir;
			if ((arquivoShapeExcluir.substring(arquivoShapeExcluir.lastIndexOf('.')).toLowerCase().equals(".prj"))) {
				imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(new SistemaCoordenada());
			}
			break;
		case 19:
			lstTemp = imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getListArquivosShape();
			diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_"
					+ TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId() + FileUploadUtil.getFileSeparator()
					+ arquivoShapeExcluir;
			if ((arquivoShapeExcluir.substring(arquivoShapeExcluir.lastIndexOf('.')).toLowerCase().equals(".prj"))) {
				imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
						.setIdeSistemaCoordenada(new SistemaCoordenada());
			}
			break;
		case 22:
			lstTemp = imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
					.getListArquivosShape();
			diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_"
					+ TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId() + FileUploadUtil.getFileSeparator()
					+ arquivoShapeExcluir;
			if ((arquivoShapeExcluir.substring(arquivoShapeExcluir.lastIndexOf('.')).toLowerCase().equals(".prj"))) {
				imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
						.setIdeSistemaCoordenada(new SistemaCoordenada());
			}
			break;
		case 23:
			lstTemp = appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape();
			diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_"
					+ TemaGeoseiaEnum.PRAD_APP.getId() + "_" + appSelecionada.getCodigoPersistirShape()
					+ FileUploadUtil.getFileSeparator() + arquivoShapeExcluir;
			if ((arquivoShapeExcluir.substring(arquivoShapeExcluir.lastIndexOf('.')).toLowerCase().equals(".prj"))) {
				appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
						.setIdeSistemaCoordenada(new SistemaCoordenada());
			}
			break;
		default:
			break;
		}

		if (lstTemp.size() > 0) {
			lstTemp.remove(arquivoShapeExcluir);

			if (!Util.isNullOuVazio(diretorioExcluir)) {
				FileUploadUtil.removerArquivos(new File(diretorioExcluir));
			}
		} else {
			JsfUtil.addWarnMessage("Não é possível excluir este arquivo.");
		}
	}

	public void verificaTipoSecaoGeometricaShape(Integer pAbaRepresentacao) throws Exception {
		File diretorioExcluir = null;

		switch (pAbaRepresentacao) {
		case 1:
			imovelRural.getIdeLocalizacaoGeografica()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(imovelRural
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			if (imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()
					.equals(3)) {
				imovelRural.getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
			} else {
				imovelRural.getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
			}

			imovelRural.getIdeLocalizacaoGeografica().setListArquivosShape(null);

			diretorioExcluir = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
					+ "_" + TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId());
			FileUploadUtil.removerArquivos(diretorioExcluir);

			break;
		case 2:
			imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(
					retornaClassificacaoSecaoGeometricaSelecionado(imovelRural.getReservaLegal()
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			if (imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao().equals(3)) {
				imovelRural.getReservaLegal().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
			} else {
				imovelRural.getReservaLegal().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
			}

			imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().setListArquivosShape(null);

			diretorioExcluir = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
					+ "_" + TemaGeoseiaEnum.RESERVA_LEGAL.getId());
			FileUploadUtil.removerArquivos(diretorioExcluir);

			break;
		case 3:
			appSelecionada.getIdeLocalizacaoGeografica()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(appSelecionada
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			if (appSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()
					.equals(3)) {
				appSelecionada.getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
			} else {
				appSelecionada.getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
			}

			appSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(null);

			diretorioExcluir = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
					+ "_" + TemaGeoseiaEnum.APP.getId() + "_" + appSelecionada.getCodigoPersistirShape());
			FileUploadUtil.removerArquivos(diretorioExcluir);

			break;
		case 4:
			areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(areaProdutivaSelecionada
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao().equals(3)) {
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
			} else {
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
			}

			areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(null);

			diretorioExcluir = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
					+ "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId() + "_"
					+ areaProdutivaSelecionada.getCodigoPersistirShape());
			FileUploadUtil.removerArquivos(diretorioExcluir);

			break;
		case 5:
			imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(
					retornaClassificacaoSecaoGeometricaSelecionado(imovelRural.getVegetacaoNativa()
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			if (imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao().equals(3)) {
				imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
			} else {
				imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
			}

			imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setListArquivosShape(null);

			diretorioExcluir = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
					+ "_" + TemaGeoseiaEnum.VEGETACAO_NATIVA.getId());
			FileUploadUtil.removerArquivos(diretorioExcluir);

			break;
		case 13:
			imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(
					retornaClassificacaoSecaoGeometricaSelecionado(imovelRural.getIdeImovelRuralRppn()
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			if (imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao().equals(3)) {
				imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
			} else {
				imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
			}

			imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setListArquivosShape(null);

			diretorioExcluir = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
					+ "_" + TemaGeoseiaEnum.RPPN.getId());
			FileUploadUtil.removerArquivos(diretorioExcluir);

			break;
		case 14:
			imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(
					retornaClassificacaoSecaoGeometricaSelecionado(imovelRural.getIdeAreaRuralConsolidada()
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			if (imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao().equals(3)) {
				imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
			} else {
				imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
			}

			imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().setListArquivosShape(null);

			diretorioExcluir = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
					+ "_" + TemaGeoseiaEnum.AREA_RURAL_CONSOLIDADA.getId());
			FileUploadUtil.removerArquivos(diretorioExcluir);

			break;
		case 15:
			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(
							imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeClassificacaoSecao()
									.getIdeClassificacaoSecao()));

			if (imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao().equals(3)) {
				imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
			} else {
				imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
			}

			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setListArquivosShape(null);

			diretorioExcluir = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
					+ "_" + TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId());
			FileUploadUtil.removerArquivos(diretorioExcluir);

			break;
		case 22:
			imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(
							imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
									.getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			if (imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
					.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(3)) {
				imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
			} else {
				imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
			}

			imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
					.setListArquivosShape(null);

			diretorioExcluir = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
					+ "_" + TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId());
			FileUploadUtil.removerArquivos(diretorioExcluir);

			break;
		case 23:
			appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeClassificacaoSecao(
					retornaClassificacaoSecaoGeometricaSelecionado(appSelecionada.getCronogramaRecuperacao()
							.getLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			if (appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao().equals(3)) {
				appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(4)); // CARREGAR SIRGAS 2000
			} else {
				appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
						.setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(0));
			}

			appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().setListArquivosShape(null);

			diretorioExcluir = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
					+ "_" + TemaGeoseiaEnum.PRAD_APP.getId() + "_" + appSelecionada.getCodigoPersistirShape());
			FileUploadUtil.removerArquivos(diretorioExcluir);

			break;
		}
	}

	// Fim Cadastro Reserva Legal

	// Início Cadastro APP

	public String cadastrarApp() {
		appSelecionada = new App();
		appSelecionada.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		appSelecionada.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
		appSelecionada.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
		appSelecionada.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(new SistemaCoordenada());
		appSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(null);
		appSelecionada.setCodigoPersistirShape(Util.getStringAlfanumAleatoria(5));
		appSelecionada.setIdeTipoApp(new TipoApp());
		appSelecionada.setIdeTipoEstadoConservacao(new TipoEstadoConservacao());
		carregarApp();
		visualizacaoApp = false;
		isEdicaoApp = false;
		return null;
	}

	public void editarApp() {
		carregarApp();
		tipoEstadoConservacaoInicial = appSelecionada.getIdeTipoEstadoConservacao();
		visualizacaoApp = false;
		isEdicaoApp = true;
	}

	public void visualizarApp() {
		carregarApp();
		visualizacaoApp = true;
		isEdicaoApp = false;
	}

	public void carregarApp() {
		carregarTipoApp();

		if (!Util.isNullOuVazio(appSelecionada) && !Util.isNullOuVazio(appSelecionada.getIdeTipoApp())
				&& !Util.isNullOuVazio(appSelecionada.getIdeTipoApp().getIdeTipoApp())) {
			carregarSubTipoApp();
		}

		carregarTipoEstadoConservacao();
		carregarTipoRecuperacao();
		carregarListaPradImportado(2);

		if (!Util.isNullOuVazio(appSelecionada) && appSelecionada.getCronogramaRecuperacao() != null) {
			aceiteCondicoesRecuperacaoApp = (!Util
					.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection()) ? true
							: false);

			if (Util.isNull(appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica())) {
				appSelecionada.getCronogramaRecuperacao().setLocalizacaoGeografica(new LocalizacaoGeografica());
			}
		} else {
			aceiteCondicoesRecuperacaoApp = false;
		}

		carregarDatums();
		carregarSecaoGeometrica();
	}

	public boolean getExisteTheGeomApp(App app) {
		try {
			if (!Util.isNullOuVazio(app) && app.getIdeLocalizacaoGeografica() != null) {
				return imovelRuralServiceFacade.existeTheGeom(app.getIdeLocalizacaoGeografica());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return false;
		}
		return false;
	}

	public boolean getExisteTheGeomPradApp(App app) {
		try {
			if (!Util.isNullOuVazio(app) && app.getCronogramaRecuperacao() != null
					&& app.getCronogramaRecuperacao().getLocalizacaoGeografica() != null) {
				return imovelRuralServiceFacade
						.existeTheGeom(app.getCronogramaRecuperacao().getLocalizacaoGeografica());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return false;
		}
		return false;
	}

	public void carregarTipoApp() {
		try {
			TipoApp tipoAppExcluir = null;
			List<TipoApp> listaTipoAppCompleta = (List<TipoApp>) imovelRuralServiceFacade.listarTipoApp();
			for (TipoApp tipoApp : listaTipoAppCompleta) {
				if (tipoApp.getIdeTipoApp().equals(12))
					tipoAppExcluir = tipoApp;
			}
			if (!Util.isNullOuVazio(tipoAppExcluir))
				listaTipoAppCompleta.remove(tipoAppExcluir);
			listaTipoApp = listaTipoAppCompleta;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void carregarSubTipoApp() {
		listaSubTipoApp = new ArrayList<SubTipoApp>();

		try {
			if (!Util.isNullOuVazio(appSelecionada) && !Util.isNullOuVazio(appSelecionada.getIdeTipoApp())) {
				listaSubTipoApp = (List<SubTipoApp>) imovelRuralServiceFacade
						.listarSubTipoApp(appSelecionada.getIdeTipoApp());

				if (appSelecionada.getIdeTipoApp().getIdeTipoApp() != 1) {
					appSelecionada.setIdeSubTipoApp(null);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void excluirCronogramaEtapaApp() {
		appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection().remove(cronogramaEtapaSelecionado);
		aceiteCondicoesRecuperacaoApp = (appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection()
				.isEmpty() ? false : true);
	}

	public void uploadPradApp(FileUploadEvent event) {
		try {
			Boolean primeiroArquivo = true;
			String tipoDocumento = "PRA";
			int W = (int) ((Math.random() + (imovelRural.getAppCollection().size() + 1))
					* (100 + imovelRural.getAppCollection().size()));

			String nmArquivo = this.imovelRural.toString() + "_" + W + "_" + tipoDocumento + "_APP";
			String caminhoArquivo = FileUploadUtil.EnviarShape(event,
					DiretorioArquivoEnum.IMOVELRURAL.toString() + FileUploadUtil.getFileSeparator()
							+ imovelRural.getIdeImovelRural().toString() + FileUploadUtil.getFileSeparator()
							+ "PRA_App",
					nmArquivo);

			if (!Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao())
					&& !Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getIdeDocumentoObrigatorio())) {
				List<CronogramaRecuperacao> lCronogramasCadastrados = obterCronogramaPorDocumento(
						appSelecionada.getCronogramaRecuperacao());

				if (cronogramaPodeSerExcluido(lCronogramasCadastrados)) {
					imovelRuralServiceFacade.removerDocumentoPrad(appSelecionada.getCronogramaRecuperacao());
				} else {
					tratarDocumentoPRAAssociado(lCronogramasCadastrados, appSelecionada.getCronogramaRecuperacao());
				}

				primeiroArquivo = false;
			}

			appSelecionada.getCronogramaRecuperacao()
					.setIdeDocumentoObrigatorio(new DocumentoImovelRural(null, nmArquivo, caminhoArquivo));

			File file = new File(caminhoArquivo.trim());
			appSelecionada.getCronogramaRecuperacao().getIdeDocumentoObrigatorio().setFileSize(file.length());

			if (primeiroArquivo) {
				RequestContext.getCurrentInstance().execute("dlgUploadPradApp.hide()");
			} else {
				RequestContext.getCurrentInstance().execute("dlgUploadPradApp.hide()");
			}

			carregarListaPradImportado(2);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage("O processo de Upload do arquivo não foi concluído, Por favor tente novamente.");
		}
	}

	public void importarShapeApp(FileUploadEvent event) {
		String caminhoArquivo;
		try {
			if (Util.isNullOuVazio(appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape())) {
				appSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			}

			if (appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().size() > 3) {
				JsfUtil.addWarnMessage("Não é possível enviar mais de 4 arquivos.");
			} else {
				caminhoArquivo = FileUploadUtil.EnviarShape(event,
						DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural().toString()
								+ "_" + TemaGeoseiaEnum.APP.getId() + "_"
								+ appSelecionada.getCodigoPersistirShape().toString(),
						"shapeApp");

				if (appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape()
						.contains(FileUploadUtil.getFileName(caminhoArquivo))) {
					JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
				} else {
					if (caminhoArquivo.contains(".prj")) {
						appSelecionada.getIdeLocalizacaoGeografica()
								.setIdeSistemaCoordenada(imovelRuralServiceFacade.obterSistCoordPRJ(caminhoArquivo));

						if (Util.isNull(appSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
							JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_srid_prj"));
							FileUploadUtil.removerArquivos(new File(caminhoArquivo));
							appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape()
									.remove(FileUploadUtil.getFileName(caminhoArquivo));
							return;
						}
					}

					appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape()
							.add(FileUploadUtil.getFileName(caminhoArquivo));

					if (appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().size() == 4) {
						RequestContext.getCurrentInstance().execute("dlgUploadShapeApp.hide()");
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("geral_msg_erro_envio_arquivo"));
		}
	}

	public void importarShapePradApp(FileUploadEvent event) {
		String caminhoArquivo;
		try {
			if (Util.isNullOuVazio(
					appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape())) {
				appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
						.setListArquivosShape(new ArrayList<String>());
			}

			if (appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape()
					.size() > 3) {
				JsfUtil.addWarnMessage("Não é possível enviar mais de 4 arquivos.");
			} else {
				caminhoArquivo = FileUploadUtil.EnviarShape(event,
						DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural().toString()
								+ "_" + TemaGeoseiaEnum.PRAD_APP.getId() + "_"
								+ appSelecionada.getCodigoPersistirShape().toString(),
						"shapePradApp");

				if (appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape()
						.contains(FileUploadUtil.getFileName(caminhoArquivo))) {
					JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
				} else {
					if (caminhoArquivo.contains(".prj")) {
						appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
								.setIdeSistemaCoordenada(imovelRuralServiceFacade.obterSistCoordPRJ(caminhoArquivo));

						if (Util.isNull(appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
								.getIdeSistemaCoordenada())) {
							JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_srid_prj"));
							FileUploadUtil.removerArquivos(new File(caminhoArquivo));
							appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape()
									.remove(FileUploadUtil.getFileName(caminhoArquivo));
							return;
						}
					}

					appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape()
							.add(FileUploadUtil.getFileName(caminhoArquivo));

					if (appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape()
							.size() == 4) {
						RequestContext.getCurrentInstance().execute("dlgUploadShapePradApp.hide()");
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("geral_msg_erro_envio_arquivo"));
		}
	}

	public void salvarApp() {
		try {
			if (!Util.isNull(appSelecionada) && !Util.isNull(appSelecionada.getIdeLocalizacaoGeografica())) {
				if (appSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isShapeFile()
						&& !habilitaSalvarShape(appSelecionada.getIdeLocalizacaoGeografica())
						&& !getExisteTheGeomApp(appSelecionada)) {
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_arquivos_shape_prj"));
					return;
				} else if (appSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isDesenho()
						&& !temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.APP.getId() + "_"
								+ appSelecionada.getCodigoPersistirShape())
						&& !getExisteTheGeomApp(appSelecionada)) {
					JsfUtil.addErrorMessage(
							Util.getString("msg_generica_preenchimento_obrigatorio_localizacao_geografica_poligonal"));
					return;
				}
			}

			if (!Util.isNull(appSelecionada.getCronogramaRecuperacao())
					&& !Util.isNull(appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica())) {
				if (appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao()
						.isShapeFile()
						&& !habilitaSalvarShape(appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica())
						&& !getExisteTheGeomPradApp(appSelecionada)) {
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_arquivos_shape_prj"));
					return;
				} else if (appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
						.getIdeClassificacaoSecao().isDesenho()
						&& !temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.PRAD_APP.getId()
								+ "_" + appSelecionada.getCodigoPersistirShape())
						&& !getExisteTheGeomPradApp(appSelecionada)) {
					JsfUtil.addErrorMessage(
							Util.getString("msg_generica_preenchimento_obrigatorio_localizacao_geografica_poligonal"));
					return;
				}
			}

			appSelecionada.setIdeTipoApp(retornaTipoAppSelecionado(appSelecionada.getIdeTipoApp().getIdeTipoApp()));

			if (!Util.isNullOuVazio(appSelecionada.getIdeSubTipoApp())) {
				appSelecionada.setIdeSubTipoApp(
						retornaSubTipoAppSelecionado(appSelecionada.getIdeSubTipoApp().getIdeSubTipoApp()));
			}

			if (!Util.isNull(appSelecionada.getIdeTipoEstadoConservacao())) {
				appSelecionada.setIdeTipoEstadoConservacao(retornaTipoEstadoConservacaoSelecionado(
						appSelecionada.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao()));
				validarApp();
			}

			// Se for um novo cadastro
			if (!isEdicaoApp) {
				// Salvando dados da app
				appSelecionada.setIdeImovelRural(this.imovelRural);

				// Verifica se o estado de conservação é Parcialmente Degradada ou Degradada
				if (!Util.isNull(appSelecionada.getIdeTipoEstadoConservacao())
						&& (appSelecionada.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao()
								.equals(TipoEstadoConservacaoEnum.PARCIALMENTE_DEGRADADA.getId())
								|| appSelecionada.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao()
										.equals(TipoEstadoConservacaoEnum.DEGRADADA.getId()))) {
					// Para imóveis PCT
					if (ContextoUtil.getContexto().isPCT() && !Util.isNull(appSelecionada.getIndDesejaCadPrad())) {
						appSelecionada.setDtcRespDesejaCadPrad(new Date());

						// Caso tenha respondido sim ao cadastro do PRAD
						if (appSelecionada.getIndDesejaCadPrad()) {
							// Salvando dados do cronograma de recuperação
							if (Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao())) {
								appSelecionada.getCronogramaRecuperacao().setIdeApp(appSelecionada);
								appSelecionada.getCronogramaRecuperacao().setDtcCriacao(new Date());
							}

							// Salvando documento obrigatório
							if (!Util.isNull(appSelecionada.getCronogramaRecuperacao().getIndPradImportada())
									&& appSelecionada.getCronogramaRecuperacao().getIndPradImportada()) {
								appSelecionada.getCronogramaRecuperacao().setIdeDocumentoObrigatorio(
										retornarDocumentoPradAppSelecionado(appSelecionada.getCronogramaRecuperacao()
												.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()));
							}

							// Salvando as etapas do cronograma
							if (!Util.isNullOuVazio(
									appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection())) {
								for (CronogramaEtapa cronogramaEtapa : appSelecionada.getCronogramaRecuperacao()
										.getCronogramaEtapaCollection()) {
									cronogramaEtapa
											.setIdeCronogramaRecuperacao(appSelecionada.getCronogramaRecuperacao());
								}
							}

							if (Util.isNullOuVazio(
									appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica())) {
								appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
										.setDtcCriacao(new Date());
								appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
										.setDtcExclusao(null);
								appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
										.setIndExcluido(Boolean.FALSE);
							}

							if (temShapeTemporario(
									imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.PRAD_APP.getId() + "_"
											+ appSelecionada.getCodigoPersistirShape())) {
								persistirShapesPradApp();
							}
						}
					}
					// Para os demais tipos de imóveis (não PCT)
					else if (!ContextoUtil.getContexto().isPCT()) {
						// Salvando dados do cronograma de recuperação
						if (Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao())) {
							appSelecionada.getCronogramaRecuperacao().setIdeApp(appSelecionada);
							appSelecionada.getCronogramaRecuperacao().setDtcCriacao(new Date());
						}

						// Salvando documento obrigatório
						if (!Util.isNull(appSelecionada.getCronogramaRecuperacao().getIndPradImportada())
								&& appSelecionada.getCronogramaRecuperacao().getIndPradImportada()) {
							appSelecionada.getCronogramaRecuperacao().setIdeDocumentoObrigatorio(
									retornarDocumentoPradAppSelecionado(appSelecionada.getCronogramaRecuperacao()
											.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()));
						}

						// Salvando as etapas do cronograma
						if (!Util.isNullOuVazio(
								appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection())) {
							for (CronogramaEtapa cronogramaEtapa : appSelecionada.getCronogramaRecuperacao()
									.getCronogramaEtapaCollection()) {
								cronogramaEtapa.setIdeCronogramaRecuperacao(appSelecionada.getCronogramaRecuperacao());
							}
						}

						if (Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica())) {
							appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
									.setDtcCriacao(new Date());
							appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().setDtcExclusao(null);
							appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
									.setIndExcluido(Boolean.FALSE);
						}

						if (temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.PRAD_APP.getId()
								+ "_" + appSelecionada.getCodigoPersistirShape())) {
							persistirShapesPradApp();
						}
					}
				} else {
					appSelecionada.setIndProcessoRecuperacao(null);
					appSelecionada.setCronogramaRecuperacao(null);
				}

				if (Util.isNullOuVazio(appSelecionada.getIdeLocalizacaoGeografica())) {
					appSelecionada.getIdeLocalizacaoGeografica().setDtcCriacao(new Date());
					appSelecionada.getIdeLocalizacaoGeografica().setDtcExclusao(null);
					appSelecionada.getIdeLocalizacaoGeografica().setIndExcluido(Boolean.FALSE);
				}

				if (temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.APP.getId() + "_"
						+ appSelecionada.getCodigoPersistirShape())) {
					persistirShapesApp();
				}

				if (!imovelRural.getIsFinalizado()) {
					persistirApp(appSelecionada);
				}

				imovelRural.getAppCollection().add(appSelecionada);
				RequestContext.getCurrentInstance().execute("dlgCadApp.hide()");
			} else {
				// Verifica se o estado de conservação é Parcialmente Degradada ou Degradada
				if (!Util.isNull(appSelecionada.getIdeTipoEstadoConservacao())
						&& (appSelecionada.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao()
								.equals(TipoEstadoConservacaoEnum.PARCIALMENTE_DEGRADADA.getId())
								|| appSelecionada.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao()
										.equals(TipoEstadoConservacaoEnum.DEGRADADA.getId()))) {
					// Para imóveis PCT
					if (ContextoUtil.getContexto().isPCT() && !Util.isNull(appSelecionada.getIndDesejaCadPrad())) {
						if (Util.isNull(appSelecionada.getDtcRespDesejaCadPrad())) {
							appSelecionada.setDtcRespDesejaCadPrad(new Date());
						}

						// Caso tenha respondido sim ao cadastro do PRAD
						if (appSelecionada.getIndDesejaCadPrad()) {
							// Salvando dados do cronograma de recuperação
							if (Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao())) {
								appSelecionada.getCronogramaRecuperacao().setIdeApp(appSelecionada);
								appSelecionada.getCronogramaRecuperacao().setDtcCriacao(new Date());
							}

							// Salvando documento obrigatório
							if (!Util.isNull(appSelecionada.getCronogramaRecuperacao().getIndPradImportada())
									&& appSelecionada.getCronogramaRecuperacao().getIndPradImportada()) {
								appSelecionada.getCronogramaRecuperacao().setIdeDocumentoObrigatorio(
										retornarDocumentoPradAppSelecionado(appSelecionada.getCronogramaRecuperacao()
												.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()));
							}

							// Salvando as etapas do cronograma
							if (!Util.isNullOuVazio(
									appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection())) {
								for (CronogramaEtapa cronogramaEtapa : appSelecionada.getCronogramaRecuperacao()
										.getCronogramaEtapaCollection()) {
									cronogramaEtapa.setIdeCronogramaEtapa(null);
									cronogramaEtapa
											.setIdeCronogramaRecuperacao(appSelecionada.getCronogramaRecuperacao());
								}
							}

							// Salvando localização geográfica
							if (Util.isNullOuVazio(
									appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica())) {
								appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
										.setDtcCriacao(new Date());
								appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
										.setDtcExclusao(null);
								appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
										.setIndExcluido(Boolean.FALSE);
							}

							if (temShapeTemporario(imovelRural.getIdeImovelRural() + "_"
									+ TemaGeoseiaEnum.PRAD_APP.getId() + "_" + appSelecionada.getCodigoPersistirShape())
									&& !appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
											.getNovosArquivosShapeImportados()) {
								persistirShapesPradApp();
							}

							// Se cronograma completo, exibe mensagem sugestiva para estado de conservação
							// Recuperada
							if (!naoExibeEstadoConservacaoRecuperada(appSelecionada.getCronogramaRecuperacao())) {
								JsfUtil.addWarnMessage(Util.getString("cefir_msg_A057"));
							}
						}
					}
					// Para os demais tipos de imóveis (não PCT)
					else if (!ContextoUtil.getContexto().isPCT()) {
						// Salvando dados do cronograma de recuperação
						if (Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao())) {
							appSelecionada.getCronogramaRecuperacao().setIdeApp(appSelecionada);
							appSelecionada.getCronogramaRecuperacao().setDtcCriacao(new Date());
						}

						// Salvando documento obrigatório
						if (!Util.isNull(appSelecionada.getCronogramaRecuperacao().getIndPradImportada())
								&& appSelecionada.getCronogramaRecuperacao().getIndPradImportada()) {
							appSelecionada.getCronogramaRecuperacao().setIdeDocumentoObrigatorio(
									retornarDocumentoPradAppSelecionado(appSelecionada.getCronogramaRecuperacao()
											.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()));
						}

						// Salvando as etapas do cronograma
						if (!Util.isNullOuVazio(
								appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection())) {
							for (CronogramaEtapa cronogramaEtapa : appSelecionada.getCronogramaRecuperacao()
									.getCronogramaEtapaCollection()) {
								cronogramaEtapa.setIdeCronogramaEtapa(null);
								cronogramaEtapa.setIdeCronogramaRecuperacao(appSelecionada.getCronogramaRecuperacao());
							}
						}

						// Salvando localização geográfica
						if (Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica())) {
							appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
									.setDtcCriacao(new Date());
							appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().setDtcExclusao(null);
							appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
									.setIndExcluido(Boolean.FALSE);
						}

						if (temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.PRAD_APP.getId()
								+ "_" + appSelecionada.getCodigoPersistirShape())
								&& !appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
										.getNovosArquivosShapeImportados()) {
							persistirShapesPradApp();
						}

						// Se cronograma completo, exibe mensagem sugestiva para estado de conservação
						// Recuperada
						if (!naoExibeEstadoConservacaoRecuperada(appSelecionada.getCronogramaRecuperacao())) {
							JsfUtil.addWarnMessage(Util.getString("cefir_msg_A057"));
						}
					}
				}

				if (Util.isNullOuVazio(appSelecionada.getIdeLocalizacaoGeografica())) {
					appSelecionada.getIdeLocalizacaoGeografica().setDtcCriacao(new Date());
					appSelecionada.getIdeLocalizacaoGeografica().setDtcExclusao(null);
					appSelecionada.getIdeLocalizacaoGeografica().setIndExcluido(Boolean.FALSE);
				}

				if (temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.APP.getId() + "_"
						+ appSelecionada.getCodigoPersistirShape())
						&& !appSelecionada.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					persistirShapesApp();
				}

				if (!imovelRural.getIsFinalizado()) {
					persistirApp(appSelecionada);
				}

				RequestContext.getCurrentInstance().execute("dlgCadApp.hide()");
				JsfUtil.addSuccessMessage("Alteração efetuada com sucesso.");
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public boolean temShapeTemporario(String nomePasta, Integer... qtdArquivos) {
		File diretorioShape = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + nomePasta);
		File[] arquivos = diretorioShape.listFiles();

		if (!Util.isNullOuVazio(arquivos)) {
			if (!Util.isNullOuVazio(qtdArquivos)) {
				if (arquivos.length == qtdArquivos[0]) {
					return true;
				}
			} else if (arquivos.length >= 3) {
				return true;
			}
		}

		return false;
	}

	private TipoEstadoConservacao retornaTipoEstadoConservacaoSelecionado(Integer ideTipoEstadoConservacao) {
		for (TipoEstadoConservacao tipoEstadoConservacao : listaTipoEstadoConservacao) {
			if (tipoEstadoConservacao.getIdeTipoEstadoConservacao().equals(ideTipoEstadoConservacao))
				return tipoEstadoConservacao;
		}
		return null;
	}

	private TipoApp retornaTipoAppSelecionado(Integer ideTipoApp) {
		for (TipoApp tipoApp : listaTipoApp) {
			if (tipoApp.getIdeTipoApp().equals(ideTipoApp))
				return tipoApp;
		}
		return null;
	}

	private SubTipoApp retornaSubTipoAppSelecionado(Integer ideSubTipoApp) {
		for (SubTipoApp subTipoApp : listaSubTipoApp) {
			if (subTipoApp.getIdeSubTipoApp().equals(ideSubTipoApp))
				return subTipoApp;
		}
		return null;
	}

	public void persistirApp(App pApp) throws Exception {
		if (pApp.getIdeLocalizacaoGeografica().getLocalizacaoExcluida()) {
			if (pApp.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() != null) {
				imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(
						pApp.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				pApp.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(null);
			}

			pApp.getIdeLocalizacaoGeografica().setLocalizacaoExcluida(false);
		}

		if (!Util.isNullOuVazio(pApp.getCronogramaRecuperacao())
				&& !Util.isNullOuVazio(pApp.getCronogramaRecuperacao().getLocalizacaoGeografica())
				&& pApp.getCronogramaRecuperacao().getLocalizacaoGeografica().getLocalizacaoExcluida()) {
			if (pApp.getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeLocalizacaoGeografica() != null) {
				imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(
						pApp.getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				pApp.getCronogramaRecuperacao().getLocalizacaoGeografica().setDadoGeograficoCollection(null);
			}

			pApp.getCronogramaRecuperacao().getLocalizacaoGeografica().setLocalizacaoExcluida(false);
		}

		if (Util.isNullOuVazio(pApp.getIdeApp())) {
			if (!Util.isNullOuVazio(pApp.getCronogramaRecuperacao())
					&& !Util.isNullOuVazio(pApp.getCronogramaRecuperacao().getLocalizacaoGeografica())) {
				imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
						pApp.getCronogramaRecuperacao().getLocalizacaoGeografica());
			}

			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(pApp.getIdeLocalizacaoGeografica());
			imovelRuralServiceFacade.salvarApp(pApp);
			auditoria.salvar(pApp);
		} else {
			App appAntiga = imovelRuralServiceFacade.carregarTudoApp(pApp);

			if (!Util.isNull(pApp.getIdeTipoEstadoConservacao()) && pApp.getIdeTipoEstadoConservacao()
					.getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.PRESERVADA.getId())) {
				if (!Util.isNullOuVazio(appAntiga.getCronogramaRecuperacao())
						&& !Util.isNullOuVazio(appAntiga.getCronogramaRecuperacao().getIdeCronogramaRecuperacao())) {
					excluirCronogramaApp(appAntiga);
					pApp.setIndProcessoRecuperacao(null);
					pApp.setCronogramaRecuperacao(null);
				}
			} else if (!Util.isNullOuVazio(appAntiga.getCronogramaRecuperacao())
					&& (!Util.isNull(pApp.getCronogramaRecuperacao())
							&& Util.isNullOuVazio(pApp.getCronogramaRecuperacao().getIdeCronogramaRecuperacao()))) {
				excluirCronogramaApp(appAntiga);
			}

			if (!Util.isNull(pApp.getCronogramaRecuperacao())
					&& !Util.isNullOuVazio(pApp.getCronogramaRecuperacao().getIdeCronogramaRecuperacao())) {
				imovelRuralServiceFacade.excluirAllEtapasByIdeCronogramaRecuperacao(
						pApp.getCronogramaRecuperacao().getIdeCronogramaRecuperacao());

				if (!Util.isNullOuVazio(pApp.getCronogramaRecuperacao().getCronogramaEtapaCollection())) {
					for (CronogramaEtapa etapa : pApp.getCronogramaRecuperacao().getCronogramaEtapaCollection()) {
						etapa.setIdeCronogramaEtapa(null);
					}
				}

				imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
						pApp.getCronogramaRecuperacao().getLocalizacaoGeografica());
			}

			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(pApp.getIdeLocalizacaoGeografica());
			imovelRuralServiceFacade.salvarOuAtualizarApp(pApp);
			auditoria.atualizar(appAntiga, pApp);
		}

		if (temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.APP.getId() + "_"
				+ pApp.getCodigoPersistirShape())) {
			FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(pApp.getIdeLocalizacaoGeografica(),
					TemaGeoseiaEnum.APP.getId() + "_" + pApp.getCodigoPersistirShape(),
					this.imovelRural.getIdeImovelRural().toString());
			imovelRuralServiceFacade.persistirShapes(pApp.getIdeLocalizacaoGeografica(), null);
			pApp.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
		}

		if (temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.PRAD_APP.getId() + "_"
				+ pApp.getCodigoPersistirShape())) {
			FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(
					pApp.getCronogramaRecuperacao().getLocalizacaoGeografica(),
					+TemaGeoseiaEnum.PRAD_APP.getId() + "_" + pApp.getCodigoPersistirShape(),
					this.imovelRural.getIdeImovelRural().toString());
			imovelRuralServiceFacade.persistirShapes(pApp.getCronogramaRecuperacao().getLocalizacaoGeografica(), null);
			pApp.getCronogramaRecuperacao().getLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
		}

		if (!Util.isNullOuVazio(pApp.getCronogramaRecuperacao())
				&& !Util.isNull(pApp.getCronogramaRecuperacao().getLocalizacaoGeografica())) {
			pApp.getCronogramaRecuperacao().getLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
		}

		pApp.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
	}

	private void excluirCronogramaApp(App app) throws Exception {
		if (!Util.isNullOuVazio(app.getCronogramaRecuperacao().getIdeDocumentoObrigatorio()) && !Util.isNullOuVazio(
				app.getCronogramaRecuperacao().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())) {
			List<CronogramaRecuperacao> lCronogramasCadastrados;
			lCronogramasCadastrados = obterCronogramaPorDocumento(app.getCronogramaRecuperacao());
			if (cronogramaPodeSerExcluido(lCronogramasCadastrados)) {
				imovelRuralServiceFacade.removerDocumentoPrad(app.getCronogramaRecuperacao());
			} else {
				tratarDocumentoPRAAssociado(lCronogramasCadastrados, app.getCronogramaRecuperacao());
			}
			app.getCronogramaRecuperacao().setIdeDocumentoObrigatorio(null);
		}
		imovelRuralServiceFacade.excluirAllEtapasByIdeCronogramaRecuperacao(
				app.getCronogramaRecuperacao().getIdeCronogramaRecuperacao());
		app.getCronogramaRecuperacao().setCronogramaEtapaCollection(null);
		imovelRuralServiceFacade.removerCronogramaRecuperacao(app.getCronogramaRecuperacao());
	}

	private void validaAreaDegradada() throws Exception {
		if (Util.isNullOuVazio(appSelecionada.getIndProcessoRecuperacao())) {
			throw new Exception("É necessário informar se a área está em processo de recuperação.");
		} else if (!aceiteCondicoesRecuperacaoApp) {
			throw new Exception("É necessário aceitar o termo com as condições para recuperação de áreas degradadas.");
		} else if (Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection())
				|| appSelecionada.getCronogramaRecuperacao().getCronogramaEtapaCollection().isEmpty()) {
			throw new Exception("É necessário adicionar o cronograma de recuperação.");
		} else if (Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getIndPradImportada())) {
			throw new Exception(
					"O campo O Plano de Recuperação Ambiental - PRA é o mesmo informado anteriormente? é de preenchimento obrigatório.");
		} else if ((Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getIdeDocumentoObrigatorio()))) {
			throw new Exception("É necessário importar o arquivo PRA.");
		}
	}

	private void validarApp() throws Exception {
		if (!isUsuarioSemRestricao()) {
			if (!imovelRural.isImovelINCRA() && !imovelRural.isImovelCDA() && !imovelRural.isImovelBNDES()
					&& getMostraCronograma(appSelecionada.getIdeTipoEstadoConservacao())) {
				if (ContextoUtil.getContexto().isPCT()) {
					if (Util.isNullOuVazio(appSelecionada.getIndDesejaCadPrad())) {
						throw new CampoObrigatorioException(
								"É necessário informar se deseja cadastrar o cronograma de recuperação de áreas degradadas.");
					} else {
						if (appSelecionada.getIndDesejaCadPrad()) {
							validaAreaDegradada();
						}
					}
				} else {
					validaAreaDegradada();
				}
			}
		}
	}

	public void persistirShapesApp() throws Exception {
		String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
				+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.APP.getId() + "_"
				+ appSelecionada.getCodigoPersistirShape().toString() + FileUploadUtil.getFileSeparator();
		String nome = "shapeApp";
		String nomeNovo = imovelRural.getIdeImovelRural().toString() + "_"
				+ appSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid();

		try {
			appSelecionada.getIdeLocalizacaoGeografica()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(appSelecionada
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			appSelecionada.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(
					appSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));

			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nome, nomeNovo);

			String[] retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					imovelRural.getIdeImovelRural(),
					imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.APP.getId() + "_"
							+ appSelecionada.getCodigoPersistirShape().toString(),
					appSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(),
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
							.getCoordGeobahiaMunicipio());

			if (retorno == null || retorno.length < 2) {
				throw new Exception("Erro ao persistir Localização Geográfica da APP");
			} else if (retorno[0].equalsIgnoreCase("ERRO")) {
				throw new Exception(retorno[2] + " [" + retorno[1] + "]");
			}

			String geometriaApp = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
					imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.APP.getId(),
					appSelecionada.getCodigoPersistirShape().toString());

			if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaApp)) {
				throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
			}

			appSelecionada.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
			appSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
		} catch (Exception e) {
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nomeNovo, nome);
			throw new Exception(e.getMessage());
		}
	}

	public void persistirShapesPradApp() throws Exception {
		String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
				+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.PRAD_APP.getId() + "_"
				+ appSelecionada.getCodigoPersistirShape().toString() + FileUploadUtil.getFileSeparator();
		String nome = "shapePradApp";
		String nomeNovo = imovelRural.getIdeImovelRural().toString() + "_" + appSelecionada.getCronogramaRecuperacao()
				.getLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid();

		try {
			appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeClassificacaoSecao(
					retornaClassificacaoSecaoGeometricaSelecionado(appSelecionada.getCronogramaRecuperacao()
							.getLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().setIdeSistemaCoordenada(
					retornaSistemaCordenadaSelecionado(appSelecionada.getCronogramaRecuperacao()
							.getLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));

			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nome, nomeNovo);

			String[] retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					imovelRural.getIdeImovelRural(),
					imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.PRAD_APP.getId() + "_"
							+ appSelecionada.getCodigoPersistirShape().toString(),
					appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeSistemaCoordenada()
							.getSrid(),
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
							.getCoordGeobahiaMunicipio());

			if (retorno == null || retorno.length < 2) {
				throw new Exception("Erro ao persistir Localização Geográfica da Área Degradada da APP");
			} else if (retorno[0].equalsIgnoreCase("ERRO")) {
				throw new Exception(retorno[2] + " [" + retorno[1] + "]");
			}

			String geometriaPradApp = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
					imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.PRAD_APP.getId(),
					appSelecionada.getCodigoPersistirShape().toString());

			if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaPradApp)) {
				throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
			}

			appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
			appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
					.setListArquivosShape(new ArrayList<String>());
		} catch (Exception e) {
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nomeNovo, nome);
			throw new Exception(e.getMessage());
		}
	}
	
	
	public void btnNaoapp() {
		if (!Util.isNullOuVazio(imovelRural.getAppCollection())) {
			imovelRural.getAppCollection().clear();
		}
	}

	public void excluirApp() {
		if (imovelRural.getIsFinalizado()){
			try {
				if (!Util.isNullOuVazio(appSelecionada.getIdeTipoEstadoConservacao()) && 
						!appSelecionada.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.PRESERVADA.getId())
						&& !Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao())
						&& !Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getIdeDocumentoObrigatorio()
								.getIdeDocumentoObrigatorio())) {
					List<CronogramaRecuperacao> lCronogramasCadastrados = obterCronogramaPorDocumento(
							appSelecionada.getCronogramaRecuperacao());

					if (!cronogramaPodeSerExcluido(lCronogramasCadastrados)) {
						tratarDocumentoPRAAssociado(lCronogramasCadastrados, appSelecionada.getCronogramaRecuperacao());
					}
					lCronogramasCadastrados = null;
				}
				if (Util.isNullOuVazio(appSelecionada.getIdeTipoEstadoConservacao())
						&& !Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao())
						&& !Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getIdeDocumentoObrigatorio())
						&& !Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getIdeDocumentoObrigatorio()
								.getIdeDocumentoObrigatorio())) {
					List<CronogramaRecuperacao> lCronogramasCadastrados = obterCronogramaPorDocumento(
							appSelecionada.getCronogramaRecuperacao());

					if (!cronogramaPodeSerExcluido(lCronogramasCadastrados)) {
						tratarDocumentoPRAAssociado(lCronogramasCadastrados, appSelecionada.getCronogramaRecuperacao());
					}
					lCronogramasCadastrados = null;
				}

				appSelecionada.setIndExcluido(true);
				JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			}
		} else {
			try {
				persistirExclusaoApp(appSelecionada);
				imovelRural.getAppCollection().remove(appSelecionada);
				JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
			} catch (EJBTransactionRolledbackException exception) {
				JsfUtil.addErrorMessage("Não é possível excluir APP favor retirar associação de PRA.");
			} catch (Exception exception) {
				JsfUtil.addErrorMessage("Erro ao Excluir APP");
				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}
	}

	private void persistirExclusaoApp(App pApp) throws Exception, EJBTransactionRolledbackException {
		if (!Util.isNull(pApp.getIdeTipoEstadoConservacao()) && !pApp.getIdeTipoEstadoConservacao()
				.getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.PRESERVADA.getId())) {
			if (!Util.isNullOuVazio(pApp.getCronogramaRecuperacao())) {
				if (!Util.isNullOuVazio(pApp.getCronogramaRecuperacao().getIdeDocumentoObrigatorio())
						&& !Util.isNullOuVazio(pApp.getCronogramaRecuperacao().getIdeDocumentoObrigatorio()
								.getIdeDocumentoObrigatorio())) {
					List<CronogramaRecuperacao> lCronogramasCadastrados;
					lCronogramasCadastrados = obterCronogramaPorDocumento(pApp.getCronogramaRecuperacao());
					if (cronogramaPodeSerExcluido(lCronogramasCadastrados)) {
						imovelRuralServiceFacade.removerDocumentoPrad(pApp.getCronogramaRecuperacao());
					} else {
						tratarDocumentoPRAAssociado(lCronogramasCadastrados, pApp.getCronogramaRecuperacao());
					}
					pApp.getCronogramaRecuperacao().setIdeDocumentoObrigatorio(null);
					imovelRuralServiceFacade.salvarOuAtualizarApp(pApp);
				}
				imovelRuralServiceFacade.excluirAllEtapasByIdeCronogramaRecuperacao(
						pApp.getCronogramaRecuperacao().getIdeCronogramaRecuperacao());
				pApp.getCronogramaRecuperacao().setCronogramaEtapaCollection(null);
				imovelRuralServiceFacade.removerCronogramaRecuperacao(pApp.getCronogramaRecuperacao());
				pApp.setCronogramaRecuperacao(null);
			}
		}
		LocalizacaoGeografica locApp = new LocalizacaoGeografica();
		if (!Util.isNullOuVazio(pApp.getIdeLocalizacaoGeografica())) {
			locApp = new LocalizacaoGeografica(pApp.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		}
		pApp.setIdeLocalizacaoGeografica(null);
		if (!Util.isNullOuVazio(pApp.getCronogramaRecuperacao())) {
			cronogramaRecuperacaoService.removerCronogramaRecuperacao(pApp.getCronogramaRecuperacao());
		}
		imovelRuralServiceFacade.excluirApp(pApp);
		if (!Util.isNullOuVazio(pApp.getIdeLocalizacaoGeografica())) {
			imovelRuralServiceFacade.excluirLocalizacaoGeografica(locApp);
		}
		// auditoria
		auditoria.excluir(pApp);
	}

	// Fim Cadastro APP

	// Início Cadastro Outros Passívos

	public void uploadPradOP(FileUploadEvent event) {
		try {
			String tipoDocumento = "PRAD";
			String nmArquivo = imovelRural.toString() + "_" + tipoDocumento + "_OP";
			String caminhoArquivo = FileUploadUtil.EnviarShape(event,
					DiretorioArquivoEnum.IMOVELRURAL.toString() + FileUploadUtil.getFileSeparator()
							+ imovelRural.getIdeImovelRural().toString() + FileUploadUtil.getFileSeparator()
							+ "PRAD_OP",
					nmArquivo);

			if (Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad())) {
				imovelRural.getOutrosPassivosAmbientais().setIdeDocumentoPrad(new DocumentoImovelRural());
			}

			imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad().setNomDocumentoObrigatorio(nmArquivo);
			imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad().setDscCaminhoArquivo(caminhoArquivo);

			File file = new File(caminhoArquivo.trim());
			imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad().setFileSize(file.length());

			if (Util.isNullOuVazio(
					imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad().getIdeDocumentoObrigatorio())) {
				JsfUtil.addSuccessMessage("Arquivo enviado com Sucesso.");
			} else {
				JsfUtil.addSuccessMessage("Arquivo sobrescrito e atualizado com sucesso!");
			}

			RequestContext.getCurrentInstance().execute("dlgUploadPradOp.hide()");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage("O processo de Upload do arquivo não foi concluído, Por favor tente novamente.");
		}
	}

	public void salvarOutrosPassivos() {
		try {
			String mensagem = "";
			imovelRuralServiceFacade.validaOutrosPassivos(this.imovelRural, aceiteCondicoesRecuperacaoOp);
			if (Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getIdeOutrosPassivosAmbientais()))
				mensagem = Util.getString("cefir_msg_S001");
			else
				mensagem = Util.getString("cefir_msg_S002");

			if (!imovelRural.getIsFinalizado()) {
				persistirOutrosPassivos();
			}

			JsfUtil.addSuccessMessage(mensagem);
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void persistirOutrosPassivos() throws Exception {
		if (!Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getIdeOutrosPassivosAmbientais())) {
			imovelRuralServiceFacade.atualizarOutrosPassivosAmbientais(imovelRural.getOutrosPassivosAmbientais());
		} else {
			imovelRural.getOutrosPassivosAmbientais().setIdeImovelRural(imovelRural);
			imovelRuralServiceFacade.salvarOutrosPassivosAmbientais(imovelRural.getOutrosPassivosAmbientais());
		}
	}

	public void excluirCronogramaEtapaOp() {
		imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao().getCronogramaEtapaCollection()
				.remove(cronogramaEtapaSelecionado);
		aceiteCondicoesRecuperacaoOp = (imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao()
				.getCronogramaEtapaCollection().isEmpty() ? false : true);
	}

	// Fim Cadastro Outros Passívos

	// Início Download

	public StreamedContent getFileDownload(String caminhoArquivo) {
		if (!Util.isNullOuVazio(caminhoArquivo)) {
			try {
				return imovelRuralServiceFacade.getContentFile(caminhoArquivo);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
				JsfUtil.addErrorMessage("Erro ao tentar fazer o download do arquivo");
			}
		}
		return null;
	}

	// Fim Download

	// Início Gets e Sets

	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}

	public TipoVinculoImovel getTipoVinculoImovel() {
		return tipoVinculoImovel;
	}

	public void setTipoVinculoImovel(TipoVinculoImovel tipoVinculoImovel) {
		this.tipoVinculoImovel = tipoVinculoImovel;
	}

	public String getNumCep() {
		return numCep;
	}

	public void setNumCep(String numCep) {
		this.numCep = numCep;
	}

	private Bairro getBairro() {
		return getEndereco().getIdeLogradouro().getIdeBairro();
	}

	public boolean isEdicao() {
		return !Util.isNullOuVazio(this.imovelRural.getIdeImovelRural());
	}

	public Bairro getBairroSelecionado() {
		return bairroSelecionado;
	}

	public void setBairroSelecionado(Bairro bairroSelecionado) {
		this.bairroSelecionado = bairroSelecionado;
	}

	public Logradouro getLogradouroSelecionado() {
		return logradouroSelecionado;
	}

	public void setLogradouroSelecionado(Logradouro logradouroSelecionado) {
		this.logradouroSelecionado = logradouroSelecionado;
	}

	public Estado getEstadoSelecionado() {
		return estadoSelecionado;
	}

	public void setEstadoSelecionado(Estado estadoSelecionado) {
		this.estadoSelecionado = estadoSelecionado;
	}

	public Municipio getMunicipioSelecionado() {
		return municipioSelecionado;
	}

	public void setMunicipioSelecionado(Municipio municipioSelecionado) {
		this.municipioSelecionado = municipioSelecionado;
	}

	public boolean isHabilitaComboMunicipio() {
		return habilitaComboMunicipio;
	}

	public void setHabilitaComboMunicipio(boolean habilitaComboMunicipio) {
		this.habilitaComboMunicipio = habilitaComboMunicipio;
	}

	public TipoLogradouro getTipoLogradouroSelecionado() {
		return tipoLogradouroSelecionado;
	}

	public void setTipoLogradouroSelecionado(TipoLogradouro tipoLogradouroSelecionado) {
		this.tipoLogradouroSelecionado = tipoLogradouroSelecionado;
	}

	public Wizard getWizard() {
		return wizard;
	}

	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
	}

	public ResponsavelImovelRural getResponsavelTecnicoSelecionado() {
		return responsavelTecnicoSelecionado;
	}

	public void setResponsavelTecnicoSelecionado(ResponsavelImovelRural responsavelTecnicoSelecionado) {
		this.responsavelTecnicoSelecionado = responsavelTecnicoSelecionado;
	}

	public Boolean getIsEdicaoResponsavelTecnico() {
		return isEdicaoResponsavelTecnico;
	}

	public void setIsEdicaoResponsavelTecnico(Boolean isEdicaoResponsavelTecnico) {
		this.isEdicaoResponsavelTecnico = isEdicaoResponsavelTecnico;
	}

	public Boolean getIsPessoaFisicaJaCadastrada() {
		return isPessoaFisicaJaCadastrada;
	}

	public void setIsPessoaFisicaJaCadastrada(Boolean isPessoaFisicaJaCadastrada) {
		this.isPessoaFisicaJaCadastrada = isPessoaFisicaJaCadastrada;
	}

	public void setEscolaridade(ResponsavelImovelRural responsavel) {
		for (Escolaridade escolaridade : listaEscolaridade)
			if (responsavel.getIdePessoaFisica().getIdeEscolaridade().getIdeEscolaridade()
					.equals(escolaridade.getIdeEscolaridade()))
				responsavel.getIdePessoaFisica().setIdeEscolaridade(escolaridade);
	}

	public ArrayList<DocumentoImovelRural> getListArt() {
		ArrayList<DocumentoImovelRural> listArt = new ArrayList<DocumentoImovelRural>();
		if (!Util.isNullOuVazio(responsavelTecnicoSelecionado.getIdeDocumentoResponsavel()) && !Util
				.isNullOuVazio(responsavelTecnicoSelecionado.getIdeDocumentoResponsavel().getDscCaminhoArquivo()))
			listArt.add(responsavelTecnicoSelecionado.getIdeDocumentoResponsavel());
		return listArt;
	}

	public Collection<Pais> getListaPais() {
		return listaPais;
	}

	public List<Escolaridade> getListaEscolaridade() {
		return listaEscolaridade;
	}

	public List<TipoEstadoConservacao> getListaTipoEstadoConservacaoApp() {
		if (!Util.isNull(getListaTipoEstadoConservacao())) {
			if (!Util.isNull(appSelecionada)) {
				TipoEstadoConservacao recuperada = obterEstadoConservacaoRecuperada();
				if (naoExibeEstadoConservacaoRecuperada(appSelecionada.getCronogramaRecuperacao())) {
					listaTipoEstadoConservacao.remove(recuperada);
				} else {
					if (!listaTipoEstadoConservacao.contains(recuperada)) {
						listaTipoEstadoConservacao.add(recuperada);
					}
				}
			}

		}
		return getListaTipoEstadoConservacao();
	}

	protected TipoEstadoConservacao obterEstadoConservacaoRecuperada() {
		return new TipoEstadoConservacao(TipoEstadoConservacaoEnum.RECUPERADA);
	}

	public List<TipoEstadoConservacao> getListaTipoEstadoConservacao() {
		return listaTipoEstadoConservacao;
	}

	/**
	 * @return the cronogramaEtapaSelecionado
	 */
	public CronogramaEtapa getCronogramaEtapaSelecionado() {
		return cronogramaEtapaSelecionado;
	}

	/**
	 * @param cronogramaEtapaSelecionado the cronogramaEtapaSelecionado to set
	 */
	public void setCronogramaEtapaSelecionado(CronogramaEtapa cronogramaEtapaSelecionado) {
		this.cronogramaEtapaSelecionado = cronogramaEtapaSelecionado;
	}

	public List<DocumentoImovelRural> getListaPradImportadosRl() {
		return listaPradImportadosRl;
	}

	public void setListaPradImportadosRl(List<DocumentoImovelRural> listaPradImportados) {
		this.listaPradImportadosRl = listaPradImportados;
	}

	public String getArquivoShapeExcluir() {
		return arquivoShapeExcluir;
	}

	public void setArquivoShapeExcluir(String arquivoShapeExcluir) {
		this.arquivoShapeExcluir = arquivoShapeExcluir;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public String getCaminhoGeoBahia() {
		return caminhoGeoBahia;
	}

	public void setCaminhoGeoBahia(String caminhoGeoBahia) {
		this.caminhoGeoBahia = caminhoGeoBahia;
	}

	public String getCaminhoNovoGeoBahia() {
		return caminhoNovoGeoBahia;
	}

	public void setCaminhoNovoGeoBahia(String caminhoNovoGeoBahia) {
		this.caminhoNovoGeoBahia = caminhoNovoGeoBahia;
	}

	public List<ClassificacaoSecaoGeometrica> getListaSecaoGeometrica() {
		return listaSecaoGeometrica;
	}

	public void setListaSecaoGeometrica(List<ClassificacaoSecaoGeometrica> listaSecaoGeomerica) {
		this.listaSecaoGeometrica = listaSecaoGeomerica;
	}

	public Collection<SistemaCoordenada> getListaDatums() {
		return listaDatums;
	}

	public void setListaDatums(Collection<SistemaCoordenada> listaDatums) {
		this.listaDatums = listaDatums;
	}

	public Collection<TipoRecuperacao> getListaTipoRecuperacao() {
		return listaTipoRecuperacao;
	}

	public void setListaTipoRecuperacao(Collection<TipoRecuperacao> listaTipoRecuperacao) {
		this.listaTipoRecuperacao = listaTipoRecuperacao;
	}

	public void setTipoRecuperacao(CronogramaEtapa cronogramaEtapa) {
		for (TipoRecuperacao tipoRecuperacao : listaTipoRecuperacao)
			if (cronogramaEtapa.getIdeTipoRecuperacao().getIdeTipoRecuperacao()
					.equals(tipoRecuperacao.getIdeTipoRecuperacao())) {
				cronogramaEtapa.setIdeTipoRecuperacao(tipoRecuperacao);
			}
	}

	public ClassificacaoSecaoGeometrica selecionarSecaoGeometrica(
			ClassificacaoSecaoGeometrica classificacaoSecaoGeometrica) {
		for (ClassificacaoSecaoGeometrica classificacao : listaSecaoGeometrica) {
			if (classificacaoSecaoGeometrica.getIdeClassificacaoSecao()
					.equals(classificacao.getIdeClassificacaoSecao()))
				return classificacao;
		}
		return new ClassificacaoSecaoGeometrica();
	}

	public SistemaCoordenada selecionarSistemaCoordenada(SistemaCoordenada sistemaCoordenada) {
		for (SistemaCoordenada sistema : listaDatums) {
			if (sistemaCoordenada.getIdeSistemaCoordenada().equals(sistema.getIdeSistemaCoordenada()))
				return sistema;
		}
		return new SistemaCoordenada();
	}

	public ArrayList<DocumentoImovelRural> getListPradOp() {
		ArrayList<DocumentoImovelRural> listPrad = new ArrayList<DocumentoImovelRural>();
		if (!Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad()) && !Util
				.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad().getDscCaminhoArquivo()))
			listPrad.add(imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad());
		return listPrad;
	}

	public App getAppSelecionada() {
		return appSelecionada;
	}

	public void setAppSelecionada(App appSelecionada) {
		this.appSelecionada = appSelecionada;
	}

	public Integer getTipoCronogramaEtapa() {
		return tipoCronogramaEtapa;
	}

	public void setTipoCronogramaEtapa(Integer tipoCronogramaEtapa) {
		this.tipoCronogramaEtapa = tipoCronogramaEtapa;
	}

	public Boolean getVisualizacaoApp() {
		return visualizacaoApp;
	}

	public void setVisualizacaoApp(Boolean visualizacao) {
		this.visualizacaoApp = visualizacao;
	}

	public List<TipoApp> getListaTipoApp() {
		return listaTipoApp;
	}

	public void setListaTipoApp(List<TipoApp> listaTipoApp) {
		this.listaTipoApp = listaTipoApp;
	}

	public List<SubTipoApp> getListaSubTipoApp() {
		return listaSubTipoApp;
	}

	public void setListaSubTipoApp(List<SubTipoApp> listaSubTipoApp) {
		this.listaSubTipoApp = listaSubTipoApp;
	}

	public boolean getMostraCronograma(TipoEstadoConservacao estadoConservacao) {
		return !Util.isNull(estadoConservacao) && !Util.isNull(estadoConservacao.getIdeTipoEstadoConservacao())
				&& (estadoConservacao.getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.DEGRADADA.getId())
						|| estadoConservacao.getIdeTipoEstadoConservacao()
								.equals(TipoEstadoConservacaoEnum.PARCIALMENTE_DEGRADADA.getId()));
	}

	public boolean getMostraDesejaCadastrarCronograma(TipoEstadoConservacao estadoConservacao) {
		if (ContextoUtil.getContexto().isPCT()) {
			return getMostraCronograma(estadoConservacao);
		} else {
			return false;
		}
	}

	public boolean getMostraDesejaCadastrarCronogramaApp() {
		if (ContextoUtil.getContexto().isPCT() && !Util.isNull(appSelecionada)) {
			return getMostraCronograma(appSelecionada.getIdeTipoEstadoConservacao());
		} else {
			return false;
		}
	}

	private boolean isEstadoConversacaoDegradado(TipoEstadoConservacao tec) {
		if (!Util.isNullOuVazio(tec)) {
			return (TipoEstadoConservacaoEnum.DEGRADADA.getId().equals(tec.getId())
					|| TipoEstadoConservacaoEnum.PARCIALMENTE_DEGRADADA.getId().equals(tec.getId()));
		} else {
			return false;
		}
	}
	
	private boolean isEstadoConservacaoPreservada(TipoEstadoConservacao tec) {
		if(!Util.isNullOuVazio(tec)) {
			return (TipoEstadoConservacaoEnum.PRESERVADA.getId().equals(tec.getId()));
		}else {
			return false;
		}		
	}
	
	public void changeTipoEstadoConservacao(ValueChangeEvent event) {
		if (event != null && event.getNewValue() != null && event.getNewValue() instanceof TipoEstadoConservacao) {
			TipoEstadoConservacao tec = (TipoEstadoConservacao) event.getNewValue();

			if (ContextoUtil.getContexto().isPCT()) {
				if (!isEstadoConversacaoDegradado(tec) || !isEstadoConversacaoDegradado(tipoEstadoConservacaoInicial)) {
					appSelecionada.setIndDesejaCadPrad(null);
					appSelecionada.setDtcRespDesejaCadPrad(null);
					appSelecionada.setCronogramaRecuperacao(null);
					this.setAceiteCondicoesRecuperacaoApp(false);
				}
			} else {
				if (isEstadoConversacaoDegradado(tec)
						&& Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao())) {
					appSelecionada.setCronogramaRecuperacao(new CronogramaRecuperacao());
					appSelecionada.getCronogramaRecuperacao().setLocalizacaoGeografica(new LocalizacaoGeografica());
					appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
							.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
					appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
							.setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
					appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
							.setIdeSistemaCoordenada(new SistemaCoordenada());
				}else if((!Util.isNull(tec) && isEstadoConservacaoPreservada(tec)) ||
							Util.isNull(tec)) {
					appSelecionada.setIndDesejaCadPrad(null);
					appSelecionada.setDtcRespDesejaCadPrad(null);
					appSelecionada.setCronogramaRecuperacao(null);
					this.setAceiteCondicoesRecuperacaoApp(false);
					}	
			}
		} else {
			appSelecionada.setIndDesejaCadPrad(null);
			appSelecionada.setDtcRespDesejaCadPrad(null);
			appSelecionada.setCronogramaRecuperacao(null);
			this.setAceiteCondicoesRecuperacaoApp(false);
		
		}
	}

	public void changeDesejaCadastrarCronograma() {
		if (appSelecionada.getIndDesejaCadPrad()) {
			appSelecionada.setCronogramaRecuperacao(new CronogramaRecuperacao());
			appSelecionada.getCronogramaRecuperacao().setLocalizacaoGeografica(new LocalizacaoGeografica());
			appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
					.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
			appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
					.setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
			appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
					.setIdeSistemaCoordenada(new SistemaCoordenada());
		} else {
			appSelecionada.setCronogramaRecuperacao(null);
		}
	}

	public boolean getCadastrarCronogramaApp() {
		if (ContextoUtil.getContexto().isPCT()) {
			Boolean desejaCadPrad = null;

			if (!Util.isNullOuVazio(appSelecionada)) {
				desejaCadPrad = appSelecionada.getIndDesejaCadPrad();
			}

			if (Util.isNullOuVazio(desejaCadPrad)) {
				if ((isEdicaoApp || visualizacaoApp) && isEstadoConversacaoDegradado(tipoEstadoConservacaoInicial)
						&& !Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao())) {
					return getMostraCronograma(appSelecionada.getIdeTipoEstadoConservacao());
				} else {
					return false;
				}
			} else {
				return desejaCadPrad;
			}
		} else {
			return getMostraCronograma(appSelecionada.getIdeTipoEstadoConservacao());
		}
	}

	public Boolean getAceiteCondicoesRecuperacaoApp() {
		return this.aceiteCondicoesRecuperacaoApp;
	}

	public void setAceiteCondicoesRecuperacaoApp(Boolean aceiteCondicoesRecuperacaoApp) {
		this.aceiteCondicoesRecuperacaoApp = aceiteCondicoesRecuperacaoApp;
	}

	public ArrayList<DocumentoImovelRural> getListaPradApp() {
		ArrayList<DocumentoImovelRural> listPrad = new ArrayList<DocumentoImovelRural>();
		if (!Util.isNullOuVazio(appSelecionada)
				&& !Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getIdeDocumentoObrigatorio())
				&& !Util.isNullOuVazio(
						appSelecionada.getCronogramaRecuperacao().getIdeDocumentoObrigatorio().getDscCaminhoArquivo()))
			listPrad.add(appSelecionada.getCronogramaRecuperacao().getIdeDocumentoObrigatorio());
		return listPrad;
	}

	public List<DocumentoImovelRural> getListaPradImportadosApp() {
		return listaPradImportadosApp;
	}

	public void setListaPradImportadosApp(List<DocumentoImovelRural> listaPradImportadosApp) {
		this.listaPradImportadosApp = listaPradImportadosApp;
	}

	public boolean getIsEdicaoApp() {
		return isEdicaoApp;
	}

	public void setIsEdicaoApp(boolean isEdicaoApp) {
		this.isEdicaoApp = isEdicaoApp;
	}

	public Boolean getMostrarBotaoUploadShapeApp() {
		return !Util.isNullOuVazio(appSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao())
				&& !Util.isNullOuVazio(appSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
						.getIdeClassificacaoSecao())
				&& appSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()
						.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())
				&& (Util.isNullOuVazio(appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape())
						|| appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().size() < 4);
	}

	public Boolean getMostrarBotaoUploadShapePradApp() {
		return !Util.isNullOuVazio(
				appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao())
				&& !Util.isNullOuVazio(appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica()
						.getIdeClassificacaoSecao().getIdeClassificacaoSecao())
				&& appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeClassificacaoSecao()
						.getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())
				&& (Util.isNullOuVazio(
						appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape())
						|| appSelecionada.getCronogramaRecuperacao().getLocalizacaoGeografica().getListArquivosShape()
								.size() < 4);
	}

	public DocumentoImovelRural retornarDocumentoPradRlSelecionado(Integer ideDocumentoImovelRural) {
		for (DocumentoImovelRural documento : listaPradImportadosRl) {
			if (documento.getIdeDocumentoObrigatorio().equals(ideDocumentoImovelRural))
				return documento;
		}
		return null;
	}

	public DocumentoImovelRural retornarDocumentoPradAppSelecionado(Integer ideDocumentoImovelRural) {
		for (DocumentoImovelRural documento : listaPradImportadosApp) {
			if (documento.getIdeDocumentoObrigatorio().equals(ideDocumentoImovelRural))
				return documento;
		}
		return null;
	}

	public Collection<App> getListaApp() {
		listaApp = new ArrayList<App>();
		if (Util.isLazyInitExcepOuNull(imovelRural.getAppCollection())) {
			imovelRural.setAppCollection(new ArrayList<App>());
		}
		if (!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(imovelRural.getAppCollection())) {
			for (App app : imovelRural.getAppCollection()) {
				if (!app.getIndExcluido())
					listaApp.add(app);
			}
		}
		return listaApp;
	}

	public void setListaApp(Collection<App> listaApp) {
		this.listaApp = listaApp;
	}

	// Fim Gets e Sets

	// Proprietário imóvel
	public boolean isTelaCpf() {
		return telaCpf;
	}

	public void setTelaCpf(boolean telaCpf) {
		this.telaCpf = telaCpf;
	}

	public boolean isTelaCnpj() {
		return telaCnpj;
	}

	public void setTelaCnpj(boolean telaCnpj) {
		this.telaCnpj = telaCnpj;
	}

	public void alterarLayoutTelaProprietario(ValueChangeEvent event) {
		Boolean value = (Boolean) event.getNewValue();
		if (value) {
			this.telaCnpj = false;
			this.telaCpf = true;
		} else {
			this.telaCpf = false;
			this.telaCnpj = true;
		}
	}

	public void pesquisarPessoa() {
		if (telaCpf) {
			pesquisarPessoaFisica();
			if (Util.isNull(pessoaFisicaPersist.getIdePessoaFisica())) {
				setHabilitaCadastroProprietarioImovel(true);
			}
		} else {
			pesquisarPessoaJuridica();
			if (Util.isNull(pessoaJuridicaPersist.getIdePessoaJuridica())) {
				setHabilitaCadastroProprietarioImovel(true);
			}
		}

	}

	public void pesquisarPessoaFisica() {
		try {
			String cpf = pessoaFisicaPersist.getNumCpf();
			pessoaFisicaPersist = imovelRuralServiceFacade.filtrarPessoaFisicaByCpf(pessoaFisicaPersist);
			if (Util.isNullOuVazio(pessoaFisicaPersist)) {
				pessoaFisicaPersist = new PessoaFisica();
				pessoaFisicaPersist.setNumCpf(cpf);
				pessoaPersist = new Pessoa();
			} else {
				pessoaPersist = pessoaFisicaPersist.getPessoa();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void pesquisarPessoaJuridica() {
		try {
			String cnpj = pessoaJuridicaPersist.getNumCnpj();
			pessoaJuridicaPersist = imovelRuralServiceFacade.filtrarPessoaJuridicaByCnpj(pessoaJuridicaPersist);
			if (Util.isNullOuVazio(pessoaJuridicaPersist)) {
				pessoaJuridicaPersist = new PessoaJuridica();
				pessoaJuridicaPersist.setNumCnpj(cnpj);
				pessoaPersist = new Pessoa();
			} else {
				pessoaPersist = pessoaJuridicaPersist.getPessoa();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void limparObjetosProprietarioImovel() {
		if (!this.telaCnpj) {
			this.telaCpf = Boolean.TRUE;
		} else {
			this.telaCpf = Boolean.FALSE;
		}
		pessoaPersist = new Pessoa();
		pessoaFisicaPersist = new PessoaFisica();
		pessoaJuridicaPersist = new PessoaJuridica();
		setHabilitaCadastroProprietarioImovel(false);
	}

	public void preparaParaEdicaoProprietario() {
		setHabilitaCadastroProprietarioImovel(false);
		if (!Util.isNull(pessoaPersist.getPessoaFisica())) {
			pessoaFisicaPersist = pessoaPersist.getPessoaFisica();
			this.telaCpf = Boolean.TRUE;
			this.telaCnpj = Boolean.FALSE;
		} else {
			pessoaJuridicaPersist = pessoaPersist.getPessoaJuridica();
			this.telaCnpj = Boolean.TRUE;
			this.telaCpf = Boolean.FALSE;
		}
	}

	public void excluirPessoaImovel() {
		try {
			if (imovelRuralServiceFacade.validaExclusaoProprietario(this.proprietarioExclusao, imovelRural)) {
				if (!Util.isNullOuVazio(this.listProprietariosImovel)) {
					if (tipoVinculoImovel.getIdeTipoVinculoImovel() == TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR) {
						this.proprietarioExclusao.setIndExcluido(true);
						if (!imovelRural.getIsFinalizado()) {
							imovelRuralServiceFacade.removerPessoaImovel(this.proprietarioExclusao);
							getListProprietariosImovel().remove(this.proprietarioExclusao);
						}
						JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
						return;
					} else {
						if (this.listProprietariosImovel.size() > 1) {
							this.proprietarioExclusao.setIndExcluido(true);
							if (!imovelRural.getIsFinalizado()) {
								imovelRuralServiceFacade.removerPessoaImovel(this.proprietarioExclusao);
								getListProprietariosImovel().remove(this.proprietarioExclusao);
							}
							JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
							return;
						}
					}
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public String salvarProprietario() {
		if (editandoProprietarioCda) {
			try {
				if (this.telaCpf) {
					imovelRuralServiceFacade.salvarOuAtualizarPessoaFisica(pessoaFisicaPersist);
				} else {
					imovelRuralServiceFacade.salvarOuAtualizarPessoaJuridica(pessoaJuridicaPersist);
				}
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro ao alterar proprietário.");
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			}
			JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S002"));
			RequestContext.getCurrentInstance().execute("dialogCompAcionaria.hide()");
			editandoProprietarioCda = false;
			return null;
		} else if (!Util.isNullOuVazio(pessoaPersist) && !Util.isNullOuVazio(pessoaPersist.getIdePessoa())
				&& !editandoProprietario) {
			if (proprietarioJaFoiCadastrado()) {
				JsfUtil.addErrorMessage(Util.getString("cefir_msg_proprietario_existente"));
				RequestContext.getCurrentInstance().execute("dialogCompAcionaria.hide()");
				return null;
			}
			if (!imovelRural.getIsFinalizado()) {
				this.listProprietariosImovel.clear();
				getListProprietariosImovel();
			}
			salvarPessoaImovel();
			JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S001"));
			RequestContext.getCurrentInstance().execute("dialogCompAcionaria.hide()");
			return null;
		} else {
			if (telaCpf) {
				salvarPessoaFisica();
			} else {
				salvarPessoaJuridica();
			}

			if (!editandoProprietario) {
				salvarPessoaImovel();
				getListProprietariosImovel();
				RequestContext.getCurrentInstance().execute("dialogCompAcionaria.hide()");
				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S001"));
				return null;
			}
		}
		limparObjetosProprietarioImovel();
		this.listProprietariosImovel.clear();
		getListProprietariosImovel();
		if (editandoProprietario) {
			JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S002"));
			RequestContext.getCurrentInstance().execute("dialogCompAcionaria.hide()");
			return null;
		}
		JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S001"));
		RequestContext.getCurrentInstance().execute("dialogCompAcionaria.hide()");
		return null;
	}

	private void salvarPessoaImovel() {
		PessoaImovel pessoaIm = new PessoaImovel();
		pessoaIm.setIdeImovel(this.imovelRural.getImovel());
		pessoaIm.setIdePessoa(pessoaPersist);
		pessoaIm.setIdeTipoVinculoImovel(tipoVinculoImovel);
		pessoaIm.setDtcCriacao(new Date());
		try {

			this.listProprietariosImovel.add(pessoaIm);
			if (!imovelRural.getIsFinalizado()) {
				imovelRuralServiceFacade.salvarImovelPessoa(pessoaIm);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void salvarPessoaFisica() {
		try {
			if(Util.isNullOuVazio(pessoaPersist.getIdePessoa())) {
				pessoaPersist.setDtcCriacao(new Date());				
			}
			pessoaPersist.setIndExcluido(Boolean.FALSE);
			pessoaFisicaPersist.setPessoa(pessoaPersist);
			pessoaPersist.setPessoaFisica(pessoaFisicaPersist);
			if (!imovelRural.getIsFinalizado()) {
				persistirPessoaFisicaOrJuridica();
			}
			// salvarParticipacaoAcionaria(TipoAcionistaEnum.PESSOA_FISICA);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void persistirPessoaFisicaOrJuridica() throws Exception {
		if (this.telaCpf) {
			imovelRuralServiceFacade.salvarOuAtualizarPessoaFisica(pessoaFisicaPersist);
		} else {
			imovelRuralServiceFacade.salvarOuAtualizarPessoaJuridica(pessoaJuridicaPersist);
		}
	}

	private void salvarPessoaJuridica() {
		try {
			if(Util.isNullOuVazio(pessoaPersist.getIdePessoa())) {
				pessoaPersist.setDtcCriacao(new Date());
			}
			pessoaPersist.setIndExcluido(Boolean.FALSE);
			pessoaJuridicaPersist.setPessoa(pessoaPersist);
			pessoaPersist.setPessoaJuridica(pessoaJuridicaPersist);
			if (!imovelRural.getIsFinalizado()) {
				persistirPessoaFisicaOrJuridica();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private boolean proprietarioJaFoiCadastrado() {
		Collection<PessoaImovel> listProprietariosImovel = getListProprietariosImovel();
		Iterator<PessoaImovel> iterator = listProprietariosImovel.iterator();
		while (iterator.hasNext()) {
			PessoaImovel pessoa = iterator.next();
			if (!Util.isNullOuVazio(pessoa) && !Util.isNullOuVazio(pessoa.getIdePessoa().getPessoaFisica())) {
				if (pessoa.getIdePessoa().getPessoaFisica().equals(pessoaFisicaPersist)) {
					return true;
				} else if (!Util.isNullOuVazio(pessoa.getIdePessoa().getPessoaJuridica())
						&& pessoa.getIdePessoa().getPessoaJuridica().getIdePessoaJuridica()
								.equals(pessoaJuridicaPersist.getIdePessoaJuridica())) {
					return true;
				}
			}
		}
		return false;
	}

	public PessoaFisica getPessoaFisicaPersist() {
		return pessoaFisicaPersist;
	}

	public void setPessoaFisicaPersist(PessoaFisica pessoaFisicaPersist) {
		this.pessoaFisicaPersist = pessoaFisicaPersist;
	}

	public PessoaJuridica getPessoaJuridicaPersist() {
		return pessoaJuridicaPersist;
	}

	public void setPessoaJuridicaPersist(PessoaJuridica pessoaJuridicaPersist) {
		this.pessoaJuridicaPersist = pessoaJuridicaPersist;
	}

	public Pessoa getPessoaPersist() {
		return pessoaPersist;
	}

	public void setPessoaPersist(Pessoa pessoaPersist) {
		this.pessoaPersist = pessoaPersist;
	}

	public void setListProprietariosImovel(Collection<PessoaImovel> listProprietariosImovel) {
		this.listProprietariosImovel = listProprietariosImovel;
	}

	public PessoaImovel getProprietarioExclusao() {
		return proprietarioExclusao;
	}

	public void setProprietarioExclusao(PessoaImovel proprietarioExclusao) {
		this.proprietarioExclusao = proprietarioExclusao;
	}

	public boolean isEditandoProprietario() {
		return editandoProprietario;
	}

	public void setEditandoProprietario(boolean editandoProprietario) {
		this.editandoProprietario = editandoProprietario;
	}

	public boolean isEditandoProprietarioCda() {
		return editandoProprietarioCda;
	}

	public void setEditandoProprietarioCda(boolean editandoProprietarioCda) {
		this.editandoProprietarioCda = editandoProprietarioCda;
	}

	public boolean isVisualizarProprietario() {
		return visualizarProprietario;
	}

	public void setVisualizarProprietario(boolean visualizarProprietario) {
		this.visualizarProprietario = visualizarProprietario;
	}

	public boolean isImovelEstaSendoFinalizado() {
		return imovelEstaSendoFinalizado;
	}

	public void setImovelEstaSendoFinalizado(boolean imovelEstaSendoFinalizado) {
		this.imovelEstaSendoFinalizado = imovelEstaSendoFinalizado;
	}

	public void uploadDocumentoPossePropriedade(FileUploadEvent event) {
		try {
			String tipoDocumento = "";
			if (getTipoVinculoImovel().getIdeTipoVinculoImovel().equals(1)) {
				tipoDocumento = "posse_propriedade";
			} else {
				tipoDocumento = "CCIR";
			}

			String caminhoArquivo = FileUploadUtil.EnviarShape(event,
					DiretorioArquivoEnum.IMOVELRURAL.toString() + FileUploadUtil.getFileSeparator()
							+ this.imovelRural.getIdeImovelRural().toString(),
					this.imovelRural.getIdeImovelRural().toString() + "_" + tipoDocumento + "_"
							+ this.imovelRural.getDesDenominacao().replace("/", "").replace("\\", ""));

			if (!Util.isNullOuVazio(this.imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural())) {
				this.imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural()
						.setDscCaminhoArquivo(caminhoArquivo);
				this.imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural()
						.setNomDocumentoObrigatorio(tipoDocumento);
			} else {
				this.imovelRural.getDocumentoImovelRuralPosse()
						.setIdeDocumentoImovelRural(new DocumentoImovelRural(null, tipoDocumento, caminhoArquivo));
				File file = new File(caminhoArquivo.trim());
				this.imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural().setFileSize(file.length());
			}
			this.imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural()
					.setDocumentoImovelRuralPosse(this.imovelRural.getDocumentoImovelRuralPosse());
			this.imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural()
					.setImovelRural(this.imovelRural);
			JsfUtil.addSuccessMessage(Util.getString("documento_obrigatorio_msg_envio_documentacao_efetuado_sucesso"));
			RequestContext.getCurrentInstance().execute("dlgUploadDocumentoPossePropriedade.hide()");

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage("O processo de Upload do arquivo não foi concluído. Por favor, tente novamente.");
		}
	}

	public void uploadDocumentoProcuracaoImovel(FileUploadEvent event) {
		try {

			String tipoDocumento = "Procuracao";

			String caminhoArquivo = FileUploadUtil.EnviarShape(event,
					DiretorioArquivoEnum.IMOVELRURAL.toString() + FileUploadUtil.getFileSeparator()
							+ this.imovelRural.getIdeImovelRural().toString(),
					this.imovelRural.getIdeImovelRural().toString() + "_" + tipoDocumento + "_"
							+ this.imovelRural.getDesDenominacao().replace("/", "").replace("\\", ""));

			if (!Util.isNullOuVazio(this.imovelRural.getIdeDocumentoProcuracao())) {
				this.imovelRural.getIdeDocumentoProcuracao().setDscCaminhoArquivo(caminhoArquivo);
				this.imovelRural.getIdeDocumentoProcuracao().setNomDocumentoObrigatorio(tipoDocumento);
				if (!imovelRural.getIsFinalizado()) {
					imovelRuralServiceFacade
							.atualizarDocumentoObrigatorio(this.imovelRural.getIdeDocumentoProcuracao());
				}
			} else {
				this.imovelRural
						.setIdeDocumentoProcuracao(new DocumentoImovelRural(null, tipoDocumento, caminhoArquivo));
				File file = new File(caminhoArquivo.trim());
				this.imovelRural.getIdeDocumentoProcuracao().setFileSize(file.length());
				if (!imovelRural.getIsFinalizado()) {
					imovelRuralServiceFacade.salvarDocumentoObrigatorio(this.imovelRural.getIdeDocumentoProcuracao());
				}
			}

			JsfUtil.addSuccessMessage(Util.getString("documento_obrigatorio_msg_envio_documentacao_efetuado_sucesso"));
			RequestContext.getCurrentInstance().execute("dlgUploadDocumentoProcuradorImovel.hide()");

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage("O processo de Upload do arquivo não foi concluído, Por favor tente novamente.");
		}
	}

	/**
	 * Ailton
	 * 
	 * @param event
	 */
	public void uploadDocumentoPlanoManejo(FileUploadEvent event) {
		try {

			String tipoDocumento = getManejoSustentavel() ? "Comprovante_Plano_Manejo_Sustentavel"
					: "Comprovante_Plano_Manejo_Cabruca";

			String caminhoArquivo = FileUploadUtil.EnviarShape(event,
					DiretorioArquivoEnum.IMOVELRURAL.toString() + FileUploadUtil.getFileSeparator()
							+ this.imovelRural.getIdeImovelRural().toString(),
					this.imovelRural.getIdeImovelRural().toString() + "_" + tipoDocumento + "_"
							+ this.imovelRural.getDesDenominacao().replace("/", "").replace("\\", ""));

			if (Util.isNull(this.areaProdutivaSelecionada.getIdeDocumentoAutorizacaoManejo())) {
				this.areaProdutivaSelecionada.setIdeDocumentoAutorizacaoManejo(
						new DocumentoImovelRural(null, tipoDocumento, caminhoArquivo));
			} else {
				this.areaProdutivaSelecionada.getIdeDocumentoAutorizacaoManejo().setDscCaminhoArquivo(caminhoArquivo);
			}
			this.areaProdutivaSelecionada.getIdeDocumentoAutorizacaoManejo().setDscCaminhoArquivo(caminhoArquivo);
			this.areaProdutivaSelecionada.getIdeDocumentoAutorizacaoManejo().setNomDocumentoObrigatorio(tipoDocumento);
			this.listaDocumentoPlanoManejo.add(this.areaProdutivaSelecionada.getIdeDocumentoAutorizacaoManejo());

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage("O processo de Upload do arquivo não foi concluído, Por favor tente novamente.");
		}
	}

	public void excluirDocumentoPlanoManejo() {

		try {
			this.areaProdutivaSelecionada.setIdeDocumentoAutorizacaoManejo(null);
			this.listaDocumentoPlanoManejo.clear();

			JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
			return;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro003"));
	}

	/*************************************************************************************************************************/

	public List<DocumentoImovelRural> getListDocumentoPossePropriedade() {
		List<DocumentoImovelRural> list = new ArrayList<DocumentoImovelRural>();
		if (!Util.isLazyInitExcepOuNull(this.imovelRural.getDocumentoImovelRuralPosse()) && !Util
				.isLazyInitExcepOuNull(this.imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural())) {
			if (!Util.isNullOuVazio(this.imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural()
					.getDscCaminhoArquivo())) {
				list.add(this.imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural());
			}
		}
		return list;
	}

	public List<DocumentoImovelRural> getListDocumentoProcuracaoImovel() {
		List<DocumentoImovelRural> list = new ArrayList<DocumentoImovelRural>();
		if (!Util.isLazyInitExcepOuNull(this.imovelRural.getIdeDocumentoProcuracao())) {
			list.add(this.imovelRural.getIdeDocumentoProcuracao());
		}
		return list;
	}

	public void excluirDocumentoPossePropriedade() {
		this.imovelRural.getDocumentoImovelRuralPosse().setIdeDocumentoImovelRural(null);

		JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
	}

	public void excluirDocumentoProcuracaoImovel() {

		DocumentoImovelRural ideDocumentoProcuracao = this.imovelRural.getIdeDocumentoProcuracao();
		this.imovelRural.setIdeDocumentoProcuracao(null);

		try {
			if (!imovelRural.getIsFinalizado()) {
				LocalizacaoGeografica localizacaoGeograficaLoteTemp = null;

				if (imovelRural.isImovelINCRA() && this.imovelRural.getIdeLocalizacaoGeograficaLote() != null
						&& Util.isNullOuVazio(
								this.imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica())) {
					localizacaoGeograficaLoteTemp = this.imovelRural.getIdeLocalizacaoGeograficaLote();
					this.imovelRural.setIdeLocalizacaoGeograficaLote(null);
				}

				persistirImovelRural();
				carregarImovelRural();

				if (localizacaoGeograficaLoteTemp != null) {
					this.imovelRural.setIdeLocalizacaoGeograficaLote(localizacaoGeograficaLoteTemp);
					localizacaoGeograficaLoteTemp = null;
				}
				imovelRuralServiceFacade.excluirDocumentoObrigatorio(ideDocumentoProcuracao);
				auditoria.excluir(ideDocumentoProcuracao);
			}
			JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
			return;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro003"));
	}

	// Fim proprietário imóvel

	// Inicio questionário

	public void setListaSubTipologia(List<Tipologia> listaSubTipologia) {
		this.listaSubTipologia = listaSubTipologia;
	}

	public List<TipologiaAtividade> getListaTipologiaAtividade() {
		return listaTipologiaAtividade;
	}

	public Boolean getIndLancamentoResiduosLiquidos() {
		return indLancamentoResiduosLiquidos;
	}

	public void setIndLancamentoResiduosLiquidos(Boolean indLancamentoResiduosLiquidos) {
		this.indLancamentoResiduosLiquidos = indLancamentoResiduosLiquidos;
	}

	/**
	 * Depreciei o método por não haver nenhuma implementação
	 * 
	 * @deprecated
	 */
	public void anulaValorReservaLegal() {
		// if (temAverbado)
		// temProcesso = false;
		//
		// inema_numero_processo = null;
		// numCertificado = null;
		// tipoOrigemCertificado = null;
		// listaArquivoReservaAprovada.clear();
	}

	public void changeDesejaCompletarInformacoes(ValueChangeEvent event) {
		imovelRural.setIndDesejaCompletarInformacoes((Boolean) event.getNewValue());
		if (!imovelRural.getIndDesejaCompletarInformacoes()) {
			RequestContext.getCurrentInstance().execute("confirmacaoExclusaoDadosEspecificos.show()");
		}
	}

	public void excluirDadosQuestionario(Boolean confirmaExclusao) {
		if (confirmaExclusao) {
			this.imovelRural.setIndDesejaCompletarInformacoes(false);
			this.imovelRural.setIndApp(null);
			this.imovelRural.setIndAreaProdutiva(null);
			this.imovelRural.setIndAgrotoxico(null);
			this.imovelRural.setIndVegetacaoNativa(null);
			this.imovelRural.setIndSupressaoVegetacao(null);
			this.imovelRural.setIndRppn(null);
			this.imovelRural.setIndTac(null);
			this.imovelRural.setIndConcessionaria(null);
			this.imovelRural.setIndPrecipitacao(null);
			this.imovelRural.setIndSubterraneo(null);
			this.imovelRural.setIndSuperficial(null);
			this.imovelRural.setIndContratoConvenio(null);

			if (!Util.isNullOuVazio(this.imovelRural.getSupressaoVegetacao())) {
				this.imovelRural.getSupressaoVegetacao().setValArea(null);
				this.imovelRural.getSupressaoVegetacao().setNumProcesso(null);
			}

			if (!Util.isNullOuVazio(this.imovelRural.getImovelRuralTac())) {
				this.imovelRural.getImovelRuralTac().setDscOrgaoEmissor(null);
				this.imovelRural.getImovelRuralTac().setDtcAssinatura(null);
				this.imovelRural.getImovelRuralTac().setDtcEncerramento(null);
			}

			if (!Util.isNullOuVazio(this.imovelRural.getImovelRuralPrad())) {
				this.imovelRural.getImovelRuralPrad().setDscOrgaoEmissor(null);
				this.imovelRural.getImovelRuralPrad().setDtcAssinatura(null);
				this.imovelRural.getImovelRuralPrad().setDtcEncerramento(null);
			}

			this.imovelRural.setIndLancamentoConcessionaria(null);
			this.imovelRural.setIndLancamentoManancial(null);

			if (getShowPCT()) {
				this.setIndLancamentoResiduosLiquidos(null);
			} else {
				this.setIndLancamentoResiduosLiquidos(false);
			}

			this.imovelRural.setIndIntervencao(null);
			this.imovelRural.setIndOutrosPassivos(null);

			if (!imovelRural.getIsFinalizado()) {
				try {
					excluirDadosCefir();
					imovelRuralServiceFacade.atualizarImovelRural(this.imovelRural);
					carregarImovelRural();
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
				}
			}
		} else {
			imovelRural.setIndDesejaCompletarInformacoes(true);
		}
	}

	public void changePerguntaAderiuPara(ValueChangeEvent event) {
		Boolean valor = (Boolean) event.getNewValue();
		if (!valor) {
			this.imovelRural.setNumProcessoPara(null);
		}
	}

	public void changeIndLancamentoResiduosLiquidos(ValueChangeEvent event) {
		Boolean valor = (Boolean) event.getNewValue();
		if (!valor) {
			this.imovelRural.setIndLancamentoConcessionaria(null);
			this.imovelRural.setIndLancamentoManancial(null);
		}
	}

	public void setDscTipoUsoAgua(String dscTipoUsoAgua) {
		this.dscTipoUsoAgua = dscTipoUsoAgua;
	}

	public Integer getTipoUsoAgua() {
		return tipoUsoAgua;
	}

	public void changeSupressaoVegetacao(ValueChangeEvent event) {
		Boolean valor = (Boolean) event.getNewValue();

		if (valor != null && valor && Util.isNullOuVazio(imovelRural.getSupressaoVegetacao())) {
			imovelRural.setSupressaoVegetacao(new SupressaoVegetacao());
		}
	}

	// Fim questionário

	public boolean getIsInclusaoAtividade() {
		return isInclusaoAtividade;
	}

	public void setInclusaoAtividade(boolean isInclusaoAtividade) {
		this.isInclusaoAtividade = isInclusaoAtividade;
	}

	// Inicio Listagem
	public String carregarPagListarImoveisRurais() {
		ContextoUtil.getContexto().setIsProcPfPjOuRepLegal(null);
		ContextoUtil.getContexto().setImovelRural(null);
		ContextoUtil.getContexto().setLabelTitutoRequerimento("Cadastro de Imóvel Rural");
		return "/paginas/manter-imoveis/cefir/listaImoveisRural.xhtml";
	}

	// Fim Listagem

	// Finalização inicio

	public String finalizaCadImovelRuralRequerimento() {
		try {
			salvarDadosParaFinalizar();
			if (Boolean.TRUE.equals(imovelRural.getIndDesejaCompletarInformacoes())) {
				if (!Util.isNullOuVazio(imovelRural.getReservaLegal())
						&& (imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(2)
								|| imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(3))
						|| getShowPCT()) {
					if (this.imovelRural.isImovelCDA() || this.imovelRural.isImovelBNDES()) {
						if (!isImovelCDAOuBndesStatusCadastrado()) {
							finalizaStatusRegistradoInformacoesCompletas();
						} else {
							finalizaStatusCadastrado();
						}
					} else {
						finalizaStatusCadastrado();
					}
				} else {
					finalizaStatusRegistradoInformacoesCompletas();
				}
			} else {
				finalizaStatusRegistradoInformacoesIncompletas();
			}
			bloquearLimiteImovel();
			return carregarPagListarImoveisRurais();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(
					"Erro na finalização do imóvel favor contactar o administrador do sistema. " + e.getMessage());
			return null;
		}
	}

	private void finalizaStatusRegistradoInformacoesIncompletas() throws Exception {
		ImovelRural objAntigo = imovelRuralServiceFacade.carregarTudo(this.imovelRural.getIdeImovelRural());
		imovelRural.setStatusCadastro(StatusCadastroImovelRuralEnum.REGISTRADO.getId());
		this.imovelRural.setDtcFinalizacao(new Date());

		if (Util.isNull(this.imovelRural.getDtcPrimeiraFinalizacao())) {
			this.imovelRural.setDtcPrimeiraFinalizacao(new Date());
		}
		salvarRequerimentoImovel(this.imovelRural);
		auditoria.atualizar(objAntigo, this.imovelRural);
		ContextoUtil.getContexto().setObject("Cadastro básico do Imóvel Rural concluído com Sucesso!");
	}

	private void finalizaStatusCadastrado() throws Exception {
		imovelRural.setStatusCadastro(StatusCadastroImovelRuralEnum.CADASTRADO.getId());
		this.imovelRural.setDtcFinalizacao(new Date());
		if (Util.isNull(this.imovelRural.getDtcPrimeiraFinalizacao())) {
			this.imovelRural.setDtcPrimeiraFinalizacao(new Date());
		}
		this.imovelRural = imovelRuralServiceFacade.montarGeracaoCertificadoImovelRural(this.imovelRural);
		salvarRequerimentoImovel(imovelRural);

		this.imovelRuralServiceFacade.gerarCertificado(this.imovelRural);
		/**
		 * Carregando PctImovelRural para gerar Json
		 **/
		if (imovelRural.isImovelPCT()) {
			imovelRural.setIdePctImovelRural(pctImovelRuralFacade.buscarPctImovelRural(imovelRural));
			imovelRuralFacade.buscarLocalizacaoGeograficaPCT(imovelRural);
			GeoJsonSicar geoJsonSicar = imovelRuralFacade.buscarGeoJsonSicar(Arrays.asList(
					imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica()));
			imovelRural.setGeoJsonSicarPctLimiteTerritorio(geoJsonSicar);
			GeoJsonSicar geoJsonSicar2 = imovelRuralFacade.buscarGeoJsonSicar(
					Arrays.asList(imovelRural.getIdeLocalizacaoGeograficaPct().getIdeLocalizacaoGeografica()));
			imovelRural.setGeoJsonSicarPct(geoJsonSicar2);
		}

		if (!Util.isNullOuVazio(imovelRural.getImovelRuralSicar())
				&& !Util.isNullOuVazio(imovelRural.getImovelRuralSicar().getIdeImovelRuralSicar())) {
			montarImovelRuralSicar();
			imovelRuralServiceFacade.atualizarImovelRuralSicar(imovelRural.getImovelRuralSicar());
		} else {
			imovelRural.setImovelRuralSicar(new ImovelRuralSicar());
			imovelRural.getImovelRuralSicar().setDtcCriacao(new Date());
			montarImovelRuralSicar();
			imovelRuralServiceFacade.salvarImovelRuralSicar(imovelRural.getImovelRuralSicar());
		}

		if (!getShowPCT()) {
			ContextoUtil.getContexto().setObject(Util.getString("cefir_msg_S004"));
		}
	}

	private void finalizaStatusRegistradoInformacoesCompletas() throws Exception {
		imovelRural.setStatusCadastro(StatusCadastroImovelRuralEnum.REGISTRADO.getId());
		imovelRural.setStsCertificado(false);
		ImovelRural objAntigo = imovelRuralServiceFacade.carregarTudo(this.imovelRural.getIdeImovelRural());
		this.imovelRural.setDtcFinalizacao(new Date());

		if (Util.isNull(this.imovelRural.getDtcPrimeiraFinalizacao())) {
			this.imovelRural.setDtcPrimeiraFinalizacao(new Date());
		}

		if (this.imovelRural.isImovelBNDES()) {
			imovelRural.setStsCertificado(false);
			salvarRequerimentoImovel(this.imovelRural);
			this.imovelRuralServiceFacade.gerarCertificado(this.imovelRural);
		} else {

			salvarRequerimentoImovel(this.imovelRural);
		}

		// AUDITORIA
		auditoria.atualizar(objAntigo, this.imovelRural);
		if (!Util.isNullOuVazio(imovelRural.getImovelRuralSicar())
				&& !Util.isNullOuVazio(imovelRural.getImovelRuralSicar().getIdeImovelRuralSicar())) {
			montarImovelRuralSicar();
			imovelRuralServiceFacade.atualizarImovelRuralSicar(imovelRural.getImovelRuralSicar());
		} else {
			imovelRural.setImovelRuralSicar(new ImovelRuralSicar());
			imovelRural.getImovelRuralSicar().setDtcCriacao(new Date());
			montarImovelRuralSicar();
			imovelRuralServiceFacade.salvarImovelRuralSicar(imovelRural.getImovelRuralSicar());
		}

		/*
		 * if(this.imovelRural.isImovelBNDES()) {
		 * imovelRuralServiceFacade.enviarEmaiComAvisoBndes(this.imovelRural); }
		 */

		ContextoUtil.getContexto().setObject(
				"As informações são enviadas ao Sistema de Cadastro Ambiental Rural - SICAR diariamente, em horário específico. Por isso, o seu número CAR, que dará acesso ao recibo de inscrição no CAR, será disponibilizado em até 48 horas. O INEMA enviará comunicado via email sobre a complementação das informações a respeito da Reserva Legal do imóvel, quando ficará disponível o Certificado de Inscrição ou Termo de Compromisso do CEFIR, conforme o caso.");
	}

	private void carregarDadosGeoJsonSicar() throws Exception {
		List<Integer> localizacoes = new ArrayList<Integer>();
		localizacoes.add(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		imovelRural.setGeoJsonSicar(imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes));

		localizacoes.clear();

		if (!Util.isNullOuVazio(imovelRural.getReservaLegal())
				&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica())) {
			localizacoes.add(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			imovelRural.getReservaLegal().setGeoJsonSicar(imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes));
		}

		localizacoes.clear();

		if (imovelRural.isImovelPCT()) {
			localizacoes
					.add(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica());
			imovelRural.setGeoJsonSicarPctLimiteTerritorio(imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes));

			localizacoes.clear();

			localizacoes.add(imovelRural.getIdeLocalizacaoGeograficaPct().getIdeLocalizacaoGeografica());
			imovelRural.setGeoJsonSicarPct(imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes));

			localizacoes.clear();
		}

		localizacoes.clear();

		if (imovelRural.getIndVegetacaoNativa() && !Util.isNullOuVazio(imovelRural.getVegetacaoNativa())) {
			localizacoes
					.add(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			imovelRural.getVegetacaoNativa().setGeoJsonSicar(imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes));
		}

		localizacoes.clear();

		if (!Util.isNullOuVazio(this.imovelRural.getIndAreaRuralConsolidada())
				&& this.imovelRural.getIndAreaRuralConsolidada()) {
			localizacoes.add(imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
					.getIdeLocalizacaoGeografica());
			imovelRural.getIdeAreaRuralConsolidada()
					.setGeoJsonSicar(imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes));
		}

		if (imovelRural.getIndApp() && !Util.isNullOuVazio(imovelRural.getAppCollection())) {
			List<Integer> localizacoesApp = new ArrayList<Integer>();
			for (App app : imovelRural.getAppCollection()) {
				localizacoes.clear();
				localizacoes.add(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				app.setGeoJsonSicar(imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes));
				localizacoesApp.add(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
			imovelRural.setGeoJsonSicarAppTotal(imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoesApp));
		}
	}

	private void montarImovelRuralSicar() throws Exception {
		carregarDadosGeoJsonSicar();
		carregarRepresentantesLegaisAssociacao();
		String numToken = "";

		if (imovelRural.isTermoCompromisso()) {
			List<Certificado> lCefir = imovelRuralServiceFacade.obterCertificadoPorImovelAndTipo(
					this.imovelRural.getIdeImovelRural(), TipoCertificadoEnum.TERMO_DE_COMPROMISSO.getId());
			numToken = "-" + lCefir.get(0).getNumToken();
			imovelRural.getImovelRuralSicar().setNumCertificado(lCefir.get(0).getNumCertificado());
		} else {
			List<Certificado> lCefir = imovelRuralServiceFacade.obterCertificadoPorImovelAndTipo(
					this.imovelRural.getIdeImovelRural(), TipoCertificadoEnum.CEFIR.getId());
			if (!Util.isNullOuVazio(lCefir)) {
				numToken = "-" + lCefir.get(0).getNumToken();
				imovelRural.getImovelRuralSicar().setNumCertificado(lCefir.get(0).getNumCertificado());
			}
		}

		imovelRural.getImovelRuralSicar().setIdeImovelRural(imovelRural);
		imovelRural.getImovelRuralSicar().setNumProtocolo("BA-" + imovelRural.getIdeImovelRural() + "-"
				+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + numToken);
		imovelRural.getImovelRuralSicar().setIndSicronia(false);
		imovelRural.getImovelRuralSicar().setDtcIniSicronia(null);
		imovelRural.getImovelRuralSicar().setDtcFimSicronia(null);
		imovelRural.getImovelRuralSicar().setToken(null);
		imovelRural.getImovelRuralSicar().setMsgRetornoSincronia(null);
		imovelRural.getImovelRuralSicar().setCodRetornoSincronia(null);
		imovelRural.getImovelRuralSicar().setUrlReciboInscricao(null);
		imovelRural.getImovelRuralSicar().setJson(
				JsonUtil.montarJsonImovelRuralSicar(imovelRural, this.imovelRural.getIdeImovelRuralRppn()).toString());
	}

	private void carregarRepresentantesLegaisAssociacao() throws Exception {
		if (!Util.isNullOuVazio(this.imovelRural.getIdePctImovelRural())
				&& !Util.isNullOuVazio(this.imovelRural.getIdePctImovelRural().getPessoaJuridicaPctListaAssociacao())) {
			for (PessoaJuridicaPct pessoaJuridicaPct : this.imovelRural.getIdePctImovelRural()
					.getPessoaJuridicaPctListaAssociacao()) {
				if (Util.isNullOuVazio(pessoaJuridicaPct.getIdePessoaJuridica().getRepresentanteLegalCollection())) {
					pessoaJuridicaPct.getIdePessoaJuridica()
							.setRepresentanteLegalCollection(new ArrayList<RepresentanteLegal>());
					List<RepresentanteLegal> representantesLegais = representanteLegalService
							.getListaRepresentanteLegalByPessoa(pessoaJuridicaPct.getIdePessoaJuridica());
					for (RepresentanteLegal representanteLegal : representantesLegais) {
						if (Util.isNullOuVazio(
								representanteLegal.getIdePessoaFisica().getPessoa().getTelefoneCollection())) {
							representanteLegal.getIdePessoaFisica().getPessoa()
									.setTelefoneCollection(new ArrayList<Telefone>());
							List<Telefone> telefones = telefoneService
									.buscarTelefonesPorPessoa(representanteLegal.getIdePessoaFisica().getPessoa());
							representanteLegal.getIdePessoaFisica().getPessoa().getTelefoneCollection()
									.addAll(telefones);
							EnderecoPessoa enderecoPessoa = enderecoPessoaService
									.buscarEnderecoPorPessoa(representanteLegal.getIdePessoaFisica().getPessoa());
							if (!Util.isNullOuVazio(enderecoPessoa)) {
								representanteLegal.getIdePessoaFisica().getPessoa()
										.setEndereco(enderecoPessoa.getIdeEndereco());
							} else {
								representanteLegal.getIdePessoaFisica().getPessoa().setEndereco(new Endereco());
							}
						}
					}
					pessoaJuridicaPct.getIdePessoaJuridica().getRepresentanteLegalCollection()
							.addAll(representantesLegais);
				}
			}
		}
	}

	private void salvarRequerimentoImovel(ImovelRural imovelRural) {
		Requerimento requerimento;// = new Requerimento();
		RequerimentoImovel reqImovel = null;
		Pessoa solicitante = new Pessoa();
		solicitante = getRequerenteCadastro();
		try {

			requerimento = imovelRuralServiceFacade.carregarRequerimentoByIdeImovel(imovelRural.getIdeImovelRural());
			if (Util.isNullOuVazio(requerimento) || Util.isNullOuVazio(requerimento.getIdeRequerimento())) {
				requerimento = new Requerimento();
				requerimento.setIdeOrgao(new Orgao(1, 1));
				requerimento.getIdeOrgao().setDscSiglaOrgao("INEMA");
				EnderecoPessoa enderecoSolicitante = imovelRuralServiceFacade.filtrarEnderecoByPessoa(solicitante);
				if (!Util.isNullOuVazio(enderecoSolicitante)
						&& !Util.isNullOuVazio(enderecoSolicitante.getIdeEndereco()))
					requerimento.setIdeEnderecoContato(enderecoSolicitante.getIdeEndereco());
				requerimento.setIdeTipoRequerimento(new TipoRequerimento(4));
				List<Telefone> telefoneSolicitante = imovelRuralServiceFacade.filtraTelefonePorPessoa(solicitante);
				if (!Util.isNullOuVazio(telefoneSolicitante) && !Util.isNullOuVazio(telefoneSolicitante.get(0)))
					requerimento.setNumTelefone(telefoneSolicitante.get(0).getNumTelefone());
				requerimento.setDesEmail(solicitante.getDesEmail());
				if (!Util.isNullOuVazio(solicitante.getPessoaFisica())) {
					requerimento.setNomContato(solicitante.getPessoaFisica().getNomPessoa());
				} else {
					requerimento.setNomContato(solicitante.getPessoaJuridica().getNomRazaoSocial());
				}
				requerimento.setIndDeclaracao(true);

				requerimento.setDtcCriacao(new Date());
				requerimento.setNumRequerimento(imovelRuralServiceFacade.gerarNumero(requerimento));
				imovelRuralServiceFacade.inserirRequerimento(requerimento);

				reqImovel = new RequerimentoImovel();
				reqImovel.setRequerimento(requerimento);
				reqImovel.setImovel(imovelRural.getImovel());
				reqImovel.setIdeTipoImovelRequerimento(new TipoImovelRequerimento(2));
				reqImovel.setDtcCriacao(new Date());
			} else {
				reqImovel = imovelRuralServiceFacade.obterUltimoRequerimentoImovel(imovelRural.getImovel());
			}

			reqImovel.setVlrArea(BigDecimal.valueOf(imovelRural.getValArea()));
			imovelRuralServiceFacade.salvarRequerimentoImovel(reqImovel);

			ImovelRural objAntigo = imovelRuralServiceFacade.carregarTudo(this.imovelRural.getIdeImovelRural());
			imovelRuralServiceFacade.atualizarImovelRural(imovelRural);
			// AUDITORIA
			auditoria.atualizar(objAntigo, this.imovelRural);

		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível Finalizar o requerimento do Cadastro de Imóvel Rural!");
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public String validaFinalizaCadImovelRuralRequerimento() {
		try {
			this.rlMenorQueVintePorCento = false;
			this.existeSobreposicaoRlApp = false;
			this.existeSobreposicaoRlAreaProdutiva = false;
			this.processoRlConcluidoCerberus = false;

			RequestContext context = RequestContext.getCurrentInstance();
			context.addCallbackParam("openModal", false);
			context.addCallbackParam("isRlMenorQueVintePorCento", rlMenorQueVintePorCento);
			context.addCallbackParam("existeSobreposicaoRlApp", existeSobreposicaoRlApp);
			context.addCallbackParam("existeSobreposicaoRlAp", existeSobreposicaoRlAreaProdutiva);
			context.addCallbackParam("processoRlConcluidoCerberus", processoRlConcluidoCerberus);

			if (validaImovelRuralRevalidacao(imovelRural) || isUsuarioSemRestricao()) {
				if (imovelRural.isImovelBNDES()) {
					ContratoConvenioCefir contratoConvenioCefir = imovelRuralServiceFacade
							.buscarContratoConvenioByNumero(
									this.imovelRural.getIdeContratoConvenioCefir().getNumContratoConvenioCefir());

					if (!Util.isNullOuVazio(contratoConvenioCefir)) {
						validarContratoConvenioCefirBndes(contratoConvenioCefir);
					} else {
						throw new Exception(Util.getString("cefir_msg_A014"));
					}
				}

				if (isUsuarioSemRestricao()) {
					context.addCallbackParam("openModal", true);
					this.existeSobreposicaoRlApp = false;
					this.existeSobreposicaoRlAreaProdutiva = false;
					/*
					 * this.rlMenorQueVintePorCento = (Boolean)
					 * context.getCallbackParams().get("isRlMenorQueVintePorCento");
					 */
					/*
					 * this.processoRlConcluidoCerberus = (Boolean)
					 * context.getCallbackParams().get("processoRlConcluidoCerberus");
					 */
				} else {
					if (ContextoUtil.getContexto().isPCT()) {
						imovelRural.setIdePctImovelRural(pctImovelRural);
					}

					if (!ContextoUtil.getContexto().isPCT() && !imovelRural.isMenorQueQuatroModulosFiscais()
							&& !this.validaQuestionario()) {
						throw new Exception("O preenchimento do questionário é obrigatório!");
					}

					if (imovelRuralServiceFacade.validaFinalizacaoImovelRural(imovelRural, this.listProprietariosImovel,
							this.tipoVinculoImovel, context, aceiteCondicoesRecuperacaoOp)) {
						context.addCallbackParam("openModal", true);

						this.rlMenorQueVintePorCento = (Boolean) context.getCallbackParams()
								.get("isRlMenorQueVintePorCento");
						this.existeSobreposicaoRlApp = (Boolean) context.getCallbackParams()
								.get("existeSobreposicaoRlApp");
						this.existeSobreposicaoRlAreaProdutiva = (Boolean) context.getCallbackParams()
								.get("existeSobreposicaoRlAp");
						this.processoRlConcluidoCerberus = (Boolean) context.getCallbackParams()
								.get("processoRlConcluidoCerberus");
					}
				}
			}

			return "";
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);

			if (!ContextoUtil.getContexto().isPCT()) {
				JsfUtil.addErrorMessage(e.getMessage());
			} else {
				JsfUtil.addWarnMessage(e.getMessage());
			}

			return null;
		}
	}

	private boolean validaImovelRuralRevalidacao(ImovelRural pImovelRural) {
		try {
			if (!Util.isNullOuVazio(pImovelRural.getImovelRuralRevalidacaoCollection()) && !isUsuarioSemRestricao()) {
				imovelRuralServiceFacade.validaImovelRuralRevalidacao(pImovelRural);
			}
			return true;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			return false;
		}
	}

	private void salvarDadosParaFinalizar() throws Exception {
		VegetacaoNativa vegetacaoTemp = null;
		ReservaLegal reservaLegalTemp = null;
		ImovelRuralRppn imovelRuralRppnTemp = null;
		OutrosPassivosAmbientais outrosPassivosAmbientaisTemp = null;
		AreaRuralConsolidada areaRuralConsolidadaTemp = null;

		excluirDadosCefir();

		if (!Util.isNullOuVazio(imovelRural.getVegetacaoNativa())) {
			vegetacaoTemp = imovelRural.getVegetacaoNativa();
			imovelRural.setVegetacaoNativa(null);
		}

		if (!Util.isNullOuVazio(imovelRural.getIdeImovelRuralRppn())) {
			imovelRuralRppnTemp = imovelRural.getIdeImovelRuralRppn();
			imovelRural.setIdeImovelRuralRppn(null);
		}

		if (!Util.isNullOuVazio(imovelRural.getReservaLegal())) {
			if (!Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeTipoArl())
					&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl())) {
				reservaLegalTemp = imovelRural.getReservaLegal();
			}
			imovelRural.setReservaLegal(null);
		}

		if (!Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais())) {
			outrosPassivosAmbientaisTemp = imovelRural.getOutrosPassivosAmbientais();
			imovelRural.setOutrosPassivosAmbientais(null);
		}

		if (!Util.isNullOuVazio(imovelRural.getIdeAreaRuralConsolidada())) {
			areaRuralConsolidadaTemp = imovelRural.getIdeAreaRuralConsolidada();
			imovelRural.setIdeAreaRuralConsolidada(null);
		}

		// Tratamento de passivos de proprietários cadastrados e o tipo do imóvel é
		// justo possuidor
		// - Na finalização caso forma de vínculo seja justo possuidor todos os
		// proprietários ainda cadastrados serão vinculados como justo possuidor

		if (!Util.isNullOuVazio(listProprietariosImovel)) {
			for (PessoaImovel pessoaImovel : listProprietariosImovel) {
				if (!pessoaImovel.getIndExcluido()) {
					pessoaImovel.setIdeTipoVinculoImovel(getTipoVinculoImovel());
				}
			}
		}

		// Salva os objetos da aba Dados Básicos
		salvarDadosBasicos();

		// Salva os objetos da aba Limite do Imóvel
		persistirLocalizacaoGeograficaImovelRural();

		if (Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote())) {
			imovelRural.setIdeLocalizacaoGeograficaLote(null);
		}

		// Salva os objetos da Reserva Legal na aba Dados Específicos
		imovelRural.setReservaLegal(reservaLegalTemp);

		if (!imovelRural.isObrigatorioRl() && Util.isNull(this.imovelRural.getReservaLegal())) {
			ReservaLegal reservaLegal = imovelRuralServiceFacade.buscaReservaLegalByImovelRural(imovelRural);

			if (!Util.isNullOuVazio(reservaLegal) && !Util.isNullOuVazio(reservaLegal.getIdeReservaLegal())) {
				LocalizacaoGeografica locReserva = reservaLegal.getIdeLocalizacaoGeografica();

				if (!Util.isNullOuVazio(reservaLegal.getIdeReservaLegalAverbada())) {
					imovelRuralServiceFacade.removerReservaLegalAverbada(reservaLegal.getIdeReservaLegalAverbada());
				}

				imovelRuralServiceFacade.removerReservaLegal(reservaLegal);
				auditoria.excluir(reservaLegal);

				if (!Util.isNullOuVazio(locReserva)) {
					imovelRuralServiceFacade.excluirLocalizacaoGeografica(locReserva);
					auditoria.excluir(locReserva);
				}
			}
		} else {
			ReservaLegal objAntigoReservaLegal = null;
			CronogramaRecuperacao objAntigoCronogramaRecuperacaoRl = null;

			if (!Util.isNullOuVazio(imovelRural.getReservaLegal())
					&& (imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(2)
							|| imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(3))) {
				imovelRural.getReservaLegal().setIndSobreposicaoApp(existeSobreposicaoRlApp);
				imovelRural.getReservaLegal().setIndSobreposicaoAp(existeSobreposicaoRlAreaProdutiva);
				imovelRural.getReservaLegal().setIndMenorVintePorcento(rlMenorQueVintePorCento);

				if (existeSobreposicaoRlApp || rlMenorQueVintePorCento) {
					imovelRural.setIndPermissaoASV(false);
				} else {
					imovelRural.setIndPermissaoASV(true);
				}

				// copiar para salvarRl
				if (Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeStatus()) || (!imovelRural.getReservaLegal()
						.getIdeStatus().getIdeStatusReservaLegal().equals(1)
						&& !imovelRural.getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(2))) {
					if (!Util.isNullOuVazio(imovelRural.getReservaLegal().getIndAverbada())
							&& imovelRural.getReservaLegal().getIndAverbada()) {
						imovelRural.getReservaLegal().setIdeStatus(new StatusReservaLegal(4)); // Aguardando validação
																								// de documento
					} else {
						if (!Util.isNullOuVazio(imovelRural.getIndReservaLegal()) && imovelRural.getIndReservaLegal()
								&& processoRlConcluidoCerberus) {
							imovelRural.getReservaLegal().setIdeStatus(new StatusReservaLegal(1));
							imovelRural.getReservaLegal()
									.setIdeUsuarioAprovacao(imovelRuralServiceFacade.buscarPorLogin("CERBERUS"));
							imovelRural.getReservaLegal().setDtcAprovacao(new Date());
						} else if (!Util.isNullOuVazio(imovelRural.getIndReservaLegal())
								&& imovelRural.getIndReservaLegal()) {
							if (!imovelRural.getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(9)
									|| imovelRural.getReservaLegal().isAlterarStatusRl()) {
								imovelRural.getReservaLegal().setIdeStatus(new StatusReservaLegal(8));
							}
						}
					}
				}

				imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(
						retornaClassificacaoSecaoGeometricaSelecionado(imovelRural.getReservaLegal()
								.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));
				imovelRural.getReservaLegal().getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(imovelRural.getReservaLegal()
								.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));

				if (!Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao())
						&& !Util.isNullOuVazio(
								imovelRural.getReservaLegal().getCronogramaRecuperacao().getIndPradImportada())
						&& imovelRural.getReservaLegal().getCronogramaRecuperacao().getIndPradImportada()) {
					imovelRural.getReservaLegal().getCronogramaRecuperacao().setIdeDocumentoObrigatorio(
							retornarDocumentoPradRlSelecionado(imovelRural.getReservaLegal().getCronogramaRecuperacao()
									.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()));
					imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
							.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(
									imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
											.getIdeClassificacaoSecao().getIdeClassificacaoSecao()));
					imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
							.setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(
									imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
											.getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
				}

				objAntigoReservaLegal = imovelRuralServiceFacade.carregarTudo(imovelRural.getReservaLegal());

				if (!Util.isNull(objAntigoReservaLegal)
						&& !Util.isNullOuVazio(objAntigoReservaLegal.getCronogramaRecuperacao()) && !Util.isNullOuVazio(
								objAntigoReservaLegal.getCronogramaRecuperacao().getIdeCronogramaRecuperacao())) {
					objAntigoCronogramaRecuperacaoRl = imovelRuralServiceFacade
							.filtrarCronogramaRecuperacaoById(objAntigoReservaLegal.getCronogramaRecuperacao());
				}

				imovelRuralServiceFacade.persistirReservaLegal(this.imovelRural, objAntigoReservaLegal,
						objAntigoCronogramaRecuperacaoRl);

				if (!Util.isNull(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica())
						&& !Util.isNull(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica()
								.getNovosArquivosShapeImportados())
						&& imovelRural.getReservaLegal().getIdeLocalizacaoGeografica()
								.getNovosArquivosShapeImportados()) {
					FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(
							imovelRural.getReservaLegal().getIdeLocalizacaoGeografica(),
							TemaGeoseiaEnum.RESERVA_LEGAL.getId().toString(),
							imovelRural.getIdeImovelRural().toString());
					imovelRuralServiceFacade
							.persistirShapes(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica(), null);
					imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
				}

				if (!Util.isNull(imovelRural.getReservaLegal().getCronogramaRecuperacao())
						&& !Util.isNull(
								imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica())
						&& !Util.isNull(imovelRural.getReservaLegal().getCronogramaRecuperacao()
								.getLocalizacaoGeografica().getNovosArquivosShapeImportados())
						&& imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
								.getNovosArquivosShapeImportados()) {
					FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(
							imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica(),
							TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId().toString(),
							imovelRural.getIdeImovelRural().toString());
					imovelRuralServiceFacade.persistirShapes(
							imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica(), null);
					imovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica()
							.setNovosArquivosShapeImportados(false);
				}
			} else {
				if (!Util.isNullOuVazio(imovelRural.getReservaLegal())) {
					imovelRural.getReservaLegal().setIdeStatus(new StatusReservaLegal(7));
					imovelRuralServiceFacade.persistirReservaLegal(this.imovelRural, objAntigoReservaLegal,
							objAntigoCronogramaRecuperacaoRl);
				}
			}
		}

		// Salva os demais dados da aba Dados específicos
		if (isHabilitaCadastroAmbiental()) {
			// Responsavel Técnico
			carregarListaEscolaridade();
			for (ResponsavelImovelRural r : this.imovelRural.getResponsavelImovelRuralCollection()) {
				persistirResponsavelTecnico(r);
			}

			// APP
			if (!Util.isNull(this.imovelRural.getIndApp()) && this.imovelRural.getIndApp()) {
				for (App app : this.imovelRural.getAppCollection()) {
					if (app.getIndExcluido()) {
						persistirExclusaoApp(app);
						continue;
					}
					persistirApp(app);
				}
			}

			// Atividade Desenvolvida
			if (!Util.isNull(this.imovelRural.getIndAreaProdutiva()) && this.imovelRural.getIndAreaProdutiva()) {
				for (AreaProdutiva ap : this.imovelRural.getAreaProdutivaCollection()) {
					if (ap.getIndExcluido()) {
						persistirExclusaoAreaProduitiva(ap);
						continue;
					}
					persistirAreaProdutiva(ap);
				}
			}

			// Vegetação Nativa
			if (!Util.isNullOuVazio(vegetacaoTemp)) {
				imovelRural.setVegetacaoNativa(vegetacaoTemp);
			}

			if (!Util.isNull(this.imovelRural.getIndVegetacaoNativa()) && this.imovelRural.getIndVegetacaoNativa()) {
				persistirVegetacaoNativa();
			}

			// Área Rural Consolidada
			if (!Util.isNullOuVazio(this.imovelRural.getIndAreaRuralConsolidada())
					&& this.imovelRural.getIndAreaRuralConsolidada() && !Util.isNullOuVazio(areaRuralConsolidadaTemp)) {
				this.imovelRural.setIdeAreaRuralConsolidada(areaRuralConsolidadaTemp);
				persistirAreaRuralConsolidada();
			}

			// RPPN
			if (!Util.isNullOuVazio(imovelRuralRppnTemp)) {
				imovelRural.setIdeImovelRuralRppn(imovelRuralRppnTemp);
			}

			if (!Util.isNullOuVazio(this.imovelRural.getIndRppn()) && this.imovelRural.getIndRppn()) {
				persistirImovelRppn();
			}

			// Uso da Água - Captação Subterrânea
			if (!Util.isNull(this.imovelRural.getIndSubterraneo()) && this.imovelRural.getIndSubterraneo()
					&& !Util.isNullOuVazio(listCapSubterranea)) {
				for (ImovelRuralUsoAgua i : listCapSubterranea) {
					this.imovelRuralUsoAgua = i;
					persistirUsoAgua();
				}
				limparListasUsoAgua();
			}

			// Uso da Água - Captação Superficial
			if (!Util.isNullOuVazio(this.imovelRural.getIndSuperficial()) && this.imovelRural.getIndSuperficial()
					&& !Util.isNullOuVazio(listCapSuperficial)) {
				for (ImovelRuralUsoAgua i : listCapSuperficial) {
					this.imovelRuralUsoAgua = i;
					persistirUsoAgua();
				}
				limparListasUsoAgua();
			}

			// Uso da Água - Lançamento Resíduos
			if (!Util.isNullOuVazio(this.imovelRural.getIndLancamentoManancial())
					&& !Util.isNullOuVazio(listLancamentoResiduos)) {
				for (ImovelRuralUsoAgua i : listLancamentoResiduos) {
					this.imovelRuralUsoAgua = i;
					persistirUsoAgua();
				}
				limparListasUsoAgua();
			}

			// Uso da Água - Intervenção
			if (!Util.isNull(this.imovelRural.getIndIntervencao()) && !Util.isNullOuVazio(listPontoIntervencao)) {
				for (ImovelRuralUsoAgua i : listPontoIntervencao) {
					this.imovelRuralUsoAgua = i;
					persistirUsoAgua();
				}
				limparListasUsoAgua();
			}

			// Outros Passivos
			if (!Util.isNullOuVazio(outrosPassivosAmbientaisTemp)) {
				imovelRural.setOutrosPassivosAmbientais(outrosPassivosAmbientaisTemp);
			}
			if (!Util.isNull(this.imovelRural.getIndOutrosPassivos()) && this.imovelRural.getIndOutrosPassivos()) {
				persistirOutrosPassivos();
			}

			// Associações INCRA
			if (!Util.isNullOuVazio(this.listAssociacoesIncra)) {
				for (AssociacaoIncra ai : this.listAssociacoesIncra) {
					this.setAssociacaoIncraSelecionada(ai);
					if (ai.isIndExcluido()) {
						if (!Util.isNull(ai.getIdeAssociacaoIncra())) {
							persistirExclusaoAssociacaoIncra();
							continue;
						}
					}
					persistirAssociacaoIncra();
				}
			}

			// Assentado INCRA
			if (!Util.isLazyInitExcepOuNull(this.imovelRural.getAssentadoIncraImovelRuralCollection())) {
				for (AssentadoIncraImovelRural ai : this.imovelRural.getAssentadoIncraImovelRuralCollection()) {
					this.setAssentadoIncraImovelRuralSelecionado(ai);
					if (ai.isIndExcluido()) {
						if (!Util.isNull(ai.getIdeAssentadoIncra())) {
							persistirExclusaoAssentadoIncraImovelRural();
							continue;
						}
					}
					persistirAssentadoIncraImovelRural();
				}
			}
		}

		// Se cadastro do tipo PCT, salva os objetos específicos
		if (getShowPCT()) {
			salvarPCT();
		}

		imovelRuralServiceFacade.atualizarImovelRural(this.imovelRural);
		imovelRuralServiceFacade
				.atualizarListImovelRuralRevalidacao(this.imovelRural.getImovelRuralRevalidacaoCollection());
		carregarImovelRural();
	}

	public void limparPastaTemporariaArquivoShape() throws IOException {
		FileUploadUtil.removerPastasPorPrefixo(DiretorioArquivoEnum.SHAPEFILESTEMP.toString(),
				this.imovelRural.getIdeImovelRural().toString());
	}

	private Boolean isImovelCDAOuBndesStatusCadastrado() {

		// ABA - DOCUMENTACAO
		// proprietario independente de modulos fiscais
		if (tipoVinculoImovel.isProprietario()) {
			// Cartorio
			if (isValidaCartorio() && Util.isNullOuVazio(this.imovelRural.getDesCartorio())) {
				return false;
			}
		}

		// maior que 4 modulos
		if (!this.imovelRural.isMenorQueQuatroModulosFiscais()) {

			// ITR obrigatorio
			if (Util.isNullOuVazio(this.imovelRural.getNumItr())) {
				return false;
			}
		}

		// Posse ou propriedade
		if ((Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse())
				|| Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural())
				|| Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural()
						.getDscCaminhoArquivo()))) {

			return false;
		}

		// Documento Procuração
		if (getPossuiProcurador() && Util.isNullOuVazio(this.imovelRural.getIdeDocumentoProcuracao())) {
			return false;
		}
		// FIM ABA DOCUMENTACAO

		// QUESTIONARIO
		// Area produtiva
		if (Util.isNull(this.imovelRural.getIndAreaProdutiva())) {
			return false;
		}
		// Agrotoxico
		if (Util.isNull(this.imovelRural.getIndAgrotoxico())) {
			return false;
		}
		// Supressao vegetacao
		if (Util.isNull(this.imovelRural.getIndSupressaoVegetacao())) {
			return false;
		}

		// Uso agua

		// Lancamento Residuos Liquidos
		if (Util.isNull(getIndLancamentoResiduosLiquidos())) {
			return false;
		}
		// Intervencao corpo hidrico
		if (Util.isNull(this.imovelRural.getIndIntervencao())) {
			return false;
		}
		// Areas degradasdas fora de APP e RL
		if (Util.isNull(this.imovelRural.getIndOutrosPassivos())) {
			return false;
		}
		// FIM ABA QUESTIONARIO

		// RESPONSAVEL TECNICO
		if (!Util.isNullOuVazio(imovelRural.getResponsavelImovelRuralCollection())
				&& !imovelRural.getResponsavelImovelRuralCollection().isEmpty()
				|| this.imovelRural.isMenorQueQuatroModulosFiscais()) {
			int qtdResponsaveisCadastrados = 0;

			for (ResponsavelImovelRural responsavelImovelRural : imovelRural.getResponsavelImovelRuralCollection()) {
				if (responsavelImovelRural.getIndExcluido()) {
					continue;
				}
				qtdResponsaveisCadastrados++;
				if (!imovelRural.isMenorQueQuatroModulosFiscais()) {
					if (Util.isNullOuVazio(responsavelImovelRural.getIdeDocumentoResponsavel())
							&& !responsavelImovelRural.getIndExcluido()) {
						return false;
					}
				}
				if (Util.isNullOuVazio(responsavelImovelRural.getIdePessoaFisica().getIdeEscolaridade())) {
					return false;
				}
			}
			if (qtdResponsaveisCadastrados < 1 && !this.imovelRural.isMenorQueQuatroModulosFiscais()) {
				return false;
			}
		} else {
			return false;
		}
		// FIM ABA RESPONSAVEL TECNICO

		// Reserva legal
		if (!Util.isNull(this.imovelRural.getReservaLegal())
				&& Util.isNull(this.imovelRural.getReservaLegal().getIdeReservaLegalAverbada())) {
			if (this.imovelRural.getReservaLegal().getIndAverbada()) {
				// Matricula
				if (Util.isNull(this.imovelRural.getReservaLegal().getIdeReservaLegalAverbada().getNumMatricula())) {
					return false;
				}
				// Documento averbacao
				if (Util.isNull(
						this.imovelRural.getReservaLegal().getIdeReservaLegalAverbada().getIdeDocumentoAverbacao())) {
					return false;
				}
			} else if (this.imovelRural.getIndReservaLegal()) {
				// Origem certificado
				if (Util.isNull(this.imovelRural.getReservaLegal().getIdeTipoOrigemCertificado())
						|| Util.isNull(this.imovelRural.getReservaLegal().getIdeTipoOrigemCertificado()
								.getIdeTipoOrigemCertificado())) {
					return false;
				}
				// Numero processo
				if (Util.isNull(this.imovelRural.getReservaLegal().getNumProcesso())) {
					return false;
				}
				// Documento aprovacao
				if (Util.isNull(this.imovelRural.getReservaLegal().getIdeDocumentoAprovacao()) || Util.isNullOuVazio(
						this.imovelRural.getReservaLegal().getIdeDocumentoAprovacao().getDscCaminhoArquivo())) {
					return false;
				}
				// Estado conservacao
				if (Util.isNull(this.imovelRural.getReservaLegal().getIdeTipoEstadoConservacao())) {
					return false;
				}

			}

			// APP
			if (!(imovelRural.isImovelBNDES() || imovelRural.isImovelCDA()) && imovelRural.getIndApp() != null
					&& imovelRural.getIndApp() && !Util.isNullOuVazio(imovelRural.getAppCollection())) {

				for (App app : imovelRural.getAppCollection()) {
					if (Util.isNull(app.getIdeTipoEstadoConservacao())) {
						return false;
					}
				}
			}

			// Area Produtiva
			if (!Util.isNull(this.imovelRural.getIndAreaProdutiva()) && this.imovelRural.getIndAreaProdutiva()) {
				if (Util.isNullOuVazio(this.imovelRural.getAreaProdutivaCollection())) {
					return false;
				}
			}

			// Uso agua
			// Superficial
			if (!Util.isNull(this.imovelRural.getIndSuperficial()) && this.imovelRural.getIndSuperficial()) {
				if (Util.isNullOuVazio(this.imovelRural.getImovelRuralUsoAguaCollection())) {
					return false;
				}
				for (ImovelRuralUsoAgua i : this.imovelRural.getImovelRuralUsoAguaCollection()) {
					if (i.getTipoUso().equals(TipoUsoAgua.SUPERFICIAL.getId().toString())) {
						break;
					}
				}
			}

			// Subterraneo
			if (!Util.isNull(this.imovelRural.getIndSubterraneo()) && this.imovelRural.getIndSubterraneo()) {
				if (Util.isNullOuVazio(this.imovelRural.getImovelRuralUsoAguaCollection())) {
					return false;
				}
				for (ImovelRuralUsoAgua i : this.imovelRural.getImovelRuralUsoAguaCollection()) {
					if (i.getTipoUso().equals(TipoUsoAgua.SUBTERRANEO.getId().toString())) {
						break;
					}
				}
			}

			// Manancial
			if (!Util.isNull(this.imovelRural.getIndLancamentoManancial())
					&& this.imovelRural.getIndLancamentoManancial()) {
				if (Util.isNullOuVazio(this.imovelRural.getImovelRuralUsoAguaCollection())) {
					return false;
				}
				for (ImovelRuralUsoAgua i : this.imovelRural.getImovelRuralUsoAguaCollection()) {
					if (i.getTipoUso().equals(TipoUsoAgua.LANCAMENTO.getId().toString())) {
						break;
					}
				}
			}
			// Intervencao
			if (!Util.isNull(this.imovelRural.getIndIntervencao()) && this.imovelRural.getIndIntervencao()) {
				if (Util.isNullOuVazio(this.imovelRural.getImovelRuralUsoAguaCollection())) {
					return false;
				}
				for (ImovelRuralUsoAgua i : this.imovelRural.getImovelRuralUsoAguaCollection()) {
					if (i.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
						break;
					}
				}
			}

			// RPPN
			if (!Util.isNull(this.imovelRural.getIndRppn()) && this.imovelRural.getIndRppn()) {
				if (Util.isNullOuVazio(this.imovelRural.getIdeImovelRuralRppn().getNomRppn())) {
					return false;
				}
				if (!getExisteTheGeomRppn()) {
					return false;
				}
			}

		}

		return true;

	}

	// Finalizacao fim

	// Início Cadastro Vegetação Nativa

	public void salvarVegetacaoNativa() {
		try {
			if (!Util.isNull(imovelRural.getVegetacaoNativa())
					&& !Util.isNull(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica())) {
				if (imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
						.isShapeFile()
						&& !habilitaSalvarShape(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica())
						&& !getExisteTheGeomVn()) {
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_arquivos_shape_prj"));
					return;
				} else if (imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
						.isDesenho()
						&& !temShapeTemporario(
								imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.VEGETACAO_NATIVA.getId())
						&& !getExisteTheGeomVn()) {
					JsfUtil.addErrorMessage(
							Util.getString("msg_generica_preenchimento_obrigatorio_localizacao_geografica_poligonal"));
					return;
				}
			}

			imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
					.setIdeClassificacaoSecao(selecionarSecaoGeometrica(
							imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()));
			imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
					.setIdeSistemaCoordenada(selecionarSistemaCoordenada(
							imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()));

			if (imovelRural.getVegetacaoNativa().getTipoFinalidadeVegetacaoNativaCollection().size() == 0) {
				throw new Exception(
						"Deve ser informado o que você deseja fazer com a área excedente de vegetação nativa!");
			}

			if (Util.isNullOuVazio(imovelRural.getVegetacaoNativa().getIdeVegetacaoNativa())) {

				imovelRural.getVegetacaoNativa().setIdeImovelRural(this.imovelRural);

				// Salvando dados da localização
				imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setDtcCriacao(new Date());
				imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setDtcExclusao(null);
				imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setIndExcluido(Boolean.FALSE);

				if (temShapeTemporario(
						imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.VEGETACAO_NATIVA.getId())) {
					persistirShapesVn();
				}

				if (!imovelRural.getIsFinalizado()) {
					persistirVegetacaoNativa();
				}
				JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
			} else {
				if (temShapeTemporario(
						imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.VEGETACAO_NATIVA.getId())) {
					persistirShapesVn();
				}

				if (!imovelRural.getIsFinalizado()) {
					persistirVegetacaoNativa();
				}
				JsfUtil.addSuccessMessage("Alteração efetuada com sucesso.");
			}

			imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setListArquivosShape(null);
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private TipoFinalidadeVegetacaoNativa selecionarTipoFinalidadeVegetacaoNativa(
			TipoFinalidadeVegetacaoNativa tipoFinalidade) {
		Collection<TipoFinalidadeVegetacaoNativa> listTipoFinalidadeVegetacaoNativa = new ArrayList<TipoFinalidadeVegetacaoNativa>();
		try {
			listTipoFinalidadeVegetacaoNativa = imovelRuralServiceFacade.listarTipoFinalidadeVegetacaoNativa();
			for (TipoFinalidadeVegetacaoNativa tipo : listTipoFinalidadeVegetacaoNativa) {
				if (tipo.getIdeTipoFinalidadeVegetacaoNativa()
						.equals(tipoFinalidade.getIdeTipoFinalidadeVegetacaoNativa())) {
					tipoFinalidade = tipo;
					break;
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}

		return tipoFinalidade;
	}
	
	public void excluirVn() throws EJBTransactionRolledbackException, Exception {
		if (Util.isNullOuVazio(imovelRural.getIndVegetacaoNativa()) || !imovelRural.getIndVegetacaoNativa()) {
			if (!Util.isNullOuVazio(imovelRural.getVegetacaoNativa())
					&& !Util.isNullOuVazio(imovelRural.getVegetacaoNativa().getIdeVegetacaoNativa())) {
				LocalizacaoGeografica locVn = imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica();
				imovelRuralServiceFacade.excluirVegetacaoNativa(imovelRural.getVegetacaoNativa());
				imovelRuralServiceFacade.excluirLocalizacaoGeografica(locVn);
			}
			imovelRural.setVegetacaoNativa(null);
		}
	}

	public void salvarAreaRuralConsolidada() {
		try {
			if (!Util.isNull(imovelRural.getIdeAreaRuralConsolidada())
					&& !Util.isNull(imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica())) {
				if (imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
						.isShapeFile()
						&& !habilitaSalvarShape(imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica())
						&& !getExisteTheGeomArc()) {
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_arquivos_shape_prj"));
					return;
				} else if (imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
						.getIdeClassificacaoSecao().isDesenho()
						&& !temShapeTemporario(
								imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.AREA_RURAL_CONSOLIDADA.getId())
						&& !getExisteTheGeomArc()) {
					JsfUtil.addErrorMessage(
							Util.getString("msg_generica_preenchimento_obrigatorio_localizacao_geografica_poligonal"));
					return;
				}
			}

			imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
					.setIdeClassificacaoSecao(selecionarSecaoGeometrica(imovelRural.getIdeAreaRuralConsolidada()
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()));
			imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
					.setIdeSistemaCoordenada(selecionarSistemaCoordenada(imovelRural.getIdeAreaRuralConsolidada()
							.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()));

			if (Util.isNullOuVazio(imovelRural.getIdeAreaRuralConsolidada().getIdeAreaRuralConsolidada())) {

				imovelRural.getIdeAreaRuralConsolidada().setIdeImovelRural(this.imovelRural);

				// Salvando dados da localização
				imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().setDtcCriacao(new Date());
				imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().setDtcExclusao(null);
				imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().setIndExcluido(Boolean.FALSE);

				if (temShapeTemporario(
						imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.AREA_RURAL_CONSOLIDADA.getId())) {
					persistirShapesArc();
				}

				if (!imovelRural.getIsFinalizado()) {
					persistirAreaRuralConsolidada();
				}
				JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
			} else {
				if (temShapeTemporario(
						imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.AREA_RURAL_CONSOLIDADA.getId())) {
					persistirShapesArc();
				}

				if (!imovelRural.getIsFinalizado()) {
					persistirAreaRuralConsolidada();
				}
				JsfUtil.addSuccessMessage("Alteração efetuada com sucesso.");
			}

			imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().setListArquivosShape(null);
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, exception);
		}
	}

	private void persistirVegetacaoNativa() throws Exception {
		imovelRural.getVegetacaoNativa().getVegetacaoNativaFinalidadeCollection().clear();

		for (TipoFinalidadeVegetacaoNativa tipoFinalidadeVegetacaoNativa : imovelRural.getVegetacaoNativa()
				.getTipoFinalidadeVegetacaoNativaCollection()) {
			VegetacaoNativaFinalidade vegetacaoNativaFinalidade = new VegetacaoNativaFinalidade();
			tipoFinalidadeVegetacaoNativa = selecionarTipoFinalidadeVegetacaoNativa(tipoFinalidadeVegetacaoNativa);
			vegetacaoNativaFinalidade.setIdeTipoFinalidadeVegetacaoNativa(tipoFinalidadeVegetacaoNativa);
			vegetacaoNativaFinalidade.setIdeVegetacaoNativa(imovelRural.getVegetacaoNativa());
			imovelRural.getVegetacaoNativa().getVegetacaoNativaFinalidadeCollection().add(vegetacaoNativaFinalidade);
		}
		if (imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getLocalizacaoExcluida()) {
			if (imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() != null) {
				imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(
						imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(null);
			}
			imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setLocalizacaoExcluida(false);
		}
		if (Util.isNullOuVazio(imovelRural.getVegetacaoNativa().getIdeVegetacaoNativa())) {
			imovelRural.getVegetacaoNativa().setIdeImovelRural(this.imovelRural);
			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
					imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica());
			imovelRuralServiceFacade.salvarVegetacaoNativa(imovelRural.getVegetacaoNativa());
			auditoria.salvar(imovelRural.getVegetacaoNativa());
		} else {
			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
					imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica());
			VegetacaoNativa lVegetacaoNativa = imovelRuralServiceFacade
					.carregarTudoVegetacaoNativa(imovelRural.getVegetacaoNativa());
			imovelRuralServiceFacade.atualizarVegetacaoNativa(imovelRural.getVegetacaoNativa());
			auditoria.atualizar(lVegetacaoNativa, imovelRural.getVegetacaoNativa());
		}

		if (!Util.isNull(
				this.imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados())
				&& this.imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
						.getNovosArquivosShapeImportados()) {
			FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(
					this.imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica(), "5",
					this.imovelRural.getIdeImovelRural().toString());
			imovelRuralServiceFacade
					.persistirShapes(this.imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica(), null);
			this.imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
		}
	}

	private void persistirAreaRuralConsolidada() throws Exception {
		if (imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getLocalizacaoExcluida()) {
			if (imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
					.getIdeLocalizacaoGeografica() != null) {
				imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(imovelRural.getIdeAreaRuralConsolidada()
						.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
						.setDadoGeograficoCollection(null);
			}
			imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().setLocalizacaoExcluida(false);
		}
		if (Util.isNullOuVazio(imovelRural.getIdeAreaRuralConsolidada().getIdeAreaRuralConsolidada())) {
			imovelRural.getIdeAreaRuralConsolidada().setIdeImovelRural(this.imovelRural);
			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
					imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica());
			imovelRuralServiceFacade.salvarAreaConsolidada(imovelRural.getIdeAreaRuralConsolidada());
			auditoria.salvar(imovelRural.getIdeAreaRuralConsolidada());
		} else {
			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
					imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica());
			AreaRuralConsolidada lAreaRuralConsolidada = imovelRuralServiceFacade
					.carregarTudoAreaConsolidada(imovelRural.getIdeAreaRuralConsolidada());
			imovelRuralServiceFacade.atualizarAreaConsolidada(imovelRural.getIdeAreaRuralConsolidada());
			auditoria.atualizar(lAreaRuralConsolidada, imovelRural.getIdeAreaRuralConsolidada());
		}

		if (!Util.isNull(this.imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
				.getNovosArquivosShapeImportados())
				&& this.imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
						.getNovosArquivosShapeImportados()) {
			FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(
					this.imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica(), "14",
					this.imovelRural.getIdeImovelRural().toString());
			imovelRuralServiceFacade
					.persistirShapes(this.imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica(), null);
			this.imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
					.setNovosArquivosShapeImportados(false);
		}
	}

	public boolean getExisteTheGeomVn() {
		try {
			if (!Util.isNullOuVazio(imovelRural.getVegetacaoNativa())
					&& !Util.isNullOuVazio(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica())) {
				if (imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					return true;
				} else if (!imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getLocalizacaoExcluida())
					return imovelRuralServiceFacade.verificaSeExisteTheGeomValido(imovelRural.getVegetacaoNativa()
							.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			} else if (!Util.isNullOuVazio(imovelRural.getVegetacaoNativa())
					&& imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica() != null && imovelRural
							.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() == null) {
				if (imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados())
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return false;
		}
		return false;
	}

	public boolean getExisteTheGeomArc() {
		try {
			if (!Util.isNullOuVazio(imovelRural.getIdeAreaRuralConsolidada())
					&& !Util.isNullOuVazio(imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica())) {
				if (imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
						.getNovosArquivosShapeImportados()) {
					return true;
				} else if (!imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
						.getLocalizacaoExcluida())
					return imovelRuralServiceFacade.verificaSeExisteTheGeomValido(imovelRural
							.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			} else if (!Util.isNullOuVazio(imovelRural.getIdeAreaRuralConsolidada())
					&& imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica() != null
					&& imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
							.getIdeLocalizacaoGeografica() == null) {
				if (imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
						.getNovosArquivosShapeImportados())
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return false;
		}
		return false;
	}

	public void importarShapeVn(FileUploadEvent event) {
		String caminhoArquivo;

		try {
			if (Util.isNullOuVazio(
					imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape())) {
				imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
						.setListArquivosShape(new ArrayList<String>());
			}

			if (imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape().size() > 3) {
				JsfUtil.addWarnMessage("Não é possível enviar mais de 4 arquivos.");
			} else {
				caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
						+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.VEGETACAO_NATIVA.getId(),
						"shapeVegetacaoNativa");

				if (imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape()
						.contains(FileUploadUtil.getFileName(caminhoArquivo))) {
					JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
				} else {
					imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape()
							.add(FileUploadUtil.getFileName(caminhoArquivo));

					if (caminhoArquivo.contains(".prj")) {
						imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
								.setIdeSistemaCoordenada(imovelRuralServiceFacade.obterSistCoordPRJ(caminhoArquivo));

						if (Util.isNull(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
								.getIdeSistemaCoordenada())) {
							JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_srid_prj"));
							FileUploadUtil.removerArquivos(new File(caminhoArquivo));
							imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape()
									.remove(FileUploadUtil.getFileName(caminhoArquivo));
							return;
						}
					}
					if (imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape()
							.size() == 4) {
						RequestContext.getCurrentInstance().execute("dlgUploadShapeVn.hide()");
					}
					JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("geral_msg_erro_envio_arquivo"));
		}
	}

	public void importarShapeArc(FileUploadEvent event) {
		String caminhoArquivo;
		try {
			if (Util.isNullOuVazio(
					imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getListArquivosShape())) {
				imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
						.setListArquivosShape(new ArrayList<String>());
			}

			if (imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getListArquivosShape()
					.size() > 3) {
				JsfUtil.addWarnMessage("Não é possível enviar mais de 4 arquivos.");
			} else {

				caminhoArquivo = FileUploadUtil.EnviarShape(event,
						DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural().toString()
								+ "_" + TemaGeoseiaEnum.AREA_RURAL_CONSOLIDADA.getId(),
						"shapeArc");

				if (imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getListArquivosShape()
						.contains(FileUploadUtil.getFileName(caminhoArquivo))) {
					JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
				} else {
					imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getListArquivosShape()
							.add(FileUploadUtil.getFileName(caminhoArquivo));

					if (caminhoArquivo.contains(".prj")) {

						imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
								.setIdeSistemaCoordenada(imovelRuralServiceFacade.obterSistCoordPRJ(caminhoArquivo));

						if (Util.isNull(imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
								.getIdeSistemaCoordenada())) {
							JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_srid_prj"));
							FileUploadUtil.removerArquivos(new File(caminhoArquivo));
							imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
									.getListArquivosShape().remove(FileUploadUtil.getFileName(caminhoArquivo));
							return;
						}
					}
					if (imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getListArquivosShape()
							.size() == 4) {
						RequestContext.getCurrentInstance().execute("dlgUploadShapeArc.hide()");
					}
					JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("geral_msg_erro_envio_arquivo"));
		}
	}

	public void persistirShapesVn() throws Exception {
		String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
				+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.VEGETACAO_NATIVA.getId()
				+ FileUploadUtil.getFileSeparator();
		String nome = "shapeVegetacaoNativa";
		String nomeNovo = imovelRural.getIdeImovelRural().toString() + "_"
				+ imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid();

		try {
			imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(
					retornaClassificacaoSecaoGeometricaSelecionado(imovelRural.getVegetacaoNativa()
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
					.setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(imovelRural.getVegetacaoNativa()
							.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));

			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nome, nomeNovo);

			String[] retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					imovelRural.getIdeImovelRural(),
					imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.VEGETACAO_NATIVA.getId(),
					imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(),
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
							.getCoordGeobahiaMunicipio());

			if (retorno == null || retorno.length < 2) {
				throw new Exception("Erro ao persistir Localização Geográfica da Vegetação Nativa");
			} else if (retorno[0].equalsIgnoreCase("ERRO")) {
				throw new Exception(retorno[2] + " [" + retorno[1] + "]");
			}

			String geometriaVn = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
					imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.VEGETACAO_NATIVA.getId(), null);

			if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaVn)) {
				throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
			}

			imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
			imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()
					.setListArquivosShape(new ArrayList<String>());
		} catch (Exception e) {
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nomeNovo, nome);
			throw new Exception(e.getMessage());
		}
	}

	public void persistirShapesArc() throws Exception {
		String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
				+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.AREA_RURAL_CONSOLIDADA.getId()
				+ FileUploadUtil.getFileSeparator();
		String nome = "shapeArc";
		String nomeNovo = imovelRural.getIdeImovelRural().toString() + "_" + imovelRural.getIdeAreaRuralConsolidada()
				.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid();

		try {
			imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(
					retornaClassificacaoSecaoGeometricaSelecionado(imovelRural.getIdeAreaRuralConsolidada()
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
					.setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(imovelRural.getIdeAreaRuralConsolidada()
							.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));

			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nome, nomeNovo);

			String[] retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					imovelRural.getIdeImovelRural(),
					imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.AREA_RURAL_CONSOLIDADA.getId(),
					imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()
							.getSrid(),
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
							.getCoordGeobahiaMunicipio());

			if (retorno == null || retorno.length < 2) {
				throw new Exception("Erro ao persistir Localização Geográfica da Área Rural Consolidada");
			} else if (retorno[0].equalsIgnoreCase("ERRO")) {
				throw new Exception(retorno[2] + " [" + retorno[1] + "]");
			}

			String geometriaArc = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
					imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.AREA_RURAL_CONSOLIDADA.getId(), null);

			if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaArc)) {
				throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
			}

			imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
					.setNovosArquivosShapeImportados(true);
			imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica()
					.setListArquivosShape(new ArrayList<String>());
		} catch (Exception e) {
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nomeNovo, nome);
			throw new Exception(e.getMessage());
		}
	}

	public Boolean getMostrarBotaoUploadShapeVn() {
		if (Util.isNullOuVazio(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape())
				|| imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape().size() < 4) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getMostrarBotaoUploadShapeArc() {
		if (Util.isNullOuVazio(
				imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getListArquivosShape())
				|| imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getListArquivosShape()
						.size() < 4) {
			return true;
		} else {
			return false;
		}
	}

	public Collection<TipoFinalidadeVegetacaoNativa> getListTipoFinalidadeVegetacaoNativa() {
		Collection<TipoFinalidadeVegetacaoNativa> listTipoFinalidadeVegetacaoNativa = new ArrayList<TipoFinalidadeVegetacaoNativa>();
		try {
			listTipoFinalidadeVegetacaoNativa = imovelRuralServiceFacade.listarTipoFinalidadeVegetacaoNativa();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}

		return listTipoFinalidadeVegetacaoNativa;
	}

	public void salvarLocalizacaoGeograficaImovelRural() {
		try {
			if (getShowPCT()) {
				if (!Util.isNull(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio())
						&& imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeClassificacaoSecao()
								.isShapeFile()
						&& !habilitaSalvarShape(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio())
						&& !getExisteTheGeomImovelRuralPct()) {
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_arquivos_shape_prj"));
					return;
				}

				if (!Util.isNull(imovelRural.getIdeLocalizacaoGeografica())
						&& imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isShapeFile()
						&& !habilitaSalvarShape(imovelRural.getIdeLocalizacaoGeografica())
						&& !getExisteTheGeomImovelRural()) {
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_arquivos_shape_prj"));
					return;
				}

				if (!Util.isNull(imovelRural.getIdeLocalizacaoGeograficaPct())
						&& Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaPct().getIdeSistemaCoordenada())) {
					JsfUtil.addErrorMessage(
							Util.getString("msg_generica_preenchimento_obrigatorio_localizacao_geografica_inf_32"));
					return;
				}

				if (Util.isNullOuVazio(tipoUpload)) {
					MensagemUtil.alerta("Todos os campos sao de preechimento obrigatório");
					return;
				}
			} else {
				if (!Util.isNull(imovelRural.getIdeLocalizacaoGeografica())) {
					if (imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isShapeFile()
							&& !habilitaSalvarShape(imovelRural.getIdeLocalizacaoGeografica())
							&& !getExisteTheGeomImovelRural()) {
						JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_arquivos_shape_prj"));
						return;
					} else if (imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isDesenho()
							&& !temShapeTemporario(
									imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId())
							&& !getExisteTheGeomImovelRural()) {
						JsfUtil.addErrorMessage(Util
								.getString("msg_generica_preenchimento_obrigatorio_localizacao_geografica_poligonal"));
						return;
					}
				}
			}

			String mensagemSucesso = "";

			if (getShowPCT()) {
				mensagemSucesso = salvarLocalizacaoGeograficaPctLimiteTerritorio();
			}

			if (imovelRural.getReservaLegal() != null && imovelRural.getReservaLegal().getIdeReservaLegal() == null) {
				imovelRural.setReservaLegal(null);
			}

			mensagemSucesso = salvarLocalizacaoGeografica();

			if (imovelRural.isImovelINCRA() && temShapeTemporarioLotes()) {
				if (imovelRural != null && imovelRural.getIdeLocalizacaoGeografica() != null
						&& imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada() != null) {
					imovelRural.getIdeLocalizacaoGeograficaLote().setIdeSistemaCoordenada(
							imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada());
				}

				if (imovelRural != null && imovelRural.getIdeLocalizacaoGeografica() != null
						&& imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao() != null) {
					imovelRural.getIdeLocalizacaoGeograficaLote().setIdeClassificacaoSecao(
							imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao());
				}

				if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote())) {
					persistirShapesLotes();
				} else {
					imovelRural.getIdeLocalizacaoGeograficaLote().setDtcCriacao(new Date());
					imovelRural.getIdeLocalizacaoGeograficaLote().setDtcExclusao(null);
					imovelRural.getIdeLocalizacaoGeograficaLote().setIndExcluido(Boolean.FALSE);

					persistirShapesLotes();
				}
			}

			if (!imovelRural.getIsFinalizado()) {
				persistirLocalizacaoGeograficaImovelRural();
			}

			if (getShowPCT() && !imovelRural.getIsFinalizado()) {
				String theGeomLimiteTerritorio = validacaoGeoSeiaService.buscarGeometriaShape(
						imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica());
				String theGeomLimiteComunidade = validacaoGeoSeiaService
						.buscarGeometriaShape(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());

				if (Util.isNullOuVazio(theGeomLimiteTerritorio)) {
					throw new Exception(
							"Erro ao validar Localização Geográfica. A geometria do Limite do Território não existe.");
				}

				if (Util.isNullOuVazio(theGeomLimiteComunidade)) {
					throw new Exception(
							"Erro ao validar Localização Geográfica. A geometria do Limite da Comunidade não existe.");
				}

				if (!validacaoGeoSeiaService.verificaSobreposicaoShapeUsandoContainsTheGeom(theGeomLimiteTerritorio,
						theGeomLimiteComunidade)) {
					MensagemUtil.alerta(
							"A poligonal da comunidade inserida está fora dos limites do território informado.");
					RequestContext.getCurrentInstance().execute("desabilitarBotaoProximo();");
					return;
				}

				if (!validacaoGeoSeiaService.verificaPontoNoShapeUsandoContains(
						imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio(),
						imovelRural.getIdeLocalizacaoGeograficaPct())) {
					MensagemUtil.alerta("A coordenada do ponto/sede deve estar dentro do território/comunidade.");
					RequestContext.getCurrentInstance().execute("desabilitarBotaoProximo();");
					excluirLocalizacacaoPct(ideLocalizacaoGeograficaPctOld);
					return;
				}

				if ((!Util.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeografica())
						&& !Util.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeograficaPct())
						&& !Util.isNullOuVazio(this.ideLocalizacaoGeograficaPctOld))) {
					excluirLocalizacacaoPct(ideLocalizacaoGeograficaPctOld);
				}
			} else if (getShowPCT() && imovelRural.getIsFinalizado()) {
				String theGeomLimiteTerritorio = null;
				String theGeomLimiteComunidade = null;

				if (imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getNovosArquivosShapeImportados()) {
					theGeomLimiteTerritorio = imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getTheGeom();
				} else {
					theGeomLimiteTerritorio = validacaoGeoSeiaService.buscarGeometriaShape(
							imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica());
				}

				if (imovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					theGeomLimiteComunidade = imovelRural.getIdeLocalizacaoGeografica().getTheGeom();
				} else {
					theGeomLimiteComunidade = validacaoGeoSeiaService.buscarGeometriaShape(
							imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}

				if (Util.isNullOuVazio(theGeomLimiteTerritorio)) {
					throw new Exception(
							"Erro ao validar Localização Geográfica. A geometria do Limite do Território não existe.");
				}

				if (Util.isNullOuVazio(theGeomLimiteComunidade)) {
					throw new Exception(
							"Erro ao validar Localização Geográfica. A geometria do Limite da Comunidade não existe.");
				}

				if (!validacaoGeoSeiaService.verificaSobreposicaoShapeUsandoContainsTheGeom(theGeomLimiteTerritorio,
						theGeomLimiteComunidade)) {
					MensagemUtil.alerta(
							"A poligonal da comunidade inserida está fora dos limites do território informado.");
					RequestContext.getCurrentInstance().execute("desabilitarBotaoProximo();");
					return;
				}

				if (!validacaoGeoSeiaService.verificaPontoNoShapeUsandoContainsTheGeom(theGeomLimiteTerritorio,
						imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio(),
						imovelRural.getIdeLocalizacaoGeograficaPct())) {
					MensagemUtil.alerta("A coordenada do ponto/sede deve estar dentro do território/comunidade.");
					RequestContext.getCurrentInstance().execute("desabilitarBotaoProximo();");
					excluirLocalizacacaoPct(ideLocalizacaoGeograficaPctOld);
					return;
				}

				if ((!Util.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeografica())
						&& !Util.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeograficaPct())
						&& !Util.isNullOuVazio(this.ideLocalizacaoGeograficaPctOld))) {
					excluirLocalizacacaoPct(ideLocalizacaoGeograficaPctOld);
				}
			}

			tipoUpload = null;
			RequestContext.getCurrentInstance().execute("habilitarBotaoProximo();");
			JsfUtil.addSuccessMessage(mensagemSucesso);

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);

			Html.executarJS("desabilitarBotaoProximoMesmoFinalizado();");

			if (!e.getMessage().equals("Imóvel duplicado."))
				JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private String salvarLocalizacaoGeograficaPctLimiteTerritorio() throws Exception {
		String mensagemSucesso = "";

		if (imovelRural != null && imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio() != null
				&& imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeSistemaCoordenada() != null) {

			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setIdeSistemaCoordenada(
					retornaSistemaCordenadaSelecionado(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
							.getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
		}

		if (imovelRural != null && imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio() != null
				&& imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeClassificacaoSecao() != null) {

			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(
							imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeClassificacaoSecao()
									.getIdeClassificacaoSecao()));
		}

		if (!Util.isNullOuVazio(
				imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica())) {
			if (temShapeTemporario(
					imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId())) {
				persistirShapesImovelRuralPctLimiteTerritorio();
			}
			mensagemSucesso = "Alteração efetuada com sucesso.";
		} else {
			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setDtcCriacao(new Date());
			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setDtcExclusao(null);
			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setIndExcluido(Boolean.FALSE);

			if (temShapeTemporario(
					imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId())) {
				persistirShapesImovelRuralPctLimiteTerritorio();
			}
			mensagemSucesso = "Inclusão efetuada com sucesso.";
		}
		return mensagemSucesso;
	}

	private String salvarLocalizacaoGeografica() throws Exception {

		String mensagemSucesso = "";

		if (imovelRural != null && imovelRural.getIdeLocalizacaoGeografica() != null
				&& imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada() != null) {
			imovelRural.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(
					imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
		}

		if (imovelRural != null && imovelRural.getIdeLocalizacaoGeografica() != null
				&& imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao() != null) {
			imovelRural.getIdeLocalizacaoGeografica()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(imovelRural
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));
		}

		if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())) {
			if (temShapeTemporario(
					imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId())) {
				persistirShapesImovelRural();
			}
			mensagemSucesso = "Alteração efetuada com sucesso.";
		} else {
			imovelRural.getIdeLocalizacaoGeografica().setDtcCriacao(new Date());
			imovelRural.getIdeLocalizacaoGeografica().setDtcExclusao(null);
			imovelRural.getIdeLocalizacaoGeografica().setIndExcluido(Boolean.FALSE);

			if (temShapeTemporario(
					imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId())) {
				persistirShapesImovelRural();
			}
			mensagemSucesso = "Inclusão efetuada com sucesso.";
		}

		return mensagemSucesso;
	}

	private void persistirLocalizacaoGeograficaImovelRural() throws Exception {
		if (imovelRural.getIdeLocalizacaoGeografica().getLocalizacaoExcluida()) {

			if (imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() != null) {
				imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(
						imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				imovelRural.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(null);
			}

			imovelRural.getIdeLocalizacaoGeografica().setLocalizacaoExcluida(false);
		}

		if (getShowPCT()) {
			if (imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getLocalizacaoExcluida()) {
				if (imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
						.getIdeLocalizacaoGeografica() != null) {
					imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(
							imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica());
					imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setDadoGeograficoCollection(null);
				}
				imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setLocalizacaoExcluida(false);
			}
		}

		if (imovelRural.isImovelINCRA() && imovelRural.getIdeLocalizacaoGeograficaLote() != null
				&& imovelRural.getIdeLocalizacaoGeograficaLote().getLocalizacaoExcluida()) {

			if (imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica() != null) {
				imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(
						imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica());
				imovelRural.getIdeLocalizacaoGeograficaLote().setDadoGeograficoCollection(null);
			}

			imovelRural.getIdeLocalizacaoGeograficaLote().setLocalizacaoExcluida(false);
		}

		if (imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() == null) {
			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(imovelRural.getIdeLocalizacaoGeografica());
			auditoria.salvar(imovelRural.getIdeLocalizacaoGeografica());

		} else {
			LocalizacaoGeografica objAntigo = imovelRuralServiceFacade.carregarLocalizacaoGeografica(
					imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(imovelRural.getIdeLocalizacaoGeografica());
			auditoria.atualizar(objAntigo, imovelRural.getIdeLocalizacaoGeografica());
		}

		if (getShowPCT()) {
			if (imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica() == null) {
				imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
						imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio());
				auditoria.salvar(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio());
			} else {
				LocalizacaoGeografica objAntigo = imovelRuralServiceFacade.carregarLocalizacaoGeografica(
						imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica());
				imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
						imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio());
				auditoria.atualizar(objAntigo, imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio());
			}
		}

		if (imovelRural.isImovelINCRA() && imovelRural.getIdeLocalizacaoGeograficaLote() != null
				&& imovelRural.getIdeLocalizacaoGeograficaLote().getNovosArquivosShapeImportados()) {

			if (imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica() == null) {
				imovelRuralServiceFacade
						.salvarOuAtualizarLocalizacaoGeografica(imovelRural.getIdeLocalizacaoGeograficaLote());
				auditoria.salvar(imovelRural.getIdeLocalizacaoGeograficaLote());

			} else {
				LocalizacaoGeografica objAntigo = imovelRuralServiceFacade.carregarLocalizacaoGeografica(
						imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica());
				imovelRuralServiceFacade
						.salvarOuAtualizarLocalizacaoGeografica(imovelRural.getIdeLocalizacaoGeograficaLote());
				auditoria.atualizar(objAntigo, imovelRural.getIdeLocalizacaoGeograficaLote());
			}
		}

		if (Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote())) {
			imovelRural.setIdeLocalizacaoGeograficaLote(null);
		}

		imovelRuralServiceFacade.atualizarImovelRural(imovelRural);

		if (imovelRural.isImovelINCRA() && Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote())) {
			imovelRural.setIdeLocalizacaoGeograficaLote(new LocalizacaoGeografica());
		}

		if (!Util.isNull(imovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados())
				&& imovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {

			FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(imovelRural.getIdeLocalizacaoGeografica(),
					"1", imovelRural.getIdeImovelRural().toString());

			imovelRuralServiceFacade.persistirShapes(imovelRural.getIdeLocalizacaoGeografica(), imovelRural.getImovel()
					.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());

			imovelRural.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
		}

		if (getShowPCT()
				&& !Util.isNull(this.imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
						.getNovosArquivosShapeImportados())
				&& this.imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
						.getNovosArquivosShapeImportados()) {
			FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(
					this.imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio(),
					TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId().toString(),
					this.imovelRural.getIdeImovelRural().toString());
			imovelRuralServiceFacade.persistirShapes(this.imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio(),
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
							.getCoordGeobahiaMunicipio());
			this.imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setNovosArquivosShapeImportados(false);
		}

		if (imovelRural.isImovelINCRA() && imovelRural.getIdeLocalizacaoGeograficaLote() != null
				&& !Util.isNull(imovelRural.getIdeLocalizacaoGeograficaLote().getNovosArquivosShapeImportados())
				&& imovelRural.getIdeLocalizacaoGeograficaLote().getNovosArquivosShapeImportados()) {

			moverArquivoShapeLocalizacaoLotesPastaTemporariaParaPastaOriginal();
			imovelRuralServiceFacade.persistirShapes(imovelRural.getIdeLocalizacaoGeograficaLote(), null);
			imovelRural.getIdeLocalizacaoGeograficaLote().setNovosArquivosShapeImportados(false);
		}
	}

	protected SistemaCoordenada retornaSistemaCordenadaSelecionado(Integer ideSistemaCordenada) {
		if (Util.isNullOuVazio(listaDatums)) {
			carregarDatums();
		}

		for (SistemaCoordenada sistemaCoordenada : listaDatums) {
			if (sistemaCoordenada.getIdeSistemaCoordenada().equals(ideSistemaCordenada))
				return sistemaCoordenada;
		}

		return null;
	}

	protected ClassificacaoSecaoGeometrica retornaClassificacaoSecaoGeometricaSelecionado(
			Integer ideClassificacaoSecaoGeometrica) {
		carregarSecaoGeometrica();
		for (ClassificacaoSecaoGeometrica classificacaoSecaoGeometrica : listaSecaoGeometrica) {
			if (classificacaoSecaoGeometrica.getIdeClassificacaoSecao().equals(ideClassificacaoSecaoGeometrica))
				return classificacaoSecaoGeometrica;
		}
		return null;
	}

	public boolean getExisteTheGeomImovelRural() {
		try {
			if (!Util.isNullOuVazio(imovelRural) && imovelRural.getIdeLocalizacaoGeografica() != null) {
				return imovelRuralServiceFacade.existeTheGeom(imovelRural.getIdeLocalizacaoGeografica());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return false;
		}
		return false;
	}

	public boolean getExisteTheGeomImovelRuralPct() {
		try {
			if (!Util.isNullOuVazio(imovelRural)
					&& imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio() != null) {
				return imovelRuralServiceFacade
						.existeTheGeom(this.imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public void importarShapeImovelRural(FileUploadEvent event) {
		try {
			if (getShowPCT() && TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId().equals(tipoUpload)) {
				uploadArquivoPct(event);
			} else {
				uploadArquivo(event);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JsfUtil.addErrorMessage(Util.getString("geral_msg_erro_envio_arquivo"));
		}
	}

	private void uploadArquivo(FileUploadEvent event) throws Exception {
		String caminhoArquivo;
		if (Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getListArquivosShape()))
			imovelRural.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());

		if (imovelRural.getIdeLocalizacaoGeografica().getListArquivosShape().size() > 3) {
			JsfUtil.addWarnMessage("Não é possível enviar mais de 4 arquivos.");
		} else {
			caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
					+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(),
					"shapeImovelRural");

			if (imovelRural.getIdeLocalizacaoGeografica().getListArquivosShape()
					.contains(FileUploadUtil.getFileName(caminhoArquivo))) {
				JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
			} else {
				imovelRural.getIdeLocalizacaoGeografica().getListArquivosShape()
						.add(FileUploadUtil.getFileName(caminhoArquivo));

				if (caminhoArquivo.contains(".prj")) {

					imovelRural.getIdeLocalizacaoGeografica()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.obterSistCoordPRJ(caminhoArquivo));

					if (Util.isNull(imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
						JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_srid_prj"));
						FileUploadUtil.removerArquivos(new File(caminhoArquivo));
						imovelRural.getIdeLocalizacaoGeografica().getListArquivosShape()
								.remove(FileUploadUtil.getFileName(caminhoArquivo));
						return;
					}
				}

				if (imovelRural.getIdeLocalizacaoGeografica().getListArquivosShape().size() == 4) {
					RequestContext.getCurrentInstance().execute("dlgUploadShapeImovelRural.hide()");
				}
				JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
			}
		}
	}

	private void uploadArquivoPct(FileUploadEvent event) throws Exception {
		String caminhoArquivo;
		if (Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getListArquivosShape())) {
			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setListArquivosShape(new ArrayList<String>());
		}
		if (imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getListArquivosShape().size() > 3) {
			JsfUtil.addWarnMessage("Não é possível enviar mais de 4 arquivos.");
		} else {
			caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
					+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId(),
					"shapeImovelRuralPct");

			if (imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getListArquivosShape()
					.contains(FileUploadUtil.getFileName(caminhoArquivo))) {
				JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
			} else {
				imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getListArquivosShape()
						.add(FileUploadUtil.getFileName(caminhoArquivo));

				if (caminhoArquivo.contains(".prj")) {

					imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
							.setIdeSistemaCoordenada(imovelRuralServiceFacade.obterSistCoordPRJ(caminhoArquivo));

					if (Util.isNull(
							imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeSistemaCoordenada())) {
						JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_srid_prj"));
						FileUploadUtil.removerArquivos(new File(caminhoArquivo));
						imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getListArquivosShape()
								.remove(FileUploadUtil.getFileName(caminhoArquivo));
						return;
					}
				}

				if (imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getListArquivosShape().size() == 4) {
					RequestContext.getCurrentInstance().execute("dlgUploadShapeImovelRural.hide()");
				}
				JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
			}
		}
	}

	public void persistirShapesImovelRural() throws Exception {
		String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
				+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId()
				+ FileUploadUtil.getFileSeparator();
		String nome = "shapeImovelRural";
		String nomeNovo = imovelRural.getIdeImovelRural().toString() + "_"
				+ imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid();

		try {
			boolean podePersistirShape = true;
			List<Imovel> imoveisSeremListados = new ArrayList<Imovel>();

			imovelRural.getIdeLocalizacaoGeografica()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(imovelRural
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			imovelRural.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(
					imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));

			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nome, nomeNovo);

			String[] retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					imovelRural.getIdeImovelRural(),
					imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(),
					imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(),
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
							.getCoordGeobahiaMunicipio());

			if (retorno == null || retorno.length < 2) {
				throw new Exception("Erro ao persistir Localização Geográfica do Limite da Propriedade");
			} else if (retorno[0].equalsIgnoreCase("ERRO")) {
				throw new Exception(retorno[2] + " [" + retorno[1] + "]");
			}

			String geometriaImovel = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
					imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), null);

			imovelRural.getIdeLocalizacaoGeografica().setTheGeom(geometriaImovel);

			if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaImovel)) {
				throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
			}

			if (!imovelRural.getIsFinalizado() && !Util.isNullOuVazio(imovelRural.getIdeRequerenteCadastro())) {
				List<Imovel> imoveisIguais = new ArrayList<Imovel>();
				imoveisIguais = imovelRuralServiceFacade.listarImoveisRuraisComInformacoesIguais(imovelRural);

				if (!Util.isNullOuVazio(imoveisIguais)) {
					for (Imovel imovel : imoveisIguais) {
						if (geometriaImovel != null && imovel != null && imovel.getImovelRural() != null
								&& !Util.isNullOuVazio(imovel.getImovelRural().getIdeLocalizacaoGeografica())) {
							boolean isLocGeoIgual = imovelRuralServiceFacade
									.isLocalizacaoGeograficaIgual(geometriaImovel, imovel.getImovelRural()
											.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());

							if (isLocGeoIgual)
								imoveisSeremListados.add(imovel);
						}
					}
				}

				if (imoveisSeremListados.size() > 0) {
					podePersistirShape = false;
				}
			}

			if (podePersistirShape) {
				imovelRural.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
				imovelRural.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			} else {
				ContextoUtil.getContexto().setImoveisDuplicados(imoveisSeremListados);

				if (!ContextoUtil.getContexto().isPCT()) {
					RequestContext.getCurrentInstance().execute("confirmDialogImovelDuplicado.show();");
				} else {
					JsfUtil.addWarnMessage(
							"A poligonal da comunidade inserida sobrepõe outra comunidade . Verifique os dados e tente novamente.");
				}

				throw new Exception("Imóvel duplicado.");
			}
		} catch (Exception e) {
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nomeNovo, nome);
			throw new Exception(e.getMessage());
		}
	}

	public void persistirShapesImovelRuralPctLimiteTerritorio() throws Exception {
		String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
				+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId()
				+ FileUploadUtil.getFileSeparator();
		String nome = "shapeImovelRuralPct";
		String nomeNovo = imovelRural.getIdeImovelRural().toString() + "_"
				+ imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeSistemaCoordenada().getSrid();

		try {
			List<Imovel> imoveisSeremListados = new ArrayList<Imovel>();

			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(
							imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeClassificacaoSecao()
									.getIdeClassificacaoSecao()));

			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setIdeSistemaCoordenada(
					retornaSistemaCordenadaSelecionado(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
							.getIdeSistemaCoordenada().getIdeSistemaCoordenada()));

			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nome, nomeNovo);

			String[] retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					imovelRural.getIdeImovelRural(),
					imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId(),
					imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeSistemaCoordenada().getSrid(),
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
							.getCoordGeobahiaMunicipio());

			if (retorno == null || retorno.length < 2) {
				throw new Exception("Erro ao persistir Localização Geográfica do Limite de Território");
			} else if (retorno[0].equalsIgnoreCase("ERRO")) {
				throw new Exception(retorno[2] + " [" + retorno[1] + "]");
			}

			String geometriaImovel = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
					imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId(), null);

			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setTheGeom(geometriaImovel);

			if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaImovel)) {
				throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
			}

			if (!imovelRural.getIsFinalizado() && !Util.isNullOuVazio(imovelRural.getIdeRequerenteCadastro())) {
				List<Imovel> imoveisIguais = new ArrayList<Imovel>();
				imoveisIguais = imovelRuralServiceFacade.listarImoveisRuraisComInformacoesIguais(imovelRural);

				if (!Util.isNullOuVazio(imoveisIguais)) {
					for (Imovel imovel : imoveisIguais) {
						if (geometriaImovel != null && imovel != null && imovel.getImovelRural() != null
								&& !Util.isNullOuVazio(
										imovel.getImovelRural().getIdeLocalizacaoGeograficaPctLimiteTerritorio())) {
							boolean isLocGeoIgual = imovelRuralServiceFacade.isLocalizacaoGeograficaIgual(
									geometriaImovel,
									imovel.getImovelRural().getIdeLocalizacaoGeograficaPctLimiteTerritorio()
											.getIdeLocalizacaoGeografica());

							if (isLocGeoIgual)
								imoveisSeremListados.add(imovel);
						}
					}
				}
			}

			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setNovosArquivosShapeImportados(true);
			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setListArquivosShape(new ArrayList<String>());
		} catch (Exception e) {
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nomeNovo, nome);
			throw new Exception(e.getMessage());
		}
	}

	public String excluirImovelDuplicado() {
		try {
			imovelRuralServiceFacade.excluirImovel(this.imovelRural.getImovel());
			ContextoUtil.getContexto().setImovelRural(null);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return "/paginas/manter-imoveis/cefir/listaImoveisRural.xhtml";
	}

	public Boolean getMostrarBotaoUploadShapeImovelRural() {
		if (!Util.isNull(imovelRural.getIdeLocalizacaoGeografica()) && !getExisteTheGeomImovelRural()) {
			if (!Util.isNull(imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()) && imovelRural
					.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao() == 2) {

				if (Util.isNull(imovelRural.getIdeLocalizacaoGeografica().getListArquivosShape())
						|| imovelRural.getIdeLocalizacaoGeografica().getListArquivosShape().size() < 4) {
					return true;
				}
			}
		}
		return false;
	}

	public Boolean getMostrarBotaoUploadShapeImovelRuralPct() {
		if (!Util.isNull(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio())
				&& !getExisteTheGeomImovelRuralPct()) {
			if (!Util.isNull(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeClassificacaoSecao())
					&& imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeClassificacaoSecao()
							.getIdeClassificacaoSecao() == 2) {

				if (Util.isNull(imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getListArquivosShape())
						|| imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getListArquivosShape()
								.size() < 4) {
					return true;
				}
			}
		}
		return false;
	}

	public String getCoordenadaPontoxy() {
		return coordenadaPontoxy;
	}

	public void setCoordenadaPontoxy(String coordenadaPontoxy) {
		this.coordenadaPontoxy = coordenadaPontoxy;
	}

	private String reorganizarCoordenadaPonto(String ponto) {
		String aux = ponto.substring(7, ponto.length() - 1);
		String[] c = aux.split(" ");
		aux = c[1] + " " + c[0];
		return aux;
	}

	public void excluirPontoLocalizacao() {
		if (imovelRural.getIsFinalizado()) {
			imovelRural.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
			imovelRural.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
			imovelRural.getIdeLocalizacaoGeografica().setLocalizacaoExcluida(true);
			JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
		} else {
			try {
				imovelRuralServiceFacade.excluirDadosPersistidos(
						imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
			} catch (Exception exception) {
				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}
	}

	public void excluirPontoLocalizacaoPct() {
		if (imovelRural.getIsFinalizado()) {
			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
					.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setNovosArquivosShapeImportados(false);
			imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().setLocalizacaoExcluida(true);
			JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
		} else {
			try {
				imovelRuralServiceFacade.excluirDadosPersistidos(
						imovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica());
				JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
			} catch (Exception exception) {
				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}
	}

	public String getLinkGeobahia(Integer ideImovel) {
		String linkGeobahia = "";

		try {
			StringBuilder buffer = getURLGeoBahia(ideImovel);
			linkGeobahia = ConfigEnum.GEOBAHIA_SERVER
					+ buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return null;
		}
		return linkGeobahia;
	}

	public String getLinkGeobahiaRL(Integer ideImovel) {
		String linkGeobahiaRL = "";

		try {
			StringBuilder buffer = getURLGeoBahia(ideImovel);
			ReservaLegal lReserva = imovelRuralServiceFacade.buscaReservaLegalByImovelRural(new ImovelRural(ideImovel));

			if (!Util.isNullOuVazio(lReserva) && !Util.isNullOuVazio(lReserva.getIdeLocalizacaoGeografica())) {
				buffer = getURLGeoBahia(
						"idloc=" + lReserva.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				linkGeobahiaRL = ConfigEnum.GEOBAHIA_SERVER
						+ buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20");
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return null;
		}

		return linkGeobahiaRL;
	}

	private StringBuilder getURLGeoBahia(Integer ideImovel) throws MalformedURLException, IOException {
		return criarStreamComUrl(obterStringUrlGeoBahiaPorTipo("idimov=" + ideImovel + "&res=640%20480"));
	}

	private StringBuilder getURLGeoBahia(String parametros) throws MalformedURLException, IOException {
		return criarStreamComUrl(obterStringUrlGeoBahiaPorTipo(parametros));
	}

	private String obterStringUrlGeoBahiaPorTipo(String parametros) {
		return URLEnum.CAMINHO_GEOBAHIA_CERTIFICADO + parametros;
	}

	private StringBuilder criarStreamComUrl(String pUrl) throws MalformedURLException, IOException {
		URL url = new URL(pUrl);

		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

		StringBuilder buffer = new StringBuilder();

		String linha;
		while ((linha = br.readLine()) != null) {
			buffer.append(linha);
		}
		br.close();
		return buffer;
	}

	public List<ImovelRuralUsoAgua> getListUsoAgua(String tipoUsoAgua) {
		try {
			if (tipoUsoAgua.equals(TipoUsoAgua.SUPERFICIAL.getId().toString())) {
				if (Util.isNullOuVazio(listCapSuperficial)) {
					listCapSuperficial = imovelRuralServiceFacade.obteUsoAguaPorTipotipoUsoAgua(tipoUsoAgua,
							this.imovelRural);
					carregarTipoFinalidadeUsoAgua(listCapSuperficial);
				}
				return filtraListaUsoAguaNaoExcluido(listCapSuperficial);
			}
			if (tipoUsoAgua.equals(TipoUsoAgua.SUBTERRANEO.getId().toString())) {
				if (Util.isNullOuVazio(listCapSubterranea)) {
					listCapSubterranea = imovelRuralServiceFacade.obteUsoAguaPorTipotipoUsoAgua(tipoUsoAgua,
							this.imovelRural);
					carregarTipoFinalidadeUsoAgua(listCapSubterranea);
				}
				return filtraListaUsoAguaNaoExcluido(listCapSubterranea);
			}
			if (tipoUsoAgua.equals(TipoUsoAgua.LANCAMENTO.getId().toString())) {
				if (Util.isNullOuVazio(listLancamentoResiduos)) {
					listLancamentoResiduos = imovelRuralServiceFacade.obteUsoAguaPorTipotipoUsoAgua(tipoUsoAgua,
							this.imovelRural);
				}
				return filtraListaUsoAguaNaoExcluido(listLancamentoResiduos);
			}
			if (tipoUsoAgua.equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
				if (Util.isNullOuVazio(listPontoIntervencao)) {
					listPontoIntervencao = imovelRuralServiceFacade.obteUsoAguaPorTipotipoUsoAgua(tipoUsoAgua,
							this.imovelRural);
					// carregarTipoFinalidadeUsoAgua(listPontoIntervencao);
				}
				return filtraListaUsoAguaNaoExcluido(listPontoIntervencao);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return new ArrayList<ImovelRuralUsoAgua>();
	}

	public void carregarTipoFinalidadeUsoAgua(List<ImovelRuralUsoAgua> lImovelRuralUsoAgua) throws Exception {
		for (ImovelRuralUsoAgua imovelRuralUsoAgua : lImovelRuralUsoAgua) {
			imovelRuralUsoAgua.setTipoFinalidadeCollection(
					imovelRuralServiceFacade.listarImovelRuralUsoAguaTipoFinalidadeUsoAgua(imovelRuralUsoAgua));
		}
	}

	public List<ImovelRuralUsoAgua> filtraListaUsoAguaNaoExcluido(List<ImovelRuralUsoAgua> lImovelRuralUsoAgua)
			throws Exception {
		List<ImovelRuralUsoAgua> listRetorno = new ArrayList<ImovelRuralUsoAgua>();
		for (ImovelRuralUsoAgua imovelRuralUsoAgua : lImovelRuralUsoAgua) {
			if (!imovelRuralUsoAgua.isIndExcluido()) {
				listRetorno.add(imovelRuralUsoAgua);
			}
		}
		return listRetorno;
	}

	public String obterPontoFormatado(ImovelRuralUsoAgua pImovelRuralUsoAgua) {

		try {
			CoordenadaGeografica cdPrimaria = new CoordenadaGeografica();
			CoordenadaGeografica cdSegundaria = new CoordenadaGeografica();

			String nome = "";

			if (!Util.isNull(pImovelRuralUsoAgua.getIdeLocalizacaoGeografica())) {
				if (Util.isNullOuVazio(pImovelRuralUsoAgua.getDadoGeografico())) {

					DadoGeografico dadoGeografico = new DadoGeografico();
					dadoGeografico.setIdeLocalizacaoGeografica(pImovelRuralUsoAgua.getIdeLocalizacaoGeografica());
					dadoGeografico = imovelRuralServiceFacade.buscarEntidadePorLocalizacaoGeografica(dadoGeografico);

					if (!Util.isNullOuVazio(dadoGeografico)) {
						cdPrimaria = GeoUtil
								.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica());
					} else {
						cdPrimaria = null;
					}
				} else {
					cdPrimaria = GeoUtil.converterPointParaCoordenadaGeografica(
							pImovelRuralUsoAgua.getDadoGeografico().getCoordGeoNumerica());
				}

				if (!Util.isNullOuVazio(
						pImovelRuralUsoAgua.getIdeLocalizacaoGeografica().getDesLocalizacaoGeografica())) {
					nome += "| " + pImovelRuralUsoAgua.getIdeLocalizacaoGeografica().getDesLocalizacaoGeografica();
				}
			}

			if (!Util.isNull(pImovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal())) {
				if (Util.isNullOuVazio(pImovelRuralUsoAgua.getDadoGeograficoPonto2())) {
					DadoGeografico dadoGeografico = new DadoGeografico();
					dadoGeografico.setIdeLocalizacaoGeografica(pImovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal());
					dadoGeografico = imovelRuralServiceFacade.buscarEntidadePorLocalizacaoGeografica(dadoGeografico);
					if (!Util.isNullOuVazio(dadoGeografico)) {
						cdSegundaria = GeoUtil
								.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica());
					} else {
						cdSegundaria = null;
					}
				} else {
					cdSegundaria = GeoUtil.converterPointParaCoordenadaGeografica(
							pImovelRuralUsoAgua.getDadoGeograficoPonto2().getCoordGeoNumerica());
				}
			}

			String ponto = "";
			if (!Util.isNullOuVazio(cdPrimaria)) {
				if (!cdPrimaria.getAsGD().isEmpty()) {
					if (isUTM(pImovelRuralUsoAgua.getIdeLocalizacaoGeografica())) {
						if (pImovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
							ponto = "Ponto 1: " + cdPrimaria.getLatitude() + ", " + cdPrimaria.getLongitude() + " ";

							if (!Util.isNullOuVazio(cdSegundaria)) {
								ponto = ponto.concat("| Ponto 2: " + cdSegundaria.getLatitude() + ", "
										+ cdSegundaria.getLongitude() + " ");
							}
						} else {
							return cdPrimaria.getLatitude() + ", " + cdPrimaria.getLongitude() + " " + nome;
						}
					} else {
						if (pImovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
							ponto = "Ponto 1: " + cdPrimaria.getLatitude().getAsGD() + "S "
									+ cdPrimaria.getLongitude().getAsGD() + "W ";

							if (!Util.isNullOuVazio(cdSegundaria) && !cdSegundaria.getAsGD().isEmpty()) {
								ponto = ponto.concat("| Ponto 2: " + cdSegundaria.getLatitude().getAsGD() + "S "
										+ cdSegundaria.getLongitude().getAsGD() + "W ");
							} else {
								return cdPrimaria.getLatitude().getAsGD() + "S " + cdPrimaria.getLongitude().getAsGD()
										+ "W " + nome;
							}
						} else {
							ponto = "Ponto 1: " + cdPrimaria.getLatitude() + ", " + cdPrimaria.getLongitude() + " ";
						}
					}
				}
			} else if (!Util.isNullOuVazio(cdSegundaria)) {
				if (isUTM(pImovelRuralUsoAgua.getIdeLocalizacaoGeografica())) {
					if (pImovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {

						if (!Util.isNull(!cdSegundaria.getAsGD().isEmpty())) {
							ponto = ponto.concat("| Ponto 2: " + cdSegundaria.getLatitude().getAsGD() + ", "
									+ cdSegundaria.getLongitude().getAsGD() + " ");
						}
					}
				} else {
					if (pImovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {

						if (!cdSegundaria.getAsGD().isEmpty()) {
							ponto = ponto.concat("| Ponto 2: " + cdSegundaria.getLatitude().getAsGD() + "S "
									+ cdSegundaria.getLongitude().getAsGD() + "W ");
						}
					}
				}

			}
			Integer sistemaCoordenada = pImovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()
					.getIdeSistemaCoordenada();

			if (!sistemaCoordenada.equals(SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getId())
					|| !sistemaCoordenada.equals(SistemaCoordenadaEnum.GEOGRAFICA_SAD69.getId())) {
				ponto = ponto.replaceAll("-", "");
			}

			return ponto.concat(nome);

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

	}

	public String buscarNomeTipoIntervencao(ImovelRuralUsoAgua imovelRuralUsoAgua) {

		ImovelRuralUsoAguaIntervencao pIRUAguaIntervencao = imovelRuralUsoAgua
				.getImovelRuralUsoAguaIntervencaoCollection().iterator().next();
		for (TipoIntervencao tipoIntervencao : getListTipoIntervencao()) {
			if (tipoIntervencao.getIdeTipoIntervencao() == pIRUAguaIntervencao.getIdeTipoIntervencao()
					.getIdeTipoIntervencao()) {
				return tipoIntervencao.getNomTipoIntervencao();
			}
		}
		return "";
	}

	public void prepararParaEdicaoImovelRuralUsoAgua(ImovelRuralUsoAgua imovelRuralUsoAgua) {
		try {
			if (imovelRural.getIsFinalizado()) {
				this.imovelRuralUsoAgua = imovelRuralUsoAgua;
				this.imovelRuralUsoAgua.setIndEdicao(true);
			} else {
				this.imovelRuralUsoAgua = imovelRuralServiceFacade.obterPorId(imovelRuralUsoAgua);
			}

			if (!Util.isNullOuVazio(this.imovelRuralUsoAgua)) {
				if (!Util.isLazyInitExcepOuNull(this.imovelRuralUsoAgua.getTipoFinalidadeCollection())) {
					this.listFinalidadesSelecionadas = new ArrayList<TipoFinalidadeUsoAgua>();
					for (ImovelRuralUsoAguaTipoFinalidadeUsoAgua i : this.imovelRuralUsoAgua
							.getTipoFinalidadeCollection()) {
						this.listFinalidadesSelecionadas.add(i.getIdeTipoFinalidadeUsoAgua());
					}
				}

				carregarCoordenadasGeograficasUsoAgua(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica(),
						TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId());

				if (Util.isNullOuVazio(this.imovelRuralUsoAgua.getProcessoUsoAguaCollection())) {
					this.processoUsoAguaCollection = null;
				} else {
					this.processoUsoAguaCollection = (List<ProcessoUsoAgua>) this.imovelRuralUsoAgua
							.getProcessoUsoAguaCollection();
				}

				if (this.getImovelRuralUsoAgua().getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
					carregarCoordenadasGeograficasUsoAgua(this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal(),
							TemaGeoseiaEnum.RESERVA_LEGAL.getId());
				}

				if (!Util.isNullOuVazio(this.imovelRuralUsoAgua.getImovelRuralUsoAguaIntervencaoCollection())) {
					this.imovelRuralUsoAguaIntervencao = this.imovelRuralUsoAgua
							.getImovelRuralUsoAguaIntervencaoCollection().iterator().next();
				} else {
					this.imovelRuralUsoAguaIntervencao = new ImovelRuralUsoAguaIntervencao();
					this.imovelRuralUsoAguaIntervencao.setIdeTipoIntervencao(new TipoIntervencao());
				}
				carregarUsoBarragem();
				if (!Util.isNull(this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao()) && !Util
						.isNull(this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao().getIdeTipoIntervencao())) {

					if (this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao().getIdeTipoIntervencao() == 3
							|| this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao()
									.getIdeTipoIntervencao() == 10) {

						this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao().setIdeTipoIntervencao(null);

					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void excluirUsoAgua() {
		try {
			if (!Util.isNullOuVazio(this.imovelRuralUsoAgua)
					&& !Util.isNullOuVazio(this.imovelRuralUsoAgua.getIdeImovelRuralUsoAgua())) {
				prepararImovelRuralUsoAguaParaExclusao();
				if (!imovelRural.getIsFinalizado()) {
					persistirExclusaoUsoAgua(this.imovelRuralUsoAgua);
					removeElementoListaImovelUsoAgua(this.imovelRuralUsoAgua);
				}
			} else {
				removeElementoListaImovelUsoAgua(this.imovelRuralUsoAgua);
			}

		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro003"));
			return;
		}
		JsfUtil.addSuccessMessage(Util.getString("cefir_msg_exclusao_sucesso"));
	}

	public void prepararImovelRuralUsoAguaParaExclusao() {
		this.imovelRuralUsoAgua.setDtcExclusao(new Date());
		this.imovelRuralUsoAgua.setIndExcluido(true);

		if (!Util.isNullOuVazio(this.imovelRuralUsoAgua.getProcessoUsoAguaCollection())) {
			for (ProcessoUsoAgua processoUsoAgua : this.imovelRuralUsoAgua.getProcessoUsoAguaCollection()) {
				processoUsoAgua.setDtcExclusao(new Date());
				processoUsoAgua.setIndExcluido(true);
			}
		}

		if (this.imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
			this.imovelRuralUsoAgua.getImovelRuralUsoAguaIntervencaoCollection().iterator().next().setIndExcluido(true);
			this.imovelRuralUsoAgua.getImovelRuralUsoAguaIntervencaoCollection().iterator().next()
					.setDtcExclusao(new Date());
		}
	}

	private void persistirExclusaoUsoAgua(ImovelRuralUsoAgua pImovelUsoAgua) throws Exception {
		if (pImovelUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
			DadoGeografico dadoGeograficoPonto2 = new DadoGeografico();
			dadoGeograficoPonto2.setIdeLocalizacaoGeografica(pImovelUsoAgua.getIdeLocalizacaoGeografica());
			dadoGeograficoPonto2 = imovelRuralServiceFacade
					.buscarEntidadePorLocalizacaoGeografica(dadoGeograficoPonto2);

			if (!Util.isNullOuVazio(dadoGeograficoPonto2)) {
				imovelRuralServiceFacade.excluirDadoGeografico(dadoGeograficoPonto2);
				auditoria.excluir(dadoGeograficoPonto2);
			}
			imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(
					this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		}

		DadoGeografico dadoGeografico = new DadoGeografico();
		dadoGeografico.setIdeLocalizacaoGeografica(pImovelUsoAgua.getIdeLocalizacaoGeografica());
		dadoGeografico = imovelRuralServiceFacade.buscarEntidadePorLocalizacaoGeografica(dadoGeografico);

		if (!Util.isNullOuVazio(dadoGeografico)) {
			imovelRuralServiceFacade.excluirDadoGeografico(dadoGeografico);
			auditoria.excluir(dadoGeografico);
		}

		imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(
				pImovelUsoAgua.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());

		auditoria.excluir(pImovelUsoAgua.getIdeLocalizacaoGeografica());

		// EXCLUI TODAS AS FINALIDADES VINCULADAS AO PONTO
		imovelRuralServiceFacade.excluirTipoFinalidadeUsoAguaPorIdImovelRuralUsoAgua(pImovelUsoAgua);
		pImovelUsoAgua.setTipoFinalidadeCollection(null);
		imovelRuralServiceFacade.salvarAtualizarUsoAgua(pImovelUsoAgua);
		auditoria.excluir(pImovelUsoAgua);

	}

	private void removeElementoListaImovelUsoAgua(ImovelRuralUsoAgua pImovelUsoAgua) {
		if (pImovelUsoAgua.getTipoUso().equals(TipoUsoAgua.SUPERFICIAL.getId().toString())) {
			listCapSuperficial.remove(pImovelUsoAgua);
		} else if (pImovelUsoAgua.getTipoUso().equals(TipoUsoAgua.SUBTERRANEO.getId().toString())) {
			listCapSubterranea.remove(pImovelUsoAgua);
		} else if (pImovelUsoAgua.getTipoUso().equals(TipoUsoAgua.LANCAMENTO.getId().toString())) {
			listLancamentoResiduos.remove(pImovelUsoAgua);
		} else if (pImovelUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
			listPontoIntervencao.remove(pImovelUsoAgua);
		}
	}

	private void adicionaElementoListaImovelUsoAgua() {
		if (!this.imovelRuralUsoAgua.isIndEdicao()) {
			if (this.imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.SUPERFICIAL.getId().toString())) {
				listCapSuperficial.add(this.imovelRuralUsoAgua);
			} else if (this.imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.SUBTERRANEO.getId().toString())) {
				listCapSubterranea.add(this.imovelRuralUsoAgua);
			} else if (this.imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.LANCAMENTO.getId().toString())) {
				listLancamentoResiduos.add(this.imovelRuralUsoAgua);
			} else if (this.imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
				listPontoIntervencao.add(this.imovelRuralUsoAgua);
			}
		}
	}

	/**
	 * Se lat = true retorna latitude Se lat = true retorna longitude Se lat e lon =
	 * false retorna os dois concatenados
	 *
	 * @param lat
	 * @param lon
	 * @param localizacaoGeografica
	 * @return latitude e/ou longitude
	 */
	public String obterLatLongPorLocGeo(Boolean lat, Boolean lon, LocalizacaoGeografica localizacaoGeografica) {
		try {
			DadoGeografico dadoGeografico = new DadoGeografico();
			dadoGeografico.setIdeLocalizacaoGeografica(localizacaoGeografica);
			dadoGeografico = imovelRuralServiceFacade.buscarEntidadePorLocalizacaoGeografica(dadoGeografico);
			if (!Util.isNullOuVazio(dadoGeografico)) {

				CoordenadaGeografica coordenadaGeografica = GeoUtil
						.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica());

				if (!Util.isNullOuVazio(coordenadaGeografica)) {
					if (lat) {
						return coordenadaGeografica.getLatitude().getAsGD();
					} else if (lon) {
						return coordenadaGeografica.getLongitude().getAsGD();
					} else {
						return coordenadaGeografica.getLongitude().getAsGD() + " "
								+ coordenadaGeografica.getLatitude().getAsGD();
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return "";
	}

	public Collection<SistemaCoordenada> getObterDatums() {
		try {
			return imovelRuralServiceFacade.listarDatum();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return null;
	}

	public ImovelRuralUsoAgua getImovelRuralUsoAgua() {
		return imovelRuralUsoAgua;
	}

	public void setImovelRuralUsoAgua(ImovelRuralUsoAgua imovelRuralUsoAgua) {
		this.imovelRuralUsoAgua = imovelRuralUsoAgua;
	}

	public void inicializarObjetosUsoAgua(String tipoImovelRuralUsoAgua) {
		this.imovelRuralUsoAgua = new ImovelRuralUsoAgua();
		imovelRuralUsoAgua.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		imovelRuralUsoAgua.setTipoUso(tipoImovelRuralUsoAgua);

		if (tipoImovelRuralUsoAgua.equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
			imovelRuralUsoAgua.setIdeLocalizacaoGeograficaFinal(new LocalizacaoGeografica());
		}
		this.setImovelRuralUsoAgua(imovelRuralUsoAgua);
		this.setImovelRuralUsoAguaIntervencao(new ImovelRuralUsoAguaIntervencao());
		this.getImovelRuralUsoAguaIntervencao().setIdeTipoIntervencao(new TipoIntervencao());

		this.limparFiltroModalTipoIntervencao();

	}

	public void alterarFormaDeInformarCoordenadaUsoAgua(ValueChangeEvent event) {
		Boolean value = (Boolean) event.getNewValue();
		if (value) {
			this.setExibirLatitudeLongitude(true);
		} else {
			this.setExibirLatitudeLongitude(false);
		}
	}

	public boolean getExibirLatitudeLongitude() {
		return this.exibirLatitudeLongitude;
	}

	public void setExibirLatitudeLongitude(boolean exibirLatitudeLongitude) {
		this.exibirLatitudeLongitude = exibirLatitudeLongitude;
	}

	public void calculaFracaoGrauLatitude(String numPonto) {
		try {
			if (!Util.isNullOuVazio(numPonto)) {
				this.fracaoGrauLatitudeLocPonto2 = PostgisUtil.calculaFracaoGrauLatitude(getGrausLatitudeLocPonto2(),
						getMinutosLatitudeLocPonto2(), getSegundosLatitudeLocPonto2()).toString();
			} else {
				this.fracaoGrauLatitudeLoc = PostgisUtil.calculaFracaoGrauLatitude(getGrausLatitudeLoc(),
						getMinutosLatitudeLoc(), getSegundosLatitudeLoc()).toString();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void calculaFracaoGrauLongitude(String numPonto) {
		try {
			if (!Util.isNullOuVazio(numPonto)) {
				this.fracaoGrauLongitudeLocPonto2 = PostgisUtil.calculaFracaoGrauLongitude(getGrausLongitudeLocPonto2(),
						getMinutosLongitudeLocPonto2(), getSegundosLongitudeLocPonto2()).toString();
			} else {
				this.fracaoGrauLongitudeLoc = PostgisUtil.calculaFracaoGrauLongitude(getGrausLongitudeLoc(),
						getMinutosLongitudeLoc(), getSegundosLongitudeLoc()).toString();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public String getMinutosLatitudeLoc() {
		return minutosLatitudeLoc;
	}

	public void setMinutosLatitudeLoc(String minutosLatitudeLoc) {
		this.minutosLatitudeLoc = minutosLatitudeLoc;
		calculaFracaoGrauLatitude("");
	}

	public String getSegundosLatitudeLoc() {
		return segundosLatitudeLoc;
	}

	public void setSegundosLatitudeLoc(String segundosLatitudeLoc) {
		this.segundosLatitudeLoc = segundosLatitudeLoc;
		calculaFracaoGrauLatitude("");
	}

	public String getGrausLatitudeLoc() {
		return grausLatitudeLoc;
	}

	public void setGrausLatitudeLoc(String grausLatitudeLoc) {
		this.grausLatitudeLoc = grausLatitudeLoc;
		calculaFracaoGrauLatitude("");
	}

	public String getMinutosLongitudeLoc() {
		return minutosLongitudeLoc;
	}

	public void setMinutosLongitudeLoc(String minutosLongitudeLoc) {
		this.minutosLongitudeLoc = minutosLongitudeLoc;
		calculaFracaoGrauLongitude("");
	}

	public String getSegundosLongitudeLoc() {
		return segundosLongitudeLoc;
	}

	public void setSegundosLongitudeLoc(String segundosLongitudeLoc) {
		this.segundosLongitudeLoc = segundosLongitudeLoc;
		calculaFracaoGrauLongitude("");
	}

	public String getGrausLongitudeLoc() {
		return grausLongitudeLoc;
	}

	public void setGrausLongitudeLoc(String grausLongitudeLoc) {
		this.grausLongitudeLoc = grausLongitudeLoc;
		calculaFracaoGrauLongitude("");
	}

	public Collection<TipoFinalidadeUsoAgua> getListTipoFinalidadeUsoAgua() {
		try {
			if (!Util.isNull(imovelRuralUsoAgua.getTipoUso())) {
				final String tipoUso = imovelRuralUsoAgua.getTipoUso();
				ModalidadeOutorgaEnum modalidadeUsoAgua = null;
				if (tipoUso.equals(TipoUsoAgua.SUPERFICIAL.getId().toString())) {
					modalidadeUsoAgua = ModalidadeOutorgaEnum.CAPTACAO_SUPERFICIAL;
				} else if (tipoUso.equals(TipoUsoAgua.SUBTERRANEO.getId().toString())) {
					modalidadeUsoAgua = ModalidadeOutorgaEnum.CAPTACAO_SUBTERRANEA;
				}
				if (modalidadeUsoAgua != null) {
					return imovelRuralServiceFacade.buscarFinalidadeUsoAguaByIdeTipologia(modalidadeUsoAgua);
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return new ArrayList<TipoFinalidadeUsoAgua>();
	}

	public List<TipoFinalidadeUsoAgua> getListFinalidadesSelecionadas() {
		return listFinalidadesSelecionadas;
	}

	public void setListFinalidadesSelecionadas(List<TipoFinalidadeUsoAgua> listFinalidadesSelecionadas) {
		this.listFinalidadesSelecionadas = listFinalidadesSelecionadas;
	}

	public void salvarUsoAgua() {
		try {
			Boolean isReceptorResiduo = true;

			validarCamposObrigatorios(isReceptorResiduo);
			if (Util.isNullOuVazio(this.getImovelRuralUsoAgua().getIdeImovelRuralUsoAgua())) {
				this.getImovelRuralUsoAgua().setDtcCriacao(new Date());
				this.getImovelRuralUsoAgua().setIdeImovelRural(this.imovelRural);

				if (!Util.isNullOuVazio(getProcessoUsoAguaCollection())) {
					if (this.imovelRuralUsoAgua.getIndDispensa() || this.imovelRuralUsoAgua.getIndProcesso()) {
						for (ProcessoUsoAgua p : getProcessoUsoAguaCollection()) {
							p.setIdeImovelRuralUsoAgua(this.getImovelRuralUsoAgua());
							p.setDtcCriacao(new Date());
						}
						this.imovelRuralUsoAgua.setProcessoUsoAguaCollection(getProcessoUsoAguaCollection());
					}
				}

				if (this.getImovelRuralUsoAgua().getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {

					getImovelRuralUsoAguaIntervencao().setDtcCriacao(new Date());
					getImovelRuralUsoAguaIntervencao().setIdeImovelRuralUsoAgua(this.getImovelRuralUsoAgua());

					Collection<ImovelRuralUsoAguaIntervencao> collection = new ArrayList<ImovelRuralUsoAguaIntervencao>();
					collection.add(getImovelRuralUsoAguaIntervencao());

					this.getImovelRuralUsoAgua().setImovelRuralUsoAguaIntervencaoCollection(collection);

					preparaUsoBarragemParaPersistencia();

					this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal().setDesLocalizacaoGeografica(
							this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getDesLocalizacaoGeografica());
					this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal().setIdeSistemaCoordenada(
							this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada());
				}

				preparaLocalizacaoUsoAguaParaPersistencia(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica());
				this.imovelRuralUsoAgua.setDadoGeografico(
						preparaDadoGeograficoParaPersistencia(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica()));

				if (this.getImovelRuralUsoAgua().getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
					preparaLocalizacaoUsoAguaParaPersistencia(
							this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal());
					this.imovelRuralUsoAgua.setDadoGeograficoPonto2(preparaDadoGeograficoParaPersistencia(
							this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal(), "2"));
				}

				salvarTipoFinalidadeUsoAgua();

				if (!imovelRural.getIsFinalizado()) {
					persistirUsoAgua();
					limparListasUsoAgua();
				} else {
					adicionaElementoListaImovelUsoAgua();
				}

				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S001"));
			} else {

				atualizarProcessoUsoAgua();
				if (this.getImovelRuralUsoAgua().getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
					ImovelRuralUsoAguaIntervencao imovelIntervencao = this.getImovelRuralUsoAgua()
							.getImovelRuralUsoAguaIntervencaoCollection().iterator().next();
					imovelIntervencao.setIndExcluido(true);
					imovelIntervencao.setDtcExclusao(new Date());
					preparaUsoBarragemParaPersistencia();
					this.getImovelRuralUsoAgua().getImovelRuralUsoAguaIntervencaoCollection()
							.add(this.imovelRuralUsoAguaIntervencao);
				}

				preparaLocalizacaoUsoAguaParaPersistencia(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica());
				this.imovelRuralUsoAgua.setDadoGeografico(
						preparaDadoGeograficoParaPersistencia(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica()));
				if (this.getImovelRuralUsoAgua().getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
					preparaLocalizacaoUsoAguaParaPersistencia(
							this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal());
					this.imovelRuralUsoAgua.setDadoGeograficoPonto2(preparaDadoGeograficoParaPersistencia(
							this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal(), "2"));
				}

				salvarTipoFinalidadeUsoAgua();

				// if (!imovelRural.getIsFinalizado()) {
				persistirUsoAgua();
				limparListasUsoAgua();
				// } else {
				// adicionaElementoListaImovelUsoAgua();
				// }

				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S002"));
			}
			RequestContext context = RequestContext.getCurrentInstance();
			context.addCallbackParam("closeModal", true);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

	}

	private void persistirUsoAgua() throws Exception {
		if (!Util.isNullOuVazio(this.imovelRuralUsoAgua)) {

			salvarLocalizacaoGeograficaUsoAgua();
			// salvarTipoFinalidadeUsoAgua();

			imovelRuralServiceFacade.salvarAtualizarUsoAgua(this.imovelRuralUsoAgua);

			auditoria.salvar(this.imovelRuralUsoAgua);
		} else {
			ImovelRuralUsoAgua objAntigo = imovelRuralServiceFacade.obterPorId(this.imovelRuralUsoAgua);

			salvarLocalizacaoGeograficaUsoAgua();

			imovelRuralServiceFacade.excluirTipoFinalidadeUsoAguaPorIdImovelRuralUsoAgua(this.imovelRuralUsoAgua);

			if (!Util.isNullOuVazio(this.imovelRuralUsoAgua.getTipoFinalidadeCollection())) {
				for (ImovelRuralUsoAguaTipoFinalidadeUsoAgua tipoFinalidade : this.imovelRuralUsoAgua
						.getTipoFinalidadeCollection()) {
					tipoFinalidade.setIdeImovelRuralUsoAguaTipoFinalidadeUsoAgua(null);
				}
			}

			// salvarTipoFinalidadeUsoAgua();

			imovelRuralServiceFacade.salvarAtualizarUsoAgua(this.imovelRuralUsoAgua);

			auditoria.atualizar(objAntigo, this.imovelRuralUsoAgua);
		}

	}

	private void limparListasUsoAgua() {
		if (this.imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.SUPERFICIAL.getId().toString())) {
			listCapSuperficial.clear();
		}
		if (this.imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.SUBTERRANEO.getId().toString())) {
			listCapSubterranea.clear();
		}
		if (this.imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.LANCAMENTO.getId().toString())) {
			listLancamentoResiduos.clear();
		}
		if (this.imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
			listPontoIntervencao.clear();
		}
	}

	private void salvarTipoFinalidadeUsoAgua() throws Exception {
		if (!Util.isNullOuVazio(this.listFinalidadesSelecionadas)) {
			List<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> listImovelUsoAguaFinalidades = new ArrayList<ImovelRuralUsoAguaTipoFinalidadeUsoAgua>();
			for (TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua : this.listFinalidadesSelecionadas) {
				ImovelRuralUsoAguaTipoFinalidadeUsoAgua imovelUsoAguaFinalidade = new ImovelRuralUsoAguaTipoFinalidadeUsoAgua();
				imovelUsoAguaFinalidade.setIdeImovelRuralUsoAgua(this.getImovelRuralUsoAgua());
				imovelUsoAguaFinalidade.setIdeTipoFinalidadeUsoAgua(tipoFinalidadeUsoAgua);
				listImovelUsoAguaFinalidades.add(imovelUsoAguaFinalidade);
			}
			if (!Util.isNullOuVazio(listImovelUsoAguaFinalidades)) {
				this.imovelRuralUsoAgua.setTipoFinalidadeCollection(listImovelUsoAguaFinalidades);
			}
			// if(!imovelRural.getIsFinalizado()) {
			// imovelRuralServiceFacade.salvarImovelRuralUsoAguaTipoFinalidadeUsoAguaEmLote(listImovelUsoAguaFinalidades);
			// for (ImovelRuralUsoAguaTipoFinalidadeUsoAgua
			// imovelRuralUsoAguaTipoFinalidadeUsoAgua :
			// listImovelUsoAguaFinalidades) {
			// auditoria.salvar(imovelRuralUsoAguaTipoFinalidadeUsoAgua);
			// }
			// }
		}
	}

	private void preparaUsoBarragemParaPersistencia() {
		if (this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao().isBarragem()) {
			List<UsoBarragem> listUsoBarragem = getListUsoBarragemEnum();
			if (listUsoBarragem.size() > 1) {
				this.imovelRuralUsoAguaIntervencao.setUsoBarragem(UsoBarragem.TODOS.getId().toString());
			} else {
				this.imovelRuralUsoAguaIntervencao.setUsoBarragem(listUsoBarragem.get(0).getId().toString());
			}
		}
	}

	private void validarCamposObrigatorios(Boolean isReceptorResiduo) throws Exception {
		if (Util.isNullOuVazio(isReceptorResiduo)) {
			isReceptorResiduo = false;
		}

		if (!isUsuarioSemRestricao()) {
			if (!Util.isNull(this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao()) && !Util
					.isNull(this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao().getIdeTipoIntervencao())) {

				if (!(this.imovelRuralUsoAguaIntervencao.isTravessiaDuto()
						|| this.imovelRuralUsoAguaIntervencao.isConstrucaoPonte()
						|| this.imovelRuralUsoAguaIntervencao.isConstrucaoCais()
						|| this.imovelRuralUsoAguaIntervencao.isDrenagemPluvialLancamento()
						|| this.imovelRuralUsoAguaIntervencao.isDesassoreamentoLimpeza()
						|| this.imovelRuralUsoAguaIntervencao.isConstrucaoTravessia()
						|| this.imovelRuralUsoAguaIntervencao.isConstrucaoPier())) {

					if (Util.isNullOuVazio(
							this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
						throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
								Util.getString("empreendimento_lbl_datum")));
					}

					if (Util.isNullOuVazio(this.imovelRuralUsoAgua.getIndDispensa())) {
						throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
								Util.getString("cefir_lbl_outorga_ou_dispensa_outorga")));
					} else if (!this.imovelRuralUsoAgua.getIndDispensa()
							&& Util.isNullOuVazio(this.imovelRuralUsoAgua.getIndProcesso())) {
						throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
								Util.getString("cefir_lbl_possui_processo_em_tramite")));
					}

					if (this.imovelRuralUsoAgua.getIndDispensa() || this.imovelRuralUsoAgua.getIndProcesso()) {
						if (Util.isNullOuVazio(processoUsoAguaCollection)) {
							throw new Exception(Util.getString("cefir_msg_incluir_um_processo"));
						} else {
							if (processoUsoAguaCollection.size() == 1
									&& processoUsoAguaCollection.get(0).isIndExcluido()) {
								throw new Exception(Util.getString("cefir_msg_incluir_um_processo"));
							}
						}
					}
				}
			} else {
				if (this.imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
					throw new Exception(
							Util.getString("javax.faces.component.UIInput.REQUIRED", "Tipo de Intervenção"));
				}

				if (Util.isNullOuVazio(
						this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
					throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
							Util.getString("empreendimento_lbl_datum")));
				}

				if (Util.isNullOuVazio(this.imovelRuralUsoAgua.getIndDispensa())) {
					throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
							Util.getString("cefir_lbl_outorga_ou_dispensa_outorga")));
				} else if (!this.imovelRuralUsoAgua.getIndDispensa()
						&& Util.isNullOuVazio(this.imovelRuralUsoAgua.getIndProcesso())) {
					throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
							Util.getString("cefir_lbl_possui_processo_em_tramite")));
				}

				if (this.imovelRuralUsoAgua.getIndDispensa() || this.imovelRuralUsoAgua.getIndProcesso()) {
					if (Util.isNullOuVazio(processoUsoAguaCollection)) {
						throw new Exception(Util.getString("cefir_msg_incluir_um_processo"));
					} else {
						if (processoUsoAguaCollection.size() == 1 && processoUsoAguaCollection.get(0).isIndExcluido()) {
							throw new Exception(Util.getString("cefir_msg_incluir_um_processo"));
						}
					}
				}
			}
			if (Util.isNullOuVazio(fracaoGrauLatitudeLoc)) {
				throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
						Util.getString("empreendimento_lbl_latitude")));
			}
			if (Util.isNullOuVazio(fracaoGrauLongitudeLoc)) {
				throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
						Util.getString("empreendimento_lbl_longitude")));
			}

			if (this.imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {

				if (this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao().isTravessiaDuto()) {
					if (Util.isNullOuVazio(this.imovelRuralUsoAguaIntervencao.getTipoTravessia())) {
						throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
								Util.getString("cefir_lbl_tipo_travessia_duto")));
					}
				} else if (this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao().isBarragem()) {
					if (Util.isNullOuVazio(this.imovelRuralUsoAguaIntervencao.getVolumeBarragem())) {
						throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
								Util.getString("cefir_lbl_volume_maximo_acumulado")));
					}
					if (Util.isNullOuVazio(this.imovelRuralUsoAguaIntervencao.getAreaBarragem())) {
						throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
								Util.getString("cefir_lbl_area_reservatiorio_nivel_maximo")));
					}
					if (Util.isNullOuVazio(listUsoBarragemEnum)) {
						throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
								Util.getString("cefir_lbl_barragem_de")));
					}
					if (Util.isNullOuVazio(this.imovelRuralUsoAguaIntervencao.getTipoBarragem())) {
						throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
								Util.getString("cefir_lbl_barragem_para")));
					}
				}
				if (Util.isNullOuVazio(fracaoGrauLatitudeLocPonto2)) {
					throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
							Util.getString("empreendimento_lbl_latitude")));
				}
				if (Util.isNullOuVazio(fracaoGrauLongitudeLocPonto2)) {
					throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
							Util.getString("empreendimento_lbl_longitude")));
				}

			} else {
				if (Util.isNullOuVazio(this.imovelRuralUsoAgua.getValVazao())) {
					throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
							Util.getString("requerimentoUnicoLabelVazaoCaptacao")));
				}
				if (Util.isNullOuVazio(listFinalidadesSelecionadas) && !isReceptorResiduo) {
					throw new Exception(Util.getString("javax.faces.component.UIInput.REQUIRED",
							Util.getString("cefir_lbl_finalidade")));
				}
			}
		}
	}

	private void atualizarProcessoUsoAgua() throws Exception {
		if (!Util.isNull(this.imovelRuralUsoAgua.getIndDispensa()) && !this.imovelRuralUsoAgua.getIndDispensa()) {
			if (!Util.isNullOuVazio(this.imovelRuralUsoAgua.getIndProcesso())
					&& !this.imovelRuralUsoAgua.getIndProcesso()) {
				if (!Util.isNullOuVazio(getProcessoUsoAguaCollection())) {
					for (ProcessoUsoAgua p : getProcessoUsoAguaCollection()) {
						if (!Util.isNullOuVazio(p.getIdeProcessoUsoAgua())) {
							p.setDtcExclusao(new Date());
							p.setIndExcluido(true);
						}
					}
				}
				this.getImovelRuralUsoAgua().setProcessoUsoAguaCollection(getProcessoUsoAguaCollection());
				return;
			}
		}

		List<ProcessoUsoAgua> processorPersistidos = imovelRuralServiceFacade
				.obterProcessoImovelRuralUsoAgua(this.getImovelRuralUsoAgua());
		if (Util.isNullOuVazio(processorPersistidos)) {
			if (!Util.isNullOuVazio(getProcessoUsoAguaCollection())) {
				for (ProcessoUsoAgua p : getProcessoUsoAguaCollection()) {
					p.setIdeImovelRuralUsoAgua(this.imovelRuralUsoAgua);
					p.setDtcCriacao(new Date());
				}
				this.getImovelRuralUsoAgua().setProcessoUsoAguaCollection(getProcessoUsoAguaCollection());
			}
		} else {
			for (ProcessoUsoAgua p : getProcessoUsoAguaCollection()) {
				if (!processorPersistidos.contains(p)) {
					p.setIdeImovelRuralUsoAgua(this.imovelRuralUsoAgua);
					p.setDtcCriacao(new Date());
				}
			}
			for (ProcessoUsoAgua processoUsoAgua : processorPersistidos) {
				if (!getProcessoUsoAguaCollection().contains(processoUsoAgua)) {
					processoUsoAgua.setDtcExclusao(new Date());
					processoUsoAgua.setIndExcluido(true);
					getProcessoUsoAguaCollection().add(processoUsoAgua);
				}
			}
			this.getImovelRuralUsoAgua().setProcessoUsoAguaCollection(getProcessoUsoAguaCollection());
		}
	}

	private void salvarLocalizacaoGeograficaUsoAgua() {
		try {
			LocalizacaoGeografica localizacaoAntiga = null;
			if (this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica() != null) {
				if (!Util.isNullOuVazio(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())
						&& !Util.isNullOuVazio(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica()
								.getIdeSistemaCoordenada().getIdeSistemaCoordenada())) {

					preparaLocalizacaoUsoAguaParaPersistencia(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica());

					if (imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() == null) {
						imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
								imovelRuralUsoAgua.getIdeLocalizacaoGeografica());
						auditoria.salvar(imovelRuralUsoAgua.getIdeLocalizacaoGeografica());
					} else {
						localizacaoAntiga = imovelRuralServiceFacade.carregarLocalizacaoGeografica(
								imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
								imovelRuralUsoAgua.getIdeLocalizacaoGeografica());
						auditoria.atualizar(localizacaoAntiga, imovelRuralUsoAgua.getIdeLocalizacaoGeografica());
					}

					// DadoGeografico dadoGeografico = null;
					// dadoGeografico =
					// preparaDadoGeograficoParaPersistencia(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica());
					// if (!Util.isNullOuVazio(this.imovelRuralUsoAgua.getDadoGeografico())) {
					imovelRuralServiceFacade.salvarOuAtualizarVertice(this.imovelRuralUsoAgua.getDadoGeografico());

					imovelRuralServiceFacade.salvarParamsEPersistindoVerticeTheGeom(
							this.imovelRuralUsoAgua.getDadoGeografico(),
							this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada());
					// }
				}
			}

			if (this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal() != null) {
				if (!Util.isNullOuVazio(
						this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal().getIdeSistemaCoordenada())
						&& !Util.isNullOuVazio(this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal()
								.getIdeSistemaCoordenada().getIdeSistemaCoordenada())) {

					preparaLocalizacaoUsoAguaParaPersistencia(
							this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal());

					if (imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal().getIdeLocalizacaoGeografica() == null) {
						imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
								imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal());
						auditoria.salvar(imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal());
					} else {
						localizacaoAntiga = imovelRuralServiceFacade.carregarLocalizacaoGeografica(
								imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal().getIdeLocalizacaoGeografica());
						imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
								imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal());
						auditoria.atualizar(localizacaoAntiga, imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal());
					}
					imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
							this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal());

					if (!Util.isNull(this.imovelRuralUsoAgua.getDadoGeograficoPonto2())) {
						imovelRuralServiceFacade
								.salvarOuAtualizarVertice(this.imovelRuralUsoAgua.getDadoGeograficoPonto2());
						imovelRuralServiceFacade.salvarParamsEPersistindoVerticeTheGeom(
								this.imovelRuralUsoAgua.getDadoGeograficoPonto2(),
								this.imovelRuralUsoAgua.getIdeLocalizacaoGeograficaFinal().getIdeSistemaCoordenada());

					}
				}
			}

			/*
			 * if(imovelRural.getIsFinalizado()) { if(this.imovelRuralUsoAgua.isIndEdicao())
			 * {
			 *
			 * if(!Util.isNullOuVazio(this.imovelRuralUsoAgua.getDadoGeografico() )) {
			 * preparaCooredenadasUsoAguaExibirNaTela(1,
			 * this.imovelRuralUsoAgua.getDadoGeografico()); } if(!Util.isNullOuVazio
			 * (this.imovelRuralUsoAgua.getDadoGeograficoPonto2())) {
			 * preparaCooredenadasUsoAguaExibirNaTela(2,
			 * this.imovelRuralUsoAgua.getDadoGeograficoPonto2()); } }else{ return; } }
			 */

		} catch (Exception e1) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e1);
		}
	}

	private DadoGeografico preparaDadoGeograficoParaPersistencia(LocalizacaoGeografica localizacaoGeograficaUsoAgua,
			String... tipo) throws Exception {
		Point ponto = null;
		if (tipo.length > 0) {
			ponto = PostgisUtil.criarPonto(Double.parseDouble(getFracaoGrauLatitudeLocPonto2().replace(',', '.')),
					Double.parseDouble(getFracaoGrauLongitudeLocPonto2().replace(',', '.')));
		} else {
			ponto = PostgisUtil.criarPonto(Double.parseDouble(getFracaoGrauLatitudeLoc().replace(',', '.')),
					Double.parseDouble(getFracaoGrauLongitudeLoc().replace(',', '.')));
		}

		DadoGeografico dadoGeografico = new DadoGeografico();
		dadoGeografico.setIdeLocalizacaoGeografica(localizacaoGeograficaUsoAgua);
		dadoGeografico = imovelRuralServiceFacade.buscarEntidadePorLocalizacaoGeografica(dadoGeografico);

		if (Util.isNullOuVazio(dadoGeografico)) {
			dadoGeografico = new DadoGeografico();
		}
		dadoGeografico.setCoordGeoNumerica(ponto.toString());
		dadoGeografico.setIdeLocalizacaoGeografica(localizacaoGeograficaUsoAgua);
		return dadoGeografico;
	}

	private void preparaLocalizacaoUsoAguaParaPersistencia(LocalizacaoGeografica localizacaoGeograficaUsoAgua)
			throws Exception {
		localizacaoGeograficaUsoAgua.setIdeSistemaCoordenada(imovelRuralServiceFacade
				.carregarDatum(localizacaoGeograficaUsoAgua.getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
		localizacaoGeograficaUsoAgua.setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica(1));

		if (Util.isNullOuVazio(localizacaoGeograficaUsoAgua.getDtcCriacao())) {
			localizacaoGeograficaUsoAgua.setDtcCriacao(new Date());
		}
	}

	public ImovelRuralUsoAguaIntervencao getImovelRuralUsoAguaIntervencao() {
		return imovelRuralUsoAguaIntervencao;
	}

	public void setImovelRuralUsoAguaIntervencao(ImovelRuralUsoAguaIntervencao imovelRuralUsoAguaIntervencao) {
		this.imovelRuralUsoAguaIntervencao = imovelRuralUsoAguaIntervencao;
	}

	public List<ProcessoUsoAgua> getProcessoUsoAguaCollection() {
		if (Util.isNullOuVazio(processoUsoAguaCollection)) {
			return processoUsoAguaCollection;
		}
		List<ProcessoUsoAgua> processos = new ArrayList<ProcessoUsoAgua>();
		for (ProcessoUsoAgua p : processoUsoAguaCollection) {
			if (!p.isIndExcluido()) {
				processos.add(p);
			}
		}
		return processos;
	}

	public void setProcessoUsoAguaCollection(List<ProcessoUsoAgua> processoUsoAguaCollection) {
		this.processoUsoAguaCollection = processoUsoAguaCollection;
	}

	public void excluirProcessoUsoAgua() {
		for (ProcessoUsoAgua p : processoUsoAguaCollection) {
			if (p.getNumeroProcesso().equals(processoUsoAguaSelecionado.getNumeroProcesso())) {
				p.setIndExcluido(true);
				p.setDtcExclusao(new Date());
			}
		}
	}

	public String getFracaoGrauLatitudeLoc() {
		return fracaoGrauLatitudeLoc;
	}

	public void setFracaoGrauLatitudeLoc(String fracaoGrauLatitudeLoc) {
		this.fracaoGrauLatitudeLoc = fracaoGrauLatitudeLoc;
	}

	public String getFracaoGrauLongitudeLoc() {
		return fracaoGrauLongitudeLoc;
	}

	public void setFracaoGrauLongitudeLoc(String fracaoGrauLongitudeLoc) {
		this.fracaoGrauLongitudeLoc = fracaoGrauLongitudeLoc;
	}

	private void carregarCoordenadasGeograficasUsoAgua(LocalizacaoGeografica localizacao, Integer numeroPonto)
			throws Exception {
		DadoGeografico dadoGeografico = new DadoGeografico();
		dadoGeografico.setIdeLocalizacaoGeografica(localizacao);
		dadoGeografico = imovelRuralServiceFacade.buscarEntidadePorLocalizacaoGeografica(dadoGeografico);
		if (this.imovelRuralUsoAgua.isIndEdicao() && !Util.isNullOuVazio(this.imovelRuralUsoAgua.getDadoGeografico())
				&& numeroPonto == 1) {
			DadoGeografico dadoTemp = null;
			dadoTemp = this.imovelRuralUsoAgua.getDadoGeografico();
			if (Util.isNull(dadoGeografico.getIdeDadoGeografico())) {
				dadoGeografico = dadoTemp;
			} else {
				dadoGeografico.setCoordGeoNumerica(dadoTemp.getCoordGeoNumerica());
			}
		}

		if (this.imovelRuralUsoAgua.isIndEdicao() && !Util.isNullOuVazio(this.imovelRuralUsoAgua.getDadoGeografico())
				&& numeroPonto == 2) {
			DadoGeografico dadoTemp = null;
			dadoTemp = this.imovelRuralUsoAgua.getDadoGeograficoPonto2();
			if (Util.isNull(dadoGeografico.getIdeDadoGeografico())) {
				dadoGeografico = dadoTemp;
			} else {
				dadoGeografico.setCoordGeoNumerica(dadoTemp.getCoordGeoNumerica());
			}
		}

		preparaCooredenadasUsoAguaExibirNaTela(numeroPonto, dadoGeografico);
	}

	private void preparaCooredenadasUsoAguaExibirNaTela(Integer numeroPonto, DadoGeografico dadoGeografico) {
		if (!Util.isNullOuVazio(dadoGeografico)) {

			CoordenadaGeografica coordenadaGeografica = GeoUtil
					.converterPointParaCoordenadaGeografica(dadoGeografico.getCoordGeoNumerica());

			if (numeroPonto.equals(1)) {
				grausLatitudeLoc = coordenadaGeografica.getLatitude().getGrau().toString();
				minutosLatitudeLoc = coordenadaGeografica.getLatitude().getMinuto().toString();
				segundosLatitudeLoc = coordenadaGeografica.getLatitude().getSegundo().toString();

				grausLongitudeLoc = coordenadaGeografica.getLongitude().getGrau().toString();
				minutosLongitudeLoc = coordenadaGeografica.getLongitude().getMinuto().toString();
				segundosLongitudeLoc = coordenadaGeografica.getLongitude().getSegundo().toString();

				fracaoGrauLatitudeLoc = coordenadaGeografica.getLatitude().getAsGD();
				fracaoGrauLongitudeLoc = coordenadaGeografica.getLongitude().getAsGD();
			} else {
				grausLatitudeLocPonto2 = coordenadaGeografica.getLatitude().getGrau().toString();
				minutosLatitudeLocPonto2 = coordenadaGeografica.getLatitude().getMinuto().toString();
				segundosLatitudeLocPonto2 = coordenadaGeografica.getLatitude().getSegundo().toString();

				grausLongitudeLocPonto2 = coordenadaGeografica.getLongitude().getGrau().toString();
				minutosLongitudeLocPonto2 = coordenadaGeografica.getLongitude().getMinuto().toString();
				segundosLongitudeLocPonto2 = coordenadaGeografica.getLongitude().getSegundo().toString();

				fracaoGrauLatitudeLocPonto2 = coordenadaGeografica.getLatitude().getAsGD();
				fracaoGrauLongitudeLocPonto2 = coordenadaGeografica.getLongitude().getAsGD();
			}
		}
	}

	public boolean getVisualizarUsoAgua() {
		return visualizarUsoAgua;
	}

	public void setVisualizarUsoAgua(boolean visualizarUsoAgua) {
		this.visualizarUsoAgua = visualizarUsoAgua;
	}

	public String getMinutosLongitudeLocPonto2() {
		return minutosLongitudeLocPonto2;
	}

	public void setMinutosLongitudeLocPonto2(String minutosLongitudeLocPonto2) {
		this.minutosLongitudeLocPonto2 = minutosLongitudeLocPonto2;
		calculaFracaoGrauLongitude("2");
	}

	public String getSegundosLongitudeLocPonto2() {
		return segundosLongitudeLocPonto2;
	}

	public void setSegundosLongitudeLocPonto2(String segundosLongitudeLocPonto2) {
		this.segundosLongitudeLocPonto2 = segundosLongitudeLocPonto2;
		calculaFracaoGrauLongitude("2");
	}

	public String getGrausLongitudeLocPonto2() {
		return grausLongitudeLocPonto2;
	}

	public void setGrausLongitudeLocPonto2(String grausLongitudeLocPonto2) {
		this.grausLongitudeLocPonto2 = grausLongitudeLocPonto2;
		calculaFracaoGrauLongitude("2");
	}

	public String getFracaoGrauLatitudeLocPonto2() {
		return fracaoGrauLatitudeLocPonto2;
	}

	public void setFracaoGrauLatitudeLocPonto2(String fracaoGrauLatitudeLocPonto2) {
		this.fracaoGrauLatitudeLocPonto2 = fracaoGrauLatitudeLocPonto2;
		// calculaFracaoGrauLatitude("2");
	}

	public String getFracaoGrauLongitudeLocPonto2() {
		return fracaoGrauLongitudeLocPonto2;
	}

	public void setFracaoGrauLongitudeLocPonto2(String fracaoGrauLongitudeLocPonto2) {
		this.fracaoGrauLongitudeLocPonto2 = fracaoGrauLongitudeLocPonto2;
		// calculaFracaoGrauLongitude("2");
	}

	public String getMinutosLatitudeLocPonto2() {
		return minutosLatitudeLocPonto2;
	}

	public void setMinutosLatitudeLocPonto2(String minutosLatitudeLocPonto2) {
		this.minutosLatitudeLocPonto2 = minutosLatitudeLocPonto2;
		calculaFracaoGrauLatitude("2");
	}

	public String getSegundosLatitudeLocPonto2() {
		return segundosLatitudeLocPonto2;
	}

	public void setSegundosLatitudeLocPonto2(String segundosLatitudeLocPonto2) {
		this.segundosLatitudeLocPonto2 = segundosLatitudeLocPonto2;
		calculaFracaoGrauLatitude("2");
	}

	public String getGrausLatitudeLocPonto2() {
		return grausLatitudeLocPonto2;
	}

	public void setGrausLatitudeLocPonto2(String grausLatitudeLocPonto2) {
		this.grausLatitudeLocPonto2 = grausLatitudeLocPonto2;
		calculaFracaoGrauLatitude("2");
	}

	public List<TipoIntervencao> getListTipoIntervencao() {
		try {
			List<TipoIntervencao> listTipoIntervencaoCEFIR = this.imovelRuralServiceFacade.obterTipoIntervencao();
			List<TipoIntervencao> listReturn = new ArrayList<TipoIntervencao>();

			for (TipoIntervencao tipoIntervencao : listTipoIntervencaoCEFIR) {
				if (!(tipoIntervencao.getId() == TipoIntervencaoEnum.CANALIZACAO_DESASSOREAMENTO_DRAGAGEM.getId()
						.intValue())
						&& !(tipoIntervencao.getId() == TipoIntervencaoEnum.CAIS_PIER_DIQUE.getId().intValue())) {
					listReturn.add(tipoIntervencao);
				}
			}
			return listReturn;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return new ArrayList<TipoIntervencao>();
	}

	public List<UsoBarragem> getListUsoBarragemEnum() {
		return listUsoBarragemEnum;
	}

	public void setListUsoBarragemEnum(List<UsoBarragem> listUsoBarragemEnum) {
		this.listUsoBarragemEnum = listUsoBarragemEnum;
	}

	public List<UsoBarragem> getListUsoBarragem() {
		UsoBarragem[] values = UsoBarragem.values();
		List<UsoBarragem> list = new ArrayList<UsoBarragem>();
		for (UsoBarragem usoBarragem : values) {
			if (!usoBarragem.equals(UsoBarragem.TODOS)) {
				list.add(usoBarragem);
			}
		}
		return list;
	}

	public List<TipoBarragemEnum> getListTipoBarragem() {
		return Arrays.asList(TipoBarragemEnum.values());
	}

	public List<TipoTravessiaDuto> getListTipoTravessia() {
		return Arrays.asList(TipoTravessiaDuto.values());
	}

	private void carregarUsoBarragem() {
		List<UsoBarragem> listUsoBarragem = new ArrayList<UsoBarragem>();
		if (!Util.isNullOuVazio(this.imovelRuralUsoAguaIntervencao.getUsoBarragem())) {
			if (this.imovelRuralUsoAguaIntervencao.getUsoBarragem().equals(UsoBarragem.TODOS.getId().toString())) {
				listUsoBarragem.add(UsoBarragem.CAPTACAO);
				listUsoBarragem.add(UsoBarragem.LANCAMENTO);
			} else {
				for (UsoBarragem usoBarragem : UsoBarragem.values()) {
					if (this.imovelRuralUsoAguaIntervencao.getUsoBarragem().equals(usoBarragem.getId().toString())) {
						listUsoBarragem.add(usoBarragem);
						break;
					}
				}
			}
		}
		setListUsoBarragemEnum(listUsoBarragem);
	}

	// uso agua fim

	public void validarProcessoExterno() throws Exception {
		if (isUsuarioSemRestricao()) {

			ProcessoExterno processoExterno = new ProcessoExterno();

			processoExterno.setProcesso(areaProdutivaSelecionada.getNumProcessoAmc());
			processoExterno.setSistema("CERBERUS");

			List<ProcessoExterno> listaProcessoExterno = imovelRuralServiceFacade
					.buscarProcessoExternoBySistemaNumero(processoExterno);

			if (Util.isNullOuVazio(listaProcessoExterno)) {
				throw new Exception(Util.getString("cefir_msg_processo_nao_encontrado"));
			}
		}
	}

	// INICIO PROCESSO TRAMITE
	public void incluirProcessoTramite() {
		if (getTipoProcessoTramite().equals(PROCESSO_NOVO)) {
			// processos da tabela do seia gerados a partir do dia 05/06/2012
			incluirProcessoNovo();
		} else {
			incluirProcessoAntigo();
		}
		iniciarObjetosProcessoTramite();
	}

	private void incluirProcessoNovo() {
		try {
			Processo processo = new Processo();
			processo.setNumProcesso(getProcessoExterno().getProcesso());

			Processo processoRecuperado = imovelRuralServiceFacade.filtrarProcessoByNumero(processo);
			if (!Util.isNull(processoRecuperado)) {
				adiconarProcessoTramite(processoRecuperado.getNumProcesso());
			} else {
				iniciarObjetosProcessoTramite();
				JsfUtil.addErrorMessage(Util.getString("cefir_msg_processo_nao_encontrado"));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void incluirProcessoAntigo() {
		List<String> listaSistemas = new ArrayList<String>();
		listaSistemas.add("PROHIDROS");
		if (getTipoProcessoTramiteAntigo().equals(PROCESSO_NOVO)) {
			// licença (cerberus e prohidros)
			listaSistemas.add("CERBERUS");
			getProcessoExterno().setSistema("CERBERUS");
		} else {
			// outorga (bdrh)
			listaSistemas.add("BDRH");
			getProcessoExterno().setSistema("");
		}
		try {
			List<ProcessoExterno> listaProcessoExterno = imovelRuralServiceFacade
					.buscarProcessoExternoBySistemaNumero(getProcessoExterno(), listaSistemas);
			if (!Util.isNull(listaProcessoExterno) && !listaProcessoExterno.isEmpty()) {
				adiconarProcessoTramite(listaProcessoExterno.get(0).getProcesso());
			} else if (getTipoProcessoTramiteAntigo().equals("1")) {
				adiconarProcessoTramite(getProcessoExterno().getProcesso());
			} else {
				JsfUtil.addErrorMessage(Util.getString("cefir_msg_processo_nao_encontrado"));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void adiconarProcessoTramite(String numProcesso) {
		// this.processoTramiteImovelRural = new ProcessoTramiteImovelRural();

		ProcessoTramiteImovelRural processoTramiteImovelRural = new ProcessoTramiteImovelRural();
		processoTramiteImovelRural.setNumProcessoTramiteImovelRural(numProcesso);
		processoTramiteImovelRural.setIdeImovelRural(this.imovelRural);
		// Adicao de processo por Outorga
		ProcessoUsoAgua processoUsoAgua = new ProcessoUsoAgua();
		processoUsoAgua.setNumeroProcesso(numProcesso);

		if (Util.isNullOuVazio(processoUsoAguaCollection)) {
			processoUsoAguaCollection = new ArrayList<ProcessoUsoAgua>();
		}
		processoUsoAguaCollection.add(processoUsoAgua);
		if (Util.isNullOuVazio(imovelRural.getProcessoTramiteImovelRuralCollection())) {
			imovelRural.setProcessoTramiteImovelRuralCollection(new ArrayList<ProcessoTramiteImovelRural>());
		}
		imovelRural.getProcessoTramiteImovelRuralCollection().add(processoTramiteImovelRural);
		// RequestContext.getCurrentInstance().execute("dialogProcessoTramiteImovelRural.hide()");
		// this.processoTramiteAtivo = Boolean.TRUE;
	}

	public void excluirProcessoTramite() {
		// if (this.editando) {
		// imovelRural.getCollProcessotramiteImovelRuralExclusao().add(processoTramiteImovelRural);
		// }
		// imovelRural.getProcessoTramiteImovelRuralCollection().remove(processoTramiteImovelRural);
		// this.processoTramiteAtivo =
		// imovelRural.getProcessoTramiteImovelRuralCollection().size() > 0;
	}

	public void alterarLayoutProcessoAntigo(ValueChangeEvent event) {
		// String value = (String) event.getNewValue();
		// if (value.equals(PROCESSO_NOVO)) {
		// isLicenca = Boolean.TRUE;
		// } else {
		// isLicenca = Boolean.FALSE;
		// }
	}

	public void iniciarObjetosProcessoTramite() {
		// this.processoTramiteImovelRural = new ProcessoTramiteImovelRural();
		// this.showPanelProcessosAntigos = Boolean.FALSE;
		// this.setTipoProcessoTramite(PROCESSO_NOVO);
		// this.isLicenca = Boolean.TRUE;
		this.tipoProcessoTramiteAntigo = "1";
		this.tipoProcessoTramite = "0";
		this.processoExterno = new ProcessoExterno();
		// this.processoUsoAgua = null;
	}

	// public void alterarLayoutProcessoTramite(ValueChangeEvent event) {
	// String value = (String) event.getNewValue();
	// if (value.equals(PROCESSO_NOVO)) {
	// showPanelProcessosAntigos = Boolean.FALSE;
	// } else if (value.equals("1")) {
	// showPanelProcessosAntigos = Boolean.TRUE;
	// } else {
	// showPanelProcessosAntigos = null;
	// }
	// }

	public String getTipoProcessoTramite() {
		return tipoProcessoTramite;
	}

	public void setTipoProcessoTramite(String tipoProcessoTramite) {
		this.tipoProcessoTramite = tipoProcessoTramite;
	}

	public ProcessoExterno getProcessoExterno() {
		return processoExterno;
	}

	public void setProcessoExterno(ProcessoExterno processoExterno) {
		this.processoExterno = processoExterno;
	}

	public String getTipoProcessoTramiteAntigo() {
		return tipoProcessoTramiteAntigo;
	}

	public void setTipoProcessoTramiteAntigo(String tipoProcessoTramiteAntigo) {
		this.tipoProcessoTramiteAntigo = tipoProcessoTramiteAntigo;
	}

	public ProcessoUsoAgua getProcessoUsoAguaSelecionado() {
		return processoUsoAguaSelecionado;
	}

	public void setProcessoUsoAguaSelecionado(ProcessoUsoAgua processoUsoAguaSelecionado) {
		this.processoUsoAguaSelecionado = processoUsoAguaSelecionado;
	}

	// FIM PROCESSO TRAMITE

	// Inicio Finalizacao

	public String getPerguntaRlMenorQueVinte() {
		String pergunta = "";

		try {
			if (!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(imovelRural.getImovel())
					&& !Util.isNullOuVazio(imovelRural.getImovel().getIdeEndereco())
					&& !Util.isNullOuVazio(imovelRural.getImovel().getIdeEndereco().getIdeLogradouro())
					&& !Util.isNullOuVazio(
							imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio())
					&& !Util.isNullOuVazio(imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
							.getCoordGeobahiaMunicipio())) {

				List<ModuloFiscal> listModuloFiscal = imovelRuralServiceFacade.filtrarByCodIbge(imovelRural.getImovel()
						.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());

				if (!Util.isNullOuVazio(listModuloFiscal)) {
					pergunta = "Em 22/07/2008 a área do imóvel era igual ou menor que "
							+ listModuloFiscal.get(0).getModFiscal() * 4 + ".00 ha ?";
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			MensagemUtil.erro("Houve um erro ao tentar carregar os módulos fiscais.");
			return "";
		}

		return pergunta;
	}

	public String habilitaMensagensFinalizacao() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("openModal", true);
		context.addCallbackParam("isRlMenorQueVintePorCento", rlMenorQueVintePorCento);
		context.addCallbackParam("existeSobreposicaoRlApp", existeSobreposicaoRlApp);
		context.addCallbackParam("existeSobreposicaoRlAp", existeSobreposicaoRlAreaProdutiva);
		return null;
	}

	public String cancelaPerguntaRlMenorQueVinte() {
		JsfUtil.addErrorMessage(Util.getString("cefir_msg_A007"));
		return null;
	}

	public String cancelaSobreposicaoRlAp() {
		JsfUtil.addErrorMessage(Util.getString("cefir_msg_sobreposicao_rl_ap"));
		return null;
	}

	// Fim Finalizacao

	// Tela de confirmação
	public List<DocumentoImovelRural> getListaArquivoReservaAprovada() {
		List<DocumentoImovelRural> listaArquivoReservaAprovada = new ArrayList<DocumentoImovelRural>();
		if (!Util.isNullOuVazio(this.imovelRural.getReservaLegal())
				&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeDocumentoAprovacao())) {
			listaArquivoReservaAprovada.add(this.imovelRural.getReservaLegal().getIdeDocumentoAprovacao());
		}
		return listaArquivoReservaAprovada;
	}

	public List<DocumentoImovelRural> getListaArquivoReservaAverbada() {
		List<DocumentoImovelRural> listaArquivoReservaAverbada = new ArrayList<DocumentoImovelRural>();
		if (!Util.isNullOuVazio(this.imovelRural.getReservaLegal())
				&& !Util.isNullOuVazio(this.imovelRural.getReservaLegal().getIdeReservaLegalAverbada())) {
			listaArquivoReservaAverbada
					.add(this.imovelRural.getReservaLegal().getIdeReservaLegalAverbada().getIdeDocumentoAverbacao());
		}
		return listaArquivoReservaAverbada;
	}

	public CommandButton getBtnFinalizar() {
		return btnFinalizar;
	}

	public void setBtnFinalizar(CommandButton btnFinalizar) {
		this.btnFinalizar = btnFinalizar;
	}

	public boolean getHouveMudanca() {
		return houveMudanca;
	}

	public void setHouveMudanca(boolean houveMudanca) {
		this.houveMudanca = houveMudanca;
	}

	public boolean isIgualShapeRlSeiaECerberus() {
		return isIgualShapeRlSeiaECerberus;
	}

	public void setIgualShapeRlSeiaECerberus(boolean isIgualShapeRlSeiaECerberus) {
		this.isIgualShapeRlSeiaECerberus = isIgualShapeRlSeiaECerberus;
	}

	public void atualizarModuloFiscal() throws Exception {
		if (!Util.isNullOuVazio(imovelRural.getImovel().getIdeEndereco()) && !Util.isNull(imovelRural.getImovel()
				.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio())) {
			Double codIbgeImovelRural = imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
					.getCoordGeobahiaMunicipio();

			Double qtdModuloFiscal;
			ModuloFiscal mf;

			mf = imovelRuralServiceFacade.filtrarByCodIbge(codIbgeImovelRural).get(0);
			qtdModuloFiscal = imovelRural.getValArea() / mf.getModFiscal();

			qtdModuloFiscal = Util.converterDoubleDuasCasas(qtdModuloFiscal);

			imovelRural.setQtdModuloFiscal(qtdModuloFiscal);
		}
	}

	public boolean getHabilitaCadastroProprietarioImovel() {
		if (editandoProprietarioCda)
			return editandoProprietarioCda;
		else
			return habilitaCadastroProprietarioImovel;
	}

	public void setHabilitaCadastroProprietarioImovel(boolean habilitaCadastroProprietarioImovel) {
		this.habilitaCadastroProprietarioImovel = habilitaCadastroProprietarioImovel;
	}

	public boolean getHabilitaBotaoSalvarProprietario() {
		if (getHabilitaCadastroProprietarioImovel()) {
			return true;
		} else {
			if (pessoaFisicaPersist.getIdePessoaFisica() != null
					|| pessoaJuridicaPersist.getIdePessoaJuridica() != null) {
				return true;
			}
		}
		return false;
	}

	public Collection<TipoDocumentoImovelRural> getListTipoDocumentoImovelRural() {
		try {
			listTipoDocumentoImovelRural = new ArrayList<TipoDocumentoImovelRural>();

			if (getShowPCT()) {
				if (!Util.isNull(this.pctImovelRural)
						&& !Util.isNullOuVazio(this.pctImovelRural.getIdeTipoTerritorioPct())) {
					listTipoDocumentoImovelRural = imovelRuralServiceFacade
							.listarTipoDocumentoImovelRuralPorIdeTipoTerritorioPct(
									this.pctImovelRural.getIdeTipoTerritorioPct().getIdeTipoTerritorioPct());
				}
			} else {
				if (!Util.isNullOuVazio(tipoVinculoImovel)
						&& !Util.isNullOuVazio(tipoVinculoImovel.getIdeTipoVinculoImovel()))
					listTipoDocumentoImovelRural = imovelRuralServiceFacade
							.listarTipoDocumentoImovelRuralPorIdeTipoVinculoImovel(
									this.tipoVinculoImovel.getIdeTipoVinculoImovel());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return listTipoDocumentoImovelRural;
	}

	public void setListTipoDocumentoImovelRural(Collection<TipoDocumentoImovelRural> listTipoDocumentoImovelRural) {
		this.listTipoDocumentoImovelRural = listTipoDocumentoImovelRural;
	}

	public void carregaTipoDocumentoSelecionado() {
		getListTipoDocumentoImovelRural();
		for (TipoDocumentoImovelRural tipoDocumento : listTipoDocumentoImovelRural) {
			if (tipoDocumento.equals(imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural())) {
				imovelRural.getDocumentoImovelRuralPosse().setIdeTipoDocumentoImovelRural(tipoDocumento);
				break;
			}
		}
	}

	public boolean habilitaVisualizacaoGrupo(Integer numGrupo) {
		boolean habilitaGrupo = false;
		if (!Util.isNull(imovelRural.getDocumentoImovelRuralPosse())
				&& !Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural())) {
			carregaTipoDocumentoSelecionado();
			if (imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural().getNumGrupoDocumento()
					.equals(numGrupo)) {
				habilitaGrupo = true;
			}
		}
		return habilitaGrupo;
	}

	public boolean isRenderedCamposConcessao() {
		if (!Util.isNull(imovelRural.getDocumentoImovelRuralPosse())
				&& !Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural())) {
			return TipoDocumentoImovelRuralEnum.CONTRATO_DE_CONCESSAO_DE_DIREITO_REAL_DE_USO.getId().equals(imovelRural
					.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural().getIdeTipoDocumentoImovelRural());
		}
		return false;
	}

	public boolean isNotRenderCamposPct() {

		if (Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse())
				|| Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural())) {
			return true;
		} else if (imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural()
				.getIdeTipoDocumentoImovelRural().equals(18)
				|| imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural()
						.getIdeTipoDocumentoImovelRural().equals(27)
				|| imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural()
						.getIdeTipoDocumentoImovelRural().equals(28)
				|| imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural()
						.getIdeTipoDocumentoImovelRural().equals(29)
				|| imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural()
						.getIdeTipoDocumentoImovelRural().equals(30)) {

			return false;
		}

		return true;
	}

	private Endereco enderecoDeclarante;
	private Logradouro logradouroDeclarante;
	private Logradouro logradouroPesquisa;
	private Bairro bairroDeclarante;
	private Bairro bairroDeclaranteCombo;
	private Municipio municipioDeclarante;
	private Estado estadoDeclarante;
	private TipoLogradouro tipoLogradouroDeclarante;
	private List<Logradouro> listaLogradouroDeclarante;
	private List<Bairro> listaBairroDeclarante;
	private List<Municipio> listaMunicipioDeclarante;
	private List<Estado> listaEstadoDeclarante;
	private boolean showInputs;
	private boolean showInputLogradouro;
	private boolean enableEnderecoDeclarante;
	private boolean escolheuBairro = false;
	private Boolean showUfCidade = Boolean.FALSE;
	private Boolean showFdbairro = Boolean.FALSE;
	private Boolean isMunicipioSalvador;
	private FileUploadEvent fileUploadEventPlanilhaPct;
	private Boolean enableChangeBairroDeclarante = true;
	
	public void limparCamposDocumento(ValueChangeEvent event) {
		Integer numGrupo = 0;
		isEdicaoDocumentacao = true;
		for (TipoDocumentoImovelRural tipoDocumento : listTipoDocumentoImovelRural) {
			if (tipoDocumento.equals(event.getNewValue())) {
				numGrupo = tipoDocumento.getNumGrupoDocumento();
				break;
			}
		}
		if (numGrupo.equals(4)) {
			imovelRural.getDocumentoImovelRuralPosse().setIdeEnderecoDeclarante(new Endereco());
			imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().setIdeLogradouro(new Logradouro());
			imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro()
					.setIdeTipoLogradouro(new TipoLogradouro(0));
			imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro()
					.setIdeBairro(new Bairro(0));
			imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro()
					.setIdeMunicipio(new Municipio(0));
			imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().getIdeLogradouro().getIdeMunicipio()
					.setIdeEstado(new Estado(0));
			imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().setNumEndereco("");
			imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante().setDesComplemento("");
			enderecoDeclarante = new Endereco();
			logradouroDeclarante = new Logradouro();
			logradouroPesquisa = new Logradouro();
			listaLogradouroDeclarante = new ArrayList<Logradouro>();
			tipoLogradouroDeclarante = new TipoLogradouro();
			bairroDeclarante = new Bairro();
			listaBairroDeclarante = new ArrayList<Bairro>();
			municipioDeclarante = new Municipio();
			estadoDeclarante = new Estado();
			enableEnderecoDeclarante = false;
			enableChangeBairroDeclarante = false;
		} else {
			imovelRural.getDocumentoImovelRuralPosse().setIdeEnderecoDeclarante(null);
		}
		imovelRural.getDocumentoImovelRuralPosse().setDtcDocumento(null);
		imovelRural.getDocumentoImovelRuralPosse().setDscEmissorDocumento(null);
		imovelRural.getDocumentoImovelRuralPosse().setNomVendedor(null);
		imovelRural.getDocumentoImovelRuralPosse().setNumCpfVendedor(null);
		imovelRural.getDocumentoImovelRuralPosse().setNomDeclarante(null);
		imovelRural.getDocumentoImovelRuralPosse().setNumCpfCnpjDeclarante(null);

		if (getShowPCT()) {

			imovelRural.setNumMatricula(null);
			imovelRural.setNumRegistro(null);

			imovelRural.setNumCcir(null);
			imovelRural.setNumItr(null);
			imovelRural.setCodSipra(null);
			imovelRural.setIdeMunicipioCartorio(null);
			imovelRural.setDesCartorio(null);
			imovelRural.setDesLivro(null);
			imovelRural.setNumFolha(null);
			imovelRural.setNumNirf(null);
			imovelRural.setDscTermoAutodeclaracao(null);
		}

	}
	

	public void filtrarEnderecoDeclarantePorCep() {
		try {
			logradouroDeclarante = new Logradouro(0);
			tipoLogradouroDeclarante = new TipoLogradouro(0);
			Logradouro logradouro = montaListaBairrosDeclarante();
			EnderecoDTO enderecoApi = cepService.getEnderecoApi();
			showInputs = false;
			escolheuBairro = false;
			showInputLogradouro = false;	
			
			if (logradouro.getIndOrigemApi() == true) {
				if (enderecoApi.getLogradouro().equals("")) {
					showInputs = true;
					showInputLogradouro = true;
				}
				if(Util.isNullOuVazio(logradouro.getIdeBairro())) {
					enableChangeBairroDeclarante = true;
				} else {
					enableChangeBairroDeclarante = false;
				}
			}
			if(logradouro.getIdeBairro()==null) {
				bairroDeclarante = new Bairro(0);
				setBairroDeclaranteCombo(new Bairro(0));
			}else {
				bairroDeclarante = cepService.getBairroEnt();
				setBairroDeclaranteCombo(cepService.getBairroEnt());
				if(listaBairroDeclarante.contains(cepService.getBairroEnt())==false) {
					listaBairroDeclarante.add(cepService.getBairroEnt());
				}
			}
			if(!logradouro.getNomLogradouro().isEmpty()) {
				logradouroDeclarante=logradouroService.getLogradouroById(logradouro.getIdeLogradouro());
				if(!listaLogradouroDeclarante.contains(logradouroDeclarante)) {
					listaLogradouroDeclarante.add(logradouroDeclarante);
				}
				tipoLogradouroDeclarante = tipoLogradouroService.findTipoLogradouroByIde(enderecoApi.getIdeTipoLogradouro());
			}
			enderecoDeclarante.setNumEndereco("");
			enderecoDeclarante.setDesComplemento("");
			isMunicipioSalvador = null;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public Logradouro montaListaBairrosDeclarante() {
		try {
				EnderecoDTO	enderecoApi = cepService.carregar(logradouroPesquisa.getNumCep().toString());		
				if(Util.isNullOuVazio(enderecoApi) || Util.isNullOuVazio(logradouroPesquisa.getNumCep())) {
					throw new Exception("CEP inválido.");
				}
				cepService.montarEstrutura();
				listaBairroDeclarante = imovelRuralServiceFacade
						.filtrarBairroPorCepSemDuplicidade(Integer.valueOf(logradouroPesquisa.getNumCep()));
				Logradouro logradouro  = cepService.montarEstrutura();
				isMunicipioSalvador = enderecoApi.getIdeMunicipio().equals(MunicipioEnum.SALVADOR.getId());
				
				if (logradouro.getIndOrigemApi() == true) {
					if (enderecoApi.getLogradouro().equals("")) {
						showInputs = true;
						showInputLogradouro = true;
					}
					if(Util.isNullOuVazio(logradouro.getIdeBairro())) {
						enableChangeBairroDeclarante = true;
					} else {
						enableChangeBairroDeclarante = false;
					}
				} else {
					enableChangeBairroDeclarante = true;
				}

				Municipio municipio = cepService.getMunicipioEnt();
				if (!Util.isNullOuVazio(municipio)) {
					municipioDeclarante = municipio;
					estadoDeclarante = municipio.getIdeEstado();
				}
				showUfCidade = Boolean.FALSE;
				enableEnderecoDeclarante = true;
				return logradouro;
		} catch (Exception ex) {
			showUfCidade = Boolean.TRUE;
			enableEnderecoDeclarante = false;
			if (ex.getMessage().equals("-1") || ex.getMessage().equals("CEP inválido.")) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, ex);
				JsfUtil.addErrorMessage("CEP inválido ou não encontrado na base dos correios.");
			}
			if (ex.getMessage().equals("-2")) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, ex);
				JsfUtil.addErrorMessage("Erro ao carregar dados do Endereço, contate o administrador do sistema.");
			} else {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, ex);
				JsfUtil.addErrorMessage("Erro ao carregar dados do Endereço, contate o administrador do sistema.");
			}
		}
		return null;
	}

	public void changeLogradouroMunicipioDeclarante(ValueChangeEvent event) {
		try {
			Bairro bairroSelected = (Bairro) event.getNewValue();

			if (bairroSelected != null && bairroSelected.getIdeBairro() == -1) {
				bairroDeclarante = new Bairro();
				setBairroDeclaranteCombo(new Bairro());
				logradouroDeclarante = new Logradouro(-1);
				tipoLogradouroDeclarante = new TipoLogradouro(0);
				escolheuBairro = false;
				showFdbairro = true;
				showInputs = Boolean.TRUE;
				showUfCidade = Boolean.TRUE;
			} 

			if (bairroSelected != null && bairroSelected.getIdeBairro() != 0 && bairroSelected.getIdeBairro() != -1) {
				bairroDeclarante = enderecoFacade.filtrarBairroById(bairroSelected);
				setBairroDeclaranteCombo(enderecoFacade.filtrarBairroById(bairroSelected));
				if (bairroDeclarante != null) {
					listaLogradouroDeclarante = imovelRuralServiceFacade.filtrarLogradouroByNome(bairroDeclarante,
							logradouroPesquisa.getNumCep());

					if (!Util.isNullOuVazio(logradouroDeclarante)
							&& !logradouroDeclarante.getIdeLogradouro().equals(0)) {

						if (!listaLogradouroDeclarante.contains(logradouroDeclarante)
								&& !Util.isNullOuVazio(listLogradouro)) {

							for (Logradouro logradouro : listLogradouro) {

								if (logradouro.getNomLogradouro().trim().toLowerCase()
										.equals(logradouroDeclarante.getNomLogradouro().trim().toLowerCase())) {
									logradouroDeclarante = logradouro;
									break;
								}
							}
						}
					} else {
						logradouroDeclarante = new Logradouro(0);
					}
				}

				municipioDeclarante = bairroDeclarante.getIdeMunicipio();
				isMunicipioSalvador = municipioDeclarante.getIdeMunicipio().equals(MunicipioEnum.SALVADOR.getId());
				estadoDeclarante = municipioDeclarante.getIdeEstado();
				showInputs = false;
				escolheuBairro = true;
				showInputLogradouro = false;
				showUfCidade = Boolean.FALSE;
				showFdbairro = false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public Collection<SelectItem> getValuesComboBairroDeclarante() {
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		Iterator<Bairro> i = listaBairroDeclarante.iterator();
		toReturn.add(new SelectItem(null, "Selecione..."));
		while (i.hasNext()) {
			Bairro bairro = i.next();
			toReturn.add(new SelectItem(bairro, bairro.getNomBairro()));
		}
		toReturn.add(new SelectItem(new Bairro(-1), "Outro"));
		return toReturn;
	}

	public Collection<SelectItem> getValuesComboLogradouroDeclarante() {
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		if (!Util.isNullOuVazio(listaLogradouroDeclarante)) {
			Iterator<Logradouro> i = listaLogradouroDeclarante.iterator();
			toReturn.add(new SelectItem(new Logradouro(0), "Selecione..."));
			while (i.hasNext()) {
				Logradouro logradouro = i.next();
				toReturn.add(new SelectItem(logradouro, logradouro.getNomLogradouro()));
			}
		} else {
			toReturn.add(new SelectItem(new Logradouro(0), "Selecione..."));

		}
		toReturn.add(new SelectItem(new Logradouro(-1), "Outro"));
		return toReturn;
	}

	public void changeLogradouroDeclarante(ValueChangeEvent event) {
		try {
			Logradouro logradouroSelected = (Logradouro) event.getNewValue();

			if (logradouroSelected.getIdeLogradouro() == -1) { // para aparecer a caixa de texto de Logradouro
				showInputs = false;
				showInputLogradouro = true;
				logradouroDeclarante.setIdeLogradouro(-1);
			}

			if (!Util.isNull(logradouroSelected) && logradouroSelected.getIdeLogradouro() != 0
					&& logradouroSelected.getIdeLogradouro() != -1) {
//				logradouroDeclarante = enderecoFacade.filtrarLogradouroById(logradouroSelected);
				tipoLogradouroDeclarante = logradouroDeclarante.getIdeTipoLogradouro();
				showInputLogradouro = false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void changeEstadoDeclarante() {
		if (estadoDeclarante != null && !(estadoDeclarante.getIdeEstado() == null)) {
			try {
				listaMunicipioDeclarante = (List<Municipio>) imovelRuralServiceFacade
						.filtrarListaMunicipiosPorEstado(estadoDeclarante);
				if (estadoDeclarante.getIdeEstado().intValue() == 5
						&& (isMunicipioSalvador == null || isMunicipioSalvador)) {
					isMunicipioSalvador = null;
					setMunicipioDeclarante(new Municipio(837, "Salvador"));
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			}
		} else {
			listaMunicipioDeclarante = new ArrayList<Municipio>();
		}
	}

	private void carregarPropriedadesEnderecoDeclarante() {
		if (!Util.isNullOuVazio(imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante())) {
			enderecoDeclarante = imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante();
			logradouroPesquisa = enderecoDeclarante.getIdeLogradouro();
			montaListaBairrosDeclarante();
			logradouroDeclarante = enderecoDeclarante.getIdeLogradouro();
			if (!Util.isNullOuVazio(logradouroDeclarante) && !Util.isNullOuVazio(logradouroDeclarante.getIdeBairro())) {
				for (Bairro bairro : listaBairroDeclarante) {
					if (bairro.getNomBairro().trim().toLowerCase()
							.equals(logradouroDeclarante.getIdeBairro().getNomBairro().trim().toLowerCase())) {
						bairroDeclarante = bairro;
						setBairroDeclaranteCombo(bairro);
						break;
					}
				}
			}
			try {
				if (!Util.isNullOuVazio(bairroDeclarante)) {
					listaLogradouroDeclarante = imovelRuralServiceFacade.filtrarLogradouroByNome(bairroDeclarante,
							Integer.valueOf(logradouroPesquisa.getNumCep()));
				} else {
					bairroDeclarante = imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante()
							.getIdeLogradouro().getIdeBairro();
					setBairroDeclaranteCombo(imovelRural.getDocumentoImovelRuralPosse().getIdeEnderecoDeclarante()
							.getIdeLogradouro().getIdeBairro());
				}

				if (!Util.isNullOuVazio(this.logradouroDeclarante)
						&& !this.logradouroDeclarante.getIdeLogradouro().equals(0)
						&& (Util.isNotNullAndTrue(this.logradouroDeclarante.getIndOrigemCorreio()) || Util.isNotNullAndTrue(this.logradouroDeclarante.getIndOrigemApi()))) {
					if (!listaLogradouroDeclarante.contains(this.logradouroDeclarante)) {
						for (Logradouro logradouro : listaLogradouroDeclarante) {
							if (logradouro.getNomLogradouro().trim().toLowerCase()
									.equals(this.logradouroDeclarante.getNomLogradouro().trim().toLowerCase())) {
								setLogradouroDeclarante(logradouro);
								break;
							}
						}
					}
				} else {
					if (Util.isNullOuVazio(logradouroDeclarante.getNomLogradouro())) {
						setLogradouroDeclarante(new Logradouro(0));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			logradouroDeclarante.setIdeBairro(bairroDeclarante);
			if (!Util.isNullOuVazio(logradouroDeclarante) && !Util.isNullOuVazio(logradouroDeclarante.getIdeBairro())) {
				tipoLogradouroDeclarante = logradouroDeclarante.getIdeTipoLogradouro();

				if (!Util.isNullOuVazio(logradouroDeclarante.getIdeMunicipio())) {
					municipioDeclarante = logradouroDeclarante.getIdeMunicipio();
				} else {
					municipioDeclarante = logradouroPesquisa.getIdeMunicipio();
				}

				if (!Util.isNullOuVazio(municipioDeclarante)
						&& !Util.isNullOuVazio(municipioDeclarante.getIdeMunicipio()))
					isMunicipioSalvador = (municipioDeclarante.getIdeMunicipio().equals(837));
				else
					isMunicipioSalvador = null;
			}

			if (!Util.isNullOuVazio(municipioDeclarante) && !Util.isNullOuVazio(municipioDeclarante.getIdeEstado())) {
				estadoDeclarante = municipioDeclarante.getIdeEstado();
			}
			enableEnderecoDeclarante = true;
			enableChangeBairroDeclarante = true;
		}
	}

	public Endereco getEnderecoDeclarante() {
		return enderecoDeclarante;
	}

	public void setEnderecoDeclarante(Endereco enderecoDeclarante) {
		this.enderecoDeclarante = enderecoDeclarante;
	}

	public Logradouro getLogradouroDeclarante() {
		return logradouroDeclarante;
	}

	public void setLogradouroDeclarante(Logradouro logradouroDeclarante) {
		this.logradouroDeclarante = logradouroDeclarante;
	}

	public Logradouro getLogradouroPesquisa() {
		return logradouroPesquisa;
	}

	public void setLogradouroPesquisa(Logradouro logradouroPesquisa) {
		this.logradouroPesquisa = logradouroPesquisa;
	}

	public Bairro getBairroDeclarante() {
		return bairroDeclarante;
	}

	public void setBairroDeclarante(Bairro bairroDeclarante) {
		this.bairroDeclarante = bairroDeclarante;
	}

	public Municipio getMunicipioDeclarante() {
		return municipioDeclarante;
	}

	public void setMunicipioDeclarante(Municipio municipioDeclarante) {
		this.municipioDeclarante = municipioDeclarante;
	}

	public Estado getEstadoDeclarante() {
		return estadoDeclarante;
	}

	public void setEstadoDeclarante(Estado estadoDeclarante) {
		this.estadoDeclarante = estadoDeclarante;
	}

	public TipoLogradouro getTipoLogradouroDeclarante() {
		return tipoLogradouroDeclarante;
	}

	public void setTipoLogradouroDeclarante(TipoLogradouro tipoLogradouroDeclarante) {
		this.tipoLogradouroDeclarante = tipoLogradouroDeclarante;
	}

	public List<Logradouro> getListaLogradouroDeclarante() {
		return listaLogradouroDeclarante;
	}

	public void setListaLogradouroDeclarante(List<Logradouro> listaLogradouroDeclarante) {
		this.listaLogradouroDeclarante = listaLogradouroDeclarante;
	}

	public List<Bairro> getListaBairroDeclarante() {
		return listaBairroDeclarante;
	}

	public void setListaBairroDeclarante(List<Bairro> listaBairroDeclarante) {
		this.listaBairroDeclarante = listaBairroDeclarante;
	}

	public List<Municipio> getListaMunicipioDeclarante() {
		return listaMunicipioDeclarante;
	}

	public void setListaMunicipioDeclarante(List<Municipio> listaMunicipioDeclarante) {
		this.listaMunicipioDeclarante = listaMunicipioDeclarante;
	}

	public List<Estado> getListaEstadoDeclarante() {
		return listaEstadoDeclarante;
	}

	public void setListaEstadoDeclarante(List<Estado> listaEstadoDeclarante) {
		this.listaEstadoDeclarante = listaEstadoDeclarante;
	}

	public boolean isShowInputs() {
		return showInputs;
	}

	public void setShowInputs(boolean showInputs) {
		this.showInputs = showInputs;
	}

	public boolean isShowInputLogradouro() {
		return showInputLogradouro;
	}

	public void setShowInputLogradouro(boolean showInputLogradouro) {
		this.showInputLogradouro = showInputLogradouro;
	}

	public Boolean getShowUfCidade() {
		return showUfCidade;
	}

	public void setShowUfCidade(Boolean showUfCidade) {
		this.showUfCidade = showUfCidade;
	}

	public Boolean getIsMunicipioSalvador() {
		return isMunicipioSalvador;
	}

	public void setIsMunicipioSalvador(Boolean isMunicipioSalvador) {
		this.isMunicipioSalvador = isMunicipioSalvador;
	}

	public boolean isEnableEnderecoDeclarante() {
		return enableEnderecoDeclarante;
	}

	public void setEnableEnderecoDeclarante(boolean enableEnderecoDeclarante) {
		this.enableEnderecoDeclarante = enableEnderecoDeclarante;
	}

	public Boolean getShowFdbairro() {
		return showFdbairro;
	}

	public void setShowFdbairro(Boolean showFdbairro) {
		this.showFdbairro = showFdbairro;
	}

	public boolean getDesabilitarLogradouroDeclarante() {
		return !(enableEnderecoDeclarante && escolheuBairro);
	}

	public void limparCpfCnpjDocumento() {
		imovelRural.getDocumentoImovelRuralPosse().setNumCpfCnpjDeclarante(null);
	}

	public void changeTac(ValueChangeEvent event) {
		Boolean valor = (Boolean) event.getNewValue();
		if (valor && Util.isNullOuVazio(this.imovelRural.getImovelRuralTac())) {
			this.imovelRural.setImovelRuralTac(new ImovelRuralTac());
		}
	}

	public void changePrad(ValueChangeEvent event) {
		Boolean valor = (Boolean) event.getNewValue();
		if (valor && Util.isNullOuVazio(this.imovelRural.getImovelRuralPrad())) {
			this.imovelRural.setImovelRuralPrad(new ImovelRuralPrad());
		}
	}

	public boolean isArquivosShapeRppnImportados() {
		return !Util.isNullOuVazio(this.imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica())
				&& this.imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
						.getNovosArquivosShapeImportados();
	}

	/**
	 * Método para verificar se o {@link ImovelRuralRppn} foi instanciado
	 *
	 * @author eduardo.fernandes
	 * @return !Util.isNullOuVazio(this.imovelRuralRppn);
	 */
	public boolean isImovelRuralRppnInicializado() {
		return !Util.isNullOuVazio(this.imovelRural.getIdeImovelRuralRppn());
	}

	/**
	 * Método para verificar se o {@link ImovelRuralRppn} já foi persistido no banco
	 *
	 * @author eduardo.fernandes
	 * @return !Util.isNullOuVazio(this.imovelRuralRppn.getIdeImovelRuralRppn());
	 */
	public boolean isEdicaoImoveRuralRppn(ImovelRural imovelRuralP) {
		return !Util.isNullOuVazio(imovelRuralP.getIdeImovelRuralRppn().getIdeImovelRuralRppn());
	}

	public boolean getExisteTheGeomRppn() {
		try {
			if (!Util.isNullOuVazio(imovelRural.getIdeImovelRuralRppn())
					&& !Util.isNullOuVazio(imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica())) {
				if (imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
						.getNovosArquivosShapeImportados()) {
					return true;
				} else if (!imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getLocalizacaoExcluida())
					return imovelRuralServiceFacade.verificaSeExisteTheGeomValido(imovelRural.getIdeImovelRuralRppn()
							.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			} else if (!Util.isNullOuVazio(imovelRural.getIdeImovelRuralRppn())
					&& imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica() != null
					&& imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
							.getIdeLocalizacaoGeografica() == null) {
				if (imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados())
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return false;
		}
		return false;
	}

	public Boolean getMostrarBotaoUploadShapeRppn() {
		if (Util.isNullOuVazio(imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getListArquivosShape())
				|| imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getListArquivosShape()
						.size() < 4) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Método que carrega o {@link ImovelRuralRppn} e, quando não nulo, seta a sua
	 * {@link LocalizacaoGeografica} na LocalizacaoGeograficaGenericController
	 *
	 * @author eduardo.fernandes
	 * @return {@link ImovelRuralRppn}
	 */
	public ImovelRuralRppn carregarImovelRuralRppn(ImovelRural imovelRuralP) {
		try {
			ImovelRuralRppn imovelRuralRppn = imovelRuralServiceFacade
					.carregarImovelRuralRppnByIdeImovelRural(imovelRuralP);
			return imovelRuralRppn;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return null;
		}
	}

	public void salvarRppn() {
		try {
			if (!Util.isNull(imovelRural.getIdeImovelRuralRppn())
					&& !Util.isNull(imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica())) {
				if (imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
						.isShapeFile()
						&& !habilitaSalvarShape(imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica())
						&& !getExisteTheGeomRppn()) {
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_arquivos_shape_prj"));
					return;
				} else if (imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
						.isDesenho()
						&& !temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.RPPN.getId())
						&& !getExisteTheGeomRppn()) {
					JsfUtil.addErrorMessage(
							Util.getString("msg_generica_preenchimento_obrigatorio_localizacao_geografica_poligonal"));
					return;
				}
			}

			StringBuilder msg = new StringBuilder();
			if (isEdicaoImoveRuralRppn(this.imovelRural)) {
				msg.append("Alteração efetuada com sucesso.");
			} else {
				registrarCriacaoLocalizacaoGeografica(
						imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica());
				msg.append("Inclusão efetuada com sucesso.");
			}
			if (temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.RPPN.getId())) {
				persistirShapesRppn();
			}
			if (!imovelRural.getIsFinalizado()) {
				persistirImovelRppn();
			}
			JsfUtil.addSuccessMessage(msg.toString());
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void registrarCriacaoLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		localizacaoGeografica.setDtcCriacao(new Date());
		localizacaoGeografica.setDtcExclusao(null);
		localizacaoGeografica.setIndExcluido(Boolean.FALSE);
	}

	public void persistirShapesRppn() throws Exception {
		String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
				+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.RPPN.getId()
				+ FileUploadUtil.getFileSeparator();
		String nome = "shapeRPPN";
		String nomeNovo = imovelRural.getIdeImovelRural().toString() + "_"
				+ imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid();

		try {
			imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(
					retornaClassificacaoSecaoGeometricaSelecionado(imovelRural.getIdeImovelRuralRppn()
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
					.setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(imovelRural.getIdeImovelRuralRppn()
							.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));

			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nome, nomeNovo);

			String[] retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					imovelRural.getIdeImovelRural(),
					imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.RPPN.getId(),
					imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()
							.getSrid(),
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
							.getCoordGeobahiaMunicipio());

			if (retorno == null || retorno.length < 2) {
				throw new Exception("Erro ao persistir Localização Geográfica da RPPN");
			} else if (retorno[0].equalsIgnoreCase("ERRO")) {
				throw new Exception(retorno[2] + " [" + retorno[1] + "]");
			}

			String geometriaRppn = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
					imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.RPPN.getId(), null);

			if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaRppn)) {
				throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
			}

			imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
			imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
					.setListArquivosShape(new ArrayList<String>());
		} catch (Exception e) {
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nomeNovo, nome);
			throw new Exception(e.getMessage());
		}
	}

	private void persistirImovelRppn() throws Exception {
		if (imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getLocalizacaoExcluida()) {
			if (imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
					.getIdeLocalizacaoGeografica() != null) {
				imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(imovelRural.getIdeImovelRuralRppn()
						.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(null);
			}
			imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setLocalizacaoExcluida(false);
		}

		LocalizacaoGeografica temp = null;
		if (!Util
				.isNull(imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao())) {
			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(
					imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica());
		} else {
			temp = imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica();
			imovelRural.getIdeImovelRuralRppn().setIdeLocalizacaoGeografica(null);
		}

		if (!isEdicaoImoveRuralRppn(this.imovelRural)) {
			this.imovelRural.getIdeImovelRuralRppn().setIdeImovelRural(this.imovelRural);
			imovelRuralServiceFacade.salvarImovelRuralRppn(this.imovelRural.getIdeImovelRuralRppn());
			auditoria.salvar(this.imovelRural.getIdeImovelRuralRppn());
		} else {
			ImovelRuralRppn imovelRuralRppnAtual = carregarImovelRuralRppn(this.imovelRural);
			imovelRuralServiceFacade.atualizarImovelRuralRppn(this.imovelRural.getIdeImovelRuralRppn());
			auditoria.atualizar(imovelRuralRppnAtual, this.imovelRural.getIdeImovelRuralRppn());
		}
		if (temp != null) {
			imovelRural.getIdeImovelRuralRppn().setIdeLocalizacaoGeografica(temp);
		}
		if (!Util.isNull(this.imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
				.getNovosArquivosShapeImportados())
				&& this.imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
						.getNovosArquivosShapeImportados()) {
			FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(
					this.imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica(), "13",
					this.imovelRural.getIdeImovelRural().toString());
			imovelRuralServiceFacade
					.persistirShapes(this.imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica(), null);
			this.imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
					.setNovosArquivosShapeImportados(false);
		}
	}

	public void importarShapeRppn(FileUploadEvent event) {
		String caminhoArquivo;

		try {
			if (Util.isNullOuVazio(
					imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getListArquivosShape())) {
				imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
						.setListArquivosShape(new ArrayList<String>());
			}

			if (imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getListArquivosShape().size() > 3) {
				JsfUtil.addWarnMessage("Não é possível enviar mais de 4 arquivos.");
			} else {
				caminhoArquivo = FileUploadUtil.EnviarShape(
						event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
								+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.RPPN.getId(),
						"shapeRPPN");

				if (imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getListArquivosShape()
						.contains(FileUploadUtil.getFileName(caminhoArquivo))) {
					JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
				} else {
					imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getListArquivosShape()
							.add(FileUploadUtil.getFileName(caminhoArquivo));

					if (caminhoArquivo.contains(".prj")) {
						imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
								.setIdeSistemaCoordenada(imovelRuralServiceFacade.obterSistCoordPRJ(caminhoArquivo));

						if (Util.isNull(imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica()
								.getIdeSistemaCoordenada())) {
							JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_srid_prj"));
							FileUploadUtil.removerArquivos(new File(caminhoArquivo));
							imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getListArquivosShape()
									.remove(FileUploadUtil.getFileName(caminhoArquivo));
							return;
						}
					}

					if (imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getListArquivosShape()
							.size() == 4) {
						RequestContext.getCurrentInstance().execute("dlgUploadShapeRppn.hide()");
					}
					JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("geral_msg_erro_envio_arquivo"));
		}
	}

	private void excluirImovelRuralRppn() throws Exception {
		if (Util.isNullOuVazio(this.imovelRural.getIndRppn()) || !this.imovelRural.getIndRppn()) {
			if (isImovelRuralRppnInicializado() && isEdicaoImoveRuralRppn(this.imovelRural)) {
				LocalizacaoGeografica locGeo = this.imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica();
				imovelRuralServiceFacade.excluirImovelRuralRppnByIdeImovelRural(this.imovelRural);
				imovelRuralServiceFacade.excluirLocalizacaoGeografica(locGeo);
			}
			this.imovelRural.setIdeImovelRuralRppn(null);
		}
	}

	/**
	 * Método genérico para retornar o nome do Dado Específico, usado em
	 * {@link #validaTheGeom(LocalizacaoGeografica, Double, Integer)}
	 *
	 * @author eduardo.fernandes
	 * @param numPasta
	 * @return StringBuilder
	 */
	private StringBuilder retonaObjetoFilhoDoImovelRural(Integer numPasta) {
		StringBuilder objetoReferenciado = new StringBuilder();
		switch (numPasta) {
		case 13:
			return objetoReferenciado.append("RPPN");
		default:
			return null;
		}
	}

	/**
	 * Método genérico que faz a validação do TheGeom presente na
	 * {@link LocalizacaoGeografica} e verifica a área do Dado Especifíco com a
	 * presente no Shape
	 *
	 * @author eduardo.fernandes
	 * @param localizacaoGeografica
	 * @param valArea
	 * @param numPasta
	 * @return true or false
	 * @throws Exception
	 */
	private boolean validaTheGeom(LocalizacaoGeografica localizacaoGeografica, Double valArea, Integer numPasta)
			throws Exception {
		if (!isUsuarioSemRestricao()) {
			StringBuilder objetoReferenciado = retonaObjetoFilhoDoImovelRural(numPasta);
			try {
				// Se a geometria do limite do imóvel não estiver carregada, obtem
				// através do arquivo shape temporário ou diretamente do banco
				if (Util.isNullOuVazio(geometriaIm)) {
					if (imovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
						geometriaIm = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
								imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), null);
					} else {
						geometriaIm = imovelRuralServiceFacade.buscarGeometriaShape(
								imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
					}
				}

				if (Util.isNull(localizacaoGeografica)) {
					JsfUtil.addErrorMessage("Não existe localização geográfica do(a) " + objetoReferenciado + ".");
					return false;
				}
				/*
				 * if(!getExisteTheGeom()){
				 * JsfUtil.addErrorMessage("Favor importar o shapefile do(a) "
				 * +objetoReferenciado+"."); return false; }
				 */
				else {
					// Obtem a geometria da Localização Geográfica através do
					// arquivo shape temporário ou diretamente do banco
					String geometriaLoc = null;

					if (localizacaoGeografica.getNovosArquivosShapeImportados()) {
						geometriaLoc = imovelRuralServiceFacade
								.buscarGeometriaShapeTemporario(imovelRural.getIdeImovelRural(), numPasta, null);
					} else {
						geometriaLoc = imovelRuralServiceFacade
								.buscarGeometriaShape(localizacaoGeografica.getIdeLocalizacaoGeografica());
					}

					imovelRuralServiceFacade.validarAreaDeclaradaShapeTemporario(valArea, geometriaLoc);
					imovelRuralServiceFacade.validarLocalizacaoGeografica(null, geometriaLoc, null, geometriaIm);
				}

			} catch (AreaDeclaradaInvalidaException a) {
				JsfUtil.addErrorMessage("A área informada do(a) " + objetoReferenciado + " (" + a.getAreaDeclarada()
						+ " ha) não confere com a área do shapefile importado (" + a.getAreaCalculada() + " ha).");
				return false;
			} catch (LocalizacaoGeograficaException l) {
				JsfUtil.addErrorMessage("A geometria do(a) " + objetoReferenciado
						+ " não está dentro do Limite do Imóvel Rural cadastrado.");
				return false;
			} catch (CampoObrigatorioException c) {
				JsfUtil.addErrorMessage(c.getMessage());
				return false;
			} catch (Exception e) {
				JsfUtil.addErrorMessage(e.getMessage());
				return false;
			}
		}
		return true;
	}

	public List<FaseAssentamentoImovelRural> getListFaseAssentamentoImovelRural() {
		try {
			return imovelRuralServiceFacade.listarFaseAssentamentoImovelRural();
		} catch (Exception e) {
			JsfUtil.addWarnMessage(Util.getString("msg_generica_erro_ao_carregar") + e.getMessage());
		}
		return new ArrayList<FaseAssentamentoImovelRural>();
	}

	public String getRetornaLabelValorArea() {
		if (!Util.isNullOuVazio(imovelRural) && imovelRural.isImovelINCRA()) {
			return "Área medida pelo INCRA (ha)";
		} else {
			if (getShowPCT()) {
				return "Área do território (ha)";
			} else if (Util.isNullOuVazio(tipoVinculoImovel)
					|| Util.isNullOuVazio(tipoVinculoImovel.getIdeTipoVinculoImovel())
					|| tipoVinculoImovel.getIdeTipoVinculoImovel() == TipoVinculoImovel.TIPO_VINCULO_PROPRIETARIO) {
				return "Área registrada em cartório (ha) ";
			} else {
				return "Área do imóvel informado no documento de posse (ha)";
			}
		}
	}

	public List<AssociacaoIncra> getListAssociacoesIncra() {
		try {
			if (Util.isNullOuVazio(this.listAssociacoesIncra)) {
				this.listAssociacoesIncra = imovelRuralServiceFacade
						.listAssociacaoIncraPorImovelRural(this.imovelRural);
			}
			List<AssociacaoIncra> listAssociacaoNew = new ArrayList<AssociacaoIncra>();
			for (AssociacaoIncra ai : this.listAssociacoesIncra) {
				if (!ai.isIndExcluido()) {
					listAssociacaoNew.add(ai);
				}
			}
			return listAssociacaoNew;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return new ArrayList<AssociacaoIncra>();
	}

	public void cadastrarAssociacaoIncra() {
		this.associacaoIncraSelecionada = new AssociacaoIncra();
		this.associacaoIncraSelecionada.setIdePessoaJuridica(new PessoaJuridica());
	}

	public void cadastrarAssentadoIncra() {
		this.assentadoIncraImovelRuralSelecionado = new AssentadoIncraImovelRural();
		this.assentadoIncraImovelRuralSelecionado.setIdeAssentadoIncra(new AssentadoIncra());
		this.assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().setIdePessoaFisica(new PessoaFisica());
		this.assentadoIncraImovelRuralSelecionado
				.setAssociacaoAssentadoImovelRuralIncraCollection(new ArrayList<AssociacaoAssentadoImovelRuralIncra>());
		this.listAssociacaoAssentadoImovelRuralIncraSelecionado = new ArrayList<AssociacaoAssentadoImovelRuralIncra>();
	}

	public void editarAssentadoIncra() {
		this.assentadoIncraImovelRuralSelecionado.setIndEdicao(true);
		this.assentadoIncraImovelRuralSelecionado.setIndVisualizacao(false);
		if (Util.isNullOuVazio(
				assentadoIncraImovelRuralSelecionado.getAssociacaoAssentadoImovelRuralIncraCollection())) {
			this.listAssociacaoAssentadoImovelRuralIncraSelecionado = new ArrayList<AssociacaoAssentadoImovelRuralIncra>();
		} else {
			this.listAssociacaoAssentadoImovelRuralIncraSelecionado = new ArrayList<AssociacaoAssentadoImovelRuralIncra>(
					assentadoIncraImovelRuralSelecionado.getAssociacaoAssentadoImovelRuralIncraCollection());
		}
	}

	public void visualizarAssentadoIncra() {
		this.assentadoIncraImovelRuralSelecionado.setIndEdicao(false);
		this.assentadoIncraImovelRuralSelecionado.setIndVisualizacao(true);
		this.listAssociacaoAssentadoImovelRuralIncraSelecionado = new ArrayList<AssociacaoAssentadoImovelRuralIncra>(
				assentadoIncraImovelRuralSelecionado.getAssociacaoAssentadoImovelRuralIncraCollection());
	}

	public AssociacaoIncra getAssociacaoIncraSelecionada() {
		if (associacaoIncraSelecionada == null) {
			cadastrarAssociacaoIncra();
		}
		return associacaoIncraSelecionada;
	}

	public void setAssociacaoIncraSelecionada(AssociacaoIncra associacaoIncra) {
		this.associacaoIncraSelecionada = associacaoIncra;
	}

	public List<AssentadoIncra> getListAssentadosIncraPorAssociacao() {
		try {
			if (!Util.isNull(getAssociacaoIncraSelecionada())
					&& !Util.isNull(this.getAssociacaoIncraSelecionada().getIdePessoaJuridica())) {
				if (!Util.isNull(this.getAssociacaoIncraSelecionada().getIdePessoaJuridica().getNumCnpj())) {
					if (!Util.isNullOuVazio(this.imovelRural.getAssentadoIncraImovelRuralCollection())) {
						List<AssentadoIncra> listAssentado = new ArrayList<AssentadoIncra>();
						for (AssentadoIncraImovelRural assentadoIncraImovel : this.imovelRural
								.getAssentadoIncraImovelRuralCollection()) {
							if (!assentadoIncraImovel.isIndExcluido()) {
								if (!Util.isNullOuVazio(
										assentadoIncraImovel.getAssociacaoAssentadoImovelRuralIncraCollection())) {
									for (AssociacaoAssentadoImovelRuralIncra associacaoAssentado : assentadoIncraImovel
											.getAssociacaoAssentadoImovelRuralIncraCollection()) {
										if (associacaoAssentado.getIdeAssociacaoIncraImovelRural()
												.getIdeAssociacaoIncra().getIdePessoaJuridica().getNumCnpj()
												.equals(this.getAssociacaoIncraSelecionada().getIdePessoaJuridica()
														.getNumCnpj())) {
											listAssentado.add(assentadoIncraImovel.getIdeAssentadoIncra());
										}
									}
								}
							}
						}
						return listAssentado;
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return new ArrayList<AssentadoIncra>();
	}

	public void excluirAssociacaoIncra() {
		try {
			if (Util.isNull(this.getAssociacaoIncraSelecionada().getIdeAssociacaoIncra())) {
				this.listAssociacoesIncra.remove(this.getAssociacaoIncraSelecionada());
			}

			this.getAssociacaoIncraSelecionada().setIndExcluido(true);
			if (!imovelRural.getIsFinalizado()) {
				if (Util.isNullOuVazio(this.imovelRuralServiceFacade.listAssentadosIncraDoImovelRuralPorAssociacao(
						this.getAssociacaoIncraSelecionada(), this.imovelRural))) {
					persistirExclusaoAssociacaoIncra();
				}
				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_associacao_com_assentado"));
				return;
			}
			JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void excluirAssentadoIncraImovelRural() {
		try {
			if (Util.isNull(this.assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncraImovelRural())) {
				imovelRural.getAssentadoIncraImovelRuralCollection().remove(this.assentadoIncraImovelRuralSelecionado);
			} else {
				this.assentadoIncraImovelRuralSelecionado.setIndExcluido(true);
				if (!imovelRural.getIsFinalizado()) {
					persistirExclusaoAssentadoIncraImovelRural();
				}
			}
			Html.atualizar("formCadImovelRural:tabAbasDadosEspecificos:tableAssentadoIncra");
			JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void excluirAssociacaoAssentadoImovelRuralIncra() {
		if (!Util.isNullOuVazio(associacaoAssentadoImovelRuralIncraSelecionado)) {
			listAssociacaoAssentadoImovelRuralIncraSelecionado.remove(associacaoAssentadoImovelRuralIncraSelecionado);
		}
	}

	private void persistirExclusaoAssociacaoIncra() throws Exception {
		imovelRuralServiceFacade.excluirAssocicao(this.getAssociacaoIncraSelecionada(), this.imovelRural);
	}

	private void persistirExclusaoAssentadoIncraImovelRural() throws Exception {
		imovelRuralServiceFacade.excluirAssentadoIncraImovelRural(assentadoIncraImovelRuralSelecionado);
	}

	public void salvarAssociacaoIncra() {
		try {
			validarAssociacaoRepetida();
			if (!imovelRural.getIsFinalizado()) {
				persistirAssociacaoIncra();
			}
			if (!this.getAssociacaoIncraSelecionada().isIndEdicao()) {
				this.listAssociacoesIncra.add(this.getAssociacaoIncraSelecionada());
			}
			RequestContext.getCurrentInstance().execute("dlgCadAssociacaoIncra.hide()");
			JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S001"));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void salvarAssentadoIncraImovelRural() {
		try {
			String msgSucesso = "";

			if (Util.isNull(imovelRural.getAssentadoIncraImovelRuralCollection())) {
				imovelRural.setAssentadoIncraImovelRuralCollection(new ArrayList<AssentadoIncraImovelRural>());
			}

			if (!this.assentadoIncraImovelRuralSelecionado.isIndEdicao()) {
				validarAssentadoRepetido();
			}
			this.assentadoIncraImovelRuralSelecionado.setIdeImovelRural(imovelRural);
			if (this.assentadoIncraImovelRuralSelecionado.getLatitude() > 0) {
				this.assentadoIncraImovelRuralSelecionado
						.setLatitude(-this.assentadoIncraImovelRuralSelecionado.getLatitude());
			}
			if (this.assentadoIncraImovelRuralSelecionado.getLongitude() > 0) {
				this.assentadoIncraImovelRuralSelecionado
						.setLongitude(-this.assentadoIncraImovelRuralSelecionado.getLongitude());
			}

			if (this.assentadoIncraImovelRuralSelecionado.isIndEdicao()) {
				if (!imovelRural.getIsFinalizado()) {
					persistirExclusaoAssentadoIncraImovelRural();
					this.assentadoIncraImovelRuralSelecionado.setIdeAssentadoIncraImovelRural(null);
				}
			}

			if (!Util.isNull(this.imovelRural.getIdeLocalizacaoGeograficaLote())) {
				if (!Util.isNull(assentadoIncraImovelRuralSelecionado.getLatitude())
						&& assentadoIncraImovelRuralSelecionado.getLatitude() != 0
						&& !Util.isNull(assentadoIncraImovelRuralSelecionado.getLongitude())
						&& assentadoIncraImovelRuralSelecionado.getLongitude() != 0) {
					List<AssentadoIncraImovelRural> list = new ArrayList<AssentadoIncraImovelRural>();
					list.add(assentadoIncraImovelRuralSelecionado);
					validarAssentadoLote(list);
				}
			}

			this.assentadoIncraImovelRuralSelecionado.setAssociacaoAssentadoImovelRuralIncraCollection(
					new ArrayList<AssociacaoAssentadoImovelRuralIncra>(
							listAssociacaoAssentadoImovelRuralIncraSelecionado));
			if (!imovelRural.getIsFinalizado()) {
				persistirAssentadoIncraImovelRural();
			}
			if (!this.assentadoIncraImovelRuralSelecionado.isIndEdicao()) {
				this.imovelRural.getAssentadoIncraImovelRuralCollection()
						.add(this.assentadoIncraImovelRuralSelecionado);
				msgSucesso = "cefir_msg_S001";
			} else {
				msgSucesso = "cefir_msg_S002";
			}

			this.listAssociacaoAssentadoImovelRuralIncraSelecionado = null;
			RequestContext.getCurrentInstance().execute("dlgCadAssentadoIncra.hide()");
			JsfUtil.addSuccessMessage(Util.getString(msgSucesso));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void validarAssociacaoRepetida() throws Exception {
		if (!Util.isNull(this.getAssociacaoIncraSelecionada().getIdePessoaJuridica().getNumCnpj())
				&& !isUsuarioSemRestricao()) {
			for (AssociacaoIncra associacaoIncra : this.listAssociacoesIncra) {
				if (associacaoIncra.getIdePessoaJuridica().getNumCnpj()
						.equals(this.getAssociacaoIncraSelecionada().getIdePessoaJuridica().getNumCnpj())) {
					if (!associacaoIncra.isIndEdicao() && !associacaoIncra.isIndExcluido()) {
						throw new Exception(Util.getString("cefir_msg_associacao_cadastrada"));
					}
				}
			}
		}
	}

	private void validarAssentadoRepetido() throws Exception {
		if (!isUsuarioSemRestricao()) {
			if (!Util.isNullOuVazio(imovelRural.getAssentadoIncraImovelRuralCollection())) {
				for (AssentadoIncraImovelRural assentadoIncraImovelRural : imovelRural
						.getAssentadoIncraImovelRuralCollection()) {
					if (!assentadoIncraImovelRural.isIndExcluido()) {
						if (assentadoIncraImovelRural.getIdeAssentadoIncra().getIdePessoaFisica().getNumCpf()
								.equals(assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica()
										.getNumCpf())) {
							throw new Exception(Util.getString("cefir_msg_assentado_cadastrado"));
						}
					}
				}
			}
		}
	}

	private void persistirAssociacaoIncra() throws Exception {
		if (Util.isNull(this.getAssociacaoIncraSelecionada().getIdePessoaJuridica().getIdePessoaJuridica())) {
			this.getAssociacaoIncraSelecionada().getIdePessoaJuridica().setPessoa(new Pessoa());
			this.getAssociacaoIncraSelecionada().getIdePessoaJuridica().getPessoa().setDtcCriacao(new Date());
			this.getAssociacaoIncraSelecionada().getIdePessoaJuridica().getPessoa().setIndExcluido(false);
			this.getAssociacaoIncraSelecionada().getIdePessoaJuridica().setIdeNaturezaJuridica(new NaturezaJuridica(5));
			;
			this.imovelRuralServiceFacade
					.salvarOuAtualizarPessoaJuridica(this.getAssociacaoIncraSelecionada().getIdePessoaJuridica());
		}
		this.imovelRuralServiceFacade.salvarAssociacaoIncra(this.getAssociacaoIncraSelecionada(), this.imovelRural);
	}

	private void persistirAssentadoIncraImovelRural() throws Exception {
		if (Util.isNull(assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica()
				.getIdePessoaFisica())) {
			assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica().setPessoa(new Pessoa());
			assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica().setIdePais(new Pais(1));
			assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica().getPessoa()
					.setDtcCriacao(new Date());
			assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica().getPessoa()
					.setIndExcluido(false);
		}

		this.imovelRuralServiceFacade.salvarOuAtualizarPessoaFisica(
				assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica());
		this.imovelRuralServiceFacade.salvarAssentadoIncra(assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra());
		this.imovelRuralServiceFacade.salvarAssentadoIncraImovelRural(assentadoIncraImovelRuralSelecionado);

	}

	public boolean getHabilitaCadastroAssociacaoIncra() {
		if (Util.isNull(this.getAssociacaoIncraSelecionada())) {
			return false;
		}
		if (!Util.isNull(this.getAssociacaoIncraSelecionada().getIdePessoaJuridica())
				&& !Util.isNull(this.getAssociacaoIncraSelecionada().getIdePessoaJuridica().getIdePessoaJuridica())) {
			return true;
		}
		return false;
	}

	public boolean getHabilitaCadastroAssentadoIncra() {
		if (Util.isNull(assentadoIncraImovelRuralSelecionado)
				|| Util.isNull(assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra())) {
			return false;
		}
		if (!Util.isNull(assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica())) {
			return true;
		}
		return false;
	}

	public boolean getHabilitaEdicaoNomeMaeAssentadoIncra() {
		if (getHabilitaCadastroAssentadoIncra() && Util.isNullOuVazio(
				assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica().getNomMae())) {
			return true;
		}
		return false;
	}

	public boolean getHabilitaEdicaoDataNascimentoAssentadoIncra() {
		if (getHabilitaCadastroAssentadoIncra() && Util.isNull(
				assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica().getDtcNascimento())) {
			return true;
		}
		return false;
	}

	public void carregarAssociacao() {
		try {
			PessoaJuridica pJ = this.imovelRuralServiceFacade
					.filtrarPessoaJuridicaByCnpj(this.getAssociacaoIncraSelecionada().getIdePessoaJuridica());
			if (Util.isNull(pJ)) {
				pJ = new PessoaJuridica(this.getAssociacaoIncraSelecionada().getIdePessoaJuridica().getNumCnpj());
			} else {
				AssociacaoIncra aI = this.imovelRuralServiceFacade.carregarAssociacaoIncraPorPessoaJuridica(pJ);
				if (!Util.isNull(aI)) {
					this.setAssociacaoIncraSelecionada(aI);
					return;
				}
			}
			this.getAssociacaoIncraSelecionada().setIdePessoaJuridica(pJ);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void carregarAssentado() {
		try {
			if (!Util.isNullOuVazio(
					assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica().getNumCpf())) {
				PessoaFisica pF = this.imovelRuralServiceFacade.filtrarPessoaFisicaByCpf(
						assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica());
				if (!Util.isNull(pF)) {
					AssentadoIncra aI = this.imovelRuralServiceFacade.carregarAssentadoIncraPorPessoaFisica(pF);
					if (!Util.isNull(aI)) {
						assentadoIncraImovelRuralSelecionado.setIdeAssentadoIncra(aI);
					} else {
						assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().setIdePessoaFisica(pF);
					}
				}
			} else {
				JsfUtil.addErrorMessage(
						Util.getString("javax.faces.component.UIInput.REQUIRED", Util.getString("geral_lbl_cpf")));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void uploadAssentadosIncra(FileUploadEvent event) {
		String nmArquivo = imovelRural.toString() + "_" + event.getFile().getFileName();
		String caminhoArquivo = FileUploadUtil.EnviarShape(event,
				DiretorioArquivoEnum.IMOVELRURAL.toString() + FileUploadUtil.getFileSeparator()
						+ this.imovelRural.getIdeImovelRural().toString(),
				this.imovelRural.getIdeImovelRural().toString() + "_" + nmArquivo);
		try {
			List<AssentadoIncraImovelRural> listAssentadosPlanilha = this.imovelRuralServiceFacade
					.carregarAssentadosDaPlanilha(caminhoArquivo);
			if (Util.isNullOuVazio(listAssentadosPlanilha)) {
				JsfUtil.addSuccessMessage(Util.getString("cefir_msg_A012"));
			}
			try {
				for (AssentadoIncraImovelRural assentadoIncraImovelRural : listAssentadosPlanilha) {
					this.assentadoIncraImovelRuralSelecionado = assentadoIncraImovelRural;
					carregarAssentado();
					validarAssentadoRepetido();
					assentadoIncraImovelRuralSelecionado.setIdeImovelRural(imovelRural);
				}
			} catch (Exception e) {
				JsfUtil.addErrorMessage(e.getMessage() + " [linha: "
						+ (listAssentadosPlanilha.indexOf(this.assentadoIncraImovelRuralSelecionado) + 1) + "]");
				return;
			}

			validarAssentadoLote(listAssentadosPlanilha);
			imovelRural.getAssentadoIncraImovelRuralCollection().addAll(listAssentadosPlanilha);

			if (!imovelRural.getIsFinalizado()) {
				for (AssentadoIncraImovelRural assentadoIncraImovelRural : imovelRural
						.getAssentadoIncraImovelRuralCollection()) {
					if (assentadoIncraImovelRural.isIndExcluido()) {
						continue;
					}
					this.assentadoIncraImovelRuralSelecionado = assentadoIncraImovelRural;
					persistirAssentadoIncraImovelRural();
				}

			}
			RequestContext.getCurrentInstance().execute("dlgUploadListaAssentados.hide()");
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void validarAssentadoLote(List<AssentadoIncraImovelRural> listAssentadosPlanilha) throws Exception {
		if (!isUsuarioSemRestricao()) {
			String geometria = null;
			if (!Util.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeograficaLote())) {
				if (this.imovelRural.getIdeLocalizacaoGeograficaLote().getNovosArquivosShapeImportados()) {
					geometria = this.imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
							imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LOTE_ASSENTAMENTO.getId(), null);
				} else if (this.imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica() != null
						&& existeTheGeomByIdeLocGeo(
								this.imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica())) {
					geometria = "(SELECT st_multi(st_collect(the_geom)) FROM dado_geografico WHERE ide_localizacao_geografica = "
							+ this.imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica() + ")";
				}
			}

			if (Util.isNullOuVazio(geometria)) {
				for (AssentadoIncraImovelRural assentadoIncraImovelRural : listAssentadosPlanilha) {
					assentadoIncraImovelRural.setLatitude(null);
					assentadoIncraImovelRural.setLongitude(null);
				}
				return;
			}

			List<AssentadoIncraImovelRural> listAssentados = obterAssentadosParaValidacao();
			listAssentadosPlanilha.addAll(0, listAssentados);

			this.imovelRuralServiceFacade.validarAssentadoLote(listAssentadosPlanilha, this.imovelRural, geometria);
		}
	}

	private List<AssentadoIncraImovelRural> obterAssentadosParaValidacao() {
		List<AssentadoIncraImovelRural> listAssentados = new ArrayList<AssentadoIncraImovelRural>();
		if (!Util.isNullOuVazio(imovelRural.getAssentadoIncraImovelRuralCollection())) {
			for (AssentadoIncraImovelRural assentadoIncraImovelRural : imovelRural
					.getAssentadoIncraImovelRuralCollection()) {
				if (assentadoIncraImovelRural.isIndExcluido()) {
					continue;
				}
				if (!assentadoIncraImovelRural.getIdeAssentadoIncra().getIdePessoaFisica().getNumCpf()
						.equals(this.assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica()
								.getNumCpf())) {
					if (!Util.isNull(assentadoIncraImovelRural.getLatitude())
							&& !Util.isNull(assentadoIncraImovelRural.getLongitude())) {
						if (assentadoIncraImovelRural.getLatitude() < 0
								&& assentadoIncraImovelRural.getLongitude() < 0) {
							listAssentados.add(assentadoIncraImovelRural);
						}
					}
				}
			}
		}
		return listAssentados;
	}

	public void validarCamposPontoAssentado(List<String> messages) {
		if (!isUsuarioSemRestricao()) {
			if (Util.isNullOuVazio(fracaoGrauLatitudeLoc)) {
				messages.add("O campo 'Latitude' é de preenchimento obrigatório.");
			}
			if (Util.isNullOuVazio(fracaoGrauLongitudeLoc)) {
				messages.add("O campo 'Longitude' é de preenchimento obrigatório.");
			}
		}
	}

	public void limparFormIncluirAssociacao() {
		this.associacaoIncraSelecionada = new AssociacaoIncra();
		this.associacaoIncraSelecionada.setIdePessoaJuridica(new PessoaJuridica());
	}

	public void inserirAssociacaoIncraAssentado() {
		try {
			if (!Util.isNullOuVazio(associacaoIncraSelecionada) && validarAssossiacaoIncluida()) {
				AssociacaoAssentadoImovelRuralIncra associacaoAssentadoImovelRuralIncra = new AssociacaoAssentadoImovelRuralIncra();
				associacaoAssentadoImovelRuralIncra
						.setIdeAssentadoIncraImovelRural(assentadoIncraImovelRuralSelecionado);

				AssociacaoIncra associacaoIncra = retornaAssociacaoIncraSelecionada(associacaoIncraSelecionada);

				AssociacaoIncraImovelRural associacaoIncarImovelRural = imovelRuralServiceFacade
						.buscarAssociacaoIncraImovelRural(associacaoIncra, imovelRural);
				if (Util.isNull(associacaoIncarImovelRural)) {
					associacaoIncarImovelRural = new AssociacaoIncraImovelRural();
					associacaoIncarImovelRural.setIdeAssociacaoIncra(associacaoIncra);
				}
				associacaoAssentadoImovelRuralIncra.setIdeAssociacaoIncraImovelRural(associacaoIncarImovelRural);
				listAssociacaoAssentadoImovelRuralIncraSelecionado.add(associacaoAssentadoImovelRuralIncra);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	/**
	 * método utilizado para validar se já existe associação incra com o mesmo cnpj
	 * na lista.
	 * 
	 * @return boolean
	 */
	private boolean validarAssossiacaoIncluida() {

		if (!Util.isNullOuVazio(this.listAssociacaoAssentadoImovelRuralIncraSelecionado)) {

			for (AssociacaoAssentadoImovelRuralIncra assentadoImovelRuralIncra : this.listAssociacaoAssentadoImovelRuralIncraSelecionado) {

				if (associacaoIncraSelecionada.getIdePessoaJuridica().getNumCnpj()
						.equals(assentadoImovelRuralIncra.getIdeAssociacaoIncraImovelRural().getIdeAssociacaoIncra()
								.getIdePessoaJuridica().getNumCnpj())) {
					JsfUtil.addWarnMessage("Associação já foi incluída.");
					return false;
				}
			}
		}

		return true;
	}

	private AssociacaoIncra retornaAssociacaoIncraSelecionada(AssociacaoIncra associacaoIncra) {
		AssociacaoIncra associacaoSelecionada = new AssociacaoIncra();
		for (AssociacaoIncra associacao : this.listAssociacoesIncra) {
			if (associacao.getIdePessoaJuridica().getNumCnpj()
					.equals(associacaoIncra.getIdePessoaJuridica().getNumCnpj())) {
				associacaoSelecionada = associacao;
			}
		}
		return associacaoSelecionada;
	}

	public AssociacaoAssentadoImovelRuralIncra getAssociacaoAssentadoImovelRuralIncraSelecionado() {
		return associacaoAssentadoImovelRuralIncraSelecionado;
	}

	public void setAssociacaoAssentadoImovelRuralIncraSelecionado(
			AssociacaoAssentadoImovelRuralIncra associacaoAssentadoImovelRuralIncraSelecionado) {
		this.associacaoAssentadoImovelRuralIncraSelecionado = associacaoAssentadoImovelRuralIncraSelecionado;
	}

	public AssentadoIncraImovelRural getAssentadoIncraImovelRuralSelecionado() {
		if (!Util.isNull(this.assentadoIncraImovelRuralSelecionado)) {
			if (!Util.isNull(this.assentadoIncraImovelRuralSelecionado.getLatitude())
					&& !Util.isNull(this.assentadoIncraImovelRuralSelecionado.getLongitude())) {
				if (this.assentadoIncraImovelRuralSelecionado.getLatitude() < 0) {
					this.assentadoIncraImovelRuralSelecionado
							.setLatitude(-this.assentadoIncraImovelRuralSelecionado.getLatitude());
				}
				if (this.assentadoIncraImovelRuralSelecionado.getLongitude() < 0) {
					this.assentadoIncraImovelRuralSelecionado
							.setLongitude(-this.assentadoIncraImovelRuralSelecionado.getLongitude());
				}
			}
		}
		return assentadoIncraImovelRuralSelecionado;
	}

	public void setAssentadoIncraImovelRuralSelecionado(
			AssentadoIncraImovelRural assentadoIncraImovelRuralSelecionado) {
		this.assentadoIncraImovelRuralSelecionado = assentadoIncraImovelRuralSelecionado;
	}

	public List<AssentadoIncraImovelRural> getListAssentadoIncraImovelRural() {
		List<AssentadoIncraImovelRural> listTmp = new ArrayList<AssentadoIncraImovelRural>();
		if (!Util.isLazyInitExcepOuNull(imovelRural.getAssentadoIncraImovelRuralCollection())
				&& !Util.isNullOuVazio(imovelRural.getAssentadoIncraImovelRuralCollection())) {
			for (AssentadoIncraImovelRural assentado : imovelRural.getAssentadoIncraImovelRuralCollection()) {
				if (!assentado.isIndExcluido()) {
					listTmp.add(assentado);
				}
			}
		}
		return listTmp;
	}

	public List<AssociacaoAssentadoImovelRuralIncra> getListAssociacaoAssentadoImovelRuralIncraSelecionado() {
		List<AssociacaoAssentadoImovelRuralIncra> list = new ArrayList<AssociacaoAssentadoImovelRuralIncra>();
		if (!Util.isNullOuVazio(listAssociacaoAssentadoImovelRuralIncraSelecionado)) {
			for (AssociacaoAssentadoImovelRuralIncra a : listAssociacaoAssentadoImovelRuralIncraSelecionado) {
				if (!a.getIdeAssociacaoIncraImovelRural().getIdeAssociacaoIncra().isIndExcluido()) {
					list.add(a);
				}
			}
		}
		return list;
	}

	public void setListAssociacaoAssentadoImovelRuralIncraSelecionado(
			List<AssociacaoAssentadoImovelRuralIncra> listAssociacaoAssentadoImovelRuralIncraSelecionado) {
		this.listAssociacaoAssentadoImovelRuralIncraSelecionado = listAssociacaoAssentadoImovelRuralIncraSelecionado;
	}

	private boolean isRequerenteIncra() {
		Pessoa requerente = new Pessoa();
		if (!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(imovelRural.getIdeRequerenteCadastro())) {
			requerente = imovelRural.getIdeRequerenteCadastro();
		} else {
			if (ContextoUtil.getContexto().getIsProcPfPjOuRepLegal()) {
				requerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa();
			}
		}

		if (Util.isNullOuVazio(requerente)) {
			return false;
		}

		for (INCRAEnum incarEnum : INCRAEnum.values()) {
			if (requerente.getCpfCnpj().equals(incarEnum.getNumeroCnpj())) {
				return true;
			}
		}
		return false;
	}

	private void excluirPontosDosAssentados() throws Exception {
		for (AssentadoIncraImovelRural a : this.getListAssentadoIncraImovelRural()) {
			a.setLatitude(null);
			a.setLongitude(null);
			assentadoIncraImovelRuralSelecionado = a;
			if (!imovelRural.getIsFinalizado()) {
				persistirAssentadoIncraImovelRural();
			}
		}
	}

	public void importarShapeLotes(FileUploadEvent event) {
		String caminhoArquivo;
		if (Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote().getListArquivosShape()))
			imovelRural.getIdeLocalizacaoGeograficaLote().setListArquivosShape(new ArrayList<String>());
		if (imovelRural.getIdeLocalizacaoGeograficaLote().getListArquivosShape().size() < 3) {
			if ((!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()) && Util
					.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid()))
					|| Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
				imovelRural.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(selecionarSistemaCoordenada(
						imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()));
			}

			caminhoArquivo = FileUploadUtil.EnviarShape(event,
					DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural().toString() + "_"
							+ TemaGeoseiaEnum.LOTE_ASSENTAMENTO.getId(),
					imovelRural.getIdeImovelRural().toString() + "_"
							+ imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid());

			if (imovelRural.getIdeLocalizacaoGeograficaLote().getListArquivosShape()
					.contains(FileUploadUtil.getFileName(caminhoArquivo)))
				JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
			else {
				imovelRural.getIdeLocalizacaoGeograficaLote().getListArquivosShape()
						.add(FileUploadUtil.getFileName(caminhoArquivo));
				if (imovelRural.getIdeLocalizacaoGeograficaLote().getListArquivosShape().size() == 3)
					RequestContext.getCurrentInstance().execute("dlgUploadShapeLote.hide()");
				JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
			}
		} else
			JsfUtil.addWarnMessage("Não é possível enviar mais de 3 arquivos.");
	}

	public void persistirShapesLotes() throws Exception {
		String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
				+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.LOTE_ASSENTAMENTO.getId()
				+ FileUploadUtil.getFileSeparator();
		String nome = "shapeLoteAssentamento";
		String nomeNovo = imovelRural.getIdeImovelRural().toString() + "_"
				+ imovelRural.getIdeLocalizacaoGeograficaLote().getIdeSistemaCoordenada().getSrid();

		try {
			imovelRural.getIdeLocalizacaoGeograficaLote()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(imovelRural
							.getIdeLocalizacaoGeograficaLote().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			imovelRural.getIdeLocalizacaoGeograficaLote().setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(
					imovelRural.getIdeLocalizacaoGeograficaLote().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));

			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nome, nomeNovo);

			String[] retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					imovelRural.getIdeImovelRural(),
					imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.LOTE_ASSENTAMENTO.getId(),
					imovelRural.getIdeLocalizacaoGeograficaLote().getIdeSistemaCoordenada().getSrid(),
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
							.getCoordGeobahiaMunicipio());

			if (retorno == null || retorno.length < 2) {
				throw new Exception("Erro ao persistir Localização Geográfica do Lote de Assentamento");
			} else if (retorno[0].equalsIgnoreCase("ERRO")) {
				throw new Exception(retorno[2] + " [" + retorno[1] + "]");
			}

			String geometriaLotes = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
					imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LOTE_ASSENTAMENTO.getId(), null);

			if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaLotes)) {
				throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
			}

			imovelRural.getIdeLocalizacaoGeograficaLote().setNovosArquivosShapeImportados(true);
			imovelRural.getIdeLocalizacaoGeograficaLote().setListArquivosShape(new ArrayList<String>());
		} catch (Exception e) {
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nomeNovo, nome);
			throw new Exception(e.getMessage());
		}
	}

	private void moverArquivoShapeLocalizacaoLotesPastaTemporariaParaPastaOriginal() throws Exception {
		FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(
				this.imovelRural.getIdeLocalizacaoGeograficaLote(), "12",
				this.imovelRural.getIdeImovelRural().toString());
	}

	public boolean getExisteTheGeomLotes() {
		try {
			if (imovelRural.isImovelINCRA() && !Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote())) {
				if (imovelRural.getIdeLocalizacaoGeograficaLote().getNovosArquivosShapeImportados()) {
					return true;
				} else if (!imovelRural.getIdeLocalizacaoGeograficaLote().getLocalizacaoExcluida()) {
					return imovelRuralServiceFacade.verificaSeExisteTheGeomValido(
							imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica());
				}
			} else if (imovelRural.getIdeLocalizacaoGeograficaLote() != null
					&& imovelRural.getIdeLocalizacaoGeograficaLote().getIdeLocalizacaoGeografica() == null) {
				if (imovelRural.getIdeLocalizacaoGeograficaLote().getNovosArquivosShapeImportados())
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return false;
		}
		return false;
	}

	public Boolean getMostrarBotaoUploadShapeLote() {
		if (imovelRural.isImovelINCRA()
				&& !Util.isNullOuVazio(
						imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada())
				&& (Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeograficaLote().getListArquivosShape())
						|| imovelRural.getIdeLocalizacaoGeograficaLote().getListArquivosShape().size() < 3)) {
			return true;

		}
		return false;
	}

	// Fim Imóveis INCRA

	public boolean getExibeUploadShapeImovel() {
		if (this.getMostrarBotaoUploadShapeImovelRural() && this.imovelRural.getIdeLocalizacaoGeografica()
				.getIdeClassificacaoSecao().getIdeClassificacaoSecao() == 2 && !this.getExisteTheGeomImovelRural()) {
			return true;
		}

		if (!Util.isNull(this.imovelRural.getIdeLocalizacaoGeografica())) {
			if (!Util.isNull(this.imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao())
					&& !Util.isNull(this.imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
							.getIdeClassificacaoSecao())
					&& this.imovelRural.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
							.getIdeClassificacaoSecao() == 3
					&& !this.getExisteTheGeomImovelRural()) {
				return true;
			}

			if (!Util.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeografica().getListArquivosShape())) {
				return true;
			}
		}

		if (this.getExisteTheGeomImovelRural()) {
			return true;
		}

		return false;
	}

	public boolean getIsEdicaoCronogramaEtapa() {
		return isEdicaoCronogramaEtapa;
	}

	public void setIsEdicaoCronogramaEtapa(boolean isEdicaoCronogramaEtapa) {
		this.isEdicaoCronogramaEtapa = isEdicaoCronogramaEtapa;
	}

	public List<String> validaCamposTelaDocumentacao() {
		List<String> mensagens = new ArrayList<String>();

		if (!isUsuarioSemRestricao()) {

			if (Util.isNullOuVazio(this.imovelRural.getDocumentoImovelRuralPosse().getIdeTipoDocumentoImovelRural())
					|| Util.isNullOuVazio(this.imovelRural.getDocumentoImovelRuralPosse()
							.getIdeTipoDocumentoImovelRural().getIdeTipoDocumentoImovelRural())) {
				mensagens.add(Util.getString("cefir_msg_A001", "Tipo de Documento"));
			} else if (tipoVinculoImovel.getIdeTipoVinculoImovel()
					.equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR) && getShowPCT()
					&& Util.isNullOuVazio(this.imovelRural.getDscTermoAutodeclaracao())) {
				mensagens.add(Util.getString("cefir_msg_A001", "Termo de autorização"));
			}

			if (Util.isNull(this.imovelRural.getDocumentoImovelRuralPosse()) || Util
					.isNullOuVazio(this.imovelRural.getDocumentoImovelRuralPosse().getIdeDocumentoImovelRural())) {
				if (!this.imovelRural.isImovelCDA() && !this.imovelRural.isImovelBNDES() && !getShowPCT()) {
					mensagens.add(Util.getString("cefir_msg_documento_posse_obrigatorio"));
				}
			}

			if (getPossuiProcurador()) {
				if (Util.isNullOuVazio(this.imovelRural.getIdeDocumentoProcuracao())) {
					if (!this.imovelRural.isImovelCDA() && !this.imovelRural.isImovelBNDES() && !getShowPCT()) {
						mensagens.add(Util.getString("cefir_msg_obrigatorio_documento_procurador_imovel"));
					}
				}
			}

			if (Util.isNullOuVazio(listProprietariosImovel)
					&& tipoVinculoImovel.getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_PROPRIETARIO)
					&& !getShowPCT()) {
				mensagens.add(Util.getString("cefir_msg_cadastrar_proprietario"));
			}

			if (!Util.isNullOuVazio(listProprietariosImovel)) {
				for (PessoaImovel pessoaImovel : listProprietariosImovel) {
					if (pessoaImovel.getIdePessoa().isPF()
							&& Util.isNull(pessoaImovel.getIdePessoa().getPessoaFisica().getDtcNascimento())) {
						mensagens.add(Util.getString("cefir_msg_atualizar_proprietario"));
						break;
					}
				}
			}

		}
		return mensagens;
	}

	public List<TipoTerritorioPct> getListarTipoTerritorioPct() {
		List<TipoTerritorioPct> tipoTerritorioPcts = null;

		try {
			tipoTerritorioPcts = imovelRuralServiceFacade.listarTipoTerritorioPct();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tipoTerritorioPcts;
	}

	public void getListarTipoSeguimentoPct() {
		try {
			tipoSeguimentoPcts = new DualListModel<TipoSeguimentoPct>(
					imovelRuralServiceFacade.listarTipoSeguimentoPct(), new ArrayList<TipoSeguimentoPct>());
			ajustarSegmento();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void adicionarSegmentoPct() {
		try {
			if (!tipoSeguimentoPcts.getTarget().isEmpty()) {

				for (Iterator<TipoSeguimentoPct> iterator = tipoSeguimentoPcts.getTarget().iterator(); iterator
						.hasNext();) {
					TipoSeguimentoPct tipoSeguimentoPct = iterator.next();
					if (Util.isNullOuVazio(pctImovelRural.getTipoSeguimentoPctCollection())) {
						pctImovelRural.setTipoSeguimentoPctCollection(new ArrayList<TipoSeguimentoPct>());
					}

					pctImovelRural.getTipoSeguimentoPctCollection().add(tipoSeguimentoPct);
					iterator.remove();
				}
			}
			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");

		} catch (Exception exception) {
			exception.printStackTrace();
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void removerSegmento() {
		pctImovelRural.getTipoSeguimentoPctCollection().remove(tipoSeguimentoPctSelecionadoExclusao);

		if ("Outros".equals(tipoSeguimentoPctSelecionadoExclusao.getDscTipoSeguimentoPct())) {
			pctImovelRural.setDscTipoSeguimentoPctOutros(null);
		}

		getListarTipoSeguimentoPct();

		JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
	}

	private void ajustarSegmento() {
		if (Util.isNullOuVazio(pctImovelRural.getTipoSeguimentoPctCollection())) {
			pctImovelRural.setTipoSeguimentoPctCollection(new ArrayList<TipoSeguimentoPct>());
		}

		for (Iterator iterator = pctImovelRural.getTipoSeguimentoPctCollection().iterator(); iterator.hasNext();) {
			TipoSeguimentoPct tipoSeguimentoPct = (TipoSeguimentoPct) iterator.next();

			tipoSeguimentoPcts.getSource().remove(tipoSeguimentoPct);
		}
	}

	public Boolean getAceiteCondicoesRecuperacaoOp() {
		return aceiteCondicoesRecuperacaoOp;
	}

	public void setAceiteCondicoesRecuperacaoOp(Boolean aceiteCondicoesRecuperacaoOp) {
		this.aceiteCondicoesRecuperacaoOp = aceiteCondicoesRecuperacaoOp;
	}

	public boolean isRenderPollProprietarios() {
		return renderPollProprietarios;
	}

	public void setRenderPollProprietarios(boolean renderPollProprietarios) {
		this.renderPollProprietarios = renderPollProprietarios;
	}

	public String getTituloProprietariosJustoPossuidores() {
		return tituloProprietariosJustoPossuidores;
	}

	public void setTituloProprietariosJustoPossuidores(String tituloProprietariosJustoPossuidores) {
		this.tituloProprietariosJustoPossuidores = tituloProprietariosJustoPossuidores;
	}

	public String getLblListaProprietariosJustoPossuidores() {
		return lblListaProprietariosJustoPossuidores;
	}

	public void setLblListaProprietariosJustoPossuidores(String lblListaProprietariosJustoPossuidores) {
		this.lblListaProprietariosJustoPossuidores = lblListaProprietariosJustoPossuidores;
	}

	public String getLblBotaoIncluirProprietariosJustoPossuidores() {
		return lblBotaoIncluirProprietariosJustoPossuidores;
	}

	public void setLblBotaoIncluirProprietariosJustoPossuidores(String lblBotaoIncluirProprietariosJustoPossuidores) {
		this.lblBotaoIncluirProprietariosJustoPossuidores = lblBotaoIncluirProprietariosJustoPossuidores;
	}

	public String getLblDocumentoPossePropriedade() {
		return lblDocumentoPossePropriedade;
	}

	public void setLblDocumentoPossePropriedade(String lblDocumentoPossePropriedade) {
		this.lblDocumentoPossePropriedade = lblDocumentoPossePropriedade;
	}

	/***
	 * Este método é responsável por carregar o label do documento que o usuário faz
	 * upload na aba de documentação. Caso seja escolhido o tipo de vinculo
	 * proprietário aparecerá Documento de Propriedade. Caso seja escolhido o tipo
	 * de vinculo justo possuidor aparecerá Documento de Posse.
	 *
	 * @return string do label que deve aparecer quando o usuário fizer upload de um
	 *         documento na tabela que lista o documento que foi feito o upload.
	 */
	public String getNomeDocumentoPorTipoVinculo() {
		if (tipoVinculoImovel.getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_PROPRIETARIO)) {
			return Util.getString("cefir_lbl_documento_propriedade");
		}
		return Util.getString("cefir_lbl_documento_posse");
	}

	/***
	 * Este método é responsável por controlar o evento change da pergunta
	 * Contrato/Convênio no questionário Caso seja escolhido o SIM é instanciado um
	 * novo ContratoConvenioCefir na entidade ImovelRural Caso seja escolhido o NÃO
	 * é atribuído nulo a propriedade ideContratoConvenioCefir na entidade
	 * ImovelRural
	 */
	public void changeContratoConvenio(ValueChangeEvent event) {
		Boolean valor = (Boolean) event.getNewValue();
		if (valor && Util.isNullOuVazio(this.imovelRural.getIdeContratoConvenioCefir())) {
			this.imovelRural.setIdeContratoConvenioCefir(new ContratoConvenioCefir());
		} else if (!valor) {
			this.imovelRural.setIdeContratoConvenioCefir(null);
		}
	}

	/***
	 * Este método é responsável por validar o numero do contrato convênio informado
	 * na tela de questionário É realizada uma consulta para obter o contrato
	 * convênio com o número informado Caso seja encontrado um contrato convênio o
	 * mesmo é atribuído a propriedade ideContratoConvenioCefir na entidade
	 * ImovelRural Caso não seja encontrado é disparada uma mensagem e é atribuido
	 * nulo a propiedade numContratoConvenioCefir
	 */
	public void validarContratoConvenioCefir() {
		if (!isUsuarioSemRestricao()) {
			try {
				ContratoConvenioCefir contratoConvenioCefir = imovelRuralServiceFacade.buscarContratoConvenioByNumero(
						this.imovelRural.getIdeContratoConvenioCefir().getNumContratoConvenioCefir());
				if (!Util.isNullOuVazio(contratoConvenioCefir)) {
					if (this.imovelRural.isImovelBNDES()) {
						validarContratoConvenioCefirBndes(contratoConvenioCefir);
					}
					imovelRural.setIdeContratoConvenioCefir(contratoConvenioCefir);
				} else {
					imovelRural.setIdeContratoConvenioCefir(new ContratoConvenioCefir());
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_A014"));
				}
			} catch (Exception e) {
				imovelRural.setIdeContratoConvenioCefir(new ContratoConvenioCefir());
				JsfUtil.addErrorMessage(e.getMessage());
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			}
		}
	}

	/**
	 * Método responsável por realizar a validação específica para os contratos do
	 * BNDES
	 * 
	 * @param contratoConvenioCefir
	 * @throws ValidationException
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 21/10/2015
	 */
	private void validarContratoConvenioCefirBndes(ContratoConvenioCefir contratoConvenioCefir)
			throws ValidationException {
		if (!isUsuarioSemRestricao()) {
			if (!contratoConvenioCefir.isIndProjetoBndes()) {
				throw new ValidationException(Util.getString("cefir_msg_A014"));
			} else {
				if (!Util.isNullOuVazio(contratoConvenioCefir.getContratoConvenioCefirMunicipioCollection())) {
					for (ContratoConvenioCefirMunicipio contratoConvenioCefirMunicipio : contratoConvenioCefir
							.getContratoConvenioCefirMunicipioCollection()) {
						if (contratoConvenioCefirMunicipio.getIdeMunicipio().getIdeMunicipio().equals(this.imovelRural
								.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getIdeMunicipio())) {
							return;
						}
					}
				}
			}
			throw new ValidationException(Util.getString("cefir_msg_contrato_convenio_municipio_divergente"));
		}
	}

	public boolean getIsRequiredCDAProprietario() {
		if (!this.imovelRural.isImovelCDA() && !this.imovelRural.isImovelBNDES()
				&& (Util.isNullOuVazio(this.tipoVinculoImovel)
						|| Util.isNullOuVazio(this.tipoVinculoImovel.getIdeTipoVinculoImovel())
						|| tipoVinculoImovel.isProprietario())) {
			return true;
		}
		return false;
	}

	public boolean getIsRequiredCDAJustoPossuidor() {
		if (!this.imovelRural.isImovelCDA() && !this.imovelRural.isImovelBNDES()
				&& (Util.isNullOuVazio(this.tipoVinculoImovel)
						|| Util.isNullOuVazio(this.tipoVinculoImovel.getIdeTipoVinculoImovel())
						|| tipoVinculoImovel.isJustoPossuidor())) {
			return true;
		}
		return false;
	}

	public boolean validaQuestionario() {

		if (getShowPCT()) {
			if (Util.isNull(this.imovelRural.getIndContratoConvenio())
					|| Util.isNull(this.imovelRural.getIndVegetacaoNativa())
					|| Util.isNull(this.imovelRural.getIndApp())) {
				return false;
			}
		} else {

			if (!isUsuarioSemRestricao()) {
				// Cadastro Informações
				if (this.imovelRural.getIndDesejaCompletarInformacoes()) {

					// MENOR QUE 4 MODULOS
					if (this.imovelRural.isMenorQueQuatroModulosFiscais()) {

						// CONTRATO CONVENIO
						if (Util.isNull(this.imovelRural.getIndContratoConvenio())) {
							return false;
						} else {
							if (this.imovelRural.getIndContratoConvenio()
									&& Util.isNull(this.imovelRural.getIdeContratoConvenioCefir())
									&& Util.isNull(this.imovelRural.getIdeContratoConvenioCefir()
											.getNumContratoConvenioCefir())) {
								return false;
							}
						}
					}

					// RESERVA LEGAL
					if (this.imovelRural.isImovelCDA() && this.imovelRural.isImovelBNDES()
							&& this.imovelRural.isMenorQueQuatroModulosFiscais()
							&& Util.isNull(this.imovelRural.getIndExistiaRemanescenteVegetacaoNativa())) {
						return false;
					}

					// ÁREA RURAL CONSOLIDADA
					if (this.imovelRural.isImovelINCRA()
							&& Util.isNull(this.imovelRural.getIndAreaRuralConsolidada())) {
						return false;
					}

					// TAC
					if (Util.isNull(this.imovelRural.getIndTac())) {
						return false;
					} else if (this.imovelRural.getIndTac()) {
						if (Util.isNull(this.imovelRural.getImovelRuralTac().getDscOrgaoEmissor())
								|| Util.isNull(this.imovelRural.getImovelRuralTac().getDtcAssinatura())
								|| Util.isNull(this.imovelRural.getImovelRuralTac().getDtcEncerramento())) {
							return false;
						} else if (!Util.isNull(this.imovelRural.getImovelRuralTac().getDtcAssinatura())
								&& !Util.isNull(this.imovelRural.getImovelRuralTac().getDtcEncerramento())
								&& Util.validarDuasDatas(this.imovelRural.getImovelRuralTac().getDtcEncerramento(),
										this.imovelRural.getImovelRuralTac().getDtcAssinatura())) {
							msgDataInvalida = "A data de abertura do TAC não pode ser posterior à data de encerramento.";
							return false;
						}
					}
					msgDataInvalida = null;
				}

				// PRAD
				if (Util.isNull(this.imovelRural.getIndPrad())) {
					return false;
				} else if (this.imovelRural.getIndPrad()) {
					if (Util.isNull(this.imovelRural.getImovelRuralPrad().getDscOrgaoEmissor())
							|| Util.isNull(this.imovelRural.getImovelRuralPrad().getDtcAssinatura())
							|| Util.isNull(this.imovelRural.getImovelRuralPrad().getDtcEncerramento())) {
						return false;
					} else if (!Util.isNull(this.imovelRural.getImovelRuralPrad().getDtcAssinatura())
							&& !Util.isNull(this.imovelRural.getImovelRuralPrad().getDtcEncerramento())
							&& Util.validarDuasDatas(this.imovelRural.getImovelRuralPrad().getDtcEncerramento(),
									this.imovelRural.getImovelRuralPrad().getDtcAssinatura())) {
						msgDataInvalida = "A data de abertura do PRAD não pode ser posterior à data de encerramento.";
						return false;
					}
					msgDataInvalida = null;
				}

				// INFRAÇÕES
				if (Util.isNull(this.imovelRural.getIndInfracaoSupressao())) {
					return false;
				}

				// CRF
				if (Util.isNull(this.imovelRural.getIndCrf())) {
					return false;
				}

				// ALTERAÇÃO ÁREA
				if (Util.isNull(this.imovelRural.getIndAlteracaoTamanho())) {
					return false;
				} else {
					if (this.imovelRural.getIndAlteracaoTamanho()
							&& Util.isNull(this.imovelRural.getValAlteracaoTamanho())) {
						return false;
					}
				}

				// APP
				if (Util.isNull(this.imovelRural.getIndApp())) {
					return false;
				}

				// VEGETAÇÃO NATIVA
				if (Util.isNull(this.imovelRural.getIndVegetacaoNativa())) {
					return false;
				}

				// RPPN
				if (Util.isNull(this.imovelRural.getIndRppn())) {
					return false;
				}

				// SUPRESSÃO VEGETAÇÃO
				if (imovelRural.getIndSupressaoVegetacao() == null) {
					if (isIndSupressaoVegetacaoObrigatorio()) {
						return false;
					}
				} else {
					if (imovelRural.getIndSupressaoVegetacao()
							&& imovelRural.getSupressaoVegetacao().getValArea() == null) {
						return false;
					}

					if (imovelRural.getIndSupressaoVegetacao() && imovelRural.getSupressaoVegetacao().isIndProcesso()
							&& Util.isNullOuVazio(imovelRural.getSupressaoVegetacao().getNumProcesso())) {

						return false;
					}
				}

				// RESIDUO LIQUIDO
				if (Util.isNull(this.getIndLancamentoResiduosLiquidos())) {
					if (!this.imovelRural.isImovelCDA() && !this.imovelRural.isImovelBNDES()) {
						return false;
					}
				} else {
					if (this.getIndLancamentoResiduosLiquidos() && !this.imovelRural.isImovelCDA()
							&& (Util.isNull(this.imovelRural.getIndLancamentoConcessionaria())
									|| !this.imovelRural.getIndLancamentoConcessionaria())
							&& (Util.isNull(this.imovelRural.getIndLancamentoManancial())
									|| !this.imovelRural.getIndLancamentoManancial())) {
						return false;
					}
				}

				// VALIDAÇÃO NÃO CDA
				if (!this.imovelRural.isImovelCDA() && !this.imovelRural.isImovelBNDES()) {
					// ÁREA PRODUTIVA
					if (Util.isNull(this.imovelRural.getIndAreaProdutiva())) {
						return false;
					}

					// AGROTÓXICO
					if (imovelRural.getIndAgrotoxico() == null && !imovelRural.isImovelCDA()
							&& !imovelRural.isImovelBNDES() && !imovelRural.isImovelINCRA()) {
						return false;
					}

					// INTERVENÇÃO CORPO HÍDRICO
					if (Util.isNull(this.imovelRural.getIndIntervencao())) {
						return false;
					}

					// PASSIVOS
					if (Util.isNull(this.imovelRural.getIndOutrosPassivos())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public String getNomeTipoVinculo() {
		if (!Util.isNull(this.getTipoVinculoImovel())) {
			if (Util.isNullOuVazio(this.tipoVinculoImovel.getIdeTipoVinculoImovel()) || this.tipoVinculoImovel
					.getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_PROPRIETARIO)) {
				return "Proprietário do Imóvel";
			}
			return "Justo Possuidor do imóvel";
		}
		return "";
	}

	public boolean getVisualizarMensagemAtualizacaoProprietario() {
		if (!Util.isNullOuVazio(imovelRural)
				&& !Util.isLazyInitExcepOuNull(imovelRural.getImovelRuralMudancaStatusJustificativaCollection())
				&& !Util.isNullOuVazio(imovelRural.getImovelRuralMudancaStatusJustificativaCollection())) {
			if (imovelRural.getImovel().getPessoaImovelCollection().size() > 1) {
				for (ImovelRuralMudancaStatusJustificativa imovelRuralMudancaStatusJustificativa : imovelRural
						.getImovelRuralMudancaStatusJustificativaCollection()) {
					if (imovelRuralMudancaStatusJustificativa.getDtcJustificativa()
							.after(imovelRural.getDtcFinalizacao())
							&& imovelRuralMudancaStatusJustificativa.isIndAlterarProprietario()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isValidaCartorio() {
		if (Util.isNullOuVazio(this.tipoVinculoImovel)
				|| Util.isNullOuVazio(this.tipoVinculoImovel.getIdeTipoVinculoImovel()) || !this.tipoVinculoImovel
						.getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_PROPRIETARIO)) {

			return false;
		}

		if (!getIsRequiredCDAProprietario() && !getIsRequiredCDAJustoPossuidor()) {
			return false;
		}

		return true;
	}

	// Outras Tipologias

	public void carregarDivisaoAtividade() {
		try {
			Collection<Tipologia> listCandidatas = imovelRuralServiceFacade.carregarDivisaoAtividade();
			listaDivisaoAtividade = new ArrayList<Tipologia>();
			for (Tipologia tipologia : listCandidatas) {
				if (DivisaoAtividadeCefirEnum.containsDivisaoAtividade(tipologia)) {
					listaDivisaoAtividade.add(tipologia);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void changeDivisaoAtividade() {
		final boolean divisaoFoiSelecionada = (divisaoAtividade != null && divisaoAtividade.getIdeTipologia() != null);
		if (divisaoFoiSelecionada) {
			// this.tipologia =
			// retornaTipologiaApSelecionada(this.tipologia.getIdeTipologia());
			this.listaDocumentoPlanoManejo.clear();
			carregarGrupoAtividadePorDivisao();
			limparGrupoAtividade();
			limparSubGrupoAtividade();
			limparTipologiaAtividade();
		}
	}

	private void limparSubGrupoAtividade() {
		this.areaProdutivaSelecionada.setIdeTipologiaSubgrupo(new Tipologia());
		this.listaSubGrupoAtividade = new ArrayList<Tipologia>();
	}

	private void limparTipologiaAtividade() {
		this.tipologiaAtividade = new TipologiaAtividade();
		this.listaTipologiaAtividade = new ArrayList<TipologiaAtividade>();
		areaProdutivaSelecionada.getAreaProdutivaTipologiaAtividadeCollection().clear();
	}

	private void limparGrupoAtividade() {
		this.areaProdutivaSelecionada.setIdeTipologia(new Tipologia());
	}

	private void carregarGrupoAtividadePorDivisao() {
		try {
			this.listaGrupoAtividade = new ArrayList<Tipologia>();
			Collection<Tipologia> candidatos = imovelRuralServiceFacade
					.filtrarListaTipologiasByIdePai(this.divisaoAtividade);
			for (Tipologia c : candidatos) {
				// Verificar a necessidade de filtro
				if (!c.getIdeTipologia().equals(TipologiaCefirEnum.ASSENTAMENTO_REFORMA_AGRARIA.getId())
						&& !c.getIdeTipologia()
								.equals(TipologiaCefirEnum.PRODUCAO_CARVAO_VEGETAL_CARATER_TEMPORARIO.getId())
						&& !c.getIdeTipologia().equals(TipologiaCefirEnum.PRODUTOS_AGRICULTURA_NEW.getId())) {
					this.listaGrupoAtividade.add(c);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}

	}

	public void changeGrupoAtividade() {
		final boolean grupoFoiSelecionado = (this.areaProdutivaSelecionada.getIdeTipologia() != null
				&& this.areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia() != null);
		if (grupoFoiSelecionado) {
			// this.tipologia =
			// retornaTipologiaApSelecionada(this.tipologia.getIdeTipologia());
			limparSubGrupoAtividade();
			incluirTipologiaAtividade(false);
			atualizarPainelAtividadesDesenvolvidas(0);
			carregarSubGrupoAtividadePorGrupo();
			limparTipologiaAtividade();
		}
	}

	private void carregarSubGrupoAtividadePorGrupo() {
		try {
			Collection<Tipologia> candidatas = imovelRuralServiceFacade
					.filtrarListaTipologiasByIdePai(this.areaProdutivaSelecionada.getIdeTipologia());
			this.listaSubGrupoAtividade = new ArrayList<Tipologia>();
			// verificar a necessidade do filtro
			for (Tipologia tipologia : candidatas) {
				if (!tipologia.getIdeTipologia().equals(TipologiaCefirEnum.PRODUTOS_AGRICULTURA.getId())
						&& !tipologia.getIdeTipologia()
								.equals(TipologiaCefirEnum.CARCINICULTURA_EM_VIVEIROS_ESCAVADOS_OLD.getId())
						&& !tipologia.getIdeTipologia()
								.equals(TipologiaCefirEnum.ALGICULTURA_E_MALACOCULTURA_OLD.getId())
						&& !tipologia.getIdeTipologia().equals(TipologiaCefirEnum.AGRICULTURA_OLD.getId())
						&& !tipologia.getIdeTipologia()
								.equals(TipologiaCefirEnum.SILVICULTURA_VINCULADA_PROCESSOS_INDUSTRIAIS.getId())
						&& !tipologia.getIdeTipologia()
								.equals(TipologiaCefirEnum.SILVICULTURA_NAO_VINCULADA_PROCESSOS_INDUSTRIAIS.getId())
						&& !tipologia.getIdeTipologia().equals(TipologiaCefirEnum.PERCUARIA_NEW.getId())) {

					if (!listaSubGrupoAtividade.contains(tipologia)) {
						listaSubGrupoAtividade.add(tipologia);
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	private void carregarTipologiaAtividade() {
		try {
			this.listaTipologiaAtividade = new ArrayList<TipologiaAtividade>();
			if (!Util.isNullOuVazio(this.areaProdutivaSelecionada.getIdeTipologiaSubgrupo())
					&& !this.areaProdutivaSelecionada.getIdeTipologiaSubgrupo().getIdeTipologia()
							.equals(TipologiaCefirEnum.RANICULTURA.getId())
					&& !this.areaProdutivaSelecionada.getIdeTipologiaSubgrupo().getIdeTipologia()
							.equals(TipologiaCefirEnum.SILVICULTURA_NAO_VINCULADA_PROCESSOS_INDUSTRIAIS.getId())) {
				Collection<TipologiaAtividade> candidatas = imovelRuralServiceFacade
						.filtrarListaTipologiaAtividadeByIdePai(
								this.areaProdutivaSelecionada.getIdeTipologiaSubgrupo());
				// verificar a necessidade do filtro
				for (TipologiaAtividade tipologia : candidatas) {
					if (!tipologia.getIdeTipologiaAtividade()
							.equals(TipologiaCefirEnum.PSICULTURA_MARINHA_TANQUE_REDE_RACEWAY_SIMILAR_OLD.getId())
							&& !tipologia.getIdeTipologiaAtividade()
									.equals(TipologiaCefirEnum.CARCINICULTURA_EM_VIVEIROS_ESCAVADOS_OLD.getId())) {
						this.listaTipologiaAtividade.add(tipologia);
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void changeSubGrupoAtividade() {
		final boolean subGrupoFoiSelecionado = (this.areaProdutivaSelecionada.getIdeTipologiaSubgrupo() != null
				&& this.areaProdutivaSelecionada.getIdeTipologiaSubgrupo().getIdeTipologia() != null);
		if (subGrupoFoiSelecionado) {
			this.listaDocumentoPlanoManejo.clear();
			limparTipologiaAtividade();
			carregarTipologiaAtividade();
			atualizarPainelAtividadesDesenvolvidas(0);
			incluirTipologiaAtividade(false);
		}
	}

	private void atualizarPainelAtividadesDesenvolvidas(Integer ideTipologia) {
		switch (ideTipologia) {

		case 6: // agricultura irrigada
			/*
			 * limpaFormTipologiaAtividadeAgricultura();
			 * limpaFormTipologiaAtividadePiscicultura();
			 */
			this.pnlAgricultura = true;
			this.pnlAnimal = false;
			this.pnlPiscicultura = false;
			carregarMetodoIrrigacao();
			break;
		case 8:// criação de animais
		case 10:// pecuária
			/*
			 * limpaFormTipologiaAtividadeAnimal();
			 * limpaFormTipologiaAtividadePiscicultura();
			 */
			this.pnlAnimal = true;
			this.pnlAgricultura = false;
			this.pnlPiscicultura = false;
			break;
		case 16:// Aquicultura
			/*
			 * limpaFormTipologiaAtividadeAgricultura();
			 * limpaFormTipologiaAtividadeAnimal();
			 */
			this.pnlPiscicultura = true;
			this.pnlAgricultura = false;
			this.pnlAnimal = false;
			break;
		default:
			this.pnlPiscicultura = false;
			this.pnlAgricultura = false;
			this.pnlAnimal = false;
			break;
		}

	}

	public void adicionarTipologiaAtividade() {
		try {

			// caso nao exista cai na exception
			verficaExistenciaTipologiaAtividade(this.tipologiaAtividade);

			this.tipologiaAtividade = imovelRuralServiceFacade
					.carregarTipologiaAtividadeByIde(this.tipologiaAtividade.getIdeTipologiaAtividade());

			if (pnlAgricultura) {
				adicionarAtividadeAgricultura();
			} else if (pnlAnimal) {
				adicionarAtividadeAnimal();
			} else if (pnlPiscicultura) {
				adicionarAtividadePisicultura();
			} else {
				AreaProdutivaTipologiaAtividade apta = getInstanciaTipologiaAtividade();
				areaProdutivaSelecionada.getAreaProdutivaTipologiaAtividadeCollection().add(apta);
			}

			limpaFormTipologiaAtividade();
			incluirTipologiaAtividade(false);

		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void verficaExistenciaTipologiaAtividade(TipologiaAtividade pTipologiaAtividade) throws Exception {

		for (AreaProdutivaTipologiaAtividade atividade : areaProdutivaSelecionada
				.getAreaProdutivaTipologiaAtividadeCollection()) {
			if (atividade.getIdeTipologiaAtividade().getIdeTipologiaAtividade()
					.equals(pTipologiaAtividade.getIdeTipologiaAtividade())) {
				if (pTipologiaAtividade.getIdeTipologia().getIdeTipologia()
						.equals(TipologiaCefirEnum.AGRICULTURA_IRRIGADA.getId())) {
					if (this.metodoIrrigacao.getIdeMetodoIrrigacao()
							.equals(atividade.getIdeAreaProdutivaTipologiaAtividadeAgricultura().getIdeMetodoIrrigacao()
									.getIdeMetodoIrrigacao())) {
						throw new Exception("Esta atividade já foi adicionada!");
					}
				} else {
					throw new Exception("Esta atividade já foi adicionada!");
				}
			}
		}
	}

	private AreaProdutivaTipologiaAtividade getInstanciaTipologiaAtividade() {
		AreaProdutivaTipologiaAtividade apta = new AreaProdutivaTipologiaAtividade();
		apta.setIdeTipologiaAtividade(this.tipologiaAtividade);
		return apta;
	}

	private void adicionarAtividadePisicultura() {

		AreaProdutivaTipologiaAtividade apta = getInstanciaTipologiaAtividade();

		apta.setIdeAreaProdutivaTipologiaAtividadePiscicultura(new AreaProdutivaTipologiaAtividadePiscicultura());

		apta.getIdeAreaProdutivaTipologiaAtividadePiscicultura()
				.setVolume(Double.parseDouble(this.valVolume.replaceAll("\\.", "").replace(",", ".")));
		areaProdutivaSelecionada.getAreaProdutivaTipologiaAtividadeCollection().add(apta);
	}

	private void adicionarAtividadeAnimal() {
		if (!this.indManejoCria && !this.indManejoEngorda && !this.indManejoRecria && !this.indManejoReproducao) {
			JsfUtil.addWarnMessage("Informe o manejo.");
			return;
		}
		if (areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia().equals(10)
				&& areaProdutivaSelecionada.getAreaProdutivaTipologiaAtividadeCollection().size() == 1) {
			// 10 - Criações confinadas
			JsfUtil.addWarnMessage("Este tipo de atividade só pode ser adicionada somente uma vez na lista.");
			return;
		}

		if (this.temUsoDessedentacao && this.temUsoLimpeza) {
			this.tipoUsoAgua = 3;
			this.dscTipoUsoAgua = "Dessedentação e Limpeza";
		} else if (this.temUsoDessedentacao) {
			this.tipoUsoAgua = 1;
			this.dscTipoUsoAgua = "Dessedentação";
		} else if (this.temUsoLimpeza) {
			this.tipoUsoAgua = 2;
			this.dscTipoUsoAgua = "Limpeza";
		} else {
			this.tipoUsoAgua = 0;
			this.dscTipoUsoAgua = "Nenhum uso informado";
		}

		AreaProdutivaTipologiaAtividade apta = getInstanciaTipologiaAtividade();

		apta.setIdeAreaProdutivaTipologiaAtividadeAnimal(new AreaProdutivaTipologiaAtividadeAnimal());
		apta.getIdeAreaProdutivaTipologiaAtividadeAnimal().setQtdCabeca(this.qtdCabeca);
		apta.getIdeAreaProdutivaTipologiaAtividadeAnimal().setTipoUsoAgua(this.tipoUsoAgua);
		apta.getIdeAreaProdutivaTipologiaAtividadeAnimal().setIndManejoCria(this.indManejoCria);
		apta.getIdeAreaProdutivaTipologiaAtividadeAnimal().setIndManejoEngorda(this.indManejoEngorda);
		apta.getIdeAreaProdutivaTipologiaAtividadeAnimal().setIndManejoRecria(this.indManejoRecria);
		apta.getIdeAreaProdutivaTipologiaAtividadeAnimal().setIndManejoReproducao(this.indManejoReproducao);

		areaProdutivaSelecionada.getAreaProdutivaTipologiaAtividadeCollection().add(apta);
	}

	private void adicionarAtividadeAgricultura() throws Exception {
		this.dscMesesIrrigacao = "";
		this.numMesesIrrigacao = "";
		if (this.temMesJan) {
			this.dscMesesIrrigacao += "JAN, ";
			this.numMesesIrrigacao += "1-";
		}
		if (this.temMesFev) {
			this.dscMesesIrrigacao += "FEV, ";
			this.numMesesIrrigacao += "2-";
		}
		if (this.temMesMar) {
			this.dscMesesIrrigacao += "MAR, ";
			this.numMesesIrrigacao += "3-";
		}
		if (this.temMesAbr) {
			this.dscMesesIrrigacao += "ABR, ";
			this.numMesesIrrigacao += "4-";
		}
		if (this.temMesMai) {
			this.dscMesesIrrigacao += "MAI, ";
			this.numMesesIrrigacao += "5-";
		}
		if (this.temMesJun) {
			this.dscMesesIrrigacao += "JUN, ";
			this.numMesesIrrigacao += "6-";
		}
		if (this.temMesJul) {
			this.dscMesesIrrigacao += "JUL, ";
			this.numMesesIrrigacao += "7-";
		}
		if (this.temMesAgo) {
			this.dscMesesIrrigacao += "AGO, ";
			this.numMesesIrrigacao += "8-";
		}
		if (this.temMesSet) {
			this.dscMesesIrrigacao += "SET, ";
			this.numMesesIrrigacao += "9-";
		}
		if (this.temMesOut) {
			this.dscMesesIrrigacao += "OUT, ";
			this.numMesesIrrigacao += "10-";
		}
		if (this.temMesNov) {
			this.dscMesesIrrigacao += "NOV, ";
			this.numMesesIrrigacao += "11-";
		}
		if (this.temMesDez) {
			this.dscMesesIrrigacao += "DEZ, ";
			this.numMesesIrrigacao += "12-";
		}
		if (this.dscMesesIrrigacao.isEmpty()) {
			throw new Exception("O campo Meses de Irrigação é de preenchimento obrigatório.");
		} else {
			this.dscMesesIrrigacao = this.dscMesesIrrigacao.substring(0, this.dscMesesIrrigacao.length() - 2);
			this.numMesesIrrigacao = this.numMesesIrrigacao.substring(0, this.numMesesIrrigacao.length() - 1);
		}

		AreaProdutivaTipologiaAtividade apta = getInstanciaTipologiaAtividade();

		AreaProdutivaTipologiaAtividadeAgricultura areaProdutivaTipologiaAtivAgricultura;
		areaProdutivaTipologiaAtivAgricultura = new AreaProdutivaTipologiaAtividadeAgricultura();

		// Plano de Manejo Sustentavel ou da Cabruca
		if (getManejoSustentavel() || getManejoCabruca()) {
			this.areaProdutivaSelecionada.setIdeTipologiaSubgrupo(null);
		}

		apta.setIdeAreaProdutivaTipologiaAtividadeAgricultura(areaProdutivaTipologiaAtivAgricultura);

		apta.getIdeAreaProdutivaTipologiaAtividadeAgricultura().setIdeMetodoIrrigacao(
				imovelRuralServiceFacade.carregarMetodoIrrigacaoByIde(this.metodoIrrigacao.getIdeMetodoIrrigacao()));
		apta.getIdeAreaProdutivaTipologiaAtividadeAgricultura().setMeses(this.numMesesIrrigacao);

		areaProdutivaSelecionada.getAreaProdutivaTipologiaAtividadeCollection().add(apta);
	}

	public boolean getManejoSustentavel() {

		Integer idTipologia = this.areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia();

		return (!Util.isNull(idTipologia)
				&& idTipologia.equals(TipologiaCefirEnum.MANEJO_FLORESTAL_SUSTENTAVEL.getId())) ? true : false;
	}

	public boolean getManejoCabruca() {

		Integer idTipologia = this.areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia();

		return (!Util.isNull(idTipologia) && idTipologia.equals(TipologiaCefirEnum.MANEJO_CABRUCA.getId())) ? true
				: false;
	}

	public List<Tipologia> getListaDivisaoAtividade() {
		return listaDivisaoAtividade;
	}

	public Tipologia getDivisaoAtividade() {
		return divisaoAtividade;
	}

	public void setDivisaoAtividade(Tipologia divisaoAtividade) {
		this.divisaoAtividade = divisaoAtividade;
	}

	public List<Tipologia> getListaGrupoAtividade() {
		return listaGrupoAtividade;
	}

	public List<Tipologia> getListaSubGrupoAtividade() {
		return listaSubGrupoAtividade;
	}

	public void salvarAreaProdutiva() {
		try {
			if (!Util.isNull(areaProdutivaSelecionada)
					&& !Util.isNull(areaProdutivaSelecionada.getIdeLocalizacaoGeografica())) {
				if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isShapeFile()
						&& !habilitaSalvarShape(areaProdutivaSelecionada.getIdeLocalizacaoGeografica())
						&& !existeTheGeomAp(areaProdutivaSelecionada)) {
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_arquivos_shape_prj"));
					return;
				} else if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().isDesenho()
						&& !temShapeTemporario(
								imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId()
										+ "_" + areaProdutivaSelecionada.getCodigoPersistirShape())
						&& !existeTheGeomAp(areaProdutivaSelecionada)) {
					JsfUtil.addErrorMessage(
							Util.getString("msg_generica_preenchimento_obrigatorio_localizacao_geografica_poligonal"));
					return;
				}
			}

			validarGrupoAtividadeRepetido();

			areaProdutivaSelecionada.setIdeTipologia(
					retornaGrupoAtividade(areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia()));

			if (!Util.isNullOuVazio(areaProdutivaSelecionada.getIdeTipologiaSubgrupo())
					&& !Util.isNullOuVazio(areaProdutivaSelecionada.getIdeTipologiaSubgrupo().getIdeTipologia())) {
				areaProdutivaSelecionada.setIdeTipologiaSubgrupo(
						retornaSubGrupoAtividade(areaProdutivaSelecionada.getIdeTipologiaSubgrupo().getIdeTipologia()));
			}

			if (!imovelRuralServiceFacade.validouCamposObrigatoriosAreaProdutiva(areaProdutivaSelecionada,
					imovelRural.isImovelINCRA())) {
				JsfUtil.addErrorMessage("Tipo atividade obrigatório!");
				return;
			}

			if (getRenderedPerguntaLicenca() && Util.isNull(areaProdutivaSelecionada.getLicenciada())) {
				JsfUtil.addErrorMessage("O campo Possui Licença é de preenchimento obrigatório.");
				return;
			}

			if (getManejoCabruca()) {

				validarProcessoExterno();

				this.areaProdutivaSelecionada.setIdeTipologiaSubgrupo(null);
			}

			if (getManejoSustentavel()) {
				String geometriaAp;
				if (Util.isNullOuVazio(this.listaDocumentoPlanoManejo)) {
					JsfUtil.addErrorMessage("Comprovante do Plano de Manejo Sustentável é obrigatório!");
					return;
				}
				if (temShapeTemporario(
						imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId() + "_"
								+ areaProdutivaSelecionada.getCodigoPersistirShape())) {

					String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
							+ imovelRural.getIdeImovelRural().toString() + "_"
							+ TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId() + "_"
							+ areaProdutivaSelecionada.getCodigoPersistirShape().toString().toString()
							+ FileUploadUtil.getFileSeparator();

					areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
							.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(
									areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
											.getIdeClassificacaoSecao()));

					areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(
							retornaSistemaCordenadaSelecionado(areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
									.getIdeSistemaCoordenada().getIdeSistemaCoordenada()));

					FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, "shapeAtividadeDesenvolvida",
							imovelRural.getIdeImovelRural().toString() + "_" + areaProdutivaSelecionada
									.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid().toString());

					geometriaAp = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
							this.imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId(),
							this.areaProdutivaSelecionada.getCodigoPersistirShape());
				} else {
					geometriaAp = imovelRuralServiceFacade.buscarGeometriaShape(
							this.areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}
				imovelRuralServiceFacade.validarTalhoesDeclaradosGeometria(
						this.areaProdutivaSelecionada.getNumUnidadeProducao(), geometriaAp);

				this.areaProdutivaSelecionada.setIdeTipologiaSubgrupo(null);
			}

			if (!isEdicaoAp) {

				areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setDtcCriacao(new Date());
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setDtcExclusao(null);
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setIndExcluido(Boolean.FALSE);
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(areaProdutivaSelecionada
								.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(
						retornaClassificacaoSecaoGeometricaSelecionado(areaProdutivaSelecionada
								.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

				if (areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia().equals(6)) {
					for (AreaProdutivaTipologiaAtividade apta : areaProdutivaSelecionada
							.getAreaProdutivaTipologiaAtividadeCollection()) {
						apta.setIdeAreaProdutiva(areaProdutivaSelecionada);
						apta.setIndExcluido(Boolean.FALSE);
						apta.setDtcCriacao(new Date());
						apta.setDtcExclusao(null);
						apta.getIdeAreaProdutivaTipologiaAtividadeAgricultura()
								.setIdeAreaProdutivaTipologiaAtividade(apta);
					}
				} else if (areaProdutivaSelecionada.possuiTipologiaSubGrupoCadastrada()) {
					if (areaProdutivaSelecionada.getIdeTipologiaSubgrupo().getIdeTipologia().equals(8)
							|| areaProdutivaSelecionada.getIdeTipologiaSubgrupo().getIdeTipologia().equals(10)) {
						for (AreaProdutivaTipologiaAtividade apta : areaProdutivaSelecionada
								.getAreaProdutivaTipologiaAtividadeCollection()) {
							apta.setIdeAreaProdutiva(areaProdutivaSelecionada);
							apta.setIndExcluido(Boolean.FALSE);
							apta.setDtcCriacao(new Date());
							apta.setDtcExclusao(null);
							apta.getIdeAreaProdutivaTipologiaAtividadeAnimal()
									.setIdeAreaProdutivaTipologiaAtividade(apta);
						}
					} else if (areaProdutivaSelecionada.getIdeTipologiaSubgrupo().getIdeTipologia().equals(16)) {
						for (AreaProdutivaTipologiaAtividade apta : areaProdutivaSelecionada
								.getAreaProdutivaTipologiaAtividadeCollection()) {
							apta.setIdeAreaProdutiva(areaProdutivaSelecionada);
							apta.setIndExcluido(Boolean.FALSE);
							apta.setDtcCriacao(new Date());
							apta.setDtcExclusao(null);
							apta.getIdeAreaProdutivaTipologiaAtividadePiscicultura()
									.setIdeAreaProdutivaTipologiaAtividade(apta);
						}
					}
				} else {
					for (AreaProdutivaTipologiaAtividade apta : areaProdutivaSelecionada
							.getAreaProdutivaTipologiaAtividadeCollection()) {
						apta.setIdeAreaProdutiva(areaProdutivaSelecionada);
						apta.setIndExcluido(Boolean.FALSE);
						apta.setDtcCriacao(new Date());
						apta.setDtcExclusao(null);
					}
				}

				if (temShapeTemporario(
						imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId() + "_"
								+ areaProdutivaSelecionada.getCodigoPersistirShape())) {
					persistirShapesAp(areaProdutivaSelecionada.getIdeLocalizacaoGeografica());
				}

				if (!imovelRural.getIsFinalizado())
					persistirAreaProdutiva(areaProdutivaSelecionada);

				imovelRural.getAreaProdutivaCollection().add(areaProdutivaSelecionada);
				JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
			} else {

				areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
						.setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(areaProdutivaSelecionada
								.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(
						retornaClassificacaoSecaoGeometricaSelecionado(areaProdutivaSelecionada
								.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

				if (temShapeTemporario(
						imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId() + "_"
								+ areaProdutivaSelecionada.getCodigoPersistirShape())) {
					persistirShapesAp(areaProdutivaSelecionada.getIdeLocalizacaoGeografica());
				}

				if (!imovelRural.getIsFinalizado())
					persistirAreaProdutiva(areaProdutivaSelecionada);

				JsfUtil.addSuccessMessage("Alteração efetuada com sucesso.");
			}

			if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao().equals(2)) {
				this.temLocGeoShapeAreaProdutiva = true;
				this.temLocGeoDesenhoAreaProdutiva = false;
			} else if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao().equals(3)) {
				this.temLocGeoShapeAreaProdutiva = false;
				this.temLocGeoDesenhoAreaProdutiva = true;
			}

			if (!Util.isNullOuVazio(areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape()))
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().clear();
			RequestContext.getCurrentInstance().execute("dlgCadAreaProdutiva.hide()");
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void validarGrupoAtividadeRepetido() throws Exception {
		if (!isUsuarioSemRestricao()) {
			if (!this.isEdicaoAp) {
				if (areaProdutivaSelecionada.possuiTipologiaSubGrupoCadastrada()) {
					Tipologia subgrupo = areaProdutivaSelecionada.getIdeTipologiaSubgrupo();
					if (imovelRural.tipologiaSubgrupoJaFoiCadastrada(subgrupo)) {
						if (areaProdutivaSelecionada.podeCadastrarTipologiaSomenteUmaVez(subgrupo)) {
							throw new Exception("Atividade desenvolvida já cadastrada");
						}
						Collection<AreaProdutivaTipologiaAtividade> apta = areaProdutivaSelecionada
								.getAreaProdutivaTipologiaAtividadeCollection();
						if (imovelRural.tipologiaAtividadeJaFoiCadastrada(apta)) {
							throw new Exception("Atividade desenvolvida com a atividade selecionada já cadastrada");
						}
					}
				} else if (areaProdutivaSelecionada.possuiTipologiaCadastrada()) {
					Tipologia grupo = areaProdutivaSelecionada.getIdeTipologia();
					if (imovelRural.tipologiaGrupoJaFoiCadastrada(grupo)) {
						if (areaProdutivaSelecionada.podeCadastrarTipologiaSomenteUmaVez(grupo)) {
							throw new Exception("Atividade desenvolvida já cadastrada");
						}
					}
				}
			}
		}
	}

	private Tipologia retornaGrupoAtividade(Integer ideTipologia) {
		for (Tipologia tipologia : listaGrupoAtividade) {
			if (tipologia.getIdeTipologia().equals(ideTipologia))
				return tipologia;
		}
		return null;
	}

	private Tipologia retornaSubGrupoAtividade(Integer ideSubTipologia) {
		for (Tipologia tipologia : listaSubGrupoAtividade) {
			if (tipologia.getIdeTipologia().equals(ideSubTipologia))
				return tipologia;
		}
		return null;
	}

	private Tipologia retornaDivisaoAtividadeSelecionada(Integer ideSubTipologia) {
		for (Tipologia tipologia : listaDivisaoAtividade) {
			if (tipologia.getIdeTipologia().equals(ideSubTipologia))
				return tipologia;
		}
		return null;
	}

	/**
	 * Método chamado quando clica no botão novo na aba de atividades desenvolvidas
	 */

	public void cadastrarAreaProdutiva() {
		limpaFormAreaProdutiva();
		carregarDivisaoAtividade();
		isInclusaoAtividade = false;
		isEdicaoAp = false;
		visualizacaoAp = false;
	}

	public void editarAreaProdutiva() {
		this.divisaoAtividade = areaProdutivaSelecionada.getIdeTipologia().getIdeTipologiaPai();
		carregarDivisaoAtividade();
		carregarGrupoAtividadePorDivisao();
		carregarSubGrupoAtividadePorGrupo();
		carregarTipologiaAtividade();
		carregarMetodoIrrigacao();
		isInclusaoAtividade = false;
		this.isEdicaoAp = true;
		this.visualizacaoAp = false;
		this.listaDocumentoPlanoManejo = new ArrayList<DocumentoImovelRural>();
		carregarAreaProdutiva();
	}

	public void visualizarAreaProdutiva() {
		this.divisaoAtividade = areaProdutivaSelecionada.getIdeTipologia().getIdeTipologiaPai();
		carregarDivisaoAtividade();
		carregarGrupoAtividadePorDivisao();
		carregarSubGrupoAtividadePorGrupo();
		carregarTipologiaAtividade();
		carregarMetodoIrrigacao();
		isInclusaoAtividade = false;
		this.isEdicaoAp = false;
		this.visualizacaoAp = true;
		carregarAreaProdutiva();
	}

	public void carregarAreaProdutiva() {

		try {
			if (!Util.isNullOuVazio(areaProdutivaSelecionada.getIdeTipologiaSubgrupo())) {
				switch (areaProdutivaSelecionada.getIdeTipologiaSubgrupo().getIdeTipologia()) {
				case 6:// Agricultura irrigada
					if (!Util.isNullOuVazio(areaProdutivaSelecionada.getIdeAreaProdutiva())) {
						for (AreaProdutivaTipologiaAtividade ta : areaProdutivaSelecionada
								.getAreaProdutivaTipologiaAtividadeCollection()) {
							ta.setIdeAreaProdutivaTipologiaAtividadeAgricultura(
									imovelRuralServiceFacade.carregarTipologiaAtividadeAgriculturaByIde(ta));
						}
					}

					this.pnlAgricultura = true;
					this.pnlAnimal = false;
					this.pnlPiscicultura = false;
					break;
				case 8:
				case 10:// Pecuaria e criacoes confinadas
					this.pnlAgricultura = false;
					this.pnlAnimal = true;
					this.pnlPiscicultura = false;
					break;
				case 16:// Aquicultura
					this.pnlAgricultura = false;
					this.pnlAnimal = false;
					this.pnlPiscicultura = true;
					break;
				default:
					this.pnlAgricultura = false;
					this.pnlAnimal = false;
					this.pnlPiscicultura = false;
					break;
				}
			} else {
				this.pnlAgricultura = false;
				this.pnlAnimal = false;
				this.pnlPiscicultura = false;
			}

			if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao().equals(2)) {
				this.temLocGeoShapeAreaProdutiva = true;
				this.temLocGeoDesenhoAreaProdutiva = false;
				this.setOpcaoDesenhoAp(false);
			} else if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao().equals(3)) {
				this.temLocGeoShapeAreaProdutiva = false;
				this.temLocGeoDesenhoAreaProdutiva = true;
				this.setOpcaoDesenhoAp(true);
			}
			areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			if (!Util.isNull(areaProdutivaSelecionada.getIdeDocumentoAutorizacaoManejo())) {
				this.listaDocumentoPlanoManejo.add(areaProdutivaSelecionada.getIdeDocumentoAutorizacaoManejo());
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public String retornaDscMesesIrrigacao(String pMeses) {
		return Util.converteNumToDscMeses(pMeses);
	}

	public String retornaDscTipoUsoAgua(Integer ideTipoUsoAgua) {
		if (ideTipoUsoAgua == 3) {
			return "Dessedentação e Limpeza";
		} else if (ideTipoUsoAgua == 1) {
			return "Dessedentação";
		} else if (ideTipoUsoAgua == 2) {
			return "Limpeza";
		} else {
			return "Nenhum uso informado";
		}
	}

	public String retornaDscManejo(AreaProdutivaTipologiaAtividadeAnimal aptaa) {
		String dscManejo = "";
		if (aptaa.isIndManejoCria()) {
			dscManejo += "Cria - ";
		}
		if (aptaa.isIndManejoEngorda()) {
			dscManejo += "Engorda - ";
		}
		if (aptaa.isIndManejoRecria()) {
			dscManejo += "Recria - ";
		}
		if (aptaa.isIndManejoReproducao()) {
			dscManejo += "Reprodução - ";
		}

		if (!dscManejo.isEmpty() && dscManejo.length() > 2) {
			dscManejo = dscManejo.substring(0, dscManejo.length() - 3);
		}

		return dscManejo;
	}

	public void incluirTipologiaAtividade(boolean incluir) {
		if (incluir) {
			isInclusaoAtividade = true;

			final boolean possuiSubGrupoAtividade = !Util.isNullOuVazio(this.listaSubGrupoAtividade);
			if (possuiSubGrupoAtividade) {
				atualizarPainelAtividadesDesenvolvidas(
						areaProdutivaSelecionada.getIdeTipologiaSubgrupo().getIdeTipologia());
			} else {
				atualizarPainelAtividadesDesenvolvidas(areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia());
			}

		} else {
			isInclusaoAtividade = false;
		}
	}

	private void limpaFormTipologiaAtividade() {
		this.isInclusaoAtividade = false;
		this.tipologiaAtividade = null;
		limpaFormTipologiaAtividadeAgricultura();
		limpaFormTipologiaAtividadeAnimal();
		limpaFormTipologiaAtividadePiscicultura();
	}

	private void limpaFormTipologiaAtividadeAgricultura() {
		this.metodoIrrigacao = null;
		this.dscMesesIrrigacao = "";
		this.numMesesIrrigacao = "";
		this.temMesJan = false;
		this.temMesFev = false;
		this.temMesMar = false;
		this.temMesAbr = false;
		this.temMesMai = false;
		this.temMesJun = false;
		this.temMesJul = false;
		this.temMesAgo = false;
		this.temMesSet = false;
		this.temMesOut = false;
		this.temMesNov = false;
		this.temMesDez = false;
	}

	private void limpaFormTipologiaAtividadeAnimal() {
		this.qtdCabeca = null;
		this.tipoUsoAgua = null;
		this.dscTipoUsoAgua = "";
		this.temUsoDessedentacao = false;
		this.temUsoLimpeza = false;
		this.indManejoCria = false;
		this.indManejoEngorda = false;
		this.indManejoRecria = false;
		this.indManejoReproducao = false;
	}

	private void limpaFormTipologiaAtividadePiscicultura() {
		this.valVolume = null;
	}

	public void excluirTipologiaAtividade(AreaProdutivaTipologiaAtividade pAtividadeExcluir) {
		areaProdutivaSelecionada.getAreaProdutivaTipologiaAtividadeCollection().remove(pAtividadeExcluir);
	}

	public void carregarMetodoIrrigacao() {
		try {
			this.listaMetodoIrrigacao = (List<MetodoIrrigacao>) imovelRuralServiceFacade.listarMetodoIrrigacao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public boolean existeTheGeomByIdeLocGeo(Integer pIdeLocGeo) throws Exception {
		return imovelRuralServiceFacade.verificaSeExisteTheGeomValido(pIdeLocGeo);
	}

	public void excluirAreaProduitiva() {
		try {
			if (!imovelRural.getIsFinalizado()) {
				persistirExclusaoAreaProduitiva(areaProdutivaSelecionada);
				imovelRural.getAreaProdutivaCollection().remove(areaProdutivaSelecionada);
			} else {
				if (!Util.isNullOuVazio(areaProdutivaSelecionada.getIdeAreaProdutiva()))
					areaProdutivaSelecionada.setIndExcluido(true);
				else
					imovelRural.getAreaProdutivaCollection().remove(areaProdutivaSelecionada);
			}

			JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
		} catch (Exception exception) {
			JsfUtil.addErrorMessage("Erro ao Excluir Área Produtiva");
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	public void botaoAreaProdutiva() {
		if (!Util.isNullOuVazio(imovelRural.getAreaProdutivaCollection())) {
			imovelRural.getAreaProdutivaCollection().clear();
		}
	}

	private void persistirExclusaoAreaProduitiva(AreaProdutiva areaProdutiva) throws Exception {
		imovelRuralServiceFacade.excluirAreaProdutiva(areaProdutiva);
		if (!Util.isNullOuVazio(areaProdutiva.getIdeLocalizacaoGeografica()))
			imovelRuralServiceFacade.excluirLocalizacaoGeografica(areaProdutiva.getIdeLocalizacaoGeografica());
		auditoria.excluir(areaProdutiva);
	}

	public void carregarTipologia() {
	}

	public void carregarSubTipologia() {
		try {
			if (this.tipologia != null && !(this.tipologia.getIdeTipologia() == null)) {
				this.listaSubTipologia = (List<Tipologia>) imovelRuralServiceFacade
						.filtrarListaTipologiasByIdePai(this.tipologia);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}

	public void limpaFormAreaProdutiva() {
		this.divisaoAtividade = null;
		this.listaGrupoAtividade = null;
		this.listaSubGrupoAtividade = null;
		this.subTipologia = null;
		this.valAreaProdutiva = null;
		this.temLicAreaProdutiva = null;
		this.numProcessoAp = null;
		this.tipologiaAtividade = null;
		this.pnlAgricultura = false;
		this.pnlAnimal = false;
		this.pnlPiscicultura = false;
		this.metodoIrrigacao = null;
		this.indManejoCria = false;
		this.indManejoEngorda = false;
		this.indManejoRecria = false;
		this.indManejoReproducao = false;
		this.sistemaCoordenadaShapeAreaProdutiva = null;
		this.secaoGeometricaShapeAreaProdutiva = null;
		this.descLocGeoShapeAreaProdutiva = null;
		this.areaProdutivaIdeLocGeoSelecionada = null;
		this.areaProdutivaSelecionada = null;
		this.temUplShapefileAreaProdutiva = false;
		this.temLocGeoShapeAreaProdutiva = false;
		this.temLocGeoDesenhoAreaProdutiva = false;
		this.listaDocumentoPlanoManejo = new ArrayList<DocumentoImovelRural>();
		this.isInclusaoAtividade = false;

		areaProdutivaSelecionada = new AreaProdutiva();
		areaProdutivaSelecionada.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		areaProdutivaSelecionada
				.setAreaProdutivaTipologiaAtividadeCollection(new ArrayList<AreaProdutivaTipologiaAtividade>());
		areaProdutivaSelecionada.setIdeTipologia(new Tipologia());
		areaProdutivaSelecionada.setIdeTipologiaSubgrupo(new Tipologia());
		areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
		areaProdutivaSelecionada.setCodigoPersistirShape(Util.getStringAlfanumAleatoria(5));
	}

	public void importarShapeAp(FileUploadEvent event) {
		String caminhoArquivo;
		try {
			if (Util.isNullOuVazio(areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape())) {
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			}

			if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().size() > 3) {
				JsfUtil.addWarnMessage("Não é possível enviar mais de 4 arquivos.");
			} else {
				caminhoArquivo = FileUploadUtil.EnviarShape(event,
						DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural().toString()
								+ "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId() + "_"
								+ areaProdutivaSelecionada.getCodigoPersistirShape().toString(),
						"shapeAtividadeDesenvolvida");

				if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape()
						.contains(FileUploadUtil.getFileName(caminhoArquivo))) {
					JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
				} else {
					areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape()
							.add(FileUploadUtil.getFileName(caminhoArquivo));

					if (caminhoArquivo.contains(".prj")) {
						areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
								.setIdeSistemaCoordenada(imovelRuralServiceFacade.obterSistCoordPRJ(caminhoArquivo));

						if (Util.isNull(
								areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
							JsfUtil.addErrorMessage(Util.getString("cefir_msg_validacao_srid_prj"));
							FileUploadUtil.removerArquivos(new File(caminhoArquivo));
							areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape()
									.remove(FileUploadUtil.getFileName(caminhoArquivo));
							return;
						}
					}
					if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().size() == 4) {
						RequestContext.getCurrentInstance().execute("dlgUploadShapeAp.hide()");
					}
					JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("geral_msg_erro_envio_arquivo"));
		}
	}

	public void persistirShapesAp(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		String caminhoArquivosShape = DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
				+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId()
				+ "_" + areaProdutivaSelecionada.getCodigoPersistirShape().toString().toString()
				+ FileUploadUtil.getFileSeparator();
		String nome = "shapeAtividadeDesenvolvida";
		String nomeNovo = imovelRural.getIdeImovelRural().toString() + "_"
				+ areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid();

		try {
			areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
					.setIdeClassificacaoSecao(retornaClassificacaoSecaoGeometricaSelecionado(areaProdutivaSelecionada
							.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()));

			areaProdutivaSelecionada.getIdeLocalizacaoGeografica()
					.setIdeSistemaCoordenada(retornaSistemaCordenadaSelecionado(areaProdutivaSelecionada
							.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getIdeSistemaCoordenada()));

			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nome, nomeNovo);

			String[] retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					imovelRural.getIdeImovelRural(),
					imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId()
							+ "_" + areaProdutivaSelecionada.getCodigoPersistirShape().toString(),
					areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(),
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
							.getCoordGeobahiaMunicipio());

			if (retorno == null || retorno.length < 2) {
				throw new Exception("Erro ao persistir Localização Geográfica da Atividade Desenvolvida");
			} else if (retorno[0].equalsIgnoreCase("ERRO")) {
				throw new Exception(retorno[2] + " [" + retorno[1] + "]");
			}

			String geometriaAp = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
					imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId(),
					areaProdutivaSelecionada.getCodigoPersistirShape().toString());

			if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaAp)) {
				throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
			}

			areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
			areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
		} catch (Exception e) {
			FileUploadUtil.renomearArquivoShape(caminhoArquivosShape, nomeNovo, nome);
			throw new Exception(e.getMessage());
		}
	}

	public void persistirAreaProdutiva(AreaProdutiva pAreaProdutiva) throws Exception {
		List<AreaProdutivaTipologiaAtividade> listaAtividade = new ArrayList<AreaProdutivaTipologiaAtividade>();
		if (pAreaProdutiva.possuiTipologiaAtividadeCadastrada()) {
			listaAtividade.addAll(pAreaProdutiva.getAreaProdutivaTipologiaAtividadeCollection());
		}

		if (pAreaProdutiva.getIdeLocalizacaoGeografica().getLocalizacaoExcluida()) {
			if (pAreaProdutiva.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() != null) {
				imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(
						pAreaProdutiva.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				pAreaProdutiva.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(null);
			}
			pAreaProdutiva.getIdeLocalizacaoGeografica().setLocalizacaoExcluida(false);
		}
		if (Util.isNullOuVazio(pAreaProdutiva.getIdeAreaProdutiva())) {
			imovelRuralServiceFacade
					.salvarOuAtualizarLocalizacaoGeografica(pAreaProdutiva.getIdeLocalizacaoGeografica());

			pAreaProdutiva.setIdeImovelRural(imovelRural);
			pAreaProdutiva.setAreaProdutivaTipologiaAtividadeCollection(null);
			if (!Util.isNull(pAreaProdutiva.getIdeDocumentoAutorizacaoManejo())) {
				imovelRuralServiceFacade.salvarDocumentoObrigatorio(pAreaProdutiva.getIdeDocumentoAutorizacaoManejo());
			}
			imovelRuralServiceFacade.salvarAreaProdutiva(pAreaProdutiva);
			auditoria.salvar(pAreaProdutiva);

		} else {
			imovelRuralServiceFacade
					.salvarOuAtualizarLocalizacaoGeografica(pAreaProdutiva.getIdeLocalizacaoGeografica());
			if (!Util.isNull(pAreaProdutiva.getIdeDocumentoAutorizacaoManejo())) {
				imovelRuralServiceFacade.salvarDocumentoObrigatorio(pAreaProdutiva.getIdeDocumentoAutorizacaoManejo());
			}
			AreaProdutiva lAreaProdutivaAntigo = imovelRuralServiceFacade.carregarTudoAreaProdutiva(pAreaProdutiva);
			pAreaProdutiva.setAreaProdutivaTipologiaAtividadeCollection(null);
			imovelRuralServiceFacade.atualizarAreaProdutiva(pAreaProdutiva);
			auditoria.atualizar(lAreaProdutivaAntigo, pAreaProdutiva);
		}

		imovelRuralServiceFacade.excluirAllAptaByIdeAreaProdutiva(pAreaProdutiva);

		if (!Util.isNullOuVazio(pAreaProdutiva.getIdeTipologiaSubgrupo())) {

			if (pAreaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia()
					.equals(TipologiaCefirEnum.AGRICULTURA_IRRIGADA.getId())) {
				for (AreaProdutivaTipologiaAtividade apta : listaAtividade) {
					apta.setIdeAreaProdutiva(pAreaProdutiva);
					apta.setIndExcluido(Boolean.FALSE);
					apta.setDtcCriacao(new Date());
					apta.setDtcExclusao(null);
					apta.setIdeAreaProdutivaTipologiaAtividade(null);
					apta.getIdeAreaProdutivaTipologiaAtividadeAgricultura()
							.setIdeAreaProdutivaTipologiaAtividadeAgricultura(null);
					AreaProdutivaTipologiaAtividadeAgricultura aptaAgricultura = apta
							.getIdeAreaProdutivaTipologiaAtividadeAgricultura();
					apta.setIdeAreaProdutivaTipologiaAtividadeAgricultura(null);
					imovelRuralServiceFacade.salvarAreaProdutivaTipologiaAtividade(apta);
					auditoria.salvar(apta);
					aptaAgricultura.setIdeAreaProdutivaTipologiaAtividade(apta);
					imovelRuralServiceFacade.salvarAreaProdutivaTipologiaAtividadeAgricultura(aptaAgricultura);
					auditoria.salvar(aptaAgricultura);
					apta.setIdeAreaProdutivaTipologiaAtividadeAgricultura(aptaAgricultura);
				}
			} else if (pAreaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(8)
					|| pAreaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(10)) {
				for (AreaProdutivaTipologiaAtividade apta : listaAtividade) {
					apta.setIdeAreaProdutiva(pAreaProdutiva);
					apta.setIndExcluido(Boolean.FALSE);
					apta.setDtcCriacao(new Date());
					apta.setDtcExclusao(null);
					AreaProdutivaTipologiaAtividadeAnimal aptaAnimal = apta
							.getIdeAreaProdutivaTipologiaAtividadeAnimal();
					apta.setIdeAreaProdutivaTipologiaAtividadeAnimal(null);
					imovelRuralServiceFacade.salvarAreaProdutivaTipologiaAtividade(apta);
					auditoria.salvar(apta);
					aptaAnimal.setIdeAreaProdutivaTipologiaAtividade(apta);
					imovelRuralServiceFacade.salvarAreaProdutivaTipologiaAtividadeAnimal(aptaAnimal);
					auditoria.salvar(aptaAnimal);
					apta.setIdeAreaProdutivaTipologiaAtividadeAnimal(aptaAnimal);
				}
			} else if (pAreaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(16)) {
				for (AreaProdutivaTipologiaAtividade apta : listaAtividade) {
					apta.setIdeAreaProdutiva(pAreaProdutiva);
					apta.setIndExcluido(Boolean.FALSE);
					apta.setDtcCriacao(new Date());
					apta.setDtcExclusao(null);
					AreaProdutivaTipologiaAtividadePiscicultura aptaPisicultura = apta
							.getIdeAreaProdutivaTipologiaAtividadePiscicultura();
					apta.setIdeAreaProdutivaTipologiaAtividadePiscicultura(null);
					imovelRuralServiceFacade.salvarAreaProdutivaTipologiaAtividade(apta);
					auditoria.salvar(apta);
					aptaPisicultura.setIdeAreaProdutivaTipologiaAtividade(apta);
					imovelRuralServiceFacade.salvarAreaProdutivaTipologiaAtividadePiscicultura(aptaPisicultura);
					auditoria.salvar(aptaPisicultura);
					apta.setIdeAreaProdutivaTipologiaAtividadePiscicultura(aptaPisicultura);
				}
			} else {
				for (AreaProdutivaTipologiaAtividade apta : listaAtividade) {
					apta.setIdeAreaProdutiva(pAreaProdutiva);
					apta.setIndExcluido(Boolean.FALSE);
					apta.setDtcCriacao(new Date());
					apta.setDtcExclusao(null);
					apta.setIdeAreaProdutivaTipologiaAtividade(null);
					imovelRuralServiceFacade.salvarAreaProdutivaTipologiaAtividade(apta);
					auditoria.salvar(apta);
				}
			}
			pAreaProdutiva.setAreaProdutivaTipologiaAtividadeCollection(
					new ArrayList<AreaProdutivaTipologiaAtividade>(listaAtividade));
		}

		if (temShapeTemporario(imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId()
				+ "_" + pAreaProdutiva.getCodigoPersistirShape())) {
			FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginal(
					pAreaProdutiva.getIdeLocalizacaoGeografica(), "4_" + pAreaProdutiva.getCodigoPersistirShape(),
					this.imovelRural.getIdeImovelRural().toString());
			imovelRuralServiceFacade.persistirShapes(pAreaProdutiva.getIdeLocalizacaoGeografica(), null);
			pAreaProdutiva.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
		}
		pAreaProdutiva.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());

	}

	private TipologiaAtividade retornaTipologiaAtividadeSelecionada(Integer ideTipologiaAtividade) {
		for (TipologiaAtividade tipologiaAtividade : listaTipologiaAtividade) {
			if (tipologiaAtividade.getIdeTipologiaAtividade().equals(ideTipologiaAtividade))
				return tipologiaAtividade;
		}
		return null;
	}

	public boolean existeTheGeomAp(AreaProdutiva areaProdutiva) {
		try {
			if (!Util.isNullOuVazio(areaProdutiva.getIdeLocalizacaoGeografica())) {
				if (areaProdutiva.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					return true;
				} else if (!areaProdutiva.getIdeLocalizacaoGeografica().getLocalizacaoExcluida())
					return imovelRuralServiceFacade.verificaSeExisteTheGeomValido(
							areaProdutiva.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			} else if (areaProdutiva.getIdeLocalizacaoGeografica() != null
					&& areaProdutiva.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() == null) {
				if (areaProdutiva.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados())
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return false;
		}
		return false;
	}

	public void pesquisarPctPessoa() throws Exception {

		pctHabilitarCampos = false;

		pctPessoaFisicaSelecionado = new PessoaFisica(pctPessoaFisicaSelecionado.getNumCpf());

		if (Util.isNullOuVazio(pctPessoaFisicaSelecionado.getNumCpf())) {
			MensagemUtil.alerta("Informe o CPF.");
		} else if (existePessoaPctFamilia(pctPessoaFisicaSelecionado)) {
			MensagemUtil.alerta("A pessoa informada já está associada a esta família.");
		} else if (pctFamiliaService.existePessoaPctFamilia(pctPessoaFisicaSelecionado.getNumCpf(), null)) {
			MensagemUtil.alerta("A pessoa informada já está associada a uma família.");
		} else {
			if (Util.isNullOuVazio(pctImovelRural.getPctFamiliaCollection())) {
				pctImovelRural.setPctFamiliaCollection(new ArrayList<PctFamilia>());
				pctImovelRural.setListarPctRepresentanteFamiliaCollection(new ArrayList<PctFamilia>());
				indRepresentanteFamilia = true;
			}

			PessoaFisica pessoaFisica = imovelRuralServiceFacade.filtrarPessoaFisicaByCpf(pctPessoaFisicaSelecionado);
			if (!Util.isNullOuVazio(pessoaFisica)) {
				pctPessoaFisicaSelecionado = pessoaFisica;
				if (Util.isNullOuVazio(pctPessoaFisicaSelecionado.getNomPessoa())
						|| Util.isNullOuVazio(pctPessoaFisicaSelecionado.getDtcNascimento())
						|| Util.isNullOuVazio(pctPessoaFisicaSelecionado.getNomMae())) {

					MensagemUtil.alerta(
							"É necessário completar o cadastro da pessoa física para adicioná-la como membro da família. "
									+ "Edite o cadastro para preenchimento dos dados ou entre em contato com o atendimento.");
				}
			} else {
				pctHabilitarCampos = true;
				MensagemUtil.alerta("Preencha os dados para cadastrar este CPF como um membro da comunidade.");
			}
		}
	}

	private boolean existePessoaPctFamilia(PessoaFisica pctPessoaFisicaSelecionado) {
		if (!Util.isNullOuVazio(pctImovelRural.getPctFamiliaCollection())) {
			for (Iterator<PctFamilia> i = pctImovelRural.getPctFamiliaCollection().iterator(); i.hasNext();) {
				PctFamilia pctFamilia1 = i.next();
				if (pctPessoaFisicaSelecionado.getNumCpf()
						.equals(pctFamilia1.getIdePessoa().getPessoaFisica().getNumCpf())) {
					return true;
				} else {
					for (Iterator<PctFamilia> j = pctFamilia1.getMembrosFamiliaCollection().iterator(); j.hasNext();) {
						PctFamilia pctFamilia2 = j.next();
						if (pctPessoaFisicaSelecionado.getNumCpf()
								.equals(pctFamilia2.getIdePessoa().getPessoaFisica().getNumCpf())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void excluirPctFamilia() throws Exception {

		if (Util.isNullOuVazio(pctImovelRural.getPctFamiliaExclusaoCollection())) {
			pctImovelRural.setPctFamiliaExclusaoCollection(new ArrayList<PctFamilia>());
		}

		pctImovelRural.getPctFamiliaExclusaoCollection().add(pctFamiliaExclusaoSelecionada);

		if (pctImovelRural.getPctFamiliaCollection().contains(pctFamiliaExclusaoSelecionada)) {
			pctFamiliaCollectionRemoverItem(pctImovelRural.getPctFamiliaCollection(), pctFamiliaExclusaoSelecionada);
			if (pctImovelRural.getListarPctRepresentanteFamiliaCollection().contains(pctFamiliaExclusaoSelecionada)) {
				pctFamiliaCollectionRemoverItem(pctImovelRural.getListarPctRepresentanteFamiliaCollection(),
						pctFamiliaExclusaoSelecionada);
			}
		} else {
			for (Iterator<PctFamilia> iterator = pctImovelRural.getPctFamiliaCollection().iterator(); iterator
					.hasNext();) {
				PctFamilia pctFamilia = iterator.next();
				pctFamiliaCollectionRemoverItem(pctFamilia.getMembrosFamiliaCollection(),
						pctFamiliaExclusaoSelecionada);
			}
		}

		pctPessoaFisicaSelecionado = new PessoaFisica();
		Html.atualizar("formCadImovelRural:tabAbasDadosEspecificos:panelPovosComunidadesTradicionais");

		if (Util.isNullOuVazio(pctImovelRural.getListarPctRepresentanteFamiliaCollection())) {
			this.indRepresentanteFamilia = true;
		}
	}

	private void pctFamiliaCollectionRemoverItem(Collection<PctFamilia> pctFamiliaCollection,
			PctFamilia pctFamiliaExclusaoSelecionada) {
		if (Util.isNullOuVazio(pctFamiliaExclusaoSelecionada)) {
			List<PctFamilia> lista = (List<PctFamilia>) pctFamiliaCollection;
			for (int i = lista.size() - 1; i >= 0; i--) {
				if (lista.get(i) == pctFamiliaExclusaoSelecionada) {
					lista.remove(i);
					break;
				}

				List<PctFamilia> membros = (List<PctFamilia>) lista.get(i).getMembrosFamiliaCollection();
				if (!Util.isNullOuVazio(membros)) {
					for (int j = membros.size() - 1; j >= 0; j--) {
						if (membros.get(j) == pctFamiliaExclusaoSelecionada) {
							membros.remove(j);
							break;
						}
					}
				}
			}
		} else {
			pctFamiliaCollection.remove(pctFamiliaExclusaoSelecionada);
		}
	}

	public void adicionarPctProprietarioPossuidor() throws Exception {
		try {
			if (indRepresentanteFamilia && !Util.isNullOuVazio(pessoaRepresentante)) {

				pctImovelRural.setPessoaRepresentanteFamiliaSelecionada(new PctFamilia());
				pessoaRepresentante = new PctFamilia();

			} else if (!indRepresentanteFamilia) {
				pctImovelRural.setPessoaRepresentanteFamiliaSelecionada(pessoaRepresentante);
			}

			pctFamiliaService.validarPctPessoaFisica(pctImovelRural, pctPessoaFisicaSelecionado, null,
					indRepresentanteFamilia);
			pctFamiliaService.adicionarPctProprietarioPossuidor(pctImovelRural, pctPessoaFisicaSelecionado,
					indRepresentanteFamilia);

			pctHabilitarCampos = false;
			pctPessoaFisicaSelecionado = new PessoaFisica();

			MensagemUtil.sucesso(BUNDLE.getString("geral_msg_inclusao_sucesso"));

			pctImovelRural.setPessoaRepresentanteFamiliaSelecionada(new PctFamilia());
			pessoaRepresentante = null;
			this.setIndRepresentanteFamilia(false);

		} catch (SeiaValidacaoRuntimeException e) {
			MensagemUtil.alerta(e.getMessage());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar salvar os dados.");
			e.printStackTrace();
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void salvarPCT() throws Exception {
		pctFamiliaService.salvarPCT(this.pctImovelRural);

		carregarPCTProprietarioPossuidor();
		carregarPCTRepresentanteFamilia();
		MensagemUtil.sucesso(BUNDLE.getString("geral_msg_inclusao_sucesso"));
	}

	public StreamedContent getDownloadPlanilhaPCT() {
		try {
			return pctFamiliaService.getDownloadPlanilhaPCT();
		} catch (Exception e) {
			e.printStackTrace();
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	public void pesquisarPctPessoaJuridica() throws Exception {

		if (!existePessoaJuridicaAssociadaComunidade()) {

			PessoaJuridica pessoaJuridica = new PessoaJuridica(pessoaJuridicaPct.getIdePessoaJuridica().getNumCnpj());

			if (!Util.isNullOuVazio(pessoaJuridica.getNumCnpj())) {
				pessoaJuridica = imovelRuralServiceFacade.filtrarPessoaJuridicaByCnpj(pessoaJuridica);
				if (!Util.isNullOuVazio(pessoaJuridica)) {
					pessoaJuridicaPct.setIdePessoaJuridica(pessoaJuridica);
					pctHabilitarCamposCnpj = false;
				} else {
					pctHabilitarCamposCnpj = true;
					String numCnpj = pessoaJuridicaPct.getIdePessoaJuridica().getNumCnpj();
					pessoaJuridicaPct.setIdePessoaJuridica(new PessoaJuridica());
					pessoaJuridicaPct.getIdePessoaJuridica().setNumCnpj(numCnpj);

					pessoaJuridicaPct.setIdePessoaFisicaRepresentanteLegal(new PessoaFisica());

					pessoaJuridicaPct.setIdeTelefone(new Telefone());
				}
			}
		} else {
			MensagemUtil.alerta("Esse CNPJ já está associado a comunidade!");
		}
	}

	public void adicionarPessoaJuridicaPct(ActionEvent evt) {

		TipoPessoaJuridicaPctEnum tipoPessoaJuridicaPctEnum = TipoPessoaJuridicaPctEnum.getEnum(evt);

		try {
			if (!existePessoaJuridicaAssociadaComunidade()) {
				pctImovelRural.setIdeImovelRural(imovelRural);
				pessoaJuridicaPct.setIdePctImovelRural(pctImovelRural);
				pessoaJuridicaPct.setDtcCadastro(new Date());
				pessoaJuridicaPct.setIndExcluido(false);
				pessoaJuridicaPct.setIdeTipoPessoaJuridicaPct(new TipoPessoaJuridicaPct(tipoPessoaJuridicaPctEnum));

				if (Util.isNullOuVazio(pctImovelRural.getPessoaJuridicaPctList())) {
					pctImovelRural.setPessoaJuridicaPctList(new ArrayList<PessoaJuridicaPct>());
				}

				pctImovelRural.getPessoaJuridicaPctList().add(pessoaJuridicaPct);

				pessoaJuridicaPct = new PessoaJuridicaPct();
				pessoaJuridicaPct.setIdePessoaJuridica(new PessoaJuridica());
				pessoaJuridicaPct.setIdePessoaFisicaRepresentanteLegal(new PessoaFisica());
				pessoaJuridicaPct.setIdeTelefone(new Telefone());

				pctHabilitarCamposCnpj = false;

				pctHabilitarCampoRepresentanteLegal = false;

				atualizarListaPessoaJuridicaPct(tipoPessoaJuridicaPctEnum);

				MensagemUtil.sucesso(BUNDLE.getString("geral_msg_inclusao_sucesso"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void atualizarListaPessoaJuridicaPct(TipoPessoaJuridicaPctEnum tipoPessoaJuridicaPctEnum) {

		pctImovelRural.setTipoPessoaJuridicaPctEnum(tipoPessoaJuridicaPctEnum);

		if (!Util.isNullOuVazio(pctImovelRural.getPessoaJuridicaPctList())) {

			List<PessoaJuridicaPct> lista = null;

			switch (tipoPessoaJuridicaPctEnum) {
			case ASSOCIACAO:
				pctImovelRural.setPessoaJuridicaPctListaAssociacao(new ArrayList<PessoaJuridicaPct>());
				lista = pctImovelRural.getPessoaJuridicaPctListaAssociacao();
				break;
			case CONCEDENTE:
				pctImovelRural.setPessoaJuridicaPctListaConcedente(new ArrayList<PessoaJuridicaPct>());
				lista = pctImovelRural.getPessoaJuridicaPctListaConcedente();
				break;
			case CONCESSIONARIO:
				pctImovelRural.setPessoaJuridicaPctListaConcessionario(new ArrayList<PessoaJuridicaPct>());
				lista = pctImovelRural.getPessoaJuridicaPctListaConcessionario();
				break;
			default:
				return;
			}

			for (PessoaJuridicaPct pjPct : pctImovelRural.getPessoaJuridicaPctList()) {
				if (tipoPessoaJuridicaPctEnum.getId()
						.equals(pjPct.getIdeTipoPessoaJuridicaPct().getIdeTipoPessoaJuridicaPct())) {
					lista.add(pjPct);
				}
			}
		} else {
			pctImovelRural.setPessoaJuridicaPctListaAssociacao(null);
			pctImovelRural.setPessoaJuridicaPctListaConcedente(null);
			pctImovelRural.setPessoaJuridicaPctListaConcessionario(null);

		}
	}

	public boolean existePessoaJuridicaAssociadaComunidade() {

		boolean retorno = false;

		try {
			retorno = imovelRuralServiceFacade.existePessoaJuridicaAssociadaComunidade(pctImovelRural,
					pessoaJuridicaPct);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retorno;
	}

	public void pesquisarPctPessoaRepresentante() {
		try {
			PessoaFisica pessoaFisica = null;
			String numCpf = pessoaJuridicaPct.getNumCpf();

			pessoaJuridicaPct.setIdePessoaJuridica(new PessoaJuridica());
			pessoaJuridicaPct.setIdePessoaFisicaRepresentanteLegal(new PessoaFisica());
			pessoaJuridicaPct.setIdeTelefone(new Telefone());

			if (Util.isNullOuVazio(numCpf)) {
				pctHabilitarCampoRepresentanteLegal = false;
				throw new AppExceptionError("Nenhum CPF foi informado.");
			} else {

				if (!Util.isNullOuVazio(pctImovelRural.getPctFamiliaCollection())) {
					for (PctFamilia pctFamilia : pctImovelRural.getPctFamiliaCollection()) {
						if (numCpf.equals(pctFamilia.getIdePessoa().getPessoaFisica().getNumCpf())) {
							pessoaFisica = pctFamilia.getIdePessoa().getPessoaFisica();
							break;
						}

						if (!Util.isNullOuVazio(pctFamilia.getMembrosFamiliaCollection())) {
							for (PctFamilia pctFamiliaMembros : pctFamilia.getMembrosFamiliaCollection()) {
								if (numCpf.equals(pctFamiliaMembros.getIdePessoa().getPessoaFisica().getNumCpf())) {
									pessoaFisica = pctFamiliaMembros.getIdePessoa().getPessoaFisica();
									break;
								}
							}
						}
					}
				}

				if (Util.isNull(pessoaFisica)) {
					pessoaFisica = imovelRuralServiceFacade.filtrarPessoaFisicaByCpf(new PessoaFisica(numCpf));
				}

				if (Util.isNull(pessoaFisica)) {
					pctHabilitarCampoRepresentanteLegal = true;
					pessoaJuridicaPct.getIdePessoaFisicaRepresentanteLegal().setNumCpf(numCpf);
				} else {
					if (Util.isNull(pessoaFisica.getIdePessoaFisica())) {
						pctHabilitarCampoRepresentanteLegal = true;
					} else {
						pctHabilitarCampoRepresentanteLegal = false;
						pessoaJuridicaPct
								.setIdeTelefone(imovelRuralServiceFacade.buscarTelefonesPorPessoa(pessoaFisica));
					}
					pessoaJuridicaPct.setIdePessoaFisicaRepresentanteLegal(pessoaFisica);
				}
			}
		} catch (AppExceptionError e) {
			MensagemUtil.alerta(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setarPessoaJuridicaExclusao(ActionEvent evt) throws Exception {
		pessoaJuridicaPctExclusaoSelecionada = (PessoaJuridicaPct) evt.getComponent().getAttributes()
				.get("pessoaJuridica");
		TipoPessoaJuridicaPctEnum tipoPessoaJuridicaPctEmun = TipoPessoaJuridicaPctEnum.getEnum(evt);
		pctImovelRural.setTipoPessoaJuridicaPctEnum(tipoPessoaJuridicaPctEmun);
		setarClientId(evt.getComponent().getClientId(), tipoPessoaJuridicaPctEmun);

	}

	private void setarClientId(String clientId, TipoPessoaJuridicaPctEnum tipoPessoaJuridicaPctEmun) {
		this.clientId = clientId.substring(0, clientId.lastIndexOf(tipoPessoaJuridicaPctEmun.getValue()));
		this.clientId += tipoPessoaJuridicaPctEmun.getValue() + ":panelPctCnpj";
	}

	public void excluirPctPessoaJuridica() throws Exception {
		if (Util.isNullOuVazio(pctImovelRural.getPessoaJuridicaPctListaExclusao())) {
			pctImovelRural.setPessoaJuridicaPctListaExclusao(new ArrayList<PessoaJuridicaPct>());
		}

		pctImovelRural.getPessoaJuridicaPctListaExclusao().add(pessoaJuridicaPctExclusaoSelecionada);

		if (Util.isNullOuVazio(pessoaJuridicaPctExclusaoSelecionada)) {
			for (int i = pctImovelRural.getPessoaJuridicaPctList().size() - 1; i > 0; i--) {
				PessoaJuridicaPct pj = pctImovelRural.getPessoaJuridicaPctList().get(i);
				if (Util.isNullOuVazio(pj) && Util.isNullOuVazio(pessoaJuridicaPctExclusaoSelecionada)
						&& pj == pessoaJuridicaPctExclusaoSelecionada) {
					pctImovelRural.getPessoaJuridicaPctList().remove(i);
				}
			}
		} else {
			pctImovelRural.getPessoaJuridicaPctList().remove(pessoaJuridicaPctExclusaoSelecionada);
		}

		atualizarListaPessoaJuridicaPct(pctImovelRural.getTipoPessoaJuridicaPctEnum());

		Html.atualizar(clientId);
	}

	public void existemAssociacoes() throws Exception {

		if (!Util.isNullOuVazio(pctImovelRural.getIndAssociacaoComunidade())) {

			if (!pctImovelRural.getIndAssociacaoComunidade()) {

				if (Util.isNullOuVazio(pctImovelRural.getPessoaJuridicaPctList())) {
					imovelRuralServiceFacade.salvarPctImovelRural(pctImovelRural);
					Html.atualizar("formCadImovelRural:tabAbasDadosEspecificos:associacao:panelPctCnpj");
				} else {

					Html.exibir("confirmacaoAlterarAssociacaoComunidade");
				}
			} else if (pctImovelRural.getIndAssociacaoComunidade()) {
				imovelRuralServiceFacade.salvarPctImovelRural(pctImovelRural);
				Html.atualizar("formCadImovelRural:tabAbasDadosEspecificos:associacao:panelPctCnpj");
			}
		}

	}

	public void atualizarGridAssociacao() throws Exception {

		if (!pctImovelRural.getIndAssociacaoComunidade()) {
			imovelRuralServiceFacade.salvarPctImovelRural(pctImovelRural);

			if (!Util.isNullOuVazio(pctImovelRural.getPessoaJuridicaPctList())) {
				for (PessoaJuridicaPct pessoaJuridicaPct : pctImovelRural.getPessoaJuridicaPctList()) {
					imovelRuralServiceFacade.excluirPessoaJuridicaPct(pessoaJuridicaPct);
				}
				pctImovelRural.getPessoaJuridicaPctList().clear();
				Html.atualizar("formCadImovelRural:tabAbasDadosEspecificos:panelPctCnpj");
			}
		}
	}

	// Início Gets e Sets Área Produtiva
	public Boolean getMostrarBotaoUploadShapeAp() {
		if (Util.isNullOuVazio(areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape())
				|| areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().size() < 4)
			return true;
		else
			return false;
	}

	public Collection<AreaProdutiva> getListaAreaProdutiva() {
		listaAreaProdutiva = new ArrayList<AreaProdutiva>();
		if (Util.isLazyInitExcepOuNull(imovelRural.getAreaProdutivaCollection())) {
			imovelRural.setAreaProdutivaCollection(new ArrayList<AreaProdutiva>());
		}
		if (!Util.isNullOuVazio(imovelRural) && !Util.isLazyInitExcepOuNull(imovelRural.getAreaProdutivaCollection())
				&& !Util.isNullOuVazio(imovelRural.getAreaProdutivaCollection())) {
			for (AreaProdutiva ap : imovelRural.getAreaProdutivaCollection()) {
				if (!ap.getIndExcluido())
					listaAreaProdutiva.add(ap);
			}
		}
		return listaAreaProdutiva;
	}

	public boolean getRenderedSubGrupoAtividade() {
		return !Util.isNullOuVazio(this.listaSubGrupoAtividade);
	}

	public boolean getRenderedIncluirAtividade() {

		final int AGRICULTURA = 3;
		final int PECUARIA = 8;

		if (!Util.isNullOuVazio(this.listaTipologiaAtividade) && areaProdutivaSelecionada.possuiTipologiaCadastrada()
				&& areaProdutivaSelecionada.possuiTipologiaSubGrupoCadastrada()) {
			if (areaProdutivaSelecionada.possuiTipologiaAtividadeCadastrada()) {
				if (!areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia().equals(AGRICULTURA)
						&& !areaProdutivaSelecionada.getIdeTipologiaSubgrupo().getIdeTipologia().equals(PECUARIA)) {
					return false;
				}
			}
		} else {
			return false;
		}

		return true;
	}

	public void prepararUpload() {

		if (isLiConcordoImportacaoPlanilha()) {
			try {
				List<String> lista = new ArrayList<String>();

				pctFamiliaService.adicionarPctFamiliaCSV(getFileUploadEventPlanilhaPct(), pctImovelRural, lista);
				MensagemUtil.sucesso(Util.getString("geral_msg_inclusao_sucesso"));

				if (!Util.isNullOuVazio(lista)) {
					gerarTxt(lista);
					Html.exibir("pctTxt");
				}
			} catch (SeiaValidacaoRuntimeException e) {
				MensagemUtil.alerta(e.getMessage());
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro ao tentar salvar os dados.");
				e.printStackTrace();
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			} finally {
				setLiConcordoImportacaoPlanilha(false);
			}
		}
	}

	public void gerarTxt(List<String> lista) throws IOException {

		StringBuffer stringBuffer = new StringBuffer();

		for (String item : lista) {
			stringBuffer.append(item);
			stringBuffer.append("\n");
		}

		InputStream is = new ByteArrayInputStream(stringBuffer.toString().getBytes("UTF-8"));

		filePctTxt = new DefaultStreamedContent(is,
				MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType("text/txt"),
				"membros_dados_divergentes.txt");
	}

	public void carregarPCTRepresentanteFamilia() {
		try {
			if (!Util.isNullOuVazio(pctImovelRural)) {
				pctImovelRural.setListarPctRepresentanteFamiliaCollection(
						pctFamiliaService.listarPctRepresentanteFamilia(pctImovelRural));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean getRenderedPerguntaLicenca() {
		return imovelRuralServiceFacade.getRenderedPerguntaLicenca(areaProdutivaSelecionada, false, imovelRural);
	}

	public boolean getRenderedDatatableAtividade() {
		return areaProdutivaSelecionada.possuiTipologiaAtividadeCadastrada() && !pnlAgricultura && !pnlAnimal
				&& !pnlPiscicultura;
	}

	public boolean isSilvicultura() {
		final int SILVICULTURA = 22;
		return !Util.isNull(this.areaProdutivaSelecionada.getIdeTipologia())
				&& this.areaProdutivaSelecionada.getIdeTipologia().getIdeTipologia().equals(SILVICULTURA);
	}

	public void setListaAreaProdutiva(Collection<AreaProdutiva> listaAreaProdutiva) {
		this.listaAreaProdutiva = listaAreaProdutiva;
	}

	public AreaProdutiva getAreaProdutivaSelecionada() {
		return areaProdutivaSelecionada;
	}

	public void setAreaProdutivaSelecionada(AreaProdutiva areaProdutivaSelecionada) {
		this.areaProdutivaSelecionada = areaProdutivaSelecionada;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public Tipologia getSubTipologia() {
		return subTipologia;
	}

	public void setSubTipologia(Tipologia subTipologia) {
		this.subTipologia = subTipologia;
	}

	public TipologiaAtividade getTipologiaAtividade() {
		return tipologiaAtividade;
	}

	public void setTipologiaAtividade(TipologiaAtividade tipologiaAtividade) {
		if (!Util.isNullOuVazio(tipologiaAtividade))
			this.tipologiaAtividade = retornaTipologiaAtividadeSelecionada(
					tipologiaAtividade.getIdeTipologiaAtividade());
		else
			this.tipologiaAtividade = null;
	}

	public String getValAreaProdutiva() {
		return valAreaProdutiva;
	}

	public void setValAreaProdutiva(String valAreaProdutiva) {
		this.valAreaProdutiva = valAreaProdutiva;
	}

	public List<TipologiaAtividade> getListaTableTipologiaAtividade() {
		return listaTableTipologiaAtividade;
	}

	public void setListaTableTipologiaAtividade(List<TipologiaAtividade> listaTableTipologiaAtividade) {
		this.listaTableTipologiaAtividade = listaTableTipologiaAtividade;
	}

	public TipologiaAtividade getTipologiaAtividadeExcluir() {
		return tipologiaAtividadeExcluir;
	}

	public void setTipologiaAtividadeExcluir(TipologiaAtividade tipologiaAtividadeExcluir) {
		this.tipologiaAtividadeExcluir = tipologiaAtividadeExcluir;
	}

	public LocalizacaoGeografica getLocGeoShapeAreaProdutiva() {
		return locGeoShapeAreaProdutiva;
	}

	public void setLocGeoShapeAreaProdutiva(LocalizacaoGeografica locGeoShapeAreaProdutiva) {
		this.locGeoShapeAreaProdutiva = locGeoShapeAreaProdutiva;
	}

	public boolean isTemLocGeoShapeAreaProdutiva() {
		return temLocGeoShapeAreaProdutiva;
	}

	public void setTemLocGeoShapeAreaProdutiva(boolean temLocGeoShapeAreaProdutiva) {
		this.temLocGeoShapeAreaProdutiva = temLocGeoShapeAreaProdutiva;
	}

	public boolean isTemLocGeoDesenhoAreaProdutiva() {
		return temLocGeoDesenhoAreaProdutiva;
	}

	public void setTemLocGeoDesenhoAreaProdutiva(boolean temLocGeoDesenhoAreaProdutiva) {
		this.temLocGeoDesenhoAreaProdutiva = temLocGeoDesenhoAreaProdutiva;
	}

	public boolean isTemUplShapefileAreaProdutiva() {
		return temUplShapefileAreaProdutiva;
	}

	public void setTemUplShapefileAreaProdutiva(boolean temUplShapefileAreaProdutiva) {
		this.temUplShapefileAreaProdutiva = temUplShapefileAreaProdutiva;
	}

	public StreamedContent getBaixarShapeAreaProdutiva() {
		return baixarShapeAreaProdutiva;
	}

	public void setBaixarShapeAreaProdutiva(StreamedContent baixarShapeAreaProdutiva) {
		this.baixarShapeAreaProdutiva = baixarShapeAreaProdutiva;
	}

	public String getExcluirShapeAreaProdutiva() {
		return excluirShapeAreaProdutiva;
	}

	public void setExcluirShapeAreaProdutiva(String excluirShapeAreaProdutiva) {
		this.excluirShapeAreaProdutiva = excluirShapeAreaProdutiva;
	}

	public SistemaCoordenada getSistemaCoordenadaShapeAreaProdutiva() {
		return sistemaCoordenadaShapeAreaProdutiva;
	}

	public void setSistemaCoordenadaShapeAreaProdutiva(SistemaCoordenada sistemaCoordenadaShapeAreaProdutiva) {
		this.sistemaCoordenadaShapeAreaProdutiva = sistemaCoordenadaShapeAreaProdutiva;
	}

	public ClassificacaoSecaoGeometrica getSecaoGeometricaShapeAreaProdutiva() {
		return secaoGeometricaShapeAreaProdutiva;
	}

	public void setSecaoGeometricaShapeAreaProdutiva(ClassificacaoSecaoGeometrica secaoGeometricaShapeAreaProdutiva) {
		this.secaoGeometricaShapeAreaProdutiva = secaoGeometricaShapeAreaProdutiva;
	}

	public String getDescLocGeoShapeAreaProdutiva() {
		return descLocGeoShapeAreaProdutiva;
	}

	public void setDescLocGeoShapeAreaProdutiva(String descLocGeoShapeAreaProdutiva) {
		this.descLocGeoShapeAreaProdutiva = descLocGeoShapeAreaProdutiva;
	}

	public Integer getAreaProdutivaIdeLocGeoSelecionada() {
		return areaProdutivaIdeLocGeoSelecionada;
	}

	public void setAreaProdutivaIdeLocGeoSelecionada(Integer areaProdutivaIdeLocGeoSelecionada) {
		this.areaProdutivaIdeLocGeoSelecionada = areaProdutivaIdeLocGeoSelecionada;
	}

	public MetodoIrrigacao getMetodoIrrigacao() {
		return metodoIrrigacao;
	}

	public void setMetodoIrrigacao(MetodoIrrigacao metodoIrrigacao) {
		this.metodoIrrigacao = metodoIrrigacao;
	}

	public List<MetodoIrrigacao> getListaMetodoIrrigacao() {
		return listaMetodoIrrigacao;
	}

	public void setListaMetodoIrrigacao(List<MetodoIrrigacao> listaMetodoIrrigacao) {
		this.listaMetodoIrrigacao = listaMetodoIrrigacao;
	}

	public boolean isIndManejoCria() {
		return indManejoCria;
	}

	public void setIndManejoCria(boolean indManejoCria) {
		this.indManejoCria = indManejoCria;
	}

	public boolean isIndManejoEngorda() {
		return indManejoEngorda;
	}

	public void setIndManejoEngorda(boolean indManejoEngorda) {
		this.indManejoEngorda = indManejoEngorda;
	}

	public boolean isIndManejoRecria() {
		return indManejoRecria;
	}

	public void setIndManejoRecria(boolean indManejoRecria) {
		this.indManejoRecria = indManejoRecria;
	}

	public boolean isIndManejoReproducao() {
		return indManejoReproducao;
	}

	public void setIndManejoReproducao(boolean indManejoReproducao) {
		this.indManejoReproducao = indManejoReproducao;
	}

	public boolean isPnlAgricultura() {
		return pnlAgricultura;
	}

	public void setPnlAgricultura(boolean pnlAgricultura) {
		this.pnlAgricultura = pnlAgricultura;
	}

	public boolean isPnlAnimal() {
		return pnlAnimal;
	}

	public void setPnlAnimal(boolean pnlAnimal) {
		this.pnlAnimal = pnlAnimal;
	}

	public List<TipologiaAtividadeAgriculturaDTO> getListaTableTipologiaAtividadeAgricultura() {
		return listaTableTipologiaAtividadeAgricultura;
	}

	public void setListaTableTipologiaAtividadeAgricultura(
			List<TipologiaAtividadeAgriculturaDTO> listaTableTipologiaAtividadeAgricultura) {
		this.listaTableTipologiaAtividadeAgricultura = listaTableTipologiaAtividadeAgricultura;
	}

	public List<TipologiaAtividadeAnimalDTO> getListaTableTipologiaAtividadeAnimal() {
		return listaTableTipologiaAtividadeAnimal;
	}

	public void setListaTableTipologiaAtividadeAnimal(
			List<TipologiaAtividadeAnimalDTO> listaTableTipologiaAtividadeAnimal) {
		this.listaTableTipologiaAtividadeAnimal = listaTableTipologiaAtividadeAnimal;
	}

	public String getMesesIrrigacao() {
		return dscMesesIrrigacao;
	}

	public void setMesesIrrigacao(String dscMesesIrrigacao) {
		this.dscMesesIrrigacao = dscMesesIrrigacao;
	}

	public Integer getQtdCabeca() {
		return qtdCabeca;
	}

	public void setQtdCabeca(Integer qtdCabeca) {
		this.qtdCabeca = qtdCabeca;
	}

	public String getDscMesesIrrigacao() {
		return dscMesesIrrigacao;
	}

	public void setDscMesesIrrigacao(String dscMesesIrrigacao) {
		this.dscMesesIrrigacao = dscMesesIrrigacao;
	}

	public String getNumMesesIrrigacao() {
		return numMesesIrrigacao;
	}

	public void setNumMesesIrrigacao(String numMesesIrrigacao) {
		this.numMesesIrrigacao = numMesesIrrigacao;
	}

	public boolean isTemMesJan() {
		return temMesJan;
	}

	public void setTemMesJan(boolean temMesJan) {
		this.temMesJan = temMesJan;
	}

	public boolean isTemMesFev() {
		return temMesFev;
	}

	public void setTemMesFev(boolean temMesFev) {
		this.temMesFev = temMesFev;
	}

	public boolean isTemMesMar() {
		return temMesMar;
	}

	public void setTemMesMar(boolean temMesMar) {
		this.temMesMar = temMesMar;
	}

	public boolean isTemMesAbr() {
		return temMesAbr;
	}

	public void setTemMesAbr(boolean temMesAbr) {
		this.temMesAbr = temMesAbr;
	}

	public boolean isTemMesMai() {
		return temMesMai;
	}

	public void setTemMesMai(boolean temMesMai) {
		this.temMesMai = temMesMai;
	}

	public boolean isTemMesJun() {
		return temMesJun;
	}

	public void setTemMesJun(boolean temMesJun) {
		this.temMesJun = temMesJun;
	}

	public boolean isTemMesJul() {
		return temMesJul;
	}

	public void setTemMesJul(boolean temMesJul) {
		this.temMesJul = temMesJul;
	}

	public boolean isTemMesAgo() {
		return temMesAgo;
	}

	public void setTemMesAgo(boolean temMesAgo) {
		this.temMesAgo = temMesAgo;
	}

	public boolean isTemMesSet() {
		return temMesSet;
	}

	public void setTemMesSet(boolean temMesSet) {
		this.temMesSet = temMesSet;
	}

	public boolean isTemMesOut() {
		return temMesOut;
	}

	public void setTemMesOut(boolean temMesOut) {
		this.temMesOut = temMesOut;
	}

	public boolean isTemMesNov() {
		return temMesNov;
	}

	public void setTemMesNov(boolean temMesNov) {
		this.temMesNov = temMesNov;
	}

	public boolean isTemMesDez() {
		return temMesDez;
	}

	public boolean isTemUsoDessedentacao() {
		return temUsoDessedentacao;
	}

	public void setTemUsoDessedentacao(boolean temUsoDessedentacao) {
		this.temUsoDessedentacao = temUsoDessedentacao;
	}

	public void setTemUsoLimpeza(boolean temUsoLimpeza) {
		this.temUsoLimpeza = temUsoLimpeza;
	}

	public boolean getTemUsoLimpeza() {
		return this.temUsoLimpeza;
	}

	public String getDscTipoUsoAgua() {
		return dscTipoUsoAgua;
	}

	public void setTipoUsoAgua(Integer tipoUsoAgua) {
		this.tipoUsoAgua = tipoUsoAgua;
	}

	public Boolean getTemLicAreaProdutiva() {
		return temLicAreaProdutiva;
	}

	public void setTemLicAreaProdutiva(Boolean temLicAreaProdutiva) {
		this.temLicAreaProdutiva = temLicAreaProdutiva;
	}

	public String getNumProcessoAp() {
		return numProcessoAp;
	}

	public void setNumProcessoAp(String numProcessoAp) {
		this.numProcessoAp = numProcessoAp;
	}

	public boolean isPnlPiscicultura() {
		return pnlPiscicultura;
	}

	public void setPnlPiscicultura(boolean pnlPiscicultura) {
		this.pnlPiscicultura = pnlPiscicultura;
	}

	public String getValVolume() {
		return valVolume;
	}

	public void setValVolume(String valVolume) {
		this.valVolume = valVolume;
	}

	public List<TipologiaAtividadePisciculturaDTO> getListaTableTipologiaAtividadePiscicultura() {
		return listaTableTipologiaAtividadePiscicultura;
	}

	public void setListaTableTipologiaAtividadePiscicultura(
			List<TipologiaAtividadePisciculturaDTO> listaTableTipologiaAtividadePiscicultura) {
		this.listaTableTipologiaAtividadePiscicultura = listaTableTipologiaAtividadePiscicultura;
	}

	public boolean getIsEdicaoAp() {
		return isEdicaoAp;
	}

	public void setIsEdicaoAp(boolean isEdicaoAp) {
		this.isEdicaoAp = isEdicaoAp;
	}

	public boolean getVisualizacaoAp() {
		return visualizacaoAp;
	}

	public void setVisualizacaoAp(boolean isVisualizacaoAp) {
		this.visualizacaoAp = isVisualizacaoAp;
	}

	public List<Tipologia> getListaSubTipologia() {
		return listaSubTipologia;
	}

	public void setTemMesDez(boolean temMesDez) {
		this.temMesDez = temMesDez;
	}

	public boolean isOpcaoDesenhoAp() {
		return opcaoDesenhoAp;
	}

	public void setOpcaoDesenhoAp(boolean opcaoDesenhoAp) {
		this.opcaoDesenhoAp = opcaoDesenhoAp;
	}

	public List<DocumentoImovelRural> getListaDocumentoPlanoManejo() {
		return listaDocumentoPlanoManejo;
	}

	public void setListaDocumentoPlanoManejo(List<DocumentoImovelRural> listaDocumentoPlanoManejo) {
		this.listaDocumentoPlanoManejo = listaDocumentoPlanoManejo;
	}

	public String getLabelAtividadesDesenvolvidas(AreaProdutiva areaProdutiva) {

		String label = "";
		try {

			if (!Util.isLazyInitExcepOuNull(areaProdutiva.getAreaProdutivaTipologiaAtividadeCollection())
					&& !Util.isNullOuVazio(areaProdutiva.getAreaProdutivaTipologiaAtividadeCollection())
					&& !isExcecaoLabelAtividade(areaProdutiva)) {
				label += areaProdutiva.getIdeTipologiaSubgrupo().getDesTipologia() + ": ";
				for (AreaProdutivaTipologiaAtividade areaProdutivaTipologiaAtividade : areaProdutiva
						.getAreaProdutivaTipologiaAtividadeCollection()) {
					label += areaProdutivaTipologiaAtividade.getIdeTipologiaAtividade().getDscTipologiaAtividade()
							+ " - ";
				}
				label = label.substring(0, label.length() - 3);
			} else if (areaProdutiva.possuiTipologiaSubGrupoCadastrada()) {
				label += areaProdutiva.getIdeTipologia().getDesTipologia() + ": "
						+ areaProdutiva.getIdeTipologiaSubgrupo().getDesTipologia();
			} else if (areaProdutiva.possuiTipologiaCadastrada()) {
				label += areaProdutiva.getIdeTipologia().getIdeTipologiaPai().getDesTipologia() + ": "
						+ areaProdutiva.getIdeTipologia().getDesTipologia();
			} else {
				label += areaProdutiva.getIdeTipologia().getIdeTipologiaPai().getDesTipologia();
			}
		} catch (Exception e) {

			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return label;
	}

	private boolean isExcecaoLabelAtividade(AreaProdutiva areaProdutiva) {
		if (areaProdutiva.possuiTipologiaSubGrupoCadastrada()) {
			if (areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia()
					.equals(TipologiaCefirEnum.AGRICULTURA_DE_SEQUEIRO.getId())
					|| areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia()
							.equals(TipologiaCefirEnum.AGRICULTURA_IRRIGADA.getId())
					|| areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia()
							.equals(TipologiaCefirEnum.PECUARIA.getId())) {
				return true;
			}
		}
		return false;
	}

	public boolean getPossuiRecidoSicar() {
		if (!Util.isNull(this.imovelRural) && (!Util.isNull(this.imovelRural.getImovelRuralSicar()))) {
			return (this.imovelRural.getImovelRuralSicar().getUrlReciboInscricao() != null);
		}
		return false;
	}

	/**
	 * Método responsável por renderizar na tela de cadastro de imóveis rurais o
	 * ckeckbox Projeto CAR/BNDES/INEMA
	 * 
	 * @return boolean
	 * @throws Exception
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 21/10/2015
	 */
	public boolean getRenderedCheckProjetoBndes() throws Exception {

		if (imovelRural != null && imovelRural.getIdeImovelRural() != null && imovelRural.getStatusCadastro() != null) {

			if (!imovelRural.isCadastrado() && imovelRural.getIdeRequerenteCadastro().isPF()
					&& !ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica()
							.equals(imovelRural.getIdeRequerenteCadastro().getPessoaFisica().getIdePessoaFisica())) {

				if (imovelRuralServiceFacade
						.isUsuarioVinculadoProjetoBndes(ContextoUtil.getContexto().getUsuarioLogado())) {
					return true;
				}
			}
		} else {
			Pessoa requerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa();
			Pessoa solicitante = Util.isNullOuVazio(ContextoUtil.getContexto().getSolicitanteRequerimento())
					? (Pessoa) getAtributoSession("SOLICITANTE_REQ_IMOVEL_RURAL")
					: ContextoUtil.getContexto().getSolicitanteRequerimento();

			if (solicitante.getIdePessoa() != requerente.getIdePessoa() && requerente.isPF()
					&& ContextoUtil.getContexto().getIsProcPfPjOuRepLegal() && imovelRuralServiceFacade
							.isUsuarioVinculadoProjetoBndes(ContextoUtil.getContexto().getUsuarioLogado())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Método responsável por controlar o evento change do ckeckbox Projeto
	 * CAR/BNDES/INEMA
	 * 
	 * @param event
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 21/10/2015
	 */
	public void changeProjetoCarBndesInema() {

		if (this.indProjetoBndes) {
			this.imovelRural.setIdeTipoCadastroImovelRural(
					new TipoCadastroImovelRural(TipoCadastroImovelRuralEnum.IMOVEL_RURAL_PROJETO_BNDES.getTipo()));
		} else if (!this.indProjetoBndes && !Util.isNull(this.imovelRural.getIdeTipoCadastroImovelRural())
				&& !Util.isNull(this.imovelRural.getIdeTipoCadastroImovelRural().getIdeTipoCadastroImovelRural())
				&& this.imovelRural.getIdeTipoCadastroImovelRural().getIdeTipoCadastroImovelRural()
						.equals(TipoCadastroImovelRuralEnum.IMOVEL_RURAL_PROJETO_BNDES.getTipo())) {
			this.imovelRural.setIdeTipoCadastroImovelRural(
					new TipoCadastroImovelRural(TipoCadastroImovelRuralEnum.COMUM.getTipo()));
		}
	}

	public boolean isIndProjetoBndes() {
		if (!Util.isNull(this.imovelRural) && !Util.isNull(this.imovelRural.getIdeTipoCadastroImovelRural())
				&& !Util.isNull(this.imovelRural.getIdeTipoCadastroImovelRural().getIdeTipoCadastroImovelRural())
				&& this.imovelRural.getIdeTipoCadastroImovelRural().getIdeTipoCadastroImovelRural()
						.equals(TipoCadastroImovelRuralEnum.IMOVEL_RURAL_PROJETO_BNDES.getTipo())) {
			this.indProjetoBndes = true;
		}
		return indProjetoBndes;
	}

	public void setIndProjetoBndes(boolean indProjetoBndes) {
		this.indProjetoBndes = indProjetoBndes;
	}

	public String getlabelLatitudeLongitudeFiltroSelecionado() {
		if (!Util.isNull(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()))
			if (this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()
					.getIdeSistemaCoordenada() == 1
					|| this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()
							.getIdeSistemaCoordenada() == 4) {
				return "Grau/Minuto/Segundo";
			}
		return Util.getString("incluirvertice_lbl_latitude_longitude_grau_minuto_segundo");
	}

	public String getlabelGrauFiltroSelecionado() {
		if (!Util.isNull(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
			if (this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()
					.getIdeSistemaCoordenada() == 1
					|| this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()
							.getIdeSistemaCoordenada() == 4) {
				return "Grau Decimal";
			}
		}
		return Util.getString("incluirvertice_lbl_grau_fracao_grau");
	}

	public boolean gettipoCoordenadaReferencia() {
		if (!Util.isNull(this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
			if (this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()
					.getIdeSistemaCoordenada() == 1
					|| this.imovelRuralUsoAgua.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()
							.getIdeSistemaCoordenada() == 4) {
				return true;
			}
		}
		return false;
	}

	public boolean getdesabilitaPerguntaOutorgaIntervencao() {
		if (!Util.isNull(this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao())
				&& !Util.isNull(this.imovelRuralUsoAguaIntervencao.getIdeTipoIntervencao().getIdeTipoIntervencao())) {
			if (this.imovelRuralUsoAguaIntervencao.isTravessiaDuto()
					|| this.imovelRuralUsoAguaIntervencao.isConstrucaoPonte()
					|| this.imovelRuralUsoAguaIntervencao.isConstrucaoCais()
					|| this.imovelRuralUsoAguaIntervencao.isDrenagemPluvialLancamento()
					|| this.imovelRuralUsoAguaIntervencao.isDesassoreamentoLimpeza()
					|| this.imovelRuralUsoAguaIntervencao.isConstrucaoTravessia()
					|| this.imovelRuralUsoAguaIntervencao.isConstrucaoPier()) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public void limparFiltroModalTipoIntervencao() {
		this.imovelRuralUsoAgua.setIndDispensa(null);
		this.imovelRuralUsoAgua.setIndProcesso(null);

		imovelRuralUsoAgua.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		this.exibirLatitudeLongitude = false;

		this.grausLatitudeLoc = "";
		this.minutosLatitudeLoc = "";
		this.segundosLatitudeLoc = "";
		this.grausLongitudeLoc = "";
		this.minutosLongitudeLoc = "";
		this.segundosLongitudeLoc = "";
		this.fracaoGrauLatitudeLoc = "";
		this.fracaoGrauLongitudeLoc = "";

		this.grausLatitudeLocPonto2 = "";
		this.minutosLatitudeLocPonto2 = "";
		this.segundosLatitudeLocPonto2 = "";
		this.grausLongitudeLocPonto2 = "";
		this.minutosLongitudeLocPonto2 = "";
		this.segundosLongitudeLocPonto2 = "";
		this.fracaoGrauLatitudeLocPonto2 = "";
		this.fracaoGrauLongitudeLocPonto2 = "";

		this.listFinalidadesSelecionadas = null;

		this.processoUsoAguaCollection = null;

	}

	public String getLabelModalUsoAgua() {
		return this.imovelRuralUsoAgua.getLabelTipoUsoAgua();
	}

	/**
	 * Método responsável por desabilitar a pergunta "O imóvel possui outras áreas
	 * com vegetação nativa, além de Reserva Legal e área de preservação
	 * permanente?" na aba questionário
	 * 
	 * @return boolean
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 04/05/2016
	 */
	public boolean getDisablePerguntaVegetacaoNativa() {
		if (!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(imovelRural.getVegetacaoNativa())
				&& !Util.isNullOuVazio(imovelRural.getVegetacaoNativa().getIdeVegetacaoNativa())
				&& imovelRural.isCedeAreaParaCompensacaoRl()) {
			return true;
		}
		return false;
	}

	public boolean isDisableSalvarResponsavelTecnico() {
		return disableSalvarResponsavelTecnico;
	}

	public void setDisableSalvarResponsavelTecnico(boolean disableSalvarResponsavelTecnico) {
		this.disableSalvarResponsavelTecnico = disableSalvarResponsavelTecnico;
	}

	public boolean isUTM(LocalizacaoGeografica lg) {
		return !lg.getIdeSistemaCoordenada().getIdeSistemaCoordenada()
				.equals(SistemaCoordenadaEnum.GEOGRAFICA_SAD69.getId())
				&& !lg.getIdeSistemaCoordenada().getIdeSistemaCoordenada()
						.equals(SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getId());
	}

	/**
	 * <p>
	 * O sistema não deverá realizar nenhum tipo de validação na edição do imóvel
	 * rural para o perfil de Maria Daniela;
	 * </p>
	 * 
	 * @see <a href="http://10.105.17.77/redmine/issues/8035">8035</a>
	 */
	public boolean isUsuarioSemRestricao() {
		return ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica().equals(USUARIO_SEM_RESTRICAO)
				|| ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica().equals(USUARIO_SEM_RESTRICAO1)
				|| ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica().equals(USUARIO_SEM_RESTRICAO2)
				|| ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica().equals(USUARIO_SEM_RESTRICAO3);
	}

	/**
	 * <p>
	 * (...) o sistema deve permitir que o perfil de Maria Daniela edite quaisquer
	 * informações, inclusive para imóveis que possuem reserva legal aprovada;
	 * </p>
	 * 
	 * @see <a href="http://10.105.17.77/redmine/issues/8035">8035</a>
	 */
	public boolean isEditavelParaUsuarioSemRestricao() {
		return isUsuarioSemRestricao();
	}

	public String getDataHoje() {
		return Util.getDataHoje();
	}

	/**
	 * Método que limpa as informações na tela cadastro de {@link AssentadoIncra}.
	 * 
	 * @author ivanildo.souza
	 * @since 28/12/2016
	 */
	public void limparModalAssentadoIncra() {
		if (!Util.isNullOuVazio(assentadoIncraImovelRuralSelecionado)) {
			assentadoIncraImovelRuralSelecionado = null;
		}
	}

	public boolean getHabilitaCamposAssentadoIncra() {
		if (!Util.isNull(assentadoIncraImovelRuralSelecionado)
				&& !Util.isNull(assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra())
				&& !Util.isNull(assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica())
				&& !Util.isNullOuVazio(
						assentadoIncraImovelRuralSelecionado.getIdeAssentadoIncra().getIdePessoaFisica().getNumCpf())) {
			return true;
		}
		return false;
	}

	public boolean renderedLabelSRID(LocalizacaoGeografica localizacaoGeografica) {
		if (!Util.isNull(localizacaoGeografica) && !Util.isNull(localizacaoGeografica.getIdeSistemaCoordenada())
				&& !Util.isNull(localizacaoGeografica.getIdeSistemaCoordenada().getIdeSistemaCoordenada())) {
			return true;
		}
		return false;
	}

	public boolean isImovelCDAOuBNDES() {
		return imovelRural.isImovelCDA() || imovelRural.isImovelBNDES();
	}

	protected boolean temShapeTemporarioLotes() {
		File diretorioShape = new File(DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural()
				+ "_" + TemaGeoseiaEnum.LOTE_ASSENTAMENTO.getId());

		File[] arquivos = diretorioShape.listFiles();

		if (!Util.isNullOuVazio(arquivos) && arquivos.length == 3) {
			return true;
		}
		return false;
	}

	public Boolean getDesabilitaSalvarShape(LocalizacaoGeografica localizacaoGeografica) {
		if (!Util.isNullOuVazio(localizacaoGeografica)
				&& !Util.isNullOuVazio(localizacaoGeografica.getIdeClassificacaoSecao())
				&& !Util.isNullOuVazio(localizacaoGeografica.getIdeClassificacaoSecao().getIdeClassificacaoSecao())
				&& localizacaoGeografica.getIdeClassificacaoSecao().getIdeClassificacaoSecao()
						.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())) {
			if (Util.isNull(localizacaoGeografica.getListArquivosShape())
					|| localizacaoGeografica.getListArquivosShape().size() != 4) {
				return true;
			}
		}
		return false;
	}

	public Boolean habilitaSalvarShape(LocalizacaoGeografica localizacaoGeografica) {
		return (!Util.isNull(localizacaoGeografica) && !Util.isNullOuVazio(localizacaoGeografica.getListArquivosShape())
				&& localizacaoGeografica.getListArquivosShape().size() == 4);
	}

	public void limparCampoNumeroLicencaAreaProdutiva(ValueChangeEvent event) {
		if (!(Boolean) event.getNewValue()) {
			areaProdutivaSelecionada.setNumProcesso(new String());
		}
	}

	public void limparCampoDescricaoTerritorioOutros() {
		if (!Util.isNullOuVazio(pctImovelRural.getIdeTipoTerritorioPct())) {
			if (!Util.isNullOuVazio(this.pctImovelRural.getDscTipoTerritorioPctOutros())
					&& !"Outros".equals(this.pctImovelRural.getIdeTipoTerritorioPct().getDscTipoTerritorioPct())) {
				this.pctImovelRural.setDscTipoTerritorioPctOutros(null);
			}
		}
	}

	public void removerLocalizacacaoTerritorioPct() {
		try {
			ideLocalizacaoGeograficaPctOld = imovelRural.getIdeLocalizacaoGeograficaPct().clone();
			imovelRural.setIdeLocalizacaoGeograficaPct(new LocalizacaoGeografica());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public boolean isRenderedConcedenteConcessionario() {
		if (Util.isNullOuVazio(pctImovelRural.getIdeTipoTerritorioPct())) {
			return false;
		}
		return new TipoTerritorioPct(TipoTerritorioPctEnum.CONCESSAO).equals(pctImovelRural.getIdeTipoTerritorioPct());
	}

	public void excluirLocalizacacaoPct(LocalizacaoGeografica localizacaoGeografica) {
		try {
			imovelRuralServiceFacade.excluirDadoGeografico(localizacaoGeografica);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void montarPctImovelRural() {
		this.pctImovelRural.setIdeImovelRural(imovelRural);

		if (!Util.isNullOuVazio(ContextoUtil.getContexto().getTipoVinculoPCT())) {

			this.pctImovelRural.setIndAceite(ContextoUtil.getContexto().getTipoVinculoPCT().isIndAceite());
		} else {
			this.pctImovelRural.setIndAceite(true);
		}

		this.pctImovelRural.setContratoConvenio(ContextoUtil.getContexto().getContratoConvenio());
	}
	
	public void bloquearLimiteImovel() {
		try {
			if(!Util.isNullOuVazio(imovelRural)) {
				ImovelRural objAntigo = new ImovelRural();
				imovelRural = imovelRuralServiceFacade.carregarTudo(imovelRural.getIdeImovelRural());
				if(!Util.isNullOuVazio(imovelRural.getDtcPrimeiraFinalizacao())) {
					objAntigo = imovelRural.clone();
				}
				if (!Util.isNullOuVazio(imovelRural.getIndRestaurado())) {
					if(imovelRural.getIndRestaurado()) {
						imovelRural.setIndBloqueioLimite(true);
					}
				}	
				if (!Util.isNullOuVazio(imovelRural.getIdePctImovelRural())) {
						imovelRural.setIndBloqueioLimite(true);
				}	
				if (!Util.isNullOuVazio(imovelRural.getIdeFaseAssentamentoImovelRural())) {
					//imovelRural.setIndBloqueioLimite(true);
					imovelRural.setIndBloqueioLimite(false); //36257 - Desbloqueio de todos os imóveis do tipo "Assentamento"
				}	
				if(!Util.isNullOuVazio(objAntigo)) {
					if(!Util.isNullOuVazio(imovelRural.getIndBloqueioLimite()) &&
							!imovelRural.getIndBloqueioLimite().equals(objAntigo.getIndBloqueioLimite())) {
						auditoria.atualizar(objAntigo, imovelRural);
					}
				}
				imovelRuralServiceFacade.atualizarImovelRural(imovelRural);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar bloquear limite de imóvel.");
			e.printStackTrace();
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void uploadDocumentoSolicitacaoDesbloqueio(FileUploadEvent event) {
		String nmArquivo = getImovelRural().getIdeImovelRural().toString()+"_"+"DESBLOQUEIO_IMOVEL_RURAL";
		String caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.IMOVELRURAL.toString()+
				FileUploadUtil.getFileSeparator()+getImovelRural().getIdeImovelRural().toString()+FileUploadUtil.getFileSeparator(), nmArquivo);
		
		setIdeDocumentoResponsavel(new DocumentoImovelRural(null, nmArquivo, caminhoArquivo));
		File file = new File(caminhoArquivo.trim());
		getIdeDocumentoResponsavel().setFileSize(file.length());
		
		
		if(Util.isNullOuVazio(listDocumentoSolicitacaoDesbloqueio)) {
			listDocumentoSolicitacaoDesbloqueio = new ArrayList<DocumentoImovelRural>();
			listDocumentoSolicitacaoDesbloqueio.add(getIdeDocumentoResponsavel());
		}else {
			listDocumentoSolicitacaoDesbloqueio.clear();
			listDocumentoSolicitacaoDesbloqueio.add(getIdeDocumentoResponsavel());
		}
		
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");		
	}
	
	public void limparCampos() {
		this.ImovelRuralDesbloqueio = new ImovelRuralDesbloqueio();
		this.listDocumentoSolicitacaoDesbloqueio = new ArrayList<DocumentoImovelRural>();
	}
	
	public void desbloquearLimiteImovel() throws Exception {
		try {
			
			if(Util.isNullOuVazio(this.getListDocumentoSolicitacaoDesbloqueio())) {
				throw new Exception("Campo Documento de Desbloqueio obrigatório!");
			}
			if(Util.isNullOuVazio(this.getImovelRuralDesbloqueioObservacao())) {
				throw new Exception("Campo Observação do Desbloqueio obrigatório!");
			}
			
			if(!Util.isNullOuVazio(imovelRural)) {
				ImovelRural objAntigo = new ImovelRural();
				
				imovelRural = imovelRuralServiceFacade.carregarTudo(imovelRural.getIdeImovelRural());
				if(!Util.isNullOuVazio(imovelRural.getDtcPrimeiraFinalizacao())) {
					objAntigo = imovelRural.clone();
				}
				if (!Util.isNullOuVazio(imovelRural.getIndRestaurado())) {
					if(imovelRural.getIndRestaurado()) {
						imovelRural.setIndBloqueioLimite(false);
					}
				}	
				if (!Util.isNullOuVazio(imovelRural.getIdePctImovelRural())) {
						imovelRural.setIndBloqueioLimite(false);
				}	
				if (!Util.isNullOuVazio(imovelRural.getIdeFaseAssentamentoImovelRural())) {
					imovelRural.setIndBloqueioLimite(false);
				}	
				if(!Util.isNullOuVazio(objAntigo)) {
					if(!Util.isNullOuVazio(imovelRural.getIndBloqueioLimite()) &&
							!imovelRural.getIndBloqueioLimite().equals(objAntigo.getIndBloqueioLimite())) {
						auditoria.atualizar(objAntigo, imovelRural);
					}
				}
				imovelRuralServiceFacade.atualizarImovelRural(imovelRural);
				
				this.ImovelRuralDesbloqueio.setDtcJustificativa(new Date());
				this.ImovelRuralDesbloqueio.setIdeUsuario(ContextoUtil.getContexto().getUsuarioLogado());
				this.ImovelRuralDesbloqueio.setObservacao(this.getImovelRuralDesbloqueioObservacao());
				this.ImovelRuralDesbloqueio.setIdeImovelRural(imovelRural);
				this.imovelRuralServiceFacade.salvarDesbloqueio(ImovelRuralDesbloqueio);
				
				auditoria.salvar(ImovelRuralDesbloqueio);
			}
			RequestContext.getCurrentInstance().execute(nmModal+".hide()");
			JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S002"));
			RequestContext.getCurrentInstance().execute("fecharModal('#statusDialog'); abrirModal('#statusDialog'); document.getElementById('filtroImoveis:btnConsulta').click();");
			
		} catch (Exception e) {
			//JsfUtil.addErrorMessage("Erro ao tentar desbloquear limite de imóvel.");
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public Endereco getIdeEnderecoSelecionado() {
		return ideEnderecoSelecionado;
	}

	public void setIdeBairroSelecionado(Endereco ideEnderecoSelecionado) {
		this.ideEnderecoSelecionado = ideEnderecoSelecionado;
	}

	public boolean isFinalizarRendered() {
		return finalizarRendered;
	}

	public void setFinalizarRendered(boolean finalizarRendered) {
		this.finalizarRendered = finalizarRendered;
	}

	public Boolean getShowPCT() {

		return ContextoUtil.getContexto().isPCT();
	}

	public PctImovelRural getPctImovelRural() {
		return pctImovelRural;
	}

	public void setPctImovelRural(PctImovelRural pctImovelRural) {
		this.pctImovelRural = pctImovelRural;
	}

	public PessoaFisica getPctPessoaFisicaSelecionado() {
		return pctPessoaFisicaSelecionado;
	}

	public void setPctPessoaFisicaSelecionado(PessoaFisica pctPessoaFisicaSelecionado) {
		this.pctPessoaFisicaSelecionado = pctPessoaFisicaSelecionado;
	}

	public int getPctTipoInclusaoProprietario() {
		return pctTipoInclusaoProprietario;
	}

	public void setPctTipoInclusaoProprietario(int pctTipoInclusaoProprietario) {
		this.pctTipoInclusaoProprietario = pctTipoInclusaoProprietario;
	}

	public PctFamilia getPctFamiliaExclusaoSelecionada() {
		return pctFamiliaExclusaoSelecionada;
	}

	public void setPctFamiliaExclusaoSelecionada(PctFamilia pctFamiliaExclusaoSelecionada) {
		this.pctFamiliaExclusaoSelecionada = pctFamiliaExclusaoSelecionada;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeograficaPctOld() {
		return ideLocalizacaoGeograficaPctOld;
	}

	public void setIdeLocalizacaoGeograficaPctOld(LocalizacaoGeografica ideLocalizacaoGeograficaPctOld) {
		this.ideLocalizacaoGeograficaPctOld = ideLocalizacaoGeograficaPctOld;
	}

	public boolean isPctHabilitarCampos() {
		return pctHabilitarCampos;
	}

	public void setPctHabilitarCampos(boolean pctHabilitarCampos) {
		this.pctHabilitarCampos = pctHabilitarCampos;
	}

	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}

	public PessoaJuridicaPct getPessoaJuridicaPct() {
		return pessoaJuridicaPct;
	}

	public void setPessoaJuridicaPct(PessoaJuridicaPct pessoaJuridicaPct) {
		this.pessoaJuridicaPct = pessoaJuridicaPct;
	}

	public boolean isPctHabilitarCamposCnpj() {
		return pctHabilitarCamposCnpj;
	}

	public void setPctHabilitarCamposCnpj(boolean pctHabilitarCamposCnpj) {
		this.pctHabilitarCamposCnpj = pctHabilitarCamposCnpj;
	}

	public boolean isPctHabilitarCampoRepresentanteLegal() {
		return pctHabilitarCampoRepresentanteLegal;
	}

	public void setPctHabilitarCampoRepresentanteLegal(boolean pctHabilitarCampoRepresentanteLegal) {
		this.pctHabilitarCampoRepresentanteLegal = pctHabilitarCampoRepresentanteLegal;
	}

	public PessoaJuridicaPct getPessoaJuridicaPctExclusaoSelecionada() {
		return pessoaJuridicaPctExclusaoSelecionada;
	}

	public void setPessoaJuridicaPctExclusaoSelecionada(PessoaJuridicaPct pessoaJuridicaPctExclusaoSelecionada) {
		this.pessoaJuridicaPctExclusaoSelecionada = pessoaJuridicaPctExclusaoSelecionada;
	}

	public DualListModel<TipoSeguimentoPct> getTipoSeguimentoPcts() {
		return tipoSeguimentoPcts;
	}

	public void setTipoSeguimentoPcts(DualListModel<TipoSeguimentoPct> tipoSeguimentoPcts) {
		this.tipoSeguimentoPcts = tipoSeguimentoPcts;
	}

	public TipoSeguimentoPct getTipoSeguimentoPctSelecionadoExclusao() {
		return tipoSeguimentoPctSelecionadoExclusao;
	}

	public void setTipoSeguimentoPctSelecionadoExclusao(TipoSeguimentoPct tipoSeguimentoPctSelecionadoExclusao) {
		this.tipoSeguimentoPctSelecionadoExclusao = tipoSeguimentoPctSelecionadoExclusao;
	}

	public boolean isIndRepresentanteFamilia() {
		return indRepresentanteFamilia;
	}

	public void setIndRepresentanteFamilia(boolean indRepresentanteFamilia) {
		this.indRepresentanteFamilia = indRepresentanteFamilia;
	}

	public Pessoa getPessoaRepresentanteFamilia() {
		return pessoaRepresentanteFamilia;
	}

	public void setPessoaRepresentanteFamilia(Pessoa pessoaRepresentanteFamilia) {
		this.pessoaRepresentanteFamilia = pessoaRepresentanteFamilia;
	}

	public PctFamilia getPessoaRepresentante() {
		return pessoaRepresentante;
	}

	public void setPessoaRepresentante(PctFamilia pessoaRepresentante) {
		this.pessoaRepresentante = pessoaRepresentante;
	}

	public Integer getTipoUpload() {
		return tipoUpload;
	}

	public void setTipoUpload(Integer tipoUpload) {
		this.tipoUpload = tipoUpload;
	}

	public ImovelRuralFacade getImovelRuralServiceFacade() {
		return imovelRuralServiceFacade;
	}

	public String getMsgDataInvalida() {
		return msgDataInvalida;
	}

	public List<ImovelRuralUsoAgua> getListCapSuperficial() {
		return listCapSuperficial;
	}

	public List<ImovelRuralUsoAgua> getListCapSubterranea() {
		return listCapSubterranea;
	}

	public List<ImovelRuralUsoAgua> getListLancamentoResiduos() {
		return listLancamentoResiduos;
	}

	public List<ImovelRuralUsoAgua> getListPontoIntervencao() {
		return listPontoIntervencao;
	}

	@SuppressWarnings("deprecation")
	public boolean isIndSupressaoVegetacaoObrigatorio() {

		if (!imovelRural.isImovelCDA() && !imovelRural.isImovelBNDES() && !imovelRural.isImovelINCRA()) {
			return true;

		} else if (imovelRural.isImovelINCRA() && !Util.isNullOuVazio(imovelRural.getDtcCriacaoAssentamento())
				&& imovelRural.getDtcCriacaoAssentamento().before(new Date("2008/07/22"))) {

			return true;
		}

		return false;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCaminhoDesenharGeoBahia() {
		return caminhoDesenharGeoBahia;
	}

	public void setCaminhoDesenharGeoBahia(String caminhoDesenharGeoBahia) {
		this.caminhoDesenharGeoBahia = caminhoDesenharGeoBahia;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public StreamedContent getFilePctTxt() {
		return filePctTxt;
	}

	public void setMsgDataInvalida(String msgDataInvalida) {
		this.msgDataInvalida = msgDataInvalida;
	}

	public boolean isLiConcordoImportacaoPlanilha() {
		return liConcordoImportacaoPlanilha;
	}

	public void setLiConcordoImportacaoPlanilha(boolean liConcordoImportacaoPlanilha) {
		this.liConcordoImportacaoPlanilha = liConcordoImportacaoPlanilha;
	}

	public UploadedFile getPlanilhaPctUploaded() {
		return planilhaPctUploaded;
	}

	public void setPlanilhaPctUploaded(UploadedFile planilhaPctUploaded) {
		this.planilhaPctUploaded = planilhaPctUploaded;
	}

	public void handleFileUpload(FileUploadEvent event) {
		setFileUploadEventPlanilhaPct(event);
		setPlanilhaPctUploaded(event.getFile());
	}

	public FileUploadEvent getFileUploadEventPlanilhaPct() {
		return fileUploadEventPlanilhaPct;
	}

	public void setFileUploadEventPlanilhaPct(FileUploadEvent fileUploadEventPlanilhaPct) {
		this.fileUploadEventPlanilhaPct = fileUploadEventPlanilhaPct;
	}

	private Boolean getIsEdicaoDocumentacao() {
		return isEdicaoDocumentacao;
	}

	private void setEdicaoDocumentacao(boolean isEdicaoDocumentacao) {
		this.isEdicaoDocumentacao = isEdicaoDocumentacao;
	}

	public Bairro getBairroSelecionadoCombo() {
		return bairroSelecionadoCombo;
	}

	public void setBairroSelecionadoCombo(Bairro bairroSelecionadoCombo) {
		this.bairroSelecionadoCombo = bairroSelecionadoCombo;
	}

	public Bairro getBairroDeclaranteCombo() {
		return bairroDeclaranteCombo;
	}

	public void setBairroDeclaranteCombo(Bairro bairroDeclaranteCombo) {
		this.bairroDeclaranteCombo = bairroDeclaranteCombo;
	}

	public Boolean getEnableChangeBairroDeclarante() {
		return enableChangeBairroDeclarante;
	}

	public void setEnableChangeBairroDeclarante(Boolean enableChangeBairroDeclarante) {
		this.enableChangeBairroDeclarante = enableChangeBairroDeclarante;
	}
	
	public String getImovelRuralDesbloqueioObservacao() {
		return ImovelRuralDesbloqueio.getObservacao();
	}
	
	public ImovelRuralDesbloqueio getImovelRuralDesbloqueio() {
		return ImovelRuralDesbloqueio;
	}
	
	public List<DocumentoImovelRural> getListDocumentoSolicitacaoDesbloqueio() {
		return listDocumentoSolicitacaoDesbloqueio;
	}
	
	private void setIdeDocumentoResponsavel(DocumentoImovelRural documentoImovelRural) {
		ImovelRuralDesbloqueio.setIdeDocumentoSolicitacao(documentoImovelRural);
	}

	private DocumentoImovelRural getIdeDocumentoResponsavel() {
		return ImovelRuralDesbloqueio.getIdeDocumentoSolicitacao();
	}
	
}
