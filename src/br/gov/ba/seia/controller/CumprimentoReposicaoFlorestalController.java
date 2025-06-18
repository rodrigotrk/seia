package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.CumprimentoReposicaoFlorestalDTO;
import br.gov.ba.seia.dto.MemoriaCalculoDTO;
import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.dto.UnidadeMedidaCalculoDTO;
import br.gov.ba.seia.entity.Bioma;
import br.gov.ba.seia.entity.ConsumidorReposicaoFlorestal;
import br.gov.ba.seia.entity.CumprimentoReposicaoFlorestal;
import br.gov.ba.seia.entity.DetentorReposicaoFlorestal;
import br.gov.ba.seia.entity.DevedorReposicaoFlorestal;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.NumeroCarReposicaoFlorestal;
import br.gov.ba.seia.entity.OrgaoEmissorAuto;
import br.gov.ba.seia.entity.PagamentoReposicaoFlorestal;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TipoVolumeFlorestalRemanescente;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.enumerator.TipoBiomaEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.UnidadeMedidaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BiomaService;
import br.gov.ba.seia.service.ConsumidorReposicaoFlorestalService;
import br.gov.ba.seia.service.CumprimentoReposicaoFlorestalService;
import br.gov.ba.seia.service.DetentorReposicaoFlorestalService;
import br.gov.ba.seia.service.DevedorReposicaoFlorestalService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.OrgaoEmissorAutoService;
import br.gov.ba.seia.service.PagamentoReposicaoFlorestalService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.TipoVolumeFlorestalRemanescenteService;
import br.gov.ba.seia.service.UnidadeMedidaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("cumprimentoReposicaoFlorestalController")
@ViewScoped
public class CumprimentoReposicaoFlorestalController {
	
	@EJB
	private CumprimentoReposicaoFlorestalService cumprimentoReposicaoFlorestalService;
	
	@EJB
	private PagamentoReposicaoFlorestalService pagamentoReposicaoFlorestalService;
	
	@EJB
	private TipoVolumeFlorestalRemanescenteService tipoVolumeFlorestalRemanescenteService;
	
	@EJB
	private DetentorReposicaoFlorestalService detentorReposicaoFlorestalService;
	
	@EJB
	private ConsumidorReposicaoFlorestalService consumidorReposicaoFlorestalService;
	
	@EJB
	private DevedorReposicaoFlorestalService devedorReposicaoFlorestalService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private OrgaoEmissorAutoService orgaoEmissorAutoService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private BiomaService biomaService;
	
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private UnidadeMedidaService unidadeMedidaService;
	
	private Requerimento requerimento;
	private RequerimentoPessoa reqPessoaRequerente;
	private RequerimentoPessoa reqPessoaSolicitante;
	
	private List<PagamentoReposicaoFlorestal> listaPagamentoReposicaoFlorestal;
	private List<PagamentoReposicaoFlorestal> listaPagamentoReposicaoFlorestalFilho;
	private List<TipoVolumeFlorestalRemanescente> listaTipoVolumeFlorestalRemanescente;
	private List<OrgaoEmissorAuto> listaOrgaoEmissorAuto;
	private List<Bioma> listaBioma;
	private List<Municipio> listaMunicipio;
	private List<UnidadeMedida> listaUnidadeMedida;
	
	private CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO;
	private String tipoAtoRelacionado;
	
	private RequerimentoDTO requerimentoDTO;
	private boolean desabilitarTudo;
	private NumeroCarReposicaoFlorestal numeroCarReposicaoFlorestal;
	private Date dataAtual;
	
	private List<MemoriaCalculoDTO> listaMemoriaCalculoDTO;
	private List<String> listaLegenda;
	
	@PostConstruct
	public void init() {
		try {
			dataAtual = new Date();
			Pessoa solicitante = ContextoUtil.getContexto().getSolicitanteRequerimento();
			reqPessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente();
			reqPessoaSolicitante = ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante();	
			
			// Caso o usuário clique em VISUALIZAR ou EDITAR na tela de CONSULTA.
			final Object temp = ContextoUtil.getContexto().getObject();
			
			if (temp != null && temp instanceof RequerimentoDTO) {
				requerimentoDTO = ((RequerimentoDTO) temp);
				requerimento = requerimentoDTO.getRequerimento();
				desabilitarTudo = requerimentoDTO.isVisualizar();
			}
			
			limpar();
			carregarFomulario();
			
			RequerimentoPessoa requerimentoPessoa = null;
			
			if(!Util.isNullOuVazio(requerimento)) {
				carregarDados();
				
			} else if (!Util.isNull(solicitante)) {
				requerimentoPessoa = requerimentoService.buscarRequerimentoRepflorPorSolicitante(reqPessoaRequerente.getPessoa(), new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId()));
				
				if (Util.isNull(requerimentoPessoa)) {
					requerimento = cumprimentoReposicaoFlorestalService.salvarNovoRequerimentoCRF(reqPessoaRequerente.getPessoa());
				} else {
					requerimento = requerimentoPessoa.getRequerimento();
					carregarDados();
				}
			}
			
			ContextoUtil.getContexto().setObject(null);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void limpar() {
		if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO)) {
			cumprimentoReposicaoFlorestalDTO = new CumprimentoReposicaoFlorestalDTO();
		} else {
			cumprimentoReposicaoFlorestalDTO = new CumprimentoReposicaoFlorestalDTO(cumprimentoReposicaoFlorestalDTO.getCumprimentoReposicaoFlorestal(), cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal(), cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalAnterior());
		}
		listaMemoriaCalculoDTO = null;
	}
	
	public void carregarFomulario() throws Exception {
		limpar();
		listaPagamentoReposicaoFlorestal = pagamentoReposicaoFlorestalService.listarPagamentoReposicaoFlorestalPai();
		
		if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal())) {
			if (isDetentor()) {
				carregarDadosFilho();
			}
			else if (isConsumidor()) {
				carregarUnidadeMedida();
			}
			else if (isDevedor()) {
				carregarDevedor();
			}
			calcularValorPecuniario();
			
			Html.atualizar("formReposicaoFlorestal");
		}
	}
	
	public void carregarDevedor()  {
		listaOrgaoEmissorAuto = orgaoEmissorAutoService.listarTodosOrgaoEmissorAuto();
		listaBioma = biomaService.obterBiomaPorTipo(TipoBiomaEnum.TERRESTRE.getId());
		listaMunicipio = municipioService.filtrarListaMunicipiosDaBahia();
	}
	
	public void carregarDados() throws Exception {
		CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal = cumprimentoReposicaoFlorestalService.obterCumprimentoReposicaoFlorestalPorRequerimento(requerimento);
		
		if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestal)) {
			cumprimentoReposicaoFlorestalDTO.setCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal);
			cumprimentoReposicaoFlorestalDTO.setIndCiente(cumprimentoReposicaoFlorestal.getIndCiente());
			cumprimentoReposicaoFlorestalDTO.setPagamentoReposicaoFlorestal(cumprimentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal());
			cumprimentoReposicaoFlorestalDTO.setPagamentoReposicaoFlorestalAnterior(cumprimentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal());
			
			if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestalPai())) {
				cumprimentoReposicaoFlorestalDTO.setPagamentoReposicaoFlorestal(cumprimentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestalPai());
				cumprimentoReposicaoFlorestalDTO.setPagamentoReposicaoFlorestalFilho(cumprimentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal());
			}
			
			cumprimentoReposicaoFlorestalDTO.setValorPecuniario(cumprimentoReposicaoFlorestal.getVlrPecuniario().doubleValue());
			
			if (isDetentor() || isAML() || isASV() || isReconhecimentoFlorestalRemanescente()) {
				cumprimentoReposicaoFlorestalDTO.setPagamentoReposicaoFlorestal(cumprimentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestalPai());
				cumprimentoReposicaoFlorestalDTO.setPagamentoReposicaoFlorestalFilho(cumprimentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal());
				carregarDadosFilho();
				gerarTipoAtoRelacionado();
				
				DetentorReposicaoFlorestal detentorReposicaoFlorestal = detentorReposicaoFlorestalService.obterDetentorReposicaoFlorestalPorCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal.getIdeCumprimentoReposicaoFlorestal());
				cumprimentoReposicaoFlorestalDTO.setDetentorReposicaoFlorestal(detentorReposicaoFlorestal);
				
				calcular(detentorReposicaoFlorestal.getIdeUnidadeMedida(), detentorReposicaoFlorestal.getVolumeAutorizado().doubleValue());
			} 
			else if (isConsumidor()) {
				carregarUnidadeMedida();
				ConsumidorReposicaoFlorestal consumidorReposicaoFlorestal = consumidorReposicaoFlorestalService.obterConsumidorReposicaoFlorestalPorCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal.getIdeCumprimentoReposicaoFlorestal());
				cumprimentoReposicaoFlorestalDTO.setConsumidorReposicaoFlorestal(consumidorReposicaoFlorestal);
				
				calcular(consumidorReposicaoFlorestal.getIdeUnidadeMedida(), consumidorReposicaoFlorestal.getVlmMaterialLenhosoConsumido().doubleValue());
			}
			else if (isDevedor()) {
				carregarDevedor();
				
				DevedorReposicaoFlorestal devedorReposicaoFlorestal = devedorReposicaoFlorestalService.obterDevedorReposicaoFlorestalCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal.getIdeCumprimentoReposicaoFlorestal());
				cumprimentoReposicaoFlorestalDTO.setDevedorReposicaoFlorestal(devedorReposicaoFlorestal);
			}
		}
	}
	
	public void carregarDadosFilho() throws Exception {
		listaPagamentoReposicaoFlorestalFilho = pagamentoReposicaoFlorestalService.listarPagamentoReposicaoFlorestalFilho(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal());
		listaTipoVolumeFlorestalRemanescente = tipoVolumeFlorestalRemanescenteService.listarTipoVolumeFlorestalRemanescente();
		
		carregarUnidadeMedida();
	}
	
	public void carregarUnidadeMedida() throws Exception {
		List<UnidadeMedida> lista = new ArrayList<UnidadeMedida>() {{
		    add(new UnidadeMedida(UnidadeMedidaEnum.M3.getId()));
		    add(new UnidadeMedida(UnidadeMedidaEnum.MDC.getId()));
		    add(new UnidadeMedida(UnidadeMedidaEnum.ST.getId()));
		}};

		listaUnidadeMedida = unidadeMedidaService.listarUnidadeMedida(lista);
	}
	
	public boolean isDetentor() {
		return cumprimentoReposicaoFlorestalService.isDetentor(cumprimentoReposicaoFlorestalDTO);
	}
	
	public boolean isConsumidor() {
		return cumprimentoReposicaoFlorestalService.isConsumidor(cumprimentoReposicaoFlorestalDTO);
	}
	
	public boolean isDevedor() {
		return cumprimentoReposicaoFlorestalService.isDevedor(cumprimentoReposicaoFlorestalDTO);
	}
	
	public boolean isASV() {
		return cumprimentoReposicaoFlorestalService.isASV(cumprimentoReposicaoFlorestalDTO);
	}
	
	public boolean isAML() {
		return cumprimentoReposicaoFlorestalService.isAML(cumprimentoReposicaoFlorestalDTO);
	}
	
	public boolean isReconhecimentoFlorestalRemanescente() {
		return cumprimentoReposicaoFlorestalService.isReconhecimentoFlorestalRemanescente(cumprimentoReposicaoFlorestalDTO);
	}
	
	public boolean isSecretariaMunicipal() {
		return cumprimentoReposicaoFlorestalService.isSecretariaMunicipal(cumprimentoReposicaoFlorestalDTO);
	}
	
	public boolean validar() {
		boolean retorno = true;
		
		if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal())) {
			JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "pagamento da reposição florestal"));
			retorno = false;
		}
		
		if (isDetentor()) {
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalFilho())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "respectivo ato relacionado"));
				retorno = false;
			}
			
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getNumPortaria())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "número da portaria"));
				retorno = false;
			}
			
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getDataPublicacaoPortaria())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "data da publicação"));
				retorno = false;
			}
			
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getNumProcesso())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "número do processo"));
				retorno = false;
			}
			
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getVolumeAutorizado())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "volume autorizado"));
				retorno = false;
			}
			
			if (isReconhecimentoFlorestalRemanescente()) {
				if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getIdeTipoVolumeFlorestalRemanescente())) {
					JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "reconhecimento de volume florestal remanescente"));
					retorno = false;
				}
				
				if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getNumProcessoAsvAml())) {
					JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "número do processo da ASV ou AML"));
					retorno = false;
				}
			}
			
		} 
		else if (isConsumidor()) {
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().getVlmMaterialLenhosoConsumido())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "volume de material lenhoso a ser consumido"));
				retorno = false;
			}
			
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().getNumPortariaAtoAdquirido())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "número da portaria do ato que originou o volume adquirido"));
				retorno = false;
			}
			
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().getDataPublicacaoPortaria())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "informe a data da publicação da portaria"));
				retorno = false;
			}
			
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().getNumProcessoOriginal())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "número do processo que originou o ato"));
				retorno = false;
			}
			
	
		}
		else if (isDevedor()) {
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().getIdeOrgaoEmissorAuto())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "órgão emissor do auto"));
				retorno = false;
			}
			
			if (isSecretariaMunicipal()) {
				if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().getIdeMunicipio())) {
					JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "município"));
					retorno = false;
				}
			}
			
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().getNumAutoInfracao())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "número do Auto de Infração"));
				retorno = false;
			}
			
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().getIdeBioma())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "bioma"));
				retorno = false;
			}
			
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().getVlrAreaSuprimida()) || "0,0000".equals(cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().getVlrAreaSuprimida())) {
				JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "área suprimida"));
				retorno = false;
			}
			
		}
		
		if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getIndCiente()) || !cumprimentoReposicaoFlorestalDTO.getIndCiente()) {
			JsfUtil.addWarnMessage(Util.getString("javax.faces.component.UIInput.REQUIRED", "ciência"));
			retorno = false;
		}
		
		return retorno;
	}
	
	public void gerarTipoAtoRelacionado() {
		if (isASV()) {
			this.tipoAtoRelacionado = "da ASV";
		} 
		else if (isAML()) {
			this.tipoAtoRelacionado = "da AML";
		}
		else if (isReconhecimentoFlorestalRemanescente()) {
			this.tipoAtoRelacionado = "de RVFR";
		}
		
		cumprimentoReposicaoFlorestalDTO.setDetentorReposicaoFlorestal(new DetentorReposicaoFlorestal(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getIdeDetentorReposicaoFlorestal()));
	}
	
	public void limparNumeroCAR() {
		numeroCarReposicaoFlorestal = new NumeroCarReposicaoFlorestal();
		Html.atualizar("formCAR");
	}
	

	
	public void removerCar() {
		cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().getNumeroCarReposicaoFlorestalCollection().remove(numeroCarReposicaoFlorestal);
		
		if (!Util.isNullOuVazio(numeroCarReposicaoFlorestal.getIdeNumeroCarReposicaoFlorestal())) {
			if (Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().getNumeroCarReposicaoFlorestalExcluido())) {
				cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().setNumeroCarReposicaoFlorestalExcluido(new ArrayList<NumeroCarReposicaoFlorestal>());
			}
			
			cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().getNumeroCarReposicaoFlorestalExcluido().add(numeroCarReposicaoFlorestal);
		}
		
		Html.esconder("dialogExcluirCAR");
		Html.atualizar("formReposicaoFlorestal");
	}
	
	public void calcularValorPecuniario() throws Exception {
		
		Double valorPecuniario = cumprimentoReposicaoFlorestalService.calcularValorPecuniario(cumprimentoReposicaoFlorestalDTO);
		cumprimentoReposicaoFlorestalDTO.setValorPecuniario(valorPecuniario);
		setListaMemoriaCalculoDTO(cumprimentoReposicaoFlorestalService.gerarMemoriaCalculo(cumprimentoReposicaoFlorestalDTO));
		setListaLegenda(cumprimentoReposicaoFlorestalService.gerarLegenda(cumprimentoReposicaoFlorestalDTO));
	}
	
	public void calcularValorPecuniario(UnidadeMedida unidadeMedida, Double valor) throws Exception {
		
		if(valor<=1000000){
			Double valorPecuniario = cumprimentoReposicaoFlorestalService.calcularValorPecuniario(cumprimentoReposicaoFlorestalDTO);
			cumprimentoReposicaoFlorestalDTO.setValorPecuniario(valorPecuniario);
			setListaMemoriaCalculoDTO(cumprimentoReposicaoFlorestalService.gerarMemoriaCalculo(cumprimentoReposicaoFlorestalDTO));
			setListaLegenda(cumprimentoReposicaoFlorestalService.gerarLegenda(cumprimentoReposicaoFlorestalDTO));
		}else{
			cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().setVolumeAutorizado(null);
			cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().setVlmMaterialLenhosoConsumido(null);
			cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().setVlrAreaSuprimida(null);
			JsfUtil.addErrorMessage("O valor máximo é de 1.000.000,000 (um milhão)");
		}
		
	}
	
	public void verificarProcessoSEIAPorAtoAmbiental() throws Exception {
		cumprimentoReposicaoFlorestalService.verificarProcessoSEIAPorAtoAmbiental(cumprimentoReposicaoFlorestalDTO, tipoAtoRelacionado, null);
	}
	
	public void verificarProcessoSEIA() throws Exception {
		cumprimentoReposicaoFlorestalService.verificarProcessoSEIAPorAtoAmbiental(cumprimentoReposicaoFlorestalDTO, tipoAtoRelacionado, cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getNumProcessoAsvAml());
	}
	
	public void finalizar() {
		try {
			if (validar()) {
				cumprimentoReposicaoFlorestalDTO.setRequerimento(this.requerimento);
				cumprimentoReposicaoFlorestalService.finalizar(cumprimentoReposicaoFlorestalDTO, requerimento);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao Finalizar o Cumprimento de Reposição Florestal.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void calcular(UnidadeMedida unidadeMedida, Double valor) throws Exception {
		cumprimentoReposicaoFlorestalDTO.setUnidadeMedidaCalculoDTO(new ArrayList<UnidadeMedidaCalculoDTO>());
		
		if (!Util.isNullOuVazio(unidadeMedida) && !Util.isNullOuVazio(valor)) {
			cumprimentoReposicaoFlorestalService.calcularUnidadeMedida(cumprimentoReposicaoFlorestalDTO, unidadeMedida, valor);
			calcularValorPecuniario(unidadeMedida, valor);
			setListaMemoriaCalculoDTO(cumprimentoReposicaoFlorestalService.gerarMemoriaCalculo(cumprimentoReposicaoFlorestalDTO));
			setListaLegenda(cumprimentoReposicaoFlorestalService.gerarLegenda(cumprimentoReposicaoFlorestalDTO));
		}
	}

	public List<PagamentoReposicaoFlorestal> getListaPagamentoReposicaoFlorestal() {
		return listaPagamentoReposicaoFlorestal;
	}

	public void setListaPagamentoReposicaoFlorestal(
			List<PagamentoReposicaoFlorestal> listaPagamentoReposicaoFlorestal) {
		this.listaPagamentoReposicaoFlorestal = listaPagamentoReposicaoFlorestal;
	}

	public List<PagamentoReposicaoFlorestal> getListaPagamentoReposicaoFlorestalFilho() {
		return listaPagamentoReposicaoFlorestalFilho;
	}

	public void setListaPagamentoReposicaoFlorestalFilho(
			List<PagamentoReposicaoFlorestal> listaPagamentoReposicaoFlorestalFilho) {
		this.listaPagamentoReposicaoFlorestalFilho = listaPagamentoReposicaoFlorestalFilho;
	}

	public List<TipoVolumeFlorestalRemanescente> getListaTipoVolumeFlorestalRemanescente() {
		return listaTipoVolumeFlorestalRemanescente;
	}

	public void setListaTipoVolumeFlorestalRemanescente(
			List<TipoVolumeFlorestalRemanescente> listaTipoVolumeFlorestalRemanescente) {
		this.listaTipoVolumeFlorestalRemanescente = listaTipoVolumeFlorestalRemanescente;
	}

	public CumprimentoReposicaoFlorestalDTO getCumprimentoReposicaoFlorestalDTO() {
		return cumprimentoReposicaoFlorestalDTO;
	}

	public void setCumprimentoReposicaoFlorestalDTO(
			CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) {
		this.cumprimentoReposicaoFlorestalDTO = cumprimentoReposicaoFlorestalDTO;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public boolean isDesabilitarTudo() {
		return desabilitarTudo;
	}

	public void setDesabilitarTudo(boolean desabilitarTudo) {
		this.desabilitarTudo = desabilitarTudo;
	}

	public String getTipoAtoRelacionado() {
		return tipoAtoRelacionado;
	}

	public void setTipoAtoRelacionado(String tipoAtoRelacionado) {
		this.tipoAtoRelacionado = tipoAtoRelacionado;
	}

	public NumeroCarReposicaoFlorestal getNumeroCarReposicaoFlorestal() {
		return numeroCarReposicaoFlorestal;
	}

	public void setNumeroCarReposicaoFlorestal(
			NumeroCarReposicaoFlorestal numeroCarReposicaoFlorestal) {
		this.numeroCarReposicaoFlorestal = numeroCarReposicaoFlorestal;
	}

	public List<OrgaoEmissorAuto> getListaOrgaoEmissorAuto() {
		return listaOrgaoEmissorAuto;
	}

	public void setListaOrgaoEmissorAuto(
			List<OrgaoEmissorAuto> listaOrgaoEmissorAuto) {
		this.listaOrgaoEmissorAuto = listaOrgaoEmissorAuto;
	}

	public List<Bioma> getListaBioma() {
		return listaBioma;
	}

	public void setListaBioma(List<Bioma> listaBioma) {
		this.listaBioma = listaBioma;
	}

	public List<Municipio> getListaMunicipio() {
		return listaMunicipio;
	}

	public void setListaMunicipio(List<Municipio> listaMunicipio) {
		this.listaMunicipio = listaMunicipio;
	}

	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}

	public List<UnidadeMedida> getListaUnidadeMedida() {
		return listaUnidadeMedida;
	}

	public void setListaUnidadeMedida(List<UnidadeMedida> listaUnidadeMedida) {
		this.listaUnidadeMedida = listaUnidadeMedida;
	}

	public List<MemoriaCalculoDTO> getListaMemoriaCalculoDTO() {
		return listaMemoriaCalculoDTO;
	}

	public void setListaMemoriaCalculoDTO(
			List<MemoriaCalculoDTO> listaMemoriaCalculoDTO) {
		this.listaMemoriaCalculoDTO = listaMemoriaCalculoDTO;
	}

	public List<String> getListaLegenda() {
		return listaLegenda;
	}

	public void setListaLegenda(List<String> listaLegenda) {
		this.listaLegenda = listaLegenda;
	}
}
