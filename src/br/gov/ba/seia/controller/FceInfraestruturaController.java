package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FceOutorgaInfraestrutura;
import br.gov.ba.seia.entity.FceOutorgaTipoInfraestrutura;
import br.gov.ba.seia.entity.FceOutorgaTipoInfraestruturaUtilizada;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoPeriodoDerivacaoEnum;
import br.gov.ba.seia.facade.FceInfraestruturaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemLacFceUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

@Named("fceInfraestruturaController")
@ViewScoped
public class FceInfraestruturaController extends FceController {
	
	@EJB
	private FceInfraestruturaFacade facade;
	
	private FceOutorgaInfraestrutura fceOutorgaInfraestrutura;
	private List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao;
	private List<FceOutorgaTipoInfraestrutura> listaTodosFceOutorgaTipoInfraestrutura;
	private boolean opcaoOutrosSelecionada;
	private static final DocumentoObrigatorio DOC_OBRIGATORIO_INFRAESTRUTURA = 
			new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_INFRAESTRUTURA.getId(), "Formulário de Caracterização do Empreendimento - Infraestrutura");
	
	@Override
	public void init() {
		limpar();
		verificarEdicao();
		carregarAba();
		
		if(!isFceSalvo()){
			iniciarFce(DOC_OBRIGATORIO_INFRAESTRUTURA);
			fceOutorgaInfraestrutura = new FceOutorgaInfraestrutura(super.fce);
		} else {
			carregarFceOutorgaInfraestruturaEFilhos();
		}
	}

	private void carregarFceOutorgaInfraestruturaEFilhos() {
		fceOutorgaInfraestrutura = facade.buscarFceOutorgaInfraestruturaPorFce(super.fce);
		
		fceOutorgaInfraestrutura.setFceOutorgaTipoInfraestruturaUtilizadaCollection(
				facade.listarFceOutorgaTipoInfraestruturaUtilizadaPorFceOutorgaTipoInfraestrutura(fceOutorgaInfraestrutura));
		
		for (FceOutorgaTipoInfraestrutura tipoInfra : listaTodosFceOutorgaTipoInfraestrutura) {
			
			for (FceOutorgaTipoInfraestruturaUtilizada tipoInfraUtilizada : fceOutorgaInfraestrutura.getFceOutorgaTipoInfraestruturaUtilizadaCollection()) {
				
				if(tipoInfra.getIdeFceOutorgaTipoInfraestrutura().equals(tipoInfraUtilizada.getIdeFceOutorgaTipoInfraestrutura().getIdeFceOutorgaTipoInfraestrutura())) {
					tipoInfra.setIndChecked(true);
					verificaOpcaoOutros(tipoInfra, true);
				}
			}
		}
	}
	
	@Override
	public void limpar() {
		opcaoOutrosSelecionada = false;
	}

	@Override
	public void verificarEdicao() {
		if(!Util.isNullOuVazio(super.requerimento)){
			carregarFceDoRequerente(DOC_OBRIGATORIO_INFRAESTRUTURA);
		}
	}

	@Override
	public void carregarAba() {
		carregarListaTipoPeriodoDerivacao();
		carregarListaFceOutorgaTipoInfraestrutura();
		
	}
	
	private void carregarListaTipoPeriodoDerivacao() {
		List<TipoPeriodoDerivacaoEnum> listaTemp = new ArrayList<TipoPeriodoDerivacaoEnum>();
		listaTemp.add(TipoPeriodoDerivacaoEnum.CONTINUO);
		listaTemp.add(TipoPeriodoDerivacaoEnum.INTERMITENTE);
		
		listaTipoPeriodoDerivacao = facade.listarTipoPeriodoDerivacaoPorListaTipoPeriodoDerivacaoEnum(listaTemp);
	}
	
	private void carregarListaFceOutorgaTipoInfraestrutura() {
		listaTodosFceOutorgaTipoInfraestrutura = facade.listarTodosFceOutorgaTipoInfraestrutura();
	}
	
	public boolean getRenderizaTempoCaptacao() {
		if(fceOutorgaInfraestrutura != null && !Util.isNullOuVazio(fceOutorgaInfraestrutura.getIdeTipoPeriodoDerivacao())
				&& fceOutorgaInfraestrutura.getIdeTipoPeriodoDerivacao().getIdeTipoPeriodoDerivacao().equals(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId())) {
			return true;
		} else return false;
	}
	
	public boolean getRenderizaHintUtilidadePublica(String nome) {
		if(!Util.isNullOuVazio(nome) && nome.equals("Obras de infraestrutura hídrica de utilidade pública")) return true;
		return false;
	}
	
	public boolean getRenderizaHintInteresseSocial(String nome) {
		if(!Util.isNullOuVazio(nome) && nome.equals("Obras de infraestrutura hídrica de interesse social")) return true;
		return false;
	}
	
	public void verificaOpcaoOutros(FceOutorgaTipoInfraestrutura tipoInfra, boolean isLoading){
		if(tipoInfra != null && tipoInfra.getDscFceTipoInfraestrutura().equals("Construção de outras obras de infraestrutura")) {
			
			if(tipoInfra.isIndChecked()) {
				opcaoOutrosSelecionada = true;
				if(!isLoading) MensagemLacFceUtil.exibirInformacao001();
			} else {
				opcaoOutrosSelecionada = false;
			}
		}
		
		Html.atualizar("formInfraestrutura:botoesFceInfraestrutura");
	}
	
	public void mudarTipoDerivacao(AjaxBehaviorEvent event) {
		if(fceOutorgaInfraestrutura.getIdeTipoPeriodoDerivacao().getIdeTipoPeriodoDerivacao().equals(TipoPeriodoDerivacaoEnum.CONTINUO.getId())) {
			fceOutorgaInfraestrutura.setValTempoCaptacao(null);
		}
	}
	
	@Override
	public boolean validarAba() {
		
		if(fceOutorgaInfraestrutura != null) {
			
			if(Util.isNullOuVazio(fceOutorgaInfraestrutura.getValVazao())) {
				MensagemUtil.msg0003("Vazão");
				return false;
			} else if(!calculaLimiteVazoes()) {
				return false;
			}
			
			if(Util.isNullOuVazio(fceOutorgaInfraestrutura.getIdeTipoPeriodoDerivacao())) {
				MensagemUtil.msg0003("Período de derivação");
				return false;
			} else {
				if(fceOutorgaInfraestrutura.getIdeTipoPeriodoDerivacao().getIdeTipoPeriodoDerivacao().equals(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId())) {
					if(Util.isNullOuVazio(fceOutorgaInfraestrutura.getValTempoCaptacao())) {
						MensagemUtil.msg0003("Tempo de captação");
						return false;
					} else if(fceOutorgaInfraestrutura.getValTempoCaptacao() < 1 || fceOutorgaInfraestrutura.getValTempoCaptacao() > 24) {
							MensagemUtil.alerta("O tempo de captação tem que ser maior que 0h e menor que 24h");
							return false;
					}
				}
			}
			
			if(!Util.isNullOuVazio(listaTodosFceOutorgaTipoInfraestrutura)) {
				boolean algumTipoInfraSelecionado = false;
				
				for (FceOutorgaTipoInfraestrutura tipoInfra : listaTodosFceOutorgaTipoInfraestrutura) {
					if(tipoInfra.isIndChecked()) {
						algumTipoInfraSelecionado = true;
						break;
					}
				}
				
				if(!algumTipoInfraSelecionado) {
					MensagemUtil.msg0003("Tipo de infraestrutura");
					return false;
				}
			}
		} else {
			MensagemUtil.erro008();
			return false;
		}
		
		return true;
	}
	
	private boolean calculaLimiteVazoes() {
		BigDecimal numVazao = BigDecimal.valueOf(fceOutorgaInfraestrutura.getValVazao()).setScale(2, RoundingMode.HALF_UP);
		BigDecimal vazaoTotal =BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP);
		
		List<OutorgaLocalizacaoGeografica> outorgas = facade.listarOutorgaLocalizacaoGeograficaPorRequerimento(requerimento);
		
		if(!Util.isNullOuVazio(outorgas)) {
			for (OutorgaLocalizacaoGeografica olg : outorgas) {
				if(!Util.isNullOuVazio(olg.getNumVazao())) {
					vazaoTotal = vazaoTotal.add(olg.getNumVazao());
				}
			}
			
			if(numVazao.compareTo(vazaoTotal) == 1) {
				MensagemUtil.alerta("O valor da vazão não pode ultrapassar a soma das vazões selecionadas anteriormente. Vazão total cadastrada = " 
						+ vazaoTotal.toString() + " (m³/dia).");
				
				return false;
			}
		}
		
		return true;
	}
	
	private void montarListaFceOutorgaTipoInfraestruturaUtilizada() {
		fceOutorgaInfraestrutura.setFceOutorgaTipoInfraestruturaUtilizadaCollection(new ArrayList<FceOutorgaTipoInfraestruturaUtilizada>());
		
		for (FceOutorgaTipoInfraestrutura tipoInfra : listaTodosFceOutorgaTipoInfraestrutura) {
			if(tipoInfra.isIndChecked()) {
				FceOutorgaTipoInfraestruturaUtilizada tipoInfraUtilizada = new FceOutorgaTipoInfraestruturaUtilizada(fceOutorgaInfraestrutura, tipoInfra);
				fceOutorgaInfraestrutura.getFceOutorgaTipoInfraestruturaUtilizadaCollection().add(tipoInfraUtilizada);
			}
		}
	}
	
	private void salvarFceOutorgaInfraestrutura() {
		fceOutorgaInfraestrutura.setIdeFce(fce);
		facade.salvarFceOutorgaInfraestrutura(fceOutorgaInfraestrutura);
		
		facade.excluirFceOutorgaTipoInfraestruturaUtilizadaPorFceOutorgaInfraestrutura(fceOutorgaInfraestrutura);
		
		for (FceOutorgaTipoInfraestruturaUtilizada tipoInfraUtilizada : fceOutorgaInfraestrutura.getFceOutorgaTipoInfraestruturaUtilizadaCollection()) {
			tipoInfraUtilizada.setIdeFceOutorgaInfraestrutura(fceOutorgaInfraestrutura);
			facade.salvarFceOutorgaTipoInfraestruturaUtilizada(tipoInfraUtilizada);
		}
	}
	
	@Override
	public void prepararParaFinalizar() throws Exception {
		
		if(opcaoOutrosSelecionada) {
			fce.setIndConcluido(false);
			super.salvarFce();
		} else {
			super.concluirFce();
		}
		
		montarListaFceOutorgaTipoInfraestruturaUtilizada();
		salvarFceOutorgaInfraestrutura();
		
		if(!isFceSalvo()){
			super.exibirMensagem001();
		} else {
			super.exibirMensagem002();
		}
		
		Html.esconder("dialogInfraestrutura");
		if(fce.isIndConcluido()) Html.exibir("relatorioInfraestrutura");
	}
	
	@Override
	public void finalizar() {
		if(validarAba()) {
			facade.finalizar(this);
		}
	}

	@Override
	protected void prepararDuplicacao() {
		fceOutorgaInfraestrutura.setIdeFceOutorgaInfraestrutura(null);
		fceOutorgaInfraestrutura.setIdeFce(null);
		
		for (FceOutorgaTipoInfraestruturaUtilizada tipoInfraUtilizada : fceOutorgaInfraestrutura.getFceOutorgaTipoInfraestruturaUtilizadaCollection()) {
			tipoInfraUtilizada.setIdeFceOutorgaTipoInfraestruturaUtilizada(null);
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			super.salvarFce();
			salvarFceOutorgaInfraestrutura();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico() {
		carregarAba();
		carregarFceOutorgaInfraestruturaEFilhos();
	}

	@Override
	public void abrirDialog() {
		Html.exibir("dialogInfraestrutura");
		Html.atualizar("formInfraestrutura");
	}
	
	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOC_OBRIGATORIO_INFRAESTRUTURA);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Infraestrutura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/*********************
	/*					 *
	//XXX: GETS AND SETS *
	/* 					 *
	/*********************/

	public List<TipoPeriodoDerivacao> getListaTipoPeriodoDerivacao() {
		return listaTipoPeriodoDerivacao;
	}

	public void setListaTipoPeriodoDerivacao(List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao) {
		this.listaTipoPeriodoDerivacao = listaTipoPeriodoDerivacao;
	}

	public FceOutorgaInfraestrutura getFceOutorgaInfraestrutura() {
		return fceOutorgaInfraestrutura;
	}

	public void setFceOutorgaInfraestrutura(FceOutorgaInfraestrutura fceOutorgaInfraestrutura) {
		this.fceOutorgaInfraestrutura = fceOutorgaInfraestrutura;
	}

	public List<FceOutorgaTipoInfraestrutura> getListaTodosFceOutorgaTipoInfraestrutura() {
		return listaTodosFceOutorgaTipoInfraestrutura;
	}

	public void setListaTodosFceOutorgaTipoInfraestrutura(List<FceOutorgaTipoInfraestrutura> listaTodosFceOutorgaTipoInfraestrutura) {
		this.listaTodosFceOutorgaTipoInfraestrutura = listaTodosFceOutorgaTipoInfraestrutura;
	}

	public boolean isOpcaoOutrosSelecionada() {
		return opcaoOutrosSelecionada;
	}

	public void setOpcaoOutrosSelecionada(boolean opcaoOutrosSelecionada) {
		this.opcaoOutrosSelecionada = opcaoOutrosSelecionada;
	}
}