package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import org.quartz.xml.ValidationException;

import br.gov.ba.seia.controller.AtualizarShapesController;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.ValidacaoGeoSeiaDAOIf;
import br.gov.ba.seia.dto.ImovelRuralDTO;
import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.Area;
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
import br.gov.ba.seia.entity.CronogramaEtapa;
import br.gov.ba.seia.entity.CronogramaRecuperacao;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.DocumentoImovelRuralPosse;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Escolaridade;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.FaseAssentamentoImovelRural;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.GeoJsonSicar;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralAbastecimento;
import br.gov.ba.seia.entity.ImovelRuralDesbloqueio;
import br.gov.ba.seia.entity.ImovelRuralMudancaStatusJustificativa;
import br.gov.ba.seia.entity.ImovelRuralRevalidacao;
import br.gov.ba.seia.entity.ImovelRuralRppn;
import br.gov.ba.seia.entity.ImovelRuralSicar;
import br.gov.ba.seia.entity.ImovelRuralUsoAgua;
import br.gov.ba.seia.entity.ImovelRuralUsoAguaIntervencao;
import br.gov.ba.seia.entity.ImovelRuralUsoAguaTipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Manejo;
import br.gov.ba.seia.entity.MetodoIrrigacao;
import br.gov.ba.seia.entity.ModuloFiscal;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.OutrosPassivosAmbientais;
import br.gov.ba.seia.entity.Pais;
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
import br.gov.ba.seia.entity.ReservaLegalAverbada;
import br.gov.ba.seia.entity.ReservaLegalBloqueio;
import br.gov.ba.seia.entity.ReservaLegalTramite;
import br.gov.ba.seia.entity.ResponsavelImovelRural;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.entity.SubTipoApp;
import br.gov.ba.seia.entity.SupressaoVegetacao;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoApp;
import br.gov.ba.seia.entity.TipoArl;
import br.gov.ba.seia.entity.TipoCadastroImovelRural;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.TipoDocumentoImovelRural;
import br.gov.ba.seia.entity.TipoEstadoConservacao;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoFinalidadeVegetacaoNativa;
import br.gov.ba.seia.entity.TipoIntervencao;
import br.gov.ba.seia.entity.TipoOrigemCertificado;
import br.gov.ba.seia.entity.TipoRecuperacao;
import br.gov.ba.seia.entity.TipoSeguimentoPct;
import br.gov.ba.seia.entity.TipoTerritorioPct;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.VegetacaoNativa;
import br.gov.ba.seia.entity.auditoria.FiltroAuditoria;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.service.AppService;
import br.gov.ba.seia.service.AreaProdutivaService;
import br.gov.ba.seia.service.AreaRuralConsolidadaService;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.AssentadoIncraImovelRuralService;
import br.gov.ba.seia.service.AssentadoIncraService;
import br.gov.ba.seia.service.AssociacaoAssentadoImovelRuralIncraService;
import br.gov.ba.seia.service.AssociacaoIncraImovelRuralService;
import br.gov.ba.seia.service.AssociacaoIncraService;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.ClassificacaoSecaoGeometricaService;
import br.gov.ba.seia.service.ContratoConvenioCefirServiceImpl;
import br.gov.ba.seia.service.CronogramaEtapaService;
import br.gov.ba.seia.service.CronogramaRecuperacaoService;
import br.gov.ba.seia.service.DatumService;
import br.gov.ba.seia.service.DocumentoImovelRuralPosseService;
import br.gov.ba.seia.service.DocumentoImovelRuralService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.EnderecoService;
import br.gov.ba.seia.service.EscolaridadeService;
import br.gov.ba.seia.service.FaseAssentamentoImovelRuralService;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.ImovelRuralDesbloqueioService;
import br.gov.ba.seia.service.ImovelRuralMudancaStatusJustificativaService;
import br.gov.ba.seia.service.ImovelRuralRevalidacaoService;
import br.gov.ba.seia.service.ImovelRuralRppnService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.ImovelRuralSicarService;
import br.gov.ba.seia.service.ImovelRuralUsoAguaIntervencaoService;
import br.gov.ba.seia.service.ImovelRuralUsoAguaService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.ManejoService;
import br.gov.ba.seia.service.MetodoIrrigacaoService;
import br.gov.ba.seia.service.ModuloFiscalService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.OutrosPassivosAmbientaisService;
import br.gov.ba.seia.service.PaisService;
import br.gov.ba.seia.service.ParamPersistDadoGeoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.ProcessoExternoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.ProcessoUsoAguaService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.RequerimentoImovelService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.ReservaLegalAverbadaService;
import br.gov.ba.seia.service.ReservaLegalBloqueioService;
import br.gov.ba.seia.service.ReservaLegalService;
import br.gov.ba.seia.service.ReservaLegalTramiteService;
import br.gov.ba.seia.service.ResponsavelImovelRuralService;
import br.gov.ba.seia.service.StatusReservaLegalService;
import br.gov.ba.seia.service.SubTipoAppService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoAppService;
import br.gov.ba.seia.service.TipoArlService;
import br.gov.ba.seia.service.TipoCadastroImovelRuralService;
import br.gov.ba.seia.service.TipoDocumentoImovelRuralService;
import br.gov.ba.seia.service.TipoEstadoConservacaoService;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.TipoFinalidadeVegetacaoNativaService;
import br.gov.ba.seia.service.TipoIntervencaoService;
import br.gov.ba.seia.service.TipoOrigemCertificadoService;
import br.gov.ba.seia.service.TipoRecuperacaoService;
import br.gov.ba.seia.service.TipoVinculoImovelService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.service.UsuarioService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.service.VegetacaoNativaService;
import br.gov.ba.seia.service.VerticeService;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralFacade {

	@Inject
	private IDAO<ProcessoTramiteImovelRural> processoTramiteImovelRuralDAO;

	@Inject
	private IDAO<ImovelRuralAbastecimento> imovelRuralAbastecimentoDAO;

	@Inject
	private IDAO<SupressaoVegetacao> supressaoVegetacaoDAO;

	@EJB
	private RequerimentoService requerimentoService;

	@EJB
	private ImovelRuralService imovelRuralService;

	@EJB
	private ReservaLegalService reservaLegalService;

	@EJB
	private StatusReservaLegalService statusReservaLegalService;

	@EJB
	private TelefoneService telefoneService;

	@EJB
	private RepresentanteLegalService representanteLegalService;

	@EJB
	private ResponsavelImovelRuralService responsavelImovelRuralService;

	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;

	@EJB
	private DatumService serviceDatum;

	@EJB
	private ClassificacaoSecaoGeometricaService serviceClassifSecGeometrica;

	@EJB
	private CronogramaEtapaService cronogramaEtapaService;

	@EJB
	private CronogramaRecuperacaoService cronogramaRecuperacaoService;

	@EJB
	private AppService appService;

	@EJB
	private TipologiaService tipologiaService;

	@EJB
	private AreaProdutivaService areaProdutivaService;

	@EJB
	private VegetacaoNativaService vegetacaoNativaService;

	@EJB
	private TipoAppService tipoAppService;

	@EJB
	private SubTipoAppService subTipoAppService;

	@EJB
	private TipoRecuperacaoService tipoRecuperacaoService;

	@EJB
	private MetodoIrrigacaoService metodoIrrigacaoService;

	@EJB
	private ManejoService manejoService;

	@EJB
	private TipoVinculoImovelService tipoVinculoImovelService;

	@EJB
	private TipoCadastroImovelRuralService tipoCadastroImovelRuralService;

	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;

	@EJB
	private TipoArlService tipoArlService;

	@EJB
	private TipoOrigemCertificadoService tipoOrigemCertificadoService;

	@EJB
	private ImovelRuralUsoAguaService imovelRuralUsoAguaService;

	@EJB
	private VerticeService verticeService;

	@EJB
	private GerenciaArquivoService gerenciaArquivoService;

	@EJB
	private EmpreendimentoService empreendimentoService;

	@EJB
	private ReservaLegalTramiteService reservaTramiteService;

	@EJB
	private CertificadoService certificadoService;

	@EJB
	private RequerimentoImovelService requerimentoImovelService;

	@EJB
	private AuditoriaFacade auditoriaFacade;

	@EJB
	private ReservaLegalAverbadaService reservaLegalAverbadaService;

	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;

	@EJB
	private UsuarioService usuarioService;

	@EJB
	private PessoaFisicaService pessoaFisicaService;

	@EJB
	private PaisService paisService;

	@EJB
	private EscolaridadeService escolaridadeService;

	@EJB
	private DocumentoImovelRuralService documentoImovelRuralService;

	@EJB
	private TipoEstadoConservacaoService tipoEstadoConservacaoService;

	@EJB
	private ParamPersistDadoGeoService paramPersistDadoGeoService;

	@EJB
	private PessoaJuridicaService pessoaJuridicaService;

	@EJB
	private EnderecoService enderecoService;

	@EJB
	private ModuloFiscalService moduloFiscalService;

	@EJB
	private EnderecoPessoaService enderecoPessoaService;

	@EJB
	private ProcessoUsoAguaService processoUsoAguaService;

	@EJB
	private ProcessoService processoService;

	@EJB
	private ProcessoExternoService processoExternoService;

	@EJB
	private TipoIntervencaoService tipoIntervencaoService;

	@EJB
	private MunicipioService municipioService;

	@EJB
	private BairroService bairroService;

	@EJB
	private LogradouroService logradouroService;

	@EJB
	private FaseAssentamentoImovelRuralService faseAssentamentoImovelRuralService;

	@EJB
	private AssociacaoIncraService associacaoIncraService;

	@EJB
	private AssentadoIncraService assentadoIncraService;

	@EJB
	private AssociacaoAssentadoImovelRuralIncraService associacaoAssentadoImovelRuralIncraService;

	@EJB
	private AssociacaoIncraImovelRuralService associacaoIncraImovelRuralService;

	@EJB
	private AssentadoIncraImovelRuralService assentadoIncraImovelRuralService;

	@EJB
	private ImovelRuralMudancaStatusJustificativaService mudancaStatusJustificativaService;
	
	@EJB
	private ImovelRuralDesbloqueioService desbloqueioService;

	@EJB
	private ImovelRuralSicarService imovelRuralSicarService;

	@EJB
	private DocumentoImovelRuralPosseService documentoImovelRuralPosseService;

	@EJB
	private TipoDocumentoImovelRuralService tipoDocumentoImovelRuralService;

	@EJB
	private ImovelRuralRppnService imovelRuralRppnService;

	@EJB
	private TipoFinalidadeVegetacaoNativaService tipoFinalidadeVegetacaoNativaService;

	@EJB
	private AreaRuralConsolidadaService areaRuralConsolidadaService;

	@EJB
	private OutrosPassivosAmbientaisService outrosPassivosAmbientaisService;

	@EJB
	private ContratoConvenioCefirServiceImpl contratoConvenioService;

	@EJB
	private ImovelRuralMudancaStatusJustificativaService imovelRuralMudancaStatusJustificativaService;

	@EJB
	private ImovelRuralRevalidacaoService imovelRuralRevalidacaoService;

	@EJB
	private ReservaLegalBloqueioService reservaLegalBloqueioService;

	@EJB
	private AreaService areaService;

	@EJB
	private FuncionarioService funcionarioService;

	@EJB
	private ImovelRuralUsoAguaIntervencaoService usoAguaIntervencaoService;

	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;

	@EJB
	private PctImovelRuralFacade pctImovelRuralFacade;
	
	@Inject
	private ValidacaoGeoSeiaDAOIf validarGeoSeiaDAO;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	
	private ImovelRural imovel;
	private PessoaImovel pessoa;
	private ReservaLegal reserva;
	private Empreendimento empreendimento;
	private ReservaLegalTramite reservaTramite;
	private ImovelRuralAbastecimento imovRuralAbastecimento;
	private SupressaoVegetacao supressaoVegetacao;



	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<SistemaCoordenada> listarDatum() {

		try {
			return serviceDatum.listarDatum();
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoFinalidadeUsoAgua> buscarFinalidadeUsoAguaByIdeTipologia(ModalidadeOutorgaEnum modalidadeOutorgaEnum){
		try {
			return tipoFinalidadeUsoAguaService.buscarFinalidadeUsoAguaByIdeTipologia(modalidadeOutorgaEnum.getIdTipologia());
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoRecuperacao> listarTipoRecuperacao(){
		try {
			return tipoRecuperacaoService.listarTipoRecuperacao();
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}



	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarImovelRural(ImovelRuralDTO imovelRuralDTO){
		this.imovel = imovelRuralDTO.getImovelRural();

		this.pessoa = imovelRuralDTO.getPessoaImovel();
		this.pessoa.setDtcCriacao(new Date());
		this.empreendimento = empreendimentoService.carregarById(imovelRuralDTO.getEmpreendimento().getIdeEmpreendimento());
		this.reservaTramite = imovelRuralDTO.getReservaTramite();

		imovelRuralService.salvarImovelRural(imovel);
		if(!Util.isNullOuVazio(pessoa) && !Util.isNullOuVazio(pessoa.getIdePessoa())){
			imovelRuralService.salvarImovelPessoa(pessoa);
		}
		Collection<Imovel> listaImoveis = this.empreendimento.getImovelCollection();
		listaImoveis.add(this.imovel.getImovel());
		this.empreendimento.setImovelCollection(listaImoveis);
		empreendimentoService.atualizarEmpreendimento(empreendimento);


		reservaTramiteService.salvar(this.reservaTramite);
		if(imovel.getIndReservaLegal()) {
			reservaLegalService.salvar(reserva);
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarImovelRuralDoRequerimento(ImovelRuralDTO imovelRuralDTO) {
		this.imovel = imovelRuralDTO.getImovelRural();

		this.pessoa = imovelRuralDTO.getPessoaImovel();
		this.pessoa.setDtcCriacao(new Date());
		this.reserva = imovelRuralDTO.getReservaLegal();
		this.imovRuralAbastecimento = imovelRuralDTO.getImovelRuralAbastecimento();
		this.supressaoVegetacao = imovelRuralDTO.getSupressaoVegetacao();

		List<ProcessoTramiteImovelRural> pTramiteRural = (ArrayList<ProcessoTramiteImovelRural>) this.imovel.getProcessoTramiteImovelRuralCollection();
		this.imovel.setProcessoTramiteImovelRuralCollection(null);

		imovelRuralService.salvarImovelRural(this.imovel);
		for (ProcessoTramiteImovelRural processoTramiteImovelRural : pTramiteRural) {
			processoTramiteImovelRural.setIdeImovelRural(this.imovel);
			processoTramiteImovelRural.setDtcCriacao(new Date());
		}

		processoTramiteImovelRuralDAO.salvarEmLote(pTramiteRural);

		if(!Util.isNullOuVazio(pessoa) && !Util.isNullOuVazio(pessoa.getIdePessoa())){
			imovelRuralService.salvarImovelPessoa(pessoa);
		}

		if(!Util.isNullOuVazio(imovelRuralDTO.getPessoaImovelSolicitante()) && !Util.isNullOuVazio(imovelRuralDTO.getPessoaImovelSolicitante().getIdePessoa())){
			imovelRuralService.salvarImovelPessoa(imovelRuralDTO.getPessoaImovelSolicitante());
		}

		if(!Util.isNullOuVazio(imovelRuralDTO.getPessoaQueSalvouOuEditouImovel()) && !Util.isNullOuVazio(imovelRuralDTO.getPessoaQueSalvouOuEditouImovel().getIdePessoa())){
			imovelRuralService.salvarImovelPessoa(imovelRuralDTO.getPessoaQueSalvouOuEditouImovel());
		}

		if(!Util.isNullOuVazio(reserva))
			reservaLegalService.salvar(reserva);

		if(!Util.isNullOuVazio(imovRuralAbastecimento))
			imovelRuralAbastecimentoDAO.salvar(imovRuralAbastecimento);

		if(!Util.isNullOuVazio(supressaoVegetacao))
			supressaoVegetacaoDAO.salvar(supressaoVegetacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarImovelPessoa(PessoaImovel pessoaImovel)  {
		imovelRuralService.salvarImovelPessoa(pessoaImovel);
	}

	public Integer qtdImoveisPendentesPorRequerente(Integer idePessoa){
		return imovelRuralService.qtdImoveisPendentesPorRequerente(idePessoa);
	}

	public Boolean isPrimeiroEmpreendimentoAssociado(Imovel imovel, Empreendimento empreend) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideImovel", imovel.getIdeImovel());

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ide_empreendimento FROM imovel_empreendimento" +
					" WHERE ide_imovel = :ideImovel"+
					" ORDER BY dtc_associacao ASC, ide_empreendimento;");

		Empreendimento emp = new Empreendimento();
		emp = empreendimentoService.buscaEmpreendimentoAssociadoAoImovelOrdenadoPorDataCriacao(imovel);

		if (emp.getIdeEmpreendimento().equals(empreend.getIdeEmpreendimento())) {
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void desassociarImovel(Imovel imovel, Integer ideEmpreendimento)  {
		imovelRuralService.desassociarImovel(imovel, ideEmpreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirImovel(Imovel imovel) {
		imovelRuralService.excluir(imovel);
	}
	
	public List<ImovelRural> carregarImoveisRuraisByRequerimento(Integer ideRequerimento) {
		return this.imovelRuralService.carregarTipoReservaImovelByRequerimento(ideRequerimento);
	}


	public Requerimento obterRequerimentoByIdeImovel(Integer ideImovel)  {
		return this.requerimentoService.carregarRequerimentoByIdeImovel(ideImovel);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasCertificado(Integer ideImovel) {
	    Requerimento requerimento = obterRequerimento(ideImovel);
	    if(!Util.isNullOuVazio(requerimento)) {
	    	Certificado certificado = this.certificadoService.obterUltimoCertificadoPorRequerimento(requerimento.getIdeRequerimento());
	    	if(!Util.isNullOuVazio(certificado)) {
	    		return certificado.isCefir();
	    	}
	    	return false;
	    }
	    return false;
	}

	private Requerimento obterRequerimento(Integer ideImovel)  {
	    return requerimentoService.carregarRequerimentoByIdeImovel(ideImovel);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasTermosDeCompromisso(Integer ideImovel)  {
		Requerimento requerimento = requerimentoService.carregarRequerimentoByIdeImovel(ideImovel);
	    if(!Util.isNullOuVazio(requerimento)) {
		Certificado certificado = this.certificadoService.obterUltimoCertificadoPorRequerimento(requerimento.getIdeRequerimento());
		if(!Util.isNullOuVazio(certificado)) {
		    return certificado.isTermoCompromisso();
		}
		return false;
	    }
	    return false;
	}

	public List<Certificado> obterCertificadoPorImovelAndTipo(
		Integer ideImovel, Integer tipoCertificado) {
	    return this.certificadoService.carregarByIdeImovelAndTipo(ideImovel, tipoCertificado);
	}


	public void salvarCertificado(Certificado certificado)  {
		String numeroCertificado = this.certificadoService.gerarNumeroCertificadoByTipo(certificado);
		certificado.setNumCertificado(numeroCertificado);
		this.certificadoService.salvar(certificado);
	}

	public void invalidarCertificado(ImovelRural imovelRural) {
	    certificadoService.limparNumeroTokenCertificadoDoImovel(imovelRural);
	    imovelRuralService.invalidarCertificado(imovelRural);
	}


	/**
	 * Carrega completamente o objeto imovelRural usando criteria, apenas informando o id do imovel como parametro.
	 * @param id
	 * @return ImovelRural
	 * @throws Exception
	 * @author carlos.duarte
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRural carregarTudo(Integer id) {
		if(id == -1) return new ImovelRural(-1);

		ImovelRural imovelRural = imovelRuralService.carregar(id);

		if(!Util.isNullOuVazio(imovelRural)) {
			imovelRural.getImovel().setPessoaImovelCollection(imovelRuralService.filtrarPROPRIETARIOImovel(imovelRural.getImovel()));
		}

		return imovelRural;
	}

	public Collection<PessoaImovel> filtrarPessoasPorImovel(Imovel imovel) {
		return imovelRuralService.filtrarPessoasPorImovel(imovel);
	}

	public Collection<Pessoa> listarProprietariosImovel(Integer ideImovel)  {
		return imovelRuralService.listarProprietariosImovel(ideImovel);
	}

	/**
	 * @author carlos.duarte
	 * Retorna a lista de pessoas que tem relacionamento com o imóvel, sendo elas proprietárias ou não.
	 * @param imovel
	 * @return Collection<PessoaImovel>
	 * @throws Exception
	 */
	public List<PessoaImovel> filtrarPessoasProprietariasOuNaoPorImovel(Imovel imovel) {
		return imovelRuralService.filtrarPessoasProprietariasOuNaoPorImovel(imovel);
	}

	/**
	 * Retorna a lista de pessoas que tem relacionamento com o imóvel, sendo elas proprietárias.
	 * @author Desconhecido, mas quem fez esse comentário javadoc foi Micael Coutinho
	 * @param imovel
	 * @return Collection<PessoaImovel>
	 * @throws Exception
	 */
	public Collection<PessoaImovel> filtrarPROPRIETARIOImovel(Imovel imovel) {
		return imovelRuralService.filtrarPROPRIETARIOImovel(imovel);
	}

	public ReservaLegal buscaReservaLegalByImovelRural(ImovelRural imovelRural){
		return reservaLegalService.buscaReservaLegalByImovelRural(imovelRural);
	}

	public void salvarReservaLegal(ReservaLegal reservalegal){
		reservaLegalService.salvar(reservalegal);
	}

	public List<Telefone> filtraTelefonePorPessoa(Pessoa pessoa) throws Exception {
		return telefoneService.buscarTelefonesPorPessoa(pessoa);
	}

	public List<RepresentanteLegal> listaRepresentanteLegalByPessoa(PessoaJuridica pessoaJuridica) {
		return representanteLegalService.listarRepresentanteLegalPorPessoaJuridica(pessoaJuridica);
	}

	public Collection<ResponsavelImovelRural> filtrarResponsaveisPorImovelRural(ImovelRural imovelRural) {
		return responsavelImovelRuralService.filtrarResponsaveisPorImovelRural(imovelRural);
	}

	public List<DadoGeografico> listarDadoGeografico(LocalizacaoGeografica locGeo, Integer classifSec){
		return localizacaoGeograficaService.listarDadoGeografico(locGeo, classifSec);
	}

	public Boolean verificaSeExisteTheGeomValido(Integer ideLocalizacaoGeograf){
		return localizacaoGeograficaService.verificaSeExisteTheGeomValido(ideLocalizacaoGeograf);
	}

	public LocalizacaoGeografica carregarSomenteComSistemaCoordenada(Integer ide){
		return localizacaoGeograficaService.carregarSomenteComSistemaCoordenada(ide);
	}

	public LocalizacaoGeografica carregarLocalizacaoGeografica(Integer ide){
		return localizacaoGeograficaService.carregar(ide);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDadosPersistidosLocalizacao(Integer pIdeLocGeo){
		localizacaoGeograficaService.excluirDadosPersistidos(pIdeLocGeo);
	}

	public void salvarOuAtualizarLocalizacaoGeografica(LocalizacaoGeografica pLocalizacaoGeografica) {
		localizacaoGeograficaService.salvarOuAtualizarLocalizacaoGeografica(pLocalizacaoGeografica);
	}

	public void atualizarLocalizacaoGeografica(LocalizacaoGeografica pLocalizacaoGeografica) {
		localizacaoGeograficaService.atualizarLocalizacaoGeografica(pLocalizacaoGeografica);
	}

	public void salvarlocalizacaoComImovel(LocalizacaoGeografica localizacaoGeografica, ImovelRural imovelRural) {
		localizacaoGeograficaService.salvarlocalizacaoComImovel(localizacaoGeografica, imovelRural);
	}

	public void excluirDadosPersistidos(Integer pIdeLocGeo){
		localizacaoGeograficaService.excluirDadosPersistidos(pIdeLocGeo);
	}

	public void excluirLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica){
		localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(localizacaoGeografica);
	}

	public void persistirShapes(LocalizacaoGeografica localizacaoGeografica, Double codigoMunicipio) throws Exception {
		localizacaoGeograficaService.persistirShapes(localizacaoGeografica, codigoMunicipio);
	}

	public SistemaCoordenada carregarDatum(Integer ideDatum){
		return serviceDatum.carregar(ideDatum);
	}

	public List<ClassificacaoSecaoGeometrica> listarClassificacaoSecaoGeometrica(){
		return serviceClassifSecGeometrica.listarClassificacaoSecaoGeometrica();
	}

	public List<CronogramaEtapa> listarCronogramaEtapaByCronogramaRecuperacao(CronogramaRecuperacao pCronogramaRecuperacao){
		return cronogramaEtapaService.listarCronogramaEtapaByCronogramaRecuperacao(pCronogramaRecuperacao);
	}

	public void excluirAllEtapasByIdeCronogramaRecuperacao(Integer ideCronogramaRecuperacao){
		cronogramaEtapaService.excluirAllByIdeCronogramaRecuperacao(ideCronogramaRecuperacao);
	}

	public void salvarCronogramaEtapa(CronogramaEtapa pCronogramaEtapa){
		cronogramaEtapaService.salvar(pCronogramaEtapa);
	}

	public void salvarOuAtualizarCronogramaRecuperacao(CronogramaRecuperacao pCronogramaRecuperacao){
		cronogramaRecuperacaoService.salvarOuAtualizar(pCronogramaRecuperacao);
	}
	public void salvarCronogramaRecuperacao(CronogramaRecuperacao pCronogramaRecuperacao) {
		cronogramaRecuperacaoService.salvar(pCronogramaRecuperacao);
	}

	public void atualizarCronogramaRecuperacao(CronogramaRecuperacao pCronogramaRecuperacao) {
		cronogramaRecuperacaoService.atualizar(pCronogramaRecuperacao);
	}

    public void salvarDocumentoPrad(DocumentoImovelRural arqSalvar){
    	cronogramaRecuperacaoService.salvarDocumentoPrad(arqSalvar);
    }

	public CronogramaRecuperacao listarCronogramaRecuperacaoByReservaLegal(ReservaLegal pReservaLegal){
		return cronogramaRecuperacaoService.listarCronogramaRecuperacaoByReservaLegal(pReservaLegal);
	}

	public CronogramaRecuperacao listarCronogramaRecuperacaoByApp(App pApp){
		return cronogramaRecuperacaoService.listarCronogramaRecuperacaoByApp(pApp);
	}

	public List<CronogramaRecuperacao> listarCronogramaRecuperacaoById(CronogramaRecuperacao pCronogramaRecuperacao){
		return cronogramaRecuperacaoService.listarCronogramaRecuperacaoById(pCronogramaRecuperacao);
	}

	public CronogramaRecuperacao filtrarCronogramaRecuperacaoById(CronogramaRecuperacao cronogramaRecuperacao){
		return cronogramaRecuperacaoService.filtrarCronogramaRecuperacaoById(cronogramaRecuperacao);
	}

	public List<CronogramaRecuperacao> filtrarCronogramaRecuperacaoByDocumento(CronogramaRecuperacao cronogramaRecuperacao){
		return cronogramaRecuperacaoService.filtrarCronogramaRecuperacaoByDocumento(cronogramaRecuperacao);
	}

	public void removerDocumentoPrad(CronogramaRecuperacao cronogramaRecuperacao){
		cronogramaRecuperacaoService.removerDocumentoPrad(cronogramaRecuperacao);
	}

	public void removerCronogramaRecuperacao(CronogramaRecuperacao pCronogramaRecuperacao) {
		cronogramaRecuperacaoService.excluir(pCronogramaRecuperacao);
	}

	public Collection<App> listarAppByImovelRural(ImovelRural imovelRural){
		return appService.listarAppByImovelRural(imovelRural);
	}

	public void salvarApp(App pApp)  {
		appService.salvar(pApp);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarApp(App pApp) {
		appService.salvarOuAtualizar(pApp);
	}

	public App carregarTudoApp(App pApp)  {
		return appService.carregarTudo(pApp);
	}

	public void atualizarApp(App pApp) {
		appService.atualizar(pApp);
	}

	public void excluirApp(App pApp) throws Exception {
		appService.excluir(pApp);
	}

	public Tipologia carregarTipologiaPaiByIdeFilho(Integer pIdeTipologia) throws Exception {
		return tipologiaService.carregarTipologiaPaiByIdeFilho(pIdeTipologia);
	}

	public Tipologia carregarTipologia(Integer pIdeTipologia) throws Exception {
		return tipologiaService.carregarTipologia(pIdeTipologia);
	}

	public TipologiaAtividade carregarTipologiaAtividadeByIde(Integer ideTipologiaAtividade) throws Exception {
		return tipologiaService.carregarTipologiaAtividadeByIde(ideTipologiaAtividade);
	}

	public Collection<AreaProdutivaTipologiaAtividade> carregarTipologiaAtividadeByAreaProdutiva(AreaProdutiva pAreaProdutiva) throws Exception {
		return tipologiaService.carregarTipologiaAtividadeByAreaProdutiva(pAreaProdutiva);
	}

	public AreaProdutivaTipologiaAtividadeAgricultura carregarTipologiaAtividadeAgriculturaByIde(AreaProdutivaTipologiaAtividade pIdeAreaProdutivaTipologiaAtividade) throws Exception {
		return tipologiaService.carregarTipologiaAtividadeAgriculturaByIde(pIdeAreaProdutivaTipologiaAtividade);
	}

	public AreaProdutivaTipologiaAtividadeAnimal carregarTipologiaAtividadeAnimalByIde(AreaProdutivaTipologiaAtividade pIdeAreaProdutivaTipologiaAtividade) throws Exception {
		return tipologiaService.carregarTipologiaAtividadeAnimalByIde(pIdeAreaProdutivaTipologiaAtividade);
	}

	public AreaProdutivaTipologiaAtividadePiscicultura carregarTipologiaAtividadePisciculturaByIde(Integer pIdeAreaProdutivaTipologiaAtividadePiscicultura) throws Exception {
		return tipologiaService.carregarTipologiaAtividadePisciculturaByIde(pIdeAreaProdutivaTipologiaAtividadePiscicultura);
	}

	public Collection<Tipologia> filtrarListaTipologiasByIdePai(Tipologia pTipologia) throws Exception {
		return tipologiaService.filtrarListaTipologiasByIdePai(pTipologia);
	}

	public Collection<TipologiaAtividade> filtrarListaTipologiaAtividadeByIdePai(Tipologia pTipologia) throws Exception {
		return this.tipologiaService.filtrarListaTipologiaAtividadeByIdePai(pTipologia);
	}

	public Collection<Tipologia> listarTipologia() throws Exception {
		return tipologiaService.listarTipologia();
	}

	public Collection<AreaProdutiva> listarAreaProdutivaByImovelRural(ImovelRural imovelRural) {
		return areaProdutivaService.listarAreaProdutivaByImovelRural(imovelRural);
	}

	public AreaProdutiva carregarTudoAreaProdutiva(AreaProdutiva pAreaProdutiva) {
		return areaProdutivaService.carregarTudo(pAreaProdutiva);
	}

	public void excluirAreaProdutiva(AreaProdutiva pAreaProdutiva)  {
		areaProdutivaService.excluir(pAreaProdutiva);
	}

	public void excluirAllAptaByIdeAreaProdutiva(AreaProdutiva pAreaProdutiva)  {
		areaProdutivaService.excluirAllAptaByIdeAreaProdutiva(pAreaProdutiva);
	}

	public void salvarAreaProdutiva(AreaProdutiva pAreaProdutiva)  {
		areaProdutivaService.salvar(pAreaProdutiva);
	}

	public void atualizarAreaProdutiva(AreaProdutiva pAreaProdutiva)  {
		areaProdutivaService.atualizar(pAreaProdutiva);
	}

	public void salvarOuAtualizarAreaProdutiva(AreaProdutiva pAreaProdutiva) {
		areaProdutivaService.salvarOuAtualizar(pAreaProdutiva);
	}

	public void salvarAreaProdutivaTipologiaAtividade(AreaProdutivaTipologiaAtividade pAreaProdutivaTipologiaAtividade)  {
		areaProdutivaService.salvar(pAreaProdutivaTipologiaAtividade);
	}

	public void salvarOuAtualizarAreaProdutivaTipologiaAtividade(AreaProdutivaTipologiaAtividade pAreaProdutivaTipologiaAtividade)  {
		areaProdutivaService.salvarOuAtualizar(pAreaProdutivaTipologiaAtividade);
	}

	public void salvarAreaProdutivaTipologiaAtividadeAgricultura(AreaProdutivaTipologiaAtividadeAgricultura pAPTAAG) {
		areaProdutivaService.salvarAgricultura(pAPTAAG);
	}

	public void salvarAreaProdutivaTipologiaAtividadeAnimal(AreaProdutivaTipologiaAtividadeAnimal pAPTAAN) {
		areaProdutivaService.salvarAnimal(pAPTAAN);
	}

	public void salvarAreaProdutivaTipologiaAtividadePiscicultura(AreaProdutivaTipologiaAtividadePiscicultura pAPTAPI) {
		areaProdutivaService.salvarPiscicultura(pAPTAPI);
	}

	public VegetacaoNativa listarVegetacaoNativaByImovelRural(ImovelRural imovelRural) throws Exception{
		return vegetacaoNativaService.listarVegetacaoNativaByImovelRural(imovelRural);
	}

	public void salvarVegetacaoNativa(VegetacaoNativa pVegetacaoNativa) throws Exception {
		vegetacaoNativaService.salvar(pVegetacaoNativa);
	}

	public VegetacaoNativa carregarTudoVegetacaoNativa(VegetacaoNativa pVegetacaoNativa) throws Exception {
		return vegetacaoNativaService.carregarTudo(pVegetacaoNativa);
	}

	public void atualizarVegetacaoNativa(VegetacaoNativa pVegetacaoNativa) throws Exception {
		vegetacaoNativaService.atualizar(pVegetacaoNativa);
	}

	public void excluirVegetacaoNativa(VegetacaoNativa pVegetacaoNativa) throws Exception {
		vegetacaoNativaService.remover(pVegetacaoNativa);
	}

	public Collection<TipoApp> listarTipoApp() {
		return tipoAppService.listarTipoApp();
	}

	public Collection<SubTipoApp> listarSubTipoApp(TipoApp tipoApp) {
		return subTipoAppService.listarSubTipoAppByTipoApp(tipoApp);
	}


	public Collection<MetodoIrrigacao> listarMetodoIrrigacao(){
		return metodoIrrigacaoService.listarMetodoIrrigacao();
	}

	public MetodoIrrigacao carregarMetodoIrrigacaoByIde(Integer ideMetodologiaIrrigacao) {
		return metodoIrrigacaoService.carregarMetodoIrrigacaoByIde(ideMetodologiaIrrigacao);
	}

	public Collection<Manejo> listarManejo() {
		return manejoService.listarManejo();
	}

	public Collection<TipoVinculoImovel> listarTipoVinculoImoveis() {
		return tipoVinculoImovelService.listarTipoVinculoImoveis();
	}

	public Collection<TipoCaptacao> listarTodosTipoCaptacao(){
		return requerimentoUnicoService.listarTodosTipoCaptacao();
	}

	public Collection<TipoFinalidadeUsoAgua> listarTodosTipoFinalidadeUsoAgua() {
		return requerimentoUnicoService.listarTodosTipoFinalidadeUsoAgua();
	}

	public Collection<TipoArl> listarTipoArls() {
		return tipoArlService.listarTipoArls();
	}

	public Collection<TipoOrigemCertificado> listarTipoOrigemCertificado() {
		return tipoOrigemCertificadoService.listarTipoOrigemCertificado();
	}

	public ImovelRuralUsoAgua obterPorId(ImovelRuralUsoAgua pImovelRuralUsoAgua) {
		return imovelRuralUsoAguaService.obterPorId(pImovelRuralUsoAgua);
	}

	public List<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAguaPorIdImovelRuralUsoAgua(ImovelRuralUsoAgua pImovelRuralUsoAgua){
		return imovelRuralUsoAguaService.listarTipoFinalidadeUsoAguaPorIdImovelRuralUsoAgua(pImovelRuralUsoAgua);
	}

	public List<ImovelRuralUsoAgua> obterListaUsoAguaImovelRural(ImovelRural imovelRural) {
		return imovelRuralUsoAguaService.obterTodosPorImovelRural(imovelRural);
	}

	public DadoGeografico buscarEntidadePorLocalizacaoGeografica(DadoGeografico vertice) {
		return verticeService.buscarEntidadePorLocalizacaoGeografica(vertice);
	}

	public StreamedContent getContentFile(String caminhoArquivo) throws Exception {
		return gerenciaArquivoService.getContentFile(caminhoArquivo);
	}

	public ReservaLegal carregarTudo(ReservaLegal pReservaLegal) {
		return reservaLegalService.carregarTudo(pReservaLegal);
	}

	public RequerimentoImovel obterUltimoRequerimentoImovel(Imovel imovel) {
	    return requerimentoImovelService.obterUltimoRequerimentoImovel(imovel);
	}

	public void gerarCertificado(TipoCertificadoEnum tipoCertificado,Integer ideImovel) {
	    Requerimento requerimento = this.obterRequerimentoByIdeImovel(ideImovel);
	    Certificado certificado = CertificadoUtil.gerarCertificadoByTipo(requerimento,tipoCertificado);
	    auditoriaFacade.salvar(certificado);
	    this.salvarCertificado(certificado);
	}

	public void gerarNovoTokenPorCertificado(Integer tipoCertificado, Integer ideImovel) {
	    List<Certificado> lCefir = obterCertificadoPorImovelAndTipo(ideImovel, tipoCertificado);
	    if(!Util.isNullOuVazio(lCefir)) {
		Certificado certificado = lCefir.get(0);
		certificadoService.gerarEValidarToken(certificado);
		certificadoService.atualizar(certificado);
	    }
	}

	/**
	 * Método responsável por gerar certificado por tipo (TC, CEFIR ou AVISO BNDES)
	 * @param imovelRural
	 * @return void
	 * @throws Exception
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	public void gerarCertificado(ImovelRural imovelRural)  {
	    if (hasCertificado(imovelRural.getIdeImovelRural())) {
	    	if(imovelRural.isTermoCompromisso()) {
	    		// MÉTODO SÓ ESTÁ PUBLICO POIS NA IMPRESSÃO AINDA GERA CERTIFICADO
	            gerarCertificado(TipoCertificadoEnum.TERMO_DE_COMPROMISSO, imovelRural.getIdeImovelRural());
	        }else {
	            gerarNovoTokenPorCertificado(TipoCertificadoEnum.CEFIR.getId(), imovelRural.getIdeImovelRural());
	        }
	    }else if(hasTermosDeCompromisso(imovelRural.getIdeImovelRural())) {
	    	if(imovelRural.getStsCertificado()) {
	    		gerarCertificado(TipoCertificadoEnum.CEFIR, imovelRural.getIdeImovelRural());
	        }else {
	            gerarNovoTokenPorCertificado(TipoCertificadoEnum.TERMO_DE_COMPROMISSO.getId(), imovelRural.getIdeImovelRural());
	        }
	    }else {
	    	if(imovelRural.isTermoCompromisso()) {
	            gerarCertificado(TipoCertificadoEnum.TERMO_DE_COMPROMISSO, imovelRural.getIdeImovelRural());
	        }else if(imovelRural.getStsCertificado()) {
	        	gerarCertificado(TipoCertificadoEnum.CEFIR, imovelRural.getIdeImovelRural());
	        }else if(imovelRural.isAvisoBndes()) {
	        	gerarCertificado(TipoCertificadoEnum.AVISO_BNDES, imovelRural.getIdeImovelRural());
	        }
	    }
	}

	public ArrayList<Imovel> listarPorRequerimento(Integer ideRequerimento) {
		return imovelRuralService.listarPorRequerimento(ideRequerimento);
	}

	public void atualizarReservaLegal(ReservaLegal pReservaLegal) {
		reservaLegalService.atualizar(pReservaLegal);
	}

	public Collection<StatusReservaLegal> listarStatusReservaLegal()  {
		return statusReservaLegalService.listarStatusReservaLegal();
	}

	public List<FiltroAuditoria> filtrarHistorico(Date dataInicio, Date dataFim, ImovelRural ideImovelRural, int first, int pageSize)
			throws Exception {
		return imovelRuralService.filtrarHistorico(dataInicio, dataFim, ideImovelRural, first, pageSize);
	}

	public List<ImovelRural> listarImovelRuralByProprietario(Pessoa proprietario)  {
		return imovelRuralService.listarImovelRuralByProprietario(proprietario, 0, imovelRuralService.countListarImovelByProprietario(proprietario));
	}

	public void salvarAverbacao(ReservaLegalAverbada pReservaLegalAverbada) {
		reservaLegalAverbadaService.salvar(pReservaLegalAverbada);
	}

	public void atualizarAverbacao(ReservaLegalAverbada pReservaLegalAverbada) {
		reservaLegalAverbadaService.atualizar(pReservaLegalAverbada);
	}

	public ReservaLegalAverbada carregarReservaLegalAverbada(ReservaLegal reservaLegal)  {
		return reservaLegalAverbadaService.carregarReservaLegalAverbada(reservaLegal);
	}

	public Boolean verificarProcessoCerberus(String processo, String certificado) {
		return reservaLegalService.verificaProcessoCerberus(processo, certificado);
	}

	public void validarAreaDeclarada(Double valorAreaDeclarada, Integer ideLocalizacao) throws Exception {
		validacaoGeoSeiaService.validarAreaDeclarada(valorAreaDeclarada, ideLocalizacao);
	}

	public void validarAreaDeclaradaShapeTemporario(Double areaDeclarada, String geometria) throws Exception {
		validacaoGeoSeiaService.validarAreaDeclaradaShapeTemporario(areaDeclarada, geometria);
	}

	public void validarCodigoIbgeMunicipioShape(String geometria, Integer pCodIbegeMunicipio) throws Exception{
		validacaoGeoSeiaService.validarCodigoIbgeMunicipioShape(geometria, pCodIbegeMunicipio);
	}

	public void validarSobreposicaoTemaShapeTemporario(String geometria, int tipo, Integer ideLocalizacao) throws Exception {
		validacaoGeoSeiaService.validarSobreposicaoTemaShapeTemporario(geometria, tipo, ideLocalizacao);
	}

	public Boolean isLocalizacaoGeograficaIgual(final String geometriaShapeTemporario, final Integer ideLocalizacaoSobreposta) throws Exception {
		return validacaoGeoSeiaService.isLocalizacaoGeograficaIgual(geometriaShapeTemporario, ideLocalizacaoSobreposta);
	}

	public void validarLocalizacaoGeografica(Integer ideLocalizacaoA, String geometriaA, Integer ideLocalizacaoB, String geometriaB) throws Exception {
		validacaoGeoSeiaService.validarLocalizacaoGeografica(ideLocalizacaoA, geometriaA, ideLocalizacaoB, geometriaB);
	}

	public Boolean isValidaCompensacaoReservaLegal(Collection<PessoaImovel> lProprietarios, ImovelRural pImovelRural) throws Exception {
		return validacaoGeoSeiaService.isValidaCompensacaoReservaLegal(lProprietarios, pImovelRural);
	}

	public String buscarGeometriaShape(Integer ideLocalizacaoGeografica) throws Exception {
		return validacaoGeoSeiaService.buscarGeometriaShape(ideLocalizacaoGeografica);
	}

	public String buscarGeometriaShapeTemporario(Integer ideImovel, Integer tipo, String identificador) throws Exception {
		return validacaoGeoSeiaService.buscarGeometriaShapeTemporario(ideImovel, tipo, identificador);
	}

	public boolean validaPercentualSobreposicao(String geometriaA, String geometriaB) throws Exception{
		return validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaA, geometriaB);
	}

	public Boolean isRlMenor20PorCento(ImovelRural pImovelRural) throws Exception {
		return validacaoGeoSeiaService.isRlMenor20PorCento(pImovelRural);
	}

	public Usuario buscarPorLogin(String pLogin) {
		return usuarioService.buscarPorLogin(pLogin);
	}

	public List<TipoVinculoImovel> listarTipoVinculoImoveisCefir()  {
		return tipoVinculoImovelService.listarTipoVinculoImoveisCefir();
	}

	public List<TipoCadastroImovelRural> listarTipoCadastroImovelRural()  {
		return tipoCadastroImovelRuralService.listarTipoCadastroImovelRural();
	}

	public ResponsavelImovelRural salvarOuAtualizarResponsavelImovelRural(ResponsavelImovelRural responsavel)  {
		return responsavelImovelRuralService.salvarOuAtualizarResponsavelImovelRural(responsavel);
	}

	public PessoaFisica filtrarPessoaFisicaByCpf(PessoaFisica idePessoaFisica) {
		return pessoaFisicaService.filtrarPessoaFisicaByCpf(idePessoaFisica);
	}

	public Collection<Pais> listarPaises(){
		return  paisService.listar();
	}

	public List<Escolaridade> listarEscolaridadeResponsavelTecnico() {
		return escolaridadeService.listarEscolaridadeResponsavelTecnico();
	}

	public PessoaFisica carregarTudoPessoaFisica(final PessoaFisica pessoaFisica) throws Exception {
		return pessoaFisicaService.carregarTudo(pessoaFisica);
	}

	public PessoaFisica salvarOuAtualizarPessoaFisica(PessoaFisica pessoaFisica){
		return pessoaFisicaService.salvarOuAtualizarPessoaFisica(pessoaFisica);
	}

	public PessoaJuridica salvarOuAtualizarPessoaJuridica(PessoaJuridica pessoaJuridica){
		return pessoaJuridicaService.salvarOuAtualizarPessoaJuridica(pessoaJuridica);
	}

	public void salvarDocumentoObrigatorio(DocumentoImovelRural documentoResponsavel) throws Exception {
		documentoImovelRuralService.salvarDocumentoObrigatorio(documentoResponsavel);
	}

	public ResponsavelImovelRural filtrarResponsavelImovelRuralById(Integer ideResponsavelImovelRural) {
		return responsavelImovelRuralService.filtrarResponsavelImovelRuralById(ideResponsavelImovelRural);
	}

	/**
	 * Método que retorna a lista {@link ResponsavelImovelRural} com suas PK apenas.
	 *
	 * @author eduardo.fernandes
	 * @param idePessoaFisica
	 * @return {@link ResponsavelImovelRural}
	 * @throws Exception
	 */
	public List<ResponsavelImovelRural> listarResponsavelImovelRuralById(Integer idePessoaFisica)  {
		return responsavelImovelRuralService.listarResponsaveisPorIdePessoaFisica(idePessoaFisica);
	}

	public List<TipoEstadoConservacao> listarTipoEstadoConservacao() {
		return (List<TipoEstadoConservacao>) tipoEstadoConservacaoService.listarTipoEstadoConservacao();
	}

	/**
	 * Método responsável por gerenciar o salvamento dos dados básicos do imóvel rural
	 * @param pImovelRural
	 * @param imovelRuralDto
	 * @return void
	 * @throws Exception
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDadosBasicos(ImovelRural pImovelRural, ImovelRuralDTO imovelRuralDto) throws Exception {
		
		atualizarModuloFiscal(pImovelRural);
		
		validarQtdModulosFiscaisImovelBndes(pImovelRural);
		
		enderecoService.salvarEndereco(pImovelRural.getImovel().getIdeEndereco());
		
		salvarImovelRuralDoRequerimento(imovelRuralDto);
		if (!Util.isNull(imovelRuralDto.getPctImovelRural())) {
			salvarPctImovelRural(imovelRuralDto.getPctImovelRural());
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String[] salvarParamsEPersistindoShapeTheGeom(LocalizacaoGeografica locGeo, Double codigoMunicipio) throws Exception {
		return paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(locGeo, false, codigoMunicipio, false,1);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String[] persistirShapeTheGeomImportacaoImovelFinalizado(Integer ideImovel, String nomePastaArquivos, String srid, Double codigoMunicipio) throws Exception {
		return paramPersistDadoGeoService.persistirShapeTheGeomImportacaoImovelFinalizado(ideImovel, nomePastaArquivos, srid, codigoMunicipio);
	}

	public String[] persistirShapeTheGeomDesenhoImovelFinalizado(Integer ideParam, Double codigoMunicipio)  {
		return paramPersistDadoGeoService.persistirShapeTheGeomDesenhoImovelFinalizado(ideParam, codigoMunicipio);
	}

	public Integer carregarSomenteComParamPersistDadoGeo(Integer pIdeLocalizacaoGeografica){
		return paramPersistDadoGeoService.carregarSomenteComParamPersistDadoGeo(pIdeLocalizacaoGeografica);
	}

	public Integer carregarParamPersistDadoGeoPorPasta(String pPastaArquivos){
		return paramPersistDadoGeoService.carregarParamPersistDadoGeoPorPasta(pPastaArquivos);
	}

	public String[] salvarParamsEPersistindoDesenhoTheGeom(Integer pIdeParamPersistDadoGeo, Double codigoMunicipio) {
		return paramPersistDadoGeoService.salvarParamsEPersistindoDesenhoTheGeom(pIdeParamPersistDadoGeo, codigoMunicipio);
	}

	public void atualizarDocumentoObrigatorio(DocumentoImovelRural docObrigatorio) throws Exception {
		documentoImovelRuralService.atualizarDocumentoObrigatorio(docObrigatorio);
	}

	public void excluirDocumentoObrigatorio(DocumentoImovelRural documentoObrigatorio) throws Exception {
		documentoImovelRuralService.excluirDocumentoObrigatorio(documentoObrigatorio);
	}

	public void atualizarImovelRural(ImovelRural imovelRural) throws Exception {
		atualizarModuloFiscal(imovelRural);
		imovelRuralService.atualizarImovelRural(imovelRural);
	}

	public PessoaJuridica filtrarPessoaJuridicaByCnpj(PessoaJuridica idePessoaJuridica) {
		return pessoaJuridicaService.filtrarPessoaFisicaByCnpj(idePessoaJuridica);
	}

	public void removerPessoaImovel(PessoaImovel pessoaImovelExclusao) {
		imovelRuralService.removerPessoaImovel(pessoaImovelExclusao);
	}

	public void removerDocumentoAprovacaoReserva(ReservaLegal reservaLegal) {
		imovelRuralService.removerDocumentoAprovacaoRl(reservaLegal);
	}

	public void removerDocumentoAverbacaoReserva(ReservaLegalAverbada reservaLegalAverbada) {
		imovelRuralService.removerDocumentoAverbacaoRl(reservaLegalAverbada);
	}

	public void atualizarModuloFiscal(ImovelRural imovelRural) throws Exception {
		if(!Util.isNull(imovelRural.getImovel().getIdeEndereco()) && !Util.isNull(imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio())){
			Double codIbgeImovelRural = imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio();

				Double qtdModuloFiscal;
				ModuloFiscal mf;

				mf = moduloFiscalService.filtrarByCodIbge(codIbgeImovelRural).get(0);
				qtdModuloFiscal = imovelRural.getValArea()/mf.getModFiscal();

				qtdModuloFiscal = Util.converterDoubleDuasCasas(qtdModuloFiscal);

				imovelRural.setQtdModuloFiscal(qtdModuloFiscal);
		}
	}

	public List<ModuloFiscal> filtrarByCodIbge(Double pCodIbge) {
		return moduloFiscalService.filtrarByCodIbge(pCodIbge);
	}

	public Requerimento carregarRequerimentoByIdeImovel(Integer ideImovel)  {
		return requerimentoService.carregarRequerimentoByIdeImovel(ideImovel);
	}

	public EnderecoPessoa filtrarEnderecoByPessoa(Pessoa pessoa)  {
		return enderecoPessoaService.buscarEnderecoPorPessoa(pessoa);
	}

	public String gerarNumero(Requerimento requerimento) throws Exception {
		return requerimentoUnicoService.gerarNumero(requerimento);
	}

	public void inserirRequerimento(Requerimento requerimento) throws Exception {
		requerimentoUnicoService.inserirRequerimento(requerimento);
	}

	public void salvarRequerimentoImovel(RequerimentoImovel requerimentoImovel) throws Exception {
		requerimentoUnicoService.salvarRequerimentoImovel(requerimentoImovel);
	}

	public List<ImovelRuralUsoAgua> obteUsoAguaPorTipotipoUsoAgua(String tipoUsoAgua, ImovelRural imovelRural)  {
		return imovelRuralUsoAguaService.obter(tipoUsoAgua, imovelRural);
	}

	public void salvarAtualizarUsoAgua(ImovelRuralUsoAgua pImovelUsoAgua) {
		Collection<ImovelRuralUsoAguaIntervencao> listAguaIntervencao = pImovelUsoAgua.getImovelRuralUsoAguaIntervencaoCollection();
		pImovelUsoAgua.setImovelRuralUsoAguaIntervencaoCollection(null);

		imovelRuralUsoAguaService.salvarAtualizarUsoAgua(pImovelUsoAgua);

		if(!Util.isNullOuVazio(listAguaIntervencao)) {
			for (ImovelRuralUsoAguaIntervencao intervencao : listAguaIntervencao) {
				usoAguaIntervencaoService.salvarAtualizarImovelRuralUsoAguaIntervencao(intervencao);
			}
		}
		pImovelUsoAgua.setImovelRuralUsoAguaIntervencaoCollection(listAguaIntervencao);
	}

	public List<ProcessoUsoAgua> obterProcessoImovelRuralUsoAgua(ImovelRuralUsoAgua pImovelRuralUsoAgua) {
		return processoUsoAguaService.obterPorImovelRuralUsoAgua(pImovelRuralUsoAgua);
	}

	public void salvarOuAtualizarVertice(DadoGeografico dadoGeografico) {
		verticeService.salvarOuAtualizarVertice(dadoGeografico);
	}

	public void salvarParamsEPersistindoVerticeTheGeom(DadoGeografico dadoGeografico, SistemaCoordenada ideSistemaCoordenada) throws Exception {
		paramPersistDadoGeoService.salvarParamsEPersistindoVerticeTheGeom(dadoGeografico, ideSistemaCoordenada);
	}

	public void salvarImovelRuralUsoAguaTipoFinalidadeUsoAguaEmLote(
			List<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> listImovelUsoAguaFinalidades){
		imovelRuralUsoAguaService.salvarImovelRuralUsoAguaTipoFinalidadeUsoAguaEmLote(listImovelUsoAguaFinalidades);

	}

	public Processo filtrarProcessoByNumero(Processo processo) {
		return processoService.filtrarProcessoByNumero(processo);
	}

	public List<ProcessoExterno> buscarProcessoExternoBySistemaNumero(ProcessoExterno processoExterno,
			List<String> listaSistemas) throws Exception {
		return processoExternoService.buscarProcessoExternoBySistemaNumero(processoExterno, listaSistemas);
	}

	public List<ProcessoExterno> buscarProcessoExternoBySistemaNumero(ProcessoExterno processoExterno) {
		return processoExternoService.buscarProcessoExternoBySistemaNumero(processoExterno);
	}

	public Collection<ProcessoTramiteImovelRural> listarProcesTramitImovelRuralPorImovelRural(ImovelRural imovelRural) {
		return processoService.listarProcesTramitImovelRuralPorImovelRural(imovelRural);
	}

	public void excluirTipoFinalidadeUsoAguaPorIdImovelRuralUsoAgua(ImovelRuralUsoAgua imovelRuralUsoAgua){
		imovelRuralUsoAguaService.excluirTipoFinalidadeUsoAguaPorIdImovelRuralUsoAgua(imovelRuralUsoAgua);

	}
	public void excluirDadoGeografico(DadoGeografico dadoGeografico) {
		verticeService.excluir(dadoGeografico);
	}

	public List<TipoIntervencao> obterTipoIntervencao() throws Exception {
		return tipoIntervencaoService.obterTodos();
	}

	public List<Imovel> listarImoveisRuraisComInformacoesIguais(ImovelRural imovelRural) {
		return imovelRuralService.listarImoveisRuraisComInformacoesIguais(imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerReservaLegal(ReservaLegal reservaLegal) {
		reservaLegal.setIdeReservaLegalAverbada(null);
		if(!Util.isNullOuVazio(reservaLegal.getCronogramaRecuperacao())){
			cronogramaEtapaService.excluirAllByIdeCronogramaRecuperacao(reservaLegal.getCronogramaRecuperacao().getIdeCronogramaRecuperacao());
			cronogramaRecuperacaoService.excluir(reservaLegal.getCronogramaRecuperacao());
		}
		reservaLegalService.remover(new ReservaLegal(reservaLegal.getIdeReservaLegal()));
	}

	public void removerReservaLegalAverbada(ReservaLegalAverbada reservaLegalAverbada) {
		reservaLegalAverbadaService.remover(reservaLegalAverbada);
	}

	public List<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> listarImovelRuralUsoAguaTipoFinalidadeUsoAgua(ImovelRuralUsoAgua imovelRuralUsoAgua) throws Exception {
		return imovelRuralUsoAguaService. listarImovelRuralUsoAguaTipoFinalidadeUsoAgua(imovelRuralUsoAgua);

	}

	public List<Municipio> filtrarListaMunicipiosDaBahia() {
		return municipioService.filtrarListaMunicipiosDaBahia();
	}

	public Collection<Municipio> filtrarListaMunicipiosPorEstado(Estado estado) {
		return municipioService.filtrarListaMunicipiosPorEstado(estado);
	}

	public List<Bairro> filtrarBairroPorCepSemDuplicidade(Integer numCep){
		return bairroService.filtrarBairroPorCepSemDuplicidade(numCep);
	}

	public List<Logradouro> filtrarLogradouroByNome(Bairro nomBairro, Integer numCep){
		return logradouroService.filtrarLogradouroByNome(nomBairro, numCep);
	}
	
	public List<Logradouro> filtrarLogradouroByNomeAndApi(Bairro nomBairro, Integer numCep){
		return logradouroService.filtrarLogradouroByNomeAndApi(nomBairro, numCep);
	}

	public Municipio isCepValido(Integer numCep) {
		return municipioService.validarCep(numCep);
	}

	public void excluirSupressaoVegetacaoPorImovelRural(Integer ideImovelRural){
		imovelRuralService.excluirSupressaoVegetacaoPorImovelRural(ideImovelRural);
	}

	public Pessoa filtrarRequerenteImovelRural(Pessoa idePessoa) {
		Pessoa p = new Pessoa();
		p.setPessoaFisica(pessoaFisicaService.buscarPessoaFisica(new PessoaFisica(idePessoa.getIdePessoa())));
		p.setPessoaJuridica(pessoaJuridicaService.buscarPessoaJuridicaByIde(idePessoa.getIdePessoa()));

		return p;
	}

	public void salvarJustificativaMudancaStatus(ImovelRuralMudancaStatusJustificativa iRmudancaStatusJustificativa) throws Exception {
		mudancaStatusJustificativaService.salvar(iRmudancaStatusJustificativa);
	}
	
	public void salvarDesbloqueio(ImovelRuralDesbloqueio imovelRuralDesbloqueio) throws Exception {
		desbloqueioService.salvar(imovelRuralDesbloqueio);
	}

	public void validarSeCedeAreaParaCompensarReservaLegal(Integer ideImovelRural) throws Exception {
		mudancaStatusJustificativaService.validarSeCedeAreaParaCompensarReservaLegal(ideImovelRural);
	}

	public void validarSeReservaAprovadaOuAverbada(ImovelRural imovelRural) throws Exception {
		mudancaStatusJustificativaService.validarSeReservaAprovadaOuAverbada(imovelRural);
	}

	public void validarPossuiVinculoComEmpreendimento(ImovelRural imovelRural) throws Exception {
		mudancaStatusJustificativaService.validarPossuiVinculoComEmpreendimento(imovelRural);
	}

	public List<FaseAssentamentoImovelRural> listarFaseAssentamentoImovelRural() {
		return faseAssentamentoImovelRuralService.listarFaseAssentamentoImovelRural();
	}

	public void salvarImovelRuralSicar(ImovelRuralSicar imovelRuralSicar)  {
		imovelRuralSicarService.salvar(imovelRuralSicar);
	}
	public List<AssociacaoIncra> listAssociacaoIncraPorImovelRural(ImovelRural ideImovelRural) {
		List<AssociacaoIncraImovelRural> listAssociacaoIncraImovelRural = associacaoIncraImovelRuralService.listarAssociacaoIncraImovelRuralPorImovelRural(ideImovelRural);
		return associacaoIncraService.listarAssociacaoIncraPorImovelRural(listAssociacaoIncraImovelRural);
	}

	public void atualizarImovelRuralSicar(ImovelRuralSicar imovelRuralSicar) {
		imovelRuralSicarService.atualizar(imovelRuralSicar);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarImovelRuralSicarComMerge(ImovelRuralSicar imovelRuralSicar) {
		imovelRuralSicarService.atualizarComMerge(imovelRuralSicar);
	}

	public Collection<TipoDocumentoImovelRural> listarTipoDocumentoImovelRuralPorIdeTipoVinculoImovel(Integer ideTipoVinculoImovel) {
		return tipoDocumentoImovelRuralService.findByIdeTipoVinculoImovel(ideTipoVinculoImovel);
	}

	public DocumentoImovelRuralPosse filtrarDocumentoImovelRuralPosseById(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse) throws Exception {
		return documentoImovelRuralPosseService.filtrarById(pDocumentoImovelRuralPosse);
	}

	public DocumentoImovelRuralPosse carregarTudoDocumentoImovelRuralPosse(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse) throws Exception {
		return documentoImovelRuralPosseService.carregarTudo(pDocumentoImovelRuralPosse);
	}

	public DocumentoImovelRuralPosse buscarDocumentoImovelRuralPosseByImovelRural(ImovelRural imovelRural) throws Exception{
		return documentoImovelRuralPosseService.buscarByImovelRural(imovelRural);
	}

	public void salvarDocumentoImovelRuralPosse(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse) throws Exception {
		documentoImovelRuralPosseService.salvar(pDocumentoImovelRuralPosse);
	}

	public void atualizarDocumentoImovelRuralPosse(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse) throws Exception {
		documentoImovelRuralPosseService.atualizar(pDocumentoImovelRuralPosse);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDocumentoImovelRuralPosse(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse) throws Exception {
		documentoImovelRuralPosseService.remover(pDocumentoImovelRuralPosse);
	}

	public void excluirImovelRuralTacPorImovelRural(Integer ideImovelRural) {
		imovelRuralService.excluirImovelRuralTacPorImovelRural(ideImovelRural);
	}


	public void excluirImovelRuralPradPorImovelRural(Integer ideImovelRural) {
		imovelRuralService.excluirImovelRuralPradPorImovelRural(ideImovelRural);
	}
	public void salvarImovelRuralRppn(ImovelRuralRppn imovelRuralRppn) {
		imovelRuralRppnService.salvar(imovelRuralRppn);
	}

	public ImovelRuralRppn carregarImovelRuralRppnByIdeImovelRural(ImovelRural imovelRural) {
		return imovelRuralRppnService.buscarImoveRuralRppnByImovelRural(imovelRural);
	}

	public void atualizarImovelRuralRppn(ImovelRuralRppn imovelRuralRppn) {
		imovelRuralRppnService.atualizar(imovelRuralRppn);
	}

	public void excluirImovelRuralRppnByIdeImovelRural(ImovelRural imovelRural){
		imovelRuralRppnService.excluirImovelRuralRppnPorImovelRural(imovelRural);
	}

	public Collection<TipoFinalidadeVegetacaoNativa> listarTipoFinalidadeVegetacaoNativa(){
		return tipoFinalidadeVegetacaoNativaService.listarTipoFinalidadeVegetacaoNativa();
	}
	public List<AssentadoIncraImovelRural> listarAssentadoIncraImovelRuralPorImovelRural(ImovelRural ideImovelRural){
		return assentadoIncraImovelRuralService.listarAssentadoIncraImovelRuralPorImovelRural(ideImovelRural);
	}

	public List<AssentadoIncra> listAssentadosIncraDoImovelRuralPorAssociacao(AssociacaoIncra ideAssociacaoIncra, ImovelRural ideImovelRural) {
		AssociacaoIncraImovelRural associacaoIncraImovelRural = associacaoIncraImovelRuralService.buscarAssociacaoIncraImovelRural(ideAssociacaoIncra, ideImovelRural);
		if(Util.isNull(associacaoIncraImovelRural)) {
			return new ArrayList<AssentadoIncra>();
		}
		List<AssociacaoAssentadoImovelRuralIncra> associacaoAssentadoIncraImovelRural = associacaoAssentadoImovelRuralIncraService.listarPorIdeAssociacaoIncraImovelRural(associacaoIncraImovelRural);
		return assentadoIncraService.listAssentadosIncraDoImovelRuralPorAssociacao(associacaoAssentadoIncraImovelRural);
	}

	public void salvarAssociacaoIncra(AssociacaoIncra associacaoIncra, ImovelRural ideImovelRural) {
		associacaoIncraService.salvarOuAtualizar(associacaoIncra);

		AssociacaoIncraImovelRural associacaoIncraImovelRural = associacaoIncraImovelRuralService.buscarAssociacaoIncraImovelRural(associacaoIncra, ideImovelRural);
		if(Util.isNullOuVazio(associacaoIncraImovelRural)) {
			associacaoIncraImovelRural = new AssociacaoIncraImovelRural();
		}
		associacaoIncraImovelRural.setIdeAssociacaoIncra(associacaoIncra);
		associacaoIncraImovelRural.setIdeImovelRural(ideImovelRural);
		associacaoIncraImovelRuralService.salvarOuAtualizar(associacaoIncraImovelRural);
	}

	public void salvarAssentadoIncra(AssentadoIncra assentadoIncra) {
		assentadoIncraService.salvarOuAtualizar(assentadoIncra);
	}

	public void salvarAssentadoIncraImovelRural(AssentadoIncraImovelRural assentadoIncraImovelRural) {
		List<AssociacaoAssentadoImovelRuralIncra> listAssociacoes = null;
		if(Util.isNullOuVazio(assentadoIncraImovelRural.getAssociacaoAssentadoImovelRuralIncraCollection())) {
			listAssociacoes = new ArrayList<AssociacaoAssentadoImovelRuralIncra>();
		}else{
			listAssociacoes = new ArrayList<AssociacaoAssentadoImovelRuralIncra>(assentadoIncraImovelRural.getAssociacaoAssentadoImovelRuralIncraCollection());
		}
		assentadoIncraImovelRural.setAssociacaoAssentadoImovelRuralIncraCollection(null);
		assentadoIncraImovelRuralService.salvarOuAtualizar(assentadoIncraImovelRural);
		assentadoIncraImovelRural.setAssociacaoAssentadoImovelRuralIncraCollection(new ArrayList<AssociacaoAssentadoImovelRuralIncra>(listAssociacoes));
		associacaoAssentadoImovelRuralIncraService.excluirAllByAssentadoIncraImovelRural(assentadoIncraImovelRural);

		if(!Util.isNullOuVazio(assentadoIncraImovelRural.getAssociacaoAssentadoImovelRuralIncraCollection())) {
			for(AssociacaoAssentadoImovelRuralIncra associacaoAssentadoImovelRuralIncra : assentadoIncraImovelRural.getAssociacaoAssentadoImovelRuralIncraCollection()){
				if(!Util.isNull(associacaoAssentadoImovelRuralIncra.getIdeAssociacaoIncraImovelRural()) && Util.isNull(associacaoAssentadoImovelRuralIncra.getIdeAssociacaoIncraImovelRural().getIdeAssociacaoIncraImovelRural())) {
						AssociacaoIncraImovelRural associacaoIncraImovelRural = associacaoIncraImovelRuralService.buscarAssociacaoIncraImovelRural(associacaoAssentadoImovelRuralIncra.getIdeAssociacaoIncraImovelRural().getIdeAssociacaoIncra(), assentadoIncraImovelRural.getIdeImovelRural());
						if(associacaoIncraImovelRural != null) {
							associacaoAssentadoImovelRuralIncra.setIdeAssociacaoIncraImovelRural(associacaoIncraImovelRural);
						}
					associacaoIncraImovelRuralService.salvarOuAtualizar(associacaoAssentadoImovelRuralIncra.getIdeAssociacaoIncraImovelRural());
				}
				associacaoAssentadoImovelRuralIncraService.salvar(associacaoAssentadoImovelRuralIncra);
			}
		}

	}

	public void excluirAssociacaoAssentadoImovelRuralIncra(AssociacaoAssentadoImovelRuralIncra associacaoAssentadoImovelRuralIncra) {
		associacaoAssentadoImovelRuralIncraService.excluir(associacaoAssentadoImovelRuralIncra);
	}

	public void salvarAssociacaoAssentadoImovelRuralIncra(AssociacaoAssentadoImovelRuralIncra associacaoAssentadoImovelRuralIncra) {
		associacaoAssentadoImovelRuralIncraService.salvar(associacaoAssentadoImovelRuralIncra);
	}

	public void excluirAssocicao(AssociacaoIncra associacaoIncra, ImovelRural ideImovelRural) {
		associacaoIncraImovelRuralService.excluir(associacaoIncraImovelRuralService.buscarAssociacaoIncraImovelRural(associacaoIncra, ideImovelRural));
	}

	public AssociacaoIncraImovelRural buscarAssociacaoIncraImovelRural(AssociacaoIncra ideAssociacaoIncra, ImovelRural ideImovelRural)  {
		return associacaoIncraImovelRuralService.buscarAssociacaoIncraImovelRural(ideAssociacaoIncra, ideImovelRural);
	}

	public void excluirAssentadoIncraImovelRural(AssentadoIncraImovelRural assentadoIncraImovelRural)  {
		associacaoAssentadoImovelRuralIncraService.excluirAllByAssentadoIncraImovelRural(assentadoIncraImovelRural);
		assentadoIncraImovelRural.setAssociacaoAssentadoImovelRuralIncraCollection(null);
		assentadoIncraImovelRuralService.excluir(assentadoIncraImovelRural);
	}

	public AssociacaoIncra carregarAssociacaoIncraPorPessoaJuridica(PessoaJuridica idePessoaJuridica) {
		return associacaoIncraService.carregarAssociacaoIncraPorPessoaJuridica(idePessoaJuridica);
	}

	public AssentadoIncra carregarAssentadoIncraPorPessoaFisica(PessoaFisica idePessoaFisica) {
		return assentadoIncraService.carregarAssentadoIncraPorPessoaFisica(idePessoaFisica);
	}

	public List<AssociacaoAssentadoImovelRuralIncra> listarAssociacaoIncraDoImovelRuralPorAssentados(AssentadoIncra ideAssentadoIncra, ImovelRural ideImovelRural) {
		AssentadoIncraImovelRural assentadoIncraImovelRural = assentadoIncraImovelRuralService.buscarAssentadoIncraImovelRural(ideAssentadoIncra, ideImovelRural);
		if(Util.isNull(assentadoIncraImovelRural)) {
			return new ArrayList<AssociacaoAssentadoImovelRuralIncra>();
		}
		return associacaoAssentadoImovelRuralIncraService.listarPorIdeAssentadoIncraImovelRural(assentadoIncraImovelRural);
	}

	public List<AssociacaoAssentadoImovelRuralIncra> listarAssociacaoAssentadoImovelRuralIncraPorAssentadoIncraImovelRural(AssentadoIncraImovelRural assentadoIncraImovelRural)  {

		return associacaoAssentadoImovelRuralIncraService.listarPorIdeAssentadoIncraImovelRural(assentadoIncraImovelRural);
	}

	public List<AssentadoIncraImovelRural> carregarAssentadosDaPlanilha(String pathNomeArquivo) throws Exception {
		return assentadoIncraService.carregarAssentadosDaPlanilha(pathNomeArquivo);
	}

	public void validarAssentadoLote(List<AssentadoIncraImovelRural> listAssentadosPlanilha, ImovelRural ideImovel, String geometria) throws Exception {
		validacaoGeoSeiaService.validarAssentadoLote(ideImovel.getIdeImovelRural(), listAssentadosPlanilha, geometria);
	}

	public void salvarAreaConsolidada(AreaRuralConsolidada areaRuralConsolidada)  {
		areaRuralConsolidadaService.salvarOuAtualizar(areaRuralConsolidada);
	}

	public void atualizarAreaConsolidada(AreaRuralConsolidada areaRuralConsolidada) {
		areaRuralConsolidadaService.salvarOuAtualizar(areaRuralConsolidada);
	}

	public AreaRuralConsolidada carregarTudoAreaConsolidada(AreaRuralConsolidada areaRuralConsolidada)  {
		return areaRuralConsolidadaService.carregarTudo(areaRuralConsolidada);
	}

	public AreaRuralConsolidada listarAreaRuralConsolidadaNativaByImovelRural(ImovelRural imovelRural)  {
		return areaRuralConsolidadaService.listarAreaRuralConsolidadaByImovelRural(imovelRural);
	}

	public GeoJsonSicar buscarGeoJsonSicar(List<Integer> localizacoes) throws Exception {
		return validacaoGeoSeiaService.buscarGeoJsonSicar(localizacoes);
	}

	public OutrosPassivosAmbientais buscaOutrosPassivosAmbientaisByImovelRural(ImovelRural imovelRural) {
		return outrosPassivosAmbientaisService.buscaOutrosPassivosAmbientaisByImovelRural(imovelRural);
	}

	public void salvarOutrosPassivosAmbientais(OutrosPassivosAmbientais pOutrosPassivosAmbientais) throws Exception{
		CronogramaRecuperacao cronogramaTemp = pOutrosPassivosAmbientais.getCronogramaRecuperacao();
		pOutrosPassivosAmbientais.setCronogramaRecuperacao(null);
		outrosPassivosAmbientaisService.salvar(pOutrosPassivosAmbientais);
		pOutrosPassivosAmbientais.setCronogramaRecuperacao(cronogramaTemp);
		pOutrosPassivosAmbientais.getCronogramaRecuperacao().setIdeOutrosPassivosAmbientais(pOutrosPassivosAmbientais);
		pOutrosPassivosAmbientais.getCronogramaRecuperacao().setDtcCriacao(new Date());
		salvarOuAtualizarCronogramaRecuperacao(pOutrosPassivosAmbientais.getCronogramaRecuperacao());
	}

	public void atualizarOutrosPassivosAmbientais(OutrosPassivosAmbientais pOutrosPassivosAmbientais) throws Exception {
		if(!Util.isNullOuVazio(pOutrosPassivosAmbientais.getCronogramaRecuperacao().getIdeCronogramaRecuperacao())){
			excluirAllEtapasByIdeCronogramaRecuperacao(pOutrosPassivosAmbientais.getCronogramaRecuperacao().getIdeCronogramaRecuperacao());
			if(!Util.isNullOuVazio(pOutrosPassivosAmbientais.getCronogramaRecuperacao().getCronogramaEtapaCollection())) {
				for(CronogramaEtapa etapa : pOutrosPassivosAmbientais.getCronogramaRecuperacao().getCronogramaEtapaCollection()){
					etapa.setIdeCronogramaEtapa(null);
				}
			}
		}else{
			pOutrosPassivosAmbientais.getCronogramaRecuperacao().setIdeOutrosPassivosAmbientais(pOutrosPassivosAmbientais);
			pOutrosPassivosAmbientais.getCronogramaRecuperacao().setDtcCriacao(new Date());
		}
		salvarOuAtualizarCronogramaRecuperacao(pOutrosPassivosAmbientais.getCronogramaRecuperacao());
		outrosPassivosAmbientaisService.atualizar(pOutrosPassivosAmbientais);
	}

	public OutrosPassivosAmbientais carregarTudoOutrosPassivosAmbientais(OutrosPassivosAmbientais pOutrosPassivosAmbientais) {
		return outrosPassivosAmbientaisService.carregarTudo(pOutrosPassivosAmbientais);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void removerOutrosPassivosAmbientais(OutrosPassivosAmbientais pOutrosPassivosAmbientais) throws Exception {
		if(!Util.isNullOuVazio(pOutrosPassivosAmbientais.getCronogramaRecuperacao()) && !Util.isNullOuVazio(pOutrosPassivosAmbientais.getCronogramaRecuperacao().getIdeCronogramaRecuperacao())){
			this.cronogramaEtapaService.excluirAllByIdeCronogramaRecuperacao(pOutrosPassivosAmbientais.getCronogramaRecuperacao().getIdeCronogramaRecuperacao());
			pOutrosPassivosAmbientais.getCronogramaRecuperacao().setCronogramaEtapaCollection(null);
			pOutrosPassivosAmbientais.getCronogramaRecuperacao().setIdeDocumentoObrigatorio(null);
			pOutrosPassivosAmbientais.getCronogramaRecuperacao().setIdeOutrosPassivosAmbientais(null);
			this.cronogramaRecuperacaoService.excluir(pOutrosPassivosAmbientais.getCronogramaRecuperacao());
			pOutrosPassivosAmbientais.setCronogramaRecuperacao(null);
		}
		outrosPassivosAmbientaisService.remover(pOutrosPassivosAmbientais);
		this.documentoImovelRuralService.excluirDocumentoObrigatorio(pOutrosPassivosAmbientais.getIdeDocumentoPrad());
	}

	public ImovelRural montarGeracaoCertificadoImovelRural(ImovelRural imovelRural) throws Exception {
		return imovelRuralService.montarGeracaoCertificadoImovelRural(imovelRural);
	}

	public boolean validaExclusaoProprietario(PessoaImovel proprietarioExclusao, ImovelRural imovelRural) throws Exception {
		return imovelRuralService.validaExclusaoProprietario(proprietarioExclusao, imovelRural);
	}

	public boolean verificarExistenciaProprietarios(Collection<PessoaImovel> listProprietariosImovel) {
		return imovelRuralService.verificarExistenciaProprietarios(listProprietariosImovel);
	}

	public ContratoConvenioCefir buscarContratoConvenioByNumero(String numContratoConvenioCefir) {
		return contratoConvenioService.buscarContratoConvenioByNumero(numContratoConvenioCefir);
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvarBairroDeclarante(Bairro bairro) {
		bairroService.salvarBairro(bairro);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvarLogradouroDeclarante(Logradouro logradouro) {
		logradouroService.salvarLogradouro(logradouro);
	}

	public boolean validaTipoGeometriaPoligono(Integer ideLocalizacao, String geometria) throws Exception {
		return validacaoGeoSeiaService.validaTipoGeometriaPoligono(ideLocalizacao, geometria);
	}

	public boolean validaTipoGeometriaLinha(Integer ideLocalizacao, String geometria) throws Exception {
		return validacaoGeoSeiaService.validaTipoGeometriaLinha(ideLocalizacao, geometria);
	}

	public boolean validaTipoGeometriaPonto(Integer ideLocalizacao, String geometria) throws Exception {
		return validacaoGeoSeiaService.validaTipoGeometriaPonto(ideLocalizacao, geometria);
	}

	public Collection<ImovelRuralMudancaStatusJustificativa> listarImovelRuralMudancaStatusJustificativaPorImovelRural(ImovelRural imovelRural) throws Exception {
		return this.imovelRuralMudancaStatusJustificativaService.listarImovelRuralMudancaStatusJustificativaPorImovel(imovelRural);
	}

	public void atualizarListImovelRuralRevalidacao(Collection<ImovelRuralRevalidacao> listImovelRuralRevalidacao) {
		imovelRuralRevalidacaoService.salvarOuAtualizar(listImovelRuralRevalidacao);
	}

	public Collection<ImovelRuralRevalidacao> listarImovelRuralRevalidacaoByImovelRural(ImovelRural imovelRural){
		return imovelRuralRevalidacaoService.listarImovelRuralRevalidacaoByImovelRural(imovelRural);
	}

	public void validaImovelRuralRevalidacao(ImovelRural pImovelRural) throws Exception {
		imovelRuralRevalidacaoService.validaImovelRuralRevalidacao(pImovelRural);
	}

	/**
	 * Método para buscar a última {@link ReservaLegalBloqueio} criada para à {@link ReservaLegal}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param reservaLegal
	 * @return
	 * @throws Exception
	 * @since 20/07/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaLegalBloqueio obterUltimaReservaLegalBloqueioBy(ReservaLegal reservaLegal){
		return reservaLegalBloqueioService.obterUltimaReservaLegalBloqueioBy(reservaLegal);
	}

	/**
	 * Método para buscar a lista de {@link ReservaLegalBloqueio} criada para à {@link ReservaLegal}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param reservaLegal
	 * @return
	 * @throws Exception
	 * @since 20/07/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ReservaLegalBloqueio> listarReservaLegalBloqueioBy(ReservaLegal reservaLegal){
		return reservaLegalBloqueioService.listarReservaLegalBloqueioBy(reservaLegal);
	}

	/**
	 * Método para obter a {@link Area} daquele {@link Usuario}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param area
	 * @return
	 * @throws Exception
	 * @since 20/07/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area obterAreaByUsuario(Usuario usuario){
		return areaService.buscarAreaPorPessoaFisica(usuario.getIdePessoaFisica());
	}

	/**
	 * Método para obter o Coordenador ({@link Funcionario}) daquela {@link Area}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param area
	 * @return
	 * @throws Exception
	 * @since 20/07/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Funcionario obterCoordenadorByArea(Area area){
		return funcionarioService.obterCoordenadorPorIdeArea(area);
	}

	/**
	 * Método para persistir a {@link ReservaLegalBloqueio}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 20/07/2015
	 * @see #7112
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ReservaLegalBloqueio salvarReservaLegalBloqueio(ReservaLegalBloqueio ultimaReservaLegalBloqueio, ReservaLegalBloqueio novaReservaLegalBloqueio){

		reservaLegalBloqueioService.salvarOrAtualizarReservaLegalBloqueio(novaReservaLegalBloqueio);
		return novaReservaLegalBloqueio;
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param ultimaReservaLegalBloqueio
	 * @param novaReservaLegalBloqueio
	 * @since 21/07/2015
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarAuditoria(ReservaLegalBloqueio ultimaReservaLegalBloqueio, ReservaLegalBloqueio novaReservaLegalBloqueio) {
		if(Util.isNull(ultimaReservaLegalBloqueio)){
			auditoriaFacade.salvar(novaReservaLegalBloqueio);
		}
		else {
			auditoriaFacade.atualizar(ultimaReservaLegalBloqueio, novaReservaLegalBloqueio);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario obterSubstitutoDoCoordenador(Integer idePessoaFisica) {
		return usuarioService.retornarUsuarioSubstitutoDoCoordenador(idePessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean validaFinalizacaoImovelRural(ImovelRural imovelRural, Collection<PessoaImovel> listProprietariosImovel, TipoVinculoImovel tipoVinculoImovel, RequestContext context, Boolean aceiteCondicoesRecuperacaoOp) throws Exception {
		if(imovelRuralService.validaImovelRuralparaRequerimento(imovelRural, tipoVinculoImovel)) {
			imovelRuralService.validaImovelParaFinalizacao(imovelRural);

			if(imovelRural.isImovelPCT() || Boolean.TRUE.equals(imovelRural.getIndDesejaCompletarInformacoes())){
				reservaLegalService.validaRlParaFinalizacao(imovelRural, listProprietariosImovel, context);
				imovelRuralService.validaResponavelTecnico(imovelRural);

				if(imovelRural.isImovelINCRA()) {
					assentadoIncraImovelRuralService.validaAssentadosIncraParaFinalizacao(imovelRural);
					areaRuralConsolidadaService.validaAreaRuralConsolidadaParaFinalizacao(imovelRural);
				}else {
					imovelRuralService.validaOutrosPassivosParaFinalizacao(imovelRural, aceiteCondicoesRecuperacaoOp);
					imovelRuralService.validaUsoAguaParaFinalizacao(imovelRural);
				}

				areaProdutivaService.validaAreaProdutivaParaFinalizacao(imovelRural, context);
				appService.validaAppParaFinalizacao(imovelRural, context);
				vegetacaoNativaService.validaVegetacaoNativaParaFinalizacao(imovelRural);
				imovelRuralRppnService.validaRppnParaFinalizacao(imovelRural);
			}
		}
		return true;
	}

	public boolean existeTheGeom(LocalizacaoGeografica localizacaoGeografica) {
		return localizacaoGeograficaService.existeTheGeom(localizacaoGeografica);
	}

	public boolean possuiProcurador(ImovelRural imovelRural)  {
		return imovelRuralService.possuiProcurador(imovelRural);
	}

	public void validaOutrosPassivos(ImovelRural imovelRural, boolean aceiteCondicoesRecuperacaoOp) throws Exception {
		imovelRuralService.validaOutrosPassivosParaFinalizacao(imovelRural, aceiteCondicoesRecuperacaoOp);
	}

	public boolean validouCamposObrigatoriosAreaProdutiva(AreaProdutiva areaProdutiva, boolean isImovelIncra) {
		return areaProdutivaService.validouCamposObrigatoriosAreaProdutiva(areaProdutiva, isImovelIncra);
	}

	//Outras tipologias
	public Collection<Tipologia> carregarDivisaoAtividade() throws Exception {
		return tipologiaService.carregarTipologiasPai();
	}

	public boolean getRenderedPerguntaLicenca(AreaProdutiva areaProdutiva, boolean isVisualizacao, ImovelRural imovelRural) {
		return areaProdutivaService.getRenderedPerguntaLicenca(areaProdutiva, isVisualizacao, imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persistirReservaLegal(ImovelRural imovelRural, ReservaLegal objAntigoReservaLegal, CronogramaRecuperacao objAntigoCronogramaRecuperacaoRl) throws Exception {
		reservaLegalService.persistirReservaLegal(imovelRural, objAntigoReservaLegal, objAntigoCronogramaRecuperacaoRl);
	}

	public void validarReservaLegal(ImovelRural imovelRural) throws Exception {
		reservaLegalService.validarReservaLegal(imovelRural);
	}

	public void persistirDadoGeografico(LocalizacaoGeografica ideLocalizacaoGeografica, String geometriaRl) throws Exception {
		localizacaoGeograficaService.persistirDadoGeografico(ideLocalizacaoGeografica, geometriaRl);
	}

	public void validarTalhoesDeclaradosGeometria(Integer numUnidadeProducao, String geometriaAp) throws Exception {
		validacaoGeoSeiaService.validarTalhoesDeclaradosGeometria(numUnidadeProducao, geometriaAp);
	}

	/**
	 * Método para validar se a quantidade de módulos fiscais dos imóveis cadastrados pelo
	 * projeto CAR/BNDES/INEMA é até 4
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @param pImovelRural
	 * @throws ValidationException
	 * @since 13/10/2015
	 */
	public void validarQtdModulosFiscaisImovelBndes(ImovelRural pImovelRural) throws ValidationException {
		imovelRuralService.validarQtdModulosFiscaisImovelBndes(pImovelRural);
	}

	/**
	 * Método responsável por verificar se o usuário está vinculado a algum contrato convêndio do projeto CAR/BNDES/INEMA
	 * @param usuario
	 * @return boolean
	 * @throws Exception
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	public boolean isUsuarioVinculadoProjetoBndes(Usuario usuario) {
		return contratoConvenioService.isUsuarioVinculadoProjetoBndes(usuario);
	}

	/**
	 * Método responsável por carregar os dados do contrato convênio
	 * @param contratoConvenioCefir
	 * @return ContratoConvenioCefir
	 * @throws Exception
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 26/10/2015
	 */
	public ContratoConvenioCefir carregarContratoConvenio(ContratoConvenioCefir contratoConvenioCefir) {
		return contratoConvenioService.carregarContratoConvenio(contratoConvenioCefir);
	}

	/**
	 * Método responsável pela chamada do método no sévice para envio de email com o aviso bndes
	 * @param pImovelRural
	 * @throws Exception
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 26/10/2015
	 */
	public void enviarEmaiComAvisoBndes(ImovelRural pImovelRural) throws Exception {
		imovelRuralService.enviarEmailComAvisoImovelBndes(pImovelRural);
	}

	public StreamedContent getImprimirAvisoBndes(Integer ideImovel, boolean hasMarcaDagua){
		return imovelRuralService.getImprimirAvisoBndes(ideImovel, hasMarcaDagua);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRural> imoveisEmCompensacaoDeReserva(Integer ideImovelRural) {
		return imovelRuralService.imoveisEmCompensacaoDeReserva(ideImovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRural carregarByIde(Integer id) {
		return imovelRuralService.carregarById(id);
	}

	public SistemaCoordenada obterSistCoordPRJ(String caminhoArquivo){
		return localizacaoGeograficaService.obterSistCoordPRJ(caminhoArquivo);
	}

	public boolean validaCampoLicencaAreaProdutiva(ImovelRural imovelRural){
		return areaProdutivaService.validaCampoLicencaAreaProdutiva(imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRural> listarImovelRuralSemSicar(boolean isCount){
		return imovelRuralSicarService.listarImovelRuralComErroSincronia(isCount);
	}

	public ClassificacaoSecaoGeometrica carregarClassificacaoSecaoGeometricaPorId(Integer id){
		return serviceClassifSecGeometrica.carregar(id);
	}

	public double getAreaShapeTemporario(String geometria) throws Exception {
		return validacaoGeoSeiaService.getAreaShapeTemporario(geometria);
	}
	public double getAreaShape(String geometria) throws Exception {
		return validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(geometria);
	}

	public void validarReservaAreaDeclaradaShapeTemporario(Double areaDeclarada, Double areaShape) throws Exception {
		validacaoGeoSeiaService.validarReservaAreaDeclaradaShapeTemporario(areaDeclarada, areaShape);
	}
	public void validarValorAreaDeclaradaShapeTemporario(Double areaDeclarada, Double areaShape) throws Exception {
		validacaoGeoSeiaService.validarValorAreaDeclaradaShapeTemporario(areaDeclarada, areaShape);
	}

	public String ObterShapeDiretorio(String caminho, String nomeDoArquivo, String extensao){
		return gerenciaArquivoService.ObterShapeDiretorio(caminho, nomeDoArquivo, extensao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarAtualizacaoShapes(AtualizarShapesController asc) {
		try {
			asc.finalizar();
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<Pessoa> listarProprietariosJustoPossuidoresImovelPorDemanda(Integer ideImovel, Integer first, Integer pageSize){
		return imovelRuralService.listarProprietariosJustoPossuidoresImovelPorDemanda(ideImovel, first, pageSize);
	}
	
	public Collection<Pessoa> listarProprietariosJustoPossuidoresImovel(Integer ideImovel){
		return imovelRuralService.listarProprietariosJustoPossuidoresImovel(ideImovel);
	}
	
	public int listarProprietariosJustoPossuidoresImovelCount(Integer ideImovel) {
		return imovelRuralService.listarProprietariosJustoPossuidoresImovelCount(ideImovel);
	}
	
	public void enviarEmailTransferenciaTitularidade(List<String> destinatario,String nomeImovel) throws Exception {
		imovelRuralService.enviarEmailTransferenciaTitularidade(destinatario,nomeImovel);
	}
	
	public List<TipoTerritorioPct> listarTipoTerritorioPct() throws Exception{
		return pctImovelRuralFacade.listarTipoTerritorioPct();
	}
	
	public List<TipoSeguimentoPct> listarTipoSeguimentoPct() throws Exception{
		
		return pctImovelRuralFacade.listarTipoSeguimentoPct();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPctImovelRural(PctImovelRural pctImovelRural) throws Exception{
		pctImovelRuralFacade.salvarPctImovelRural(pctImovelRural);
	}
	
	public PctImovelRural buscarPctImovelRural(ImovelRural imovelRural) throws Exception{
		return pctImovelRuralFacade.buscarPctImovelRural(imovelRural);
	}
	
	public Collection<TipoDocumentoImovelRural> listarTipoDocumentoImovelRuralPorIdeTipoTerritorioPct(Integer ideTipoTerritorioPct) throws Exception {
		return pctImovelRuralFacade.findByIdeTipoVinculoImovelPorTerritorio(ideTipoTerritorioPct);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public TipoDocumentoImovelRural findByIdeTipoDocumentoImovelRural(Integer ideTipoDocumentoImovelRural) throws Exception {
		return tipoDocumentoImovelRuralService.findByIdeTipoDocumentoImovelRural(ideTipoDocumentoImovelRural);
	}
	
	public boolean validaSobreposicaoImovel(String geometria, Integer ideImovelRural) throws Exception {
		return validacaoGeoSeiaService.validaSobreposicaoImovelTerritorio(geometria, ideImovelRural);
	}
	
	public boolean validaSobreposicaoImovelPCT(String geometria, Integer ideImovelRural) throws Exception {
		return validacaoGeoSeiaService.validaSobreposicaoImovelTerritorioPCT(geometria, ideImovelRural);
	}
	
	public void tratarPonto(LocalizacaoGeografica locGeo) {
		localizacaoGeograficaServiceFacade.tratarPonto(locGeo);
	}
	
	public void excluirDadoGeografico(LocalizacaoGeografica localizacaoGeografica) throws Exception{
		localizacaoGeograficaService.excluirDadosPersistidos(localizacaoGeografica);
	}
	
	public boolean existePessoaJuridicaAssociadaComunidade(PctImovelRural pctImovelRural, PessoaJuridicaPct pessoaJuridicaPct) throws Exception{
		return pctImovelRuralFacade.existePessoaJuridicaAssociadaComunidade(pctImovelRural, pessoaJuridicaPct);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPessoaJuridicarPct(PessoaJuridicaPct pessoaJuridicaPct) throws Exception{
		if (Util.isNullOuVazio(pessoaJuridicaPct.getIdePctImovelRural().getIdePctImovelRural())){
			salvarPctImovelRural(pessoaJuridicaPct.getIdePctImovelRural()); 
		}
		
		if(Util.isNullOuVazio(pessoaJuridicaPct.getIdePessoaJuridica())){
			pctImovelRuralFacade.salvarPessoaJuridica(pessoaJuridicaPct.getIdePessoaJuridica());
			
			if (Util.isNullOuVazio(pessoaJuridicaPct.getIdePessoaFisicaRepresentanteLegal().getIdePessoaFisica())) {
				pctImovelRuralFacade.salvarPessoaFisicaRepresentante(pessoaJuridicaPct.getIdePessoaFisicaRepresentanteLegal());
			}
			
			RepresentanteLegal representanteLegal = new RepresentanteLegal();
			
			representanteLegal.setIdePessoaFisica(pessoaJuridicaPct.getIdePessoaFisicaRepresentanteLegal());
			representanteLegal.setIdePessoaJuridica(pessoaJuridicaPct.getIdePessoaJuridica());
			representanteLegal.setDtcInicio(new Date());
			representanteLegal.setIndExcluido(false);
			representanteLegal.setDscCaminhoRepresentacao("caminho");
			representanteLegal.setIndAssinaturaObrigatoria(false);
			
			pctImovelRuralFacade.salvarRepresentanteLegal(representanteLegal);
			pctImovelRuralFacade.salvarTelefone(pessoaJuridicaPct);
		}
		
		
		pctImovelRuralFacade.salvarPessoaJuridicarPct(pessoaJuridicaPct);
	}
	
	public List<PessoaJuridicaPct> listarPessoaJuridicaByPct(PctImovelRural pctImovelRural) throws Exception{
		return pctImovelRuralFacade.listarPessoaJuridicaByPct(pctImovelRural);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaJuridicaPct(PessoaJuridicaPct pessoaJuridicaPct) throws Exception{
		pctImovelRuralFacade.excluirPessoaJuridicaPct(pessoaJuridicaPct);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Telefone buscarTelefonesPorPessoa(PessoaFisica pessoaFisica) throws Exception {
		return pctImovelRuralFacade.buscarTelefonesPorPessoa(pessoaFisica);
	}
	
	public Municipio filtrarMunicipioById(Municipio municipio) throws Exception {
		return municipioService.filtrarMunicipioById(municipio);
	}
	
	public boolean isPCT(ImovelRural imovelRural) throws Exception{
		return pctImovelRuralFacade.isPCT(imovelRural);
	}
	
	public LocalizacaoGeografica buscarLocalizacaoGeograficaPCT(ImovelRural imovelRural) throws Exception{
		return localizacaoGeograficaService.buscarPctLocalizacaoGeografica(imovelRural);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void inativarImovel(Imovel imovel) {
		imovelRuralService.inativar(imovel);
	}
	
	public Integer getIdImovelRuralDesbloqueio(ImovelRural imovelRural) {
		return this.desbloqueioService.getImovelRural(imovelRural);
	}

}
