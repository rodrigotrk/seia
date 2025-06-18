package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.apache.log4j.Level;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.DadoConcedidoFceImpl;
import br.gov.ba.seia.dto.DadoConcedidoImpl;
import br.gov.ba.seia.dto.TransferenciaAmbientalDTO;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.DevedorReposicaoFlorestal;
import br.gov.ba.seia.entity.DocumentoIdentificacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.DocumentoRepresentacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoRequerimentoEmpreendimento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.MemoriaCalculoDaeParcela;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.PessoaJuridicaHistorico;
import br.gov.ba.seia.entity.Portaria;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.SolicitacaoAdministrativoAtoAmbiental;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.facade.AtoDeclaratorioFacade;
import br.gov.ba.seia.facade.DeclaracaoQueimaControladaFacade;
import br.gov.ba.seia.facade.FceServiceFacade;
import br.gov.ba.seia.facade.FormarEquipeServiceFacade;
import br.gov.ba.seia.facade.ProcessoReenquadramentoHistAtoServiceFacade;
import br.gov.ba.seia.facade.RFPFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorio;
import br.gov.ba.seia.service.BoletoPagamentoRequerimentoService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.DevedorReposicaoFlorestalService;
import br.gov.ba.seia.service.DocumentoRequerimentoEmpreendimentoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.MemoriaCalculoDaeParcelaService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaHistoricoService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.PortariaService;
import br.gov.ba.seia.service.ProcessoAtoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoAtoAmbientalService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.service.TransferenciaAmbientalService;
import br.gov.ba.seia.service.VwConsultaProcessoService;
import br.gov.ba.seia.service.facade.AutorizacaoManejoCabrucaServiceFacade;
import br.gov.ba.seia.service.facade.DetalharProcessoFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.ReportUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceUtil;

@Named("detalharProcessoController")
@ViewScoped
public class DetalharProcessoController {

	@EJB
	private ProcessoAtoService processoAtoService;
	
	@EJB
	private VwConsultaProcessoService vwConsultaProcessoService;
	
	@EJB
	private TipologiaService tipologiaService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private ParametroService parametroService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;
	
	@EJB
	private SolicitacaoAdministrativoAtoAmbientalService solicitacaoAdministrativoAtoAmbientalService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private BoletoPagamentoRequerimentoService boletoPagamentoRequerimentoService;	
	
	@EJB
	private FceServiceFacade fceServiceFacade;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private TransferenciaAmbientalService transferenciaAmbientalService;
	
	@EJB
	private PortariaService portariaService;
	
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	
	@EJB
	private FuncionarioService funcionarioService;
	
	@EJB
	private AtoDeclaratorioFacade atosDeclaratoriosFacade;
	
	@EJB
	private DeclaracaoQueimaControladaFacade declaracaoQueimaControladaFacade;
	
	@EJB
	private RFPFacade rfpFacade;
	
	@EJB
	private ProcessoReenquadramentoHistAtoServiceFacade processoReenquadramentoHistAtoServiceFacade;
	
	@EJB
	private AutorizacaoManejoCabrucaServiceFacade amcService;
	
	@EJB
	private PessoaJuridicaHistoricoService pessoaJuridicaHistoricoService;
	
	@EJB
	private FormarEquipeServiceFacade formarEquipeServiceFacade;
	
	@EJB
	private DetalharProcessoFacade detalharProcessoFacade;
	
	@EJB
	private DocumentoRequerimentoEmpreendimentoService documentoRequerimentoEmpreendimentoService;
	
	@EJB
	private DevedorReposicaoFlorestalService devedorReposicaoFlorestalService;
	
	@EJB
	private MemoriaCalculoDaeParcelaService memoriaCalculoDaeParcelaService;
	
	/**
	 * OBJETOS
	 */
	
	private VwConsultaProcesso vwProcesso;

	private DocumentoObrigatorio documentoObrigatorio;

	private DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento;

	private ArquivoProcesso arquivoProcesso;

	private Notificacao notificacaoEnviadaSelecionada;

	private Notificacao notificacaoReprovadaSelecionada;

	private Notificacao notificacaoParcialSelecionada;

	private ComprovantePagamento comprovantePagamento;

	private ComprovantePagamentoDae comprovantePagamentoDae;

	private String desTipologiaAto;

	private String desMotivoIsencaoBoleto;

	private List<PessoaJuridicaHistorico> listaPessoaJuridicaHistorico;

	private List<Fce> listaFce;
		
	private List<Portaria> listaPortaria;
	
	private String [] transferencia;
	
	private int activeIndex;

	private boolean visualizarDetalheProcessoTLA;

	private List<TransferenciaAmbientalDTO> transferenciaAmbientalDTO;
	
	private List<AtoDeclaratorio> listaAtoDeclaratorio;
	
	private ProcessoReenquadramento processoReenquadramento;
	
	private List<ProcessoAtoConcedido> listaProcessoAtoConcedido;
	
	private DevedorReposicaoFlorestal devedorReposicaoFlorestal;
	
	private List<MemoriaCalculoDaeParcela> memoriaCalculoDaeParcelaLista;
		

	public void onChangeAba(TabChangeEvent event) {
		Tab abaAtiva = event.getTab();
	
		if(abaAtiva.getId().equals("docsFormacao")) {
			carregarListaDocumentoObrigatorioFce();
			carregarListaAtoDeclaratorio();
			carregarListaDocumentoRequerimentoEmpreendimento();
			carregarReposicaoFlorestal();
			carregarDae();
		}
		else if(abaAtiva.getId().equals("docsApensados")) {
			carregarListaPortaria();
			carregarListaDadosConcedidosFce();
			carregarListaDadosConcedidosAmcAsv();
		}
	}

	private void carregarListaDadosConcedidosAmcAsv() {
		try {
			listaProcessoAtoConcedido = detalharProcessoFacade.listaProcessoConcedidoAprovadoNaAnaliseTecnicaAmcAsvBy(vwProcesso.getIdeProcesso());
			List<ProcessoAtoConcedido> listaTemp = new ArrayList<ProcessoAtoConcedido>();
			
			if(!Util.isNullOuVazio(listaProcessoAtoConcedido)){
				for (ProcessoAtoConcedido atoConcedido : listaProcessoAtoConcedido) {
					if (!Util.isNullOuVazio(listaTemp)){
						for (ProcessoAtoConcedido processoAtoConcedido : listaTemp) {
							if(!processoAtoConcedido.getIdeProcessoAto().getAtoAmbiental().equals(atoConcedido.getIdeProcessoAto().getAtoAmbiental())){
								listaTemp.add(atoConcedido);
							}
						}
					} else {
						listaTemp.add(atoConcedido);
					}
				}
			}
			listaProcessoAtoConcedido = listaTemp;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

	public StreamedContent getImprimirPortaria(Portaria portaria) {
		try {
			Map<String, Object> lParametros = new HashMap<String, Object>();
			Integer ideProcesso = portaria.getIdeProcesso().getIdeProcesso();
			lParametros.put("ideProcesso",ideProcesso);
			return new RelatorioUtil("portaria.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isRenderedPortaria() {	
		boolean isRender = false;
		try {
			isRender = !Util.isNullOuVazio(listaPortaria) && 
				controleTramitacaoService.buscarUltimoPorProcesso(new Processo(vwProcesso.getIdeProcesso())).getIdeStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.CONCLUIDO.getStatus());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return isRender;
	}
	
	public boolean isRenderedDocumentoFormacaoProcesso() {
		return !Util.isNullOuVazio(vwProcesso.getDocumentoObrigatorioRequerimentoList());
	}
	
	public boolean isRenderedDocumentoObrigatorioEnquadramento() {
		return !Util.isNullOuVazio(vwProcesso.getDocumentoObrigatorioReenquadramentoList());
	}
	
	public boolean isRenderedDocumentoRepresentacaoProcesso() {
		return !Util.isNullOuVazio(vwProcesso.getDocumentoRepresentacaoRequerimentoList());
	}

	public boolean isRenderedFCE() {
		return !Util.isNullOuVazio(listaFce);
	}
	
	public boolean isRenderedAmcAsv() {
		return !Util.isNullOuVazio(listaProcessoAtoConcedido);
	}

	public boolean isRenderedComprovantePagamento() {
		return !Util.isNullOuVazio(vwProcesso.getComprovantePagamentoList());
	}
	
	public boolean isRenderedComprovantePagamentoComplementar() {
		return !Util.isNullOuVazio(vwProcesso.getComprovantePagamentoComplementarList());
	}	

	public boolean isRenderedComprovantePagamentoDAE() {
		return !Util.isNullOuVazio(vwProcesso.getComprovantePagamentoDaeList());
	}

	public boolean isRenderedLocalizacaoGeografica() {
		return !Util.isNullOuVazio(vwProcesso.getPerguntas());
	}

	public boolean isRenderedAlteracaoRazaoSocial() {
		return !Util.isNullOuVazio(listaPessoaJuridicaHistorico);
	}
	
	public boolean isRenderedFormularioAtoDeclaratorio() {
		return !Util.isNullOuVazio(listaAtoDeclaratorio);
	}
	
	public boolean isRenderedBotaoComprovantePagamento() {
		for (ComprovantePagamento comprovantePagamento : vwProcesso.getComprovantePagamentoList()) {
			return !Util.isNullOuVazio(comprovantePagamento.getDtcValidacao());
		}
		return true;
	}
	
	public boolean isRenderedDocumentoIdentificacaoProcesso() {
		return !Util.isNullOuVazio(vwProcesso.getDocumentoIdentificacaoRequerimentoList());
	}
	
	public boolean isSomenteFceASV(){
		
		if(!Util.isNullOuVazio(listaFce) && listaFce.size()==1){
			
			for(Fce fce: listaFce){
				
				if(DocumentoObrigatorioEnum.FCE_ASV.getId().equals(fce.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
					return true;
				}
			}
		}
			
		return false;
		
	}
	
	public boolean isRendereDocumentoRequerimentoEmpreendimento(){
		return !Util.isNullOuVazio(vwProcesso.getDocumentoRequerimentoEmpreendimentoList());
	}
	
	public boolean isRenderedDAE() {
		return !Util.isNullOuVazio(this.memoriaCalculoDaeParcelaLista);
	}
	
	private void carregarListaPortaria() {
		listaPortaria = new ArrayList<Portaria>();
		try {
				Portaria portaria;
				portaria = portariaService.buscarPortariaByProcesso(new Processo(vwProcesso.getIdeProcesso()));
							
			if(!Util.isNullOuVazio(portaria)){
				listaPortaria.add(portaria);
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarListaDocumentoObrigatorioFce() {
		try {
			listaFce = fceServiceFacade.listarFceByIdeRequerimento(new Requerimento(vwProcesso.getIdeRequerimento()));
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarListaAtoDeclaratorio() {
		try {
			listaAtoDeclaratorio = atosDeclaratoriosFacade.listarAtoDeclaratorioBy(new Requerimento(vwProcesso.getIdeRequerimento()));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarListaDocumentoRequerimentoEmpreendimento(){
		
		try {
			vwProcesso.setDocumentoRequerimentoEmpreendimentoList(documentoRequerimentoEmpreendimentoService.listarDocumentoRequerimentoPorRequerimento(vwProcesso.getIdeRequerimento()));
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/**
	 * Método para carregar a lista de {@link Fce}s da {@link AnaliseTecnica} aprovada pelo coordenador.  
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @since 14/03/2016
	 */
	private void carregarListaDadosConcedidosFce() {
		try {
			listaFce = fceServiceFacade.listarFceAprovadoNaAnaliseTecnicaBy(vwProcesso.getIdeProcesso());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

	public StreamedContent imprimirRelatorio(Object object) {
		try {
			if(object instanceof Fce){
				return fceServiceFacade.getImprimirRelatorio((Fce) object);
				
			} else if(object instanceof AtoDeclaratorio){
				return imprimirRelatorioAtoDeclaratorio((AtoDeclaratorio) object);
				
			} else {
				JsfUtil.addErrorMessage("Não é possí­vel imprimir esse tipo de relatório.");
				return null;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private StreamedContent imprimirRelatorioAtoDeclaratorio(AtoDeclaratorio atoDeclaratorio) {
		try {
			return new ImpressoraAtoDeclaratorio().imprimirRelatorio(atoDeclaratorio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void visualizarFce(ActionEvent evt) {
		Fce fce = (Fce) evt.getComponent().getAttributes().get("fce");
		FceUtil.visualizarFce(new DadoConcedidoFceImpl(fce));
	}
	
	public void visualizarAmcAsv(ActionEvent evt) {
		
		ProcessoAtoConcedido processoAtoConcedido = (ProcessoAtoConcedido) evt.getComponent().getAttributes().get("amcAsv");
		ProcessoAto processoAto = processoAtoConcedido.getIdeProcessoAto();
		
		DadoConcedidoImpl dado = new DadoConcedidoImpl(processoAto);
		
		if(AtoAmbientalEnum.AMC.getId().equals(processoAtoConcedido.getIdeProcessoAto().getAtoAmbiental().getIdeAtoAmbiental())){
			
			AutorizacaoManejoCabrucaController autorizacaoManejoController = (AutorizacaoManejoCabrucaController) SessaoUtil.recuperarManagedBean("#{autorizacaoManejoCabrucaController}", AutorizacaoManejoCabrucaController.class);
			autorizacaoManejoController.load(dado, Boolean.TRUE);
		}else if(AtoAmbientalEnum.ASV.getId().equals(processoAtoConcedido.getIdeProcessoAto().getAtoAmbiental().getIdeAtoAmbiental())){
			
			if(!Util.isNullOuVazio(listaFce)){
				
				for(Fce fce:listaFce){
					
					if(DocumentoObrigatorioEnum.FCE_ASV.getId().equals(fce.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
						
						AsvDadosGeraisController asvDadosGeraisController = (AsvDadosGeraisController) SessaoUtil.recuperarManagedBean("#{asvDadosGeraisController}", AsvDadosGeraisController.class);
						
						asvDadosGeraisController.setIsNotFceASV(true);
						FceUtil.visualizarFce(fce);
								
					}
				}
			}
			
			DadoConcedidoAsvController dadoConcedidoController = (DadoConcedidoAsvController) SessaoUtil.recuperarManagedBean("#{dadoConcedidoAsvController}", DadoConcedidoAsvController.class);
			dadoConcedidoController.load(dado, Boolean.TRUE);
		}
		
		
	}
	
	public void visualizarAtoDeclaratorio(ActionEvent evt) {
		AtoDeclaratorio ato = (AtoDeclaratorio) evt.getComponent().getAttributes().get("ato");
		
		if(!Util.isNullOuVazio(ato) && !Util.isNullOuVazio(ato.getIdeDocumentoObrigatorio())) {
			if(ato.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.FORMULARIO_DIAP.getId())) {
				
				DeclaracaoIntervencaoAppController diapController = 
						(DeclaracaoIntervencaoAppController) SessaoUtil.recuperarManagedBean("#{declaracaoIntervencaoAppController}", DeclaracaoIntervencaoAppController.class);
				
				diapController.setRequerimento(ato.getIdeRequerimento());
				diapController.init();
				
				diapController.getDeclaracaoIntervencaoApp().getIdeAtoDeclaratorio().setDesabilitaTudo(true);
				
				Html.exibir("dialogDiap");
				
			} else if(ato.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.FORMULARIO_RFP.getId())) {
				
				RFPController rfpController = 
						(RFPController) SessaoUtil.recuperarManagedBean("#{registroFlorestaPlantadaController}", RFPController.class);
				
				rfpController.inicializarVariaveis();
				rfpController.getIdeRegistroFlorestaProducao().setIdeAtoDeclaratorio(ato);
				rfpController.setRequerimento(ato.getIdeRequerimento());
				rfpController.carregarListas();
				rfpController.carregar();
				rfpController.carregarImoveisArrendados();
				
				rfpController.getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio().setDesabilitaTudo(true);
				
				Html.exibir("dialogRegistroFlorestaPlantada");
				
			} else if(ato.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.FORMULARIO_DQC.getId())) {
				
				DeclaracaoQueimaControladaController dqcController = 
						(DeclaracaoQueimaControladaController) SessaoUtil.recuperarManagedBean("#{dqcController}", DeclaracaoQueimaControladaController.class); 
								
				dqcController.setDqc(declaracaoQueimaControladaFacade.buscarDeclaracaoQueimaControladaPorAtoDeclaratorio(ato));
				dqcController.setRequerimento(ato.getIdeRequerimento());
				dqcController.carregarListasDqc();
				
				dqcController.getDqc().getIdeAtoDeclaratorio().setDesabilitaTudo(true);
				
				Html.exibir("dialogDQC");
			}
		}
	}

	public String visualizarProcesso() {
		try {
			this.activeIndex = 0;
			
			if(vwProcesso != null) {
				if (!Util.isNullOuVazio(processoReenquadramento)){
					vwProcesso.setProcessoReenquadramento(processoReenquadramento);
				} else {
					processoReenquadramento = vwProcesso.getProcessoReenquadramento();
				}
				
				vwProcesso = vwConsultaProcessoService.getProcessoCompleto(vwProcesso);
				vwProcesso.setListProcessoAto(carregaProcessoAto(vwProcesso.getListProcessoAto()));
				vwProcesso.setListProcessoAtoReequadramento(carregaProcessoAto(vwProcesso.getListProcessoAtoReequadramento()));
				
				Collection<ProcessoAto> listProcessoAto = formarEquipeServiceFacade.ajustarProcessoAto(vwProcesso.getListProcessoAto());
				vwProcesso.setListProcessoAto(listProcessoAto);
				
				listaPessoaJuridicaHistorico = pessoaJuridicaHistoricoService.listarPessoaJuridicaHistoricoByProcesso(vwProcesso.getIdeProcesso());
				transferenciaAmbientalDTO = getTransferenciaAmbiental(new Processo(vwProcesso.getIdeProcesso()));
	
				carregarListaPortaria();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
		return null;
	}
	public Collection<ProcessoAto> carregaProcessoAto(Collection<ProcessoAto> listaProcessoAto) throws Exception{
			
		Collection<Processo> processoTransferidos = new ArrayList<Processo>();
		Collection<Processo> processoNaoTransferidos = new ArrayList<Processo>();
		
		for (ProcessoAto pa : listaProcessoAto) {
			pa.setStatus(getNomStatus(pa));	

			if(processoAtoPossuiTransferenciaAmbiental(pa)){
			
				Collection<Processo> processoEnvolvidos = new ArrayList<Processo>();
				Collection<String> processoEmList = getHistNumProcesso(pa);

				for (String numProcesso : processoEmList) {
					processoEnvolvidos.add(processoService.buscarProcessoPorNumero(numProcesso));
				}
				
				for (Processo processo : processoEnvolvidos) {
					for (ProcessoAto processoAto : processoAtoService.listarAtosPorProcesso(processo.getIdeProcesso())) {
						
						if(transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(processoAto.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.DEFERIDO.getId())){
							if(!pa.getProcessoDeferido().contains(processo)){
								pa.getProcessoDeferido().add(processo.getClone());
							}
						}else if(
							!transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(processoAto.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.DEFERIDO.getId())&&
							!transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(processoAto.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.NULO.getId())){
								if(!pa.getProcessoNaoDeferido().contains(processo)){
									pa.getProcessoNaoDeferido().add(processo.getClone());
								}
						}
					}
				}
				
				processoTransferidos.clear();
				processoNaoTransferidos.clear();
					
				if (!pa.getListProcessoNaoDeferidos().equals("Existe transferência de licença ambiental (TLA) em trâmite para esse ato. ")){
					pa.setTlaStatusDeferido(Boolean.TRUE);
				}
			}

			if (!Util.isNullOuVazio(processoReenquadramento)){
				String mensagem = processoReenquadramentoHistAtoServiceFacade.mensagemObservaoReenquadramento(processoReenquadramento, pa.getAtoAmbiental(), pa.getTipologia());
				if (!Util.isNullOuVazio(mensagem)){
					pa.setObservacaoAtoProcesso(mensagem);
					pa.setTlaStatusDeferido(Boolean.TRUE);
				}
			}
		}
		return listaProcessoAto;
	}
	
	public List<String> getHistNumProcesso(ProcessoAto pa) throws Exception{
		List<String> retorno = new ArrayList<String>();
		for (Processo processo : transferenciaAmbientalService.getHistNumProcesso(pa)) {
			retorno.add(processo.getNumProcesso());
		}
		return retorno;
	}
	
	public String getNomStatus(ProcessoAto pa) throws Exception{
		Integer ideTipoStatus = transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(pa.getIdeProcessoAto());
		if(ideTipoStatus!=0){
			return transferenciaAmbientalService.carregarStatusProcessoAto(new StatusProcessoAto(ideTipoStatus)).getNomStatusProcessoAto();
		}
		return "";
	}
	
	public StatusProcessoAto buscarStatusProcessoAto(StatusProcessoAto statusProcessoAto)throws Exception{
		return transferenciaAmbientalService.carregarStatusProcessoAto(statusProcessoAto);
	}
	
	public StatusProcessoAto getStatusProcessoAto(ProcessoAto pa) throws Exception{	
		return  transferenciaAmbientalService.carregarStatusProcessoAto(pa.getIdeProcessoAto());
	}
	
	public Boolean processoAtoTLAdeferido(Integer ideProcessoTLA) throws Exception{
		return transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(ideProcessoTLA).equals(StatusProcessoAtoEnum.DEFERIDO.getId());
	}
	
	public Boolean processoAtoPossuiTransferenciaAmbiental(ProcessoAto pa) throws Exception{
		return !Util.isNullOuVazio(transferenciaAmbientalService.isProcessoAtoTransferenciaAmbiental(pa));
	}
	
	public List<TransferenciaAmbientalDTO> getTransferenciaAmbiental(Processo processoTla){
		
		List<TransferenciaAmbientalDTO> listaTransferenciaAmbientalDTO = new ArrayList<TransferenciaAmbientalDTO>();
		TransferenciaAmbientalDTO transferenciaAmbientalDTO;
		Processo processoOriginal = null;
		
		try {
			
			if(visualizarTLA(processoTla.getIdeProcesso())){
				visualizarDetalheProcessoTLA = true;
				
				if(processoAtoService.isProcessoTla(processoTla) ){
					processoOriginal = processoTla;					
					while(processoAtoService.isProcessoTla(processoOriginal) && processoOriginal!=null){
						processoOriginal = solicitacaoAdministrativoService.getProcessoPai(processoOriginal);
					}
				}					
				
				if(transferenciaAmbientalService.isTransferenciaComOrigem(processoTla)){
					
					for (ProcessoAto processoAto : transferenciaAmbientalService.getListProcessoAtoRecebidos(processoTla)) {	
										
						transferenciaAmbientalDTO = new TransferenciaAmbientalDTO();
						transferenciaAmbientalDTO.setProcesso(processoOriginal);
						transferenciaAmbientalDTO.setAtoAmbiental(processoAto.getAtoAmbiental());
						transferenciaAmbientalDTO.setReceptor(transferenciaAmbientalService.getPessoaReceptor(processoTla.getIdeProcesso()));
						transferenciaAmbientalDTO.setCedente(transferenciaAmbientalService.getPessoaCedente(processoTla.getIdeProcesso()));
						
						if(!Util.isNullOuVazio(processoAto.getTipologia())){
							transferenciaAmbientalDTO.setTipologia(processoAto.getTipologia());
						}else{
							transferenciaAmbientalDTO.setTipologia(new Tipologia());							
						}
						
						listaTransferenciaAmbientalDTO.add(transferenciaAmbientalDTO);
					}
					
				}else{
					
					Processo processo = processoService.carregarProcesso(processoTla.getIdeProcesso());
					SolicitacaoAdministrativo solicitacaoAdminstrativo = requerimentoService.getSolicitacaoAdminstrativo(processo.getIdeRequerimento(),TipoSolicitacaoEnum.TRANSFERENCIA_LICENCA_AMBIENTAL);

					if (!Util.isNullOuVazio(solicitacaoAdminstrativo) && Util.isNullOuVazio(solicitacaoAdminstrativo.getSolicitacaoAdminstrativoAtoAmbientalCollection())) {
						solicitacaoAdminstrativo.setSolicitacaoAdminstrativoAtoAmbientalCollection(solicitacaoAdministrativoAtoAmbientalService.listaAtoBySolicitacaoComTipologia(solicitacaoAdminstrativo));
					}					
					
					if (!Util.isNullOuVazio(solicitacaoAdminstrativo) && !Util.isNullOuVazio(solicitacaoAdminstrativo.getSolicitacaoAdminstrativoAtoAmbientalCollection())) {

						for(SolicitacaoAdministrativoAtoAmbiental sol : solicitacaoAdminstrativo.getSolicitacaoAdminstrativoAtoAmbientalCollection()) {
							
							transferenciaAmbientalDTO = new TransferenciaAmbientalDTO();
							
							transferenciaAmbientalDTO.setProcesso(processoOriginal);
							transferenciaAmbientalDTO.setAtoAmbiental(sol.getIdeAtoAmbiental());

							if (Util.isNullOuVazio(sol.getIdeTipologia())) {
								transferenciaAmbientalDTO.setTipologia(sol.getIdeTipologia());
							} else {
								transferenciaAmbientalDTO.setTipologia(new Tipologia());
							}
							transferenciaAmbientalDTO.setProcesso(processoOriginal);

							transferenciaAmbientalDTO.setReceptor(transferenciaAmbientalService.getPessoaReceptor(processoTla.getIdeProcesso()));
							transferenciaAmbientalDTO.setCedente(transferenciaAmbientalService.getPessoaCedente(processoTla.getIdeProcesso()));
											
							listaTransferenciaAmbientalDTO.add(transferenciaAmbientalDTO);
						}	
				}	
			}
						
			}else{
				visualizarDetalheProcessoTLA = false;
			}
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return listaTransferenciaAmbientalDTO;
	}

	private boolean visualizarTLA(int ideProcesso) throws Exception {

		boolean possuiAtoDeferido = false;
		for (ProcessoAto pa : processoAtoService.listarAtosPorProcesso(ideProcesso)) {
			if(transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(pa.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.DEFERIDO.getId())){
				possuiAtoDeferido = true;
			}
		}

		return transferenciaAmbientalService.isProcessoPossuiSolicitacaoAmbiental(ideProcesso, TipoSolicitacaoEnum.TLA.getId()) && possuiAtoDeferido;
	}

	public String getDesTipologiaAto() {
		return desTipologiaAto;
	}

	public String setDesTipologiaAto(Integer ideAtoAmbiental, Integer ideProcesso) {
		ProcessoAto processoAto = new ProcessoAto();
		processoAto.setProcesso(new Processo(ideProcesso));
		processoAto.setAtoAmbiental(new AtoAmbiental(ideAtoAmbiental));

		Exception erro = null;
		try{
			return desTipologiaAto=tipologiaService.buscarTipologiaDoAtoAmbiental(processoAto).getDesTipologia();
		}
		catch(NoResultException n){
			return desTipologiaAto=null;
		}
		catch (Exception e) {
			erro =e;
			return desTipologiaAto=null;
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

	}
	
	public List<MemoriaCalculoDaeParcela> carregarDae() {
		
		return this.memoriaCalculoDaeParcelaLista = memoriaCalculoDaeParcelaService.obterMemoriaCalculoDaeParcelaPorIdeRequerimento(getVwProcesso().getIdeRequerimento());
	}
		
	public boolean isRenderedDocsApensados(){
		
		boolean processoStatusFluxoConcluido = false;
		
		try {
			if(!Util.isNullOuVazio(vwProcesso)  && !Util.isNullOuVazio(vwProcesso.getIdeProcesso())){
				processoStatusFluxoConcluido  = processoService.isProcessoNoStatus(vwProcesso.getIdeProcesso(), StatusFluxoEnum.CONCLUIDO.getStatus());				
			}
		} catch (Exception e) {	
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		if(Util.isNullOuVazio(processoStatusFluxoConcluido)){
			processoStatusFluxoConcluido = false;
		}
	
		if(processoStatusFluxoConcluido || !ContextoUtil.getContexto().isUsuarioExterno()) {
			return true;
		}
		else{
			return false;
		}
	}
	
	private void carregarReposicaoFlorestal(){
		
		try {
			this.devedorReposicaoFlorestal = devedorReposicaoFlorestalService.obterDevedorPorRequerimento(this.vwProcesso.getIdeRequerimento());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getSimpleName(), Level.ERROR, e);
		}
	}

	public boolean isUsuarioExterno() {
		return ContextoUtil.getContexto().isUsuarioExterno();
	}

	public VwConsultaProcesso getVwProcesso() {
		if (Util.isNullOuVazio(vwProcesso)) {
			vwProcesso = new VwConsultaProcesso();
		}
		return vwProcesso;
	}

	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public DocumentoObrigatorio getDocumentoObrigatorio() {
		return documentoObrigatorio;
	}

	public void setDocumentoObrigatorio(DocumentoObrigatorio documentoObrigatorio) {
		this.documentoObrigatorio = documentoObrigatorio;
	}

	public ArquivoProcesso getArquivoProcesso() {
		return arquivoProcesso;
	}

	public void setArquivoProcesso(ArquivoProcesso arquivoProcesso) {
		this.arquivoProcesso = arquivoProcesso;
	}

	public StreamedContent getDocumentoObrigatorioSC() {
		try {
			return gerenciaArquivoService.getContentFile(documentoObrigatorioRequerimento.getDscCaminhoArquivo());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getDocumentoIdentificacaoRequerimentoSC(DocumentoIdentificacaoRequerimento dir) {
		try {
			return gerenciaArquivoService.getContentFile(dir.getDscCaminhoArquivo());
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent getDocumentoRepresentacaoRequerimentoSC(DocumentoRepresentacaoRequerimento drr) {
		try {
			return gerenciaArquivoService.getContentFile(drr.getDscCaminhoArquivo());
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getArquivoProcessoSC() {
		try {
			return gerenciaArquivoService.getContentFile(arquivoProcesso.getDscCaminhoArquivo());
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent getDocumentoRequerimentoEmpreendimentoDownload(DocumentoRequerimentoEmpreendimento doc){
		try {
			return gerenciaArquivoService.getContentFile(doc.getDscCaminhoArquivo());
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent getDocumentoParecerTecnico() {
		try {
			return gerenciaArquivoService.getContentFile(devedorReposicaoFlorestal.getDscCaminhoParecerTecnico());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public DocumentoObrigatorioRequerimento getDocumentoObrigatorioRequerimento() {
		return documentoObrigatorioRequerimento;
	}

	public void setDocumentoObrigatorioRequerimento(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) {
		this.documentoObrigatorioRequerimento = documentoObrigatorioRequerimento;
	}

	public Notificacao getNotificacaoEnviadaSelecionada() {
		return notificacaoEnviadaSelecionada;
	}

	public void setNotificacaoEnviadaSelecionada(Notificacao notificacaoEnviadaSelecionada) {
		this.notificacaoEnviadaSelecionada = notificacaoEnviadaSelecionada;
	}

	public Notificacao getNotificacaoReprovadaSelecionada() {
		return notificacaoReprovadaSelecionada;
	}

	public void setNotificacaoReprovadaSelecionada(Notificacao notificacaoReprovadaSelecionada) {
		this.notificacaoReprovadaSelecionada = notificacaoReprovadaSelecionada;
	}

	public Notificacao getNotificacaoParcialSelecionada() {
		return notificacaoParcialSelecionada;
	}

	public void setNotificacaoParcialSelecionada(Notificacao notificacaoParcialSelecionada) {
		this.notificacaoParcialSelecionada = notificacaoParcialSelecionada;
	}

	public boolean isLac(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento){
		return this.isLacErb(documentoObrigatorioRequerimento) || this.isLacPosto(documentoObrigatorioRequerimento);
	}

	public boolean isVerificaExistenciaArquivo(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) {
		if(!Util.isNullOuVazio(documentoObrigatorioRequerimento) && !Util.isNullOuVazio(documentoObrigatorioRequerimento.getDscCaminhoArquivo())) {
			return Util.verificaExistenciaArquivo(documentoObrigatorioRequerimento.getDscCaminhoArquivo());
		} else {
			return false;
		}
	}

	public boolean isLacErb(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) {
		Boolean isValido = Boolean.FALSE;
		DocumentoObrigatorio documentoObrigatorio = null;

		if(!Util.isNull(documentoObrigatorioRequerimento.getIdeDocumentoAto())
				&& !Util.isNull(documentoObrigatorioRequerimento.getIdeDocumentoAto().getIdeDocumentoObrigatorio())) {

			documentoObrigatorio = documentoObrigatorioRequerimento.getIdeDocumentoAto().getIdeDocumentoObrigatorio();

		} else if(!Util.isNull(documentoObrigatorioRequerimento.getIdeDocumentoObrigatorio())) {
			documentoObrigatorio = documentoObrigatorioRequerimento.getIdeDocumentoObrigatorio();
		}


		Exception erro = null;

		try {
			if (!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())) {
				if (Util.isNullOuVazio(documentoObrigatorio.getDscCaminhoArquivo()) && documentoObrigatorio.getIdeDocumentoObrigatorio().intValue()
						== Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_ERB).getDscValor())) {

					isValido = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

		return isValido;
	}

	public boolean isLacPosto(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) {
		Boolean isValido = Boolean.FALSE;
		DocumentoObrigatorio documentoObrigatorio = null;

		if(!Util.isNull(documentoObrigatorioRequerimento) && !Util.isNull(documentoObrigatorioRequerimento.getIdeDocumentoAto())
				&& !Util.isNull(documentoObrigatorioRequerimento.getIdeDocumentoAto().getIdeDocumentoObrigatorio())) {

			documentoObrigatorio = documentoObrigatorioRequerimento.getIdeDocumentoAto().getIdeDocumentoObrigatorio();
		} else if(!Util.isNull(documentoObrigatorioRequerimento) && !Util.isNull(documentoObrigatorioRequerimento.getIdeDocumentoObrigatorio())) {
			documentoObrigatorio = documentoObrigatorioRequerimento.getIdeDocumentoObrigatorio();
		}

		Exception erro = null;

		try {
			if (!Util.isNullOuVazio(documentoObrigatorio) && !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())) {
				if (Util.isNullOuVazio(documentoObrigatorio.getDscCaminhoArquivo())
						&& documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_POSTO).getDscValor())) {
					isValido = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

		return isValido;
	}

	public VwConsultaProcessoService getVwConsultaProcessoService() {
		return vwConsultaProcessoService;
	}

	public void setVwConsultaProcessoService(VwConsultaProcessoService vwConsultaProcessoService) {
		this.vwConsultaProcessoService = vwConsultaProcessoService;
	}

	public String formatarNumero(BigDecimal pValor){
		return Util.getDecimalFormatPtBr().format(pValor);
	}

	public String valorBoletoSomadoEFormatado(BoletoPagamentoRequerimento bpr) {
		BigDecimal valor = new BigDecimal(0);

		if(!Util.isNullOuVazio(bpr.getValBoleto())) {
			valor = bpr.getValBoleto();
		}
		if(!Util.isNullOuVazio(bpr.getValBoletoOutorga())) {
			valor = valor.add(bpr.getValBoletoOutorga());
		}

		DecimalFormat df = Util.getDecimalFormatPtBr();
		return df.format(valor);
	}

	public StreamedContent getComprovantePagamentoSC() {
		try {
			if(isVerificaExistenciaComprovante()) {
				return gerenciaArquivoService.getContentFile(comprovantePagamento.getDscCaminhoArquivo());
			}
			else {
				return null;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public ComprovantePagamento getComprovantePagamento() {
		return comprovantePagamento;
	}

	public void setComprovantePagamento(ComprovantePagamento comprovantePagamento) {
		this.comprovantePagamento = comprovantePagamento;
	}

	public ComprovantePagamentoDae getComprovantePagamentoDae() {
		return comprovantePagamentoDae;
	}

	public void setComprovantePagamentoDae(ComprovantePagamentoDae comprovantePagamentoDae) {
		this.comprovantePagamentoDae = comprovantePagamentoDae;
	}

	public StreamedContent getComprovantePagamentoDaeSC() {
		try {
			return gerenciaArquivoService.getContentFile(comprovantePagamentoDae.getDscCaminhoArquivo());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getGerarResumoRequerimentoPdf() {

		try {
			
			Requerimento req = requerimentoService.buscarEntidadeCarregadaPorId(new Requerimento(vwProcesso.getIdeRequerimento()));
			
			if (!Util.isNull(req) && !Util.isNullOuVazio(req.getDesCaminhoResumoOriginal())) {
				return new RelatorioUtil().downloadArquivoRelatorio(req.getDesCaminhoResumoOriginal());
			}
			else {
				List<Imovel> listaImovel = null;
				Map<String, Object> params = new HashMap<String, Object>();
				Integer empreendimento = vwProcesso.getIdeEmpreendimento();
				
				if(!Util.isNull(empreendimento)) {
					listaImovel = (List<Imovel>) empreendimentoService.obterTipoImovelPorIdeEmpreendimento(empreendimento).getImovelCollection();
					params.put("nom_tipo_imovel", listaImovel.isEmpty() ? "Não Identificado" : listaImovel.get(0).getIdeTipoImovel().getNomTipoImovel());
				}
				
				Integer ideRequerimento = vwProcesso.getIdeRequerimento();
				params.put("ide_requerimento", ideRequerimento);
				
				if(this.requerimentoService.isRequerimentoAntigo(ideRequerimento)) {
					RelatorioUtil lRelatorio = new RelatorioUtil("resumo_requerimento.jasper", params);
					return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
				}
				else{
					return  new ReportUtil().imprimir("resumo_requerimento.jasper", params);
				}
			}
		}
		catch (Exception e) {
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public StreamedContent getImprimirProtocoloProcesso() {
		RelatorioUtil lRelatorio = new RelatorioUtil("protocolo_formacao_processo.jasper", new HashMap<String, Object>());
		Exception erro = null;
		try {
			List<VwConsultaProcesso> dataSource = new ArrayList<VwConsultaProcesso>();
			dataSource.add(vwProcesso);
			return lRelatorio.gerarRelatorioComDataSource(RelatorioUtil.RELATORIO_PDF, true, dataSource);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

		return null;
	}

	public boolean isBoletoIsento() {
		Exception erro = null;
		boolean retorno = false;

		try {
			if(!Util.isNullOuVazio(vwProcesso)) {
				List<BoletoPagamentoRequerimento> lBpr = boletoPagamentoRequerimentoService.carregarListaBoletoByRequerimento(vwProcesso.getIdeRequerimento());
				BoletoPagamentoRequerimento bpr = null;
				if(!Util.isNullOuVazio(lBpr)){
					bpr = lBpr.get(0);
				}

				if(Util.isNullOuVazio(bpr)) {
					retorno = true;
				} else if(!Util.isNullOuVazio(bpr) && !Util.isNullOuVazio(bpr.getIndIsento()) && bpr.getIndIsento()) {

					if(!Util.isNullOuVazio(bpr.getIdeMotivoIsencaoBoleto())	&& !Util.isNullOuVazio(bpr.getIdeMotivoIsencaoBoleto().getDscMotivoIsencaoBoleto())) {
						desMotivoIsencaoBoleto = bpr.getIdeMotivoIsencaoBoleto().getDscMotivoIsencaoBoleto();
					}

					retorno = true;
				}
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

		return retorno;
	}

	/**
	 * Requerimentos antigos quando eram "isentos" nÃ£o havia campo para escrever o motivo.
	 * @author eduardo
	 * @return !Util.isNullOuVazio(desMotivoIsencaoBoleto)
	 */
	public boolean isExisteMotivoIsencaoBoleto(){
		return !Util.isNullOuVazio(desMotivoIsencaoBoleto);
	}
	
	public boolean isRenderedListaProcessoAtoReenquadramento(){
		return !Util.isNullOuVazio(vwProcesso.getListProcessoAtoReequadramento());
	}
	
	public boolean isRenderedDownload(){
		
		if(!Util.isNullOuVazio(listaPortaria)){
			
			for(Portaria portaria : listaPortaria){
				
				if(Util.isNullOuVazio(portaria.getTextoPortaria())){
					return false;
				}else{
					return true;
				}
			}
		}
		return false;
	}

	public String getDesMotivoIsencaoBoleto() {

		return desMotivoIsencaoBoleto;
	}

	public void setDesMotivoIsencaoBoleto(String desMotivoIsencaoBoleto) {

		this.desMotivoIsencaoBoleto = desMotivoIsencaoBoleto;
	}

	public boolean isVerificaExistenciaComprovante() {
		if(!Util.isNullOuVazio(comprovantePagamento) && !Util.isNullOuVazio(comprovantePagamento.getDscCaminhoArquivo())) {
			if(!Util.verificaExistenciaArquivo(comprovantePagamento.getDscCaminhoArquivo())){
				JsfUtil.addErrorMessage("O Arquivo não pode ser encontrado");
				return false;
			}
			return true;
			
		} else {
			return false;
		}
	}

	/**
	 * @return the listaPessoaJuridicaHistorico
	 */
	public List<PessoaJuridicaHistorico> getListaPessoaJuridicaHistorico() {
		return listaPessoaJuridicaHistorico;
	}

	/**
	 * @param listaPessoaJuridicaHistorico the listaPessoaJuridicaHistorico to set
	 */
	public void setListaPessoaJuridicaHistorico(List<PessoaJuridicaHistorico> listaPessoaJuridicaHistorico) {
		this.listaPessoaJuridicaHistorico = listaPessoaJuridicaHistorico;
	}

	public List<Fce> getListaFce() {
		return listaFce;
	}

	public String [] getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(String [] transferencia) {
		this.transferencia = transferencia;
	}


	public List<TransferenciaAmbientalDTO> getTransferenciaAmbientalDTO() {
		return transferenciaAmbientalDTO;
	}

	public void setTransferenciaAmbientalDTO(List<TransferenciaAmbientalDTO> transferenciaAmbientalDTO) {
		this.transferenciaAmbientalDTO = transferenciaAmbientalDTO;
	}

	public boolean isVisualizarDetalheProcessoTLA() {
		return visualizarDetalheProcessoTLA;
	}

	public void setVisualizarDetalheProcessoTLA(boolean visualizarDetalheProcessoTLA) {
		this.visualizarDetalheProcessoTLA = visualizarDetalheProcessoTLA;
	}

	public List<Portaria> getListaPortaria() {
		return listaPortaria;
	}

	public void setListaPortaria(List<Portaria> portaria) {
		this.listaPortaria = portaria;
	}

	public List<AtoDeclaratorio> getListaAtoDeclaratorio() {
		return listaAtoDeclaratorio;
	}

	public void setListaAtoDeclaratorio(List<AtoDeclaratorio> listaAtoDeclaratorio) {
		this.listaAtoDeclaratorio = listaAtoDeclaratorio;
	}

	public ProcessoReenquadramento getProcessoReenquadramento() {
		return processoReenquadramento;
	}

	public void setProcessoReenquadramento(
			ProcessoReenquadramento processoReenquadramento) {
		this.processoReenquadramento = processoReenquadramento;
	}

	public List<ProcessoAtoConcedido> getListaProcessoAtoConcedido() {
		return listaProcessoAtoConcedido;
	}

	public void setListaProcessoAtoConcedido(
			List<ProcessoAtoConcedido> listaProcessoAtoConcedido) {
		this.listaProcessoAtoConcedido = listaProcessoAtoConcedido;
	}

	public DevedorReposicaoFlorestal getDevedorReposicaoFlorestal() {
		return devedorReposicaoFlorestal;
	}

	public void setDevedorReposicaoFlorestal(
			DevedorReposicaoFlorestal devedorReposicaoFlorestal) {
		this.devedorReposicaoFlorestal = devedorReposicaoFlorestal;
	}

	public List<MemoriaCalculoDaeParcela> getMemoriaCalculoDaeParcelaLista() {
		return memoriaCalculoDaeParcelaLista;
	}

	public void setMemoriaCalculoDaeParcelaLista(List<MemoriaCalculoDaeParcela> memoriaCalculoDaeParcelaLista) {
		this.memoriaCalculoDaeParcelaLista = memoriaCalculoDaeParcelaLista;
	}
	
}