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
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoProjeto;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.enumerator.EstadoEnum;
import br.gov.ba.seia.enumerator.MunicipioEnum;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.facade.EnderecoGenericoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.EstadoService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("projetoEnderecoController")
@ViewScoped
public class ProjetoEnderecoController extends TemEndereco {

	private Bairro bairro;
	private Endereco endereco;
	private Estado estado;
	private Logradouro logradouro;
	private Logradouro logradouroPesquisa;
	private Municipio municipio;
	private TipoLogradouro tipoLogradouro;

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
	
	private DeclaracaoInexigibilidadeInfoProjeto declaracaoInfoProjeto;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	@EJB
	private EnderecoGenericoFacade facade;
		
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private BairroService bairroService;
	
	@EJB
	private EstadoService estadoService;
	
	@PostConstruct
	public void init() {
		if (Util.isNull(endereco)) {
			endereco = new Endereco();
			logradouro = new Logradouro();
			logradouroPesquisa = new Logradouro();
			listaLogradouro = new ArrayList<Logradouro>();
			tipoLogradouro = new TipoLogradouro();
			bairro = new Bairro();
			listaBairro = new ArrayList<Bairro>();
			municipio = new Municipio();
			estado = new Estado();
			
			enderecoGenericoController.setCepConsultado(null);
			enderecoGenericoController.setBairro(new Bairro());
			enderecoGenericoController.setLogradouro(new Logradouro());
			enderecoGenericoController.setTipoLogradouro(new TipoLogradouro());
			enderecoGenericoController.setEstado(new Estado());
			enderecoGenericoController.setMunicipio(new Municipio());
			
			
			enviarId();
			obterEstadoBA();
			
			declaracaoInfoProjeto = new DeclaracaoInexigibilidadeInfoProjeto();
			
			enableFormEndereco = true;
			
			this.permiteEditar = true;
		} else {
			carregaEndereco();
			this.permiteEditar = false;
		}
		showFdbairro = Boolean.FALSE;
		cepModificado = false;
	}

	private void obterEstadoBA() {
		try {
			estado = estadoService.buscar(new Estado(EstadoEnum.BAHIA.getId()));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void carregaEndereco() {
		try {
			logradouroPesquisa = endereco.getIdeLogradouro();
			montaListaBairros();
			logradouro = endereco.getIdeLogradouro();
			if (!Util.isNullOuVazio(logradouro) && !Util.isNullOuVazio(logradouro.getIdeBairro()))
				bairro = logradouro.getIdeBairro();
			
				listaLogradouro = enderecoFacade.filtrarLogradouroByBairroSemIndCorreio(bairro, logradouroPesquisa.getNumCep());
	
			if (!Util.isNullOuVazio(logradouro) && !Util.isNullOuVazio(logradouro.getIdeBairro())) {
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				municipio = logradouro.getIdeMunicipio();
			}
			
			if (!Util.isNullOuVazio(municipio) && !Util.isNullOuVazio(municipio.getIdeEstado())) {
				estado = municipio.getIdeEstado();
			}
			enableFormEndereco = true;
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void limparEndereco() {
		endereco = new Endereco();
		logradouro = new Logradouro();
		logradouroPesquisa = new Logradouro();
		listaLogradouro = new ArrayList<Logradouro>();
		tipoLogradouro = new TipoLogradouro();
		bairro = new Bairro();
		listaBairro = new ArrayList<Bairro>();
		municipio = new Municipio();
		estado = new Estado();
		
		obterEstadoBA();
		
		this.declaracaoInfoProjeto = new DeclaracaoInexigibilidadeInfoProjeto();
		declaracaoInfoProjeto.setEndereco(new Endereco());
		
		enableFormEndereco = true;
		showInputs = false;
		showInputLogradouro = false;
		
		this.permiteEditar = true;
		
		super.enderecoGenericoController.setCepConsultado(null);
		super.enderecoGenericoController.getEndereco().setNumEndereco(null);
		super.enderecoGenericoController.setBairro(new Bairro());
		super.enderecoGenericoController.setLogradouro(new Logradouro());
		super.enderecoGenericoController.setTipoLogradouro(new TipoLogradouro());
		super.enderecoGenericoController.setEstado(null);
		super.enderecoGenericoController.setMunicipio(new Municipio());
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
	
	public void filtrarPorCep() {
		try {
			
			if (cepModificado && !Util.isNullOuVazio(enderecoGenericoController.getCepConsultado())) {
				logradouro = new Logradouro(0);
				montaListaBairros();
				
				
				enableFormEndereco = true;
				showInputs = false;
				escolheuBairro = false;
				showInputLogradouro = false;
				bairro = new Bairro(0);
				endereco.setNumEndereco("");
				endereco.setDesComplemento("");
				endereco.setDesPontoReferencia("");
				isMunicipioSalvador = null;
				this.enableTipoLogradouro = true;
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
			if (bairroSelected.getIdeBairro() == -1) {
				enderecoGenericoController.setBairro(bairroSelected);
				escolheuBairro = true;
				showFdbairro = true;
				showInputs = false;
				showUfCidade = Boolean.TRUE;
				estado = new Estado(EstadoEnum.BAHIA.getId());
				this.enableTipoLogradouro = false;
				logradouro = new Logradouro(-1);
				enderecoGenericoController.setBairroOutroInexibilidade(true);
			}else{
				enderecoGenericoController.setBairroOutroInexibilidade(false);
			}
			
			if (!Util.isNull(bairroSelected) && bairroSelected.getIdeBairro() != 0 && bairroSelected.getIdeBairro() != -1) {
				bairro = enderecoFacade.filtrarBairroById(bairroSelected);
				listaLogradouro = enderecoFacade.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				
				if(!Util.isNullOuVazio(listaLogradouro) && listaLogradouro.size()!=0){
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
					escolheuBairro = false;
					showUfCidade = Boolean.FALSE;
					showInputs = Boolean.FALSE;
				}else{
					escolheuBairro = true;
					showUfCidade = Boolean.FALSE;
					showFdbairro = false;
				}
			}
			tipoLogradouro = null;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void changeLogradouro(ValueChangeEvent event) {
		try {
			
			enderecoGenericoController.changeLogradouro(event);
			
			Logradouro logradouroSelected = (Logradouro) event.getNewValue();
			if (logradouroSelected.getIdeLogradouro() == null || logradouroSelected.getIdeLogradouro() == -1) {//para aparecer a caixa de texto de Logradouro
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
	
	public void carregarDados(){
		if(!Util.isNull(enderecoGenericoController.getBairro())){
			bairro = enderecoGenericoController.getBairro();                 
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
		 logradouro = enderecoGenericoController.getEndereco().getIdeLogradouro();
		 if(!Util.isNullOuVazio(enderecoGenericoController.getMunicipio())){
			 municipio = enderecoGenericoController.getMunicipio();
			 estado = enderecoGenericoController.getEstado();  
		 }
	}	
	
	public void carregarDadosEnderecoProjeto(){
		 enderecoGenericoController.setBairro(bairro);                 
		 enderecoGenericoController.setEndereco(endereco);             
		 enderecoGenericoController.setEstado(estado);                 
		 enderecoGenericoController.setLogradouro(logradouro);         
		 enderecoGenericoController.setMunicipio(municipio);           
		 enderecoGenericoController.setTipoLogradouro(tipoLogradouro); 
	}

	public void changeEstado() {
		if (estado != null && !(estado.getIdeEstado() == null)) {
			try {
				listaMunicipio = (List<Municipio>) municipioService.filtrarListaMunicipiosDaBahia();//filtrarListaMunicipiosPorEstado(estado);				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		} else {
			listaMunicipio = new ArrayList<Municipio>();
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
			
			if(!Util.isNullOuVazio(endereco.getIdeLogradouro())){
				if(!Util.isNullOuVazio(endereco.getIdeLogradouro().getIdeBairro())){
					bairroClone = endereco.getIdeLogradouro().getIdeBairro();
				}
			}
			
			//enderecoGenericoController.setInexigibilidade(true);
			enderecoGenericoController.buscarEnderecoPorCep();
			endereco.setDesComplemento(descEndereco);
			endereco.setNumEndereco(numEndereco);

			carregarDados();
			
			if(!Util.isNullOuVazio(bairroClone)){
				enderecoGenericoController.setBairro(bairroClone);
				if(bairroClone.getIndOrigemCorreio() || bairroClone.isIndOrigemApi()){
					enderecoGenericoController.setBairroOutroInexibilidade(true);
				}else{
					enderecoGenericoController.setBairroOutroInexibilidade(false);
				}
			}
			
			if (!enderecoGenericoController.getListaBairro().isEmpty() && !Util.isNullOuVazio(((List<Bairro>) enderecoGenericoController.getListaBairro()).get(0))) {
				
				if(((List<Logradouro>)enderecoGenericoController.getListaLogradouro()).get(0).getIdeLogradouro() == -1){
					enderecoGenericoController.setListaBairro(new ArrayList<Bairro>());
					enderecoGenericoController.getListaBairro().add(enderecoGenericoController.getFacade().getOutroBairro());
					enderecoGenericoController.setBairro(new Bairro()); 
					enderecoGenericoController.getBairro().setIdeMunicipio(new Municipio());
				}
				
				if(!Util.isNullOuVazio(enderecoGenericoController.getBairro()) && !Util.isNullOuVazio(enderecoGenericoController.getBairro().getIdeMunicipio())){
					isEstadoBahia = enderecoGenericoController.getEstado().getId().equals(Integer.valueOf(EstadoEnum.BAHIA.getId()));
				}else{			
					logradouro.setNumCep(enderecoGenericoController.getFacade().retornarCEP(enderecoGenericoController.getCepConsultado()));
					listaLogradouro = enderecoFacade.filtrarLogradouroByCepSemIndCorreio(logradouro);
					isEstadoBahia = listaLogradouro.get(0).getMunicipio().getIdeEstado().getId().equals(Integer.valueOf(EstadoEnum.BAHIA.getId()));
					
					if(Util.isNullOuVazio(enderecoGenericoController.getListaMunicipio())){
						enderecoGenericoController.setListaMunicipio(new ArrayList<Municipio>());
						for(Logradouro log : listaLogradouro){
							enderecoGenericoController.getListaMunicipio().add(log.getIdeMunicipio());
						}
					}
					enderecoGenericoController.getFacade().listarMunicipio(enderecoGenericoController, listaLogradouro.get(0).getMunicipio().getIdeEstado());
					enderecoGenericoController.getLogradouro().setIdeMunicipio(listaLogradouro.get(0).getIdeMunicipio());
				}
			} else {
				showUfCidade = Boolean.TRUE;
			}
			
			if(Util.isNull(isEstadoBahia) || (!Util.isNull(isEstadoBahia) && !isEstadoBahia)) {
				JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
				logradouroPesquisa = new Logradouro();
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
		toReturn.add(new SelectItem(new Municipio(0), "Selecione..."));
		while (i.hasNext()) {
			Municipio municipio = (Municipio) i.next();
			toReturn.add(new SelectItem(municipio, municipio.getNomMunicipio()));
		}
		return toReturn;
	}

	public Collection<SelectItem> getValuesComboLogradouro() {
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		Iterator<Logradouro> i = listaLogradouro.iterator();
		toReturn.add(new SelectItem(new Logradouro(0), "Selecione..."));
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

	public void visualizarEndereco(DeclaracaoInexigibilidadeInfoProjeto declaracaoEndereco) {
		if(!Util.isNull(declaracaoEndereco) && !Util.isNull(declaracaoEndereco.getEndereco())) {
			declaracaoInfoProjeto = declaracaoEndereco;
			
			this.endereco = declaracaoEndereco.getEndereco();
			enderecoGenericoController.setLogradouro(endereco.getIdeLogradouro());
			carregaEndereco();
			
			if(!enderecoGenericoController.getBairro().getIndOrigemCorreio() && !enderecoGenericoController.getBairro().isIndOrigemApi()){
				enderecoGenericoController.setBairroOutroInexibilidade(true);
			}else{
				enderecoGenericoController.setBairroOutroInexibilidade(false);
			}
			
			this.enableFormEndereco = false;
			
			this.permiteEditar = false;
		}
	}
	
	public void editarEndereco(DeclaracaoInexigibilidadeInfoProjeto declaracaoEndereco) {
		if(!Util.isNull(declaracaoEndereco) && !Util.isNull(declaracaoEndereco.getEndereco())) {
			
			limparEndereco();
			
			try {
				this.endereco = enderecoFacade.carregar(declaracaoEndereco.getEndereco().getIdeEndereco());
				
				this.declaracaoInfoProjeto = declaracaoEndereco;
				
				enderecoGenericoController.setLogradouro(endereco.getIdeLogradouro());
				carregaEndereco();
				if(!enderecoGenericoController.getBairro().getIndOrigemCorreio() && !enderecoGenericoController.getBairro().isIndOrigemApi()){
					enderecoGenericoController.setBairroOutroInexibilidade(true);
				}else{
					enderecoGenericoController.setBairroOutroInexibilidade(false);
				}
			
				permiteEditar = true;
			
				this.enableFormEndereco = true;
				if(!endereco.getIdeLogradouro().getIdeBairro().getIndOrigemCorreio() && !endereco.getIdeLogradouro().getIdeBairro().isIndOrigemApi()){
					showInputs = true;
				}
				Html.atualizar("formProjeto");
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage("Não foi possível carregar os dados do Projeto.");
			}
		}
	}
	
	public void changeCep(ValueChangeEvent e){
		Integer cepSemMascara = enderecoGenericoController.getFacade().retornarCEP(e.getNewValue().toString());
		String OldValue = "";
		if(!Util.isNull(e.getOldValue())){
			OldValue = e.getOldValue().toString();
		}
		
		if(e != null && !((OldValue).equals(cepSemMascara.toString()))){
			cepModificado = true;
		}
	}
	
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

	public DeclaracaoInexigibilidadeInfoProjeto getDeclaracaoInfoProjeto() {
		return declaracaoInfoProjeto;
	}

	public void setDeclaracaoInfoProjeto(
			DeclaracaoInexigibilidadeInfoProjeto declaracaoInfoProjeto) {
		this.declaracaoInfoProjeto = declaracaoInfoProjeto;
	}

	@Override
	public void salvarEnderecoPessoaEndereco() throws Exception {
		
		
	}

	@Override
	public void enviarId() {
		super.enviarId("formProjeto");
	}

	@Override
	public void desabilitarParaVisualizacao() {
		super.enderecoGenericoController.setVisualizacao(false);
	}

	@Override
	public void prepararEnderecoGenericoController() {
		
		
	}

	
}