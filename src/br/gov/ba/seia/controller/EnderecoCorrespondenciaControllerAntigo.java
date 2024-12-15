package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoEndereco;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.TipoLogradouroService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Named
@ViewScoped
public class EnderecoCorrespondenciaControllerAntigo {
	
	    
	    private Pessoa pessoa;
	// --------- ENDERECO  ------ 
		private Endereco endereco;
		private Logradouro logradouro;
		private Logradouro logradouroPesquisa;
		private Bairro bairro;
		private Municipio municipio;
		private Estado estado;
		private TipoLogradouro tipoLogradouro;
		private Collection<TipoLogradouro> listaTipoLogradouro;
		private List<Logradouro> listaLogradouro;
		private Collection<Bairro> listaBairro;
		private List<Municipio> listaMunicipio;
		private List<Estado> listaEstado;
		private boolean showInputs;
		private boolean showInputLogradouro;
		private boolean enableFormEndereco;
		private boolean salvaEnderecoPessoa;
		
				
		// Utilizados quando escolher no Bairro Op��o Outro
		private Bairro bairroNovo;
		private Logradouro logradouroNovo;
		private TipoLogradouro tipoLogradouroNovo;
		
		
		
		//Condi��es de renderiza��o
		
		private boolean cadastraNovoLogradouro;
		private boolean temPessoa;
		
		
		@EJB
		private EnderecoFacade enderecoService;
		
		@EJB
		private MunicipioService municipioService;
		@EJB TipoLogradouroService tipoLogService;
		
		
		@PostConstruct
		public void init() {
			endereco = new Endereco();
		    logradouro = new Logradouro();
		    logradouroPesquisa = new Logradouro();
		    listaLogradouro = new ArrayList<Logradouro>();
		    tipoLogradouro = new TipoLogradouro();
		    bairro = new Bairro();
		    listaBairro = new ArrayList<Bairro>();
		    municipio = new Municipio();
		    estado = new Estado();
		    enableFormEndereco = false;
		    carregarTipoLogradouros();
		    cadastraNovoLogradouro = true;
		    temPessoa = false;
		    pessoa = null;
		    this.salvaEnderecoPessoa = true;
		    logradouroNovo = new Logradouro();
		}
		
		
		//------- ENDERE�O  ------ // 
	    public String salvarEndereco() {
			try {
				if (bairro.getIdeBairro().equals(-1)) {
					persistirDependencias();
				}
				
				if (logradouro.getIdeLogradouro().equals(-1)) {
					persistirLogradouro();
				}
//				endereco.setIdeEndereco(null);
				endereco.setIdeLogradouro(logradouro);
				enderecoService.salvarEndereco(endereco);
				if(salvaEnderecoPessoa) {
					salvarEnderecoPessoa();
				}
				JsfUtil.addSuccessMessage("Endereço cadastrado com sucesso!");
				enableFormEndereco = Boolean.TRUE;
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
			return "";
			
		}
		
		private void salvarEnderecoPessoa() {
			EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
			enderecoPessoa.setIdePessoa(pessoa);
			enderecoPessoa.setIdeEndereco(endereco);
			enderecoPessoa.setIdeTipoEndereco(new TipoEndereco(TipoEnderecoEnum.RESIDENCIAL.getId()));
			List<EnderecoPessoa> lista = new ArrayList<EnderecoPessoa>();
			lista.add(enderecoPessoa);
			endereco.setEnderecoPessoaCollection(lista);
			try {
				enderecoService.salvarEnderecoPessoa(enderecoPessoa);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		
		
		
		//** M�TODOS ANTIGOS. **//
		
		
		// ap�s consultar os logradouros deve montar os bairros a partir destes logradouros
		public void consultarLogradouros() {
			try {
				listaLogradouro = enderecoService.filtrarLogradouroByCep(logradouroPesquisa);
				carregarBairros();
			} catch (Exception e) {
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		
		//A partir da lista de logradouros criar uma lista de 
		private void carregarBairros(){
			for (Logradouro logradouroObj : listaLogradouro) {
				if (!Util.isObjectInList(logradouroObj.getIdeBairro(), listaBairro)) {
					listaBairro.add(logradouroObj.getIdeBairro());
				}
			}
			
		}
		
		private void carregarTipoLogradouros() {
             try {
				listaTipoLogradouro = tipoLogService.listarTipoLogradouro();
			} catch (Exception e) {
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
			
		}

	
		
		public void filtrarPorCep() {
			try {
				logradouro = new Logradouro();
				montaListaBairros();
				enableFormEndereco = true;
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		
		public void changeLogradouroMunicipio(ValueChangeEvent event) {
			try {
				Bairro bairroSelected = (Bairro)event.getNewValue();
				if (bairroSelected == null) {
					showInputs = true;
					showInputLogradouro = true;
					bairro = new Bairro();
					cadastraNovoLogradouro = true;
				}
				if (!Util.isNull(bairroSelected) && bairroSelected.getIdeBairro()!=0 && bairroSelected.getIdeBairro()!=-1) {
					bairro = enderecoService.filtrarBairroById(bairroSelected);
					listaLogradouro = enderecoService.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
					municipio = bairro.getIdeMunicipio();
					estado = municipio.getIdeEstado();
					cadastraNovoLogradouro = false;
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		
		public void changeLogradouro(ValueChangeEvent event) {
			try {
				Logradouro logradouroSelected = (Logradouro)event.getNewValue();
				if (logradouroSelected.getIdeLogradouro() == -1) {
					showInputs = false;
					showInputLogradouro = true;
					logradouro = new Logradouro();
				}
				
				if (!Util.isNull(logradouroSelected) && logradouroSelected.getIdeLogradouro()!=0 && logradouroSelected.getIdeLogradouro()!=-1) {
					logradouro = enderecoService.filtrarLogradouroById(logradouroSelected);
					tipoLogradouro = logradouro.getIdeTipoLogradouro();
					showInputLogradouro = false;
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		
		public void changeEstado() {
			if(estado !=null && !(estado.getIdeEstado()==null)) {
				try {
					listaMunicipio = (List<Municipio>) municipioService.filtrarListaMunicipiosPorEstado(estado);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			} else {
				listaMunicipio = new ArrayList<Municipio>();
			}
		}
		
		
		private void persistirDependencias() {
			try {
				bairro.setIdeBairro(null);
				bairro.setIdeMunicipio(municipio);
				bairro.setIndOrigemCorreio(false);
				bairro.setIndOrigemApi(false);
				enderecoService.salvarBairro(bairro);
				persistirLogradouro();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		
		
		private void persistirLogradouro() {
			try {
				logradouro.setIdeLogradouro(null);
				logradouro.setIdeMunicipio(municipio);
				logradouro.setIndOrigemCorreio(false);
				logradouro.setIndOrigemApi(false);
				logradouro.setIdeBairro(bairro);
				logradouro.setIdeTipoLogradouro(tipoLogradouro);
				logradouro.setNumCep(logradouroPesquisa.getNumCep());
				enderecoService.salvarLogradouro(logradouro);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		
		private void montaListaBairros() {
			try {
				listaBairro = new ArrayList<Bairro>();
				listaBairro = enderecoService.filtrarBairroByCep(logradouroPesquisa);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}	
		
		public Collection<SelectItem> getValuesComboBoxMunicipio(){
			 changeEstado();
			 Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		     Iterator<Municipio> i = listaMunicipio.iterator();
		     while (i.hasNext()) {
		    	 Municipio municipio =i.next();
		       	toReturn.add(new SelectItem(municipio, municipio.getNomMunicipio()));  
		     }
		     return toReturn;
	    }
		
		
		//--- GET'S AND SET'S


		public Endereco getEndereco() {
			return endereco;
		}


		public void setEndereco(Endereco endereco) {
			this.endereco = endereco;
		}


		public Logradouro getLogradouro() {
			return logradouro;
		}


		public void setLogradouro(Logradouro logradouro) {
			this.logradouro = logradouro;
		}


		public Logradouro getLogradouroPesquisa() {
			return logradouroPesquisa;
		}


		public void setLogradouroPesquisa(Logradouro logradouroPesquisa) {
			this.logradouroPesquisa = logradouroPesquisa;
		}


		public Bairro getBairro() {
			return bairro;
		}


		public void setBairro(Bairro bairro) {
			this.bairro = bairro;
		}


		public Municipio getMunicipio() {
			return municipio;
		}


		public void setMunicipio(Municipio municipio) {
			this.municipio = municipio;
		}


		public Estado getEstado() {
			return estado;
		}


		public void setEstado(Estado estado) {
			this.estado = estado;
		}


		public TipoLogradouro getTipoLogradouro() {
			return tipoLogradouro;
		}


		public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
			this.tipoLogradouro = tipoLogradouro;
		}


		public List<Logradouro> getListaLogradouro() {
			return listaLogradouro;
		}


		public void setListaLogradouro(List<Logradouro> listaLogradouro) {
			this.listaLogradouro = listaLogradouro;
		}


		public Collection<Bairro> getListaBairro() {
			return listaBairro;
		}


		public void setListaBairro(Collection<Bairro> listaBairro) {
			this.listaBairro = listaBairro;
		}


		public List<Municipio> getListaMunicipio() {
			return listaMunicipio;
		}


		public void setListaMunicipio(List<Municipio> listaMunicipio) {
			this.listaMunicipio = listaMunicipio;
		}


		public List<Estado> getListaEstado() {
			return listaEstado;
		}


		public void setListaEstado(List<Estado> listaEstado) {
			this.listaEstado = listaEstado;
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


		public boolean isEnableFormEndereco() {
			return enableFormEndereco;
		}


		public void setEnableFormEndereco(boolean enableFormEndereco) {
			this.enableFormEndereco = enableFormEndereco;
		}


		public EnderecoFacade getEnderecoService() {
			return enderecoService;
		}


		public void setEnderecoService(EnderecoFacade enderecoService) {
			this.enderecoService = enderecoService;
		}


		public Pessoa getPessoa() {
			return pessoa;
		}


		public void setPessoa(Pessoa pessoa) {
			this.pessoa = pessoa;			
		}


		public Collection<TipoLogradouro> getListaTipoLogradouro() {
			return listaTipoLogradouro;
		}


		public void setListaTipoLogradouro(Collection<TipoLogradouro> listaTipoLogradouro) {
			this.listaTipoLogradouro = listaTipoLogradouro;
		}


	
		public Bairro getBairroNovo() {
			return bairroNovo;
		}


		public void setBairroNovo(Bairro bairroNovo) {
			this.bairroNovo = bairroNovo;
		}


		public Logradouro getLogradouroNovo() {
			return logradouroNovo;
		}


		public void setLogradouroNovo(Logradouro logradouroNovo) {
			this.logradouroNovo = logradouroNovo;
		}


		public TipoLogradouro getTipoLogradouroNovo() {
			return tipoLogradouroNovo;
		}


		public void setTipoLogradouroNovo(TipoLogradouro tipoLogradouroNovo) {
			this.tipoLogradouroNovo = tipoLogradouroNovo;
		}


		public boolean isCadastraNovoLogradouro() {
			return cadastraNovoLogradouro;
		}


		public void setCadastraNovoLogradouro(boolean cadastraNovoLogradouro) {
			this.cadastraNovoLogradouro = cadastraNovoLogradouro;
		}


		public boolean isTemPessoa() {
			if( Util.isNullOuVazio(this.pessoa) || Util.isNullOuVazio(this.pessoa.getIdePessoa()) ) {
				temPessoa = false;
			}
			else {
				temPessoa = true;
			}
			return temPessoa;
		}


		public void setTemPessoa(boolean temPessoa) {
			this.temPessoa = temPessoa;
		}


		public boolean isSalvaEnderecoPessoa() {
			return salvaEnderecoPessoa;
		}


		public void setSalvaEnderecoPessoa(boolean salvaEnderecoPessoa) {
			this.salvaEnderecoPessoa = salvaEnderecoPessoa;
		}
		
		
        		
    		

}
