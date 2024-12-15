package br.gov.ba.seia.controller;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.enumerator.TipoPessoaEnum;
import br.gov.ba.seia.facade.PessoaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;

/**
 * Controller genérico responsável pelo <i>dialogSelecionarSolicitante.xhtml</i>.
 * 
 * @author eduardo.fernandes 
 */
@Named("selecionarSolicitanteController")
@ViewScoped
public class SelecionarSolicitanteController{

	@EJB
	private PessoaFacade pessoaFacade;
	
	private LazyDataModel<PessoaFisica> pessoasFisicaModel;
	private LazyDataModel<PessoaJuridica> pessoasJuridicaModel;

	private String solicitante; //Nome ou Razão Social
	private String numDocumento; // CPF ou CNPJ
	private Integer tipoPessoa = 0;

	private Pessoa solicitanteSelecionado;

	private boolean renderizaJuridica;
	private boolean renderizaFisica;
	private boolean renderizaTipoPessoa = true;	
	private MetodoUtil metodoSelecionarPessoa;
	private String title=null;
	
	public void init(ActionEvent evt){
		  this.title = !Util.isNull(evt.getComponent().getAttributes().get("title"))
		  ? evt.getComponent().getAttributes().get("title").toString() :null;
		 		this.metodoSelecionarPessoa = (MetodoUtil) evt.getComponent().getAttributes().get("metodoSelecionarSolicitante");
				/*
				 * System.out.println(evt.getComponent().getAttributes().get("title").toString()
				 * );
				 */	
	}
	
	public void limpar(){
		enviarSolicitante();
		tipoPessoa = 0;
		renderizaTipoPessoa = true;
		changeSolicitante();
		metodoSelecionarPessoa=null;
		title="Selecionar Solicitante";
	}

	private void enviarSolicitante() {
		try {
			if(!Util.isNull(metodoSelecionarPessoa)){
				metodoSelecionarPessoa.executeMethod(solicitanteSelecionado);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void changeSolicitante(){
		pessoasFisicaModel = null;
		pessoasJuridicaModel = null;
		solicitante = null;
		numDocumento = null;
		renderizaFisica = false;
		renderizaJuridica = false;
	}

	public void consultarSolicitante() {
		if(isTipoSolicitantePessoaFisica()){
			consultarPessoaFisica();
			renderizaFisica = true;
			renderizaJuridica = false;
		} 
		else if(isTipoSolicitantePessoaJuridica()){
			consultarPessoaJuridica();
			renderizaFisica = false;
			renderizaJuridica = true;
		}
	}

	private void consultarPessoaFisica() {
		try {
			carregarLazyModelPessoaFisica(new PessoaFisica(solicitante, numDocumento));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Ocorreu um erro ao carregar a lista de Pessoas Físicas.");
			throw Util.capturarException(e ,Util.SEIA_EXCEPTION);
		}		
	}

	@SuppressWarnings("serial")
	private void carregarLazyModelPessoaFisica(final PessoaFisica pessoaSelecionada) throws Exception {
		pessoasFisicaModel = new LazyDataModel<PessoaFisica>() {
			@Override
			public List<PessoaFisica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<PessoaFisica> pessoasFisicas = null;
				try {
					setPageSize(pageSize);
					pessoasFisicas = populateListPessoaFisica(pessoaSelecionada, first, pageSize);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e ,Util.SEIA_EXCEPTION);
				}
				return pessoasFisicas;
			}
		};
		pessoasFisicaModel.setRowCount(getRowCount(pessoaSelecionada));
	}

	protected List<PessoaFisica> populateListPessoaFisica(PessoaFisica pessoaFisicaSelecionada, int first, int pageSize) throws Exception {
		return pessoaFacade.listarPessoaFisica(pessoaFisicaSelecionada, first, pageSize);
	}

	protected int getRowCount(PessoaFisica pessoaFisicaSelecionada) throws Exception {
		return pessoaFacade.getRowCountPessoaFisica(pessoaFisicaSelecionada);
	}

	private void consultarPessoaJuridica() {
		try {
			carregarLazyModelPessoaJuridica(new PessoaJuridica(null, solicitante, numDocumento));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Ocorreu um erro ao carregar a lista de Pessoas Jurídicas.");
			throw Util.capturarException(e ,Util.SEIA_EXCEPTION);
		}
	}

	@SuppressWarnings("serial")
	private void carregarLazyModelPessoaJuridica(final PessoaJuridica pessoaSelecionada) throws Exception {
		pessoasJuridicaModel = new LazyDataModel<PessoaJuridica>() {
			@Override
			public List<PessoaJuridica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<PessoaJuridica> pessoasJuridicas = null;
				try {
					setPageSize(pageSize);
					pessoasJuridicas = populateListJuridica(pessoaSelecionada, first, pageSize);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e ,Util.SEIA_EXCEPTION);
				}
				return pessoasJuridicas;
			}
		};
		pessoasJuridicaModel.setRowCount(getRowCountJuridica(pessoaSelecionada));
	}

	protected List<PessoaJuridica> populateListJuridica(PessoaJuridica pessoaJuridicaSelecionada, int first, int pageSize) throws Exception {
		return pessoaFacade.listarPessoaJuridica(pessoaJuridicaSelecionada, first, pageSize);
	}

	protected int getRowCountJuridica(PessoaJuridica pessoaJuridicaSelecionada) throws Exception {
		return pessoaFacade.getRowCountPessoaJuridica(pessoaJuridicaSelecionada);
	}

	public boolean isTipoSolicitantePessoaFisica(){
		return !Util.isNull(tipoPessoa) && tipoPessoa.equals(TipoPessoaEnum.FISICA.getId());
	}

	public boolean isTipoSolicitantePessoaJuridica(){
		return !Util.isNull(tipoPessoa) && tipoPessoa.equals(TipoPessoaEnum.JURIDICA.getId());
	}

	/*
	 * 
	 * getters && setters
	 * 
	 */
	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public Integer getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(Integer tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public boolean isRenderizaFisica() {
		return renderizaFisica;
	}

	public boolean isRenderizaJuridica() {
		return renderizaJuridica;
	}

	public LazyDataModel<PessoaFisica> getPessoasFisicaModel() {
		return pessoasFisicaModel;
	}

	public void setPessoasFisicaModel(LazyDataModel<PessoaFisica> pessoasFisicaModel) {
		this.pessoasFisicaModel = pessoasFisicaModel;
	}

	public LazyDataModel<PessoaJuridica> getPessoasJuridicaModel() {
		return pessoasJuridicaModel;
	}

	public void setPessoasJuridicaModel(LazyDataModel<PessoaJuridica> pessoasJuridicaModel) {
		this.pessoasJuridicaModel = pessoasJuridicaModel;
	}

	public Pessoa getSolicitanteSelecionado() {
		return solicitanteSelecionado;
	}

	public void setSolicitanteSelecionado(Pessoa solicitanteSelecionado) {
		this.solicitanteSelecionado = solicitanteSelecionado;
	}

	public boolean isRenderizaTipoPessoa() {
		return renderizaTipoPessoa;
	}

	public void setRenderizaTipoPessoa(boolean renderizaTipoPessoa) {
		this.renderizaTipoPessoa = renderizaTipoPessoa;
	}

	public void setRenderizaJuridica(boolean renderizaJuridica) {
		this.renderizaJuridica = renderizaJuridica;
	}

	public void setRenderizaFisica(boolean renderizaFisica) {
		this.renderizaFisica = renderizaFisica;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}