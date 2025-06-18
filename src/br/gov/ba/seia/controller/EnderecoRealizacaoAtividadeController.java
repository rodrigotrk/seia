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

import br.gov.ba.seia.controller.abstracts.TemEndereco;
import br.gov.ba.seia.dto.EnderecoDTO;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.enumerator.EstadoEnum;
import br.gov.ba.seia.enumerator.MunicipioEnum;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.EstadoService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ServicoDeCepService;
import br.gov.ba.seia.service.TipoLogradouroService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("enderecoRealizacaoAtividadeController")
@ViewScoped
public class EnderecoRealizacaoAtividadeController extends TemEndereco {

	private Bairro bairro;
	private Bairro bairroOutro;
	private Endereco endereco;
	private Estado estado;
	private Logradouro logradouro;
	private Logradouro logradouroPesquisa;
	private Municipio municipio;
	private TipoLogradouro tipoLogradouro;
	private EnderecoDTO enderecoApi;
	
	
	private List<Logradouro> listaLogradouro;
	private List<Bairro> listaBairro;
	private List<Municipio> listaMunicipio;
	private List<Estado> listaEstado;
	
	private Boolean showUfCidade = Boolean.FALSE;
	private Boolean showFdbairro = Boolean.FALSE;
	private Boolean isMunicipioSalvador;	
	private Boolean isEstadoBahia;
	private boolean escolheuBairro = false;
	private boolean showInputs;
	private boolean showInputLogradouro;
	private boolean enableFormEndereco;
	private boolean enableTipoLogradouro = true;
	private boolean permiteEditar;

	private boolean cepModificado;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private BairroService bairroService;
	
	@EJB
	private ServicoDeCepService cepService;
	
	@EJB
	private EstadoService estadoService;
	
	@EJB
	private LogradouroService logradouroService;
	
	@EJB
	private TipoLogradouroService tipoLogradouroService;

	private Collection<SelectItem> listaBairroSelectItem;
	private Collection<SelectItem> listaLogradouroSelectItem;
	
	@PostConstruct
	public void init() {
		if (Util.isNull(endereco)) {
			endereco = new Endereco();
			logradouro = new Logradouro();
			logradouroPesquisa = new Logradouro();
			listaLogradouro = new ArrayList<Logradouro>();
			tipoLogradouro = new TipoLogradouro();
			bairro = new Bairro();
			bairroOutro = new Bairro();
			listaBairro = new ArrayList<Bairro>();
			municipio = new Municipio();
			estado = new Estado();
			
			enderecoGenericoController.setCepConsultado(null);
			enderecoGenericoController.setBairro(new Bairro());
			enderecoGenericoController.setLogradouro(new Logradouro());
			enderecoGenericoController.setTipoLogradouro(new TipoLogradouro());
			enderecoGenericoController.setEstado(new Estado());
			enderecoGenericoController.setMunicipio(new Municipio());
			enderecoGenericoController.setInexigibilidade(true);
			
			enviarId();
			obterEstadoBA();
			
//			declaracaoInexigibilidadeInfoGeral = new DeclaracaoInexigibilidadeInfoGeral();
			
/*			montarListaBairro();
			
			montarListaLogradouro();*/
			
			enableFormEndereco = true;
			
			this.permiteEditar = true;
		} else {
			carregaEndereco();
			this.permiteEditar = false;
			
		}
		showFdbairro = Boolean.FALSE;
		cepModificado = false;
	}
	
	private void montarListaBairro() {
		listaBairroSelectItem = new ArrayList<SelectItem>();
		listaBairroSelectItem.add(new SelectItem(null, "Selecione..."));
		
		if(!Util.isNullOuVazio(this.listaBairro)) {
			for (Bairro bairro : listaBairro) {
				listaBairroSelectItem.add(new SelectItem(bairro, bairro.getNomBairro()));
			}
		}
		
		listaBairroSelectItem.add(new SelectItem(new Bairro(-1), "Outro"));
	}
	
	private void montarListaLogradouro() {
		listaLogradouroSelectItem = new ArrayList<SelectItem>();
		
		listaLogradouroSelectItem.add(new SelectItem(null, "Selecione..."));
		
		if(!Util.isNullOuVazio(this.listaLogradouro)) {
			for (Logradouro logradouro : listaLogradouro) {
				listaLogradouroSelectItem.add(new SelectItem(logradouro, logradouro.getNomLogradouro()));
			}
		}
		
		this.listaLogradouroSelectItem.add(new SelectItem(new Logradouro(-1), "Outro"));
	}

	private void obterEstadoBA() {
		try {
			estado = estadoService.buscar(new Estado(EstadoEnum.BAHIA.getId()));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void carregaEndereco() {
		this.showInputs = false;
		this.showInputLogradouro = false;
		this.enableTipoLogradouro = true;
		
		logradouroPesquisa = endereco.getIdeLogradouro();
		montaListaBairros();
		montarListaBairro();
		
		logradouro = endereco.getIdeLogradouro();
		if (!Util.isNullOuVazio(logradouro) && !Util.isNullOuVazio(logradouro.getIdeBairro()))
			bairro = logradouro.getIdeBairro();
		try {
			listaLogradouro = enderecoFacade.filtrarLogradouroByBairroSemIndCorreio(bairro, logradouroPesquisa.getNumCep());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		if (!Util.isNullOuVazio(logradouro) && !Util.isNullOuVazio(logradouro.getIdeBairro())) {
			tipoLogradouro = logradouro.getIdeTipoLogradouro();
			municipio = logradouro.getIdeMunicipio();
		}
		
		if (!Util.isNullOuVazio(municipio) && !Util.isNullOuVazio(municipio.getIdeEstado())) {
			estado = municipio.getIdeEstado();
		}
		
		montarListaLogradouro();
		enableFormEndereco = true;
	}

	public void limparEndereco() {
		endereco = new Endereco();
		logradouro = new Logradouro();
		logradouroPesquisa = new Logradouro();
		listaLogradouro = new ArrayList<Logradouro>();
		tipoLogradouro = new TipoLogradouro();
		bairro = new Bairro();
		bairroOutro = new Bairro();
		listaBairro = new ArrayList<Bairro>();
		municipio = new Municipio();
		estado = new Estado();
		obterEstadoBA();
		
		
		enableFormEndereco = true;
		showInputs = false;
		showInputLogradouro = false;
		this.enableTipoLogradouro = true;
		this.permiteEditar = true;
		this.cepModificado = false;
		super.enderecoGenericoController.setBairroOutroInexibilidade(false);
		super.enderecoGenericoController.setCepConsultado(null);
		super.enderecoGenericoController.setBairro(null);
		super.enderecoGenericoController.setLogradouro(new Logradouro());
		super.enderecoGenericoController.setTipoLogradouro(new TipoLogradouro());
		super.enderecoGenericoController.setEstado(null);
		super.enderecoGenericoController.setMunicipio(new Municipio(0));
		super.enderecoGenericoController.setListaBairro(new ArrayList<Bairro>());
		super.enderecoGenericoController.setListaEstado(new ArrayList<Estado>());
		super.enderecoGenericoController.setListaLogradouro(new ArrayList<Logradouro>());
		super.enderecoGenericoController.setListaTipoLogradouro(new ArrayList<TipoLogradouro>());
		
	}

	public boolean podeSalvar(){

		boolean permiteSalvar = true;
		
		if(Util.isNullOuVazio(logradouroPesquisa.getNumCep())){
			 permiteSalvar = false;
		
			 return permiteSalvar;
		}
		
		if(Util.isNullOuVazio(bairro)){
			 permiteSalvar = false;
			 return permiteSalvar;
		}

		if((bairro.getIdeBairro()== -1)  && 
		   ((bairro.getNomBairro().equals(""))||(Util.isNullOuVazio(bairro.getNomBairro())))){
			 permiteSalvar = false;
			 return permiteSalvar;
		}
			
		if(Util.isNullOuVazio(tipoLogradouro)){
			 permiteSalvar = false;
			 return permiteSalvar;
		}
		
		if(Util.isNullOuVazio(tipoLogradouro.getIdeTipoLogradouro())){
			 permiteSalvar = false;
			 return permiteSalvar;
		}
		
		if(Util.isNullOuVazio(tipoLogradouro.getNomTipoLogradouro())){
			 permiteSalvar = false;
			 return permiteSalvar;
		}
		
		if(Util.isNullOuVazio(endereco.getDesComplemento())){
			 permiteSalvar = false;
			 return permiteSalvar;
		}
		
		if(Util.isNullOuVazio(estado)){
			 permiteSalvar = false;
			 return permiteSalvar;
		}
		
		if(Util.isNullOuVazio(municipio)){
			 permiteSalvar = false;
			 return permiteSalvar;
		}
		
		return true;
	}
	
	public String showMessagem(){
		
		return "";
	}
	
	public void filtrarPorCep(){
		try {
			if (cepModificado && !Util.isNullOuVazio(enderecoGenericoController.getCepConsultado())) {
//				logradouroPesquisa.setIndOrigemCorreio(true);
				logradouroPesquisa.setNumCep(enderecoGenericoController.getFacade().retornarCEP(enderecoApi.getCep()));

			    Collection<TipoLogradouro> tiposLogradouro = tipoLogradouroService.listarTipoLogradouro();
			    
			    montaListaBairros();
				
				if(Util.isNull(isEstadoBahia) || (!Util.isNull(isEstadoBahia) && !isEstadoBahia)) {
					JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
					logradouroPesquisa = new Logradouro();
					return;
				}
				
				logradouro = new Logradouro(0);
				enableFormEndereco = true;
				showInputs = false;
				escolheuBairro = false;
				showInputLogradouro = false;
				//bairro = new Bairro(0);	
				bairroOutro = new Bairro();
				isMunicipioSalvador = null;
				this.enableTipoLogradouro = true;
				
				montarListaBairro();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void changeLogradouroMunicipio(ValueChangeEvent event) {
		try {
			
			enderecoGenericoController.changeBairro(event);
			
			carregarDados();
			Bairro bairroSelected = (Bairro) event.getNewValue();
			if(bairroSelected.getIdeBairro() == null) {
				showInputLogradouro =false;
				escolheuBairro = false;
				showUfCidade = Boolean.TRUE;
				this.enableTipoLogradouro = false;
				enderecoGenericoController.setBairroOutroInexibilidade(false);
			}else
			if (bairroSelected.getIdeBairro() == -1) {
				showInputLogradouro =true;
				escolheuBairro = true;
				showFdbairro = true;
				showInputs = false;
				showUfCidade = Boolean.TRUE;
				this.enableTipoLogradouro = false;
				logradouro = new Logradouro(-1);
				bairro = null;
				enderecoGenericoController.setBairro(null);
				enderecoGenericoController.setBairroOutroInexibilidade(true);
				
			}else{
				enderecoGenericoController.setBairroOutroInexibilidade(false);
			}
			if (!Util.isNull(bairroSelected) && bairroSelected.getIdeBairro() > 0) {
				bairro = enderecoFacade.filtrarBairroById(bairroSelected);
				enderecoGenericoController.setBairro(bairroSelected);
				listaLogradouro = enderecoFacade.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				
				if(!Util.isNullOuVazio(listaLogradouro) && !listaLogradouro.isEmpty()){
					showInputs = false;
					showInputLogradouro = false;
					logradouro = new Logradouro();
					logradouro.setIdeLogradouro(-1);
				}
				
				municipio = bairro.getIdeMunicipio();
				isMunicipioSalvador = municipio.getIdeMunicipio().equals(MunicipioEnum.SALVADOR.getId());
				estado = municipio.getIdeEstado();
				isEstadoBahia = estado.getIdeEstado().equals(EstadoEnum.BAHIA.getId());
				
				if(estado != null && !isEstadoBahia) {
					JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
					estado = new Estado(0);
					municipio = new Municipio(0);
					bairro = new Bairro();
					enderecoGenericoController.setBairro(new Bairro());
					bairroOutro = new Bairro();
					escolheuBairro = false;
					showUfCidade = Boolean.FALSE;
					showInputs = Boolean.FALSE;
				}else{
					escolheuBairro = true;
					showUfCidade = Boolean.FALSE;
					showFdbairro = false;
				}
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void changeLogradouro(ValueChangeEvent event) {
		try {
			
			enderecoGenericoController.changeLogradouro(event);
			
			 Logradouro logradouroSelected = (Logradouro) event.getNewValue();
			if (logradouroSelected.getIdeLogradouro() == null || logradouroSelected.getIdeLogradouro() == -1) {
				showInputs = false;
				showInputLogradouro = true;
				logradouro.setIdeLogradouro(-1);
				this.enableTipoLogradouro = false; 
			}
			if (!Util.isNull(logradouroSelected) && logradouroSelected.getIdeLogradouro() != 0 && logradouroSelected.getIdeLogradouro() != -1) {
				logradouro = enderecoFacade.filtrarLogradouroById(logradouroSelected);
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				showInputLogradouro = false;
				this.enableTipoLogradouro = true;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	public void changeEstado() {
		if (estado != null && !(estado.getIdeEstado() == null)) {
			try {
				enderecoApi.equals(enderecoApi.getMunicipio());
				listaMunicipio =  municipioService.filtrarListaMunicipiosDaBahia();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		} else {
			listaMunicipio = new ArrayList<Municipio>();
		}
	}
	
	public void carregarDados() throws CloneNotSupportedException{
		if(!Util.isNull(enderecoGenericoController.getBairro())){
			bairro = (Bairro) enderecoGenericoController.getBairro().clone();
		}
		 logradouroPesquisa.setNumCep(Integer.parseInt(enderecoGenericoController.getCepConsultado().replace(".", "").replace("-", "")));
		 if(!Util.isNullOuVazio(enderecoGenericoController.getEndereco())){
			 enderecoGenericoController.getEndereco().setNumEndereco(endereco.getNumEndereco());
			 enderecoGenericoController.getEndereco().setDesComplemento(endereco.getDesComplemento());
			 endereco = enderecoGenericoController.getEndereco();         
		 }
		 if(!Util.isNullOuVazio(enderecoGenericoController.getTipoLogradouro())){
			 tipoLogradouro = enderecoGenericoController.getTipoLogradouro(); 
		 }
		 logradouro = (Logradouro) enderecoGenericoController.getEndereco().getIdeLogradouro().clone();
		 if(!Util.isNullOuVazio(enderecoGenericoController.getMunicipio())){
			 municipio = enderecoGenericoController.getMunicipio();
			 estado = enderecoGenericoController.getEstado();  
		 }
	}	

	private void montaListaBairros() {
		try {
			String numEndereco= "";
			String descEndereco="";
			Bairro bairroClone= null;
			
			listaBairro = new ArrayList<Bairro>();
			
			listaBairro = bairroService.listarBairroByLogradouro(logradouroPesquisa);
			
			if(!Util.isNullOuVazio(endereco)){
				if(!Util.isNullOuVazio(endereco.getIdeLogradouro().getNumCep().toString())){
					if(Util.isNullOuVazio(enderecoGenericoController.getCepConsultado())){
						enderecoGenericoController.setCepConsultado(endereco.getIdeLogradouro().getNumCep().toString());
					}
					numEndereco = endereco.getNumEndereco();
					descEndereco = endereco.getDesComplemento();
				}
			}
			
			enderecoGenericoController.buscarEnderecoPorCep();
			if(!Util.isNullOuVazio(endereco.getIdeLogradouro())){
				enderecoGenericoController.setLogradouro(endereco.getIdeLogradouro());
				if(!enderecoGenericoController.getListaLogradouro().contains(endereco.getIdeLogradouro())) {
					enderecoGenericoController.getListaLogradouro().add(endereco.getIdeLogradouro());
				}
				if(!Util.isNullOuVazio(endereco.getIdeLogradouro().getIdeBairro())){
					bairroClone = endereco.getIdeLogradouro().getIdeBairro();
					enderecoGenericoController.setBairro(bairroClone);
					if(!listaBairro.contains(bairroClone)) {
						listaBairro.add(bairroClone);
					}
					enderecoGenericoController.setBairroOutroInexibilidade(false);
					showInputLogradouro=false;
				}
			}
			listaBairro.add(enderecoGenericoController.getFacade().getOutroBairro());
			endereco.setDesComplemento(descEndereco);
			endereco.setNumEndereco(numEndereco);
			
			carregarDados();
			
			if(!Util.isNullOuVazio(bairroClone)){
				enderecoGenericoController.setBairro(bairroClone);
				if(listaBairro.contains(bairroClone)){
					enderecoGenericoController.setBairroOutroInexibilidade(false);
				}else{
					enderecoGenericoController.setBairroOutroInexibilidade(true);
				}
			}
			
			enderecoGenericoController.setListaBairro(listaBairro);
			if (!enderecoGenericoController.getListaBairro().isEmpty() && !Util.isNullOuVazio(((List<Bairro>) enderecoGenericoController.getListaBairro()).get(0))) {
				
				
				if(!Util.isNullOuVazio(enderecoGenericoController.getBairro()) && !Util.isNullOuVazio(enderecoGenericoController.getBairro().getIdeMunicipio())){
					isEstadoBahia = enderecoGenericoController.getEstado().getId().equals(EstadoEnum.BAHIA.getId());
				}else{			
					logradouro.setNumCep(enderecoGenericoController.getFacade().retornarCEP(endereco.getIdeLogradouro().getNumCepString())); //enderecoApi.getCep() -- Retirado ticket 
					listaLogradouro = enderecoFacade.filtrarLogradouroByCepSemIndCorreio(logradouro);
					isEstadoBahia = listaLogradouro.get(0).getMunicipio().getIdeEstado().getId().equals(EstadoEnum.BAHIA.getId());
					
					if(Util.isNullOuVazio(enderecoGenericoController.getListaMunicipio())){
						enderecoGenericoController.setListaMunicipio(new ArrayList<Municipio>());
						for(Logradouro log : listaLogradouro){
							enderecoGenericoController.getListaMunicipio().add(log.getIdeMunicipio());
						}
					}
					enderecoGenericoController.getFacade().listarMunicipio(enderecoGenericoController, listaLogradouro.get(0).getMunicipio().getIdeEstado());
					enderecoGenericoController.getLogradouro().setIdeMunicipio(listaLogradouro.get(0).getIdeMunicipio());
				}
				this.municipio = enderecoGenericoController.getMunicipio();	
				showUfCidade = Boolean.FALSE;
			} else {
				isEstadoBahia = null;
				showUfCidade = Boolean.TRUE;
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<SelectItem> getValuesComboBoxMunicipio() {
		changeEstado();
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		Iterator<Municipio> i = listaMunicipio.iterator();
		toReturn.add(new SelectItem(null, "Selecione..."));
		while (i.hasNext()) {
			Municipio municipio = i.next();
			toReturn.add(new SelectItem(municipio, municipio.getNomMunicipio()));
		}
		return toReturn;
	}

	public Collection<SelectItem> getValuesComboLogradouro() {
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		Iterator<Logradouro> i = listaLogradouro.iterator();
		toReturn.add(new SelectItem(null, "Selecione..."));
		while (i.hasNext()) {
			Logradouro logradouro = i.next();
			toReturn.add(new SelectItem(logradouro, logradouro.getNomLogradouro()));
		}
		toReturn.add(new SelectItem(new Logradouro(-1), "Outro"));
		return toReturn;
	}

	public Collection<SelectItem> getValuesComboBairro() {
		
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		Iterator<Bairro> i = listaBairro.iterator();
		toReturn.add(new SelectItem(null, "Selecione..."));
		while (i.hasNext()) {
			Bairro bairro = i.next();
			toReturn.add(new SelectItem(bairro, bairro.getNomBairro()));
		}
		toReturn.add(new SelectItem(new Bairro(-1), "Outro"));
		return toReturn;
	}

	public void visualizarEndereco(Endereco declaracaoEndereco) {
		if(!Util.isNull(declaracaoEndereco)) {
			
			try {

				this.endereco = enderecoFacade.carregar(declaracaoEndereco.getIdeEndereco());
				enderecoGenericoController.setLogradouro(endereco.getIdeLogradouro());				
				carregaEndereco();
				
				if(!enderecoGenericoController.getBairro().getIndOrigemCorreio() && !enderecoGenericoController.getBairro().isIndOrigemApi()){
					enderecoGenericoController.setBairroOutroInexibilidade(true);
				}else{
					enderecoGenericoController.setBairroOutroInexibilidade(false);
				}
				
				this.enableFormEndereco = false;
				
				this.permiteEditar = false;
				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage("Não foi possível carregar os dados do Local de Realização.");
			}
			
		}
	}
	
	public void editarEndereco(Endereco declaracaoEndereco) {
		if(!Util.isNull(declaracaoEndereco)) {
			limparEndereco();
			
			try {
				this.endereco = enderecoFacade.carregar(declaracaoEndereco.getIdeEndereco());
				
				enderecoGenericoController.setLogradouro(endereco.getIdeLogradouro());
				carregaEndereco();
				if(!enderecoGenericoController.getListaBairro().contains(enderecoGenericoController.getBairro())){
					enderecoGenericoController.setBairroOutroInexibilidade(true);
				}else{
					enderecoGenericoController.setBairroOutroInexibilidade(false);
				}
			
				permiteEditar = true;
			
				this.enableFormEndereco = true;
				
				if(!endereco.getIdeLogradouro().getIdeBairro().getIndOrigemCorreio() && !endereco.getIdeLogradouro().getIdeBairro().isIndOrigemApi()){
					showInputs = true;
				}
				
				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage("Não foi possível carregar os dados do Local de Realização.");
			}
			
		}
	}
	
	public void carregarDadosEndereco() {

		enderecoGenericoController.setBairro(bairro);
		enderecoGenericoController.setEndereco(endereco);
		enderecoGenericoController.setEstado(estado);
		enderecoGenericoController.setLogradouro(logradouro);
		enderecoGenericoController.setMunicipio(municipio);
		enderecoGenericoController.setTipoLogradouro(tipoLogradouro);
	}
	
	public void carregarMunicipio() {
		try {
			
			if(bairro == null) {
				bairro = new Bairro();
				enderecoGenericoController.setBairro(null);
				bairroOutro = new Bairro();
				escolheuBairro = false;
				showFdbairro = false;
				showInputs = Boolean.FALSE;
				showUfCidade = Boolean.FALSE;
			}
			
			if(bairro != null && bairro.getIdeBairro() != null) {
				if (bairro.getIdeBairro() == -1) {
					escolheuBairro = true;
					showFdbairro = true;
					showInputs = false;
					showInputLogradouro = true;
					showUfCidade = Boolean.TRUE;
					estado = new Estado(EstadoEnum.BAHIA.getId());
					this.enableTipoLogradouro = false;
					logradouro = new Logradouro(-1);
					
				} else if (bairro.getIdeBairro() != 0 && bairro.getIdeBairro() != -1) {
					this.enableTipoLogradouro = true;
					bairro = enderecoFacade.filtrarBairroById(bairro);
					enderecoGenericoController.setBairro(bairro);
					bairroOutro = null;
					listaLogradouro = enderecoFacade.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
					
					montarListaLogradouro();
					
					if(!Util.isNullOuVazio(listaLogradouro)){
						showInputs = false;
						showInputLogradouro = false;
						logradouro = new Logradouro();
					}
					
					municipio = bairro.getIdeMunicipio();
					isMunicipioSalvador = municipio.getIdeMunicipio().equals(MunicipioEnum.SALVADOR.getId());
					estado = municipio.getIdeEstado();
					isEstadoBahia = estado.getIdeEstado().equals(EstadoEnum.BAHIA.getId());
					
					if(estado != null && !isEstadoBahia) {
						JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
						estado = new Estado(0);
						municipio = new Municipio(0);
						bairro = new Bairro();
						enderecoGenericoController.setBairro(null);
						bairroOutro = new Bairro();
						escolheuBairro = false;
						showUfCidade = Boolean.FALSE;
						showInputs = Boolean.FALSE;
					}else{
						escolheuBairro = true;
						showUfCidade = Boolean.FALSE;
						showFdbairro = false;
					}
				}
			}
			tipoLogradouro = null;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void changeCep(ValueChangeEvent e) throws Exception {
		try {

			if (!Util.isNullOuVazio(e.getNewValue())) {
				cepService.carregar(e.getNewValue().toString().replaceAll("[^0-9]", ""));
				cepService.montarEstrutura();
				enderecoApi = cepService.getEnderecoApi();  
				Bairro bairroApi = cepService.getBairroEnt();
				
				/*Condição para incluir bairro, de acordo com os correios, caso ele não exista no banco*/
				 if(Util.isNullOuVazio(bairroApi)) {
					 bairroService.adicionarBairroApi(enderecoApi.getBairro(), enderecoApi.getIdeMunicipio());
					 bairroApi = bairroService
								.buscarBairroByNome(enderecoApi.getBairro(), enderecoApi.getIdeMunicipio());
				 }
				if(bairroApi != null) {
				enderecoApi.setIdeBairro(bairroApi.getIdeBairro());
				}
				endereco.setIdeLogradouro(cepService.getLogradouroEnt());
			
				
				String oldValue = "";
				if (!Util.isNull(e.getOldValue())) {
					oldValue = e.getOldValue().toString();
				}
				if (e != null && !((oldValue).equals(enderecoApi.toString()))) {
					cepModificado = true;
				}
				if (Util.isNullOuVazio(enderecoApi)) {
					JsfUtil.addErrorMessage(
							"CEP não encontrado. Caso não saiba o CEP correto, utilizar o geral do Município.");
					limparEndereco();
				}
			}else {
				limparEndereco();
			}

		} catch (Exception ex) {
			System.out.println(ex);
			JsfUtil.addErrorMessage("CEP inválido ou não encontrado na base dos correios. ");
		}
	}
		
		
		/*if(!Util.isNullOuVazio(e.getNewValue())) {
			Integer cepSemMascara = enderecoGenericoController.getFacade().retornarCEP(e.getNewValue().toString());
			String oldValue = "";
			if(!Util.isNull(e.getOldValue())){
				oldValue = e.getOldValue().toString();
			}
			
			if(e != null && !((oldValue).equals(cepSemMascara.toString()))){
				cepModificado = true;
			}
		}*/
	
	
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

	public List<Bairro> getListaBairro() {
		return listaBairro;
	}

	public void setListaBairro(List<Bairro> listaBairro) {
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

	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public boolean getDesabilitarLogradouro() {
		return !(enableFormEndereco && escolheuBairro);
	}

	public Boolean getShowUfCidade() {
		return showUfCidade;
	}

	public void setShowUfCidade(Boolean showUfCidade) {
		this.showUfCidade = showUfCidade;
	}

	public Boolean getShowFdbairro() {
		return showFdbairro;
	}

	public void setShowFdbairro(Boolean showFdbairro) {
		this.showFdbairro = showFdbairro;
	}

	public Boolean getIsMunicipioSalvador() {
		return isMunicipioSalvador;
	}

	public void setIsMunicipioSalvador(Boolean isMunicipioSalvador) {
		this.isMunicipioSalvador = isMunicipioSalvador;
	}

	public Boolean getIsEstadoBahia() {
		return isEstadoBahia;
	}

	public void setIsEstadoBahia(Boolean isEstadoBahia) {
		this.isEstadoBahia = isEstadoBahia;
	}

	public boolean isPermiteEditar() {
		return permiteEditar;
	}

	public void setPermiteEditar(boolean permiteEditar) {
		this.permiteEditar = permiteEditar;
	}

	public boolean isEnableTipoLogradouro() {
		return enableTipoLogradouro;
	}

	public void setEnableTipoLogradouro(boolean enableTipoLogradouro) {
		this.enableTipoLogradouro = enableTipoLogradouro;
	}
	
	public Collection<SelectItem> getListaBairroSelectItem() {
		return listaBairroSelectItem;
	}

	public void setListaBairroSelectItem(
			Collection<SelectItem> listaBairroSelectItem) {
		this.listaBairroSelectItem = listaBairroSelectItem;
	}

	public Collection<SelectItem> getListaLogradouroSelectItem() {
		return listaLogradouroSelectItem;
	}

	public void setListaLogradouroSelectItem(
			Collection<SelectItem> listaLogradouroSelectItem) {
		this.listaLogradouroSelectItem = listaLogradouroSelectItem;
	}

	public Bairro getBairroOutro() {
		return bairroOutro;
	}

	public void setBairroOutro(Bairro bairroOutro) {
		this.bairroOutro = bairroOutro;
	}

	@Override
	public void salvarEnderecoPessoaEndereco() throws Exception {
		
		
	}

	@Override
	public void enviarId() {
		super.enviarId("formAtividadeEndereco");
	}

	@Override
	public void desabilitarParaVisualizacao() {
		super.enderecoGenericoController.setVisualizacao(false);
		
	}

	@Override
	public void prepararEnderecoGenericoController() {
		
		
	}
}
