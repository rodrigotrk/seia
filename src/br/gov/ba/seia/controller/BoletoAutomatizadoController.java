package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Bioma;
import br.gov.ba.seia.entity.BiomaRequerimento;
import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.Classe;
import br.gov.ba.seia.entity.DetalhamentoBoleto;
import br.gov.ba.seia.entity.DetalhamentoBoletoFinalidade;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.EnquadramentoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Fauna;
import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.FlorestalCaracteristicaFlorestaProducao;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.MotivoIsencaoBoleto;
import br.gov.ba.seia.entity.ObjetoAlteracao;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.ParametroCalculo;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.PotencialPoluicao;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.CaracteristicaFlorestaProducaoEnum;
import br.gov.ba.seia.enumerator.FaseEmpreendimentoEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.ObjetoAlteracaoEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.PorteEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.exception.SeiaTramitacaoRequerimentoRuntimeException;
import br.gov.ba.seia.facade.BiomaRequerimentoServiceFacade;
import br.gov.ba.seia.facade.BoletoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.BiomaService;
import br.gov.ba.seia.service.BoletoDaeHistoricoService;
import br.gov.ba.seia.service.BoletoDaeRequerimentoService;
import br.gov.ba.seia.service.BoletoPagamentoHistoricoService;
import br.gov.ba.seia.service.BoletoService;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.ClasseService;
import br.gov.ba.seia.service.EmpreendimentoRequerimentoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.FaunaService;
import br.gov.ba.seia.service.FceLocalizacaoGeograficaService;
import br.gov.ba.seia.service.FlorestalService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.LicencaService;
import br.gov.ba.seia.service.MotivoIsencaoBoletoService;
import br.gov.ba.seia.service.ObjetoAlteracaoService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaService;
import br.gov.ba.seia.service.ParametroCalculoService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.PorteService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("boletoAutomatizadoController")
@ViewScoped
public class BoletoAutomatizadoController {
	
	@EJB
	private BoletoServiceFacade boletoServiceFacade;
	
	@EJB
	private BoletoService boletoService;
	
	@EJB
	private PessoaService pessoaService;
	
	@EJB
	private BoletoPagamentoHistoricoService boletoPagamentoHistoricoService;
	
	@EJB
	private BoletoDaeHistoricoService boletoDaeHistoricoService;
	
	@EJB
	private BoletoDaeRequerimentoService boletoDaeService;

	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private EnquadramentoService enquadramentoService;
	
	@EJB
	private MotivoIsencaoBoletoService motivoIsencaoBoletoService;
	
	@EJB
	private ParametroService parametroService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private ParametroCalculoService parametroCalculoService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;
	
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;

	@EJB
	private ClasseService classeService;
	
	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private LicencaService licencaService;
	
	@EJB
	private ObjetoAlteracaoService objetoAlteracaoService;
	
	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;
	
	@EJB
	private CertificadoService certificadoService;
	
	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	
	@EJB
	private FceLocalizacaoGeograficaService fceLocalizacaoGeograficaService;
	
	@EJB
	private OutorgaService outorgaService;
	
	@EJB
	private FaunaService faunaService;
	
	@EJB
	private BiomaRequerimentoServiceFacade biomaRequerimentoServiceFacade;
	@EJB
	private BiomaService biomaService;
	
	@EJB
	private PorteService porteService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	@EJB
	private EmpreendimentoRequerimentoService empreendimentoRequerimentoService;
	@Inject
	private AbaFinalizarRequerimentoController abaFinalizarRequerimentoController;
	@EJB
	private GerenciaArquivoService gerenciaArquivoservice;
	@EJB
	private FlorestalService florestalService;
	
	private Requerimento requerimento;

	private BoletoPagamentoRequerimento boleto;

	private BoletoDaeRequerimento certificado; 
	
	private BoletoDaeRequerimento vistoria;

	private DetalhamentoBoleto detalhamentoBoletoACalcular;

	private Classe classe;

	private Collection<MotivoIsencaoBoleto> motivosIsencao;

	private Collection<DetalhamentoBoleto> detalhamentosBoleto;

	private Integer diasVencimento;

	private boolean existeDae;
	
	private boolean existeBoleto;
	
	private boolean faltaParametros;
	
	private boolean isValorFixo;
	
	private String strMsgAlerta;

	private Collection<Tipologia> atividadesLicenca;
	
	private ParametroCalculo parametroCalculoSelecionado;
		
	private Collection<EnquadramentoAtoAmbiental> enquadramentoAtosAmbientaisDoReq;
	
	private EmpreendimentoRequerimento empreendimentoRequerimento;
	
	private BigDecimal valorTotalDosOutrosDetalhamentos;
	
	private Double valorTotalCertificadoDAE;
	
	private Double valorTotalVistoriaDAE;
	
	private Double valorTotalAreaBioma;

	private TramitacaoRequerimento tramitacaoRequerimento;
	
	private Collection<BiomaRequerimento> listaBiomaRequerimento;
	
	private Collection<BiomaRequerimento> listaBiomaRequerimentoDetalhamento;
	
	private Collection<OutorgaLocalizacaoGeografica> listaCaptacaoSubterranea;
	private Collection<OutorgaLocalizacaoGeografica> listaCaptacaoSuperficial;
	private Collection<OutorgaLocalizacaoGeografica> listaLancamentoEfluente;
	private String dataHoje;
	private Bioma biomaPredominante;

	private boolean isBoletoApe;

	private Collection<MotivoIsencaoBoleto> motivoIsencaoVistoriaDae;

	@PostConstruct
	public void init() {}

	public void load(Requerimento req) {
		try {
			abaFinalizarRequerimentoController.init();
			inicializarVariaveis();
			requerimento = this.requerimentoService.carregarDadosBasicos(req.getIdeRequerimento());
			criarDetalhamentos(requerimento);
			carregarAtosAmbientais(requerimento);
			carregarListaBiomaRequerimento(requerimento);
			carregarOuCriarClasse(requerimento);
			carregarClasse(requerimento);
			
			calcular();
			calcularDAE();
			
			verificaFaltaParametroParaAlgumAto();
			atividadesLicenca = new ArrayList<Tipologia>();
			carregarAtividadesDoRequerimento(requerimento);
			setTramitacaoRequerimento(tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(requerimento.getIdeRequerimento()));
			carregarDadosOutorga();
			RequestContext.getCurrentInstance().addPartialUpdateTarget("boleto");
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void loadDtrp(Requerimento req) {
		try {
			inicializarVariaveis();
			requerimento = this.requerimentoService.carregarDadosBasicos(req.getIdeRequerimento());
			criarDetalhamentos(requerimento);
			carregarAtosAmbientais(requerimento);
			
			calcular();
			
			verificaFaltaParametroParaAlgumAto();
			atividadesLicenca = new ArrayList<Tipologia>();
			carregarAtividadesDoRequerimento(requerimento);
			setTramitacaoRequerimento(tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(requerimento.getIdeRequerimento()));
			carregarDadosOutorga();
			RequestContext.getCurrentInstance().addPartialUpdateTarget("boleto");
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarDadosOutorga() {
		try{
			listaCaptacaoSubterranea = outorgaLocalizacaoGeograficaService
					.listarOutorgaLocalizacaoGeografica(requerimento.getIdeRequerimento(), ModalidadeOutorgaEnum.CAPTACAO, TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA);
			listaCaptacaoSuperficial = outorgaLocalizacaoGeograficaService
					.listarOutorgaLocalizacaoGeografica(requerimento.getIdeRequerimento(), ModalidadeOutorgaEnum.CAPTACAO, TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL);
			listaLancamentoEfluente = outorgaLocalizacaoGeograficaService
					.listarOutorgaLocalizacaoGeografica(requerimento.getIdeRequerimento(), ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES, null);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isRenderedOutorgaRecusosHidricos() {
		if(Util.isNull(requerimento)==false) {
			for(AtoAmbiental aa :  requerimento.getAtosAmbientais()) {
				if(aa.getIdeAtoAmbiental().equals(AtoAmbientalEnum.OUTORGA.getId())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void calcularDAE() throws Exception {
		boolean temRFP = false;
		boolean isRcfp = false;
		boolean isARL = false;
		for(DetalhamentoBoleto detalhamentoBoleto : detalhamentosBoleto) {
			if(detalhamentoBoleto.isDae()) {
				if(!isARL){
					isARL = detalhamentoBoleto.getIdeAtoAmbiental().isARL();
				}
				Collection<ParametroCalculo> parametros = parametroCalculoService.listarParametrosDAE(detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental());
				for (ParametroCalculo parametroCalculo : parametros) {
					if (!parametroCalculo.isExigeCalculo()) {
						BigDecimal valorTaxa = parametroCalculo.getValorTaxa();
						detalhamentoBoleto.setValorCertificado(valorTaxa);
						if(!temRFP) {
							temRFP = detalhamentoBoleto.getIdeAtoAmbiental().isRfp();
						}
						if(!isRcfp){
							isRcfp = detalhamentoBoleto.getIdeAtoAmbiental().isRcfp();
						}
					}
				}
			}
		}
		
		valorTotalCertificadoDAE = calcularCertificado();
		
		if((!Util.isNullOuVazio(listaBiomaRequerimento)|| isARL) && !temRFP && !isRcfp) {
			valorTotalVistoriaDAE = calcularVistoria();
		}
	}
	
	public boolean isRenderedCalcularTaxaDAE(DetalhamentoBoleto detalhamentoBoleto) {
		
		List<AtoAmbiental> lista = Arrays.asList(new AtoAmbiental[] {
				new AtoAmbiental(AtoAmbientalEnum.ANUENCIA.getId()),
				new AtoAmbiental(AtoAmbientalEnum.RVFR.getId())
		});
		
		return detalhamentoBoleto.isDae() && lista.contains(detalhamentoBoleto.getIdeAtoAmbiental());
	}
	
	private void carregarListaBiomaRequerimento(Requerimento requerimento) throws Exception {
		try {
			//AJUSTE TEMPORÃ�RIO SOLICITADO POR DANILO COSTA
			biomaRequerimentoServiceFacade.removerPorRequerimento(requerimento.getIdeRequerimento());
			
			listaBiomaRequerimento = biomaRequerimentoServiceFacade.carregarListaBiomaRequerimento(requerimento);
			atualizarListaDetalhamentoBiomaRequerimento();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void atualizarAreaBiomaRequerimento(Collection<DetalhamentoBoleto> listaDetalhamentoBoleto, Collection<BiomaRequerimento> listaBiomaRequerimento) {
		try {
			
			this.listaBiomaRequerimento = listaBiomaRequerimento;
			this.detalhamentosBoleto = listaDetalhamentoBoleto;
			atualizarListaDetalhamentoBiomaRequerimento();
			calcularDAE();
			RequestContext.getCurrentInstance().addPartialUpdateTarget("boleto");
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}


	private void atualizarListaDetalhamentoBiomaRequerimento() {
		if(Util.isNullOuVazio(listaBiomaRequerimento)==false) {
			listaBiomaRequerimentoDetalhamento = biomaRequerimentoServiceFacade.somarAreasBiomaRequerimento(listaBiomaRequerimento);
			valorTotalAreaBioma = biomaRequerimentoServiceFacade.calcularValorTotalAreaBioma(listaBiomaRequerimentoDetalhamento);
		}
	}
	
	public boolean isRenderedPnlDetalhamentoBioma() {
		return !Util.isNullOuVazio(listaBiomaRequerimentoDetalhamento);
	}
	
	public Bioma retornarBiomaPredominante(){
		if(Util.isNullOuVazio(listaBiomaRequerimento)==false) {
			BiomaRequerimento predominante = null;
			for(BiomaRequerimento br : listaBiomaRequerimento) {
				if(predominante == null || (predominante != null && predominante.getValArea() < br.getValArea())) {
					predominante = br;
				}
			}
			return predominante.getIdeBioma();
		}
		else{
			List<Bioma> listarBiomaEmpreendimento = biomaService.listarBiomaPor(requerimento.getUltimoEmpreendimento().getIdeLocalizacaoGeografica());
			Bioma predominante = null;
			for(Bioma br : listarBiomaEmpreendimento) {
				if(predominante == null || (predominante != null && predominante.getValArea() < br.getValArea())) {
					predominante = br;
				}
			}
			return predominante;
		}
	}

	
	/**
	 * @author Alexandre Queiroz
	 * @since 03/02/2015
	 * @throws Exception
	 */
	private void inicializarVariaveis() throws Exception {
		existeDae = false;
		existeBoleto = false;
		faltaParametros = false;
		boleto = new BoletoPagamentoRequerimento();
		boleto.setIndIsento(false);
		boleto.setIndBoletoGeradoManualmente(false);
		Calendar c = this.calcularVencimento();
		boleto.setDtcVencimento(c.getTime());

		vistoria = new BoletoDaeRequerimento();
		certificado = new BoletoDaeRequerimento();

		detalhamentosBoleto = new ArrayList<DetalhamentoBoleto>();
		detalhamentoBoletoACalcular = new DetalhamentoBoleto();
		valorTotalVistoriaDAE = new Double(0.0); 
		valorTotalCertificadoDAE = new Double(0.0);
		listaBiomaRequerimento = null;
		listaBiomaRequerimentoDetalhamento = null;
		motivosIsencao = this.motivoIsencaoBoletoService.listarMotivosInsencaoDoRequerimentoAtivos();
		
		obterMotivoIsencaoVistoriaDae(motivosIsencao);
		
		isBoletoApe = false;
	}

	public void obterMotivoIsencaoVistoriaDae(Collection<MotivoIsencaoBoleto> isencaoBoletos) {
		List<MotivoIsencaoBoleto> isencaoBoletosTemp = new ArrayList<MotivoIsencaoBoleto>();
		for (MotivoIsencaoBoleto motivoIsencaoBoleto : isencaoBoletos) {
			if(motivoIsencaoBoleto.getIdeMotivoIsencaoBoleto().equals(5)){
				isencaoBoletosTemp.add(motivoIsencaoBoleto);
			}
		}
		setMotivoIsencaoVistoriaDae(isencaoBoletosTemp);
	}
	
	public void incluirParametroCalculo() {
		limparParametroCalculoSelecionado();
		RequestContext.getCurrentInstance().execute("dialogIncluirParametroCalculo.show();");
	}
	
	private void limparParametroCalculoSelecionado() {
		parametroCalculoSelecionado = null;
	}
	
	public boolean getIsRenderedIncluirParametroCalculoBoleto() {
		return isRequerimentoAntigoEnquadradoEmOutorga();
	}
	
	private void carregarAtosAmbientais(Requerimento requerimento) {
		Collection<AtoAmbiental> listaAtoAmbiental = atoAmbientalService.listarAtosEnquadradosByRequerimento(requerimento.getIdeRequerimento());
		requerimento.setAtosAmbientais(listaAtoAmbiental);
	}
	
	private boolean isRequerimentoAntigoEnquadradoEmOutorga() {
		try{
			if(!Util.isNull(requerimento)) {
				if(requerimentoService.isRequerimentoAntigo(requerimento.getIdeRequerimento())) {
					for (AtoAmbiental atoAmbiental : listaAtosOutorgaRequerimentoAntigo()) {
						if(requerimento.getAtosAmbientais().contains(atoAmbiental)) {
							return true;
						}
					}
				}
			}
			return false;
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private List<AtoAmbiental> listaAtosOutorgaRequerimentoAntigo() {
		List<AtoAmbiental> atos = new ArrayList<AtoAmbiental>();
		atos.add(new AtoAmbiental(AtoAmbientalEnum.AOUT.getId()));
		atos.add(new AtoAmbiental(AtoAmbientalEnum.COUT.getId()));
		atos.add(new AtoAmbiental(AtoAmbientalEnum.DOUT.getId()));
		atos.add(new AtoAmbiental(AtoAmbientalEnum.OUTP.getId()));
		atos.add(new AtoAmbiental(AtoAmbientalEnum.OUTORGA.getId()));
		atos.add(new AtoAmbiental(AtoAmbientalEnum.PPV_OUT.getId()));
		atos.add(new AtoAmbiental(AtoAmbientalEnum.ROUT.getId()));
		return atos;
	}
	
	public void removerParametroDetalhamentoBoleto(DetalhamentoBoleto detalhamentoBoleto) throws Exception {
		if(!Util.isNull(detalhamentoBoleto)) {
			detalhamentosBoleto.remove(detalhamentoBoleto);
			calcular();
			JsfUtil.addSuccessMessage("ParÃ¢metro removido com sucesso.");
		}
	}
	
	public boolean isRenderedLinkRemoverParametro(DetalhamentoBoleto detalhamentoBoleto) {
		if(getIsRenderedIncluirParametroCalculoBoleto()) {
			for(ParametroCalculo parametroCalculo : getListaParametroCalculo()) {
				if(parametroCalculo.getIdeAtoAmbiental().equals(detalhamentoBoleto.getIdeAtoAmbiental())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public List<ParametroCalculo> getListaParametroCalculo() {
		
		try{
			
			if(Util.isNull(this.requerimento)) {
				return null;
			}
			
			Map<String,ParametroCalculo> parametrosValidos = new  HashMap<String, ParametroCalculo>();
			
			for(ParametroCalculo parametroCalculo : parametroCalculoService.listarParametrosAtivosPorRequerimento(this.requerimento.getIdeRequerimento(),listaAtosOutorgaRequerimentoAntigo())) {
				parametrosValidos.put(parametroCalculo.getDscAtoTipologiaFinalidade(), parametroCalculo);
			}
			
			return retornarListaParametroCalculoValido(parametrosValidos);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("NÃ£o foi possÃ­vel carregar a lista de paramentros de cÃ¡lculo.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

	private List<ParametroCalculo> retornarListaParametroCalculoValido(Map<String, ParametroCalculo> parametrosValidos) {
		List<ParametroCalculo> parametros = new ArrayList<ParametroCalculo>();
		Iterator<String> it = parametrosValidos.keySet().iterator();
		while (it.hasNext()) {
			String parametroIdentifier = it.next();
			ParametroCalculo parametro = (ParametroCalculo) parametrosValidos.get(parametroIdentifier);				
			if(!Util.isNull(parametro)) {
				parametros.add(parametro);					
			}
		}
		Collections.sort(parametros);
		return  parametros;
	}

	public void verificaFaltaClasseDoEmpreendimento(Integer ideRequerimento) throws Exception {
		if(Util.isNull(this.classe) && !isValorFixo) {
			if(!carregaClasseRequerimentoUnico(ideRequerimento)) {
				strMsgAlerta = "NÃ£o foi possÃ­vel obter a classe do empreendimento desse requerimento. Favor entrar em contato com o administrador do sistema.";
				faltaParametros = true;
				return;
			} else {
				this.empreendimentoRequerimento.setIdeClasse(this.classe);
				requerimentoService.atualizarEmpreendRequerimento(empreendimentoRequerimento);
				faltaParametros = false;
				return;
			}
		}
	}
	
	private Boolean carregaClasseRequerimentoUnico(Integer ideRequerimento) {
	
		PotencialPoluicao potencialPoluicao = null;
		Porte porte = null;
		
		try {
			RequerimentoUnico ru = requerimentoUnicoService.recuperarRequerimentoUnicoPorId(new RequerimentoUnico(ideRequerimento));
			
			if (!Util.isNull(ru)) {
				empreendimentoRequerimento = empreendimentoService.buscarEmpreendimentoRequerimento(requerimento);
				
				RequerimentoTipologia requerimentoTipologia = requerimentoTipologiaService.buscarRequerimentoTipologiaPrincipal(requerimento);
				
				if (!Util.isNullOuVazio(requerimentoTipologia) && !Util.isNullOuVazio(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo())
						&& !Util.isNullOuVazio(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo())
						&& !Util.isNullOuVazio(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdePotencialPoluicao())) {
					
					porte = empreendimentoRequerimento.getIdePorte();
					potencialPoluicao = requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdePotencialPoluicao();
					
					this.classe = classeService.buscarClasseAtividade(porte, potencialPoluicao);
					
					if (!Util.isNull(this.classe)) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return false;
	}

	private void carregarClasse(Requerimento requerimento) throws Exception {
		boolean isLicenca = false;
				
		if(!Util.isNullOuVazio(requerimento.getAtosAmbientais())) {
			for (AtoAmbiental aa : requerimento.getAtosAmbientais()) {
				
				if(aa.getIdeAtoAmbiental().equals(AtoAmbientalEnum.AUTORIZACAO_AMBIENTAL.getId()) && requerimento.getAtosAmbientais().size() == 1) {
					isValorFixo = true;
				}
				
				if(aa.getIdeTipoAto().getIdeTipoAto().equals(TipoAtoEnum.LICENCA.getId())) {
					if(!(aa.getIdeAtoAmbiental().equals(AtoAmbientalEnum.AUTORIZACAO_AMBIENTAL.getId()) ||
							aa.getIdeAtoAmbiental().equals(AtoAmbientalEnum.PRORROGACAO_LICENCA.getId()) ||
							aa.getIdeAtoAmbiental().equals(AtoAmbientalEnum.PRORROGACAO_AUTORIZACAO.getId()))){
						isLicenca = true;
					}
				}
			}
		}
		
		if(isLicenca) {
			
			EmpreendimentoRequerimento empreendimentoRequerimento = this.empreendimentoService.buscarEmpreendimentoRequerimento(requerimento);

			if (!Util.isNull(empreendimentoRequerimento)) {
				this.classe = empreendimentoRequerimento.getIdeClasse();
			}
			
			verificaFaltaClasseDoEmpreendimento(requerimento.getIdeRequerimento());
		}
	}

	private Calendar calcularVencimento() throws Exception {
		this.diasVencimento = this.parametroService.obterValorInt(ParametroEnum.DIAS_VENCIMENTO_BOLETO);

		Calendar c = new GregorianCalendar();
		c.add(Calendar.DATE, this.diasVencimento); 
		return c;
	}
	
	public Boolean isTLAEnquadramentoAto(){
		try {
			if(Util.isNullOuVazio(enquadramentoAtosAmbientaisDoReq) )
				enquadramentoAtosAmbientaisDoReq = this.enquadramentoService.listarEnquadramentoAtoByRequerimento(requerimento.getIdeRequerimento());
			for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramentoAtosAmbientaisDoReq) {
				if(enquadramentoAtoAmbiental.getAtoAmbiental().isTLA()){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}
	
	public Boolean isPerfuracaoPocoEnquadramentoAto(){
		try {
			if(Util.isNullOuVazio(enquadramentoAtosAmbientaisDoReq) )
				enquadramentoAtosAmbientaisDoReq = this.enquadramentoService.listarEnquadramentoAtoByRequerimento(requerimento.getIdeRequerimento());
			for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramentoAtosAmbientaisDoReq) {
				if(enquadramentoAtoAmbiental.getAtoAmbiental().isPerfuracaoDePoco()){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}
	
	public Boolean getDesabiliatarInputValorTotalBoleto(){
		if(isPerfuracaoPocoEnquadramentoAto() || isTLAEnquadramentoAto())
			return false;
		else
			return true;
	}
	
	private void criarDetalhamentos(Requerimento requerimento) throws Exception {
		BigDecimal vazaoTotalSuperficial = new BigDecimal(0);
		BigDecimal vazaoTotalSubterranea = new BigDecimal(0);
		
		for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramentoService.listarEnquadramentoAtoByRequerimento(requerimento.getIdeRequerimento())) {
			
			List<OutorgaLocalizacaoGeografica> listOLG = null;
				
			if(!Util.isNullOuVazio(enquadramentoAtoAmbiental.getTipologia())) {
				
				if(enquadramentoAtoAmbiental.getTipologia().isCaptacaoSubterranea()) {

					listOLG = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA, requerimento);
					
					if(enquadramentoAtoAmbiental.getAtoAmbiental().isDispensaOutorga()) {
						vazaoTotalSubterranea = gerarDetalhamentoDispensaOutorga(listOLG, enquadramentoAtoAmbiental);
					}
				} else if(enquadramentoAtoAmbiental.getTipologia().isCaptacaoSuperficial()){
					listOLG = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL, requerimento);
					
					if(enquadramentoAtoAmbiental.getAtoAmbiental().isDispensaOutorga()) {
						vazaoTotalSuperficial = gerarDetalhamentoDispensaOutorga(listOLG, enquadramentoAtoAmbiental);
					} 
				} else if(enquadramentoAtoAmbiental.getAtoAmbiental().isDispensaOutorga() && enquadramentoAtoAmbiental.getTipologia().isIntervencao()) {
					gerarDetalhamento(enquadramentoAtoAmbiental);
				}
				
				if(enquadramentoAtoAmbiental.getAtoAmbiental().isOutorga() || enquadramentoAtoAmbiental.getAtoAmbiental().isOutorgaPreventiva()){
					
					if(enquadramentoAtoAmbiental.getTipologia().isCaptacaoSubterranea()){
						gerarDetalhamentoParaCaptacao(enquadramentoAtoAmbiental, listOLG, vazaoTotalSubterranea);
					} else if(enquadramentoAtoAmbiental.getTipologia().isCaptacaoSuperficial()){
						gerarDetalhamentoParaCaptacao(enquadramentoAtoAmbiental, listOLG, vazaoTotalSuperficial);
					} else {
						gerarDetalhamento(enquadramentoAtoAmbiental);
					}
				} else if(!enquadramentoAtoAmbiental.getAtoAmbiental().isDispensaOutorga()) {
					gerarDetalhamento(enquadramentoAtoAmbiental);
				}
			} else if(!enquadramentoAtoAmbiental.getAtoAmbiental().isDispensaOutorga()) {
				gerarDetalhamento(enquadramentoAtoAmbiental);
			}
		}
	}
	
	private BigDecimal gerarDetalhamentoDispensaOutorga(List<OutorgaLocalizacaoGeografica> lista, EnquadramentoAtoAmbiental eaa) {
		
		BigDecimal menorVazao = null;
		BigDecimal vazaoTotalDispensa= new BigDecimal(0);
		
		DetalhamentoBoleto detalhe = new DetalhamentoBoleto(new AtoAmbiental(AtoAmbientalEnum.OUTORGA.getId()), eaa.getTipologia());
		if(isOutorga(eaa.getAtoAmbiental().getIdeAtoAmbiental()) && !CollectionUtils.isEmpty(eaa.getEnquadramentoFinalidadeUsoAguaCollection())){
			gerarDetalhamentoFinalidadeUsoAgua(eaa.getEnquadramentoFinalidadeUsoAguaCollection(), detalhe);
		}
		
		Collection<ParametroCalculo> pcs = parametroCalculoService.listar(detalhe, null, null);
		
		for (ParametroCalculo pc : pcs) {
			if(menorVazao == null) {
				menorVazao = pc.getVazaoMinima();
			} else if(!Util.isNullOuVazio(pc.getVazaoMinima()) && pc.getVazaoMinima().compareTo(menorVazao) == -1) {
				menorVazao = pc.getVazaoMinima();
			}
		}
		
		for (OutorgaLocalizacaoGeografica olg : lista) {

			if(olg.getNumVazao() != null && (olg.getNumVazao().compareTo(menorVazao) == -1)) {

				vazaoTotalDispensa = vazaoTotalDispensa.add(olg.getNumVazao());
			}
		}
		
		DetalhamentoBoleto detalhamentoBoleto = new DetalhamentoBoleto(eaa.getAtoAmbiental(), eaa.getTipologia());
		detalhamentoBoleto.setNumVazao(vazaoTotalDispensa);
		detalhamentoBoleto.setDetalhamentoBoletoFinalidadeCollection(detalhe.getDetalhamentoBoletoFinalidadeCollection());
		detalhamentosBoleto.add(detalhamentoBoleto);
		
		return vazaoTotalDispensa;
	}
	
	private void gerarDetalhamentoParaCaptacao(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental, List<OutorgaLocalizacaoGeografica> listOLG, BigDecimal vazaoTotalDispensa) {
		Collection<ParametroCalculo> pcs = null;
		
		BigDecimal menorVazao =  null;
		
		if (!Util.isNullOuVazio(listOLG)) {
			
			DetalhamentoBoleto detalhamentoBoleto = new DetalhamentoBoleto();
			
			detalhamentoBoleto.setIdeTipologia(enquadramentoAtoAmbiental.getTipologia());
			detalhamentoBoleto.setIdeAtoAmbiental(enquadramentoAtoAmbiental.getAtoAmbiental());
			detalhamentoBoleto.setNumVazao((getSomaVazao(listOLG)).subtract(vazaoTotalDispensa));
			detalhamentoBoleto.setVazoesCaptacao(getVazoesCaptacao(listOLG));

			try {
				pcs = parametroCalculoService.listar(detalhamentoBoleto, null, null);
			} catch (Exception e) {
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
			
			for (ParametroCalculo pc : pcs) {
				if(menorVazao == null) {
					menorVazao = pc.getVazaoMinima();
				} else if(!Util.isNullOuVazio(pc.getVazaoMinima()) && pc.getVazaoMinima().compareTo(menorVazao) == -1) {
					menorVazao = pc.getVazaoMinima();
				}
			}
			
			for (OutorgaLocalizacaoGeografica olg : listOLG) {
				
				if(!enquadramentoAtoAmbiental.getAtoAmbiental().isDispensaOutorga() && (olg.getNumVazao().compareTo(menorVazao) > 0)){
					for (OutorgaLocalizacaoGeograficaFinalidade olgf : olg.getOutorgaLocalizacaoGeograficaFinalidadeCollection()) {
						if(!olgf.getIndExcluido()) {
							DetalhamentoBoletoFinalidade dbf = new DetalhamentoBoletoFinalidade();
							dbf.setIdeDetalhamentoBoleto(detalhamentoBoleto);
							dbf.setIdeTipoFinalidadeUsoAgua(olgf.getIdeTipoFinalidadeUsoAgua());
							
							if(dbf.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.DESSEDENTACAO_ANIMAL.getId())) {
								dbf.setNumAreaDessedentacaoAnimal(olg.getNumAreaDessedentacaoAnimal());
							}
							
							if(dbf.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getId())) {
								dbf.setIndAbastecimentoEmDistritoIndustrial(olg.getIndAbastecimentoEmDistritoIndustrial());
							}
							
							if(dbf.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.IRRIGACAO.getId())) {
								dbf.setNumAreaIrrigada(olg.getNumAreaIrrigadaCaptacao());
							}
							
							if(dbf.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.PULVERIZACAO_AGRICOLA.getId())) {
								dbf.setNumAreaPulverizada(olg.getNumAreaPulverizada());
							}
							
							if(Util.isNullOuVazio(detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection())) {
								detalhamentoBoleto.setDetalhamentoBoletoFinalidadeCollection(new ArrayList<DetalhamentoBoletoFinalidade>());
								detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection().add(dbf);
								
							} else if(!detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection().contains(dbf)) {
								detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection().add(dbf);
							}
						}
					}
				}
			}
			
			detalhamentosBoleto.add(detalhamentoBoleto);
		}
	}
	
	private Collection<BigDecimal> getVazoesCaptacao(List<OutorgaLocalizacaoGeografica> OLG) {
		Collection<BigDecimal> listVazoesCaptacao = new ArrayList<BigDecimal>();
		
		for (OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : OLG) {
			listVazoesCaptacao.add(outorgaLocalizacaoGeografica.getNumVazao());
		}
				
		return listVazoesCaptacao;
	}

	public BigDecimal getSomaVazao(List<OutorgaLocalizacaoGeografica> olg){
		BigDecimal vazao = new BigDecimal(0);
		
		for (OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : olg) {
			
			if(outorgaLocalizacaoGeografica.getNumVazao() != null) {
				vazao = vazao.add(outorgaLocalizacaoGeografica.getNumVazao());
			}
		}
		
		return vazao;
	}
	
	private void gerarDetalhamento(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental) {
		DetalhamentoBoleto detalhamentoBoleto = new DetalhamentoBoleto();
		Collection<Tipologia> tipologiasLicenca;
		Tipologia tipologia = null;
		final Double vazaoDispensaIntervencao = 200000.0;
		
		try {
			tipologiasLicenca = requerimentoTipologiaService.buscarTipologiasLicenca(requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("NÃ£o foi possÃ­vel carregar a lista de paramentros de cÃ¡lculo.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
		if (!Util.isNullOuVazio(tipologiasLicenca)){
			for (Tipologia pTipologia : tipologiasLicenca) {
				DetalhamentoBoleto detalhamentoBoletoTemp = null;
				if(enquadramentoAtoAmbiental.getAtoAmbiental().isAPE()){
					detalhamentoBoletoTemp = new DetalhamentoBoleto();
					detalhamentoBoletoTemp.setIdeTipologia(pTipologia);
					detalhamentoBoletoTemp.setIdeAtoAmbiental(enquadramentoAtoAmbiental.getAtoAmbiental());
				} else {
					if (Util.isNullOuVazio(enquadramentoAtoAmbiental.getTipologia()) || pTipologia.getIdeTipologia().equals(enquadramentoAtoAmbiental.getTipologia().getIdeTipologia())) {
						tipologia = new Tipologia(pTipologia);
						break;
					}
				}
				if (!Util.isNullOuVazio(detalhamentoBoletoTemp)){
					detalhamentosBoleto.add(detalhamentoBoletoTemp);
				}
			}
			if(detalhamentosBoleto.size() > 1){
				Collection<DetalhamentoBoleto> detBoletosTemp = new ArrayList<DetalhamentoBoleto>();
				for (DetalhamentoBoleto detBoleto : detalhamentosBoleto) {
					if(!detBoletosTemp.contains(detBoleto) && !Util.isNullOuVazio(detBoleto)){
						detBoletosTemp.add(detBoleto);
					}
				}
				detalhamentosBoleto = detBoletosTemp;
			}
		}

		if (enquadramentoAtoAmbiental.getAtoAmbiental().getIdeTipoAto().getIdeTipoAto().equals(TipoAtoEnum.LICENCA.getId())){
			detalhamentoBoleto.setIdeTipologia(tipologia);
		
		} else {
			detalhamentoBoleto.setIdeTipologia(enquadramentoAtoAmbiental.getTipologia());
		}
		
		detalhamentoBoleto.setIdeAtoAmbiental(enquadramentoAtoAmbiental.getAtoAmbiental());
		
		Collection<AtoAmbiental> listaAtosEspeciais = Arrays.asList(new AtoAmbiental[] {
				new AtoAmbiental(AtoAmbientalEnum.ANUENCIA.getId()),
				new AtoAmbiental(AtoAmbientalEnum.RVFR.getId())
		});
		
		if(listaAtosEspeciais.contains(enquadramentoAtoAmbiental.getAtoAmbiental())) {
			detalhamentoBoleto.setExisteArea(true);
		}
		
		try {
			if(detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental() != AtoAmbientalEnum.DOUT.getId() && !Util.isNullOuVazio(detalhamentoBoleto.getIdeTipologia()) 
				&& (detalhamentoBoleto.getIdeTipologia().isLancamentoEfluentes() || detalhamentoBoleto.getIdeTipologia().isIntervencao())) {
				
				List<OutorgaLocalizacaoGeografica> listOLG = null;
				
				if(detalhamentoBoleto.getIdeTipologia().isIntervencao()) {
					listOLG = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByModalidadeOutorgaRequerimento(
						ModalidadeOutorgaEnum.INTERVENCAO, requerimento);
					
				} else if(detalhamentoBoleto.getIdeTipologia().isLancamentoEfluentes()) {
					listOLG = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByModalidadeOutorgaRequerimento(
						ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES, requerimento);
				}
					
				if(!Util.isNullOuVazio(listOLG)) {
					for (OutorgaLocalizacaoGeografica olg : listOLG) {
						
						if(detalhamentoBoleto.getIdeTipologia().isLancamentoEfluentes()) {
							for (OutorgaLocalizacaoGeograficaFinalidade olgf : olg.getOutorgaLocalizacaoGeograficaFinalidadeCollection()) {
								DetalhamentoBoletoFinalidade dbf = new DetalhamentoBoletoFinalidade(detalhamentoBoleto);
								dbf.setIdeTipoFinalidadeUsoAgua(olgf.getIdeTipoFinalidadeUsoAgua());
								
								if(Util.isNullOuVazio(detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection())) {
									detalhamentoBoleto.setDetalhamentoBoletoFinalidadeCollection(new ArrayList<DetalhamentoBoletoFinalidade>());
									detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection().add(dbf);
									
								} else if(!detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection().contains(dbf)) {
									detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection().add(dbf);
								}
							}
						} else if(detalhamentoBoleto.getIdeTipologia().isIntervencao() && !Util.isNullOuVazio(olg.getNumVolumeMaximoAcumulado())
								&& olg.getNumVolumeMaximoAcumulado().doubleValue() > vazaoDispensaIntervencao) {
							
							DetalhamentoBoletoFinalidade dbf = new DetalhamentoBoletoFinalidade(detalhamentoBoleto);
							dbf.setIdeTipoFinalidadeUsoAgua(olg.getTipoIntervencao().getIdeTipoFinalidadeUsoAgua());
							
							if(Util.isNullOuVazio(detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection())) {
								detalhamentoBoleto.setDetalhamentoBoletoFinalidadeCollection(new ArrayList<DetalhamentoBoletoFinalidade>());
								detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection().add(dbf);
								
							} else if(!detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection().contains(dbf)) {
								detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection().add(dbf);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		if(!enquadramentoAtoAmbiental.getAtoAmbiental().isAPE()){
			detalhamentosBoleto.add(detalhamentoBoleto);
		}
	}
	
	public void adicionarParametro() throws Exception {
		if(Util.isNull(parametroCalculoSelecionado)){
			JsfUtil.addWarnMessage("Nenhum parÃ¢metro foi selecionado.");
		}
		else{
			gerarDetalhamentoBoletoPorParamentroSelecionado(parametroCalculoSelecionado);
			calcular();
			RequestContext.getCurrentInstance().execute("dialogIncluirParametroCalculo.hide();");
		}
	}
	
	private void gerarDetalhamentoBoletoPorParamentroSelecionado(ParametroCalculo parametroCalculo) {

		DetalhamentoBoleto detalhamentoBoleto = new DetalhamentoBoleto();
		detalhamentoBoleto.setIdeTipologia(parametroCalculo.getIdeTipologia());
		detalhamentoBoleto.setIdeAtoAmbiental(parametroCalculo.getIdeAtoAmbiental());
		detalhamentoBoleto.setIdeTipoFinalidadeUsoAgua(parametroCalculo.getIdeTipoFinalidadeUsoAgua());
		
		detalhamentosBoleto.add(detalhamentoBoleto);
		
	}

	public void isentarPagamento() {
		try {
			if (this.boleto.getIndIsento()) {
				this.boleto.setValBoleto(new BigDecimal(0));
			} else {
				this.boleto.setIdeMotivoIsencaoBoleto(null);
				this.calcular();
			}
			
			if (this.certificado.isIndIsento()) {
				this.certificado.setVlrTotalVistoria(BigDecimal.ZERO);
				this.setValorTotalVistoriaDAE(0D);
			} else {
				this.certificado.setIdeMotivoIsencaoBoleto(null);
				this.calcularDAE();
			}
			
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void verificaFaltaParametroParaAlgumAto(){
		try {
			if(!Util.isNullOuVazio(detalhamentosBoleto) && !isAOUT(this.detalhamentosBoleto)) {
				
				if(isAlteracaoLicencaComUnicoObjeto(ObjetoAlteracaoEnum.SUBSTITUICAO)) {
					List<Tipologia> tipologiasRequerimento = (List<Tipologia>) requerimentoTipologiaService.buscarTipologias(requerimento);
					
					if (Util.isNullOuVazio(tipologiasRequerimento)) {
						strMsgAlerta = "NÃ£o foi possÃ­vel encontrar parÃ¢metro para um dos atos ambientais desse requerimento. Favor entrar em contato com o administrador do sistema.";
						faltaParametros = true;
						return;
					}
				} 
					
				for (DetalhamentoBoleto detalhamentoBoleto : this.detalhamentosBoleto) {
					
					Collection<ParametroCalculo> parametros = new ArrayList<ParametroCalculo>();
					
					if(!Util.isNullOuVazio(detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection())) {
						for (DetalhamentoBoletoFinalidade dbf : detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection()) {
							parametros.addAll(parametroCalculoService.listarPorDetalhamentoBoletoFinalidade(dbf));
						}
					}
					
					if (Util.isNullOuVazio(parametros)) {
						parametros = this.parametroCalculoService.listar(detalhamentoBoleto, classe, detalhamentoBoleto.getNumVazao());
					
						if (Util.isNullOuVazio(parametros)) {
							//#10176 - busca sem tipologia
							detalhamentoBoleto.setIdeTipologia(null);
							parametros = this.parametroCalculoService.listar(detalhamentoBoleto, classe, detalhamentoBoleto.getNumVazao());
							
							if(detalhamentoBoleto.getIdeAtoAmbiental().isAPE()){
								boleto.setIndBoletoGeradoManualmente(true);
								setBoletoApe(true);
								break;
							}

							if (Util.isNullOuVazio(parametros) && !AtoAmbientalEnum.LR.getId().equals(detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental()) && !AtoAmbientalEnum.REAPPO.getId().equals(detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental())) {
								strMsgAlerta = "NÃ£o foi possÃ­vel encontrar parÃ¢metro para um dos atos ambientais desse requerimento. Favor entrar em contato com o administrador do sistema.";
								faltaParametros = true;
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			strMsgAlerta = "NÃ£o foi possÃ­vel encontrar parÃ¢metro para um dos atos ambientais desse requerimento. Favor entrar em contato com o administrador do sistema.";
			faltaParametros = true;
		}
	}
	
	public boolean isAOUT(Collection<DetalhamentoBoleto> detalhamentoBoleto){
		Boolean retorno = false;
		for (DetalhamentoBoleto boleto : detalhamentoBoleto) {
			if(isAOUT(boleto)){
				retorno =true;
			}
		}
		
		return retorno;
	}
	

	private void gerarDetalhamentoFinalidadeUsoAgua(Collection<EnquadramentoFinalidadeUsoAgua> enquadramentoFinalidades, DetalhamentoBoleto detalhe) {

		List<DetalhamentoBoletoFinalidade> detalhamentoFinalidades = new ArrayList<DetalhamentoBoletoFinalidade>();
		for(EnquadramentoFinalidadeUsoAgua finalidade : enquadramentoFinalidades){
			DetalhamentoBoletoFinalidade detFinal = new DetalhamentoBoletoFinalidade();
			detFinal.setIdeDetalhamentoBoleto(detalhe);
			detFinal.setIdeTipoFinalidadeUsoAgua(finalidade.getIdeTipoFinalidadeUsoAgua());
			detalhamentoFinalidades.add(detFinal);
		}
		
		detalhe.setDetalhamentoBoletoFinalidadeCollection(detalhamentoFinalidades);
	}
	
	public boolean isAOUT(DetalhamentoBoleto detalhamentoBoleto){
		return detalhamentoBoleto.getIdeAtoAmbiental().isAOUT() && (detalhamentoBoleto.getIdeTipologia().isCaptacaoSubterranea() || detalhamentoBoleto.getIdeTipologia().isCaptacaoSuperficial());
	}
	
	public void calcular() throws Exception {
		
		BigDecimal valorTotal = new BigDecimal(0);
		
		for (DetalhamentoBoleto detalhamentoBoleto : this.detalhamentosBoleto) {
			
			//Obtem o valor da taxa
			Collection<ParametroCalculo> parametros = obtemParametros(detalhamentoBoleto);
			
			if (Util.isNullOuVazio(parametros) && Util.isNull(detalhamentoBoleto.getValor()) && !AtoAmbientalEnum.LR.getId().equals(detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental()) && !AtoAmbientalEnum.REAPPO.getId().equals(detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental())) {
				
				if(isAOUT(detalhamentoBoleto)){					
					ParametroCalculo parametroCalculo = new ParametroCalculo();
					parametroCalculo.setIndBoleto(true);
					parametros.add(parametroCalculo);
				}else{
					continue;
				}
				
				detalhamentoBoleto.setExigeCalculo(true);
			}
			
			verificaDesmembramentoLA(parametros, detalhamentoBoleto.getIdeAtoAmbiental().isLA());
			
			BigDecimal valor = new BigDecimal(0);
			
			if (!Util.isNull(detalhamentoBoleto.getValor())) {
				valor = valor.add(detalhamentoBoleto.getValor());

			} else {
				List<ParametroCalculo> listaAbastecimentoIndustrialEmDistrito = new ArrayList<ParametroCalculo>();
				List<ParametroCalculo> listaAbastecimentoIndustrialIsolado = new ArrayList<ParametroCalculo>();
				
				parametros: for (ParametroCalculo parametroCalculo : parametros) {
					
					if (parametroCalculo.isDAE()) {
						detalhamentoBoleto.setDae(true);
						this.existeDae = true;
						break;
					} else {
						this.existeBoleto = true;
					}

					if (detalhamentoBoleto.getIdeAtoAmbiental().isAPE()) {
						valor = calcularAPE(parametroCalculo, detalhamentoBoleto);
						
					} else if(!detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.OUTORGA.getId())
							&& !detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.OUTP.getId())) {
						
						if (parametroCalculo.isExigeCalculo() && Util.isNull(detalhamentoBoleto.getValor())) {
							detalhamentoBoleto.setExigeCalculo(true);
							detalhamentoBoleto.setValor(null);
							break parametros;
						}
					} else if(!Util.isNullOuVazio(detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection())) {
						for (DetalhamentoBoletoFinalidade dbf : detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection()) {

							BigDecimal valorAreaCalculo = new BigDecimal(0);
							
							if(dbf.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.IRRIGACAO.getId()) && !Util.isNullOuVazio(dbf.getNumAreaIrrigada())){
								valorAreaCalculo = valorAreaCalculo.add(dbf.getNumAreaIrrigada());
							}
							
							if(dbf.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.PULVERIZACAO_AGRICOLA.getId())) {
								valorAreaCalculo = valorAreaCalculo.add(dbf.getNumAreaPulverizada());
							}
							
							if(!dbf.isIndCalculado() 
									&& !Util.isNullOuVazio(parametroCalculo.getFatorMultiplicador()) && !Util.isNullOuVazio(valorAreaCalculo)){ 
								
								BigDecimal valorEspecial = new BigDecimal(0);
								valorEspecial = parametroCalculo.getFatorMultiplicador().multiply(valorAreaCalculo);
								valorEspecial = valorEspecial.add(parametroCalculo.getValorTaxa());
								parametroCalculo.setValorTaxa(valorEspecial);
								dbf.setIndCalculado(true);
								break;
							}
						}
					} 
					
					verificaAbastecimentoIndustrial(parametroCalculo, listaAbastecimentoIndustrialEmDistrito, listaAbastecimentoIndustrialIsolado);
					
					if(Util.isNullOuVazio(parametroCalculo.getValorTaxa()))
						valor = valor.add(new BigDecimal(0)) ;
					else
						valor = valor.add(parametroCalculo.getValorTaxa());
				}
			}
			
			if(!Util.isNullOuVazio(detalhamentoBoleto.getIdeAtoAmbiental())) {
				
				if(detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental() == AtoAmbientalEnum.TLA.getId()
						|| detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental() == AtoAmbientalEnum.PERFURACAO_POCO.getId()) {
					
					valor = calculaValorPorQuantidade(detalhamentoBoleto, valor);
				}
			}
			
			/**
			 * Melhoria que atende ao Decreto 16.366/2015 refernete a cobranÃ§a da taxa de anÃ¡lise dos processos de 
			 * Dispensa de Outorga POR PONTO informado no requerimento.
			 * 
			 * @redmine: http://10.105.17.77/redmine/issues/8380
			 * @author danilo.santos
			 */
		 
			if(detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental() == AtoAmbientalEnum.DOUT.getId()){
				valor = valor.multiply(new BigDecimal(contarEquadramentos(requerimento, detalhamentoBoleto)));
			}
			
			//#10424
			EmpreendimentoRequerimento empreendReq = this.empreendimentoService.buscarEmpreendimentoRequerimento(requerimento);
			if(Util.isNull(detalhamentoBoleto.getValor()) && detalhamentoBoleto.getIdeAtoAmbiental() != null &&  (AtoAmbientalEnum.LR.getId().equals(detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental()) || AtoAmbientalEnum.REAPPO.getId().equals(detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental()))
					&& empreendReq != null){
				List<Integer> classes = new ArrayList<Integer>();
				classes.add(3); classes.add(4); classes.add(5); classes.add(6);
				
				valor = new BigDecimal(0);
				if(empreendReq.getIdeClasse()!= null && classes.contains(empreendReq.getIdeClasse().getIdeClasse())){
					if(empreendReq.getIdeFaseEmpreendimento() != null 
							&& (FaseEmpreendimentoEnum.IMPLANTACAO.getId().equals(empreendReq.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento())
									|| FaseEmpreendimentoEnum.OPERACAO.getId().equals(empreendReq.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento()))){
						DetalhamentoBoleto detalhamentoLP = detalhamentoBoleto.clone();
						detalhamentoLP.setIdeAtoAmbiental(atoAmbientalService.obterAtoAmbientalPorIde(AtoAmbientalEnum.LP.getId()));
						valor = valor.add(getValor(detalhamentoLP));
						
						DetalhamentoBoleto detalhamentoLI = detalhamentoBoleto.clone();
						detalhamentoLI.setIdeAtoAmbiental(atoAmbientalService.obterAtoAmbientalPorIde(AtoAmbientalEnum.LI.getId()));
						valor = valor.add(getValor(detalhamentoLI));
					}
					
					if(empreendReq.getIdeFaseEmpreendimento() != null 
							&& FaseEmpreendimentoEnum.OPERACAO.getId().equals(empreendReq.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento())){
						DetalhamentoBoleto detalhamentoLO = detalhamentoBoleto.clone();
						detalhamentoLO.setIdeAtoAmbiental(atoAmbientalService.obterAtoAmbientalPorIde(AtoAmbientalEnum.LO.getId()));
						valor = valor.add(getValor(detalhamentoLO));
					}
				} else {
					DetalhamentoBoleto detalhamentoLO = detalhamentoBoleto.clone();
					detalhamentoLO.setIdeAtoAmbiental(atoAmbientalService.obterAtoAmbientalPorIde(AtoAmbientalEnum.LU.getId()));
					valor = valor.add(getValor(detalhamentoLO));
				}
				
				this.existeBoleto = true;
			}
			
			valorTotal = valorTotal.add(valor);
			
			if (!detalhamentoBoleto.isExigeCalculo() && !detalhamentoBoleto.isDae() && !isAOUT(detalhamentoBoleto)){
				detalhamentoBoleto.setValor(valor);
			}
		}
		
		this.boleto.setValBoleto(valorTotal);
	}

	/**
	 * #10913
	 */
	private void verificaAbastecimentoIndustrial(ParametroCalculo parametroCalculo, 
			List<ParametroCalculo> listaAbastecimentoIndustrialEmDistrito, List<ParametroCalculo> listaAbastecimentoIndustrialIsolado) {
		
		if(!Util.isNullOuVazio(parametroCalculo.getIdeAtoAmbiental()) 
			&& !Util.isNullOuVazio(parametroCalculo.getIdeTipologia()) 
			&& !Util.isNullOuVazio(parametroCalculo.getIdeTipoFinalidadeUsoAgua())
			&&
			(parametroCalculo.getIdeAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.OUTORGA.getId()) 
					|| parametroCalculo.getIdeAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.OUTP.getId())
			)
			&& 
			(parametroCalculo.getIdeTipologia().getIdeTipologia().equals(TipologiaEnum.CAPTACAO_SUBTERRANEA.getId())
					|| parametroCalculo.getIdeTipologia().getIdeTipologia().equals(TipologiaEnum.CAPTACAO_SUPERFICIAL.getId())
			)
			&& 
			(parametroCalculo.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getId())))
			
		{
			if(parametroCalculo.getIdeClasse().getIdeClasse().equals(1)) {
				if(Util.isNullOuVazio((listaAbastecimentoIndustrialIsolado))){
					listaAbastecimentoIndustrialIsolado.add(parametroCalculo);
				} else {
					parametroCalculo.setValorTaxa(BigDecimal.ZERO);
				}
			} else if(parametroCalculo.getIdeClasse().getIdeClasse().equals(2)) {
				if(Util.isNullOuVazio((listaAbastecimentoIndustrialEmDistrito))) {
					listaAbastecimentoIndustrialEmDistrito.add(parametroCalculo);
				} else {
					parametroCalculo.setValorTaxa(BigDecimal.ZERO);
				}
			}
		}
	}
	
	//obtem o valor de outra fase do empreendimento para detalhamentos de ato LR #10424
	private BigDecimal getValor(DetalhamentoBoleto detalhamentoBoleto) throws Exception {
		BigDecimal valor = new BigDecimal(0);
		
		//Obtem o valor da taxa
		Collection<ParametroCalculo> parametros = obtemParametros(detalhamentoBoleto);
		if (Util.isNullOuVazio(parametros) && Util.isNull(detalhamentoBoleto.getValor())) {
			
			if(isAOUT(detalhamentoBoleto)){					
				ParametroCalculo parametroCalculo = new ParametroCalculo();
				parametroCalculo.setIndBoleto(true);
				parametros.add(parametroCalculo);
			}
			
			detalhamentoBoleto.setExigeCalculo(true);
		}
		
		verificaDesmembramentoLA(parametros, detalhamentoBoleto.getIdeAtoAmbiental().isLA());
		
		
		if (!Util.isNull(detalhamentoBoleto.getValor())) {
			valor = valor.add(detalhamentoBoleto.getValor());

		} else {
			parametros: for (ParametroCalculo parametroCalculo : parametros) {

				if (parametroCalculo.isExigeCalculo() && Util.isNull(detalhamentoBoleto.getValor())) {
					detalhamentoBoleto.setExigeCalculo(true);
					detalhamentoBoleto.setValor(null);
					break parametros;
				}
				
				if(Util.isNullOuVazio(parametroCalculo.getValorTaxa()))
					valor = valor.add(new BigDecimal(0));
				else
					valor = valor.add(parametroCalculo.getValorTaxa());
			}
		}
		
		return valor;
	}

	private BigDecimal calcularAPE(ParametroCalculo parametro, DetalhamentoBoleto detalhamentoBoleto) throws Exception {
		BigDecimal valor = new BigDecimal(0);
		BigDecimal valorAtividadeRequerimento = requerimentoTipologiaService.buscarValorAtividadeByRequerimentoAndTipologia(this.requerimento.getIdeRequerimento(), detalhamentoBoleto.getIdeTipologia().getIdeTipologia());
		
		if (!Util.isNullOuVazio(parametro.getFatorMultiplicador())) {
			valor =  valorAtividadeRequerimento.multiply(parametro.getFatorMultiplicador());
		} 
		
		return valor;
	}

	private Collection<ParametroCalculo> obtemParametros(DetalhamentoBoleto detalhamentoBoleto) throws Exception {
		
		Collection<ParametroCalculo> parametros = new ArrayList<ParametroCalculo>();
		
		if(!Util.isNullOuVazio(detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection())) {
			
			for (DetalhamentoBoletoFinalidade dbf : detalhamentoBoleto.getDetalhamentoBoletoFinalidadeCollection()) {
				parametros.addAll(parametroCalculoService.listarPorDetalhamentoBoletoFinalidade(dbf));
				if(detalhamentoBoleto.getIdeTipologia().getIdeTipologia().equals(TipologiaEnum.LANCAMENTO_EFLUENTES.getId())) {
					break;
				}
			}
			
			return parametros;
			
		} else if(!Util.isNullOuVazio(detalhamentoBoleto.getVazoesCaptacao())) {
			for (BigDecimal vazao : detalhamentoBoleto.getVazoesCaptacao()) {
				
				Collection<ParametroCalculo> pcs = parametroCalculoService.listar(detalhamentoBoleto, classe, vazao);
				
				for (ParametroCalculo pc : pcs) {
					parametros.add(pc);
				}
			}
			
			return parametros;
			
		} else if(isCalculoDeLicencas(detalhamentoBoleto)) {
			
			if(detalhamentoBoleto.getIdeAtoAmbiental().isLA()){
				
				if(isAlteracaoLicencaComUnicoObjeto(ObjetoAlteracaoEnum.SUBSTITUICAO)) {
				
					List<Tipologia> tipologiasRequerimento = (List<Tipologia>) requerimentoTipologiaService.buscarTipologias(requerimento);
					
					if (!Util.isNullOuVazio(tipologiasRequerimento)) {
						
						for (Tipologia tr : tipologiasRequerimento) {
							
							detalhamentoBoleto.setIdeTipologia(tr);
							
							for(ParametroCalculo pc : parametroCalculoService.listar(detalhamentoBoleto, new Classe(1), null)) {
								tr.setValorTaxa(pc.getValorTaxa());
							}
						}
						
						BigDecimal maiorValor = null;
						Tipologia tipologiaSelecionada = null;
						
						for (Tipologia tr : tipologiasRequerimento) {
							if(maiorValor == null) {
								maiorValor = tr.getValorTaxa();
								tipologiaSelecionada = tr;
								
							} else if(tr.getValorTaxa().compareTo(maiorValor) == 1) {
								maiorValor = tr.getValorTaxa();
								tipologiaSelecionada = tr;
							}
						}
						
						tipologiaSelecionada.setDesTipologia("-");
						detalhamentoBoleto.setNumVazao(null);
						detalhamentoBoleto.setIdeTipologia(tipologiaSelecionada);
						
						return parametroCalculoService.listar(detalhamentoBoleto, new Classe(1), null);
					} else {
						return null;
					}
				} else if(isAlteracaoLicencaComUnicoObjeto(ObjetoAlteracaoEnum.DESMEMBRAMENTO)) {
					detalhamentoBoleto.setIdeTipologia(null);
					return parametroCalculoService.listar(detalhamentoBoleto, null, null);
				}
			}
			
			//#7284
			List<Tipologia> tipologias = (List<Tipologia>) requerimentoTipologiaService.buscarTipologias(requerimento);
			
			for (Tipologia t : tipologias) {
				if(!t.getIndAutorizacao()){
					Porte porteCalculado = retornarPorteCalculado(t);
					t.setPorte(porteCalculado);
					PotencialPoluicao potencialPoluicao = t.getTipologiaGrupo().getIdePotencialPoluicao();
					Classe classeCalculada = retornarClasseCalculada(t,porteCalculado, potencialPoluicao);
					t.setClasse(classeCalculada);
					
					detalhamentoBoleto.setIdeTipologia(t);
					
					Collection<ParametroCalculo> listaPc = parametroCalculoService.listar(detalhamentoBoleto, t.getClasse(), null);
					
					if (Util.isNullOuVazio(listaPc)){
						detalhamentoBoleto.setIdeTipologia(null);
						listaPc = parametroCalculoService.listar(detalhamentoBoleto, t.getClasse(), null);
					}
					
					for (ParametroCalculo parametroCalculo : listaPc) {
						t.setValorTaxa(parametroCalculo.getValorTaxa());
					}
				}
			}
			
			if(tipologias.size() > 1) {
				Classe maiorClasse = null;
				BigDecimal maiorValor = null;
				Tipologia tipologiaSelecionada = null;
				Tipologia ultimaTipologia = new Tipologia();
				
				for (Tipologia t : tipologias) {
					if(!Util.isNull(t.getClasse())){ 

						if(maiorClasse == null) {
							maiorClasse = t.getClasse();
							maiorValor = t.getValorTaxa();	
							tipologiaSelecionada = t;

						} else if(t.getClasse().getIdeClasse() > maiorClasse.getIdeClasse()) {
							maiorClasse = t.getClasse();
							maiorValor = t.getValorTaxa();
							tipologiaSelecionada = t;

						} else if(t.getClasse().getIdeClasse() == maiorClasse.getIdeClasse()) {
							if(!Util.isNullOuVazio(maiorValor) && t.getValorTaxa().compareTo(maiorValor) == 1) {
								maiorClasse = t.getClasse();
								maiorValor = t.getValorTaxa();
								tipologiaSelecionada = t;
							}
						}
					}
					ultimaTipologia = t;
				}
				
				if(Util.isNullOuVazio(tipologiaSelecionada)) {
					tipologiaSelecionada = ultimaTipologia;
				}

				tipologiaSelecionada.setDesTipologia("-");
				detalhamentoBoleto.setNumVazao(null);
				detalhamentoBoleto.setIdeTipologia(tipologiaSelecionada);
				return parametroCalculoService.listar(detalhamentoBoleto, tipologiaSelecionada.getClasse(), null);
				
			} else if(tipologias.size() == 1) {
				tipologias.get(0).setDesTipologia("-");
				detalhamentoBoleto.setNumVazao(null);
				detalhamentoBoleto.setIdeTipologia(tipologias.get(0));
				
				
				Collection<ParametroCalculo> listaPc = parametroCalculoService.listar(detalhamentoBoleto, tipologias.get(0).getClasse(), null);
				
				if (Util.isNullOuVazio(listaPc)){
					detalhamentoBoleto.setIdeTipologia(null);
					listaPc = parametroCalculoService.listar(detalhamentoBoleto, tipologias.get(0).getClasse(), null);
				}
				
				return listaPc;
			} 
		} else if(detalhamentoBoleto.getIdeAtoAmbiental().isSisfauna() && detalhamentoBoleto.getIdeTipologia().isCriacaoFaunaSilvestre()) {
			
			Fauna f = faunaService.getFaunaByIdeRequerimento(requerimento);
			
			if(!Util.isNullOuVazio(f)) {
				
				f.setTipoCriadouroFaunaCollection(faunaService.getListTipoCriadouroFaunaByFauna(f));
				
				if(!Util.isNullOuVazio(f.getTipoCriadouroFaunaCollection())) {
					
					detalhamentoBoleto.setTipoCriadouroFaunaCollection(f.getTipoCriadouroFaunaCollection());
				}
			}
			
			return parametroCalculoService.listar(detalhamentoBoleto, classe, detalhamentoBoleto.getNumVazao());
		}
		
		return parametroCalculoService.listar(detalhamentoBoleto, classe, detalhamentoBoleto.getNumVazao());
	}

	private Classe retornarClasseCalculada(Tipologia t, Porte porteCalculado,PotencialPoluicao potencialPoluicao) {
		Classe classeCalculada = classeService.buscarClasseAtividade(t.getPorte(), potencialPoluicao);
		if(Util.isNull(classeCalculada) && porteCalculado.getIdePorte().equals(PorteEnum.NAO_IDENTIFICADO.getId())) {
			classeCalculada = new Classe(1, "Classe 1");						
		}
		return classeCalculada;
	}

	private Porte retornarPorteCalculado(Tipologia t) throws Exception {
		Porte porteCalculado = porteService.calcularValorPorte(t.getTipologiaGrupo().getIdeTipologiaGrupo(), retornarValorAtividade(t));
		if(Util.isNull(porteCalculado)) {
			porteCalculado = new Porte(PorteEnum.NAO_IDENTIFICADO.getId());
		}
		return porteCalculado;
	}
	
	private boolean isCalculoDeLicencas(DetalhamentoBoleto db) {
		
		if(db.getIdeAtoAmbiental().getIdeTipoAto().getIdeTipoAto().equals(TipoAtoEnum.LICENCA.getId())) {
			return true;
		} else {
			return false;
		}
	}

	private BigDecimal calculaValorPorQuantidade(DetalhamentoBoleto db, BigDecimal valor) throws Exception {
		
		int quantidade =0;
		
		if(!Util.isNullOuVazio(db.getIdeAtoAmbiental())) {
			
			if(db.getIdeAtoAmbiental().getIdeAtoAmbiental() == AtoAmbientalEnum.TLA.getId()){
				quantidade = atoAmbientalService.countAtosEnquadradosBySolicitacaoAdminstrativo(requerimento.getIdeRequerimento());
				
			} else if(db.getIdeAtoAmbiental().getIdeAtoAmbiental() == AtoAmbientalEnum.PERFURACAO_POCO.getId()) {
				int q1 = outorgaLocalizacaoGeograficaService.countOutorgaLocalizacaoGeograficaByRequerimentoPerfuracaoPoco(requerimento);
				int q2 = fceLocalizacaoGeograficaService.countFceLocalizacaoGeograficaByRequerimento(requerimento);
				quantidade = (q2>q1)?q2:q1; 
			}
		}
		
		if(!Util.isNullOuVazio(db.getIdeTipologia())) {
			
			if(db.getIdeTipologia().isLancamentoEfluentes()) {
				quantidade = outorgaLocalizacaoGeograficaService.countOutorgaLocalizacaoGeograficaByRequerimentoLancamentoEfluentes(requerimento);
				
			} else if(db.getIdeTipologia().isIntervencao()) {
				quantidade = outorgaLocalizacaoGeograficaService.countOutorgaLocalizacaoGeograficaByRequerimentoIntervencao(requerimento);
			}
		}
		
		if(quantidade != 0){
			valor = valor.multiply(new BigDecimal(quantidade));
		}
		
		return valor;
	}
	
	private void verificaDesmembramentoLA(Collection<ParametroCalculo> parametros, boolean isLA) throws Exception {
		
		boolean isDesmembramentoLA = false;
		boolean isApenasDesmembramentoLA = false;
		Collection<ParametroCalculo> novaListaParametros = new ArrayList<ParametroCalculo>();

		if(!Util.isNullOuVazio(parametros)) {
		
			Licenca licenca = licencaService.getLicencaByIdeRequerimentoTipoAlteracao(requerimento);
			
			if(!Util.isNull(licenca) && isLA) {
				isDesmembramentoLA = isDesmembramento(licenca);
				isApenasDesmembramentoLA = isAlteracaoLicencaComUnicoObjeto(ObjetoAlteracaoEnum.DESMEMBRAMENTO);
			}
			
			//DESMEMBRAMENTO DE LA
			if(isDesmembramentoLA) {
			
				BigDecimal somatorioValores = new BigDecimal(0.0);
				
				for (ParametroCalculo pc : parametros) {
					somatorioValores = somatorioValores.add(pc.getValorTaxa());
				}
				
				//QUANDO O VALOR ORIGINAL DA TAXA FOR MENOR QUE 1000, DEVE-SE ELEVAR O VALOR PARA 1000
				if(somatorioValores.compareTo(new BigDecimal(2000.0)) == -1) {
					for (ParametroCalculo pc : parametros) {
						if(pc.getValorTaxa().compareTo(new BigDecimal(1000.0)) != -1){
							novaListaParametros.add(pc);
						}
					}
				} else {
					if(isApenasDesmembramentoLA) {
						for (ParametroCalculo pc : parametros) {
							if(pc.getValorTaxa().compareTo(BigDecimal.valueOf(1000.0)) == 0){
								novaListaParametros.add(pc);
							}
						}
					} else {
						//QUANDO O VALOR ORIGINAL DA TAXA FOR MAIOR QUE 1000, DEVE-SE MANTER O VALOR ORIGINAL
						for (ParametroCalculo pc : parametros) {
							if(pc.getValorTaxa().compareTo(BigDecimal.valueOf(1000.0)) == 1){
								novaListaParametros.add(pc);
							}
						}
					}
				}
			
				if(!Util.isNullOuVazio(novaListaParametros)){
					parametros.clear();
					parametros.addAll(novaListaParametros);
				}
			}
		}
	}

	private boolean isDesmembramento(Licenca licenca) {
		boolean isDesmembramentoLA = false;
		Collection<ObjetoAlteracao> listObjAlt = objetoAlteracaoService.ListarObjetoAlteracaoByLicenca(licenca);
		
		//VERIFICA SE E DESMEMBRAMENTO DE LA
		if(!Util.isNullOuVazio(listObjAlt)) {
			for (ObjetoAlteracao oa : listObjAlt) {
				if(oa.getIdeObjetoAlteracao().equals(2)) {
					isDesmembramentoLA = true;
				}
			}
		}
		return isDesmembramentoLA;
	}

	public Double calcularCertificado() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		for (DetalhamentoBoleto detalhamentoBoleto : detalhamentosBoleto) {
			if (detalhamentoBoleto.isDae() && !Util.isNull(detalhamentoBoleto.getValorCertificado()))
				valorTotal = detalhamentoBoleto.getValorCertificado().add(valorTotal);
		}
		return valorTotal.doubleValue();
	}
	
	
	public Double calcularVistoria() throws Exception {
		
		BigDecimal somaAreaVistoria = BigDecimal.ZERO;
		
		biomaPredominante = retornarBiomaPredominante();
		if(!Util.isNullOuVazio(listaBiomaRequerimento)) {
			for(BiomaRequerimento br : listaBiomaRequerimento) {
				if(!verificaBiomaSemVistoria(br)) somaAreaVistoria = somaAreaVistoria.add(BigDecimal.valueOf(br.getValArea()));
			}
		}
		else{
			Collection<Florestal> listaFlorestal = florestalService.carregarListaFlorestal(requerimento);
			if(!Util.isNullOuVazio(listaFlorestal)) {
				for(Florestal florestal : listaFlorestal) {
					somaAreaVistoria=somaAreaVistoria.add(florestal.getNumAreaReservaLegal());
				}
				listaBiomaRequerimentoDetalhamento = new ArrayList<BiomaRequerimento>();
				valorTotalAreaBioma = somaAreaVistoria.doubleValue();
				listaBiomaRequerimentoDetalhamento.add(new BiomaRequerimento(biomaPredominante,valorTotalAreaBioma));
			}
		}
		
		somaAreaVistoria = calcularValorDAE(somaAreaVistoria, biomaPredominante.getIdeBioma());
		
		return somaAreaVistoria.doubleValue();
	}

	private boolean verificaBiomaSemVistoria(BiomaRequerimento br) {
		
		List<PerguntaRequerimento> pr = 
				boletoServiceFacade.listarPerguntaRequerimentoPorRequerimentoECodPergunta
					(br.getIdeRequerimento(), PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p13.getCod());
		
		if(!Util.isNullOuVazio(pr)) {
			List<Florestal> listaFlorestal = boletoServiceFacade.listarFlorestalPorRequerimento(br.getIdeRequerimento());
			
			for (Florestal florestal : listaFlorestal) {
				
				List<FlorestalCaracteristicaFlorestaProducao> listaFCFP = 
						boletoServiceFacade.listarFlorestalCaracteristicaFlorestaProducaoPorFlorestal(florestal);
				
				if(!Util.isNullOuVazio(listaFCFP) && listaFCFP.size() == 1) {
					if(listaFCFP.get(0).getIdeCaracteristicaFlorestaProducao().getIdeCaracteristicaFlorestaProducao()
						.equals(CaracteristicaFlorestaProducaoEnum.NENHUMA.getId())) {
						
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private BigDecimal calcularValorDAE(BigDecimal areaVistoriada, Integer ideBioma) {
				
		final int MENOR = -1;
		final int IGUAL =  0;
		final int MAIOR =  1;

		boolean pValor           = false;
		boolean deveCalcular     = true;
		boolean possuiAreaMinima = false;
	    boolean possuiAreaMaxima = false;
	
		BigDecimal pCalculo = BigDecimal.ZERO;
		
		try {
			Collection<ParametroCalculo> parametrosDAE = this.parametroCalculoService.listarParametrosDAEPorBioma(ideBioma);

			for (ParametroCalculo parametroCalculo : parametrosDAE) {

				if (!parametroCalculo.isExigeCalculo()) {
					continue;
				}

				BigDecimal areaMaxima = null;
				
				if (!Util.isNullOuVazio(parametroCalculo.getAreaMaxima())) {
					areaMaxima = parametroCalculo.getAreaMaxima();
					possuiAreaMaxima = true;
				}else{
					areaMaxima = new BigDecimal("0.00");
					possuiAreaMaxima = false;
				}

				BigDecimal areaMinima;
				
				if(!Util.isNullOuVazio(parametroCalculo.getAreaMinima())){
					areaMinima = parametroCalculo.getAreaMinima();
					possuiAreaMinima = true;
				}else{
					areaMinima = new BigDecimal("0.0001");
					possuiAreaMinima = false;
				}
						
				if(areaVistoriada.compareTo(BigDecimal.ZERO)==0){
					deveCalcular = false;
				}
				
				if(deveCalcular){
				
					if(areaVistoriada.compareTo(areaMinima)==IGUAL || areaVistoriada.compareTo(areaMaxima)==IGUAL){
						pValor = true;
					}
					
					else if(areaVistoriada.compareTo(areaMinima)==MAIOR && areaVistoriada.compareTo(areaMaxima)==MENOR){
						pValor = true;
					}					
					
					else if((areaVistoriada.compareTo(areaMinima)==MAIOR || areaVistoriada.compareTo(areaMinima)==IGUAL)  && !possuiAreaMaxima){
						pValor = true;
					}
					
					else if(!possuiAreaMinima && !possuiAreaMaxima){
						pValor = false;	
					}
									
				}
				
				if(pValor){
					pCalculo =  parametroCalculo.getValorTaxa();	
					break;
				}	
									
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}

		return pCalculo;
	}

	public void salvarBoletos() {
		try {
			if(isBoletoManualValido()){
				boletoServiceFacade.salvarBoletos(this);
			}
		}
		catch(SeiaTramitacaoRequerimentoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void gerarBoleto() throws Exception {
		this.boleto.setDtcEmissao(new Date());
		this.boleto.setIdePessoa(this.getOperador());
		this.boleto.setIdeRequerimento(requerimento);
		this.boleto.setDetalhamentoBoletoCollection(new ArrayList<DetalhamentoBoleto>());
		valorTotalDosOutrosDetalhamentos = new BigDecimal(0);
		this.boleto.setIndBoletoRegistrado(true);
		
		for (DetalhamentoBoleto detalhamentoBoleto : detalhamentosBoleto) {
			if(!detalhamentoBoleto.getIdeAtoAmbiental().isPerfuracaoDePoco() && !Util.isNullOuVazio(detalhamentoBoleto.getValor())){
				valorTotalDosOutrosDetalhamentos = valorTotalDosOutrosDetalhamentos.add(detalhamentoBoleto.getValor());
			}
		}

		for (DetalhamentoBoleto detalhamentoBoleto : detalhamentosBoleto) {
			
			if (!detalhamentoBoleto.isDae()) {
				
				if(isTLAEnquadramentoAto())
					detalhamentoBoleto.setValor(boleto.getValBoleto());
				if(detalhamentoBoleto.getIdeAtoAmbiental().isPerfuracaoDePoco()){
					detalhamentoBoleto.setValor((boleto.getValBoleto()).subtract(valorTotalDosOutrosDetalhamentos != null ? valorTotalDosOutrosDetalhamentos : new BigDecimal(0),MathContext.DECIMAL32));
				}
				
				detalhamentoBoleto.setDtcCriacao(new Date());
				detalhamentoBoleto.setIdeBoletoPagamentoRequerimento(this.boleto);
				this.boleto.getDetalhamentoBoletoCollection().add(detalhamentoBoleto);
			}
		}
	}

	public void gerarDAECertificado(Double valorTotalDAECertificado) throws Exception {

		this.certificado.setVlrTotalCertificado(BigDecimal.valueOf(valorTotalDAECertificado));
		this.certificado.setIdeRequerimento(requerimento);
		this.certificado.setIdePessoa(this.getOperador());

		this.certificado.setDetalhamentoBoletoCollection(new ArrayList<DetalhamentoBoleto>());

		for (DetalhamentoBoleto detalhamentoBoleto : this.detalhamentosBoleto) {
			if (detalhamentoBoleto.isDae()) {
				detalhamentoBoleto.setDtcCriacao(new Date());
				detalhamentoBoleto.setIdeBoletoDaeRequerimento(this.certificado);
				this.certificado.getDetalhamentoBoletoCollection().add(detalhamentoBoleto);
			}
		}

	}

	public void gerarDAEVistoria(Double valorTotalDAEVistoria) throws Exception {

		this.vistoria.setVlrTotalVistoria(BigDecimal.valueOf(valorTotalDAEVistoria));
		this.vistoria.setIdeRequerimento(this.requerimento);
		this.vistoria.setIdePessoa(this.getOperador());

		this.vistoria.setDetalhamentoBoletoCollection(new ArrayList<DetalhamentoBoleto>());

		double vlrAreaVistoria = 0;
		for (DetalhamentoBoleto detalhamentoBoleto : this.detalhamentosBoleto) {
			if (detalhamentoBoleto.isDae() && detalhamentoBoleto.isExisteArea()) {
				vlrAreaVistoria += detalhamentoBoleto.getAreaVistoriada().doubleValue();
				detalhamentoBoleto.setDtcCriacao(new Date());
				detalhamentoBoleto.setIdeBoletoDaeRequerimento(this.vistoria);
				vistoria.getDetalhamentoBoletoCollection().add(detalhamentoBoleto);
			}
		}
		
		this.vistoria.setVlrAreaVistoria(BigDecimal.valueOf(vlrAreaVistoria));
	}
	
	private void carregarAtividadesDoRequerimento(Requerimento requerimento)  {
		this.atividadesLicenca.addAll(this.requerimentoTipologiaService.buscarTipologiasAutorizacao(requerimento));
		this.atividadesLicenca.addAll(this.requerimentoTipologiaService.buscarTipologiasLicenca(requerimento));
	}
	
	private boolean isAlteracaoLicencaComUnicoObjeto(ObjetoAlteracaoEnum obj)  {
		Licenca lic = licencaService.getLicencaByIdeRequerimentoTipoAlteracao(requerimento);
		
		if (!Util.isNullOuVazio(lic)) {
			List<ObjetoAlteracao> loa = (List<ObjetoAlteracao>) objetoAlteracaoService.ListarObjetoAlteracaoByLicenca(lic);
			
			if (!Util.isNullOuVazio(loa) && loa.size() == 1) {

				if (loa.get(0).getIdeObjetoAlteracao().equals(obj.getId())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private BigDecimal retornarValorAtividade(Tipologia tipologia) {
		String valorAtividade = null;
		
		if(tipologia.getValAtividade().contains(",")) {
			valorAtividade = tipologia.getValAtividade().replace(".", "").replace(",", ".");
		} else {
			if(Util.isNullOuVazio(tipologia.getValAtividade())){
				valorAtividade = "0";
			} else {
				valorAtividade = tipologia.getValAtividade();
			}
		}
		
		return BigDecimal.valueOf(Double.parseDouble(valorAtividade));
	}
	
	private void carregarOuCriarClasse(Requerimento requerimento) throws Exception{
		List<String> codPerguntas = new ArrayList<String>();
		codPerguntas.add(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA2_1.getCod());
		codPerguntas.add(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA3_1.getCod());
		
		List<PerguntaRequerimento> perguntas = perguntaRequerimentoService.listarPerguntaRequerimentoPor(requerimento, codPerguntas);
		Collection<EmpreendimentoRequerimento> empreendimentos = empreendimentoRequerimentoService.listarEmpreendimentoRequerimento(requerimento);
		
		for(EmpreendimentoRequerimento e : empreendimentos){
			for(PerguntaRequerimento p : perguntas){
				if(p.getIndResposta() && Util.isNullOuVazio(e.getIdeClasse())) {
					List<RequerimentoTipologia> requerimentosTipologia = this.requerimentoTipologiaService.buscarRequerimentoTipologiasPorRequerimento(requerimento);
					for(RequerimentoTipologia r : requerimentosTipologia){
						if(!Util.isNullOuVazio(r.getValAtividade())){
							abaFinalizarRequerimentoController.calcularPorte();
							abaFinalizarRequerimentoController.salvarEmpreendimentoRequerimentoPorBoleto(e);
						}
					}
				}
			}
		}
	}

	private Pessoa getOperador() throws Exception {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return new Pessoa(usuario.getIdePessoaFisica());
	}
	
	public void checkBoletoManual() {
		try {
			if (boleto.getIndBoletoGeradoManualmente()) {
				boleto.setIndIsento(false);
				boleto.setIdeMotivoIsencaoBoleto(null);
			} else{
				Calendar c = this.calcularVencimento();
				boleto.setDtcVencimento(c.getTime());
				boleto.setNumBoleto(null);
				boleto.setDesCaminhoBoleto(null);
				boleto.setPathComprovante(null);
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void uploadBoleto(FileUploadEvent event) {
		try {
			String path = this.gerenciaArquivoservice.uploadBoletoManual(event.getFile(), this.requerimento);
			boleto.setPathComprovante(path);
			boleto.setDesCaminhoBoleto(path);
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public StreamedContent getFileDownload(String caminhoArquivo) {
		try {
			return gerenciaArquivoservice.getContentFile(caminhoArquivo);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	private Integer contarEquadramentos(Requerimento requerimento, DetalhamentoBoleto detalhe) {
		final Double vazaoDispensaCaptacao = 43.2;
		final Double vazaoDispensaIntervencao = 200000.0;
		int totalEquadramentos = 0;	
		
		for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramentoService.listarEnquadramentoAtoByRequerimento(requerimento.getIdeRequerimento())) {
			List<OutorgaLocalizacaoGeografica> listOLG = null;
				
			if(!Util.isNullOuVazio(enquadramentoAtoAmbiental.getTipologia())) {
				
				if(enquadramentoAtoAmbiental.getTipologia().isCaptacaoSubterranea() && detalhe.getIdeTipologia().equals(enquadramentoAtoAmbiental.getTipologia())) {
					
					if(detalhe.getIdeAtoAmbiental().isDispensaOutorga() && enquadramentoAtoAmbiental.getAtoAmbiental().isDispensaOutorga()){
						
						List<OutorgaLocalizacaoGeografica> novalistOLG = new ArrayList<OutorgaLocalizacaoGeografica>();
						
						listOLG = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA, requerimento);
						
						for (OutorgaLocalizacaoGeografica olg : listOLG) {
							if (olg.getNumVazao().doubleValue() <= vazaoDispensaCaptacao){
								novalistOLG.add(olg);
							}
						} 
						
						totalEquadramentos =  totalEquadramentos +  novalistOLG.size();
					}
				}
				
				if(enquadramentoAtoAmbiental.getTipologia().isCaptacaoSuperficial() && detalhe.getIdeTipologia().equals(enquadramentoAtoAmbiental.getTipologia())){
					
					if(detalhe.getIdeAtoAmbiental().isDispensaOutorga() && enquadramentoAtoAmbiental.getAtoAmbiental().isDispensaOutorga()){
						
						List<OutorgaLocalizacaoGeografica> novalistOLG = new ArrayList<OutorgaLocalizacaoGeografica>();
						
						listOLG = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL, requerimento);
						
						for (OutorgaLocalizacaoGeografica olg : listOLG) {
							if (olg.getNumVazao().doubleValue() <= vazaoDispensaCaptacao){
								novalistOLG.add(olg);
							}
						} 
						
						totalEquadramentos =  totalEquadramentos +  novalistOLG.size();
					}
				}
				
				if (enquadramentoAtoAmbiental.getTipologia().isIntervencao() && detalhe.getIdeTipologia().equals(enquadramentoAtoAmbiental.getTipologia())) {
					
					if(detalhe.getIdeAtoAmbiental().isDispensaOutorga() && enquadramentoAtoAmbiental.getAtoAmbiental().isDispensaOutorga()){
						
						List<OutorgaLocalizacaoGeografica> novalistOLG = new ArrayList<OutorgaLocalizacaoGeografica>();
						
						listOLG = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByModalidadeOutorga(ModalidadeOutorgaEnum.INTERVENCAO, requerimento);
						
						for (OutorgaLocalizacaoGeografica olg : listOLG) {
							if (!Util.isNullOuVazio(olg.getNumVolumeMaximoAcumulado()) && olg.getNumVolumeMaximoAcumulado().doubleValue() <= vazaoDispensaIntervencao){
								novalistOLG.add(olg);
							}
						} 
						
						totalEquadramentos =  totalEquadramentos +  novalistOLG.size();
					}
				}
			}
		}
		
		return totalEquadramentos;
	}

	public String getDataHoje() {
		dataHoje = Util.getDataHoje();
		return dataHoje;
	}
	
	private boolean isBoletoManualValido(){
		if(boleto != null && boleto.getIndBoletoGeradoManualmente() != null && boleto.getIndBoletoGeradoManualmente()){
			if(Util.isNullOuVazio(boleto.getNumBoleto())){
				JsfUtil.addWarnMessage("O Campo NÃºmero do Boleto Ã© de preenchimento obrigatÃ³rio.");
				return false;
			}
			if(Util.isNullOuVazio(boleto.getDesCaminhoBoleto())){
				JsfUtil.addWarnMessage("Ã‰ necessÃ¡rio fazer upload do documento.");
				return false;
			}
		}
		return true;
	}
	
	public void removerCaracteres(){
		if(!Util.isNullOuVazio(boleto.getNumBoleto())){
			boleto.setNumBoleto(boleto.getNumBoleto().replaceAll("[^0-9]", ""));
		}
	}
	
	private boolean isOutorga(Integer ideAtoAmbiental){ //qualquer tipo de outorga
		return AtoAmbientalEnum.AOUT.getId().equals(ideAtoAmbiental)
				|| AtoAmbientalEnum.COUT.getId().equals(ideAtoAmbiental)
				|| AtoAmbientalEnum.DOUT.getId().equals(ideAtoAmbiental)
				|| AtoAmbientalEnum.OUTORGA.getId().equals(ideAtoAmbiental)
				|| AtoAmbientalEnum.OUTP.getId().equals(ideAtoAmbiental)
				|| AtoAmbientalEnum.PPV_OUT.getId().equals(ideAtoAmbiental)
				|| AtoAmbientalEnum.ROUT.getId().equals(ideAtoAmbiental);
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public BoletoPagamentoRequerimento getBoleto() {
		return boleto;
	}

	public void setBoleto(BoletoPagamentoRequerimento boleto) {
		this.boleto = boleto;
	}

	public Collection<DetalhamentoBoleto> getDetalhamentosBoleto() {
		return detalhamentosBoleto;
	}

	public void setDetalhamentosBoleto(Collection<DetalhamentoBoleto> detalhamentosBoleto) {
		this.detalhamentosBoleto = detalhamentosBoleto;
	}

	public Collection<MotivoIsencaoBoleto> getMotivosIsencao() {
		return motivosIsencao;
	}

	public void setMotivosIsencao(Collection<MotivoIsencaoBoleto> motivosIsencao) {
		this.motivosIsencao = motivosIsencao;
	}

	public Integer getDiasVencimento() {
		return diasVencimento;
	}

	public void setDiasVencimento(Integer diasVencimento) {
		this.diasVencimento = diasVencimento;
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public boolean isExisteDae() {
		return existeDae;
	}

	public void setExisteDae(boolean existeDae) {
		this.existeDae = existeDae;
	}

	public BoletoDaeRequerimento getCertificado() {
		return certificado;
	}

	public void setCertificado(BoletoDaeRequerimento certificado) {
		this.certificado = certificado;
	}

	public BoletoDaeRequerimento getVistoria() {
		return vistoria;
	}

	public void setVistoria(BoletoDaeRequerimento vistoria) {
		this.vistoria = vistoria;
	}

	public DetalhamentoBoleto getDetalhamentoBoletoACalcular() {
		return detalhamentoBoletoACalcular;
	}

	public void setDetalhamentoBoletoACalcular(DetalhamentoBoleto detalhamentoBoletoACalcular) {
		this.detalhamentoBoletoACalcular = detalhamentoBoletoACalcular;
	}

	public boolean isExisteBoleto() {
		return existeBoleto;
	}

	public void setExisteBoleto(boolean existeBoleto) {
		this.existeBoleto = existeBoleto;
	}

	public boolean isFaltaParametros() {
		return faltaParametros;
	}

	public void setFaltaParametros(boolean faltaParametros) {
		this.faltaParametros = faltaParametros;
	}

	public String getStrMsgAlerta() {
		return strMsgAlerta;
	}

	public void setStrMsgAlerta(String strMsgAlerta) {
		this.strMsgAlerta = strMsgAlerta;
	}

	public Collection<Tipologia> getAtividadesLicenca() {
		return atividadesLicenca;
	}

	public void setAtividadesLicenca(Collection<Tipologia> atividadesLicenca) {
		this.atividadesLicenca = atividadesLicenca;
	}

	public Collection<EnquadramentoAtoAmbiental> getEnquadramentoAtosAmbientaisDoReq() {
		return enquadramentoAtosAmbientaisDoReq;
	}

	public void setEnquadramentoAtosAmbientaisDoReq(Collection<EnquadramentoAtoAmbiental> enquadramentoAtosAmbientaisDoReq) {
		this.enquadramentoAtosAmbientaisDoReq = enquadramentoAtosAmbientaisDoReq;
	}

	public EmpreendimentoRequerimento getEmpreendimentoRequerimento() {
		return empreendimentoRequerimento;
	}

	public void setEmpreendimentoRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento) {
		this.empreendimentoRequerimento = empreendimentoRequerimento;
	}

	public ParametroCalculo getParametroCalculoSelecionado() {
		return parametroCalculoSelecionado;
	}

	public void setParametroCalculoSelecionado(ParametroCalculo parametroCalculoSelecionado) {
		this.parametroCalculoSelecionado = parametroCalculoSelecionado;
	}

	public TramitacaoRequerimento getTramitacaoRequerimento() {
		return tramitacaoRequerimento;
	}

	public void setTramitacaoRequerimento(TramitacaoRequerimento tramitacaoRequerimento) {
		this.tramitacaoRequerimento = tramitacaoRequerimento;
	}

	public boolean getExisteBoleto() {
		return existeBoleto;
	}

	public boolean getExisteDae() {
		return existeDae;
	}

	public Collection<BiomaRequerimento> getListaBiomaRequerimento() {
		return listaBiomaRequerimento;
	}

	public void setListaBiomaRequerimento(Collection<BiomaRequerimento> listaBiomaRequerimento) {
		this.listaBiomaRequerimento = listaBiomaRequerimento;
	}

	public Double getValorTotalCertificadoDAE() {
		return valorTotalCertificadoDAE;
	}

	public void setValorTotalCertificadoDAE(Double valorTotalCertificadoDAE) {
		this.valorTotalCertificadoDAE = valorTotalCertificadoDAE;
	}

	public Double getValorTotalVistoriaDAE() {
		return valorTotalVistoriaDAE;
	}

	public void setValorTotalVistoriaDAE(Double valorTotalVistoriaDAE) {
		this.valorTotalVistoriaDAE = valorTotalVistoriaDAE;
	}

	public Collection<BiomaRequerimento> getListaBiomaRequerimentoDetalhamento() {
		return listaBiomaRequerimentoDetalhamento;
	}

	public void setListaBiomaRequerimentoDetalhamento(
			Collection<BiomaRequerimento> listaBiomaRequerimentoDetalhamento) {
		this.listaBiomaRequerimentoDetalhamento = listaBiomaRequerimentoDetalhamento;
	}

	public Double getValorTotalAreaBioma() {
		return valorTotalAreaBioma;
	}

	public void setValorTotalAreaBioma(Double valorTotalAreaBioma) {
		this.valorTotalAreaBioma = valorTotalAreaBioma;
	}

	public Collection<OutorgaLocalizacaoGeografica> getListaCaptacaoSubterranea() {
		return listaCaptacaoSubterranea;
	}

	public void setListaCaptacaoSubterranea(Collection<OutorgaLocalizacaoGeografica> listaCaptacaoSubterranea) {
		this.listaCaptacaoSubterranea = listaCaptacaoSubterranea;
	}

	public Collection<OutorgaLocalizacaoGeografica> getListaCaptacaoSuperficial() {
		return listaCaptacaoSuperficial;
	}

	public void setListaCaptacaoSuperficial(Collection<OutorgaLocalizacaoGeografica> listaCaptacaoSuperficial) {
		this.listaCaptacaoSuperficial = listaCaptacaoSuperficial;
	}

	public Collection<OutorgaLocalizacaoGeografica> getListaLancamentoEfluente() {
		return listaLancamentoEfluente;
	}

	public void setListaLancamentoEfluente(Collection<OutorgaLocalizacaoGeografica> listaLancamentoEfluente) {
		this.listaLancamentoEfluente = listaLancamentoEfluente;
	}

	public void setDataHoje(String dataHoje) {
		this.dataHoje = dataHoje;
	}
	
	public boolean isBoletoApe() {
		return isBoletoApe;
	}

	public void setBoletoApe(boolean isBoletoApe) {
		this.isBoletoApe = isBoletoApe;
	}

	public Collection<MotivoIsencaoBoleto> getMotivoIsencaoVistoriaDae() {
		return motivoIsencaoVistoriaDae;
	}

	public void setMotivoIsencaoVistoriaDae(Collection<MotivoIsencaoBoleto> motivoIsencaoVistoriaDae) {
		this.motivoIsencaoVistoriaDae = motivoIsencaoVistoriaDae;
	}

	public Bioma getBiomaPredominante() {
		return biomaPredominante;
	}

	public void setBiomaPredominante(Bioma biomaPredominante) {
		this.biomaPredominante = biomaPredominante;
	}

}