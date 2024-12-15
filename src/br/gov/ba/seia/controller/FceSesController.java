package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.FceSesElevatoriaDTO;
import br.gov.ba.seia.entity.CaracteristicaEfluente;
import br.gov.ba.seia.entity.CaracterizacaoEfluente;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FaixaDiametroAdutora;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceSes;
import br.gov.ba.seia.entity.FceSesCaracteristicaLancamento;
import br.gov.ba.seia.entity.FceSesCoordenadasLancamento;
import br.gov.ba.seia.entity.FceSesCoordenadasLancamentoLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceSesDadosElevatoria;
import br.gov.ba.seia.entity.FceSesDadosEstacaoTipoComposicao;
import br.gov.ba.seia.entity.FceSesDadosEstacaoTratamentoEsgoto;
import br.gov.ba.seia.entity.FceSesElevatoriaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceSesTipoComposicao;
import br.gov.ba.seia.entity.LancamentoCaracterizacaoEfluente;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.SesTipoComposicaoEnum;
import br.gov.ba.seia.facade.FceServiceFacade;
import br.gov.ba.seia.facade.FceSesFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.CaracteristicaCaptacaoService;
import br.gov.ba.seia.service.CaracterizacaoEfluenteService;
import br.gov.ba.seia.service.FceSesCaracteristicaLancamentoService;
import br.gov.ba.seia.service.LancamentoCaracterizacaoEfluenteService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.TipoPeriodoDerivacaoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author rodrigo
 *
 */
@Named("fceSesController")
@ViewScoped
public class FceSesController extends BaseFceSaaController{

	private FceSes fceSes;
	
	private FceSesElevatoriaLocalizacaoGeografica fceSesElevatoriaLocalizacaoGeografica;
	
	private FceSesElevatoriaDTO fceSesElevatoriaDTO;
	
	private List<FceSesElevatoriaDTO> listFceSesElevatoriaDTO;
	
	private FceSesDadosEstacaoTratamentoEsgoto fceSesDadosEstacaoTratamentoEsgoto;
	
	private FceSesDadosEstacaoTratamentoEsgoto fceSesDadosEstacaoTratamentoEsgotoSelecinado;
	
	private FceSesCoordenadasLancamento fceSesCoordenadasLancamento;
	
	private FceSesCoordenadasLancamentoLocalizacaoGeografica fceSesCoordenadasLancamentoLocalizacaoGeografica;
	
	private LocalizacaoGeografica localizacaoGeograficaSelecionada;
	
	private List<FaixaDiametroAdutora> listFaixaAdutora;
	
	private List<FaixaDiametroAdutora> listFaixaAdutoraTratEsgoto;
	
	private List<FceSesElevatoriaLocalizacaoGeografica> listFceSesElevatoriaLocalizacaoGeografica;
	
	private List<FceSesDadosEstacaoTratamentoEsgoto> listFceSesDadosEstacaoTratamentoEsgoto;
	
	private List<FceSesTipoComposicao> listTipoComposicoes;
	
	private List<FceSesDadosEstacaoTipoComposicao> listTipoComposicoesSelecionadas;
	
	private List<FceSesCoordenadasLancamentoLocalizacaoGeografica> listFceCoordenadasLancamentoLocalizacaoGeografica;
	
	private List<FceSesCaracteristicaLancamento> listCaracteristicaLancamento;
	
	private List<CaracteristicaEfluente> listaCaracteristicaEfluente;
	
	private FceSesCaracteristicaLancamento fceSesCaracteristicaLancamentoSelecionado;
	
	private List<TipoPeriodoDerivacao>  listPeriodoDerivacao;
	
	private List<CaracterizacaoEfluente> listCaracterizacaoEfluente;
	
	private List<CaracterizacaoEfluente> listCaracterizacaoEfluenteSelecionados;
	
	private MetodoUtil metodoExterno;
	
	@Inject
	private LocalizacaoGeograficaGenericController localizacaoGeograficaController;
			
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@EJB
	private FceSesFacade fceSesFacade;
	
	@EJB
	private FceServiceFacade fceServiceFacade;
	
	@EJB
	private TipoPeriodoDerivacaoService tipoPeriodoService;

	@EJB
	private CaracterizacaoEfluenteService caracterizacaoEfluenteService;
	
	@EJB
	private LancamentoCaracterizacaoEfluenteService lancamentoEfluenteService;
	
	@EJB
	private CaracteristicaCaptacaoService caracteristicaCaptacaoService;
	
	@EJB
	private FceSesCaracteristicaLancamentoService fceSesCaracteristicaLancamentoService;
	
	private static final DocumentoObrigatorio DOC_FCE_SES = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_CARACTERIZACAO_SES.getId(), "Formulário de Caracterização do Empreendimento - Sistema de Esgotamento Sanitário (SES)");
	
	private boolean btnFinalizar;
	
	@Inject
	private FceLancamentoEfluentesController fceLancamentoEfluentesController;
	
	@Override
	public void init() {
		inicializarObjetos();
		limparDados();
		verificarEdicao();
		carregarDados();
		if(!isFceSalvo()){
			iniciarFce(DOC_FCE_SES);
			fceSes = new FceSes(super.fce);
		}
		else {
			carregarFceSes();
			carregarDadosTratamentoEsgoto();
			carregarAbas();
			if(isFceTecnico()){
				carregarCoordenadasLancamento();
			}
		}
	}
	

	private void carregarAbas() {
		
		List<FceSesDadosElevatoria> listDadosElevatoria;
		try {
			listDadosElevatoria = fceSesFacade.listarDadosElevatoria(fceSes);
			
			listFceSesDadosEstacaoTratamentoEsgoto = fceSesFacade.listarDadosEstacaoEsgoto(fceSes);
			
			for(FceSesDadosElevatoria dados : listDadosElevatoria){
				dados.setFceSesElevatoriaLocalizacaoGeograficaCollection(fceSesFacade.buscarLocalizacaoElevatoria(dados));
			}
			
			for(FceSesDadosEstacaoTratamentoEsgoto dadosTratamento : listFceSesDadosEstacaoTratamentoEsgoto){
				
				for(DadoGeografico dado : dadosTratamento.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()){
					dadosTratamento.getIdeLocalizacaoGeografica().setLatitudeInicial(localizacaoGeograficaController.getLatitude(dado));
					dadosTratamento.getIdeLocalizacaoGeografica().setLongitudeInicial(localizacaoGeograficaController.getLongitude(dado));
				}
				dadosTratamento.setDadosEstacaoTipoComposicaoCollection(fceSesFacade.listarComposicoes(dadosTratamento));
				
				for(FceSesDadosEstacaoTipoComposicao item : dadosTratamento.getDadosEstacaoTipoComposicaoCollection()){
					item.setDesabilitarItem(Boolean.TRUE);
					
					if(item.getIdeFceSesTipoComposicao().getIdeFceSesTipoComposicao() == SesTipoComposicaoEnum.EMISSARIO_lANCAMENTO.getId()){ 
						item.setIndDigestorAnaerobio(Boolean.TRUE);
					}else{
						item.setIndDigestorAnaerobio(Boolean.FALSE);
					}
				}
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
				
		for(FceSesDadosElevatoria dadoElevatoria : listDadosElevatoria){
		
			FceSesElevatoriaDTO dto = new FceSesElevatoriaDTO();
			dto.setDadosElevatoria(dadoElevatoria);
			
			for(FceSesElevatoriaLocalizacaoGeografica item : dadoElevatoria.getFceSesElevatoriaLocalizacaoGeograficaCollection()){
			
				for(DadoGeografico dado : item.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()){
					item.getIdeLocalizacaoGeografica().setLatitudeInicial(localizacaoGeograficaController.getLatitude(dado));
					item.getIdeLocalizacaoGeografica().setLongitudeInicial(localizacaoGeograficaController.getLongitude(dado));
				}
				
				if(item.getIndExtravasamento()){
					dto.setLocalizacaoExtravazamento(item.getIdeLocalizacaoGeografica());
				}else{
					dto.setLocalizacaoElevatoria(item.getIdeLocalizacaoGeografica());
				}
			}
			
			listFceSesElevatoriaDTO.add(dto);
		}
		
	}
	

	private void carregarFceSes() {
		try {
			fceSes = fceSesFacade.buscarFceSesByIdeFce(super.fce);
			if(Util.isNullOuVazio(fceSes)){
				fceSes = new FceSes();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Irrigação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}	
	}

	
	public void carregarCoordenadasLancamento(){
		try {
				listFceCoordenadasLancamentoLocalizacaoGeografica = fceSesFacade.buscarFceSesCoordenadasLancamentoLocalizacaoGeografica(fceSes);
				
				if (!Util.isNullOuVazio(listFceCoordenadasLancamentoLocalizacaoGeografica)) {
					
					for(FceSesCoordenadasLancamentoLocalizacaoGeografica item : listFceCoordenadasLancamentoLocalizacaoGeografica){
						item.setDesabilitarItem(Boolean.TRUE);
						for(DadoGeografico dado : item.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()){
							item.getIdeLocalizacaoGeografica().setLatitudeInicial(localizacaoGeograficaController.getLatitude(dado));
							item.getIdeLocalizacaoGeografica().setLongitudeInicial(localizacaoGeograficaController.getLongitude(dado));
						}
					}
				} else {
					fceSesCoordenadasLancamentoLocalizacaoGeografica = new FceSesCoordenadasLancamentoLocalizacaoGeografica();
					fceSesCoordenadasLancamentoLocalizacaoGeografica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
				}
				for(FceSesCaracteristicaLancamento caracteristica : listCaracteristicaLancamento){
					caracteristica.setConfirmado(true);
				}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void carregarDados() {
		try {
			listFaixaAdutora = fceSesFacade.carregarFaixasDiametro(1, null, null);
			listaCaracteristicaEfluente = fceSesFacade.listarCaracteristicaEfluenteSemFosforo();
			listPeriodoDerivacao = tipoPeriodoService.listarTipoPeriodoDerivacao();
			listCaracterizacaoEfluente = caracterizacaoEfluenteService.listarCaracterizacaoEfluente();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarDadosTratamentoEsgoto(){
		
		try {
			limparDados();
			listFaixaAdutoraTratEsgoto = fceSesFacade.carregarFaixasDiametro(1, null, null);
			listTipoComposicoes = fceSesFacade.carregarTipoComposicoes();
			ordernarComposicoes();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
		
	@Override
	public void finalizar(){ 
		if(validarAba()){
			try {
				validarFce();
				fceSesFacade.finalizar(this);
				Html.exibir("confimarImpressaoRelatorioSes");
				Html.esconder("caracterizacaoSes");
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Sistema de Esgotamento Sanitário");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	
	
	@Override
	public boolean validarAba(){
		boolean retorno = Boolean.TRUE;
		if(Util.isNullOuVazio(fceSes.getValorHorizonteProjeto()) || fceSes.getValorHorizonteProjeto() == 0) {
			retorno = false;
			MensagemUtil.msg0003("Horizonte do projeto");
		}
		if(Util.isNullOuVazio(fceSes.getValorVazaoMediaProjeto()) || fceSes.getValorVazaoMediaProjeto() == 0.00) {
			retorno = false;
			MensagemUtil.msg0003("Vazão média do projeto");
		}
		if(Util.isNullOuVazio(fceSes.getValorExtensaoTotalRede()) || fceSes.getValorExtensaoTotalRede() == 0.00) {
			retorno = false;
			MensagemUtil.msg0003("Extensão total da adutora");
		}
		if(Util.isNullOuVazio(fceSes.getIdeFaixaDiametroAdutora())) {
			retorno = false;
			MensagemUtil.msg0003("Faixa de diâmetro da adutora");
		}
		if(Util.isNullOuVazio(listFceSesElevatoriaDTO)) {
			retorno = false;
			MensagemUtil.msg0003("Dados da elevatória");
		}
		if(Util.isNullOuVazio(listFceSesDadosEstacaoTratamentoEsgoto)) {
			retorno = false;
			MensagemUtil.msg0003("Estação de tratamento");
		}
		
		if(isFceTecnico()){
			if(!Util.isNullOuVazio(listFceCoordenadasLancamentoLocalizacaoGeografica)){
				for(FceSesCoordenadasLancamentoLocalizacaoGeografica item : listFceCoordenadasLancamentoLocalizacaoGeografica){
					if(Util.isNullOuVazio(item.getNomeCoporHidrico()) || !item.isDesabilitarItem()) {
						retorno = false;
						MensagemUtil.msg0003("Nome do corpo hidríco/receptor");
					}
				}
			}else if(CollectionUtils.isEmpty(getLancamentosEfluentes())){
				retorno = Boolean.FALSE;
				MensagemUtil.msg0003("Coordenada Geográfica");
			}
		}
		return retorno;
	}
	
	public StreamedContent getImprimirRelatorio() {
		try{
			return super.getImprimirRelatorio(super.fce, DOC_FCE_SES);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Sistema esgotamento sanitário.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	public void prepararParaFinalizar() throws Exception{
		this.concluirFce();
		salvarFce();
		salvarFceSes();
		salvarLocalizacoes();
		if(isFceTecnico()){
			salvarLancamentoEfluente();
		}
		if(!isFceSalvo()){
			super.exibirMensagem001();
		}
		else {
			super.exibirMensagem002();
		}
		Html.esconder("sistemaAbastecimentoAgua");
		Html.atualizar("dlgAnaliseTecnica");
		limpar();
	}
	
	@Override
	protected void concluirFce() throws Exception {
		if(!Util.isNullOuVazio(this.listTipoComposicoesSelecionadas)) {
			for(FceSesDadosEstacaoTipoComposicao s :this.listTipoComposicoesSelecionadas) {
				if(s.getIdeFceSesTipoComposicao().getIdeFceSesTipoComposicao().equals(SesTipoComposicaoEnum.OUTROS.getId())) {
					this.fce.setIndConcluido(false);
					return;
				}
			}
		}
		this.fce.setIndConcluido(true);
	}
	
	private void salvarLancamentoEfluente() {
		
		try {
			for(FceSesCoordenadasLancamentoLocalizacaoGeografica localizacao : listFceCoordenadasLancamentoLocalizacaoGeografica){
				localizacao.setIdeFceSes(fceSes);
				fceSesFacade.salvarFceSesCoordenadasLancamentoLocalizacaoGeografica(localizacao);
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	}

	private void salvarLocalizacoes() {
		
		try {
		
			if(!Util.isNull(listFceSesElevatoriaDTO)){
			
				for(FceSesElevatoriaDTO dto: listFceSesElevatoriaDTO){
						dto.getDadosElevatoria().setIdeFceSes(fceSes);
						fceSesFacade.salvarFceSesDadosElevatoria(dto.getDadosElevatoria());
						
						
						if(!Util.isNullOuVazio(dto.getDadosElevatoria().getFceSesElevatoriaLocalizacaoGeograficaCollection())){
							
							
							for(FceSesElevatoriaLocalizacaoGeografica elevatoria : dto.getDadosElevatoria().
									getFceSesElevatoriaLocalizacaoGeograficaCollection()){
								
								elevatoria.setIdeFceSesDadosElevatoria(dto.getDadosElevatoria());
								fceSesFacade.salvarFceSesElevatoriaLocalizacaoGeografica(elevatoria);
							}
						}
						
					} 
				}
			
			
			if(!Util.isNullOuVazio(listFceSesDadosEstacaoTratamentoEsgoto)){
				
				for (FceSesDadosEstacaoTratamentoEsgoto dadosTratamento : listFceSesDadosEstacaoTratamentoEsgoto){
					dadosTratamento.setIdeFceSes(fceSes);

					fceSesFacade.salvarFceSesDadosEstacaoTratamentoEsgoto(dadosTratamento);
					
					if(!Util.isNullOuVazio(dadosTratamento.getDadosEstacaoTipoComposicaoCollection())){
						
						for(FceSesDadosEstacaoTipoComposicao composicao : dadosTratamento.getDadosEstacaoTipoComposicaoCollection()){
							composicao.setIdeFceSesDadosEstacaoTratamentoEsgoto(dadosTratamento);
							fceSesFacade.salvarFceSesDadosEstacaoTipoComposicao(composicao);
						}
					}
				}
			}
		
		}catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

	private void salvarFceSes() {
		try {
			fceSes.setIdeFce(super.fce);
			fceSesFacade.salvarFceSes(fceSes);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);	
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	public void limparDados() {
		fceSesDadosEstacaoTratamentoEsgoto = new FceSesDadosEstacaoTratamentoEsgoto();
		fceSesDadosEstacaoTratamentoEsgoto.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		fceSesDadosEstacaoTratamentoEsgoto.setDadosEstacaoTipoComposicaoCollection(new ArrayList<FceSesDadosEstacaoTipoComposicao>());
		listTipoComposicoesSelecionadas = new ArrayList<FceSesDadosEstacaoTipoComposicao>();
		fceSesDadosEstacaoTratamentoEsgotoSelecinado = new FceSesDadosEstacaoTratamentoEsgoto();
	}

	public void salvarDadosTratamentoEsgoto(){
		
		if(validarDadosTratamentoEsgoto()){
			if(!listFceSesDadosEstacaoTratamentoEsgoto.contains(fceSesDadosEstacaoTratamentoEsgoto)){
				fceSesDadosEstacaoTratamentoEsgoto.setDadosEstacaoTipoComposicaoCollection(listTipoComposicoesSelecionadas);
				listFceSesDadosEstacaoTratamentoEsgoto.add(fceSesDadosEstacaoTratamentoEsgoto);
			}
			MensagemUtil.sucesso("Dados da estação de tratamento salvo com sucesso!");
			Html.esconder("dadosTratamentoEsgoto");
		}
	}
	
	public void excluirDadosEstacaoTratamento(){
		try {
				if(!Util.isNullOuVazio(fceSesDadosEstacaoTratamentoEsgotoSelecinado)){
				fceSesFacade.excluirFceSesDadosEstacaoTipoComposicaoImpl(fceSesDadosEstacaoTratamentoEsgotoSelecinado);
				fceSesFacade.excluirFceSesDadosEstacaoTratamentoEsgoto(fceSesDadosEstacaoTratamentoEsgotoSelecinado);
				listFceSesDadosEstacaoTratamentoEsgoto.remove(fceSesDadosEstacaoTratamentoEsgotoSelecinado);
				MensagemUtil.sucesso("Excluído com sucesso!");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
		
	private boolean validarDadosTratamentoEsgoto() {
		boolean result = true;
		if(!Util.isNullOuVazio(fceSesDadosEstacaoTratamentoEsgoto)){

			if(Util.isNullOuVazio(fceSesDadosEstacaoTratamentoEsgoto.getNomeEstacao())) {
				result = false;
				MensagemUtil.msg0003("Identificação da estação");
			}
			if(Util.isNullOuVazio(fceSesDadosEstacaoTratamentoEsgoto.getValorVazaoMedia())) {
				result = false;
				MensagemUtil.msg0003("Vazão");
			}
			if(Util.isNullOuVazio(fceSesDadosEstacaoTratamentoEsgoto.getIdeLocalizacaoGeografica())) {
				result = false;
				MensagemUtil.msg0003("Coordenada Geográfica");
			}
			if(Util.isNullOuVazio(listTipoComposicoesSelecionadas)) {
				result = false;
				MensagemUtil.msg0003("Composições");
			}
			
			fceSesDadosEstacaoTratamentoEsgoto.setDadosEstacaoTipoComposicaoCollection(listTipoComposicoesSelecionadas);
			for(FceSesDadosEstacaoTipoComposicao composicao : fceSesDadosEstacaoTratamentoEsgoto.getDadosEstacaoTipoComposicaoCollection()){
				if(!Util.isNullOuVazio(composicao.getValorQuantidadeAtivo())){
					if(composicao.getIndDigestorAnaerobio()) {
						if(Util.isNullOuVazio(composicao.getIdeFaixaDiametroAdutora())) {
							result = false;
							MensagemUtil.msg0003("Diâmetro");
						}
						if(Util.isNullOuVazio(composicao.getValorExtensaoAtivo())) {
							result = false;
							MensagemUtil.msg0003("Extensão(m)");
						}						
					}
					if(Util.isNullOuVazio(composicao.getIndDigestorAnaerobio())) {
						result = false;
						MensagemUtil.msg0003("Quantidade(Und)");
					}
				}
				if(!composicao.getDesabilitarItem()) {
					result = false;
					MensagemUtil.erro("É necessário confirmar cada composição selecionada.");
				}
			}
		}
		return result;
	}
	
	
	public void salvarDadosElevatoria(){
		if(validarDadosElevatorio()){

			FceSesElevatoriaLocalizacaoGeografica dadosSesElevatoria = new FceSesElevatoriaLocalizacaoGeografica();
			fceSesElevatoriaDTO.getDadosElevatoria().setFceSesElevatoriaLocalizacaoGeograficaCollection(new ArrayList<FceSesElevatoriaLocalizacaoGeografica>());
			dadosSesElevatoria.setIdeFceSesDadosElevatoria(fceSesElevatoriaDTO.getDadosElevatoria());
			dadosSesElevatoria.setIdeLocalizacaoGeografica(fceSesElevatoriaDTO.getLocalizacaoElevatoria());
			fceSesElevatoriaDTO.getDadosElevatoria().getFceSesElevatoriaLocalizacaoGeograficaCollection().add(dadosSesElevatoria);
			dadosSesElevatoria.setIndExtravasamento(Boolean.FALSE);
			
			if(!Util.isNullOuVazio(fceSesElevatoriaDTO.getLocalizacaoExtravazamento())) {
				FceSesElevatoriaLocalizacaoGeografica dadosSesExtravasamento = new FceSesElevatoriaLocalizacaoGeografica();
				dadosSesExtravasamento.setIdeFceSesDadosElevatoria(fceSesElevatoriaDTO.getDadosElevatoria());
				dadosSesExtravasamento.setIdeLocalizacaoGeografica(fceSesElevatoriaDTO.getLocalizacaoExtravazamento());
				dadosSesExtravasamento.setIndExtravasamento(Boolean.TRUE);
				fceSesElevatoriaDTO.getDadosElevatoria().getFceSesElevatoriaLocalizacaoGeograficaCollection().add(dadosSesExtravasamento);
			}

			
			
			if(!listFceSesElevatoriaDTO.contains(fceSesElevatoriaDTO)){
				listFceSesElevatoriaDTO.add(fceSesElevatoriaDTO);
			}
			Html.esconder("dadosElevatoria");
			MensagemUtil.sucesso("Salvo com sucesso!");
		}
	}
	
	public void excluirDadosLocalizacaoElevatoria(){
		if(listFceSesElevatoriaDTO.contains(fceSesElevatoriaDTO)){
			try {
				fceSesFacade.excluirFceSesElevatoriaLocalizacaoGeografica(fceSesElevatoriaDTO.getDadosElevatoria());
				fceSesFacade.excluirFceSesDadosElevatoriaImpl(fceSesElevatoriaDTO.getDadosElevatoria());
				listFceSesElevatoriaDTO.remove(fceSesElevatoriaDTO);
				MensagemUtil.sucesso("Excluído com sucesso!");
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void excluirCoordenadaLancamento(){
			try {
				if(listFceCoordenadasLancamentoLocalizacaoGeografica.contains(fceSesCoordenadasLancamentoLocalizacaoGeografica)){
					lancamentoEfluenteService.deletarCaracterizacaoEfluente(fceSesCoordenadasLancamentoLocalizacaoGeografica);
					fceSesFacade.excluirFceSesCaracteristicaLancamento(fceSesCoordenadasLancamentoLocalizacaoGeografica);
					fceSesFacade.excluirCoordenadasLancamentoLocalizacaoGeografica(fceSesCoordenadasLancamentoLocalizacaoGeografica);
					listFceCoordenadasLancamentoLocalizacaoGeografica.remove(fceSesCoordenadasLancamentoLocalizacaoGeografica);
					
					fceSesCoordenadasLancamentoLocalizacaoGeografica = new FceSesCoordenadasLancamentoLocalizacaoGeografica();
					fceSesCoordenadasLancamentoLocalizacaoGeografica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
	}
	
	
	private Boolean validarDadosElevatorio() {
		boolean result = true;
		
		if(Util.isNullOuVazio(fceSesElevatoriaDTO.getDadosElevatoria().getDescricaoIdentificacao())) {
			result = false;
			MensagemUtil.msg0003("Identificação da elevatória");
		}
		if(Util.isNullOuVazio(fceSesElevatoriaDTO.getDadosElevatoria().getValorVazao())) {
			result = false;
			MensagemUtil.msg0003("Vazão");
		}
		if(Util.isNullOuVazio(fceSesElevatoriaDTO.getDadosElevatoria().getValorExtensaoLinha())) {
			result = false;
			MensagemUtil.msg0003("Extensão da linha");
		}
		if(Util.isNullOuVazio(fceSesElevatoriaDTO.getDadosElevatoria().getIdeFaixaDiametroAdutora())) {
			result = false;
			MensagemUtil.msg0003("Faixa de diâmetro da linha");
		}
		if(Util.isNullOuVazio(fceSesElevatoriaDTO.getLocalizacaoElevatoria()) 
				|| Util.isNullOuVazio(fceSesElevatoriaDTO.getLocalizacaoElevatoria().getIdeLocalizacaoGeografica())) {
			result = false;
			MensagemUtil.msg0003("Coordenada da elevatória");
		}
		
		return result;
	}
	
	public void editarDadosLocalizacaoElevatoria(FceSesElevatoriaDTO fceSesElevatoriaDTO){
		this.fceSesElevatoriaDTO = fceSesElevatoriaDTO;
	}
	
	public String getRequireMesage(String campo) {
		return campo +" "+ Util.getString("messagem_003");
	}
	
	public void zerarDadosElevatoria(){
		fceSesElevatoriaDTO = new FceSesElevatoriaDTO();
		fceSesElevatoriaDTO.setDadosElevatoria(new FceSesDadosElevatoria());
		fceSesElevatoriaDTO.setLocalizacaoElevatoria(new LocalizacaoGeografica());
		fceSesElevatoriaDTO.setLocalizacaoExtravazamento(new LocalizacaoGeografica());
	}	

	
	@Override
	public void verificarEdicao(){
		
		super.carregarFceDoRequerente(DOC_FCE_SES);
	}
	
	public void selecionarTipoComposicao(FceSesTipoComposicao tipoComposicao){
		if(SesTipoComposicaoEnum.OUTROS.getId().equals(tipoComposicao.getIdeFceSesTipoComposicao())) {
			MensagemUtil.alerta(Util.getString("lac_dadosGerais_info001"));
		} else{
			FceSesDadosEstacaoTipoComposicao estacaoEsgotoTipoComposicao = new FceSesDadosEstacaoTipoComposicao();
			estacaoEsgotoTipoComposicao.setIdeFceSesTipoComposicao(tipoComposicao);
			
			estacaoEsgotoTipoComposicao.setIndDigestorAnaerobio(Boolean.FALSE);
			if(tipoComposicao.getIdeFceSesTipoComposicao().equals(SesTipoComposicaoEnum.EMISSARIO_lANCAMENTO.getId())){
				estacaoEsgotoTipoComposicao.setIndDigestorAnaerobio(Boolean.TRUE);
			}
			
			listTipoComposicoes.remove(estacaoEsgotoTipoComposicao.getIdeFceSesTipoComposicao());
			
			ordernarComposicoes();
			
			listTipoComposicoesSelecionadas.add(estacaoEsgotoTipoComposicao);
			
			
			Html.atualizar("formSesDadosTratamentoEsgoto:pngDadosTratamentoEsgoto");
		}
	}


	private void ordernarComposicoes() {
		Collections.sort(listTipoComposicoes, new Comparator<FceSesTipoComposicao>(){
			@Override
			public int compare(FceSesTipoComposicao o1, FceSesTipoComposicao o2) {
				return o1.getIdeFceSesTipoComposicao() < o2.getIdeFceSesTipoComposicao() ? -1 : 1;
			}
		});
	}
	
	public void confirmarComposicao(FceSesDadosEstacaoTipoComposicao tipoComposicao){
		
		if(!Util.isNullOuVazio(tipoComposicao.getValorQuantidadeAtivo())){
			
			if((Util.isNullOuVazio(tipoComposicao.getIdeFaixaDiametroAdutora()) || Util.isNullOuVazio(tipoComposicao.getValorExtensaoAtivo()))
					&& tipoComposicao.getIndDigestorAnaerobio()){
			
				MensagemUtil.erro("Preencha as informações da maneira correta");

			}else{
				
				for(FceSesDadosEstacaoTipoComposicao item : listTipoComposicoesSelecionadas){
					if(item.equals(tipoComposicao)){
						Integer index = listTipoComposicoesSelecionadas.indexOf(tipoComposicao);
						listTipoComposicoesSelecionadas.get(index).setValorQuantidadeAtivo(tipoComposicao.getValorQuantidadeAtivo());
						listTipoComposicoesSelecionadas.get(index).setValorExtensaoAtivo(tipoComposicao.getValorExtensaoAtivo());
						listTipoComposicoesSelecionadas.get(index).setIdeFaixaDiametroAdutora(tipoComposicao.getIdeFaixaDiametroAdutora());
						listTipoComposicoesSelecionadas.get(index).setDesabilitarItem(true);
						
						
					}
				}
			}
		}else{
			MensagemUtil.erro("Preencha as informações da maneira correta");
		}
	}
	
	public void confirmarDadosCoordenadaEfluente(FceSesCoordenadasLancamentoLocalizacaoGeografica coordenada){
		
		if(Util.isNullOuVazio(coordenada.getNomeCoporHidrico())){
			MensagemUtil.erro("Corpo Hídrico/Receptor é de preenchimento obrigatório");
		}else{
				
			for(FceSesCoordenadasLancamentoLocalizacaoGeografica item : listFceCoordenadasLancamentoLocalizacaoGeografica){
				if(item.equals(coordenada)){
					item = coordenada;
					item.setDesabilitarItem(Boolean.TRUE);
				}
			}
		}
	}
	
	public void excluirLocGeoSelecionada() {
		try {
			if(!Util.isNullOuVazio(this.localizacaoGeograficaSelecionada)) {

				if(!Util.isNullOuVazio(fceSesDadosEstacaoTratamentoEsgoto.getIdeLocalizacaoGeografica())){
					if(fceSesDadosEstacaoTratamentoEsgoto.getIdeLocalizacaoGeografica().equals(localizacaoGeograficaSelecionada)){
						fceSesDadosEstacaoTratamentoEsgoto.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
					}
				}else if(!Util.isNullOuVazio(fceSesElevatoriaDTO)) {
					if(fceSesElevatoriaDTO.getLocalizacaoElevatoria().equals(localizacaoGeograficaSelecionada)){
						
						fceSesElevatoriaDTO.setLocalizacaoElevatoria(new LocalizacaoGeografica());
						
					}else if(fceSesElevatoriaDTO.getLocalizacaoExtravazamento().equals(localizacaoGeograficaSelecionada)){
						fceSesElevatoriaDTO.setLocalizacaoExtravazamento(new LocalizacaoGeografica());
					}
				}else if(!Util.isNullOuVazio(fceSesCoordenadasLancamentoLocalizacaoGeografica.getIdeLocalizacaoGeografica())){
					Integer index = listFceCoordenadasLancamentoLocalizacaoGeografica.indexOf(fceSesCoordenadasLancamentoLocalizacaoGeografica);
					listFceCoordenadasLancamentoLocalizacaoGeografica.remove(index);
					fceSesCoordenadasLancamentoLocalizacaoGeografica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
				}
				
				localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(this.localizacaoGeograficaSelecionada);
				this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
				JsfUtil.addSuccessMessage("Localização excluída com sucesso!");
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void habilitarComposicao(FceSesDadosEstacaoTipoComposicao tipoComposicao){
		
		for(FceSesDadosEstacaoTipoComposicao item : listTipoComposicoesSelecionadas){
			if(item.equals(tipoComposicao)){
				Integer index = listTipoComposicoesSelecionadas.indexOf(tipoComposicao);
				listTipoComposicoesSelecionadas.get(index).setDesabilitarItem(Boolean.FALSE);
			}
		}
	}	
	
	public void habilitarItemLancamentoEfluente(FceSesCoordenadasLancamentoLocalizacaoGeografica coordenada){
		
		for(FceSesCoordenadasLancamentoLocalizacaoGeografica item : listFceCoordenadasLancamentoLocalizacaoGeografica){
			if(item.equals(coordenada)){
				item.setDesabilitarItem(Boolean.FALSE);
			}
		}
	}	
	
	public void excluirComposicao(FceSesDadosEstacaoTipoComposicao tipoComposicao){
		listTipoComposicoesSelecionadas.remove(tipoComposicao);
		fceSesDadosEstacaoTratamentoEsgoto.getDadosEstacaoTipoComposicaoCollection().remove(tipoComposicao);
		listTipoComposicoes.add(tipoComposicao.getIdeFceSesTipoComposicao());
		ordernarComposicoes();
		Html.atualizar("formSesDadosTratamentoEsgoto:pngDadosTratamentoEsgoto");
	}	
	
	public void editarDadosTratamento(FceSesDadosEstacaoTratamentoEsgoto itemLista){

		try {
			listTipoComposicoes = fceSesFacade.carregarTipoComposicoes();
			fceSesDadosEstacaoTratamentoEsgoto = itemLista;
			listTipoComposicoesSelecionadas = itemLista.getDadosEstacaoTipoComposicaoCollection();
			
			List<FceSesTipoComposicao> listTipoComposicoesRemovidas = new ArrayList<FceSesTipoComposicao>();
			
				for(FceSesDadosEstacaoTipoComposicao selecionado : listTipoComposicoesSelecionadas){
					
					for(FceSesTipoComposicao composicao : listTipoComposicoes){
						
						if(composicao.equals(selecionado.getIdeFceSesTipoComposicao())){
							listTipoComposicoesRemovidas.add(composicao);
						}
					}
				}
				
				listTipoComposicoes.removeAll(listTipoComposicoesRemovidas);
				ordernarComposicoes();
				
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	@Override
	protected void prepararDuplicacao() {
		fceSes.setIdeFceSes(null);
		fceSes.setIdeFce(null);
		for(FceSesElevatoriaDTO elevatoria : listFceSesElevatoriaDTO){
			elevatoria.getDadosElevatoria().setIdeFceSesDadosElevatoria(null);
			elevatoria.getDadosElevatoria().setIdeFceSes(null);
			if(!CollectionUtils.isEmpty(elevatoria.getDadosElevatoria().getFceSesElevatoriaLocalizacaoGeograficaCollection())){
				for(FceSesElevatoriaLocalizacaoGeografica loc : elevatoria.getDadosElevatoria().getFceSesElevatoriaLocalizacaoGeograficaCollection()){
					loc.setIdeFceSesElevatoriaLocalizacaoGeografica(null);
					loc.setIdeFceSesDadosElevatoria(null);
				}
			}
		}
		
		for(FceSesDadosEstacaoTratamentoEsgoto ete : listFceSesDadosEstacaoTratamentoEsgoto){
			ete.setIdeFceSesDadosTramentoEsgoto(null);
			ete.setIdeFceSes(null);
			
			for(FceSesDadosEstacaoTipoComposicao tipoComposicao : ete.getDadosEstacaoTipoComposicaoCollection()){
				tipoComposicao.setIdeFceSesDadosEstacaoTipoComposicao(null);
			}
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			super.salvarFce();
			salvarFceSes();
			salvarLocalizacoes();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " Sistema de esgotamento sanitário.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	protected void carregarFceTecnico(){
		try {
			//Carregar Dados do FCE Técnico		
			inicializarObjetos();
			btnFinalizar = false;
			limparDados();
			super.carregarFceDoTecnico(DOC_FCE_SES);
			carregarDados();
			carregarFceSes();
			carregarDadosTratamentoEsgoto();
			carregarAbas();
			carregarCoordenadasLancamento();
			carregarOutorgas();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Sistema de esgotamento sanitário.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void confirmarEfluente(FceSesCaracteristicaLancamento fceSesCaracteristicaLancamentoSelecionado){
		if(validarFceLancamentoEfluenteCaracteristica(fceSesCaracteristicaLancamentoSelecionado)){
			fceSesCaracteristicaLancamentoSelecionado.setConfirmado(true);
			fceSesCaracteristicaLancamentoSelecionado.setValorEficienciRemocao(calcularEficienciaRemocao(fceSesCaracteristicaLancamentoSelecionado));
		}
	}
	
	public void editarEfluente(){
		fceSesCaracteristicaLancamentoSelecionado.setConfirmado(false);
	}
	
	public void avancarAba(){
		btnFinalizar = true;
		activeTab++;
	}
	
	public void changeAba(){
		btnFinalizar = false;
		
		if(activeTab == 0 && !isFceTecnico()){
			btnFinalizar = true;
		}else if(activeTab == 1){
			btnFinalizar = true;
		}
	}
	
	private boolean validarFceLancamentoEfluenteCaracteristica(FceSesCaracteristicaLancamento fceSesCaracteristicaLancamentoSelecionado){
		boolean valido = true;
		if(Util.isNullOuVazio(fceSesCaracteristicaLancamentoSelecionado.getValorBrutoEfluente())){
			JsfUtil.addErrorMessage(Util.getString("fce_inf042"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceSesCaracteristicaLancamentoSelecionado.getValorEfluenteTratado())){
			JsfUtil.addErrorMessage(Util.getString("fce_inf043"));
			valido = false;
		}
		if(valido && fceSesCaracteristicaLancamentoSelecionado.getValorBrutoEfluente().compareTo(fceSesCaracteristicaLancamentoSelecionado.getValorEfluenteTratado()) < 0){
			JsfUtil.addErrorMessage(Util.getString("fce_lancamento_efluente_bruto_menor_tratado"));
			valido = false;
		}
		return valido;
	}
	
	private BigDecimal calcularEficienciaRemocao(FceSesCaracteristicaLancamento fceSesCaracteristicaLancamentoSelecionado){
		return ((fceSesCaracteristicaLancamentoSelecionado.getValorBrutoEfluente().subtract(fceSesCaracteristicaLancamentoSelecionado.getValorEfluenteTratado())).multiply(new BigDecimal(100))).divide(fceSesCaracteristicaLancamentoSelecionado.getValorBrutoEfluente(), RoundingMode.HALF_UP);
	}
	
	@Override
	public void voltarAba() {
		if(activeTab == 0){
			Html.esconder("caracterizacaoSes");
		}else{
			activeTab--;
			btnFinalizar = false;
		}	
	}
	
	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlSes");
		RequestContext.getCurrentInstance().execute("caracterizacaoSes.show();");
	}
	
	
	private void inicializarObjetos() {
		
		
		fceSes = new FceSes();
		fceSesElevatoriaDTO = new FceSesElevatoriaDTO();
		fceSesElevatoriaDTO.setDadosElevatoria(new FceSesDadosElevatoria());
		fceSesElevatoriaDTO.setLocalizacaoElevatoria(new LocalizacaoGeografica());
		fceSesElevatoriaDTO.setLocalizacaoExtravazamento(new LocalizacaoGeografica());
		listTipoComposicoesSelecionadas = new ArrayList<FceSesDadosEstacaoTipoComposicao>();
		listFaixaAdutora = new ArrayList<FaixaDiametroAdutora>();
		listFceSesElevatoriaLocalizacaoGeografica = new ArrayList<FceSesElevatoriaLocalizacaoGeografica>();
		listFceSesElevatoriaDTO = new ArrayList<FceSesElevatoriaDTO>();
		listTipoComposicoes = new ArrayList<FceSesTipoComposicao>();
		listFaixaAdutoraTratEsgoto = new ArrayList<FaixaDiametroAdutora>();
		listFceSesDadosEstacaoTratamentoEsgoto = new ArrayList<FceSesDadosEstacaoTratamentoEsgoto>();
		localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
		listFceSesElevatoriaDTO = new ArrayList<FceSesElevatoriaDTO>();
		listFceCoordenadasLancamentoLocalizacaoGeografica = new ArrayList<FceSesCoordenadasLancamentoLocalizacaoGeografica>();
		fceSesCoordenadasLancamentoLocalizacaoGeografica = new FceSesCoordenadasLancamentoLocalizacaoGeografica();
		fceSesCoordenadasLancamentoLocalizacaoGeografica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		fceSesCoordenadasLancamento = new FceSesCoordenadasLancamento();
		listaCaracteristicaEfluente = new ArrayList<CaracteristicaEfluente>();
		listCaracteristicaLancamento =  new ArrayList<FceSesCaracteristicaLancamento>();
		fceSesCaracteristicaLancamentoSelecionado = new FceSesCaracteristicaLancamento();
		activeTab = 0;
		metodoExterno = new MetodoUtil(this, "atualizarDados");
		btnFinalizar = true;
	}
	
	public void atualizarDados(){
		
		if(!Util.isNullOuVazio(fceSesCoordenadasLancamentoLocalizacaoGeografica.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			if(!StringUtils.isEmpty(fceSesCoordenadasLancamentoLocalizacaoGeografica.getIdeLocalizacaoGeografica().getPontoLatitude())
					&& !StringUtils.isEmpty(fceSesCoordenadasLancamentoLocalizacaoGeografica.getIdeLocalizacaoGeografica().getPontoLongitude())){
				if(!listFceCoordenadasLancamentoLocalizacaoGeografica.contains(fceSesCoordenadasLancamentoLocalizacaoGeografica)){
					listFceCoordenadasLancamentoLocalizacaoGeografica.add(fceSesCoordenadasLancamentoLocalizacaoGeografica);
				}
			}
				fceSesCoordenadasLancamentoLocalizacaoGeografica = new FceSesCoordenadasLancamentoLocalizacaoGeografica();
				fceSesCoordenadasLancamentoLocalizacaoGeografica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		
		if(!Util.isNullOuVazio(fceSesElevatoriaDTO.getLocalizacaoElevatoria()) && !Util.isNullOuVazio(fceSesElevatoriaDTO.getLocalizacaoElevatoria().getIdeLocalizacaoGeografica())){
			if(StringUtils.isEmpty(fceSesElevatoriaDTO.getLocalizacaoElevatoria().getLongitudeInicial())
					|| StringUtils.isEmpty(fceSesElevatoriaDTO.getLocalizacaoElevatoria().getLatitudeInicial())){
				fceSesElevatoriaDTO.setLocalizacaoElevatoria(new LocalizacaoGeografica());
			}
		}
		
		if(!Util.isNullOuVazio(fceSesElevatoriaDTO.getLocalizacaoExtravazamento()) && !Util.isNullOuVazio(fceSesElevatoriaDTO.getLocalizacaoExtravazamento().getIdeLocalizacaoGeografica())){
			if(StringUtils.isEmpty(fceSesElevatoriaDTO.getLocalizacaoExtravazamento().getLatitudeInicial())
					|| StringUtils.isEmpty(fceSesElevatoriaDTO.getLocalizacaoExtravazamento().getLongitudeInicial())){
				fceSesElevatoriaDTO.setLocalizacaoExtravazamento(new LocalizacaoGeografica());
			}
		}
		
		if(!Util.isNullOuVazio(fceSesDadosEstacaoTratamentoEsgoto) && !Util.isNullOuVazio(fceSesDadosEstacaoTratamentoEsgoto.getIdeLocalizacaoGeografica())){
			if(StringUtils.isEmpty(fceSesDadosEstacaoTratamentoEsgoto.getIdeLocalizacaoGeografica().getLatitudeInicial())
					|| StringUtils.isEmpty(fceSesDadosEstacaoTratamentoEsgoto.getIdeLocalizacaoGeografica().getLongitudeInicial())){
				fceSesDadosEstacaoTratamentoEsgoto.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			}
		}
		
	}
	
	public boolean isPontoPreenchido(LocalizacaoGeografica localizacao){
		
		try {
			if(!Util.isNullOuVazio(fceSesFacade.buscarGeometriaShape(localizacao.getIdeLocalizacaoGeografica()))){
				return true;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return false;
		
	}
	
	private void validarFce() {
		try {
			if(this.fce != null && this.fce.getIdeFce() != null){
				Fce fcePai = fceServiceFacade.buscarFcePorIdeFce(this.fce.getIdeFce());
				if(fcePai == null){
					this.fce.setIdeFce(null);
				}
			}
				
			if(fceSes != null && fceSes.getIdeFceSes() != null){
				FceSes ses = fceSesFacade.buscarFceSesByIdeFceSes(fceSes);
				if(ses == null){
					this.fceSes.setIdeFceSes(null);
				}
			}
			
			for(FceSesElevatoriaDTO dto : listFceSesElevatoriaDTO){
				if(dto.getDadosElevatoria() != null && dto.getDadosElevatoria().getIdeFceSesDadosElevatoria() != null){
					FceSesDadosElevatoria elevatoria = fceSesFacade.buscarFceSesDadosElevatoriaByIdeDadosElevatoria(dto.getDadosElevatoria());
					if(elevatoria == null){
						dto.getDadosElevatoria().setIdeFceSesDadosElevatoria(null);
					}
				}
			}
			
			for(FceSesDadosEstacaoTratamentoEsgoto ete : listFceSesDadosEstacaoTratamentoEsgoto){
				if(ete.getIdeFceSesDadosTramentoEsgoto() != null){
					FceSesDadosEstacaoTratamentoEsgoto estacao = fceSesFacade.buscarDadosEstacaoTratamentoEsgotoByIdeDadosEstacao(ete);
					if(estacao == null){
						ete.setIdeFceSesDadosTramentoEsgoto(null);
					}
				}
			}
				
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarCaracteristicaLancamentoEfluente(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica){
		fceLancamentoEfluentesController.setFceOutorgaLocalizacaoGeograficaSelecionada(fceOutorgaLocalizacaoGeografica);
		fceLancamentoEfluentesController.carregarListaCaracterizacaoElfuentes();
		fceLancamentoEfluentesController.carregarListaTipoPeriodoDerivacao();
		fceLancamentoEfluentesController.prepararFceLancamentoEfluentes();
	}
	
	public void selecionarFceLancamentoLocalizacaoGeografica(FceSesCoordenadasLancamentoLocalizacaoGeografica fceSesCoordenadasLancamentoLocalizacaoGeografica){
		
		try {
			fceSesCoordenadasLancamentoLocalizacaoGeografica.setIdeFceSes(fceSes);
			if(!Util.isNullOuVazio(fceSesCoordenadasLancamentoLocalizacaoGeografica.getNomeCoporHidrico())){
				fceSesFacade.salvarFceSesCoordenadasLancamentoLocalizacaoGeografica(fceSesCoordenadasLancamentoLocalizacaoGeografica);
			
			
				this.fceSesCoordenadasLancamentoLocalizacaoGeografica = fceSesCoordenadasLancamentoLocalizacaoGeografica;
				
				listCaracteristicaLancamento = new ArrayList<FceSesCaracteristicaLancamento>();
				listCaracteristicaLancamento.addAll(fceSesCaracteristicaLancamentoService.listarFceSesCaracteristicaLancamento(fceSesCoordenadasLancamentoLocalizacaoGeografica));
				
				
				List<LancamentoCaracterizacaoEfluente> listaLancamentoCaracterizacao = lancamentoEfluenteService.listarCaracterizacaoEfluente(fceSesCoordenadasLancamentoLocalizacaoGeografica);
				listCaracterizacaoEfluenteSelecionados = new ArrayList<CaracterizacaoEfluente>();
				
				for(LancamentoCaracterizacaoEfluente item : listaLancamentoCaracterizacao){
					listCaracterizacaoEfluenteSelecionados.add(item.getIdeCaracterizacaoEfluente());
				}
				
				
				if(Util.isNullOuVazio(listCaracteristicaLancamento)){
					for(CaracteristicaEfluente caracteristicaEfluente : listaCaracteristicaEfluente){
						listCaracteristicaLancamento.add(new FceSesCaracteristicaLancamento(caracteristicaEfluente,fceSesCoordenadasLancamentoLocalizacaoGeografica));
					}
				}else{
					for(FceSesCaracteristicaLancamento lancamento : listCaracteristicaLancamento){
						if(!Util.isNullOuVazio(lancamento.getValorEficienciRemocao())){
							lancamento.setConfirmado(true);	
						}
					}
				}
				Html.exibir("dadosLancamentoEfluente");
			}else{
				MensagemUtil.erro("Corpo Hídrico/Receptor é de preenchimento obrigatório");
			}
			Html.atualizar("efluentes_dialog_ses");
			Html.atualizar("dadosLancamentoEfluente");
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void salvarCaracterizacaoPontoOutorgado(){
		
		try {

			boolean retorno = false;
			
			lancamentoEfluenteService.deletarCaracterizacaoEfluente(fceSesCoordenadasLancamentoLocalizacaoGeografica);
			for(CaracterizacaoEfluente ce : listCaracterizacaoEfluenteSelecionados){
				LancamentoCaracterizacaoEfluente lce = new LancamentoCaracterizacaoEfluente();
				lce.setIdeCaracterizacaoEfluente(ce);
				lce.setIdeCoordenadaFceLancamentoLocalizacaoGeografica(fceSesCoordenadasLancamentoLocalizacaoGeografica);
				
				lancamentoEfluenteService.salvarCaracterizacaoEfluente(lce);
				retorno = true;
			}
			
			fceSesFacade.salvarFceSesCoordenadasLancamentoLocalizacaoGeografica(fceSesCoordenadasLancamentoLocalizacaoGeografica);
			
			for(FceSesCaracteristicaLancamento caracteristica : listCaracteristicaLancamento){
				if(validarFceLancamentoEfluenteCaracteristica(caracteristica)){
					caracteristica.setValorEficienciRemocao(calcularEficienciaRemocao(caracteristica));
					fceSesFacade.salvarFceSesCaracteristicaLancamento(caracteristica);
					retorno = true;
				}else{
					retorno = false;
					break;
				}
			}
			
			if(retorno){
				MensagemUtil.sucesso("Salvo com sucesso!");
			}
			Html.atualizar("formDadosCaracteristasEfluente:tabEfluentes");
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
			
	}
	
	public int getClassificacaoPonto(){
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}

	public FceSes getFceSes() {
		return fceSes;
	}

	public void setFceSes(FceSes fceSes) {
		this.fceSes = fceSes;
	}

	public MetodoUtil getMetodoExterno() {
		return metodoExterno;
	}

	public void setMetodoExterno(MetodoUtil metodoExterno) {
		this.metodoExterno = metodoExterno;
	}


	public List<FaixaDiametroAdutora> getListFaixaAdutora() {
		return listFaixaAdutora;
	}


	public void setListFaixaAdutora(List<FaixaDiametroAdutora> listFaixaAdutora) {
		this.listFaixaAdutora = listFaixaAdutora;
	}


	public List<FceSesElevatoriaLocalizacaoGeografica> getListFceSesElevatoriaLocalizacaoGeografica() {
		return listFceSesElevatoriaLocalizacaoGeografica;
	}


	public void setListFceSesElevatoriaLocalizacaoGeografica(
			List<FceSesElevatoriaLocalizacaoGeografica> listFceSesElevatoriaLocalizacaoGeografica) {
		this.listFceSesElevatoriaLocalizacaoGeografica = listFceSesElevatoriaLocalizacaoGeografica;
	}


	public FceSesElevatoriaLocalizacaoGeografica getFceSesElevatoriaLocalizacaoGeografica() {
		return fceSesElevatoriaLocalizacaoGeografica;
	}

	public void setFceSesElevatoriaLocalizacaoGeografica(
			FceSesElevatoriaLocalizacaoGeografica fceSesElevatoriaLocalizacaoGeografica) {
		this.fceSesElevatoriaLocalizacaoGeografica = fceSesElevatoriaLocalizacaoGeografica;
	}


	public FceSesElevatoriaDTO getFceSesElevatoriaDTO() {
		return fceSesElevatoriaDTO;
	}


	public void setFceSesElevatoriaDTO(FceSesElevatoriaDTO fceSesElevatoriaDTO) {
		this.fceSesElevatoriaDTO = fceSesElevatoriaDTO;
	}


	public List<FceSesElevatoriaDTO> getListFceSesElevatoriaDTO() {
		return listFceSesElevatoriaDTO;
	}


	public void setListFceSesElevatoriaDTO(
			List<FceSesElevatoriaDTO> listFceSesElevatoriaDTO) {
		this.listFceSesElevatoriaDTO = listFceSesElevatoriaDTO;
	}


	public FceSesDadosEstacaoTratamentoEsgoto getFceSesDadosEstacaoTratamentoEsgoto() {
		return fceSesDadosEstacaoTratamentoEsgoto;
	}


	public void setFceSesDadosEstacaoTratamentoEsgoto(
			FceSesDadosEstacaoTratamentoEsgoto fceSesDadosEstacaoTratamentoEsgoto) {
		this.fceSesDadosEstacaoTratamentoEsgoto = fceSesDadosEstacaoTratamentoEsgoto;
	}


	public List<FceSesTipoComposicao> getListTipoComposicoes() {
		return listTipoComposicoes;
	}


	public void setListTipoComposicoes(
			List<FceSesTipoComposicao> listTipoComposicoes) {
		this.listTipoComposicoes = listTipoComposicoes;
	}


	public List<FceSesDadosEstacaoTipoComposicao> getListTipoComposicoesSelecionadas() {
		return listTipoComposicoesSelecionadas;
	}


	public void setListTipoComposicoesSelecionadas(
			List<FceSesDadosEstacaoTipoComposicao> listTipoComposicoesSelecionadas) {
		this.listTipoComposicoesSelecionadas = listTipoComposicoesSelecionadas;
	}


	public List<FaixaDiametroAdutora> getListFaixaAdutoraTratEsgoto() {
		return listFaixaAdutoraTratEsgoto;
	}


	public void setListFaixaAdutoraTratEsgoto(
			List<FaixaDiametroAdutora> listFaixaAdutoraTratEsgoto) {
		this.listFaixaAdutoraTratEsgoto = listFaixaAdutoraTratEsgoto;
	}


	public LocalizacaoGeografica getLocalizacaoGeograficaSelecionada() {
		return localizacaoGeograficaSelecionada;
	}


	public void setLocalizacaoGeograficaSelecionada(
			LocalizacaoGeografica localizacaoGeograficaSelecionada) {
		this.localizacaoGeograficaSelecionada = localizacaoGeograficaSelecionada;
	}


	public List<FceSesDadosEstacaoTratamentoEsgoto> getListFceSesDadosEstacaoTratamentoEsgoto() {
		return listFceSesDadosEstacaoTratamentoEsgoto;
	}


	public void setListFceSesDadosEstacaoTratamentoEsgoto(
			List<FceSesDadosEstacaoTratamentoEsgoto> listFceSesDadosEstacaoTratamentoEsgoto) {
		this.listFceSesDadosEstacaoTratamentoEsgoto = listFceSesDadosEstacaoTratamentoEsgoto;
	}


	public FceSesDadosEstacaoTratamentoEsgoto getFceSesDadosEstacaoTratamentoEsgotoSelecinado() {
		return fceSesDadosEstacaoTratamentoEsgotoSelecinado;
	}


	public void setFceSesDadosEstacaoTratamentoEsgotoSelecinado(
			FceSesDadosEstacaoTratamentoEsgoto fceSesDadosEstacaoTratamentoEsgotoSelecinado) {
		this.fceSesDadosEstacaoTratamentoEsgotoSelecinado = fceSesDadosEstacaoTratamentoEsgotoSelecinado;
	}

	public FceSesCoordenadasLancamentoLocalizacaoGeografica getFceSesCoordenadasLancamentoLocalizacaoGeografica() {
		return fceSesCoordenadasLancamentoLocalizacaoGeografica;
	}


	public void setFceSesCoordenadasLancamentoLocalizacaoGeografica(
			FceSesCoordenadasLancamentoLocalizacaoGeografica fceSesCoordenadasLancamentoLocalizacaoGeografica) {
		this.fceSesCoordenadasLancamentoLocalizacaoGeografica = fceSesCoordenadasLancamentoLocalizacaoGeografica;
	}


	public List<FceSesCoordenadasLancamentoLocalizacaoGeografica> getListFceCoordenadasLancamentoLocalizacaoGeografica() {
		return listFceCoordenadasLancamentoLocalizacaoGeografica;
	}


	public void setListFceCoordenadasLancamentoLocalizacaoGeografica(
			List<FceSesCoordenadasLancamentoLocalizacaoGeografica> listFceCoordenadasLancamentoLocalizacaoGeografica) {
		this.listFceCoordenadasLancamentoLocalizacaoGeografica = listFceCoordenadasLancamentoLocalizacaoGeografica;
	}


	public FceSesCoordenadasLancamento getFceSesCoordenadasLancamento() {
		return fceSesCoordenadasLancamento;
	}


	public void setFceSesCoordenadasLancamento(
			FceSesCoordenadasLancamento fceSesCoordenadasLancamento) {
		this.fceSesCoordenadasLancamento = fceSesCoordenadasLancamento;
	}


	public List<FceSesCaracteristicaLancamento> getListCaracteristicaLancamento() {
		return listCaracteristicaLancamento;
	}


	public void setListCaracteristicaLancamento(
			List<FceSesCaracteristicaLancamento> listCaracteristicaLancamento) {
		this.listCaracteristicaLancamento = listCaracteristicaLancamento;
	}


	public List<CaracteristicaEfluente> getListaCaracteristicaEfluente() {
		return listaCaracteristicaEfluente;
	}


	public void setListaCaracteristicaEfluente(
			List<CaracteristicaEfluente> listaCaracteristicaEfluente) {
		this.listaCaracteristicaEfluente = listaCaracteristicaEfluente;
	}


	public FceSesCaracteristicaLancamento getFceSesCaracteristicaLancamentoSelecionado() {
		return fceSesCaracteristicaLancamentoSelecionado;
	}


	public void setFceSesCaracteristicaLancamentoSelecionado(
			FceSesCaracteristicaLancamento fceSesCaracteristicaLancamentoSelecionado) {
		this.fceSesCaracteristicaLancamentoSelecionado = fceSesCaracteristicaLancamentoSelecionado;
	}


	public boolean isBtnFinalizar() {
		return btnFinalizar;
	}


	public void setBtnFinalizar(boolean btnFinalizar) {
		this.btnFinalizar = btnFinalizar;
	}


	public List<TipoPeriodoDerivacao> getListPeriodoDerivacao() {
		return listPeriodoDerivacao;
	}


	public void setListPeriodoDerivacao(
			List<TipoPeriodoDerivacao> listPeriodoDerivacao) {
		this.listPeriodoDerivacao = listPeriodoDerivacao;
	}


	public List<CaracterizacaoEfluente> getListCaracterizacaoEfluente() {
		return listCaracterizacaoEfluente;
	}


	public void setListCaracterizacaoEfluente(
			List<CaracterizacaoEfluente> listCaracterizacaoEfluente) {
		this.listCaracterizacaoEfluente = listCaracterizacaoEfluente;
	}


	public List<CaracterizacaoEfluente> getListCaracterizacaoEfluenteSelecionados() {
		return listCaracterizacaoEfluenteSelecionados;
	}


	public void setListCaracterizacaoEfluenteSelecionados(
			List<CaracterizacaoEfluente> listCaracterizacaoEfluenteSelecionados) {
		this.listCaracterizacaoEfluenteSelecionados = listCaracterizacaoEfluenteSelecionados;
	}


}
