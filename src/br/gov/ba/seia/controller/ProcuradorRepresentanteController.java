package br.gov.ba.seia.controller;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaHistorico;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaHistoricoService;
import br.gov.ba.seia.service.ProcuradorPessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorRepresentanteService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

@Named("procuradorRepresentanteController")
@ViewScoped
public class ProcuradorRepresentanteController {
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");

	private PessoaFisica pessoaFisica;
	private PessoaJuridica pessoaJuridica;
	private PessoaFisica pessoaSelecionada;
	private PessoaFisica pessoaPesquisa;
	private Boolean showRepresentanteLegal;
	private DataModel<ProcuradorRepresentante> modelProcuradorRepresentante;
	private ProcuradorRepresentante procuradorRepresentanteSelecionado;

	private Boolean emptyList;
	
	private boolean showExpandirFormCadastro;
	private boolean renderedFormPessoaFisicaProcurador;
	private boolean disabledFormPessoaFisicaProcurador;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private ProcuradorRepresentanteService procuradorRepresentanteService;
	
	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;
	
	@EJB
	private PessoaJuridicaHistoricoService pessoaJuridicaHistoricoService;
	
	@PostConstruct
	public void init(){
		pessoaFisica = new PessoaFisica();
		pessoaSelecionada = new PessoaFisica();
		pessoaPesquisa = new PessoaFisica();
		showRepresentanteLegal = true;
		procuradorRepresentanteSelecionado = new ProcuradorRepresentante();
		tratarPessoaJuridicaSessao();
		carregarListaProcuradorRepresentante();
		showExpandirFormCadastro = false;
	}
	
	public void salvarPessoa() {
	
		try {
			
			pessoaSelecionada.setNumCpf(pessoaPesquisa.getNumCpf());
			pessoaSelecionada.setTipSexo(null);
			
			salvarProcurador();
			
			showExpandirFormCadastro = false;
			
			pessoaJuridicaHistoricoService.salvarPessoaJuridicaHistorico(preencherHistoricoPessoaJuridica(true));
			
			carregarListaProcuradorRepresentante();
			
			JsfUtil.addSuccessMessage("Inclusão realizada com sucesso!");
			
			limparObjetosPessoaFisicaAction();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
	
	public PessoaJuridicaHistorico preencherHistoricoPessoaJuridica(Boolean adicaoRemocao) {
		PessoaJuridicaHistorico pessoaJuridicaHistorico = new PessoaJuridicaHistorico();
		PessoaJuridicaHistorico oldPessoaJuridicaHistorico = new PessoaJuridicaHistorico();
		
		oldPessoaJuridicaHistorico = pessoaJuridicaHistoricoService.buscarHistoricoAnterior(pessoaJuridica);
		
		pessoaJuridicaHistorico.setIdePessoaJuridica(pessoaJuridica);
		pessoaJuridicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaJuridicaHistorico.setDtcHistorico(new Date());
		pessoaJuridicaHistorico.setIdeProcuradorRepresentante(procuradorRepresentanteSelecionado);
		pessoaJuridicaHistorico.setStatusProcurador(adicaoRemocao);
		
		if(!Util.isNullOuVazio(pessoaJuridica.getNumInscricaoMunicipal())) {
			pessoaJuridicaHistorico.setNumInscricaoMunicipal(pessoaJuridica.getNumInscricaoMunicipal());
		}
		
		if(!Util.isNullOuVazio(oldPessoaJuridicaHistorico)) {
			pessoaJuridicaHistorico.setDscCaminhoArquivo(oldPessoaJuridicaHistorico.getDscCaminhoArquivo());
			pessoaJuridicaHistorico.setIdeProcesso(oldPessoaJuridicaHistorico.getIdeProcesso());
			pessoaJuridicaHistorico.setNomeFantasia(oldPessoaJuridicaHistorico.getNomeFantasia());
			pessoaJuridicaHistorico.setDtcAbertura(oldPessoaJuridicaHistorico.getDtcAbertura());
			pessoaJuridicaHistorico.setNomNaturezaJuridica(oldPessoaJuridicaHistorico.getNomNaturezaJuridica());
		} 
		
		
		return pessoaJuridicaHistorico;
	}

	private void salvarProcurador() throws Exception {
		
		if(procuradorExisteNaLista(pessoaSelecionada,modelProcuradorRepresentante)){
			return;
		}
		if (procuradorRepresentanteSelecionado == null) {
			procuradorRepresentanteSelecionado = new ProcuradorRepresentante();
		}
		procuradorRepresentanteSelecionado.setIdeProcurador(pessoaSelecionada);
		procuradorRepresentanteSelecionado.setDtcInicio(new Date());
		procuradorRepresentanteSelecionado.setIndExcluido(false);
		procuradorRepresentanteSelecionado.setIdePessoaJuridica(pessoaJuridica);
		procuradorRepresentanteService.salvarProcuradorRepresentante(procuradorRepresentanteSelecionado);
	}
	
	private boolean procuradorExisteNaLista(PessoaFisica pessoaSelecionada,DataModel<ProcuradorRepresentante> modelProcuradorRepresentante) {
		if (!Util.isNull(modelProcuradorRepresentante)) {
			for(ProcuradorRepresentante pr: modelProcuradorRepresentante){
				if(pr.getIdeProcurador().getNumCpf().equals(pessoaSelecionada.getNumCpf())){
					return true;
				}
			}
		}
		return false;
		
	}


	public void consultarPessoaFisicaPorCpf() {
		try {
			this.pessoaSelecionada = pessoaFisicaService.filtrarPessoaFisicaByCpf(pessoaPesquisa);
			if (!Util.isNull(pessoaSelecionada)) {
				disabledFormPessoaFisicaProcurador = true;
				renderedFormPessoaFisicaProcurador = true;
			}
			else {
				MensagemUtil.alerta(BUNDLE.getString("procuradorMsgErroCpfNaoCadastrado"));
			}
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void limparObjetosPessoaFisicaAction(){
		limparObjetos();
	}
	
	public void limparObjetos(){
		pessoaPesquisa = new PessoaFisica();
		pessoaSelecionada = new PessoaFisica();
		procuradorRepresentanteSelecionado = new ProcuradorRepresentante();
		renderedFormPessoaFisicaProcurador=false;
		disabledFormPessoaFisicaProcurador=false;
		
	}
	
	public void carregarListaProcuradorRepresentante() {
		try {
			if (procuradorRepresentanteSelecionado == null) {
				procuradorRepresentanteSelecionado = new ProcuradorRepresentante();
			}
			
			if (!Util.isNull(pessoaJuridica) && !Util.isNull(pessoaJuridica.getIdePessoaJuridica())) {
				procuradorRepresentanteSelecionado.setIdePessoaJuridica(pessoaJuridica);
				List<ProcuradorRepresentante> listaProcuradorRepresentante = (List<ProcuradorRepresentante>) procuradorRepresentanteService.getListaProcuradorRepresentante(procuradorRepresentanteSelecionado);
				this.modelProcuradorRepresentante = new ListDataModel<ProcuradorRepresentante>(listaProcuradorRepresentante);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirProcuradorRepresentante() {
		procuradorRepresentanteSelecionado.setIndExcluido(true);
		procuradorRepresentanteSelecionado.setDtcExclusao(new Date());
		try {
			procuradorRepresentanteService.excluirProcuradorRepresentante(procuradorRepresentanteSelecionado);
			carregarListaProcuradorRepresentante();
			pessoaJuridicaHistoricoService.salvarPessoaJuridicaHistorico(preencherHistoricoPessoaJuridica(false));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void tratarPessoaJuridicaSessao() {
		if (!Util.isNull(ContextoUtil.getContexto().getPessoaJuridica())) {
			pessoaJuridica = ContextoUtil.getContexto().getPessoaJuridica();
			if (Util.isNull(pessoaJuridica)) {
				pessoaJuridica = new PessoaJuridica();
			} 
		}
	}
	
	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}
	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}
	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}
	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
		carregarListaProcuradorRepresentante();
	}

	public PessoaFisica getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(PessoaFisica pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}

	public PessoaFisica getPessoaPesquisa() {
		return pessoaPesquisa;
	}

	public void setPessoaPesquisa(PessoaFisica pessoaPesquisa) {
		this.pessoaPesquisa = pessoaPesquisa;
	}

	public Boolean getShowRepresentanteLegal() {
		return showRepresentanteLegal;
	}


	public void setShowRepresentanteLegal(Boolean showRepresentanteLegal) {
		this.showRepresentanteLegal = showRepresentanteLegal;
	}
	
	public DataModel<ProcuradorRepresentante> getModelProcuradorRepresentante() {
		return modelProcuradorRepresentante;
	}


	public void setModelProcuradorRepresentante(
			DataModel<ProcuradorRepresentante> modelProcuradorRepresentante) {
		this.modelProcuradorRepresentante = modelProcuradorRepresentante;
	}


	public ProcuradorRepresentante procurador() {
		return procuradorRepresentanteSelecionado;
	}


	public void setProcuradorRepresentanteSelecionado(
			ProcuradorRepresentante procuradorRepresentanteSelecionado) {
		this.procuradorRepresentanteSelecionado = procuradorRepresentanteSelecionado;
	}

	public ProcuradorRepresentante getProcuradorRepresentanteSelecionado() {
		return procuradorRepresentanteSelecionado;
	}


	public Boolean getEmptyList() {
		if (Util.isNull(modelProcuradorRepresentante) || modelProcuradorRepresentante.getRowCount() == 0) {
			emptyList = false;
		} else {
			emptyList = true;
		}
		return emptyList;
	}


	public void setEmptyList(Boolean emptyList) {
		this.emptyList = emptyList;
	}


	public boolean getShowExpandirFormCadastro() {
		return showExpandirFormCadastro;
	}


	public void setShowExpandirFormCadastro(boolean showExpandirFormCadastro) {
		this.showExpandirFormCadastro = showExpandirFormCadastro;
	}

	public boolean isRenderedFormPessoaFisicaProcurador() {
		return renderedFormPessoaFisicaProcurador;
	}

	public void setRenderedFormPessoaFisicaProcurador(boolean renderedFormPessoaFisicaProcurador) {
		this.renderedFormPessoaFisicaProcurador = renderedFormPessoaFisicaProcurador;
	}

	public boolean isDisabledFormPessoaFisicaProcurador() {
		return disabledFormPessoaFisicaProcurador;
	}

	public void setDisabledFormPessoaFisicaProcurador(boolean disabledFormPessoaFisicaProcurador) {
		this.disabledFormPessoaFisicaProcurador = disabledFormPessoaFisicaProcurador;
	}
	
}
