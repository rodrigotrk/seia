package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.enumerator.EstadoEnum;
import br.gov.ba.seia.facade.EnderecoGenericoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;

@Named("enderecoGenericoController")
@ViewScoped
public class EnderecoGenericoController {
	
	@EJB
	private EnderecoGenericoFacade facade;

	private String cepConsultado;
	private String idForm;

	private Endereco endereco;
	
	private Collection<Logradouro> listaLogradouro;
	private Collection<TipoLogradouro> listaTipoLogradouro;

	private Collection<Bairro> listaBairro;
	
	private Collection<Estado> listaEstado;
	private Collection<Municipio> listaMunicipio;

	private MetodoUtil metodo;
	
	private boolean visualizacao;
	
	private boolean bairroOutroInexibilidade;
	
	private boolean isInexigibilidade = false;
	
	private boolean enableFormEndereco;
	
	
	@PostConstruct
	public void init() {
		
		facade.inicializarVariaveis(this);
		
	}
	
	public void carregarEndereco(){
		try {
			facade.montarEndereco(this);
			
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	public void buscarEnderecoPorCep() {
		try {
			
			if(!isSemCep()){
				facade.setInexigibilidade(isInexigibilidade);
				facade.buscarEnderecoPorCep(this);
				enableFormEndereco = true;
			} 
			else {
				JsfUtil.addErrorMessage(Util.getString("msg_003", "CEP"));
			}
			
			
			atualizarFormEndereco();
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para atualizar a lista de {@link Logradouro}
	 * 
	 * @author eduardo.fernandes 
	 * @since 03/02/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8338">#8338</a>
	 * @param event
	 */
	public void changeBairro(ValueChangeEvent event) {
		try {

			Bairro bairro = (Bairro) event.getNewValue();

			if(!Util.isNull(bairro)){
				
				// Se o Bairro selecionado é diferente do anterior 
				if(!bairro.equals(getBairro())){
	
					// Se o Bairro selecionado foi cadastrado pelos CORREIOS
					if(bairro.isIndOrigemApi()){
						
						getLogradouro().setIndOrigemApi(true);
						facade.listarLogradouro(this, bairro);
						
					}
					else if(bairro.getIndOrigemCorreio()){
						
						getLogradouro().setIndOrigemCorreio(true);
						facade.listarLogradouro(this, bairro);
						
					}
					else {
						Municipio mClone = null;
						Estado eClone = null;
						// Mantem o Municipio e Estado obtidos através do CEP consultado
						if(!isLocalidadeNaoSelecionado()){
							mClone = getMunicipio().clone();
							if(!isEstadoNaoSelecionado()){
								eClone = getEstado().clone();
							}
						}
						
						// NOVO Bairro para ser cadastro - Salva-se esse Bairro
						if(bairro.isOutroBairro()){
							
							init();
							
							setLogradouro(facade.getOutroLogradouro());
							
							getLogradouro().setIdeTipoLogradouro(null);
							getLogradouro().setNomLogradouro(null);
							
							if(!Util.isNull(mClone)){
								setMunicipio(mClone);
								setEstado(eClone);
							} 
							else {
								setMunicipio(new Municipio());
								setEstado(new Estado());
							}
							
							bairro.setNomBairro(null);
							bairro.setIdeMunicipio(getMunicipio());
							bairro.getIdeMunicipio().setIdeEstado(getEstado());
	
							facade.listarTipoLogradouro(this);
							
						} 
						 
					} 
					
					atualizarBairro();
					atualizarLogradouro();
				}
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método para atualizar o combo de {@link TipoLogradouro}. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 03/02/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8338">#8338</a>
	 * @param event
	 */
	public void changeLogradouro(ValueChangeEvent event){
		try {
			Logradouro logradouro = (Logradouro) event.getNewValue();
			
			// Se o Logradouro selecionado é diferente do anterior 
			if(!Util.isNull(logradouro) && !logradouro.equals(getLogradouro())){

				listaTipoLogradouro = new ArrayList<TipoLogradouro>();
				if(!Util.isNull(logradouro.getIdeTipoLogradouro())){
					
					listaTipoLogradouro.add(logradouro.getIdeTipoLogradouro());
					setTipoLogradouro(logradouro.getIdeTipoLogradouro());
					
				} 
				else if(logradouro.isOutroLogradouro()){

					logradouro.setNomLogradouro(null);
					logradouro.setIdeBairro(getBairro());
					logradouro.setIdeMunicipio(getMunicipio());
					logradouro.getIdeMunicipio().setIdeEstado(getEstado());
					logradouro.setNumCep(getLogradouro().getNumCep());

					facade.listarTipoLogradouro(this);

				}
				
				atualizarLogradouro();
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método para atualizar a lista de {@link Municipio}. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 03/02/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8338">#8338</a>
	 * @param event
	 */
	public void changeEstado(ValueChangeEvent event){
		try {
			Estado estado = (Estado) event.getNewValue();
			
			if(!estado.getIdeEstado().equals(0) && !estado.equals(getEstado())){
			
				facade.listarMunicipio(this, estado);
				
				atualizarMunicipio();
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void changeLocalidade(ValueChangeEvent event){
		try {
			Municipio municipio = (Municipio) event.getNewValue();
			
			if(!municipio.getIdeMunicipio().equals(0) && !municipio.equals(getMunicipio())) {
				
				getBairro().setIdeMunicipio(municipio);
				
			}
				
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean validarEndereco(){
		boolean valido = true;
		if(isSemCep()){
			JsfUtil.addErrorMessage(Util.getString("msg_003", "CEP"));				
			valido = false;
		} 
		else {
			if(isBairroNaoSelecionado()){
				JsfUtil.addErrorMessage(Util.getString("msg_003", "campo Bairro"));				
				valido = false;
			} 
			else {
				if(isOutroBairro() && Util.isNullOuVazio(getBairro().getNomBairro())){
					JsfUtil.addErrorMessage(Util.getString("msg_003", "campo nome do Bairro"));				
					valido = false;
				}
				if(Util.isNull(getLogradouro().getIdeLogradouro()) || isTipoLogradouroNaoSelecionado()){
					JsfUtil.addErrorMessage(Util.getString("msg_003", "campo Logradouro"));				
					valido = false;
				} 
				else if(isOutroLogradouro() && Util.isNullOuVazio(getLogradouro().getNomLogradouro())){
					JsfUtil.addErrorMessage(Util.getString("msg_003", "campo nome do Logradouro"));				
					valido = false;
				}
			}
			if(Util.isNullOuVazio(getEndereco().getNumEndereco())){
				JsfUtil.addErrorMessage(Util.getString("msg_003", "campo Número"));				
				valido = false;
			}
			if(isEstadoNaoSelecionado()){
				JsfUtil.addErrorMessage(Util.getString("msg_003", "campo UF"));				
				valido = false;
			} 
			else if(isLocalidadeNaoSelecionado()){
				JsfUtil.addErrorMessage(Util.getString("msg_003", "campo Localidade"));				
				valido = false;
			}
		}
		return valido;
	}
	
	public void salvarEndereco() {
		try {
			
			if(validarEndereco()){

				facade.finalizarEndereco(this);
				
			}
			
			atualizarFormEndereco();
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void atualizarFormEnderecoPesquisa() {
		Html.atualizar(idForm + ":formEnderecoPesquisaCep:cep");
	}
	
	public void atualizarFormEndereco() {
		Html.atualizar(idForm + ":formEndereco");
	}

	private void atualizarBairro(){
		Html.atualizar(idForm + ":gridbairro");
	}
	
	private void atualizarLogradouro(){
		Html.atualizar(idForm + ":gridlogradouro");
	}
	
	private void atualizarMunicipio(){
		Html.atualizar(idForm + ":idcombomunicipio");
	}
	
	/*
	 * flags
	 */
	public boolean isOutroBairro(){
		return !isBairroNaoSelecionado() && getBairro().isOutroBairro();
	}
	
	public boolean isOutroLogradouro(){
		return !Util.isNull(getLogradouro()) && getLogradouro().isOutroLogradouro();
	}

	protected boolean isEstadoBahia() {
		return getEstado().equals(new Estado(EstadoEnum.BAHIA.getId()));
	}
	
	public boolean isEstadoNaoSelecionado() {
		return Util.isNull(getEstado()) || Util.isNull(getEstado().getIdeEstado()) || getEstado().getIdeEstado().equals(0);
	}

	private boolean isLocalidadeNaoSelecionado() {
		return Util.isNull(getMunicipio()) || Util.isNull(getMunicipio().getIdeMunicipio()) || getMunicipio().getIdeMunicipio().equals(0);
	}

	private boolean isSemCep(){
		return Util.isNullOuVazio(cepConsultado) || (enableFormEndereco = false);
	}
	
	public boolean isLogradouroCorreios(){
		return !isBairroNaoSelecionado() && (getBairro().getIndOrigemCorreio() || getBairro().isIndOrigemApi());
	}
	
	public boolean isDesabilitaGeral(){
		return isSemCep() || isVisualizacao();
	}
	
	public boolean isDesabilitaTipoLogradouro(){
		return !isOutroLogradouro() && (isDesabilitaGeral() || isBairroNaoSelecionado() || isLogradouroCorreios());
	}
	
	public boolean isBairroNaoSelecionado(){
		return Util.isNull(getBairro()) || Util.isNull(getBairro().getIdeBairro());
	}
	
	public boolean isTipoLogradouroNaoSelecionado(){
		return Util.isNull(getTipoLogradouro()) || Util.isNull(getTipoLogradouro().getIdeTipoLogradouro());
	}
	
	public boolean isEdicao(){
		return !Util.isNull(getEndereco()) && !Util.isNull(getEndereco().getIdeEndereco());
	}
	
	public boolean isBloqueioUfLocalidade(){
		return isDesabilitaGeral() || contemUmBairroDoCorreios();
	}
	
	private boolean contemUmBairroDoCorreios(){
		if(!Util.isNullOuVazio(listaBairro)){
			for (Bairro bairro : listaBairro) {
				if(bairro.getIndOrigemCorreio()){
					return true;
				}
				if(bairro.isIndOrigemApi()){
					return true;
				}
			}
		}
		return false;
	}
	
	public Municipio getMunicipioSelecione(){
		return new Municipio(0, "Selecione...");
	}
	
	public Estado getEstadoSelecione(){
		return new Estado(0, "Selecione...");
	}
	
	/*
	 * getters && setters
	 */
	public Logradouro getLogradouro() {
		return endereco.getIdeLogradouro();
	}
	
	public void setLogradouro(Logradouro logradouro) {
		endereco.setIdeLogradouro(logradouro);
	}
	
	public Bairro getBairro() {
		return getLogradouro().getIdeBairro();
	}
	
	public void setBairro(Bairro bairro) {
		getLogradouro().setIdeBairro(bairro);
	}
	
	public Municipio getMunicipio() {
		return getLogradouro().getIdeMunicipio();
	}
	
	public void setMunicipio(Municipio municipio) {
		getLogradouro().setIdeMunicipio(municipio);
	}

	public Estado getEstado() {
		return getMunicipio().getIdeEstado();
	}
	
	public void setEstado(Estado estado) {
		if(Util.isNullOuVazio(getMunicipio())){
			setMunicipio(new Municipio());
		}
		getMunicipio().setIdeEstado(estado);
	}
	
	public TipoLogradouro getTipoLogradouro(){
		return getLogradouro().getIdeTipoLogradouro();
	}
	
	public void setTipoLogradouro(TipoLogradouro tipoLogradouro){
		getLogradouro().setIdeTipoLogradouro(tipoLogradouro);
	}

	public String getCepConsultado() {
		return cepConsultado;
	}

	public void setCepConsultado(String cepConsultado) {
		this.cepConsultado = cepConsultado;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Collection<Logradouro> getListaLogradouro() {
		return listaLogradouro;
	}

	public void setListaLogradouro(Collection<Logradouro> listaLogradouro) {
		this.listaLogradouro = listaLogradouro;
	}

	public Collection<TipoLogradouro> getListaTipoLogradouro() {
		return listaTipoLogradouro;
	}

	public void setListaTipoLogradouro(Collection<TipoLogradouro> listaTipoLogradouro) {
		this.listaTipoLogradouro = listaTipoLogradouro;
	}

	public Collection<Bairro> getListaBairro() {
		return listaBairro;
	}

	public void setListaBairro(Collection<Bairro> listaBairro) {
		this.listaBairro = listaBairro;
	}

	public Collection<Estado> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(Collection<Estado> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public Collection<Municipio> getListaMunicipio() {
		return listaMunicipio;
	}

	public void setListaMunicipio(Collection<Municipio> listaMunicipio) {
		this.listaMunicipio = listaMunicipio;
	}

	public boolean isVisualizacao() {
		return visualizacao;
	}

	public void setVisualizacao(boolean visualizacao) {
		this.visualizacao = visualizacao;
	}

	public MetodoUtil getMetodo() {
		return metodo;
	}

	public void setMetodo(MetodoUtil metodo) {
		this.metodo = metodo;
	}

	public String getIdForm() {
		return idForm;
	}

	public void setIdForm(String idForm) {
		this.idForm = idForm;
	}

	public boolean isBairroOutroInexibilidade() {
		return bairroOutroInexibilidade;
	}

	public void setBairroOutroInexibilidade(boolean bairroOutroInexibilidade) {
		this.bairroOutroInexibilidade = bairroOutroInexibilidade;
	}

	public EnderecoGenericoFacade getFacade() {
		return facade;
	}

	public void setFacade(EnderecoGenericoFacade facade) {
		this.facade = facade;
	}

	public boolean isInexigibilidade() {
		return isInexigibilidade;
	}

	public void setInexigibilidade(boolean isInexigibilidade) {
		this.isInexigibilidade = isInexigibilidade;
	}

}
