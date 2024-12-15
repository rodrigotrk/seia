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
import br.gov.ba.seia.entity.DeclaracaoTransporteResiduoEndereco;
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
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ServicoDeCepService;
import br.gov.ba.seia.service.TipoLogradouroService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("declaracaoEnderecoDestinacaoResiduoController")
@ViewScoped
public class DeclaracaoEnderecoDestinacaoResiduoController {

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
	private List<TipoLogradouro> listTipoLogradouro;
	
	private Boolean showUfCidade = Boolean.FALSE;
	private Boolean showFdbairro = Boolean.FALSE;
	private Boolean isMunicipioSalvador;	
	private Boolean isEstadoBahia;
	private boolean escolheuBairro = false;
	private boolean showInputs;
	private boolean showInputLogradouro;
	private boolean enableFormEndereco;
	private boolean enableTipoLogradouro = false;
	
	private boolean permiteEditar;
	private boolean permiteEditarNumProcesso = true;
	
	private DeclaracaoTransporteResiduoEndereco enderecoSelecionado;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private BairroService bairroService;
	
	@EJB
	private EstadoService estadoService;
	
	@EJB
	private TipoLogradouroService tipoLogradouroService;
	
	@EJB
	private ServicoDeCepService cepService;
	
	private Collection<SelectItem> listaBairroSelectItem;
	private Collection<SelectItem> listaLogradouroSelectItem;
	private Collection<SelectItem> listaTipoLogradouroSelectItem;
	
	@PostConstruct
	public void init() {
		if (Util.isNull(endereco)) {
			if(Util.isNull(enderecoSelecionado)) { 
				enderecoSelecionado = new DeclaracaoTransporteResiduoEndereco();
				enderecoSelecionado.setIndPossuiLicencaAutorizacao(Boolean.FALSE);
			}
			
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
			
			montarListaBairro();
			
			montarListaLogradouro();
			
			montarListaTipoLogradouro();
			
			enableFormEndereco = false;
			
			this.permiteEditar = true;
			
			this.permiteEditarNumProcesso = false;
		} else {
			carregaEndereco();
            if(!this.listaBairro.contains(bairro)) {
                this.listaBairro.add(bairro);
            }
			
			montarListaBairro();
			montarListaLogradouro();
			montarListaTipoLogradouro();
			
			this.permiteEditar = false;
			this.permiteEditarNumProcesso = true;
		}
		showFdbairro = Boolean.FALSE;
	}
	
	private void obterEstadoBA() {
		try {
			estado = estadoService.buscar(new Estado(EstadoEnum.BAHIA.getId()));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
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
	
	private void montarListaTipoLogradouro() {
		listaTipoLogradouroSelectItem = new ArrayList<SelectItem>();
		
		listaTipoLogradouroSelectItem.add(new SelectItem(null, "Selecione..."));
		
		if(!Util.isNullOuVazio(this.listTipoLogradouro)) {
			for (TipoLogradouro tipologradouro : listTipoLogradouro) {
				listaTipoLogradouroSelectItem.add(new SelectItem(tipologradouro, tipologradouro.getNomTipoLogradouro()));
			}
		}
	}

	private void carregaEndereco() {
		logradouroPesquisa = endereco.getIdeLogradouro();
		montaListaBairros();
		
		logradouro = endereco.getIdeLogradouro();
		if (!Util.isNullOuVazio(logradouro) && !Util.isNullOuVazio(logradouro.getIdeBairro()))
			bairro = logradouro.getIdeBairro();
		try {
			listaLogradouro = enderecoFacade.filtrarLogradouroByBairroSemIndCorreio(bairro, logradouroPesquisa.getNumCep());
			listTipoLogradouro = (List<TipoLogradouro>) tipoLogradouroService.listarTipoLogradouro();
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
		enableFormEndereco = true;
	}

	public void limparEndereco() {
		if(Util.isNull(enderecoSelecionado)) { 
			enderecoSelecionado = new DeclaracaoTransporteResiduoEndereco();
			enderecoSelecionado.setIndPossuiLicencaAutorizacao(Boolean.FALSE);
		}
		
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
		
		montarListaBairro();
		
		montarListaLogradouro();
		
		enableFormEndereco = false;
		showInputs = false;
		showInputLogradouro = false;
		
		this.permiteEditar = true;
		
		this.permiteEditarNumProcesso = false;
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
	
	public void salvarEnderecoRequerimento() {
		try {
			String msg = "Inclusão efetuada com sucesso";
			if (bairro.getIdeBairro().equals(-1)) {
				persistirDependencias();
			}
			if (logradouro.getIdeLogradouro().equals(-1)) {
				persistirLogradouro();
			}
			endereco.setIdeLogradouro(logradouro);
			
			if(!Util.isNull(endereco.getIdeEndereco())){
				msg = "Alteração efetuada com sucesso";
			}
			enderecoFacade.salvarEndereco(endereco);
			JsfUtil.addSuccessMessage(msg);
			ContextoUtil.getContexto().setRequerimentoEndereco(endereco);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void filtrarPorCep() {
		try {
			if (!Util.isNullOuVazio(logradouroPesquisa.getNumCep())) {
				montaListaBairros();
				
				if(Util.isNull(isEstadoBahia) || (!Util.isNull(isEstadoBahia) && !isEstadoBahia)) {
					if(logradouroPesquisa.getNumCep() < 40000000 || logradouroPesquisa.getNumCep() > 48999999){
						JsfUtil.addErrorMessage("CEP não localizado.");
					} else {
						JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
					}
					logradouroPesquisa = new Logradouro();
					tipoLogradouro = new TipoLogradouro();
					municipio = new Municipio();
					enableFormEndereco = false;						
					Html.atualizar("tabViewDTRP:formDeclaracao");
					return;
				}

				
				logradouro = new Logradouro(0);
				enableFormEndereco = true;
				showInputs = false;
				escolheuBairro = false;
				showInputLogradouro = false;
				bairro = new Bairro(0);
				endereco.setNumEndereco("");
				endereco.setDesComplemento("");
				endereco.setDesPontoReferencia("");
				isMunicipioSalvador = null;
				this.enableTipoLogradouro = false;
				
				montarListaBairro();
				montarListaTipoLogradouro();
			}else{
				logradouro = new Logradouro(0);
				tipoLogradouro = new TipoLogradouro(0);
				enableFormEndereco = false;
				showInputs = false;
				escolheuBairro = false;
				showInputLogradouro = false;
				bairro = new Bairro(0);
				estado = new Estado(0);
				municipio =  new Municipio(0);
				endereco.setNumEndereco("");
				endereco.setDesComplemento("");
				endereco.setDesPontoReferencia("");
				isMunicipioSalvador = null;
				this.enableTipoLogradouro = false;
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void changeLogradouroMunicipio(ValueChangeEvent event) {
		try {
			
			Bairro bairroSelected = (Bairro) event.getNewValue();
			if (bairroSelected.getIdeBairro() == -1) {
				bairro = new Bairro();
				escolheuBairro = false;
				showFdbairro = true;
				showInputs = Boolean.TRUE;
				showUfCidade = Boolean.TRUE;
				estado = new Estado(0);
				this.permiteEditarNumProcesso = false;
			}
			if (!Util.isNull(bairroSelected) && bairroSelected.getIdeBairro() != 0 && bairroSelected.getIdeBairro() != -1) {
				bairro = enderecoFacade.filtrarBairroById(bairroSelected);
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
				
				this.permiteEditarNumProcesso = true;
				
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
	
	public void carregarMunicipio() {
		try {
			
			if(bairro == null) {
				bairro = new Bairro();
				tipoLogradouro = new TipoLogradouro();
				logradouro = new Logradouro();
				escolheuBairro = false;
				showFdbairro = false;
				showInputs = Boolean.FALSE;
				showUfCidade = Boolean.FALSE;
				estado = new Estado(0);
				this.permiteEditarNumProcesso = false;
			}
			
			if(bairro != null && bairro.getIdeBairro() != null) {
				if (bairro.getIdeBairro() == -1) {
					tipoLogradouro = new TipoLogradouro();
					
					logradouro = new Logradouro(-1);
					

					escolheuBairro = false;
					showFdbairro = true;
					showInputs = Boolean.TRUE;
					showUfCidade = Boolean.TRUE;

					
					this.enableTipoLogradouro = true;
					this.permiteEditarNumProcesso = true;
					listTipoLogradouro = (List<TipoLogradouro>) tipoLogradouroService.listarTipoLogradouro();
					montarListaTipoLogradouro();
					
				} else if (bairro.getIdeBairro() != 0 && bairro.getIdeBairro() != -1) {
					bairro = enderecoFacade.filtrarBairroById(bairro);
					enableTipoLogradouro = false;
					tipoLogradouro = new TipoLogradouro();
					logradouro = new Logradouro();
					listaLogradouro = enderecoFacade.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
					listTipoLogradouro = (List<TipoLogradouro>) tipoLogradouroService.listarTipoLogradouro();
					montarListaLogradouro();
					montarListaTipoLogradouro();
					
					if(!Util.isNullOuVazio(listaLogradouro) && listaLogradouro.size()!=0){
						showInputs = false;
						showInputLogradouro = false;
						logradouro = new Logradouro();
					}
					
					municipio = bairro.getIdeMunicipio();
					isMunicipioSalvador = municipio.getIdeMunicipio().equals(MunicipioEnum.SALVADOR.getId());
					estado = municipio.getIdeEstado();
					isEstadoBahia = estado.getIdeEstado().equals(EstadoEnum.BAHIA.getId());
					
					this.permiteEditarNumProcesso = true;
					
					if(estado != null && !isEstadoBahia) {
						JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
						estado = new Estado(0);
						municipio = new Municipio(0);
						tipoLogradouro = new TipoLogradouro();
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
			}
			tipoLogradouro = null;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void changeLogradouro() {
		try {
			if(Util.isNull(logradouro)) {
				logradouro = new Logradouro();
				tipoLogradouro = new TipoLogradouro();
			}
			
			if (!Util.isNull(logradouro) && !Util.isNull(logradouro.getIdeLogradouro()) && logradouro.getIdeLogradouro() == -1) {//para aparecer a caixa de texto de Logradouro
				showInputs = false;
				showInputLogradouro = true;
				logradouro.setIdeLogradouro(-1);
				
				this.tipoLogradouro = new TipoLogradouro();
				
				this.enableTipoLogradouro = true; 
			}
			if (!Util.isNull(logradouro) && !Util.isNull(logradouro.getIdeLogradouro()) && logradouro.getIdeLogradouro() != 0 && logradouro.getIdeLogradouro() != -1) {
				logradouro = enderecoFacade.filtrarLogradouroById(logradouro);
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				showInputLogradouro = false;
				this.enableTipoLogradouro = false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void changeEstado() {
		if (estado != null && !(estado.getIdeEstado() == null)) {
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
			enderecoFacade.salvarBairro(bairro);
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
			enderecoFacade.salvarLogradouro(logradouro);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void montaListaBairros() {
		try {
			cepService.carregar(logradouroPesquisa.getNumCep().toString());
			cepService.montarEstrutura();
			listaBairro = new ArrayList<Bairro>();
			
			isEstadoBahia = null;
			if(cepService.getMunicipioEnt()!=null) {
				municipio = cepService.getMunicipioEnt();
				isEstadoBahia = (cepService.getMunicipioEnt().getIdeEstado().getIdeEstado().equals(Integer.valueOf(EstadoEnum.BAHIA.getId())));
			}
			
			
			if (cepService.getBairroEnt()!=null) {
				listaBairro.add(cepService.getBairroEnt());
				Bairro bairro = listaBairro.get(0);
				
				showUfCidade = Boolean.FALSE;
			} else {
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
		toReturn.add(new SelectItem(new Municipio(0), "Selecione..."));
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

	public void visualizarEnderecoGeradorResiduo(DeclaracaoTransporteResiduoEndereco declaracaoEndereco) {
		if(!Util.isNull(declaracaoEndereco) && !Util.isNull(declaracaoEndereco.getEndereco())) {
			enderecoSelecionado = declaracaoEndereco;
			
			this.endereco = declaracaoEndereco.getEndereco();
			
			carregaEndereco();
			
			this.enableFormEndereco = false;
			
			this.permiteEditar = false;
			
			this.permiteEditarNumProcesso = false;
		}
	}
	
	public void editarEnderecoGeradorResiduo(DeclaracaoTransporteResiduoEndereco declaracaoEndereco) {
		if(!Util.isNull(declaracaoEndereco) && !Util.isNull(declaracaoEndereco.getEndereco())) {
			this.endereco = declaracaoEndereco.getEndereco();
			this.enderecoSelecionado = declaracaoEndereco;
			
			carregaEndereco();
			
			permiteEditar = false;
			
			permiteEditarNumProcesso = true;
			
			this.enableFormEndereco = false;
			
		}
	}
	
	public void mudarNumProcesso() {
		if(!enderecoSelecionado.getIndPossuiLicencaAutorizacao()) {
			enderecoSelecionado.setNumProcessoLicencaAutorizacao(null);
		}
	}
	
	public void changePossuiLicenca(){
		this.permiteEditarNumProcesso = !permiteEditarNumProcesso;
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

	public boolean isPermiteEditarNumProcesso() {
		return permiteEditarNumProcesso;
	}

	public void setPermiteEditar(boolean permiteEditar) {
		this.permiteEditar = permiteEditar;
	}

	public void setPermiteEditarNumProcesso(boolean permiteEditarNumProcesso) {
		this.permiteEditarNumProcesso = permiteEditarNumProcesso;
	}

	public DeclaracaoTransporteResiduoEndereco getEnderecoSelecionado() {
		return enderecoSelecionado;
	}

	public void setEnderecoSelecionado(
			DeclaracaoTransporteResiduoEndereco enderecoSelecionado) {
		this.enderecoSelecionado = enderecoSelecionado;
	}

	public boolean isEnableTipoLogradouro() {
		return enableTipoLogradouro;
	}

	public void setEnableTipoLogradouro(boolean enableTipoLogradouro) {
		this.enableTipoLogradouro = enableTipoLogradouro;
	}
	
	public boolean getDisableNumeroProcesso() {
		return (this.permiteEditarNumProcesso || this.enderecoSelecionado.getIndPossuiLicencaAutorizacao());
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

	public void setListaLogradouroSelectItem(Collection<SelectItem> listaLogradouroSelectItem) {
		this.listaLogradouroSelectItem = listaLogradouroSelectItem;
	}

	public List<TipoLogradouro> getListTipoLogradouro() {
		return listTipoLogradouro;
	}

	public void setListTipoLogradouro(List<TipoLogradouro> listTipoLogradouro) {
		this.listTipoLogradouro = listTipoLogradouro;
	}

	public Collection<SelectItem> getListaTipoLogradouroSelectItem() {
		return listaTipoLogradouroSelectItem;
	}

	public void setListaTipoLogradouroSelectItem(Collection<SelectItem> listaTipoLogradouroSelectItem) {
		this.listaTipoLogradouroSelectItem = listaTipoLogradouroSelectItem;
	}
}