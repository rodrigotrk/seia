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
import br.gov.ba.seia.enumerator.EstadoEnum;
import br.gov.ba.seia.enumerator.MunicipioEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("enderecoController")
@ViewScoped
public class EnderecoController extends EstadoMunicipioController {

	private Bairro bairro;
	private Endereco endereco;
	private EnderecoPessoa enderecoPessoa;
	private Logradouro logradouro;
	private Logradouro logradouroPesquisa;
	private Pessoa pessoaJuridica;
	private Pessoa pessoaRepresentanteLegal;
	private Pessoa pessoaProcuradorPessoaJuridica;
	private TipoLogradouro tipoLogradouro;

	private List<TipoLogradouro> listTipoLogradouro;
	private List<Logradouro> listaLogradouro;
	private List<Bairro> listaBairro;
	private Pessoa pessoa;
	
	private Boolean showUfCidade = Boolean.FALSE;
	private Boolean showFdbairro = Boolean.FALSE;
	private Boolean isMunicipioSalvador;	
	private Boolean isEstadoBahia;
	private boolean escolheuBairro = false;
	private boolean consultaPJlacTransporte;
	private boolean showInputs;
	private boolean showInputLogradouro;
	private boolean enableFormEndereco;
	
	private static final int OUTRO = -1;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	@EJB
	private MunicipioService municipioService;
	
	@PostConstruct
	public void init() {
		endereco = ContextoUtil.getContexto().getRequerimentoEndereco();
		ContextoUtil.getContexto().setRequerimentoEndereco(null);
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
			enableFormEndereco = false;
		} else {
			logradouroPesquisa = endereco.getIdeLogradouro();
			montaListaBairros();
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
			enableFormEndereco = true;
		}
		pessoa = new Pessoa();
		pessoaJuridica = new Pessoa();
		pessoaRepresentanteLegal = new Pessoa();
		pessoaProcuradorPessoaJuridica = new Pessoa();
		showFdbairro = Boolean.FALSE;
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
		enableFormEndereco = false;
		pessoa = new Pessoa();
		pessoaJuridica = new Pessoa();
		pessoaRepresentanteLegal = new Pessoa();
		pessoaProcuradorPessoaJuridica = new Pessoa();
		showInputs = false;
		showInputLogradouro = false;
		consultaPJlacTransporte = false;
	}

	public String salvarEndereco() {
		salvarEndereco(pessoa);
		limparEndereco();
		return "";
	}
	
	public String salvarEnderecoSemLimpar() {
		salvarEndereco(pessoa);
		return "";
	}

	public String salvarEnderecoPJterceiros() {
		salvarEndereco(ContextoUtil.getContexto().getPessoa());
		return "";
	}
	
	public String salvarEnderecoPessoaJuridica() {
		salvarEndereco(pessoaJuridica);								
		return "";
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
	
	public String salvarEnderecoPessoaRepresentanteLegal() {
		salvarEndereco(pessoaRepresentanteLegal);
		return "";
	}

	public String salvarEnderecoPessoaProcuradorPessoaJuridica() {
		salvarEndereco(pessoaProcuradorPessoaJuridica);
		return "";
	}

	private void salvarEndereco(Pessoa pessoa) {
		try {
			
			boolean isUpdate = true;
			
			if(Util.isNullOuVazio(tipoLogradouro)) {
				JsfUtil.addWarnMessage("Selecione o tipo do logradouro.");
				return;
			}
			
			if (bairro.getIdeBairro().equals(-1)) {
				persistirDependencias();
			}
			
			if (Util.isNull(endereco.getIdeEndereco())) {
				isUpdate = false;
			}
			
			if (logradouro.getIdeLogradouro().equals(-1)) {
				persistirLogradouro();
			}
			
			if (!Util.isNull(logradouro) && logradouro.getIdeLogradouro() != 0 && logradouro.getIdeLogradouro() != -1) {
				logradouro = enderecoFacade.filtrarLogradouroById(logradouro);
			}
			
			endereco.setIdeLogradouro(logradouro);
			enderecoFacade.salvarEndereco(endereco);
			
			if (!isUpdate) {
				salvarEnderecoPessoa(pessoa);
			}
			
			JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
			enableFormEndereco = Boolean.TRUE;
			
		} catch (Exception e) {
			if (e.getMessage().contains("bairro_uk")) {
				JsfUtil.addErrorMessage("O bairro informado já existe em nossa base de dados!");
			}else if (e.getMessage().contains("endereco_logradouro_fk")) {
				JsfUtil.addErrorMessage("O logradouro informado já existe em nossa base de dados!");
			} else {
				JsfUtil.addErrorMessage(e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
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

	private void salvarEnderecoPessoa(Pessoa pessoa) {
		if (Util.isNull(enderecoPessoa)) {
			enderecoPessoa = new EnderecoPessoa();
		}
		enderecoPessoa.setIdePessoa(pessoa);
		enderecoPessoa.setIdeEndereco(endereco);
		enderecoPessoa.setIdeTipoEndereco(new TipoEndereco(TipoEnderecoEnum.RESIDENCIAL.getId()));
		List<EnderecoPessoa> lista = new ArrayList<EnderecoPessoa>();
		lista.add(enderecoPessoa);
		endereco.setEnderecoPessoaCollection(lista);
		try {
			enderecoFacade.salvarEnderecoPessoa(enderecoPessoa);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void filtrarPorCep() {
		try {
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
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void filtrarPorCepTerceiro() {
		try {
			logradouro = new Logradouro(0);
			montaListaBairrosTerceiros();
			if(Util.isNullOuVazio(bairro)){
				bairro = new Bairro(0);
				escolheuBairro = false;
			}
			endereco.setNumEndereco("");
			endereco.setDesComplemento("");
			isMunicipioSalvador = null;
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
	
			}
			if (!Util.isNull(bairroSelected) && bairroSelected.getIdeBairro() != 0 && bairroSelected.getIdeBairro() != -1) {
				bairro = enderecoFacade.filtrarBairroById(bairroSelected);
				listaLogradouro = enderecoFacade.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				
				if(!Util.isNullOuVazio(listaLogradouro)){
					showInputs = false;
					showInputLogradouro = false;
					
					if(listaLogradouro.size()==1) { 
						this.logradouro = (listaLogradouro.get(0));
						this.tipoLogradouro = logradouro.getIdeTipoLogradouro(); 
				 }
					 
				}
	
				
				municipio = bairro.getIdeMunicipio();
				isMunicipioSalvador = municipio.getIdeMunicipio().equals(MunicipioEnum.SALVADOR.getId());
				estado = municipio.getIdeEstado();
				isEstadoBahia = estado.getIdeEstado().equals(EstadoEnum.BAHIA.getId());
				
				if(estado != null && !isEstadoBahia) {
					JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
					estado = new Estado(0);

					municipio = new Municipio();
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
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}
	
	public void changeLogradouro(ValueChangeEvent event) {
		try {
			Logradouro logradouroSelected = (Logradouro) event.getNewValue();
			if (logradouroSelected.getIdeLogradouro() == OUTRO) {//para aparecer a caixa de texto de Logradouro
				showInputs = false;
				showInputLogradouro = true;
				logradouro.setIdeLogradouro(-1);
				tipoLogradouro.setIdeTipoLogradouro(-1);
			}
			if (!Util.isNull(logradouroSelected) && logradouroSelected.getIdeLogradouro() != 0 && logradouroSelected.getIdeLogradouro() != -1) {
				logradouro = enderecoFacade.filtrarLogradouroById(logradouroSelected);
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				showInputLogradouro = false;
				showInputs = false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
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
			listaBairro = new ArrayList<Bairro>();
			listaBairro = enderecoFacade.filtrarBairroByCep(logradouroPesquisa);
			
			if(!Util.isNullOuVazio(logradouroPesquisa.getNumCep())){
				Municipio tentaCarregar = municipioService.obterMunicipioPorCep(logradouroPesquisa.getNumCep());
				
				if(!Util.isNullOuVazio(tentaCarregar)){
					municipio = municipioService.obterMunicipioPorCep(logradouroPesquisa.getNumCep());
					
					if(!Util.isNullOuVazio(municipio.getIdeEstado()) && !Util.isNullOuVazio(municipio.getIdeEstado().getDesSigla()) && !"BA".equals(municipio.getIdeEstado().getDesSigla())) {
						JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
					}
				}
				
			}else if (Util.isNullOuVazio(logradouroPesquisa)){
				municipio = null;
			}
			
			if (!listaBairro.isEmpty() && !Util.isNullOuVazio(listaBairro.get(0))) {
				if(!Util.isNullOuVazio(municipio) && !Util.isNullOuVazio(municipio.getIdeMunicipio())){
					isMunicipioSalvador = (municipio.getIdeMunicipio().equals(MunicipioEnum.SALVADOR.getId()));					
				}else{					
					isMunicipioSalvador = null;
				}
				showUfCidade = Boolean.FALSE;
			} else {
				showUfCidade = Boolean.TRUE;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	private void montaListaBairrosTerceiros() {
		try {
			if(!Util.isNullOuVazio(logradouroPesquisa.getNumCep())){
				listaBairro = new ArrayList<Bairro>();
				listaBairro = enderecoFacade.filtrarBairroByCep(logradouroPesquisa);
				if (!listaBairro.isEmpty() && !Util.isNullOuVazio(listaBairro.get(0))) {
					if(listaBairro.size() == 1){
						bairro = listaBairro.get(0);
						escolheuBairro = true;
						showInputs = false;
						showUfCidade = false;
						showFdbairro = false;
					}
					municipio = listaBairro.get(0).getIdeMunicipio();
					estado = municipio.getIdeEstado();
					if(!Util.isNullOuVazio(municipio)){
						isMunicipioSalvador = (municipio.getIdeMunicipio().equals(837));
					}else{
						isMunicipioSalvador = null;
						showUfCidade = false;
					}
				} 
				else {
					if(Util.isNullOuVazio(bairro)){
						bairro = new Bairro(-1);
					}
					showFdbairro = true;
					showUfCidade = true;
				}
				listaLogradouro = enderecoFacade.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				if(listaLogradouro.size() == 1){
					logradouro = listaLogradouro.get(0);
					tipoLogradouro = new TipoLogradouro();
					tipoLogradouro = logradouro.getIdeTipoLogradouro();
					showInputLogradouro = false;
				}
				else if(listaLogradouro.isEmpty()){
					logradouro = new Logradouro();
					showInputLogradouro = true;
				}
				enableFormEndereco = true;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<SelectItem> getValuesComboBairro() {
		
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		Iterator<Bairro> i = listaBairro.iterator();
		toReturn.add(new SelectItem(null, "Selecione..."));
		while (i.hasNext()) {
			Bairro bairroObj = i.next();
			toReturn.add(new SelectItem(bairroObj, bairroObj.getNomBairro()));
		}
		toReturn.add(new SelectItem(new Bairro(-1), "Outro"));
		return toReturn;
	}
	
	public List<Logradouro> getListLogradouro() {
		try {
			if(bairro != null) {
				
				if(!Util.isNullOuVazio(bairro.getIdeBairro())) {
					
						if(bairro.getIdeBairro() != OUTRO) {
							
							Bairro bairroTemp = enderecoFacade.filtrarBairroById(bairro);
							
							if(!Util.isNull(bairroTemp)) {
								listaLogradouro = enderecoFacade.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
								listaLogradouro.add(new Logradouro(OUTRO, "Outro", null, false, null));
								
								if(!Util.isNullOuVazio(this.logradouro) && this.logradouro.getIdeLogradouro() != 0) {
									
									if(listaLogradouro.size()==1) {
										this.logradouro = (listaLogradouro.get(0));
										this.tipoLogradouro = logradouro.getIdeTipoLogradouro();
									}
								} else {
									this.logradouro = new Logradouro(0);
								}
							}
						}
				} else {
					listaLogradouro = new ArrayList<Logradouro>();
				}
			}
		} catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return listaLogradouro;
	}
	
	public List<TipoLogradouro> getListTipoLogradouro() {

			try {
				listTipoLogradouro = enderecoFacade.listarTipoLogradouro();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		
		return listTipoLogradouro;
	}
	
	
	
	public void carregarEnderecoPJterceiros(Pessoa pessoa) {
		consultaPJlacTransporte = true;
		carregarEnderecoPessoa(pessoa);
	}
	
	public void carregarEnderecoPessoa() {
		carregarEnderecoPessoa(pessoa);
	}

	public void carregarEnderecoPessoaJuridica() {
		if (Util.isNull(pessoaJuridica) || Util.isNull(pessoaJuridica.getIdePessoa())) {
			tratarPessoaJuridicaSessao();
		}
		carregarEnderecoPessoa(pessoaJuridica);
	}

	public void carregarEnderecoPessoaRepresentanteLegal() {
		carregarEnderecoPessoa(pessoaRepresentanteLegal);
	}

	public void carregarEnderecoPessoaProcuradorPessoaJuridica(Pessoa pessoaProcuradorPessoaJuridica) {
		carregarEnderecoPessoa(pessoaProcuradorPessoaJuridica);
	}
	
	public void carregarEnderecoPessoaProcuradorPessoaJuridica() {
		carregarEnderecoPessoa(pessoaProcuradorPessoaJuridica);
	}

	private void carregarEnderecoPessoa(Pessoa pessoa) {
		try {
			
			enderecoPessoa = enderecoFacade.filtrarEnderecoByPessoa(pessoa);
			
			if (!Util.isNull(enderecoPessoa)) {
				endereco = enderecoPessoa.getIdeEndereco();
				logradouro = enderecoFacade.filtrarLogradouroById(endereco.getIdeLogradouro());
			
				if (!Util.isNotNullAndTrue(logradouro.getIndOrigemCorreio()) && !Util.isNotNullAndTrue(logradouro.getIndOrigemApi())) {
					showInputLogradouro = true;
					showInputs = true;
				}
				
				bairro = enderecoFacade.filtrarBairroById(logradouro.getIdeBairro());
				
				municipio = bairro.getIdeMunicipio();
				estado = municipio.getIdeEstado();
				logradouroPesquisa.setNumCep(logradouro.getNumCep());
				
				if(isConsultaPJlacTransporte()){
					montaListaBairrosTerceiros();
				} else {
					montaListaBairros();
				}
				
				listaLogradouro = enderecoFacade.filtrarLogradouroByBairro(bairro, logradouroPesquisa.getNumCep());
				tipoLogradouro = logradouro.getIdeTipoLogradouro();
				enableFormEndereco = true;
				
				if (!bairro.getIndOrigemCorreio() && !bairro.isIndOrigemApi()) {
					showFdbairro = Boolean.TRUE;
					bairro.setIdeBairro(-1);
				} else {
					showFdbairro = Boolean.FALSE;
				}
			} else {
				endereco = new Endereco();
				logradouro = new Logradouro();
				bairro = new Bairro();
				estado = new Estado();
				logradouroPesquisa = new Logradouro();
				listaLogradouro = new ArrayList<Logradouro>();
				tipoLogradouro = new TipoLogradouro();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void tratarPessoaJuridicaSessao() {
		if (!Util.isNull(ContextoUtil.getContexto().getPessoaJuridica())) {
			pessoaJuridica = ContextoUtil.getContexto().getPessoaJuridica().getPessoa();
			if (Util.isNull(pessoaJuridica)) {
				pessoaJuridica = new Pessoa();
			}
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

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
		if (!Util.isNullOuVazio(pessoa) && !Util.isNullOuVazio(pessoa.getIdePessoa())) {
			carregarEnderecoPessoa();
		} else {
			limparEndereco();
		}
	}

	public Pessoa getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(Pessoa pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
		carregarEnderecoPessoaJuridica();
	}

	public Pessoa getPessoaRepresentanteLegal() {
		return pessoaRepresentanteLegal;
	}

	public void setPessoaRepresentanteLegal(Pessoa pessoaRepresentanteLegal) {
		this.pessoaRepresentanteLegal = pessoaRepresentanteLegal;
	}

	public Pessoa getPessoaProcuradorPessoaJuridica() {
		return pessoaProcuradorPessoaJuridica;
	}

	public void setPessoaProcuradorPessoaJuridica(Pessoa pessoaProcuradorPessoaJuridica) {
		this.pessoaProcuradorPessoaJuridica = pessoaProcuradorPessoaJuridica;
	}

	public EnderecoPessoa getEnderecoPessoa() {
		return enderecoPessoa;
	}

	public void setEnderecoPessoa(EnderecoPessoa enderecoPessoa) {
		this.enderecoPessoa = enderecoPessoa;
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

	public boolean isConsultaPJlacTransporte() {
		return consultaPJlacTransporte;
	}

	public void setConsultaPJlacTransporte(boolean consultaPJlacTransporte) {
		this.consultaPJlacTransporte = consultaPJlacTransporte;
	}

	public Boolean getIsEstadoBahia() {
		return isEstadoBahia;
	}

	public void setIsEstadoBahia(Boolean isEstadoBahia) {
		this.isEstadoBahia = isEstadoBahia;
	}
}
